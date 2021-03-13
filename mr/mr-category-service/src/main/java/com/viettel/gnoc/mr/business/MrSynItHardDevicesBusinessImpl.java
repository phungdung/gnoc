package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
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
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrConfigRepository;
import com.viettel.gnoc.mr.repository.MrHisRepository;
import com.viettel.gnoc.mr.repository.MrITHardScheduleRepository;
import com.viettel.gnoc.mr.repository.MrITHisRepository;
import com.viettel.gnoc.mr.repository.MrITSoftCatMarketRepository;
import com.viettel.gnoc.mr.repository.MrITSoftScheduleRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrSynItHardDevicesRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Drawing;
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
public class MrSynItHardDevicesBusinessImpl implements MrSynItHardDevicesBusiness {

  @Autowired
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  MrConfigRepository repository;

  @Autowired
  MrRepository mrRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UserRepository userRepository;

  @Autowired
  MrSynItHardDevicesRepository mrSynItHardDevicesRepository;

  @Autowired
  MrITSoftScheduleRepository mrITSoftScheduleRepository;

  @Autowired
  MrITHardScheduleRepository mrITHardScheduleRepository;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  MrITHisRepository mrITHisRepository;

  @Autowired
  MrHisRepository mrHisRepository;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  MrITSoftCatMarketRepository mrITSoftCatMarketRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;
  Map<String, String> mapMarket = new HashMap<>();
  Map<String, String> mapImportantLevel = new HashMap<>();
  Map<String, String> mapBoUnit = new HashMap<>();
  Map<String, String> mapWoCdGroup = new HashMap<>();
  Map<String, String> mapMrConfirmHard = new HashMap<>();
  Map<String, String> mapArray = new HashMap<>();
  boolean checkIsComplete = true;

  @Override
  public List<MrSynItDevicesDTO> getListMrSynITDeviceHardDTO(MrSynItDevicesDTO dto, int rowStart,
      int maxRow) {
    List<MrSynItDevicesDTO> result = new ArrayList<>();
    mapMarket.clear();
    mapWoCdGroup.clear();
    mapBoUnit.clear();
    mapMrConfirmHard.clear();
    mapImportantLevel.clear();
    setMapWoCd();
    setMapMarketName();
    setMapBoUnit();
    setMapImportantLevel();
    setMrConfirmHardStr();
    try {
      result = mrSynItSoftDevicesRepository.getListMrSynITDeviceSoftDTO(dto, rowStart, maxRow);
      if (result != null && !result.isEmpty()) {
        for (MrSynItDevicesDTO item : result) {
          if (!StringUtils.isStringNullOrEmpty(item.getMarketCode())) {
            String marketName = mapMarket.get(item.getMarketCode());
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
          if (!StringUtils.isStringNullOrEmpty(item.getCdId())) {
            String cdName = mapWoCdGroup.get(item.getCdId());
            if (!StringUtils.isStringNullOrEmpty(cdName)) {
              item.setCdIdName(cdName);
            }
          }
          if (!StringUtils.isStringNullOrEmpty(item.getMrConfirmHard())) {
            if (mapMrConfirmHard.containsKey(item.getMrConfirmHard())) {
              item.setMrConfirmSoftDisplay(mapMrConfirmHard.get(item.getMrConfirmHard()));
            }
          }
          if (!StringUtils.isStringNullOrEmpty(item.getLevelImportant())) {
            if (mapImportantLevel.containsKey(item.getLevelImportant())) {
              item.setLevelImportantName(mapImportantLevel.get(item.getLevelImportant()));
            }
          }
          if (!StringUtils.isStringNullOrEmpty(item.getBoUnitHard())) {
            if (mapBoUnit.containsKey(item.getBoUnitHard())) {
              item.setBoUnitHardName(mapBoUnit.get(item.getBoUnit()));
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public Datatable getListMrSynITDeviceHardPage(MrSynItDevicesDTO dto) {
    UserToken userToken = ticketProvider.getUserToken();
    mapMarket.clear();
    mapWoCdGroup.clear();
    mapImportantLevel.clear();
    setMapMarketName();
    setMapWoCd();
    setMapImportantLevel();
    dto.setUserLogin(userToken.getUserName());
    Datatable datatable = mrSynItHardDevicesRepository.getListMrSynITDeviceHardPage(dto);
    List<MrSynItDevicesDTO> mrDeviceDTOList = (List<MrSynItDevicesDTO>) datatable.getData();
    if (mrDeviceDTOList != null && !mrDeviceDTOList.isEmpty()) {
      for (MrSynItDevicesDTO deviceDTO : mrDeviceDTOList) {
        if (!StringUtils.isStringNullOrEmpty(deviceDTO.getMarketCode())) {
          String marketName = mapMarket.get(deviceDTO.getMarketCode());
          if (!StringUtils.isStringNullOrEmpty(marketName)) {
            deviceDTO.setMarketName(marketName);
          }
        }
        if (StringUtils.isStringNullOrEmpty(dto.getApproveStatusHard())) {
          if ("0".equals(dto.getApproveStatus())) {
            dto.setApproveStatusName(I18n.getLanguage("mrDevice.approveStatus.0"));
          } else if ("1".equals(dto.getApproveStatus())) {
            dto.setApproveStatusName(I18n.getLanguage("mrDevice.approveStatus.1"));
          } else if ("2".equals(dto.getApproveStatus())) {
            dto.setApproveStatusName(I18n.getLanguage("mrDevice.approveStatus.2"));
          }
        }
        if ("1".equals(deviceDTO.getMrHard())) {
          deviceDTO.setMrHardStr(I18n.getLanguage("common.yes"));
        } else if ("0".equals(deviceDTO.getMrHard())) {
          deviceDTO.setMrHardStr(I18n.getLanguage("common.no"));
        }
        if (!StringUtils.isStringNullOrEmpty(deviceDTO.getLevelImportant())) {
          if (mapImportantLevel.containsKey(deviceDTO.getLevelImportant())) {
            deviceDTO.setLevelImportantName(mapImportantLevel.get(deviceDTO.getLevelImportant()));
          }
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getCdId())) {
          String cdName = mapWoCdGroup.get(dto.getCdId());
          if (!StringUtils.isStringNullOrEmpty(cdName)) {
            dto.setCdIdName(cdName);
          }
        }
        //checkApprove
        if (deviceDTO.getBoUnitHard() != null) {
          log.info(String.valueOf(userToken.getDeptId()));
          String unitApprove = userRepository.getUnitParentForApprove("1",
              deviceDTO.getBoUnitHard());
          if (userToken.getDeptId() != null && unitApprove != null
              && userToken.getDeptId().equals(Long.parseLong(unitApprove))
              && StringUtils.isNotNullOrEmpty(deviceDTO.getApproveStatusHard()) && deviceDTO
              .getApproveStatusHard()
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
  public MrSynItDevicesDTO getMrSynItDevicesHardDetail(Long id) {
    log.debug("Request to getMrSynItDevicesHardDetail: {}", id);
    return mrSynItHardDevicesRepository.getMrSynItDevicesHardDetail(id);
  }

  public void setMapImportantLevel() {
    mapImportantLevel
        .put("1", I18n.getLanguage("mrSynItHardDevice.importantLevel.veryImportant"));
    mapImportantLevel.put("2", I18n.getLanguage("mrSynItHardDevice.importantLevel.important"));
    mapImportantLevel.put("3", I18n.getLanguage("mrSynItHardDevice.importantLevel.normal"));
  }

  public void setMapBoUnit() {
    List<UnitDTO> lsUnit = unitRepository.getListUnit(new UnitDTO());
    if (lsUnit != null && !lsUnit.isEmpty()) {
      for (UnitDTO dto : lsUnit) {
        mapBoUnit.put(String.valueOf(dto.getUnitId()), dto.getUnitName());
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
  public File ExportData(MrSynItDevicesDTO mrSynItDevicesDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    mrSynItDevicesDTO.setUserLogin(userToken.getUserName());
    List<MrSynItDevicesDTO> mrSynITDeviceHardList = mrSynItHardDevicesRepository
        .getListMrSynItDeviceHardExport(mrSynItDevicesDTO);
    if (mrSynITDeviceHardList != null && !mrSynITDeviceHardList.isEmpty()) {
      mapImportantLevel.clear();
      setMapImportantLevel();
      for (int i = mrSynITDeviceHardList.size() - 1; i > -1; i--) {
        MrSynItDevicesDTO mrDeviceExport = mrSynITDeviceHardList.get(i);
        if (StringUtils.isNotNullOrEmpty(mrDeviceExport.getStatus())) {
          if ("0".equals(mrDeviceExport.getStatus())) {
            mrDeviceExport.setStatusStr(I18n.getLanguage("mrDevice.status.inActive"));
          } else if ("1".equals(mrDeviceExport.getStatus())) {
            mrDeviceExport.setStatusStr(I18n.getLanguage("mrDevice.status.active"));
          }
        }
        if (StringUtils.isNotNullOrEmpty(mrDeviceExport.getMrHard())) {
          if ("1".equals(mrDeviceExport.getMrHard())) {
            mrDeviceExport.setMrHardStr(I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.1"));
            mrDeviceExport.setMrConfirmHard("");
          } else if ("0".equals(mrDeviceExport.getMrHard())) {
            mrDeviceExport.setMrHardStr(I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.0"));
          }
        }
        if (StringUtils.isNotNullOrEmpty(mrDeviceExport.getApproveStatusHard())) {
          if ("0".equals(mrDeviceExport.getApproveStatusHard())) {
            mrDeviceExport.setApproveStatusHardName(
                I18n.getLanguage("mrSynItHardDevice.approveStatusHard.0"));
          } else if ("1".equals(mrDeviceExport.getApproveStatusHard())) {
            mrDeviceExport.setApproveStatusHardName(
                I18n.getLanguage("mrSynItHardDevice.approveStatusHard.1"));
          } else if ("2".equals(mrDeviceExport.getApproveStatusHard())) {
            mrDeviceExport.setApproveStatusHardName(
                I18n.getLanguage("mrSynItHardDevice.approveStatusHard.2"));
          }
        }
        String levelImportant = mrDeviceExport.getLevelImportant();
        if (!StringUtils.isStringNullOrEmpty(levelImportant) && mapImportantLevel
            .containsKey(levelImportant)) {
          mrDeviceExport.setLevelImportantName(mapImportantLevel.get(levelImportant));
        }
      }
    }
    return exportFileTemplate(mrSynITDeviceHardList, "");
  }

  @Override
  public ResultInSideDto deleteMrSynItHardDevice(MrSynItDevicesDTO mrSynItDevicesDTO) {
    log.debug("Request to delete: {}", mrSynItDevicesDTO);
    ResultInSideDto resultInSideDto;
    List<MrITHardScheduleDTO> listSchedule = new ArrayList<>();
    MrITHardScheduleDTO scheduleItDTO = new MrITHardScheduleDTO();
    scheduleItDTO.setDeviceId(mrSynItDevicesDTO.getObjectId());
    scheduleItDTO.setDeviceCode(mrSynItDevicesDTO.getObjectCode());
    List<MrITHardScheduleDTO> schedules = mrITHardScheduleRepository
        .getListScheduleMove(scheduleItDTO);
    listSchedule.addAll(schedules);
    resultInSideDto = mrITHardScheduleRepository.deleteListSchedule(schedules);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      resultInSideDto = mrSynItSoftDevicesRepository
          .deleteMrSynItSoftDevice(Long.valueOf(mrSynItDevicesDTO.getId()));
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        if (!listSchedule.isEmpty()) {
          mrSynItDevicesDTO.setCheckNode("0");
          resultInSideDto = insertMrScheduleTelHis(listSchedule, mrSynItDevicesDTO);
        }
      }
    }
    return resultInSideDto;
  }

  private ResultInSideDto insertMrScheduleTelHis(List<MrITHardScheduleDTO> listScheduleIt,
      MrSynItDevicesDTO mrSynItDevicesDTO) {
    MrInsideDTO mrInsideDTO = new MrInsideDTO();
    List<MrScheduleITHisDTO> listScheduleITHis = new ArrayList<>();
    MrITHardScheduleDTO scheduleItDTO = listScheduleIt.get(0);
    mrInsideDTO.setMrId(scheduleItDTO.getMrId());
    if (mrInsideDTO.getMrId() != null) {
      mrInsideDTO = mrRepository.findMrById(mrInsideDTO.getMrId());
    }
    for (MrITHardScheduleDTO itDTO : listScheduleIt) {
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
          .setMrDate(itDTO.getNextDateModify() != null ? DateTimeUtils
              .convertDateToString(itDTO.getNextDateModify()) : null);
      scheduleHisDTO.setMrContent(mrInsideDTO == null ? null
          : mrInsideDTO.getMrContentId());
      scheduleHisDTO.setMrMode("H");
      scheduleHisDTO.setMrType(mrInsideDTO == null ? null
          : mrInsideDTO.getMrType());
      scheduleHisDTO.setMrId(itDTO.getMrId() != null ? String.valueOf(itDTO.getMrId()) : null);
      scheduleHisDTO.setMrCode(mrInsideDTO == null ? null
          : mrInsideDTO.getMrCode());
      if (!StringUtils.isStringNullOrEmpty(itDTO.getWoId())) {
        scheduleHisDTO.setWoId(itDTO.getWoId().toString());
      }
      scheduleHisDTO.setProcedureId(
          itDTO.getProcedureId() != null ? String.valueOf(itDTO.getProcedureId()) : null);
      scheduleHisDTO.setProcedureName(itDTO.getProcedureName());
      scheduleHisDTO.setNetworkType(itDTO.getNetworkType());
      scheduleHisDTO.setCycle(itDTO.getCycle() != null ? String.valueOf(itDTO.getCycle()) : null);
      scheduleHisDTO.setRegion(itDTO.getRegion());
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getCheckNode()) && "1"
          .equals(mrSynItDevicesDTO.getCheckNode())) {
        scheduleHisDTO.setNote(I18n.getLanguage("mrScheduleTelhis.note.mrConfirm"));
      } else {
        scheduleHisDTO.setNote(I18n.getLanguage("mrScheduleTelhis.note.delete"));
      }
      listScheduleITHis.add(scheduleHisDTO);
    }
    return mrITHisRepository.insertUpdateListScheduleHis(listScheduleITHis);
  }

  private File exportFileTemplate(List<MrSynItDevicesDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("mrSynItHardDevice.export.title");
    String title = I18n.getLanguage("mrSynItHardDevice.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      headerExportList = readerHeaderSheet("id", "marketName", "regionHard", "arrayCodeStr",
          "deviceTypeStr", "objectCode", "objectName", "ipNode", "createUserHard", "userMrHard",
          "mrHardStr", "notes", "vendor", "station", "statusStr", "mrConfirmHardStr",
          "cdIdName", "boUnitHardName", "approveStatusHardName", "levelImportantName", "synDate",
          "db", "ud", "upTime", "resultImport");
      fileNameOut = "MR_IT_HARD_DEVICE_RESULT_IMPORT";
      subTitle = null;
    } else {
      headerExportList = readerHeaderSheet("id", "marketName", "regionHard", "arrayCodeStr",
          "deviceType", "objectCode", "objectName", "ipNode", "createUserHard", "userMrHard",
          "mrHardStr", "notes", "vendor", "station", "statusStr", "mrConfirmHardDisplay",
          "cdIdName",
          "boUnitHardName", "approveStatusHardName", "levelImportantName", "synDate", "db", "ud",
          "upTime");
      fileNameOut = "MR_IT_HARD_DEVICE_EXPORT";
      subTitle = I18n
          .getLanguage("mrSynItHardDevice.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.mrSynItHardDevice"
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

  private List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  //
  @Override
  public ResultInSideDto approveItHardDevice(MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    MrSynItDevicesDTO mrDeviceOld = mrSynItSoftDevicesRepository
        .findMrDeviceById(Long.valueOf(mrSynItDevicesDTO.getId()));
    if (mrDeviceOld == null) {
      resultInSideDto.setKey(RESULT.ERROR);
      return resultInSideDto;
    }
    List<MrITHardScheduleDTO> lstMr = new ArrayList<>();
    mrDeviceOld.setApproveReasonHard(mrSynItDevicesDTO.getApproveReasonHard());
    mrDeviceOld
        .setApproveStatusHard("1".equals(mrSynItDevicesDTO.getApproveStatusHard()) ? "1" : "2");
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getApproveStatusHard()) && "1"
        .equals(String.valueOf(mrSynItDevicesDTO.getApproveStatusHard()))) {
      mrDeviceOld.setMrHard("1".equalsIgnoreCase(mrSynItDevicesDTO.getMrHard()) ? "0" : "1");
      if (StringUtils.isNotNullOrEmpty(mrDeviceOld.getMrConfirmHard())) {
        MrITHardScheduleDTO mrITHardScheduleDTO = new MrITHardScheduleDTO();
        mrITHardScheduleDTO.setDeviceId(mrSynItDevicesDTO.getObjectId());
        mrITHardScheduleDTO.setDeviceCode(mrSynItDevicesDTO.getObjectCode());
        List<MrITHardScheduleDTO> listSchedule = mrITHardScheduleRepository
            .getListScheduleMove(mrITHardScheduleDTO);
        if (listSchedule != null && !listSchedule.isEmpty()) {
          mrDeviceOld.setCheckNode("1");
          insertMrScheduleTelHis(listSchedule, mrDeviceOld); //sinh lịch sử từ list Lịch
          for (MrITHardScheduleDTO dtoUpdate : listSchedule) {
            checkCycleHard(mrDeviceOld, dtoUpdate); //set lại iscomplete theo cycle
            if (!StringUtils.isStringNullOrEmpty(dtoUpdate.getMrId())) {
              lstMr.add(dtoUpdate);
            }
          }
        }
        if (listSchedule.size() > 0) {
          mrITHardScheduleRepository.deleteListSchedule(listSchedule);
        }
        //xoa MR neu thay doi trang thai tu Co BD -> Ko BD
        if (!lstMr.isEmpty()) {
          for (MrITHardScheduleDTO mr : lstMr) {
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
      if ("1".equals(mrDeviceOld.getMrHard()) && StringUtils
          .isNotNullOrEmpty(mrDeviceOld.getMrConfirmHard())) {
        mrDeviceOld.setMrConfirmHard(null);
      }
    } else {
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrHard()) && "1"
          .equals(mrSynItDevicesDTO.getMrHard())) {
        mrDeviceOld.setMrConfirmHard(null);
      } else if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrHard()) && "0"
          .equals(mrSynItDevicesDTO.getMrHard())) {
        mrDeviceOld.setMrConfirmHard(mrSynItDevicesDTO.getMrConfirmHard());
      }
    }
    return mrSynItSoftDevicesRepository.updateMrSynItDevice(mrDeviceOld);

  }

  @Override
  public ResultInSideDto updateMrSynItHardDevice(MrSynItDevicesDTO mrSynItDevicesDTO) {
    log.debug("Request to updateMrSynItHardDevice: {}", mrSynItDevicesDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getBoUnitHard())) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("mrSynItSoftDevice.err.boUnitHard.notnull"));
      return resultInSideDto;
    }
    UserToken userToken = ticketProvider.getUserToken();
    boolean checkIsCommittee = false;
    MrSynItDevicesDTO mrItDeviceOld = mrSynItSoftDevicesRepository
        .findMrDeviceById(Long.valueOf(mrSynItDevicesDTO.getId()));
    if (mrItDeviceOld != null) {
      mrItDeviceOld.setUpdateDate(new Date());
      mrItDeviceOld.setUpdateUser(userToken.getUserName());
      mrItDeviceOld
          .setIsComplete1m(StringUtils.isStringNullOrEmpty(mrItDeviceOld.getIsComplete1m()) ? null
              : mrItDeviceOld.getIsComplete1m());
      mrItDeviceOld
          .setIsComplete3m(StringUtils.isStringNullOrEmpty(mrItDeviceOld.getIsComplete3m()) ? null
              : mrItDeviceOld.getIsComplete3m());
      mrItDeviceOld
          .setIsComplete6m(StringUtils.isStringNullOrEmpty(mrItDeviceOld.getIsComplete6m()) ? null
              : mrItDeviceOld.getIsComplete6m());
      mrItDeviceOld
          .setIsComplete12m(StringUtils.isStringNullOrEmpty(mrItDeviceOld.getIsComplete12m()) ? null
              : mrItDeviceOld.getIsComplete12m());
      List<MrITHardScheduleDTO> listScheduleItHard = new ArrayList<>();
      List<MrITHardScheduleDTO> listScheduleItInActive = new ArrayList<>();
      MrITHardScheduleDTO mrITHardScheduleDTO = new MrITHardScheduleDTO();
      mrITHardScheduleDTO.setDeviceId(mrSynItDevicesDTO.getObjectId());
      mrITHardScheduleDTO.setDeviceCode(mrSynItDevicesDTO.getObjectCode());
      List<MrITHardScheduleDTO> listSchedule = mrITHardScheduleRepository
          .getListScheduleMove(mrITHardScheduleDTO);

      if (userToken.getDeptId() != null && mrSynItDevicesDTO.getBoUnitHard() != null
          && userToken.getDeptId().equals(mrSynItDevicesDTO.getBoUnitHard())) {
        UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
        if (unitDTO.getIsCommittee().equals(0L)) {
          if (userRepository.checkRoleOfUser("TP", userToken.getUserID())) {
            checkIsCommittee = true;
          }
        }
      }
      //dungpv edit
      if (mrSynItDevicesDTO.getBoUnitHard() != null) {
        String unitApprove = userRepository.getUnitParentForApprove("1",
            mrSynItDevicesDTO.getBoUnitHard());
        if (userToken.getDeptId() != null && unitApprove != null
            && userToken.getDeptId().equals(Long.parseLong(unitApprove))) {
          UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
          if (unitDTO.getIsCommittee().equals(0L)) {
            if (userRepository.checkRoleOfUser("TP", userToken.getUserID())) {
              checkIsCommittee = true;
            }
          }
        }
      }

      //Start cap nhat tu khong bao duong ve co bao duong
      if ("1".equals(mrSynItDevicesDTO.getMrHard()) && StringUtils
          .isStringNullOrEmpty(mrSynItDevicesDTO.getMrConfirmHard()) && StringUtils
          .isNotNullOrEmpty(mrSynItDevicesDTO.getMrConfirmHard())) {
        if (checkIsCommittee) {
          mrSynItDevicesDTO.setApproveStatusHard("1");
          mrSynItDevicesDTO.setApproveReasonHard("");
          mrSynItDevicesDTO.setMrConfirmHard(null);
        }
        // từ có bảo dưỡng về không bảo dưỡng
      } else if ("0".equals(mrSynItDevicesDTO.getMrHard()) && StringUtils
          .isNotNullOrEmpty(mrSynItDevicesDTO.getMrConfirmHard())) {
        if (checkIsCommittee) {
          mrSynItDevicesDTO.setApproveStatusHard("1");
          mrSynItDevicesDTO.setApproveReasonHard("");
        }
      }
      //end
      if (StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getMrConfirmHard()) && !mrSynItDevicesDTO
          .getMrConfirmHard().equals(mrItDeviceOld.getMrConfirmHard())) {
        if (listSchedule != null && !listSchedule.isEmpty()) {
          mrSynItDevicesDTO.setCheckNode("1");
          if (checkIsCommittee) {
            mrSynItDevicesDTO.setApproveStatusHard("1");
            insertMrScheduleTelHis(listSchedule, mrSynItDevicesDTO); //sinh lịch sử từ list Lịch

            for (MrITHardScheduleDTO dtoUpdate : listSchedule) {
              checkCycleHard(mrSynItDevicesDTO, dtoUpdate); //set lại iscomplete theo cycle
            }
          }
          checkIsComplete = false;
        }
      }
      //xử lý khi thay đổi khu vực
      processAttention(mrSynItDevicesDTO, mrItDeviceOld, listScheduleItHard);
      inActive(mrSynItDevicesDTO, listScheduleItInActive);
      if (StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getMrConfirmHard()) && !mrSynItDevicesDTO
          .getMrConfirmHard().equals(mrItDeviceOld.getMrConfirmHard())
          && checkIsCommittee) {
        if (listSchedule != null && !listSchedule.isEmpty()) {
          List<MrITHardScheduleDTO> lstMr = new ArrayList<>();
          for (MrITHardScheduleDTO dtoUpdate : listSchedule) {
            if (!StringUtils.isStringNullOrEmpty(dtoUpdate.getMrId())) {
              lstMr.add(dtoUpdate);
            }
          }
          if (!lstMr.isEmpty()) {
            for (MrITHardScheduleDTO mr : lstMr) {
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
      if (StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getMrConfirmHard()) && checkIsCommittee) {
        if (listSchedule.size() > 0) {
          mrITHardScheduleRepository.deleteListSchedule(listSchedule);
        }
      } else if ("0".equals(mrSynItDevicesDTO.getStatus())) {
        if (listScheduleItInActive.size() > 0) {
          mrITHardScheduleRepository.deleteListSchedule(listScheduleItInActive);
        }
      } else if (!mrSynItDevicesDTO.getRegionHard().equals(mrItDeviceOld.getRegionHard())
          || !mrSynItDevicesDTO
          .getUserMrHard().equalsIgnoreCase(mrItDeviceOld.getUserMrHard()) || !mrSynItDevicesDTO
          .getStation().equalsIgnoreCase(mrItDeviceOld.getStation())) {
        if (listScheduleItHard.size() > 0) {
          mrITHardScheduleRepository.deleteListSchedule(listScheduleItHard);
        }
      }
      if (!mrSynItDevicesDTO.getMrHard().equals(mrItDeviceOld.getMrHard()) && !checkIsCommittee) {
        mrSynItDevicesDTO.setApproveStatusHard("0");
        mrSynItDevicesDTO.setApproveReasonHard("");
        mrSynItDevicesDTO
            .setMrHard(mrItDeviceOld.getMrHard());//giu nguyen trang thai bao duong cua thiet bi
      }
      resultInSideDto = mrSynItSoftDevicesRepository.updateMrSynItDevice(mrSynItDevicesDTO);
    }
    return resultInSideDto;
  }

  @Override
  public List<MrITHardScheduleDTO> getListRegionByMrSynItHardDevices(String country) {
    return mrITHardScheduleRepository.getListRegionByMrSynItDevices(country);
  }

  private void processAttention(MrSynItDevicesDTO mrSynItDevicesDTO, MrSynItDevicesDTO mrDeviceOld,
      List<MrITHardScheduleDTO> listScheduleItHard) {
    MrITHardScheduleDTO scheduleItDTO = new MrITHardScheduleDTO();
    scheduleItDTO.setDeviceId(mrSynItDevicesDTO.getObjectId());
    scheduleItDTO.setDeviceCode(mrSynItDevicesDTO.getObjectCode());
    scheduleItDTO.setMrHard("1");
    List<MrITHardScheduleDTO> listSchedule = mrITHardScheduleRepository
        .getListScheduleMove(scheduleItDTO);
    boolean checkRegion = true;
    if (StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getRegionHard()) || StringUtils
        .isNotNullOrEmpty(mrSynItDevicesDTO.getUserMrHard()) || !StringUtils
        .isStringNullOrEmpty(mrSynItDevicesDTO.getStation())) {
      if (StringUtils.isStringNullOrEmpty(mrDeviceOld.getUserMrHard())) {
        mrDeviceOld.setUserMrHard("");
      }
      if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getUserMrHard())) {
        mrSynItDevicesDTO.setUserMrHard("");
      }
      if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getStation())) {
        mrSynItDevicesDTO.setStation("");
      }
      if (!mrSynItDevicesDTO.getRegionHard().equals(mrDeviceOld.getRegionHard())
          || !mrSynItDevicesDTO
          .getUserMrHard().equalsIgnoreCase(mrDeviceOld.getUserMrHard()) || !mrSynItDevicesDTO
          .getStation().equalsIgnoreCase(mrDeviceOld.getStation())) {
        List<MrScheduleITHisDTO> listMrScheduleItHis = new ArrayList();
        if (listSchedule != null && listSchedule.size() > 0) {
          // cap nhap lich sang mr_schedule_IT_his
          for (MrITHardScheduleDTO schedule : listSchedule) {
            if (schedule.getMrId() == null && schedule.getWoId() == null) {
              listMrScheduleItHis.add(convertScheduleTelHis(schedule, 1));
              listScheduleItHard.add(schedule);
              checkCycleHard(mrSynItDevicesDTO, schedule);
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
                      checkCycleHard(mrSynItDevicesDTO, schedule);
                      mrSynItHardDevicesRepository.updateMrSynItDevice(mrSynItDevicesDTO);
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

  private void checkCycleHard(MrSynItDevicesDTO mrDeviceDTO, MrITHardScheduleDTO schedule) {
    if ("M".equals(schedule.getCycleType())) {
      if ("1".equals(String.valueOf(schedule.getCycle()))) {
        mrDeviceDTO.setIsComplete1m("1");
      } else if ("3".equals(String.valueOf(schedule.getCycle()))) {
        mrDeviceDTO.setIsComplete3m("1");
      } else if ("6".equals(String.valueOf(schedule.getCycle()))) {
        mrDeviceDTO.setIsComplete6m("1");
      } else if ("12".equals(String.valueOf(schedule.getCycle()))) {
        mrDeviceDTO.setIsComplete12m("1");
      }
    }
  }

  private void updateIsComplete(List<MrITHardScheduleDTO> schedules, MrSynItDevicesDTO dto,
      MrSynItDevicesDTO dtoOld) {
    if (schedules != null && !schedules.isEmpty()) {
      for (MrITHardScheduleDTO dtoUpdate : schedules) {
        if ("M".equals(dtoUpdate.getCycleType())) {
          if ("1".equals(dtoUpdate.getCycle())) {
            if ("0".equals(dtoOld.getIsComplete1m())) {
              dto.setIsComplete1m("0");
            }
          } else if ("3".equals(dtoUpdate.getCycle())) {
            if ("0".equals(dtoOld.getIsComplete3m())) {
              dto.setIsComplete3m("0");
            }
          } else if ("6".equals(dtoUpdate.getCycle())) {
            if ("0".equals(dtoOld.getIsComplete6m())) {
              dto.setIsComplete6m("0");
            }
          } else if ("12".equals(dtoUpdate.getCycle())) {
            if ("0".equals(dtoOld.getIsComplete12m())) {
              dto.setIsComplete12m("0");
            }
          }
        }
      }
    } else {
      if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete1m()) && "1"
          .equals(dtoOld.getIsComplete1m())) {
        dto.setIsComplete1m("1");
      } else if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete1m()) && "0"
          .equals(dtoOld.getIsComplete1m())) {
        dto.setIsComplete1m("0");
      }
      if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete3m()) && "1"
          .equals(dtoOld.getIsComplete3m())) {
        dto.setIsComplete3m("1");
      } else if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete3m()) && "0"
          .equals(dtoOld.getIsComplete3m())) {
        dto.setIsComplete3m("0");
      }
      if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete6m()) && "1"
          .equals(dtoOld.getIsComplete6m())) {
        dto.setIsComplete6m("1");
      } else if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete6m()) && "0"
          .equals(dtoOld.getIsComplete6m())) {
        dto.setIsComplete6m("0");
      }
      if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete12m()) && "1"
          .equals(dtoOld.getIsComplete12m())) {
        dto.setIsComplete12m("1");
      } else if (!StringUtils.isStringNullOrEmpty(dtoOld.getIsComplete12m()) && "0"
          .equals(dtoOld.getIsComplete12m())) {
        dto.setIsComplete12m("0");
      }
    }
  }

  private void inActive(MrSynItDevicesDTO mrSynItDeviceDTO,
      List<MrITHardScheduleDTO> inActiveList) {
    MrITHardScheduleDTO scheduleItDTO = new MrITHardScheduleDTO();
    String inActive = "0";
    if (StringUtils.isNotNullOrEmpty(mrSynItDeviceDTO.getStatus())) {
      if (inActive.equals(mrSynItDeviceDTO.getStatus())) {
        scheduleItDTO.setDeviceId(mrSynItDeviceDTO.getObjectId());
        scheduleItDTO.setMrHard("1");
        List<MrITHardScheduleDTO> listSchedule = mrITHardScheduleRepository
            .getListScheduleMove(scheduleItDTO);
        List<MrScheduleITHisDTO> listMrScheduleItHis = new ArrayList();
        if (listSchedule != null && listSchedule.size() > 0) {
          for (MrITHardScheduleDTO schedule : listSchedule) {
            listMrScheduleItHis.add(convertScheduleTelHis(schedule, 0));
            inActiveList.add(schedule);
            checkCycleHard(mrSynItDeviceDTO, schedule);
          }
          if (listMrScheduleItHis.size() > 0) {
            mrITHisRepository.insertUpdateListScheduleHis(listMrScheduleItHis);
          }
        }
      }
    }
  }

  public MrScheduleITHisDTO convertScheduleTelHis(MrITHardScheduleDTO dto, int type) {
    MrScheduleITHisDTO mrScheduleITHisDTO = new MrScheduleITHisDTO();
    mrScheduleITHisDTO.setArrayCode(dto.getArrayCode());
    mrScheduleITHisDTO.setArrayName(dto.getArrayName());
    mrScheduleITHisDTO.setCycle(dto.getCycle() != null ? String.valueOf(dto.getCycle()) : null);
    mrScheduleITHisDTO.setDeviceCode(dto.getDeviceCode());
    mrScheduleITHisDTO
        .setDeviceId(dto.getDeviceId() != null ? String.valueOf(dto.getDeviceId()) : null);
    mrScheduleITHisDTO.setDeviceName(dto.getDeviceName());
    mrScheduleITHisDTO.setDeviceType(dto.getDeviceType());
    mrScheduleITHisDTO.setMarketCode(dto.getMarketCode());
    mrScheduleITHisDTO.setMarketName(dto.getMarketName());
    mrScheduleITHisDTO.setNetworkType(dto.getNetworkType());
    mrScheduleITHisDTO.setMrId(dto.getMrId() != null ? String.valueOf(dto.getMrId()) : null);
    mrScheduleITHisDTO.setMrType(dto.getMrType());
    mrScheduleITHisDTO.setMrDate(
        dto.getNextDateModify() != null ? DateTimeUtils.convertDateToString(dto.getNextDateModify())
            : null);
    mrScheduleITHisDTO.setMrContent(dto.getMrContentId());
    mrScheduleITHisDTO.setMrMode("H");
    mrScheduleITHisDTO.setWoId(dto.getWoId() != null ? String.valueOf(dto.getWoId()) : null);
    mrScheduleITHisDTO
        .setProcedureId(dto.getProcedureId() != null ? String.valueOf(dto.getProcedureId()) : null);
    mrScheduleITHisDTO.setProcedureName(dto.getProcedureName());
    mrScheduleITHisDTO.setRegion(dto.getRegion());
    if (type == 1) {
      mrScheduleITHisDTO.setNote(I18n.getLanguage("mrDevice.node.hard"));
    } else {
      mrScheduleITHisDTO.setNote(I18n.getLanguage("mrDevice.node.status"));
    }
    return mrScheduleITHisDTO;
  }

  public void setMapWoCd() {
    List<WoCdGroupInsideDTO> lstCd = woCategoryServiceProxy
        .getListCdGroupByUser(new WoCdGroupTypeUserDTO());
    if (lstCd != null && !lstCd.isEmpty()) {
      for (WoCdGroupInsideDTO item : lstCd) {
        if (item.getWoGroupId() != null && item.getWoGroupId() != 0L && !mapWoCdGroup
            .containsKey(String.valueOf(item.getWoGroupId()))) {
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
        if (itemDataCRInside.getValueStr() != null && itemDataCRInside.getValueStr() != 0L
            && !mapMarket.containsKey(String.valueOf(itemDataCRInside.getValueStr()))) {
          mapMarket
              .put(String.valueOf(itemDataCRInside.getValueStr()),
                  itemDataCRInside.getDisplayStr());
        }
      }
    }
  }

  public void setMrConfirmHardStr() {
    List<MrConfigDTO> lstMrConfirmHardStr = repository.getConfigByGroup(
        MR_CONFIG_GROUP.LY_DO_KO_BD);
    if (lstMrConfirmHardStr != null && !lstMrConfirmHardStr.isEmpty()) {
      for (MrConfigDTO item : lstMrConfirmHardStr) {
        mapMrConfirmHard
            .put(String.valueOf(item.getConfigCode()), item.getConfigName());
      }
    }
  }

  public void setMapArray() {
    List<CatItemDTO> lstArrayCode = catItemBusiness
        .getListItemByCategoryAndParent(Constants.MR_ITEM_NAME.MR_SUBCATEGORY, null);
    if (lstArrayCode != null && !lstArrayCode.isEmpty()) {
      for (CatItemDTO catItemDTO : lstArrayCode) {
        if (!mapArray.containsKey(catItemDTO.getItemCode())) {
          mapArray
              .put(catItemDTO.getItemCode(), catItemDTO.getItemName());
        }
      }
    }
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
//    XSSFSheet sheetParam4 = workbook.createSheet("param4");
    XSSFSheet sheetParam5 = workbook.createSheet("param5");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrSynItHardDevice.idStr"),
        I18n.getLanguage("mrSynItHardDevice.marketName"),
        I18n.getLanguage("mrSynItHardDevice.regionHard"),
        I18n.getLanguage("mrSynItHardDevice.arrayCodeStr"),
        I18n.getLanguage("mrSynItHardDevice.deviceTypeStr"),
        I18n.getLanguage("mrSynItHardDevice.objectCode"),
        I18n.getLanguage("mrSynItHardDevice.objectName"),
        I18n.getLanguage("mrSynItHardDevice.ipNode"),
        I18n.getLanguage("mrSynItHardDevice.createUserHard"),
        I18n.getLanguage("mrSynItHardDevice.userMrHard"),
        I18n.getLanguage("mrSynItHardDevice.mrHardStr"),
        I18n.getLanguage("mrSynItHardDevice.notes"),
        I18n.getLanguage("mrSynItHardDevice.vendor"),
        I18n.getLanguage("mrSynItHardDevice.stationStr"),
        I18n.getLanguage("mrSynItHardDevice.statusStr"),
        I18n.getLanguage("mrSynItHardDevice.mrConfirmHardStr"),
        I18n.getLanguage("mrSynItHardDevice.cdId"),
        I18n.getLanguage("mrSynItHardDevice.boUnitHardName"),
        I18n.getLanguage("mrSynItHardDevice.approveStatusHardName"),
        I18n.getLanguage("mrSynItHardDevice.levelImportantName"),
        I18n.getLanguage("mrSynItHardDevice.synDate") + "(dd/MM/yyyy)",
        I18n.getLanguage("mrSynItHardDevice.db"),
        I18n.getLanguage("mrSynItHardDevice.ud"),
        I18n.getLanguage("mrSynItHardDevice.upTime")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("mrSynItHardDevice.idStr"),
//        I18n.getLanguage("mrSynItHardDevice.marketName"),
        I18n.getLanguage("mrSynItHardDevice.regionHard"),
//        I18n.getLanguage("mrSynItHardDevice.arrayCodeStr"),
//        I18n.getLanguage("mrSynItHardDevice.deviceTypeStr"),
//        I18n.getLanguage("mrSynItHardDevice.objectCode"),
//        I18n.getLanguage("mrSynItHardDevice.objectName"),
//        I18n.getLanguage("mrSynItHardDevice.createUserHard"),
        I18n.getLanguage("mrSynItHardDevice.mrHardStr"),
//        I18n.getLanguage("mrSynItHardDevice.statusStr"),
        I18n.getLanguage("mrSynItHardDevice.boUnitHardName")

    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

//    int marketNameColumn = listHeader
//        .indexOf(I18n.getLanguage("mrSynItHardDevice.marketName"));
//    int arrayCodeStrColumn = listHeader.indexOf(I18n.getLanguage("mrSynItHardDevice.arrayCodeStr"));
//    int statusStrColumn = listHeader.indexOf(I18n.getLanguage("mrSynItHardDevice.statusStr"));
//    int mrConfirmSoftStrColumn = listHeader
//        .indexOf(I18n.getLanguage("mrSynItHardDevice.mrConfirmHardStr"));
    int mrHardStrColumn = listHeader.indexOf(I18n.getLanguage("mrSynItHardDevice.mrHardStr"));
//    int levelImportantNameColumn = listHeader
//        .indexOf(I18n.getLanguage("mrSynItHardDevice.levelImportantName"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("mrSynItHardDevice.title"));
    titleCell.setCellStyle(style.get("title"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(4);
    Row headerMarket = sheetParam2.createRow(0);
    Row headerArray = sheetParam3.createRow(0);
//    Row headerWoCd = sheetParam4.createRow(0);
    Row headerUnit = sheetParam5.createRow(0);
    headerRow.setHeightInPoints(30);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }
      if (i == 18) {
        Drawing drawing = headerCell.getSheet().createDrawingPatriarch();
        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(i + 1);
        anchor.setCol2(i + 2);
        anchor.setRow1(3);
        anchor.setRow2(4);
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(factory.createRichTextString("Nhập mã đơn vị"));
        headerCell.setCellComment(comment);
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
    sheetParam5.setColumnWidth(0, 2000);
    sheetParam5.setColumnWidth(1, 15000);

    Cell headerCellMarket = headerMarket.createCell(0);
    Cell headerCellRegion = headerMarket.createCell(1);
    XSSFRichTextString market = new XSSFRichTextString(
        I18n.getLanguage("mrSynItHardDevice.marketName"));
    XSSFRichTextString region = new XSSFRichTextString(
        I18n.getLanguage("mrSynItHardDevice.regionSoft"));
    headerCellMarket.setCellValue(market);
    headerCellMarket.setCellStyle(style.get("header"));
    headerCellRegion.setCellValue(region);
    headerCellRegion.setCellStyle(style.get("header"));
    sheetParam2.setColumnWidth(0, 15000);
    sheetParam2.setColumnWidth(1, 15000);

    Cell headerCellArray = headerArray.createCell(0);
    Cell headerCellDevice = headerArray.createCell(1);
    XSSFRichTextString array = new XSSFRichTextString(
        I18n.getLanguage("mrSynItHardDevice.arrayCodeStr"));
    XSSFRichTextString device = new XSSFRichTextString(
        I18n.getLanguage("mrSynItHardDevice.deviceType"));
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
//    for (ItemDataCRInside dto : lstMarketCode) {
//      excelWriterUtils
//          .createCell(sheetParam, 2, row++, dto.getDisplayStr(), style.get("cell"));
//    }
//    sheetParam.autoSizeColumn(1);
//    Name marketName = workbook.createName();
//    marketName.setNameName("marketName");
//    marketName.setRefersToFormula("param!$C$2:$C$" + row);
//    XSSFDataValidationConstraint marketNameConstraint = new XSSFDataValidationConstraint(
//        DataValidationConstraint.ValidationType.LIST, "marketName");
//    CellRangeAddressList marketNameCreate = new CellRangeAddressList(5, 65000,
//        marketNameColumn,
//        marketNameColumn);
//    XSSFDataValidation dataValidationMarketName = (XSSFDataValidation) dataValidationHelper
//        .createValidation(
//            marketNameConstraint, marketNameCreate);
//    dataValidationMarketName.setShowErrorBox(true);
//    sheetOne.addValidationData(dataValidationMarketName);
//
//    row = 5;
//    List<CatItemDTO> lstArrayCode = catItemBusiness
//        .getListItemByCategoryAndParent(Constants.MR_ITEM_NAME.MR_SUBCATEGORY, null);
//    for (CatItemDTO dto : lstArrayCode) {
//      excelWriterUtils
//          .createCell(sheetParam, 4, row++, dto.getItemName(), style.get("cell"));
//    }
//    sheetParam.autoSizeColumn(1);
//    Name arrayCodeStr = workbook.createName();
//    arrayCodeStr.setNameName("arrayCodeStr");
//    arrayCodeStr.setRefersToFormula("param!$E$2:$E$" + row);
//    XSSFDataValidationConstraint arrayCodeStrConstraint = new XSSFDataValidationConstraint(
//        DataValidationConstraint.ValidationType.LIST, "arrayCodeStr");
//    CellRangeAddressList arrayCodeStrCreate = new CellRangeAddressList(5, 65000, arrayCodeStrColumn,
//        arrayCodeStrColumn);
//    XSSFDataValidation dataValidationArrayCodeStr = (XSSFDataValidation) dataValidationHelper
//        .createValidation(
//            arrayCodeStrConstraint, arrayCodeStrCreate);
//    dataValidationArrayCodeStr.setShowErrorBox(true);
//    sheetOne.addValidationData(dataValidationArrayCodeStr);
//
    row = 1;
    for (ItemDataCRInside marketCode : lstMarketCode) {
      List<MrITHardScheduleDTO> lstRegion = mrITHardScheduleRepository
          .getListRegionByMrSynItDevices(String.valueOf(marketCode.getValueStr()));
      excelWriterUtils
          .createCell(sheetParam2, 0, row, marketCode.getDisplayStr(), style.get("cell"));
      for (MrITHardScheduleDTO mrITSoftScheduleDTO : lstRegion) {
        excelWriterUtils
            .createCell(sheetParam2, 1, row++, mrITSoftScheduleDTO.getRegion(), style.get("cell"));
      }
      excelWriterUtils
          .createCell(sheetParam2, 2, row++, null, style.get("cell"));
    }
    sheetParam2.autoSizeColumn(0);
    sheetParam2.autoSizeColumn(1);

//    row = 1;
//    for (CatItemDTO arrayCode : lstArrayCode) {
//      List<MrSynItDevicesDTO> lstDeviceType = mrSynItSoftDevicesRepository
//          .getListDeviceTypeByArrayCode(arrayCode.getItemValue());
//      excelWriterUtils
//          .createCell(sheetParam3, 0, row, arrayCode.getItemName(), style.get("cell"));
//      for (MrSynItDevicesDTO deviceDTO : lstDeviceType) {
//        excelWriterUtils
//            .createCell(sheetParam3, 1, row++, deviceDTO.getDeviceType(), style.get("cell"));
//      }
//      excelWriterUtils
//          .createCell(sheetParam3, 2, row++, null, style.get("cell"));
//    }
//    sheetParam3.autoSizeColumn(0);
//    sheetParam3.autoSizeColumn(1);

    row = 5;
    excelWriterUtils
        .createCell(sheetParam, 11, row++, I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.0")
            , style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam, 11, row++, I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.1")
            , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name mrSoftStr = workbook.createName();
    mrSoftStr.setNameName("mrHardStr");
    mrSoftStr.setRefersToFormula("param!$L$2:$L$" + row);
    XSSFDataValidationConstraint mrSoftStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "mrHardStr");
    CellRangeAddressList mrSoftStrCreate = new CellRangeAddressList(5, 65000, mrHardStrColumn,
        mrHardStrColumn);
    XSSFDataValidation dataValidationMrSoftStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            mrSoftStrConstraint, mrSoftStrCreate);
    dataValidationMrSoftStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationMrSoftStr);

//    row = 5;
//    excelWriterUtils
//        .createCell(sheetParam, 17, row++,
//            I18n.getLanguage("mrSynItHardDevice.importantLevel.normal")
//            , style.get("cell"));
//    excelWriterUtils
//        .createCell(sheetParam, 17, row++,
//            I18n.getLanguage("mrSynItHardDevice.importantLevel.important")
//            , style.get("cell"));
//    excelWriterUtils
//        .createCell(sheetParam, 17, row++,
//            I18n.getLanguage("mrSynItHardDevice.importantLevel.veryImportant")
//            , style.get("cell"));
//    sheetParam.autoSizeColumn(1);
//    Name levelImportantName = workbook.createName();
//    levelImportantName.setNameName("levelImportantName");
//    levelImportantName.setRefersToFormula("param!$R$2:$R$" + row);
//    XSSFDataValidationConstraint levelImportantNameConstraint = new XSSFDataValidationConstraint(
//        DataValidationConstraint.ValidationType.LIST, "levelImportantName");
//    CellRangeAddressList levelImportantNameCreate = new CellRangeAddressList(5, 65000,
//        levelImportantNameColumn,
//        levelImportantNameColumn);
//    XSSFDataValidation dataValidationLevelImportantName = (XSSFDataValidation) dataValidationHelper
//        .createValidation(
//            levelImportantNameConstraint, levelImportantNameCreate);
//    dataValidationLevelImportantName.setShowErrorBox(true);
//    sheetOne.addValidationData(dataValidationLevelImportantName);
//
//    row = 5;
//    List<MrConfigDTO> lstMrConfirmSoftStr = repository.getConfigByGroup("LY_DO_KO_BD");
//    for (MrConfigDTO dto : lstMrConfirmSoftStr) {
//      excelWriterUtils
//          .createCell(sheetParam, 15, row++, dto.getConfigName(), style.get("cell"));
//    }
//    sheetParam.autoSizeColumn(1);
//    Name mrConfirmHardStr = workbook.createName();
//    mrConfirmHardStr.setNameName("mrConfirmSoftStr");
//    mrConfirmHardStr.setRefersToFormula("param!$P$2:$P$" + row);
//    XSSFDataValidationConstraint mrConfirmSoftStrConstraint = new XSSFDataValidationConstraint(
//        DataValidationConstraint.ValidationType.LIST, "mrConfirmSoftStr");
//    CellRangeAddressList mrConfirmSoftStrCreate = new CellRangeAddressList(5, 65000,
//        mrConfirmSoftStrColumn,
//        mrConfirmSoftStrColumn);
//    XSSFDataValidation dataValidationMrConfirmSoftStr = (XSSFDataValidation) dataValidationHelper
//        .createValidation(
//            mrConfirmSoftStrConstraint, mrConfirmSoftStrCreate);
//    dataValidationMrConfirmSoftStr.setShowErrorBox(true);
//    sheetOne.addValidationData(dataValidationMrConfirmSoftStr);
//
//    row = 5;
//    excelWriterUtils
//        .createCell(sheetParam, 14, row++, I18n.getLanguage("mrSynItHardDevice.status.0")
//            , style.get("cell"));
//    excelWriterUtils
//        .createCell(sheetParam, 14, row++, I18n.getLanguage("mrSynItHardDevice.status.1")
//            , style.get("cell"));
//    sheetParam.autoSizeColumn(1);
//    Name statusStr = workbook.createName();
//    statusStr.setNameName("statusStr");
//    statusStr.setRefersToFormula("param!$O$2:$O$" + row);
//    XSSFDataValidationConstraint statusStrConstraint = new XSSFDataValidationConstraint(
//        DataValidationConstraint.ValidationType.LIST, "statusStr");
//    CellRangeAddressList statusStrCreate = new CellRangeAddressList(5, 65000, statusStrColumn,
//        statusStrColumn);
//    XSSFDataValidation dataValidationStatusStr = (XSSFDataValidation) dataValidationHelper
//        .createValidation(
//            statusStrConstraint, statusStrCreate);
//    dataValidationStatusStr.setShowErrorBox(true);
//    sheetOne.addValidationData(dataValidationStatusStr);

    row = 1;
    List<UnitDTO> unitNameList = unitRepository.getListUnit(null);
    for (UnitDTO dto : unitNameList) {
      excelWriterUtils
          .createCell(sheetParam5, 0, row, dto.getUnitId().toString(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam5, 1, row++, dto.getUnitName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);
    sheetParam.autoSizeColumn(1);
    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("mrSynItHardDevice.title"));
    workbook.setSheetName(2, I18n.getLanguage("mrSynItHardDevice.regionSoft"));
    workbook.setSheetName(3, I18n.getLanguage("mrSynItHardDevice.arrDevice"));
//    workbook.setSheetName(4, I18n.getLanguage("mrSynItHardDevice.cdIdName"));
    workbook.setSheetName(4, I18n.getLanguage("mrSynItHardDevice.boUnitHardName"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MR_SYN_IT_HARD_DEVICE" + "_" + System.currentTimeMillis() + ".xlsx";
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
            24,
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
            24,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }
        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
//          setMapMarketName();
//          setMapArray();
//          setMrConfirmHardStr();
          for (Object[] obj : dataImportList) {
            MrSynItDevicesDTO mrSynItDevicesDTO = new MrSynItDevicesDTO();
            if (obj[1] != null) {
              if (!DataUtil.isInteger(obj[1].toString().trim())) {
                if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getResultImport())) {
                  mrSynItDevicesDTO
                      .setResultImport(I18n.getLanguage("mrSynItHardDevice.errType.deviceIdStr"));
                  mrSynItDevicesDTO.setIdStr(obj[1].toString().trim());
                } else {
                  mrSynItDevicesDTO
                      .setResultImport(mrSynItDevicesDTO.getResultImport() + "\n" + I18n
                          .getLanguage("mrSynItHardDevice.errType.deviceIdStr"));
                  mrSynItDevicesDTO.setIdStr(obj[1].toString().trim());
                }
              } else {
                mrSynItDevicesDTO.setIdStr(obj[1].toString().trim());
                mrSynItDevicesDTO.setId(obj[1].toString().trim());
              }
            } else {
              mrSynItDevicesDTO.setIdStr(null);
            }
            if (obj[2] != null) {
              mrSynItDevicesDTO.setMarketName(obj[2].toString().trim());
//              for (Map.Entry<String, String> item : mapMarket.entrySet()) {
//                if (mrSynItDevicesDTO.getMarketName().equals(item.getValue())) {
//                  mrSynItDevicesDTO.setMarketCode(item.getKey());
//                  break;
//                } else {
//                  mrSynItDevicesDTO.setMarketCode(null);
//                }
//              }
            } else {
              mrSynItDevicesDTO.setMarketName(null);
            }
            if (obj[3] != null) {
              mrSynItDevicesDTO.setRegionHard(obj[3].toString().trim());
            } else {
              mrSynItDevicesDTO.setRegionHard(null);
            }
            if (obj[4] != null) {
              mrSynItDevicesDTO.setArrayCodeStr(obj[4].toString().trim());
//              for (Map.Entry<String, String> item : mapArray.entrySet()) {
//                if (mrSynItDevicesDTO.getArrayCodeStr().equals(item.getValue())) {
//                  mrSynItDevicesDTO.setArrayCode(item.getKey());
//                  break;
//                } else {
//                  mrSynItDevicesDTO.setArrayCode(null);
//                }
//              }
            } else {
              mrSynItDevicesDTO.setArrayCodeStr(null);
            }
            if (obj[5] != null) {
              mrSynItDevicesDTO.setDeviceTypeStr(obj[5].toString().trim());
            } else {
              mrSynItDevicesDTO.setDeviceTypeStr(null);
            }
            if (obj[6] != null) {
              mrSynItDevicesDTO.setObjectCode(obj[6].toString().trim().toUpperCase());
            } else {
              mrSynItDevicesDTO.setObjectCode(null);
            }
            if (obj[7] != null) {
              mrSynItDevicesDTO.setObjectName(obj[7].toString().trim());
            } else {
              mrSynItDevicesDTO.setObjectName(null);
            }
            if (obj[8] != null) {
              mrSynItDevicesDTO.setIpNode(obj[8].toString().trim());
            } else {
              mrSynItDevicesDTO.setIpNode(null);
            }
            if (obj[9] != null) {
              mrSynItDevicesDTO.setCreateUserHard(obj[9].toString().trim());
            } else {
              mrSynItDevicesDTO.setCreateUserHard(null);
            }
            if (obj[10] != null) {
              mrSynItDevicesDTO.setUserMrHard(obj[10].toString().trim());
            } else {
              mrSynItDevicesDTO.setUserMrHard(null);
            }
            if (obj[11] != null) {
              mrSynItDevicesDTO.setMrHardStr(obj[11].toString().trim());
              if (I18n.getLanguage("mrSynItHardDevice.mrHardStr.0")
                  .equals(mrSynItDevicesDTO.getMrHardStr())) {
                mrSynItDevicesDTO.setMrHard("0");
              } else if (I18n.getLanguage("mrSynItHardDevice.mrHardStr.1")
                  .equals(mrSynItDevicesDTO.getMrHardStr())) {
                mrSynItDevicesDTO.setMrHard("1");
              } else {
                mrSynItDevicesDTO.setMrHard(null);
              }
            } else {
              mrSynItDevicesDTO.setMrHardStr(null);
            }
            if (obj[12] != null) {
              mrSynItDevicesDTO.setNotes(obj[12].toString().trim());
            } else {
              mrSynItDevicesDTO.setNotes(null);
            }
            if (obj[13] != null) {
              mrSynItDevicesDTO.setVendor(obj[13].toString().trim());
            } else {
              mrSynItDevicesDTO.setVendor(null);
            }
            if (obj[14] != null) {
              mrSynItDevicesDTO.setStationStr(obj[14].toString().trim());
              mrSynItDevicesDTO.setStation(obj[14].toString().trim());
            } else {
              mrSynItDevicesDTO.setStationStr(null);
            }
            if (obj[15] != null) {
              mrSynItDevicesDTO.setStatusStr(obj[15].toString().trim());
//              if (I18n.getLanguage("mrSynItHardDevice.status.1")
//                  .equals(mrSynItDevicesDTO.getStatusStr())) {
//                mrSynItDevicesDTO.setStatus("1");
//              } else if (I18n.getLanguage("mrSynItHardDevice.status.0")
//                  .equals(mrSynItDevicesDTO.getStatusStr())) {
//                mrSynItDevicesDTO.setStatus("0");
//              } else {
//                mrSynItDevicesDTO.setStatus(null);
//              }
            } else {
              mrSynItDevicesDTO.setStatusStr(null);
            }
            if (obj[16] != null) {
              mrSynItDevicesDTO.setMrConfirmHardStr(obj[16].toString().trim());
//              for (Map.Entry<String, String> item : mapMrConfirmHard.entrySet()) {
//                if (mrSynItDevicesDTO.getMrConfirmHardStr().equals(item.getValue())) {
//                  mrSynItDevicesDTO.setMrConfirmHard(item.getKey());
//                  break;
//                } else {
//                  mrSynItDevicesDTO.setMrConfirmHard(null);
//                }
//              }
            } else {
              mrSynItDevicesDTO.setMrConfirmHardStr(null);
            }
            if (obj[17] != null) {
              mrSynItDevicesDTO.setCdIdName(obj[17].toString().trim());
              mrSynItDevicesDTO.setCdId(obj[17].toString().trim());
            } else {
              mrSynItDevicesDTO.setCdId(null);
            }
            if (obj[18] != null) {
              if (!DataUtil.isInteger(obj[18].toString().trim())) {
                if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getResultImport())) {
                  mrSynItDevicesDTO
                      .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.boUnit.exist"));
                  mrSynItDevicesDTO.setBoUnitHardName(obj[18].toString().trim());
                } else {
                  mrSynItDevicesDTO
                      .setResultImport(mrSynItDevicesDTO.getResultImport() + "\n" + I18n
                          .getLanguage("mrSynItSoftDevice.err.boUnit.exist"));
                  mrSynItDevicesDTO.setBoUnitHardName(obj[18].toString().trim());
                }
              } else {
                mrSynItDevicesDTO.setBoUnitHardName(obj[18].toString().trim());
                mrSynItDevicesDTO.setBoUnitHard(obj[18].toString().trim());
              }
            } else {
              mrSynItDevicesDTO.setBoUnitHardName(null);
            }
            if (obj[19] != null) {
              mrSynItDevicesDTO.setApproveStatusHardName(obj[19].toString().trim());
            } else {
              mrSynItDevicesDTO.setApproveStatusHardName(null);
            }
            if (obj[20] != null) {
              mrSynItDevicesDTO.setLevelImportantName(obj[20].toString().trim());
//              if (I18n.getLanguage("mrSynItHardDevice.importantLevel.normal")
//                  .equals(mrSynItDevicesDTO.getLevelImportantName())) {
//                mrSynItDevicesDTO.setLevelImportant("3");
//              } else if (I18n.getLanguage("mrSynItHardDevice.importantLevel.important")
//                  .equals(mrSynItDevicesDTO.getLevelImportantName())) {
//                mrSynItDevicesDTO.setLevelImportant("2");
//              } else if (I18n.getLanguage("mrSynItHardDevice.importantLevel.veryImportant")
//                  .equals(mrSynItDevicesDTO.getLevelImportantName())) {
//                mrSynItDevicesDTO.setLevelImportant("1");
//              } else {
//                mrSynItDevicesDTO.setLevelImportant(null);
//              }
            } else {
              mrSynItDevicesDTO.setLevelImportantName(null);
            }
            if (obj[21] != null) {
              try {
                mrSynItDevicesDTO
                    .setSynDate(DateTimeUtils.convertStringToDate(obj[21].toString().trim()));
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                mrSynItDevicesDTO
                    .setResultImport(I18n.getLanguage("mrSynItHardDevice.errType.synDate"));
              }
            } else {
              mrSynItDevicesDTO.setSynDate(null);
            }
            if (obj[22] != null) {
              mrSynItDevicesDTO.setDb(obj[22].toString().trim());
            } else {
              mrSynItDevicesDTO.setDb(null);
            }
            if (obj[23] != null) {
              mrSynItDevicesDTO.setUd(obj[23].toString().trim());
            } else {
              mrSynItDevicesDTO.setUd(null);
            }
            if (obj[24] != null) {
              if (!DataUtil.isInteger(obj[24].toString().trim())) {
              } else {
                mrSynItDevicesDTO.setUpTime(Long.valueOf(obj[24].toString().trim()));
              }
            } else {
              mrSynItDevicesDTO.setUpTime(null);
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
    if (count != 25) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItHardDevice.idStr") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.marketName")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItHardDevice.regionHard") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.arrayCodeStr")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.deviceTypeStr")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.objectCode")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.objectName")
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.ipNode")
        .equalsIgnoreCase(objects[8].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.createUserHard")
        .equalsIgnoreCase(objects[9].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.userMrHard")
        .equalsIgnoreCase(objects[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItHardDevice.mrHardStr") + "*")
        .equalsIgnoreCase(objects[11].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.notes")
        .equalsIgnoreCase(objects[12].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.vendor")
        .equalsIgnoreCase(objects[13].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.stationStr")
        .equalsIgnoreCase(objects[14].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.statusStr")
        .equalsIgnoreCase(objects[15].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.mrConfirmHardStr")
        .equalsIgnoreCase(objects[16].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.cdId")
        .equalsIgnoreCase(objects[17].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItHardDevice.boUnitHardName") + "*")
        .equalsIgnoreCase(objects[18].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.approveStatusHardName")
        .equalsIgnoreCase(objects[19].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.levelImportantName")
        .equalsIgnoreCase(objects[20].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrSynItHardDevice.synDate") + "(dd/MM/yyyy)")
        .equalsIgnoreCase(objects[21].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.db")
        .equalsIgnoreCase(objects[22].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.ud")
        .equalsIgnoreCase(objects[23].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrSynItHardDevice.upTime")
        .equalsIgnoreCase(objects[24].toString().trim())) {
      return false;
    }

    return true;
  }

  private MrSynItDevicesDTO validateImportInfo(MrSynItDevicesDTO mrSynItDevicesDTO,
      List<MrSynItDevicesDTO> list) {
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getIdStr())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.idStr"));
      return mrSynItDevicesDTO;
    }
//    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMarketName())) {
//      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.marketName"));
//      return mrSynItDevicesDTO;
//    }
//    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMarketCode())) {
//      mrSynItDevicesDTO
//          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.marketName.exist"));
//      return mrSynItDevicesDTO;
//    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getId()) && StringUtils
        .isStringNullOrEmpty(mrSynItDevicesDTO.getRegionHard())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.regionSoft"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getId()) && !StringUtils
        .isStringNullOrEmpty(mrSynItDevicesDTO.getRegionHard())
        && mrSynItDevicesDTO.getRegionHard().length() > 200) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.regionSoft"));
      return mrSynItDevicesDTO;
    }
//    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getArrayCodeStr())) {
//      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.arrayCodeStr"));
//      return mrSynItDevicesDTO;
//    }
//    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getArrayCode())) {
//      mrSynItDevicesDTO
//          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.arrayCode.exist"));
//      return mrSynItDevicesDTO;
//    }
//    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getDeviceTypeStr())) {
//      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.deviceTypeStr"));
//      return mrSynItDevicesDTO;
//    }
//    MrSynItDevicesDTO deviceCheck = mrSynItSoftDevicesRepository
//        .checkDeviceTypeByArrayCode(mrSynItDevicesDTO.getArrayCode(),
//            mrSynItDevicesDTO.getDeviceTypeStr());
//    if (deviceCheck != null) {
//      mrSynItDevicesDTO.setDeviceType(mrSynItDevicesDTO.getDeviceTypeStr());
//    } else {
//      mrSynItDevicesDTO
//          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.deviceTypeStr.exist"));
//      return mrSynItDevicesDTO;
//    }
//    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getIpNode())
//        && mrSynItDevicesDTO.getIpNode().length() > 200) {
//      mrSynItDevicesDTO
//          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.nodeIP"));
//      return mrSynItDevicesDTO;
//    }
//    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getObjectCode())) {
//      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.objectCode"));
//      return mrSynItDevicesDTO;
//    }
//    if (mrSynItDevicesDTO.getObjectCode().length() > 500) {
//      mrSynItDevicesDTO
//          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.objectCode"));
//      return mrSynItDevicesDTO;
//    }
//    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getObjectName())) {
//      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.objectName"));
//      return mrSynItDevicesDTO;
//    }
//    if (mrSynItDevicesDTO.getObjectName().length() > 500) {
//      mrSynItDevicesDTO
//          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.objectName"));
//      return mrSynItDevicesDTO;
//    }
////    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getCreateUserHard())) {
////      mrSynItDevicesDTO
////          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.getCreateUserSoft"));
////      return mrSynItDevicesDTO;
////    }
//    UsersInsideDto usersInsideDto = new UsersInsideDto();
//    usersInsideDto.setUsername(mrSynItDevicesDTO.getUserMrHard());
//    List<UsersInsideDto> userDTOList = userRepository.getListUsersDTOS(usersInsideDto);
//    if (userDTOList == null || userDTOList.isEmpty()) {
//      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItHardDevice.err.username.exist"));
//      return mrSynItDevicesDTO;
//    } else {
//      if (!StringUtils.isStringNullOrEmpty(userDTOList.get(0).getIsEnable()) && "0"
//          .equals(userDTOList.get(0).getIsEnable().toString())) {
//        mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItHardDevice.err.username.exist"));
//        return mrSynItDevicesDTO;
//      }
//    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrHardStr())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItHardDevice.err.mrHardStr"));
      return mrSynItDevicesDTO;
    }
    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrHard())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItHardDevice.err.mrHard.exist"));
      return mrSynItDevicesDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getNotes())
        && mrSynItDevicesDTO.getNotes().length() > 2000) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.notes"));
      return mrSynItDevicesDTO;
    }
//    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getVendor())
//        && mrSynItDevicesDTO.getVendor().length() > 200) {
//      mrSynItDevicesDTO
//          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.vendor"));
//      return mrSynItDevicesDTO;
//    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getStation())
        && mrSynItDevicesDTO.getStation().length() > 500) {
      mrSynItDevicesDTO
          .setResultImport(I18n.getLanguage("mrSynItSoftDevice.errSize.station"));
      return mrSynItDevicesDTO;
    }
//    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getStatusStr())) {
//      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.statusStr"));
//      return mrSynItDevicesDTO;
//    }
//    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getStatus())) {
//      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.status.exist"));
//      return mrSynItDevicesDTO;
//    }
//    if (!(StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrConfirmHardStr())) && StringUtils
//        .isStringNullOrEmpty(mrSynItDevicesDTO.getMrConfirmHard())) {
//      mrSynItDevicesDTO
//          .setResultImport(I18n.getLanguage("mrSynItHardDevice.err.mrConfirmHard.exist"));
//      return mrSynItDevicesDTO;
//    }
//    if ("1".equals(mrSynItDevicesDTO.getMrHard()) && !StringUtils
//        .isStringNullOrEmpty(mrSynItDevicesDTO.getMrConfirmHard())) {
//      mrSynItDevicesDTO
//          .setResultImport(I18n.getLanguage("mrSynItHardDevice.err.mrConfirmHard.required1"));
//      return mrSynItDevicesDTO;
//    }
//    if ("0".equals(mrSynItDevicesDTO.getMrHard()) && StringUtils
//        .isStringNullOrEmpty(mrSynItDevicesDTO.getMrConfirmHard())) {
//      mrSynItDevicesDTO
//          .setResultImport(I18n.getLanguage("mrSynItHardDevice.err.mrConfirmHard.required"));
//      return mrSynItDevicesDTO;
//    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getId()) && StringUtils
        .isStringNullOrEmpty(mrSynItDevicesDTO.getBoUnitHard())) {
      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.boUnitStr"));
      return mrSynItDevicesDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getBoUnitHard())) {
      UnitDTO unit = unitRepository.findUnitById(Long.valueOf(mrSynItDevicesDTO.getBoUnitHard()));
      if (unit != null) {
        if (!StringUtils.isStringNullOrEmpty(unit.getStatus()) && "1"
            .equals(unit.getStatus().toString())) {

        } else {
          mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.boUnit.exist"));
          return mrSynItDevicesDTO;
        }
      } else {
        mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.boUnit.exist"));
        return mrSynItDevicesDTO;
      }
    }
//    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getLevelImportantName()) && StringUtils
//        .isStringNullOrEmpty(mrSynItDevicesDTO.getLevelImportant())) {
//      mrSynItDevicesDTO
//          .setResultImport(I18n.getLanguage("mrSynItHardDevice.err.importantLevel"));
//      return mrSynItDevicesDTO;
//    }
//    MrSynItDevicesDTO mrDevice = mrSynItSoftDevicesRepository
//        .ckeckMrSynItDeviceSoftExist(mrSynItDevicesDTO);
//    if (StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getId()) && mrDevice != null) {
//      mrSynItDevicesDTO.setResultImport(I18n.getLanguage("mrSynItSoftDevice.err.duplicate"));
//      return mrSynItDevicesDTO;
//    } else
    if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getId())) {
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
//    if (list != null && list.size() > 0 && mrSynItDevicesDTO.getResultImport() == null) {
//      mrSynItDevicesDTO = validateDuplicate(list, mrSynItDevicesDTO);
//    }

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
//      if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getId())) {
//        UserToken userToken = ticketProvider.getUserToken();
//        mrDeviceDTO.setUpdateUser(userToken.getUserName());
//        mrDeviceDTO.setCreateUserHard(userToken.getUserName());
//        mrDeviceDTO.setUpdateDate(new Date());
//        resultInSideDto = mrSynItHardDevicesRepository.updateMrSynItDevice(mrDeviceDTO);
//      } else
      if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getId())) {
        MrSynItDevicesDTO mrDeviceOld = mrDeviceDTO.getMrSynItDevicesOld();
//        if (!StringUtils.isStringNullOrEmpty(mrDeviceOld.getApproveStatus()) && !"0"
//            .equals(mrDeviceOld.getApproveStatus())) {
        mrDeviceOld.setRegionHard(mrDeviceDTO.getRegionHard());
//        mrDeviceOld.setUserMrHard(mrDeviceDTO.getUserMrHard());
        mrDeviceOld.setMrHard(mrDeviceDTO.getMrHard());
//        mrDeviceOld.setSynDate(StringUtils.isStringNullOrEmpty(mrDeviceDTO.getSynDate()) ? null
//            : mrDeviceDTO.getSynDate());
//        mrDeviceOld.setLevelImportant(
//            StringUtils.isStringNullOrEmpty(mrDeviceDTO.getLevelImportant()) ? null
//                : mrDeviceDTO.getLevelImportant());
//        mrDeviceOld.setMrConfirmHard(
//            StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrConfirmHard()) ? null
//                : mrDeviceDTO.getMrConfirmHard());
        mrDeviceOld.setStation(
            StringUtils.isStringNullOrEmpty(mrDeviceDTO.getStation()) ? null
                : mrDeviceDTO.getStation());
//        mrDeviceOld.setStatus(mrDeviceDTO.getStatus());
        mrDeviceOld.setBoUnitHard(mrDeviceDTO.getBoUnitHard());
        mrDeviceOld.setNotes(
            StringUtils.isStringNullOrEmpty(mrDeviceDTO.getNotes()) ? null
                : mrDeviceDTO.getNotes());
//        if (!StringUtils.isStringNullOrEmpty(mrDeviceDTO.getMrConfirmHard())) {
//          mrDeviceOld.setApproveStatus("1");
//        }
        resultInSideDto = updateMrSynItHardDevice(mrDeviceOld);
//        }
      }
    }
    return resultInSideDto;
  }
}
