/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils;

import com.viettel.bccs.inventory.service.BaseMessage;
//import com.viettel.bccs.inventory.service.StockTotalFullDTO;
import com.viettel.bccs.inventory.service.StockTotalResult;
//import com.viettel.bccs.inventory.service.StockTransDetailDTO;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.utils.bccs3.im.StockTotalFullDTO;
import com.viettel.gnoc.wo.utils.bccs3.im.StockTransDetailDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author thanhlv12
 */
@Service
public class WS_IM_Direction {

  /**
   * Ham thuc hien dieu huong goi tru kho
   *
   * @param cmt
   * @param account
   * @param troubleType
   * @param lstMaterial
   * @param transactionId
   * @param date
   * @param deptCode
   * @param nationCode
   * @return
   */

  @Autowired
  WSIMInventoryPort wsimInventoryPort;

  @Autowired
  WS_BCCS_IM3_Port ws_bccs_im3_port;

  @Autowired
  WSIMInventory_MYT_Port wsimInventory_myt_port;

  @Value("${application.ws.bccs_im2_myt_url:null}")
  public String SERVICE_URL_IM_MYT_CC2;

  public BaseMessage saveAPDeploymentWithWODatetime(String cmt, String account,
      String troubleType, List<StockTransDetailDTO> lstMaterial,
      String transactionId, Date date, String deptCode, String nationCode) {

    BaseMessage res = null;

    if (StringUtils.isStringNullOrEmpty(nationCode)
        || "VNM".equals(nationCode)) {
      res = wsimInventoryPort.saveAPDeploymentWithWODatetime(cmt, account, troubleType,
          convertStockTransDetailDTO(lstMaterial), transactionId, date);

    } else if ("MVT".equals(nationCode) ||
        "VTP".equals(nationCode)) {
      WS_BCCS_IM3_Port gPort = ws_bccs_im3_port.getInstance(nationCode,
          Constants.REST_FUNC.IM_TRU_KHO);
      res = gPort.saveAPDeploymentWithWODatetime(deptCode, account,
          troubleType, lstMaterial, transactionId, date);

    } else if ("MYT".equals(nationCode)) {
      if (lstMaterial != null && lstMaterial.size() > 0) {
        List<com.viettel.webservice.inventory2.global.StockTransDetailDTO> lstT
            = new ArrayList<com.viettel.webservice.inventory2.global.StockTransDetailDTO>();

        for (StockTransDetailDTO i : lstMaterial) {
          com.viettel.webservice.inventory2.global.StockTransDetailDTO o
              = new com.viettel.webservice.inventory2.global.StockTransDetailDTO();
          o.setActionCode(i.getActionCode());
          o.setAmount(i.getAmount());
          o.setProdOfferCode(i.getProdOfferCode());
          o.setProdOfferId(i.getProdOfferId());
          o.setQuantity(i.getQuantity());

          lstT.add(o);
        }
        wsimInventory_myt_port.setInstance(SERVICE_URL_IM_MYT_CC2, nationCode);
        com.viettel.webservice.inventory2.global.BaseMessage resT
            = wsimInventory_myt_port.saveAPDeploymentWithWODatetime(cmt, account, troubleType,
            lstT, transactionId, date);

        res = new BaseMessage();
        if (resT != null) { // parse dc
          res.setSuccess(resT.isSuccess());
          res.setDescription(resT.getDescription());
        } else {
          res.setSuccess(false);
          res.setDescription(I18n.getLanguage("errWhenCallIM"));
        }

      }
    }
    return res;
  }

  /**
   * ham dieu huong thuc hien rollback khi co loi
   */
  public BaseMessage restoreAPDeploymentByIdNo(String cmt, String account,
      String troubleType, List<StockTransDetailDTO> lstMaterial,
      String transactionId, String deptCode, String nationCode) {

    BaseMessage res = null;

    if (StringUtils.isStringNullOrEmpty(nationCode)
        || "VNM".equals(nationCode)) {

      res = wsimInventoryPort.restoreAPDeploymentByIdNo(cmt, account, troubleType,
          convertStockTransDetailDTO(lstMaterial), transactionId);

    } else if ("MVT".equals(nationCode) ||
        "VTP".equals(nationCode)) {
      WS_BCCS_IM3_Port gPort = ws_bccs_im3_port.getInstance(nationCode,
          Constants.REST_FUNC.IM_ROLLBACK);
      res = gPort.restoreAPDeploymentByIdNo(cmt, account, troubleType,
          lstMaterial, transactionId, deptCode);

    } else if ("MYT".equals(nationCode)) {
      if (lstMaterial != null && lstMaterial.size() > 0) {
        List<com.viettel.webservice.inventory2.global.StockTransDetailDTO> lstT
            = new ArrayList<com.viettel.webservice.inventory2.global.StockTransDetailDTO>();

        for (StockTransDetailDTO i : lstMaterial) {
          com.viettel.webservice.inventory2.global.StockTransDetailDTO o
              = new com.viettel.webservice.inventory2.global.StockTransDetailDTO();
          o.setActionCode(i.getActionCode());
          o.setAmount(i.getAmount());
          o.setProdOfferCode(i.getProdOfferCode());
          o.setProdOfferId(i.getProdOfferId());
          o.setQuantity(i.getQuantity());

          lstT.add(o);
        }
        wsimInventory_myt_port.setInstance(SERVICE_URL_IM_MYT_CC2, nationCode);
        com.viettel.webservice.inventory2.global.BaseMessage resT
            = wsimInventory_myt_port.restoreAPDeploymentByIdNo(cmt, account, troubleType,
            lstT, transactionId);

        res = new BaseMessage();
        if (resT != null) { // parse dc

          res.setSuccess(resT.isSuccess());
          res.setDescription(resT.getDescription());
        } else {
          res.setSuccess(false);
          res.setDescription(I18n.getLanguage("errWhenCallIM"));
        }

      }

    }
    return res;
  }

  /**
   * ham dieu huong validate hoan thanh
   */
  public StockTotalResult validateStockTotalByStaffIdNo(String deptCode,
      List<StockTotalFullDTO> lstMaterial, String nationCode) {
    StockTotalResult res = null;

    if (StringUtils.isStringNullOrEmpty(nationCode)
        || "VNM".equals(nationCode)) {

      res = wsimInventoryPort.validateStockTotalByStaffIdNo(deptCode,  convertStockTotalFullDTO(lstMaterial));

    } else if ("MVT".equals(nationCode) ||
        "VTP".equals(nationCode)) {
      WS_BCCS_IM3_Port gPort = ws_bccs_im3_port
          .getInstance(nationCode, Constants.REST_FUNC.IM_CHECK_KHO);
      res = gPort.validateStockTotalByStaffIdNo(deptCode, lstMaterial);

    } else if ("MYT".equals(nationCode)) {
      if (lstMaterial != null && lstMaterial.size() > 0) {
        List<com.viettel.webservice.inventory2.global.StockTotalFullDTO> lstT
            = new ArrayList<com.viettel.webservice.inventory2.global.StockTotalFullDTO>();

        for (StockTotalFullDTO i : lstMaterial) {
          com.viettel.webservice.inventory2.global.StockTotalFullDTO u
              = new com.viettel.webservice.inventory2.global.StockTotalFullDTO();
          u.setProdOfferCode(i.getProdOfferCode());
          u.setProdOfferId(i.getProdOfferId());
          u.setCurrentQuantity(i.getCurrentQuantity());
          lstT.add(u);

        }

        wsimInventory_myt_port.setInstance(SERVICE_URL_IM_MYT_CC2, nationCode);
        com.viettel.webservice.inventory2.global.StockTotalResult resTmp
            = wsimInventory_myt_port.validateStockTotalByStaffIdNo(deptCode, lstT);

        res = new StockTotalResult();
        if (resTmp != null) { // parse dc
          res.setSuccess(resTmp.isSuccess());
          res.setErrorCode(resTmp.getErrorCode());
          if (resTmp.getLstError() != null && resTmp.getLstError().size() > 0) {
            res.getLstError().addAll(resTmp.getLstError());
          }
        } else {
          res.setSuccess(false);
          res.setDescription(I18n.getLanguage("errWhenParseObj"));
        }

      }
    }
    return res;
  }

  public List<com.viettel.bccs.inventory.service.StockTotalFullDTO> convertStockTotalFullDTO(List<StockTotalFullDTO> lst) {
    List<com.viettel.bccs.inventory.service.StockTotalFullDTO> lstR = new ArrayList<com.viettel.bccs.inventory.service.StockTotalFullDTO>();
    for (StockTotalFullDTO i : lst) {
      com.viettel.bccs.inventory.service.StockTotalFullDTO o = new com.viettel.bccs.inventory.service.StockTotalFullDTO();
      o.setCurrentQuantity(i.getCurrentQuantity());
      o.setProdOfferCode(i.getProdOfferCode());
      o.setProdOfferId(i.getProdOfferId());
      lstR.add(o);
    }
    return lstR;
  }

  public List<com.viettel.bccs.inventory.service.StockTransDetailDTO> convertStockTransDetailDTO(List<StockTransDetailDTO> lst) {
    List<com.viettel.bccs.inventory.service.StockTransDetailDTO> lstR = new ArrayList<com.viettel.bccs.inventory.service.StockTransDetailDTO>();
    for (StockTransDetailDTO i : lst) {
      com.viettel.bccs.inventory.service.StockTransDetailDTO o = new com.viettel.bccs.inventory.service.StockTransDetailDTO();
      o.setProdOfferId(i.getProdOfferId());
      o.setQuantity(i.getQuantity());
      lstR.add(o);
    }
    return lstR;
  }

}
