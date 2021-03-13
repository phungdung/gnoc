/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.od.wsclient;

import com.sun.xml.ws.client.BindingProviderProperties;
import com.viettel.nims.webservice.ht.InfraWS;
import com.viettel.voffice.ws_autosign.service.Vo2AutoSignSystemImpl;
import javax.xml.ws.BindingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author thanhlv12
 */
@Service
public class WsInterface {

  @Autowired
  WSUtil wsUtil;
  @Value("${application.ws.recvTimeoutNTMS:120000}")
  private Integer recvTimeoutNTMS;
  @Value("${application.ws.connectTimeoutNTMS:60000}")
  private Integer connectTimeoutNTMS;

  protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger
      .getLogger(WsInterface.class);

  public <T> T createPort(String wsName, String wsLink) {
    if ("NIMS_HT".equalsIgnoreCase(wsName)) {
      return (T) createConnectNIMSHT();
    } else if ("VOFFICE".equalsIgnoreCase(wsName)) {
      return (T) createConnectVoffice();
    } else {
      return null;
    }
  }

  private InfraWS createConnectNIMSHT() {
    try {
      InfraWS port = wsUtil.getNIMSHTService();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeoutNTMS);
      return port;
    } catch (Exception exx) {
      logger.error(exx.getMessage(), exx);
//            exx.printStackTrace();
      return null;
    }

  }

  private Vo2AutoSignSystemImpl createConnectVoffice() {
    try {
      Vo2AutoSignSystemImpl port = wsUtil.getVofficePort();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeoutNTMS);
      return port;
    } catch (Exception exx) {
      logger.error(exx.getMessage(), exx);
      return null;
    }

  }

}
