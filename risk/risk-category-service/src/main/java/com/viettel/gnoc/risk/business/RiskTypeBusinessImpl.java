package com.viettel.gnoc.risk.business;

import com.google.common.base.Splitter;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
import com.viettel.gnoc.risk.repository.RiskTypeDetailRepository;
import com.viettel.gnoc.risk.repository.RiskTypeRepository;
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
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
public class RiskTypeBusinessImpl implements RiskTypeBusiness {

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  RiskTypeRepository riskTypeRepository;

  @Autowired
  RiskTypeDetailRepository riskTypeDetailRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;

  private int maxRecord = 500;

  Map<Long, String> mapPriority = new HashMap<>();
  Map<Long, String> mapGroupType = new HashMap<>();

  @Override
  public Datatable getDataRiskTypeSearchWeb(RiskTypeDTO riskTypeDTO) {
    log.debug("Request to getDataRiskTypeSearchWeb: {}", riskTypeDTO);
    return riskTypeRepository.getDataRiskTypeSearchWeb(riskTypeDTO);
  }

  @Override
  public List<RiskTypeDTO> getListRiskTypeDTO(RiskTypeDTO riskTypeDTO) {
    log.debug("Request to getListRiskTypeDTO: {}", riskTypeDTO);
    return riskTypeRepository.getListRiskTypeDTO(riskTypeDTO);
  }

  @Override
  public List<RiskTypeDetailDTO> getListRiskTypeDetail(RiskTypeDetailDTO riskTypeDetailDTO) {
    log.debug("Request to getListRiskTypeDetail: {}", riskTypeDetailDTO);
    return riskTypeDetailRepository.getListRiskTypeDetail(riskTypeDetailDTO);
  }

  @Override
  public RiskTypeDTO findRiskTypeByIdFromWeb(Long riskTypeId) {
    log.debug("Request to findRiskTypeByIdFromWeb: {}", riskTypeId);
    RiskTypeDTO riskTypeDTO = riskTypeRepository.findRiskTypeByIdFromWeb(riskTypeId);
    RiskTypeDetailDTO riskTypeDetailDTO = new RiskTypeDetailDTO();
    riskTypeDetailDTO.setRiskTypeId(riskTypeId);
    List<RiskTypeDetailDTO> listRiskTypeDetailDTO = riskTypeDetailRepository
        .getListRiskTypeDetail(riskTypeDetailDTO);
    riskTypeDTO.setListRiskTypeDetailDTO(listRiskTypeDetailDTO);
    return riskTypeDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdateRiskType(RiskTypeDTO riskTypeDTO) {
    log.debug("Request to insertOrUpdateRiskType : {}", riskTypeDTO);
    ResultInSideDto resultInSideDto;
    Long riskTypeId = riskTypeDTO.getRiskTypeId();
    RiskTypeDTO oldHis = new RiskTypeDTO();
    if (riskTypeDTO.getRiskTypeId() != null) {
      oldHis = findRiskTypeByIdFromWeb(riskTypeDTO.getRiskTypeId());
    }
    resultInSideDto = riskTypeRepository.insertOrUpdateRiskType(riskTypeDTO);
    if (riskTypeId == null) {
      riskTypeId = resultInSideDto.getId();
    } else {
      riskTypeDetailRepository.deleteListRiskTypeDetail(riskTypeId);
    }
    List<RiskTypeDetailDTO> lstRiskTypeDetail = riskTypeDTO.getListRiskTypeDetailDTO();
    if (lstRiskTypeDetail != null && !lstRiskTypeDetail.isEmpty()) {
      for (RiskTypeDetailDTO riskTypeDetailDTO : lstRiskTypeDetail) {
        riskTypeDetailDTO.setRiskTypeId(riskTypeId);
        riskTypeDetailDTO.setId(null);
        resultInSideDto = riskTypeDetailRepository.add(riskTypeDetailDTO);
      }
    }
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("RISK_TYPES");
        dataHistoryChange.setOldObject(oldHis);
        dataHistoryChange.setActionType(riskTypeDTO.getRiskTypeId() != null ? "update" : "add");
        //New Object History
        dataHistoryChange.setNewObject(riskTypeDTO);
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
  public ResultInSideDto delete(Long riskTypeId) {
    log.debug("Request to delete : {}", riskTypeId);
    ResultInSideDto resultInSideDto;
    RiskTypeDTO oldHis = findRiskTypeByIdFromWeb(riskTypeId);
    riskTypeDetailRepository.deleteListRiskTypeDetail(riskTypeId);
    resultInSideDto =  riskTypeRepository.delete(riskTypeId);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(riskTypeId.toString());
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new RiskTypeDTO());
        dataHistoryChange.setType("RISK_TYPES");
        dataHistoryChange.setActionType("delete");
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
  public File exportDataRiskType(RiskTypeDTO riskTypeDTO) throws Exception {
    log.debug("Request to exportDataRiskType: {}", riskTypeDTO);
    List<RiskTypeDTO> listExport = new ArrayList<>();
    List<RiskTypeDTO> list = riskTypeRepository.getListRiskTypeExport(riskTypeDTO);
    if (list != null && list.size() > 0) {
      for (RiskTypeDTO typeDTO : list) {
        RiskTypeDetailDTO typeDetailDTO = new RiskTypeDetailDTO();
        typeDetailDTO.setRiskTypeId(typeDTO.getRiskTypeId());
        List<RiskTypeDetailDTO> listDetail = riskTypeDetailRepository
            .getListRiskTypeDetail(typeDetailDTO);
        if (listDetail == null || listDetail.size() == 0) {
          for (String temp : mapPriority.values()) {
            RiskTypeDTO dto = new RiskTypeDTO();
            dto.setRiskTypeCode(typeDTO.getRiskTypeCode());
            dto.setRiskTypeName(typeDTO.getRiskTypeName());
            dto.setStatusName(typeDTO.getStatusName());
            dto.setRiskGroupTypeName(typeDTO.getRiskGroupTypeName());
            dto.setPriorityName(temp);
            dto.setProcessTime(null);
            dto.setTimeWarningOverdue(null);
            dto.setWarningSchedule(null);
            listExport.add(dto);
          }
        } else {
          setMapPriority();
          for (RiskTypeDetailDTO detailDTO : listDetail) {
            RiskTypeDTO dto = new RiskTypeDTO();
            dto.setRiskTypeCode(typeDTO.getRiskTypeCode());
            dto.setRiskTypeName(typeDTO.getRiskTypeName());
            dto.setStatusName(typeDTO.getStatusName());
            dto.setRiskGroupTypeName(typeDTO.getRiskGroupTypeName());
            dto.setPriorityName(mapPriority.get(detailDTO.getPriorityId()));
            dto.setProcessTime(detailDTO.getProcessTime());
            dto.setWarningSchedule(detailDTO.getWarningSchedule());
            dto.setTimeWarningOverdue(detailDTO.getTimeWarningOverdue());
            listExport.add(dto);
          }
        }
      }
    }
    String[] header = new String[]{"riskTypeCode", "riskTypeName", "riskGroupTypeName",
        "statusName", "priorityName", "processTime", "warningSchedule", "timeWarningOverdue"};
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(listExport, header, DateUtil.date2ddMMyyyyHHMMss(date),
        "EXPORT_RISK_TYPE");
  }

  @Override
  public File getTemplateImport() throws IOException {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");

    String[] header = new String[]{
        I18n.getLanguage("riskType.STT"),
        I18n.getLanguage("riskType.riskTypeCode"),
        I18n.getLanguage("riskType.riskTypeName"),
        I18n.getLanguage("riskType.riskGroupTypeName"),
        I18n.getLanguage("riskType.statusName"),
        I18n.getLanguage("riskType.priorityName"),
        I18n.getLanguage("riskType.processTimeStr"),
        I18n.getLanguage("riskType.warningScheduleStr"),
        I18n.getLanguage("riskType.timeWarningOverdueStr"),
        I18n.getLanguage("riskType.actionName")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("riskType.riskTypeCode"),
        I18n.getLanguage("riskType.riskTypeName"),
        I18n.getLanguage("riskType.statusName"),
        I18n.getLanguage("riskType.priorityName"),
        I18n.getLanguage("riskType.processTimeStr"),
        I18n.getLanguage("riskType.actionName")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("riskType.STT"));
    int riskTypeCodeColumn = listHeader.indexOf(I18n.getLanguage("riskType.riskTypeCode"));
    int riskTypeNameColumn = listHeader.indexOf(I18n.getLanguage("riskType.riskTypeName"));
    int riskGroupTypeNameColumn = listHeader
        .indexOf(I18n.getLanguage("riskType.riskGroupTypeName"));
    int statusNameColumn = listHeader.indexOf(I18n.getLanguage("riskType.statusName"));
    int priorityNameColumn = listHeader.indexOf(I18n.getLanguage("riskType.priorityName"));
    int processTimeColumn = listHeader.indexOf(I18n.getLanguage("riskType.processTimeStr"));
    int warningScheduleColumn = listHeader.indexOf(I18n.getLanguage("riskType.warningScheduleStr"));
    int timeWarningOverdueColumn = listHeader
        .indexOf(I18n.getLanguage("riskType.timeWarningOverdueStr"));
    int actionNameColumn = listHeader.indexOf(I18n.getLanguage("riskType.actionName"));

    String firstLeftHeaderTitle = I18n.getLanguage("riskType.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("riskType.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("riskType.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("riskType.export.secondRightHeader");

    Font xssFontTopHeader = workBook.createFont();
    xssFontTopHeader.setFontName("Times New Roman");
    xssFontTopHeader.setFontHeightInPoints((short) 10);
    xssFontTopHeader.setColor(IndexedColors.BLACK.index);
    xssFontTopHeader.setBold(true);

    Font xSSFFont = workBook.createFont();
    xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFont.setFontHeightInPoints((short) 20);
    xSSFFont.setBold(true);
    xSSFFont.setColor(IndexedColors.BLACK.index);

    Font xSSFFontHeader = workBook.createFont();
    xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    xSSFFontHeader.setColor(IndexedColors.BLUE.index);
    xSSFFontHeader.setBold(true);

    Font rowDataFont = workBook.createFont();
    rowDataFont.setFontName(HSSFFont.FONT_ARIAL);
    rowDataFont.setFontHeightInPoints((short) 10);
    rowDataFont.setColor(IndexedColors.BLACK.index);

    CellStyle cellStyleTopHeader = workBook.createCellStyle();
    cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTopHeader.setFont(xssFontTopHeader);

    CellStyle cellStyleTopRightHeader = workBook.createCellStyle();
    cellStyleTopRightHeader.setAlignment(HorizontalAlignment.LEFT);
    cellStyleTopRightHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTopRightHeader.setFont(xssFontTopHeader);

    CellStyle cellStyleTitle = workBook.createCellStyle();
    cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
    cellStyleTitle.setFont(xSSFFont);

    CellStyle cellStyleHeader = workBook.createCellStyle();
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
    mainCellTitle.setCellValue(I18n.getLanguage("riskType.template.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetMain.createRow(5);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append(" (*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    int row = 1;
    Datatable dataRiskGroupType = catItemRepository
        .getItemMaster(Constants.CATEGORY.RISK_GROUP_TYPE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> listRiskGroupType = (List<CatItemDTO>) dataRiskGroupType.getData();
    for (CatItemDTO dto : listRiskGroupType) {
      ewu.createCell(sheetOrther, 0, row++, dto.getItemName(), styles.get("cell"));
    }
    Name riskGroupTypeName = workBook.createName();
    riskGroupTypeName.setNameName("riskGroupTypeName");
    riskGroupTypeName.setRefersToFormula("Orther!$A$2:$A$" + row);
    XSSFDataValidationConstraint riskGroupTypeConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "riskGroupTypeName");
    CellRangeAddressList cellRangeRiskGroupType = new CellRangeAddressList(6, (maxRecord + 6),
        riskGroupTypeNameColumn, riskGroupTypeNameColumn);
    XSSFDataValidation dataValidationRiskGroupType = (XSSFDataValidation) dvHelper
        .createValidation(riskGroupTypeConstraint, cellRangeRiskGroupType);
    dataValidationRiskGroupType.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationRiskGroupType);

    row = 1;
    ewu.createCell(sheetOrther, 1, row++, I18n.getLanguage("riskType.status.1"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 1, row++, I18n.getLanguage("riskType.status.0"),
        styles.get("cell"));
    Name statusName = workBook.createName();
    statusName.setNameName("statusName");
    statusName.setRefersToFormula("Orther!$B$2:$B$" + row);
    XSSFDataValidationConstraint statusConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "statusName");
    CellRangeAddressList cellRangeStatus = new CellRangeAddressList(6, (maxRecord + 6),
        statusNameColumn, statusNameColumn);
    XSSFDataValidation dataValidationStatus = (XSSFDataValidation) dvHelper.createValidation(
        statusConstraint, cellRangeStatus);
    dataValidationStatus.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationStatus);

    row = 1;
    Datatable dataPriority = catItemRepository.getItemMaster(Constants.CATEGORY.RISK_PRIORITY,
        LANGUAGUE_EXCHANGE_SYSTEM.RISK, APPLIED_BUSSINESS.RISK_TYPE,
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> listPriority = (List<CatItemDTO>) dataPriority.getData();
    for (CatItemDTO dto : listPriority) {
      ewu.createCell(sheetOrther, 2, row++, dto.getItemName(), styles.get("cell"));
    }
    Name priorityName = workBook.createName();
    priorityName.setNameName("priorityName");
    priorityName.setRefersToFormula("Orther!$C$2:$C$" + row);
    XSSFDataValidationConstraint priorityConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "priorityName");
    CellRangeAddressList cellRangePriority = new CellRangeAddressList(6, (maxRecord + 6),
        priorityNameColumn, priorityNameColumn);
    XSSFDataValidation dataValidationPriority = (XSSFDataValidation) dvHelper.createValidation(
        priorityConstraint, cellRangePriority);
    dataValidationPriority.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationPriority);

    row = 1;
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("riskType.action.0"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("riskType.action.1"),
        styles.get("cell"));
    Name actionName = workBook.createName();
    actionName.setNameName("actionName");
    actionName.setRefersToFormula("Orther!$D$2:$D$" + row);
    XSSFDataValidationConstraint actionConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "actionName");
    CellRangeAddressList cellRangeAction = new CellRangeAddressList(6, (maxRecord + 6),
        actionNameColumn, actionNameColumn);
    XSSFDataValidation dataValidationAction = (XSSFDataValidation) dvHelper.createValidation(
        actionConstraint, cellRangeAction);
    dataValidationAction.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationAction);

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(riskTypeCodeColumn, 8000);
    sheetMain.setColumnWidth(riskTypeNameColumn, 8000);
    sheetMain.setColumnWidth(riskGroupTypeNameColumn, 6000);
    sheetMain.setColumnWidth(statusNameColumn, 6000);
    sheetMain.setColumnWidth(priorityNameColumn, 6000);
    sheetMain.setColumnWidth(processTimeColumn, 6000);
    sheetMain.setColumnWidth(warningScheduleColumn, 6000);
    sheetMain.setColumnWidth(timeWarningOverdueColumn, 6000);
    sheetMain.setColumnWidth(actionNameColumn, 6000);

    sheetOrther.setSelected(false);
    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("riskType.import.title"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_RISK_TYPE" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importDataRiskType(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<RiskTypeDTO> listDto;
    List<RiskTypeDTO> listImportDto;
    Map<String, String> mapImportDTO;

    String[] header = new String[]{"riskTypeCode", "riskTypeName", "riskGroupTypeName",
        "statusName", "priorityName", "processTimeStr", "warningScheduleStr",
        "timeWarningOverdueStr", "actionName", "resultImport"};

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
            0, 9, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 6,
            0, 9, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        listImportDto = new ArrayList<>();
        mapImportDTO = new HashMap<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          setMapGroupType();
          setMapPriority();
          for (Object[] obj : lstData) {
            RiskTypeDTO importDTO = new RiskTypeDTO();
            if (obj[1] != null) {
              importDTO.setRiskTypeCode(obj[1].toString().trim());
            }
            if (obj[2] != null) {
              importDTO.setRiskTypeName(obj[2].toString().trim());
            }
            if (obj[3] != null) {
              importDTO.setRiskGroupTypeName(obj[3].toString().trim());
            }
            if (obj[4] != null) {
              importDTO.setStatusName(obj[4].toString().trim());
            }
            if (obj[5] != null) {
              importDTO.setPriorityName(obj[5].toString().trim());
            }
            if (obj[6] != null) {
              importDTO.setProcessTimeStr(obj[6].toString().trim());
            }
            if (obj[7] != null) {
              importDTO.setWarningScheduleStr(obj[7].toString().trim());
            }
            if (obj[8] != null) {
              importDTO.setTimeWarningOverdueStr(obj[8].toString().trim());
            }
            if (obj[9] != null) {
              importDTO.setActionName(obj[9].toString().trim());
            }
            RiskTypeDTO tempImportDTO = validateImportInfo(importDTO, mapImportDTO);
            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO.setResultImport(I18n.getLanguage("riskType.result.import.ok"));
              listImportDto.add(tempImportDTO);
              String riskTypeCode = tempImportDTO.getRiskTypeCode();
              Long priorityId = tempImportDTO.getPriorityId();
              mapImportDTO.put(riskTypeCode + "_" + priorityId, String.valueOf(value));
              listDto.add(tempImportDTO);
            } else {
              listImportDto.add(tempImportDTO);
              index++;
            }
            value++;
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              for (RiskTypeDTO dto : listDto) {
                resultInSideDto = insertOrUpdateRiskTypeImport(dto);
              }
            }
          } else {
            File fileExport = handleFileExport(listImportDto, header, null, "RESULT_IMPORT");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFileExport(listImportDto, header, null, "RESULT_IMPORT");
          resultInSideDto.setFile(fileExport);
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

  public ResultInSideDto insertOrUpdateRiskTypeImport(RiskTypeDTO riskTypeDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<RiskTypeDetailDTO> listDetailDTO = new ArrayList<>();
    RiskTypeDetailDTO detailDTO = new RiskTypeDetailDTO();
    detailDTO.setPriorityId(riskTypeDTO.getPriorityId());
    detailDTO.setProcessTime(riskTypeDTO.getProcessTime());
    detailDTO.setTimeWarningOverdue(riskTypeDTO.getTimeWarningOverdue());
    detailDTO.setWarningSchedule(riskTypeDTO.getWarningSchedule());
    listDetailDTO.add(detailDTO);
    riskTypeDTO.setListRiskTypeDetailDTO(listDetailDTO);
    if (riskTypeDTO.getAction().equals(0L)) {
      if (riskTypeDTO.getRiskTypeId() == null) {
        resultInSideDto = insertOrUpdateRiskType(riskTypeDTO);
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
      }
    }
    if (riskTypeDTO.getAction().equals(1L)) {
      if (riskTypeDTO.getRiskTypeId() != null) {
        resultInSideDto = insertOrUpdateRiskType(riskTypeDTO);
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
      }
    }
    return resultInSideDto;
  }

  public RiskTypeDTO validateImportInfo(RiskTypeDTO importDTO, Map<String, String> mapImportDTO) {
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(importDTO.getRiskTypeCode())) {
      resultImport = resultImport.concat(I18n.getLanguage("riskType.err.riskTypeCode"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getRiskTypeName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskType.err.riskTypeName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getStatusName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskType.err.status"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getPriorityName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskType.err.priority"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getProcessTimeStr())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskType.err.processTime"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getActionName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskType.err.actionName"));
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      importDTO.setResultImport(resultImport);
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getRiskTypeCode())) {
      String riskTypeCode = importDTO.getRiskTypeCode();
      if (!isValidRiskTypeCode(riskTypeCode)) {
        importDTO.setResultImport(I18n.getLanguage("riskType.err.riskTypeCode.notValid"));
        return importDTO;
      } else {
        char cf = riskTypeCode.charAt(0);
        char ce = riskTypeCode.charAt(riskTypeCode.length() - 1);
        if (cf == '_') {
          importDTO.setResultImport(I18n.getLanguage("riskType.err.riskTypeCode.underscoreFirst"));
          return importDTO;
        }
        if (ce == '_') {
          importDTO.setResultImport(I18n.getLanguage("riskType.err.riskTypeCode.underscoreEnd"));
          return importDTO;
        }
        if (riskTypeCode.length() > 500) {
          importDTO.setResultImport(I18n.getLanguage("riskType.err.riskTypeCode.length"));
          return importDTO;
        }
      }
      importDTO.setRiskTypeCode(riskTypeCode.toUpperCase());
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getRiskTypeName())) {
      if (importDTO.getRiskTypeName().length() > 500) {
        importDTO.setResultImport(I18n.getLanguage("riskType.err.riskTypeName.length"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getRiskGroupTypeName())) {
      setMapGroupType();
      for (Long riskGroupType : mapGroupType.keySet()) {
        if (importDTO.getRiskGroupTypeName().equals(mapGroupType.get(riskGroupType))) {
          importDTO.setRiskGroupTypeId(riskGroupType);
        }
      }
      if (importDTO.getRiskGroupTypeId() == null) {
        importDTO.setResultImport(I18n.getLanguage("riskType.err.riskGroupType.valid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getStatusName())) {
      if (I18n.getLanguage("riskType.status.1").equals(importDTO.getStatusName())) {
        importDTO.setStatus(1L);
      } else if (I18n.getLanguage("riskType.status.0").equals(importDTO.getStatusName())) {
        importDTO.setStatus(0L);
      } else {
        importDTO.setResultImport(I18n.getLanguage("riskType.err.status.valid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getPriorityName())) {
      setMapPriority();
      for (Long priority : mapPriority.keySet()) {
        if (importDTO.getPriorityName().equals(mapPriority.get(priority))) {
          importDTO.setPriorityId(priority);
        }
      }
    }
    if (importDTO.getPriorityId() == null) {
      importDTO.setResultImport(I18n.getLanguage("riskType.err.priority.valid"));
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getProcessTimeStr())) {
      String processTimeStr = importDTO.getProcessTimeStr();
      if (!isValidDouble(processTimeStr)) {
        importDTO.setResultImport(I18n.getLanguage("riskType.err.processTime.valid"));
        return importDTO;
      } else {
        if (processTimeStr.contains(".")) {
          List<String> listValue = Splitter.on(".").trimResults().omitEmptyStrings()
              .splitToList(processTimeStr);
          String pn = listValue.get(0);
          String ptp = listValue.get(1);
          if (pn.length() > 10) {
            importDTO.setResultImport(I18n.getLanguage("riskType.err.processTime.valid.pn"));
            return importDTO;
          }
          if (ptp.length() > 2) {
            importDTO.setResultImport(I18n.getLanguage("riskType.err.processTime.valid.ptp"));
            return importDTO;
          }
          if (Double.valueOf(processTimeStr) < 0D) {
            importDTO.setResultImport(I18n.getLanguage("riskType.err.processTime.valid"));
            return importDTO;
          }
          importDTO.setProcessTime(Double.valueOf(importDTO.getProcessTimeStr()));
        } else {
          if (processTimeStr.length() > 10) {
            importDTO.setResultImport(I18n.getLanguage("riskType.err.processTime.valid.pn"));
            return importDTO;
          }
          if (Double.valueOf(processTimeStr) < 0L) {
            importDTO.setResultImport(I18n.getLanguage("riskType.err.processTime.valid"));
            return importDTO;
          }
          importDTO.setProcessTime(Double.valueOf(processTimeStr));
        }
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getWarningScheduleStr())) {
      if (!StringUtils.isLong(importDTO.getWarningScheduleStr())) {
        importDTO.setResultImport(I18n.getLanguage("riskType.err.warningSchedule.valid"));
        return importDTO;
      } else if (Long.valueOf(importDTO.getWarningScheduleStr()) < 0L) {
        importDTO.setResultImport(I18n.getLanguage("riskType.err.warningSchedule.valid"));
        return importDTO;
      }
      importDTO.setWarningSchedule(Long.valueOf(importDTO.getWarningScheduleStr()));
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getTimeWarningOverdueStr())) {
      if (!StringUtils.isLong(importDTO.getTimeWarningOverdueStr())) {
        importDTO.setResultImport(I18n.getLanguage("riskType.err.timeWarningOverdue.valid"));
        return importDTO;
      } else if (Long.valueOf(importDTO.getTimeWarningOverdueStr()) < 0L) {
        importDTO.setResultImport(I18n.getLanguage("riskType.err.timeWarningOverdue.valid"));
        return importDTO;
      }
      importDTO.setTimeWarningOverdue(Long.valueOf(importDTO.getTimeWarningOverdueStr()));
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getActionName())) {
      if (I18n.getLanguage("riskType.action.0").equals(importDTO.getActionName())) {
        importDTO.setAction(0L);
      } else if (I18n.getLanguage("riskType.action.1").equals(importDTO.getActionName())) {
        importDTO.setAction(1L);
      } else {
        importDTO.setResultImport(I18n.getLanguage("riskType.err.action.valid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getRiskTypeCode())) {
      RiskTypeDTO dtoTmp = riskTypeRepository.checkRiskTypeExit(importDTO);
      if (dtoTmp != null) {
        if (I18n.getLanguage("riskType.action.0").equals(importDTO.getActionName())) {
          importDTO.setResultImport(I18n.getLanguage("riskType.err.dup-code"));
          return importDTO;
        }
        if (I18n.getLanguage("riskType.action.1").equals(importDTO.getActionName())) {
          importDTO.setRiskTypeId(dtoTmp.getRiskTypeId());
        }
      } else {
        if (I18n.getLanguage("riskType.action.1").equals(importDTO.getActionName())) {
          importDTO.setResultImport(I18n.getLanguage("riskType.err.notExist"));
          return importDTO;
        }
      }
    }
    String validateDuplicate = validateDuplicateImport(importDTO, mapImportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      importDTO.setResultImport(validateDuplicate);
      return importDTO;
    }
    return importDTO;
  }

  public String validateDuplicateImport(RiskTypeDTO importDTO, Map<String, String> mapImportDTO) {
    String riskTypeCode = importDTO.getRiskTypeCode();
    Long priorityId = importDTO.getPriorityId();
    String key = riskTypeCode + "_" + priorityId;
    String value = mapImportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("riskType.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }

  private boolean isValidDouble(String value) {
    if (value.contains(".")) {
      List<String> listValue = Splitter.on(".").trimResults().omitEmptyStrings().splitToList(value);
      if (listValue.size() <= 2) {
        for (String va : listValue) {
          for (int i = 0; i < va.length(); i++) {
            if (!Character.isDigit(va.charAt(i))) {
              return false;
            }
          }
        }
        return true;
      } else {
        return false;
      }
    } else {
      for (int i = 0; i < value.length(); i++) {
        if (!Character.isDigit(value.charAt(i))) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isValidRiskTypeCode(String riskTypeCode) {
    String regex = "([A-Za-z0-9_]+)";
    return riskTypeCode.matches(regex);
  }

  private String checkResultImport(String resultImport) {
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      resultImport = resultImport.concat("; \n");
    }
    return resultImport;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 10) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("riskType.STT"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskType.riskTypeCode") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskType.riskTypeName") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskType.riskGroupTypeName"))
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskType.statusName") + " (*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskType.priorityName") + " (*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskType.processTimeStr") + " (*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskType.warningScheduleStr"))
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskType.timeWarningOverdueStr"))
        .equalsIgnoreCase(obj[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskType.actionName") + " (*)")
        .equalsIgnoreCase(obj[9].toString().trim())) {
      return false;
    }
    return true;
  }

  public File handleFileExport(List<RiskTypeDTO> list, String[] columnExport, String date,
      String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = "";
    String title = "";
    String fileNameOut = "";
    String headerPrefix = "language.riskType";
    String firstLeftHeader = I18n.getLanguage("riskType.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("riskType.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("riskType.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("riskType.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle;
    if (StringUtils.isNotNullOrEmpty(date)) {
      subTitle = I18n.getLanguage("riskType.export.exportDate", date);
    } else {
      subTitle = "";
    }
    switch (code) {
      case "RESULT_IMPORT":
        sheetName = I18n.getLanguage("riskType.import.sheetname");
        title = I18n.getLanguage("riskType.import.title");
        fileNameOut = I18n.getLanguage("riskType.import.fileNameOut");
        break;
      case "EXPORT_RISK_TYPE":
        sheetName = I18n.getLanguage("riskType.export.sheetname");
        title = I18n.getLanguage("riskType.export.title");
        fileNameOut = I18n.getLanguage("riskType.export.fileNameOut");
        break;
      default:
        break;
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        list,
        sheetName,
        title,
        subTitle,
        startRow,
        cellTitleIndex,
        mergeTitleEndIndex,
        true,
        headerPrefix,
        lstHeaderSheet1,
        fieldSplit,
        "",
        firstLeftHeader,
        secondLeftHeader,
        firstRightHeader,
        secondRightHeader
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(startRow, 0, 0, 0,
        I18n.getLanguage("riskType.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  public List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false,
          0, 0, new String[]{}, null, "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
  }

  public void setMapPriority() {
    List<CatItemDTO> list = catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null);
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapPriority.put(dto.getItemId(), dto.getItemName());
      }
    }
  }

  public void setMapGroupType() {
    Datatable dataGroupType = catItemRepository
        .getItemMaster(Constants.CATEGORY.RISK_GROUP_TYPE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataGroupType.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapGroupType.put(dto.getItemId(), dto.getItemName());
      }
    }
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = Arrays.asList("riskTypeId", "riskTypeCode", "riskTypeName", "status", "riskGroupTypeId");
      return keys;
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
