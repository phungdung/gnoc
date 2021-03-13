package com.viettel.gnoc.incident.business;

import com.viettel.bccs2.CauseDTO;
import com.viettel.bccs2.CauseErrorExpireDTO;
import com.viettel.bccs2.TroubleNetworkSolutionDTO;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.CfgInfoTtSpmDTO;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmDTO;
import com.viettel.gnoc.commons.dto.ConcaveDTO;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.CfgInfoTtSpmRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.PROBLEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.ActionInfoDTO;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.dto.CommonDTO;
import com.viettel.gnoc.incident.dto.CommonFileDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleNodeDTO;
import com.viettel.gnoc.incident.dto.TroubleWireDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.ItAccountEntity;
import com.viettel.gnoc.incident.model.ItSpmInfoEntity;
import com.viettel.gnoc.incident.model.TroubleActionLogsEntity;
import com.viettel.gnoc.incident.model.TroubleFileEntity;
import com.viettel.gnoc.incident.model.TroublesEntity;
import com.viettel.gnoc.incident.repository.ItAccountRepository;
import com.viettel.gnoc.incident.repository.ItSpmInfoRepository;
import com.viettel.gnoc.incident.repository.TroubleActionLogsRepository;
import com.viettel.gnoc.incident.repository.TroubleNodeRepository;
import com.viettel.gnoc.incident.repository.TroubleWireRepository;
import com.viettel.gnoc.incident.repository.TroubleWorklogRepository;
import com.viettel.gnoc.incident.repository.TroublesRepository;
import com.viettel.gnoc.incident.repository.TroublesServiceForCCRepository;
import com.viettel.gnoc.incident.utils.TroubleBccsUtils;
import com.viettel.gnoc.incident.utils.TroubleTktuUtils;
import com.viettel.gnoc.wo.dto.RequestApiWODTO;
import com.viettel.gnoc.wo.dto.Wo;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import com.viettel.gnoc.ws.provider.WSCxfInInterceptor;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.w3c.dom.Element;

@Service
@Transactional
@Slf4j
public class TroublesServiceForCCBusinessImpl implements TroublesServiceForCCBusiness {

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

  @Autowired
  TroublesServiceForCCRepository troublesServiceForCCRepository;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  TroublesRepository troublesRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  CfgInfoTtSpmRepository cfgInfoTtSpmRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  CommonRepository commonRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TroubleWorklogRepository troubleWorklogRepository;

  @Autowired
  TroubleNodeRepository troubleNodeRepository;

  @Autowired
  TroubleWireRepository troubleWireRepository;

  @Autowired
  ItAccountRepository itAccountRepository;

  @Autowired
  ItSpmInfoRepository itSpmInfoRepository;

  @Autowired
  TroubleActionLogsRepository troubleActionLogsRepository;

  @Autowired
  MessagesRepository messagesRepository;

  @Autowired
  TroublesBusiness troublesBusiness;

  @Resource
  private WebServiceContext wsContext;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  TroubleBccsUtils troubleBccsUtils;

  @Autowired
  TroubleTktuUtils troubleTktuUtils;

  @Autowired
  WOCreateBusiness woCreateBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  TroubleBccsUtils bccsUtils;

  @Override
  public List<ResultDTO> onRollBackTroubleForCC(List<CommonDTO> lstComplaint) {
    List<ResultDTO> lstResult = new ArrayList<>();
    String spmCodeCurr = "";
    try {
      if (lstComplaint != null && !lstComplaint.isEmpty()) {
        for (CommonDTO commonDTO : lstComplaint) {
          spmCodeCurr = commonDTO.getComplaintId();
          ResultDTO resultDTO = new ResultDTO();
          if (StringUtils.isStringNullOrEmpty(spmCodeCurr)) {
            resultDTO.setMessage("complaintId is null");
            resultDTO.setKey(RESULT.FAIL);
            lstResult.add(resultDTO);
            continue;
          }
          String fromDate = DateUtil
              .date2ddMMyyyyHHMMss(new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000L));
          String toDate = DateUtil
              .date2ddMMyyyyHHMMss(new Date(new Date().getTime() + 24 * 60 * 60 * 1000L));
          List<TroublesInSideDTO> troubleDTOs = troublesServiceForCCRepository
              .getTroubleDTOForRollback(commonDTO.getTroubleCode(), commonDTO.getComplaintId(),
                  fromDate, toDate);
          if (troubleDTOs != null && !troubleDTOs.isEmpty()) {
            for (TroublesInSideDTO troubleDTO : troubleDTOs) {
              resultDTO = onRollBackTrouble(String.valueOf(troubleDTO.getTroubleId()),
                  troubleDTO.getWoCode());
            }
          } else {
            resultDTO.setMessage(I18n.getLanguage("incident.invalid.ticket"));
            resultDTO.setKey(RESULT.SUCCESS);
          }
          resultDTO.setId(commonDTO.getComplaintId());
          lstResult.add(resultDTO);
        }
      } else {
        ResultDTO resultDTO1 = new ResultDTO();
        resultDTO1.setKey(RESULT.FAIL);
        resultDTO1.setMessage("lstComplaint " + I18n.getLanguage("wo.close.trouble.isRequire"));
        lstResult.add(resultDTO1);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      ResultDTO resultDTO2 = new ResultDTO();
      resultDTO2.setKey(RESULT.FAIL);
      resultDTO2.setMessage(ex.toString());
      resultDTO2.setId(spmCodeCurr);
      lstResult.add(resultDTO2);
    }
    return lstResult;
  }

  private ResultDTO onRollBackTrouble(String troubleId, String woCode) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      troublesRepository.delete(Long.valueOf(troubleId));
      troubleActionLogsRepository.deleteTroubleActionLogsByTroubleId(Long.valueOf(troubleId));
      troubleWorklogRepository.deleteTroubleWorklogByTroubleId(Long.valueOf(troubleId));
      troublesRepository.deleteTroubleFileByTroubleId(Long.valueOf(troubleId));
      itSpmInfoRepository.deleteItSpmInfoByIncidentId(Long.valueOf(troubleId));
      itAccountRepository.deleteItAccountByIncidentId(Long.valueOf(troubleId));
      if (StringUtils.isNotNullOrEmpty(woCode)) {
        resultDTO = woServiceProxy.deleteWOForRollbackProxy(woCode, "rollback BCCS", "BCCS");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setId(null);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage("TT " + e.getMessage());
    }
    return resultDTO;
  }

  @Override
  public int onSearchCountForCC(TroublesDTO troublesDTO) {
    log.debug("Request to onSearchCountForCC: {}", troublesDTO);
    return troublesServiceForCCRepository.onSearchCountForCC(troublesDTO);
  }

  @Override
  public List<TroublesDTO> onSearchForCC(TroublesDTO troublesDTO, Integer startRow,
      Integer pageLength) {
    List<TroublesDTO> lstReturn = new ArrayList<>();
    try {
      String myLanguage = WSCxfInInterceptor.getLanguage();
      String locale = getLocale();
      if (StringUtils.isNotNullOrEmpty(locale)) {
        myLanguage = locale;
      }
      lstReturn = troublesServiceForCCRepository.onSearchForCC(troublesDTO, startRow, pageLength);
      Map<String, Object> map = DataUtil
          .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
              Constants.APPLIED_BUSSINESS.CAT_ITEM.toString(), myLanguage);
      String sqlLanguage = (String) map.get("sql");
      Map mapParam = (Map) map.get("mapParam");
      Map mapType = (Map) map.get("mapType");
      List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
          .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
      lstReturn = DataUtil.setLanguage(lstReturn, lstLanguage, "priorityId", "priorityName");
      lstReturn = DataUtil.setLanguage(lstReturn, lstLanguage, "state", "stateName");
      lstReturn = DataUtil.setLanguage(lstReturn, lstLanguage, "typeId", "typeName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstReturn;
  }

  @Override
  public ResultDTO getTroubleInfoForCC(TroublesDTO troublesDTO) {
    ResultDTO result = new ResultDTO();
    try {
      String startTime = DateTimeUtils.getSysDateTime();
      String validate = validateInputGetTroubleInfo(troublesDTO);
      if (validate.equals(RESULT.SUCCESS)) {
        List<TroublesDTO> lst = troublesServiceForCCRepository.getTroubleInfo(troublesDTO);
        if (lst != null && !lst.isEmpty()) {
          for (TroublesDTO troublesDTO1 : lst) {
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setBusinessId(Long.valueOf(troublesDTO1.getTroubleId()));
            List<GnocFileDto> lstFile = troublesRepository.getTroubleFileDTO(gnocFileDto);
            if (lstFile != null && !lstFile.isEmpty()) {
              List<String> lstName = new ArrayList<>();
              List<byte[]> lstByte = new ArrayList<>();
              for (GnocFileDto fileDTO : lstFile) {
                try {
                  byte[] bytes = FileUtils
                      .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                          PassTranformer.decrypt(ftpPass), fileDTO.getPath());
                  lstName.add(fileDTO.getFileName());
                  lstByte.add(bytes);
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                }
              }
              troublesDTO1.setArrFileName(lstName);
              troublesDTO1.setFileDocumentByteArray(lstByte);
            }
          }
        }
        result.setLstResult(lst);
      }
      String endTime = DateTimeUtils.getSysDateTime();
      if (validate.equals(RESULT.SUCCESS)) {
        result.setKey(RESULT.SUCCESS);
      } else {
        result.setKey(RESULT.FAIL);
      }
      result.setMessage(validate);
      result.setRequestTime(startTime);
      result.setFinishTime(endTime);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public ResultDTO onInsertTroubleForCC(TroublesDTO troublesDTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      List<byte[]> lstFileDocumentByteArray = new ArrayList<>();
      List<String> lstArrFileName = new ArrayList<>();
      if (troublesDTO.getArFileData() != null && troublesDTO.getArFileData().length > 0
          && troublesDTO.getArFileName() != null && troublesDTO.getArFileName().length > 0) {
        for (int i = 0; i < troublesDTO.getArFileData().length; i++) {
          byte[] fileData = troublesDTO.getArFileData()[i];
          String fileName = troublesDTO.getArFileName()[i];
          lstArrFileName.add(fileName);
          lstFileDocumentByteArray.add(fileData);
        }
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getInsertSource())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("trouble.insertSourceNull");
        return resultDTO;
      }
      String ret = validateDTO(troublesDTO);
      if ("".equals(ret)) {
        String id = troublesRepository.getSequenseTroubles("TROUBLES_SEQ", 1).get(0);
        troublesDTO.setTroubleId(String.valueOf(id));
        String itemCode = troublesDTO.getTypeName();
        String ptCode =
            "GNOC_TT_" + itemCode + "_" + DateTimeUtils.convertDateToString(new Date(), "yyMMdd")
                + "_" + id;
        troublesDTO.setTroubleCode(ptCode);
        troublesDTO.setArrFileName(lstArrFileName);
        troublesDTO.setFileDocumentByteArray(lstFileDocumentByteArray);
        TroublesInSideDTO troublesInSideDTO = troublesDTO.toModelInSide();
        resultDTO = doOnInsertTroubleForCC(troublesInSideDTO);
      } else {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(ret);
        resultDTO.setId(null);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
      resultDTO.setId(null);
      //tiennv them de rollback data neu co loi
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    return resultDTO;
  }

  @Override
  public TroublesInSideDTO findTroublesById(Long id) {
    log.debug("Request to findTroublesById: {}", id);
    TroublesInSideDTO troublesDTO = troublesRepository.findTroublesById(id).toDTO();
    return troublesDTO;
  }

  @Override
  public List<String> getSequenseTroubles(String seqName, int[] size) {
    log.debug("Request to getSequenseTroubles: {}", seqName);
    int number = (size[0] > 0 ? size[0] : 1);
    return troublesRepository.getSequenseTroubles(seqName, number);
  }

  @Override
  public ResultDTO reassignTicketForCC(TroublesDTO troublesDTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getTroubleCode())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("TroubleCode " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getBeginTroubleTime())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("BeginTroubleTime " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getBeginTroubleTime())) {
        try {
          Date startTime = DateTimeUtils.convertStringToDate(troublesDTO.getBeginTroubleTime());
          if (startTime.getTime() > new Date().getTime()) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage(I18n.getLanguage("incident.beginTroubleTimeMustBeLowerThanNow"));
            return resultDTO;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          resultDTO.setKey(RESULT.FAIL);
          resultDTO
              .setMessage("BeginTroubleTime " + I18n.getLanguage("wo.close.trouble.date.format"));
          return resultDTO;
        }
      }

      if (!"SPM_VTNET".equals(troublesDTO
          .getInsertSource())) { // 20200211_HaNV15_Note: Khac loai SPM_VTNET thi thuc hien nhu binh thuong
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getTimeProcess())) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage(I18n.getLanguage("incident.not.input.timeProcess"));
          return resultDTO;
        }
        try {
          Double timeProcess = Double.valueOf(troublesDTO.getTimeProcess());
          if (timeProcess <= 0) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO
                .setMessage("TimeProcess " + I18n.getLanguage("incident.timeProcess.validate"));
            return resultDTO;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("TimeProcess " + I18n.getLanguage("incident.timeProcess.validate"));
          return resultDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getProcessingUserName())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO
            .setMessage("ProcessingUserName " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getProcessingUnitName())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO
            .setMessage("ProcessingUnitName " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getRejectReason())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("RejectReason " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }

      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getDeferredTime())) {
        try {
          Date deferrTime = DateTimeUtils.convertStringToDate(troublesDTO.getDeferredTime());
          Date startTime = DateTimeUtils.convertStringToDate(troublesDTO.getBeginTroubleTime());
          if (deferrTime.getTime() < startTime.getTime()) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage(I18n.getLanguage("trouble.list.beginTroubleTime") + " "
                + I18n.getLanguage("common.less") + " "
                + I18n.getLanguage("incident.defferedDueTime")
            );
            return resultDTO;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("DeferredTime " + I18n.getLanguage("wo.close.trouble.date.format"));
          return resultDTO;
        }
      }
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getReceiveUnitId())) {
        UnitDTO unitDTO = new UnitDTO();
        unitDTO.setUnitCode(String.valueOf(troublesDTO.getReceiveUnitId()));

        List<UnitDTO> u = unitRepository.getUnitByUnitDTO(unitDTO);
        if (u == null || u.isEmpty()) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("incident.receiveUnitNotExist");
          return resultDTO;
        }
        troublesDTO.setReceiveUnitId(String.valueOf(u.get(0).getUnitId()));
        troublesDTO
            .setReceiveUnitName(u.get(0).getUnitName() + " (" + u.get(0).getUnitCode() + ")");
      }
      TroublesInSideDTO troublesInSideDTO = troublesDTO.toModelInSide();
      ResultInSideDto resultInSideDto = doReassignTicketForCC(troublesInSideDTO);
      resultDTO
          .setId(resultInSideDto.getId() != null ? RESULT.SUCCESS : null);
      resultDTO.setKey(resultInSideDto.getKey());
      resultDTO.setMessage(resultInSideDto.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setId(null);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
    }
    return resultDTO;
  }

  @Override
  public List<TroubleActionLogsDTO> getListTroubleActionLog(String troubleCode) {
    log.debug("Request to getListTroubleActionLog: {}", troubleCode);
    return troublesServiceForCCRepository.getListTroubleActionLog(troubleCode);
  }

  @Override
  public List<TroubleWorklogInsiteDTO> getListWorkLog(String troubleCode) {
    log.debug("Request to getListWorkLog: {}", troubleCode);
    String troubleId = troubleCode.substring(troubleCode.lastIndexOf("_") + 1);
    List<ConditionBean> lstCondition = new ArrayList<>();
    lstCondition.add(new ConditionBean("troubleId", troubleId, "NAME_EQUAL", "NUMBER"));
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    return troublesServiceForCCRepository
        .searchByConditionBean(lstCondition, 0, Integer.MAX_VALUE, "desc", "createdTime");
  }

  @Override
  public String getConcaveByTicket(String troubleCode) throws Exception {
    log.debug("Request to getConcaveByTicket: {}", troubleCode);
    return troubleTktuUtils.getConcaveByTicket(troubleCode);
  }

  @Override
  public ResultDTO sendTicketToTKTU(TroublesInSideDTO tForm) throws Exception {
    log.debug("Request to sendTicketToTKTU: {}", tForm);
    return troubleTktuUtils.sendTicketToTKTU(tForm);
  }

  @Override
  public List<ConcaveDTO> getConcaveByCellAndLocation(List<String> lstCell, String lng,
      String lat) throws Exception {
    log.debug("Request to getConcaveByCellAndLocation: {}", lstCell);
    Map<String, String> mapProperty = troublesServiceForCCRepository.getConfigProperty();
    String radius = mapProperty.get("RADIUS_TKTU");
    return troubleTktuUtils.getConcaveByCellAndLocation(lstCell, lng, lat, radius);
  }

  @Override
  public List<UnitDTO> getListUnitByTrouble(String troubleCode) {
    log.debug("Request to getListUnitByTrouble: {}", troubleCode);
    return troublesServiceForCCRepository.getListUnitByTrouble(troubleCode);
  }

  @Override
  public ResultDTO onUpdateTroubleFromTKTU(TroublesInSideDTO tForm) throws Exception {
    log.debug("Request to onUpdateTroubleFromTKTU: {}", tForm);
    ResultInSideDto result = new ResultInSideDto();
    try {
      if (StringUtils.isStringNullOrEmpty(tForm.getTroubleCode())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("TroubleCode " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result.toResultDTO();
      }
      TroublesInSideDTO troubleDTO = troublesRepository
          .getTroubleDTO(null, tForm.getTroubleCode(), null, null, null, null, null);
      result = validateTKTU(tForm, troubleDTO);
      if (!result.getKey().equals(Constants.RESULT.SUCCESS)) {
        return result.toResultDTO();
      }
      troubleDTO.setProcessingUnitName(tForm.getReceiveUnitName());
      troubleDTO.setProcessingUnitId(String.valueOf(troubleDTO.getCreateUnitId()));
      troubleDTO.setProcessingUserName(tForm.getReceiveUserName());
      String oldWorkLog = troubleDTO.getWorkLog() == null ? "" : (troubleDTO.getWorkLog() + " \n ");
      troubleDTO
          .setWorkLog(oldWorkLog + DateTimeUtils.getSysDateTime() + " TKTU " + tForm.getWorkLog());
      if (tForm.getStateName().equals(Constants.TT_STATE.DEFERRED)) {
        troubleDTO.setDeferredTime(tForm.getDeferredTime());
        troubleDTO.setEstimateTime(tForm.getEstimateTime());
        troubleDTO.setCellService(tForm.getCellService());
        troubleDTO.setLongitude(tForm.getLongitude());
        troubleDTO.setLatitude(tForm.getLatitude());
        troubleDTO.setConcave(tForm.getConcave());
        String workArroundOld =
            troubleDTO.getWorkArround() == null ? "" : (troubleDTO.getWorkArround() + " \n ");
        troubleDTO.setWorkArround(
            workArroundOld + DateTimeUtils.getSysDateTime() + " TKTU " + tForm.getWorkArround());
        if (!(7 == (troubleDTO.getState()))) {
          result.setMessage(RESULT.SUCCESS);
          result.setKey(RESULT.SUCCESS);
          return result.toResultDTO();
        }
        troubleDTO.setCheckbox("TKTU_1");
        result = troublesBusiness.onUpdateTrouble(troubleDTO, true);
      } else if (tForm.getStateName().equals(Constants.TT_STATE.CLOSED)) {
        troubleDTO.setWorkArround(tForm.getWorkArround());
        if (10 == (troubleDTO.getState()) || 11 == (troubleDTO.getState())
            || troubleDTO.getAutoClose() == null || !troubleDTO.getAutoClose().equals(1L)) {
          result.setMessage(RESULT.SUCCESS);
          result.setKey(RESULT.SUCCESS);
          return result.toResultDTO();

        } else {
          troubleDTO.setCheckbox("TKTU_2");
          if (DataUtil.isNullOrEmpty(troubleDTO.getRelatedKedb())) {
            troubleDTO.setState(10L);
            troubleDTO.setStateName(Constants.TT_STATE.CLOSED_Not_KEDB);
          } else {
            troubleDTO.setState(11L);
            troubleDTO.setStateName(Constants.TT_STATE.CLOSED);
          }
        }
        result = troublesBusiness.onUpdateTrouble(troubleDTO, true);
        if ("SUCCESS".equals(result.getKey())) {
          String smsContent = "message.closed.tktu";
          sendSms(troubleDTO, smsContent);
        }
      } else if (tForm.getStateName().equals(Constants.TT_STATE.QUEUE)) {
        if (!(7 == (troubleDTO.getState()))) {
          result.setMessage(RESULT.SUCCESS);
          result.setKey(RESULT.SUCCESS);
          return result.toResultDTO();
        }
        troubleDTO.setState(5L);
        troubleDTO.setStateName(Constants.TT_STATE.QUEUE);
        troubleDTO.setCheckbox("TKTU_3");
        result = troublesBusiness.onUpdateTrouble(troubleDTO, true);
        if (result.getKey().equals(RESULT.SUCCESS)) {
          String smsContent = "message.open.defer.tktu";
          sendSms(troubleDTO, smsContent);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setId(null);
      result.setKey(RESULT.FAIL);
      result.setMessage(e.getMessage());
      throw e;
    }
    ResultDTO resultDTO = result.toResultDTO();
    resultDTO.setId(tForm.getTroubleCode());
    return resultDTO;
  }

  @Override
  public ResultDTO onUpdateTroubleCC(TroublesDTO tForm) throws Exception {
    validateOnUpdateTTForCC(tForm);
    ResultDTO resultDTO = new ResultDTO();
    try {
      TroublesInSideDTO troubleDTO = troublesRepository
          .getTroubleDTO(null, tForm.getTroubleCode(), null, null, null, null, null);
      if (troubleDTO == null) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(I18n.getLanguage("incident.invalid.ticket"));
        return resultDTO;
      }
      TroublesInSideDTO trouble = troublesBusiness
          .findTroublesById(troubleDTO.getTroubleId());
      TroubleActionLogsDTO troubleActionLogsDTO = new TroubleActionLogsDTO(null,
          troubleDTO.compareContent(trouble.toEntity()), new Date(),
          trouble.getCreateUnitId(), trouble.getCreateUserId(),
          I18n.getLanguage("common.btn.update"), trouble.getTroubleId(),
          tForm.getProcessingUnitName(), tForm.getProcessingUserName(),
          Long.valueOf(troubleDTO.getState()),
          troubleDTO.getTroubleCode(), troubleDTO.getStateName(), null, null);
      if ("UPDATE_HOT".equals(tForm.getStateName())) { //tich hot
        boolean check = false;
        try {
          if (!StringUtils.isStringNullOrEmpty(tForm.getTimeProcess()) && !StringUtils
              .isStringNullOrEmpty(troubleDTO.getRemainTime())
              && Double.parseDouble(tForm.getTimeProcess()) < Double
              .parseDouble(troubleDTO.getRemainTime())) {
            trouble.setAssignTime(new Date());
            trouble.setAssignTimeTemp(new Date());
            trouble.setTimeProcess(Double.parseDouble(tForm.getTimeProcess()));
            trouble.setTimeUsed(null);
            check = true;

          }

        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }

        //goi WO
        if (!StringUtils.isStringNullOrEmpty(troubleDTO.getWoCode())) {

          Map<String, String> mapProperty = troublesServiceForCCRepository.getConfigProperty();
          String timeRate = mapProperty.get("TT.TIME.PER.WO.TIME");
          Long time = 1L;
          Long totalTime = 1L;
          if (timeRate != null && !"".equals(timeRate)) {
            time = Long.valueOf(timeRate.split(":")[0]);
            totalTime = Long.valueOf(timeRate.split(":")[0]) + Long.valueOf(timeRate.split(":")[1]);
          }

          WoDTO wdto = new WoDTO();
          wdto.setIsHot("1");
          wdto.setWoCode(troubleDTO.getWoCode());
          if (check) {
            wdto.setEstimateTime(
                (Double.parseDouble(tForm.getTimeProcess()) * time / totalTime) + "");
          }
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

        CatItemDTO prio = new CatItemDTO();
        prio.setCategoryCode("PRIRORITY_TT_CC");
        prio.setItemCode("HOT");
        List<CatItemDTO> lstCatItem = catItemRepository.getListCatItemDTO(prio);
        if (lstCatItem != null && !lstCatItem.isEmpty()) {
          prio = new CatItemDTO();
          prio.setItemCode(lstCatItem.get(0).getItemValue().toLowerCase());
          List<CatItemDTO> lstCat = catItemRepository.getListCatItemDTO(prio);
          if (lstCat != null && !lstCat.isEmpty()) {
            trouble.setPriorityId(lstCat.get(0).getItemId());
          }
        } else {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage(I18n.getLanguage("priority.config.gnoc.spm"));
          return resultDTO;
        }

        troubleActionLogsDTO.setContent(
            I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS
                + " "
                + I18n.getLanguage("tt.tich.hot"));
        troublesRepository.updateTroubles(trouble.toEntity());
        troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO.toEntity());

      } else if ("UPDATE_WORKLOG".equals(tForm.getStateName())) { //cap nhat ghi chu
        //goi WO
        if (!StringUtils.isStringNullOrEmpty(troubleDTO.getWoCode())) {
          WoDTO wdto = new WoDTO();
          wdto.setWoDescription(tForm.getWorkLog().trim());
          wdto.setWoCode(troubleDTO.getWoCode());
          wdto.setCustomerTimeDesireFrom(tForm.getCustomerTimeDesireFrom());
          wdto.setCustomerTimeDesireTo(tForm.getCustomerTimeDesireTo());
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
        trouble.setWorkLog(
            (trouble.getWorkLog() == null ? "" : (trouble.getWorkLog() + " \n ")) + DateTimeUtils
                .getSysDateTime() + " " + tForm.getWorkLog().trim());

        troubleDTO.setWorkLog(tForm.getWorkLog().trim());
        troubleDTO.setCreateUnitName(tForm.getProcessingUnitName());
        troubleDTO.setCreateUserName(tForm.getProcessingUserName());
        troubleDTO
            .setLastUpdateTime(DateTimeUtils.convertStringToDate(DateTimeUtils.getSysDateTime()));
        onInsertWorkLog(troubleDTO);

        troubleActionLogsDTO.setContent(
            I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS
                + " "
                + I18n.getLanguage("spm.update.worklog"));
        troublesRepository.updateTroubles(trouble.toEntity());
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
        if (!StringUtils.isStringNullOrEmpty(troubleDTO.getWoCode())) {
          ResultDTO rdto = new ResultDTO();
          if ("UPDATE_OPEN_DEFERRED".equals(tForm.getStateName())) {
            tForm.setDeferredTime(null);
            rdto = woServiceProxy
                .updatePendingWo(
                    new RequestApiWODTO(troubleDTO.getWoCode(), tForm.getDeferredTime(),
                        tForm.getProcessingUserName(), tForm.getWorkLog().trim(), "CC", false));
          } else if ("UPDATE_DEFERRED".equals(tForm.getStateName())) {
            rdto = woServiceProxy
                .pendingWo(new RequestApiWODTO(troubleDTO.getWoCode(), tForm.getDeferredTime(),
                    tForm.getProcessingUserName(), "CC", tForm.getWorkLog().trim(), "", "", ""));
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
        trouble.setWorkLog(
            (trouble.getWorkLog() == null ? "" : (trouble.getWorkLog() + " \n ")) + tForm
                .getWorkLog().trim());

        troubleDTO.setWorkLog(tForm.getWorkLog().trim());
        troubleDTO.setCreateUnitName(tForm.getProcessingUnitName());
        troubleDTO.setCreateUserName(tForm.getProcessingUserName());
        troubleDTO.setLastUpdateTime(
            DateTimeUtils.convertStringToDateTime(DateTimeUtils.getSysDateTime()));
        onInsertWorkLog(troubleDTO);
        if ("UPDATE_OPEN_DEFERRED".equals(tForm.getStateName())) { //mo tam dong
          trouble.setState(5L);
          trouble.setAssignTimeTemp(new Date());

          troubleActionLogsDTO.setStateId(5L);
          CatItemDTO stateItem = catItemRepository
              .getCatItemById(trouble.getState());
          troubleActionLogsDTO.setStateName(stateItem.getItemName());
          troubleActionLogsDTO.setContent(
              I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS
                  + " "
                  + I18n.getLanguage("spm.update.open"));

        } else if ("UPDATE_DEFERRED".equals(tForm.getStateName())) { //tam dong
          //cap nhat lai thoi gian
          trouble.setDeferredTime(DateTimeUtils.convertStringToDateTime(tForm.getDeferredTime()));

          troubleActionLogsDTO.setStateId(7L);
          CatItemDTO stateItem = catItemRepository
              .getCatItemById(trouble.getState());
          troubleActionLogsDTO.setStateName(stateItem.getItemName());
          troubleActionLogsDTO.setContent(
              I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS
                  + " "
                  + I18n.getLanguage("spm.update.delay") + " " + tForm
                  .getDeferredTime());
        }
        troublesRepository.updateTroubles(trouble.toEntity());
        troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO.toEntity());

      } else if ("UPDATE_CLOSED".equals(tForm.getStateName())) {
        if (trouble.getState().equals(11L) || trouble.getState().equals(10L)) {
        } else {
          if (trouble.getWoCode() != null) {
            List<Wo> lstWo = new ArrayList<>();
            Wo wo = new Wo();
            wo.setWoCode(trouble.getWoCode());
            wo.setWoSystemId(trouble.getTroubleCode());
            lstWo.add(wo);
            resultDTO = woServiceProxy
                .closeWoForSPM(new RequestApiWODTO(lstWo, "CC", tForm.getProcessingUserName(),
                    Long.parseLong(tForm.getReasonLv3Id())));
            if (!"SUCCESS".equals(resultDTO.getKey())) {
              resultDTO.setKey(RESULT.FAIL);
              resultDTO.setMessage("WO: " + resultDTO.getMessage());
              return resultDTO;
            }
          }
          if (!StringUtils.isStringNullOrEmpty(trouble.getRelatedKedb())) {
            trouble.setState(11L);
          } else {
            trouble.setState(10L);
          }
          trouble.setEndTroubleTime(
              trouble.getEndTroubleTime() == null ? new Date() : trouble.getEndTroubleTime());
          trouble
              .setClearTime(trouble.getClearTime() == null ? new Date() : trouble.getClearTime());
          trouble.setClosedTime(
              trouble.getClosedTime() == null ? new Date() : trouble.getClosedTime());
          trouble.setLastUpdateTime(new Date());
          troubleActionLogsDTO.setStateId(11L);
          CatItemDTO stateItem = catItemRepository
              .getCatItemById(trouble.getState());
          troubleActionLogsDTO.setStateName(stateItem.getItemName());
          troubleActionLogsDTO.setContent(
              I18n.getLanguage("common.btn.update") + " " + RESULT.SUCCESS
                  + " "
                  + I18n.getLanguage("spm.update.close"));

          String temp = "###" + trouble.getTroubleCode() + ";0;" + troubleDTO.getTimeUsed();
          if (troubleDTO.getRemainTime() != null
              && Double.parseDouble(troubleDTO.getRemainTime()) < 0) {
            temp = temp + ";0" + "###";
          } else {
            temp = temp + ";1" + "###";
          }
          resultDTO.setId(temp);

          troublesRepository.updateTroubles(trouble.toEntity());
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

  @Override
  public ResultDTO onUpdateTroubleFromWo(TroublesDTO tForm) throws Exception {
    ResultDTO result = new ResultDTO();
    try {
      if (StringUtils.isStringNullOrEmpty(tForm.getTroubleCode())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("TroubleCode" + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      TroublesInSideDTO troubleDTO = troublesRepository
          .getTroubleDTO(null, tForm.getTroubleCode(), null, null, null, null, null);
      result = validate(tForm, troubleDTO);
      if (!result.getKey().equals(RESULT.SUCCESS)) {
        return result;
      }
      troubleDTO.setProcessingUnitName(tForm.getReceiveUnitName());
      troubleDTO.setProcessingUnitId(String.valueOf(tForm.getReceiveUnitId()));
      troubleDTO.setProcessingUserName(tForm.getReceiveUserName());
      troubleDTO.setProcessingUserPhone(tForm.getProcessingUserPhone());
      troubleDTO.setTotalPendingTimeGnoc(tForm.getTotalPendingTimeGnoc());
      troubleDTO.setTotalProcessTimeGnoc(tForm.getTotalProcessTimeGnoc());
      troubleDTO.setEvaluateGnoc(tForm.getEvaluateGnoc());
      troubleDTO.setStateWo(tForm.getStateWo());
      troubleDTO.setSpmName("MOBILE");
      troubleDTO.setWorkLog(tForm.getWorkLog());
      troubleDTO.setCustomerTimeDesireFrom(tForm.getCustomerTimeDesireFrom());
      troubleDTO.setCustomerTimeDesireTo(tForm.getCustomerTimeDesireTo());
      troubleDTO.setCheckbox(tForm.getCheckbox());
      if (tForm.getStateName().equals(Constants.TT_STATE.DEFERRED)) {
        troubleDTO.setDeferType(
            StringUtils.validString(tForm.getDeferType()) ? Long.valueOf(tForm.getDeferType())
                : null);
        troubleDTO.setDeferredTime(StringUtils.validString(tForm.getDeferredTime()) ? DateTimeUtils
            .convertStringToDate(tForm.getDeferredTime()) : null);
        troubleDTO.setDeferredReason(tForm.getDeferredReason());
        troubleDTO.setEstimateTime(StringUtils.validString(tForm.getEstimateTime()) ? DateTimeUtils
            .convertStringToDate(tForm.getEstimateTime()) : null);
        troubleDTO.setNumPending(
            StringUtils.validString(tForm.getNumPending()) ? Long.valueOf(tForm.getNumPending())
                : null);
        //truongnt comment khong luu groupSolution
//        troubleDTO.setGroupSolution(StringUtils.validString(tForm.getGroupSolution()) ? Long
//            .valueOf(tForm.getGroupSolution()) : null);
        troubleDTO.setCellService(tForm.getCellService());
        troubleDTO.setLongitude(tForm.getLongitude());
        troubleDTO.setLatitude(tForm.getLatitude());
        troubleDTO.setConcave(tForm.getConcave());
        if (7 == (troubleDTO.getState())) {
          result.setMessage(RESULT.SUCCESS);
          result.setKey(RESULT.SUCCESS);
          return result;
        }
        troubleDTO.setState(7L);
        troubleDTO.setStateName(Constants.TT_STATE.DEFERRED);
        troubleDTO.setCheckbox("7");
        ResultInSideDto resultInSideDto = troublesBusiness.onUpdateTrouble(troubleDTO, false);
        result.setId(
            tForm.getTroubleCode());
        result.setKey(resultInSideDto.getKey());
        result.setMessage(resultInSideDto.getMessage());
      } else if (tForm.getStateName().equals(Constants.TT_STATE.CLOSED)) {
//        troubleDTO.setGroupSolution(StringUtils.validString(tForm.getGroupSolution()) ? Long
//            .valueOf(tForm.getGroupSolution()) : null);
        troubleDTO.setCellService(tForm.getCellService());
        troubleDTO.setLongitude(tForm.getLongitude());
        troubleDTO.setLatitude(tForm.getLatitude());
        troubleDTO.setConcave(tForm.getConcave());
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
        troubleDTO.setEndTroubleTime(
            StringUtils.validString(tForm.getEndTroubleTime()) ? DateTimeUtils
                .convertStringToDate(tForm.getEndTroubleTime()) : null);
        troubleDTO.setReasonId(
            StringUtils.validString(tForm.getReasonId()) ? Long.valueOf(tForm.getReasonId())
                : null);
        troubleDTO.setReasonName(tForm.getReasonName());
        troubleDTO.setSolutionType(
            StringUtils.validString(tForm.getSolutionType()) ? Long.valueOf(tForm.getSolutionType())
                : null);
        troubleDTO.setWorkArround(tForm.getWorkArround());
        troubleDTO.setRootCause(tForm.getRootCause());
        troubleDTO.setInfoTicket(tForm.getInfoTicket());

        boolean check = true;
        if (10 == (troubleDTO.getState()) || 11 == (troubleDTO.getState())) {
          result.setMessage(RESULT.SUCCESS);
          result.setKey(RESULT.SUCCESS);
          return result;
        } else if (tForm.getCustomerTimeDesireTo() != null && (
            "1".equals(tForm.getCustomerTimeDesireTo())
                || "5".equals(tForm.getCustomerTimeDesireTo())
                || "6".equals(tForm.getCustomerTimeDesireTo()))) {
          check = false;
          troubleDTO.setCheckbox("4");
          if (StringUtils.isStringNullOrEmpty(troubleDTO.getRelatedKedb())) {
            troubleDTO.setState(10L);
            troubleDTO.setStateName(Constants.TT_STATE.CLOSED_Not_KEDB);
          } else {
            troubleDTO.setState(11L);
            troubleDTO.setStateName(Constants.TT_STATE.CLOSED);
          }
        } else {
          troubleDTO.setCheckbox("8");
        }
        ResultInSideDto resultInSideDto = troublesBusiness.onUpdateTrouble(troubleDTO, false);
        result.setId(tForm.getTroubleCode());
        result.setKey(resultInSideDto.getKey());
        result.setMessage(resultInSideDto.getMessage());
        if ("SUCCESS".equals(result.getKey()) && check) {
          String smsContent = "message.closed.WO";
          sendSms(troubleDTO, smsContent);
        }
      } else if (tForm.getStateName().equals(Constants.TT_STATE.QUEUE)) {
        if (!(7 == (troubleDTO.getState()))) {
          result.setMessage(RESULT.SUCCESS);
          result.setKey(RESULT.SUCCESS);
          return result;
        }
        troubleDTO.setState(5L);
        troubleDTO.setStateName(Constants.TT_STATE.QUEUE);
        troubleDTO.setProcessingUnitId(tForm.getReceiveUnitId());
        troubleDTO.setProcessingUnitName(tForm.getReceiveUnitName());
        troubleDTO.setProcessingUserName(tForm.getReceiveUserName());
        troubleDTO.setCheckbox("6");
        String smsContent = "message.open.defer.WO";
        sendSms(troubleDTO, smsContent);
        ResultInSideDto resultInSideDto = troublesBusiness.onUpdateTrouble(troubleDTO, false);
        result.setId(tForm.getTroubleCode());
        result.setKey(resultInSideDto.getKey());
        result.setMessage(resultInSideDto.getMessage());
      } else if ("UPDATE".equals(tForm.getStateName())) {
        TroublesInSideDTO troubles = troublesBusiness.findTroublesById(troubleDTO.getTroubleId());
        troubleDTO.setWorkLog(tForm.getWorkLog().trim());
        troubleDTO.setCreateUnitName(tForm.getReceiveUnitName());
        troubleDTO.setCreateUserName(tForm.getReceiveUserName());
        troubleDTO.setLastUpdateTime(new Date());
        onInsertWorkLog(troubleDTO);
        troubles.setWorkLog(
            DateTimeUtils.getSysDateTime() + " " + (troubles.getWorkLog() == null ? ""
                : (troubles.getWorkLog() + "\n")) + tForm.getWorkLog().trim());
        troubleDTO.setProcessingUnitName(tForm.getReceiveUnitName());
        troubleDTO.setProcessingUnitId(tForm.getReceiveUnitId());
        troubleDTO.setProcessingUserName(tForm.getReceiveUserName());
        troubleDTO.setProcessingUserPhone(tForm.getProcessingUserPhone());
        troubleDTO.setComplaintId(troubles.getComplaintId());
        CatItemDTO catItemDTO = catItemRepository
            .getCatItemById(troubles.getTypeId());
        troubleDTO.setTypeName(catItemDTO.getItemCode());
        catItemDTO = catItemRepository.getCatItemById(troubles.getState());
        troubleDTO.setStateName(catItemDTO.getItemName());
        CfgServerNocDTO cfgServerNocDTO = new CfgServerNocDTO();
        cfgServerNocDTO.setInsertSource(troubles.getInsertSource());
        cfgServerNocDTO = troublesRepository.getServerDTO(cfgServerNocDTO);
        // khong luu groupSolution
        tForm.setGroupSolution(null);
        TroublesInSideDTO troublesInSideDTO = tForm.toModelInSide();
        troubleBccsUtils
            .updateCompProcessing(messagesRepository, 1, troublesInSideDTO, cfgServerNocDTO);
        TroubleActionLogsDTO troubleActionLogsDTO = new TroubleActionLogsDTO(null,
            troubleDTO.getWorkLog(),
            new Date(),
            troubleDTO.getProcessingUnitId() == null ? null
                : Long.parseLong(troubleDTO.getProcessingUnitId()),
            troubleDTO.getProcessingUserId() == null ? null
                : Long.parseLong(troubleDTO.getProcessingUserId()),
            I18n.getLanguage("common.btn.update"), troubles.getTroubleId(),
            troubleDTO.getProcessingUnitName(), troubleDTO.getProcessingUserName(),
            troubleDTO.getState(),
            tForm.getTroubleCode(), troubleDTO.getStateName(), null, null);
        troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO.toEntity());
        troublesRepository.updateTroubles(troubles.toEntity());
      } else if ("IS_HELP".equals(tForm.getStateName())) {
        TroublesInSideDTO troubles = troublesBusiness
            .findTroublesById(troubleDTO.getTroubleId());
        troubles.setIsTickHelp(1L);
        Long numHelp = troubles.getNumHelp() == null ? 1L : troubles.getNumHelp() + 1;
        if (troubles.getAutoClose() != null && troubles.getAutoClose().equals(1L)
            && troubles.getNumHelp() == null) {
          Map<String, String> mapProperty = troublesServiceForCCRepository.getConfigProperty();
          String timeRate = mapProperty.get("TT.TIME.PER.WO.TIME");
          Long time = 1L;
          Long totalTime = 1L;
          if (timeRate != null && !"".equals(timeRate)) {
            time = Long.valueOf(timeRate.split(":")[0]);
            totalTime = Long.valueOf(timeRate.split(":")[0]) + Long.valueOf(timeRate.split(":")[1]);
          }
          troubles.setTimeProcess(troubles.getTimeProcess() * time / totalTime);
          troubles.setAssignTimeTemp(new Date());
          troubles.setClearTime(null);
          troubles.setState(5L);
        }
        troubles.setNumHelp(numHelp);
        troubles.setWorkLog(
            (troubles.getWorkLog() == null ? "" : (troubles.getWorkLog() + "\n")) + tForm
                .getWorkLog().trim());
        troublesRepository.updateTroubles(troubles.toEntity());

        troubleDTO.setWorkLog(
            I18n.getLanguage("tt.tich.help") + " " + tForm.getWorkLog().trim());
        troubleDTO.setCreateUnitName(tForm.getReceiveUnitName());
        troubleDTO.setCreateUserName(tForm.getReceiveUserName());
        troubleDTO.setLastUpdateTime(new Date());
        onInsertWorkLog(troubleDTO);

        troubleDTO.setProcessingUnitName(tForm.getReceiveUnitName());
        troubleDTO.setProcessingUnitId(tForm.getReceiveUnitId());
        troubleDTO.setProcessingUserName(tForm.getReceiveUserName());
        troubleDTO.setProcessingUserPhone(tForm.getProcessingUserPhone());

        CfgServerNocDTO cfgServerNocDTO = new CfgServerNocDTO();
        cfgServerNocDTO.setInsertSource(troubles.getInsertSource());
        cfgServerNocDTO = troublesRepository.getServerDTO(cfgServerNocDTO);
        troubleBccsUtils.updateCompProcessing(messagesRepository, 1, troubleDTO, cfgServerNocDTO);

        TroubleActionLogsDTO troubleActionLogsDTO = new TroubleActionLogsDTO(null,
            troubleDTO.getWorkLog(),
            new Date(),
            troubleDTO.getProcessingUnitId() == null ? null
                : Long.parseLong(troubleDTO.getProcessingUnitId()),
            troubleDTO.getProcessingUserId() == null ? null
                : Long.parseLong(troubleDTO.getProcessingUserId()),
            I18n.getLanguage("common.btn.update"), troubles.getTroubleId(),
            troubleDTO.getProcessingUnitName(), troubleDTO.getProcessingUserName(),
            troubleDTO.getState(),
            tForm.getTroubleCode(), troubleDTO.getStateName(), null, null);
        troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO.toEntity());

        String smsContent = "message.help.from.wo";
        troubleDTO.setTroubleCode(troubles.getTroubleCode());
        troubleDTO.setTroubleName(troubles.getTroubleName());
        sendSms(troubleDTO, smsContent);

        result.setMessage(RESULT.SUCCESS);
        result.setKey(RESULT.SUCCESS);
      }
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      log.error(e.getMessage());
      result.setId(null);
      result.setKey(RESULT.FAIL);
      result.setMessage(e.getMessage());
      throw e;
    }
    return result;
  }

  @Override
  public List<WoHistoryDTO> getListWoLog(String troubleCode) {
    log.debug("Request to getListWoLog: {}", troubleCode);
    TroublesInSideDTO troublesDTO = troublesRepository
        .getTroubleDTO(null, troubleCode, null, null, null, null, null);
    List<WoHistoryDTO> lst = new ArrayList<>();
    if (troublesDTO != null) {
      try {
        lst = woServiceProxy
            .getListWoHistoryBySystem(
                new RequestApiWODTO("", "", "", troublesDTO.getTroubleCode(), "", ""));
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return lst;
  }

  @Override
  public List<CauseDTO> getCompCauseDTOForCC3(String parentId, String serviceTypeId,
      String probGroupId, CfgServerNocDTO nocDTO) {
    return bccsUtils.getCompCauseDTO(parentId, serviceTypeId, probGroupId, nocDTO);
  }

  @Override
  public List<TroubleNetworkSolutionDTO> getGroupSolution(CfgServerNocDTO nocDTO) {
    return bccsUtils.getGroupSolution(nocDTO);
  }

  public ResultDTO validate(TroublesDTO tForm, TroublesInSideDTO troubleDTO) {
    ResultDTO result = new ResultDTO();
    result.setKey(RESULT.SUCCESS);
    if (troubleDTO == null || troubleDTO.getTroubleId() == null) {
      result.setKey(RESULT.FAIL);
      result.setMessage(I18n.getLanguage("incident.invalid.ticket"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getReceiveUnitId())) {
      result.setKey(RESULT.FAIL);
      result.setMessage("ReceiveUnitId " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getReceiveUserId())) {
      result.setKey(RESULT.FAIL);
      result.setMessage("ReceiveUserId " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getReceiveUnitName())) {
      result.setKey(RESULT.FAIL);
      result.setMessage("ReceiveUnitName " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getReceiveUserName())) {
      result.setKey(RESULT.FAIL);
      result.setMessage("ReceiveUserName " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getProcessingUserPhone())) {
      result.setKey(RESULT.FAIL);
      result.setMessage("ProcessingUserPhone " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getStateName())) {
      result.setKey(RESULT.FAIL);
      result.setMessage("StateName " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getWorkLog())) {
      result.setKey(RESULT.FAIL);
      result.setMessage("WorkLog " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (Constants.TT_STATE.DEFERRED.equals(tForm.getStateName())) {
      if (StringUtils.isStringNullOrEmpty(tForm.getDeferredReason())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("DeferredReason " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getDeferType())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("DeferType " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if ("2".equals(tForm.getDeferType())) {
        if (StringUtils.isStringNullOrEmpty(tForm.getCellService())) {
          result.setKey(RESULT.FAIL);
          result.setMessage("CellService " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return result;
        }
        if (StringUtils.isStringNullOrEmpty(tForm.getLongitude())) {
          result.setKey(RESULT.FAIL);
          result.setMessage("Longitude " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return result;
        }
        if (StringUtils.isStringNullOrEmpty(tForm.getLatitude())) {
          result.setKey(RESULT.FAIL);
          result.setMessage("Latitude " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return result;
        }
      }
      if (!StringUtils.isStringNullOrEmpty(tForm.getLongitude())) {
        try {
          Double dValue = new Double(tForm.getLongitude().trim());
          if (dValue < 102 || dValue >= 115) {
            result.setKey(RESULT.FAIL);
            result.setMessage(I18n.getLanguage("incident.msgErrorLongitudeReal"));
            return result;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("incident.msgErrorLongitudeReal"));
          return result;
        }
      }
      if (!StringUtils.isStringNullOrEmpty(tForm.getLatitude())) {
        try {
          Double dValue = new Double(tForm.getLatitude().trim());
          if (dValue < 7 || dValue >= 24) {
            result.setKey(RESULT.FAIL);
            result.setMessage(I18n.getLanguage("incident.msgErrorLatitudeReal"));
            return result;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("incident.msgErrorLatitudeReal"));
          return result;
        }
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getDeferredTime())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("DeferredTime " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getEstimateTime())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("EstimateTime " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getGroupSolution())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("GroupSolution " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getNumPending())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("NumPending " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      Date now = new Date();
      try {
        Date deferredTime = DateTimeUtils.convertStringToDateTime(tForm.getDeferredTime());
        if (deferredTime.compareTo(now) < 0) {
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("incident.deferredTimeMustBeBiggerThanNow"));
          return result;
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        result.setKey(RESULT.FAIL);
        result.setMessage("DeferredTime " + I18n.getLanguage("wo.close.trouble.date.format"));
        return result;
      }
      try {
        Date estimate = DateTimeUtils.convertStringToDateTime(tForm.getEstimateTime());
        if (estimate.compareTo(now) < 0) {
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("incident.estimateTime") + " " +
              I18n.getLanguage("common.greater") + " " + I18n.getLanguage("ptMngt.addpt.now"));
          return result;
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        result.setKey(RESULT.FAIL);
        result.setMessage("EstimateTime " + I18n.getLanguage("wo.close.trouble.date.format"));
        return result;
      }
    } else if (Constants.TT_STATE.CLOSED.equals(tForm.getStateName())) {
      if (StringUtils.isStringNullOrEmpty(tForm.getEndTroubleTime())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("EndTime " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      try {
        DateTimeUtils.convertStringToDateTime(tForm.getEndTroubleTime());
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        result.setKey(RESULT.FAIL);
        result.setMessage("EndTime " + I18n.getLanguage("wo.close.trouble.date.format"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getGroupSolution())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("GroupSolution " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
//      if (StringUtils.isStringNullOrEmpty(tForm.getCellService())) {
//        result.setKey(RESULT.FAIL);
//        result.setMessage("CellService " + I18n.getLanguage("wo.close.trouble.isRequire"));
//        return result;
//      }

      if (StringUtils.isStringNullOrEmpty(tForm.getLongitude())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("Longitude " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }

      if (StringUtils.isStringNullOrEmpty(tForm.getLatitude())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("Latitude " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (!StringUtils.isStringNullOrEmpty(tForm.getLongitude())) {
        try {
          Double dValue = new Double(tForm.getLongitude().trim());
          if (dValue < 102 || dValue >= 115) {
            result.setKey(RESULT.FAIL);
            result.setMessage(I18n.getLanguage("incident.msgErrorLongitudeReal"));
            return result;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("incident.msgErrorLongitudeReal"));
          return result;
        }
      }
      if (!StringUtils.isStringNullOrEmpty(tForm.getLatitude())) {
        try {
          Double dValue = new Double(tForm.getLatitude().trim());
          if (dValue < 7 || dValue >= 24) {
            result.setKey(RESULT.FAIL);
            result.setMessage(I18n.getLanguage("incident.msgErrorLatitudeReal"));
            return result;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("incident.msgErrorLatitudeReal"));
          return result;
        }
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv1Id())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("ReasonLv1Id " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv1Name())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("ReasonLv1Name " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv2Id())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("ReasonLv2Id " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv2Name())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("ReasonLv2Name " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv3Id())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("ReasonLv3Id " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getReasonLv3Name())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("ReasonLv3Name " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getRootCause())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("RootCause " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
//      if (StringUtils.isStringNullOrEmpty(tForm.getWorkArround())) {
//        result.setKey(RESULT.FAIL);
//        result.setMessage("WorkArround " + I18n.getLanguage("wo.close.trouble.isRequire"));
//        return result;
//      }
      if (StringUtils.isStringNullOrEmpty(tForm.getTotalPendingTimeGnoc())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("TotalPendingTimeGnoc " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getTotalProcessTimeGnoc())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("TotalProcessTimeGnoc " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getEvaluateGnoc())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("EvaluateGnoc " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
    }
    return result;
  }

  public ResultInSideDto validateTKTU(TroublesInSideDTO tForm, TroublesInSideDTO troubleDTO) {
    ResultInSideDto result = new ResultInSideDto();
    result.setKey(RESULT.SUCCESS);
    if (troubleDTO == null || troubleDTO.getTroubleId() == null) {
      result.setKey(RESULT.FAIL);
      result.setMessage(I18n.getLanguage("incident.invalid.ticket"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getReceiveUnitName())) {
      result.setKey(RESULT.FAIL);
      result.setMessage("ReceiveUnitName " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getReceiveUserName())) {
      result.setKey(RESULT.FAIL);
      result.setMessage("ReceiveUserName " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getStateName())) {
      result.setKey(RESULT.FAIL);
      result.setMessage("StateName " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (StringUtils.isStringNullOrEmpty(tForm.getWorkLog())) {
      result.setKey(RESULT.FAIL);
      result.setMessage("WorkLog " + I18n.getLanguage("wo.close.trouble.isRequire"));
      return result;
    }
    if (tForm.getStateName().equals(Constants.TT_STATE.DEFERRED)) {
      if (StringUtils.isStringNullOrEmpty(tForm.getConcave())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("Concave " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      Date now = new Date();
      if (StringUtils.isStringNullOrEmpty(tForm.getEstimateTime())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("EstimateTime " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      try {
        Date estimate = tForm.getEstimateTime();
        if (estimate.compareTo(now) < 0) {
          result.setKey(RESULT.FAIL);
          result.setMessage(
              I18n.getLanguage("incident.estimateTime") + " " + I18n.getLanguage("common.greater")
                  + " " + I18n.getLanguage("ptMngt.addpt.now"));
          return result;
        }
        if (Constants.TT_PRIORITY.TT_Minor.equalsIgnoreCase(troubleDTO.getPriorityCode())) {
          estimate = DateUtils.addDays(estimate, 6);
        } else if (Constants.TT_PRIORITY.TT_Major.equalsIgnoreCase(troubleDTO.getPriorityCode())) {
          estimate = DateUtils.addDays(estimate, 4);
        } else if (Constants.TT_PRIORITY.TT_Critical
            .equalsIgnoreCase(troubleDTO.getPriorityCode())) {
          estimate = DateUtils.addDays(estimate, 2);
        }
        tForm.setDeferredTime(estimate);
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        result.setKey(RESULT.FAIL);
        result.setMessage("EstimateTime " + I18n.getLanguage("wo.close.trouble.date.format"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getDeferredTime())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("DeferredTime " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      try {
        Date deferredTime = tForm.getDeferredTime();
        if (deferredTime.compareTo(now) < 0) {
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("incident.deferredTimeMustBeBiggerThanNow"));
          return result;
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        result.setKey(RESULT.FAIL);
        result.setMessage("DeferredTime " + I18n.getLanguage("wo.close.trouble.date.format"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getCellService())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("CellService " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getLongitude())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("Longitude " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getLatitude())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("Latitude " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
      if (!StringUtils.isStringNullOrEmpty(tForm.getLongitude())) {
        try {
          Double dValue = new Double(tForm.getLongitude().trim());
          if (dValue < 102 || dValue >= 115) {
            result.setKey(RESULT.FAIL);
            result.setMessage(I18n.getLanguage("incident.msgErrorLongitudeReal"));
            return result;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("incident.msgErrorLongitudeReal"));
          return result;
        }
      }
      if (!StringUtils.isStringNullOrEmpty(tForm.getLatitude())) {
        try {
          Double dValue = new Double(tForm.getLatitude().trim());
          if (dValue < 7 || dValue >= 24) {
            result.setKey(RESULT.FAIL);
            result.setMessage(I18n.getLanguage("incident.msgErrorLatitudeReal"));
            return result;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("incident.msgErrorLatitudeReal"));
          return result;
        }
      }
      if (StringUtils.isStringNullOrEmpty(tForm.getWorkArround())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("WorkArround " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
    } else if (tForm.getStateName().equals(Constants.TT_STATE.CLOSED)) {
      if (StringUtils.isStringNullOrEmpty(tForm.getWorkArround())) {
        result.setKey(RESULT.FAIL);
        result.setMessage("WorkArround " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return result;
      }
    }
    return result;
  }

  public ResultInSideDto doReassignTicketForCC(TroublesInSideDTO tForm) throws Exception {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    TroublesInSideDTO troublesDTO = troublesRepository
        .getTroubleDTO(null, tForm.getTroubleCode(), null, null,
            StringUtils.isStringNullOrEmpty(tForm.getComplaintId()) ? null
                : String.valueOf(tForm.getComplaintId()),
            tForm.getCreatedTimeFrom(), tForm.getCreatedTimeTo());
    if (troublesDTO == null || StringUtils.isStringNullOrEmpty(troublesDTO.getTroubleId())) {
      resultInSideDTO.setKey(RESULT.FAIL);
      resultInSideDTO.setMessage(I18n.getLanguage("incident.invalid.ticket"));
      return resultInSideDTO;
    }
    String state = String.valueOf(troublesDTO.getState());
    if (10 == (troublesDTO.getState()) || 11 == (troublesDTO.getState()) || 7
        == (troublesDTO.getState())) {
    } else {
      resultInSideDTO.setKey(RESULT.FAIL);
      resultInSideDTO.setMessage(
          I18n.getLanguage("incident.not.close") + ", " + I18n.getLanguage("incident.not.delay"));
      return resultInSideDTO;
    }
    troublesDTO.setProcessingUserName(tForm.getProcessingUserName());
    troublesDTO.setProcessingUnitName(tForm.getProcessingUnitName());
    // 20200306_HaNV15_Edit_Start
    if ("SPM_VTNET".equals(troublesDTO.getInsertSource())) {
      if (!StringUtils.isStringNullOrEmpty(tForm.getTimeProcess())) {
        troublesDTO.setTimeProcess(tForm.getTimeProcess());
      }
    } else {
      troublesDTO.setTimeProcess(tForm.getTimeProcess());
      troublesDTO.setAssignTime(DateTimeUtils.convertStringToDate(DateTimeUtils.getSysDateTime()));
      troublesDTO
          .setAssignTimeTemp(DateTimeUtils.convertStringToDate(DateTimeUtils.getSysDateTime()));
    }
    // 20200306_HaNV15_Edit_Start
    troublesDTO.setClearTime(null);
    troublesDTO.setClearUserId(null);
    troublesDTO.setClearUserName(null);
    troublesDTO.setEndTroubleTime(null);
    troublesDTO.setState(5L);
    troublesDTO.setStateName(Constants.TT_STATE.QUEUE);
    troublesDTO.setLastUpdateTime(new Date());
    if (!StringUtils.isStringNullOrEmpty(tForm.getDeferredTime())) {
      troublesDTO.setState(7L);
      troublesDTO.setStateName(Constants.TT_STATE.DEFERRED);
      troublesDTO.setDeferredTime(tForm.getDeferredTime());
      troublesDTO.setDeferredReason(tForm.getRejectReason());
    } else {
      troublesDTO.setDeferredTime(null);
    }
    troublesDTO.setNumReassign(troublesDTO.getNumReassign() == null ? 1L
        : (troublesDTO.getNumReassign() + 1));
    troublesDTO.setRejectReason(tForm.getRejectReason());
    if ("10".equals(state) || "11".equals(state)) {
      troublesDTO.setTimeUsed(null);
    }
    if (!StringUtils.isStringNullOrEmpty(tForm.getReceiveUnitId())) {
      troublesDTO.setReceiveUnitId(tForm.getReceiveUnitId());
      troublesDTO.setReceiveUnitName(tForm.getReceiveUnitName());
    }

    Double timeProcess;
    if ("SPM_VTNET".equals(troublesDTO.getInsertSource())) {
      Double remainTime = 0d;
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getRemainTime())) {
        remainTime = Double.parseDouble(troublesDTO.getRemainTime());
      }
      if (remainTime <= 0) {
        timeProcess = 0.01;
      } else {
        timeProcess = remainTime * 2;
      }

    } else {
      timeProcess = Double.valueOf(tForm.getTimeProcess());
      troublesDTO.setTimeProcess(tForm.getTimeProcess());
      //cap nhat time used
      if ("10".equals(state) || "11".equals(state)) {
        troublesDTO.setTimeUsed(null);
      }
    }
    if (!StringUtils.isStringNullOrEmpty(troublesDTO.getWoCode()) && ("10".equals(state) || "11"
        .equals(state))) {
      Date startTime = new Date();
      Date endTime = new Date(
          startTime.getTime() + ((Double) (timeProcess * 60 * 60 * 1000))
              .longValue());

      WoUpdateStatusForm statusForm = new WoUpdateStatusForm();
      statusForm.setWoCode(troublesDTO.getWoCode());
      statusForm.setUserChange("system");
      statusForm.setSystemChange("BCCS_CC");
      statusForm.setReasonChange(tForm.getRejectReason());
      statusForm.setStartTimeNew(DateUtil.date2ddMMyyyyHHMMss(startTime));
      statusForm.setEndTimeNew(DateUtil.date2ddMMyyyyHHMMss(endTime));
      statusForm.setNewStatus(3L);
      ResultDTO resultWoDTO = woServiceProxy.changeStatusWoProxy(statusForm);
      if (resultInSideDTO != null && RESULT.SUCCESS.equals(resultWoDTO.getMessage())) {
        resultInSideDTO = reassignTicket(troublesDTO, tForm);
      } else {
        String msgReturn = resultWoDTO.getMessage();
        if (!RESULT.SUCCESS.equalsIgnoreCase(resultWoDTO.getKey()) && StringUtils
            .isStringNullOrEmpty(resultWoDTO.getMessage())) {
          msgReturn = I18n.getLanguage("wo.error.process");
        }
        resultInSideDTO = new ResultInSideDto(null, resultWoDTO.getKey(), msgReturn);
      }
    } else if (!StringUtils.isStringNullOrEmpty(troublesDTO.getWoCode()) && "7".equals(state)) {
      ResultDTO resultWoDTO = woServiceProxy
          .updatePendingWo(new RequestApiWODTO(troublesDTO.getWoCode(), null,
              "system", troublesDTO.getRejectReason(), "BCCS", false));
      String msgReturn = resultWoDTO.getMessage();
      if (!RESULT.SUCCESS.equalsIgnoreCase(resultWoDTO.getKey()) && StringUtils
          .isStringNullOrEmpty(resultWoDTO.getMessage())) {
        msgReturn = I18n.getLanguage("wo.error.process");
      }
      resultInSideDTO = new ResultInSideDto(null, resultWoDTO.getKey(), msgReturn);
    } else {
      resultInSideDTO = reassignTicket(troublesDTO, tForm);
    }
    if (resultInSideDTO != null && resultInSideDTO.getKey().equals(RESULT.SUCCESS)) {
      String smsContent = "message.reassign.TT";
      sendSms(troublesDTO, smsContent);
    }
    return resultInSideDTO;
  }

  public ResultInSideDto reassignTicket(TroublesInSideDTO troublesDTO, TroublesInSideDTO tForm) {
    ResultInSideDto result = new ResultInSideDto();
    try {
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getTroubleId())) {
        troublesRepository.insertTroubles(troublesDTO.toEntity());
      } else {
        troublesRepository.updateTroubles(troublesDTO.toEntity());
      }
      onInsertTroubleFileWS(troublesDTO);
      TroubleActionLogsDTO troubleActionLogsDTO = new TroubleActionLogsDTO(null,
          I18n.getLanguage("incident.reassign")
              + " \r\n " + troublesDTO.getRejectReason(),
          new Date(),
          troublesDTO.getCreateUnitId(),
          StringUtils.isStringNullOrEmpty(troublesDTO.getCreateUserId()) ? null
              : troublesDTO.getCreateUserId(),
          I18n.getLanguage("common.btn.update"),
          troublesDTO.getTroubleId(),
          troublesDTO.getProcessingUnitName(), troublesDTO.getProcessingUserName(),
          troublesDTO.getState(), troublesDTO.getTroubleCode(),
          troublesDTO.getStateName(), null, null);
      troubleActionLogsDTO.setInsertSource(tForm.getInsertSource());
      if ("SPM_VTNET".equalsIgnoreCase(tForm.getInsertSource())) {
        ActionInfoDTO info = new ActionInfoDTO();
        info.setTroubleCode(troublesDTO.getTroubleCode());
        info.setUpdateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        info.setState(String.valueOf(troublesDTO.getState()));
        info.setWorkLogs(I18n.getLanguage("reasign.from.vtnet") + " " + tForm.getRejectReason());
        troubleActionLogsDTO.setActionInfo(info.toString());
      }
      result = troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogsDTO.toEntity());
      result.setMessage(null);
    } catch (Exception e) {
      result.setId(null);
      result.setKey(RESULT.FAIL);
      result.setMessage(e.toString());
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public ResultDTO doOnInsertTroubleForCC(TroublesInSideDTO troublesDTO) throws Exception {
    ResultDTO result = new ResultDTO();
    ResultDTO resultWO = new ResultDTO();
    try {
      if (troublesDTO.getAutoCreateWO() != null && troublesDTO.getAutoCreateWO().equals(1L)) {
        if (Constants.TT_ARRAY.SPM_PAKH_CDBR.equals(troublesDTO.getTypeName())) {
          resultWO = woCreateBusiness.createWOMobile(troublesDTO, null, troublesRepository);
        } else {
          resultWO = woCreateBusiness.createWOForCC(troublesDTO, troublesRepository);
        }
        if (RESULT.SUCCESS.equals(resultWO.getMessage())) {
          troublesDTO.setWoCode(String.valueOf(resultWO.getId()));
        } else {
          result.setKey(RESULT.FAIL);
          result.setMessage(resultWO.getMessage());
          throw new Exception("Create Wo Fail: " + resultWO.getMessage());
        }
      }
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getDeferredTime())) {
        troublesDTO.setState(7L);
      } else {
        troublesDTO.setState(5L);
        troublesDTO.setQueueTime(new Date());
      }
      troublesDTO.setEndTroubleTime(null);
      result = insertTroubleForCC(troublesDTO);
      if (resultWO.getId() != null) {
        result.setId(result.getId() + "," + resultWO.getId());
        //result.setId(result.getId());
      }
      if (RESULT.SUCCESS.equals(result.getKey())) {
        if (resultWO.getKey() != null) {
          result.setMessage(resultWO.getKey());
        }
      } else {
        throw new Exception("Create TT Fail: " + result.getMessage());
      }
      if (result != null && result.getKey().equals(RESULT.SUCCESS)) {
        if (troublesDTO.getArFileData() != null && troublesDTO.getArFileData().length > 0
            && troublesDTO.getArFileName() != null && troublesDTO.getArFileName().length > 0) {
          ResultInSideDto resultInsertFile = onInsertTroubleFileWS(troublesDTO);
          if (resultInsertFile != null && !Constants.RESULT.SUCCESS
              .equals(resultInsertFile.getKey())) {
            result.setId(null);
            result.setKey(RESULT.FAIL);
            result.setMessage(resultInsertFile.getMessage());
            throw new Exception(resultInsertFile.getMessage());
          }
        }
        sendSms(troublesDTO, null);
      }
    } catch (Exception e) {
      result = new ResultDTO();
      result.setId(null);
      result.setKey(RESULT.FAIL);
      result.setMessage(e.getMessage());
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getWoCode())) {
        woServiceProxy
            .deleteWOForRollbackProxy(troublesDTO.getWoCode(), "rollback CC", "CC");
      }
      log.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    return result;
  }

  public String sendSms(TroublesInSideDTO troubleDTO, String content) throws Exception {
    List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();
    List<UsersInsideDto> lstUser;
    List<UnitDTO> lstUnit;
    String smsContent;
    if (!StringUtils.isStringNullOrEmpty(content)) {
      smsContent = content;
    } else {
      smsContent = "message.created.TT";
    }
    String smsUnit = String.valueOf(troubleDTO.getReceiveUnitId());
    List<ConditionBean> lstCondition = new ArrayList<>();
    lstCondition.add(
        new ConditionBean("unitId", smsUnit, Constants.NAME_EQUAL,
            Constants.NUMBER));
    lstCondition.add(new ConditionBean("isEnable", "1", Constants.NAME_EQUAL, Constants.NUMBER));
    lstUser = userBusiness
        .getListUsersByCondition(lstCondition, 0, 100, "asc", "username");

    UnitDTO unit = new UnitDTO();
    unit.setUnitId(Long.valueOf(smsUnit));
    unit.setStatus(1L);
    String alias = getAlias(troubleDTO);
    lstUnit = unitRepository.getListUnitDTO(unit, 0, 1, "asc", "unitName");

    if (lstUser != null && !lstUser.isEmpty()) {
      for (UsersInsideDto usersInsideDto : lstUser) {
        if (usersInsideDto != null && (usersInsideDto.getIsNotReceiveMessage() == null || !"1"
            .equals(String.valueOf(usersInsideDto.getIsNotReceiveMessage())))) {
          UserTokenGNOCSimple userTokenGNOCSimple = new UserTokenGNOCSimple();
          userTokenGNOCSimple.setUserId(usersInsideDto.getUserId());
          userTokenGNOCSimple.setUserName(usersInsideDto.getUsername().toLowerCase());
          Double offset;
          try {
            offset = TimezoneContextHolder.getOffsetDouble();
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            offset = 0D;
          }
          Date updateTime = new Date(new Date().getTime() + (long) (offset * 60 * 60 * 1000));
          String timeUpdate = DateUtil.date2ddMMyyyyHHMMss(updateTime);

          MessagesDTO messagesDTO = new MessagesDTO();
          String sms = DataUtil
              .getLang("2".equals(usersInsideDto.getUserLanguage()) ? new Locale("en", "US") : null,
                  smsContent);
          sms = sms.replaceAll("#troubleCode#", troubleDTO.getTroubleCode())
              .replaceAll("#troubleName#", troubleDTO.getTroubleName())
              .replaceAll("#updateTime#", timeUpdate)
              .replaceAll("#createUnit#", troubleDTO.getCreateUnitName())
              .replaceAll("#receiveUnit#", troubleDTO.getReceiveUnitName())
              .replaceAll("#currentUser#", troubleDTO.getCreateUnitName())
              .replaceAll("#createUser#",
                  troubleDTO.getCreateUserName() == null ? troubleDTO.getCreateUnitName()
                      : troubleDTO.getCreateUserName())
              .replaceAll("#priority#", troubleDTO.getPriorityName())
              .replaceAll("#stateName#", troubleDTO.getStateName());
          //Sua noi dung message khi tao TT (onInsertTroubleMobile,onInsertTroubleForCC)
          if (!StringUtils.isStringNullOrEmpty(troubleDTO.getWoContent())) {
            messagesDTO.setContent(troubleDTO.getWoContent());
          } else {
            messagesDTO.setContent(sms);
          }
          messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
          messagesDTO.setReceiverId(String.valueOf(usersInsideDto.getUserId()));
          messagesDTO.setReceiverUsername(usersInsideDto.getUsername());
          messagesDTO.setReceiverPhone(usersInsideDto.getMobile());
          if (lstUnit != null && !lstUnit.isEmpty()) {
            messagesDTO.setSmsGatewayId(String.valueOf(lstUnit.get(0).getSmsGatewayId()));
          }
          messagesDTO.setStatus("0");
          messagesDTO.setAlias(alias);

          lsMessagesDTOs.add(messagesDTO);
        }
      }
    }
    if (lsMessagesDTOs != null && !lsMessagesDTOs.isEmpty()) {
      for (int i = lsMessagesDTOs.size() - 1; i >= 0; i--) {
        if ("".equals(lsMessagesDTOs.get(i).getSmsGatewayId())
            || lsMessagesDTOs.get(i).getSmsGatewayId() == null) {
          lsMessagesDTOs.remove(i);
        }
      }
      messagesRepository.insertOrUpdateListMessagesCommon(lsMessagesDTOs);
    }
    return RESULT.SUCCESS;
  }

  private String getAlias(TroublesInSideDTO troublesDTO) {
    String alias = "";
    if (troublesDTO != null && troublesDTO.getTypeId() != null
        && troublesDTO.getAlarmGroupId() != null) {
      CatItemDTO catItem = catItemRepository
          .getCatItemById(troublesDTO.getTypeId());
      if (catItem != null) {
        String temp = "TT_" + catItem.getItemCode();
        catItem = catItemRepository.getCatItemById(Long.parseLong(troublesDTO.getAlarmGroupId()));
        if (catItem != null) {
          temp = temp + "_" + catItem.getItemCode();
          CatItemDTO catItemDTO = new CatItemDTO();
          catItemDTO.setItemCode(temp);
          catItemDTO.setStatus(1L);
          List<CatItemDTO> lstCatItem = catItemRepository.getListCatItemDTO(catItemDTO);
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

  public ResultInSideDto onInsertTroubleFileWS(TroublesInSideDTO troublesDTO) throws Exception {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    try {
      Date now = new Date();
      if (troublesDTO.getArFileData() != null && troublesDTO.getArFileData().length > 0
          && troublesDTO.getArFileName() != null && troublesDTO.getArFileName().length > 0) {
        Long createUserId;
        if (null != troublesDTO.getCreateUserId() && !"".equals(troublesDTO.getCreateUserId())) {
          createUserId = troublesDTO.getCreateUserId();
        } else {
          Map<String, String> mapProperty = troublesServiceForCCRepository.getConfigProperty();
          String userIdDefault = mapProperty.get("WO.TT.CREATE_PERSON.ID");
          createUserId = Long.parseLong(userIdDefault);
        }
        UsersEntity usersEntity = userBusiness.getUserByUserId(createUserId);
        UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
        List<GnocFileDto> gnocFileDtos = new ArrayList<>();
        for (int i = 0; i < troublesDTO.getArFileData().length; i++) {
          byte[] fileData = troublesDTO.getArFileData()[i];
          String fileName = troublesDTO.getArFileName()[i];
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
          troubleFile.setTroubleId(Long.valueOf(troublesDTO.getTroubleId()));
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
            .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.TROUBLES, troublesDTO.getTroubleId(),
                gnocFileDtos);
        resultInSideDTO.setKey(RESULT.SUCCESS);
        resultInSideDTO.setMessage(RESULT.SUCCESS);
      }
    } catch (Exception e) {
      resultInSideDTO.setId(null);
      resultInSideDTO.setKey(RESULT.FAIL);
      resultInSideDTO.setMessage(e.getMessage());
      log.error(e.getMessage(), e);
      throw e;
    }
    return resultInSideDTO;
  }

  public ResultDTO insertTroubleForCC(TroublesInSideDTO tForm) {
    ResultDTO result = new ResultDTO();
    try {
      TroublesEntity trouble = tForm.toEntity();
      updateTroubleInfo(tForm, trouble, null);
      if (trouble.getInsertSource() != null && Constants.INSERT_SOURCE.BCCS
          .equals(trouble.getInsertSource()) && trouble.getCreateUserId() == null) {
        Map<String, String> mapProperty = troublesServiceForCCRepository.getConfigProperty();
        String UserIdDefault = mapProperty.get("WO.TT.CREATE_PERSON.ID");
        trouble.setCreateUserId(Long.valueOf(UserIdDefault));
        UsersInsideDto user = userRepository.getUserByUserId(Long.valueOf(UserIdDefault)).toDTO();
        trouble.setCreateUserName(user.getUsername());
      }
      ResultInSideDto resultInSideDto = troublesRepository.insertTroubles(trouble);
      long id = resultInSideDto.getId();
      tForm.setCreateUnitName(trouble.getCreateUnitName());
      tForm.setCreateUserName(trouble.getCreateUserName());
      onInsertWorkLog(tForm);
      onInsertTroubleNode(tForm);
      if (Constants.TT_ARRAY.SPM_PAKH_CDBR.equals(tForm.getTypeName())) {
        onInsertTroubleWire(tForm);
      }
      if (tForm.getListAccount() != null && tForm.getListAccount().size() > 0) {
        for (String s : tForm.getListAccount()) {
          ItAccountEntity i = new ItAccountEntity(null, id, s);
          itAccountRepository.insertItAccount(i);
        }
      }
      if (!StringUtils.isStringNullOrEmpty(tForm.getSpmCode())) {
        ItSpmInfoEntity itSpmInfo = new ItSpmInfoEntity(null, id, Long.parseLong(tForm.getSpmId()),
            tForm.getSpmCode(), trouble.getTroubleCode());
        itSpmInfoRepository.insertItSpmInfo(itSpmInfo);
      }

      CatItemDTO catItemDTO = catItemRepository.getCatItemById(tForm.getState());
      if (catItemDTO != null) {
        tForm.setStateName(catItemDTO.getItemCode());
      }

      TroubleActionLogsEntity troubleActionLogs = new TroubleActionLogsEntity(null,
          trouble.toString(),
          new Date(),
          Long.valueOf(tForm.getCreateUnitId()),
          StringUtils.isStringNullOrEmpty(tForm.getCreateUserId()) ? null
              : Long.valueOf(tForm.getCreateUserId()),
          I18n.getLanguage("TransitionStateConfigDialog.insert"),
          Long.valueOf(tForm.getTroubleId()),
          tForm.getProcessingUnitName(), tForm.getProcessingUserName(),
          Long.valueOf(tForm.getState()), trouble.getTroubleCode(), tForm.getStateName(), null,
          null);
      troubleActionLogs.setContent(
          I18n.getLanguage("TransitionStateConfigDialog.insert") + " "
              + trouble.getInsertSource() + " :" + RESULT.SUCCESS + "" + tForm
              .getDescription());
      troubleActionLogsRepository.insertTroubleActionLogs(troubleActionLogs);

      result.setId(trouble.getTroubleCode());
      result.setMessage("Unit:" + trouble.getReceiveUnitName());
      result.setKey(RESULT.SUCCESS);
      result.setFinishTime(
          DateTimeUtils.convertDateToString(trouble.getCreatedTime(), "dd/MM/yyyy HH:mm:ss"));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setId(null);
      result.setKey(RESULT.FAIL);
      result.setMessage(e.getMessage());
      throw e;
    }
    return result;
  }

  public String onInsertTroubleWire(TroublesInSideDTO troublesDTO) {
    try {
      TroubleWireDTO troubleWire = new TroubleWireDTO();
      troubleWire.setServiceType(troublesDTO.getServiceType().toString());
      troubleWire.setTelServiceId(troublesDTO.getTelServiceId());
      troubleWire.setInfraType(troublesDTO.getInfraType());
      troubleWire.setIsCcResult(troublesDTO.getIsCcResult());
      troubleWire.setProductCode(troublesDTO.getProductCode());
      troubleWire.setAccountIsdn(troublesDTO.getListAccount().get(0));
      troubleWire.setCustomerPhone(troublesDTO.getCustomerPhone());
      troubleWire.setTroubleId(troublesDTO.getTroubleId());
      troubleWire.setContent(troublesDTO.getWoContent());
      if (troublesDTO.getInfraType() != null) {
        switch (troublesDTO.getInfraType().toUpperCase()) {
          case "CCN":
            troubleWire.setInfraType("1");
            break;
          case "CATV":
            troubleWire.setInfraType("2");
            break;
          case "FCN":
            troubleWire.setInfraType("3");
            break;
          case "GPON":
            troubleWire.setInfraType("4");
            break;
          case "N/A":
            troubleWire.setInfraType("0");
            break;
          default:
            break;
        }
      }
      troubleWireRepository.insertTroubleWire(troubleWire);
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  public String onInsertTroubleNode(TroublesInSideDTO trouble) {
    try {
      List<InfraDeviceDTO> lstDevice = trouble.getLstNode();
      if (lstDevice != null && !lstDevice.isEmpty()) {
        for (InfraDeviceDTO device : lstDevice) {
          TroubleNodeDTO troubleNode = new TroubleNodeDTO();
          if (device.getDeviceId() != null) {
            troubleNode.setDeviceId(Long.parseLong(device.getDeviceId()));
          }
          troubleNode.setTroubleId(trouble.getTroubleId());
          troubleNode.setDeviceCode(device.getDeviceCode());
          troubleNode.setDeviceName(device.getDeviceName());
          troubleNode.setIp(device.getIp());
          troubleNodeRepository.insertTroubleNode(troubleNode.toEntity());
        }
      }
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  public String onInsertWorkLog(TroublesInSideDTO tForm) {
    try {
      if (!StringUtils.isStringNullOrEmpty(tForm.getWorkLog())) {
        TroubleWorklogInsiteDTO troubleLog = new TroubleWorklogInsiteDTO();
        troubleLog.setTroubleId(tForm.getTroubleId());
        troubleLog.setCreateUnitId(tForm.getCreateUnitId());
        troubleLog.setCreateUnitName(tForm.getCreateUnitName());
        troubleLog
            .setCreateUserId(tForm.getCreateUserId() == null ? null : tForm.getCreateUserId());
        troubleLog
            .setCreateUserName(tForm.getCreateUserName() == null ? "" : tForm.getCreateUserName());
        troubleLog.setWorklog(tForm.getWorkLog());
        troubleLog.setCreatedTime(tForm.getLastUpdateTime());
        troubleWorklogRepository.insertTroubleWorklog(troubleLog.toEntity());
      }
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return e.getMessage();
    }
  }

  private void updateTroubleInfo(TroublesInSideDTO tForm, TroublesEntity trouble,
      CfgTimeTroubleProcessDTO config) {
    String locationNameFull = troublesRepository.getLocationNameFull(
        String.valueOf(tForm.getLocationId()));
    trouble.setLocation(locationNameFull);
    if (tForm.getStateName().equals(Constants.TT_STATE.Waiting_Receive) || tForm.getStateName()
        .equals(Constants.TT_STATE.QUEUE)) {
      trouble.setAssignTime(trouble.getCreatedTime());
      if (tForm.getStateName().equals(Constants.TT_STATE.QUEUE)) {
        trouble.setQueueTime(trouble.getCreatedTime());
      }
    }
    if (config != null) {
      trouble.setTimeProcess(
          config.getProcessTtTime() == null ? null : Double.valueOf(config.getProcessTtTime()));
      trouble.setTimeCreateCfg(
          config.getCreateTtTime() == null ? null : Double.valueOf(config.getCreateTtTime()));
      trouble.setCloseTtTime(
          config.getCloseTtTime() == null ? null : Double.valueOf(config.getCloseTtTime()));
    }
  }

  public String validateDTO(TroublesDTO dialog) {
    String result = "";
    try {
      if (StringUtils.isStringNullOrEmpty(dialog.getTroubleName())) {
        return "incident.troubleNameNull";
      } else if (dialog.getTroubleName().trim().length() > 500) {
        return "incident.troubleNameMax500";
      }
      if (StringUtils.isStringNullOrEmpty(dialog.getPriorityId())) {
        return "incident.priorityNull";
      }
      if (dialog.getInsertSource() != null && dialog.getInsertSource().length() > 200) {
        return "incident.insertSourceMax200";
      }
      CatItemDTO prio = new CatItemDTO();
      prio.setItemCode(String.valueOf(dialog.getPriorityId()).trim().toLowerCase());
      prio.setStatus(1L);
      List<CatItemDTO> lstCat = catItemRepository.getListCatItemDTO(prio);
      if (lstCat == null || lstCat.isEmpty() || (lstCat != null && !lstCat.get(0).getCategoryCode()
          .equals(Constants.TROUBLE.PRIORITY))) {
        return I18n.getLanguage("incident.priorityNotExist");
      } else {
        dialog.setPriorityId(String.valueOf(lstCat.get(0).getItemId()));
        dialog.setPriorityName(lstCat.get(0).getItemName());
      }
      if (StringUtils.isStringNullOrEmpty(dialog.getBeginTroubleTime())) {
        return "incident.beginTroubleTimeNull";
      } else {
        try {
          Date now = new Date();
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
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          return "incident.beginTroubleTimeInvalidFormat.ddMMyyyyHHmmss";
        }
      }
      dialog.setAssignTime(dialog.getBeginTroubleTime());
      if (StringUtils.isNotNullOrEmpty(dialog.getDeferredTime())) {
        if (StringUtils.isStringNullOrEmpty(dialog.getDeferredReason())) {
          return "incident.deferredReasonNull";
        }
        Date now = new Date();
        try {
          Date deferredTime = DateTimeUtils.convertStringToDateTime(dialog.getDeferredTime());
          String msgValid = DataUtil.validateDateTimeDdMmYyyy_HhMmSs(dialog.getDeferredTime());
          if (msgValid != null && msgValid.length() > 0) {
            return "incident.deferredTimeInvalidFormat.ddMMyyyyHHmmss";
          }
          if (deferredTime.compareTo(now) < 0) {
            return I18n.getLanguage("incident.deferredTimeMustBeBiggerThanNow");
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          return "incident.deferredTimeInvalidFormat.ddMMyyyyHHmmss";
        }
      }
      CatItemDTO catItem = new CatItemDTO();
      catItem.setItemCode(Constants.TT_STATE.Waiting_Receive);
      List<CatItemDTO> lst = catItemRepository.getListCatItemDTO(catItem);
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
        List<CatItemDTO> lstService = catItemRepository.getListCatItemDTO(serviceDTO);
        Map<String, CatItemDTO> mapService = new HashMap<>();
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
      if (StringUtils.isStringNullOrEmpty(dialog.getTypeId())) {
        return "incident.typeNull";
      } else {
        CatItemDTO typeDTO = new CatItemDTO();
        typeDTO.setItemCode(dialog.getTypeId().toLowerCase());
        typeDTO.setStatus(1L);
        List<CatItemDTO> lstType = catItemRepository.getListCatItemDTO(typeDTO);
        if (lstType == null || lstType.isEmpty()) {
          return "incident.typeNotExist";
        } else {
          if (!lstType.get(0).getCategoryCode().equals(Constants.TROUBLE.PT_TYPE)) {
            return "incident.notIstype";
          }
          dialog.setTypeId(String.valueOf(lstType.get(0).getItemId()));
          dialog.setTypeName(lstType.get(0).getItemCode());
        }
      }
      if (StringUtils.isStringNullOrEmpty(dialog.getSubCategoryId())) {
        if (Constants.TT_ARRAY.SPM_PAKH_CDBR.equals(dialog.getTypeName())) {
          if (dialog.getAutoCreateWO() != null && (dialog.getAutoCreateWO().equals(1L))) {
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
        List<CatItemDTO> lstType = catItemRepository.getListCatItemDTO(typeDTO);
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
      if (StringUtils.isStringNullOrEmpty(dialog.getLocationId())) {
        return "incident.locationCodeNull";
      } else {
        try {
          CatLocationDTO location = catLocationRepository
              .getLocationByCode(dialog.getLocationId(), null,
                  dialog.getNationCode());
          if (location == null) {
            return "incident.locationCodeNotExist";
          }
          dialog.setLocationCode(dialog.getLocationId());
          dialog.setLocationId(location.getLocationId());
          dialog.setLocation(location.getLocationNameFull());
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          return "incident.locationInvalid";
        }
      }
      if (StringUtils.isStringNullOrEmpty(dialog.getComplaintGroupId())) {
        return "incident.ComplaintGroupIsNotNull";
      }
      if (!StringUtils.isStringNullOrEmpty(dialog.getComplaintTypeId())) {
        String kedbCode = troublesRepository.getKedbByComplaint(dialog.getComplaintTypeId());
        dialog.setRelatedKedb(kedbCode);
      }
      if (dialog.getComplaintId() != null && dialog.getComplaintId().length() > 10) {
        return "incident.complaintGroupIdMax10";
      }

      if (StringUtils.isStringNullOrEmpty(dialog.getTimeProcess())) {
        return "incident.timeProcessIsNotNull";
      }

      try {
        Double timeProcess = Double.valueOf(dialog.getTimeProcess());
        if (timeProcess <= 0) {
          return "TimeProcess " + I18n.getLanguage("incident.timeProcess.validate");
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        return "TimeProcess " + I18n.getLanguage("incident.timeProcess.validate");
      }

      if (StringUtils.isStringNullOrEmpty(dialog.getProcessingUserName())) {
        return "incident.ProcessingUserNameIsNotNull";
      }
      dialog.setCreateUserByCC(dialog.getProcessingUserName());
      if (StringUtils.isStringNullOrEmpty(dialog.getProcessingUnitName())) {
        return "incident.ProcessingUnitNameIsNotNull";
      }
      CatItemDTO alarmGroupDTO = new CatItemDTO();
      alarmGroupDTO.setItemCode(dialog.getAlarmGroupCode());
      //mang brcd
      if (Constants.TT_ARRAY.SPM_PAKH_CDBR.equals(dialog.getTypeName())) {
        if (dialog.getServiceType() == null) {
          return "incident.serviceTypeIsNotNull";
        }
      }
      if (dialog.getAutoCreateWO() != null && dialog.getAutoCreateWO().equals(1L)) {
        if (StringUtils.isStringNullOrEmpty(dialog.getEndTroubleTime())) {
          return "incident.endTroubleTimeNull";
        } else {
          try {
            DateTimeUtils.convertStringToDateTime(dialog.getEndTroubleTime());
            String msgValid = DataUtil.validateDateTimeDdMmYyyy_HhMmSs(dialog.getEndTroubleTime());
            if (msgValid != null && msgValid.length() > 0) {
              return "incident.endTimeInvalid";
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "incident.endTroubleTimeInvalidFormat.ddMMyyyyHHmmss";
          }
        }
        if (Constants.TT_ARRAY.SPM_PAKH_CDBR.equals(dialog.getTypeName())) {
          if (StringUtils.isStringNullOrEmpty(dialog.getTelServiceId())) {
            return "incident.TelServiceIdIsNotNull";
          }
          if (StringUtils.isStringNullOrEmpty(dialog.getIsCcResult())) {
            return "incident.IsCcResultIsNotNull";
          }
          if (StringUtils.isStringNullOrEmpty(dialog.getTechnology())) {
            return "incident.TechnologyIsNotNull";
          }
          if (dialog.getListAccount() == null || dialog.getListAccount().isEmpty()) {
            return "incident.listAccountIsNotNull";
          } else {
            dialog.setSpmName(dialog.getListAccount().get(0));
          }

          CfgInfoTtSpmDTO cfgInfoTtSpmDTO = new CfgInfoTtSpmDTO();
          cfgInfoTtSpmDTO.setTypeId(String.valueOf(dialog.getTypeId()));
          List<CfgInfoTtSpmDTO> infoTtSpmDTO = cfgInfoTtSpmRepository
              .getListCfgInfoTtSpmDTO(cfgInfoTtSpmDTO, 0, 1, "", "");
          if (infoTtSpmDTO != null && !infoTtSpmDTO.isEmpty()) {
            dialog.setAlarmGroupId(infoTtSpmDTO.get(0).getAlarmGroupId());
            dialog.setSubCategoryId(infoTtSpmDTO.get(0).getSubCategoryId());
            dialog.setTroubleName(
                infoTtSpmDTO.get(0).getTroubleName().trim() + " " + dialog.getTroubleName().trim());

            alarmGroupDTO.setItemId(Long.valueOf(infoTtSpmDTO.get(0).getAlarmGroupId()));
            alarmGroupDTO.setItemCode(null);
          } else {
            return I18n.getLanguage("cfg.info.tt");
          }
        }
      }
      List<CatItemDTO> lstAlarmGroup;
      //tiennv bo sung neu alarmGroupDTO ko get duoc cau hinh
      if (alarmGroupDTO != null && alarmGroupDTO.getItemId() == null
          && alarmGroupDTO.getItemCode() == null) {
        lstAlarmGroup = catItemRepository
            .getListItemByCategoryAndParent(PROBLEM.ALARM_GROUP, dialog.getTypeId());
      } else {
        lstAlarmGroup = catItemRepository.getListCatItemDTO(alarmGroupDTO);
      }
      if (lstAlarmGroup != null && !lstAlarmGroup.isEmpty()) {
        dialog.setAlarmGroupId(String.valueOf(lstAlarmGroup.get(0).getItemId()));
        dialog.setAlarmGroupCode(lstAlarmGroup.get(0).getItemValue());
        dialog.setStrAlarmGroupDescription(lstAlarmGroup.get(0).getDescription());
      } else {
        return "incident.alarmGroupCodeNotExist";
      }
      boolean check = true;
      if (StringUtils.isStringNullOrEmpty(dialog.getReceiveUnitId())) {
        if (!Constants.TT_ARRAY.SPM_PAKH_CDBR.equals(dialog.getTypeName()) && StringUtils
            .isStringNullOrEmpty(dialog.getTypeUnit())) {
          return "incident.TypeUnitNull";
        }
        CfgUnitTtSpmDTO ttSpmDTO = troublesRepository
            .getUnitByLocation(dialog.getLocationId(), dialog.getTypeId(), dialog.getTypeUnit());
        if (ttSpmDTO != null && ttSpmDTO.getUnitId() != null) {
          dialog.setCreateUnitName(ttSpmDTO.getUnitName());
          dialog.setReceiveUnitName(ttSpmDTO.getUnitName());
          dialog.setReceiveUnitId(ttSpmDTO.getUnitId());
          dialog.setCreateUnitId(ttSpmDTO.getUnitId());
          check = false;
        } else {
          return I18n.getLanguage("unit.location");
        }
      }
      if (!StringUtils.isStringNullOrEmpty(dialog.getReceiveUnitId())) {
        UnitDTO unitDTO = new UnitDTO();
        if (check) {
          unitDTO.setUnitCode(dialog.getReceiveUnitId());
        } else {
          unitDTO.setUnitId(Long.valueOf(dialog.getReceiveUnitId()));
        }
        unitDTO.setStatus(1L);
        List<UnitDTO> u = unitRepository.getUnitByUnitDTO(unitDTO);
        if (u == null || u.isEmpty()) {
          return "incident.receiveUnitNotExist";
        }
        dialog.setReceiveUnitId(String.valueOf(u.get(0).getUnitId()));
        dialog.setReceiveUnitName(u.get(0).getUnitName() + " (" + u.get(0).getUnitCode() + ")");
      } else {
        return "incident.receiveUnitCodeNull";
      }

      if (!StringUtils.isStringNullOrEmpty(dialog.getCreateUnitId())) {
        UnitDTO createUnit = new UnitDTO();
        if (check) {
          createUnit.setUnitCode(dialog.getCreateUnitId());
        } else {
          createUnit.setUnitId(Long.valueOf(dialog.getCreateUnitId()));
        }
        createUnit.setStatus(1L);
        List<UnitDTO> rUnit = unitRepository.getUnitByUnitDTO(createUnit);
        if (rUnit == null || rUnit.isEmpty()) {
          createUnit = new UnitDTO();
          createUnit.setUnitId(Long.valueOf(dialog.getReceiveUnitId()));
          rUnit = unitRepository.getUnitByUnitDTO(createUnit);
          if (rUnit == null || rUnit.isEmpty()) {
            return "incident.createUnitNotExist";
          }
        }
        dialog.setCreateUnitId(rUnit.get(0).getUnitId().toString());
        dialog.setCreateUnitName(
            rUnit.get(0).getUnitName() + " (" + rUnit.get(0).getUnitCode() + ")");
      } else {
        return "incident.createUnitCodeNull";
      }

      if (!StringUtils.isStringNullOrEmpty(dialog.getCreateUserName())) {
        try {
          UsersInsideDto u = commonRepository.getUserByUserName(dialog.getCreateUserName().trim());
          if (u == null) {
            return "incident.createUserNotExist";
          } else {
            if (!u.getUnitId().toString().equals(dialog.getCreateUnitId())) {
              return "incident.createUserNotInCreateUnit";
            }
            dialog.setCreateUserId(String.valueOf(u.getUserId()));
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          return "incident.createUserInvalid";
        }
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
      String currDate = DateTimeUtils.getSysDateTime();
      dialog.setCreatedTime(currDate);
      dialog.setLastUpdateTime(currDate);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return e.getMessage();
    }
    return result;
  }

  private String validateInputGetTroubleInfo(TroublesDTO troublesDTO) {
    String ret = RESULT.SUCCESS;
    if (troublesDTO == null) {
      ret = I18n.getLanguage("incident.not.input.filter");
      return ret;
    }
    if (StringUtils.isStringNullOrEmpty(troublesDTO.getTroubleCode())) {
      ret = I18n.getLanguage("incident.not.input.trouble.code");
      return ret;
    }
    return ret;
  }

  private String getLocale() {
    try {
      MessageContext mcc = wsContext.getMessageContext();
      Message message = ((WrappedMessageContext) mcc).getWrappedMessage();
      List<Header> headers = (List<Header>) message.get(Header.HEADER_LIST);
      for (Header h : headers) {
        Element e = (Element) h.getObject();
        if (e != null && "locale".equalsIgnoreCase(e.getNodeName())) {
          String locale = e.getTextContent();
          if (locale != null && "en".equalsIgnoreCase(locale) || "en_us".equalsIgnoreCase(locale)) {
            locale = "en_US";
          } else if (locale != null && "vi".equalsIgnoreCase(locale) || "vi_vn"
              .equalsIgnoreCase(locale)) {
            locale = "vi_VN";
          } else if (locale != null && "lo".equalsIgnoreCase(locale) || "lo_la"
              .equalsIgnoreCase(locale)) {
            locale = "lo_LA";
          }
          return locale;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
    return null;
  }

  @Override
  public List<CauseErrorExpireDTO> getCauseErrorExpireForCC3(String parentId,
      CfgServerNocDTO nocDTO) {
    return bccsUtils.getCauseErrorExpire(parentId, nocDTO);
  }


  private ResultDTO validateOnUpdateTTForCC(TroublesDTO troublesDTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {

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

      if (StringUtils.isStringNullOrEmpty(troublesDTO.getStateName())) {
        resultDTO.setKey(Constants.RESULT.FAIL);
        resultDTO.setMessage("StateName " + I18n.getLanguage("wo.close.trouble.isRequire"));
        return resultDTO;
      }

      if ("UPDATE_WORKLOG".equals(troublesDTO.getStateName()) || "UPDATE_OPEN_DEFERRED"
          .equals(troublesDTO.getStateName())
          || "UPDATE_DEFERRED".equals(troublesDTO.getStateName())) {
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getWorkLog())) {
          resultDTO.setKey(Constants.RESULT.FAIL);
          resultDTO.setMessage("WorkLog " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return resultDTO;
        }
      }
      if ("UPDATE_CLOSED".equals(troublesDTO.getStateName())) {
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getReasonLv3Id())) {
          resultDTO.setKey(Constants.RESULT.FAIL);
          resultDTO.setMessage("ReasonLv3Id " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return resultDTO;
        }
      }
      if ("UPDATE_DEFERRED".equals(troublesDTO.getStateName())) {
        if (StringUtils.isStringNullOrEmpty(troublesDTO.getDeferredTime())) {
          resultDTO.setKey(Constants.RESULT.FAIL);
          resultDTO.setMessage("DeferredTime " + I18n.getLanguage("wo.close.trouble.isRequire"));
          return resultDTO;
        }
        try {
          Date defferTime = DateTimeUtils.convertStringToDateTime(troublesDTO.getDeferredTime());
          if (defferTime.compareTo(new Date()) < 0) {
            resultDTO.setKey(Constants.RESULT.FAIL);
            resultDTO.setMessage(I18n.getLanguage("incident.deferredTimeMustBeBiggerThanNow"));
            return resultDTO;
          }

        } catch (Exception ex) {
          log.error(ex.getMessage());
          resultDTO.setKey(Constants.RESULT.FAIL);
          resultDTO.setMessage("DeferredTime " + I18n.getLanguage("wo.close.trouble.date.format"));
          return resultDTO;
        }
      }

    } catch (Exception ex) {
      log.error(ex.getMessage());
      resultDTO.setKey(Constants.RESULT.FAIL);
      resultDTO.setKey(ex.getMessage());
    }
    return resultDTO;

  }
}
