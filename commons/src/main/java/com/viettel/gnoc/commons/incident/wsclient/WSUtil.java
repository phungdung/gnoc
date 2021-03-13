/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.incident.wsclient;

import com.viettel.gnoc.iim.webservice.IimServices;
import com.viettel.gnoc.iim.webservice.IimServices_Service;
import com.viettel.nims.infra.webservice.InfraWS;
import com.viettel.nims.infra.webservice.InfraWS_Service;
import com.viettel.nocproV4.NocproWebservice;
import com.viettel.nocproV4.NocproWebservice_Service;
import com.viettel.security.service.SecurityProvideService;
import com.viettel.security.service.SecurityProvideService_Service;
import com.viettel.service.SPMProvide;
import com.viettel.service.SPMProvide_Service;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.viettel.smartoffice.SmartOfficeService;
import vn.viettel.smartoffice.SmartOfficeService_Service;

@Service("wSUtilChat")
@Slf4j
public class WSUtil {

  @Value("${application.ws.service_url_chat:null}")
  public String SERVICE_URL_CHAT;
  public static final int CONNECT_TIMEOUT = 60000;//ms
  public static final int REQUEST_TIMEOUT = 60000;//ms
  private SmartOfficeService smartOfficeService;

  @Value("${application.ws.service_url_nims:null}")
  public String SERVICE_URL_NIMS;
  @Value("${application.ws.nocprov4_url:null}")
  public String NOCPROV4_URL;
  @Value("${application.ws.iim_url:null}")
  public String IIM_URL;
  @Value("${application.ws.service_url_security:null}")
  public String SERVICE_URL_SECURITY;
  @Value("${application.ws.service_url_trace_phone:null}")
  public String SERVICE_URL_TRACE_PHONE;

  @PostConstruct
  public void init() throws Exception {
    try {
      URL baseUrl = SmartOfficeService.class.getResource(".");
      URL url = new URL(baseUrl, SERVICE_URL_CHAT);
      SmartOfficeService_Service service = new SmartOfficeService_Service(url,
          new QName("http://smartoffice.viettel.vn", "smartOfficeService"));
      smartOfficeService = (SmartOfficeService) setTimeOut(service.getSmartOfficeServicePort());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      //throw e;
    }
  }

  public SmartOfficeService getChat() throws Exception {
    return smartOfficeService;
  }

  public InfraWS getNIMSInfraWS() throws Exception {
    try {
      URL baseUrl = InfraWS_Service.class.getResource(".");
      URL url = new URL(baseUrl, SERVICE_URL_NIMS);
      InfraWS_Service service = new InfraWS_Service(url,
          new QName("http://webservice.infra.nims.viettel.com/", "InfraWS"));
      return (InfraWS) setTimeOut(service.getInfraWSPort());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  public NocproWebservice getNOCPROV4WS() throws Exception {
    try {
      URL url;
      URL baseUrl = NocproWebservice_Service.class.getResource(".");
      url = new URL(baseUrl, NOCPROV4_URL);
      NocproWebservice_Service service = new NocproWebservice_Service(url,
          new QName("http://service.nocpro.nms.viettel.com/", "NocproWebservice"));
      return (NocproWebservice) setTimeOut(service.getNocproWebservicePort());
    } catch (MalformedURLException ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public IimServices getIIMWS() throws Exception {
    try {
      URL url;
      URL baseUrl = IimServices_Service.class.getResource(".");
      url = new URL(baseUrl, IIM_URL);
      IimServices_Service service = new IimServices_Service(url,
          new QName("http://main.services.iim.viettel.com/", "IimServices"));
      return (IimServices) setTimeOut(service.getIimServicesPort());
    } catch (MalformedURLException ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public SecurityProvideService getSecurityWS() throws Exception {
    try {
      URL url = null;
      URL baseUrl;
      SecurityProvideService_Service service;
      baseUrl = SecurityProvideService_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_SECURITY);
      service = new SecurityProvideService_Service(url,
          new QName("http://service.security.viettel.com/", "SecurityProvideService"));
      return (SecurityProvideService) setTimeOut(service.getSecurityProvideServicePort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public SPMProvide getSPMProviderWS() throws Exception {
    try {
      URL url = null;
      URL baseUrl;
      SPMProvide_Service service;
      baseUrl = SPMProvide_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_TRACE_PHONE);
      service = new SPMProvide_Service(url,
          new QName("http://service.viettel.com/", "SPMProvide"));
      return (SPMProvide) setTimeOut(service.getSPMProvidePort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public static Object setTimeOut(Object port) {
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.connect.timeout", CONNECT_TIMEOUT);
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.request.timeout", REQUEST_TIMEOUT);
    return port;
  }

}
