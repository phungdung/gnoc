/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.commons.dto.RestObjForBCCS;
import com.viettel.gnoc.commons.dto.RestfulDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UserPass;
import com.viettel.gnoc.commons.utils.CallRestful;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.security.PassTranformer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WS_AMIONE_Port {

  @Value("${application.ws.amiOne.url:null}")
  String url;

  @Value("${application.ws.amiOne.amiUser:null}")
  String amiUser;

  @Value("${application.ws.amiOne.amiPass:null}")
  String amiPass;

  @Value("${application.ws.recvTimeoutNTMS:60000}")
  Integer timeOut;

  public HttpHeaders headers;

  public String resourcePath;

  public RestfulDTO input;

  public Map<String, String> mapParam;

  @PostConstruct
  public void init() {
    headers = new HttpHeaders();
//        if (Constants.NATION_CODE.MVT.equals(nationCode)) {
//            url = getConfig("bccs_cc3_mvt_url");
//            headers.set("Accept-Language", "en-MZ");
    headers.setContentType(MediaType.APPLICATION_JSON);
//        } else { //default
//
//        }
    try {
      amiUser = PassTranformer.decrypt(amiUser);
      amiPass = PassTranformer.decrypt(amiPass);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * lay token BCCS 3
   */
  public String getToken(String nationCode) {
    String res = null;
    try {
      RestfulDTO inputToken = new RestfulDTO();

      HttpHeaders mHeader = new HttpHeaders();
      mHeader.set("Content-Type", "application/json");
      UserPass us = new UserPass(amiPass, amiUser);
      inputToken.setBody(DataUtil.toJson(us));

      inputToken.setUrl(url);
      inputToken.setResourcePath("/viettelnms/public/authen/login");
      inputToken.setTimeOut(timeOut);
      inputToken.setHeaders(mHeader);

      ResponseEntity<String> response = CallRestful.postForGetResponse(inputToken);
      if (response != null) {
        HttpStatus status = response.getStatusCode();
        if (status == status.OK) {
          HttpHeaders h = response.getHeaders();
          if (h != null) {
            List<String> hTmp = h.get("Set-Cookie");
            if (hTmp != null && hTmp.size() > 0) {
              String[] key = hTmp.get(0).split(";");
              res = key[0];
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return res;
  }

  //method bat buoc phai goi truoc khi goi ham
  public void setInputHeader(String nationCode, String func) {
    String token = getToken(nationCode);

    headers.set("Cookie", token);
    String resourcePathLocal = resourcePath;
    if (Constants.REST_FUNC.AMI_ONE_CHECK_ONLINE.equals(func)) {
      resourcePathLocal = "/viettelnms/public/method/checkOnline";
    }
//        else if (Constants.REST_FUNC.IM_CHECK_KHO.equals(func)) {
//            resourcePath = "/inventory/validate-stock-total";
//        } else if (Constants.REST_FUNC.IM_ROLLBACK.equals(func)) {
//            resourcePath = "/saga/restore-ap-deployment";
//        }

    // input
    input = new RestfulDTO();
    input.setUrl(url);
    input.setResourcePath(resourcePathLocal);
    input.setTimeOut(timeOut);
    if (headers.size() > 0) {
      input.setHeaders(headers);
    }
  }

  public ResultDTO checkOnline(String account) {
    try {
      mapParam = input.getParams();
      if (mapParam == null) {
        mapParam = new HashMap<>();
      }
      if (account != null) {
        mapParam.put("account_no", account);
      }

      input.setParams(mapParam);

      String resultRest = CallRestful.get(input);
      if (!StringUtils.isStringNullOrEmpty(resultRest)) {
        RestObjForBCCS resTmp = (RestObjForBCCS) DataUtil.toObj(resultRest, RestObjForBCCS.class);

        if (resTmp != null) {
          ResultDTO res = new ResultDTO();
          if ("OK".equals(resTmp.getResult())) {
            res.setKey("SUCCESS");
          } else if ("NOK".equals(resTmp.getResult())) {
            res.setKey("FAIL");
            res.setMessage(resTmp.getErrorCode() + ":" + resTmp.getDescription());
          } else {
            res.setKey("FAIL");
            res.setMessage(resTmp.getErrorCode() + ":" + resTmp.getDescription());
          }
          return res;
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
