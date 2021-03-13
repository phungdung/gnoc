package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.mr.repository.MrDeviceCDRepository;
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
public class MrDeviceCDBusinessImpl implements MrDeviceCDBusiness {

  @Autowired
  MrDeviceCDRepository mrDeviceCDRepository;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  UserRepository userRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  private List<MrDeviceCDDTO> lstPutMapImport;
  private List<ItemDataCRInside> lstCountry;
  private Map<String, String> mapErrorResult = new HashMap<>();
  private List<ErrorInfo> cellErrorList = new ArrayList<>();

  @Override
  public List<MrDeviceCDDTO> getComboboxDeviceType() {
    return mrDeviceCDRepository.getComboboxDeviceType();
  }

  @Override
  public List<MrDeviceCDDTO> getComboboxStationCode() {
    return mrDeviceCDRepository.getComboboxStationCode();
  }

  @Override
  public Datatable onSearch(MrDeviceCDDTO mrDeviceCDDTO) {
    if ("statusDisplay".equalsIgnoreCase(mrDeviceCDDTO.getSortName())) {
      mrDeviceCDDTO.setSortName("status");
    }
    modifiedState(mrDeviceCDDTO);
    Datatable datatable = mrDeviceCDRepository.onSearch(mrDeviceCDDTO);
    if (datatable != null && datatable.getData() != null) {
      List<MrDeviceCDDTO> data = (List<MrDeviceCDDTO>) datatable.getData();
      for (MrDeviceCDDTO dto : data) {
        if ("1".equals(dto.getStatus())) {
          dto.setStatusDisplay(I18n.getLanguage("mrDeviceCD.statusValue1"));
        } else {
          dto.setStatusDisplay(I18n.getLanguage("mrDeviceCD.statusValue2"));
        }
      }
      datatable.setData(data);
    }
    return datatable;
  }

  @Override
  public File exportSearchData(MrDeviceCDDTO mrDeviceCDDTO) throws Exception {
    lstCountry = commonStreamServiceProxy.getListLocationByLevelCBBProxy(1L, null);
    modifiedState(mrDeviceCDDTO);
    List<MrDeviceCDDTO> data = mrDeviceCDRepository.exportSearchData(mrDeviceCDDTO);
    for (MrDeviceCDDTO dto : data) {
      if ("1".equals(dto.getStatus())) {
        dto.setStatusDisplay(I18n.getLanguage("mrDeviceCD.statusValue1"));
      } else {
        dto.setStatusDisplay(I18n.getLanguage("mrDeviceCD.statusValue2"));
      }
    }
    String[] header = new String[]{"deviceCdId", "marketCode", "marketName", "deviceName",
        "deviceType", "stationCode", "userMrHard", "statusDisplay"};
    String[] align = new String[]{"CENTER", "CENTER", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT",
        "LEFT"};

    return exportFileEx(data, renderHeaderSheet(align, header), "EXPORT");
  }

  @Override
  public ResultInSideDto importMrDeviceCD(MultipartFile multipartFile) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    mapErrorResult.clear();
    cellErrorList.clear();
    lstCountry = commonStreamServiceProxy.getListLocationByLevelCBBProxy(1L, null);
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
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }

        String validate = validateFileImport(filePath);
        if (StringUtils.isStringNullOrEmpty(validate)) {
          //TODO
          Map<String, MrDeviceCDDTO> mapCurrentMrDeviceCD = new HashMap<>();
          File fileImp = new File(filePath);

          List<Object[]> lstData;
          if (filePath.contains("xlsx")) {
            lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 8,
                0, 8, 5);
          } else {
            lstData = CommonImport.getDataFromExcel(fileImp, 0, 8,
                0, 8, 5);
          }
          List<MrDeviceCDDTO> lstAddOrUpdate = new ArrayList<>();
          lstPutMapImport = mrDeviceCDRepository
              .getListMrDeviceCDDTO(new MrDeviceCDDTO(), 0, 0, "", "");
          for (MrDeviceCDDTO dto : lstPutMapImport) {
            mapCurrentMrDeviceCD.put(
                dto.getMarketCode() + "_" + dto.getDeviceName() + "_" + dto.getDeviceType() + "_"
                    + dto.getStationCode(), dto);
          }
          if (lstData != null && lstData.size() > 0) {
            int row = 8;
            boolean allTrue = true;
            for (Object[] obj : lstData) {
              MrDeviceCDDTO dto = new MrDeviceCDDTO();
              if (obj[1] != null) {
                dto.setDeviceCdId(obj[1].toString().trim());
              } else {
                dto.setDeviceCdId("");
              }
              if (obj[2] != null) {
                dto.setMarketCode(obj[2].toString().trim());
              } else {
                dto.setMarketCode(null);
              }
              if (obj[4] != null) {
                dto.setDeviceName(obj[4].toString().trim());
              } else {
                dto.setDeviceName(null);
              }
              if (obj[5] != null) {
                dto.setDeviceType(obj[5].toString().trim());
              } else {
                dto.setDeviceType(null);
              }
              if (obj[6] != null) {
                dto.setStationCode(obj[6].toString().trim());
              } else {
                dto.setStationCode(null);
              }
              if (obj[7] != null) {
                dto.setUserMrHard(obj[7].toString().trim());
              } else {
                dto.setUserMrHard(null);
              }
              if (obj[8] != null) {
                if (I18n.getLanguage("mrDeviceCD.statusValue1").equals(obj[8].toString().trim())) {
                  dto.setStatus("1");
                } else if (I18n.getLanguage("mrDeviceCD.statusValue2")
                    .equals(obj[8].toString().trim())) {
                  dto.setStatus("0");
                } else {
                  dto.setStatus(obj[8].toString().trim());
                }
              } else {
                dto.setStatus(null);
              }
              if (validateImportInfo(dto)) {
                if (!"281".equals(dto.getMarketCode())) {
                  if (StringUtils.isStringNullOrEmpty(dto.getDeviceCdId())) {
                    if (mapCurrentMrDeviceCD.get(
                        dto.getMarketCode() + "_" + dto.getDeviceName() + "_" + dto.getDeviceType()
                            + "_" + dto.getStationCode()) != null) {
                      String id = mapCurrentMrDeviceCD.get(
                          dto.getMarketCode() + "_" + dto.getDeviceName() + "_" + dto
                              .getDeviceType() + "_" + dto.getStationCode()).getDeviceCdId();
                      dto.setDeviceCdId(id);
                      for (MrDeviceCDDTO tmp : lstPutMapImport) {
                        if (tmp.getDeviceCdId().equals(dto.getDeviceCdId())) {
//                                                tmp.setDeviceName(dto.getDeviceName());
//                                                tmp.setDeviceType(dto.getDeviceType());
//                                                tmp.setStationCode(dto.getStationCode());
                          tmp.setUserMrHard(dto.getUserMrHard());
                          tmp.setStatus(dto.getStatus());
                          dto = tmp;
                          break;
                        }
                      }
                      if (lstAddOrUpdate.size() > 0) {
                        boolean check = true;
                        for (MrDeviceCDDTO item : lstAddOrUpdate) {
                          if (item.getDeviceCdId().equals(dto.getDeviceCdId())) {
                            allTrue = false;
                            check = false;
                            cellErrorList.add(new ErrorInfo(row,
                                I18n.getLanguage("mrDeviceCD.import.duplicate")));
                            break;
                          }
                        }
                        if (check) {
                          lstAddOrUpdate.add(dto);
                          cellErrorList.add(new ErrorInfo(row,
                              I18n.getLanguage("mrDeviceCD.import.validRecord")));
                        }
                      } else {
                        lstAddOrUpdate.add(dto);
                        cellErrorList.add(
                            new ErrorInfo(row, I18n.getLanguage("mrDeviceCD.import.validRecord")));
                      }
                    } else {
                      mapCurrentMrDeviceCD.put(
                          dto.getMarketCode() + "_" + dto.getDeviceName() + "_" + dto
                              .getDeviceType() + "_" + dto.getStationCode(), dto);
                      lstAddOrUpdate.add(dto);
                      cellErrorList.add(
                          new ErrorInfo(row, I18n.getLanguage("mrDeviceCD.import.validRecord")));
                    }
                  } else {
                    boolean check = true;
                    MrDeviceCDDTO dtoTmp = new MrDeviceCDDTO();
                    for (MrDeviceCDDTO tmp : lstPutMapImport) {
                      if (tmp.getDeviceCdId().equals(dto.getDeviceCdId())) {
                        dtoTmp = tmp;
                        check = false;
                        break;
                      }
                    }
                    if (check) {
                      allTrue = false;
                      cellErrorList.add(new ErrorInfo(row,
                          I18n.getLanguage("mrDeviceCD.list.deviceCdId") + " " + I18n
                              .getLanguage("notify.invalid")));
                    }
                    if (dtoTmp.getMarketCode().equals(dto.getMarketCode())) {
                      lstAddOrUpdate.add(dto);
                      cellErrorList.add(
                          new ErrorInfo(row, I18n.getLanguage("mrDeviceCD.import.validRecord")));
                    } else {
                      allTrue = false;
                      cellErrorList.add(new ErrorInfo(row,
                          I18n.getLanguage("mrDeviceCD.import.invalidCountryWithID")));
                    }
                  }
                }

                if ("281".equals(dto.getMarketCode())) {
                  MrDeviceCDDTO dtoVN = new MrDeviceCDDTO();
                  for (MrDeviceCDDTO tmp : lstPutMapImport) {
                    if (tmp.getDeviceCdId().equals(dto.getDeviceCdId())) {
                      tmp.setUserMrHard(dto.getUserMrHard());
                      tmp.setStatus(dto.getStatus());
                      dtoVN = tmp;
                      break;
                    }
                  }
                  if (dtoVN.getMarketCode().equals(dto.getMarketCode())) {
                    boolean check = true;
                    if (lstAddOrUpdate.size() > 0) {
                      for (MrDeviceCDDTO item : lstAddOrUpdate) {
                        if (item.getDeviceCdId().equals(dtoVN.getDeviceCdId())) {
                          allTrue = false;
                          check = false;
                          cellErrorList.add(
                              new ErrorInfo(row, I18n.getLanguage("mrDeviceCD.import.duplicate")));
                          break;
                        }
                      }
                      if (check) {
                        lstAddOrUpdate.add(dtoVN);
                        cellErrorList.add(
                            new ErrorInfo(row, I18n.getLanguage("mrDeviceCD.import.validRecord")));
                      }
                    } else {
                      lstAddOrUpdate.add(dtoVN);
                      cellErrorList.add(
                          new ErrorInfo(row, I18n.getLanguage("mrDeviceCD.import.validRecord")));
                    }
                  } else {
                    allTrue = false;
                    cellErrorList.add(new ErrorInfo(row,
                        I18n.getLanguage("mrDeviceCD.import.invalidCountryWithID")));
                  }
                }
              } else {
                allTrue = false;
                cellErrorList.add(new ErrorInfo(row, getValidateResult(obj)));
              }
              row++;
            }

            if (allTrue) {
              if (!lstAddOrUpdate.isEmpty()) {
                Map<String, ItemDataCRInside> mapMarket = new HashMap<>();
                for (ItemDataCRInside item : lstCountry) {
                  mapMarket.put(String.valueOf(item.getValueStr()), item);
                }
                for (MrDeviceCDDTO dtoInsert : lstAddOrUpdate) {
                  dtoInsert.setMarketName(mapMarket.get(dtoInsert.getMarketCode()).getDisplayStr());
                  dtoInsert.setUpdateUser(userToken.getUserName());
                  dtoInsert.setUpdateDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                }
                String res = mrDeviceCDRepository.insertOrUpdateListMrDeviceCD(lstAddOrUpdate);
                if (res != null && "SUCCESS".equals(res)) {
                  //neu insert thanh cong thi chuyen data cot ket qua : Ban ghi hop le -> Thanh cong
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
                  I18n.getLanguage("softDeviceView.data.import.error"));
            }
            resultInSideDto.setFilePath(filePath);
          } else {
            resultInSideDto = new ResultInSideDto(null, RESULT.NODATA,
                I18n.getLanguage("common.searh.nodata"));
          }

          exportFileImport(lstData, filePath, cellErrorList);
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
  public File getFileTemplate() {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_MR_DEVICE_CD_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_MR_DEVICE_CD_TEMPLATE_VN.xlsx";
    }
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Workbook workBook = null;
    String pathFolder =
        tempFolder + File.separator + FileUtils.createPathByDate(new Date()) + File.separator;
    try {
      workBook = ewu.readFileExcelFromTemplate(getFilePathTemplate());
      Sheet sheetTwo = workBook.getSheetAt(1);
      lstCountry = commonStreamServiceProxy.getListLocationByLevelCBBProxy(1L, null);
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
      ewu.saveToFileExcel(workBook, pathFolder, fileTemplateName);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        workBook.close();
      } catch (IOException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return new File(pathFolder + fileTemplateName);
  }

  private void modifiedState(MrDeviceCDDTO mrDeviceCDDTO) {
    String statusDisplay1 = I18n.getLanguage("mrDeviceCD.statusValue1");
    String statusDisplay2 = I18n.getLanguage("mrDeviceCD.statusValue2");
    if (StringUtils.isNotNullOrEmpty(mrDeviceCDDTO.getStatusDisplay())) {
      if (statusDisplay1.toLowerCase()
          .contains(mrDeviceCDDTO.getStatusDisplay().trim().toLowerCase())) {
        mrDeviceCDDTO.setStatus("1");
      } else if (statusDisplay2.toLowerCase()
          .contains(mrDeviceCDDTO.getStatusDisplay().trim().toLowerCase())) {
        mrDeviceCDDTO.setStatus("0");
      } else {
        mrDeviceCDDTO.setStatus("-1");//them dieu kien fail neu ng dung nhap sai
      }
    }
  }

  private File exportFileEx(List<MrDeviceCDDTO> lstData, List<ConfigHeaderExport> lstHeaderSheet,
      String code) throws Exception {
    String sheetName;
    String fileNameOut = "MrDeviceCDReport";
    ConfigFileExport configfileExport;
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet;
    String rootPath = tempFolder + File.separator;
    if (Constants.RESULT_IMPORT.equals(code)) {
      sheetName = "MrDeviceCD";
      fileNameOut = "MrDeviceCDImport";
      configfileExport = new ConfigFileExport(
          lstData
          , sheetName
          , null
          , null
          , 5
          , 4
          , lstHeaderSheet.size()
          , true
          , "language.mrDeviceCD.list"
          , lstHeaderSheet
          , fieldSplit
          , ""
          , null
          , null
          , null
          , null
      );
      cellSheet = new CellConfigExport(5, 0, 0, 0, I18n.getLanguage("mrDeviceCD.list.stt"),
          "HEAD", "STRING");
    } else {
      configfileExport = new ConfigFileExport(
          lstData
          , null
          , I18n.getLanguage("mrDeviceCD.report")
          , null
          , 7
          , 4
          , lstHeaderSheet.size()
          , false
          , "language.mrDeviceCD.list"
          , lstHeaderSheet
          , fieldSplit
          , ""
          , null
          , null
          , null
          , null
      );
      cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("mrDeviceCD.list.stt"),
          "HEAD", "STRING");
    }

    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    List<Integer> lsColumnHidden = new ArrayList<>();
    lsColumnHidden.add(1);

    String[] header = new String[]{"valueStr", "displayStr"};
    String[] align = new String[]{"LEFT", "LEFT"};
    ConfigFileExport configSheet2 = new ConfigFileExport
        (lstCountry
            , null
            , null
            , null
            , 4
            , 0
            , 0
            , false
            , ""
            , renderHeaderSheet(align, header)
            , null
            , ""
            , null
            , null
            , null
            , null
        );

    File fileExport = CommonExport.exportFileWithTemplateXLSX(
        getFilePathTemplate()
        , fileNameOut
        , configfileExport
        , rootPath,
        lsColumnHidden,
        configSheet2,
        I18n.getLanguage("mrDeviceCD.report")

    );
    return fileExport;
  }

  private List<ConfigHeaderExport> renderHeaderSheet(String[] align, String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], align[i], false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  public String validateFileImport(String path) {
    if (path.contains(".xls")) {
//      if (path.contains(".xlsx")) {
//        return I18n.getLanguage("mrSchedule.import.file.invalid");
//      } else {
      boolean check = validateFileData(path);
      if (!check) {
        return I18n.getLanguage("mrChecklist.import.errorTemplate");
      }
//      }
    } else {
      return I18n.getLanguage("mrSchedule.import.file.extend.invalid");
    }
    return "";
  }

  private boolean validateFileData(String fileImportPathOut) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Workbook workBook = null;
    Workbook workBook2 = null;
    boolean resultOut = true;
    try {
      workBook = ewu.readFileExcelFromTemplate(getFilePathTemplate());
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

  private boolean validateImportInfo(MrDeviceCDDTO dto) {
    List<String> lstId = new ArrayList<>();
    for (MrDeviceCDDTO dtoTmp : lstPutMapImport) {
      lstId.add(dtoTmp.getDeviceCdId());
    }
    if (StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
      setMapResult(dto, I18n.getLanguage("mrDeviceCD.list.marketCode") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } //neu la quoc gia VN thi chi dc update
    //bat buoc co id ban ghi
    //kiem tra id ban ghi co trong list hien tai hay ko
    if ("281".equals(dto.getMarketCode())) {
      if (StringUtils.isStringNullOrEmpty(dto.getDeviceCdId())) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.list.deviceCdId") + " " + I18n
            .getLanguage("common.notnull"));
        return false;
      } else if (!lstId.contains(dto.getDeviceCdId())) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.list.deviceCdId") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getUserMrHard())) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.list.userMrHard") + " " + I18n
            .getLanguage("common.notnull"));
        return false;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getStatus())) {
        setMapResult(dto,
            I18n.getLanguage("mrDeviceCD.list.status") + " " + I18n.getLanguage("notify.invalid"));
        return false;
      }
    } else {

      //bo sung bat buoc nhap khi update
      if (StringUtils.isStringNullOrEmpty(dto.getUserMrHard())) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.list.userMrHard") + " " + I18n
            .getLanguage("common.notnull"));
        return false;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getStatus())) {
        setMapResult(dto,
            I18n.getLanguage("mrDeviceCD.list.status") + " " + I18n.getLanguage("notify.invalid"));
        return false;
      }

      //neu la quoc gia khac thi duoc update va insert, bat buoc nhap them ten tb, loai tb, ma tong tram
      List<String> lstStrCountry = new ArrayList<>();
      for (ItemDataCRInside dtoCountry : lstCountry) {
        lstStrCountry.add(String.valueOf(dtoCountry.getValueStr()));
      }
      if (!lstStrCountry.contains(dto.getMarketCode())) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.list.marketCode") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getDeviceName())) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.list.deviceName") + " " + I18n
            .getLanguage("common.notnull"));
        return false;
      } else if (dto.getDeviceName().trim().length() > 100) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.import.deviceName.over100"));
        return false;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.list.deviceType") + " " + I18n
            .getLanguage("common.notnull"));
        return false;
      } else if (dto.getDeviceType().trim().length() > 100) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.import.deviceType.over100"));
        return false;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getStationCode())) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.list.stationCode") + " " + I18n
            .getLanguage("common.notnull"));
        return false;
      } else if (dto.getStationCode().trim().length() > 100) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.import.stationCode.over100"));
        return false;
      }
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getUserMrHard())) {
      UsersEntity userTokenGNOC = userRepository.getUserByUserName(dto.getUserMrHard());
      if (userTokenGNOC == null || userTokenGNOC.getUserId() == null || !"1"
          .equals(String.valueOf(userTokenGNOC.getIsEnable()))) {
        setMapResult(dto, I18n.getLanguage("mrDeviceCD.list.userMrHard") + " " + I18n
            .getLanguage("common.userNotExist"));
        return false;
      }
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getStatus())) {
      if (!"1".equalsIgnoreCase(dto.getStatus()) && !"0".equalsIgnoreCase(dto.getStatus())) {
        setMapResult(dto,
            I18n.getLanguage("mrDeviceCD.list.status") + " " + I18n.getLanguage("notify.invalid"));
        return false;
      }
    }
    //namtn edit import
    return true;
  }

  private void setMapResult(MrDeviceCDDTO dto, String msg) {
    mapErrorResult.put(dto.getDeviceCdId(), msg);
  }

  private String getValidateResult(Object[] obj) {
    return mapErrorResult.get(obj[1] == null ? "" : obj[1].toString().trim());
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
      Cell cell = row.createCell(9);
      cell.setCellStyle(cellStResult);
      cell.setCellValue(I18n.getLanguage("mrChecklist.list.grid.importError"));

      for (Object[] c_row : lstData) {
        if (c_row[8] == null && c_row[1] == null && c_row[2] == null && c_row[3] == null
            && c_row[4] == null && c_row[5] == null && c_row[6] == null && c_row[7] == null) {
          break;
        }
        ewu.createCell(sheetOne, 0, i, c_row[0] == null ? "" : c_row[0].toString().trim(), null);
        ewu.createCell(sheetOne, 1, i, c_row[1] == null ? "" : c_row[1].toString().trim(), null);
        ewu.createCell(sheetOne, 2, i, c_row[2] == null ? "" : c_row[2].toString().trim(), null);
        ewu.createCell(sheetOne, 3, i, c_row[3] == null ? "" : c_row[3].toString().trim(), null);
        ewu.createCell(sheetOne, 4, i, c_row[4] == null ? "" : c_row[4].toString().trim(), null);
        ewu.createCell(sheetOne, 5, i, c_row[5] == null ? "" : c_row[5].toString().trim(), null);
        ewu.createCell(sheetOne, 6, i, c_row[6] == null ? "" : c_row[6].toString().trim(), null);
        ewu.createCell(sheetOne, 7, i, c_row[7] == null ? "" : c_row[7].toString().trim(), null);
        ewu.createCell(sheetOne, 8, i, c_row[8] == null ? "" : c_row[8].toString().trim(), null);
        i++;
      }

      for (ErrorInfo err : lstError) {
        row = sheetOne.getRow(err.getRow());
        cell = row.createCell(9);
        cell.setCellValue(err.getMsg());
      }

      Sheet sheetTwo = workBook.getSheetAt(1);
      //insert data from line i + 1
      int k = 4;
      int l = 1;
      CellStyle cellSt1 = null;
      for (ItemDataCRInside rowS2 : lstCountry) {
        if (rowS2 == null) {
          break;
        }
        ewu.createCell(sheetTwo, 0, k, String.valueOf(l), cellSt1);
        ewu.createCell(sheetTwo, 1, k,
            rowS2.getValueStr() == null ? "" : rowS2.getValueStr().toString(), cellSt1);
        ewu.createCell(sheetTwo, 2, k,
            rowS2.getDisplayStr() == null ? "" : rowS2.getDisplayStr().trim(), cellSt1);
        k++;
        l++;
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

  private String getFilePathTemplate() {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_MR_DEVICE_CD_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_MR_DEVICE_CD_TEMPLATE_VN.xlsx";
    }
    return "templates" + File.separator + "maintenance" + File.separator + fileTemplateName;
  }
}
