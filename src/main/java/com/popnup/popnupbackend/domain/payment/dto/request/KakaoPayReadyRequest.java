package com.popnup.popnupbackend.domain.payment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoPayReadyRequest {

  private String cid;
  private String partnerOrderId;
  private String partnerUserId;
  private String itemName;
  private String quantity;
  private String totalAmount;
  private String taxFreeAmount;
  private String approvalUrl;
  private String cancelUrl;
  private String failUrl;
}
