package com.viettel.gnoc.wo.service;

import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.WS_RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.business.WoBusiness;
import com.viettel.gnoc.wo.business.WoSPMBusiness;
import com.viettel.gnoc.wo.business.WoTicketBusiness;
import com.viettel.gnoc.wo.business.WoVSmartBusiness;
import com.viettel.gnoc.wo.dto.AmiOneForm;
import com.viettel.gnoc.wo.dto.CfgSupportForm;
import com.viettel.gnoc.wo.dto.ObjFile;
import com.viettel.gnoc.wo.dto.ObjResponse;
import com.viettel.gnoc.wo.dto.SearchWoKpiCDBRForm;
import com.viettel.gnoc.wo.dto.Wo;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import com.viettel.gnoc.wo.dto.WoWorklogDTO;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WoServicesImpl implements WoServices {

  @Autowired
  protected WoBusiness woBusiness;

  @Autowired
  protected WoVSmartBusiness woVSmartBusiness;

  @Autowired
  protected WoSPMBusiness woSPMBusiness;

  @Autowired
  WoTicketBusiness woTicketBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<WoDTO> getListWoDTO(WoDTO woDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    if (woDTO != null) {
      return woBusiness.getListWoDTO(woDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }

  @Override
  public ResultDTO getResultCallIPCC(String userName, String passWord, String input) {
    return woBusiness.getResultCallIPCC(userName, passWord, input);
  }

  @Override
  public ResultDTO checkInfraForComplete(WoDTO woDTO) {
    return woVSmartBusiness.checkInfraForComplete(woDTO);
  }

  @Override
  public ResultDTO insertWo(WoDTO woDTO){
    try{
      I18n.setLocaleForService(wsContext);
      return woBusiness.insertWo(woDTO);
    }catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }

  }

  @Override
  public ResultDTO updateHelpFromSPM(String woCode, String description, Long result) {
    try {
      I18n.setLocaleForService(wsContext);
      return woSPMBusiness.updateHelpFromSPM(woCode, description, result);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public ResultDTO updateWoInfo(WoDTO woDTO) {
    try {
      I18n.setLocaleForService(wsContext);
      return woBusiness.updateWoInfo(woDTO);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO("0", RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public List<SearchWoKpiCDBRForm> searchWoKpiCDBR(String startTime, String endTime) {
    I18n.setLocaleForService(wsContext);
    return woBusiness.searchWoKpiCDBR(startTime, endTime);
  }

  @Override
  public ResultDTO createWoTKTU(WoDTO createWoDto) {
    I18n.setLocaleForService(wsContext);
    return woBusiness.createWoTKTU(createWoDto);
  }

  @Override
  public List<String> getSequenseWo(String seqName, int... size) {
    return woBusiness.getSequenseWo(seqName, size);
  }

  @Override
  public ResultDTO updatePendingWo(String woCode, String endPendingTime, String user,
      String comment, String system) {
    try {
      I18n.setLocaleForService(wsContext);
      return woBusiness.updatePendingWo(woCode, endPendingTime, user, comment, system, false);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public ResultDTO updateWoForSPM(WoDTO woDTO) {
    try {
      I18n.setLocaleForService(wsContext);
      return woBusiness.updateWoForSPM(woDTO);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public List<WoDTO> getListWoForAccount(List<String> lstAccount) {
    try {
      return woSPMBusiness.getListWoForAccount(lstAccount);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public List<CfgSupportForm> getListWoSupportInfo(String woCode) {
    try {
      return woTicketBusiness.getListWoSupportInfo(woCode);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public ResultDTO changeStatusWo(WoUpdateStatusForm updateForm) {
    I18n.setLocaleForService(wsContext);
    try {
      return woBusiness.changeStatusWo(updateForm);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("0", RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public ResultDTO createWoFollowNode(WoDTO createWoDto, List<String> listNode) {
    I18n.setLocaleForService(wsContext);
    return woBusiness.createWoFollowNode(createWoDto, listNode);
  }

  @Override
  public String getNationFromUnitId(Long unitId) {
    try {
      return woBusiness.getNationFromUnitId(unitId);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public ResultDTO callIPCC(String woId, String userCall) {
    I18n.setLocaleForService(wsContext);
    WoInsideDTO woInsideDTO = new WoInsideDTO();
    woInsideDTO.setWoId(Long.valueOf(woId));
    woInsideDTO.setUserCall(userCall);
    return woBusiness.callIPCC(woInsideDTO).toResultDTO();
  }

  @Override
  public ObjResponse getWoDetail(String woId, String userId) {
    I18n.setLocaleForService(wsContext);
    return woBusiness.getWoDetail(woId, userId);
  }

  @Override
  public List<CompCause> getListReasonOverdue(Long parentId, String nationCode) {
    return woBusiness.getListReasonOverdue(parentId, nationCode);
  }

  @Override
  public List<WoDTOSearch> getListWoByWoType(String woTypeCode, String createTimeFrom,
      String createTimeTo) {
    try {
      I18n.setLocaleForService(wsContext);
      return woSPMBusiness.getListWoByWoType(woTypeCode, createTimeFrom, createTimeTo);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public List<WoDTOSearch> getListWOByUsers(String userId, String summaryStatus, Integer isDetail,
      WoDTOSearch woDTO, int start, int count) {
    I18n.setLocaleForService(wsContext);
    return woBusiness.getListWOByUsers(userId, summaryStatus, isDetail, woDTO, start, count, 0);
  }

  @Override
  public ResultDTO pendingWo(String woCode, String endPendingTime, String user, String system,
      String reasonName, String reasonId, String customer, String phone) {
    try {
      I18n.setLocaleForService(wsContext);
      return woBusiness.pendingWo(woCode, endPendingTime, user, system, reasonName, reasonId,
          customer, phone);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public String checkProblemSignalForAccount(String subscriptionAccount, String workId,
      String sysId) {
    return woBusiness.checkProblemSignalForAccount(subscriptionAccount, workId, sysId);
  }

  @Override
  public ResultDTO updateFileForWo(WoDTO woDTO) {
    try {
      I18n.setLocaleForService(wsContext);
      return woBusiness.updateFileForWo(woDTO);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public ResultDTO approveWo(String username, String woId, String comment, String action,
      String ftName) {
    try {
      I18n.setLocaleForService(wsContext);
      return woBusiness.approveWo(null, username, woId, comment, action, ftName, "", "");
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO("NOK", e.getMessage());
    }
  }

  @Override
  public ResultDTO insertWoWorklog(WoWorklogDTO woWorklogDTO) {
    woWorklogDTO.setWoWorklogId(null);
    return woBusiness.insertWoWorklog(woWorklogDTO);
  }

  @Override
  public ResultDTO acceptWo(String username, String woId, String comment) {
    try {
      I18n.setLocaleForService(wsContext);
      return woBusiness.acceptWo(username, woId, comment);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("NOK", e.getMessage());
    }
  }

  @Override
  public ResultDTO createWo(WoDTO createWoDto) {
    try {
      I18n.setLocaleForService(wsContext);
      ResultDTO dto = woBusiness.createWo(createWoDto);
      return dto;
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public List<UsersDTO> getListCd(Long cdGroupId) {
    return woBusiness.getListCd(cdGroupId);
  }

  @Override
  public ResultDTO updateStatus(String username, String woId, String status, String comment) {
    try {
      I18n.setLocaleForService(wsContext);
      return woBusiness.updateStatus(username, woId, status, comment);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO("NOK", e.getMessage());
    }
  }

  @Override
  public ResultDTO updateDescriptionWoSPM(WoDTO woDTO) {
    try {
      return woSPMBusiness.updateDescriptionWoSPM(woDTO);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public ResultDTO completeWorkHelp(String woCode, String userName, String worklog,
      String reasonCcId) {
    try {
      I18n.setLocaleForService(wsContext);
      return woTicketBusiness.completeWorkHelp(woCode, userName, worklog, reasonCcId);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO("0", RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public List<ObjFile> getFileFromWo(String woId, List<String> lstFileName) {
    return woBusiness
        .getFileFromWo(
            (StringUtils.isNotNullOrEmpty(woId) && StringUtils.isLong(woId)) ? Long.valueOf(woId)
                : null, lstFileName);
  }

  @Override
  public List<ResultDTO> getWOSummaryInfobyUser(String userId) {
    return woBusiness.getWOSummaryInfobyUser(userId);
  }

  @Override
  public ResultDTO rejectWo(String username, String woId, String comment) {
    try {
      I18n.setLocaleForService(wsContext);
      return woBusiness
          .rejectWo(username, StringUtils.isLong(woId) ? Long.valueOf(woId) : null, comment, "CD")
          .toResultDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("NOK", e.getMessage());
    }
  }

  @Override
  public String updateWo(WoDTO woDTO) throws IOException, ParseException {
    return woBusiness.updateWo(woDTO);
  }

  @Override
  public ResultDTO closeWoForSPM(List<Wo> lstWo, String system, String user, Long reasonLevel3Id) {
    try {
      I18n.setLocaleForService(wsContext);
      return woSPMBusiness.closeWoForSPM(lstWo, system, user, reasonLevel3Id);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("0", RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public WoDTO findWoById(Long id) {
    if (id != null && id > 0) {
      return woBusiness.findWoById(id);
    }
    return null;
  }

  @Override
  public ResultDTO closeWo(List<String> listCode, String system) {
    I18n.setLocaleForService(wsContext);
    return woBusiness.closeWo(listCode, system);
  }

  @Override
  public ResultDTO deleteWOForRollback(String woCode, String reason, String system) {
    I18n.setLocaleForService(wsContext);
    return woBusiness.deleteWOForRollback(woCode, reason, system);
  }

  @Override
  public ResultDTO insertWoForSPM(WoDTO woDTO) {
    try {
      I18n.setLocaleForService(wsContext);
      ResultDTO dto = woBusiness.insertWoForSPM(woDTO);
      return dto;
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public String getConfigProperty() {
    return woBusiness.getConfigProperty();
  }

  @Override
  public ResultDTO callIPCCWithName(String woId, Long userId, String userCall,
      String fileAudioName) {
    I18n.setLocaleForService(wsContext);
    return woBusiness.callIPCCWithName(woId, userId, userCall, fileAudioName);
  }

  @Override
  public ResultDTO updateMopInfo(String woCode, String result, String mopId, Long type) {
    I18n.setLocaleForService(wsContext);
    return woBusiness.updateMopInfo(woCode, result, mopId, type);
  }

  @Override
  public ResultDTO createWoForOtherSystem(WoDTO createWoDto) {
    try {
      I18n.setLocaleForService(wsContext);
      return woBusiness.createWoForOtherSystem(createWoDto);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public List<AmiOneForm> getInfoAmiOne(List<String> lstAmiOneId) {
    try {
      return woTicketBusiness.getInfoAmiOne(lstAmiOneId);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public ResultDTO dispatchWo(String username, String ftName, String woId, String comment) {
    try {
      I18n.setLocaleForService(wsContext);
      ResultDTO resultDTO = new ResultDTO();
      ResultInSideDto res = woBusiness
          .dispatchWo(username, ftName, StringUtils.isLong(woId) ? Long.valueOf(woId) : null,
              comment);
      resultDTO.setId(RESULT.SUCCESS.equals(res.getKey()) ? WS_RESULT.OK : WS_RESULT.NOK);
      resultDTO.setQuantitySucc(res.getQuantitySucc());
      resultDTO.setMessage(res.getMessage());
      return resultDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("NOK", e.getMessage());
    }
  }

  @Override
  public ResultDTO updateResultTestFromSAP(WoDTO woDTO) {
    try {
      I18n.setLocaleForService(wsContext);
      return woBusiness.updateResultTestFromSAP(woDTO);
    } catch (Exception e) {
      return new ResultDTO("FAIL", "FAIL", e.getMessage());
    }
  }

}
