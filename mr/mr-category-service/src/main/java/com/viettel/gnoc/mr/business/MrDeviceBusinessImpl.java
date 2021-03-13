package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.MR_CONFIG_GROUP;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.WO_STATE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrDeviceSoftRepository;
import com.viettel.gnoc.mr.repository.MrHisRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MrDeviceBusinessImpl implements MrDeviceBusiness {

  @Autowired
  MrDeviceRepository mrDeviceRepository;
  @Autowired
  CatLocationBusiness catLocationBusiness;
  @Autowired
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;
  @Autowired
  MrScheduleTelRepository mrScheduleTelRepository;
  @Autowired
  MrDeviceSoftRepository mrDeviceSoftRepository;
  @Autowired
  MrRepository mrRepository;
  @Autowired
  MrScheduleTelHisRepository mrScheduleTelHisRepository;
  @Autowired
  WoServiceProxy woServiceProxy;
  @Autowired
  UnitRepository unitRepository;
  @Autowired
  TicketProvider ticketProvider;
  @Autowired
  UserRepository userRepository;
  @Autowired
  MrHisRepository mrHisRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;
  HashMap<String, String> mapMarketName = new HashMap<>();
  HashMap<String, String> mapArray = new HashMap<>();
  HashMap<String, String> mapMrConfirmHard = new HashMap<>();
  Map<Long, UnitDTO> mapUnit = new HashMap<>();
  Map<String, UsersInsideDto> mapUser = new HashMap<>();
  Map<String, MrDeviceDTO> mapDeviceInDB = new HashMap<>();
  Map<String, String> mapA_NInDB = new HashMap<>();
  Map<String, String> mapA_N_DInDB = new HashMap<>();
  UserToken userTokenImport = null;
  boolean checkIsComplete = true;
  private final static String MR_DEVICE_RESULT_IMPORT = "MR_DEVICE_RESULT_IMPORT";
  private final static String MR_DEVICE_EXPORT = "MR_DEVICE_EXPORT";

  private int maxRecord = 1500;

  public void setMapMarketName() {
    List<ItemDataCRInside> lstMarketCode = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstMarketCode != null && !lstMarketCode.isEmpty()) {
      for (ItemDataCRInside itemDataCRInside : lstMarketCode) {
        mapMarketName
            .put(String.valueOf(itemDataCRInside.getValueStr()), itemDataCRInside.getDisplayStr());
      }
    }
  }

  public void setMrConfirmHardStr() {
    List<MrConfigDTO> lstMrConfirmHardStr = mrScheduleTelRepository.getConfigByGroup(
        MR_CONFIG_GROUP.LY_DO_KO_BD);
    if (lstMrConfirmHardStr != null && !lstMrConfirmHardStr.isEmpty()) {
      for (MrConfigDTO item : lstMrConfirmHardStr) {
        mapMrConfirmHard
            .put(String.valueOf(item.getConfigCode()), item.getConfigName());
      }
    }
  }

  public void setMapArray() {
    List<CatItemDTO> lstArrayCode = mrCfgProcedureCDBusiness.getMrSubCategory();
    if (lstArrayCode != null && !lstArrayCode.isEmpty()) {
      for (CatItemDTO catItemDTO : lstArrayCode) {
        mapArray
            .put(String.valueOf(catItemDTO.getItemCode()), catItemDTO.getItemName());
      }
    }
  }

  @Override
  public List<MrDeviceDTO> getListDeviceTypeByNetworkType(String arrayCode, String networkType) {
    return mrDeviceRepository.getListDeviceTypeByNetworkType(arrayCode, networkType);
  }

  @Override
  public List<MrDeviceDTO> getListNetworkTypeByArrayCode(String arrayCode) {
    return mrDeviceRepository.getListNetworkTypeByArrayCode(arrayCode);
  }

  @Override
  public Datatable getListMrDeviceSoftDTO(MrDeviceDTO mrDeviceDTO) {
    log.debug("Request to getListMrDeviceSoftDTO: {}", mrDeviceDTO);
    Datatable datatable = mrDeviceRepository.getListMrDeviceSoftDTO(mrDeviceDTO);
    List<MrDeviceDTO> mrDeviceDTOList = (List<MrDeviceDTO>) datatable.getData();
    if (mrDeviceDTOList != null && !mrDeviceDTOList.isEmpty()) {
      UserToken userToken = ticketProvider.getUserToken();
      mapUnit.clear();
      setMapUnit();
      for (MrDeviceDTO deviceDTO : mrDeviceDTOList) {
        setDetailValue(deviceDTO);
        deviceDTO.setMrType("1");
        //checkApprove
        if (deviceDTO.getBoUnitHard() != null) {
          log.info(userToken.getDeptId().toString());
          String unitApprove = userRepository.getUnitParentForApprove("1",
              deviceDTO.getBoUnitHard().toString());
          if (userToken.getDeptId() != null && unitApprove != null
              && userToken.getDeptId().equals(Long.parseLong(unitApprove))
              && deviceDTO.getApproveStatusHard() != null && deviceDTO.getApproveStatusHard()
              .equals(0L)) {
            UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
            if (unitDTO.getIsCommittee().equals(0L)) {
              if (userRepository.checkRoleOfUser("TP", userToken.getUserID())) {
                deviceDTO.setCheckApprove(1L);
              }
            }
          }
        }
      }
      datatable.setData(mrDeviceDTOList);
    }
    return datatable;
  }

  public void setDetailValue(MrDeviceDTO mrDeviceDTO) {
    String implementUnitId = mrDeviceDTO.getImplementUnit();
    if (StringUtils.isNotNullOrEmpty(implementUnitId) && mapUnit
        .containsKey(Long.valueOf(implementUnitId))) {
      mrDeviceDTO.setImplementUnitName(mapUnit.get(Long.valueOf(implementUnitId)).getUnitName());
    }
    String checkingUnitId = mrDeviceDTO.getCheckingUnit();
    if (StringUtils.isNotNullOrEmpty(checkingUnitId) && mapUnit
        .containsKey(Long.valueOf(checkingUnitId))) {
      mrDeviceDTO.setCheckingUnitName(mapUnit.get(Long.valueOf(checkingUnitId)).getUnitName());
    }
    Long approveStatus = mrDeviceDTO.getApproveStatusHard();
    if (approveStatus != null) {
      if (approveStatus.equals(0L)) {
        mrDeviceDTO.setApproveStatusHardName(I18n.getLanguage("mrDevice.approveStatus.0"));
      } else if (approveStatus.equals(1L)) {
        mrDeviceDTO.setApproveStatusHardName(I18n.getLanguage("mrDevice.approveStatus.1"));
      } else if (approveStatus.equals(2L)) {
        mrDeviceDTO.setApproveStatusHardName(I18n.getLanguage("mrDevice.approveStatus.2"));
      }
    }
    String dateIntegratedStr = mrDeviceDTO.getDateIntegratedStr();
    if (dateIntegratedStr != null && dateIntegratedStr.length() > 10) {
      mrDeviceDTO.setDateIntegratedStr(dateIntegratedStr.substring(0, 10));
    }
  }

  public void setMapUnit() {
    List<UnitDTO> listUnit = unitRepository.getListUnit(null);
    if (listUnit != null && listUnit.size() > 0) {
      for (UnitDTO dto : listUnit) {
        if (!mapUnit.containsKey(dto.getUnitId())) {
          mapUnit.put(dto.getUnitId(), dto);
        }
      }
    }
  }

  @Override
  public List<MrDeviceDTO> getListRegionByMarketCode(String marketCode) {
    log.debug("Request to getListRegionByMarketCode: {}", marketCode);
    return mrDeviceRepository.getListRegionByMarketCode(marketCode);
  }

  @Override
  public ResultInSideDto updateMrDeviceHard(MrDeviceDTO mrDeviceDTO, boolean checkIsCommittee, boolean isImport) {
    log.debug("Request to edit: {}", mrDeviceDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    MrDeviceDTO mrDeviceOld = mapDeviceInDB.get(String.valueOf(mrDeviceDTO.getDeviceId()));
    UserToken userToken = userTokenImport;
    if (!isImport || userToken == null) {
      userToken = ticketProvider.getUserToken();
    }
    if (!isImport || mrDeviceOld == null) {//cho update from web
      mrDeviceOld = mrDeviceSoftRepository.findMrDeviceById(mrDeviceDTO.getDeviceId());
    }
    if (mrDeviceOld != null) {
      mrDeviceDTO.setUpdateDate(new Date());
      mrDeviceDTO.setUpdateUser(userToken.getUserName());
      mrDeviceDTO.setIsComplete1m(
          StringUtils.isStringNullOrEmpty(mrDeviceOld.getIsComplete1m()) ? null
              : mrDeviceOld.getIsComplete1m());
      mrDeviceDTO.setIsComplete3m(
          StringUtils.isStringNullOrEmpty(mrDeviceOld.getIsComplete3m()) ? null
              : mrDeviceOld.getIsComplete3m());
      mrDeviceDTO.setIsComplete6m(
          StringUtils.isStringNullOrEmpty(mrDeviceOld.getIsComplete6m()) ? null
              : mrDeviceOld.getIsComplete6m());
      mrDeviceDTO.setIsComplete12m(
          StringUtils.isStringNullOrEmpty(mrDeviceOld.getIsComplete12m()) ? null
              : mrDeviceOld.getIsComplete12m());
      List<MrScheduleTelDTO> listScheduleTel = new ArrayList<>();
      List<MrScheduleTelDTO> listScheduleTelInActive = new ArrayList<>();
      MrScheduleTelDTO scheduleTelDTO = new MrScheduleTelDTO();
      scheduleTelDTO.setDeviceId(mrDeviceDTO.getDeviceId());
      scheduleTelDTO.setDeviceCode(mrDeviceDTO.getNodeCode());
      scheduleTelDTO.setMrHard("1");
      List<MrScheduleTelDTO> listSchedule = mrScheduleTelRepository
          .getListMrScheduleTelDTO(scheduleTelDTO);

      //dungpv edit
      if (!isImport) {
        if (mrDeviceDTO.getBoUnitHard() != null) {
          String unitApprove = userRepository.getUnitParentForApprove("1",
              mrDeviceDTO.getBoUnitHard().toString());
          if (userToken.getDeptId() != null && unitApprove != null
              && userToken.getDeptId().equals(Long.parseLong(unitApprove))) {
            UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
            if (unitDTO != null && unitDTO.getIsCommittee().equals(0L)) {
              if (userRepository.checkRoleOfUser("TP", userToken.getUserID())) {
                checkIsCommittee = true;
              }
            }
          }
        }
      }
//      từ không bảo dưỡng về có bảo dưỡng
      if ("1".equals(mrDeviceDTO.getMrHard()) && StringUtils
          .isStringNullOrEmpty(mrDeviceDTO.getMrConfirmHard())) {
        mrDeviceDTO.setMrConfirmHard("");
        if (checkIsCommittee) {
          mrDeviceDTO.setApproveStatusHard(1L);
          mrDeviceDTO.setApproveReasonHard("");
          mrDeviceDTO.setMrConfirmHard(null);
        }
//        từ có bảo dưỡng về không bảo dưỡng
      } else if ("0".equals(mrDeviceDTO.getMrHard()) && StringUtils
          .isNotNullOrEmpty(mrDeviceDTO.getMrConfirmHard())) {
        if (checkIsCommittee) {
          mrDeviceDTO.setApproveStatusHard(1L);
          mrDeviceDTO.setApproveReasonHard("");
        }
      }
      //dungpv end

      if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getMrConfirmHard()) && !mrDeviceDTO
          .getMrConfirmHard().equals(mrDeviceOld.getMrConfirmHard())) {
        if (listSchedule != null && !listSchedule.isEmpty()) {
          mrDeviceDTO.setCheckNode(true);
          if (checkIsCommittee) {
            mrDeviceDTO.setApproveStatusHard(1L);
            insertMrScheduleTelHis(listSchedule, mrDeviceDTO); //sinh lịch sử từ list Lịch

            for (MrScheduleTelDTO dtoUpdate : listSchedule) {
              checkCycleHard(mrDeviceDTO, dtoUpdate); //set lại iscomplete theo cycle
            }
          }
          checkIsComplete = false;
        }
      }
      processAttention(mrDeviceDTO, mrDeviceOld, listScheduleTel);
      inActive(mrDeviceDTO, listScheduleTelInActive);
      if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getMrConfirmHard()) && !mrDeviceDTO
          .getMrConfirmHard().equals(mrDeviceOld.getMrConfirmHard())
          && checkIsCommittee) {
        if (listSchedule != null && !listSchedule.isEmpty()) {
          List<MrScheduleTelDTO> lstMr = new ArrayList<>();
          for (MrScheduleTelDTO dtoUpdate : listSchedule) {
            if (!StringUtils.isStringNullOrEmpty(dtoUpdate.getMrId())) {
              lstMr.add(dtoUpdate);
            }
          }
          if (!lstMr.isEmpty()) {
            for (MrScheduleTelDTO mr : lstMr) {
              MrInsideDTO mrInsideDTO = mrRepository.findMrById(mr.getMrId());
              if (mrInsideDTO != null) {
                resultInSideDto = mrRepository.deleteMr(Long.valueOf(mr.getMrId()));
                if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                  throw new RuntimeException("Error occur while delete data in MR");
                }
                //move MR sang MR HIS
                MrHisDTO mrHis = new MrHisDTO();
                mrHis.setMrId(String.valueOf(mr.getMrId()));
                mrHis.setStatus(mrInsideDTO.getState());
                mrHis.setActionCode("3");
                mrHis.setChangeDate(DateTimeUtils.getSysDateTime());
                mrHis.setNotes("AUTO_DELETE_MR_AND_MOVE_MR_TO_HIS");
                mrHis.setComments(I18n.getLanguage("mrScheduleTelhis.updateSchedule.mrHisComment"));
                resultInSideDto = mrHisRepository.insertMrHis(mrHis);
                if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                  throw new RuntimeException("Error occur while insert data to MR_HIS");
                }
              }
            }
          }
        }
      }
      if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getMrConfirmHard()) && checkIsCommittee) {
        if (listSchedule.size() > 0) {
          mrScheduleTelRepository.deleteListSchedule(listSchedule);
        }
      } else if ("0".equals(mrDeviceDTO.getStatus())) {
        if (listScheduleTelInActive.size() > 0) {
          mrScheduleTelRepository.deleteListSchedule(listScheduleTelInActive);
        }
      } else if (!mrDeviceDTO.getRegionHard().equals(mrDeviceOld.getRegionHard()) || !mrDeviceDTO
          .getUserMrHard().equalsIgnoreCase(mrDeviceOld.getUserMrHard()) || !mrDeviceDTO
          .getStationCode().equalsIgnoreCase(mrDeviceOld.getStationCode())) {
        if (listScheduleTel.size() > 0) {
          mrScheduleTelRepository.deleteListSchedule(listScheduleTel);
        }
      }
      if (!mrDeviceDTO.getMrHard().equals(mrDeviceOld.getMrHard()) && !checkIsCommittee) {
        mrDeviceDTO.setApproveStatusHard(0L);
        mrDeviceDTO.setApproveReasonHard("");
        mrDeviceDTO
            .setMrHard(mrDeviceOld.getMrHard());//giu nguyen trang thai bao duong cua thiet bi
      }
      resultInSideDto = mrDeviceRepository.edit(mrDeviceDTO);
    }
    return resultInSideDto;
  }

  private void inActive(MrDeviceDTO mrDeviceDTO, List<MrScheduleTelDTO> inActiveList) {
    MrScheduleTelDTO scheduleTelDTO = new MrScheduleTelDTO();
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getStatus())) {
      if ("0".equals(mrDeviceDTO.getStatus())) {
        scheduleTelDTO.setDeviceId(mrDeviceDTO.getDeviceId());
        scheduleTelDTO.setMrHard("1");
        List<MrScheduleTelDTO> listSchedule = mrScheduleTelRepository
            .getListMrScheduleTelDTO(scheduleTelDTO);
        List<MrScheduleTelHisDTO> listMrScheduleTelHis = new ArrayList();
        if (listSchedule != null && listSchedule.size() > 0) {
          for (MrScheduleTelDTO schedule : listSchedule) {
            listMrScheduleTelHis.add(convertScheduleTelHis(schedule, 0));
            inActiveList.add(schedule);
            checkCycleHard(mrDeviceDTO, schedule);
          }
          if (listMrScheduleTelHis.size() > 0) {
            mrScheduleTelHisRepository.insertUpdateListScheduleHis(listMrScheduleTelHis);
          }
        }
      }
    }
  }

  private void processAttention(MrDeviceDTO mrDeviceDTO, MrDeviceDTO mrDeviceOld,
      List<MrScheduleTelDTO> listScheduleTel) {
    MrScheduleTelDTO scheduleTelDTO = new MrScheduleTelDTO();
    scheduleTelDTO.setDeviceId(mrDeviceDTO.getDeviceId());
    scheduleTelDTO.setDeviceCode(mrDeviceDTO.getNodeCode());
    scheduleTelDTO.setMrHard("1");
    List<MrScheduleTelDTO> listSchedule = mrScheduleTelRepository
        .getListMrScheduleTelDTO(scheduleTelDTO);
    boolean checkRegion = true;
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getRegionHard()) || StringUtils
        .isNotNullOrEmpty(mrDeviceDTO.getUserMrHard()) || !StringUtils
        .isStringNullOrEmpty(mrDeviceDTO.getStationCode())) {
      if (StringUtils.isStringNullOrEmpty(mrDeviceOld.getUserMrHard())) {
        mrDeviceOld.setUserMrHard("");
      }
      if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getUserMrHard())) {
        mrDeviceDTO.setUserMrHard("");
      }
      if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getStationCode())) {
        mrDeviceDTO.setStationCode("");
      }
      if (!mrDeviceDTO.getRegionHard().equals(mrDeviceOld.getRegionHard()) || !mrDeviceDTO
          .getUserMrHard().equalsIgnoreCase(mrDeviceOld.getUserMrHard()) || !mrDeviceDTO
          .getStationCode().equalsIgnoreCase(mrDeviceOld.getStationCode())) {
        List<MrScheduleTelHisDTO> listMrScheduleTelHis = new ArrayList();
        if (listSchedule != null && listSchedule.size() > 0) {
          // cap nhap lich sang mr_schedule_tel_his
          for (MrScheduleTelDTO schedule : listSchedule) {
            if (schedule.getMrId() == null && schedule.getWoId() == null) {
              listMrScheduleTelHis.add(convertScheduleTelHis(schedule, 1));
              listScheduleTel.add(schedule);
              checkCycleHard(mrDeviceDTO, schedule);
              checkRegion = false;
            } else {
              if (schedule.getMrId() != null && schedule.getWoId() != null) {
                MrInsideDTO mrInsideDTO = mrRepository.findMrById(schedule.getMrId());
                WoInsideDTO woInsideDTO = woServiceProxy.findWoByIdProxy(schedule.getWoId());
                if (mrInsideDTO != null && woInsideDTO != null) {
                  if (StringUtils.isNotNullOrEmpty(mrInsideDTO.getState()) && !StringUtils
                      .isStringNullOrEmpty(woInsideDTO.getStatus())) {
                    if (mrInsideDTO.getState().equals(Constants.MR_STATE_CODE.CLOSE) && woInsideDTO
                        .getStatus().toString().equals(WO_STATE.CLOSED_CD)) {
                      checkCycleHard(mrDeviceDTO, schedule);
                      mrDeviceRepository.edit(mrDeviceDTO);
                    }
                  }
                }
              }
            }
          }
          if (listMrScheduleTelHis.size() > 0) {
            mrScheduleTelHisRepository.insertUpdateListScheduleHis(listMrScheduleTelHis);
          }
        }
      }
      if (checkIsComplete) {
        if (checkRegion) {
          updateIsComplete(listSchedule, mrDeviceDTO, mrDeviceOld);
        }
      }
    }
  }

  private void updateIsComplete(List<MrScheduleTelDTO> schedules, MrDeviceDTO dto,
      MrDeviceDTO dtoOld) {
    if (schedules != null && !schedules.isEmpty()) {
//      MrScheduleTelDTO dtoUpdate = schedules.get(0);
      for (MrScheduleTelDTO dtoUpdate : schedules) {
        if ("M".equals(dtoUpdate.getCycleType())) {
          if ("1".equals(dtoUpdate.getCycle())) {
            if ("0".equals(String.valueOf(dtoOld.getIsComplete1m()))) {
              dto.setIsComplete1m(0L);
            }
          } else if ("3".equals(dtoUpdate.getCycle())) {
            if ("0".equals(String.valueOf(dtoOld.getIsComplete3m()))) {
              dto.setIsComplete3m(0L);
            }
          } else if ("6".equals(dtoUpdate.getCycle())) {
            if ("0".equals(String.valueOf(dtoOld.getIsComplete6m()))) {
              dto.setIsComplete6m(0L);
            }
          } else if ("12".equals(dtoUpdate.getCycle())) {
            if ("0".equals(String.valueOf(dtoOld.getIsComplete12m()))) {
              dto.setIsComplete12m(0L);
            }
          }
        }
      }
    } else {
      if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete1m()) && "1"
          .equals(String.valueOf(dtoOld.getIsComplete1m()))) {
        dto.setIsComplete1m(1L);
      } else if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete1m()) && "0"
          .equals(String.valueOf(dtoOld.getIsComplete1m()))) {
        dto.setIsComplete1m(0L);
      }
      if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete3m()) && "1"
          .equals(String.valueOf(dtoOld.getIsComplete3m()))) {
        dto.setIsComplete3m(1L);
      } else if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete3m()) && "0"
          .equals(String.valueOf(dtoOld.getIsComplete3m()))) {
        dto.setIsComplete3m(0L);
      }
      if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete6m()) && "1"
          .equals(String.valueOf(dtoOld.getIsComplete6m()))) {
        dto.setIsComplete6m(1L);
      } else if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete6m()) && "0"
          .equals(String.valueOf(dtoOld.getIsComplete6m()))) {
        dto.setIsComplete6m(0L);
      }
      if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete12m()) && "1"
          .equals(String.valueOf(dtoOld.getIsComplete12m()))) {
        dto.setIsComplete12m(1L);
      } else if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete12m()) && "0"
          .equals(String.valueOf(dtoOld.getIsComplete12m()))) {
        dto.setIsComplete12m(0L);
      }

    }
  }

  public MrScheduleTelHisDTO convertScheduleTelHis(MrScheduleTelDTO dto, int type) {
    MrScheduleTelHisDTO mrScheduleTelHisDTO = new MrScheduleTelHisDTO();
    mrScheduleTelHisDTO.setArrayCode(dto.getArrayCode());
    mrScheduleTelHisDTO.setArrayName(dto.getArrayName());
    mrScheduleTelHisDTO.setCrId(dto.getCrId() != null ? String.valueOf(dto.getCrId()) : null);
    mrScheduleTelHisDTO.setCrNumber(dto.getCrNumber());
    mrScheduleTelHisDTO.setCycle(dto.getCycle());
    mrScheduleTelHisDTO.setDeviceCode(dto.getDeviceCode());
//    mrScheduleTelHisDTO.setDefaultSortField(dto.getDefaultSortField());
    mrScheduleTelHisDTO
        .setDeviceId(dto.getDeviceId() != null ? String.valueOf(dto.getDeviceId()) : null);
    mrScheduleTelHisDTO.setDeviceName(dto.getDeviceName());
    mrScheduleTelHisDTO.setDeviceType(dto.getDeviceType());
    mrScheduleTelHisDTO.setMarketCode(dto.getMarketCode());
    mrScheduleTelHisDTO.setMarketName(dto.getMarketName());
    mrScheduleTelHisDTO.setNetworkType(dto.getNetworkType());
    mrScheduleTelHisDTO.setMrId(dto.getMrId() != null ? String.valueOf(dto.getMrId()) : null);
    mrScheduleTelHisDTO.setMrType(dto.getMrType());
    mrScheduleTelHisDTO.setMrDate(dto.getNextDateModify() != null ? DateTimeUtils
        .date2ddMMyyyyHHMMss(dto.getNextDateModify()) : null);
    mrScheduleTelHisDTO.setMrContent(dto.getMrContentId());
    mrScheduleTelHisDTO.setMrMode("H");
    mrScheduleTelHisDTO
        .setCrNumber(genCrNumber(dto.getCrId(), dto.getTypeCr(), dto.getArrayActionName()));
    mrScheduleTelHisDTO
        .setProcedureId(dto.getProcedureId() != null ? String.valueOf(dto.getProcedureId()) : null);
    mrScheduleTelHisDTO.setProcedureName(dto.getProcedureName());
    mrScheduleTelHisDTO.setRegion(dto.getRegion());
    if (type == 1) {
      mrScheduleTelHisDTO.setNote(I18n.getLanguage("mrDevice.node.hard"));
    } else {
      mrScheduleTelHisDTO.setNote(I18n.getLanguage("mrDevice.node.status"));
    }
    return mrScheduleTelHisDTO;
  }

  private ResultInSideDto insertMrScheduleTelHis(List<MrScheduleTelDTO> listScheduleTel,
      MrDeviceDTO mrDeviceDTO) {
    MrInsideDTO mrInsideDTO = new MrInsideDTO();
    List<MrScheduleTelHisDTO> listScheduleTelHis = new ArrayList<>();
    MrScheduleTelDTO scheduleTelDTO = listScheduleTel.get(0);
    mrInsideDTO.setMrId(scheduleTelDTO.getMrId());
    if (mrInsideDTO.getMrId() != null) {
      mrInsideDTO = mrRepository.findMrById(mrInsideDTO.getMrId());
    }
    for (MrScheduleTelDTO telDTO : listScheduleTel) {
      MrScheduleTelHisDTO scheduleHisDTO = new MrScheduleTelHisDTO();
      scheduleHisDTO.setMarketCode(telDTO.getMarketCode());
      scheduleHisDTO.setArrayCode(telDTO.getArrayCode());
      scheduleHisDTO.setDeviceType(telDTO.getDeviceType());
      scheduleHisDTO
          .setDeviceId(
              telDTO.getDeviceId() != null ? String.valueOf(telDTO.getDeviceId()) : null);
      scheduleHisDTO.setDeviceCode(telDTO.getDeviceCode());
      scheduleHisDTO.setDeviceName(telDTO.getDeviceName());
      scheduleHisDTO.setMrDate(telDTO.getNextDateModify() != null ? DateTimeUtils
          .date2ddMMyyyyHHMMss(telDTO.getNextDateModify()) : null);
      scheduleHisDTO.setMrContent(mrInsideDTO == null ? null
          : mrInsideDTO.getMrContentId());
      scheduleHisDTO.setMrMode("H");
      scheduleHisDTO.setMrType(mrInsideDTO == null ? null
          : mrInsideDTO.getMrType());
      scheduleHisDTO.setMrId(telDTO.getMrId() != null ? String.valueOf(telDTO.getMrId()) : null);
      scheduleHisDTO.setMrCode(mrInsideDTO == null ? null
          : mrInsideDTO.getMrCode());
      scheduleHisDTO.setCrId(telDTO.getCrId() != null ? String.valueOf(telDTO.getCrId()) : null);
      if (telDTO.getCrId() != null) {
        scheduleHisDTO
            .setCrNumber(
                genCrNumber(telDTO.getCrId(), telDTO.getTypeCr(), telDTO.getArrayActionName()));
      }
      scheduleHisDTO.setProcedureId(
          telDTO.getProcedureId() != null ? String.valueOf(telDTO.getProcedureId()) : null);
      scheduleHisDTO.setProcedureName(telDTO.getProcedureName());
      scheduleHisDTO.setNetworkType(telDTO.getNetworkType());
      scheduleHisDTO.setCycle(telDTO.getCycle());
      scheduleHisDTO.setRegion(telDTO.getRegion());
      scheduleHisDTO.setTitle(telDTO.getMrComment());
      if (mrDeviceDTO.getCheckNode() != null && mrDeviceDTO.getCheckNode()) {
        scheduleHisDTO.setNote(I18n.getLanguage("mrScheduleTelhis.note.mrConfirm"));
      } else {
        scheduleHisDTO.setNote(I18n.getLanguage("mrScheduleTelhis.note.delete"));
      }
      listScheduleTelHis.add(scheduleHisDTO);
    }
    return mrScheduleTelHisRepository.insertUpdateListScheduleHis(listScheduleTelHis);
  }

  public String genCrNumber(Long crId, String sTypeCr, String sArrayActionName) {
    String crTypeLong;
    if (crId != null) {
      if (sTypeCr == null) {
        crTypeLong = String.valueOf(Constants.CR_TYPE.STANDARD);
      } else {
        crTypeLong = sTypeCr;
      }
      String crType =
          String.valueOf(Constants.CR_TYPE.NORMAL).equalsIgnoreCase(crTypeLong) ? "NORMAL"
              : String.valueOf(Constants.CR_TYPE.EMERGENCY).equalsIgnoreCase(crTypeLong)
                  ? "EMERGENCY"
                  : String.valueOf(Constants.CR_TYPE.STANDARD).equalsIgnoreCase(crTypeLong)
                      ? "STANDARD" : "";
      String crNumber = "CR_" + crType + "_" + sArrayActionName + "_" + crId;
      return crNumber.toUpperCase(Locale.US);
    }
    return null;
  }

  private void checkCycleHard(MrDeviceDTO mrDeviceDTO, MrScheduleTelDTO schedule) {

    if ("M".equals(schedule.getCycleType())) {
      if ("1".equals(schedule.getCycle())) {
        mrDeviceDTO.setIsComplete1m(1L);
      } else if ("3".equals(schedule.getCycle())) {
        mrDeviceDTO.setIsComplete3m(1L);
      } else if ("6".equals(schedule.getCycle())) {
        mrDeviceDTO.setIsComplete6m(1L);
      } else if ("12".equals(schedule.getCycle())) {
        mrDeviceDTO.setIsComplete12m(1L);
      }
    }
  }

  @Override
  public MrDeviceDTO getDetail(Long deviceId) {
    log.debug("Request to getDetail: {}", deviceId);
    return mrDeviceRepository.getDetail(deviceId);
  }

  @Override
  public ResultInSideDto deleteMrDeviceHard(MrDeviceDTO mrDeviceDTO) {
    log.debug("Request to delete: {}", mrDeviceDTO);
    ResultInSideDto resultInSideDto;
    List<MrScheduleTelDTO> listSchedule = new ArrayList<>();
    MrScheduleTelDTO scheduleTelDTO = new MrScheduleTelDTO();
    scheduleTelDTO.setDeviceId(mrDeviceDTO.getDeviceId());
    scheduleTelDTO.setDeviceCode(mrDeviceDTO.getNodeCode());
    List<MrScheduleTelDTO> schedules = mrScheduleTelRepository
        .getListMrScheduleTelDTO(scheduleTelDTO);
    listSchedule.addAll(schedules);
    resultInSideDto = mrScheduleTelRepository.deleteListSchedule(schedules);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      resultInSideDto = mrDeviceRepository.delete(mrDeviceDTO.getDeviceId());
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        if (!listSchedule.isEmpty()) {
          mrDeviceDTO.setCheckNode(false);
          resultInSideDto = insertMrScheduleTelHis(listSchedule, mrDeviceDTO);
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public File exportData(MrDeviceDTO mrDeviceDTO) throws Exception {
    List<MrDeviceDTO> mrDeviceDTOList = mrDeviceRepository
        .getListDataExport(mrDeviceDTO);
    if (mrDeviceDTOList != null && !mrDeviceDTOList.isEmpty()) {
      mapUnit.clear();
      setMapUnit();
      for (int i = mrDeviceDTOList.size() - 1; i > -1; i--) {
        MrDeviceDTO mrDeviceExport = mrDeviceDTOList.get(i);
        setDetailValue(mrDeviceExport);
        String status = mrDeviceExport.getStatus();
        if (StringUtils.isNotNullOrEmpty(status)) {
          if ("0".equals(status)) {
            mrDeviceExport.setStatus(I18n.getLanguage("mrDevice.status.inActive"));
          } else if ("1".equals(status)) {
            mrDeviceExport.setStatus(I18n.getLanguage("mrDevice.status.active"));
          }
        }
        String mrHard = mrDeviceExport.getMrHard();
        if (StringUtils.isNotNullOrEmpty(mrHard)) {
          if ("1".equals(mrHard)) {
            mrDeviceExport.setMrHard(I18n.getLanguage("mrDevice.mrHardDisplay.yes"));
            mrDeviceExport.setMrConfirmHardStr("");
          } else if ("0".equals(mrHard)) {
            mrDeviceExport.setMrHard(I18n.getLanguage("mrDevice.mrHardDisplay.no"));
          }
        }
        mrDeviceExport.setMrType(I18n.getLanguage("mrDevice.mrType.default"));
      }
    }
    return exportFileTemplate(mrDeviceDTOList, "");
  }

  private List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private File exportFileTemplate(List<MrDeviceDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("mrDevice.export.title");
    String title = I18n.getLanguage("mrDevice.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      headerExportList = readerHeaderSheet("deviceIdStr", "marketName", "regionHard",
          "arrayCodeStr", "networkTypeStr", "deviceTypeStr", "nodeIp", "nodeCode", "deviceName",
          "vendor",
          "statusStr", "dateIntegratedStr", "stationCode", "userMrHard", "mrConfirmHardStr",
          "mrHardStr", "boUnitHardName", "mrTypeStr", "resultImport");
      fileNameOut = MR_DEVICE_RESULT_IMPORT;
      subTitle = null;
    } else {
      headerExportList = readerHeaderSheet("deviceId", "marketName",
          "regionHard",
          "arrayCodeStr", "networkType", "deviceType", "nodeIp", "nodeCode", "deviceName", "vendor",
          "status", "dateIntegrated", "stationCode", "cdIdHardConfigStr", "userMrHard",
          "createUserHard",
          "mrConfirmHardStr",
          "mrHard", "boUnitHardName", "approveStatusHardName", "mrType");
      fileNameOut = MR_DEVICE_EXPORT;
      subTitle = I18n
          .getLanguage("mrDevice.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.mrDevice"
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

  @Override
  public File getTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");
    XSSFSheet sheetParam2 = workbook.createSheet("param2");
    XSSFSheet sheetParam3 = workbook.createSheet("param3");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrDevice.deviceIdStr"),
        I18n.getLanguage("mrDevice.marketName"),
        I18n.getLanguage("mrDevice.regionHard"),
        I18n.getLanguage("mrDevice.arrayCodeStr"),
        I18n.getLanguage("mrDevice.networkTypeStr"),
        I18n.getLanguage("mrDevice.deviceTypeStr"),
        I18n.getLanguage("mrDevice.nodeIp"),
        I18n.getLanguage("mrDevice.nodeCode"),
        I18n.getLanguage("mrDevice.deviceName"),
        I18n.getLanguage("mrDevice.vendor"),
        I18n.getLanguage("mrDevice.statusStr"),
        I18n.getLanguage("mrDevice.dateIntegrated"),
        I18n.getLanguage("mrDevice.stationCode"),
        I18n.getLanguage("mrDevice.userMrHard"),
        I18n.getLanguage("mrDevice.mrConfirmHardStr"),
        I18n.getLanguage("mrDevice.mrHardStr"),
        I18n.getLanguage("mrDevice.boUnitHard"),
        I18n.getLanguage("mrDevice.mrTypeStr")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("mrDevice.marketName"),
        I18n.getLanguage("mrDevice.arrayCodeStr"),
        I18n.getLanguage("mrDevice.networkTypeStr"),
        I18n.getLanguage("mrDevice.deviceTypeStr"),
        I18n.getLanguage("mrDevice.nodeCode"),
        I18n.getLanguage("mrDevice.deviceName"),
        I18n.getLanguage("mrDevice.vendor"),
        I18n.getLanguage("mrDevice.statusStr"),
        I18n.getLanguage("mrDevice.mrHardStr")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int marketNameColumn = listHeader
        .indexOf(I18n.getLanguage("mrDevice.marketName"));
    int arrayCodeStrColumn = listHeader.indexOf(I18n.getLanguage("mrDevice.arrayCodeStr"));
    int statusStrColumn = listHeader.indexOf(I18n.getLanguage("mrDevice.statusStr"));
    int mrConfirmHardStrColumn = listHeader.indexOf(I18n.getLanguage("mrDevice.mrConfirmHardStr"));
    int mrHardStrColumn = listHeader.indexOf(I18n.getLanguage("mrDevice.mrHardStr"));
    int mrTypeStrColumn = listHeader.indexOf(I18n.getLanguage("mrDevice.mrTypeStr"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);
    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("mrDevice.title"));
    titleCell.setCellStyle(style.get("title"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(4);
    Row headerArray = sheetParam2.createRow(0);
    Row headerUnit = sheetParam3.createRow(0);
    headerRow.setHeightInPoints(30);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }
      if (i == 12) {
        headerCell.setCellComment(
            setCommentHeader(workbook, headerCell, i, "Nhập định dạng dd/MM/yyyy HH24:mm:ss"));
      }
      if (i == 17) {
        headerCell.setCellComment(setCommentHeader(workbook, headerCell, i, "Nhập ID đơn vị"));
      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }

    Cell headerCellUnitId = headerUnit.createCell(0);
    Cell headerCellUnit = headerUnit.createCell(1);
    XSSFRichTextString unitId = new XSSFRichTextString(I18n.getLanguage("mrDevice.unitId"));
    XSSFRichTextString unit = new XSSFRichTextString(I18n.getLanguage("mrDevice.unitName"));
    headerCellUnit.setCellValue(unit);
    headerCellUnit.setCellStyle(style.get("header"));
    headerCellUnitId.setCellValue(unitId);
    headerCellUnitId.setCellStyle(style.get("header"));

    Cell headerCellArray = headerArray.createCell(0);
    Cell headerCellNetwork = headerArray.createCell(1);
    Cell headerCellDevice = headerArray.createCell(2);
    XSSFRichTextString array = new XSSFRichTextString(
        I18n.getLanguage("mrDevice.arrayCodeStr"));
    XSSFRichTextString network = new XSSFRichTextString(
        I18n.getLanguage("mrDevice.networkTypeStr"));
    XSSFRichTextString device = new XSSFRichTextString(
        I18n.getLanguage("mrDevice.deviceType"));
    headerCellArray.setCellValue(array);
    headerCellArray.setCellStyle(style.get("header"));
    headerCellNetwork.setCellValue(network);
    headerCellNetwork.setCellStyle(style.get("header"));
    headerCellDevice.setCellValue(device);
    headerCellDevice.setCellStyle(style.get("header"));
    sheetParam2.setColumnWidth(0, 15000);
    sheetParam2.setColumnWidth(1, 15000);
    sheetParam2.setColumnWidth(2, 15000);
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown
    int row = 5;
    List<ItemDataCRInside> lstMarketCode = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    for (ItemDataCRInside dto : lstMarketCode) {
      excelWriterUtils
          .createCell(sheetParam, 2, row++, dto.getDisplayStr(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name marketName = workbook.createName();
    marketName.setNameName("marketName");
    marketName.setRefersToFormula("param!$C$2:$C$" + row);
    XSSFDataValidationConstraint marketNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "marketName");
    CellRangeAddressList marketNameCreate = new CellRangeAddressList(5, 65000,
        marketNameColumn,
        marketNameColumn);
    XSSFDataValidation dataValidationMarketName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            marketNameConstraint, marketNameCreate);
    dataValidationMarketName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationMarketName);

    row = 5;
    List<CatItemDTO> lstArrayCode = mrCfgProcedureCDBusiness.getMrSubCategory();
    for (CatItemDTO dto : lstArrayCode) {
      excelWriterUtils
          .createCell(sheetParam, 4, row++, dto.getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name arrayCodeStr = workbook.createName();
    arrayCodeStr.setNameName("arrayCodeStr");
    arrayCodeStr.setRefersToFormula("param!$E$2:$E$" + row);
    XSSFDataValidationConstraint arrayCodeStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "arrayCodeStr");
    CellRangeAddressList arrayCodeStrCreate = new CellRangeAddressList(5, 65000, arrayCodeStrColumn,
        arrayCodeStrColumn);
    XSSFDataValidation dataValidationArrayCodeStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            arrayCodeStrConstraint, arrayCodeStrCreate);
    dataValidationArrayCodeStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationArrayCodeStr);

    row = 1;
    List<MrDeviceDTO> lstANT = mrDeviceRepository.getMrDeviceByA_N_T();
    Map<String, String> mapAN = new HashMap<>();
    Map<String, String> mapANT = new HashMap<>();
    Map<String, String> mapArrCodeEN = new HashMap<>();
    for (CatItemDTO dto : lstArrayCode) {
      mapArrCodeEN.put(dto.getItemValue(), dto.getItemName());
    }
    if (lstANT != null && !lstANT.isEmpty()) {
      for (MrDeviceDTO item : lstANT) {
        if (!mapAN.containsKey(item.getArrayCode())) {
          if (row > 1) {
            excelWriterUtils
                .createCell(sheetParam2, 0, row, "", style.get("header"));
            excelWriterUtils
                .createCell(sheetParam2, 1, row, "", style.get("header"));
            excelWriterUtils
                .createCell(sheetParam2, 2, row, "", style.get("header"));
            row++; // them ngan cach
          }
          String valueDefault = item.getArrayCode();
          if (mapArrCodeEN.containsKey(valueDefault)) {
            valueDefault = mapArrCodeEN.get(valueDefault);
          }
          excelWriterUtils
              .createCell(sheetParam2, 0, row, valueDefault, style.get("cell"));
        }
        if (!mapANT.containsKey(item.getArrayCode() + ";" + item.getNetworkType())) {
          excelWriterUtils
              .createCell(sheetParam2, 1, row, item.getNetworkType(), style.get("cell"));
        }
        excelWriterUtils
            .createCell(sheetParam2, 2, row++, item.getDeviceType(), style.get("cell"));
        mapAN.put(item.getArrayCode(), item.getNetworkType());
        mapANT.put(item.getArrayCode() + ";" + item.getNetworkType(), item.getDeviceType());
      }
    }
    /*
    for (CatItemDTO arrayCode : lstArrayCode) {
      List<MrDeviceDTO> lstNetwork = mrDeviceRepository
          .getListNetworkTypeByArrayCode(arrayCode.getItemValue());
      excelWriterUtils
          .createCell(sheetParam2, 0, row, arrayCode.getItemName(), style.get("cell"));
      for (MrDeviceDTO networkCode : lstNetwork) {
        List<MrDeviceDTO> lstDeviceType = mrDeviceRepository
            .getListDeviceTypeByNetworkType(arrayCode.getItemValue(), networkCode.getNetworkType());
        excelWriterUtils
            .createCell(sheetParam2, 1, row, networkCode.getNetworkType(), style.get("cell"));
        for (MrDeviceDTO deviceType : lstDeviceType) {
          excelWriterUtils
              .createCell(sheetParam2, 2, row++, deviceType.getDeviceType(), style.get("cell"));
        }
        excelWriterUtils
            .createCell(sheetParam2, 0, row++, null, style.get("cell"));
      }
      excelWriterUtils
          .createCell(sheetParam2, 0, row++, null, style.get("cell"));
    }
    */
    sheetParam2.autoSizeColumn(0);
    sheetParam2.autoSizeColumn(1);

    row = 5;
    excelWriterUtils.createCell(sheetParam, 11, row++, I18n.getLanguage("mrDevice.status.0")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 11, row++, I18n.getLanguage("mrDevice.status.1")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name statusStr = workbook.createName();
    statusStr.setNameName("statusStr");
    statusStr.setRefersToFormula("param!$L$2:$L$" + row);
    XSSFDataValidationConstraint statusStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "statusStr");
    CellRangeAddressList statusStrCreate = new CellRangeAddressList(5, 65000, statusStrColumn,
        statusStrColumn);
    XSSFDataValidation dataValidationStatusStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            statusStrConstraint, statusStrCreate);
    dataValidationStatusStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationStatusStr);

    row = 5;
    List<MrConfigDTO> lstMrConfirmHardStr = mrScheduleTelRepository.getConfigByGroup("LY_DO_KO_BD");
    for (MrConfigDTO dto : lstMrConfirmHardStr) {
      excelWriterUtils
          .createCell(sheetParam, 15, row++, dto.getConfigName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name mrConfirmHardStr = workbook.createName();
    mrConfirmHardStr.setNameName("mrConfirmHardStr");
    mrConfirmHardStr.setRefersToFormula("param!$P$2:$P$" + row);
    XSSFDataValidationConstraint mrConfirmHardStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "mrConfirmHardStr");
    CellRangeAddressList mrConfirmHardStrCreate = new CellRangeAddressList(5, 65000,
        mrConfirmHardStrColumn,
        mrConfirmHardStrColumn);
    XSSFDataValidation dataValidationMrConfirmHardStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            mrConfirmHardStrConstraint, mrConfirmHardStrCreate);
    dataValidationMrConfirmHardStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationMrConfirmHardStr);

    row = 5;
    excelWriterUtils.createCell(sheetParam, 16, row++, I18n.getLanguage("mrDevice.mrHardStr.0")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 16, row++, I18n.getLanguage("mrDevice.mrHardStr.1")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name mrHardStr = workbook.createName();
    mrHardStr.setNameName("mrHardStr");
    mrHardStr.setRefersToFormula("param!$Q$2:$Q$" + row);
    XSSFDataValidationConstraint mrHardStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "mrHardStr");
    CellRangeAddressList mrHardStrCreate = new CellRangeAddressList(5, 65000, mrHardStrColumn,
        mrHardStrColumn);
    XSSFDataValidation dataValidationMrHardStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            mrHardStrConstraint, mrHardStrCreate);
    dataValidationMrHardStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationMrHardStr);

    row = 5;
    excelWriterUtils.createCell(sheetParam, 18, row++, I18n.getLanguage("mrDevice.mrTypeStr.BD")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name mrTypeStr = workbook.createName();
    mrTypeStr.setNameName("mrTypeStr");
    mrTypeStr.setRefersToFormula("param!$S$2:$S$" + row);
    XSSFDataValidationConstraint mrTypeStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "mrTypeStr");
    CellRangeAddressList mrTypeStrCreate = new CellRangeAddressList(5, 65000, mrTypeStrColumn,
        mrTypeStrColumn);
    XSSFDataValidation dataValidationMrTypeStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            mrTypeStrConstraint, mrTypeStrCreate);
    dataValidationMrTypeStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationMrTypeStr);

    row = 1;
    List<UnitDTO> unitNameList = unitRepository.getListUnit(null);
    for (UnitDTO dto : unitNameList) {
      excelWriterUtils
          .createCell(sheetParam3, 0, row, dto.getUnitId().toString(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam3, 1, row++, dto.getUnitName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);
    sheetParam.autoSizeColumn(1);
    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("mrDevice.title"));
    workbook.setSheetName(2, I18n.getLanguage("mrDevice.arrNet"));
    workbook.setSheetName(3, I18n.getLanguage("mrDevice.boUnitHard"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MR_DEVICE" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  private Comment setCommentHeader(XSSFWorkbook workbook, Cell headerCell, int col,
      String content) {
    Drawing drawing = headerCell.getSheet().createDrawingPatriarch();
    CreationHelper factory = workbook.getCreationHelper();
    ClientAnchor anchor = factory.createClientAnchor();
    anchor.setCol1(col + 1);
    anchor.setCol2(col + 2);
    anchor.setRow1(3);
    anchor.setRow2(4);
    Comment comment = drawing.createCellComment(anchor);
    comment.setString(factory.createRichTextString(content));
    return comment;
  }

  private void setMapUser() {
    List<UsersInsideDto> userDTOList = userRepository.getListUsersDTOS(new UsersInsideDto());
    if (userDTOList != null && userDTOList.size() > 0) {
      for (UsersInsideDto dto : userDTOList) {
        if (!mapUser.containsKey(dto.getUsername())) {
          mapUser.put(dto.getUsername(), dto);
        }
      }
    }
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    clearMapData();
    List<MrDeviceDTO> mrDeviceDTOList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    setMapUser();
    setMapUnit();
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      } else {
        if (!(multipartFile.getOriginalFilename().toUpperCase().endsWith(".XLSX") || multipartFile
            .getOriginalFilename().toUpperCase().endsWith(".XLS"))) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);

        File fileImport = new File(filePath);

        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            18,
            1000
        );
        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORM);
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            18,
            1000
        );
        if (dataImportList.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }
        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          setMapMarketName();
          setMapArray();
          setMrConfirmHardStr();
          List<String> lstDeviceIdsToMap = new ArrayList<>();
          List<String> lstArrayCodesToMap = new ArrayList<>();
          List<String> lstNetWorkTypeToMap = new ArrayList<>();
          List<String> lstDeviceTypeToMap = new ArrayList<>();
          for (Object[] obj : dataImportList) {
            if (!StringUtils.isStringNullOrEmpty(obj[1])) {
              if (DataUtil.isInteger(obj[1].toString().trim())) {
                  lstDeviceIdsToMap.add(obj[1].toString().trim());
              }
            }

            if (!StringUtils.isStringNullOrEmpty(obj[4])) {
              for (Map.Entry<String, String> item : mapArray.entrySet()) {
                if (obj[4].toString().trim().equals(item.getValue())) {
                  if (!lstArrayCodesToMap.contains(item.getKey())) {
                      lstArrayCodesToMap.add(item.getKey());
                  }
                  break;
                }
              }
            }
            if (!StringUtils.isStringNullOrEmpty(obj[5])) {
              if (!lstNetWorkTypeToMap.contains(obj[5].toString().trim())) {
                lstNetWorkTypeToMap.add(obj[5].toString().trim());
              }
            }
            if (!StringUtils.isStringNullOrEmpty(obj[6])) {
              if (!lstDeviceTypeToMap.contains(obj[6].toString().trim())) {
                lstDeviceTypeToMap.add(obj[6].toString().trim());
              }
            }
          }
          if (lstDeviceIdsToMap != null && lstDeviceIdsToMap.size() > 0) {
            mapDeviceInDB = mrDeviceRepository.getMapMrDevice(lstDeviceIdsToMap);
          }
          if (lstArrayCodesToMap != null && lstArrayCodesToMap.size() > 0) {
            mrDeviceRepository.getMapArrayCode(lstArrayCodesToMap, mapA_NInDB, mapA_N_DInDB);
          }
          if (lstNetWorkTypeToMap != null && lstNetWorkTypeToMap.size() > 0) {
            mrDeviceRepository.getMapNetWorkType(lstNetWorkTypeToMap, mapA_NInDB, mapA_N_DInDB);
          }
          if (lstDeviceTypeToMap != null && lstDeviceTypeToMap.size() > 0) {
            mrDeviceRepository.getMapDeviceType(lstDeviceTypeToMap, mapA_NInDB, mapA_N_DInDB);
          }

          for (Object[] obj : dataImportList) {
            MrDeviceDTO mrDeviceDTO = new MrDeviceDTO();
            if (!StringUtils.isStringNullOrEmpty(obj[1])) {
              if (!DataUtil.isInteger(obj[1].toString().trim())) {
                if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getResultImport())) {
                  mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.errType.deviceIdStr"));
                  mrDeviceDTO.setDeviceIdStr(obj[1].toString().trim());
                } else {
                  mrDeviceDTO.setResultImport(mrDeviceDTO.getResultImport() + "\n" + I18n
                      .getLanguage("mrDevice.errType.deviceIdStr"));
                  mrDeviceDTO.setDeviceIdStr(obj[1].toString().trim());
                }
              } else {
                mrDeviceDTO.setDeviceIdStr(obj[1].toString().trim());
                mrDeviceDTO.setDeviceId(Long.valueOf(obj[1].toString().trim()));
              }
            } else {
              mrDeviceDTO.setDeviceIdStr(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[2])) {
              mrDeviceDTO.setMarketName(obj[2].toString().trim());
              for (Map.Entry<String, String> item : mapMarketName.entrySet()) {
                if (mrDeviceDTO.getMarketName().equals(item.getValue())) {
                  mrDeviceDTO.setMarketCode(item.getKey());
                  break;
                } else {
                  mrDeviceDTO.setMarketCode(null);
                }
              }
            } else {
              mrDeviceDTO.setMarketName(null);
            }
            if (obj[3] != null) {
              mrDeviceDTO.setRegionHard(obj[3].toString().trim());
            } else {
              mrDeviceDTO.setRegionHard(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[4])) {
              mrDeviceDTO.setArrayCodeStr(obj[4].toString().trim());
              for (Map.Entry<String, String> item : mapArray.entrySet()) {
                if (mrDeviceDTO.getArrayCodeStr().equals(item.getValue())) {
                  mrDeviceDTO.setArrayCode(item.getKey());
                  break;
                } else {
                  mrDeviceDTO.setArrayCode(null);
                }
              }
            } else {
              mrDeviceDTO.setArrayCodeStr(null);
            }
            if (obj[5] != null) {
              mrDeviceDTO.setNetworkTypeStr(obj[5].toString().trim());
            } else {
              mrDeviceDTO.setNetworkTypeStr(null);
            }
            if (obj[6] != null) {
              mrDeviceDTO.setDeviceTypeStr(obj[6].toString().trim());
            } else {
              mrDeviceDTO.setDeviceTypeStr(null);
            }
            if (obj[7] != null) {
              mrDeviceDTO.setNodeIp(obj[7].toString().trim());
            } else {
              mrDeviceDTO.setNodeIp(null);
            }
            if (obj[8] != null) {
              mrDeviceDTO.setNodeCode(obj[8].toString().trim().toUpperCase());
            } else {
              mrDeviceDTO.setNodeCode(null);
            }
            if (obj[9] != null) {
              mrDeviceDTO.setDeviceName(obj[9].toString().trim());
            } else {
              mrDeviceDTO.setDeviceName(null);
            }
            if (obj[10] != null) {
              mrDeviceDTO.setVendor(obj[10].toString().trim());
            } else {
              mrDeviceDTO.setVendor(null);
            }
            if (obj[11] != null) {
              mrDeviceDTO.setStatusStr(obj[11].toString().trim());
              if (I18n.getLanguage("mrDevice.status.1")
                  .equals(mrDeviceDTO.getStatusStr())) {
                mrDeviceDTO.setStatus("1");
              } else if (I18n.getLanguage("mrDevice.status.0")
                  .equals(mrDeviceDTO.getStatusStr())) {
                mrDeviceDTO.setStatus("0");
              } else {
                mrDeviceDTO.setStatus(null);
              }
            } else {
              mrDeviceDTO.setStatusStr(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[12])) {
              try {
                String checkDate = DataUtil
                    .validateDateTimeDdMmYyyy_HhMmSs(obj[12].toString().trim());
                if (checkDate != "") {
                  mrDeviceDTO.setDateIntegratedStr(obj[12].toString().trim());
                  mrDeviceDTO.setResultImport(
                      I18n.getLanguage("mrDevice.err.dateIntegratedStr.invalid"));
                } else {
                  mrDeviceDTO
                      .setDateIntegrated(DateUtil.string2DateTime(obj[12].toString().trim()));
                  mrDeviceDTO.setDateIntegratedStr(obj[12].toString().trim());
                }
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                mrDeviceDTO.setDateIntegratedStr(obj[12].toString().trim());
                mrDeviceDTO.setResultImport(
                    I18n.getLanguage("mrDevice.err.dateIntegratedStr.validateForm"));
              }
            } else {
              mrDeviceDTO.setDateIntegratedStr(null);
            }
            if (obj[13] != null) {
              mrDeviceDTO.setStationCode(obj[13].toString().trim());
            } else {
              mrDeviceDTO.setStationCode(null);
            }
            if (obj[14] != null) {
              mrDeviceDTO.setUserMrHard(obj[14].toString().trim());
            } else {
              mrDeviceDTO.setUserMrHard(null);
            }
            if (obj[15] != null) {
              mrDeviceDTO.setMrConfirmHardStr(obj[15].toString().trim());
              for (Map.Entry<String, String> item : mapMrConfirmHard.entrySet()) {
                if (mrDeviceDTO.getMrConfirmHardStr().equals(item.getValue())) {
                  mrDeviceDTO.setMrConfirmHard(item.getKey());
                  break;
                } else {
                  mrDeviceDTO.setMrConfirmHard(null);
                }
              }
            } else {
              mrDeviceDTO.setMrConfirmHardStr(null);
            }
            if (obj[16] != null) {
              mrDeviceDTO.setMrHardStr(obj[16].toString().trim());
              if (I18n.getLanguage("mrDevice.mrHardStr.0")
                  .equals(mrDeviceDTO.getMrHardStr())) {
                mrDeviceDTO.setMrHard("0");
              } else if (I18n.getLanguage("mrDevice.mrHardStr.1")
                  .equals(mrDeviceDTO.getMrHardStr())) {
                mrDeviceDTO.setMrHard("1");
              } else {
                mrDeviceDTO.setMrHard(null);
              }
            } else {
              mrDeviceDTO.setMrHardStr(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[17])) {
              if (!DataUtil.isInteger(obj[17].toString().trim())) {
                if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getResultImport())) {
                  mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.boUnit.exist"));
                  mrDeviceDTO.setBoUnitHardName(obj[17].toString().trim());
                } else {
                  mrDeviceDTO.setResultImport(mrDeviceDTO.getResultImport() + "\n" + I18n
                      .getLanguage("mrDevice.err.boUnit.exist"));
                  mrDeviceDTO.setBoUnitHardName(obj[17].toString().trim());
                }
              } else {
                mrDeviceDTO.setBoUnitHardName(obj[17].toString().trim());
                mrDeviceDTO.setBoUnitHard(Long.valueOf(obj[17].toString().trim()));
              }
            } else {
              mrDeviceDTO.setBoUnitHardName(null);
            }
            if (obj[18] != null) {
              mrDeviceDTO.setMrTypeStr(obj[18].toString().trim());
              if (I18n.getLanguage("mrDevice.mrTypeStr.BD")
                  .equals(mrDeviceDTO.getMrTypeStr())) {
                mrDeviceDTO.setMrType("1");
              } else {
                mrDeviceDTO.setMrType(null);
              }
            } else {
              mrDeviceDTO.setMrTypeStr(null);
            }
            if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getResultImport())) {
              MrDeviceDTO mrDeviceDTOTmp = validateImportInfo(mrDeviceDTO, mrDeviceDTOList);

              if (mrDeviceDTOTmp.getResultImport() == null) {
                mrDeviceDTOTmp
                    .setResultImport(I18n.getLanguage("mrDevice.result.import"));
                mrDeviceDTOList.add(mrDeviceDTOTmp);
              } else {
                mrDeviceDTOList.add(mrDeviceDTO);
                index++;
              }
            } else {
              mrDeviceDTOList.add(mrDeviceDTO);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (mrDeviceDTOList != null) {
              resultInSideDto = insertImport(mrDeviceDTOList);
              if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                File fileExport = exportFileTemplate(mrDeviceDTOList, Constants.RESULT_IMPORT);
                resultInSideDto.setFile(fileExport);
                resultInSideDto.setFilePath(fileExport.getPath());
              }
            }
          } else {
            File fileExport = exportFileTemplate(mrDeviceDTOList,
                Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
            resultInSideDto.setFilePath(fileExport.getPath());
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileTemplate(mrDeviceDTOList, Constants.RESULT_IMPORT);
          resultInSideDto.setFile(fileExport);
          resultInSideDto.setFilePath(fileExport.getPath());
          return resultInSideDto;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
    }
    clearMapData();
    return resultInSideDto;
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
    if (count != 19) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDevice.deviceIdStr")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDevice.marketName") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDevice.regionHard")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDevice.arrayCodeStr") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDevice.networkTypeStr") + "*")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDevice.deviceTypeStr") + "*")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDevice.nodeIp")
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDevice.nodeCode") + "*")
        .equalsIgnoreCase(objects[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDevice.deviceName") + "*")
        .equalsIgnoreCase(objects[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDevice.vendor") + "*")
        .equalsIgnoreCase(objects[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDevice.statusStr") + "*")
        .equalsIgnoreCase(objects[11].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDevice.dateIntegrated")
        .equalsIgnoreCase(objects[12].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDevice.stationCode")
        .equalsIgnoreCase(objects[13].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDevice.userMrHard")
        .equalsIgnoreCase(objects[14].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDevice.mrConfirmHardStr")
        .equalsIgnoreCase(objects[15].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDevice.mrHardStr") + "*")
        .equalsIgnoreCase(objects[16].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDevice.boUnitHard")
        .equalsIgnoreCase(objects[17].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrDevice.mrTypeStr")
        .equalsIgnoreCase(objects[18].toString().trim())) {
      return false;
    }

    return true;
  }

  public MrDeviceDTO validateImportInfo(MrDeviceDTO mrDeviceDTO,
      List<MrDeviceDTO> list) {
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMarketName())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.marketName"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMarketCode())) {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.err.marketName.exist"));
      return mrDeviceDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getDeviceId()) && StringUtils
        .isStringNullOrEmpty(mrDeviceDTO.getRegionHard())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.regionHard"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getDeviceId()) && !StringUtils
        .isStringNullOrEmpty(mrDeviceDTO.getRegionHard())
        && mrDeviceDTO.getRegionHard().length() > 100) {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.errSize.regionHard"));
      return mrDeviceDTO;
    }
    if (!(StringUtils.isStringNullOrEmpty(mrDeviceDTO.getDeviceId())) && !StringUtils
        .isStringNullOrEmpty(mrDeviceDTO.getRegionHard())
        && mrDeviceDTO.getRegionHard().length() > 100) {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.errSize.regionHard"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getArrayCodeStr())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.arrayCodeStr"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getArrayCode())) {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.err.arrayCode.exist"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getNetworkTypeStr())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.networkTypeStr"));
      return mrDeviceDTO;
    }
//    MrDeviceDTO networkCheck = mrDeviceRepository
//        .checkNetworkTypeByArrayCode(mrDeviceDTO.getArrayCode(), mrDeviceDTO.getNetworkTypeStr());
    if (mapA_NInDB.containsKey(mrDeviceDTO.getArrayCode() + ";" + mrDeviceDTO.getNetworkTypeStr())) { //so sanh mrId cua 2 thang = nhau la co ton tai
      mrDeviceDTO.setNetworkType(mrDeviceDTO.getNetworkTypeStr());
    } else {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.err.networkType.exist"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getDeviceTypeStr())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.deviceTypeStr"));
      return mrDeviceDTO;
    }
//    MrDeviceDTO deviceCheck = mrDeviceRepository
//        .checkDeviceTypeByArrayNet(mrDeviceDTO.getArrayCode(), mrDeviceDTO.getNetworkType(),
//            mrDeviceDTO.getDeviceTypeStr());
    if (mapA_N_DInDB.containsKey(mrDeviceDTO.getArrayCode() + ";" + mrDeviceDTO.getNetworkType() + ";" + mrDeviceDTO.getDeviceTypeStr())) {
//    if (deviceCheck != null) {
      mrDeviceDTO.setDeviceType(mrDeviceDTO.getDeviceTypeStr());
    } else {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.err.deviceTypeStr.exist"));
      return mrDeviceDTO;
    }

    if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getNodeIp())
        && mrDeviceDTO.getNodeIp().length() > 2000) {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.errSize.nodeIP"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getNodeCode())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.nodeCode"));
      return mrDeviceDTO;
    }
    if (mrDeviceDTO.getNodeCode().length() > 100) {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.errSize.nodeCode"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getDeviceName())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.deviceName"));
      return mrDeviceDTO;
    }
    if (mrDeviceDTO.getDeviceName().length() > 500) {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.errSize.deviceName"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getVendor())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.vendor"));
      return mrDeviceDTO;
    }
    if (mrDeviceDTO.getVendor().length() > 100) {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.errSize.vendor"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getStatusStr())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.statusStr"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getStatus())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.status.exist"));
      return mrDeviceDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getStationCode())
        && mrDeviceDTO.getStationCode().length() > 200) {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.errSize.stationCode"));
      return mrDeviceDTO;
    }
//    UsersInsideDto usersInsideDto = new UsersInsideDto();
//    usersInsideDto.setUsername(mrDeviceDTO.getUserMrHard());
//    List<UsersInsideDto> userDTOList = userRepository.getListUsersDTOS(usersInsideDto);
    if (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getUserMrHard())) {
      if (!mapUser.containsKey(mrDeviceDTO.getUserMrHard())) {
        mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.username.exist"));
        return mrDeviceDTO;
      } else {
        if (!StringUtils.isStringNullOrEmpty(mapUser.get(mrDeviceDTO.getUserMrHard()).getIsEnable()) && "0"
            .equals(mapUser.get(mrDeviceDTO.getUserMrHard()).getIsEnable().toString())) {
          mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.username.exist"));
          return mrDeviceDTO;
        }
      }
    }
    if (!(StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrConfirmHardStr())) && StringUtils
        .isStringNullOrEmpty(mrDeviceDTO.getMrConfirmHard())) {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.err.mrConfirmHard.exist"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrHardStr())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.mrHardStr"));
      return mrDeviceDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrHard())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.mrHard.exist"));
      return mrDeviceDTO;
    }
    if ("1".equals(mrDeviceDTO.getMrHard()) && !StringUtils
        .isStringNullOrEmpty(mrDeviceDTO.getMrConfirmHard())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.mrConfirmHard.required1"));
      return mrDeviceDTO;
    }
    if ("0".equals(mrDeviceDTO.getMrHard()) && StringUtils
        .isStringNullOrEmpty(mrDeviceDTO.getMrConfirmHard())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.mrConfirmHard.required"));
      return mrDeviceDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getDeviceId()) && StringUtils
        .isStringNullOrEmpty(mrDeviceDTO.getBoUnitHard())) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.boUnitStr"));
      return mrDeviceDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getBoUnitHard())) {
//      UnitDTO unit = unitRepository.findUnitById(mrDeviceDTO.getBoUnitHard());
      if (mapUnit.containsKey(mrDeviceDTO.getBoUnitHard())) {
//        if (!StringUtils.isStringNullOrEmpty(mapUnit.get(mrDeviceDTO.getBoUnitHard()).getStatus()) && "1"
//            .equals(mapUnit.get(mrDeviceDTO.getBoUnitHard()).getStatus().toString())) {
//
//        } else {
//          mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.boUnit.exist"));
//          return mrDeviceDTO;
//        }
      } else {
        mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.boUnit.exist"));
        return mrDeviceDTO;
      }
    }
    if (!(StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrTypeStr())) && StringUtils
        .isStringNullOrEmpty(mrDeviceDTO.getMrType())) {
      mrDeviceDTO
          .setResultImport(I18n.getLanguage("mrDevice.err.mrType.exist"));
      return mrDeviceDTO;
    }
    MrDeviceDTO mrDevice = mrDeviceRepository.ckeckMrDeviceHardExist(mrDeviceDTO);
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getDeviceId()) && mrDevice != null) {
      mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.duplicate"));
      return mrDeviceDTO;
    } else if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getDeviceId())) {
//      MrDeviceDTO dto = mrDeviceSoftRepository.findMrDeviceById(mrDeviceDTO.getDeviceId());
      MrDeviceDTO dto = mapDeviceInDB.get(String.valueOf(mrDeviceDTO.getDeviceId()));
      if (dto == null) {
        mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.noData"));
        return mrDeviceDTO;
      } else {
        mrDeviceDTO.setMrDeviceOld(dto);
        if (!StringUtils
            .isStringNullOrEmpty(dto.getApproveStatusHard()) && "0"
            .equals(dto.getApproveStatusHard())) {
          mrDeviceDTO
              .setResultImport(I18n.getLanguage("mrDevice.err.approveStatusHard"));
        }
      }
    }
    if (list != null && list.size() > 0 && mrDeviceDTO.getResultImport() == null) {
      mrDeviceDTO = validateDuplicate(list, mrDeviceDTO);
    }

    return mrDeviceDTO;
  }

  public MrDeviceDTO validateDuplicate(List<MrDeviceDTO> list,
      MrDeviceDTO mrDeviceDTO) {
    for (int i = 0; i < list.size(); i++) {
      MrDeviceDTO mrDeviceTmp = list.get(i);
      if (I18n.getLanguage("mrDevice.result.import")
          .equals(mrDeviceTmp.getResultImport())
          && mrDeviceTmp.getMarketCode().equals(mrDeviceDTO.getMarketCode()) && mrDeviceTmp
          .getNodeCode().equals(mrDeviceDTO.getNodeCode())) {
        mrDeviceDTO.setResultImport(I18n.getLanguage("mrDevice.err.dup-code-in-file")
            .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return mrDeviceDTO;
  }

  public ResultInSideDto insertImport(List<MrDeviceDTO> mrDeviceDTOList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    userTokenImport = ticketProvider.getUserToken();
    UnitDTO unitDTO = unitRepository.findUnitById(userTokenImport.getDeptId());
    boolean isUserTP = false;
    if (unitDTO != null && "0".equals(String.valueOf(unitDTO.getIsCommittee()))) {
      if (userRepository.checkRoleOfUser("TP", userTokenImport.getUserID())) {
        isUserTP = true;
      }
    }
    for (MrDeviceDTO mrDeviceDTO : mrDeviceDTOList) {
      if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getDeviceId())) {
        mrDeviceDTO.setUpdateUser(userTokenImport.getUserName());
        mrDeviceDTO.setUpdateDate(new Date());
        resultInSideDto = mrDeviceRepository.edit(mrDeviceDTO);
      } else {
        MrDeviceDTO mrDeviceOld = mrDeviceDTO.getMrDeviceOld();
        if (!"0".equals(String.valueOf(mrDeviceOld.getApproveStatusHard()))) {
          mrDeviceOld.setRegionHard(mrDeviceDTO.getRegionHard());
          mrDeviceOld.setStatus(mrDeviceDTO.getStatus());
          mrDeviceOld.setMrHard(mrDeviceDTO.getMrHard());
          mrDeviceOld.setMrConfirmHard(
              StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrConfirmHard()) ? null
                  : mrDeviceDTO.getMrConfirmHard());
          mrDeviceOld.setStationCode(
              StringUtils.isStringNullOrEmpty(mrDeviceDTO.getStationCode()) ? null
                  : mrDeviceDTO.getStationCode());
          mrDeviceOld
              .setUserMrHard(StringUtils.isStringNullOrEmpty(mrDeviceDTO.getUserMrHard()) ? null
                  : mrDeviceDTO.getUserMrHard());
          mrDeviceOld.setBoUnitHard(
              StringUtils.isStringNullOrEmpty(mrDeviceDTO.getBoUnitHard()) ? null
                  : mrDeviceDTO.getBoUnitHard());
          if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrConfirmHard())) {
            mrDeviceOld.setApproveStatusHard(1L);
          }
          // fix import
          boolean checkIsCommittee = false;
          if (mrDeviceDTO.getBoUnitHard() != null) {
            String unitApprove = userRepository.getUnitParentForApprove("1",
                mrDeviceDTO.getBoUnitHard().toString());
            if (userTokenImport.getDeptId() != null && unitApprove != null
                && userTokenImport.getDeptId().equals(Long.parseLong(unitApprove)) && isUserTP) {
              checkIsCommittee = true;
            }
          }
          resultInSideDto = updateMrDeviceHard(mrDeviceOld, checkIsCommittee, true);
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public List<MrDeviceDTO> onSearchEntity(MrDeviceDTO mrDeviceDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return mrDeviceRepository
        .onSearchEntity(mrDeviceDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public ResultInSideDto insertOrUpdateListDevice(List<MrDeviceDTO> lstMrDeviceDto) {
    return mrDeviceRepository.updateList(lstMrDeviceDto);
  }

  @Override
  public void updateStatusAndLastDate(MrScheduleTelDTO objScheduleTel, String status,
      String lastDate) {
    Date lastDateUpdate = new Date();
    try {
      if (lastDate != null && !"".equals(lastDate)) {
        lastDateUpdate = DateTimeUtils.convertStringToDate(lastDate);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    try {
      MrDeviceDTO objUpdate = mrDeviceRepository.findMrDeviceById(objScheduleTel.getDeviceId());
      if (objUpdate != null) {
        if ("1".equals(objScheduleTel.getMrHard())) {
          if ("1".equals(String.valueOf(objScheduleTel.getMrHardCycle()))) {
            objUpdate.setIsComplete1m(Long.valueOf(status));
            objUpdate.setLastDate1m(lastDateUpdate);
          } else if ("3".equals(String.valueOf(objScheduleTel.getMrHardCycle()))) {
            objUpdate.setIsComplete3m(Long.valueOf(status));
            objUpdate.setLastDate3m(lastDateUpdate);
          } else if ("6".equals(String.valueOf(objScheduleTel.getMrHardCycle()))) {
            objUpdate.setIsComplete6m(Long.valueOf(status));
            objUpdate.setLastDate6m(lastDateUpdate);
          } else if ("12".equals(String.valueOf(objScheduleTel.getMrHardCycle()))) {
            objUpdate.setIsComplete12m(Long.valueOf(status));
            objUpdate.setLastDate12m(lastDateUpdate);
          }
        } else {
          objUpdate.setIsCompleteSoft(Long.valueOf(status));
          objUpdate.setLastDate(lastDateUpdate);
        }
        objUpdate.setUpdateDate(lastDateUpdate);
        mrDeviceRepository.updateMrDeviceServices(objUpdate);
      }
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public List<MrDeviceDTO> getDeviceStationCodeCbb() {
    return mrDeviceRepository.getDeviceStationCodeCbb();
  }

  @Override
  public ResultInSideDto approveMrDeviceHard(MrDeviceDTO mrDeviceDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    MrDeviceDTO mrDeviceOld = mrDeviceSoftRepository.findMrDeviceById(mrDeviceDTO.getDeviceId());
    if (mrDeviceOld == null) {
      resultInSideDto.setKey(RESULT.ERROR);
      return resultInSideDto;
    }
    List<MrScheduleTelDTO> lstMr = new ArrayList<>();
    mrDeviceOld.setApproveReasonHard(mrDeviceDTO.getApproveReasonHard());
    mrDeviceOld.setApproveStatusHard(mrDeviceDTO.getApproveStatusHard().equals(1L) ? 1L : 2L);
    if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getApproveStatusHard()) && "1"
        .equals(String.valueOf(mrDeviceDTO.getApproveStatusHard()))) {
      mrDeviceOld.setMrHard("1".equalsIgnoreCase(mrDeviceDTO.getMrHard()) ? "0" : "1");
      if (StringUtils.isNotNullOrEmpty(mrDeviceOld.getMrConfirmHard())) {
        MrScheduleTelDTO scheduleTelDTO = new MrScheduleTelDTO();
        scheduleTelDTO.setDeviceId(mrDeviceDTO.getDeviceId());
        scheduleTelDTO.setDeviceCode(mrDeviceDTO.getNodeCode());
        scheduleTelDTO.setMrHard("1");
        List<MrScheduleTelDTO> listSchedule = mrScheduleTelRepository
            .getListMrScheduleTelDTO(scheduleTelDTO);
        if (listSchedule != null && !listSchedule.isEmpty()) {
          mrDeviceOld.setCheckNode(true);
          insertMrScheduleTelHis(listSchedule, mrDeviceOld); //sinh lịch sử từ list Lịch
          for (MrScheduleTelDTO dtoUpdate : listSchedule) {
            checkCycleHard(mrDeviceOld, dtoUpdate); //set lại iscomplete theo cycle
            if (!StringUtils.isStringNullOrEmpty(dtoUpdate.getMrId())) {
              lstMr.add(dtoUpdate);
            }
          }
        }
        if (listSchedule.size() > 0) {
          mrScheduleTelRepository.deleteListSchedule(listSchedule);
        }
        //xoa MR neu thay doi trang thai tu Co BD -> Ko BD
        if (!lstMr.isEmpty()) {
          for (MrScheduleTelDTO mr : lstMr) {
            MrInsideDTO mrInsideDTO = mrRepository.findMrById(mr.getMrId());
            if (mrInsideDTO != null) {
              resultInSideDto = mrRepository.deleteMr(Long.valueOf(mr.getMrId()));
              if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                throw new RuntimeException("Error occur while delete data in MR");
              }
              //move MR sang MR HIS
              MrHisDTO mrHis = new MrHisDTO();
              mrHis.setMrId(String.valueOf(mr.getMrId()));
              mrHis.setStatus(mrInsideDTO.getState());
              mrHis.setActionCode("3");
              mrHis.setChangeDate(DateTimeUtils.getSysDateTime());
              mrHis.setNotes("AUTO_DELETE_MR_AND_MOVE_MR_TO_HIS");
              mrHis.setComments(I18n.getLanguage("mrScheduleTelhis.updateSchedule.mrHisComment"));
              resultInSideDto = mrHisRepository.insertMrHis(mrHis);
              if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                throw new RuntimeException("Error occur while insert data to MR_HIS");
              }
            }
          }
          //Goi WS CR
        }
      }
      if ("1".equals(mrDeviceOld.getMrHard()) && StringUtils
          .isNotNullOrEmpty(mrDeviceOld.getMrConfirmHard())) {
        mrDeviceOld.setMrConfirmHard(null);
      }
    } else {
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrHard()) && "1"
          .equals(mrDeviceDTO.getMrHard())) {
        mrDeviceOld.setMrConfirmHard(null);
      } else if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrHard()) && "0"
          .equals(mrDeviceDTO.getMrHard())) {
        mrDeviceOld.setMrConfirmHard(mrDeviceDTO.getMrConfirmHard());
      }
    }
    return mrDeviceRepository.edit(mrDeviceOld);
  }

  private void clearMapData(){
    mapMarketName.clear();
    mapArray.clear();
    mapMrConfirmHard.clear();
    mapUnit.clear();
    mapUser.clear();
    mapDeviceInDB.clear();
    mapA_NInDB.clear();
    mapA_N_DInDB.clear();
    userTokenImport = null;
  }

}
