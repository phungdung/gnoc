/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ConcaveDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.soc.spm.service.AuthorityDTO;
import com.viettel.tktu.webservice.ConcaveForm;
import com.viettel.tktu.webservice.ConcavePointWithCell;
import com.viettel.tktu.webservice.InputUpdateTicketForConcavePoint;
import com.viettel.tktu.webservice.MessageReturnBO;
import com.viettel.tktu.webservice.NTMSTicketManager;
import com.viettel.tktu.webservice.OutputUpdateTicketForConcavePoint;
import com.viettel.tktu.webservice.TroubleDTO;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WSTKTUPort {

  NTMSTicketManager port = null;
  public static final AuthorityDTO requestSPM = new AuthorityDTO();
  @Autowired
  WSTKTUPortFactory wstktuPortFactory;


  private void createConnect() throws MalformedURLException, Exception {
    port = (NTMSTicketManager) wstktuPortFactory.getWsFactory().borrowObject();

  }

  public MessageReturnBO sendTicketToTKTU(TroublesInSideDTO trouble) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        TroubleDTO troubleDTO = new TroubleDTO();
        troubleDTO.setLocationId(
            trouble.getLocationId() == null ? 0 : trouble.getLocationId());

        // lay them thong tin ve ticket
        troubleDTO.setCells(trouble.getCellService());  // cell phuc vu
        XMLGregorianCalendar xmlStart = toXMLGregorianCalendar(
            trouble.getCreatedTime());
        troubleDTO.setCreatedTime(xmlStart);//thoi gian hen
        if (trouble.getDeferredTime() != null) {
          XMLGregorianCalendar xmlEnd = toXMLGregorianCalendar(
              trouble.getDeferredTime());
          troubleDTO.setDeadLine(xmlEnd);
        }
        troubleDTO.setDescription(trouble.getDescription());
        troubleDTO.setAssignedId(
            trouble.getCreateUserId() == null ? 0 : trouble.getCreateUserId());
        troubleDTO.setReason(trouble.getRootCause());
        troubleDTO.setSolution(trouble.getWorkArround());
        troubleDTO.setTroubleCode(trouble.getTroubleCode());
        troubleDTO.setTroubleGroupId(
            trouble.getTypeId() == null ? 0 : trouble.getTypeId());
        troubleDTO.setTroubleName(trouble.getTroubleName());
        troubleDTO.setStatusNtms(trouble.getStateName());

        MessageReturnBO returnBO = port.insertTroubleTKTU(troubleDTO);
        return returnBO;

      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wstktuPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public String getConcaveByTicket(String troubleCode) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        ConcaveForm cvForm = port.getConcaveByTicket(troubleCode);
        if (cvForm != null && cvForm.getConcaveCode() != null) {
          return cvForm.getConcaveCode();
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wstktuPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return "";
  }

  public List<ConcaveDTO> getConcavePointByListCellForGNOC(List<String> lstCell, String lng,
      String lat, String radius) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        Double longitude = Double.valueOf(lng);
        Double latitude = Double.valueOf(lat);
        Double rs = Double.valueOf(radius);
        List<ConcaveDTO> lst = new ArrayList<>();
        List<ConcavePointWithCell> lstConcave = port
            .getConcavePointByListCellForGNOC(null, null, null, lstCell, longitude, latitude, rs, 0,
                200);

        if (lstConcave != null && !lstConcave.isEmpty()) {
          for (ConcavePointWithCell cell : lstConcave) {
            ConcaveDTO concaveDTO = new ConcaveDTO();
            concaveDTO.setConcavePointCode(cell.getConcavePointCode());
            concaveDTO.setApproveStatusName(cell.getApproveStatusName());
            concaveDTO.setCells(cell.getCells());
            concaveDTO.setLat(cell.getLat());
            concaveDTO.setLng(cell.getLng());
            concaveDTO.setLocationNameFull(cell.getLocationNameFull());
            concaveDTO.setNetworkTypeName(cell.getNetworkTypeName());
            concaveDTO.setStatusName(cell.getStatusName());
            concaveDTO.setSolutionName(cell.getSolutionName());
            concaveDTO.setPlanProcess(DateTimeUtils
                .convertDateToString(toDate(cell.getPlanProcess()), Constants.ddMMyyyyHHmmss));
            concaveDTO.setCabinetCode(cell.getCabinetCode());
            lst.add(concaveDTO);
          }
        }
        return lst;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wstktuPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }


  public String updateTicketForConcavePoint(String username, String password,
      InputUpdateTicketForConcavePoint input) throws Exception {
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        OutputUpdateTicketForConcavePoint output = port
            .updateTicketForConcavePoint(username, password, input);

        if (output != null && output.getStatus() == 1) {
          return "";
        } else if (output != null) {
          throw new Exception(
              I18n.getLanguage("transitionStateConfig.import.haveSomeError") + " TKTU: " + output
                  .getMessage());
        }
        throw new Exception(
            I18n.getLanguage("transitionStateConfig.import.haveSomeError") + " TKTU: ERROR");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    } finally {
      try {
        if (null != port) {
          wstktuPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return null;
  }

  public XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
    if (date != null) {
      GregorianCalendar gCalendar = new GregorianCalendar();
      gCalendar.setTime(date);
      XMLGregorianCalendar xmlCalendar = null;
      try {
        xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
      return xmlCalendar;
    }
    return null;
  }

  public static Date toDate(XMLGregorianCalendar calendar) {
    if (calendar == null) {
      return null;
    }
    return calendar.toGregorianCalendar().getTime();
  }

}
