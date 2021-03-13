package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.business.LogChangeConfigBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.MappingVsaUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.repository.MappingVsaUnitRepository;
import com.viettel.gnoc.repository.UnitCommonRepository;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
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
@Slf4j
@Transactional
public class MappingVsaUnitBusinessImpl implements MappingVsaUnitBusiness {

  @Autowired
  private MappingVsaUnitRepository mappingVsaUnitRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  private final static String Mapping_Vsa_Unit_Export = "Mapping_Vsa_Unit_Export";

  @Autowired
  protected LogChangeConfigBusiness logChangeConfigBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  private UnitCommonRepository unitCommonRepository;
//
//  Map<String, String> unitMap = new HashMap<>();
//  Map<String, String> vsaMap = new HashMap<>();

  Map<String, Long> mapListUnitId = new HashMap<>();
  Map<String, Long> mapListUnitIdVsa = new HashMap<>();
//  public void setMappUnitAndVsa() {
//    List<UnitDTO> unitCode = unitCommonRepository.getListUnit(new UnitDTO());
//    if (unitCode != null && unitCode.size() > 0) {
//      for (UnitDTO dto : unitCode) {
//        unitMap.put(dto.getUnitCode(), dto.getUnitId().toString());
//      }
//    }
//
//    List<UnitDTO> vsaUnitCode = unitCommonRepository.getListUnitVSANotName(new UnitDTO());
//    if (vsaUnitCode != null && vsaUnitCode.size() > 0) {
//      for (UnitDTO dto : vsaUnitCode) {
//        vsaMap.put(dto.getUnitCode(), dto.getUnitId().toString());
//      }
//    }
//  }

  private void clearMapObject() {
    mapListUnitId.clear();
    mapListUnitIdVsa.clear();
  }

  @Override
  public Datatable getListMappingVsaUnitDTO(MappingVsaUnitDTO mappingVsaUnitDTO) {
    return mappingVsaUnitRepository.getListMappingVsaUnitDTO(mappingVsaUnitDTO);
  }

  @Override
  public ResultInSideDto updateMappingVsaUnit(MappingVsaUnitDTO mappingVsaUnitDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    if (!StringUtils.isStringNullOrEmpty(mappingVsaUnitDTO.getVsaUnit())) {
      boolean check = validateDuplicate(mappingVsaUnitDTO);
      if (check) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto.setMessage(I18n.getLanguage("mappingVSA.msg.duplicate"));
        return resultInSideDto;
      }
      for (Long item : mappingVsaUnitDTO.getVsaUnit()) {
        MappingVsaUnitDTO dto = new MappingVsaUnitDTO();
        dto.setAppUnitId(mappingVsaUnitDTO.getAppUnitId());
        dto.setVsaUnitId(item);

        resultInSideDto = mappingVsaUnitRepository
            .updateMappingVsaUnit(mappingVsaUnitDTO);

        if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
          logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
              "Update MappingVsaUnit", "Update MappingVsaUnit" + " " + resultInSideDto.getId(),
              mappingVsaUnitDTO, null));
        }
      }
    }

    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMappingVsaUnit(Long id) {
    ResultInSideDto resultInSideDto = mappingVsaUnitRepository.deleteMappingVsaUnit(id);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete MappingVsaUnit", "Delete MappingVsaUnit" + " " + resultInSideDto.getId(),
          id, null));
    }

    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListMappingVsaUnit(List<MappingVsaUnitDTO> mappingVsaUnitListDTO) {
    return mappingVsaUnitRepository.deleteListMappingVsaUnit(mappingVsaUnitListDTO);
  }

  @Override
  public MappingVsaUnitDTO findMappingVsaUnitById(Long id) {
    return mappingVsaUnitRepository.findMappingVsaUnitById(id);
  }

  public boolean validateDuplicate(MappingVsaUnitDTO mappingVsaUnitDTO) {
    for (Long item : mappingVsaUnitDTO.getVsaUnit()) {
      MappingVsaUnitDTO dto = new MappingVsaUnitDTO();
      dto.setAppUnitId(mappingVsaUnitDTO.getAppUnitId());
      dto.setVsaUnitId(item);

      List<MappingVsaUnitDTO> list = mappingVsaUnitRepository.checkExistVsaUnit(dto);
      if (list != null && list.size() > 0) {
        return true;
      }
    }
    return false;
  }

  @Override
  public ResultInSideDto insertMappingVsaUnit(MappingVsaUnitDTO mappingVsaUnitDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    if (!StringUtils.isStringNullOrEmpty(mappingVsaUnitDTO.getVsaUnit())) {
      boolean check = validateDuplicate(mappingVsaUnitDTO);
      if (check) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto.setMessage(I18n.getLanguage("mappingVSA.msg.duplicate"));
        return resultInSideDto;
      }

      for (Long item : mappingVsaUnitDTO.getVsaUnit()) {
        MappingVsaUnitDTO dto = new MappingVsaUnitDTO();
        dto.setAppUnitId(mappingVsaUnitDTO.getAppUnitId());
        dto.setVsaUnitId(item);

        resultInSideDto = mappingVsaUnitRepository
            .insertMappingVsaUnit(dto);

        if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
          logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
              "Insert MappingVsaUnit", "Insert MappingVsaUnit" + " " + resultInSideDto.getId(),
              dto, null));
        }
      }
    }

    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertMappingVsaUnitImport(MappingVsaUnitDTO mappingVsaUnitDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<MappingVsaUnitDTO> list = mappingVsaUnitRepository.checkExistVsaUnit(mappingVsaUnitDTO);
    if (list.size() > 0) {
      resultInSideDto.setKey(RESULT.DUPLICATE);
      resultInSideDto.setMessage(I18n.getLanguage("mappingVSA.msg.duplicate"));
      return resultInSideDto;
    }
    return resultInSideDto = mappingVsaUnitRepository
        .insertMappingVsaUnit(mappingVsaUnitDTO);
  }

  @Override
  public ResultInSideDto insertOrUpdateListMappingVsaUnit(
      List<MappingVsaUnitDTO> mappingVsaUnitDTO) {
    ResultInSideDto resultInSideDto = mappingVsaUnitRepository
        .insertOrUpdateListMappingVsaUnit(mappingVsaUnitDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Insert MappingVsaUnit", "Insert MappingVsaUnit" + " " + resultInSideDto.getId(),
          mappingVsaUnitDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public File getVsaTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFFont fontStar = workbook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    XSSFSheet sheetOne = workbook.getSheetAt(0);

    String[] header = new String[]{
        I18n.getLanguage("mappingVSA.list.appUnitCode"),
        I18n.getLanguage("mappingVSA.list.vsaUnitCode")
    };

    List<String> listHeader = Arrays.asList(header);

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);
    sheetOne.addMergedRegion(new CellRangeAddress(0, 1, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(0);
    titleRow.setHeightInPoints(14);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("vsaUnit.export.title"));
    titleCell.setCellStyle(style.get("title"));

    //Tao note
    sheetOne.addMergedRegion(new CellRangeAddress(2, 3, 0, listHeader.size() - 1));
    Row noteRow = sheetOne.createRow(1);
    noteRow.setHeightInPoints(10);
    Cell noteCell = noteRow.createCell(0);
    noteCell.setCellValue(I18n.getLanguage("mappingVSA.template.excel.note"));
    noteCell.setCellStyle(style.get("note"));

    Row headerRow = sheetOne.createRow(4);
    headerRow.setHeightInPoints(16);

    // create cell excell for template
    int lenHead = listHeader.size();
    for (int i = 0; i < lenHead; i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeader : listHeader) {
        if (checkHeader.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("(*)", fontStar);
        }
      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }

    sheetOne.setColumnWidth(0, 20000);

    workbook.setSheetName(0, I18n.getLanguage("vsaUnit.export.title"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MAPPING_VSA_UNIT" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File getVsaTemplateReference() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    workbook.removeSheetAt(0);
    XSSFSheet sheetUnitCode = workbook.createSheet(I18n.getLanguage("mappingVSA.list.appUnitCode"));
    XSSFSheet sheetVsaUnitCode = workbook
        .createSheet(I18n.getLanguage("mappingVSA.list.vsaUnitCode"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    List<UnitDTO> unitCode = unitCommonRepository.getListUnit(new UnitDTO());
    excelWriterUtils
        .createCell(sheetUnitCode, 0, 0, I18n.getLanguage("mappingVSA.list.appUnitId"),
            style.get("header"));
    excelWriterUtils
        .createCell(sheetUnitCode, 1, 0, I18n.getLanguage("mappingVSA.list.appUnitCode"),
            style.get("header"));

    int row = 1;
    int lengUnitCode = unitCode.size();
    for (int i = 0; i < lengUnitCode; i++) {
      excelWriterUtils
          .createCell(sheetUnitCode, 0, row, unitCode.get(i).getUnitId().toString(),
              style.get("cell"));
      excelWriterUtils
          .createCell(sheetUnitCode, 1, row, unitCode.get(i).getUnitCode(),
              style.get("cell"));
      row++;
    }
    sheetUnitCode.setColumnWidth(0, 13000);
    sheetUnitCode.setColumnWidth(1, 13000);

    List<UnitDTO> vsaUnitCode = unitCommonRepository.getListUnitVSANotNameAndActive(new UnitDTO());
    excelWriterUtils
        .createCell(sheetVsaUnitCode, 0, 0, I18n.getLanguage("mappingVSA.list.vsaUnitId"),
            style.get("header"));
    excelWriterUtils
        .createCell(sheetVsaUnitCode, 1, 0, I18n.getLanguage("mappingVSA.list.vsaUnitCode"),
            style.get("header"));
    excelWriterUtils
        .createCell(sheetVsaUnitCode, 2, 0, I18n.getLanguage("mappingVSA.list.vsaUnitName"),
            style.get("header"));
    row = 1;
    int lenVsa = vsaUnitCode.size();
    for (int i = 0; i < lenVsa; i++) {
      excelWriterUtils
          .createCell(sheetVsaUnitCode, 0, row, vsaUnitCode.get(i).getUnitId().toString(),
              style.get("cell"));
      excelWriterUtils
          .createCell(sheetVsaUnitCode, 1, row, vsaUnitCode.get(i).getUnitCode().toString(),
              style.get("cell"));
      excelWriterUtils
          .createCell(sheetVsaUnitCode, 2, row, vsaUnitCode.get(i).getUnitName(),
              style.get("cell"));
      row++;
    }
    sheetVsaUnitCode.setColumnWidth(0, 13000);
    sheetVsaUnitCode.setColumnWidth(1, 13000);
    sheetVsaUnitCode.setColumnWidth(2, 13000);

    String fileResult = tempFolder + File.separator;
    String fileName = "REF_MAPPING_VSA_UNIT" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }


  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) throws Exception {
    clearMapObject();
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
//    boolean checkDouble = true;
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
            1,
            2
        );

        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> lstDataAll = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            1,
            1000
        );

        if (lstDataAll.size() <= 0) {
          resultInSideDto.setKey(RESULT.NODATA);
          return resultInSideDto;
        }

        if (lstDataAll.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }

        Boolean hasErr = false;

        List<MappingVsaUnitDTO> listMapping = new ArrayList<>();
        if (!lstDataAll.isEmpty()) {
          listMapping = validateDataImport(lstDataAll);
        }

        if (listMapping != null && listMapping.size() > 0) {
          for (MappingVsaUnitDTO item : listMapping) {
            if (!StringUtils.isStringNullOrEmpty(item.getResultImport())) {
              hasErr = true;
            } else {
              item.setResultImport(I18n.getLanguage("mappingVSA.result.import.ok"));
            }
          }

          if (hasErr) {
            File expFile = exportFile(listMapping, "RESULT_IMPORT", true);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(expFile);
          } else {
            for (MappingVsaUnitDTO item : listMapping) {
              Long unitId = mapListUnitId.get(item.getAppUnitCode());
              if (unitId != null) {
                item.setAppUnitId(unitId);
              }

              Long unitIdVsa = mapListUnitIdVsa.get(item.getVsaUnitCode());
              if (unitIdVsa != null) {
                item.setVsaUnitId(unitIdVsa);
              }
              resultInSideDto = insertMappingVsaUnitImport(item);
              if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
                logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
                    "Import Mapping_Vsa_Unit", "Mapping_Vsa_Unit:" + resultInSideDto.getId(),
                    null, null));
              }
            }


          }
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
    if (count != 2) {
      return false;
    }

    if (objects[0] == null) {
      return false;
    }

    if (!(I18n.getLanguage("mappingVSA.list.appUnitCode") + "(*)")
        .equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mappingVSA.list.vsaUnitCode") + "(*)")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    return true;
  }

  private List<MappingVsaUnitDTO> validateDataImport(List<Object[]> lstDataAll) {
//    List<String> lstDuplicateInfile = new ArrayList<>();
    Map<String, String> mapDulicapte = new HashMap<>();
    int k = 1;
    List<MappingVsaUnitDTO> listAddOrUpdate = new ArrayList<>();

    // unitVsa
    Map<String, UnitDTO> listMappingVsa = new HashMap<>();
    List<UnitDTO> lstVSAMapping = mappingVsaUnitRepository.checkExistUnitNotActive(new UnitDTO());
    for (UnitDTO dto : lstVSAMapping) {
      listMappingVsa.put(dto.getUnitCode(), dto);
    }
    // unitApp
    Map<String, UnitDTO> listUnitApp = new HashMap<>();
    List<UnitDTO> listUnitAppMapping = unitCommonRepository.getListUnitNotLike(new UnitDTO());
    for (UnitDTO dto : listUnitAppMapping) {
      listUnitApp.put(dto.getUnitCode(), dto);
    }

    for (Object[] objects : lstDataAll) {
      UnitDTO unitDTOVsa = new UnitDTO();
      UnitDTO unitDTOApp = new UnitDTO();
      MappingVsaUnitDTO mappingVSAUnitResult = new MappingVsaUnitDTO();
      String ret = "";
      if (StringUtils.isStringNullOrEmpty(objects[1])) {
        ret += I18n.getLanguage("mappingVSA.result.import.vsaCode.null");
      } else {
        unitDTOVsa = listMappingVsa.get(objects[1].toString());
        if (unitDTOVsa == null) {
          ret += I18n.getLanguage("mappingVSA.result.import.vsaCode.noExits");
        } else {
          // Xử lý không hiệu lực
          if (!unitDTOVsa.getStatusStr().equals("0")) {
            ret += I18n.getLanguage("mappingVSA.result.import.vsaCode.noExitsActive");
          } else {
            mapListUnitIdVsa.put(unitDTOVsa.getUnitCode(), unitDTOVsa.getUnitId());
          }
        }
        // set id for vsa from unit
        mappingVSAUnitResult.setVsaUnitCode(objects[1].toString());
      }
      if (StringUtils.isStringNullOrEmpty(objects[0])) {
        ret += I18n.getLanguage("mappingVSA.result.import.unitCode.null");
      } else {
        unitDTOApp = listUnitApp.get(objects[0].toString());
        if (unitDTOApp == null) {
          ret += I18n.getLanguage("mappingVSA.result.import.unitCode.noExist");
        } else {
          mapListUnitId.put(unitDTOApp.getUnitCode(), unitDTOApp.getUnitId());
        }
        // set id for unit from unit

        mappingVSAUnitResult.setAppUnitCode(objects[0].toString());
      }

      if (!StringUtils.isStringNullOrEmpty(ret)) {
        mappingVSAUnitResult.setResultImport(ret);
      } else if (unitDTOVsa != null && unitDTOVsa.getUnitId() != null && unitDTOApp != null
          && unitDTOApp.getUnitId() != null) {
        mappingVSAUnitResult.setAppUnitId(unitDTOApp.getUnitId());
        mappingVSAUnitResult.setVsaUnitId(unitDTOVsa.getUnitId());
        List<MappingVsaUnitDTO> check = mappingVsaUnitRepository
            .checkExistUnitAndVsaUnit(mappingVSAUnitResult);
        if (check != null && !check.isEmpty()) {
          mappingVSAUnitResult.setResultImport(I18n.getLanguage("mappingVSA.result.import.Exist"));
        }
      }

      if (!StringUtils.isStringNullOrEmpty(mappingVSAUnitResult.getAppUnitCode())
          && !StringUtils.isStringNullOrEmpty(mappingVSAUnitResult.getVsaUnitCode())) {
        if (mapDulicapte
            .get(mappingVSAUnitResult.getAppUnitCode().trim() + mappingVSAUnitResult
                .getVsaUnitCode().trim()) == null) {
          mapDulicapte.put(mappingVSAUnitResult.getAppUnitCode().trim() + mappingVSAUnitResult
              .getVsaUnitCode().trim(), k + "");

        } else {
          String position = mapDulicapte
              .get(mappingVSAUnitResult.getAppUnitCode().trim() + mappingVSAUnitResult
                  .getVsaUnitCode().trim());
          ret += I18n.getLanguage("mappingVSA.result.import.vsaCode.Exits", position);
          mappingVSAUnitResult.setResultImport(ret);
        }
      }
      listAddOrUpdate.add(mappingVSAUnitResult);
      k++;
    }

    return listAddOrUpdate;
  }

  private File exportFile(List<MappingVsaUnitDTO> lstCrEx, String key, boolean check) throws
      Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("vsaUnit.export.title");
    String title = I18n.getLanguage("vsaUnit.export.title");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();

    columnSheet = new ConfigHeaderExport("appUnitCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("vsaUnitCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    if (check) {
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
    }

    if (Constants.RESULT_IMPORT.equals(key)) {

    } else {
      fileNameOut = Mapping_Vsa_Unit_Export;
      subTitle = I18n
          .getLanguage("UnitCommon.export.eportDate", dateFormat.format(new Date()));
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstCrEx,
        sheetName,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.mappingVSA.list",
        lstHeaderSheet,
        fieldSplit,
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
}
