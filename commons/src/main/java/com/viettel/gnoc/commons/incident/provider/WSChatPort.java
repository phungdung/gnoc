/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.incident.provider;

import com.viettel.gnoc.commons.utils.ws.PassProtector;
import java.net.MalformedURLException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.viettel.smartoffice.GroupBusiness;
import vn.viettel.smartoffice.GroupResponse;
import vn.viettel.smartoffice.SmartOfficeService;

@Service
@Slf4j
public class WSChatPort {

  SmartOfficeService port = null;

  @Value("${application.ws.user_chat:null}")
  private String userNameEncript;
  @Value("${application.ws.pass_chat:null}")
  private String passwordEncript;
  @Value("${application.ws.appCode:null}")
  private String appCode;
  @Value("${application.ws.chat_salt:null}")
  private String salt;

  String username;
  String password;

  @Autowired
  private WSChatPortFactory wsChatPortFactory;

  @PostConstruct
  public void init() {
    try {
      username = PassProtector.decrypt(userNameEncript, salt);
      password = PassProtector.decrypt(passwordEncript, salt);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void createConnect() throws MalformedURLException, Exception {
    port = (SmartOfficeService) wsChatPortFactory.getWsFactory().borrowObject();

  }

  public GroupResponse createGroupInBusiness2(GroupBusiness business) throws Exception {
    GroupResponse res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        business.setAppCode(appCode);
        res = port.createGroupInBusiness2(username, password, business);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (null != port) {
          wsChatPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

}
