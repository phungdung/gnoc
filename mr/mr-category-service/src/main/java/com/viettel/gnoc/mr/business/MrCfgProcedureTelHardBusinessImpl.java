package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.repository.MrCfgProcedureTelRepository;
import com.viettel.gnoc.mr.repository.MrCheckListRepository;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

@Service
@Transactional
@Slf4j
public class MrCfgProcedureTelHardBusinessImpl implements MrCfgProcedureTelHardBusiness {

  @Autowired
  MrCfgProcedureTelRepository mrCfgProcedureTelRepository;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  MrDeviceRepository mrDeviceRepository;

  @Autowired
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Autowired
  MrScheduleTelRepository mrScheduleTelRepository;

  @Autowired
  MrScheduleTelHisRepository mrScheduleTelHisRepository;

  @Autowired
  MrDeviceBusiness mrDeviceBusiness;

  @Autowired
  CatItemBusiness catItemBusiness;
  @Autowired
  protected CatLocationBusiness catLocationBusiness;
  @Autowired
  MrCheckListRepository mrCheckListRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;
  private List<ItemDataCRInside> lstCountry;
  private String pathTemplate = "templates" + File.separator + "maintenance" + File.separator;
  private Map<String, String> mapErrorResult = new HashMap<>();
  public final static String REGEX_NUMBER = "([+-]*[1-9])([0-9])*";
  public final static String REGEX_DATE_MONTH_YEAR = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
  public final static String REGEX_MONTH_YEAR = "^(0?[1-9]|1[0-2])/(19|2[0-9])\\d{2}$";
  private List<ErrorInfo> cellErrorList = new ArrayList<>();

  private Map<String, String> mapCountryName = new HashMap<>();
  private Map<String, MrDeviceDTO> mapNetworkType = new HashMap<>();
  private Map<String, MrDeviceDTO> mapDeviceType = new HashMap<>();
  private Map<String, CatItemDTO> mapMrType = new HashMap<>();

  public void setMapCountryName() {
    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstCountryName) {
        mapCountryName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }

  public void setMapNetworkType() {
    List<MrDeviceDTO> lstNetwork = mrCheckListRepository.getListNetworkType();
    if (lstNetwork != null && !lstNetwork.isEmpty()) {
      for (MrDeviceDTO item : lstNetwork) {
        mapNetworkType.put(item.getNetworkType(), item);
      }
    }
  }

  public void setMapDeviceType() {
    List<MrDeviceDTO> lstDevice = mrCheckListRepository.getListDeviceType();
    if (lstDevice != null && !lstDevice.isEmpty()) {
      for (MrDeviceDTO item : lstDevice) {
        mapDeviceType.put(item.getNetworkType(), item);
      }
    }
  }

  public void setMapMrType() {
    List<CatItemDTO> dataMrContent = catItemBusiness
        .getListItemByCategoryAndParent(MR_ITEM_NAME.MR_TYPE, null);
    if (dataMrContent != null && !dataMrContent.isEmpty()) {
      for (CatItemDTO item : dataMrContent) {
        mapMrType.put(item.getItemValue(), item);
      }
    }
  }

  @Override
  public Datatable onSearch(MrCfgProcedureTelDTO mrCfgProcedureTelDTO) {
    //BD Cung'
    mrCfgProcedureTelDTO.setMrMode("H");
    Datatable datatable = mrCfgProcedureTelRepository.onSearch(mrCfgProcedureTelDTO, "H");
    return datatable;
  }

  @Override
  public ResultInSideDto insert(MrCfgProcedureTelDTO mrCfgProcedureTelDTO) {
    log.debug("Request to insert : {}", mrCfgProcedureTelDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    mrCfgProcedureTelDTO.setMrMode("H");
    List<MrCfgProcedureTelDTO> cfgProcedureTelDTOList = mrCfgProcedureTelRepository
        .checkMrCfgProcedureTelHardExist2(mrCfgProcedureTelDTO);
    if (cfgProcedureTelDTOList != null && cfgProcedureTelDTOList.size() > 0) {
      resultInSideDto.setKey(RESULT.DUPLICATE);
      resultInSideDto.setMessage(I18n.getLanguage("cfgProcedureHardView.validation.isDuplicate"));
      return resultInSideDto;
    } else {
      resultInSideDto = mrCfgProcedureTelRepository.insertOrUpdate(mrCfgProcedureTelDTO);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(MrCfgProcedureTelDTO mrCfgProcedureTelDTO) {
    log.debug("Request to update : {}", mrCfgProcedureTelDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    boolean checkUpdate = false;
    boolean check = true;

    if (mrCfgProcedureTelDTO != null) {
      //Check trùng
      List<MrCfgProcedureTelDTO> cfgProcedureTelDTOList = mrCfgProcedureTelRepository
          .checkMrCfgProcedureTelHardExist2(mrCfgProcedureTelDTO);
      if (cfgProcedureTelDTOList != null && cfgProcedureTelDTOList.size() > 0) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto.setMessage(I18n.getLanguage("cfgProcedureHardView.validation.isDuplicate"));
        check = false;
        return resultInSideDto;
      }
      if (check) {
        MrCfgProcedureTelDTO dtoUpdate = mrCfgProcedureTelRepository
            .getDetail(mrCfgProcedureTelDTO.getProcedureId());
        MrScheduleTelDTO dtoTel = new MrScheduleTelDTO();
        dtoTel.setProcedureId(mrCfgProcedureTelDTO.getProcedureId());

        // update cycleType, Cycle , sinh MR truoc ngay, thoi gian thuc hien -----> xóa lịch , Cập nhật is_complete_1M/ 3M/ 6M/ 12M = 1 ở MR_DEVICE
        List<MrScheduleTelDTO> lstScheduleTel = new ArrayList<>();
        List<MrScheduleTelDTO> lstScheduleTelDel = new ArrayList<>();
        if (!StringUtils.isStringNullOrEmpty(dtoUpdate)) {
          if (!mrCfgProcedureTelDTO.getCycleType().equals(dtoUpdate.getCycleType())
              || !String.valueOf(mrCfgProcedureTelDTO.getCycle())
              .equals(String.valueOf(dtoUpdate.getCycle()))
              || !mrCfgProcedureTelDTO.getGenMrBefore().equals(dtoUpdate.getGenMrBefore())
              || !mrCfgProcedureTelDTO.getMrTime().equals(dtoUpdate.getMrTime())) {
            lstScheduleTel = mrCfgProcedureTelRepository.getScheduleInProcedure(dtoTel);
            if (lstScheduleTel != null && !lstScheduleTel.isEmpty()) {
              for (MrScheduleTelDTO item : lstScheduleTel) {
                //Trường hợp: MrId null && WoID null
                if (StringUtils.isStringNullOrEmpty(item.getMrId()) && StringUtils
                    .isStringNullOrEmpty(item.getWoId())) {
                  lstScheduleTelDel.add(item);
                }
              }
            }
            // update MrDevice
            MrDeviceDTO deviceDTO = new MrDeviceDTO();
            deviceDTO.setMarketCode(mrCfgProcedureTelDTO.getMarketCode());
            deviceDTO.setArrayCode(mrCfgProcedureTelDTO.getArrayCode());
            deviceDTO.setDeviceType(mrCfgProcedureTelDTO.getDeviceType());
            deviceDTO.setNetworkType(mrCfgProcedureTelDTO.getNetworkType());
            deviceDTO.setCycle(mrCfgProcedureTelDTO.getCycle() == null ? null
                : mrCfgProcedureTelDTO.getCycle().toString());
            deviceDTO.setMrHard("1");

            List<MrDeviceDTO> result = new ArrayList<>();
            int rowStart = 0;
            int maxRow = 5000;
            while (true) {
              List<MrDeviceDTO> lstDTO = mrDeviceBusiness
                  .onSearchEntity(deviceDTO, rowStart, maxRow, "asc", "nodeCode");
              if (lstDTO != null && !lstDTO.isEmpty()) {
                result.addAll(lstDTO);
                if (rowStart == 0) {
                  rowStart++;
                }
                rowStart += maxRow;
              } else {
                break;
              }
            }

            // Cập nhật is_complete_1M/ 3M/ 6M/ 12M = 1 ở MR_DEVICE
            List<MrDeviceDTO> resultUpdate = new ArrayList<>();
            if (lstScheduleTelDel != null && !lstScheduleTelDel.isEmpty()) {
              if (result != null && !result.isEmpty()) {
                int size = result.size();
                for (int i = size - 1; i > -1; i--) {
                  for (MrScheduleTelDTO item : lstScheduleTelDel) {
                    if (item.getDeviceCode().equals(result.get(i).getNodeCode())) {
                      if ("M".equals(item.getCycleType())) {
                        if (!StringUtils.isStringNullOrEmpty(item.getMrHardCycle())) {
                          if (1L == item.getMrHardCycle()) {
                            result.get(i).setIsComplete1m(1L);
                            resultUpdate.add(result.get(i));
                            break;
                          } else if (3L == item.getMrHardCycle()) {
                            result.get(i).setIsComplete3m(1L);
                            resultUpdate.add(result.get(i));
                            break;
                          } else if (6L == item.getMrHardCycle()) {
                            result.get(i).setIsComplete6m(1L);
                            resultUpdate.add(result.get(i));
                            break;
                          } else if (12L == item.getMrHardCycle()) {
                            result.get(i).setIsComplete12m(1L);
                            resultUpdate.add(result.get(i));
                            break;
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            if (lstScheduleTelDel != null && !lstScheduleTelDel.isEmpty()) {
              checkUpdate = true;
              insertMrScheduleTelHis(lstScheduleTelDel, checkUpdate);
              mrScheduleTelRepository.deleteListSchedule(lstScheduleTelDel);
            }
            if (resultUpdate != null && !resultUpdate.isEmpty()) {
              mrDeviceBusiness.insertOrUpdateListDevice(resultUpdate);
            }
          }
        }
        //update thu tuc bao duong
        resultInSideDto = mrCfgProcedureTelRepository.insertOrUpdate(mrCfgProcedureTelDTO);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long procedureId) {
    log.debug("Request to delete : {}", procedureId);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
    //xoa cau hinh
    //Lay ra 2 listMrScheduleTel va listMrDevice
    MrScheduleTelDTO scheduleTelDTO = new MrScheduleTelDTO();
    scheduleTelDTO.setProcedureId(procedureId);
    List<MrScheduleTelDTO> lstMrScheduleTel = mrCfgProcedureTelRepository
        .getScheduleInProcedure(scheduleTelDTO);
    List<MrDeviceDTO> lstMrDevice = new ArrayList<>();
    MrDeviceDTO dtoDevice = new MrDeviceDTO();
    if (lstMrScheduleTel != null && !lstMrScheduleTel.isEmpty()) {
      for (MrScheduleTelDTO item : lstMrScheduleTel) {
        dtoDevice.setDeviceId(item.getDeviceId());
        MrDeviceDTO dtoOld = mrDeviceRepository.findMrDeviceById(dtoDevice.getDeviceId());
        //Cap nhat is_complete_1M/ 3M/ 6M/ 12M = 1 ở MR_DEVICE
        if (dtoOld != null && !StringUtils.isStringNullOrEmpty(dtoOld.getDeviceId())) {
          if ("M".equals(item.getCycleType())) {
            if (!StringUtils.isStringNullOrEmpty(item.getMrHardCycle())) {
              if (1L == item.getMrHardCycle()) {
                dtoOld.setIsComplete1m(1L);
              } else if (3L == item.getMrHardCycle()) {
                dtoOld.setIsComplete3m(1L);
              } else if (6L == item.getMrHardCycle()) {
                dtoOld.setIsComplete6m(1L);
              } else if (12L == item.getMrHardCycle()) {
                dtoOld.setIsComplete12m(1L);
              }
            }
          }
          lstMrDevice.add(dtoOld);
        }
      }
    }
    if (lstMrDevice != null && !lstMrDevice.isEmpty()) {
      mrDeviceBusiness.insertOrUpdateListDevice(lstMrDevice);
    }

    if (procedureId != null) {
      resultInSideDto = mrCfgProcedureTelRepository.delete(procedureId);
      if (lstMrScheduleTel != null && !lstMrScheduleTel.isEmpty()) {
        mrScheduleTelRepository.deleteListSchedule(lstMrScheduleTel);
        insertMrScheduleTelHis(lstMrScheduleTel, false);
      }
    }
    return resultInSideDto;
  }

  private void insertMrScheduleTelHis(List<MrScheduleTelDTO> lstScheduleTel, boolean checkUpdate) {
    List<MrScheduleTelHisDTO> lstScheduleTelHis = new ArrayList<>();
    for (MrScheduleTelDTO item : lstScheduleTel) {
      MrScheduleTelHisDTO scheduleHisDTO = new MrScheduleTelHisDTO();
      scheduleHisDTO.setMarketCode(item.getMarketCode());
      scheduleHisDTO.setArrayCode(item.getArrayCode());
      scheduleHisDTO.setDeviceType(item.getDeviceType());
      scheduleHisDTO.setDeviceId(item.getDeviceId() == null ? null : item.getDeviceId().toString());
      scheduleHisDTO.setDeviceCode(item.getDeviceCode());
      scheduleHisDTO.setDeviceName(item.getDeviceName());
      scheduleHisDTO.setMrDate(item.getNextDateModify() == null ? null
          : DateTimeUtils.date2ddMMyyyyHHMMss(item.getNextDateModify()));
      scheduleHisDTO.setMrContent(item.getMrContentId());
      scheduleHisDTO.setMrMode("H");
      scheduleHisDTO.setMrType(item.getMrType());
      scheduleHisDTO.setMrId(item.getMrId() == null ? null : item.getMrId().toString());
      scheduleHisDTO.setMrCode(item.getMrCode());
      scheduleHisDTO.setCrId(item.getCrId() == null ? null : item.getCrId().toString());
      scheduleHisDTO
          .setProcedureId(item.getProcedureId() == null ? null : item.getProcedureId().toString());
      scheduleHisDTO.setProcedureName(item.getProcedureName());
      scheduleHisDTO.setNetworkType(item.getNetworkType());
      scheduleHisDTO.setCycle(item.getCycle());
      scheduleHisDTO.setRegion(item.getRegion());
      if (checkUpdate) {
        scheduleHisDTO.setNote(I18n.getLanguage("mrCfgProcedure.update.note"));
      } else {
        scheduleHisDTO.setNote(I18n.getLanguage("mrCfgProcedure.delete.node"));
      }
      lstScheduleTelHis.add(scheduleHisDTO);
    }

    mrScheduleTelHisRepository.insertUpdateListScheduleHis(lstScheduleTelHis);
  }

  @Override
  public MrCfgProcedureTelDTO getDetail(Long procedureId) {
    log.debug("Request to getDetail : {}", procedureId);
    MrCfgProcedureTelDTO dto = mrCfgProcedureTelRepository.getDetail(procedureId);
    if (dto != null) {
      setMapMrWorkName(dto);
      if ("M".equalsIgnoreCase(dto.getCycleType())) {
        dto.setCycleName(I18n.getLanguage("cfgProcedureHardView.combobox.month"));
      } else {
        dto.setCycleName(I18n.getLanguage("cfgProcedureHardView.combobox.day"));
      }
      if ("I".equalsIgnoreCase(dto.getStatus())) {
        dto.setStatusName(I18n.getLanguage("cfgProcedureHardView.combobox.inActive"));
      } else {
        dto.setStatusName(I18n.getLanguage("cfgProcedureHardView.combobox.active"));
      }
      if ("H".equalsIgnoreCase(dto.getMrMode())) {
        dto.setMrModeName(I18n.getLanguage("cfgProcedureHardView.combobox.Hard"));
      } else {
        dto.setMrModeName(I18n.getLanguage("cfgProcedureHardView.combobox.Soft"));
      }
    }
    return dto;
  }

  @Override
  public ResultInSideDto importMrCfgProcedureTelHard(MultipartFile multipartFile) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    mapErrorResult.clear();
    cellErrorList.clear();
    mapArrayByNetworkType.clear();
    mapNetworkTypeByDeviceType.clear();
    mapNetworkType.clear();
    mapDeviceType.clear();
    mapMrType.clear();
    setMapArrayByNetworkType();
    setMapNetworkTypeByDeviceType();
    setMapNetworkType();
    setMapDeviceType();
    setMapMrType();
    lstCountry = catLocationBusiness.getListLocationByLevelCBB(null, 1L, null);
    Map<String, CatItemDTO> mapOgWork = new HashMap<>();
    Map<String, Long> mapSubCat = new HashMap<>();
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
          //Lấy list Mảng, List Đầu việc
          List<CatItemDTO> dataOgWork = mrCfgProcedureCDBusiness.getOgListWorks(null);
          List<CatItemDTO> lstSubCat = mrCfgProcedureCDBusiness.getMrSubCategory();
          //xử lý map đầu việc
          if (dataOgWork != null && !dataOgWork.isEmpty()) {
            dataOgWork.forEach(i -> {
              mapOgWork.put(String.valueOf(i.getItemId()), i);
            });
          }
          //xử lý map mảng
          if (lstSubCat != null && !lstSubCat.isEmpty()) {
            lstSubCat.forEach(i -> {
              mapSubCat.put(i.getItemName(), i.getItemId());
            });
          }
          File fileImp = new File(filePath);
          List<Object[]> lstData;
          if (filePath.contains("xlsx")) {
            lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 8,
                0, 18, 2);
          } else {
            lstData = CommonImport.getDataFromExcel(fileImp, 0, 8,
                0, 18, 2);
          }
          List<MrCfgProcedureTelDTO> lstAddOrUpdate = new ArrayList<>();
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
                  && lstData.get(i)[16] == null && lstData.get(i)[17] == null
                  && lstData.get(i)[18] == null
              ) {
                lstData.remove(i);
              }
            }
            if (lstData.size() <= 1000) {
              boolean allTrue = true;
              int row = 8;
              for (Object[] obj : lstData) {
                MrCfgProcedureTelDTO dto = new MrCfgProcedureTelDTO();
                if (obj[1] != null) {
                  dto.setMarketCode(obj[1].toString().trim());
                } else {
                  dto.setMarketCode(null);
                }
                if (obj[2] != null) {
                  dto.setArrayCode(obj[2].toString().trim());
                } else {
                  dto.setArrayCode(null);
                }
                if (obj[3] != null) {
                  dto.setNetworkType(obj[3].toString().trim());
                } else {
                  dto.setNetworkType(null);
                }
                if (obj[4] != null) {
                  dto.setDeviceType(obj[4].toString().trim());
                } else {
                  dto.setDeviceType(null);
                }
                if (obj[5] != null) {
                  dto.setCycleType(obj[5].toString().trim());
                } else {
                  dto.setCycleType(null);
                }
                if (obj[6] != null) {
                  dto.setCycleStr(obj[6].toString().trim());
                } else {
                  dto.setCycleStr(null);
                }
                if (obj[7] != null) {
                  dto.setMrMode(obj[7].toString().trim());
                } else {
                  dto.setMrMode(null);
                }
                if (obj[8] != null) {
                  dto.setProcedureName(obj[8].toString().trim());
                } else {
                  dto.setProcedureName(null);
                }
                if (obj[9] != null) {
                  dto.setGenMrBeforeStr(obj[9].toString().trim());
                } else {
                  dto.setGenMrBeforeStr(null);
                }

                if (obj[10] != null) {
                  dto.setMrContentId(obj[10].toString().trim());
                } else {
                  dto.setMrContentId(null);
                }
                if (obj[11] != null) {
                  dto.setMrTimeStr(obj[11].toString().trim());
                } else {
                  dto.setMrTimeStr(null);
                }
                if (obj[12] != null) {
                  dto.setPriorityCode(obj[12].toString().trim());
                } else {
                  dto.setPriorityCode(null);
                }
                if (obj[13] != null) {
                  dto.setReGenMrAfterStr(obj[13].toString().trim());
                } else {
                  dto.setReGenMrAfterStr(null);
                }
                if (obj[14] != null) {
                  dto.setImpact(obj[14].toString().trim());
                } else {
                  dto.setImpact(null);
                }
                if (obj[15] != null) {
                  dto.setStatus(obj[15].toString().trim());
                } else {
                  dto.setStatus(null);
                }
                if (obj[16] != null) {
                  dto.setMrWorks(obj[16].toString().trim().replaceAll(" ", ""));
                } else {
                  dto.setMrWorks(null);
                }
                if (obj[17] != null) {
                  dto.setStrExpDate(obj[17].toString().trim());
                } else {
                  dto.setExpDate(null);
                }

                if (obj[18] != null) {
                  dto.setWoContent(obj[18].toString().trim());
                } else {
                  dto.setWoContent(null);
                }

                if (validateImportInfo(Long.valueOf(row), dto, mapMarket, mapOgWork, mapSubCat)) {
                  cellErrorList
                      .add(new ErrorInfo(row, I18n.getLanguage("common.import.validRecord")));
                } else {
                  allTrue = false;
                  cellErrorList.add(new ErrorInfo(row, getValidateResult(Long.valueOf(row))));
                }
                lstAddOrUpdate.add(dto);
                row++;
              }

              if (allTrue) {
                if (!lstAddOrUpdate.isEmpty()) {
                  int k = 0;
                  for (MrCfgProcedureTelDTO dtoTmp : lstAddOrUpdate) {
                    if (dtoTmp.getProcedureId() == null) {
                      resultInSideDto = insert(dtoTmp);
                    } else {
                      resultInSideDto = update(dtoTmp);
                    }
                    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                      ErrorInfo resultColumn = cellErrorList.get(k);
                      if (dtoTmp.getProcedureId() == null) {
                        resultColumn.setMsg(I18n.getLanguage("common.success1"));
                      } else {
                        resultColumn
                            .setMsg(I18n.getLanguage("cfgProcedureHardView.update.importUpdate"));
                      }
                    }
                    k++;
                  }
                } else {
                  resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                      I18n.getLanguage("import.common.fail"));
                }
              } else {
                resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                    I18n.getLanguage("import.common.fail"));
              }

              //>>>===Xuất file kết quả===<<<
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
          resultInSideDto = new ResultInSideDto(null, RESULT.ERROR, validate);
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
    String fileTemplateName = "IMPORT_CFG_PROCEDURE_TEL_HARD_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_CFG_PROCEDURE_TEL_HARD_TEMPLATE_VI.xlsx";
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

  private boolean validateImportInfo(Long index, MrCfgProcedureTelDTO dto,
      Map<String, ItemDataCRInside> mapCountry, Map<String, CatItemDTO> mapOgWork,
      Map<String, Long> mapSubCat) {
    //Quốc gia
    if (StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.marketCode") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else if (!mapCountry.containsKey(dto.getMarketCode())) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.marketCode") + " " + I18n
          .getLanguage("notify.invalid"));
      return false;
    }

    //Mảng arrayCode
    if (StringUtils.isStringNullOrEmpty(dto.getArrayCode())) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.arrayCode") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else {
      if (!mapSubCat.containsKey(dto.getArrayCode())) {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.list.grid.arrayCode") + " " + I18n
                .getLanguage("notify.invalid"));
        return false;
      }
    }

    //Loại mạng networkType
    if (StringUtils.isStringNullOrEmpty(dto.getNetworkType())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.networkType") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else {
      if (!mapNetworkType.containsKey(dto.getNetworkType())) {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.list.grid.networkType") + " " + I18n
                .getLanguage("cfgProcedureHardView.dataImport.notexist"));
        return false;
      }
      //so sánh networkType nhap vao co dung voi mang hay ko?
      if (!dto.getArrayCode()
          .equals(mapArrayByNetworkType.get(dto.getNetworkType() + "_" + dto.getArrayCode()))) {
        setMapResult(index, I18n.getLanguage("cfgProcedureHardView.networkType.falseArrayCode"));
        return false;
      }
    }

    //Loại thiết bị deviceType
    if (StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.deviceType") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else {
      if (!mapDeviceType.containsKey(dto.getDeviceType())) {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.list.grid.deviceType") + " " + I18n
                .getLanguage("cfgProcedureHardView.dataImport.notexist"));
        return false;
      }
      //so sánh DeviceType nhap vao co dung voi Loai Mang hay ko?
      if (!dto.getNetworkType().equals(
          mapNetworkTypeByDeviceType.get(dto.getDeviceType() + "_" + dto.getNetworkType()))) {
        setMapResult(index, I18n.getLanguage("cfgProcedureHardView.deviceType.falseNetworkType"));
        return false;
      }

    }

    //Loại chu kỳ
    if (!StringUtils.isStringNullOrEmpty(dto.getCycleType())) {
      if ("D".equalsIgnoreCase(dto.getCycleType()) || "M".equalsIgnoreCase(dto.getCycleType())) {
        dto.setCycleType(dto.getCycleType().toUpperCase());
      } else {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.list.grid.cycleType") + " " + I18n
                .getLanguage("notify.invalid"));
        return false;
      }
    }

    //Chu kỳ
    if (StringUtils.isStringNullOrEmpty(dto.getCycleStr())) {

    } else if (!dto.getCycleStr().matches(REGEX_NUMBER)) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.check.cycle") + " " + I18n
          .getLanguage("cfgProcedureHardView.checkData.number"));
      return false;
    } else if (dto.getCycleStr().matches(REGEX_NUMBER)) {
      if (Long.parseLong(dto.getCycleStr()) > 0) {
        dto.setCycle(Long.parseLong(dto.getCycleStr()));
      } else {
        setMapResult(index, I18n.getLanguage("cfgProcedureHardView.check.cycle") + " " + I18n
            .getLanguage("cfgProcedureHardView.checkData.number"));
        return false;
      }
    }
    //Hình thức BD mrMode
    if (StringUtils.isStringNullOrEmpty(dto.getMrMode())) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.mrMode") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else {
      if ("H".equalsIgnoreCase(dto.getMrMode())) {
        dto.setMrMode(dto.getMrMode().toUpperCase());
      } else {
        setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.mrMode") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
    }

    //Tên thủ tục procedureName
    if (StringUtils.isStringNullOrEmpty(dto.getProcedureName())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.procedureName") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else if (dto.getProcedureName().length() > 250) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.procedureName.maxLength.250"));
      return false;
    }

    //Sinh MR trước(Ngày) genMrBefore
    if (StringUtils.isStringNullOrEmpty(dto.getGenMrBeforeStr())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.genMrBefore") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else if (!dto.getGenMrBeforeStr().matches(REGEX_NUMBER)) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.check.genMrBefore") + " " + I18n
              .getLanguage("cfgProcedureHardView.checkData.number"));
      return false;
    } else if (dto.getGenMrBeforeStr().matches(REGEX_NUMBER)) {
      if (Long.parseLong(dto.getGenMrBeforeStr()) > 0) {
        dto.setGenMrBefore(Long.parseLong(dto.getGenMrBeforeStr()));
      } else {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.check.genMrBefore") + " " + I18n
                .getLanguage("cfgProcedureHardView.checkData.number"));
        return false;
      }
    }

    //Mã loại hoạt động mrContentId
    if (StringUtils.isStringNullOrEmpty(dto.getMrContentId())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.mrContentId") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else {
      if (!mapMrType.containsKey(dto.getMrContentId())) {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.list.grid.mrContentId") + " " + I18n
                .getLanguage("notify.invalid"));
        return false;
      } else {
        //set lai itemName vao DB chu ko luu bang itemValue
        dto.setMrContentId(mapMrType.get(dto.getMrContentId()).getItemName());
      }
    }

    //Thời gian thực hiện(Ngày) mrTime
    if (StringUtils.isStringNullOrEmpty(dto.getMrTimeStr())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.mrTime") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else if (!dto.getMrTimeStr().matches(REGEX_NUMBER)) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.check.mrTime") + " " + I18n
              .getLanguage("cfgProcedureHardView.checkData.number"));
      return false;
    } else if (dto.getMrTimeStr().matches(REGEX_NUMBER)) {
      if (Long.parseLong(dto.getMrTimeStr()) > 0) {
        dto.setMrTime(Long.parseLong(dto.getMrTimeStr()));
      } else {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.check.mrTime") + " " + I18n
                .getLanguage("cfgProcedureHardView.checkData.number"));
        return false;
      }
    }

    //Mức độ ưu tiên priorityCode
    if (StringUtils.isStringNullOrEmpty(dto.getPriorityCode())) {

    } else if (!"1".equals(dto.getPriorityCode()) && !"2".equals(dto.getPriorityCode())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.priorityCode") + " " + I18n
              .getLanguage("cfgProcedureHardView.checkData.priorityCode"));
      return false;
    } else if ("1".equals(dto.getPriorityCode()) || "2".equals(dto.getPriorityCode())) {
      dto.setPriorityCode(
          I18n.getLanguage("cfgProcedureHardView.list.grid.level.priorityCode") + " " + dto
              .getPriorityCode());
    }

    //Tạo lại MR sau reGenMrAfter
    if (StringUtils.isStringNullOrEmpty(dto.getReGenMrAfterStr())) {

    } else if (!dto.getReGenMrAfterStr().matches(REGEX_NUMBER)) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.check.reGenMrAfter") + " " + I18n
              .getLanguage("cfgProcedureHardView.checkData.number"));
      return false;
    } else if (dto.getReGenMrAfterStr().matches(REGEX_NUMBER)) {
      if (Long.parseLong(dto.getReGenMrAfterStr()) > 0) {
        dto.setReGenMrAfter(Long.parseLong(dto.getReGenMrAfterStr()));
      } else {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.check.reGenMrAfter") + " " + I18n
                .getLanguage("cfgProcedureHardView.checkData.number"));
        return false;
      }
    }

    //Mức ảnh hưởng impact
    if (StringUtils.isStringNullOrEmpty(dto.getImpact())) {

    } else if (
        !"1".equals(dto.getImpact()) && !"2".equals(dto.getImpact())) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.impact") + " " + I18n
          .getLanguage("cfgProcedureHardView.checkData.impact"));
      return false;
    } else if ("1".equals(dto.getImpact()) || "2".equals(dto.getImpact())) {
      dto.setImpact(
          I18n.getLanguage("cfgProcedureHardView.list.grid.level.impact") + " " + dto.getImpact());
    }

    //Trạng thái status
    if (StringUtils.isStringNullOrEmpty(dto.getStatus())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.status") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else {
      if ("A".equalsIgnoreCase(dto.getStatus()) || "I".equalsIgnoreCase(dto.getStatus())) {
        dto.setStatus(dto.getStatus().toUpperCase());
      } else {
        setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.status") + " " + I18n
            .getLanguage("cfgProcedureHardView.checkData.status"));
        return false;
      }
    }

    // Đầu việc mrWorks MR_WORKS
    if (StringUtils.isStringNullOrEmpty(dto.getMrWorks())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.mrWorks") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else {
      try {
        String arrayMrWork[] = dto.getMrWorks().split(",");
        //Lấy ra mã đầu việc theo mảng
        boolean isCheck = true;
        Long subCatItem = mapSubCat.get(dto.getArrayCode());
        String msg = "";
        if (arrayMrWork != null) {
          for (int i = 0; i < arrayMrWork.length; i++) {
            if (mapOgWork.containsKey(arrayMrWork[i].trim())) {
              CatItemDTO itemDTO = mapOgWork.get(arrayMrWork[i].trim());
              if (itemDTO != null && itemDTO.getParentItemId() != null) {
                if (!itemDTO.getParentItemId().equals(subCatItem)) {
                  isCheck = false;
                  msg += itemDTO.getItemId() + ",";
                  setMapResult(index, msg);
                }
              } else {
                isCheck = false;
                msg += arrayMrWork[i].trim() + ",";
                setMapResult(index, msg);
              }
            } else {
              setMapResult(index,
                  I18n.getLanguage("cfgProcedureHardView.list.grid.mrWorks") + " " + I18n
                      .getLanguage("notify.invalid"));
              isCheck = false;
            }
            setMapResult(index, I18n.getLanguage("cfgProcedureHardView.mrWorks.nok") + " " + msg);
          }
          if (!isCheck) {
            return false;
          }
        }
      } catch (Exception e) {
        setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.mrWorks") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
    }

    // Ngày hết hiệu lực expDate
    if (StringUtils.isStringNullOrEmpty(dto.getStrExpDate())) {

    } else if (!dto.getStrExpDate().matches(REGEX_DATE_MONTH_YEAR)) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.strExpDate") + " " + I18n
          .getLanguage("cfgProcedureHardView.strExpDate.format"));
      return false;
    } else if (dto.getStrExpDate().matches(REGEX_DATE_MONTH_YEAR)) {
      dto.setExpDate(DateTimeUtils.convertStringToDate(dto.getStrExpDate()));
    }

    //Nội dung công việc woContent
    if (StringUtils.isStringNullOrEmpty(dto.getWoContent())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.woContent") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else if (dto.getWoContent().length() > 4000) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.woContent") + " " + I18n
          .getLanguage("cfgProcedureHardView.list.grid.woContent.maxLength.4000"));
      return false;
    }

    //Xử lý tìm trong DB nếu đã tồn tại thì mặc định là Update
    if (dto != null) {
      List<MrCfgProcedureTelDTO> cfgProcedureTelDTOList = mrCfgProcedureTelRepository
          .checkMrCfgProcedureTelHardExist2(dto);
      if (cfgProcedureTelDTOList != null && cfgProcedureTelDTOList.size() > 0) {
        MrCfgProcedureTelDTO dtoTmp = cfgProcedureTelDTOList.get(0);
        //set lai ProcedureId đã tồn tại trong Database cho DTO mới
        dto.setProcedureId(dtoTmp.getProcedureId());
      }
    }
    return true;
  }

  private void setMapResult(Long index, String msg) {
    mapErrorResult.put(index.toString(), msg);
  }

  private String getValidateResult(Long index) {
    return mapErrorResult.get(index.toString());
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

  public Workbook processFileTemplate(ExcelWriterUtils ewu) throws Exception {
    Workbook workBook;
    //Quốc gia
    lstCountry = catLocationBusiness.getListLocationByLevelCBB(null, 1L, null);
    String pathFileImport = pathTemplate + getFileTemplateName();
    workBook = ewu.readFileExcelFromTemplate(pathFileImport);
    Sheet sheetTwo = workBook.getSheetAt(1);
    int k = 2;
    int l = 1;
    CellStyle cellSt1 = null;
    for (ItemDataCRInside rowS2 : lstCountry) {
      if (rowS2 == null) {
        break;
      }
      ewu.createCell(sheetTwo, 0, k, String.valueOf(l), cellSt1);
      ewu.createCell(sheetTwo, 1, k,
          rowS2.getValueStr() == null ? "" : String.valueOf(rowS2.getValueStr()).trim(), cellSt1);
      ewu.createCell(sheetTwo, 2, k,
          rowS2.getDisplayStr() == null ? "" : rowS2.getDisplayStr().trim(), cellSt1);
      k++;
      l++;
    }

    //Mảng
    List<CatItemDTO> data = mrCfgProcedureCDBusiness.getMrSubCategory();
    Sheet sheetThree = workBook.getSheetAt(2);
    k = 2;
    l = 1;
    CellStyle cellSt2 = null;
    for (CatItemDTO array : data) {
      if (array == null) {
        break;
      }
      ewu.createCell(sheetThree, 0, k, String.valueOf(l), cellSt2);
      ewu.createCell(sheetThree, 1, k, array.getItemName() == null ? "" : array.getItemName(),
          cellSt2);
      k++;
      l++;
    }

    //Loại mạng
    Sheet sheetFour = workBook.getSheetAt(3);
    k = 2;
    l = 1;
    List<MrDeviceDTO> list = mrCfgProcedureTelRepository.getListNetworkType();
    CellStyle cellSt3 = null;
    for (MrDeviceDTO mrDeviceDTO : list) {
      if (mrDeviceDTO == null) {
        break;
      }
      ewu.createCell(sheetFour, 0, k, String.valueOf(l), cellSt3);
      ewu.createCell(sheetFour, 1, k,
          mrDeviceDTO.getArrayCode() == null ? "" : mrDeviceDTO.getArrayCode(), cellSt3);
      ewu.createCell(sheetFour, 2, k,
          mrDeviceDTO.getNetworkType() == null ? "" : mrDeviceDTO.getNetworkType(), cellSt3);
      k++;
      l++;
    }

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

    //Loại thiết bị
    //phai tao thêm các sheet trong code. vi tao trong template se bi overload data read excel
    Sheet sheetFive = workBook
        .createSheet(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType"));
    //set heigh cho thanh header
    Row rowHeadStyle5 = sheetFive.createRow(1);
    rowHeadStyle5.setHeightInPoints(25);
    List<MrDeviceDTO> listDeviceType2 = mrCfgProcedureTelRepository.getListDeviceType2();
    k = 2;
    l = 1;
    CellStyle cellSt4 = null;
    ewu.createCell(sheetFive, 0, k - 1, I18n.getLanguage("common.list.grid.stt"), cellStyleHeader);
    ewu.createCell(sheetFive, 1, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType.arrayCode"),
        cellStyleHeader);
    ewu.createCell(sheetFive, 2, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType.networkType"),
        cellStyleHeader);
    ewu.createCell(sheetFive, 3, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType.deviceType"),
        cellStyleHeader);
    for (int i = 0; i <= 3; i++) {
      sheetFive.autoSizeColumn(i);
      sheetFive.setColumnWidth(1, 10000);
      sheetFive.setColumnWidth(2, 10000);
      sheetFive.setColumnWidth(3, 10000);
    }

    for (MrDeviceDTO mrDeviceDTO : listDeviceType2) {
      if (mrDeviceDTO == null) {
        break;
      }
      ewu.createCell(sheetFive, 0, k, String.valueOf(l), cellSt4);
      ewu.createCell(sheetFive, 1, k,
          mrDeviceDTO.getArrayCode() == null ? "" : mrDeviceDTO.getArrayCode(), cellSt4);
      ewu.createCell(sheetFive, 2, k,
          mrDeviceDTO.getNetworkType() == null ? "" : mrDeviceDTO.getNetworkType(), cellSt4);
      ewu.createCell(sheetFive, 3, k,
          mrDeviceDTO.getDeviceType() == null ? "" : mrDeviceDTO.getDeviceType(), cellSt4);
      k++;
      l++;
    }

    //Loại hoạt động
    Sheet sheetSix = workBook
        .createSheet(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent"));
    //set heigh cho thanh header
    Row rowHeadStyle6 = sheetSix.createRow(1);
    rowHeadStyle6.setHeightInPoints(25);
    List<CatItemDTO> dataMrContent = catItemBusiness
        .getListItemByCategoryAndParent(MR_ITEM_NAME.MR_TYPE, null);
    k = 2;
    l = 1;
    CellStyle cellSt5 = null;
    ewu.createCell(sheetSix, 0, k - 1, I18n.getLanguage("common.list.grid.stt"), cellStyleHeader);
    ewu.createCell(sheetSix, 1, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent.mrContentID"),
        cellStyleHeader);
    ewu.createCell(sheetSix, 2, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent.mrContentName"),
        cellStyleHeader);
    for (int i = 0; i <= 2; i++) {
      sheetSix.autoSizeColumn(i);
      sheetSix.setColumnWidth(1, 15000);
      sheetSix.setColumnWidth(2, 15000);
    }
    for (CatItemDTO mrContent : dataMrContent) {
      if (mrContent == null) {
        break;
      }
      ewu.createCell(sheetSix, 0, k, String.valueOf(l), cellSt5);
      ewu.createCell(sheetSix, 1, k,
          mrContent.getItemValue() == null ? "" : mrContent.getItemValue(), cellSt5);
      ewu.createCell(sheetSix, 2, k, mrContent.getItemName() == null ? "" : mrContent.getItemName(),
          cellSt5);
      k++;
      l++;
    }

    //Đầu việc
    Sheet sheetSeven = workBook
        .createSheet(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrWork"));
    //set heigh cho thanh header
    Row rowHeadStyle7 = sheetSeven.createRow(1);
    rowHeadStyle7.setHeightInPoints(25);
    k = 2;
    l = 1;
    CellStyle cellSt6 = null;
    ewu.createCell(sheetSeven, 0, k - 1, I18n.getLanguage("common.list.grid.stt"), cellStyleHeader);
    ewu.createCell(sheetSeven, 1, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType.arrayCode"),
        cellStyleHeader);
    ewu.createCell(sheetSeven, 2, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent.mrWorkCode"),
        cellStyleHeader);
    ewu.createCell(sheetSeven, 3, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent.mrWorkName"),
        cellStyleHeader);
    for (int i = 0; i <= 3; i++) {
      sheetSeven.autoSizeColumn(i);
      sheetSeven.setColumnWidth(1, 20000);
      sheetSeven.setColumnWidth(2, 5000);
      sheetSeven.setColumnWidth(3, 30000);
    }
    //Lấy danh sách mảng
    List<CatItemDTO> lstDataArrayCode = mrCfgProcedureCDBusiness.getMrSubCategory();
    Long itemId = null;
    if (lstDataArrayCode != null) {
      for (CatItemDTO dtoArray : lstDataArrayCode) {
        //Duyệt từng array lấy ra list đầu việc tương ứng theo ItemId
        itemId = dtoArray.getItemId();
        List<CatItemDTO> dataWorkByArray = mrCfgProcedureCDBusiness.getOgListWorks(itemId);
        for (CatItemDTO dto : dataWorkByArray) {
          if (dto == null) {
            break;
          }
          ewu.createCell(sheetSeven, 0, k, String.valueOf(l), cellSt6);
          ewu.createCell(sheetSeven, 1, k,
              dtoArray.getItemValue() == null ? "" : dtoArray.getItemValue(), cellSt6);
          ewu.createCell(sheetSeven, 2, k,
              dto.getItemId() == null ? "" : dto.getItemId().toString(), cellSt6);
          ewu.createCell(sheetSeven, 3, k, dto.getItemValue() == null ? "" : dto.getItemValue(),
              cellSt6);
          k++;
          l++;
        }
      }
    }
    return workBook;
  }

  @Override
  public File exportSearchData(MrCfgProcedureTelDTO mrCfgProcedureTelDTO) throws Exception {
    String[] header = new String[]{
        "marketCode", "arrayCode", "networkType",
        "deviceType", "cycleType", "cycle",
        "mrModeName", "procedureName", "genMrBefore",
        "mrContentId", "mrTime", "priorityCode",
        "reGenMrAfter", "impact", "statusName",
        "mrWorksName", "strExpDate", "woContent"
    };

    String[] align = new String[]{
        "LEFT", "LEFT", "LEFT",
        "LEFT", "LEFT", "LEFT",
        "LEFT", "LEFT", "LEFT",
        "LEFT", "LEFT", "LEFT",
        "LEFT", "LEFT", "LEFT",
        "LEFT", "LEFT", "LEFT"
    };
    //BD Cung'
    mrCfgProcedureTelDTO.setMrMode("H");
    List<MrCfgProcedureTelDTO> lstData = mrCfgProcedureTelRepository
        .onSearchExport(mrCfgProcedureTelDTO, "H");
    if (lstData != null && lstData.size() > 0) {
      for (MrCfgProcedureTelDTO telDTO : lstData) {
        if ("D".equals(telDTO.getCycleType())) {
          telDTO.setCycleType(I18n.getLanguage("cfgProcedureHardView.combobox.day"));
        } else if ("M".equals(telDTO.getCycleType())) {
          telDTO.setCycleType(I18n.getLanguage("cfgProcedureHardView.combobox.month"));
        }
      }
    }
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header, align);
    return exportFileEx(lstData, lstHeaderSheet);
  }

  private File exportFileEx(List<MrCfgProcedureTelDTO> lstData,
      List<ConfigHeaderExport> lstHeaderSheet) throws Exception {
    String fileNameOut = "Procedure";
    String sheetName = I18n.getLanguage("cfgProcedureHardView.export.sheetname");
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
        , sheetName
        , I18n.getLanguage("cfgProcedureHardView.list.grid")
        , ""
        , 7
        , 4
        , lstHeaderSheet.size()
        , false
        , "language.cfgProcedureHardView.list.grid"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , null
        , null
        , null
        , null
    );
    cellSheet = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("cfgProcedureHardView.list.grid.stt"),
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
        I18n.getLanguage("cfgProcedureHardView.list.grid")

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

  private String getFileTemplateName() {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_CFG_PROCEDURE_TEL_HARD_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_CFG_PROCEDURE_TEL_HARD_TEMPLATE_VI.xlsx";
    }
    return fileTemplateName;
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
      Cell cell = row.createCell(19);
      cell.setCellStyle(cellStResult);
      cell.setCellValue(I18n.getLanguage("cfgProcedureHardView.list.grid.importError"));

      for (Object[] c_row : lstData) {
        if (c_row[8] == null && c_row[1] == null && c_row[2] == null && c_row[3] == null
            && c_row[4] == null && c_row[5] == null && c_row[6] == null && c_row[7] == null) {
          break;
        }
        for (int j = 0; j < 19; j++) {
          ewu.createCell(sheetOne, j, i, c_row[j] == null ? "" : c_row[j].toString().trim(), null);
        }
        i++;
      }
      for (ErrorInfo err : lstError) {
        row = sheetOne.getRow(err.getRow());
        cell = row.createCell(19);
        cell.setCellValue(err.getMsg());
      }
      sheetOne.autoSizeColumn(19, true);
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

  HashMap<String, String> mapArrayByNetworkType = new HashMap<>();

  public void setMapArrayByNetworkType() {
    //Lấy ra ds Mảng, loại mạng, loại thiết bị
    List<MrDeviceDTO> deviceDTOList = mrCfgProcedureTelRepository.getListDeviceType2();
    if (deviceDTOList != null && !deviceDTOList.isEmpty()) {
      for (MrDeviceDTO dto : deviceDTOList) {
        mapArrayByNetworkType
            .put(dto.getNetworkType() + "_" + dto.getArrayCode(), dto.getArrayCode());
      }
    }
  }

  HashMap<String, String> mapNetworkTypeByDeviceType = new HashMap<>();

  public void setMapNetworkTypeByDeviceType() {
    //Lấy ra ds Mảng, loại mạng, loại thiết bị
    List<MrDeviceDTO> mrDeviceDTOList = mrCfgProcedureTelRepository.getListDeviceType2();
    if (mrDeviceDTOList != null && !mrDeviceDTOList.isEmpty()) {
      for (MrDeviceDTO dto : mrDeviceDTOList) {
        mapNetworkTypeByDeviceType
            .put(dto.getDeviceType() + "_" + dto.getNetworkType(), dto.getNetworkType());
      }
    }
  }

  //lấy ra mảng theo mã đầu việc tương ứng.
  HashMap<Long, Long> mapArrayByMrWork = new HashMap<>();

  public void setMapArrayByMrWork() {
    //Lấy ra ds MrWork với parentItemId =null
    List<CatItemDTO> dataWorkList = mrCfgProcedureCDBusiness.getOgListWorks(null);
    if (dataWorkList != null && !dataWorkList.isEmpty()) {
      for (CatItemDTO dtoTemp : dataWorkList) {
        mapArrayByMrWork.put(dtoTemp.getItemId(), dtoTemp.getParentItemId());
      }
    }
  }

  //Set ten cho tung ma dau viec MrWork
  private void setMapMrWorkName(MrCfgProcedureTelDTO dto) {
    List<CatItemDTO> lstCatItem = commonStreamServiceProxy.getListCatItemDTO(new CatItemDTO());
    if (StringUtils.isNotNullOrEmpty(dto.getMrWorks())) {
      if (lstCatItem != null && !lstCatItem.isEmpty()) {
        String strCate = "";
        String[] arrRoleCode = dto.getMrWorks().split(",");
        for (String rCode : arrRoleCode) {
          for (CatItemDTO catDTO : lstCatItem) {
            if (rCode.equalsIgnoreCase(String.valueOf(catDTO.getItemId()))) {
              strCate += catDTO.getItemName() + ", ";
              break;
            }
          }
        }
        if (strCate.trim().endsWith(",")) {
          strCate = strCate.trim().substring(0, strCate.trim().length() - 1);
        }
        dto.setMrWorksName(strCate);
      } else {
        dto.setMrWorksName(dto.getMrWorks());
      }
    }
  }
}
