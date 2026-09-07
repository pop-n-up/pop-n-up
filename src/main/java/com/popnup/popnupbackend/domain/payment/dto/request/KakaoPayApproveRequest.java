package com.popnup.popnupbackend.domain.payment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoPayApproveRequest {

  private String cid;
  private String tid;
  private String partnerOrderId;
  private String partnerUserId;
  private String pgToken;
}
