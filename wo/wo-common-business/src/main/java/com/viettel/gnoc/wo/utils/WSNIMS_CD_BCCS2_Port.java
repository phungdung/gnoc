package com.viettel.gnoc.wo.utils;

import com.viettel.nims.webservice.cd.bccs2.BccsWS;
import com.viettel.nims.webservice.cd.bccs2.SubscriptionInfoForm;
import java.net.MalformedURLException;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSNIMS_CD_BCCS2_Port {

  BccsWS port = null;

  @Autowired
  WSNIMS_CD_BCCS2_PortFactory wsnims_cd_bccs2_portFactory;

  @PostConstruct
  public void init() {
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect SpmServiceImpl WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (BccsWS) wsnims_cd_bccs2_portFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect SpmServiceImpl WS: " + (Calendar.getInstance().getTimeInMillis()
            - startTime));
  }

  public SubscriptionInfoForm getSubscriptionInfo(String accountIsdn, Long pointNumber) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        SubscriptionInfoForm res = port.getSubscriptionInfo(accountIsdn, pointNumber);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsnims_cd_bccs2_portFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

}
