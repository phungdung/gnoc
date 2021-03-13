package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.MR_UCTT;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.mr.repository.MrChecklistBtsDetailRepository;
import com.viettel.gnoc.mr.repository.MrChecklistBtsRepository;
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
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MrChecklistBtsBusinessImpl implements MrChecklistBtsBusiness {

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  MrChecklistBtsRepository mrChecklistBtsRepository;

  @Autowired
  MrChecklistBtsDetailRepository mrChecklistBtsDetailRepository;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  MrDeviceBtsRepository mrDeviceBtsRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  private List<ItemDataCRInside> lstCountry;
  private List<MrChecklistsBtsDTO> lstDto;

  private final static String MR_CHECK_LIST_BTS_RESULT_IMPORT = "MR_CHECK_LIST_BTS_RESULT_IMPORT";

  Map<Long, String> mapMarket = new HashMap<>();
  Map<String, String> mapDeviceType = new HashMap<>();
  Map<String, String> mapMaterialType = new HashMap<>();
  Map<String, String> mapCycle = new HashMap<>();
  Map<String, String> mapImportDTO = new HashMap<>();

  private Map<String, String> mapErrorResult = new HashMap<>();
  private List<ErrorInfo> cellErrorList = new ArrayList<>();
  private String pathTemplate = "templates" + File.separator + "maintenance" + File.separator;

  private File fileExport;

  @Override
  public Datatable getListDataSearchWeb(MrChecklistsBtsDTO mrChecklistsBtsDTO) {
    log.debug("Request to getListDataSearchWeb: {}", mrChecklistsBtsDTO);
    Datatable datatable = mrChecklistBtsRepository.getListDataSearchWeb(mrChecklistsBtsDTO);
    if (datatable != null) {
      List<MrChecklistsBtsDTO> list = (List<MrChecklistsBtsDTO>) datatable.getData();
      if (list != null && list.size() > 0) {
        setMapMarket();
        setMapDeviceType();
        setMapMaterialType();
        for (MrChecklistsBtsDTO btsDTO : list) {
          setDetailValue(btsDTO);
        }
      }
    }
    return datatable;
  }

  @Override
  public ResultInSideDto insertMrChecklistBts(MrChecklistsBtsDTO mrChecklistsBtsDTO) {
    log.debug("Request to insertMrChecklistBts: {}", mrChecklistsBtsDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    mrChecklistsBtsDTO.setCreatedTime(new Date());
    mrChecklistsBtsDTO.setCreatedUser(userToken.getUserName());
    MrChecklistsBtsDTO dtoCheckDupp = mrChecklistBtsRepository
        .checkMrChecklistBtsExit(mrChecklistsBtsDTO);
    if (dtoCheckDupp != null && !String.valueOf(mrChecklistsBtsDTO.getChecklistId())
        .equals(String.valueOf(dtoCheckDupp.getChecklistId()))) {
      return new ResultInSideDto(null, RESULT.ERROR,
          I18n.getValidation("mrChecklistsBtsDTO.multiple.unique"));
    }
    Long checklistId = mrChecklistsBtsDTO.getChecklistId();
    if (checklistId == null) {
      resultInSideDto = mrChecklistBtsRepository.insertMrChecklistBts(mrChecklistsBtsDTO);
      checklistId = resultInSideDto.getId();
    }
    List<MrChecklistsBtsDetailDTO> listDetail = mrChecklistsBtsDTO.getListDetail();
    if (listDetail != null && listDetail.size() > 0) {
      for (MrChecklistsBtsDetailDTO detailDTO : listDetail) {
        detailDTO.setChecklistId(checklistId);
        resultInSideDto = mrChecklistBtsDetailRepository.insertMrChecklistBtsDetail(detailDTO);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateMrChecklistBts(MrChecklistsBtsDTO mrChecklistsBtsDTO) {
    log.debug("Request to updateMrChecklistBts: {}", mrChecklistsBtsDTO);
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto;
    mrChecklistsBtsDTO.setUpdatedUser(userToken.getUserName());
    mrChecklistsBtsDTO.setUpdatedTime(new Date());
    MrChecklistsBtsDTO dtoCheckDupp = mrChecklistBtsRepository
        .checkMrChecklistBtsExit(mrChecklistsBtsDTO);
    if (dtoCheckDupp != null && !String.valueOf(mrChecklistsBtsDTO.getChecklistId())
        .equals(String.valueOf(dtoCheckDupp.getChecklistId()))) {
      return new ResultInSideDto(null, RESULT.ERROR,
          I18n.getValidation("mrChecklistsBtsDTO.multiple.unique"));
    }
    resultInSideDto = mrChecklistBtsRepository.updateMrChecklistBts(mrChecklistsBtsDTO);
    Long checklistId = resultInSideDto.getId();
    List<MrChecklistsBtsDetailDTO> listDetailNew = mrChecklistsBtsDTO.getListDetail();
    List<MrChecklistsBtsDetailDTO> listDetailOld = mrChecklistBtsDetailRepository
        .getListDetailByChecklistId(checklistId);
    List<Long> listDelete = new ArrayList<>();
    if (listDetailOld != null && listDetailOld.size() > 0) {
      for (MrChecklistsBtsDetailDTO detailOldDTO : listDetailOld) {
        listDelete.add(detailOldDTO.getChecklistDetailId());
      }
    }
    if (listDetailNew != null && listDetailNew.size() > 0) {
      for (MrChecklistsBtsDetailDTO detailDTO : listDetailNew) {
        if (detailDTO.getChecklistDetailId() != null) {
          listDelete.remove(detailDTO.getChecklistDetailId());
          resultInSideDto = mrChecklistBtsDetailRepository.updateMrChecklistBtsDetail(detailDTO);
        } else {
          detailDTO.setChecklistId(checklistId);
          resultInSideDto = mrChecklistBtsDetailRepository.insertMrChecklistBtsDetail(detailDTO);
        }
      }
    }
    if (listDelete.size() > 0) {
      for (Long delId : listDelete) {
        resultInSideDto = mrChecklistBtsDetailRepository.deleteMrChecklistBtsDetail(delId);
      }
    }
    return resultInSideDto;
  }

  @Override
  public MrChecklistsBtsDTO findMrChecklistBtsByIdFromWeb(MrChecklistsBtsDTO mrChecklistsBtsDTO) {
    log.debug("Request to findMrChecklistBtsByIdFromWeb: {}", mrChecklistsBtsDTO);
    MrChecklistsBtsDTO searchDTO = mrChecklistBtsRepository
        .findMrChecklistBtsById(mrChecklistsBtsDTO.getChecklistId());
    if (searchDTO != null) {
      setMapMarket();
      setMapDeviceType();
      setMapMaterialType();
      setDetailValue(searchDTO);
      MrChecklistsBtsDetailDTO detailDTO = new MrChecklistsBtsDetailDTO();
      detailDTO.setChecklistId(searchDTO.getChecklistId());
      List<MrChecklistsBtsDetailDTO> lstDetail = mrChecklistBtsDetailRepository
          .getListDetail(detailDTO);
      Datatable datatable = new Datatable();
      datatable.setData(lstDetail);
      searchDTO.setDataDetail(datatable);
    }
    return searchDTO;
  }

  @Override
  public ResultInSideDto deleteMrChecklistBts(Long checklistId) {
    log.debug("Request to deleteMrChecklistBts: {}", checklistId);
    ResultInSideDto resultInSideDto;
    List<MrChecklistsBtsDetailDTO> listDetail = mrChecklistBtsDetailRepository
        .getListDetailByChecklistId(checklistId);
    if (listDetail != null && listDetail.size() > 0) {
      for (MrChecklistsBtsDetailDTO detailDTO : listDetail) {
        mrChecklistBtsDetailRepository.deleteMrChecklistBtsDetail(detailDTO.getChecklistDetailId());
      }
    }
    resultInSideDto = mrChecklistBtsRepository.deleteMrChecklistBts(checklistId);
    return resultInSideDto;
  }

  @Override
  public File exportDataMrChecklistBts(MrChecklistsBtsDTO mrChecklistsBtsDTO) throws Exception {
    log.debug("Request to exportDataMrChecklistBts: {}", mrChecklistsBtsDTO);
    List<MrChecklistsBtsDTO> list = mrChecklistBtsRepository
        .getListMrChecklistBtsExport(mrChecklistsBtsDTO);
    if (list != null && list.size() > 0) {
      setMapMarket();
      setMapDeviceType();
      setMapMaterialType();
      for (MrChecklistsBtsDTO btsDTO : list) {
        setDetailValue(btsDTO);
      }
    }
    String[] header = new String[]{"marketName", "deviceTypeName", "materialTypeName", "cycle",
        "supplierCode", "checklistDetailId", "content", "photoReqDisplay", "minPhoto", "maxPhoto",
        "captureGuide", "scoreChecklist", "isImportantName"};
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(date),
        "EXPORT_MR_CHECKLIST_BTS");
  }

  @Override
  public List<MrDeviceBtsDTO> getListSupplierBtsCombobox(MrDeviceBtsDTO mrDeviceBtsDTO) {
    return mrDeviceBtsRepository.getListSupplierBtsByDeviceType(mrDeviceBtsDTO.getDeviceType(),
        mrDeviceBtsDTO.getMarketCode());
  }

  //  Dunglv commit
  private String getValidateResult(Long index) {
    return mapErrorResult.get(index.toString());
  }


  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) throws IOException {
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
        resultInSideDto.setMessage(I18n.getLanguage("mrChecklistBTS.fileEmpty"));
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
          List<MrChecklistsBtsDTO> lstAddOrUpdate = new ArrayList<>();
          if (lstData != null && lstData.size() > 0) {
            for (int i = lstData.size() - 1; i >= 0; i--) {
              if (lstData.get(i)[0] == null && lstData.get(i)[1] == null
                  && lstData.get(i)[2] == null && lstData.get(i)[3] == null
                  && lstData.get(i)[4] == null && lstData.get(i)[5] == null
                  && lstData.get(i)[6] == null && lstData.get(i)[7] == null
                  && lstData.get(i)[8] == null && lstData.get(i)[9] == null
                  && lstData.get(i)[10] == null) {
                lstData.remove(i);
              }
            }
            if (lstData.size() <= 1000) {
              boolean allTrue = true;
              int row = 8;
              Map<String, MrChecklistsBtsDTO> mapCheck = new HashMap<>();
              for (Object[] obj : lstData) {
                MrChecklistsBtsDTO dto = new MrChecklistsBtsDTO();
                List<MrChecklistsBtsDetailDTO> listDetailTmp = new ArrayList<>();
                MrChecklistsBtsDetailDTO btsDetailDTO = new MrChecklistsBtsDetailDTO();
                if (obj[1] != null && obj[1] != "") {
                  dto.setMarketCode(obj[1].toString().trim());
                } else {
                  dto.setMarketName("");
                }
                if (obj[2] != null && obj[2] != "") {
                  dto.setDeviceType(obj[2].toString().trim());
                } else {
                  dto.setDeviceType("");
                }
                if (obj[3] != null && obj[3] != "") {
                  dto.setMaterialType(obj[3].toString().trim());
                } else {
                  dto.setMaterialType("");
                }
                if (obj[4] != null && obj[4] != "") {
                  dto.setCycleStr(obj[4].toString().trim());
                } else {
                  dto.setCycleStr(null);
                }
                if (obj[5] != null && obj[5] != "") {
                  dto.setSupplierCode(obj[5].toString().trim());
                } else {
                  dto.setSupplierCode("");
                }
//                if (obj[6] != null && obj[6] != "") {
//                  dto.setChecklistDetailId(Long.valueOf(obj[6].toString().trim()));
//                } else {
//                  dto.setChecklistDetailId(null);
//                }
                if (obj[6] != null && obj[6] != "") {
//                  dto.setListDetail(obj[6].toString().trim());
                  btsDetailDTO.setContent(obj[6].toString().trim());
                } else {
                  btsDetailDTO.setContent("");
                }
                if (obj[7] != null && obj[7] != "") {
                  if (I18n.getLanguage("mrChecklistBts.photoReq.yes")
                      .equals(obj[7].toString().trim())) {
                    btsDetailDTO.setPhotoReq(1L);
                  } else if (I18n.getLanguage("mrChecklistBts.photoReq.no")
                      .equals(obj[7].toString().trim())) {
                    btsDetailDTO.setPhotoReq(0L);
                  } else if (obj[7].toString().trim() != null) {
                    btsDetailDTO.setPhotoReq(2L);
                  }
                } else {
                  btsDetailDTO.setPhotoReq(0L);
                }
                if (obj[8] != null && obj[8] != "") {
                  btsDetailDTO.setMinPhoto(Long.valueOf(obj[8].toString().trim()));
                } else {
                  btsDetailDTO.setMinPhoto(null);
                }
                if (obj[9] != null && obj[9] != "") {
                  btsDetailDTO.setMaxPhoto(Long.valueOf(obj[9].toString().trim()));
                } else {
                  btsDetailDTO.setMaxPhoto(null);
                }
                if (obj[10] != null && obj[10] != "") {
                  btsDetailDTO.setCaptureGuide(obj[10].toString().trim());
                } else {
                  btsDetailDTO.setCaptureGuide(null);
                }
                if (obj[11] != null && obj[11] != "") {
                  btsDetailDTO.setScoreChecklist(Double.valueOf(obj[11].toString().trim()));
                } else {
                  btsDetailDTO.setScoreChecklist(null);
                }
                if (obj[12] != null && obj[12] != "") {
                  if (I18n.getLanguage("mrChecklistBts.photoReq.yes")
                      .equals(obj[12].toString().trim())) {
                    btsDetailDTO.setIsImportant(1L);
                  } else if (I18n.getLanguage("mrChecklistBts.photoReq.no")
                      .equals(obj[12].toString().trim())) {
                    btsDetailDTO.setIsImportant(0L);
                  } else {
                    btsDetailDTO.setIsImportant(2L);
                  }
                } else {
                  btsDetailDTO.setPhotoReq(null);
                }
                //set List
                listDetailTmp.add(btsDetailDTO);
                dto.setListDetail(listDetailTmp);

                if (validateImportInfo(Long.valueOf(row), dto, lstAddOrUpdate)) {
                  cellErrorList.add(new ErrorInfo(row, I18n.getLanguage("common.success1")));
                } else {
                  allTrue = false;
                  cellErrorList.add(new ErrorInfo(row, getValidateResult(Long.valueOf(row))));
                }
                lstAddOrUpdate.add(dto);
                String key = dto.getMaterialType() + "_" + dto.getCycle() + "_" + dto.getDeviceType() + "_" + dto.getMarketCode() + "_" + dto.getSupplierCode();
                if (!mapCheck.containsKey(key)) {
                  mapCheck.put(key, dto);
                } else {
                  mapCheck.get(key).getListDetail().addAll(listDetailTmp);
                }
                row++;
              }

              if (allTrue) {
                if (!mapCheck.isEmpty()) {
                  int k = 0;
                  List<MrChecklistsBtsDTO> lst = new ArrayList<>(mapCheck.values());
                  for (MrChecklistsBtsDTO dto : lst) {
                    MrChecklistsBtsDTO objectSearch = new MrChecklistsBtsDTO();
                    objectSearch.setMarketCode(dto.getMarketCode());
                    objectSearch.setDeviceType(dto.getDeviceType());
                    objectSearch.setCycle(dto.getCycle());
                    objectSearch.setMaterialType(dto.getMaterialType());
                    MrChecklistsBtsDTO checklistsBtsDTO = mrChecklistBtsRepository.checkMrChecklistBtsExit(objectSearch);
                    if (checklistsBtsDTO != null) {
                      dto.setChecklistId(checklistsBtsDTO.getChecklistId());
                    }
                    ResultInSideDto res = insertMrChecklistBts(dto);
                    if (RESULT.SUCCESS.equals(res.getKey())) {
                      ErrorInfo resultColumn = cellErrorList.get(k);
                      resultColumn.setMsg(I18n.getLanguage("common.success1"));
                      k++;
                    } else {
                      resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                          I18n.getLanguage("import.common.fail"));
                    }
                  }
                }
              } else {
                resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                    I18n.getLanguage("import.common.fail"));
              }
              resultInSideDto.setFilePath(filePath);
              exportFileImport(lstData, filePath, cellErrorList);

            } else {
              resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
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
      Cell cell = row.createCell(13);
      cell.setCellStyle(cellStResult);
      cell.setCellValue(I18n.getLanguage("cfgProcedureHardView.list.grid.importError"));

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
      sheetOne.autoSizeColumn(13, true);
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

  private ResultInSideDto insertOrUpdateMrChecklistBtsImport(MrChecklistsBtsDTO mrChecklistBtsDTO) {
    if (mrChecklistBtsDTO.getChecklistId() != null) {
      return updateMrChecklistBts(mrChecklistBtsDTO);
    } else {
      return insertMrChecklistBts(mrChecklistBtsDTO);
    }
  }

  public boolean validateImportInfo(Long index, MrChecklistsBtsDTO importDTO,
      List<MrChecklistsBtsDTO> list) {
    String resultImport = "";
    MrChecklistsBtsDetailDTO checklistsBtsDetailDTO = importDTO.getListDetail().get(0);
    if (StringUtils.isStringNullOrEmpty(importDTO.getMarketCode())) {
      resultImport = resultImport.concat(I18n.getLanguage("mrChecklistBts.err.marketName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getDeviceType())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("mrChecklistBts.err.deviceTypeName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getMaterialType())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("mrChecklistBts.err.materialTypeName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getCycleStr())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("mrChecklistBts.err.cycleStr"));
    }
    if (StringUtils.isStringNullOrEmpty(checklistsBtsDetailDTO.getContent())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("mrChecklistBts.err.content"));
    }
    if (checklistsBtsDetailDTO.getPhotoReq() != null) {
      if (checklistsBtsDetailDTO.getPhotoReq() == 1L) {
        if (StringUtils.isStringNullOrEmpty(checklistsBtsDetailDTO.getMinPhoto())) {
          resultImport = checkResultImport(resultImport)
              .concat(I18n.getLanguage("mrChecklistBts.err.minPhotoStr"));
        }
        if (StringUtils.isStringNullOrEmpty(checklistsBtsDetailDTO.getMaxPhoto())) {
          resultImport = checkResultImport(resultImport)
              .concat(I18n.getLanguage("mrChecklistBts.err.maxPhotoStr"));
        }
      }
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      setMapResult(index, resultImport);
      return false;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getMarketCode())) {
      setMapMarket();
      if (mapMarket.containsKey(Long.valueOf(importDTO.getMarketCode()))) {
        importDTO.setMarketName(String.valueOf(mapMarket.get(importDTO.getMarketCode())));
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getMarketName())) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.marketName.valid"));
        return false;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getDeviceType())) {
      setMapDeviceType();
      if (mapDeviceType.containsKey(importDTO.getDeviceType())) {
        importDTO.setDeviceTypeName(String.valueOf(mapMaterialType.get(importDTO.getDeviceType())));
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getDeviceTypeName())) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.deviceTypeName.valid"));
        return false;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getMaterialType())) {
      setMapMaterialType();
      if (mapMaterialType.containsKey(importDTO.getMaterialType())) {
        importDTO
            .setMaterialTypeName(String.valueOf(mapMaterialType.get(importDTO.getMaterialType())));
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getMaterialTypeName())) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.materialTypeName.valid"));
        return false;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getCycleStr())) {
      setMapCycle();
      for (String cycle : mapCycle.keySet()) {
        if (importDTO.getCycleStr().equals(mapCycle.get(cycle))) {
          importDTO.setCycle(Long.valueOf(cycle));
        }
      }
      if (importDTO.getCycle() == null) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.cycleStr.valid"));
        return false;
      }
    }
    if (StringUtils.isNotNullOrEmpty(checklistsBtsDetailDTO.getContent())) {
      MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO = new MrChecklistsBtsDetailDTO();
      List<MrChecklistsBtsDetailDTO> listContent = mrChecklistBtsDetailRepository
          .getListDetail(mrChecklistsBtsDetailDTO);
      List<String> listContentStr = new ArrayList<>();
      if (listContent != null && listContent.size() > 0) {
        listContent.forEach(item -> {
          listContentStr.add(item.getContent());
        });
        if (listContentStr.contains(checklistsBtsDetailDTO.getContent())) {
          setMapResult(index, I18n.getLanguage("mrChecklistBts.err.content.valid"));
          return false;
        }
      }
      importDTO.setContent(checklistsBtsDetailDTO.getContent());
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getSupplierCode())) {
      List<MrDeviceBtsDTO> listSupplier = mrDeviceBtsRepository
          .getListSupplierBtsByDeviceType(importDTO.getDeviceType(), importDTO.getMarketCode());
      List<String> listSupplierStr = new ArrayList<>();
      if (listSupplier != null && listSupplier.size() > 0) {
        listSupplier.forEach(item -> {
          listSupplierStr.add(item.getProducer());
        });
        if (!listSupplierStr.contains(importDTO.getSupplierCode())) {
          String supplier = "";
          for (MrDeviceBtsDTO deviceBtsDTO : listSupplier) {
            supplier += deviceBtsDTO.getProducer() + ", ";
          }
          supplier = supplier.trim();
          if (supplier.length() > 0) {
            supplier = supplier.substring(0, supplier.lastIndexOf(","));
          }
          setMapResult(index, I18n.getLanguage("mrChecklistBts.err.supplierCode.valid")
              + "; " + I18n.getLanguage("mrChecklistBts.suggest") + " " + supplier);
          return false;
        }
      } else {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.supplierCode.valid"));
        return false;
      }
    } else if (StringUtils.isNotNullOrEmpty(importDTO.getMarketCode())) {
      if ("2000289729".equals(importDTO.getMarketCode())) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.supplierCode.requireNation"));
        return false;
      }
    }
    if (StringUtils.isNotNullOrEmpty(checklistsBtsDetailDTO.getContent())) {
      if (checklistsBtsDetailDTO.getContent().length() > 1000) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.content.length"));
        return false;
      }
    }
    if (checklistsBtsDetailDTO.getPhotoReq() != null) {
      if (checklistsBtsDetailDTO.getPhotoReq() == 2L) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.photoReqDisplay.valid"));
        return false;
      }
    } else {
      importDTO.setPhotoReq(0L);
    }
    if (checklistsBtsDetailDTO.getMinPhoto() != null) {
      if (!StringUtils.isLong(String.valueOf(checklistsBtsDetailDTO.getMinPhoto()))) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.minPhotoStr.valid"));
        return false;
      } else if (Long.valueOf(checklistsBtsDetailDTO.getMinPhoto()) < 0L) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.minPhotoStr.valid"));
        return false;
      }
    }
    if (checklistsBtsDetailDTO.getMaxPhoto() != null) {
      if (!StringUtils.isLong(String.valueOf(checklistsBtsDetailDTO.getMaxPhoto()))) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.maxPhotoStr.valid"));
        return false;
      } else if (Long.valueOf(checklistsBtsDetailDTO.getMaxPhoto()) < 0L) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.maxPhotoStr.valid"));
        return false;
      }
      if (checklistsBtsDetailDTO.getMinPhoto() != null) {
        if (Long.valueOf(checklistsBtsDetailDTO.getMaxPhoto()) < Long
            .valueOf(checklistsBtsDetailDTO.getMinPhoto())) {
          setMapResult(index, I18n.getLanguage("mrChecklistBts.err.maxminCompare"));
          return false;
        }
      }
    }
    if (StringUtils.isNotNullOrEmpty(checklistsBtsDetailDTO.getCaptureGuide())) {
      if (checklistsBtsDetailDTO.getCaptureGuide().length() > 2000) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.captureGuide.length"));
        return false;
      }
    }
    String validateDuplicate = validateDuplicateImport(importDTO, list);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      setMapResult(index, validateDuplicate);
      return false;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getMarketCode()) &&
        StringUtils.isNotNullOrEmpty(importDTO.getDeviceType()) &&
        StringUtils.isNotNullOrEmpty(importDTO.getMaterialType()) &&
        StringUtils.isNotNullOrEmpty(String.valueOf(importDTO.getCycle())) &&
        StringUtils.isNotNullOrEmpty(importDTO.getContent())) {
      MrChecklistsBtsDTO dtoTmp = mrChecklistBtsRepository.checkMrChecklistBtsExit(importDTO);
      if (dtoTmp != null) {
//        importDTO.setChecklistId(dtoTmp.getChecklistId());
        setMapResult(index, I18n.getLanguage("MrCDCheckListBD.err.duplicate.inFile"));
        return false;
      }
    }

    if (checklistsBtsDetailDTO.getScoreChecklist() != null) {
      String regex = "[+-]?[0-9]+([.][0-9]{1,2})?";
      String point = String.valueOf(checklistsBtsDetailDTO.getScoreChecklist());
      if (!point.matches(regex)) {
        setMapResult(index, I18n.getLanguage("mrChecklistBts.err.point.valid"));
        return false;
      }
    }

    if (checklistsBtsDetailDTO.getIsImportant() != null && checklistsBtsDetailDTO.getIsImportant() == 2L) {
      setMapResult(index, I18n.getLanguage("mrChecklistBts.err.serious.valid"));
      return false;
    }
    return true;
  }

  private String checkResultImport(String resultImport) {
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      resultImport = resultImport.concat("; \n");
    }
    return resultImport;
  }

  private String validateDuplicateImport(MrChecklistsBtsDTO importDTO,
      List<MrChecklistsBtsDTO> list) {
    String marketCode = importDTO.getMarketCode();
    String deviceType = importDTO.getDeviceType();
    String materialType = importDTO.getMaterialType();
    String cycle = String.valueOf(importDTO.getCycle());
    String content = String.valueOf(importDTO.getContent());
    for (int i = 0; i < list.size(); i++) {
      MrChecklistsBtsDTO dtoCheck = list.get(i);
      if (marketCode.equals(dtoCheck.getMarketCode()) && deviceType.equals(dtoCheck.getDeviceType())
          && materialType.equals(dtoCheck.getMaterialType()) && cycle
          .equals(String.valueOf(dtoCheck.getCycle())) && content.equals(dtoCheck.getContent())) {
        return I18n.getLanguage("mrChecklistBts.err.dup-code-in-file")
            .replaceAll("0", String.valueOf(i + 1));
      }
    }
    return null;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 11) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.STT")).equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.marketName") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.deviceTypeName") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.materialTypeName"))
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.cycleStr") + " (*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.supplierCode"))
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.content") + " (*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.photoReqDisplay"))
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.minPhotoStr") + "\n" +
        I18n.getLanguage("mrChecklistBts.photoReq.required"))
        .equalsIgnoreCase(obj[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.maxPhotoStr") + "\n" +
        I18n.getLanguage("mrChecklistBts.photoReq.required"))
        .equalsIgnoreCase(obj[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.captureGuide"))
        .equalsIgnoreCase(obj[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.scoreChecklist"))
        .equalsIgnoreCase(obj[11].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrChecklistBts.isImportantName"))
        .equalsIgnoreCase(obj[12].toString().trim())) {
      return false;
    }
    return true;
  }

  public File handleFileExport(List<MrChecklistsBtsDTO> list, String[] columnExport, String date,
      String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = "";
    String title = "";
    String fileNameOut = "";
    String headerPrefix = "language.mrChecklistBts";
    String firstLeftHeader = I18n.getLanguage("mrChecklistBts.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("mrChecklistBts.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("mrChecklistBts.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("mrChecklistBts.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle;
    if (StringUtils.isNotNullOrEmpty(date)) {
      subTitle = I18n.getLanguage("mrChecklistBts.export.exportDate", date);
    } else {
      subTitle = "";
    }
    switch (code) {
      case "RESULT_IMPORT":
        sheetName = I18n.getLanguage("mrChecklistBts.import.sheetname");
        title = I18n.getLanguage("mrChecklistBts.import.title");
        fileNameOut = I18n.getLanguage("mrChecklistBts.import.fileNameOut");
        break;
      case "EXPORT_MR_CHECKLIST_BTS":
        sheetName = I18n.getLanguage("mrChecklistBts.export.sheetname");
        title = I18n.getLanguage("mrChecklistBts.export.title");
        fileNameOut = I18n.getLanguage("mrChecklistBts.export.fileNameOut");
        break;
      default:
        break;
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        list,
        sheetName,
        title,
        subTitle,
        startRow,
        cellTitleIndex,
        mergeTitleEndIndex,
        true,
        headerPrefix,
        lstHeaderSheet1,
        fieldSplit,
        "",
        firstLeftHeader,
        secondLeftHeader,
        firstRightHeader,
        secondRightHeader
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(startRow, 0, 0, 0,
        I18n.getLanguage("mrChecklistBts.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT_NOVALUE.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false,
          0, 0, new String[]{}, null, "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
  }

  public void setDetailValue(MrChecklistsBtsDTO mrChecklistBtsDTO) {
    if (StringUtils.isNotNullOrEmpty(mrChecklistBtsDTO.getMarketCode())) {
      mrChecklistBtsDTO
          .setMarketName(mapMarket.get(Long.valueOf(mrChecklistBtsDTO.getMarketCode())));
    }
    if (StringUtils.isNotNullOrEmpty(mrChecklistBtsDTO.getDeviceType())) {
      mrChecklistBtsDTO
          .setDeviceTypeName(mapDeviceType.get(mrChecklistBtsDTO.getDeviceType().toUpperCase()));
      if (MR_UCTT.REFRESHER_CD.equals(mrChecklistBtsDTO.getDeviceType())) {
        mrChecklistBtsDTO
            .setMaterialTypeName(mapMaterialType.get(mrChecklistBtsDTO.getMaterialType()));
      } else if (MR_UCTT.GENERATOR_CD.equals(mrChecklistBtsDTO.getDeviceType())) {
        mrChecklistBtsDTO
            .setMaterialTypeName(mapMaterialType.get(mrChecklistBtsDTO.getMaterialType()));
      }
    }
    if (mrChecklistBtsDTO.getPhotoReq() != null) {
      if (MR_UCTT.PHOTO_REQ_YES.equals(String.valueOf(mrChecklistBtsDTO.getPhotoReq()))) {
        mrChecklistBtsDTO.setPhotoReqDisplay(I18n.getLanguage("mrChecklistBts.photoReq.yes"));
      } else if (MR_UCTT.PHOTO_REQ_NO.equals(String.valueOf(mrChecklistBtsDTO.getPhotoReq()))) {
        mrChecklistBtsDTO.setPhotoReqDisplay(I18n.getLanguage("mrChecklistBts.photoReq.no"));
      }
    }
    if (mrChecklistBtsDTO.getIsImportant() != null) {
      if (MR_UCTT.PHOTO_REQ_YES.equals(String.valueOf(mrChecklistBtsDTO.getIsImportant()))) {
        mrChecklistBtsDTO.setIsImportantName(I18n.getLanguage("mrChecklistBts.photoReq.yes"));
      } else if (MR_UCTT.PHOTO_REQ_NO.equals(String.valueOf(mrChecklistBtsDTO.getIsImportant()))) {
        mrChecklistBtsDTO.setIsImportantName(I18n.getLanguage("mrChecklistBts.photoReq.no"));
      }
    } else {
      mrChecklistBtsDTO.setIsImportantName(I18n.getLanguage("mrChecklistBts.photoReq.no"));
    }
  }

  public void setMapMarket() {
    List<ItemDataCRInside> list = catLocationRepository.getListLocationByLevelCBB(null, 1L, null);
    if (list != null && !list.isEmpty()) {
      for (ItemDataCRInside dto : list) {
        mapMarket.put(dto.getValueStr(), dto.getDisplayStr());
      }
    }
  }

  public void setMapDeviceType() {
    mapDeviceType
        .put(MR_UCTT.REFRESHER_CD, I18n.getLanguage("mrChecklistBts.deviceType.refresher"));
    mapDeviceType
        .put(MR_UCTT.GENERATOR_CD, I18n.getLanguage("mrChecklistBts.deviceType.generator"));
  }

  public void setMapMaterialType() {
    mapMaterialType
        .put(MR_UCTT.OIL_CD, I18n.getLanguage("mrChecklistBts.materialType.oil"));
    mapMaterialType
        .put(MR_UCTT.GAS_CD, I18n.getLanguage("mrChecklistBts.materialType.gas"));
//    mapMaterialType.put(MR_UCTT.R410a, "R410a");
//    mapMaterialType.put(MR_UCTT.R410A, "R410A");
//    mapMaterialType.put(MR_UCTT.O, "0");
//    mapMaterialType.put(MR_UCTT.R32, "R32");
    List<MrDeviceBtsDTO> lstfuel = mrDeviceBtsRepository
        .getListfuelTypeByDeviceType(Constants.DEVICE_TYPE_MAP_MULTI_LANG.REFRESHER_CD, "");
    if (lstfuel != null && !lstfuel.isEmpty()) {
      for (MrDeviceBtsDTO dto : lstfuel) {
        mapMaterialType.put(dto.getFuelType(), dto.getFuelType());
      }
    }
  }

  public void setMapCycle() {
    Datatable datatCycle = catItemRepository.getItemMaster(MR_UCTT.MR_CHECKLIST_BTS_CYCLE,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) datatCycle.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapCycle.put(dto.getItemValue(), dto.getItemName());
      }
    }
  }

  //Dunglv commit

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
    String fileTemplateName = "IMPORT_MR_CHECK_LIST_BTS_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_MR_CHECK_LIST_BTS_TEMPLATE_VN.xlsx";
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
//
    // Loại thiết bị
    Sheet sheetThree = workBook.getSheetAt(2);
    k = 4;
    l = 1;
    CellStyle cellSt2 = null;
    for (String deviceType : Constants.DEVICE_TYPE_LST) {
      ewu.createCell(sheetThree, 0, k, String.valueOf(l++), cellSt2);
      ewu.createCell(sheetThree, 1, k, deviceType, cellSt2);
      ewu.createCell(sheetThree, 2, k,
          I18n.getLanguage(Constants.DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName().get(deviceType)),
          cellSt2);
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

//    Tao tieu de
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
        Constants.DEVICE_TYPE_MAP_MULTI_LANG.REFRESHER_CD, "");
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
    workBook.setActiveSheet(0);
    return workBook;
  }

  private String getFileTemplateName() {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_MR_CHECK_LIST_BTS_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_MR_CHECK_LIST_BTS_TEMPLATE_VN.xlsx";
    }
    return fileTemplateName;
  }

  private void setMapResult(Long index, String msg) {
    mapErrorResult.put(index.toString(), msg);
  }

}
