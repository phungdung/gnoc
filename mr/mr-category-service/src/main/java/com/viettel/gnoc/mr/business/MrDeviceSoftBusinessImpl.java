package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.MR_CONFIG_GROUP;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgCrUnitTelDTO;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.repository.MrCfgCrUnitTelRepository;
import com.viettel.gnoc.mr.repository.MrCfgMarketRepository;
import com.viettel.gnoc.mr.repository.MrConfigRepository;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrDeviceSoftRepository;
import com.viettel.gnoc.mr.repository.MrHisRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
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
public class MrDeviceSoftBusinessImpl implements MrDeviceSoftBusiness {

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  MrDeviceSoftRepository mrDeviceSoftRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  MrConfigRepository mrConfigRepository;

  @Autowired
  MrCfgCrUnitTelRepository mrCfgCrUnitTelRepository;

  @Autowired
  MrScheduleTelRepository mrScheduleTelRepository;

  @Autowired
  MrRepository mrRepository;

  @Autowired
  MrScheduleTelHisRepository mrScheduleTelHisRepository;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  MrCfgMarketRepository mrCfgMarketRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UserRepository userRepository;

  @Autowired
  MrHisRepository mrHisRepository;

  @Autowired
  MrDeviceRepository mrDeviceRepository;

  @Autowired
  CatItemRepository catItemRepository;

  Map<Long, UnitDTO> mapUnit = new HashMap<>();
  Map<Long, String> mapWoCdGroup = new HashMap<>();
  Map<Long, String> mapMarket = new HashMap<>();
  Map<String, MrConfigDTO> mapMrConfig = new HashMap<>();
  List<MrCfgCrUnitTelDTO> listMrCfgCrUnitTel = new ArrayList<>();
  Map<String, CatItemDTO> mapArray = new HashMap<>();
  Map<Long, List<String>> mapRegion = new HashMap<>();
  Map<String, List<String>> mapNetwork = new HashMap<>();
  Map<String, List<String>> mapDevice = new HashMap<>();
  Map<String, UsersInsideDto> mapUser = new HashMap<>();

  private int maxRecord = 1000;

  @Override
  public Datatable getListDataMrDeviceSoftSearchWeb(MrDeviceDTO mrDeviceDTO) {
    log.debug("Request to getListDataMrDeviceSoftSearchWeb: {}", mrDeviceDTO);
    UserToken userToken = ticketProvider.getUserToken();
    Datatable datatable = mrDeviceSoftRepository.getListDataMrDeviceSoftSearchWeb(mrDeviceDTO);
    if (datatable != null) {
      List<MrDeviceDTO> list = (List<MrDeviceDTO>) datatable.getData();
      if (list != null && list.size() > 0) {
        setMapUnit();
        setMapWoCdGroup();
        setMapMarket();
        setMapMrConfig();
        setListMrCfgCrUnitTel();
        for (MrDeviceDTO dto : list) {
          //checkApprove
          // dungpv edit
          if (dto.getBoUnitSoft() != null) {
            String unitApprove = userRepository.getUnitParentForApprove("1",
                dto.getBoUnitSoft().toString());
            if (userToken.getDeptId() != null && unitApprove != null
                && userToken.getDeptId().equals(Long.parseLong(unitApprove))
                && dto.getApproveStatusSoft() != null && dto.getApproveStatusSoft().equals(0L)) {
              UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
              if (unitDTO.getIsCommittee().equals(0L)) {
                if (userRepository.checkRoleOfUser("TP", userToken.getUserID())) {
                  dto.setCheckApprove(1L);
                }
              }
            }
          }
          //end
          setDetailValue(dto);
        }
      }
    }
    return datatable;
  }

  @Override
  public MrDeviceDTO findMrDeviceSoftWeb(Long deviceId) {
    log.debug("Request to findMrDeviceSoftWeb: {}", deviceId);
    MrDeviceDTO mrDeviceDTO = mrDeviceSoftRepository.findMrDeviceSoftWeb(deviceId);
    if (mrDeviceDTO != null) {
      setMapUnit();
      setMapWoCdGroup();
      setMapMarket();
      setMapMrConfig();
      setListMrCfgCrUnitTel();
      setDetailValue(mrDeviceDTO);
    }
    return mrDeviceDTO;
  }

  @Override
  public ResultInSideDto updateMrDeviceSoft(MrDeviceDTO mrDeviceDTO) {
    log.debug("Request to updateMrDeviceSoft: {}", mrDeviceDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    MrDeviceDTO mrDeviceOld = mrDeviceSoftRepository.findMrDeviceById(mrDeviceDTO.getDeviceId());
    mrDeviceDTO.setUserIdLogin(userToken.getUserID());
    mrDeviceDTO.setUserNameLogin(userToken.getUserName());
    mrDeviceDTO.setUnitIdLogin(userToken.getDeptId());
    if (mrDeviceDTO.getApproveStatusSoft() != null
        && mrDeviceDTO.getApproveStatusSoft().equals(0L)) {
      resultInSideDto.setKey(RESULT.ERROR_UPDATE);
      resultInSideDto.setMessage(I18n.getLanguage("mrDeviceSoft.approve.update"));
      return resultInSideDto;
    }
    return doUpdate(mrDeviceDTO, mrDeviceOld);
  }

  public ResultInSideDto doUpdate(MrDeviceDTO mrDeviceDTO, MrDeviceDTO mrDeviceOld) {
    ResultInSideDto resultInSideDto;
    // dungpv edit
    if (mrDeviceDTO.getBoUnitSoft() != null) {
      String unitApprove = userRepository.getUnitParentForApprove("1",
          mrDeviceDTO.getBoUnitSoft().toString());
      if (mrDeviceDTO.getUnitIdLogin() != null && unitApprove != null
          && mrDeviceDTO.getUnitIdLogin().equals(Long.parseLong(unitApprove))) {
        UnitDTO unitDTO = unitRepository.findUnitById(mrDeviceDTO.getUnitIdLogin());
        if (unitDTO.getIsCommittee().equals(0L) && mrDeviceDTO.getUserIdLogin() != null) {
          if (userRepository.checkRoleOfUser("TP", mrDeviceDTO.getUserIdLogin())) {
            mrDeviceDTO.setCheckTP(1L);
          }
        }
      }
    }
    //end dungpv

    //cap nhat khu vuc/ma nhom
    boolean isUpdateGroupOrRegion = false;
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getGroupCode())) {
      mrDeviceDTO.setGroupCode("");
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceOld.getGroupCode())) {
      mrDeviceOld.setGroupCode("");
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getRegionSoft())) {
      mrDeviceDTO.setRegionSoft("");
    }
    if (StringUtils.isStringNullOrEmpty(mrDeviceOld.getRegionSoft())) {
      mrDeviceOld.setRegionSoft("");
    }
    if ((StringUtils.isNotNullOrEmpty(mrDeviceDTO.getGroupCode())
        && !mrDeviceDTO.getGroupCode().equals(mrDeviceOld.getGroupCode()))
        || (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getGroupCode())
        && StringUtils.isNotNullOrEmpty(mrDeviceOld.getGroupCode()))
        || (StringUtils.isNotNullOrEmpty(mrDeviceDTO.getRegionSoft())
        && !mrDeviceDTO.getRegionSoft().equals(mrDeviceOld.getRegionSoft()))
        || (StringUtils.isStringNullOrEmpty(mrDeviceDTO.getRegionSoft())
        && StringUtils.isNotNullOrEmpty(mrDeviceOld.getRegionSoft()))) {
      isUpdateGroupOrRegion = true;
    }
    //cap nhat trang thai tu bao duong => ko bao duong hoac nguoc lai
    boolean isUpdateStatusBD = false;
    if (StringUtils.isNotNullOrEmpty(mrDeviceOld.getMrSoft())
        && StringUtils.isNotNullOrEmpty(mrDeviceDTO.getMrSoft())
        && (("1".equals(mrDeviceOld.getMrSoft()) && "0".equals(mrDeviceDTO.getMrSoft()))
        || ("0".equals(mrDeviceOld.getMrSoft()) && "1".equals(mrDeviceDTO.getMrSoft())))) {
      if ("1".equals(mrDeviceDTO.getMrSoft())) {
        if (mrDeviceDTO.getCheckTP() != null && mrDeviceDTO.getCheckTP().equals(1L)) {
          mrDeviceDTO.setMrConfirmSoft(null);
          mrDeviceDTO.setApproveStatusSoft(1L);
          mrDeviceDTO.setApproveReasonSoft("");
          isUpdateStatusBD = true;
        } else {
          mrDeviceDTO.setMrSoft("0");
          mrDeviceDTO.setApproveStatusSoft(0L);
          mrDeviceDTO.setApproveReasonSoft("");
        }
      } else if ("0".equals(mrDeviceDTO.getMrSoft())) {
        if (mrDeviceDTO.getCheckTP() != null && mrDeviceDTO.getCheckTP().equals(1L)) {
          isUpdateStatusBD = true;
          mrDeviceDTO.setApproveStatusSoft(1L);
          mrDeviceDTO.setApproveReasonSoft("");
        } else {
          mrDeviceDTO.setMrSoft("1");
          mrDeviceDTO.setApproveStatusSoft(0L);
          mrDeviceDTO.setApproveReasonSoft("");
        }
      }
    }
    //get lich
    MrDeviceDTO searchDTO = new MrDeviceDTO();
    searchDTO.setDeviceId(mrDeviceDTO.getDeviceId());
    searchDTO.setNodeCode(mrDeviceDTO.getNodeCode());
    searchDTO.setMrSoft("1");
    searchDTO.setMarketCode(mrDeviceDTO.getMarketCode());

    List<MrScheduleTelHisDTO> listMrScheduleTelHisExist = mrScheduleTelRepository
        .getListScheduleMoveToHis(searchDTO);
    List<MrScheduleTelDTO> listMrScheduleTelDelete = new ArrayList<>();
    List<MrScheduleTelHisDTO> listMrScheduleTelHisInsert = new ArrayList<>();
    List<MrScheduleTelHisDTO> lstMr = new ArrayList<>();

    if (isUpdateGroupOrRegion || isUpdateStatusBD) {
      if (listMrScheduleTelHisExist != null && !listMrScheduleTelHisExist.isEmpty()) {
        //move lich sang lich su
        for (int i = listMrScheduleTelHisExist.size() - 1; i > -1; i--) {
          MrScheduleTelHisDTO item = listMrScheduleTelHisExist.get(i);
          if (isUpdateGroupOrRegion && !isUpdateStatusBD) {
            //neu mr va cr != null thi giu nguyen lich
            if (item.getCrId() != null || item.getMrId() != null) {
              listMrScheduleTelHisExist.remove(i);
              continue;
            }
          }
          item.setMrMode("S");
          String note = "";
          if (isUpdateGroupOrRegion) {
            note += I18n.getLanguage("mrScheduleTelhis.note.updateGroupCodeRegion");
          }
          if (isUpdateStatusBD) {
            note += StringUtils.isStringNullOrEmpty(note) ?
                I18n.getLanguage("mrScheduleTelhis.note.updateMrConfirm")
                : "; " + I18n.getLanguage("mrScheduleTelhis.note.updateMrConfirm");
            if (StringUtils.isNotNullOrEmpty(item.getMrId())) {
              lstMr.add(item);
            }
          }
          item.setNote(note);

          MrScheduleTelDTO scheduleDelete = new MrScheduleTelDTO();
          scheduleDelete.setScheduleId(item.getScheduleId());
          listMrScheduleTelDelete.add(scheduleDelete);
        }
        listMrScheduleTelHisInsert.addAll(listMrScheduleTelHisExist);
        resultInSideDto = mrScheduleTelHisRepository
            .insertUpdateListScheduleHis(listMrScheduleTelHisInsert);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          throw new RuntimeException("Error occur while insert data into MR_SCHEDULE_TEL_HIS");
        }
        //xoa lich hien tai
        if (!listMrScheduleTelDelete.isEmpty()) {
          resultInSideDto = mrScheduleTelRepository.deleteListSchedule(listMrScheduleTelDelete);
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            throw new RuntimeException("Error occur while delete data in MR_SCHEDULE_TEL");
          }
        }
        if (isUpdateStatusBD) {
          if (!lstMr.isEmpty()) {
            for (MrScheduleTelHisDTO mr : lstMr) {
              resultInSideDto = mrRepository.deleteMr(Long.valueOf(mr.getMrId()));
              if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                throw new RuntimeException("Error occur while delete data in MR");
              }
              //move MR sang MR HIS
              MrHisDTO mrHis = new MrHisDTO();
              mrHis.setMrId(mr.getMrId());
              mrHis.setUserId(String.valueOf(mrDeviceDTO.getUserIdLogin()));
              mrHis.setUnitId(String.valueOf(mrDeviceDTO.getUnitIdLogin()));
              mrHis.setStatus(mr.getMrState());
              mrHis.setActionCode("3");
              mrHis.setChangeDate(DateTimeUtils.getSysDateTime());
              mrHis.setNotes("AUTO_DELETE_MR_AND_MOVE_MR_TO_HIS");
              mrHis.setComments(I18n.getLanguage("mrScheduleTelhis.updateSchedule.mrHisComment"));
              resultInSideDto = mrHisRepository.insertMrHis(mrHis);
              if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                throw new RuntimeException("Error occur while insert data to MR_HIS");
              }
            }
            //Goi WS CR
          }
        }
      }
      //cap nhat thiet bi
      if (isUpdateGroupOrRegion || isUpdateStatusBD) {
        mrDeviceDTO.setIsCompleteSoft(1L);
      }
    }
    mrDeviceDTO.setUpdateDate(new Date());
    mrDeviceDTO.setUpdateUser(mrDeviceDTO.getUserNameLogin());
//    if (mrDeviceDTO.getApproveStatusSoft() != null
//        && mrDeviceDTO.getApproveStatusSoft().equals(2L)) {
//      mrDeviceDTO.setMrConfirmSoft(null);
//    }
    return mrDeviceSoftRepository.updateMrDevice(mrDeviceDTO);
  }

  @Override
  public ResultInSideDto deleteMrDeviceSoft(MrDeviceDTO mrDeviceDTO) {
    log.debug("Request to deleteMrDeviceSoft: {}", mrDeviceDTO);
    ResultInSideDto resultInSideDto;
    List<MrScheduleTelHisDTO> listScheduleTelHis = new ArrayList<>();
    List<MrScheduleTelDTO> listDeleteSchedule = new ArrayList<>();
    List<MrScheduleTelHisDTO> listScheduleHis = mrScheduleTelRepository
        .getListScheduleMoveToHis(mrDeviceDTO);
    if (listScheduleHis != null && !listScheduleHis.isEmpty()) {
      for (int i = listScheduleHis.size() - 1; i > -1; i--) {
        MrScheduleTelHisDTO itemHis = listScheduleHis.get(i);
        itemHis.setMrMode("S");
        itemHis.setNote(I18n.getLanguage("mrScheduleTelhis.note.deleteSchedule"));

        MrScheduleTelDTO schedule = new MrScheduleTelDTO();
        schedule.setScheduleId(itemHis.getScheduleId());
        listDeleteSchedule.add(schedule);
      }
      listScheduleTelHis.addAll(listScheduleHis);
    }
    //move lich sang lich su
    resultInSideDto = mrScheduleTelHisRepository.insertUpdateListScheduleHis(listScheduleTelHis);
    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      throw new RuntimeException("Error occur while insert data into MR_SCHEDULE_TEL_HIS");
    }
    //xoa lich hien tai
    if (!listDeleteSchedule.isEmpty()) {
      resultInSideDto = mrScheduleTelRepository.deleteListSchedule(listDeleteSchedule);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        throw new RuntimeException("Error occur while delete data in MR_SCHEDULE_TEL");
      }
    }
    //xoa thiet bi
    resultInSideDto = mrDeviceSoftRepository.deleteMrDevice(mrDeviceDTO.getDeviceId());
    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      throw new RuntimeException("Error occur while delete data in MR_DEVICE");
    }
    return resultInSideDto;
  }

  @Override
  public List<MrConfigDTO> getListMaintainStatusCombobox() {
    log.debug("Request to getListMaintainStatusCombobox: {}");
    return mrConfigRepository.getConfigByGroup(MR_CONFIG_GROUP.MR_BD);
  }

  @Override
  public File exportDataMrDeviceSoft(MrDeviceDTO mrDeviceDTO) throws Exception {
    log.debug("Request to exportDataMrDeviceSoft: {}", mrDeviceDTO);
    List<MrDeviceDTO> list = mrDeviceSoftRepository.getListMrDeviceSoftExport(mrDeviceDTO);
    if (list != null && list.size() > 0) {
      setMapUnit();
      setMapWoCdGroup();
      setMapMarket();
      setMapMrConfig();
      setListMrCfgCrUnitTel();
      for (MrDeviceDTO dto : list) {
        setDetailValue(dto);
        if (StringUtils.isNotNullOrEmpty(dto.getMrSoft())) {
          dto.setMrSoft(I18n.getLanguage("mrDeviceSoft.mrSoft." + dto.getMrSoft()));
        }
      }
    }
    String[] header = new String[]{"deviceId", "marketName", "regionSoft", "arrayName",
        "networkType", "deviceType", "nodeIp", "nodeCode", "deviceName", "createUserSoft",
        "comments", "mrSoft", "mrConfirmSoftDisplay", "impactNode", "numberOfCr",
        "boUnitSoftName", "cdId",
        "groupCode", "vendor", "stationCode", "statusName", "dateIntegrated", "mrType"};
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(date),
        "EXPORT_MR_DEVICE_SOFT");
  }

  @Override
  public List<WoCdGroupInsideDTO> getListWoCdGroupCombobox() {
    log.debug("Request to getListWoCdGroupCombobox: {}");
    return woCategoryServiceProxy.getListCdGroupByUserProxy(new WoCdGroupTypeUserDTO());
  }

  @Override
  public List<MrConfigDTO> getListConfirmSoftCombobox() {
    log.debug("Request to getListConfirmSoftCombobox: {}");
    return mrConfigRepository.getConfigByGroup(MR_CONFIG_GROUP.LY_DO_KO_BD);
  }

  @Override
  public List<MrCfgMarketDTO> getListMrCfgMarket() {
    log.debug("Request to getListMrCfgMarket: {}");
    return mrCfgMarketRepository.getListCfgMarket(null);
  }


  @Override
  public ResultInSideDto updateListMrCfgMarket(MrCfgMarketDTO mrCfgMarketDTO) {
    log.debug("Request to updateListMrCfgMarket: {}", mrCfgMarketDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    Date now = new Date();
    List<MrCfgMarketDTO> listMrCfgMarket = mrCfgMarketDTO.getMrCfgMarketDTOList();
    if (listMrCfgMarket != null && listMrCfgMarket.size() > 0) {
      for (MrCfgMarketDTO marketUpdate : listMrCfgMarket) {
        MrCfgMarketDTO marketOld = mrCfgMarketRepository
            .findMrCfgMarketById(marketUpdate.getIdMarket());
        marketOld.setUpdatedTime(now);
        marketOld.setUpdatedUser(userToken.getUserName());
        if (marketUpdate.getCreatedUserSoft() == null && marketOld.getCreatedUserSoft() != null) {
          marketOld.setCreatedUserSoft(null);
          resultInSideDto = mrCfgMarketRepository.add(marketOld);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            resultInSideDto = mrDeviceSoftRepository
                .updateCreateUserSoftByMarket(null, String.valueOf(marketOld.getMarketCode()));
          }
        } else if (marketUpdate.getCreatedUserSoft() != null && !marketUpdate.getCreatedUserSoft()
            .equals(marketOld.getCreatedUserSoft())) {
          marketOld.setCreatedUserSoft(marketUpdate.getCreatedUserSoft());
          resultInSideDto = mrCfgMarketRepository.add(marketOld);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            UsersEntity usersEntity = userRepository
                .getUserByUserId(marketUpdate.getCreatedUserSoft());
            resultInSideDto = mrDeviceSoftRepository
                .updateCreateUserSoftByMarket(usersEntity.getUsername(),
                    String.valueOf(marketOld.getMarketCode()));
          }
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto approveMrDeviceSoft(MrDeviceDTO mrDeviceDTO) {
    log.debug("Request to approveMrDeviceSoft: {}", mrDeviceDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    MrDeviceDTO mrDeviceOld = mrDeviceSoftRepository.findMrDeviceById(mrDeviceDTO.getDeviceId());
    if (mrDeviceOld == null) {
      resultInSideDto.setKey(RESULT.ERROR);
      return resultInSideDto;
    }
    mrDeviceOld.setApproveReasonSoft(mrDeviceDTO.getApproveReasonSoft());
    mrDeviceOld.setApproveStatusSoft(mrDeviceDTO.getApproveStatusSoft().equals(1L) ? 1L : 2L);
    if (mrDeviceDTO.getApproveStatusSoft().equals(1L)) {
      List<MrScheduleTelHisDTO> lstScheduleHis = new ArrayList<>();
      List<MrScheduleTelDTO> lstDeleteSchedule = new ArrayList<>();
      List<MrScheduleTelHisDTO> lstMr = new ArrayList<>();
      MrDeviceDTO searchDTO = new MrDeviceDTO();
      searchDTO.setDeviceId(mrDeviceOld.getDeviceId());
      searchDTO.setNodeCode(mrDeviceOld.getNodeCode());
      searchDTO.setMrSoft("1");
      searchDTO.setMarketCode(mrDeviceOld.getMarketCode());
      List<MrScheduleTelHisDTO> lst = mrScheduleTelRepository.getListScheduleMoveToHis(searchDTO);
      if (lst != null && !lst.isEmpty()) {
        for (int i = lst.size() - 1; i > -1; i--) {
          MrScheduleTelHisDTO itemHis = lst.get(i);
          itemHis.setMrMode("S");
          itemHis.setNote(I18n.getLanguage("mrScheduleTelhis.note.updateMrConfirm"));

          MrScheduleTelDTO schedule = new MrScheduleTelDTO();
          schedule.setScheduleId(itemHis.getScheduleId());
          lstDeleteSchedule.add(schedule);
          if (StringUtils.isNotNullOrEmpty(itemHis.getMrId())) {
            lstMr.add(itemHis);
          }
        }
        lstScheduleHis.addAll(lst);
      }
      //move lich sang lich su
      resultInSideDto = mrScheduleTelHisRepository.insertUpdateListScheduleHis(lstScheduleHis);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        throw new RuntimeException("Error occur while insert data into MR_SCHEDULE_TEL_HIS");
      }
      //xoa lich hien tai
      if (!lstDeleteSchedule.isEmpty()) {
        resultInSideDto = mrScheduleTelRepository.deleteListSchedule(lstDeleteSchedule);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          throw new RuntimeException("Error occur while delete data in MR_SCHEDULE_TEL");
        }
      }
      //xoa MR neu thay doi trang thai tu Co BD -> Ko BD
      if (!lstMr.isEmpty()) {
        for (MrScheduleTelHisDTO mr : lstMr) {
          resultInSideDto = mrRepository.deleteMr(Long.valueOf(mr.getMrId()));
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            throw new RuntimeException("Error occur while delete data in MR");
          }
          //move MR sang MR HIS
          MrHisDTO mrHis = new MrHisDTO();
          mrHis.setMrId(mr.getMrId());
          mrHis.setStatus(mr.getMrState());
          mrHis.setActionCode("3");
          mrHis.setChangeDate(DateTimeUtils.getSysDateTime());
          mrHis.setNotes("AUTO_DELETE_MR_AND_MOVE_MR_TO_HIS");
          mrHis.setComments(I18n.getLanguage("mrScheduleTelhis.updateSchedule.mrHisComment"));
          resultInSideDto = mrHisRepository.insertMrHis(mrHis);
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            throw new RuntimeException("Error occur while insert data to MR_HIS");
          }
        }
        //Goi WS CR
      }
      mrDeviceOld.setIsCompleteSoft(1L);
      mrDeviceOld.setMrSoft("1".equals(mrDeviceDTO.getMrSoft()) ? "0" : "1");
    }
    if ("1".equals(mrDeviceOld.getMrSoft())) {
      mrDeviceOld.setMrConfirmSoft(null);
    }
    return mrDeviceSoftRepository.updateMrDevice(mrDeviceOld);
  }

  @Override
  public ResultInSideDto importDataMrDeviceSoft(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<MrDeviceDTO> listDto;
    List<MrDeviceDTO> listImportDto;
    Map<String, String> mapImportInsert;
    Map<String, String> mapImportUpdate;

    String[] header = new String[]{"deviceIdStr", "marketName", "regionSoft", "arrayName",
        "networkType", "deviceType", "nodeIp", "nodeCode", "deviceName", "createUserName",
        "comments", "mrSoftDisplay", "mrConfirmSoftDisplay", "impactNode", "numberOfCrStr",
        "boUnitSoftId", "cdIdName", "groupCode", "vendor", "stationCode", "statusName",
        "dateIntegratedStr", "mrTypeStr", "resultImport"};

    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }

        File fileImp = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 23, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 6,
            0, 23, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        listImportDto = new ArrayList<>();
        mapImportInsert = new HashMap<>();
        mapImportUpdate = new HashMap<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          setMapAll();
          setMapMrConfig();
          setMapUnit();
          setMapUser();
          for (Object[] obj : lstData) {
            MrDeviceDTO importDTO = new MrDeviceDTO();
            importDTO.setUserIdLogin(userToken.getUserID());
            importDTO.setUserNameLogin(userToken.getUserName());
            importDTO.setUnitIdLogin(userToken.getDeptId());
            if (obj[1] != null) {
              importDTO.setDeviceIdStr(obj[1].toString().trim());
            }
            if (obj[2] != null) {
              importDTO.setMarketName(obj[2].toString().trim());
            }
            if (obj[3] != null) {
              importDTO.setRegionSoft(obj[3].toString().trim());
            }
            if (obj[4] != null) {
              importDTO.setArrayName(obj[4].toString().trim());
            }
            if (obj[5] != null) {
              importDTO.setNetworkType(obj[5].toString().trim());
            }
            if (obj[6] != null) {
              importDTO.setDeviceType(obj[6].toString().trim());
            }
            if (obj[7] != null) {
              importDTO.setNodeIp(obj[7].toString().trim());
            }
            if (obj[8] != null) {
              importDTO.setNodeCode(obj[8].toString().trim());
            }
            if (obj[9] != null) {
              importDTO.setDeviceName(obj[9].toString().trim());
            }
            if (obj[10] != null) {
              importDTO.setCreateUserName(obj[10].toString().trim());
            }
            if (obj[11] != null) {
              importDTO.setComments(obj[11].toString().trim());
            }
            if (obj[12] != null) {
              importDTO.setMrSoftDisplay(obj[12].toString().trim());
            }
            if (obj[13] != null) {
              importDTO.setMrConfirmSoftDisplay(obj[13].toString().trim());
            }
            if (obj[14] != null) {
              importDTO.setImpactNode(obj[14].toString().trim());
            }
            if (obj[15] != null) {
              importDTO.setNumberOfCrStr(obj[15].toString().trim());
            }
            if (obj[16] != null) {
              importDTO.setBoUnitSoftId(obj[16].toString().trim());
            }
            if (obj[17] != null) {
              importDTO.setCdIdName(obj[17].toString().trim());
            }
            if (obj[18] != null) {
              importDTO.setGroupCode(obj[18].toString().trim());
            }
            if (obj[19] != null) {
              importDTO.setVendor(obj[19].toString().trim());
            }
            if (obj[20] != null) {
              importDTO.setStationCode(obj[20].toString().trim());
            }
            if (obj[21] != null) {
              importDTO.setStatusName(obj[21].toString().trim());
            }
            if (obj[22] != null) {
              importDTO.setDateIntegratedStr(obj[22].toString().trim());
            }
            if (obj[23] != null) {
              importDTO.setMrTypeStr(obj[23].toString().trim());
            }
            MrDeviceDTO tempImportDTO = validateImportInfo(importDTO, mapImportInsert,
                mapImportUpdate);
            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.result.import.ok"));
              listImportDto.add(tempImportDTO);
              String marketCode = importDTO.getMarketCode();
              String nodeCode = importDTO.getNodeCode();
              String keyInsert = marketCode + "_" + nodeCode;
              mapImportInsert.put(keyInsert, String.valueOf(value));
              if (tempImportDTO.getDeviceId() != null) {
                String keyUpdate = String.valueOf(tempImportDTO.getDeviceId());
                mapImportUpdate.put(keyUpdate, String.valueOf(value));
              }
              listDto.add(tempImportDTO);
            } else {
              listImportDto.add(tempImportDTO);
              index++;
            }
            value++;
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              for (MrDeviceDTO dto : listDto) {
                resultInSideDto = insertUpdateMrDeviceSoft(dto);
              }
            }
          } else {
            File fileExport = handleFileExport(listImportDto, header, null, "RESULT_IMPORT");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getLanguage("import.common.fail"));
            resultInSideDto.setFile(fileExport);
            resultInSideDto.setFilePath(fileExport.getPath());
            return resultInSideDto;
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFileExport(listImportDto, header, null, "RESULT_IMPORT");
          resultInSideDto.setFile(fileExport);
          resultInSideDto.setFilePath(fileExport.getPath());
          return resultInSideDto;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(ex.getMessage());
    }
    return resultInSideDto;
  }

  private ResultInSideDto insertUpdateMrDeviceSoft(MrDeviceDTO importDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    if (importDTO.getDeviceId() != null) {
      MrDeviceDTO mrDeviceOld = importDTO.getMrDeviceOld();
      MrDeviceDTO mrDeviceDTO = mrDeviceOld.clone();
      if (mrDeviceDTO != null) {
        mrDeviceDTO.setUserIdLogin(importDTO.getUserIdLogin());
        mrDeviceDTO.setUserNameLogin(importDTO.getUserNameLogin());
        mrDeviceDTO.setUnitIdLogin(importDTO.getUnitIdLogin());
        mrDeviceDTO.setRegionSoft(importDTO.getRegionSoft());
        mrDeviceDTO.setCreateUserSoft(importDTO.getCreateUserSoft());
        mrDeviceDTO.setComments(importDTO.getComments());
        mrDeviceDTO.setMrSoft(importDTO.getMrSoft());
        mrDeviceDTO.setMrConfirmSoft(importDTO.getMrConfirmSoft());
//        if (StringUtils.isNotNullOrEmpty(importDTO.getMrConfirmSoft())) {
//          mrDeviceDTO.setMrSoft("0");
//          mrDeviceDTO.setMrConfirmSoft(importDTO.getMrConfirmSoft());
//        } else {
//          mrDeviceDTO.setMrConfirmSoft(null);
//        }
        mrDeviceDTO.setImpactNode(importDTO.getImpactNode());
        mrDeviceDTO.setNumberOfCr(importDTO.getNumberOfCr());
        mrDeviceDTO.setCdId(importDTO.getCdId());
        mrDeviceDTO.setGroupCode(importDTO.getGroupCode());
        mrDeviceDTO.setBoUnitSoft(importDTO.getBoUnitSoft());

        return doUpdate(mrDeviceDTO, mrDeviceOld);
      }
    } else {
      return mrDeviceSoftRepository.insertMrDevice(importDTO);
    }
    return resultInSideDto;
  }

  private MrDeviceDTO validateImportInfo(MrDeviceDTO importDTO, Map<String, String> mapImportInsert,
      Map<String, String> mapImportUpdate) {
    if (StringUtils.isNotNullOrEmpty(importDTO.getDeviceIdStr())) {
      if (!StringUtils.isLong(importDTO.getDeviceIdStr())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.deviceId.valid"));
        return importDTO;
      } else {
        importDTO.setDeviceId(Long.valueOf(importDTO.getDeviceIdStr()));
      }
      MrDeviceDTO dtoUpdate = mrDeviceSoftRepository.findMrDeviceById(importDTO.getDeviceId());
      if (dtoUpdate == null) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.notExist"));
        return importDTO;
      } else {
        if (dtoUpdate.getApproveStatusSoft() != null
            && dtoUpdate.getApproveStatusSoft().equals(0L)) {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.approveStatus"));
          return importDTO;
        }
        importDTO.setMrDeviceOld(dtoUpdate);
      }
      String valiDupUpdate = valiDupUpdate(importDTO, mapImportUpdate);
      if (StringUtils.isNotNullOrEmpty(valiDupUpdate)) {
        importDTO.setResultImport(valiDupUpdate);
        return importDTO;
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getRegionSoft())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.regionSoft"));
        return importDTO;
      } else {
        if (importDTO.getRegionSoft().length() > 200) {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.regionSoft.length"));
          return importDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getCreateUserName())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.createUserSoft"));
        return importDTO;
      } else {
        if (importDTO.getCreateUserName().length() > 200) {
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.createUserSoft.length"));
          return importDTO;
        }
        for (String userName : mapUser.keySet()) {
          if (importDTO.getCreateUserName().equals(mapUser.get(userName).getUsername())) {
            importDTO.setCreateUserSoft(userName);
            break;
          }
        }
        if (StringUtils.isStringNullOrEmpty(importDTO.getCreateUserSoft())) {
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.createUserSoft.valid"));
          return importDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getMrSoftDisplay())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.mrSoft"));
        return importDTO;
      } else {
        if (I18n.getLanguage("mrDeviceSoft.mrSoft.1").equals(importDTO.getMrSoftDisplay())) {
          importDTO.setMrSoft("1");
        } else if (I18n.getLanguage("mrDeviceSoft.mrSoft.0").equals(importDTO.getMrSoftDisplay())) {
          importDTO.setMrSoft("0");
        } else {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.mrSoft.valid"));
          return importDTO;
        }
        if ("0".equals(importDTO.getMrSoft())) {
          if (StringUtils.isStringNullOrEmpty(importDTO.getMrConfirmSoftDisplay())) {
            importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.mrConfirmSoft"));
            return importDTO;
          } else {
            for (String mrConfirm : mapMrConfig.keySet()) {
              if (importDTO.getMrConfirmSoftDisplay()
                  .equals(mapMrConfig.get(mrConfirm).getConfigName())) {
                importDTO.setMrConfirmSoft(mrConfirm);
                break;
              }
            }
            if (StringUtils.isStringNullOrEmpty(importDTO.getMrConfirmSoft())) {
              importDTO
                  .setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.mrConfirmSoft.valid"));
              return importDTO;
            }
          }
        }
        if ("1".equals(importDTO.getMrSoft())) {
          if (StringUtils.isNotNullOrEmpty(importDTO.getMrConfirmSoftDisplay())) {
            importDTO
                .setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.mrConfirmSoft.check"));
            return importDTO;
          }
          if (StringUtils.isStringNullOrEmpty(importDTO.getImpactNode())) {
            importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.impactNode"));
            return importDTO;
          } else {
            if (importDTO.getImpactNode().length() > 2000) {
              importDTO
                  .setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.impactNode.length"));
              return importDTO;
            }
          }
          if (StringUtils.isStringNullOrEmpty(importDTO.getNumberOfCrStr())) {
            importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.numberOfCr"));
            return importDTO;
          } else {
            if (!StringUtils.isLong(importDTO.getNumberOfCrStr())) {
              importDTO
                  .setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.numberOfCr.valid"));
              return importDTO;
            } else {
              importDTO.setNumberOfCr(Long.valueOf(importDTO.getNumberOfCrStr()));
            }
          }
          if (StringUtils.isStringNullOrEmpty(importDTO.getCdIdName())) {
            importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.cdId"));
            return importDTO;
          } else {
            for (Long cdId : mapWoCdGroup.keySet()) {
              if (importDTO.getCdIdName().equals(String.valueOf(cdId))) {
                importDTO.setCdId(cdId);
                break;
              }
            }
            if (importDTO.getCdId() == null) {
              importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.cdId.valid"));
              return importDTO;
            }
          }
        }
      }
//      if (StringUtils.isNotNullOrEmpty(importDTO.getMrConfirmSoftDisplay())) {
//        for (String mrConfirm : mapMrConfig.keySet()) {
//          if (importDTO.getMrConfirmSoftDisplay()
//              .equals(mapMrConfig.get(mrConfirm).getConfigName())) {
//            importDTO.setMrConfirmSoft(mrConfirm);
//            break;
//          }
//        }
//      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getNumberOfCrStr())) {
        if (!StringUtils.isLong(importDTO.getNumberOfCrStr())) {
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.numberOfCr.valid"));
          return importDTO;
        } else {
          importDTO.setNumberOfCr(Long.valueOf(importDTO.getNumberOfCrStr()));
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getBoUnitSoftId())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.boUnit"));
        return importDTO;
      } else {
        for (Long unitId : mapUnit.keySet()) {
          if (importDTO.getBoUnitSoftId()
              .equals(String.valueOf(mapUnit.get(unitId).getUnitId()))) {
            importDTO.setBoUnitSoft(unitId);
            break;
          }
        }
        if (importDTO.getBoUnitSoft() == null) {
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.update.boUnit.valid"));
          return importDTO;
        }
      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getCdIdName())) {
        for (Long cdId : mapWoCdGroup.keySet()) {
          if (importDTO.getCdIdName().equals(String.valueOf(cdId))) {
            importDTO.setCdId(cdId);
            break;
          }
        }
      }
    } else if (StringUtils.isStringNullOrEmpty(importDTO.getDeviceIdStr())) {
      if (StringUtils.isStringNullOrEmpty(importDTO.getMarketName())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.marketName"));
        return importDTO;
      } else {
        for (Long marketId : mapMarket.keySet()) {
          if (importDTO.getMarketName().equals(mapMarket.get(marketId))) {
            importDTO.setMarketCode(String.valueOf(marketId));
            break;
          }
        }
        if (StringUtils.isStringNullOrEmpty(importDTO.getMarketCode())) {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.marketName.valid"));
          return importDTO;
        }
      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getRegionSoft())) {
        if (importDTO.getRegionSoft().length() > 200) {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.regionSoft.length"));
          return importDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getArrayName())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.arrayCode"));
        return importDTO;
      } else {
        for (String array : mapArray.keySet()) {
          if (importDTO.getArrayName().equals(mapArray.get(array).getItemName())) {
            importDTO.setArrayCode(array);
            break;
          }
        }
        if (StringUtils.isStringNullOrEmpty(importDTO.getArrayCode())) {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.array.valid"));
          return importDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getNetworkType())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.networkType"));
        return importDTO;
      } else {
        List<String> listNetwork = mapNetwork.get(importDTO.getArrayCode());
        if (listNetwork.size() > 0) {
          if (!listNetwork.contains(importDTO.getNetworkType())) {
            importDTO
                .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.networkType.valid"));
            return importDTO;
          }
        }
        if (listNetwork.size() == 0) {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.networkType.valid"));
          return importDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getDeviceType())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.deviceType"));
        return importDTO;
      } else {
        List<String> listDevice = mapDevice.get(importDTO.getNetworkType());
        if (listDevice.size() > 0) {
          if (!listDevice.contains(importDTO.getDeviceType())) {
            importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.deviceType.valid"));
            return importDTO;
          }
        }
        if (listDevice.size() == 0) {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.deviceType.valid"));
          return importDTO;
        }
      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getNodeIp())) {
        if (importDTO.getNodeIp().length() > 2000) {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.nodeIp.length"));
          return importDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getNodeCode())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.nodeCode"));
        return importDTO;
      } else {
        if (importDTO.getNodeCode().length() > 200) {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.nodeCode.length"));
          return importDTO;
        }
      }
      MrDeviceDTO dtoInsert = mrDeviceSoftRepository.checkMrDeviceExit(importDTO);
      if (dtoInsert != null) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.dup-code"));
        return importDTO;
      }
      String valiDupInsert = valiDupInsert(importDTO, mapImportInsert);
      if (StringUtils.isNotNullOrEmpty(valiDupInsert)) {
        importDTO.setResultImport(valiDupInsert);
        return importDTO;
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getDeviceName())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.deviceName"));
        return importDTO;
      } else {
        if (importDTO.getDeviceName().length() > 500) {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.deviceName.length"));
          return importDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getCreateUserName())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.createUserSoft"));
        return importDTO;
      } else {
        if (importDTO.getCreateUserName().length() > 200) {
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.createUserSoft.length"));
          return importDTO;
        }
        for (String userName : mapUser.keySet()) {
          if (importDTO.getCreateUserName().equals(mapUser.get(userName).getUsername())) {
            importDTO.setCreateUserSoft(userName);
            break;
          }
        }
        if (StringUtils.isStringNullOrEmpty(importDTO.getCreateUserSoft())) {
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.createUserSoft.valid"));
          return importDTO;
        }
      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getComments())) {
        if (importDTO.getComments().length() > 2000) {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.comments.length"));
          return importDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getMrSoftDisplay())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.mrSoft"));
        return importDTO;
      } else {
        if (I18n.getLanguage("mrDeviceSoft.mrSoft.1").equals(importDTO.getMrSoftDisplay())) {
          importDTO.setMrSoft("1");
        } else if (I18n.getLanguage("mrDeviceSoft.mrSoft.0").equals(importDTO.getMrSoftDisplay())) {
          importDTO.setMrSoft("0");
        } else {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.mrSoft.valid"));
          return importDTO;
        }
        if ("0".equals(importDTO.getMrSoft())) {
          if (StringUtils.isStringNullOrEmpty(importDTO.getMrConfirmSoftDisplay())) {
            importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.mrConfirmSoft"));
            return importDTO;
          } else {
            for (String mrConfirm : mapMrConfig.keySet()) {
              if (importDTO.getMrConfirmSoftDisplay()
                  .equals(mapMrConfig.get(mrConfirm).getConfigName())) {
                importDTO.setMrConfirmSoft(mrConfirm);
                break;
              }
            }
            if (StringUtils.isStringNullOrEmpty(importDTO.getMrConfirmSoft())) {
              importDTO
                  .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.mrConfirmSoft.valid"));
              return importDTO;
            }
          }
          if (StringUtils.isStringNullOrEmpty(importDTO.getBoUnitSoftId())) {
            importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.boUnit"));
            return importDTO;
          } else {
            for (Long unitId : mapUnit.keySet()) {
              if (importDTO.getBoUnitSoftId()
                  .equals(String.valueOf(mapUnit.get(unitId).getUnitId()))) {
                importDTO.setBoUnitSoft(unitId);
                break;
              }
            }
            if (importDTO.getBoUnitSoft() == null) {
              importDTO
                  .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.boUnit.valid"));
              return importDTO;
            }
          }
        }
        if ("1".equals(importDTO.getMrSoft())) {
          if (StringUtils.isStringNullOrEmpty(importDTO.getImpactNode())) {
            importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.impactNode"));
            return importDTO;
          } else {
            if (importDTO.getImpactNode().length() > 2000) {
              importDTO
                  .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.impactNode.length"));
              return importDTO;
            }
          }
          if (StringUtils.isStringNullOrEmpty(importDTO.getNumberOfCrStr())) {
            importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.numberOfCr"));
            return importDTO;
          } else {
            if (!StringUtils.isLong(importDTO.getNumberOfCrStr())) {
              importDTO
                  .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.numberOfCr.valid"));
              return importDTO;
            } else {
              importDTO.setNumberOfCr(Long.valueOf(importDTO.getNumberOfCrStr()));
            }
          }
          if (StringUtils.isStringNullOrEmpty(importDTO.getCdIdName())) {
            importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.cdId"));
            return importDTO;
          } else {
            for (Long cdId : mapWoCdGroup.keySet()) {
              if (importDTO.getCdIdName().equals(String.valueOf(cdId))) {
                importDTO.setCdId(cdId);
                break;
              }
            }
            if (importDTO.getCdId() == null) {
              importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.cdId.valid"));
              return importDTO;
            }
          }
        }
      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getGroupCode())) {
        if (importDTO.getGroupCode().length() > 2000) {
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.groupCode.length"));
          return importDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getVendor())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.vendor"));
        return importDTO;
      } else {
        if (importDTO.getVendor().length() > 1000) {
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.vendor.length"));
          return importDTO;
        }
      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getStationCode())) {
        if (importDTO.getStationCode().length() > 200) {
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.stationCode.length"));
          return importDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getStatusName())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.status"));
        return importDTO;
      } else {
        if (I18n.getLanguage("mrDeviceSoft.status.1").equals(importDTO.getStatusName())) {
          importDTO.setStatus("1");
        } else if (I18n.getLanguage("mrDeviceSoft.status.0").equals(importDTO.getStatusName())) {
          importDTO.setStatus("0");
        } else {
          importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.status.valid"));
          return importDTO;
        }
      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getDateIntegratedStr())) {
        try {
          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
          Date date = sdf.parse(importDTO.getDateIntegratedStr());
          if (!importDTO.getDateIntegratedStr().equals(sdf.format(date)) || StringUtils
              .isNotNullOrEmpty(
                  DataUtil.validateDateTimeDdMmYyyy_HhMmSs(importDTO.getDateIntegratedStr()))) {
            importDTO
                .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.dateIntegrated.valid"));
            return importDTO;
          } else {
            importDTO.setDateIntegrated(date);
          }
        } catch (ParseException e) {
          log.error(e.getMessage(), e);
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.dateIntegrated.valid"));
          return importDTO;
        }
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getMrTypeStr())) {
        importDTO.setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.mrType"));
        return importDTO;
      } else {
        if (!I18n.getLanguage("mrDeviceSoft.mrType.mrSoftDisplay")
            .equals(importDTO.getMrTypeStr())) {
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.mrType.valid"));
          return importDTO;
        }
      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getMrConfirmSoftDisplay())) {
        for (String mrConfirm : mapMrConfig.keySet()) {
          if (importDTO.getMrConfirmSoftDisplay()
              .equals(mapMrConfig.get(mrConfirm).getConfigName())) {
            importDTO.setMrConfirmSoft(mrConfirm);
            break;
          }
        }
      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getNumberOfCrStr())) {
        if (!StringUtils.isLong(importDTO.getNumberOfCrStr())) {
          importDTO
              .setResultImport(I18n.getLanguage("mrDeviceSoft.err.insert.numberOfCr.valid"));
          return importDTO;
        } else {
          importDTO.setNumberOfCr(Long.valueOf(importDTO.getNumberOfCrStr()));
        }
      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getBoUnitSoftId())) {
        for (Long unitId : mapUnit.keySet()) {
          if (importDTO.getBoUnitSoftId().equals(String.valueOf(mapUnit.get(unitId).getUnitId()))) {
            importDTO.setBoUnitSoft(unitId);
            break;
          }
        }
      }
      if (StringUtils.isNotNullOrEmpty(importDTO.getCdIdName())) {
        for (Long cdId : mapWoCdGroup.keySet()) {
          if (importDTO.getCdIdName().equals(String.valueOf(cdId))) {
            importDTO.setCdId(cdId);
            break;
          }
        }
      }
    }
    return importDTO;
  }

  private String valiDupUpdate(MrDeviceDTO importDTO, Map<String, String> mapUpdate) {
    String key = importDTO.getDeviceIdStr();
    String value = mapUpdate.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("mrDeviceSoft.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }

  private String valiDupInsert(MrDeviceDTO importDTO, Map<String, String> mapInsert) {
    String marketCode = importDTO.getMarketCode();
    String nodeCode = importDTO.getNodeCode();
    String key = marketCode + "_" + nodeCode;
    String value = mapInsert.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("mrDeviceSoft.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }

  private String checkResultImport(String resultImport) {
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      resultImport = resultImport.concat("; \n");
    }
    return resultImport;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 24) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.STT")).equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.deviceId") + "\n" +
        I18n.getLanguage("mrDeviceSoft.deviceId.noti"))
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.marketName") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.regionSoft") + "\n" +
        I18n.getLanguage("mrDeviceSoft.deviceId.required"))
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.arrayCode") + " (*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.networkType") + " (*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.deviceType") + " (*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.nodeIp")).equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.nodeCode") + " (*)")
        .equalsIgnoreCase(obj[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.deviceName") + " (*)")
        .equalsIgnoreCase(obj[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.createUserSoft") + " (*)")
        .equalsIgnoreCase(obj[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.comments")).equalsIgnoreCase(obj[11].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.mrSoft") + " (*)")
        .equalsIgnoreCase(obj[12].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.mrConfirmSoftDisplay") + "\n" +
        I18n.getLanguage("mrDeviceSoft.mrSoft.required.no"))
        .equalsIgnoreCase(obj[13].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.impactNode") + "\n" +
        I18n.getLanguage("mrDeviceSoft.mrSoft.required.yes"))
        .equalsIgnoreCase(obj[14].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.numberOfCr") + "\n" +
        I18n.getLanguage("mrDeviceSoft.mrSoft.required.yes"))
        .equalsIgnoreCase(obj[15].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.boUnitSoftId") + "\n" +
        I18n.getLanguage("mrDeviceSoft.deviceId.required"))
        .equalsIgnoreCase(obj[16].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.cdIdName") + "\n" +
        I18n.getLanguage("mrDeviceSoft.mrSoft.required.yes"))
        .equalsIgnoreCase(obj[17].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.groupCode"))
        .equalsIgnoreCase(obj[18].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.vendor") + " (*)")
        .equalsIgnoreCase(obj[19].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.stationCode"))
        .equalsIgnoreCase(obj[20].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.status") + " (*)")
        .equalsIgnoreCase(obj[21].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.dateIntegratedStr"))
        .equalsIgnoreCase(obj[22].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrDeviceSoft.mrTypeStr") + " (*)")
        .equalsIgnoreCase(obj[23].toString().trim())) {
      return false;
    }
    return true;
  }

  @Override
  public File getTemplateImport() throws IOException {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT_NOVALUE.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");
    XSSFSheet sheetNetworkType = workBook
        .createSheet(I18n.getLanguage("mrDeviceSoft.sheetName.networkType"));
    XSSFSheet sheetDeviceType = workBook
        .createSheet(I18n.getLanguage("mrDeviceSoft.sheetName.deviceType"));
    XSSFSheet sheetUnit = workBook.createSheet(I18n.getLanguage("mrDeviceSoft.sheetName.unit"));
    XSSFSheet sheetWoCdGroup = workBook
        .createSheet(I18n.getLanguage("mrDeviceSoft.sheetName.woCdGroup"));

    String[] header = new String[]{
        I18n.getLanguage("mrDeviceSoft.STT"),
        I18n.getLanguage("mrDeviceSoft.deviceId"),
        I18n.getLanguage("mrDeviceSoft.marketName"),
        I18n.getLanguage("mrDeviceSoft.regionSoft"),
        I18n.getLanguage("mrDeviceSoft.arrayCode"),
        I18n.getLanguage("mrDeviceSoft.networkType"),
        I18n.getLanguage("mrDeviceSoft.deviceType"),
        I18n.getLanguage("mrDeviceSoft.nodeIp"),
        I18n.getLanguage("mrDeviceSoft.nodeCode"),
        I18n.getLanguage("mrDeviceSoft.deviceName"),
        I18n.getLanguage("mrDeviceSoft.createUserSoft"),
        I18n.getLanguage("mrDeviceSoft.comments"),
        I18n.getLanguage("mrDeviceSoft.mrSoft"),
        I18n.getLanguage("mrDeviceSoft.mrConfirmSoftDisplay"),
        I18n.getLanguage("mrDeviceSoft.impactNode"),
        I18n.getLanguage("mrDeviceSoft.numberOfCr"),
        I18n.getLanguage("mrDeviceSoft.boUnitSoftId"),
        I18n.getLanguage("mrDeviceSoft.cdIdName"),
        I18n.getLanguage("mrDeviceSoft.groupCode"),
        I18n.getLanguage("mrDeviceSoft.vendor"),
        I18n.getLanguage("mrDeviceSoft.stationCode"),
        I18n.getLanguage("mrDeviceSoft.statusName"),
        I18n.getLanguage("mrDeviceSoft.dateIntegratedStr"),
        I18n.getLanguage("mrDeviceSoft.mrTypeStr")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("mrDeviceSoft.marketName"),
        I18n.getLanguage("mrDeviceSoft.arrayCode"),
        I18n.getLanguage("mrDeviceSoft.networkType"),
        I18n.getLanguage("mrDeviceSoft.deviceType"),
        I18n.getLanguage("mrDeviceSoft.nodeCode"),
        I18n.getLanguage("mrDeviceSoft.deviceName"),
        I18n.getLanguage("mrDeviceSoft.createUserSoft"),
        I18n.getLanguage("mrDeviceSoft.mrSoft"),
        I18n.getLanguage("mrDeviceSoft.vendor"),
        I18n.getLanguage("mrDeviceSoft.statusName"),
        I18n.getLanguage("mrDeviceSoft.mrTypeStr")
    };
    String[] headerNotiDeviceId = new String[]{
        I18n.getLanguage("mrDeviceSoft.deviceId")
    };
    String[] headerReqDeviceId = new String[]{
        I18n.getLanguage("mrDeviceSoft.regionSoft"),
        I18n.getLanguage("mrDeviceSoft.boUnitSoftId")
    };

    String[] headerReqMrSoftNo = new String[]{
        I18n.getLanguage("mrDeviceSoft.mrConfirmSoftDisplay")
    };
    String[] headerReqMrSoftYes = new String[]{
        I18n.getLanguage("mrDeviceSoft.impactNode"),
        I18n.getLanguage("mrDeviceSoft.numberOfCr"),
        I18n.getLanguage("mrDeviceSoft.cdIdName")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    List<String> listNotiDeviceId = Arrays.asList(headerNotiDeviceId);
    List<String> listReqDeviceId = Arrays.asList(headerReqDeviceId);
    List<String> listHeaderReqMrSoftNo = Arrays.asList(headerReqMrSoftNo);
    List<String> listHeaderReqMrSoftYes = Arrays.asList(headerReqMrSoftYes);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.STT"));
    int deviceIdColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.deviceId"));
    int marketColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.marketName"));
    int regionSoftColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.regionSoft"));
    int arrayCodeColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.arrayCode"));
    int networkTypeColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.networkType"));
    int deviceTypeColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.deviceType"));
    int nodeIpColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.nodeIp"));
    int nodeCodeColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.nodeCode"));
    int deviceNameColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.deviceName"));
    int createUserSoftColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.createUserSoft"));
    int commentsColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.comments"));
    int mrSoftColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.mrSoft"));
    int mrConfirmSoftColumn = listHeader
        .indexOf(I18n.getLanguage("mrDeviceSoft.mrConfirmSoftDisplay"));
    int impactNodeColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.impactNode"));
    int numberOfCrColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.numberOfCr"));
    int boUnitColumn = listHeader
        .indexOf(I18n.getLanguage("mrDeviceSoft.boUnitSoftId"));
    int cdIdColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.cdIdName"));
    int groupCodeColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.groupCode"));
    int vendorColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.vendor"));
    int stationCodeColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.stationCode"));
    int statusColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.statusName"));
    int dateIntegratedColumn = listHeader
        .indexOf(I18n.getLanguage("mrDeviceSoft.dateIntegratedStr"));
    int mrTypeColumn = listHeader.indexOf(I18n.getLanguage("mrDeviceSoft.mrTypeStr"));

    String firstLeftHeaderTitle = I18n.getLanguage("mrDeviceSoft.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("mrDeviceSoft.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("mrDeviceSoft.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("mrDeviceSoft.export.secondRightHeader");

    CellStyle cellStyleTopHeader = CommonExport.setCellStyleTopHeader(workBook);
    CellStyle cellStyleTopRightHeader = CommonExport.setCellStyleTopRightHeader(workBook);
    CellStyle cellStyleTitle = CommonExport.setCellStyleTitle(workBook);
    CellStyle cellStyleHeader = CommonExport.setCellStyleHeader(workBook);

    //Tao quoc hieu
    Row headerFirstTitle = sheetMain.createRow(0);
    Row headerSecondTitle = sheetMain.createRow(1);
    int sizeheaderTitle = 7;
    Cell firstLeftHeader = headerFirstTitle.createCell(1);
    firstLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell secondLeftHeader = headerSecondTitle.createCell(1);
    secondLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell firstRightHeader = headerFirstTitle.createCell(sizeheaderTitle - 2);
    firstRightHeader.setCellStyle(cellStyleTopRightHeader);
    Cell secondRightHeader = headerSecondTitle.createCell(sizeheaderTitle - 2);
    secondRightHeader.setCellStyle(cellStyleTopRightHeader);
    firstLeftHeader.setCellValue(firstLeftHeaderTitle);
    secondLeftHeader.setCellValue(secondLeftHeaderTitle);
    firstRightHeader.setCellValue(firstRightHeaderTitle);
    secondRightHeader.setCellValue(secondRightHeaderTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, sizeheaderTitle - 2,
        sizeheaderTitle + 1));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, sizeheaderTitle - 2,
        sizeheaderTitle + 1));

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(3);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("mrDeviceSoft.template.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetMain.createRow(5);
    headerRow.setHeight((short) 1200);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append(" (*)", fontStar);
        }
      }
      for (String checkHeaderNotiDeviceId : listNotiDeviceId) {
        if (checkHeaderNotiDeviceId.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("\n" + I18n.getLanguage("mrDeviceSoft.deviceId.noti"), fontStar);
        }
      }
      for (String checkHeaderReqDeviceId : listReqDeviceId) {
        if (checkHeaderReqDeviceId.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("\n" + I18n.getLanguage("mrDeviceSoft.deviceId.required"), fontStar);
        }
      }
      for (String checkHeaderReqMrSoftNo : listHeaderReqMrSoftNo) {
        if (checkHeaderReqMrSoftNo.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("\n" + I18n.getLanguage("mrDeviceSoft.mrSoft.required.no"), fontStar);
        }
      }
      for (String checkHeaderReqMrSoftYes : listHeaderReqMrSoftYes) {
        if (checkHeaderReqMrSoftYes.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("\n" + I18n.getLanguage("mrDeviceSoft.mrSoft.required.yes"), fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    int row = 1;
    List<ItemDataCRInside> listMarket = catLocationRepository
        .getListLocationByLevelCBB(null, 1L, null);
    if (listMarket != null && listMarket.size() > 0) {
      for (ItemDataCRInside dto : listMarket) {
        ewu.createCell(sheetOrther, 0, row++, dto.getDisplayStr(), styles.get("cell"));
      }
    }
    Name marketName = workBook.createName();
    marketName.setNameName("marketName");
    marketName.setRefersToFormula("Orther!$A$2:$A$" + row);
    XSSFDataValidationConstraint marketConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "marketName");
    CellRangeAddressList cellRangeMarket = new CellRangeAddressList(6, (maxRecord + 6),
        marketColumn, marketColumn);
    XSSFDataValidation dataValidationMarket = (XSSFDataValidation) dvHelper
        .createValidation(marketConstraint, cellRangeMarket);
    dataValidationMarket.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationMarket);

    row = 1;
    Datatable dataArray = catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> listArray = (List<CatItemDTO>) dataArray.getData();
    if (listArray != null && listArray.size() > 0) {
      for (CatItemDTO dto : listArray) {
        ewu.createCell(sheetOrther, 1, row++, dto.getItemName(), styles.get("cell"));
      }
    }
    Name arrayCodeName = workBook.createName();
    arrayCodeName.setNameName("arrayCodeName");
    arrayCodeName.setRefersToFormula("Orther!$B$2:$B$" + row);
    XSSFDataValidationConstraint arrayCodeConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "arrayCodeName");
    CellRangeAddressList cellRangeArrayCode = new CellRangeAddressList(6, (maxRecord + 6),
        arrayCodeColumn, arrayCodeColumn);
    XSSFDataValidation dataValidationArrayCode = (XSSFDataValidation) dvHelper
        .createValidation(arrayCodeConstraint, cellRangeArrayCode);
    dataValidationArrayCode.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationArrayCode);

    row = 1;
    int rowDe = 1;
    ewu.createCell(sheetNetworkType, 0, 0,
        I18n.getLanguage("mrDeviceSoft.arrayCode").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetNetworkType, 1, 0,
        I18n.getLanguage("mrDeviceSoft.networkType").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetDeviceType, 0, 0,
        I18n.getLanguage("mrDeviceSoft.arrayCode").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetDeviceType, 1, 0,
        I18n.getLanguage("mrDeviceSoft.networkType").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetDeviceType, 2, 0,
        I18n.getLanguage("mrDeviceSoft.deviceType").toUpperCase(), styles.get("header"));
    if (listArray != null && listArray.size() > 0) {
      for (CatItemDTO dto : listArray) {
        List<String> listNetwork = mrDeviceRepository.getNetworkTypeByArrayCode(dto.getItemCode());
        if (listNetwork != null && listNetwork.size() > 0) {
          for (String network : listNetwork) {
            List<String> listDevice = mrDeviceRepository
                .getDeviceTypeByNetworkType(dto.getItemCode(), network);
            for (String device : listDevice) {
              ewu.createCell(sheetDeviceType, 0, rowDe, dto.getItemName(), styles.get("cell"));
              ewu.createCell(sheetDeviceType, 1, rowDe, network, styles.get("cell"));
              ewu.createCell(sheetDeviceType, 2, rowDe++, device, styles.get("cell"));
            }
            ewu.createCell(sheetNetworkType, 0, row, dto.getItemName(), styles.get("cell"));
            ewu.createCell(sheetNetworkType, 1, row++, network, styles.get("cell"));
          }
        }
      }
    }
    sheetNetworkType.autoSizeColumn(0);
    sheetNetworkType.autoSizeColumn(1);
    sheetNetworkType.setSelected(false);

    sheetDeviceType.autoSizeColumn(0);
    sheetDeviceType.autoSizeColumn(1);
    sheetDeviceType.autoSizeColumn(2);
    sheetDeviceType.setSelected(false);

    row = 1;
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("mrDeviceSoft.mrSoft.1"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("mrDeviceSoft.mrSoft.0"),
        styles.get("cell"));
    Name mrSoftName = workBook.createName();
    mrSoftName.setNameName("mrSoftName");
    mrSoftName.setRefersToFormula("Orther!$C$2:$C$" + row);
    XSSFDataValidationConstraint mrSoftConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "mrSoftName");
    CellRangeAddressList cellRangeMrSoft = new CellRangeAddressList(6, (maxRecord + 6),
        mrSoftColumn, mrSoftColumn);
    XSSFDataValidation dataValidationMrSoft = (XSSFDataValidation) dvHelper.createValidation(
        mrSoftConstraint, cellRangeMrSoft);
    dataValidationMrSoft.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationMrSoft);

    row = 1;
    List<MrConfigDTO> listConfirmSoft = mrConfigRepository
        .getConfigByGroup(MR_CONFIG_GROUP.LY_DO_KO_BD);
    if (listConfirmSoft != null && listConfirmSoft.size() > 0) {
      for (MrConfigDTO configDTO : listConfirmSoft) {
        ewu.createCell(sheetOrther, 3, row++, configDTO.getConfigName(), styles.get("cell"));
      }
    }
    Name confirmSoftName = workBook.createName();
    confirmSoftName.setNameName("confirmSoftName");
    confirmSoftName.setRefersToFormula("Orther!$D$2:$D$" + row);
    XSSFDataValidationConstraint confirmSoftConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "confirmSoftName");
    CellRangeAddressList cellRangeConfirmSoft = new CellRangeAddressList(6, (maxRecord + 6),
        mrConfirmSoftColumn, mrConfirmSoftColumn);
    XSSFDataValidation dataValidationConfirmSoft = (XSSFDataValidation) dvHelper
        .createValidation(confirmSoftConstraint, cellRangeConfirmSoft);
    dataValidationConfirmSoft.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationConfirmSoft);

    List<UnitDTO> listUnit = unitRepository.getListUnit(null);
    row = 1;
    ewu.createCell(sheetUnit, 0, 0, I18n.getLanguage("mrDeviceSoft.unitId").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetUnit, 1, 0, I18n.getLanguage("mrDeviceSoft.unitCode").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetUnit, 2, 0, I18n.getLanguage("mrDeviceSoft.unitName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetUnit, 3, 0, I18n.getLanguage("mrDeviceSoft.unitParentId").toUpperCase(),
        styles.get("header"));
    for (UnitDTO dto : listUnit) {
      ewu.createCell(sheetUnit, 0, row, String.valueOf(dto.getUnitId()), styles.get("cell"));
      ewu.createCell(sheetUnit, 1, row, dto.getUnitCode(), styles.get("cell"));
      ewu.createCell(sheetUnit, 2, row, dto.getUnitTrueName(), styles.get("cell"));
      ewu.createCell(sheetUnit, 3, row++, String.valueOf(dto.getParentUnitId()),
          styles.get("cell"));
    }
    sheetUnit.setColumnWidth(0, 3000);
    sheetUnit.setColumnWidth(1, 7000);
    sheetUnit.setColumnWidth(2, 10000);
    sheetUnit.setColumnWidth(3, 4000);
    sheetUnit.setSelected(false);

    List<WoCdGroupInsideDTO> listWoCdGroup = woCategoryServiceProxy
        .getListCdGroupByUserProxy(new WoCdGroupTypeUserDTO());
    row = 1;
    ewu.createCell(sheetWoCdGroup, 0, 0, I18n.getLanguage("mrDeviceSoft.woGroupId").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetWoCdGroup, 1, 0, I18n.getLanguage("mrDeviceSoft.woGroupName").toUpperCase(),
        styles.get("header"));
    if (listWoCdGroup != null && !listWoCdGroup.isEmpty()) {
      for (WoCdGroupInsideDTO groupDTO : listWoCdGroup) {
        ewu.createCell(sheetWoCdGroup, 0, row, String.valueOf(groupDTO.getWoGroupId()),
            styles.get("cell"));
        ewu.createCell(sheetWoCdGroup, 1, row++, groupDTO.getWoGroupName(), styles.get("cell"));
      }
    }
    sheetWoCdGroup.setColumnWidth(0, 3000);
    sheetWoCdGroup.setColumnWidth(1, 10000);

    row = 1;
    ewu.createCell(sheetOrther, 4, row++, I18n.getLanguage("mrDeviceSoft.status.1"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 4, row++, I18n.getLanguage("mrDeviceSoft.status.0"),
        styles.get("cell"));
    Name statusName = workBook.createName();
    statusName.setNameName("statusName");
    statusName.setRefersToFormula("Orther!$E$2:$E$" + row);
    XSSFDataValidationConstraint statusConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "statusName");
    CellRangeAddressList cellRangeStatus = new CellRangeAddressList(6, (maxRecord + 6),
        statusColumn, statusColumn);
    XSSFDataValidation dataValidationStatus = (XSSFDataValidation) dvHelper.createValidation(
        statusConstraint, cellRangeStatus);
    dataValidationStatus.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationStatus);

    row = 1;
    ewu.createCell(sheetOrther, 5, row++, I18n.getLanguage("mrDeviceSoft.mrType.mrSoftDisplay"),
        styles.get("cell"));
    Name mrTypeName = workBook.createName();
    mrTypeName.setNameName("mrTypeName");
    mrTypeName.setRefersToFormula("Orther!$F$2:$F$" + row);
    XSSFDataValidationConstraint mrTypeConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "mrTypeName");
    CellRangeAddressList cellRangeMrType = new CellRangeAddressList(6, (maxRecord + 6),
        mrTypeColumn, mrTypeColumn);
    XSSFDataValidation dataValidationMrType = (XSSFDataValidation) dvHelper.createValidation(
        mrTypeConstraint, cellRangeMrType);
    dataValidationMrType.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationMrType);

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(deviceIdColumn, 6000);
    sheetMain.setColumnWidth(marketColumn, 6000);
    sheetMain.setColumnWidth(regionSoftColumn, 6000);
    sheetMain.setColumnWidth(arrayCodeColumn, 10000);
    sheetMain.setColumnWidth(networkTypeColumn, 7000);
    sheetMain.setColumnWidth(deviceTypeColumn, 6000);
    sheetMain.setColumnWidth(nodeIpColumn, 6000);
    sheetMain.setColumnWidth(nodeCodeColumn, 6000);
    sheetMain.setColumnWidth(deviceNameColumn, 6000);
    sheetMain.setColumnWidth(createUserSoftColumn, 5000);
    sheetMain.setColumnWidth(commentsColumn, 6000);
    sheetMain.setColumnWidth(mrSoftColumn, 4000);
    sheetMain.setColumnWidth(mrConfirmSoftColumn, 10000);
    sheetMain.setColumnWidth(impactNodeColumn, 5000);
    sheetMain.setColumnWidth(numberOfCrColumn, 5000);
    sheetMain.setColumnWidth(boUnitColumn, 5000);
    sheetMain.setColumnWidth(cdIdColumn, 5000);
    sheetMain.setColumnWidth(groupCodeColumn, 7000);
    sheetMain.setColumnWidth(vendorColumn, 7000);
    sheetMain.setColumnWidth(stationCodeColumn, 7000);
    sheetMain.setColumnWidth(statusColumn, 4000);
    sheetMain.setColumnWidth(dateIntegratedColumn, 6000);
    sheetMain.setColumnWidth(mrTypeColumn, 4000);

    sheetOrther.setSelected(false);
    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("mrDeviceSoft.import.sheetName"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MR_DEVICE_SOFT_TEMPLATE" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public List<String> getListRegionCombobox(String marketCode) {
    log.debug("Request to getListRegionCombobox: {}", marketCode);
    return mrDeviceRepository.getListRegionSoftByMarketCode(marketCode);
  }

  @Override
  public List<String> getListNetworkTypeCombobox(String arrayCode) {
    log.debug("Request to getListNetworkTypeCombobox: {}", arrayCode);
    return mrDeviceRepository.getNetworkTypeByArrayCode(arrayCode);
  }

  @Override
  public List<String> getListDeviceTypeCombobox(MrDeviceDTO mrDeviceDTO) {
    log.debug("Request to getListDeviceTypeCombobox: {}", mrDeviceDTO);
    return mrDeviceRepository
        .getDeviceTypeByNetworkType(mrDeviceDTO.getArrayCode(), mrDeviceDTO.getNetworkType());
  }

  @Override
  public List<UnitDTO> getListUnitCombobox() {
    log.debug("Request to getListUnitCombobox: {}");
    return unitRepository.getListUnit(null);
  }

  public File handleFileExport(List<MrDeviceDTO> list, String[] columnExport, String date,
      String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = "";
    String title = "";
    String fileNameOut = "";
    String headerPrefix = "language.mrDeviceSoft";
    String firstLeftHeader = I18n.getLanguage("mrDeviceSoft.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("mrDeviceSoft.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("mrDeviceSoft.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("mrDeviceSoft.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle;
    if (StringUtils.isNotNullOrEmpty(date)) {
      subTitle = I18n.getLanguage("mrDeviceSoft.export.exportDate", date);
    } else {
      subTitle = "";
    }
    switch (code) {
      case "RESULT_IMPORT":
        sheetName = I18n.getLanguage("mrDeviceSoft.import.sheetname");
        title = I18n.getLanguage("mrDeviceSoft.import.title");
        fileNameOut = I18n.getLanguage("mrDeviceSoft.import.fileNameOut");
        break;
      case "EXPORT_MR_DEVICE_SOFT":
        sheetName = I18n.getLanguage("mrDeviceSoft.export.sheetname");
        title = I18n.getLanguage("mrDeviceSoft.export.title");
        fileNameOut = I18n.getLanguage("mrDeviceSoft.export.fileNameOut");
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
        I18n.getLanguage("mrDeviceSoft.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT_NOVALUE.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  public List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false,
          0, 0, new String[]{}, null, "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
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
    Long boUnit = mrDeviceDTO.getBoUnitSoft();
    if (boUnit != null && mapUnit.containsKey(boUnit)) {
      mrDeviceDTO.setBoUnitSoftName(mapUnit.get(boUnit).getUnitName());
    }
    Long approveStatus = mrDeviceDTO.getApproveStatusSoft();
    if (approveStatus != null) {
      if (approveStatus.equals(0L)) {
        mrDeviceDTO.setApproveStatusSoftName(I18n.getLanguage("mrDeviceSoft.approveStatus.0"));
      } else if (approveStatus.equals(1L)) {
        mrDeviceDTO.setApproveStatusSoftName(I18n.getLanguage("mrDeviceSoft.approveStatus.1"));
      } else if (approveStatus.equals(2L)) {
        mrDeviceDTO.setApproveStatusSoftName(I18n.getLanguage("mrDeviceSoft.approveStatus.2"));
      }
    }
    Long cdId = mrDeviceDTO.getCdId();
    if (cdId != null && mapWoCdGroup.containsKey(cdId)) {
      mrDeviceDTO.setCdIdName(mapWoCdGroup.get(cdId));
    }
    Long cdIdHard = mrDeviceDTO.getCdIdHard();
    if (cdIdHard != null && mapWoCdGroup.containsKey(cdIdHard)) {
      mrDeviceDTO.setCdIdHardName(mapWoCdGroup.get(cdIdHard));
    }
    String status = mrDeviceDTO.getStatus();
    if (StringUtils.isNotNullOrEmpty(status)) {
      if ("0".equals(status)) {
        mrDeviceDTO.setStatusName(I18n.getLanguage("mrDeviceSoft.status.inActive"));
      } else if ("1".equals(status)) {
        mrDeviceDTO.setStatusName(I18n.getLanguage("mrDeviceSoft.status.active"));
      }
    }
    String dateIntegratedStr = mrDeviceDTO.getDateIntegratedStr();
    if (dateIntegratedStr != null && dateIntegratedStr.length() > 10) {
      mrDeviceDTO.setDateIntegratedStr(dateIntegratedStr.substring(0, 10));
    }
    String marketCode = mrDeviceDTO.getMarketCode();
    if (StringUtils.isNotNullOrEmpty(marketCode)) {
      mrDeviceDTO.setMarketName(mapMarket.get(Long.valueOf(marketCode)));
    }
    mrDeviceDTO.setMrType(I18n.getLanguage("mrDeviceSoft.mrType.mrSoftDisplay"));
    String mrConfirmSoft = mrDeviceDTO.getMrConfirmSoft();
    if (StringUtils.isNotNullOrEmpty(mrConfirmSoft)) {
      mrDeviceDTO.setMrSoftDisplay(I18n.getLanguage("mrDeviceSoft.mrSoftDisplay.no"));
    } else {
      mrDeviceDTO.setMrSoftDisplay(I18n.getLanguage("mrDeviceSoft.mrSoftDisplay.yes"));
    }
    String mrSoft = mrDeviceDTO.getMrSoft();
    if (StringUtils.isNotNullOrEmpty(mrSoft)) {
      if ("1".equals(mrSoft)) {
        mrDeviceDTO.setMrSoftDisplay(I18n.getLanguage("mrDeviceSoft.mrSoftDisplay.yes"));
      } else if ("0".equals(mrSoft)) {
        mrDeviceDTO.setMrSoftDisplay(I18n.getLanguage("mrDeviceSoft.mrSoftDisplay.no"));
      }
    }
    if (StringUtils.isNotNullOrEmpty(mrConfirmSoft) && mapMrConfig.containsKey(mrConfirmSoft)) {
      mrDeviceDTO.setMrConfirmSoftDisplay(mapMrConfig.get(mrConfirmSoft).getConfigName());
    }
    for (MrCfgCrUnitTelDTO mrCfg : listMrCfgCrUnitTel) {
      if (mrCfg.getMarketCode().equals(mrDeviceDTO.getMarketCode())
          && mrCfg.getRegion().equals(mrDeviceDTO.getRegionSoft())
          && mrCfg.getArrayCode().equals(mrDeviceDTO.getArrayCode())
          && mrCfg.getDeviceType().equals(mrDeviceDTO.getDeviceType())) {
        mrDeviceDTO.setImplementUnit(
            mrCfg.getImplementUnit() != null ? String.valueOf(mrCfg.getImplementUnit()) : null);
        mrDeviceDTO.setCheckingUnit(
            mrCfg.getCheckingUnit() != null ? String.valueOf(mrCfg.getCheckingUnit()) : null);
        break;
      }
    }
  }

  public void setMapUnit() {
    List<UnitDTO> listUnit = unitRepository.getListUnit(new UnitDTO());
    if (listUnit != null && listUnit.size() > 0) {
      for (UnitDTO dto : listUnit) {
        mapUnit.put(dto.getUnitId(), dto);
      }
    }
  }

  public void setMapWoCdGroup() {
    List<WoCdGroupInsideDTO> listWoCdGroup = woCategoryServiceProxy
        .getListCdGroupByUserProxy(new WoCdGroupTypeUserDTO());
    if (listWoCdGroup != null && listWoCdGroup.size() > 0) {
      for (WoCdGroupInsideDTO dto : listWoCdGroup) {
        mapWoCdGroup.put(dto.getWoGroupId(), dto.getWoGroupName());
      }
    }
  }

  public void setMapMarket() {
    List<ItemDataCRInside> listMarket = catLocationRepository
        .getListLocationByLevelCBB(null, 1L, null);
    if (listMarket != null && !listMarket.isEmpty()) {
      for (ItemDataCRInside dto : listMarket) {
        mapMarket.put(dto.getValueStr(), dto.getDisplayStr());
      }
    }
  }

  public void setMapMrConfig() {
    List<MrConfigDTO> listMrConfig = mrConfigRepository
        .getConfigByGroup(MR_CONFIG_GROUP.LY_DO_KO_BD);
    if (listMrConfig != null && !listMrConfig.isEmpty()) {
      for (MrConfigDTO configDTO : listMrConfig) {
        mapMrConfig.put(configDTO.getConfigCode(), configDTO);
      }
    }
  }

  public void setListMrCfgCrUnitTel() {
    listMrCfgCrUnitTel = mrCfgCrUnitTelRepository.getListMrCfgCrUnitTel(new MrCfgCrUnitTelDTO());
  }

  public void setMapAll() {
    List<ItemDataCRInside> listMarket = catLocationRepository
        .getListLocationByLevelCBB(null, 1L, null);
    if (listMarket != null && !listMarket.isEmpty()) {
      for (ItemDataCRInside dto : listMarket) {
        mapMarket.put(dto.getValueStr(), dto.getDisplayStr());
        List<String> listRegion = mrDeviceRepository
            .getListRegionSoftByMarketCode(String.valueOf(dto.getValueStr()));
        if (listRegion != null && listRegion.size() > 0) {
          mapRegion.put(dto.getValueStr(), listRegion);
        } else {
          mapRegion.put(dto.getValueStr(), new ArrayList<>());
        }
      }
    }
    Datatable dataArray = catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> listArray = (List<CatItemDTO>) dataArray.getData();
    if (listArray != null && listArray.size() > 0) {
      for (CatItemDTO dto : listArray) {
        mapArray.put(dto.getItemName(), dto);
        List<String> listNetwork = mrDeviceRepository.getNetworkTypeByArrayCode(dto.getItemName());
        if (listNetwork != null && listNetwork.size() > 0) {
          mapNetwork.put(dto.getItemName(), listNetwork);
          for (String network : listNetwork) {
            List<String> listDevice = mrDeviceRepository
                .getDeviceTypeByNetworkType(dto.getItemName(), network);
            if (listDevice != null && listDevice.size() > 0) {
              mapDevice.put(network, listDevice);
            } else {
              mapDevice.put(network, new ArrayList<>());
            }
          }
        } else {
          mapNetwork.put(dto.getItemName(), new ArrayList<>());
        }
      }
    }
  }

  public void setMapUser() {
    List<UsersInsideDto> listUser = userRepository.getListUsersDTOS(new UsersInsideDto());
    if (listUser != null && listUser.size() > 0) {
      for (UsersInsideDto userDTO : listUser) {
        mapUser.put(userDTO.getUsername(), userDTO);
      }
    }
  }
}
