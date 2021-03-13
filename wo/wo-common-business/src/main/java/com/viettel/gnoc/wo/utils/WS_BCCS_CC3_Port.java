/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.dto.RestObjForBCCS;
import com.viettel.gnoc.commons.dto.RestfulDTO;
import com.viettel.gnoc.commons.utils.CallRestful;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 * @author thanhlv12
 */
@Service
@Slf4j
public class WS_BCCS_CC3_Port {


  @Value("${application.ws.recvTimeoutNTMS:null}")
  private String timeOut;

  @Value("${application.ws.bccs_cc3_mvt.url:null}")
  private String MVT_URL;

  @Value("${application.ws.bccs_cc3_vtp.url:null}")
  private String VTP_URL;

  public String url;
  public String resourcePath;
  public HttpHeaders headers;
  public Map<String, String> mapParam;
  public RestfulDTO input;
  public String stationCode;

  /*
  khoi tao du lieu dua vao nation
   */
  public WS_BCCS_CC3_Port getInstance(String nationCode, String func) {

    headers = new HttpHeaders();
    // url
    if (Constants.NATION_CODES.MVT.equals(nationCode)) {
      url = MVT_URL;
      headers.set("Accept-Language", "en-MZ");
      headers.setContentType(MediaType.APPLICATION_JSON);
    } else if (Constants.NATION_CODE.VTP.equals(nationCode)) {
      url = VTP_URL;
      headers.set("Accept-Language", "en-PE");
      headers.setContentType(MediaType.APPLICATION_JSON);
    } else { //default

    }

    String token = getToken(nationCode);

    headers.set("Authorization", "Bearer " + token);

    if (Constants.REST_FUNC.CC_GET_CAUSE_EXPIRE.equals(func)) {
      resourcePath = "/problem/complains/cause-error-expires";
    }
//        else if (Constants.REST_FUNC.IM_CHECK_KHO.equals(func)) {
//            resourcePath = "/inventory/validate-stock-total";
//        } else if (Constants.REST_FUNC.IM_ROLLBACK.equals(func)) {
//            resourcePath = "/saga/restore-ap-deployment";
//        }

    // input
    input = new RestfulDTO();
    input.setUrl(url);
    input.setResourcePath(resourcePath);
    input.setTimeOut(Integer.parseInt(timeOut));
    if (headers.size() > 0) {
      input.setHeaders(headers);
    }
    return this;
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
      mHeader.set("Authorization", "Basic Z25vY19zeXN0ZW06YmNjczM=");

      Map<String, String> mParam = new HashMap<String, String>();
      mParam.put("grant_type", "client_credentials");

      inputToken.setUrl(url);
      inputToken.setResourcePath("/oauth/token");
      inputToken.setTimeOut(Integer.parseInt(timeOut));
      inputToken.setHeaders(mHeader);
      inputToken.setParams(mParam);
      String resulRest = CallRestful.post(inputToken);

      RestObjForBCCS token = (RestObjForBCCS) DataUtil.toObj(resulRest, RestObjForBCCS.class);

      if (token != null) {
        res = token.getAccess_token();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return res;
  }

  public List<CompCause> getCauseExpires(Long parentId) {
    try {
      mapParam = input.getParams();
      if (mapParam == null) {
        mapParam = new HashMap<String, String>();
      }
      if (parentId != null) {
        mapParam.put("parentId", parentId.toString());
      }

      input.setParams(mapParam);

      String resultRest = CallRestful.get(input);
      if (!StringUtils.isStringNullOrEmpty(resultRest)) {
        List<RestObjForBCCS> lst = (List<RestObjForBCCS>) new Gson()
            .fromJson(resultRest, new TypeToken<List<RestObjForBCCS>>() {
            }.getType());
        if (lst != null && lst.size() > 0) {
          List<CompCause> res = new ArrayList<CompCause>();
          for (RestObjForBCCS i : lst) {
            CompCause c = new CompCause();
            c.setCode(i.getCode());
            c.setCompCauseId(i.getCauseErrExpId());
            c.setDescription(i.getDescription());
            c.setIsactive(i.getStatus());
            c.setName(i.getName());
            res.add(c);
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
