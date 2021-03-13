/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ConcaveDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.tktu.webservice.InputUpdateTicketForConcavePoint;
import com.viettel.tktu.webservice.MessageReturnBO;
import com.viettel.tktu.webservice.TroubleBO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author quangdx
 */

@Slf4j
@Service
public class TroubleTktuUtils {

  @Autowired
  WSTKTUPort wstktuPort;

  public ResultDTO sendTicketToTKTU(TroublesInSideDTO tForm) throws Exception {
    ResultDTO res = new ResultDTO();
    try {
      //WSTKTUPort port = new WSTKTUPort();
      MessageReturnBO returnBO = wstktuPort.sendTicketToTKTU(tForm);
      if (returnBO != null) {
        if (returnBO.getSuccessCode() != null && "OK".equalsIgnoreCase(returnBO.getSuccessCode())) {
          res.setKey("SUCCESS");
          res.setMessage(I18n.getString("incident.send.tktu"));
        } else {
          res.setKey("FAIL");
          res.setMessage(returnBO.getSuccessCode() + " " + returnBO.getSuccessCode());
        }
      } else {
        res.setKey("FAIL");
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }

    return res;

  }

  public String getConcaveByTicket(String troubleCode) throws Exception {
    try {
      // WSTKTUPort port = new WSTKTUPort();
      return wstktuPort.getConcaveByTicket(troubleCode);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  public List<ConcaveDTO> getConcaveByCellAndLocation(List<String> lstCell, String lng, String lat,
      String radius) throws Exception {
    try {
      //WSTKTUPort port = new WSTKTUPort();
      return wstktuPort.getConcavePointByListCellForGNOC(lstCell, lng, lat, radius);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  public String updateTicketForConcavePoint(
      String troubleCode, String troubleName, String concaveCode, String locationId)
      throws Exception {
    try {
      //WSTKTUPort port = new WSTKTUPort();
      InputUpdateTicketForConcavePoint input = new InputUpdateTicketForConcavePoint();
      input.setConcavePointCode(concaveCode);
      TroubleBO troubleBO = new TroubleBO();
      troubleBO.setTroubleCode(troubleCode);
      troubleBO.setTroubleName(troubleName);
      troubleBO.setLocationId(Long.parseLong(locationId));
      input.setTicketInfo(troubleBO);
      return wstktuPort
          .updateTicketForConcavePoint("6b76abe1e355fe29", "d6ad379dace5feae0b8a79fa144d6b1d",
              input);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

}
