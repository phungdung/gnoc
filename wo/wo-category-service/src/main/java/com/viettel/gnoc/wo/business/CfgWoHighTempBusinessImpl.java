package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ObjectSearchDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
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
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTOSearch;
import com.viettel.gnoc.wo.dto.CfgWoHighTempDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.repository.CfgWoHighTempRepository;
import java.io.File;
import java.io.IOException;
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
public class CfgWoHighTempBusinessImpl implements CfgWoHighTempBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  CfgWoHighTempRepository cfgWoHighTempRepository;

  @Autowired
  CommonRepository commonRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WoCdGroupBusiness woCdGroupBusiness;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  TtServiceProxy ttServiceProxy;

  private int maxRecord = 3000;
  private Map<String, DataItemDTO> mapPriority = new HashMap<>();
  private Map<String, DataItemDTO> mapPriorityId = new HashMap<>();
  private Map<String, CatItemDTO> mapAction = new HashMap<>();
  private Map<Long, CatItemDTO> mapActionId = new HashMap<>();
  private Map<String, CatItemDTO> mapActionName = new HashMap<>();
  private Map<String, CatItemDTO> mapActionNameId = new HashMap<>();
  private Map<String, CatReasonInSideDTOSearch> mapReasonLv1Id = new HashMap<>();
  private Map<String, CatReasonInSideDTOSearch> mapReasonLv2Id = new HashMap<>();
  private Map<String, CatItemDTO> mapReportFailureType = new HashMap<>();
  private Map<String, CatItemDTO> mapReportFailureTypeId = new HashMap<>();
  private Map<String, CatItemDTO> mapCdType = new HashMap<>();
  private Map<String, CatItemDTO> mapCdTypeId = new HashMap<>();

  @Override
  public Datatable onSearch(CfgWoHighTempDTO dto) {
    Datatable datatable = cfgWoHighTempRepository.onSearch(dto);
    List<CfgWoHighTempDTO> list = (List<CfgWoHighTempDTO>) datatable.getData();
    setDetailSearch(list);
    datatable.setData(list);
    return datatable;
  }

  private void setDetailSearch(List<CfgWoHighTempDTO> list) {
    if (list != null && !list.isEmpty()) {
      setMapPriority();
      setMapReason();
      setMapAction();
      setMapActionName();
      setMapReportFailureType();
      setMapCdType();
      for (CfgWoHighTempDTO highTempDTO : list) {
        highTempDTO.setStatusName(I18n.getLanguage("cfgWoHighTemp.status.inactive"));
        if (highTempDTO.getStatus() != null && highTempDTO.getStatus() == 1L) {
          highTempDTO.setStatusName(I18n.getLanguage("cfgWoHighTemp.status.active"));
        }
        highTempDTO.setIsAssignFtName(I18n.getLanguage("cfgWoHighTemp.status.no"));
        if (highTempDTO.getIsAssignFt() != null && highTempDTO.getIsAssignFt() == 1L) {
          highTempDTO.setIsAssignFtName(I18n.getLanguage("cfgWoHighTemp.status.yes"));
        }
        if (mapPriorityId.containsKey(String.valueOf(highTempDTO.getPriorityId()))) {
          highTempDTO.setPriorityName(
              mapPriorityId.get(String.valueOf(highTempDTO.getPriorityId())).getItemName());
        }
        if (mapReasonLv1Id.containsKey(String.valueOf(highTempDTO.getReasonLv1Id()))) {
          highTempDTO
              .setReasonLv1Name(mapReasonLv1Id.get(String.valueOf(highTempDTO.getReasonLv1Id())).getReasonName());
        }
        if (mapReasonLv2Id.containsKey(String.valueOf(highTempDTO.getReasonLv2Id()))) {
          highTempDTO
              .setReasonLv2Name(mapReasonLv2Id.get(String.valueOf(highTempDTO.getReasonLv2Id())).getReasonName());
        }
        if (mapActionId.containsKey(highTempDTO.getActionId())) {
          highTempDTO.setActionIdStr(mapActionId.get(highTempDTO.getActionId()).getItemName());
        }
        if (mapActionNameId.containsKey(highTempDTO.getActionNameId())) {
          highTempDTO
              .setActionNameIdStr(mapActionNameId.get(highTempDTO.getActionNameId()).getItemName());
        }
        if (mapReportFailureTypeId.containsKey(highTempDTO.getReportFailureType())) {
          highTempDTO.setReportFailureTypeName(
              mapReportFailureTypeId.get(highTempDTO.getReportFailureType()).getItemName());
        }
        if (mapCdTypeId.containsKey(highTempDTO.getCdType())) {
          highTempDTO.setCdTypeName(
              mapCdTypeId.get(highTempDTO.getCdType()).getItemName());
        }
      }
    }
  }

  @Override
  public ResultInSideDto insertOrUpdate(CfgWoHighTempDTO dto) {
    return cfgWoHighTempRepository.insertOrUpdate(dto);
  }

  @Override
  public CfgWoHighTempDTO findById(Long id) {
    return cfgWoHighTempRepository.findById(id);
  }

  @Override
  public ResultInSideDto delete(Long id) {
    return cfgWoHighTempRepository.delete(id);
  }

  @Override
  public File exportData(CfgWoHighTempDTO dto) throws Exception {
    String[] header = new String[]{"reasonLv1Name", "reasonLv2Name", "actionIdStr",
        "actionNameIdStr", "priorityName", "cdTypeName", "processTime", "isAssignFtName",
        "reportFailureTypeName",
        "statusName"};
    List<CfgWoHighTempDTO> lstData = onSearchExport(dto);
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header);
    return exportFileEx(lstData, lstHeaderSheet, "");
  }

  private List<CfgWoHighTempDTO> onSearchExport(CfgWoHighTempDTO dto) {
    List<CfgWoHighTempDTO> lstData = cfgWoHighTempRepository.onSearchExport(dto);
    setDetailSearch(lstData);
    return lstData;
  }

  private File exportFileEx(List<CfgWoHighTempDTO> lstData,
      List<ConfigHeaderExport> lstHeaderSheet, String code) throws Exception {
    String title = I18n.getLanguage("cfgWoHighTemp.export.grid.title");
    String sheetName = title;
    String fileNameOut;
    ConfigFileExport configfileExport = null;
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet = null;
    ConfigHeaderExport columnSheet1 = null;
    if (Constants.RESULT_IMPORT.equals(code)) {
      fileNameOut = "CFG_WO_HIGH_TEMP_IMPORT";
      columnSheet1 = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet1);
    } else {
      fileNameOut = "CFG_WO_HIGH_TEMP_EXPORT";
    }
    configfileExport = new ConfigFileExport(
        lstData
        , sheetName
        , title
        , null
        , 6
        , 3
        , lstHeaderSheet.size()
        , true
        , "language.cfgWoHighTemp.export.grid"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    cellSheet = new CellConfigExport(6, 0, 0, 0,
        I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    configfileExport.setLangKey(I18n.getLocale());
    fileExports.add(configfileExport);

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

  private List<ConfigHeaderExport> renderHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  @Override
  public File getTemplate() throws IOException {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetParam = workBook.createSheet("param");
    XSSFSheet sheetParam2 = workBook
        .createSheet(I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv1Name") + ", " + I18n
            .getLanguage("cfgWoHighTemp.export.grid.reasonLv2Name"));
    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetMain);
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv1Name"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv2Name"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.actionIdStr"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.actionNameIdStr"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.priorityName"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.cdTypeName"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.processTimeStr"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.isAssignFtName"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.reportFailureTypeName"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.statusName")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv1Name"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv2Name"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.actionIdStr"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.actionNameIdStr"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.priorityName"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.cdTypeName"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.processTimeStr"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.isAssignFtName"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.reportFailureTypeName"),
        I18n.getLanguage("cfgWoHighTemp.export.grid.statusName")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("common.STT"));
    int reasonLv1Column = listHeader
        .indexOf(I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv1Name"));
    int reasonLv2Column = listHeader
        .indexOf(I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv2Name"));
    int actionIdColumn = listHeader
        .indexOf(I18n.getLanguage("cfgWoHighTemp.export.grid.actionIdStr"));
    int actionNameIdColumn = listHeader
        .indexOf(I18n.getLanguage("cfgWoHighTemp.export.grid.actionNameIdStr"));
    int priorityColumn = listHeader
        .indexOf(I18n.getLanguage("cfgWoHighTemp.export.grid.priorityName"));
    int cdTypeColumn = listHeader
        .indexOf(I18n.getLanguage("cfgWoHighTemp.export.grid.cdTypeName"));
    int processTimeColumn = listHeader
        .indexOf(I18n.getLanguage("cfgWoHighTemp.export.grid.processTimeStr"));
    int isAssignFtColumn = listHeader
        .indexOf(I18n.getLanguage("cfgWoHighTemp.export.grid.isAssignFtName"));
    int reportFailureTypeColumn = listHeader
        .indexOf(I18n.getLanguage("cfgWoHighTemp.export.grid.reportFailureTypeName"));
    int statusColumn = listHeader.indexOf(I18n.getLanguage("cfgWoHighTemp.export.grid.statusName"));

    String firstLeftHeaderTitle = I18n.getLanguage("common.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("common.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("common.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("common.export.secondRightHeader");

    CellStyle cellStyleTopHeader = CommonExport.setCellStyleTopHeader(workBook);
    CellStyle cellStyleTopRightHeader = CommonExport.setCellStyleTopRightHeader(workBook);
    CellStyle cellStyleTitle = CommonExport.setCellStyleTitle(workBook);
    CellStyle cellStyleHeader = CommonExport.setCellStyleHeader(workBook);

    //Tao quoc hieu
    Row headerFirstTitle = sheetMain.createRow(0);
    Row headerSecondTitle = sheetMain.createRow(1);
    int sizeheaderTitle = 7;
    Cell firstLeftHeader = headerFirstTitle.createCell(1);
    firstLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell secondLeftHeader = headerSecondTitle.createCell(1);
    secondLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell firstRightHeader = headerFirstTitle.createCell(sizeheaderTitle - 2);
    firstRightHeader.setCellStyle(cellStyleTopRightHeader);
    Cell secondRightHeader = headerSecondTitle.createCell(sizeheaderTitle - 2);
    secondRightHeader.setCellStyle(cellStyleTopRightHeader);
    firstLeftHeader.setCellValue(firstLeftHeaderTitle);
    secondLeftHeader.setCellValue(secondLeftHeaderTitle);
    firstRightHeader.setCellValue(firstRightHeaderTitle);
    secondRightHeader.setCellValue(secondRightHeaderTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, sizeheaderTitle - 2,
        sizeheaderTitle + 1));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, sizeheaderTitle - 2,
        sizeheaderTitle + 1));

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(3);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("cfgWoHighTemp.import.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetMain.createRow(5);
    Row headerReason = sheetParam2.createRow(0);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("(*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    Cell headerCellReasonLv1Id = headerReason.createCell(0);
    Cell headerCellReasonLv1Name = headerReason.createCell(1);
    Cell headerCellReasonLv2Id = headerReason.createCell(2);
    Cell headerCellReasonLv2Name = headerReason.createCell(3);
    XSSFRichTextString reasonLv1Id = new XSSFRichTextString(
        I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv1Id"));
    XSSFRichTextString reasonLv1Name = new XSSFRichTextString(
        I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv1Name"));
    XSSFRichTextString reasonLv2Id = new XSSFRichTextString(
        I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv2Id"));
    XSSFRichTextString reasonLv2Name = new XSSFRichTextString(
        I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv2Name"));
    headerCellReasonLv1Id.setCellValue(reasonLv1Id);
    headerCellReasonLv1Id.setCellStyle(styles.get("header"));
    headerCellReasonLv1Name.setCellValue(reasonLv1Name);
    headerCellReasonLv1Name.setCellStyle(styles.get("header"));
    headerCellReasonLv2Id.setCellValue(reasonLv2Id);
    headerCellReasonLv2Id.setCellStyle(styles.get("header"));
    headerCellReasonLv2Name.setCellValue(reasonLv2Name);
    headerCellReasonLv2Name.setCellStyle(styles.get("header"));
    sheetParam2.setColumnWidth(0, 4000);
    sheetParam2.setColumnWidth(1, 10000);
    sheetParam2.setColumnWidth(2, 4000);
    sheetParam2.setColumnWidth(3, 10000);

    // tạo sheet ly do cap 1, 2
    setMapReason();
    int row = 1;
    List<CatReasonInSideDTOSearch> listReasonLv1 = new ArrayList<>(mapReasonLv1Id.values());
    List<CatReasonInSideDTOSearch> listReasonLv2 = new ArrayList<>(mapReasonLv2Id.values());
    for (CatReasonInSideDTOSearch reasonLv1 : listReasonLv1) {
      ewu.createCell(sheetParam2, 0, row, String.valueOf(reasonLv1.getId()), styles.get("cell"));
      ewu.createCell(sheetParam2, 1, row++, reasonLv1.getReasonName(), styles.get("cell"));
      for (CatReasonInSideDTOSearch reasonLv2 : listReasonLv2) {
        if (reasonLv1.getId().equals(reasonLv2.getParentId())) {
          ewu.createCell(sheetParam2, 2, row, String.valueOf(reasonLv2.getId()),
              styles.get("cell"));
          ewu.createCell(sheetParam2, 3, row++, reasonLv2.getReasonName(), styles.get("cell"));
        }
      }
    }

    // set cbb Giao viec den CD
    setMapCdType();
    row = 5;

    List<CatItemDTO> lstReason = new ArrayList<>(mapCdType.values());
    for (CatItemDTO dto : lstReason) {
      ewu.createCell(sheetParam, 6, row++, dto.getItemName(), styles.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name cdTypeName = workBook.createName();
    cdTypeName.setNameName("cdTypeName");
    cdTypeName.setRefersToFormula("param!$G$2:$G$" + row);
    XSSFDataValidationConstraint cdTypeNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "cdTypeName");
    CellRangeAddressList cdTypeNameCreate = new CellRangeAddressList(5, 65000, cdTypeColumn,
        cdTypeColumn);
    XSSFDataValidation dataValidationCdTypeName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            cdTypeNameConstraint, cdTypeNameCreate);
    dataValidationCdTypeName.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationCdTypeName);

    // set cbb hanh dong
    setMapAction();
    row = 5;

    List<CatItemDTO> lstAction = new ArrayList<>(mapAction.values());
    for (CatItemDTO dto : lstAction) {
      ewu.createCell(sheetParam, 3, row++, dto.getItemName(), styles.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name actionId = workBook.createName();
    actionId.setNameName("actionId");
    actionId.setRefersToFormula("param!$D$2:$D$" + row);
    XSSFDataValidationConstraint actionIdConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "actionId");
    CellRangeAddressList actionIdCreate = new CellRangeAddressList(5, 65000, actionIdColumn,
        actionIdColumn);
    XSSFDataValidation dataValidationActionId = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            actionIdConstraint, actionIdCreate);
    dataValidationActionId.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationActionId);

    // set cbb hanh dong thuc hien
    setMapActionName();
    row = 5;

    List<CatItemDTO> lstActionName = new ArrayList<>(mapActionName.values());
    for (CatItemDTO dto : lstActionName) {
      ewu.createCell(sheetParam, 4, row++, dto.getItemName(), styles.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name actionName = workBook.createName();
    actionName.setNameName("actionName");
    actionName.setRefersToFormula("param!$E$2:$E$" + row);
    XSSFDataValidationConstraint actionNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "actionName");
    CellRangeAddressList actionNameCreate = new CellRangeAddressList(5, 65000, actionNameIdColumn,
        actionNameIdColumn);
    XSSFDataValidation dataValidationActionName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            actionNameConstraint, actionNameCreate);
    dataValidationActionName.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationActionName);

    // set cbb priority
    setMapPriority();
    row = 5;

    List<DataItemDTO> lstPriority = new ArrayList<>(mapPriority.values());
    for (DataItemDTO dto : lstPriority) {
      ewu.createCell(sheetParam, 5, row++, dto.getItemName(), styles.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name priorityName = workBook.createName();
    priorityName.setNameName("priorityName");
    priorityName.setRefersToFormula("param!$F$2:$F$" + row);
    XSSFDataValidationConstraint priorityNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "priorityName");
    CellRangeAddressList priorityNameCreate = new CellRangeAddressList(5, 65000, priorityColumn,
        priorityColumn);
    XSSFDataValidation dataValidationPriorityName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            priorityNameConstraint, priorityNameCreate);
    dataValidationPriorityName.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationPriorityName);

    // set cbb isAssignFt
    row = 5;

    ewu.createCell(sheetParam, 8, row++, I18n.getLanguage("cfgWoHighTemp.status.yes")
        , styles.get("cell"));
    ewu.createCell(sheetParam, 8, row++, I18n.getLanguage("cfgWoHighTemp.status.no")
        , styles.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name isAssignFtName = workBook.createName();
    isAssignFtName.setNameName("isAssignFtName");
    isAssignFtName.setRefersToFormula("param!$I$2:$I$" + row);
    XSSFDataValidationConstraint isAssignFtNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isAssignFtName");
    CellRangeAddressList isAssignFtNameCreate = new CellRangeAddressList(5, 65000, isAssignFtColumn,
        isAssignFtColumn);
    XSSFDataValidation dataValidationIsAssignFtName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isAssignFtNameConstraint, isAssignFtNameCreate);
    dataValidationIsAssignFtName.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationIsAssignFtName);

    // set cbb loai bao hong
    setMapReportFailureType();
    row = 5;

    List<CatItemDTO> lstReportType = new ArrayList<>(mapReportFailureType.values());
    for (CatItemDTO dto : lstReportType) {
      ewu.createCell(sheetParam, 9, row++, dto.getItemName(), styles.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name reportFailureTypeName = workBook.createName();
    reportFailureTypeName.setNameName("reportFailureTypeName");
    reportFailureTypeName.setRefersToFormula("param!$J$2:$J$" + row);
    XSSFDataValidationConstraint reportTypeNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "reportFailureTypeName");
    CellRangeAddressList reportTypeNameCreate = new CellRangeAddressList(5, 65000,
        reportFailureTypeColumn,
        reportFailureTypeColumn);
    XSSFDataValidation dataValidationReportTypeName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            reportTypeNameConstraint, reportTypeNameCreate);
    dataValidationReportTypeName.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationReportTypeName);

    // set cbb status
    row = 5;

    ewu.createCell(sheetParam, 10, row++, I18n.getLanguage("cfgWoHighTemp.status.active")
        , styles.get("cell"));
    ewu.createCell(sheetParam, 10, row++, I18n.getLanguage("cfgWoHighTemp.status.inactive")
        , styles.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name statusName = workBook.createName();
    statusName.setNameName("statusName");
    statusName.setRefersToFormula("param!$K$2:$K$" + row);
    XSSFDataValidationConstraint statusNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "statusName");
    CellRangeAddressList statusNameCreate = new CellRangeAddressList(5, 65000, statusColumn,
        statusColumn);
    XSSFDataValidation dataValidationStatusName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            statusNameConstraint, statusNameCreate);
    dataValidationStatusName.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationStatusName);

    // set độ rông các cột
    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(reasonLv1Column, 6000);
    sheetMain.setColumnWidth(reasonLv2Column, 6000);
    sheetMain.setColumnWidth(actionIdColumn, 6000);
    sheetMain.setColumnWidth(actionNameIdColumn, 6000);
    sheetMain.setColumnWidth(priorityColumn, 6000);
    sheetMain.setColumnWidth(cdTypeColumn, 6000);
    sheetMain.setColumnWidth(processTimeColumn, 6000);
    sheetMain.setColumnWidth(isAssignFtColumn, 6000);
    sheetMain.setColumnWidth(reportFailureTypeColumn, 6000);
    sheetMain.setColumnWidth(statusColumn, 6000);

    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("cfgWoHighTemp.import.title"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_CFG_WO_HIGH_TEMP" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<CfgWoHighTempDTO> listDto;
    List<CfgWoHighTempDTO> listImportDto;
    Map<String, String> mapImportDTO;

    String[] header = new String[]{"reasonLv1Name", "reasonLv2Name", "actionIdStr",
        "actionNameIdStr", "priorityName", "cdTypeName", "processTimeStr", "isAssignFtName",
        "reportFailureTypeName",
        "statusName"};
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header);
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

        File fileImp = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 10, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 6,
            0, 10, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey("ERROR_NO_DOWNLOAD");
          resultInSideDto.setMessage(I18n.getLanguage("cfgWoHighTemp.maxrow.import"));
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        listImportDto = new ArrayList<>();
        mapImportDTO = new HashMap<>();

        if (!lstData.isEmpty()) {
          //get map data
          setMapPriority();
          setMapReason();
          setMapAction();
          setMapActionName();
          setMapReportFailureType();
          setMapCdType();
          int index = 0;
          for (Object[] obj : lstData) {
            CfgWoHighTempDTO importDTO = new CfgWoHighTempDTO();
            if (obj[1] != null) {
              importDTO.setReasonLv1Name(obj[1].toString().trim());
            }
            if (obj[2] != null) {
              importDTO.setReasonLv2Name(obj[2].toString().trim());
            }
            if (obj[3] != null) {
              importDTO.setActionIdStr(obj[3].toString().trim());
            }
            if (obj[4] != null) {
              importDTO.setActionNameIdStr(obj[4].toString().trim());
            }
            if (obj[5] != null) {
              importDTO.setPriorityName(obj[5].toString().trim());
            }
            if (obj[6] != null) {
              importDTO.setCdTypeName(obj[6].toString().trim());
            }
            if (obj[7] != null) {
              importDTO.setProcessTimeStr(obj[7].toString().trim());
            }
            if (obj[8] != null) {
              importDTO.setIsAssignFtName(obj[8].toString().trim());
              if (I18n.getLanguage("cfgWoHighTemp.status.yes")
                  .equals(importDTO.getIsAssignFtName())) {
                importDTO.setIsAssignFt(1L);
              } else if (I18n.getLanguage("cfgWoHighTemp.status.no")
                  .equals(importDTO.getIsAssignFtName())) {
                importDTO.setIsAssignFt(0L);
              }
            }
            if (obj[9] != null) {
              importDTO.setReportFailureTypeName(obj[9].toString().trim());
            }
            if (obj[10] != null) {
              importDTO.setStatusName(obj[10].toString().trim());
              if (I18n.getLanguage("cfgWoHighTemp.status.active")
                  .equals(importDTO.getStatusName())) {
                importDTO.setStatus(1L);
              } else if (I18n.getLanguage("cfgWoHighTemp.status.inactive")
                  .equals(importDTO.getStatusName())) {
                importDTO.setStatus(0L);
              }
            }

            CfgWoHighTempDTO tempImportDTO = validateImportInfo(importDTO, mapImportDTO);
            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO
                  .setResultImport(I18n.getLanguage("common.import.validRecord"));
              listImportDto.add(tempImportDTO);
              listDto.add(tempImportDTO);
            } else {
              listImportDto.add(tempImportDTO);
              index++;
            }
            String reasonLv1Id = String.valueOf(importDTO.getReasonLv1Id());
            String reasonLv2Id = String.valueOf(importDTO.getReasonLv2Id());
            String actionId = String.valueOf(importDTO.getActionId());

            String key = reasonLv1Id + "_" + reasonLv2Id + "_" + actionId;
            mapImportDTO.put(key, key);
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              boolean check = false;
              for (CfgWoHighTempDTO dto : listDto) {
                resultInSideDto = insertOrUpdate(dto);
                if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                  check = true;
                  break;
                }
              }
              if (!check == true) {
                File fileExport = exportFileEx(listImportDto, lstHeaderSheet, "RESULT_IMPORT");
                resultInSideDto.setFile(fileExport);
                resultInSideDto.setFilePath(fileExport.getPath());
                resultInSideDto.setKey(RESULT.SUCCESS);
              }
            }
          } else {
            File fileExport = exportFileEx(listImportDto, lstHeaderSheet, "RESULT_IMPORT");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
            resultInSideDto.setFilePath(fileExport.getPath());
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileEx(listImportDto, lstHeaderSheet, "RESULT_IMPORT");
          resultInSideDto.setFile(fileExport);
          resultInSideDto.setFilePath(fileExport.getPath());
          return resultInSideDto;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(ex.getMessage());
    }
    return resultInSideDto;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 11) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("common.STT"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv1Name") + "(*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv2Name") + "(*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgWoHighTemp.export.grid.actionIdStr") + "(*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgWoHighTemp.export.grid.actionNameIdStr") + "(*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgWoHighTemp.export.grid.priorityName") + "(*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgWoHighTemp.export.grid.cdTypeName") + "(*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgWoHighTemp.export.grid.processTimeStr") + "(*)")
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgWoHighTemp.export.grid.isAssignFtName") + "(*)")
        .equalsIgnoreCase(obj[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgWoHighTemp.export.grid.reportFailureTypeName") + "(*)")
        .equalsIgnoreCase(obj[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgWoHighTemp.export.grid.statusName") + "(*)")
        .equalsIgnoreCase(obj[10].toString().trim())) {
      return false;
    }
    return true;
  }

  private CfgWoHighTempDTO validateImportInfo(CfgWoHighTempDTO importDTO,
      Map<String, String> mapImportDTO) {
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(importDTO.getReasonLv1Name())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv1Name") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      if (!mapReasonLv1Id.containsKey(importDTO.getReasonLv1Name())) {
        resultImport = resultImport.concat(
            I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv1Name") + " " + I18n
                .getLanguage("common.invalid") + ";");
      } else {
        Long reasonLv1Id = Long.valueOf(mapReasonLv1Id.get(importDTO.getReasonLv1Name()).getId());
        importDTO.setReasonLv1Id(reasonLv1Id);
      }
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getReasonLv2Name())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv2Name") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      if (!mapReasonLv2Id.containsKey(importDTO.getReasonLv2Name())) {
        resultImport = resultImport.concat(
            I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv2Name") + " " + I18n
                .getLanguage("common.invalid") + ";");
      } else if (importDTO.getReasonLv1Id() != null && !importDTO.getReasonLv1Id()
          .equals(mapReasonLv2Id.get(importDTO.getReasonLv2Name()).getParentId())) {
        resultImport = resultImport.concat(
            I18n.getLanguage("cfgWoHighTemp.export.grid.reasonLv2Name") + " " + I18n
                .getLanguage("common.invalid") + ";");
      } else {
        Long reasonLv2Id = Long.valueOf(mapReasonLv2Id.get(importDTO.getReasonLv2Name()).getId());
        importDTO.setReasonLv2Id(reasonLv2Id);
      }
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getActionIdStr())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.actionIdStr") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      if (!mapAction.containsKey(importDTO.getActionIdStr())) {
        resultImport = resultImport.concat(
            I18n.getLanguage("cfgWoHighTemp.export.grid.actionIdStr") + " " + I18n
                .getLanguage("common.invalid") + ";");
      } else {
        Long actionId = Long.valueOf(mapAction.get(importDTO.getActionIdStr()).getItemId());
        importDTO.setActionId(actionId);
      }
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getActionNameIdStr())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.actionNameIdStr") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      if (!mapActionName.containsKey(importDTO.getActionNameIdStr())) {
        resultImport = resultImport.concat(
            I18n.getLanguage("cfgWoHighTemp.export.grid.actionNameIdStr") + " " + I18n
                .getLanguage("common.invalid") + ";");
      } else {
        importDTO.setActionNameId(mapActionName.get(importDTO.getActionNameIdStr()).getItemValue());
      }
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getPriorityName())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.priorityName") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      if (!mapPriority.containsKey(importDTO.getPriorityName())) {
        resultImport = resultImport.concat(
            I18n.getLanguage("cfgWoHighTemp.export.grid.priorityName") + " " + I18n
                .getLanguage("common.invalid") + ";");
      } else {
        Long priorityId = Long.valueOf(mapPriority.get(importDTO.getPriorityName()).getItemId());
        importDTO.setPriorityId(priorityId);
      }
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getCdTypeName())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.cdTypeName") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      if (!mapCdType.containsKey(importDTO.getCdTypeName())) {
        resultImport = resultImport.concat(
            I18n.getLanguage("cfgWoHighTemp.export.grid.cdTypeName") + " " + I18n
                .getLanguage("common.invalid") + ";");
      } else {
        importDTO.setCdType(mapCdType.get(importDTO.getCdTypeName()).getItemValue());
      }
    }

    if (StringUtils.isStringNullOrEmpty(importDTO.getProcessTimeStr())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.processTimeStr") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      try {
        Long processTime = Long.valueOf(importDTO.getProcessTimeStr());
        if (importDTO.getProcessTimeStr().length() > 10) {
          resultImport = resultImport.concat(
              I18n.getValidation("cfgWoHighTemp.processTime.max.length.invalid")
                  + ";");
        } else {
          String regex = "[+]?[0-9]+";
          if (!importDTO.getProcessTimeStr().matches(regex)) {
            resultImport = resultImport.concat(
                I18n.getValidation("cfgWoHighTemp.processTime.invalid") + ";");
          } else {
            importDTO.setProcessTime(processTime);
          }
        }
      } catch (Exception ex) {
        resultImport = resultImport
            .concat(I18n.getValidation("cfgWoHighTemp.processTime.invalid") + ";");
      }
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getIsAssignFtName())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.isAssignFtName") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else if (importDTO.getIsAssignFt() == null) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.isAssignFtName") + " " + I18n
              .getLanguage("common.invalid") + ";");
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getReportFailureTypeName())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.reportFailureTypeName") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      if (!mapReportFailureType.containsKey(importDTO.getReportFailureTypeName())) {
        resultImport = resultImport.concat(
            I18n.getLanguage("cfgWoHighTemp.export.grid.reportFailureTypeName") + " " + I18n
                .getLanguage("common.invalid") + ";");
      } else {
        importDTO.setReportFailureType(
            mapReportFailureType.get(importDTO.getReportFailureTypeName()).getItemValue());
      }
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getStatusName())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.statusName") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else if (importDTO.getStatus() == null) {
      resultImport = resultImport.concat(
          I18n.getLanguage("cfgWoHighTemp.export.grid.statusName") + " " + I18n
              .getLanguage("common.invalid") + ";");
    }
    if (StringUtils.isStringNullOrEmpty(resultImport)) {
      if (cfgWoHighTempRepository
          .checkExisted(importDTO.getReasonLv1Id(), importDTO.getReasonLv2Id(),
              importDTO.getActionId(), null)) {
        resultImport = resultImport.concat(
            I18n.getValidation("cfgWoHighTemp.null.unique") + ";");
      }
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      importDTO.setResultImport(resultImport);
      return importDTO;
    }
    String validateDuplicate = validateDuplicateImport(importDTO, mapImportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      importDTO.setResultImport(validateDuplicate);
      return importDTO;
    }

    return importDTO;
  }

  private String validateDuplicateImport(CfgWoHighTempDTO importDTO,
      Map<String, String> mapImportDTO) {
    Long reasonLv1Id = importDTO.getReasonLv1Id();
    Long reasonLv2Id = importDTO.getReasonLv2Id();
    Long actionId = importDTO.getActionId();

    String key = reasonLv1Id + "_" + reasonLv2Id + "_" + actionId;
    String value = mapImportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("common.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }

  @Override
  public List<DataItemDTO> getListPriority() {
    return cfgWoHighTempRepository.getListPriority();
  }

  @Override
  public List<CatReasonInSideDTOSearch> getListReason(Long parentId) {
    String value = commonRepository.getConfigPropertyValue("WO_HIGH_TEMP_REASON");
    if (value != null) {
      List<Long> lstTypeId = new ArrayList<>();
      for (String s : value.split(",")) {
        lstTypeId.add(Long.valueOf(s));
      }
      CatReasonInSideDTO catReasonInSideDTO = new CatReasonInSideDTO();
      catReasonInSideDTO.setLstTypeId(lstTypeId);
      if (parentId == null) {
        catReasonInSideDTO.setIsParentNull(1L);
      } else {
        catReasonInSideDTO.setParentId(parentId);
      }
      List<CatReasonInSideDTOSearch> lst = ttServiceProxy.getListReason(catReasonInSideDTO);
      return lst;
    }
    return new ArrayList<>();
  }

  private void setMapPriority() {
    List<DataItemDTO> lst = getListPriority();
    if (lst != null && !lst.isEmpty()) {
      mapPriority.clear();
      mapPriorityId.clear();
      for (DataItemDTO dto : lst) {
        if (!mapPriority.containsKey(dto.getItemName())) {
          mapPriority.put(dto.getItemName(), dto);
        }
        if (!mapPriorityId.containsKey(dto.getItemId())) {
          mapPriorityId.put(dto.getItemId(), dto);
        }
      }
    }
  }

  private void setMapAction() {
    Datatable datatable = catItemRepository.getItemMaster("WO_HIGH_TEMP_ACTION",
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> lst = (List<CatItemDTO>) datatable.getData();

    if (lst != null && !lst.isEmpty()) {
      mapAction.clear();
      mapActionId.clear();
      for (CatItemDTO dto : lst) {
        if (!mapAction.containsKey(dto.getItemName())) {
          mapAction.put(dto.getItemName(), dto);
        }
        if (!mapActionId.containsKey(dto.getItemId())) {
          mapActionId.put(dto.getItemId(), dto);
        }
      }
    }
  }

  private void setMapActionName() {
    Datatable datatable = catItemRepository.getItemMaster("WO_HIGH_TEMP_ACTION_NAME",
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> lst = (List<CatItemDTO>) datatable.getData();

    if (lst != null && !lst.isEmpty()) {
      mapActionName.clear();
      mapActionNameId.clear();
      for (CatItemDTO dto : lst) {
        if (!mapActionName.containsKey(dto.getItemName())) {
          mapActionName.put(dto.getItemName(), dto);
        }
        if (!mapActionNameId.containsKey(dto.getItemId())) {
          mapActionNameId.put(dto.getItemValue(), dto);
        }
      }
    }
  }

  private void setMapReason() {
    String value = commonRepository.getConfigPropertyValue("WO_HIGH_TEMP_REASON");
    if (value != null) {
      List<Long> lstTypeId = new ArrayList<>();
      for (String s : value.split(",")) {
        lstTypeId.add(Long.valueOf(s));
      }
      CatReasonInSideDTO catReasonInSideDTO = new CatReasonInSideDTO();
      catReasonInSideDTO.setLstTypeId(lstTypeId);
      List<CatReasonInSideDTOSearch> lst = ttServiceProxy.getListReason(catReasonInSideDTO);

      if (lst != null && !lst.isEmpty()) {
        mapReasonLv1Id.clear();
        mapReasonLv2Id.clear();
        for (CatReasonInSideDTOSearch dto : lst) {
          if (dto.getParentId() == null) {
            if (!mapReasonLv1Id.containsKey(dto.getId())) {
              mapReasonLv1Id.put(String.valueOf(dto.getId()), dto);
            }
          } else {
            if (!mapReasonLv2Id.containsKey(dto.getId())) {
              mapReasonLv2Id.put(String.valueOf(dto.getId()), dto);
            }
          }
        }
      }
    }
  }

  private void setMapReportFailureType() {
    Datatable datatable = catItemRepository.getItemMaster("WO_HIGH_TEMP_REPORT_FAILURE_TYPE",
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> lst = (List<CatItemDTO>) datatable.getData();

    if (lst != null && !lst.isEmpty()) {
      mapReportFailureType.clear();
      mapReportFailureTypeId.clear();
      for (CatItemDTO dto : lst) {
        if (!mapReportFailureType.containsKey(dto.getItemName())) {
          mapReportFailureType.put(dto.getItemName(), dto);
        }
        if (!mapReportFailureTypeId.containsKey(dto.getItemId())) {
          mapReportFailureTypeId.put(dto.getItemValue(), dto);
        }
      }
    }
  }

  private void setMapCdType() {
    Datatable datatable = catItemRepository.getItemMaster("WO_HIGH_TEMP_CD_TYPE",
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> lst = (List<CatItemDTO>) datatable.getData();

    if (lst != null && !lst.isEmpty()) {
      mapCdType.clear();
      mapCdTypeId.clear();
      for (CatItemDTO dto : lst) {
        if (!mapCdType.containsKey(dto.getItemName())) {
          mapCdType.put(dto.getItemName(), dto);
        }
        if (!mapCdTypeId.containsKey(dto.getItemId())) {
          mapCdTypeId.put(dto.getItemValue(), dto);
        }
      }
    }
  }
}
