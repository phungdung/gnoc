/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.ws;

import com.sun.xml.ws.client.BindingProviderProperties;
import com.viettel.gnoc.commons.ws.utils.WSCommonUtil;
import com.viettel.vsaadmin.service.VsaadminService;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author ITSOL
 */
@Service
@Slf4j
public class WsCommonInterface {

  @Autowired
  WSCommonUtil wsCommonUtil;

  @Value("${application.ws.recvTimeoutNTMS:60000}")
  private Integer recvTimeoutNTMS;
  @Value("${application.ws.connectTimeoutNTMS:60000}")
  private Integer connectTimeoutNTMS;

  public <T> T createPort(String wsName) {

    if (wsName.contains("WS_VSA")) {
      return (T) createConnectVSA();
    }
    return null;
  }

  private VsaadminService createConnectVSA() {
    try {
      VsaadminService port = wsCommonUtil.getVSAWS();
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
