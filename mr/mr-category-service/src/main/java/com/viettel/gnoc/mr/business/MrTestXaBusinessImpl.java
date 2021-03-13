package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.mr.repository.MrNodesRepository;
import com.viettel.gnoc.mr.repository.MrTestXaRepository;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
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
public class MrTestXaBusinessImpl implements MrTestXaBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  MrTestXaRepository mrTestXaRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  MrNodesRepository mrNodesRepository;

  private Map<String, String> mapErrorResult = new HashMap<>();
  private List<ErrorInfo> cellErrorList = new ArrayList<>();

  private String pathTemplate = "templates" + File.separator + "maintenance" + File.separator;

  private final static String EXPORT_MR_TEST_XA = "EXPORT_MR_TEST_XA";
  Map<Long, Object> mapId = new HashMap<>();
  Map<Long, String> mapIds = new HashMap<>();


  @Override
  public Datatable getListDatatableMrCdBatterryDTO(MrCdBatteryDTO mrCdBatteryDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    mrCdBatteryDTO.setUserImplemen(userToken.getUserName());
    return mrTestXaRepository.getListDatatableMrCdBatterryDTO(mrCdBatteryDTO);
  }

  private Date converClientDateToServerDate(Date clientTime, Double offset) {
    Date result = clientTime;
    if (offset == null || offset.equals(0.0)) {
      return result;
    }
    if (clientTime == null) {
      return result;
    }
    try {
      Calendar cal = Calendar.getInstance();
      Date date = result;
      cal.setTime(date);
      cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - offset.intValue());
      return cal.getTime();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public MrCdBatteryDTO findMrCDBatteryById(Long dcPowerId) {
    return mrTestXaRepository.findMrCDBatteryById(dcPowerId);
  }

  @Override
  public MrCdBatteryDTO findMrCDBatteryByProperty(MrCdBatteryDTO mrCdBatteryDTO) {
    return mrTestXaRepository.findMrCDBatteryByProperty(mrCdBatteryDTO);
  }

  @Override
  public List<ItemDataCRInside> getListLocationCombobox(Long parentId) {
    return mrTestXaRepository.getListLocationCombobox(parentId);
  }

  @Override
  public List<ItemDataCRInside> getListCountry() {
    return mrTestXaRepository.getListCountry();
  }

  @Override
  public MrCdBatteryDTO getDetailById(Long dcPowerId) {
    return mrTestXaRepository.getDetailById(dcPowerId);
  }

  @Override
  public ResultInSideDto updateMrCdBatteryDTO(MrCdBatteryDTO mrCdBatteryDTO) {
    MrCdBatteryDTO find = mrTestXaRepository
        .findMrCDBatteryByProperty(mrCdBatteryDTO);
    UserToken userToken = ticketProvider.getUserToken();
    List<MrCdBatteryDTO> lstWoCode = new ArrayList<>();
    if (StringUtils.isNotNullOrEmpty(find.getWoCode())) {
      if (("0".equals(mrCdBatteryDTO.getIswoAccu())) && !find
          .getIswoAccu().equals(mrCdBatteryDTO.getIswoAccu())) {
        // lay danh sach tu co cung woCode trong bang MrCdBattery
        lstWoCode = mrTestXaRepository
            .getListWoFromMrCdBattery(find.getWoCode());

        List<String> lstCloseWo = new ArrayList<>();
        List<MrNodesDTO> lstDeleMrNodes = new ArrayList<>();

        if (lstWoCode != null && !lstWoCode.isEmpty()) {
          if (lstWoCode.size() == 1) {
            lstCloseWo.add(lstWoCode.get(0).getWoCode());
            String[] arrayWoId = lstWoCode.get(0).getWoCode().split("_");
            MrNodesDTO mrNodesDele = new MrNodesDTO();
            mrNodesDele.setWoId(arrayWoId[(arrayWoId.length - 1)]);
            mrNodesDele.setNodeName(lstWoCode.get(0).getDcPower());
            lstDeleMrNodes.add(mrNodesDele);
          } else if (lstWoCode.size() > 1) {
            int size = lstWoCode.size();
            int count = 0;
            for (int i = lstWoCode.size() - 1; i > -1; i--) {
              MrCdBatteryDTO mrCdBattery;
              if (lstWoCode.get(i).getDcPowerId().equals(mrCdBatteryDTO.getDcPowerId())) {
                mrCdBattery = mrCdBatteryDTO;
              } else {
                mrCdBattery = lstWoCode.get(i);
              }

              if (!StringUtils.isStringNullOrEmpty(mrCdBattery.getIswoAccu()) && "0"
                  .equals(mrCdBattery.getIswoAccu())) {
                String[] arrayWoId = mrCdBattery.getWoCode().split("_");
                MrNodesDTO mrNodesDele = new MrNodesDTO();
                mrNodesDele.setWoId(arrayWoId[(arrayWoId.length - 1)]);
                mrNodesDele.setNodeName(mrCdBattery.getDcPower());
                lstDeleMrNodes.add(mrNodesDele);
                count++;
              } else {
                lstWoCode.remove(lstWoCode.get(i));
              }
            }
            if (size == count) {
              //add vao list goi dong WO
              Map<String, String> mapCheckWo = new HashMap<>();
              for (MrCdBatteryDTO mcbdto : lstWoCode) {
                if (!mapCheckWo.containsKey(mcbdto.getWoCode())) {
                  lstCloseWo.add(mcbdto.getWoCode());
                  mapCheckWo.put(mcbdto.getWoCode(), mcbdto.getWoCode());
                }
              }
            }
          }
        }
        //20102020 dung pv bo goi dong wo
        if (lstCloseWo != null && !lstCloseWo.isEmpty()) {
          for (String woCode : lstCloseWo) {
            WoUpdateStatusForm closeWo = new WoUpdateStatusForm();
            WoInsideDTO woInsideDTO = woServiceProxy.findWoByWoCodeNoOffset(woCode);
            if ((woInsideDTO != null)) {
              closeWo.setNewStatus(8L);
              closeWo.setWoCode(woInsideDTO.getWoCode());
              closeWo.setReasonChange(I18n.getLanguage("mrCdBatteryDTO.closeWo"));
              closeWo.setSystemChange("MR");
              closeWo.setUserChange(userToken.getUserName());
              closeWo.setFinishTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
              closeWo.setResultClose(0L);
              woServiceProxy.changeStatusWoProxy(closeWo);
            }
          }
        }
        //end
        //20102020 dung pv bo xoa danh sach tu trong MrNode thay bang cap nhat status =1,update_date = sysdate,comment
        if (lstDeleMrNodes != null && !lstDeleMrNodes.isEmpty()) {
          for (MrNodesDTO mrNodesDTO : lstDeleMrNodes) {
//            mrNodesRepository
//                .deleteMrNodeByWoIdAndNodeName(mrNodesDTO.getWoId(), mrNodesDTO.getNodeName());
            mrNodesRepository
                .updateMrNodeByWoIdAndNodeName(mrNodesDTO.getWoId(), mrNodesDTO.getNodeName());
          }
        }
        mrCdBatteryDTO.setMrCode("");
        mrCdBatteryDTO.setWoCode("");
      }
    }
    // update các MrCdBatteryDTO tru ban ghi cap nhat tren web
    if (lstWoCode != null && !lstWoCode.isEmpty()) {
      for (MrCdBatteryDTO dto : lstWoCode) {
        if (!find.getDcPowerId().equals(dto.getDcPowerId())) {
          MrCdBatteryDTO updateMrCdBattery = mrTestXaRepository
              .findById(Long.parseLong(dto.getDcPowerId()));
          updateMrCdBattery.setMrCode(null);
          updateMrCdBattery.setWoCode(null);
          mrTestXaRepository.updateMrCdBatteryDTO(updateMrCdBattery);
        }
      }
    }
    mrCdBatteryDTO.setRecentDischargeCd(find.getRecentDischargeCd());
    mrCdBatteryDTO.setUpdatedUser(userToken.getUserName());
    mrCdBatteryDTO.setUpdatedTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
    ResultInSideDto resultInSideDto = mrTestXaRepository.updateMrCdBatteryDTO(mrCdBatteryDTO);
    return resultInSideDto;
  }

  private ResultInSideDto importUpdateMrCdBatteryDTO(MrCdBatteryDTO mrCdBatteryDTO) {
    MrCdBatteryDTO find = mrTestXaRepository
        .findMrCDBatteryByProperty(mrCdBatteryDTO);
    UserToken userToken = ticketProvider.getUserToken();
    mrCdBatteryDTO.setRecentDischargeCd(find.getRecentDischargeCd());
    mrCdBatteryDTO.setUpdatedUser(userToken.getUserName());
    mrCdBatteryDTO.setUpdatedTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
    ResultInSideDto resultInSideDto = mrTestXaRepository.updateMrCdBatteryDTO(mrCdBatteryDTO);
    return resultInSideDto;
  }

  @Override
  public List<ItemDataCRInside> getListLocationComboboxByCode(String locationCode) {
    return mrTestXaRepository.getListLocationComboboxByCode(locationCode);
  }

  @Override
  public File exportData(MrCdBatteryDTO mrCdBatteryDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    mrCdBatteryDTO.setUserImplemen(userToken.getUserName());
    List<MrCdBatteryDTO> mrCdBatteryDTOS = mrTestXaRepository.getDataExport(mrCdBatteryDTO);
    return exportFileTemplate(mrCdBatteryDTOS);
  }

  @Override
  public File getTemplate() {
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
  public ResultInSideDto importData(MultipartFile multipartFile) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    Map<String, ItemDataCRInside> mapMarket = new HashMap<>();
    mapErrorResult.clear();
    cellErrorList.clear();
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
                0, 16, 2);
          } else {
            lstData = CommonImport.getDataFromExcel(fileImp, 0, 8,
                0, 16, 2);
          }
          if (lstData.size() > 5000) {
            resultInSideDto.setKey("ERROR_NO_DOWNLOAD");
            resultInSideDto.setMessage(I18n.getLanguage("mrTestXa.maxrow.import"));
            return resultInSideDto;
          }
          List<MrCdBatteryDTO> lstAddOrUpdate = new ArrayList<>();
          if (lstData != null && lstData.size() > 0) {
            for (int i = lstData.size() - 1; i >= 0; i--) {
              if (lstData.get(i)[0] == null && lstData.get(i)[1] == null
                  && lstData.get(i)[2] == null && lstData.get(i)[3] == null
                  && lstData.get(i)[4] == null && lstData.get(i)[5] == null
                  && lstData.get(i)[6] == null && lstData.get(i)[7] == null
                  && lstData.get(i)[8] == null && lstData.get(i)[9] == null
                  && lstData.get(i)[10] == null && lstData.get(i)[11] == null
                  && lstData.get(i)[12] == null && lstData.get(i)[13] == null
                  && lstData.get(i)[14] == null && lstData.get(i)[15] == null
                  && lstData.get(i)[16] == null) {
                lstData.remove(i);
              }
            }
            if (lstData.size() <= 5000) {
              boolean allTrue = true;
              int row = 8;
              UserToken userToken = ticketProvider.getUserToken();
              List<Long> lstID = new ArrayList<>();
              List<MrCdBatteryDTO> lstOR = new ArrayList<>();
              List<MrCdBatteryDTO> lstIDs = new ArrayList<>();
              for (Object[] obj : lstData) {
                MrCdBatteryDTO dto = new MrCdBatteryDTO();
                if (obj[1] != null) {
                  dto.setDcPowerId(obj[1].toString().trim());
                } else {
                  dto.setDcPowerId("");
                }
                lstOR.add(dto);
              }
              for (int i = 0; i < lstOR.size(); i++) {
                lstID.add(Long.parseLong(lstOR.get(i).getDcPowerId()));
                if ((i != 0 && i % 500 == 0) || i == lstOR.size() - 1) {
                  lstIDs = mrTestXaRepository.getByIdImport(lstID, userToken.getUserName());
                  setMapId(lstIDs);
                  lstID.clear();
                }
              }

              for (Object[] obj : lstData) {
                MrCdBatteryDTO dto = new MrCdBatteryDTO();
                if (obj[1] != null) {
                  dto.setDcPowerId(obj[1].toString().trim());
                } else {
                  dto.setDcPowerId("");
                }
                if (obj[2] != null) {
                  dto.setMarketName(obj[2].toString().trim());
                } else {
                  dto.setMarketName("");
                }
                if (obj[3] != null) {
                  dto.setAreaName(obj[3].toString().trim());
                } else {
                  dto.setAreaName("");
                }
                if (obj[4] != null) {
                  dto.setProvinceName(obj[4].toString().trim());
                } else {
                  dto.setProvinceName("");
                }
                if (obj[5] != null) {
                  dto.setDistrictName(obj[5].toString().trim());
                } else {
                  dto.setDistrictName(null);
                }
                if (obj[6] != null) {
                  dto.setStationCode(obj[6].toString().trim());
                } else {
                  dto.setStationCode("");
                }
                if (obj[7] != null) {
                  dto.setDcPower(obj[7].toString().trim());
                } else {
                  dto.setDcPower("");
                }
                if (obj[8] != null) {
                  dto.setDischargeType(obj[8].toString().trim());
                } else {
                  dto.setDischargeType("");
                }
                if (obj[9] != null) {
                  dto.setRecentDischargeCd(obj[9].toString().trim());
                } else {
                  dto.setRecentDischargeCd("");
                }
                if (obj[10] != null) {
                  dto.setTimeDischarge(obj[10].toString().trim());
                } else {
                  dto.setTimeDischarge("");
                }
                if (obj[11] != null) {
                  if (I18n.getLanguage("mrTestXa.yes").equals(obj[11].toString().trim())) {
                    dto.setIswoAccu(Constants.TESTXA.ISWOACCUTRUE);
                  } else if (I18n.getLanguage("mrTestXa.no")
                      .equals(obj[11].toString().trim())) {
                    dto.setIswoAccu(Constants.TESTXA.ISWOACCUFAIL);
                  } else {
                    dto.setIswoAccu(Constants.TESTXA.ISWOACCUVALIDATE);
                  }
                }
                if (obj[12] != null) {
                  dto.setMrCode(obj[12].toString().trim());
                } else {
                  dto.setMrCode("");
                }
                if (obj[13] != null) {
                  dto.setWoCode(obj[13].toString().trim());
                } else {
                  dto.setWoCode("");
                }
                if (obj[14] != null) {
                  dto.setStatus(obj[14].toString().trim());
                } else {
                  dto.setStatus("");
                }
                if (obj[15] != null) {
                  dto.setStatusNode(obj[15].toString().trim());
                } else {
                  dto.setStatusNode("");
                }
                if (obj[16] != null) {
                  dto.setDischargeConfirm(obj[16].toString().trim());
                } else {
                  dto.setDischargeConfirm("");
                }

                if (validateImportInfo(Long.valueOf(row), dto, lstAddOrUpdate)) {
                  cellErrorList.add(new ErrorInfo(row, I18n.getLanguage("common.success1")));
                } else {
                  allTrue = false;
                  cellErrorList.add(new ErrorInfo(row, getValidateResult(Long.valueOf(row))));
                }
                lstAddOrUpdate.add(dto);
                row++;
              }
              if (allTrue) {
                if (!lstAddOrUpdate.isEmpty()) {
                  List<MrCdBatteryDTO> lstRoot = new ArrayList<>();
                  Map<Long, MrCdBatteryDTO> mapRoot = new HashMap<>();
                  List<Long> lstIds = new ArrayList<>();
                  for (int i = 0; i < lstAddOrUpdate.size(); i++) {
                    lstIds.add(Long.parseLong(lstAddOrUpdate.get(i).getDcPowerId()));
                    if ((i != 0 && i % 500 == 0) || i == lstAddOrUpdate.size() - 1) {
                      List<MrCdBatteryDTO> DcPowerTemps = mrTestXaRepository
                          .getListMrCdBatteryByListId(lstIds);
                      if (DcPowerTemps != null && DcPowerTemps.size() > 0) {
                        lstRoot.addAll(DcPowerTemps);
                      }
                      lstIds.clear();
                    }
                  }
                  lstRoot.forEach(i -> {
                    mapRoot.put(Long.parseLong(i.getDcPowerId()), i);
                  });
                  for (int i = lstAddOrUpdate.size() - 1; i > -1; i--) {
                    MrCdBatteryDTO btsDTO = lstAddOrUpdate.get(i);
                    MrCdBatteryDTO mrCdBatteryDTO = mapRoot.get(
                        btsDTO.getDcPowerId() != null ? Long.parseLong(btsDTO.getDcPowerId())
                            : null);
                    if (mrCdBatteryDTO != null) {
                      btsDTO.setStationId(mrCdBatteryDTO.getStationId());
                      btsDTO.setStationCode(mrCdBatteryDTO.getStationCode());
                      btsDTO.setDcPower(mrCdBatteryDTO.getDcPower());
                      btsDTO.setProvince(mrCdBatteryDTO.getProvince());
                      btsDTO.setStaffName(mrCdBatteryDTO.getStaffName());
                      btsDTO.setStaffMail(mrCdBatteryDTO.getStaffMail());
                      btsDTO.setStaffPhone(mrCdBatteryDTO.getStaffPhone());
                      btsDTO.setDischargeType(mrCdBatteryDTO.getDischargeType());
                      btsDTO.setTimeDischarge(mrCdBatteryDTO.getTimeDischarge());
                      btsDTO.setRecentDischargeCd(mrCdBatteryDTO.getRecentDischargeCd());
                      btsDTO.setMrCode(mrCdBatteryDTO.getMrCode());
                      btsDTO.setWoCode(mrCdBatteryDTO.getWoCode());
                      btsDTO.setStatus(mrCdBatteryDTO.getStatus());
                      btsDTO.setCreatedTime(mrCdBatteryDTO.getCreatedTime());
                      btsDTO.setUpdatedTime(DateTimeUtils.convertDateToString(new Date()));
                      btsDTO.setUpdatedUser(mrCdBatteryDTO.getUpdatedUser());
                      btsDTO.setRecentDischageGnoc(mrCdBatteryDTO.getRecentDischageGnoc());
                      btsDTO.setRecentDischageNoc(mrCdBatteryDTO.getRecentDischageNoc());
                      btsDTO.setProductionTechnology(mrCdBatteryDTO.getProductionTechnology());
                      btsDTO.setDistrictCode(mrCdBatteryDTO.getDistrictCode());
//                      btsDTO.setDischarge_confirm(mrCdBatteryDTO.getDischarge_confirm());
                      btsDTO.setResultDischarge(mrCdBatteryDTO.getResultDischarge());
                      btsDTO.setDischargeNumber(mrCdBatteryDTO.getDischargeNumber());
                      btsDTO.setDischargeReasonFail(mrCdBatteryDTO.getDischargeReasonFail());
                      btsDTO.setRecentDischargeGnoc(mrCdBatteryDTO.getRecentDischargeGnoc());
                      btsDTO.setRecentDischargeNoc(mrCdBatteryDTO.getRecentDischargeNoc());
                      btsDTO.setReasonAccept(mrCdBatteryDTO.getReasonAccept());
                      btsDTO.setStatusAccept(mrCdBatteryDTO.getStatusAccept());
                      btsDTO.setAreaCode(mrCdBatteryDTO.getAreaCode());
                      btsDTO.setProvinceCode(mrCdBatteryDTO.getProvinceCode());
//                      btsDTO.setIswo_accu(mrCdBatteryDTO.getIswo_accu());
                      btsDTO.setMarketCode(mrCdBatteryDTO.getMarketCode());
                      btsDTO.setDistrictName(mrCdBatteryDTO.getDistrictName());
                      resultInSideDto = importUpdateMrCdBatteryDTO(btsDTO);
                    }
                  }
//                  String res = UpdateListMrCdBattery(lstAddOrUpdate);
                  if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
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

  private void setMapId(List<MrCdBatteryDTO> lstIDs) {
    if (!lstIDs.isEmpty()) {
      for (MrCdBatteryDTO item : lstIDs) {
        mapId.put(Long.valueOf(item.getDcPowerId()), item);
      }

    }
  }

  private boolean validateImportInfo(Long index, MrCdBatteryDTO dto, List<MrCdBatteryDTO> lst) {
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(dto.getIswoAccu())) {
      resultImport = resultImport
          .concat(I18n.getLanguage("mrTestXa.list.grid.iswo_accu_name.isnotnul"));
    }
    if (Constants.TESTXA.ISWOACCUVALIDATE.equals(dto.getIswoAccu())) {
      resultImport = resultImport
          .concat(I18n.getLanguage("mrTestXa.list.grid.iswo_accu_name.validate"));
    }
    if (!mapId.containsKey(Long.valueOf(dto.getDcPowerId()))) {
      resultImport = resultImport
          .concat(I18n.getLanguage("mrTestXa.list.grid.record.isnull"));
      setMapResult(index, resultImport);
      return false;
    }

    String duplicate = validateDuplicate(dto, lst);
    if (StringUtils.isNotNullOrEmpty(duplicate)) {
      resultImport = duplicate;
      setMapResult(index, resultImport);
      return false;
    }

    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      setMapResult(index, resultImport);
      return false;
    }

    return true;
  }

  private String validateDuplicate(MrCdBatteryDTO dto, List<MrCdBatteryDTO> lst) {
    if (!lst.isEmpty()) {
      for (int i = 0; i < lst.size(); i++) {
        MrCdBatteryDTO mrCd = lst.get(i);
        if (dto.getDcPower().equals(mrCd.getDcPower()) && dto.getDischargeType()
            .equals(mrCd.getDischargeType()) && dto.getStationCode()
            .equals(mrCd.getStationCode())) {
          return I18n.getLanguage("mrTestXa.err.dup-code-in-file")
              .replaceAll("0", String.valueOf(i + 1));
        }
      }
    }
    return null;
  }

  private void setMapResult(Long index, String msg) {
    mapErrorResult.put(index.toString(), msg);
  }

  private String getValidateResult(Long index) {
    return mapErrorResult.get(index.toString());
  }

  private String getFileTemplateName() {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_MR_TEST_XA_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_MR_TEST_XA_TEMPLATE_VI.xlsx";
    }
    return fileTemplateName;
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
    String fileTemplateName = "IMPORT_MR_TEST_XA_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_MR_TEST_XA_TEMPLATE_VI.xlsx";
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
      Cell cell = row.createCell(17);
      cell.setCellStyle(cellStResult);
      cell.setCellValue(I18n.getLanguage("mrTestXa.list.grid.importError"));

      for (Object[] c_row : lstData) {
        if (c_row[8] == null && c_row[1] == null && c_row[2] == null && c_row[3] == null
            && c_row[4] == null && c_row[5] == null && c_row[6] == null && c_row[7] == null) {
          break;
        }
        for (int j = 0; j < 17; j++) {
          ewu.createCell(sheetOne, j, i, c_row[j] == null ? "" : c_row[j].toString().trim(), null);
        }
        i++;
      }
      for (ErrorInfo err : lstError) {
        row = sheetOne.getRow(err.getRow());
        cell = row.createCell(17);
        cell.setCellValue(err.getMsg());
      }
      sheetOne.autoSizeColumn(11, true);
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

  public Workbook processFileTemplate(ExcelWriterUtils ewu) throws Exception {
    Workbook workBook;
    String pathFileImport = pathTemplate + getFileTemplateName();
    workBook = ewu.readFileExcelFromTemplate(pathFileImport);

    //Tao tieu de
    Font xssFontTitle = workBook.createFont();
    xssFontTitle.setFontName("Times New Roman");
    xssFontTitle.setFontHeightInPoints((short) 22);
    xssFontTitle.setColor(IndexedColors.BLACK.index);
    xssFontTitle.setBold(true);

    //set font chữ cho header
    Font xSSFFontHeader = workBook.createFont();
    xSSFFontHeader.setFontName("Times New Roman");
    xSSFFontHeader.setFontHeightInPoints((short) 12);
    xSSFFontHeader.setColor(IndexedColors.BLACK.index);
    xSSFFontHeader.setBold(true);

    CellStyle cellStyleHeader = workBook.createCellStyle();
    cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleHeader.setBorderLeft(BorderStyle.THIN);
    cellStyleHeader.setBorderBottom(BorderStyle.THIN);
    cellStyleHeader.setBorderRight(BorderStyle.THIN);

    HSSFWorkbook hwb = null;
    hwb = new HSSFWorkbook();
    HSSFPalette palette = hwb.getCustomPalette();
    //set màu cho header
    HSSFColor myColor = palette
        .findSimilarColor(Integer.valueOf(146), Integer.valueOf(208),
            Integer.valueOf(80));
    cellStyleHeader.setBorderTop(BorderStyle.THIN);
    cellStyleHeader.setFillForegroundColor(myColor.getIndex());
    cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyleHeader.setWrapText(true);
    cellStyleHeader.setFont(xSSFFontHeader);

    workBook.setActiveSheet(0);
    return workBook;
  }

  private File exportFileTemplate(List<MrCdBatteryDTO> mrCdBatteryDTOS)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("mrTestXa.export.title");
    String title = I18n.getLanguage("mrTestXa.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("dcPowerId", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("marketName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("areaName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("provinceName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");

    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("districtName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("stationCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("dcPower", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("dischargeTypeName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("recentDischargeCd", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("timeDischarge", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("iswoAccuName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("mrCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("woCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("statusWoName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("statusNode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("dischargeConfirm", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    //kiểm tra đầu vào

    fileNameOut = EXPORT_MR_TEST_XA;
    subTitle = I18n
        .getLanguage("mrTestXa.export.exportDate", DateTimeUtils.convertDateOffset());
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        mrCdBatteryDTOS
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.mrTestXa.list.grid"
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
    configFileExport.setCustomColumnWidthNoMerge(
        new String[]{"2000", "3000", "6000", "6000", "5000", "9000", "6000", "6000", "5000", "6000",
            "6000", "4000", "10000", "10000", "5000", "4000", "10000"});
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
}
