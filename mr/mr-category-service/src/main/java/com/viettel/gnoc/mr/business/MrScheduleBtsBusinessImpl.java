package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsDTO;
import com.viettel.gnoc.mr.repository.MrDeviceBtsRepository;
import com.viettel.gnoc.mr.repository.MrScheduleBtsRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Slf4j
public class MrScheduleBtsBusinessImpl implements MrScheduleBtsBusiness {

  @Autowired
  MrScheduleBtsRepository mrScheduleBtsRepository;
  @Autowired
  MrDeviceBtsRepository mrDeviceBtsRepository;

  @Autowired
  UserRepository userRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  private Map<String, String> mapErrorResult = new HashMap<>();
  private List<ErrorInfo> cellErrorList = new ArrayList<>();
  private String pathTemplate = "templates" + File.separator + "maintenance" + File.separator;

  @Override
  public Datatable onSearch(MrScheduleBtsDTO dto) {
    if ("woStatusName".equalsIgnoreCase(dto.getSortName())) {
      dto.setSortName("woStatus");
    }
    if ("ProvinceCode".equalsIgnoreCase(dto.getSortName())) {
      dto.setSortName("ProvinceName");
    }
    if ("DeviceTypeName".equalsIgnoreCase(dto.getSortName())) {
      dto.setSortName("DeviceType");
    }

    Datatable datatable = mrScheduleBtsRepository.onSearch(dto);
    if (datatable != null) {
      List<MrScheduleBtsDTO> data = (List<MrScheduleBtsDTO>) datatable.getData();
      List<MrDeviceBtsDTO> lstProvince = mrDeviceBtsRepository.getListProvince(null);
      Map<String, String> catLocationProvince = new HashMap<>();
      if (lstProvince != null) {
        for (MrDeviceBtsDTO provinceDTO : lstProvince) {
          catLocationProvince.put(provinceDTO.getProvinceCode(), provinceDTO.getProvinceName());
        }
      }

      if (data != null) {
        processDataSearch(data, catLocationProvince);
      }
      datatable.setData(data);
    }
    return datatable;
  }

  @Override
  public File exportSearchData(MrScheduleBtsDTO mrScheduleBtsDTO) throws Exception {

    String[] header = new String[]{"marketName", "provinceName", "stationCode", "deviceTypeName",
        "serial", "cycle", "mrCode", "woCode", "isWoOriginal", "woStatusName", "modifyDate",
        "nextDateModify", "userManager"};
    String[] align = new String[]{"LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "RIGHT", "LEFT", "LEFT",
        "CENTER", "CENTER", "CENTER", "CENTER", "LEFT"};
    List<MrScheduleBtsDTO> lstData = getDataExport(mrScheduleBtsDTO);
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header, align);
    return exportFileEx(lstData, lstHeaderSheet);
  }

  @Override
  public File getFileTemplate(MrScheduleBtsDTO mrScheduleBtsDTO) {

    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Workbook workBook = null;
    String pathFolder =
        tempFolder + File.separator + FileUtils.createPathByDate(new Date()) + File.separator;
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_SCHEDULE_BTS_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_SCHEDULE_BTS_TEMPLATE_VI.xlsx";
    }
    try {
      String pathFileImport = pathTemplate + fileTemplateName;
      workBook = ewu.readFileExcelFromTemplate(pathFileImport);
      Sheet sheetSchedule = workBook.getSheetAt(0);
      int k = 8;
      List<MrScheduleBtsDTO> lstData = getDataExport(mrScheduleBtsDTO);
//      CellStyle cellSt1 = null;
      for (MrScheduleBtsDTO dto : lstData) {
        if (dto == null) {
          break;
        }
        ewu.createCell(sheetSchedule, 0, k, dto.getScheduleId(), null);
        ewu.createCell(sheetSchedule, 1, k, dto.getMarketName(), null);
        ewu.createCell(sheetSchedule, 2, k, dto.getProvinceName(), null);
        ewu.createCell(sheetSchedule, 3, k, dto.getStationCode(), null);
        ewu.createCell(sheetSchedule, 4, k, dto.getDeviceTypeName(), null);
        ewu.createCell(sheetSchedule, 5, k, dto.getSerial(), null);
        ewu.createCell(sheetSchedule, 6, k, dto.getCycle(), null);
        ewu.createCell(sheetSchedule, 7, k, dto.getMrCode(), null);
        ewu.createCell(sheetSchedule, 8, k, dto.getWoCode(), null);
        ewu.createCell(sheetSchedule, 9, k, dto.getModifyDate(), null);
        ewu.createCell(sheetSchedule, 10, k, dto.getNextDateModify(), null);
        ewu.createCell(sheetSchedule, 11, k, dto.getUserManager(), null);
        k++;
      }
      ewu.saveToFileExcel(workBook, pathFolder, fileTemplateName);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      if (workBook != null) {
        try {
          workBook.close();
        } catch (IOException ex) {
          log.error(ex.getMessage(), ex);
        }
      }
    }
    return new File(pathFolder + fileTemplateName);
  }

  @Override
  public ResultInSideDto importMrScheduleBTS(MultipartFile multipartFile) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    mapErrorResult.clear();
    cellErrorList.clear();
    try {
      if (multipartFile == null) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        resultInSideDto.setMessage(I18n.getLanguage("mrScheduleBTS.fileEmpty"));
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        String validate = validateFileImport(filePath);
        if (StringUtils.isStringNullOrEmpty(validate)) {
          File fileImp = new File(filePath);
          List<Object[]> lstData;
          if (filePath.contains("xlsx")) {
            lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 8,
                0, 11, 2);
          } else {
            lstData = CommonImport.getDataFromExcel(fileImp, 0, 8,
                0, 11, 2);
          }
          List<MrScheduleBtsDTO> lstAddOrUpdate = new ArrayList<>();
          if (lstData != null) {
            for (int i = lstData.size() - 1; i >= 0; i--) {
              if (lstData.get(i)[0] == null && lstData.get(i)[1] == null
                  && lstData.get(i)[2] == null && lstData.get(i)[3] == null
                  && lstData.get(i)[4] == null
                  && lstData.get(i)[5] == null && lstData.get(i)[6] == null
                  && lstData.get(i)[7] == null && lstData.get(i)[8] == null
                  && lstData.get(i)[9] == null && lstData.get(i)[10] == null) {
                lstData.remove(i);
              }
            }
            if (lstData.size() <= 1000) {
              boolean allTrue = true;
              int row = 8;
              for (Object[] obj : lstData) {
                MrScheduleBtsDTO dto = new MrScheduleBtsDTO();
                if (obj[0] != null) {
                  dto.setScheduleId(obj[0].toString().trim());
                } else {
                  dto.setScheduleId("");
                }
                if (obj[1] != null) {
                  dto.setMarketName(obj[1].toString().trim());
                } else {
                  dto.setMarketName("");
                }
                if (obj[2] != null) {
                  dto.setProvinceName(obj[2].toString().trim());
                } else {
                  dto.setProvinceName("");
                }
                if (obj[3] != null) {
                  dto.setStationCode(obj[3].toString().trim());
                } else {
                  dto.setStationCode("");
                }
                if (obj[4] != null) {
                  dto.setDeviceTypeName(obj[4].toString().trim());
                } else {
                  dto.setDeviceTypeName("");
                }
                if (obj[5] != null) {
                  dto.setSerial(obj[5].toString().trim());
                } else {
                  dto.setSerial("");
                }
                if (obj[6] != null) {
                  dto.setCycle(obj[6].toString().trim());
                } else {
                  dto.setCycle("");
                }
                if (obj[7] != null) {
                  dto.setMrCode(obj[7].toString().trim());
                } else {
                  dto.setMrCode("");
                }
                if (obj[8] != null) {
                  dto.setWoCode(obj[8].toString().trim());
                } else {
                  dto.setWoCode("");
                }
                if (obj[9] != null) {
                  dto.setModifyDate(obj[9].toString().trim());
                } else {
                  dto.setModifyDate("");
                }
                if (obj[10] != null) {
                  dto.setNextDateModify(obj[10].toString().trim());
                } else {
                  dto.setNextDateModify("");
                }
                if (obj[11] != null) {
                  dto.setUserManager(obj[11].toString().trim());
                } else {
                  dto.setUserManager("");
                }
                if (validateImportInfo(dto)) {
                  lstAddOrUpdate.add(dto);
                  cellErrorList.add(new ErrorInfo(row, I18n.getLanguage("common.success1")));
                } else {
                  allTrue = false;
                  cellErrorList.add(new ErrorInfo(row, getValidateResult(dto)));
                }
                row++;
              }

              if (allTrue) {
                if (!lstAddOrUpdate.isEmpty()) {
                  String res = mrScheduleBtsRepository.updateMrScheduleBts(lstAddOrUpdate);
                  if (res != null && "SUCCESS".equals(res)) {
                    for (ErrorInfo resultColumn : cellErrorList) {
                      resultColumn.setMsg(I18n.getLanguage("common.success1"));
                    }
                    resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
                  } else {
                    resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                        I18n.getLanguage("import.common.fail"));
                  }
                }
              } else {
                resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                    I18n.getLanguage("import.common.fail"));
              }
              resultInSideDto.setFilePath(filePath);
              exportFileImport(lstData, filePath, cellErrorList);

            } else {
              resultInSideDto = new ResultInSideDto(null, RESULT.DATA_OVER,
                  I18n.getLanguage("common.importing.maxrow"));
            }
          } else {
            resultInSideDto = new ResultInSideDto(null, RESULT.NODATA,
                I18n.getLanguage("common.searh.nodata"));
          }

        } else {
          resultInSideDto = new ResultInSideDto(null,
              RESULT.FILE_INVALID_FORMAT.equals(validate) ? RESULT.FILE_INVALID_FORMAT
                  : RESULT.ERROR, validate);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }

  private File exportFileEx(List<MrScheduleBtsDTO> lstData, List<ConfigHeaderExport> lstHeaderSheet)
      throws Exception {
    String fileNameOut = "MrScheduleBtsReport";
    ConfigFileExport configfileExport;
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet;
    String rootPath = tempFolder + File.separator;
    String language = I18n.getLocale();
    String fileTemplate = "TEMPLATE_EXPORT_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplate = "TEMPLATE_EXPORT.xlsx";
    }

    configfileExport = new ConfigFileExport(
        lstData
        , "CfgTime"
        , I18n.getLanguage("mrScheduleBTS.list.grid.report")
        , ""
        , 7
        , 4
        , lstHeaderSheet.size()
        , false
        , "language.mrScheduleBTS.list.grid"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , null
        , null
        , null
        , null
    );
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("mrScheduleBTS.list.grid.stt"),
        "HEAD", "STRING");

    configfileExport.setLangKey(I18n.getLocale());
    configfileExport.setIsAutoSize(true);
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
//    List<Integer> lsColumnHidden = new ArrayList<>();
//    lsColumnHidden.add(1);

    File fileExport = CommonExport.exportFileWithTemplateXLSX(
        "templates" + File.separator + fileTemplate
        , fileNameOut
        , configfileExport
        , rootPath,
        null,
        null,
        I18n.getLanguage("mrScheduleBTS.list.grid.report")

    );
    return fileExport;
  }

  public void exportFileImport(List<Object[]> lstData, String fileTempPath,
      List<ErrorInfo> lstError) {
    try {
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      Workbook workBook = ewu.readFileExcelFromTemplate(getFilePathTemplate());
      Sheet sheetOne = workBook.getSheetAt(0);
      //insert data from line i + 1
      int i = 8;
      CellStyle cellStResult = sheetOne.getRow(7).getCell(0).getCellStyle();
      Row row = sheetOne.getRow(7);
      Cell cell = row.createCell(12);
      cell.setCellStyle(cellStResult);
      cell.setCellValue(I18n.getLanguage("mrChecklist.list.grid.importError"));

      for (Object[] c_row : lstData) {
        if (c_row[8] == null && c_row[1] == null && c_row[2] == null && c_row[3] == null
            && c_row[4] == null && c_row[5] == null && c_row[6] == null && c_row[7] == null
            && c_row[9] == null && c_row[10] == null && c_row[11] == null
        ) {
          break;
        }
        for (int j = 0; j < 12; j++) {
          ewu.createCell(sheetOne, j, i, c_row[j] == null ? "" : c_row[j].toString().trim(), null);
        }
        i++;
      }

      for (ErrorInfo err : lstError) {
        row = sheetOne.getRow(err.getRow());
        cell = row.createCell(12);
        cell.setCellValue(err.getMsg());
      }
      FileOutputStream fileOut = null;
      try {

        // R3292_EDIT_DUNGNV50_13122012_START
        fileOut = new FileOutputStream(fileTempPath);
        // R3292_EDIT_DUNGNV50_13122012_END
        workBook.write(fileOut);
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      } finally {
        try {
          if (fileOut != null) {
            fileOut.close();
          }
          workBook.close();
        } catch (IOException ex) {
          log.error(ex.getMessage(), ex);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void processDataSearch(List<MrScheduleBtsDTO> data,
      Map<String, String> catLocationProvince) {
    for (MrScheduleBtsDTO resultDTO : data) {
      resultDTO.setProvinceName(catLocationProvince.get(resultDTO.getProvinceCode()));
      resultDTO.setDeviceTypeName(I18n.getLanguage(
          Constants.DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName().get(resultDTO.getDeviceType())));
      if (resultDTO.getWoStatus() != null) {
        Long woStatus = Long.valueOf(resultDTO.getWoStatus());
        if (Constants.WO_STATE.getStateName().containsKey(woStatus)) {
          resultDTO
              .setWoStatusName(I18n.getLanguage(Constants.WO_STATE.getStateName().get(woStatus)));
        }
      }
      if ("0".equals(resultDTO.getIsWoOriginal())) {
        resultDTO.setIsWoOriginal(I18n.getLanguage("mrScheduleBTS.list.grid.notOriginal"));
      } else {
        resultDTO.setIsWoOriginal(I18n.getLanguage("mrScheduleBTS.list.grid.original"));
      }
    }
  }

  private List<ConfigHeaderExport> renderHeaderSheet(String[] col, String[] align) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], align[i], false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private List<MrScheduleBtsDTO> getDataExport(MrScheduleBtsDTO mrScheduleBtsDTO) {
    List<MrScheduleBtsDTO> lstData = mrScheduleBtsRepository.onSearchExport(mrScheduleBtsDTO);
    if (lstData != null) {
      List<MrDeviceBtsDTO> lstProvince = mrDeviceBtsRepository.getListProvince(null);
      Map<String, String> catLocationProvince = new HashMap<>();
      if (lstProvince != null) {
        for (MrDeviceBtsDTO provinceDTO : lstProvince) {
          catLocationProvince.put(provinceDTO.getProvinceCode(), provinceDTO.getProvinceName());
        }
      }
      processDataSearch(lstData, catLocationProvince);
    } else {
      lstData = new ArrayList<>();
    }
    return lstData;
  }

  public String validateFileImport(String path) {
    String msg = "";
    if (path.contains(".xls")) {
//      if (path.contains(".xlsx")) {
//        msg = I18n.getLanguage("mrScheduleCd.import.file.invalid");
//      } else {
      boolean check = validateFileData(path);
      if (!check) {
        msg = I18n.getLanguage("common.import.errorTemplate");
      }
//      }
    } else {
      msg = I18n.getLanguage("mrScheduleCd.import.file.extend.invalid");
    }
    return msg;
  }

  private boolean validateFileData(String fileImportPathOut) {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_SCHEDULE_BTS_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_SCHEDULE_BTS_TEMPLATE_VI.xlsx";
    }
    boolean resultOut = true;
    String pathFileImport = pathTemplate + fileTemplateName;
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Workbook workBook = null;
    Workbook workBook2 = null;
    try {
      workBook = ewu.readFileExcelFromTemplate(pathFileImport);
      Sheet sheetTemplate = workBook.getSheetAt(0);

      workBook2 = ewu.readFileExcel(fileImportPathOut);
      Sheet sheetFileImport = workBook2.getSheetAt(0);

      Row rowFileImport = sheetFileImport.getRow(7);
      Row rowTemplate = sheetTemplate.getRow(7);
      String arrHeaderTemplate = "";
      String arrHeaderFileImport = "";
      int k = rowTemplate.getLastCellNum();
      for (int i = 0; i < k; i++) {
        if (rowTemplate.getCell(i).getStringCellValue() != null) {
          arrHeaderTemplate += "," + rowTemplate.getCell(i).getStringCellValue();
        } else {
          break;
        }
      }
      int l = rowFileImport.getLastCellNum();
      for (int j = 0; j < l; j++) {
        if (rowFileImport.getCell(j).getStringCellValue() != null) {
          arrHeaderFileImport += "," + rowFileImport.getCell(j).getStringCellValue();
        } else {
          break;
        }
      }
      if (!arrHeaderTemplate.equals(arrHeaderFileImport)) {
        resultOut = false;
      }
    } catch (Exception e) {
      resultOut = false;
      log.error(e.getMessage(), e);
    } finally {
      try {
        workBook.close();
        workBook2.close();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return resultOut;
  }

  private boolean validateImportInfo(MrScheduleBtsDTO dto) {
    if (StringUtils.isStringNullOrEmpty(dto.getScheduleId())) {
      setMapResult(dto, I18n.getLanguage("mrScheduleBTS.list.grid.scheduleId") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    }
    // TODO: them xu ly check ton tai schedule id
    if (StringUtils.isStringNullOrEmpty(dto.getNextDateModify())) {
      setMapResult(dto, I18n.getLanguage("mrScheduleBTS.list.grid.nextDateModify") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else {
      DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
      try {
        df.parse(dto.getNextDateModify());
      } catch (ParseException pe) {
        setMapResult(dto, I18n.getLanguage("mrScheduleBTS.list.grid.nextDateModify") + " " + I18n
            .getLanguage("valid.pattern.date"));
        return false;
      }
    }
    return true;
  }

  private void setMapResult(MrScheduleBtsDTO dto, String msg) {
    mapErrorResult.put(dto.getScheduleId(), msg);
  }

  private String getValidateResult(MrScheduleBtsDTO dto) {
    return mapErrorResult.get(dto.getScheduleId());
  }

  private String getFilePathTemplate() {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_SCHEDULE_BTS_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_SCHEDULE_BTS_TEMPLATE_VI.xlsx";
    }
    return "templates" + File.separator + "maintenance" + File.separator + fileTemplateName;
  }
}
