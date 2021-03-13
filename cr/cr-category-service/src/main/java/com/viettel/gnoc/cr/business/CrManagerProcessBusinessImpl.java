package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.*;
import com.viettel.gnoc.cr.repository.CfgTempImportRepository;
import com.viettel.gnoc.cr.repository.CrImpactSegmentRepository;
import com.viettel.gnoc.cr.repository.CrManagerProcessRepository;
import com.viettel.gnoc.cr.repository.DeviceTypesRepository;
import com.viettel.gnoc.cr.repository.GroupUnitDetailRepository;
import com.viettel.gnoc.cr.repository.GroupUnitRepository;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

/**
 * @author KienPV
 */
@Service
@Slf4j
@Transactional
public class CrManagerProcessBusinessImpl implements CrManagerProcessBusiness {

  private final Map<String, String> mapAddObject = new HashMap<>();
  private final Map<String, String> mapAddObjectWo = new HashMap<>();

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  protected CrManagerProcessRepository crManagerProcessRepository;

  @Autowired
  protected DeviceTypesRepository deviceTypesRepository;

  @Autowired
  protected CrImpactSegmentRepository crImpactSegmentRepository;

  @Autowired
  protected CfgTempImportRepository cfgTempImportRepository;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected GroupUnitRepository groupUnitRepository;

  @Autowired
  protected GroupUnitDetailRepository groupUnitDetailRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected CommonRepository commonRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  private final static String CR_PROCESS_MANAGER_EXPORT = "Cr_Process_Manager_EXPORT";
  public static final String XLSX_FILE_EXTENTION = ".xlsx";
  public static final String XLSM_FILE_EXTENTION = ".xlsm";

  Map<String, Long> mapImpactType = new HashMap<>();
  List<Object[]> woCheck = new ArrayList<>();

  List<Integer> temp = new ArrayList<>();

  @Override
  public Datatable getListSearchCrProcess(CrProcessInsideDTO crProcessDTO) {
    return crManagerProcessRepository.getListSearchCrProcess(crProcessDTO);
  }

  @Override
  public List<CrProcessInsideDTO> actionGetListProcessType(CrProcessInsideDTO crProcessDTO) {
    log.info("Request to actionGetListProcessType : {}", crProcessDTO);
    return crManagerProcessRepository.actionGetListProcessType(crProcessDTO);
  }

  @Override
  public List<ItemDataCRInside> getListCrProcessCBB(CrProcessInsideDTO crProcessDTO) {
    log.info("Request to getListCrProcessCBB : {}", crProcessDTO);
    return crManagerProcessRepository.getListCrProcessCBB(crProcessDTO);
  }

  @Override
  public List<CrProcessInsideDTO> getRootCrProcess() {
    log.info("Request to getRootCrProcess : {}");
    return crManagerProcessRepository.getRootCrProcess();
  }

  @Override
  public List<CrProcessGroup> getLstFileFromProcessId(CrProcessGroup crProcessGroup) {
    log.info("Request to getLstFileFromProcessId : {}", crProcessGroup);
    return crManagerProcessRepository.getLstFileFromProcessId(crProcessGroup);
  }

  @Override
  public List<CrProcessGroup> getLstUnitFromProcessId(Long crProcessId) {
    log.info("Request to getLstUnitFromProcessId : {}", crProcessId);
    return crManagerProcessRepository.getLstUnitFromProcessId(crProcessId);
  }

  @Override
  public List<CrProcessInsideDTO> getLstAllChildrenByProcessId(Long crProcessId) {
    log.info("Request to getLstAllChildrenByProcessId : {}", crProcessId);
    return crManagerProcessRepository.getLstAllChildrenByProcessId(crProcessId);
  }

  @Override
  public CrProcessInsideDTO getParentByProcessId(Long crProcessId) {
    log.info("Request to getParentByProcessId : {}", crProcessId);
    return crManagerProcessRepository.getParentByProcessId(crProcessId);
  }

  @Override
  public CrProcessInsideDTO getCrProcessDetail(Long crProcessId) {
    log.info("Request to getCrProcessDetail : {}", crProcessId);
    CrProcessInsideDTO crProcessDTO = crManagerProcessRepository.getCrProcessDetail(crProcessId);
    crProcessDTO.setListCrProcessName(languageExchangeRepository
        .getListLanguageExchangeById("OPEN_PM", "OPEN_PM.CR_PROCESS", crProcessId, null));
    return crProcessDTO;
  }

  @Override
  public CrProcessInsideDTO getCrProcessById(Long crProcessId) {
    log.info("Request to getCrProcessDetail : {}", crProcessId);
    return crManagerProcessRepository.getCrProcessById(crProcessId);
  }

  @Override
  public List<CrProcessInsideDTO> getListCrProcessDTO(CrProcessInsideDTO crProcessDTO, int rowStart,
      int maxRow,
      String sortType, String sortFieldList) {
    return crManagerProcessRepository
        .getListCrProcessDTO(crProcessDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public ResultInSideDto deleteGroupUnitOrFileByProcessId(Long crProcessId) {
    log.info("Request to deleteGroupUnitOrFileByProcessId : {}", crProcessId);
    return crManagerProcessRepository.deleteGroupUnitOrFileByProcessId(crProcessId);
  }

  @Override
  public ResultInSideDto deleteAllChildByParent(Long crProcessId) {
    log.info("Request to deleteAllChildByParent : {}", crProcessId);
    return crManagerProcessRepository.deleteAllChildByParent(crProcessId);
  }

  @Override
  public ResultInSideDto deleteFileAndDataWhenChangeProcess(String crId) {
    log.info("Request to deleteFileAndDataWhenChangeProcess : {}", crId);
    return crManagerProcessRepository.deleteFileAndDataWhenChangeProcess(crId);
  }

  @Override
  public ResultInSideDto saveCrProcessWo(CrProcessWoDTO crProcessWoDTO) {
    log.info("Request to saveCrProcessWo : {}", crProcessWoDTO);
    return crManagerProcessRepository.saveCrProcessWo(crProcessWoDTO);
  }

  @Override
  public ResultInSideDto deleteListCrProcessWo(List<Long> lstCrProcessWoId) {
    log.info("Request to deleteListCrProcessWo : {}", lstCrProcessWoId);
    return crManagerProcessRepository.deleteListCrProcessWo(lstCrProcessWoId);
  }

  @Override
  public List<CrProcessWoDTO> getLstWoFromProcessId(Long crProcessId) {
    log.info("Request to getLstWoFromProcessId : {}", crProcessId);
    return crManagerProcessRepository.getLstWoFromProcessId(crProcessId);
  }

  @Override
  public List<CrOcsScheduleDTO> getListCrOcsScheduleDTO(CrOcsScheduleDTO crOcsScheduleDTO) {
    log.info("Request to getListCrOcsScheduleDTO : {}", crOcsScheduleDTO);
    return crManagerProcessRepository.getListCrOcsScheduleDTO(crOcsScheduleDTO);
  }

  @Override
  public ResultInSideDto insertOrUpdateCrOcsScheduleDTO(CrOcsScheduleDTO crOcsScheduleDTO) {
    log.info("Request to getListCrOcsScheduleDTO : {}", crOcsScheduleDTO);
    return crManagerProcessRepository.insertOrUpdateCrOcsScheduleDTO(crOcsScheduleDTO);
  }

  public void checkLevel(CrProcessInsideDTO crProcessDTO) {
    // If the new check is added to level 3, then some of the values will follow step 2
    if (StringUtils.isStringNullOrEmpty(crProcessDTO.getCrProcessId())) {
      if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getParentId())) {
        CrProcessInsideDTO checkLevel = getCrProcessById(crProcessDTO.getParentId());
        if (checkLevel.getCrProcessLevel() == 2) {
          crProcessDTO.setImpactType(checkLevel.getImpactType());
          crProcessDTO.setImpactCharacteristic(checkLevel.getImpactCharacteristic());
          crProcessDTO.setCrTypeId(checkLevel.getCrTypeId());
          crProcessDTO.setRiskLevel(checkLevel.getRiskLevel());
          crProcessDTO.setImpactSegmentId(checkLevel.getImpactSegmentId());
          crProcessDTO.setDeviceTypeId(checkLevel.getDeviceTypeId());
          crProcessDTO.setOtherDept(checkLevel.getOtherDept());
//          crProcessDTO.setIsVmsaActiveCellProcess(checkLevel.getIsVmsaActiveCellProcess());
//          crProcessDTO.setRequireFileLog(checkLevel.getRequireFileLog());
//          crProcessDTO.setRequireApprove(checkLevel.getRequireApprove());
//          crProcessDTO.setCloseCrWhenResolveSuccess(checkLevel.getCloseCrWhenResolveSuccess());
//          crProcessDTO.setIsLaneImpact(checkLevel.getIsLaneImpact());
//          crProcessDTO.setRequireMop(checkLevel.getRequireMop());
//          crProcessDTO.setVmsaActiveCellProcessKey(checkLevel.getVmsaActiveCellProcessKey());
          crProcessDTO.setApprovalLevel(checkLevel.getApprovalLevel());
        }
      }
    }
  }

  @Override
  public ResultInSideDto insertOrUpdateCrProcessDTO(CrProcessInsideDTO crProcessDTO) {
    log.info("Request to getListCrOcsScheduleDTO : {}", crProcessDTO);

	checkLevel(crProcessDTO);
    CrProcessInsideDTO oldHis = new CrProcessInsideDTO();
    if (crProcessDTO.getCrProcessId() != null) {
      oldHis = getCrProcessById(crProcessDTO.getCrProcessId());
    }
    
    ResultInSideDto resultInSideDto = crManagerProcessRepository
        .insertOrUpdateCrProcessDTO(crProcessDTO);

    // check if them moi thang 3 an theo thang 2,  luc nay chua co data
    if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getParentId())) {
      CrProcessInsideDTO checkLevel = crManagerProcessRepository
          .getCrProcessDetail(crProcessDTO.getParentId());
      if (checkLevel.getCrProcessLevel() == 2) {
        List<CrProcessTemplateDTO> listTemp = checkLevel.getListCrProcessTemplate();
        List<CrProcessWoDTO> listWo = checkLevel.getListCrProcessWo();
        List<CrProcessDeptGroupDTO> groupDTOList = checkLevel.getListCrProcessDeptGroup();
        if (listTemp != null && listTemp.size() > 0) {
          for (CrProcessTemplateDTO crProcessTemplateDTO : listTemp) {
            crProcessTemplateDTO.setCpteId(0l);
            crProcessTemplateDTO.setCrProcessId(resultInSideDto.getId());
            crManagerProcessRepository.saveCrProcessTemplate(crProcessTemplateDTO);
          }
        }

        if (groupDTOList != null && groupDTOList.size() > 0) {
          for (CrProcessDeptGroupDTO groupDTO : groupDTOList) {
            groupDTO.setCpdgpId(0l);
            groupDTO.setCrProcessId(resultInSideDto.getId());
            groupDTO.setCpdgpType(1l);
            crManagerProcessRepository.saveCrProcessGroupDept(groupDTO);
          }
        }

        if (listWo != null && listWo.size() > 0) {
          for (CrProcessWoDTO crProcessWoDTO : listWo) {
            crProcessWoDTO.setCrProcessWoId(0l);
            crProcessWoDTO.setCrProcessId(resultInSideDto.getId());
            crManagerProcessRepository.saveCrProcessWo(crProcessWoDTO);
          }
        }
      }
    }

    languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.CR_PROCESS", resultInSideDto.getId(),
            crProcessDTO.getListCrProcessName());
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      //Add history
      try {
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("CR_PROCESS_MANAGEMENT");
        //Old Object History
        dataHistoryChange.setOldObject(convertListName(oldHis));
        dataHistoryChange.setActionType(crProcessDTO.getCrProcessId() != null ? "update" : "add");
        //New Object History
        dataHistoryChange.setNewObject(convertListName(crProcessDTO));
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
      String content = "";
      if (StringUtils.isStringNullOrEmpty(crProcessDTO.getCrProcessId())) {
        content = "Insert CrProcess";
      } else {
        content = "Update CrProcess";
      }

      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          content, content + " " + resultInSideDto.getId(),
          crProcessDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListCrOcsScheduleDTO(List<Long> crOcsScheduleDTOs) {
    log.info("Request to deleteListCrOcsScheduleDTO : {}", crOcsScheduleDTOs);
    return crManagerProcessRepository.deleteListCrOcsScheduleDTO(crOcsScheduleDTOs);
  }

  @Override
  public ResultInSideDto saveAllList(CrProcessInsideDTO crProcessDTO) {
    log.info("Request to saveAllList : {}", crProcessDTO);
    CrProcessInsideDTO oldHis = getCrProcessDetail(crProcessDTO.getCrProcessId());
    ResultInSideDto resultInSideDto = crManagerProcessRepository.saveAllList(crProcessDTO);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("CR_PROCESS_MANAGEMENT");
        //Old Object History
        dataHistoryChange.setOldObject(convertListName(oldHis));
        dataHistoryChange.setActionType("update");
        //New Object History
        dataHistoryChange.setNewObject(convertListName(crProcessDTO));
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.CR_PROCESS", resultInSideDto.getId(),
            crProcessDTO.getListCrProcessName());

    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update CrProcess", "Update CrProcess: " + resultInSideDto.getId(),
          crProcessDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public CrProcessWoDTO getcCrProcessWo(Long id) {
    log.info("Request to getcCrProcessWo : {}", id);
    return crManagerProcessRepository.getcCrProcessWo(id);
  }


  @Override
  public File exportData(CrProcessInsideDTO crProcessDTO) throws Exception {
    List<CrProcessInsideDTO> lstCrEx =
        crManagerProcessRepository
            .getListDataExport(crProcessDTO);

    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "exportData", "ExportData CrProcess: " + crProcessDTO.getCrProcessId(),
        crProcessDTO, null));

    return exportFileEx_NoParam(lstCrEx, "");
  }

  @Override
  public List<WoTypeInsideDTO> getListWoType() {
    log.info("Request to getListWoType : {}");
    return crManagerProcessRepository.getListWoType();
  }

  @Override
  public Datatable getLstFileTemplate(CrProcessGroup crProcessGroup) {
    return crManagerProcessRepository.getLstFileTemplate(crProcessGroup);
  }

  @Override
  public ResultInSideDto deleteListCrProcess(List<Long> crProcessIds) {
    return crManagerProcessRepository.deleteListCrProcess(crProcessIds);
  }

  @Override
  public ResultInSideDto deleteCrProcess(Long crProcessId) {
    UserToken userToken = ticketProvider.getUserToken();
    CrProcessInsideDTO oldHis = getCrProcessDetail(crProcessId);
    ResultInSideDto resultInSideDto = crManagerProcessRepository.deleteCrProcessDTO(crProcessId);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(crProcessId.toString());
        dataHistoryChange.setType("CR_PROCESS_MANAGEMENT");
        //Old Object History
        dataHistoryChange.setOldObject(convertListName(oldHis));
        dataHistoryChange.setActionType("delete");
        //New Object History
        dataHistoryChange.setNewObject(convertListName(new CrProcessInsideDTO()));
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Delete CrProcess", "Delete CrProcess: " + crProcessId,
        crProcessId, null));
    languageExchangeRepository
        .deleteListLanguageExchange("OPEN_PM", "OPEN_PM.CR_PROCESS", crProcessId);
    // Delete all child
    List<Long> listChild = crManagerProcessRepository.deleteChildByParent(crProcessId);
    for (Long id : listChild) {
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete CrProcess", "Delete CrProcess: " + id,
          id, null));
      languageExchangeRepository
          .deleteListLanguageExchange("OPEN_PM", "OPEN_PM.CR_PROCESS", id);
    }
    return resultInSideDto;
  }

  @Override
  public CrProcessWoDTO getCrProcessWoDTO(Long crProcessWoId) {
    return crManagerProcessRepository.getCrProcessWoDTO(crProcessWoId);
  }

  @Override
  public File getTemplateSepical() throws Exception {
    return exportFileEx(null, "1", true, null);
  }

  @Override
  public List<CrProcessInsideDTO> getAllCrProcess(Long parentId) {
    return crManagerProcessRepository.getAllCrProcess(parentId);
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;

    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }

    if (count != 16) {
      return false;
    }
    if (obj0 == null) {
      return false;
    }
    if (obj0[0] == null) {
      return false;
    }

    if (!(I18n.getLanguage("CrProcessManager.stt")).equalsIgnoreCase(obj0[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("CrProcessManager.crProcessName") + " (*)")
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("CrProcessManager.crProcessCode") + " (*)")
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("CrProcessManager.crTypeName") + " (*)")
        .equalsIgnoreCase(obj0[4].toString().trim().replace("\n", " "))) {
      return false;
    }
    if (!(I18n.getLanguage("CrProcessManager.riskLevelName") + " (*)")
        .equalsIgnoreCase(obj0[5].toString().trim().replace("\n", " "))) {
      return false;
    }
    if (!(I18n.getLanguage("CrProcessManager.deviceTypeCode") + " (*)")
        .equalsIgnoreCase(obj0[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("CrProcessManager.impactSegmentCode") + " (*)")
        .equalsIgnoreCase(obj0[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("CrProcessManager.impactCharacteristicName") + " (*)")
        .equalsIgnoreCase(obj0[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("CrProcessManager.impactTypeName") + " (*)")
        .equalsIgnoreCase(obj0[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("CrProcessManager.isAdd") + " (*)")
        .equalsIgnoreCase(obj0[15].toString().trim())) {
      return false;
    }

    return true;
  }

  private boolean validFileFormatSheetTwo(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;

    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }

    if (count != 9) {
      return false;
    }
    if (obj0 == null) {
      return false;
    }
    if (obj0[0] == null) {
      return false;
    }

    if (!(I18n.getLanguage("CrProcessManager.stt")).equalsIgnoreCase(obj0[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.crProcessCode"))
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.woName"))
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.durationWo"))
        .equalsIgnoreCase(obj0[4].toString().trim().replace("\n", " "))) {
      return false;
    }
    if (!(I18n.getLanguage("wo.woTypeCode"))
        .equalsIgnoreCase(obj0[5].toString().trim().replace("\n", " "))) {
      return false;
    }

    return true;
  }


  public void getDataFromImport(CrProcessInsideDTO dto, Object[] obj) {
    String[] crType = {I18n.getLanguage("CrProcessManager.crType.CR_NORMAL"),
        I18n.getLanguage("CrProcessManager.crType.CR_EMERGENCY"),
        I18n.getLanguage("CrProcessManager.crType.CR_STANDARD")};

    String[] riskType = {I18n.getLanguage("CrProcessManager.riskType.LOW"),
        I18n.getLanguage("CrProcessManager.riskType.MEDIUM"),
        I18n.getLanguage("CrProcessManager.riskType.HIGH"),
        I18n.getLanguage("CrProcessManager.riskType.VERY_HIGH")};

    String[] physicalType = {I18n.getLanguage("CrProcessManager.physicalType.LOGIC"),
        I18n.getLanguage("CrProcessManager.physicalType.PHYSICAL")};

    if (!StringUtils.isStringNullOrEmpty(obj[4])) {
      if (obj[4].toString().trim().equalsIgnoreCase(crType[0].trim())) {
        dto.setCrTypeId(0l);
      }
      if (obj[4].toString().trim().equalsIgnoreCase(crType[1].trim())) {
        dto.setCrTypeId(1l);
      }
      if (obj[4].toString().trim().equalsIgnoreCase(crType[2].trim())) {
        dto.setCrTypeId(2l);
      }
      dto.setCrTypeName(obj[4].toString());
    }

    if (!StringUtils.isStringNullOrEmpty(obj[5])) {
      if (obj[5].toString().trim().equalsIgnoreCase(riskType[0].trim())) {
        dto.setRiskLevel(1l);
      }
      if (obj[5].toString().trim().equalsIgnoreCase(riskType[1].trim())) {
        dto.setRiskLevel(2l);
      }
      if (obj[5].toString().trim().equalsIgnoreCase(riskType[2].trim())) {
        dto.setRiskLevel(3l);
      }
      if (obj[5].toString().trim().equalsIgnoreCase(riskType[3].trim())) {
        dto.setRiskLevel(4l);
      }
      dto.setRiskLevelName(obj[5].toString());
    }

    DeviceTypesDTO deviceTypesDTO = new DeviceTypesDTO();
    deviceTypesDTO.setDeviceTypeCode(obj[6] == null ? "" : obj[6].toString().trim());
    dto.setDeviceTypeCode(obj[6] == null ? "" : obj[6].toString().trim());
    DeviceTypesDTO deviceTypesBy = deviceTypesRepository
        .findDeviceTypesBy(deviceTypesDTO);
    if (deviceTypesBy != null) {
      dto.setDeviceTypeId(deviceTypesBy.getDeviceTypeId());
    } else {
      dto.setDeviceTypeId(0l);
    }

    ImpactSegmentDTO impactSegmentDTO = new ImpactSegmentDTO();
    impactSegmentDTO
        .setImpactSegmentCode(obj[7] == null ? "" : obj[7].toString().trim());
    ImpactSegmentDTO impactSegment = crImpactSegmentRepository
        .findImpactSegmentBy(impactSegmentDTO);
    dto.setImpactSegmentCode(obj[7] == null ? "" : obj[7].toString().trim());
    if (impactSegment != null) {
      dto.setImpactSegmentId(impactSegment.getImpactSegmentId());
    } else {
      dto.setImpactSegmentId(0l);
    }

    if (!StringUtils.isStringNullOrEmpty(obj[8])) {
      if (obj[8].toString().trim().equalsIgnoreCase(physicalType[0].trim())) {
        dto.setImpactCharacteristic(1l);
      }
      if (obj[8].toString().trim().equalsIgnoreCase(physicalType[1].trim())) {
        dto.setImpactCharacteristic(2l);
      }
      dto.setImpactCharacteristicName(obj[8].toString());
    }

    if (!StringUtils.isStringNullOrEmpty(obj[9])) {
      if (mapImpactType != null) {
        dto.setImpactType(mapImpactType.get(obj[9]));
        dto.setImpactTypeName(obj[9].toString());
      } else {
        dto.setImpactType(0l);
        dto.setImpactTypeName("");
      }
    }

    getObj(dto, obj);
  }

  public void getObj(CrProcessInsideDTO dto, Object[] obj) {
    String[] mopType = {I18n.getLanguage("CrProcessManager.mopType.NO"),
        I18n.getLanguage("CrProcessManager.mopType.YES")};

    String[] logType = {I18n.getLanguage("CrProcessManager.logType.NO"),
        I18n.getLanguage("CrProcessManager.logType.YES")};

    if (!StringUtils.isStringNullOrEmpty(obj[10])) {
      if (obj[10].toString().trim().equalsIgnoreCase(mopType[0].trim())) {
        dto.setRequireMop(0l);
      }
      if (obj[10].toString().trim().equalsIgnoreCase(mopType[1].trim())) {
        dto.setRequireMop(1l);
      }
      dto.setRequireMopName(obj[10].toString());
    }

    if (!StringUtils.isStringNullOrEmpty(obj[11])) {
      if (obj[11].toString().trim().equalsIgnoreCase(logType[0].trim())) {
        dto.setRequireFileLog(0l);
      }
      if (obj[11].toString().trim().equalsIgnoreCase(logType[1].trim())) {
        dto.setRequireFileLog(1l);
      }
      dto.setRequireFileLogName(obj[11].toString());
    }
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {

    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    ResultInSideDto result = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDTO.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDTO;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDTO.getKey())) {
          return resultInSideDTO;
        }
        File fileImport = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImport,
            0,//sheet
            7,//begin row
            0,//from column
            15,//to column
            2);

        List<Object[]> lstHeaderSheet2;
        lstHeaderSheet2 = CommonImport.getDataFromExcelFile(fileImport,
            1,//sheet
            7,//begin row
            0,//from column
            8,//to column
            2);

        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDTO.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDTO;
        }

        if (lstHeaderSheet2.size() == 0 || !validFileFormatSheetTwo(lstHeaderSheet2)) {
          resultInSideDTO.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDTO;
        }

        // Lay du lieu import
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(
            fileImport,
            0,//sheet
            8,//begin row
            0,//from column
            15,//to column
            1000
        );

        // Lay du lieu import sheet 2
        List<Object[]> lstDataSheet2 = CommonImport.getDataFromExcelFile(
            fileImport,
            1,//sheet
            8,//begin row
            0,//from column
            8,//to column
            1000
        );

        if (lstData.size() > 1500) {
          resultInSideDTO.setKey(RESULT.DATA_OVER);
          return resultInSideDTO;
        }

        if (lstDataSheet2.size() > 1500) {
          resultInSideDTO.setKey(RESULT.DATA_OVER);
          return resultInSideDTO;
        }

        woCheck = lstDataSheet2;

        mapAddObject.clear();
        String[] fileType = {I18n.getLanguage("CrProcessManager.file.INPUT"),
            I18n.getLanguage("CrProcessManager.file.OUT")};
        String[] action = {I18n.getLanguage("CrProcessManager.action.NO"),
            I18n.getLanguage("CrProcessManager.action.YES")};
        String[] mopType = {I18n.getLanguage("CrProcessManager.mopType.NO"),
            I18n.getLanguage("CrProcessManager.mopType.YES")};

        mapImpactType.clear();
        ObjectSearchDto searchDto = new ObjectSearchDto();
        searchDto.setIsHasChildren(0l);
        searchDto.setModuleName("GNOC_CR_IMPACT_FRAME");
        searchDto.setRownum(0l);
        List<DataItemDTO> list = commonRepository.getListCombobox(searchDto);
        for (DataItemDTO item : list) {
          mapImpactType.put(item.getItemName(), Long.parseLong(item.getItemId()));
        }

        if (!lstData.isEmpty()) {
          List<CrProcessInsideDTO> lstAddOrUpdate = new ArrayList<>();
          List<CrProcessInsideDTO> lstError = new ArrayList<>();
          for (Object[] obj : lstData) {
            CrProcessInsideDTO dto = new CrProcessInsideDTO();

            if (!StringUtils.isStringNullOrEmpty(obj[1])) {
              dto.setCrProcessName(obj[1].toString().trim());
            } else {
              dto.setCrProcessName("");
            }

            if (!StringUtils.isStringNullOrEmpty(obj[2])) {
              dto.setCrProcessCode(obj[2].toString().trim());
            } else {
              dto.setCrProcessCode("");
            }

            if (obj[3] != null) {
              CrProcessInsideDTO crProcessDTO = crManagerProcessRepository
                  .getCrProcessLevelByCode(obj[3].toString().trim());
              if (crProcessDTO != null) {
                dto.setParentId(crProcessDTO.getCrProcessId());
              }
              getDataFromImport(dto, obj);
              dto.setParentCode(obj[3].toString());
            } else {
              getDataFromImport(dto, obj);
            }

            dto.setApprovalLevel(0l);
            dto.setFileCode(obj[12] == null ? "" : obj[12].toString().trim());
            dto.setFileCodeTypeName(obj[13] == null ? "" : obj[13].toString().trim());

            dto.setGroupUnitCode(obj[14] == null ? "" : obj[14].toString());

            if (!StringUtils.isStringNullOrEmpty(obj[15])) {
              if (obj[15].toString().trim().equalsIgnoreCase(action[0].trim())) {
                dto.setIsAdd("2");
              }
              if (obj[15].toString().trim().equalsIgnoreCase(action[1].trim())) {
                dto.setIsAdd("1");
              }
              dto.setIsAddName(obj[15].toString());
            }

            if (validateImportInfo(dto)) {
              if ("1".equalsIgnoreCase(dto.getIsAdd())) {
                if (mapAddObject.get(dto.getCrProcessCode()) == null) {
                  mapAddObject.put(dto.getCrProcessCode(), dto.getCrProcessName());
                  lstAddOrUpdate.add(dto);
                } else {
                  dto.setResultImport(
                      I18n.getLanguage("CrProcessManager.err.processCode.dupplicate.inFile"));
                }
              } else {
                CrProcessInsideDTO temp = crManagerProcessRepository.checkCrProcessExist(dto);
                if (temp != null && temp.getCrProcessId() != null) {
                  dto.setCrProcessId(temp.getCrProcessId());
                  if (mapAddObject.get(dto.getCrProcessCode()) == null) {
                    mapAddObject.put(dto.getCrProcessCode(), dto.getCrProcessName());

                    lstAddOrUpdate.add(dto);
                  } else {
                    dto.setResultImport(
                        I18n.getLanguage("CrProcessManager.err.processCode.dupplicate.inFile"));
                  }
                }
              }
            }
            lstError.add(dto);
          }

          List<CrProcessWoDTO> lstErrorWo = new ArrayList<>();
          List<CrProcessWoDTO> lstAddWo = new ArrayList<>();
          mapAddObjectWo.clear();
          if (!lstDataSheet2.isEmpty()) {
            for (Object[] obj : lstDataSheet2) {
              CrProcessWoDTO crProcessWoDTO = new CrProcessWoDTO();
              if (!StringUtils.isStringNullOrEmpty(obj[1])) {
                crProcessWoDTO.setCrProcessCode(obj[1].toString().trim());
              }
              if (!StringUtils.isStringNullOrEmpty(obj[2])) {
                crProcessWoDTO.setWoName(obj[2].toString().trim());
              }
              if (!StringUtils.isStringNullOrEmpty(obj[3])) {
                crProcessWoDTO.setDescription(obj[3].toString().trim());
              }
              if (!StringUtils.isStringNullOrEmpty(obj[4])) {
                if (StringUtils.isInteger(obj[4].toString())) {
                  crProcessWoDTO.setDurationWo(Double.parseDouble(obj[4].toString()));
                } else {
                  crProcessWoDTO.setDurationWo(0.0);
                }
                crProcessWoDTO.setDurationWoName(obj[4].toString());
              }

              if (!StringUtils.isStringNullOrEmpty(obj[5])) {
                WoTypeInsideDTO typeS = new WoTypeInsideDTO();
                typeS.setEnableCreate(1L);
                typeS.setWoTypeCode(obj[5].toString().trim());
                List<WoTypeInsideDTO> listTypeWo = woCategoryServiceProxy.getListWoTypeDTO(typeS);
                if (listTypeWo != null) {
                  crProcessWoDTO.setWoTypeId(Long.valueOf(listTypeWo.get(0).getWoTypeId()));
                  crProcessWoDTO.setWoTypeCode(listTypeWo.get(0).getWoTypeCode());
                } else {
                  crProcessWoDTO.setWoTypeCode(obj[5].toString().trim());
                  crProcessWoDTO.setWoTypeId(0l);
                }
              }
              if (!StringUtils.isStringNullOrEmpty(obj[6])) {
                if (obj[6].toString().trim().equalsIgnoreCase(mopType[0].trim())) {
                  crProcessWoDTO.setIsRequire(0l);
                } else if (obj[6].toString().trim().equalsIgnoreCase(mopType[1].trim())) {
                  crProcessWoDTO.setIsRequire(1l);
                } else {
                  crProcessWoDTO.setIsRequire(2l);
                }
                crProcessWoDTO.setIsRequireName(obj[6].toString());
              } else {
                crProcessWoDTO.setIsRequire(0l);
              }
              if (!StringUtils.isStringNullOrEmpty(obj[7])) {
                if (obj[7].toString().trim().equalsIgnoreCase(mopType[0].trim())) {
                  crProcessWoDTO.setIsRequireCloseWo(0l);
                } else if (obj[7].toString().trim().equalsIgnoreCase(mopType[1].trim())) {
                  crProcessWoDTO.setIsRequireCloseWo(1l);
                } else {
                  crProcessWoDTO.setIsRequireCloseWo(2l);
                }
                crProcessWoDTO.setIsRequireWoName(obj[7].toString());
              } else {
                crProcessWoDTO.setIsRequireCloseWo(0l);
              }
              if (!StringUtils.isStringNullOrEmpty(obj[8])) {
                if (obj[8].toString().trim().equalsIgnoreCase(mopType[0].trim())) {
                  crProcessWoDTO.setCreateWoWhenCloseCR(0l);
                } else if (obj[8].toString().trim().equalsIgnoreCase(mopType[1].trim())) {
                  crProcessWoDTO.setCreateWoWhenCloseCR(1l);
                } else {
                  crProcessWoDTO.setCreateWoWhenCloseCR(2l);
                }
                crProcessWoDTO.setCreateWhenCloseCrName(obj[8].toString());
              } else {
                crProcessWoDTO.setCreateWoWhenCloseCR(0l);
              }

              if (validateImportSheetWo(crProcessWoDTO, lstError)) {
                if (mapAddObjectWo.get(
                    crProcessWoDTO.getCrProcessCode().trim() + crProcessWoDTO.getWoName().trim())
                    == null) {
                  mapAddObjectWo.put(
                      crProcessWoDTO.getCrProcessCode().trim() + crProcessWoDTO.getWoName().trim(),
                      crProcessWoDTO.getWoName());
                  lstAddWo.add(crProcessWoDTO);
                } else {
                  crProcessWoDTO.setResultImport(
                      I18n.getLanguage("wo.err.woTypeName.dupplicate.inFile"));
                }
              }
              lstErrorWo.add(crProcessWoDTO);
            }
          }

          boolean check = false;
          boolean checkSheetTwo = false;
          for (CrProcessInsideDTO item : lstError) {
            if (!StringUtils.isStringNullOrEmpty(item.getResultImport())) {
              check = true;
              break;
            }
          }

          for (CrProcessWoDTO item : lstErrorWo) {
            if (!StringUtils.isStringNullOrEmpty(item.getResultImport())) {
              checkSheetTwo = true;
              break;
            }
          }

          if (check || checkSheetTwo) {

            for (CrProcessInsideDTO item : lstError) {
              if (StringUtils.isStringNullOrEmpty(item.getResultImport())) {
                item.setResultImport(I18n.getLanguage("CrProcess.record.import"));
              }
            }

            for (CrProcessWoDTO item : lstErrorWo) {
              if (StringUtils.isStringNullOrEmpty(item.getResultImport())) {
                item.setResultImport(I18n.getLanguage("CrProcess.record.import"));
              }
            }

            File expFile = exportFileEx(lstError, "2", true, lstErrorWo);
            resultInSideDTO.setKey(RESULT.ERROR);
            resultInSideDTO.setFile(expFile);
          } else {
            if (!lstAddOrUpdate.isEmpty()) {
              UserToken userToken = ticketProvider.getUserToken();

              for (CrProcessInsideDTO crProcessDTO : lstAddOrUpdate) {
                resultInSideDTO = crManagerProcessRepository
                    .insertOrUpdateCrProcessDTO(crProcessDTO);
                List<LanguageExchangeDTO> listCrProcessName = new ArrayList<>();
                LanguageExchangeDTO languageExchangeDTO = new LanguageExchangeDTO();
                languageExchangeDTO.setLeeLocale(I18n.getLocale());
                languageExchangeDTO.setLeeValue(crProcessDTO.getCrProcessName());
                listCrProcessName.add(languageExchangeDTO);
                languageExchangeRepository.saveListLanguageExchange("OPEN_PM", "OPEN_PM.CR_PROCESS",
                    resultInSideDTO.getId(), listCrProcessName);

                CrProcessInsideDTO levelCheck = null;
                if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getParentId())) {
                  levelCheck = crManagerProcessRepository
                      .getCrProcessDetail(crProcessDTO.getParentId());
                }

                List<TempImportDTO> tempImportDTOList = new ArrayList<>();
                List<GroupUnitDTO> groupUnitList = new ArrayList<>();
                // if it does not have children, then update the file code, file type, unit group code
                if (!crProcessDTO.isUpdateParent()) {
                  if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getFileCode())) {
                    String[] fileCode = crProcessDTO.getFileCode().split(",");
                    String[] fileCodeType = crProcessDTO.getFileCodeTypeName().split(",");
                    for (int i = 0; i < fileCode.length; i++) {
                      List<TempImportDTO> listDataTemp = crManagerProcessRepository
                          .getListTempImportDTO(fileCode[i].trim());
                      if (listDataTemp != null && listDataTemp.size() > 0) {
                        TempImportDTO dto = listDataTemp.get(0);

                        if (!StringUtils.isStringNullOrEmpty(fileCodeType[i])) {
                          if ("1".equalsIgnoreCase(fileCodeType[i].trim())) {
                            dto.setFileType(101l);
                          }

                          if ("2".equalsIgnoreCase(fileCodeType[i].trim())) {
                            dto.setFileType(102l);
                          }
                        }
                        tempImportDTOList.add(dto);
                      }
                    }
                  } else {
                    // if add level 3
                    if (!StringUtils.isStringNullOrEmpty(levelCheck)) {
                      if (levelCheck.getCrProcessLevel() == 2) {
                        List<CrProcessTemplateDTO> template = levelCheck.getListCrProcessTemplate();
                        if (template != null) {
                          for (CrProcessTemplateDTO item : template) {
                            TempImportDTO dto = new TempImportDTO();
                            dto.setFileType(item.getFileType());
                            dto.setTempImportId(item.getTempImportId());
                            tempImportDTOList.add(dto);
                          }
                        }
                      }
                    }
                  }

                  if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getGroupUnitCode())) {
                    String[] groupCode = crProcessDTO.getGroupUnitCode().split(",");
                    for (int i = 0; i < groupCode.length; i++) {
                      GroupUnitDTO groupUnitDTO = new GroupUnitDTO();
                      groupUnitDTO.setGroupUnitCode(groupCode[i]);

                      List<GroupUnitDTO> listDataGroup = crManagerProcessRepository
                          .getGroupUnitDTO(groupUnitDTO);
                      if (listDataGroup != null && listDataGroup.size() > 0) {
                        groupUnitList.add(listDataGroup.get(0));
                      }
                    }
                  } else {
                    // if add level 3
                    if (!StringUtils.isStringNullOrEmpty(levelCheck)) {
                      if (levelCheck.getCrProcessLevel() == 2) {
                        List<CrProcessDeptGroupDTO> group = levelCheck.getListCrProcessDeptGroup();
                        if (group != null) {
                          for (CrProcessDeptGroupDTO item : group) {
                            GroupUnitDTO groupUnitDTO = new GroupUnitDTO();
                            groupUnitDTO.setGroupUnitId(item.getGroupUnitId());
                            groupUnitList.add(groupUnitDTO);
                          }
                        }
                      }
                    }
                  }

                  if (!lstAddWo.isEmpty()) {
                    crManagerProcessRepository.deleteCrProcessWo(resultInSideDTO.getId());
                    for (CrProcessWoDTO item : lstAddWo) {
                      CrProcessWoDTO crProcessWoDTO = new CrProcessWoDTO();
                      crProcessWoDTO.setCrProcessId(resultInSideDTO.getId());
                      crProcessWoDTO.setWoName(item.getWoName());
                      crProcessWoDTO.setDescription(item.getDescription());
                      crProcessWoDTO.setDurationWo(item.getDurationWo());
                      crProcessWoDTO.setWoTypeId(item.getWoTypeId());
                      crProcessWoDTO.setIsRequire(item.getIsRequire());
                      crProcessWoDTO.setIsRequireCloseWo(item.getIsRequireCloseWo());
                      crProcessWoDTO.setCreateWoWhenCloseCR(item.getCreateWoWhenCloseCR());

                      result = crManagerProcessRepository.saveCrProcessWo(crProcessWoDTO);

                      if (result != null && "SUCCESS".equals(result.getKey())) {
                        commonStreamServiceProxy
                            .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
                                "Import Data", "Import Data CrProcessWo: " + result.getId(),
                                crProcessWoDTO, null));
                      }
                    }
                  } else {
                    // if add level 3
                    if ("1".equalsIgnoreCase(crProcessDTO.getIsAdd())) {
                      if (!StringUtils.isStringNullOrEmpty(levelCheck)) {
                        if (levelCheck.getCrProcessLevel() == 2) {
                          List<CrProcessWoDTO> woLst = levelCheck.getListCrProcessWo();
                          if (woLst != null && woLst.size() > 0) {
                            for (CrProcessWoDTO item : woLst) {
                              item.setCrProcessWoId(0l);
                              item.setCrProcessId(resultInSideDTO.getId());
                              result = crManagerProcessRepository.saveCrProcessWo(item);

                              if (result != null && "SUCCESS".equals(result.getKey())) {
                                commonStreamServiceProxy
                                    .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
                                        "Import Data", "Import Data CrProcessWo: " + result.getId(),
                                        item, null));
                              }
                            }
                          }
                        }
                      }
                    }
                  }

                } else {
                  // if level 2 , group unit update
                  if (levelCheck != null) {
                    if (levelCheck.getCrProcessLevel() == 1) {
                      if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getGroupUnitCode())) {
                        String[] groupCode = crProcessDTO.getGroupUnitCode().split(",");
                        for (int i = 0; i < groupCode.length; i++) {
                          GroupUnitDTO groupUnit = new GroupUnitDTO();
                          groupUnit.setGroupUnitCode(groupCode[i]);

                          List<GroupUnitDTO> listDataGroup = crManagerProcessRepository
                              .getGroupUnitDTO(groupUnit);
                          if (listDataGroup != null && listDataGroup.size() > 0) {
                            groupUnitList.add(listDataGroup.get(0));
                          }
                        }
                      }
                    }
                  }
                }

                crManagerProcessRepository
                    .saveDataImport(resultInSideDTO.getId(), tempImportDTOList, groupUnitList);

                if (resultInSideDTO != null && "SUCCESS".equals(resultInSideDTO.getKey())) {
                  commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
                      "Import Data", "Import Data CrProcess: " + resultInSideDTO.getId(),
                      crProcessDTO, null));


                }
              }
            }
          }
        }
      }

    } catch (Exception e) {
      log.error("Exception:", e);
      resultInSideDTO = new ResultInSideDto();
      resultInSideDTO.setKey(RESULT.ERROR);
      resultInSideDTO.setMessage(e.getMessage());
    }

    return resultInSideDTO;
  }

  public boolean validateImportSheetWo(CrProcessWoDTO crProcessWoDTO,
      List<CrProcessInsideDTO> lstError) {
    crProcessWoDTO.setResultImport("");
    if (StringUtils.isStringNullOrEmpty(crProcessWoDTO.getCrProcessCode())) {
      crProcessWoDTO.setResultImport(
          crProcessWoDTO.getResultImport() + "," + I18n.getLanguage("wo.processCode.isEmpty"));
    } else {
      boolean check = false;
      for (int i = 0; i < lstError.size(); i++) {
        if (lstError.get(i).getCrProcessCode().equals(crProcessWoDTO.getCrProcessCode())) {
          check = true;
          break;
        }
      }

      if (!check) {
        crProcessWoDTO.setResultImport(
            crProcessWoDTO.getResultImport() + "," + I18n.getLanguage("wo.processCode.notFound"));
      }
    }

    if (StringUtils.isStringNullOrEmpty(crProcessWoDTO.getWoName())) {
      crProcessWoDTO.setResultImport(
          crProcessWoDTO.getResultImport() + "," + I18n.getLanguage("wo.woName.isEmpty"));
    }
    if (StringUtils.isStringNullOrEmpty(crProcessWoDTO.getDurationWoName())) {
      crProcessWoDTO.setResultImport(
          crProcessWoDTO.getResultImport() + "," + I18n.getLanguage("wo.dayWork.isEmpty"));
    } else if (crProcessWoDTO.getDurationWo() == 0) {
      crProcessWoDTO.setResultImport(
          crProcessWoDTO.getResultImport() + "," + I18n.getLanguage("wo.dayWork.notNumber"));
    }
    if (StringUtils.isStringNullOrEmpty(crProcessWoDTO.getWoTypeCode())) {
      crProcessWoDTO.setResultImport(
          crProcessWoDTO.getResultImport() + "," + I18n.getLanguage("wo.groupType.isEmpty"));
    } else if (crProcessWoDTO.getWoTypeId() == 0) {
      crProcessWoDTO.setResultImport(
          crProcessWoDTO.getResultImport() + "," + I18n.getLanguage("wo.groupType.notFound"));
    }
    if (crProcessWoDTO.getIsRequire() == 2l) {
      crProcessWoDTO.setResultImport(
          crProcessWoDTO.getResultImport() + "," + I18n.getLanguage("wo.requiredWO.notEmpty"));
    }
    if (crProcessWoDTO.getIsRequireCloseWo() == 2l) {
      crProcessWoDTO.setResultImport(
          crProcessWoDTO.getResultImport() + "," + I18n
              .getLanguage("wo.requiredWOCloseCr.notEmpty"));
    }
    if (crProcessWoDTO.getIsRequireCloseWo() == 1l) {
      if (crProcessWoDTO.getIsRequire() == 1 || crProcessWoDTO.getCreateWoWhenCloseCR() == 1) {
        crProcessWoDTO.setResultImport(
            crProcessWoDTO.getResultImport() + "," + I18n
                .getLanguage("wo.requiredWOCloseCr.notNull"));
      }
    }
    if (crProcessWoDTO.getCreateWoWhenCloseCR() == 2l) {
      crProcessWoDTO.setResultImport(
          crProcessWoDTO.getResultImport() + "," + I18n.getLanguage("wo.completeCR.notEmpty"));
    }

    if (!StringUtils.isStringNullOrEmpty(crProcessWoDTO.getCrProcessCode())) {
      CrProcessInsideDTO checkLevel = crManagerProcessRepository
          .getCrProcessLevelByCode(crProcessWoDTO.getCrProcessCode());
      if (checkLevel != null) {
        if (checkLevel.getCrProcessLevel() == 1) {
          if (crManagerProcessRepository.checkIsParent(checkLevel.getCrProcessId())) {
            // get list fileCode, groupCode
            crProcessWoDTO.setResultImport(
                crProcessWoDTO.getResultImport() + "," + I18n
                    .getLanguage("CrProcessManager.err.level1.listWo"));
          }
        }
        if (checkLevel.getCrProcessLevel() == 2) {
          if (crManagerProcessRepository.checkIsParent(checkLevel.getCrProcessId())) {
            // get list fileCode, groupCode
            CrProcessInsideDTO crProcessDTO = getCrProcessDetail(checkLevel.getCrProcessId());
            List<CrProcessWoDTO> list = crProcessDTO.getListCrProcessWo();
            if (list == null || list.size() < 0) {
              crProcessWoDTO.setResultImport(
                  crProcessWoDTO.getResultImport() + "," + I18n
                      .getLanguage("CrProcessManager.err.level2.listWo"));
            } else {
              int k = 0;
              List<String> name = new ArrayList<>();
              for (CrProcessWoDTO item : list) {
                name.add(item.getWoName());
              }

              if (!name.contains(crProcessWoDTO.getWoName())) {
                crProcessWoDTO.setResultImport(
                    crProcessWoDTO.getResultImport() + "," + I18n
                        .getLanguage("CrProcessManager.err.level2.listWo"));
              }
            }
          }
        }
        if (checkLevel.getCrProcessLevel() == 3) {
          if (crManagerProcessRepository.checkIsParent(checkLevel.getCrProcessId())) {
            // get list fileCode, groupCode
            crProcessWoDTO.setResultImport(
                crProcessWoDTO.getResultImport() + "," + I18n
                    .getLanguage("CrProcessManager.err.level3.listWo"));
          }
        }
      }
    }

    if (!StringUtils.isStringNullOrEmpty(crProcessWoDTO.getResultImport())) {
      String temp =
          crProcessWoDTO.getResultImport().startsWith(",") ? crProcessWoDTO.getResultImport()
              .substring(1) : crProcessWoDTO.getResultImport();
      crProcessWoDTO.setResultImport(temp);
      return false;
    } else {
      return true;
    }
  }

  private File exportFileEx_NoParam(List<CrProcessInsideDTO> lstCrEx, String key) throws
      Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("CrProcessManager.export.title");
    String title = I18n.getLanguage("CrProcessManager.export.title");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();

    columnSheet = new ConfigHeaderExport("crProcessCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("crProcessName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("crTypeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("riskLevel", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("impactSegmentName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("deviceTypeName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    if (Constants.RESULT_IMPORT.equals(key)) {

    } else {
      fileNameOut = CR_PROCESS_MANAGER_EXPORT;
      subTitle = I18n
          .getLanguage("CrProcessManager.export.eportDate", dateFormat.format(new Date()));
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstCrEx,
        sheetName,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.CrProcessManager",
        lstHeaderSheet,
        fieldSplit,
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


  public ConfigFileExport getConfigFileExport(List<CrProcessWoDTO> lstErr, boolean checkRs) {
    String subTitle = "";
    String sheetName = I18n.getLanguage("wo.typeWo.title");
    String title = I18n.getLanguage("wo.typeWo.title");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("crProcessCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("woName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("description", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("durationWoName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("woTypeCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("isRequireName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("isRequireWoName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("createWhenCloseCrName", "LEFT", false, 0, 0,
        new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    if (checkRs) {
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
    }

    subTitle = I18n
        .getLanguage("CrProcessManager.note4");

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstErr,
        sheetName,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.wo",
        lstHeaderSheet,
        fieldSplit,
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

    return configFileExport;

  }

  private File exportFileEx(List<CrProcessInsideDTO> lstCrEx, String key, boolean isImport,
      List<CrProcessWoDTO> lstErr) throws
      Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("CrProcessManager.export.title");
    String title = I18n.getLanguage("CrProcessManager.export.title");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("crProcessName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("crProcessCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("parentCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("crTypeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("riskLevelName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("deviceTypeCode", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("impactSegmentCode", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    if (isImport) {
      columnSheet = new ConfigHeaderExport("impactCharacteristicName", "LEFT", false, 0, 0,
          new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      columnSheet = new ConfigHeaderExport("impactTypeName", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      columnSheet = new ConfigHeaderExport("requireMopName", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      columnSheet = new ConfigHeaderExport("requireFileLogName", "LEFT", false, 0, 0,
          new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      columnSheet = new ConfigHeaderExport("fileCode", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      columnSheet = new ConfigHeaderExport("fileCodeTypeName", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      columnSheet = new ConfigHeaderExport("groupUnitCode", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      columnSheet = new ConfigHeaderExport("isAddName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      if ("2".equals(key)) {
        columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
            null,
            "STRING");
        lstHeaderSheet.add(columnSheet);
      }
    }

    if (Constants.RESULT_IMPORT.equals(key)) {

    } else {
      fileNameOut = CR_PROCESS_MANAGER_EXPORT;
      subTitle = I18n
          .getLanguage("CrProcessManager.export.eportDate", dateFormat.format(new Date()));
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstCrEx,
        sheetName,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.CrProcessManager",
        lstHeaderSheet,
        fieldSplit,
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

    if (isImport) {
      boolean checkRs = false;
      if ("2".equals(key)) {
        checkRs = true;
      }
      ConfigFileExport configFileExportTwo = getConfigFileExport(lstErr, checkRs);
      fileExports.add(configFileExportTwo);
    }

    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    List<DeviceTypesDTO> deviceList = deviceTypesRepository.getDeviceTypesCBB();
    ImpactSegmentDTO impact = new ImpactSegmentDTO();
    impact.setIsActive(1L);
    impact.setAppliedSystem(2L);
    List<ImpactSegmentDTO> impactSegmentList = crImpactSegmentRepository
        .getListImpactSegmentDTO(impact);
    GroupUnitDTO groupUnitDTO = new GroupUnitDTO();
    List<GroupUnitDTO> groupUnitList = groupUnitRepository.getAllListGroupUnitDTO(groupUnitDTO);
    GroupUnitDetailDTO groupUnitDetailDTO = new GroupUnitDetailDTO();
    List<GroupUnitDetailDTO> groupUnitDetailList = groupUnitDetailRepository
        .getAllListGroupUnitDetailDTO(groupUnitDetailDTO);

    List<TempImportDTO> tempImportList = crManagerProcessRepository.getListTempImportDTO("");

    File fileExport = exportExcelWithCb(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath,
        deviceList,
        impactSegmentList,
        groupUnitList,
        groupUnitDetailList,
        tempImportList
        , new String[]{}
    );
    return fileExport;
  }

  public boolean contains(final long[] arr, final long key) {
    return Arrays.stream(arr).anyMatch(i -> i == key);
  }

  public boolean contains(final String[] arr, final String key) {
    return Arrays.stream(arr).anyMatch(i -> i.equals(key));
  }

  private boolean validateImportInfo(CrProcessInsideDTO dto) {

    String[] crTypeName = {I18n.getLanguage("CrProcessManager.crType.CR_NORMAL"),
        I18n.getLanguage("CrProcessManager.crType.CR_EMERGENCY"),
        I18n.getLanguage("CrProcessManager.crType.CR_STANDARD")};

    String[] riskTypeName = {I18n.getLanguage("CrProcessManager.riskType.LOW"),
        I18n.getLanguage("CrProcessManager.riskType.MEDIUM"),
        I18n.getLanguage("CrProcessManager.riskType.HIGH"),
        I18n.getLanguage("CrProcessManager.riskType.VERY_HIGH")};

    String[] physicalTypeName = {I18n.getLanguage("CrProcessManager.physicalType.LOGIC"),
        I18n.getLanguage("CrProcessManager.physicalType.PHYSICAL")};

    String[] mopTypeName = {I18n.getLanguage("CrProcessManager.mopType.NO"),
        I18n.getLanguage("CrProcessManager.mopType.YES")};

    String[] logTypeName = {I18n.getLanguage("CrProcessManager.logType.NO"),
        I18n.getLanguage("CrProcessManager.logType.YES")};

    String[] fileCodeName = {I18n.getLanguage("CrProcessManager.file.INPUT"),
        I18n.getLanguage("CrProcessManager.file.OUT")};

    dto.setResultImport("");

    if (!StringUtils.isStringNullOrEmpty(dto.getCrProcessCode())) {
      if (dto.getCrProcessCode().length() > 100) {
        dto.setResultImport(
            I18n.getLanguage("CrProcessManager.err.processCode.maxlength.100"));
      }
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getCrProcessName())) {
      if (dto.getCrProcessName().length() > 1000) {
        dto.setResultImport(dto.getResultImport() + "," +
            I18n.getLanguage("CrProcessManager.err.processName.maxlength.100"));
      }
    }

    if (StringUtils.isStringNullOrEmpty(dto.getCrProcessName())) {
      dto.setResultImport(dto.getResultImport() + "," + I18n
          .getLanguage("CrProcessManager.err.processName.notEmpty"));
    }

    if (StringUtils.isStringNullOrEmpty(dto.getCrProcessCode())) {
      dto.setResultImport(dto.getResultImport() + "," + I18n
          .getLanguage("CrProcessManager.err.processCode.notEmpty"));
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getParentCode())) {
      if (StringUtils.isStringNullOrEmpty(dto.getParentId())) {
        dto.setResultImport(dto.getResultImport() + "," + I18n
            .getLanguage("CrProcessManager.err.processCode.notFoundParent"));
      }
    }

    if (StringUtils.isStringNullOrEmpty(dto.getCrTypeName())) {
      dto.setResultImport(
          dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.crType.notEmty"));
    } else if (!contains(crTypeName, dto.getCrTypeName()) && !StringUtils
        .isStringNullOrEmpty(dto.getCrTypeName())) {
      dto.setResultImport(
          dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.crType"));
    } else if (!StringUtils.isStringNullOrEmpty(dto.getCrTypeName()) && StringUtils
        .isStringNullOrEmpty(dto.getCrTypeId())) {
      dto.setResultImport(
          dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.crType"));
    }

    if (StringUtils.isStringNullOrEmpty(dto.getRiskLevelName())) {
      dto.setResultImport(
          dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.risk.notEmty"));
    } else if (!contains(riskTypeName, dto.getRiskLevelName()) && !StringUtils
        .isStringNullOrEmpty(dto.getRiskLevelName())) {
      dto.setResultImport(
          dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.risk"));
    } else if (!StringUtils.isStringNullOrEmpty(dto.getRiskLevelName()) && StringUtils
        .isStringNullOrEmpty(dto.getRiskLevel())) {
      dto.setResultImport(
          dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.risk"));
    }

    if (StringUtils.isStringNullOrEmpty(dto.getDeviceTypeCode())) {
      dto.setResultImport(dto.getResultImport() + "," + I18n
          .getLanguage("CrProcessManager.err.deviceTypesId.emty"));
    }

    if (dto.getDeviceTypeId() == 0) {
      dto.setResultImport(dto.getResultImport() + "," + I18n
          .getLanguage("CrProcessManager.err.deviceTypesId.notFound"));
    }

    if (StringUtils.isStringNullOrEmpty(dto.getImpactCharacteristicName())) {
      dto.setResultImport(
          dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.impactCharacTimer"));
    } else if (!contains(physicalTypeName, dto.getImpactCharacteristicName()) && !StringUtils
        .isStringNullOrEmpty(dto.getImpactCharacteristicName())) {
      dto.setResultImport(dto.getResultImport() + "," + I18n
          .getLanguage("CrProcessManager.err.impactCharacTimer.err"));
    }

    if (StringUtils.isStringNullOrEmpty(dto.getImpactSegmentCode())) {
      dto.setResultImport(dto.getResultImport() + "," + I18n
          .getLanguage("CrProcessManager.err.impactSegment.blank"));
    } else if (dto.getImpactSegmentId() == 0) {
      dto.setResultImport(dto.getResultImport() + "," + I18n
          .getLanguage("CrProcessManager.err.impactSegmentId.notFound"));
    }

    if (StringUtils.isStringNullOrEmpty(dto.getImpactType()) || dto.getImpactType() == 0) {
      dto.setResultImport(
          dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.impactTypeName"));
    } else {
      long value = mapImpactType.get(dto.getImpactTypeName());
      if (StringUtils.isStringNullOrEmpty(value) || value == 0) {
        dto.setResultImport(dto.getResultImport() + "," + I18n
            .getLanguage("CrProcessManager.err.impactTypeName.err"));
      }
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getRequireMopName())) {
      if (!contains(mopTypeName, dto.getRequireMopName())) {
        dto.setResultImport(
            dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.processCode.mop"));
      }
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getRequireMop())) {
      long mopArr[] = {0l, 1l};
      if (!contains(mopArr, dto.getRequireMop())) {
        dto.setResultImport(
            dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.processCode.mop"));
      }
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getRequireFileLogName())) {
      if (!contains(logTypeName, dto.getRequireFileLogName())) {
        dto.setResultImport(
            dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.processCode.log"));
      }
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getRequireFileLog())) {
      long fileLog[] = {0l, 1l};
      if (!contains(fileLog, dto.getRequireFileLog())) {
        dto.setResultImport(
            dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.err.processCode.log"));
      }
    }

    CrProcessInsideDTO result = crManagerProcessRepository.checkCrProcessExist(dto);
    CrProcessInsideDTO levelByCode = crManagerProcessRepository
        .getCrProcessLevelByCode(dto.getCrProcessCode());

    // check ma file, nhom don vi xem co ton tai hay khong
    List<TempImportDTO> tempImportDTOList;
    List<GroupUnitDTO> groupUnitList;

    if (!StringUtils.isStringNullOrEmpty(dto.getFileCode())) {
      String[] fileCode = dto.getFileCode().split(",");
      String[] fileCodeType = dto.getFileCodeTypeName().split(",");

      if (fileCode.length == 0) {
        dto.setResultImport(
            dto.getResultImport() + "," + I18n.getLanguage("TempImport.code.noExits"));
      } else {
        for (int i = 0; i < fileCode.length; i++) {
          tempImportDTOList = crManagerProcessRepository.getListTempImportDTO(fileCode[i].trim());
          if (tempImportDTOList == null || tempImportDTOList.size() < 1) {
            dto.setResultImport(
                dto.getResultImport() + "," + I18n.getLanguage("TempImport.code.noExits"));
            break;
          }
        }
      }

      if (fileCode.length != fileCodeType.length) {
        dto.setResultImport(
            dto.getResultImport() + "," + I18n.getLanguage("TempImport.fileCode.length"));
      }

      boolean check = true;
      for (int i = 0; i < fileCodeType.length; i++) {
        if ("1".equalsIgnoreCase(fileCodeType[i].trim()) || "2"
            .equalsIgnoreCase(fileCodeType[i].trim())) {
          if ("1".equalsIgnoreCase(fileCodeType[i])) {
            dto.setFileCodeType(1l);
          }
          if ("2".equalsIgnoreCase(fileCodeType[i])) {
            dto.setFileCodeType(2l);
          }
        } else {
          check = false;
          break;
        }
      }

      if (!check) {
        dto.setResultImport(
            dto.getResultImport() + "," + I18n.getLanguage("TempImport.fileType.err"));
      }

      if (duplicates(fileCode)) {
        dto.setResultImport(dto.getResultImport() + "," + I18n.getLanguage("TempImport.duplicate"));
      }

      // check level 2 have children not change file list
      if ("2".equals(dto.getIsAdd())) {
        if (levelByCode != null) {
          if (levelByCode.getCrProcessLevel() == 2) {
            CrProcessInsideDTO level2Rs = crManagerProcessRepository
                .getCrProcessDetail(levelByCode.getCrProcessId());
            boolean checkParent = crManagerProcessRepository
                .checkIsParent(level2Rs.getCrProcessId());

            List<CrProcessTemplateDTO> templateList = level2Rs.getListCrProcessTemplate();
            if (level2Rs != null) {
              if (checkParent) {
                if (templateList != null) {
                  if (templateList.size() != fileCode.length) {
                    dto.setResultImport(
                        dto.getResultImport() + "," + I18n
                            .getLanguage("CrProcessManager.err.level2.fileCode"));
                  } else {
                    List<String> list = new ArrayList<>();
                    for (CrProcessTemplateDTO item : templateList) {
                      list.add(item.getCode());
                    }

                    for (int i = 0; i < fileCode.length; i++) {
                      list.remove(fileCode[i].trim());
                    }

                    if (list.size() > 0) {
                      dto.setResultImport(
                          dto.getResultImport() + "," + I18n
                              .getLanguage("CrProcessManager.err.level2.fileCode"));
                    }
                  }
                } else {
                  dto.setResultImport(
                      dto.getResultImport() + "," + I18n
                          .getLanguage("CrProcessManager.err.level2.fileCode"));
                }
              }
            }
          }
        }
      }
    } else {
      if (levelByCode != null) {
        CrProcessInsideDTO level2Rs = crManagerProcessRepository
            .getCrProcessDetail(levelByCode.getCrProcessId());
        boolean checkParent = crManagerProcessRepository
            .checkIsParent(level2Rs.getCrProcessId());
        List<CrProcessTemplateDTO> templateList = level2Rs.getListCrProcessTemplate();
        if ("2".equals(dto.getIsAdd())) {
          if (levelByCode != null) {
            if (levelByCode.getCrProcessLevel() == 2) {
              if (level2Rs != null) {
                if (checkParent) {
                  if (templateList != null && templateList.size() > 0) {
                    dto.setResultImport(
                        dto.getResultImport() + "," + I18n
                            .getLanguage("CrProcessManager.err.level2.fileCode"));
                  }
                }
              }
            }
            if (levelByCode.getCrProcessLevel() == 3) {
              if (level2Rs != null) {
                if (checkParent) {
                  if (templateList != null) {
                    dto.setResultImport(
                        dto.getResultImport() + "," + I18n
                            .getLanguage("CrProcessManager.err.level3.listTemplate"));
                  }
                }
              }
            }

          }
        }
      }
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getGroupUnitCode())) {
      GroupUnitDTO groupUnitDTO = new GroupUnitDTO();
      String[] groupCode = dto.getGroupUnitCode().split(",");

      if (groupCode.length == 0) {
        dto.setResultImport(
            dto.getResultImport() + "," + I18n.getLanguage("TempImport.groupUnitCode.noExits"));
      } else {
        for (int i = 0; i < groupCode.length; i++) {
          groupUnitDTO.setGroupUnitCode(groupCode[i].trim());
          groupUnitList = crManagerProcessRepository.getGroupUnitDTO(groupUnitDTO);
          if (groupUnitList == null || groupUnitList.size() < 1) {
            dto.setResultImport(
                dto.getResultImport() + "," + I18n.getLanguage("TempImport.groupUnitCode.noExits"));
            break;
          }
        }

        if (duplicates(groupCode)) {
          dto.setResultImport(
              dto.getResultImport() + "," + I18n.getLanguage("GroupUnit.duplicate"));
        }
      }

      // check level 2 not change file list, group unit level 3 , excell might null
      if ("2".equals(dto.getIsAdd())) {
        if (result != null) {
          if (levelByCode.getCrProcessLevel() == 3) {
            CrProcessInsideDTO level2Rs = crManagerProcessRepository
                .getCrProcessDetail(levelByCode.getCrProcessId());
            List<CrProcessDeptGroupDTO> groupUnit = level2Rs.getListCrProcessDeptGroup();
            if (groupUnit != null) {
              if (groupUnit.size() != groupCode.length) {
                dto.setResultImport(
                    dto.getResultImport() + "," + I18n
                        .getLanguage("CrProcessManager.err.level3.groupUnit"));
              } else {
                List<String> list = new ArrayList<>();
                for (CrProcessDeptGroupDTO item : groupUnit) {
                  list.add(item.getGroupUnitCode());
                }

                for (int i = 0; i < groupCode.length; i++) {
                  list.remove(groupCode[i].trim());
                }

                if (list.size() > 0) {
                  dto.setResultImport(
                      dto.getResultImport() + "," + I18n
                          .getLanguage("CrProcessManager.err.level3.groupUnit"));
                }
              }
            } else {
              dto.setResultImport(
                  dto.getResultImport() + "," + I18n
                      .getLanguage("CrProcessManager.err.level3.groupUnit"));
            }
          }
        }
      }
    } else {
      if ("2".equals(dto.getIsAdd())) {
        CrProcessInsideDTO level2Rs = crManagerProcessRepository
            .getCrProcessDetail(levelByCode.getCrProcessId());
        boolean checkParent = crManagerProcessRepository
            .checkIsParent(level2Rs.getCrProcessId());
        List<CrProcessDeptGroupDTO> groupLst = level2Rs.getListCrProcessDeptGroup();
        if (levelByCode.getCrProcessLevel() == 3) {
          CrProcessInsideDTO group = getCrProcessDetail(levelByCode.getParentId());
          if (group != null) {
            dto.setListCrProcessDeptGroup(group.getListCrProcessDeptGroup());
          }

          if (checkParent) {
            if (groupLst != null) {
              dto.setResultImport(
                  dto.getResultImport() + "," + I18n
                      .getLanguage("CrProcessManager.err.level3.listGroup"));
            }
          }
        }
      }
    }

    if (StringUtils.isStringNullOrEmpty(dto.getIsAddName())) {
      dto.setResultImport(
          dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.isAddName.blank"));
    } else {
      if (StringUtils.isStringNullOrEmpty(dto.getIsAdd())) {
        dto.setResultImport(
            dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.isAddName.err"));
      } else {
        String isAddArr[] = {"1", "2"};
        if (!contains(isAddArr, dto.getIsAdd())) {
          dto.setResultImport(
              dto.getResultImport() + "," + I18n.getLanguage("CrProcessManager.isAddName.err"));
        }
      }
    }

    if ("1".equals(dto.getIsAdd())) {
      if (result != null) {
        dto.setResultImport(dto.getResultImport() + "," +
            I18n.getLanguage("CrProcessManager.err.processCode.dupplicate"));
      }
      checkLevel3(dto);
    } else if ("2".equals(dto.getIsAdd())) {
      if (result == null) {
        dto.setResultImport(dto.getResultImport() + "," + I18n
            .getLanguage("CrProcessManager.err.processCode.notFound"));
      } else {
        if (StringUtils.isStringNullOrEmpty((dto.getParentId()))) {
          dto.setParentId(0l);
        }
        if (StringUtils.isStringNullOrEmpty((result.getParentId()))) {
          result.setParentId(0l);
        }

        if (dto.getParentId().longValue() != result.getParentId().longValue()) {
          dto.setResultImport(dto.getResultImport() + "," + I18n
              .getLanguage("CrProcessManager.err.processCode.parentId"));
        }

        boolean checkParent = crManagerProcessRepository.checkIsParent(result.getCrProcessId());
        if (checkParent) {
          dto.setUpdateParent(true); // if true, then update the file code, file type, unit code
        }

        checkLevel1(dto);
        checkLevel2(dto);
        checkLevel3(dto);
      }
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getResultImport())) {
      String temp = dto.getResultImport().startsWith(",") ? dto.getResultImport().substring(1)
          : dto.getResultImport();
      dto.setResultImport(temp.trim());
      return false;
    } else {
      return true;
    }
  }

  public boolean duplicates(final String[] zipcodelist) {
    Set<String> lump = new HashSet<String>();
    for (String i : zipcodelist) {
      if (lump.contains(i)) {
        return true;
      }
      lump.add(i);
    }
    return false;
  }

  public void checkLevel1(CrProcessInsideDTO dto) {
    if (StringUtils.isStringNullOrEmpty(dto.getResultImport())) {
      dto.setResultImport("");
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getCrProcessId()) && dto.getCrProcessId() != 0) {
      CrProcessInsideDTO checkLevel = crManagerProcessRepository
          .getCrProcessById(dto.getCrProcessId());
      if (checkLevel.getCrProcessLevel() == 1) {
        if (crManagerProcessRepository.checkIsParent(checkLevel.getCrProcessId())) {
          // get list fileCode, groupCode
          List<CrProcessTemplateDTO> listTemplate = checkLevel.getListCrProcessTemplate();
          List<CrProcessDeptGroupDTO> listDeptGroup = checkLevel.getListCrProcessDeptGroup();
          String[] fileCode = dto.getFileCode().split(",");
          String[] groupCode = dto.getGroupUnitCode().split(",");

          if (listTemplate != null && !listTemplate.isEmpty() && (fileCode != null
              && fileCode.length > 0)) {
            if (listTemplate.size() != fileCode.length) {
              dto.setResultImport(dto.getResultImport() + "," + I18n
                  .getLanguage("CrProcessManager.err.level1.listTemplate"));
            } else {
              for (int i = 0; i < listTemplate.size(); i++) {
                if (!listTemplate.get(i).getCode().equalsIgnoreCase(fileCode[i])) {
                  dto.setResultImport(dto.getResultImport() + "," + I18n
                      .getLanguage("CrProcessManager.err.level1.listTemplate"));
                  break;
                }
              }
            }
          }

          if (listDeptGroup != null && !listDeptGroup.isEmpty() && (groupCode != null
              && groupCode.length > 0)) {
            if (listDeptGroup.size() != groupCode.length) {
              dto.setResultImport(dto.getResultImport() + "," + I18n
                  .getLanguage("CrProcessManager.err.level1.listGroup"));
            } else {
              for (int i = 0; i < listDeptGroup.size(); i++) {
                if (!listDeptGroup.get(i).getGroupUnitCode().equalsIgnoreCase(groupCode[i])) {
                  dto.setResultImport(dto.getResultImport() + "," + I18n
                      .getLanguage("CrProcessManager.err.level1.listGroup"));
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  public void checkLevel2(CrProcessInsideDTO dto) {
    if (StringUtils.isStringNullOrEmpty(dto.getResultImport())) {
      dto.setResultImport("");
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getParentId()) && dto.getParentId() != 0) {
      CrProcessInsideDTO checkLevel = crManagerProcessRepository
          .getCrProcessById(dto.getParentId());
      if (checkLevel.getCrProcessLevel() == 1) {
        if (crManagerProcessRepository.checkIsParent(checkLevel.getCrProcessId())) {
          // get list fileCode, groupCode
          List<CrProcessTemplateDTO> listTemplate = checkLevel.getListCrProcessTemplate();
          String[] fileCode = dto.getFileCode().split(",");

          if (listTemplate != null && !listTemplate.isEmpty() && (fileCode != null
              && fileCode.length > 0)) {
            if (listTemplate.size() != fileCode.length) {
              dto.setResultImport(dto.getResultImport() + "," + I18n
                  .getLanguage("CrProcessManager.err.level2.listTemplate"));
            } else {
              for (int i = 0; i < listTemplate.size(); i++) {
                if (!listTemplate.get(i).getCode().equalsIgnoreCase(fileCode[i])) {
                  dto.setResultImport(dto.getResultImport() + "," + I18n
                      .getLanguage("CrProcessManager.err.level2.listTemplate"));
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  public void checkLevel3(CrProcessInsideDTO dto) {
    if (StringUtils.isStringNullOrEmpty(dto.getResultImport())) {
      dto.setResultImport("");
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getParentId()) && dto.getParentId() != 0) {
      CrProcessInsideDTO checkLevel = crManagerProcessRepository
          .getCrProcessById(dto.getParentId());
      if (checkLevel.getCrProcessLevel() == 2) {
        validateLevel3(checkLevel, dto);
      }
    }
  }

  public void validateLevel3(CrProcessInsideDTO checkLevel, CrProcessInsideDTO dto) {
    if (!StringUtils.isStringNullOrEmpty(checkLevel.getCrTypeId())) {
      if (!checkLevel.getCrTypeId().equals(dto.getCrTypeId())) {
        dto.setResultImport(dto.getResultImport() + "," + I18n
            .getLanguage("CrProcessManager.err.level3.crTypeId"));
      }
    }
    if (!StringUtils.isStringNullOrEmpty(checkLevel.getRiskLevel())) {
      if (!checkLevel.getRiskLevel().equals(dto.getRiskLevel())) {
        dto.setResultImport(dto.getResultImport() + "," + I18n
            .getLanguage("CrProcessManager.err.level3.riskLevel"));
      }
    }
    if (!StringUtils.isStringNullOrEmpty(checkLevel.getDeviceTypeId())) {
      if (!checkLevel.getDeviceTypeId().equals(dto.getDeviceTypeId())) {
        dto.setResultImport(dto.getResultImport() + "," + I18n
            .getLanguage("CrProcessManager.err.level3.deviceTypeId"));
      }
    }
    if (!StringUtils.isStringNullOrEmpty(checkLevel.getImpactSegmentId())) {
      if (!checkLevel.getImpactSegmentId().equals(dto.getImpactSegmentId())) {
        dto.setResultImport(dto.getResultImport() + "," + I18n
            .getLanguage("CrProcessManager.err.level3.impactSegmentId"));
      }
    }
    if (!StringUtils.isStringNullOrEmpty(checkLevel.getImpactType())) {
      if (!checkLevel.getImpactType().equals(dto.getImpactType())) {
        dto.setResultImport(dto.getResultImport() + "," + I18n
            .getLanguage("CrProcessManager.err.level3.impactType"));
      }
    }
    if (!StringUtils.isStringNullOrEmpty(checkLevel.getImpactCharacteristic())) {
      if (!checkLevel.getImpactCharacteristic().equals(dto.getImpactCharacteristic())) {
        dto.setResultImport(dto.getResultImport() + "," + I18n
            .getLanguage("CrProcessManager.err.level3.impactCharacteristic"));
      }
    }

//    if (!StringUtils.isStringNullOrEmpty(checkLevel.getRequireFileLog())) {
//      if (!checkLevel.getRequireFileLog().equals(dto.getRequireFileLog())) {
//        dto.setResultImport(dto.getResultImport() + "," + I18n
//            .getLanguage("CrProcessManager.err.level3.requiredLog"));
//      }
//    }
//
//    if (!StringUtils.isStringNullOrEmpty(checkLevel.getRequireMop())) {
//      if (!checkLevel.getRequireMop().equals(dto.getRequireMop())) {
//        dto.setResultImport(dto.getResultImport() + "," + I18n
//            .getLanguage("CrProcessManager.err.level3.requiredMop"));
//      }
//    }

    // get list fileCode, groupCode
    List<CrProcessTemplateDTO> listTemplate = checkLevel.getListCrProcessTemplate();
    List<CrProcessDeptGroupDTO> listDeptGroup = checkLevel.getListCrProcessDeptGroup();

    String[] fileCode = dto.getFileCode().split(",");
    String[] groupCode = dto.getGroupUnitCode().split(",");

    if (listTemplate != null && !listTemplate.isEmpty()) {
      if (listTemplate.size() != fileCode.length) {
        dto.setResultImport(dto.getResultImport() + "," + I18n
            .getLanguage("CrProcessManager.err.level3.listTemplate"));
      } else {
        for (int i = 0; i < listTemplate.size(); i++) {
          if (!listTemplate.get(i).getCode().equalsIgnoreCase(fileCode[i])) {
            dto.setResultImport(dto.getResultImport() + "," + I18n
                .getLanguage("CrProcessManager.err.level3.listTemplate"));
            break;
          }
        }
      }
    }

    if (listDeptGroup != null && !listDeptGroup.isEmpty()) {
      if (listDeptGroup.size() != groupCode.length) {
        dto.setResultImport(dto.getResultImport() + "," + I18n
            .getLanguage("CrProcessManager.err.level3.listGroup"));
      } else {
        for (int i = 0; i < listDeptGroup.size(); i++) {
          if (!listDeptGroup.get(i).getGroupUnitCode().equalsIgnoreCase(groupCode[i])) {
            dto.setResultImport(dto.getResultImport() + "," + I18n
                .getLanguage("CrProcessManager.err.level3.listGroup"));
            break;
          }
        }
      }
    }
  }

  @Override
  public File getTemplate() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    org.springframework.core.io.Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workBook.getSheetAt(0);
    XSSFSheet sheetParam = workBook.createSheet("param");
    String[] header = new String[]{I18n.getLanguage("CrProcessManager.stt"),
        I18n.getLanguage("CrProcessManager.crProcessName"),
        I18n.getLanguage("CrProcessManager.crProcessCode"),
        I18n.getLanguage("CrProcessManager.parentId"),
        I18n.getLanguage("CrProcessManager.crTypeId"),
        I18n.getLanguage("CrProcessManager.riskLevel"),
        I18n.getLanguage("CrProcessManager.deviceTypeId"),
        I18n.getLanguage("CrProcessManager.impactSegmentId"),
        I18n.getLanguage("CrProcessManager.Time"),
        I18n.getLanguage("CrProcessManager.impactType"),
        I18n.getLanguage("CrProcessManager.requireMop"),
        I18n.getLanguage("CrProcessManager.requireLog"),
        I18n.getLanguage("CrProcessManager.fileCode"),
        I18n.getLanguage("CrProcessManager.fileType"),
        I18n.getLanguage("CrProcessManager.groupUnitCode"),
        I18n.getLanguage("CrProcessManager.isAdd")
    };

    String[] headerStar = new String[]{
        I18n.getLanguage("CrProcessManager.crProcessName"),
        I18n.getLanguage("CrProcessManager.crProcessCode"),
        I18n.getLanguage("CrProcessManager.crTypeId"),
        I18n.getLanguage("CrProcessManager.riskLevel"),
        I18n.getLanguage("CrProcessManager.deviceTypeId"),
        I18n.getLanguage("CrProcessManager.impactSegmentId"),
        I18n.getLanguage("CrProcessManager.impactCharacteristicName"),
        I18n.getLanguage("CrProcessManager.impactType"),
        I18n.getLanguage("CrProcessManager.isAdd")
    };
    //XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);
    //Tao tieu de
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(0);
    titleRow.setHeightInPoints(45);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("CrProcessManager.import.title"));
    titleCell.setCellStyle(styles.get("title"));

    //Tao note
    sheetOne.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeader.size() - 1));
    Row noteRow = sheetOne.createRow(1);
    noteRow.setHeightInPoints(18);
    Cell noteCell = noteRow.createCell(0);
    noteCell.setCellValue(I18n.getLanguage("importfile.template.excel.note"));
    noteCell.setCellStyle(styles.get("note"));
    CellStyle cellStyle = noteRow.getSheet().getWorkbook().createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.CENTER);

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetOne.createRow(2);
    headerRow.setHeightInPoints(18);

    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString xssfRichTextString = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeader : listHeaderStar) {
        if (checkHeader.equalsIgnoreCase(listHeader.get(i))) {
          xssfRichTextString.append("(*)", fontStar);
        }
      }
      headerCell.setCellValue(xssfRichTextString);
      headerCell.setCellStyle(styles.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }

    sheetOne.setColumnWidth(0, 3000);
    workBook.setSheetName(0, I18n.getLanguage("CrProcessManager.import.title"));
    workBook.setSheetHidden(1, true);
    sheetParam.setSelected(false);
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_CR_PROCESS" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;

  }

  @Override
  public CrProcessInsideDTO findCrProcess(CrProcessInsideDTO crProcessDTO) {
    return crManagerProcessRepository.findCrProcess(crProcessDTO);
  }

  @Override
  public CrProcessInsideDTO generateCrProcessCode(CrProcessInsideDTO crProcessDTO) {
    return crManagerProcessRepository.generateCrProcessCode(crProcessDTO);
  }

  public File exportExcelWithCb(
      String pathTemplate,
      String fileNameOut,
      List<ConfigFileExport> config,
      String pathOut,
      List<DeviceTypesDTO> deviceList, List<ImpactSegmentDTO> impactNodeList,
      List<GroupUnitDTO> groupUnitList, List<GroupUnitDetailDTO> groupUnitDetailList,
      List<TempImportDTO> tempImportList,
      String... exportChart
  ) throws Exception {
    File folderOut = new File(pathOut);
    if (!folderOut.exists()) {
      folderOut.mkdir();
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
    String strCurTimeExp = dateFormat.format(new Date());
    strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
    pathOut = pathOut + fileNameOut + strCurTimeExp + (exportChart != null && exportChart.length > 0
        ? XLSM_FILE_EXTENTION : XLSX_FILE_EXTENTION);
    try {
      log.info("Start get template file!");
      pathTemplate = StringUtils.removeSeparator(pathTemplate);
      Resource resource = new ClassPathResource(pathTemplate);
      InputStream fileTemplate = resource.getInputStream();
      XSSFWorkbook workbook_temp = new XSSFWorkbook(fileTemplate);
      log.info("End get template file!");
      SXSSFWorkbook workbook = new SXSSFWorkbook(workbook_temp, 1000);

      //<editor-fold defaultstate="collapsed" desc="Declare style">
      CellStyle cellStyleFormatNumber = workbook.createCellStyle();
      cellStyleFormatNumber.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      cellStyleFormatNumber.setAlignment(HorizontalAlignment.RIGHT);
      cellStyleFormatNumber.setBorderLeft(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderBottom(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderRight(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderTop(BorderStyle.THIN);

      CellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setAlignment(HorizontalAlignment.CENTER);
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle.setWrapText(true);

      Font xSSFFont = workbook.createFont();
      xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
      xSSFFont.setFontHeightInPoints((short) 20);
      xSSFFont.setBold(true);
      xSSFFont.setColor(IndexedColors.BLACK.index);

      CellStyle cellStyleTitle = workbook.createCellStyle();
      cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
      cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
      cellStyleTitle.setFont(xSSFFont);

      Font xSSFFontHeader = workbook.createFont();
      xSSFFontHeader.setFontName("Times New Roman");
      xSSFFontHeader.setFontHeightInPoints((short) 10);
      xSSFFontHeader.setColor(IndexedColors.BLUE.index);
      xSSFFontHeader.setBold(true);

      Font subTitleFont = workbook.createFont();
      subTitleFont.setFontName(HSSFFont.FONT_ARIAL);
      subTitleFont.setFontHeightInPoints((short) 10);
      subTitleFont.setColor(IndexedColors.BLACK.index);

      Font xssFontTopHeader = workbook.createFont();
      xssFontTopHeader.setFontName("Times New Roman");
      xssFontTopHeader.setFontHeightInPoints((short) 10);
      xssFontTopHeader.setColor(IndexedColors.BLACK.index);
      xssFontTopHeader.setBold(true);

      Font rowDataFont = workbook.createFont();
      rowDataFont.setFontName(HSSFFont.FONT_ARIAL);
      rowDataFont.setFontHeightInPoints((short) 10);
      rowDataFont.setColor(IndexedColors.BLACK.index);

      CellStyle cellStyleTopHeader = workbook.createCellStyle();
      cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
      cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTopHeader.setFont(xssFontTopHeader);

      CellStyle cellStyleTopRightHeader = workbook.createCellStyle();
      cellStyleTopRightHeader.setAlignment(HorizontalAlignment.LEFT);
      cellStyleTopRightHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTopRightHeader.setFont(xssFontTopHeader);

      CellStyle cellStyleSubTitle = workbook.createCellStyle();
      cellStyleSubTitle.setAlignment(HorizontalAlignment.CENTER);
      cellStyleSubTitle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleSubTitle.setFont(subTitleFont);

      CellStyle cellStyleHeader = workbook.createCellStyle();
      cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
      cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleHeader.setBorderLeft(BorderStyle.THIN);
      cellStyleHeader.setBorderBottom(BorderStyle.THIN);
      cellStyleHeader.setBorderRight(BorderStyle.THIN);
      cellStyleHeader.setBorderTop(BorderStyle.THIN);
      cellStyleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyleHeader.setWrapText(true);
      cellStyleHeader.setFont(xSSFFontHeader);

      CellStyle cellStyleLeft = workbook.createCellStyle();
      cellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
      cellStyleLeft.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleLeft.setBorderLeft(BorderStyle.THIN);
      cellStyleLeft.setBorderBottom(BorderStyle.THIN);
      cellStyleLeft.setBorderRight(BorderStyle.THIN);
      cellStyleLeft.setBorderTop(BorderStyle.THIN);
      cellStyleLeft.setWrapText(true);
      cellStyleLeft.setFont(rowDataFont);
      CellStyle cellStyleRight = workbook.createCellStyle();
      cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
      cellStyleRight.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleRight.setBorderLeft(BorderStyle.THIN);
      cellStyleRight.setBorderBottom(BorderStyle.THIN);
      cellStyleRight.setBorderRight(BorderStyle.THIN);
      cellStyleRight.setBorderTop(BorderStyle.THIN);
      cellStyleRight.setWrapText(true);
      cellStyleRight.setFont(rowDataFont);
      //gnoc_cr
      CellStyle cellStyleHeaderOver = workbook.createCellStyle();
      cellStyleHeaderOver.setAlignment(HorizontalAlignment.LEFT);
      cellStyleHeaderOver.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleHeaderOver.setBorderLeft(BorderStyle.THIN);
      cellStyleHeaderOver.setBorderBottom(BorderStyle.THIN);
      cellStyleHeaderOver.setBorderRight(BorderStyle.THIN);
      cellStyleHeaderOver.setBorderTop(BorderStyle.THIN);
      cellStyleHeaderOver.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyleHeaderOver.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyleHeaderOver.setWrapText(true);
      cellStyleHeaderOver.setFont(xSSFFontHeader);

      CellStyle cellStyleCenter = workbook.createCellStyle();
      cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
      cellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleCenter.setBorderLeft(BorderStyle.THIN);
      cellStyleCenter.setBorderBottom(BorderStyle.THIN);
      cellStyleCenter.setBorderRight(BorderStyle.THIN);
      cellStyleCenter.setBorderTop(BorderStyle.THIN);
      cellStyleCenter.setWrapText(true);
      cellStyleRight.setFont(rowDataFont);

      CellStyle right = workbook.createCellStyle();
      right.setAlignment(HorizontalAlignment.RIGHT);
      right.setVerticalAlignment(VerticalAlignment.CENTER);
      right.setWrapText(true);

      CellStyle left = workbook.createCellStyle();
      left.setAlignment(HorizontalAlignment.LEFT);
      left.setVerticalAlignment(VerticalAlignment.CENTER);
      left.setWrapText(true);

      CellStyle center = workbook.createCellStyle();
      center.setAlignment(HorizontalAlignment.CENTER);
      center.setVerticalAlignment(VerticalAlignment.CENTER);
      center.setWrapText(true);
      //</editor-fold>

      CellStyle top = workbook.createCellStyle();
      top.setAlignment(HorizontalAlignment.LEFT);
      top.setVerticalAlignment(VerticalAlignment.TOP);
      top.setWrapText(true);
      top.setFillForegroundColor(IndexedColors.YELLOW.index);
      top.setFillBackgroundColor(IndexedColors.YELLOW.index);
      top.setFont(xSSFFontHeader);
      int check = 0;
      for (ConfigFileExport item : config) {

        Map<String, String> fieldSplit = item.getFieldSplit();
        SXSSFSheet sheet;
        if (exportChart != null && exportChart.length > 0) {
          sheet = workbook.getSheetAt(0);
        } else {
          sheet = workbook.createSheet(item.getSheetName());
        }

        if (item.getCellTitleIndex() >= 3) {
          //TienNV them template header
          Row headerFirstTitle = sheet.createRow(0);
          Row headerSecondTitle = sheet.createRow(1);
          int sizeheader = 5;
          Cell firstLeftHeader = headerFirstTitle.createCell(1);
          firstLeftHeader.setCellStyle(cellStyleTopHeader);
          Cell secondLeftHeader = headerSecondTitle.createCell(1);
          secondLeftHeader.setCellStyle(cellStyleTopHeader);
          Cell firstRightHeader = headerFirstTitle.createCell(sizeheader - 1);
          firstRightHeader.setCellStyle(cellStyleTopRightHeader);
          Cell secondRightHeader = headerSecondTitle.createCell(sizeheader - 1);
          secondRightHeader.setCellStyle(cellStyleTopRightHeader);
          firstLeftHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getFirstLeftHeaderTitle()) ? ""
                  : item.getFirstLeftHeaderTitle());
          secondLeftHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getSecondLeftHeaderTitle()) ? ""
                  : item.getSecondLeftHeaderTitle());
          firstRightHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getFirstRightHeaderTitle()) ? ""
                  : item.getFirstRightHeaderTitle());
          secondRightHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getSecondRightHeaderTitle()) ? ""
                  : item.getSecondRightHeaderTitle());
          sheet.addMergedRegion(new CellRangeAddress(0, 0, 1,
              2));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, 1,
              2));
          sheet.addMergedRegion(new CellRangeAddress(0, 0, sizeheader - 1,
              sizeheader));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, sizeheader - 1,
              sizeheader));
          //end tiennv
        }

        // Title
        Row rowMainTitle = sheet.createRow(item.getCellTitleIndex());
        Cell mainCellTitle = rowMainTitle.createCell(1);
        mainCellTitle.setCellValue(item.getTitle() == null ? "" : item.getTitle());
        mainCellTitle.setCellStyle(cellStyleTitle);
        sheet.addMergedRegion(
            new CellRangeAddress(item.getCellTitleIndex(), item.getCellTitleIndex(), 1,
                item.getMergeTitleEndIndex()));

        // Sub title
        int indexSubTitle =
            (StringUtils.isStringNullOrEmpty(item.getSubTitle())) ? item.getCellTitleIndex() + 1
                : item.getCellTitleIndex() + 2;
        Row rowSubTitle = sheet.createRow(indexSubTitle);
        Cell cellTitle = rowSubTitle.createCell(1);
        cellTitle.setCellValue(item.getSubTitle() == null ? "" : item.getSubTitle());
        cellTitle.setCellStyle(cellStyleSubTitle);
        sheet.addMergedRegion(
            new CellRangeAddress(indexSubTitle, indexSubTitle, 1,
                item.getMergeTitleEndIndex()));

        String[] headerStar = new String[]{
            I18n.getLanguage("CrProcessManager.crProcessName"),
            I18n.getLanguage("CrProcessManager.crProcessCode"),
            I18n.getLanguage("CrProcessManager.crTypeName"),
            I18n.getLanguage("CrProcessManager.riskLevelName"),
            I18n.getLanguage("CrProcessManager.deviceTypeCode"),
            I18n.getLanguage("CrProcessManager.impactSegmentCode"),
            I18n.getLanguage("CrProcessManager.impactCharacteristicName"),
            I18n.getLanguage("CrProcessManager.impactTypeName"),
            I18n.getLanguage("CrProcessManager.isAdd")
        };

        String[] commentHeader = new String[]{
            I18n.getLanguage("CrProcessManager.crTypeCm"),
            I18n.getLanguage("CrProcessManager.riskLevelCm"),
            I18n.getLanguage("CrProcessManager.impactTypeCm"),
            I18n.getLanguage("CrProcessManager.TimeCm"),
            I18n.getLanguage("CrProcessManager.requireMopCm"),
            I18n.getLanguage("CrProcessManager.requireFileLogCm"),
            I18n.getLanguage("CrProcessManager.fileCodeCm"),
            I18n.getLanguage("CrProcessManager.fileTypeCm"),
            I18n.getLanguage("CrProcessManager.actionCm")
        };

        Font fontStar = workbook.createFont();
        fontStar.setColor(IndexedColors.YELLOW.getIndex());
        fontStar.setFontName(HSSFFont.FONT_ARIAL);
        fontStar.setFontHeightInPoints((short) 20);
        fontStar.setBold(true);

        if (check == 0) {
          Row noteRow = sheet.createRow(4);
          noteRow.setHeightInPoints(14);
          Cell noteCell1 = noteRow.createCell(1);
          noteCell1.setCellValue(I18n.getLanguage("CrProcessManager.note1"));
          noteCell1.setCellStyle(top);
          Row abc = sheet.createRow(5);
          abc.setHeightInPoints(14);
          Cell noteCell2 = abc.createCell(1);
          noteCell2.setCellValue(I18n.getLanguage("CrProcessManager.note2"));
          noteCell2.setCellStyle(top);
          Row def = sheet.createRow(6);
          def.setHeightInPoints(14);
          Cell noteCell3 = def.createCell(1);
          noteCell3.setCellValue(I18n.getLanguage("CrProcessManager.note3"));
          noteCell3.setCellStyle(top);
        } else if (check == 1) {
          Row ghx = sheet.createRow(5);
          ghx.setHeightInPoints(14);
          Cell noteCell4 = ghx.createCell(1);
          noteCell4.setCellValue(I18n.getLanguage("CrProcessManager.note4"));
          noteCell4.setCellStyle(top);
        }

        int indexRowData = 0;
        //<editor-fold defaultstate="collapsed" desc="Build header">
        if (item.isCreatHeader()) {
          int index = -1;
          Cell cellHeader = null;
          Row rowHeader = sheet.createRow(item.getStartRow());
          rowHeader.setHeight((short) 500);
          Row rowHeaderSub = null;

          for (ConfigHeaderExport header : item.getHeader()) {
            if (fieldSplit != null) {
              if (fieldSplit.get(header.getFieldName()) != null) {
                String[] fieldSplitHead = fieldSplit.get(header.getFieldName())
                    .split(item.getSplitChar());
                for (String field : fieldSplitHead) {
                  cellHeader = rowHeader.createCell(index + 2);
                  cellHeader.setCellValue(field == null ? "" : field.replaceAll("\\<.*?>", " "));
                  if (header.isHasMerge()) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(
                        item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                        index + 2, index + 2 + header.getMergeColumn());
                    sheet.addMergedRegion(cellRangeAddress);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress,
                        sheet);

                    if (header.getMergeRow() > 0) {
                      indexRowData = header.getMergeRow();
                    }
                    if (header.getMergeColumn() > 0) {
                      index++;
                    }

                    if (header.getSubHeader().length > 0) {
                      if (rowHeaderSub == null) {
                        rowHeaderSub = sheet.createRow(item.getStartRow() + 1);
                      }

                      int k = index + 1;
                      int s = 0;
                      for (String sub : header.getSubHeader()) {
                        Cell cellHeaderSub1 = rowHeaderSub.createCell(k);
                        cellHeaderSub1.setCellValue(
                            I18n.getString(item.getHeaderPrefix() + "." + sub));
                        cellHeaderSub1.setCellStyle(cellStyleHeader);

                        k++;
                        s++;
                      }
                    }
                  }
                  cellHeader.setCellStyle(cellStyleHeader);
                  index++;
                }
              } else {
                cellHeader = rowHeader.createCell(index + 2);
                cellHeader.setCellValue(
                    I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
                if (header.isHasMerge()) {
                  CellRangeAddress cellRangeAddress = new CellRangeAddress(
                      item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                      index + 2, index + 2 + header.getMergeColumn());
                  sheet.addMergedRegion(cellRangeAddress);
                  RegionUtil
                      .setBorderBottom(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderLeft(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderRight(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);

                  if (header.getMergeRow() > 0) {
                    indexRowData = header.getMergeRow();
                  }
                  if (header.getMergeColumn() > 0) {
                    index++;
                  }
                }
                cellHeader.setCellStyle(cellStyleHeader);
                index++;
              }
            } else {
              cellHeader = rowHeader.createCell(index + 2);
              cellHeader.setCellValue(
                  I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
              if (header.isHasMerge()) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(item.getStartRow(),
                    item.getStartRow() + header.getMergeRow(), index + 2,
                    index + 2 + header.getMergeColumn());
                sheet.addMergedRegion(cellRangeAddress);
                RegionUtil
                    .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);

                if (header.getMergeRow() > 0) {
                  indexRowData = header.getMergeRow();
                }
                if (header.getMergeColumn() > 0) {
                  index++;
                }
              }
              cellHeader.setCellStyle(cellStyleHeader);
              index++;
            }

            SXSSFDrawing drawing = sheet.createDrawingPatriarch();
            if (cellHeader.getStringCellValue()
                .equalsIgnoreCase(I18n.getLanguage("CrProcessManager.crTypeId"))) {
              XSSFClientAnchor anchor = (XSSFClientAnchor) drawing
                  .createAnchor(0, 0, 0, 0, 4, 7, 7, 8);
              XSSFComment comment = (XSSFComment) drawing.createCellComment(anchor);
              comment.setString(commentHeader[0]);
              cellHeader.setCellComment(comment);
            }
            if (cellHeader.getStringCellValue()
                .equalsIgnoreCase(I18n.getLanguage("CrProcessManager.riskLevel"))) {
              XSSFClientAnchor anchor = (XSSFClientAnchor) drawing
                  .createAnchor(0, 0, 0, 0, 5, 7, 8, 8);
              XSSFComment comment = (XSSFComment) drawing.createCellComment(anchor);
              comment.setString(commentHeader[1]);

              cellHeader.setCellComment(comment);
            }
            if (cellHeader.getStringCellValue()
                .equalsIgnoreCase(I18n.getLanguage("CrProcessManager.impactCharacteristicName"))) {
              XSSFClientAnchor anchor = (XSSFClientAnchor) drawing
                  .createAnchor(0, 0, 0, 0, 8, 7, 10, 8);
              XSSFComment comment = (XSSFComment) drawing.createCellComment(anchor);
              comment.setString(commentHeader[2]);
              cellHeader.setCellComment(comment);
            }
            if (cellHeader.getStringCellValue()
                .equalsIgnoreCase(I18n.getLanguage("CrProcessManager.impactType"))) {
              XSSFClientAnchor anchor = (XSSFClientAnchor) drawing
                  .createAnchor(0, 0, 0, 0, 9, 7, 11, 8);
              XSSFComment comment = (XSSFComment) drawing.createCellComment(anchor);
              comment.setString(commentHeader[3]);
              cellHeader.setCellComment(comment);
            }
            if (cellHeader.getStringCellValue()
                .equalsIgnoreCase(I18n.getLanguage("CrProcessManager.requireMop"))) {
              XSSFClientAnchor anchor = (XSSFClientAnchor) drawing
                  .createAnchor(0, 0, 0, 0, 10, 7, 12, 8);
              XSSFComment comment = (XSSFComment) drawing.createCellComment(anchor);
              comment.setString(commentHeader[4]);
              cellHeader.setCellComment(comment);
            }
            if (cellHeader.getStringCellValue()
                .equalsIgnoreCase(I18n.getLanguage("CrProcessManager.requireFileLog"))) {
              XSSFClientAnchor anchor = (XSSFClientAnchor) drawing
                  .createAnchor(0, 0, 0, 0, 11, 7, 13, 8);
              XSSFComment comment = (XSSFComment) drawing.createCellComment(anchor);
              comment.setString(commentHeader[5]);
              cellHeader.setCellComment(comment);
            }
            if (cellHeader.getStringCellValue()
                .equalsIgnoreCase(I18n.getLanguage("CrProcessManager.fileCode"))) {
              XSSFClientAnchor anchor = (XSSFClientAnchor) drawing
                  .createAnchor(0, 0, 0, 0, 12, 7, 14, 8);
              XSSFComment comment = (XSSFComment) drawing.createCellComment(anchor);
              comment.setString(commentHeader[6]);
              cellHeader.setCellComment(comment);
            }
            if (cellHeader.getStringCellValue()
                .equalsIgnoreCase(I18n.getLanguage("CrProcessManager.fileType"))) {
              XSSFClientAnchor anchor = (XSSFClientAnchor) drawing
                  .createAnchor(0, 0, 0, 0, 13, 7, 15, 8);
              XSSFComment comment = (XSSFComment) drawing.createCellComment(anchor);
              comment.setString(commentHeader[7]);
              cellHeader.setCellComment(comment);
            }
            if (cellHeader.getStringCellValue()
                .equalsIgnoreCase(I18n.getLanguage("CrProcessManager.isAdd"))) {
              XSSFClientAnchor anchor = (XSSFClientAnchor) drawing
                  .createAnchor(0, 0, 0, 0, 15, 7, 17, 8);
              XSSFComment comment = (XSSFComment) drawing.createCellComment(anchor);
              comment.setString(commentHeader[8]);
              cellHeader.setCellComment(comment);
            }

            for (int i = 0; i < headerStar.length; i++) {
              XSSFRichTextString xssfRichTextString = new XSSFRichTextString(
                  cellHeader.getStringCellValue());
              if (cellHeader.getStringCellValue().equalsIgnoreCase(headerStar[i])) {
                xssfRichTextString.append(" (*) ", (XSSFFont) fontStar);
                cellHeader.setCellStyle(cellStyleHeader);
                cellHeader.setCellValue(xssfRichTextString);
                break;
              }
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Build other cell">
        if (item.getLstCreatCell() != null) {
          Row row;
          for (CellConfigExport cell : item.getLstCreatCell()) {
            row = sheet.getRow(cell.getRow());
            if (row == null) {
              row = sheet.createRow(cell.getRow());
            }
            //row.setHeight((short) -1);
            Cell newCell = row.createCell(cell.getColumn());
            if ("NUMBER".equals(cell.getStyleFormat())) {
              newCell.setCellValue(Double.valueOf(cell.getValue()));
            } else {
              newCell.setCellValue(cell.getValue() == null ? "" : cell.getValue());
            }

            if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
              CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                  cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                  cell.getColumn() + cell.getColumnMerge());
              sheet.addMergedRegion(cellRangeAddress);
              RegionUtil
                  .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            }

            if ("HEAD".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleHeader);
            }
            if ("CENTER".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleCenter);
            }
            if ("LEFT".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleLeft);
            }
            if ("RIGHT".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleRight);
            }
            if ("CENTER_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(center);
            }
            if ("LEFT_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(left);
            }
            if ("RIGHT_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(right);
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Fill data">
        if (item.getLstData() != null && !item.getLstData().isEmpty()) {
          //init mapColumn
          Object firstRow = item.getLstData().get(0);
          Map<String, Field> mapField = new HashMap<String, Field>();
          for (ConfigHeaderExport header : item.getHeader()) {
            for (Field f : firstRow.getClass().getDeclaredFields()) {
              f.setAccessible(true);
              if (f.getName().equals(header.getFieldName())) {
                mapField.put(header.getFieldName(), f);
              }
              String[] replace = header.getReplace();
              if (replace != null) {
                if (replace.length > 2) {
                  for (int n = 2; n < replace.length; n++) {
                    if (f.getName().equals(replace[n])) {
                      mapField.put(replace[n], f);
                    }
                  }
                }
              }
            }
            if (firstRow.getClass().getSuperclass() != null) {
              for (Field f : firstRow.getClass()
                  .getSuperclass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getName().equals(header.getFieldName())) {
                  mapField.put(header.getFieldName(), f);
                }
                String[] replace = header.getReplace();
                if (replace != null) {
                  if (replace.length > 2) {
                    for (int n = 2; n < replace.length; n++) {
                      if (f.getName().equals(replace[n])) {
                        mapField.put(replace[n], f);
                      }
                    }
                  }
                }
              }
            }
          }

          //fillData
          Row row;
          List lstData = item.getLstData();
          List<ConfigHeaderExport> lstHeader = item.getHeader();
          int startRow = item.getStartRow();
          String splitChar = item.getSplitChar();
          for (int i = 0; i < lstData.size(); i++) {
            row = sheet.createRow(i + startRow + 1 + indexRowData);
            row.setHeight((short) 250);
            //row.setHeight((short) -1);
            Cell cell;

            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell.setCellStyle(cellStyleCenter);

            int j = 0;
            for (int e = 0; e < lstHeader.size(); e++) {
              ConfigHeaderExport head = lstHeader.get(e);
              String header = head.getFieldName();
              String align = head.getAlign();
              Object obj = lstData.get(i);

              Field f = mapField.get(header);

              if (fieldSplit != null && fieldSplit.containsKey(header)) {
                String[] arrHead = fieldSplit.get(header).split(splitChar);
                String value = "";
                Object tempValue = f.get(obj);
                if (tempValue instanceof Date) {
                  value = tempValue == null ? "" : DateUtil.date2ddMMyyyyHHMMss((Date) tempValue);
                } else {
                  value = tempValue == null ? "" : tempValue.toString();
                }

                String[] fieldSplitValue = value.split(splitChar);
                for (int m = 0; m < arrHead.length; m++) {
                  if (head.isHasMerge() && head.getSubHeader().length > 0) {
                    int s = 0;
                    for (String sub : head.getSubHeader()) {
                      cell = row.createCell(j + 1);
                      String[] replace = head.getReplace();
                      if (replace != null) {
                        List<String> more = new ArrayList<>();
                        if (replace.length > 2) {
                          for (int n = 2; n < replace.length; n++) {
                            Object objStr = mapField.get(replace[n]).get(obj);
                            String valueStr = objStr == null ? "" : objStr.toString();
                            more.add(valueStr);
                          }
                        }
                        if ("NUMBER".equals(head.getStyleFormat())) {
                          double numberValue = CommonExport.replaceNumberValue(replace[0], m,
                              more, s);
                          if (Double.compare(numberValue, -888) == 0) {
                            cell.setCellValue("*");
                          } else if (Double.compare(numberValue, -999) == 0) {
                            cell.setCellValue("-");
                          } else {
                            cell.setCellValue(numberValue);
                          }
                        } else {
                          cell.setCellValue(
                              CommonExport.replaceStringValue(replace[0], m, more, s));
                        }
                        s++;
                      } else {
                        String subValue = "";
                        for (Field subf : firstRow.getClass().getDeclaredFields()) {
                          subf.setAccessible(true);
                          if (subf.getName().equals(sub)) {
                            String[] arrSub = (subf.get(obj) == null ? "" : subf.get(
                                obj).toString()).split(item.getSplitChar());
                            subValue = arrSub[m];
                          }
                        }
                        if ("NUMBER".equals(head.getStyleFormat())) {
                          if (StringUtils.isNotNullOrEmpty(subValue)) {
                            cell.setCellValue(Double.valueOf(subValue));
                          } else {
                            cell.setCellValue(subValue == null ? "" : subValue);
                          }
                        } else {
                          cell.setCellValue(subValue == null ? "" : subValue);
                        }
                      }

                      if ("CENTER".equals(align)) {
                        cell.setCellStyle(cellStyleCenter);
                      }
                      if ("LEFT".equals(align)) {
                        cell.setCellStyle(cellStyleLeft);
                      }
                      if ("RIGHT".equals(align)) {
                        cell.setCellStyle(cellStyleRight);
                      }
                      j++;
                    }
                  } else {
                    cell = row.createCell(j + 1);

                    String[] replace = head.getReplace();
                    if (replace != null) {
                      Object valueReplace = mapField.get(replace[1]).get(obj);
                      List<String> more = new ArrayList<>();
                      if (replace.length > 2) {
                        for (int n = 2; n < replace.length; n++) {
                          Object objStr = mapField.get(replace[n]).get(obj);
                          String valueStr = objStr == null ? "" : objStr.toString();
                          more.add(valueStr);
                        }
                      }
                      if ("NUMBER".equals(head.getStyleFormat())) {
                        double numberValue = CommonExport.replaceNumberValue(replace[0],
                            valueReplace, more, m);
                        if (Double.compare(numberValue, -888) == 0) {
                          cell.setCellValue("*");
                        } else if (Double.compare(numberValue, -999) == 0) {
                          cell.setCellValue("-");
                        } else {
                          cell.setCellValue(numberValue);
                        }
                      } else {
                        cell.setCellValue(
                            CommonExport.replaceStringValue(replace[0], valueReplace, more, m));
                      }
                    } else {
                      if ("NUMBER".equals(head.getStyleFormat())) {
                        if (StringUtils.isNotNullOrEmpty(fieldSplitValue[m])) {
                          cell.setCellValue(Double.valueOf(fieldSplitValue[m]));
                        } else {
                          cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                        }
                      } else {
                        cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                      }
                    }

                    if ("CENTER".equals(align)) {
                      cell.setCellStyle(cellStyleCenter);
                    }
                    if ("LEFT".equals(align)) {
                      cell.setCellStyle(cellStyleLeft);
                    }
                    if ("RIGHT".equals(align)) {
                      cell.setCellStyle(cellStyleRight);
                    }
                    j++;
                  }
                }
              } else {
                String value = "";
                if (f != null) {
                  Object tempValue = f.get(obj);
                  if (tempValue instanceof Date) {
                    value = tempValue == null ? "" : DateUtil.date2ddMMyyyyHHMMss((Date) tempValue);
                  } else {
                    value = tempValue == null ? "" : tempValue.toString();
                  }
                }
                cell = row.createCell(j + 1);

                String[] replace = head.getReplace();
                if (replace != null) {
                  Object valueReplace = mapField.get(replace[1]).get(obj);
                  List<String> more = new ArrayList<>();
                  if (replace.length > 2) {
                    for (int n = 2; n < replace.length; n++) {
                      Object objStr = mapField.get(replace[n]).get(obj);
                      String valueStr = objStr == null ? "" : objStr.toString();
                      more.add(valueStr);
                    }
                  }
                  if ("NUMBER".equals(head.getStyleFormat())) {
                    double numberValue = CommonExport
                        .replaceNumberValue(replace[0], valueReplace, more,
                            i);
                    if (Double.compare(numberValue, -888) == 0) {
                      cell.setCellValue("*");
                    } else if (Double.compare(numberValue, -999) == 0) {
                      cell.setCellValue("-");
                    } else {
                      cell.setCellValue(numberValue);
                    }
                  } else {
                    cell.setCellValue(
                        CommonExport.replaceStringValue(replace[0], valueReplace, more, i));
                  }
                } else {
                  if ("NUMBER".equals(head.getStyleFormat())) {
                    if (StringUtils.isNotNullOrEmpty(value)) {
                      cell.setCellValue(Double.valueOf(value));
                    } else {
                      cell.setCellValue(value == null ? "" : value);
                    }
                  } else {
                    cell.setCellValue(value == null ? "" : value);
                  }
                }

                if ("CENTER".equals(align)) {
                  cell.setCellStyle(cellStyleCenter);
                }
                if ("LEFT".equals(align)) {
                  cell.setCellStyle(cellStyleLeft);
                }
                if ("RIGHT".equals(align)) {
                  cell.setCellStyle(cellStyleRight);
                }

                j++;
              }
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Merge row">
        if (item.getLstCellMerge() != null) {
          for (CellConfigExport cell : item.getLstCellMerge()) {
            if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
              CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                  cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                  cell.getColumn() + cell.getColumnMerge());
              sheet.addMergedRegion(cellRangeAddress);
              RegionUtil
                  .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Auto size column">
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i <= item.getHeader().size(); i++) {
          sheet.autoSizeColumn(i);
          if (sheet.getColumnWidth(i) > 20000) {
            sheet.setColumnWidth(i, 20000);
          }
        }
        //</editor-fold>

        check++;
      }

      createCombo(workbook, deviceList, impactNodeList, groupUnitList, groupUnitDetailList,
          tempImportList);

      if (exportChart == null || exportChart.length == 0) {
        workbook.removeSheetAt(0);
      }

      if (exportChart != null && exportChart.length > 0) {
        //<editor-fold defaultstate="collapsed" desc="Ve bieu do">
        ConfigFileExport item = config.get(0);
        Sheet sheetConf = workbook_temp.getSheet("conf");

        // Dong bat dau du lieu cua chart
        Row rowStartConf = sheetConf.getRow(0);
        Cell cellStartConf = rowStartConf.getCell(1);
        cellStartConf.setCellValue(item.getStartRow() + 1);

        // Dong ket thuc du lieu cua chart
        Row rowEndConf = sheetConf.getRow(1);
        Cell cellEndConf = rowEndConf.getCell(1);
        cellEndConf.setCellValue(item.getStartRow() + 1 + item.getLstData().size());

        // Cot bat dau du lieu cua chart
        String xStart = "";

        // Cot ket thuc du lieu cua chart
        String xEnd = "";

        // xAxis
        Row rowXvalue = sheetConf.getRow(2);
        Cell cellXvalue = rowXvalue.getCell(1);
        cellXvalue.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + xStart + "${startRow}:$" + xEnd + "${startRow}");

        // Categories
        Row rowNameList = sheetConf.getRow(3);
        Cell cellNameList = rowNameList.getCell(1);
        cellNameList.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + exportChart[0] + "${i}");

        // Data
        Row rowDataValue = sheetConf.getRow(4);
        Cell cellDataValue = rowDataValue.getCell(1);
        cellDataValue.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + xStart + "${i}:$" + xEnd + "${i}");
        //</editor-fold>
      }

      try {
        FileOutputStream fileOut = new FileOutputStream(pathOut);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    } catch (FileNotFoundException e) {
      log.error(e.getMessage(), e);
    }
    return new File(pathOut);
  }

  public void createCombo(SXSSFWorkbook workbook, List<DeviceTypesDTO> deviceList,
      List<ImpactSegmentDTO> impactNodeList,
      List<GroupUnitDTO> groupUnitList, List<GroupUnitDetailDTO> groupUnitDetailList,
      List<TempImportDTO> tempImportList) {

    WoTypeInsideDTO typeS = new WoTypeInsideDTO();
    typeS.setEnableCreate(1L);

    List<WoTypeInsideDTO> listTypeWo = woCategoryServiceProxy.getListWoTypeDTO(typeS);
    createComboSheetTwo(workbook);
    createOtherSheet(workbook);
    createTypeWoSheet(workbook, listTypeWo);
    createDeviceSheet(workbook, deviceList);
    createImpactNodeSheet(workbook, impactNodeList);
    createGroupUnitSheet(workbook, groupUnitList);
    createGroupUnitDetailSheet(workbook, groupUnitDetailList);
    createTempImportSheet(workbook, tempImportList);

    XSSFSheet sheetOne = workbook.getXSSFWorkbook().getSheetAt(1);
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetOne);

    Name CrType = workbook.createName();
    CrType.setNameName("CrType");
    CrType.setRefersToFormula("Orther!$A$2:$A$4");

    XSSFDataValidationConstraint phaseNameConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "CrType");

    CellRangeAddressList cellRangeFlowPhase = new CellRangeAddressList(8, 1000, 4, 4);
    XSSFDataValidation dataValidationFlowPhase = (XSSFDataValidation) dvHelper.createValidation(
        phaseNameConstraint, cellRangeFlowPhase);

    dataValidationFlowPhase.setSuppressDropDownArrow(true);
    dataValidationFlowPhase.setShowErrorBox(true);
    dataValidationFlowPhase.setShowPromptBox(true);
    sheetOne.addValidationData(dataValidationFlowPhase);

    // cb 2
    Name RiskType = workbook.createName();
    RiskType.setNameName("RiskType");
    RiskType.setRefersToFormula("Orther!$B$2:$B$5");

    XSSFDataValidationConstraint risk = new XSSFDataValidationConstraint(
        ValidationType.LIST, "RiskType");

    CellRangeAddressList cellRisk = new CellRangeAddressList(8, 1000, 5, 5);
    XSSFDataValidation validationRisk = (XSSFDataValidation) dvHelper.createValidation(
        risk, cellRisk);

    validationRisk.setSuppressDropDownArrow(true);
    validationRisk.setShowErrorBox(true);
    validationRisk.setShowPromptBox(true);
    sheetOne.addValidationData(validationRisk);

    // cb 3
    Name PhysicalType = workbook.createName();
    PhysicalType.setNameName("PhysicalType");
    PhysicalType.setRefersToFormula("Orther!$C$2:$C$3");

    XSSFDataValidationConstraint physical = new XSSFDataValidationConstraint(
        ValidationType.LIST, "PhysicalType");

    CellRangeAddressList cellPhysical = new CellRangeAddressList(8, 1000, 8, 8);
    XSSFDataValidation validationPhysical = (XSSFDataValidation) dvHelper.createValidation(
        physical, cellPhysical);

    validationPhysical.setSuppressDropDownArrow(true);
    validationPhysical.setShowErrorBox(true);
    validationPhysical.setShowPromptBox(true);
    sheetOne.addValidationData(validationPhysical);

    // cb 4
    Name ImpactTimer = workbook.createName();
    ImpactTimer.setNameName("ImpactTimer");
    ImpactTimer.setRefersToFormula("Orther!$D$2:$D$" + temp.get(0));

    XSSFDataValidationConstraint impactTimer = new XSSFDataValidationConstraint(
        ValidationType.LIST, "ImpactTimer");

    CellRangeAddressList cellImpactTimer = new CellRangeAddressList(8, 1000, 9, 9);
    XSSFDataValidation validationImpactTimer = (XSSFDataValidation) dvHelper.createValidation(
        impactTimer, cellImpactTimer);

    validationImpactTimer.setSuppressDropDownArrow(true);
    validationImpactTimer.setShowErrorBox(true);
    validationImpactTimer.setShowPromptBox(true);
    sheetOne.addValidationData(validationImpactTimer);

    // cb 5
    Name Mop = workbook.createName();
    Mop.setNameName("Mop");
    Mop.setRefersToFormula("Orther!$E$2:$E$3");

    XSSFDataValidationConstraint mop = new XSSFDataValidationConstraint(
        ValidationType.LIST, "Mop");

    CellRangeAddressList cellMop = new CellRangeAddressList(8, 1000, 10, 10);
    XSSFDataValidation validationMop = (XSSFDataValidation) dvHelper.createValidation(
        mop, cellMop);

    validationMop.setSuppressDropDownArrow(true);
    validationMop.setShowErrorBox(true);
    validationMop.setShowPromptBox(true);
    sheetOne.addValidationData(validationMop);

    // cb 6
    Name Log = workbook.createName();
    Log.setNameName("Log");
    Log.setRefersToFormula("Orther!$F$2:$F$3");

    XSSFDataValidationConstraint log = new XSSFDataValidationConstraint(
        ValidationType.LIST, "Log");

    CellRangeAddressList cellLog = new CellRangeAddressList(8, 1000, 11, 11);
    XSSFDataValidation validationLog = (XSSFDataValidation) dvHelper.createValidation(
        log, cellLog);

    validationLog.setSuppressDropDownArrow(true);
    validationLog.setShowErrorBox(true);
    validationLog.setShowPromptBox(true);
    sheetOne.addValidationData(validationLog);

    // cb 8
    Name action = workbook.createName();
    action.setNameName("action");
    action.setRefersToFormula("Orther!$H$2:$H$3");

    XSSFDataValidationConstraint actionType = new XSSFDataValidationConstraint(
        ValidationType.LIST, "action");

    CellRangeAddressList cellAction = new CellRangeAddressList(8, 1000, 15, 15);
    XSSFDataValidation validationAction = (XSSFDataValidation) dvHelper.createValidation(
        actionType, cellAction);

    validationAction.setSuppressDropDownArrow(true);
    validationAction.setShowErrorBox(true);
    validationAction.setShowPromptBox(true);
    sheetOne.addValidationData(validationAction);
  }

  public void createComboSheetTwo(SXSSFWorkbook workbook) {
    XSSFSheet sheetTwo = workbook.getXSSFWorkbook().getSheetAt(2);
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetTwo);

    Name isRequire = workbook.createName();
    isRequire.setNameName("isRequire");
    isRequire.setRefersToFormula("Orther!$E$2:$E$3");

    XSSFDataValidationConstraint mop = new XSSFDataValidationConstraint(
        ValidationType.LIST, "isRequire");

    CellRangeAddressList cellMop = new CellRangeAddressList(8, 1000, 6, 6);
    XSSFDataValidation validationMop = (XSSFDataValidation) dvHelper.createValidation(
        mop, cellMop);

    validationMop.setSuppressDropDownArrow(true);
    validationMop.setShowErrorBox(true);
    validationMop.setShowPromptBox(true);
    sheetTwo.addValidationData(validationMop);

    // cb 2
    Name isRequireWo = workbook.createName();
    isRequireWo.setNameName("isRequireWo");
    isRequireWo.setRefersToFormula("Orther!$E$2:$E$3");

    XSSFDataValidationConstraint risk = new XSSFDataValidationConstraint(
        ValidationType.LIST, "isRequireWo");

    CellRangeAddressList cellRisk = new CellRangeAddressList(8, 1000, 7, 7);
    XSSFDataValidation validationRisk = (XSSFDataValidation) dvHelper.createValidation(
        risk, cellRisk);

    validationRisk.setSuppressDropDownArrow(true);
    validationRisk.setShowErrorBox(true);
    validationRisk.setShowPromptBox(true);
    sheetTwo.addValidationData(validationRisk);

    // cb 3
    Name createCloseCr = workbook.createName();
    createCloseCr.setNameName("createCloseCr");
    createCloseCr.setRefersToFormula("Orther!$E$2:$E$3");

    XSSFDataValidationConstraint physical = new XSSFDataValidationConstraint(
        ValidationType.LIST, "createCloseCr");

    CellRangeAddressList cellPhysical = new CellRangeAddressList(8, 1000, 8, 8);
    XSSFDataValidation validationPhysical = (XSSFDataValidation) dvHelper.createValidation(
        physical, cellPhysical);

    validationPhysical.setSuppressDropDownArrow(true);
    validationPhysical.setShowErrorBox(true);
    validationPhysical.setShowPromptBox(true);
    sheetTwo.addValidationData(validationPhysical);
  }

  public void createOtherSheet(SXSSFWorkbook workbook) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook().createSheet("Orther");
    ewu.createCell(sheetOrther, 0, 0,
        "CrType", styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        "RiskType", styles.get("header"));
    ewu.createCell(sheetOrther, 2, 0,
        "PhysicalType", styles.get("header"));
    ewu.createCell(sheetOrther, 3, 0,
        "ImpactTimer", styles.get("header"));
    ewu.createCell(sheetOrther, 4, 0,
        "Mop", styles.get("header"));
    ewu.createCell(sheetOrther, 5, 0,
        "Log", styles.get("header"));
    ewu.createCell(sheetOrther, 6, 0,
        "File", styles.get("header"));
    ewu.createCell(sheetOrther, 7, 0,
        "Action", styles.get("header"));

    int row = 1;
    String[] crType = {I18n.getLanguage("CrProcessManager.crType.CR_NORMAL"),
        I18n.getLanguage("CrProcessManager.crType.CR_EMERGENCY"),
        I18n.getLanguage("CrProcessManager.crType.CR_STANDARD")};
    for (String item : crType) {
      ewu.createCell(sheetOrther, 0, row++, item, styles.get("cell"));
    }

    row = 1;
    String[] riskType = {I18n.getLanguage("CrProcessManager.riskType.LOW"),
        I18n.getLanguage("CrProcessManager.riskType.MEDIUM"),
        I18n.getLanguage("CrProcessManager.riskType.HIGH"),
        I18n.getLanguage("CrProcessManager.riskType.VERY_HIGH")};
    for (String item : riskType) {
      ewu.createCell(sheetOrther, 1, row++, item, styles.get("cell"));
    }

    row = 1;
    String[] physicalType = {I18n.getLanguage("CrProcessManager.physicalType.LOGIC"),
        I18n.getLanguage("CrProcessManager.physicalType.PHYSICAL")};
    for (String item : physicalType) {
      ewu.createCell(sheetOrther, 2, row++, item, styles.get("cell"));
    }

    row = 1;
    ObjectSearchDto dto = new ObjectSearchDto();
    dto.setIsHasChildren(0l);
    dto.setModuleName("GNOC_CR_IMPACT_FRAME");
    dto.setRownum(0l);
    List<DataItemDTO> list = commonRepository.getListCombobox(dto);
    for (DataItemDTO item : list) {
      ewu.createCell(sheetOrther, 3, row++, item.getItemName(), styles.get("cell"));
      mapImpactType.put(item.getItemName(), Long.parseLong(item.getItemId()));
    }

    temp.add(row);

    row = 1;
    String[] mopType = {I18n.getLanguage("CrProcessManager.mopType.YES"),
        I18n.getLanguage("CrProcessManager.mopType.NO")};
    for (String item : mopType) {
      ewu.createCell(sheetOrther, 4, row++, item, styles.get("cell"));
    }

    row = 1;
    String[] logType = {I18n.getLanguage("CrProcessManager.logType.YES"),
        I18n.getLanguage("CrProcessManager.logType.NO")};
    for (String item : logType) {
      ewu.createCell(sheetOrther, 5, row++, item, styles.get("cell"));
    }

    row = 1;
    String[] file = {I18n.getLanguage("CrProcessManager.file.INPUT"),
        I18n.getLanguage("CrProcessManager.file.OUT")};
    for (String item : file) {
      ewu.createCell(sheetOrther, 6, row++, item, styles.get("cell"));
    }

    row = 1;
    String[] action = {I18n.getLanguage("CrProcessManager.action.YES"),
        I18n.getLanguage("CrProcessManager.action.NO")};
    for (String item : action) {
      ewu.createCell(sheetOrther, 7, row++, item, styles.get("cell"));
    }

    workbook.setSheetHidden(3, true);
  }

  public void createDeviceSheet(SXSSFWorkbook workbook, List<DeviceTypesDTO> typesDTOList) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("deviceTypes.title_export"));
    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("deviceTypes.deviceTypeCode"), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("deviceTypes.deviceTypeName"), styles.get("header"));

    sheetOrther.setColumnWidth(0, 20000);
    sheetOrther.setColumnWidth(1, 25000);

    int row = 1;
    for (DeviceTypesDTO item : typesDTOList) {
      ewu.createCell(sheetOrther, 0, row++, item.getDeviceTypeCode(), styles.get("cell"));
    }

    row = 1;
    for (DeviceTypesDTO item : typesDTOList) {
      ewu.createCell(sheetOrther, 1, row++, item.getDeviceTypeName(), styles.get("cell"));
    }

  }

  public void createImpactNodeSheet(SXSSFWorkbook workbook,
      List<ImpactSegmentDTO> impactSegmentDTOList) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("ImpactSegment.export.title"));
    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("CrProcessManager.impactSegmentCode"), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("ImpactSegment.impactSegmentName"), styles.get("header"));
    ewu.createCell(sheetOrther, 2, 0,
        I18n.getLanguage("ImpactSegment.appliedSystem"), styles.get("header"));

    sheetOrther.setColumnWidth(0, 20000);
    sheetOrther.setColumnWidth(1, 20000);
    sheetOrther.setColumnWidth(2, 1200);

    int row = 1;
    for (ImpactSegmentDTO item : impactSegmentDTOList) {
      ewu.createCell(sheetOrther, 0, row++, item.getImpactSegmentCode(), styles.get("cell"));
    }

    row = 1;
    for (ImpactSegmentDTO item : impactSegmentDTOList) {
      ewu.createCell(sheetOrther, 1, row++, item.getImpactSegmentName(), styles.get("cell"));
    }

    row = 1;
    for (ImpactSegmentDTO item : impactSegmentDTOList) {
      ewu.createCell(sheetOrther, 2, row++, item.getAppliedSystem() + "", styles.get("cell"));
    }
  }

  public void createGroupUnitSheet(SXSSFWorkbook workbook, List<GroupUnitDTO> groupUnitDTOList) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("GroupUnit.export"));
    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("GroupUnit.code"), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("GroupUnit.name"), styles.get("header"));

    sheetOrther.setColumnWidth(0, 10000);
    sheetOrther.setColumnWidth(1, 10000);

    int row = 1;
    for (GroupUnitDTO item : groupUnitDTOList) {
      ewu.createCell(sheetOrther, 0, row++, item.getGroupUnitCode(), styles.get("cell"));
    }

    row = 1;
    for (GroupUnitDTO item : groupUnitDTOList) {
      ewu.createCell(sheetOrther, 1, row++, item.getGroupUnitName(), styles.get("cell"));
    }
  }

  public void createGroupUnitDetailSheet(SXSSFWorkbook workbook,
      List<GroupUnitDetailDTO> groupUnitDetailDTOList) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("GroupUnitDetail.export"));
    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("GroupUnitDetail.code"), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("GroupUnitDetail.codeOfGroup"), styles.get("header"));

    sheetOrther.setColumnWidth(0, 10000);
    sheetOrther.setColumnWidth(1, 10000);

    int row = 1;
    for (GroupUnitDetailDTO item : groupUnitDetailDTOList) {
      ewu.createCell(sheetOrther, 0, row++, item.getGroupUnitId() + "", styles.get("cell"));
    }

    row = 1;
    for (GroupUnitDetailDTO item : groupUnitDetailDTOList) {
      ewu.createCell(sheetOrther, 1, row++, item.getUnitId() + "", styles.get("cell"));
    }
  }

  public void createTempImportSheet(SXSSFWorkbook workbook, List<TempImportDTO> tempImportDTOList) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("TempImport.export"));
    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("TempImport.code"), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("TempImport.name"), styles.get("header"));

    sheetOrther.setColumnWidth(0, 20000);
    sheetOrther.setColumnWidth(1, 20000);

    int row = 1;
    for (TempImportDTO item : tempImportDTOList) {
      ewu.createCell(sheetOrther, 0, row++, item.getCode(), styles.get("cell"));
    }

    row = 1;
    for (TempImportDTO item : tempImportDTOList) {
      ewu.createCell(sheetOrther, 1, row++, item.getName(), styles.get("cell"));
    }

  }

  public void createWoSheet(SXSSFWorkbook workbook) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetWo = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("CrProcessManager.wo.export.title"));
    ewu.createCell(sheetWo, 0, 0,
        I18n.getLanguage("common.STT"), styles.get("header"));
    ewu.createCell(sheetWo, 1, 0,
        I18n.getLanguage("wo.processCode"), styles.get("header"));
    ewu.createCell(sheetWo, 2, 0,
        I18n.getLanguage("wo.woName"), styles.get("header"));
    ewu.createCell(sheetWo, 3, 0,
        I18n.getLanguage("wo.descreption"), styles.get("header"));
    ewu.createCell(sheetWo, 4, 0,
        I18n.getLanguage("wo.dayWork"), styles.get("header"));
    ewu.createCell(sheetWo, 5, 0,
        I18n.getLanguage("wo.groupType"), styles.get("header"));
    ewu.createCell(sheetWo, 6, 0,
        I18n.getLanguage("wo.requiredWO"), styles.get("header"));
    ewu.createCell(sheetWo, 7, 0,
        I18n.getLanguage("wo.requiredWOCloseCr"), styles.get("header"));
    ewu.createCell(sheetWo, 8, 0,
        I18n.getLanguage("wo.completeCR"), styles.get("header"));

    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("wo.processCode"),
        I18n.getLanguage("wo.woName"),
        I18n.getLanguage("wo.dayWork"),
        I18n.getLanguage("wo.groupType")
    };
    XSSFFont starFont = workbook.getXSSFWorkbook().createFont();
    starFont.setColor(IndexedColors.RED.getIndex());
    List<String> listHeaderStars = Arrays.asList(headerStar);

    XSSFRichTextString rich0 = new XSSFRichTextString(listHeaderStars.get(0));
    XSSFRichTextString rich1 = new XSSFRichTextString(listHeaderStars.get(1));
    XSSFRichTextString rich2 = new XSSFRichTextString(listHeaderStars.get(2));
    XSSFRichTextString rich3 = new XSSFRichTextString(listHeaderStars.get(3));
    XSSFRichTextString rich4 = new XSSFRichTextString(I18n.getLanguage("wo.completeCR"));

    rich0.append("*", starFont);
    rich1.append("*", starFont);
    rich2.append("*", starFont);
    rich3.append("*", starFont);
    rich4.append(I18n.getLanguage("typeWo.warning"), starFont);

    Row tempRow = sheetWo.getRow(0);
    tempRow.getCell(1).setCellValue(rich0);
    tempRow.getCell(2).setCellValue(rich1);
    tempRow.getCell(4).setCellValue(rich2);
    tempRow.getCell(5).setCellValue(rich3);
    tempRow.getCell(8).setCellValue(rich4);

    Font xSSFFontHeader = workbook.createFont();
    xSSFFontHeader.setFontName("Times New Roman");
    xSSFFontHeader.setFontHeightInPoints((short) 12);
    xSSFFontHeader.setColor(IndexedColors.BLACK.index);
    xSSFFontHeader.setBold(true);

    CellStyle cellStyleHeader = workbook.createCellStyle();
    cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleHeader.setBorderLeft(BorderStyle.THIN);
    cellStyleHeader.setBorderBottom(BorderStyle.THIN);
    cellStyleHeader.setBorderRight(BorderStyle.THIN);
    cellStyleHeader.setBorderTop(BorderStyle.THIN);
    cellStyleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
    cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyleHeader.setWrapText(true);
    cellStyleHeader.setFont(xSSFFontHeader);

    tempRow.getCell(1).setCellStyle(cellStyleHeader);
    tempRow.getCell(2).setCellStyle(cellStyleHeader);
    tempRow.getCell(4).setCellStyle(cellStyleHeader);
    tempRow.getCell(5).setCellStyle(cellStyleHeader);
    tempRow.getCell(6).setCellStyle(cellStyleHeader);

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetWo);

    // cb 5
    Name createWo = workbook.createName();
    createWo.setNameName("createWo");
    createWo.setRefersToFormula("Orther!$E$2:$E$3");

    XSSFDataValidationConstraint wo = new XSSFDataValidationConstraint(
        ValidationType.LIST, "createWo");

    CellRangeAddressList cellWo = new CellRangeAddressList(1, 1000, 6, 6);
    XSSFDataValidation validationCreateWo = (XSSFDataValidation) dvHelper.createValidation(
        wo, cellWo);

    validationCreateWo.setSuppressDropDownArrow(true);
    validationCreateWo.setShowErrorBox(true);
    validationCreateWo.setShowPromptBox(true);
    sheetWo.addValidationData(validationCreateWo);

    // cb 6
    Name woCloseCR = workbook.createName();
    woCloseCR.setNameName("woCloseCR");
    woCloseCR.setRefersToFormula("Orther!$E$2:$E$3");

    XSSFDataValidationConstraint woCR = new XSSFDataValidationConstraint(
        ValidationType.LIST, "woCloseCR");

    CellRangeAddressList cellWoCR = new CellRangeAddressList(1, 1000, 7, 7);
    XSSFDataValidation validationWoCR = (XSSFDataValidation) dvHelper.createValidation(
        woCR, cellWoCR);

    validationWoCR.setSuppressDropDownArrow(true);
    validationWoCR.setShowErrorBox(true);
    validationWoCR.setShowPromptBox(true);
    sheetWo.addValidationData(validationWoCR);

    // cb 6
    Name completeCR = workbook.createName();
    completeCR.setNameName("completeCR");
    completeCR.setRefersToFormula("Orther!$E$2:$E$3");

    XSSFDataValidationConstraint cpltCR = new XSSFDataValidationConstraint(
        ValidationType.LIST, "completeCR");

    CellRangeAddressList complete = new CellRangeAddressList(1, 1000, 8, 8);
    XSSFDataValidation validationComplete = (XSSFDataValidation) dvHelper.createValidation(
        cpltCR, complete);

    validationComplete.setSuppressDropDownArrow(true);
    validationComplete.setShowErrorBox(true);
    validationComplete.setShowPromptBox(true);
    sheetWo.addValidationData(validationComplete);

  }

  public void createTypeWoSheet(SXSSFWorkbook workbook, List<WoTypeInsideDTO> list) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetTypeWo = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("wo.typeWork.title"));
    ewu.createCell(sheetTypeWo, 0, 0,
        I18n.getLanguage("wo.typeWo.id"), styles.get("header"));
    ewu.createCell(sheetTypeWo, 1, 0,
        I18n.getLanguage("wo.typeWo.code"), styles.get("header"));
    ewu.createCell(sheetTypeWo, 2, 0,
        I18n.getLanguage("wo.typeWo.name"), styles.get("header"));

    sheetTypeWo.setColumnWidth(0, 9000);
    sheetTypeWo.setColumnWidth(1, 15000);
    sheetTypeWo.setColumnWidth(2, 35000);

    int row = 1;
    for (WoTypeInsideDTO item : list) {
      ewu.createCell(sheetTypeWo, 0, row++, item.getWoTypeId().toString(), styles.get("cell"));
    }

    row = 1;
    for (WoTypeInsideDTO item : list) {
      ewu.createCell(sheetTypeWo, 1, row++, item.getWoTypeCode(), styles.get("cell"));
    }

    row = 1;
    for (WoTypeInsideDTO item : list) {
      ewu.createCell(sheetTypeWo, 2, row++, item.getWoTypeName(), styles.get("cell"));
    }
  }

  //2020-10-03 hungtv add

  private CrProcessInsideDTO convertListName (CrProcessInsideDTO dto) {
    try {
      if (dto.getListCrProcessTemplate() != null && dto.getListCrProcessTemplate().size() > 0) {
        List<String> listCrProcessTemplateName = new ArrayList<>();
        List<CrProcessTemplateDTO> listCrProcessTemplate = dto.getListCrProcessTemplate();
        for (CrProcessTemplateDTO file : listCrProcessTemplate) {
          listCrProcessTemplateName.add(file.getCode());
        }
        dto.setListCrProcessTemplateName(listCrProcessTemplateName);
      }
      if (dto.getListCrProcessWo() != null && dto.getListCrProcessWo().size() > 0) {
        List<String> listCrProcessWoName = new ArrayList<>();
        List<CrProcessWoDTO> listCrProcessWo = dto.getListCrProcessWo();
        for (CrProcessWoDTO crProcessWoDTO : listCrProcessWo) {
          listCrProcessWoName.add(crProcessWoDTO.getWoTypeName());
        }
        dto.setListCrProcessWoName(listCrProcessWoName);
      }
      if (dto.getListCrProcessDeptGroup() != null && dto.getListCrProcessDeptGroup().size() > 0) {
        List<String> listCrProcessDeptGroupName = new ArrayList<>();
        List<CrProcessDeptGroupDTO> listCrProcessDeptGroup = dto.getListCrProcessDeptGroup();
        for (CrProcessDeptGroupDTO dept : listCrProcessDeptGroup) {
          listCrProcessDeptGroupName.add(dept.getGroupUnitCode());
        }
        dto.setListCrProcessDeptGroupName(listCrProcessDeptGroupName);
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return dto;
  }

  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = Arrays.asList("crProcessId", "crProcessCode", "crProcessName", "description", "impactSegmentId",
          "deviceTypeId", "subcategoryId", "riskLevel", "impactType", "crTypeId", "isActive", "parentId", "impactCharacteristic",
          "otherDept", "requireMop", "requireFileLog", "requireFileTest", "approvalLevel", "closeCrWhenResolveSuccess",
          "isVmsaActiveCellProcess", "vmsaActiveCellProcessKey", "isLaneImpact", "requireApprove", "crProcessIndex",
          "listCrProcessTemplateName", "listCrProcessWoName", "listCrProcessDeptGroupName");
      return keys;
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
