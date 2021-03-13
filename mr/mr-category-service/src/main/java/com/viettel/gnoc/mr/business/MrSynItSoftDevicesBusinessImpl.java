package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
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
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrConfigRepository;
import com.viettel.gnoc.mr.repository.MrHisRepository;
import com.viettel.gnoc.mr.repository.MrITHisRepository;
import com.viettel.gnoc.mr.repository.MrITSoftCatMarketRepository;
import com.viettel.gnoc.mr.repository.MrITSoftScheduleRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MrSynItSoftDevicesBusinessImpl implements MrSynItSoftDevicesBusiness {

  @Autowired
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;
  @Autowired
  CatLocationRepository catLocationRepository;
  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;
  @Autowired
  MrConfigRepository repository;
  @Autowired
  UnitRepository unitRepository;
  @Autowired
  TicketProvider ticketProvider;
  @Autowired
  CatItemBusiness catItemBusiness;
  @Autowired
  MrScheduleTelRepository mrScheduleTelRepository;
  @Autowired
  MrITSoftScheduleRepository mrITSoftScheduleRepository;
  @Autowired
  MrRepository mrRepository;
  @Autowired
  CrServiceProxy crServiceProxy;
  @Autowired
  MrITHisRepository mrITHisRepository;
  @Autowired
  MrHisRepository mrHisRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  MrITSoftCatMarketRepository mrITSoftCatMarketRepository;
  @Value("${application.temp.folder}")
  private String tempFolder;
  Map<String, String> mapMarketName = new HashMap<>();
  Map<String, String> mapImportantLevel = new HashMap<>();
  Map<String, String> mapBoUnit = new HashMap<>();
  Map<String, String> mapWoCdGroup = new HashMap<>();
  Map<String, String> mapMrConfirmSoft = new HashMap<>();
  Map<String, String> mapArray = new HashMap<>();
  boolean checkIsComplete = true;

  @Override
  public List<MrSynItDevicesDTO> getListMrSynITDeviceSoftDTO(MrSynItDevicesDTO dto, int rowStart,
      int maxRow) {
    List<MrSynItDevicesDTO> result = new ArrayList<>();
    mapMarketName.clear();
    mapMrConfirmSoft.clear();
    mapBoUnit.clear();
    mapWoCdGroup.clear();
    mapImportantLevel.clear();
    setMapMarketName();
    setMrConfirmSoftStr();
    setMapWoCd();
    getMapImportantLevel();
    setMapBoUnit();

    try {
      result = mrSynItSoftDevicesRepository.getListMrSynITDeviceSoftDTO(dto, rowStart, maxRow);
      if (result != null && !result.isEmpty()) {
        for (MrSynItDevicesDTO item : result) {
          if (!StringUtils.isStringNullOrEmpty(item.getMarketCode())) {
            String marketName = mapMarketName.get(item.getMarketCode());
            if (!StringUtils.isStringNullOrEmpty(marketName)) {
              item.setMarketName(marketName);
            }
          }
          if ("1".equals(item.getMrSoft())) {
            item.setMrSoftDisplay(I18n.getLanguage("yes"));
          } else if ("0".equals(item.getMrSoft())) {
            item.setMrSoftDisplay(I18n.getLanguage("no"));
          }
          if ("1".equals(item.getStatus())) {
            item.setStatus(I18n.getLanguage("common.active"));
          } else if ("0".equals(item.getStatus())) {
            item.setStatus(I18n.getLanguage("common.inActive"));
          }
          if (!StringUtils.isStringNullOrEmpty(item.getMrConfirmSoft())) {
            if (mapMrConfirmSoft.containsKey(item.getMrConfirmSoft())) {
              item.setMrConfirmSoftDisplay(mapMrConfirmSoft.get(item.getMrConfirmSoft()));
            }
          }
          if (!StringUtils.isStringNullOrEmpty(item.getLevelImportant())) {
            if (mapImportantLevel.containsKey(item.getLevelImportant())) {
              item.setLevelImportantName(mapImportantLevel.get(item.getLevelImportant()));
            }
          }
          if (!StringUtils.isStringNullOrEmpty(item.getBoUnit())) {
            if (mapBoUnit.containsKey(item.getBoUnit())) {
              item.setBoUnitName(mapBoUnit.get(item.getBoUnit()));
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public void getMapImportantLevel() {
    mapImportantLevel
        .put("1", I18n.getLanguage("mrSynItHardDevice.importantLevel.veryImportant"));
    mapImportantLevel.put("2", I18n.getLanguage("mrSynItHardDevice.importantLevel.important"));
    mapImportantLevel.put("3", I18n.getLanguage("mrSynItHardDevice.importantLevel.normal"));
  }

  public void setMapBoUnit() {
    List<UnitDTO> lsUnit = unitRepository.getUnitByUnitDTO(new UnitDTO());
    if (lsUnit != null && !lsUnit.isEmpty()) {
      for (UnitDTO dto : lsUnit) {
        if (!mapBoUnit.containsKey(dto.getUnitId().toString())) {
          mapBoUnit.put(dto.getUnitId().toString(), dto.getUnitName());
        }
      }
    }
  }

  public void setMapWoCd() {
    List<WoCdGroupInsideDTO> lstCd = woCategoryServiceProxy
        .getListCdGroupByUser(new WoCdGroupTypeUserDTO());
    if (lstCd != null && !lstCd.isEmpty()) {
      if (mapWoCdGroup.entrySet().size() == 0) {
        for (WoCdGroupInsideDTO item : lstCd) {
          mapWoCdGroup.put(String.valueOf(item.getWoGroupId()), item.getWoGroupName());
        }
      }
    }
  }

  public void setMapMarketName() {
    List<ItemDataCRInside> lstMarketCode = catLocationRepository
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstMarketCode != null && !lstMarketCode.isEmpty()) {
      for (ItemDataCRInside itemDataCRInside : lstMarketCode) {
        if (itemDataCRInside.getValueStr() != null && !mapMarketName
            .containsKey(String.valueOf(itemDataCRInside.getValueStr()))) {
          mapMarketName
              .put(String.valueOf(itemDataCRInside.getValueStr()),
                  itemDataCRInside.getDisplayStr());
        }

      }
    }
  }

  public void setMrConfirmSoftStr() {
    List<MrConfigDTO> lstMrConfirmHardStr = mrScheduleTelRepository.getConfigByGroup(
        MR_CONFIG_GROUP.LY_DO_KO_BD);
    if (lstMrConfirmHardStr != null && !lstMrConfirmHardStr.isEmpty()) {
      for (MrConfigDTO item : lstMrConfirmHardStr) {
        mapMrConfirmSoft
            .put(String.valueOf(item.getConfigCode()), item.getConfigName());
      }
    }
  }

  public void setMapArray() {
    List<CatItemDTO> lstArrayCode = catItemBusiness
        .getListItemByCategoryAndParent(Constants.MR_ITEM_NAME.MR_SUBCATEGORY, null);
    if (lstArrayCode != null && !lstArrayCode.isEmpty()) {
      for (CatItemDTO catItemDTO : lstArrayCode) {
        mapArray
            .put(String.valueOf(catItemDTO.getItemCode()), catItemDTO.getItemName());
      }
    }
  }

  @Override
  public List<MrSynItDevicesDTO> getListDeviceTypeByArrayCode(String arrayCode) {
    return mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(arrayCode);
  }

  @Override
  public MrSynItDevicesDTO findMrDeviceByObjectId(MrSynItDevicesDTO dto) {
    return mrSynItSoftDevicesRepository.findMrDeviceByObjectId(dto);
  }

  @Override
  public ResultInSideDto insertOrUpdateListDevice(List<MrSynItDevicesDTO> lstMrDeviceDto) {
    return mrSynItSoftDevicesRepository.updateList(lstMrDeviceDto);
  }

  @Override
  public List<MrSynItDevicesDTO> getDeviceITStationCodeCbb() {
    return mrSynItSoftDevicesRepository.getDeviceITStationCodeCbb();
  }

  @Override
  public List<MrSynItDevicesDTO> onSearchEntity(MrSynItDevicesDTO mrDeviceDTO, int rowStart,
      int maxRow,
      String sortType, String sortFieldList) {
    return mrSynItSoftDevicesRepository
        .onSearchEntity(mrDeviceDTO, rowStart, maxRow, sortType, sortFieldList);
  }


  @Override
  public MrSynItDevicesDTO getMrSynItDevicesDetail(Long id) {
    log.debug("Request to getMrSynItDevicesDetail: {}", id);
    return mrSynItSoftDevicesRepository.getMrSynItDevicesDetail(id);
  }


  @Override
  public File ExportData(MrSynItDevicesDTO mrSynItDevicesDTO) throws Exception {
    List<MrSynItDevicesDTO> mrSynITDeviceSoftList = mrSynItSoftDevicesRepository
        .getListMrSynItDeviceSoftExport(mrSynItDevicesDTO);
    if (mrSynITDeviceSoftList != null && !mrSynITDeviceSoftList.isEmpty()) {
      mapImportantLevel.clear();
      getMapImportantLevel();
      for (int i = mrSynITDeviceSoftList.size() - 1; i > -1; i--) {
        MrSynItDevicesDTO mrDeviceExport = mrSynITDeviceSoftList.get(i);
        if (StringUtils.isNotNullOrEmpty(mrDeviceExport.getStatus())) {
          if ("0".equals(mrDeviceExport.getStatus())) {
            mrDeviceExport.setStatusStr(I18n.getLanguage("mrDevice.status.inActive"));
          } else if ("1".equals(mrDeviceExport.getStatus())) {
            mrDeviceExport.setStatusStr(I18n.getLanguage("mrDevice.status.active"));
          }
        }
        if (StringUtils.isNotNullOrEmpty(mrDeviceExport.getMrSoft())) {
          if ("1".equals(mrDeviceExport.getMrSoft())) {
            mrDeviceExport.setMrSoftStr(I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.1"));
            mrDeviceExport.setMrConfirmSoftStr("");
          } else if ("0".equals(mrDeviceExport.getMrSoft())) {
            mrDeviceExport.setMrSoftStr(I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.0"));
          }
        }
        if (StringUtils.isNotNullOrEmpty(mrDeviceExport.getApproveStatus())) {
          mrDeviceExport.setApproveStatusName(I18n.getLanguage(
              "mtItSoftSchedule.approveStatus." + mrDeviceExport.getApproveStatus()));
        } else {
          mrDeviceExport.setApproveStatusName(I18n.getLanguage(
              "mtItSoftSchedule.approveStatus.null"));
        }
        if (!StringUtils.isStringNullOrEmpty(mrDeviceExport.getLevelImportant())) {
          if (mapImportantLevel.containsKey(mrDeviceExport.getLevelImportant())) {
            mrDeviceExport
                .setLevelImportantName(mapImportantLevel.get(mrDeviceExport.getLevelImportant()));
          }
        }
        if (!StringUtils.isStringNullOrEmpty(mrDeviceExport.getIsRunMop())
            && mrDeviceExport.getIsRunMop() == 0) {
          mrDeviceExport.setIsRunMopName(I18n.getLanguage("common.no"));
        } else {
          mrDeviceExport.setIsRunMopName(I18n.getLanguage("common.yes"));
        }
      }
    }
    return exportFileTemplate(mrSynITDeviceSoftList, "");
  }

  @Override
  public Datatable getListMrSynItDeviceSoftDTO(MrSynItDevicesDTO mrSynItDevicesDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    mapMarketName.clear();
    mapWoCdGroup.clear();
    mapImportantLevel.clear();
    setMapMarketName();
    setMapWoCd();
    getMapImportantLevel();

    Datatable datatable = mrSynItSoftDevicesRepository
        .getListMrSynItDeviceSoftDTO(mrSynItDevicesDTO);
    List<MrSynItDevicesDTO> mrDeviceDTOList = (List<MrSynItDevicesDTO>) datatable.getData();
    if (mrDeviceDTOList != null && !mrDeviceDTOList.isEmpty()) {
      for (MrSynItDevicesDTO deviceDTO : mrDeviceDTOList) {
        if (!StringUtils.isStringNullOrEmpty(deviceDTO.getMarketCode())) {
          String marketName = mapMarketName.get(deviceDTO.getMarketCode());
          if (!StringUtils.isStringNullOrEmpty(marketName)) {
            deviceDTO.setMarketName(marketName);
          }
        }
        if (StringUtils.isStringNullOrEmpty(deviceDTO.getApproveStatus())) {
          if ("0".equals(mrSynItDevicesDTO.getApproveStatus())) {
            deviceDTO.setApproveStatusName(I18n.getLanguage("mrDevice.approveStatus.0"));
          } else if ("1".equals(mrSynItDevicesDTO.getApproveStatus())) {
            deviceDTO.setApproveStatusName(I18n.getLanguage("mrDevice.approveStatus.1"));
          } else if ("2".equals(mrSynItDevicesDTO.getApproveStatus())) {
            deviceDTO.setApproveStatusName(I18n.getLanguage("mrDevice.approveStatus.2"));
          }
        }
        if ("1".equals(deviceDTO.getMrHard())) {
          deviceDTO.setMrHardStr(I18n.getLanguage("common.yes"));
        } else if ("0".equals(deviceDTO.getMrHard())) {
          deviceDTO.setMrHardStr(I18n.getLanguage("common.no"));
        }
        if ("1".equals(deviceDTO.getStatus())) {
          deviceDTO.setStatus(I18n.getLanguage("common.active"));
        } else if ("0".equals(deviceDTO.getStatus())) {
          deviceDTO.setStatus(I18n.getLanguage("common.inActive"));
        }
        if (!StringUtils.isStringNullOrEmpty(deviceDTO.getLevelImportant())) {
          if (mapImportantLevel.containsKey(deviceDTO.getLevelImportant())) {
            deviceDTO.setLevelImportantName(mapImportantLevel.get(deviceDTO.getLevelImportant()));
          }
        }
        if (!StringUtils.isStringNullOrEmpty(deviceDTO.getIsRunMop())
            && deviceDTO.getIsRunMop() == 0) {
          deviceDTO.setIsRunMopName(I18n.getLanguage("common.no"));
        } else {
          deviceDTO.setIsRunMopName(I18n.getLanguage("common.yes"));
        }
        //checkApprove
        if (deviceDTO.getBoUnit() != null) {
          log.info(String.valueOf(userToken.getDeptId()));
          String unitApprove = userRepository.getUnitParentForApprove("1",
              deviceDTO.getBoUnit());
          if (userToken.getDeptId() != null && unitApprove != null
              && userToken.getDeptId().equals(Long.parseLong(unitApprove))
              && StringUtils.isNotNullOrEmpty(deviceDTO.getApproveStatus()) && deviceDTO
              .getApproveStatus()
              .equals("0")) {
            UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
            if (unitDTO.getIsCommittee().equals(0L)) {
              if (userRepository.checkRoleOfUser("TP", userToken.getUserID())) {
                deviceDTO.setCheckApprove("1");
              }
            }
          }
        }
      }
      datatable.setData(mrDeviceDTOList);
    }
    return datatable;
  }

  @Override
  public ResultInSideDto deleteMrSynItSoftDevice(MrSynItDevicesDTO mrSynItDevicesDTO) {
    log.debug("Request to delete: {}", mrSynItDevicesDTO);
    ResultInSideDto resultInSideDto;
    List<MrITSoftScheduleDTO> listSchedule = new ArrayList<>();
    MrITSoftScheduleDTO scheduleItDTO = new MrITSoftScheduleDTO();
    scheduleItDTO.setDeviceId(mrSynItDevicesDTO.getObjectId());
    scheduleItDTO.setDeviceCode(mrSynItDevicesDTO.getObjectCode());
    List<MrITSoftScheduleDTO> schedules = mrITSoftScheduleRepository
        .getListScheduleMove(scheduleItDTO);
    listSchedule.addAll(schedules);
    resultInSideDto = mrITSoftScheduleRepository.deleteListSchedule(schedules);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      resultInSideDto = mrSynItSoftDevicesRepository
          .deleteMrSynItSoftDevice(Long.valueOf(mrSynItDevicesDTO.getId()));
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        if (!listSchedule.isEmpty()) {
          mrSynItDevicesDTO.setCheckNode("0");
          resultInSideDto = insertMrScheduleITHis(listSchedule, mrSynItDevicesDTO);
        }
      }
    }
    return resultInSideDto;
  }

  public ResultInSideDto insertMrScheduleITHis(List<MrITSoftScheduleDTO> listScheduleIt,
      MrSynItDevicesDTO mrSynItDevicesDTO) {
    MrInsideDTO mrInsideDTO = new MrInsideDTO();
    List<MrScheduleITHisDTO> listScheduleITHis = new ArrayList<>();
    MrITSoftScheduleDTO scheduleItDTO = listScheduleIt.get(0);
    if (StringUtils.isNotNullOrEmpty(scheduleItDTO.getMrId())) {
      mrInsideDTO.setMrId(Long.valueOf(scheduleItDTO.getMrId()));
      mrInsideDTO = mrRepository.findMrById(mrInsideDTO.getMrId());
    }
    for (MrITSoftScheduleDTO itDTO : listScheduleIt) {
      MrScheduleITHisDTO scheduleHisDTO = new MrScheduleITHisDTO();
      scheduleHisDTO.setMarketCode(itDTO.getMarketCode());
      scheduleHisDTO.setArrayCode(itDTO.getArrayCode());
      scheduleHisDTO.setDeviceType(itDTO.getDeviceType());
      scheduleHisDTO
          .setDeviceId(
              itDTO.getDeviceId() != null ? String.valueOf(itDTO.getDeviceId()) : null);
      scheduleHisDTO.setDeviceCode(itDTO.getDeviceCode());
      scheduleHisDTO.setDeviceName(itDTO.getDeviceName());
      scheduleHisDTO
          .setMrDate(itDTO.getNextDateModify() != null ? itDTO.getNextDateModify() : null);
      scheduleHisDTO.setMrContent(mrInsideDTO == null ? null
          : mrInsideDTO.getMrContentId());
      scheduleHisDTO.setMrMode("S");
      scheduleHisDTO.setMrType(mrInsideDTO == null ? null
          : mrInsideDTO.getMrType());
      scheduleHisDTO.setMrId(itDTO.getMrId() != null ? String.valueOf(itDTO.getMrId()) : null);
      scheduleHisDTO.setMrCode(mrInsideDTO == null ? null
          : mrInsideDTO.getMrCode());
      scheduleHisDTO.setCrId(itDTO.getCrId() != null ? String.valueOf(itDTO.getCrId()) : null);
      if (itDTO.getCrId() != null) {
        scheduleHisDTO
            .setCrNumber(
                genCrNumber(Long.valueOf(itDTO.getCrId()), itDTO.getTypeCr(),
                    itDTO.getArrayActionName()));
      }
      scheduleHisDTO.setProcedureId(
          itDTO.getProcedureId() != null ? String.valueOf(itDTO.getProcedureId()) : null);
      scheduleHisDTO.setProcedureName(itDTO.getProcedureName());
      scheduleHisDTO.setNetworkType(itDTO.getNetworkType());
      scheduleHisDTO.setCycle(itDTO.getCycle());
      scheduleHisDTO.setRegion(itDTO.getRegion());
      scheduleHisDTO.setMrDate(itDTO.getMrDate());
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getCheckNode()) && "1"
          .equals(mrSynItDevicesDTO.getCheckNode())) {
        scheduleHisDTO.setNote(I18n.getLanguage("mrSynItSoftDevice.note.mrConfirm"));
      } else {
        scheduleHisDTO.setNote(I18n.getLanguage("mrScheduleTelhis.note.delete"));
      }
      listScheduleITHis.add(scheduleHisDTO);
    }
    return mrITHisRepository.insertUpdateListScheduleHis(listScheduleITHis);
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

  public File exportFileTemplate(List<MrSynItDevicesDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("mrSynItSoftDevice.export.title");
    String title = I18n.getLanguage("mrSynItSoftDevice.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    //kiểm tra đầu vào

    if (Constants.RESULT_IMPORT.equals(key)) {
      headerExportList = readerHeaderSheet("idStr", "marketName",
          "regionSoft", "arrayCodeStr", "deviceTypeStr", "ipNode", "objectCode", "objectName",
          "createUserSoft", "notes", "mrSoftStr", "mrConfirmSoftStr",
          "nodeAffected", "groupCode", "vendor", "stationStr", "statusStr",
          "boUnitName", "isRunMopName", "resultImport");
      fileNameOut = "MR_IT_SOFT_DEVICE_RESULT_IMPORT";
      subTitle = null;
    } else {
      headerExportList = readerHeaderSheet("id", "marketName",
          "regionSoft", "arrayCodeStr", "deviceType", "ipNode", "objectCode",
          "objectName", "createUserSoft", "notes", "mrSoftStr", "mrConfirmSoftDisplay",
          "nodeAffected", "groupCode",
          "vendor", "station", "statusStr", "boUnitName", "isRunMopName", "statusIIM",
          "implementUnitName", "checkingUnitName", "levelImportantName",
          "synDateStr", "approveStatusName",
          "db", "ud", "upTime");
      fileNameOut = "MR_IT_SOFT_DEVICE_EXPORT";
      subTitle = I18n
          .getLanguage("mrSynItSoftDevice.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.mrSynItSoftDevice"
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

    /*
    if (!Constants.RESULT_IMPORT.equals(key)) {
      ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
      //apache POI XSSF
      Workbook workbook = excelWriterUtils.readFileExcelFromFile(fileExport, -1, 0);
      Sheet sheetParam = workbook.createSheet("param");
      Sheet sheetParam2 = workbook.createSheet("param2");
      Sheet sheetParam3 = workbook.createSheet("param3");
      Sheet sheetParam4 = workbook.createSheet("param4");
      Sheet sheetParam5 = workbook.createSheet("param5");
      Sheet sheetParam6 = workbook.createSheet("param6");

      Map<String, CellStyle> style = CommonExport.createStyles(workbook);

      int row = 1;
      List<UnitDTO> unitNameList = unitRepository.getListUnit(null);
      for (UnitDTO dto : unitNameList) {
        excelWriterUtils
            .createCell(sheetParam5, 0, row, dto.getUnitId().toString(), style.get("cell"));
        excelWriterUtils.createCell(sheetParam5, 1, row++, dto.getUnitName(), style.get("cell"));
      }

      row = 1;
      List<MrConfigDTO> lstConfig = mrScheduleTelRepository.getConfigByGroup("LY_DO_KO_BD");
      for (MrConfigDTO dto : lstConfig) {
        excelWriterUtils
            .createCell(sheetParam6, 0, row, dto.getConfigCode(), style.get("cell"));
        excelWriterUtils.createCell(sheetParam6, 1, row++, dto.getConfigName(), style.get("cell"));
      }

      sheetParam.setColumnWidth(0, 25000);
      sheetParam.setColumnWidth(1, 25000);

      row = 1;
      List<ItemDataCRInside> lstMarketCode = catLocationRepository
          .getListLocationByLevelCBB(null, 1L, null);
      for (ItemDataCRInside marketCode : lstMarketCode) {
        List<MrITSoftScheduleDTO> lstRegion = mrITSoftScheduleRepository
            .getListRegionByMrSynItDevices(String.valueOf(marketCode.getValueStr()));
        excelWriterUtils
            .createCell(sheetParam2, 0, row, marketCode.getDisplayStr(), style.get("cell"));
        for (MrITSoftScheduleDTO mrITSoftScheduleDTO : lstRegion) {
          excelWriterUtils
              .createCell(sheetParam2, 1, row++, mrITSoftScheduleDTO.getRegion(),
                  style.get("cell"));
        }
        excelWriterUtils
            .createCell(sheetParam2, 2, row++, null, style.get("cell"));
      }
      sheetParam2.setColumnWidth(0, 25000);
      sheetParam2.setColumnWidth(1, 25000);

      row = 1;
      Map<String, List<String>> mapDevicesDTO = new HashMap<>();
      List<MrSynItDevicesDTO> lstANT = mrSynItSoftDevicesRepository.getListDT_AC();
      for (MrSynItDevicesDTO mrSynItDevicesDTO : lstANT) {
        if (mapDevicesDTO.containsKey(mrSynItDevicesDTO.getArrayCode())) {
          List<String> item = mapDevicesDTO.get(mrSynItDevicesDTO.getArrayCode());
          item.add(mrSynItDevicesDTO.getDeviceType());
          mapDevicesDTO.put(mrSynItDevicesDTO.getArrayCode(), item);
        } else {
          List<String> item = new ArrayList<>();
          item.add(mrSynItDevicesDTO.getDeviceType());
          mapDevicesDTO.put(mrSynItDevicesDTO.getArrayCode(), item);
        }
      }
      List<CatItemDTO> lstArrayCode = catItemBusiness
          .getListItemByCategoryAndParent(Constants.MR_ITEM_NAME.MR_SUBCATEGORY, null);
      for (CatItemDTO arrayCode : lstArrayCode) {
        excelWriterUtils
            .createCell(sheetParam3, 0, row, arrayCode.getItemName(), style.get("cell"));
        if (mapDevicesDTO.containsKey(arrayCode.getItemCode())) {
          List<String> lstDV = mapDevicesDTO.get(arrayCode.getItemCode());
          for (String itemDV : lstDV) {
            excelWriterUtils
                .createCell(sheetParam3, 1, row++, itemDV, style.get("cell"));
          }
          mapDevicesDTO.remove(arrayCode.getItemCode());
        }
        excelWriterUtils
            .createCell(sheetParam3, 2, row++, null, style.get("cell"));
      }
      sheetParam3.setColumnWidth(0, 25000);
      sheetParam3.setColumnWidth(1, 25000);

      row = 1;
      setMapWoCd();
      for (Map.Entry<String, String> item : mapWoCdGroup.entrySet()) {
        excelWriterUtils
            .createCell(sheetParam4, 0, row, item.getKey(), style.get("cell"));
        excelWriterUtils.createCell(sheetParam4, 1, row++, item.getValue(), style.get("cell"));
      }
      //set tên trang excel
      workbook.setSheetName(2, I18n.getLanguage("mrSynItSoftDevice.regionSoft"));
      workbook.setSheetName(3, I18n.getLanguage("mrSynItSoftDevice.arrDevice"));
      workbook.setSheetName(4, I18n.getLanguage("mrSynItSoftDevice.cdIdName"));
      workbook.setSheetName(5, I18n.getLanguage("mrSynItSoftDevice.boUnit"));
      workbook.setSheetName(6, I18n.getLanguage("MrScheduleTel.mrConfirmName"));
      workbook.setSheetHidden(1, true);
      sheetParam.setSelected(false);

      //set tên file excel
      String fileResult = tempFolder + File.separator;
      String fileName = "MR_IT_SOFT_DEVICE_EXPORT" + "_" + System.currentTimeMillis() + ".xlsx";
      excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
      String resultPath = fileResult + fileName;
      fileExport = new File(resultPath);
    }*/
    return fileExport;
  }

  public List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  @Override
  public ResultInSideDto approveItSoftDevice(MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    MrSynItDevicesDTO mrDeviceOld = mrSynItSoftDevicesRepository
        .findMrDeviceById(Long.valueOf(mrSynItDevicesDTO.getId()));
    if (mrDeviceOld == null) {
      resultInSideDto.setKey(RESULT.ERROR);
      return resultInSideDto;
    }
    List<MrITSoftScheduleDTO> lstMr = new ArrayList<>();
    mrDeviceOld.setApproveReason(mrSynItDevicesDTO.getApproveReason());
    mrDeviceOld.setApproveStatus("1".equals(mrSynItDevicesDTO.getApproveStatus()) ? "1" : "2");
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getApproveStatus()) && "1"
        .equals(String.valueOf(mrSynItDevicesDTO.getApproveStatus()))) {
      mrDeviceOld.setMrSoft("1".equalsIgnoreCase(mrSynItDevicesDTO.getMrSoft()) ? "0" : "1");
      if (StringUtils.isNotNullOrEmpty(mrDeviceOld.getMrConfirmSoft())) {
        MrITSoftScheduleDTO scheduleTelDTO = new MrITSoftScheduleDTO();
        scheduleTelDTO.setDeviceId(mrSynItDevicesDTO.getObjectId());
        scheduleTelDTO.setDeviceCode(mrSynItDevicesDTO.getObjectCode());
        scheduleTelDTO.setMrSoft("1");
        List<MrITSoftScheduleDTO> listSchedule = mrITSoftScheduleRepository
            .getListScheduleMove(scheduleTelDTO);
        if (listSchedule != null && !listSchedule.isEmpty()) {
          mrDeviceOld.setCheckNode("1");
          mrDeviceOld.setIsCompleteSoft("1");
          insertMrScheduleITHis(listSchedule, mrDeviceOld); //sinh lịch sử từ list Lịch
          for (MrITSoftScheduleDTO dtoUpdate : listSchedule) {
            if (!StringUtils.isStringNullOrEmpty(dtoUpdate.getMrId())) {
              lstMr.add(dtoUpdate);
            }
          }
        }
        if (listSchedule.size() > 0) {
          mrITSoftScheduleRepository.deleteListSchedule(listSchedule);
        }
        //xoa MR neu thay doi trang thai tu Co BD -> Ko BD
        if (!lstMr.isEmpty()) {
          for (MrITSoftScheduleDTO mr : lstMr) {
            MrInsideDTO mrInsideDTO = mrRepository.findMrById(Long.valueOf(mr.getMrId()));
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
      if ("1".equals(mrDeviceOld.getMrSoft()) && StringUtils
          .isNotNullOrEmpty(mrDeviceOld.getMrConfirmSoft())) {
        mrDeviceOld.setMrConfirmSoft(null);
      }
    } else {
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrSoft()) && "1"
          .equals(mrSynItDevicesDTO.getMrSoft())) {
        mrDeviceOld.setMrConfirmSoft(null);
      } else if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrSoft()) && "0"
          .equals(mrSynItDevicesDTO.getMrSoft())) {
        mrDeviceOld.setMrConfirmSoft(mrSynItDevicesDTO.getMrConfirmSoft());
      }
    }
    return mrSynItSoftDevicesRepository.updateMrSynItDevice(mrDeviceOld);

  }

  @Override
  public ResultInSideDto updateMrItSoftDevice(MrSynItDevicesDTO mrSynItDevicesDTO) {
    log.debug("Request to edit: {}", mrSynItDevicesDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    boolean checkIsCommittee = false;
    MrSynItDevicesDTO mrItDeviceOld = mrSynItSoftDevicesRepository
        .findMrDeviceById(Long.valueOf(mrSynItDevicesDTO.getId()));
    if (mrItDeviceOld != null) {
      mrItDeviceOld.setUpdateDate(new Date());
      mrItDeviceOld.setUpdateUser(userToken.getUserName());
      mrItDeviceOld.setIsCompleteSoft(
          StringUtils.isStringNullOrEmpty(mrItDeviceOld.getIsCompleteSoft()) ? null
              : mrItDeviceOld.getIsCompleteSoft());
      List<MrITSoftScheduleDTO> listScheduleItSoft = new ArrayList<>();
      List<MrITSoftScheduleDTO> listScheduleItInActive = new ArrayList<>();
      MrITSoftScheduleDTO scheduleItDTO = new MrITSoftScheduleDTO();
      scheduleItDTO.setDeviceId(mrSynItDevicesDTO.getObjectId());
      scheduleItDTO.setDeviceCode(mrSynItDevicesDTO.getObjectCode());
      scheduleItDTO.setMrSoft("1");
      List<MrITSoftScheduleDTO> listSchedule = mrITSoftScheduleRepository
          .getListScheduleMove(scheduleItDTO);
      if (mrSynItDevicesDTO.getBoUnit() != null) {
        String unitApprove = userRepository.getUnitParentForApprove("1",
            mrSynItDevicesDTO.getBoUnit());
        if (userToken.getDeptId() != null && unitApprove != null
            && userToken.getDeptId().equals(Long.parseLong(unitApprove))) {
          UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
          if (unitDTO.getIsCommittee().equals(0L)) {
            if (userRepository.checkRoleOfUser("TP", userToken.getUserID())) {
              checkIsCommittee = true;
            }
          }
        }
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("mrSynItSoftDevice.err.boUnitStr"));
        return resultInSideDto;
      }
      //Start cap nhat tu khong bao duong ve co bao duong
      if ("1".equals(mrSynItDevicesDTO.getMrSoft()) && StringUtils
          .isStringNullOrEmpty(mrSynItDevicesDTO.getMrConfirmSoft()) && !StringUtils
          .isStringNullOrEmpty(mrItDeviceOld.getMrConfirmSoft())) {
        if (checkIsCommittee) {
          mrSynItDevicesDTO.setApproveStatus("1");
          mrSynItDevicesDTO.setApproveReason("");
          mrSynItDevicesDTO.setMrConfirmSoft(null);
        }
      } else if ("0".equals(mrSynItDevicesDTO.getMrSoft()) && StringUtils
          .isNotNullOrEmpty(mrSynItDevicesDTO.getMrConfirmSoft())) {
        if (checkIsCommittee) {
          mrSynItDevicesDTO.setApproveStatus("1");
          mrSynItDevicesDTO.setApproveReason("");
        }
      }
      //end
      if (StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getMrConfirmSoft()) && !mrSynItDevicesDTO
          .getMrConfirmSoft().equals(mrItDeviceOld.getMrConfirmSoft())) {
        if (listSchedule != null && !listSchedule.isEmpty()) {
          mrSynItDevicesDTO.setCheckNode("1");
          if (checkIsCommittee) {
            mrSynItDevicesDTO.setApproveStatus("1");
            insertMrScheduleITHis(listSchedule, mrSynItDevicesDTO); //sinh lịch sử từ list Lịch
          }
          checkIsComplete = false;
        }
      }
      processAttention(mrSynItDevicesDTO, mrItDeviceOld, listScheduleItSoft);
      inActive(mrSynItDevicesDTO, listScheduleItInActive);
      if (StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getMrConfirmSoft()) && !mrSynItDevicesDTO
          .getMrConfirmSoft().equals(mrItDeviceOld.getMrConfirmSoft())
          && checkIsCommittee) {
        if (listSchedule != null && !listSchedule.isEmpty()) {
          List<MrITSoftScheduleDTO> lstMr = new ArrayList<>();
          for (MrITSoftScheduleDTO dtoUpdate : listSchedule) {
            if (!StringUtils.isStringNullOrEmpty(dtoUpdate.getMrId())) {
              lstMr.add(dtoUpdate);
            }
          }
          if (!lstMr.isEmpty()) {
            for (MrITSoftScheduleDTO mr : lstMr) {
              MrInsideDTO mrInsideDTO = mrRepository.findMrById(Long.valueOf(mr.getMrId()));
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
      if (StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getMrConfirmSoft()) && checkIsCommittee) {
        if (listSchedule.size() > 0) {
          mrITSoftScheduleRepository.deleteListSchedule(listSchedule);
        }
      } else if ("0".equals(mrSynItDevicesDTO.getStatus())) {
        if (listScheduleItInActive.size() > 0) {
          mrITSoftScheduleRepository.deleteListSchedule(listScheduleItInActive);
          mrSynItDevicesDTO.setIsCompleteSoft("1");
        }
      } else if (!mrSynItDevicesDTO.getRegionSoft().equals(mrItDeviceOld.getRegionSoft())
          || !mrSynItDevicesDTO.getGroupCode().equalsIgnoreCase(mrItDeviceOld.getGroupCode())) {
        if (listScheduleItSoft.size() > 0) {
          mrITSoftScheduleRepository.deleteListSchedule(listScheduleItSoft);
          mrSynItDevicesDTO.setIsCompleteSoft("1");
        }
      }
      if (!mrSynItDevicesDTO.getMrSoft().equals(mrItDeviceOld.getMrSoft()) && !checkIsCommittee) {
        mrSynItDevicesDTO.setApproveStatus("0");
        mrSynItDevicesDTO.setApproveReason("");
        mrSynItDevicesDTO
            .setMrSoft(mrItDeviceOld.getMrSoft());//giu nguyen trang thai bao duong cua thiet bi
      }
      if (checkIsCommittee && "1".equals(mrSynItDevicesDTO.getApproveStatus())) {
        mrSynItDevicesDTO.setIsCompleteSoft("1");
      }
      resultInSideDto = mrSynItSoftDevicesRepository.updateMrSynItDevice(mrSynItDevicesDTO);
    }
    return resultInSideDto;
  }

  public void inActive(MrSynItDevicesDTO mrSynItDeviceDTO,
      List<MrITSoftScheduleDTO> inActiveList) {
    MrITSoftScheduleDTO scheduleItDTO = new MrITSoftScheduleDTO();
    String inActive = "0";
    if (StringUtils.isNotNullOrEmpty(mrSynItDeviceDTO.getStatus())) {
      if (inActive.equals(mrSynItDeviceDTO.getStatus())) {
        scheduleItDTO.setDeviceId(mrSynItDeviceDTO.getObjectId());
        scheduleItDTO.setMrSoft("1");
        List<MrITSoftScheduleDTO> listSchedule = mrITSoftScheduleRepository
            .getListScheduleMove(scheduleItDTO);
        List<MrScheduleITHisDTO> listMrScheduleItHis = new ArrayList();
        if (listSchedule != null && listSchedule.size() > 0) {
          for (MrITSoftScheduleDTO schedule : listSchedule) {
            listMrScheduleItHis.add(convertScheduleTelHis(schedule, 0));
            inActiveList.add(schedule);
//            checkCycleHard(mrDeviceDTO, schedule);
            mrSynItDeviceDTO.setIsCompleteSoft("1");
          }
          if (listMrScheduleItHis.size() > 0) {
            mrITHisRepository.insertUpdateListScheduleHis(listMrScheduleItHis);
          }
        }
      }
    }
  }

  public void processAttention(MrSynItDevicesDTO mrSynItDevicesDTO, MrSynItDevicesDTO mrDeviceOld,
      List<MrITSoftScheduleDTO> listScheduleItSoft) {
    MrITSoftScheduleDTO scheduleItDTO = new MrITSoftScheduleDTO();
    scheduleItDTO.setDeviceId(mrSynItDevicesDTO.getObjectId());
    scheduleItDTO.setDeviceCode(mrSynItDevicesDTO.getObjectCode());
    scheduleItDTO.setMrSoft("1");
    List<MrITSoftScheduleDTO> listSchedule = mrITSoftScheduleRepository
        .getListScheduleMove(scheduleItDTO);
    boolean checkRegion = true;
    boolean isUpdateGroupOrRegion = false;
    if (StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getRegionSoft()) || StringUtils
        .isNotNullOrEmpty(mrSynItDevicesDTO.getGroupCode()) || !StringUtils
        .isStringNullOrEmpty(mrSynItDevicesDTO.getStation())) {
      if (StringUtils.isStringNullOrEmpty(mrDeviceOld.getGroupCode())) {
        mrDeviceOld.setGroupCode("");
      }
      if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getGroupCode())) {
        mrSynItDevicesDTO.setGroupCode("");
      }
      if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getRegionSoft())) {
        mrSynItDevicesDTO.setRegionSoft("");
      }
      if (StringUtils.isStringNullOrEmpty(mrDeviceOld.getRegionSoft())) {
        mrDeviceOld.setRegionSoft("");
      }
      if ((StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getGroupCode())
          && !mrSynItDevicesDTO.getGroupCode().equals(mrDeviceOld.getGroupCode()))
          || (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getGroupCode())
          && StringUtils.isNotNullOrEmpty(mrDeviceOld.getGroupCode()))
          || (StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getRegionSoft())
          && !mrSynItDevicesDTO.getRegionSoft().equals(mrDeviceOld.getRegionSoft()))
          || (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getRegionSoft())
          && StringUtils.isNotNullOrEmpty(mrDeviceOld.getRegionSoft()))) {
        isUpdateGroupOrRegion = true;
      }
      if (isUpdateGroupOrRegion) {

        List<MrScheduleITHisDTO> listMrScheduleItHis = new ArrayList();
        if (listSchedule != null && listSchedule.size() > 0) {
          // cap nhap lich sang mr_schedule_IT_his
          for (MrITSoftScheduleDTO schedule : listSchedule) {
            if (schedule.getMrId() == null && schedule.getCrId() == null) {
              listMrScheduleItHis.add(convertScheduleTelHis(schedule, 1));
              listScheduleItSoft.add(schedule);
//              checkCycleHard(mrDeviceDTO, schedule);
              checkRegion = false;
            } else {
              if (schedule.getMrId() != null && schedule.getCrId() != null) {
                MrInsideDTO mrInsideDTO = mrRepository.findMrById(Long.valueOf(schedule.getMrId()));
                CrInsiteDTO crInsiteDTO = crServiceProxy
                    .findCrByIdProxy(Long.valueOf(schedule.getCrId()));
                if (mrInsideDTO != null && crInsiteDTO != null) {
                  if (StringUtils.isNotNullOrEmpty(mrInsideDTO.getState()) && !StringUtils
                      .isStringNullOrEmpty(crInsiteDTO.getStatus())) {
                    if (mrInsideDTO.getState().equals(Constants.MR_STATE_CODE.CLOSE) && crInsiteDTO
                        .getStatus().equals(String.valueOf(WO_STATE.CLOSED_CD))) {
//                      checkCycleHard(mrDeviceDTO, schedule);
                      mrSynItSoftDevicesRepository.updateMrSynItDevice(mrSynItDevicesDTO);
                    }
                  }
                }
              }
            }
          }
          if (listMrScheduleItHis.size() > 0) {
            mrITHisRepository.insertUpdateListScheduleHis(listMrScheduleItHis);
          }
        }
      }
      if (checkIsComplete) {
        if (checkRegion) {
          updateIsComplete(listSchedule, mrSynItDevicesDTO, mrDeviceOld);
        }
      }
    }
  }

  public void updateIsComplete(List<MrITSoftScheduleDTO> schedules, MrSynItDevicesDTO dto,
      MrSynItDevicesDTO dtoOld) {
    if (schedules != null && !schedules.isEmpty()) {
    } else {
      if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsCompleteSoft()) && "1"
          .equals(dtoOld.getIsCompleteSoft())) {
        dto.setIsCompleteSoft("1");
      }
    }
  }

  public MrScheduleITHisDTO convertScheduleTelHis(MrITSoftScheduleDTO dto, int type) {
    MrScheduleITHisDTO mrScheduleITHisDTO = new MrScheduleITHisDTO();
    mrScheduleITHisDTO.setArrayCode(dto.getArrayCode());
    mrScheduleITHisDTO.setArrayName(dto.getArrayName());
    mrScheduleITHisDTO.setCrId(dto.getCrId());
    mrScheduleITHisDTO.setCrNumber(dto.getCrNumber());
    mrScheduleITHisDTO.setCycle(dto.getCycle());
    mrScheduleITHisDTO.setDeviceCode(dto.getDeviceCode());
    mrScheduleITHisDTO
        .setDeviceId(dto.getDeviceId());
    mrScheduleITHisDTO.setDeviceName(dto.getDeviceName());
    mrScheduleITHisDTO.setDeviceType(dto.getDeviceType());
    mrScheduleITHisDTO.setMarketCode(dto.getMarketCode());
    mrScheduleITHisDTO.setMarketName(dto.getMarketName());
    mrScheduleITHisDTO.setNetworkType(dto.getNetworkType());
    mrScheduleITHisDTO.setMrId(dto.getMrId());
    mrScheduleITHisDTO.setMrType(dto.getMrType());
    mrScheduleITHisDTO.setMrDate(dto.getMrDate());
    mrScheduleITHisDTO.setMrContent(dto.getMrContentId());
    mrScheduleITHisDTO.setMrCode(dto.getMrCode());
    mrScheduleITHisDTO.setMrMode("S");
    if (!StringUtils.isStringNullOrEmpty(dto.getCrId())) {
      mrScheduleITHisDTO
          .setCrNumber(
              genCrNumber(Long.valueOf(dto.getCrId()), dto.getTypeCr(),
                  dto.getArrayActionName()));
    }
    mrScheduleITHisDTO
        .setProcedureId(dto.getProcedureId());
    mrScheduleITHisDTO.setProcedureName(dto.getProcedureName());
    mrScheduleITHisDTO.setRegion(dto.getRegion());
    if (type == 1) {
      mrScheduleITHisDTO.setNote(I18n.getLanguage("mrDeviceSynItSort.node"));
    } else {
      mrScheduleITHisDTO.setNote(I18n.getLanguage("mrDevice.node.status"));
    }
    return mrScheduleITHisDTO;
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
    XSSFSheet sheetParam5 = workbook.createSheet("param5");
    XSSFSheet sheetParam6 = workbook.createSheet("param6");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrSynItSoftDevice.idStr"),
        I18n.getLanguage("mrSynItSoftDevice.marketName"),
        I18n.getLanguage("mrSynItSoftDevice.regionSoft"),
        I18n.getLanguage("mrSynItSoftDevice.arrayCodeStr"),
        I18n.getLanguage("mrSynItSoftDevice.deviceTypeStr"),
        I18n.getLanguage("mrSynItSoftDevice.ipNode"),
        I18n.getLanguage("mrSynItSoftDevice.objectCode"),
        I18n.getLanguage("mrSynItSoftDevice.objectName"),
        I18n.getLanguage("mrSynItSoftDevice.createUserSoft"),
        I18n.getLanguage("mrSynItSoftDevice.notes"),
        I18n.getLanguage("mrSynItSoftDevice.mrSoftStr"),
        I18n.getLanguage("mrSynItSoftDevice.mrConfirmSoftStr"),
        I18n.getLanguage("mrSynItSoftDevice.nodeAffected"),
        I18n.getLanguage("mrSynItSoftDevice.groupCode"),
        I18n.getLanguage("mrSynItSoftDevice.vendor"),
        I18n.getLanguage("mrSynItSoftDevice.stationStr"),
        I18n.getLanguage("mrSynItSoftDevice.statusStr"),
        I18n.getLanguage("mrSynItSoftDevice.boUnitName"),
        I18n.getLanguage("mrSynItSoftDevice.isRunMopName")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("mrSynItSoftDevice.marketName"),
        I18n.getLanguage("mrSynItSoftDevice.arrayCodeStr"),
        I18n.getLanguage("mrSynItSoftDevice.deviceTypeStr"),
        I18n.getLanguage("mrSynItSoftDevice.objectCode"),
        I18n.getLanguage("mrSynItSoftDevice.objectName"),
        I18n.getLanguage("mrSynItSoftDevice.createUserSoft"),
        I18n.getLanguage("mrSynItSoftDevice.mrSoftStr"),
        I18n.getLanguage("mrSynItSoftDevice.statusStr"),
        I18n.getLanguage("mrSynItSoftDevice.boUnitName"),
        I18n.getLanguage("mrSynItSoftDevice.isRunMopName")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int marketNameColumn = listHeader
        .indexOf(I18n.getLanguage("mrSynItSoftDevice.marketName"));
    int arrayCodeStrColumn = listHeader.indexOf(I18n.getLanguage("mrSynItSoftDevice.arrayCodeStr"));
    int statusStrColumn = listHeader.indexOf(I18n.getLanguage("mrSynItSoftDevice.statusStr"));
//    int mrConfirmSoftStrColumn = listHeader
//        .indexOf(I18n.getLanguage("mrSynItSoftDevice.mrConfirmSoftStr"));
    int mrSoftStrColumn = listHeader.indexOf(I18n.getLanguage("mrSynItSoftDevice.mrSoftStr"));
    int isRunMopNameColumn = listHeader.indexOf(I18n.getLanguage("mrSynItSoftDevice.isRunMopName"));
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("mrSynItSoftDevice.title"));
    titleCell.setCellStyle(style.get("title"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(4);
    Row headerMarket = sheetParam2.createRow(0);
    Row headerArray = sheetParam3.createRow(0);
    Row headerUnit = sheetParam5.createRow(0);
    Row headerReason = sheetParam6.createRow(0);
    headerRow.setHeightInPoints(30);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }

    Cell headerCellIdReason = headerReason.createCell(0);
    Cell headerCellReasonName = headerReason.createCell(1);
    XSSFRichTextString idReason = new XSSFRichTextString(
        I18n.getLanguage("mtItSoftSchedule.mrConfirmId"));
    XSSFRichTextString reasonName = new XSSFRichTextString(
        I18n.getLanguage("mtItSoftSchedule.mrConfirmName"));
    headerCellReasonName.setCellValue(reasonName);
    headerCellReasonName.setCellStyle(style.get("header"));
    headerCellIdReason.setCellValue(idReason);
    headerCellIdReason.setCellStyle(style.get("header"));
    sheetParam6.setColumnWidth(0, 2000);
    sheetParam6.setColumnWidth(1, 15000);

    Cell headerCellUnitId = headerUnit.createCell(0);
    Cell headerCellUnit = headerUnit.createCell(1);
    XSSFRichTextString unitId = new XSSFRichTextString(I18n.getLanguage("mrDevice.unitId"));
    XSSFRichTextString unit = new XSSFRichTextString(I18n.getLanguage("mrDevice.unitName"));
    headerCellUnit.setCellValue(unit);
    headerCellUnit.setCellStyle(style.get("header"));
    headerCellUnitId.setCellValue(unitId);
    headerCellUnitId.setCellStyle(style.get("header"));
    sheetParam5.setColumnWidth(0, 2000);
    sheetParam5.setColumnWidth(1, 15000);

    Cell headerCellMarket = headerMarket.createCell(0);
    Cell headerCellRegion = headerMarket.createCell(1);
    XSSFRichTextString market = new XSSFRichTextString(
        I18n.getLanguage("mrSynItSoftDevice.marketName"));
    XSSFRichTextString region = new XSSFRichTextString(
        I18n.getLanguage("mrSynItSoftDevice.regionSoft"));
    headerCellMarket.setCellValue(market);
    headerCellMarket.setCellStyle(style.get("header"));
    headerCellRegion.setCellValue(region);
    headerCellRegion.setCellStyle(style.get("header"));
    sheetParam2.setColumnWidth(0, 15000);
    sheetParam2.setColumnWidth(1, 15000);

    Cell headerCellArray = headerArray.createCell(0);
    Cell headerCellDevice = headerArray.createCell(1);
    XSSFRichTextString array = new XSSFRichTextString(
        I18n.getLanguage("mrSynItSoftDevice.arrayCodeStr"));
    XSSFRichTextString device = new XSSFRichTextString(
        I18n.getLanguage("mrSynItSoftDevice.deviceType"));
    headerCellArray.setCellValue(array);
    headerCellArray.setCellStyle(style.get("header"));
    headerCellDevice.setCellValue(device);
    headerCellDevice.setCellStyle(style.get("header"));
    sheetParam3.setColumnWidth(0, 15000);
    sheetParam3.setColumnWidth(1, 15000);
    sheetOne.setColumnWidth(0, 3000);
    // Set dữ liệu vào column dropdown
    int row = 5;
    List<ItemDataCRInside> lstMarketCode = catLocationRepository
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
    List<CatItemDTO> lstArrayCode = catItemBusiness
        .getListItemByCategoryAndParent(Constants.MR_ITEM_NAME.MR_SUBCATEGORY, null);
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

    Map<String, String> mapRG = new HashMap<>();
    row = 1;
    for (ItemDataCRInside marketCode : lstMarketCode) {
      //khu vực
      if (!mapRG.containsKey(String.valueOf(marketCode.getValueStr()))) {
        List<MrITSoftScheduleDTO> lstRegion = mrITSoftScheduleRepository
            .getListRegionByMrSynItDevices(String.valueOf(marketCode.getValueStr()));
        excelWriterUtils
            .createCell(sheetParam2, 0, row, marketCode.getDisplayStr(), style.get("cell"));
        for (MrITSoftScheduleDTO mrITSoftScheduleDTO : lstRegion) {
          excelWriterUtils
              .createCell(sheetParam2, 1, row++, mrITSoftScheduleDTO.getRegion(),
                  style.get("cell"));
        }
        excelWriterUtils
            .createCell(sheetParam2, 2, row++, null, style.get("cell"));
        mapRG.put(String.valueOf(marketCode.getValueStr()), marketCode.getDisplayStr());
      }
    }
    sheetParam2.autoSizeColumn(0);
    sheetParam2.autoSizeColumn(1);

    row = 1;
    for (CatItemDTO arrayCode : lstArrayCode) {
      List<MrSynItDevicesDTO> lstDeviceType = mrSynItSoftDevicesRepository
          .getListDeviceTypeByArrayCode(arrayCode.getItemValue());
      excelWriterUtils
          .createCell(sheetParam3, 0, row, arrayCode.getItemName(), style.get("cell"));
      for (MrSynItDevicesDTO deviceDTO : lstDeviceType) {
        excelWriterUtils
            .createCell(sheetParam3, 1, row++, deviceDTO.getDeviceType(), style.get("cell"));
      }
      excelWriterUtils
          .createCell(sheetParam3, 2, row++, null, style.get("cell"));
    }
    sheetParam3.autoSizeColumn(0);
    sheetParam3.autoSizeColumn(1);

    row = 5;
    excelWriterUtils
        .createCell(sheetParam, 11, row++, I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.1")
            , style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam, 11, row++, I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.0")
            , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name mrSoftStr = workbook.createName();
    mrSoftStr.setNameName("mrSoftStr");
    mrSoftStr.setRefersToFormula("param!$L$2:$L$" + row);
    XSSFDataValidationConstraint mrSoftStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "mrSoftStr");
    CellRangeAddressList mrSoftStrCreate = new CellRangeAddressList(5, 65000, mrSoftStrColumn,
        mrSoftStrColumn);
    XSSFDataValidation dataValidationMrSoftStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            mrSoftStrConstraint, mrSoftStrCreate);
    dataValidationMrSoftStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationMrSoftStr);

    row = 5;
    excelWriterUtils
        .createCell(sheetParam, isRunMopNameColumn, row++, I18n.getLanguage("common.yes")
            , style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam, isRunMopNameColumn, row++, I18n.getLanguage("common.no")
            , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name isRunMopStr = workbook.createName();
    isRunMopStr.setNameName("isRunMopName");
    isRunMopStr.setRefersToFormula("param!$T$2:$T$" + row);
    XSSFDataValidationConstraint isRunMopStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isRunMopName");
    CellRangeAddressList isRunMopStrCreate = new CellRangeAddressList(5, 65000, isRunMopNameColumn,
        isRunMopNameColumn);
    XSSFDataValidation dataValidationIsRunMop = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isRunMopStrConstraint, isRunMopStrCreate);
    dataValidationIsRunMop.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationIsRunMop);

    List<MrConfigDTO> lstMrConfirmSoftStr = mrScheduleTelRepository.getConfigByGroup("LY_DO_KO_BD");

    row = 1;
    for (MrConfigDTO dto : lstMrConfirmSoftStr) {
      excelWriterUtils
          .createCell(sheetParam6, 0, row, dto.getConfigCode(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam6, 1, row++, dto.getConfigName(), style.get("cell"));
    }
    sheetParam6.autoSizeColumn(0);
    sheetParam6.autoSizeColumn(1);

    row = 5;
    excelWriterUtils
        .createCell(sheetParam, 17, row++, I18n.getLanguage("mrSynItSoftDevice.status.0")
            , style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam, 17, row++, I18n.getLanguage("mrSynItSoftDevice.status.1")
            , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name statusStr = workbook.createName();
    statusStr.setNameName("statusStr");
    statusStr.setRefersToFormula("param!$R$2:$R$" + row);
    XSSFDataValidationConstraint statusStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "statusStr");
    CellRangeAddressList statusStrCreate = new CellRangeAddressList(5, 65000, statusStrColumn,
        statusStrColumn);
    XSSFDataValidation dataValidationStatusStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            statusStrConstraint, statusStrCreate);
    dataValidationStatusStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationStatusStr);

    row = 1;
    List<UnitDTO> unitNameList = unitRepository.getListUnit(null);
    for (UnitDTO dto : unitNameList) {
      excelWriterUtils
          .createCell(sheetParam5, 0, row, dto.getUnitId().toString(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam5, 1, row++, dto.getUnitName(), style.get("cell"));
    }
    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("mrSynItSoftDevice.title"));
    workbook.setSheetName(2, I18n.getLanguage("mrSynItSoftDevice.regionSoft"));
    workbook.setSheetName(3, I18n.getLanguage("mrSynItSoftDevice.arrDevice"));
    workbook.setSheetName(4, I18n.getLanguage("mrSynItSoftDevice.boUnit"));
    workbook.setSheetName(5, I18n.getLanguage("MrScheduleTel.mrConfirmName"));
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

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<MrSynItDevicesDTO> mrDeviceDTOList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
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
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }
        File fileImport = new File(filePath);

        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            19,
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
            19,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }
        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          mapMarketName.clear();
          mapArray.clear();
          mapMrConfirmSoft.clear();
          mapBoUnit.clear();
//          mapWoCdGroup.clear();
          setMapMarketName();
          setMapArray();
          setMrConfirmSoftStr();
          setMapBoUnit();
//          setMapWoCd();
          for (Object[] obj : dataImportList) {
            MrSynItDevicesDTO mrSynItDevicesDTO = new MrSynItDevicesDTO();
            if (!StringUtils.isStringNullOrEmpty(obj[1])) {
              if (!DataUtil.isInteger(obj[1].toString().trim())) {
                mrSynItDevicesDTO
                    .setResultImport(mrSynItDevicesDTO.getResultImport() + "\n" + I18n
                        .getLanguage("mrSynItSoftDevice.errType.deviceIdStr"));
                mrSynItDevicesDTO.setIdStr(obj[1].toString().trim());
              } else {
                mrSynItDevicesDTO.setIdStr(obj[1].toString().trim());
                mrSynItDevicesDTO.setId(obj[1].toString().trim());
              }
            } else {
              mrSynItDevicesDTO.setIdStr(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[2])) {
              mrSynItDevicesDTO.setMarketName(obj[2].toString().trim());
              for (Map.Entry<String, String> item : mapMarketName.entrySet()) {
                if (mrSynItDevicesDTO.getMarketName().equals(item.getValue())) {
                  mrSynItDevicesDTO.setMarketCode(item.getKey());
                  break;
                } else {
                  mrSynItDevicesDTO.setMarketCode(null);
                }
              }
            } else {
              mrSynItDevicesDTO.setMarketName(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[3])) {
              mrSynItDevicesDTO.setRegionSoft(obj[3].toString().trim());
            } else {
              mrSynItDevicesDTO.setRegionSoft(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[4])) {
              mrSynItDevicesDTO.setArrayCodeStr(obj[4].toString().trim());
              for (Map.Entry<String, String> item : mapArray.entrySet()) {
                if (mrSynItDevicesDTO.getArrayCodeStr().equals(item.getValue())) {
                  mrSynItDevicesDTO.setArrayCode(item.getKey());
                  break;
                } else {
                  mrSynItDevicesDTO.setArrayCode(null);
                }
              }
            } else {
              mrSynItDevicesDTO.setArrayCodeStr(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[5])) {
              mrSynItDevicesDTO.setDeviceTypeStr(obj[5].toString().trim());
            } else {
              mrSynItDevicesDTO.setDeviceTypeStr(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[6])) {
              mrSynItDevicesDTO.setIpNode(obj[6].toString().trim());
            } else {
              mrSynItDevicesDTO.setIpNode(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[7])) {
              mrSynItDevicesDTO.setObjectCode(obj[7].toString().trim().toUpperCase());
            } else {
              mrSynItDevicesDTO.setObjectCode(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[8])) {
              mrSynItDevicesDTO.setObjectName(obj[8].toString().trim());
            } else {
              mrSynItDevicesDTO.setObjectName(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[9])) {
              mrSynItDevicesDTO.setCreateUserSoft(obj[9].toString().trim());
            } else {
              mrSynItDevicesDTO.setCreateUserSoft(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[10])) {
              mrSynItDevicesDTO.setNotes(obj[10].toString().trim());
            } else {
              mrSynItDevicesDTO.setNotes(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[11])) {
              mrSynItDevicesDTO.setMrSoftStr(obj[11].toString().trim());
              if (I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.1")
                  .equals(mrSynItDevicesDTO.getMrSoftStr())) {
                mrSynItDevicesDTO.setMrSoft("1");
              } else if (I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.0")
                  .equals(mrSynItDevicesDTO.getMrSoftStr())) {
                mrSynItDevicesDTO.setMrSoft("0");
              } else {
                mrSynItDevicesDTO.setMrSoft(null);
              }
            } else {
              mrSynItDevicesDTO.setMrSoftStr(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[12])) {
              mrSynItDevicesDTO.setMrConfirmSoftStr(obj[12].toString().trim());
            } else {
              mrSynItDevicesDTO.setMrConfirmSoftStr(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[13])) {
              mrSynItDevicesDTO.setNodeAffected(obj[13].toString().trim());
            } else {
              mrSynItDevicesDTO.setNodeAffected(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[14])) {
              mrSynItDevicesDTO.setGroupCode(obj[14].toString().trim());
            } else {
              mrSynItDevicesDTO.setGroupCode(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[15])) {
              mrSynItDevicesDTO.setVendor(obj[15].toString().trim());
            } else {
              mrSynItDevicesDTO.setVendor(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[16])) {
              mrSynItDevicesDTO.setStationStr(obj[16].toString().trim());
              mrSynItDevicesDTO.setStation(obj[16].toString().trim());
            } else {
              mrSynItDevicesDTO.setStationStr(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[17])) {
              mrSynItDevicesDTO.setStatusStr(obj[17].toString().trim());
              if (I18n.getLanguage("mrSynItSoftDevice.status.1")
                  .equals(mrSynItDevicesDTO.getStatusStr())) {
                mrSynItDevicesDTO.setStatus("0");
              } else if (I18n.getLanguage("mrSynItSoftDevice.status.0")
                  .equals(mrSynItDevicesDTO.getStatusStr())) {
                mrSynItDevicesDTO.setStatus("1");
              } else {
                mrSynItDevicesDTO.setStatus(null);
              }
            } else {
              mrSynItDevicesDTO.setStatusStr(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[18])) {
              mrSynItDevicesDTO.setBoUnitName(obj[18].toString().trim());
            } else {
              mrSynItDevicesDTO.setBoUnitName(null);
            }
            if (!StringUtils.isStringNullOrEmpty(obj[19])) {
              mrSynItDevicesDTO.setIsRunMopName(obj[19].toString().trim());
            } else {
              mrSynItDevicesDTO.setIsRunMopName(null);
            }
            if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getResultImport())) {
              MrSynItDevicesDTO mrSynItDevicesDTOTmp = validateImportInfo(
                  mrSynItDevicesDTO, mrDeviceDTOList);
              if (mrSynItDevicesDTOTmp.getResultImport() == null) {
                mrSynItDevicesDTOTmp
                    .setResultImport(I18n.getLanguage("mrSynItSoftDevice.result.import"));
                mrDeviceDTOList.add(mrSynItDevicesDTOTmp);
              } else {
                mrDeviceDTOList.add(mrSynItDevicesDTO);
                index++;
              }
            } else {
              mrDeviceDTOList.add(mrSynItDevicesDTO);
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
    if (count != 20) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItSoftDevice.idStr")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItSoftDevice.marketName") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItSoftDevice.regionSoft")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItSoftDevice.arrayCodeStr") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItSoftDevice.deviceTypeStr") + "*")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItSoftDevice.ipNode")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItSoftDevice.objectCode") + "*")
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItSoftDevice.objectName") + "*")
        .equalsIgnoreCase(objects[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItSoftDevice.createUserSoft") + "*")
        .equalsIgnoreCase(objects[9].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItSoftDevice.notes")
        .equalsIgnoreCase(objects[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItSoftDevice.mrSoftStr") + "*")
        .equalsIgnoreCase(objects[11].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItSoftDevice.mrConfirmSoftStr")
        .equalsIgnoreCase(objects[12].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItSoftDevice.nodeAffected")
        .equalsIgnoreCase(objects[13].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItSoftDevice.groupCode")
        .equalsIgnoreCase(objects[14].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItSoftDevice.vendor")
        .equalsIgnoreCase(objects[15].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItSoftDevice.stationStr")
        .equalsIgnoreCase(objects[16].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItSoftDevice.statusStr") + "*")
        .equalsIgnoreCase(objects[17].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItSoftDevice.boUnitName") + "*")
        .equalsIgnoreCase(objects[18].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItSoftDevice.isRunMopName") + "*")
        .equalsIgnoreCase(objects[19].toString().trim())) {
      return false;
    }
    return true;
  }

  private MrSynItDevicesDTO validateImportInfo(MrSynItDevicesDTO mrSynItDevicesDTO,
      List<MrSynItDevicesDTO> list) {
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMarketName())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.marketName"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMarketCode())) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.marketName.exist"));
      return mrSynItDevicesDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getId()) && StringUtils
        .isStringNullOrEmpty(mrSynItDevicesDTO.getRegionSoft())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.regionSoft"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getId()) && !StringUtils
        .isStringNullOrEmpty(mrSynItDevicesDTO.getRegionSoft())
        && mrSynItDevicesDTO.getRegionSoft().length() > 200) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.regionSoft"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getArrayCodeStr())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.arrayCodeStr"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getArrayCode())) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.arrayCode.exist"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getDeviceTypeStr())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.deviceTypeStr"));
      return mrSynItDevicesDTO;
    }
    MrSynItDevicesDTO deviceCheck = mrSynItSoftDevicesRepository
        .checkDeviceTypeByArrayCode(mrSynItDevicesDTO.getArrayCode(),
            mrSynItDevicesDTO.getDeviceTypeStr());
    if (deviceCheck != null) {
      mrSynItDevicesDTO.setDeviceType(mrSynItDevicesDTO.getDeviceTypeStr());
    } else {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.deviceTypeStr.exist"));
      return mrSynItDevicesDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getIpNode())
        && mrSynItDevicesDTO.getIpNode().length() > 200) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.ipNode"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getObjectCode())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.objectCode"));
      return mrSynItDevicesDTO;
    }
    if (mrSynItDevicesDTO.getObjectCode().length() > 500) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.objectCode"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getObjectName())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.objectName"));
      return mrSynItDevicesDTO;
    }
    if (mrSynItDevicesDTO.getObjectName().length() > 500) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.objectName"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getCreateUserSoft())) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.getCreateUserSoft"));
      return mrSynItDevicesDTO;
    }
    UsersInsideDto usersInsideDto = new UsersInsideDto();
    usersInsideDto.setUsername(mrSynItDevicesDTO.getCreateUserSoft());
    List<UsersInsideDto> userDTOList = userRepository.getListUsersDTOS(usersInsideDto);
    if (userDTOList == null || userDTOList.isEmpty()) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.getCreateUserSoft.exist"));
      return mrSynItDevicesDTO;
    } else {
      if (!StringUtils.isStringNullOrEmpty(userDTOList.get(0).getIsEnable()) && "0"
          .equals(userDTOList.get(0).getIsEnable().toString())) {
        mrSynItDevicesDTO
            .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.getCreateUserSoft.exist"));
        return mrSynItDevicesDTO;
      }
    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getNotes())
        && mrSynItDevicesDTO.getNotes().length() > 2000) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.notes"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrSoftStr())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.mrSoftStr"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrSoft())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.mrSoft.exist"));
      return mrSynItDevicesDTO;
    }

    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrSoftStr())) {
      if (!I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.0")
          .equals(mrSynItDevicesDTO.getMrSoftStr()) && !I18n
          .getLanguage("mrSynItSoftDevice.mrSoftStr.1")
          .equals(mrSynItDevicesDTO.getMrSoftStr())) {
        mrSynItDevicesDTO
            .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.mrConfrim"));
        return mrSynItDevicesDTO;
      }

    }

    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrSoftStr())) {
      if (I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.1")
          .equals(mrSynItDevicesDTO.getMrSoftStr())) {
        if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrConfirmSoftStr())) {
          mrSynItDevicesDTO
              .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.mrConfirmHard.required1"));
          return mrSynItDevicesDTO;
        }

        if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getNodeAffected())) {
          mrSynItDevicesDTO
              .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.mrConfirmHard.required2"));
          return mrSynItDevicesDTO;
        }
      }

      if (I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.0")
          .equals(mrSynItDevicesDTO.getMrSoftStr())) {
        if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrConfirmSoftStr())) {
          mrSynItDevicesDTO
              .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.mrConfirmSoft.required"));
          return mrSynItDevicesDTO;
        }
        if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrConfirmSoftStr())) {
          if (mapMrConfirmSoft.containsKey(mrSynItDevicesDTO.getMrConfirmSoftStr())) {
            mrSynItDevicesDTO.setMrConfirmSoft(mrSynItDevicesDTO.getMrConfirmSoftStr());
          } else {
            mrSynItDevicesDTO
                .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.mrConfirmSoft.exist"));
            return mrSynItDevicesDTO;
          }
        }
      }
    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getNodeAffected())
        && mrSynItDevicesDTO.getNodeAffected().length() > 2000) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.nodeAffected"));
      return mrSynItDevicesDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getGroupCode())
        && mrSynItDevicesDTO.getGroupCode().length() > 2000) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.groupCode"));
      return mrSynItDevicesDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getVendor())
        && mrSynItDevicesDTO.getVendor().length() > 200) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.vendor"));
      return mrSynItDevicesDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getStation())
        && mrSynItDevicesDTO.getStation().length() > 500) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.station"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getStatusStr())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.statusStr"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getStatus())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.status.exist"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getBoUnitName())) {
      if (mapBoUnit.containsKey(mrSynItDevicesDTO.getBoUnitName())) {
        mrSynItDevicesDTO.setBoUnit(mrSynItDevicesDTO.getBoUnitName());
      } else if (mapBoUnit.containsValue(mrSynItDevicesDTO.getBoUnitName())) {
        String key = getKeyMap(mapBoUnit, mrSynItDevicesDTO.getBoUnitName());
        if (StringUtils.isNotNullOrEmpty(key)) {
          mrSynItDevicesDTO.setBoUnit(key);
        }
      } else {
        mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.boUnit.exist"));
        return mrSynItDevicesDTO;
      }
    } else {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.boUnitStr"));
      return mrSynItDevicesDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getIsRunMopName())) {
      if (I18n.getLanguage("common.yes").equals(mrSynItDevicesDTO.getIsRunMopName())) {
        mrSynItDevicesDTO.setIsRunMop(1L);
      } else {
        mrSynItDevicesDTO.setIsRunMop(0L);
      }
    } else {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.isRunMopName"));
      return mrSynItDevicesDTO;
    }
//    MrSynItDevicesDTO mrDevice = mrSynItSoftDevicesRepository
//        .ckeckMrSynItDeviceSoftExist(mrSynItDevicesDTO);
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getId())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.Id.notnull"));
      return mrSynItDevicesDTO;
    } else if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getId())) {
      MrSynItDevicesDTO dto = mrSynItSoftDevicesRepository
          .findMrDeviceById(Long.valueOf(mrSynItDevicesDTO.getId()));
      if (dto == null) {
        mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.noData"));
        return mrSynItDevicesDTO;
      } else {
        mrSynItDevicesDTO.setMrSynItDevicesOld(dto);
        if (!StringUtils
            .isStringNullOrEmpty(dto.getApproveStatus()) && "0"
            .equals(dto.getApproveStatus())) {
          mrSynItDevicesDTO
              .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.approveStatus"));
        }
      }
    }
    if (list != null && list.size() > 0 && mrSynItDevicesDTO.getResultImport() == null) {
      mrSynItDevicesDTO = validateDuplicate(list, mrSynItDevicesDTO);
    }

    return mrSynItDevicesDTO;
  }

  private MrSynItDevicesDTO validateDuplicate(List<MrSynItDevicesDTO> list,
      MrSynItDevicesDTO mrSynItDevicesDTO) {
    for (int i = 0; i < list.size(); i++) {
      MrSynItDevicesDTO mrDeviceTmp = list.get(i);
      if (I18n.getLanguage("mrSynItSoftDevice.result.import")
          .equals(mrDeviceTmp.getResultImport())
          && mrDeviceTmp.getMarketCode().equals(mrSynItDevicesDTO.getMarketCode()) && mrDeviceTmp
          .getObjectCode().equals(mrSynItDevicesDTO.getObjectCode())) {
        mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.dup-code-in-file")
            .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return mrSynItDevicesDTO;
  }

  private ResultInSideDto insertImport(List<MrSynItDevicesDTO> mrDeviceDTOList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    for (MrSynItDevicesDTO mrDeviceDTO : mrDeviceDTOList) {
      if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getId())) {
        UserToken userToken = ticketProvider.getUserToken();
        mrDeviceDTO.setUpdateUser(userToken.getUserName());
        mrDeviceDTO.setUpdateDate(new Date());
        mrDeviceDTO.setLevelImportant("3");
        resultInSideDto = mrSynItSoftDevicesRepository.updateMrSynItDevice(mrDeviceDTO);
      } else {
        MrSynItDevicesDTO mrDeviceOld = mrDeviceDTO.getMrSynItDevicesOld();
        mrDeviceOld.setRegionSoft(mrDeviceDTO.getRegionSoft());
        mrDeviceDTO.setIpNode(StringUtils.isStringNullOrEmpty(mrDeviceDTO.getIpNode()) ? null
            : mrDeviceDTO.getIpNode());
        mrDeviceOld.setCreateUserSoft(mrDeviceDTO.getCreateUserSoft());
        mrDeviceDTO.setIpNode(StringUtils.isStringNullOrEmpty(mrDeviceDTO.getNotes()) ? null
            : mrDeviceDTO.getNotes());
        mrDeviceOld.setMrSoft(mrDeviceDTO.getMrSoft());
        mrDeviceOld.setMrConfirmSoft(
            StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrConfirmSoft()) ? null
                : mrDeviceDTO.getMrConfirmSoft());
        mrDeviceOld.setNodeAffected(
            StringUtils.isStringNullOrEmpty(mrDeviceDTO.getNodeAffected()) ? null
                : mrDeviceDTO.getNodeAffected());
        mrDeviceOld.setGroupCode(
            StringUtils.isStringNullOrEmpty(mrDeviceDTO.getGroupCode()) ? null
                : mrDeviceDTO.getGroupCode());
        mrDeviceOld.setVendor(
            StringUtils.isStringNullOrEmpty(mrDeviceDTO.getVendor()) ? null
                : mrDeviceDTO.getVendor());
        mrDeviceOld.setStation(
            StringUtils.isStringNullOrEmpty(mrDeviceDTO.getStation()) ? null
                : mrDeviceDTO.getStation());
        mrDeviceOld.setStatus(StringUtils.isStringNullOrEmpty(mrDeviceDTO.getStatus()) ? null
            : mrDeviceDTO.getStatus());
        mrDeviceOld.setBoUnit(
            StringUtils.isStringNullOrEmpty(mrDeviceDTO.getBoUnit()) ? null
                : mrDeviceDTO.getBoUnit());
        mrDeviceOld.setIsRunMop(mrDeviceDTO.getIsRunMop());
        resultInSideDto = updateMrItSoftDevice(mrDeviceOld);
      }
    }
    return resultInSideDto;
  }

  private String getKeyMap(Map<String, String> map, String value) {
    List<String> lst = map
        .entrySet()
        .stream()
        .filter(e -> Objects.equals(e.getValue(), value))
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
    if (lst != null && !lst.isEmpty()) {
      return lst.get(0);
    }
    return null;
  }
}
