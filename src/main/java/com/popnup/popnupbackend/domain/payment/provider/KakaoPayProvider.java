package com.popnup.popnupbackend.domain.payment.provider;

import com.popnup.popnupbackend.domain.payment.dto.request.KakaoPayApproveRequest;
import com.popnup.popnupbackend.domain.payment.dto.request.KakaoPayOrderRequest;
import com.popnup.popnupbackend.domain.payment.dto.request.KakaoPayReadyRequest;
import com.popnup.popnupbackend.domain.payment.dto.response.KakaoPayApproveResponse;
import com.popnup.popnupbackend.domain.payment.dto.response.KakaoPayReadyResponse;
import com.popnup.popnupbackend.domain.reservation.entity.Reservation;
import com.popnup.popnupbackend.domain.reservation.exception.ReservationErrorCode;
import com.popnup.popnupbackend.domain.reservation.repository.ReservationRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class KakaoPayProvider {
  // 카카오페이 서버에 결제 요청해주는 담당자

  private final RestTemplate restTemplate;
  private final ReservationRepository reservationRepository;

  // restTempalte == 다른 서버에 http 요청을 보내는 도구, Rest 방식으로 Api를 호출할 수 있는 spring 내장 클래스

  @Value("${kakaopay.secretKey}")
  private String secretKey;

  @Value("${kakaopay.cid}")
  private String cid;

  // 카카오페이에 결제 준비 요청을 보내고 카카오페이가 보내준 결과 반환
  public KakaoPayReadyResponse ready(KakaoPayOrderRequest request) {
    Reservation reservation =
        reservationRepository
            .findById(request.getReservationId())
            .orElseThrow(ReservationErrorCode.RESERVATION_NOT_FOUND::toException);
    // 서버에 보낼 결제 준비 정보
    KakaoPayReadyRequest kakaoPayReadyRequest =
        KakaoPayReadyRequest.builder()
            .cid(cid)
            .partnerOrderId(reservation.getReservationNumber())
            .partnerUserId(String.valueOf(reservation.getMember().getId()))
            .itemName(request.getItemName())
            .quantity(request.getQuartity())
            .totalAmount(request.getTotalPrice())
            .taxFreeAmount("0")
            .approvalUrl("http://localhost:8080/api/v1/kakao-pay/approve")
            .cancelUrl("http://localhost:8080/api/v1/kakao-pay/cancel")
            .failUrl("http://localhost:8080/kakao-pay/fail")
            .build();

    HttpEntity<KakaoPayReadyRequest> entity = new HttpEntity<>(kakaoPayReadyRequest, getHeaders());
    // HTTP 요청에 필요한 Body랑 header 묶음

    // rest api 호출 이후 응답받을 때까지 기다리는 동기 방식
    // .class -> "이 클래스의 타입 정보를 주는 것"
    ResponseEntity<KakaoPayReadyResponse> response =
        restTemplate.postForEntity(
            "https://open-api.kakaopay.com/online/v1/payment/ready",
            entity,
            KakaoPayReadyResponse.class);

    // 카카오페이가 발급해준 tid(결제 거래 ID) 세션에 저장
    SessionProvider.addAttribute("tid", Objects.requireNonNull(response.getBody()).getTid());

    return response.getBody();
  }

  // apporove API : 결제 성공시 자동으로 호출되는 결제 승인 api
  public KakaoPayApproveResponse approve(String pgToken) {

    Long reservationId = SessionProvider.getLongAttribute("reservationId");

    Reservation reservation =
        reservationRepository
            .findById(reservationId)
            .orElseThrow(ReservationErrorCode.RESERVATION_NOT_FOUND::toException);

    KakaoPayApproveRequest request =
        KakaoPayApproveRequest.builder()
            .cid(cid)
            .tid(SessionProvider.getStringAttribute("tid"))
            .partnerOrderId(reservation.getReservationNumber())
            .partnerUserId(String.valueOf(reservation.getMember().getId()))
            .pgToken(pgToken)
            .build();

    HttpEntity<KakaoPayApproveRequest> entity = new HttpEntity<>(request, getHeaders());

    ResponseEntity<KakaoPayApproveResponse> response =
        restTemplate.postForEntity(
            "https://open-api.kakaopay.com/online/v1/payment/approve",
            entity,
            KakaoPayApproveResponse.class);

    return response.getBody();
  }

  // 카카오페이 api를 호출할 때 필요한 인증정보와 데이터 형식 header에 넣음
  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "SECRET_KEY " + secretKey);
    headers.add("Content-type", "application/json");
    return headers;
  }
}
