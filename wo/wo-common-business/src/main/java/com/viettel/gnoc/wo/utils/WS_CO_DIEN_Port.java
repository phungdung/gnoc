package com.viettel.gnoc.wo.utils;

import com.viettel.webservice.codien.EEE2WS;
import com.viettel.webservice.codien.Result;
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
public class WS_CO_DIEN_Port {

  EEE2WS port = null;
  @Value("${application.ws.user.co.dien:null}")
  private String user;
  @Value("${application.ws.pass.co.dien:null}")
  private String pass;

  @Autowired
  WS_CO_DIEN_PortFactory ws_co_dien_portFactory;

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
    System.out.println("Start create connect Co_dien WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (EEE2WS) ws_co_dien_portFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect Co_dien WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));
  }

  public Result updateResultWORepairDevice(String woCode, Long repairResult, String failReason,
      List<String> lstFileName, List<byte[]> lstFileArr) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Result res = port
            .updateResultWORepairDevice(user, pass, woCode, repairResult, failReason, lstFileName,
                lstFileArr);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          ws_co_dien_portFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;

  }

}
