/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.od.wsclient;

import com.viettel.nims.webservice.ht.InfraWS;
import com.viettel.nims.webservice.ht.InfraWS_Service;
import com.viettel.voffice.ws_autosign.service.Vo2AutoSignSystemImpl;
import com.viettel.voffice.ws_autosign.service.Vo2AutoSignSystemImplService;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author ThanhLV12
 */
@Service
public class WSUtil {

  protected static Logger logger = Logger.getLogger(WSUtil.class);
  ;
  static WSUtil instance;

  @Value("${application.ws.nims_ht_url}")
  public String SERVICE_URL_NIMS_HT;
  @Value("${application.ws.voffice_url}")
  public String SERVICE_URL_VOFFICE;

  public static final int CONNECT_TIMEOUT = 60000;//ms
  public static final int REQUEST_TIMEOUT = 60000;//ms

  public static synchronized WSUtil getInstance() {
    if (instance == null) {
      instance = new WSUtil();
    }
    return instance;
  }

  public Vo2AutoSignSystemImpl getVofficePort() throws Exception {
    try {
      URL url;
      URL baseUrl;
      Vo2AutoSignSystemImplService service;
      baseUrl = Vo2AutoSignSystemImplService.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_VOFFICE);
      service = new Vo2AutoSignSystemImplService(url,
          new QName("http://service.ws_autosign.voffice.viettel.com/",
              "Vo2AutoSignSystemImplService"));
      return (Vo2AutoSignSystemImpl) setTimeOut(service.getVo2AutoSignSystemImplPort());
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public InfraWS getNIMSHTService() throws Exception {
    try {
      URL url;
      URL baseUrl;
      InfraWS_Service service;
      baseUrl = InfraWS_Service.class.getResource(".");
      url = new URL(baseUrl, SERVICE_URL_NIMS_HT);
      service = new InfraWS_Service(url,
          new QName("http://webservice.infra.nims.viettel.com/", "InfraWS"));
      return (InfraWS) setTimeOut(service.getInfraWSPort());
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
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
