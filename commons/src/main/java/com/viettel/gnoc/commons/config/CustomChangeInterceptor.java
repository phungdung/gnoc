package com.viettel.gnoc.commons.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Getter
@Setter
public class CustomChangeInterceptor extends HandlerInterceptorAdapter {

  public static final String DEFAULT_TIME_ZONE_NAME = "utcOffset";
  public static final String DEFAULT_TIME_ZONE_VALUE = "0";
  private String timeZoneName = DEFAULT_TIME_ZONE_NAME;
  private String timeZoneValue = DEFAULT_TIME_ZONE_VALUE;

  public static final String DEFAULT_TOKEN_NAME = "token";
  public static final String DEFAULT_TOKEN_VALUE = "";
  private String tokenName = DEFAULT_TOKEN_NAME;
  private String tokenValue = DEFAULT_TOKEN_VALUE;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {

    String newOffset = request.getParameter(getTimeZoneName());
    if (newOffset != null) {
      TimezoneContextHolder.setOffset(newOffset);
    } else {
      TimezoneContextHolder.setOffset(getTimeZoneValue());
    }

    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String authToken = authHeader.substring(7);
      TokenContextHolder.setToken(authToken);
    } else {
      TokenContextHolder.setToken(getTokenValue());
    }

    return true;
  }
}
