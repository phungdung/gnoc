package com.viettel.gnoc.incident.service;

import com.viettel.bccs2.CauseDTO;
import com.viettel.bccs2.TroubleNetworkSolutionDTO;
import com.viettel.gnoc.commons.dto.ConcaveDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.incident.business.TroublesServiceForCCBusiness;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.CommonDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.utils.TroubleBccsUtils;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
@Slf4j
public class TroublesServiceForCCImpl implements TroublesServiceForCC {

  @Autowired
  protected TroublesServiceForCCBusiness troublesServiceForCCBusiness;

  @Resource
  private WebServiceContext wsContext;

  @Autowired
  TroubleBccsUtils troubleBccsUtils;

  @Override
  public List<ResultDTO> onRollBackTroubleForCC(List<CommonDTO> lstComplaint) {
    log.debug("Request to onRollBackTroubleForCC : {}", lstComplaint);
    setLocale();
    return troublesServiceForCCBusiness.onRollBackTroubleForCC(lstComplaint);
  }

  @Override
  public int onSearchCountForCC(TroublesDTO troublesDTO, List<String> lstCreateUnitIdByCC,
      List<String> lstComplaintTypeId, List<String> lstComplaintParentId,
      List<String> lstTroubleCode) {
    log.debug("Request to onSearchCountForCC : {}", troublesDTO);
    troublesDTO.setLstCreateUnitIdByCC(lstCreateUnitIdByCC);
    troublesDTO.setLstComplaintTypeId(lstComplaintTypeId);
    troublesDTO.setLstComplaintParentId(lstComplaintParentId);
    troublesDTO.setLstTroubleCode(lstTroubleCode);
    return troublesServiceForCCBusiness.onSearchCountForCC(troublesDTO);
  }

  @Override
  public List<TroublesDTO> onSearchForCC(TroublesDTO troublesDTO, List<String> lstCreateUnitIdByCC,
      List<String> lstComplaintTypeId, List<String> lstComplaintParentId,
      List<String> lstTroubleCode,
      Integer startRow, Integer pageLength) {
    log.debug("Request to onSearchForCC : {}", troublesDTO);
    troublesDTO.setLstCreateUnitIdByCC(lstCreateUnitIdByCC);
    troublesDTO.setLstComplaintTypeId(lstComplaintTypeId);
    troublesDTO.setLstComplaintParentId(lstComplaintParentId);
    troublesDTO.setLstTroubleCode(lstTroubleCode);
    return troublesServiceForCCBusiness.onSearchForCC(troublesDTO, startRow, pageLength);
  }

  @Override
  public ResultDTO getTroubleInfoForCC(TroublesDTO troublesDTO) {
    log.debug("Request to getTroubleInfoForCC : {}", troublesDTO);
    return troublesServiceForCCBusiness.getTroubleInfoForCC(troublesDTO);
  }

  @Override
  public ResultDTO onInsertTroubleForCC(TroublesDTO troublesDTO, String[] arrFileName,
      byte[][] arrFileData) {
    log.debug("Request to onInsertTroubleForCC : {}", troublesDTO);
    troublesDTO.setArFileName(arrFileName);
    troublesDTO.setArFileData(arrFileData);
    setLocale();
    return troublesServiceForCCBusiness.onInsertTroubleForCC(troublesDTO);
  }

  @Override
  public TroublesDTO findTroublesById(Long id) {
    log.debug("Request to findTroublesById : {}", id);
    TroublesInSideDTO troublesInSideDTO = troublesServiceForCCBusiness.findTroublesById(id);
    return troublesInSideDTO.toModelOutSide();
  }

  @Override
  public List<String> getSequenseTroubles(String seqName, int... size) {
    log.debug("Request to getSequenseTroubles : {}", seqName);
    return troublesServiceForCCBusiness.getSequenseTroubles(seqName, size);
  }

  @Override
  public ResultDTO reassignTicketForCC(TroublesDTO troublesDTO, String[] arrFileName,
      byte[][] arrFileData) {
    log.debug("Request to reassignTicketForCC : {}", troublesDTO);
    setLocale();
    troublesDTO.setArFileName(arrFileName);
    troublesDTO.setArFileData(arrFileData);
    return troublesServiceForCCBusiness.reassignTicketForCC(troublesDTO);
  }

  @Override
  public List<TroubleActionLogsDTO> getListTroubleActionLog(String troubleCode) {
    log.debug("Request to getListTroubleActionLog : {}", troubleCode);
    return troublesServiceForCCBusiness.getListTroubleActionLog(troubleCode);
  }

  @Override
  public List<TroubleWorklogDTO> getListWorkLog(String troubleCode) {
    log.debug("Request to getListWorkLog : {}", troubleCode);
    List<TroubleWorklogInsiteDTO> lst = troublesServiceForCCBusiness.getListWorkLog(troubleCode);
    List<TroubleWorklogDTO> results = new ArrayList<>();
    if (lst != null && lst.size() > 0) {
      lst.forEach(t -> {
        results.add(t.toOutSite());
      });
    }
    return results;
  }

  @Override
  public String getConcaveByTicket(String troubleCode) {
    try {
      log.debug("Request to getConcaveByTicket : {}", troubleCode);
      return troublesServiceForCCBusiness.getConcaveByTicket(troubleCode);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "";
    }
  }

  @Override
  public ResultDTO sendTicketToTKTU(TroublesDTO troublesDTO) {
    try {
      log.debug("Request to sendTicketToTKTU : {}", troublesDTO);
      setLocale();
      return troublesServiceForCCBusiness.sendTicketToTKTU(troublesDTO.toModelInSide());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(null);
      resultDTO.setKey(Constants.RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
      return resultDTO;
    }
  }

  @Override
  public List<ConcaveDTO> getConcaveByCellAndLocation(List<String> lstCell, String lng,
      String lat) {
    try {
      log.debug("Request to getConcaveByCellAndLocation : {}", lstCell);
      return troublesServiceForCCBusiness.getConcaveByCellAndLocation(lstCell, lng, lat);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ArrayList<>();
    }
  }

  @Override
  public List<UnitDTO> getListUnitByTrouble(String troubleCode) {
    log.debug("Request to getListUnitByTrouble : {}", troubleCode);
    return troublesServiceForCCBusiness.getListUnitByTrouble(troubleCode);
  }

  @Override
  public ResultDTO onUpdateTroubleFromTKTU(TroublesDTO troublesDTO) {
    try {
      log.debug("Request to onUpdateTroubleFromTKTU : {}", troublesDTO);
      setLocale();
      return troublesServiceForCCBusiness.onUpdateTroubleFromTKTU(troublesDTO.toModelInSide());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(null);
      resultDTO.setKey(Constants.RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
      return resultDTO;
    }
  }

  @Override
  public ResultDTO onUpdateTroubleFromWo(TroublesDTO troublesDTO) {
    try {
      log.debug("Request to onUpdateTroubleFromWo : {}", troublesDTO);
      setLocale();
      return troublesServiceForCCBusiness.onUpdateTroubleFromWo(troublesDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(null);
      resultDTO.setKey(Constants.RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
      return resultDTO;
    }
  }

  @Override
  public ResultDTO onUpdateTroubleCC(TroublesDTO troublesDTO) {
    try {
      log.debug("Request to onUpdateTroubleCC : {}", troublesDTO.getTroubleCode());
      return troublesServiceForCCBusiness.onUpdateTroubleCC(troublesDTO);
    } catch (Exception e) {
      log.error(e.getMessage());
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(null);
      resultDTO.setKey(Constants.RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
      return resultDTO;
    }

  }

  @Override
  public List<WoHistoryDTO> getListWoLog(String troubleCode) {
    log.debug("Request to getListWoLog : {}", troubleCode);
    return troublesServiceForCCBusiness.getListWoLog(troubleCode);
  }

  private void setLocale() {
    Locale locale = getLocale();
    if (locale != null) {
      LocaleContextHolder.setLocale(locale);
    }
  }

  private Locale getLocale() {
    String locale = null;
    Locale localeCus = null;
    try {
      MessageContext mcc = wsContext.getMessageContext();
      Message message = ((WrappedMessageContext) mcc).getWrappedMessage();
      List<Header> headers = (List<Header>) message.get(Header.HEADER_LIST);
      for (Header h : headers) {
        Element e = (Element) h.getObject();
        NodeList nodelists = e.getElementsByTagName("language");
        if (nodelists != null && nodelists.getLength() > 0) {
          Node node = nodelists.item(0);
          locale = node.getTextContent();
          if (locale != null && "en".equalsIgnoreCase(locale) || "en_us"
              .equalsIgnoreCase(locale)) {
            localeCus = new Locale("en", "US");
          } else if (locale != null && "vi".equalsIgnoreCase(locale) || "vi_vn"
              .equalsIgnoreCase(locale)) {
            localeCus = new Locale("vi", "VN");
          } else if (locale != null && "lo".equalsIgnoreCase(locale) || "lo_la"
              .equalsIgnoreCase(locale)) {
            localeCus = new Locale("lo", "LA");
          }
          break;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return localeCus;
  }

  @Override
  public List<TroubleNetworkSolutionDTO> getGroupSolutionForCC3(CfgServerNocDTO nocDTO) {
    return troublesServiceForCCBusiness.getGroupSolution(nocDTO);
  }

  @Override
  public List<CauseDTO> getCompCauseDTOForCC3(String parentId, String serviceTypeId,
      String probGroupId, CfgServerNocDTO nocDTO) {
    return troublesServiceForCCBusiness
        .getCompCauseDTOForCC3(parentId, serviceTypeId, probGroupId, nocDTO);
  }

}
