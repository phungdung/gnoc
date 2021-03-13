/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.nms.nocpro.service.GnocActionBO;
import com.viettel.nms.nocpro.service.GnocWebservice;
import com.viettel.nms.nocpro.service.GnocWebservice_Service;
import com.viettel.nms.nocpro.service.ResponseBO;
import java.net.URL;
import java.util.Date;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;

@Slf4j
public class NocProWS {

  GnocWebservice port;
  GnocWebservice portVTP;
  GnocWebservice portNAT;
  GnocWebservice portCMR;
  GnocWebservice portOther;
  GnocWebservice portTZN;
  GnocWebservice portSTL;
  GnocWebservice portMVT;
  GnocWebservice portVTL;
  GnocWebservice portBRD;
  GnocWebservice portVTC;
  GnocWebservice portMYT;
  Logger logger;
  private static NocProWS nocProWS = null;
  public static final int CONNECT_TIMEOUT = 180000;//ms
  public static final int REQUEST_TIMEOUT = 180000;//ms

  //multiple simultaneous callers may see partially initialized objects
  public static synchronized NocProWS getNocProWS() {
    if (nocProWS == null) {
      nocProWS = new NocProWS();
    }
    return nocProWS;
  }

  public NocProWS() {
    this.logger = Logger.getLogger("QltsWs.class");
  }

  private void createNOCWS(CfgServerNocDTO cfgServerNocDTO) throws Exception {

    URL baseUrl = GnocWebservice_Service.class.getResource(".");
    URL url = new URL(baseUrl, cfgServerNocDTO.getLink());
    System.out.println(
        "connect webservice qlts: link=" + cfgServerNocDTO.getLink() + " begin time: "
            + new Date());
    GnocWebservice_Service service = new GnocWebservice_Service(url,
        new QName(cfgServerNocDTO.getLinkName(), cfgServerNocDTO.getServerName()));
    if ("NOC_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portNAT = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    } else if ("NOC_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portVTP = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    } else if ("NOC_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portCMR = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    } else if ("NOC_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portTZN = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    } else if ("NOC_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portSTL = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    } else if ("NOC_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portMVT = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    } else if ("NOC_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portVTL = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    } else if ("NOC_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portBRD = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    } else if ("NOC_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portVTC = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    } else if ("NOC_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portMYT = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    } else if ("NOC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      port = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    } else {
      portOther = (GnocWebservice) setTimeOut(service.getGnocWebservicePort());
    }
    logger.info("connect webservice qlts success end time:" + new Date());
  }

  public ResponseBO getAlarmClearGNOC(String requestId, CfgServerNocDTO cfgServerNocDTO,
      GnocActionBO gnocActionBO) throws Exception {
    ResponseBO lst = null;
    try {

      if ("NOC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port == null) {
        createNOCWS(cfgServerNocDTO);
      } else if ("NOC_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portNAT == null) {
        createNOCWS(cfgServerNocDTO);
      } else if ("NOC_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portVTP == null) {
        createNOCWS(cfgServerNocDTO);
      } else if ("NOC_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portCMR == null) {
        createNOCWS(cfgServerNocDTO);
      } else if ("NOC_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portTZN == null) {
        createNOCWS(cfgServerNocDTO);
      } else if ("NOC_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portSTL == null) {
        createNOCWS(cfgServerNocDTO);
      } else if ("NOC_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portMVT == null) {
        createNOCWS(cfgServerNocDTO);
      } else if ("NOC_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portVTL == null) {
        createNOCWS(cfgServerNocDTO);
      } else if ("NOC_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portBRD == null) {
        createNOCWS(cfgServerNocDTO);
      } else if ("NOC_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portVTC == null) {
        createNOCWS(cfgServerNocDTO);
      } else if ("NOC_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portMYT == null) {
        createNOCWS(cfgServerNocDTO);
      } else if (portOther == null) {
        createNOCWS(cfgServerNocDTO);
      }

      if ("NOC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port != null) {
        lst = port
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      } else if ("NOC_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portNAT != null) {
        lst = portNAT
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      } else if ("NOC_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portVTP != null) {
        lst = portVTP
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      } else if ("NOC_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portCMR != null) {
        lst = portCMR
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      } else if ("NOC_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portTZN != null) {
        lst = portTZN
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      } else if ("NOC_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portSTL != null) {
        lst = portSTL
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      } else if ("NOC_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portMVT != null) {
        lst = portMVT
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      } else if ("NOC_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portVTL != null) {
        lst = portVTL
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      } else if ("NOC_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portBRD != null) {
        lst = portBRD
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      } else if ("NOC_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portVTC != null) {
        lst = portVTC
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      } else if ("NOC_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portMYT != null) {
        lst = portMYT
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      } else if (portOther != null) {
        lst = portOther
            .getAlarmClearGNOC(requestId, cfgServerNocDTO.getUserName(), cfgServerNocDTO.getPass(),
                gnocActionBO);
      }
    } catch (Exception e) {
      throw e;
    }
    return lst;
  }

  public static Object setTimeOut(Object port) {
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.connect.timeout", CONNECT_TIMEOUT);
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.request.timeout", REQUEST_TIMEOUT);
    return port;
  }

}
