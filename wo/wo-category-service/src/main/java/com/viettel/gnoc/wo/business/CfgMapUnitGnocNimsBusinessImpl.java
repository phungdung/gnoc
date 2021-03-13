package com.viettel.gnoc.wo.business;

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
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.CfgMapUnitGnocNimsDTO;
import com.viettel.gnoc.wo.repository.CfgMapUnitGnocNimsRepository;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
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
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class CfgMapUnitGnocNimsBusinessImpl implements CfgMapUnitGnocNimsBusiness {

  @Autowired
  protected CfgMapUnitGnocNimsRepository cfgMapUnitGnocNimsRepository;

  @Autowired
  protected CatItemRepository catItemRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;

  @Value("${application.temp.folder}")
  private String tempFolder;
  List<CfgMapUnitGnocNimsDTO> cfgMapUnitGnocNimsDTOList = new ArrayList<>();
  Map<String, String> mapBusinessName = new HashMap<>();
  private final static String CFG_MAP_UNIT_GNOC_NIMS_RESULT_IMPORT = "CFG_MAP_UNIT_GNOC_NIMS_RESULT_IMPORT";
  private final static String CFG_MAP_UNIT_GNOC_NIMS_EXPORT = "CFG_MAP_UNIT_GNOC_NIMS_EXPORT";

  @Override
  public Datatable getListCfgMapUnitGnocNimsPage(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    log.debug("Request to getListCfgMapUnitGnocNimsPage : {}", cfgMapUnitGnocNimsDTO);
    return cfgMapUnitGnocNimsRepository.getListCfgMapUnitGnocNimsPage(cfgMapUnitGnocNimsDTO);
  }

  @Override
  public ResultInSideDto delete(Long id) {
    log.debug("Request to delete : {}", id);
    ResultInSideDto resultInSideDto;
    CfgMapUnitGnocNimsDTO oldHis = findById(id);
    resultInSideDto = cfgMapUnitGnocNimsRepository.delete(id);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(id.toString());
        dataHistoryChange.setType("WO_CONFIG_MAP_UNIT_GNOC_NIMS");
        dataHistoryChange.setActionType("delete");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new CfgMapUnitGnocNimsDTO());
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
  public ResultInSideDto insert(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    log.debug("Request to insert : {}", cfgMapUnitGnocNimsDTO);
    ResultInSideDto resultInSideDto;
    //fix luu BusinessName -Start
    if (cfgMapUnitGnocNimsDTO != null && !StringUtils
        .isStringNullOrEmpty(cfgMapUnitGnocNimsDTO.getBusinessCode())) {
      CatItemDTO catItemDTO = catItemRepository
          .getCatItemById(cfgMapUnitGnocNimsDTO.getBusinessCode());
      if (catItemDTO != null) {
        cfgMapUnitGnocNimsDTO.setBusinessName(catItemDTO.getItemValue());
      }
    }
    //fix luu BusinessName -End
    resultInSideDto = cfgMapUnitGnocNimsRepository.add(cfgMapUnitGnocNimsDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        List<String> keys = getAllKeysDTO();
        UserToken userToken = ticketProvider.getUserToken();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("WO_CONFIG_MAP_UNIT_GNOC_NIMS");
        dataHistoryChange.setActionType("add");
        //Old Object History
        dataHistoryChange.setOldObject(new CfgMapUnitGnocNimsDTO());
        //New Object History
        dataHistoryChange.setNewObject(cfgMapUnitGnocNimsDTO);
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
  public ResultInSideDto update(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    log.debug("Request to update : {}", cfgMapUnitGnocNimsDTO);
    ResultInSideDto resultInSideDto;
    //fix luu BusinessName Start
    CfgMapUnitGnocNimsDTO oldHis = null;
    if (cfgMapUnitGnocNimsDTO != null) {
      if (!StringUtils
          .isStringNullOrEmpty(cfgMapUnitGnocNimsDTO.getBusinessCode())) {
        CatItemDTO catItemDTO = catItemRepository
            .getCatItemById(cfgMapUnitGnocNimsDTO.getBusinessCode());
        if (catItemDTO != null) {
          cfgMapUnitGnocNimsDTO.setBusinessName(catItemDTO.getItemValue());
        }
      }
      //fix luu BusinessName End
      oldHis = findById(cfgMapUnitGnocNimsDTO.getId());
    }

    resultInSideDto = cfgMapUnitGnocNimsRepository.edit(cfgMapUnitGnocNimsDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(cfgMapUnitGnocNimsDTO.getId().toString());
        dataHistoryChange.setType("WO_CONFIG_MAP_UNIT_GNOC_NIMS");
        dataHistoryChange.setActionType("update");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(cfgMapUnitGnocNimsDTO);
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
  public CfgMapUnitGnocNimsDTO findById(Long id) {
    log.debug("Request to findById : {}", id);
    return cfgMapUnitGnocNimsRepository.findById(id);
  }


  public void setMapBusinessName() {
    Datatable businessMaster = catItemRepository
        .getItemMaster(CATEGORY.CFG_MAP_GNOC_NIMS_BUSINESS, LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), "itemId", "itemName");
    List<CatItemDTO> lstBusinessName = (List<CatItemDTO>) businessMaster.getData();
    if (lstBusinessName != null && !lstBusinessName.isEmpty()) {
      for (CatItemDTO catItemDTO : lstBusinessName) {
        mapBusinessName.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
  }

  @Override
  public File getTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("cfgMapUnitGnocNims.unitNimsCode"),
        I18n.getLanguage("cfgMapUnitGnocNims.unitGnocCode"),
        I18n.getLanguage("cfgMapUnitGnocNims.businessName")
    };

    String[] headerStar = new String[]{
        I18n.getLanguage("cfgMapUnitGnocNims.unitNimsCode"),
        I18n.getLanguage("cfgMapUnitGnocNims.unitGnocCode"),
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int businessNameColumn = listHeader
        .indexOf(I18n.getLanguage("cfgMapUnitGnocNims.businessName"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(0);
    titleRow.setHeightInPoints(30);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("cfgMapUnitGnocNims.title"));
    titleCell.setCellStyle(style.get("title"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

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
    sheetOne.setColumnWidth(0, 3000);

    int row = 5;
    Datatable businessMaster = catItemRepository
        .getItemMaster(CATEGORY.CFG_MAP_GNOC_NIMS_BUSINESS, LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            APPLIED_BUSSINESS.CAT_ITEM.toString(), "itemId", "itemName");
    List<CatItemDTO> businessNameList = (List<CatItemDTO>) businessMaster.getData();
    for (CatItemDTO dto : businessNameList) {
      excelWriterUtils.createCell(sheetParam, 3, row++, dto.getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);

    Name businessName = workbook.createName();
    businessName.setNameName("businessName");
    businessName.setRefersToFormula("param!$D$2:$D$" + row);

    XSSFDataValidationConstraint businessNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "businessName");
    CellRangeAddressList businessNameCreate = new CellRangeAddressList(5, 65000, businessNameColumn,
        businessNameColumn);
    XSSFDataValidation dataValidationBusinessName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            businessNameConstraint, businessNameCreate);
    dataValidationBusinessName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationBusinessName);

    workbook.setSheetName(0, I18n.getLanguage("cfgMapUnitGnocNims.title"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_CFG_MAP_UNIT_GNOC_NIMS" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File exportData(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) throws Exception {
    List<CfgMapUnitGnocNimsDTO> cfgMapUnitGnocNimsDTOList = (List<CfgMapUnitGnocNimsDTO>) cfgMapUnitGnocNimsRepository
        .getListDataExport(cfgMapUnitGnocNimsDTO).getData();
    return exportFileTemplate(cfgMapUnitGnocNimsDTOList, "");
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<CfgMapUnitGnocNimsDTO> exportDTOList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
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
            3,
            1000
        );
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
            3,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }

        cfgMapUnitGnocNimsDTOList = new ArrayList<>();

        if (!dataImportList.isEmpty()) {
          int row = 7;
          int index = 0;
          setMapBusinessName();
          for (Object[] obj : dataImportList) {
            CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO = new CfgMapUnitGnocNimsDTO();
            if (obj[1] != null) {
              cfgMapUnitGnocNimsDTO.setUnitNimsCode(obj[1].toString().trim());

            } else {
              cfgMapUnitGnocNimsDTO.setUnitNimsCode(null);
            }
            if (obj[2] != null) {
              cfgMapUnitGnocNimsDTO.setUnitGnocCode(obj[2].toString().trim());
            } else {
              cfgMapUnitGnocNimsDTO.setUnitGnocCode(null);
            }
            if (obj[3] != null) {
              cfgMapUnitGnocNimsDTO.setBusinessName(obj[3].toString().trim());
              for (Map.Entry<String, String> item : mapBusinessName.entrySet()) {
                if (cfgMapUnitGnocNimsDTO.getBusinessName().equals(item.getValue())) {
                  cfgMapUnitGnocNimsDTO.setBusinessCode(Long.valueOf(item.getKey()));
                  break;
                } else {
                  cfgMapUnitGnocNimsDTO.setBusinessCode(null);
                }
              }
            } else {
              cfgMapUnitGnocNimsDTO.setBusinessName(null);
            }

            CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTOTmp = validateImportInfo(
                cfgMapUnitGnocNimsDTO, exportDTOList);

            if (cfgMapUnitGnocNimsDTOTmp.getResultImport() == null) {
              cfgMapUnitGnocNimsDTOTmp
                  .setResultImport(I18n.getLanguage("cfgMapUnitGnocNims.result.import"));
              exportDTOList.add(cfgMapUnitGnocNimsDTOTmp);
              cfgMapUnitGnocNimsDTOList.add(cfgMapUnitGnocNimsDTO);
            } else {
              exportDTOList.add(cfgMapUnitGnocNimsDTOTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!cfgMapUnitGnocNimsDTOList.isEmpty()) {
              //fix luu BusinessName Start
              for (CfgMapUnitGnocNimsDTO gnocNimsDTO : cfgMapUnitGnocNimsDTOList) {
                if (gnocNimsDTO != null && !StringUtils
                    .isStringNullOrEmpty(gnocNimsDTO.getBusinessCode())) {
                  CatItemDTO catItemDTO = catItemRepository
                      .getCatItemById(gnocNimsDTO.getBusinessCode());
                  if (catItemDTO != null) {
                    gnocNimsDTO.setBusinessName(catItemDTO.getItemValue());
                  }
                }
                resultInSideDto = cfgMapUnitGnocNimsRepository.insertImport(gnocNimsDTO);
              }
              //fix luu BusinessName End
            }
          } else {
            File fileExport = exportFileTemplate(exportDTOList, Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileTemplate(exportDTOList, Constants.RESULT_IMPORT);
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
    if (count != 4) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgMapUnitGnocNims.unitNimsCode") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfgMapUnitGnocNims.unitGnocCode") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("cfgMapUnitGnocNims.businessName")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    return true;
  }

  private CfgMapUnitGnocNimsDTO validateImportInfo(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO,
      List<CfgMapUnitGnocNimsDTO> lstoExportDTOS) {
    if (StringUtils.isStringNullOrEmpty(cfgMapUnitGnocNimsDTO.getUnitNimsCode())) {
      cfgMapUnitGnocNimsDTO
          .setResultImport(I18n.getLanguage("cfgMapUnitGnocNims.err.unitNimsCode"));
      return cfgMapUnitGnocNimsDTO;
    }

    if (StringUtils.isStringNullOrEmpty(cfgMapUnitGnocNimsDTO.getUnitGnocCode())) {
      cfgMapUnitGnocNimsDTO
          .setResultImport(I18n.getLanguage("cfgMapUnitGnocNims.err.unitGnocCode"));
      return cfgMapUnitGnocNimsDTO;
    }

    if (StringUtils.isNotNullOrEmpty(cfgMapUnitGnocNimsDTO.getBusinessName()) && StringUtils.isStringNullOrEmpty(cfgMapUnitGnocNimsDTO.getBusinessCode())) {
      cfgMapUnitGnocNimsDTO
          .setResultImport(I18n.getLanguage("cfgMapUnitGnocNims.err.businessName.exist"));
      return cfgMapUnitGnocNimsDTO;
    }

    if (cfgMapUnitGnocNimsDTO.getUnitNimsCode().length() > 500) {
      cfgMapUnitGnocNimsDTO
          .setResultImport(I18n.getLanguage("cfgMapUnitGnocNims.err.unitNimsCode.tooLong"));
    }

    if (cfgMapUnitGnocNimsDTO.getUnitGnocCode().length() > 500) {
      cfgMapUnitGnocNimsDTO
          .setResultImport(I18n.getLanguage("cfgMapUnitGnocNims.err.unitGnocCode.tooLong"));
    }

    CfgMapUnitGnocNimsDTO mapUnitGnocNimsDTO = cfgMapUnitGnocNimsRepository
        .checkCfgMapUnitGnocNimsExist(cfgMapUnitGnocNimsDTO.getUnitNimsCode()
            , cfgMapUnitGnocNimsDTO.getUnitGnocCode(), cfgMapUnitGnocNimsDTO.getBusinessCode());
    if (mapUnitGnocNimsDTO != null) {
      cfgMapUnitGnocNimsDTO
          .setResultImport(I18n.getLanguage("cfgMapUnitGnocNims.err.duplicate"));
    }
    if (lstoExportDTOS != null && lstoExportDTOS.size() > 0) {
      cfgMapUnitGnocNimsDTO = validateDuplicate(lstoExportDTOS, cfgMapUnitGnocNimsDTO);
    }
    return cfgMapUnitGnocNimsDTO;
  }

  private CfgMapUnitGnocNimsDTO validateDuplicate(List<CfgMapUnitGnocNimsDTO> list,
      CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    for (int i = 0; i < list.size(); i++) {
      CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsTmp = list.get(i);
      if (cfgMapUnitGnocNimsTmp.getUnitGnocCode().equals(
          cfgMapUnitGnocNimsDTO.getUnitGnocCode()) && cfgMapUnitGnocNimsTmp.getUnitNimsCode()
          .equals(
              cfgMapUnitGnocNimsDTO.getUnitNimsCode())
          && cfgMapUnitGnocNimsTmp.getBusinessName() == cfgMapUnitGnocNimsDTO
          .getBusinessName()) {
        cfgMapUnitGnocNimsDTO
            .setResultImport(I18n.getLanguage("cfgMapUnitGnocNims.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return cfgMapUnitGnocNimsDTO;
  }

  private File exportFileTemplate(List<CfgMapUnitGnocNimsDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("cfgMapUnitGnocNims.export.title");
    String title = I18n.getLanguage("cfgMapUnitGnocNims.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("unitNimsCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("unitGnocCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("businessName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = CFG_MAP_UNIT_GNOC_NIMS_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("cfgMapUnitGnocNims.export.importDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      fileNameOut = CFG_MAP_UNIT_GNOC_NIMS_EXPORT;
      subTitle = I18n
          .getLanguage("cfgMapUnitGnocNims.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.cfgMapUnitGnocNims"
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

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = CfgMapUnitGnocNimsDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        return keys;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
