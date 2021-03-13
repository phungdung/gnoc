package com.viettel.gnoc.mr.business;

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
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.mr.repository.MrDeviceBtsRepository;
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
public class MrDeviceBtsBusinessImpl implements MrDeviceBtsBusiness {

  @Autowired
  MrDeviceBtsRepository mrDeviceBtsRepository;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;
  private Map<String, String> mapArea = new HashMap<>();
  private Map<String, String> mapAreaName = new HashMap<>();
  private Map<String, String> mapProvinceName = new HashMap<>();
  private Map<String, String> mapCountryName = new HashMap<>();


  @Value("${application.temp.folder}")
  private String tempFolder;

  private Map<String, String> mapErrorResult = new HashMap<>();
  private List<ErrorInfo> cellErrorList = new ArrayList<>();
  private String pathTemplate = "templates" + File.separator + "maintenance" + File.separator;

  private final static String MR_DEVICE_BTS_EXPORT = "MR_DEVICE_BTS_EXPORT";

  @Override
  public Datatable getListMrDeviceBtsPage(MrDeviceBtsDTO mrDeviceBtsDTO) {
    log.debug("Request to getListMrDeviceBtsPage : {}", mrDeviceBtsDTO);
    return mrDeviceBtsRepository.getListMrDeviceBtsPage(mrDeviceBtsDTO);
  }

  @Override
  public List<MrDeviceBtsDTO> getListMrDeviceBtsDTO(MrDeviceBtsDTO mrDeviceBtsDTO) {
    log.debug("Request to getListMrDeviceBtsDTO : {}", mrDeviceBtsDTO);
    return mrDeviceBtsRepository.getListMrDeviceBtsDTO(mrDeviceBtsDTO);
  }

  @Override
  public ResultInSideDto update(MrDeviceBtsDTO mrDeviceBtsDTO) {
    log.debug("Request to update : {}", mrDeviceBtsDTO);
    return mrDeviceBtsRepository.insertOrUpdate(mrDeviceBtsDTO);
  }

  @Override
  public ResultInSideDto delete(Long deviceId) {
    log.debug("Request to delete : {}", deviceId);
    return mrDeviceBtsRepository.delete(deviceId);
  }

  @Override
  public MrDeviceBtsDTO getDetail(Long deviceId) {
    log.debug("Request to delete : {}", deviceId);
    mapArea.clear();
    mapAreaName.clear();
    MrDeviceBtsDTO mrDeviceBtsDTO = mrDeviceBtsRepository.getDetail(deviceId);
    List<CatLocationDTO> dataList = catLocationBusiness
        .getCatLocationByParentId(mrDeviceBtsDTO.getMarketCode());
    if (dataList != null && !dataList.isEmpty()) {
      for (CatLocationDTO catLocationDTO : dataList) {
        mapArea
            .put(String.valueOf(catLocationDTO.getLocationCode()), catLocationDTO.getLocationId());
        mapAreaName.put(String.valueOf(catLocationDTO.getLocationCode()),
            catLocationDTO.getLocationName());
      }
    }
    mrDeviceBtsDTO.setAreaName(mapAreaName.get(mrDeviceBtsDTO.getAreaCode()));
    mrDeviceBtsDTO.setAreaID(mapArea.get(mrDeviceBtsDTO.getAreaCode()));
    return mrDeviceBtsDTO;
  }

  @Override
  public List<MrDeviceBtsDTO> getListDeviceType() {
    log.debug("Request to getListDeviceType : {}");
    return mrDeviceBtsRepository.getListDeviceType();
  }

  @Override
  public List<MrDeviceBtsDTO> getListfuelTypeByDeviceType(String deviceType, String marketCode) {
    log.debug("Request to getListfuelTypeByDeviceType : {}", deviceType, marketCode);
    return mrDeviceBtsRepository.getListfuelTypeByDeviceType(deviceType, marketCode);
  }

  @Override
  public List<MrDeviceBtsDTO> getListProducerByDeviceType(String deviceType, String marketCode) {
    log.debug("Request to getListProducerByDeviceType : {}", deviceType, marketCode);
    return mrDeviceBtsRepository.getListProducerByDeviceType(deviceType, marketCode);
  }

  @Override
  public List<MrDeviceBtsDTO> getListProvince(String marketCode) {
    log.debug("Request to getListProvince : {}", marketCode);
    return mrDeviceBtsRepository.getListProvince(marketCode);
  }

  @Override
  public List<MrDeviceBtsDTO> getListMrDeviceBtsForCD(MrDeviceBtsDTO mrDeviceBtsDTO)
      throws Exception {
    log.debug("Request to getListMrDeviceBtsForCD : {}", mrDeviceBtsDTO);
    return mrDeviceBtsRepository.getListMrDeviceBtsForCD(mrDeviceBtsDTO);
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoBts(String woCode) {
    log.debug("Request to getListWoBts : {}", woCode);
    return mrDeviceBtsRepository.getListWoBts(woCode);
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO dto) {
    log.debug("Request to getListWoCodeMrScheduleBtsHisDetail : {}", dto);
    return mrDeviceBtsRepository.getListWoCodeMrScheduleBtsHisDetail(dto);
  }

  @Override
  public File exportSearchData(MrDeviceBtsDTO mrDeviceBtsDTO) throws Exception {
    List<MrDeviceBtsDTO> mrDeviceBtsDTOList = mrDeviceBtsRepository.onSearchExport(mrDeviceBtsDTO);
    return exportFileEx(mrDeviceBtsDTOList, "EXPORT");
  }

  @Override
  public ResultInSideDto importMrDeviceBts(MultipartFile multipartFile) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    mapErrorResult.clear();
    cellErrorList.clear();
    try {
      if (multipartFile == null) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        resultInSideDto.setMessage(I18n.getLanguage("mrDeviceBts.fileEmpty"));
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }
        File fileImp = new File(filePath);
        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImp,
            0,
            7,
            0,
            14,
            2
        );

        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        List<Object[]> lstData;
        if (filePath.contains("xlsx")) {
          lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 8,
              0, 14, 2);
        } else {
          lstData = CommonImport.getDataFromExcel(fileImp, 0, 8,
              0, 14, 2);
        }
        List<MrDeviceBtsDTO> lstAddOrUpdate = new ArrayList<>();
        mapErrorResult.clear();
        cellErrorList.clear();
        if (lstData.size() != 0) {
          for (int i = lstData.size() - 1; i >= 0; i--) {
            if (lstData.get(i)[0] == null && lstData.get(i)[1] == null
                && lstData.get(i)[2] == null
                && lstData.get(i)[3] == null && lstData.get(i)[4] == null
                && lstData.get(i)[5] == null
                && lstData.get(i)[6] == null && lstData.get(i)[7] == null
                && lstData.get(i)[8] == null
                && lstData.get(i)[9] == null && lstData.get(i)[10] == null
                && lstData.get(i)[11] == null
                && lstData.get(i)[12] == null && lstData.get(i)[13] == null
                && lstData.get(i)[14] == null) {
              lstData.remove(i);
            }
          }
          boolean allTrue = true;
          int row = 8;
          for (Object[] obj : lstData) {
            MrDeviceBtsDTO dto = new MrDeviceBtsDTO();
            if (obj[0] != null) {
              dto.setDeviceId(Long.parseLong(obj[0].toString().trim()));
            } else {
              dto.setDeviceId(null);
            }
            if (obj[1] != null) {
              dto.setCountryName(obj[1].toString().trim());
            } else {
              dto.setCountryName("");
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
              dto.setDeviceTypeStr(obj[4].toString().trim());
            } else {
              dto.setDeviceTypeStr("");
            }
            if (obj[5] != null) {
              dto.setWoCode(obj[5].toString().trim());
            } else {
              dto.setWoCode("");
            }
            if (obj[6] != null) {
              dto.setUserManager(obj[6].toString().trim());
            } else {
              dto.setUserManager("");
            }
            if (obj[7] != null) {
              dto.setSerial(obj[7].toString().trim());
            } else {
              dto.setSerial("");
            }
            if (obj[8] != null) {
              dto.setProducer(obj[8].toString().trim());
            } else {
              dto.setProducer("");
            }
            if (obj[9] != null) {
              dto.setFuelTypeName(obj[9].toString().trim());
            } else {
              dto.setFuelTypeName("");
            }
            if (obj[10] != null) {
              dto.setPower(obj[10].toString().trim());
            } else {
              dto.setPower("");
            }
            if (obj[11] != null) {
              dto.setMaintenanceTime(obj[11].toString().trim());
            } else {
              dto.setMaintenanceTime("");
            }
            if (obj[12] != null) {
              dto.setOperationHour(obj[12].toString().trim());
            } else {
              dto.setOperationHour(null);
            }
            if (obj[13] != null) {
              dto.setPutStatusStr(obj[13].toString().trim());
            } else {
              dto.setPutStatusStr("");
            }
            if (obj[14] != null) {
              dto.setStrInKTTS(obj[14].toString().trim());
            } else {
              dto.setStrInKTTS("");
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
              //set lại giá trị để lưu vào DB
              List<MrDeviceBtsDTO> lstRoot = new ArrayList<>();
              Map<Long, MrDeviceBtsDTO> mapRoot = new HashMap<>();
              List<Long> lstIds = new ArrayList<>();
              for (int i = 0; i < lstAddOrUpdate.size(); i++) {
                lstIds.add(lstAddOrUpdate.get(i).getDeviceId());
                if ((i != 0 && i % 500 == 0) || i == lstAddOrUpdate.size() - 1) {
                  List<MrDeviceBtsDTO> deviceTemps = mrDeviceBtsRepository
                      .getListMrDeviceBtsByListId(lstIds);
                  if (deviceTemps != null && deviceTemps.size() > 0) {
                    lstRoot.addAll(deviceTemps);
                  }
                  lstIds.clear();
                }
              }

              lstRoot.forEach(i -> {
                mapRoot.put(i.getDeviceId(), i);
              });
              for (int i = lstAddOrUpdate.size() - 1; i > -1; i--) {
                MrDeviceBtsDTO btsDTO = lstAddOrUpdate.get(i);
                MrDeviceBtsDTO mrDeviceBtsDTO = mapRoot.get(btsDTO.getDeviceId());
                if (mrDeviceBtsDTO != null) {
                  btsDTO.setMarketCode(mrDeviceBtsDTO.getMarketCode());
                  btsDTO.setProvinceCode(mrDeviceBtsDTO.getProvinceCode());
//                      btsDTO.setStationCode(mrDeviceBtsDTO.getStationCode());
                  btsDTO.setDeviceType(mrDeviceBtsDTO.getDeviceType());
//                  btsDTO.setWoCode(mrDeviceBtsDTO.getWoCode());//tiennv tam thoi bo comment lai
//                      btsDTO.setUserManager(mrDeviceBtsDTO.getUserManager());
                  btsDTO.setUpdateUser(mrDeviceBtsDTO.getUpdateUser());
//                      btsDTO.setSerial(mrDeviceBtsDTO.getSerial());
                  btsDTO.setFuelType(mrDeviceBtsDTO.getFuelType());
//                      btsDTO.setPower(mrDeviceBtsDTO.getPower());
                  btsDTO.setAreaCode(mrDeviceBtsDTO.getAreaCode());
                  btsDTO
                      .setOperationHourLastUpdate(mrDeviceBtsDTO.getOperationHourLastUpdate());
//                      btsDTO.setProducer(mrDeviceBtsDTO.getProducer());
                  btsDTO.setPutStatus(mrDeviceBtsDTO.getPutStatus());
                  btsDTO.setInKTTS(mrDeviceBtsDTO.getInKTTS());
//                      btsDTO.setCountryName(mrDeviceBtsDTO.getCountryName());
//                      btsDTO.setProvinceName(mrDeviceBtsDTO.getProvinceName());
                }

              }

              String res = mrDeviceBtsRepository.UpdateListDeviceBts(lstAddOrUpdate);
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
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
        }
        //Xuất ra file kết quả
        resultInSideDto.setFilePath(filePath);
        exportFileImport(filePath, cellErrorList);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
    }
    return resultInSideDto;
  }

  public String validateFileImport(String path) {
    String msg = "";
    if (path.contains(".xls")) {
//      if (path.contains(".xlsx")) {
//        msg = I18n.getLanguage("mrScheduleCd.import.file.invalid");
//      } else {
      boolean check = validateFileData(path);
      if (!check) {
        msg = I18n.getLanguage("mrChecklist.import.errorTemplate");
      }
//      }
    } else {
      msg = I18n.getLanguage("mrScheduleCd.import.file.extend.invalid");
    }
    return msg;
  }

  private boolean validateFileData(String fileImportPathOut) {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_UPDATE_DEVICE_BTS_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_UPDATE_DEVICE_BTS_TEMPLATE_VI.xlsx";
    }
    boolean resultOut = true;
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Workbook workBook = null;
    Workbook workBook2 = null;
    try {
      String pathFileImport = pathTemplate + fileTemplateName;
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

  private boolean validateImportInfo(MrDeviceBtsDTO dto) {
    if (StringUtils.isStringNullOrEmpty(dto.getDeviceId())) {
      setMapResult(dto,
          I18n.getLanguage("mrDeviceBts.list.deviceId") + " " + I18n.getLanguage("common.notnull"));
      return false;
    }
    // TODO: them xu ly check ton tai MrDeviceBts
    if (StringUtils.isStringNullOrEmpty(dto.getMaintenanceTime())) {
      setMapResult(dto, I18n.getLanguage("mrDeviceBts.list.maintenanceTime") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else {
      DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
      try {
        df.parse(dto.getMaintenanceTime());
      } catch (ParseException pe) {
        setMapResult(dto, I18n.getLanguage("mrDeviceBts.list.maintenanceTime") + " " + I18n
            .getLanguage("valid.pattern.date"));
        return false;
      }
    }
//    if (dto.getOperationHour().length() > 10) {
//      setMapResult(dto, I18n.getLanguage("mrDeviceBts.list.operationHour") + " " + I18n
//          .getLanguage("mrDeviceBts.operationHour.overlength"));
//      return false;
//    }
//    try {
//      Long.parseLong(dto.getOperationHour());
//    } catch (NumberFormatException nfe) {
//      setMapResult(dto, I18n.getLanguage("mrDeviceBts.list.operationHour") + " " + I18n
//          .getLanguage("mrDeviceBts.numberFormatInvalid"));
//      return false;
//    }
    return true;
  }

  private void setMapResult(MrDeviceBtsDTO dto, String msg) {
    mapErrorResult.put(String.valueOf(dto.getDeviceId()), msg);
  }

  private String getValidateResult(MrDeviceBtsDTO dto) {
    return mapErrorResult.get(String.valueOf(dto.getDeviceId()));
  }

  public void exportFileImport(String fileTempPath, List<ErrorInfo> lstError) {
    try {
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      Workbook workBook = ewu.readFileExcel(fileTempPath);
      Sheet sheetOne = workBook.getSheetAt(0);
      //insert data from line i + 1
      int i = 8;
      CellStyle cellStResult = sheetOne.getRow(7).getCell(0).getCellStyle();
      Row row = sheetOne.getRow(7);
      Cell cell = row.createCell(15);
      cell.setCellStyle(cellStResult);
      cell.setCellValue(I18n.getLanguage("mrChecklist.list.grid.importError"));
      for (ErrorInfo err : lstError) {
        row = sheetOne.getRow(err.getRow());
        cell = row.createCell(15);
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

  @Override
  public File getFileTemplate(MrDeviceBtsDTO mrDeviceBtsDTO) {

    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Workbook workBook = null;
    String pathFolder =
        tempFolder + File.separator + FileUtils.createPathByDate(new Date()) + File.separator;
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_UPDATE_DEVICE_BTS_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_UPDATE_DEVICE_BTS_TEMPLATE_VI.xlsx";
    }
    try {
      String pathFileImport = pathTemplate + fileTemplateName;
      int k = 8;
      workBook = ewu.readFileExcelFromTemplate(pathFileImport, k);
      Sheet sheetDeviceBTS = workBook.getSheetAt(0);
      List<MrDeviceBtsDTO> lstData = getDataExport(mrDeviceBtsDTO);
//      CellStyle cellSt1 = null;
      for (MrDeviceBtsDTO dto : lstData) {
        if (dto == null) {
          break;
        }
        ewu.createCell(sheetDeviceBTS, 0, k, String.valueOf(dto.getDeviceId()), null);
        ewu.createCell(sheetDeviceBTS, 1, k, dto.getCountryName(), null);
        ewu.createCell(sheetDeviceBTS, 2, k, dto.getProvinceName(), null);
        ewu.createCell(sheetDeviceBTS, 3, k, dto.getStationCode(), null);
        ewu.createCell(sheetDeviceBTS, 4, k, dto.getDeviceTypeStr(), null);
        ewu.createCell(sheetDeviceBTS, 5, k, dto.getWoCode(), null);
        ewu.createCell(sheetDeviceBTS, 6, k, dto.getUserManager(), null);
        ewu.createCell(sheetDeviceBTS, 7, k, dto.getSerial(), null);
        ewu.createCell(sheetDeviceBTS, 8, k, dto.getProducer(), null);
        ewu.createCell(sheetDeviceBTS, 9, k, dto.getFuelTypeName(), null);
        ewu.createCell(sheetDeviceBTS, 10, k, dto.getPower(), null);
        ewu.createCell(sheetDeviceBTS, 11, k, dto.getMaintenanceTime(), null);
        ewu.createCell(sheetDeviceBTS, 12, k, dto.getOperationHour(), null);
        ewu.createCell(sheetDeviceBTS, 13, k, dto.getPutStatusStr(), null);
        ewu.createCell(sheetDeviceBTS, 14, k, dto.getStrInKTTS(), null);
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

  private List<MrDeviceBtsDTO> getDataExport(MrDeviceBtsDTO mrDeviceBtsDTO) {
    List<MrDeviceBtsDTO> lstData = mrDeviceBtsRepository.onSearchExport(mrDeviceBtsDTO);
    if (lstData != null && lstData.size() > 0) {
      List<MrDeviceBtsDTO> lstProvince = mrDeviceBtsRepository.getListProvince(null);
      Map<String, String> catLocationProvince = new HashMap<>();
      if (lstProvince != null) {
        for (MrDeviceBtsDTO provinceDTO : lstProvince) {
          catLocationProvince.put(provinceDTO.getProvinceCode(), provinceDTO.getProvinceName());
        }
      }
    }
    return lstData;
  }

  private File exportFileEx(List<MrDeviceBtsDTO> lstData, String key) throws Exception {
    String title = "";
    String fileNameOut = "";
    String subTitle = "";
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    if (Constants.RESULT_IMPORT.equals(key)) {
    } else {
      fileNameOut = MR_DEVICE_BTS_EXPORT;
      title = I18n.getLanguage("mrDeviceBts.export.title");
      subTitle = I18n
          .getLanguage("mrDeviceBts.export.exportDate", DateTimeUtils.convertDateOffset());
      lstHeaderSheet = readerHeaderSheet("countryName", "provinceName", "stationCode",
          "deviceTypeStr", "woCode", "userManager", "serial", "producer", "fuelTypeName", "power",
          "maintenanceTime", "operationHour", "putStatusStr", "StrInKTTS",
          "operationHourLastUpdate");
    }

    Map<String, String> filedSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstData,
        I18n.getLanguage("mrDeviceBts.export.title"),
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.mrDeviceBts.list",
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
    configFileExport.setCustomColumnWidthNoMerge(
        new String[]{"1500", "4000", "4000", "4000", "4000", "4000", "4000", "10000", "8000",
            "4000", "4000", "4000", "4000", "4000", "4000", "4000",});
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
    if (count != 15) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }

    if (!(I18n.getLanguage("mrDeviceBts.list.deviceId") + "*")
        .equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.countryName")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.provinceName")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.stationCode")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.deviceTypeStr")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.woCode")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.userManager")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.serial")
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.producer")
        .equalsIgnoreCase(objects[8].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.fuelTypeName")
        .equalsIgnoreCase(objects[9].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.power")
        .equalsIgnoreCase(objects[10].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.maintenanceTime")
        .equalsIgnoreCase(objects[11].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.operationHour")
        .equalsIgnoreCase(objects[12].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.putStatusStr")
        .equalsIgnoreCase(objects[13].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDeviceBts.list.StrInKTTS")
        .equalsIgnoreCase(objects[14].toString().trim())) {
      return false;
    }
    return true;
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

  //tiennv them
  @Override
  public List<MrDeviceBtsDTO> getListSupplierBtsByDeviceType(String deviceType, String marketCode) {
    return mrDeviceBtsRepository.getListSupplierBtsByDeviceType(deviceType, marketCode);
  }
}
