package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
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
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
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
public class LanguageExchangeBussinessImpl implements LanguageExchangeBussiness {

  @Autowired
  private LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  private CatItemBusiness catItemBusiness;


  @Value("${application.temp.folder}")
  private String tempFolder;
  List<LanguageExchangeDTO> languageExchangeDTOS = new ArrayList<>();
  Map<Long, String> mapAppliedSystem = new HashMap<>();
  Map<Long, String> mapAppliedBussiness = new HashMap<>();
  Map<String, String> mapLanguage = new HashMap<>();
  private final static String LANGUAGE_EXCHANGE_RESULT_IMPORT = "LANGUAGE_EXCHANGE_RESULT_IMPORT";

  private final static String EXPORT_LANGUAGE_EXCHANGE = "EXPORT_LANGUAGE_EXCHANGE";

  @Override
  public Datatable getListLanguageExchange(LanguageExchangeDTO dto) {
    log.debug("Request to getListLanguageExchange : {}", dto);
    return languageExchangeRepository.getListLanguageExchange(dto);
  }


  @Override
  public List<CatItemDTO> getListTableBySystem(String systemName) {
    return languageExchangeRepository.getListTableBySystem(systemName);
  }

  @Override
  public List<GnocLanguageDto> getListLanguage() {
    return languageExchangeRepository.getListLanguage();
  }

  @Override
  public LanguageExchangeDTO getDetailLanguageExchange(Long langExchangeId) {
    log.debug("Request to getDetailLanguageExchange : {}", langExchangeId);
    return languageExchangeRepository.getDetailLanguageExchange(langExchangeId);
  }

  @Override
  public ResultInSideDto updateLanguageExchange(LanguageExchangeDTO dto) {
    log.debug("Request to updateLanguageExchange : {}", dto);
    return languageExchangeRepository.updateLanguageExchange(dto);
  }

  @Override
  public ResultInSideDto insertLanguageExchange(LanguageExchangeDTO dto) {
    log.debug("Request to insertLanguageExchange : {}", dto);
    return languageExchangeRepository.insertLanguageExchange(dto);
  }

  @Override
  public ResultInSideDto deleteLanguageExchangeById(Long id) {
    log.debug("Request to deleteLanguageExchangeById : {}", id);
    return languageExchangeRepository.deleteLanguageExchangeById(id);
  }

  @Override
  public File exportLanguageExchange(LanguageExchangeDTO languageExchangeDTO) throws Exception {
    List<LanguageExchangeDTO> languageExchangeDTOList = new ArrayList<>();
    if (languageExchangeDTO.getAppliedSystem() != null || languageExchangeDTO.getAppliedBussiness() != null) {
      languageExchangeDTOList = languageExchangeRepository
          .getDataExport(languageExchangeDTO);
    }
    Map<String, String> mapSchema = new HashMap<>();
    Map<String, String> mapTable = new HashMap<>();
    Map<String, String> mapLanguage = new HashMap<>();
    List<CatItemDTO> lstSchema = catItemBusiness
        .getListItemByCategoryAndParent("MASTER_DATA_SCHEMA", null);
    for (CatItemDTO dto : lstSchema) {
      if (!mapSchema.containsKey(dto.getItemValue())) {
        mapSchema.put(dto.getItemValue(), dto.getItemName());
      }
    }
    List<CatItemDTO> lstTable = catItemBusiness
        .getListItemByCategoryAndParent("MASTER_DATA_TABLE", null);
    for (CatItemDTO dto : lstTable) {
      if (!mapTable.containsKey(dto.getItemValue())) {
        mapTable.put(dto.getItemValue(), dto.getItemName());
      }
    }
    List<GnocLanguageDto> lstLanguage = getListLanguage();
    for (GnocLanguageDto dto : lstLanguage) {
      if (!mapLanguage.containsKey(dto.getLanguageKey())) {
        mapLanguage.put(dto.getLanguageKey(), dto.getLanguageName());
      }
    }
    for (LanguageExchangeDTO dto : languageExchangeDTOList) {
      if (StringUtils.isNotNullOrEmpty(dto.getLeeLocale()) && mapLanguage
          .containsKey(dto.getLeeLocale())) {
        dto.setLeeLocaleName(mapLanguage.get(dto.getLeeLocale()));
      }
      if (dto.getAppliedSystem() != null && mapSchema
          .containsKey(String.valueOf(dto.getAppliedSystem()))) {
        dto.setAppliedSystemName(mapSchema.get(String.valueOf(dto.getAppliedSystem())));
      }
      if (dto.getAppliedBussiness() != null && mapTable
          .containsKey(String.valueOf(dto.getAppliedBussiness()))) {
        dto.setAppliedBusinessName(mapTable.get(String.valueOf(dto.getAppliedBussiness())));
      }
    }
    return exportFileTemplate(languageExchangeDTOList, "", "0");
  }

  @Override
  public File exportLanguageExchangeNotConfig(LanguageExchangeDTO languageExchangeDTO)
      throws Exception {
    List<LanguageExchangeDTO> languageExchangeDTOList = languageExchangeRepository
        .getListExchangeNotConfig(languageExchangeDTO);

    return exportFileTemplate(languageExchangeDTOList, "EXPORT_NOT_CONFIG", "0");
  }

  public void setMapAppliedSystem() {
    List<CatItemDTO> lstSystem = catItemBusiness
        .getListItemByCategory("MASTER_DATA_SCHEMA", "");
    if (lstSystem != null && !lstSystem.isEmpty()) {
      for (CatItemDTO itemSystem : lstSystem) {
        mapAppliedSystem.put(Long.valueOf(itemSystem.getItemValue()), itemSystem.getItemName());
      }
    }
  }

  public void setMapAppliedBussiness() {
    List<CatItemDTO> lstTable = catItemBusiness
        .getListItemByCategory("MASTER_DATA_TABLE", "");
    if (lstTable != null && !lstTable.isEmpty()) {
      for (CatItemDTO itemTable : lstTable) {
        mapAppliedBussiness.put(Long.valueOf(itemTable.getItemValue()), itemTable.getItemName());
      }
    }
  }

  public void setMapLanguage() {
    List<GnocLanguageDto> lstLanguage = languageExchangeRepository.getListLanguage();
    if (lstLanguage != null && !lstLanguage.isEmpty()) {
      for (GnocLanguageDto itemLanguage : lstLanguage) {
        mapLanguage.put(itemLanguage.getLanguageKey(), itemLanguage.getLanguageName());
      }
    }
  }


  private File exportFileTemplate(List<LanguageExchangeDTO> dtoList, String key,
      String isImportError)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("languageExchange.export.title");
    String title = I18n.getLanguage("languageExchange.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("leeLocaleName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("appliedSystemName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("appliedBusinessName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    if (isImportError == "0") {
      columnSheet = new ConfigHeaderExport("bussinessId", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
    } else {
      columnSheet = new ConfigHeaderExport("businessIdImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
    }
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("leeValue", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = LANGUAGE_EXCHANGE_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("languageExchange.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("action", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else if ("EXPORT_NOT_CONFIG".equals(key)) {
      headerExportList = new ArrayList<>();
      columnSheet = new ConfigHeaderExport("bussinessId", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("leeValue", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("appliedBusinessName", "LEFT", false, 0, 0,
          new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
      fileNameOut = EXPORT_LANGUAGE_EXCHANGE;
      subTitle = I18n
          .getLanguage("languageExchange.export.exportDate", DateTimeUtils.convertDateOffset());
    } else {
      fileNameOut = EXPORT_LANGUAGE_EXCHANGE;
      subTitle = I18n
          .getLanguage("languageExchange.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.languageExchange"
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
    XSSFSheet sheetOther = workbook.createSheet("Other");

    Map<String, List<String>> appliedBusiness = new HashMap<>();
    List<CatItemDTO> systemList = catItemBusiness.getListItemByCategory("MASTER_DATA_SCHEMA", "");
    List<String> lstSystemName = new ArrayList<>();
    List<Long> lstSystemId = new ArrayList<>();
    for (int i = 0; i < systemList.size(); i++) {
      lstSystemName.add(systemList.get(i).getItemName());
      lstSystemId.add(Long.valueOf(systemList.get(i).getItemValue()));
      List<String> lstBusinessName = new ArrayList<>();
      List<CatItemDTO> lstBusiness = languageExchangeRepository
          .getListTableBySystem(lstSystemName.get(i));
      if (lstBusiness != null && !lstBusiness.isEmpty()) {
        for (CatItemDTO dto : lstBusiness) {
          lstBusinessName.add(dto.getItemName());
        }
        appliedBusiness.put(lstSystemName.get(i), lstBusinessName);
      } else {
        lstBusinessName.add("null");
        appliedBusiness.put(lstSystemName.get(i), lstBusinessName);
      }
    }

    XSSFSheet sheetBusiness = workbook.createSheet("SheetBusiness");
    Row ro1;
    String col1;
    String ref1;
    int c1 = 0;
    for (String key : appliedBusiness.keySet()) {
      int r1 = 0;
      ro1 = sheetBusiness.getRow(r1);
      if (ro1 == null) {
        ro1 = sheetBusiness.createRow(r1);
      }
      r1++;
      ro1.createCell(c1).setCellValue(key);
      List<String> items = appliedBusiness.get(key);
      if (items != null) {
        if (items.size() > 0) {
          for (String item : items) {
            ro1 = sheetBusiness.getRow(r1);
            if (ro1 == null) {
              ro1 = sheetBusiness.createRow(r1);
            }
            r1++;
            ro1.createCell(c1).setCellValue(item);
          }
        }
      }

      col1 = CellReference.convertNumToColString(c1);
      Name type = workbook.createName();
      type.setNameName(key);
      ref1 = "SheetBusiness!$" + col1 + "$2:$" + col1 + "$" + r1;
      type.setRefersToFormula(ref1);
      c1++;
    }
    col1 = CellReference.convertNumToColString((c1 - 1));
    Name systemName = workbook.createName();
    systemName.setNameName("SystemName");
    ref1 = "SheetBusiness!$A$1:$" + col1 + "$1";
    systemName.setRefersToFormula(ref1);
    sheetBusiness.setSelected(false);
//    workbook.setSheetHidden(2, true);
    workbook.setActiveSheet(0);

    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("languageExchange.appliedSystemName"),
        I18n.getLanguage("languageExchange.appliedBusinessName"),
        I18n.getLanguage("languageExchange.bussinessId"),
        I18n.getLanguage("languageExchange.leeLocaleName"),
        I18n.getLanguage("languageExchange.leeValue"),
        I18n.getLanguage("languageExchange.action")

    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("languageExchange.appliedSystemName"),
        I18n.getLanguage("languageExchange.appliedBusinessName"),
        I18n.getLanguage("languageExchange.bussinessId"),
        I18n.getLanguage("languageExchange.leeLocaleName"),
        I18n.getLanguage("languageExchange.leeValue"),
        I18n.getLanguage("languageExchange.action")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int actionNameColumn = listHeader.indexOf(I18n.getLanguage("languageExchange.action"));
    int systemNameColumn = listHeader
        .indexOf(I18n.getLanguage("languageExchange.appliedSystemName"));
    int businessNameColumn = listHeader
        .indexOf(I18n.getLanguage("languageExchange.appliedBusinessName"));
    int leeLocaleColumn = listHeader.indexOf(I18n.getLanguage("languageExchange.leeLocaleName"));
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("languageExchange.title"));
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

    excelWriterUtils.createCell(sheetOther, 0, 0,
        I18n.getLanguage("languageExchange.appliedSystemName").toUpperCase(), style.get("header"));
    excelWriterUtils.createCell(sheetOther, 1, 0,

        I18n.getLanguage("languageExchange.leeLocaleName").toUpperCase(), style.get("header"));
    excelWriterUtils.createCell(sheetOther, 2, 0,
        I18n.getLanguage("languageExchange.action").toUpperCase(), style.get("header"));

    // Set dữ liệu vào column dropdown
    int row = 1;
    for (CatItemDTO dto : systemList) {
      excelWriterUtils.createCell(sheetOther, 0, row++, dto.getItemCode(), style.get("cell"));
    }

    XSSFDataValidationConstraint systemNameConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "SystemName");
    CellRangeAddressList systemNameCreate = new CellRangeAddressList(5, 66, systemNameColumn,
        systemNameColumn);
    XSSFDataValidation dataValidationSystemName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            systemNameConstraint, systemNameCreate);
    dataValidationSystemName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationSystemName);

    XSSFDataValidationConstraint businessNameConstraint;
    CellRangeAddressList cellRangeBusiness;
    XSSFDataValidation dataValidationBusiness;
    for (int i = 5; i <= 67; i++) {
      businessNameConstraint = new XSSFDataValidationConstraint(
          ValidationType.LIST, "INDIRECT($B$" + i + ")");
      cellRangeBusiness = new CellRangeAddressList(
          i - 1, i - 1, businessNameColumn, businessNameColumn);
      dataValidationBusiness = (XSSFDataValidation) dataValidationHelper.createValidation(
          businessNameConstraint, cellRangeBusiness);
      dataValidationBusiness.setShowErrorBox(true);
      sheetOne.addValidationData(dataValidationBusiness);
    }

    row = 1;
    List<GnocLanguageDto> languageList = languageExchangeRepository.getListLanguage();
    //Tạo dữ liệu bên sheet Other từ đó tham chiếu qua sheetOne
    for (GnocLanguageDto dto : languageList) {
      excelWriterUtils.createCell(sheetOther, 1, row++, dto.getLanguageName(), style.get("cell"));
    }
    sheetOne.autoSizeColumn(1);
    Name languageName = workbook.createName();
    // tham chiếu qua sheetOne thông qua other
    languageName.setNameName("leeLocaleName");
    languageName.setRefersToFormula("Other!$B$2:$B$" + row);

    XSSFDataValidationConstraint leeLocaleConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "leeLocaleName");
    CellRangeAddressList leeLocaleCreate = new CellRangeAddressList(5, 65000, leeLocaleColumn,
        leeLocaleColumn);
    XSSFDataValidation dataValidationLeeLocale = (XSSFDataValidation) dataValidationHelper
        .createValidation(leeLocaleConstraint, leeLocaleCreate);
    dataValidationLeeLocale.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationLeeLocale);

    row = 1;
    excelWriterUtils.createCell(sheetOther, 2, row++, I18n.getLanguage("languageExchange.action.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetOther, 2, row++, I18n.getLanguage("languageExchange.action.0")
        , style.get("cell"));
    sheetOne.autoSizeColumn(1);
    Name actionName = workbook.createName();
    actionName.setNameName("action");
    actionName.setRefersToFormula("Other!$C$2:$C$" + row);
    XSSFDataValidationConstraint actionNameConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "action");
    CellRangeAddressList actionNameCreate = new CellRangeAddressList(5, 65000, actionNameColumn,
        actionNameColumn);
    XSSFDataValidation dataValidationActionName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            actionNameConstraint, actionNameCreate);
    dataValidationActionName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationActionName);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("languageExchange.title"));
//    workbook.setSheetHidden(1, true);
    sheetOne.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_LANGUAGE_EXCHANGE" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }


  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<LanguageExchangeDTO> languageExchangeDTOList = new ArrayList<>();
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
            6,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }

        languageExchangeDTOS = new ArrayList<>();
        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          setMapAppliedSystem();
//          setMapAppliedBussiness();
          List<CatItemDTO> lstTable = catItemBusiness
              .getListItemByCategory("MASTER_DATA_TABLE", "");
          setMapLanguage();
          for (Object[] obj : dataImportList) {
            LanguageExchangeDTO languageExchangeDTO = new LanguageExchangeDTO();
            if (obj[1] != null) {
              languageExchangeDTO.setAppliedSystemName(obj[1].toString().trim());
              for (Map.Entry<Long, String> item : mapAppliedSystem.entrySet()) {
                if (languageExchangeDTO.getAppliedSystemName().equals(item.getValue())) {
                  languageExchangeDTO.setAppliedSystem(Long.valueOf(item.getKey()));
                  break;
                } else {
                  languageExchangeDTO.setAppliedSystem(null);
                }
              }
            } else {
              languageExchangeDTO.setAppliedSystemName(null);
            }
//            if (obj[2] != null) {
//              languageExchangeDTO.setAppliedBusinessName(obj[2].toString().trim());
//              for (Map.Entry<Long, String> item : mapAppliedBussiness.entrySet()) {
//                if (languageExchangeDTO.getAppliedBusinessName().equals(item.getValue())) {
//                  languageExchangeDTO.setAppliedBussiness(Long.valueOf(item.getKey()));
//                  break;
//                } else {
//                  languageExchangeDTO.setAppliedBussiness(null);
//                }
//              }
//            } else {
//              languageExchangeDTO.setAppliedBusinessName(null);
//            }
            if (obj[2] != null) {
              languageExchangeDTO.setAppliedBusinessName(obj[2].toString().trim());
              for (CatItemDTO catItemDTO : lstTable) {
                if (languageExchangeDTO.getAppliedBusinessName().equals(catItemDTO.getItemName())) {
                  languageExchangeDTO.setAppliedBussiness(Long.valueOf(catItemDTO.getItemValue()));
                  break;
                } else {
                  languageExchangeDTO.setAppliedBussiness(null);
                }
              }
            } else {
              languageExchangeDTO.setAppliedBusinessName(null);
            }
            if (obj[3] != null) {
              languageExchangeDTO.setBusinessIdImport(obj[3].toString().trim());
            } else {
              languageExchangeDTO.setBusinessIdImport(null);
            }
            if (obj[4] != null) {
              languageExchangeDTO.setLeeLocaleName(obj[4].toString().trim());
              for (Map.Entry<String, String> item : mapLanguage.entrySet()) {
                if (languageExchangeDTO.getLeeLocaleName().equals(item.getValue())) {
                  languageExchangeDTO.setLeeLocale(item.getKey());
                  break;
                } else {
                  languageExchangeDTO.setLeeLocale(null);
                }
              }
            } else {
              languageExchangeDTO.setAppliedBusinessName(null);
            }
            if (obj[5] != null) {
              languageExchangeDTO.setLeeValue(obj[5].toString().trim());
            } else {
              languageExchangeDTO.setLeeValue(null);
            }
            if (obj[6] != null) {
              languageExchangeDTO.setActionName(obj[6].toString().trim());
              languageExchangeDTO.setAction(languageExchangeDTO.getActionName());
              if (!(I18n.getLanguage("languageExchange.action.1")
                  .equals(languageExchangeDTO.getActionName()) || I18n
                  .getLanguage("languageExchange.action.0")
                  .equals(languageExchangeDTO.getActionName()))) {
                languageExchangeDTO.setAction(null);
              }
            } else {
              languageExchangeDTO.setActionName(null);
            }
            LanguageExchangeDTO languageExchangeDTOTmp = validateImportInfo(
                languageExchangeDTO, languageExchangeDTOList);

            if (languageExchangeDTOTmp.getResultImport() == null) {
              languageExchangeDTOTmp
                  .setResultImport(I18n.getLanguage("languageExchange.result.import"));
              languageExchangeDTOList.add(languageExchangeDTOTmp);
              languageExchangeDTOS.add(languageExchangeDTO);
            } else {
              languageExchangeDTOList.add(languageExchangeDTOTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!languageExchangeDTOList.isEmpty()) {

              resultInSideDto = languageExchangeRepository
                  .saveListLanguageExchangeImport(languageExchangeDTOList);
            }
          } else {
            File fileExport = exportFileTemplate(languageExchangeDTOList, Constants.RESULT_IMPORT,
                "1");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileTemplate(languageExchangeDTOList, Constants.RESULT_IMPORT,
              "1");
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
//    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
//      return false;
//    }
    if (!(I18n.getLanguage("languageExchange.appliedSystemName") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("languageExchange.appliedBusinessName") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("languageExchange.bussinessId") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("languageExchange.leeLocaleName") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("languageExchange.leeValue") + "*")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    return true;
  }

  private LanguageExchangeDTO validateImportInfo(LanguageExchangeDTO languageExchangeDTO,
      List<LanguageExchangeDTO> list) {
    if (StringUtils.isStringNullOrEmpty(languageExchangeDTO.getAppliedSystemName())) {
      languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.systemName"));
      return languageExchangeDTO;
    }

    if (StringUtils.isStringNullOrEmpty(languageExchangeDTO.getAppliedBusinessName())) {
      languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.businessName"));
      return languageExchangeDTO;
    }

    if (StringUtils.isStringNullOrEmpty(languageExchangeDTO.getLeeLocaleName())) {
      languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.localeName"));
      return languageExchangeDTO;
    }

    if (StringUtils.isStringNullOrEmpty(languageExchangeDTO.getBusinessIdImport())) {
      languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.businessId"));
      return languageExchangeDTO;
    }

    if (StringUtils.isStringNullOrEmpty(languageExchangeDTO.getLeeValue())) {
      languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.leeValue"));
      return languageExchangeDTO;
    }

    if (!(StringUtils.isStringNullOrEmpty(languageExchangeDTO.getLeeValue()))
        && languageExchangeDTO.getLeeValue().length() > 2000) {
      languageExchangeDTO
          .setResultImport(I18n.getLanguage("languageExchange.err.leeValue.tooLong"));
      return languageExchangeDTO;
    }
    if (!(StringUtils.isStringNullOrEmpty(languageExchangeDTO.getBusinessIdImport()))) {
//      languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.businessId.tooLong"));
//      return languageExchangeDTO;
      int count = 0;
      for (int i = 0; i < languageExchangeDTO.getBusinessIdImport().length(); i++) {
        if (Character.isDigit(languageExchangeDTO.getBusinessIdImport().charAt(i))) {
          count++;
        } else {
          languageExchangeDTO
              .setResultImport(I18n.getLanguage("languageExchange.err.businessId.notNumber"));
          return languageExchangeDTO;
        }
      }
      if (count > 10) {
        languageExchangeDTO
            .setResultImport(I18n.getLanguage("languageExchange.err.businessId.maxLength"));
        return languageExchangeDTO;
      }

    }
    if (StringUtils.isStringNullOrEmpty(languageExchangeDTO.getActionName())) {
      languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.action"));
      return languageExchangeDTO;
    }
    if (StringUtils.isStringNullOrEmpty(languageExchangeDTO.getAppliedSystem())) {
      languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.system.exist"));
      return languageExchangeDTO;
    }
    if (StringUtils.isStringNullOrEmpty(languageExchangeDTO.getLeeLocale())) {
      languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.language.exist"));
      return languageExchangeDTO;
    }
    if (StringUtils.isStringNullOrEmpty(languageExchangeDTO.getAppliedBussiness())) {
      languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.business.exist"));
      return languageExchangeDTO;
    }
    if (StringUtils.isStringNullOrEmpty(languageExchangeDTO.getAction())) {
      languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.action.exist"));
      return languageExchangeDTO;
    }
    LanguageExchangeDTO languageExchangeTmp = languageExchangeRepository
        .checkLanguageExchangeExist(languageExchangeDTO.getAppliedSystem()
            , languageExchangeDTO.getAppliedBussiness()
            , Long.valueOf(languageExchangeDTO.getBusinessIdImport())
            , languageExchangeDTO.getLeeLocale());
    if (languageExchangeTmp != null) {
      if (I18n.getLanguage("languageExchange.action.1").equals(languageExchangeDTO.getAction())) {
        languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.duplicate"));
        return languageExchangeDTO;
      }
      if (I18n.getLanguage("languageExchange.action.0").equals(languageExchangeDTO.getAction())) {
        languageExchangeDTO.setLeeId(languageExchangeTmp.getLeeId());
      }

    } else {
      if (I18n.getLanguage("languageExchange.action.0").equals(languageExchangeDTO.getAction())) {
        languageExchangeDTO.setResultImport(I18n.getLanguage("languageExchange.err.noData"));
        return languageExchangeDTO;
      }
    }

    if (list != null && list.size() > 0 && languageExchangeDTO.getResultImport() == null) {
      languageExchangeDTO = validateDuplicate(list, languageExchangeDTO);
    }

    return languageExchangeDTO;
  }

  private LanguageExchangeDTO validateDuplicate(List<LanguageExchangeDTO> list,
      LanguageExchangeDTO languageExchangeDTO) {
    for (int i = 0; i < list.size(); i++) {
      LanguageExchangeDTO languageExchangeTmp = list.get(i);
      if (I18n.getLanguage("languageExchange.result.import")
          .equals(languageExchangeTmp.getResultImport()) && languageExchangeTmp.getAppliedSystem()
          .equals(languageExchangeDTO.getAppliedSystem())
          && languageExchangeTmp.getAppliedBussiness().equals(
          languageExchangeDTO.getAppliedBussiness()) && languageExchangeTmp.getBusinessIdImport()
          .equals(languageExchangeDTO.getBusinessIdImport()) && languageExchangeTmp
          .getLeeLocale().equals(languageExchangeDTO.getLeeLocale())) {
        languageExchangeDTO
            .setResultImport(I18n.getLanguage("languageExchange.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return languageExchangeDTO;
  }


}
