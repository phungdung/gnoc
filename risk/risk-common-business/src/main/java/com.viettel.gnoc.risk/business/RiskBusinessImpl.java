package com.viettel.gnoc.risk.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.RiskCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.SrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.*;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.RISK_ROLE;
import com.viettel.gnoc.commons.utils.Constants.USER_ROLE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusRoleDTO;
import com.viettel.gnoc.risk.dto.RiskDTO;
import com.viettel.gnoc.risk.dto.RiskDTOSearch;
import com.viettel.gnoc.risk.dto.RiskFileDTO;
import com.viettel.gnoc.risk.dto.RiskHistoryDTO;
import com.viettel.gnoc.risk.dto.RiskRelationDTO;
import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
import com.viettel.gnoc.risk.model.RiskEntity;
import com.viettel.gnoc.risk.model.RiskFileEntity;
import com.viettel.gnoc.risk.model.RiskSystemDetailEntity;
import com.viettel.gnoc.risk.model.RiskSystemEntity;
import com.viettel.gnoc.risk.repository.RiskChangeStatusRepository;
import com.viettel.gnoc.risk.repository.RiskFileRepository;
import com.viettel.gnoc.risk.repository.RiskHistoryRepository;
import com.viettel.gnoc.risk.repository.RiskRelationRepository;
import com.viettel.gnoc.risk.repository.RiskRepository;
import com.viettel.gnoc.risk.repository.RiskSystemDetailRepository;
import com.viettel.gnoc.risk.repository.RiskSystemRepository;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class RiskBusinessImpl implements RiskBusiness {

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

  @Value("${application.SmsGatewayId:5}")
  private String smsGatewayId;

  @Value("${application.SenderId:400}")
  private String senderId;

  @Autowired
  RiskRepository riskRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  RiskHistoryRepository riskHistoryRepository;

  @Autowired
  RiskFileRepository riskFileRepository;

  @Autowired
  RiskRelationRepository riskRelationRepository;

  @Autowired
  RiskChangeStatusRepository riskChangeStatusRepository;

  @Autowired
  MessagesRepository messagesRepository;

  @Autowired
  RiskSystemDetailRepository riskSystemDetailRepository;

  @Autowired
  RiskCategoryServiceProxy riskCategoryServiceProxy;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  SrServiceProxy srServiceProxy;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  RiskSystemRepository riskSystemRepository;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  //thanhlv12 add 22-09-2020
  @Autowired
  CommonRepository commonRepository;
  //end

  private final static String RISK_SEQ = "RISK_SEQ";
  Map<String, String> mapStatus = new HashMap<>();
  Map<Long, String> mapPriority = new HashMap<>();
  Map<Long, String> mapEffect = new HashMap<>();
  Map<Long, String> mapFrequency = new HashMap<>();
  Map<Long, String> mapRedundacy = new HashMap<>();

  @Override
  public Datatable getListDataSearchWeb(RiskDTO riskDTO) {
    log.debug("Request to getListDataSearchWeb: {}", riskDTO);
    UserToken userToken = ticketProvider.getUserToken();
    if (userToken == null) {
      log.error("getListDataSearchWeb: UserToken is null");
      riskDTO.setOffSetFromUser(0D);
    } else {
      Double offSetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
      riskDTO = converTimeFromClientToServerWeb(riskDTO, offSetFromUser);
      riskDTO.setOffSetFromUser(offSetFromUser);
      riskDTO.setUserId(userToken.getUserID());
    }

    Datatable datatable = riskRepository.getListDataSearchWeb(riskDTO);
    if (datatable != null) {
      List<RiskDTO> list = (List<RiskDTO>) datatable.getData();
      if (list != null && list.size() > 0) {
        setMapStatus();
        setMapPriority();
        for (RiskDTO dto : list) {
          setStatusName(dto);
          setPriorityName(dto);
          UnitDTO subject = unitRepository.findUnitById(dto.getSubjectId());
          dto.setSubjectName(subject.getUnitName());
        }
      }
    }
    return datatable;
  }

  @Override
  public ResultInSideDto insertRiskFromWeb(List<MultipartFile> fileAttacks, RiskDTO riskDTO)
      throws Exception {
    log.debug("Request to insertRiskFromWeb: {}", riskDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    Date date = new Date();
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Long riskId = Long.valueOf(riskRepository.getSeqTableWo(RISK_SEQ));
    riskDTO.setRiskId(riskId);
    String riskCode = "RISK_" + DateUtil.date2StringNoSlash(date) + "_" + riskId;
    riskDTO.setRiskCode(riskCode);
    riskDTO.setStatus(Constants.RISK_STATUS.REQUEST_TO_OPEN);
    riskDTO.setLastUpdateTime(date);
    riskDTO.setCreateTime(date);
    Date startT = riskDTO.getStartTime();
    Date endT = riskDTO.getEndTime();
    String start = DateTimeUtils.converClientDateToServerDate(dfm.format(startT), offsetFromUser);
    String end = DateTimeUtils.converClientDateToServerDate(dfm.format(endT), offsetFromUser);
    try {
      startT = dfm.parse(start);
      endT = dfm.parse(end);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (startT.getTime() > endT.getTime()) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("risk.dateCompare"));
      return resultInSideDto;
    } else if (startT.getTime() + 300000 < new Date().getTime()) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("risk.dateNotLargeForNow"));
      return resultInSideDto;
    }
    riskDTO.setStartTime(startT);
    riskDTO.setEndTime(endT);
    String statusName = getValueFromDesKey(Constants.AP_PARAM.RISK_STATUS_PROPERTY,
        riskDTO.getStatus() != null ? String.valueOf(riskDTO.getStatus()) : null);
    String logTime =
        "{User: " + userToken.getUserName() + " ; " + "Insert Time: " + DateTimeUtils.
            converClientDateToServerDate(dfm.format(riskDTO.getCreateTime()), offsetFromUser)
            + " ; "
            + "Status: " + "null" + "-->" + statusName + "}\r\n";
    riskDTO.setLogTime(logTime);
    resultInSideDto = riskRepository.insertRisk(riskDTO);
    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      throw new Exception(I18n.getLanguage("risk.errSaveRisk"));
    }

    //luu lich su
    RiskHistoryDTO riskHistoryDTO = new RiskHistoryDTO();
    riskHistoryDTO.setContent(I18n.getLanguage("risk.addNewRisk"));
    riskHistoryDTO.setNewStatus(riskDTO.getStatus());
    riskHistoryDTO.setRiskId(riskId);
    riskHistoryDTO.setOldStatus(null);
    riskHistoryDTO.setUnitId(userToken.getDeptId());
    riskHistoryDTO.setUpdateTime(riskDTO.getLastUpdateTime());
    riskHistoryDTO.setUserId(userToken.getUserID());
    riskHistoryDTO.setUserName(userToken.getUserName());
    resultInSideDto = riskHistoryRepository.insertRiskHistory(riskHistoryDTO);
    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      throw new Exception(I18n.getLanguage("risk.errSaveRiskHistory"));
    }
    // File attachment
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    for (MultipartFile multipartFile : fileAttacks) {
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
              multipartFile.getBytes(), riskDTO.getCreateTime());
      String fullPathOld = FileUtils
          .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
              uploadFolder, riskDTO.getCreateTime());
      String fileName = multipartFile.getOriginalFilename();
      RiskFileDTO riskFileDTO = new RiskFileDTO();
      riskFileDTO.setRiskId(riskId);
      riskFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
      riskFileDTO.setPath(FileUtils.getFilePath(fullPathOld));
      resultInSideDto = riskFileRepository.insertRiskFile(riskFileDTO);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        throw new Exception(I18n.getLanguage("risk.errSaveRiskFile"));
      }

      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(fileName);
      gnocFileDto.setCreateUnitId(userToken.getDeptId());
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(userToken.getUserID());
      gnocFileDto.setCreateUserName(userToken.getUserName());
      gnocFileDto.setCreateTime(riskDTO.getCreateTime());
      gnocFileDto.setMappingId(resultInSideDto.getId());
      gnocFileDtos.add(gnocFileDto);
    }
    gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.RISK, riskId, gnocFileDtos);
    //luu thong tin lien ket
    if (riskDTO.getLstRiskRelation() != null && riskDTO.getLstRiskRelation().size() > 0) {
      for (RiskRelationDTO riskRelationDTO : riskDTO.getLstRiskRelation()) {
        riskRelationDTO.setRiskId(riskId);
        resultInSideDto = riskRelationRepository.insertRiskRelation(riskRelationDTO);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          throw new Exception(I18n.getLanguage("risk.errSaveRiskRelation"));
        }
      }
    }

    // thuc hien nhan tin
    sendMesseageUpdateRisk(riskDTO, -1L);

    resultInSideDto.setId(riskId);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);

    return resultInSideDto;
  }

  public String getDesFromValueKey(String configKey, String desKey) {
    return riskRepository.getValueFromValueKey(configKey, desKey);
  }

  @Override
  public RiskDTO findRiskByIdFromWeb(Long riskId) {
    log.debug("Request to findRiskByIdFromWeb: {}", riskId);
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    RiskDTO riskDTO = riskRepository.findRiskByIdFromWeb(riskId, offsetFromUser);
    RiskRelationDTO riskRelationDTO = new RiskRelationDTO();
    riskRelationDTO.setOffset(offsetFromUser);
    riskRelationDTO.setRiskId(riskId);
    List<RiskRelationDTO> listRiskRelation = riskRelationRepository
        .getRiskRelationByRiskId(riskRelationDTO);
    if (riskDTO != null) {
      setMapStatus();
      setMapPriority();
      setMapFrequency();
      setMapEffect();

      setStatusName(riskDTO);
      setPriorityName(riskDTO);
      setEffectName(riskDTO);
      setFrequencyName(riskDTO);
      setRedundacyName(riskDTO);

      riskDTO.setLstRiskRelation(listRiskRelation);
    }
    return riskDTO;
  }

  public void setRedundacyName(RiskDTO riskDTO) {
    if (!mapRedundacy.isEmpty()) {
      for (Map.Entry<Long, String> item : mapRedundacy.entrySet()) {
        if (item.getKey().equals(riskDTO.getRedundancy())) {
          riskDTO.setRedundacyName(item.getValue());
          break;
        }
      }
    }
  }

  public void setEffectName(RiskDTO riskDTO) {
    if (!mapEffect.isEmpty()) {
      for (Map.Entry<Long, String> item : mapEffect.entrySet()) {
        if (item.getKey().equals(riskDTO.getEffect())) {
          riskDTO.setEffectName(item.getValue());
          break;
        }
      }
    }
  }

  @Override
  public List<GnocFileDto> getListFileFromRisk(Long riskId) {
    log.debug("Request to getListFileFromRisk: {}", riskId);
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.RISK);
    gnocFileDto.setBusinessId(riskId);
    List<GnocFileDto> gnocFileDtos = gnocFileRepository.getListGnocFileByDto(gnocFileDto);
    for (GnocFileDto fileDto : gnocFileDtos) {
      fileDto.setFileName(FileUtils.getFileName(fileDto.getPath()));
    }
    return gnocFileDtos;
  }

  @Override
  public Datatable getListRiskHistoryByRiskId(RiskHistoryDTO riskHistoryDTO) {
    log.debug("Request to getListRiskHistoryByRiskId: {}", riskHistoryDTO);
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    riskHistoryDTO.setOffset(offsetFromUser);
    setMapStatus();
    Datatable datatable = riskHistoryRepository.getListRiskHistoryByRiskId(riskHistoryDTO);
    if (datatable != null) {
      List<RiskHistoryDTO> lstData = (List<RiskHistoryDTO>) datatable.getData();
      if (lstData != null && lstData.size() > 0 && mapStatus != null) {
        for (RiskHistoryDTO item : lstData) {
          if (mapStatus.containsKey(String.valueOf(item.getOldStatus()))) {
            item.setOldStatusName(mapStatus.get(String.valueOf(item.getOldStatus())));
          }
          if (mapStatus.containsKey(String.valueOf(item.getNewStatus()))) {
            item.setNewStatusName(mapStatus.get(String.valueOf(item.getNewStatus())));
          }
        }
      }
      datatable.setData(lstData);
    }
    return datatable;
  }

  @Override
  public Datatable getListRiskRelationByRiskId(RiskRelationDTO riskRelationDTO) {
    log.debug("Request to getListRiskRelationByRiskId: {}", riskRelationDTO);
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    riskRelationDTO.setOffset(offsetFromUser);
    return riskRelationRepository.getListRiskRelationByRiskId(riskRelationDTO);
  }

  @Override
  public List<RiskSystemDTO> getListRiskSystemCombobox(RiskSystemDTO riskSystemDTO) {
    log.debug("Request to getListRiskSystemCombobox: {}", riskSystemDTO);
    return riskCategoryServiceProxy.getListRiskSystem(riskSystemDTO);
  }

  @Override
  public List<RiskTypeDTO> getListRiskTypeCombobox(RiskTypeDTO riskTypeDTO) {
    log.debug("Request to getListRiskTypeCombobox: {}", riskTypeDTO);
    riskTypeDTO.setProxyLocale(I18n.getLocale());
    return riskCategoryServiceProxy.getListRiskTypeDTO(riskTypeDTO);
  }

  @Override
  public ResultInSideDto getValueFromValueKey(String configKey, String valueKey) {
    log.debug("Request to getValueFromValueKey: {}", configKey, valueKey);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(riskRepository.getValueFromValueKey(configKey, valueKey));
    return resultInSideDto;
  }

  @Override
  public UnitDTO findUnitById(Long unitId) {
    log.debug("Request to findUnitById: {}", unitId);
    return unitRepository.findUnitById(unitId);
  }

  @Override
  public List<RiskTypeDetailDTO> getListRiskTypeDetail(RiskTypeDetailDTO riskTypeDetailDTO) {
    log.debug("Request to getListRiskTypeDetail: {}", riskTypeDetailDTO);
    return riskCategoryServiceProxy.getListRiskTypeDetail(riskTypeDetailDTO);
  }

  @Override
  public ResultInSideDto updateRiskFromWeb(List<MultipartFile> fileAttacks,
      List<MultipartFile> fileCfgAttacks, RiskDTO riskDTO) throws Exception {
    log.debug("Request to updateRiskFromWeb: {}", riskDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    RiskDTO oldRiskDTO = findRiskByIdFromWeb(riskDTO.getRiskId());
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    RiskEntity riskEntity = riskRepository.findRiskByRiskId(riskDTO.getRiskId());
    riskDTO.setUserIdLogin(userToken.getUserID());
    riskDTO.setUnitIdLogin(userToken.getDeptId());
    String checkPermission = checkPermissionUpdate(riskDTO, riskEntity);
    if (StringUtils.isNotNullOrEmpty(checkPermission)) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(checkPermission);
      return resultInSideDto;
    }
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    Long oldStatus = riskDTO.getOldStatus();
    Date date = new Date();
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    if (oldStatus != null) {
      if (oldStatus.compareTo(riskEntity.getStatus()) != 0) {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("risk.inProcessing"));
        return resultInSideDto;
      }
    }
    String oldStatusName = getValueFromDesKey(Constants.AP_PARAM.RISK_STATUS_PROPERTY,
        oldStatus != null ? String.valueOf(oldStatus) : null);
    String newStatusName = getValueFromDesKey(Constants.AP_PARAM.RISK_STATUS_PROPERTY,
        riskDTO.getStatus() != null ? String.valueOf(riskDTO.getStatus()) : null);
    String logTime =
        riskDTO.getLogTime() + "{User: " + userToken.getUserName() + " ; " + "Update Time: "
            + DateTimeUtils.convertDateToString(date, Constants.ddMMyyyyHHmmss) + " ; "
            + "Status: " + oldStatusName + "-->" + newStatusName + "}\r\n";
    riskDTO.setLogTime(logTime);
    if (String.valueOf(riskDTO.getStatus()).equals(
        getValueFromValueKey(Constants.AP_PARAM.RISK_STATUS_PROPERTY,
            Constants.RISK_STATUS.ACCEPT).getMessage())) {
      riskDTO.setAcceptedDate(date);
    } else if (String.valueOf(riskDTO.getStatus()).equals(
        getValueFromValueKey(Constants.AP_PARAM.RISK_STATUS_PROPERTY,
            Constants.RISK_STATUS.REJECT).getMessage())) {
      riskDTO.setRejectedDate(date);
    } else if (String.valueOf(riskDTO.getStatus()).equals(
        getValueFromValueKey(Constants.AP_PARAM.RISK_STATUS_PROPERTY,
            Constants.RISK_STATUS.CANCEL).getMessage())) {
      riskDTO.setCanceledDate(date);
    } else if (String.valueOf(riskDTO.getStatus()).equals(
        getValueFromValueKey(Constants.AP_PARAM.RISK_STATUS_PROPERTY,
            Constants.RISK_STATUS.CLOSE).getMessage())) {
      riskDTO.setClosedDate(date);
    } else if (String.valueOf(riskDTO.getStatus()).equals(
        getValueFromValueKey(Constants.AP_PARAM.RISK_STATUS_PROPERTY,
            Constants.RISK_STATUS.RECEIVE).getMessage())) {
      riskDTO.setReceivedDate(date);
    } else if (String.valueOf(riskDTO.getStatus()).equals(
        getValueFromValueKey(Constants.AP_PARAM.RISK_STATUS_PROPERTY,
            Constants.RISK_STATUS.OPEN).getMessage())) {
      riskDTO.setOpenedDate(date);
      riskDTO.setIsSendSmsOverdue(null);
    }
    Date startT = riskDTO.getStartTime();
    Date endT = riskDTO.getEndTime();
    Date creatT = riskDTO.getCreateTime();
    String start = DateTimeUtils.converClientDateToServerDate(dfm.format(startT), offsetFromUser);
    String end = DateTimeUtils.converClientDateToServerDate(dfm.format(endT), offsetFromUser);
    String creat = DateTimeUtils.converClientDateToServerDate(dfm.format(creatT), offsetFromUser);
    try {
      startT = dfm.parse(start);
      endT = dfm.parse(end);
      creatT = dfm.parse(creat);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    riskDTO.setStartTime(startT);
    riskDTO.setEndTime(endT);
    riskDTO.setCreateTime(creatT);
    riskDTO.setLastUpdateTime(date);
    resultInSideDto = riskRepository.updateRisk(riskDTO);

    //luu lich su
    RiskHistoryDTO riskHistoryDTO = new RiskHistoryDTO();
    riskHistoryDTO.setContent(riskDTO.getComment());
    riskHistoryDTO.setNewStatus(riskDTO.getStatus());
    riskHistoryDTO.setRiskId(riskDTO.getRiskId());
    riskHistoryDTO.setOldStatus(oldStatus);
    riskHistoryDTO.setUnitId(userToken.getDeptId());
    riskHistoryDTO.setUpdateTime(riskDTO.getLastUpdateTime());
    riskHistoryDTO.setUserId(userToken.getUserID());
    riskHistoryDTO.setUserName(userToken.getUserName());
    riskDTO.setOldStatus(null);
    oldRiskDTO.setLogTime(null);
    riskDTO.setRiskComment(null);
    oldRiskDTO.setRiskComment(null);
    riskDTO.setLogTime(null);
    riskDTO.setCreateTime(oldRiskDTO.getCreateTime());
    String response = compareObj(oldRiskDTO, riskDTO, offsetFromUser);
    riskHistoryDTO.setInforMation(response);
    resultInSideDto = riskHistoryRepository.insertRiskHistory(riskHistoryDTO);
    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      throw new Exception(I18n.getLanguage("risk.errSaveRiskHistory"));
    }

    // lay danh sach nghiep vu tiep theo
    RiskChangeStatusDTO cfg = getCfgBusiness(riskDTO, oldStatus);
    if (cfg != null && cfg.getNextAction() != null) {
      CatItemDTO nextAction = riskRepository.getDoNextAction(cfg.getNextAction());
      String[] ne = nextAction.getItemValue().split(",");
      for (int i = 0; i < ne.length; i++) {
        if (ne[i].contains(Constants.OD_NEXT_ACTION.CHANGE_STATUS_TO)) {
          String valueSt = ne[i].substring(17);
          updateNextStatus(riskHistoryDTO, riskDTO.getStatus(), Long.parseLong(valueSt));
          riskDTO.setStatus(Long.parseLong(valueSt));
        }
      }
    }

    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      throw new Exception(I18n.getLanguage("risk.errSaveRisk"));
    }

    // File attachment
    resultInSideDto = updateRiskFileAttach(riskDTO, fileAttacks, fileCfgAttacks, userToken,
        unitToken);
    if (RESULT.FILE_INVALID_FORMAT.equals(resultInSideDto.getKey())) {
      throw new Exception(
          I18n.getLanguage("risk.fileAttachInvalid") + ": " + resultInSideDto.getMessage());
    }

    //luu thong tin lien ket
    if (riskDTO.getLstRiskRelation() != null && riskDTO.getLstRiskRelation().size() > 0) {
      RiskRelationDTO relationSearch = new RiskRelationDTO();
      relationSearch.setOffset(offsetFromUser);
      relationSearch.setRiskId(riskDTO.getRiskId());
      List<RiskRelationDTO> lstRelation = riskRelationRepository
          .getRiskRelationByRiskId(relationSearch);
      Map<String, RiskRelationDTO> mapRelation = new HashMap<>();
      for (RiskRelationDTO relationDTO : lstRelation) {
        if (!mapRelation.containsKey(String.valueOf(relationDTO.getId()))) {
          mapRelation.put(String.valueOf(relationDTO.getId()), relationDTO);
        }
      }
      for (RiskRelationDTO riskRelationDTO : riskDTO.getLstRiskRelation()) {
        if (!mapRelation.containsKey(String.valueOf(riskRelationDTO.getId()))) {
          riskRelationDTO.setRiskId(riskDTO.getRiskId());
          resultInSideDto = riskRelationRepository.insertRiskRelation(riskRelationDTO);
        }
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          throw new Exception(I18n.getLanguage("risk.errSaveRiskRelation"));
        }
      }
    }

    // thuc hien nhan tin
    sendMesseageUpdateRisk(riskDTO, oldStatus);

    resultInSideDto.setId(riskDTO.getRiskId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);

    return resultInSideDto;
  }

  /*
    Cap nhat trang thai tiep theo
   */
  public void updateNextStatus(RiskHistoryDTO riskHistoryDTO, Long oldStatus, Long newStatus) {
    riskHistoryDTO.setOldStatus(oldStatus);
    riskHistoryDTO.setNewStatus(newStatus);
    riskHistoryDTO.setInforMation(null);
    riskHistoryDTO.setContent(null);
    riskHistoryRepository.insertRiskHistory(riskHistoryDTO);
  }

  public String checkPermissionUpdate(RiskDTO riskDTO, RiskEntity riskEntity) {
    // lay danh sach quyen
    RiskChangeStatusDTO dtoS = new RiskChangeStatusDTO();
    dtoS.setRiskTypeId(riskDTO.getRiskTypeId());
    dtoS.setOldStatus(riskEntity.getStatus());
    dtoS.setNewStatus(riskDTO.getStatus());
    dtoS.setRiskPriority(riskDTO.getPriorityId());
    dtoS.setIsSearch(false);

    List<RiskChangeStatusDTO> lstChangeStatus = riskCategoryServiceProxy
        .getListRiskChangeStatusDTO(dtoS);
    if (lstChangeStatus != null && lstChangeStatus.size() > 0) {
      RiskChangeStatusDTO o = lstChangeStatus.get(0);

      // lay danh sach ban ghi cau hinh bat buoc giao dien
      RiskChangeStatusRoleDTO cfgRole = new RiskChangeStatusRoleDTO();
      cfgRole.setRiskChangeStatusId(o.getId());
      List<RiskChangeStatusRoleDTO> lstRole = riskCategoryServiceProxy
          .getListRiskChangeStatusRoleDTO(cfgRole);
      if (lstRole != null && lstRole.size() > 0) {
        for (RiskChangeStatusRoleDTO i : lstRole) {
          if (Constants.RISK_ROLE.NVT.equals(i.getRoleId())) {  // quyen nhan vien tao
            if (riskDTO.getUserIdLogin() != null && riskDTO.getUserIdLogin()
                .equals(riskEntity.getCreateUserId())) {
              return "";
            }
          } else if (Constants.RISK_ROLE.NVXL.equals(i.getRoleId())) { // nhan vien xu ly
            if (riskDTO.getUserIdLogin() != null && riskDTO.getUserIdLogin()
                .equals(riskEntity.getReceiveUserId())) {
              return "";
            }
          } else if (Constants.RISK_ROLE.LDPQLRR
              .equals(i.getRoleId())) { // lanh dao phong quan ly rui ro
            List<RiskSystemDetailEntity> lstSystemDetail = riskSystemDetailRepository
                .getListEntityBySystemId(riskEntity.getSystemId());
            for (RiskSystemDetailEntity systemDetailEntity : lstSystemDetail) {
              if (systemDetailEntity.getManageUnitId() != null && checkRoleRisk(
                  systemDetailEntity.getManageUnitId(),
                  riskDTO.getUserIdLogin(), Constants.RISK_ROLE.LDPQLRR)) {
                return "";
              }
            }
          } else if (Constants.RISK_ROLE.LDPXLRR
              .equals(i.getRoleId())) { // lanh dao phong xu ly rui ro
            if (riskEntity.getReceiveUnitId() != null
                && checkRoleRisk(riskEntity.getReceiveUnitId(), riskDTO.getUserIdLogin(),
                Constants.RISK_ROLE.LDPXLRR)) {
              return "";
            }
          } else if (Constants.RISK_ROLE.NVQLRR
              .equals(i.getRoleId())) { // chuyen vien quan ly rui ro
            if (checkRoleRisk(null, riskDTO.getUserIdLogin(), Constants.RISK_ROLE.NVQLRR)) {
              return "";
            }
          } else if (Constants.RISK_ROLE.BGDTT
              .equals(i.getRoleId())) { // ban giam doc trung tam
            if (checkRoleRisk(null, riskDTO.getUserIdLogin(), Constants.RISK_ROLE.BGDTT)) {
              return "";
            }
          } else if (Constants.RISK_ROLE.CVQLRRTT
              .equals(i.getRoleId())) { // chuyen vien quan ly rui ro thi truong
            if (checkTT(riskDTO) && checkRoleRisk(null, riskDTO.getUserIdLogin(),
                Constants.RISK_ROLE.CVQLRRTT)) {
              return "";
            }
          } else if (RISK_ROLE.LD_CREATE_UNIT
              .equals(i.getRoleId())) { // lanh dao don vi tao
            if (riskEntity.getCreateUnitId() != null
                && checkRoleRisk(riskEntity.getCreateUnitId(), riskDTO.getUserIdLogin(),
                Constants.RISK_ROLE.LD_CREATE_UNIT)) {
              return "";
            }
          }
        }
      } else {
        return I18n.getLanguage("risk.not.config.role");
      }
    } else {
      return I18n.getLanguage("risk.not.config.change.status");
    }
    return I18n.getLanguage("risk.not.config.role");
  }

  public ResultInSideDto updateRiskFileAttach(RiskDTO riskDTO, List<MultipartFile> files,
      List<MultipartFile> fileCfgAttacks, UserToken userToken, UnitDTO unitToken) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);
    try {
      //Start save file old
//      List<RiskFileEntity> lstFileOld = riskFileRepository
//          .getListRiskFileByRiskId(riskDTO.getRiskId());
//      List<Long> listIdFileIdOld = lstFileOld.stream()
//          .map(RiskFileEntity::getRiskFileId).collect(Collectors.toList());
//      List<GnocFileDto> gnocFileDtosWeb = riskDTO.getGnocFileDtos();
//      List<Long> listIdFileIdNew = gnocFileDtosWeb.stream()
//          .map(GnocFileDto::getMappingId).collect(Collectors.toList());
//      listIdFileIdOld.removeAll(listIdFileIdNew);
      for (Long aLong : riskDTO.getIdDeleteList()) {
        riskFileRepository.deleteRiskFile(aLong);
      }
      //End save file old
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      for (MultipartFile multipartFile : files) {
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                multipartFile.getBytes(), riskDTO.getCreateTime());
        String fullPathOld = FileUtils
            .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                uploadFolder, riskDTO.getCreateTime());
        String fileName = multipartFile.getOriginalFilename();
        //Start save file old
        RiskFileDTO riskFileDTO = new RiskFileDTO();
        riskFileDTO.setRiskId(riskDTO.getRiskId());
        riskFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
        riskFileDTO.setPath(FileUtils.getFilePath(fullPathOld));
        ResultInSideDto resultFileDataOld = riskFileRepository.insertRiskFile(riskFileDTO);
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(fileName);
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(riskDTO.getCreateTime());
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFileDtos.add(gnocFileDto);
      }
      // File cfg
      if (riskDTO.getGnocFileTemplate() != null && !riskDTO.getGnocFileTemplate().isEmpty()) {
//        GnocFileDto gnocFileTemplateOld = new GnocFileDto();
//        gnocFileTemplateOld.setBusinessCode(GNOC_FILE_BUSSINESS.RISK_CONFIG);
//        gnocFileTemplateOld.setBusinessId(riskDTO.getRiskChangeStatusId());
//        List<GnocFileDto> gnocFileTemplateOlds = gnocFileRepository
//            .getListGnocFileByDto(gnocFileTemplateOld);
//        Map<Long, String> mapCfgFileTemplateOld = new HashMap<>();
//        for (GnocFileDto gnocFileDto : gnocFileTemplateOlds) {
//          mapCfgFileTemplateOld.put(gnocFileDto.getMappingId(), gnocFileDto.getPath());
//        }
        for (GnocFileDto gnocFileDto : riskDTO.getGnocFileTemplate()) {
          if (gnocFileDto.getIndexFile() != null) {
            gnocFileDto.setMultipartFile(fileCfgAttacks.get(gnocFileDto.getIndexFile().intValue()));
          }
        }
        for (GnocFileDto gnocFileTemplate : riskDTO.getGnocFileTemplate()) {
          if (gnocFileTemplate.getMultipartFile() != null) {
            String filePathOld = gnocFileTemplate.getPath();
            MultipartFile multipartFile = gnocFileTemplate.getMultipartFile();
            String filePathNew = FileUtils
                .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                    tempFolder);
            if (StringUtils.isNotNullOrEmpty(filePathOld)) {
              byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort,
                  PassTranformer.decrypt(ftpUser), PassTranformer.decrypt(ftpPass), filePathOld);
              String filePathNewTemp = FileUtils
                  .saveTempFile(FileUtils.getFileName(filePathOld), bytes,
                      tempFolder);
              if (!ExcelWriterUtils.compareHeader(filePathNewTemp, filePathNew)) {
                resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
                resultInSideDto.setMessage(FileUtils.getFileName(filePathNew));
                return resultInSideDto;
              }
              String fullPath = FileUtils
                  .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), ftpFolder,
                      multipartFile.getOriginalFilename(),
                      multipartFile.getBytes(), riskDTO.getCreateTime());
              String fileName = gnocFileTemplate.getMultipartFile().getOriginalFilename();
              GnocFileDto gnocFileDto = new GnocFileDto();
              gnocFileDto.setPath(fullPath);
              gnocFileDto.setFileName(fileName);
              gnocFileDto.setCreateUnitId(userToken.getDeptId());
              gnocFileDto.setCreateUnitName(unitToken.getUnitName());
              gnocFileDto.setCreateUserId(userToken.getUserID());
              gnocFileDto.setCreateUserName(userToken.getUserName());
              gnocFileDto.setCreateTime(riskDTO.getCreateTime());
              gnocFileDto.setMappingId(riskDTO.getRiskId());
              gnocFileDtos.add(gnocFileDto);
            }
          }
        }
      }

//      List<GnocFileDto> gnocFileDtosAll = new ArrayList<>();
//      gnocFileDtosAll.addAll(gnocFileDtos);
//      gnocFileDtosAll.addAll(riskDTO.getGnocFileDtos());
      gnocFileRepository.deleteListGnocFile(GNOC_FILE_BUSSINESS.RISK, riskDTO.getRiskId(),
          riskDTO.getIdDeleteList());
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.RISK, riskDTO.getRiskId(),
              gnocFileDtos);
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(I18n.getLanguage("risk.errSaveRiskFile"));
    }
  }

  @Override
  public List<RiskChangeStatusDTO> getListRiskChangeStatus(
      RiskChangeStatusDTO riskChangeStatusDTO) {
    log.debug("Request to getListRiskChangeStatus: {}", riskChangeStatusDTO);
    return riskCategoryServiceProxy.getListRiskChangeStatusDTO(riskChangeStatusDTO);
  }

  @Override
  public List<RiskCfgBusinessDTO> getListRiskCfgBusiness(RiskCfgBusinessDTO riskCfgBusinessDTO) {
    log.debug("Request to getListRiskChangeStatus: {}", riskCfgBusinessDTO);
    return riskCategoryServiceProxy.getListRiskCfgBusinessDTO(riskCfgBusinessDTO);
  }

  @Override
  public List<Long> getListStatusNext(RiskDTO riskDTO) {
    log.debug("Request to getListStatusNext: {}", riskDTO);
    List<Long> listStatusNext = new ArrayList<>();
    List<Long> listRole = new ArrayList<>();
    List<RiskChangeStatusDTO> lstChangeStatus = getListChangeStatus(riskDTO.getRiskTypeId(),
        riskDTO.getStatus(), null, riskDTO.getPriorityId());
    UserToken userToken = ticketProvider.getUserToken();
    riskDTO.setUserIdLogin(userToken.getUserID());
    riskDTO.setUnitIdLogin(userToken.getDeptId());
    if (riskDTO.getCreateUserId() != null
        && userToken.getUserID().compareTo(riskDTO.getCreateUserId()) == 0) {
      listRole.add(Constants.RISK_ROLE.NVT);
    }
    if (riskDTO.getReceiveUserId() != null
        && userToken.getUserID().compareTo(riskDTO.getReceiveUserId()) == 0) {
      listRole.add(Constants.RISK_ROLE.NVXL);
    }
    if (riskDTO.getCreateUnitId() != null && checkRoleRisk(riskDTO.getCreateUnitId(),
        userToken.getUserID(), Constants.RISK_ROLE.LDPQLRR)) {
      listRole.add(Constants.RISK_ROLE.LDPQLRR);
    }
    if (riskDTO.getReceiveUnitId() != null && checkRoleRisk(riskDTO.getReceiveUnitId(),
        userToken.getUserID(), Constants.RISK_ROLE.LDPXLRR)) {
      listRole.add(Constants.RISK_ROLE.LDPXLRR);
    }
    if (checkRoleRisk(null, userToken.getUserID(), Constants.RISK_ROLE.NVQLRR)) {
      listRole.add(Constants.RISK_ROLE.NVQLRR);
    }
    if (checkRoleRisk(null, userToken.getUserID(), Constants.RISK_ROLE.BGDTT)) {
      listRole.add(Constants.RISK_ROLE.BGDTT);
    }
    if (checkTT(riskDTO) && checkRoleRisk(null, userToken.getUserID(),
        Constants.RISK_ROLE.CVQLRRTT)) {
      listRole.add(Constants.RISK_ROLE.CVQLRRTT);
    }
    if (riskDTO.getCreateUnitId() != null && checkRoleRisk(riskDTO.getCreateUnitId(),
        userToken.getUserID(), RISK_ROLE.LD_CREATE_UNIT)) {
      listRole.add(RISK_ROLE.LD_CREATE_UNIT);
    }

    if (lstChangeStatus != null && lstChangeStatus.size() > 0 && listRole != null
        && listRole.size() > 0) {
      for (RiskChangeStatusDTO i : lstChangeStatus) {
        // check quyen
        RiskChangeStatusRoleDTO cfgRole = new RiskChangeStatusRoleDTO();
        cfgRole.setRiskChangeStatusId(i.getId());
        List<RiskChangeStatusRoleDTO> lstRoleChangeStatus = riskCategoryServiceProxy
            .getListRiskChangeStatusRoleDTO(cfgRole);
        if (lstRoleChangeStatus != null && lstRoleChangeStatus.size() > 0) {
          for (RiskChangeStatusRoleDTO role : lstRoleChangeStatus) {
            if (listRole.indexOf(role.getRoleId()) > -1) {
              listStatusNext.add(i.getNewStatus());
              break;
            }
          }
        }
      }
    }
    return listStatusNext;
  }

  @Override
  public Datatable getListRiskRelationBySystem(RiskRelationDTO riskRelationDTO) {
    List<RiskRelationDTO> listData = new ArrayList<>();
    Datatable data = new Datatable();
    UserToken userToken = ticketProvider.getUserToken();
    String system = riskRelationDTO.getSystem();
    int totalRow = 0;
    if ("CR".equalsIgnoreCase(system)) {
      CrInsiteDTO crInsiteDTO = new CrInsiteDTO();
      crInsiteDTO.setCrNumber(riskRelationDTO.getSystemCode());
      crInsiteDTO.setCreatedDateFrom(DateUtil.date2ddMMyyyyHHMMss(riskRelationDTO.getCreateTime()));
      crInsiteDTO.setCreatedDateTo(DateUtil.date2ddMMyyyyHHMMss(riskRelationDTO.getEndTime()));
      List<CrDTO> list = crServiceProxy.getListCrInfo(crInsiteDTO);
      totalRow = list.size();
      list = (List<CrDTO>) DataUtil
          .subPageList(list, riskRelationDTO.getPage(), riskRelationDTO.getPageSize());
      if (list != null && list.size() > 0) {
        for (CrDTO crDTO : list) {
          RiskRelationDTO dto = new RiskRelationDTO();
          dto.setContent(crDTO.getTitle());
          dto.setCreatePersonName(crDTO.getChangeOrginatorName());
          dto.setCreateTime(DateTimeUtils.convertStringToDate(crDTO.getCreatedDate()));
          dto.setEndTime(DateTimeUtils.convertStringToDate(crDTO.getLatestStartTime()));
          dto.setSystem("CR");
          dto.setStatus(convertStatusCR(crDTO.getState()));
          dto.setSystemCode(crDTO.getCrNumber());
          dto.setSystemId(Long.valueOf(crDTO.getCrId()));
          dto.setReceiveUnitName(crDTO.getChangeResponsibleUnitName());
          dto.setTotalRow(totalRow);
          listData.add(dto);
        }
      }
    } else if ("SR".equalsIgnoreCase(system)) {
      SRDTO dtoTmp = new SRDTO();
      dtoTmp.setSrCode(riskRelationDTO.getSystemCode());
      dtoTmp.setCreateFromDate(DateUtil.date2ddMMyyyyHHMMss(riskRelationDTO.getCreateTime()));
      dtoTmp.setCreateToDate(DateUtil.date2ddMMyyyyHHMMss(riskRelationDTO.getEndTime()));
      List<SRDTO> lst = srServiceProxy.getListSRForWO(dtoTmp);
      totalRow = lst.size();
      lst = (List<SRDTO>) DataUtil
          .subPageList(lst, riskRelationDTO.getPage(), riskRelationDTO.getPageSize());
      if (lst != null) {
        for (SRDTO i : lst) {
          RiskRelationDTO o = new RiskRelationDTO();
          o.setContent(i.getTitle());
          o.setCreatePersonName(i.getCreatedUser());
          o.setCreateTime(DateTimeUtils.convertStringToDate(i.getCreatedTime()));
          o.setEndTime(DateTimeUtils.convertStringToDate(i.getEndTime()));
          o.setSystem("SR");
          o.setStatus(i.getStatusName());
          o.setSystemCode(i.getSrCode());
          o.setSystemId(Long.valueOf(i.getSrId()));
          o.setReceiveUnitName(i.getUnitName());
          o.setTotalRow(totalRow);
          listData.add(o);
        }
      }
    } else if ("WO".equalsIgnoreCase(system)) {
      WoInsideDTO dtoTmp = new WoInsideDTO();
      dtoTmp.setWoCode(riskRelationDTO.getSystemCode());
      dtoTmp.setUserId(userToken.getUserID());
      dtoTmp.setStartTimeFrom(riskRelationDTO.getCreateTime());
      dtoTmp.setStartTimeTo(riskRelationDTO.getEndTime());
      Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
      dtoTmp.setOffSetFromUser(offsetFromUser);
      List<WoInsideDTO> lst = woServiceProxy.getListDataForRisk(dtoTmp);
      totalRow = lst.size();
      lst = (List<WoInsideDTO>) DataUtil
          .subPageList(lst, riskRelationDTO.getPage(), riskRelationDTO.getPageSize());
      if (lst != null && lst.size() > 0) {
        for (WoInsideDTO i : lst) {
          RiskRelationDTO o = new RiskRelationDTO();
          o.setContent(i.getWoContent());
          o.setCreatePersonName(i.getCreatePersonName());
          o.setCreateTime(i.getCreateDate());
          o.setEndTime(i.getEndTime());
          o.setStatus(convertStatus2String(i.getStatus()));
          o.setSystem("WO");
          o.setSystemCode(i.getWoCode());
          o.setSystemId(i.getWoId());
          o.setReceiveUnitName(i.getCdName());
          o.setTotalRow(totalRow);
          listData.add(o);
        }
      }
    }
    int pages = (int) Math.ceil(totalRow * 1.0 / riskRelationDTO.getPageSize());
    data.setTotal(totalRow);
    data.setPages(pages);
    data.setData(listData);
    return data;
  }

  @Override
  public File exportDataRisk(RiskDTO riskDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    Double offSetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    riskDTO = converTimeFromClientToServerWeb(riskDTO, offSetFromUser);
    riskDTO.setOffSetFromUser(offSetFromUser);
    riskDTO.setUserId(userToken.getUserID());
    setMapStatus();
    setMapPriority();
    setMapEffect();
    setMapFrequency();
    setMapRedundacy();
    List<RiskDTO> list = riskRepository.getListRiskExport(riskDTO);
    if (!commonRepository.checkMaxRowExport(list.size())) {
      if (list != null && list.size() > 0) {
        for (RiskDTO dto : list) {
          setStatusName(dto);
          setPriorityName(dto);
          setEffectName(dto);
          setFrequencyName(dto);
          setRedundacyName(dto);
          setFileName(dto);

          UnitDTO subject = unitRepository.findUnitById(dto.getSubjectId());
          dto.setSubjectName(subject.getUnitName());

          dto.setCreateUnitName(dto.getCreateUnitName() + "(" + dto.getCreateUnitCode() + ")");
          dto.setReceiveUnitName(dto.getReceiveUnitName() + "(" + dto.getReceiveUnitCode() + ")");
        }
      }
      String[] header = new String[]{"riskCode", "riskName", "statusName", "riskTypeName",
          "systemName", "subjectName", "description", "priorityName", "effectName", "effectDetail",
          "frequencyName", "createUnitName", "createUserName", "createTime", "suggestSolution",
          "receiveUserName", "receiveUnitName", "isExternalVtnet", "startTime", "endTime",
          "remainTime", "redundacyName", "evedence", "frequencyDetail", "receivedDate", "solution",
          "resultProcessing", "reasonAccept", "reasonReject", "reasonCancel", "openedDate",
          "rejectedDate", "acceptedDate", "canceledDate", "closedDate", "fileName",
          "lastUpdateTime",
          "riskComment", "logTime"};
      Date date = DateTimeUtils.convertDateOffset();
      return handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(date));
    } else {
      throw new Exception();
    }
  }

  @Override
  public List<GnocFileDto> getFileByBusinessId(Long businessId) {
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.RISK_CONFIG);
    gnocFileDto.setBusinessId(businessId);
    return gnocFileRepository.getListGnocFileByDto(gnocFileDto);
  }

  private void setFileName(RiskDTO dto) {
    List<RiskFileEntity> listEntity = riskFileRepository.getListRiskFileByRiskId(dto.getRiskId());
    if (listEntity != null) {
      String fileName = "";
      for (RiskFileEntity entity : listEntity) {
        fileName += entity.getFileName() + ", ";
      }
      fileName = fileName.trim();
      if (fileName.length() > 0) {
        fileName = fileName.substring(0, fileName.lastIndexOf(","));
      }
      dto.setFileName(fileName);
    }
  }

  public void setFrequencyName(RiskDTO dto) {
    if (!mapFrequency.isEmpty()) {
      for (Map.Entry<Long, String> item : mapFrequency.entrySet()) {
        if (item.getKey().equals(dto.getFrequency())) {
          dto.setFrequencyName(item.getValue());
          break;
        }
      }
    }
  }

  public void setStatusName(RiskDTO dto) {
    if (!mapStatus.isEmpty()) {
      for (Map.Entry<String, String> item : mapStatus.entrySet()) {
        if (String.valueOf(dto.getStatus()).equals(item.getKey())) {
          dto.setStatusName(item.getValue());
          break;
        }
      }
    }
  }

  public void setPriorityName(RiskDTO dto) {
    if (!mapPriority.isEmpty()) {
      for (Map.Entry<Long, String> item : mapPriority.entrySet()) {
        if (item.getKey().equals(dto.getPriorityId())) {
          dto.setPriorityName(item.getValue());
          break;
        }
      }
    }
  }

  public File handleFileExport(List<RiskDTO> list, String[] columnExport, String date)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("risk.export.sheetname");
    String title = I18n.getLanguage("risk.export.title");
    String fileNameOut = I18n.getLanguage("risk.export.fileNameOut");
    String headerPrefix = "language.risk";
    String firstLeftHeader = I18n.getLanguage("risk.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("risk.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("risk.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("risk.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle = I18n.getLanguage("risk.export.exportDate", date);
    ConfigFileExport configFileExport = new ConfigFileExport(
        list,
        sheetName,
        title,
        subTitle,
        startRow,
        cellTitleIndex,
        mergeTitleEndIndex,
        true,
        headerPrefix,
        lstHeaderSheet1,
        fieldSplit,
        "",
        firstLeftHeader,
        secondLeftHeader,
        firstRightHeader,
        secondRightHeader
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(startRow, 0, 0, 0,
        I18n.getLanguage("risk.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false,
          0, 0, new String[]{}, null, "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
  }

  public String convertStatus2String(Long status) {
    if (status == null) {
      return "";
    } else if (status.equals(0L)) {
      return I18n.getLanguage("risk.wo.status.UNASSIGNED");
    } else if (status.equals(1L)) {
      return I18n.getLanguage("risk.wo.status.ASSIGNED");
    } else if (status.equals(2L)) {
      return I18n.getLanguage("risk.wo.status.REJECT");
    } else if (status.equals(3L)) {
      return I18n.getLanguage("risk.wo.status.DISPATCH");
    } else if (status.equals(4L)) {
      return I18n.getLanguage("risk.wo.status.ACCEPT");
    } else if (status.equals(5L)) {
      return I18n.getLanguage("risk.wo.status.INPROCESS");
    } else if (status.equals(6L)) {
      return I18n.getLanguage("risk.wo.status.CLOSED_FT");
    } else if (status.equals(7L)) {
      return I18n.getLanguage("risk.wo.status.DRAFT");
    } else if (status.equals(8L)) {
      return I18n.getLanguage("risk.wo.status.CLOSED_CD");
    } else if (status.equals(9L)) {
      return I18n.getLanguage("risk.wo.status.PENDING");
    } else {
      return "";
    }
  }

  public String convertStatusCR(String stateId) {
    if ("0".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.draft");
    }
    if ("1".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.open");
    }
    if ("2".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.queue");
    }
    if ("3".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.coordinate");
    }
    if ("4".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.evaluate");
    }
    if ("5".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.approve");
    }
    if ("6".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.accept");
    }
    if ("7".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.resolve");
    }
    if ("8".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.incomplete");
    }
    if ("9".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.close");
    }
    if ("10".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.cancel");
    }
    if ("11".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.wait_cab");
    }
    if ("12".equals(stateId)) {
      return I18n.getLanguage("risk.cr.state.cab");
    }
    return "";
  }

  public List<RiskChangeStatusDTO> getListChangeStatus(Long typeId, Long oldStatus,
      Long newStatus, Long priorityId) {
    RiskChangeStatusDTO dto = new RiskChangeStatusDTO();
    dto.setRiskTypeId(typeId);
    dto.setOldStatus(oldStatus);
    dto.setNewStatus(newStatus);
    dto.setRiskPriority(priorityId);
    dto.setIsSearch(false);
    return riskCategoryServiceProxy.getListRiskChangeStatusDTO(dto);
  }

  public Boolean checkTT(RiskDTO riskDTO) {
    UnitDTO un = null;
    if (riskDTO.getUnitIdLogin() != null) {
      un = unitRepository.findUnitById(riskDTO.getUnitIdLogin());
    }
    UnitDTO unp = null;
    if (un != null && un.getParentUnitId() != null) {
      unp = unitRepository.findUnitById(un.getParentUnitId());
    }
    Long locationId = null;
    if (unp != null && unp.getLocationId() != null) {
      locationId = unp.getLocationId();
    } else if (un != null && un.getLocationId() != null) {
      locationId = un.getLocationId();
    }
    if (locationId != null) {
      RiskSystemEntity riskSystemEntity = riskSystemRepository
          .getRiskSystemById(riskDTO.getSystemId());
      if (riskSystemEntity != null && riskSystemEntity.getCountryId() != null) {
        CatItemDTO catItemDTO = catItemRepository.getCatItemById(riskSystemEntity.getCountryId());
        CatLocationDTO catLocationDTO = catLocationRepository
            .getNationByLocationId(locationId);
        if (catItemDTO != null && catLocationDTO != null
            && StringUtils.isNotNullOrEmpty(catItemDTO.getDescription())
            && StringUtils.isNotNullOrEmpty(catLocationDTO.getNationCode())
            && catItemDTO.getDescription().equals(catLocationDTO.getNationCode())) {
          return true;
        }
      }
    }
    return false;
  }

  public Boolean checkRoleRisk(Long unitId, Long userId, Long role) {
    UsersEntity us = null;
    if (userId != null) {
      us = userRepository.getUserByUserIdCheck(userId);
    }
    UnitDTO un = null;
    if (unitId != null) {
      un = unitRepository.findUnitById(unitId);
    }
    UnitDTO unp = null;
    if (un != null && un.getParentUnitId() != null) {
      unp = unitRepository.findUnitById(un.getParentUnitId());
    }
    if (us != null) {
      if (Constants.RISK_ROLE.NVQLRR.equals(role)) { // chuyen vien quan ly rui ro
        return userRepository.checkRoleOfUser(Constants.USER_ROLE.CVQLRR, us.getUserId());
      } else if (Constants.RISK_ROLE.LDPQLRR.equals(role)
          && unitId != null) { // lanh dao phong quan ly rui ro
        if (us.getUnitId() != null && (unitId.equals(us.getUnitId())
            || (unp != null && unp.getUnitId() != null && unp.getUnitId()
            .equals(us.getUnitId())))) {
          return userRepository.checkRoleOfUser(Constants.USER_ROLE.TP, us.getUserId());
        }
      } else if (Constants.RISK_ROLE.LDPXLRR.equals(role)) { // lanh dao phong xu ly rui ro
        if (us.getUnitId() != null && unitId != null && unitId.equals(us.getUnitId())
            || (unp != null && unp.getUnitId() != null && unp.getUnitId().equals(us.getUnitId()))) {
          return userRepository.checkRoleOfUser(Constants.USER_ROLE.TP, us.getUserId());
        }
      } else if (Constants.RISK_ROLE.BGDTT.equals(role)) { // ban giam doc trung tam
        return userRepository.checkRoleOfUser(Constants.USER_ROLE.BGDTT, us.getUserId());
      } else if (Constants.RISK_ROLE.CVQLRRTT
          .equals(role)) { // chuyen vien quan ly rui ro thi truong
        return userRepository.checkRoleOfUser(Constants.USER_ROLE.CVQLRRTT, us.getUserId());
      } else if (RISK_ROLE.LD_CREATE_UNIT.equals(role)) { // lanh dao phong don vi tao
        if (us.getUnitId() != null && unitId != null && unitId.equals(us.getUnitId())
            || (unp != null && unp.getUnitId() != null && unp.getUnitId().equals(us.getUnitId()))) {
          return userRepository.checkRoleOfUser(Constants.USER_ROLE.TP, us.getUserId());
        }
      }
    }
    return false;
  }

  public void sendMesseageUpdateRisk(RiskDTO riskDTO, Long oldStatus) {
    try {
      // lay danh sach cau hinh
      RiskChangeStatusDTO riskChangeStatusDTO = new RiskChangeStatusDTO(riskDTO.getRiskTypeId(),
          oldStatus, riskDTO.getStatus(), riskDTO.getPriorityId());
      List<RiskChangeStatusDTO> lstChangeStatus = riskChangeStatusRepository
          .onSearch(riskChangeStatusDTO, 0, Integer.MAX_VALUE, "", "");
      RiskChangeStatusDTO dtoChange = null;
      if (lstChangeStatus != null && lstChangeStatus.size() > 0) { // co cau hinh
        dtoChange = lstChangeStatus.get(0);
      } else {
        riskChangeStatusDTO.setRiskTypeId(null);
        riskChangeStatusDTO.setIsDefault(1L);
        lstChangeStatus = riskChangeStatusRepository
            .onSearch(riskChangeStatusDTO, 0, Integer.MAX_VALUE, "", "");
        if (lstChangeStatus != null && lstChangeStatus.size() > 0) {  // lay cau hinh mac dinh
          dtoChange = lstChangeStatus.get(0);
        }
      }
      if (dtoChange != null) {
        // nhan tin nhan vien tao
        try {
          if (dtoChange.getSendCreate() != null && dtoChange.getSendCreate().equals(1L)
              && StringUtils.isNotNullOrEmpty(dtoChange.getCreateContent())) {
            UsersEntity u = userRepository.getUserByUserIdCheck(riskDTO.getCreateUserId());
            String createSmsContent = getSmsContenFromCfg(u.toDTO(), dtoChange.getCreateContent());
            createSmsContent = replaceSmsContent(riskDTO, createSmsContent);
            createMessage(u.toDTO(), createSmsContent);
          }
        } catch (Exception e) {
          log.error(
              "GNOC_RISK: Co loi khi nhan tin cho nhan vien tao RISK:" + riskDTO.getRiskCode(), e);
        }
        // nhan tin don vi quan ly rui ro
        try {
          if (dtoChange.getSendRiskManagementUnit() != null
              && (dtoChange.getSendRiskManagementUnit().equals(1L)
              || dtoChange.getSendRiskManagementUnit().equals(3L))
              && StringUtils.isNotNullOrEmpty(dtoChange.getRiskManagementUnitContent())) {
            List<Users> lstUs = new ArrayList<>();
            // lay danh sach don vi quan ly rui ro
            List<UnitDTO> lstUn = getLstUnitManageSystem(Long.valueOf(riskDTO.getSystemId()));
            for (UnitDTO u : lstUn) {
              List<Users> lstTmp = userRepository.getListUserOfUnit(u.getUnitId());
              // neu chi giao cho TP va don vi la ban
              if (dtoChange.getSendRiskManagementUnit().equals(3L) && (u.getIsCommittee() != null
                  && u.getIsCommittee().equals(1L))) {
                UnitDTO unp = unitRepository.findUnitById(u.getParentUnitId());
                List<Users> lstParentTmp = userRepository.getListUserOfUnit(unp.getUnitId());
                if (lstParentTmp != null && lstParentTmp.size() > 0) {
                  lstTmp.addAll(lstParentTmp);
                }
              }
              if (lstTmp != null && lstTmp.size() > 0) {
                lstUs.addAll(lstTmp);
              }
            }

            for (Users i : lstUs) {
              try {
                if (dtoChange.getSendRiskManagementUnit().equals(3L)) {
                  if (!userRepository.checkRoleOfUser("TP", i.getUserId())) {
                    continue;
                  }
                }
                String smsContent = getSmsContenFromCfg(i.toDTO(),
                    dtoChange.getRiskManagementUnitContent());
                smsContent = replaceSmsContent(riskDTO, smsContent);
                createMessage(i.toDTO(), smsContent);
              } catch (Exception e) {
                log.error("GNOC_RISK: Co loi khi nhan tin cho nhan vien:" + i.getUsername(), e);
              }
            }
          }
        } catch (Exception e) {
          log.error(
              "GNOC_RISK: Co loi khi nhan tin cho nhan vien xu ly RISK:" + riskDTO.getRiskCode(),
              e);
        }
        // nhan tin nhan vien thuc hien
        try {
          if (dtoChange.getSendReceiveUser() != null && dtoChange.getSendReceiveUser().equals(1L)
              && StringUtils.isNotNullOrEmpty(dtoChange.getReceiveUserContent())
              && riskDTO.getReceiveUserId() != null) {
            UsersEntity u = userRepository.getUserByUserIdCheck(riskDTO.getReceiveUserId());
            String smsContent = getSmsContenFromCfg(u.toDTO(), dtoChange.getReceiveUserContent());
            smsContent = replaceSmsContent(riskDTO, smsContent);
            createMessage(u.toDTO(), smsContent);
          }
        } catch (Exception e) {
          log.error(
              "GNOC_RISK: Co loi khi nhan tin cho nhan vien xu ly RISK:" + riskDTO.getRiskCode(),
              e);
        }
        // nhan tin don vi thuc hien
        try {
          if (dtoChange.getSendReceiveUnit() != null && (dtoChange.getSendReceiveUnit().equals(1L)
              || dtoChange.getSendReceiveUnit().equals(3L))
              && StringUtils.isNotNullOrEmpty(dtoChange.getReceiveUnitContent())
              && riskDTO.getReceiveUnitId() != null) {
            // lay danh sach don vi quan ly rui ro
            UnitDTO u = unitRepository.findUnitById(riskDTO.getReceiveUnitId());
            List<Users> lstUs = userRepository.getListUserOfUnit(riskDTO.getReceiveUnitId());
            // neu chi giao cho TP va don vi la ban
            if (dtoChange.getSendRiskManagementUnit().equals(3L)
                && u.getIsCommittee() != null && u.getIsCommittee().equals(1L)) {
              UnitDTO unp = unitRepository.findUnitById(u.getParentUnitId());
              List<Users> lstParentUs = userRepository.getListUserOfUnit(unp.getUnitId());
              if (lstParentUs != null && lstParentUs.size() > 0) {
                if (lstUs == null) {
                  lstUs = new ArrayList<>();
                }
                lstUs.addAll(lstParentUs);
              }
            }
            for (Users i : lstUs) {
              try {
                if (dtoChange.getSendRiskManagementUnit().equals(3L)) {
                  if (!userRepository.checkRoleOfUser("TP", i.getUserId())) {
                    continue;
                  }
                }
                String smsContent = getSmsContenFromCfg(i.toDTO(),
                    dtoChange.getReceiveUnitContent());
                smsContent = replaceSmsContent(riskDTO, smsContent);
                createMessage(i.toDTO(), smsContent);
              } catch (Exception e) {
                log.error("GNOC_RISK: Co loi khi nhan tin cho nhan vien:" + i.getUsername(), e);
              }
            }
          }
        } catch (Exception e) {
          log.error(
              "GNOC_RISK: Co loi khi nhan tin cho nhan vien xu ly RISK:" + riskDTO.getRiskCode(),
              e);
        }
        // nhan tin chuyen vien quan ly rui ro
        if (dtoChange.getSendNvQlrr() != null && dtoChange.getSendNvQlrr().equals(1L) && StringUtils
            .isNotNullOrEmpty(dtoChange.getNvQlrrContent())) {

          List<UsersInsideDto> lstUs = userRepository
              .getUsersByRoleCode(Constants.USER_ROLE.CVQLRR);
          if (lstUs != null) {
            for (UsersInsideDto i : lstUs) {
              try {
                UsersEntity u = userRepository.getUserByUserIdCheck(i.getUserId());
                String smsContent = getSmsContenFromCfg(u.toDTO(),
                    dtoChange.getNvQlrrContent());
                smsContent = replaceSmsContent(riskDTO, smsContent);
                createMessage(u.toDTO(), smsContent);
              } catch (Exception e) {
                log.error("GNOC_RISK: Co loi khi nhan tin cho nhan vien:" + i.getUsername(), e);
              }
            }
          }
        }
        // nhan tin ban giam doc trung tam
        if (dtoChange.getSendRiskManagement() != null && dtoChange.getSendRiskManagement()
            .equals(1L) && StringUtils
            .isNotNullOrEmpty(dtoChange.getRiskManagementContent())) {
          List<UsersInsideDto> lstUs = userRepository
              .getUsersByRoleCode(USER_ROLE.BGDTT);
          if (lstUs != null) {
            for (UsersInsideDto i : lstUs) {
              try {
                UsersEntity u = userRepository.getUserByUserIdCheck(i.getUserId());
                String smsContent = getSmsContenFromCfg(u.toDTO(),
                    dtoChange.getRiskManagementContent());
                smsContent = replaceSmsContent(riskDTO, smsContent);
                createMessage(u.toDTO(), smsContent);
              } catch (Exception e) {
                log.error("GNOC_RISK: Co loi khi nhan tin cho nhan vien:" + i.getUsername(), e);
              }
            }
          }
        }
        // nhan tin chuyen vien quan ly rui ro thi truong
        if (dtoChange.getSendNvQlrrtt() != null && dtoChange.getSendNvQlrrtt().equals(1L)
            && StringUtils
            .isNotNullOrEmpty(dtoChange.getNvQlrrttContent())) {
          List<UsersInsideDto> lstUs = userRepository
              .getUsersByRoleCode(USER_ROLE.CVQLRRTT);
          if (lstUs != null) {
            for (UsersInsideDto i : lstUs) {
              RiskDTO riskDTO1 = new RiskDTO();
              UsersEntity usersEntity = userRepository.getUserByUserId(i.getUserId());
              riskDTO1.setUnitIdLogin(usersEntity != null ? usersEntity.getUnitId() : null);
              riskDTO1.setSystemId(riskDTO.getSystemId());
              if (checkTT(riskDTO1)) {
                try {
                  UsersEntity u = userRepository.getUserByUserIdCheck(i.getUserId());
                  String smsContent = getSmsContenFromCfg(u.toDTO(),
                      dtoChange.getNvQlrrttContent());
                  smsContent = replaceSmsContent(riskDTO, smsContent);
                  createMessage(u.toDTO(), smsContent);
                } catch (Exception e) {
                  log.error("GNOC_RISK: Co loi khi nhan tin cho nhan vien:" + i.getUsername(), e);
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      log.error("GNOC_RISK: Co loi khi nhan tin RISK:" + riskDTO.getRiskCode(), e);
    }
  }

  private List<UnitDTO> getLstUnitManageSystem(Long systemId) {
    List<UnitDTO> listUnit = new ArrayList<>();
    List<RiskSystemDetailEntity> listSysEntity = riskSystemDetailRepository
        .getListEntityBySystemId(systemId);
    if (listSysEntity != null && listSysEntity.size() > 0) {
      for (RiskSystemDetailEntity systemDetailEntity : listSysEntity) {
        UnitDTO u = unitRepository.findUnitById(systemDetailEntity.getManageUnitId());
        if (u != null) {
          listUnit.add(u);
        }
      }
      return listUnit;
    }
    return null;
  }

  public void createMessage(UsersInsideDto u, String smsContent) {
    try {
      MessagesDTO message = new MessagesDTO();
      message.setSmsGatewayId(smsGatewayId);  // fix code = 5
      message.setReceiverId(String.valueOf(u.getUserId()));
      message.setReceiverUsername(u.getUsername());
      message.setReceiverFullName(u.getFullname());
      message.setSenderId(senderId);  // fix code = 400
      message.setReceiverPhone(u.getMobile());
      message.setStatus("0");
      message
          .setCreateTime(DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
      message.setContent(smsContent);
      message.setAlias("GNOC_RM");
      messagesRepository.insertOrUpdateWfm(message);
    } catch (Exception e) {
      log.error("GNOC_OD: Co loi xay ra khi tao tin nhan noi dung: " + smsContent, e);
    }
  }

  public String getSmsContenFromCfg(UsersInsideDto u, String content) {
    String[] c = content.split("####");
    if ("2".equals(u.getUserLanguage())) {
      return c.length == 2 ? c[1] : c[0];
    } else {
      return c[0];
    }
  }

  public String replaceSmsContent(RiskDTO riskDTO, String content) {
    // ma rui ro
    if (riskDTO.getRiskCode() != null) {
      content = content.replace("[riskCode]", riskDTO.getRiskCode());
    }
    // nguyen nhan tu choi
    if (riskDTO.getReasonReject() != null) {
      content = content.replace("[reasonReject]", riskDTO.getReasonReject());
    }
    // nguyen nhan huy
    if (riskDTO.getReasonCancel() != null) {
      content = content.replace("[reasonCancel]", riskDTO.getReasonCancel());
    }
    // ly do chap nhan
    if (riskDTO.getReasonAccept() != null) {
      content = content.replace("[reasonAccept]", riskDTO.getReasonAccept());
    }
    // ket qua xu ly
    if (riskDTO.getResultProcessing() != null) {
      content = content.replace("[resultProcessing]", riskDTO.getResultProcessing());
    }
    // bang chung xu ly
    if (riskDTO.getEvedence() != null) {
      content = content.replace("[evedence]", riskDTO.getEvedence());
    }
    // rui do du
    if (riskDTO.getRedundancy() != null) {
      content = content.replace("[redundancy]", String.valueOf(riskDTO.getRedundancy()));
    }
    // giai phap
    if (riskDTO.getSolution() != null) {
      content = content.replace("[solution]", riskDTO.getSolution());
    }
    //
    if (riskDTO.getRiskName() != null) {
      content = content.replace("[riskName]", riskDTO.getRiskName());
    }
    //
    if (riskDTO.getCreateTime() != null) {
      content = content
          .replace("[createTime]", DateUtil.date2ddMMyyyyHHMMss(riskDTO.getCreateTime()));
    }
    //
    if (riskDTO.getDescription() != null) {
      content = content.replace("[description]", riskDTO.getDescription());
    }
    //
    if (riskDTO.getStartTime() != null) {
      content = content
          .replace("[startTime]", DateUtil.date2ddMMyyyyHHMMss(riskDTO.getStartTime()));
    }
    //
    if (riskDTO.getEndTime() != null) {
      content = content.replace("[endTime]", DateUtil.date2ddMMyyyyHHMMss(riskDTO.getEndTime()));
    }
    return content;
  }

  public RiskDTO converTimeFromClientToServerWeb(RiskDTO riskDTO, Double offsetFromUser) {
    try {
      riskDTO.setStartTime(
          DateTimeUtils.convertDateToOffset(riskDTO.getStartTime(), offsetFromUser, true));
      riskDTO.setStartTimeFrom(
          DateTimeUtils.convertDateToOffset(riskDTO.getStartTimeFrom(), offsetFromUser, true));
      riskDTO.setStartTimeTo(
          DateTimeUtils.convertDateToOffset(riskDTO.getStartTimeTo(), offsetFromUser, true));
      riskDTO.setEndTime(
          DateTimeUtils.convertDateToOffset(riskDTO.getEndTime(), offsetFromUser, true));
      riskDTO.setEndTimeFrom(
          DateTimeUtils.convertDateToOffset(riskDTO.getEndTimeFrom(), offsetFromUser, true));
      riskDTO.setEndTimeTo(
          DateTimeUtils.convertDateToOffset(riskDTO.getEndTimeTo(), offsetFromUser, true));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return riskDTO;
  }

  public void setMapStatus() {
    Datatable dataStatus = catItemRepository
        .getItemMaster(Constants.CATEGORY.RISK_STATUS,
            LANGUAGUE_EXCHANGE_SYSTEM.RISK, APPLIED_BUSSINESS.RISK.toString(),
            Constants.ITEM_VALUE, Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataStatus.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapStatus.put(dto.getItemValue(), dto.getItemName());
      }
    }
  }

  public void setMapPriority() {
    List<CatItemDTO> list = catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null);
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapPriority.put(dto.getItemId(), dto.getItemName());
      }
    }
  }

  public void setMapEffect() {
    List<CatItemDTO> list = catItemRepository.getListItemByCategory(CATEGORY.RISK_EFFECT, null);
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapEffect.put(dto.getItemId(), dto.getItemName());
      }
    }
  }

  public void setMapFrequency() {
    List<CatItemDTO> list = catItemRepository.getListItemByCategory(CATEGORY.RISK_FREQUENCY, null);
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapFrequency.put(dto.getItemId(), dto.getItemName());
      }
    }
  }

  public void setMapRedundacy() {
    List<CatItemDTO> list = catItemRepository.getListItemByCategory(CATEGORY.RISK_REDUNDANCY, null);
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapRedundacy.put(dto.getItemId(), dto.getItemName());
      }
    }
  }

  @Override
  public RiskHistoryDTO findRiskHistoryByIdFromWeb(RiskHistoryDTO riskHistoryDTO) {
    return riskHistoryRepository.findRiskHistoryByIdFromWeb(riskHistoryDTO);
  }

  public static String compareObj(RiskDTO o1, RiskDTO o2, Double offset)
      throws JsonProcessingException {
    Map<String, Object> parameters = new HashMap<>();
    SimpleDateFormat dfm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    try {
      Field[] o1Field = o1.getClass().getDeclaredFields();
      Field[] o2Field = o2.getClass().getDeclaredFields();
      for (Field f1 : o1Field) {
        try {
          f1.setAccessible(true);
          for (Field f2 : o2Field) {
            f2.setAccessible(true);
            if (f1.getName().equals(f2.getName())) {
              var v1 = f1.get(o1);
              var v2 = f2.get(o2);
              List<String> lstField = Arrays
                  .asList("riskId", "createUserId", "createUnitId", "systemId", "arrId",
                      "priorityId",
                      "receiveUserId", "receiveUnitId", "riskTypeId", "groupUnitId", "subjectId",
                      "effect", "frequency", "redundancy",
                      "gnocFileDtos", "userIdLogin", "unitIdLogin", "gnocFileTemplate",
                      "idDeleteList",
                      "riskChangeStatusId", "userId");
              List<String> lstTime = Arrays
                  .asList("createTime", "lastUpdateTime", "startTime", "endTime", "finishTime",
                      "rejectedDate",
                      "closedTime", "closeTime", "acceptedDate", "closedDate", "canceledDate",
                      "receivedDate", "openedDate");
              if (lstTime.contains(f1.getName())) {
                v1 = DateTimeUtils.converClientDateToServerDate(dfm.format(v1), offset);
                v2 = DateTimeUtils.converClientDateToServerDate(dfm.format(v2), offset);
              }
              if (!lstField.contains(f1.getName())) {
                if (v1 != null && v2 != null && !v2.equals(v1)) {
                  String value = "{" + '"' + "oldValue" + '"' + ":" + '"' + v1 + '"' + ","
                      + '"' + "newValue" + '"' + ":" + '"' + v2 + '"' + "}";
                  parameters.put(f2.getName(), value);
                }
                if (v1 == null && v2 != null) {
                  String value = "{" + '"' + "oldValue" + '"' + ":" + '"' + "null" + '"' + ","
                      + '"' + "newValue" + '"' + ":" + '"' + v2 + '"' + "}";
                  parameters.put(f2.getName(), value);
                }
                if (v1 != null && v2 == null) {
                  String value = "{" + '"' + "oldValue" + '"' + ":" + '"' + v1 + '"' + ","
                      + '"' + "newValue" + '"' + ":" + '"' + "null" + '"' + "}";
                  parameters.put(f2.getName(), value);
                }
              }
              break;
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

  @Override
  public List<RiskDTOSearch> getListDataSearchForOther(RiskDTOSearch riskDTOSearch, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    List<RiskDTOSearch> lst = riskRepository
        .getListDataSearchForOther(riskDTOSearch, rowStart, maxRow);
    try {
      Map<String, Object> map = DataUtil
          .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.RISK,
              Constants.APPLIED_BUSSINESS.RISK_PRIORITY, I18n.getLocale());
      String sqlLanguage = (String) map.get("sql");
      Map mapParam = (Map) map.get("mapParam");
      Map mapType = (Map) map.get("mapType");

      List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
          .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);

      lst = DataUtil.setLanguage(lst, lstLanguage, "priorityId", "priorityName");

      Map<String, Object> map2 = DataUtil
          .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.RISK,
              Constants.APPLIED_BUSSINESS.RISK_TYPE, I18n.getLocale());
      String sqlLanguage2 = (String) map2.get("sql");
      Map mapParam2 = (Map) map2.get("mapParam");
      Map mapType2 = (Map) map2.get("mapType");
      List<LanguageExchangeDTO> lstLanguage2 = languageExchangeRepository
          .findBySql(sqlLanguage2, mapParam2, mapType2, LanguageExchangeDTO.class);
      lst = DataUtil.setLanguage(lst, lstLanguage2, "riskTypeId", "riskTypeName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public RiskChangeStatusDTO getCfgBusiness(RiskDTO riskDTO, Long oldStatus) {
    try {
      RiskChangeStatusDTO dtoS = new RiskChangeStatusDTO();

      dtoS.setRiskTypeId(riskDTO.getRiskTypeId());
      dtoS.setOldStatus(oldStatus);
      dtoS.setNewStatus(riskDTO.getStatus());
      dtoS.setRiskPriority(riskDTO.getPriorityId());

      List<RiskChangeStatusDTO> lstChangeStatus = riskCategoryServiceProxy
          .getListRiskChangeStatusDTO(dtoS);
      if (lstChangeStatus == null || lstChangeStatus.size() == 0) {
        dtoS.setIsDefault(1l);
        dtoS.setRiskTypeId(null);
        lstChangeStatus = riskCategoryServiceProxy.getListRiskChangeStatusDTO(dtoS);
      }
      if (lstChangeStatus != null && lstChangeStatus.size() > 0) {
        return lstChangeStatus.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public String getValueFromDesKey(String configKey, String desKey) {
    return riskRepository.getValueFromDesKey(configKey, desKey);
  }

  @Override
  public File onDownloadMultipleFile(RiskDTO riskDTO) {
    return riskRepository.onDownloadMultipleFile(riskDTO);
  }

  @Override
  public List<RiskDTO> getListDataSearchByOther(RiskDTO riskDTO) {
    List<RiskDTO> lst = (List<RiskDTO>) riskRepository.getListDataSearchWeb(riskDTO).getData();
    return lst;
  }

  @Override
  public ResultInSideDto updateRiskOtherSystem(RiskDTO riskDTO) {
    return riskRepository.updateRiskOtherSystem(riskDTO);
  }
}
