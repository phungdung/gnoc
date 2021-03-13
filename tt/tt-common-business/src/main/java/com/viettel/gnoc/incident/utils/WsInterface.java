/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

import com.sun.xml.ws.client.BindingProviderProperties;
import com.viettel.bccs.cc.service.SpmServiceImpl;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.nims.infra.webservice.InfraWS;
import com.viettel.soc.spm.service.ServiceProblemService;
import com.viettel.tktu.webservice.NTMSTicketManager;
import com.viettel.webservice.function.ServiceForOtherSystem;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author thanhlv12
 */
@Service
@Slf4j
public class WsInterface {

  @Autowired
  WSUtil wsUtil;

  @Value("${application.ws.recvTimeoutNTMS:60000}")
  private Integer recvTimeoutNTMS;
  @Value("${application.ws.connectTimeoutNTMS:60000}")
  private Integer connectionTimeout;

  public <T> T createPort(String wsName) {
    if (!StringUtils.isStringNullOrEmpty(wsName)) {
      if ("SPM".equals(wsName)) {
        return (T) createConnectSPM();
      } else if ("TKTU".equals(wsName)) {
        return (T) createConnectTKTU();
      } else if (wsName.contains("CC2")) {
        String url = wsName.substring(wsName.indexOf("CC2###") + 6, wsName.length());
        return (T) createConnectBCCS2(url);
      } else if ("NIMS_Infra".equals(wsName)) {
        return (T) createConnectNIMSInfra();
      } else if (wsName.contains("SAP")) {
        return (T) createConnectSapProvider();
      } else if (wsName.contains("CCV2")) {
        String url = wsName.substring(wsName.indexOf("CCV2###") + 7, wsName.length());
        return (T) createConnectBCCSV2(url);
      }
    }
    return null;
  }

  private ServiceProblemService createConnectSPM() {
    try {
      ServiceProblemService port = wsUtil.getSPMWS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.connect.timeout", connectionTimeout);
      return port;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  private SpmServiceImpl createConnectBCCS2(String url) {
    try {
      SpmServiceImpl port = wsUtil.getCC2WS(url);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.connect.timeout", connectionTimeout);
      return port;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  private NTMSTicketManager createConnectTKTU() {
    try {
      NTMSTicketManager port = wsUtil.getTKTUService();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.connect.timeout", connectionTimeout);
      return port;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  private InfraWS createConnectNIMSInfra() {
    try {
      InfraWS port = wsUtil.getNIMSInfraWS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.connect.timeout", connectionTimeout);
      return port;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  private ServiceForOtherSystem createConnectSapProvider() {
    try {
      ServiceForOtherSystem port = wsUtil.getSapWs();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.connect.timeout", connectionTimeout);
      return port;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }

  private com.viettel.bccs2.SpmServiceImpl createConnectBCCSV2(String url) {
    try {
      com.viettel.bccs2.SpmServiceImpl port = wsUtil.getCCV2WS(url);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectionTimeout);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.connect.timeout", connectionTimeout);
      return port;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

}
