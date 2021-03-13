/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.vmsa.ResultDTO;
import com.viettel.vmsa.WSForGNOC;
import java.net.MalformedURLException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WS_SAP_Port {

  WSForGNOC port = null;

  @Value("${application.ws.vipa_userService:null}")
  String userWS;

  @Value("${application.ws.vipa_passService:null}")
  String passWS;

  @Value("${application.ws.vipa_saltService:null}")
  String salt;

  @Autowired
  WS_SAP_PortFactory ws_sap_portFactory;


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
    port = (WSForGNOC) ws_sap_portFactory.getWSFactory().borrowObject();

  }

  public ResultDTO runMop(Long mopId) {
    ResultDTO res = null;
    log.info("--- REQUEST METHOD runMop --- : " + String.valueOf(mopId));
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.runMop(userWS, passWS, mopId);
        StringUtils.printLogData("RESPONSE runMop ", res, ResultDTO.class);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          ws_sap_portFactory.getWSFactory().returnObject(this.port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }
}
