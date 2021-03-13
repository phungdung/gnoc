package com.viettel.gnoc.cr.business;

import static com.viettel.gnoc.commons.repository.BaseRepository.getSqlLanguageExchange;
import static com.viettel.gnoc.commons.repository.BaseRepository.setLanguage;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatVendorBODTO;
import com.viettel.gnoc.commons.dto.CrAlarmFaultGroupDTO;
import com.viettel.gnoc.commons.dto.CrModuleDraftDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.incident.provider.WSIIMPort;
import com.viettel.gnoc.commons.incident.provider.WSNIMSInfraPort;
import com.viettel.gnoc.commons.incident.provider.WSNocprov4Port;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.repository.CrAlarmSettingRepository;
import com.viettel.gnoc.cr.repository.CrImpactSegmentRepository;
import com.viettel.gnoc.cr.repository.CrManagerProcessRepository;
import com.viettel.nims.infra.webservice.CatVendorBO;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

/**
 * @author DungPV
 */
@Service
@Slf4j
@Transactional
public class CrAlarmSettingBusinessImpl implements CrAlarmSettingBusiness {

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected CommonRepository commonRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Value("${application.temp.folder}")
  private String tempFolder;

  private final static String Cr_Alarm_Setting_EXPORT = "Cr_Alarm_Setting_EXPORT";
  @Autowired
  protected CrManagerProcessRepository crManagerProcessRepository;

  @Autowired
  protected CrAlarmSettingRepository crAlarmSettingRepository;

  @Autowired
  protected CrImpactSegmentRepository crImpactSegmentRepository;

  private List<CrAlarmInsiteDTO> lstAlarmDTO = new ArrayList<>();

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  WSNocprov4Port wsNocprov4Port;

  @Autowired
  @Qualifier("WSNIMSInfraPorts")
  protected WSNIMSInfraPort wsnimsInfraPort;

  @Autowired
  protected WSIIMPort wsiimPort;
  @Value("${application.ws.iim_nationCodes:null}")
  String nationCodeList;

  @Override
  public Datatable getCrAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO) {
    log.info("Request to getListCrAffectedLevel : {}", crAlarmSettingDTO);
    Datatable datatable = crAlarmSettingRepository.getCrAlarmSetting(crAlarmSettingDTO);
    List<CrAlarmSettingDTO> lst = (List<CrAlarmSettingDTO>) datatable.getData();
    List<CrAlarmSettingDTO> lstCrAlarmSettingDTO = new ArrayList<>();
    for (CrAlarmSettingDTO dto : lst) {
      if (dto.getApprovalStatus() == 1) {
        dto.setStatus(I18n.getLanguage("CrAlarmSetting.status.1"));
      } else {
        dto.setStatus(I18n.getLanguage("CrAlarmSetting.status.0"));
      }
      lstCrAlarmSettingDTO.add(dto);
    }
    datatable.setData(lstCrAlarmSettingDTO);
    return datatable;
  }

  @Override
  public List<ImpactSegmentDTO> getListImpactSegmentCBB() {
    log.info("Request to getListImpactSegmentCBB : {}");
    ImpactSegmentDTO impactSegmentDTO = new ImpactSegmentDTO();
    impactSegmentDTO.setIsActive(1L);
    impactSegmentDTO.setAppliedSystem(2L);
    List<ImpactSegmentDTO> lstReturn = crImpactSegmentRepository
        .getListImpactSegmentDTO(impactSegmentDTO);
    getListLanguageExchange(lstReturn, "impactSegmentId", "impactSegmentName",
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR, Constants.APPLIED_BUSSINESS.IMPACT_SEGMENT);
    return lstReturn;
  }

  private List<?> getListLanguageExchange(List<?> lstReturn, String colId, String colName,
      String system, String bussiness) {
    try {
      String locale = I18n.getLocale();
      Map<String, Object> map = getSqlLanguageExchange(system, bussiness, locale);
      String sqlLanguage = (String) map.get("sql");
      Map mapParam = (Map) map.get("mapParam");
      Map mapType = (Map) map.get("mapType");
      List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
          .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
      lstReturn = setLanguage(lstReturn, lstLanguage, colId, colName);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstReturn;
  }

  @Override
  public File exportData(CrAlarmSettingDTO crAlarmSettingDTO) throws Exception {
    log.info("Request to exportData : {}", crAlarmSettingDTO);
    UserToken userToken = ticketProvider.getUserToken();
    List<CrAlarmSettingDTO> lstEx = crAlarmSettingRepository
        .getCrAlarmSettingExport(crAlarmSettingDTO);
    List<CrAlarmSettingDTO> lstExpot = new ArrayList<>();
    for (CrAlarmSettingDTO dto : lstEx) {
      if (dto.getApprovalStatus() == 1) {
        dto.setStatus(I18n.getLanguage("CrAlarmSetting.status.1"));
      } else {
        dto.setStatus(I18n.getLanguage("CrAlarmSetting.status.0"));
      }
      lstExpot.add(dto);
    }
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "ExportData AlarmSetting", "ExportData AlarmSetting ",
        crAlarmSettingDTO, null));
    return exportFileEx(lstExpot);
  }

  @Override
  public List<ItemDataCRInside> getListCrProcessCBB(CrProcessInsideDTO crProcessDTO) {
    log.info("Request to getListCrProcessCBB : {}", crProcessDTO);
    return crAlarmSettingRepository.listCrProcessCBB(crProcessDTO);
  }

  @Override
  public ResultInSideDto saveOrUpdateAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO) {
    log.info("Request to saveOrUpdateAlarmSetting : {}", crAlarmSettingDTO);
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<CrAlarmSettingDTO> lstCrAlarmSettingDTO = lstAlarmDtoTolstAlarmSettingDto(
        crAlarmSettingDTO.getLstAlarm(), crAlarmSettingDTO.getCrProcessId(),
        userToken.getUserName(), crAlarmSettingDTO.getAutoLoad());
    for (CrAlarmSettingDTO settingDTO : lstCrAlarmSettingDTO) {
      String check = checkDuplicate(settingDTO.getFaultId(), settingDTO.getCrProcessId(),
          settingDTO.getFaultName());
      if (check != null) {
        resultInSideDto.setKey(Constants.RESULT.DUPLICATE);
        resultInSideDto.setMessage(check);
        return resultInSideDto;
      }
    }
    if (lstCrAlarmSettingDTO.size() > 0) {
      for (CrAlarmSettingDTO dto : lstCrAlarmSettingDTO) {
        resultInSideDto = crAlarmSettingRepository.saveOrUpdateAlarmSetting(dto);
        if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
          commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
              "Add AlarmSetting",
              "Add AlarmSetting ID: " + resultInSideDto.getId(),
              crAlarmSettingDTO, null));
        }
      }
    }
    return resultInSideDto;
  }

  private String checkDuplicate(Long faultId, Long processId, String faultName) {
    if (crAlarmSettingRepository.checkDuplicate(faultId, processId).getKey()
        .equals(Constants.RESULT.DUPLICATE)) {
      return faultName + " " + I18n.getValidation("CrAlarmSettingDTO.unique");
    }
    return null;
  }

  @Override
  public ResultInSideDto updateVendorOrModuleAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO) {
    log.info("Request to updateVendorOrModuleAlarmSetting : {}", crAlarmSettingDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    List<CrAlarmSettingDTO> lstCrAlarmSettingDTO = lstAlarmDtoTolstAlarmSettingDto(
        crAlarmSettingDTO.getLstAlarm(), crAlarmSettingDTO.getCrProcessId(),
        userToken.getUserName(), crAlarmSettingDTO.getAutoLoad());
    if (lstCrAlarmSettingDTO.size() > 0) {
      for (CrAlarmSettingDTO dto : lstCrAlarmSettingDTO) {
        resultInSideDto = crAlarmSettingRepository.saveOrUpdateAlarmSetting(dto);
        if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
          commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
              "Update AlarmSetting",
              "Update AlarmSetting ID: " + resultInSideDto.getId(),
              crAlarmSettingDTO, null));
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto validateAlarmSetting(Long casId) {
    log.info("Request to validateAlarmSetting : {}", casId);
    CrAlarmSettingDTO crAlarmSettingDTO = crAlarmSettingRepository.findCrAlarmSettingById(casId);
    crAlarmSettingDTO.setApprovalStatus(1L);
    return crAlarmSettingRepository.saveOrUpdateAlarmSetting(crAlarmSettingDTO);
  }

  @Override
  public ResultInSideDto updateAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO) {
    log.info("Request to updateAlarmSetting : {}", crAlarmSettingDTO);
    ResultInSideDto resultInSideDto;
    UserToken userToken = ticketProvider.getUserToken();
    Long idParent = null;
    for (CrAlarmInsiteDTO alarmDTO : crAlarmSettingDTO.getLstAlarm()) {
      if (alarmDTO.getCrAlarmSettingId() != null) {
        idParent = alarmDTO.getCrAlarmSettingId();
        break;
      }
    }
    CrAlarmSettingDTO dto = crAlarmSettingRepository.findCrAlarmSettingById(idParent);
    if (dto != null && dto.getCrProcessId().equals(crAlarmSettingDTO.getCrProcessId())) {
      return updateAlarmSettingByProcessIdOld(crAlarmSettingDTO, userToken.getUserName());
    } else {
      resultInSideDto = saveOrUpdateAlarmSetting(crAlarmSettingDTO);
    }
    return resultInSideDto;
  }

  private ResultInSideDto updateAlarmSettingByProcessIdOld(CrAlarmSettingDTO crAlarmSettingDTO,
      String userToken) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<CrAlarmSettingDTO> lstCrAlarmSettingDTO = lstAlarmDtoTolstAlarmSettingDto(
        crAlarmSettingDTO.getLstAlarm(), crAlarmSettingDTO.getCrProcessId(),
        userToken, crAlarmSettingDTO.getAutoLoad());
    for (CrAlarmSettingDTO settingDTO : lstCrAlarmSettingDTO) {
      if (settingDTO.getCasId() == null || settingDTO.getCasId() == 0) {
        String check = checkDuplicate(settingDTO.getFaultId(), settingDTO.getCrProcessId(),
            settingDTO.getFaultName());
        if (check != null) {
          resultInSideDto.setKey(Constants.RESULT.DUPLICATE);
          resultInSideDto.setMessage(check);
          return resultInSideDto;
        }
      }
    }
    if (crAlarmSettingDTO.getLstDeleteId() != null
        || crAlarmSettingDTO.getLstDeleteId().size() > 0) {
      for (Long id : crAlarmSettingDTO.getLstDeleteId()) {
        delete(id);
      }
    }
    if (lstCrAlarmSettingDTO.size() > 0) {
      for (CrAlarmSettingDTO dto : lstCrAlarmSettingDTO) {
        resultInSideDto = crAlarmSettingRepository.saveOrUpdateAlarmSetting(dto);
        if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
          commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken,
              "Update AlarmSetting",
              "Update AlarmSetting ID: " + resultInSideDto.getId(),
              crAlarmSettingDTO, null));
        }
      }
    }
    return resultInSideDto;
  }

  private List<CrAlarmSettingDTO> lstAlarmDtoTolstAlarmSettingDto(List<CrAlarmInsiteDTO> lstAlarm,
      Long processId, String createdUser, Long autoLoad) {
    List<CrAlarmSettingDTO> lstCrAlarmSettingDTO = new ArrayList<>();
    for (CrAlarmInsiteDTO alarmDTO : lstAlarm) {
      CrAlarmSettingDTO setting = reutrnCrAlarmSettingDTO(alarmDTO, processId, createdUser,
          autoLoad);
      lstCrAlarmSettingDTO.add(setting);
    }
    return lstCrAlarmSettingDTO;
  }

  private CrAlarmSettingDTO reutrnCrAlarmSettingDTO(CrAlarmInsiteDTO alarmDTO, Long processId,
      String createdUser, Long autoLoad) {
    CatVendorBO catVendorBO = loadVendor(alarmDTO.getLstVendor());
    CrModuleDraftDTO crModuleDraftDTO = loadModule(alarmDTO.getLstModule());
    CrAlarmSettingDTO setting = new CrAlarmSettingDTO();
    setting.setCasId(alarmDTO.getCrAlarmSettingId());
    setting.setApprovalStatus(1L);
    setting.setApprovalUserId(null);
    setting.setCrProcessId(processId);
    setting.setDeviceTypeCode(alarmDTO.getDeviceTypeCode());
    setting.setFaultGroupName(alarmDTO.getFaultGroupName());
    setting.setFaultId(alarmDTO.getFaultId());
    setting.setFaultLevelCode(alarmDTO.getFaultLevelCode());
    setting.setFaultName(alarmDTO.getFaultName());
    setting.setFaultSrc(alarmDTO.getFaultSrc());
    if (crModuleDraftDTO.getMODULE_CODE() != null) {
      setting.setModuleCode(crModuleDraftDTO.getMODULE_CODE());
      setting.setModuleName(crModuleDraftDTO.getMODULE_NAME());
    } else {
      setting.setModuleCode(alarmDTO.getModuleCode() == null ? null : alarmDTO.getModuleCode());
      setting.setModuleName(alarmDTO.getModuleName() == null ? null : alarmDTO.getModuleName());
    }
    setting.setNationCode(null);
    setting.setType(1L);
    if (catVendorBO.getVendorCode() != null) {
      setting.setVendorCode(catVendorBO.getVendorCode());
      setting.setVendorName(catVendorBO.getVendorName());
    } else {
      setting.setVendorCode(alarmDTO.getVendorCode() == null ? null : alarmDTO.getVendorCode());
      setting.setVendorName(alarmDTO.getVendorName() == null ? null : alarmDTO.getVendorName());
    }
    setting.setCreatedUser(createdUser);
    setting.setKeyword(alarmDTO.getKeyword());
    setting.setAutoLoad(autoLoad);
    setting.setNumberOccurences(alarmDTO.getNumberOccurences());
    return setting;
  }

  private CatVendorBO loadVendor(List<CatVendorBO> lstVendor) {
    CatVendorBO catVendorBO = new CatVendorBO();
    String venderCode = "";
    String venderName = "";
    if (lstVendor != null && !lstVendor.isEmpty()) {
      for (CatVendorBO dto : lstVendor) {
        venderCode += dto.getVendorCode() + ",";
        venderName += dto.getVendorName() + ",";
      }
      if (venderCode.endsWith(",")) {
        venderCode = venderCode.substring(0, venderCode.length() - 1);
      }
      if (venderName.endsWith(",")) {
        venderName = venderName.substring(0, venderName.length() - 1);
      }
      catVendorBO.setVendorCode(venderCode);
      catVendorBO.setVendorName(venderName);
    }
    return catVendorBO;
  }

  private CrModuleDraftDTO loadModule(List<CrModuleDraftDTO> lstModule) {
    CrModuleDraftDTO crModuleDraftDTO = new CrModuleDraftDTO();
    String moduleCode = "";
    String moduleName = "";
    if (lstModule != null && lstModule.size() > 0) {
      for (CrModuleDraftDTO moduleDraftDTO : lstModule) {
        moduleCode += moduleDraftDTO.getMODULE_CODE() + ",";
        moduleName += moduleDraftDTO.getMODULE_NAME() + ",";
      }
      if (moduleCode.endsWith(",")) {
        moduleCode = moduleCode.substring(0, moduleCode.length() - 1);
      }
      if (moduleName.endsWith(",")) {
        moduleName = moduleName.substring(0, moduleName.length() - 1);
      }
      crModuleDraftDTO.setMODULE_CODE(moduleCode);
      crModuleDraftDTO.setMODULE_NAME(moduleName);
    }
    return crModuleDraftDTO;
  }

  @Override
  public ResultInSideDto delete(Long casId) {
    log.info("Request to delete : {}", casId);
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = crAlarmSettingRepository.deleteAlarmSetting(casId);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete AlarmSetting",
          "Delete AlarmSetting ID: " + casId,
          null, null));
    }
    return resultInSideDto;
  }

  @Override
  public List<CrAlarmFaultGroupDTO> getListGroupFaultSrc(String faultSrc, String nationCode)
      throws Exception {
    log.info("Request to getListGroupFaultSrc : {}");
    List<CrAlarmFaultGroupDTO> lstFaultGroupDTO = wsNocprov4Port.getFautGroupInfo(nationCode);
    HashMap<String, List<CrAlarmFaultGroupDTO>> groupMap = new HashMap<>();
    HashSet<String> faultSrcList = new HashSet<>();
    if (lstFaultGroupDTO == null || lstFaultGroupDTO.isEmpty()) {
      return null;
    }
    for (CrAlarmFaultGroupDTO data : lstFaultGroupDTO) {
      if (data.getFault_src() == null || data.getFault_src().trim().isEmpty()) {
        continue;
      }
      String fault = data.getFault_src().trim();
      if (faultSrcList.contains(fault)) {
        groupMap.get(fault).add(data);
      } else {
        faultSrcList.add(fault);
        List<CrAlarmFaultGroupDTO> lst = new ArrayList<>();
        lst.add(data);
        groupMap.put(fault, lst);
      }
    }
    return groupMap.get(faultSrc);
  }

  @Override
  public Datatable getAlarmList(CrAlarmInsiteDTO crAlarmDTO) throws Exception {
    log.info("Request to getAlarmList : {}", crAlarmDTO);
    Datatable datatable = new Datatable();
    CrAlarmInsiteDTO dto = new CrAlarmInsiteDTO();
    dto.setFaultSrc(crAlarmDTO.getFaultSrc());
    dto.setFaultName(crAlarmDTO.getFaultName());
    dto.setFaultGroupId(crAlarmDTO.getFaultGroupId());
    dto.setNationCode(crAlarmDTO.getNationCode());
    dto.setProcessId(crAlarmDTO.getProcessId());
    List<CrAlarmInsiteDTO> lst = wsNocprov4Port.getAlarmList(dto);
    lstAlarmDTO = new ArrayList<>();
    for (CrAlarmInsiteDTO alarmDTO : lst) {
      alarmDTO.setLstVendor(returnVendorList(alarmDTO.getVendorCode(), alarmDTO.getVendorName()));
      alarmDTO.setLstModule(returnModuleList(alarmDTO.getModuleCode(), alarmDTO.getModuleName()));
      alarmDTO.setTotalRow(lst.size());
      lstAlarmDTO.add(alarmDTO);
    }
    List<CrAlarmInsiteDTO> lstReturn = new ArrayList<>();
    int size = crAlarmDTO.getPageSize();
    size = (size > 0) ? size : 5;
    if (!lstAlarmDTO.isEmpty() && lstAlarmDTO.size() > 0) {
      int pageSize = (int) Math.ceil(lst.size() * 1.0 / size);
      datatable.setTotal(lstAlarmDTO.size());
      datatable.setPages(pageSize);
      lstReturn = (List<CrAlarmInsiteDTO>) DataUtil
          .subPageList(lstAlarmDTO, crAlarmDTO.getPage(), size);
    }
    datatable.setData(lstReturn);
    return datatable;
  }

  @Override
  public HashSet<String> getListFaultSrc(String nationCode) throws Exception {
    log.info("Request to getListFaultSrc : {}", nationCode);
    List<CrAlarmFaultGroupDTO> lstFaultGroupDTO = wsNocprov4Port.getFautGroupInfo(nationCode);
    HashSet<String> faultSrcList = new HashSet<>();
    if (lstFaultGroupDTO == null || lstFaultGroupDTO.isEmpty()) {
      return null;
    }
    for (CrAlarmFaultGroupDTO data : lstFaultGroupDTO) {
      if (data.getFault_src() == null || data.getFault_src().trim().isEmpty()) {
        continue;
      }
      String faultSrc = data.getFault_src().trim();
      if (!faultSrcList.contains(faultSrc)) {
        faultSrcList.add(faultSrc);
      }
    }
    return faultSrcList;
  }

  @Override
  public Datatable getModuleList(String serviceCode, String moduleCode,
      String unitCode, String nationCode, int page, int pageSize) throws Exception {
    log.info("Request to getModuleList : {}");
    Map<String, String> nationMap = new HashMap<>();
    try {
      String[] nationArr = nationCodeList.split(";");
      for (String data : nationArr) {
        String[] info = data.split(":");
        if (info.length != 2) {
          continue;
        }
        nationMap.put(info[0].trim(), info[1].trim());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    List<CrModuleDraftDTO> list = wsiimPort
        .getIIMModules(serviceCode, null, moduleCode, null, unitCode, nationMap.get(nationCode));
    Datatable datatable = new Datatable();
    List<CrModuleDraftDTO> lstReturn = new ArrayList<>();
    int size = pageSize;
    size = (size > 0) ? size : 5;
    if (!list.isEmpty() && list.size() > 0) {
      int pageSize1 = (int) Math.ceil(list.size() * 1.0 / size);
      datatable.setTotal(list.size());
      datatable.setPages(pageSize1);
      lstReturn = (List<CrModuleDraftDTO>) DataUtil
          .subPageList(list, page, size);
    }
    List<CrModuleDraftDTO> lstData = new ArrayList<>();
    for (CrModuleDraftDTO crModuleDraftDTO : lstReturn) {
      crModuleDraftDTO.setTotalRow(list.size());
      lstData.add(crModuleDraftDTO);
    }
    datatable.setData(lstData);
    return datatable;
  }

  @Override
  public CrAlarmSettingDTO findAlarmSettingByProcessId(CrAlarmSettingDTO crAlarmSettingDTO) {
    log.info("Request to getCrAlarmSettingDetail : {}");
    Map<String, CrAlarmInsiteDTO> mapReview = new HashMap<String, CrAlarmInsiteDTO>();
    List<CrAlarmInsiteDTO> lstAlarm = new ArrayList<>();
    List<CrAlarmSettingDTO> lst = crAlarmSettingRepository
        .findCrAlarmSettingByProcessId(crAlarmSettingDTO.getCrProcessId());
    for (CrAlarmSettingDTO dto : lst) {
      CrAlarmInsiteDTO alarmDTO = new CrAlarmInsiteDTO();
      alarmDTO.setCrAlarmSettingId(dto.getCasId());
      alarmDTO.setDeviceTypeCode(dto.getDeviceTypeCode());
      alarmDTO.setFaultGroupName(dto.getFaultGroupName());
      alarmDTO.setFaultId(dto.getFaultId());
      alarmDTO.setFaultLevelCode(dto.getFaultLevelCode());
      alarmDTO.setFaultName(dto.getFaultName());
      alarmDTO.setFaultSrc(dto.getFaultSrc());
      alarmDTO.setModuleCode(dto.getModuleCode());
      alarmDTO.setModuleName(dto.getModuleName());
      alarmDTO.setVendorCode(dto.getVendorCode());
      alarmDTO.setVendorName(dto.getVendorName());
      if (dto.getAutoLoad() != null) {
        alarmDTO.setAutoLoad(dto.getAutoLoad() == 1 ? "1" : "0");
      } else {
        alarmDTO.setAutoLoad("0");
      }
      alarmDTO.setCreatedUser(dto.getCreatedUser());
      alarmDTO.setNationCode(dto.getNationCode());
      alarmDTO.setLstVendor(returnVendorList(alarmDTO.getVendorCode(), alarmDTO.getVendorName()));
      alarmDTO.setLstModule(returnModuleList(alarmDTO.getModuleCode(), alarmDTO.getModuleName()));
      alarmDTO.setKeyword(dto.getKeyword());
      alarmDTO.setNumberOccurences(dto.getNumberOccurences());
      String key = (alarmDTO.getFaultId() + alarmDTO.getFaultName());
      if (!mapReview.containsKey(key)) {
        mapReview.put(key, alarmDTO);
        lstAlarm.add(alarmDTO);
      }
    }
    CrAlarmSettingDTO detail = new CrAlarmSettingDTO();
    if (crAlarmSettingDTO.getCasId() != null && crAlarmSettingDTO.getCasId() > 0) {
      detail = crAlarmSettingRepository.findCrAlarmSettingById(crAlarmSettingDTO.getCasId());
    }
    detail.setLstAlarm(lstAlarm);
    return detail;
  }

  @Override
  public Datatable getVendorList(String vendorCode, String vendorName, int page, int pageSize)
      throws Exception {
    log.info("Request to getVendorList : {}");
    Datatable datatable = new Datatable();
    List<CatVendorBO> lst = wsnimsInfraPort.getVendorList(vendorCode, vendorName);
    List<CatVendorBO> lstReturn = new ArrayList<>();
    int size = pageSize;
    size = (size > 0) ? size : 5;
    if (!lst.isEmpty() && lst.size() > 0) {
      int pageSize1 = (int) Math.ceil(lst.size() * 1.0 / size);
      datatable.setTotal(lst.size());
      datatable.setPages(pageSize1);
      lstReturn = (List<CatVendorBO>) DataUtil
          .subPageList(lst, page, size);
    }
    List<CatVendorBODTO> lstData = new ArrayList<>();
    for (CatVendorBO catVendorBO : lstReturn) {
      CatVendorBODTO dto = new CatVendorBODTO();
      dto.setTotalRow(lst.size());
      dto.setVendorId(catVendorBO.getVendorId());
      dto.setVendorCode(catVendorBO.getVendorCode());
      dto.setVendorName(catVendorBO.getVendorName());
      dto.setDescription(catVendorBO.getDescription());
      dto.setCatCode(catVendorBO.getCatCode());
      lstData.add(dto);
    }
    datatable.setData(lstData);
    return datatable;
  }

  @Override
  public CrAlarmSettingDTO findCrAlarmSettingById(CrAlarmInsiteDTO crAlarmDTO) {
    log.info("Request to findCrAlarmSettingById : {}", crAlarmDTO);
    CrAlarmSettingDTO crAlarmSettingDTO = new CrAlarmSettingDTO();
    if (crAlarmDTO.getCrAlarmSettingId() != null && crAlarmDTO.getCrAlarmSettingId() > 0) {
      crAlarmSettingDTO = crAlarmSettingRepository
          .findCrAlarmSettingById(crAlarmDTO.getCrAlarmSettingId());
      crAlarmSettingDTO.setLstVendor(
          returnVendorList(crAlarmSettingDTO.getVendorCode(), crAlarmSettingDTO.getVendorName()));
      crAlarmSettingDTO.setLstModule(
          returnModuleList(crAlarmSettingDTO.getModuleCode(), crAlarmSettingDTO.getModuleName()));
    } else {
      for (CrAlarmInsiteDTO dto : lstAlarmDTO) {
        if (dto.getFaultId().equals(crAlarmDTO.getFaultId())) {
          crAlarmSettingDTO = reutrnCrAlarmSettingDTO(dto, null, null, null);
          crAlarmSettingDTO
              .setLstVendor(returnVendorList(dto.getVendorCode(), dto.getVendorName()));
          crAlarmSettingDTO
              .setLstModule(returnModuleList(dto.getModuleCode(), dto.getModuleName()));
        }
      }
    }
    return crAlarmSettingDTO;
  }

  @Override
  public Map<String, String> nationMap() {
    Map<String, String> nationMap = new HashMap<>();
    String[] nationArr = nationCodeList.split(";");
    for (String data : nationArr) {
      String[] info = data.split(":");
      if (info.length != 2) {
        continue;
      }
      nationMap.put(info[0].trim(), info[0].trim());
    }
    return nationMap;
  }

  private List<CatVendorBO> returnVendorList(String vendorCode, String vendorName) {
    List<CatVendorBO> lstVendor = new ArrayList<>();
    String[] vendorCodes = null;
    String[] vendorNames = null;
    if (StringUtils.isNotNullOrEmpty(vendorCode)) {
      vendorCodes = vendorCode.split(",");
      if (StringUtils.isNotNullOrEmpty(vendorName)) {
        vendorNames = vendorName.split(",");
        for (int i = 0; i < vendorCodes.length; i++) {
          CatVendorBO catVendorBO = new CatVendorBO();
          catVendorBO.setVendorCode(vendorCodes[i]);
          if (StringUtils.isNotNullOrEmpty(vendorName) && (vendorCodes.length
              == vendorNames.length)) {
            catVendorBO.setVendorName(vendorNames[i]);
          }
          lstVendor.add(catVendorBO);
        }
      }
    }
    return lstVendor;
  }

  private List<CrModuleDraftDTO> returnModuleList(String moduleCode, String moduleName) {
    List<CrModuleDraftDTO> lstModule = new ArrayList<>();
    String[] moduleCodes = null;
    String[] moduleNames = null;
    if (StringUtils.isNotNullOrEmpty(moduleCode)) {
      moduleCodes = moduleCode.split(",");
      if (StringUtils.isNotNullOrEmpty(moduleName)) {
        moduleNames = moduleName.split(",");
        for (int i = 0; i < moduleCodes.length; i++) {
          CrModuleDraftDTO moduleDraftDTO = new CrModuleDraftDTO();
          moduleDraftDTO.setMODULE_CODE(moduleCodes[i]);
          if (StringUtils.isNotNullOrEmpty(moduleName) && (moduleCodes.length
              == moduleNames.length)) {
            moduleDraftDTO.setMODULE_NAME(moduleNames[i]);
          }
          lstModule.add(moduleDraftDTO);
        }
      }
    }
    return lstModule;
  }

  private File exportFileEx(List<CrAlarmSettingDTO> lstData) throws Exception {
    String title = I18n.getLanguage("CrAlarmSetting.export.title");
    String fileNameOut = Cr_Alarm_Setting_EXPORT;
    String subTitle = I18n
        .getLanguage("CrAlarmSetting.export.exportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = readerHeaderSheet(
//        "status",
        "crProcessName", "faultName", "vendorCode", "moduleCode", "createdUser", "keyword",
        "deviceTypeCode", "faultGroupName");
    Map<String, String> filedSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstData,
        I18n.getLanguage("CrAlarmSetting.export.title"),
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.CrAlarmSetting",
        lstHeaderSheet,
        filedSplit,
        "",
        I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configFileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configFileExport);
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

  private List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }
}
