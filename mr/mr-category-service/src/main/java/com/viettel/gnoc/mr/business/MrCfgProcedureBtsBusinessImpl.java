package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.DEVICE_TYPE_MAP_MULTI_LANG;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureBtsExportDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.mr.repository.MrCfgProcedureBtsRepository;
import com.viettel.gnoc.mr.repository.MrDeviceBtsRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Slf4j
public class MrCfgProcedureBtsBusinessImpl implements MrCfgProcedureBtsBusiness {

  @Autowired
  MrCfgProcedureBtsRepository mrCfgProcedureBtsRepository;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  MrDeviceBtsRepository mrDeviceBtsRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  private List<ItemDataCRInside> lstCountry;
  private Map<String, String> mapErrorResult = new HashMap<>();
  private List<ErrorInfo> cellErrorList = new ArrayList<>();
  private String pathTemplate = "templates" + File.separator + "maintenance" + File.separator;

  @Override
  public Datatable onSearch(MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO) {
    Datatable datatable = mrCfgProcedureBtsRepository.onSearch(mrCfgProcedureBtsDTO);
    if (datatable != null) {
      List<MrCfgProcedureBtsDTO> data = (List<MrCfgProcedureBtsDTO>) datatable.getData();
      if (data != null) {
        processDataSearch(data);
      }
      datatable.setData(data);
    }
    return datatable;
  }

  @Override
  public File exportSearchData(MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO) throws Exception {
    List<MrCfgProcedureBtsExportDTO> lstData = mrCfgProcedureBtsRepository
        .onSearchExport(mrCfgProcedureBtsDTO);
    if (lstData != null) {
      for (MrCfgProcedureBtsExportDTO resultDTO : lstData) {
        resultDTO.setDeviceTypeName(I18n.getLanguage(
            Constants.DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName().get(resultDTO.getDeviceType())));
      }
    }
    String[] header = new String[]{
        "marketName", "deviceTypeName", "materialTypeName", "cycle", "maintenanceHour",
        "supplierCode", "genMrBefore", "mrTime"};
    String[] align = new String[]{
        "LEFT", "LEFT", "LEFT", "RIGHT", "LEFT", "LEFT", "RIGHT", "RIGHT"
    };

    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header, align);
    return exportFileEx(lstData, lstHeaderSheet, "");
  }

  @Override
  public ResultInSideDto insertOrUpdateMrCfgProcedureBts(MrCfgProcedureBtsDTO dto) {
    List lstCheckDup = mrCfgProcedureBtsRepository.checkDupp(dto.toCheckDupDTO());
    if (lstCheckDup != null && lstCheckDup.size() > 0) {
      return new ResultInSideDto(dto.getCfgProcedureBtsId(), RESULT.ERROR,
          I18n.getLanguage("cfgProcedureBTS.duplicated"));
    }
    return mrCfgProcedureBtsRepository.insertOrUpdateMrCfgProcedureBts(dto);
  }

  @Override
  public ResultInSideDto deleteById(Long id) {
    return mrCfgProcedureBtsRepository.deleteById(id);
  }

  @Override
  public MrCfgProcedureBtsDTO findById(Long id) {
    return mrCfgProcedureBtsRepository.findById(id);
  }

  private File exportFileEx(List<MrCfgProcedureBtsExportDTO> lstData,
      List<ConfigHeaderExport> lstHeaderSheet, String code) throws Exception {
    String fileNameOut = "EXPORT_CONFIG_PROCEDURE_BTS_";
    ConfigFileExport configfileExport;
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet;
    String rootPath = tempFolder + File.separator;

    configfileExport = new ConfigFileExport(
        lstData
        , "CfgTime"
        , I18n.getLanguage("cfgProcedureBTS.list.grid")
        , ""
        , 7
        , 4
        , lstHeaderSheet.size()
        , false
        , "language.cfgProcedureBTS.list.grid"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , null
        , null
        , null
        , null
    );
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("cfgProcedureBTS.list.grid.stt"),
        "HEAD", "STRING");

    configfileExport.setIsAutoSize(true);
    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);

    File fileExport = CommonExport.exportFileWithTemplateXLSX(
        "templates" + File.separator + "TEMPLATE_EXPORT.xlsx"
        , fileNameOut
        , configfileExport
        , rootPath,
        null,
        null,
        I18n.getLanguage("cfgProcedureBTS.list.grid")

    );
    return fileExport;
  }

  private List<ConfigHeaderExport> renderHeaderSheet(String[] col, String[] align) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], align[i], false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private void processDataSearch(List<MrCfgProcedureBtsDTO> data) {
    for (MrCfgProcedureBtsDTO resultDTO : data) {
      resultDTO.setDeviceTypeName(I18n.getLanguage(
          Constants.DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName().get(resultDTO.getDeviceType())));
    }
  }

  @Override
  public File getFileTemplate() {
    Workbook workBook = null;
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String pathFolder =
        tempFolder + File.separator + FileUtils.createPathByDate(new Date()) + File.separator;
    try {
      workBook = processFileTemplate(ewu);
      ewu.saveToFileExcel(workBook, pathFolder, getFileTemplateName());
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
    return new File(pathFolder + getFileTemplateName());
  }

  @Override
  public ResultInSideDto importMrCfgProcedureBTS(MultipartFile multipartFile) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    mapErrorResult.clear();
    cellErrorList.clear();
    lstCountry = commonStreamServiceProxy.getListLocationByLevelCBBProxy(1L, null);
    Map<String, ItemDataCRInside> mapMarket = new HashMap<>();
    if (lstCountry != null) {
      for (ItemDataCRInside item : lstCountry) {
        mapMarket.put(String.valueOf(item.getValueStr()), item);
      }
    }
    try {
      if (multipartFile == null) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        resultInSideDto.setMessage(I18n.getLanguage("common.fileEmpty"));
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
                0, 8, 2);
          } else {
            lstData = CommonImport.getDataFromExcel(fileImp, 0, 8,
                0, 8, 2);
          }
          List<MrCfgProcedureBtsDTO> lstAddOrUpdate = new ArrayList<>();
          if (lstData != null && !lstData.isEmpty()) {
            for (int i = lstData.size() - 1; i >= 0; i--) {
              if (lstData.get(i)[0] == null && lstData.get(i)[1] == null
                  && lstData.get(i)[2] == null
                  && lstData.get(i)[3] == null && lstData.get(i)[4] == null
                  && lstData.get(i)[5] == null
                  && lstData.get(i)[6] == null && lstData.get(i)[7] == null
                  && lstData.get(i)[8] == null
              ) {
                lstData.remove(i);
              }
            }
            if (lstData.size() <= 1000) {
              boolean allTrue = true;
              int row = 8;
              for (Object[] obj : lstData) {
                MrCfgProcedureBtsExportDTO dto = new MrCfgProcedureBtsExportDTO();
                if (obj[1] != null) {
                  dto.setMarketCode(obj[1].toString().trim());
                } else {
                  dto.setMarketCode("");
                }
                if (obj[2] != null) {
                  dto.setDeviceType(obj[2].toString().trim());
                } else {
                  dto.setDeviceType("");
                }
                if (obj[3] != null) {
                  dto.setCycle(obj[3].toString().trim());
                } else {
                  dto.setCycle("");
                }
                if (obj[4] != null) {
                  dto.setGenMrBefore(obj[4].toString().trim());
                } else {
                  dto.setGenMrBefore("");
                }
                if (obj[5] != null) {
                  dto.setMrTime(obj[5].toString().trim());
                } else {
                  dto.setMrTime("");
                }

                if (obj[6] != null) { //tiennv bo sung MPD thi hien thi loai nhien lieu
                  dto.setMaterialType(obj[6].toString().trim());
                } else {
                  dto.setMaterialType("");
                }

                if (obj[7] != null && dto.getDeviceType().equalsIgnoreCase(
                    DEVICE_TYPE_MAP_MULTI_LANG.GENERATOR_CD)) { //tiennv bo sung MPD thi hien thi so gio chay
                  dto.setMaintenanceHour(obj[7].toString().trim());
                } else {
                  dto.setMaintenanceHour("");
                }

                if (obj[8] != null) {
                  dto.setSupplierCode(obj[8].toString().trim());
                } else {
                  dto.setSupplierCode("");
                }

                if (validateImportInfo(dto, mapMarket)) {
                  lstAddOrUpdate.add(dto.toDTO());
                  cellErrorList
                      .add(new ErrorInfo(row, I18n.getLanguage("common.import.validRecord")));
                } else {
                  allTrue = false;
                  cellErrorList.add(new ErrorInfo(row, getValidateResult(dto)));
                }
                row++;
              }

              if (allTrue) {
                if (!lstAddOrUpdate.isEmpty()) {
                  String res = mrCfgProcedureBtsRepository
                      .insertListMrCfgProcedureBts(lstAddOrUpdate);
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
      msg = I18n.getLanguage("common.import.file.extend.invalid");
    }
    return msg;
  }

  private boolean validateFileData(String fileImportPathOut) {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_CFG_PROCEDURE_BTS_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_CFG_PROCEDURE_BTS_TEMPLATE_VI.xlsx";
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

  private boolean validateImportInfo(MrCfgProcedureBtsExportDTO dto,
      Map<String, ItemDataCRInside> mapCountry) {
    if (StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.marketCode") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else if (!mapCountry.containsKey(dto.getMarketCode())) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.marketCode") + " " + I18n
          .getLanguage("notify.invalid"));
      return false;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.deviceType") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else if (!Constants.DEVICE_TYPE_LST.contains(dto.getDeviceType())) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.deviceType") + " " + I18n
          .getLanguage("notify.invalid"));
      return false;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getCycle())) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.cycle") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else if (!Constants.CYCLE_LST.contains(dto.getCycle())) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.cycle") + " " + I18n
          .getLanguage("notify.invalid"));
      return false;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getGenMrBefore())) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.genMrBefore") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else if (!dto.getGenMrBefore().matches("\\d+")) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.genMrBefore.invalid"));
      return false;
    } else if (dto.getGenMrBefore().length() > 3) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.genMrBefore.overlength"));
      return false;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getMrTime())) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.mrTime") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else if (!dto.getMrTime().matches("\\d+")) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.mrTime.invalid"));
      return false;
    } else if (dto.getMrTime().length() > 3) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.mrTime.overlength"));
      return false;
    }

    if (dto.getDeviceType().equalsIgnoreCase(DEVICE_TYPE_MAP_MULTI_LANG.GENERATOR_CD)) {
      if (StringUtils.isStringNullOrEmpty(dto.getMaterialType())) {
        setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.materialType") + " " + I18n
            .getLanguage("common.notnull"));
        return false;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getMaintenanceHour())) {
        setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.maintenanceHour") + " " + I18n
            .getLanguage("common.notnull"));
        return false;
      } else if (!dto.getMaintenanceHour().matches("\\d+")) {
        setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.maintenanceHour") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
    } else {
      if (StringUtils.isNotNullOrEmpty(dto.getMaterialType())) {
        setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.materialType") + " " + I18n
            .getLanguage("cfgProcedureBTS.isNotRequired"));
        return false;
      }
    }

    if (StringUtils.isStringNullOrEmpty(dto.getSupplierCode())) {
      if (Constants.MARKET_CODE.HAITI.equals(dto.getMarketCode())) {
        setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.list.grid.supplierCode") + " " + I18n
            .getLanguage("common.notnull"));
        return false;
      }
    } else {
      boolean isExists = false;
      List<MrDeviceBtsDTO> lstSupplierByDeviceType = mrDeviceBtsRepository
          .getListSupplierBtsByDeviceType(dto.getDeviceType(), dto.getMarketCode());
      for (MrDeviceBtsDTO mrDeviceBtsDTO : lstSupplierByDeviceType) {
        if (mrDeviceBtsDTO.getProducer() != null && mrDeviceBtsDTO.getProducer()
            .equals(dto.getSupplierCode())) {
          isExists = true;
          break;
        }
      }
      if (!isExists) {
        setMapResult(dto, I18n.getLanguage("import.supplier.notexist"));
        return false;
      }
    }

    List<MrCfgProcedureBtsDTO> lst = mrCfgProcedureBtsRepository
        .checkDupp(dto.toDTO().toCheckDupDTO());
    if (lst != null && lst.size() > 0) {
      setMapResult(dto, I18n.getLanguage("cfgProcedureBTS.duplicated"));
      return false;
    }
    return true;
  }

  private void setMapResult(MrCfgProcedureBtsExportDTO dto, String msg) {
    mapErrorResult.put(dto.getCfgProcedureBtsId(), msg);
  }

  private String getValidateResult(MrCfgProcedureBtsExportDTO dto) {
    return mapErrorResult.get(dto.getCfgProcedureBtsId());
  }

  public Workbook processFileTemplate(ExcelWriterUtils ewu) throws Exception {
    Workbook workBook;
    lstCountry = commonStreamServiceProxy.getListLocationByLevelCBBProxy(1L, null);
    String pathFileImport = pathTemplate + getFileTemplateName();
    workBook = ewu.readFileExcelFromTemplate(pathFileImport);
    Sheet sheetTwo = workBook.getSheetAt(1);
//      CellStyle cellSt1 = null;
    int k = 4;
    int l = 1;
    CellStyle cellSt1 = null;
    for (ItemDataCRInside rowS2 : lstCountry) {
      if (rowS2 == null) {
        break;
      }
      ewu.createCell(sheetTwo, 0, k, String.valueOf(l++), cellSt1);
      ewu.createCell(sheetTwo, 1, k,
          rowS2.getValueStr() == null ? "" : String.valueOf(rowS2.getValueStr()).trim(), cellSt1);
      ewu.createCell(sheetTwo, 2, k,
          rowS2.getDisplayStr() == null ? "" : rowS2.getDisplayStr().trim(), cellSt1);
      k++;
    }

    // Loại thiết bị
    Sheet sheetThree = workBook.getSheetAt(2);
    k = 4;
    l = 1;
    CellStyle cellSt2 = null;
    for (String deviceType : Constants.DEVICE_TYPE_LST) {
      ewu.createCell(sheetThree, 0, k, String.valueOf(l++), cellSt2);
      ewu.createCell(sheetThree, 1, k, deviceType, cellSt2);
      ewu.createCell(sheetThree, 2, k,
          I18n.getLanguage(DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName().get(deviceType)), cellSt2);
      k++;
    }

    // Chu kỳ
    Sheet sheetFive1 = workBook.getSheetAt(3);
    k = 4;
    l = 1;
    CellStyle cellSt5 = null;
    for (String cycle : Constants.CYCLE_LST) {
      ewu.createCell(sheetFive1, 0, k, String.valueOf(l), cellSt5);
      ewu.createCell(sheetFive1, 1, k, cycle, cellSt5);
      k++;
      l++;
    }

    //Tao tieu de
    Font xssFontTitle = workBook.createFont();
    xssFontTitle.setFontName("Times New Roman");
    xssFontTitle.setFontHeightInPoints((short) 22);
    xssFontTitle.setColor(IndexedColors.BLACK.index);
    xssFontTitle.setBold(true);

    CellStyle styleTitle = workBook.createCellStyle();
    styleTitle.setAlignment(HorizontalAlignment.CENTER);
    styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    styleTitle.setFont(xssFontTitle);

    Font xSSFFontHeader = workBook.createFont();
    xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    xSSFFontHeader.setColor(IndexedColors.BLUE.index);
    xSSFFontHeader.setBold(true);

    CellStyle cellStyleHeader = workBook.createCellStyle();
    cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleHeader.setBorderLeft(BorderStyle.THIN);
    cellStyleHeader.setBorderBottom(BorderStyle.THIN);
    cellStyleHeader.setBorderRight(BorderStyle.THIN);
    cellStyleHeader.setBorderTop(BorderStyle.THIN);
    cellStyleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
    cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyleHeader.setWrapText(true);
    cellStyleHeader.setFont(xSSFFontHeader);

    // Loại nhiên liệu/loại gas
    //phai tao 2 sheet trong code. vi tao trong template se bi overload data read excel
    Sheet sheetFive = workBook.createSheet(I18n.getLanguage("mrMaterial.list.grid.sheetName"));
    k = 4;
    l = 1;
    CellStyle cellSt3 = null;
    ewu.createCell(sheetFive, 0, k - 1, I18n.getLanguage("common.list.grid.stt"), cellStyleHeader);
    ewu.createCell(sheetFive, 1, k - 1, I18n.getLanguage("mrMaterial.list.grid.fuelTypeCode"),
        cellStyleHeader);
    ewu.createCell(sheetFive, 2, k - 1, I18n.getLanguage("mrMaterial.list.grid.fuelTypeName"),
        cellStyleHeader);
    ewu.createCell(sheetFive, 0, 1, I18n.getLanguage("mrMaterial.list.grid.title"), styleTitle);
    for (int i = 1; i <= 2; i++) {
      sheetFive.autoSizeColumn(i);
    }
    for (String fuelTypeCd : Constants.FUEL_TYPE_LST) {
      ewu.createCell(sheetFive, 0, k, String.valueOf(l), cellSt3);
      ewu.createCell(sheetFive, 1, k, fuelTypeCd, cellSt3);
      ewu.createCell(sheetFive, 2, k,
          I18n.getLanguage(Constants.FUEL_TYPE_MAP_MULTI_LANG.get(fuelTypeCd)), cellSt3);
      k++;
      l++;
    }
    List<MrDeviceBtsDTO> lstFuelRefresher = mrDeviceBtsRepository.getListfuelTypeByDeviceType(
        DEVICE_TYPE_MAP_MULTI_LANG.REFRESHER_CD, "");
    if (lstFuelRefresher != null) {
      for (MrDeviceBtsDTO dto : lstFuelRefresher) {
        ewu.createCell(sheetFive, 0, k, String.valueOf(l), cellSt3);
        ewu.createCell(sheetFive, 1, k, dto.getFuelType() == null ? "" : dto.getFuelType().trim(),
            cellSt3);
        ewu.createCell(sheetFive, 2, k, dto.getFuelType() == null ? "" : dto.getFuelType().trim(),
            cellSt3);
        k++;
        l++;
      }
    }

    //Hang san xuat
    Sheet sheetSix = workBook.createSheet(I18n.getLanguage("supplierCode.sheetName"));
    k = 4;
    l = 1;
    CellStyle cellSt4 = null;
    ewu.createCell(sheetSix, 0, k - 1, I18n.getLanguage("common.list.grid.stt"), cellStyleHeader);
    ewu.createCell(sheetSix, 1, k - 1, I18n.getLanguage("supplierCode.list.grid.supplier"),
        cellStyleHeader);
    ewu.createCell(sheetSix, 0, 1, I18n.getLanguage("supplierCode.list.grid.title"), styleTitle);
    sheetSix.autoSizeColumn(1);
    List<MrDeviceBtsDTO> lstSupplier = mrDeviceBtsRepository.getListSupplierBtsByDeviceType("", "");
    if (lstSupplier != null) {
      for (MrDeviceBtsDTO dto : lstSupplier) {
        if (dto == null) {
          break;
        }
        ewu.createCell(sheetSix, 0, k, String.valueOf(l), cellSt4);
        ewu.createCell(sheetSix, 1, k, dto.getProducer() == null ? "" : dto.getProducer().trim(),
            cellSt4);
        k++;
        l++;
      }
    }
    return workBook;
  }

  public void exportFileImport(List<Object[]> lstData, String fileTempPath,
      List<ErrorInfo> lstError) {
    try {
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      Workbook workBook = processFileTemplate(ewu);
      Sheet sheetOne = workBook.getSheetAt(0);
      //insert data from line i + 1
      int i = 8;
      CellStyle cellStResult = sheetOne.getRow(7).getCell(0).getCellStyle();
      Row row = sheetOne.getRow(7);
      Cell cell = row.createCell(9);
      cell.setCellStyle(cellStResult);
      cell.setCellValue(I18n.getLanguage("common.importError"));
      for (Object[] c_row : lstData) {
        if (c_row[8] == null && c_row[1] == null && c_row[2] == null && c_row[3] == null
            && c_row[4] == null && c_row[5] == null && c_row[6] == null && c_row[7] == null) {
          break;
        }
        for (int j = 0; j < 9; j++) {
          ewu.createCell(sheetOne, j, i, c_row[j] == null ? "" : c_row[j].toString().trim(), null);
        }
        i++;
      }

      for (ErrorInfo err : lstError) {
        row = sheetOne.getRow(err.getRow());
        cell = row.createCell(9);
        cell.setCellValue(err.getMsg());
      }
      sheetOne.autoSizeColumn(9, true);
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

  private String getFileTemplateName() {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_CFG_PROCEDURE_BTS_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_CFG_PROCEDURE_BTS_TEMPLATE_VI.xlsx";
    }
    return fileTemplateName;
  }
}
