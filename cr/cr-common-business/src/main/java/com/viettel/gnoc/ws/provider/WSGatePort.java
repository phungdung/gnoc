/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

import com.viettel.gate.webservice.LogBypassUcttBO;
import com.viettel.gate.webservice.ResultDTO;
import com.viettel.gate.webservice.UpdateECRForGNOCService;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.security.PassTranformer;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import viettel.passport.client.UserToken;

/**
 * @author thanhlv12
 */
@Service
@Slf4j
public class WSGatePort {

  UpdateECRForGNOCService port = null;
  @Value("${application.ws.gate_user:null}")
  String userConfigWS;

  @Value("${application.ws.gate_pass:null}")
  String passConfigWS;

  String userWS = "tudn";
  String passWS = "tudn";

  @Autowired
  WSGatePortFactory wsGatePortFactory;

  @Autowired
  UserRepository userRepository;

  @PostConstruct
  public void init() {
    try {
      userWS = PassTranformer.decrypt(userConfigWS);
      passWS = PassTranformer.decrypt(passConfigWS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }


  private void createConnect() throws MalformedURLException, Exception {
    port = (UpdateECRForGNOCService) wsGatePortFactory.getWsFactory().borrowObject();
    if (port == null) {
      throw new Exception("Can not create GatePort");
    }
  }

  boolean validateString(String txt) {
    return !(txt == null || txt.trim().isEmpty());
  }

  public List<ItemDataCR> getListSessionUCTT(String userName) throws Exception {
    List<ItemDataCR> res = new ArrayList<>();
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        ResultDTO resultDTO = port.getListSessionUCTT(userWS, passWS, userName);
        if (resultDTO.getResultCode() == 0) {
          log.info("Get data from Gate " + resultDTO.getResultMessage());
          return new ArrayList<>();
        }
        List<LogBypassUcttBO> lst = resultDTO.getData();

        Double offSet = getOffset();
        offSet = offSet * -1;

        if (lst != null && !lst.isEmpty()) {
          for (LogBypassUcttBO bypassUcttBO : lst) {
            ItemDataCR dataCR = new ItemDataCR();
            dataCR.setValueStr(bypassUcttBO.getBypassUcttId() + "");

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            TimeZone zone = TimeZone.getTimeZone("GMT+7");
            formatter.setTimeZone(zone);

            log.info("bypassUcttBO " + bypassUcttBO.getStartExpTime());
            String startTime = formatter
                .format(bypassUcttBO.getStartExpTime().toGregorianCalendar().getTime());
            String endTime = formatter
                .format(bypassUcttBO.getEndExpTime().toGregorianCalendar().getTime());

            if (startTime != null && !startTime.trim().isEmpty()) {
              startTime = setTimeOffset(startTime, -1 * offSet);
              startTime = DateTimeUtils.convertDateTimeStampToString(
                  DateTimeUtils.convertStringToTime(startTime, DateTimeUtils.patternDateTime));
            }

            if (endTime != null && !endTime.trim().isEmpty()) {
              endTime = setTimeOffset(endTime, -1 * offSet);
              endTime = DateTimeUtils.convertDateTimeStampToString(
                  DateTimeUtils.convertStringToTime(endTime, DateTimeUtils.patternDateTime));
            }

            dataCR.setDisplayStr("[" + startTime + " - " + endTime + "]");
            dataCR.setSecondValue(startTime);
            dataCR.setThirdValue(endTime);
            res.add(dataCR);

          }
        }

      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsGatePortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public ResultDTO updateECR(String userName, String crNumber, String title, Long status,
      String statusName, Long bypassUcttId, String isDelete) {
    StringUtils
        .printLogData("Request Comunicate CR - GATE: method updateECR", userName, String.class);
    log.info("");
    ResultDTO res = new ResultDTO();
    try {
      if (port == null) {
        createConnect();
      }

      if (port != null) {
        Boolean temp = true;
        if ("1".equals(isDelete)) {
          temp = false;
        }
        log.info(
            "-updateECR- " + " -userName- " + userName + " -crNumber- " + crNumber + " -title- "
                + title + " -status- " + status + " -statusName- " + statusName + " -bypassUcttId- "
                + bypassUcttId + " -temp- " + temp);
        ResultDTO resultDTO = port
            .updateECR(userWS, passWS, userName, crNumber, title, status, statusName, bypassUcttId,
                temp);
        StringUtils.printLogData("ResultDTO", resultDTO, ResultDTO.class);
        if (resultDTO.getResultCode() == 0) {
          log.info("Update to Gate " + resultDTO.getResultMessage());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsGatePortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  private Double getOffset() {
    Double offSet;
    try {
      UserToken userToken = TicketProvider.getUserToken();
      offSet = userRepository.getOffsetFromUser(userToken.getUserID());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      offSet = 0D;
    }
    return offSet;
  }

  public String setTimeOffset(String time, Double offSet) {
    SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    try {
      if (time != null && !"".equals(time)) {
        Date d = spd.parse(time);
        return spd.format(new Date(d.getTime() + (long) (offSet * 60 * 60 * 1000)));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return "";
  }
}
