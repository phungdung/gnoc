package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.business.RoleUserBusiness;
import com.viettel.gnoc.commons.business.RolesBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RoleUserDTO;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.TransitionStateConfigDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.incident.provider.WSChatPort;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.OdServiceProxy;
import com.viettel.gnoc.commons.proxy.SrServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.TransitionStateConfigRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.PROBLEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemActionLogsDTO;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
import com.viettel.gnoc.pt.dto.ProblemFilesDTO;
import com.viettel.gnoc.pt.dto.ProblemMonitorDTO;
import com.viettel.gnoc.pt.dto.ProblemNodeDTO;
import com.viettel.gnoc.pt.dto.ProblemWorklogDTO;
import com.viettel.gnoc.pt.dto.ProblemsChartDTO;
import com.viettel.gnoc.pt.dto.ProblemsDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemsMobileDTO;
import com.viettel.gnoc.pt.repository.ProblemConfigTimeRepository;
import com.viettel.gnoc.pt.repository.PtProblemsRepository;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;
import vn.viettel.smartoffice.GroupBusiness;
import vn.viettel.smartoffice.GroupResponse;

@Service
@Transactional
@Slf4j
public class ProblemsBusinessImpl implements ProblemsBusiness {

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

  @Value("${application.ftp.folder}")
  private String ftpFolder;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Autowired
  PtProblemsRepository ptProblemsRepository;

  @Autowired
  ProblemNodeBusiness problemNodeBusiness;

  @Autowired
  ProblemActionLogsBusiness problemActionLogsBusiness;

  @Autowired
  ProblemWorklogBusiness problemWorklogBusiness;

  @Autowired
  UnitBusiness unitBusiness;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  MessagesBusiness messagesBusiness;

  @Autowired
  RoleUserBusiness roleUserBusiness;

  @Autowired
  RolesBusiness rolesBusiness;

  @Autowired
  WSChatPort wsChatPort;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  CatLocationBusiness catLocationBusiness;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ProblemConfigTimeRepository problemConfigTimeRepository;

  @Autowired
  OdServiceProxy odServiceProxy;

  @Autowired
  SrServiceProxy srServiceProxy;

  @Autowired
  TransitionStateConfigRepository transitionStateConfigRepository;

  List<UnitDTO> lstUnitAll;
  Map<String, UnitDTO> mapUnit = null;
  Map<Long, String> mapPtRelatedType = new HashMap<>();
  Map<String, String> mapPtState = new HashMap<>();
  Map<String, String> mapPMGroupName = new HashMap<>();

  private final static String PROBLEM_SEQ = "PROBLEM_SEQ";

  @Override
  public List<ProblemsInsideDTO> getListProblemDTO(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to getListProblemDTO : {}", problemsInsideDTO);
    return ptProblemsRepository.getListProblemDTO(problemsInsideDTO);
  }

  @Override
  public List<ProblemsInsideDTO> getListProblemDTOForTT(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to getListProblemDTOForTT : {}", problemsInsideDTO);
    return ptProblemsRepository.getListProblemDTO(problemsInsideDTO);
  }

  @Override
  public List<ProblemsInsideDTO> getListProblemsDTO(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to getListProblemsDTO : {}", problemsInsideDTO);
    return ptProblemsRepository.getListProblemsDTO(problemsInsideDTO);
  }

  @Override
  public Datatable getListProblemsSearch(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to getListProblemsDTO : {}", problemsInsideDTO);
    UserToken userToken = ticketProvider.getUserToken();
    List<RolesDTO> rolesDTOS = getListRolePmByUser(userToken.getUserID());
    List<String> roleId = new ArrayList<>();
    if (rolesDTOS != null && rolesDTOS.size() > 0) {
      rolesDTOS.forEach(r -> {
        roleId.add(r.getRoleId());
      });
    }
    problemsInsideDTO.setLstPmGroup(roleId);
    Datatable datatable = ptProblemsRepository.getListProblemsSearch(problemsInsideDTO);
    List<ProblemsInsideDTO> lst = searchProblemTicket(
        (List<ProblemsInsideDTO>) datatable.getData());
    datatable.setData(lst);
    return datatable;
  }

  @Override
  public List<ProblemsInsideDTO> getListProblemSearch(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to getListProblemSearch : {}", problemsInsideDTO);
    return ptProblemsRepository.getListProblemSearch(problemsInsideDTO);
  }

  @Override
  public File getListProblemsSearchExport(ProblemsInsideDTO problemsInsideDTO) throws Exception {
    log.debug("Request to getListProblemsSearchExport : {}", problemsInsideDTO);
    String[] header = new String[]{"isOutOfDate", "statusStr", "problemCode", "problemName",
        /* "description", */
        "typeIdStr", "categorizationStr", "impactIdStr", "priorityStr",
        "vendor", "subCategoryIdStr", "affectedNode", "affectedService", "location",
        "createdTime", "assignTimeTemp", "esRcaTime", "esWaTime", "esSlTime",
        "createUserName", "createUnitName", "createUserPhone",
        "receiveUserIdStr", "receiveUnitIdStr", "insertSource", "relatedTt", "relatedPt",
        "relatedKedb",
        "closedTime", "rca", "wa", "solution", "worklog", "influenceScope",
        "solutionTypeName", "rcaTypeStr", "ptRelatedTypeStr", "rcaFoundTime", "waFoundTime",
        "slFoundTime",
        "timeUsed", "deferredTimex", "lastUpdateTime", "notes", "pmUserName", "pmGroupName",
        "softwareVersion",
        "hardwareVersion", "reasonOverdue"};
    UserToken userToken = ticketProvider.getUserToken();
    List<RolesDTO> rolesDTOS = getListRolePmByUser(userToken.getUserID());
    List<String> roleId = new ArrayList<>();
    if (rolesDTOS != null && rolesDTOS.size() > 0) {
      rolesDTOS.forEach(r -> {
        roleId.add(r.getRoleId());
      });
    }
    problemsInsideDTO.setLstPmGroup(roleId);
    List<ProblemsInsideDTO> lst = ptProblemsRepository.getListProblemsSearchExport(
        problemsInsideDTO);
    lst = searchProblemTicket(lst);
    return exportFileEx(lst, header, problemsInsideDTO.getFromDate(),
        problemsInsideDTO.getToDate());
  }

  @Override
  public int getListProblemSearchCount(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to getListProblemSearchCount : {}", problemsInsideDTO);
    return ptProblemsRepository.getListProblemSearchCount(problemsInsideDTO);
  }

  @Override
  public List<String> getSequenseProblems(String seqName, int... size) {
    log.debug("Request to getListProblemSearchCount : {}", seqName);
    return ptProblemsRepository.getSequenseProblems(PROBLEM_SEQ, size);
  }

  @Override
  public ResultInSideDto add(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to add : {}", problemsInsideDTO);
    Long id = Long.valueOf(ptProblemsRepository.getSeqTableProblems(PROBLEM_SEQ));
    problemsInsideDTO.setProblemId(id);
    return ptProblemsRepository.add(problemsInsideDTO);
  }

  @Override
  public ResultInSideDto edit(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to edit : {}", problemsInsideDTO);
    return ptProblemsRepository.edit(problemsInsideDTO);
  }

  @Override
  public ResultInSideDto delete(Long id) {
    log.debug("Request to edit : {}", id);
    return ptProblemsRepository.delete(id);
  }

  @Override
  public ResultInSideDto deleteListProblems(List<ProblemsInsideDTO> problemsListDTO) {
    log.debug("Request to edit : {}", problemsListDTO);
    return ptProblemsRepository.deleteListProblems(problemsListDTO);
  }

  @Override
  public ProblemsInsideDTO findProblemsById(Long id) {
    log.debug("Request to findProblemsById : {}", id);
    ProblemsInsideDTO problemsInsideDTO = ptProblemsRepository.findProblemsById(id);
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.PROBLEMS);
    gnocFileDto.setBusinessId(id);
    problemsInsideDTO.setGnocFileDtos(gnocFileRepository.getListGnocFileByDto(gnocFileDto));
    Double offset = TimezoneContextHolder.getOffsetDouble();
    try {
      setTimeZone(problemsInsideDTO, offset);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return problemsInsideDTO;
  }

  @Override
  public List<ProblemsChartDTO> getProblemsChartData(String receiveUnitId) {
    log.debug("Request to getProblemsChartData : {}", receiveUnitId);
    return ptProblemsRepository.getProblemsChartData(receiveUnitId);
  }

  @Override
  public List<ProblemMonitorDTO> getProblemsMonitor(String priorityId, String unitId,
      String fromDate, String toDate, String findInSubUnit, String unitType) {
    log.debug("Request to getProblemsMonitor : {}", priorityId);
    return ptProblemsRepository.getProblemsMonitor(priorityId, unitId,
        fromDate, toDate, findInSubUnit, unitType);
  }

  @Override
  public List<ProblemsMobileDTO> getProblemsMobileUnitAll(String receiveUnitId) {
    log.debug("Request to getProblemsMobileUnitAll : {}", receiveUnitId);
    return ptProblemsRepository.getProblemsMobileUnitAll(receiveUnitId);
  }

  @Override
  public List<ProblemsInsideDTO> getListProblemsByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    log.debug("Request to getListProblemsByCondition : {}", lstCondition);
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    return ptProblemsRepository
        .getListProblemsByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<ProblemsMobileDTO> getProblemsMobileUnit(String receiveUnitId) {
    log.debug("Request to getProblemsMobileUnit : {}", receiveUnitId);
    return ptProblemsRepository.getProblemsMobileUnit(receiveUnitId);
  }

  @Override
  public Datatable getListProblemSearchDulidate(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to getListProblemSearchDulidate : {}", problemsInsideDTO);
    Datatable datatable = new Datatable();
    UserTokenGNOCSimple userTokenGNOC = problemsInsideDTO.getUserTokenGNOC();
    if (problemsInsideDTO.getProblemName() == null) {
      return datatable;
    }
    int size = problemsInsideDTO.getPageSize();
    Long tableMaxSize = ptProblemsRepository.getMaxRowDuplicate("NUM_DUPLICATE");
    if (tableMaxSize <= 0) {
      tableMaxSize = 30l;
    }
    if (userTokenGNOC != null && StringUtils.isNotNullOrEmpty(userTokenGNOC.getFullName())) {
      problemsInsideDTO.setProblemState(userTokenGNOC.getFullName());
    }
    datatable = getListProblemsSearch(problemsInsideDTO);
    List<ProblemsInsideDTO> datas = (List<ProblemsInsideDTO>) datatable.getData();
    if (datas == null || datas.isEmpty()) {
      //tiennv them bo cac ki tu dac biet
      String regex = "([\\\\\\.\\[\\{\\(\\*\\+\\?\\^\\$\\|\\)\\]\\}#%&<>/?,'\";\\!@\\-=:`~])";//+([ ]|$)"; //xoa het
      String problemName = problemsInsideDTO.getProblemName();
      if (problemName != null) {
        problemName = problemName.replaceAll(regex, " ");
        problemsInsideDTO.setProblemName(problemName);
      }
      List<String> lstIn = splitString(problemsInsideDTO.getProblemName());
      List<ProblemsInsideDTO> lstNotIn = new ArrayList<>();

      if (userTokenGNOC != null && StringUtils.isNotNullOrEmpty(userTokenGNOC.getUnitName())) {
        ProblemsInsideDTO pdto = new ProblemsInsideDTO();
        pdto.setProblemCode(userTokenGNOC.getUnitName());
        lstNotIn.add(pdto);
      }
      if (lstIn != null && !lstIn.isEmpty()) {
        datatable = ptProblemsRepository
            .getListProblemSearchDulidates(problemsInsideDTO.getFromDate(),
                problemsInsideDTO.getToDate(),
                userTokenGNOC, lstIn, lstNotIn, 1, tableMaxSize.intValue(),
                problemsInsideDTO.getSortName(), problemsInsideDTO.getSortType());
        datas = (List<ProblemsInsideDTO>) datatable.getData();
      }
    }

    if (datas != null && datas.size() > 0) {
      int totalSize = datas.size();
      datatable.setTotal(totalSize);
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      datatable.setPages(pageSize);
      datas = (List<ProblemsInsideDTO>) DataUtil
          .subPageList(datas, problemsInsideDTO.getPage(), problemsInsideDTO.getPageSize());
      if (datas != null && datas.size() > 0) {
        datas.get(0).setTotalRow(totalSize);
      }
      datatable.setData(datas);
    }

    return datatable;
  }

  @Override
  public ResultInSideDto insertProblems(List<MultipartFile> files,
      ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to insertProblems : {}", problemsInsideDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    try {
      setTimeZone(problemsInsideDTO, -1L * offset);
      if (problemsInsideDTO != null && problemsInsideDTO.getStateCode().equals(PROBLEM.OPEN)) {
        String content = "ptMngt.editpt.createdPT1";
        messagesBusiness
            .insertSMSMessageForPm(content, Long.toString(problemsInsideDTO.getPmGroup()),
                problemsInsideDTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    try {
      if (problemsInsideDTO != null) {
        return insertProblemsCommon(files, problemsInsideDTO.toModelOutSide(), true);
      }
    } catch (Exception e) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
    }
    return resultInSideDto;
  }


  @Override
  public ResultInSideDto insertProblemsCommon(List<MultipartFile> files,
      ProblemsDTO problemsDTO, boolean isInside) {
    log.debug("Request to insertProblemsCommon : {}", problemsDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    String result;
    try {
      String ret = "";
      if (!"PT".equals(problemsDTO.getInsertSource())) {
        ret = validateInsertPt(problemsDTO);
      }
      if (!"".equals(ret)) {
        resultInSideDto.setKey(Constants.RESULT.FAIL);
        resultInSideDto.setMessage(ret);
        return resultInSideDto;
      }

      Long id = Long.valueOf(ptProblemsRepository.getSeqTableProblems(PROBLEM_SEQ));
      problemsDTO.setProblemId(String.valueOf(id));
      String itemCode = problemsDTO.getItemTypeCode();
      Long createUnitId = StringUtils.isStringNullOrEmpty(problemsDTO.getCreateUnitId()) ? null
          : Long.valueOf(problemsDTO.getCreateUnitId());
      String createUnitName = problemsDTO.getCreateUnitName();
      Long createUserId = StringUtils.isStringNullOrEmpty(problemsDTO.getCreateUserId()) ? null
          : Long.valueOf(problemsDTO.getCreateUserId());
      String createUserName = problemsDTO.getCreateUserName();
      String createUserPhone = problemsDTO.getCreateUserPhone();
      if (isInside) {
        UserToken userToken = ticketProvider.getUserToken();
        UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
        createUserId = userToken.getUserID();
        createUserName = userToken.getUserName();
        createUserPhone = userToken.getTelephone();
        createUnitName = unitToken.getUnitName();
        createUnitId = userToken.getDeptId();
      }
      String ptCode =
          "PT_" + itemCode + "_" + problemsDTO.getSubCategoryIdStr() + "_" + DateTimeUtils
              .convertDateToString(new Date(), "yyMMdd") + "_" + id;
      problemsDTO.setProblemCode(ptCode);
      String worklog = problemsDTO.getWorklog();
      if (worklog == null || "".equals(worklog)) {
        worklog = "";
      } else {
        problemsDTO.setWorklog(
            createUserName + " " + DateUtil.date2ddMMyyyyHHMMss(new Date()) + ": " + worklog);
      }
      problemsDTO.setCreateUnitId(String.valueOf(createUnitId));
      problemsDTO.setCreateUnitName(createUnitName);
      problemsDTO.setCreateUserId(String.valueOf(createUserId));
      problemsDTO.setCreateUserName(createUserName);
      problemsDTO.setCreateUserPhone(createUserPhone);
      resultInSideDto = ptProblemsRepository.add(problemsDTO.toModelInSide());
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        resultInSideDto.setKey(RESULT.ERROR);
        return resultInSideDto;
      }
      List<String> lstNodeCode = DataUtil.splitListFileByComma(problemsDTO.getAffectedNode());
      List<String> lstNodeName = DataUtil.splitListFileByComma(problemsDTO.getNodeName());
      List<String> lstNodeIp = DataUtil.splitListFileByComma(problemsDTO.getNodeIp());
      List<ProblemNodeDTO> lstProblemNodeDTOs = new ArrayList();
      Map<String, String> mapDeviceNation = new HashMap<>();
      try {
        List<InfraDeviceDTO> infraDeviceDTOS = getInfraDeviceDTOSByListCode(lstNodeCode);
        if (infraDeviceDTOS != null && infraDeviceDTOS.size() > 0) {
          infraDeviceDTOS.forEach(i -> {
            mapDeviceNation.put(i.getDeviceCode(), i.getNationCode());
          });
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      if (!lstNodeCode.isEmpty()) {
        for (int i = 0; i < lstNodeCode.size(); i++) {
          ProblemNodeDTO problemNodeDTO = new ProblemNodeDTO(null, null, lstNodeCode.get(i),
              (id), "", lstNodeName.get(i), lstNodeIp.get(i),
              mapDeviceNation.get(lstNodeCode.get(i)));
          lstProblemNodeDTOs.add(problemNodeDTO);
        }
      }
      result = problemNodeBusiness.insertOrUpdateListProblemNode(lstProblemNodeDTOs);
      if (!RESULT.SUCCESS.equals(result)) {
        resultInSideDto.setKey(RESULT.ERROR);
        return resultInSideDto;
      }
      ProblemActionLogsDTO problemActionLogsDTO = new ProblemActionLogsDTO(null,
          problemsDTO.getContent(), new Date(),
          createUnitId, createUserId,
          problemsDTO.getStateName(),
          id, createUnitName, createUserName, null);
      problemActionLogsBusiness.insertProblemActionLogs(problemActionLogsDTO);
      if (!"".equals(worklog)) {
        ProblemWorklogDTO problemWorklogDTO = new ProblemWorklogDTO(null, createUserId,
            createUserName, createUnitId, createUnitName, worklog, problemsDTO.getStateName(),
            (new Date()), Long.valueOf(problemsDTO.getProblemId()));
        problemWorklogBusiness.onInsert(problemWorklogDTO);
      }
      resultInSideDto.setMessage(ptCode);
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      if (files != null && files.size() > 0) {
        for (MultipartFile multipartFile : files) {
          Date date = new Date();
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                  multipartFile.getBytes(), date);
          String fileName = multipartFile.getOriginalFilename();
          //Start save file old
          String fullPathOld = FileUtils
              .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  uploadFolder, date);
          ProblemFilesDTO problemFilesDTO = new ProblemFilesDTO();
          problemFilesDTO.setProblemFileName(FileUtils.getFileName(fullPathOld));
          problemFilesDTO
              .setCreateUnitId(DataUtil.isNullOrEmpty(problemsDTO.getCreateUnitId()) ? null
                  : Long.valueOf(problemsDTO.getCreateUnitId()));
          problemFilesDTO.setCreateUnitName(problemsDTO.getCreateUnitName());
          problemFilesDTO
              .setCreateUserId(DataUtil.isNullOrEmpty(problemsDTO.getCreateUserId()) ? null
                  : Long.valueOf(problemsDTO.getCreateUserId()));
          problemFilesDTO.setCreateUserName(problemsDTO.getCreateUserName());
          problemFilesDTO.setCreateTime(date);
          problemFilesDTO.setProblemId(Long.valueOf(problemsDTO.getProblemId()));
          ResultInSideDto resultFileDataOld = ptProblemsRepository
              .insertProblemFiles(problemFilesDTO);
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(fileName);
          gnocFileDto.setCreateUnitId(createUnitId);
          gnocFileDto.setCreateUnitName(createUnitName);
          gnocFileDto.setCreateUserId(createUserId);
          gnocFileDto.setCreateUserName(createUserName);
          gnocFileDto.setCreateTime(problemFilesDTO.getCreateTime());
          gnocFileDto.setMappingId(resultFileDataOld.getId());
          gnocFileDtos.add(gnocFileDto);
        }
        gnocFileRepository
            .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.PROBLEMS,
                Long.valueOf(problemsDTO.getProblemId()),
                gnocFileDtos);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
    return resultInSideDto;
  }

  private String validateInsertPt(ProblemsDTO dialog) {
    String result = "";
    try {
      Date now = new Date();
      if (dialog.getInsertSource().contains("NOC")) {

        dialog.setImpactId("-1");
        dialog.setAssignTimeTemp(DateUtil.date2ddMMyyyyHHMMss(now));
        dialog.setCreatedTime(DateUtil.date2ddMMyyyyHHMMss(now));
        dialog.setLastUpdateTime(DateUtil.date2ddMMyyyyHHMMss(now));
        dialog.setContent(I18n.getLanguage("common.button.addProblem"));

        if (DataUtil.isNullOrEmpty(dialog.getProblemName())) {
          return "problem.problemNameNull";
        } else if (dialog.getProblemName().trim().length() > 500) {
          return "problem.problemName500";
        }

        if (dialog.getTypeId() == null) {
          return "typeId is not null";
        } else {
          CatItemDTO typeDTO = new CatItemDTO();
          typeDTO.setItemCode(dialog.getTypeId());
          List<CatItemDTO> lstType = catItemBusiness.getListCatItemDTO(typeDTO);
          if (lstType == null || lstType.isEmpty()) {

            return "problem.typeNotExist";
          } else {
            if (!lstType.get(0).getCategoryCode().equals(Constants.TROUBLE.PT_TYPE)) {
              return "problem.notIstype";
            }
            dialog.setTypeId(String.valueOf(lstType.get(0).getItemId()));
            dialog.setTypeIdStr(lstType.get(0).getItemCode());
            dialog.setItemTypeCode(lstType.get(0).getItemCode());
          }
        }

        if (DataUtil.isNullOrEmpty(dialog.getPriorityId())) {
          return "priorityId is not null";
        } else {
          CatItemDTO prio = new CatItemDTO();
          prio.setItemCode(dialog.getPriorityId().trim().toLowerCase());
          List<CatItemDTO> lstCat = catItemBusiness.getListCatItemDTO(prio);
          if (lstCat == null || lstCat.isEmpty()
              || (lstCat != null && !lstCat.get(0).getCategoryCode()
              .equals(Constants.PROBLEM.PRIORITY))) {
            return I18n.getLanguage("incident.priorityNotExist");
          } else {
            dialog.setPriorityId(String.valueOf(lstCat.get(0).getItemId()));
            dialog.setPriorityStr(lstCat.get(0).getItemName());
          }
        }

        if (DataUtil.isNullOrEmpty(dialog.getProblemState())) {
          return "problemState is not null";
        } else {

          CatItemDTO catItem = new CatItemDTO();
          catItem.setItemCode(Constants.PROBLEM.OPEN);
          List<CatItemDTO> lst = catItemBusiness.getListCatItemDTO(catItem);
          if (lst == null || lst.isEmpty()) {
            return "problemState is not exist";
          } else {
            dialog.setProblemState(String.valueOf(lst.get(0).getItemId()));
            dialog.setStateCode(lst.get(0).getItemCode());
            dialog.setStateName(lst.get(0).getItemName());
          }
        }
        if (DataUtil.isNullOrEmpty(dialog.getPmGroup())) {
          return "pmGroup is not null";
        } else {

          RolesDTO rolesDTO = new RolesDTO();
          rolesDTO.setRoleCode(dialog.getPmGroup());
          List<RolesDTO> lstRole = rolesBusiness.getListRolesDTO(rolesDTO, 0, 1, "asc", "roleCode");
          if (lstRole != null && !lstRole.isEmpty()) {
            for (int i = lstRole.size() - 1; i >= 0; i--) {
              rolesDTO = lstRole.get(0);
              if (!rolesDTO.getRoleCode().contains("PM_")) {
                return "pmGroup is not exist";
              }
            }
            dialog.setPmGroup(lstRole.get(0).getRoleId());
          }
        }
        if (DataUtil.isNullOrEmpty(dialog.getSubCategoryId())) {
          return "subCategoryId is not null";
        } else {
          CatItemDTO typeDTO = new CatItemDTO();
          typeDTO.setItemCode(dialog.getSubCategoryId());
          List<CatItemDTO> lstType = catItemBusiness.getListCatItemDTO(typeDTO);
          if (lstType == null || lstType.isEmpty()) {
            return "problem.notIsSubCate";
          } else {
            CatItemDTO subCate = lstType.get(0);
            if (subCate.getParentItemId() == null || !String.valueOf(subCate.getParentItemId())
                .equals(dialog.getTypeId())) {
              return "problem.notIsSubCate";
            }
            dialog.setSubCategoryId(String.valueOf(subCate.getItemId()));
            dialog.setSubCategoryIdStr(subCate.getItemCode());
          }
        }

        if (DataUtil.isNullOrEmpty(dialog.getVendor())) {
          return "vendor is not null";
        } else {
          CatItemDTO vendorDTO = new CatItemDTO();
          vendorDTO.setItemCode(dialog.getVendor());
          List<CatItemDTO> lstVendor = catItemBusiness.getListCatItemDTO(vendorDTO);
          if (lstVendor != null && !lstVendor.isEmpty()) {
            dialog.setVendor(lstVendor.get(0).getItemCode());
          } else {
            return "problem.vendorNotExist";
          }
        }
        if (DataUtil.isNullOrEmpty(dialog.getCreateUserName())) {
          return "createUserName is not null";
        } else if (!DataUtil.isNullOrEmpty(dialog.getCreateUserName())) {
          try {
            UsersInsideDto u = userBusiness
                .getUserDTOByUserNameInnerJoint(dialog.getCreateUserName().trim());
            if (u == null) {
              return "problem.createUserNotExist";
            } else {
              dialog.setCreateUserId(u.getUserId().toString());
              dialog.setCreateUserName(u.getUsername());
              dialog.setCreateUserPhone(u.getMobile());
              dialog.setCreateUnitId(u.getUnitId().toString());
              dialog.setCreateUnitName(u.getUnitName());
            }
          } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "problem.createUserInvalid";
          }
        }
        if (DataUtil.isNullOrEmpty(dialog.getCreateUnitId())) {
          return "createUnitId is not null";
        }

        if (DataUtil.isNullOrEmpty(dialog.getReceiveUnitId())) {
          return "receiveUnitId is not null";
        } else {
          UnitDTO unitDTO = new UnitDTO();
          unitDTO.setUnitCode(dialog.getReceiveUnitId());

          List<UnitDTO> u = unitRepository.getUnitByUnitDTO(unitDTO);
          if (u == null || u.isEmpty()) {
            return "problem.receiveUnitNotExist";
          }
          //Set lai gia tri ReceiveUnitId = gia tri unitId
          dialog.setReceiveUnitId(u.get(0).getUnitId().toString());

        }

        if (DataUtil.isNullOrEmpty(dialog.getLocationId())) {
          return "locationId is not null";
        } else {
          try {
            CatLocationDTO location = catLocationBusiness
                .getLocationByCode(dialog.getLocationId(), null, null);
            if (location == null) {
              return "problem.locationCodeNotExist";
            }

            dialog.setLocationId(location.getLocationId());
            dialog.setLocation(location.getLocationNameFull());

          } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "problem.locationInvalid";
          }
        }

        if (DataUtil.isNullOrEmpty(dialog.getCategorization())) {
          return "categorization is not null";
        } else {
          CatItemDTO catItemDTO = new CatItemDTO();
          catItemDTO.setItemCode(dialog.getCategorization());
          List<CatItemDTO> lstCate = catItemBusiness.getListCatItemDTO(catItemDTO);
          if (lstCate != null && !lstCate.isEmpty()) {
            dialog.setCategorization(String.valueOf(lstCate.get(0).getItemId()));
            dialog.setCategorizationStr(lstCate.get(0).getItemCode());
          } else {
            return "problem.categorizationNotExist";
          }
        }

        if (DataUtil.isNullOrEmpty(dialog.getDescription())) {
          return "description is not null";
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    return result;

  }

  @Override
  public ResultInSideDto insertOrUpdateListProblems(List<ProblemsInsideDTO> problemsInsideDTOList) {
    log.debug("Request to insertOrUpdateListProblems : {}", problemsInsideDTOList);
    return ptProblemsRepository.insertOrUpdateListProblems(problemsInsideDTOList);
  }

  @Override
  public ResultInSideDto updateProblems(ProblemsInsideDTO problemsInsideDTO) throws Exception {
    log.debug("Request to updateProblems : {}", problemsInsideDTO);
    ResultInSideDto resultInSideDto;
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitBusiness.findUnitById(userToken.getDeptId());
    Double offset = TimezoneContextHolder.getOffsetDouble();
    try {
      setTimeZone(problemsInsideDTO, -1L * offset);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    ResultInSideDto result = checkInfoUserPM();
    List<String> list = (List<String>) result.getObject();
    ProblemsInsideDTO problemsInsideDTOOld = problemsInsideDTO.getProblemsInsideDTOOld();
    // kiem tra nguoi xu ly la bo
    List<RolesDTO> lstRoleUser = new ArrayList<>();

//    try {
    String createUnitId = String.valueOf(userToken.getDeptId());
    String createUnitName = unitToken.getUnitName();
    String createUserId = String.valueOf(userToken.getUserID());
    String createUserName = userToken.getUserName();

    //fix for kedb
    if ("YES".equals(problemsInsideDTO.getInsertKedb())) {
      createUnitId = problemsInsideDTO.getUnitUpdateId();
      createUnitName = problemsInsideDTO.getUnitUpdateName();
      createUserId = problemsInsideDTO.getUserUpdateId();
      createUserName = problemsInsideDTO.getUserUpdateName();
    }

    String contentWorkLog = problemsInsideDTO.compareContent(problemsInsideDTOOld);
    if (contentWorkLog != null && contentWorkLog.length() > 2000) {
      contentWorkLog = contentWorkLog.substring(0, 1999);
    }
    if ((PROBLEM.OPEN.equals(problemsInsideDTOOld.getStateCode()) || PROBLEM.OPEN_2
        .equals(problemsInsideDTOOld.getStateCode())) &&
        PROBLEM.UNASSIGN.equals(problemsInsideDTO.getStateCode())) {
      problemsInsideDTO.setPmId(userToken.getUserID());
      problemsInsideDTO.setPmUserName(userToken.getUserName());
    }

    //cap nhat lai cac truong rcaFoundTIme, Temp...
    boolean isChangeStatus = (StringUtils.isNotNullOrEmpty(problemsInsideDTO.getStateCode())
        && StringUtils
        .isNotNullOrEmpty(problemsInsideDTOOld.getStateCode())
        && !problemsInsideDTO.getStateCode().equals(problemsInsideDTOOld.getStateCode()));
    String content = "";
    String smsFor = "";
    String stateCode = problemsInsideDTO.getStateCode();
    if (problemsInsideDTO.getReceiveUserId() != null) {
      lstRoleUser = getListRolePmByUser(problemsInsideDTO.getReceiveUserId());
      content = "ptMngt.editpt.created";
      smsFor = "CREATE";
    }
    if (isChangeStatus) {
      if (stateCode.equals(PROBLEM.PT_REJECTED) && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.OPEN)) {
        content = "ptMngt.editpt.openToReject";
//          List<RolesDTO> lstRole = getListRolePmByUser(problemsInsideDTO.getCreateUserId());
        List<RolesDTO> lstRole = getListRolePmByUser(problemsInsideDTO.getCreateUserId());
        int temp = 0;
        if (lstRole != null && !lstRole.isEmpty()) {//pm
          for (RolesDTO lDTO : lstRole) {
            if (lDTO.getRoleCode().contains("PM_")) {
              smsFor = "CREATE_" + lDTO.getRoleId();
              temp = 1;
              break;
            }
          }
        }
        if (temp == 0) {
          smsFor = "CREATE";
        }
      }

      //xxx -> mo
      if (stateCode.equals(PROBLEM.OPEN_2)) {
        content = "ptMngt.editpt.created";
        smsFor = "CREATE";
      }

      //du thao --> mo
      if (stateCode.equals(PROBLEM.OPEN_2) && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.OPEN)) {
        content = "ptMngt.editpt.created";
        smsFor = "CREATE";
      }
      // xxx => du thao
      if (stateCode.equals(PROBLEM.OPEN) && problemsInsideDTOOld.getStateCode() != null
          && !problemsInsideDTOOld.getStateCode().equals(stateCode)) {
        content = "ptMngt.editpt.createdPT";
        smsFor = "PM";

      }

      // tu choi => du thao
      if (stateCode.equals(PROBLEM.OPEN) && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.PT_REJECTED)) {
        content = "ptMngt.editpt.createdPT";
        smsFor = "PM";
      }

      // Du thao --> cho tiep nhan
      if (stateCode.equals(PROBLEM.UNASSIGN)
          && problemsInsideDTOOld.getStateCode() != null
          && (problemsInsideDTOOld.getStateCode().equals(PROBLEM.OPEN)
          || problemsInsideDTOOld.getStateCode().equals(PROBLEM.OPEN_2))) {
        problemsInsideDTO.setAssignTimeTemp(new Date());
        problemsInsideDTO.setAssignedTime(new Date());
        content = "ptMngt.editpt.process";
        smsFor = "PROCESS";
      }

      // Tam dung --> cho tiep nhan
      if (stateCode.equals(PROBLEM.UNASSIGN)
          && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.DEFERRED)) {
        content = "ptMngt.editpt.DeferreToUnassign";
        smsFor = "PROCESS";
      }

      // Yeu cau tam dong --> cho tiep nhan
      if (stateCode.equals(PROBLEM.UNASSIGN)
          && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.REQ_DEFERRED)) {
        content = "ptMngt.editpt.noDeferre";
        smsFor = "PROCESS";
      }

      //tim nguyen nhan goc
      if (stateCode.equals(PROBLEM.QUEUE)) {
        content = "ptMngt.editpt.receive";
        smsFor = "PM";
      }

      //   cho tiep nhan --> tim nguyen nhan goc
      if (stateCode.equals(PROBLEM.QUEUE) && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.UNASSIGN)) {
        problemsInsideDTO.setAssignTimeTemp(new Date());
        content = "ptMngt.editpt.receive";
        smsFor = "PM";
      }
      //tam dong --> tim nguyen nhan goc
      if (stateCode.equals(PROBLEM.QUEUE) && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.DEFERRED)) {
        problemsInsideDTO.setAssignTimeTemp(new Date());
        content = "ptMngt.editpt.openDeferre";
        smsFor = "PM";
      }
      //de xuat nguyen nhan goc --> tim nguyen nhan goc
      if (stateCode.equals(PROBLEM.QUEUE) && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.ROOT_CAUSE_PROPOSAL)) {
        content = "ptMngt.editpt.rejectRootcause";
        smsFor = "PROCESS";
      }

      //yeu cau tam dung --> tim nguyen nhan goc
      if (stateCode.equals(PROBLEM.QUEUE) && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.REQ_DEFERRED)) {
        content = "ptMngt.editpt.openDeferre";
        smsFor = "PROCESS";
      }

      //Yeu cau tam dong
      if (stateCode.equals(PROBLEM.REQ_DEFERRED)) {
        content = "ptMngt.editpt.requestDeferre";
        smsFor = "PM";
      }

      //tim nguyen nhan goc -> Yeu cau tam dong
      if (stateCode.equals(PROBLEM.REQ_DEFERRED) && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.QUEUE)) {
        content = "ptMngt.editpt.requestDeferre";
        smsFor = "PM";
      }

      //Dong y tam dong
      if (stateCode.equals(PROBLEM.DEFERRED)) {
        //set lai thoi gian tam dong
        content = "ptMngt.editpt.yesDeferre";
        smsFor = "PROCESS";
      }

      //Chuyen Diagnose da co nguyen nhan goc
      if (stateCode.equals(PROBLEM.DIAGNOSED)) {
        //cap nhat lai kedb
        if (!stateCode.equals(problemsInsideDTOOld.getStateCode())) {
          problemsInsideDTO.setRcaFoundTime(new Date());
        }
        content = "ptMngt.editpt.yesdiagnosed";
        smsFor = "PROCESS";
      }
      //de xuat gp tam thoi --> da co nguyen nhan goc
      if (stateCode.equals(PROBLEM.DIAGNOSED) && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.WORKARROUND_PROPOSAL)) {
        content = "ptMngt.editpt.rejectDiagnose";
        smsFor = "PROCESS";
      }

      //da co giai phap tam thoi --> da co nguyen nhan goc
      if (stateCode.equals(PROBLEM.DIAGNOSED) && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.WORKAROUND_FOUND)) {
        problemsInsideDTO.setAssignTimeTemp(new Date());
        content = "ptMngt.editpt.rejectDiagnose1";
        smsFor = "PROCESS";
      }

      //da co gp triet de --> da co nguyen nhan goc
      if (stateCode.equals(PROBLEM.DIAGNOSED) && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.SOLUTION_FOUND)) {
        content = "ptMngt.editpt.rejectWorkAroundFound1";
        smsFor = "PROCESS";
      }

      //Chuyen WA found da co gp tam thoi
      if (stateCode.equals(PROBLEM.WORKAROUND_FOUND)) {
        if (!stateCode.equals(problemsInsideDTOOld.getStateCode())) {
          problemsInsideDTO.setWaFoundTime(new Date());
        }
        content = "ptMngt.editpt.yeswaFound";
        smsFor = "PROCESS";
      }
      //da trien khai gp tam thoi
      if (stateCode.equals(PROBLEM.WORKAROUND_IMPL)) {
        content = "ptMngt.editpt.applyWaFound";
        smsFor = "PROCESS";

      }
      //de xuat gp triet de  --> da trien khai gp tam thoi
      if (stateCode.equals(PROBLEM.WORKAROUND_IMPL)
          && problemsInsideDTOOld.getStateCode() != null
          && problemsInsideDTOOld.getStateCode().equals(PROBLEM.SOLUTION_PROPOSAL)) {
        content = "ptMngt.editpt.rejectWorkAroundFound";
        smsFor = "PROCESS";
      }

      // de xuat gp triet de
      if (stateCode.equals(PROBLEM.SOLUTION_PROPOSAL)) {
        content = "ptMngt.editpt.solution_proposal";
        smsFor = "PM";
      }

      //da co gp triet de --> da trien khai gp tam thoi, de xuat gp triet de
      if ((stateCode.equals(PROBLEM.WORKAROUND_IMPL) || stateCode
          .equals(PROBLEM.SOLUTION_PROPOSAL))
          && problemsInsideDTOOld.getStateCode() != null && problemsInsideDTOOld.getStateCode()
          .equals(PROBLEM.SOLUTION_FOUND)) {
        content = "ptMngt.editpt.rejectWorkAroundFound1";
        smsFor = "PROCESS";
      }

      //da co gp triet de
      if (stateCode.equals(PROBLEM.SOLUTION_FOUND)) {
        if (!stateCode.equals(problemsInsideDTOOld.getStateCode())) {
          problemsInsideDTO.setSlFoundTime(new Date());
        }
        content = "ptMngt.editpt.yesSolutionFound";
        smsFor = "PROCESS";
      }

      //Da co gp triet de va duoc phe duyet
      if (stateCode.equals(PROBLEM.SOLUTION_IMPL)) {
        content = "ptMngt.editpt.applySolutionFound";
        smsFor = "PROCESS";
      }
      //De xuat nguyen nhan goc
      if (stateCode.equals(PROBLEM.ROOT_CAUSE_PROPOSAL)) {
        content = "ptMngt.editpt.root_cause_proposal";
        smsFor = "PM";
      }

      //De xuat gp tam thoi
      if (stateCode.equals(PROBLEM.WORKARROUND_PROPOSAL)) {
        content = "ptMngt.editpt.workarround_proposal";
        smsFor = "PM";
      }

      //Update trang thai tu x -> cho tiep nhan (nang cap PT)
      if (stateCode.equals(PROBLEM.UNASSIGN)) {
        problemsInsideDTO.setPmId(userToken.getUserID());
        problemsInsideDTO.setPmUserName(userToken.getUserName());
      }

      // skip status
      TransitionStateConfigDTO transitionStateConfigDTO = new TransitionStateConfigDTO();
      transitionStateConfigDTO.setBeginStateCode(problemsInsideDTOOld.getStateCode());
      transitionStateConfigDTO.setEndStateCode(stateCode);
      List<TransitionStateConfigDTO> transitionStateConfigDTOList = ptProblemsRepository
          .getSkipStatusPT(transitionStateConfigDTO);
      if (transitionStateConfigDTOList.size() > 0 && !StringUtils
          .isStringNullOrEmpty(transitionStateConfigDTOList.get(0).getSkipStatus())) {
        problemsInsideDTO.setSkipStatus(transitionStateConfigDTOList.get(0).getSkipStatus());
      }

    }
    resultInSideDto = edit(problemsInsideDTO);
    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      resultInSideDto.setKey(RESULT.ERROR);
      return resultInSideDto;
    }
    StringBuilder stringBuilder = new StringBuilder();
    if (!StringUtils.isStringNullOrEmpty(problemsInsideDTO.getSkipStatus())) {
      stringBuilder.append(I18n.getLanguage("problemConfigTime.statusCancle")).append(": ");
      String[] arr = problemsInsideDTO.getSkipStatus().split(",");
      List<CatItemDTO> getListState = transitionStateConfigRepository.getListState(3L);
      for (int i = 0; i < arr.length; i++) {
        for (CatItemDTO catItemDTO : getListState) {
          if (arr[i].equals(catItemDTO.getItemId().toString()) && i < arr.length - 1) {
            stringBuilder.append(catItemDTO.getItemName()).append(", ");
          } else if (arr[i].equals(catItemDTO.getItemId().toString()) && i == arr.length - 1) {
            stringBuilder.append(catItemDTO.getItemName());
          }
        }
      }
    }
    ProblemActionLogsDTO problemActionLogsDTO = new ProblemActionLogsDTO(null, contentWorkLog,
        (new Date()), Long.parseLong(createUnitId), Long.parseLong(createUserId),
        problemsInsideDTO.getStateName(), (problemsInsideDTO.getProblemId()), createUnitName,
        createUserName, stringBuilder.toString().trim());
    problemActionLogsBusiness.insertProblemActionLogs(problemActionLogsDTO);
    if (problemsInsideDTO.getWorklogNew() != null && !""
        .equals(problemsInsideDTO.getWorklogNew())) {
      ProblemWorklogDTO problemWorklogDTO = new ProblemWorklogDTO(null,
          Long.valueOf(createUserId), createUserName, Long.valueOf(createUnitId), createUnitName,
          problemsInsideDTO.getWorklogNew(), problemsInsideDTO.getStateName(),
          (new Date()), problemsInsideDTO.getProblemId());
      problemWorklogBusiness.onInsert(problemWorklogDTO);
    }

    try {
      //TienNV them
      if (isChangeStatus) {
        if (!StringUtils.isStringNullOrEmpty(smsFor) && smsFor.contains("PM")) {
          messagesBusiness
              .insertSMSMessageForPm(content, Long.toString(problemsInsideDTO.getPmGroup()),
                  problemsInsideDTO);
        } else if (!StringUtils.isStringNullOrEmpty(smsFor) && smsFor.contains("PROCESS")) {
          if (problemsInsideDTO.getReceiveUserId() != null) {
            List<ConditionBean> lstCondition = new ArrayList<>();
            lstCondition.add(
                new ConditionBean("userId", String.valueOf(problemsInsideDTO.getReceiveUserId()),
                    Constants.NAME_EQUAL, Constants.NUMBER));
            lstCondition
                .add(new ConditionBean("isEnable", "1", Constants.NAME_EQUAL, Constants.NUMBER));
            List<UsersInsideDto> lstUser = userBusiness
                .getListUsersByCondition(lstCondition, 0, 1000, Constants.ASC, "username");
            if (lstUser != null && !lstUser.isEmpty()) {
              messagesBusiness.insertSMSMessageForUser(content, lstUser.get(0), problemsInsideDTO);
            }
          } else if (problemsInsideDTO.getReceiveUnitId() != null) {
            messagesBusiness.insertSMSMessageForUnitAllUser(content,
                String.valueOf(problemsInsideDTO.getReceiveUnitId()), null, problemsInsideDTO);
          }
        } else if (!StringUtils.isStringNullOrEmpty(smsFor) && smsFor.contains("CREATE")) {
          if (smsFor.contains("_")) {
            messagesBusiness
                .insertSMSMessageForPm(content, smsFor.split("_")[1], problemsInsideDTO);
          } else {
            List<ConditionBean> lstCondition = new ArrayList<>();
            lstCondition.add(
                new ConditionBean("userId", String.valueOf(problemsInsideDTO.getCreateUserId()),
                    Constants.NAME_EQUAL, Constants.NUMBER));
            lstCondition
                .add(new ConditionBean("isEnable", "1", Constants.NAME_EQUAL, Constants.NUMBER));
            List<UsersInsideDto> lstUser = userBusiness
                .getListUsersByCondition(lstCondition, 0, 1000, Constants.ASC, "username");

            if (lstUser != null && !lstUser.isEmpty()) {
              messagesBusiness.insertSMSMessageForUser(content, lstUser.get(0), problemsInsideDTO);
            }

            //du thao --> mo : nt them cho don vi xu ly
            if (stateCode.equals(PROBLEM.OPEN_2) && problemsInsideDTOOld.getStateCode() != null
                && problemsInsideDTOOld.getStateCode().equals(PROBLEM.OPEN)) {
              lstCondition = new ArrayList<>();
              lstCondition.add(
                  new ConditionBean("userId", String.valueOf(problemsInsideDTO.getReceiveUserId()),
                      Constants.NAME_EQUAL, Constants.NUMBER));
              lstCondition
                  .add(new ConditionBean("isEnable", "1", Constants.NAME_EQUAL, Constants.NUMBER));
              lstUser = userBusiness
                  .getListUsersByCondition(lstCondition, 0, 1000, Constants.ASC, "username");

              if (lstUser != null && !lstUser.isEmpty()) {
                messagesBusiness
                    .insertSMSMessageForUser("ptMngt.editpt.process", lstUser.get(0),
                        problemsInsideDTO);
              }
            }

            if (lstRoleUser.size() == 0) {
//              lstUser = ptProblemsRepository.lstUserLDP1(problemsInsideDTO.getReceiveUserId());
              if (lstUser != null && !lstUser.isEmpty()) {
                messagesBusiness
                    .insertSMSMessageForUserForPT("ptMngt.editpt.userBo", lstUser.get(0),
                        problemsInsideDTO);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateListProblems(List<ProblemsInsideDTO> lstProblemsInsideDTOS,
      ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to updateListProblems : {}", problemsInsideDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      List<ProblemActionLogsDTO> lstActionLogsDTOs = new ArrayList();
      List<ProblemWorklogDTO> lstWorklogDTOs = new ArrayList();
      String createUnitId = problemsInsideDTO.getUnitUpdateId();
      String createUnitName = problemsInsideDTO.getUnitUpdateName();
      String createUserId = problemsInsideDTO.getUserUpdateId();
      String createUserName = problemsInsideDTO.getUserUpdateName();

      resultInSideDto = edit(problemsInsideDTO);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        resultInSideDto.setKey(RESULT.ERROR);
        return resultInSideDto;
      }

      if (lstProblemsInsideDTOS != null && !lstProblemsInsideDTOS.isEmpty()) {

        ResultInSideDto resultInSideDtoTmp = insertOrUpdateListProblems(lstProblemsInsideDTOS);
        if (!RESULT.SUCCESS.equalsIgnoreCase(resultInSideDtoTmp.getKey())) {
          resultInSideDto.setKey(RESULT.ERROR);
          return resultInSideDto;
        }
        for (ProblemsInsideDTO problemsRelatedDTO : lstProblemsInsideDTOS) {
          ProblemActionLogsDTO problemActionLogsDTO = new ProblemActionLogsDTO(null,
              problemsRelatedDTO.getContent(), (new Date()),
              Long.parseLong(createUnitId), Long.parseLong(createUserId),
              problemsRelatedDTO.getStateName(),
              (problemsRelatedDTO.getProblemId()), createUnitName, createUserName, null);
          lstActionLogsDTOs.add(problemActionLogsDTO);
          if (problemsRelatedDTO.getWorklog() != null && !""
              .equals(problemsRelatedDTO.getWorklog())) {
            ProblemWorklogDTO problemWorklogDTO = new ProblemWorklogDTO(null,
                Long.valueOf(createUserId), createUserName, Long.valueOf(createUserId),
                createUnitName, problemsRelatedDTO.getWorklog(), problemsRelatedDTO.getStateName(),
                (new Date()), problemsRelatedDTO.getProblemId());
            lstWorklogDTOs.add(problemWorklogDTO);
          }
        }
      }

      ProblemActionLogsDTO problemActionLogsDTO = new ProblemActionLogsDTO(null,
          problemsInsideDTO.getContent(),
          (new Date()),
          Long.parseLong(createUnitId), Long.parseLong(createUserId),
          problemsInsideDTO.getStateName(),
          (problemsInsideDTO.getProblemId()),
          createUnitName, createUserName, null);
      lstActionLogsDTOs.add(problemActionLogsDTO);
      problemActionLogsBusiness.insertOrUpdateListProblemActionLogs(lstActionLogsDTOs);

      if (problemsInsideDTO.getWorklogNew() != null && !""
          .equals(problemsInsideDTO.getWorklogNew())) {
        ProblemWorklogDTO problemWorklogDTO = new ProblemWorklogDTO(null,
            Long.valueOf(createUserId), createUserName, Long.valueOf(createUserId), createUnitName,
            problemsInsideDTO.getWorklogNew(), problemsInsideDTO.getStateName(),
            (new Date()), problemsInsideDTO.getProblemId());
        lstWorklogDTOs.add(problemWorklogDTO);
        problemWorklogBusiness.insertOrUpdateListProblemWorklog(lstWorklogDTOs);
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateProblemsNew(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to updateProblemsNew : {}", problemsInsideDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    ProblemsInsideDTO problemsNewDTO = problemsInsideDTO.getProblemsNewDTO();
    try {
      String createUnitId = problemsInsideDTO.getUnitUpdateId();
      String createUnitName = problemsInsideDTO.getUnitUpdateName();
      String createUserId = problemsInsideDTO.getUserUpdateId();
      String createUserName = problemsInsideDTO.getUserUpdateName();

      resultInSideDto = edit(problemsInsideDTO);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        resultInSideDto.setKey(RESULT.ERROR);
        return resultInSideDto;
      }

      ProblemActionLogsDTO problemActionLogsDTO1 = new ProblemActionLogsDTO(null,
          problemsInsideDTO.getContent(), (new Date()), Long.parseLong(createUnitId),
          Long.parseLong(createUserId), problemsInsideDTO.getStateName(),
          (problemsInsideDTO.getProblemId()),
          createUnitName,
          createUserName, null);
      problemActionLogsBusiness.insertProblemActionLogs(problemActionLogsDTO1);
      if (problemsInsideDTO.getWorklogNew() != null && !""
          .equals(problemsInsideDTO.getWorklogNew())) {
        ProblemWorklogDTO problemWorklogDTO1 = new ProblemWorklogDTO(null,
            Long.valueOf(createUserId),
            createUserName, Long.valueOf(createUserId), createUnitName,
            problemsInsideDTO.getWorklogNew(),
            problemsInsideDTO.getStateName(), (new Date()),
            problemsInsideDTO.getProblemId());
        problemWorklogBusiness.onInsert(problemWorklogDTO1);
      }

      //tao PT moi
      if (problemsNewDTO != null) {
        String id = getSequenseProblems("problem_seq", 1).get(0);
        problemsNewDTO.setProblemId(Long.valueOf(id));
        String itemCode = problemsInsideDTO.getItemTypeCode();

        String ptCode =
            "PT_" + itemCode + "_" + DateTimeUtils.convertDateToString(new Date(), "yyMMdd") + "_"
                + id;
        problemsNewDTO.setProblemCode(ptCode);

        String worklog = problemsInsideDTO.getWorklog();
        if (worklog != null && !"".equals(worklog)) {
          problemsNewDTO.setWorklog(
              createUserName + " " + DateUtil.date2ddMMyyyyHHMMss(new Date()) + ": " + worklog);
        }
        resultInSideDto = add(problemsNewDTO);
        try {
          ProblemActionLogsDTO problemActionLogsDTO = new ProblemActionLogsDTO(null,
              problemsInsideDTO.getContent(), (new Date()), Long.parseLong(createUnitId),
              Long.parseLong(createUserId), problemsNewDTO.getStateName(), Long.parseLong(id),
              createUnitName, createUserName, null);
          problemActionLogsBusiness.insertProblemActionLogs(problemActionLogsDTO);
          if (worklog != null && !"".equals(worklog)) {
            ProblemWorklogDTO problemWorklogDTO = new ProblemWorklogDTO(null,
                Long.valueOf(createUserId),
                createUserName, Long.valueOf(createUserId), createUnitName, worklog,
                problemsNewDTO.getStateName(), (new Date()),
                problemsNewDTO.getProblemId());
            problemWorklogBusiness.onInsert(problemWorklogDTO);
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }
        resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getTransitionStatus(ProblemsInsideDTO dto) {
    Datatable datatable = new Datatable();
    UserToken userToken = ticketProvider.getUserToken();
    List<CatItemDTO> lstStateFinal = new ArrayList<>();
    List<CatItemDTO> lstState = catItemBusiness.getListItemByCategory(PROBLEM.STATE, null);
    List<RolesDTO> lstRole = getListRolePmByUser(userToken.getUserID());
    String beginStateCode = "";
    CatItemDTO cancelItem = new CatItemDTO();
    for (CatItemDTO catItemDTO : lstState) {
      if (dto.getProblemState().equals(String.valueOf(catItemDTO.getItemId()))) {
        beginStateCode = catItemDTO.getItemCode();
      }
      if (catItemDTO.getItemCode().equals(PROBLEM.PT_CANCELED)) {
        cancelItem = catItemDTO;
      }
    }

    boolean isPm = false;
    if (lstRole != null && !lstRole.isEmpty()) {
      String role = "";
      for (RolesDTO lDTO : lstRole) {
        role += lDTO.getRoleId() + ",";
      }

      if (role.contains(Long.toString(dto.getPmGroup()))) {
        isPm = true;
      }
    }
    if (isPm) {
      dto.setRoleCode(1L);
      lstStateFinal.addAll(ptProblemsRepository.getTransitionStatus(dto));
    }
    if (userToken.getUserID().equals(dto.getCreateUserId()) && (
        beginStateCode.equals(PROBLEM.OPEN) || beginStateCode.equals(PROBLEM.PT_REJECTED))) {
      if (beginStateCode.equals(PROBLEM.OPEN)) {
        CatItemDTO catItemDTO = new CatItemDTO();
        catItemDTO.setItemCode(cancelItem.getItemCode());
        catItemDTO.setItemId(cancelItem.getItemId());
        catItemDTO.setItemName(cancelItem.getItemName());
        lstStateFinal.add(catItemDTO);
      } else if (beginStateCode.equals(PROBLEM.PT_REJECTED)) {
        lstStateFinal.addAll(ptProblemsRepository.getTransitionStatus(dto));
      }
    }
    if (userToken.getUserID().equals(dto.getReceiveUserId())
        || userToken.getDeptId() != null
        && userToken.getDeptId()
        .equals(dto.getReceiveUnitId())) {
      dto.setRoleCode(2L);
      lstStateFinal.addAll(ptProblemsRepository.getTransitionStatus(dto));
    }

    if (lstStateFinal != null && lstStateFinal.size() > 0) {
      catItemBusiness.setLan(lstStateFinal);
      datatable.setTotal(lstStateFinal.size());
      datatable.setData(lstStateFinal);
    }
    return datatable;
  }

  public List<InfraDeviceDTO> getInfraDeviceDTOSByListCode(List<String> nodeCodes) {
    if (nodeCodes != null && nodeCodes.size() > 0) {
      return ptProblemsRepository.getInfraDeviceDTOSByListCode(nodeCodes);
    }
    return null;
  }

  private List<String> splitString(String problemName) {
    Map<String, String> map = new HashMap<String, String>();
    List<String> lst = new ArrayList<String>();
    problemName = problemName.toLowerCase().replaceAll("_", " ").replaceAll("-", " ");
    String[] arrayName = problemName.split(" ");
    if (arrayName != null && arrayName.length > 0) {
      for (String name : arrayName) {
        if (StringUtils.isNotNullOrEmpty(name) && map.get(name) == null) {
          map.put(name, name);
          lst.add(name);
        }
      }
    }
    return lst;
  }

  public List<ProblemsInsideDTO> searchProblemTicket(List<ProblemsInsideDTO> lst) {
//    Double offSet = (Double) Double.valueOf(I18n.getOffset());
    if (lst == null || lst.size() == 0) {
      lst = new ArrayList<>();
      return lst;
    }
//    SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    if (mapUnit == null) {
      mapUnit = getMapUnit();
    }
    setMapPMGroupName();
    List<CatItemDTO> lstAllCatItem = getAllCatItem("", "", "");
    Map<String, String> mapItem = new HashMap<String, String>();
    Map<String, String> mapItemCode = new HashMap<String, String>();
    for (CatItemDTO dto : lstAllCatItem) {
      mapItem.put(String.valueOf(dto.getItemId()), dto.getItemName());
      mapItemCode.put(String.valueOf(dto.getItemId()), dto.getItemCode());
    }
    List<String> lstUserId = new ArrayList<String>();
    for (ProblemsInsideDTO dto : lst) {
      String stateId = dto.getProblemState();
      String prioId = Long.toString(dto.getPriorityId());
      String typeId1 = String.valueOf(dto.getTypeId());
      String subCategoryId1 = String.valueOf(dto.getSubCategoryId());
      String impactId = String.valueOf(dto.getImpactId());
      String urgencyId = String.valueOf(dto.getUrgencyId());
      String accessId = String.valueOf(dto.getAccessId());
      String receiveUnitId1 = String.valueOf(dto.getReceiveUnitId());
      String receiveUserId =
          (dto.getReceiveUserId() == null) ? "" : String.valueOf(dto.getReceiveUserId());
      String categorization1 = String.valueOf(dto.getCategorization());
      String rcaTypeStr = String.valueOf(dto.getRcaType());
      String ptRelatedTypeStr = String.valueOf(dto.getPtRelatedType());
      if (!lstUserId.contains(receiveUserId) && receiveUserId != null && !""
          .equals(receiveUserId)) {
        lstUserId.add(receiveUserId);
      }
      dto.setTypeIdStr(mapItem.get(typeId1));
      dto.setTypeCode(mapItemCode.get(typeId1));
      dto.setSubCategoryIdStr(mapItem.get(subCategoryId1));
      dto.setImpactIdStr(mapItem.get(impactId));
      dto.setUrgencyIdStr(mapItem.get(urgencyId));
      dto.setAccessIdStr(mapItem.get(accessId));
      dto.setPriorityStr(mapItem.get(prioId));
      dto.setStatusStr(mapItem.get(stateId));
      dto.setSolutionTypeName(mapItem.get(String.valueOf(dto.getSolutionType()))); //HaiNV20 add
      //tiennv them remove html
      dto.setWa(StringUtils.isStringNullOrEmpty(dto.getWa()) ? "" : extractHtmlTag(dto.getWa()));
      dto.setRca(StringUtils.isStringNullOrEmpty(dto.getRca()) ? "" : extractHtmlTag(dto.getRca()));
      dto.setSolution(StringUtils.isStringNullOrEmpty(dto.getSolution()) ? ""
          : extractHtmlTag(dto.getSolution()));
      dto.setDescription(StringUtils.isStringNullOrEmpty(dto.getDescription()) ? ""
          : extractHtmlTag(dto.getDescription()));
      UnitDTO unitDto = mapUnit.get(receiveUnitId1);
      if (unitDto != null) {
        dto.setReceiveUnitIdStr(unitDto.getUnitName() + " (" + unitDto.getUnitCode() + ")");
      }
      dto.setCategorizationStr(mapItem.get(categorization1));
      dto.setRcaTypeStr(mapItem.get(rcaTypeStr));
      dto.setPtRelatedTypeStr(mapItem.get(ptRelatedTypeStr));
      dto.setPmGroupName(mapPMGroupName.get(String.valueOf(dto.getPmGroup())));
      try {
//        setTimeZone(dto, offSet);
        //rca color
        Date esTimeWa = null;
        Date esTimeSl = null;
        Date esTimeRca = null;
        Date createTime = null;
        if (dto.getCreatedTime() != null) {
          createTime = dto.getCreatedTime();
        }
        Date timeRca = dto.getRcaFoundTime();

        try {
          esTimeWa = dto.getEsWaTime();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        try {
          esTimeRca = dto.getEsRcaTime();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        try {
          esTimeSl = dto.getEsSlTime();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        Date now = new Date();

        Date timeWa = dto.getWaFoundTime();
        if (esTimeWa != null && timeWa == null) {
          Long timeRest = esTimeWa.getTime() - now.getTime();
          if (timeRest <= 0) {
            dto.setIsOutOfDate(I18n.getLanguage("yes"));
          }
        }
        if (esTimeRca != null && timeRca == null) {
          Long timeRest = esTimeRca.getTime() - now.getTime();
          if (timeRest <= 0) {
            dto.setIsOutOfDate(I18n.getLanguage("yes"));
          }
        }
        Date timeSl = dto.getSlFoundTime();
        if (esTimeSl != null && timeSl == null) {
          Long timeRest = esTimeSl.getTime() - now.getTime();
          if (timeRest <= 0) {
            dto.setIsOutOfDate(I18n.getLanguage("yes"));
          }
        }

        if (esTimeRca != null && timeRca == null && null != createTime) {
          dto = setColor(dto, esTimeRca, now, createTime);
        }

        if (StringUtils.isStringNullOrEmpty(dto.getColor())) {
          //wa color
          if (esTimeWa != null && timeWa == null && null != createTime) {
            dto = setColor(dto, esTimeWa, now, createTime);
          }
        }

        if (StringUtils.isStringNullOrEmpty(dto.getColor())) {
          //sl color
          if (esTimeSl != null && timeSl == null && null != createTime) {
            dto = setColor(dto, esTimeSl, now, createTime);
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      if (dto.getIsOutOfDate() == null) {
        dto.setIsOutOfDate(I18n.getLanguage("no"));
      }
      if (dto.getColor() == null) {
        dto.setColor("white");
      }
    }
    //Lay user
    List<UsersInsideDto> lstUser = null;
    String idIn = "";
    idIn = lstUserId.stream().filter(st -> st != null).map((st) -> st + ",")
        .reduce(idIn, String::concat);
    if (StringUtils.isNotNullOrEmpty(idIn)) {
      ConditionBean con = new ConditionBean();
      con.setField("userId");
      con.setOperator(Constants.NAME_IN);
      con.setType(Constants.NUMBER);
      con.setValue(idIn);
      List<ConditionBean> lstCondition = new ArrayList<ConditionBean>();
      lstCondition.add(con);
      try {
        lstUser = userBusiness.getListUsersByCondition(lstCondition, 0, 0, "", "");
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
    }

    if (lstUser == null) {
      lstUser = new ArrayList<>();
    }
    Map<String, String> mapUser = new HashMap<>();
    for (UsersInsideDto dto : lstUser) {
      mapUser.put(String.valueOf(dto.getUserId()), dto.getUsername());
    }
    for (ProblemsInsideDTO dto : lst) {
      String receiveUserId = String.valueOf(dto.getReceiveUserId());
      dto.setReceiveUserIdStr(mapUser.get(receiveUserId));
    }
    return lst;
  }

  private ProblemsInsideDTO setColor(ProblemsInsideDTO dto, Date timeSet, Date timeNow,
      Date creatTime) {
    Long timeRest = timeSet.getTime() - timeNow.getTime();
    Long timeEs = timeSet.getTime() - creatTime.getTime();
    if (timeRest <= 0) {
      dto.setColor("red");
    } else {
      Double percent = timeRest.doubleValue() / timeEs;
      if (percent < 0.2) {
        dto.setColor("yellow");
      }
    }
    return dto;
  }

  public Map<String, UnitDTO> getMapUnit() {
    lstUnitAll = unitBusiness.getListUnitByLevel("");
    UnitDTO unitDTO = new UnitDTO();
    try {
      lstUnitAll = unitBusiness.getListUnitDTO(unitDTO, 0, 20000, "asc", "unitName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (lstUnitAll == null) {
      lstUnitAll = new ArrayList<UnitDTO>();
    }
    Map<String, UnitDTO> mapUnit = new HashMap<String, UnitDTO>();
    for (UnitDTO dto : lstUnitAll) {
      mapUnit.put(String.valueOf(dto.getUnitId()), dto);
    }
    return mapUnit;
  }

  public List<CatItemDTO> getAllCatItem(String lan, String appliedSystem, String appliedBussiness) {
    List<CatItemDTO> lstCatItemDTO = new ArrayList<CatItemDTO>();
    try {
      CatItemDTO catItemDTO = new CatItemDTO();
      catItemDTO.setStatus(1l);
      lstCatItemDTO = catItemBusiness
          .getListCatItemDTOLE("", "", "", catItemDTO, 0, 0, "asc", "itemName");
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return lstCatItemDTO;
  }

  private File exportFileEx(List<ProblemsInsideDTO> lstImport, String[] columnExport,
      String fromDate,
      String toDate) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    Map<String, String> fieldSplit = new HashMap<>();
    String title = I18n.getLanguage("problems.export.title");
    String fileNameOut = "PROBLEMS_EXPORT";
    String sheetName = I18n.getLanguage("problems.export.sheetName");
    String subTitle;
    if (fromDate != null && toDate != null) {
      Date fromDateTmp = DataUtil.convertStringToDate(fromDate);
      Date toDateTmp = DataUtil.convertStringToDate(toDate);
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      subTitle = I18n.getLanguage("problems.export.eportDate", dateFormat.format(fromDateTmp),
          dateFormat.format(toDateTmp));
    } else {
      subTitle = "";
    }

    ConfigFileExport configfileExport = new ConfigFileExport(
        lstImport
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.problems"
        , lstHeaderSheet1
        , fieldSplit
        , ""
        , I18n.getLanguage("problems.export.firstLeftHeader")
        , I18n.getLanguage("problems.export.secondLeftHeader")
        , I18n.getLanguage("problems.export.firstRightHeader")
        , I18n.getLanguage("problems.export.secondRightHeader")
    );
    configfileExport.setLangKey(I18n.getLocale());
    configfileExport.setIsAutoSize(true); //tiennv bo sung auto size column
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("problems.stt"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configfileExport);
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  private List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
  }

  @Override
  public List<UsersInsideDto> getListChatUsers(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to getListChatUsers : {}", problemsInsideDTO);
    return ptProblemsRepository.getListChatUsers(problemsInsideDTO);
  }

  @Override
  public List<ProblemsInsideDTO> getListPtRelated(ProblemsInsideDTO problemsInsideDTO) {
    String ptRelatedTypeCode;
    List<ProblemsInsideDTO> listProblemsInsideDTO = new ArrayList<>();
    setMapPtRelatedType();
    setMapPtState();
    if (problemsInsideDTO.getPtRelatedType() != null) {
      Double offset = TimezoneContextHolder.getOffsetDouble();
      ptRelatedTypeCode = mapPtRelatedType.get(problemsInsideDTO.getPtRelatedType());
      if (ptRelatedTypeCode.equals(PROBLEM.PT_PARENT) || ptRelatedTypeCode
          .equals(PROBLEM.PT_PRIMARY)) {
        List<ConditionBean> lstConditionParent = new ArrayList<>();
        lstConditionParent.add(new ConditionBean("relatedPt", problemsInsideDTO.getProblemCode(),
            Constants.NAME_EQUAL, Constants.STRING));
        listProblemsInsideDTO = getListProblemsByCondition(lstConditionParent, 0,
            1000, "asc", "problemId");
        for (ProblemsInsideDTO dto : listProblemsInsideDTO) {
          dto.setStatusStr(mapPtState.get(dto.getProblemState()));
        }
      } else if (ptRelatedTypeCode.equals(PROBLEM.PT_CHILDREN) || ptRelatedTypeCode
          .equals(PROBLEM.PT_SECONDARY) ||
          ptRelatedTypeCode.equals(PROBLEM.PT_DUPLICATED) || ptRelatedTypeCode
          .equals(PROBLEM.PT_REDO)) {
        List<ConditionBean> lstConditionParent = new ArrayList<>();
        lstConditionParent.add(new ConditionBean("problemCode", problemsInsideDTO.getRelatedPt(),
            Constants.NAME_EQUAL, Constants.STRING));
        listProblemsInsideDTO = getListProblemsByCondition(lstConditionParent, 0,
            1000, "asc", "problemId");
        for (ProblemsInsideDTO dto : listProblemsInsideDTO) {
          dto.setStatusStr(mapPtState.get(dto.getProblemState()));
        }
      }
      for (ProblemsInsideDTO dto : listProblemsInsideDTO) {
        try {
          setTimeZone(dto, offset);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return listProblemsInsideDTO;
  }

  @Override
  public ResultInSideDto sendChatListUsers(ProblemsInsideDTO problemsInsideDTO) throws Exception {
    ResultInSideDto resultInSideDto;
    //Neu chua tao nhom chat, thi se hien popup chon nhom chat. lstUsers la list user lay duoc tu popup
    //bao gom ca: lstUserDefault: [userTokenGNOC.getUserId(), dtoTran.getCreateUserId(), dtoTran.getReceiveUserId()]
    try {
      UserToken userToken = ticketProvider.getUserToken();
      UnitDTO unitToken = unitBusiness.findUnitById(userToken.getDeptId());
      GroupBusiness business = new GroupBusiness();
      business.setGroupName(problemsInsideDTO.getProblemCode());
      business.setUserCode(userToken.getUserName());

      business.setModuleCode("PT");
      business.setSubModules(I18n.getLanguage("monitor.system.PT"));
      business.setId(problemsInsideDTO.getProblemCode());
      if (problemsInsideDTO.getIsChat() != null && problemsInsideDTO.getIsChat() == 1) {
        business.setMembers(userToken.getUserName());
        //WSChatPort chatPort = new WSChatPort();
        GroupResponse groupResponse = wsChatPort.createGroupInBusiness2(business);
        if (groupResponse != null && groupResponse.getResultCode() == 1) {
          resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS,
              I18n.getLanguage("common.chat.success"));
        } else if (groupResponse != null) {
          resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
              I18n.getLanguage("common.update.fail"));
        } else {
          resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
              I18n.getLanguage("common.update.fail"));
        }
      } else {
        if (problemsInsideDTO.getUsersInsideDtos() != null && !problemsInsideDTO
            .getUsersInsideDtos().isEmpty()) {
          String member = "";
          for (UsersInsideDto usersInsideDTO : problemsInsideDTO.getUsersInsideDtos()) {
            member = member + "," + usersInsideDTO.getUsername();
          }
          member = member.replaceFirst(",", "");
          business.setMembers(member);
          // WSChatPort chatPort = new WSChatPort();
          GroupResponse groupResponse = wsChatPort.createGroupInBusiness2(business);
          if (groupResponse != null && groupResponse.getResultCode() == 1) {
            ProblemsInsideDTO problemsInsideDTOOld = findProblemsById(
                problemsInsideDTO.getProblemId());
            ProblemsInsideDTO newDTO = problemsInsideDTOOld;
            newDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
            newDTO.setIsChat(1L);
            newDTO.setUnitUpdateId(userToken.getDeptId().toString());
            newDTO.setUnitUpdateName(unitToken.getUnitName());
            newDTO.setUserUpdateId(userToken.getUserID().toString());
            newDTO.setUserUpdateName(userToken.getUserName());
            updateProblems(newDTO);
            resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS,
                I18n.getLanguage("common.chat.success"));
          } else if (groupResponse != null) {
            resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                I18n.getLanguage("common.update.fail"));
          } else {
            resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                I18n.getLanguage("common.update.fail"));
          }
        } else {
          resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
              I18n.getLanguage("common.update.fail"));
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new Exception(e);
    }
    return resultInSideDto;
  }

  @Override
  public Datatable loadUserSupportGroup(ProblemsInsideDTO problemsInsideDTO) {
    CatItemDTO catItemDTO = catItemBusiness.getCatItemById(problemsInsideDTO.getTypeId());
    String type = "PT_CHAT_";
    if (catItemDTO != null && catItemDTO.getItemCode() != null) {
      type += catItemDTO.getItemCode();
    }
    List<CatItemDTO> lst = catItemBusiness.getListItemByCategory("CHAT_CODE", null);
    CatItemDTO itemDTO = new CatItemDTO();
    if (lst != null && !lst.isEmpty()) {
      for (CatItemDTO cat : lst) {
        if (type.equals(cat.getItemCode())) {
          itemDTO = cat;
          break;
        }
      }
    }
    List<String> lstUsername = new ArrayList<>();
    //danh sach nhan vien dc cau hinh tai danh muc chung
    if (StringUtils.isNotNullOrEmpty(itemDTO.getDescription())) {
      lstUsername = Arrays.asList(itemDTO.getDescription().split(","));
    }
    UsersInsideDto usersInsideDto = new UsersInsideDto();
    usersInsideDto.setLstUserName(lstUsername);
    usersInsideDto.setPage(problemsInsideDTO.getPage());
    usersInsideDto.setPageSize(problemsInsideDTO.getPageSize());
    if (lstUsername != null && !lstUsername.isEmpty()) {
      Datatable datatable = userBusiness.getListUsersByList(usersInsideDto);
      return datatable;
    }
    return new Datatable();
  }

  public void setMapPtRelatedType() {
    List<CatItemDTO> list = catItemRepository.getListItemByCategory(CATEGORY.PT_RELATED_TYPE, null);
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapPtRelatedType.put(dto.getItemId(), dto.getItemCode());
      }
    }
  }

  public void setMapPtState() {
    List<CatItemDTO> list = catItemRepository.getListItemByCategory(CATEGORY.PT_STATE, null);
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapPtState.put(String.valueOf(dto.getItemId()), dto.getItemName());
      }
    }
  }

  public void setMapPMGroupName() {
    ConditionBean condition = new ConditionBean("roleCode", "PM_",
        Constants.NAME_LIKE, Constants.STRING);
    List<ConditionBean> lstCondition = new ArrayList<>();
    lstCondition.add(condition);
    List<RolesDTO> lstRolePM = rolesBusiness.getListRolesByCondition(lstCondition,
        0, 0, "asc", "roleCode");
    if (lstRolePM != null && !lstRolePM.isEmpty()) {
      for (RolesDTO dto : lstRolePM) {
        mapPMGroupName.put(String.valueOf(dto.getRoleId()), dto.getRoleName());
      }
    }
  }

  @Override
  public List<RolesDTO> getListRolePmByUser(Long userLoginId) {
    List<String> lstRoleId = new ArrayList<String>();
    List<ConditionBean> lstCondition = new ArrayList<>();
    ConditionBean condition = new ConditionBean("userId", String.valueOf(userLoginId),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);

    try {
      List<RoleUserDTO> lstRoleUser = roleUserBusiness
          .getListRoleUserByCondition(lstCondition, 0, 0, "asc", "roleId");
      if (lstRoleUser != null && lstRoleUser.size() >= 1) {
        for (RoleUserDTO roleUserDTO : lstRoleUser) {
          lstRoleId.add(roleUserDTO.getRoleId());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    List<RolesDTO> lstRole = new ArrayList<>();
    lstCondition = new ArrayList<>();
    condition = new ConditionBean("roleCode", "PM_", Constants.NAME_LIKE, Constants.STRING);
    lstCondition.add(condition);
    try {
      lstRole = rolesBusiness.getListRolesByCondition(lstCondition, 0, 0, "asc", "roleCode");
      for (int i = lstRole.size() - 1; i >= 0; i--) {
        if (!lstRoleId.contains(lstRole.get(i).getRoleId())) {
          lstRole.remove(i);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstRole;
  }

  public static void setTimeZone(ProblemsInsideDTO problemsInsideDTO, Double offset)
      throws ParseException {
    if (problemsInsideDTO.getCreatedTime() != null && !"".equals(problemsInsideDTO.getCreatedTime())
        && problemsInsideDTO.getProblemId() != null) {
      Date d = problemsInsideDTO.getCreatedTime();
      problemsInsideDTO.setCreatedTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getLastUpdateTime() != null && !""
        .equals(problemsInsideDTO.getLastUpdateTime())
        && problemsInsideDTO.getProblemId() != null) {
      Date d = problemsInsideDTO.getLastUpdateTime();
      problemsInsideDTO.setLastUpdateTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getAssignTimeTemp() != null && !""
        .equals(problemsInsideDTO.getAssignTimeTemp())
        && problemsInsideDTO.getProblemId() != null) {
      Date d = problemsInsideDTO.getAssignTimeTemp();
      problemsInsideDTO.setAssignTimeTemp(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getAssignedTime() != null && !""
        .equals(problemsInsideDTO.getAssignedTime())) {
      Date d = problemsInsideDTO.getAssignedTime();
      problemsInsideDTO.setAssignedTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getEsRcaTime() != null && !"".equals(problemsInsideDTO.getEsRcaTime())) {
      Date d = problemsInsideDTO.getEsRcaTime();
      problemsInsideDTO.setEsRcaTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getEsWaTime() != null && !"".equals(problemsInsideDTO.getEsWaTime())) {
      Date d = problemsInsideDTO.getEsWaTime();
      problemsInsideDTO.setEsWaTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getEsSlTime() != null && !"".equals(problemsInsideDTO.getEsSlTime())) {
      Date d = problemsInsideDTO.getEsSlTime();
      problemsInsideDTO.setEsSlTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getStartedTime() != null && !""
        .equals(problemsInsideDTO.getStartedTime())) {
      Date d = problemsInsideDTO.getStartedTime();
      problemsInsideDTO.setStartedTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getEndedTime() != null && !"".equals(problemsInsideDTO.getEndedTime())) {
      Date d = problemsInsideDTO.getEndedTime();
      problemsInsideDTO.setEndedTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getRcaFoundTime() != null && !""
        .equals(problemsInsideDTO.getRcaFoundTime())
        && problemsInsideDTO.getProblemId() != null) {
      Date d = problemsInsideDTO.getRcaFoundTime();
      problemsInsideDTO.setRcaFoundTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getWaFoundTime() != null && !"".equals(problemsInsideDTO.getWaFoundTime())
        && problemsInsideDTO.getProblemId() != null) {
      Date d = problemsInsideDTO.getWaFoundTime();
      problemsInsideDTO.setWaFoundTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getSlFoundTime() != null && !"".equals(problemsInsideDTO.getSlFoundTime())
        && problemsInsideDTO.getProblemId() != null) {
      Date d = problemsInsideDTO.getSlFoundTime();
      problemsInsideDTO.setSlFoundTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getClosedTime() != null && !"".equals(problemsInsideDTO.getClosedTime())
        && problemsInsideDTO.getProblemId() != null) {
      Date d = problemsInsideDTO.getClosedTime();
      problemsInsideDTO.setClosedTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getDelayTime() != null && !"".equals(problemsInsideDTO.getDelayTime())
        && problemsInsideDTO.getProblemId() != null) {
      Date d = problemsInsideDTO.getDelayTime();
      problemsInsideDTO.setDelayTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
    if (problemsInsideDTO.getDeferredTime() != null && !""
        .equals(problemsInsideDTO.getDeferredTime())) {
      Date d = problemsInsideDTO.getDeferredTime();
      problemsInsideDTO.setDeferredTime(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)));
    }
  }

  private String extractHtmlTag(String value) {
    Document docDes = Jsoup.parse(value);
    docDes.outputSettings(new OutputSettings().prettyPrint(false));
    docDes.select("br").after("\\n");
    docDes.select("p").before("\\n");
    String des = docDes.html().replaceAll("\\\\n", "\n");
    String field = (Jsoup.clean(des, "", Whitelist.none(), new OutputSettings().prettyPrint(false)))
        .replaceAll("&nbsp;", " ");
    if (field.startsWith("\n")) {
      field = field.replaceFirst("\n", "");
    }
    return field;
  }

  @Override
  public Datatable searchParentPTForCR(ProblemsInsideDTO dto) {
    ConditionBean conditionBean1 = new ConditionBean("categoryId", "3", Constants.NAME_EQUAL,
        Constants.NUMBER);
    ConditionBean conditionBean2 = new ConditionBean("itemCode",
        "PT_QUEUED,PT_DIAGNOSED,PT_WA_FOUND,PT_WA_IMPL,"
            + "PT_SL_FOUND,PT_SL_IMPL,PT_CLEAR,PT_ROOT_CAUSE_PROPOSAL,PT_WORKARROUND_PROPOSAL,PT_SOLUTION_PROPOSAL",
        Constants.NAME_IN, Constants.STRING);
    List<ConditionBean> lstConditionBeans = new ArrayList<>();
    lstConditionBeans.add(conditionBean1);
    lstConditionBeans.add(conditionBean2);
    ConditionBeanUtil.sysToOwnListCondition(lstConditionBeans);
    List<CatItemDTO> lstCatItemDTO = catItemRepository
        .getListCatItemByCondition(lstConditionBeans, 0, 10, Constants.ASC, "itemName");
    String stateId = "122,123,124,125,126,127,128,3203,3204,3223";

    if (lstCatItemDTO != null && !lstCatItemDTO.isEmpty()) {
      stateId = "";
      for (CatItemDTO catItemDTO : lstCatItemDTO) {
        stateId += catItemDTO.getItemId() + ",";
      }
      if (stateId.endsWith(",")) {
        stateId = stateId.substring(0, stateId.length() - 1);
      }
    }
    dto.setStateCode(stateId);
    return ptProblemsRepository.searchParentPTForCR(dto);
  }

  @Override
  public Datatable getListProblemFiles(GnocFileDto gnocFileDto) {
    log.debug("Request to getListProblemFiles: {}", gnocFileDto);
    Datatable datatable = ptProblemsRepository.getListProblemFiles(gnocFileDto);
    Double offset;
    try {
      offset = TimezoneContextHolder.getOffsetDouble();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      offset = 0D;
    }
    List<GnocFileDto> list = (List<GnocFileDto>) datatable.getData();
    SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    for (GnocFileDto dto : list) {
      try {
        String createDate = DateUtil.date2ddMMyyyyHHMMss(dto.getCreateTime());
        Date display = spd.parse(createDate);
        display = new Date(display.getTime() + (long) (offset * 60 * 60 * 1000));
        dto.setCreateTime(display);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    datatable.setData(list);
    return datatable;
  }

  @Override
  public ResultInSideDto insertProblemFiles(List<MultipartFile> files,
      ProblemsInsideDTO problemsInsideDTO)
      throws IOException {
    log.debug("Request to insertProblemFiles: {}", problemsInsideDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    for (MultipartFile multipartFile : files) {
      String fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
          PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
          multipartFile.getBytes(), null);
      String fullPathOld = FileUtils
          .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
              uploadFolder, null);
      String fileName = multipartFile.getOriginalFilename();
      //Start save file old
      ProblemFilesDTO problemFilesDTO = new ProblemFilesDTO();
      problemFilesDTO.setProblemFileName(FileUtils.getFileName(fullPathOld));
      problemFilesDTO.setCreateUnitId(userToken.getDeptId());
      problemFilesDTO.setCreateUnitName(unitToken.getUnitName());
      problemFilesDTO.setCreateUserId(userToken.getUserID());
      problemFilesDTO.setCreateUserName(userToken.getUserName());
      problemFilesDTO.setCreateTime(new Date());
      problemFilesDTO.setProblemId(problemsInsideDTO.getProblemId());
      ResultInSideDto resultFileDataOld = ptProblemsRepository
          .insertProblemFiles(problemFilesDTO);
      //End save file old
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(fileName);
      gnocFileDto.setCreateUnitId(userToken.getDeptId());
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(userToken.getUserID());
      gnocFileDto.setCreateUserName(userToken.getUserName());
      gnocFileDto.setCreateTime(problemFilesDTO.getCreateTime());
      gnocFileDto.setMappingId(resultFileDataOld.getId());
      gnocFileDtos.add(gnocFileDto);
    }
    gnocFileRepository
        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.PROBLEMS,
            problemsInsideDTO.getProblemId(), gnocFileDtos);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteProblemFiles(GnocFileDto gnocFileDto) {
    log.debug("Request to deleteProblemFiles: {}", gnocFileDto);
    //Start delete file old
    ptProblemsRepository.deleteProblemFiles(gnocFileDto.getMappingId());
    //End delete file old
    return gnocFileRepository.deleteGnocFileByDto(gnocFileDto);
  }

  @Override
  public Datatable getDatatableProblemsChartUpgrade(ProblemsInsideDTO problemsInsideDTO) {
    log.info("Request to getDatatableProblemsChartUpgrade: {}", problemsInsideDTO);
    Datatable datatable = ptProblemsRepository.getDatatableProblemsChartUpgrade(problemsInsideDTO);
    List<ProblemsInsideDTO> lst = searchProblemTicket(
        (List<ProblemsInsideDTO>) datatable.getData());
    datatable.setData(lst);
    return datatable;
  }

  @Override
  public Map<String, Map<String, Long>> getListProblemsChartUpgrade(
      ProblemsInsideDTO problemsInsideDTO) {
    log.info("Request to getListProblemsChartUpgrade: {}", problemsInsideDTO);
    List<ProblemsInsideDTO> lstData = ptProblemsRepository
        .getListProblemsChartUpgrade(problemsInsideDTO);
    Date timeNow = new Date();
    Map<String, Map<String, Long>> mapresult = new HashMap<>();
    Map<String, Map<String, Long>> mapresult1 = new HashMap<>();
    Map<String, Long> item1 = new HashMap<>();
    item1.put("red", 0L);
    item1.put("white", 0L);
    item1.put("yellow", 0L);
    String a[] = problemsInsideDTO.getProblemState().split(",");
    boolean check = false;

    for (ProblemsInsideDTO dto : lstData) {
      String keyStatus = dto.getProblemState();
      String key = processTimeOverrdua(dto, timeNow);
      if (mapresult.containsKey(keyStatus)) {
        Map<String, Long> item = mapresult.get(keyStatus);
        if (item.containsKey(key)) {
          Long itemNum = item.get(key);
          item.put(key, itemNum + 1);
        } else {
          item.put(key, 1L);
        }
      } else {
        Map<String, Long> item = new HashMap<>();
        item.put(key, 1L);
        mapresult.put(keyStatus, item);
      }
    }

    for (int i = 0; i < a.length; i++) {
      check = false;
      for (Map.Entry m : mapresult.entrySet()) {
        if (mapresult.containsKey(a[i])) {
          check = true;
        }
      }
      if (check == false) {
        mapresult.put(a[i], item1);
      }
    }

    return mapresult;
  }

  @Override
  public ResultInSideDto checkInfoUserPM() {
    UserToken userToken = ticketProvider.getUserToken();
    boolean isManager = userRepository.isManagerOfUnits(userToken.getUserID());
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    List<RolesDTO> lstRole = getListRolePmByUser(userToken.getUserID());
    List<String> pmId = new ArrayList<>();
    if (lstRole != null && lstRole.size() > 0) {
      lstRole.forEach(item -> {
        pmId.add(item.getRoleId());
      });
    }
    resultInSideDto.setObject(pmId);
    resultInSideDto.setCheck(isManager);
    resultInSideDto.setId(userToken.getUserID());
    return resultInSideDto;
  }

  @Override
  public String countDayOff(ProblemConfigTimeDTO dto) {
    ProblemConfigTimeDTO problemConfigTimeDTO = null;
    String countDayOff = "";
    if (dto.getSolutionTypeId() != null && dto.getReasonGroupId() != null && dto.getTypeId() != null
        && dto.getSubCategoryId() != null) {
      Datatable datatable = problemConfigTimeRepository.onSearchProbleConfigTime(dto);
      if (!datatable.getData().isEmpty()) {
        problemConfigTimeDTO = (ProblemConfigTimeDTO) datatable.getData().get(0);
        problemConfigTimeDTO.getTimeProcess();
        Date td, fd = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(fd);
        c.add(Calendar.DATE, Integer.parseInt(problemConfigTimeDTO.getTimeProcess().toString()));
        td = c.getTime();
        String fromDate = DateTimeUtils.convertDateToString(fd);
        String toDate = DateTimeUtils.convertDateToString(td);
        countDayOff = catItemRepository.countDayOff("0", fromDate, toDate, "VN");
      }
    }
    return countDayOff;
  }

  @Override
  public List<OdSearchInsideDTO> findListOdByPt(Long problemId) {
    log.info("Request to findListOdByPt : {}", problemId);
    UserToken userToken = ticketProvider.getUserToken();
    OdSearchInsideDTO odDTOSearch = new OdSearchInsideDTO();
    odDTOSearch.setUserId(userToken.getUserID().toString());
    odDTOSearch.setInsertSource("PT");
    odDTOSearch.setOtherSystemCode(problemId.toString());
    odDTOSearch.setPage(1);
    odDTOSearch.setPageSize(Integer.MAX_VALUE);
    return odServiceProxy.getListDataSearchForOther(odDTOSearch);
  }

  @Override
  public List<SrInsiteDTO> findListSrByPt(Long problemId) {
    log.info("Request to findListSrByPt : {}", problemId);
    UserToken userToken = ticketProvider.getUserToken();
    SrInsiteDTO srInsiteDTO = new SrInsiteDTO();
    srInsiteDTO.setUserId(userToken.getUserID().toString());
    srInsiteDTO.setInsertSource("PT");
    srInsiteDTO.setOtherSystemCode(problemId.toString());
    srInsiteDTO.setPage(1);
    srInsiteDTO.setPageSize(Integer.MAX_VALUE);
    return srServiceProxy.getListDataSearchForPt(srInsiteDTO);
  }

  @Override
  public UsersEntity usersEntity(Long userId) {
    UsersEntity usersEntity = userRepository.getUserByUserId(userId);
    return usersEntity;
  }

  @Override
  public List<ProblemsDTO> onSynchProblem(String fromDate, String toDate, String insertSource,
      List<String> lstState) {
    return ptProblemsRepository.onSynchProblem(fromDate, toDate, insertSource, lstState);
  }


  private String processTimeOverrdua(ProblemsInsideDTO dto, Date now) {

    Date createTime = dto.getCreatedTime();
    Date esTimeWa = dto.getEsWaTime();
    Date esTimeRca = dto.getEsRcaTime();
    Date esTimeSl = dto.getEsSlTime();
    Date timeWa = dto.getWaFoundTime();
    Date timeSl = dto.getSlFoundTime();
    Date timeRca = dto.getRcaFoundTime();
    String msgKey = "";
    if (esTimeRca != null && timeRca == null && null != createTime) {
      msgKey = setKey(esTimeRca, now, createTime);
    }

    if (StringUtils.isStringNullOrEmpty(msgKey)) {
      //wa color
      if (esTimeWa != null && timeWa == null && null != createTime) {
        msgKey = setKey(esTimeWa, now, createTime);
      }
    }

    if (StringUtils.isStringNullOrEmpty(msgKey)) {
      //sl color
      if (esTimeSl != null && timeSl == null && null != createTime) {
        msgKey = setKey(esTimeSl, now, createTime);
      }
    }

    if (StringUtils.isStringNullOrEmpty(msgKey)) {
      msgKey = "white";
    }
    return msgKey;
  }

  private String setKey(Date timeSet, Date timeNow,
      Date creatTime) {
    Long timeRest = timeSet.getTime() - timeNow.getTime();
    Long timeEs = timeSet.getTime() - creatTime.getTime();
    if (timeRest <= 0) {
      return "red";
    } else {
      Double percent = timeRest.doubleValue() / timeEs;
      if (percent < 0.2) {
        return "yellow";
      }
    }
    return "";
  }
}
