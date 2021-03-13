package com.viettel.gnoc.commons.config;

import com.google.gson.Gson;
import com.viettel.gnoc.commons.dto.HazelcastDto;
import com.viettel.gnoc.commons.dto.HazelcastResponseDto;
import com.viettel.gnoc.commons.proxy.HazelcastProxy;
import com.viettel.gnoc.commons.utils.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import viettel.passport.client.UserToken;

/**
 * @author TungPV
 */
@Service
@Slf4j
public class TicketProvider {

  private static final String TICKET = "ticket";
  private static final String SECURITY_AUTHENTICATION_JWT_SECRET = "SECURITY_AUTHENTICATION_JWT_SECRET";
  private static String SECURITY_AUTHENTICATION_JWT_SECRET_VALUE = "";

  public static UserToken getUserToken() {
    UserToken vsaUserToken = null;
    Long currentTime = new Date().getTime();
    int retryProxy = 0;
    while (retryProxy < 2) {
      try {
        HazelcastDto hazelcastDTO = new HazelcastDto(SECURITY_AUTHENTICATION_JWT_SECRET,
            "" + currentTime,
            0, null);
        HazelcastProxy hazelcastProxy = SpringApplicationContext.bean(HazelcastProxy.class);
        HazelcastResponseDto hazelcastResponseDto;
        if (StringUtils.isStringNullOrEmpty(SECURITY_AUTHENTICATION_JWT_SECRET_VALUE)) {
          hazelcastResponseDto = hazelcastProxy.getDataFromHazelCast(hazelcastDTO);
          if (hazelcastResponseDto == null || (hazelcastResponseDto != null && StringUtils
              .isStringNullOrEmpty(hazelcastResponseDto.getResponse()))) {
            log.error("GET SECURITY_AUTHENTICATION_JWT_SECRET IS NULL: " + currentTime);
          }
          if (hazelcastResponseDto != null && !StringUtils
              .isStringNullOrEmpty(hazelcastResponseDto.getResponse())) {
            log.info("START INITIAL SECURITY_AUTHENTICATION_JWT_SECRET_VALUE");
            SECURITY_AUTHENTICATION_JWT_SECRET_VALUE = hazelcastResponseDto.getResponse();
          }
        } else {
          log.info(String.format("SEC_AU_JWT_SECRET_VALUE: {%s}",
              SECURITY_AUTHENTICATION_JWT_SECRET_VALUE));
        }
        String token = TokenContextHolder.getToken();
        if (StringUtils.isStringNullOrEmpty(token)) {
          log.error("Token is null");
        }

        Claims claims = Jwts.parser().setSigningKey(SECURITY_AUTHENTICATION_JWT_SECRET_VALUE)
            .parseClaimsJws(token).getBody();
        String ticket = (String) claims.get(TICKET);
        //cap nhat lai key, get ticket
        hazelcastDTO.setKey(ticket);
        hazelcastResponseDto = hazelcastProxy.getDataFromHazelCast(hazelcastDTO);
        if (hazelcastResponseDto == null || (hazelcastResponseDto != null && StringUtils
            .isStringNullOrEmpty(hazelcastResponseDto.getResponse()))) {
          log.error("GET TICKET {" + ticket + "} IS NULL: " + currentTime);
        } else {
          vsaUserToken = new Gson().fromJson(hazelcastResponseDto.getResponse(), UserToken.class);
        }
      } catch (Exception ex) {
        log.error("Cannot get user from cache", ex);
      }
      if (vsaUserToken == null) {
        retryProxy++;
        try {
          log.info("Continues To Retry get Token: " + currentTime);
          Thread.sleep(100);
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }
      } else {
        log.info("Break getToken: " + currentTime);
        break;
      }
    }
//   if (vsaUserToken == null) {
//      vsaUserToken = new UserToken();
//      setVsaUserToken(vsaUserToken);
//    }
    return vsaUserToken;
  }
  /*
  static void setVsaUserToken(UserToken vsaUserToken) {
//    vsaUserToken.setUserID(256021L);
//    vsaUserToken.setDeptId(436320L);
    vsaUserToken.setUserID(999999L);
    vsaUserToken.setDeptId(413314L);
    vsaUserToken.setUserName("thanhlv12");
    vsaUserToken.setFullName("Le Van Thanh");
    vsaUserToken.setTelephone("866891368");
  }*/
}
