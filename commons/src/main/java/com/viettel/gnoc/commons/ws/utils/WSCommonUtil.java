/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.ws.utils;

import com.viettel.nims.infra.webservice.InfraWS_Service;
import com.viettel.vsaadmin.service.VsaadminService;
import com.viettel.vsaadmin.service.VsaadminServiceService;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WSCommonUtil {

  @Value("${application.ws.service_url_vsa:null}")
  public String SERVICE_URL_VSA;
  public static final int CONNECT_TIMEOUT = 60000;//ms
  public static final int REQUEST_TIMEOUT = 60000;//ms

  public VsaadminService getVSAWS() throws Exception {
    try {
      URL baseUrl = InfraWS_Service.class.getResource(".");
      URL url = new URL(baseUrl, SERVICE_URL_VSA);
      VsaadminServiceService service = new VsaadminServiceService(url,
          new QName("http://service.vsaadmin.viettel.com/", "VsaadminServiceService"));
      return (VsaadminService) setTimeOut(service.getVsaadminServicePort());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
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
