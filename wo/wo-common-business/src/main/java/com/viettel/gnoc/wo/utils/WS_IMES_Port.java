/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.HeaderForm;
import com.viettel.security.PassTranformer;
import com.viettel.webservice.imes.QltnConcentrateWS;
import com.viettel.webservice.imes.ResultQoS;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author TamdX
 */
@Slf4j
@Service
public class WS_IMES_Port {

  List<HeaderForm> lstHeader;
  String nationCoce;

  QltnConcentrateWS port = null;

  @Value("${application.ws.imes.user:null}")
  private String user;
  @Value("${application.ws.imes.pass:null}")
  private String pass;

  @Autowired
  WS_IMES_PortFactory ws_imes_portFactory;


  @PostConstruct
  public void init() {
    try {
      user = PassTranformer.decrypt(user);
      pass = PassTranformer.decrypt(pass);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public void setNationCode(String nationCode) {
    this.nationCoce = nationCode;
    if (!StringUtils.isStringNullOrEmpty(nationCode)) {
      this.lstHeader = new ArrayList<>();
      this.lstHeader.add(new HeaderForm("nationCode", nationCode));
    }
  }

  public void clearData() {
    lstHeader = null;
    nationCoce = null;
  }

  public void setLstHeader(List<HeaderForm> lstHeader) {
    this.lstHeader = lstHeader;
  }

  private void createConnect() throws MalformedURLException, Exception {
    System.out.println("Start create connect iMes WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (QltnConcentrateWS) ws_imes_portFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect iMes WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));

  }

  //truoc kh igoi phai set value nationCode, header truoc
  public ResultQoS checkQoS(String stationCode, Long type) {
    ResultQoS res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        SetHeaderWs.setHeaderHandler(port, lstHeader);
        res = port.checkQoS(user, pass, stationCode, type);
        clearData();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      clearData();
      try {
        if (null != port) {
          ws_imes_portFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;

  }


}
