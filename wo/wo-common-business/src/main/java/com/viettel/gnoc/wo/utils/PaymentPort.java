package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.commons.utils.Constants.WS_USERS;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.payment.services.PaymentServices;
import com.viettel.payment.services.StaffBean;
import java.net.MalformedURLException;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author ITSOL
 */

@Slf4j
@Service
public class PaymentPort {

  PaymentServices port = null;
  @Value("${application.ws.userPayment:null}")
  private String user;
  @Value("${application.ws.passPayment:null}")
  private String pass;

  @Autowired
  PaymentPortFactory paymentPortFactory;

  @PostConstruct
  public void init() {
    try {
      user = PassProtector.decrypt(user, WS_USERS.SALT);
      pass = PassProtector.decrypt(pass, WS_USERS.SALT);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect Payment WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (PaymentServices) paymentPortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect Payment WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));
  }

  public StaffBean getStaffByIsdn(String isdn) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        StaffBean res = port.getStaffByIsdn(isdn, user, pass);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          paymentPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

}
