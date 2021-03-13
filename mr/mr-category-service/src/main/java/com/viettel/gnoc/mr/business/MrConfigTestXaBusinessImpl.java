package com.viettel.gnoc.mr.business;

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
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrConfigTestXaDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftProcedureDTO;
import com.viettel.gnoc.mr.repository.MrConfigTestXaRepository;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
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
public class MrConfigTestXaBusinessImpl implements MrConfigTestXaBusiness {

  @Autowired
  MrConfigTestXaRepository mrConfigTestXaRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CatLocationBusiness catLocationBusiness;

  @Value("${application.temp.folder}")
  private String tempFolder;

  private Map<String, String> mapCountryName = new HashMap<>();
  private Map<String, String> mapProvinceName = new HashMap<>();
  private Map<String, String> mapDistrictName = new HashMap<>();
  private Map<String, String> mapStation = new HashMap<>();

  @Override
  public Datatable getListMrConfigTestXa(MrConfigTestXaDTO mrConfigTestXaDTO) {
    return mrConfigTestXaRepository.getListMrConfigTestXa(mrConfigTestXaDTO);
  }

  @Override
  public ResultInSideDto insert(MrConfigTestXaDTO mrConfigTestXaDTO) {
    log.debug("Request to insert: {}", mrConfigTestXaDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    if (mrConfigTestXaDTO != null) {
      MrConfigTestXaDTO checkConfig = new MrConfigTestXaDTO();
      checkConfig.setCountry(mrConfigTestXaDTO.getCountry());
      checkConfig.setProvince(mrConfigTestXaDTO.getProvince());
      List<MrConfigTestXaDTO> lstCheckInsert = mrConfigTestXaRepository
          .checkListDTOExisted(checkConfig);
      List<MrConfigTestXaDTO> listDTOS = mrConfigTestXaRepository
          .checkListDTOExisted(mrConfigTestXaDTO);
      boolean isDuplicateCountryAndProvince = !lstCheckInsert.isEmpty();
      boolean isDuplicateConfig = !listDTOS.isEmpty();
      if (isDuplicateCountryAndProvince && isDuplicateConfig) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto.setMessage(I18n.getLanguage("mrConfigTestXa.message.configDuplicate"));
      }
      if (isDuplicateCountryAndProvince && !isDuplicateConfig) {
        mrConfigTestXaDTO.setCreateUser(lstCheckInsert.get(0).getCreateUser());
        mrConfigTestXaDTO.setCreateTime(lstCheckInsert.get(0).getCreateTime());
        mrConfigTestXaDTO.setConfigId(lstCheckInsert.get(0).getConfigId());
        resultInSideDto = update(mrConfigTestXaDTO);
      }
      if (!isDuplicateCountryAndProvince) {
        mrConfigTestXaDTO.setCreateUser(userToken.getUserName());
        mrConfigTestXaDTO.setCreateTime(DateTimeUtils.getSysDateTime());
        resultInSideDto = mrConfigTestXaRepository.insertOrUpdate(mrConfigTestXaDTO);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(MrConfigTestXaDTO mrConfigTestXaDTO) {
    log.debug("Request to insert: {}", mrConfigTestXaDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    if (mrConfigTestXaDTO != null) {
      List<MrConfigTestXaDTO> listDTOS = mrConfigTestXaRepository
          .checkListDTOExisted(mrConfigTestXaDTO);
      if (!listDTOS.isEmpty()) {
        MrConfigTestXaDTO dtoTmp = listDTOS.get(0);
        if (dtoTmp.getConfigId().equals(mrConfigTestXaDTO.getConfigId())) {
          mrConfigTestXaDTO.setUpdateUser(userToken.getUserName());
          mrConfigTestXaDTO.setUpdateTime(DateTimeUtils.getSysDateTime());
          resultInSideDto = mrConfigTestXaRepository.insertOrUpdate(mrConfigTestXaDTO);
        } else {
          resultInSideDto.setKey(RESULT.DUPLICATE);
          resultInSideDto.setMessage(I18n.getLanguage("mrConfigTestXa.message.configDuplicate"));
        }
      } else {
        mrConfigTestXaDTO.setUpdateUser(userToken.getUserName());
        mrConfigTestXaDTO.setUpdateTime(DateTimeUtils.getSysDateTime());
        resultInSideDto = mrConfigTestXaRepository.insertOrUpdate(mrConfigTestXaDTO);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrConfigTestXa(Long configId) {
    log.debug("Request to deleteMrCheckList : {}", configId);
    return mrConfigTestXaRepository.deleteMrConfigTestXa(configId);
  }

  @Override
  public MrConfigTestXaDTO getDetail(Long configId) {
    log.debug("Request to getDetail : {}", configId);
    return mrConfigTestXaRepository.getDetail(configId);
  }

  @Override
  public List<MrConfigTestXaDTO> getListStation() {
    return mrConfigTestXaRepository.getListStation();
  }

  @Override
  public File exportData(MrConfigTestXaDTO mrConfigTestXaDTO) throws Exception {
    List<MrConfigTestXaDTO> mrConfigTestXaDTOS = mrConfigTestXaRepository
        .getDataExport(mrConfigTestXaDTO);
    for (MrConfigTestXaDTO dto : mrConfigTestXaDTOS) {
      if ("1".equalsIgnoreCase(dto.getStatus())) {
        dto.setStatus(I18n.getLanguage("mrConfigTestXa.status.active"));
      } else {
        dto.setStatus(I18n.getLanguage("mrConfigTestXa.status.inactive"));
      }
    }
    return exportFileTemplate(mrConfigTestXaDTOS, "");
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
        I18n.getLanguage("mrConfigTestXa.list.grid.stt"),
        I18n.getLanguage("mrConfigTestXa.country"),
        I18n.getLanguage("mrConfigTestXa.province"),
        I18n.getLanguage("mrConfigTestXa.timeTestXa"),
        I18n.getLanguage("mrConfigTestXa.stationAtATime"),
        I18n.getLanguage("mrConfigTestXa.excepDistrict"),
        I18n.getLanguage("mrConfigTestXa.excepStation"),
        I18n.getLanguage("mrConfigTestXa.status")
    };

    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("mrConfigTestXa.country"),
        I18n.getLanguage("mrConfigTestXa.province"),
        I18n.getLanguage("mrConfigTestXa.timeTestXa"),
        I18n.getLanguage("mrConfigTestXa.stationAtATime"),
        I18n.getLanguage("mrConfigTestXa.status")
    };

    String[] header_LstCountry = new String[]{
        I18n.getLanguage("mrConfigTestXa.list.grid.stt"),
        I18n.getLanguage("mrConfigTestXa.country"),
        I18n.getLanguage("mrConfigTestXa.countryName"),
        I18n.getLanguage("mrConfigTestXa.province"),
        I18n.getLanguage("mrConfigTestXa.provinceName"),
        I18n.getLanguage("mrConfigTestXa.district"),
        I18n.getLanguage("mrConfigTestXa.districtName")
    };

    String[] headerStatus = new String[]{
        I18n.getLanguage("mrConfigTestXa.list.grid.stt"),
        I18n.getLanguage("mrConfigTestXa.status"),
        I18n.getLanguage("mrConfigTestXa.statusName")
    };

    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    List<String> listHeaderLstCountry = Arrays.asList(header_LstCountry);
    List<String> listHeaderStatus = Arrays.asList(headerStatus);
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(3, 3, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(3);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("mrConfigTestXa.title"));
    titleCell.setCellStyle(style.get("title"));

    sheetParam.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstCountry.size() - 1));
    Row titleRow_lstcountry = sheetParam.createRow(1);
    titleRow_lstcountry.setHeightInPoints(25);
    Cell titleCell1 = titleRow_lstcountry.createCell(0);
    titleCell1.setCellValue(I18n.getLanguage("mrConfigTestXa.lstCountry.title"));
    titleCell1.setCellStyle(style.get("title"));

    sheetParam1.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderStatus.size() - 1));
    Row titleRow_lstStatus = sheetParam1.createRow(1);
    titleRow_lstStatus.setHeightInPoints(25);
    Cell titleStatusCell = titleRow_lstStatus.createCell(0);
    titleStatusCell.setCellValue(I18n.getLanguage("mrConfigTestXa.status.title"));
    titleStatusCell.setCellStyle(style.get("title"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header
    Row headerRow = sheetOne.createRow(7);
    Row headerListCountry = sheetParam.createRow(3);
    Row headerSheetStatus = sheetParam1.createRow((3));

    headerRow.setHeightInPoints(16);

    int len = listHeader.size();
    for (int i = 0; i < len; i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }

      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      if (i != 0) {
        sheetOne.setColumnWidth(i, 6000);
      }
    }

    //Của sheet Country
    Cell headerCellStt = headerListCountry.createCell(0);
    Cell headerCellCountry = headerListCountry.createCell(1);
    Cell headerCellCountryName = headerListCountry.createCell(2);
    Cell headerCellProvince = headerListCountry.createCell(3);
    Cell headerCellProvinceName = headerListCountry.createCell(4);
    Cell headerCellDistrict = headerListCountry.createCell(5);
    Cell headerCellDistrictName = headerListCountry.createCell(6);

    XSSFRichTextString stt = new XSSFRichTextString(
        I18n.getLanguage("mrConfigTestXa.list.grid.stt"));
    XSSFRichTextString country = new XSSFRichTextString(
        I18n.getLanguage("mrConfigTestXa.country"));
    XSSFRichTextString countryName = new XSSFRichTextString(
        I18n.getLanguage("mrConfigTestXa.countryName"));
    XSSFRichTextString province = new XSSFRichTextString(
        I18n.getLanguage("mrConfigTestXa.province"));
    XSSFRichTextString provinceName = new XSSFRichTextString(
        I18n.getLanguage("mrConfigTestXa.provinceName"));
    XSSFRichTextString district = new XSSFRichTextString(
        I18n.getLanguage("mrConfigTestXa.district"));
    XSSFRichTextString districtName = new XSSFRichTextString(
        I18n.getLanguage("mrConfigTestXa.districtName"));

    headerCellStt.setCellValue(stt);
    headerCellStt.setCellStyle(style.get("header"));
    headerCellCountry.setCellValue(country);
    headerCellCountry.setCellStyle(style.get("header"));
    headerCellCountryName.setCellValue(countryName);
    headerCellCountryName.setCellStyle(style.get("header"));
    headerCellProvince.setCellValue(province);
    headerCellProvince.setCellStyle(style.get("header"));
    headerCellProvinceName.setCellValue(provinceName);
    headerCellProvinceName.setCellStyle(style.get("header"));
    headerCellDistrict.setCellValue(district);
    headerCellDistrict.setCellStyle(style.get("header"));
    headerCellDistrictName.setCellValue(districtName);
    headerCellDistrictName.setCellStyle(style.get("header"));

    sheetParam.setColumnWidth(1, 5000);
    sheetParam.setColumnWidth(2, 5000);
    sheetParam.setColumnWidth(3, 5000);
    sheetParam.setColumnWidth(4, 5000);
    sheetParam.setColumnWidth(5, 5000);
    sheetParam.setColumnWidth(6, 5000);

    //sheet Status
    Cell headerStatusStt = headerSheetStatus.createCell(0);
    Cell headerCellStatus = headerSheetStatus.createCell(1);
    Cell headerCellStatusName = headerSheetStatus.createCell(2);

    XSSFRichTextString statusStt = new XSSFRichTextString(
        I18n.getLanguage("mrConfigTestXa.list.grid.stt"));
    XSSFRichTextString status = new XSSFRichTextString(
        I18n.getLanguage("mrConfigTestXa.status"));
    XSSFRichTextString statusName = new XSSFRichTextString(
        I18n.getLanguage("mrConfigTestXa.statusName"));

    headerStatusStt.setCellValue(statusStt);
    headerStatusStt.setCellStyle(style.get("header"));
    headerCellStatus.setCellValue(status);
    headerCellStatus.setCellStyle(style.get("header"));
    headerCellStatusName.setCellValue(statusName);
    headerCellStatusName.setCellStyle(style.get("header"));

    // Set dữ liệu vào từng sheet
    //-->sheet List Quoc Gia
    int row = 4;
    int k = 1;
    List<ItemDataCRInside> countryNameList = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    for (ItemDataCRInside dto : countryNameList) {
      List<ItemDataCRInside> provinceNameList = catLocationBusiness
          .getListLocationByLevelCBB(null, 3L, dto.getValueStr());
      excelWriterUtils.createCell(sheetParam, 0, row, String.valueOf(k), style.get("cell"));
      excelWriterUtils
          .createCell(sheetParam, 1, row, String.valueOf(dto.getValueStr()), style.get("cell"));
      excelWriterUtils.createCell(sheetParam, 2, row, dto.getDisplayStr(), style.get("cell"));
      if (provinceNameList.size() > 0) {
        for (ItemDataCRInside provinceDTO : provinceNameList) {
          List<ItemDataCRInside> districtNameList = catLocationBusiness
              .getListLocationByLevelCBB(null, 4L, provinceDTO.getValueStr());
          excelWriterUtils
              .createCell(sheetParam, 3, row, String.valueOf(provinceDTO.getValueStr()),
                  style.get("cell"));
          excelWriterUtils
              .createCell(sheetParam, 4, row, provinceDTO.getDisplayStr(), style.get("cell"));
          if (districtNameList.size() > 0) {
            for (ItemDataCRInside districtDTO : districtNameList) {
              excelWriterUtils
                  .createCell(sheetParam, 5, row, String.valueOf(districtDTO.getValueStr()),
                      style.get("cell"));
              excelWriterUtils
                  .createCell(sheetParam, 6, row, districtDTO.getDisplayStr(), style.get("cell"));
              row++;
            }
          } else {
            row++;
          }
        }
      } else {
        row++;
      }
      k++;
    }
    sheetParam.autoSizeColumn(0);

    //-->sheet status
    excelWriterUtils.createCell(sheetParam1, 0, 4, "1", style.get("cell"));
    excelWriterUtils.createCell(sheetParam1, 1, 4, "1", style.get("cell"));
    excelWriterUtils.createCell(sheetParam1, 2, 4, I18n.getLanguage("mrConfigTestXa.status.active"),
        style.get("cell"));
    excelWriterUtils.createCell(sheetParam1, 0, 5, "2", style.get("cell"));
    excelWriterUtils.createCell(sheetParam1, 1, 5, "2", style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam1, 2, 5, I18n.getLanguage("mrConfigTestXa.status.inactive"),
            style.get("cell"));

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("mrConfigTestXa.title"));
    workbook.setSheetName(1, I18n.getLanguage("mrConfigTestXa.list.location"));
    workbook.setSheetName(2, I18n.getLanguage("mrConfigTestXa.status.sheetName"));

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "MR_CONFIG_TESTXA_TEMPLATE_EXPORT.xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importData(MultipartFile uploadFile) throws Exception {
    List<MrConfigTestXaDTO> exportMrConfigTestXaDTOList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    try {
      if (uploadFile == null || uploadFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      }
      String filePath = FileUtils
          .saveTempFile(uploadFile.getOriginalFilename(), uploadFile.getBytes(),
              tempFolder);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        return resultInSideDto;
      }
      File fileImport = new File(filePath);

      List<Object[]> headerList;
      headerList = CommonImport.getDataFromExcelFile(
          fileImport,
          0,
          7,
          0,
          7,
          1000
      );
      //Kiểm tra form header có đúng chuẩn
      if (headerList.size() == 0 || !validFileFormat(headerList)) {
        resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
        return resultInSideDto;
      }

      //Lấy dữ liệu import
      List<Object[]> dataImportList = CommonImport.getDataFromExcelFileNew(
          fileImport,
          0,
          8,
          0,
          7,
          1000
      );

      if (dataImportList.size() > 1000) {
        resultInSideDto.setKey(RESULT.DATA_OVER);
        return resultInSideDto;
      }

      if (dataImportList.isEmpty()) {
        resultInSideDto.setKey(RESULT.NODATA);
        resultInSideDto.setMessage(Constants.FILE_NULL);
        File fileExport = exportFileTemplate(exportMrConfigTestXaDTOList, Constants.RESULT_IMPORT);
        resultInSideDto.setFile(fileExport);
        resultInSideDto.setFilePath(fileExport.getPath());
        return resultInSideDto;
      }

      List<MrConfigTestXaDTO> mrConfigTestXaDTOInsert = new ArrayList<>();

      setMapCountryName();
      setMapStation();

      int index = 0;

      for (Object[] obj : dataImportList) {

        MrConfigTestXaDTO mrConfigTestXaDTO = new MrConfigTestXaDTO();
        if (obj[1] != null) {
          mrConfigTestXaDTO.setCountry(obj[1].toString().trim());
          if (!mapCountryName.containsKey(obj[1].toString().trim())) {
            mrConfigTestXaDTO
                .setResultImport(I18n.getValidation("mrConfigTestXa.doesNotExist.country"));
          } else {
            setMapProvinceName(Long.valueOf(obj[1].toString().trim()));
          }
        }
        if (obj[2] != null) {
          mrConfigTestXaDTO.setProvince(obj[2].toString().trim());
          if (!mapProvinceName.containsKey(obj[2].toString().trim())) {
            mrConfigTestXaDTO
                .setResultImport(I18n.getValidation("mrConfigTestXa.doesNotExist.province"));
          } else {
            setMapDistrictName(Long.valueOf(obj[2].toString().trim()));
          }

        }
        if (obj[3] != null) {
          mrConfigTestXaDTO.setTimeTestXa(obj[3].toString().trim());
        }
        if (obj[4] != null) {
          mrConfigTestXaDTO.setStationAtATime(obj[4].toString().trim());
        }
        if (obj[5] != null) {
          String[] convertStringToArray = obj[5].toString().trim().split(",");
          List<String> excepDistrict = new ArrayList<>();
          for (String district : convertStringToArray) {
            excepDistrict.add(district.trim());
            if (!mapDistrictName.containsKey(district.trim())) {
              mrConfigTestXaDTO
                  .setResultImport(I18n.getValidation("mrConfigTestXa.doesNotExist.district") + " : " + district.trim());
            }
          }
          mrConfigTestXaDTO.setExcepDistrict(String.join(",", excepDistrict));
        }
        if (obj[6] != null) {
          String[] convertStringToArray = obj[6].toString().trim().split(",");
          List<String> excepStation = new ArrayList<>();
          for (String station : convertStringToArray) {
            excepStation.add(station.trim());
            if (!mapStation.containsKey(station.trim())) {
              mrConfigTestXaDTO
                  .setResultImport(I18n.getValidation("mrConfigTestXa.doesNotExist.station")+ " : " + station.trim());
            }
          }
          mrConfigTestXaDTO.setExcepStation(String.join(",", excepStation));
        }
        if (obj[7] != null) {
          if ("1".equalsIgnoreCase(obj[7].toString().trim()) || "2"
              .equalsIgnoreCase(obj[7].toString().trim())) {
            mrConfigTestXaDTO.setStatus(obj[7].toString().trim());
          }
        }

        MrConfigTestXaDTO mrConfigTestXaDTOTmp = new MrConfigTestXaDTO();
        mrConfigTestXaDTOTmp = validateImportInfo(mrConfigTestXaDTO);

        if (mrConfigTestXaDTOTmp.getResultImport() != null) {
          exportMrConfigTestXaDTOList.add(mrConfigTestXaDTOTmp);
          index++;
        } else {
          mrConfigTestXaDTOTmp.setResultImport(I18n.getValidation("mrConfigTestXa.result.import"));
          exportMrConfigTestXaDTOList.add(mrConfigTestXaDTOTmp);
          mrConfigTestXaDTOInsert.add(mrConfigTestXaDTO);
        }
      }

      if (index == 0) {
        if (!mrConfigTestXaDTOInsert.isEmpty()) {
          for (MrConfigTestXaDTO dtoInsert : mrConfigTestXaDTOInsert) {
            resultInSideDto = insert(dtoInsert);
          }
        }
      } else {
        File fileExport = exportFileTemplate(exportMrConfigTestXaDTOList, Constants.RESULT_IMPORT);
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setFile(fileExport);
        resultInSideDto.setFilePath(fileExport.getPath());
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }

  private File exportFileTemplate(List<MrConfigTestXaDTO> lstData, String code) throws Exception {
    String title = "";
    String sheetName = "";
    String fileNameOut = "";
    ConfigFileExport configfileExport = null;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet = null;
    if (Constants.RESULT_IMPORT.equals(code)) {
      lstHeaderSheet = renderHeaderSheet("country", "province", "timeTestXa",
          "stationAtATime", "excepDistrict", "excepStation", "status", "resultImport");
      title = I18n.getLanguage("mrConfigTestXa.import");
      sheetName = title;
      fileNameOut = "MRCONFIGTESTXA_RESULT_IMPORT";
    } else {
      lstHeaderSheet = renderHeaderSheet("country", "province", "timeTestXa",
          "stationAtATime", "excepDistrict", "excepStation", "status");
      title = I18n.getLanguage("mrConfigTestXa.list.grid");
      sheetName = title;
      fileNameOut = "MRCONFIGTESTXA_RESULT_EXPORT";
    }
    configfileExport = new ConfigFileExport(
        lstData
        , sheetName
        , title
        , null
        , 7
        , 3
        , lstHeaderSheet.size()
        , true
        , "language.mrConfigTestXa"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , I18n.getLanguage("mrConfigTestXa.firstLeftHeader")
        , I18n.getLanguage("mrConfigTestXa.secondLeftHeader")
        , I18n.getLanguage("mrConfigTestXa.firstRightHeader")
        , I18n.getLanguage("mrConfigTestXa.secondRightHeader")
    );
    cellSheet = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("mrConfigTestXa.list.grid.stt"),
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
    if (!I18n.getLanguage("mrConfigTestXa.list.grid.stt")
        .equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrConfigTestXa.country") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrConfigTestXa.province") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrConfigTestXa.timeTestXa") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrConfigTestXa.stationAtATime") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrConfigTestXa.excepDistrict"))
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrConfigTestXa.excepStation"))
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrConfigTestXa.status") + "*")
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    return true;
  }

  private void setMapCountryName() {
    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstCountryName) {
        mapCountryName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }

  private void setMapProvinceName(Long parenId) {
    List<ItemDataCRInside> lstProvinceName = catLocationBusiness
        .getListLocationByLevelCBB(null, 3L, parenId);
    if (lstProvinceName != null && !lstProvinceName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstProvinceName) {
        mapProvinceName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }

  private void setMapDistrictName(Long parenId) {
    List<ItemDataCRInside> lstDistrictName = catLocationBusiness
        .getListLocationByLevelCBB(null, 4L, parenId);
    if (lstDistrictName != null && !lstDistrictName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstDistrictName) {
        mapDistrictName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }

  private void setMapStation() {
    List<MrConfigTestXaDTO> lstStation = mrConfigTestXaRepository
        .getListStation();
    if (lstStation != null && !lstStation.isEmpty()) {
      for (MrConfigTestXaDTO dto : lstStation) {
        mapStation.put(dto.getStation(), dto.getStation());
      }
    }
  }

  private MrConfigTestXaDTO validateImportInfo(MrConfigTestXaDTO mrConfigTestXaDTO) {
    if (StringUtils.isNotNullOrEmpty(mrConfigTestXaDTO.getResultImport())) {
      return mrConfigTestXaDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrConfigTestXaDTO.getCountry())) {
      mrConfigTestXaDTO.setResultImport(I18n.getValidation("mrConfigTestXa.null.country"));
      return mrConfigTestXaDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrConfigTestXaDTO.getProvince())) {
      mrConfigTestXaDTO.setResultImport(I18n.getValidation("mrConfigTestXa.null.province"));
      return mrConfigTestXaDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrConfigTestXaDTO.getTimeTestXa())) {
      mrConfigTestXaDTO.setResultImport(I18n.getValidation("mrConfigTestXa.null.timeTestXa"));
      return mrConfigTestXaDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrConfigTestXaDTO.getStationAtATime())) {
      mrConfigTestXaDTO.setResultImport(I18n.getValidation("mrConfigTestXa.null.stationAtATime"));
      return mrConfigTestXaDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrConfigTestXaDTO.getStatus())) {
      mrConfigTestXaDTO.setResultImport(I18n.getValidation("mrConfigTestXa.null.status"));
      return mrConfigTestXaDTO;
    }
    if (!"1".equalsIgnoreCase(mrConfigTestXaDTO.getStatus()) && !"2"
        .equalsIgnoreCase(mrConfigTestXaDTO.getStatus())) {
      mrConfigTestXaDTO.setResultImport(I18n.getValidation("mrConfigTestXa.exist.status"));
      return mrConfigTestXaDTO;
    }
    List<MrConfigTestXaDTO> lst = new ArrayList<>();
    lst = mrConfigTestXaRepository.checkListDTOExisted(mrConfigTestXaDTO);
    if (!lst.isEmpty()) {
      mrConfigTestXaDTO.setResultImport(I18n.getValidation("mrConfigTestXa.duplicate"));
      return mrConfigTestXaDTO;
    }
    return mrConfigTestXaDTO;
  }
}
