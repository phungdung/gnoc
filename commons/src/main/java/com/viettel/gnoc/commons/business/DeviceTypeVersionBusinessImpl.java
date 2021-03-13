package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.DeviceTypeVersionDTO;
import com.viettel.gnoc.commons.dto.DeviceTypeVersionExportDTO;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.DeviceTypeVersionRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.PROBLEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.awt.Color;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
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
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
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
public class DeviceTypeVersionBusinessImpl implements DeviceTypeVersionBusiness {

  @Autowired
  DeviceTypeVersionRepository deviceTypeVersionRepository;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Value("${application.temp.folder}")
  private String tempFolder;

  private int maxRecord = 1000;

  @Override
  public Datatable getListDeviceTypeVersionDTO(DeviceTypeVersionDTO deviceTypeVersionDTO) {
    log.debug("Request to getListDeviceTypeVersionDTO: {}", deviceTypeVersionDTO);
    Datatable datatable = deviceTypeVersionRepository
        .getListDeviceTypeVersionDTO(deviceTypeVersionDTO);
    return datatable;
  }


  @Override
  public List<DeviceTypeVersionDTO> getListDeviceTypeVersion(
      DeviceTypeVersionDTO deviceTypeVersionDTO) {
    log.debug("Request to getListDeviceTypeVersion: {}", deviceTypeVersionDTO);
    return translateList(
        deviceTypeVersionRepository.getListDeviceTypeVersion(deviceTypeVersionDTO));
  }

  public List<CatItemDTO> getAllCatItem(String lan, String appliedSystem, String appliedBussiness) {
    List<CatItemDTO> lstCatItemDTO = new ArrayList<CatItemDTO>();
    try {
      CatItemDTO catItemDTO = new CatItemDTO();
      catItemDTO.setStatus(1l);
      lstCatItemDTO = catItemBusiness
          .getListCatItemDTOLE("", "", "", catItemDTO, 0, 0, "asc", "itemName");
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return lstCatItemDTO;
  }

  @Override
  public ResultInSideDto insertDeviceTypeVersion(DeviceTypeVersionDTO deviceTypeVersionDTO) {
    log.debug("Request to insertDeviceTypeVersion: {}", deviceTypeVersionDTO);
    return deviceTypeVersionRepository.insertDeviceTypeVersion(deviceTypeVersionDTO);
  }

  @Override
  public ResultInSideDto updateDeviceTypeVersion(DeviceTypeVersionDTO deviceTypeVersionDTO) {
    log.debug("Request to updateDeviceTypeVersion: {}", deviceTypeVersionDTO);
    return deviceTypeVersionRepository.updateDeviceTypeVersion(deviceTypeVersionDTO);
  }

  @Override
  public DeviceTypeVersionDTO findDeviceTypeVersionById(Long id) {
    DeviceTypeVersionDTO dto = deviceTypeVersionRepository.findDeviceTypeVersionById(id);
    if (dto != null) {
      CatItemDTO itemDTO = catItemBusiness.getCatItemById(dto.getTypeId());
      if (itemDTO != null) {
        dto.setSubTypeId(itemDTO.getParentItemId());
      }
    }
    return dto;
  }

  public List<DeviceTypeVersionDTO> translateList(List<DeviceTypeVersionDTO> lstRole) {
    String mySystem = Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
    String myBussiness = Constants.COMMON_TRANSLATE_BUSINESS.DEVICE_TYPE_VERSION_SOFTWARE_VER
        .toString();
    Map<String, Object> mapSQLSoftware = languageExchangeRepository
        .mapLanguageExchange(I18n.getLocale(), mySystem, myBussiness);
    myBussiness = Constants.COMMON_TRANSLATE_BUSINESS.DEVICE_TYPE_VERSION_HARDWARE_VER.toString();
    Map<String, Object> mapSQLHardware = languageExchangeRepository
        .mapLanguageExchange(I18n.getLocale(), mySystem, myBussiness);

    String sqlLanguage = (String) mapSQLSoftware.get("sql");
    Map mapParam = (Map) mapSQLSoftware.get("mapParam");
    Map mapType = (Map) mapSQLSoftware.get("mapType");

    List<LanguageExchangeDTO> lstLanguageSoftware = languageExchangeRepository
        .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);

    sqlLanguage = (String) mapSQLHardware.get("sql");
    mapParam = (Map) mapSQLHardware.get("mapParam");
    mapType = (Map) mapSQLHardware.get("mapType");

    List<LanguageExchangeDTO> lstLanguageHardware = languageExchangeRepository
        .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);

    Map<String, String> mapTranslateSoftware = new HashMap<>();
    Map<String, String> mapTranslateHardware = new HashMap<>();
    if (lstLanguageSoftware != null) {
      for (LanguageExchangeDTO dto : lstLanguageSoftware) {
        if (dto.getBussinessId() != null) {
          mapTranslateSoftware.put(dto.getBussinessId().toString(), dto.getLeeValue());
        }
      }
    }
    if (lstLanguageHardware != null) {
      for (LanguageExchangeDTO dto : lstLanguageHardware) {
        if (dto.getBussinessId() != null) {
          mapTranslateHardware.put(dto.getBussinessId().toString(), dto.getLeeValue());
        }
      }
    }
    for (DeviceTypeVersionDTO deviceTypeVersionDTO : lstRole) {
      if (mapTranslateSoftware != null
          && mapTranslateSoftware.get(deviceTypeVersionDTO.getDeviceTypeVersionId().toString())
          != null) {
        deviceTypeVersionDTO.setSoftwareVersion(
            mapTranslateSoftware.get(deviceTypeVersionDTO.getDeviceTypeVersionId().toString())
                .trim());
      }
      if (mapTranslateHardware != null
          && mapTranslateHardware.get(deviceTypeVersionDTO.getDeviceTypeVersionId().toString())
          != null) {
        deviceTypeVersionDTO.setHardwareVersion(
            mapTranslateHardware.get(deviceTypeVersionDTO.getDeviceTypeVersionId().toString())
                .trim());
      }
    }
    return lstRole;
  }

  private void mapDataName(List<DeviceTypeVersionDTO> lst) {
    if (lst != null && lst.size() > 0) {
      List<CatItemDTO> lstAllCatItem = getAllCatItem("", "", "");
      Map<String, String> mapItem = new HashMap<>();
      for (CatItemDTO dto : lstAllCatItem) {
        mapItem.put(String.valueOf(dto.getItemId()), dto.getItemName());
      }
      for (DeviceTypeVersionDTO dto : lst) {
        String vendorId = String.valueOf(dto.getVendorId());
        String typeId = String.valueOf(dto.getTypeId());
        dto.setVendorIdStr(mapItem.get(vendorId));
        dto.setTypeIdStr(mapItem.get(typeId));
        if (dto.getSubTypeId() != null) {
          dto.setSubTypeIdStr(mapItem.get(String.valueOf(dto.getSubTypeId())));
        }
      }
    }
  }

  @Override
  public File exportData(DeviceTypeVersionDTO deviceTypeVersionDTO) throws Exception {
    log.debug("Request to exportData: {}", deviceTypeVersionDTO);
    List<DeviceTypeVersionDTO> lstDTO = deviceTypeVersionRepository
        .getDataExportDeviceTypeVersionDTO(deviceTypeVersionDTO);
    mapDataName(lstDTO);
    List<DeviceTypeVersionExportDTO> lstExport = new ArrayList<>();
    if (lstDTO != null) {
      for (DeviceTypeVersionDTO dto : lstDTO) {
        lstExport.add(new DeviceTypeVersionExportDTO(
            dto.getSubTypeIdStr(), dto.getTypeIdStr(), dto.getVendorIdStr(),
            dto.getHardwareVersion(), dto.getSoftwareVersion()
        ));
      }
    }
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet("vendorIdStr", "typeIdStr",
        "hardwareVersion", "softwareVersion");
    return exportFileEx(lstExport, lstHeaderSheet, "");
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<DeviceTypeVersionDTO> listDTVersionDTO;
    List<DeviceTypeVersionExportDTO> listDTVersionExportDTO;
    Map<String, String> mapExportDTO;

    String[] header = new String[]{"subTypeIdStr", "typeIdStr", "vendorIdStr", "hardwareVersion",
        "softwareVersion", "resultImport"};
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
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 1,
            0, 5, 1000);

        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 2,
            0, 5, 1000);

        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }

        listDTVersionDTO = new ArrayList<>();
        listDTVersionExportDTO = new ArrayList<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          mapExportDTO = new HashMap<>();
          Map<String, Long> mapArray = setMapArray();
          Map<String, Long> mapTypeNode = setMapTypeNode();
          Map<String, Long> mapVendor = setMapVendor();
          for (Object[] obj : lstData) {
            DeviceTypeVersionDTO dtvDTO = new DeviceTypeVersionDTO();
            DeviceTypeVersionExportDTO dtvExportDTO = new DeviceTypeVersionExportDTO();

            if (obj[1] != null) {
              String arrayCode = obj[1].toString().trim();
              String c0 = "_";
              char c1 = arrayCode.charAt(1);
              if (c0.equals(String.valueOf(arrayCode.charAt(0))) && Character.isDigit(c1)) {
                arrayCode = arrayCode.substring(1);
              }
              dtvExportDTO.setSubTypeIdStr(arrayCode);
              if (mapArray.containsKey(dtvExportDTO.getSubTypeIdStr())) {
                dtvDTO.setSubTypeId(mapArray.get(dtvExportDTO.getSubTypeIdStr()));
              }
            } else {
              dtvExportDTO.setSubTypeIdStr(null);
            }

            if (obj[2] != null) {
              dtvExportDTO.setTypeIdStr(obj[2].toString().trim());
              if (mapTypeNode.containsKey(dtvExportDTO.getTypeIdStr())) {
                dtvDTO.setTypeId(mapTypeNode.get(dtvExportDTO.getTypeIdStr()));
              }
            } else {
              dtvExportDTO.setTypeIdStr(null);
            }
            if (obj[3] != null) {
              dtvExportDTO.setVendorIdStr(obj[3].toString().trim());
              if (mapVendor.containsKey(dtvExportDTO.getVendorIdStr())) {
                dtvDTO.setVendorId(mapVendor.get(dtvExportDTO.getVendorIdStr()));
              }
            } else {
              dtvExportDTO.setVendorIdStr(null);
            }
            if (obj[4] != null) {
              dtvExportDTO.setHardwareVersion(obj[4].toString().trim());
              dtvDTO.setHardwareVersion(dtvExportDTO.getHardwareVersion());
            } else {
              dtvExportDTO.setHardwareVersion(null);
            }
            if (obj[5] != null) {
              dtvExportDTO.setSoftwareVersion(obj[5].toString().trim());
              dtvDTO.setSoftwareVersion(dtvExportDTO.getSoftwareVersion());
            } else {
              dtvExportDTO.setSoftwareVersion(null);
            }

            DeviceTypeVersionExportDTO exportDTO = validateImportInfo(dtvExportDTO, dtvDTO,
                mapExportDTO);
            if (exportDTO.getResultImport() == null) {
              exportDTO.setResultImport(I18n.getLanguage("deviceTypeVersion.result.import.ok"));
              listDTVersionExportDTO.add(exportDTO);
              listDTVersionDTO.add(dtvDTO);
              mapExportDTO.put(
                  exportDTO.getSubTypeIdStr() + "_" + exportDTO.getTypeIdStr() + "_" + exportDTO
                      .getVendorIdStr() + "_"
                      + exportDTO.getHardwareVersion() + "_" + exportDTO.getSoftwareVersion(),
                  String.valueOf(value));
            } else {
              listDTVersionExportDTO.add(exportDTO);
              index++;
            }
            value++;
          }
          if (index == 0) {
            if (!listDTVersionDTO.isEmpty()) {
              for (DeviceTypeVersionDTO dtvdto : listDTVersionDTO) {
                resultInSideDto = insertDeviceTypeVersion(dtvdto);
              }
            }
          } else {
            File fileExport = exportFileEx(listDTVersionExportDTO, renderHeaderSheet(header),
                Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileEx(listDTVersionExportDTO, renderHeaderSheet(header),
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

  @Override
  public File getTemplate() throws Exception {
    List<CatItemDTO> lstVendor = new ArrayList<>();
    List<CatItemDTO> lstArrayNode = new ArrayList<>();
    List<CatItemDTO> lstTypeNode = new ArrayList<>();
    Map<String, List<String>> categoryTypeSub = new HashMap<>();

    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");

    Datatable dataType = catItemBusiness
        .getItemMaster(PROBLEM.PT_TYPE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);

    if (dataType != null && dataType.getData() != null) {
      lstArrayNode = (List<CatItemDTO>) dataType.getData();
    }

    dataType = catItemBusiness
        .getItemMasterHasParent(PROBLEM.PT_SUBCAT,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);

    if (dataType != null && dataType.getData() != null) {
      lstTypeNode = (List<CatItemDTO>) dataType.getData();
    }

    dataType = catItemBusiness
        .getItemMaster(PROBLEM.VENDOR,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    if (dataType != null && dataType.getData() != null) {
      lstVendor = (List<CatItemDTO>) dataType.getData();
    }

    List<String> lstArrayCode = new ArrayList<>();
    List<String> lstArrayName = new ArrayList<>();
    List<Long> lstArrayId = new ArrayList<>();

    for (int i = 0; i < lstArrayNode.size(); i++) {
      String arrayCode = lstArrayNode.get(i).getItemCode();
      char c = arrayCode.charAt(0);
      if (Character.isDigit(c)) {
        arrayCode = "_" + arrayCode;
      }
      lstArrayCode.add(arrayCode);
      lstArrayName.add(lstArrayNode.get(i).getItemName());
      lstArrayId.add(lstArrayNode.get(i).getItemId());
      List<String> lstSubCategoryCode = new ArrayList<>();
      List<CatItemDTO> lstSubCategory = new ArrayList<>();
      for (CatItemDTO item : lstTypeNode) {
        if (lstArrayNode.get(i).getItemId().equals(item.getParentItemId())) {
          lstSubCategory.add(item);
        }
      }
      if (lstSubCategory != null && !lstSubCategory.isEmpty()) {
        for (CatItemDTO dto : lstSubCategory) {
          lstSubCategoryCode.add(dto.getItemCode());
        }
        categoryTypeSub.put(lstArrayCode.get(i), lstSubCategoryCode);
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
        I18n.getLanguage("deviceTypeVersion.list.stt"),
        I18n.getLanguage("deviceTypeVersion.list.array"),
        I18n.getLanguage("deviceTypeVersion.list.typeId"),
        I18n.getLanguage("deviceTypeVersion.list.vendorId"),
        I18n.getLanguage("deviceTypeVersion.list.hardwareVersion"),
        I18n.getLanguage("deviceTypeVersion.list.softwareVersion")
    };

    String[] headerStar = new String[]{
        I18n.getLanguage("deviceTypeVersion.list.array"),
        I18n.getLanguage("deviceTypeVersion.list.typeId"),
        I18n.getLanguage("deviceTypeVersion.list.vendorId"),
        I18n.getLanguage("deviceTypeVersion.list.hardwareVersion"),
        I18n.getLanguage("deviceTypeVersion.list.softwareVersion")
    };

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int arrayColumn = listHeader.indexOf(I18n.getLanguage("deviceTypeVersion.list.array"));
    int typeNodeColumn = listHeader.indexOf(I18n.getLanguage("deviceTypeVersion.list.typeId"));
    int vendorNameColumn = listHeader.indexOf(I18n.getLanguage("deviceTypeVersion.list.vendorId"));

    //Tao tieu de
    Font xssFontTitle = workBook.createFont();
    xssFontTitle.setFontName("Times New Roman");
    xssFontTitle.setFontHeightInPoints((short) 22);
    xssFontTitle.setColor(IndexedColors.BLACK.index);

    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetMain.createRow(0);
    XSSFCellStyle styleTitle = workBook.createCellStyle();
    styleTitle.setBorderBottom(BorderStyle.THIN);
    styleTitle.setBorderLeft(BorderStyle.THIN);
    styleTitle.setBorderTop(BorderStyle.THIN);
    styleTitle.setBorderRight(BorderStyle.THIN);

    styleTitle.setAlignment(HorizontalAlignment.CENTER);
    styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    styleTitle
        .setFillForegroundColor(new XSSFColor(new Color(0, 176, 80), new DefaultIndexedColorMap()));
    styleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleTitle.setFont(xssFontTitle);
    titleRow.setHeightInPoints(41.25f);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("deviceTypeVersion.list.title_import"));
    titleCell.setCellStyle(styleTitle);

    Font xssFontHeader = workBook.createFont();
    xssFontHeader.setFontName("Times New Roman");
    xssFontHeader.setFontHeightInPoints((short) 14);
    xssFontHeader.setColor(IndexedColors.BLACK.index);
    xssFontHeader.setBold(true);
    XSSFCellStyle styleHeader = workBook.createCellStyle();
    styleHeader.setAlignment(HorizontalAlignment.CENTER);
    styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    styleHeader.setFillForegroundColor(
        new XSSFColor(new Color(141, 180, 226), new DefaultIndexedColorMap()));
    styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleHeader.setFont(xssFontHeader);
    styleHeader.setBorderBottom(BorderStyle.THIN);
    styleHeader.setBorderRight(BorderStyle.THIN);
    styleHeader.setBorderTop(BorderStyle.THIN);

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());
    //Tao header
    Row headerRow = sheetMain.createRow(1);
    headerRow.setHeightInPoints(69);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeader : listHeaderStar) {
        if (checkHeader.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("(*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(styleHeader);
      sheetMain.setColumnWidth(i, 7000);
    }
    sheetMain.setColumnWidth(0, 3000);

    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("deviceTypeVersion.list.arrayName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("deviceTypeVersion.list.arrayName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 2, 0,
        I18n.getLanguage("deviceTypeVersion.list.typeIdStr").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 3, 0,
        I18n.getLanguage("deviceTypeVersion.list.vendorIdStr").toUpperCase(),
        styles.get("header"));

    //create sheet Other
    int row = 1;
    for (CatItemDTO dto : lstArrayNode) {
      ewu.createCell(sheetOrther, 0, row++, dto.getItemName(), styles.get("cell"));
    }
    Name arrayName = workBook.createName();
    arrayName.setNameName("arrayName");
    arrayName.setRefersToFormula("Orther!$A$2:$A$" + row);

    row = 1;
    for (CatItemDTO dto : lstArrayNode) {
      ewu.createCell(sheetOrther, 1, row++, dto.getItemCode(), styles.get("cell"));
    }
    Name arrayCode = workBook.createName();
    arrayCode.setNameName("arrayCode");
    arrayCode.setRefersToFormula("Orther!$B$2:$B$" + row);
    XSSFDataValidationConstraint typeArrayCodeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "arrayCode");
    CellRangeAddressList cellRangeArrayCode = new CellRangeAddressList(2, (maxRecord + 2),
        arrayColumn, arrayColumn);
    XSSFDataValidation dataValidationArrayCode = (XSSFDataValidation) dvHelper.createValidation(
        typeArrayCodeConstraint, cellRangeArrayCode);
    dataValidationArrayCode.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationArrayCode);

    row = 1;
    for (CatItemDTO dto : lstTypeNode) {
      ewu.createCell(sheetOrther, 2, row++, dto.getItemCode(), styles.get("cell"));
    }

    XSSFDataValidationConstraint typeNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "Type");
    CellRangeAddressList cellRangeType = new CellRangeAddressList(
        2, (maxRecord + 2), arrayColumn, arrayColumn);
    XSSFDataValidation dataValidationType = (XSSFDataValidation) dvHelper.createValidation(
        typeNameConstraint, cellRangeType);
    dataValidationType.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationType);

    XSSFDataValidationConstraint subCategoryNameConstraint;
    CellRangeAddressList cellRangeSubCategory;
    XSSFDataValidation dataValidationSubCategory;
    for (int i = 2; i <= (maxRecord + 2); i++) {
      subCategoryNameConstraint = new XSSFDataValidationConstraint(
          DataValidationConstraint.ValidationType.LIST, "INDIRECT($B$" + i + ")");
      cellRangeSubCategory = new CellRangeAddressList(
          i - 1, i - 1, typeNodeColumn, typeNodeColumn);
      dataValidationSubCategory = (XSSFDataValidation) dvHelper.createValidation(
          subCategoryNameConstraint, cellRangeSubCategory);
      dataValidationSubCategory.setShowErrorBox(true);
      sheetMain.addValidationData(dataValidationSubCategory);
    }

    row = 1;
    for (CatItemDTO dto : lstVendor) {
      ewu.createCell(sheetOrther, 3, row++, dto.getItemName(), styles.get("cell"));
    }

    Name vendorName = workBook.createName();
    vendorName.setNameName("vendorName");
    vendorName.setRefersToFormula("Orther!$D$2:$D$" + row);
    XSSFDataValidationConstraint vendorNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "vendorName");
    CellRangeAddressList cellRangeVendor = new CellRangeAddressList(2, (maxRecord + 2),
        vendorNameColumn, vendorNameColumn);
    XSSFDataValidation dataValidationVendor = (XSSFDataValidation) dvHelper.createValidation(
        vendorNameConstraint, cellRangeVendor);
    dataValidationVendor.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationVendor);

    for (int i = 0; i <= 11; i++) {
      sheetOrther.autoSizeColumn(i);
    }

    sheetOrther.setSelected(false);
    workBook.setSheetName(0, I18n.getLanguage("Version"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_VERSION_TEMPLATE" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  private File exportFileEx(List<DeviceTypeVersionExportDTO> lstData,
      List<ConfigHeaderExport> lstHeaderSheet, String code) throws Exception {
    String title = I18n.getLanguage("deviceTypeVersion.title_export");
    String sheetName = title;
    String fileNameOut = "VersionReport";
    ConfigFileExport configfileExport = null;
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet;
    if (Constants.RESULT_IMPORT.equals(code)) {
      sheetName = "Version";
      fileNameOut = "VersionImport";
      title = I18n.getLanguage("deviceTypeVersion.list.title_import");
      configfileExport = new ConfigFileExport(
          lstData
          , sheetName
          , title
          , null
          , 5
          , 3
          , lstHeaderSheet.size()
          , true
          , "language.deviceTypeVersion.list"
          , lstHeaderSheet
          , fieldSplit
          , ""
          , I18n.getLanguage("deviceTypeVersion.export.firstLeftHeader")
          , I18n.getLanguage("deviceTypeVersion.export.secondLeftHeader")
          , I18n.getLanguage("deviceTypeVersion.export.firstRightHeader")
          , I18n.getLanguage("deviceTypeVersion.export.secondRightHeader")
      );
      cellSheet = new CellConfigExport(5, 0, 0, 0, I18n.getLanguage("deviceTypeVersion.list.stt"),
          "HEAD", "STRING");
    } else {
      configfileExport = new ConfigFileExport(
          lstData
          , sheetName
          , title
          , null
          , 7
          , 3
          , lstHeaderSheet.size()
          , true
          , "language.deviceTypeVersion.list"
          , lstHeaderSheet
          , fieldSplit
          , ""
          , I18n.getLanguage("deviceTypeVersion.export.firstLeftHeader")
          , I18n.getLanguage("deviceTypeVersion.export.secondLeftHeader")
          , I18n.getLanguage("deviceTypeVersion.export.firstRightHeader")
          , I18n.getLanguage("deviceTypeVersion.export.secondRightHeader")
      );
      cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("deviceTypeVersion.list.stt"),
          "HEAD", "STRING");
    }

    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
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

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count < 6) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("deviceTypeVersion.list.stt"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("deviceTypeVersion.list.array") + "(*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("deviceTypeVersion.list.typeId") + "(*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("deviceTypeVersion.list.vendorId") + "(*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("deviceTypeVersion.list.hardwareVersion") + "(*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("deviceTypeVersion.list.softwareVersion") + "(*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    return true;
  }

  private Map<String, Long> setMapArray() {
    Map<String, Long> mapArray = new HashMap<>();
    Datatable datatable = catItemBusiness
        .getItemMaster(PROBLEM.PT_TYPE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);

    if (datatable != null && datatable.getData() != null) {
      List<CatItemDTO> lstArrayNode = (List<CatItemDTO>) datatable.getData();
      if (lstArrayNode != null) {
        for (CatItemDTO catItemDTO : lstArrayNode) {
          if (catItemDTO.getItemCode() != null && catItemDTO.getItemCode().length() > 0) {
            mapArray.put(catItemDTO.getItemCode(), catItemDTO.getItemId());
          }
        }
      }
    }
    return mapArray;
  }

  private Map<String, Long> setMapTypeNode() {
    Map<String, Long> mapTypeNode = new HashMap<>();
    Datatable datatable = catItemBusiness
        .getItemMasterHasParent(PROBLEM.PT_SUBCAT,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);

    if (datatable != null && datatable.getData() != null) {
      List<CatItemDTO> lstTypeNode = (List<CatItemDTO>) datatable.getData();
      if (lstTypeNode != null) {
        for (CatItemDTO catItemDTO : lstTypeNode) {
          if (catItemDTO.getItemCode() != null && catItemDTO.getItemCode().length() > 0) {
            mapTypeNode.put(catItemDTO.getItemCode(), catItemDTO.getItemId());
          }
        }
      }
    }
    return mapTypeNode;
  }

  private Map<String, Long> setMapVendor() {
    Map<String, Long> mapVendor = new HashMap<>();
    Datatable datatable = catItemBusiness
        .getItemMaster(PROBLEM.VENDOR,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);

    if (datatable != null && datatable.getData() != null) {
      List<CatItemDTO> lstTypeNode = (List<CatItemDTO>) datatable.getData();
      if (lstTypeNode != null) {
        for (CatItemDTO catItemDTO : lstTypeNode) {
          if (catItemDTO.getItemCode() != null && catItemDTO.getItemCode().length() > 0) {
            mapVendor.put(catItemDTO.getItemCode(), catItemDTO.getItemId());
          }
        }
      }
    }

    return mapVendor;
  }

  private DeviceTypeVersionExportDTO validateImportInfo(DeviceTypeVersionExportDTO dtvExportDTO,
      DeviceTypeVersionDTO dtvDTO, Map<String, String> mapExportDTO) {
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(dtvExportDTO.getSubTypeIdStr())) {
      resultImport = resultImport
          .concat(I18n.getValidation("deviceTypeVersion.arrName.isRequired"));
    } else if (dtvDTO.getSubTypeId() == null) {
      resultImport = resultImport.concat(I18n.getValidation("deviceTypeVersion.arrName.invalid"));
    }
    if (StringUtils.isStringNullOrEmpty(dtvExportDTO.getTypeIdStr())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getValidation("deviceTypeVersion.typeNode.isRequired"));
    } else if (dtvDTO.getTypeId() == null) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getValidation("deviceTypeVersion.typeNode.invalid"));
    }
    if (StringUtils.isStringNullOrEmpty(dtvExportDTO.getVendorIdStr())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getValidation("deviceTypeVersion.vendor.isRequired"));
    } else if (dtvDTO.getVendorId() == null) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getValidation("deviceTypeVersion.vendor.invalid"));
    }
    if (StringUtils.isStringNullOrEmpty(dtvExportDTO.getHardwareVersion())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getValidation("deviceTypeVersion.hardwareVersion.isRequired"));
    }
    if (StringUtils.isStringNullOrEmpty(dtvExportDTO.getSoftwareVersion())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("deviceTypeVersion.softwareVersion.isRequired"));
    }

    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      dtvExportDTO.setResultImport(resultImport);
      return dtvExportDTO;
    }

    if (dtvDTO.getTypeId() != null && dtvDTO.getVendorId() != null &&
        StringUtils.isNotNullOrEmpty(dtvDTO.getHardwareVersion()) &&
        StringUtils.isNotNullOrEmpty(dtvDTO.getSoftwareVersion())
    ) {
      DeviceTypeVersionDTO dtoTmp = deviceTypeVersionRepository
          .checkDeviceTypeVersionExit(dtvDTO);
      if (dtoTmp != null) {
        dtvExportDTO.setResultImport(I18n.getValidation("deviceTypeVersion.dup-code"));
        return dtvExportDTO;
      }
    }
    String validateDuplicate = validateDuplicate(dtvExportDTO, mapExportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      dtvExportDTO.setResultImport(validateDuplicate);
      return dtvExportDTO;
    }
    return dtvExportDTO;

  }

  private String validateDuplicate(DeviceTypeVersionExportDTO exportDTO,
      Map<String, String> mapExportDTO) {
    String arrayName = exportDTO.getSubTypeIdStr();
    String typeNode = exportDTO.getTypeIdStr();
    String vendor = exportDTO.getVendorIdStr();
    String hardware = exportDTO.getHardwareVersion();
    String software = exportDTO.getSoftwareVersion();
    String key = arrayName + "_" + typeNode + "_" + vendor + "_" + hardware + "_" + software;
    String value = mapExportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getValidation("deviceTypeVersion.dup-code-in-file")
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
}
