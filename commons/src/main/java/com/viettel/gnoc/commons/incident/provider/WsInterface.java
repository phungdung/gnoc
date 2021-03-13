/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.incident.provider;

import com.sun.xml.ws.client.BindingProviderProperties;
import com.viettel.gnoc.commons.incident.wsclient.WSUtil;
import com.viettel.gnoc.iim.webservice.IimServices;
import com.viettel.nims.infra.webservice.InfraWS;
import com.viettel.nocproV4.NocproWebservice;
import com.viettel.security.service.SecurityProvideService;
import com.viettel.service.SPMProvide;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.viettel.smartoffice.SmartOfficeService;

/**
 * @author ITSOL
 */
@Service("wsInterfaceChat")
@Slf4j
public class WsInterface {

  @Autowired
  @Qualifier("wSUtilChat")
  WSUtil wSUtilChat;

  @Value("${application.ws.recvTimeoutNTMS:60000}")
  private Integer recvTimeoutNTMS;
  @Value("${application.ws.connectTimeoutNTMS:60000}")
  private Integer connectTimeoutNTMS;

  public <T> T createPort(String wsName) {

    if (wsName.contains("CHAT")) {
      return (T) createConnectChatProvider();
    } else if ("NIMS_Infra".equals(wsName)) {
      return (T) createConnectNIMSInfra();
    } else if (wsName.contains("NOCPROV4")) {
      return (T) createConnectNocproV4();
    } else if (wsName.contains("IIM")) {
      return (T) createConnectToIIM();
    } else if ("SECURITYWS".equals(wsName)) {
      return (T) createConnectSecurity();
    } else if (wsName.contains("SPMPROVIDER")) {
      return (T) createConnectSPMProvider();
    }
    return null;
  }

  private SmartOfficeService createConnectChatProvider() {
    try {
      SmartOfficeService port = wSUtilChat.getChat();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.connect.timeout", connectTimeoutNTMS);

      return port;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }

  }

  private InfraWS createConnectNIMSInfra() {
    try {
      InfraWS port = wSUtilChat.getNIMSInfraWS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.connect.timeout", connectTimeoutNTMS);
      return port;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }

  }

  private NocproWebservice createConnectNocproV4() {
    try {
      NocproWebservice port = wSUtilChat.getNOCPROV4WS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.connect.timeout", connectTimeoutNTMS);
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private IimServices createConnectToIIM() {
    try {
      IimServices port = wSUtilChat.getIIMWS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout",
          connectTimeoutNTMS);
      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private SecurityProvideService createConnectSecurity() {
    try {
      SecurityProvideService port = wSUtilChat.getSecurityWS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.connect.timeout", connectTimeoutNTMS);

      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private SPMProvide createConnectSPMProvider() {
    try {
      SPMProvide port = wSUtilChat.getSPMProviderWS();
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.REQUEST_TIMEOUT, recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.receiveTimeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("javax.xml.ws.client.connectionTimeout", connectTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.request.timeout", recvTimeoutNTMS);
      ((BindingProvider) port).getRequestContext()
          .put("com.sun.xml.internal.ws.connect.timeout", connectTimeoutNTMS);

      return port;
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }
}
