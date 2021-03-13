package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
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
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.repository.SRRoleUserRepository;
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
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
import org.apache.poi.ss.usermodel.Drawing;
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
public class SRRoleUserBusinessImpl implements SRRoleUserBusiness {

  @Autowired
  protected SRRoleUserRepository srRoleUserRepository;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  protected UnitBusiness unitBusiness;

  @Autowired
  protected SRRoleBusiness srRoleBusiness;

  @Autowired
  protected UserRepository userRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;
  List<SRRoleUserInSideDTO> srRoleUserDTOS = new ArrayList<>();
  Map<String, String> mapCountryName = new HashMap<>();
  Map<String, String> mapUnitName = new HashMap<>();
  Map<String, String> mapRoleCode = new HashMap<>();
  private final static String SR_ROLE_USER_RESULT_IMPORT = "SR_ROLE_USER_RESULT_IMPORT";
  private final static String SR_ROLE_USER_EXPORT = "SR_ROLE_USER_EXPORT";

  @Override
  public Datatable getListSRRoleUserPage(SRRoleUserInSideDTO srRoleUserDTO) {
    log.debug("Request to getListSRRoleUserPage : {}", srRoleUserDTO);
    return srRoleUserRepository.getListSRRoleUserPage(srRoleUserDTO);
  }

  @Override
  public List<SRRoleUserInSideDTO> getListSRRoleUser(SRRoleUserInSideDTO srRoleUserDTO) {
    log.debug("Request to getListSRRoleUser : {}", srRoleUserDTO);
    return srRoleUserRepository.getListSRRoleUser(srRoleUserDTO);
  }

  @Override
  public ResultInSideDto insert(SRRoleUserInSideDTO srRoleUserDTO) {
    log.debug("Request to insert : {}", srRoleUserDTO);
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<SRRoleUserInSideDTO> srRoleUserDTOS = new ArrayList<>();
    boolean check = true;
    if (srRoleUserDTO.getUsernameStr() != null && srRoleUserDTO.getIsLeaderStr() != null) {
      String[] strIsLeader = srRoleUserDTO.getIsLeaderStr().split(",");
      String[] strUsername = srRoleUserDTO.getUsernameStr().split(",");
      List<SRRoleUserInSideDTO> srRoleUserDTOList = new ArrayList<>();
      if (strIsLeader != null && strUsername != null) {
        for (int i = 0; i < strIsLeader.length; i++) {
          for (int j = 0; j < strUsername.length; j++) {
            if (i == j) {
              SRRoleUserInSideDTO dto = new SRRoleUserInSideDTO();
              dto.setIsLeader(Long.valueOf(strIsLeader[i]));
              dto.setUsername(strUsername[j]);
              srRoleUserDTOList.add(dto);
            }
          }
        }
      }
      for (SRRoleUserInSideDTO item : srRoleUserDTOList) {
        item.setCountry(srRoleUserDTO.getCountry());
        item.setUnitId(srRoleUserDTO.getUnitId());
        item.setRoleCode(srRoleUserDTO.getRoleCode());
        item.setStatus("A");
        item.setCreatedUser(userToken.getUserName());
        item.setCreatedTime(new Date(System.currentTimeMillis()));
        srRoleUserDTOS.add(item);
        if (srRoleUserRepository
            .checkRoleUserExist(srRoleUserDTO.getCountry(), srRoleUserDTO.getRoleCode(),
                item.getUsername(), srRoleUserDTO.getUnitId()) != null) {
          resultInSideDto.setKey(RESULT.DUPLICATE);
          resultInSideDto.setMessage(item.getUsername());
          check = false;
          return resultInSideDto;
        }
      }
      if (check) {
        for (SRRoleUserInSideDTO dto : srRoleUserDTOS) {
          resultInSideDto = srRoleUserRepository.add(dto);
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(SRRoleUserInSideDTO srRoleUserDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    srRoleUserDTO.setUpdatedUser(userToken.getUserName());
    srRoleUserDTO.setUpdatedTime(new Date(System.currentTimeMillis()));
    SRRoleUserInSideDTO userDTO = srRoleUserRepository
        .checkRoleUserExist(srRoleUserDTO.getCountry(), srRoleUserDTO.getRoleCode(),
            srRoleUserDTO.getUsername(), srRoleUserDTO.getUnitId());
    if (userDTO != null) {
      if (!userDTO.getRoleUserId().toString().equals(srRoleUserDTO.getRoleUserId().toString())) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto.setMessage(srRoleUserDTO.getUsername());
        return resultInSideDto;
      }
    }
    return srRoleUserRepository.edit(srRoleUserDTO);
  }

  @Override
  public ResultInSideDto delete(Long roleUserId) {
    log.debug("Request to delete : {}", roleUserId);
    return srRoleUserRepository.delete(roleUserId);
  }

  @Override
  public SRRoleUserInSideDTO getDetail(Long roleUserId) {
    log.debug("Request to getDetail : {}", roleUserId);
    return srRoleUserRepository.getDetail(roleUserId);
  }

  public void setMapCountryName() {
    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstCountryName) {
        mapCountryName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }

  public void setMapUnitName() {
    List<UnitDTO> lstUnitName = unitBusiness.getListUnit(null);
    if (lstUnitName != null && !lstUnitName.isEmpty()) {
      for (UnitDTO dto : lstUnitName) {
        mapUnitName.put(String.valueOf(dto.getUnitId()), dto.getUnitName());
      }
    }
  }

  public void setMapRoleCode(String country) {
    SRRoleDTO srRoleDTO = new SRRoleDTO();
    srRoleDTO.setCountry(country);
    List<SRRoleDTO> lstRoleCode = srRoleBusiness.getListSRRoleByLocationCBB(srRoleDTO);
    if (lstRoleCode != null && !lstRoleCode.isEmpty()) {
      for (SRRoleDTO dto : lstRoleCode) {
        mapRoleCode.put(String.valueOf(dto.getRoleId()), dto.getRoleCode());
      }
    }
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
    XSSFSheet sheetParam2 = workbook.createSheet("param2");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("srRoleUser.countryName"),
        I18n.getLanguage("srRoleUser.unitName"),
        I18n.getLanguage("srRoleUser.roleCode"),
        I18n.getLanguage("srRoleUser.userName"),
        I18n.getLanguage("srRoleUser.isLeaderStr"),
        I18n.getLanguage("srRoleUser.actionName")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("srRoleUser.countryName"),
        I18n.getLanguage("srRoleUser.unitName"),
        I18n.getLanguage("srRoleUser.roleCode"),
        I18n.getLanguage("srRoleUser.userName"),
        I18n.getLanguage("srRoleUser.isLeaderStr"),
        I18n.getLanguage("srRoleUser.actionName")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int countryNameColumn = listHeader.indexOf(I18n.getLanguage("srRoleUser.countryName"));
    int isLeaderStrColumn = listHeader.indexOf(I18n.getLanguage("srRoleUser.isLeaderStr"));
    int actionNameColumn = listHeader.indexOf(I18n.getLanguage("srRoleUser.actionName"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("srRoleUser.title"));
    titleCell.setCellStyle(style.get("title"));

    Row firstLeftHeaderRow = sheetOne.createRow(0);
    Cell firstLeftHeaderCell = firstLeftHeaderRow.createCell(2);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.firstLeftHeader"));
    firstLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Row secondLeftHeaderRow = sheetOne.createRow(1);
    Cell secondLeftHeaderCell = secondLeftHeaderRow.createCell(2);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.secondLeftHeader"));
    secondLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell firstRightHeaderCell = firstLeftHeaderRow.createCell(6);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstRightHeaderCell.setCellValue(I18n.getLanguage("common.export.firstRightHeader"));
    firstRightHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell secondRightHeaderCell = secondLeftHeaderRow.createCell(6);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondRightHeaderCell.setCellValue(I18n.getLanguage("common.export.secondRightHeader"));
    secondRightHeaderCell.setCellStyle(style.get("indexTitle"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(4);
    Row headerUnit = sheetParam1.createRow(0);
    Row headerRole = sheetParam2.createRow(0);
    headerRow.setHeightInPoints(16);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }
      if (i == 2) {
        Drawing drawing = headerCell.getSheet().createDrawingPatriarch();
        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(i + 1);
        anchor.setCol2(i + 2);
        anchor.setRow1(3);
        anchor.setRow2(4);
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(factory.createRichTextString("Nhập ID đơn vị"));
        headerCell.setCellComment(comment);
      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }
    Cell headerCellStt = headerUnit.createCell(0);
    Cell headerCellCountry = headerRole.createCell(0);
    Cell headerCellUnitId = headerUnit.createCell(1);
    Cell headerCellUnit = headerUnit.createCell(2);
    Cell headerCellRole = headerRole.createCell(1);
    XSSFRichTextString stt = new XSSFRichTextString(I18n.getLanguage("common.STT"));
    XSSFRichTextString unitId = new XSSFRichTextString(I18n.getLanguage("srRoleUser.unitId"));
    XSSFRichTextString unit = new XSSFRichTextString(I18n.getLanguage("srRoleUser.unitName"));
    XSSFRichTextString country = new XSSFRichTextString(I18n.getLanguage("srRoleUser.countryName"));
    XSSFRichTextString role = new XSSFRichTextString(I18n.getLanguage("srRoleUser.roleCode"));
    headerCellStt.setCellValue(stt);
    headerCellStt.setCellStyle(style.get("header"));
    headerCellCountry.setCellValue(country);
    headerCellCountry.setCellStyle(style.get("header"));
    headerCellUnitId.setCellValue(unitId);
    headerCellUnitId.setCellStyle(style.get("header"));
    headerCellUnit.setCellValue(unit);
    headerCellUnit.setCellStyle(style.get("header"));
    headerCellRole.setCellValue(role);
    headerCellRole.setCellStyle(style.get("header"));
    sheetParam1.setColumnWidth(1, 12000);
    sheetParam1.setColumnWidth(2, 20000);
    sheetParam2.setColumnWidth(0, 10000);
    sheetParam2.setColumnWidth(1, 12000);
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown
    int row = 5;

    List<ItemDataCRInside> countryNameList = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    for (ItemDataCRInside dto : countryNameList) {
      excelWriterUtils.createCell(sheetParam, 1, row++, dto.getDisplayStr(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);

    Name countryName = workbook.createName();
    countryName.setNameName("countryName");
    countryName.setRefersToFormula("param!$B$2:$B$" + row);

    XSSFDataValidationConstraint countryNameConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "countryName");
    CellRangeAddressList countryNameCreate = new CellRangeAddressList(5, 65000, countryNameColumn,
        countryNameColumn);
    XSSFDataValidation dataValidationCountryName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            countryNameConstraint, countryNameCreate);
    dataValidationCountryName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationCountryName);

    row = 1;
    List<UnitDTO> unitNameList = unitBusiness.getListUnit(null);
    for (UnitDTO dto : unitNameList) {
      excelWriterUtils
          .createCell(sheetParam1, 1, row, dto.getUnitId().toString(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam1, 2, row++, dto.getUnitName(), style.get("cell"));
    }
    for (row = 1; row <= unitNameList.size(); row++) {
      excelWriterUtils.createCell(sheetParam1, 0, row, String.valueOf(row), style.get("cell"));
    }
    sheetParam1.autoSizeColumn(0);
    sheetParam1.autoSizeColumn(1);

    row = 1;
    for (ItemDataCRInside dto : countryNameList) {
      excelWriterUtils.createCell(sheetParam2, 0, row, dto.getDisplayStr(), style.get("cell"));
      SRRoleDTO srRoleDTO = new SRRoleDTO();
      srRoleDTO.setCountry(dto.getValueStr().toString());
      List<SRRoleDTO> srRoleDTOList = srRoleBusiness.getListSRRoleByLocationCBB(srRoleDTO);
      for (SRRoleDTO sr : srRoleDTOList) {
        excelWriterUtils.createCell(sheetParam2, 1, row++, sr.getRoleCode(), style.get("cell"));
      }
      excelWriterUtils.createCell(sheetParam2, 0, row++, null, style.get("cell"));
    }
    sheetParam1.autoSizeColumn(0);
    sheetParam1.autoSizeColumn(1);

    row = 5;
    excelWriterUtils.createCell(sheetParam, 5, row++, I18n.getLanguage("srRoleUser.isLeader.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 5, row++, I18n.getLanguage("srRoleUser.isLeader.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name isLeaderStr = workbook.createName();
    isLeaderStr.setNameName("isLeaderStr");
    isLeaderStr.setRefersToFormula("param!$F$2:$F$" + row);
    XSSFDataValidationConstraint isLeaderStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isLeaderStr");
    CellRangeAddressList isLeaderStrCreate = new CellRangeAddressList(5, 65000, isLeaderStrColumn,
        isLeaderStrColumn);
    XSSFDataValidation dataValidationIsLeaderStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isLeaderStrConstraint, isLeaderStrCreate);
    dataValidationIsLeaderStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationIsLeaderStr);

    row = 5;
    excelWriterUtils.createCell(sheetParam, 6, row++, I18n.getLanguage("srRoleUser.action.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 6, row++, I18n.getLanguage("srRoleUser.action.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name actionName = workbook.createName();
    actionName.setNameName("actionName");
    actionName.setRefersToFormula("param!$G$2:$G$" + row);
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
    workbook.setSheetName(0, I18n.getLanguage("srRoleUser.title"));
    workbook.setSheetName(2, I18n.getLanguage("srRoleUser.unitName"));
    workbook.setSheetName(3, I18n.getLanguage("srRoleUser.role"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_SR_ROLE_USER" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File exportData(SRRoleUserInSideDTO srRoleUserDTO) throws Exception {
    List<SRRoleUserInSideDTO> srRoleUserDTOList = (List<SRRoleUserInSideDTO>) srRoleUserRepository
        .getListDataExport(srRoleUserDTO).getData();
    if (srRoleUserDTOList != null && !srRoleUserDTOList.isEmpty()) {
      for (int i = srRoleUserDTOList.size() - 1; i > -1; i--) {
        SRRoleUserInSideDTO srRoleUserExport = srRoleUserDTOList.get(i);
        if ("1".equals(String.valueOf(srRoleUserExport.getIsLeader()))) {
          srRoleUserExport.setIsLeaderStr(I18n.getLanguage("srRoleUser.isLeader.1"));
        } else if ("0".equals(String.valueOf(srRoleUserExport.getIsLeader()))) {
          srRoleUserExport.setIsLeaderStr(I18n.getLanguage("srRoleUser.isLeader.0"));
        }

        if ("A".equals(srRoleUserExport.getStatus())) {
          srRoleUserExport.setStatusName(I18n.getLanguage("srRoleUser.status.A"));
        } else if ("I".equals(srRoleUserExport.getStatus())) {
          srRoleUserExport.setStatusName(I18n.getLanguage("srRoleUser.status.I"));
        }
      }
    }
    return exportFileTemplate(srRoleUserDTOList, "");
  }


  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<SRRoleUserInSideDTO> srRoleUserDTOList = new ArrayList<>();
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
            6,
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
            6,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }

        srRoleUserDTOS = new ArrayList<>();

        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          setMapCountryName();
          setMapUnitName();
          for (Object[] obj : dataImportList) {
            SRRoleUserInSideDTO srRoleUserDTO = new SRRoleUserInSideDTO();
            if (obj[1] != null) {
              srRoleUserDTO.setCountryName(obj[1].toString().trim());
              for (Map.Entry<String, String> item : mapCountryName.entrySet()) {
                if (srRoleUserDTO.getCountryName().equals(item.getValue())) {
                  srRoleUserDTO.setCountry(item.getKey());
                  break;
                } else {
                  srRoleUserDTO.setCountry(null);
                }
              }
            } else {
              srRoleUserDTO.setCountryName(null);
            }
            if (obj[2] != null) {
              if (!DataUtil.isInteger(obj[2].toString().trim())) {
                srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.errType.unitId"));
                srRoleUserDTO.setUnitIdStr(obj[2].toString().trim());
              } else {
                srRoleUserDTO.setUnitIdStr(obj[2].toString().trim());
                srRoleUserDTO.setUnitId(Long.valueOf(obj[2].toString().trim()));
              }
            } else {
              srRoleUserDTO.setUnitIdStr(null);
            }
            if (srRoleUserDTO.getCountry() != null) {
              setMapRoleCode(srRoleUserDTO.getCountry());
              if (obj[3] != null) {
                boolean check = true;
                srRoleUserDTO.setRoleCode(obj[3].toString().trim());
                srRoleUserDTO.setRoleCodeStr(srRoleUserDTO.getRoleCode());
                for (Map.Entry<String, String> item : mapRoleCode.entrySet()) {
                  if (srRoleUserDTO.getRoleCode().equals(item.getValue())) {
                    srRoleUserDTO.setRoleCode(item.getValue());
                    check = true;
                    break;
                  } else {
                    check = false;
                  }
                }
                if (!check) {
                  srRoleUserDTO.setRoleCode(null);
                }
              } else {
                srRoleUserDTO.setRoleCodeStr(null);
              }
            } else {
              srRoleUserDTO.setRoleCodeStr(obj[3].toString().trim());
              srRoleUserDTO.setRoleCode(obj[3].toString().trim());
            }
            if (obj[4] != null) {
              srRoleUserDTO.setUsername(obj[4].toString().trim());
            } else {
              srRoleUserDTO.setUsername(null);
            }
            if (obj[5] != null) {
              srRoleUserDTO.setIsLeaderStr(obj[5].toString().trim());
              if (I18n.getLanguage("srRoleUser.isLeader.1")
                  .equals(srRoleUserDTO.getIsLeaderStr())) {
                srRoleUserDTO.setIsLeader(Long.valueOf(1));
              } else if (I18n.getLanguage("srRoleUser.isLeader.0")
                  .equals(srRoleUserDTO.getIsLeaderStr())) {
                srRoleUserDTO.setIsLeader(Long.valueOf(0));
              } else {
                srRoleUserDTO.setIsLeader(null);
              }
            } else {
              srRoleUserDTO.setIsLeaderStr(null);
            }
            if (obj[6] != null) {
              srRoleUserDTO.setActionName(obj[6].toString().trim());
              srRoleUserDTO.setAction(srRoleUserDTO.getActionName());
              if (!(I18n.getLanguage("srRoleUser.action.1")
                  .equals(srRoleUserDTO.getActionName()) || I18n.getLanguage("srRoleUser.action.0")
                  .equals(srRoleUserDTO.getActionName()))) {
                srRoleUserDTO.setAction(null);
              }
            } else {
              srRoleUserDTO.setActionName(null);
            }
            srRoleUserDTO.setStatus("A");
            SRRoleUserInSideDTO srRoleUserExportDTOTmp = validateImportInfo(
                srRoleUserDTO, srRoleUserDTOList);

            if (srRoleUserExportDTOTmp.getResultImport() == null) {
              srRoleUserExportDTOTmp
                  .setResultImport(I18n.getLanguage("srRoleUser.result.import"));
              srRoleUserDTOList.add(srRoleUserExportDTOTmp);
              srRoleUserDTOS.add(srRoleUserDTO);
            } else {
              srRoleUserDTOList.add(srRoleUserExportDTOTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!srRoleUserDTOList.isEmpty()) {
              for (SRRoleUserInSideDTO dto : srRoleUserDTOList) {
                UserToken userToken = ticketProvider.getUserToken();
                dto.setCreatedUser(userToken.getUserName());
                dto.setCreatedTime(new Date(System.currentTimeMillis()));
              }
              resultInSideDto = srRoleUserRepository
                  .insertOrUpdateList(srRoleUserDTOList);
            }
          } else {
            File fileExport = exportFileTemplate(srRoleUserDTOList, Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileTemplate(srRoleUserDTOList, Constants.RESULT_IMPORT);
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

  @Override
  public List<SRRoleUserInSideDTO> getListUser(String unitId, String country, String username) {
    return srRoleUserRepository.getListUser(unitId, country, username);
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
    if (count != 7) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srRoleUser.countryName") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srRoleUser.unitName") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srRoleUser.roleCode") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srRoleUser.userName") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srRoleUser.isLeaderStr") + "*")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srRoleUser.actionName") + "*")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    return true;
  }

  private SRRoleUserInSideDTO validateImportInfo(SRRoleUserInSideDTO srRoleUserDTO,
      List<SRRoleUserInSideDTO> list) {
    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getCountryName())) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.countryName"));
      return srRoleUserDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUnitIdStr())) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.unitStr"));
      return srRoleUserDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getRoleCodeStr())) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.roleCode"));
      return srRoleUserDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUsername())) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.userName"));
      return srRoleUserDTO;
    }
    if (!(StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUsername()))
        && srRoleUserDTO.getUsername().length() > 200) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.userName.tooLong"));
      return srRoleUserDTO;
    }
    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getIsLeaderStr())) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.isLeaderStr"));
      return srRoleUserDTO;
    }
    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getActionName())) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.action"));
      return srRoleUserDTO;
    }
    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getCountry())) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.country.exist"));
      return srRoleUserDTO;
    }
    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getIsLeader())) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.isLeader.exist"));
      return srRoleUserDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUnitId())) {
      boolean check = false;
      List<UnitDTO> unitNameList = unitBusiness.getListUnit(null);
      for (UnitDTO dto : unitNameList) {
        if (!StringUtils.isStringNullOrEmpty(dto.getUnitId()) && srRoleUserDTO.getUnitId()
            .toString()
            .equals(dto.getUnitId().toString())) {
          check = true;
          break;
        }
      }
      if (!check) {
        srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.unit.exist"));
        return srRoleUserDTO;
      }
    }
    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getRoleCode())) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.roleCode.exist"));
      srRoleUserDTO.setRoleCode(srRoleUserDTO.getRoleCodeStr());
      return srRoleUserDTO;
    }
    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getAction())) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.action.exist"));
      return srRoleUserDTO;
    }
    UsersInsideDto usersInsideDto = new UsersInsideDto();
    usersInsideDto.setUsername(srRoleUserDTO.getUsername());
    List<UsersInsideDto> userDTOList = userRepository.getListUsersDTOS(usersInsideDto);
    if (userDTOList == null || userDTOList.isEmpty()) {
      srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.username.exist"));
      return srRoleUserDTO;
    }
    SRRoleUserInSideDTO srRoleUserTmp = srRoleUserRepository
        .checkRoleUserExist(srRoleUserDTO.getCountry()
            , srRoleUserDTO.getRoleCode()
            , srRoleUserDTO.getUsername()
            , srRoleUserDTO.getUnitId());
    if (srRoleUserTmp != null) {
      if (I18n.getLanguage("srRoleUser.action.1").equals(srRoleUserDTO.getAction())) {
        srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.duplicate"));
        return srRoleUserDTO;
      }
      if (I18n.getLanguage("srRoleUser.action.0").equals(srRoleUserDTO.getAction())) {
        srRoleUserDTO.setRoleUserId(srRoleUserTmp.getRoleUserId());
      }

    } else {
      if (I18n.getLanguage("srRoleUser.action.0").equals(srRoleUserDTO.getAction())) {
        srRoleUserDTO.setResultImport(I18n.getLanguage("srRoleUser.err.noData"));
        return srRoleUserDTO;
      }
    }

    if (list != null && list.size() > 0 && srRoleUserDTO.getResultImport() == null) {
      srRoleUserDTO = validateDuplicate(list, srRoleUserDTO);
    }

    return srRoleUserDTO;
  }

  private SRRoleUserInSideDTO validateDuplicate(List<SRRoleUserInSideDTO> list,
      SRRoleUserInSideDTO srRoleUserDTO) {
    for (int i = 0; i < list.size(); i++) {
      SRRoleUserInSideDTO roleUserDTOTmp = list.get(i);
      if (I18n.getLanguage("srRoleUser.result.import")
          .equals(roleUserDTOTmp.getResultImport()) && roleUserDTOTmp.getCountryName()
          .equals(srRoleUserDTO.getCountryName())
          && roleUserDTOTmp.getUnitIdStr().equals(
          srRoleUserDTO.getUnitIdStr()) && roleUserDTOTmp.getRoleCode()
          .equals(srRoleUserDTO.getRoleCode()) && roleUserDTOTmp
          .getUsername().equals(srRoleUserDTO.getUsername())) {
        srRoleUserDTO
            .setResultImport(I18n.getLanguage("srRoleUser.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return srRoleUserDTO;
  }

  private File exportFileTemplate(List<SRRoleUserInSideDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("srRoleUser.export.title");
    String title = I18n.getLanguage("srRoleUser.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = SR_ROLE_USER_RESULT_IMPORT;
      columnSheet = new ConfigHeaderExport("countryName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("unitIdStr", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("roleCode", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("username", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("isLeaderStr", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("actionName", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
      subTitle = I18n
          .getLanguage("srRoleUser.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      columnSheet = new ConfigHeaderExport("countryName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("roleCode", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("username", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("statusName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("isLeaderStr", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
      fileNameOut = SR_ROLE_USER_EXPORT;
      subTitle = I18n
          .getLanguage("srRoleUser.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.srRoleUser"
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
}
