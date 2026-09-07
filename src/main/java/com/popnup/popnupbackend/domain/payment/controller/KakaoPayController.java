package com.popnup.popnupbackend.domain.payment.controller;

import com.popnup.popnupbackend.domain.payment.provider.KakaoPayProvider;
import com.popnup.popnupbackend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/kakao-pay")
public class KakaoPayController {

    private final KakaoPayProvider kakaoPayProvider;

    @PostMapping("/ready")
    public ResponseEntity<ApiResponse<KakaoPayReadyResponse>> ready(@RequestBody KakaoPayOrderReqeust reqeust) {
        return ResponseEntity.ok(ApiResponse.success(kakaoPayProvider.ready(reqeust)));
    }
}
