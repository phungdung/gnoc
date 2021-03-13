/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.commons.utils.Constants.WS_USERS;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.ipcc.services.AutoCallOutInput;
import com.viettel.ipcc.services.CallService;
import com.viettel.ipcc.services.NomalOutput;
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
public class IPCCMVTPort {

  CallService port = null;
  @Value("${application.ws.userIpcc:null}")
  private String user;

  @Value("${application.ws.passIpcc:null}")
  private String pass;

  @Autowired
  IPCCMVTPortFactory ipccmvtPortFactory;


  @PostConstruct
  public void init() {
    try {
      user = PassProtector.decrypt(user, WS_USERS.SALT);
      pass = PassProtector.decrypt(pass, WS_USERS.SALT);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void createConnect(String wsLink) throws MalformedURLException, Exception {
    System.out.println("Start create connect IPCCMVT WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (CallService) ipccmvtPortFactory.init(wsLink).borrowObject();
    System.out.println(
        "End create connect IPCCMVT WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));

  }

  public NomalOutput autoCallout(String wsLink, AutoCallOutInput input) {
    try {
      if (port == null) {
        createConnect(wsLink);
      }
      if (port != null) {
        NomalOutput res = port.autoCallout(user, pass, input);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          ipccmvtPortFactory.init(wsLink).returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

}
