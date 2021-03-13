package com.viettel.gnoc.wo.utils;

import com.viettel.webservice.qlctkt.bccs.BccsService;
import com.viettel.webservice.qlctkt.bccs.ResultService;
import java.net.MalformedURLException;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WS_QLCTKT_BCCS_Port {

  BccsService port = null;
  @Value("${application.ws.user.QLCTKT.BCCS:null}")
  private String user;
  @Value("${application.ws.pass.QLCTKT.BCCS:null}")
  private String pass;

  @Autowired
  WS_QLCTKT_BCCSPortFactory ws_qlctkt_bccsPortFactory;

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
    port = (BccsService) ws_qlctkt_bccsPortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect SpmServiceImpl WS: " + (Calendar.getInstance().getTimeInMillis()
            - startTime));
  }

  public ResultService getDeptManageConnector(Long technology, String connectorCode,
      String splitter) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        ResultService res = port
            .getDeptManageConnector(technology, connectorCode, splitter, user, pass);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          ws_qlctkt_bccsPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public ResultService getListStaffInfraConnector(String connectorCode, Long technology) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        ResultService res = port.getListStaffInfraConnector(connectorCode, technology, user, pass);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          ws_qlctkt_bccsPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

}
