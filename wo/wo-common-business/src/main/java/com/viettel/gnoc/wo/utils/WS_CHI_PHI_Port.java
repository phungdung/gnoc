package com.viettel.gnoc.wo.utils;

import com.viettel.eee2ws.webservice.CPWS;
import com.viettel.eee2ws.webservice.CpRequestParam;
import com.viettel.eee2ws.webservice.Result;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WS_CHI_PHI_Port {

  CPWS port = null;
  @Value("${application.ws.user.chi.phi:null}")
  private String user;
  @Value("${application.ws.pass.chi.phi:null}")
  private String pass;

  @Autowired
  WS_CHI_PHI_PortFactory ws_chi_phi_portFactory;

  @PostConstruct
  public void init() {
    try {
//      user = PassProtector.decrypt(user, WS_USERS.SALT);
//      pass = PassProtector.decrypt(pass, WS_USERS.SALT);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect SpmServiceImpl WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (CPWS) ws_chi_phi_portFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect SpmServiceImpl WS: " + (Calendar.getInstance().getTimeInMillis()
            - startTime));
  }

  public Result confirmSucceedWO(Long type, String woCode, List<CpRequestParam> list) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Result res = port.confirmSucceedWO(user, pass, type, woCode, list);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          ws_chi_phi_portFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

}
