package com.viettel.gnoc.incident.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.bccs.cc.service.CauseDTO;
import com.viettel.bccs.cc.service.CauseErrorExpireDTO;
import com.viettel.bccs.cc.service.CauseFrom;
import com.viettel.bccs.cc.service.ProblemGroupDTO;
import com.viettel.bccs.cc.service.ProblemTypeDTO;
import com.viettel.bccs.cc.service.SpmRespone;
import com.viettel.bccs.cc.service.TroubleNetworkSolutionDTO;
import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.CfgCreateWoBusiness;
import com.viettel.gnoc.commons.business.CfgInfoTtSpmBusiness;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.business.CompCauseBusiness;
import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatCfgClosedTicketDTO;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.CfgCreateWoDTO;
import com.viettel.gnoc.commons.dto.CfgInfoTtSpmDTO;
import com.viettel.gnoc.commons.dto.CfgRequireHaveWoDTO;
import com.viettel.gnoc.commons.dto.CfgTimeExtendTtDTO;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmDTO;
import com.viettel.gnoc.commons.dto.CompCauseDTO;
import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.MapFlowTemplatesDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TroubleStatisticForm;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.incident.provider.WSChatPort;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.OdServiceProxy;
import com.viettel.gnoc.commons.proxy.RiskServiceProxy;
import com.viettel.gnoc.commons.proxy.TtCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatCfgClosedTicketRepository;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CfgRequireHaveWoRepository;
import com.viettel.gnoc.commons.repository.CfgTimeExtendTtRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.SmsGatewayRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.INSERT_SOURCE;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.TROUBLE;
import com.viettel.gnoc.commons.utils.Constants.TT_STATE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.JsonUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.ValidateAccount;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrSearchDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.incident.dto.ActionInfoDTO;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.dto.CommonFileDTO;
import com.viettel.gnoc.incident.dto.ItAccountDTO;
import com.viettel.gnoc.incident.dto.ItemDataTT;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleMopDTO;
import com.viettel.gnoc.incident.dto.TroubleMopInsiteDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.dto.TroublesTempClosedDTO;
import com.viettel.gnoc.incident.model.CfgServerNocEntity;
import com.viettel.gnoc.incident.model.ItAccountEntity;
import com.viettel.gnoc.incident.model.ItSpmInfoEntity;
import com.viettel.gnoc.incident.model.TroubleActionLogsEntity;
import com.viettel.gnoc.incident.model.TroubleAssignEntity;
import com.viettel.gnoc.incident.model.TroubleFileEntity;
import com.viettel.gnoc.incident.model.TroubleNodeEntity;
import com.viettel.gnoc.incident.model.TroubleWorklogEntity;
import com.viettel.gnoc.incident.model.TroublesEntity;
import com.viettel.gnoc.incident.repository.CfgServerNocRepository;
import com.viettel.gnoc.incident.repository.ItAccountRepository;
import com.viettel.gnoc.incident.repository.ItSpmInfoRepository;
import com.viettel.gnoc.incident.repository.TroubleActionLogsRepository;
import com.viettel.gnoc.incident.repository.TroubleAssignRepository;
import com.viettel.gnoc.incident.repository.TroubleNodeRepository;
import com.viettel.gnoc.incident.repository.TroubleWorklogRepository;
import com.viettel.gnoc.incident.repository.TroublesRepository;
import com.viettel.gnoc.incident.utils.NocProWS;
import com.viettel.gnoc.incident.utils.TroubleBccsUtils;
import com.viettel.gnoc.incident.utils.TroubleSpmUtils;
import com.viettel.gnoc.incident.utils.TroubleTktuUtils;
import com.viettel.gnoc.incident.utils.WSBCCS2Port;
import com.viettel.gnoc.incident.utils.WSIPCC;
import com.viettel.gnoc.incident.utils.WSNocPro;
import com.viettel.gnoc.incident.utils.WSSAPPort;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.risk.dto.RiskDTO;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import com.viettel.gnoc.wo.dto.RequestApiWODTO;
import com.viettel.gnoc.wo.dto.Wo;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.ipcc.services.AutoCallOutInput;
import com.viettel.ipcc.services.NomalOutput;
import com.viettel.nms.nocpro.service.GnocActionBO;
import com.viettel.nms.nocpro.service.ResponseBO;
import com.viettel.security.PassTranformer;
import com.viettel.webservice.function.JsonResponseBO;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;
import vn.viettel.smartoffice.GroupBusiness;
import vn.viettel.smartoffice.GroupResponse;

@Service
@Transactional
@Slf4j
public class TroublesBusinessImpl implements TroublesBusiness {

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

  @Value("${application.ftp.folder}")
  private String ftpFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.extensionAllow:null}")
  private String extension;

  @Autowired
  TroublesRepository troublesRepository;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  UnitBusiness unitBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  CatLocationBusiness catLocationBusiness;

  @Autowired
  CfgInfoTtSpmBusiness cfgInfoTtSpmBusiness;

  @Autowired
  CfgCreateWoBusiness cfgCreateWoBusiness;

  @Autowired
  TroubleMopBusiness troubleMopBussiness;

  @Autowired
  TroubleWorklogRepository troubleWorklogRepository;

  @Autowired
  TroubleNodeRepository troubleNodeRepository;

  @Autowired
  TroubleActionLogsRepository troubleActionLogsRepository;

  @Autowired
  ItAccountRepository itAccountRepository;

  @Autowired
  ItSpmInfoRepository itSpmInfoRepository;

  @Autowired
  MessagesRepository messagesRepository;

  @Autowired
  TroubleAssignRepository troubleAssignRepository;

  @Autowired
  CfgTimeExtendTtRepository cfgTimeExtendTtRepository;

  @Autowired
  TtCategoryServiceProxy ttCategoryServiceProxy;

  @Autowired
  SmsGatewayRepository smsGatewayRepository;

  @Autowired
  CompCauseBusiness compCauseBusiness;

  @Autowired
  CfgServerNocRepository cfgServerNocRepository;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  CatReasonBusiness catReasonBusiness;

  @Autowired
  MessagesBusiness messagesBusiness;

  @Autowired
  CfgRequireHaveWoRepository cfgRequireHaveWoRepository;

  @Autowired
  CommonBusiness commonBusiness;

  @Autowired
  TroublesTempClosedBusiness troublesTempClosedBusiness;

  @Autowired
  TroublesServiceForCCBusiness troublesServiceForCCBusiness;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  WSChatPort wsChatPort;

  @Autowired
  WSBCCS2Port wsbccs2Port;

  @Autowired
  TroubleBccsUtils troubleBccsUtils;

  @Autowired
  TroubleSpmUtils troubleSpmUtils;

  @Autowired
  TroubleTktuUtils troubleTktuUtils;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  WOCreateBusiness woCreateBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  CatCfgClosedTicketRepository catCfgClosedTicketRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  WSSAPPort wssapPort;

  @Autowired
  UserRepository userRepository;

  @Autowired
  OdServiceProxy odServiceProxy;

  @Autowired
  RiskServiceProxy riskServiceProxy;

  @Override
  public List<TroublesInSideDTO> getTroubleInfo(TroublesInSideDTO dto) {
    log.debug("Request to getTroubleInfo: {}", dto);
    return troublesRepository.getTroubleInfo(dto);
  }

  @Override
  public ResultDTO updateTroublesNOC(AuthorityDTO requestDTO, List<TroublesDTO> listTrouble) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    try {
      ValidateAccount account = new ValidateAccount();
      String validateAcc = account.checkAuthenticate(requestDTO);
      if (validateAcc.equals(RESULT.SUCCESS)) {
        List<TroublesTempClosedDTO> lstTroublesTemp = new ArrayList<>();
        //Lay danh sach trouble va xu ly.
        if (null != listTrouble && !listTrouble.isEmpty()) {

          Map<String, CatReasonInSideDTO> mapReason = catReasonBusiness.getCatReasonData();
          Map<String, String> mapProperty = commonBusiness.getConfigProperty();
          for (TroublesDTO trouble : listTrouble) {
            //lay thong tin su co
            TroublesTempClosedDTO newTrouble = new TroublesTempClosedDTO();
            //Update lastUpdateTime
            String now = DateTimeUtils.getSysDateTime();
            newTrouble.setLastUpdateTime(DateTimeUtils.convertStringToDate(now));
            newTrouble.setCreatedTime(DateTimeUtils.convertStringToDate(now));
            newTrouble.setTroubleCode(trouble.getTroubleCode());
            String defaultKeDBAutoClose = mapProperty.get("TT.DEFAULT.KEDB.AUTOCLOSE");
            String defaultAutoCloseCode = mapProperty.get("TT.DEFAULT.CLOSECODE.AUTOCLOSE");
            Date endTroubleTimeTmp =
                StringUtils.validString(trouble.getEndTroubleTime()) ? DateTimeUtils
                    .convertStringToDate(trouble.getEndTroubleTime()) : null;
            newTrouble
                .setAssignTimeTemp(endTroubleTimeTmp); //Lay bang thoi gian giao clear canh bao.
            newTrouble.setState(11L); //11.Close
            newTrouble.setRelatedKedb(defaultKeDBAutoClose);
            newTrouble.setCloseCode(defaultAutoCloseCode);
            //Bo sung dong ticket thi cap nhat them closetime
            newTrouble.setClosedTime(endTroubleTimeTmp);
            newTrouble.setClearTime(endTroubleTimeTmp);
            newTrouble
                .setEndTroubleTime(endTroubleTimeTmp); //Cap nhat thoi gian clear canh bao
            newTrouble.setReasonId(
                StringUtils.validString(trouble.getReasonId()) ? Long.valueOf(trouble.getReasonId())
                    : null); //Id cua nguyen nhan
            if (trouble.getReasonId() != null) {
              CatReasonInSideDTO catReasonInSideDTO = mapReason.get(trouble.getReasonId());
              newTrouble.setReasonName(
                  catReasonInSideDTO == null ? ""
                      : catReasonInSideDTO.getReasonName()); //Ten cua nguyen nhan
            }
            newTrouble.setRootCause(trouble.getRootCause()); //Mo ta chi tiet nguyen nhan
            newTrouble.setSolutionType(StringUtils.validString(trouble.getSolutionType()) ? Long
                .valueOf(trouble.getSolutionType()) : null); //Id giai phap
            newTrouble.setWorkArround(trouble.getWorkArround()); //Noi dung giai phap
            lstTroublesTemp.add(newTrouble);

          }
        }

        //insert bang tam
        resultInSideDTO = troublesTempClosedBusiness.insertList(lstTroublesTemp);
        if (!RESULT.SUCCESS.equals(resultInSideDTO.getKey())) {
          resultInSideDTO.setKey(RESULT.FAIL);
          resultInSideDTO.setMessage("Faile to insert incident" + resultInSideDTO.getKey());
          return resultInSideDTO.toResultDTO();
        }

      } else {
        resultInSideDTO.setKey(RESULT.FAIL);
        resultInSideDTO.setMessage(validateAcc);
      }

      resultInSideDTO.setKey(RESULT.SUCCESS);
      resultInSideDTO.setMessage(RESULT.SUCCESS);

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDTO.setId(null);
      resultInSideDTO.setKey(RESULT.FAIL);
      resultInSideDTO.setMessage(ex.toString());
    }
    return resultInSideDTO.toResultDTO();
  }

  @Override
  public ResultDTO onSynchTrouble(AuthorityDTO requestDTO, String fromDate, String toDate,
      String insertSource, String subCategoryCode, String tableCurrent) {
    ResultDTO result = new ResultDTO();
    try {
      String startTime = DateTimeUtils.getSysDateTime();
      ValidateAccount account = new ValidateAccount();
      String validate = account.checkAuthenticate(requestDTO);

      if (validate.equals(RESULT.SUCCESS)) {

        validate = DateTimeUtils.validateInput(fromDate, toDate);
        if (validate.equals(RESULT.SUCCESS)) {
          List<TroublesDTO> lst = troublesRepository.onSynchTrouble(fromDate, toDate,
              insertSource, subCategoryCode, tableCurrent);
          result.setLstResult(lst);
        }
      }
      String endTime = DateTimeUtils.getSysDateTime();

      if (validate.equals(RESULT.SUCCESS)) {
        result.setKey(RESULT.SUCCESS);
      } else {
        result.setKey(RESULT.FAIL);
      }
      result.setRequestTime(startTime);
      result.setFinishTime(endTime);
      result.setMessage(validate);

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return result;
  }

  @Override
  public List<TroublesDTO> onSearchForSPM(TroublesDTO troublesDTO, String account, String spmCode,
      Long typeSearch) {
    log.debug("Request to onSearchForSPM: {}", troublesDTO);
    return troublesRepository.onSearchForSPM(troublesDTO, account, spmCode, typeSearch);
  }

  @Override
  public ResultDTO onUpdateTroubleSPM(AuthorityDTO requestDTO, TroublesDTO troublesDTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      ValidateAccount account = new ValidateAccount();
      String validate = account.checkAuthenticate(requestDTO);
      if (validate.equals(RESULT.SUCCESS)) {
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getSpmCode())) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("SpmCode " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return resultDTO;
        }
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getProcessingUnitName())) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO
              .setMessage("ProcessingUnitName " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return resultDTO;
        }
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getProcessingUserName())) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO
              .setMessage("ProcessingUserName " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return resultDTO;
        }

        if (StringUtils.isStringNullOrEmpty(troublesDTO.getStateName())) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("StateName " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return resultDTO;
        }
        if ("UPDATE_WORKLOG".equals(troublesDTO.getStateName()) || "UPDATE_OPEN_DEFERRED"
            .equals(troublesDTO.getStateName())
            || "UPDATE_DEFERRED".equals(troublesDTO.getStateName())) {
          if (StringUtils.isStringNullOrEmpty(troublesDTO.getWorkLog())) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage("WorkLog " + I18n.getLanguage("wo.close.trouble.isRequire"));
            return resultDTO;
          }
        }
        if ("UPDATE_CLOSED".equals(troublesDTO.getStateName())) {
          if (StringUtils.isStringNullOrEmpty(troublesDTO.getReasonLv3Id())) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage("ReasonLv3Id " + I18n.getLanguage("wo.close.trouble.isRequire"));
            return resultDTO;
          }
        }
        if ("UPDATE_DEFERRED".equals(troublesDTO.getStateName())) {
          if (StringUtils.isStringNullOrEmpty(troublesDTO.getDeferredTime())) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage("DeferredTime " + I18n.getLanguage("wo.close.trouble.isRequire"));
            return resultDTO;
          }
          try {
            Date defferTime = DateTimeUtils.convertStringToDateTime(troublesDTO.getDeferredTime());
            if (defferTime.compareTo(new Date()) < 0) {
              resultDTO.setKey(RESULT.FAIL);
              resultDTO.setMessage(I18n.getLanguage("incident.deferredTimeMustBeBiggerThanNow"));
              return resultDTO;
            }
          } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            resultDTO.setKey(RESULT.FAIL);
            resultDTO
                .setMessage("DeferredTime " + I18n.getLanguage("wo.close.trouble.date.format"));
            return resultDTO;
          }
        }
        resultDTO = onUpdateTroubleDtoSPM(troublesDTO);

      } else {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setKey(validate);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setKey(ex.getMessage());
    }
    return resultDTO;
  }

  public ResultDTO onUpdateTroubleDtoSPM(TroublesDTO tForm) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    try {
      TroublesInSideDTO troubleDTO = troublesRepository
          .getTroubleDTO(null, null, tForm.getSpmCode(), null, null, null, null);
      if (troubleDTO == null) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(I18n.getLanguage("incident.invalid.ticket"));
        return resultDTO;
      }
      TroublesEntity trouble = troublesRepository.findTroublesById(troubleDTO.getTroubleId());
      if (!StringUtils.isStringNullOrEmpty(tForm.getPriorityId())) {
        CatItemDTO catItemDTO = new CatItemDTO();
        catItemDTO.setItemCode(tForm.getPriorityId());
        List<CatItemDTO> lstCatItem = catItemBusiness.getListCatItemDTO(catItemDTO);
        if (lstCatItem == null || lstCatItem.isEmpty()
            || (lstCatItem != null && !lstCatItem.get(0).getCategoryCode()
            .equals(Constants.TROUBLE.PRIORITY))) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage(I18n.getLanguage("incident.priorityNotExist"));
          return resultDTO;
        } else {
          tForm.setPriorityId(lstCatItem.get(0).getItemId().toString());
          tForm.setPriorityCode(lstCatItem.get(0).getItemCode());
        }
      }
      TroubleActionLogsDTO troubleActionLogsDTO = new TroubleActionLogsDTO(null,
          troubleDTO.compareContent(tForm.toModelInSide().toEntity()), new Date(),
          troubleDTO.getCreateUnitId(), troubleDTO.getCreateUserId(),
          I18n.getLanguage("common.btn.update"), troubleDTO.getTroubleId(),
          tForm.getProcessingUnitName(), tForm.getProcessingUserName(),
          troubleDTO.getState(),
          troubleDTO.getTroubleCode(), troubleDTO.getStateName(), null, null);
      if ("SPM_VTNET".equals(trouble.getInsertSource())
          || "SPM".equals(trouble.getInsertSource())) {
        troubleActionLogsDTO.setInsertSource(trouble.getInsertSource());
        tForm.setTroubleCode(trouble.getTroubleCode().toString());

      }

      //ThanhLv12 thuc hien bo sung log info cho cac WO tao tu SOC_VTNET_start
      if ("SPM_VTNET".equals(trouble.getInsertSource())
          || "SPM".equals(trouble.getInsertSource())) {
        troubleActionLogsDTO.setInsertSource(trouble.getInsertSource());
        tForm.setTroubleCode(trouble.getTroubleCode().toString());

      }
      //ThanhLv12 thuc hien bo sung log info cho cac WO tao tu SOC_VTNET_end

      if ("UPDATE_HOT".equals(tForm.getStateName())) { //tich hot
        //xu ly them neu insertSoure = SPM_VTNET
        if (Constants.INSERT_SOURCE.SPM_VTNET.equals(troubleDTO.getInsertSource())
            && Constants.INSERT_SOURCE.SPM_VTNET.equals(tForm.getInsertSource())) {
          trouble.setInfoTicket(tForm.getInfoTicket());
          if (!StringUtils.isStringNullOrEmpty(tForm.getAlarmGroupCode())) {
            CatItemDTO alarmGroupDTO = new CatItemDTO();
            alarmGroupDTO.setItemCode(tForm.getAlarmGroupCode());
            alarmGroupDTO.setParentItemId(troubleDTO.getTypeId());
            List<CatItemDTO> lstAlarmGroup = catItemBusiness.getListCatItemDTO(alarmGroupDTO);
            if (lstAlarmGroup != null && !lstAlarmGroup.isEmpty()) {
              trouble.setAlarmGroup(lstAlarmGroup.get(0).getItemId().toString());

            } else {
              resultDTO.setKey(RESULT.FAIL);
              resultDTO.setMessage("incident.alarmGroupCodeNotExist");
              return resultDTO;
            }
          } else {// 20200121_HaNV15_Add_Start: alarm group require
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage("AlarmGroup " + I18n.getLanguage("wo.close.trouble.isRequire"));
            return resultDTO;
          }// 20200121_HaNV15_Add_End: alarm group require

          if (tForm.getPriorityId() == null || tForm.getPriorityId().length() == 0) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage("Priority " + I18n.getLanguage("wo.close.trouble.isRequire"));
            return resultDTO;
          }

          CatItemDTO prio = new CatItemDTO();
          prio.setItemCode(tForm.getPriorityCode().trim().toLowerCase());
          List<CatItemDTO> lstCat = catItemBusiness.getListCatItemDTO(prio);
          if (lstCat == null || lstCat.isEmpty()
              || (lstCat != null && !lstCat.get(0).getCategoryCode()
              .equals(Constants.TROUBLE.PRIORITY))) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage(I18n.getLanguage("incident.priorityNotExist"));
            return resultDTO;

          } else {
            trouble.setPriorityId(lstCat.get(0).getItemId());
          }
          CfgTimeTroubleProcessDTO config = troublesRepository
              .getConfigTimeTroubleProcess(trouble.getTypeId(),
                  Long.parseLong(trouble.getAlarmGroup()), trouble.getPriorityId(), null);
          if (config != null) {
            tForm.setTimeProcess(config.getProcessTtTime().toString());
          } else { // 20200122_HaNV15_Add_Start: Validate config time process
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage(I18n.getLanguage("not.config.time.process"));
            return resultDTO;
          }
          // 20200122_HaNV15_Add_End: Validate config time process
        }
        try {

          if (!StringUtils.isStringNullOrEmpty(tForm.getTimeProcess()) && !StringUtils
              .isStringNullOrEmpty(troubleDTO.getRemainTime())
              && Double.parseDouble(tForm.getTimeProcess()) < Double
              .parseDouble(troubleDTO.getRemainTime())) {
            trouble.setAssignTime(new Date());
            trouble.setAssignTimeTemp(new Date());
            trouble.setTimeProcess(Double.parseDouble(tForm.getTimeProcess()));
            trouble.setTimeUsed(null);
          }

        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }

        //goi WO
        if (!StringUtils.isStringNullOrEmpty(troubleDTO.getWoCode())) {
          WoDTO wdto = new WoDTO();
          wdto.setIsHot("1");
          wdto.setWoCode(troubleDTO.getWoCode());
          wdto.setEstimateTime(tForm.getTimeProcess());
          ResultDTO rdto = woServiceProxy.updateWoInfoProxy(wdto);
          if (rdto != null && rdto.getKey().equals(RESULT.SUCCESS)) {
          } else if (rdto != null) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage("WO: " + rdto.getMessage());
            return resultDTO;
          } else {
            resultDTO.setKey(RESULT.FAIL);
            return resultDTO;
          }
        }

        // 20200121_HaNV15_Add_Start: Add condition check insert source not equal SPM_VTNET
        if (!Constants.INSERT_SOURCE.SPM_VTNET.equals(troubleDTO.getInsertSource())
            && !Constants.INSERT_SOURCE.SPM_VTNET.equals(tForm.getInsertSource())) {
          CatItemDTO prio = new CatItemDTO();
          prio.setCategoryCode("PRIRORITY_TT_CC");
          prio.setItemCode("HOT");
          List<CatItemDTO> lstCatItemDTO = catItemBusiness.getListCatItemDTO(prio);
          if (lstCatItemDTO != null && !lstCatItemDTO.isEmpty()) {
            prio = new CatItemDTO();
            prio.setItemCode(lstCatItemDTO.get(0).getItemValue().toLowerCase());
            List<CatItemDTO> lstCat = catItemBusiness.getListCatItemDTO(prio);
            if (lstCat != null && !lstCat.isEmpty()) {
              trouble.setPriorityId(lstCat.get(0).getItemId());
            }
          } else {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage(I18n.getLanguage("priority.config.gnoc.spm"));
            return resultDTO;
          }
        }
        troubleActionLogsDTO.setContent(
            I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS
                + " " + I18n.getLanguage("tich.hot.from.other") + " " + trouble.getInsertSource());
        troubleActionLogsDTO.setInsertSource(trouble.getInsertSource());
        tForm.setWorkLog(troubleActionLogsDTO.getContent());
        tForm.setState(trouble.getState().toString());
        ActionInfoDTO info = new ActionInfoDTO(tForm);
        troubleActionLogsDTO.setActionInfo(info.toString());
        troublesRepository.updateTroubles(trouble);
        troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO.toEntity());

      } else if ("UPDATE_WORKLOG".equals(tForm.getStateName())) { //cap nhat ghi chu

        //goi WO
        if (!StringUtils.isStringNullOrEmpty(troubleDTO.getWoCode())) {
          WoDTO wdto = new WoDTO();
          wdto.setWoDescription(tForm.getWorkLog().trim());
          wdto.setWoCode(troubleDTO.getWoCode());
          wdto.setCustomerTimeDesireFrom(tForm.getCustomerTimeDesireFrom());
          wdto.setCustomerTimeDesireTo(tForm.getCustomerTimeDesireTo());
          wdto.setEndTime(tForm.getEndTroubleTime());
          ResultDTO rdto = woServiceProxy.updateWoInfoProxy(wdto);
          if (rdto != null && rdto.getKey().equals(RESULT.SUCCESS)) {
          } else if (rdto != null) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage("WO: " + rdto.getMessage());
            return resultDTO;
          } else {
            resultDTO.setKey(RESULT.FAIL);
            return resultDTO;
          }
        }
        troubleDTO.setWorkLog(
            (troubleDTO.getWorkLog() == null ? "" : (troubleDTO.getWorkLog() + " \n "))
                + DateTimeUtils
                .getSysDateTime() + " " + tForm.getWorkLog().trim());

        troubleDTO.setWorkLog(tForm.getWorkLog().trim());
        troubleDTO.setCreateUnitName(tForm.getProcessingUnitName());
        troubleDTO.setCreateUserName(tForm.getProcessingUserName());
        troubleDTO.setLastUpdateTime(new Date());
        if (!DataUtil.isNullOrEmpty(troubleDTO.getWorkLog())) {
          TroubleWorklogInsiteDTO troubleWorklogDTO = setTroubleWorklog(troubleDTO);
          troubleWorklogRepository.insertTroubleWorklog(troubleWorklogDTO.toEntity());
        }
        troubleActionLogsDTO.setContent(
            I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS
                + " "
                + I18n.getLanguage("spm.update.worklog"));
        troublesRepository.updateTroubles(troubleDTO.toEntity());
        troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO.toEntity());

      } else if ("UPDATE_NUM_COMPLAINT".equals(tForm.getStateName())) {
        //goi WO
        if (!StringUtils.isStringNullOrEmpty(troubleDTO.getWoCode())) {
          WoDTO wdto = new WoDTO();
          wdto.setNumComplaint("1");
          wdto.setWoCode(troubleDTO.getWoCode());
          ResultDTO rdto = woServiceProxy.updateWoInfoProxy(wdto);
          if (rdto != null && rdto.getKey().equals(RESULT.SUCCESS)) {
            resultDTO.setMessage(RESULT.SUCCESS);
            resultDTO.setKey(RESULT.SUCCESS);
            return resultDTO;
          } else if (rdto != null) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage("WO: " + rdto.getMessage());
            return resultDTO;
          } else {
            resultDTO.setKey(RESULT.FAIL);
            return resultDTO;
          }
        }

      } else if ("UPDATE_OPEN_DEFERRED".equals(tForm.getStateName()) || "UPDATE_DEFERRED"
          .equals(tForm.getStateName())) {
        if (!DataUtil.isNullOrEmpty(troubleDTO.getWoCode())) {
          ResultDTO rdto = new ResultDTO();
          if ("UPDATE_OPEN_DEFERRED".equals(tForm.getStateName())) {
            tForm.setDeferredTime(null);
            rdto = woServiceProxy
                .updatePendingWo(
                    new RequestApiWODTO(troubleDTO.getWoCode(), tForm.getDeferredTime(),
                        tForm.getProcessingUserName(), tForm.getWorkLog().trim(), "SPM", false));
          } else if ("UPDATE_DEFERRED".equals(tForm.getStateName())) {
            rdto = woServiceProxy
                .pendingWo(new RequestApiWODTO(troubleDTO.getWoCode(), tForm.getDeferredTime(),
                    tForm.getProcessingUserName(), "SPM", tForm.getWorkLog().trim(), "", "", ""));
          }

          if (rdto != null && rdto.getKey().equals(RESULT.SUCCESS)) {
          } else if (rdto != null) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage("WO: " + rdto.getMessage());
            return resultDTO;
          } else {
            resultDTO.setKey(RESULT.FAIL);
            return resultDTO;
          }
        }
        //cap nhat WO log
        troubleDTO.setWorkLog(
            (troubleDTO.getWorkLog() == null ? "" : (troubleDTO.getWorkLog() + " \n ")) + tForm
                .getWorkLog().trim());

        troubleDTO.setWorkLog(tForm.getWorkLog().trim());
        troubleDTO.setCreateUnitName(tForm.getProcessingUnitName());
        troubleDTO.setCreateUserName(tForm.getProcessingUserName());
        troubleDTO.setLastUpdateTime(new Date());
        if (!DataUtil.isNullOrEmpty(troubleDTO.getWorkLog())) {
          TroubleWorklogInsiteDTO troubleWorklogDTO = setTroubleWorklog(troubleDTO);
          troubleWorklogRepository.insertTroubleWorklog(troubleWorklogDTO.toEntity());
        }
        if ("UPDATE_OPEN_DEFERRED".equals(tForm.getStateName())) { //mo tam dong
          troubleDTO.setState(5L);
          troubleDTO.setAssignTimeTemp(new Date());

          troubleActionLogsDTO.setStateId(5L);
          CatItemDTO stateItem = catItemBusiness
              .getCatItemById(troubleDTO.getState());
          troubleActionLogsDTO.setStateName(stateItem.getItemName());
          troubleActionLogsDTO.setContent(
              I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS
                  + " " + I18n.getLanguage("spm.update.open"));
        } else if ("UPDATE_DEFERRED".equals(tForm.getStateName())) { //tam dong
          //cap nhat lai thoi gian
          troubleDTO.setDeferredTime(
              StringUtils.validString(tForm.getDeferredTime()) ? DateTimeUtils
                  .convertStringToDate(tForm.getDeferredTime())
                  : null);
          troubleActionLogsDTO.setStateId(7L);
          CatItemDTO stateItem = catItemBusiness
              .getCatItemById(troubleDTO.getState());
          troubleActionLogsDTO.setStateName(stateItem.getItemName());
          troubleActionLogsDTO.setContent(
              I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS
                  + " " + I18n.getLanguage("spm.update.delay") + " " + tForm.getDeferredTime());
        }
        troublesRepository.updateTroubles(troubleDTO.toEntity());
        troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO.toEntity());

      } else if ("UPDATE_CLOSED".equals(tForm.getStateName())) {
        if (troubleDTO.getState() != null && !(11L == (troubleDTO.getState()))) {
          if (troubleDTO.getWoCode() != null) {
            List<Wo> lstWo = new ArrayList<>();
            Wo wo = new Wo();
            wo.setWoCode(troubleDTO.getWoCode());
            wo.setWoSystemId(troubleDTO.getTroubleCode());
            lstWo.add(wo);
            resultDTO = woServiceProxy
                .closeWoForSPM(new RequestApiWODTO(lstWo, "SPM", tForm.getProcessingUserName(),
                    Long.parseLong(tForm.getReasonLv3Id())));
            if (!"SUCCESS".equals(resultDTO.getKey())) {
              resultDTO.setKey(RESULT.FAIL);
              resultDTO.setMessage("WO: " + resultDTO.getMessage());
              return resultDTO;
            }
          }
          Date timeNewDate = new Date();
          troubleDTO.setState(11L);
          troubleDTO.setEndTroubleTime(
              troubleDTO.getEndTroubleTime() == null ? timeNewDate
                  : troubleDTO.getEndTroubleTime());
          troubleDTO.setClearTime(
              troubleDTO.getClearTime() == null ? timeNewDate : troubleDTO.getClearTime());
          troubleDTO.setClosedTime(
              troubleDTO.getClosedTime() == null ? timeNewDate : troubleDTO.getClosedTime());
          troubleDTO.setLastUpdateTime(timeNewDate);
          troubleActionLogsDTO.setStateId(11L);
          CatItemDTO stateItem = catItemBusiness
              .getCatItemById(troubleDTO.getState());
          troubleActionLogsDTO.setStateName(stateItem.getItemName());
          troubleActionLogsDTO.setContent(
              I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS
                  + " " + I18n.getLanguage("update.close.fromOther") + " " + trouble
                  .getInsertSource());
          String temp = "###" + troubleDTO.getTroubleCode() + ";0;" + troubleDTO.getTimeUsed();

          tForm.setWorkLog(troubleActionLogsDTO.getContent());
          tForm.setState(troubleDTO.getState().toString());
          ActionInfoDTO info = new ActionInfoDTO(tForm);
          troubleActionLogsDTO.setActionInfo(info.toString());
          troubleActionLogsDTO.setInsertSource(trouble.getInsertSource());
          if (troubleDTO.getRemainTime() != null
              && Double.parseDouble(troubleDTO.getRemainTime()) < 0) {
            temp = temp + ";0" + "###";
          } else {
            temp = temp + ";1" + "###";
          }
          resultDTO.setId(temp);

          troublesRepository.updateTroubles(troubleDTO.toEntity());
          troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO.toEntity());
        }
      } else {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(I18n.getLanguage("spm.update.not.exist"));
        return resultDTO;
      }
      resultDTO.setKey(RESULT.SUCCESS);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(ex.toString());
      return resultDTO;
    }
    return resultDTO;
  }

  private TroubleWorklogInsiteDTO setTroubleWorklog(TroublesInSideDTO tForm) {
    TroubleWorklogInsiteDTO troubleLog = new TroubleWorklogInsiteDTO();
    troubleLog.setTroubleId(tForm.getTroubleId());
    troubleLog.setCreateUnitId(tForm.getCreateUnitId());
    troubleLog.setCreateUnitName(tForm.getCreateUnitName());
    troubleLog.setCreateUserId(tForm.getCreateUserId() == null ? null : tForm.getCreateUserId());
    troubleLog
        .setCreateUserName(tForm.getCreateUserName() == null ? "" : tForm.getCreateUserName());
    troubleLog.setWorklog(tForm.getWorkLog());
    troubleLog.setCreatedTime(tForm.getLastUpdateTime());
    return troubleLog;
  }

  @Override
  public List<ResultDTO> onRollBackTroubleSPM(AuthorityDTO requestDTO, List<String> lstSpmCode) {
    List<ResultDTO> lstResult = new ArrayList<>();
    try {
      ValidateAccount account = new ValidateAccount();
      String validate = account.checkAuthenticate(requestDTO);
      if (validate.equals(RESULT.SUCCESS)) {
        if (lstSpmCode != null && !lstSpmCode.isEmpty()) {
          ResultDTO resultDTO = new ResultDTO();
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("SpmCode " + I18n.getLanguage("wo.close.trouble.isRequire"));
          lstResult.add(resultDTO);
        }
      }
      return onRollBackTroubleDtoSPM(lstSpmCode);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      ResultDTO resultDTO1 = new ResultDTO();
      resultDTO1.setKey(RESULT.FAIL);
      resultDTO1.setKey(ex.getMessage());
      lstResult.add(resultDTO1);
    }
    return lstResult;
  }


  public List<ResultDTO> onRollBackTroubleDtoSPM(List<String> lstSpmCode) throws Exception {
    List<ResultDTO> lstResult = new ArrayList<>();
    String spmCodeCurr = "";
    try {
      if (lstSpmCode != null && !lstSpmCode.isEmpty()) {
        for (String spmCode : lstSpmCode) {
          spmCodeCurr = spmCode;
          ResultDTO resultDTO = new ResultDTO();
          TroublesInSideDTO troubleDTO = troublesRepository
              .getTroubleDTO(null, null, spmCode, null, null, null, null);
          if (troubleDTO != null) {
            resultDTO = deleteDataRollBack(troubleDTO.getTroubleId(),
                troubleDTO.getWoCode());
          } else {
            resultDTO.setMessage(I18n.getLanguage("incident.invalid.ticket"));
            resultDTO.setKey(RESULT.SUCCESS);
          }
          resultDTO.setId(spmCode);
          lstResult.add(resultDTO);
        }
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      ResultDTO resultDTO1 = new ResultDTO();
      resultDTO1.setKey(RESULT.FAIL);
      resultDTO1.setMessage(ex.toString());
      resultDTO1.setId(spmCodeCurr);
      lstResult.add(resultDTO1);
    }
    return lstResult;
  }

  public ResultDTO deleteDataRollBack(Long troubleId, String woCode) {
    ResultDTO resultDTO = new ResultDTO();
    troublesRepository.delete(troubleId);
    troubleActionLogsRepository.deleteTroubleActionLogsByTroubleId(troubleId);
    itSpmInfoRepository.deleteItSpmInfoByIncidentId(troubleId);
    if (!DataUtil.isNullOrEmpty(woCode)) {
      resultDTO = woServiceProxy.deleteWOForRollbackProxy(woCode, "rollback SPM", "SPM");
    }
    return resultDTO;
  }

  @Override
  public List<TroublesDTO> onSearchForVsmart(TroublesDTO dto, Integer startRow,
      Integer pageLength) {
    log.debug("Request to onSearchForVsmart: {}", dto);
    List<TroublesDTO> data = troublesRepository.onSearchForVsmart(dto, startRow, pageLength);
    return data;
  }

  @Override
  public int onSearchCountForVsmart(TroublesDTO troublesDTO) {
    log.debug("Request to onSearchForVsmart: {}", troublesDTO);
    return troublesRepository.onSearchCountForVsmart(troublesDTO);
  }

  @Override
  public ResultDTO onInsertTroubleMobile(AuthorityDTO requestDTO, TroublesDTO troublesDTO,
      List<String> listAccount,
      String[] arrFileName, byte[][] arrFileData) {

    ResultDTO resultDTO = new ResultDTO();
    String ptCode = "";
    try {
      //namtn convert fileName va fileData start
      List<byte[]> lstFileDocumentByteArray = new ArrayList<>();
      List<String> lstArrFileName = new ArrayList<>();
      if (arrFileData != null && arrFileData.length > 0) {
        for (int i = 0; i < arrFileData.length; i++) {
          byte[] fileData = arrFileData[i];
          String fileName = arrFileName[i];
          lstArrFileName.add(fileName);
          lstFileDocumentByteArray.add(fileData);
        }
      }
      //namtn convert fileName va fileData end

      String ret = "";
      ValidateAccount account = new ValidateAccount();
      String validateAcc = account.checkAuthenticate(requestDTO);
      if (validateAcc.equals(RESULT.SUCCESS)) {
        if (DataUtil.isNullOrEmpty(troublesDTO.getInsertSource())) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("trouble.insertSourceNull");
          return resultDTO;
        }
//        ret = validateDTO(troublesDTO, null);
        //
        if (troublesDTO.getInsertSource().contains("SPM")) {
          if (listAccount == null || listAccount.isEmpty()) {
            ret = "incident.listAccountIsNotNull";
          } else {
            troublesDTO.setSpmName(listAccount.get(0));
            if (troublesDTO.getListAccount() == null || troublesDTO.getListAccount().isEmpty()) {
              troublesDTO.setListAccount(new ArrayList<>());
            }
            troublesDTO.getListAccount().addAll(listAccount);
          }
        }
        ret = StringUtils.isStringNullOrEmpty(ret) ? validateDTO(troublesDTO, null) : ret;
        if ("".equals(ret)) {
          if (null == troublesDTO.getTroubleCode()) { //Trong truong hop tao moi
            String id = getSequenseTroubles("TROUBLES_SEQ", 1).get(0);
            troublesDTO.setTroubleId(id);
            String itemCode = troublesDTO.getTypeName();
            ptCode = "GNOC_TT_" + itemCode + "_" + DateTimeUtils
                .convertDateToString(new Date(), "yyMMdd") + "_" + id;
            troublesDTO.setTroubleCode(ptCode);

            //namtn set arrFileName and arrFileData for TT start
            troublesDTO.setArrFileName(lstArrFileName);
            troublesDTO.setFileDocumentByteArray(lstFileDocumentByteArray);
            //namtn set arrFileName and arrFileData for TT end

            resultDTO = onInsertTroubleMobileNew(troublesDTO, listAccount, arrFileName,
                arrFileData);
            if (resultDTO != null && resultDTO.getKey() != null && RESULT.FAIL
                .equals(resultDTO.getKey())) {
              //goi WO
              WoDTOSearch dTOSearch = new WoDTOSearch();
              if (!StringUtils.isStringNullOrEmpty(ptCode)) {
                dTOSearch.setUserId("256066");
                dTOSearch.setWoSystemId(ptCode);
                dTOSearch.setPage(1);
                dTOSearch.setPageSize(Integer.MAX_VALUE);
                dTOSearch.setSortName("woCode");
                dTOSearch.setSortType("");
                List<WoDTOSearch> lstWo = woServiceProxy.getListDataSearchProxy(dTOSearch);
                if (lstWo != null && !lstWo.isEmpty()) {
                  for (WoDTOSearch search : lstWo) {
                    woServiceProxy
                        .deleteWOForRollbackProxy(search.getWoCode(), "rollback TT", "TT");
                  }
                }
              }
            }
          }

        } else {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage(ret);
          resultDTO.setId(null);
        }
      } else {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(validateAcc);
        resultDTO.setId(null);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (resultDTO != null) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(e.getMessage());
        resultDTO.setId(null);
      }
      if (resultDTO != null && resultDTO.getKey() != null && RESULT.FAIL
          .equals(resultDTO.getKey())) {
        //goi WO
        WoDTOSearch dTOSearch = new WoDTOSearch();
        if (!StringUtils.isStringNullOrEmpty(ptCode)) {
          dTOSearch.setUserId("256066");
          dTOSearch.setWoSystemId(ptCode);
          dTOSearch.setPage(1);
          dTOSearch.setPageSize(Integer.MAX_VALUE);
          dTOSearch.setSortName("woCode");
          dTOSearch.setSortType("");
          List<WoDTOSearch> lstWo = woServiceProxy.getListDataSearchProxy(dTOSearch);
          if (lstWo != null && !lstWo.isEmpty()) {
            for (WoDTOSearch search : lstWo) {
              woServiceProxy.deleteWOForRollbackProxy(search.getWoCode(), "rollback TT", "TT");
            }
          }
        }
      }
    }
    return resultDTO;
  }


  public ResultDTO onInsertTroubleMobileNew(TroublesDTO troublesDTO, List<String> listAccount,
      String[] arrFileName, byte[][] arrFileData) throws Exception {

    TroublesInSideDTO troublesInSideDTO = troublesDTO.toModelInSide();
    ResultDTO resultDTO = new ResultDTO();
    try {
      resultDTO = insertTroubleMobile(troublesDTO, listAccount, true);
      if (resultDTO != null && resultDTO.getKey().equals(RESULT.SUCCESS)) {
        if (null != arrFileName && arrFileName.length > 0) {
          ResultInSideDto resultInsertFile = onInsertTroubleFileWS(troublesInSideDTO, arrFileName,
              arrFileData);
          if (resultInsertFile != null && !resultInsertFile.getKey()
              .equals(RESULT.SUCCESS)) {
            resultDTO.setId(null);
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage(resultInsertFile.getMessage());
            throw new Exception(resultInsertFile.getMessage());
          }
        }

      }
    } catch (Exception ex) {
      resultDTO = new ResultDTO();
      resultDTO.setId(null);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(ex.getMessage());
      log.error(ex.getMessage(), ex);
      throw new Exception(ex.getMessage());
    }
    return resultDTO;
  }

  public ResultDTO insertTroubleMobile(TroublesDTO tForm, List<String> listAccount,
      boolean isSendSms) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    ResultDTO resultWO = new ResultDTO();
    //Lay thong tin cau hinh thoi gian xu ly ticket
    try {
      Date now = new Date();
      System.out.println("--com.viettel.gnoc.onInsertTroubleMobile-- " + tForm.getTroubleName()
          + " CfgTimeTroubleProcessDTO ");
      Long alarmGroupIdTmp =
          StringUtils.validString(tForm.getAlarmGroupId()) ? Long.valueOf(tForm.getAlarmGroupId())
              : null;
      Long priorityIdTmp =
          StringUtils.validString(tForm.getPriorityId()) ? Long.valueOf(tForm.getPriorityId())
              : null;
      CfgTimeTroubleProcessDTO config = troublesRepository
          .getConfigTimeTroubleProcess(Long.valueOf(tForm.getTypeId()), alarmGroupIdTmp,
              priorityIdTmp, tForm.getInsertSource());

      System.out.println("--com.viettel.gnoc.onInsertTroubleMobile-- " + tForm.getTroubleName()
          + " CfgTimeTroubleProcessDTO " + " " + (new Date().getTime() - now.getTime()) / 1000);
      if (tForm.getAutoCreateWO() != null && "1".equals(String.valueOf(tForm.getAutoCreateWO()))) {

        now = new Date();
        TroublesInSideDTO troublesInSideDTO = tForm.toModelInSide();
        resultWO = woCreateBusiness.createWOMobile(troublesInSideDTO, config, troublesRepository);

        System.out.println("--com.viettel.gnoc.onInsertTroubleMobile-- " + tForm.getTroubleName()
            + " createWOMobile " + " " + (new Date().getTime() - now.getTime()) / 1000);
        //Neu tao WO thanh cong thi tao Ticket voi trang thai cho tiep nhan
        if (RESULT.SUCCESS.equals(resultWO.getMessage())) {
          tForm.setWoCode(resultWO.getId());
          tForm.setWoContent(resultWO.getKey());
        } else {
          //Tao WO thanh cong thi ticket duoc tao la Cho tiep nhan
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage(resultWO.getMessage());
          throw new Exception("Create Wo Fail: " + resultWO.getMessage());
        }
      }
      tForm.setTechnology(tForm.getInfraType());
      tForm.setState("5");//Tao WO thanh cong thi ticket duoc tao la Da tiep nhan
      String queueTimeTmp = DateUtil.date2ddMMyyyyHHMMss(new Date());
      tForm.setQueueTime(queueTimeTmp); //Thoi diem tiep nhan la thoi diem hien tai.
      //Giao tiep SPM_VTNET
      // neu insert source khac TT thi lay thoi gian bat dau theo nguon
      if (tForm != null && tForm.getInsertSource() != null
          && !"TT".equalsIgnoreCase(tForm.getInsertSource())
          && tForm.getBeginTroubleTime() != null
          && tForm.getBeginTroubleTime().length() > 0) {
        tForm.setBeginTroubleTime(tForm.getBeginTroubleTime());
      } else {
        tForm.setBeginTroubleTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
      }
      tForm.setEndTroubleTime(null);
      //Insert trouble
      now = new Date();
      TroublesInSideDTO troublesInSideDTO = tForm.toModelInSide();
      resultDTO = onInsertTroubleDTOMobile(troublesInSideDTO, listAccount, config);
      if (resultWO.getId() != null) {
        resultDTO.setId(resultDTO.getId() + "," + resultWO.getId());
      }
      System.out.println("--com.viettel.gnoc.onInsertTroubleMobile-- " + tForm.getTroubleName()
          + " Business onInsertTroubleMobile " + " "
          + (new Date().getTime() - now.getTime()) / 1000);
      if (RESULT.SUCCESS.equals(resultDTO.getKey())) {
        if (resultWO.getKey() != null) {
          resultDTO.setMessage(resultWO.getKey());
        }
      } else {
        throw new Exception("Create TT Fail: " + resultDTO.getMessage());
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO = new ResultDTO();
      resultDTO.setId(null);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(ex.getMessage());
      throw ex;
    }
    return resultDTO;
  }

  public ResultDTO onInsertTroubleDTOMobile(TroublesInSideDTO tForm, List<String> listAccount,
      CfgTimeTroubleProcessDTO config)
      throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    try {
      TroublesEntity trouble = tForm.toEntity();
      updateTroubleInfo(tForm, trouble, config);
      //
      if (trouble.getInsertSource() != null && trouble.getInsertSource().contains("SPM")
          && trouble.getCreateUserId() == null) {
        Map<String, String> mapProperty = troublesRepository.getConfigProperty();
        String UserIdDefault = mapProperty.get("WO.TT.CREATE_PERSON.ID");
        trouble.setCreateUserId(Long.valueOf(UserIdDefault));
        UsersEntity user = userBusiness.getUserByUserId(Long.valueOf(UserIdDefault));
        trouble.setCreateUserName(user.getUsername());
      }
      //HaiNV20
      ResultInSideDto result = troublesRepository.insertTroubles(trouble);
      long id = result.getId();
      //inert worklog
      tForm.setCreateUnitName(trouble.getCreateUnitName());
      tForm.setCreateUserName(trouble.getCreateUserName());
      onInsertWorkLog(tForm);
      //insert file
      List<String> lstFiles = DataUtil.splitListFileByComma(tForm.getAttachFileList());
      onInsertTroubleFile(tForm, lstFiles);
      //insert trouble node
      onInsertTroubleNode(tForm);
      //HaiNV20 open comment out start

      if (listAccount != null && listAccount.size() > 0) {
        for (String s : listAccount) {
          ItAccountEntity i = new ItAccountEntity(null, id, s);
          itAccountRepository.insertItAccount(i);
        }
      }
      //thá»±c hiá»‡n thÃªm má»›i SP map vá»›i IT
      if (!DataUtil.isNullOrEmpty(tForm.getSpmCode())) {
        ItSpmInfoEntity itSpmInfo = new ItSpmInfoEntity(null, id, Long.parseLong(tForm.getSpmId()),
            tForm.getSpmCode(), trouble.getTroubleCode());
        itSpmInfoRepository.insertItSpmInfo(itSpmInfo);
      }
      TroubleActionLogsEntity troubleActionLogsDTO = new TroubleActionLogsEntity(null,
          trouble.toString(),
          new Date(),
          tForm.getCreateUnitId(), tForm.getCreateUserId(),
          I18n.getLanguage("TransitionStateConfigDialog.insert"),
          tForm.getTroubleId(), tForm.getCreateUnitName(),
          tForm.getCreateUserName(), tForm.getState(),
          trouble.getTroubleCode(), tForm.getStateName(), null, null);
      troubleActionLogsDTO.setContent(I18n.getLanguage("TransitionStateConfigDialog.insert") + " "
          + trouble.getInsertSource() + " :" + RESULT.SUCCESS + "" + trouble.toString());

      if ("SPM_VTNET".equals(trouble.getInsertSource())
          || "SPM".equals(trouble.getInsertSource())) {

        tForm.setWorkLog(I18n.getLanguage("TransitionStateConfigDialog.insert") + " "
            + trouble.getInsertSource() + " :" + RESULT.SUCCESS + ", Trouble code:" + tForm
            .getTroubleCode()
            + ", WoCode:" + tForm.getWoCode() + " , " + tForm.getWoContent()
        );
        troubleActionLogsDTO.setInsertSource(trouble.getInsertSource());
        ActionInfoDTO info = new ActionInfoDTO(tForm.toModelOutSide());

        troubleActionLogsDTO.setActionInfo(info.toString());
      }
      troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO);

      //HaiNV20 open comment out end
      resultDTO.setId(trouble.getTroubleCode());
      resultDTO.setMessage(RESULT.SUCCESS);
      resultDTO.setKey(RESULT.SUCCESS);
      resultDTO.setFinishTime(
          DateTimeUtils.convertDateToString(trouble.getCreatedTime(), "dd/MM/yyyy HH:mm:ss"));

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setId(null);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(ex.getMessage());
      throw ex;
    }
    return resultDTO;
  }

  //duongnt start
  @Override
  public ResultInSideDto updateTroubleSpmVTNET(TroublesDTO troublesDTO) {
    ResultInSideDto resultDTO = new ResultInSideDto();
    try {
      resultDTO = troublesRepository.updateTroubleSpmVTNET(troublesDTO);
      TroubleActionLogsEntity troubleActionLogsDTO = new TroubleActionLogsEntity(null,
          I18n.getLanguage("spm.vtnet.update"),
          new Date(),
          troublesDTO.getReceiveUnitId() == null ? -1
              : Long.valueOf(troublesDTO.getReceiveUnitId()),
          troublesDTO.getReceiveUserId() == null ? -1
              : Long.valueOf(troublesDTO.getReceiveUserId()),
          I18n.getLanguage("common.btn.update"),
          troublesDTO.getTroubleId() == null ? -1 : Long.valueOf(troublesDTO.getTroubleId()),
          troublesDTO.getReceiveUnitName(),
          troublesDTO.getReceiveUserName(),
          Long.valueOf(troublesDTO.getState()),
          troublesDTO.getTroubleCode(),
          troublesDTO.getStateName(), null, null);
      troubleActionLogsDTO
          .setContent(I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS + " "
              + I18n.getLanguage("spm.vtnet.update"));
      troubleActionLogsDTO.setRootCause(troublesDTO.getRootCause());
      troubleActionLogsDTO.setWorkArround(troublesDTO.getWorkArround());
      troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO);

//      resultDTO.setId(Long.valueOf(troublesDTO.getTroubleCode()));
      resultDTO.setMessage(RESULT.SUCCESS);
      resultDTO.setKey(RESULT.SUCCESS);

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(ex.getMessage());
    }
    return resultDTO;
  }

  @Override
  public TroublesDTO checkAccountSPM(List<String> lstAccount, String insertSource) {
    TroublesDTO troublesDTO = troublesRepository.checkAccountSPM(lstAccount, insertSource);
    return troublesDTO;
  }
//duongnt end

  @Override
  public ResultDTO onInsertTroubleFileWS(AuthorityDTO requestDTO, TroublesDTO troublesDTO,
      List<String> listAccount, String[] arrFileName, byte[][] arrFileData) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      String ret;
      ValidateAccount account = new ValidateAccount();
      String validateAcc = account.checkAuthenticate(requestDTO);
      if (validateAcc.equals(RESULT.SUCCESS)) {
        if (DataUtil.isNullOrEmpty(troublesDTO.getInsertSource())) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("trouble.insertSourceNull");
          return resultDTO;
        }
        ret = validateDTO(troublesDTO, null);
        if ("".equals(ret)) {
          String id = getSequenseTroubles("TROUBLES_SEQ", 1).get(0);
          troublesDTO.setTroubleId(id);
          String itemCode = troublesDTO.getTypeName();
          String ptCode =
              "GNOC_TT_" + itemCode + "_" + DateTimeUtils.convertDateToString(new Date(), "yyMMdd")
                  + "_" + id;
          troublesDTO.setTroubleCode(ptCode);
          troublesDTO.setEndTroubleTime(null);
          TroublesInSideDTO troublesInSideDTO = troublesDTO.toModelInSide();
          resultDTO = onInsertTroubleFileWSNew(troublesInSideDTO, listAccount, arrFileName,
              arrFileData);
          resultDTO.setId(ptCode);
        } else {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage(ret);
          resultDTO.setId(null);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(I18n.getLanguage("common_error"));
      resultDTO.setId(null);
    }
    return resultDTO;
  }

  public ResultDTO onInsertTroubleFileWSNew(TroublesInSideDTO troublesDTO, List<String> listAccount,
      String[] arrFileName, byte[][] arrFileData) throws Exception {

    ResultDTO resultDTO = new ResultDTO();
    ResultInSideDto result;
    try {
      result = onInsertTrouble(troublesDTO, listAccount, true, null);
      resultDTO.setKey(result.getKey());
      resultDTO.setMessage(result.getMessage());
      resultDTO.setId(result.getId() == null ? null : String.valueOf(result.getId()));
      if (result != null && result.getKey().equals(RESULT.SUCCESS)) {
        if (null != arrFileName && arrFileName.length > 0) {
          ResultInSideDto resultInsertFile = onInsertTroubleFileWS(troublesDTO, arrFileName,
              arrFileData);
          if (resultInsertFile != null && !resultInsertFile.getKey()
              .equals(RESULT.SUCCESS)) {
            result.setId(null);
            result.setKey(RESULT.FAIL);
            result.setMessage(RESULT.FAIL);
            throw new Exception(resultInsertFile.getMessage());
          }
        }
      }
    } catch (Exception ex) {
      resultDTO = new ResultDTO();
      resultDTO.setId(null);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(I18n.getLanguage("common_error"));
      log.error(ex.getMessage(), ex);
      throw new Exception(ex.getMessage());
    }
    return resultDTO;
  }

  public ResultInSideDto onInsertTroubleFileWS(TroublesInSideDTO trouble, String[] arrFileName,
      byte[][] arrFileData) throws Exception {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    try {
      Date now = new Date();
      if (arrFileData != null && arrFileData.length > 0) {
        Long createUserId;
        if (null != trouble.getCreateUserId() && !"".equals(trouble.getCreateUserId())) {
          createUserId = trouble.getCreateUserId();
        } else {
          Map<String, String> mapConfigProperty = getConfigProperty();
          String UserIdDefault = mapConfigProperty.get("WO.TT.CREATE_PERSON.ID");
          createUserId = Long.valueOf(UserIdDefault);
        }
        UsersEntity usersEntity = userBusiness.getUserByUserId(createUserId);
        UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
        List<GnocFileDto> gnocFileDtos = new ArrayList<>();
        for (int i = 0; i < arrFileData.length; i++) {
          byte[] fileData = arrFileData[i];
          String fileName = arrFileName[i];
          fileName = fileName
              .replaceAll("[~!@#$%^&*()+`=;,\\-\\s+\\[\\]{}']", "_")
              .replaceAll("(_)+", "_");
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, fileName, fileData, null);
          //Start save file old
          String fullPathOld = FileUtils.saveUploadFile(fileName, fileData, uploadFolder, null);
          CommonFileDTO commonFileDTO = new CommonFileDTO();
          commonFileDTO.setCreateTime(now);
          commonFileDTO.setCreateUserId(createUserId);
          commonFileDTO.setPath(FileUtils.getFilePath(fullPathOld) + File.separator);
          commonFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
          ResultInSideDto resultFileDataOld = troublesRepository.insertCommonFile(commonFileDTO);
          TroubleFileEntity troubleFile = new TroubleFileEntity();
          troubleFile.setTroubleId(Long.valueOf(trouble.getTroubleId()));
          troubleFile.setFileId(resultFileDataOld.getId());
          troublesRepository.insertTroubleFile(troubleFile);
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(fileName);
          gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(createUserId);
          gnocFileDto.setCreateUserName(usersEntity.getUsername());
          gnocFileDto.setCreateTime(commonFileDTO.getCreateTime());
          gnocFileDto.setMappingId(resultFileDataOld.getId());
          gnocFileDtos.add(gnocFileDto);
        }
        gnocFileRepository
            .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.TROUBLES, trouble.getTroubleId(),
                gnocFileDtos);
        resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
        resultInSideDTO.setMessage(Constants.RESULT.SUCCESS);
      }
    } catch (Exception ex) {
      resultInSideDTO.setId(null);
      resultInSideDTO.setKey(RESULT.FAIL);
      resultInSideDTO.setMessage(ex.getMessage());
      log.error(ex.getMessage(), ex);
    }
    return resultInSideDTO;
  }

  @Override
  public Datatable onSearch(TroublesInSideDTO dto) {
    log.debug("Request to onSearchTroublesDTO: {}", dto);
    //start Dunglv3 edit
    if ("unitMove".equals(dto.getCheckMoveUnit())) {
      UserToken userToken = ticketProvider.getUserToken();
      UsersDTO usersDTO = troublesRepository.getUnitIdByUserId(userToken.getUserID());
      if (StringUtils.isNotNullOrEmpty(usersDTO.getUnitId())) {
        dto.setUnitMove(Long.valueOf(usersDTO.getUnitId()));
      }
    }
    //end
    Datatable datatable = troublesRepository.onSearch(dto);
    List<TroublesInSideDTO> troublesDTOS = (List<TroublesInSideDTO>) datatable.getData();
    updateTroubleDTOInfo(troublesDTOS);
    datatable.setData(troublesDTOS);
    return datatable;
  }

  @Override
  public TroublesInSideDTO findTroublesById(Long id) throws Exception {
    TroublesInSideDTO troublesDTO = troublesRepository.findTroublesById(id).toDTO();
    try {
      CatItemDTO catItemDTO = catItemBusiness.getCatItemById(troublesDTO.getState());
      String itemCode = catItemDTO == null ? "" : catItemDTO.getItemCode();
      Double time_process = troublesDTO.getTimeProcess() == null ? 0 : troublesDTO.getTimeProcess();
      Double time_used = troublesDTO.getTimeUsed() == null ? 0 : troublesDTO.getTimeUsed();
      Date clear_time =
          troublesDTO.getClearTime() == null ? new Date() : troublesDTO.getClearTime();
      Date assign_time_temp = troublesDTO.getAssignTimeTemp() == null ? troublesDTO.getAssignTime()
          : troublesDTO.getAssignTimeTemp();
      Double remainTime = null;
      if ("WAITING RECEIVE".equalsIgnoreCase(itemCode) || "QUEUE".equalsIgnoreCase(itemCode) ||
          "SOLUTION FOUND".equalsIgnoreCase(itemCode) || "CLEAR".equalsIgnoreCase(itemCode) ||
          "CLOSED".equalsIgnoreCase(itemCode) || "CLOSED NOT KEDB".equalsIgnoreCase(itemCode) ||
          "WAIT FOR DEFERRED".equalsIgnoreCase(itemCode)
      ) {
        remainTime = time_process - (time_used +
            Math.round((clear_time.getTime() - assign_time_temp.getTime()) * 100 / (1000 * 60 * 60))
                * 1.0 / 100);

      } else if ("DEFERRED".equalsIgnoreCase(itemCode)) {
        remainTime = time_process - time_used;
      }
      troublesDTO.setRemainTime(remainTime == null ? null : String.valueOf(remainTime));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    Double offset = TimezoneContextHolder.getOffsetDouble();
    setTimeZoneForTrouble(troublesDTO, offset);
    return troublesDTO;
  }

  @Override
  public List<String> getSequenseTroubles(String seqName, int... size) {
    log.debug("Request to getSequenseTroubles: {}", seqName);
    return troublesRepository.getSequenseTroubles(seqName, size);
  }

  @Override
  public List<TroublesInSideDTO> searchByConditionBean(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    log.debug("Request to searchByConditionBean: {}", lstCondition);
    return troublesRepository.searchByConditionBean(lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public Datatable onSearchTroubleRelated(TroublesInSideDTO troublesDTO) {
    log.debug("Request to onSearchTroubleRelated : {}", troublesDTO);
    Datatable datatable = troublesRepository.onSearchTroubleRelated(troublesDTO);
    List<TroublesInSideDTO> list = (List<TroublesInSideDTO>) datatable.getData();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    for (TroublesInSideDTO dto : list) {
      try {
        setTimeZoneForTrouble(dto, offset);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    datatable.setData(list);
    return datatable;
  }

  @Override
  public ResultInSideDto insertTroublesTT(AuthorityDTO requestDTO, List<MultipartFile> files,
      TroublesInSideDTO troublesDTO, List<String> listAccount) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    Date currDate = new Date();
    setTimeZoneForTrouble(troublesDTO, -1L * TimezoneContextHolder.getOffsetDouble());
    troublesDTO.setCreatedTime(currDate);
    List<String> fileName = new ArrayList<>();
    List<byte[]> dataArray = new ArrayList<>();
    if (files != null && files.size() > 0) {
      for (int i = 0; i < files.size(); i++) {
        dataArray.add(files.get(i).getBytes());
        fileName.add(files.get(i).getOriginalFilename());
      }
      troublesDTO.setFileDocumentByteArray(dataArray);
      troublesDTO.setArrFileName(fileName);
    }
    troublesDTO = createDTOToInsert(troublesDTO, ticketProvider.getUserToken());

    TroublesDTO troublesOutSideDTO = troublesDTO.toModelOutSide();

    ResultDTO resultDTO = insertTroublesNOC(requestDTO, troublesOutSideDTO, listAccount);
    resultInSideDto.setKey(resultDTO.getKey());
    resultInSideDto.setMessage(resultDTO.getMessage());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto onUpdateTroubleTT(TroublesInSideDTO tForm,
      List<MultipartFile> multipartFileList)
      throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {

      UserToken userToken = ticketProvider.getUserToken();
      setTimeZoneForTrouble(tForm, -1L * TimezoneContextHolder.getOffsetDouble());
      TroublesInSideDTO troubleOld = troublesRepository
          .getTroubleDTO(tForm.getTroubleId() == null ? null : String.valueOf(tForm.getTroubleId()),
              null, null, null, null, null, null);
      tForm = createDTO(tForm, troubleOld, userToken);
      List<String> fileName = new ArrayList<>();
      List<byte[]> dataArray = new ArrayList<>();
      if (multipartFileList != null && multipartFileList.size() > 0) {
        for (int i = 0; i < multipartFileList.size(); i++) {
          dataArray.add(multipartFileList.get(i).getBytes());
          fileName.add(multipartFileList.get(i).getOriginalFilename());
        }
        tForm.setFileDocumentByteArray(dataArray);
        tForm.setArrFileName(fileName);
      }

      CatItemDTO stateDTOOld = catItemBusiness.getCatItemById(troubleOld.getState());
      CatItemDTO stateDTO = catItemBusiness.getCatItemById(tForm.getState());
      callSIRC(tForm, troubleOld, stateDTOOld, stateDTO, troublesRepository.getConfigProperty());
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      //chuyển trạng thái về Đóng hoặc Đóng chưa có KeDB: gọi WS SPM
      String resultCallOtherSys =
          callOtherSystem(tForm, troubleOld, userToken, unitToken);
      if (!StringUtils.isStringNullOrEmpty(resultCallOtherSys) && resultCallOtherSys.length() > 0) {
        ///ghi log message
        log.error("Result call other system: " + resultCallOtherSys);
        log.error("Goi sang auto check co loi");
      } else {
        log.debug("HNV write log call service update TT");
        tForm.setSqlQuery(SYSTEM.TT.toString());
        resultInSideDto = onUpdateTrouble(tForm, false);
        showTicketAfterUpdate(userToken, resultInSideDto, troubleOld, tForm);
      }
    } catch (Exception ex) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey("NOT CLOSED");
      resultInSideDto.setMessage(ex.getMessage());
    }
    return resultInSideDto;
  }

  @Override
  public ResultDTO insertTroublesNOC(AuthorityDTO requestDTO, TroublesDTO troublesDTO,
      List<String> listAccount) throws Exception {
    log.debug("Request to insertTroublesNOC: {}", troublesDTO);
    ResultDTO resultDTO = new ResultDTO();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    String startTime = "";
    String ptCode = "";
    String ret = "";
    try {
      Map<String, String> map = troublesDTO.getMap();
      if (map != null) {
        troublesDTO.setIsStationVip(map.get("ISSTATIONVIP"));
        troublesDTO.setCdId(map.get("RECEIVE_UNIT_WO_CODE"));
        troublesDTO.setCatchingTime(map.get("CATCHING_TIME"));
        troublesDTO.setLinkId(map.get("LINK_ID"));
        troublesDTO.setLinkCode(map.get("LINK_CODE"));
        troublesDTO.setAmiId(map.get("AMI_ID"));
        troublesDTO.setCameraId(map.get("CAMERA_ID"));
        troublesDTO.setNationCode(map.get("COUNTRY_CODE"));
        //ami one
        troublesDTO.setAccountGline(map.get("ACCOUNT_NAME"));
        troublesDTO.setCustomerName(map.get("CUSTOMER_NAME"));
        troublesDTO.setCustomerPhone(map.get("CUSTOMER_PHONE"));
      }

      List<TroubleMopDTO> lstMop = troublesDTO.getLstMop();
      startTime = DateTimeUtils.getSysDateTime();
      ValidateAccount account = new ValidateAccount();
      String validateAcc = account.checkAuthenticate(requestDTO);
      if (validateAcc.equals(RESULT.SUCCESS)) {
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getInsertSource())) {
          resultDTO.setKey(RESULT.ERROR);
          resultDTO.setMessage("trouble.insertSourceNull");
          return resultDTO;
        }

        if (!"TT".equals(troublesDTO.getInsertSource())) {
          ret = validateDTO(troublesDTO, lstMop);
        } else {
          if (troublesDTO.getAlarmGroupCode() == null || ""
              .equals(troublesDTO.getAlarmGroupCode())) {
            ret = "alarm_group_code is not null";
          }
          CatItemDTO alarmGroupDTO = new CatItemDTO();
          alarmGroupDTO.setItemCode(troublesDTO.getAlarmGroupCode());
          List<CatItemDTO> lstAlarmGroup = catItemBusiness.getListCatItemDTO(alarmGroupDTO);
          if (lstAlarmGroup != null && !lstAlarmGroup.isEmpty()) {
            troublesDTO.setAlarmGroupId(String.valueOf(lstAlarmGroup.get(0).getItemId()));
            troublesDTO.setAlarmGroupCode(lstAlarmGroup.get(0).getItemValue());
          } else {
            ret = "alarm_group_code is not exits";
          }
        }
        if ("".equals(ret)) {
          CfgCreateWoDTO createWoDTO = new CfgCreateWoDTO();
          createWoDTO.setTypeId(troublesDTO.getTypeId());
          createWoDTO.setAlarmGroupId(troublesDTO.getAlarmGroupId());
          List<CfgCreateWoDTO> lstCfg = cfgCreateWoBusiness
              .getListCfgCreateWoDTO(createWoDTO, 0, 1000, "", "");
          if (lstCfg != null && !lstCfg.isEmpty() && troublesDTO.getInsertSource()
              .contains("NOC")) {
            if (troublesDTO.getAutoClose() != null && troublesDTO.getAutoCreateWO() != null
                && troublesDTO.getAutoClose().equals(1L) && troublesDTO.getAutoCreateWO()
                .equals(1L)) {
              //dong tu dong, tao wo tu dong thi ko tao wo tai san
            } else if (troublesDTO.getAutoCreateWO() != null && troublesDTO.getAutoCreateWO()
                .equals(1L)) {
              troublesDTO.setCheckbox("1");
            }
          } else if (lstCfg != null && !lstCfg.isEmpty() && "TT"
              .equals(troublesDTO.getInsertSource())
              && troublesDTO.getAutoCreateWO() != null && troublesDTO.getAutoCreateWO()
              .equals(1L)) {
            troublesDTO.setCheckbox("1");
          }
          if (troublesDTO.getInsertSource().contains("NOC")) {
            List<MapFlowTemplatesDTO> lstMap = troublesRepository
                .getMapFlowTemplate(troublesDTO.getTypeId(), troublesDTO.getAlarmGroupId());
            if (lstMap != null && !lstMap.isEmpty()) {
              troublesDTO.setCheckbox("2");
            }
          }
          String id = getSequenseTroubles("TROUBLES_SEQ", 1).get(0);
          troublesDTO.setTroubleId(id);
          String itemCode = troublesDTO.getTypeName();
          ptCode = "GNOC_TT_" + itemCode + "_" + DateTimeUtils
              .convertDateToString(new Date(), "yyMMdd") + "_" + id;
          troublesDTO.setTroubleCode(ptCode);
          TroublesInSideDTO troublesInSideDTO = troublesDTO.toModelInSide();
          resultInSideDto = onInsertTrouble(troublesInSideDTO, listAccount, true, lstMop);
          resultDTO.setId(ptCode);
          resultDTO.setKey(resultInSideDto.getKey());
          resultDTO.setMessage(resultInSideDto.getMessage());
          resultDTO.setFinishTime(troublesDTO.getCreatedTime());
          if (resultDTO != null && resultDTO.getKey() != null && RESULT.ERROR
              .equals(resultDTO.getKey())) {
            log.info("--Xoa PT-- 1" + ptCode);
            WoDTOSearch dTOSearch = new WoDTOSearch();
            if (!StringUtils.isStringNullOrEmpty(ptCode)) {
              log.info("--Xoa PT-- 2" + ptCode);
              dTOSearch.setUserId("256066");
              dTOSearch.setWoSystemId(ptCode);
              dTOSearch.setPage(1);
              dTOSearch.setPageSize(Integer.MAX_VALUE);
              dTOSearch.setSortName("woCode");
              dTOSearch.setSortType("asc");
              List<WoDTOSearch> lstWo = woServiceProxy
                  .getListDataSearchProxy(dTOSearch);
              if (lstWo != null && !lstWo.isEmpty()) {
                for (WoDTOSearch search : lstWo) {
                  System.out.println("--Xoa PT-- 3" + ptCode + " " + search.getWoCode());
                  woServiceProxy.deleteWOForRollbackProxy(search.getWoCode(), "rollback TT", "TT");
                }
              }
            }
          }

        } else {
          if (ret != null && (ret.contains("_XXXXXXYYYYYY") || ret.contains("_XXXXXXZZZZZZ"))) {
            resultDTO.setKey(RESULT.SUCCESS);
            if (ret.contains("_XXXXXXYYYYYY")) {
              ret = ret.substring(0, ret.indexOf("_XXXXXXYYYYYY"));
            }
            resultDTO.setMessage(ret);
          } else {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage(ret);
          }
        }
      } else {
        resultDTO.setKey(RESULT.ERROR);
        resultDTO.setMessage(validateAcc);
      }
      resultDTO.setRequestTime(startTime);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setId(null);
      resultDTO.setKey(RESULT.ERROR);
      resultDTO.setMessage(e.getMessage());
      //goi WO
      System.out.println("--Xoa PT-- 1" + ptCode);
      WoDTOSearch dTOSearch = new WoDTOSearch();
      if (!StringUtils.isStringNullOrEmpty(ptCode)) {
        dTOSearch.setUserId("256066");
        dTOSearch.setWoSystemId(ptCode);
        dTOSearch.setPage(1);
        dTOSearch.setPageSize(Integer.MAX_VALUE);
        dTOSearch.setSortName("woCode");
        dTOSearch.setSortType("asc");
        System.out.println("--Xoa PT-- 2" + ptCode);
        List<WoDTOSearch> lstWo = woServiceProxy
            .getListDataSearchProxy(dTOSearch);
        if (lstWo != null && !lstWo.isEmpty()) {
          for (WoDTOSearch search : lstWo) {
            System.out.println("--Xoa PT-- 3" + ptCode + " " + search.getWoCode());
            woServiceProxy.deleteWOForRollbackProxy(search.getWoCode(), "rollback TT", "TT");
          }
        }
      }

      //tiennv them de rollback data neu co loi
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//      throw new RuntimeException(e.getMessage());
    }

    return resultDTO;
  }

  public ResultInSideDto onInsertTrouble(TroublesInSideDTO tForm, List<String> listAccount,
      boolean isSendSms,
      List<TroubleMopDTO> lstMop) throws Exception {
    //Lay thong tin cau hinh thoi gian xu ly ticket
    log.debug("Request to onInsertTrouble: {}", tForm);
    ResultInSideDto result = new ResultInSideDto();
    try {

      String insertSource = tForm.getInsertSource();
      if ("TT".equals(tForm.getInsertSource())) {
        insertSource = tForm.getCountry();
      }

      CfgTimeTroubleProcessDTO config = troublesRepository.getConfigTimeTroubleProcess(
          tForm.getTypeId(), Long.valueOf(tForm.getAlarmGroupId()),
          tForm.getPriorityId(), insertSource);
      if (insertSource != null && config == null && "VSMART".equals(insertSource)) {
        result.setKey(RESULT.ERROR);
        result.setMessage(I18n.getLanguage("incident.admin"));
        return result;
      }
      //cap nhat thoi gian lai cho tram vip
      if (config != null && tForm.getIsStationVip() != null && 1 == (tForm.getIsStationVip())
          && config.getTimeStationVip() != null) {
        config.setProcessTtTime(config.getTimeStationVip());
        config.setProcessWoTime(config.getTimeWoVip());
      }
      //them mop
      if (lstMop != null && !lstMop.isEmpty()) {
        for (TroubleMopDTO mopOutSiteDTO : lstMop) {
          TroubleMopInsiteDTO mopDTO = mopOutSiteDTO.toInsiteDTO();
          mopDTO.setTroubleMopId(tForm.getTroubleId());
          mopDTO.setTroubleId(tForm.getTroubleId());
          mopDTO.setTblAlarmCurrent(tForm.getTblCurr());
          mopDTO.setAlarmId(tForm.getAlarmId());
          mopDTO.setCreateTime(new Date());
          if (mopDTO.getRunType() != null && 1 == (mopDTO.getRunType())) { //1 chay tu dong
            mopDTO.setIsRun(1L);
            mopDTO.setLastUpdateTime(null);
          } else {
            mopDTO.setIsRun(0L);
            mopDTO.setLastUpdateTime(new Date());
          }

          mopDTO.setWorkLog(DateTimeUtils.getSysDateTime() + " "
              + I18n.getLanguage("TransitionStateConfigDialog.insert") + " Mop");
          mopDTO.setStateMop(0L);
          troubleMopBussiness.insertTroubleMop(mopDTO);
        }
      }

      if (tForm.getAutoCreateWO() != null && (tForm.getAutoCreateWO().equals(1L) || tForm
          .getAutoCreateWO().equals(2L))) {
        ResultDTO resultWO = woCreateBusiness.createWO(tForm, config, troublesRepository);

        //Check kqua
        if (RESULT.SUCCESS.equals(resultWO.getKey())) {
          tForm.setWoCode(resultWO.getId());

          //tao su co
          result = onInsertTrouble(tForm, config, listAccount);
          if (result.getKey().equals(RESULT.SUCCESS)) {
            result.setMessage(
                StringUtils.isStringNullOrEmpty(resultWO.getRequestTime()) == true ? String
                    .valueOf(resultWO.getId()) : resultWO.getRequestTime());
          }

        } else {
          // nếu là tạo từ NOC thì vẫn cho tạo TT khi fail WO
          if (tForm.getInsertSource() != null && !tForm.getInsertSource().contains("NOC")) {
            result.setKey("FAIL_AUTO_WO");
            result.setMessage(resultWO.getMessage());
            throw new Exception("FAIL_AUTO_CREATE_WO: " + resultWO.getMessage());
          } else {
            if (resultWO.getRequestTime() != null && resultWO.getRequestTime().contains("UCTT:")) {
              tForm.setWoCode(resultWO.getId());
            }
            result = onInsertTrouble(tForm, config, listAccount);
            if (result.getKey().equals(RESULT.SUCCESS)) {
              result.setKey(RESULT.SUCCESS_NOT_WO);
              result.setMessage("FAIL_AUTO_CREATE_WO: " + resultWO.getMessage());
            }
          }
        }

      } else {
        result = onInsertTrouble(tForm, config, listAccount);
      }
      if (result != null && (result.getKey().equals(Constants.RESULT.SUCCESS) || result.getKey()
          .equals(RESULT.SUCCESS_NOT_WO)) && isSendSms
          && !(1 == (tForm.getState()))) { //du thao ko nhan tin
        sendSms(tForm, null);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
//      result = new ResultInSideDto(null, RESULT.ERROR, ex.getMessage());
      throw new Exception(ex.getMessage());
    }
    return result;
  }

  public ResultInSideDto onInsertTrouble(TroublesInSideDTO tForm, CfgTimeTroubleProcessDTO config,
      List<String> listAccount) throws Exception {
    log.debug("Request to onInsertTrouble: {}", tForm);
    ResultInSideDto result = new ResultInSideDto();
    try {
      TroublesEntity trouble = tForm.toEntity();
      trouble.setLastUpdateTime(new Date());
      updateTroubleInfo(tForm, trouble, config);

      //bo sung truong insert bo sung thong tin nguoi tao
      if (trouble.getInsertSource() != null && "SPM".equals(trouble.getInsertSource())
          && trouble.getCreateUserId() == null) {
        Map<String, String> mapProperty = troublesRepository.getConfigProperty();
        String UserIdDefault = mapProperty.get("WO.TT.CREATE_PERSON.ID");
        trouble.setCreateUserId(Long.valueOf(UserIdDefault));
        UsersEntity usersEntity = userBusiness.getUserByUserId(Long.valueOf(UserIdDefault));
        trouble.setCreateUserName(usersEntity.getUsername());
      }
      //HaiNV20 bÃ¡Â»â€¢ sung trong truong hÃ¡Â»Â£p insert tÃ¡Â»Â« SPM bÃ¡Â»â€¢ sung thÃƒÂªm thÃƒÂ´ng tin nguoi tÃ¡ÂºÂ¡o End.
      ResultInSideDto resultInSideDto = troublesRepository.insertTroubles(trouble);
      long id = resultInSideDto.getId();

      //insert worklog
      onInsertWorkLog(tForm);
      //insert trouble file
      List<String> lstFiles = DataUtil.splitListFileByComma(tForm.getAttachFileList());
      onInsertTroubleFile(tForm, lstFiles);
      //insert trouble node
      onInsertTroubleNode(tForm);
      //insert log
      TroubleActionLogsEntity troubleActionLogsEntity = new TroubleActionLogsEntity(null,
          trouble.toString(),
          new Date(),
          tForm.getCreateUnitId(),
          StringUtils.isStringNullOrEmpty(tForm.getCreateUserId()) ? null
              : tForm.getCreateUserId(),
          I18n.getLanguage("TransitionStateConfigDialog.insert"),
          tForm.getTroubleId(),
          tForm.getCreateUnitName(), tForm.getCreateUserName(), tForm.getState(),
          trouble.getTroubleCode(), tForm.getStateName(), null, null);
      troubleActionLogsEntity.setContent(
          I18n.getLanguage("TransitionStateConfigDialog.insert") + " " + trouble.getInsertSource()
              + " :" + RESULT.SUCCESS + "" + trouble.toString());
      troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsEntity);

      //HaiNV20 open comment out start
      if (listAccount != null && listAccount.size() > 0) {
        for (String s : listAccount) {
          ItAccountEntity i = new ItAccountEntity(null, id, s);
          itAccountRepository.insertItAccount(i);
        }
      }
      if (!StringUtils.isStringNullOrEmpty(tForm.getSpmCode())) {
        ItSpmInfoEntity itSpmInfo = new ItSpmInfoEntity(null, id, Long.parseLong(tForm.getSpmId()),
            tForm.getSpmCode(), trouble.getTroubleCode());
        itSpmInfoRepository.insertItSpmInfo(itSpmInfo);
      }
      //HaiNV20 open comment out end
      result.setId(id);
      result.setMessage(RESULT.SUCCESS);
      result.setKey(RESULT.SUCCESS);
      result.setFinishTime(
          DateTimeUtils.convertDateToString(trouble.getCreatedTime(), "dd/MM/yyyy HH:mm:ss"));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    return result;
  }

  //Important day pathFileAttach + lstFileName
  @Override
  public ResultInSideDto onUpdateTrouble(TroublesInSideDTO tForm, boolean callFromTKTU)
      throws Exception {
    String systemCall = tForm.getSqlQuery();
    ResultInSideDto result = new ResultInSideDto();
    Date now = new Date();
    boolean isSendSPm = false;
//    UserToken userToken = ticketProvider.getUserToken();
    TroublesInSideDTO troubleOld = troublesRepository
        .getTroubleDTO(tForm.getTroubleId() == null ? null : String.valueOf(tForm.getTroubleId()),
            null, null, null, null, null, null);
    TroublesEntity trouble = troublesRepository
        .findTroublesById(tForm.getTroubleId());
    CatItemDTO stateDTOOld = catItemBusiness.getCatItemById(troubleOld.getState());
    CatItemDTO stateDTO = catItemBusiness.getCatItemById(tForm.getState());

    if (!(10 == (tForm.getState())) && !(11 == (tForm.getState()))
        && (stateDTOOld.getItemCode().equals(Constants.TT_STATE.CLOSED) || stateDTOOld.getItemCode()
        .equals(Constants.TT_STATE.CLOSED_Not_KEDB))) {
      result.setMessage(I18n.getValidation("tt.is.closed"));
      result.setKey(RESULT.ERROR);
      return result;
    }
    Long isMove = trouble.getIsMove();
    updateTroubleByForm(trouble, tForm, troubleOld);

    if (!trouble.getState().equals(troubleOld.getState())) {
      //Truong hop chuyen sang trang thai Waiting Receive
      if (Constants.TT_STATE.Waiting_Receive.equals(stateDTO.getItemCode())) {
        if (Constants.TT_STATE.CLEAR.equals(stateDTOOld.getItemCode())
            || Constants.TT_STATE.REJECT.equals(stateDTOOld.getItemCode())) {
          if (Constants.TT_STATE.CLEAR.equals(stateDTOOld.getItemCode())) {
            Long countOpen = trouble.getCountReopen() == null ? 0L : trouble.getCountReopen();
            trouble.setCountReopen(countOpen + 1);
          }
          if (troubleOld.getReceiveUnitId().equals(tForm.getReceiveUnitId())) {
            //Giao lai cung don vi
            trouble.setClearTime(null);
            trouble.setClearUserId(null);
            trouble.setClearUserName(null);
          } else {
            //Giao lai khac don vi
            if (troubleOld.getAssignTime() == null) {
              trouble.setAssignTime(now);
            }
            trouble.setRejectedCode(null);
            trouble.setRejectReason("");
            trouble.setAssignTimeTemp(null);
            trouble.setTimeUsed(null);
            if (tForm.getCheckbox() != null && "3".equals(tForm.getCheckbox())) {
            } else {
              trouble.setQueueTime(null);
            }
            trouble.setDeferredTime(null);
            trouble.setDeferredReason("");
            trouble.setClearTime(null);
            trouble.setClearUserId(null);
            trouble.setClearUserName(null);
            trouble.setReasonId(null);
            trouble.setRootCause(null);
            trouble.setRelatedKedb("");
            trouble.setSolutionType(null);
            trouble.setWorkArround("");
            trouble.setEndTroubleTime(null);
            trouble.setReceiveUserName(null);
            trouble.setReasonName(null);
            trouble.setRelatedTroubleCodes(null);
            trouble.setClearUserId(null);
            trouble.setClearUserName(null);
            trouble.setTransReasonEffectiveContent("");
            trouble.setTransReasonEffectiveId(null);
            trouble.setTransNetworkTypeId(null);
            trouble.setWhereWrong("");
            trouble.setAsessmentData(null);
            trouble.setLineCutCode("");
            trouble.setCodeSnippetOff("");
            trouble.setClosuresReplace("");
            trouble.setCableType(null);
            trouble.setReasonLv1Id("");
            trouble.setReasonLv2Id("");
            trouble.setReasonLv3Id("");
            trouble.setReasonOverdueId("");
            trouble.setReasonOverdueId2("");
            trouble.setReasonLv1Name("");
            trouble.setReasonLv2Name("");
            trouble.setReasonLv3Name("");
            trouble.setReasonOverdueName("");
            trouble.setReasonOverdueName2("");
            trouble.setDateMove(null);
            trouble.setUnitMove(null);
            trouble.setUnitMoveName(null);
            trouble.setIsMove(null);
            trouble.setNumAon(null);
            trouble.setNumGpon(null);
            trouble.setNumNexttv(null);
            trouble.setNumThc(null);
            trouble.setNumPending(null);
            trouble.setInfoTicket(null);
          }
        }

        //Lan dau chuyen sang trang thai waiting receive
        if (troubleOld.getAssignTime() == null) {
          trouble.setAssignTime(now);
        }
        //ThanhLV12_bo xung chuyen sang trang thai clear set lai thoi gian clear
      } else if (!Constants.TT_STATE.CLEAR.equals(stateDTOOld.getItemCode())
          && Constants.TT_STATE.CLEAR.equals(stateDTO.getItemCode())) {
        if (trouble.getInsertSource().contains("NOC")) {
          trouble.setClearTime(trouble.getEndTroubleTime()); //tinh lai thoi gian con lai
        } else {
          trouble.setClearTime(now);
        }
        trouble.setClearUserId(tForm.getClearUserId());
        trouble.setClearUserName(tForm.getClearUserName());
      } else if (Constants.TT_STATE.QUEUE.equals(stateDTO.getItemCode())) {
        //Neu chuyen sang trang thai QUEUE
        if (troubleOld.getQueueTime() == null) {
          //Truong hop lan dau chuyen trang thai
          trouble.setQueueTime(now);
        }
        //Truong hop trang thai cu la tam dong. Mo lai trang thai Queue thi thiet lap thoi gian assign_time_temp
        if (Constants.TT_STATE.DEFERRED.equalsIgnoreCase(stateDTOOld.getItemCode())) {
          if (trouble.getInsertSource().contains("NOC") && trouble.getEndTroubleTime() != null) {
            //tinh lai thoi gian con lai
            trouble.setAssignTimeTemp(trouble.getEndTroubleTime());
          } else {
            trouble.setAssignTimeTemp(now);
          }
          if (trouble.getTroubleAssignId() != null) {
            TroubleAssignEntity troubleAssign = troubleAssignRepository
                .findTroubleAssignById(trouble.getTroubleAssignId());
            troubleAssign.setAssignTime(now);
            troubleAssignRepository.updateTroubleAssign(troubleAssign);
          }
          tForm.setStateWo("5");
        }
        //tu choi tam dong
        if (Constants.TT_STATE.Wait_for_Deferred.equalsIgnoreCase(stateDTOOld.getItemCode())) {
          trouble.setDeferredTime(null);
          trouble.setDeferredReason("");
        }

        //tam dong
      } else if (Constants.TT_STATE.DEFERRED.equals(stateDTO.getItemCode())
          && !Constants.TT_STATE.DEFERRED.equals(stateDTOOld.getItemCode())) {

        //Thoi gian da su dung tinh theo gio
        try {
          Date assignTime;
          if (StringUtils.isStringNullOrEmpty(troubleOld.getAssignTimeTemp())) {
            assignTime = troubleOld.getAssignTime();
          } else {
            assignTime = troubleOld.getAssignTimeTemp();
          }
          Double timeUsed = 0.00d;
          if (trouble.getTimeUsed() != null) {
            timeUsed = trouble.getTimeUsed();
          }
          trouble.setTimeUsed(
              timeUsed + (now.getTime() - assignTime.getTime()) / (60.00d * 60.00d * 1000));
          if (trouble.getInsertSource().contains(Constants.INSERT_SOURCE.BCCS)) {
            if (tForm.getDeferType() != null && 2 == (tForm.getDeferType())) {
              trouble.setNumPending(
                  trouble.getNumPending() == null ? 1 : (trouble.getNumPending() + 1));
            }
            if (trouble.getTroubleAssignId() != null) {
              TroubleAssignEntity troubleAssign = troubleAssignRepository
                  .findTroubleAssignById(trouble.getTroubleAssignId());
              timeUsed = 0.00d;
              if (troubleAssign.getTimeUsed() != null) {
                timeUsed = troubleAssign.getTimeUsed();
              }
              troubleAssign.setTimeUsed(
                  timeUsed + (now.getTime() - assignTime.getTime()) / (60.00d * 60.00d * 1000));
              troubleAssignRepository.updateTroubleAssign(troubleAssign);
            }
          } else {
            trouble
                .setNumPending(trouble.getNumPending() == null ? 1 : (trouble.getNumPending() + 1));
          }

          tForm.setStateWo("7");
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }
        //dong su co
      } else if ((Constants.TT_STATE.CLOSED.equals(stateDTO.getItemCode())
          || Constants.TT_STATE.CLOSED_Not_KEDB.equals(stateDTO.getItemCode()))) {
        if ("5".equals(tForm.getCheckbox()) || "4".equals(tForm.getCheckbox())) {
          if (trouble.getInsertSource().contains("NOC")) {
            trouble.setClearTime(trouble.getEndTroubleTime()); //tinh lai thoi gian con lai
          } else if (trouble.getClearTime() == null) {
            trouble.setClearTime(now);
          }
          trouble.setClearUserId(tForm.getProcessingUserId() == null ? null
              : Long.parseLong(tForm.getProcessingUserId()));
          trouble.setClearUserName(tForm.getProcessingUserName());
        }
        //dong lan dau
        if (!Constants.TT_STATE.CLOSED_Not_KEDB.equals(stateDTOOld.getItemCode())) {
          trouble.setClosedTime(now);
          //HaiNV20 bo sung truong hop neu ticket dong tu dong thi goi wo de dong tu dong start
          if (null != trouble.getAutoClose() && trouble.getAutoClose() == 1L && !"MOBILE"
              .equals(tForm.getSpmName())) { //Thuc hien dong wo doi voi cac ticket dong tu dong.
            List<String> lstTTCode_CloseTime = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String strTTCode_CloseTime = trouble.getTroubleCode() + ";" + dateFormat.format(now);
            ;
            lstTTCode_CloseTime.add(strTTCode_CloseTime);
            ResultDTO rs = woServiceProxy
                .closeWoProxy(new RequestApiWODTO(lstTTCode_CloseTime, "TT"));
            if (rs.getKey() != null && !rs.getKey().equals(Constants.RESULT.SUCCESS)) {
              throw new Exception("[INCIDENT] call close wo fail: " + rs.getMessage());
            }
          }
          //cap nhat dong
          if (Constants.INSERT_SOURCE.SPM.equals(trouble.getInsertSource())
              || trouble.getInsertSource().contains(Constants.INSERT_SOURCE.BCCS)) {
            tForm.setTimeUsed(troubleOld.getTimeUsed());
            tForm.setRemainTime(troubleOld.getRemainTime());
            //dinh kem day sang SPM
            isSendSPm = true;
            CfgServerNocDTO cfgServerNocDTO = new CfgServerNocDTO();
            cfgServerNocDTO.setInsertSource(tForm.getInsertSource());
            cfgServerNocDTO = troublesRepository.getServerDTO(cfgServerNocDTO);
            if (Constants.INSERT_SOURCE.SPM.equals(trouble.getInsertSource())) {
              troubleSpmUtils.updateSpmCompleteNotCheck(tForm, trouble);
            } else if (trouble.getInsertSource().contains(Constants.INSERT_SOURCE.BCCS)) {
              CatItemDTO catItemDTO = catItemBusiness.getCatItemById(trouble.getTypeId());
              tForm.setTypeName(catItemDTO.getItemCode());
              catItemDTO = catItemBusiness.getCatItemById(trouble.getState());
              tForm.setStateName(catItemDTO.getItemName());
              troubleBccsUtils.updateCompProcessing(messagesRepository, 2, tForm, cfgServerNocDTO);
            } else if (trouble.getInsertSource().contains(Constants.INSERT_SOURCE.SPM) && !trouble
                .getInsertSource().equals(Constants.INSERT_SOURCE.SPM_VTNET)) {
              troubleBccsUtils.updateComp(2, trouble, tForm.toModelOutSide(), cfgServerNocDTO);
            }
          }
          //cap nhat ban ghi cu da giao
          if (trouble.getTroubleAssignId() != null) {
            TroubleAssignEntity troubleAssign = troubleAssignRepository
                .findTroubleAssignById(trouble.getTroubleAssignId());
            troubleAssign.setContent(I18n.getLanguage("incident.complete"));
            troubleAssign.setCompleteTime(now);
            troubleAssignRepository.updateTroubleAssign(troubleAssign);
          }
        }
        //mo lai su co
      } else if (Constants.TT_STATE.OPEN.equals(stateDTO.getItemCode()) && Constants.TT_STATE.REJECT
          .equals(stateDTOOld.getItemCode())) {

        trouble.setRejectedCode(null);
        trouble.setRejectReason("");
        trouble.setAssignTime(null);
        trouble.setAssignTimeTemp(null);
        trouble.setTimeUsed(null);
        if (tForm.getCheckbox() != null && "2".equals(tForm.getCheckbox())) {
        } else {
          trouble.setQueueTime(null);
        }
      } else if (Constants.TT_STATE.REJECT.equals(stateDTO.getItemCode())
          && trouble.getRejectTime() == null) {
        trouble.setRejectTime(now);
        // De xuat tam dung
      } else if (TT_STATE.Wait_for_Deferred.equals(stateDTO.getItemCode())) {
          String content = "Ticket: " + tForm.getTroubleCode() + " " + I18n.getLanguage("trouble.approval");
          tForm.setSendSmsApproval(true);
          sendSms(tForm, content);
      }
    }
    //cap nhat dia ban
    if ("9".equals(tForm.getCheckbox())) {
      reloadTrouble(trouble);
      String workLog = DateUtil.date2ddMMyyyyHHMMss(now) + "/" + troubleOld.getReceiveUnitName()
          + "/" + troubleOld.getRemainTime();
      if (!StringUtils.isStringNullOrEmpty(tForm.getWorkArround())) {
        workLog = workLog + " / " + tForm.getWorkArround();
      }
      if (!StringUtils.isStringNullOrEmpty(tForm.getReasonName())) {
        workLog = workLog + " / " + tForm.getReasonName();
      }

      tForm.setWorkLog(workLog);
      tForm.setCreateUnitId(Long.parseLong(tForm.getProcessingUnitId()));
      tForm.setCreateUnitName(tForm.getProcessingUnitName());
      tForm.setCreateUserId(tForm.getCreateUserId());
      tForm.setCreateUserName(tForm.getProcessingUserName());

      onInsertWorkLog(tForm);
    }
    String locationNameFull = troublesRepository.getLocationNameFull(
        tForm.getLocationId() == null ? null : String.valueOf(tForm.getLocationId()));
    trouble.setLocation(locationNameFull);
    //cap nhat lai thoi gian xu ly
    if (Constants.INSERT_SOURCE.SPM.equals(trouble.getInsertSource())) {
    } else {
      if (trouble.getAlarmGroup() != null && trouble.getTypeId() != null
          && ((trouble.getPriorityId() != troubleOld.getPriorityId())
          || trouble.getTimeProcess() == null || trouble.getIsStationVip() != null)) {
        CfgTimeTroubleProcessDTO config = troublesRepository
            .getConfigTimeTroubleProcess(trouble.getTypeId(),
                Long.parseLong(trouble.getAlarmGroup()), trouble.getPriorityId(),
                trouble.getInsertSource());

        if (config != null) {
          if (trouble.getIsStationVip() != null && trouble.getIsStationVip().equals(1L)
              && config.getTimeStationVip() != null) {
            trouble.setTimeProcess(Double.valueOf(config.getTimeStationVip()));
          } else {
            trouble.setTimeProcess(config.getProcessTtTime() == null ? null
                : Double.valueOf(config.getProcessTtTime()));
          }
          trouble.setTimeCreateCfg(
              config.getCreateTtTime() == null ? null : Double.valueOf(config.getCreateTtTime()));
        }
      }
      //cap nhat extend time
      if (trouble.getReasonId() != null && (trouble.getIsStationVip() == null || trouble
          .getIsStationVip().equals(0L))) {

        List<ConditionBean> lstCondition = new ArrayList<>();
        lstCondition.add(
            new ConditionBean("typeId", trouble.getTypeId().toString(), "NAME_EQUAL", "NUMBER"));
        lstCondition.add(
            new ConditionBean("alarmGroupId", trouble.getAlarmGroup(), "NAME_EQUAL", "NUMBER"));
        lstCondition.add(
            new ConditionBean("priorityId", trouble.getPriorityId().toString(), "NAME_EQUAL",
                "NUMBER"));
        ConditionBean conditionBean = new ConditionBean("reasonId",
            trouble.getReasonId().toString(), "NAME_EQUAL",
            "NUMBER");
        lstCondition.add(conditionBean);
        if (trouble.getInsertSource().contains("NOC")) {
          lstCondition
              .add(new ConditionBean("country", trouble.getInsertSource(), "NAME_EQUAL", "STRING"));
        } else {
          lstCondition.add(new ConditionBean("country", "NOC", "NAME_EQUAL", "STRING"));
        }
        ConditionBeanUtil.sysToOwnListCondition(lstCondition);
        List<CfgTimeExtendTtDTO> cfgTimeExtendTtDTO = cfgTimeExtendTtRepository
            .getListCfgTimeExtendTtByCondition(lstCondition, 0, 1, "", "");
        if (cfgTimeExtendTtDTO != null && !cfgTimeExtendTtDTO.isEmpty()) { // theo nguyen nhan
          trouble.setTimeProcess(Double.valueOf(cfgTimeExtendTtDTO.get(0).getTimeExtend()));
        } else {
          //theo mang, muc uu tien, nhom canh bao
          for (ConditionBean conditionBean1 : lstCondition) {
            if (conditionBean.getValue().equalsIgnoreCase(conditionBean1.getValue())) {
              lstCondition.remove(conditionBean1);
              break;
            }
          }
          List<ConditionBean> conditionBean2 = new ArrayList<>();
          conditionBean2.add(new ConditionBean("reasonId", "-1", "NAME_EQUAL", "NUMBER"));
          ConditionBeanUtil.sysToOwnListCondition(conditionBean2);
          lstCondition.addAll(conditionBean2);
          cfgTimeExtendTtDTO = cfgTimeExtendTtRepository
              .getListCfgTimeExtendTtByCondition(lstCondition, 0, 1, "", "");
          if (cfgTimeExtendTtDTO != null && !cfgTimeExtendTtDTO.isEmpty()) {
            trouble.setTimeProcess(Double.valueOf(cfgTimeExtendTtDTO.get(0).getTimeExtend()));
          }
        }
      }
    }
    //tu choi nhan su co
    String unitMovename = "";
    if ("2".equals(tForm.getCheckbox())) {
      unitMovename = trouble.getUnitMoveName();
      trouble.setUnitMoveName(null);
      trouble.setUnitMove(null);
    }
    if (isMove != null && isMove.equals(2L)) {
      trouble.setRejectTime(null);
      trouble.setRejectReason(null);
      trouble.setRejectedCode(null);
    }
    trouble.setLastUpdateTime(now);
    Double offsetFromUser = 0D;
    if (SYSTEM.TT.toString().equals(systemCall)) {
      UserToken userToken = TicketProvider.getUserToken();
//    UsersEntity usersEntity = userBusiness.getUserByUserId(userToken.getUserID());
//    UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
//    if (StringUtils.isNotNullOrEmpty(String.valueOf(troubleOld.getReceiveUnitId())) && StringUtils
//        .isNotNullOrEmpty(String.valueOf(tForm.getReceiveUnitId()))) {
//      if (!troubleOld.getReceiveUnitId().equals(tForm.getReceiveUnitId())) {
//        trouble.setDateMove(now);
//        trouble.setIsMove(1L);
//        trouble.setUnitMove(unitToken.getUnitId());
//        trouble.setUnitMoveName(unitToken.getUnitName());
//      }
//    }
//    // THANG DT UPDATE
      if (userToken != null) {
        offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
        if (troubleOld != null && troubleOld.getUnitMove() == null
            && troubleOld.getReceiveUnitId() != null &&
            troubleOld.getReceiveUnitId().equals(userToken.getDeptId()) &&
            !troubleOld.getReceiveUnitId().equals(tForm.getReceiveUnitId())) {
          trouble.setDateMove(now);
          trouble.setIsMove(1L);
          trouble.setUnitMove(troubleOld.getReceiveUnitId());
          trouble.setUnitMoveName(troubleOld.getReceiveUnitName());
        } else if (troubleOld != null && troubleOld.getUnitMove() == null && !troubleOld
            .getReceiveUnitId().equals(userToken.getDeptId()) && !troubleOld.getReceiveUnitId()
            .equals(tForm.getReceiveUnitId())) {
          trouble.setIsMove(troubleOld.getIsMove() != null ? troubleOld.getIsMove() : null);
          trouble.setUnitMove(troubleOld.getUnitMove() != null ? troubleOld.getUnitMove() : null);
          trouble.setUnitMoveName(
              troubleOld.getUnitMoveName() != null ? troubleOld.getUnitMoveName() : null);
          trouble.setDateMove(troubleOld.getDateMove() != null ? troubleOld.getDateMove() : null);
        }
      }
    }

    troublesRepository.updateTroubles(trouble);
    List<String> lstFiles = DataUtil.splitListFileByComma(tForm.getAttachFileList());

    //TienNV Comment lai
    onInsertTroubleFile(tForm, lstFiles);
    //insert trouble node
    onInsertTroubleNode(tForm);

    result.setId(trouble.getTroubleId());
    result.setIdValue(trouble.getTroubleCode());
    result.setMessage(Constants.RESULT.SUCCESS);
    result.setKey(Constants.RESULT.SUCCESS);

    //ghi log cho truong hop giao su co
    String contentLog = troubleOld.compareContent(trouble);
    String statusName;
    if (trouble.getInsertSource() != null && trouble.getInsertSource()
        .contains(Constants.INSERT_SOURCE.BCCS)) {
      if (!Constants.TT_STATE.DEFERRED.equals(stateDTOOld.getItemCode())
          && Constants.TT_STATE.DEFERRED.equals(stateDTO.getItemCode())) {
        contentLog = I18n.getLanguage("incident.deffer");
      } else if (!Constants.TT_STATE.CLOSED_Not_KEDB.equals(stateDTOOld.getItemCode())
          && !Constants.TT_STATE.CLOSED.equals(stateDTOOld.getItemCode())
          && (Constants.TT_STATE.CLOSED_Not_KEDB.equals(stateDTO.getItemCode())
          || Constants.TT_STATE.CLOSED.equals(stateDTO.getItemCode()))) {
        contentLog = I18n.getLanguage("incident.complete");

      } else if (Constants.TT_STATE.DEFERRED.equals(stateDTOOld.getItemCode())
          && !Constants.TT_STATE.DEFERRED.equals(stateDTO.getItemCode())) {
        contentLog = I18n.getLanguage("incident.open.deffer");

      } else {
        contentLog = I18n.getLanguage("incident.update");
      }
      statusName = I18n.getLanguage("incident.update") + " " + contentLog;
      contentLog = contentLog + " / " + tForm.getWorkLog();

    } else {
      statusName = I18n.getLanguage("incident.update");
    }
    TroubleActionLogsEntity troubleActionLogsDTO = new TroubleActionLogsEntity(null,
        contentLog,
        new Date(),
        tForm.getProcessingUnitId() == null ? null : Long.valueOf(tForm.getProcessingUnitId()),
        tForm.getProcessingUserId() == null ? null : Long.valueOf(tForm.getProcessingUserId()),
        statusName, trouble.getTroubleId(),
        tForm.getProcessingUnitName(), tForm.getProcessingUserName(),
        tForm.getState(), tForm.getTroubleCode(), tForm.getStateName(), null, null, null);

    //dunglv3 add them Log vao one_tm.trouble_action_logs
    String response = compareObj(troubleOld, trouble.toDTO(), offsetFromUser);
    troubleActionLogsDTO.setInforMation(response);
    //tu choi nhan su co

    //tu choi nhan su co
    if ("2".equals(tForm.getCheckbox())) {
      troubleActionLogsDTO.setContent(unitMovename + " " + I18n.getLanguage("trouble.reject")
          + " " + trouble.getReceiveUnitName() + " " + DateTimeUtils.getSysDateTime()
      );
      //cap nhat ban ghi cu da giao
      if (trouble.getTroubleAssignId() != null) {
        TroubleAssignEntity troubleAssign = troubleAssignRepository
            .findTroubleAssignById(trouble.getTroubleAssignId());
        troubleAssign.setContent(I18n.getLanguage("incident.reject"));
        troubleAssign.setUpdateTime(now);
        troubleAssignRepository.updateTroubleAssign(troubleAssign);
      }
      tForm.setWorkLog(troubleActionLogsDTO.getContent());
      //giao su co cho don vi khac
    } else if ("3".equals(tForm.getCheckbox())) {
      troubleActionLogsDTO
          .setContent(trouble.getUnitMoveName() + " " + I18n.getLanguage("trouble.moveTicket")
              + " " + trouble.getReceiveUnitName() + " " + DateTimeUtils
              .convertDateTimeToString(trouble.getDateMove())
          );
      tForm.setWorkLog(troubleActionLogsDTO.getContent());
      //cap nhat ban ghi cu
      if (trouble.getTroubleAssignId() != null) {
        TroubleAssignEntity troubleAssign = troubleAssignRepository
            .findTroubleAssignById(trouble.getTroubleAssignId());
        troubleAssign.setContent(I18n.getLanguage("incident.reject"));
        troubleAssign.setUpdateTime(now);
        troubleAssignRepository.updateTroubleAssign(troubleAssign);

      }

      // dong WO cap nhat TT
    } else if ("4".equals(tForm.getCheckbox())) {
      troubleActionLogsDTO
          .setContent(I18n.getLanguage("incident.update") + " " + Constants.RESULT.SUCCESS + " "
              + I18n.getLanguage("wo.update.closed"));
    } else if ("5".equals(tForm.getCheckbox())) {
      troubleActionLogsDTO
          .setContent(I18n.getLanguage("incident.update") + " " + Constants.RESULT.SUCCESS + " "
              + I18n.getLanguage("tt.update.closed"));
    } else if ("6".equals(tForm.getCheckbox())) {
      troubleActionLogsDTO
          .setContent(I18n.getLanguage("incident.update") + " " + Constants.RESULT.SUCCESS + " "
              + I18n.getLanguage("wo.update.open"));
    } else if ("7".equals(tForm.getCheckbox())) {
      troubleActionLogsDTO
          .setContent(I18n.getLanguage("incident.update") + " " + Constants.RESULT.SUCCESS + " "
              + I18n.getLanguage("wo.update.delay"));
    } else if ("8".equals(tForm.getCheckbox())) {
      troubleActionLogsDTO
          .setContent(I18n.getLanguage("incident.update") + " " + Constants.RESULT.SUCCESS + " "
              + I18n.getLanguage("wo.update.tt"));
    } else if ("9".equals(tForm.getCheckbox())) {
      troubleActionLogsDTO
          .setContent(I18n.getLanguage("incident.update") + " " + Constants.RESULT.SUCCESS + " "
              + I18n.getLanguage("tt.update.unit") + " " + troubleOld.getReceiveUnitName() + " --> "
              + trouble.getReceiveUnitName());
      tForm.setWorkLog(troubleActionLogsDTO.getContent());
      if (trouble.getInsertSource().contains(Constants.INSERT_SOURCE.BCCS)) {
        CatItemDTO catItemDTO = catItemBusiness.getCatItemById(trouble.getTypeId());
        Map<String, String> mapProperty = troublesRepository.getConfigProperty();
        String timeDiscount = mapProperty.get("TT.TIME.DISCOUNT");
        if (catItemDTO.getItemCode().equals(Constants.TT_ARRAY.DD_MOBILE)) {
          timeDiscount = mapProperty.get("TT.TIME.DISCOUNT.DD");
        }
        Double timeProcess = trouble.getTimeProcess();
        if (!StringUtils.isStringNullOrEmpty(timeDiscount)) {
          timeProcess = Double.valueOf(timeDiscount) * trouble.getTimeProcess();
        }
        //tao ban ghi giao moi
        List<String> lstSequence = getSequenseTroubles("COMMON_FILE_SEQ", 1);
        Long troubleAssignId = Long.valueOf(lstSequence.get(0));
        TroubleAssignEntity assign = new TroubleAssignEntity(troubleAssignId,
            trouble.getTroubleId(),
            troubleOld.getReceiveUnitId(), troubleOld.getReceiveUnitName(),
            trouble.getReceiveUnitId(), trouble.getReceiveUnitName(),
            timeProcess, null, I18n.getLanguage("incident.assign"),
            troubleActionLogsDTO.getContent(),
            now, null, null
        );
        troubleAssignRepository.updateTroubleAssign(assign);
        trouble.setTroubleAssignId(troubleAssignId);
        troublesRepository.updateTroubles(trouble);
      }
    } else if ("TKTU_1".equalsIgnoreCase(tForm.getCheckbox())) {
      troubleActionLogsDTO
          .setContent(I18n.getLanguage("incident.update") + " " + Constants.RESULT.SUCCESS + " "
              + I18n.getLanguage("tktu.defer"));
    } else if ("TKTU_2".equalsIgnoreCase(tForm.getCheckbox())) {
      troubleActionLogsDTO
          .setContent(I18n.getLanguage("incident.update") + " " + Constants.RESULT.SUCCESS + " "
              + I18n.getLanguage("tktu.closed"));
    } else if ("TKTU_3".equalsIgnoreCase(tForm.getCheckbox())) {
      troubleActionLogsDTO
          .setContent(I18n.getLanguage("incident.update") + " " + Constants.RESULT.SUCCESS + " "
              + I18n.getLanguage("tktu.open.defer"));
    }
    if ("MOBILE".equals(tForm.getSpmName())) {
      troubleActionLogsDTO.setContent(
          troubleActionLogsDTO.getContent() + " \n " + (tForm.getWorkLog() == null ? ""
              : tForm.getWorkLog()));
    }
    troubleActionLogsDTO.setRootCause(trouble.getRootCause());
    troubleActionLogsDTO.setWorkArround(trouble.getWorkArround());

    //ThanhLv12 thuc hien bo sung log info cho cac WO tao tu SOC_VTNET_start
    if ("SPM_VTNET".equals(trouble.getInsertSource())
        || "SPM".equals(trouble.getInsertSource())) {
      troubleActionLogsDTO.setInsertSource(trouble.getInsertSource());
      ActionInfoDTO info = new ActionInfoDTO(tForm.toModelOutSide());
      tForm.setState(trouble.getState());
      troubleActionLogsDTO.setActionInfo(info.toString());
    }
    //ThanhLv12 thuc hien bo sung log info cho cac WO tao tu SOC_VTNET_end

    troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO);
    //HaiNV20 bo sung cap nhat action cho SPM start
    if (isSendSPm == false && trouble.getInsertSource() != null
        && (Constants.INSERT_SOURCE.SPM.equals(trouble.getInsertSource())
        || trouble.getInsertSource().contains(Constants.INSERT_SOURCE.BCCS))) {
      CatItemDTO stateItem = new CatItemDTO();
      stateItem.setItemCode(tForm.getStateName());
      List<CatItemDTO> lstTTState = catItemBusiness.getListCatItemDTO(stateItem);
      //dinh kem day sang SPM
      if (!"MOBILE".equals(tForm.getSpmName())) {
        List<String> lstName = new ArrayList<String>();
        List<byte[]> lstByte = new ArrayList<byte[]>();
        if (lstFiles != null && !lstFiles.isEmpty()) {
          for (String lstF : lstFiles) {
            String path = tForm.getPathFileAttach() + lstF.trim();
            File file = new File(path);
            byte[] bFile = org.apache.commons.io.FileUtils.readFileToByteArray(file);
            lstName.add(lstF.trim());
            lstByte.add(bFile);
          }
        }
        tForm.setArrFileName(lstName);
        tForm.setFileDocumentByteArray(lstByte);
      }
      if (Constants.INSERT_SOURCE.SPM.equals(trouble.getInsertSource())) {
        troubleSpmUtils.updateSpmAction(tForm, trouble, lstTTState.get(0).getItemName());
      } else if (trouble.getInsertSource().contains(Constants.INSERT_SOURCE.BCCS)) {
        CatItemDTO catItemDTO = catItemBusiness.getCatItemById(trouble.getTypeId());
        tForm.setTypeName(catItemDTO.getItemCode());
        catItemDTO = catItemBusiness.getCatItemById(trouble.getState());
        tForm.setStateName(catItemDTO.getItemName());
        CfgServerNocDTO cfgServerNocDTO = new CfgServerNocDTO();
        cfgServerNocDTO.setInsertSource(tForm.getInsertSource());
        cfgServerNocDTO = troublesRepository.getServerDTO(cfgServerNocDTO);
        troubleBccsUtils.updateCompProcessing(messagesRepository, 1, tForm, cfgServerNocDTO);
      }
    }
    if (TROUBLE.isTKTU.equals(String.valueOf(tForm.getIsSendTktu())) && !callFromTKTU) {
      troubleTktuUtils
          .updateTicketForConcavePoint(tForm.getTroubleCode(), tForm.getTroubleName(),
              tForm.getConcave(), String.valueOf(tForm.getLocationId()));

    }
    if ("SPM".equals(tForm.getInsertSource()) && tForm.getErrorCode() != null && 1 == (tForm
        .getErrorCode())
        && (Constants.TT_STATE.REJECT.equals(stateDTO.getItemCode()) && Constants.TT_STATE.REJECT
        .equals(stateDTOOld.getItemCode()))
        || (Constants.TT_STATE.CLEAR.equals(stateDTO.getItemCode()) && Constants.TT_STATE.CLEAR
        .equals(stateDTOOld.getItemCode()))) {
      troublesRepository.onUpdatebatchTrouble(tForm, stateDTO.getItemCode());
    }
    return result;
  }

  @Override
  public ResultInSideDto updateTroubleToSPM(TroublesInSideDTO troublesDTO, String type) {
    ResultInSideDto result = new ResultInSideDto();
    try {
      TroublesEntity trouble = troublesRepository
          .findTroublesById(Long.valueOf(troublesDTO.getTroubleId()));
      if ("1".equals(type)) { //hoan thanh
        troubleSpmUtils.updateSpmCompleteNotCheck(troublesDTO, trouble);
      } else { //cap nhat

        troubleSpmUtils.updateSpmAction(troublesDTO, trouble, troublesDTO.getStateName());
      }
      result.setKey(RESULT.SUCCESS);
      result.setMessage(Constants.RESULT.SUCCESS);
    } catch (Exception ex) {
      result.setKey(RESULT.ERROR);
      result.setMessage(ex.getMessage());
      log.error(ex.getMessage(), ex);
    }
    return result;
  }

  @Override
  public ResultInSideDto updateTrouble(TroublesInSideDTO tForm) {
    try {
      setTimeZoneForTrouble(tForm, -1L * TimezoneContextHolder.getOffsetDouble());
      troublesRepository.updateTroubles(tForm.toEntity());
      return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
  }

  @Override
  public ResultInSideDto sendChatListUsers(TroublesInSideDTO dtoTran) throws Exception {
    ResultInSideDto resultInSideDto;
    //Neu chua tao nhom chat, thi se hien popup chon nhom chat. lstUsers la list user lay duoc tu popup
    //bao gom ca: lstUserDefault: [userTokenGNOC.getUserId(), dtoTran.getCreateUserId(), dtoTran.getReceiveUserId()]
    try {
      UserToken userToken = ticketProvider.getUserToken();
      GroupBusiness business = new GroupBusiness();
      business.setGroupName(dtoTran.getTroubleCode());
      business.setUserCode(userToken.getUserName());

      business.setModuleCode("TT");
      business.setSubModules(I18n.getLanguage("monitor.system.TT"));
      business.setId(dtoTran.getTroubleCode());
      if (dtoTran.getIsChat() != null && 1 == (dtoTran.getIsChat())) {
        business.setMembers(userToken.getUserName());
        //WSChatPort chatPort = new WSChatPort();
        GroupResponse groupResponse = wsChatPort.createGroupInBusiness2(business);
        if (groupResponse != null && groupResponse.getResultCode() == 1) {
          resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS,
              I18n.getLanguage("common.chat.success"));
        } else if (groupResponse != null) {
          resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
              I18n.getLanguage("common.update.fail"));
        } else {
          resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
              I18n.getLanguage("common.update.fail"));
        }
      } else {
        if (dtoTran.getUsersInsideDtos() != null && !dtoTran.getUsersInsideDtos().isEmpty()) {
          String member = "";
          for (UsersInsideDto usersInsideDTO : dtoTran.getUsersInsideDtos()) {
            member = member + "," + usersInsideDTO.getUsername();
          }
          member = member.replaceFirst(",", "");
          business.setMembers(member);
          //WSChatPort chatPort = new WSChatPort();
          GroupResponse groupResponse = wsChatPort.createGroupInBusiness2(business);
          if (groupResponse != null && groupResponse.getResultCode() == 1) {
            TroublesEntity troublesEntity = troublesRepository
                .findTroublesById(dtoTran.getTroubleId());
            troublesEntity.setIsChat(1L);
            troublesRepository.updateTroubles(troublesEntity);
            resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS,
                I18n.getLanguage("common.chat.success"));
          } else if (groupResponse != null) {
            resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                I18n.getLanguage("common.update.fail"));
          } else {
            resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                I18n.getLanguage("common.update.fail"));
          }
        } else {
          resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
              I18n.getLanguage("common.update.fail"));
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new Exception(e);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto callIPCC(TroublesInSideDTO troublesDTO) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    UnitDTO unitDTO = new UnitDTO();
    unitDTO.setUnitId(troublesDTO.getReceiveUnitId());
    List<UnitDTO> units = unitRepository.getListUnitDTO(unitDTO, 0, 1, null, null);
    if (units != null && !units.isEmpty()) {
      unitDTO = units.get(0);
    }

    if (unitDTO != null && !StringUtils.isStringNullOrEmpty(unitDTO.getIpccServiceId())
        && !StringUtils.isStringNullOrEmpty(unitDTO.getMobile())) {
      IpccServiceDTO ipccServiceDTO = new IpccServiceDTO();
      ipccServiceDTO.setIpccServiceId(unitDTO.getIpccServiceId());
      List<IpccServiceDTO> lst = smsGatewayRepository.getListIpccServiceDTO(ipccServiceDTO);
      if (lst == null || lst.isEmpty()) {
        ipccServiceDTO = new IpccServiceDTO();
        ipccServiceDTO.setIsDefault(1L);
        lst = smsGatewayRepository.getListIpccServiceDTO(ipccServiceDTO);
      }
      if (lst != null && !lst.isEmpty()) {
        Map<String, String> mapConfigProperty = troublesRepository.getConfigProperty();
        String urlForIPCC = mapConfigProperty.get("url_ipcc_tt");
        troublesDTO.setProcessingUserName(userToken.getUserName());
        troublesDTO.setProcessingUnitName(unitToken.getUnitName());
        troublesDTO.setCreatedTimeFrom(null);
        IpccServiceDTO ipcc = lst.get(0);
        ipcc.setMobile("");
        ResultInSideDto resultInSideDTO = doCallIPCC(unitDTO, ipcc, troublesDTO, urlForIPCC);
        return resultInSideDTO;
      } else {
        resultInSideDto.setMessage(I18n.getLanguage("ipcc.not.exist"));
      }
    }
    resultInSideDto.setMessage(I18n.getLanguage("ipcc.config.server"));
    return resultInSideDto;
  }

  private void updateTroubleInfo(TroublesInSideDTO tForm, TroublesEntity trouble,
      CfgTimeTroubleProcessDTO config) throws Exception {
    //Set gia tri truong location
    log.debug("Request to updateTroubleInfo: {}", tForm);
    String locationNameFull = troublesRepository.getLocationNameFull(
        tForm.getLocationId() == null ? null : String.valueOf(tForm.getLocationId()));
    trouble.setLocation(locationNameFull);
    if (tForm.getStateName().equals(Constants.TT_STATE.Waiting_Receive) || tForm.getStateName()
        .equals(Constants.TT_STATE.QUEUE)) {
      trouble.setAssignTime(trouble.getCreatedTime());
      if (tForm.getStateName().equals(Constants.TT_STATE.QUEUE)) {
        trouble.setQueueTime(trouble.getCreatedTime());
      }
    }
    //Cap nhat thoi gian phai hoan thanh xu ly su co va thoi gian phai tao su co
    if (trouble.getInsertSource() != null && config != null
        && (Constants.INSERT_SOURCE.SPM_VTNET.equals(trouble.getInsertSource())
        || trouble.getInsertSource().startsWith(Constants.INSERT_SOURCE.NOC)
        || Constants.INSERT_SOURCE.SIRC.equals(trouble.getInsertSource())
        || Constants.INSERT_SOURCE.TT.equals(trouble.getInsertSource())
        || "VSMART".equals(trouble.getInsertSource()))
    ) {
      trouble.setTimeProcess(
          config.getProcessTtTime() == null ? null : config.getProcessTtTime());
      trouble.setTimeCreateCfg(
          config.getCreateTtTime() == null ? null : config.getCreateTtTime());
      trouble.setCloseTtTime(
          config.getCloseTtTime() == null ? null : config.getCloseTtTime());
    }
  }

  public String onInsertWorkLog(TroublesInSideDTO tForm) throws Exception {
    try {
      log.debug("Request to onInsertWorkLog: {}", tForm);
      if (!StringUtils.isStringNullOrEmpty(tForm.getWorkLog())) {
        TroubleWorklogEntity troubleLog = new TroubleWorklogEntity();
        troubleLog.setTroubleId(tForm.getTroubleId());
        troubleLog.setCreateUnitId(tForm.getCreateUnitId());
        troubleLog.setCreateUnitName(tForm.getCreateUnitName());
        troubleLog.setCreateUserId(tForm.getCreateUserId());
        troubleLog
            .setCreateUserName(tForm.getCreateUserName() == null ? "" : tForm.getCreateUserName());
        troubleLog.setWorklog(tForm.getWorkLog());
        troubleLog.setCreatedTime(tForm.getLastUpdateTime());
        troubleWorklogRepository.insertTroubleWorklog(troubleLog);
      }
      return RESULT.SUCCESS;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return ex.getMessage();
    }
  }

  public String onInsertTroubleFile(TroublesInSideDTO trouble, List<String> lstFileOld)
      throws Exception {
    try {
      log.debug("Request to onInsertTroubleFile: {}", trouble);
      Date now = new Date();
      if (trouble.getFileDocumentByteArray() != null
          && trouble.getFileDocumentByteArray().size() > 0) {
        Long createUserId;
        if (!StringUtils.isStringNullOrEmpty(trouble.getProcessingUserId())) {
          createUserId = Long.valueOf(trouble.getProcessingUserId());
        } else {
          createUserId = trouble.getCreateUserId() == null ? 256066L : trouble.getCreateUserId();
        }
        UsersEntity usersEntity = userBusiness.getUserByUserId(createUserId);
        UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
        List<GnocFileDto> gnocFileDtos = new ArrayList<>();
        for (int i = 0; i < trouble.getFileDocumentByteArray().size(); i++) {
          byte[] fileData = trouble.getFileDocumentByteArray().get(i);
          String fileName = trouble.getArrFileName().get(i);
          fileName = fileName
              .replaceAll("[~!@#$%^&*()+`=;,\\-\\s+\\[\\]{}']", "_")
              .replaceAll("(_)+", "_");
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, fileName, fileData, null);
          //Start save file old
          String fullPathOld = FileUtils.saveUploadFile(fileName, fileData, uploadFolder, null);
          CommonFileDTO commonFileDTO = new CommonFileDTO();
          commonFileDTO.setCreateTime(now);
          commonFileDTO.setCreateUserId(createUserId);
          commonFileDTO.setPath(File.separator + FileUtils.getFilePath(fullPathOld));
          commonFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
          ResultInSideDto resultFileDataOld = troublesRepository.insertCommonFile(commonFileDTO);
          TroubleFileEntity troubleFile = new TroubleFileEntity();
          troubleFile.setTroubleId(Long.valueOf(trouble.getTroubleId()));
          troubleFile.setFileId(resultFileDataOld.getId());
          troublesRepository.insertTroubleFile(troubleFile);
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(fileName);
          gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(createUserId);
          gnocFileDto.setCreateUserName(usersEntity.getUsername());
          gnocFileDto.setCreateTime(commonFileDTO.getCreateTime());
          gnocFileDto.setMappingId(resultFileDataOld.getId());
          gnocFileDtos.add(gnocFileDto);
        }
        gnocFileRepository
            .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.TROUBLES, trouble.getTroubleId(),
                gnocFileDtos);
      }
      return RESULT.SUCCESS;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public String onInsertTroubleNode(TroublesInSideDTO trouble) throws Exception {
    log.debug("Request to onInsertTroubleNode: {}", trouble);
    List<InfraDeviceDTO> lstDevice = trouble.getLstNode();
    if (lstDevice != null && !lstDevice.isEmpty()) {
      for (InfraDeviceDTO device : lstDevice) {
        TroubleNodeEntity troubleNode = new TroubleNodeEntity();
        if (device.getDeviceId() != null) {
          troubleNode.setDeviceId(Long.parseLong(device.getDeviceId()));
        }
        troubleNode.setTroubleId(trouble.getTroubleId());
        troubleNode.setDeviceCode(device.getDeviceCode());
        troubleNode.setDeviceName(device.getDeviceName());
        troubleNode.setIp(device.getIp());
        troubleNodeRepository.insertTroubleNode(troubleNode);
      }
    }
    return RESULT.SUCCESS;
  }

  public String sendSms(TroublesInSideDTO troubleDTO, String content) throws Exception {
    log.debug("Request to sendSms: {}", content);
    List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();
    String smsContent;
    if (!StringUtils.isStringNullOrEmpty(content)) {
      smsContent = content;
    } else {
      smsContent = "message.created.TT";
    }
    String smsUnit = troubleDTO.getReceiveUnitId() == null ? null
        : String.valueOf(troubleDTO.getReceiveUnitId());
    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getUnitApproval())) {
      if (troubleDTO.getSendSmsApproval()) {
        smsUnit = troubleDTO.getUnitApproval();
      }
    }
    List<ConditionBean> lstCondition = new ArrayList<>();
    lstCondition.add(
        new ConditionBean("unitId", smsUnit, Constants.NAME_EQUAL,
            Constants.NUMBER));
    lstCondition.add(new ConditionBean("isEnable", "1", Constants.NAME_EQUAL, Constants.NUMBER));
    List<UsersInsideDto> lstUser = userBusiness
        .getListUsersByCondition(lstCondition, 0, 100, "asc", "username");

    UnitDTO unit = new UnitDTO();
    unit.setUnitId(Long.parseLong(smsUnit));
    unit.setStatus(1l);
    String alias = getAlias(troubleDTO);
    List<UnitDTO> lstUnit = unitRepository.getListUnitDTO(unit, 0, 1, "asc", "unitName");

    if (lstUser != null && !lstUser.isEmpty()) {
      for (UsersInsideDto usersInsideDTO : lstUser) {
        if (usersInsideDTO != null && (usersInsideDTO.getIsNotReceiveMessage() == null || !"1"
            .equals(String.valueOf(usersInsideDTO.getIsNotReceiveMessage())))) {
          //lay time zone
          Double offset = 0D;
          try {
            offset = TimezoneContextHolder.getOffsetDouble();
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            try {
              offset = userBusiness.getOffsetFromUser(usersInsideDTO.getUserId());
            } catch (Exception ex) {
              log.error(ex.getMessage(), ex);
            }
          }

          Date updateTime = new Date(new Date().getTime() + (long) (offset * 60 * 60 * 1000));
          String timeUpdate = DateUtil.date2ddMMyyyyHHMMss(updateTime);

          MessagesDTO messagesDTO = new MessagesDTO();
          //get noi dung tin nhan theo language
          String sms = DataUtil
              .getLang("2".equals(usersInsideDTO.getUserLanguage()) ? new Locale("en", "US") : null,
                  smsContent);
          String troubleName = escapeXML(troubleDTO.getTroubleName());
          sms = sms.replaceAll("#troubleCode#", troubleDTO.getTroubleCode() != null ? troubleDTO.getTroubleCode() : "")
              .replaceAll("#troubleName#", troubleName)
              .replaceAll("#updateTime#", timeUpdate)
              .replaceAll("#createUnit#", troubleDTO.getCreateUnitName() != null ? troubleDTO.getCreateUnitName() : "")
              .replaceAll("#receiveUnit#", troubleDTO.getReceiveUnitName() != null ? troubleDTO.getReceiveUnitName() : "")
              .replaceAll("#currentUser#", troubleDTO.getCreateUnitName() != null ? troubleDTO.getCreateUnitName() : "")
              .replaceAll("#createUser#",
                  troubleDTO.getCreateUserName() == null ? troubleDTO.getCreateUnitName()
                      : troubleDTO.getCreateUserName())
              .replaceAll("#priority#", troubleDTO.getPriorityName() != null ? troubleDTO.getPriorityName() : "")
              .replaceAll("#stateName#", troubleDTO.getStateName() != null ? troubleDTO.getStateName() : "");
          messagesDTO.setContent(sms);

          messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
          messagesDTO.setReceiverId(String.valueOf(usersInsideDTO.getUserId()));
          messagesDTO.setReceiverUsername(usersInsideDTO.getUsername());
          messagesDTO.setReceiverPhone(usersInsideDTO.getMobile());
          if (lstUnit != null && !lstUnit.isEmpty()) {
            messagesDTO.setSmsGatewayId(lstUnit.get(0).getSmsGatewayId() == null ? null
                : String.valueOf(lstUnit.get(0).getSmsGatewayId()));
          }
          messagesDTO.setStatus("0");
          messagesDTO.setAlias(alias);
          lsMessagesDTOs.add(messagesDTO);
        }
      }
    }
    if (lsMessagesDTOs != null && !lsMessagesDTOs.isEmpty()) {
      //tuanpv_fix sms_gateway_id null
      for (int i = lsMessagesDTOs.size() - 1; i >= 0; i--) {
        if ("".equals(lsMessagesDTOs.get(i).getSmsGatewayId())
            || lsMessagesDTOs.get(i).getSmsGatewayId() == null) {
          lsMessagesDTOs.remove(i);
        }
      }
      messagesRepository.insertOrUpdateListMessagesCommon(lsMessagesDTOs);
    }

    return Constants.RESULT.SUCCESS;

  }

  private String getAlias(TroublesInSideDTO troublesDTO) throws Exception {
    String alias = "";
    if (troublesDTO != null && troublesDTO.getTypeId() != null
        && troublesDTO.getAlarmGroupId() != null) { //nhan tin tt
      CatItemDTO catItem = catItemBusiness.getCatItemById(troublesDTO.getTypeId());
      if (catItem != null) {
        String temp = "TT_" + catItem.getItemCode();
        catItem = catItemBusiness.getCatItemById(Long.parseLong(troublesDTO.getAlarmGroupId()));
        if (catItem != null) {
          temp = temp + "_" + catItem.getItemCode();
          CatItemDTO catItemDTO = new CatItemDTO();
          catItemDTO.setItemCode(temp);
          catItemDTO.setStatus(1l);
          List<CatItemDTO> lstCatItem = catItemBusiness.getListCatItemDTO(catItemDTO);
          if (lstCatItem != null && !lstCatItem.isEmpty()) {
            alias = lstCatItem.get(0).getItemValue();
          }
        }
      }
    }
    if (StringUtils.isStringNullOrEmpty(alias)) {
      alias = "GNOC_TT";
    }
    return alias;
  }

  /*tao dto insert TT*/
  private TroublesInSideDTO createDTOToInsert(TroublesInSideDTO dtoUpdate, UserToken userToken) {
    TroublesInSideDTO dto = dtoUpdate;
    try {
      Date now = new Date();
      Date currDate = now;

      if (userToken != null) {
        UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
        UnitDTO unitDTO = new UnitDTO();
        unitDTO.setUnitId(userToken.getDeptId());
        unitDTO.setStatus(1L);
        List<UnitDTO> lstUnit = unitRepository.getListUnitDTO(unitDTO, 0, 10, "asc", "unitName");
        if (lstUnit != null && lstUnit.size() > 0) {
          dto.setCreateUnitName(
              unitToken.getUnitName() + " (" + lstUnit.get(0).getUnitCode() + ")");
          dto.setProcessingUnitName(
              unitToken.getUnitName() + " (" + lstUnit.get(0).getUnitCode() + ")");
        } else {
          dto.setCreateUnitName(unitToken.getUnitName());
          dto.setProcessingUnitName(unitToken.getUnitName());
        }

        dto.setCreateUnitId(userToken.getDeptId());
        dto.setCreateUserId(userToken.getUserID());
        dto.setCreateUserName(userToken.getUserName());
        //HaiNV20 bo sung thong tin của người đang xử lý để cung cấp cho SPM
        dto.setProcessingUnitId(String.valueOf(userToken.getDeptId()));
        dto.setProcessingUserName(userToken.getUserName());
        dto.setProcessingUserPhone(userToken.getTelephone());
        //HaiNV20 end
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getWorkLog())) {
        String content = userToken != null ? userToken.getUserName() : "";
        content += " " + currDate + " (GMT +7) : " + dto.getWorkLog().trim();
        dto.setWorkLog(content);
      } else {
        dto.setWorkLog("");
      }

      CatItemDTO stateDTO = catItemBusiness.getCatItemById(Long.valueOf(dto.getState()));
      if (StringUtils.isStringNullOrEmpty(dto.getReceiveUserId()) || "-1"
          .equals(dto.getReceiveUserId())) {
        if (stateDTO != null && stateDTO.getItemCode().equals(Constants.TT_STATE.QUEUE)
            && userToken != null) {
          dto.setReceiveUserId(userToken.getUserID());
          dto.setReceiveUserName(userToken.getUserName());
        }
      }

      //Thoi gian cap nhat gan nhat
      dto.setLastUpdateTime(currDate);
      //ThanhLV12_R12038_end
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return dto;
  }

  /**
   * Tao DTO de cap nhat su co
   */
  private TroublesInSideDTO createDTO(TroublesInSideDTO dtoUpdateCurrent,
      TroublesInSideDTO troubleOld,
      UserToken userToken) {
    TroublesInSideDTO dto = dtoUpdateCurrent;
    Long curentReceiveUserId = dto.getReceiveUserId();
    String currentReceiveUserName = dto.getReceiveUserName();
    try {
      if (userToken != null) {
        //HaiNV20 bo sung thong tin của người đang xử lý để cung cấp cho SPM
        dto.setProcessingUnitId(String.valueOf(userToken.getDeptId()));
        dto.setProcessingUserName(userToken.getUserName());
        dto.setProcessingUserPhone(userToken.getTelephone());
        dto.setProcessingUserId(userToken.getUserID().toString());
        //HaiNV20 end
      }

      //don vi
      CatItemDTO stateDTO = catItemBusiness.getCatItemById(dto.getState());
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      dto.setProcessingUnitName(unitToken.getUnitName());
      //don vi bi chuyen viec tu choi nhan viec
      if (troubleOld != null && troubleOld.getIsMove() != null && 1L == (troubleOld.getIsMove())
          && stateDTO != null && stateDTO.getItemCode().equals(Constants.TT_STATE.REJECT)) {
        dto.setReceiveUnitId(dto.getUnitMove());
        UnitDTO unitDTO = unitRepository.findUnitById(dto.getUnitMove());
        dto.setReceiveUnitName(unitDTO.getUnitName() + " (" + unitDTO.getUnitCode() + ")");
        dto.setIsMove(2L);
        dto.setUnitMoveName(unitToken.getUnitName() + " (" + unitToken.getUnitCode() + ")");
        dto.setDateMove(null);
        dto.setState(5L);//trang thai da tiep nhan
        dto.setStateName(Constants.TT_STATE.QUEUE);
        dto.setCheckbox("2");
        dto.setReceiveUserId(null);
        dto.setReceiveUserName(null);
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getWorkLog())) {
        String content = userToken != null ? userToken.getUserName() : "";
        String currDate = DateTimeUtils.convertDateTimeStampToString(new Date());
        content += " " + currDate + " (GMT + 7): " + dto.getWorkLog().trim();
        dto.setWorkLog(content);
      } else {
        dto.setWorkLog("");
      }

      if (!StringUtils.isStringNullOrEmpty(dtoUpdateCurrent.getReceiveUnitId())) {
        if (dto.getCheckbox() == null || !"2".equals(dto.getCheckbox())) {
          Long receiveUnitId = dtoUpdateCurrent.getReceiveUnitId();

          //Ten don vi thuc hien
          //cap nhat lai don vi thuc hien
          if (!receiveUnitId.equals(troubleOld.getReceiveUnitId())
              && userToken.getDeptId().equals(troubleOld.getReceiveUnitId())) {
            dto.setIsMove(1L);
            dto.setUnitMove(userToken.getDeptId());
            dto.setUnitMoveName(unitToken.getUnitName() + " (" + unitToken.getUnitCode() + ")");
            dto.setDateMove(new Date());
            dto.setReceiveUserId(null);
            dto.setReceiveUserName(null);
            dto.setState(3L);
            dto.setStateName(Constants.TT_STATE.Waiting_Receive);
            dto.setCheckbox("3");
          }
          dto.setReceiveUnitId(receiveUnitId);
        }
      } else {
        dto.setReceiveUnitId(null);
      }

      //Nguoi thuc hien
      if (dto.getCheckbox() != null && ("2".equals(dto.getCheckbox()) || "3"
          .equals(dto.getCheckbox()))) {
        if (curentReceiveUserId != null && curentReceiveUserId != -1L) {
          dto.setReceiveUserId(curentReceiveUserId);
          dto.setReceiveUserName(currentReceiveUserName);
        } else {
          dto.setReceiveUserId(null);
          dto.setReceiveUserName(null);
        }
      } else {
        if (curentReceiveUserId != null && curentReceiveUserId != -1L) {
          dto.setReceiveUserId(curentReceiveUserId);
          dto.setReceiveUserName(currentReceiveUserName);
        } else {
          if (stateDTO != null && (stateDTO.getItemCode().equals(Constants.TT_STATE.QUEUE)
              || stateDTO.getItemCode().equals(Constants.TT_STATE.Waiting_Receive))
              && userToken != null) {
            dto.setReceiveUserId(userToken.getUserID());
            dto.setReceiveUserName(userToken.getUserName());
          }
        }
      }

      if (stateDTO != null && stateDTO.getItemCode().equals(Constants.TT_STATE.CLEAR)) {
        dto.setClearUserId(userToken.getUserID());
        dto.setClearUserName(userToken.getUserName());
      }

      if ("-1".equals(dtoUpdateCurrent.getAlarmGroupId())) {
        CatItemDTO alarmGroup = catItemBusiness
            .getCatItemById(Long.valueOf(dtoUpdateCurrent.getAlarmGroupId()));
        if (alarmGroup != null) {
          dto.setAlarmGroupId(String.valueOf(alarmGroup.getItemId()));
          dto.setAlarmGroupCode(alarmGroup.getItemCode());
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return dto;
  }

  public String validateDTO(TroublesDTO dialog, List<TroubleMopDTO> lstMop) {
    String result = "";
    try {
      int check = 0;
      if (dialog.getInsertSource().contains("NOC")) {

        if (StringUtils.isStringNullOrEmpty(dialog.getTblCurr())) {
          return "incident.tblCurrNull";
        } else if (dialog.getTblCurr().trim().length() > 100) {
          return "incident.tblCurr100";
        }

        if (StringUtils.isStringNullOrEmpty(dialog.getTblHis())) {
          return "incident.tblHisNull";
        } else if (dialog.getTblHis().trim().length() > 100) {
          return "incident.tblHis100";
        }
        if (StringUtils.isStringNullOrEmpty(dialog.getTblClear())) {
          return "incident.tblClearNull";
        } else if (dialog.getTblClear().trim().length() > 100) {
          return "incident.tblClear100";
        }

        if (StringUtils.isStringNullOrEmpty(dialog.getAlarmId())) {
          return "incident.alarmIdNull";
        } else {
          try {
            Long.valueOf(dialog.getAlarmId().trim());
          } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "incident.alarmIdInvalidFormat";
          }
        }

        if (StringUtils.isStringNullOrEmpty(dialog.getCatchingTime())) {
          dialog.setCatchingTime(dialog.getBeginTroubleTime());
          //return "incident.catchingTimeIdNull";
        } else {
          try {
            DateTimeUtils.convertStringToDate(dialog.getCatchingTime());
          } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "incident.catchingTimeInvalidFormat";
          }
        }

        List<ConditionBean> lstCondition = new ArrayList<>();
        lstCondition.add(new ConditionBean("alarmId", dialog.getAlarmId(), "NAME_EQUAL",
            "NUMBER"));
        lstCondition.add(new ConditionBean("tblCurr", dialog.getTblCurr(), "NAME_EQUAL", "STRING"));
        lstCondition.add(
            new ConditionBean("insertSource", dialog.getInsertSource(), "NAME_EQUAL", "STRING"));
        ConditionBeanUtil.sysToOwnListCondition(lstCondition);
        List<TroublesInSideDTO> lstTrouble = searchByConditionBean(lstCondition, 0,
            Integer.MAX_VALUE, "",
            "");
        if (lstTrouble != null && !lstTrouble.isEmpty()) {
          return "incident.alarmIdExisted";
        }

        //la ticket can nhieu
        if (!StringUtils.isStringNullOrEmpty(dialog.getWoType())) {
          dialog.setCountReopen("10");
          CatItemDTO prio = new CatItemDTO();
          prio.setCategoryCode("TROUBLE_INTERFERENCE");
          prio.setStatus(1l);
          List<CatItemDTO> lstCfg = catItemBusiness.getListCatItemDTO(prio);

          if (lstCfg != null && !lstCfg.isEmpty()) {
            for (CatItemDTO catItemDTO : lstCfg) {
              String[] itemCode = catItemDTO.getItemCode().split("###");
              if (itemCode != null && itemCode.length == 3
                  && itemCode[0].equals(dialog.getTypeId())
                  && itemCode[1].equals(dialog.getAlarmGroupCode())
                  && itemCode[2].equals(dialog.getSubCategoryId())) {
                check = 1;
                break;
              }

            }
          }
        }

        if (dialog.getAutoCreateWO() != null && dialog.getAutoCreateWO() == 1L) {
          if (dialog.getLstNode() == null || dialog.getLstNode().size() < 1) {
            return "LstNode is not null";
          }
        }

        if (dialog.getAutoCreateWO() != null && dialog.getAutoCreateWO() == 2L) {
          if (StringUtils.isStringNullOrEmpty(dialog.getCdId())) {
            return "CdId is not null";
          }
          dialog.setAutoCreateWO(1L);
        } else {
          dialog.setCdId(null);
        }
        if (lstMop != null && !lstMop.isEmpty()) {
          for (TroubleMopDTO mopDTO : lstMop) {
            if (StringUtils.isStringNullOrEmpty(mopDTO.getRule())) {
              return "rule" + " " + I18n.getValidation("wo.close.trouble.isRequire");
            }
            if (StringUtils.isStringNullOrEmpty(mopDTO.getRunType())) {
              mopDTO.setRunType("2");
              if (lstMop.size() > 1) {
                mopDTO.setRunType("2");
              }
            }
            if (StringUtils.isStringNullOrEmpty(mopDTO.getMopId())) {
              return "Mop id" + " " + I18n.getValidation("wo.close.trouble.isRequire");
            }
            if (StringUtils.isStringNullOrEmpty(mopDTO.getMopName())) {
              return "Mop name" + " " + I18n.getValidation("wo.close.trouble.isRequire");
            }
            if (StringUtils.isStringNullOrEmpty(mopDTO.getRunCycle())) {
              mopDTO.setRunCycle("1");
            }

            if (StringUtils.isStringNullOrEmpty(mopDTO.getSystem())) {
              return "system" + " " + I18n.getValidation("wo.close.trouble.isRequire");
            }

            if (StringUtils.isStringNullOrEmpty(mopDTO.getMaxNumberRun())) {
              mopDTO.setMaxNumberRun("1");
            }

            if (StringUtils.isStringNullOrEmpty(mopDTO.getDomain())) {
              return "Domain " + I18n.getValidation("wo.close.trouble.isRequire");
            }
          }
        }
      }

      if (StringUtils.isStringNullOrEmpty(dialog.getTroubleName())) {
        return "incident.troubleNameNull";
      } else if (dialog.getTroubleName().trim().length() > 500) {
        return "incident.troubleNameMax500";
      }
      //lay muc uu tien
      if (StringUtils.isStringNullOrEmpty(dialog.getPriorityId())) {
        return "incident.priorityNull";
      }
      if (dialog.getInsertSource().contains("SPM")) {
        dialog.setPriorityCode(dialog.getPriorityId());
        CatItemDTO prio = new CatItemDTO();
        prio.setCategoryCode("PRIRORITY_TT_CC");
        List<CatItemDTO> lstCatItem = catItemBusiness.getListCatItemDTO(prio);
        if (lstCatItem != null && !lstCatItem.isEmpty()) {
          for (CatItemDTO catItem : lstCatItem) {
            if (catItem.getItemCode().equalsIgnoreCase(dialog.getPriorityId())) {
              dialog.setPriorityId(catItem.getItemValue());
              break;
            }
          }
        }
        //nguy co
        if (!StringUtils.isStringNullOrEmpty(dialog.getRisk())) {
          prio = new CatItemDTO();
          prio.setItemCode(dialog.getRisk().trim().toLowerCase());
          prio.setCategoryCode("TT_RISK");
          lstCatItem = catItemBusiness.getListCatItemDTO(prio);
          if (lstCatItem != null && !lstCatItem.isEmpty()) {
            dialog.setRisk(String.valueOf(lstCatItem.get(0).getItemId()));
            dialog.setRiskName(lstCatItem.get(0).getItemCode());
          } else {
            dialog.setRisk(null);
            dialog.setRiskName(null);
          }
        }

      }

      CatItemDTO prio = new CatItemDTO();
      prio.setItemCode(dialog.getPriorityId().trim().toLowerCase());
      prio.setStatus(1L);
      List<CatItemDTO> lstCat = catItemBusiness.getListCatItemDTO(prio);
      if (lstCat == null || lstCat.isEmpty()
          || (lstCat != null && !lstCat.get(0).getCategoryCode()
          .equals(TROUBLE.PRIORITY))) {
        return I18n.getValidation("incident.priorityNotExist");
      } else {
        dialog.setPriorityId(String.valueOf(lstCat.get(0).getItemId()));
        dialog.setPriorityName(lstCat.get(0).getItemName());
      }

      if (StringUtils.isStringNullOrEmpty(dialog.getBeginTroubleTime())) {
        return "incident.beginTroubleTimeNull";
      } else {
        try {
          Date now = new Date();
          DateTimeUtils.convertStringToDateTime(dialog.getBeginTroubleTime());
          Date beginTroubleTime = DateTimeUtils
              .convertStringToDateTime(dialog.getBeginTroubleTime());
          try {
            if (StringUtils.isNotNullOrEmpty(dialog.getEndTroubleTime())) {
              Date endTroubleTime = DateTimeUtils
                  .convertStringToDateTime(dialog.getEndTroubleTime());
              if (beginTroubleTime.compareTo(endTroubleTime) > 0) {
                return "incident.beginTroubleTimeMustBeSmallerThanEndTroubleTime";
              }
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "incident.endTroubleTimeInvalidFormat.ddMMyyyyHHmmss";
          }
          String msgValid = DataUtil.validateDateTimeDdMmYyyy_HhMmSs(dialog.getBeginTroubleTime());
          if (msgValid != null && msgValid.length() > 0) {
            return "incident.beginTroubleTimeInvalid";
          }
          if (beginTroubleTime.compareTo(now) > 0) {
            return "incident.beginTroubleTimeMustBeSmallerThanNow";
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          return "incident.beginTroubleTimeInvalidFormat.ddMMyyyyHHmmss";
        }
      }

      CatItemDTO catItem = new CatItemDTO();
      catItem.setItemCode(Constants.TT_STATE.Waiting_Receive);
      List<CatItemDTO> lst = catItemBusiness.getListCatItemDTO(catItem);
      if (lst == null || lst.isEmpty()) {
        return "incident.haveSomeError";
      } else {
        dialog.setState(String.valueOf(lst.get(0).getItemId()));
        dialog.setStateName(lst.get(0).getItemCode());
      }

      if (StringUtils.isStringNullOrEmpty(dialog.getAffectedService())) {
        dialog.setImpactId(Constants.TT_IMPACT.NO);
      } else {
        CatItemDTO serviceDTO = new CatItemDTO();
        serviceDTO.setCategoryCode("PT_AFFECT_SERVICE");
        serviceDTO.setStatus(1L);
        List<CatItemDTO> lstService = catItemBusiness.getListCatItemDTO(serviceDTO);
        Map<String, CatItemDTO> mapService = new HashMap<String, CatItemDTO>();
        for (CatItemDTO sDTO : lstService) {
          mapService.put(sDTO.getItemCode().toUpperCase(), sDTO);
        }
        String[] arrService = dialog.getAffectedService().replace(", ", ",").split(",");
        if (arrService.length == 0) {
          return "incident.serviceAffectedIsNotNull";
        }
        for (String arra : arrService) {
          if (mapService.get(arra.toUpperCase()) == null) {
            return "incident.serviceAffectedNotExist";
          }
        }
        dialog.setImpactId(Constants.TT_IMPACT.YES);
      }

      if (StringUtils.isStringNullOrEmpty(dialog.getDescription())) {
        return "incident.descriptionNull";
      } else if (dialog.getDescription().trim().length() > 4000) {

        return "incident.descriptionMax4000";
      }

      if (StringUtils.isStringNullOrEmpty(dialog.getReceiveUnitId())) {
        if (dialog.getInsertSource().contains("SPM")) {
          if (dialog.getAutoCreateWO() != null && (dialog.getAutoCreateWO()
              .equals(1L))) { //tao WO thi ko bat buoc
          } else {
            return "incident.receiveUnitCodeNull";
          }
        }

      }
      if (!StringUtils.isStringNullOrEmpty(dialog.getReceiveUnitId())) {
        UnitDTO unitDTO = new UnitDTO();
        unitDTO.setUnitCode(dialog.getReceiveUnitId());
        unitDTO.setStatus(1L);
        List<UnitDTO> u = unitRepository.getListUnitDTO(unitDTO, 0, 10, "asc", "unitName");
        if (u == null || u.isEmpty()) {
          return "incident.receiveUnitNotExist";
        }
        //Set lai gia tri ReceiveUnitId = gia tri unitId
        dialog.setReceiveUnitId(String.valueOf(u.get(0).getUnitId()));
        dialog.setReceiveUnitName(u.get(0).getUnitName() + " (" + u.get(0).getUnitCode() + ")");
      }

      if (!StringUtils.isStringNullOrEmpty(dialog.getCreateUserName())) {

        try {
          UsersInsideDto u = userBusiness.getUserDTOByUserName(dialog.getCreateUserName().trim());
          if (u == null) {
            return "incident.createUserNotExist";
          } else {
            UnitDTO unitDTO = unitBusiness.findUnitById(u.getUnitId());
            if ("NOC".equals(dialog.getInsertSource())) {
              if (!StringUtils.isStringNullOrEmpty(dialog.getCreateUnitId())
                  &&
                  !String.valueOf(unitDTO.getUnitCode()).equals(dialog.getCreateUnitId())
              ) {
                return "incident.createUserNotInCreateUnit";
              }
            } else {
              if (!StringUtils.isStringNullOrEmpty(dialog.getCreateUnitId())
                  &&
                  !String.valueOf(u.getUnitId()).equals(dialog.getCreateUnitId())
              ) {
                return "incident.createUserNotInCreateUnit";
              }
            }
            dialog.setCreateUserId(String.valueOf(u.getUserId()));
            dialog.setCreateUnitId(String.valueOf(u.getUnitId()));
            if (unitDTO != null) {
              dialog.setCreateUnitName(u.getUnitName() + " (" + unitDTO.getUnitCode() + ")");
            } else {
              dialog.setCreateUnitName(u.getUnitName());
            }
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          return "incident.createUserInvalid";
        }
      } else if (!StringUtils.isStringNullOrEmpty(dialog.getCreateUnitId())) {
        UnitDTO createUnit = new UnitDTO();
        createUnit.setUnitCode(dialog.getCreateUnitId());
        createUnit.setStatus(1L);

        List<UnitDTO> rUnit = unitRepository.getListUnitDTO(createUnit, 0, 1000, null, null);
        if (rUnit == null || rUnit.isEmpty()) {
          createUnit = new UnitDTO();
          createUnit.setUnitId(Long.valueOf(dialog.getReceiveUnitId()));
          rUnit = unitRepository.getListUnitDTO(createUnit, 0, 1000, null, null);
          if (rUnit == null || rUnit.isEmpty()) {
            return "incident.createUnitNotExist";
          }
        }
        dialog.setCreateUnitId(String.valueOf(rUnit.get(0).getUnitId()));
        dialog.setCreateUnitName(
            rUnit.get(0).getUnitName() + " (" + rUnit.get(0).getUnitCode() + ")");
      }
      if (StringUtils.isStringNullOrEmpty(dialog.getCreateUnitId())) {
        if (dialog.getInsertSource().contains("SPM")) { //them moi sau GNOC2
          if (dialog.getAutoCreateWO() != null && (dialog.getAutoCreateWO()
              == 1L)) { //tao WO thi ko bat buoc
          } else {
            return "incident.createUnitCodeNull";
          }
        }
      }

      if (StringUtils.isStringNullOrEmpty(dialog.getTypeId())) {
        return "incident.typeNull";
      } else {
        CatItemDTO typeDTO = new CatItemDTO();
        typeDTO.setItemCode(dialog.getTypeId());
        typeDTO.setStatus(1L);
        List<CatItemDTO> lstType = catItemBusiness.getListCatItemDTO(typeDTO);
        if (lstType == null || lstType.isEmpty()) {

          return "incident.typeNotExist";
        } else {
          if (!lstType.get(0).getCategoryCode().equals(TROUBLE.PT_TYPE)) {
            return "incident.notIstype";
          }
          dialog.setTypeId(String.valueOf(lstType.get(0).getItemId()));
          dialog.setTypeName(lstType.get(0).getItemCode());
        }
      }

      if (StringUtils.isStringNullOrEmpty(dialog.getSubCategoryId())) {
        if (dialog.getInsertSource().contains("SPM")) {
          if (dialog.getAutoCreateWO() != null && (dialog.getAutoCreateWO()
              == 1L)) { //tao WO thi ko bat buoc
          } else {
            return "incident.subCategoryNull";
          }
        } else {
          return "incident.subCategoryNull";
        }
      }
      if (!StringUtils.isStringNullOrEmpty(dialog.getSubCategoryId())) {
        CatItemDTO typeDTO = new CatItemDTO();
        typeDTO.setItemCode(dialog.getSubCategoryId());
        typeDTO.setStatus(1L);
        List<CatItemDTO> lstType = catItemBusiness.getListCatItemDTO(typeDTO);
        if (lstType == null || lstType.isEmpty()) {
          return "incident.notIsSubCate";
        } else {
          CatItemDTO subCate = lstType.get(0);
          if (subCate.getParentItemId() == null || !String.valueOf(subCate.getParentItemId())
              .equals(dialog.getTypeId())) {
            return "incident.notIsSubCate";
          }
          dialog.setSubCategoryId(String.valueOf(subCate.getItemId()));
        }

      }

      if (!StringUtils.isStringNullOrEmpty(dialog.getVendorId())) {
        CatItemDTO vendorDTO = new CatItemDTO();
        vendorDTO.setItemCode(dialog.getVendorId());
        vendorDTO.setStatus(1L);
        List<CatItemDTO> lstVendor = catItemBusiness.getListCatItemDTO(vendorDTO);
        if (lstVendor != null && !lstVendor.isEmpty()) {
          dialog.setVendorId(String.valueOf(lstVendor.get(0).getItemId()));
        } else {
          return "incident.vendorNotExist";
        }
      }

      if (StringUtils.isStringNullOrEmpty(dialog.getLocationId())) {
        return "incident.locationCodeNull";
      } else {
        try {
          CatLocationDTO location = catLocationBusiness.getLocationByCode(dialog.getLocationId(),
              null, dialog.getNationCode());
          if (location == null) {
            return "incident.locationCodeNotExist";
          }
          if ("SPM_RADIO".equals(dialog.getInsertSource()) && !"5"
              .equals(location.getLocationLevel())) {
            return "incident.locationIsCommune";
          }
          dialog.setLocationCode(dialog.getLocationId());
          dialog.setLocationId(location.getLocationId());
          dialog.setLocation(location.getLocationNameFull());

        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          return "incident.locationInvalid";
        }
      }
      // ThanhLv12_start  -- nếu khác NOC và tạo WO tự động thì bắt buộc nhập thiết bị
      if (dialog.getAutoCreateWO() != null && dialog.getAutoCreateWO().equals(1L)) {
        if (!dialog.getInsertSource().contains("NOC")
            && !dialog.getInsertSource().contains("SPM")) {
          if (dialog.getLstNode() != null && !dialog.getLstNode().isEmpty()) {
          } else {
            return "incident.ListNodeIsNotNull";
          }
        }
        if (dialog.getInsertSource().contains("SPM") && StringUtils
            .isStringNullOrEmpty(dialog.getTelServiceId())) {
          return "incident.TelServiceIdIsNotNull";
        }
        if (dialog.getInsertSource().contains("SPM") && StringUtils
            .isStringNullOrEmpty(dialog.getIsCcResult())) {
          return "incident.IsCcResultIsNotNull";
        }
      }
      if (StringUtils.isStringNullOrEmpty(dialog.getAlarmGroupCode())) {
        if (dialog.getInsertSource().contains("SPM")) {
          if (dialog.getAutoCreateWO() != null && (dialog.getAutoCreateWO()
              .equals(1L))) { //tao WO thi ko bat buoc
          } else {
            return "incident.AlarmGroupCodeIsNotNull";
          }
        } else {
          return "incident.AlarmGroupCodeIsNotNull";
        }

      }
      // bắt buộc nhập loại dịch vụ
      if (dialog.getInsertSource().contains("SPM") && dialog.getServiceType() == null) {
        return "incident.serviceTypeIsNotNull";
      }
      if ((dialog.getInsertSource().contains("SPM"))
          && StringUtils.isStringNullOrEmpty(dialog.getSpmCode())) {
        return "incident.spmCodeIsNotNull";
      }

      if ((dialog.getInsertSource().contains("SPM"))
          && dialog.getSpmId() == null) {
        return "incident.spmIdIsNotNull";
      }
      if ((dialog.getInsertSource().contains("SPM"))
          && dialog.getComplaintGroupId() == null) {
        return "incident.ComplaintGroupIsNotNull";
      }
      if (!StringUtils.isStringNullOrEmpty(dialog.getComplaintTypeId())) {
        String kedbCode = troublesRepository.getKedbByComplaint(dialog.getComplaintTypeId());
        dialog.setRelatedKedb(kedbCode);
      }
      if ((dialog.getInsertSource().contains("SPM"))
          && dialog.getTimeProcess() == null) {
        if (!Constants.INSERT_SOURCE.SPM_VTNET.equalsIgnoreCase(dialog.getInsertSource())) {
          return "incident.timeProcessIsNotNull";
        }
      }

      CatItemDTO alarmGroupDTO = new CatItemDTO();
      //incident  only lay thong tin tu SPM
      if (StringUtils.isNotNullOrEmpty(dialog.getInsertSource()) && dialog.getInsertSource()
          .contains("SPM")) { // lay don vi
        //check spm
        List<ConditionBean> lstCondition = new ArrayList<>();
        lstCondition
            .add(new ConditionBean("spmCode", dialog.getSpmCode().trim(), "NAME_EQUAL", "STRING"));
        lstCondition.add(
            new ConditionBean("insertSource", dialog.getInsertSource(), "NAME_EQUAL", "STRING"));
        ConditionBeanUtil.sysToOwnListCondition(lstCondition);
        List<TroublesInSideDTO> lstTrouble = searchByConditionBean(lstCondition, 0,
            Integer.MAX_VALUE, "",
            "");
        if (lstTrouble != null && !lstTrouble.isEmpty()) {
          return I18n.getValidation("incident.spmIdExisted");
        }
        //duongnt start
        if (!Constants.INSERT_SOURCE.SPM_VTNET.equalsIgnoreCase(dialog.getInsertSource())) {
          if (StringUtils.isStringNullOrEmpty(dialog.getEndTroubleTime())) {
            return "incident.endTroubleTimeNull";
          } else {
            try {
              DateTimeUtils.convertStringToDateTime(dialog.getEndTroubleTime());
            } catch (Exception ex) {
              log.error(ex.getMessage(), ex);
              return "incident.endTroubleTimeInvalidFormat.ddMMyyyyHHmmss";
            }
          }
        }

        if ("SPM".equalsIgnoreCase(dialog.getInsertSource())) {
          //check trung voi spm_vtnet
          TroublesDTO troublesDTO = troublesRepository
              .checkAccountSPM(dialog.getListAccount(), null);
          if (troublesDTO != null && !StringUtils.isStringNullOrEmpty(troublesDTO.getTroubleId())) {
            //update ticket
            if (troublesDTO.getRemainTime() != null
                && Double.parseDouble(troublesDTO.getRemainTime()) < Double
                .parseDouble(dialog.getTimeProcess())) {
              dialog.setTimeProcess(null);
            }
            dialog.setTroubleCode(troublesDTO.getTroubleCode());
            dialog.setTroubleId(troublesDTO.getTroubleId());
            ResultInSideDto resultTemp = updateTroubleSpmVTNET(dialog);
            if (resultTemp.getKey().equals(RESULT.SUCCESS)) {
              return troublesDTO.getInsertSource() + "_ACCOUNT_EXIST: " + troublesDTO
                  .getTroubleCode() + "," + troublesDTO.getWoCode();
            } else {
              try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
              } catch (Exception e) {
                log.error(e.getMessage(), e);
              }
              return resultTemp.getMessage();
            }

          }
        } else if (Constants.INSERT_SOURCE.SPM_VTNET
            .equalsIgnoreCase(dialog.getInsertSource())) {// Constants.INSERT_SOURCE.SPM_VTNET
          TroublesDTO troublesDTO = troublesRepository
              .checkAccountSPM(dialog.getListAccount(), null);//hanv15 20200130
          if (troublesDTO != null && !StringUtils.isStringNullOrEmpty(troublesDTO.getTroubleId())) {
            return troublesDTO.getInsertSource() + "_ACCOUNT_EXIST: " + troublesDTO.getTroubleCode()
                + "," + troublesDTO.getWoCode();// truong hop tao tt da ton tai
          }
        }
        //duongnt end
        //lay don vi
        CfgUnitTtSpmDTO ttSpmDTO = troublesRepository
            .getUnitByLocation(dialog.getLocationId(), dialog.getTypeId(), null);
        if (ttSpmDTO != null && ttSpmDTO.getUnitId() != null) {
          dialog.setReceiveUnitName(ttSpmDTO.getUnitName());
          dialog.setCreateUnitName(ttSpmDTO.getUnitName());
          dialog.setReceiveUnitId(ttSpmDTO.getUnitId());
          dialog.setCreateUnitId(ttSpmDTO.getUnitId());

        } else {
          return I18n.getValidation("unit.location");
        }
        //lay tt co ban
        CfgInfoTtSpmDTO cfgInfoTtSpmDTO = new CfgInfoTtSpmDTO();
        cfgInfoTtSpmDTO.setTypeId(dialog.getTypeId());
        List<CfgInfoTtSpmDTO> infoTtSpmDTO = cfgInfoTtSpmBusiness
            .getListCfgInfoTtSpmDTO(cfgInfoTtSpmDTO, 0, 1, "", "");
        if (infoTtSpmDTO != null && !infoTtSpmDTO.isEmpty()) {
          dialog.setAlarmGroupId(infoTtSpmDTO.get(0).getAlarmGroupId());
          dialog.setSubCategoryId(infoTtSpmDTO.get(0).getSubCategoryId());
          dialog.setTroubleName(
              infoTtSpmDTO.get(0).getTroubleName().trim() + " " + dialog.getTroubleName().trim());

          alarmGroupDTO.setItemId(Long.valueOf(infoTtSpmDTO.get(0).getAlarmGroupId()));
        } else {
          return I18n.getValidation("cfg.info.tt");
        }

      } else {
        alarmGroupDTO.setItemCode(dialog.getAlarmGroupCode());
      }

      List<CatItemDTO> lstAlarmGroup = catItemBusiness.getListCatItemDTO(alarmGroupDTO);
      if (lstAlarmGroup != null && !lstAlarmGroup.isEmpty()) {
        dialog.setAlarmGroupId(String.valueOf(lstAlarmGroup.get(0).getItemId()));
        dialog.setAlarmGroupCode(lstAlarmGroup.get(0).getItemValue());
        dialog.setStrAlarmGroupDescription(lstAlarmGroup.get(0).getDescription());
        //HaiNV20 End
      } else {
        return "incident.alarmGroupCodeNotExist";
      }

      if (dialog.getLstNode() != null && !dialog.getLstNode().isEmpty()) {
        String strTmp = "";
        for (InfraDeviceDTO dto : dialog.getLstNode()) {
          strTmp += dto.getDeviceCode() + ",";
        }
        if (strTmp.length() > 0) {
          dialog.setAffectedNode(strTmp.substring(0, strTmp.length() - 1));
        }
      }
      if (check == 1) { //kiem tr su co can nhieu da tao chua
        List<ConditionBean> lstCondition = new ArrayList<>();
        lstCondition.add(new ConditionBean("typeId", dialog.getTypeId(), "NAME_EQUAL",
            "NUMBER"));
        lstCondition.add(new ConditionBean("subCategoryId", dialog.getSubCategoryId(),
            "NAME_EQUAL", "NUMBER"));
        lstCondition
            .add(new ConditionBean("alarmGroup", dialog.getAlarmGroupId(), "NAME_EQUAL", "STRING"));
        lstCondition.add(new ConditionBean("tblCurr", dialog.getTblCurr(), "NAME_EQUAL", "STRING"));
        lstCondition.add(
            new ConditionBean("insertSource", dialog.getInsertSource(), "NAME_EQUAL", "STRING"));
        lstCondition.add(new ConditionBean("state", "11", "NAME_NOT_EQUAL", "NUMBER"));
        lstCondition.add(new ConditionBean("state", "10", "NAME_NOT_EQUAL", "NUMBER"));
        lstCondition.add(new ConditionBean("countReopen", "10", "NAME_EQUAL", "NUMBER"));

        ConditionBeanUtil.sysToOwnListCondition(lstCondition);
        List<TroublesInSideDTO> lstTrouble = searchByConditionBean(lstCondition, 0,
            Integer.MAX_VALUE, "",
            "");
        if (lstTrouble != null && !lstTrouble.isEmpty()) {
          return I18n.getValidation("incident.reference");
        }
      }

      String currDate = DateTimeUtils.getSysDateTime();
      dialog.setCreatedTime(currDate);
      dialog.setLastUpdateTime(currDate);

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return ex.getMessage();
    }
    return result;
  }

  public static String escapeXML(String in) {

    if (in == null || ("".equals(in))) {
      return in; // vacancy test.
    }
    in = in.replaceAll("[^A-Za-z0-9]+", "-");

    return in;
  }

  private void updateTroubleByForm(TroublesEntity trouble, TroublesInSideDTO tForm,
      TroublesInSideDTO troubleOld)
      throws Exception {
    try {
      trouble.setAffectedNode(tForm.getAffectedNode());
      trouble.setPriorityId(tForm.getPriorityId());
      trouble.setState(tForm.getState());
      trouble.setImpactId(tForm.getImpactId());
      trouble.setLastUpdateTime(tForm.getLastUpdateTime());
      trouble.setDescription(tForm.getDescription());
      trouble.setTroubleName(tForm.getTroubleName());
      trouble.setReceiveUserId(tForm.getReceiveUserId());
      trouble.setReceiveUserName(tForm.getReceiveUserName());
      trouble.setAffectedService(tForm.getAffectedService());
      trouble.setBeginTroubleTime(tForm.getBeginTroubleTime());
      trouble.setEndTroubleTime(tForm.getEndTroubleTime());
      trouble.setTypeId(tForm.getTypeId());
      trouble.setReceiveUnitId(tForm.getReceiveUnitId());
      trouble.setReceiveUnitName(tForm.getReceiveUnitName());
      trouble.setSubCategoryId(tForm.getSubCategoryId());
      trouble.setLocationId(tForm.getLocationId());
      trouble.setLocation(tForm.getLocation());
      trouble.setRisk(tForm.getRisk());
      trouble.setReasonId(tForm.getReasonId());
      trouble.setReasonName(tForm.getReasonName());
      trouble.setVendorId(tForm.getVendorId());
      trouble.setAffectedNode(tForm.getAffectedNode());
      trouble.setCloseCode(tForm.getCloseCode());
      trouble.setRootCause(tForm.getRootCause());
      trouble.setRejectedCode(tForm.getRejectedCode());
      trouble.setDeferredTime(tForm.getDeferredTime());
      trouble.setDeferredReason(tForm.getDeferredReason());
      trouble.setSupportUnitId(tForm.getSupportUnitId());
      trouble.setSupportUnitName(tForm.getSupportUnitName());
      trouble.setRejectReason(tForm.getRejectReason());
      trouble.setRelatedKedb(tForm.getRelatedKedb());
      trouble.setSolutionType(tForm.getSolutionType());
      trouble.setWorkArround(tForm.getWorkArround());
      trouble.setNetworkLevel(tForm.getNetworkLevel());
      trouble.setLineCutCode(tForm.getLineCutCode());
      trouble.setCodeSnippetOff(tForm.getCodeSnippetOff());
      trouble.setCableType(tForm.getCableType());
      trouble.setClosuresReplace(tForm.getClosuresReplace());
      trouble.setWhereWrong(tForm.getWhereWrong());
      trouble.setAsessmentData(tForm.getAsessmentData());
      trouble.setReasonLv1Id(tForm.getReasonLv1Id());
      trouble.setReasonLv1Name(tForm.getReasonLv1Name());
      trouble.setReasonLv2Id(tForm.getReasonLv2Id());
      trouble.setReasonLv2Name(tForm.getReasonLv2Name());
      trouble.setReasonLv3Id(tForm.getReasonLv3Id());
      trouble.setReasonLv3Name(tForm.getReasonLv3Name());
      trouble.setReasonOverdueId(tForm.getReasonOverdueId());
      trouble.setReasonOverdueId2(tForm.getReasonOverdueId2());
      trouble.setReasonOverdueName(tForm.getReasonOverdueName());
      trouble.setReasonOverdueName2(tForm.getReasonOverdueName2());
      trouble.setSpmCode(tForm.getSpmCode());
      trouble.setTransNetworkTypeId(tForm.getTransNetworkTypeId());
      trouble.setTransReasonEffectiveId(tForm.getTransReasonEffectiveId());
      trouble.setTransReasonEffectiveContent(tForm.getTransReasonEffectiveContent());
      trouble.setAutoClose(tForm.getAutoClose());
      trouble.setWoCode(tForm.getWoCode());
      trouble.setRelatedTroubleCodes(tForm.getRelatedTroubleCodes());
      trouble.setIsMove(tForm.getIsMove());
      trouble.setDateMove(tForm.getDateMove());
      trouble.setUnitMove(tForm.getUnitMove());
      trouble.setUnitMoveName(tForm.getUnitMoveName());
      trouble.setIsStationVip(tForm.getIsStationVip());
      trouble.setIsTickHelp(tForm.getIsTickHelp());
      trouble.setNumHelp(tForm.getNumHelp());
      trouble.setAmiId(tForm.getAmiId());
      trouble.setNumAon(tForm.getNumAon());
      trouble.setNumGpon(tForm.getNumGpon());
      trouble.setNumNexttv(tForm.getNumNexttv());
      trouble.setNumThc(tForm.getNumThc());
      trouble.setInfoTicket(tForm.getInfoTicket());
      trouble.setRelatedCr(tForm.getRelatedCr());
      trouble.setNationCode(tForm.getNationCode());
      trouble.setDeferType(tForm.getDeferType());
      trouble.setEstimateTime(tForm.getEstimateTime());
      trouble.setLongitude(tForm.getLongitude());
      trouble.setLatitude(tForm.getLatitude());
      trouble.setGroupSolution(tForm.getGroupSolution());
      trouble.setCellService(tForm.getCellService());
      trouble.setIsSendTktu(tForm.getIsSendTktu());
      if ((6 == (tForm.getState()) || 7 == (tForm.getState()))
          && StringUtils.isNotNullOrEmpty(tForm.getConcave()) && !tForm.getConcave()
          .equals(trouble.getConcave())) {
        tForm.setIsSendTktu(Long.parseLong(TROUBLE.isTKTU));
      }
      trouble.setConcave(tForm.getConcave());
      trouble.setTroubleAssignId(tForm.getTroubleAssignId());
      trouble.setIsChat(tForm.getIsChat());
      trouble.setComplaintId(tForm.getComplaintId());
      trouble.setDowntime(tForm.getDowntime());
      trouble.setNumAffect(tForm.getNumAffect());
      trouble.setSubMin(tForm.getSubMin());
      trouble.setWarnLevel(tForm.getWarnLevel());
      trouble.setIsFailDevice(tForm.getIsFailDevice());
      trouble.setCountry(tForm.getCountry());
      trouble.setCountReopen(tForm.getCountReopen());
      // hungtv77 add phe duyet tam dong start
      trouble.setUnitApproval(tForm.getUnitApproval());
      // end
      if (tForm.getRemainTime() != null && Double.parseDouble(tForm.getRemainTime()) < 0) {
        trouble.setIsOverdue(1L);
      }
      //cap nhat lai thoi gian xu ly
      if (trouble.getInsertSource().contains(Constants.INSERT_SOURCE.SPM)) { //bccs 3
      } else {
        if (trouble.getAlarmGroup() != null && trouble.getTypeId() != null
            && ((!trouble.getPriorityId().toString().equals(troubleOld.getPriorityId()))
            || trouble.getTimeProcess() == null || trouble.getIsStationVip() != null)) {
          CfgTimeTroubleProcessDTO config = troublesRepository
              .getConfigTimeTroubleProcess(trouble.getTypeId(),
                  Long.parseLong(trouble.getAlarmGroup()), trouble.getPriorityId(),
                  trouble.getInsertSource());

          if (config != null) {
            if (trouble.getIsStationVip() != null && trouble.getIsStationVip().equals(1L)
                && config.getTimeStationVip() != null) {
              trouble.setTimeProcess(Double.valueOf(config.getTimeStationVip()));
            } else {
              trouble.setTimeProcess(config.getProcessTtTime() == null ? null
                  : Double.valueOf(config.getProcessTtTime()));
            }
            trouble.setTimeCreateCfg(
                config.getCreateTtTime() == null ? null : Double.valueOf(config.getCreateTtTime()));
          }
        }
        //cap nhat extend time

        List<CfgTimeExtendTtDTO> cfgTimeExtendTtDTO;
        if (trouble.getReasonId() != null && (trouble.getIsStationVip() == null || trouble
            .getIsStationVip().equals(0L))) {
          List<ConditionBean> lstCondition = new ArrayList<>();
          lstCondition.add(
              new ConditionBean("reasonId", trouble.getReasonId().toString(), "NAME_EQUAL",
                  "NUMBER"));
          lstCondition.add(
              new ConditionBean("typeId", trouble.getTypeId().toString(), "NAME_EQUAL", "NUMBER"));
          lstCondition.add(
              new ConditionBean("alarmGroupId", trouble.getAlarmGroup(), "NAME_EQUAL", "NUMBER"));
          lstCondition.add(
              new ConditionBean("priorityId", trouble.getPriorityId().toString(), "NAME_EQUAL",
                  "NUMBER"));

          if (trouble.getInsertSource().contains("NOC")) {
            lstCondition.add(
                new ConditionBean("country", trouble.getInsertSource(), "NAME_EQUAL", "STRING"));
          } else {
            lstCondition.add(new ConditionBean("country", "NOC", "NAME_EQUAL", "STRING"));
          }
          ConditionBeanUtil.sysToOwnListCondition(lstCondition);
          cfgTimeExtendTtDTO = cfgTimeExtendTtRepository
              .getListCfgTimeExtendTtByCondition(lstCondition, 0, 1, "", "");
          if (cfgTimeExtendTtDTO != null && !cfgTimeExtendTtDTO.isEmpty()) { // theo nguyen nhan
            trouble.setTimeProcess(Double.valueOf(cfgTimeExtendTtDTO.get(0).getTimeExtend()));
            if (!trouble.getReasonId().toString().equals(troubleOld.getReasonId())) {
              tForm.setIsStationVipName("Extend " + trouble.getTimeProcess() + " (h) ");
            }
            //cap nhat sang WO
            try {
              if (!StringUtils.isStringNullOrEmpty(trouble.getWoCode())) {
                WoDTO woDto = new WoDTO();
                woDto.setWoCode(trouble.getWoCode());
                woDto.setEstimateTime(cfgTimeExtendTtDTO.get(0).getTimeExtend());
                woServiceProxy.updateWoInfoProxy(woDto);

              }
            } catch (Exception ex) {
              log.error(ex.getMessage());
            }
          }
        } else {                                                        //theo mang, muc uu tien, nhom canh bao
          List<ConditionBean> lstCondition = new ArrayList<>();
          lstCondition.add(
              new ConditionBean("typeId", trouble.getTypeId().toString(), "NAME_EQUAL", "NUMBER"));
          lstCondition.add(
              new ConditionBean("alarmGroupId", trouble.getAlarmGroup(), "NAME_EQUAL", "NUMBER"));
          lstCondition.add(
              new ConditionBean("priorityId", trouble.getPriorityId().toString(), "NAME_EQUAL",
                  "NUMBER"));
          lstCondition.add(new ConditionBean("reasonId", "-1", "NAME_EQUAL", "NUMBER"));
          if (trouble.getInsertSource().contains("NOC")) {
            lstCondition.add(
                new ConditionBean("country", trouble.getInsertSource(), "NAME_EQUAL", "STRING"));
          } else {
            lstCondition.add(new ConditionBean("country", "NOC", "NAME_EQUAL", "STRING"));
          }
          ConditionBeanUtil.sysToOwnListCondition(lstCondition);
          cfgTimeExtendTtDTO = cfgTimeExtendTtRepository
              .getListCfgTimeExtendTtByCondition(lstCondition, 0, 1, "", "");
          if (cfgTimeExtendTtDTO != null && !cfgTimeExtendTtDTO.isEmpty()) {

            trouble.setTimeProcess(Double.valueOf(cfgTimeExtendTtDTO.get(0).getTimeExtend()));
            tForm.setIsStationVipName("Extend " + trouble.getTimeProcess() + " (h) ");

            //cap nhat sang WO
            try {
              if (!StringUtils.isStringNullOrEmpty(trouble.getWoCode())) {
                WoDTO woDto = new WoDTO();
                woDto.setWoCode(trouble.getWoCode());
                woDto.setEstimateTime(cfgTimeExtendTtDTO.get(0).getTimeExtend());
                System.out.println(
                    "WoCode " + trouble.getWoCode() + " EstimateTime " + cfgTimeExtendTtDTO.get(0)
                        .getTimeExtend());
                woServiceProxy.updateWoInfoProxy(woDto);

              }
            } catch (Exception ex) {
              log.error(ex.getMessage());
            }
          }
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  public static void reloadTrouble(TroublesEntity trouble) {
    if (trouble.getAssignTime() == null) {
      trouble.setAssignTime(new Date());
    }
    trouble.setRejectedCode(null);
    trouble.setRejectReason("");
    trouble.setAssignTimeTemp(null);
    trouble.setTimeUsed(null);
    trouble.setQueueTime(null);
    trouble.setDeferredTime(null);
    trouble.setDeferredReason("");
    trouble.setClearTime(null);
    trouble.setReasonId(null);
    trouble.setRootCause(null);
    trouble.setRelatedKedb("");
    trouble.setSolutionType(null);
    trouble.setWorkArround("");
    trouble.setEndTroubleTime(null);
    trouble.setReceiveUserName(null);
    trouble.setReasonName(null);
    trouble.setRelatedTroubleCodes(null);
    trouble.setClearUserId(null);
    trouble.setClearUserName(null);
    trouble.setTransReasonEffectiveContent("");
    trouble.setTransReasonEffectiveId(null);
    trouble.setTransNetworkTypeId(null);
    trouble.setWhereWrong("");
    trouble.setAsessmentData(null);
    trouble.setLineCutCode("");
    trouble.setCodeSnippetOff("");
    trouble.setClosuresReplace("");
    trouble.setCableType(null);
    trouble.setReasonLv1Id("");
    trouble.setReasonLv2Id("");
    trouble.setReasonLv3Id("");
    trouble.setReasonOverdueId("");
    trouble.setReasonOverdueId2("");
    trouble.setReasonLv1Name("");
    trouble.setReasonLv2Name("");
    trouble.setReasonLv3Name("");
    trouble.setReasonOverdueName("");
    trouble.setReasonOverdueName2("");
    trouble.setDateMove(null);
    trouble.setUnitMove(null);
    trouble.setUnitMoveName(null);
    trouble.setIsMove(null);
    trouble.setNumAon(null);
    trouble.setNumGpon(null);
    trouble.setNumNexttv(null);
    trouble.setNumThc(null);
    trouble.setNumPending(null);
    trouble.setInfoTicket(null);
  }

  public ResultInSideDto doCallIPCC(UnitDTO unitDTO, IpccServiceDTO ipccServiceDTO,
      TroublesInSideDTO troublesDTO, String urlForIPCC) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    try {
      //lay danh sach file
      String recordFileName = troublesDTO.getCreatedTimeFrom();
      if (StringUtils.isStringNullOrEmpty(recordFileName) && !StringUtils
          .isStringNullOrEmpty(troublesDTO.getTypeId())
          && !StringUtils.isStringNullOrEmpty(troublesDTO.getAlarmGroupId())
          && !StringUtils.isStringNullOrEmpty(troublesDTO.getPriorityId())) {
        CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO = new CfgTimeTroubleProcessDTO();
        cfgTimeTroubleProcessDTO.setTypeId(troublesDTO.getTypeId());
        cfgTimeTroubleProcessDTO.setSubCategoryId(Long.valueOf(troublesDTO.getAlarmGroupId()));
        cfgTimeTroubleProcessDTO.setPriorityId(troublesDTO.getPriorityId());
        cfgTimeTroubleProcessDTO.setCountry(troublesDTO.getInsertSource());
        CfgTimeTroubleProcessDTO cttpdto = ttCategoryServiceProxy
            .getConfigTimeTroubleProcess(cfgTimeTroubleProcessDTO);
        if (cttpdto != null) {
          recordFileName = cttpdto.getCdAudioName() == null ? cttpdto.getFtAudioName()
              : cttpdto.getCdAudioName();
        }
      }
      if (StringUtils.isStringNullOrEmpty(recordFileName)) {
        resultInSideDTO.setKey(Constants.RESULT.ERROR);
        resultInSideDTO.setMessage(I18n.getLanguage("incident.config.file"));
        return resultInSideDTO;
      }
      String mobile = ipccServiceDTO.getMobile();
      if (StringUtils.isStringNullOrEmpty(mobile)) {
        //lay don vi nhan tin
        //insert logs
        if (unitDTO != null && !StringUtils.isStringNullOrEmpty(unitDTO.getMobile())) {
          mobile = unitDTO.getMobile();
        }
      }

      if (!StringUtils.isStringNullOrEmpty(mobile)) {
        LogCallIpccDTO dto = new LogCallIpccDTO();
        dto.setStartCallTime(
            DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
        dto.setWoId((Long.parseLong(
            troublesDTO.getTroubleId() == null ? null : String.valueOf(troublesDTO.getTroubleId()))
            + 1000000000L) + "");
        dto.setTransactionId(
            troublesDTO.getTroubleCode() + "_" + DateUtil.dateTime2StringNoSlash(new Date()));
        dto.setUserCall(troublesDTO.getProcessingUserName());
        dto.setUserName(troublesDTO.getProcessingUnitName());
        dto.setPhone(mobile);
        dto.setRecordFileCode(recordFileName);
        woServiceProxy.insertLogCallIpcc(dto);
        //day sang ipcc
        AutoCallOutInput input = new AutoCallOutInput();
        input.setPhoneNumber(mobile);
        input.setRecordFileCode(recordFileName);
        input.setTransactionId(dto.getTransactionId());
        input.setResponseUrl(urlForIPCC);
        NomalOutput res = WSIPCC.getWsIpcc().autoCallout(ipccServiceDTO, input);
        if (res != null && !Constants.RESULT.SUCCESS.equalsIgnoreCase(res.getDescription())) {
          List<ConditionBean> lstCondition = new ArrayList<ConditionBean>();
          lstCondition.add(
              new ConditionBean("transactionId", dto.getTransactionId(), "NAME_EQUAL", "STRING"));
          ConditionBeanUtil.sysToOwnListCondition(lstCondition);
          List<LogCallIpccDTO> lst = woServiceProxy
              .getListLogCallIpccByCondition(
                  new BaseDto(lstCondition, 0, Integer.MAX_VALUE, "desc", "id"));
          if (lst != null && !lst.isEmpty()) {
            LogCallIpccDTO dtoUpdate = lst.get(0);
            dtoUpdate.setResult(RESULT.FAIL);
            dtoUpdate.setDescription(res.getDescription());
            dtoUpdate.setResultTime(
                DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
            woServiceProxy.updateLogCallIpcc(dtoUpdate);
          }
          throw new Exception("IPCC " + res.getDescription());
        }
      } else {
        resultInSideDTO.setKey(RESULT.ERROR);
        resultInSideDTO.setMessage(I18n.getLanguage("incident.call.ipcc.mobile"));
        return resultInSideDTO;
      }

      resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDTO.setKey(RESULT.ERROR);
      resultInSideDTO.setMessage(ex.getMessage());
    }

    return resultInSideDTO;
  }

  private void callSIRC(TroublesInSideDTO newDTO, TroublesInSideDTO troubleDTO,
      CatItemDTO stateOldDTO,
      CatItemDTO stateDTO, Map<String, String> mapConfigProperty) {
    try {
      if (newDTO != null && Constants.INSERT_SOURCE.SIRC.equals(newDTO.getInsertSource())) {
        String note = "OK";
        if ((Constants.TT_STATE.REJECT.equals(stateDTO.getItemCode()))
            && !newDTO.getState().equals(troubleDTO.getState())) {
          note = newDTO.getRejectReason();
        }
        if ((Constants.TT_STATE.REJECT.equals(stateDTO.getItemCode())
            || Constants.TT_STATE.CLEAR.equals(stateDTO.getItemCode())
            || Constants.TT_STATE.CLOSED_Not_KEDB.equals(stateDTO.getItemCode())
            || Constants.TT_STATE.CLOSED.equals(stateDTO.getItemCode())
            || Constants.TT_STATE.Waiting_Receive.equals(stateDTO.getItemCode()))
            && !newDTO.getState().equals(troubleDTO.getState())) {
          JsonUtils client = new JsonUtils();
          HttpClient httpClient = client.getHttpClient(60000, 60000);
          String method = "POST";
          String urlStr = mapConfigProperty.get("url_srsc");
          String temp = "";
          if (Constants.TT_STATE.REJECT.equals(stateDTO.getItemCode())) {
            temp = "None";
          }

          String postData = "{   \n"
              + "\"ticket_id\": " + newDTO.getAlarmId() + ",   \n"
              + "\"old_status\": \"" + stateOldDTO.getItemCode() + "\", \n"
              + "\"new_status\": \"" + stateDTO.getItemCode() + "\",   \n"
              + "\"note\": \"" + note + "\", \n"
              + "\"status_code\": \"" + temp + "\"}";
          RequestBuilder requestBuilder = client.configureRequest(method, urlStr, postData);
          HttpResponse response = client.doRequest(httpClient, requestBuilder);
          HttpEntity entity = response.getEntity();
          if (entity != null) {
            System.out.println("--EntityUtils.toString(entity)-- " + EntityUtils.toString(entity));
          }
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  private String callOtherSystem(TroublesInSideDTO newDTO, TroublesInSideDTO troubleDTO,
      UserToken userToken, UnitDTO unitToken)
      throws Exception {
    String messages = "";
    if (troubleDTO != null && !StringUtils.isStringNullOrEmpty(troubleDTO.getWoCode()) && "1"
        .equals(String.valueOf(troubleDTO.getIsTickHelp()))
        && Constants.INSERT_SOURCE.SPM.equals(troubleDTO.getInsertSource())) {
      if (!(9 == (troubleDTO.getState())) && 9 == (newDTO.getState())) {//da khac phuc
        String worklog = I18n.getLanguage("tt.update.success.help");
        String reasonId = newDTO.getReasonLv3Id();
        worklog = worklog + " \n " + I18n.getLanguage("trouble.cmbReasonLv1") + ": " + newDTO
            .getReasonLv1Name()
            + " \n " + I18n.getLanguage("trouble.cmbReasonLv2") + ": " + newDTO.getReasonLv2Name()
            + " \n " + I18n.getLanguage("trouble.cmbReasonLv3") + ": " + newDTO.getReasonLv3Name()
            + " \n " + (newDTO.getWorkLog() == null ? "" : newDTO.getWorkLog());

        newDTO.setWorkLog(worklog);

        //goi sang WO
        woServiceProxy
            .completeWorkHelp(new RequestApiWODTO(troubleDTO.getWoCode(), userToken.getUserName(),
                newDTO.getWorkLog(),
                reasonId));

        //goi sang CC
        newDTO.setProcessingUnitName(unitToken.getUnitName());
        newDTO.setProcessingUnitId(userToken.getDeptId().toString());
        newDTO.setProcessingUserName(userToken.getUserName());
        newDTO.setProcessingUserPhone(userToken.getTelephone());
        updateTroubleToSPM(newDTO, "2");
      }
      if (!(4 == (troubleDTO.getState())) && 4 == (newDTO.getState())) {//reject
        String reasonReject =
            I18n.getLanguage("tt.update.reject.help") + " " + newDTO.getRejectReason();
        String worklog = StringUtils.isStringNullOrEmpty(newDTO.getWorkLog()) == true ? reasonReject
            : (newDTO.getWorkLog() + " \n " + userToken.getUserName() + " " + reasonReject);
        newDTO.setWorkLog(worklog);
        //goi sang wo
        woServiceProxy
            .completeWorkHelp(
                new RequestApiWODTO(troubleDTO.getWoCode(), userToken.getUserName(), reasonReject,
                    "-1"));
        //goi sang CC
        newDTO.setProcessingUnitName(unitToken.getUnitName());
        newDTO.setProcessingUnitId(userToken.getDeptId().toString());
        newDTO.setProcessingUserName(userToken.getUserName());
        newDTO.setProcessingUserPhone(userToken.getTelephone());
        updateTroubleToSPM(newDTO, "2");
      }
    }

    List<byte[]> fileDocumentByteArray = newDTO.getFileDocumentByteArray();
    if (troubleDTO != null && Constants.INSERT_SOURCE.SPM.equals(troubleDTO.getInsertSource())
        && newDTO.getWoCode() != null
        && newDTO.getArrFileName() != null && !newDTO.getArrFileName().isEmpty()) {
      WoDTO woDTO = new WoDTO();
      woDTO.setWoCode(newDTO.getWoCode());
      woDTO.setListFileName(newDTO.getArrFileName());
      woDTO.setFileArr(fileDocumentByteArray);
      woServiceProxy.updateWoForSPMProxy(woDTO);
    }
    //namtn update file to WO from SPM end

    //goi sang spm_vtnet
    if ((newDTO.getStateName().equals(Constants.TT_STATE.CLOSED) || newDTO.getStateName()
        .equals(Constants.TT_STATE.CLOSED_Not_KEDB))
        && !troubleDTO.getStateName().equals(Constants.TT_STATE.CLOSED) && !troubleDTO
        .getStateName().equals(Constants.TT_STATE.CLOSED_Not_KEDB)
        && newDTO.getInsertSource()
        .equalsIgnoreCase(Constants.INSERT_SOURCE.SPM_VTNET)) { //SPM_VTNET
      ConditionBean conditionBean = new ConditionBean("incidentId",
          troubleDTO.getTroubleId().toString(), Constants.NAME_EQUAL,
          Constants.NUMBER);// 20200121_HaNV15_Edit: STRING -> NUMBER
      List<ConditionBean> lstCon = new ArrayList<>();
      lstCon.add(conditionBean);
      ConditionBeanUtil.sysToOwnListCondition(lstCon);
      List<ItAccountDTO> lstItAccount = itAccountRepository
          .getListItAccountByCondition(lstCon, 0, 1, "", "");
      if (lstItAccount != null && !lstItAccount.isEmpty()) {// call auto check
        log.info("Begin call auto check!");
        JsonResponseBO res = wssapPort
            .autoCheck(lstItAccount.get(0).getAccount(), troubleDTO.getTroubleCode());
        StringUtils.printLogData("goi sang spm_vtnet autoCheck: ", res, JsonResponseBO.class);
        log.info("End call auto check!");
        if (res != null && res.getResultCode() != 0) {
          messages = res.getResultMessage();
        }
      }

    }
    return messages;
  }

  @Override
  public ResultInSideDto deleteTrouble(Long id) {
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    TroublesEntity troublesEntity = troublesRepository.findTroublesById(id);
    troublesEntity.setState(2L);
    troublesRepository.updateTroubles(troublesEntity);

    TroubleWorklogEntity condition = new TroubleWorklogEntity();
    condition.setCreateUnitId(userToken.getDeptId());
    condition.setCreateUnitName(unitToken.getUnitName());
    condition.setCreateUserId(userToken.getUserID());
    condition.setCreateUserName(userToken.getUserName());
    condition.setDescription(I18n.getLanguage("ticket.close"));
    condition.setTroubleId(troublesEntity.getTroubleId());
    condition.setWorklog(userToken.getUserName() + " " + I18n.getLanguage("ticket.close"));
    condition.setCreatedTime(new Date());
    troubleWorklogRepository.insertTroubleWorklog(condition);

    //lien ket SRSC
    if (Constants.INSERT_SOURCE.SIRC.equals(troublesEntity.getInsertSource())) {
      Map<String, String> mapConfigProperty = troublesRepository.getConfigProperty();
      JsonUtils client = new JsonUtils();
      HttpClient httpClient = client.getHttpClient(60000, 60000);
      String method = "POST";
      String urlStr = mapConfigProperty.get("url_srsc");
      String postData = "{\"ticket_id\": " + troublesEntity.getAlarmId() + ", \"status\": -1}";

      RequestBuilder requestBuilder = client.configureRequest(method, urlStr, postData);
      HttpResponse response = client.doRequest(httpClient, requestBuilder);
      StatusLine statusLine = response.getStatusLine();
      statusLine.getStatusCode();
      response.getEntity();
    }

    return new ResultInSideDto(troublesEntity.getTroubleId(), RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public Datatable viewCall(TroublesInSideDTO dtoTran) {
    List<ConditionBean> lstCondition = new ArrayList<ConditionBean>();
    Datatable data = new Datatable();
    lstCondition.add(new ConditionBean("woId", (Long
        .parseLong(dtoTran.getTroubleId() == null ? null : String.valueOf(dtoTran.getTroubleId()))
        + 1000000000L) + "", Constants.NAME_EQUAL, Constants.NUMBER));
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    List<LogCallIpccDTO> logCallIpccDTOS = woServiceProxy.
        getListLogCallIpccByCondition(
            new BaseDto(lstCondition, 0, Integer.MAX_VALUE, "desc", "id"));
    if (logCallIpccDTOS != null && logCallIpccDTOS.size() > 0) {
      data.setTotal(logCallIpccDTOS.size());
      int pages = (int) Math.ceil(logCallIpccDTOS.size() * 1.0 / dtoTran.getPageSize());
      data.setPages(pages);
      data.setData(DataUtil.subPageList(logCallIpccDTOS, dtoTran.getPage(), dtoTran.getPageSize()));
    }
    return data;
  }

  @Override
  public List<UsersInsideDto> getListChatUsers(TroublesInSideDTO troublesDTO) {
    log.debug("Request to getListChatUsers : {}", troublesDTO);
    UserToken userToken = ticketProvider.getUserToken();
    List<Long> userIds = new ArrayList<>();
    userIds.add(userToken.getUserID());
    if (troublesDTO.getCreateUserId() != null) {
      userIds.add(troublesDTO.getCreateUserId());
    }
    if (troublesDTO.getReceiveUserId() != null) {
      userIds.add(troublesDTO.getReceiveUserId());
    }
    return userBusiness.getListUsersByListUserId(userIds);
  }

  @Override
  public Datatable loadUserSupportGroup(TroublesInSideDTO troublesDTO) {
    CatItemDTO catItemDTO = catItemBusiness.getCatItemById(troublesDTO.getTypeId());
    String type = "TT_CHAT_";
    if (catItemDTO != null && catItemDTO.getItemCode() != null) {
      type += catItemDTO.getItemCode();
    }
    List<CatItemDTO> lst = catItemBusiness.getListItemByCategory("CHAT_CODE", null);
    CatItemDTO itemDTO = new CatItemDTO();
    if (lst != null && !lst.isEmpty()) {
      for (CatItemDTO cat : lst) {
        if (type.equals(cat.getItemCode())) {
          itemDTO = cat;
          break;
        }
      }
    }
    List<String> lstUsername = new ArrayList<>();
    //danh sach nhan vien dc cau hinh tai danh muc chung
    if (StringUtils.isNotNullOrEmpty(itemDTO.getDescription())) {
      lstUsername = Arrays.asList(itemDTO.getDescription().split(","));
    }
    UsersInsideDto usersInsideDto = new UsersInsideDto();
    usersInsideDto.setLstUserName(lstUsername);
    usersInsideDto.setPage(troublesDTO.getPage());
    usersInsideDto.setPageSize(troublesDTO.getPageSize());
    if (lstUsername != null && !lstUsername.isEmpty()) {
      Datatable datatable = userBusiness.getListUsersByList(usersInsideDto);
      return datatable;
    }
    return new Datatable();
  }

  @Override
  public List<CatItemDTO> onSearchCountByState(TroublesInSideDTO dto) {
    return troublesRepository.onSearchCountByState(dto);
  }

  @Override
  public Map<String, String> getConfigProperty() {
    return troublesRepository.getConfigProperty();
  }

  @Override
  public ResultInSideDto getListReasonBCCS(TroublesInSideDTO dto, Long parentId, int level) {
    ResultInSideDto result = new ResultInSideDto();
    List<CompCause> lst = new ArrayList<>();
    try {
      if (dto != null) {
        List<Long> lstCompGroup = new ArrayList<Long>();
        if (dto.getComplaintGroupId() != null) {
          String[] complaintGroup = dto.getComplaintGroupId().split(",");
          for (int i = 0; i < complaintGroup.length; i++) {
            lstCompGroup.add(Long.parseLong(complaintGroup[i]));
          }
        }
        if (Constants.INSERT_SOURCE.BCCS.equals(dto.getInsertSource())
            || Constants.INSERT_SOURCE.SPM.equals(dto.getInsertSource())
            || Constants.INSERT_SOURCE.SPM_VTNET.equals(dto.getInsertSource())) { //vn
          Long cfgType = null;
          if (dto.getIsTickHelp() != null && 1L == (dto.getIsTickHelp())) {//co
            cfgType = 1L;
          }

          List<CompCauseDTO> lstCauseDTO = compCauseBusiness
              .translateList(compCauseBusiness.getComCauseList(dto.getServiceType(),
                  lstCompGroup, parentId, level, dto.getTechnology(), cfgType, null, true),
                  I18n.getLocale());
          if (lstCauseDTO != null && !lstCauseDTO.isEmpty()) {
            for (CompCauseDTO causeDTO : lstCauseDTO) {
              CompCause cause = new CompCause();
              cause.setCompCauseId(Long.parseLong(causeDTO.getCompCauseId()));
              cause.setName(causeDTO.getName());
              cause.setCode(causeDTO.getCode());
              lst.add(cause);
            }

          }
        } else if (dto.getInsertSource()
            .contains(INSERT_SOURCE.BCCS)) { //equals --> contains sua bccs 3

          List<CfgServerNocEntity> lstServer = cfgServerNocRepository
              .getListCfgServerNocByCondition(dto.getInsertSource());
          if (lstServer == null || lstServer.isEmpty()) {
            result.setMessage(I18n.getValidation("cfg.ws.cc"));
            return result;
          }
          CauseFrom causeFrom = new CauseFrom();
          causeFrom.getListGroupId().addAll(lstCompGroup);
          causeFrom.setParentId(parentId);
          causeFrom.setServiceTypeId(dto.getServiceType());
          causeFrom.setLineType(dto.getTechnology());
          causeFrom.setCauseLevel(Long.valueOf("" + level));
          SpmRespone res = wsbccs2Port.getListCause(lstServer.get(0).toDTO(), 2, causeFrom);
          List<CauseDTO> lstCauseDTO = res.getListCauseDTO();
          if (lstCauseDTO != null && !lstCauseDTO.isEmpty()) {
            for (CauseDTO causeDTO : lstCauseDTO) {
              CompCause cause = new CompCause();
              cause.setCompCauseId(causeDTO.getCauseId());
              cause.setName(causeDTO.getName());
              cause.setCode(causeDTO.getCode());
              lst.add(cause);
            }

          }
          //Cap nhat goi ws nguyen nhan , giai phap tu BCCS
        } else if (dto.getInsertSource()
            .contains(INSERT_SOURCE.SPM)) { //equals --> contains sua bccs 3
          List<CfgServerNocEntity> lstServer = cfgServerNocRepository
              .getListCfgServerNocByCondition(dto.getInsertSource());
          if (lstServer == null || lstServer.isEmpty()) {
            result.setMessage(I18n.getValidation("cfg.ws.cc"));
            return result;
          }
          List<com.viettel.bccs2.CauseDTO> lstCauseDTO = troublesServiceForCCBusiness
              .getCompCauseDTOForCC3(
                  (parentId == null ? "" : parentId) + "",
                  (dto.getServiceType() == null ? "" : dto.getServiceType()) + "",
                  dto.getComplaintGroupId(), lstServer.get(0).toDTO());
          if (lstCauseDTO != null && !lstCauseDTO.isEmpty()) {
            for (com.viettel.bccs2.CauseDTO causeDTO : lstCauseDTO) {
              CompCause cause = new CompCause();
              cause.setCompCauseId(causeDTO.getCauseId());
              cause.setName(causeDTO.getName());
              cause.setCode(causeDTO.getCode());
              lst.add(cause);
            }

          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (!StringUtils.isStringNullOrEmpty(result)) {
      result.setObject(lst);
    }

    return result;
  }

  @Override
  public List getListReasonOverdue(TroublesInSideDTO troubleDTO, Long parentId) {
    List<CompCause> lst = new ArrayList<CompCause>();
    try {
      List<ConditionBean> lstCondition = new ArrayList<ConditionBean>();
      lstCondition.add(
          new ConditionBean("insertSource", troubleDTO.getInsertSource(), Constants.NAME_EQUAL,
              Constants.STRING));
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      List<CfgServerNocDTO> lstServer = cfgServerNocRepository
          .getListCfgServerNocByCondition(lstCondition, 0, 100, "", "");
      if (lstServer == null || lstServer.isEmpty()) {
        return lst;
      }
      //duongnt thay đổi lấy nn quá hạn thay vì gọi hàm cũ
      if (troubleDTO.getInsertSource().contains(Constants.INSERT_SOURCE.BCCS)
          || Constants.INSERT_SOURCE.SPM.equals(troubleDTO.getInsertSource())) {
        SpmRespone res = wsbccs2Port.getCauseExistError(parentId, lstServer.get(0));
        if ("00".equals(res.getErrorCode())) {
          List<CauseErrorExpireDTO> lst2 = res.getListCauseErrorExpire();
          if (lst2 != null) {
            for (CauseErrorExpireDTO expireDTO : lst2) {
              CompCause cause = new CompCause();
              cause.setCompCauseId(expireDTO.getCauseErrExpId());
              cause.setCode(expireDTO.getCode());
              cause.setName(expireDTO.getName());
              lst.add(cause);
            }
          }
        }
      } else if (troubleDTO.getInsertSource().contains(Constants.INSERT_SOURCE.SPM)) {
        List<com.viettel.bccs2.CauseErrorExpireDTO> lstErrorExpireDTO = troublesServiceForCCBusiness
            .getCauseErrorExpireForCC3((parentId == null ? "" : parentId) + "", lstServer.get(0));
        if (lstErrorExpireDTO != null && !lstErrorExpireDTO.isEmpty()) {
          for (com.viettel.bccs2.CauseErrorExpireDTO expireDTO : lstErrorExpireDTO) {
            CompCause cause = new CompCause();
            cause.setCompCauseId(expireDTO.getCauseErrExpId());
            cause.setCode(expireDTO.getCode());
            cause.setName(expireDTO.getName());
            lst.add(cause);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List getListGroupSolution(TroublesInSideDTO troubleDTO) throws Exception {
    List<TroubleNetworkSolutionDTO> lst = new ArrayList<>();
    try {
      List<ConditionBean> lstCondition = new ArrayList<ConditionBean>();
      lstCondition.add(
          new ConditionBean("insertSource", troubleDTO.getInsertSource(), Constants.NAME_EQUAL,
              Constants.STRING));
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      List<CfgServerNocDTO> lstServer = cfgServerNocRepository
          .getListCfgServerNocByCondition(lstCondition, 0, 100, "", "");

      if (lstServer == null || lstServer.isEmpty()) {
        return lst;
      }
      SpmRespone res = wsbccs2Port.getListSolution(lstServer.get(0));
      if ("00".equals(res.getErrorCode())) {
        lst = res.getListTroubleNetworkSolutionDTO();
      } else {
        throw new Exception(
            I18n.getLanguage("incident.groupSolution") + " " + res.getErrorDescription());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<CatItemDTO> getLstNetworkLevel(String typeId) {
    List<CatItemDTO> lst = new ArrayList<>();
    CfgMapNetLevelIncTypeDTO dto = new CfgMapNetLevelIncTypeDTO(null, Long.parseLong(typeId), null);
    List<CfgMapNetLevelIncTypeDTO> lstMap = commonStreamServiceProxy
        .getListCfgMapNetLevelIncTypeDTO(dto);
    if (lstMap != null && lstMap.size() > 0 && lstMap.get(0).getNetworkLevelId() != null) {
      String[] tmp = lstMap.get(0).getNetworkLevelId().split(",");
      for (int i = 0; i < tmp.length; i++) {
        CatItemDTO cat = new CatItemDTO();
        cat.setItemCode(tmp[i].trim());
        cat.setItemName(tmp[i].trim());
        cat.setItemId(Long.valueOf(i + ""));
        lst.add(cat);
      }
    }
    return lst;
  }

  @Override
  public File getListTroubleSearchExport(TroublesInSideDTO troubleSearch) throws Exception {
    log.debug("Request to getListTroubleSearchExport : {}", troubleSearch);
    String[] header = new String[]{"troubleName", "troubleCode", "stateName",
        "description", "typeName", "alarmGroupName", "priorityName", "impactName",
        "affectedService", "location", "affectedNode", "createUserName", "createUnitName",
        "receiveUnitName", "receiveUserName", "solutionTypeName", "workArround",
        "riskName", "supportUnitName", "vendorName", "relatedPt", "workLog",
        "rejectedCodeName", "createdTime", "lastUpdateTime", "assignTime", "queueTime",
        "clearTime", "closedTime", "createLateStr", "beginTroubleTime", "endTroubleTime",
        "affectTime", "fixTime", "isOverdueFixTime", "isOverdueCloseTime", "reasonName",
        "rootCause",
        "remainTime", "clearUserId", "clearUserName", "autoCloseName", "closeTtTime",
        "relatedTroubleCodes",
        "relatedKedb", "subCategoryName", "relatedTt", "dateMove", "unitMoveName",
        "isStationVipName",
        "infoTicket", "catchingTime", "numHelp", "relatedCr", "warnLevel", "woCode",
        "createUserByCC", "deferredTime", "longitude", "latitude", "insertSource", "cellService", "concave"
        //, "warnLevel"
    };

    if (troubleSearch.getLstType() != null && !troubleSearch.getLstType().isEmpty()
        && troubleSearch.getLstType().size() == 1) {
      Map<String, String> mapConfigProperty = getConfigProperty();
      if (checkExitsProperty(mapConfigProperty,
          troubleSearch.getLstType().get(0), Constants.CONFIG_PROPERTY.TT_TYPE_TRANS)) {
        header = new String[]{"troubleName", "troubleCode", "stateName",
            "description", "typeName", "alarmGroupName", "priorityName", "impactName",
            "affectedService", "location", "affectedNode", "createUserName", "createUnitName",
            "receiveUnitName", "receiveUserName", "solutionTypeName", "workArround",
            "riskName", "supportUnitName", "vendorName", "relatedPt", "workLog",
            "rejectedCodeName", "createdTime", "lastUpdateTime", "assignTime", "queueTime",
            "clearTime", "closedTime", "createLateStr", "beginTroubleTime", "endTroubleTime",
            "affectTime", "fixTime", "isOverdueFixTime", "isOverdueCloseTime", "reasonName",
            "rootCause",
            "remainTime", "networkLevel",
            "lineCutCode", "codeSnippetOff", "cableTypeName", "closuresReplace", "asessmentData",
            "whereWrong",
            "timeUsed", "closeTtTime", "relatedKedb", "autoCloseName", "transNetworkTypeName",
            "transReasonEffectiveName",
            "transReasonEffectiveContent", "clearUserId", "clearUserName", "subCategoryName",
            "relatedTt",
            "dateMove", "unitMoveName", "catchingTime", "numHelp", "relatedCr", "warnLevel",
            "woCode", "createUserByCC", "insertSource", "cellService", "concave"
            //, "warnLevel"
        };
      }
    }

    List<TroublesInSideDTO> lst = troublesRepository.getTroublesSearchExport(troubleSearch);
    updateTroubleDTOInfo(lst);
    return exportFileEx(lst, header, troubleSearch.getCreatedTimeFrom(),
        troubleSearch.getCreatedTimeTo());

  }

  private File exportFileEx(List<TroublesInSideDTO> lstImport, String[] columnExport,
      String fromDate,
      String toDate) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    Map<String, String> fieldSplit = new HashMap<>();
    String title = I18n.getLanguage("trouble.report");
    String fileNameOut = "TroubleReport";
    String sheetName = I18n.getLanguage("trouble.report");
    String subTitle;
    if (fromDate != null && toDate != null) {
      Date fromDateTmp = DataUtil.convertStringToDate(fromDate);
      Date toDateTmp = DataUtil.convertStringToDate(toDate);
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      subTitle = I18n.getLanguage("trouble.export.exportDate", dateFormat.format(fromDateTmp),
          dateFormat.format(toDateTmp));
    } else {
      subTitle = "";
    }

    ConfigFileExport configfileExport = new ConfigFileExport(
        lstImport
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 5
        , true
        , "language.trouble.list"
        , lstHeaderSheet1
        , fieldSplit
        , ""
        , I18n.getLanguage("trouble.export.firstLeftHeader")
        , I18n.getLanguage("trouble.export.secondLeftHeader")
        , I18n.getLanguage("trouble.export.firstRightHeader")
        , I18n.getLanguage("trouble.export.secondRightHeader")
    );
    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("trouble.list.id"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configfileExport);
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  private List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
  }

  public static Boolean checkExitsProperty(Map<String, String> map, String id, String key) {
    try {
      String typeConfig = map.get(key);
      String[] arrTypeTrans = typeConfig.split(",");
      List<String> lst = Arrays.asList(arrTypeTrans);
      if (lst != null && lst.contains(id)) {
        return true;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
    return false;
  }

  //time zone
  public static void setTimeZoneForTrouble(TroublesInSideDTO troubleDTO, Double offset)
      throws Exception {

    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getCreatedTime()) && !Double.valueOf(0)
        .equals(offset) && troubleDTO.getTroubleId() != null) {
      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date createDate = new Date(
          troubleDTO.getCreatedTime().getTime() + (long) (
              offset * 60 * 60 * 1000));
      troubleDTO.setCreatedTime(createDate);
    }
    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getLastUpdateTime()) && !Double.valueOf(0)
        .equals(offset)) {
      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date lastUpdateTime = new Date(
          troubleDTO.getLastUpdateTime().getTime() + (long) (
              offset * 60 * 60 * 1000));
      troubleDTO.setLastUpdateTime(lastUpdateTime);
    }
    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getAssignTime()) && !Double.valueOf(0)
        .equals(offset)) { //cap nhat lai

      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date assignTime = new Date(
          troubleDTO.getAssignTime().getTime() + (long) (
              offset * 60 * 60 * 1000));
      troubleDTO.setAssignTime(assignTime);
    }
    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getQueueTime()) && !Double.valueOf(0)
        .equals(offset)) {

      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date queueTime = new Date(
          troubleDTO.getQueueTime().getTime() + (long) (
              offset * 60 * 60 * 1000));
      troubleDTO.setQueueTime(queueTime);
    }

    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getClearTime()) && !Double.valueOf(0)
        .equals(offset)) {

      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date clearTime = new Date(
          troubleDTO.getClearTime().getTime() + (long) (
              offset * 60 * 60 * 1000));
      troubleDTO.setClearTime(clearTime);
    }
    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getClosedTime()) && !Double.valueOf(0)
        .equals(offset)) {

      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date closeTime = new Date(
          troubleDTO.getClosedTime().getTime() + (long) (
              offset * 60 * 60 * 1000));
      troubleDTO.setClosedTime(closeTime);
    }
    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getBeginTroubleTime()) && !Double.valueOf(0)
        .equals(offset)) {

      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date beginTroubleTime = new Date(
          troubleDTO.getBeginTroubleTime().getTime()
              + (long) (offset * 60 * 60 * 1000));
      troubleDTO.setBeginTroubleTime(beginTroubleTime);
    }
    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getEndTroubleTime()) && !Double.valueOf(0)
        .equals(offset)) {

      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date endTroubleTime = new Date(
          troubleDTO.getEndTroubleTime().getTime() + (long) (
              offset * 60 * 60 * 1000));
      troubleDTO.setEndTroubleTime(endTroubleTime);
    }
    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getDeferredTime()) && !Double.valueOf(0)
        .equals(offset)) {

      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date deferredTime = new Date(
          troubleDTO.getDeferredTime().getTime() + (long) (
              offset * 60 * 60 * 1000));
      troubleDTO.setDeferredTime(deferredTime);
    }
    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getAssignTimeTemp()) && !Double.valueOf(0)
        .equals(offset)) {

      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date deferredTime = new Date(
          troubleDTO.getAssignTimeTemp().getTime() + (long) (
              offset * 60 * 60 * 1000));
      troubleDTO.setAssignTimeTemp(deferredTime);
    }

    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getDateMove()) && !Double.valueOf(0)
        .equals(offset)) {

      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date dateMove = new Date(
          troubleDTO.getDateMove().getTime() + (long) (offset
              * 60 * 60 * 1000));
      troubleDTO.setDateMove(dateMove);
    }

    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getCatchingTime()) && !Double.valueOf(0)
        .equals(offset)) {

      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date catchTime = new Date(
          troubleDTO.getCatchingTime().getTime() + (long) (
              offset * 60 * 60 * 1000));
      troubleDTO.setCatchingTime(catchTime);
    }
    if (!StringUtils.isStringNullOrEmpty(troubleDTO.getEstimateTime()) && !Double.valueOf(0)
        .equals(offset)) {
      //Convert thoi gian ve thoi gian theo timezone nguoi dung
      Date estimateTime = new Date(
          troubleDTO.getEstimateTime().getTime() + (long) (
              offset * 60 * 60 * 1000));
      troubleDTO.setEstimateTime(estimateTime);
    }
  }

  public void updateTroubleDTOInfo(List<TroublesInSideDTO> lst) {
    Date beginTroubleTime = null;
    Date createTime = null;
    Double delta = null;
    Double timeCreatCfg = null;
    Date fixTime = new Date();
    //HaiNV20 add, get worklog

    try {
      //time_zone
      Map<String, CatItemDTO> mapItem = getMapItem();
      //time_zone
      if (lst != null && !lst.isEmpty()) {
        List<CatItemDTO> lstReason = getListReasion();
        List<CatItemDTO> lstTroubleCap = getTroubleDutCap();
        for (TroublesInSideDTO troubleDTO : lst) {
          //TienNV comment lai

          troubleDTO.setStateName(troubleDTO.getState() == null ? ""
              : mapItem.get(String.valueOf(troubleDTO.getState())) != null ? mapItem
                  .get(String.valueOf(troubleDTO.getState()))
                  .getItemName() : "");
          troubleDTO.setTypeName(troubleDTO.getTypeId() == null ? ""
              : mapItem.get(String.valueOf(troubleDTO.getTypeId())) != null ? mapItem
                  .get(String.valueOf(troubleDTO.getTypeId()))
                  .getItemName() : "");
          troubleDTO.setSubCategoryName(troubleDTO.getSubCategoryId() == null ? ""
              : mapItem.get(String.valueOf(troubleDTO.getSubCategoryId())) != null ? mapItem
                  .get(String.valueOf(troubleDTO.getSubCategoryId())).getItemName() : "");
          troubleDTO.setPriorityName(troubleDTO.getPriorityId() == null ? ""
              : mapItem.get(String.valueOf(troubleDTO.getPriorityId())) != null ? mapItem
                  .get(String.valueOf(troubleDTO.getPriorityId())).getItemName() : "");
          troubleDTO.setImpactName(troubleDTO.getImpactId() == null ? ""
              : mapItem.get(String.valueOf(troubleDTO.getImpactId())) != null ? mapItem
                  .get(String.valueOf(troubleDTO.getImpactId())).getItemName() : "");
          troubleDTO.setSolutionTypeName(troubleDTO.getSolutionType() == null ? ""
              : mapItem.get(String.valueOf(troubleDTO.getSolutionType())) != null ? mapItem
                  .get(String.valueOf(troubleDTO.getSolutionType())).getItemName() : "");
          troubleDTO.setRiskName(troubleDTO.getRisk() == null ? ""
              : mapItem.get(String.valueOf(troubleDTO.getRisk())) != null ? mapItem
                  .get(String.valueOf(troubleDTO.getRisk()))
                  .getItemName() : "");
          troubleDTO.setVendorName(troubleDTO.getVendorId() == null ? ""
              : mapItem.get(String.valueOf(troubleDTO.getVendorId())) != null ? mapItem
                  .get(String.valueOf(troubleDTO.getVendorId())).getItemName() : "");

          troubleDTO.setRejectedCodeName(troubleDTO.getRejectedCode() == null ? ""
              : mapItem.get(String.valueOf(troubleDTO.getRejectedCode())) != null ? mapItem
                  .get(String.valueOf(troubleDTO.getRejectedCode())).getItemName() : "");
          troubleDTO.setWarnLevelName(troubleDTO.getWarnLevel() == null ? ""
              : mapItem.get(String.valueOf(troubleDTO.getWarnLevel())) != null ? mapItem
                  .get(String.valueOf(troubleDTO.getWarnLevel())).getItemName() : "");

          String cableType = "";
          if (troubleDTO.getCableType() != null) {
            if (1 == (troubleDTO.getCableType())) {// cap treo
              cableType = I18n.getLanguage("incident.slings");
            } else {
              cableType = I18n.getLanguage("incident.buried.cable");
            }
          }
          //Co khong
          if (null != troubleDTO.getAutoClose() && troubleDTO.getAutoClose() == 1L) {
            troubleDTO.setAutoCloseName(I18n.getLanguage("trouble.list.autoCloseName"));
          } else {
            troubleDTO.setAutoCloseName(I18n.getLanguage("trouble.list.closebyhand"));
          }
          troubleDTO.setCableTypeName(cableType);

          createTime = troubleDTO.getCreatedTime();
          if (troubleDTO.getCatchingTime() != null) {
            Date catchTime = troubleDTO.getCatchingTime();
            delta = (double) (createTime.getTime() - catchTime.getTime()) / (60 * 60 * 10) / 100;
            if (troubleDTO.getTimeCreateCfg() != null) {
              timeCreatCfg = Double.valueOf(troubleDTO.getTimeCreateCfg());
              if (delta.compareTo(timeCreatCfg) > 0) {
                troubleDTO.setCreateLateStr(I18n.getLanguage("incident.outofdate"));
              } else {
                troubleDTO.setCreateLateStr(I18n.getLanguage("incident.induedate"));
              }
            }
          }
          if (troubleDTO.getBeginTroubleTime() != null) {
            beginTroubleTime = troubleDTO.getBeginTroubleTime();

            if (troubleDTO.getEndTroubleTime() != null) {
              Date endTroubleTime = troubleDTO.getEndTroubleTime();
              troubleDTO.setAffectTime(String.valueOf(
                  (endTroubleTime.getTime() - beginTroubleTime.getTime()) / (60 * 60 * 1000)));
            }
          }

          if (troubleDTO.getClosedTime() != null) {
            fixTime = troubleDTO.getClosedTime();
          }
          troubleDTO.setFixTime(
              String.valueOf((fixTime.getTime() - createTime.getTime()) / (60 * 60 * 1000)));

          if (troubleDTO.getRemainTime() != null) {
            Double remainTime = Double.valueOf(troubleDTO.getRemainTime());
            if (remainTime < 0) {
              troubleDTO.setIsOverdueFixTime(I18n.getLanguage("incident.outofdate"));
            } else {
              troubleDTO.setIsOverdueFixTime(I18n.getLanguage("incident.induedate"));
            }
          }

          if (troubleDTO.getIsStationVip() != null && troubleDTO.getIsStationVip() == 1) {
            troubleDTO.setIsStationVipName(I18n.getLanguage("trouble.list.isStationVipName"));
          } else {
            troubleDTO.setIsStationVipName(I18n.getLanguage("trouble.list.normalStation"));
          }

          //TienNV comment
          //setTimeZoneForTrouble(troubleDTO, offset);
          //HaiNV20 add
          troubleDTO.setTransNetworkTypeName((troubleDTO.getTransNetworkTypeId() != null
              && troubleDTO.getTransNetworkTypeId() != -1) ? mapItem
              .get(troubleDTO.getTransNetworkTypeId().toString()).getItemName() : "");
          List<CatItemDTO> lstReasonType = new ArrayList<>();
          //Lay danh sach nguyen nhan tuong ung
          if (troubleDTO.getTypeId() != null && 2046 == (troubleDTO.getTypeId())) {
            if (lstReason != null && lstReason.size() > 0) {
              lstReasonType.addAll(lstReason);
            }
          } //Loai su co la dut cap (TD,IP)
          else if (troubleDTO.getTypeId() != null && 2047 == (troubleDTO.getTypeId())) {
            if (lstReason != null && lstReason.size() > 0) {
              lstReasonType.addAll(lstTroubleCap);
            }
          }
          if (lstReasonType.size() > 0) {
            for (CatItemDTO item : lstReasonType) {
              if (null != troubleDTO.getTransReasonEffectiveId() && -1 != troubleDTO
                  .getTransReasonEffectiveId()
                  && String.valueOf(item.getItemId())
                  .equals(String.valueOf(troubleDTO.getTransReasonEffectiveId()))) {
                troubleDTO.setTransReasonEffectiveName(item.getItemName());
                break;
              }
            }
            lstReasonType.clear();
          }

        }

      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  public Map<String, CatItemDTO> getMapItem() {
    List<String> lstCategory = new ArrayList<>();
    //Mang
    lstCategory.add(Constants.PROBLEM.PT_TYPE);
    //Loai node mang
    lstCategory.add(Constants.PROBLEM.PT_SUBCAT);
    //Muc anh huong
    lstCategory.add(TROUBLE.TT_IMPACT);
    //Muc do uu tien
    lstCategory.add(TROUBLE.PRIORITY);
    //Dich vu anh huong
    lstCategory.add(Constants.PROBLEM.AFFECT_SERVICE);
    //Giai phap tam thoi
    lstCategory.add(TROUBLE.TT_WORK_ARROUND);
    //Trang thai
    lstCategory.add(TROUBLE.TT_STATE);
    //Nhom rui ro
    lstCategory.add(TROUBLE.TT_RISK);
    //Loai node mang
    lstCategory.add(Constants.SUB_CATEGORY);
    //thanhlv12_add M?c m?ng
    lstCategory.add(TROUBLE.NETWORK_LEVEL);
    //Nhom canh bao
    lstCategory.add(Constants.PROBLEM.ALARM_GROUP);
    //Loai node mang
    lstCategory.add(TROUBLE.VENDOR);
    lstCategory.add(TROUBLE.TT_REJECT_CODE);
    lstCategory.add(TROUBLE.TT_CLOSE_CODE);

    lstCategory.add(TROUBLE.TT_SOLUTION_TYPE);
    //HaiNV20 Loai mang truyen dan
    lstCategory.add(TROUBLE.TT_TRANS_NETWORK_TYPE);
    //danh sach vai tro don vi
    lstCategory.add(TROUBLE.ROLE_LIST);
    lstCategory.add(TROUBLE.WARN_LEVEL);

    Map<String, CatItemDTO> mapItem = new HashMap<>();
    List<CatItemDTO> lstCatItem = catItemBusiness
        .getListCatItemDTOByListCategoryLE(I18n.getLocale(),
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            "1", lstCategory);
    List<CatItemDTO> lstPtSubcat = catItemBusiness
        .getListItemByCategoryAndParent(Constants.PROBLEM.PT_SUBCAT, null);
    if (lstCatItem != null && !lstCatItem.isEmpty()) {
      for (CatItemDTO itemDTO : lstCatItem) {
        mapItem.put(String.valueOf(itemDTO.getItemId()), itemDTO);
        mapItem.put(itemDTO.getItemCode(), itemDTO);
      }
    }

    if (lstPtSubcat != null && !lstPtSubcat.isEmpty()) {
      for (CatItemDTO itemDTO : lstPtSubcat) {
        mapItem.put(String.valueOf(itemDTO.getItemId()), itemDTO);
      }
    }
    return mapItem;
  }

  @Override
  public Datatable searchCrRelated(CrSearchDTO crSearchDTO) {
//    List<CrDTO> lstData = new ArrayList<>();
    Datatable datatable = new Datatable();
    try {
      String code = crSearchDTO.getCrNumber();
      String name = crSearchDTO.getTitle();
      boolean check = false;
      CrDTO crDTO = new CrDTO();
      crDTO.setSearchType("0");
      if (!StringUtils.isStringNullOrEmpty(code)) {
        crDTO.setCrNumber(code.trim());
        check = true;
      }

      if (!StringUtils.isStringNullOrEmpty(name)) {
        crDTO.setTitle(name.trim());
        check = true;
      }
      crDTO.setEarliestStartTimeTo(crSearchDTO.getEarliestStartTimeTo());
      crDTO.setEarliestStartTime(crSearchDTO.getEarliestStartTime());
      UserToken userToken = ticketProvider.getUserToken();
      crDTO.setUserLogin(userToken.getUserID().toString());
      crDTO.setUserLoginUnit(userToken.getDeptId().toString());
      crDTO.setState("0,1,2,3,4,5,6,8,11,12");

      if (check) {
        CrInsiteDTO crInsiteDTO = crDTO.toModelInsiteDTO();
        crInsiteDTO.setAction(INSERT_SOURCE.TT);
        crInsiteDTO.setPage(crSearchDTO.getPage());
        crInsiteDTO.setPageSize(crSearchDTO.getPageSize());
        datatable = crServiceProxy.onSearch(crInsiteDTO);
        if (datatable != null && datatable.getData() != null) {
          int page = datatable.getPages();
          int total = datatable.getTotal();
          List<LinkedHashMap> lstHasMap = (List<LinkedHashMap>) datatable.getData();
          List<CrDTO> lstOutSite = new ArrayList<>();
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
          if (lstHasMap != null) {
            lstHasMap.forEach(item -> {
              Object crId = item.get("crId");
              Object changeOrginatorName = item.get("changeOrginatorName");
              Object crNumber = item.get("crNumber");
              Object title = item.get("title");
              Object state = item.get("state");
              Object earliestStartTime = item.get("earliestStartTime");
              Object latestStartTime = item.get("latestStartTime");
              CrDTO dto = new CrDTO();
              dto.setCrId(crId == null ? null : crId.toString());
              dto.setChangeOrginatorName(
                  changeOrginatorName == null ? null : changeOrginatorName.toString());
              dto.setCrNumber(crNumber == null ? null : crNumber.toString());
              dto.setTitle(title == null ? null : title.toString());
              dto.setState(state == null ? null : state.toString());
              try {
                dto.setEarliestStartTime(StringUtils.isStringNullOrEmpty(earliestStartTime) ? null
                    : DateTimeUtils.date2ddMMyyyyHHMMss(df.parse(earliestStartTime.toString())));
                dto.setLatestStartTime(StringUtils.isStringNullOrEmpty(latestStartTime) ? null
                    : DateTimeUtils.date2ddMMyyyyHHMMss(df.parse(latestStartTime.toString())));
              } catch (Exception e) {
                log.error(e.getMessage(), e);
              }
              lstOutSite.add(dto);
            });
          }

          List<CrSearchDTO> crSearchDTOS = convertMapDataCR(lstOutSite);
          if (crSearchDTOS != null && crSearchDTOS.size() > 0) {
            crSearchDTOS.get(0).setPage(page);
            crSearchDTOS.get(0).setPageSize(crSearchDTO.getPageSize());
            crSearchDTOS.get(0).setTotalRow(total);
          }
          datatable.setData(crSearchDTOS);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return datatable;
  }

  @Override
  public List<CrSearchDTO> loadCrRelatedDetail(String crRelatedCode) {
    List<ConditionBean> lstCon = new ArrayList<>();
    lstCon
        .add(new ConditionBean("crNumber", crRelatedCode, Constants.NAME_EQUAL, Constants.STRING));
    List<CrDTO> dtoCRResult = crServiceProxy
        .getListCrByCondition(new BaseDto(lstCon, 0, 1, "", ""));
    return convertMapDataCR(dtoCRResult);
  }

  @Override
  public Datatable loadTroubleCrDTO(TroublesInSideDTO dto) {
    CrInsiteDTO crInsiteDTO = new CrInsiteDTO();
    crInsiteDTO.setPage(dto.getPage());
    crInsiteDTO.setPageSize(dto.getPageSize());
    crInsiteDTO.setSystemId(Long.valueOf(CrCreatedFromOtherSysDTO.SYSTEM.TT));
    crInsiteDTO.setObjectId(dto.getTroubleId());
    Datatable datatable = new Datatable();
    List<CrInsiteDTO> dtoCRResult = crServiceProxy.getListCRFromOtherSystemOfSR(crInsiteDTO);
    if (dtoCRResult != null && dtoCRResult.size() > 0) {
      datatable.setData(dtoCRResult);
      datatable.setTotal(dtoCRResult.get(0).getTotalRow());
      datatable.setPages(dto.getPage());
    }
    return datatable;
  }

  @Override
  public ResultInSideDto sendTicketToTKTU(TroublesInSideDTO dto) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    ResultDTO rDto = troublesServiceForCCBusiness.sendTicketToTKTU(dto);
    if (RESULT.SUCCESS.equalsIgnoreCase(rDto.getKey())) {
      dto.setIsSendTktu(1L);
      return updateTrouble(dto);
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
    }
    return resultInSideDto;
  }

  @Override
  public void insertCrCreatedFromOtherSystem(TroublesInSideDTO troublesDTO) {
    List<CrDTO> crDTOS = troublesDTO.getCrDTOS();
    if (crDTOS != null) {
      for (CrDTO crDTO : crDTOS) {
        CrCreatedFromOtherSysDTO ccfosdto = new CrCreatedFromOtherSysDTO();
        ccfosdto.setCrId(crDTO.getCrId());
        ccfosdto.setIsActive("1");
        ccfosdto.setObjectId(
            troublesDTO.getTroubleId() == null ? null : String.valueOf(troublesDTO.getTroubleId()));
        ccfosdto.setSystemId("3");
        crServiceProxy.insertCrCreatedFromOtherSystem(ccfosdto);
      }
    }
  }

  @Override
  public ResultInSideDto getAlarmClearGNOC(TroublesInSideDTO troubleDTO) throws Exception {
    GnocActionBO gnocActionBO = new GnocActionBO();
    Double offset = 0D;
    try {
      gnocActionBO.setAlarmId(troubleDTO.getAlarmId());
      offset = -1 * TimezoneContextHolder.getOffsetDouble();
      if (troubleDTO.getBeginTroubleTime() != null) {
        Date beginTroubleTime = new Date(
            troubleDTO.getBeginTroubleTime().getTime() + (long) (offset * 60 * 60 * 1000));
        gnocActionBO.setStartTime(
            DateTimeUtils.convertDateToString(beginTroubleTime, "yyyy/MM/dd HH:mm:ss"));

      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      gnocActionBO.setStartTime(DateTimeUtils
          .convertDateToString(troubleDTO.getBeginTroubleTime(), "yyyy/MM/dd HH:mm:ss"));
    }
    gnocActionBO.setTblAlarmClear(troubleDTO.getTblClear());
    gnocActionBO.setTblAlarmCurrent(troubleDTO.getTblCurr());
    gnocActionBO.setTblAlarmHis(troubleDTO.getTblHis());
    gnocActionBO.setTroubleCode(troubleDTO.getTroubleCode());
    gnocActionBO.setNtmsActionId(0L);
    Date now = new Date();
    String requestId = String.valueOf(now.getTime());

    List<ConditionBean> lstCondition = new ArrayList<>();
    lstCondition.add(
        new ConditionBean("insertSource", troubleDTO.getInsertSource(), Constants.NAME_EQUAL,
            Constants.STRING));
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    List<CfgServerNocDTO> lst = cfgServerNocRepository
        .getListCfgServerNocByCondition(lstCondition, 0, 100, "", "");
    if (lst == null || lst.isEmpty()) {
      return new ResultInSideDto(null, RESULT.ERROR, I18n.getLanguage("cfg.ws.noc"));
    } else {
      CfgServerNocDTO cfgServerNocDTO = lst.get(0);
      ResponseBO result = NocProWS
          .getNocProWS().getAlarmClearGNOC(requestId, cfgServerNocDTO, gnocActionBO);
      if (result != null && result.getStatus() == 1) {
        if (result.getAlarmBO() != null && !StringUtils
            .isStringNullOrEmpty(result.getAlarmBO().getEndTime())) {
          Date endTrouble = DateTimeUtils
              .convertStringToTime(result.getAlarmBO().getEndTime(), "yyyy/MM/dd HH:mm:ss");
          //Convert thoi gian ve thoi gian thi truong
          ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS,
              RESULT.SUCCESS);
          endTrouble = new Date(endTrouble.getTime() - (long) (offset * 60 * 60 * 1000));
          resultInSideDto.setObject(endTrouble);
          return resultInSideDto;
        } else {
          return new ResultInSideDto(null, RESULT.ERROR, result.getDescription());
        }
      } else {
        return new ResultInSideDto(null, RESULT.ERROR, I18n.getLanguage("trouble.notClearInNoc"));
      }
    }
  }

  @Override
  public Datatable getDataOfTabWO(TroublesInSideDTO troublesDTO) {
    WoDTOSearch woSearchDTO = new WoDTOSearch();
    UserToken userToken = ticketProvider.getUserToken();
    Datatable datatable = new Datatable();
    woSearchDTO.setWoSystemId(troublesDTO.getTroubleCode());
    woSearchDTO.setUserId(String.valueOf(userToken.getUserID()));
    woSearchDTO.setPage(1);
    woSearchDTO.setPageSize(Integer.MAX_VALUE);
    woSearchDTO.setSortName("woCode");
    woSearchDTO.setSortType("");
    List<WoDTOSearch> lstWo = woServiceProxy
        .getListDataSearchProxy(woSearchDTO);
    if (lstWo != null && lstWo.size() > 0) {
      int totalSize = lstWo.size();
      int size = troublesDTO.getPageSize();
      int page = troublesDTO.getPage();
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      List<WoDTOSearch> woSubList = (List<WoDTOSearch>) DataUtil.subPageList(lstWo, page, size);
      for (WoDTOSearch dto : woSubList) {
        dto.setStatusName(convertStatus2String(dto.getStatus()));
      }
      datatable.setData(woSubList);
      datatable.setTotal(totalSize);
      datatable.setPages(pageSize);
    }
    return datatable;
  }

  @Override
  public String checkWoRequiredClosed(TroublesInSideDTO troubleDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    try {
      CfgRequireHaveWoDTO haveWoDTO = new CfgRequireHaveWoDTO();
      haveWoDTO.setTypeId(troubleDTO.getTypeId());
      haveWoDTO.setAlarmGroupId(StringUtils.isStringNullOrEmpty(troubleDTO.getAlarmGroupId()) ? null
          : Long.valueOf(troubleDTO.getAlarmGroupId()));
      haveWoDTO.setReasonId(troubleDTO.getReasonId());
      String woTypeId = "";
      List<CfgRequireHaveWoDTO> lstHaveWoDTO = cfgRequireHaveWoRepository
          .getListCfgRequireHaveWoDTO(haveWoDTO, 0, 1, "", "");
      if (lstHaveWoDTO != null && !lstHaveWoDTO.isEmpty()) {
        woTypeId = lstHaveWoDTO.get(0).getWoTypeId();

      } else {
        haveWoDTO.setReasonId(-1L);
        lstHaveWoDTO = cfgRequireHaveWoRepository
            .getListCfgRequireHaveWoDTO(haveWoDTO, 0, 1, "", "");
        if (lstHaveWoDTO != null && !lstHaveWoDTO.isEmpty()) {
          woTypeId = lstHaveWoDTO.get(0).getWoTypeId();
        }
      }

      //danh sach WO
      if (!"".equals(woTypeId)) {
        //danh sach wo da tao
        WoDTOSearch condition = new WoDTOSearch();
        condition.setWoSystemId(troubleDTO.getTroubleCode());
        condition.setUserId(userToken.getUserID().toString());
        condition.setPage(1);
        condition.setPageSize(Integer.MAX_VALUE);
        condition.setSortName("woCode");
        condition.setSortType("");
        List<WoDTOSearch> lstWo = woServiceProxy
            .getListDataSearchProxy(condition);
        woTypeId = "," + woTypeId + ",";
        if (lstWo != null && !lstWo.isEmpty()) {
          for (WoDTOSearch dTOSearch : lstWo) {
            woTypeId = woTypeId.replace("," + dTOSearch.getWoTypeId() + ",", ",");
          }
        }
        List<String> lstWoTypeId = Arrays.asList(woTypeId.split(","));
        if (lstWoTypeId != null && !lstWoTypeId.isEmpty()) { //danh sach loai wo can tao
          return getMapWoType(lstWoTypeId);
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return "";
  }

  @Override
  public List<WoDTOSearch> getListDataSearchWo(WoDTOSearch woDTOSearch) {
    try {
      woDTOSearch.setPage(1);
      woDTOSearch.setPageSize(Integer.MAX_VALUE);
      woDTOSearch.setSortName("woCode");
      woDTOSearch.setSortType("");
      return woServiceProxy.getListDataSearchProxy(woDTOSearch);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<TroublesDTO> countTroubleByStation(String stationCode, String startTime,
      String endTime, String priority, int type) {
    return troublesRepository
        .countTroubleByStation(stationCode, startTime, endTime, priority, type);
  }

  private List<CrSearchDTO> convertMapDataCR(List<CrDTO> dtoCRResult) {
    List<CrSearchDTO> crSearchDTOS = new ArrayList<>();
    if (dtoCRResult != null) {
      for (CrDTO crDTO : dtoCRResult) {
        crDTO.setState(convertStatusCR(crDTO.getState()));
      }
      for (CrDTO crDTO : dtoCRResult) {
        CrSearchDTO crSearchDTO = new CrSearchDTO();
        crSearchDTO.setCrId(crDTO.getCrId());
        crSearchDTO.setChangeOrginatorName(crDTO.getChangeOrginatorName());
        crSearchDTO.setCrNumber(crDTO.getCrNumber());
        crSearchDTO.setTitle(crDTO.getTitle());
        crSearchDTO.setState(crDTO.getState());
        crSearchDTO.setEarliestStartTime(crDTO.getEarliestStartTime());
        crSearchDTO.setLatestStartTime(crDTO.getLatestStartTime());
        crSearchDTOS.add(crSearchDTO);
      }
    }

    return crSearchDTOS;
  }

  private void showTicketAfterUpdate(UserToken userToken, ResultInSideDto result,
      TroublesInSideDTO oldDto,
      TroublesInSideDTO newDTO) throws Exception {
    if (RESULT.SUCCESS.equals(result.getKey())) {
      Map<String, String> smsGeneric = updateSmsContent(newDTO, oldDto);
      String smsContent = smsGeneric.get("smsContent");
      String smsUnit = smsGeneric.get("smsUnit");
      CatItemDTO stateItem = catItemBusiness.getCatItemById(newDTO.getState());
      CatItemDTO priorityItem = catItemBusiness
          .getCatItemById(newDTO.getPriorityId());
      if (!StringUtils.isStringNullOrEmpty(smsContent) && !StringUtils
          .isStringNullOrEmpty(smsUnit)) {
        newDTO.setStateName(stateItem.getItemName());
        newDTO.setPriorityName(priorityItem.getItemName());
        newDTO.setUnitMoveName(userToken.getUserName());
        messagesBusiness.insertSMSMessageForUnitAllUser(smsContent, smsUnit, newDTO, null);
      }
    }
  }

  public Map<String, String> updateSmsContent(TroublesInSideDTO newDTO, TroublesInSideDTO oldDTO) {
    String smsContent = "";
    String smsUnit = "";
    if (oldDTO == null) {
      smsContent = "message.createTrouble";
      smsUnit =
          newDTO.getReceiveUnitId() == null ? null : String.valueOf(newDTO.getReceiveUnitId());
    } else {
      //Truong hop tahy doi trang thai
      if (!StringUtils.isStringNullOrEmpty(newDTO.getState())
          && !StringUtils.isStringNullOrEmpty(oldDTO.getState())
          && newDTO.getState() != oldDTO.getState()) {

        String oldState = catItemBusiness.getCatItemById(oldDTO.getState())
            .getItemCode();
        //da tiep nhan, cho tiep nhan --> reject
        if (newDTO.getStateName().equals(Constants.TT_STATE.REJECT) && (
            oldState.equals(Constants.TT_STATE.Waiting_Receive)
                || oldState.equals(Constants.TT_STATE.QUEUE))) {
          smsContent = "message.waitingReceiveToReject";
          smsUnit =
              newDTO.getCreateUnitId() == null ? null : String.valueOf(newDTO.getCreateUnitId());
        } else if (newDTO.getStateName().equals(Constants.TT_STATE.Wait_for_Deferred) && oldState
            .equals(Constants.TT_STATE.QUEUE)) { //da tiep nhan -> yeu cau tam dong
          smsContent = "message.queueToWaitingDeferred";
          smsUnit =
              newDTO.getCreateUnitId() == null ? null : String.valueOf(newDTO.getCreateUnitId());
        } else if (newDTO.getStateName().equals(Constants.TT_STATE.QUEUE) && oldState
            .equals(Constants.TT_STATE.Wait_for_Deferred)) { //yeu cau tam dong -> da tiep nha
          smsContent = "message.waitingDeferredToQueue";
          smsUnit =
              newDTO.getReceiveUnitId() == null ? null : String.valueOf(newDTO.getReceiveUnitId());
        } else if (newDTO.getStateName().equals(Constants.TT_STATE.CLEAR) && (
            oldState.equals(Constants.TT_STATE.SOLUTION_FOUND)
                || TT_STATE.Waiting_Receive.equals(oldState)
                || TT_STATE.QUEUE.equals(oldState)
        )) {
          smsContent = "message.solutionFoundToClear";
          smsUnit =
              newDTO.getCreateUnitId() == null ? null : String.valueOf(newDTO.getCreateUnitId());
        } else if (newDTO.getStateName().equals(Constants.TT_STATE.Waiting_Receive) && oldState
            .equals(Constants.TT_STATE.CLEAR)) { //hoan thanh --> cho tiep nhan
          smsContent = "message.clearToWaitingReceive";
          smsUnit =
              newDTO.getReceiveUnitId() == null ? null : String.valueOf(newDTO.getReceiveUnitId());
        } else if (newDTO.getStateName().equals(Constants.TT_STATE.CLOSED_Not_KEDB) && oldState
            .equals(Constants.TT_STATE.CLEAR)) { //hoan thanh -> dong chua co kedb
          smsContent = "message.clearToCloseNotKedb";
          smsUnit =
              newDTO.getCreateUnitId() == null ? null : String.valueOf(newDTO.getCreateUnitId());
        } else if (newDTO.getStateName().equals(Constants.TT_STATE.Waiting_Receive) && oldState
            .equals(Constants.TT_STATE.OPEN)) { // du thao --> cho tiep nhan
          smsContent = "message.createTrouble";
          smsUnit =
              newDTO.getReceiveUnitId() == null ? null : String.valueOf(newDTO.getReceiveUnitId());
        }
      }
      // tu choi tiep nhan
      if (newDTO.getCheckbox() != null && "2".equals(newDTO.getCheckbox())) {
        smsContent = "message.waitingReceiveToRejectByUnitMove";
        smsUnit = newDTO.getUnitMove() == null ? null : String.valueOf(newDTO.getUnitMove());
        //giao cho don vi khac
      } else if ("3".equals(newDTO.getCheckbox()) || "9".equals(newDTO.getCheckbox())) {
        smsContent = "message.createTrouble";
        smsUnit =
            newDTO.getReceiveUnitId() == null ? null : String.valueOf(newDTO.getReceiveUnitId());
      }
    }
    Map<String, String> response = new HashMap<>();
    response.put("smsContent", smsContent);
    response.put("smsUnit", smsUnit);
    return response;
  }

  public String convertStatusCR(String stateId) {
    if (stateId != null) {
      return I18n.getLanguage("cr.state." + stateId);
    }
    return "";
  }

  public String getMapWoType(List<String> lstWoTypeId) {
    String result = "";
    Map<String, WoTypeInsideDTO> map = new HashMap<>();
    List<WoTypeInsideDTO> lstWoType = woCategoryServiceProxy
        .getListWoTypeDTO(new WoTypeInsideDTO());
    for (WoTypeInsideDTO woTypeDTO : lstWoType) {
      map.put(String.valueOf(woTypeDTO.getWoTypeId()), woTypeDTO);
    }
    for (String woTypeId : lstWoTypeId) {
      if (woTypeId != null && !"".equals(woTypeId)) {
        result = result + "\n " + map.get(woTypeId.trim()).getWoTypeName();
      }
    }
    if (result.length() > 0) {
      result = result.substring(1);
    }
    return result;
  }

  @Override
  public Datatable searchParentTTForCR(TroublesInSideDTO troublesInSideDTO) {
    ConditionBean conditionBean1 = new ConditionBean("categoryId", "2", Constants.NAME_EQUAL,
        Constants.NUMBER);
    ConditionBean conditionBean2 = new ConditionBean("itemCode", "QUEUE,SOLUTION FOUND",
        Constants.NAME_IN, Constants.STRING);
    List<ConditionBean> lstConditionBeans = new ArrayList<ConditionBean>();
    lstConditionBeans.add(conditionBean1);
    lstConditionBeans.add(conditionBean2);
    ConditionBeanUtil.sysToOwnListCondition(lstConditionBeans);
    List<CatItemDTO> lstCatItemDTO = catItemRepository
        .getListCatItemByCondition(lstConditionBeans, 0, 10, Constants.ASC, "itemName");
    List<String> lstState = new ArrayList<>();
    lstState.add("5");
    lstState.add("8");
    if (lstCatItemDTO != null && !lstCatItemDTO.isEmpty()) {
      lstState.clear();
      for (CatItemDTO dto : lstCatItemDTO) {
        lstState.add(String.valueOf(dto.getItemId()));
      }
    }
    troublesInSideDTO.setLstState(lstState);
    return troublesRepository.searchParentTTForCR(troublesInSideDTO);
  }

  @Override
  public List<TroublesInSideDTO> countTicketByShift(TroublesInSideDTO troublesDTO) {
    return troublesRepository.countTicketByShift(troublesDTO);
  }

  @Override
  public ResultDTO onUpdateTroubleMobile(TroublesDTO tForm) {
    ResultDTO result = new ResultDTO();
    try {
      result = validate(tForm);
      if (RESULT.SUCCESS.equals(result.getKey())) {
      } else if (RESULT.SUCCESS_NOT_WO.equals(result.getKey())) {
        result.setKey(RESULT.SUCCESS);
        return result;
      } else {
        return result;
      }
      TroublesDTO troubleDTO = troublesRepository
          .getTroubleDTO(null, tForm.getTroubleCode(), null, null, null, null, null)
          .toModelOutSide();
      troubleDTO.setProcessingUnitName(tForm.getReceiveUnitName());
      troubleDTO.setProcessingUnitId(tForm.getReceiveUnitId());
      troubleDTO.setProcessingUserName(tForm.getReceiveUserName());
      troubleDTO.setProcessingUserPhone(tForm.getProcessingUserPhone());
      troubleDTO.setTotalPendingTimeGnoc(tForm.getTotalPendingTimeGnoc());
      troubleDTO.setTotalProcessTimeGnoc(tForm.getTotalProcessTimeGnoc());
      troubleDTO.setEvaluateGnoc(tForm.getEvaluateGnoc());
      troubleDTO.setStateWo(tForm.getStateWo());
      troubleDTO.setNumPending(tForm.getNumPending());
      troubleDTO.setSpmName("MOBILE");
      troubleDTO.setWorkLog(tForm.getWorkLog());
      troubleDTO.setIsCheck(tForm.getIsCheck());
      tForm.setSpmName("MOBILE");
      //TH1, trong TH WO tam dong (9), Incident se chi can tu dong cap nhat trang thai ticket sang cho tam dong.
      if (tForm.getStateName().equals(Constants.TT_STATE.DEFERRED)) { //tam dong
        if ("7".equals(troubleDTO.getState())) {
          result.setMessage(RESULT.SUCCESS);
          result.setKey(RESULT.SUCCESS);
          return result;
        }
        troubleDTO.setDeferredTime(tForm.getDeferredTime());
        troubleDTO.setDeferredReason(tForm.getDeferredReason()); //Ly do tam dong
        troubleDTO.setState("7"); //Chuyen sang tam dong
        troubleDTO.setStateName(Constants.TT_STATE.DEFERRED);
        troubleDTO.setCheckbox("7");

        ResultInSideDto res = onUpdateTrouble(troubleDTO.toModelInSide(), false);
        result.setId(res.getIdValue());
        result.setKey(res.getKey());
        result.setMessage(res.getMessage());

        //TH2, trong TH WO thuc hien dong, Incident chi can nhan tin cho don vi.
      } else if (tForm.getStateName().equals(Constants.TT_STATE.CLOSED)) {
        if (troubleDTO.getIsFailDevice() == null || "0".equals(troubleDTO.getIsFailDevice())) {
          troubleDTO.setReasonLv1Id(tForm.getReasonLv1Id());
          troubleDTO.setReasonLv1Name(tForm.getReasonLv1Name());
          troubleDTO.setReasonLv2Id(tForm.getReasonLv2Id());
          troubleDTO.setReasonLv2Name(tForm.getReasonLv2Name());
          troubleDTO.setReasonLv3Id(tForm.getReasonLv3Id());
          troubleDTO.setReasonLv3Name(tForm.getReasonLv3Name());
          troubleDTO.setReasonOverdueId(tForm.getReasonOverdueId());
          troubleDTO.setReasonOverdueId2(tForm.getReasonOverdueId2());
          troubleDTO.setReasonOverdueName(tForm.getReasonOverdueName());
          troubleDTO.setReasonOverdueName2(tForm.getReasonOverdueName2());
        }

        troubleDTO.setEndTroubleTime(tForm.getEndTroubleTime());
        troubleDTO.setReasonId(tForm.getReasonId());
        troubleDTO.setReasonName(tForm.getReasonName());
        troubleDTO.setSolutionType(tForm.getSolutionType());
        troubleDTO.setWorkArround(tForm.getWorkArround());
        troubleDTO.setRootCause(tForm.getRootCause());
        troubleDTO.setCheckbox(tForm.getCheckbox());

        if (("1".equals(troubleDTO.getIsTickHelp()) && !"9".equals(troubleDTO.getState()) && !"4"
            .equals(troubleDTO.getState()))
            || "10".equals(troubleDTO.getState()) || "11".equals(troubleDTO.getState())) {
          result.setMessage(RESULT.SUCCESS);
          result.setKey(RESULT.SUCCESS);
          return result;

        } else {
          troubleDTO.setCheckbox("4");
          if (DataUtil.isNullOrEmpty(troubleDTO.getRelatedKedb())) { //chua co kedb
            troubleDTO.setState("10");
            troubleDTO.setStateName(Constants.TT_STATE.CLOSED_Not_KEDB);
          } else { //co kedb
            troubleDTO.setState("11");
            troubleDTO.setStateName(Constants.TT_STATE.CLOSED);
          }
        }
        ResultInSideDto res = onUpdateTrouble(troubleDTO.toModelInSide(), false);
        result.setId(res.getIdValue());
        result.setMessage(res.getMessage());
        if ("SUCCESS".equals(res.getKey())) {
          String smsContent = "message.closed.WO";
          sendSms(troubleDTO.toModelInSide(), smsContent);
        }
        //TH3 mo tam dong
      } else if (tForm.getStateName().equals(Constants.TT_STATE.QUEUE)) { //mo tam dong
        if (!"7".equals(troubleDTO.getState())) {
          result.setMessage(RESULT.SUCCESS);
          result.setKey(RESULT.SUCCESS);
          return result;
        }
        //Lay thong tin nguoi xu ly thuoc don vi nhan de nhan tin
        troubleDTO.setState("5"); //Mo ticket tu tam dong sang cho tiep nhan.
        troubleDTO.setStateName(Constants.TT_STATE.QUEUE);
        troubleDTO.setProcessingUnitId(tForm.getReceiveUnitId());
        troubleDTO.setProcessingUnitName(tForm.getReceiveUnitName());
        troubleDTO.setProcessingUserName(tForm.getReceiveUserName());
        troubleDTO.setCheckbox("6");
        String smsContent = "message.open.defer.WO";
        sendSms(troubleDTO.toModelInSide(), smsContent);
        //He thong thuc hien mo tam dong

        ResultInSideDto res = onUpdateTrouble(troubleDTO.toModelInSide(),
            false); //Update Ticket state to Deffer.
        result.setId(res.getIdValue());
        result.setMessage(res.getMessage());
      } else if ("UPDATE".equals(tForm.getStateName())) {
        //ghi worklog
        TroublesEntity troubles = troublesRepository
            .findTroublesById(Long.valueOf(troubleDTO.getTroubleId()));

        troubleDTO.setWorkLog(tForm.getWorkLog().trim());
        troubleDTO.setCreateUnitName(tForm.getReceiveUnitName());
        troubleDTO.setCreateUserName(tForm.getReceiveUserName());
        troubleDTO.setLastUpdateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        onInsertWorkLog(troubleDTO.toModelInSide());

        troubles.setWorkLog(
            (troubles.getWorkLog() == null ? "" : (troubles.getWorkLog() + " \n ")) + DateTimeUtils
                .getSysDateTime() + " " + tForm.getWorkLog().trim());

        //cap nhat sang SPM
        tForm.setProcessingUnitName(tForm.getReceiveUnitName());
        tForm.setProcessingUnitId(tForm.getReceiveUnitId());
        tForm.setProcessingUserName(tForm.getReceiveUserName());
        tForm.setProcessingUserPhone(tForm.getProcessingUserPhone());
        //day sang SPM
        com.viettel.soc.spm.service.ResultDTO resSpm = new com.viettel.soc.spm.service.ResultDTO();
        if (troubles.getInsertSource() != null && troubles.getInsertSource()
            .equals(INSERT_SOURCE.SPM)) {
          CatItemDTO stateItem = catItemRepository.getCatItemById(troubles.getState());
          StringUtils.printLogData("Request updateSpmAction StateName in UPDATE", tForm,
              TroublesDTO.class);
          resSpm = troubleSpmUtils
              .updateSpmAction(tForm.toModelInSide(), troubles, stateItem.getItemName());
          StringUtils
              .printLogData("Result updateSpmAction StateName in UPDATE", resSpm, ResultDTO.class);
        } else if (troubles.getInsertSource() != null && troubles.getInsertSource()
            .equals(INSERT_SOURCE.SPM) && troubles.getInsertSource()
            .equals(INSERT_SOURCE.SPM_VTNET)) {
          CfgServerNocDTO cfgServerNocDTO = new CfgServerNocDTO();
          cfgServerNocDTO.setInsertSource(troubles.getInsertSource());
          cfgServerNocDTO = troublesRepository.getServerDTO(cfgServerNocDTO);

          troubleBccsUtils
              .updateComp(1, troubleDTO.toModelInSide().toEntity(), tForm, cfgServerNocDTO);
        } else if (troubles.getInsertSource() != null && troubles.getInsertSource()
            .contains(INSERT_SOURCE.BCCS)) {
          CfgServerNocDTO cfgServerNocDTO = new CfgServerNocDTO();
          cfgServerNocDTO.setInsertSource(troubles.getInsertSource());
          cfgServerNocDTO = troublesRepository.getServerDTO(cfgServerNocDTO);
          troubleBccsUtils.updateCompProcessing(messagesRepository, 1, troubleDTO.toModelInSide(),
              cfgServerNocDTO);
        }

        if (resSpm.getKey() != null && !RESULT.SUCCESS.equals(resSpm.getKey())) {
          result.setKey(RESULT.ERROR);
          result.setMessage(resSpm.getMessage());
          return result;
        }

        // ThanhLV12_ Thuc hien ghi log lich su _start
        TroubleActionLogsEntity troubleActionLogsDTO = new TroubleActionLogsEntity(null,
            I18n.getLanguage("common.btn.update"),
            new Date(),
            tForm.getProcessingUnitId() == null ? null : Long.valueOf(tForm.getProcessingUnitId()),
            tForm.getProcessingUserId() == null ? null : Long.valueOf(tForm.getProcessingUserId()),
            I18n.getLanguage("common.btn.update"), troubles.getTroubleId(),
            tForm.getProcessingUnitName(), tForm.getProcessingUserName(),
            troubles.getState(), tForm.getTroubleCode(),
            tForm.getStateName(), null, null);

        if ("SPM_VTNET".equals(troubles.getInsertSource())
            || "SPM".equals(troubles.getInsertSource())) {
          troubleActionLogsDTO.setInsertSource(troubles.getInsertSource());
          tForm.setState(troubles.getState().toString());
          ActionInfoDTO info = new ActionInfoDTO(tForm);
          troubleActionLogsDTO.setActionInfo(info.toString());
        }

        troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO);
        // ThanhLV12_ Thuc hien ghi log lich su _end

        troublesRepository.updateTroubles(troubles);
      } else { //update tich help
        TroublesEntity troubles = troublesRepository
            .findTroublesById(Long.valueOf(troubleDTO.getTroubleId()));
        troubles.setIsTickHelp(1L);
        String workLog = I18n.getLanguage("tt.tich.help") + " " + tForm.getWorkLog().trim();
        if (StringUtils.isNotNullOrEmpty(tForm.getErrorCode()) && "SPM"
            .equals(troubles.getInsertSource())) {
          workLog = workLog + " " + tForm.getConcave();
          troubles.setConcave(tForm.getConcave());
          troubles.setErrorCode(Long.parseLong(tForm.getErrorCode()));
        }

        Long numHelp = troubles.getNumHelp() == null ? 1L : troubles.getNumHelp() + 1;
        if (troubles.getNumHelp() == null) { //lan dau tich help cap nhat lai thoi gian
          troubles.setAssignTimeTemp(new Date());
          CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO = new CfgTimeTroubleProcessDTO();
          cfgTimeTroubleProcessDTO.setTypeId(troubles.getTypeId());
          cfgTimeTroubleProcessDTO.setSubCategoryId(Long.parseLong(troubles.getAlarmGroup()));
          cfgTimeTroubleProcessDTO.setPriorityId(troubles.getPriorityId());
          cfgTimeTroubleProcessDTO.setCountry(null);
          CfgTimeTroubleProcessDTO config = ttCategoryServiceProxy
              .getConfigTimeTroubleProcess(cfgTimeTroubleProcessDTO);
          if (config != null) {
            troubles.setTimeProcess(
                config.getProcessTtTime() == null ? null
                    : Double.valueOf(config.getProcessTtTime()));
          }
        }
        troubles.setClearTime(null);
        troubles.setNumHelp(numHelp); //so lan help
        troubles.setState(5L);

        troubles.setWorkLog(
            (troubles.getWorkLog() == null ? "" : (troubles.getWorkLog() + "\n")) + tForm
                .getWorkLog()
                .trim());
        troublesRepository.updateTroubles(troubles);
        //dinh kem file
        troubleDTO.setFileDocumentByteArray(tForm.getFileDocumentByteArray());
        troubleDTO.setArrFileName(tForm.getArrFileName());
        onInsertTroubleFile(troubleDTO.toModelInSide(), null);
        //insert work log
        troubleDTO.setWorkLog(workLog);
        troubleDTO.setCreateUnitName(tForm.getReceiveUnitName());
        troubleDTO.setCreateUserName(tForm.getReceiveUserName());
        troubleDTO.setLastUpdateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        onInsertWorkLog(troubleDTO.toModelInSide());

        // ThanhLV12_ thuc hien luu them log action_start
        TroubleActionLogsEntity troubleActionLogsDTO = new TroubleActionLogsEntity(null,
            I18n.getLanguage("common.btn.update"),
            new Date(),
            tForm.getReceiveUnitId() == null ? null : Long.valueOf(tForm.getReceiveUnitId()),
            tForm.getReceiveUserId() == null ? null : Long.valueOf(tForm.getReceiveUserId()),
            I18n.getLanguage("common.btn.update"), troubles.getTroubleId(),
            tForm.getProcessingUnitName(), tForm.getProcessingUserName(),
            troubles.getState(),
            tForm.getTroubleCode(), tForm.getStateName(), null, null);

        if ("SPM_VTNET".equals(troubles.getInsertSource())
            || "SPM".equals(troubles.getInsertSource())) {
          troubleActionLogsDTO.setInsertSource(troubles.getInsertSource());
          tForm.setState(troubles.getState().toString());
          ActionInfoDTO info = new ActionInfoDTO(tForm);
          troubleActionLogsDTO.setActionInfo(info.toString());
        }

        troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO);

        // ThanhLV12_ thuc hien luu them log action_end

        //day sang SPM
        //cap nhat sang SPM
        if (troubles.getInsertSource() != null && troubles.getInsertSource()
            .contains(Constants.INSERT_SOURCE.SPM)) {
          tForm.setProcessingUnitName(tForm.getReceiveUnitName());
          tForm.setProcessingUnitId(tForm.getReceiveUnitId());
          tForm.setProcessingUserName(tForm.getReceiveUserName());
          tForm.setProcessingUserPhone(tForm.getProcessingUserPhone());
          CatItemDTO stateItem = catItemRepository.getCatItemById(troubles.getState());
          StringUtils
              .printLogData("Request updateSpmAction StateName # all", tForm, TroublesDTO.class);
          com.viettel.soc.spm.service.ResultDTO resSpm = new com.viettel.soc.spm.service.ResultDTO();
          resSpm = troubleSpmUtils
              .updateSpmAction(tForm.toModelInSide(), troubles, stateItem.getItemName());
          StringUtils
              .printLogData("Result updateSpmAction StateName # all", resSpm, ResultDTO.class);
        }
        // nhan tin tich help
        String smsContent = "message.help.from.wo";
        troubleDTO.setTroubleCode(troubles.getTroubleCode());
        troubleDTO.setTroubleName(troubles.getTroubleName());
        sendSms(troubleDTO.toModelInSide(), smsContent);

        result.setMessage(RESULT.SUCCESS);
        result.setKey(RESULT.SUCCESS);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw new RuntimeException(ex);
    }
    return result;
  }

  @Override
  public ResultDTO onClosetroubleFromWo(TroublesDTO troublesDTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      if (troublesDTO == null) {
        resultDTO.setKey(RESULT.ERROR);
        resultDTO.setMessage(I18n.getLanguage("wo.close.trouble.troublesDTO"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getTroubleCode())) {
        resultDTO.setKey(RESULT.ERROR);
        resultDTO.setMessage("TroubleCode " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      TroublesDTO trouble = troublesRepository
          .getTroubleDTO(null, troublesDTO.getTroubleCode(), null, null, null, null, null)
          .toModelOutSide();
      if (trouble == null || StringUtils.isStringNullOrEmpty(trouble.getTroubleCode())) {
        resultDTO.setKey(RESULT.ERROR);
        resultDTO.setMessage(I18n.getLanguage("incident.invalid.ticket"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getState())) {
        resultDTO.setKey(RESULT.ERROR);
        resultDTO.setMessage("State " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getReasonId())) {
        resultDTO.setKey(RESULT.ERROR);
        resultDTO.setMessage("ReasonId " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getReasonName())) {
        resultDTO.setKey(RESULT.ERROR);
        resultDTO.setMessage("ReasonName " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getReceiveUnitId())) {
        resultDTO.setKey(RESULT.ERROR);
        resultDTO.setMessage("ReceiveUnitId " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getReceiveUserId())) {
        resultDTO.setKey(RESULT.ERROR);
        resultDTO.setMessage("ReceiveUserId " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getReceiveUnitName())) {
        resultDTO.setKey(RESULT.ERROR);
        resultDTO.setMessage("ReceiveUnitName " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getReceiveUserName())) {
        resultDTO.setKey(RESULT.ERROR);
        resultDTO.setMessage("ReceiveUserName " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (!"5".equals(troublesDTO.getState())) {
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getEndTroubleTime())) {
          resultDTO.setKey(RESULT.ERROR);
          resultDTO.setMessage("EndTroubleTime " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return resultDTO;
        }
        try {
          DateTimeUtils.convertStringToDateTime(troublesDTO.getEndTroubleTime());
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          resultDTO.setKey(RESULT.ERROR);
          resultDTO
              .setMessage("EndTroubleTime " + I18n.getLanguage("wo.close.trouble.date.format"));
          return resultDTO;
        }
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getRootCause())) {
          resultDTO.setKey(RESULT.ERROR);
          resultDTO.setMessage("RootCause " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return resultDTO;
        }
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getSolutionType())) {
          resultDTO.setKey(RESULT.ERROR);
          resultDTO.setMessage("SolutionType " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return resultDTO;
        }
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getWorkArround())) {
          resultDTO.setKey(RESULT.ERROR);
          resultDTO.setMessage("WorkArround " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return resultDTO;
        }
      } else if ("5".equals(troublesDTO.getState())) {
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getCreateUnitId())) {
          troublesDTO.setCreateUnitId(troublesDTO.getReceiveUnitId());
        }
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getCreateUnitName())) {
          troublesDTO.setCreateUnitName(troublesDTO.getReceiveUnitName());
        }
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getCreateUserId())) {
          troublesDTO.setCreateUserId(troublesDTO.getCreateUserId());
        }
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getCreateUserName())) {
          troublesDTO.setCreateUserName(troublesDTO.getReceiveUserName());
        }
        if (StringUtils.isStringNullOrEmpty(trouble.getReasonId())) {
          trouble.setReasonId(troublesDTO.getReasonId());
        }

        if (StringUtils.isStringNullOrEmpty(trouble.getReasonName())) {
          trouble.setReasonName(troublesDTO.getReasonName());
        }
        troublesDTO.setTroubleId(trouble.getTroubleId());
        troublesDTO.setLastUpdateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        String worklog = I18n.getLanguage("update.reason") + " " + troublesDTO.getReasonName();
        troublesDTO.setWorkLog(worklog);
        String temp = onInsertWorkLog(troublesDTO.toModelInSide());

        trouble.setRootCause(troublesDTO.getRootCause());
        trouble.setWorkLog(
            (trouble.getWorkLog() == null ? "" : (trouble.getWorkLog() + " \n ")) + DateTimeUtils
                .getSysDateTime() + " " + worklog);
        trouble.setLastUpdateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        troublesRepository.updateTroubles(trouble.toModelInSide().toEntity());

        resultDTO.setKey(temp);
        resultDTO.setMessage(temp);
        return resultDTO;
      }

      resultDTO = closetroubleFromWo(troublesDTO, trouble);
      if (resultDTO != null && resultDTO.getKey().equals(Constants.RESULT.SUCCESS)) {
        String smsContent = "message.closed.WO";
        sendSms(trouble.toModelInSide(), smsContent);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setKey(RESULT.ERROR);
      resultDTO.setMessage(ex.toString());
      return resultDTO;
    }
    return resultDTO;
  }

  @Override
  public ResultDTO checkAlarmNOC(String troubleCode, String typeWo) {
    ResultDTO resultDTO = new ResultDTO();
    if (StringUtils.isStringNullOrEmpty(troubleCode)) {
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(I18n.getLanguage("incident.not.input.troubleCode"));
      return resultDTO;
    }
    if (StringUtils.isStringNullOrEmpty(typeWo)) {
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage("TypeWo " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return resultDTO;
    }
    try {
      TroublesDTO troubleDTO = new TroublesDTO();
      TroublesInSideDTO troublesInSideDTOTmp = troublesRepository
          .getTroubleDTO(null, troubleCode, null, null, null, null, null);
      if (troublesInSideDTOTmp != null) {
        troubleDTO = troublesInSideDTOTmp.toModelOutSide();
      }
      if (troubleDTO == null || StringUtils.isStringNullOrEmpty(troubleDTO.getTroubleId())) {
        resultDTO
            .setFinishTime(DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
        resultDTO.setKey(RESULT.SUCCESS);
        resultDTO.setMessage("");
        return resultDTO;
      }
      CatCfgClosedTicketDTO ccctdto = new CatCfgClosedTicketDTO();
      ccctdto.setAlarmGroupId(troubleDTO.getAlarmGroupId());
      ccctdto.setWoTypeId(typeWo);
      ccctdto.setTypeId(troubleDTO.getTypeId());
      List<CatCfgClosedTicketDTO> lstClosedTicketDTO = catCfgClosedTicketRepository
          .getListCatCfgClosedTicketDTO(ccctdto, 0, 100, "", "");
      if (lstClosedTicketDTO != null && !lstClosedTicketDTO.isEmpty()) {
        resultDTO.setId("1");
      } else {
        resultDTO.setId("0");
      }
      if (troubleDTO.getInsertSource() == null || !troubleDTO.getInsertSource().contains("NOC")) {
        resultDTO.setKey(RESULT.SUCCESS);
        resultDTO.setMessage("");
        return resultDTO;
      }
      GnocActionBO gnocActionBO = new GnocActionBO();
      gnocActionBO.setAlarmId(Long.valueOf(troubleDTO.getAlarmId()));
      gnocActionBO.setStartTime(DateTimeUtils.convertDateToString(
          DateTimeUtils.convertStringToDateTime(troubleDTO.getBeginTroubleTime()),
          "yyyy/MM/dd HH:mm:ss"));
      gnocActionBO.setTblAlarmClear(troubleDTO.getTblClear());
      gnocActionBO.setTblAlarmCurrent(troubleDTO.getTblCurr());
      gnocActionBO.setTblAlarmHis(troubleDTO.getTblHis());
      gnocActionBO.setTroubleCode(troubleDTO.getTroubleCode());
      gnocActionBO.setNtmsActionId(0L);
      Date now = new Date();
      String requestId = String.valueOf(now.getTime());

      List<ConditionBean> lstCondition = new ArrayList<>();
      lstCondition.add(
          new ConditionBean("insertSource", troubleDTO.getInsertSource(), "NAME_EQUAL", "STRING"));
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      List<CfgServerNocDTO> lst = cfgServerNocRepository
          .getListCfgServerNocByCondition(lstCondition, 0, 100, "", "");
      if (lst != null && !lst.isEmpty()) {
        ResponseBO result = WSNocPro.getWsNocPro()
            .getAlarmClearGNOC(requestId, lst.get(0), gnocActionBO);
        if (result != null && result.getStatus() == 1) {
          if (result.getAlarmBO() != null && StringUtils
              .isNotNullOrEmpty(result.getAlarmBO().getEndTime())) {
            Date endTrouble = DateTimeUtils
                .convertStringToTime(result.getAlarmBO().getEndTime(), "yyyy/MM/dd HH:mm:ss");
            resultDTO.setFinishTime(
                DateTimeUtils.convertDateToString(endTrouble, "dd/MM/yyyy HH:mm:ss"));
            resultDTO.setKey(RESULT.SUCCESS);
            resultDTO.setMessage("NOC");
          } else {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage(result.getDescription());
          }
        } else {
          resultDTO.setKey(RESULT.SUCCESS);
          resultDTO.setMessage("NOC");
        }
      } else {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(I18n.getLanguage("cfg.ws.noc"));
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(ex.toString());
    }
    return resultDTO;
  }

  @Override
  public List<TroublesDTO> getTroubleByCode(String troubleCode) {
    return troublesRepository.getTroubleByCode(troubleCode);
  }

  @Override
  public List<TroubleStatisticForm> getStatisticTroubleTotal(String unitId, Boolean isCreateUnit,
      Boolean searchChild, String startTime, String endTime) {
    List<TroubleStatisticForm> lstReturn = new ArrayList<TroubleStatisticForm>();
    List<TroubleStatisticForm> lstData = troublesRepository
        .getStatisticTroubleDetail(unitId, isCreateUnit, searchChild, startTime, endTime);
    TroubleStatisticForm waitingReceive = new TroubleStatisticForm(1); // Chua tiep nhan
    TroubleStatisticForm inprocessing = new TroubleStatisticForm(2);  // Dang xu ly
    TroubleStatisticForm hasSoluion = new TroubleStatisticForm(3);   // Cap nhat giai phap
    TroubleStatisticForm waitForDefered = new TroubleStatisticForm(4); // chuyen tam dong
    TroubleStatisticForm defered = new TroubleStatisticForm(5);  // tam Dung
    TroubleStatisticForm overDue = new TroubleStatisticForm(6);  // qua han
    TroubleStatisticForm aboutToOverDue = new TroubleStatisticForm(7);  // tam dung

    for (TroubleStatisticForm o : lstData) {
      if (Constants.TT_STATE.Waiting_Receive
          .equalsIgnoreCase(o.getStatusCode())) {  // cho tiep nhan
        if ("TT_Major".equalsIgnoreCase(o.getPriorityCode())) {
          waitingReceive.setNumMajor(waitingReceive.getNumMajor() + 1);
        } else if ("TT_Critical".equalsIgnoreCase(o.getPriorityCode())) {
          waitingReceive.setNumCritical(waitingReceive.getNumCritical() + 1);
        } else if ("TT_Minor".equalsIgnoreCase(o.getPriorityCode())) {
          waitingReceive.setNumMinor(waitingReceive.getNumMinor() + 1);
        }
      } else if (Constants.TT_STATE.QUEUE.equalsIgnoreCase(o.getStatusCode())) {  // dang xu ly
        if ("TT_Major".equalsIgnoreCase(o.getPriorityCode())) {
          inprocessing.setNumMajor(inprocessing.getNumMajor() + 1);
        } else if ("TT_Critical".equalsIgnoreCase(o.getPriorityCode())) {
          inprocessing.setNumCritical(inprocessing.getNumCritical() + 1);
        } else if ("TT_Minor".equalsIgnoreCase(o.getPriorityCode())) {
          inprocessing.setNumMinor(inprocessing.getNumMinor() + 1);
        }
      } else if (Constants.TT_STATE.SOLUTION_FOUND
          .equalsIgnoreCase(o.getStatusCode())) {  //da co giai phap
        if ("TT_Major".equalsIgnoreCase(o.getPriorityCode())) {
          hasSoluion.setNumMajor(hasSoluion.getNumMajor() + 1);
        } else if ("TT_Critical".equalsIgnoreCase(o.getPriorityCode())) {
          hasSoluion.setNumCritical(hasSoluion.getNumCritical() + 1);
        } else if ("TT_Minor".equalsIgnoreCase(o.getPriorityCode())) {
          hasSoluion.setNumMinor(hasSoluion.getNumMinor() + 1);
        }
      } else if (Constants.TT_STATE.Wait_for_Deferred
          .equalsIgnoreCase(o.getStatusCode())) { // cho tam dong
        if ("TT_Major".equalsIgnoreCase(o.getPriorityCode())) {
          waitForDefered.setNumMajor(waitForDefered.getNumMajor() + 1);
        } else if ("TT_Critical".equalsIgnoreCase(o.getPriorityCode())) {
          waitForDefered.setNumCritical(waitForDefered.getNumCritical() + 1);
        } else if ("TT_Minor".equalsIgnoreCase(o.getPriorityCode())) {
          waitForDefered.setNumMinor(waitForDefered.getNumMinor() + 1);
        }
      } else if (Constants.TT_STATE.DEFERRED.equalsIgnoreCase(o.getStatusCode())) {  // tam dong
        if ("TT_Major".equalsIgnoreCase(o.getPriorityCode())) {
          defered.setNumMajor(defered.getNumMajor() + 1);
        } else if ("TT_Critical".equalsIgnoreCase(o.getPriorityCode())) {
          defered.setNumCritical(defered.getNumCritical() + 1);
        } else if ("TT_Minor".equalsIgnoreCase(o.getPriorityCode())) {
          defered.setNumMinor(defered.getNumMinor() + 1);
        }
      }
      if (o.getRemainTime()
          != null) { //de phong co 1 ban ghi loi gay ra exception, chu nghiep vu dung thi remainTime khong duojc null
        if (o.getRemainTime() < 0) {//qua han
          if ("TT_Major".equalsIgnoreCase(o.getPriorityCode())) {
            overDue.setNumMajor(overDue.getNumMajor() + 1);
          } else if ("TT_Critical".equalsIgnoreCase(o.getPriorityCode())) {
            overDue.setNumCritical(overDue.getNumCritical() + 1);
          } else if ("TT_Minor".equalsIgnoreCase(o.getPriorityCode())) {
            overDue.setNumMinor(overDue.getNumMinor() + 1);
          }
        } else if (o.getRemainTime() >= 0 && o.getRemainTime() <= 0.5D) {//sap qua han
          if ("TT_Major".equalsIgnoreCase(o.getPriorityCode())) {
            aboutToOverDue.setNumMajor(aboutToOverDue.getNumMajor() + 1);
          } else if ("TT_Critical".equalsIgnoreCase(o.getPriorityCode())) {
            aboutToOverDue.setNumCritical(aboutToOverDue.getNumCritical() + 1);
          } else if ("TT_Minor".equalsIgnoreCase(o.getPriorityCode())) {
            aboutToOverDue.setNumMinor(aboutToOverDue.getNumMinor() + 1);
          }
        }
      }
    }
    lstReturn.add(overDue);
    lstReturn.add(aboutToOverDue);
    lstReturn.add(defered);
    lstReturn.add(hasSoluion);
    lstReturn.add(inprocessing);
    lstReturn.add(waitForDefered);
    lstReturn.add(waitingReceive);

    return lstReturn;
  }

  @Override
  public List<TroublesDTO> getInfoTicketForAMI(TroublesDTO troublesDTO) {
    List<TroublesDTO> lst = new ArrayList<>();
//    List<TroublesDTO> result = new ArrayList<>();
    TroublesInSideDTO troublesInSideDTO;
    List<String> troubleCodes = troublesDTO.getTroubleCodes();
    List<String> amiIds = troublesDTO.getAmiIds();
    try {
      if (troubleCodes != null && !troubleCodes.isEmpty()) {
        for (String troubleCode : troubleCodes) {
          troublesInSideDTO = troublesRepository
              .getTroubleDTO(null, troubleCode, null, null, null, null, null);
          if (troublesInSideDTO != null) {
            lst.add(troublesInSideDTO.toModelOutSide());
          }
        }
      } else if (amiIds != null && !amiIds.isEmpty()) {
        for (String amiId : amiIds) {
          troublesInSideDTO = troublesRepository
              .getTroubleDTO(null, null, null, amiId, null, null, null);
          if (troublesInSideDTO != null) {
            lst.add(troublesInSideDTO.toModelOutSide());
          }
        }
      }
      if (lst != null && !lst.isEmpty()) {
        //lay file
        for (TroublesDTO trouble : lst) {
          List<GnocFileDto> lstFile = troublesRepository.getFileByTrouble(trouble);
          if (lstFile != null && !lstFile.isEmpty()) {
            List<String> lstName = new ArrayList<>();
            List<byte[]> lstByte = new ArrayList<>();
            for (GnocFileDto fileDTO : lstFile) {
              byte[] bytes = FileUtils
                  .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), fileDTO.getPath());
              lstName.add(fileDTO.getFileName());
              lstByte.add(bytes);
            }
            trouble.setArrFileName(lstName);
            trouble.setFileDocumentByteArray(lstByte);
          }
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return lst;
  }

  public ResultDTO closetroubleFromWo(TroublesDTO troublesDTO, TroublesDTO trouble) {
    ResultDTO result = new ResultDTO();
    try {
      result.setKey(RESULT.SUCCESS);
      result.setMessage(RESULT.SUCCESS);
      if (!trouble.getInsertSource().contains("NOC") && !"TT".equals(trouble.getInsertSource())) {
        return result;
      }
      if ("10".equals(trouble.getState()) || "11".equals(trouble.getState())) {
        return result;
      }
      trouble.setProcessingUnitId(troublesDTO.getReceiveUnitId());
      trouble.setProcessingUnitName(troublesDTO.getReceiveUnitName());
      trouble.setProcessingUserId(troublesDTO.getReceiveUserId());
      trouble.setProcessingUserName(troublesDTO.getReceiveUserName());

      if (StringUtils.isNotNullOrEmpty(trouble.getEndTroubleTime())) {
        trouble.setEndTroubleTime(troublesDTO.getEndTroubleTime());
      }
      if (StringUtils.isNotNullOrEmpty(trouble.getReasonId()) && StringUtils
          .isNotNullOrEmpty(troublesDTO.getReasonId())) {
        trouble.setReasonId(troublesDTO.getReasonId());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getRootCause())) {
        trouble.setRootCause(troublesDTO.getRootCause());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getSolutionType())) {
        trouble.setSolutionType(troublesDTO.getSolutionType());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getWorkArround())) {
        trouble.setWorkArround(troublesDTO.getWorkArround());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getReasonName())) {
        trouble.setReasonName(troublesDTO.getReasonName());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getQueueTime())) {
        trouble.setQueueTime(troublesDTO.getQueueTime());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getClosuresReplace())) {
        trouble.setClosuresReplace(troublesDTO.getClosuresReplace());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getLineCutCode())) {
        trouble.setLineCutCode(troublesDTO.getLineCutCode());
      }
      if (StringUtils.isNotNullOrEmpty(troublesDTO.getCodeSnippetOff())) {
        trouble.setCodeSnippetOff(troublesDTO.getCodeSnippetOff());
      }
      //neu chua tao du WO
      if (trouble.getInsertSource().contains("NOC") && !""
          .equals(checkWoRequiredClosed(trouble.toModelInSide()))) {
        TroubleActionLogsEntity troubleActionLogsDTO = new TroubleActionLogsEntity(null,
            I18n.getLanguage("wo.update.closed"),
            new Date(),
            troublesDTO.getReceiveUnitId() == null ? null
                : Long.valueOf(troublesDTO.getReceiveUnitId()),
            troublesDTO.getReceiveUserId() == null ? null
                : Long.valueOf(troublesDTO.getReceiveUserId()),
            I18n.getLanguage("common.btn.update"),
            trouble.getTroubleId() == null ? null : Long.valueOf(trouble.getTroubleId()),
            troublesDTO.getReceiveUnitName(), troublesDTO.getReceiveUserName(),
            Long.valueOf(trouble.getState()), trouble.getTroubleCode(), trouble.getStateName(),
            null, null);

        troubleActionLogsDTO.setContent(
            I18n.getLanguage("common.btn.update") + " " + Constants.RESULT.SUCCESS
                + " "
                + I18n.getLanguage("wo.update.not.closed"));
        troubleActionLogsDTO.setRootCause(trouble.getRootCause());
        troubleActionLogsDTO.setWorkArround(trouble.getWorkArround());
        troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO);
        return result;
      }
      if ("7".equals(trouble.getState())) {
        trouble.setAssignTimeTemp(troublesDTO.getEndTroubleTime());
      }
      trouble.setCheckbox("5");
      if (trouble.getInsertSource().contains("NOC") && trouble.getCreateUnitId()
          .equals(trouble.getReceiveUnitId())) {
        if (DataUtil.isNullOrEmpty(trouble.getRelatedKedb())) { //chua co kedb
          trouble.setState("10");
          trouble.setStateName(Constants.TT_STATE.CLOSED_Not_KEDB);
        } else { //co kedb
          trouble.setState("11");
          trouble.setStateName(Constants.TT_STATE.CLOSED);
        }
        ResultInSideDto res = onUpdateTrouble(trouble.toModelInSide(), false);
        result.setId(res.getId() != null ? String.valueOf(res.getId()) : null);
        result.setKey(res.getKey());
        result.setMessage(res.getMessage());
      } else if (!"2".equals(trouble.getState()) && !"9".equals(trouble.getState())
          && !"10".equals(trouble.getState()) && !"11".equals(trouble.getState())) {
        trouble.setState("9");
        trouble.setStateName(Constants.TT_STATE.CLEAR);
        trouble.setClearUserId(Long.parseLong(trouble.getProcessingUserId()));
        trouble.setClearUserName(trouble.getProcessingUserName());
        ResultInSideDto res = onUpdateTrouble(trouble.toModelInSide(), false);
        result.setId(res.getId() != null ? String.valueOf(res.getId()) : null);
        result.setKey(res.getKey());
        result.setMessage(res.getMessage());
      }
    } catch (Exception e) {
      result.setId(null);
      result.setKey(RESULT.ERROR);
      result.setMessage(e.getMessage());
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public ResultDTO validate(TroublesDTO tForm) throws Exception {
    ResultDTO result = new ResultDTO();
    result.setKey(RESULT.SUCCESS);
    if (StringUtils.isStringNullOrEmpty(tForm.getTroubleCode())) {
      result.setKey(RESULT.ERROR);
      result.setMessage("TroubleCode" + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    TroublesInSideDTO troublesInSideDTO = troublesRepository
        .getTroubleDTO(null, tForm.getTroubleCode(), null, null, null, null, null);
    TroublesDTO troubleDTO = troublesInSideDTO != null ? troublesInSideDTO.toModelOutSide() : null;
    if (troubleDTO == null || troubleDTO.getTroubleId() == null) {
      tForm.setSpmName("MOBILE");
      tForm.setProcessingUnitId(tForm.getReceiveUnitId());
      tForm.setProcessingUnitName(tForm.getReceiveUnitName());
      tForm.setProcessingUserName(tForm.getReceiveUserName());
      tForm.setSpmCode(tForm.getTroubleCode());

      if (tForm.getStateName().equals(Constants.TT_STATE.DEFERRED)) { //tam dong
        tForm.setState("7"); //Chuyen sang tam dong
        //TH2, trong TH WO thuc hien dong, Incident chi can nhan tin cho don vi.
      } else if (tForm.getStateName().equals(Constants.TT_STATE.CLOSED)) {
        tForm.setState("11");
        //TH3 mo tam dong
      } else if (tForm.getStateName().equals(Constants.TT_STATE.QUEUE)) { //mo tam dong
        //Lay thong tin nguoi xu ly thuoc don vi nhan de nhan tin
        tForm.setState("5");
      }
      if (tForm.getStateName().equals(Constants.TT_STATE.CLOSED)) {
        troubleSpmUtils.updateSpmCompleteNotCheckFromWo(tForm);
      } else {
        troubleSpmUtils.updateSpmActionFromWo(tForm);
      }
      result.setKey(RESULT.SUCCESS_NOT_WO);
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getReceiveUnitId())) {
      result.setKey(RESULT.ERROR);
      result.setMessage("ReceiveUnitId " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getReceiveUserId())) {
      result.setKey(RESULT.ERROR);
      result.setMessage("ReceiveUserId " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getReceiveUnitName())) {
      result.setKey(RESULT.ERROR);
      result.setMessage("ReceiveUnitName " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getReceiveUserName())) {
      result.setKey(RESULT.ERROR);
      result.setMessage("ReceiveUserName " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getProcessingUserPhone())) {
      result.setKey(RESULT.ERROR);
      result.setMessage("ProcessingUserPhone " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getStateName())) {
      result.setKey(RESULT.ERROR);
      result.setMessage("StateName " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getStateWo())) {
      result.setKey(RESULT.ERROR);
      result.setMessage("StateWo " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (tForm.getStateName().equals(Constants.TT_STATE.DEFERRED)) {
      //lay danh sach mang duoc tam dong
      List<CatItemDTO> lst = catItemRepository.getListItemByCategory("ARRAY_DEFER_WO", null);
      if (lst != null && !lst.isEmpty()) {
        String typeCode = "," + lst.get(0).getDescription() + ",";
        CatItemDTO catItemDTO = catItemRepository
            .getCatItemById(Long.parseLong(troubleDTO.getTypeId()));
        if (typeCode.contains("," + catItemDTO.getItemCode() + ",")) {
        } else {
          result.setKey(RESULT.ERROR);
          result.setMessage(I18n.getLanguage("wo.update.defer"));
          return result;
        }
      } else {
        result.setKey(RESULT.ERROR);
        result.setMessage(I18n.getLanguage("wo.update.defer"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getDeferredReason())) {
        result.setKey(RESULT.ERROR);
        result.setMessage("DeferredReason " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getDeferredTime())) {
        result.setKey(RESULT.ERROR);
        result.setMessage("DeferredTime " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      try {
        DateTimeUtils.convertStringToDateTime(tForm.getDeferredTime());
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        result.setKey(RESULT.ERROR);
        result.setMessage("DeferredTime " + I18n.getLanguage("wo.close.trouble.date.format"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getNumPending())) {
        result.setKey(RESULT.ERROR);
        result.setMessage("NumPending " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
    } else if (tForm.getStateName().equals(Constants.TT_STATE.CLOSED)) {
      if (StringUtils.isStringNullOrEmpty(tForm.getEndTroubleTime())) {
        result.setKey(RESULT.ERROR);
        result.setMessage("EndTime " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      try {
        DateTimeUtils.convertStringToDateTime(tForm.getEndTroubleTime());
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        result.setKey(RESULT.ERROR);
        result.setMessage("EndTime " + I18n.getLanguage("wo.close.trouble.date.format"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv1Id())) {
        result.setKey(RESULT.ERROR);
        result.setMessage(
            "ReasonLv1Id " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv1Name())) {
        result.setKey(RESULT.ERROR);
        result.setMessage(
            "ReasonLv1Name " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv2Id())) {
        result.setKey(RESULT.ERROR);
        result.setMessage(
            "ReasonLv2Id " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }

      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv2Name())) {
        result.setKey(RESULT.ERROR);
        result.setMessage(
            "ReasonLv2Name " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }

      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv3Id())) {
        result.setKey(RESULT.ERROR);
        result.setMessage(
            "ReasonLv3Id " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv3Name())) {
        result.setKey(RESULT.ERROR);
        result.setMessage(
            "ReasonLv3Name " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getTotalPendingTimeGnoc())) {
        result.setKey(RESULT.ERROR);
        result.setMessage(
            "TotalPendingTimeGnoc " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getTotalProcessTimeGnoc())) {
        result.setKey(RESULT.ERROR);
        result.setMessage(
            "TotalProcessTimeGnoc " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getEvaluateGnoc())) {
        result.setKey(RESULT.ERROR);
        result.setMessage(
            "EvaluateGnoc " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
    } else if (tForm.getStateName().equals(Constants.TT_STATE.QUEUE)) {

    } else if ("UPDATE".equals(tForm.getStateName())) {
      if (StringUtils.isStringNullOrEmpty(tForm.getWorkLog())) {
        result.setKey(RESULT.ERROR);
        result.setMessage("WorkLog " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
    } else {
      if (StringUtils.isStringNullOrEmpty(tForm.getWorkLog())) {
        result.setKey(RESULT.ERROR);
        result.setMessage("WorkLog " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
    }
    return result;
  }

  public String convertStatus2String(String status) {
    if (StringUtils.isStringNullOrEmpty(status)) {
      return "";
    } else {
      String result = ("UNASSIGNED".equals(status) || "0".equals(status)) ? I18n
          .getString("message.wo.status.UNASSIGNED")//
          : ("ASSIGNED".equals(status) || "1".equals(status)) ? I18n
              .getString("message.wo.status.ASSIGNED")//
              : ("REJECT".equals(status) || "2".equals(status)) ? I18n
                  .getString("message.wo.status.REJECT")//
                  : ("DISPATCH".equals(status) || "3".equals(status)) ? I18n
                      .getString("message.wo.status.DISPATCH")//
                      : ("ACCEPT".equals(status) || "4".equals(status)) ? I18n
                          .getString("message.wo.status.ACCEPT")//
                          : ("INPROCESS".equals(status) || "5".equals(status)) ? I18n
                              .getString("message.wo.status.INPROCESS")//
                              : ("CLOSED_FT".equals(status) || "6".equals(status)) ? I18n
                                  .getString("message.wo.status.CLOSED_FT")//
                                  : ("DRAFT".equals(status) || "7".equals(status)) ? I18n
                                      .getString("message.wo.status.DRAFT")//
                                      : ("CLOSED_CD".equals(status) || "8".equals(status)) ? I18n
                                          .getString("message.wo.status.CLOSED_CD")//
                                          : ("PENDING".equals(status) || "9".equals(status)) ? I18n
                                              .getString("message.wo.status.PENDING")//
                                              : status;
      return result;
    }

  }

  @Override
  public Datatable getListFileAttachByTroubleId(GnocFileDto gnocFileDto) {
    log.debug("Request to getListFileAttachByTroubleId : {}", gnocFileDto);
    Datatable datatable = troublesRepository.getListFileAttachByTroubleId(gnocFileDto);
    Double offset;
    try {
      offset = TimezoneContextHolder.getOffsetDouble();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      offset = 0D;
    }
    List<GnocFileDto> list = (List<GnocFileDto>) datatable.getData();
    SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    for (GnocFileDto dto : list) {
      try {
        String createDate = DateUtil.date2ddMMyyyyHHMMss(dto.getCreateTime());
        Date display = spd.parse(createDate);
        display = new Date(display.getTime() + (long) (offset * 60 * 60 * 1000));
        dto.setCreateTime(display);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return datatable;
  }

  @Override
  public ResultInSideDto insertTroubleFilesUpload(List<MultipartFile> files,
      TroublesInSideDTO troublesDTO) throws IOException {
    log.debug("Request to insertProblemFilesUpload: {}", troublesDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    for (MultipartFile multipartFile : files) {
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
              multipartFile.getBytes(), null);
      String fileName = multipartFile.getOriginalFilename();
      //Start save file old
      String fullPathOld = FileUtils
          .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
              uploadFolder, null);
      CommonFileDTO commonFileDTO = new CommonFileDTO();
      commonFileDTO.setCreateTime(new Date());
      commonFileDTO.setCreateUserId(userToken.getUserID());
      commonFileDTO.setCreateUserName(userToken.getUserName());
      commonFileDTO.setPath(FileUtils.getFilePath(fullPathOld) + File.separator);
      commonFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
      ResultInSideDto resultFileDataOld = troublesRepository.insertCommonFile(commonFileDTO);
      TroubleFileEntity troubleFile = new TroubleFileEntity();
      troubleFile.setTroubleId(Long.valueOf(troublesDTO.getTroubleId()));
      troubleFile.setFileId(resultFileDataOld.getId());
      troublesRepository.insertTroubleFile(troubleFile);
      //End save file old
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(fileName);
      gnocFileDto.setCreateUnitId(userToken.getDeptId());
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(userToken.getUserID());
      gnocFileDto.setCreateUserName(userToken.getUserName());
      gnocFileDto.setCreateTime(commonFileDTO.getCreateTime());
      gnocFileDto.setMappingId(resultFileDataOld.getId());
      gnocFileDtos.add(gnocFileDto);
    }
    gnocFileRepository
        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.TROUBLES, troublesDTO.getTroubleId(),
            gnocFileDtos);
    return resultInSideDto;
  }

  @Override
  public ResultDTO updateReasonTroubleFromNOC(TroublesDTO tForm) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      TroublesInSideDTO troubleDTO = troublesRepository
          .getTroubleDTO(null, tForm.getTroubleCode(), null, null, null, null, null);
      if (troubleDTO == null || troubleDTO.getTroubleId() == null) {
        resultDTO.setKey(Constants.RESULT.FAIL);
        resultDTO.setMessage(I18n.getLanguage("incident.invalid.ticket"));
        return resultDTO;
      }
      TroublesInSideDTO trouble = findTroublesById(troubleDTO.getTroubleId());

      TroubleActionLogsDTO troubleActionLogsDTO = new TroubleActionLogsDTO(null,
          troubleDTO.compareContent(trouble.toEntity()), new Date(),
          trouble.getCreateUnitId(), trouble.getCreateUserId(),
          I18n.getLanguage("common.btn.update"), trouble.getTroubleId(),
          tForm.getProcessingUnitName(), tForm.getProcessingUserName(),
          Long.valueOf(troubleDTO.getState()),
          troubleDTO.getTroubleCode(), troubleDTO.getStateName(), null, null);

      if (!StringUtils.isStringNullOrEmpty(troubleDTO.getReasonName()) || !StringUtils
          .isStringNullOrEmpty(troubleDTO.getRootCause()) || !StringUtils
          .isStringNullOrEmpty(troubleDTO.getWorkArround())) {
        troubleDTO.setWorkLog(
            (troubleDTO.getReasonName() == null ? "" : troubleDTO.getReasonName()) + " --- " + (
                troubleDTO.getRootCause() == null ? "" : troubleDTO.getRootCause())
                + " --- " + (troubleDTO.getWorkArround() == null ? ""
                : troubleDTO.getWorkArround()));
        troubleDTO.setCreateUnitName(tForm.getProcessingUnitName());
        troubleDTO.setCreateUserName(tForm.getProcessingUserName());
        troubleDTO
            .setLastUpdateTime(DateTimeUtils.convertStringToDate(DateTimeUtils.getSysDateTime()));
        troublesRepository.onInsertWorkLog(troubleDTO);
      }

      if (!StringUtils.isStringNullOrEmpty(tForm.getReasonId())) {
        trouble.setReasonId(Long.parseLong(tForm.getReasonId()));
      }

      if (!StringUtils.isStringNullOrEmpty(tForm.getReasonName())) {
        trouble.setReasonName(tForm.getReasonName());
      }
      trouble.setRootCause(tForm.getRootCause());
      trouble.setWorkArround(tForm.getWorkArround());

      troubleActionLogsDTO
          .setContent(I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS + " "
              + I18n.getLanguage("noc.update.reason"));
      if (!StringUtils.isStringNullOrEmpty(trouble.getTroubleId())) {
        troublesRepository.onUpdateTrouble(trouble);
      }
      if (!StringUtils.isStringNullOrEmpty(troubleActionLogsDTO)) {
        troublesRepository.onInsertTroubleActionLogs(troubleActionLogsDTO);
      }
      resultDTO.setKey(Constants.RESULT.SUCCESS);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(ex.toString());
      return resultDTO;
    }
    return resultDTO;
  }

  @Override
  public List<ProblemGroupDTO> getListProblemGroupParent(CfgServerNocDTO cfgServerNocDTO)
      throws Exception {
    log.debug("Request to getListProblemGroupParent: {}", cfgServerNocDTO);
    return troublesRepository.getListProblemGroupParent(cfgServerNocDTO);
  }

  @Override
  public List<ProblemGroupDTO> getListProblemGroupByParrenId(Long probGroupId,
      CfgServerNocDTO cfgServerNocDTO)
      throws Exception {
    log.debug("Request to getListProblemGroupByParrenId: {}", cfgServerNocDTO);
    return troublesRepository.getListProblemGroupByParrenId(probGroupId, cfgServerNocDTO);
  }

  @Override
  public List<ProblemTypeDTO> getListPobTypeByGroupId(Long probGroupId,
      CfgServerNocDTO cfgServerNocDTO)
      throws Exception {
    log.debug("Request to getListPobTypeByGroupId: {}", cfgServerNocDTO);
    return troublesRepository.getListPobTypeByGroupId(probGroupId, cfgServerNocDTO);
  }

  @Override
  public List<String> getListActionInfo(ActionInfoDTO actionInfoDTO) {
    log.debug("Request to getListActionInfo: {}", actionInfoDTO);
    return troubleActionLogsRepository.onSearchActionInfo(actionInfoDTO);
  }

  private List<CatItemDTO> getListReasion() {
    return catReasonBusiness.getListCatReason(23L,
        "EM_CAUSE_MAT LUONG DOWNCELL_TRUYEN DAN_VIBA_LOI CAP LUONG VIBA");
  }

  private List<CatItemDTO> getTroubleDutCap() {
    return catReasonBusiness.getListCatReason(22L,
        "EM_CAUSE_MAT LUONG DOWNCELL_TRUYEN DAN_QUANG_LOI CAP LUONG");
  }

  //Dunglv3 add
  @Override
  public UsersDTO getUserByUserLogin(String userName) {
    return troublesRepository.getUserByUserLogin(userName);
  }

  @Override
  public List<TroublesDTO> getTroubleInfoForVsSmart(TroublesDTO troublesDTO) {
    return troublesRepository.getTroubleInfoForVsSmart(troublesDTO);
  }

  @Override
  public TroublesDTO findTroublesDtoById(Long id) {
    return troublesRepository.findTroublesDtoById(id);
  }

  @Override
  public ResultDTO updateTroubleFromVSMART(TroublesDTO troublesDTO, boolean check) {
    ResultDTO resultDTO = new ResultDTO();
    if (check) {
      resultDTO = troublesRepository.updateTroubleFromVSMART(troublesDTO);
    } else {
      ResultInSideDto resultInSideDto = troublesRepository
          .onUpdateTrouble(troublesDTO.toModelInSide());
      if (resultInSideDto != null) {
        resultDTO.setKey(resultInSideDto.getKey());
        resultDTO.setMessage(resultInSideDto.getMessage());
        resultDTO.setId(StringUtils.isLongNullOrEmpty(resultInSideDto.getId()) ? null
            : resultInSideDto.getId().toString());
      }
    }

    TroublesInSideDTO troublesInSideDTO = troublesDTO.toModelInSide();
    if (!(resultDTO.getKey() != null && resultDTO.getKey().equals(RESULT.FAIL))) {
      // worklog (start)
      try {
        onInsertWorkLog(troublesInSideDTO);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      // worklog (end)
      // ghi action_log (start)
      String contentLog = I18n.getLanguage("incident.update") + " " + Constants.RESULT.SUCCESS + " "
          + I18n.getLanguage("tt.vsmart");
      CatItemDTO catItemDTO = new CatItemDTO();
      catItemDTO.setCategoryCode("TT_STATE");
      catItemDTO.setItemId(Long.parseLong(troublesDTO.getState()));
      List<CatItemDTO> list = catItemRepository.getListCatItemDTO(catItemDTO);
      String stateName = null;
      if (list != null && list.size() > 0) {
        stateName = list.get(0).getItemCode();
      }
      TroubleActionLogsEntity troubleActionLogsDTO = new TroubleActionLogsEntity(null,
          contentLog,
          new Date(),
          troublesInSideDTO.getCreateUnitId() == null ? null
              : Long.valueOf(troublesInSideDTO.getCreateUnitId()),
          troublesInSideDTO.getCreateUserId() == null ? null
              : Long.valueOf(troublesInSideDTO.getCreateUserId()),
          I18n.getLanguage("common.btn.update"), troublesInSideDTO.getTroubleId(),
          StringUtils.isStringNullOrEmpty(troublesInSideDTO.getCreateUnitName()) ? null
              : troublesInSideDTO.getCreateUnitName(),
          StringUtils.isStringNullOrEmpty(troublesInSideDTO.getCreateUserName()) ? null
              : troublesInSideDTO.getCreateUserName(),
          troublesDTO.getState() == null ? null : Long.valueOf(troublesDTO.getState()),
          StringUtils.isStringNullOrEmpty(troublesInSideDTO.getTroubleCode()) ? null
              : troublesInSideDTO.getTroubleCode(), stateName, null, null, null);
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getWorkArround())) {
        troubleActionLogsDTO.setWorkArround(troublesDTO.getWorkArround());
      }
      troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO);
      // ghi action_log (end)
    }
    resultDTO.setLstResult(null);
    return resultDTO;
  }

  @Override
  public List<ItemDataCRInside> getListLocationCombobox(Long parentId) {
    return troublesRepository.getListLocationCombobox(parentId);
  }

  @Override
  public TroubleActionLogsDTO getTroubleActionLogDTOByTroubleId(Long id) {
    return troublesRepository.getTroubleActionLogDTOByTroubleId(id);
  }

  @Override
  public List<OdSearchInsideDTO> findListOdByTt(Long troubleId) {
    log.info("Request to findListOdByTt : {}", troubleId);
    UserToken userToken = ticketProvider.getUserToken();
    OdSearchInsideDTO odDTOSearch = new OdSearchInsideDTO();
    odDTOSearch.setUserId(userToken.getUserID().toString());
    odDTOSearch.setInsertSource("TT");
    odDTOSearch.setOtherSystemCode(troubleId.toString());
    odDTOSearch.setPage(1);
    odDTOSearch.setPageSize(Integer.MAX_VALUE);
    return odServiceProxy.getListDataSearchForOther(odDTOSearch);
  }

  @Override
  public List<RiskDTO> findListRiskByTt(Long troubleId) {
    log.info("Request to findListRiskByTt : {}", troubleId);
    RiskDTO riskDTO = new RiskDTO();
    riskDTO.setOtherSystemCode(troubleId.toString());
    riskDTO.setInsertSource("TT");
    riskDTO.setPage(1);
    riskDTO.setPageSize(Integer.MAX_VALUE);
    riskDTO.setOffSetFromUser(TimezoneContextHolder.getOffsetDouble());
    return riskServiceProxy.getListRiskByTt(riskDTO);
  }

  public String compareObj(TroublesInSideDTO o1, TroublesInSideDTO o2, Double offset)
      throws JsonProcessingException {
    Map<String, Object> parameters = new HashMap<>();
    SimpleDateFormat dfm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    try {
      Field[] o1Field = o1.getClass().getDeclaredFields();
      Field[] o2Field = o2.getClass().getDeclaredFields();
      List<String> lstField = Arrays
          .asList("troubleName", "description", "insertSource", "priorityId",
              "state", "typeId", "country",
              "receiveUnitName",
              "receiveUserName", "subCategoryId",
              "location", "impactId", "vendorId",
              "affectedNode", "lstNode", "risk",
              "affectedService", "alarmGroupId", "alarmGroupCode",
              "strAlarmGroupDescription", "networkLevel",
              "transNetworkTypeId", "autoCreateWO", "rejectedCode", "rejectReason",
              "authorityDTO", "infoTicket", "warnLevel", "solutionType", "closeCode",
              "reasonLv1Name", "reasonLv2Name", "reasonOverdueName", "reasonOverdueName2",
              "reasonLv3Name", "subMin", "downtime", "relatedKedb", "workArround", "rootCause",
              "reasonName", "supportUnitName", "numAffect");
      List<String> lstTime = Arrays
          .asList("createdTime", "beginTroubleTime", "endTroubleTime", "assignTime");
      List<String> listNull = Arrays
          .asList("affectedService");
      List<String> listImpact = Arrays
          .asList("impactId");
      Boolean check = false;
      for (Field f1 : o1Field) {
        try {
          Field f2 = o2.getClass().getDeclaredField(f1.getName());
          f1.setAccessible(true);
          f2.setAccessible(true);
          var v1 = f1.get(o1);
          var v2 = f2.get(o2);

          if (lstTime.contains(f1.getName())) {
            if (v1 != null) {
              v1 = DateTimeUtils
                  .date2ddMMyyyyHHMMss(DateTimeUtils.convertDateToOffset((Date) v1, offset, true));
            }
            if (v2 != null) {
              v2 = DateTimeUtils
                  .date2ddMMyyyyHHMMss(DateTimeUtils.convertDateToOffset((Date) v2, offset, true));
            }
          }

          if (listImpact.contains(f1.getName()) && v2.toString().equals("72")) {
            check = true;
          }

          // thangdt covert id->name
          CatItemDTO catItemDTO = null;
          CatItemDTO catItemDTO1 = null;
          if (f1.getName().equals("state") || f1.getName().equals("priorityId") || f1.getName()
              .equals("impactId") || f1.getName().equals("subCategoryId") || f1.getName()
              .equals("rejectedCode")) {
            if (v1 != null) {
              catItemDTO = troublesRepository
                  .convertIdToName(Long.parseLong(v1.toString()), null);
            }
            if (v2 != null) {
              catItemDTO1 = troublesRepository
                  .convertIdToName(Long.parseLong(v2.toString()), null);
            }

            if (catItemDTO != null && !StringUtils.isStringNullOrEmpty(catItemDTO.getItemName())) {
              v1 = catItemDTO.getItemName();
            }
            if (catItemDTO1 != null && !StringUtils
                .isStringNullOrEmpty(catItemDTO1.getItemName())) {
              v2 = catItemDTO1.getItemName();
            }
          }

          if (f1.getName().equals("warnLevel") || f1.getName().equals("solutionType") || f1
              .getName().equals("closeCode") || f1.getName().equals("vendorId")) {
            Long category = null;
            if (f1.getName().equals("warnLevel")) {
              category = 111L;
            } else if (f1.getName().equals("solutionType")) {
              category = 28L;
            } else if (f1.getName().equals("closeCode")) {
              category = 26L;
            } else if (f1.getName().equals("vendorId")) {
              category = 20L;
            }
            if (v1 != null) {
              catItemDTO = troublesRepository
                  .convertIdToName(Long.parseLong(v1.toString()), category);
            }
            if (v2 != null) {
              catItemDTO1 = troublesRepository
                  .convertIdToName(Long.parseLong(v2.toString()), category);
            }
            if (catItemDTO != null && !StringUtils.isStringNullOrEmpty(catItemDTO.getItemName())) {
              v1 = catItemDTO.getItemName();
            }
            if (catItemDTO1 != null && !StringUtils
                .isStringNullOrEmpty(catItemDTO1.getItemName())) {
              v2 = catItemDTO1.getItemName();
            }
          }

          if (lstField.contains(f1.getName()) || lstTime.contains(f1.getName())) {
            if (!(check == true && listNull.contains(f1.getName()))) {
              if (!StringUtils.isStringNullOrEmpty(v1) && !StringUtils.isStringNullOrEmpty(v2)
                  && !v2
                  .equals(v1)) {
                String value = "{" + '"' + "oldValue" + '"' + ":" + '"' + v1 + '"' + ","
                    + '"' + "newValue" + '"' + ":" + '"' + v2 + '"' + "}";
                parameters.put(f2.getName(), value);
              }
              if (StringUtils.isStringNullOrEmpty(v1) && !StringUtils.isStringNullOrEmpty(v2)) {
                String value = "{" + '"' + "oldValue" + '"' + ":" + '"' + "null" + '"' + ","
                    + '"' + "newValue" + '"' + ":" + '"' + v2 + '"' + "}";
                parameters.put(f2.getName(), value);
              }
              if (!StringUtils.isStringNullOrEmpty(v1) && StringUtils.isStringNullOrEmpty(v2)) {
                String value = "{" + '"' + "oldValue" + '"' + ":" + '"' + v1 + '"' + ","
                    + '"' + "newValue" + '"' + ":" + '"' + "null" + '"' + "}";
                parameters.put(f2.getName(), value);
              }
            }

          }


        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = mapper.writeValueAsString(parameters);
    return jsonString;
  }

  // thangdt
  public WoInsideDTO checkStationCodeTTForWo(Long id) {
    return troublesRepository.checkStationCodeTTForWo(id);
  }

  // Thangdt get danh sach dia ban theo like
  public List<DataItemDTO> getListDistrictByLocationName(String name) {
    return troublesRepository.getListDistrictByLocationName(name);
  }

  // Thangdt searchOdRelated
  @Override
  public Datatable searchOdRelated(OdSearchInsideDTO dto) {
    Datatable datatable = odServiceProxy.getListDataSearch(dto);
    return datatable;
  }

  // Thangdt insertOdCreatedFromOtherSystem
  @Override
  public void insertOdCreatedFromOtherSystem(TroublesInSideDTO troublesDTO) {
    List<OdDTO> odDTOS = troublesDTO.getOdDTOS();
    if (odDTOS != null && !odDTOS.isEmpty() && odDTOS.size() > 0) {
      odServiceProxy.updateOdOtherSystem(odDTOS.get(0));
    }
  }

  // Thangdt searchRiskRelated
  @Override
  public Datatable searchRiskRelated(RiskDTO dto) {
    Datatable datatable = riskServiceProxy.getListDataSearchWeb(dto);
    return datatable;
  }

  // Thangdt insertRiskCreatedFromOtherSystem
  @Override
  public void insertRiskCreatedFromOtherSystem(TroublesInSideDTO troublesDTO) {
    List<RiskDTO> riskDTOS = troublesDTO.getRiskDTOS();
    if (riskDTOS != null && !riskDTOS.isEmpty() && riskDTOS.size() > 0) {
      riskServiceProxy.updateRiskOtherSystem(riskDTOS.get(0));
    }
  }

  @Override
  public TroublesDTO getDetailTroublesById(Long troubleId) {
    TroublesEntity trouble = troublesRepository.findTroublesById(troubleId);
    if (trouble != null) {
      return trouble.toDTO().toModelOutSide();
    }
    return new TroublesDTO();
  }

  @Override
  public void updateFile(Long troubleId, String userLogin, List<String> listFileName,
      List<byte[]> fileArr) {
    if (listFileName != null && listFileName.size() > 0 && fileArr != null
        && fileArr.size() > 0) {
      if (listFileName.size() != fileArr.size()) {
        throw new RuntimeException(I18n.getValidation("tt.numberFileNotMap"));
      }
      UsersInsideDto usersInsideDto = userRepository.getUserDTOByUserName(userLogin);
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      Date date = new Date();
      for (int i = 0; i < listFileName.size(); i++) {
        if (extension != null) {
          String[] extendArr = extension.split(",");
          Boolean checkExt = false;
          for (String e : extendArr) {
            if (listFileName.get(i).toLowerCase().endsWith(e.toLowerCase())) {
              checkExt = true;
              break;
            }
          }
          if (!checkExt) {
            throw new RuntimeException(I18n.getValidation("tt.fileImportInvalidExten"));
          }
        }
        try {
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, listFileName.get(i), fileArr.get(i),
                  null);
          //Start save file old
          String fullPathOld = FileUtils
              .saveUploadFile(listFileName.get(i), fileArr.get(i), uploadFolder, null);

          CommonFileDTO commonFileDTO = new CommonFileDTO();
          commonFileDTO.setCreateTime(date);
          commonFileDTO.setCreateUserId(usersInsideDto.getUserId());
          commonFileDTO.setCreateUserName(usersInsideDto.getUsername());
          commonFileDTO.setPath(FileUtils.getFilePath(fullPathOld) + File.separator);
          commonFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
          ResultInSideDto resultFileDataOld = troublesRepository.insertCommonFile(commonFileDTO);
          TroubleFileEntity troubleFile = new TroubleFileEntity();
          troubleFile.setTroubleId(troubleId);
          troubleFile.setFileId(resultFileDataOld.getId());
          troublesRepository.insertTroubleFile(troubleFile);
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(listFileName.get(i));
          gnocFileDto.setCreateUnitId(usersInsideDto.getUnitId());
          gnocFileDto.setCreateUnitName(usersInsideDto.getUnitName());
          gnocFileDto.setCreateUserId(usersInsideDto.getUserId());
          gnocFileDto.setCreateUserName(usersInsideDto.getUsername());
          gnocFileDto.setCreateTime(date);
          gnocFileDto.setMappingId(resultFileDataOld.getId());
          gnocFileDtos.add(gnocFileDto);
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.TROUBLES, troubleId, gnocFileDtos);
    }
  }

  @Override
  public List<ItemDataTT> getTroubleReasonTreeById(String typeId, String id) {
    return troublesRepository.getTroubleReasonTreeById(typeId, id);
  }

  @Override
  public List<ItemDataTT> getReasonByParentId(String parentId, String typeId) {
    return troublesRepository.getReasonByParentId(parentId, typeId);
  }

  @Override
  public ResultDTO insertTroubleFromOtherSystem(TroublesInSideDTO troublesDTO,
      List<String> listAccount) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    try {
      troublesDTO = validateTroubleOutSide(troublesDTO);
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getMessage())) {
        resultDTO.setKey(RESULT.SUCCESS);
        Date currDate = new Date();
        setTimeZoneForTrouble(troublesDTO, -1L * TimezoneContextHolder.getOffsetDouble());
        troublesDTO.setCreatedTime(currDate);
        troublesDTO = prepareTrouble(troublesDTO);

        ResultInSideDto result = onInsertTrouble(troublesDTO, listAccount, true, troublesDTO.getLstMop());
        if (result != null && result.getKey() != null && result.getKey().equals(RESULT.SUCCESS)) {
          resultDTO.setKey(result.getKey());
          resultDTO.setMessage(result.getMessage());
          resultDTO.setId(troublesDTO.getTroubleCode());
        }
        else {
          resultDTO.setKey(RESULT.FAIL);
        }
      }
      else {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(troublesDTO.getMessage());
      }
    } catch (Exception e) {
      throw  e;
    }
    return resultDTO;
  }

  private TroublesInSideDTO prepareTrouble(TroublesInSideDTO dtoUpdate) {
    TroublesInSideDTO dto = dtoUpdate;
    try {
      Date currDate = new Date();
      dto.setBeginTroubleTime(currDate);
      if (!StringUtils.isStringNullOrEmpty(dto.getWorkLog())) {
        String content = dto.getCreateUserName() != null ? dto.getCreateUserName() : "";
        content += " " + currDate + " (GMT +7) : " + dto.getWorkLog().trim();
        dto.setWorkLog(content);
      } else {
        dto.setWorkLog("");
      }

      String id = getSequenseTroubles("TROUBLES_SEQ", 1).get(0);
      dto.setTroubleId(Long.parseLong(id));
      String itemCode = dto.getTypeCode();
      String ttCode = "GNOC_TT_" + itemCode + "_" + DateTimeUtils
          .convertDateToString(new Date(), "yyMMdd") + "_" + id;
      dto.setTroubleCode(ttCode);

      //Thoi gian cap nhat gan nhat
      dto.setLastUpdateTime(currDate);
      //ThanhLV12_R12038_end
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return dto;
  }

  private TroublesInSideDTO validateTroubleOutSide(TroublesInSideDTO dto) throws Exception {
    try {
      if (StringUtils.isStringNullOrEmpty(dto.getTroubleName())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.troubleName"));
        return dto;
      }

      if (StringUtils.isStringNullOrEmpty(dto.getDescription())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.troubleDescription"));
        return dto;
      }

      if (StringUtils.isStringNullOrEmpty(dto.getInsertSource())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.insertSource"));
        return dto;
      }

      if (StringUtils.isStringNullOrEmpty(dto.getNationCode())) {
        dto.setNationCode("VNM");
      }

      //Thong tin mang su so
      if (StringUtils.isStringNullOrEmpty(dto.getTypeCode())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.typeCode"));
        return dto;
      }
      else {
        CatItemDTO lstType = troublesRepository.getItemByCode("PT_TYPE", dto.getTypeCode(), null);
        if (lstType != null) {
          dto.setTypeId(lstType.getItemId());
          dto.setTypeName(lstType.getItemName());
        }
        else {
          dto.setMessage(I18n.getLanguage("trouble.validate.typeCode.exist"));
          return dto;
        }
      }

      //Thong tin nhom su co
      if (StringUtils.isStringNullOrEmpty(dto.getAlarmGroupCode())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.alarmGroupCode"));
        return dto;
      }
      else {
        CatItemDTO lstGroup = troublesRepository.getItemByCode("ALARM_GROUP", dto.getAlarmGroupCode(), dto.getTypeId().toString());
        if (lstGroup != null) {
          dto.setAlarmGroupId(lstGroup.getItemId() + "");
          dto.setAlarmGroupName(lstGroup.getItemName());
        }
        else {
          dto.setMessage(I18n.getLanguage("trouble.validate.alarmGroupCode.exist"));
          return dto;
        }
      }

      //Thong tin loai node mang
      if (StringUtils.isStringNullOrEmpty(dto.getSubCategoryCode())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.subCategoryCode"));
        return dto;
      }
      else {
        CatItemDTO lstSubCategory = troublesRepository.getItemByCode("PT_SUB_CATEGORY", dto.getSubCategoryCode(), dto.getTypeId().toString());
        if (lstSubCategory != null) {
          dto.setSubCategoryId(lstSubCategory.getItemId());
          dto.setSubCategoryName(lstSubCategory.getItemName());
        }
        else {
          dto.setMessage(I18n.getLanguage("trouble.validate.subCategoryCode.exist"));
          return dto;
        }
      }

      //Thong tin do uu tien
      if (StringUtils.isStringNullOrEmpty(dto.getPriorityCode())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.priorityCode"));
        return dto;
      }
      else {
        CatItemDTO lstPriority = troublesRepository.getPriorityTrouble(dto);
        if (StringUtils.isStringNullOrEmpty(lstPriority.getDescription())) {
          dto.setPriorityId(lstPriority.getItemId());
          dto.setPriorityName(lstPriority.getItemName());
        }
        else {
          dto.setMessage(lstPriority.getDescription());
          return dto;
        }
      }

      if (StringUtils.isStringNullOrEmpty(dto.getIsUnitGnoc())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.isUnitGnoc"));
        return dto;
      }

      // Thong tin don vi thuc hien
      if (StringUtils.isStringNullOrEmpty(dto.getReceiveUnitCode())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.receiveUnitCode"));
        return dto;
      }
      else {
        UnitDTO unit = troublesRepository.getUnitByCode(dto.getReceiveUnitCode(), dto.getIsUnitGnoc());
        if (StringUtils.isStringNullOrEmpty(unit.getDescription())) {
          dto.setReceiveUnitId(unit.getUnitId());
          dto.setReceiveUnitName(unit.getUnitName());
        }
        else {
          dto.setMessage(unit.getDescription());
          return dto;
        }
      }

      //Thong tin anh huong dich vu
      if (StringUtils.isStringNullOrEmpty(dto.getImpactCode())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.impactCode"));
        return dto;
      }
      else {
        CatItemDTO lstImpact = troublesRepository.getItemByCode("TT_IMPACT", dto.getImpactCode(), null);
        if (lstImpact != null) {
          dto.setImpactId(lstImpact.getItemId());
          dto.setImpactName(lstImpact.getItemName());
          if ("Yes".equals(dto.getImpactCode())) {
            if (StringUtils.isStringNullOrEmpty(dto.getAffectedService())) {
              dto.setMessage(I18n.getLanguage("trouble.validate.affectedService"));
              return dto;
            }
            else {
              CatItemDTO serviceDTO = new CatItemDTO();
              serviceDTO.setCategoryCode("PT_AFFECT_SERVICE");
              serviceDTO.setStatus(1L);
              List<CatItemDTO> lstService = catItemBusiness.getListCatItemDTO(serviceDTO);
              Map<String, CatItemDTO> mapService = new HashMap<String, CatItemDTO>();
              for (CatItemDTO sDTO : lstService) {
                mapService.put(sDTO.getItemCode().toUpperCase(), sDTO);
              }
              String[] arrService = dto.getAffectedService().replace(", ", ",").split(",");
              for (String arra : arrService) {
                if (mapService.get(arra.toUpperCase()) == null) {
                  dto.setMessage(I18n.getLanguage("trouble.validate.affectedService.exist"));
                  return dto;
                }
              }
            }
          }
        }
        else {
          dto.setMessage(I18n.getLanguage("trouble.validate.impactCode.exist"));
          return dto;
        }
      }

      //Thong tin dia ban
      if (StringUtils.isStringNullOrEmpty(dto.getLocationCode())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.locationCode"));
        return dto;
      }
      else {
        CatLocationDTO cat = troublesRepository.getLocationByCode(dto.getLocationCode());
        if (cat != null) {
          dto.setLocationId(Long.parseLong(cat.getLocationId()));
          dto.setLocation(cat.getLocationName());
        }
        else {
          dto.setMessage(I18n.getLanguage("trouble.validate.locationCode.exist"));
          return dto;
        }
      }

      //Thong tin trang thai
      Long state = dto.getState() == null ? 3L : dto.getState();
      CatItemDTO status = catItemBusiness.getCatItemById(state);
      if (state != null) {
        dto.setStateName(status.getItemCode());
      }

      //Thong tin user create
      if (StringUtils.isStringNullOrEmpty(dto.getCreateUserName())) {
        dto.setMessage(I18n.getLanguage("trouble.validate.createUserName"));
        return dto;
      }
      else {
        UsersInsideDto usersInsideDto = userRepository.getUserDTOByUserName(dto.getCreateUserName());
        if (usersInsideDto != null) {
          UnitDTO unitToken = unitRepository.findUnitById(usersInsideDto.getUnitId());
          if (unitToken != null ) {
            dto.setCreateUnitName(
                unitToken.getUnitName() + " (" + unitToken.getUnitCode() + ")");
            dto.setProcessingUnitName(
                unitToken.getUnitName() + " (" + unitToken.getUnitCode() + ")");
          }

          dto.setCreateUnitId(usersInsideDto.getUnitId());
          dto.setCreateUserId(usersInsideDto.getUserId());
          dto.setCreateUserName(usersInsideDto.getUsername());
          //HaiNV20 bo sung thong tin của người đang xử lý để cung cấp cho SPM
          dto.setProcessingUnitId(String.valueOf(usersInsideDto.getUnitId()));
          dto.setProcessingUserName(usersInsideDto.getUsername());
          dto.setProcessingUserPhone(usersInsideDto.getMobile());
          //HaiNV20 end
        }
        else {
          dto.setMessage(I18n.getLanguage("trouble.validate.createUserName.exist"));
          return dto;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
    return dto;
  }

  @Override
  public List<ItemDataTT> getStatusConfig(TTChangeStatusDTO ttChangeStatusDTO) {
    return troublesRepository.getStatusConfig(ttChangeStatusDTO);
  }

  @Override
  public List<TroublesDTO> countTroubleByCable(String lineCutCode, String startTime, String endTime,
      int type) {
    return troublesRepository.countTroubleByCable(lineCutCode, startTime, endTime, type);
  }

  @Override
  public List<CatItemDTO> getListUnitApproval() {
    return troublesRepository.getListUnitApproval();
  }
}
