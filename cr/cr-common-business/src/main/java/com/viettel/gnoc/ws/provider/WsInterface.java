/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

import com.sun.xml.ws.client.BindingProviderProperties;
import com.viettel.aam.TdttWebservice;
import com.viettel.bikt.webservices.ServiceGNOC;
import com.viettel.gate.webservice.UpdateECRForGNOCService;
import com.viettel.gnoc.ws.util.WSUtil;
import com.viettel.vmsa.WSForGNOC;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author thanhlv12
 */
@Slf4j
@Service("wSCrInterface")
public class WsInterface {

  @Autowired
  @Qualifier("wSCrUtil")
  WSUtil wsUtil;

  @Value("${application.ws.recvTimeoutNTMS:60000}")
  private Integer recvTimeoutNTMS;

  @Value("${application.ws.connectTimeoutNTMS:60000}")
  private Integer connectionTimeout;

  public <T> T createPort(String wsName) {
    if (wsName.contains("VIPADD")) {
      return (T) createConnectVIPADD();
    } else if (wsName.contains("VIPAIP")) {
      return (T) createConnectVIPAIP();
    } else if (wsName.contains("TDTT")) {
      return (T) createConnectTDTT();
    } else if (wsName.contains("BIKTWS")) {
      return (T) createConnectBIKT();
    } else if (wsName.contains("GATE")) {
      return (T) createConnectToGate();
    }
    return null;
  }

  private WSForGNOC createConnectVIPADD() {
    try {
      WSForGNOC port = wsUtil.getVIPADDWS();
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
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private com.viettel.vipa.WSForGNOC createConnectVIPAIP() {
    try {
      com.viettel.vipa.WSForGNOC port = wsUtil.getVIPAIPWS();
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
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private TdttWebservice createConnectTDTT() {
    try {
      TdttWebservice port = wsUtil.getTDTTWS();
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
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private ServiceGNOC createConnectBIKT() {
    try {
      ServiceGNOC port = wsUtil.getBIKTWS();
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
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

  private UpdateECRForGNOCService createConnectToGate() {
    try {
      UpdateECRForGNOCService port = wsUtil.getGateWS();
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
    } catch (Exception exx) {
      log.error(exx.getMessage(), exx);
      return null;
    }
  }

}
