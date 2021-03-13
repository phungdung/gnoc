package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
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
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHardUnitConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrDeviceCDRepository;
import com.viettel.gnoc.mr.repository.MrHardUnitConfigRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
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
public class MrHardUnitConfigBusinessImpl implements MrHardUnitConfigBusiness {

  @Autowired
  protected MrHardUnitConfigRepository mrHardUnitConfigRepository;

  @Autowired
  protected MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Autowired
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  MrDeviceCDRepository mrDeviceCDRepository;

  @Autowired
  CatLocationBusiness catLocationBusiness;

  @Autowired
  UserRepository userRepository;

  @Autowired
  MrITHardScheduleBusiness mrITHardScheduleBusiness;

  @Value("${application.temp.folder}")
  private String tempFolder;
  HashMap<String, String> mapMarketCode = new HashMap<>();
  HashMap<String, String> mapArray = new HashMap<>();
  HashMap<String, String> mapCdIdHard = new HashMap<>();
  HashMap<String, String> mapStationCode = new HashMap<>();
  private final static String MR_HARD_UNIT_CONFIG_RESULT_IMPORT = "MR_HARD_UNIT_CONFIG_RESULT_IMPORT";
  private final static String MR_HARD_UNIT_CONFIG_EXPORT = "MR_HARD_UNIT_CONFIG_EXPORT";

  @Override
  public Datatable getListMrHardUnitConfigDTO(MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    log.debug("Request to getListMrHardUnitConfigDTO: {}", mrHardUnitConfigDTO);
    return mrHardUnitConfigRepository.getListMrHardGroupConfigDTO(mrHardUnitConfigDTO);
  }

  @Override
  public ResultInSideDto insert(MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    log.debug("Request to add MrHardUnitConfig: {}", mrHardUnitConfigDTO);
    UserToken userToken = ticketProvider.getUserToken();
    mrHardUnitConfigDTO.setUpdateUser(userToken.getUserName());
    mrHardUnitConfigDTO.setUpdateDate(new Date());
    return mrHardUnitConfigRepository.add(mrHardUnitConfigDTO);
  }

  @Override
  public ResultInSideDto update(MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    log.debug("Request to update MrHardUnitConfig: {}", mrHardUnitConfigDTO);
    UserToken userToken = ticketProvider.getUserToken();
    mrHardUnitConfigDTO.setUpdateUser(userToken.getUserName());
    mrHardUnitConfigDTO.setUpdateDate(new Date());
    return mrHardUnitConfigRepository.edit(mrHardUnitConfigDTO);
  }

  @Override
  public MrHardUnitConfigDTO getDetail(Long id) {
    log.debug("Request to getDetail MrHardUnitConfig: {}", id);
    return mrHardUnitConfigRepository.getDetail(id);
  }

  @Override
  public ResultInSideDto delete(Long id) {
    log.debug("Request to delete MrHardUnitConfig: {}", id);
    return mrHardUnitConfigRepository.delete(id);
  }

  @Override
  public List<MrDeviceDTO> getListRegionByMarketCode(String marketCode) {
    log.debug("Request to getListRegionByMarketCode: {}", marketCode);
    return mrHardUnitConfigRepository.getListRegionByMarketCode(marketCode);
  }

  @Override
  public List<MrDeviceDTO> getListNetworkTypeByArrayCode(String arrayCode) {
    log.debug("Request to getListNetworkTypeByArrayCode: {}", arrayCode);
    return mrHardUnitConfigRepository.getListNetworkTypeByArrayCode(arrayCode);
  }

  public void setMapMarketCode() {
    List<ItemDataCRInside> lstMarketCode = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstMarketCode != null && !lstMarketCode.isEmpty()) {
      for (ItemDataCRInside itemDataCRInside : lstMarketCode) {
        mapMarketCode
            .put(String.valueOf(itemDataCRInside.getValueStr()), itemDataCRInside.getDisplayStr());
      }
    }
  }

  public void setMapArray() {
    List<CatItemDTO> lstArrayCode = mrCfgProcedureCDBusiness.getMrSubCategory();
    if (lstArrayCode != null && !lstArrayCode.isEmpty()) {
      for (CatItemDTO catItemDTO : lstArrayCode) {
        mapArray
            .put(String.valueOf(catItemDTO.getItemCode()), catItemDTO.getItemName());
      }
    }
  }

  public void setMapCdIdHard() {
    WoCdGroupTypeUserDTO woCdGroupTypeUserDTO = new WoCdGroupTypeUserDTO();
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = woCategoryServiceProxy
        .getListCdGroupByUser(woCdGroupTypeUserDTO);
    if (lstGroupInsideDTOS != null && !lstGroupInsideDTOS.isEmpty()) {
      for (WoCdGroupInsideDTO dto : lstGroupInsideDTOS) {
        mapCdIdHard
            .put(String.valueOf(dto.getWoGroupId()), dto.getWoGroupName());
      }
    }
  }

  public void setMapStationCode() {
    List<MrSynItDevicesDTO> lstStationCode = mrSynItSoftDevicesRepository
        .getDeviceITStationCodeCbb();
    if (lstStationCode != null && !lstStationCode.isEmpty()) {
      for (MrSynItDevicesDTO dto : lstStationCode) {
        mapStationCode
            .put(String.valueOf(dto.getStation()), dto.getStation());
      }
    }
  }

  @Override
  public File exportData(MrHardUnitConfigDTO mrHardUnitConfigDTO) throws Exception {
    List<MrHardUnitConfigDTO> mrHardUnitConfigDTOList = mrHardUnitConfigRepository
        .getListDataExport(mrHardUnitConfigDTO);
    return exportFileTemplate(mrHardUnitConfigDTOList, "");
  }

  private List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private File exportFileTemplate(List<MrHardUnitConfigDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("mrHardUnitConfig.export.title");
    String title = I18n.getLanguage("mrHardUnitConfig.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    if (Constants.RESULT_IMPORT.equals(key)) {
      headerExportList = readerHeaderSheet("id", "marketCodeStr", "regionStr",
          "arrayCodeStr", "deviceTypeStr", "cdIdHardStr", "stationCodeStr",
          "resultImport");
      fileNameOut = MR_HARD_UNIT_CONFIG_RESULT_IMPORT;
      subTitle = null;
    } else {
      headerExportList = readerHeaderSheet("id", "marketCodeStr", "region",
          "arrayCodeStr", "deviceType", "cdIdHardStr", "stationCode",
          "updateDate", "updateUser");
      fileNameOut = MR_HARD_UNIT_CONFIG_EXPORT;
      subTitle = I18n
          .getLanguage("mrDevice.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.mrHardGroupConfig"
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
    XSSFSheet sheetParam2 = workbook.createSheet("param2");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr"),
        I18n.getLanguage("mrHardGroupConfig.region"),
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"),
//        I18n.getLanguage("mrDevice.userMrHard"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"),
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr"),
        I18n.getLanguage("mrHardGroupConfig.region"),
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"),
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int marketCodeStrColumn = listHeader
        .indexOf(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"));
    int arrayCodeStrColumn = listHeader.indexOf(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"));
    int cdIdHardStrColumn = listHeader.indexOf(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"));
    int stationCodeStrColumn = listHeader
        .indexOf(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("mrHardUnitConfig.title"));
    titleCell.setCellStyle(style.get("title"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(4);
    Row headerRegion = sheetParam1.createRow(0);
    Row headerArray = sheetParam2.createRow(0);
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
    Cell headerCellMarketCode = headerRegion.createCell(0);
    Cell headerCellRegion = headerRegion.createCell(1);
    XSSFRichTextString market = new XSSFRichTextString(
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr"));
    XSSFRichTextString reg = new XSSFRichTextString(
        I18n.getLanguage("mrHardGroupConfig.region"));
    headerCellMarketCode.setCellValue(market);
    headerCellMarketCode.setCellStyle(style.get("header"));
    headerCellRegion.setCellValue(reg);
    headerCellRegion.setCellStyle(style.get("header"));
    sheetParam1.setColumnWidth(0, 15000);
    sheetParam1.setColumnWidth(1, 15000);

    Cell headerCellArray = headerArray.createCell(0);
    Cell headerCellDevice = headerArray.createCell(1);
    XSSFRichTextString array = new XSSFRichTextString(
        I18n.getLanguage("mrSynItSoftDevice.arrayCodeStr"));
    XSSFRichTextString device = new XSSFRichTextString(
        I18n.getLanguage("mrSynItSoftDevice.deviceType"));
    headerCellArray.setCellValue(array);
    headerCellArray.setCellStyle(style.get("header"));
    headerCellDevice.setCellValue(device);
    headerCellDevice.setCellStyle(style.get("header"));
    sheetParam2.setColumnWidth(0, 15000);
    sheetParam2.setColumnWidth(1, 15000);
    sheetOne.setColumnWidth(0, 3000);

//    Cell headerCellArray = headerArray.createCell(0);
//    Cell headerCellNetwork = headerArray.createCell(1);
//    Cell headerCellDevice = headerArray.createCell(2);
//    XSSFRichTextString array = new XSSFRichTextString(
//        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"));
//    XSSFRichTextString network = new XSSFRichTextString(
//        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"));
//    XSSFRichTextString device = new XSSFRichTextString(
//        I18n.getLanguage("mrHardGroupConfig.deviceType"));
//    headerCellArray.setCellValue(array);
//    headerCellArray.setCellStyle(style.get("header"));
//    headerCellNetwork.setCellValue(network);
//    headerCellNetwork.setCellStyle(style.get("header"));
//    headerCellDevice.setCellValue(device);
//    headerCellDevice.setCellStyle(style.get("header"));
//    sheetParam2.setColumnWidth(0, 15000);
//    sheetParam2.setColumnWidth(1, 15000);
//    sheetParam2.setColumnWidth(2, 15000);
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown
    int row = 5;
    List<ItemDataCRInside> lstMarketCode = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    for (ItemDataCRInside dto : lstMarketCode) {
      excelWriterUtils
          .createCell(sheetParam, 2, row++, dto.getDisplayStr(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name marketCodeStr = workbook.createName();
    marketCodeStr.setNameName("marketCodeStr");
    marketCodeStr.setRefersToFormula("param!$C$2:$C$" + row);
    XSSFDataValidationConstraint marketCodeStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "marketCodeStr");
    CellRangeAddressList marketCodeStrCreate = new CellRangeAddressList(5, 65000,
        marketCodeStrColumn,
        marketCodeStrColumn);
    XSSFDataValidation dataValidationMarketCodeStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            marketCodeStrConstraint, marketCodeStrCreate);
    dataValidationMarketCodeStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationMarketCodeStr);

    row = 1;
    for (ItemDataCRInside marketCode : lstMarketCode) {
      List<MrITHardScheduleDTO> mrDeviceDTOList = mrITHardScheduleBusiness
          .getListRegionByMrSynItDevices(String.valueOf(marketCode.getValueStr()));
      excelWriterUtils
          .createCell(sheetParam1, 0, row, marketCode.getDisplayStr(), style.get("cell"));
      for (MrITHardScheduleDTO mrITHardScheduleDTO : mrDeviceDTOList) {
        excelWriterUtils
            .createCell(sheetParam1, 1, row++, mrITHardScheduleDTO.getRegion(), style.get("cell"));
      }
    }
    sheetParam1.autoSizeColumn(0);
    sheetParam1.autoSizeColumn(1);

    row = 5;
    List<CatItemDTO> lstArrayCode = mrCfgProcedureCDBusiness.getMrSubCategory();
    for (CatItemDTO dto : lstArrayCode) {
      excelWriterUtils
          .createCell(sheetParam, 4, row++, dto.getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name arrayCodeStr = workbook.createName();
    arrayCodeStr.setNameName("arrayCodeStr");
    arrayCodeStr.setRefersToFormula("param!$E$2:$E$" + row);
    XSSFDataValidationConstraint arrayCodeStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "arrayCodeStr");
    CellRangeAddressList arrayCodeStrCreate = new CellRangeAddressList(5, 65000, arrayCodeStrColumn,
        arrayCodeStrColumn);
    XSSFDataValidation dataValidationArrayCodeStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            arrayCodeStrConstraint, arrayCodeStrCreate);
    dataValidationArrayCodeStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationArrayCodeStr);

//    row = 1;
//    for (CatItemDTO arrayCode : lstArrayCode) {
//      List<MrDeviceDTO> lstNetwork = mrDeviceRepository
//          .getListNetworkTypeByArrayCode(arrayCode.getItemValue());
//      excelWriterUtils
//          .createCell(sheetParam2, 0, row, arrayCode.getItemName(), style.get("cell"));
//      for (MrDeviceDTO networkCode : lstNetwork) {
//        List<MrDeviceDTO> lstDeviceType = mrDeviceRepository
//            .getListDeviceTypeByNetworkType(arrayCode.getItemValue(), networkCode.getNetworkType());
//        excelWriterUtils
//            .createCell(sheetParam2, 1, row, networkCode.getNetworkType(), style.get("cell"));
//        for (MrDeviceDTO deviceType : lstDeviceType) {
//          excelWriterUtils
//              .createCell(sheetParam2, 2, row++, deviceType.getDeviceType(), style.get("cell"));
//        }
//        excelWriterUtils
//            .createCell(sheetParam2, 0, row++, null, style.get("cell"));
//      }
//      excelWriterUtils
//          .createCell(sheetParam2, 0, row++, null, style.get("cell"));
//    }
//    sheetParam1.autoSizeColumn(0);
//    sheetParam1.autoSizeColumn(1);

    row = 1;
    for (CatItemDTO arrayCode : lstArrayCode) {
      List<MrSynItDevicesDTO> lstDeviceType = mrSynItSoftDevicesRepository
          .getListDeviceTypeByArrayCode(arrayCode.getItemValue());
      excelWriterUtils
          .createCell(sheetParam2, 0, row, arrayCode.getItemName(), style.get("cell"));
      for (MrSynItDevicesDTO deviceDTO : lstDeviceType) {
        excelWriterUtils
            .createCell(sheetParam2, 1, row++, deviceDTO.getDeviceType(), style.get("cell"));
      }
      excelWriterUtils
          .createCell(sheetParam2, 2, row++, null, style.get("cell"));
    }
    sheetParam2.autoSizeColumn(0);
    sheetParam2.autoSizeColumn(1);

    row = 5;
    WoCdGroupTypeUserDTO woCdGroupTypeUserDTO = new WoCdGroupTypeUserDTO();
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = woCategoryServiceProxy
        .getListCdGroupByUser(woCdGroupTypeUserDTO);
    for (WoCdGroupInsideDTO dto : lstGroupInsideDTOS) {
      excelWriterUtils
          .createCell(sheetParam, 7, row++, dto.getWoGroupName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name cdIdHardStr = workbook.createName();
    cdIdHardStr.setNameName("cdIdHardStr");
    cdIdHardStr.setRefersToFormula("param!$H$2:$H$" + row);
    XSSFDataValidationConstraint cdIdHardStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "cdIdHardStr");
    CellRangeAddressList cdIdHardStrCreate = new CellRangeAddressList(5, 65000, cdIdHardStrColumn,
        cdIdHardStrColumn);
    XSSFDataValidation dataValidationCdIdHardStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            cdIdHardStrConstraint, cdIdHardStrCreate);
    dataValidationCdIdHardStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationCdIdHardStr);

    row = 5;
    List<MrSynItDevicesDTO> lstStationCode = mrSynItSoftDevicesRepository
        .getDeviceITStationCodeCbb();
    for (MrSynItDevicesDTO dto : lstStationCode) {
      excelWriterUtils
          .createCell(sheetParam, 8, row++, dto.getStation(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name stationCodeStr = workbook.createName();
    stationCodeStr.setNameName("stationCodeStr");
    stationCodeStr.setRefersToFormula("param!$I$2:$I$" + row);
    XSSFDataValidationConstraint stationCodeStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "stationCodeStr");
    CellRangeAddressList stationCodeStrCreate = new CellRangeAddressList(5, 65000,
        stationCodeStrColumn,
        stationCodeStrColumn);
    XSSFDataValidation dataValidationStationCodeStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            stationCodeStrConstraint, stationCodeStrCreate);
    dataValidationStationCodeStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationStationCodeStr);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("mrHardUnitConfig.title"));
    workbook.setSheetName(2, I18n.getLanguage("mrHardGroupConfig.region"));
    workbook.setSheetName(3, I18n.getLanguage("mrSynItSoftDevice.arrDevice"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MR_HARD_UNIT_CONFIG" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<MrHardUnitConfigDTO> mrHardUnitConfigDTOList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      } else {
        if (!(multipartFile.getOriginalFilename().toUpperCase().endsWith(".XLSX") || multipartFile
            .getOriginalFilename().toUpperCase().endsWith(".XLS"))) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        File fileImport = new File(filePath);

        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            7,
            1000
        );
        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORM);
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            7,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }
        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          setMapMarketCode();
          setMapArray();
          setMapCdIdHard();
          setMapStationCode();
          for (Object[] obj : dataImportList) {
            MrHardUnitConfigDTO mrHardUnitConfigDTO = new MrHardUnitConfigDTO();
            if (obj[1] != null) {
              if (!DataUtil.isInteger(obj[1].toString().trim())) {
//                if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getResultImport())) {
                mrHardUnitConfigDTO
                    .setResultImport(I18n.getLanguage("mrHardGroupConfig.errType.idStr"));
                mrHardUnitConfigDTO.setIdStr(obj[1].toString().trim());
//                } else {
//                  mrHardUnitConfigDTO
//                      .setResultImport(mrHardUnitConfigDTO.getResultImport() + "\n" + I18n
//                          .getLanguage("mrHardGroupConfig.errType.idStr"));
//                  mrHardUnitConfigDTO.setIdStr(obj[1].toString().trim());
//                }
              } else {
                mrHardUnitConfigDTO.setIdStr(obj[1].toString().trim());
                mrHardUnitConfigDTO.setId(Long.valueOf(obj[1].toString().trim()));
              }
            } else {
              mrHardUnitConfigDTO.setIdStr(null);
            }
            if (obj[2] != null) {
              mrHardUnitConfigDTO.setMarketCodeStr(obj[2].toString().trim());
              for (Map.Entry<String, String> item : mapMarketCode.entrySet()) {
                if (mrHardUnitConfigDTO.getMarketCodeStr().equals(item.getValue())) {
                  mrHardUnitConfigDTO.setMarketCode(item.getKey());
                  break;
                } else {
                  mrHardUnitConfigDTO.setMarketCode(null);
                }
              }
            } else {
              mrHardUnitConfigDTO.setMarketCodeStr(null);
            }
            if (obj[3] != null) {
              mrHardUnitConfigDTO.setRegionStr(obj[3].toString().trim());
            } else {
              mrHardUnitConfigDTO.setRegionStr(null);
            }
            if (obj[4] != null) {
              mrHardUnitConfigDTO.setArrayCodeStr(obj[4].toString().trim());
              for (Map.Entry<String, String> item : mapArray.entrySet()) {
                if (mrHardUnitConfigDTO.getArrayCodeStr().equals(item.getValue())) {
                  mrHardUnitConfigDTO.setArrayCode(item.getKey());
                  break;
                } else {
                  mrHardUnitConfigDTO.setArrayCode(null);
                }
              }
            } else {
              mrHardUnitConfigDTO.setArrayCodeStr(null);
            }
//            if (!StringUtils.isStringNullOrEmpty(obj[5])) {
//              mrHardUnitConfigDTO.setUserMrHard(obj[5].toString().trim());
//            } else {
//              mrHardUnitConfigDTO.setNetworkTypeStr(null);
//            }
            if (obj[5] != null) {
              mrHardUnitConfigDTO.setDeviceTypeStr(obj[5].toString().trim());
            } else {
              mrHardUnitConfigDTO.setDeviceTypeStr(null);
            }
            if (obj[6] != null) {
              mrHardUnitConfigDTO.setCdIdHardStr(obj[6].toString().trim());
              for (Map.Entry<String, String> item : mapCdIdHard.entrySet()) {
                if (mrHardUnitConfigDTO.getCdIdHardStr().equals(item.getValue())) {
                  mrHardUnitConfigDTO.setCdIdHard(Long.valueOf(item.getKey()));
                  break;
                } else {
                  mrHardUnitConfigDTO.setCdIdHard(null);
                }
              }
            } else {
              mrHardUnitConfigDTO.setCdIdHardStr(null);
            }
            if (obj[7] != null) {
              mrHardUnitConfigDTO.setStationCodeStr(obj[7].toString().trim());
              for (Map.Entry<String, String> item : mapStationCode.entrySet()) {
                if (mrHardUnitConfigDTO.getStationCodeStr().equals(item.getValue())) {
                  mrHardUnitConfigDTO.setStationCode(item.getKey());
                  break;
                } else {
                  mrHardUnitConfigDTO.setStationCode(null);
                }
              }
            } else {
              mrHardUnitConfigDTO.setStationCodeStr(null);
            }
            if (mrHardUnitConfigDTO.getResultImport() == null) {
              MrHardUnitConfigDTO mrHardUnitConfigDTOTmp = validateImportInfo(
                  mrHardUnitConfigDTO, mrHardUnitConfigDTOList);
              if (mrHardUnitConfigDTOTmp.getResultImport() == null) {
                mrHardUnitConfigDTOTmp
                    .setResultImport(I18n.getLanguage("mrHardGroupConfig.result.import"));
                mrHardUnitConfigDTOList.add(mrHardUnitConfigDTOTmp);
              } else {
                mrHardUnitConfigDTOList.add(mrHardUnitConfigDTO);
                index++;
              }
            } else {
              mrHardUnitConfigDTOList.add(mrHardUnitConfigDTO);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (mrHardUnitConfigDTOList != null) {
              UserToken userToken = ticketProvider.getUserToken();
              for (MrHardUnitConfigDTO dto : mrHardUnitConfigDTOList) {
                dto.setUpdateUser(userToken.getUserName());
                dto.setUpdateDate(new Date());
                resultInSideDto = mrHardUnitConfigRepository.add(dto);
              }
            }
            if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
              File fileExport = exportFileTemplate(mrHardUnitConfigDTOList,
                  Constants.RESULT_IMPORT);
              resultInSideDto.setFile(fileExport);
              resultInSideDto.setFilePath(fileExport.getPath());
            }
          } else {
            File fileExport = exportFileTemplate(mrHardUnitConfigDTOList,
                Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
            resultInSideDto.setFilePath(fileExport.getPath());
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileTemplate(mrHardUnitConfigDTOList, Constants.RESULT_IMPORT);
          resultInSideDto.setFile(fileExport);
          resultInSideDto.setFilePath(fileExport.getPath());
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
    if (count != 8) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrHardGroupConfig.id")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrHardGroupConfig.region") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
//    if (!I18n.getLanguage("mrDevice.userMrHard")
//        .equalsIgnoreCase(objects[5].toString().trim())) {
//      return false;
//    }
    if (!(I18n.getLanguage("mrHardGroupConfig.deviceType") + "*")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*")
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    return true;
  }

  public MrHardUnitConfigDTO validateImportInfo(MrHardUnitConfigDTO mrHardUnitConfigDTO,
      List<MrHardUnitConfigDTO> list) {
    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getMarketCodeStr())) {
      mrHardUnitConfigDTO.setResultImport(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"));
      return mrHardUnitConfigDTO;
    }

    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getRegionStr())) {
      mrHardUnitConfigDTO.setResultImport(I18n.getLanguage("mrHardGroupConfig.err.regionStr"));
      return mrHardUnitConfigDTO;
    }

    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getArrayCodeStr())) {
      mrHardUnitConfigDTO.setResultImport(I18n.getLanguage("mrHardGroupConfig.err.arrayCodeStr"));
      return mrHardUnitConfigDTO;
    }

    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getDeviceTypeStr())) {
      mrHardUnitConfigDTO.setResultImport(I18n.getLanguage("mrHardGroupConfig.err.deviceType"));
      return mrHardUnitConfigDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getDeviceTypeStr())) {
      List<MrSynItDevicesDTO> lstDeviceType = mrSynItSoftDevicesRepository
          .getListDeviceTypeByArrayCode(mrHardUnitConfigDTO.getArrayCode());
      if (lstDeviceType != null && !lstDeviceType.isEmpty()) {
        for (MrSynItDevicesDTO dto : lstDeviceType) {
          if (dto.getDeviceType().equals(mrHardUnitConfigDTO.getDeviceTypeStr())) {
            mrHardUnitConfigDTO.setDeviceType(dto.getDeviceType());
          }
        }
        if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getDeviceType())) {
          mrHardUnitConfigDTO
              .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.deviceType.exist"));
          return mrHardUnitConfigDTO;
        }
      } else {
        mrHardUnitConfigDTO
            .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.deviceType.exist"));
        return mrHardUnitConfigDTO;
      }
    }
    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getCdIdHardStr())) {
      mrHardUnitConfigDTO.setResultImport(I18n.getLanguage("mrHardGroupConfig.err.cdIdHardStr"));
      return mrHardUnitConfigDTO;
    }

    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getStationCodeStr())) {
      mrHardUnitConfigDTO
          .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.stationCodeStr"));
      return mrHardUnitConfigDTO;
    }
//    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getNetworkType()) && !StringUtils
////        .isStringNullOrEmpty(mrHardUnitConfigDTO.getDeviceType())) {
////      mrHardUnitConfigDTO
////          .setResultImport(I18n.getLanguage("mrHardGroupConfig.errCont.deviceType"));
////      return mrHardUnitConfigDTO;
////    }
    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getMarketCode())) {
      mrHardUnitConfigDTO
          .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.marketCode.exist"));
      return mrHardUnitConfigDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getArrayCode())) {
      mrHardUnitConfigDTO
          .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.arrayCode.exist"));
      return mrHardUnitConfigDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getCdIdHard())) {
      mrHardUnitConfigDTO
          .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.cdIdHard.exist"));
      return mrHardUnitConfigDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getStationCode())) {
      mrHardUnitConfigDTO
          .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.stationCode.exist"));
      return mrHardUnitConfigDTO;
    }
//    if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getUserMrHard())) {
////      mrHardUnitConfigDTO
////          .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.userMrHard"));
////      return mrHardUnitConfigDTO;
//      UsersInsideDto usersInsideDto = new UsersInsideDto();
//      usersInsideDto.setUsername(mrHardUnitConfigDTO.getUserMrHard());
//      List<UsersInsideDto> userDTOList = userRepository.getListUsersDTOS(usersInsideDto);
//      if (userDTOList == null || userDTOList.isEmpty()) {
//        mrHardUnitConfigDTO
//            .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.userMrHard.exists"));
//        return mrHardUnitConfigDTO;
//      } else {
//        if (!StringUtils.isStringNullOrEmpty(userDTOList.get(0).getIsEnable()) && "0"
//            .equals(userDTOList.get(0).getIsEnable().toString())) {
//          mrHardUnitConfigDTO
//              .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.userMrHard.exists"));
//          return mrHardUnitConfigDTO;
//        }
//      }
//    }
    List<MrITHardScheduleDTO> mrDeviceDTOList = mrITHardScheduleBusiness
        .getListRegionByMrSynItDevices(String.valueOf(mrHardUnitConfigDTO.getMarketCode()));
    if (mrDeviceDTOList != null && !mrDeviceDTOList.isEmpty()) {
      for (MrITHardScheduleDTO dto : mrDeviceDTOList) {
        if (dto.getRegion().equals(mrHardUnitConfigDTO.getRegionStr())) {
          mrHardUnitConfigDTO.setRegion(dto.getRegion());
        }
      }
      if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getRegion())) {
        mrHardUnitConfigDTO
            .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.region.exist"));
        return mrHardUnitConfigDTO;
      }
    } else {
      mrHardUnitConfigDTO
          .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.region.exist"));
      return mrHardUnitConfigDTO;
    }
//    if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getNetworkTypeStr())) {
//      List<MrDeviceDTO> lstNetwork = mrDeviceRepository
//          .getListNetworkTypeByArrayCode(mrHardUnitConfigDTO.getArrayCode());
//      if (lstNetwork != null && !lstNetwork.isEmpty()) {
//        for (MrDeviceDTO dto : lstNetwork) {
//          if (dto.getNetworkType().equals(mrHardUnitConfigDTO.getNetworkTypeStr())) {
//            mrHardUnitConfigDTO.setNetworkType(dto.getNetworkType());
//          }
//        }
//        if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getNetworkType())) {
//          mrHardUnitConfigDTO
//              .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.networkType.exist"));
//          return mrHardUnitConfigDTO;
//        }
//      }
//      if (lstNetwork.isEmpty() || lstNetwork == null) {
//        mrHardUnitConfigDTO
//            .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.networkType.exist"));
//        return mrHardUnitConfigDTO;
//      }
//
    MrHardUnitConfigDTO mrHardGroupConfig = mrHardUnitConfigRepository
        .ckeckMrHardGroupConfigExist(mrHardUnitConfigDTO);
    if (StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getId())
        && mrHardGroupConfig != null) {
      mrHardUnitConfigDTO.setResultImport(I18n.getLanguage("mrHardGroupConfig.err.duplicate"));
      return mrHardUnitConfigDTO;
    } else if (!StringUtils.isStringNullOrEmpty(mrHardUnitConfigDTO.getId())) {
      MrHardUnitConfigDTO dto = mrHardUnitConfigRepository
          .findMrHardGroupConfigById(mrHardUnitConfigDTO.getId());
      if (dto == null) {
        mrHardUnitConfigDTO.setResultImport(I18n.getLanguage("mrHardGroupConfig.err.noData"));
        return mrHardUnitConfigDTO;
      }
      if (dto != null && mrHardGroupConfig != null && !mrHardGroupConfig.getId().toString()
          .equals(mrHardUnitConfigDTO.getId().toString())) {
        mrHardUnitConfigDTO.setResultImport(I18n.getLanguage("mrHardGroupConfig.err.duplicate"));
        return mrHardUnitConfigDTO;
      }
    }

    if (list != null && list.size() > 0 && mrHardUnitConfigDTO.getResultImport() == null) {
      mrHardUnitConfigDTO = validateDuplicate(list, mrHardUnitConfigDTO);
    }

    return mrHardUnitConfigDTO;
  }

  public MrHardUnitConfigDTO validateDuplicate(List<MrHardUnitConfigDTO> list,
      MrHardUnitConfigDTO mrHardUnitConfigDTO) {
    for (int i = 0; i < list.size(); i++) {
      MrHardUnitConfigDTO mrHardUnitConfigDTOTmp = list.get(i);
      if (I18n.getLanguage("mrHardGroupConfig.result.import")
          .equals(mrHardUnitConfigDTOTmp.getResultImport())
          && mrHardUnitConfigDTOTmp.getMarketCode().equals(mrHardUnitConfigDTO.getMarketCode())
          && mrHardUnitConfigDTOTmp.getRegion().equals(mrHardUnitConfigDTO.getRegion())
          && mrHardUnitConfigDTOTmp.getArrayCode().equals(mrHardUnitConfigDTO.getArrayCode())
          && String.valueOf(mrHardUnitConfigDTOTmp.getDeviceType())
          .equals(String.valueOf(mrHardUnitConfigDTOTmp.getDeviceType()))
          && String.valueOf(mrHardUnitConfigDTOTmp.getStationCode())
          .equals(String.valueOf(mrHardUnitConfigDTOTmp.getStationCode()))
      ) {
        mrHardUnitConfigDTO
            .setResultImport(I18n.getLanguage("mrHardGroupConfig.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return mrHardUnitConfigDTO;
  }

}
