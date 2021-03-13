package com.viettel.gnoc.incident.utils;

import com.viettel.webservice.function.AuthorityBO;
import com.viettel.webservice.function.JsonResponseBO;
import com.viettel.webservice.function.ServiceForOtherSystem;
import java.net.MalformedURLException;
import javax.xml.ws.BindingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class WSSAPPort {

  ServiceForOtherSystem port = null;
  @Value("${application.ws.user_sap}")
  private String userSap;
  @Value("${application.ws.pass_sap}")
  private String passSap;

  @Autowired
  WSSAPPortFactory wssapPortFactory;

  private void createConnect() throws MalformedURLException, Exception {
    port = (ServiceForOtherSystem) wssapPortFactory.getWsFactory().borrowObject();
  }

  public JsonResponseBO autoCheck(String serviceAcc, String ttWoCode) throws Exception {
    JsonResponseBO res = null;
    try {
      AuthorityBO authorityBO = new AuthorityBO();
      authorityBO.setRequestId(Integer.SIZE);
      authorityBO.setUserName(userSap);
      authorityBO.setPassword(passSap);

      if (port == null) {
        createConnect();
      }

      System.out.println(" --serviceAcc -- " + serviceAcc + " --ttWoCode-- " + ttWoCode);
      if (port != null) {
        res = port.autoCheck(authorityBO, serviceAcc, ttWoCode, "TT");
      }


    } catch (Exception e) {
      throw e;
    }
    return res;
  }

  public static Object setTimeOut(Object port) {
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.connect.timeout", 60000);
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.request.timeout", 60000);
    return port;
  }
}
