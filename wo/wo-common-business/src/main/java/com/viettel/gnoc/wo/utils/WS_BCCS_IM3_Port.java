/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils;

import com.google.gson.reflect.TypeToken;
import com.viettel.bccs.inventory.service.BaseMessage;
import com.viettel.bccs.inventory.service.StockTotalResult;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.RestObjForBCCS;
import com.viettel.gnoc.commons.dto.RestfulDTO;
import com.viettel.gnoc.commons.utils.CallRestful;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.utils.bccs3.im.GoodsProductDTO;
import com.viettel.gnoc.wo.utils.bccs3.im.ProdOfferSerial;
import com.viettel.gnoc.wo.utils.bccs3.im.RestoreAPDeploymentDTO;
import com.viettel.gnoc.wo.utils.bccs3.im.StockTotalDTO;
import com.viettel.gnoc.wo.utils.bccs3.im.StockTotalFullDTO;
import com.viettel.gnoc.wo.utils.bccs3.im.StockTransDetailDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

//import com.viettel.bccs.inventory.service.StockTotalFullDTO;
//import com.viettel.bccs.inventory.service.StockTransDetailDTO;

/**
 * @author thanhlv12
 */
@Service
@Slf4j
public class WS_BCCS_IM3_Port {


  @Value("${application.ws.recvTimeoutNTMS:null}")
  private String timeOut;

  @Value("${application.ws.bccs_im3_mvt.url:null}")
  private String MVT_URL;

  @Value("${application.ws.bccs_im3_vtp.url:null}")
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
  public WS_BCCS_IM3_Port getInstance(String nationCode, String func) {

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

    if (Constants.REST_FUNC.IM_TRU_KHO.equals(func)) {
//      resourcePath = "/saga/save-ap-deployment";
      resourcePath = "/saga/change-rescue-device";
    } else if (Constants.REST_FUNC.IM_CHECK_KHO.equals(func)) {
      resourcePath = "/inventory/validate-stock-total";
    } else if (Constants.REST_FUNC.IM_ROLLBACK.equals(func)) {
      resourcePath = "/saga/restore-ap-deployment";
    }

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


  /**
   * thuc hien tru kho
   */
  public BaseMessage saveAPDeploymentWithWODatetime(String deptCode, String account,
      String troubleType, List<StockTransDetailDTO> lstMaterial,
      String transactionId, Date date) {
    BaseMessage res = new BaseMessage();
    try {

      // khoi tao param nhet vao input
//      StockTransDTO dto = new StockTransDTO();
//      dto.setAccount(account);
//      dto.setCreateDateTime(date);
//      dto.setTeamCode(deptCode);
//      dto.setTransactionId(transactionId);
//      dto.setTroubleType(troubleType);
//      List<StockTransDetail> lst = new ArrayList<StockTransDetail>();
//
//      for (StockTransDetailDTO i : lstMaterial) {
//        StockTransDetail u = new StockTransDetail();
//        u.setProdOfferId(i.getProdOfferId());
//        u.setQuantity(i.getQuantity());
//        lst.add(u);
//      }
//      dto.setStockTransDetails(lst);
      List<GoodsProductDTO> lstOldGoodsProduct = new ArrayList<GoodsProductDTO>();
      List<GoodsProductDTO> lstNewGoodsProduct = new ArrayList<GoodsProductDTO>();
      RestoreAPDeploymentDTO dto = new RestoreAPDeploymentDTO();
      dto.setTeamCode(deptCode);
      dto.setAccount(account);
      dto.setTransactionId(transactionId);
      dto.setTroubleType(troubleType);
      // danh sach vat tu ko co serial
      if (lstMaterial != null && lstMaterial.size() > 0) {
        for (StockTransDetailDTO i : lstMaterial) {
          GoodsProductDTO o = new GoodsProductDTO();
          o.setGoodsProductId(i.getProdOfferId());
          o.setProductOfferingId(i.getProdOfferId());
          o.setQuantity(i.getQuantity());
          o.setName(i.getName());
          o.setCode(i.getCode());

          // vat tu
          if (i.getType() == null || i.getType().equals(0L)) {
            lstNewGoodsProduct.add(o);
          } // thiet bi thay the
          else if (i.getType().equals(1L)) {
            o.setSerial(i.getSerial());
            lstNewGoodsProduct.add(o);
          } // thiet bi thu hoi
          else if (i.getType().equals(2L)) {
            o.setSerialRetrieve(i.getSerial());
            lstOldGoodsProduct.add(o);
          }
        }
      }
      input.setBody(DataUtil.toJson(dto));

      String resultRest = CallRestful.post(input);

      RestObjForBCCS resTmp = (RestObjForBCCS) DataUtil.toObj(resultRest,
          RestObjForBCCS.class);

      if (resTmp != null) { // parse dc
        res.setSuccess(resTmp.isSuccess());
        res.setDescription(resTmp.getMessage());
      } else {
        res.setSuccess(false);
        res.setDescription(I18n.getLanguage("errWhenParseObj"));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setSuccess(false);
      res.setDescription(I18n.getLanguage("errWhenCallIM"));
    }

    return res;
  }

  /**
   * thuc hien validate danh sach vat tu khi hoan thanh
   */
  public StockTotalResult validateStockTotalByStaffIdNo(String deptCode,
      List<StockTotalFullDTO> lstMaterial) {
    StockTotalResult res = new StockTotalResult();
    try {

      mapParam = input.getParams();
      if (mapParam == null) {
        mapParam = new HashMap<>();
      }
//      mapParam.put("team-code", deptCode);
//
//      List<StockTransDetail> lst = new ArrayList<StockTransDetail>();
//
//      for (StockTotalFullDTO i : lstMaterial) {
//        StockTransDetail u = new StockTransDetail();
//        u.setProdOfferCode(i.getProdOfferCode());
//        u.setProdOfferId(i.getProdOfferId());
//        u.setQuantity(i.getCurrentQuantity());
//        lst.add(u);
//      }

      RestoreAPDeploymentDTO dto = new RestoreAPDeploymentDTO();
      List<StockTotalDTO> stockTotals = new ArrayList<StockTotalDTO>();
      List<StockTotalDTO> oldStockTotals = new ArrayList<StockTotalDTO>();
      dto.setTeamCode(deptCode);
      if (lstMaterial != null && lstMaterial.size() > 0) {
        for (StockTotalFullDTO i : lstMaterial) {
          StockTotalDTO o = new StockTotalDTO();

          o.setProdOfferId(i.getProdOfferId());
          o.setQuantity(i.getCurrentQuantity());

          // danh sach vat tu thay the
          if (StringUtils.isStringNullOrEmpty(i.getType()) || "0".equals(i.getType())) { //
            stockTotals.add(o);
          } // danh sach thiet bi thay the
          else if ("1".equals(i.getType())) {
            ProdOfferSerial serial = new ProdOfferSerial();
            serial.setFromSerial(i.getSerial());
            List<ProdOfferSerial> lstSerial = new ArrayList<ProdOfferSerial>();
            lstSerial.add(serial);
            o.setProdOfferSerials(lstSerial);
            stockTotals.add(o);
          } // danh sach thiet bi thu hoi
          else if ("2".equals(i.getType())) {
            ProdOfferSerial serial = new ProdOfferSerial();
            serial.setOldSerial(i.getSerial());
            List<ProdOfferSerial> lstSerial = new ArrayList<ProdOfferSerial>();
            lstSerial.add(serial);
            o.setProdOfferSerials(lstSerial);
            oldStockTotals.add(o);
          }
        }
      }

      dto.setOldStockTotals(oldStockTotals);
      dto.setStockTotals(stockTotals);

      if (oldStockTotals.size() > 0) {
        dto.setIsRevoke(true);
      } else {
        dto.setIsRevoke(false);
      }

      input.setBody(DataUtil.toJson(dto,
          new TypeToken<RestoreAPDeploymentDTO>() {
          }.getType()));

      System.out.println("Call IM Validate: " + input.getBody());
      input.setParams(mapParam);
      String resultRest = CallRestful.post(input);

      RestObjForBCCS resTmp = (RestObjForBCCS) DataUtil.toObj(resultRest,
          RestObjForBCCS.class);

      if (resTmp != null) { // parse dc
        res.setSuccess(resTmp.isSuccess());
        res.setErrorCode(resTmp.getMessage());
        if (resTmp.getErrors() != null && resTmp.getErrors().size() > 0) {
          res.getLstError().addAll(resTmp.getErrors());
        }
      } else {
        res.setSuccess(false);
        res.setDescription(I18n.getLanguage("errWhenParseObj"));
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setSuccess(false);
      res.setErrorCode("00");
    }
    return res;
  }

  /**
   * thuc hien rollback khi co loi
   */
  public BaseMessage restoreAPDeploymentByIdNo(String cmt, String account,
      String troubleType, List<StockTransDetailDTO> lstMaterial,
      String transactionId, String deptCode) {
    BaseMessage res = new BaseMessage();
    try {

      // khoi tao
      StockTransDTO dto = new StockTransDTO();
      dto.setAccount(account);
      dto.setTeamCode(deptCode);
      dto.setTransactionId(transactionId);
      dto.setTroubleType(troubleType);
      List<StockTransDetail> lst = new ArrayList<StockTransDetail>();

      for (StockTransDetailDTO i : lstMaterial) {
        StockTransDetail u = new StockTransDetail();
        u.setProdOfferId(i.getProdOfferId());
        u.setQuantity(i.getQuantity());
        lst.add(u);
      }
      dto.setStockTransDetails(lst);

      input.setBody(DataUtil.toJson(dto));

      String resultRest = CallRestful.post(input);

      RestObjForBCCS resTmp = (RestObjForBCCS) DataUtil.toObj(resultRest,
          RestObjForBCCS.class);

      if (resTmp != null) { // parse dc
        res.setSuccess(resTmp.isSuccess());
        res.setErrorCode(resTmp.getMessage());

      } else {
        res.setSuccess(false);
        res.setDescription(I18n.getLanguage("errWhenParseObj"));
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setSuccess(false);
      res.setDescription(I18n.getLanguage("errWhenCallIM"));
    }
    return res;
  }

}
