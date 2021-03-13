package com.viettel.gnoc.wo.utils;

import java.net.MalformedURLException;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSNIMS_CDPort {

  com.viettel.nims.webservice.BccsWS port = null;

  @Autowired
  WSNIMS_CDPortFactory wsnims_cdPortFactory;

  @PostConstruct
  public void init() {
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect SpmServiceImpl WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (com.viettel.nims.webservice.BccsWS) wsnims_cdPortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect SpmServiceImpl WS: " + (Calendar.getInstance().getTimeInMillis()
            - startTime));
  }

  public com.viettel.nims.webservice.SubscriptionInfoForm getSubscriptionInfo(String accountIsdn,
      Long pointNumber) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        com.viettel.nims.webservice.SubscriptionInfoForm res = port
            .getSubscriptionInfo(accountIsdn, pointNumber);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsnims_cdPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }
}
