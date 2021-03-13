/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.ipcc.services.AutoCallOutInput;
import com.viettel.ipcc.services.CallService;
import com.viettel.ipcc.services.CallService_Service;
import com.viettel.ipcc.services.NomalOutput;
import com.viettel.security.PassTranformer;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WSIPCC {

  CallService port;
  CallService portVTP;
  CallService portNCM;
  CallService portVCR;
  CallService portVTZ;
  CallService portSTL;
  CallService portMVT;
  CallService portVTL;
  CallService portVTB;
  CallService portVTC;
  CallService portMYT;
  public static final int CONNECT_TIMEOUT = 180000;//ms
  public static final int REQUEST_TIMEOUT = 180000;//ms
  private static WSIPCC wsIpcc = null;

  public static synchronized WSIPCC getWsIpcc() {
    if (wsIpcc == null) {
      wsIpcc = new WSIPCC();
    }
    return wsIpcc;
  }

  public WSIPCC() {

  }

  private void createIPCCWS(IpccServiceDTO ipccServiceDTO) throws Exception {

    URL baseUrl = CallService_Service.class.getResource(".");
    URL url = new URL(baseUrl, ipccServiceDTO.getUrl());
    CallService_Service service = new CallService_Service(url,
        new QName("http://services.ipcc.viettel.com/", "CallService"));
    if ("IPCC_VTP".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())) {//peru
      portVTP = (CallService) setTimeOut(service.getCallServicePort());
    } else if ("IPCC_NCM".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())) {//haiti
      portNCM = (CallService) setTimeOut(service.getCallServicePort());
    } else if ("IPCC_VCR".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())) {//cameroon
      portVCR = (CallService) setTimeOut(service.getCallServicePort());
    } else if ("IPCC_MVT".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())) {//mozambic
      portMVT = (CallService) setTimeOut(service.getCallServicePort());
    } else if ("IPCC_VTZ".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())) {//tanzania
      portVTZ = (CallService) setTimeOut(service.getCallServicePort());
    } else if ("IPCC_STL".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())) {//timo
      portSTL = (CallService) setTimeOut(service.getCallServicePort());
    } else if ("IPCC_VTL".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())) {//lao
      portVTL = (CallService) setTimeOut(service.getCallServicePort());
    } else if ("IPCC_VTB".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())) {//burundi
      portVTB = (CallService) setTimeOut(service.getCallServicePort());
    } else if ("IPCC_VTC".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())) {//cam
      portVTC = (CallService) setTimeOut(service.getCallServicePort());
    } else if ("IPCC_VN".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())) {//vn
      port = (CallService) setTimeOut(service.getCallServicePort());
    }
  }

  public NomalOutput autoCallout(IpccServiceDTO ipccServiceDTO, AutoCallOutInput input)
      throws Exception {
    NomalOutput res = null;
    try {
      String pwd;
      try {
        pwd = PassTranformer.decrypt(ipccServiceDTO.getPassword());
      } catch (Exception ex) {
        pwd = Constants.WSIPCC.P_DEFAULT;
        log.error(ex.getMessage(), ex);
      }
      ipccServiceDTO.setPassword(pwd);
      if ("IPCC_VN".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode()) && port == null) {
        createIPCCWS(ipccServiceDTO);
      } else if ("IPCC_NCM".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portNCM == null) {
        createIPCCWS(ipccServiceDTO);
      } else if ("IPCC_VTP".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVTP == null) {
        createIPCCWS(ipccServiceDTO);
      } else if ("IPCC_VCR".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVCR == null) {
        createIPCCWS(ipccServiceDTO);
      } else if ("IPCC_VTZ".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVTZ == null) {
        createIPCCWS(ipccServiceDTO);
      } else if ("IPCC_STL".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portSTL == null) {
        createIPCCWS(ipccServiceDTO);
      } else if ("IPCC_MVT".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portMVT == null) {
        createIPCCWS(ipccServiceDTO);
      } else if ("IPCC_VTL".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVTL == null) {
        createIPCCWS(ipccServiceDTO);
      } else if ("IPCC_VTB".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVTB == null) {
        createIPCCWS(ipccServiceDTO);
      } else if ("IPCC_VTC".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVTC == null) {
        createIPCCWS(ipccServiceDTO);
      }

      if ("IPCC_VN".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode()) && port != null) {
        res = port.autoCallout(ipccServiceDTO.getUserName(), ipccServiceDTO.getPassword(), input);
      } else if ("IPCC_NCM".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portNCM != null) {
        res = portNCM
            .autoCallout(ipccServiceDTO.getUserName(), ipccServiceDTO.getPassword(), input);
      } else if ("IPCC_VTP".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVTP != null) {
        res = portVTP
            .autoCallout(ipccServiceDTO.getUserName(), ipccServiceDTO.getPassword(), input);
      } else if ("IPCC_VCR".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVCR != null) {
        res = portVCR
            .autoCallout(ipccServiceDTO.getUserName(), ipccServiceDTO.getPassword(), input);
      } else if ("IPCC_VTZ".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVTZ != null) {
        res = portVTZ
            .autoCallout(ipccServiceDTO.getUserName(), ipccServiceDTO.getPassword(), input);
      } else if ("IPCC_STL".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portSTL != null) {
        res = portSTL
            .autoCallout(ipccServiceDTO.getUserName(), ipccServiceDTO.getPassword(), input);
      } else if ("IPCC_MVT".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portMVT != null) {
        res = portMVT
            .autoCallout(ipccServiceDTO.getUserName(), ipccServiceDTO.getPassword(), input);
      } else if ("IPCC_VTL".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVTL != null) {
        res = portVTL
            .autoCallout(ipccServiceDTO.getUserName(), ipccServiceDTO.getPassword(), input);
      } else if ("IPCC_VTB".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVTB != null) {
        res = portVTB
            .autoCallout(ipccServiceDTO.getUserName(), ipccServiceDTO.getPassword(), input);
      } else if ("IPCC_VTC".equalsIgnoreCase(ipccServiceDTO.getIpccServiceCode())
          && portVTC != null) {
        res = portVTC
            .autoCallout(ipccServiceDTO.getUserName(), ipccServiceDTO.getPassword(), input);
      }
    } catch (Exception e) {
      throw e;
    }
    return res;
  }

  public static Object setTimeOut(Object port) {
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.connect.timeout", CONNECT_TIMEOUT);
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.request.timeout", REQUEST_TIMEOUT);
    return port;
  }

}
