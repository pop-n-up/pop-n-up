package com.popnup.popnupbackend.domain.popup.service;

import com.popnup.popnupbackend.domain.popup.dto.naver.NaverGeocodeResponse;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class NaverGeocodeService {

  @Value("${naver.maps.client-id:xuw6b5ypsr}")
  private String clientId;

  @Value("${naver.maps.client-secret:xYpDojd2SU54of2IboHi0fIRDOSr2J96LI9RKnYZ}")
  private String clientSecret;

  private static final String GEOCODE_URL =
      "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
  private final RestClient restClient = RestClient.create();

  public NaverGeocodeResponse.AddressItem getCoordinatesByAddress(String address) {
    log.info("[Geocoding 요청] 주소: {}, Key-ID: {}", address, clientId);

    try {
      URI uri =
          UriComponentsBuilder.fromUriString(GEOCODE_URL)
              .queryParam("query", address)
              .build()
              .encode()
              .toUri();

      NaverGeocodeResponse response =
          restClient
              .get()
              .uri(uri)
              .header("x-ncp-apigw-api-key-id", clientId.trim())
              .header("x-ncp-apigw-api-key", clientSecret.trim())
              .header("Accept", MediaType.APPLICATION_JSON_VALUE)
              .retrieve()
              .body(NaverGeocodeResponse.class);

      if (response != null
          && "OK".equals(response.getStatus())
          && response.getAddresses() != null
          && !response.getAddresses().isEmpty()) {
        log.info(
            "[Geocoding 성공] 위도: {}, 경도: {}",
            response.getAddresses().get(0).getLatitude(),
            response.getAddresses().get(0).getLongitude());
        return response.getAddresses().get(0);
      } else {
        log.warn(
            "[Geocoding 응답 비어있음] status: {}", response != null ? response.getStatus() : "null");
      }
    } catch (Exception e) {
      log.error("[Geocoding 실패] 에러 메시지: {}", e.getMessage(), e);
    }

    return null;
  }
}
