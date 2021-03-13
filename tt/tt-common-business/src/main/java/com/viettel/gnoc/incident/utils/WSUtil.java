/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

import com.viettel.bccs.cc.service.SpmServiceImpl;
import com.viettel.bccs.cc.service.SpmServiceImplService;
import com.viettel.gnoc.commons.utils.ws.HeaderHandlerResolver;
import com.viettel.nims.infra.webservice.InfraWS;
import com.viettel.nims.infra.webservice.InfraWS_Service;
import com.viettel.soc.spm.service.ServiceProblemService;
import com.viettel.soc.spm.service.ServiceProblemServiceImplService;
import com.viettel.tktu.webservice.NTMSTicketManager;
import com.viettel.tktu.webservice.NTMSTicketManagerService;
import com.viettel.webservice.function.ServiceForOtherSystem;
import com.viettel.webservice.function.ServiceForOtherSystemImplService;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.viettel.smartoffice.SmartOfficeService;
import vn.viettel.smartoffice.SmartOfficeService_Service;

@Service
@Slf4j
public class WSUtil {


  private String salt;
  @Value("${application.ws.service_url_spm:null}")
  public String SERVICE_URL_SPM;
  @Value("${application.ws.service_url_tktu:null}")
  public String SERVICE_URL_TKTU;
  @Value("${application.ws.service_url_bccs2:null}")
  public String SERVICE_URL_CC_2;
  @Value("${application.ws.service_url_chat:null}")
  public String SERVICE_URL_CHAT;
  @Value("${application.ws.service_url_nims:null}")
  public String SERVICE_URL_NIMS;
  @Value("${application.ws.service_url_nims_2:null}")
  public String SERVICE_URL_NIMS_2;
  @Value("${application.ws.service_url_sap:null}")
  public String SERVICE_URL_SAP;
  @Value("${application.ws.CONNECT_TIMEOUT:60000}")
  public int CONNECT_TIMEOUT;//ms
  @Value("${application.ws.REQUEST_TIMEOUT:60000}")
  public int REQUEST_TIMEOUT;

  public ServiceProblemService getSPMWS() throws Exception {
    try {
      URL baseUrl = ServiceProblemServiceImplService.class.getResource(".");
      URL url = new URL(baseUrl, SERVICE_URL_SPM);
      ServiceProblemServiceImplService service = new ServiceProblemServiceImplService(url,
          new QName("http://service.spm.soc.viettel.com/", "ServiceProblemServiceImplService"));
      return (ServiceProblemService) setTimeOut(service.getServiceProblemServiceImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public SpmServiceImpl getCC2WS(String urlForCC2) throws Exception {
    try {
      URL baseUrl = SpmServiceImplService.class.getResource(".");
      URL url = new URL(baseUrl, urlForCC2);
      SpmServiceImplService service = new SpmServiceImplService(url,
          new QName("http://service.cc.bccs.viettel.com/", "SpmServiceImplService"));
      HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver();
      service.setHandlerResolver(handlerResolver);

      return (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public NTMSTicketManager getTKTUService() throws Exception {
    try {
      URL baseUrl = NTMSTicketManagerService.class.getResource(".");
      URL url = new URL(baseUrl, SERVICE_URL_TKTU);
      NTMSTicketManagerService service = new NTMSTicketManagerService(url,
          new QName("http://webservice.tktu.viettel.com/", "NTMSTicketManagerService"));
      return (NTMSTicketManager) setTimeOut(service.getNTMSTicketManagerPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public SmartOfficeService getChat() throws Exception {
    try {
      URL baseUrl = SmartOfficeService.class.getResource(".");
      URL url = new URL(baseUrl, SERVICE_URL_CHAT);
      SmartOfficeService_Service service = new SmartOfficeService_Service(url,
          new QName("http://smartoffice.viettel.vn", "smartOfficeService"));
      return (SmartOfficeService) setTimeOut(service.getSmartOfficeServicePort());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  public InfraWS getNIMSInfraWS() throws Exception {
    try {
      URL baseUrl = InfraWS_Service.class.getResource(".");
      URL url = new URL(baseUrl, SERVICE_URL_NIMS_2);
      InfraWS_Service service = new InfraWS_Service(url,
          new QName("http://webservice.infra.nims.viettel.com/", "InfraWS"));
      return (InfraWS) setTimeOut(service.getInfraWSPort());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  public ServiceForOtherSystem getSapWs() throws Exception {
    try {
      URL baseUrl = ServiceForOtherSystemImplService.class.getResource(".");
      URL url = new URL(baseUrl, SERVICE_URL_SAP);
      ServiceForOtherSystemImplService service = new ServiceForOtherSystemImplService(url,
          new QName("http://function.webservice.viettel.com/", "ServiceForOtherSystemImplService"));
      return (ServiceForOtherSystem) setTimeOut(service.getServiceForOtherSystemImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public com.viettel.bccs2.SpmServiceImpl getCCV2WS(String urlForCC2) throws Exception {
    try {
      URL baseUrl = com.viettel.bccs2.SpmServiceImplService.class.getResource(".");
      URL url = new URL(baseUrl, urlForCC2);
      com.viettel.bccs2.SpmServiceImplService service = new com.viettel.bccs2.SpmServiceImplService(url,
          new QName("http://service.cc.bccs.viettel.com/", "SpmServiceImplService"));
      HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver();
      service.setHandlerResolver(handlerResolver);

      return (com.viettel.bccs2.SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public Object setTimeOut(Object port) {
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.connect.timeout", CONNECT_TIMEOUT);
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.request.timeout", REQUEST_TIMEOUT);
    return port;
  }

}
