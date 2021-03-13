package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.wo.dto.CfgWoHelpVsmartDTO;
import com.viettel.gnoc.wo.repository.CfgWoHelpVsmartRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
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
public class CfgWoHelpVsmartBusinessImpl implements CfgWoHelpVsmartBusiness {

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
  CfgWoHelpVsmartRepository cfgWoHelpVsmartRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  private final static String CFG_WO_HELP_VSMART_SEQ = "CFG_WO_HELP_VSMART_SEQ";

  @Override
  public ResultInSideDto insertCfgWoHelpVsmart(List<MultipartFile> files,
      CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO)
      throws Exception {
    String id = cfgWoHelpVsmartRepository.getSequenseCfgWoHelpVsmart(CFG_WO_HELP_VSMART_SEQ);
    ResultInSideDto resultInSideDto = saveUploadedFile(files.get(0), Long.valueOf(id));
    cfgWoHelpVsmartDTO.setId(Long.valueOf(id));
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      cfgWoHelpVsmartDTO.setFileId(resultInSideDto.getFilePath());
      resultInSideDto = cfgWoHelpVsmartRepository.insertCfgWoHelpVsmart(cfgWoHelpVsmartDTO);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCfgWoHelpVsmart(List<MultipartFile> files,
      CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO)
      throws Exception {
    ResultInSideDto resultInSideDto;
    if (!files.isEmpty()) {
      resultInSideDto = saveUploadedFile(files.get(0), cfgWoHelpVsmartDTO.getId());
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        cfgWoHelpVsmartDTO.setFileId(resultInSideDto.getFilePath());
        cfgWoHelpVsmartDTO.setSyncStatus(null);
        resultInSideDto = cfgWoHelpVsmartRepository.updateCfgWoHelpVsmart(cfgWoHelpVsmartDTO);
      }
    } else {
      CfgWoHelpVsmartDTO cfgWoHelpVsmartDTOResult = cfgWoHelpVsmartRepository
          .findCfgWoHelpVsmartsById(cfgWoHelpVsmartDTO.getId());
      cfgWoHelpVsmartDTOResult.setSystemId(cfgWoHelpVsmartDTO.getSystemId());
      cfgWoHelpVsmartDTOResult.setTypeId(cfgWoHelpVsmartDTO.getTypeId());
      cfgWoHelpVsmartDTOResult.setTypeName(cfgWoHelpVsmartDTO.getTypeName());
      resultInSideDto = cfgWoHelpVsmartRepository.updateCfgWoHelpVsmart(cfgWoHelpVsmartDTOResult);
    }
    return resultInSideDto;
  }

  @Override
  public CfgWoHelpVsmartDTO findCfgWoHelpVsmartsById(Long id) {
    log.debug("Request to findCfgWoHelpVsmartsById: {}", id);
    CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO = cfgWoHelpVsmartRepository.findCfgWoHelpVsmartsById(id);
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.CFG_WO_HELP_VSMART);
    gnocFileDto.setBusinessId(id);
    GnocFileDto gnocFileDtoResult = gnocFileRepository.getGnocFileByDto(gnocFileDto);
    if (gnocFileDtoResult != null) {
      cfgWoHelpVsmartDTO.setFileId(gnocFileDtoResult.getPath());
      cfgWoHelpVsmartDTO.setFileName(gnocFileDtoResult.getFileName());
    }
    return cfgWoHelpVsmartDTO;
  }

  @Override
  public ResultInSideDto deleteCfgWoHelpVsmart(Long id) {
    log.debug("Request to deleteCfgWoHelpVsmart: {}", id);
    return cfgWoHelpVsmartRepository.deleteCfgWoHelpVsmart(id);
  }

  @Override
  public ResultInSideDto deleteListCfgWoHelpVsmart(List<CfgWoHelpVsmartDTO> cfgWoHelpVsmartDTOS) {
    return cfgWoHelpVsmartRepository.deleteListCfgWoHelpVsmart(cfgWoHelpVsmartDTOS);
  }

  @Override
  public Datatable getListCfgWoHelpVsmartDTOSearchWeb(CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO) {
    log.debug("Request to getListCfgWoHelpVsmartDTOSearchWeb: {}", cfgWoHelpVsmartDTO);
    return cfgWoHelpVsmartRepository.getListCfgWoHelpVsmartDTOSearchWeb(cfgWoHelpVsmartDTO);
  }

  @Override
  public File getTemplate(String systemId) throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();

    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut); //Xóa khoảng trắng
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workBook.getSheetAt(0);
    workBook.setSheetName(workBook.getSheetIndex(sheetOne),
        I18n.getLanguage("woHelpVsmart.export.odsr.sheetName"));
    XSSFSheet sheetTwo = workBook
        .createSheet(I18n.getLanguage("woHelpVsmart.export.extra.sheetName"));
    XSSFSheet sheetParam = workBook.createSheet("param");
    String[] header = new String[]{
        I18n.getLanguage("cfgWoHelpVsmart.export.list.key")
        ,
        I18n.getLanguage("cfgWoHelpVsmart.export.list.controlType")
        ,
        I18n.getLanguage("cfgWoHelpVsmart.export.list.keyCode")
        ,
        I18n.getLanguage("cfgWoHelpVsmart.export.list.value")
        ,
        I18n.getLanguage("cfgWoHelpVsmart.export.list.format")
        ,
        I18n.getLanguage("cfgWoHelpVsmart.export.list.dataCode")
        ,
        I18n.getLanguage("cfgWoHelpVsmart.export.list.dataType")
        ,
        I18n.getLanguage("cfgWoHelpVsmart.export.list.defaultValue")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("cfgWoHelpVsmart.export.list.key")
        ,
        I18n.getLanguage("cfgWoHelpVsmart.export.list.controlType")
        ,
        I18n.getLanguage("cfgWoHelpVsmart.export.list.keyCode")
    };
    String[] headerParam = new String[]{
        I18n.getLanguage("cfgWoHelpVsmart.export.list.key")
        ,
        I18n.getLanguage("cfgWoHelpVsmart.export.list.value")
    };

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderParam = Arrays.asList(headerParam);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    int controlTypeColumn = listHeader
        .indexOf(I18n.getLanguage("cfgWoHelpVsmart.export.list.controlType"));
    int keyCodeColumn = listHeader.indexOf(I18n.getLanguage("cfgWoHelpVsmart.export.list.keyCode"));
    int dataCodeColumn = listHeader
        .indexOf(I18n.getLanguage("cfgWoHelpVsmart.export.list.dataCode"));
    int dataTypeColumn = listHeader
        .indexOf(I18n.getLanguage("cfgWoHelpVsmart.export.list.dataType"));
    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetOne.createRow(0);
    headerRow.setHeightInPoints(18);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeader : listHeaderStar) {
        if (checkHeader.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("(*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(styles.get("header"));
      sheetOne.setColumnWidth(i, 8000);
    }
    sheetOne.setColumnWidth(0, 4000);

    //Tao header sheet Param
    Row headerParamRow = sheetTwo.createRow(0);
    headerParamRow.setHeightInPoints(18);
    for (int i = 0; i < listHeaderParam.size(); i++) {
      Cell headerCell = headerParamRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeaderParam.get(i));
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(styles.get("header"));
      sheetTwo.setColumnWidth(i, 8000);
    }
    sheetTwo.setColumnWidth(0, 4000);

    //combo controlType
    int row = 1;
    ewu.createCell(sheetParam, 0, row++, "TextBox", styles.get("cell"));
    ewu.createCell(sheetParam, 0, row++, "DatePicker", styles.get("cell"));
    ewu.createCell(sheetParam, 0, row++, "CheckBox", styles.get("cell"));
    ewu.createCell(sheetParam, 0, row++, "Function", styles.get("cell"));
    ewu.createCell(sheetParam, 0, row++, "Uploader", styles.get("cell"));
    ewu.createCell(sheetParam, 0, row++, "ComboBox", styles.get("cell"));

    sheetParam.autoSizeColumn(0);

    Name configType = workBook.createName();
    configType.setNameName("controlType");
    configType.setRefersToFormula("param!$A$2:$A$" + row);

    XSSFDataValidationConstraint controlTypeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "controlType");
    CellRangeAddressList cellRangecontrolType = new CellRangeAddressList(1, 65000,
        controlTypeColumn, controlTypeColumn);
    XSSFDataValidation dataValidationControlType = (XSSFDataValidation) dvHelper.createValidation(
        controlTypeConstraint, cellRangecontrolType);
    dataValidationControlType.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationControlType);
    //combo keyCode
    row = 1;
    if ("1".equals(systemId)) {
      ewu.createCell(sheetParam, 1, row++, "odName", styles.get("cell"));
      ewu.createCell(sheetParam, 1, row++, "odDescription", styles.get("cell"));
      ewu.createCell(sheetParam, 1, row++, "priorityCode", styles.get("cell"));
      ewu.createCell(sheetParam, 1, row++, "startTime", styles.get("cell"));
      ewu.createCell(sheetParam, 1, row++, "endTime", styles.get("cell"));
      ewu.createCell(sheetParam, 1, row++, "receiveUnitCode", styles.get("cell"));
      ewu.createCell(sheetParam, 1, row++, "odFile", styles.get("cell"));
    } else {
      ewu.createCell(sheetParam, 1, row++, "title", styles.get("cell"));
      ewu.createCell(sheetParam, 1, row++, "description", styles.get("cell"));
    }
    sheetParam.autoSizeColumn(0);
    Name keyCode = workBook.createName();
    keyCode.setNameName("keyCode");
    keyCode.setRefersToFormula("param!$B$2:$B$" + row);

    XSSFDataValidationConstraint keyCodeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "keyCode");
    CellRangeAddressList cellRangeKeyCode = new CellRangeAddressList(1, 65000, keyCodeColumn,
        keyCodeColumn);
    XSSFDataValidation dataValidationKeyCode = (XSSFDataValidation) dvHelper.createValidation(
        keyCodeConstraint, cellRangeKeyCode);
    dataValidationKeyCode.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationKeyCode);

    //combo dataCode
    row = 1;

    if ("1".equals(systemId)) {
      ewu.createCell(sheetParam, 2, row++, "GNOC_UNIT", styles.get("cell"));
      ewu.createCell(sheetParam, 2, row++, "GNOC_OD_PRIORITY", styles.get("cell"));
    }
    sheetParam.autoSizeColumn(1);

    Name isEnableValues = workBook.createName();
    isEnableValues.setNameName("dataCode");
    isEnableValues.setRefersToFormula("param!$C$2:$C$" + row);

    XSSFDataValidationConstraint dataCodeValuesConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "dataCode");
    CellRangeAddressList cellisEnableValues = new CellRangeAddressList(1, 65000, dataCodeColumn,
        dataCodeColumn);
    XSSFDataValidation dataValidationDataCode = (XSSFDataValidation) dvHelper.createValidation(
        dataCodeValuesConstraint, cellisEnableValues);
    dataValidationDataCode.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationDataCode);

    //combo dataType
    row = 1;

    ewu.createCell(sheetParam, 3, row++, "1", styles.get("cell"));
    ewu.createCell(sheetParam, 3, row++, "2", styles.get("cell"));

    sheetParam.autoSizeColumn(0);

    Name UIValues = workBook.createName();
    UIValues.setNameName("dataType");
    UIValues.setRefersToFormula("param!$D$2:$D$" + row);

    XSSFDataValidationConstraint dataTypeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "dataType");
    CellRangeAddressList dataTypeCellRange = new CellRangeAddressList(1, 65000, dataTypeColumn,
        dataTypeColumn);
    XSSFDataValidation dataValidationDataType = (XSSFDataValidation) dvHelper.createValidation(
        dataTypeConstraint, dataTypeCellRange);
    dataValidationDataType.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationDataType);

    String fileResult = tempFolder + File.separator;
    String systemName = "1".equals(systemId) ? "OD_System_" : "SR_System_";
    String fileName =
        "CfgWoHelpVsmartTemplate" + "_" + systemName + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public List<CatItemDTO> getListCbbSystem() {
    List<CatItemDTO> res = new ArrayList<>();
    List<CatItemDTO> list = cfgWoHelpVsmartRepository.getListCbbSystem();
    for (CatItemDTO item : list) {
      CatItemDTO catItemDTO = new CatItemDTO();
      catItemDTO.setItemId(Long.parseLong(item.getItemValue()));
      catItemDTO.setItemName(item.getItemName());
      res.add(catItemDTO);
    }
    return res;
  }

  private ResultInSideDto saveUploadedFile(MultipartFile multipartFile, Long id)
      throws Exception {
    Date date = new Date();
    String fullPathOld = FileUtils
        .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), uploadFolder,
            date);
    File fileTemp = new File(fullPathOld);
    ResultInSideDto resultInSideDto = checkFile(fileTemp);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      UserToken userToken = ticketProvider.getUserToken();
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
              multipartFile.getBytes(), date);
      String fileName = multipartFile.getOriginalFilename();
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(fileName);
      gnocFileDto.setCreateUnitId(userToken.getDeptId());
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(userToken.getUserID());
      gnocFileDto.setCreateUserName(userToken.getUserName());
      gnocFileDto.setCreateTime(date);
      gnocFileDto.setMappingId(id);
      gnocFileDtos.add(gnocFileDto);
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CFG_WO_HELP_VSMART, id, gnocFileDtos);
      resultInSideDto.setFilePath(fullPathOld);
    }
    return resultInSideDto;
  }

  private ResultInSideDto checkFile(File file) throws IOException {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    List<Object[]> lstHeader;
    lstHeader = CommonImport.getDataFromExcelFile(file,
        0,//sheet
        0,//begin row
        0,//from column
        7,//to column
        1000);
    if (!validFileFormat(lstHeader)) {
      resultInSideDTO.setKey(RESULT.FILE_INVALID_FORMAT);
      return resultInSideDTO;
    }
    // Lay du lieu import
    List<Object[]> lstData = CommonImport.getDataFromExcelFile(
        file,
        0,//sheet
        1,//begin row
        0,//from column
        7,//to column
        1000
    );
    if (!lstData.isEmpty()) {
      for (Object[] obj : lstData) {
        if (obj[0] == null) {
          resultInSideDTO.setKey(RESULT.NODATA);
        }
        if (obj[1] == null) {
          resultInSideDTO.setKey(RESULT.NODATA);
        }
        if (obj[2] == null) {
          resultInSideDTO.setKey(RESULT.NODATA);
        }
      }
    } else {
      resultInSideDTO.setKey(RESULT.NODATA);
    }
    return resultInSideDTO;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;
    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 8) {
      return false;
    }
    if (obj0 == null) {
      return false;
    }
    if (obj0[0] == null) {
      return false;
    }
    if (!"key(*)"
        .equalsIgnoreCase(obj0[0].toString().trim())) {
      return false;
    }
    if (!"controlType(*)"
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!"keyCode(*)"
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!"value"
        .equalsIgnoreCase(obj0[3].toString().trim())) {
      return false;
    }
    if (!"format"
        .equalsIgnoreCase(obj0[4].toString().trim())) {
      return false;
    }
    if (!"dataCode"
        .equalsIgnoreCase(obj0[5].toString().trim())) {
      return false;
    }
    if (!"dataType"
        .equalsIgnoreCase(obj0[6].toString().trim())) {
      return false;
    }
    if (!"defaultValue"
        .equalsIgnoreCase(obj0[7].toString().trim())) {
      return false;
    }
    return true;
  }

  @Override
  public List<ObjKeyValueVsmartDTO> getDataHeader(Long systemId, String typeId) {
    List<ObjKeyValueVsmartDTO> lst = new ArrayList<>();
    try {
      String id = cfgWoHelpVsmartRepository.findCfgWoHelpVsmartDTO(systemId, typeId);
      CfgWoHelpVsmartDTO wo = findCfgWoHelpVsmartsById(Long.valueOf(id));
      if (wo != null && !StringUtils.isStringNullOrEmpty(wo.getFileId())) {
        byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), wo.getFileId());
        String fullPathTemp = FileUtils
            .saveTempFile(FileUtils.getFileName(wo.getFileId()), bytes, tempFolder);
        File fileSource = new File(fullPathTemp);
        return getExcelData(fileSource);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  private List<ObjKeyValueVsmartDTO> getExcelData(File fileSource) {
    List<ObjKeyValueVsmartDTO> lstResult = new ArrayList<>();
    List<Object[]> lstDataAll;

    String locale = I18n.getLocale();

    //Read sheet 1
    try {
      lstDataAll = CommonImport.getDataFromExcelFile(fileSource,
          0,//sheet
          1,//begin row
          0,//from column
          7,//to column
          20);
      if (lstDataAll != null) {
        for (Object[] obj : lstDataAll) {
          ObjKeyValueVsmartDTO objDTO = new ObjKeyValueVsmartDTO();
          // Luu key
          if (obj[0] != null && !"".equals(obj[0])) {
            String key = obj[0].toString().trim();
            if (key.contains("#")) {
              String[] keyArr = key.split("#");
              if (locale == null || "vi".equals(locale)) {
                objDTO.setKey(keyArr[0]);
              } else {
                objDTO.setKey(keyArr[keyArr.length - 1]);
              }
            } else {
              objDTO.setKey(key);
            }
          }

          // Luu controlType
          if (obj[1] != null && !"".equals(obj[1])) {
            objDTO.setControlType(obj[1].toString().trim());
          }

          // Luu keyCode
          if (obj[2] != null && !"".equals(obj[2])) {
            objDTO.setKeyCode(obj[2].toString().trim());
          }

          // Luu value
          if (obj[3] != null && !"".equals(obj[3])) {
            objDTO.setValue(obj[3].toString().trim());
          }

          // Luu fomrat
          if (obj[4] != null && !"".equals(obj[4])) {
            objDTO.setFormat(obj[4].toString().trim());
          }

          // Luu dataCode
          if (obj[5] != null && !"".equals(obj[5])) {
            objDTO.setDataCode(obj[5].toString().trim());
          }
          // Luu dataType
          if (obj[6] != null && !"".equals(obj[6])) {
            objDTO.setDataType(obj[6].toString().trim());
          }
          // Luu defaultValue
          if (obj[7] != null && !"".equals(obj[7])) {
            objDTO.setDefaultValue(obj[7].toString().trim());
          }
          objDTO.setType("1");
          lstResult.add(objDTO);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    //Read sheet 2
    try {
      lstDataAll = CommonImport.getDataFromExcelFile(fileSource,
          1,//sheet
          1,//begin row
          0,//from column
          1,//to column
          20);
      if (lstDataAll != null) {
        for (Object[] obj : lstDataAll) {
          ObjKeyValueVsmartDTO objDTO = new ObjKeyValueVsmartDTO();
          // Luu key
          if (obj[0] != null && !"".equals(obj[0])) {
            String key = obj[0].toString().trim();
            if (key.contains("#")) {
              String[] keyArr = key.split("#");
              if (locale == null || "vi".equals(locale)) {
                objDTO.setKey(keyArr[0]);
              } else {
                objDTO.setKey(keyArr[keyArr.length - 1]);
              }
            } else {
              objDTO.setKey(key);
            }
          }
          // Luu value
          if (obj[1] != null && !"".equals(obj[1])) {
            objDTO.setValue(obj[1].toString().trim());
          }
          objDTO.setType("2");
          lstResult.add(objDTO);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstResult;
  }
}
