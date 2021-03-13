package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.DataHistoryChange;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.CFG_TIME_TROUBLE_PROCESS_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessExportDTO;
import com.viettel.gnoc.incident.repository.CfgTimeTroubleProcessRepository;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
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
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class CfgTimeTroubleProcessBusinessImpl implements CfgTimeTroubleProcessBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Autowired
  CfgTimeTroubleProcessRepository cfgTimeTroubleProcessRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;

  Map<String, String> mapCountry = new HashMap<>();
  Map<Long, String> mapType = new HashMap<>();
  Map<Long, String> mapSubCategory = new HashMap<>();
  Map<Long, String> mapPriority = new HashMap<>();

  private final static String CFG_TIME_TROUBLE_PROCESS_SEQ = "CFG_TIME_TROUBLE_PROCESS_SEQ";
  private int maxRecord = 500;

  @Override
  public Datatable getListCfgTimeTroubleProcessDTO(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    log.debug("Request to getListCfgTimeTroubleProcessDTO: {}", cfgTimeTroubleProcessDTO);
    return cfgTimeTroubleProcessRepository
        .getListCfgTimeTroubleProcessDTO(cfgTimeTroubleProcessDTO);
  }

  @Override
  public CfgTimeTroubleProcessDTO getConfigTimeTroubleProcess(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    log.debug("Request to getConfigTimeTroubleProcess: {}", cfgTimeTroubleProcessDTO);
    return cfgTimeTroubleProcessRepository
        .getConfigTimeTroubleProcess(cfgTimeTroubleProcessDTO.getTypeId(),
            cfgTimeTroubleProcessDTO.getSubCategoryId(), cfgTimeTroubleProcessDTO.getPriorityId(),
            cfgTimeTroubleProcessDTO.getCountry());
  }

  @Override
  public CfgTimeTroubleProcessDTO findCfgTimeTroubleProcessById(Long id) {
    log.debug("Request to findCfgTimeTroubleProcessById: {}", id);
    return cfgTimeTroubleProcessRepository.findCfgTimeTroubleProcessById(id);
  }

  @Override
  public ResultInSideDto insertCfgTimeTroubleProcess(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    log.debug("Request to insertCfgTimeTroubleProcess: {}", cfgTimeTroubleProcessDTO);
    ResultInSideDto resultInSideDto;
    resultInSideDto = cfgTimeTroubleProcessRepository.insertCfgTimeTroubleProcess(cfgTimeTroubleProcessDTO);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("TT_CONFIG_TIME");
        //Old Object History
        dataHistoryChange.setOldObject(new CfgTimeTroubleProcessDTO());
        dataHistoryChange.setActionType("add");
        //New Object History
        dataHistoryChange.setNewObject(cfgTimeTroubleProcessDTO);
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
  public ResultInSideDto updateCfgTimeTroubleProcess(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    log.debug("Request to updateCfgTimeTroubleProcess: {}", cfgTimeTroubleProcessDTO);
    ResultInSideDto resultInSideDto;
    CfgTimeTroubleProcessDTO oldHis = findCfgTimeTroubleProcessById(cfgTimeTroubleProcessDTO.getId());
    resultInSideDto = cfgTimeTroubleProcessRepository.updateCfgTimeTroubleProcess(cfgTimeTroubleProcessDTO);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(cfgTimeTroubleProcessDTO.getId().toString());
        dataHistoryChange.setType("TT_CONFIG_TIME");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        dataHistoryChange.setActionType("update");
        //New Object History
        dataHistoryChange.setNewObject(cfgTimeTroubleProcessDTO);
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
  public ResultInSideDto insertOrUpdateListCfgTimeTroubleProcess(
      List<CfgTimeTroubleProcessDTO> listCfgTimeTroubleProcessDTO) {
    log.debug("Request to updateCfgTimeTroubleProcess: {}", listCfgTimeTroubleProcessDTO);
    return cfgTimeTroubleProcessRepository
        .insertOrUpdateListCfgTimeTroubleProcess(listCfgTimeTroubleProcessDTO);
  }

  @Override
  public ResultInSideDto deleteCfgTimeTroubleProcess(Long id) {
    log.debug("Request to deleteCfgTimeTroubleProcess: {}", id);
    ResultInSideDto resultInSideDto;
    CfgTimeTroubleProcessDTO oldHis = findCfgTimeTroubleProcessById(id);
    resultInSideDto = cfgTimeTroubleProcessRepository.deleteCfgTimeTroubleProcess(id);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(id.toString());
        dataHistoryChange.setType("TT_CONFIG_TIME");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        dataHistoryChange.setActionType("delete");
        //New Object History
        dataHistoryChange.setNewObject(new CfgTimeTroubleProcessDTO());
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
  public ResultInSideDto deleteListCfgTimeTroubleProcess(
      List<CfgTimeTroubleProcessDTO> listCfgTimeTroubleProcessDTO) {
    log.debug("Request to deleteListCfgTimeTroubleProcess: {}", listCfgTimeTroubleProcessDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (CfgTimeTroubleProcessDTO dto : listCfgTimeTroubleProcessDTO) {
      resultInSideDto = deleteCfgTimeTroubleProcess(dto.getId());
    }
    return resultInSideDto;
  }

  @Override
  public List<String> getSequenseCfgTimeTroubleProcess(int... size) {
    log.debug("Request to getSequenseCfgTimeTroubleProcess: {}", size);
    return cfgTimeTroubleProcessRepository
        .getSequenseCfgTimeTroubleProcess(CFG_TIME_TROUBLE_PROCESS_SEQ, 1);
  }

  @Override
  public List<CfgTimeTroubleProcessDTO> getListCfgTimeTroubleProcessByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    log.debug("Request to getListCfgTimeTroubleProcessByCondition: {}", lstCondition);
    return cfgTimeTroubleProcessRepository
        .getListCfgTimeTroubleProcessByCondition(lstCondition, rowStart, maxRow, sortType,
            sortFieldList);
  }

  @Override
  public File exportData(CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) throws Exception {
    List<CfgTimeTroubleProcessExportDTO> listExport = new ArrayList<>();
    List<CfgTimeTroubleProcessDTO> list = cfgTimeTroubleProcessRepository
        .getListCfgTimeTroubleProcessExport(cfgTimeTroubleProcessDTO);
    if (list != null && !list.isEmpty()) {
      for (CfgTimeTroubleProcessDTO dto : list) {
        CfgTimeTroubleProcessExportDTO exportDTO = new CfgTimeTroubleProcessExportDTO();
        exportDTO.setTypeName(dto.getTypeName());
        exportDTO.setSubCategoryName(dto.getSubCategoryName());
        exportDTO.setPriorityName(dto.getPriorityName());
        exportDTO.setCreateTtTime(dto.getCreateTtTime());
        exportDTO.setProcessTtTime(dto.getProcessTtTime());
        exportDTO.setTimeStationVip(dto.getTimeStationVip());
        exportDTO.setTimeWoVip(dto.getTimeWoVip());
        exportDTO.setCloseTtTime(dto.getCloseTtTime());
        exportDTO.setProcessWoTime(dto.getProcessWoTime());
        exportDTO.setTimeAboutOverdue(dto.getTimeAboutOverdue());
        if ("1".equals(String.valueOf(dto.getIsCall()))) {
          exportDTO.setIsCallName(I18n.getLanguage("cfgTimeTroubleProcessDTO.isCallName.1"));
        } else {
          exportDTO.setIsCallName(I18n.getLanguage("cfgTimeTroubleProcessDTO.isCallName.0"));
        }
        if ("1".equals(String.valueOf(dto.getIsStationVip()))) {
          exportDTO.setStationVip(I18n.getLanguage("cfgTimeTroubleProcessDTO.stationVip.1"));
        } else {
          exportDTO.setStationVip(I18n.getLanguage("cfgTimeTroubleProcessDTO.stationVip.0"));
        }
        exportDTO.setLastUpdateTime(DateUtil.date2ddMMyyyyHHMMss(dto.getLastUpdateTime()));
        exportDTO.setCountryName(dto.getCountryName());
        exportDTO.setCdAudioName(dto.getCdAudioName());
        exportDTO.setFtAudioName(dto.getFtAudioName());
        listExport.add(exportDTO);
      }
    }
    String[] header = new String[]{"countryName", "typeName", "subCategoryName", "priorityName",
        "createTtTime", "processTtTime", "closeTtTime", "processWoTime", "isCallName",
        "timeAboutOverdue", "cdAudioName", "ftAudioName", "stationVip", "timeStationVip",
        "timeWoVip"}; // "lastUpdateTime"
    return handleFileExport(listExport, header, DateUtil.date2ddMMyyyyHHMMss(new Date()), "");
  }

  @Override
  public List<CatItemDTO> getItemDTO(String typeId, String alarmId, String nationCode) {
    return cfgTimeTroubleProcessRepository.getItemDTO(typeId, alarmId, nationCode);
  }

  @Override
  public File getTemplate() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetCfgTime = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");

    Map<String, List<String>> categoryTypeSub = new HashMap<>();
    Datatable dataType = catItemRepository
        .getItemMaster(CFG_TIME_TROUBLE_PROCESS_MASTER_CODE.PT_TYPE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> lstType = (List<CatItemDTO>) dataType.getData();
    List<String> lstTypeCode = new ArrayList<>();
    List<Long> lstTypeId = new ArrayList<>();
    for (int i = 0; i < lstType.size(); i++) {
      String typeCode = lstType.get(i).getItemCode();
      char c = typeCode.charAt(0);
      if (Character.isDigit(c)) {
        typeCode = "_" + typeCode;
      }
      lstTypeCode.add(typeCode);
      lstTypeId.add(lstType.get(i).getItemId());
      List<String> lstSubCategoryName = new ArrayList<>();
      List<CatItemDTO> lstSubCategory = cfgTimeTroubleProcessRepository
          .getListSubCategory(lstTypeId.get(i));
      if (lstSubCategory != null && !lstSubCategory.isEmpty()) {
        for (CatItemDTO dto : lstSubCategory) {
          lstSubCategoryName.add(dto.getItemName());
        }
        categoryTypeSub.put(lstTypeCode.get(i), lstSubCategoryName);
      }
    }

    XSSFSheet sheetTypeSub = workBook.createSheet("SheetTypeSub");
    Row ro1;
    String col1;
    String ref1;
    int c1 = 0;
    for (String key : categoryTypeSub.keySet()) {
      int r1 = 0;
      ro1 = sheetTypeSub.getRow(r1);
      if (ro1 == null) {
        ro1 = sheetTypeSub.createRow(r1);
      }
      r1++;
      ro1.createCell(c1).setCellValue(key);
      List<String> items = categoryTypeSub.get(key);
      if (items.size() > 0) {
        for (String item : items) {
          ro1 = sheetTypeSub.getRow(r1);
          if (ro1 == null) {
            ro1 = sheetTypeSub.createRow(r1);
          }
          r1++;
          ro1.createCell(c1).setCellValue(item);
        }
      }
      col1 = CellReference.convertNumToColString(c1);
      Name type = workBook.createName();
      type.setNameName(key);
      ref1 = "SheetTypeSub!$" + col1 + "$2:$" + col1 + "$" + r1;
      type.setRefersToFormula(ref1);
      c1++;
    }
    col1 = CellReference.convertNumToColString((c1 - 1));
    Name subCategory = workBook.createName();
    subCategory.setNameName("Type");
    ref1 = "SheetTypeSub!$A$1:$" + col1 + "$1";
    subCategory.setRefersToFormula(ref1);
    sheetTypeSub.setSelected(false);
    workBook.setSheetHidden(2, true);
    workBook.setActiveSheet(0);

    String[] header = new String[]{
        I18n.getLanguage("cfgTimeTroubleProcessDTO.stt"),
        I18n.getLanguage("cfgTimeTroubleProcess.countryName"),
        I18n.getLanguage("cfgTimeTroubleProcess.typeName"),
        I18n.getLanguage("cfgTimeTroubleProcess.subCategoryName"),
        I18n.getLanguage("cfgTimeTroubleProcess.priorityName"),
        I18n.getLanguage("cfgTimeTroubleProcess.createTtTime"),
        I18n.getLanguage("cfgTimeTroubleProcess.processTtTime"),
        I18n.getLanguage("cfgTimeTroubleProcess.closeTtTime"),
        I18n.getLanguage("cfgTimeTroubleProcess.processWoTime"),
        I18n.getLanguage("cfgTimeTroubleProcess.isCallName"),
        I18n.getLanguage("cfgTimeTroubleProcess.timeAboutOverdue"),
        I18n.getLanguage("cfgTimeTroubleProcess.cdAudioName"),
        I18n.getLanguage("cfgTimeTroubleProcess.ftAudioName"),
        I18n.getLanguage("cfgTimeTroubleProcess.stationVip"),
        I18n.getLanguage("cfgTimeTroubleProcess.timeStationVip"),
        I18n.getLanguage("cfgTimeTroubleProcess.timeWoVip")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("cfgTimeTroubleProcess.countryName"),
        I18n.getLanguage("cfgTimeTroubleProcess.typeName"),
        I18n.getLanguage("cfgTimeTroubleProcess.subCategoryName"),
        I18n.getLanguage("cfgTimeTroubleProcess.priorityName"),
        I18n.getLanguage("cfgTimeTroubleProcess.createTtTime"),
        I18n.getLanguage("cfgTimeTroubleProcess.processTtTime"),
        I18n.getLanguage("cfgTimeTroubleProcess.closeTtTime"),
        I18n.getLanguage("cfgTimeTroubleProcess.processWoTime")
    };
    String[] headerRequiredStationVip = new String[]{
        I18n.getLanguage("cfgTimeTroubleProcess.timeStationVip"),
        I18n.getLanguage("cfgTimeTroubleProcess.timeWoVip")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetCfgTime);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    List<String> listHeaderRequiredStationVip = Arrays.asList(headerRequiredStationVip);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("cfgTimeTroubleProcessDTO.stt"));
    int countryNameColumn = listHeader
        .indexOf(I18n.getLanguage("cfgTimeTroubleProcess.countryName"));
    int typeNameColumn = listHeader.indexOf(I18n.getLanguage("cfgTimeTroubleProcess.typeName"));
    int subCategoryNameColumn = listHeader
        .indexOf(I18n.getLanguage("cfgTimeTroubleProcess.subCategoryName"));
    int priorityNameColumn = listHeader
        .indexOf(I18n.getLanguage("cfgTimeTroubleProcess.priorityName"));
    int createTtTimeColumn = listHeader
        .indexOf(I18n.getLanguage("cfgTimeTroubleProcess.createTtTime"));
    int processTtTimeColumn = listHeader
        .indexOf(I18n.getLanguage("cfgTimeTroubleProcess.processTtTime"));
    int closeTtTimeColumn = listHeader
        .indexOf(I18n.getLanguage("cfgTimeTroubleProcess.closeTtTime"));
    int processWoTimeColumn = listHeader
        .indexOf(I18n.getLanguage("cfgTimeTroubleProcess.processWoTime"));
    int isCallNameColumn = listHeader.indexOf(I18n.getLanguage("cfgTimeTroubleProcess.isCallName"));
    int timeAboutOverdueColumn = listHeader
        .indexOf(I18n.getLanguage("cfgTimeTroubleProcess.timeAboutOverdue"));
    int cdAudioNameColumn = listHeader
        .indexOf(I18n.getLanguage("cfgTimeTroubleProcess.cdAudioName"));
    int ftAudioNameColumn = listHeader
        .indexOf(I18n.getLanguage("cfgTimeTroubleProcess.ftAudioName"));
    int stationVipColumn = listHeader.indexOf(I18n.getLanguage("cfgTimeTroubleProcess.stationVip"));
    int timeStationVipColumn = listHeader
        .indexOf(I18n.getLanguage("cfgTimeTroubleProcess.timeStationVip"));
    int timeWoVipColumn = listHeader.indexOf(I18n.getLanguage("cfgTimeTroubleProcess.timeWoVip"));

    String firstLeftHeaderTitle = I18n.getLanguage("cfgTimeTroubleProcess.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n
        .getLanguage("cfgTimeTroubleProcess.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n
        .getLanguage("cfgTimeTroubleProcess.export.firstRightHeader");
    String secondRightHeaderTitle = I18n
        .getLanguage("cfgTimeTroubleProcess.export.secondRightHeader");

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
    Row headerFirstTitle = sheetCfgTime.createRow(0);
    Row headerSecondTitle = sheetCfgTime.createRow(1);
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
    sheetCfgTime.addMergedRegion(new CellRangeAddress(0, 0, 1,
        3));
    sheetCfgTime.addMergedRegion(new CellRangeAddress(1, 1, 1,
        3));
    sheetCfgTime.addMergedRegion(new CellRangeAddress(0, 0, sizeheaderTitle - 2,
        sizeheaderTitle + 1));
    sheetCfgTime.addMergedRegion(new CellRangeAddress(1, 1, sizeheaderTitle - 2,
        sizeheaderTitle + 1));

    //Tao tieu de
    Row rowMainTitle = sheetCfgTime.createRow(3);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("cfgTimeTroubleProcess.template.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetCfgTime.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());
    //Tao header
    Row headerRow = sheetCfgTime.createRow(5);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append(" (*)", fontStar);
        }
      }
      for (String checkHeaderRequiredStationVip : listHeaderRequiredStationVip) {
        if (checkHeaderRequiredStationVip.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("\n" + I18n.getLanguage("cfgTimeTroubleProcess.stationVip.required"), fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("cfgTimeTroubleProcess.countryName").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("cfgTimeTroubleProcess.typeName").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetOrther, 2, 0,
        I18n.getLanguage("cfgTimeTroubleProcess.typeCode").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetOrther, 3, 0,
        I18n.getLanguage("cfgTimeTroubleProcess.priorityName").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetOrther, 4, 0,
        I18n.getLanguage("cfgTimeTroubleProcess.isCallName").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetOrther, 5, 0,
        I18n.getLanguage("cfgTimeTroubleProcess.stationVip").toUpperCase(), styles.get("header"));

    int row = 1;
    Datatable dataCountry = catItemRepository
        .getItemMaster(CFG_TIME_TROUBLE_PROCESS_MASTER_CODE.GNOC_COUNTRY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> listCountry = (List<CatItemDTO>) dataCountry.getData();
    for (CatItemDTO dto : listCountry) {
      ewu.createCell(sheetOrther, 0, row++, dto.getItemName(), styles.get("cell"));
    }
    Name countryName = workBook.createName();
    countryName.setNameName("countryName");
    countryName.setRefersToFormula("Orther!$A$2:$A$" + row);
    XSSFDataValidationConstraint countryConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "countryName");
    CellRangeAddressList cellRangeCountry = new CellRangeAddressList(6, (maxRecord + 6),
        countryNameColumn, countryNameColumn);
    XSSFDataValidation dataValidationCountry = (XSSFDataValidation) dvHelper.createValidation(
        countryConstraint, cellRangeCountry);
    dataValidationCountry.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationCountry);

    row = 1;
    for (CatItemDTO dto : lstType) {
      ewu.createCell(sheetOrther, 1, row++, dto.getItemName(), styles.get("cell"));
    }
    row = 1;
    for (CatItemDTO dto : lstType) {
      ewu.createCell(sheetOrther, 2, row++, dto.getItemCode(), styles.get("cell"));
    }
    XSSFDataValidationConstraint typeNameConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "Type");
    CellRangeAddressList cellRangeType = new CellRangeAddressList(
        6, (maxRecord + 6), typeNameColumn, typeNameColumn);
    XSSFDataValidation dataValidationType = (XSSFDataValidation) dvHelper.createValidation(
        typeNameConstraint, cellRangeType);
    dataValidationType.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationType);

    XSSFDataValidationConstraint subCategoryNameConstraint;
    CellRangeAddressList cellRangeSubCategory;
    XSSFDataValidation dataValidationSubCategory;
    for (int i = 7; i <= (maxRecord + 7); i++) {
      subCategoryNameConstraint = new XSSFDataValidationConstraint(
          ValidationType.LIST, "INDIRECT($C$" + i + ")");
      cellRangeSubCategory = new CellRangeAddressList(
          i - 1, i - 1, subCategoryNameColumn, subCategoryNameColumn);
      dataValidationSubCategory = (XSSFDataValidation) dvHelper.createValidation(
          subCategoryNameConstraint, cellRangeSubCategory);
      dataValidationSubCategory.setShowErrorBox(true);
      sheetCfgTime.addValidationData(dataValidationSubCategory);
    }

    row = 1;
    Datatable dataPriority = catItemRepository
        .getItemMaster(CFG_TIME_TROUBLE_PROCESS_MASTER_CODE.TT_PRIORITY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> listPriority = (List<CatItemDTO>) dataPriority.getData();
    for (CatItemDTO dto : listPriority) {
      ewu.createCell(sheetOrther, 3, row++, dto.getItemName(), styles.get("cell"));
    }
    Name priorityName = workBook.createName();
    priorityName.setNameName("priorityName");
    priorityName.setRefersToFormula("Orther!$D$2:$D$" + row);
    XSSFDataValidationConstraint priorityConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "priorityName");
    CellRangeAddressList cellRangePriority = new CellRangeAddressList(6, (maxRecord + 6),
        priorityNameColumn, priorityNameColumn);
    XSSFDataValidation dataValidationPriority = (XSSFDataValidation) dvHelper.createValidation(
        priorityConstraint, cellRangePriority);
    dataValidationPriority.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationPriority);

    XSSFDataValidationConstraint createTtTimeConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "10000.00");
    CellRangeAddressList cellRangeCreateTtTime = new CellRangeAddressList(6, (maxRecord + 6),
        createTtTimeColumn, createTtTimeColumn);
    XSSFDataValidation dataValidationCreateTtTime = (XSSFDataValidation) dvHelper.createValidation(
        createTtTimeConstraint, cellRangeCreateTtTime);
    dataValidationCreateTtTime
        .createErrorBox("Error input", I18n.getLanguage("cfgTimeTroubleProcess.err.number"));
    dataValidationCreateTtTime.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationCreateTtTime);

    XSSFDataValidationConstraint processTtTimeConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "10000.00");
    CellRangeAddressList cellRangeProcessTtTime = new CellRangeAddressList(6, (maxRecord + 6),
        processTtTimeColumn, processTtTimeColumn);
    XSSFDataValidation dataValidationProcessTtTime = (XSSFDataValidation) dvHelper.createValidation(
        processTtTimeConstraint, cellRangeProcessTtTime);
    dataValidationProcessTtTime
        .createErrorBox("Error input", I18n.getLanguage("cfgTimeTroubleProcess.err.number"));
    dataValidationProcessTtTime.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationProcessTtTime);

    XSSFDataValidationConstraint closeTtTimeConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "10000.00");
    CellRangeAddressList cellRangeCloseTtTime = new CellRangeAddressList(6, (maxRecord + 6),
        closeTtTimeColumn, closeTtTimeColumn);
    XSSFDataValidation dataValidationCloseTtTime = (XSSFDataValidation) dvHelper.createValidation(
        closeTtTimeConstraint, cellRangeCloseTtTime);
    dataValidationCloseTtTime
        .createErrorBox("Error input", I18n.getLanguage("cfgTimeTroubleProcess.err.number"));
    dataValidationCloseTtTime.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationCloseTtTime);

    XSSFDataValidationConstraint processWoTimeConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "10000.00");
    CellRangeAddressList cellRangeProcessWoTime = new CellRangeAddressList(6, (maxRecord + 6),
        processWoTimeColumn, processWoTimeColumn);
    XSSFDataValidation dataValidationProcessWoTime = (XSSFDataValidation) dvHelper.createValidation(
        processWoTimeConstraint, cellRangeProcessWoTime);
    dataValidationProcessWoTime
        .createErrorBox("Error input", I18n.getLanguage("cfgTimeTroubleProcess.err.number"));
    dataValidationProcessWoTime.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationProcessWoTime);

    row = 1;
    ewu.createCell(sheetOrther, 4, row++, I18n.getLanguage("cfgTimeTroubleProcessDTO.isCallName.1"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 4, row++, I18n.getLanguage("cfgTimeTroubleProcessDTO.isCallName.0"),
        styles.get("cell"));
    Name isCallName = workBook.createName();
    isCallName.setNameName("isCallName");
    isCallName.setRefersToFormula("Orther!$E$2:$E$" + row);
    XSSFDataValidationConstraint isCallConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "isCallName");
    CellRangeAddressList cellRangeIsCall = new CellRangeAddressList(6, (maxRecord + 6),
        isCallNameColumn, isCallNameColumn);
    XSSFDataValidation dataValidationIsCall = (XSSFDataValidation) dvHelper.createValidation(
        isCallConstraint, cellRangeIsCall);
    dataValidationIsCall.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationIsCall);

    XSSFDataValidationConstraint timeAboutOverdueConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "10000.00");
    CellRangeAddressList cellRangeTimeAboutOverdue = new CellRangeAddressList(6, (maxRecord + 6),
        timeAboutOverdueColumn, timeAboutOverdueColumn);
    XSSFDataValidation dataValidationTimeAboutOverdue = (XSSFDataValidation) dvHelper
        .createValidation(
            timeAboutOverdueConstraint, cellRangeTimeAboutOverdue);
    dataValidationTimeAboutOverdue
        .createErrorBox("Error input", I18n.getLanguage("cfgTimeTroubleProcess.err.number"));
    dataValidationTimeAboutOverdue.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationTimeAboutOverdue);

    row = 1;
    ewu.createCell(sheetOrther, 5, row++, I18n.getLanguage("cfgTimeTroubleProcessDTO.stationVip.1"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 5, row++, I18n.getLanguage("cfgTimeTroubleProcessDTO.stationVip.0"),
        styles.get("cell"));
    Name stationVipName = workBook.createName();
    stationVipName.setNameName("stationVipName");
    stationVipName.setRefersToFormula("Orther!$F$2:$F$" + row);
    XSSFDataValidationConstraint stationVipConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "stationVipName");
    CellRangeAddressList cellRangeStationVip = new CellRangeAddressList(6, (maxRecord + 6),
        stationVipColumn, stationVipColumn);
    XSSFDataValidation dataValidationStationVip = (XSSFDataValidation) dvHelper.createValidation(
        stationVipConstraint, cellRangeStationVip);
    dataValidationStationVip.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationStationVip);

    XSSFDataValidationConstraint timeStationVipConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "10000.00");
    CellRangeAddressList cellRangeTimeStationVip = new CellRangeAddressList(6, (maxRecord + 6),
        timeStationVipColumn, timeStationVipColumn);
    XSSFDataValidation dataValidationTimeStationVip = (XSSFDataValidation) dvHelper
        .createValidation(
            timeStationVipConstraint, cellRangeTimeStationVip);
    dataValidationTimeStationVip
        .createErrorBox("Error input", I18n.getLanguage("cfgTimeTroubleProcess.err.number"));
    dataValidationTimeStationVip.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationTimeStationVip);

    XSSFDataValidationConstraint timeWoVipConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "10000.00");
    CellRangeAddressList cellRangeTimeWoVip = new CellRangeAddressList(6, (maxRecord + 6),
        timeWoVipColumn, timeWoVipColumn);
    XSSFDataValidation dataValidationTimeWoVip = (XSSFDataValidation) dvHelper.createValidation(
        timeWoVipConstraint, cellRangeTimeWoVip);
    dataValidationTimeWoVip
        .createErrorBox("Error input", I18n.getLanguage("cfgTimeTroubleProcess.err.number"));
    dataValidationTimeWoVip.setShowErrorBox(true);
    sheetCfgTime.addValidationData(dataValidationTimeWoVip);

    for (int i = 0; i <= 6; i++) {
      sheetOrther.autoSizeColumn(i);
    }

    sheetCfgTime.setColumnWidth(sttColumn, 2000);
    sheetCfgTime.setColumnWidth(countryNameColumn, 4000);
    sheetCfgTime.setColumnWidth(typeNameColumn, 7000);
    sheetCfgTime.setColumnWidth(subCategoryNameColumn, 12000);
    sheetCfgTime.setColumnWidth(priorityNameColumn, 7000);
    sheetCfgTime.setColumnWidth(createTtTimeColumn, 7000);
    sheetCfgTime.setColumnWidth(processTtTimeColumn, 7000);
    sheetCfgTime.setColumnWidth(closeTtTimeColumn, 7000);
    sheetCfgTime.setColumnWidth(processWoTimeColumn, 7000);
    sheetCfgTime.setColumnWidth(isCallNameColumn, 3000);
    sheetCfgTime.setColumnWidth(timeAboutOverdueColumn, 7000);
    sheetCfgTime.setColumnWidth(cdAudioNameColumn, 8000);
    sheetCfgTime.setColumnWidth(ftAudioNameColumn, 8000);
    sheetCfgTime.setColumnWidth(stationVipColumn, 3000);
    sheetCfgTime.setColumnWidth(timeStationVipColumn, 10000);
    sheetCfgTime.setColumnWidth(timeWoVipColumn, 10000);

    sheetOrther.setSelected(false);
    workBook.setSheetName(0, I18n.getLanguage("cfgTimeTroubleProcess.import.title"));

    String fileResult = tempFolder + File.separator;
    String fileName = "TEMPLATE_IMPORT" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public List<CatItemDTO> getListSubCategory(Long typeId) {
    log.debug("Request to getListSubCategory: {}", typeId);
    return cfgTimeTroubleProcessRepository.getListSubCategory(typeId);
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<CfgTimeTroubleProcessDTO> listDto;
    List<CfgTimeTroubleProcessExportDTO> listExportDto;
    Map<String, String> mapExportDTO;

    String[] header = new String[]{"countryName", "typeName", "subCategoryName", "priorityName",
        "createTtTime", "processTtTime", "closeTtTime", "processWoTime", "isCallName",
        "timeAboutOverdue", "cdAudioName", "ftAudioName", "stationVip", "timeStationVip",
        "timeWoVip", "resultImport"};

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
            0, 15, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 6,
            0, 15, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        listExportDto = new ArrayList<>();
        mapExportDTO = new HashMap<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          setMapCountry();
          setMapType();
          setMapSubCategory();
          setMapPriority();
          for (Object[] obj : lstData) {
            CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO = new CfgTimeTroubleProcessDTO();
            CfgTimeTroubleProcessExportDTO exportDTO = new CfgTimeTroubleProcessExportDTO();
            if (obj[1] != null) {
              exportDTO.setCountryName(obj[1].toString().trim());
              for (Map.Entry<String, String> item : mapCountry.entrySet()) {
                if (exportDTO.getCountryName().equals(item.getValue())) {
                  cfgTimeTroubleProcessDTO.setCountry(item.getKey());
                }
              }
            } else {
              exportDTO.setCountryName(null);
            }
            if (obj[2] != null) {
              String typeName = obj[2].toString().trim();
              String c0 = "_";
              char c1 = typeName.charAt(1);
              if (c0.equals(String.valueOf(typeName.charAt(0))) && Character.isDigit(c1)) {
                typeName = typeName.substring(1);
              }
              exportDTO.setTypeName(typeName);
              for (Map.Entry<Long, String> item : mapType.entrySet()) {
                if (exportDTO.getTypeName().equals(item.getValue())) {
                  cfgTimeTroubleProcessDTO.setTypeId(item.getKey());
                }
              }
            } else {
              exportDTO.setTypeName(null);
            }
            if (obj[3] != null) {
              exportDTO.setSubCategoryName(obj[3].toString().trim());
              for (Map.Entry<Long, String> item : mapSubCategory.entrySet()) {
                if (exportDTO.getSubCategoryName().equals(item.getValue())) {
                  cfgTimeTroubleProcessDTO.setSubCategoryId(item.getKey());
                }
              }
            } else {
              exportDTO.setSubCategoryName(null);
            }
            if (obj[4] != null) {
              exportDTO.setPriorityName(obj[4].toString().trim());
              for (Map.Entry<Long, String> item : mapPriority.entrySet()) {
                if (exportDTO.getPriorityName().equals(item.getValue())) {
                  cfgTimeTroubleProcessDTO.setPriorityId(item.getKey());
                }
              }
            } else {
              exportDTO.setPriorityName(null);
            }
            if (obj[5] != null) {
              exportDTO.setCreateTtTime(Double.valueOf(obj[5].toString().trim()));
              cfgTimeTroubleProcessDTO.setCreateTtTime(exportDTO.getCreateTtTime());
            } else {
              exportDTO.setCreateTtTime(null);
            }
            if (obj[6] != null) {
              exportDTO.setProcessTtTime(Double.valueOf(obj[6].toString().trim()));
              cfgTimeTroubleProcessDTO.setProcessTtTime(exportDTO.getProcessTtTime());
            } else {
              exportDTO.setProcessTtTime(null);
            }
            if (obj[7] != null) {
              exportDTO.setCloseTtTime(Double.valueOf(obj[7].toString().trim()));
              cfgTimeTroubleProcessDTO.setCloseTtTime(exportDTO.getCloseTtTime());
            } else {
              exportDTO.setCloseTtTime(null);
            }
            if (obj[8] != null) {
              exportDTO.setProcessWoTime(Double.valueOf(obj[8].toString().trim()));
              cfgTimeTroubleProcessDTO.setProcessWoTime(exportDTO.getProcessWoTime());
            } else {
              exportDTO.setProcessWoTime(null);
            }
            if (obj[9] != null) {
              exportDTO.setIsCallName(obj[9].toString().trim());
              if (I18n.getLanguage("cfgTimeTroubleProcessDTO.isCallName.1")
                  .equals(exportDTO.getIsCallName())) {
                cfgTimeTroubleProcessDTO.setIsCall(1L);
              } else {
                cfgTimeTroubleProcessDTO.setIsCall(0L);
              }
            } else {
              exportDTO.setIsCallName(null);
              cfgTimeTroubleProcessDTO.setIsCall(0L);
            }
            if (obj[10] != null) {
              exportDTO.setTimeAboutOverdue(Double.valueOf(obj[10].toString().trim()));
              cfgTimeTroubleProcessDTO.setTimeAboutOverdue(exportDTO.getTimeAboutOverdue());
            } else {
              exportDTO.setTimeAboutOverdue(null);
            }
            if (obj[11] != null) {
              exportDTO.setCdAudioName(obj[11].toString().trim());
              cfgTimeTroubleProcessDTO.setCdAudioName(exportDTO.getCdAudioName());
            } else {
              exportDTO.setCdAudioName(null);
            }
            if (obj[12] != null) {
              exportDTO.setFtAudioName(obj[12].toString().trim());
              cfgTimeTroubleProcessDTO.setFtAudioName(exportDTO.getFtAudioName());
            } else {
              exportDTO.setFtAudioName(null);
            }
            if (obj[13] != null) {
              exportDTO.setStationVip(obj[13].toString().trim());
              if (I18n.getLanguage("cfgTimeTroubleProcessDTO.stationVip.1")
                  .equals(exportDTO.getStationVip())) {
                cfgTimeTroubleProcessDTO.setIsStationVip(1L);
              } else {
                cfgTimeTroubleProcessDTO.setIsStationVip(0L);
              }
            } else {
              exportDTO.setStationVip(null);
            }
            if (obj[14] != null) {
              exportDTO.setTimeStationVip(Double.valueOf(obj[14].toString().trim()));
              cfgTimeTroubleProcessDTO.setTimeStationVip(exportDTO.getTimeStationVip());
            } else {
              exportDTO.setTimeStationVip(null);
            }
            if (obj[15] != null) {
              exportDTO.setTimeWoVip(Double.valueOf(obj[15].toString().trim()));
              cfgTimeTroubleProcessDTO.setTimeWoVip(exportDTO.getTimeWoVip());
            } else {
              exportDTO.setTimeWoVip(null);
            }
            CfgTimeTroubleProcessExportDTO tempExportDTO = validateImportInfo(exportDTO,
                cfgTimeTroubleProcessDTO, mapExportDTO);
            if (tempExportDTO.getResultImport() == null) {
              tempExportDTO
                  .setResultImport(I18n.getLanguage("cfgTimeTroubleProcess.result.import.ok"));
              listExportDto.add(tempExportDTO);
              String countryName = tempExportDTO.getCountryName();
              String typeName = tempExportDTO.getTypeName();
              String subCategoryName = tempExportDTO.getSubCategoryName();
              String priorityName = tempExportDTO.getPriorityName();
              mapExportDTO.put(countryName + "_" + typeName + "_" + subCategoryName + "_"
                  + priorityName, String.valueOf(value));
              value++;
              listDto.add(cfgTimeTroubleProcessDTO);
            } else {
              listExportDto.add(tempExportDTO);
              index++;
            }
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              for (CfgTimeTroubleProcessDTO dto : listDto) {
                resultInSideDto = insertCfgTimeTroubleProcess(dto);
              }
            }
          } else {
            File fileExport = handleFileExport(listExportDto, header, null,
                Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFileExport(listExportDto, header, null,
              Constants.RESULT_IMPORT);
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

  private CfgTimeTroubleProcessExportDTO validateImportInfo(
      CfgTimeTroubleProcessExportDTO exportDTO, CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO,
      Map<String, String> mapExportDTO) {
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(exportDTO.getCountryName())) {
      resultImport = resultImport.concat(I18n.getLanguage("cfgTimeTroubleProcess.err.countryName"));
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getTypeName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.typeName"));
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getSubCategoryName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.subCategoryName"));
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getPriorityName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.priorityName"));
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getCreateTtTime())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.createTtTime"));
    } else {
      if (exportDTO.getCreateTtTime() >= 10000L) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.max.createTtTime"));
      }
      if (Double.valueOf(exportDTO.getCreateTtTime()) <= 0L) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.min.createTtTime"));
      }
      if (!fomatTime(String.valueOf(exportDTO.getCreateTtTime()))) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.format.createTtTime"));
      }
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getProcessTtTime())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.processTtTime"));
    } else {
      if (exportDTO.getProcessTtTime() >= 10000L) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.max.processTtTime"));
      }
      if (Double.valueOf(exportDTO.getProcessTtTime()) <= 0L) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.min.processTtTime"));
      }
      if (!fomatTime(String.valueOf(exportDTO.getProcessTtTime()))) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.format.processTtTime"));
      }
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getCloseTtTime())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.closeTtTime"));
    } else {
      if (exportDTO.getCloseTtTime() >= 10000L) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.max.closeTtTime"));
      }
      if (Double.valueOf(exportDTO.getCloseTtTime()) <= 0L) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.min.closeTtTime"));
      }
      if (!fomatTime(String.valueOf(exportDTO.getCloseTtTime()))) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.format.closeTtTime"));
      }
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getProcessWoTime())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.processWoTime"));
    } else {
      if (exportDTO.getProcessWoTime() >= 10000L) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.max.processWoTime"));
      }
      if (Double.valueOf(exportDTO.getProcessWoTime()) <= 0L) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.min.processWoTime"));
      }
      if (!fomatTime(String.valueOf(exportDTO.getProcessWoTime()))) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.format.processWoTime"));
      }
    }
    if (!StringUtils.isStringNullOrEmpty(exportDTO.getTimeAboutOverdue())) {
      if (exportDTO.getTimeAboutOverdue() >= 10000L) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.max.timeAboutOverdue"));
      }
      if (Double.valueOf(exportDTO.getTimeAboutOverdue()) <= 0L) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.min.timeAboutOverdue"));
      }
      if (!fomatTime(String.valueOf(exportDTO.getTimeAboutOverdue()))) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.format.timeAboutOverdue"));
      }
    }
    if (I18n.getLanguage("cfgTimeTroubleProcessDTO.stationVip.1")
        .equals(exportDTO.getStationVip())) {
      if (StringUtils.isStringNullOrEmpty(exportDTO.getTimeStationVip())) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.timeStationVip"));
      } else {
        if (exportDTO.getTimeStationVip() >= 10000L) {
          resultImport = checkResultImport(resultImport)
              .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.max.timeStationVip"));
        }
        if (Double.valueOf(exportDTO.getTimeStationVip()) <= 0L) {
          resultImport = checkResultImport(resultImport)
              .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.min.timeStationVip"));
        }
        if (!fomatTime(String.valueOf(exportDTO.getTimeStationVip()))) {
          resultImport = checkResultImport(resultImport)
              .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.format.timeStationVip"));
        }
      }
      if (StringUtils.isStringNullOrEmpty(exportDTO.getTimeWoVip())) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.timeWoVip"));
      } else {
        if (exportDTO.getTimeWoVip() >= 10000L) {
          resultImport = checkResultImport(resultImport)
              .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.max.timeWoVip"));
        }
        if (Double.valueOf(exportDTO.getTimeWoVip()) <= 0L) {
          resultImport = checkResultImport(resultImport)
              .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.min.timeWoVip"));
        }
        if (!fomatTime(String.valueOf(exportDTO.getTimeWoVip()))) {
          resultImport = checkResultImport(resultImport)
              .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.format.timeWoVip"));
        }
      }
    } else {
      if (!StringUtils.isStringNullOrEmpty(exportDTO.getTimeStationVip())) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.timeStationVip.validate"));
      }
      if (!StringUtils.isStringNullOrEmpty(exportDTO.getTimeWoVip())) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.timeWoVip.validate"));
      }
    }
    if (StringUtils.isNotNullOrEmpty(exportDTO.getCdAudioName())) {
      if (exportDTO.getCdAudioName().length() > 256) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.cdAudioName.length"));
      }
    }
    if (StringUtils.isNotNullOrEmpty(exportDTO.getFtAudioName())) {
      if (exportDTO.getFtAudioName().length() > 256) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.ftAudioName.length"));
      }
    }
    if (StringUtils.isStringNullOrEmpty(resultImport)) {
      List<CatItemDTO> listSubCategory = getListSubCategory(cfgTimeTroubleProcessDTO.getTypeId());
      if (listSubCategory == null || listSubCategory.isEmpty()) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgTimeTroubleProcess.err.subCategoryName.validate"));
      }
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      exportDTO.setResultImport(resultImport);
      return exportDTO;
    }
    if (StringUtils.isNotNullOrEmpty(cfgTimeTroubleProcessDTO.getCountry()) &&
        (cfgTimeTroubleProcessDTO.getTypeId() != null) &&
        (cfgTimeTroubleProcessDTO.getSubCategoryId() != null) &&
        (cfgTimeTroubleProcessDTO.getPriorityId() != null)) {
      CfgTimeTroubleProcessDTO dtoTmp = cfgTimeTroubleProcessRepository
          .checkCfgTimeTroubleProcessExit(cfgTimeTroubleProcessDTO);
      if (dtoTmp != null) {
        exportDTO.setResultImport(I18n.getLanguage("cfgTimeTroubleProcess.err.dup-code"));
        return exportDTO;
      }
    }
    String validateDuplicate = validateDuplicate(exportDTO, mapExportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      exportDTO.setResultImport(validateDuplicate);
      return exportDTO;
    }
    return exportDTO;
  }

  private String validateDuplicate(CfgTimeTroubleProcessExportDTO exportDTO,
      Map<String, String> mapExportDTO) {
    String countryName = exportDTO.getCountryName();
    String typeName = exportDTO.getTypeName();
    String subCategoryName = exportDTO.getSubCategoryName();
    String priorityName = exportDTO.getPriorityName();
    String key = countryName + "_" + typeName + "_" + subCategoryName + "_" + priorityName;
    String value = mapExportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("cfgTimeTroubleProcess.err.dup-code-in-file")
          .replaceAll("0", value);
    }
    return null;
  }

  private String checkResultImport(String resultImport) {
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      resultImport = resultImport.concat("; \n");
    }
    return resultImport;
  }

  private boolean fomatTime(String time) {
    int i = time.lastIndexOf('.');
    if (i == -1 || time.substring(i + 2).length() <= 1) {
      return true;
    }
    return false;
  }

  public void setMapCountry() {
    Datatable dataCountry = catItemRepository
        .getItemMaster(CFG_TIME_TROUBLE_PROCESS_MASTER_CODE.GNOC_COUNTRY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataCountry.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapCountry.put(dto.getItemCode(), dto.getItemName());
      }
    }
  }

  public void setMapType() {
    Datatable dataType = catItemRepository
        .getItemMaster(CFG_TIME_TROUBLE_PROCESS_MASTER_CODE.PT_TYPE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataType.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapType.put(dto.getItemId(), dto.getItemCode());
      }
    }
  }

  public void setMapSubCategory() {
    List<CatItemDTO> list = getListSubCategory(null);
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapSubCategory.put(dto.getItemId(), dto.getItemName());
      }
    }
  }

  public void setMapPriority() {
    Datatable dataPriority = catItemRepository
        .getItemMaster(CFG_TIME_TROUBLE_PROCESS_MASTER_CODE.TT_PRIORITY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataPriority.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapPriority.put(dto.getItemId(), dto.getItemName());
      }
    }
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 16) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcessDTO.stt"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.countryName") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.typeName") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.subCategoryName") + " (*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.priorityName") + " (*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.createTtTime") + " (*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.processTtTime") + " (*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.closeTtTime") + " (*)")
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.processWoTime") + " (*)")
        .equalsIgnoreCase(obj[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.isCallName"))
        .equalsIgnoreCase(obj[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.timeAboutOverdue"))
        .equalsIgnoreCase(obj[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.cdAudioName"))
        .equalsIgnoreCase(obj[11].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.ftAudioName"))
        .equalsIgnoreCase(obj[12].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.stationVip"))
        .equalsIgnoreCase(obj[13].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.timeStationVip") + "\n" +
        I18n.getLanguage("cfgTimeTroubleProcess.stationVip.required"))
        .equalsIgnoreCase(obj[14].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgTimeTroubleProcess.timeWoVip") + "\n" +
        I18n.getLanguage("cfgTimeTroubleProcess.stationVip.required"))
        .equalsIgnoreCase(obj[15].toString().trim())) {
      return false;
    }
    return true;
  }

  private File handleFileExport(List<CfgTimeTroubleProcessExportDTO> listExport,
      String[] columnExport, String date, String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName;
    String title;
    String fileNameOut;
    int startRow = 7;
    if (Constants.RESULT_IMPORT.equals(code)) {
      sheetName = I18n.getLanguage("cfgTimeTroubleProcess.import.sheetname");
      title = I18n.getLanguage("cfgTimeTroubleProcess.import.title");
      fileNameOut = I18n.getLanguage("cfgTimeTroubleProcess.import.fileNameOut");
    } else {
      sheetName = I18n.getLanguage("cfgTimeTroubleProcess.export.sheetname");
      title = I18n.getLanguage("cfgTimeTroubleProcess.export.title");
      fileNameOut = I18n.getLanguage("cfgTimeTroubleProcess.export.fileNameOut");
    }
    String subTitle;
    if (StringUtils.isNotNullOrEmpty(date)) {
      subTitle = I18n
          .getLanguage("cfgTimeTroubleProcess.export.eportDate", date);
    } else {
      subTitle = "";
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExport,
        sheetName,
        title,
        subTitle,
        startRow,
        3,
        6,
        true,
        "language.cfgTimeTroubleProcess",
        lstHeaderSheet1,
        fieldSplit,
        "",
        I18n.getLanguage("cfgTimeTroubleProcess.export.firstLeftHeader"),
        I18n.getLanguage("cfgTimeTroubleProcess.export.secondLeftHeader"),
        I18n.getLanguage("cfgTimeTroubleProcess.export.firstRightHeader"),
        I18n.getLanguage("cfgTimeTroubleProcess.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("cfgTimeTroubleProcessDTO.stt"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false,
          0, 0, new String[]{}, null, "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = CfgTimeTroubleProcessDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        List<String> rmKeys = Arrays.asList("priorityName", "typeName", "subCategoryName", "countryName", "isCallName");
        for (String rmKey : rmKeys) {
          keys.remove(rmKey);
        }
        return keys;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
