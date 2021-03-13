package com.viettel.gnoc.business;

import com.orlab.OrlabViettelCrMonthSolver;
import com.orlab.day.OrlabViettelCrDaySolver;
import com.orlab.input.Config;
import com.orlab.input.Constraint;
import com.orlab.input.Cr;
import com.orlab.input.Employee;
import com.orlab.input.Input;
import com.orlab.output.Output;
import com.orlab.output.ServedCr;
import com.viettel.gnoc.commons.business.LogChangeConfigBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.ScheduleCRFormDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.RequestScheduleDTO;
import com.viettel.gnoc.cr.dto.ScheduleCRDTO;
import com.viettel.gnoc.cr.dto.ScheduleEmployeeDTO;
import com.viettel.gnoc.repository.CfgRequestScheduleRepository;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Slf4j
@Service
@Transactional
public class CfgRequestScheduleBusinessImpl implements CfgRequestScheduleBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  CfgRequestScheduleRepository cfgRequestScheduleRepository;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  private LogChangeConfigBusiness logChangeConfigBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UserBusiness userBusiness;

  @Override
  public ResultInSideDto insertRequestSchedule(RequestScheduleDTO requestScheduleDTO) {
    log.debug("Request to insertRequestSchedule : {}", requestScheduleDTO);
    ResultInSideDto resultInSideDto = cfgRequestScheduleRepository
        .insertRequestSchedule(requestScheduleDTO);
    UserToken userToken = ticketProvider.getUserToken();
    logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "InsertRequestSchedule", "Add InsertRequestSchedule ID: " + resultInSideDto.getId(),
        requestScheduleDTO, null));
    return resultInSideDto;
  }

  @Override
  public String deleteRequestSchedule(Long id) {
    log.debug("Request to deleteRequestSchedule : {}", id);
    return cfgRequestScheduleRepository.deleteRequestSchedule(id);
  }

  @Override
  public Datatable getListYear(CatItemDTO catItemDTO) {
    log.debug("Request to getListYear : {}", catItemDTO);
    return cfgRequestScheduleRepository.getListYear(catItemDTO);
  }

  @Override
  public Datatable getListRequestSchedule(RequestScheduleDTO requestScheduleDTO) {
    log.debug("Request to getListRequestSchedule : {}", requestScheduleDTO);
    return cfgRequestScheduleRepository.getListRequestSchedule(requestScheduleDTO);
  }

  @Override
  public String updateRequestSchedule(RequestScheduleDTO dto) {
    log.debug("Request to updateRequestSchedule : {}", dto);
    String result = cfgRequestScheduleRepository.updateRequestSchedule(dto);
    UserToken userToken = ticketProvider.getUserToken();
    logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "UpdateRequestSchedule", "UpdateRequestSchedule ID: " + dto.getIdSchedule(),
        dto, null));
    return result;
  }

  @Override
  public RequestScheduleDTO findRequestScheduleById(Long id) {
    log.debug("Request to findRequestScheduleById : {}", id);
    return cfgRequestScheduleRepository.findRequestScheduleById(id);
  }

  @Override
  public Datatable getListUnit(UnitDTO unitDTO) {
    log.debug("Request to getListUnit : {}", unitDTO);
    return cfgRequestScheduleRepository.getListUnit(unitDTO);
  }

  @Override
  public List<ScheduleEmployeeDTO> getListEmployee(ScheduleEmployeeDTO scheduleEmployeeDTO) {
    log.debug("Request to getListEmployee : {}", scheduleEmployeeDTO);
    List<ItemDataCRInside> lstCbbArrayTemp = crServiceProxy.getListImpactSegmentCBB();
    CfgChildArrayDTO dto = new CfgChildArrayDTO();
    List<CfgChildArrayDTO> lstArrayChildTemp = crServiceProxy.getCbbChildArray(dto);
    Map<String, ItemDataCRInside> mapArray = new HashMap<>();
    Map<String, CfgChildArrayDTO> mapArrayChild = new HashMap<>();

    if (lstCbbArrayTemp != null && !lstCbbArrayTemp.isEmpty()) {
      out:
      for (ItemDataCRInside itemDTO : lstCbbArrayTemp) {
        if (itemDTO.getDisplayStr().equals(itemDTO.getDisplayStr())) {
          mapArray.put(itemDTO.getValueStr() == null ? null : String.valueOf(itemDTO.getValueStr()),
              itemDTO);
          continue out;
        }
      }
    }

    if (lstArrayChildTemp != null && !lstArrayChildTemp.isEmpty()) {
      out:
      for (CfgChildArrayDTO itemDTO : lstArrayChildTemp) {
        if (itemDTO.getChildrenId().equals(itemDTO.getChildrenId())) {
          mapArrayChild
              .put(itemDTO.getChildrenId() == null ? null : String.valueOf(itemDTO.getChildrenId()),
                  itemDTO);
          continue out;
        }
      }
    }

    List<ScheduleEmployeeDTO> datatable = cfgRequestScheduleRepository
        .getListEmployee(scheduleEmployeeDTO);
    List<ScheduleEmployeeDTO> lst = new ArrayList<>();
    if (datatable != null) {
      lst = datatable;
    }
    Date date;
    if ("0".equals(scheduleEmployeeDTO.getType())) {
      SimpleDateFormat sp = new SimpleDateFormat("yyyy");
      try {
        date = DateUtil.string2Date(scheduleEmployeeDTO.getStartDate());
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      String year = sp.format(date);

      SimpleDateFormat sp2 = new SimpleDateFormat("dd");
      try {
        date = DateUtil.string2Date(scheduleEmployeeDTO.getStartDate());
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      String startDate2 = sp2.format(date);
      int startDate = Integer.valueOf(startDate2);

      SimpleDateFormat sp3 = new SimpleDateFormat("dd");
      try {
        date = DateUtil.string2Date(scheduleEmployeeDTO.getEndDate());
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      String endDate2 = sp3.format(date);
      int endDate = Integer.valueOf(endDate2);

      SimpleDateFormat sp4 = new SimpleDateFormat("MM");
      try {
        date = DateUtil.string2Date(scheduleEmployeeDTO.getStartDate());
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      String month = sp4.format(date);
      List<String> lstDateOfMonth = new ArrayList<>();

      if ("0".equals(scheduleEmployeeDTO.getType())) {
        for (int i = startDate; i <= endDate; i++) {
          String ngay = ((i < 10) ? ("0" + i) : i) + "/" + month + "/" + year;
          lstDateOfMonth.add(ngay);
        }
      }
      List<String> lstData = new ArrayList<>();
      if (lst != null && !lst.isEmpty()) {
        for (ScheduleEmployeeDTO item : lst) {
          ItemDataCRInside dtoU = mapArray.get(item.getEmpArray());
          if (item.getEmpArray() != null) {
            item.setParentName(dtoU.getDisplayStr());
          }
          CfgChildArrayDTO dtoChild = mapArrayChild.get(item.getEmpChildren());
          if (item.getEmpChildren() != null) {
            item.setArrayChildName(dtoChild.getChildrenName());
          }
          for (String day : lstDateOfMonth) {
            if (item.getDayOff() != null && !item.getDayOff().isEmpty()) {
              if (item.getDayOff().contains(day)) {
                continue;
              }
            }
            lstData.add(day);
          }
          String workingDay = lstData.toString();
          workingDay = workingDay.substring(1, workingDay.toString().length() - 1);
          item.setWorkingDay(workingDay);
          lstData.clear();
        }

      }
    } else if ("1".equals(scheduleEmployeeDTO.getType())) {
      SimpleDateFormat sy = new SimpleDateFormat("yyyy");
      try {
        date = DateUtil.string2Date(scheduleEmployeeDTO.getStartDate());
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      String year = sy.format(date);

      SimpleDateFormat sp2 = new SimpleDateFormat("dd");
      try {
        date = DateUtil.string2Date(scheduleEmployeeDTO.getStartDate());
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      String startDate2 = sp2.format(date);
      int startDate = Integer.valueOf(startDate2);

      SimpleDateFormat sp3 = new SimpleDateFormat("dd");
      try {
        date = DateUtil.string2Date(scheduleEmployeeDTO.getEndDate());
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      String endDate2 = sp3.format(date);
      int endDate = Integer.valueOf(endDate2);

      SimpleDateFormat sp4 = new SimpleDateFormat("MM");
      try {
        date = DateUtil.string2Date(scheduleEmployeeDTO.getStartDate());
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      String month = sp4.format(date);
      List<String> lstDateOfWeek = new ArrayList<>();
      for (int i = startDate; i <= endDate; i++) {
        String ngay = ((i < 10) ? ("0" + i) : i) + "/" + month + "/" + year;
        lstDateOfWeek.add(ngay);
      }
      List<String> lstData = new ArrayList<>();
      if (lst != null && !lst.isEmpty()) {
        for (ScheduleEmployeeDTO item : lst) {
          ItemDataCRInside dtoU = mapArray.get(item.getEmpArray());
          if (item.getEmpArray() != null) {
            item.setParentName(dtoU.getDisplayStr());
          }
          CfgChildArrayDTO dtoChild = mapArrayChild.get(item.getEmpChildren());
          if (item.getEmpChildren() != null) {
            item.setArrayChildName(dtoChild.getChildrenName());
          }
          for (String day : lstDateOfWeek) {
            if (item.getDayOff() != null && !item.getDayOff().isEmpty()) {
              if (item.getDayOff().contains(day)) {
                continue;
              }
            }
            lstData.add(day);
          }
          String workingDay = lstData.toString();
          workingDay = workingDay.substring(1, workingDay.toString().length() - 1);
          item.setWorkingDay(workingDay);
          lstData.clear();
        }
      }
    } else {
      SimpleDateFormat sy = new SimpleDateFormat("yyyy");
      try {
        date = DateUtil.string2Date(scheduleEmployeeDTO.getStartDate());
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      String year = sy.format(date);

      SimpleDateFormat sp2 = new SimpleDateFormat("dd");
      try {
        date = DateUtil.string2Date(scheduleEmployeeDTO.getStartDate());
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      String startDate2 = sp2.format(date);
      int startDate = Integer.valueOf(startDate2);

      SimpleDateFormat sp4 = new SimpleDateFormat("MM");
      try {
        date = DateUtil.string2Date(scheduleEmployeeDTO.getStartDate());
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      String month = sp4.format(date);
      List<String> lstDateOfDay = new ArrayList<>();
      for (int i = startDate; i <= startDate; i++) {
        String ngay = ((i < 10) ? ("0" + i) : i) + "/" + month + "/" + year;
        lstDateOfDay.add(ngay);
      }
      List<String> lstData = new ArrayList<>();
      if (lst != null && !lst.isEmpty()) {
        for (ScheduleEmployeeDTO item : lst) {
          ItemDataCRInside dtoU = mapArray.get(item.getEmpArray());
          if (item.getEmpArray() != null) {
            item.setParentName(dtoU.getDisplayStr());
          }
          CfgChildArrayDTO dtoChild = mapArrayChild.get(item.getEmpChildren());
          if (item.getEmpChildren() != null) {
            item.setArrayChildName(dtoChild.getChildrenName());
          }
          for (String day : lstDateOfDay) {
            if (item.getDayOff() != null && !item.getDayOff().isEmpty()) {
              if (item.getDayOff().contains(day)) {
                continue;
              }
            }
            lstData.add(day);
          }
          String workingDay = lstData.toString();
          workingDay = workingDay.substring(1, workingDay.toString().length() - 1);
          item.setWorkingDay(workingDay);
          lstData.clear();
        }
      }
    }
    //datatable.setData(lst);
    return datatable;
  }

  @Override
  public ResultInSideDto cancelStatus(RequestScheduleDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    dto.setStatus(2L);
    try {
      dto.setStartDate(dto.getStartDate());
      dto.setEndDate(dto.getEndDate());
      dto.setCreatedDate(new Date());
      String result = cfgRequestScheduleRepository.updateRequestSchedule(dto);
      String message = "";
      if (result.equals(RESULT.SUCCESS)) {
        message = I18n.getLanguage("cfgRequestSchedule.cancelStatus.success");
      } else {
        message = I18n.getLanguage("cfgRequestSchedule.cancelStatus.fail");
      }
      resultInSideDto.setMessage(message);
      resultInSideDto.setKey(RESULT.SUCCESS);
      UserToken userToken = ticketProvider.getUserToken();
      logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "CancelStatus", "CancelStatus ID: " + dto.getIdSchedule(),
          dto, null));
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setMessage(RESULT.ERROR);
      resultInSideDto.setKey(RESULT.ERROR);
      return resultInSideDto;
    }
  }

  @Override
  public List<ScheduleCRDTO> onSearchCR(ScheduleCRDTO scheduleCRDTO, Long type) {
    log.debug("Request to getListScheduleCR : {}", scheduleCRDTO);
    scheduleCRDTO.setType(type);
    Map<String, ItemDataCRInside> mapArray = getMapArray();
    List<ScheduleCRDTO> lst = cfgRequestScheduleRepository.getListScheduleCR(scheduleCRDTO);
    if (lst != null) {
      for (ScheduleCRDTO dtoS : lst) {
        dtoS.setCrPrioritizeName(StringUtils.isStringNullOrEmpty(dtoS.getCrPrioritize()) ? "" : I18n
            .getChangeManagement(
                Constants.CR_PRIORITY.getGetPriorityName()
                    .get(Long.valueOf(dtoS.getCrPrioritize()))));
        if (dtoS.getCrLevel() < 2) {
          dtoS.setCrLevel(1L);
        } else {
          dtoS.setCrLevel(2L);
        }
        dtoS.setCrArrayName(
            mapArray.get(dtoS.getCrArray()) != null ? mapArray.get(dtoS.getCrArray())
                .getDisplayStr() : null);
        dtoS.setForbiddenDate(
            dtoS.getForbiddenDate() != null ? dtoS.getForbiddenDate().replaceAll(",", ";") : "");
        dtoS.setCrLevelName(StringUtils.isStringNullOrEmpty(dtoS.getCrLevel()) ? ""
            : (dtoS.getCrLevel() < 2 ? I18n.getLanguage("schedule.crLevel.hard")
                : I18n.getLanguage("schedule.crLevel.easy")));//do kho : < 2 => kho
        if (StringUtils.isNotNullOrEmpty(dtoS.getCrPerformer())) {
          UsersInsideDto usersDTO = userBusiness.getUserDTOByUserName(dtoS.getCrPerformer());
          if (usersDTO != null) {
            dtoS.setCrPerformerName(usersDTO.getFullname() + "(" + usersDTO.getUsername() + ")");
          }
        }
      }
    }
    return lst;
  }

  @Override
  public File exportData(RequestScheduleDTO requestScheduleDTO) throws Exception {
    RequestScheduleDTO requestScheduleDTO1 = getCrBefore(requestScheduleDTO);
    List<ScheduleCRFormDTO> list = requestScheduleDTO1.getScheduleCRFormDTOS();
    List<ScheduleCRDTO> listExport = new ArrayList<>();
    if (list != null && !list.isEmpty()) {
      for (ScheduleCRFormDTO scheduleCRDTO : list) {
        ScheduleCRDTO exportDTO = new ScheduleCRDTO();
        exportDTO.setId(scheduleCRDTO.getId() == null ? null : Long.valueOf(scheduleCRDTO.getId()));
        exportDTO.setIdCr(scheduleCRDTO.getIdCr());
        exportDTO.setIdSchedule(scheduleCRDTO.getIdSchedule() == null ? null
            : Long.valueOf(scheduleCRDTO.getIdSchedule()));
        exportDTO.setCodeCR(scheduleCRDTO.getCodeCR());
        exportDTO.setCrArray(scheduleCRDTO.getCrArray());
        exportDTO.setCrChildren(scheduleCRDTO.getCrChildren());
        exportDTO.setCrDeadline(DateUtil.string2DateTime(scheduleCRDTO.getCrDeadline()));
        exportDTO.setCrLevel(scheduleCRDTO.getCrLevel());
        exportDTO.setCrLevelName(scheduleCRDTO.getCrLevelName());
        exportDTO.setExecutionTime(scheduleCRDTO.getExecutionTime());
        exportDTO.setCrPrioritize(scheduleCRDTO.getCrPrioritize());
        exportDTO
            .setRegistrationDate(DateUtil.string2DateTime(scheduleCRDTO.getRegistrationDate()));
        exportDTO.setStatus(
            scheduleCRDTO.getStatus() == null ? null : Long.valueOf(scheduleCRDTO.getStatus()));
        exportDTO.setStartDate(scheduleCRDTO.getStartDate());
        exportDTO.setEndDate(scheduleCRDTO.getEndDate());
        exportDTO.setCrPerformer(scheduleCRDTO.getCrPerformer());
        exportDTO.setType(
            scheduleCRDTO.getType() == null ? null : Long.valueOf(scheduleCRDTO.getType()));
        exportDTO.setImpactNodeList(scheduleCRDTO.getImpactNodeList());
        exportDTO.setAffectServiceList(scheduleCRDTO.getAffectServiceList());
        exportDTO.setCrPrioritizeName(scheduleCRDTO.getCrPrioritizeName());
        exportDTO.setForbiddenDate(scheduleCRDTO.getForbiddenDate());
        exportDTO.setIsFixedDay(scheduleCRDTO.getIsFixedDay());
        listExport.add(exportDTO);
      }
    }
    String[] header = new String[]{
        "codeCR", "endDate", "crArray",
        "crChildren", "crDeadline", "crLevel", "crLevelName", "executionTime", "crPrioritize",
        "registrationDate",
        "status", "startDate", "endDate", "crPerformer", "forbiddenDate", "impactNodeList",
        "affectServiceList"
    };
    return handleFileExport(listExport, header, DateUtil.date2ddMMyyyyHHMMss(new Date()), "");
  }

  @Override
  public File exportDataCRAfter(RequestScheduleDTO dto4) throws Exception {
    ScheduleCRDTO scheduleDTO = new ScheduleCRDTO();
    scheduleDTO.setIdSchedule(dto4.getIdSchedule());
    List<ScheduleCRDTO> list = onSearchCR(scheduleDTO, 1L);
    List<ScheduleCRDTO> scheduleCRDTOList = dto4.getCrAfterList();
    for (ScheduleCRDTO scheduleCRDTO : list) {
      if (scheduleCRDTOList != null && scheduleCRDTOList.size() > 0) {
        for (ScheduleCRDTO scheduleCRDTO1 : scheduleCRDTOList) {
          if (scheduleCRDTO.getIdCr().equals(scheduleCRDTO1.getIdCr())) {
            scheduleCRDTO.setStartDate(scheduleCRDTO1.getStartDate());
            scheduleCRDTO.setEndDate(scheduleCRDTO1.getEndDate());
            scheduleCRDTO.setForbiddenDate(scheduleCRDTO1.getForbiddenDate());
          }
        }
      }
    }
    List<ScheduleCRDTO> listExport = new ArrayList<>();
    if (list != null && !list.isEmpty()) {
      for (ScheduleCRDTO scheduleCRDTO : list) {
        ScheduleCRDTO exportDTO1 = new ScheduleCRDTO();
        exportDTO1.setId(scheduleCRDTO.getId());
        exportDTO1.setIdCr(scheduleCRDTO.getIdCr());
        exportDTO1.setIdSchedule(scheduleCRDTO.getIdSchedule());
        exportDTO1.setCodeCR(scheduleCRDTO.getCodeCR());
        exportDTO1.setCrArray(scheduleCRDTO.getCrArray());
        exportDTO1.setCrChildren(scheduleCRDTO.getCrChildren());
        exportDTO1.setCrDeadline(scheduleCRDTO.getCrDeadline());
        exportDTO1.setCrLevel(scheduleCRDTO.getCrLevel());
        exportDTO1.setCrLevelName(scheduleCRDTO.getCrLevelName());
        exportDTO1.setExecutionTime(scheduleCRDTO.getExecutionTime());
        exportDTO1.setCrPrioritize(scheduleCRDTO.getCrPrioritize());
        exportDTO1.setRegistrationDate(scheduleCRDTO.getRegistrationDate());
        exportDTO1.setStatus(scheduleCRDTO.getStatus());
        exportDTO1.setStartDate(scheduleCRDTO.getStartDate());
        exportDTO1.setEndDate(scheduleCRDTO.getEndDate());
        exportDTO1.setCrPerformer(scheduleCRDTO.getCrPerformerName());
        exportDTO1.setType(scheduleCRDTO.getType());
        exportDTO1.setImpactNodeList(scheduleCRDTO.getImpactNodeList());
        exportDTO1.setAffectServiceList(scheduleCRDTO.getAffectServiceList());
        exportDTO1.setCrPrioritizeName(scheduleCRDTO.getCrPrioritizeName());
        exportDTO1.setForbiddenDate(scheduleCRDTO.getForbiddenDate());
        exportDTO1.setIsFixedDay(scheduleCRDTO.getIsFixedDay());
        listExport.add(exportDTO1);
      }
    }
    String[] header = new String[]{
        "codeCR", "endDate", "crArray",
        "crChildren", "crDeadline", "crLevel", "crLevelName", "executionTime", "crPrioritize",
        "registrationDate",
        "status", "startDate", "endDate", "crPerformer", "forbiddenDate", "impactNodeList",
        "affectServiceList"
    };
    return handleFileExport(listExport, header, DateUtil.date2ddMMyyyyHHMMss(new Date()), "");
  }

  private File handleFileExport(List<ScheduleCRDTO> listExport, String[] columnExport,
      String date, String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("schedule.cr.report.sheetname");
    String title = I18n.getLanguage("schedule.cr.report.title");
    String fileNameOut = I18n.getLanguage("schedule.cr.report.title");
    String subTitle;
    if (date != null) {
      Date fromDateTmp = DataUtil.convertStringToDate(date);
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      subTitle = I18n.getLanguage("schedule.cr.report.eportDate", dateFormat.format(fromDateTmp));
    } else {
      subTitle = "";
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExport,
        sheetName,
        title,
        subTitle,
        7,
        3,
        6,
        true,
        "language.schedule.cr.report",
        lstHeaderSheet1,
        fieldSplit,
        "",
        I18n.getLanguage("schedule.cr.report.export.firstLeftHeader"),
        I18n.getLanguage("schedule.cr.report.export.secondLeftHeader"),
        I18n.getLanguage("schedule.cr.report.export.firstRightHeader"),
        I18n.getLanguage("schedule.cr.report.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("schedule.cr.report.stt"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
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


  @Override
  public List<ScheduleCRDTO> searchCRAfterFail(RequestScheduleDTO requestScheduleDTO) {
    List<ScheduleCRDTO> lst;
    ScheduleCRDTO dto = new ScheduleCRDTO();
    try {
      dto.setType(0L);
      dto.setIdSchedule(requestScheduleDTO.getIdSchedule());
      lst = onSearchCR(dto, 0L);
      if (lst == null || lst.isEmpty()) {
        lst = new ArrayList<>();
      } else {
        dto.setType(1L);
        List<ScheduleCRDTO> lstCrAfter = onSearchCR(dto, 1L);
        if (lstCrAfter != null && !lstCrAfter.isEmpty()) {
          int sizeFail = lst.size();
          for (int i = sizeFail - 1; i > -1; i--) {
            for (ScheduleCRDTO form : lstCrAfter) {
              if (lst.get(i).getCodeCR().equals(form.getCodeCR())) {
                lst.remove(i);
                break;
              }
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
    return lst;
  }

  @Override
  public RequestScheduleDTO getCrBefore(RequestScheduleDTO dtoCheck) {
    RequestScheduleDTO requestScheduleDTO = new RequestScheduleDTO();

    if (validate(dtoCheck)) {
      //isGetCrBefore = true;
      dtoCheck.setIsGetCrBefore(1L);
      String startDate = "";
      String endDate = "";
      String type = dtoCheck.getType();
      if ("0".equals(type)) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.valueOf(dtoCheck.getYear()));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, Integer.valueOf(dtoCheck.getMonth()) - 1);

        startDate = DateUtil.date2ddMMyyyyString(cal.getTime()) + " 00:00:00";

        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DATE, -1);

        endDate = DateUtil.date2ddMMyyyyString(cal.getTime()) + " 23:59:59";
      } else if ("1".equals(type)) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, Integer.valueOf(dtoCheck.getWeek()));
        calendar.set(Calendar.YEAR, Integer.valueOf(dtoCheck.getYear()));

        //lay ngay dau tuan
        Date date = calendar.getTime();
        startDate = DateUtil.date2ddMMyyyyString(date) + " 00:00:00";

        //set thoi gian + them 7 ngay
        calendar.set(Calendar.DAY_OF_MONTH, date.getDate());
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        date = calendar.getTime();

        endDate = DateUtil.date2ddMMyyyyString(date) + " 23:59:59";
      } else if ("2".equals(type)) {
        startDate = DateUtil.date2ddMMyyyyString(dtoCheck.getPdfDay()) + " 23:00:00";
        String nextDay = DateUtil.date2ddMMyyyyString(DateUtil.addDay(dtoCheck.getPdfDay(), 1));
        endDate = nextDay + " 04:59:59";
      }
      CrInsiteDTO crInsiteDTO = new CrInsiteDTO();
      try {
        crInsiteDTO.setEarliestStartTime(DateUtil.string2DateTime(startDate));
      } catch (ParseException e) {
        log.error(e.getMessage(), e);
      }
      crInsiteDTO.setEarliestStartTimeTo(endDate);
      crInsiteDTO.setChangeResponsibleUnit(
          dtoCheck.getUnitId() == null ? null : String.valueOf(dtoCheck.getUnitId()));
      crInsiteDTO.setDutyType("3,6,1,5");
      crInsiteDTO.setState("11,0,1,12,2,3,4,5,6");
      if ("2".equals(type)) {
        crInsiteDTO.setState("11,1,12,2,3,4,5,6");
      }
      List<CrInsiteDTO> lstCr = getListCrAutoSchedule(crInsiteDTO);
      List<ScheduleCRFormDTO> lstCrBefore = new ArrayList<>();
      CfgChildArrayDTO cfgChildArrayDTO = new CfgChildArrayDTO();
      cfgChildArrayDTO.setStatus(1L);
      List<CfgChildArrayDTO> lstArrayChildTemp = cfgRequestScheduleRepository
          .getListCfgChildArray(cfgChildArrayDTO);
      Map<String, CfgChildArrayDTO> mapArrayChild = new HashMap<>();
      if (lstArrayChildTemp != null && !lstArrayChildTemp.isEmpty()) {
        for (CfgChildArrayDTO itemDTO : lstArrayChildTemp) {
          if (itemDTO.getChildrenId() != null) {
            mapArrayChild.put(itemDTO.getChildrenId().toString(), itemDTO);
            mapArrayChild.put(itemDTO.getChildrenName(), itemDTO);
          }
        }
      }

      Map<String, ItemDataCRInside> mapArray = getMapArray();

      if (lstCr != null && !lstCr.isEmpty()) {
        for (CrInsiteDTO cr : lstCr) {
          ScheduleCRFormDTO dtoS = new ScheduleCRFormDTO();
          dtoS.setIdCr(cr.getCrId() == null ? null : Long.valueOf(cr.getCrId()));
          dtoS.setCodeCR(cr.getCrNumber());
          dtoS.setCrPrioritize(cr.getPriority() == null ? null : Long.valueOf(cr.getPriority()));
          dtoS.setCrPrioritizeName(StringUtils.isStringNullOrEmpty(cr.getPriority()) ? "" : I18n
              .getChangeManagement(
                  Constants.CR_PRIORITY.getGetPriorityName().get(Long.valueOf(cr.getPriority()))));
          dtoS.setExecutionTime(cr.getResponeTime());
          dtoS.setType("0");
          dtoS.setCrLevel(cr.getRisk() == null ? null : Long.valueOf(cr.getRisk()));
          dtoS.setCrLevelName(StringUtils.isStringNullOrEmpty(cr.getRisk()) ? ""
              : (Integer.valueOf(cr.getRisk()) < 2 ? I18n.getLanguage("schedule.crLevel.hard")
                  : I18n.getLanguage("schedule.crLevel.easy")));
          try {
            dtoS.setStartDate(!StringUtils.isStringNullOrEmpty(cr.getEarliestStartTime()) ? cr
                .getEarliestStartTime() : DateUtil.string2Date(startDate));

            dtoS.setEndDate(
                !StringUtils.isStringNullOrEmpty(cr.getLatestStartTime()) ? cr.getLatestStartTime()
                    : DateUtil.string2Date(endDate));
          } catch (ParseException e) {
            log.error(e.getMessage(), e);
          }
          dtoS.setImpactNodeList(cr.getDescription());//danh sach node mang
          dtoS.setAffectServiceList(cr.getAffectedService());//danh sach dich vu
          dtoS.setCrArrayName(cr.getImpactSegment());
          ItemDataCRInside array = mapArray.get(cr.getImpactSegment());
          if (array != null) {
            dtoS.setCrArray(array.getValueStr() == null ? null : array.getValueStr().toString());
          }
          CfgChildArrayDTO child = mapArrayChild.get(cr.getChildImpactSegment());
          if (child != null) {
            dtoS.setCrChildrenName(cr.getChildImpactSegment());
            dtoS.setCrChildren(
                child.getChildrenId() == null ? null : child.getChildrenId().toString());
          }
          lstCrBefore.add(dtoS);
        }
        requestScheduleDTO.setScheduleCRFormDTOS(lstCrBefore);
      } else {
        String message = I18n.getLanguage("cfgRequestSchedule.listCrEmpty");
        requestScheduleDTO.setMessage(message);
      }
      ScheduleEmployeeDTO dto = new ScheduleEmployeeDTO();
      dto.setUnitId(dtoCheck.getUnitId());
      dto.setYear(dtoCheck.getYear());
      dto.setType(dtoCheck.getType());
      dto.setMonth(dtoCheck.getMonth());
      dto.setWeek(dtoCheck.getWeek());
      dto.setDayOff(dtoCheck.getDays());
      dto.setPage(dtoCheck.getPage());
      dto.setPageSize(dtoCheck.getPageSize());
      dto.setIdSchedule(dtoCheck.getIdSchedule());
      dto.setStartDate(DateUtil.date2ddMMyyyyHHMMss(dtoCheck.getStartDate()));
      dto.setEndDate(DateUtil.date2ddMMyyyyHHMMss(dtoCheck.getEndDate()));
      //Datatable datatable = cfgRequestScheduleRepository.get(dto);
      List<ScheduleEmployeeDTO> datatable;
      if (dto.getIdSchedule() != null) {
        datatable = getListEmployee(dto);
      } else {
        datatable = cfgRequestScheduleRepository.getListEmployee2(dto);
      }
      //List<ScheduleEmployeeDTO> datatable = getListEmployee(dto);
      List<ScheduleEmployeeDTO> lst = datatable;
      requestScheduleDTO.setScheduleEmployeeDTOS(lst);
    }
    return requestScheduleDTO;
  }

  private List<CrInsiteDTO> getListCrAutoSchedule(CrInsiteDTO crInsiteDTO) {
    List<CrInsiteDTO> crInsiteDTOS = cfgRequestScheduleRepository
        .getListCrAutoSchedule(crInsiteDTO);
    return crInsiteDTOS;
  }

  private boolean validate(RequestScheduleDTO dtoCheck) {
    if (StringUtils.isStringNullOrEmpty(dtoCheck.getUnitId()) || "-1"
        .equals(dtoCheck.getUnitId())) {
      return false;
    }
    String type = dtoCheck.getType();
    if ("0".equals(type)) {
      if (StringUtils.isStringNullOrEmpty(dtoCheck.getMonth())) {
        return false;
      }
      if (StringUtils.isStringNullOrEmpty(dtoCheck.getYear()) || "-1".equals(dtoCheck.getYear())) {
        return false;
      }
    } else if ("1".equals(type)) {
      String week = dtoCheck.getWeek();
      if (week == "0") {
        return false;
      }
      if (StringUtils.isStringNullOrEmpty(dtoCheck.getYear())) {
        return false;
      }
    } else {
      if (StringUtils.isStringNullOrEmpty(dtoCheck.getPdfDay())) {
        return false;
      }
    }
    Long working = dtoCheck.getWorkTime();
    if (StringUtils.isLongNullOrEmpty(working)) {
      return false;
    } else if (working < 1L) {
      return false;
    } else if (working > 24L) {
      return false;
    }
    return true;
  }

  @Override
  public ResultInSideDto onSave(RequestScheduleDTO dtoAction) {
    RequestScheduleDTO dto = new RequestScheduleDTO();
    ResultInSideDto result = new ResultInSideDto();
    String startDateEndDate = "";
    try {
      boolean check = validate(dtoAction);
      String res = "";
      List<ScheduleEmployeeDTO> itemContainerEmpAdd = dtoAction.getScheduleEmployeeDTOS();
      if (check) {
        if (dtoAction.getIsGetCrBefore() != 1L) {
          String message = I18n.getLanguage("cfgRequestSchedule.notGetCrYet");
          result.setKey(RESULT.ERROR);
          result.setMessage(message);
          return result;
        }
        if (!StringUtils.isLongNullOrEmpty(dtoAction.getComplicateWork())
            && dtoAction.getComplicateWork() == 1L) {
          dto.setComplicateWork(1L);
        } else {
          dto.setComplicateWork(0L);
        }
        if (!StringUtils.isLongNullOrEmpty(dtoAction.getSameNode())
            && dtoAction.getSameNode() == 1L) {
          dto.setSameNode(1L);
        } else {
          dto.setSameNode(0L);
        }
        if (!StringUtils.isLongNullOrEmpty(dtoAction.getSameService())
            && dtoAction.getSameService() == 1L) {
          dto.setSameService(1L);
        } else {
          dto.setSameService(0L);
        }

        String type = dtoAction.getType();
        if (type != null) {
          if ("0".equals(type)) {
            int monthInt = Integer.valueOf(dtoAction.getMonth());
            dto.setDetail(monthInt < 10 ? "0" + monthInt : String.valueOf(monthInt));
            String month = dtoAction.getMonth();
            if (!StringUtils.isStringNullOrEmpty(month)) {
              String year = dtoAction.getYear();
              if (!StringUtils.isStringNullOrEmpty(year)) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.valueOf(year));
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.MONTH, Integer.valueOf(month) - 1);
                String startDate_temp = DateUtil.date2ddMMyyyyString(cal.getTime());
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.DATE, -1);
                String endDate_temp = DateUtil.date2ddMMyyyyString(cal.getTime());
                startDateEndDate = startDate_temp + "_" + endDate_temp;
                int lastDay = cal.get(Calendar.DAY_OF_MONTH);
                try {
                  Date startDate = DateUtil
                      .string2Date("01" + "/" + month + "/" + year);
                  Date endDate = DateUtil
                      .string2Date("" + lastDay + "/" + month + "/" + year);
                  dto.setStartDate(startDate);
                  dto.setEndDate(endDate);
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                }
              } else {
                dtoAction.setDetail("");
              }
            } else {
              dtoAction.setDetail("");
            }
          } else if ("1".equals(type)) {
            int weekInt = Integer.valueOf(dtoAction.getWeek());
            dto.setDetail(weekInt < 10 ? "0" + weekInt : String.valueOf(weekInt));
            String week = dtoAction.getWeek();
            if (week == null || week != "0") {
              String year = dtoAction.getYear();
              if (!StringUtils.isStringNullOrEmpty(year)) {
                //set nam, set tuan trong nam
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.WEEK_OF_YEAR, Integer.valueOf(week));
                calendar.set(Calendar.YEAR, Integer.valueOf(year));
                String startDate_temp = DateUtil.date2ddMMyyyyString(calendar.getTime());
                //lay ngay dau tuan
                Date date = calendar.getTime();
                Date startDate = date;

                //set thoi gian + them 7 ngay
                calendar.set(Calendar.DAY_OF_MONTH, date.getDate());
                calendar.add(Calendar.DAY_OF_MONTH, 6);
                String endDate_temp = DateUtil.date2ddMMyyyyString(calendar.getTime());
                startDateEndDate = startDate_temp + "_" + endDate_temp;

                date = calendar.getTime();

                Date endDate = date;
                try {
                  dto.setStartDate(startDate);
                  dto.setEndDate(endDate);
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                }
              } else {
                dtoAction.setDetail("");
              }
            }
          } else {
            if (dtoAction.getPdfDay() != null) {
              dto.setStartDate(DateUtil.addMinute(dtoAction.getPdfDay(), 23 * 60));
              dto.setEndDate(DateUtil.addMinute(dtoAction.getPdfDay(), 24 * 60 + 4 * 60 + 59));
              startDateEndDate =
                  DateUtil.date2ddMMyyyyString(dtoAction.getPdfDay()) + "_" + DateUtil
                      .date2ddMMyyyyString(DateUtil.addDay(dtoAction.getPdfDay(), 1));
            } else {
              dto.setStartDate(null);
              dto.setEndDate(null);
            }
//            dto.setStartDate(dtoAction.getPdfDay() != null ? dtoAction.getPdfDay() : null);
//            dto.setEndDate(dtoAction.getPdfDay() != null ? dtoAction.getPdfDay() : null);
            if (!StringUtils.isLongNullOrEmpty(dtoAction.getSameServiceShift())
                && dtoAction.getSameServiceShift() == 1L) {
              dto.setSameServiceShift(1L);
            } else {
              dto.setSameServiceShift(0L);
            }
            if (!StringUtils.isLongNullOrEmpty(dtoAction.getSameNodeShift())
                && dtoAction.getSameNodeShift() == 1L) {
              dto.setSameNodeShift(1L);
            } else {
              dto.setSameNodeShift(0L);
            }
          }
        }
        UserToken userToken = ticketProvider.getUserToken();
        UnitDTO unitToken = unitRepository.findUnitById(dtoAction.getUnitId());
        dto.setUnitId(unitToken.getUnitId());
        dto.setUnitName(unitToken.getUnitName());
        dto.setCreatedUser(userToken.getUserName());
        dto.setCreatedDate(new Date());
        dto.setStatus(0L);
        dto.setType(dtoAction.getType());
        dto.setWorkTime(dtoAction.getWorkTime());
        dto.setWeek(dtoAction.getWeek());
        dto.setMonth(dtoAction.getMonth());
        dto.setYear(dtoAction.getYear());
        if (dtoAction.getIsInsert() == 1L) {
          if (checkExisted(dto)) {
            String message = I18n.getLanguage("cfgRequestSchedule.existed");
            ResultInSideDto resultInSideDto = new ResultInSideDto();
            resultInSideDto.setKey(RESULT.DUPLICATE);
            resultInSideDto.setMessage(message);
            return resultInSideDto;
          } else {
            Date date;
            result = insertRequestSchedule(dto);

            if ("2".equals(dto.getType())) {
              SimpleDateFormat sy = new SimpleDateFormat("yyyy");
              try {
                date = dto.getStartDate();
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                date = new Date();
              }
              String year = sy.format(date);
              dto.setYear(year);
            }
            dto.setIdSchedule(result.getId());
          }
        } else {
          dto.setIdSchedule(dtoAction.getIdSchedule());
          dto.setCreatedUser(userToken.getUserName());
          dto.setCreatedDate(new Date());
          if (checkExisted(dto)) {
            String message = I18n.getLanguage("cfgRequestSchedule.existed");
            result.setKey(RESULT.ERROR);
            result.setMessage(message);
            return result;
          }
        }
//        ScheduleEmployeeDTO dto1 = new ScheduleEmployeeDTO();
//        dto1.setUnitId(dtoAction.getUnitId());
//        dto1.setIdSchedule(dtoAction.getIdSchedule());
//        dto1.setYear(dtoAction.getYear());
//        dto1.setType(dtoAction.getType());
//        dto1.setMonth(dtoAction.getMonth());
//        dto1.setWeek(dtoAction.getWeek());
//        dto1.setDayOff(dtoAction.getDays());
//        dto1.setPage(dtoAction.getPage());
//        dto1.setPageSize(dtoAction.getPageSize());
//        List<ScheduleEmployeeDTO> datatable1;
//        if (dto1.getIdSchedule() != null) {
//          datatable1 = cfgRequestScheduleRepository.getListEmployee(dto1);
//        } else {
//          datatable1 = cfgRequestScheduleRepository.getListEmployee2(dto1);
//        }
        List<ScheduleEmployeeDTO> lst = dtoAction.getScheduleEmployeeDTOS();
        List<ScheduleEmployeeDTO> lstEmpAdd = new ArrayList<>();
        if (lst != null && !lst.isEmpty()) {
          for (ScheduleEmployeeDTO item : lst) {
            ScheduleEmployeeDTO dtoNew = item;
            dtoNew.setIdSchedule(
                dtoAction.getIsInsert() == 1L ? result.getId() : dto.getIdSchedule());
            lstEmpAdd.add(dtoNew);
          }
        }

        if (lstEmpAdd != null && lstEmpAdd.size() > 0) {
          res = insertListDayOffDTO(lstEmpAdd);
        }

        //<editor-fold desc="luu vao db danh sach CR truoc sap lich, type = 0" defaultstate="collapsed">
        List<ScheduleCRDTO> lstScheduleCrBefore = new ArrayList<>();
        if (!dtoAction.getScheduleCRFormDTOS().isEmpty()) {
          try {
            List<ScheduleCRFormDTO> lstScheduleCrFormBefore = dtoAction.getScheduleCRFormDTOS();
            for (ScheduleCRFormDTO crBefore : lstScheduleCrFormBefore) {
              ScheduleCRDTO dtoNew = new ScheduleCRDTO();
              dtoNew.setId(crBefore.getId() == null ? null : Long.valueOf(crBefore.getId()));
              dtoNew.setIdSchedule(
                  StringUtils.isStringNullOrEmpty(result.getId()) ? dtoAction.getIdSchedule()
                      : result.getId());
              dtoNew.setCrChildren(crBefore.getCrChildren());
              dtoNew.setCodeCR(crBefore.getCodeCR());
              dtoNew.setIdCr(crBefore.getIdCr());
              dtoNew.setCrArray(crBefore.getCrArray());
              dtoNew.setCrLevel(crBefore.getCrLevel());
              dtoNew.setCrPrioritize(crBefore.getCrPrioritize());
              dtoNew.setExecutionTime(crBefore.getExecutionTime());
              dtoNew.setAffectServiceList(crBefore.getAffectServiceList());
              dtoNew.setImpactNodeList(crBefore.getImpactNodeList());
              dtoNew.setType(0L);
              dtoNew.setStartDate(crBefore.getStartDate());
              dtoNew.setEndDate(crBefore.getEndDate());
              if (!StringUtils.isStringNullOrEmpty(crBefore.getForbiddenDate())) {
                String[] forbiddenArr = crBefore.getForbiddenDate().split(";");
                if (forbiddenArr != null) {
                  List<String> lstForbidden = new ArrayList<>();
                  for (String d : forbiddenArr) {
                    lstForbidden.add(d);
                  }
                  dtoNew.setForbiddenDate(
                      lstForbidden.toString().substring(1, lstForbidden.toString().length() - 1));
                }
              }
//              //dtoNew.setIsFixedDay(crBefore.getIsChecked() == 1L ? "1" : "0");
//              try {
//                dtoNew.setEndDate(
//                    crBefore.getEndDate());
//                dtoNew.setStartDate(crBefore.getStartDate());
//              } catch (Exception e) {
//                log.error(e.getMessage(), e);
//              }
              lstScheduleCrBefore.add(dtoNew);
            }
            List<ScheduleCRDTO> lstScheduleTmp = new ArrayList<>();
            lstScheduleTmp.addAll(lstScheduleCrBefore);
            for (ScheduleCRDTO s : lstScheduleTmp) {
              try {
                s.setStartDate(s.getStartDate());
                s.setEndDate(s.getEndDate());
              } catch (Exception e) {
                log.error(e.getMessage(), e);
              }
            }
            if (dtoAction.getIsInsert() == 1L) {
              res = insertListCRDTO(lstScheduleTmp);
            } else {
              ScheduleCRDTO u = new ScheduleCRDTO();
              u.setType(0L);
              u.setIdSchedule(dtoAction.getIdSchedule());
              List<ScheduleCRDTO> lstU = cfgRequestScheduleRepository.getListScheduleCR(u);
              if (lstU != null && !lstU.isEmpty()) {
                for (ScheduleCRDTO uCr : lstU) {
                  uCr.setType(0L);
                }
              }
              res = insertListCRDTO(lstU);
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }

        if ("SUCCESS".equals(res)) {
          //<editor-fold desc="goi lib sap lich" defaultstate="collapsed">
          List<ScheduleCRDTO> lstCrAfter = new ArrayList<>();
          if ("0".equals(type) || "1".equals(type)) {
            //<editor-fold desc="Thang va Tuan" defaultstate="collapsed">
            //NativeUtils.loadLibraryFromJar("MonthCore.dll");
            //NativeUtils.loadLibraryFromJar("/MonthAdapter.dll");
            String[] arr = startDateEndDate.split("_");
//                        String schedulingStartDate = DateUtil.dateToStringWithPattern(DateUtil.string2Date(arr[0]), "dd-MM-yy");
            String schedulingStartDate = arr[0];
//                        String schedulingEndDate = DateUtil.dateToStringWithPattern(DateUtil.string2Date(arr[1]), "dd-MM-yy");
            String schedulingEndDate = arr[1];
            List<Cr> listCr = new ArrayList<>();
            Config config = new Config(
                schedulingStartDate,
                schedulingEndDate,
                dtoAction.getWorkTime().intValue() * 60);
            Constraint constraint = new Constraint(
                ((dtoAction.getComplicateWork() == null || dtoAction.getComplicateWork() == 0L)
                    ? false : true),
                ((dtoAction.getSameNode() == null || dtoAction.getSameNode() == 0L) ? false : true),
                (dtoAction.getSameService() == null || dtoAction.getSameService() == 0L ? false
                    : true));
            for (ScheduleCRDTO item : lstScheduleCrBefore) {
              Cr cr = new Cr();
              cr.setId(item.getIdCr() == null ? null : String.valueOf(item.getIdCr()));
              if (!StringUtils.isStringNullOrEmpty(item.getCrChildren())) {
                if (StringUtils.isInteger(item.getCrChildren())) {
                  cr.setSubdivision(Integer.valueOf(item.getCrChildren()));
                }
              }
//              if(StringUtils.isStringNullOrEmpty(cr.getSubdivision())) {
//                cr.setSubdivision(Integer.parseInt("88"));
//              }
              cr.setStartDate(DateUtil.date2ddMMyyyyHHMMss(item.getStartDate()));
              cr.setEndDate(DateUtil.date2ddMMyyyyHHMMss(item.getEndDate()));
              cr.setDifficult((item.getCrLevel().intValue() < 2));
              cr.setExecutionTime(Integer.valueOf(item.getExecutionTime()));
              List<String> lstNodeId = new ArrayList<>();
              if (!StringUtils.isStringNullOrEmpty(item.getImpactNodeList())) {
                String[] arrNode = item.getImpactNodeList().split(",");
                for (String s : arrNode) {
                  lstNodeId.add(s.trim());
                }
              }
              cr.setListNodeId(lstNodeId);

              List<String> lstServiceId = new ArrayList<>();
              if (!StringUtils.isStringNullOrEmpty(item.getAffectServiceList())) {
                String[] arrService = item.getAffectServiceList().split(",");
                for (String s : arrService) {
                  lstServiceId.add(s.trim());
                }
              }
              cr.setListServiceId(lstServiceId);
              cr.setPriority(item.getCrPrioritize().intValue());
//              cr.setFixedDay("1".equals(item.getIsFixedDay()));
              if (!StringUtils.isStringNullOrEmpty(item.getForbiddenDate())) {
                String[] forb = item.getForbiddenDate().split(",");
                List<String> lstForb = new ArrayList<>();
                for (String f : forb) {
                  String d = DateUtil
                      .date2ddMMyyyyString(DateUtil.convertStringToTime(f.trim(), "dd-MM-yy"));
                  lstForb.add(d);
                }
                cr.setListForbiddenDate(lstForb);
//                        cr.setListForbiddenDate(new ArrayList<>());
              } else {
                cr.setListForbiddenDate(new ArrayList<>());
              }
              listCr.add(cr);
            }
            List<Employee> listEmployee = new ArrayList<>();
            if (!itemContainerEmpAdd.isEmpty()) {
              List<ScheduleEmployeeDTO> lstEmp = itemContainerEmpAdd;
              for (ScheduleEmployeeDTO emp : lstEmp) {
                Employee employee = new Employee();
                employee.setId(emp.getUserName());

                Map<Integer, Boolean> subExp = new HashMap<>();
                Integer subDivision = StringUtils.isStringNullOrEmpty(emp.getEmpChildren()) ? null
                    : (!StringUtils.isInteger(emp.getEmpChildren()) ? null
                        : Integer.valueOf(emp.getEmpChildren()));
//                Integer subDivision = Integer.parseInt("88");
//                Boolean hasExp = false;
                subExp.put(subDivision, ("1".equals(String.valueOf(emp.getEmpLevel()))));
//                subExp.put(subDivision, hasExp);
                employee.setSubExp(subExp);
                List<String> listForbiddenDay = new ArrayList<>();
                if (!StringUtils.isStringNullOrEmpty(emp.getDayOff())) {
                  String[] arrDayOff = emp.getDayOff().split(",");
                  for (String d : arrDayOff) {
                    listForbiddenDay.add(d.trim());
                  }
                }
                employee.setListForbiddenDay(listForbiddenDay);
                listEmployee.add(employee);
              }
            }

            Input input = new Input();
            input.setListCr(listCr);
            input.setListEmployee(listEmployee);
            input.setConfig(config);
            input.setConstraint(constraint);
            Output output = new Output();
            try {
//              Resource resource = new ClassPathResource("/MonthCore.dll");
//              System.out.println(((ClassPathResource) resource).getURI().getPath());
//              NativeUtils.loadLibraryFromJar(((ClassPathResource) resource).getURI().getPath());

              /*NativeUtils.loadLibraryFromJar("/MonthCore.dll");
              NativeUtils.loadLibraryFromJar("/MonthAdapter.dll");*/
//              Resource resource2 = new ClassPathResource("/MonthAdapter.dll");
//              System.out.println(((ClassPathResource) resource2).getURI().getPath());
//              NativeUtils.loadLibraryFromJar(((ClassPathResource) resource2).getURI().getPath());
              output = OrlabViettelCrMonthSolver.getSchedule(input);
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
            System.out.println(output.getListServedCr());

            if (!StringUtils.isStringNullOrEmpty(output.getListServedCr())) {
              for (ServedCr crAfter : output.getListServedCr()) {
                for (ScheduleCRDTO crBefore : lstScheduleCrBefore) {
                  if (crAfter.getCrId().equals(String.valueOf(crBefore.getIdCr()))) {
                    crBefore.setType(1L);
                    crBefore.setCrPerformer(crAfter.getEmployeeId());
                    try {
                      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                      String dateString = crAfter.getDate();
                      Date date = format.parse(dateString);
                      crBefore.setRegistrationDate(date);
                    } catch (Exception e) {
                      log.error(e.getMessage(), e);
                    }
                    lstCrAfter.add(crBefore);
                  }
                }
              }
            }
            //</editor-fold>
          } else {
            //<editor-fold desc="Ngay" defaultstate="collapsed">
            List<com.orlab.day.input.Cr> listCr = new ArrayList<>();
            com.orlab.day.input.Config config = new com.orlab.day.input.Config(
                DateUtil.dateToStringWithPattern(dtoAction.getPdfDay(), "dd/MM/yyyy"),
                "23:00:00", "04:59:00", Integer.valueOf(dtoAction.getWorkTime().intValue()) * 60
            );
            com.orlab.day.input.Constraint constraint = new com.orlab.day.input.Constraint(
                ((dtoAction.getSameNodeShift() == null || dtoAction.getSameNodeShift() == 0L)
                    ? false : true),
                ((dtoAction.getComplicateWork() == null || dtoAction.getComplicateWork() == 0L)
                    ? false : true),
                ((dtoAction.getSameServiceShift() == null || dtoAction.getSameServiceShift() == 0L)
                    ? false : true),
                ((dtoAction.getSameNode() == null || dtoAction.getSameNode() == 0L) ? false : true),
                ((dtoAction.getSameService() == null || dtoAction.getSameService() == 0L) ? false
                    : true)
            );
            for (ScheduleCRDTO item : lstScheduleCrBefore) {
              com.orlab.day.input.Cr cr = new com.orlab.day.input.Cr();
              cr.setId(item.getIdCr() == null ? null : String.valueOf(item.getIdCr()));
              cr.setSubdivision(StringUtils.isStringNullOrEmpty(item.getCrChildren()) ? null
                  : (!StringUtils.isInteger(item.getCrChildren()) ? null
                      : Integer.valueOf(item.getCrChildren())));
//              if(StringUtils.isStringNullOrEmpty(cr.getSubdivision())) {
//                cr.setSubdivision(Integer.parseInt("88"));
//              }
              String[] arrStart = DateTimeUtils.date2ddMMyyyyHHMMss(item.getStartDate()).split(" ");
              String[] arrEnd = DateTimeUtils.date2ddMMyyyyHHMMss(item.getEndDate()).split(" ");
              cr.setStartDate(arrStart[0].trim());//dd-MM-YY dang chuyen gio
              cr.setEndDate(arrEnd[0].trim());//dd-MM-YY dang chuyen gio
//              cr.setStartDate(DateUtil.dateToStringWithPattern(item.getStartDate(), "dd-MM-YY"));//dd-MM-YY dang chuyen gio
//              cr.setEndDate(DateUtil.dateToStringWithPattern(item.getEndDate(), "dd-MM-YY"));//dd-MM-YY dang chuyen gio
//              cr.setEndDate(DateUtil.date2ddMMyyyyHHMMss(item.getEndDate()));
              cr.setDifficult((item.getCrLevel().intValue() < 2));
              cr.setExecutionTime(Integer.valueOf(item.getExecutionTime()));
              List<String> lstNodeId = new ArrayList<>();
              if (!StringUtils.isStringNullOrEmpty(item.getImpactNodeList())) {
                String[] arrNode = item.getImpactNodeList().split(",");
                for (String s : arrNode) {
                  lstNodeId.add(s.trim());
                }
              }
              cr.setListNodeId(lstNodeId);

              List<String> lstServiceId = new ArrayList<>();
              if (!StringUtils.isStringNullOrEmpty(item.getAffectServiceList())) {
                String[] arrService = item.getAffectServiceList().split(",");
                for (String s : arrService) {
                  lstServiceId.add(s.trim());
                }
              }
              cr.setListServiceId(lstServiceId);
              Long priority =
                  "3".equals(String.valueOf(item.getCrPrioritize())) ? -1L : item.getCrPrioritize();
              cr.setPriority(priority.intValue());
//              cr.setFixedDay("1".equals(item.getIsFixedDay()));
//              if (!StringUtils.isStringNullOrEmpty(item.getForbiddenDate())) {
//                String[] forb = item.getForbiddenDate()
//                    .substring(1, item.getForbiddenDate().length() - 1).split(",");
//                List<Integer> lstForb = new ArrayList<>();
//                for (String f : forb) {
//                  f = f.substring(0, f.indexOf("-"));
//                  lstForb.add(Integer.valueOf(f));
//                }
//                cr.setListForbiddenDate(lstForb);
//              } else {
//                cr.setListForbiddenDate(new ArrayList<>());
//              }
              cr.setListForbiddenDate(new ArrayList<>());
              cr.setEarliestTime(arrStart[1].trim());
              cr.setLatestTime(arrEnd[1].trim());
//              cr.setEarliestTime("23:00:00");
//              cr.setLatestTime("04:59:00");
              listCr.add(cr);
            }
            List<com.orlab.day.input.Employee> listEmployee = new ArrayList<>();
            if (!itemContainerEmpAdd.isEmpty()) {
              List<ScheduleEmployeeDTO> lstEmp = itemContainerEmpAdd;
              Map<String, List<ScheduleEmployeeDTO>> mapFilter = lstEmp.stream()
                  .map(o -> new AbstractMap.SimpleEntry<>(o.getUserName(), o))
                  .collect(Collectors.groupingBy(Map.Entry::getKey,
                      Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

              for (Map.Entry<String, List<ScheduleEmployeeDTO>> entry : mapFilter.entrySet()) {
                String key = entry.getKey();
                List<ScheduleEmployeeDTO> values = entry.getValue();
                com.orlab.day.input.Employee employee = new com.orlab.day.input.Employee();
                employee.setId(key);
                List<String> lstWorkingDay = new ArrayList<>();
                lstWorkingDay.add(DateUtil.date2ddMMyyyyString(dtoAction.getPdfDay()));
                employee.setListWorkingDay(lstWorkingDay);

                List<Boolean> lstExp = new ArrayList<>();
                List<Integer> lstSubdivision = new ArrayList<>();
                for (ScheduleEmployeeDTO emp : values) {
                  lstExp.add("1".equals(String.valueOf(emp.getEmpLevel())));
                  Integer subD = StringUtils.isStringNullOrEmpty(emp.getEmpChildren()) ? null
                      : (!StringUtils.isInteger(emp.getEmpChildren()) ? null
                          : Integer.valueOf(emp.getEmpChildren()));
                  if (subD != null && subD > 0) {//khac null va > 0
                    lstSubdivision.add(subD);
                  }
                }
                employee.setListSubdivision(lstSubdivision);
                employee.setListExp(lstExp);

                listEmployee.add(employee);
              }
            }

            com.orlab.day.input.Input inputDay = new com.orlab.day.input.Input();
            inputDay.setListCr(listCr);
            inputDay.setListEmployee(listEmployee);
            inputDay.setConfig(config);
            inputDay.setConstraint(constraint);
            com.orlab.day.output.Output outputDay = new com.orlab.day.output.Output();
            try {
              /*Resource resource = new ClassPathResource("DayAdapter.dll");
              System.out.println(((ClassPathResource) resource).getURI().getPath());*/
//              NativeUtils.loadLibraryFromJar(((ClassPathResource) resource).getURI().getPath());
              outputDay = OrlabViettelCrDaySolver.getSchedule(inputDay);
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
            System.out.println(outputDay.getListServedCr());

            if (!StringUtils.isStringNullOrEmpty(outputDay.getListServedCr())) {
              for (com.orlab.day.output.ServedCr crAfter : outputDay.getListServedCr()) {
                for (ScheduleCRDTO crBefore : lstScheduleCrBefore) {
                  if (crAfter.getCrId().equals(String.valueOf(crBefore.getIdCr()))) {
                    crBefore.setType(1L);
                    crBefore.setCrPerformer(crAfter.getEmployeeId());
                    log.info(
                        "Ngay sap lich: " + crAfter.getStartTime() + ", CR: " + crAfter.getCrId());
                    String currentDay = DateUtil.date2ddMMyyyyString(dtoAction.getPdfDay());
                    String nextDay = DateUtil
                        .dateToStringWithPattern(DateUtil.addDay(dtoAction.getPdfDay(), 1),
                            "dd/MM/yyyy");
//                                crBefore.setRegistrationDate(crAfter.getStartTime());
                    if (crAfter.getStartTime().startsWith("23")) {
                      crBefore.setRegistrationDate(DateTimeUtils
                          .convertStringToDate(currentDay + " " + crAfter.getStartTime()));
                    } else {
                      crBefore.setRegistrationDate(DateTimeUtils
                          .convertStringToDate(nextDay + " " + crAfter.getStartTime()));
                    }
                    lstCrAfter.add(crBefore);
                  }
                }
              }
            }
            //</editor-fold>
          }
          if (!lstCrAfter.isEmpty()) {
            if (dtoAction.getIsInsert() == 1L) {
              res = insertListCRDTO(lstCrAfter);
            } else {
              ScheduleCRDTO u = new ScheduleCRDTO();
              u.setType(1L);
              u.setIdSchedule(dtoAction.getIdSchedule());
              List<ScheduleCRDTO> lstU = cfgRequestScheduleRepository.getListScheduleCR(u);
              List<ScheduleCRDTO> lstF = new ArrayList<>();
              if (lstU != null && !lstU.isEmpty()) {
                for (ScheduleCRDTO crAt : lstCrAfter) {
                  for (ScheduleCRDTO uCr : lstU) {
                    if (crAt.getIdCr().equals(uCr.getIdCr())) {
                      crAt.setId(uCr.getId());
                      crAt.setIdSchedule(dtoAction.getIdSchedule());
                      crAt.setType(1L);
                      lstF.add(crAt);
                    }
                  }
                }
              } else {
                lstF.addAll(lstCrAfter);
              }
              if (!lstF.isEmpty()) {
                try {
                  res = insertListCRDTO(lstF);
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                  String message = " Error: " + e.getMessage();
                  result.setKey(RESULT.ERROR);
                  result.setMessage(message);
                  return result;
                }
              }
            }
            dto.setStatus(1L);//Da sap lich
          }
          if ("SUCCESS".equals(res)) {
            res = updateRequestSchedule(dto);
            result.setKey(RESULT.SUCCESS);
          }
          //</editor-fold>
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      ResultInSideDto resultInSideDto = new ResultInSideDto();
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(RESULT.ERROR);
      return resultInSideDto;
    }
    result.setObject(dto);
    return result;
  }

  private String insertListCRDTO(List<ScheduleCRDTO> lstScheduleTmp) {
    log.debug("insertListCRDTO : {}", lstScheduleTmp);
    return cfgRequestScheduleRepository.insertListCRDTO(lstScheduleTmp);
  }

  private String insertListDayOffDTO(List<ScheduleEmployeeDTO> lst) {
    log.debug("insertListDayOffDTO : {}", lst);
    return cfgRequestScheduleRepository.insertListDayOffDTO(lst);
  }

  private boolean checkExisted(RequestScheduleDTO dto) {
    log.debug("checkExisted : {}", dto);
    return cfgRequestScheduleRepository.checkExisted(dto);
  }

  private Map<String, ItemDataCRInside> getMapArray() {
    Map<String, ItemDataCRInside> mapArray = new HashMap<>();
    List<ItemDataCRInside> lstCbbArrayTemp = crServiceProxy
        .getListImpactSegmentCBBLocaleProxy(I18n.getLocale());
    if (lstCbbArrayTemp != null && !lstCbbArrayTemp.isEmpty()) {
      for (ItemDataCRInside itemDTO : lstCbbArrayTemp) {
        if (itemDTO.getValueStr() != null) {
          mapArray.put(itemDTO.getValueStr().toString(), itemDTO);
          mapArray.put(itemDTO.getDisplayStr(), itemDTO);
        }
      }
    }
    return mapArray;
  }
}
