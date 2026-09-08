package com.popnup.popnupbackend.domain.payment.provider;

import java.util.Objects;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class SessionProvider {
  // 카카오페이의 tid를 결제준비에서 응답 받은 후 결제 승인으로 tid 값을 넘겨주기 위해 session에 저장할 때 사용
  // 즉 로그인한 사용자의 http 세션에 값을 넣고 나중에 꺼내는 도구, session을 쉽게 사용하기 위한 도우미 클래스

  public static void addAttribute(String key, Object value) {
    Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
        .setAttribute(key, value, RequestAttributes.SCOPE_SESSION);
  }

  // 세션에 값 저장
  // requestContextHolder : 현재 요청 정보를 가져오란 의미 , scope_session : 값을 세션 영역에 저장하란 뜻
  // requireNonNull 같은 경우에는 null이면 안됨 -> Null이면 오류 냄

  public static Object getAttribute(String key) {
    return Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
        .getAttribute(key, RequestAttributes.SCOPE_SESSION);
  }

  // 세션에 값 꺼냄

  public static String getStringAttribute(String key) {
    return (String) getAttribute(key);
  }

  // 세션에서 꺼낸 값을 string으로 변환
  public static Long getLongAttribute(String key) {
    return (Long) getAttribute(key);
  }
  // 세션에서 꺼낸 값을 Long으로 변환
}
