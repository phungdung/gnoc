package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.repository.SmsGatewayCommonRepository;
import com.viettel.gnoc.repository.UnitCommonRepository;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
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

@Service
@Transactional
@Slf4j
public class UnitCommonBusinessImpl implements UnitCommonBusiness {

  @Autowired
  private UnitCommonRepository unitCommonRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected CatItemRepository catItemRepository;

  @Autowired
  private CatLocationRepository catLocationRepository;

  @Autowired
  private SmsGatewayCommonRepository smsGatewayCommonRepository;

  private final static String Unit_Common_Export = "Unit_Common_Export";

  Map<Long, UnitDTO> mapUnit = new HashMap<>();
  Map<String, String> mapUnitCodeUnitId = new HashMap<>();
  Map<Long, String> mapUnitLevel = new HashMap<>();
  Map<Long, String> mapUnitType = new HashMap<>();
  Map<String, String> mapUnitTypeNameId = new HashMap<>();
  Map<String, String> mapUnitLevelNameId = new HashMap<>();
  Map<Long, String> mapSmsGateway = new HashMap<>();
  Map<Long, String> mapRoleType = new HashMap<>();
  Map<Long, String> mapLocation = new HashMap<>();
  HashMap<String, String> mapCatLocationCodeId = new HashMap();
  HashMap<String, String> mapSMSGateWayAliasId = new HashMap();
  HashMap<String, String> mapIPCCCodeId = new HashMap();
  HashMap<Long, String> mapIPCCCName = new HashMap();

  Map<String, String> mapParent = new HashMap<>();

  @Override
  public List<UnitDTO> getListUnit(UnitDTO unitDTO) {
    return unitCommonRepository.getListUnit(unitDTO);
  }

  public void setMapUnit() {
    mapUnit.clear();
    UnitDTO searchDTO = new UnitDTO();
    List<UnitDTO> lstUnitAll = getListUnit(searchDTO);
    if (lstUnitAll != null && lstUnitAll.size() > 0) {
      for (UnitDTO dto : lstUnitAll) {
        mapUnit.put(dto.getUnitId(), dto);
        mapUnitCodeUnitId.put(dto.getUnitCode(), dto.getUnitId().toString());
      }
    }
  }

  public void setCombobox() {
    mapUnitType.clear();
    mapUnitLevel.clear();
    mapSmsGateway.clear();
    mapRoleType.clear();
    mapLocation.clear();
    mapIPCCCodeId.clear();
    mapCatLocationCodeId.clear();
    mapSMSGateWayAliasId.clear();
    mapIPCCCName.clear();

    Datatable datatable = catItemRepository
        .getItemMaster("UNIT_TYPE", LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), null, null);
    List<CatItemDTO> typelLst = (List<CatItemDTO>) datatable.getData();
    if (typelLst != null && typelLst.size() > 0) {
      for (CatItemDTO dto : typelLst) {
        mapUnitType.put(dto.getItemId(), dto.getItemName());
        mapUnitTypeNameId.put(dto.getItemName(), dto.getItemId().toString());
      }
    }

    datatable = catItemRepository.getItemMaster("UNIT_LEVEL", LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        APPLIED_BUSSINESS.CAT_ITEM.toString(), null, null);
    List<CatItemDTO> levelLst = (List<CatItemDTO>) datatable.getData();
    if (levelLst != null && levelLst.size() > 0) {
      for (CatItemDTO dto : levelLst) {
        mapUnitLevel.put(dto.getItemId(), dto.getItemName());
        mapUnitLevelNameId.put(dto.getItemName(), dto.getItemId().toString());
      }
    }

    List<SmsGatewayDTO> smsLst = smsGatewayCommonRepository
        .getListSmsGatewayAll(new SmsGatewayDTO());
    if (smsLst != null && smsLst.size() > 0) {
      for (SmsGatewayDTO dto : smsLst) {
        mapSmsGateway.put(dto.getSmsGatewayId(), dto.getAlias());
        mapSMSGateWayAliasId.put(dto.getAlias(), dto.getSmsGatewayId().toString());

      }
    }

    datatable = catItemRepository.getItemMaster("ROLE_LIST", LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        APPLIED_BUSSINESS.CAT_ITEM.toString(), null, null);
    List<CatItemDTO> roleList = (List<CatItemDTO>) datatable.getData();
    if (roleList != null && roleList.size() > 0) {
      for (CatItemDTO dto : roleList) {
        mapRoleType.put(dto.getItemId(), dto.getItemName());
      }
    }

    List<CatLocationDTO> locationLst = catLocationRepository.getCatLocationByLevel("");
    if (locationLst != null && locationLst.size() > 0) {
      for (CatLocationDTO dto : locationLst) {
        mapLocation.put(Long.valueOf(dto.getLocationId()), dto.getLocationName());
        mapCatLocationCodeId.put(dto.getLocationCode(), dto.getLocationId());
      }
    }

//    CatLocationDTO dto = new CatLocationDTO();
//    List<CatLocationDTO> catLocationDTOs = catLocationRepository
//        .getListCatLocationDTO(dto, 0, Integer.MAX_VALUE, "ASC", "locationCode");
//    for (CatLocationDTO dtoLocation : catLocationDTOs) {
//      mapLocation.put(Long.valueOf(dto.getLocationId()), dto.getLocationName());
//      mapCatLocationCodeId.put(dtoLocation.getLocationCode(), dto.getLocationId());
//    }

    List<IpccServiceDTO> lstIpcc = smsGatewayCommonRepository
        .getListIpccServiceDTO(new IpccServiceDTO());
    for (IpccServiceDTO dtoIPCC : lstIpcc) {
      mapIPCCCodeId.put(dtoIPCC.getIpccServiceCode(), dtoIPCC.getIpccServiceId().toString());
      mapIPCCCName.put(dtoIPCC.getIpccServiceId(), dtoIPCC.getIpccServiceCode());
    }
  }

  @Override
  public Datatable getListUnitDTO(UnitDTO unitDTO) {
    Datatable datatable = unitCommonRepository.getListUnitDTO(unitDTO);
    if (datatable != null) {
      List<UnitDTO> list = (List<UnitDTO>) datatable.getData();
      if (list != null && list.size() > 0) {
        for (UnitDTO dto : list) {
          if (dto.getStatus() != null && dto.getStatus() == 0) {
            dto.setStatusStr("INACTIVE");
          }
          if (dto.getStatus() != null && dto.getStatus() == 1) {
            dto.setStatusStr("ACTIVE");
          }
        }
      }
    }

    return datatable;
  }

  public void setParams(UnitDTO dto) {
    Long parentId = dto.getParentUnitId();
    UnitDTO parent = mapUnit.get(parentId);
    if (parent != null) {
      dto.setParentUnitName(parent.getUnitName().trim());
    }
  }

  public void setDatatable(UnitDTO dto) {
    Long utLevel = dto.getUnitLevel();
    String level = mapUnitLevel.get(utLevel);
    if (level != null) {
      dto.setUnitLevelStr(level);
    }

    Long utType = dto.getUnitType();
    String type = mapUnitType.get(utType);
    if (type != null) {
      dto.setUnitTypeStr(type);
    }

    Long smsGatewayId = dto.getSmsGatewayId();
    String smsName = mapSmsGateway.get(smsGatewayId);
    if (smsName != null) {
      dto.setSmsGatewayName(smsName);
    }

    Long roleType = dto.getRoleType();
    String roleName = mapRoleType.get(roleType);
    if (roleName != null) {
      dto.setRoleTypeName(roleName);
    }

    Long location = dto.getLocationId();
    String locationName = mapLocation.get(location);
    if (locationName != null) {
      dto.setLocationName(locationName);
    }

    if (dto.getStatus() != null && dto.getStatus() == 0) {
      dto.setStatusStr("INACTIVE");
    }
    if (dto.getStatus() != null && dto.getStatus() == 1) {
      dto.setStatusStr("ACTIVE");
    }

    Long ipcc = dto.getIpccServiceId();
    String ipccName = mapIPCCCName.get(ipcc);
    if (ipccName != null) {
      dto.setIpccServiceName(ipccName);
    }

  }

  @Override
  public ResultInSideDto updateUnit(UnitDTO unitDTO) {
    UnitDTO oldHis = findUnitById(unitDTO.getUnitId());
    ResultInSideDto resultInSideDto = unitCommonRepository.updateUnit(unitDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      // Add history
      try {
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(unitDTO.getUnitId().toString());
        dataHistoryChange.setType("UTILITY_UNIT");
        dataHistoryChange.setActionType("update");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(unitDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        List<String> keys = getAllKeysDTO();
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update Unit", "Update Unit" + " " + resultInSideDto.getId(),
          unitDTO, null));
    }

    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateUnitChildren(UnitDTO unitDTO) {
    return unitCommonRepository.updateUnitChildren(unitDTO);
  }

  @Override
  public ResultInSideDto deleteUnit(Long id) {
    ResultInSideDto resultInSideDto = unitCommonRepository.deleteUnit(id);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      //Add history
      try {
        UnitDTO oldHis = findUnitById(id);
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(id.toString());
        dataHistoryChange.setType("UTILITY_UNIT");
        dataHistoryChange.setActionType("delete");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new UnitDTO());
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        List<String> keys = getAllKeysDTO();
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete Unit", "Delete Unit" + " " + resultInSideDto.getId(),
          id, null));
    }

    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListUnit(List<UnitDTO> unitListDTO) {
    return unitCommonRepository.deleteListUnit(unitListDTO);
  }

  @Override
  public UnitDTO findUnitById(Long id) {

    UnitDTO dto = unitCommonRepository.findUnitById(id);
    if (dto != null) {
      if (dto.getStatus() != null && dto.getStatus() == 0) {
        dto.setStatusStr("INACTIVE");
      }
      if (dto.getStatus() != null && dto.getStatus() == 1) {
        dto.setStatusStr("ACTIVE");
      }
    }

    return dto;
  }

  @Override
  public ResultInSideDto insertUnit(UnitDTO unitDTO) {
    ResultInSideDto resultInSideDto = unitCommonRepository.insertUnit(unitDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      // Add history
      try {
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("UTILITY_UNIT");
        dataHistoryChange.setActionType("add");
        //Old Object History
        dataHistoryChange.setOldObject(new UnitDTO());
        //New Object History
        dataHistoryChange.setNewObject(unitDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Insert Unit", "Insert Unit" + " " + resultInSideDto.getId(),
          unitDTO, null));
    }

    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertOrUpdateListUnit(List<UnitDTO> unitDTO) {
    return unitCommonRepository.insertOrUpdateListUnit(unitDTO);
  }


  @Override
  public List<UnitDTO> getListUnitByLevel(String level) {
    return unitCommonRepository.getListUnitByLevel(level);
  }

  @Override
  public List<UnitDTO> getUnitDTOForTree(Boolean isRoot, String status, String parentId) {
    return unitCommonRepository.getUnitDTOForTree(isRoot, status, parentId);
  }

  @Override
  public List<UnitDTO> getUnitVSADTOForTree(Boolean isRoot, String status, String parentId) {
    return unitCommonRepository.getUnitVSADTOForTree(isRoot, status, parentId);
  }

  @Override
  public List<UnitDTO> getListUnitVSA(UnitDTO unitDTO) {
    return unitCommonRepository.getListUnitVSA(unitDTO);
  }


  @Override
  public File exportData(UnitDTO unitDTO) throws Exception {
    setMapUnit();
    setCombobox();

    List<UnitDTO> lstCrEx =
        unitCommonRepository
            .getListUnitDTOExport(unitDTO);

    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "exportData", "ExportData unitDTO: " + unitDTO.getUnitId(),
        unitDTO, null));

    for (UnitDTO dto : lstCrEx) {
      setParams(dto);
      setDatatable(dto);
    }

    return exportFile(lstCrEx, "", false);
  }

  private File exportFile(List<UnitDTO> lstCrEx, String key, boolean check) throws
      Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("UnitCommon.export.title");
    String title = I18n.getLanguage("UnitCommon.export.title");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();

    columnSheet = new ConfigHeaderExport("unitCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("unitName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("parentUnitName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("description", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("unitTypeStr", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("unitLevelStr", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("locationName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("smsGatewayName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("ipccServiceName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    if (check) {
      columnSheet = new ConfigHeaderExport("isNocName", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
    }

    columnSheet = new ConfigHeaderExport("statusStr", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    if (check) {
      columnSheet = new ConfigHeaderExport("action", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
    }

    if (Constants.RESULT_IMPORT.equals(key)) {

    } else {
      fileNameOut = Unit_Common_Export;
      subTitle = I18n
          .getLanguage("UnitCommon.export.eportDate", dateFormat.format(new Date()));
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
        "language.UnitCommon",
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

  @Override
  public File getUnitTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");
    XSSFSheet sheetParentUnit = workbook.createSheet(I18n.getLanguage("UnitCommon.parentUnitName"));
//    XSSFSheet sheetTypeUnit = workbook.createSheet(I18n.getLanguage("UnitCommon.unitTypeStr"));
//    XSSFSheet sheetLevelUnit = workbook.createSheet(I18n.getLanguage("UnitCommon.unitLevelStr"));
    XSSFSheet sheetIpcc = workbook.createSheet(I18n.getLanguage("UnitCommon.ipccServiceName"));
    XSSFSheet sheetSmsGateway = workbook
        .createSheet(I18n.getLanguage("UnitCommon.smsGatewayName"));
    XSSFSheet sheetLocation = workbook.createSheet(I18n.getLanguage("UnitCommon.locationName"));

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("UnitCommon.unitCode"),
        I18n.getLanguage("UnitCommon.unitName"),
        I18n.getLanguage("UnitCommon.parentUnitName"),
        I18n.getLanguage("UnitCommon.description"),
        I18n.getLanguage("UnitCommon.unitTypeStr"),
        I18n.getLanguage("UnitCommon.unitLevelStr"),
        I18n.getLanguage("UnitCommon.locationCode"),
        I18n.getLanguage("UnitCommon.smsGatewayName"),
        I18n.getLanguage("UnitCommon.ipccServiceName"),
        I18n.getLanguage("UnitCommon.isNocName"),
        I18n.getLanguage("UnitCommon.statusStr"),
        I18n.getLanguage("UnitCommon.action")
    };

    String[] headerStar = new String[]{
        I18n.getLanguage("UnitCommon.unitCode"),
        I18n.getLanguage("UnitCommon.unitName"),
        I18n.getLanguage("UnitCommon.smsGatewayName"),
        I18n.getLanguage("UnitCommon.ipccServiceName"),
        I18n.getLanguage("UnitCommon.isNocName"),
        I18n.getLanguage("UnitCommon.statusStr"),
        I18n.getLanguage("UnitCommon.action")
    };

    List<String> listHeader = Arrays.asList(header);
    List<String> StarLst = Arrays.asList(headerStar);

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(0);
    titleRow.setHeightInPoints(30);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("UnitCommon.export.title"));
    titleCell.setCellStyle(style.get("title"));
    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    Row headerRow = sheetOne.createRow(4);
    headerRow.setHeightInPoints(16);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : StarLst) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }

    sheetOne.setColumnWidth(0, 3000);
    sheetParam.autoSizeColumn(0);

    workbook.setSheetName(0, I18n.getLanguage("UnitCommon.export.title"));

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);

    int row = 5;
    String[] isNoc = {"Yes", "No"};
    for (int i = 0; i < isNoc.length; i++) {
      excelWriterUtils
          .createCell(sheetParam, 1, row++, isNoc[i], style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name isNocName = workbook.createName();
    isNocName.setNameName("isNocName");
    isNocName.setRefersToFormula("param!$B$2:$B$" + row);
    XSSFDataValidationConstraint isNocConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isNocName");
    CellRangeAddressList isNocCreate = new CellRangeAddressList(5, 65000, 10,
        10);
    XSSFDataValidation dataValidationNoc = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isNocConstraint, isNocCreate);
    dataValidationNoc.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationNoc);

    row = 5;
    String[] isStatus = {"Active", "Inactive"};
    for (int i = 0; i < isStatus.length; i++) {
      excelWriterUtils
          .createCell(sheetParam, 2, row++, isStatus[i], style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name isStatusName = workbook.createName();
    isStatusName.setNameName("isStatusName");
    isStatusName.setRefersToFormula("param!$C$2:$C$" + row);
    XSSFDataValidationConstraint isStatusConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isStatusName");
    CellRangeAddressList isStatusCreate = new CellRangeAddressList(5, 65000, 11,
        11);
    XSSFDataValidation dataValidationStatus = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isStatusConstraint, isStatusCreate);
    dataValidationStatus.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationStatus);

    row = 5;
    String[] isAction = {"Insert", "Update"};
    for (int i = 0; i < isNoc.length; i++) {
      excelWriterUtils
          .createCell(sheetParam, 3, row++, isAction[i], style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name isActionName = workbook.createName();
    isActionName.setNameName("isActionName");
    isActionName.setRefersToFormula("param!$D$2:$D$" + row);
    XSSFDataValidationConstraint isActionConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isActionName");
    CellRangeAddressList actionCreate = new CellRangeAddressList(5, 65000, 12,
        12);
    XSSFDataValidation dataValidationAction = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isActionConstraint, actionCreate);
    dataValidationAction.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationAction);
    workbook.setSheetHidden(1, true);

    UnitDTO searchDTO = new UnitDTO();
    List<UnitDTO> lstUnitAll = getListUnit(searchDTO);
    excelWriterUtils
        .createCell(sheetParentUnit, 0, 0, I18n.getLanguage("UnitCommon.unitId"),
            style.get("header"));
    excelWriterUtils
        .createCell(sheetParentUnit, 1, 0, I18n.getLanguage("UnitCommon.parentUnitCode"),
            style.get("header"));
    excelWriterUtils
        .createCell(sheetParentUnit, 2, 0, I18n.getLanguage("UnitCommon.parentUnitName"),
            style.get("header"));

    row = 1;
    for (int i = 0; i < lstUnitAll.size(); i++) {
      excelWriterUtils
          .createCell(sheetParentUnit, 0, row, lstUnitAll.get(i).getUnitId().toString(),
              style.get("cell"));
      excelWriterUtils
          .createCell(sheetParentUnit, 1, row, lstUnitAll.get(i).getUnitCode(),
              style.get("cell"));
      excelWriterUtils
          .createCell(sheetParentUnit, 2, row, lstUnitAll.get(i).getUnitName(),
              style.get("cell"));
      row++;
    }

    sheetParentUnit.setColumnWidth(0, 13000);
    sheetParentUnit.setColumnWidth(1, 13000);
    sheetParentUnit.setColumnWidth(2, 13000);

    row = 1;
    Datatable datatable = catItemRepository
        .getItemMaster("UNIT_TYPE", LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), null, null);
    List<CatItemDTO> typelLst = (List<CatItemDTO>) datatable.getData();
//    excelWriterUtils
//        .createCell(sheetTypeUnit, 0, 0, I18n.getLanguage("UnitCommon.unitTypeId"),
//            style.get("header"));
//    excelWriterUtils
//        .createCell(sheetTypeUnit, 1, 0, I18n.getLanguage("UnitCommon.unitTypeStr"),
//            style.get("header"));
//    for (int i = 0; i < typelLst.size(); i++) {
//      excelWriterUtils
//          .createCell(sheetTypeUnit, 0, row, typelLst.get(i).getItemId().toString(),
//              style.get("cell"));
//      excelWriterUtils
//          .createCell(sheetTypeUnit, 1, row, typelLst.get(i).getItemName(), style.get("cell"));
//      row++;
//    }
//    sheetTypeUnit.setColumnWidth(1, 13000);

    row = 5;
    for (int i = 0; i < typelLst.size(); i++) {
      excelWriterUtils
          .createCell(sheetParam, 4, row++, typelLst.get(i).getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name isUnitType = workbook.createName();
    isUnitType.setNameName("isUnitType");
    isUnitType.setRefersToFormula("param!$E$2:$E$" + row);
    XSSFDataValidationConstraint isUnitTypeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isUnitType");
    CellRangeAddressList isUnitTypeCreate = new CellRangeAddressList(5, 65000, 5,
        5);
    XSSFDataValidation dataValidationUnitType = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isUnitTypeConstraint, isUnitTypeCreate);
    dataValidationUnitType.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationUnitType);

    row = 5;
    datatable = catItemRepository
        .getItemMaster("UNIT_LEVEL", LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), null, null);
    List<CatItemDTO> levelLst = (List<CatItemDTO>) datatable.getData();
//    excelWriterUtils
//        .createCell(sheetLevelUnit, 0, 0, I18n.getLanguage("UnitCommon.unitLevelId"),
//            style.get("header"));
//    excelWriterUtils
//        .createCell(sheetLevelUnit, 1, 0, I18n.getLanguage("UnitCommon.unitLevelStr"),
//            style.get("header"));
//    for (int i = 0; i < levelLst.size(); i++) {
//      excelWriterUtils
//          .createCell(sheetLevelUnit, 0, row, levelLst.get(i).getItemId().toString(),
//              style.get("cell"));
//      excelWriterUtils
//          .createCell(sheetLevelUnit, 1, row, levelLst.get(i).getItemName(), style.get("cell"));
//      row++;
//    }
//    sheetLevelUnit.setColumnWidth(1,13000);
    for (int i = 0; i < levelLst.size(); i++) {
      excelWriterUtils
          .createCell(sheetParam, 5, row++, levelLst.get(i).getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name isUnitLevel = workbook.createName();
    isUnitLevel.setNameName("isUnitLevel");
    isUnitLevel.setRefersToFormula("param!$F$2:$F$" + row);
    XSSFDataValidationConstraint isUnitLevelConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isUnitLevel");
    CellRangeAddressList isUnitLevelCreate = new CellRangeAddressList(5, 65000, 6,
        6);
    XSSFDataValidation dataValidationUnitLevel = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isUnitLevelConstraint, isUnitLevelCreate);
    dataValidationUnitLevel.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationUnitLevel);

    row = 1;
    List<SmsGatewayDTO> smsLst = smsGatewayCommonRepository
        .getListSmsGatewayAll(new SmsGatewayDTO());
    excelWriterUtils
        .createCell(sheetSmsGateway, 0, 0, I18n.getLanguage("UnitCommon.smsGatewayId"),
            style.get("header"));
    excelWriterUtils
        .createCell(sheetSmsGateway, 1, 0, I18n.getLanguage("UnitCommon.smsGatewayName"),
            style.get("header"));
    for (int i = 0; i < smsLst.size(); i++) {
      excelWriterUtils
          .createCell(sheetSmsGateway, 0, row, smsLst.get(i).getSmsGatewayId().toString(),
              style.get("cell"));
      excelWriterUtils
          .createCell(sheetSmsGateway, 1, row, smsLst.get(i).getAlias(), style.get("cell"));
      row++;
    }
    sheetSmsGateway.setColumnWidth(1, 13000);
    List<IpccServiceDTO> lstIpcc = smsGatewayCommonRepository
        .getListIpccServiceDTO(new IpccServiceDTO());
    excelWriterUtils
        .createCell(sheetIpcc, 0, 0, I18n.getLanguage("UnitCommon.ipccServiceId"),
            style.get("header"));
    excelWriterUtils
        .createCell(sheetIpcc, 1, 0, I18n.getLanguage("UnitCommon.ipccServiceName"),
            style.get("header"));
    row = 1;
    for (int i = 0; i < lstIpcc.size(); i++) {
      excelWriterUtils
          .createCell(sheetIpcc, 0, row, lstIpcc.get(i).getIpccServiceId().toString(),
              style.get("cell"));
      excelWriterUtils
          .createCell(sheetIpcc, 1, row, lstIpcc.get(i).getIpccServiceCode(), style.get("cell"));
      row++;
    }
    sheetIpcc.setColumnWidth(1, 13000);
    List<CatLocationDTO> locationLst = catLocationRepository.getCatLocationByLevel("");
    excelWriterUtils
        .createCell(sheetLocation, 0, 0, I18n.getLanguage("UnitCommon.locationId"),
            style.get("header"));
    excelWriterUtils
        .createCell(sheetLocation, 1, 0, I18n.getLanguage("UnitCommon.locationCode"),
            style.get("header"));
    excelWriterUtils
        .createCell(sheetLocation, 2, 0, I18n.getLanguage("UnitCommon.locationName"),
            style.get("header"));
    row = 1;
    for (int i = 0; i < locationLst.size(); i++) {
      excelWriterUtils
          .createCell(sheetLocation, 0, row, locationLst.get(i).getLocationId(),
              style.get("cell"));
      excelWriterUtils
          .createCell(sheetLocation, 1, row, locationLst.get(i).getLocationCode(),
              style.get("cell"));
      excelWriterUtils
          .createCell(sheetLocation, 2, row, locationLst.get(i).getLocationName(),
              style.get("cell"));
      row++;
    }
    sheetLocation.setColumnWidth(0, 13000);
    sheetLocation.setColumnWidth(1, 13000);
    sheetLocation.setColumnWidth(2, 13000);

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_UNIT" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) throws Exception {

    setCombobox();
    setMapUnit();
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }
        File fileImport = new File(filePath);

        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            12,
            1000
        );

        List<UnitDTO> unitImportDTOs = new ArrayList<>();

        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> lstDataAll = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            1,
            13,
            1000
        );
        if (lstDataAll.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }

        Boolean hasErr = false;

        Map<String, String> mapDuplicate = new HashMap<>();

        if (lstDataAll != null) {
          for (Object[] obj : lstDataAll) {
            UnitDTO unitImportDTO = new UnitDTO();
            int count = 0;

//            for (Object[] objDup : lstDataAll) {
//              if ((obj[0] != null) && (!"".equals(obj[0])) && (objDup[0] != null)
//                  && (!"".equals(objDup[0]))
//                  && (obj[0].toString().trim().equals(objDup[0].toString().trim()))) {
//                count++;
//              }
//            }

            if (StringUtils.isStringNullOrEmpty(unitImportDTO.getResultImport())) {
              unitImportDTO.setResultImport("");
            }

//            if (count > 1) {
//              unitImportDTO.setResultImport(I18n.getLanguage("unit.import.duperr") + ";");
//              hasErr = Boolean.valueOf(true);
//            }

            if ((obj[11] != null) && (!"".equals(obj[11]))) {
              unitImportDTO.setAction(obj[11].toString().trim());
              if (("Insert".equals(obj[11].toString().trim()))
                  && (obj[0] != null) && (!"".equals(obj[0]))) {
                if (mapUnitCodeUnitId.containsKey(obj[0].toString().trim())) {
                  unitImportDTO.setUnitCode(obj[0].toString().trim());
                  unitImportDTO.setResultImport(
                      unitImportDTO.getResultImport() + (I18n.getLanguage("UnitCommon.unitCode")
                          + " " + I18n.getLanguage("unit.import.has.value") + ";"));
                  hasErr = Boolean.valueOf(true);
                } else {
                  unitImportDTO.setUnitCode(obj[0].toString().trim());
                }
              } else if ("Update".equals(obj[11].toString().trim())
                  && obj[0] != null && !"".equals(obj[0])) {
                if (mapUnitCodeUnitId.containsKey(obj[0].toString().trim())) {
                  unitImportDTO.setUnitCode(obj[0].toString().trim());
                  unitImportDTO.setUnitId(Long.valueOf(mapUnitCodeUnitId.get(obj[0])));
                } else {
                  unitImportDTO.setUnitCode(obj[0].toString().trim());
                  unitImportDTO.setResultImport(unitImportDTO.getResultImport() + (
                      I18n.getLanguage("unit.list.unitcode.notfound") + ";"));
                  hasErr = Boolean.valueOf(true);
                }
              } else if (obj[0] != null) {
                unitImportDTO.setUnitCode(obj[0].toString().trim());
                unitImportDTO.setResultImport(unitImportDTO.getResultImport() + (I18n
                    .getLanguage("unit.list.action.wrong.value")) + ";");
                hasErr = Boolean.valueOf(true);
              }
            } else {
              unitImportDTO.setResultImport(
                  unitImportDTO.getResultImport() + (I18n.getLanguage("UnitCommon.action")) + " "
                      + (I18n.getLanguage("unit.import.must.fill")) + ";");
            }

            if ((obj[0] != null) && (!"".equals(obj[0]))) {
              if (obj[0].toString().trim().length() > 100) {
                unitImportDTO.setResultImport(
                    unitImportDTO.getResultImport() + (I18n
                        .getLanguage("unit.import.must.codeLength")) + ";");
                hasErr = Boolean.valueOf(true);
              }
              unitImportDTO.setUnitCode(obj[0].toString().trim());
            } else {
              unitImportDTO.setResultImport(
                  unitImportDTO.getResultImport() + I18n.getLanguage("UnitCommon.unitCode") + " "
                      + I18n.getLanguage("unit.import.must.fill") + ";");
              hasErr = Boolean.valueOf(true);
            }

            if ((obj[1] != null) && (!"".equals(obj[1]))) {
              if (obj[1].toString().trim().length() > 100) {
                unitImportDTO.setResultImport(
                    unitImportDTO.getResultImport() + (I18n
                        .getLanguage("unit.import.must.nameLength")) + ";");
                hasErr = Boolean.valueOf(true);
              }
              unitImportDTO.setUnitName(obj[1].toString().trim());
            } else {
              unitImportDTO.setResultImport(
                  unitImportDTO.getResultImport() + I18n.getLanguage("UnitCommon.unitName") + " "
                      + I18n.getLanguage("unit.import.must.fill") + ";");
              hasErr = Boolean.valueOf(true);
            }

            if ((obj[2] != null) && (!"".equals(obj[2]))) {
              if (!mapUnitCodeUnitId.containsKey(obj[2].toString().trim())) {
                unitImportDTO.setParentUnitName(obj[2].toString().trim());
                unitImportDTO.setResultImport(
                    unitImportDTO.getResultImport() + I18n.getLanguage("unit.import.not.found")
                        + " " +
                        I18n.getLanguage("UnitCommon.parentUnitName") + ";");
                hasErr = Boolean.valueOf(true);
              } else {
                unitImportDTO.setParentUnitName(obj[2].toString().trim());
                unitImportDTO
                    .setParentUnitId(Long.valueOf(mapUnitCodeUnitId.get(obj[2].toString().trim())));
              }

            }

            if ((obj[3] != null) && (!"".equals(obj[3]))) {
              if (obj[3].toString().trim().length() > 1000) {
                unitImportDTO.setResultImport(
                    unitImportDTO.getResultImport() + (I18n
                        .getLanguage("unit.import.must.descriptionLength")) + ";");
                hasErr = Boolean.valueOf(true);
              }
              unitImportDTO.setDescription(obj[3].toString().trim());
            }

            if ((obj[4] != null) && (!"".equals(obj[4]))) {
              if (!mapUnitTypeNameId.containsKey(obj[4].toString().trim())) {
                unitImportDTO.setUnitTypeStr(obj[4].toString().trim());
                unitImportDTO.setResultImport(
                    unitImportDTO.getResultImport() + I18n.getLanguage("unit.import.not.found")
                        + " " +
                        I18n.getLanguage("UnitCommon.unitTypeStr") + ";");
                hasErr = Boolean.valueOf(true);
              } else {
                unitImportDTO.setUnitTypeStr(obj[4].toString().trim());
                unitImportDTO
                    .setUnitType(Long.valueOf(mapUnitTypeNameId.get(obj[4].toString().trim())));
              }

            }

            if ((obj[5] != null) && (!"".equals(obj[5]))) {
              if (!mapUnitLevelNameId.containsKey(obj[5].toString().trim())) {
                unitImportDTO.setUnitLevelStr(obj[5].toString().trim());
                unitImportDTO.setResultImport(
                    unitImportDTO.getResultImport() + I18n.getLanguage("unit.import.not.found")
                        + " " +
                        I18n.getLanguage("UnitCommon.unitLevelStr") + ";");
                hasErr = Boolean.valueOf(true);
              } else {
                unitImportDTO.setUnitLevelStr(obj[5].toString().trim());
                unitImportDTO
                    .setUnitLevel(Long.valueOf(mapUnitLevelNameId.get(obj[5].toString().trim())));
              }
            }

            if ((obj[6] != null) && (!"".equals(obj[6]))) {
              if (!mapCatLocationCodeId.containsKey(obj[6].toString().trim())) {
                unitImportDTO.setLocationName(obj[6].toString().trim());
                unitImportDTO.setResultImport(
                    unitImportDTO.getResultImport() + I18n.getLanguage("unit.import.not.found")
                        + " "
                        + I18n.getLanguage("UnitCommon.locationName") + ";");
                hasErr = Boolean.valueOf(true);
              } else {
                unitImportDTO.setLocationName(obj[6].toString().trim());
                unitImportDTO.setLocationId(
                    Long.valueOf(mapCatLocationCodeId.get(obj[6].toString().trim())));
              }

            }

            if ((obj[7] != null) && (!"".equals(obj[7]))) {
              if (!mapSMSGateWayAliasId.containsKey(obj[7].toString().trim())) {
                unitImportDTO.setSmsGatewayName(obj[7].toString().trim());
                unitImportDTO.setResultImport(
                    unitImportDTO.getResultImport() + I18n.getLanguage("unit.import.not.found")
                        + " " +
                        I18n.getLanguage("UnitCommon.smsGatewayName") + ";");
                hasErr = Boolean.valueOf(true);
              } else {
                unitImportDTO.setSmsGatewayName(obj[7].toString().trim());
                unitImportDTO.setSmsGatewayId(
                    Long.valueOf(mapSMSGateWayAliasId.get(obj[7].toString().trim())));
              }
            } else {
              unitImportDTO.setResultImport(
                  unitImportDTO.getResultImport() + I18n.getLanguage("UnitCommon.smsGatewayName")
                      + " "
                      + I18n.getLanguage("unit.import.must.fill") + ";");
              hasErr = Boolean.valueOf(true);
            }

            if ((obj[8] != null) && (!"".equals(obj[8]))) {
              if (!mapIPCCCodeId.containsKey(obj[8].toString().trim())) {
                unitImportDTO.setIpccServiceName(obj[8].toString().trim());
                unitImportDTO.setResultImport(
                    unitImportDTO.getResultImport() + I18n.getLanguage("unit.import.not.found")
                        + " " +
                        I18n.getLanguage("UnitCommon.ipccServiceName") + ";");
                hasErr = Boolean.valueOf(true);
              } else {
                unitImportDTO.setIpccServiceName(obj[8].toString().trim());
                unitImportDTO
                    .setIpccServiceId(Long.parseLong(mapIPCCCodeId.get(obj[8].toString().trim())));
              }
            } else {
              unitImportDTO.setResultImport(
                  unitImportDTO.getResultImport() + I18n.getLanguage("UnitCommon.ipccServiceName")
                      + " " +
                      I18n.getLanguage("unit.import.must.fill") + ";");
              hasErr = Boolean.valueOf(true);
            }

            if ((obj[9] != null) && (!"".equals(obj[9]))) {
              if (("Yes".equals(obj[9].toString().trim())) || ("No"
                  .equals(obj[9].toString().trim()))) {

                if ("Yes".equals(obj[9].toString().trim())) {
                  unitImportDTO.setIsNoc(1l);
                } else if ("No"
                    .equals(obj[9].toString().trim())) {
                  unitImportDTO.setIsNoc(0l);
                }
                unitImportDTO.setIsNocName(obj[9].toString().trim());
              } else {
                unitImportDTO.setIsNocName(obj[9].toString().trim());
                unitImportDTO.setResultImport(
                    unitImportDTO.getResultImport() + I18n.getLanguage("UnitCommon.isNocName") + " "
                        +
                        I18n.getLanguage("unit.import.isNoc.value") + ";");
                hasErr = Boolean.valueOf(true);
              }
            } else {
              unitImportDTO.setResultImport(
                  unitImportDTO.getResultImport() + I18n.getLanguage("UnitCommon.isNocName") + " "
                      + I18n.getLanguage("unit.import.must.fill") + ";");
              hasErr = Boolean.valueOf(true);
            }

            if ((obj[10] != null) && (!"".equals(obj[10]))) {
              if (("Active".equals(obj[10].toString().trim())) || ("Inactive"
                  .equals(obj[10].toString().trim()))) {
                unitImportDTO.setStatusStr(obj[10].toString().trim());
                if ("Active".equals(obj[10].toString().trim())) {
                  unitImportDTO.setStatus(1l);
                }
                if ("Inactive".equals(obj[10].toString().trim())) {
                  unitImportDTO.setStatus(0l);
                }
              } else {
                unitImportDTO.setStatusStr(obj[10].toString().trim());
                unitImportDTO.setResultImport(
                    unitImportDTO.getResultImport() + I18n.getLanguage("UnitCommon.statusStr") + " "
                        +
                        I18n.getLanguage("unit.import.status.value") + ";");
                hasErr = Boolean.valueOf(true);
              }
            } else {
              unitImportDTO.setResultImport(
                  unitImportDTO.getResultImport() + I18n.getLanguage("UnitCommon.statusStr") + " " +
                      I18n.getLanguage("unit.import.must.fill") + ";");
              hasErr = Boolean.valueOf(true);
            }

//            if (hasErr.booleanValue()) {
//              unitImportDTO.setResultImport(errStr.toString());
//            }

            if (mapDuplicate
                .get(unitImportDTO.getUnitCode().trim() + unitImportDTO.getUnitName().trim())
                == null) {
              mapDuplicate
                  .put(unitImportDTO.getUnitCode().trim() + unitImportDTO.getUnitName().trim(),
                      unitImportDTO.getUnitName());
            } else {
              unitImportDTO.setResultImport(unitImportDTO.getResultImport() + I18n
                  .getLanguage("languageExchange.err.duplicate") + ";");
              hasErr = Boolean.valueOf(true);
            }

            unitImportDTOs.add(unitImportDTO);
          }
          //check trung ma

          if (hasErr) {
            for (UnitDTO item : unitImportDTOs) {
              if (StringUtils.isStringNullOrEmpty(item.getResultImport())) {
                item.setResultImport(I18n.getLanguage("unit.record.import"));
              }
            }

            File expFile = exportFile(unitImportDTOs, "RESULT_IMPORT", true);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(expFile);
          } else {
            //insert update
            //Chuyen class de update va insert
            List<UnitDTO> unitDTOs = new ArrayList<>();
            UnitDTO importDTO;
            String content = "";

            for (UnitDTO transferDTO : unitImportDTOs) {
              importDTO = new UnitDTO();
              if (transferDTO.getUnitId() != null && !"".equals(transferDTO.getUnitId())) {
                importDTO.setUnitId(transferDTO.getUnitId());
              }
              if (transferDTO.getUnitCode() != null && !"".equals(transferDTO.getUnitCode())) {
                importDTO.setUnitCode(transferDTO.getUnitCode());
              }
              if (transferDTO.getUnitName() != null && !"".equals(transferDTO.getUnitName())) {
                importDTO.setUnitName(transferDTO.getUnitName());
              }
              if (transferDTO.getParentUnitId() != null && !""
                  .equals(transferDTO.getParentUnitId())) {
                importDTO.setParentUnitId(transferDTO.getParentUnitId());
              }
              if (transferDTO.getDescription() != null && !""
                  .equals(transferDTO.getDescription())) {
                importDTO.setDescription(transferDTO.getDescription());
              }
              if (transferDTO.getUnitType() != null && !"".equals(transferDTO.getUnitType())) {
                importDTO.setUnitType(transferDTO.getUnitType());
              }
              if (transferDTO.getUnitLevel() != null && !"".equals(transferDTO.getUnitLevel())) {
                importDTO.setUnitLevel(transferDTO.getUnitLevel());
              }
              if (transferDTO.getLocationId() != null && !"".equals(transferDTO.getLocationId())) {
                importDTO.setLocationId(transferDTO.getLocationId());
              }
              if (transferDTO.getSmsGatewayId() != null && !""
                  .equals(transferDTO.getSmsGatewayId())) {
                importDTO.setSmsGatewayId(transferDTO.getSmsGatewayId());
              }
              if (transferDTO.getIpccServiceId() != null && !""
                  .equals(transferDTO.getIpccServiceId())) {
                importDTO.setIpccServiceId(transferDTO.getIpccServiceId());
              }
              if (transferDTO.getIsNoc() != null && !"".equals(transferDTO.getIsNoc())) {
                if ("Yes".equals(transferDTO.getIsNoc())) {
                  importDTO.setIsNoc(1l);
                } else {
                  importDTO.setIsNoc(0l);
                }

              }
              if (transferDTO.getStatus() != null && !"".equals(transferDTO.getStatus())) {
                importDTO.setStatus(transferDTO.getStatus());
              }
              unitDTOs.add(importDTO);
              content = content + importDTO.getUnitName();
            }

            for (UnitDTO item : unitImportDTOs) {
              resultInSideDto = insertUnit(item);
            }
            if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
              commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
                  "Import Unit", "Unit:" + resultInSideDto.getId(),
                  null, null));
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListUnitDatatableAll(UnitDTO unitDTO) {
    return unitCommonRepository.getListUnitDatatableAll(unitDTO);
  }

  @Override
  public Datatable getListLocationAll(CatLocationDTO catLocationDTO) {
    return catLocationRepository.getLocationDatapicker(catLocationDTO);
  }

  private boolean validFileFormat(List<Object[]> headerList) {
    Object[] objects = headerList.get(0);
    if (objects == null) {
      return false;
    }
    int count = 0;
    for (Object data : objects) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 13) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("UnitCommon.unitCode") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("UnitCommon.unitName") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    return true;
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = Arrays.asList("unitId", "unitName", "unitCode", "parentUnitId", "description", "status",
          "unitType", "unitLevel", "locationId", "isNoc", "timeZone", "isCommittee", "updateTime", "smsGatewayId",
          "ipccServiceId", "nationCode", "nationId", "mobile", "roleType", "email");
      return keys;
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
