package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatServiceDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatServiceRepository;
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
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDTO;
import com.viettel.gnoc.wo.repository.WoMaterialRepository;
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
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
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
public class WoMaterialBusinessImpl implements WoMaterialBusiness {

  @Autowired
  protected WoMaterialRepository woMaterialRepository;

  @Autowired
  protected CatItemRepository catItemRepository;

  @Autowired
  protected CatServiceRepository catServiceRepository;


  @Value("${application.temp.folder}")
  private String tempFolder;
  List<MaterialThresInsideDTO> materialThresDTOList = new ArrayList<>();
  Map<String, String> mapActionName = new HashMap<>();
  Map<String, String> mapServiceName = new HashMap<>();
  Map<String, String> mapMaterialName = new HashMap<>();
  Map<String, String> mapInfraTypeName = new HashMap<>();
  private final static String WO_MATERIAL_RESULT_IMPORT = "WO_MATERIAL_RESULT_IMPORT";
  private final static String WO_MATERIAL_EXPORT = "WO_MATERIAL_EXPORT";

  @Override
  public Datatable getListWoMaterialPage(MaterialThresInsideDTO materialThresDTO) {
    log.debug("Request to getListWoMaterialPage : {}", materialThresDTO);
    return woMaterialRepository.getListWoMaterialPage(materialThresDTO);
  }

  @Override
  public ResultInSideDto delete(Long materialThresId) {
    log.debug("Request to delete : {}", materialThresId);
    return woMaterialRepository.delete(materialThresId);
  }

  @Override
  public Datatable getListDataExport(MaterialThresInsideDTO materialThresDTO) {
    log.debug("Request to getListDataExport : {}", materialThresDTO);
    return woMaterialRepository.getListDataExport(materialThresDTO);
  }

  @Override
  public ResultInSideDto insert(MaterialThresInsideDTO materialThresDTO) {
    log.debug("Request to insert : {}", materialThresDTO);
    return woMaterialRepository.add(materialThresDTO);
  }

  @Override
  public ResultInSideDto update(MaterialThresInsideDTO materialThresDTO) {
    log.debug("Request to update : {}", materialThresDTO);
    return woMaterialRepository.edit(materialThresDTO);
  }


  @Override
  public MaterialThresInsideDTO findByMaterialThresId(Long materialThresId) {
    log.debug("Request to findByMaterialThresId : {}", materialThresId);
    return woMaterialRepository.findByMaterialThresId(materialThresId);
  }


  @Override
  public List<WoMaterialDTO> findAllMaterial(String materialName) {
    log.debug("Request to findAllMaterial : {}", materialName);
    return woMaterialRepository.findAllMaterial(materialName);
  }

  @Override
  public WoMaterialDTO findWoMaterialById(Long materialId) {
    log.debug("Request to findWoMaterialById : {}", materialId);
    return woMaterialRepository.findWoMaterialById(materialId);
  }


  public void setMapActionName() {
    Datatable actionMaster = catItemRepository
        .getItemMaster(CATEGORY.WO_ACTION_GROUP, LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), "itemId", "itemName");
    List<CatItemDTO> lstActionName = (List<CatItemDTO>) actionMaster.getData();
    if (lstActionName != null && !lstActionName.isEmpty()) {
      for (CatItemDTO catItemDTO : lstActionName) {
        mapActionName.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
  }

  public void setMapInfraTypeName() {
    Datatable infraTypeMaster = catItemRepository
        .getItemMaster("WO_TECHNOLOGY_CODE", LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), "itemId", "itemName");
    List<CatItemDTO> lstActionName = (List<CatItemDTO>) infraTypeMaster.getData();
    if (lstActionName != null && !lstActionName.isEmpty()) {
      for (CatItemDTO catItemDTO : lstActionName) {
        mapInfraTypeName.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
  }

  public void setMapServiceName() {
    Datatable serviceMaster = catServiceRepository
        .getItemServiceMaster(LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_SERVICE.toString(), "serviceId", "serviceName");
    List<CatServiceDTO> lstServiceName = (List<CatServiceDTO>) serviceMaster.getData();
    if (lstServiceName != null && !lstServiceName.isEmpty()) {
      for (CatServiceDTO catServiceDTO : lstServiceName) {
        mapServiceName
            .put(String.valueOf(catServiceDTO.getServiceId()), catServiceDTO.getServiceName());
      }
    }
  }

  public void setMapMaterialName() {
    List<WoMaterialDTO> lstMaterialDTOS = woMaterialRepository
        .findAllMaterial(null);
    if (lstMaterialDTOS != null && !lstMaterialDTOS.isEmpty()) {
      for (WoMaterialDTO woMaterialDTO : lstMaterialDTOS) {
        mapMaterialName
            .put(String.valueOf(woMaterialDTO.getMaterialId()), woMaterialDTO.getMaterialName());
      }
    }
  }

  @Override
  public File getMaterialTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut); //Xóa khoảng trắng
    Resource resource = new ClassPathResource(templatePathOut); // Xóa dấu / đầu đường dẫn
    InputStream fileTemplate = resource.getInputStream(); //Tạo luồng

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("woMaterial.technology"),
        I18n.getLanguage("woMaterial.serviceName"),
        I18n.getLanguage("woMaterial.actionName"),
        I18n.getLanguage("woMaterial.materialName"),
        I18n.getLanguage("woMaterial.techThres"),
        I18n.getLanguage("woMaterial.warningThres"),
        I18n.getLanguage("woMaterial.freeThres"),
        I18n.getLanguage("woMaterial.techDistanctThres"),
        I18n.getLanguage("woMaterial.warningDistanctThres"),
        I18n.getLanguage("woMaterial.freeDistanctThres")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("woMaterial.technology"),
        I18n.getLanguage("woMaterial.serviceName"),
        I18n.getLanguage("woMaterial.actionName"),
        I18n.getLanguage("woMaterial.materialName"),
        I18n.getLanguage("woMaterial.techThres"),
        I18n.getLanguage("woMaterial.warningThres"),
        I18n.getLanguage("woMaterial.freeThres")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int technologyColumn = listHeader.indexOf(I18n.getLanguage("woMaterial.technology"));
    int serviceNameColumn = listHeader.indexOf(I18n.getLanguage("woMaterial.serviceName"));
    int actionNameColumn = listHeader.indexOf(I18n.getLanguage("woMaterial.actionName"));
    int materialNameColumn = listHeader.indexOf(I18n.getLanguage("woMaterial.materialName"));
    int techThresColumn = listHeader.indexOf(I18n.getLanguage("woMaterial.techThres"));
    int freeThresColumn = listHeader.indexOf(I18n.getLanguage("woMaterial.freeThres"));
    int warningThresColumn = listHeader.indexOf(I18n.getLanguage("woMaterial.warningThres"));
    int techDistanctThresColumn = listHeader
        .indexOf(I18n.getLanguage("woMaterial.techDistanctThres"));
    int warningDistanctThresColumn = listHeader
        .indexOf(I18n.getLanguage("woMaterial.warningDistanctThres"));
    int freeDistanctThresColumn = listHeader
        .indexOf(I18n.getLanguage("woMaterial.freeDistanctThres"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(0);
    titleRow.setHeightInPoints(30);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("woMaterial.title"));
    titleCell.setCellStyle(style.get("title"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(4);
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
    // Set độ rộng cột stt
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown
    int row = 5;

    Datatable infraTypeMaster = catItemRepository
        .getItemMaster("WO_TECHNOLOGY_CODE", LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), "itemId", "itemName");
    List<CatItemDTO> infraTypeNameList = (List<CatItemDTO>) infraTypeMaster.getData();
    for (CatItemDTO dto : infraTypeNameList) {
      excelWriterUtils.createCell(sheetParam, 1, row++, dto.getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);

    Name technology = workbook.createName();
    technology.setNameName("technology");
    technology.setRefersToFormula("param!$B$2:$B$" + row);

    XSSFDataValidationConstraint technologyConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "technology");
    CellRangeAddressList technologyCreate = new CellRangeAddressList(5, 65000, technologyColumn,
        technologyColumn);
    XSSFDataValidation dataValidationTechnology = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            technologyConstraint, technologyCreate);
    dataValidationTechnology.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationTechnology);

    row = 5;
    Datatable serviceMaster = catServiceRepository
        .getItemServiceMaster(LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_SERVICE.toString(), "serviceId", "serviceName");
    List<CatServiceDTO> serviceNameList = (List<CatServiceDTO>) serviceMaster.getData();
    for (CatServiceDTO dto : serviceNameList) {
      excelWriterUtils.createCell(sheetParam, 2, row++, dto.getServiceName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);

    Name serviceName = workbook.createName();
    serviceName.setNameName("serviceName");
    serviceName.setRefersToFormula("param!$C$2:$C$" + row);

    XSSFDataValidationConstraint serviceNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "serviceName");
    CellRangeAddressList serviceNameCreate = new CellRangeAddressList(5, 65000, serviceNameColumn,
        serviceNameColumn);
    XSSFDataValidation dataValidationServiceName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            serviceNameConstraint, serviceNameCreate);
    dataValidationServiceName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationServiceName);

    row = 5;
    Datatable actionMaster = catItemRepository
        .getItemMaster(CATEGORY.WO_ACTION_GROUP, LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), "itemId", "itemName");
    List<CatItemDTO> actionNameList = (List<CatItemDTO>) actionMaster.getData();
    for (CatItemDTO dto : actionNameList) {
      excelWriterUtils.createCell(sheetParam, 3, row++, dto.getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);

    Name actionName = workbook.createName();
    actionName.setNameName("actionName");
    actionName.setRefersToFormula("param!$D$2:$D$" + row);

    XSSFDataValidationConstraint actionNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "actionName");
    CellRangeAddressList actionNameCreate = new CellRangeAddressList(5, 65000, actionNameColumn,
        actionNameColumn);
    XSSFDataValidation dataValidationActionName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            actionNameConstraint, actionNameCreate);
    dataValidationActionName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationActionName);

    row = 5;

    List<WoMaterialDTO> materialDTOList = woMaterialRepository.findAllMaterial(null);
    for (WoMaterialDTO dto : materialDTOList) {
      excelWriterUtils.createCell(sheetParam, 4, row++, dto.getMaterialName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);

    Name materialName = workbook.createName();
    materialName.setNameName("materialName");
    materialName.setRefersToFormula("param!$E$2:$E$" + row);

    XSSFDataValidationConstraint materialNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "materialName");
    CellRangeAddressList materialNameCreate = new CellRangeAddressList(5, 65000, materialNameColumn,
        materialNameColumn);
    XSSFDataValidation dataValidationMaterialName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            materialNameConstraint, materialNameCreate);
    dataValidationMaterialName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationMaterialName);

    XSSFDataValidationConstraint techThresConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "9999999.00");
    CellRangeAddressList techThresCreate = new CellRangeAddressList(5, 65000, techThresColumn,
        techThresColumn);
    XSSFDataValidation dataValidationTechThres = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            techThresConstraint, techThresCreate);
    dataValidationTechThres
        .createErrorBox("Error input", I18n.getLanguage("woMaterial.err.number"));
    dataValidationTechThres.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationTechThres);

    XSSFDataValidationConstraint warningThresConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "9999999.00");
    CellRangeAddressList warningThresCreate = new CellRangeAddressList(5, 65000, warningThresColumn,
        warningThresColumn);
    XSSFDataValidation dataValidationWarningThres = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            warningThresConstraint, warningThresCreate);
    dataValidationWarningThres
        .createErrorBox("Error input", I18n.getLanguage("woMaterial.err.number"));
    dataValidationWarningThres.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationWarningThres);

    XSSFDataValidationConstraint freeThresConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "9999999.00");
    CellRangeAddressList freeThresCreate = new CellRangeAddressList(5, 65000, freeThresColumn,
        freeThresColumn);
    XSSFDataValidation dataValidationFreeThres = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            freeThresConstraint, freeThresCreate);
    dataValidationFreeThres
        .createErrorBox("Error input", I18n.getLanguage("woMaterial.err.number"));
    dataValidationFreeThres.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationFreeThres);

    XSSFDataValidationConstraint techDistanctThresConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "9999999.00");
    CellRangeAddressList techDistanctThresCreate = new CellRangeAddressList(5, 65000,
        techDistanctThresColumn,
        techDistanctThresColumn);
    XSSFDataValidation dataValidationTechDistanctThres = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            techDistanctThresConstraint, techDistanctThresCreate);
    dataValidationTechDistanctThres
        .createErrorBox("Error input", I18n.getLanguage("woMaterial.err.number"));
    dataValidationTechDistanctThres.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationTechDistanctThres);

    XSSFDataValidationConstraint warningDistanctThresConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "9999999.00");
    CellRangeAddressList warningDistanctThresCreate = new CellRangeAddressList(5, 65000,
        warningDistanctThresColumn,
        warningDistanctThresColumn);
    XSSFDataValidation dataValidationWarningDistanctThres = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            warningDistanctThresConstraint, warningDistanctThresCreate);
    dataValidationWarningDistanctThres
        .createErrorBox("Error input", I18n.getLanguage("woMaterial.err.number"));
    dataValidationWarningDistanctThres.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationWarningDistanctThres);

    XSSFDataValidationConstraint freeDistanctThresConstraint = new XSSFDataValidationConstraint(
        ValidationType.DECIMAL, OperatorType.BETWEEN, "0.00", "9999999.00");
    CellRangeAddressList freeDistanctThresCreate = new CellRangeAddressList(5, 65000,
        freeDistanctThresColumn,
        freeDistanctThresColumn);
    XSSFDataValidation dataValidationFreeDistanctThres = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            freeDistanctThresConstraint, freeDistanctThresCreate);
    dataValidationFreeDistanctThres
        .createErrorBox("Error input", I18n.getLanguage("woMaterial.err.number"));
    dataValidationFreeDistanctThres.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationFreeDistanctThres);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("woMaterial.title"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_WO_MATERIAL" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File exportData(MaterialThresInsideDTO materialThresDTO) throws Exception {
    List<MaterialThresInsideDTO> materialThresExportDTOList = (List<MaterialThresInsideDTO>) woMaterialRepository
        .getListDataExport(materialThresDTO).getData();
    return exportFileTemplate(materialThresExportDTOList, "");
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<MaterialThresInsideDTO> materialThresList = new ArrayList<>();
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
            10,
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
            10,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }

        materialThresDTOList = new ArrayList<>();

        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          setMapActionName();
          setMapServiceName();
          setMapMaterialName();
          setMapInfraTypeName();
          for (Object[] obj : dataImportList) {
            MaterialThresInsideDTO materialThresDTO = new MaterialThresInsideDTO();
            materialThresDTO.setResultImport(" ");
            if (obj[1] != null) {
              materialThresDTO.setTechnology(obj[1].toString().trim());
              for (Map.Entry<String, String> item : mapInfraTypeName.entrySet()) {
                if (materialThresDTO.getTechnology().equals(item.getValue())) {
                  materialThresDTO.setInfraType(Long.valueOf(item.getKey()));
                  break;
                } else {
                  materialThresDTO.setInfraType(null);
                }
              }
            } else {
              materialThresDTO.setTechnology(null);
            }
            if (obj[2] != null) {
              materialThresDTO.setServiceName(obj[2].toString().trim());
              for (Map.Entry<String, String> item : mapServiceName.entrySet()) {
                if (materialThresDTO.getServiceName().equals(item.getValue())) {
                  materialThresDTO.setServiceId(Long.valueOf(item.getKey()));
                  break;
                } else {
                  materialThresDTO.setServiceId(null);
                }
              }
            } else {
              materialThresDTO.setServiceName(null);
            }
            if (obj[3] != null) {
              materialThresDTO.setActionName(obj[3].toString().trim());
              for (Map.Entry<String, String> item : mapActionName.entrySet()) {
                if (materialThresDTO.getActionName().equals(item.getValue())) {
                  materialThresDTO.setActionId(Long.valueOf(item.getKey()));
                  break;
                } else {
                  materialThresDTO.setActionId(null);
                }
              }
            } else {
              materialThresDTO.setActionName(null);
            }
            if (obj[4] != null) {
              materialThresDTO.setMaterialName(obj[4].toString().trim());
              for (Map.Entry<String, String> item : mapMaterialName.entrySet()) {
                if (materialThresDTO.getMaterialName().equals(item.getValue())) {
                  materialThresDTO.setMaterialId(Long.valueOf(item.getKey()));
                  break;
                } else {
                  materialThresDTO.setMaterialId(null);
                }
              }
            } else {
              materialThresDTO.setMaterialName(null);
            }
            if (obj[5] != null) {
              if (!DataUtil.isNumber(obj[5].toString().trim())) {
                materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.errType.techThres"));
                materialThresDTO.setTechThresStr(obj[5].toString().trim());
              } else {
                materialThresDTO.setTechThres(Double.valueOf(obj[5].toString().trim()));
                materialThresDTO.setTechThresStr(obj[5].toString().trim());
              }
            } else {
              materialThresDTO.setTechThres(null);
              materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.techThres"));
            }
            if (obj[6] != null) {
              if (!DataUtil.isNumber(obj[6].toString().trim())) {
                materialThresDTO
                    .setResultImport(materialThresDTO.getResultImport() + "\n" + I18n
                        .getLanguage("woMaterial.errType.warningThres"));
                materialThresDTO.setWarningThresStr(obj[6].toString().trim());
              } else {
                materialThresDTO.setWarningThres(Double.valueOf(obj[6].toString().trim()));
                materialThresDTO.setWarningThresStr(obj[6].toString().trim());
              }
            } else {
              materialThresDTO.setWarningThres(null);
              materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.warningThres"));
            }
            if (obj[7] != null) {
              if (!DataUtil.isNumber(obj[7].toString().trim())) {
                materialThresDTO.setResultImport(materialThresDTO.getResultImport() + "\n" + I18n
                    .getLanguage("woMaterial.errType.freeThres"));
                materialThresDTO.setFreeThresStr(obj[7].toString().trim());
              } else {
                materialThresDTO.setFreeThres(Double.valueOf(obj[7].toString().trim()));
                materialThresDTO.setFreeThresStr(obj[7].toString().trim());
              }
            } else {
              materialThresDTO.setFreeThres(null);
              materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.freeThres"));
            }
            if (obj[8] != null) {
              if (!DataUtil.isNumber(obj[8].toString().trim())) {
                materialThresDTO
                    .setResultImport(materialThresDTO.getResultImport() + "\n" + I18n
                        .getLanguage("woMaterial.errType.techDistanctThres"));
                materialThresDTO.setTechDistanctThresStr(obj[8].toString().trim());
              } else {
                materialThresDTO.setTechDistanctThres(Double.valueOf(obj[8].toString().trim()));
                materialThresDTO.setTechDistanctThresStr(obj[8].toString().trim());
              }
            } else {
              materialThresDTO.setTechDistanctThres(null);
            }
            if (obj[9] != null) {
              if (!DataUtil.isNumber(obj[9].toString().trim())) {
                materialThresDTO
                    .setResultImport(materialThresDTO.getResultImport() + "\n" + I18n
                        .getLanguage("woMaterial.errType.warningDistanctThres"));
                materialThresDTO.setWarningDistanctThresStr(obj[9].toString().trim());
              } else {
                materialThresDTO
                    .setWarningDistanctThres(Double.valueOf(obj[9].toString().trim()));
                materialThresDTO.setWarningDistanctThresStr(obj[9].toString().trim());
              }
            } else {
              materialThresDTO.setWarningDistanctThres(null);
            }
            if (obj[10] != null) {
              if (!DataUtil.isNumber(obj[10].toString().trim())) {
                materialThresDTO
                    .setResultImport(materialThresDTO.getResultImport() + "\n" + I18n
                        .getLanguage("woMaterial.errType.freeDistanctThres"));
                materialThresDTO.setFreeDistanctThresStr(obj[10].toString().trim());
              } else {
                materialThresDTO
                    .setFreeDistanctThres(Double.valueOf(obj[10].toString().trim()));
                materialThresDTO.setFreeDistanctThresStr(obj[10].toString().trim());
              }
            } else {
              materialThresDTO.setFreeDistanctThres(null);
            }
            materialThresDTO.setIsEnable(1L);
            MaterialThresInsideDTO materialThresExportDTOTmp = validateImportInfo(
                materialThresDTO, materialThresList);

            if (materialThresExportDTOTmp.getResultImport() == null
                || materialThresExportDTOTmp.getResultImport() == " ") {
              materialThresExportDTOTmp
                  .setResultImport(I18n.getLanguage("woMaterial.result.import"));
              materialThresList.add(materialThresExportDTOTmp);
              materialThresDTOList.add(materialThresDTO);
            } else {
              materialThresList.add(materialThresExportDTOTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!materialThresDTOList.isEmpty()) {
              resultInSideDto = woMaterialRepository
                  .insertOrUpdateListMaterialThres(materialThresDTOList);
            }
          } else {
            File fileExport = exportFileTemplate(materialThresList, Constants.RESULT_IMPORT);
            resultInSideDto.setKey(Constants.RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(Constants.RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileTemplate(materialThresList, Constants.RESULT_IMPORT);
          resultInSideDto.setFile(fileExport);
          return resultInSideDto;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(Constants.RESULT.ERROR);
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
    if (count != 11) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woMaterial.technology") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woMaterial.serviceName") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woMaterial.actionName") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woMaterial.materialName") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woMaterial.techThres") + "*")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woMaterial.warningThres") + "*")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woMaterial.freeThres") + "*")
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("woMaterial.techDistanctThres")
        .equalsIgnoreCase(objects[8].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("woMaterial.warningDistanctThres")
        .equalsIgnoreCase(objects[9].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("woMaterial.freeDistanctThresStr")
        .equalsIgnoreCase(objects[10].toString().trim())) {
      return false;
    }
    return true;
  }

  ;

  private MaterialThresInsideDTO validateImportInfo(MaterialThresInsideDTO materialThresDTO,
      List<MaterialThresInsideDTO> list) {
    if (StringUtils.isStringNullOrEmpty(materialThresDTO.getTechnology())) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.technology"));
      return materialThresDTO;
    }

    if (StringUtils.isStringNullOrEmpty(materialThresDTO.getServiceName())) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.serviceName"));
      return materialThresDTO;
    }

    if (StringUtils.isStringNullOrEmpty(materialThresDTO.getActionName())) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.actionName"));
      return materialThresDTO;
    }

    if (StringUtils.isStringNullOrEmpty(materialThresDTO.getMaterialName())) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.materialName"));
      return materialThresDTO;
    }

    if (materialThresDTO.getInfraType() == null) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.infraType.exist"));
      return materialThresDTO;
    }
    if (materialThresDTO.getActionId() == null) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.action.exist"));
      return materialThresDTO;
    }
    if (materialThresDTO.getServiceId() == null) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.service.exist"));
      return materialThresDTO;
    }
    if (materialThresDTO.getMaterialId() == null) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.material.exist"));
      return materialThresDTO;
    }

    if (!fomatDouble(String.valueOf(materialThresDTO.getTechThres()))) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.techThres.tooLong"));
      return materialThresDTO;
    }

    if (!fomatDouble(String.valueOf(materialThresDTO.getWarningThres()))) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.warningThres.tooLong"));
      return materialThresDTO;
    }

    if (!fomatDouble(String.valueOf(materialThresDTO.getFreeThres()))) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.freeThres.tooLong"));
      return materialThresDTO;
    }
    if (!fomatDouble(String.valueOf(materialThresDTO.getTechDistanctThres()))) {
      materialThresDTO
          .setResultImport(I18n.getLanguage("woMaterial.err.techDistanctThres.tooLong"));
      return materialThresDTO;
    }

    if (!fomatDouble(String.valueOf(materialThresDTO.getWarningDistanctThres()))) {
      materialThresDTO
          .setResultImport(I18n.getLanguage("woMaterial.err.warningDistanctThres.tooLong"));
      return materialThresDTO;
    }

    if (!fomatDouble(String.valueOf(materialThresDTO.getFreeDistanctThres()))) {
      materialThresDTO
          .setResultImport(I18n.getLanguage("woMaterial.err.freeDistanctThres.tooLong"));
      return materialThresDTO;
    }

    MaterialThresInsideDTO materialThresDTOTmp = woMaterialRepository
        .checkMaterialThresExist(materialThresDTO.getInfraType()
            , materialThresDTO.getActionId()
            , materialThresDTO.getServiceId()
            , materialThresDTO.getMaterialId());
    if (materialThresDTOTmp != null) {
      materialThresDTO.setResultImport(I18n.getLanguage("woMaterial.err.duplicate"));
      return materialThresDTO;
    }

    if (list != null && list.size() > 0) {
      materialThresDTO = validateDuplicate(list, materialThresDTO);
    }

    return materialThresDTO;
  }

  private MaterialThresInsideDTO validateDuplicate(List<MaterialThresInsideDTO> list,
      MaterialThresInsideDTO materialThresDTO) {
    for (int i = 0; i < list.size(); i++) {
      MaterialThresInsideDTO materialThresTmp = list.get(i);
      if (materialThresTmp.getTechnology().equals(materialThresDTO.getTechnology())
          && materialThresTmp.getActionName().equals(
          materialThresDTO.getActionName()) && materialThresTmp.getServiceName()
          .equals(materialThresDTO.getServiceName()) && materialThresTmp
          .getMaterialName().equals(materialThresDTO.getMaterialName())) {
        materialThresDTO
            .setResultImport(I18n.getLanguage("woMaterial.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return materialThresDTO;
  }

  private boolean fomatDouble(String item) {
    int i = item.lastIndexOf('.');
    if (i == -1 || item.substring(i + 2).length() <= 1) {
      return true;
    }
    return false;
  }

  private File exportFileTemplate(List<MaterialThresInsideDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("woMaterial.export.title");
    String title = I18n.getLanguage("woMaterial.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("technology", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("actionName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("materialName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("techThresStr", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("warningThresStr", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("freeThresStr", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("techDistanctThresStr", "LEFT", false, 0, 0,
        new String[]{}, null, "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("warningDistanctThresStr", "LEFT", false, 0, 0,
        new String[]{}, null, "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("freeDistanctThresStr", "LEFT", false, 0, 0,
        new String[]{}, null, "STRING");
    headerExportList.add(columnSheet);

    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = WO_MATERIAL_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("woMaterial.export.importDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      fileNameOut = WO_MATERIAL_EXPORT;
      subTitle = I18n
          .getLanguage("woMaterial.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.woMaterial"
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


}
