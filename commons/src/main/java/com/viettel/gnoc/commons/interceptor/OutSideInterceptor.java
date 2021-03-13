package com.viettel.gnoc.commons.interceptor;

import com.viettel.gnoc.commons.business.CfgWhiteListIpBusiness;
import com.viettel.gnoc.commons.dto.CfgWhiteListIpDTO;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.security.PassTranformer;
import java.net.HttpURLConnection;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Service
@Slf4j
public class OutSideInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  CfgWhiteListIpBusiness userPassWordBusiness;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String msg = validateAuthenApi(request.getHeader("username"), request.getHeader("password"),
        request.getRemoteAddr());
    if (StringUtils.isStringNullOrEmpty(msg)) {
      return true;
    } else {
      response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
      response.getOutputStream()
          .print(String.format("{\"key\":\"FAIL\", \"message\":\"%s\"}", msg));
      return false;
    }

  }

  private String validateAuthenApi(String username, String password, String ip) {
    if (StringUtils.isStringNullOrEmpty(username)) {
      return "username is not null";
    }
    if (StringUtils.isStringNullOrEmpty(password)) {
      return "password is not null";
    }

    CfgWhiteListIpDTO userPassWordDTO = userPassWordBusiness
        .checkIpSystemName(username.toLowerCase());
    if (userPassWordDTO != null && StringUtils.isNotNullOrEmpty(userPassWordDTO.getIp())) {
      String[] list = userPassWordDTO.getIp().split(",");
      if (!Arrays.asList(list).contains(ip)) {
        return "IP is invalid";
      } else {
        try {
          PassTranformer.setInputKey(userPassWordDTO.getSystemName());
          String userWS = PassTranformer.decrypt(password);
          PassTranformer.setInputKey(null);
          if (userPassWordDTO.getUserName() == null || !userPassWordDTO.getUserName()
              .equals(userWS)) {
            return "Username or password is incorrect";
          } else {
            return "";
          }

        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        return "Username or password is incorrect";
      }
    } else {
      return "Username is not exist on GNOC";
    }
  }

}
