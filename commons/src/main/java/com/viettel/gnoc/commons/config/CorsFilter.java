package com.viettel.gnoc.commons.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CorsFilter implements Filter {

  public CorsFilter() {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    final HttpServletResponse response = (HttpServletResponse) res;
    final HttpServletRequest request = (HttpServletRequest) req;
    String referer = request.getHeader("referer");
    if (referer != null && referer.contains("swagger-ui.html")) {
      response.setHeader("Access-Control-Allow-Origin", "*");
      response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
      response.setHeader("Access-Control-Allow-Headers",
          "x-requested-with, authorization, origin, content-type, accept");
      response.setHeader("Access-Control-Max-Age", "3600");
    }
    if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      logRequest(req);
      chain.doFilter(req, res);
      logResponse(res);
    }
  }

  @Override
  public void destroy() {
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
  }

  private void logRequest(ServletRequest req) {
    final HttpServletRequest request = (HttpServletRequest) req;
    log.info("===========LOG REQUEST===========");
    log.info(request.getRequestURI());
  }

  private void logResponse(ServletResponse resp) {
    final HttpServletResponse response = (HttpServletResponse) resp;
    log.info("===========LOG RESPONSE===========");
    log.info(response.getContentType());
  }
}
