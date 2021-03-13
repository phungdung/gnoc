package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.wo.dto.HeaderForm;
import com.viettel.webservice.nims_cd_global.BccsWS;
import com.viettel.webservice.nims_cd_global.SubscriptionInfoForm;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Getter
@Setter
public class WSNIMS_CD_GLOBAL_Port {

  BccsWS port = null;

  List<HeaderForm> lstHeader;
  String nationCoce;

  @Autowired
  WSNIMS_CD_GLOBAL_PortFactory wsnims_cd_global_portFactory;

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect WSNIMS_CD_GLOBAL WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (BccsWS) wsnims_cd_global_portFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect WSNIMS_CD_GLOBAL WS: " + (Calendar.getInstance().getTimeInMillis()
            - startTime));
  }

  public SubscriptionInfoForm getSubscriptionInfo(String accountIsdn, Long pointNumber) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        SetHeaderWs.setHeaderHandler(port, lstHeader);
        SubscriptionInfoForm res = port.getSubscriptionInfo(accountIsdn, pointNumber);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsnims_cd_global_portFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;

  }

}
