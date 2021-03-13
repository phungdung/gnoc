package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.CfgFileCreateWoDTO;
import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
import com.viettel.gnoc.wo.dto.WoTypeCheckListDTO;
import com.viettel.gnoc.wo.dto.WoTypeFilesGuideDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeTimeDTO;
import com.viettel.gnoc.wo.repository.WoPriorityRepository;
import com.viettel.gnoc.wo.repository.WoTypeCfgRequiredRepository;
import com.viettel.gnoc.wo.repository.WoTypeCheckListRepository;
import com.viettel.gnoc.wo.repository.WoTypeRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
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
public class WoTypeBusinessImpl implements WoTypeBusiness {

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

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Autowired
  WoTypeRepository woTypeRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  WoTypeCheckListRepository woTypeCheckListRepository;

  @Autowired
  WoTypeCfgRequiredRepository woTypeCfgRequiredRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  WoPriorityRepository woPriorityRepository;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  List<WoTypeInsideDTO> woTypeInsideDTOArrayList = new ArrayList<>();
  Map<String, String> mapWoGroupTypeName = new HashMap<>();
  private final static String WO_TYPE_RESULT_IMPORT = "WO_TYPE_RESULT_IMPORT";
  private final static String WO_TYPE_EXPORT = "WO_TYPE_EXPORT";

  @Override
  public Datatable getListWoTypeByLocalePage(WoTypeInsideDTO woTypeInsideDTO) {
    log.debug("Request to getListWoTypeByLocalePage : {}", woTypeInsideDTO);
    return woTypeRepository.getListWoTypeByLocalePage(woTypeInsideDTO);
  }

  @Override
  public ResultInSideDto delete(Long woTypeId) {
    log.debug("Request to delete : {}", woTypeId);
    ResultInSideDto resultInSideDto;
//    ResultInSideDto resultInSideDto = new ResultInSideDto();
//    List<WoInsideDTO> listWoInsideDTO = woServiceProxy.getListWoByWoTypeId(woTypeId);
//    if (listWoInsideDTO == null || listWoInsideDTO.size() == 0) {
    WoTypeInsideDTO oldHis = findByWoTypeId(woTypeId);
    resultInSideDto = woTypeRepository.delete(woTypeId);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(woTypeId.toString());
        dataHistoryChange.setType("WO_TYPE_MANAGEMENT");
        dataHistoryChange.setActionType("delete");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new WoTypeInsideDTO());
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
//    } else {
//      resultInSideDto.setKey(RESULT.DELETE_REQUIRE_EXIST);
//      resultInSideDto.setMessage(I18n.getLanguage("woType.delete.wo.exist"));
//    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListDataExport(WoTypeInsideDTO woTypeInsideDTO) {
    log.debug("Request to getListDataExport : {}", woTypeInsideDTO);
    return woTypeRepository.getListDataExport(woTypeInsideDTO);
  }

  public void setMapWoGroupTypeName() {
    Datatable woGroupTypeMaster = catItemRepository
        .getItemMaster("WO_GROUP_TYPE", LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), "itemId", "itemName");
    List<CatItemDTO> itemDTOList = (List<CatItemDTO>) woGroupTypeMaster.getData();
    if (itemDTOList != null && !itemDTOList.isEmpty()) {
      for (CatItemDTO catItemDTO : itemDTOList) {
        mapWoGroupTypeName.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
  }


  @Override
  public File getWoTypeTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("woType.woTypeCode"),
        I18n.getLanguage("woType.woTypeName"),
        I18n.getLanguage("woType.woGroupTypeName"),
        I18n.getLanguage("woType.isEnableName"),
        I18n.getLanguage("woType.enableCreateStr"),
        I18n.getLanguage("woType.allowPendingStr"),
        I18n.getLanguage("woType.createFromOtherSysStr"),
        I18n.getLanguage("woType.action")
    };

    String[] headerStar = new String[]{
        I18n.getLanguage("woType.woTypeCode"),
        I18n.getLanguage("woType.woTypeName"),
        I18n.getLanguage("woType.woGroupTypeName"),
        I18n.getLanguage("woType.isEnableName"),
        I18n.getLanguage("woType.enableCreateStr"),
        I18n.getLanguage("woType.allowPendingStr"),
        I18n.getLanguage("woType.createFromOtherSysStr"),
        I18n.getLanguage("woType.action")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int woGroupTypeNameColumn = listHeader.indexOf(I18n.getLanguage("woType.woGroupTypeName"));
    int isEnableNameColumn = listHeader.indexOf(I18n.getLanguage("woType.isEnableName"));
    int enableCreateStrColumn = listHeader.indexOf(I18n.getLanguage("woType.enableCreateStr"));
    int allowPendingStrColumn = listHeader.indexOf(I18n.getLanguage("woType.allowPendingStr"));
    int createFromOtherSysStrColumn = listHeader
        .indexOf(I18n.getLanguage("woType.createFromOtherSysStr"));
    int actionColumn = listHeader.indexOf(I18n.getLanguage("woType.action"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(0);
    titleRow.setHeightInPoints(30);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("woType.title"));
    titleCell.setCellStyle(style.get("title"));
    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    Row headerRow = sheetOne.createRow(4);
    headerRow.setHeightInPoints(16);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }

    sheetOne.setColumnWidth(0, 3000);

    int row = 5;

    Datatable woGroupTypeMaster = catItemRepository
        .getItemMaster("WO_GROUP_TYPE", LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), "itemId", "itemName");
    List<CatItemDTO> woGroupTypeNameList = (List<CatItemDTO>) woGroupTypeMaster.getData();
    for (CatItemDTO dto : woGroupTypeNameList) {
      excelWriterUtils.createCell(sheetParam, 3, row++, dto.getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);
    Name woGroupTypeName = workbook.createName();
    woGroupTypeName.setNameName("woGroupTypeName");
    woGroupTypeName.setRefersToFormula("param!$D$2:$D$" + row);
    XSSFDataValidationConstraint woGroupTypeNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "woGroupTypeName");
    CellRangeAddressList woGroupTypeNameCreate = new CellRangeAddressList(5, 65000,
        woGroupTypeNameColumn,
        woGroupTypeNameColumn);
    XSSFDataValidation dataValidationWoGroupTypeName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            woGroupTypeNameConstraint, woGroupTypeNameCreate);
    dataValidationWoGroupTypeName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationWoGroupTypeName);

    row = 5;

    excelWriterUtils.createCell(sheetParam, 4, row++, I18n.getLanguage("woType.isEnable.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 4, row++, I18n.getLanguage("woType.isEnable.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name isEnableName = workbook.createName();
    isEnableName.setNameName("isEnableName");
    isEnableName.setRefersToFormula("param!$E$2:$E$" + row);
    XSSFDataValidationConstraint isEnableNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isEnableName");
    CellRangeAddressList isEnableNameCreate = new CellRangeAddressList(5, 65000, isEnableNameColumn,
        isEnableNameColumn);
    XSSFDataValidation dataValidationIsEnableName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isEnableNameConstraint, isEnableNameCreate);
    dataValidationIsEnableName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationIsEnableName);

    row = 5;

    excelWriterUtils.createCell(sheetParam, 5, row++, I18n.getLanguage("woType.enableCreate.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 5, row++, I18n.getLanguage("woType.enableCreate.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name enableCreateStr = workbook.createName();
    enableCreateStr.setNameName("enableCreateStr");
    enableCreateStr.setRefersToFormula("param!$F$2:$F$" + row);
    XSSFDataValidationConstraint enableCreateStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "enableCreateStr");
    CellRangeAddressList enableCreateStrCreate = new CellRangeAddressList(5, 65000,
        enableCreateStrColumn,
        enableCreateStrColumn);
    XSSFDataValidation dataValidationEnableCreateStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            enableCreateStrConstraint, enableCreateStrCreate);
    dataValidationEnableCreateStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationEnableCreateStr);

    row = 5;

    excelWriterUtils.createCell(sheetParam, 6, row++, I18n.getLanguage("woType.allowPending.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 6, row++, I18n.getLanguage("woType.allowPending.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name allowPendingStr = workbook.createName();
    allowPendingStr.setNameName("allowPendingStr");
    allowPendingStr.setRefersToFormula("param!$G$2:$G$" + row);
    XSSFDataValidationConstraint allowPendingStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "allowPendingStr");
    CellRangeAddressList allowPendingStrCreate = new CellRangeAddressList(5, 65000,
        allowPendingStrColumn,
        allowPendingStrColumn);
    XSSFDataValidation dataValidationAllowPendingStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            allowPendingStrConstraint, allowPendingStrCreate);
    dataValidationAllowPendingStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationAllowPendingStr);

    row = 5;

    excelWriterUtils
        .createCell(sheetParam, 7, row++, I18n.getLanguage("woType.createFromOtherSys.1")
            , style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam, 7, row++, I18n.getLanguage("woType.createFromOtherSys.0")
            , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name createFromOtherSysStr = workbook.createName();
    createFromOtherSysStr.setNameName("createFromOtherSysStr");
    createFromOtherSysStr.setRefersToFormula("param!$H$2:$H$" + row);
    XSSFDataValidationConstraint createFromOtherSysStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "createFromOtherSysStr");
    CellRangeAddressList createFromOtherSysStrCreate = new CellRangeAddressList(5, 65000,
        createFromOtherSysStrColumn,
        createFromOtherSysStrColumn);
    XSSFDataValidation dataValidationCreateFromOtherSysStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            createFromOtherSysStrConstraint, createFromOtherSysStrCreate);
    dataValidationCreateFromOtherSysStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationCreateFromOtherSysStr);

    row = 5;

    excelWriterUtils.createCell(sheetParam, 8, row++, I18n.getLanguage("woType.action.insert"),
        style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 8, row++, I18n.getLanguage("woType.action.update"),
        style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name action = workbook.createName();
    action.setNameName("action");
    action.setRefersToFormula("param!$I$2:$I$" + row);
    XSSFDataValidationConstraint actionConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "action");
    CellRangeAddressList actionCreate = new CellRangeAddressList(5, 65000, actionColumn,
        actionColumn);
    XSSFDataValidation dataValidationAction = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            actionConstraint, actionCreate);
    dataValidationAction.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationAction);

    workbook.setSheetName(0, I18n.getLanguage("woType.title"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_WO_TYPE" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File exportData(WoTypeInsideDTO woTypeInsideDTO) throws Exception {
    List<WoTypeInsideDTO> woTypeInsideDTOList = (List<WoTypeInsideDTO>) woTypeRepository
        .getListDataExport(
            woTypeInsideDTO)
        .getData();
    if (woTypeInsideDTOList != null && !woTypeInsideDTOList.isEmpty()) {
      for (int i = woTypeInsideDTOList.size() - 1; i > -1; i--) {
        WoTypeInsideDTO woTypeExport = woTypeInsideDTOList.get(i);
        if ("1".equals(String.valueOf(woTypeExport.getIsEnable()))) {
          woTypeExport.setIsEnableName(I18n.getLanguage("woType.isEnable.1"));
        } else if ("0".equals(String.valueOf(woTypeExport.getIsEnable()))) {
          woTypeExport.setIsEnableName(I18n.getLanguage("woType.isEnable.0"));
        }

        if ("1".equals(String.valueOf(woTypeExport.getEnableCreate()))) {
          woTypeExport.setEnableCreateStr(I18n.getLanguage("woType.enableCreate.1"));
        } else if ("0".equals(String.valueOf(woTypeExport.getEnableCreate()))) {
          woTypeExport.setEnableCreateStr(I18n.getLanguage("woType.enableCreate.0"));
        }
        if ("1".equals(String.valueOf(woTypeExport.getAllowPending()))) {
          woTypeExport.setAllowPendingStr(I18n.getLanguage("woType.allowPending.1"));
        } else if ("0".equals(String.valueOf(woTypeExport.getAllowPending()))) {
          woTypeExport.setAllowPendingStr(I18n.getLanguage("woType.allowPending.0"));
        }
        if ("1".equals(String.valueOf(woTypeExport.getCreateFromOtherSys()))) {
          woTypeExport.setCreateFromOtherSysStr(I18n.getLanguage("woType.createFromOtherSys.1"));
        } else if ("0".equals(String.valueOf(woTypeExport.getCreateFromOtherSys()))) {
          woTypeExport.setCreateFromOtherSysStr(I18n.getLanguage("woType.createFromOtherSys.0"));
        }
      }
    }
    return exportFileTemplate(woTypeInsideDTOList, "");
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<WoTypeInsideDTO> exportDTOList = new ArrayList<>();
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(RESULT.SUCCESS);
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultDto.setKey(RESULT.FILE_IS_NULL);
        return resultDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultDto.getKey())) {
          return resultDto;
        }
        File fileImport = new File(filePath);

        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            8,
            1000
        );
        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultDto;
        }
        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            8,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultDto.setKey(RESULT.DATA_OVER);
          return resultDto;
        }

        woTypeInsideDTOArrayList = new ArrayList<>();

        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          setMapWoGroupTypeName();
          for (Object[] obj : dataImportList) {
            WoTypeInsideDTO woTypeInsideDTO = new WoTypeInsideDTO();
            if (obj[1] != null) {
              woTypeInsideDTO.setWoTypeCode(obj[1].toString().trim().toUpperCase());
            } else {
              woTypeInsideDTO.setWoTypeCode(null);
            }
            if (obj[2] != null) {
              woTypeInsideDTO.setWoTypeName(obj[2].toString().trim());
            } else {
              woTypeInsideDTO.setWoTypeName(null);
            }
            if (obj[3] != null) {
              woTypeInsideDTO.setWoGroupTypeName(obj[3].toString().trim());
              for (Map.Entry<String, String> item : mapWoGroupTypeName.entrySet()) {
                if (woTypeInsideDTO.getWoGroupTypeName().equals(item.getValue())) {
                  woTypeInsideDTO.setWoGroupType(Long.valueOf(item.getKey()));
                  break;
                } else {
                  woTypeInsideDTO.setWoGroupType(null);
                }
              }
            } else {
              woTypeInsideDTO.setWoGroupTypeName(null);
            }
            if (obj[4] != null) {
              woTypeInsideDTO.setIsEnableName(obj[4].toString().trim());
              if (I18n.getLanguage("woType.isEnable.1").equals(woTypeInsideDTO.getIsEnableName())) {
                woTypeInsideDTO.setIsEnable(Long.valueOf(1));
              } else if (I18n.getLanguage("woType.isEnable.0")
                  .equals(woTypeInsideDTO.getIsEnableName())) {
                woTypeInsideDTO.setIsEnable(Long.valueOf(0));
              } else {
                woTypeInsideDTO.setIsEnable(null);
              }
            } else {
              woTypeInsideDTO.setIsEnableName(null);
            }
            if (obj[5] != null) {
              woTypeInsideDTO.setEnableCreateStr(obj[5].toString().trim());
              if (I18n.getLanguage("woType.enableCreate.1")
                  .equals(woTypeInsideDTO.getEnableCreateStr())) {
                woTypeInsideDTO.setEnableCreate(Long.valueOf(1));
              } else if (I18n.getLanguage("woType.enableCreate.0")
                  .equals(woTypeInsideDTO.getEnableCreateStr())) {
                woTypeInsideDTO.setEnableCreate(Long.valueOf(0));
              } else {
                woTypeInsideDTO.setEnableCreate(null);
              }
            } else {
              woTypeInsideDTO.setEnableCreateStr(null);
            }
            if (obj[6] != null) {
              woTypeInsideDTO.setAllowPendingStr(obj[6].toString().trim());
              if (I18n.getLanguage("woType.allowPending.1")
                  .equals(woTypeInsideDTO.getAllowPendingStr())) {
                woTypeInsideDTO.setAllowPending(Long.valueOf(1));
              } else if (I18n.getLanguage("woType.allowPending.0")
                  .equals(woTypeInsideDTO.getAllowPendingStr())) {
                woTypeInsideDTO.setAllowPending(Long.valueOf(0));
              } else {
                woTypeInsideDTO.setAllowPending(null);
              }
            } else {
              woTypeInsideDTO.setAllowPendingStr(null);
            }
            if (obj[7] != null) {
              woTypeInsideDTO.setCreateFromOtherSysStr(obj[7].toString().trim());
              if (I18n.getLanguage("woType.createFromOtherSys.1")
                  .equals(woTypeInsideDTO.getCreateFromOtherSysStr())) {
                woTypeInsideDTO.setCreateFromOtherSys(Long.valueOf(1));
              } else if (I18n.getLanguage("woType.createFromOtherSys.0")
                  .equals(woTypeInsideDTO.getCreateFromOtherSysStr())) {
                woTypeInsideDTO.setCreateFromOtherSys(Long.valueOf(0));
              } else {
                woTypeInsideDTO.setCreateFromOtherSys(null);
              }
            } else {
              woTypeInsideDTO.setCreateFromOtherSysStr(null);
            }
            if (obj[8] != null) {
              woTypeInsideDTO.setAction(obj[8].toString().trim());
              if (!(I18n.getLanguage("woType.action.1")
                  .equals(woTypeInsideDTO.getAction()) || I18n.getLanguage("woType.action.0")
                  .equals(woTypeInsideDTO.getAction()))) {
                woTypeInsideDTO.setAction(null);
              }
            } else {
              woTypeInsideDTO.setAction(null);
              woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.action"));
            }
            WoTypeInsideDTO woTypeInsideDTOTmp = validateImportInfo(woTypeInsideDTO, exportDTOList);
            if (woTypeInsideDTOTmp.getWoTypeId() != null) {
              woTypeInsideDTO.setWoTypeId(woTypeInsideDTOTmp.getWoTypeId());
            }
            if ("1".equals(woTypeInsideDTOTmp.getValidate())) {
              woTypeInsideDTO.setValidate("1");
            }
            if (woTypeInsideDTOTmp.getResultImport() == null) {
              woTypeInsideDTOTmp.setResultImport(I18n.getLanguage("woType.result.import"));
              exportDTOList.add(woTypeInsideDTOTmp);
              woTypeInsideDTOArrayList.add(woTypeInsideDTO);
            } else {
              exportDTOList.add(woTypeInsideDTOTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!woTypeInsideDTOArrayList.isEmpty()) {
              resultDto = woTypeRepository.insertListWoType(woTypeInsideDTOArrayList);
            }
          } else {
            File fileExport = exportFileTemplate(exportDTOList, Constants.RESULT_IMPORT);
            resultDto.setKey(RESULT.ERROR);
            resultDto.setFile(fileExport);
          }
        } else {
          resultDto.setKey(RESULT.NODATA);
          resultDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileTemplate(exportDTOList, Constants.RESULT_IMPORT);
          resultDto.setFile(fileExport);
          return resultDto;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDto.setKey(RESULT.ERROR);
      resultDto.setMessage(e.getMessage());
    }
    return resultDto;
  }

  @Override
  public WoTypeInsideDTO findByWoTypeId(Long woTypeId) {
    WoTypeInsideDTO woTypeInsideDTO = woTypeRepository.findByWoTypeId(woTypeId);
    woTypeInsideDTO.setWoPriorityDTOList(woPriorityRepository.findAllByWoTypeID(woTypeId));
    woTypeInsideDTO
        .setWoTypeCfgRequiredDTOList(woTypeCfgRequiredRepository.findAllByWoTypeID(woTypeId));
    woTypeInsideDTO
        .setWoTypeCheckListDTOList(woTypeCheckListRepository.findAllByWoTypeID(woTypeId));
    GnocFileDto gnocFileCreateWoDto = new GnocFileDto();
    gnocFileCreateWoDto.setBusinessCode(GNOC_FILE_BUSSINESS.CFG_FILE_CREATE_WO);
    gnocFileCreateWoDto.setBusinessId(woTypeId);
    woTypeInsideDTO
        .setGnocFileCreateWoDtos(gnocFileRepository.getListGnocFileByDto(gnocFileCreateWoDto));
    GnocFileDto gnocFilesGuideDto = new GnocFileDto();
    gnocFilesGuideDto.setBusinessCode(GNOC_FILE_BUSSINESS.WO_TYPE_FILES_GUIDE);
    gnocFilesGuideDto.setBusinessId(woTypeId);
    woTypeInsideDTO
        .setGnocFilesGuideDtos(gnocFileRepository.getListGnocFileByDto(gnocFilesGuideDto));
    return woTypeInsideDTO;
  }


  @Override
  public ResultInSideDto insert(List<MultipartFile> filesGuideline,
      List<MultipartFile> filesAttached, WoTypeInsideDTO woTypeInsideDTO) throws IOException {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (StringUtils.isNotNullOrEmpty(woTypeInsideDTO.getWoCloseAutomaticTime())) {
      Long woCloseAutomaticTime = Long.valueOf(woTypeInsideDTO.getWoCloseAutomaticTime());
      woTypeInsideDTO.setWoCloseAutomaticTime(String.valueOf(woCloseAutomaticTime));
    }
    resultInSideDto = woTypeRepository.add(woTypeInsideDTO);
    Long woTypeIdTmp = resultInSideDto.getId();

    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      UserToken userToken = ticketProvider.getUserToken();
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      List<GnocFileDto> gnocFilesGuideDtos = new ArrayList<>();
      for (MultipartFile multipartFile : filesGuideline) {
        Date date = new Date();
        String fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
            multipartFile.getBytes(), date);
        String fullPathOld = FileUtils
            .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                uploadFolder, date);
        String fileName = multipartFile.getOriginalFilename();
        //Start save file old
        WoTypeFilesGuideDTO woTypeFilesGuideDTO = new WoTypeFilesGuideDTO();
        woTypeFilesGuideDTO.setFilePath(FileUtils.getFilePath(fullPathOld));
        woTypeFilesGuideDTO.setFileName(FileUtils.getFileName(fullPathOld));
        woTypeFilesGuideDTO.setWoTypeId(woTypeIdTmp);
        ResultInSideDto resultFileDataOld = woTypeRepository
            .addWoTypeFilesGuide(woTypeFilesGuideDTO);
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(fileName);
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(new Date());
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFilesGuideDtos.add(gnocFileDto);
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO_TYPE_FILES_GUIDE, woTypeIdTmp,
              gnocFilesGuideDtos);

      List<WoTypeCfgRequiredDTO> woTypeCfgRequiredDTOList = woTypeInsideDTO
          .getWoTypeCfgRequiredDTOList();
      if (woTypeCfgRequiredDTOList != null && woTypeCfgRequiredDTOList.size() > 0) {
        for (WoTypeCfgRequiredDTO woTypeCfgRequiredDTO : woTypeCfgRequiredDTOList) {
          woTypeCfgRequiredDTO.setWoTypeId(woTypeIdTmp);
          resultInSideDto = woTypeCfgRequiredRepository.add(woTypeCfgRequiredDTO);
        }
      }

      for (GnocFileDto gnocFileDto : woTypeInsideDTO.getGnocFileCreateWoDtos()) {
        if (gnocFileDto.getIndexFile() != null) {
          gnocFileDto.setMultipartFile(filesAttached.get(gnocFileDto.getIndexFile().intValue()));
        }
      }
      List<GnocFileDto> gnocFileCreateWoDtos = new ArrayList<>();
      for (GnocFileDto gnocFileCreateWoDto : woTypeInsideDTO.getGnocFileCreateWoDtos()) {
        CfgFileCreateWoDTO cfgFileCreateWoDTO = new CfgFileCreateWoDTO();
        MultipartFile multipartFile = gnocFileCreateWoDto.getMultipartFile();
        if (multipartFile != null) {
          Date date = new Date();
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                  multipartFile.getBytes(), date);
          String fullPathOld = FileUtils
              .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  uploadFolder, date);
          String fileName = gnocFileCreateWoDto.getFileName();
          //Start save file old
          cfgFileCreateWoDTO.setWoTypeId(woTypeIdTmp);
          cfgFileCreateWoDTO.setFilePath(fullPathOld);
          cfgFileCreateWoDTO.setFileName(fileName);
          cfgFileCreateWoDTO.setRequired(gnocFileCreateWoDto.getRequired());
          ResultInSideDto resultFileDataOld = woTypeRepository
              .addCfgFileCreateWo(cfgFileCreateWoDTO);
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(fileName);
          gnocFileDto.setCreateUnitId(userToken.getDeptId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(userToken.getUserID());
          gnocFileDto.setCreateUserName(userToken.getUserName());
          gnocFileDto.setCreateTime(new Date());
          gnocFileDto.setRequired(gnocFileCreateWoDto.getRequired());
          gnocFileDto.setMappingId(resultFileDataOld.getId());
          gnocFileCreateWoDtos.add(gnocFileDto);
        } else {
          cfgFileCreateWoDTO.setWoTypeId(woTypeIdTmp);
          cfgFileCreateWoDTO.setFileName(gnocFileCreateWoDto.getFileName());
          cfgFileCreateWoDTO.setRequired(gnocFileCreateWoDto.getRequired());
          ResultInSideDto resultFileDataOld = woTypeRepository
              .addCfgFileCreateWo(cfgFileCreateWoDTO);
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(null);
          gnocFileDto.setFileName(gnocFileCreateWoDto.getFileName());
          gnocFileDto.setCreateUnitId(userToken.getDeptId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(userToken.getUserID());
          gnocFileDto.setCreateUserName(userToken.getUserName());
          gnocFileDto.setCreateTime(new Date());
          gnocFileDto.setRequired(gnocFileCreateWoDto.getRequired());
          gnocFileDto.setMappingId(resultFileDataOld.getId());
          gnocFileCreateWoDtos.add(gnocFileDto);
        }
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CFG_FILE_CREATE_WO, woTypeIdTmp,
              gnocFileCreateWoDtos);

      List<WoTypeCheckListDTO> woTypeCheckListDTOList = woTypeInsideDTO.getWoTypeCheckListDTOList();
      if (woTypeCheckListDTOList != null && woTypeCheckListDTOList.size() > 0) {
        for (WoTypeCheckListDTO woTypeCheckListDTO : woTypeCheckListDTOList) {
          woTypeCheckListDTO.setWoTypeId(woTypeIdTmp);
          resultInSideDto = woTypeCheckListRepository.add(woTypeCheckListDTO);
        }
      }
      // Add history
      try {
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(woTypeIdTmp.toString());
        dataHistoryChange.setType("WO_TYPE_MANAGEMENT");
        dataHistoryChange.setActionType("add");
        //Old Object History
        dataHistoryChange.setOldObject(new WoTypeInsideDTO());
        //New Object History
        dataHistoryChange.setNewObject(woTypeInsideDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(List<MultipartFile> filesGuideline,
      List<MultipartFile> filesAttached, WoTypeInsideDTO woTypeInsideDTO) throws IOException {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (StringUtils.isNotNullOrEmpty(woTypeInsideDTO.getWoCloseAutomaticTime())) {
      Long woCloseAutomaticTime = Long.valueOf(woTypeInsideDTO.getWoCloseAutomaticTime());
      woTypeInsideDTO.setWoCloseAutomaticTime(String.valueOf(woCloseAutomaticTime));
    }
    WoTypeInsideDTO oldHis = findByWoTypeId(woTypeInsideDTO.getWoTypeId());
    resultInSideDto = woTypeRepository.add(woTypeInsideDTO);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      UserToken userToken = ticketProvider.getUserToken();
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      //Start save file old
//      List<WoTypeFilesGuideEntity> lstFileOldFilesGuide = woTypeRepository
//          .getListFilesGuideByWoTypeId(woTypeInsideDTO.getWoTypeId());
//      List<Long> listIdFileIdOldFilesGuide = lstFileOldFilesGuide.stream()
//          .map(WoTypeFilesGuideEntity::getWoTypeFilesGuideId).collect(Collectors.toList());
//      List<GnocFileDto> gnocFileDtosWebFilesGuide = woTypeInsideDTO.getGnocFilesGuideDtos();
//      List<Long> listIdFileIdNewFilesGuide = gnocFileDtosWebFilesGuide.stream()
//          .map(GnocFileDto::getMappingId).collect(Collectors.toList());
//      listIdFileIdOldFilesGuide.removeAll(listIdFileIdNewFilesGuide);
      for (Long aLong : woTypeInsideDTO.getIdDeleteListFilesGuide()) {
        woTypeRepository.deleteWoTypeFilesGuide(aLong);
      }
      //End save file old
      List<GnocFileDto> gnocFilesGuideDtos = new ArrayList<>();
      for (MultipartFile multipartFile : filesGuideline) {
        Date date = new Date();
        String fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
            multipartFile.getBytes(), date);
        String fullPathOld = FileUtils
            .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                uploadFolder, date);
        String fileName = multipartFile.getOriginalFilename();
        //Start save file old
        WoTypeFilesGuideDTO woTypeFilesGuideDTO = new WoTypeFilesGuideDTO();
        woTypeFilesGuideDTO.setFilePath(FileUtils.getFilePath(fullPathOld));
        woTypeFilesGuideDTO.setFileName(FileUtils.getFileName(fullPathOld));
        woTypeFilesGuideDTO.setWoTypeId(woTypeInsideDTO.getWoTypeId());
        ResultInSideDto resultFileDataOld = woTypeRepository
            .addWoTypeFilesGuide(woTypeFilesGuideDTO);
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(fileName);
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(new Date());
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFilesGuideDtos.add(gnocFileDto);
      }
//      List<GnocFileDto> gnocFilesGuideDtosAll = new ArrayList<>();
//      gnocFilesGuideDtosAll.addAll(gnocFilesGuideDtos);
//      gnocFilesGuideDtosAll.addAll(woTypeInsideDTO.getGnocFilesGuideDtos());
      gnocFileRepository.deleteListGnocFile(GNOC_FILE_BUSSINESS.WO_TYPE_FILES_GUIDE,
          woTypeInsideDTO.getWoTypeId(), woTypeInsideDTO.getIdDeleteListFilesGuide());
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO_TYPE_FILES_GUIDE,
              woTypeInsideDTO.getWoTypeId(),
              gnocFilesGuideDtos);

      List<WoTypeCfgRequiredDTO> woTypeCfgRequiredDTOList = woTypeInsideDTO
          .getWoTypeCfgRequiredDTOList();
      if (woTypeCfgRequiredDTOList != null && woTypeCfgRequiredDTOList.size() > 0) {
        for (WoTypeCfgRequiredDTO woTypeCfgRequiredDTO : woTypeCfgRequiredDTOList) {
          woTypeCfgRequiredDTO.setWoTypeId(woTypeInsideDTO.getWoTypeId());
          resultInSideDto = woTypeCfgRequiredRepository.add(woTypeCfgRequiredDTO);
        }
      }

      //Start save file old
//      List<CfgFileCreateWoEntity> lstFileOldFileCreate = woTypeRepository
//          .getListFileCreateByWoTypeId(woTypeInsideDTO.getWoTypeId());
//      List<Long> listIdFileIdOldFileCreate = lstFileOldFileCreate.stream()
//          .map(CfgFileCreateWoEntity::getCfgFileCreateWoId).collect(Collectors.toList());
//      List<Long> listIdFileIdNewFileCreate = new ArrayList<>();
//      for (GnocFileDto gnocFileDto : woTypeInsideDTO.getGnocFileCreateWoDtos()) {
//        if (gnocFileDto.getMappingId() != null) {
//          listIdFileIdNewFileCreate.add(gnocFileDto.getMappingId());
//        }
//      }
//      listIdFileIdOldFileCreate.removeAll(listIdFileIdNewFileCreate);
      for (Long aLong : woTypeInsideDTO.getIdDeleteListFileCreateWo()) {
        woTypeRepository.deleteCfgFileCreateWo(aLong);
      }
      //End save file old
      List<GnocFileDto> gnocFileCreateWoDtosAdd = woTypeInsideDTO.getGnocFileCreateWoDtos();
      for (GnocFileDto gnocFileDto : gnocFileCreateWoDtosAdd) {
        if (gnocFileDto.getIndexFile() != null) {
          gnocFileDto.setMultipartFile(filesAttached.get(gnocFileDto.getIndexFile().intValue()));
        }
      }
      List<GnocFileDto> gnocFileCreateWoDtos = new ArrayList<>();
      for (GnocFileDto gnocFileCreateWoDto : gnocFileCreateWoDtosAdd) {
        if (gnocFileCreateWoDto.getId() == null) {
          CfgFileCreateWoDTO cfgFileCreateWoDTO = new CfgFileCreateWoDTO();
          MultipartFile multipartFile = gnocFileCreateWoDto.getMultipartFile();
          if (multipartFile != null) {
            Date date = new Date();
            String fullPath = FileUtils
                .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                    multipartFile.getBytes(), date);
            String fullPathOld = FileUtils
                .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                    uploadFolder, date);
            String fileName = gnocFileCreateWoDto.getFileName();
            //Start save file old
            cfgFileCreateWoDTO.setWoTypeId(woTypeInsideDTO.getWoTypeId());
            cfgFileCreateWoDTO.setFilePath(fullPathOld);
            cfgFileCreateWoDTO.setFileName(fileName);
            cfgFileCreateWoDTO.setRequired(gnocFileCreateWoDto.getRequired());
            ResultInSideDto resultFileDataOld = woTypeRepository
                .addCfgFileCreateWo(cfgFileCreateWoDTO);
            //End save file old
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(fullPath);
            gnocFileDto.setFileName(fileName);
            gnocFileDto.setCreateUnitId(userToken.getDeptId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(new Date());
            gnocFileDto.setRequired(gnocFileCreateWoDto.getRequired());
            gnocFileDto.setMappingId(resultFileDataOld.getId());
            gnocFileCreateWoDtos.add(gnocFileDto);
          } else {
            cfgFileCreateWoDTO.setWoTypeId(woTypeInsideDTO.getWoTypeId());
            cfgFileCreateWoDTO.setFileName(gnocFileCreateWoDto.getFileName());
            cfgFileCreateWoDTO.setRequired(gnocFileCreateWoDto.getRequired());
            ResultInSideDto resultFileDataOld = woTypeRepository
                .addCfgFileCreateWo(cfgFileCreateWoDTO);
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(null);
            gnocFileDto.setFileName(gnocFileCreateWoDto.getFileName());
            gnocFileDto.setCreateUnitId(userToken.getDeptId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(new Date());
            gnocFileDto.setRequired(gnocFileCreateWoDto.getRequired());
            gnocFileDto.setMappingId(resultFileDataOld.getId());
            gnocFileCreateWoDtos.add(gnocFileDto);
          }
        }
      }
//      List<GnocFileDto> gnocFileCreateWoDtosAll = new ArrayList<>();
//      gnocFileCreateWoDtosAll.addAll(gnocFileCreateWoDtos);
//      for (GnocFileDto gnocFileCreateWoDtoOld : gnocFileCreateWoDtosAdd) {
//        if (gnocFileCreateWoDtoOld.getId() != null) {
//          gnocFileCreateWoDtosAll.add(gnocFileCreateWoDtoOld);
//        }
//      }
      gnocFileRepository
          .deleteListGnocFile(GNOC_FILE_BUSSINESS.CFG_FILE_CREATE_WO, woTypeInsideDTO.getWoTypeId(),
              woTypeInsideDTO.getIdDeleteListFileCreateWo());
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CFG_FILE_CREATE_WO,
              woTypeInsideDTO.getWoTypeId(),
              gnocFileCreateWoDtos);

      List<WoTypeCheckListDTO> woTypeCheckListDTOList = woTypeInsideDTO.getWoTypeCheckListDTOList();
      if (woTypeInsideDTO.getCheckListIdDeleteList() != null && !woTypeInsideDTO
          .getCheckListIdDeleteList()
          .isEmpty()) {
        for (Long idDel : woTypeInsideDTO.getCheckListIdDeleteList()) {
          resultInSideDto = woTypeCheckListRepository.delete(idDel);
        }
      }
      if (woTypeCheckListDTOList != null && woTypeCheckListDTOList.size() > 0) {
        for (WoTypeCheckListDTO woTypeCheckListDTO : woTypeCheckListDTOList) {
          woTypeCheckListDTO.setWoTypeId(woTypeInsideDTO.getWoTypeId());
          resultInSideDto = woTypeCheckListRepository.add(woTypeCheckListDTO);
        }
      }
      // Add history
      try {
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(woTypeInsideDTO.getWoTypeId().toString());
        dataHistoryChange.setType("WO_TYPE_MANAGEMENT");
        dataHistoryChange.setActionType("update");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(woTypeInsideDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        List<String> keys = getAllKeysDTO();
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public WoTypeInsideDTO findWoTypeById(Long woTypeDTOId) {
    log.debug("Request to findWoTypeById: {}", woTypeDTOId);
    return woTypeRepository.findByWoTypeId(woTypeDTOId);
  }

  @Override
  public List<WoTypeTimeDTO> getListWoTypeTimeDTO(WoTypeTimeDTO woTypeTimeDTO) {
    log.debug("Request to getListWoTypeTimeDTO: {}", woTypeTimeDTO);
    return woTypeRepository.getListWoTypeTimeDTO(woTypeTimeDTO);
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeIsEnable(WoTypeInsideDTO woTypeInsideDTO) {
    log.debug("Request to getListWoTypeIsEnable: {}", woTypeInsideDTO);
    return woTypeRepository.getListWoTypeIsEnable(woTypeInsideDTO);
  }

  @Override
  public Datatable getListWoTypeIsEnableDataTable(WoTypeInsideDTO woTypeInsideDTO) {
    return woTypeRepository.getListWoTypeIsEnableDataTable(woTypeInsideDTO);
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
    if (count != 9) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT")
        .equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woType.woTypeCode") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woType.woTypeName") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woType.woGroupTypeName") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woType.isEnableName") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woType.enableCreateStr") + "*")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woType.allowPendingStr") + "*")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woType.createFromOtherSysStr") + "*")
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woType.action") + "*")
        .equalsIgnoreCase(objects[8].toString().trim())) {
      return false;
    }
    return true;
  }

  private WoTypeInsideDTO validateImportInfo(WoTypeInsideDTO woTypeInsideDTO,
      List<WoTypeInsideDTO> woTypeInsideDTOList) {
    if (StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getWoTypeCode())) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.woTypeCode"));
      return woTypeInsideDTO;
    }

    if (StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getWoGroupTypeName())) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.woGroupTypeName"));
      return woTypeInsideDTO;
    }

    if (StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getWoTypeName())) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.woTypeName"));
      return woTypeInsideDTO;
    }

    if (StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getIsEnableName())) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.isEnableName"));
      return woTypeInsideDTO;
    }

    if (StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getEnableCreateStr())) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.enableCreateStr"));
      return woTypeInsideDTO;
    }

    if (StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getAllowPendingStr())) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.allowPendingStr"));
      return woTypeInsideDTO;
    }

    if (StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getCreateFromOtherSysStr())) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.createFromOtherSysStr"));
      return woTypeInsideDTO;
    }

    if (woTypeInsideDTO.getWoTypeCode().length() > 50) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.woTypeCode.tooLong"));
      return woTypeInsideDTO;
    }

    if (woTypeInsideDTO.getWoTypeName().length() > 1000) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.woTypeName.tooLong"));
      return woTypeInsideDTO;
    }

    if (woTypeInsideDTO.getWoGroupType() == null) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.woGroupType.exist"));
      return woTypeInsideDTO;
    }
    if (woTypeInsideDTO.getIsEnable() == null) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.isEnable.exist"));
      return woTypeInsideDTO;
    }
    if (woTypeInsideDTO.getEnableCreate() == null) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.enableCreate.exist"));
      return woTypeInsideDTO;
    }
    if (woTypeInsideDTO.getAllowPending() == null) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.allowPending.exist"));
      return woTypeInsideDTO;
    }
    if (woTypeInsideDTO.getCreateFromOtherSys() == null) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.createFromOtherSys.exist"));
      return woTypeInsideDTO;
    }
    if (woTypeInsideDTO.getAction() == null) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.action.exist"));
      return woTypeInsideDTO;
    }

    if (!isValidWoTypeCode(woTypeInsideDTO.getWoTypeCode())) {
      woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.isValidWoTypeCode"));
      return woTypeInsideDTO;
    }

    if (StringUtils.isNotNullOrEmpty(woTypeInsideDTO.getWoTypeCode())) {
      WoTypeInsideDTO woTypeInsideDTOTmp = woTypeRepository
          .checkWoTypeExist(woTypeInsideDTO.getWoTypeCode());
      if (woTypeInsideDTOTmp != null && I18n.getLanguage("woType.action.1")
          .equals(woTypeInsideDTO.getAction())) {
        woTypeInsideDTO.setResultImport(I18n.getLanguage("woType.err.woTypeCode.dup-code"));
        return woTypeInsideDTO;
      }
      if (woTypeInsideDTOTmp != null && (I18n.getLanguage("woType.action.0"))
          .equals(woTypeInsideDTO.getAction())) {
        woTypeInsideDTO.setWoTypeId(woTypeInsideDTOTmp.getWoTypeId());
      }
    }
    woTypeInsideDTO = validateDuplicate(woTypeInsideDTO, woTypeInsideDTOList);
    return woTypeInsideDTO;
  }

  private WoTypeInsideDTO validateDuplicate(WoTypeInsideDTO dto,
      List<WoTypeInsideDTO> lstoExportDTOS) {
    String codeTemp = dto.getWoTypeCode();
    String nameTemp = dto.getWoTypeName();
    if (lstoExportDTOS.size() == 0) {
      if (I18n.getLanguage("woType.action.0").equals(dto.getAction())
          && dto.getWoTypeId() == null) {
        dto.setResultImport(I18n.getLanguage("woType.err.woTypeCode.not-code"));
      }
    }
    for (int i = 0; i < lstoExportDTOS.size(); i++) {
      WoTypeInsideDTO dtoCheck = lstoExportDTOS.get(i);
      if (I18n.getLanguage("woType.action.1").equals(dto.getAction()) && codeTemp
          .equals(dtoCheck.getWoTypeCode()) && nameTemp.equals(dtoCheck.getWoTypeName())) {
        dto.setResultImport(I18n.getLanguage("woType.err.dup-code-in-file")
            .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
      if (I18n.getLanguage("woType.action.0").equals(dto.getAction())) {
        if (codeTemp.equals(dtoCheck.getWoTypeCode())) {
          dto.setValidate("1");
          break;
        }
        if (!codeTemp.equals(dtoCheck.getWoTypeCode()) && dto.getWoTypeId() == null) {
          dto.setResultImport(I18n.getLanguage("woType.err.woTypeCode.not-code"));
          break;
        }
      }
    }
    return dto;
  }

  private boolean isValidWoTypeCode(String woTypeCode) {
    String regex = "([A-Za-z0-9_]+)";
    return woTypeCode.matches(regex);
  }

  private File exportFileTemplate(List<WoTypeInsideDTO> dtoList, String key) throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("woType.export.title");
    String title = I18n.getLanguage("woType.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("woTypeCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("woTypeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("woGroupTypeName", "LEFT", false, 0, 0, new String[]{},
        null, "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("isEnableName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("enableCreateStr", "LEFT", false, 0, 0, new String[]{},
        null, "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("allowPendingStr", "LEFT", false, 0, 0, new String[]{},
        null, "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("createFromOtherSysStr", "LEFT", false, 0, 0,
        new String[]{}, null, "STRING");
    headerExportList.add(columnSheet);

    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = WO_TYPE_RESULT_IMPORT;
      subTitle = I18n.getLanguage("woType.export.importDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      fileNameOut = WO_TYPE_EXPORT;
      subTitle = I18n.getLanguage("woType.export.exportDate", DateTimeUtils.convertDateOffset());
    }
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        dtoList
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.woType"
        , headerExportList
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
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
    fileExportList.add(configFileExport);

    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExportList
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeByLocaleNotLike(WoTypeInsideDTO woTypeInsideDTO) {
    log.debug("Request to getListWoTypeByLocaleNotLike: {}", woTypeInsideDTO);
    return woTypeRepository.getListWoTypeByLocaleNotLike(woTypeInsideDTO);
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeByWoGroup(Long cdGroupId, String system,
      String locale) {
    return woTypeRepository.getListWoTypeByWoGroup(cdGroupId, system, locale);
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeByLocale(
      WoTypeInsideDTO woTypeDTO, String locale) {
    return woTypeRepository.getListWoTypeByLocale(woTypeDTO, locale);
  }

  @Override
  public WoTypeInsideDTO getWoTypeByCode(String woTypeCode) {
    return woTypeRepository.getWoTypeByCode(woTypeCode);
  }

  @Override
  public List<LanguageExchangeDTO> getLanguageExchangeWoType(String system, String business) {
    return woTypeRepository.getLanguageExchangeWoType(system, business);
  }

  @Override
  public List<WoTypeFilesGuideDTO> getListWoTypeFilesGuideDTO(WoTypeFilesGuideDTO typeFileCon) {
    return woTypeRepository.getListWoTypeFilesGuideDTO(typeFileCon);
  }

  @Override
  public UsersDTO getUnitNameByUserName(String username) {
    if (!StringUtils.isStringNullOrEmpty(username)) {
      return userBusiness.getUnitNameByUserName(username);
    }
    return null;
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = Arrays.asList("woTypeId", "woTypeCode", "woTypeName", "isEnable", "woGroupType", "cdGroupId",
          "enableCreate", "timeOver", "smsCycle", "woCloseAutomaticTime", "userTypeCode", "allowPending", "processTime",
          "createFromOtherSys", "timeAutoCloseWhenOver", "isOtherSys", "lstCdGroup", "woTypeGroupId", "woGroupId",
          "isEnableName");
      return keys;
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
