package com.popnup.popnupbackend.domain.payment.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoPayOrderRequest {

    private String itemName;
    private String quartity;
    private String totalPrice;
}
