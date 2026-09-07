package com.popnup.popnupbackend.domain.payment.dto.request;

import lombok.Getter;

@Getter
public class KakaoPayOrderRequest {

  private String itemName;
  private String quartity;
  private String totalPrice;
}
