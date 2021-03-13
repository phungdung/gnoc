/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.ws;

import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.security.PassTranformer;
import com.viettel.vsaadmin.service.Actor;
import com.viettel.vsaadmin.service.Response;
import com.viettel.vsaadmin.service.VsaadminService;
import java.net.MalformedURLException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VSAAdminPort {

  VsaadminService port = null;

  @Value("${application.ws.actor.vsa_username:null}")
  private String username;
  @Value("${application.ws.actor.vsa_password:null}")
  private String password;
  @Value("${application.ws.appcode:null}")
  private String appCode;
  Actor actor;
  @Autowired
  private VSAAdminPortFactory vsaPortFactory;

  @PostConstruct
  public void init() {
    try {

      if (StringUtils.isNotNullOrEmpty(username) && StringUtils.isNotNullOrEmpty(password)) {
        username = PassTranformer.decrypt(username);
        password = PassTranformer.decrypt(password);
        actor = new Actor();
        actor.setUserName(username);
        actor.setPassword(password);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void createConnect() throws MalformedURLException, Exception {
    port = (VsaadminService) vsaPortFactory.getWsFactory().borrowObject();

  }

  public Response getRoleOfUsers(String username) throws Exception {
    Response res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.getRoleOfUsers(actor, appCode, username);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (null != port) {
          vsaPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

}
