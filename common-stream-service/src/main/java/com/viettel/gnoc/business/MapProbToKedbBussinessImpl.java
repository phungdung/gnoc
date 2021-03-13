package com.viettel.gnoc.business;

//import com.viettel.bccs.cc.service.ProblemGroupDTO;
//import com.viettel.bccs.cc.service.ProblemTypeDTO;

import com.viettel.bccs.cc.service.ProblemGroupDTO;
import com.viettel.bccs.cc.service.ProblemTypeDTO;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MapProbToKedbDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import com.viettel.gnoc.repository.MapProbToKedbRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.viettel.gnoc.incident.repository.CfgServerNocRepository;
//import com.viettel.gnoc.incident.repository.MapProbToKedbRepository;
//import com.viettel.gnoc.incident.utils.WSBCCS2Port;

@Service
@Transactional
@Slf4j
public class MapProbToKedbBussinessImpl implements MapProbToKedbBussiness {


  @Autowired
  MapProbToKedbRepository mapProbToKedbRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;
  //  List<MapProbToKedbDTO> mapProbToKedbDTOS = new ArrayList<>();
//  Map<Long, String> mapGroup = new HashMap<>();
//  Map<Long, String> mapKind = new HashMap<>();
//  Map<Long, String> mapType = new HashMap<>();
  private final static String EXPORT_MAP_PROB_TO_KEDB = "EXPORT_MAP_PROB_TO_KEDB";
  private final static String MAP_PROB_TO_KEDB_RESULT_IMPORT = "MAP_PROB_TO_KEDB_RESULT_IMPORT";

  //
  //Lay danh sach Nhom
  @Override
  public List<ProblemGroupDTO> getListGroup() {
    return mapProbToKedbRepository.getListGroup();
  }

  //Lay danh sach the loai
  @Override
  public List<ProblemGroupDTO> getListKind(Long probGroupId) {
    return mapProbToKedbRepository.getListKind(probGroupId);
  }

  //Lay danh sach loai
  @Override
  public List<ProblemTypeDTO> getListType(Long probGroupId) {
    return mapProbToKedbRepository.getListType(probGroupId);
  }


  //Lay bai hoc kinh nghiem
  @Override
  public Datatable getListKedbDTO(KedbDTO kedbDTO) {
    return mapProbToKedbRepository.getListKedbDTO(kedbDTO);
  }

  @Override
  public Datatable getListMapProbToKedbDTO(MapProbToKedbDTO mapProbToKedbDTO) {
    return mapProbToKedbRepository.getListMapProbToKedbDTO(mapProbToKedbDTO);
  }

  @Override
  public ResultInSideDto insertMapProbToKedb(MapProbToKedbDTO mapProbToKedbDTO) {
    log.debug("Request to insertWoCdGroup: {}", mapProbToKedbDTO);
    return mapProbToKedbRepository.insertMapProbToKedb(mapProbToKedbDTO);
  }

  @Override
  public MapProbToKedbDTO getDetail(Long id) {
    log.debug("Request to getDetail : {}", id);
    return mapProbToKedbRepository.getDetail(id);
  }

  @Override
  public ResultInSideDto updateMapProbToKedb(MapProbToKedbDTO mapProbToKedbDTO) {
    log.debug("Request to updateWoFileTemp : {}", mapProbToKedbDTO);
    return mapProbToKedbRepository.updateMapProbToKedb(mapProbToKedbDTO);
  }

  @Override
  public ResultInSideDto deleteMapProbToKedb(Long id) {
    log.debug("Request to deleteWoFileTempById : {}", id);
    return mapProbToKedbRepository.deleteMapProbToKedb(id);
  }

  @Override
  public File exportData(MapProbToKedbDTO mapProbToKedbDTO) throws Exception {
    List<MapProbToKedbDTO> mapProbToKedbDTOList = mapProbToKedbRepository
        .getDataExport(mapProbToKedbDTO);

    return exportFileTemplate(mapProbToKedbDTOList, "", "0");
  }

  private File exportFileTemplate(List<MapProbToKedbDTO> dtoList, String key, String isImportError)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("mapProbToKedb.export.title");
    String title = I18n.getLanguage("mapProbToKedb.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("probTypeNameLv1", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("probTypeNameLv2", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("probTypeNameLv3", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");

    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("kedbCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = MAP_PROB_TO_KEDB_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("mapProbToKedb.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("action", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      fileNameOut = EXPORT_MAP_PROB_TO_KEDB;
      subTitle = I18n
          .getLanguage("mapProbToKedb.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.mapProbToKedb"
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
//
//  private boolean validFileFormat(List<Object[]> headerList) {
//    Object[] objects = headerList.get(0);
//    if (objects == null) {
//      return false;
//    }
//    int count = 0;
//    for (Object data : objects) {
//      if (data != null) {
//        count += 1;
//      }
//    }
//    if (count != 6) {
//      return false;
//    }
//    if (objects[0] == null) {
//      return false;
//    }
////    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
////      return false;
////    }
//    if (!(I18n.getLanguage("mapProbToKedb.probTypeNameLv1") + "*")
//        .equalsIgnoreCase(objects[1].toString().trim())) {
//      return false;
//    }
//    if (!(I18n.getLanguage("mapProbToKedb.probTypeNameLv2") + "*")
//        .equalsIgnoreCase(objects[2].toString().trim())) {
//      return false;
//    }
//    if (!(I18n.getLanguage("mapProbToKedb.probTypeNameLv3") + "*")
//        .equalsIgnoreCase(objects[3].toString().trim())) {
//      return false;
//    }
//    if (!(I18n.getLanguage("mapProbToKedb.kedbCode") + "*")
//        .equalsIgnoreCase(objects[4].toString().trim())) {
//      return false;
//    }
//
//    return true;
//  }

//  public void setMapGroup() {
//    List<ProblemGroupDTO> lstGroup = mapProbToKedbRepository
//        .getListGroup();
//    if (lstGroup != null && !lstGroup.isEmpty()) {
//      for (ProblemGroupDTO itemGroup : lstGroup) {
//        mapGroup.put(Long.valueOf(itemGroup.getProbGroupId()), itemGroup.getName());
//      }
//    }
//  }
//
//  public void setMapKind(Long probGroupId) {
//    List<ProblemGroupDTO> lstKind = mapProbToKedbRepository
//        .getListKind(probGroupId);
//    if (lstKind != null && !lstKind.isEmpty()) {
//      for (ProblemGroupDTO itemKind : lstKind) {
//        mapGroup.put(Long.valueOf(itemKind.getProbGroupId()), itemKind.getName());
//      }
//    }
//  }
//
//  public void setMapType(Long probGroupId) {
//    List<ProblemTypeDTO> lstType = mapProbToKedbRepository
//        .getListType(probGroupId);
//    if (lstType != null && !lstType.isEmpty()) {
//      for (ProblemTypeDTO itemType : lstType) {
//        mapGroup.put(Long.valueOf(itemType.getProbGroupId()), itemType.getName());
//      }
//    }
//  }

//  @Override
//  public File getTemplate() throws Exception {
//    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
//    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
//    templatePathOut = StringUtils.removeSeparator(templatePathOut);
//    Resource resource = new ClassPathResource(templatePathOut);
//    InputStream fileTemplate = resource.getInputStream();
//
//    //apache POI XSSF
//    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
//    XSSFSheet sheetOne = workbook.getSheetAt(0);
//    XSSFSheet sheetOther = workbook.createSheet("Other");
//
//    Map<String, List<String>> listKind = new HashMap<>();
//    Map<String, List<String>> listType = new HashMap<>();
//    List<ProblemGroupDTO> groupList = mapProbToKedbRepository.getListGroup();
//    List<String> lstGroupName = new ArrayList<>();
//    List<Long> lstGroupId = new ArrayList<>();
//    for (int i = 0; i < groupList.size(); i++) {
//      lstGroupName.add(groupList.get(i).getName());
//      lstGroupId.add(Long.valueOf(groupList.get(i).getProbGroupId()));
//      List<String> lstKindName = new ArrayList<>();
//      List<Long> lstKindId = new ArrayList<>();
//      List<ProblemGroupDTO> lstKind = mapProbToKedbRepository
//          .getListKind(lstGroupId.get(i));
//      if (lstKind != null && !lstKind.isEmpty()) {
//        for(int j = 0 ; j < lstKind.size();j++){
//          lstKindName.add(lstKind.get(j).getName());
//          lstKindId.add(Long.valueOf(lstKind.get(j).getProbGroupId()));
//          List<String> lstTypeName = new ArrayList<>();
//          List<Long> lstTypeId = new ArrayList<>();
//          List<ProblemTypeDTO> lstType = mapProbToKedbRepository
//              .getListType(lstKindId.get(j));
//          if (lstType != null && !lstType.isEmpty()) {
//            for (ProblemTypeDTO dto : lstType) {
//              lstTypeName.add(dto.getName());
//            }
//            listType.put(lstKindName.get(j), lstTypeName);
//          }else {
//            lstKindName.add("null");
//            listType.put(lstKindName.get(j), lstKindName);
//          }
//        }
//        listKind.put(lstGroupName.get(i), lstKindName);
//      }else {
//        lstKindName.add("null");
//        listKind.put(lstGroupName.get(i), lstKindName);
//      }
//    }
//
//
//    XSSFSheet sheetKind = workbook.createSheet("SheetKind");
//    Row ro1;
//    String col1;
//    String ref1;
//    int c1 = 0;
//    for (String key : listKind.keySet()) {
//      int r1 = 0;
//      ro1 = sheetKind.getRow(r1);
//      if (ro1 == null) {
//        ro1 = sheetKind.createRow(r1);
//      }
//      r1++;
//      ro1.createCell(c1).setCellValue(key);
//      List<String> items = listKind.get(key);
//      if(items != null){
//        if (items.size() > 0) {
//          for (String item : items) {
//            ro1 = sheetKind.getRow(r1);
//            if (ro1 == null) {
//              ro1 = sheetKind.createRow(r1);
//            }
//            r1++;
//            ro1.createCell(c1).setCellValue(item);
//          }
//        }
//      }
//
//      col1 = CellReference.convertNumToColString(c1);
//      Name type = workbook.createName();
//      type.setNameName(key);
//      ref1 = "SheetKind!$" + col1 + "$2:$" + col1 + "$" + r1;
//      type.setRefersToFormula(ref1);
//      c1++;
//    }
//
//    XSSFSheet sheetType = workbook.createSheet("SheetType");
//    Row ro2;
//    String col2;
//    String ref2;
//    int c2 = 0;
//    for (String key : listType.keySet()) {
//      int r2 = 0;
//      ro2 = sheetType.getRow(r2);
//      if (ro2 == null) {
//        ro2 = sheetType.createRow(r2);
//      }
//      r2++;
//      ro2.createCell(c2).setCellValue(key);
//      List<String> items = listType.get(key);
//      if(items != null){
//        if (items.size() > 0) {
//          for (String item : items) {
//            ro1 = sheetType.getRow(r2);
//            if (ro2 == null) {
//              ro2 = sheetType.createRow(r2);
//            }
//            r2++;
//            ro2.createCell(c2).setCellValue(item);
//          }
//        }
//      }
//      col2 = CellReference.convertNumToColString(c2);
//      Name type = workbook.createName();
//      type.setNameName(key);
//      ref2 = "SheetType!$" + col2 + "$2:$" + col2 + "$" + r2;
//      type.setRefersToFormula(ref2);
//      c2++;
//    }
//    col1 = CellReference.convertNumToColString((c1 - 1));
//    col2 = CellReference.convertNumToColString((c2 - 1));
//    Name groupName = workbook.createName();
//    groupName.setNameName("GroupName");
//    ref1 = "SheetKind!$A$1:$" + col1 + "$1";
//    groupName.setRefersToFormula(ref1);
//    ref2 = "SheetType!$A$1:$" + col2 + "$1";
//    groupName.setRefersToFormula(ref2);
//    sheetKind.setSelected(false);
//    sheetType.setSelected(false);
////    workbook.setSheetHidden(2, true);
//    workbook.setActiveSheet(0);
//
//    //Tạo 1 mảng lưu header từng cột
//    String[] header = new String[]{
//        I18n.getLanguage("common.STT"),
//        I18n.getLanguage("mapProbToKedb.probTypeNameLv1"),
//        I18n.getLanguage("mapProbToKedb.probTypeNameLv2"),
//        I18n.getLanguage("mapProbToKedb.probTypeNameLv2"),
//        I18n.getLanguage("mapProbToKedb.kedbCode"),
//
//
//    };
//    //Tiêu đề đánh dấu *
//    String[] headerStar = new String[]{
//        I18n.getLanguage("mapProbToKedb.probTypeNameLv1"),
//        I18n.getLanguage("mapProbToKedb.probTypeNameLv2"),
//        I18n.getLanguage("mapProbToKedb.probTypeNameLv2"),
//        I18n.getLanguage("mapProbToKedb.kedbCode"),
//    };
//
//    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
//    List<String> listHeader = Arrays.asList(header);
//    List<String> listHeaderStar = Arrays.asList(headerStar);
//
//
//    int groupNameColumn = listHeader.indexOf(I18n.getLanguage("mapProbToKedb.probTypeNameLv1"));
//    int kindNameColumn = listHeader.indexOf(I18n.getLanguage("mapProbToKedb.probTypeNameLv2"));
//    Map<String, CellStyle> style = CommonExport.createStyles(workbook);
//
//
//
//    //Tạo tiêu đề
//    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
//    Row titleRow = sheetOne.createRow(2);
//    titleRow.setHeightInPoints(25);
//    Cell titleCell = titleRow.createCell(0);
//    titleCell.setCellValue(I18n.getLanguage("mapProbToKedb.title"));
//    titleCell.setCellStyle(style.get("title"));
//
//    Row firstLeftHeaderRow = sheetOne.createRow(0);
//    Cell firstLeftHeaderCell = firstLeftHeaderRow.createCell(1);
//    firstLeftHeaderRow.setHeightInPoints(16);
//    firstLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.firstLeftHeader"));
//    firstLeftHeaderCell.setCellStyle(style.get("indexTitle"));
//    Row secondLeftHeaderRow = sheetOne.createRow(1);
//    Cell secondLeftHeaderCell = secondLeftHeaderRow.createCell(1);
//    secondLeftHeaderRow.setHeightInPoints(16);
//    secondLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.secondLeftHeader"));
//    secondLeftHeaderCell.setCellStyle(style.get("indexTitle"));
//    Cell firstRightHeaderCell = firstLeftHeaderRow.createCell(4);
//    firstLeftHeaderRow.setHeightInPoints(16);
//    firstRightHeaderCell.setCellValue(I18n.getLanguage("common.export.firstRightHeader"));
//    firstRightHeaderCell.setCellStyle(style.get("indexTitle"));
//    Cell secondRightHeaderCell = secondLeftHeaderRow.createCell(4);
//    secondLeftHeaderRow.setHeightInPoints(16);
//    secondRightHeaderCell.setCellValue(I18n.getLanguage("common.export.secondRightHeader"));
//    secondRightHeaderCell.setCellStyle(style.get("indexTitle"));
//
//    XSSFFont starFont = workbook.createFont();
//    starFont.setColor(IndexedColors.RED.getIndex());
//
//    //Tạo Header
//
//    Row headerRow = sheetOne.createRow(4);
//    headerRow.setHeightInPoints(30);
//    for (int i = 0; i < listHeader.size(); i++) {
//      Cell headerCell = headerRow.createCell(i);
//      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
//      for (String headerCheck : listHeaderStar) {
//        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
//          richTextString.append("*", starFont);
//        }
//      }
//      headerCell.setCellValue(richTextString);
//      headerCell.setCellStyle(style.get("header"));
//      sheetOne.setColumnWidth(i, 7000);
//    }
//
//
//    excelWriterUtils.createCell(sheetOther, 0, 0,
//        I18n.getLanguage("mapProbToKedb.probTypeNameLv1").toUpperCase(), style.get("header"));
//
//    // Set dữ liệu vào column dropdown
//    int row = 1;
//    for (ProblemGroupDTO dto : groupList) {
//      excelWriterUtils.createCell(sheetOther, 0, row++, dto.getName(), style.get("cell"));
//    }
//
//    XSSFDataValidationConstraint systemNameConstraint = new XSSFDataValidationConstraint(
//        ValidationType.LIST, "GroupName");
//    CellRangeAddressList groupNameCreate = new CellRangeAddressList(5, 66, groupNameColumn,
//        groupNameColumn);
//    XSSFDataValidation dataValidationGroupName = (XSSFDataValidation) dataValidationHelper
//        .createValidation(
//            systemNameConstraint, groupNameCreate);
//    dataValidationGroupName.setShowErrorBox(true);
//    sheetOne.addValidationData(dataValidationGroupName);
//
//    XSSFDataValidationConstraint kindNameConstraint;
//    CellRangeAddressList cellRangeKind;
//    XSSFDataValidation dataValidationKind;
//    for (int i = 5; i <= 67; i++) {
//      kindNameConstraint = new XSSFDataValidationConstraint(
//          ValidationType.LIST, "INDIRECT($B$" + i + ")");
//      cellRangeKind = new CellRangeAddressList(
//          i - 1, i - 1, kindNameColumn, kindNameColumn);
//      dataValidationKind = (XSSFDataValidation) dataValidationHelper.createValidation(
//          kindNameConstraint, cellRangeKind);
//      dataValidationKind.setShowErrorBox(true);
//      sheetOne.addValidationData(dataValidationKind);
//    }
//
//
//    //set tên trang excel
//    workbook.setSheetName(0, I18n.getLanguage("mapProbToKedb.title"));
////    workbook.setSheetHidden(1, true);
//    sheetOne.setSelected(false);
//
//    //set tên file excel
//    String fileResult = tempFolder + File.separator;
//    String fileName = "IMPORT_MAP_PROB_TO_KEDB" + "_" + System.currentTimeMillis() + ".xlsx";
//    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
//    String resultPath = fileResult + fileName;
//    File fileExport = new File(resultPath);
//    return fileExport;
//  }
//
//
//  @Override
//  public ResultInSideDto importData(MultipartFile multipartFile) {
//    List<MapProbToKedbDTO> mapProbToKedbDTOList = new ArrayList<>();
//    ResultInSideDto resultInSideDto = new ResultInSideDto();
//    boolean checkDouble = true;
//    resultInSideDto.setKey(RESULT.SUCCESS);
//    try {
//      if (multipartFile == null || multipartFile.isEmpty()) {
//        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
//        return resultInSideDto;
//      } else {
//        String filePath = FileUtils
//            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), tempFolder);
//        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
//          return resultInSideDto;
//        }
//        File fileImport = new File(filePath);
//
//        List<Object[]> headerList;
//        headerList = CommonImport.getDataFromExcelFile(
//            fileImport,
//            0,
//            4,
//            0,
//            5,
//            1000
//        );
//        //Kiểm tra form header có đúng chuẩn
//        if (headerList.size() == 0 || !validFileFormat(headerList)) {
//          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
//          return resultInSideDto;
//        }
//        //Lấy dữ liệu import
//        List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
//            fileImport,
//            0,
//            5,
//            0,
//            6,
//            1000
//        );
//        if (dataImportList.size() > 1500) {
//          resultInSideDto.setKey(RESULT.DATA_OVER);
//          return resultInSideDto;
//        }
//
//        mapProbToKedbDTOS = new ArrayList<>();
//        if (!dataImportList.isEmpty()) {
//          int row = 4;
//          int index = 0;
//          setMapGroup();
//          for (Object[] obj : dataImportList) {
//            MapProbToKedbDTO mapProbToKedbDTO = new MapProbToKedbDTO();
//            if (obj[1] != null) {
//              mapProbToKedbDTO.setProbTypeNameLv1(obj[1].toString().trim());
//              for (Map.Entry<Long, String> item : mapGroup.entrySet()) {
//                if (mapProbToKedbDTO.getProbTypeNameLv1().equals(item.getValue())) {
//                  mapProbToKedbDTO.setProbTypeIdLv1(Long.valueOf(item.getKey()));
//                  setMapKind(item.getKey());
//                  break;
//                } else {
//                  mapProbToKedbDTO.setProbTypeIdLv1(null);
//                }
//              }
//            } else {
//              mapProbToKedbDTO.setProbTypeNameLv1(null);
//            }
//            if (obj[2] != null) {
//              mapProbToKedbDTO.setProbTypeNameLv2(obj[2].toString().trim());
//
//              for (Map.Entry<Long, String> item : mapKind.entrySet()) {
//                if (mapProbToKedbDTO.getProbTypeNameLv2().equals(item.getValue())) {
//                  mapProbToKedbDTO.setProbTypeIdLv2(Long.valueOf(item.getKey()));
//                  setMapType(item.getKey());
//                  break;
//                } else {
//                  mapProbToKedbDTO.setProbTypeIdLv2(null);
//                }
//              }
//            } else {
//              mapProbToKedbDTO.setProbTypeNameLv2(null);
//            }
//
//            if (obj[3] != null) {
//              mapProbToKedbDTO.setProbTypeNameLv3(obj[3].toString().trim());
//
//              for (Map.Entry<Long, String> item : mapType.entrySet()) {
//                if (mapProbToKedbDTO.getProbTypeNameLv3().equals(item.getValue())) {
//                  mapProbToKedbDTO.setProbTypeIdLv3(Long.valueOf(item.getKey()));
//                  break;
//                } else {
//                  mapProbToKedbDTO.setProbTypeIdLv3(null);
//                }
//              }
//            } else {
//              mapProbToKedbDTO.setProbTypeNameLv3(null);
//            }
//
//            if (obj[4] != null) {
//              mapProbToKedbDTO.setKedbCode(obj[4].toString().trim());
//            } else {
//              mapProbToKedbDTO.setKedbCode(null);
//            }
//
//            MapProbToKedbDTO mapProbToKedbDTOTmp = validateImportInfo(
//                mapProbToKedbDTO, mapProbToKedbDTOList);
//
//            if (mapProbToKedbDTOTmp.getResultImport() == null) {
//              mapProbToKedbDTOTmp
//                  .setResultImport(I18n.getLanguage("mapProbToKedb.result.import"));
//              mapProbToKedbDTOList.add(mapProbToKedbDTOTmp);
//              mapProbToKedbDTOS.add(mapProbToKedbDTO);
//            } else {
//              mapProbToKedbDTOList.add(mapProbToKedbDTOTmp);
//              index++;
//            }
//            row++;
//          }
//          if (index == 0) {
//            if (!mapProbToKedbDTOList.isEmpty()) {
//
////              resultInSideDto = languageExchangeRepository
////                  .saveListLanguageExchangeImport(languageExchangeDTOList);
//            }
//          } else {
//            File fileExport = exportFileTemplate(mapProbToKedbDTOList, Constants.RESULT_IMPORT,"1");
//            resultInSideDto.setKey(RESULT.ERROR);
//            resultInSideDto.setFile(fileExport);
//          }
//        } else {
//          resultInSideDto.setKey(RESULT.NODATA);
//          resultInSideDto.setMessage(Constants.FILE_NULL);
//          File fileExport = exportFileTemplate(mapProbToKedbDTOList, Constants.RESULT_IMPORT,"1");
//          resultInSideDto.setFile(fileExport);
//          return resultInSideDto;
//        }
//      }
//    } catch (Exception e) {
//      log.error(e.getMessage(), e);
//      resultInSideDto.setKey(RESULT.ERROR);
//      resultInSideDto.setMessage(e.getMessage());
//    }
//    return resultInSideDto;
//  }
//
//
//
//  private MapProbToKedbDTO validateImportInfo(MapProbToKedbDTO mapProbToKedbDTO,
//      List<MapProbToKedbDTO> list) {
//    if (StringUtils.isStringNullOrEmpty(mapProbToKedbDTO.getProbTypeNameLv1())) {
//      mapProbToKedbDTO.setResultImport(I18n.getLanguage("mapProbToKedb.err.groupName"));
//      return mapProbToKedbDTO;
//    }
//
//    if (StringUtils.isStringNullOrEmpty(mapProbToKedbDTO.getProbTypeNameLv2())) {
//      mapProbToKedbDTO.setResultImport(I18n.getLanguage("mapProbToKedb.err.kindName"));
//      return mapProbToKedbDTO;
//    }
//
//    if (StringUtils.isStringNullOrEmpty(mapProbToKedbDTO.getProbTypeNameLv3())) {
//      mapProbToKedbDTO.setResultImport(I18n.getLanguage("mapProbToKedb.err.typeName"));
//      return mapProbToKedbDTO;
//    }
//
//    if (StringUtils.isStringNullOrEmpty(mapProbToKedbDTO.getKedbCode())) {
//      mapProbToKedbDTO.setResultImport(I18n.getLanguage("mapProbToKedb.err.kedbCode"));
//      return mapProbToKedbDTO;
//    }
//
//
//
//    if (StringUtils.isStringNullOrEmpty(mapProbToKedbDTO.getProbTypeIdLv1())) {
//      mapProbToKedbDTO.setResultImport(I18n.getLanguage("mapProbToKedb.err.group.exist"));
//      return mapProbToKedbDTO;
//    }
//    if (StringUtils.isStringNullOrEmpty(mapProbToKedbDTO.getProbTypeIdLv2())) {
//      mapProbToKedbDTO.setResultImport(I18n.getLanguage("mapProbToKedb.err.kind.exist"));
//      return mapProbToKedbDTO;
//    }
//    if (StringUtils.isStringNullOrEmpty(mapProbToKedbDTO.getProbTypeIdLv3())) {
//      mapProbToKedbDTO.setResultImport(I18n.getLanguage("mapProbToKedb.err.type.exist"));
//      return mapProbToKedbDTO;
//    }
//    if (StringUtils.isStringNullOrEmpty(mapProbToKedbDTO.getKedbCode())) {
//      mapProbToKedbDTO.setResultImport(I18n.getLanguage("mapProbToKedb.err.kedbCode.exist"));
//      return mapProbToKedbDTO;
//    }
//    MapProbToKedbDTO mapProbToKedbTmp = mapProbToKedbRepository
//        .checkExist(mapProbToKedbDTO.getProbTypeIdLv1()
//            , mapProbToKedbDTO.getProbTypeIdLv2()
//            , mapProbToKedbDTO.getProbTypeIdLv3());
//    if (mapProbToKedbTmp != null) {
//        mapProbToKedbDTO.setResultImport(I18n.getLanguage("mapProbToKedb.err.duplicate"));
//        return mapProbToKedbDTO;
//
//    }
//
//    if (list != null && list.size() > 0 && mapProbToKedbDTO.getResultImport() == null) {
//      mapProbToKedbDTO = validateDuplicate(list, mapProbToKedbDTO);
//    }
//
//    return mapProbToKedbDTO;
//  }
//
//  private MapProbToKedbDTO validateDuplicate(List<MapProbToKedbDTO> list,
//      MapProbToKedbDTO mapProbToKedbDTO) {
//    for (int i = 0; i < list.size(); i++) {
//      MapProbToKedbDTO mapProbToKedbTmp = list.get(i);
//      if (I18n.getLanguage("mapProbToKedb.result.import")
//          .equals(mapProbToKedbTmp.getResultImport()) && mapProbToKedbTmp.getProbTypeIdLv1()
//          .equals(mapProbToKedbDTO.getProbTypeIdLv1())
//          && mapProbToKedbTmp.getProbTypeIdLv2().equals(
//          mapProbToKedbDTO.getProbTypeIdLv2()) && mapProbToKedbTmp.getProbTypeIdLv3()
//          .equals(mapProbToKedbDTO.getProbTypeIdLv3())) {
//        mapProbToKedbDTO
//            .setResultImport(I18n.getLanguage("mapProbToKedb.err.dup-code-in-file")
//                .replaceAll("0", String.valueOf((i) + 1)));
//        break;
//      }
//    }
//    return mapProbToKedbDTO;
//  }


}
