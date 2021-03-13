/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

import com.viettel.security.PassTranformer;
import com.viettel.soc.spm.service.AuthorityDTO;
import com.viettel.soc.spm.service.ResultDTO;
import com.viettel.soc.spm.service.ServiceProblemInfoDTO;
import com.viettel.soc.spm.service.ServiceProblemService;
import java.net.MalformedURLException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSSPMPort {

  @Value("${application.ws.spm.party_code:null}")
  public String partyCode;

  @Value("${application.ws.spm.pass:null}")
  public String password;

  @Value("${application.ws.spm.requestId:null}")
  public String requestId;

  @Value("${application.ws.spm.username:null}")
  public String username;

  ServiceProblemService port = null;
  public static final AuthorityDTO requestSPM = new AuthorityDTO();

  @Autowired
  WSSPMPortFactory wsspmPortFactory;

  @PostConstruct
  public void init() {
    try {
      username = PassTranformer.decrypt(username);
      password = PassTranformer.decrypt(password);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void createConnect() throws MalformedURLException, Exception {
    //System.out.println("Start create connect SPM WS ");
//        long startTime = Calendar.getInstance().getTimeInMillis();
    port = (ServiceProblemService) wsspmPortFactory.getWsFactory().borrowObject();
    //System.out.println("End create connect SPM WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));

  }

  public void createAuthen() {
    requestSPM.setPartyCode(partyCode);
    requestSPM.setPassword(password);
    requestSPM.setRequestId(requestId);
    requestSPM.setUsername(username);
  }

  public ResultDTO updateSpmCompleteNotCheck(ServiceProblemInfoDTO dto) throws Exception {
    ResultDTO res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        createAuthen();
        res = port.updateSpmCompleteNotCheck(requestSPM, dto);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsspmPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        // ignored
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }


  public ResultDTO updateSpmAction(ServiceProblemInfoDTO dto) throws Exception {
    ResultDTO res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.updateSpmAction(dto);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsspmPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        // ignored
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }


}
