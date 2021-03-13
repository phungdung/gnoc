package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.repository.SRRoleRepository;
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
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
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
public class SRRoleBusinessImpl implements SRRoleBusiness {

  @Autowired
  protected SRRoleRepository srRoleRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Autowired
  protected SRRoleBusiness srRoleBusiness;

  @Value("${application.temp.folder}")
  private String tempFolder;
  List<SRRoleDTO> srRoleDTOS = new ArrayList<>();
  Map<String, String> mapCountryName = new HashMap<>();
  private final static String SR_ROLE_RESULT_IMPORT = "SR_ROLE_RESULT_IMPORT";
  private final static String SR_ROLE_EXPORT = "SR_ROLE_EXPORT";

  @Override
  public List<SRRoleDTO> getListSRRoleByLocationCBB(SRRoleDTO srRoleDTO) {
    log.debug("Request to getListSRRoleByLocationCBB : {}", srRoleDTO);
    return srRoleRepository.getListSRRoleByLocationCBB(srRoleDTO);
  }

  @Override
  public Datatable getListSRRolePage(SRRoleDTO srRoleDTO) {
    log.debug("Request to getListSRRolePage : {}", srRoleDTO);
    return srRoleRepository.getListSRRolePage(srRoleDTO);
  }

  @Override
  public ResultInSideDto insert(SRRoleDTO srRoleDTO) {
    log.debug("Request to insert : {}", srRoleDTO);
    UserToken userToken = ticketProvider.getUserToken();
    srRoleDTO.setRoleCode(srRoleDTO.getRoleCode().toUpperCase());
    srRoleDTO.setCreatedUser(userToken.getUserName());
    srRoleDTO.setCreatedTime(new Date(System.currentTimeMillis()));
    return srRoleRepository.add(srRoleDTO);
  }

  @Override
  public ResultInSideDto update(SRRoleDTO srRoleDTO) {
    log.debug("Request to update : {}", srRoleDTO);
    SRRoleDTO detail = getDetail(srRoleDTO.getRoleId());
    UserToken userToken = ticketProvider.getUserToken();
    srRoleDTO.setUpdatedUser(userToken.getUserName());
    srRoleDTO.setUpdatedTime(new Date(System.currentTimeMillis()));
    srRoleDTO.setCreatedTime(detail.getCreatedTime());
    srRoleDTO.setCreatedUser(detail.getCreatedUser());
    return srRoleRepository.edit(srRoleDTO);
  }

  @Override
  public ResultInSideDto delete(Long roleId) {
    log.debug("Request to delete : {}", roleId);
    return srRoleRepository.delete(roleId);
  }

  @Override
  public SRRoleDTO getDetail(Long roleId) {
    log.debug("Request to getDetail : {}", roleId);
    return srRoleRepository.getDetail(roleId);
  }

  @Override
  public File exportData(SRRoleDTO srRoleDTO) throws Exception {
    List<SRRoleDTO> srRoleDTOList = (List<SRRoleDTO>) srRoleRepository
        .getListDataExport(srRoleDTO).getData();

    return exportFileTemplate(srRoleDTOList, "");
  }

  private File exportFileTemplate(List<SRRoleDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("srRole.export.title");
    String title = I18n.getLanguage("srRole.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = SR_ROLE_RESULT_IMPORT;
      columnSheet = new ConfigHeaderExport("countryName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("roleCode", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("roleName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("actionName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      subTitle = I18n
          .getLanguage("srRole.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      fileNameOut = SR_ROLE_EXPORT;
      subTitle = I18n
          .getLanguage("srRole.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("countryName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("roleCode", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("roleName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
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
        , "language.srRole"
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

  public void setMapCountryName() {
    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstCountryName) {
        mapCountryName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }


  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<SRRoleDTO> srRoleDTOList = new ArrayList<>();
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
            4,
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
            4,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }

        srRoleDTOS = new ArrayList<>();

        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          setMapCountryName();
          for (Object[] obj : dataImportList) {
            SRRoleDTO srRoleDTO = new SRRoleDTO();
            if (obj[1] != null) {
              srRoleDTO.setCountryName(obj[1].toString().trim());
              for (Map.Entry<String, String> item : mapCountryName.entrySet()) {
                if (srRoleDTO.getCountryName().equals(item.getValue())) {
                  srRoleDTO.setCountry(item.getKey());
                  break;
                } else {
                  srRoleDTO.setCountry(null);
                }
              }
            } else {
              srRoleDTO.setCountryName(null);
            }
            if (obj[2] != null) {
              srRoleDTO.setRoleCode(obj[2].toString().trim().toUpperCase());
            } else {
              srRoleDTO.setRoleCode(null);
            }

            if (obj[3] != null) {
              srRoleDTO.setRoleName(obj[3].toString().trim());
            } else {
              srRoleDTO.setRoleName(null);
            }
            if (obj[4] != null) {
              srRoleDTO.setActionName(obj[4].toString().trim());
              srRoleDTO.setAction(srRoleDTO.getActionName());
              if (!(I18n.getLanguage("srRoleUser.action.1")
                  .equals(srRoleDTO.getActionName()) || I18n.getLanguage("srRoleUser.action.0")
                  .equals(srRoleDTO.getActionName()))) {
                srRoleDTO.setAction(null);
              }
            } else {
              srRoleDTO.setActionName(null);
            }

            SRRoleDTO srRoleExportDTOTmp = validateImportInfo(
                srRoleDTO, srRoleDTOList);

            if (srRoleExportDTOTmp.getResultImport() == null) {
              srRoleExportDTOTmp
                  .setResultImport(I18n.getLanguage("srRole.result.import"));
              srRoleDTOList.add(srRoleExportDTOTmp);
              srRoleDTOS.add(srRoleDTO);
            } else {
              srRoleDTOList.add(srRoleExportDTOTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!srRoleDTOList.isEmpty()) {
              for (SRRoleDTO dto : srRoleDTOList) {
                UserToken userToken = ticketProvider.getUserToken();
                dto.setCreatedUser(userToken.getUserName());
                dto.setCreatedTime(new Date(System.currentTimeMillis()));
              }
              resultInSideDto = srRoleRepository
                  .insertOrUpdateList(srRoleDTOList);
            }
          } else {
            File fileExport = exportFileTemplate(srRoleDTOList, Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileTemplate(srRoleDTOList, Constants.RESULT_IMPORT);
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
        I18n.getLanguage("srRole.countryName"),
        I18n.getLanguage("srRole.roleCode"),
        I18n.getLanguage("srRole.roleName"),
        I18n.getLanguage("srRole.actionName")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("srRole.countryName"),
        I18n.getLanguage("srRole.roleCode"),
        I18n.getLanguage("srRole.roleName"),
        I18n.getLanguage("srRole.actionName")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int countryNameColumn = listHeader.indexOf(I18n.getLanguage("srRole.countryName"));
    int actionNameColumn = listHeader.indexOf(I18n.getLanguage("srRole.actionName"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("srRole.title"));
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
    Row headerRole = sheetParam1.createRow(0);
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

    Cell headerCellCountry = headerRole.createCell(0);
    Cell headerCellRoleCode = headerRole.createCell(1);
    Cell headerCellRoleName = headerRole.createCell(2);
    XSSFRichTextString country = new XSSFRichTextString(
        I18n.getLanguage("srRole.countryName"));
    XSSFRichTextString code = new XSSFRichTextString(
        I18n.getLanguage("srRole.roleCode"));
    XSSFRichTextString name = new XSSFRichTextString(
        I18n.getLanguage("srRole.roleName"));
    headerCellCountry.setCellValue(country);
    headerCellCountry.setCellStyle(style.get("header"));
    headerCellRoleCode.setCellValue(code);
    headerCellRoleCode.setCellStyle(style.get("header"));
    headerCellRoleName.setCellValue(name);
    headerCellRoleName.setCellStyle(style.get("header"));
    sheetParam1.setColumnWidth(0, 15000);
    sheetParam1.setColumnWidth(1, 20000);
    sheetParam1.setColumnWidth(2, 20000);

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

    row = 5;
    excelWriterUtils.createCell(sheetParam, 4, row++, I18n.getLanguage("srRole.action.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 4, row++, I18n.getLanguage("srRole.action.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name actionName = workbook.createName();
    actionName.setNameName("actionName");
    actionName.setRefersToFormula("param!$E$2:$E$" + row);

    XSSFDataValidationConstraint actionNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "actionName");
    CellRangeAddressList actionNameCreate = new CellRangeAddressList(5, 65000, actionNameColumn,
        actionNameColumn);
    XSSFDataValidation dataValidationActionName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            actionNameConstraint, actionNameCreate);
    dataValidationActionName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationActionName);

    row = 1;
    SRRoleDTO srRoleDTO = new SRRoleDTO();
    for (ItemDataCRInside dataCRInside : countryNameList) {
      srRoleDTO.setCountry(String.valueOf(dataCRInside.getValueStr()));
      List<SRRoleDTO> srRoleDTOList = (List<SRRoleDTO>) srRoleRepository
          .getListDataExport(srRoleDTO).getData();
      excelWriterUtils
          .createCell(sheetParam1, 0, row, dataCRInside.getDisplayStr(), style.get("cell"));
      for (SRRoleDTO srRoleDTO1 : srRoleDTOList) {
        excelWriterUtils
            .createCell(sheetParam1, 1, row, srRoleDTO1.getRoleCode(), style.get("cell"));
        excelWriterUtils
            .createCell(sheetParam1, 2, row++, srRoleDTO1.getRoleName(), style.get("cell"));
      }
    }
    sheetParam1.autoSizeColumn(0);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("srRole.title"));
    workbook.setSheetName(2, I18n.getLanguage("srRole.list"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_SR_ROLE" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
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
    if (count != 5) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srRole.countryName") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srRole.roleCode") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }

    if (!(I18n.getLanguage("srRole.roleName") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }

    if (!(I18n.getLanguage("srRole.actionName") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    return true;
  }

  private SRRoleDTO validateImportInfo(SRRoleDTO srRoleDTO,
      List<SRRoleDTO> list) {
    if (StringUtils.isStringNullOrEmpty(srRoleDTO.getCountryName())) {
      srRoleDTO.setResultImport(I18n.getLanguage("srRole.err.countryName"));
      return srRoleDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srRoleDTO.getRoleCode())) {
      srRoleDTO.setResultImport(I18n.getLanguage("srRole.err.roleCode"));
      return srRoleDTO;
    } else if (srRoleDTO.getRoleCode().length() > 200) {
      srRoleDTO.setResultImport(I18n.getLanguage("srRole.err.roleCode.length"));
      return srRoleDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srRoleDTO.getRoleName())) {
      srRoleDTO.setResultImport(I18n.getLanguage("srRole.err.roleName"));
      return srRoleDTO;
    } else if (srRoleDTO.getRoleName().length() > 200) {
      srRoleDTO.setResultImport(I18n.getLanguage("srRole.err.roleName.length"));
      return srRoleDTO;
    }
    if (StringUtils.isStringNullOrEmpty(srRoleDTO.getActionName())) {
      srRoleDTO.setResultImport(I18n.getLanguage("srRole.err.action"));
      return srRoleDTO;
    }
    if (srRoleDTO.getCountry() == null) {
      srRoleDTO.setResultImport(I18n.getLanguage("srRole.err.country.exist"));
      return srRoleDTO;
    }
    if (srRoleDTO.getAction() == null) {
      srRoleDTO.setResultImport(I18n.getLanguage("srRole.err.action.exist"));
      return srRoleDTO;
    }
    SRRoleDTO srRoleTmp = srRoleRepository
        .checkRoleExist(srRoleDTO.getCountry()
            , srRoleDTO.getRoleCode());
    if (srRoleTmp != null) {
      if (I18n.getLanguage("srRole.action.1").equals(srRoleDTO.getAction())) {
        srRoleDTO.setResultImport(I18n.getLanguage("srRole.err.duplicate"));
        return srRoleDTO;
      }
      if (I18n.getLanguage("srRole.action.0").equals(srRoleDTO.getAction())) {
        srRoleDTO.setRoleId(srRoleTmp.getRoleId());
      }

    } else {
      if (I18n.getLanguage("srRole.action.0").equals(srRoleDTO.getAction())) {
        srRoleDTO.setResultImport(I18n.getLanguage("srRole.err.noData"));
        return srRoleDTO;
      }
    }

    if (list != null && list.size() > 0) {
      srRoleDTO = validateDuplicate(list, srRoleDTO);
    }

    return srRoleDTO;
  }

  private SRRoleDTO validateDuplicate(List<SRRoleDTO> list,
      SRRoleDTO srRoleDTO) {
    for (int i = 0; i < list.size(); i++) {
      SRRoleDTO roleDTOTmp = list.get(i);
      if (I18n.getLanguage("srRole.result.import").equals(roleDTOTmp.getResultImport())
          && roleDTOTmp.getCountryName().equals(srRoleDTO.getCountryName())
          && roleDTOTmp.getRoleCode().equals(
          srRoleDTO.getRoleCode())) {
        srRoleDTO
            .setResultImport(I18n.getLanguage("srRole.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return srRoleDTO;
  }

  @Override
  public List<SRRoleDTO> getListSRRoleDTO(SRRoleDTO srRoleDTO) {
    return srRoleRepository.getListSRRoleDTO(srRoleDTO);
  }
}
