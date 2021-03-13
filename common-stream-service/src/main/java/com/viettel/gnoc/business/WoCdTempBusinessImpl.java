package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.Users;
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
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.repository.WoCdTempRepository;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import com.viettel.gnoc.wo.dto.WoCdTempInsideDTO;
import java.io.File;
import java.io.InputStream;
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
public class WoCdTempBusinessImpl implements WoCdTempBusiness {

  @Autowired
  protected WoCdTempRepository woCdTempRepository;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Value("${application.temp.folder}")
  private String tempFolder;
  private final static String WO_CD_TEMP_RESULT_IMPORT = "WO_CD_TEMP_RESULT_IMPORT";
  private final static String WO_CD_TEMP_EXPORT = "WO_CD_TEMP_EXPORT";

  @Override
  public Datatable getListWoCdTempDTO(WoCdTempInsideDTO woCdTempInsideDTO) {
    log.debug("Request to getListWoCdTempDTO: {}", woCdTempInsideDTO);
    return woCdTempRepository.getListWoCdTempDTO(woCdTempInsideDTO);
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO) {
    log.debug("Request to getListCdGroupByUser: {}", woCdGroupTypeUserDTO);
    return woCdTempRepository.getListCdGroupByUser(woCdGroupTypeUserDTO);
  }

  @Override
  public List<UsersInsideDto> getListUserByCdGroupCBB(Long woGroupId) {
    List<UsersInsideDto> usersInsideDtoList = new ArrayList<>();
    WoCdGroupUnitDTO dtoGroupUnit = new WoCdGroupUnitDTO();
    dtoGroupUnit.setCdGroupId(woGroupId);
    List<WoCdGroupUnitDTO> lstWoCdGroupUnitDTO = woCdTempRepository
        .getListWoCdGroupUnitDTO(dtoGroupUnit);
    if (lstWoCdGroupUnitDTO != null && !lstWoCdGroupUnitDTO.isEmpty()) {
      UsersInsideDto usersInsideDto = new UsersInsideDto();
      usersInsideDto.setUnitId(lstWoCdGroupUnitDTO.get(0).getUnitId());
      usersInsideDtoList = userRepository.getListUsersDTOS(usersInsideDto);
    }
    return usersInsideDtoList;
  }

  public List<UsersInsideDto> getListUserByCdGroupIsEnable(Long woGroupId) {
    List<UsersInsideDto> usersInsideDtoList = new ArrayList<>();
    List<WoCdGroupUnitDTO> lstWoCdGroupUnitDTO = woCdTempRepository
        .getListUserByCdGroupIsEnable(woGroupId);
    if (lstWoCdGroupUnitDTO != null && !lstWoCdGroupUnitDTO.isEmpty()) {
      for (WoCdGroupUnitDTO dto : lstWoCdGroupUnitDTO) {
        List<Users> listUser = userRepository.getListUserOfUnit(dto.getUnitId());
        if (listUser != null && listUser.size() > 0) {
          for (Users users : listUser) {
            usersInsideDtoList.add(users.toDTO());
          }
        }
      }
    }
    return usersInsideDtoList;
  }

  @Override
  public ResultInSideDto insert(WoCdTempInsideDTO woCdTempInsideDTO) {
    log.debug("Request to add: {}", woCdTempInsideDTO);
    String id = woCdTempRepository.getSeqWoCdTemp("WFM.WO_CD_TEMP_SEQ");
    if (!StringUtils.isStringNullOrEmpty(id)) {
      woCdTempInsideDTO.setWoCdTempId(Long.valueOf(id));
      return woCdTempRepository.add(woCdTempInsideDTO);
    }
    return null;
  }

  @Override
  public ResultInSideDto update(WoCdTempInsideDTO woCdTempInsideDTO) {
    log.debug("Request to update: {}", woCdTempInsideDTO);
    return woCdTempRepository.edit(woCdTempInsideDTO);
  }

  @Override
  public WoCdTempInsideDTO getDetail(Long woCdTempId) {
    log.debug("Request to getDetail: {}", woCdTempId);
    return woCdTempRepository.getDetail(woCdTempId);
  }

  @Override
  public ResultInSideDto delete(WoCdTempInsideDTO woCdTempInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    WoCdDTO woCdDTO = new WoCdDTO();
    woCdDTO.setUserId(userToken.getUserID());
    if (!StringUtils.isStringNullOrEmpty(woCdTempInsideDTO.getStatus())) {
      if (woCdTempInsideDTO.getStatus() == 1L || woCdTempInsideDTO.getStatus() == 2L) {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("woCdTemp.err.deleteStatus"));
        return resultInSideDto;
      }
    }
    List<WoCdDTO> woCdDTOList = woCdTempRepository.getListWoCdDTO(woCdDTO, 0, 10, "", "");
    if (woCdDTOList != null && !woCdDTOList.isEmpty()) {
      List<String> result = new ArrayList<>();
      for (WoCdDTO dto : woCdDTOList) {
        if (dto.getWoGroupId() != null) {
          result.add(String.valueOf(dto.getWoGroupId()));
        }
      }
      if (!StringUtils.isStringNullOrEmpty(woCdTempInsideDTO.getWoGroupId()) && (result == null
          || result.isEmpty() || !result
          .contains(woCdTempInsideDTO.getWoGroupId().toString()))) {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("woCdTemp.err.delete"));
        return resultInSideDto;
      }
      resultInSideDto = woCdTempRepository.delete(woCdTempInsideDTO.getWoCdTempId());
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("woCdTemp.err.delete"));
    }
    return resultInSideDto;
  }

  @Override
  public File exportData(WoCdTempInsideDTO woCdTempInsideDTO) throws Exception {
    List<WoCdTempInsideDTO> woCdGroupInsideDTOList = woCdTempRepository
        .getListDataExport(woCdTempInsideDTO);
    if (woCdGroupInsideDTOList != null && !woCdGroupInsideDTOList.isEmpty()) {
      for (int i = woCdGroupInsideDTOList.size() - 1; i > -1; i--) {
        WoCdTempInsideDTO woCdTempExport = woCdGroupInsideDTOList.get(i);
        if ("1".equals(woCdTempExport.getIsCd().toString())) {
          woCdTempExport.setIsCdStr(I18n.getLanguage("woCdTemp.isCd.1"));
        } else if ("0".equals(woCdTempExport.getIsCd().toString())) {
          woCdTempExport.setIsCdStr(I18n.getLanguage("woCdTemp.isCd.0"));
        }
        if (StringUtils.isStringNullOrEmpty(woCdTempExport.getStatus())) {
          woCdTempExport.setStatusName(I18n.getLanguage("woCdTemp.status.0"));
        } else if ("1".equals(woCdTempExport.getStatus().toString())) {
          woCdTempExport.setStatusName(I18n.getLanguage("woCdTemp.status.1"));
        } else if ("0".equals(woCdTempExport.getStatus().toString())) {
          woCdTempExport.setStatusName(I18n.getLanguage("woCdTemp.status.0"));
        }
        if (woCdTempExport.getStartTime() != null) {
          woCdTempExport
              .setStartTimeStr(DateTimeUtils.date2ddMMyyyyHHMMss(woCdTempExport.getStartTime()));
        }
        if (woCdTempExport.getEndTime() != null) {
          woCdTempExport
              .setEndTimeStr(DateTimeUtils.date2ddMMyyyyHHMMss(woCdTempExport.getEndTime()));
        }
      }
    }
    return exportFileTemplate(woCdGroupInsideDTOList, "");
  }

  private File exportFileTemplate(List<WoCdTempInsideDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("woCdTemp.export.title");
    String title = I18n.getLanguage("woCdTemp.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("woGroupName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("username", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("startTimeStr", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("endTimeStr", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("isCdStr", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = WO_CD_TEMP_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("woCdTemp.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      columnSheet = new ConfigHeaderExport("statusName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      fileNameOut = WO_CD_TEMP_EXPORT;
      subTitle = I18n
          .getLanguage("woCdTemp.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.woCdTemp"
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
        I18n.getLanguage("woCdTemp.woGroupName"),
        I18n.getLanguage("woCdTemp.username"),
        I18n.getLanguage("woCdTemp.startTimeStr"),
        I18n.getLanguage("woCdTemp.endTimeStr"),
        I18n.getLanguage("woCdTemp.isCdStr")

    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("woCdTemp.woGroupName"),
        I18n.getLanguage("woCdTemp.username"),
        I18n.getLanguage("woCdTemp.startTimeStr"),
        I18n.getLanguage("woCdTemp.endTimeStr")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int isCdStrColumn = listHeader.indexOf(I18n.getLanguage("woCdTemp.isCdStr"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("woCdTemp.title"));
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
    XSSFRichTextString groupId = new XSSFRichTextString(I18n.getLanguage("woCdTemp.woGroupId"));
    XSSFRichTextString groupName = new XSSFRichTextString(I18n.getLanguage("woCdTemp.woGroupName"));
    headerCellGroupId.setCellValue(groupId);
    headerCellGroupId.setCellStyle(style.get("header"));
    headerCellGroupName.setCellValue(groupName);
    headerCellGroupName.setCellStyle(style.get("header"));
    sheetParam1.setColumnWidth(1, 12000);
    sheetParam1.setColumnWidth(2, 20000);
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown

    int row = 1;
    List<WoCdGroupInsideDTO> woGroupList = woCdTempRepository.getListCdGroupByUser(null);
    for (WoCdGroupInsideDTO dto : woGroupList) {
      excelWriterUtils
          .createCell(sheetParam1, 0, row, dto.getWoGroupId().toString(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam1, 1, row++, dto.getWoGroupName(), style.get("cell"));
    }
    sheetParam1.autoSizeColumn(0);
    sheetParam1.autoSizeColumn(1);

    row = 5;
    excelWriterUtils.createCell(sheetParam, 5, row++, I18n.getLanguage("woCdTemp.isCd.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 5, row++, I18n.getLanguage("woCdTemp.isCd.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name isCdStr = workbook.createName();
    isCdStr.setNameName("isCdStr");
    isCdStr.setRefersToFormula("param!$F$2:$F$" + row);
    XSSFDataValidationConstraint isCdStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isCdStr");
    CellRangeAddressList isCdStrCreate = new CellRangeAddressList(5, 65000, isCdStrColumn,
        isCdStrColumn);
    XSSFDataValidation dataValidationIsCdStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isCdStrConstraint, isCdStrCreate);
    dataValidationIsCdStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationIsCdStr);

//    row = 5;
//    excelWriterUtils.createCell(sheetParam, 6, row++, I18n.getLanguage("srRoleUser.isLeader.1")
//        , style.get("cell"));
//    excelWriterUtils.createCell(sheetParam, 6, row++, I18n.getLanguage("srRoleUser.isLeader.0")
//        , style.get("cell"));
//    sheetParam.autoSizeColumn(1);
//    Name isLeaderStr = workbook.createName();
//    isLeaderStr.setNameName("isLeaderStr");
//    isLeaderStr.setRefersToFormula("param!$G$2:$G$" + row);
//    XSSFDataValidationConstraint isLeaderStrConstraint = new XSSFDataValidationConstraint(
//        DataValidationConstraint.ValidationType.LIST, "isLeaderStr");
//    CellRangeAddressList isLeaderStrCreate = new CellRangeAddressList(5, 65000, isLeaderStrColumn,
//        isLeaderStrColumn);
//    XSSFDataValidation dataValidationIsLeaderStr = (XSSFDataValidation) dataValidationHelper
//        .createValidation(
//            isLeaderStrConstraint, isLeaderStrCreate);
//    dataValidationIsLeaderStr.setShowErrorBox(true);
//    sheetOne.addValidationData(dataValidationIsLeaderStr);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("woCdTemp.title"));
    workbook.setSheetName(2, I18n.getLanguage("woCdTemp.woGroupName"));
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
    List<WoCdTempInsideDTO> woCdTempInsideDTOList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    boolean checkDouble = true;
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
          for (Object[] obj : dataImportList) {
            WoCdTempInsideDTO woCdTempInsideDTO = new WoCdTempInsideDTO();
            if (obj[1] != null) {
              if (!DataUtil.isNumber(obj[1].toString().trim())) {
                woCdTempInsideDTO.setResultImport(I18n.getLanguage("woCdTemp.errType.woGroupId"));
                woCdTempInsideDTO.setWoGroupIdStr(obj[1].toString().trim());
                woCdTempInsideDTO.setWoGroupName(obj[1].toString().trim());
              } else {
                woCdTempInsideDTO.setWoGroupIdStr(obj[1].toString().trim());
                woCdTempInsideDTO.setWoGroupId(Long.valueOf(obj[1].toString().trim()));
                woCdTempInsideDTO.setWoGroupName(obj[1].toString().trim());
              }
            } else {
              woCdTempInsideDTO.setWoGroupIdStr(null);
            }
            if (obj[2] != null) {
              woCdTempInsideDTO.setUsername(obj[2].toString().trim());
            } else {
              woCdTempInsideDTO.setUsername(null);
            }
            if (obj[3] != null) {
              try {
                woCdTempInsideDTO.setStartTime(DateUtil.string2DateTime(obj[3].toString().trim()));
                woCdTempInsideDTO.setStartTimeStr(obj[3].toString().trim());
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                woCdTempInsideDTO.setStartTimeStr(obj[3].toString().trim());
                woCdTempInsideDTO
                    .setResultImport(I18n.getLanguage("woCdTemp.err.startTime.validateForm"));
              }
            } else {
              woCdTempInsideDTO.setStartTime(null);
            }
            if (obj[4] != null) {
              try {
                woCdTempInsideDTO.setEndTime(DateUtil.string2DateTime(obj[4].toString().trim()));
                woCdTempInsideDTO.setEndTimeStr(obj[4].toString().trim());
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                woCdTempInsideDTO.setEndTimeStr(obj[4].toString().trim());
                woCdTempInsideDTO
                    .setResultImport(I18n.getLanguage("woCdTemp.err.endTime.validateForm"));
              }
            } else {
              woCdTempInsideDTO.setEndTime(null);
            }
            if (obj[5] != null) {
              woCdTempInsideDTO.setIsCdStr(obj[5].toString().trim());
              if (I18n.getLanguage("woCdTemp.isCd.1")
                  .equals(woCdTempInsideDTO.getIsCdStr())) {
                woCdTempInsideDTO.setIsCd(Long.valueOf(1));
              } else if (I18n.getLanguage("woCdTemp.isCd.0")
                  .equals(woCdTempInsideDTO.getIsCdStr())) {
                woCdTempInsideDTO.setIsCd(Long.valueOf(0));
              } else {
                woCdTempInsideDTO.setIsCd(null);
              }
            } else {
              woCdTempInsideDTO.setIsCdStr(null);
              woCdTempInsideDTO.setIsCd(0L);
            }
            WoCdTempInsideDTO woCdTempInsideDTOTmp;
            if (woCdTempInsideDTO.getResultImport() == null) {
              woCdTempInsideDTOTmp = validateImportInfo(woCdTempInsideDTO, woCdTempInsideDTOList);
            } else {
              woCdTempInsideDTOTmp = woCdTempInsideDTO.clone();
              woCdTempInsideDTOTmp.setResultImport(woCdTempInsideDTO.getResultImport());
            }
            if (woCdTempInsideDTOTmp.getResultImport() == null) {
              woCdTempInsideDTOTmp
                  .setResultImport(I18n.getLanguage("woCdTemp.result.import"));
              woCdTempInsideDTOList.add(woCdTempInsideDTOTmp);
//              srRoleUserDTOS.add(srRoleUserDTO);
            } else {
              woCdTempInsideDTOList.add(woCdTempInsideDTO);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!woCdTempInsideDTOList.isEmpty()) {
              for (WoCdTempInsideDTO dto : woCdTempInsideDTOList) {
                String id = woCdTempRepository.getSeqWoCdTemp("WFM.WO_CD_TEMP_SEQ");
                if (!StringUtils.isStringNullOrEmpty(id)) {
                  dto.setWoCdTempId(Long.valueOf(id));
                  resultInSideDto = woCdTempRepository.add(dto);
                }
              }
            }
          } else {
            File fileExport = exportFileTemplate(woCdTempInsideDTOList, Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileTemplate(woCdTempInsideDTOList, Constants.RESULT_IMPORT);
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
    if (!(I18n.getLanguage("woCdTemp.woGroupName") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCdTemp.username") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCdTemp.startTimeStr") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCdTemp.endTimeStr") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("woCdTemp.isCdStr")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    return true;
  }

  private WoCdTempInsideDTO validateImportInfo(WoCdTempInsideDTO woCdTempInsideDTO,
      List<WoCdTempInsideDTO> list) {
    if (StringUtils.isStringNullOrEmpty(woCdTempInsideDTO.getWoGroupIdStr())) {
      woCdTempInsideDTO.setResultImport(I18n.getLanguage("woCdTemp.err.woGroupIdStr"));
      return woCdTempInsideDTO;
    }

    if (StringUtils.isStringNullOrEmpty(woCdTempInsideDTO.getUsername())) {
      woCdTempInsideDTO.setResultImport(I18n.getLanguage("woCdTemp.err.username"));
      return woCdTempInsideDTO;
    }

    if (woCdTempInsideDTO.getStartTime() == null) {
      woCdTempInsideDTO.setResultImport(I18n.getLanguage("woCdTemp.err.startTime"));
      return woCdTempInsideDTO;
    } else {
      if (woCdTempInsideDTO.getStartTime().getTime() < new Date().getTime()) {
        woCdTempInsideDTO.setResultImport(I18n.getLanguage("woCdTemp.err.startTime.compareNow"));
        return woCdTempInsideDTO;
      }
    }

    if (woCdTempInsideDTO.getEndTime() == null) {
      woCdTempInsideDTO.setResultImport(I18n.getLanguage("woCdTemp.err.endTime"));
      return woCdTempInsideDTO;
    } else {
      if (woCdTempInsideDTO.getEndTime().getTime() < woCdTempInsideDTO.getStartTime().getTime()) {
        woCdTempInsideDTO
            .setResultImport(I18n.getLanguage("woCdTemp.err.endTime.compareStartTime"));
        return woCdTempInsideDTO;
      } else {
        if (woCdTempInsideDTO.getEndTime().getTime() - woCdTempInsideDTO.getStartTime().getTime()
            > 24 * 60 * 60 * 1000L) {
          woCdTempInsideDTO
              .setResultImport(I18n.getLanguage("woCdTemp.err.validate.time"));
          return woCdTempInsideDTO;
        }
      }
    }

    if (StringUtils.isStringNullOrEmpty(woCdTempInsideDTO.getIsCd()) && !StringUtils
        .isStringNullOrEmpty(woCdTempInsideDTO.getIsCdStr())) {
      woCdTempInsideDTO.setResultImport(I18n.getLanguage("woCdTemp.err.isCd.exist"));
      return woCdTempInsideDTO;
    }
    UsersInsideDto usersInsideDto = new UsersInsideDto();
    usersInsideDto.setUsername(woCdTempInsideDTO.getUsername());
    List<UsersInsideDto> userDTOList = userRepository.getListUsersDTOS(usersInsideDto);
    if (userDTOList == null || userDTOList.isEmpty()) {
      woCdTempInsideDTO.setResultImport(I18n.getLanguage("woCdTemp.err.username.exist"));
      return woCdTempInsideDTO;
    }
    List<UsersInsideDto> listUser = getListUserByCdGroupIsEnable(woCdTempInsideDTO.getWoGroupId());
    if (listUser != null && !listUser.isEmpty()) {
      boolean check = false;
      Long userId = null;
      for (UsersInsideDto dto : listUser) {
        if (dto.getUsername().equals(woCdTempInsideDTO.getUsername())) {
          check = true;
          userId = dto.getUserId();
          break;
        }
      }
      if (check) {
        woCdTempInsideDTO.setUserId(userId);
      } else {
        woCdTempInsideDTO
            .setResultImport(I18n.getLanguage("woCdTemp.err.usernameMapWoGroup.exist"));
        return woCdTempInsideDTO;
      }
    } else {
      woCdTempInsideDTO.setResultImport(I18n.getLanguage("woCdTemp.err.usernameMapWoGroup.exist"));
      return woCdTempInsideDTO;
    }
    if (list != null && list.size() > 0 && woCdTempInsideDTO.getResultImport() == null) {
      woCdTempInsideDTO = validateDuplicate(list, woCdTempInsideDTO);
    }

    return woCdTempInsideDTO;
  }

  private WoCdTempInsideDTO validateDuplicate(List<WoCdTempInsideDTO> list,
      WoCdTempInsideDTO woCdTempInsideDTO) {
    for (int i = 0; i < list.size(); i++) {
      WoCdTempInsideDTO woCdTempInsideDTOTmp = list.get(i);
      if (I18n.getLanguage("woCdTemp.result.import")
          .equals(woCdTempInsideDTOTmp.getResultImport()) && woCdTempInsideDTOTmp.getWoGroupId()
          .toString()
          .equals(woCdTempInsideDTO.getWoGroupId().toString())
          && woCdTempInsideDTOTmp.getUsername().equals(
          woCdTempInsideDTO.getUsername())) {
        woCdTempInsideDTO
            .setResultImport(I18n.getLanguage("woCdTemp.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return woCdTempInsideDTO;
  }

}
