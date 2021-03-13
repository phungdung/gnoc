package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrMaterialDTO;
import com.viettel.gnoc.maintenance.dto.MrMaterialDisplacementDTO;
import com.viettel.gnoc.mr.repository.MrMaterialDisplacementRepository;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

@Service
@Transactional
@Slf4j
public class MrMaterialDisplacementBusinessImpl implements MrMaterialDisplacementBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  private final static String MATERIAL_REPLACE_EXPORT = "MATERIAL_REPLACE_EXPORT";
  private final static String MATERIAL_RESULT_IMPORT = "MATERIAL_RESULT_IMPORT";
  private Map<String, String> mapCheckDublicateInFile = new HashMap<>();
  @Autowired
  private MrMaterialDisplacementRepository mrMaterialDisplacementRepository;

  @Autowired
  private CatLocationBusiness catLocationBusiness;

  @Autowired
  private CatItemRepository catItemRepository;

  private Map<String, String> mapMaterialName = new HashMap<>();
  private Map<String, String> mapCountryName = new HashMap<>();

  @Override
  public List<MrMaterialDisplacementDTO> getListMrMaterialDisplacementDTO(
      MrMaterialDisplacementDTO mrMaterialDisplacementDTO) {
    log.debug("Request to getListMrMaterialDisplacementDTO: {}", mrMaterialDisplacementDTO);
    return mrMaterialDisplacementRepository
        .getListMrMaterialDisplacementDTO(mrMaterialDisplacementDTO);
  }


  @Override
  public Datatable getListMrMaterialDTO2(MrMaterialDTO mrMaterialDTO) {
    log.debug("Request to getListMrMaterialDTO2: {}", mrMaterialDTO);
    Datatable datatable = mrMaterialDisplacementRepository.getListMrMaterialDTO2(mrMaterialDTO);
    List<MrMaterialDTO> lst = (List<MrMaterialDTO>) datatable.getData();
    List<MrMaterialDTO> lstResult = new ArrayList<>();
    if (lst != null && !lst.isEmpty()) {
      for (MrMaterialDTO dto : lst) {
        lstResult.add(resultDTO(dto));
      }
      datatable.setData(lstResult);
    }
    return datatable;
  }

  private MrMaterialDTO resultDTO(MrMaterialDTO dto) {
    if (!StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
      dto.setDeviceTypeName(findDeviceTypeName(dto.getDeviceType()));
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getDateTime())) {
      try {
        dto.setDateTimeStr(DateUtil.date2MMyyString(dto.getDateTime()));
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return dto;
  }

  @Override
  public MrMaterialDTO getDetail(Long id) {
    log.debug("Request to getDetail: {}", id);
    return mrMaterialDisplacementRepository.getDetail(id);
  }

  @Override
  public File exportData(MrMaterialDTO mrMaterialDTO) throws Exception {
    log.info("Request to exportData : {}", mrMaterialDTO);
    List<MrMaterialDTO> lstEx = mrMaterialDisplacementRepository.getDataExport(mrMaterialDTO);
    List<MrMaterialDTO> lstResult = new ArrayList<>();
    if (lstEx != null && !lstEx.isEmpty()) {
      for (MrMaterialDTO dto : lstEx) {
        lstResult.add(resultDTO(dto));
      }
    }
    return exportFileEx(lstResult, "export");
  }

  @Override
  public ResultInSideDto insertMrMaterial(MrMaterialDTO mrMaterialDTO) {
    log.debug("Request to insertMrMaterial: {}", mrMaterialDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (checkDuplicated(mrMaterialDTO, true)) {
      resultInSideDto.setKey(RESULT.DUPLICATE);
      resultInSideDto.setMessage(I18n.getValidation("MrMaterialDTO.unique"));
      return resultInSideDto;
    }
    resultInSideDto = mrMaterialDisplacementRepository
        .insertOrUpdateMrMaterial(mrMaterialDTO);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      resultInSideDto.setMessage(I18n.getValidation("common.insert.success"));
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getValidation("common.insert.fail"));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateMrMaterial(MrMaterialDTO mrMaterialDTO) {
    log.debug("Request to UpdateMrMaterial: {}", mrMaterialDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (checkDuplicated(mrMaterialDTO, false)) {
      resultInSideDto.setKey(RESULT.DUPLICATE);
      resultInSideDto.setMessage(I18n.getValidation("MrMaterialDTO.unique"));
      return resultInSideDto;
    }
    if (!StringUtils.isStringNullOrEmpty(mrMaterialDTO.getMaterialId())) {
      resultInSideDto = mrMaterialDisplacementRepository
          .insertOrUpdateMrMaterial(mrMaterialDTO);
      if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
        resultInSideDto.setMessage(I18n.getValidation("common.update.success"));
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getValidation("common.update.fail"));
      }
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrMaterial(Long id) {
    log.debug("Request to deleteMrMaterial: {}", id);
    ResultInSideDto resultInSideDto = mrMaterialDisplacementRepository.deleteMrMaterial(id);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      resultInSideDto.setMessage(I18n.getValidation("common.delete.success"));
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getValidation("common.delete.fail"));
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
    XSSFSheet sheetParam2 = workbook.createSheet("param2");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MATERIAL.materialNameEN"),
        I18n.getLanguage("MATERIAL.deviceTypeName"),
        I18n.getLanguage("MATERIAL.materialName"),
        I18n.getLanguage("MATERIAL.serial"),
        I18n.getLanguage("MATERIAL.unitPrice"),
        I18n.getLanguage("MATERIAL.dateTimeStr")

    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("MATERIAL.materialNameEN"),
        I18n.getLanguage("MATERIAL.deviceTypeName"),
        I18n.getLanguage("MATERIAL.materialName"),
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int deviceTypeNameColumn = listHeader.indexOf(I18n.getLanguage("MATERIAL.deviceTypeName"));
    int materialNameColum = listHeader.indexOf(I18n.getLanguage("MATERIAL.materialNameEN"));
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);
    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("MATERIAL.title"));
    titleCell.setCellStyle(style.get("title"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(7);
    Row headerNation = sheetParam1.createRow(0);
    Row headerDevice = sheetParam2.createRow(0);
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

    // Set dữ liệu vào column dropdown

    int row = 5;
    List<ItemDataCRInside> lstCountry = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
//    for (ItemDataCRInside nationName : lstCountry) {
//      excelWriterUtils
//          .createCell(sheetParam, 1, row++, nationName.getDisplayStr(), style.get("cell"));
//    }
//    sheetParam.autoSizeColumn(0);
//    Name materialNameEN = workbook.createName();
//    materialNameEN.setNameName("materialNameEN");
//    materialNameEN.setRefersToFormula("param!$B$2:$B$" + row);
//    XSSFDataValidationConstraint materialNameConstraint = new XSSFDataValidationConstraint(
//        DataValidationConstraint.ValidationType.LIST, "materialNameEN");
//    CellRangeAddressList materialNameCreate = new CellRangeAddressList(5, 65000,
//        materialNameColum,
//        materialNameColum);
//    XSSFDataValidation dataValidationMaterialName = (XSSFDataValidation) dataValidationHelper
//        .createValidation(
//            materialNameConstraint, materialNameCreate);
//    dataValidationMaterialName.setShowErrorBox(true);
//    sheetOne.addValidationData(dataValidationMaterialName);
//
//    row = 5;
//    for (String deviceTypeCd : Constants.DEVICE_TYPE_LST) {
//      excelWriterUtils
//          .createCell(sheetParam, 2, row++, findDeviceTypeName(deviceTypeCd), style.get("cell"));
//    }
//    sheetParam.autoSizeColumn(1);
//    Name deviceTypeName = workbook.createName();
//    deviceTypeName.setNameName("deviceTypeName");
//    deviceTypeName.setRefersToFormula("param!$C$2:$C$" + row);
//    XSSFDataValidationConstraint deviceTypeNameConstraint = new XSSFDataValidationConstraint(
//        DataValidationConstraint.ValidationType.LIST, "deviceTypeName");
//    CellRangeAddressList deviceTypeNameCreate = new CellRangeAddressList(5, 65000,
//        deviceTypeNameColumn,
//        deviceTypeNameColumn);
//    XSSFDataValidation dataValidationDeviceTypeName = (XSSFDataValidation) dataValidationHelper
//        .createValidation(
//            deviceTypeNameConstraint, deviceTypeNameCreate);
//    dataValidationDeviceTypeName.setShowErrorBox(true);
//    sheetOne.addValidationData(dataValidationDeviceTypeName);

//    tab danh sách quốc gia
    //    header Market
    String[] headerMarket = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MATERIAL.materialCode"),
        I18n.getLanguage("MATERIAL.materialNameEN")};
    List<String> listHeaderMarket = Arrays.asList(headerMarket);
    for (int i = 0; i < listHeaderMarket.size(); i++) {
      Cell headerCell = headerNation.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeaderMarket.get(i));
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      sheetParam1.setColumnWidth(0, 15000);
    }

    row = 1;
    for (ItemDataCRInside marketCode : lstCountry) {
      excelWriterUtils
          .createCell(sheetParam1, 0, row, String.valueOf(row), style.get("cell"));
      excelWriterUtils
          .createCell(sheetParam1, 1, row,
              StringUtils.isStringNullOrEmpty(marketCode.getValueStr()) ? null
                  : marketCode.getValueStr().toString(), style.get("cell"));
      excelWriterUtils
          .createCell(sheetParam1, 2, row, marketCode.getDisplayStr(), style.get("cell"));
      row++;
    }
    sheetParam1.setColumnWidth(0, 1500);
    sheetParam1.setColumnWidth(1, 15000);
    sheetParam1.setColumnWidth(2, 15000);

//    tab danh sách device
//    header Device
    String[] headerDeviceName = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MATERIAL.deviceType"),
        I18n.getLanguage("MATERIAL.deviceTypeName")};
    List<String> listHeaderDevice = Arrays.asList(headerDeviceName);
    for (int i = 0; i < listHeaderDevice.size(); i++) {
      Cell headerCell = headerDevice.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeaderDevice.get(i));
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      sheetParam2.setColumnWidth(0, 15000);
    }

    row = 1;
    for (String deviceTypeCd : Constants.DEVICE_TYPE_LST) {
      excelWriterUtils
          .createCell(sheetParam2, 0, row, String.valueOf(row), style.get("cell"));
      excelWriterUtils
          .createCell(sheetParam2, 1, row, deviceTypeCd, style.get("cell"));
      excelWriterUtils
          .createCell(sheetParam2, 2, row, findDeviceTypeName(deviceTypeCd), style.get("cell"));
      row++;
    }
    sheetParam2.setColumnWidth(0, 1500);
    sheetParam2.setColumnWidth(1, 15000);
    sheetParam2.setColumnWidth(2, 15000);

    row = 1;
    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("MATERIAL.title"));
    workbook.setSheetName(2, I18n.getLanguage("MATERIAL.materialNameEN"));
    workbook.setSheetName(3, I18n.getLanguage("MATERIAL.deviceTypeName"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName =
        "IMPORT_MR_MATERIAL_DISPLACEMENT" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<MrMaterialDTO> exportDTOList = new ArrayList<>();
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(RESULT.SUCCESS);
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultDto.setKey(RESULT.FILE_IS_NULL);
        return resultDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultDto.getKey())) {
          return resultDto;
        }
        File fileImport = new File(filePath);

        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            8,
            1000
        );
        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultDto;
        }
        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFileNew(
            fileImport,
            0,
            8,
            0,
            8,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultDto.setKey(RESULT.DATA_OVER);
          return resultDto;
        }

        List<MrMaterialDTO> mrMaterialDTOArrayList = new ArrayList<>();
        setMapCountryName();
        if (!dataImportList.isEmpty()) {
          int index = 0;
          setMapMaterialName();
          mapCheckDublicateInFile.clear();
          int i = 0;
          for (Object[] obj : dataImportList) {
            i++;
            MrMaterialDTO mrMaterialDTO = new MrMaterialDTO();
            if (obj[1] != null) {
              mrMaterialDTO.setMaterialNameEN(obj[1].toString().trim());
              for (Map.Entry<String, String> item : mapCountryName.entrySet()) {
                if (mrMaterialDTO.getMaterialNameEN().equals(item.getKey())) {
                  mrMaterialDTO.setMarketCode(Long.parseLong(item.getKey()));
                  break;
                } else {
                  mrMaterialDTO.setMarketCode(null);
                }
              }
            } else {
              mrMaterialDTO.setMaterialNameEN(null);
            }
            if (obj[2] != null) {
              mrMaterialDTO.setDeviceTypeName(obj[2].toString().trim());
              for (Map.Entry<String, String> item : getDeviceTypeCBB().entrySet()) {
                if (mrMaterialDTO.getDeviceTypeName().equals(item.getKey())) {
                  mrMaterialDTO.setDeviceType(item.getKey());
                  break;
                } else {
                  mrMaterialDTO.setDeviceType(null);
                }
              }
            } else {
              mrMaterialDTO.setDeviceTypeName(null);
            }
            if (obj[3] != null) {
              mrMaterialDTO.setMaterialName(obj[3].toString().trim());
            } else {
              mrMaterialDTO.setMaterialName(null);
            }
            if (obj[4] != null) {
              mrMaterialDTO.setSerial(obj[4].toString().trim());
            } else {
              mrMaterialDTO.setSerial(null);
            }
            if (obj[5] != null) {
              mrMaterialDTO.setUnitPriceStr(obj[5].toString().trim());
              if (!DataUtil.isNumber(obj[5].toString().trim())) {
                mrMaterialDTO
                    .setResultImport(I18n.getValidation("MrMaterialDTO.errType.unitPrice"));
              } else {
                try {
                  mrMaterialDTO.setUnitPrice(Long.parseLong(obj[5].toString().trim()));
                } catch (Exception e) {
                  mrMaterialDTO
                      .setResultImport(I18n.getValidation("MrMaterialDTO.errType.unitPrice"));
                }
              }
            } else {
              mrMaterialDTO.setUnitPrice(null);
            }
            if (obj[6] != null) {
              try {
                String dateTimeStr = obj[6].toString().trim();
                String[] dateTime = dateTimeStr.split("/");
                if (dateTime.length == 2) {
                  SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
                  mrMaterialDTO.setDateTime(df.parse(obj[6].toString().trim()));
                  mrMaterialDTO.setDateTimeStr(obj[6].toString().trim());
                } else if (dateTime.length == 3) {
                  Date date = DateUtil.string2Date(obj[6].toString().trim());
                  mrMaterialDTO.setDateTime(date);
                  mrMaterialDTO.setDateTimeStr(DateUtil.date2MMyyString(date));
                } else {
                  mrMaterialDTO.setDateTimeStr(obj[6].toString().trim());
                  mrMaterialDTO
                      .setResultImport(
                          I18n.getValidation("MrMaterialDTO.err.dateTime.validateForm"));
                }
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                mrMaterialDTO.setDateTimeStr(obj[6].toString().trim());
                mrMaterialDTO
                    .setResultImport(I18n.getValidation("MrMaterialDTO.err.dateTime.validateForm"));
              }
            } else {
              mrMaterialDTO.setDateTime(null);
            }
            MrMaterialDTO mrMaterialDTOTmp = new MrMaterialDTO();
            if (StringUtils.isStringNullOrEmpty(mrMaterialDTO.getResultImport())) {
              mrMaterialDTOTmp = validateImportInfo(mrMaterialDTO, i);
            } else {
              mrMaterialDTOTmp = mrMaterialDTO.clone();
            }
            if (mrMaterialDTOTmp.getResultImport() == null) {
              mrMaterialDTOTmp.setResultImport(I18n.getValidation("MrMaterialDTO.result.import"));
              exportDTOList.add(mrMaterialDTOTmp);
              mrMaterialDTOArrayList.add(mrMaterialDTO);
            } else {
              exportDTOList.add(mrMaterialDTOTmp);
              index++;
            }
          }
          if (index == 0) {
            if (!mrMaterialDTOArrayList.isEmpty()) {
              for (MrMaterialDTO dtoInsert : mrMaterialDTOArrayList) {
                resultDto = mrMaterialDisplacementRepository.insertOrUpdateMrMaterial(dtoInsert);
              }
            }
          } else {
            File fileExport = exportFileEx(exportDTOList, Constants.RESULT_IMPORT);
            resultDto.setKey(RESULT.ERROR);
            resultDto.setFile(fileExport);
            resultDto.setFilePath(fileExport.getPath());
          }
        } else {
          resultDto.setKey(RESULT.NODATA);
          resultDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileEx(exportDTOList, Constants.RESULT_IMPORT);
          resultDto.setFile(fileExport);
          resultDto.setFilePath(fileExport.getPath());
          return resultDto;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDto.setKey(RESULT.ERROR);
      resultDto.setMessage(e.getMessage());
    }
    return resultDto;
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

  private MrMaterialDTO validateImportInfo(MrMaterialDTO mrMaterialDTO, int i) {
    if (StringUtils.isStringNullOrEmpty(mrMaterialDTO.getMaterialNameEN())) {
      mrMaterialDTO.setResultImport(I18n.getValidation("MrMaterialDTO.null.materialNameEN"));
      return mrMaterialDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrMaterialDTO.getMarketCode())) {
      mrMaterialDTO.setResultImport(I18n.getValidation("MrMaterialDTO.notexits.marketCode"));
      return mrMaterialDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrMaterialDTO.getDeviceTypeName())) {
      mrMaterialDTO.setResultImport(I18n.getValidation("MrMaterialDTO.null.deviceTypeName"));
      return mrMaterialDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrMaterialDTO.getDeviceType())) {
      mrMaterialDTO.setResultImport(I18n.getValidation("MrMaterialDTO.notexits.deviceType"));
      return mrMaterialDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrMaterialDTO.getMaterialName())) {
      mrMaterialDTO.setResultImport(I18n.getValidation("MrMaterialDTO.null.materialName"));
      return mrMaterialDTO;
    }
    if (mrMaterialDTO.getMaterialName().length() > 128) {
      mrMaterialDTO.setResultImport(I18n.getValidation("MrMaterialDTO.tooLong.materialName"));
      return mrMaterialDTO;
    }
    if (StringUtils.isNotNullOrEmpty(mrMaterialDTO.getSerial())
        && mrMaterialDTO.getSerial().length() > 128) {
      mrMaterialDTO.setResultImport(I18n.getValidation("MrMaterialDTO.tooLong.serial"));
      return mrMaterialDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrMaterialDTO.getUnitPrice())
        && mrMaterialDTO.getUnitPrice() > 999999999L) {
      mrMaterialDTO.setResultImport(I18n.getValidation("MrMaterialDTO.unitPrice.tooLong"));
      return mrMaterialDTO;
    }
    if (checkDuplicated(mrMaterialDTO, true)) {
      mrMaterialDTO.setResultImport(I18n.getValidation("MrMaterialDTO.unique"));
      return mrMaterialDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrMaterialDTO.getResultImport())) {
      String key = mrMaterialDTO.getMarketCode() + mrMaterialDTO.getDeviceType() + mrMaterialDTO
          .getMaterialName();
      if (mapCheckDublicateInFile.get(key) != null) {
        mrMaterialDTO.setResultImport(I18n.getValidation("MrMaterialDTO.unique.file")
            .replaceAll("0", mapCheckDublicateInFile.get(key)));
      } else {
        mapCheckDublicateInFile
            .put(key, String.valueOf(i));
      }
    }
    return mrMaterialDTO;
  }

  private void setMapMaterialName() {
    Datatable woGroupTypeMaster = catItemRepository
        .getItemMaster("OPEN_PM.MR_MATERIAL", LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), "itemId", "itemName");
    List<CatItemDTO> itemDTOList = (List<CatItemDTO>) woGroupTypeMaster.getData();
    if (itemDTOList != null && !itemDTOList.isEmpty()) {
      for (CatItemDTO catItemDTO : itemDTOList) {
        mapMaterialName.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
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
    if (!I18n.getLanguage("common.STT")
        .equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MATERIAL.materialNameEN") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MATERIAL.deviceTypeName") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MATERIAL.materialName") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MATERIAL.serial"))
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MATERIAL.unitPrice"))
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MATERIAL.dateTimeStr"))
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    return true;
  }

  private String findDeviceTypeName(String key) {
    return I18n.getLanguage(Constants.DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName().get(key));
  }

  public Map<String, String> getDeviceTypeCBB() {
    Map<String, String> lstDeviceType = new HashMap<>();
    for (String deviceType : Constants.DEVICE_TYPE_LST) {
      lstDeviceType.put(deviceType, findDeviceTypeName(deviceType));
    }
    return lstDeviceType;
  }

  private File exportFileEx(List<MrMaterialDTO> lstData, String key) throws Exception {
    String title = "";
    String fileNameOut = "";
    String subTitle = "";
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = MATERIAL_RESULT_IMPORT;
      title = I18n.getLanguage("MATERIAL.title");
      subTitle = I18n.getLanguage("MATERIAL.export.importDate", DateTimeUtils.convertDateOffset());
      lstHeaderSheet = readerHeaderSheet("materialNameEN",
          "deviceTypeName", "materialName", "serial", "unitPriceStr", "dateTimeStr",
          "resultImport");
    } else {
      fileNameOut = MATERIAL_REPLACE_EXPORT;
      title = I18n.getLanguage("MATERIAL.export.title");
      subTitle = I18n.getLanguage("MATERIAL.export.exportDate", DateTimeUtils.convertDateOffset());
      lstHeaderSheet = readerHeaderSheet("materialNameEN",
          "deviceTypeName", "materialName", "serial", "unitPrice", "dateTimeStr");
    }
    Map<String, String> filedSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstData,
        I18n.getLanguage("MATERIAL.export.title"),
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.MATERIAL",
        lstHeaderSheet,
        filedSplit,
        "",
        I18n.getLanguage("common.export.firstLeftHeader")
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
    fileExports.add(configFileExport);
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

  private List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private boolean checkDuplicated(MrMaterialDTO mrMaterialDTO, boolean insert) {
    MrMaterialDTO dto = new MrMaterialDTO();
    dto.setMaterialName(mrMaterialDTO.getMaterialName());
    dto.setDeviceType(mrMaterialDTO.getDeviceType());
    dto.setMarketCode(mrMaterialDTO.getMarketCode());
    List<MrMaterialDTO> lst = mrMaterialDisplacementRepository.checkListDuplicate(dto);
    if (lst != null && !lst.isEmpty()) {
      if (insert) {
        return true;
      } else {
        for (MrMaterialDTO dtoCheck : lst) {
          if (mrMaterialDTO.getMaterialId().equals(dtoCheck.getMaterialId())) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }
}
