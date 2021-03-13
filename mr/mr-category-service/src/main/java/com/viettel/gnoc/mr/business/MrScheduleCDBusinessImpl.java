package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureCDDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdExportDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdHisDTO;
import com.viettel.gnoc.maintenance.model.MrDeviceCDEntity;
import com.viettel.gnoc.mr.repository.MrCfgProcedureCDRepository;
import com.viettel.gnoc.mr.repository.MrDeviceCDRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleCDRepository;
import com.viettel.gnoc.mr.repository.MrScheduleCdHisRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MrScheduleCDBusinessImpl implements MrScheduleCDBusiness {

  @Autowired
  MrScheduleCDRepository mrScheduleCDRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  MrCfgProcedureCDRepository mrCfgProcedureCDRepository;

  @Autowired
  MrDeviceCDRepository mrDeviceCDRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  MrRepository mrRepository;

  @Autowired
  MrScheduleCdHisRepository mrScheduleCdHisRepository;

  private Map<String, String> mapErrorResult = new HashMap<>();
  private List<ErrorInfo> cellErrorList = new ArrayList<>();
  private Map<String, String> mapAddObject = new HashMap<>();//kiem tra xem du lieu nhap trong file co trung hay khong
  private Map<String, MrScheduleCdExportDTO> mapCurrentSchedule = new HashMap<>();

  @Override
  public Datatable onSearch(MrScheduleCdDTO mrScheduleCdDTO) {
    Datatable datatable = mrScheduleCDRepository.onSearch(mrScheduleCdDTO);
    return datatable;
  }

  @Override
  public File exportSearchData(MrScheduleCdDTO mrScheduleCdDTO) throws Exception {
    List<MrScheduleCdExportDTO> lstData = mrScheduleCDRepository.onSearchExport(mrScheduleCdDTO);
    String[] header = new String[]{
        "scheduleId",
        "marketName",
        "stationCode",
        "deviceType",
        "deviceName",
        "cycle",
        "mrId",
        "woCode",
        "userMrHard",
        "lastDate",
        "nextDate",
        "nextDateModify"};
    String[] align = new String[]{
        "CENTER",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "RIGHT",
        "RIGHT",
        "RIGHT",
    };

    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header, align);
    return exportFileEx(lstData, lstHeaderSheet, "");
  }

  @Override
  public ResultInSideDto importMrScheduleCD(MultipartFile multipartFile) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    mapErrorResult.clear();
    cellErrorList.clear();
    mapAddObject.clear();
    reloadMap();
    try {
      UserToken userToken = TicketProvider.getUserToken();
      if (multipartFile == null) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        resultInSideDto.setMessage(I18n.getLanguage("mrScheduleCd.fileEmpty"));
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
                0, 12, 2);
          } else {
            lstData = CommonImport.getDataFromExcel(fileImp, 0, 8,
                0, 12, 2);
          }

          List<MrScheduleCdDTO> lstAddOrUpdate = new ArrayList<>();
          if (lstData != null && !lstData.isEmpty()) {
            if (lstData.size() <= 1000) {
              boolean allTrue = true;
              int row = 8;
              for (Object[] obj : lstData) {
                if (obj[1] != null && obj[12] != null) {
                  MrScheduleCdExportDTO dto = new MrScheduleCdExportDTO();
                  dto.setScheduleId(obj[1].toString().trim());
                  dto.setNextDateModify(obj[12].toString().trim());
                  dto.setModifyUser(userToken.getUserName());
                  dto.setUpdatedDate(DateTimeUtils.convertDateToString(new Date()));

                  if (validateImportInfo(dto)) {
                    if (mapAddObject.get(dto.getScheduleId()) == null) {
                      MrScheduleCdDTO objUpdate = mrScheduleCDRepository
                          .findById(Long.valueOf(dto.getScheduleId()));
                      objUpdate.setNextDateModify(
                          DateTimeUtils.convertStringToDate(dto.getNextDateModify()));
                      objUpdate.setModifyUser(dto.getModifyUser());
                      objUpdate
                          .setUpdatedDate(DateTimeUtils.convertStringToDate(dto.getUpdatedDate()));
                      objUpdate
                          .setModifyDate(DateTimeUtils.convertStringToDate(dto.getUpdatedDate()));

                      mapAddObject.put(dto.getScheduleId(), dto.getScheduleId());
                      lstAddOrUpdate.add(objUpdate);
                      cellErrorList.add(
                          new ErrorInfo(row, I18n.getLanguage("mrDeviceCD.import.validRecord")));
                    } else {
                      cellErrorList.add(new ErrorInfo(row,
                          I18n.getLanguage("mrScheduleCd.scheduleId.dupplicate.inFile")));
                      allTrue = false;
                    }
                  } else {
                    allTrue = false;
                    cellErrorList.add(new ErrorInfo(row, getValidateResult(obj)));
                  }
                } else if (obj[1] == null) {
                  cellErrorList.add(
                      new ErrorInfo(row, I18n.getLanguage("mrScheduleCd.scheduleId.notEmpty")));
                  allTrue = false;
                } else if (obj[12] == null) {
                  cellErrorList.add(
                      new ErrorInfo(row, I18n.getLanguage("mrScheduleCd.nextDateModify.notEmpty")));
                  allTrue = false;
                }
                row++;
              }

              if (allTrue) {
                if (!lstAddOrUpdate.isEmpty()) {
                  String res = mrScheduleCDRepository.insertOrUpdateListCd(lstAddOrUpdate);
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
                "File " + I18n.getLanguage("common.searh.nodata"));
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

  @Override
  public ResultInSideDto deleteById(Long id) {
    MrScheduleCdDTO dto = mrScheduleCDRepository.findById(id);
    MrDeviceCDEntity dtoMD = mrDeviceCDRepository.findMrDeviceById(dto.getDeviceCdId());
    switch (dto.getCycle()) {
      case "1":
        dtoMD.setIsComplete1m(1L);
        dtoMD.setLastDate1M(new Date());
        break;
      case "3":
        dtoMD.setIsComplete3m(1L);
        dtoMD.setLastDate3M(new Date());
        break;
      case "6":
        dtoMD.setIsComplete6m(1L);
        dtoMD.setLastDate6M(new Date());
        break;
      case "12":
        dtoMD.setIsComplete12m(1L);
        dtoMD.setLastDate12M(new Date());
        break;
      default:
        break;
    }
    //move His
    ResultInSideDto resultInSideDto = insertMrScheduleCDTelHis(dto);
    if (resultInSideDto != null && RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      resultInSideDto = mrScheduleCDRepository.deleteById(id);
    }

    UserToken userToken = ticketProvider.getUserToken();
    dtoMD.setUpdateUser(userToken.getUserName());
    dtoMD.setUpdateDate(new Date());
    //set lastDate =  thooi gian hien tai co gio phut giay
    if (resultInSideDto != null && RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      resultInSideDto = mrDeviceCDRepository.insertOrUpdate(dtoMD);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto addOrUpdate(MrScheduleCdDTO dto) {
    return mrScheduleCDRepository.addOrUpdate(dto);
  }

  @Override
  public MrScheduleCdDTO findById(Long id) {
    MrScheduleCdDTO dto = mrScheduleCDRepository.findById(id);
    if (dto != null && dto.getProcedureId() != null) {
      try {
        MrCfgProcedureCDDTO cfgDTO = mrCfgProcedureCDRepository.findById(dto.getProcedureId());
        dto.setProcedureName(cfgDTO.getProcedureName());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return dto;
  }

  //  private File exportFileEx(List<MrScheduleCdExportDTO> lstData,
//      List<ConfigHeaderExport> lstHeaderSheet, String code) throws Exception {
//    String title = I18n.getLanguage("mrScheduleCd.report");
//    String sheetName = title;
//    String fileNameOut = "MrScheduleCdReport";
//    ConfigFileExport configfileExport = null;
//    List<ConfigFileExport> fileExports = new ArrayList<>();
//    Map<String, String> fieldSplit = new HashMap<>();
//    CellConfigExport cellSheet = null;
//    if (Constants.RESULT_IMPORT.equals(code)) {
//    } else {
//      configfileExport = new ConfigFileExport(
//          lstData
//          , sheetName
//          , title
//          , null
//          , 7
//          , 3
//          , lstHeaderSheet.size()
//          , true
//          , "language.mrScheduleCd.list"
//          , lstHeaderSheet
//          , fieldSplit
//          , ""
//          , I18n.getLanguage("cfgProcedureView.export.firstLeftHeader")
//          , I18n.getLanguage("cfgProcedureView.export.secondLeftHeader")
//          , I18n.getLanguage("cfgProcedureView.export.firstRightHeader")
//          , I18n.getLanguage("cfgProcedureView.export.secondRightHeader")
//      );
//      cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("mrScheduleCd.list.stt"),
//          "HEAD", "STRING");
//    }
//
//    List<CellConfigExport> lstCellSheet = new ArrayList<>();
//    List<Integer> lstColumnHide = new ArrayList<>();
//    lstCellSheet.add(cellSheet);
//    lstColumnHide.add(1);
//    configfileExport.setLstCreatCell(lstCellSheet);
//    configfileExport.setLangKey(I18n.getLocale());
//    configfileExport.setLsColumnHidden(lstColumnHide);
//    fileExports.add(configfileExport);
//
//    String fileTemplate = "templates" + File.separator
//        + "TEMPLATE_EXPORT.xls";
//    String rootPath = tempFolder + File.separator;
//    File fileExport = CommonExport.exportExcel(
//        fileTemplate
//        , fileNameOut
//        , fileExports
//        , rootPath
//        , new String[]{}
//    );
//    return fileExport;
//  }

  private File exportFileEx(List<MrScheduleCdExportDTO> lstData,
      List<ConfigHeaderExport> lstHeaderSheet, String code) throws Exception {
    String fileNameOut = "MrScheduleCdReport";
    ConfigFileExport configfileExport;
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet;
    String rootPath = tempFolder + File.separator;

    configfileExport = new ConfigFileExport(
        lstData
        , "CfgTime"
        , I18n.getLanguage("mrScheduleCd.report")
        , ""
        , 7
        , 4
        , lstHeaderSheet.size()
        , false
        , "language.mrScheduleCd.list"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , null
        , null
        , null
        , null
    );
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("mrScheduleCd.list.stt"),
        "HEAD", "STRING");

    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    List<Integer> lsColumnHidden = new ArrayList<>();
    lsColumnHidden.add(1);

    File fileExport = CommonExport.exportFileWithTemplateXLSX(
        "templates" + File.separator + "TEMPLATE_EXPORT.xlsx"
        , fileNameOut
        , configfileExport
        , rootPath,
        lsColumnHidden,
        null,
        I18n.getLanguage("mrScheduleCd.report")

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
      Cell cell = row.createCell(13);
      cell.setCellStyle(cellStResult);
      cell.setCellValue(I18n.getLanguage("mrChecklist.list.grid.importError"));

      for (Object[] c_row : lstData) {
        if (c_row[8] == null && c_row[1] == null && c_row[2] == null && c_row[3] == null
            && c_row[4] == null && c_row[5] == null && c_row[6] == null && c_row[7] == null) {
          break;
        }
        for (int j = 0; j < 13; j++) {
          ewu.createCell(sheetOne, j, i, c_row[j] == null ? "" : c_row[j].toString().trim(), null);
        }
        i++;
      }

      for (ErrorInfo err : lstError) {
        row = sheetOne.getRow(err.getRow());
        cell = row.createCell(13);
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

  private List<ConfigHeaderExport> renderHeaderSheet(String[] col, String[] align) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], align[i], false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  public String validateFileImport(String path) {
    String msg = "";
    if (path.contains(".xls")) {
//      if (path.contains(".xlsx")) {
//        msg = I18n.getLanguage("mrScheduleCd.import.file.invalid");
//
//      }
      boolean check = validateFileData(path);
      if (!check) {
        return I18n.getLanguage("common.import.errorTemplate");
      }
    } else {
      msg = I18n.getLanguage("mrScheduleCd.import.file.extend.invalid");
    }
    return msg;
  }

  private boolean validateFileData(String fileImportPathOut) {
//    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_SCHEDULECD_TEMPLATE.xlsx";
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Workbook workBook = null;
    Workbook workBook2 = null;
    boolean resultOut = true;
    try {
      String pathFileImport =
          "templates" + File.separator + "maintenance" + File.separator + fileTemplateName;
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

  private boolean validateImportInfo(MrScheduleCdExportDTO dto) {
    // Neu id rong
    if (StringUtils.isStringNullOrEmpty(dto.getScheduleId())) {
      setMapResult(dto, "mrScheduleCd.scheduleId.notEmpty");
      return false;
    }
    // Neu ngay sua rong
    if (StringUtils.isStringNullOrEmpty(dto.getNextDateModify())) {
      setMapResult(dto, "mrScheduleCd.nextDateModify.notEmpty");
      return false;
    }

    // Neu id khong phai la so
    if (!StringUtils.isStringNullOrEmpty(dto.getScheduleId())) {
      if (!StringUtils.isLong(dto.getScheduleId())) {
        setMapResult(dto, "mrScheduleCd.scheduleId.invalid");
        return false;
      }
    }

    // Neu dong sua khong ton tai
    if (mapCurrentSchedule.get(dto.getScheduleId()) == null) {
      setMapResult(dto, "mrScheduleCd.scheduleId.notFound");
      return false;
    }

    // Validate ngay du lien BD
    if (!StringUtils.isStringNullOrEmpty(dto.getNextDateModify())) {
      // Ngay truoc khi sua
      // Date oldDateModify = DateUtil.string2Date(mapCurrentSchedule.get(dto.getScheduleId()).getNextDateModify());
      // Ngay sau khi sua
      try {
        Date newDateModify = DateTimeUtils.string2Date(dto.getNextDateModify());

        // Neu ngay truoc khi sua la ngay qua khu
              /*
              if (DateUtil.getYY(oldDateModify) < DateUtil.getYY(new Date())) {
                  setMapResult(dto, "mrScheduleCd.nextDateModify.edit.invalid");
                  return false;
              } else if (DateUtil.getYY(oldDateModify) == DateUtil.getYY(new Date())) {
                  if (DateUtil.getMonth(oldDateModify) < DateUtil.getMonth(new Date())) {
                      setMapResult(dto, "mrScheduleCd.nextDateModify.edit.invalid");
                      return false;
                  }
              }
              */
        // Neu nam sau khi su < nam hien tai
        if (DateUtil.getYY(newDateModify) < DateUtil.getYY(new Date())) {
          setMapResult(dto, "mrScheduleCd.nextDateModify.invalid");
          return false;
        } else if (DateUtil.getYY(newDateModify) == DateUtil.getYY(new Date())) {
          if (DateUtil.getMonth(newDateModify) < DateUtil.getMonth(new Date())) {
            // Neu new date <= thang hien tai
            setMapResult(dto, "mrScheduleCd.nextDateModify.invalid");
            return false;
          } else if (DateUtil.getMonth(newDateModify) == DateUtil.getMonth(new Date())) {
            if (DateUtil.getDayInMonth(newDateModify) < DateUtil.getDayInMonth(new Date())) {
              setMapResult(dto, "mrScheduleCd.nextDateModify.invalid");
              return false;
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        setMapResult(dto, "mrScheduleCd.nextDateModify.invalid");
        return false;
      }
    }

    return true;
  }

  private void setMapResult(MrScheduleCdExportDTO dto, String msg) {
    mapErrorResult.put(dto.getScheduleId(), I18n.getLanguage(msg));
  }

  private String getValidateResult(Object[] obj) {
    return mapErrorResult.get(obj[1] == null ? "" : obj[1].toString().trim());
  }

  private void reloadMap() {
    mapCurrentSchedule.clear();
    List<MrScheduleCdExportDTO> lstCrProcessCurrent = mrScheduleCDRepository
        .onSearchExport(new MrScheduleCdDTO());
    if (lstCrProcessCurrent != null) {
      for (MrScheduleCdExportDTO dto : lstCrProcessCurrent) {
        mapCurrentSchedule.put(dto.getScheduleId(), dto);
      }
    }
  }

  private String getFilePathTemplate() {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_SCHEDULECD_TEMPLATE.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_SCHEDULECD_TEMPLATE.xlsx";
    }
    return "templates" + File.separator + "maintenance" + File.separator + fileTemplateName;
  }

  @Override
  public File getFileTemplate() {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Workbook workBook = null;
    String fileTemplateName = "IMPORT_SCHEDULECD_TEMPLATE.xlsx";
    String pathFolder =
        tempFolder + File.separator + FileUtils.createPathByDate(new Date()) + File.separator;
    try {
      workBook = ewu.readFileExcelFromTemplate(getFilePathTemplate());
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

  private ResultInSideDto insertMrScheduleCDTelHis(MrScheduleCdDTO objUpdate) {
    MrInsideDTO mrInsideDTO = new MrInsideDTO();
    MrScheduleCdHisDTO scheduleHisDTO = new MrScheduleCdHisDTO();
    if (!StringUtils.isStringNullOrEmpty(objUpdate.getMrId())) {
      mrInsideDTO = mrRepository.findMrById(objUpdate.getMrId());
      scheduleHisDTO
          .setMrId(String.valueOf(objUpdate.getMrId()));
    }
    MrCfgProcedureCDDTO procedureTelDTO = mrCfgProcedureCDRepository
        .findById(objUpdate.getProcedureId());
    if (procedureTelDTO == null) {
      procedureTelDTO = new MrCfgProcedureCDDTO();
    }

    scheduleHisDTO.setMarketCode(objUpdate.getMarketCode());
    scheduleHisDTO.setDeviceType(objUpdate.getDeviceType());
    scheduleHisDTO.setDeviceCdId(
        objUpdate.getDeviceCdId() != null ? String.valueOf(objUpdate.getDeviceCdId()) : null);
    scheduleHisDTO.setDeviceName(objUpdate.getDeviceName());
//    if (!StringUtils.isStringNullOrEmpty(objUpdate.getNextDateModify())) {
//      try {
//        scheduleHisDTO
//            .setMrDate(DateTimeUtils.convertStringToDateTime(objUpdate.getNextDateModify()));
//      } catch (Exception e) {
//        log.error(e.getMessage());
//      }
//    }
    scheduleHisDTO.setMrDate(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
    scheduleHisDTO.setMrContent(procedureTelDTO.getMrContentId());
    scheduleHisDTO.setMrType(mrInsideDTO.getMrType());
    scheduleHisDTO.setMrCode(mrInsideDTO.getMrCode());
    scheduleHisDTO.setProcedureId(
        objUpdate.getProcedureId() != null ? String.valueOf(objUpdate.getProcedureId()) : null);
    scheduleHisDTO.setProcedureName(procedureTelDTO.getProcedureName());
    scheduleHisDTO.setCycle(objUpdate.getCycle());
    scheduleHisDTO.setMrMode("H");
    return mrScheduleCdHisRepository.insertOrUpdate(scheduleHisDTO);
  }
}
