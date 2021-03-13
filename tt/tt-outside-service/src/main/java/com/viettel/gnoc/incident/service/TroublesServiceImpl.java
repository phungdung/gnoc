package com.viettel.gnoc.incident.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.ValidateAccount;
import com.viettel.gnoc.incident.business.TroublesBusiness;
import com.viettel.gnoc.incident.dto.ActionInfoDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TroublesServiceImpl implements TroublesService {

  @Autowired
  protected TroublesBusiness troublesBusiness;

  @Resource
  private WebServiceContext wsContext;


  public final static String REGEX_NUMBER = "([0-9])+";

  @Override
  public ResultDTO insertTroublesNOC(AuthorityDTO requestDTO, TroublesDTO troublesDTO,
      List<String> listAccount) {
    try {
      log.debug("Request to insertTroublesNOC : {}", troublesDTO);
      I18n.setLocaleForService(wsContext);
      return troublesBusiness.insertTroublesNOC(requestDTO, troublesDTO, listAccount);
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
  public ResultDTO updateTroublesNOC(AuthorityDTO requestDTO, List<TroublesDTO> listTrouble) {
    log.debug("Request to updateTroublesNOC : {}", listTrouble);
    I18n.setLocaleForService(wsContext);
    return troublesBusiness.updateTroublesNOC(requestDTO, listTrouble);
  }

  @Override
  public ResultDTO onSynchTrouble(AuthorityDTO requestDTO, String fromDate, String toDate,
      String insertSource,
      String subCategoryCode, String tableCurrent) {
    I18n.setLocaleForService(wsContext);
    return troublesBusiness.onSynchTrouble(requestDTO, fromDate, toDate, insertSource,
        subCategoryCode, tableCurrent);
  }

  @Override
  public List<TroublesDTO> onSearchForSPM(TroublesDTO troublesDTO, String account, String spmCode,
      Long typeSearch) {
    log.debug("Request to onSearchForSPM : {}", troublesDTO);
    return troublesBusiness.onSearchForSPM(troublesDTO, account, spmCode, typeSearch);
  }

  @Override
  public ResultDTO onUpdateTroubleSPM(AuthorityDTO requestDTO, TroublesDTO troublesDTO) {
    log.debug("Request to onUpdateTroubleSPM : {}", troublesDTO);
    I18n.setLocaleForService(wsContext);
    return troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);
  }

  @Override
  public List<ResultDTO> onRollBackTroubleSPM(AuthorityDTO requestDTO, List<String> lstSpmCode) {
    log.debug("Request to onRollBackTroubleSPM : {}", lstSpmCode);
    return troublesBusiness.onRollBackTroubleSPM(requestDTO, lstSpmCode);
  }

  @Override
  public ResultDTO onInsertTroubleMobile(AuthorityDTO requestDTO, TroublesDTO troublesDTO,
      List<String> listAccount,
      String[] arrFileName, byte[][] arrFileData) {
    I18n.setLocaleForService(wsContext);
    return troublesBusiness
        .onInsertTroubleMobile(requestDTO, troublesDTO, listAccount, arrFileName, arrFileData);
  }

  @Override
  public ResultDTO onInsertTroubleFileWS(AuthorityDTO requestDTO, TroublesDTO troublesDTO,
      List<String> listAccount, String[] arrFileName, byte[][] arrFileData) {
    log.debug("Request to onInsertTroubleFileWS : {}", troublesDTO);
    I18n.setLocaleForService(wsContext);
    return troublesBusiness
        .onInsertTroubleFileWS(requestDTO, troublesDTO, listAccount, arrFileName, arrFileData);
  }

  @Override
  public ResultDTO onUpdateTroubleMobile(TroublesDTO troublesDTO, WoDTO woDTO) {
    ResultDTO resultDTO = new ResultDTO();
    log.debug("Request to onUpdateTroubleMobile : {}", troublesDTO);
    I18n.setLocaleForService(wsContext);
    try {
      resultDTO = troublesBusiness.onUpdateTroubleMobile(troublesDTO);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setId(null);
      resultDTO.setKey(Constants.RESULT.FAIL);
      resultDTO.setMessage(ex.getMessage());
    }
    return resultDTO;
  }

  @Override
  public int onSearchCountForVsmart(TroublesDTO troublesDTO) {
    log.debug("Request to onSearchCountForVsmart : {}", troublesDTO);
    return troublesBusiness.onSearchCountForVsmart(troublesDTO);
  }

  @Override
  public List<TroublesDTO> onSearchForVsmart(TroublesDTO troublesDTO, Integer startRow,
      Integer pageLength) {
    log.debug("Request to onSearchForVsmart : {}", troublesDTO);
    return troublesBusiness.onSearchForVsmart(troublesDTO, startRow, pageLength);
  }

  @Override
  public List<TroublesDTO> countTroubleByStation(String stationCode, String startTime,
      String endTime, int type) {
    return troublesBusiness.countTroubleByStation(stationCode, startTime, endTime, null, type);
  }

  @Override
  public List<String> getListActionInfo(ActionInfoDTO actionInfoDTO) {
    return troublesBusiness.getListActionInfo(actionInfoDTO);
  }

  @Override
  public ResultDTO updateReasonTroubleFromNOC(AuthorityDTO requestDTO, TroublesDTO troublesDTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      ValidateAccount account = new ValidateAccount();
      String validate = account.checkAuthenticate(requestDTO);
      if (validate.equals(Constants.RESULT.SUCCESS)) {
        if (troublesDTO != null) {
          if (StringUtils.isStringNullOrEmpty(troublesDTO.getTroubleCode())) {
            resultDTO.setKey(Constants.RESULT.FAIL);
            resultDTO.setMessage("TroubleCode " + I18n.getLanguage("wo.close.trouble.isRequire"));
            return resultDTO;
          }

          if (StringUtils.isStringNullOrEmpty(troublesDTO.getProcessingUnitName())) {
            resultDTO.setKey(Constants.RESULT.FAIL);
            resultDTO
                .setMessage("ProcessingUnitName " + I18n.getLanguage("wo.close.trouble.isRequire"));
            return resultDTO;
          }
          if (StringUtils.isStringNullOrEmpty(troublesDTO.getProcessingUserName())) {
            resultDTO.setKey(Constants.RESULT.FAIL);
            resultDTO
                .setMessage("ProcessingUserName " + I18n.getLanguage("wo.close.trouble.isRequire"));
            return resultDTO;
          }

          if (StringUtils.isStringNullOrEmpty(troublesDTO.getReasonId())) {
            resultDTO.setKey(Constants.RESULT.FAIL);
            resultDTO.setMessage(
                "ReasonId " + I18n.getLanguage("wo.close.trouble.isRequire"));
            return resultDTO;
          }
          if (StringUtils.isStringNullOrEmpty(troublesDTO.getReasonName())) {
            resultDTO.setKey(Constants.RESULT.FAIL);
            resultDTO.setMessage("ReasonName " + I18n.getLanguage("wo.close.trouble.isRequire"));
            return resultDTO;
          }
          if (StringUtils.isStringNullOrEmpty(troublesDTO.getRootCause())) {
            resultDTO.setKey(Constants.RESULT.FAIL);
            resultDTO.setMessage(
                "RootCause " + I18n.getLanguage("wo.close.trouble.isRequire"));
            return resultDTO;
          }

          resultDTO = troublesBusiness.updateReasonTroubleFromNOC(troublesDTO);

        }
      } else {
        resultDTO.setKey(Constants.RESULT.FAIL);
        resultDTO.setKey(validate);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setKey(Constants.RESULT.FAIL);
      resultDTO.setKey(ex.getMessage());
    }
    return resultDTO;
  }

  @Override
  public ResultDTO getTroubleInfoForVSMART(TroublesDTO troublesDTO) {
    UsersDTO usersDTO = troublesBusiness.getUserByUserLogin(troublesDTO.getUserLogin());
    I18n.setLocaleForService(wsContext);
    ResultDTO resultDTO = validateTroubles(troublesDTO, usersDTO);
    troublesDTO.setReceiveUserId(usersDTO.getUserId());
    if (Constants.RESULT.SUCCESS.equals(resultDTO.getKey())) {
      List<TroublesDTO> lst = troublesBusiness.getTroubleInfoForVsSmart(troublesDTO);
      String json = "";
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        json = objectMapper.writeValueAsString(lst);
      } catch (JsonProcessingException e) {
        log.error(e.getMessage());
      }
      List<String> listJson = new ArrayList<>();
      listJson.add(json);
      if (!lst.isEmpty()) {
        resultDTO.setLstResult(listJson);
      } else {
        resultDTO.setMessage(I18n.getLanguage("tt.validate.null"));
      }
      return resultDTO;
    } else {
      resultDTO.setKey(Constants.RESULT.FAIL);
      resultDTO.setId(Constants.RESULT.FAIL);
    }
    return resultDTO;
  }

  @Override
  public ResultDTO updateTroubleFromVSMART(TroublesDTO troublesDTO) {
    I18n.setLocaleForService(wsContext);
    ResultDTO resultDTO = validateUpdateTrouble(troublesDTO);
    if (Constants.RESULT.SUCCESS.equals(resultDTO.getKey())) {
      List<TroublesDTO> list = resultDTO.getLstResult();
      TroublesDTO dto = list.get(0);
      troublesDTO.setTroubleCode(dto.getTroubleCode());
      troublesDTO.setCreateUserId(dto.getCreateUserId());
      troublesDTO.setCreateUserName(dto.getCreateUserName());
      troublesDTO.setCreateUnitId(dto.getCreateUnitId());
      troublesDTO.setCreateUnitName(dto.getCreateUnitName());
      troublesDTO.setCreatedTime(dto.getCreatedTime());
      troublesDTO.setLastUpdateTime(dto.getLastUpdateTime());
      troublesDTO.setWorkLog(dto.getWorkLog());
      ResultDTO update = troublesBusiness.updateTroubleFromVSMART(troublesDTO, true);
      resultDTO.setId(update.getId());
      resultDTO.setMessage(I18n.getLanguage("tt.update.success"));
    } else {
      resultDTO.setKey(Constants.RESULT.FAIL);
      resultDTO.setId(Constants.RESULT.FAIL);
    }
    resultDTO.setLstResult(null);
    return resultDTO;
  }

  private ResultDTO validateTroubles(TroublesDTO troublesDTO, UsersDTO usersDTO) {
    Date createTimeFrom = DateUtil
        .string2DateByPattern(troublesDTO.getCreatedTimeFrom(), "dd/MM/yyyy");
    Date createTimeTo = DateUtil.string2DateByPattern(troublesDTO.getCreatedTimeTo(), "dd/MM/yyyy");
    ResultDTO resultDTO = new ResultDTO();
    if (StringUtils.isStringNullOrEmpty(troublesDTO.getUserLogin())) {
      resultDTO.setMessage(I18n.getLanguage("tt.validate.user"));
      return resultDTO;
    }
    if (usersDTO.getUnitId() == null) {
      resultDTO.setMessage(I18n.getLanguage("tt.validate.userexist"));
      return resultDTO;
    }
    if (!StringUtils.isNotNullOrEmpty(troublesDTO.getCreatedTimeFrom())) {
      resultDTO.setMessage(I18n.getLanguage("tt.null.timefrom"));
      return resultDTO;
    } else if (createTimeFrom == null) {
      resultDTO.setMessage(I18n.getLanguage("tt.validate.timefrom"));
      return resultDTO;
    }
    if (!StringUtils.isNotNullOrEmpty(troublesDTO.getCreatedTimeTo())) {
      resultDTO.setMessage(I18n.getLanguage("tt.null.timeto"));
      return resultDTO;
    } else if (createTimeTo == null) {
      resultDTO.setMessage(I18n.getLanguage("tt.validate.timeto"));
      return resultDTO;
    }

    if (!StringUtils.isStringNullOrEmpty(troublesDTO.getStatus())) {
      List<String> lst = Arrays.asList(troublesDTO.getStatus().split(","));
      troublesDTO.setListState(lst);
      for (int i = 0; i < lst.size(); i++) {
        if (!lst.get(i).matches(REGEX_NUMBER)) {
          resultDTO.setMessage(I18n.getLanguage("tt.state.isnumble"));
          return resultDTO;
        }
      }
    } else {
      resultDTO.setMessage(I18n.getLanguage("tt.state.isnotnull"));
      return resultDTO;
    }
    if ((createTimeTo.getTime() - createTimeFrom.getTime()) / (1000 * 60 * 60 * 24) > 60) {
      resultDTO.setMessage(I18n.getLanguage("tt.validate.period"));
      return resultDTO;
    } else if ((createTimeTo.getTime() - createTimeFrom.getTime()) / (1000 * 60 * 60 * 24) < 0) {
      resultDTO.setMessage(I18n.getLanguage("tt.validate.time"));
      return resultDTO;
    }
    return new ResultDTO(Constants.RESULT.SUCCESS, Constants.RESULT.SUCCESS,
        Constants.RESULT.SUCCESS);
  }

  private ResultDTO validateUpdateTrouble(TroublesDTO troublesDTO) {
    ResultDTO resultDTO = new ResultDTO();
    if (StringUtils.isStringNullOrEmpty(troublesDTO.getTroubleId())) {
      resultDTO.setMessage(I18n.getLanguage("tt.trouble.notnull"));
      return resultDTO;
    }
    if (!troublesDTO.getTroubleId().matches(REGEX_NUMBER)) {
      resultDTO.setMessage(I18n.getLanguage("tt.trouble.isnumble"));
      return resultDTO;
    }
    TroublesDTO troublesDTOCheckID = troublesBusiness
        .findTroublesDtoById(Long.valueOf(troublesDTO.getTroubleId()));
    if (troublesDTOCheckID.getTroubleId() == null) {
      resultDTO.setMessage(I18n.getLanguage("tt.trouble.exist"));
      return resultDTO;
    }
    if (StringUtils.isNotNullOrEmpty(troublesDTO.getRejectedCode()) && StringUtils
        .isStringNullOrEmpty(troublesDTO.getRejectReason())) {
      resultDTO.setMessage(I18n.getLanguage("tt.reason.notnull"));
      return resultDTO;
    }
    if (StringUtils.isNotNullOrEmpty(troublesDTO.getRejectedCode()) && !troublesDTO
        .getRejectedCode().matches(REGEX_NUMBER)) {
      resultDTO.setMessage(I18n.getLanguage("tt.reason.isnumble"));
      return resultDTO;
    }
    if (StringUtils.isNotNullOrEmpty(troublesDTO.getRejectedCode()) && !troublesDTO
        .getRejectedCode().matches(REGEX_NUMBER)) {
      resultDTO.setMessage(I18n.getLanguage("tt.reason.isnumble"));
      return resultDTO;
    }
    if (StringUtils.isNotNullOrEmpty(troublesDTO.getSolutionType()) && !troublesDTO
        .getSolutionType().matches(REGEX_NUMBER)) {
      resultDTO.setMessage(I18n.getLanguage("tt.solutions.isnumble"));
      return resultDTO;
    }
    if (StringUtils.isNotNullOrEmpty(troublesDTO.getState()) && !troublesDTO.getState()
        .matches(REGEX_NUMBER)) {
      resultDTO.setMessage(I18n.getLanguage("tt.state.isnumble"));
      return resultDTO;
    }
    if (StringUtils.isNotNullOrEmpty(troublesDTO.getReasonId()) && !troublesDTO.getReasonId()
        .matches(REGEX_NUMBER)) {
      resultDTO.setMessage(I18n.getLanguage("tt.cause.isnumble"));
      return resultDTO;
    }
    if (StringUtils.isNotNullOrEmpty(troublesDTO.getWorkArround())
        && troublesDTO.getWorkArround().length() > 1000) {
      resultDTO
          .setMessage(I18n.getLanguage("tt.tt.workArround") + " " + I18n.getLanguage("tt.lenght"));
      return resultDTO;
    }
    if (troublesDTO.getRejectReason().length() > 1000) {
      resultDTO.setMessage(I18n.getLanguage("tt.reason") + " " + I18n.getLanguage("tt.lenght"));
      return resultDTO;
    }
    if (StringUtils.isNotNullOrEmpty(troublesDTO.getRootCause())
        && troublesDTO.getRootCause().length() > 1000) {
      resultDTO.setMessage(I18n.getLanguage("tt.rootCause") + " " + I18n.getLanguage("tt.lenght"));
      return resultDTO;
    }
    List<TroublesDTO> list = new ArrayList<>();
    list.add(troublesDTOCheckID);
    resultDTO.setKey(Constants.RESULT.SUCCESS);
    resultDTO.setId(Constants.RESULT.SUCCESS);
    resultDTO.setMessage(Constants.RESULT.SUCCESS);
    resultDTO.setLstResult(list);
    return resultDTO;
  }

  @Override
  public ResultDTO insertTroubleFromOtherSystem(TroublesInSideDTO troublesDTO,
      List<String> listAccount) throws Exception {
    return troublesBusiness.insertTroubleFromOtherSystem(troublesDTO, listAccount);
  }

  @Override
  public List<TroublesDTO> countTroubleByCable(String lineCutCode, String startTime, String endTime,
      int type) {
    return troublesBusiness.countTroubleByCable(lineCutCode, startTime, endTime, type);
  }

}
