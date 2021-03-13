package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CfgBusinessCallSmsDTO;
import com.viettel.gnoc.commons.dto.CfgBusinessCallSmsUserDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.repository.CfgBusinessCallSmsRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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

@Service
@Transactional
@Slf4j
public class CfgBusinessCallSmsBusinessImpl implements CfgBusinessCallSmsBusiness {

  @Autowired
  protected CfgBusinessCallSmsRepository cfgBusinessCallSmsRepository;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CatItemRepository catItemRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;
  HashMap<String, String> mapConfigType = new HashMap<>();
  private final static String CFG_BUSINESS_CALL_SMS_RESULT_IMPORT = "CFG_BUSINESS_CALL_SMS_RESULT_IMPORT";
  private final static String CFG_BUSINESS_CALL_SMS_EXPORT = "CFG_BUSINESS_CALL_SMS_EXPORT";

  @Override
  public Datatable getListCfgBusinessCallSmsDTO(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) {
    log.debug("Request to getListCfgBusinessCallSmsDTO: {}", cfgBusinessCallSmsDTO);
    return cfgBusinessCallSmsRepository.getListCfgBusinessCallSmsDTO(cfgBusinessCallSmsDTO);
  }

  @Override
  public List<WoCdGroupInsideDTO> getListWoCdGroupCBB() {
    log.debug("Request to getListWoCdGroupCBB: {}");
    return cfgBusinessCallSmsRepository.getListWoCdGroupCBB();
  }

  @Override
  public ResultInSideDto insert(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) {
    log.debug("Request to add: {}", cfgBusinessCallSmsDTO);
    ResultInSideDto resultInSideDto = cfgBusinessCallSmsRepository.add(cfgBusinessCallSmsDTO);
    Long id = resultInSideDto.getId();
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      if (cfgBusinessCallSmsDTO.getLstCfgBusinessCallSmsUser() != null && !cfgBusinessCallSmsDTO
          .getLstCfgBusinessCallSmsUser().isEmpty()) {
        for (CfgBusinessCallSmsUserDTO dto : cfgBusinessCallSmsDTO.getLstCfgBusinessCallSmsUser()) {
          CfgBusinessCallSmsUserDTO check = cfgBusinessCallSmsRepository
              .ckeckUserExist(id, dto.getUserId());
          if (check == null) {
            dto.setCfgBusinessId(id);
            resultInSideDto = cfgBusinessCallSmsRepository.addUser(dto);
          }
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) {
    log.debug("Request to update: {}", cfgBusinessCallSmsDTO);
    ResultInSideDto resultInSideDto = cfgBusinessCallSmsRepository.edit(cfgBusinessCallSmsDTO);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      if (cfgBusinessCallSmsDTO.getLstDelete() != null && !cfgBusinessCallSmsDTO.getLstDelete()
          .isEmpty()) {
        for (Long id : cfgBusinessCallSmsDTO.getLstDelete()) {
          resultInSideDto = cfgBusinessCallSmsRepository.deleteUser(id);
        }
      }
      if (cfgBusinessCallSmsDTO.getLstCfgBusinessCallSmsUser() != null && !cfgBusinessCallSmsDTO
          .getLstCfgBusinessCallSmsUser().isEmpty()) {
        for (CfgBusinessCallSmsUserDTO dto : cfgBusinessCallSmsDTO.getLstCfgBusinessCallSmsUser()) {
          CfgBusinessCallSmsUserDTO check = cfgBusinessCallSmsRepository
              .ckeckUserExist(cfgBusinessCallSmsDTO.getId(), dto.getUserId());
          if (dto.getId() != null) {
            dto.setCfgBusinessId(cfgBusinessCallSmsDTO.getId());
            resultInSideDto = cfgBusinessCallSmsRepository.editUser(dto);
          } else {
            if (check == null) {
              dto.setCfgBusinessId(cfgBusinessCallSmsDTO.getId());
              resultInSideDto = cfgBusinessCallSmsRepository.addUser(dto);
            }
          }
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public CfgBusinessCallSmsDTO getDetail(Long id) {
    log.debug("Request to getDetail: {}", id);
    CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO = cfgBusinessCallSmsRepository.getDetail(id);
    cfgBusinessCallSmsDTO
        .setLstCfgBusinessCallSmsUser(cfgBusinessCallSmsRepository.getListUserBycfgBusinessId(id));
    return cfgBusinessCallSmsDTO;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    log.debug("Request to delete: {}", id);
    return cfgBusinessCallSmsRepository.delete(id);
  }

  public void setMapConfigType() {
    List<CatItemDTO> lstCfgType = (List<CatItemDTO>) catItemRepository
        .getItemMaster("WO_CFG_BUSINESS_SMS_CALL", null, null, null, null).getData();
    if (lstCfgType != null && !lstCfgType.isEmpty()) {
      for (CatItemDTO catItemDTO : lstCfgType) {
        mapConfigType.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
  }

  @Override
  public File exportData(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) throws Exception {
    List<CfgBusinessCallSmsDTO> cfgBusinessCallSmsDTOList = cfgBusinessCallSmsRepository
        .getListDataExport(cfgBusinessCallSmsDTO);
    return exportFileTemplate(cfgBusinessCallSmsDTOList, "");
  }

  private File exportFileTemplate(List<CfgBusinessCallSmsDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("cfgBusinessCallSms.export.title");
    String title = I18n.getLanguage("cfgBusinessCallSms.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      columnSheet = new ConfigHeaderExport("configType", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("cdName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("cfgLevelStr", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("username", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("actionName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
      fileNameOut = CFG_BUSINESS_CALL_SMS_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("cfgBusinessCallSms.export.exportDate", DateTimeUtils.convertDateOffset());
    } else {
      columnSheet = new ConfigHeaderExport("username", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("fullName", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("mobileNumber", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("configType", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("cdName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("cfgLevel", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      fileNameOut = CFG_BUSINESS_CALL_SMS_EXPORT;
      subTitle = I18n
          .getLanguage("cfgBusinessCallSms.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.cfgBusinessCallSms"
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
    //Cấu hình đường dẫn
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
  public File getTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");
    XSSFSheet sheetParam1 = workbook.createSheet("param1");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("cfgBusinessCallSms.configType"),
        I18n.getLanguage("cfgBusinessCallSms.cdName"),
        I18n.getLanguage("cfgBusinessCallSms.cfgLevelStr"),
        I18n.getLanguage("cfgBusinessCallSms.username"),
        I18n.getLanguage("cfgBusinessCallSms.actionName")

    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("cfgBusinessCallSms.configType"),
        I18n.getLanguage("cfgBusinessCallSms.cdName"),
        I18n.getLanguage("cfgBusinessCallSms.cfgLevelStr"),
        I18n.getLanguage("cfgBusinessCallSms.username"),
        I18n.getLanguage("cfgBusinessCallSms.actionName")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int configTypeColumn = listHeader.indexOf(I18n.getLanguage("cfgBusinessCallSms.configType"));
    int cfgLevelStrColumn = listHeader.indexOf(I18n.getLanguage("cfgBusinessCallSms.cfgLevelStr"));
    int actionNameColumn = listHeader.indexOf(I18n.getLanguage("cfgBusinessCallSms.actionName"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("cfgBusinessCallSms.title"));
    titleCell.setCellStyle(style.get("title"));

    Row firstLeftHeaderRow = sheetOne.createRow(0);
    Cell firstLeftHeaderCell = firstLeftHeaderRow.createCell(1);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.firstLeftHeader"));
    firstLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Row secondLeftHeaderRow = sheetOne.createRow(1);
    Cell secondLeftHeaderCell = secondLeftHeaderRow.createCell(1);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.secondLeftHeader"));
    secondLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell firstRightHeaderCell = firstLeftHeaderRow.createCell(4);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstRightHeaderCell.setCellValue(I18n.getLanguage("common.export.firstRightHeader"));
    firstRightHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell secondRightHeaderCell = secondLeftHeaderRow.createCell(4);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondRightHeaderCell.setCellValue(I18n.getLanguage("common.export.secondRightHeader"));
    secondRightHeaderCell.setCellStyle(style.get("indexTitle"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(4);
    Row headerWoGroup = sheetParam1.createRow(0);
    headerRow.setHeightInPoints(30);
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
    Cell headerCellGroupId = headerWoGroup.createCell(0);
    Cell headerCellGroupName = headerWoGroup.createCell(1);
    XSSFRichTextString groupId = new XSSFRichTextString(
        I18n.getLanguage("cfgBusinessCallSms.woGroupId"));
    XSSFRichTextString groupName = new XSSFRichTextString(
        I18n.getLanguage("cfgBusinessCallSms.woGroupName"));
    headerCellGroupId.setCellValue(groupId);
    headerCellGroupId.setCellStyle(style.get("header"));
    headerCellGroupName.setCellValue(groupName);
    headerCellGroupName.setCellStyle(style.get("header"));
    sheetParam1.setColumnWidth(1, 12000);
    sheetParam1.setColumnWidth(2, 20000);
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown
    int row = 5;
    List<CatItemDTO> listCfgType = (List<CatItemDTO>) catItemRepository
        .getItemMaster("WO_CFG_BUSINESS_SMS_CALL", null, null, null, null).getData();
    for (CatItemDTO dto : listCfgType) {
      excelWriterUtils
          .createCell(sheetParam, 1, row++, dto.getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name configType = workbook.createName();
    configType.setNameName("configType");
    configType.setRefersToFormula("param!$B$2:$B$" + row);
    XSSFDataValidationConstraint configTypeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "configType");
    CellRangeAddressList configTypeCreate = new CellRangeAddressList(5, 65000, configTypeColumn,
        configTypeColumn);
    XSSFDataValidation dataValidationConfigType = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            configTypeConstraint, configTypeCreate);
    dataValidationConfigType.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationConfigType);

    row = 1;
    List<WoCdGroupInsideDTO> woGroupList = cfgBusinessCallSmsRepository.getListWoCdGroupCBB();
    for (WoCdGroupInsideDTO dto : woGroupList) {
      excelWriterUtils
          .createCell(sheetParam1, 0, row, dto.getWoGroupId().toString(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam1, 1, row++, dto.getWoGroupName(), style.get("cell"));
    }
    sheetParam1.autoSizeColumn(0);
    sheetParam1.autoSizeColumn(1);

    row = 5;
    excelWriterUtils
        .createCell(sheetParam, 3, row++, I18n.getLanguage("cfgBusinessCallSms.cfgLevel.1")
            , style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam, 3, row++, I18n.getLanguage("cfgBusinessCallSms.cfgLevel.2")
            , style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam, 3, row++, I18n.getLanguage("cfgBusinessCallSms.cfgLevel.3")
            , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name cfgLevelStr = workbook.createName();
    cfgLevelStr.setNameName("cfgLevelStr");
    cfgLevelStr.setRefersToFormula("param!$D$2:$D$" + row);
    XSSFDataValidationConstraint cfgLevelStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "cfgLevelStr");
    CellRangeAddressList cfgLevelStrCreate = new CellRangeAddressList(5, 65000, cfgLevelStrColumn,
        cfgLevelStrColumn);
    XSSFDataValidation dataValidationCfgLevelStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            cfgLevelStrConstraint, cfgLevelStrCreate);
    dataValidationCfgLevelStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationCfgLevelStr);

    row = 5;
    excelWriterUtils
        .createCell(sheetParam, 5, row++, I18n.getLanguage("cfgBusinessCallSms.action.1")
            , style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam, 5, row++, I18n.getLanguage("cfgBusinessCallSms.action.0")
            , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name actionName = workbook.createName();
    actionName.setNameName("actionName");
    actionName.setRefersToFormula("param!$F$2:$F$" + row);
    XSSFDataValidationConstraint actionNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "actionName");
    CellRangeAddressList actionNameCreate = new CellRangeAddressList(5, 65000, actionNameColumn,
        actionNameColumn);
    XSSFDataValidation dataValidationActionName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            actionNameConstraint, actionNameCreate);
    dataValidationActionName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationActionName);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("cfgBusinessCallSms.title"));
    workbook.setSheetName(2, I18n.getLanguage("cfgBusinessCallSms.cdName"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_WO_CD_TEMP" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<CfgBusinessCallSmsDTO> cfgBusinessCallSmsDTOList = new ArrayList<>();
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
            5,
            1000
        );
        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            5,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }
        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          setMapConfigType();
          for (Object[] obj : dataImportList) {
            CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO = new CfgBusinessCallSmsDTO();
            if (obj[1] != null) {
              cfgBusinessCallSmsDTO.setConfigType(obj[1].toString().trim());
              for (Map.Entry<String, String> item : mapConfigType.entrySet()) {
                if (cfgBusinessCallSmsDTO.getConfigType().equals(item.getValue())) {
                  cfgBusinessCallSmsDTO.setCfgTypeId(Long.valueOf(item.getKey()));
                  break;
                } else {
                  cfgBusinessCallSmsDTO.setCfgTypeId(null);
                }
              }
            } else {
              cfgBusinessCallSmsDTO.setConfigType(null);
            }
            if (obj[2] != null) {
              if (!DataUtil.isNumber(obj[2].toString().trim())) {
                cfgBusinessCallSmsDTO
                    .setResultImport(I18n.getLanguage("cfgBusinessCallSms.errType.woGroupId"));
                cfgBusinessCallSmsDTO.setCdName(obj[2].toString().trim());
              } else {
                cfgBusinessCallSmsDTO.setCdId(Long.valueOf(obj[2].toString().trim()));
                cfgBusinessCallSmsDTO.setCdName(obj[2].toString().trim());
              }
            } else {
              cfgBusinessCallSmsDTO.setCdName(null);
            }
            if (obj[3] != null) {
              cfgBusinessCallSmsDTO.setCfgLevelStr(obj[3].toString().trim());
              if (I18n.getLanguage("cfgBusinessCallSms.cfgLevel.1")
                  .equals(cfgBusinessCallSmsDTO.getCfgLevelStr())) {
                cfgBusinessCallSmsDTO.setCfgLevel(1L);
              } else if (I18n.getLanguage("cfgBusinessCallSms.cfgLevel.2")
                  .equals(cfgBusinessCallSmsDTO.getCfgLevelStr())) {
                cfgBusinessCallSmsDTO.setCfgLevel(2L);
              } else if (I18n.getLanguage("cfgBusinessCallSms.cfgLevel.3")
                  .equals(cfgBusinessCallSmsDTO.getCfgLevelStr())) {
                cfgBusinessCallSmsDTO.setCfgLevel(3L);
              } else {
                cfgBusinessCallSmsDTO.setCfgLevel(null);
              }
            } else {
              cfgBusinessCallSmsDTO.setCfgLevelStr(null);
            }
            if (obj[4] != null) {
              cfgBusinessCallSmsDTO.setUsername(obj[4].toString().trim());
            } else {
              cfgBusinessCallSmsDTO.setUsername(null);
            }
            if (obj[5] != null) {
              cfgBusinessCallSmsDTO.setActionName(obj[5].toString().trim());
              cfgBusinessCallSmsDTO.setAction(cfgBusinessCallSmsDTO.getActionName());
              if (!(I18n.getLanguage("cfgBusinessCallSms.action.1")
                  .equals(cfgBusinessCallSmsDTO.getActionName()) || I18n
                  .getLanguage("cfgBusinessCallSms.action.0")
                  .equals(cfgBusinessCallSmsDTO.getActionName()))) {
                cfgBusinessCallSmsDTO.setAction(null);
              }
            } else {
              cfgBusinessCallSmsDTO.setActionName(null);
            }
            CfgBusinessCallSmsDTO cfgBusinessCallSmsTmp = validateImportInfo(
                cfgBusinessCallSmsDTO, cfgBusinessCallSmsDTOList);

            if (cfgBusinessCallSmsTmp.getResultImport() == null) {
              cfgBusinessCallSmsTmp
                  .setResultImport(I18n.getLanguage("cfgBusinessCallSms.result.import"));
              cfgBusinessCallSmsDTOList.add(cfgBusinessCallSmsTmp);
//              srRoleUserDTOS.add(srRoleUserDTO);
            } else {
              cfgBusinessCallSmsDTOList.add(cfgBusinessCallSmsTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (cfgBusinessCallSmsDTOList != null) {
              for (CfgBusinessCallSmsDTO dto : cfgBusinessCallSmsDTOList) {
                if (dto.getAction().equals(I18n.getLanguage("cfgBusinessCallSms.action.1"))) {
                  resultInSideDto = cfgBusinessCallSmsRepository.add(dto);
                  if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
                    CfgBusinessCallSmsUserDTO callSmsUserDTO = new CfgBusinessCallSmsUserDTO();
                    callSmsUserDTO.setCfgBusinessId(resultInSideDto.getId());
                    callSmsUserDTO.setUserId(dto.getUserId());
                    resultInSideDto = cfgBusinessCallSmsRepository.addUser(callSmsUserDTO);
                  }
                }
                if (dto.getAction().equals(I18n.getLanguage("cfgBusinessCallSms.action.0"))) {
                  resultInSideDto = cfgBusinessCallSmsRepository.delete(dto.getId());
                }
              }
            }
          } else {
            File fileExport = exportFileTemplate(cfgBusinessCallSmsDTOList,
                Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileTemplate(cfgBusinessCallSmsDTOList, Constants.RESULT_IMPORT);
          resultInSideDto.setFile(fileExport);
          return resultInSideDto;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
    }
    return resultInSideDto;
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
    if (count != 6) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgBusinessCallSms.configType") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgBusinessCallSms.cdName") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgBusinessCallSms.cfgLevelStr") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgBusinessCallSms.username") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgBusinessCallSms.actionName") + "*")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    return true;
  }

  private CfgBusinessCallSmsDTO validateImportInfo(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO,
      List<CfgBusinessCallSmsDTO> list) {
    if (StringUtils.isStringNullOrEmpty(cfgBusinessCallSmsDTO.getConfigType())) {
      cfgBusinessCallSmsDTO.setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.configType"));
      return cfgBusinessCallSmsDTO;
    }

    if (StringUtils.isStringNullOrEmpty(cfgBusinessCallSmsDTO.getCdName())) {
      cfgBusinessCallSmsDTO.setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.cdName"));
      return cfgBusinessCallSmsDTO;
    }

    if (StringUtils.isStringNullOrEmpty(cfgBusinessCallSmsDTO.getCfgLevelStr())) {
      cfgBusinessCallSmsDTO.setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.cfgLevelStr"));
      return cfgBusinessCallSmsDTO;
    }

    if (StringUtils.isStringNullOrEmpty(cfgBusinessCallSmsDTO.getUsername())) {
      cfgBusinessCallSmsDTO.setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.username"));
      return cfgBusinessCallSmsDTO;
    }

    if (StringUtils.isStringNullOrEmpty(cfgBusinessCallSmsDTO.getActionName())) {
      cfgBusinessCallSmsDTO.setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.actionName"));
      return cfgBusinessCallSmsDTO;
    }

    if (StringUtils.isStringNullOrEmpty(cfgBusinessCallSmsDTO.getCfgTypeId())) {
      cfgBusinessCallSmsDTO
          .setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.cfgTypeId.exist"));
      return cfgBusinessCallSmsDTO;
    }
    if (StringUtils.isStringNullOrEmpty(cfgBusinessCallSmsDTO.getCfgLevel())) {
      cfgBusinessCallSmsDTO
          .setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.cfgLevel.exist"));
      return cfgBusinessCallSmsDTO;
    }
    if (StringUtils.isStringNullOrEmpty(cfgBusinessCallSmsDTO.getAction())) {
      cfgBusinessCallSmsDTO
          .setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.action.exist"));
      return cfgBusinessCallSmsDTO;
    }
    UsersInsideDto usersInsideDto = new UsersInsideDto();
    usersInsideDto.setUsername(cfgBusinessCallSmsDTO.getUsername());
    List<UsersInsideDto> userDTOList = userRepository.getListUsersDTOS(usersInsideDto);
    if (userDTOList == null || userDTOList.isEmpty()) {
      cfgBusinessCallSmsDTO
          .setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.username.exist"));
      return cfgBusinessCallSmsDTO;
    } else {
      cfgBusinessCallSmsDTO.setUserId(userDTOList.get(0).getUserId());
    }
    CfgBusinessCallSmsDTO callSmsDTO = cfgBusinessCallSmsRepository
        .ckeckCfgBusinessCallSmsExist(cfgBusinessCallSmsDTO.getCfgTypeId(),
            cfgBusinessCallSmsDTO.getCdId(), cfgBusinessCallSmsDTO.getCfgLevel());
    if (callSmsDTO != null) {
      if (I18n.getLanguage("cfgBusinessCallSms.action.1")
          .equals(cfgBusinessCallSmsDTO.getAction())) {
        cfgBusinessCallSmsDTO.setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.duplicate"));
        return cfgBusinessCallSmsDTO;
      }
      if (I18n.getLanguage("cfgBusinessCallSms.action.0")
          .equals(cfgBusinessCallSmsDTO.getAction())) {
        cfgBusinessCallSmsDTO.setId(callSmsDTO.getId());
      }

    } else {
      if (I18n.getLanguage("cfgBusinessCallSms.action.0")
          .equals(cfgBusinessCallSmsDTO.getAction())) {
        cfgBusinessCallSmsDTO.setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.noData"));
        return cfgBusinessCallSmsDTO;
      }
    }
    if (list != null && list.size() > 0 && cfgBusinessCallSmsDTO.getResultImport() == null) {
      cfgBusinessCallSmsDTO = validateDuplicate(list, cfgBusinessCallSmsDTO);
    }

    return cfgBusinessCallSmsDTO;
  }

  private CfgBusinessCallSmsDTO validateDuplicate(List<CfgBusinessCallSmsDTO> list,
      CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) {
    for (int i = 0; i < list.size(); i++) {
      CfgBusinessCallSmsDTO callSmsDTOTmp = list.get(i);
      if (I18n.getLanguage("cfgBusinessCallSms.result.import")
          .equals(callSmsDTOTmp.getResultImport()) && callSmsDTOTmp.getCfgTypeId().toString()
          .equals(cfgBusinessCallSmsDTO.getCfgTypeId().toString())
          && callSmsDTOTmp.getCdId().toString().equals(
          cfgBusinessCallSmsDTO.getCdId().toString()) && callSmsDTOTmp.getCfgLevel().toString()
          .equals(cfgBusinessCallSmsDTO.getCfgLevel().toString())) {
        cfgBusinessCallSmsDTO
            .setResultImport(I18n.getLanguage("cfgBusinessCallSms.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return cfgBusinessCallSmsDTO;
  }

}
