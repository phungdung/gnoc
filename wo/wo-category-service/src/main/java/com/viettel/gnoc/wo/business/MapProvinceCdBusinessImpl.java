package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.MapProvinceCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.repository.MapProvinceCdRepository;
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
public class MapProvinceCdBusinessImpl implements MapProvinceCdBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;
  private final static String MAP_PROVINCE_CD_EXPORT = "MAP_PROVINCE_CD_EXPORT";
  private final static String MAP_PROVINCE_CD_RESUILT_IMPORT = "MAP_PROVINCE_CD_RESUILT_IMPORT";
  Map<String, String> mapCdGroup = new HashMap<String, String>();
  Map<String, String> mapProvince = new HashMap<String, String>();

  @Autowired
  protected MapProvinceCdRepository mapProvinceCdRepository;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Autowired
  protected WoCdGroupBusiness woCdGroupBusiness;

  @Override
  public ResultInSideDto deleteMapProvinceCd(Long id) {
    log.debug("Request to deleteMapProvinceCd : {}", id);
    return mapProvinceCdRepository.deleteMapProvinceCd(id);
  }

  @Override
  public ResultInSideDto add(MapProvinceCdDTO mapProvinceCdDTO) {
    log.debug("Request to add : {}", mapProvinceCdDTO);
    return mapProvinceCdRepository.add(mapProvinceCdDTO);
  }

  @Override
  public ResultInSideDto edit(MapProvinceCdDTO mapProvinceCdDTO) {
    log.debug("Request to edit : {}", mapProvinceCdDTO);
    return mapProvinceCdRepository.edit(mapProvinceCdDTO);
  }

  @Override
  public MapProvinceCdDTO getDetail(Long id) {
    log.debug("Request to getDetail : {}", id);
    return mapProvinceCdRepository.getDetail(id);
  }

  @Override
  public Datatable getListDTOSearchWeb(MapProvinceCdDTO mapProvinceCdDTO) {
    log.debug("Request to getListDTOSearchWeb : {}", mapProvinceCdDTO);
    return mapProvinceCdRepository.getListDTOSearchWeb(mapProvinceCdDTO);
  }

  @Override
  public File exportData(MapProvinceCdDTO mapProvinceCdDTO) throws Exception {
    log.debug("Request to exportData : {}", mapProvinceCdDTO);
    List<MapProvinceCdDTO> list = mapProvinceCdRepository
        .getListDTOSearchWebExport(mapProvinceCdDTO);
    return exportFileEx(list, null);
  }

  @Override
  public File getTemplate() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workBook.getSheetAt(0);
    XSSFSheet sheetParam = workBook.createSheet("param");
    String[] header = new String[]{
        I18n.getLanguage("mapProvinceCd.stt"),
        I18n.getLanguage("mapProvinceCd.locationName"),
        I18n.getLanguage("mapProvinceCd.cdName"),
        I18n.getLanguage("mapProvinceCd.numberDistrictTkExp"),
        I18n.getLanguage("mapProvinceCd.numberAccountTkExp"),
        I18n.getLanguage("mapProvinceCd.numberDistrictScExp"),
        I18n.getLanguage("mapProvinceCd.numberAccountScExp")

    };
    String[] headerStar = new String[]{
        I18n.getLanguage("mapProvinceCd.locationName"),
        I18n.getLanguage("mapProvinceCd.cdName"),
        I18n.getLanguage("mapProvinceCd.numberDistrictTkExp"),
        I18n.getLanguage("mapProvinceCd.numberAccountTkExp"),
        I18n.getLanguage("mapProvinceCd.numberDistrictScExp"),
        I18n.getLanguage("mapProvinceCd.numberAccountScExp")

    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int locationNameColumn = listHeader.indexOf(I18n.getLanguage("mapProvinceCd.locationName"));
    int cdNameColumn = listHeader.indexOf(I18n.getLanguage("mapProvinceCd.cdName"));
    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    //Tao tieu de
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(0);
    titleRow.setHeightInPoints(45);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("mapProvinceCd.report.title"));
    titleCell.setCellStyle(styles.get("title"));

    //Tao note
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row noteRow = sheetOne.createRow(2);
    noteRow.setHeightInPoints(18);
    Cell noteCell = noteRow.createCell(0);
    noteCell.setCellValue(I18n.getLanguage("wocdgroup.importfile.template.excel.note"));
    noteCell.setCellStyle(styles.get("note"));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());
    //Tao header
    Row headerRow = sheetOne.createRow(4);
    headerRow.setHeightInPoints(18);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeader : listHeaderStar) {
        if (checkHeader.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("(*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(styles.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }

    sheetOne.setColumnWidth(0, 3000);

    int row = 1;
    List<CatLocationDTO> listProvince = catLocationBusiness.getListLocationProvince();

    for (CatLocationDTO dto : listProvince) {
      ewu.createCell(sheetParam, 0, row++, dto.getLocationName(), styles.get("cell"));
    }

    sheetParam.autoSizeColumn(0);

    Name locationName = workBook.createName();
    locationName.setNameName("locationName");
    locationName.setRefersToFormula("param!$A$2:$A$" + row);

    XSSFDataValidationConstraint odGroupTypeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "locationName");
    CellRangeAddressList cellRangeOdGroupType = new CellRangeAddressList(5, 65000,
        locationNameColumn, locationNameColumn);
    XSSFDataValidation dataValidationOdGroupType = (XSSFDataValidation) dvHelper.createValidation(
        odGroupTypeConstraint, cellRangeOdGroupType);
    dataValidationOdGroupType.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationOdGroupType);

    row = 1;
    List<WoCdGroupInsideDTO> listCdGroup = getListWoCdGroup();

    for (WoCdGroupInsideDTO dto : listCdGroup) {
      String woGroupName = dto.getWoGroupName() + " (" + dto.getWoGroupId() + ")";
      ewu.createCell(sheetParam, 1, row++, woGroupName, styles.get("cell"));
    }
    sheetParam.autoSizeColumn(0);

    Name cdName = workBook.createName();
    cdName.setNameName("cdName");
    cdName.setRefersToFormula("param!$B$2:$B$" + row);

    XSSFDataValidationConstraint cdNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "cdName");
    CellRangeAddressList cellRangeCdName = new CellRangeAddressList(5, 65000, cdNameColumn,
        cdNameColumn);
    XSSFDataValidation dataValidationCdName = (XSSFDataValidation) dvHelper.createValidation(
        cdNameConstraint, cellRangeCdName);
    dataValidationCdName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationCdName);

    workBook.setSheetName(0, "CfgTime");
    workBook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MAP_PROVINCE_CD" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  // API import Excel
  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<MapProvinceCdDTO> listMapProviceCd = new ArrayList<>();
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDTO.setKey(Constants.RESULT.FILE_IS_NULL);
        return resultInSideDTO;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!Constants.RESULT.SUCCESS.equals(resultInSideDTO.getKey())) {
          return resultInSideDTO;
        }

        File fileImport = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImport,
            0,//sheet
            3,//begin row
            0,//from column
            7,//to column
            1000);

        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDTO.setKey(Constants.RESULT.FILE_INVALID_FORMAT);
          return resultInSideDTO;
        }

        // Lay du lieu import
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(
            fileImport,
            0,//sheet
            5,//begin row
            0,//from column
            7,//to column
            1000
        );

        if (lstData.size() > 2500) {
          resultInSideDTO.setKey(Constants.RESULT.DATA_OVER);
          return resultInSideDTO;
        }

        if (!lstData.isEmpty()) {
          int index = 0;
          setMapCdGroup();
          setMapProvince();
          for (Object[] obj : lstData) {
            MapProvinceCdDTO mapProvinceCdDTOExport = new MapProvinceCdDTO();
            mapProvinceCdDTOExport.setResuilt("");
            if (obj[1] != null && !"".equals(obj[1])) {
              mapProvinceCdDTOExport.setLocationName(obj[1].toString().trim());
              mapProvinceCdDTOExport.setLocationCode(mapProvince.get(obj[1].toString().trim()));
              if (!StringUtils.validString(mapProvinceCdDTOExport.getLocationCode())) {
                mapProvinceCdDTOExport
                    .setResuilt(I18n.getLanguage("mapProvinceCd.combobox.locationName"));
              }
            } else {
              mapProvinceCdDTOExport
                  .setResuilt(I18n.getLanguage("mapProvinceCd.null.locationName"));
            }

            if (obj[2] != null && !"".equals(obj[2])) {
              mapProvinceCdDTOExport.setCdId(
                  StringUtils.validString(mapCdGroup.get(obj[2].toString().trim())) ? Long
                      .valueOf(mapCdGroup.get(obj[2].toString().trim())) : null);
              mapProvinceCdDTOExport.setCdName(obj[2].toString().trim());
              if (mapProvinceCdDTOExport.getCdId() == null) {
                mapProvinceCdDTOExport.setResuilt(
                    addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                        I18n.getLanguage("mapProvinceCd.combobox.cdName")));
              }
            } else {
              mapProvinceCdDTOExport.setResuilt(
                  addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                      I18n.getLanguage("mapProvinceCd.null.cdName")));
            }

            if (obj[3] != null && !"".equals(obj[3])) {
              mapProvinceCdDTOExport.setNumberDistrictTkExp(obj[3].toString().trim());
              if (DataUtil.isNumber(obj[3].toString().trim())) {
                Double numberDistrictTkTmp = Double.parseDouble(obj[3].toString().trim());
                mapProvinceCdDTOExport.setNumberDistrictTk(numberDistrictTkTmp.longValue());
                mapProvinceCdDTOExport
                    .setNumberDistrictTkExp(String.valueOf(numberDistrictTkTmp.longValue()));
              } else {
                mapProvinceCdDTOExport.setResuilt(
                    addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                        I18n.getLanguage("mapProvinceCd.checkNumber.numberDistrictTk")));
              }
              if (mapProvinceCdDTOExport.getNumberDistrictTkExp().length() > 10) {
                mapProvinceCdDTOExport.setResuilt(
                    addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                        I18n.getLanguage("mapProvinceCd.checkLength.numberDistrictTk")));
              }
            } else {
              mapProvinceCdDTOExport.setResuilt(
                  addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                      I18n.getLanguage("mapProvinceCd.null.numberDistrictTk")));
            }

            if (obj[4] != null && !"".equals(obj[4])) {
              mapProvinceCdDTOExport.setNumberAccountTkExp(obj[4].toString().trim());
              if (DataUtil.isNumber(obj[4].toString().trim())) {
                Double numberAccountTkTmp = Double.parseDouble(obj[4].toString().trim());
                mapProvinceCdDTOExport.setNumberAccountTk(numberAccountTkTmp.longValue());
                mapProvinceCdDTOExport.setNumberAccountTkExp(
                    String.valueOf(numberAccountTkTmp.longValue()));
              } else {
                mapProvinceCdDTOExport.setResuilt(
                    addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                        I18n.getLanguage("mapProvinceCd.checkNumber.numberAccountTk")));
              }
              if (mapProvinceCdDTOExport.getNumberAccountTkExp().length() > 10) {
                mapProvinceCdDTOExport.setResuilt(
                    addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                        I18n.getLanguage("mapProvinceCd.checkLength.numberAccountTk")));
              }
            } else {
              mapProvinceCdDTOExport.setResuilt(
                  addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                      I18n.getLanguage("mapProvinceCd.null.numberAccountTk")));
            }

            if (obj[5] != null && !"".equals(obj[5])) {
              mapProvinceCdDTOExport.setNumberDistrictScExp(obj[5].toString().trim());
              if (DataUtil.isNumber(obj[5].toString().trim())) {
                Double numberDistrictScTmp = Double.parseDouble(obj[5].toString().trim());
                mapProvinceCdDTOExport.setNumberDistrictSc(numberDistrictScTmp.longValue());
                mapProvinceCdDTOExport.setNumberDistrictScExp(
                    String.valueOf(numberDistrictScTmp.longValue()));
              } else {
                mapProvinceCdDTOExport.setResuilt(
                    addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                        I18n.getLanguage("mapProvinceCd.checkNumber.numberDistrictSc")));
              }
              if (mapProvinceCdDTOExport.getNumberDistrictScExp().length() > 10) {
                mapProvinceCdDTOExport.setResuilt(
                    addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                        I18n.getLanguage("mapProvinceCd.checkLength.numberDistrictSc")));
              }
            } else {
              mapProvinceCdDTOExport.setResuilt(
                  addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                      I18n.getLanguage("mapProvinceCd.null.numberDistrictSc")));
            }

            if (obj[6] != null && !"".equals(obj[6])) {
              mapProvinceCdDTOExport.setNumberAccountScExp(obj[6].toString().trim());
              if (DataUtil.isNumber(obj[6].toString().trim())) {
                Double numberAccountScTmp = Double.parseDouble(obj[6].toString().trim());
                mapProvinceCdDTOExport.setNumberAccountSc(numberAccountScTmp.longValue());
                mapProvinceCdDTOExport.setNumberAccountScExp(
                    String.valueOf(numberAccountScTmp.longValue()));
              } else {
                mapProvinceCdDTOExport.setResuilt(
                    addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                        I18n.getLanguage("mapProvinceCd.checkNumber.numberAccountSc")));
              }
              if (mapProvinceCdDTOExport.getNumberAccountScExp().length() > 10) {
                mapProvinceCdDTOExport.setResuilt(
                    addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                        I18n.getLanguage("mapProvinceCd.checkLength.numberAccountSc")));
              }
            } else {
              mapProvinceCdDTOExport.setResuilt(
                  addResuiltImport(mapProvinceCdDTOExport.getResuilt(),
                      I18n.getLanguage("mapProvinceCd.null.numberAccountSc")));
            }

            if (!StringUtils.validString(mapProvinceCdDTOExport.getResuilt())) {
              mapProvinceCdDTOExport = validateImportInfo(mapProvinceCdDTOExport, listMapProviceCd);
            }

            if (!StringUtils.validString(mapProvinceCdDTOExport.getResuilt())) {
              mapProvinceCdDTOExport
                  .setResuilt(I18n.getLanguage("mapProvinceCd.result.import.mapProvinceCdCode"));
            } else {
              index++;
            }
            listMapProviceCd.add(mapProvinceCdDTOExport);
          }
          if (index == 0) {
            if (!listMapProviceCd.isEmpty()) {
              resultInSideDTO = mapProvinceCdRepository.insertOrUpdateListImport(listMapProviceCd);
            }
          } else {
            File fileExport = exportFileEx(listMapProviceCd, Constants.RESULT_IMPORT);
            resultInSideDTO.setKey(Constants.RESULT.ERROR);
            resultInSideDTO.setFile(fileExport);
          }
        } else {
          resultInSideDTO.setKey(Constants.RESULT.NODATA);
          resultInSideDTO.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileEx(listMapProviceCd, Constants.RESULT_IMPORT);
          resultInSideDTO.setFile(fileExport);
          return resultInSideDTO;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDTO.setKey(Constants.RESULT.ERROR);
      resultInSideDTO.setMessage(ex.getMessage());
    }
    return resultInSideDTO;
  }

  private String addResuiltImport(String resuiltOld, String resuiltNew) {
    if (StringUtils.validString(resuiltOld)) {
      return resuiltOld + ",\n" + resuiltNew;
    }
    return resuiltNew;
  }

  private MapProvinceCdDTO validateImportInfo(MapProvinceCdDTO dto, List<MapProvinceCdDTO> list) {
    if (StringUtils.isNotNullOrEmpty(dto.getLocationCode()) && dto.getCdId() != null) {
      MapProvinceCdDTO mapProvinceCdDTO = mapProvinceCdRepository
          .checkMapProvinceCdExist(dto.getLocationCode());
      if (mapProvinceCdDTO != null) {
        dto.setResuilt(I18n.getLanguage("mapProvinceCd.null.unique"));
        return dto;
      }
    }
    if (list != null && list.size() > 0) {
      dto = validateDuplicate(list, dto);
    }
    return dto;
  }

  private MapProvinceCdDTO validateDuplicate(List<MapProvinceCdDTO> list,
      MapProvinceCdDTO mapProvinceCdDTO) {
    for (int i = 0; i < list.size(); i++) {
      MapProvinceCdDTO mapProvinceTmp = list.get(i);
      if (mapProvinceTmp.getCdId() == mapProvinceCdDTO.getCdId() && mapProvinceTmp.getLocationCode()
          .equals(mapProvinceCdDTO.getLocationCode())) {
        mapProvinceCdDTO.setResuilt(I18n.getLanguage("mapProvinceCd.err.dup-code-in-file")
            .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return mapProvinceCdDTO;
  }


  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;
    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 7) {
      return false;
    }

    if (obj0 == null) {
      return false;
    }
    if (obj0[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("common.STT")).equalsIgnoreCase(obj0[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mapProvinceCd.locationName") + "(*)")
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mapProvinceCd.cdName") + "(*)")
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mapProvinceCd.numberDistrictTkExp") + "(*)")
        .equalsIgnoreCase(obj0[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mapProvinceCd.numberAccountTkExp") + "(*)")
        .equalsIgnoreCase(obj0[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mapProvinceCd.numberDistrictScExp") + "(*)")
        .equalsIgnoreCase(obj0[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mapProvinceCd.numberAccountScExp") + "(*)")
        .equalsIgnoreCase(obj0[6].toString().trim())) {
      return false;
    }
    return true;
  }


  private File exportFileEx(List<MapProvinceCdDTO> lstImport, String action) throws Exception {
    String sheetName = "CfgTime";
    String title = I18n.getLanguage("mapProvinceCd.report.title");
    String fileNameOut = MAP_PROVINCE_CD_EXPORT;
    String subTitle = I18n
        .getLanguage("mapProvinceCd.export.exportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet1;
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    columnSheet1 = new ConfigHeaderExport("locationName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("cdName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("numberDistrictScExp", "LEFT", false, 0, 0,
        new String[]{},
        null, "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("numberAccountScExp", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("numberDistrictTkExp", "LEFT", false, 0, 0,
        new String[]{},
        null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("numberAccountTkExp", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    if (Constants.RESULT_IMPORT.equals(action)) {
      lstHeaderSheet1.add(columnSheet1);
      columnSheet1 = new ConfigHeaderExport("resuilt", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      fileNameOut = MAP_PROVINCE_CD_RESUILT_IMPORT;
      subTitle = I18n
          .getLanguage("mapProvinceCd.export.importDate", DateTimeUtils.convertDateOffset());
    }
    lstHeaderSheet1.add(columnSheet1);
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configfileExport = new ConfigFileExport(
        lstImport
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.mapProvinceCd"
        , lstHeaderSheet1
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
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

  public void setMapProvince() {
    List<CatLocationDTO> list = catLocationBusiness.getListLocationProvince();
    for (CatLocationDTO dto : list) {
      mapProvince.put(dto.getLocationName(), dto.getLocationCode());
    }
  }

  public void setMapCdGroup() {
    List<WoCdGroupInsideDTO> lst = getListWoCdGroup();
    if (lst != null && !lst.isEmpty()) {
      for (WoCdGroupInsideDTO dto : lst) {
        String woGroupName = dto.getWoGroupName() + " (" + dto.getWoGroupId() + ")";
        mapCdGroup.put(woGroupName, String.valueOf(dto.getWoGroupId()));
      }
    }
  }

  public List<WoCdGroupInsideDTO> getListWoCdGroup() {
    WoCdGroupInsideDTO woCdGroupInsideDTO = new WoCdGroupInsideDTO();
    woCdGroupInsideDTO.setPageSize(Integer.MAX_VALUE);
    woCdGroupInsideDTO.setPage(1);
    woCdGroupInsideDTO.setSortName("woGroupCode");
    woCdGroupInsideDTO.setSortType("");
    Datatable datatable = woCdGroupBusiness.getListWoCdGroupDTO(woCdGroupInsideDTO);
    return (List<WoCdGroupInsideDTO>) datatable.getData();
  }

}
