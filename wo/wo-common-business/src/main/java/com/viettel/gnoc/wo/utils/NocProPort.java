package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.nocproV4.AuthorityBO;
import com.viettel.nocproV4.JsonResponseBO;
import com.viettel.nocproV4.NocproWebservice;
import com.viettel.nocproV4.RequestInputBO;
import java.net.MalformedURLException;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NocProPort {

  NocproWebservice port = null;
  @Value("${application.ws.userNocPro:null}")
  private String user;
  @Value("${application.ws.passNocPro:null}")
  private String pass;

  @Autowired
  NocProPortFactory nocProPortFactory;

  @PostConstruct
  public void init() {
    try {
      user = PassProtector.decrypt(user, Constants.WS_USERS.SALT);
      pass = PassProtector.decrypt(pass, Constants.WS_USERS.SALT);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect IPCC WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (NocproWebservice) nocProPortFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect IPCC WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));
  }

  public JsonResponseBO getDataJson(RequestInputBO in) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        AuthorityBO au = new AuthorityBO();
        au.setUserName(user);
        au.setPassword(pass);
        au.setRequestId(Integer.valueOf(
            System.currentTimeMillis() / 1000 / 60 + ""));// thoi gian hien tai quy ra mili second

        JsonResponseBO res = port.getDataJson(au, in);

        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          nocProPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public JsonResponseBO onExecuteMapQuery(RequestInputBO in) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        AuthorityBO au = new AuthorityBO();
        au.setUserName(user);
        au.setPassword(pass);
        au.setRequestId(Integer.valueOf(
            System.currentTimeMillis() / 1000 / 60 + ""));// thoi gian hien tai quy ra mili second

        JsonResponseBO res = port.onExecuteMapQuery(au, in);

        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          nocProPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }


}
