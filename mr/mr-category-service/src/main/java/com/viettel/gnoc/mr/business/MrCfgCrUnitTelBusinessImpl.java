package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgCrUnitTelDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.mr.repository.MrCfgCrUnitTelRepository;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import java.io.File;
import java.io.IOException;
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

@Service
@Transactional
@Slf4j
public class MrCfgCrUnitTelBusinessImpl implements MrCfgCrUnitTelBusiness {

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  MrCfgCrUnitTelRepository mrCfgCrUnitTelRepository;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  MrDeviceRepository mrDeviceRepository;

  Map<Long, String> mapMarket = new HashMap<>();
  Map<Long, UnitDTO> mapUnit = new HashMap<>();
  Map<String, CatItemDTO> mapArray = new HashMap<>();
  Map<Long, List<String>> mapRegion = new HashMap<>();
  Map<String, List<String>> mapNetwork = new HashMap<>();
  Map<String, List<String>> mapDevice = new HashMap<>();

  private int maxRecord = 500;

  @Override
  public Datatable getListDataMrCfgCrUnitTelSearchWeb(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    log.debug("Request to getListDataMrCfgCrUnitTelSearchWeb: {}", mrCfgCrUnitTelDTO);
    Datatable datatable = mrCfgCrUnitTelRepository
        .getListDataMrCfgCrUnitTelSearchWeb(mrCfgCrUnitTelDTO);
    if (datatable != null) {
      List<MrCfgCrUnitTelDTO> list = (List<MrCfgCrUnitTelDTO>) datatable.getData();
      if (list != null && list.size() > 0) {
        setMapMarket();
        setMapUnit();
        for (MrCfgCrUnitTelDTO dto : list) {
          setDetailValue(dto);
        }
      }
    }
    return datatable;
  }

  @Override
  public ResultInSideDto insertMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    log.debug("Request to insertMrCfgCrUnitTel: {}", mrCfgCrUnitTelDTO);
    mrCfgCrUnitTelDTO.setCreatedTime(new Date());
    return mrCfgCrUnitTelRepository.insertMrCfgCrUnitTel(mrCfgCrUnitTelDTO);
  }

  @Override
  public ResultInSideDto updateMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    log.debug("Request to updateMrCfgCrUnitTel: {}", mrCfgCrUnitTelDTO);
    mrCfgCrUnitTelDTO.setUpdatedTime(new Date());
    return mrCfgCrUnitTelRepository.updateMrCfgCrUnitTel(mrCfgCrUnitTelDTO);
  }

  @Override
  public MrCfgCrUnitTelDTO findMrCfgCrUnitTelById(Long cfgId) {
    log.debug("Request to findMrCfgCrUnitTelById: {}", cfgId);
    MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO = mrCfgCrUnitTelRepository.findMrCfgCrUnitTelSoftWeb(cfgId);
    setMapMarket();
    setMapUnit();
    if (mrCfgCrUnitTelDTO != null) {
      setDetailValue(mrCfgCrUnitTelDTO);
    }
    return mrCfgCrUnitTelDTO;
  }

  @Override
  public ResultInSideDto deleteMrCfgCrUnitTel(Long cfgId) {
    log.debug("Request to deleteMrCfgCrUnitTel: {}", cfgId);
    return mrCfgCrUnitTelRepository.deleteMrCfgCrUnitTel(cfgId);
  }

  @Override
  public File exportDataMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) throws Exception {
    log.debug("Request to exportDataMrCfgCrUnitTel: {}", mrCfgCrUnitTelDTO);
    List<MrCfgCrUnitTelDTO> list = mrCfgCrUnitTelRepository
        .getListMrCfgCrUnitTelExport(mrCfgCrUnitTelDTO);
    if (list != null && list.size() > 0) {
      setMapMarket();
      setMapUnit();
      for (MrCfgCrUnitTelDTO dto : list) {
        setDetailValue(dto);
      }
    }
    String[] header = new String[]{"marketName", "region", "arrayName", "networkType",
        "deviceType", "implementUnitName", "checkingUnitName"};
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(date),
        "EXPORT_MR_CFG_CR_UNIT_TEL");
  }

  @Override
  public File getTemplateImport() throws IOException {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT_NOVALUE.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");
    XSSFSheet sheetRegion = workBook
        .createSheet(I18n.getLanguage("mrCfgCrUnitTel.sheetName.region"));
    XSSFSheet sheetNetworkType = workBook
        .createSheet(I18n.getLanguage("mrCfgCrUnitTel.sheetName.networkType"));
    XSSFSheet sheetDeviceType = workBook
        .createSheet(I18n.getLanguage("mrCfgCrUnitTel.sheetName.deviceType"));
    XSSFSheet sheetUnit = workBook.createSheet(I18n.getLanguage("mrCfgCrUnitTel.sheetName.unit"));

    String[] header = new String[]{
        I18n.getLanguage("mrCfgCrUnitTel.STT"),
        I18n.getLanguage("mrCfgCrUnitTel.marketName"),
        I18n.getLanguage("mrCfgCrUnitTel.region"),
        I18n.getLanguage("mrCfgCrUnitTel.arrayName"),
        I18n.getLanguage("mrCfgCrUnitTel.networkType"),
        I18n.getLanguage("mrCfgCrUnitTel.deviceType"),
        I18n.getLanguage("mrCfgCrUnitTel.implementUnitId"),
        I18n.getLanguage("mrCfgCrUnitTel.checkingUnitId")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("mrCfgCrUnitTel.marketName"),
        I18n.getLanguage("mrCfgCrUnitTel.region"),
        I18n.getLanguage("mrCfgCrUnitTel.arrayName"),
        I18n.getLanguage("mrCfgCrUnitTel.networkType"),
        I18n.getLanguage("mrCfgCrUnitTel.deviceType"),
        I18n.getLanguage("mrCfgCrUnitTel.implementUnitId"),
        I18n.getLanguage("mrCfgCrUnitTel.checkingUnitId")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("mrCfgCrUnitTel.STT"));
    int marketColumn = listHeader.indexOf(I18n.getLanguage("mrCfgCrUnitTel.marketName"));
    int regionColumn = listHeader.indexOf(I18n.getLanguage("mrCfgCrUnitTel.region"));
    int arrayColumn = listHeader.indexOf(I18n.getLanguage("mrCfgCrUnitTel.arrayName"));
    int networkTypeColumn = listHeader.indexOf(I18n.getLanguage("mrCfgCrUnitTel.networkType"));
    int deviceTypeColumn = listHeader.indexOf(I18n.getLanguage("mrCfgCrUnitTel.deviceType"));
    int implementUnitColumn = listHeader
        .indexOf(I18n.getLanguage("mrCfgCrUnitTel.implementUnitId"));
    int checkingUnitColumn = listHeader
        .indexOf(I18n.getLanguage("mrCfgCrUnitTel.checkingUnitId"));

    String firstLeftHeaderTitle = I18n.getLanguage("mrCfgCrUnitTel.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("mrCfgCrUnitTel.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("mrCfgCrUnitTel.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("mrCfgCrUnitTel.export.secondRightHeader");

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
    mainCellTitle.setCellValue(I18n.getLanguage("mrCfgCrUnitTel.template.title"));
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
    List<ItemDataCRInside> listMarket = catLocationRepository
        .getListLocationByLevelCBB(null, 1L, null);
    if (listMarket != null && listMarket.size() > 0) {
      for (ItemDataCRInside dto : listMarket) {
        ewu.createCell(sheetOrther, 0, row++, dto.getDisplayStr(), styles.get("cell"));
      }
    }
    Name marketName = workBook.createName();
    marketName.setNameName("marketName");
    marketName.setRefersToFormula("Orther!$A$2:$A$" + row);
    XSSFDataValidationConstraint marketConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "marketName");
    CellRangeAddressList cellRangeMarket = new CellRangeAddressList(6, (maxRecord + 6),
        marketColumn, marketColumn);
    XSSFDataValidation dataValidationMarket = (XSSFDataValidation) dvHelper
        .createValidation(marketConstraint, cellRangeMarket);
    dataValidationMarket.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationMarket);

    row = 1;
    Datatable dataArray = catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> listArray = (List<CatItemDTO>) dataArray.getData();
    if (listArray != null && listArray.size() > 0) {
      for (CatItemDTO dto : listArray) {
        ewu.createCell(sheetOrther, 1, row++, dto.getItemName(), styles.get("cell"));
      }
    }
    Name arrayName = workBook.createName();
    arrayName.setNameName("arrayName");
    arrayName.setRefersToFormula("Orther!$B$2:$B$" + row);
    XSSFDataValidationConstraint arrayConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "arrayName");
    CellRangeAddressList cellRangeArray = new CellRangeAddressList(6, (maxRecord + 6),
        arrayColumn, arrayColumn);
    XSSFDataValidation dataValidationArray = (XSSFDataValidation) dvHelper
        .createValidation(arrayConstraint, cellRangeArray);
    dataValidationArray.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationArray);

    List<UnitDTO> listUnit = unitRepository.getListUnit(null);
    row = 1;
    ewu.createCell(sheetUnit, 0, 0, I18n.getLanguage("mrCfgCrUnitTel.unitId").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetUnit, 1, 0, I18n.getLanguage("mrCfgCrUnitTel.unitCode").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetUnit, 2, 0, I18n.getLanguage("mrCfgCrUnitTel.unitName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetUnit, 3, 0, I18n.getLanguage("mrCfgCrUnitTel.unitParentId").toUpperCase(),
        styles.get("header"));
    for (UnitDTO dto : listUnit) {
      ewu.createCell(sheetUnit, 0, row, String.valueOf(dto.getUnitId()), styles.get("cell"));
      ewu.createCell(sheetUnit, 1, row, dto.getUnitCode(), styles.get("cell"));
      ewu.createCell(sheetUnit, 2, row, dto.getUnitTrueName(), styles.get("cell"));
      ewu.createCell(sheetUnit, 3, row++, String.valueOf(dto.getParentUnitId()),
          styles.get("cell"));
    }
    sheetUnit.setColumnWidth(0, 3000);
    sheetUnit.setColumnWidth(1, 7000);
    sheetUnit.setColumnWidth(2, 10000);
    sheetUnit.setColumnWidth(3, 4000);
    sheetUnit.setSelected(false);

    row = 1;
    ewu.createCell(sheetRegion, 0, 0,
        I18n.getLanguage("mrCfgCrUnitTel.marketName").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetRegion, 1, 0,
        I18n.getLanguage("mrCfgCrUnitTel.region").toUpperCase(), styles.get("header"));
    if (listMarket != null && listMarket.size() > 0) {
      for (ItemDataCRInside dto : listMarket) {
        List<String> listRegion = mrDeviceRepository
            .getListRegionSoftByMarketCode(String.valueOf(dto.getValueStr()));
        if (listRegion != null && listRegion.size() > 0) {
          for (String region : listRegion) {
            ewu.createCell(sheetRegion, 0, row, dto.getDisplayStr(), styles.get("cell"));
            ewu.createCell(sheetRegion, 1, row++, region, styles.get("cell"));
          }
        }
      }
    }
    sheetRegion.autoSizeColumn(0);
    sheetRegion.autoSizeColumn(1);
    sheetRegion.setSelected(false);

    row = 1;
    int rowDe = 1;
    ewu.createCell(sheetNetworkType, 0, 0,
        I18n.getLanguage("mrCfgCrUnitTel.arrayName").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetNetworkType, 1, 0,
        I18n.getLanguage("mrCfgCrUnitTel.networkType").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetDeviceType, 0, 0,
        I18n.getLanguage("mrCfgCrUnitTel.arrayName").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetDeviceType, 1, 0,
        I18n.getLanguage("mrCfgCrUnitTel.networkType").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetDeviceType, 2, 0,
        I18n.getLanguage("mrCfgCrUnitTel.deviceType").toUpperCase(), styles.get("header"));
    if (listArray != null && listArray.size() > 0) {
      for (CatItemDTO dto : listArray) {
        List<String> listNetwork = mrDeviceRepository.getNetworkTypeByArrayCode(dto.getItemCode());
        if (listNetwork != null && listNetwork.size() > 0) {
          for (String network : listNetwork) {
            List<String> listDevice = mrDeviceRepository
                .getDeviceTypeByNetworkType(dto.getItemCode(), network);
            for (String device : listDevice) {
              ewu.createCell(sheetDeviceType, 0, rowDe, dto.getItemName(), styles.get("cell"));
              ewu.createCell(sheetDeviceType, 1, rowDe, network, styles.get("cell"));
              ewu.createCell(sheetDeviceType, 2, rowDe++, device, styles.get("cell"));
            }
            ewu.createCell(sheetNetworkType, 0, row, dto.getItemName(), styles.get("cell"));
            ewu.createCell(sheetNetworkType, 1, row++, network, styles.get("cell"));
          }
        }
      }
    }
    sheetNetworkType.autoSizeColumn(0);
    sheetNetworkType.autoSizeColumn(1);
    sheetNetworkType.setSelected(false);

    sheetDeviceType.autoSizeColumn(0);
    sheetDeviceType.autoSizeColumn(1);
    sheetDeviceType.autoSizeColumn(2);
    sheetDeviceType.setSelected(false);

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(marketColumn, 7000);
    sheetMain.setColumnWidth(regionColumn, 7000);
    sheetMain.setColumnWidth(arrayColumn, 10000);
    sheetMain.setColumnWidth(networkTypeColumn, 7000);
    sheetMain.setColumnWidth(deviceTypeColumn, 6000);
    sheetMain.setColumnWidth(implementUnitColumn, 7000);
    sheetMain.setColumnWidth(checkingUnitColumn, 7000);

    sheetOrther.setSelected(false);
    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("mrCfgCrUnitTel.import.sheetName"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MR_CFG_CR_UNIT_TEMPLATE" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public List<String> getListRegionCombobox(String marketCode) {
    log.debug("Request to getListRegionCombobox: {}", marketCode);
    return mrDeviceRepository.getListRegionSoftByMarketCode(marketCode);
  }

  @Override
  public List<String> getListNetworkTypeCombobox(String arrayCode) {
    log.debug("Request to getListNetworkTypeCombobox: {}", arrayCode);
    return mrDeviceRepository.getNetworkTypeByArrayCode(arrayCode);
  }

  @Override
  public List<String> getListDeviceTypeCombobox(MrDeviceDTO mrDeviceDTO) {
    log.debug("Request to getListDeviceTypeCombobox: {}", mrDeviceDTO);
    return mrDeviceRepository
        .getDeviceTypeByNetworkType(mrDeviceDTO.getArrayCode(), mrDeviceDTO.getNetworkType());
  }

  @Override
  public ResultInSideDto importDataMrCfgCrUnitTel(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<MrCfgCrUnitTelDTO> listDto;
    List<MrCfgCrUnitTelDTO> listImportDto;
    Map<String, String> mapImportDTO;

    String[] header = new String[]{"marketName", "region", "arrayName", "networkType", "deviceType",
        "implementUnitId", "checkingUnitId", "resultImport"};

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
            0, 7, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 6,
            0, 7, 1000);
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
          setMapAll();
          setMapUnit();
          for (Object[] obj : lstData) {
            MrCfgCrUnitTelDTO importDTO = new MrCfgCrUnitTelDTO();
            if (obj[1] != null) {
              importDTO.setMarketName(obj[1].toString().trim());
            }
            if (obj[2] != null) {
              importDTO.setRegion(obj[2].toString().trim());
            }
            if (obj[3] != null) {
              importDTO.setArrayName(obj[3].toString().trim());
            }
            if (obj[4] != null) {
              importDTO.setNetworkType(obj[4].toString().trim());
            }
            if (obj[5] != null) {
              importDTO.setDeviceType(obj[5].toString().trim());
            }
            if (obj[6] != null) {
              importDTO.setImplementUnitId(obj[6].toString().trim());
            }
            if (obj[7] != null) {
              importDTO.setCheckingUnitId(obj[7].toString().trim());
            }
            MrCfgCrUnitTelDTO tempImportDTO = validateImportInfo(importDTO, mapImportDTO);
            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.result.import.ok"));
              listImportDto.add(tempImportDTO);
              String marketCode = importDTO.getMarketCode();
              String region = importDTO.getRegion();
              String arrayCode = importDTO.getArrayCode();
              String networkType = importDTO.getNetworkType();
              String deviceType = importDTO.getDeviceType();
              String key = marketCode + "_" + region + "_" + arrayCode + "_" + networkType + "_"
                  + deviceType;
              mapImportDTO.put(key, String.valueOf(value));
              listDto.add(tempImportDTO);
            } else {
              listImportDto.add(tempImportDTO);
              index++;
            }
            value++;
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              for (MrCfgCrUnitTelDTO dto : listDto) {
                resultInSideDto = insertMrCfgCrUnitTel(dto);
              }
            }
          } else {
            File fileExport = handleFileExport(listImportDto, header, null, "RESULT_IMPORT");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getLanguage("import.common.fail"));
            resultInSideDto.setFile(fileExport);
            resultInSideDto.setFilePath(fileExport.getPath());
            return resultInSideDto;
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFileExport(listImportDto, header, null, "RESULT_IMPORT");
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

  private MrCfgCrUnitTelDTO validateImportInfo(MrCfgCrUnitTelDTO importDTO,
      Map<String, String> mapImportDTO) {
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(importDTO.getMarketName())) {
      resultImport = resultImport.concat(I18n.getLanguage("mrCfgCrUnitTel.err.marketName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getRegion())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("mrCfgCrUnitTel.err.region"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getArrayName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("mrCfgCrUnitTel.err.arrayName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getNetworkType())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("mrCfgCrUnitTel.err.networkType"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getDeviceType())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("mrCfgCrUnitTel.err.deviceType"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getImplementUnitId())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("mrCfgCrUnitTel.err.implementUnit"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getCheckingUnitId())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("mrCfgCrUnitTel.err.checkingUnit"));
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      importDTO.setResultImport(resultImport);
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getMarketName())) {
      for (Long marketId : mapMarket.keySet()) {
        if (importDTO.getMarketName().equals(mapMarket.get(marketId))) {
          importDTO.setMarketCode(String.valueOf(marketId));
          break;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getMarketCode())) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.err.marketName.valid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getRegion())) {
      List<String> listRegion = mapRegion.get(Long.valueOf(importDTO.getMarketCode()));
      if (listRegion.size() > 0) {
        if (!listRegion.contains(importDTO.getRegion())) {
          importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.err.region.valid"));
          return importDTO;
        }
      }
      if (listRegion.size() == 0) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.err.region.valid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getArrayName())) {
      for (String array : mapArray.keySet()) {
        if (importDTO.getArrayName().equals(mapArray.get(array).getItemName())) {
          importDTO.setArrayCode(array);
          break;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getArrayCode())) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.err.array.valid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getNetworkType())) {
      List<String> listNetwork = mapNetwork.get(importDTO.getArrayCode());
      if (listNetwork.size() > 0) {
        if (!listNetwork.contains(importDTO.getNetworkType())) {
          importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.err.networkType.valid"));
          return importDTO;
        }
      }
      if (listNetwork.size() == 0) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.err.networkType.valid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getDeviceType())) {
      List<String> listDevice = mapDevice.get(importDTO.getNetworkType());
      if (listDevice.size() > 0) {
        if (!listDevice.contains(importDTO.getDeviceType())) {
          importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.err.deviceType.valid"));
          return importDTO;
        }
      }
      if (listDevice.size() == 0) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.err.deviceType.valid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getImplementUnitId())) {
      for (Long unitId : mapUnit.keySet()) {
        if (importDTO.getImplementUnitId()
            .equals(String.valueOf(mapUnit.get(unitId).getUnitId()))) {
          importDTO.setImplementUnit(unitId);
          break;
        }
      }
      if (importDTO.getImplementUnit() == null) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.err.implementUnit.valid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getCheckingUnitId())) {
      for (Long unitId : mapUnit.keySet()) {
        if (importDTO.getCheckingUnitId().equals(String.valueOf(mapUnit.get(unitId).getUnitId()))) {
          importDTO.setCheckingUnit(unitId);
          break;
        }
      }
      if (importDTO.getCheckingUnit() == null) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.err.checkingUnit.valid"));
        return importDTO;
      }
    }
    String validateDuplicate = validateDuplicateImport(importDTO, mapImportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      importDTO.setResultImport(validateDuplicate);
      return importDTO;
    }
    MrCfgCrUnitTelDTO dtoTmp = mrCfgCrUnitTelRepository.checkMrCfgCrUnitTelExit(importDTO);
    if (dtoTmp != null) {
      importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitTel.err.dup-code"));
    }
    return importDTO;
  }

  private String validateDuplicateImport(MrCfgCrUnitTelDTO importDTO,
      Map<String, String> mapImportDTO) {
    String marketCode = importDTO.getMarketCode();
    String region = importDTO.getRegion();
    String arrayCode = importDTO.getArrayCode();
    String networkType = importDTO.getNetworkType();
    String deviceType = importDTO.getDeviceType();
    String key = marketCode + "_" + region + "_" + arrayCode + "_" + networkType + "_" + deviceType;
    String value = mapImportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("mrCfgCrUnitTel.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }

  private String checkResultImport(String resultImport) {
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      resultImport = resultImport.concat("; \n");
    }
    return resultImport;
  }

  @Override
  public List<UnitDTO> getListUnitCombobox() {
    log.debug("Request to getListUnitCombobox: {}");
    return unitRepository.getListUnit(null);
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 8) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitTel.STT")).equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitTel.marketName") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitTel.region") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitTel.arrayName") + " (*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitTel.networkType") + " (*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitTel.deviceType") + " (*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitTel.implementUnitId") + " (*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitTel.checkingUnitId") + " (*)")
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    return true;
  }

  public File handleFileExport(List<MrCfgCrUnitTelDTO> list, String[] columnExport, String date,
      String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = "";
    String title = "";
    String fileNameOut = "";
    String headerPrefix = "language.mrCfgCrUnitTel";
    String firstLeftHeader = I18n.getLanguage("mrCfgCrUnitTel.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("mrCfgCrUnitTel.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("mrCfgCrUnitTel.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("mrCfgCrUnitTel.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle;
    if (StringUtils.isNotNullOrEmpty(date)) {
      subTitle = I18n.getLanguage("mrCfgCrUnitTel.export.exportDate", date);
    } else {
      subTitle = "";
    }
    switch (code) {
      case "RESULT_IMPORT":
        sheetName = I18n.getLanguage("mrCfgCrUnitTel.import.sheetName");
        title = I18n.getLanguage("mrCfgCrUnitTel.import.title");
        fileNameOut = I18n.getLanguage("mrCfgCrUnitTel.import.fileNameOut");
        break;
      case "EXPORT_MR_CFG_CR_UNIT_TEL":
        sheetName = I18n.getLanguage("mrCfgCrUnitTel.export.sheetname");
        title = I18n.getLanguage("mrCfgCrUnitTel.export.title");
        fileNameOut = I18n.getLanguage("mrCfgCrUnitTel.export.fileNameOut");
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
        I18n.getLanguage("mrCfgCrUnitTel.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT_NOVALUE.xlsx";
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

  public void setDetailValue(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    if (StringUtils.isNotNullOrEmpty(mrCfgCrUnitTelDTO.getMarketCode())) {
      mrCfgCrUnitTelDTO
          .setMarketName(mapMarket.get(Long.valueOf(mrCfgCrUnitTelDTO.getMarketCode())));
    }
    Long implementUnitId = mrCfgCrUnitTelDTO.getImplementUnit();
    if (implementUnitId != null && mapUnit.containsKey(implementUnitId)) {
      mrCfgCrUnitTelDTO.setImplementUnitName(mapUnit.get(implementUnitId).getUnitName());
    }
    Long checkingUnitId = mrCfgCrUnitTelDTO.getCheckingUnit();
    if (checkingUnitId != null && mapUnit.containsKey(checkingUnitId)) {
      mrCfgCrUnitTelDTO.setCheckingUnitName(mapUnit.get(checkingUnitId).getUnitName());
    }
//    String arrayCode = mrCfgCrUnitTelDTO.getArrayCode();
//    if (arrayCode != null && mapArray.containsKey(arrayCode)) {
//      mrCfgCrUnitTelDTO.setArrayName(mapArray.get(arrayCode).getItemName());
//    }
  }

  public void setMapMarket() {
    List<ItemDataCRInside> list = catLocationRepository.getListLocationByLevelCBB(null, 1L, null);
    if (list != null && !list.isEmpty()) {
      for (ItemDataCRInside dto : list) {
        mapMarket.put(dto.getValueStr(), dto.getDisplayStr());
      }
    }
  }

  public void setMapUnit() {
    List<UnitDTO> list = unitRepository.getListUnit(new UnitDTO());
    if (list != null && list.size() > 0) {
      for (UnitDTO dto : list) {
        mapUnit.put(dto.getUnitId(), dto);
      }
    }
  }

  public void setMapArray() {
    Datatable dataArray = catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataArray.getData();
    if (list != null && list.size() > 0) {
      for (CatItemDTO dto : list) {
        mapArray.put(dto.getItemName(), dto);
      }
    }
  }

  public void setMapAll() {
    List<ItemDataCRInside> listMarket = catLocationRepository
        .getListLocationByLevelCBB(null, 1L, null);
    if (listMarket != null && !listMarket.isEmpty()) {
      for (ItemDataCRInside dto : listMarket) {
        mapMarket.put(dto.getValueStr(), dto.getDisplayStr());
        List<String> listRegion = mrDeviceRepository
            .getListRegionSoftByMarketCode(String.valueOf(dto.getValueStr()));
        if (listRegion != null && listRegion.size() > 0) {
          mapRegion.put(dto.getValueStr(), listRegion);
        } else {
          mapRegion.put(dto.getValueStr(), new ArrayList<>());
        }
      }
    }
    Datatable dataArray = catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> listArray = (List<CatItemDTO>) dataArray.getData();
    if (listArray != null && listArray.size() > 0) {
      for (CatItemDTO dto : listArray) {
        mapArray.put(dto.getItemName(), dto);
        List<String> listNetwork = mrDeviceRepository.getNetworkTypeByArrayCode(dto.getItemName());
        if (listNetwork != null && listNetwork.size() > 0) {
          mapNetwork.put(dto.getItemName(), listNetwork);
          for (String network : listNetwork) {
            List<String> listDevice = mrDeviceRepository
                .getDeviceTypeByNetworkType(dto.getItemName(), network);
            if (listDevice != null && listDevice.size() > 0) {
              mapDevice.put(network, listDevice);
            } else {
              mapDevice.put(network, new ArrayList<>());
            }
          }
        } else {
          mapNetwork.put(dto.getItemName(), new ArrayList<>());
        }
      }
    }
  }
}
