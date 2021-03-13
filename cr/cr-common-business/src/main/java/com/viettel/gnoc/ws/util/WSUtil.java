/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.util;

import com.viettel.aam.TdttWebservice;
import com.viettel.aam.TdttWebserviceImplService;
import com.viettel.bikt.webservices.ServiceGNOC;
import com.viettel.bikt.webservices.ServiceGNOC_Service;
import com.viettel.gate.webservice.UpdateECRForGNOCService;
import com.viettel.gate.webservice.UpdateECRForGNOCService_Service;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service("wSCrUtil")
public class WSUtil {

  @Value("${application.ws.service_url_vipadd:null}")
  public String SERVICE_URL_VIPA_DD;

  @Value("${application.ws.service_url_vipaip:null}")
  public String SERVICE_URL_VIPA_IP;

  @Value("${application.ws.service_url_tdtt:null}")
  public String SERVICE_URL_TDTT;

  @Value("${application.ws.SERVICE_URL_BIKT:null}")
  public String SERVICE_URL_BIKT;

  @Value("${application.ws.gate_ws_url:null}")
  public String GATE_URL;

  @Value("${application.ws.recvTimeoutNTMS:300000}")
  private Integer recvTimeoutNTMS;

  @Value("${application.ws.connectTimeoutNTMS:300000}")
  private Integer connectionTimeout;

  public static final int CONNECT_TIMEOUT = 60000;//ms
  public static final int REQUEST_TIMEOUT = 60000;//ms

  public com.viettel.vmsa.WSForGNOC getVIPADDWS() throws Exception {
    try {
      URL baseUrl = com.viettel.vmsa.WSForGNOCImplService.class.getResource(".");
      URL url = new URL(baseUrl, SERVICE_URL_VIPA_DD); //
      com.viettel.vmsa.WSForGNOCImplService service = new com.viettel.vmsa.WSForGNOCImplService(url,
          new QName("http://webservice.viettel.com/", "WSForGNOCImplService"));
      return (com.viettel.vmsa.WSForGNOC) setTimeOut(service.getWSForGNOCImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public com.viettel.vipa.WSForGNOC getVIPAIPWS() {
    try {
      com.viettel.vipa.WSForGNOCImplService service;
      URL baseUrl = com.viettel.vipa.WSForGNOCImplService.class.getResource(".");
      URL url = new URL(baseUrl, SERVICE_URL_VIPA_IP);
      service = new com.viettel.vipa.WSForGNOCImplService(url,
          new QName("http://webservice.viettel.com/", "WSForGNOCImplService"));
      return (com.viettel.vipa.WSForGNOC) setTimeOut(service.getWSForGNOCImplPort());

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      //throw ex;
      return null;
    }
  }


  public Object setTimeOut(Object port) {
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.connect.timeout", recvTimeoutNTMS);
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.request.timeout", connectionTimeout);
    return port;
  }

  public TdttWebservice getTDTTWS() throws Exception {
    try {
      URL url = null;
      URL baseUrl;
      TdttWebserviceImplService service;
      baseUrl = TdttWebserviceImplService.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_TDTT);
      service = new TdttWebserviceImplService(url,
          new QName("http://webservice.viettel.com/", "TdttWebserviceImplService"));
      return (TdttWebservice) setTimeOut(service.getTdttWebserviceImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public ServiceGNOC getBIKTWS() throws Exception {
    try {
      URL url = null;
      URL baseUrl;
      ServiceGNOC_Service service;
      baseUrl = ServiceGNOC_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_BIKT);
      service = new ServiceGNOC_Service(url,
          new QName("http://webservices.bikt.viettel.com/", "ServiceGNOC"));
      return (ServiceGNOC) setTimeOut(service.getServiceGNOCPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public UpdateECRForGNOCService getGateWS() throws Exception {
    try {
      URL baseUrl = UpdateECRForGNOCService_Service.class.getResource(".");
      URL url = new URL(baseUrl, GATE_URL);
      UpdateECRForGNOCService_Service service = new UpdateECRForGNOCService_Service(url,
          new QName("http://webservice.gate.viettel.com/", "UpdateECRForGNOCService"));
      return (UpdateECRForGNOCService) setTimeOut(service.getUpdateECRForGNOCServicePort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }


}
