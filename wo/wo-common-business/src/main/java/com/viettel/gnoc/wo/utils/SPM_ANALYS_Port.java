package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.spm.analys.webservice.AnalysProcessService;
import com.viettel.spm.analys.webservice.AuthorityDTO;
import com.viettel.spm.analys.webservice.ParamBO;
import com.viettel.spm.analys.webservice.ResultDTO;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SPM_ANALYS_Port {

  AnalysProcessService port = null;

  @Autowired
  SPM_ANALYS_PortFactory spm_analys_portFactory;

  @Value("${application.ws.userSpmAnalys:null}")
  private String user;

  @Value("${application.ws.passSpmAnalys:null}")
  private String pass;

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
    System.out.println("Start create connect KTTS WS ");
    long startTime = Calendar.getInstance().getTimeInMillis();
    port = (AnalysProcessService) spm_analys_portFactory.getWsFactory().borrowObject();
    System.out.println(
        "End create connect KTTS WS: " + (Calendar.getInstance().getTimeInMillis() - startTime));
  }

  public ResultDTO doRequestAnalys(List<ParamBO> lstParam) {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        AuthorityDTO adto = new AuthorityDTO();
        adto.setPassword(pass);
        adto.setUsername(user);
        adto.setPartyCode("WO");

        ResultDTO res = port.doRequestAnalys(adto, lstParam);
        if (res != null) {
          return res;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          spm_analys_portFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

}
