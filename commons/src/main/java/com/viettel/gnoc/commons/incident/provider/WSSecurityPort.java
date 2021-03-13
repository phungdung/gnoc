/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.incident.provider;

import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.security.service.Actor;
import com.viettel.security.service.CrsImpactForm;
import com.viettel.security.service.NodeImpactForm;
import com.viettel.security.service.ResultSecurityForm;
import com.viettel.security.service.SecurityProvideService;
import java.net.MalformedURLException;
import java.util.List;
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
public class WSSecurityPort {

  SecurityProvideService port = null;
  @Value("${application.conf.security_userService:null}")
  public String userWS;
  @Value("${application.conf.security_passService:null}")
  public String passWS;
  @Value("${application.conf.security_saltService:null}")
  public String salt;

  @Autowired
  WSSecurityPortFactory wsSecurityPortFactory;

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
    port = (SecurityProvideService) wsSecurityPortFactory.getWsFactory().borrowObject();
  }

  public String receviceCRImpact(CrsImpactForm crBO, List<NodeImpactForm> lstNode)
      throws Exception {
    String res = "";
    try {
      if (port == null) {
        createConnect();
      }
      Actor actor = new Actor();
      actor.setUsername(userWS);
      actor.setPassword(passWS);
      if (port != null) {
        res = port.receviceCRImpact(actor, crBO, lstNode);
        //res=0 la thanh cong
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsSecurityPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public ResultSecurityForm getPasswordAccountsImpact(String exchangeIp, String crNumber,
      String execEmployeeEmail) {
    ResultSecurityForm res = new ResultSecurityForm();
    try {
      if (port == null) {
        createConnect();
      }
      Actor actor = new Actor();
      actor.setUsername(userWS);
      actor.setPassword(passWS);
      if (port != null) {
        res = port.getPasswordAccountsImpact(actor, exchangeIp, crNumber, execEmployeeEmail);
        //res=0 la thanh cong
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);

    } finally {
      try {
        if (null != port) {
          wsSecurityPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public String resetPasswordAfterCompleteCRImpact(String exchangeIp, String crNumber,
      String execEmployeeEmail) throws Exception {
    String res = "";
    try {
      if (port == null) {
        createConnect();
      }
      Actor actor = new Actor();
      actor.setUsername(userWS);
      actor.setPassword(passWS);
      if (port != null) {
        res = port
            .resetPasswordAfterCompleteCRImpact(actor, exchangeIp, crNumber, execEmployeeEmail);

      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsSecurityPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

}
