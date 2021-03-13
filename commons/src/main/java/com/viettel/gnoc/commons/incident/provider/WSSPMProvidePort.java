/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.incident.provider;

import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.service.PhoneNumberForm;
import com.viettel.service.SPMProvide;
import com.viettel.service.SpmCrsSubsciberForm;
import com.viettel.service.ValidateTraceForm;
import java.net.MalformedURLException;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSSPMProvidePort {

  SPMProvide port = null;
  @Value("${application.conf.trace_phone_username:null}")
  public String userWS;
  @Value("${application.conf.trace_phone_password:null}")
  public String passWS;
  @Value("${application.conf.trace_phone_salt:null}")
  public String salt;

  @Autowired
  WSSPMProvideFactory wsspmProvideFactory;

  @PostConstruct
  public void init() {
    try {
      userWS = PassProtector.decrypt(userWS, salt);
      passWS = PassProtector.decrypt(passWS, salt);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void createConnect() throws MalformedURLException, Exception {
//        System.out.println("Start create connect NTMS WS ");
    port = (SPMProvide) wsspmProvideFactory.getWsFactory().borrowObject();
//        System.out.println("End create connect NTMS WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));

  }

  public ValidateTraceForm validateUserPhoneNumber(List<PhoneNumberForm> lstPhone)
      throws Exception {
    ValidateTraceForm res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        ValidateTraceForm form = new ValidateTraceForm();
        form.getLstPhoneNumber().addAll(lstPhone);
        res = port.validateUserPhoneNumber(form, userWS, passWS);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsspmProvideFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public String receviceOrCompleteCRs(SpmCrsSubsciberForm form) throws Exception {
    String res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        StringUtils.printLogData("Connect method receviceOrCompleteCRs() to Trace PhoneNumber: ", form, SpmCrsSubsciberForm.class);
        res = port.receviceOrCompleteCRs(form, userWS, passWS);
        log.info("Response receviceOrCompleteCRs: " + String.valueOf(res));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsspmProvideFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

}
