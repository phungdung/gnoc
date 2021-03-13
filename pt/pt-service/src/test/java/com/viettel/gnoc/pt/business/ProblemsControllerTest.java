package com.viettel.gnoc.pt.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.SpringApplicationContext;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.incident.provider.WSChatPort;
import com.viettel.gnoc.commons.incident.provider.WSChatPortFactory;
import com.viettel.gnoc.commons.repository.CatItemRepositoryImpl;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.pt.dto.ProblemActionLogsDTO;
import com.viettel.gnoc.pt.dto.ProblemMonitorDTO;
import com.viettel.gnoc.pt.dto.ProblemsChartDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemsMobileDTO;
import com.viettel.gnoc.pt.repository.PtProblemsRepository;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import viettel.passport.client.UserToken;
@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({ProblemsBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, ConditionBeanUtil.class, WSChatPort.class, WSChatPortFactory.class,
    PassProtector.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class ProblemsControllerTest {

  @InjectMocks
  ProblemsBusinessImpl problemsBusiness;

  @Mock
  ProblemsBusinessImpl problemsBusiness1;

  @Mock
  ProblemActionLogsBusiness problemActionLogsBusiness;

  @Mock
  ProblemWorklogBusiness problemWorklogBusiness;

  @Mock
  CatItemRepositoryImpl catItemRepository;

  @Mock
  CatItemBusiness catItemBusiness;

  @Mock
  ProblemNodeBusiness problemNodeBusiness;

  @Mock
  PtProblemsRepository ptProblemsRepository;

  @Mock
  Map<Long, String> mapPtRelatedType;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitRepository unitRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Test
  public void getListProblemDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemsInsideDTO problemsInsideDTO = PowerMockito.mock(ProblemsInsideDTO.class);
    List<ProblemsInsideDTO> problemsInsideDTOS = Mockito.spy(ArrayList.class);
    problemsInsideDTOS.add(problemsInsideDTO);
    PowerMockito.when(problemsBusiness.getListProblemDTO(problemsInsideDTO)).thenReturn(
        problemsInsideDTOS);
    List<ProblemsInsideDTO> problemsInsideDTOS1 = ptProblemsRepository.getListProblemDTO(
        problemsInsideDTO);
    Assert.assertEquals(problemsInsideDTOS, problemsInsideDTOS1);
  }

  @Test
  public void getListProblemDTOForTT() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
    List<ProblemsInsideDTO> problemsInsideDTOS = Mockito.spy(ArrayList.class);
    problemsInsideDTOS.add(problemsInsideDTO);
    PowerMockito.when(problemsBusiness.getListProblemDTOForTT(problemsInsideDTO))
        .thenReturn(problemsInsideDTOS);
    List<ProblemsInsideDTO> problemsInsideDTOS1 = ptProblemsRepository.getListProblemDTO(
        problemsInsideDTO);
    Assert.assertEquals(problemsInsideDTOS, problemsInsideDTOS1);
  }

//  @Test
//  public void exportDataProblemsSearch() throws Exception {
//    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//    File file = null;
//    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
//    problemsInsideDTO.setFromDate("06/04/2014");
//    problemsInsideDTO.setToDate("06/04/2019");
//    Field field = ProblemsBusinessImpl.class.getDeclaredField("tempFolder");
//    field.setAccessible(true);
//    field.set(problemsBusiness, "./pt-temp");
//    PowerMockito.when(problemsBusiness.getListProblemsSearchExport(problemsInsideDTO)).thenReturn(file);
//    Assert.assertEquals(file, null);
//
//  }

  @Test
  public void getListProblemsDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemsInsideDTO problemsInsideDTO = PowerMockito.mock(ProblemsInsideDTO.class);
    List<ProblemsInsideDTO> problemsInsideDTOS = Mockito.spy(ArrayList.class);
    problemsInsideDTOS.add(problemsInsideDTO);
    PowerMockito.when(problemsBusiness.getListProblemsDTO(problemsInsideDTO)).thenReturn(
        problemsInsideDTOS);
    List<ProblemsInsideDTO> problemsInsideDTOS1 = ptProblemsRepository.getListProblemsDTO(
        problemsInsideDTO);
    Assert.assertEquals(problemsInsideDTOS, problemsInsideDTOS1);
  }

//  @Test
//  public void getListProblemsSearch() {
//    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//    Datatable datatable = PowerMockito.mock(Datatable.class);
//    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
//    PowerMockito.when(ptProblemsRepository.getListProblemsSearch(problemsInsideDTO))
//        .thenReturn(datatable);
//    Datatable datatable1 = problemsBusiness.getListProblemsSearch(problemsInsideDTO);
//    Assert.assertEquals(datatable, datatable1);
//  }


  @Test
  public void getListProblemSearchCount() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Integer integer = PowerMockito.mock(Integer.class);
    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
    PowerMockito.when(ptProblemsRepository.getListProblemSearchCount(problemsInsideDTO))
        .thenReturn(integer);
    Integer integer1 = problemsBusiness.getListProblemSearchCount(problemsInsideDTO);
    Assert.assertEquals(integer, integer1);
  }

  @Test
  public void delete() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    Long problemId = 1L;
    PowerMockito.when(ptProblemsRepository.delete(problemId)).thenReturn(resultInSideDto);
    ApplicationContext context = Mockito.spy(ApplicationContext.class);
    setMockStatic(context);
    MessageSource messageSource = Mockito.spy(MessageSource.class);
    PowerMockito.when(SpringApplicationContext.bean(MessageSource.class)).thenReturn(messageSource);
    PowerMockito.when(messageSource.getMessage(anyString(), any(), any())).thenReturn("testTest");
    ResultInSideDto resultInSideDto1 = problemsBusiness.delete(problemId);
    Assert.assertEquals(resultInSideDto, resultInSideDto1);
  }

  @Test
  public void deleteListProblems() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
    List<ProblemsInsideDTO> problemsInsideDTOS = Mockito.spy(ArrayList.class);
    problemsInsideDTOS.add(problemsInsideDTO);
    PowerMockito.when(ptProblemsRepository.deleteListProblems(problemsInsideDTOS)).thenReturn(
        resultInSideDto);
    ResultInSideDto resultInSideDto1 = problemsBusiness.deleteListProblems(problemsInsideDTOS);
    Assert.assertEquals(resultInSideDto, resultInSideDto1);
  }

  @Test
  public void findProblemsById() {
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      ProblemsInsideDTO problemsInsideDTO = PowerMockito.mock(ProblemsInsideDTO.class);
      PowerMockito.when(ptProblemsRepository.findProblemsById(problemsInsideDTO.getProblemId()))
          .thenReturn(problemsInsideDTO);
      ProblemsInsideDTO problemsInsideDTO1 = problemsBusiness
          .findProblemsById(problemsInsideDTO.getProblemId());
      Assert.assertEquals(problemsInsideDTO, problemsInsideDTO1);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Test
  public void getProblemsChartData() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemsChartDTO problemsChartDTO = PowerMockito.mock(ProblemsChartDTO.class);
    List<ProblemsChartDTO> problemsChartDTOS = Mockito.spy(ArrayList.class);
    problemsChartDTOS.add(problemsChartDTO);
    String receiveUnitId = "2";
    PowerMockito.when(problemsBusiness.getProblemsChartData(receiveUnitId))
        .thenReturn(problemsChartDTOS);
    List<ProblemsChartDTO> problemsChartDTOS1 = ptProblemsRepository
        .getProblemsChartData(receiveUnitId);
    Assert.assertEquals(problemsChartDTOS, problemsChartDTOS1);
  }

  @Test
  public void getProblemsMonitor() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
    problemsInsideDTO.setPriorityId(1L);
    problemsInsideDTO.setUnitId("2");
    problemsInsideDTO.setFromDate("10/06/2019");
    problemsInsideDTO.setToDate("10/07/2019");
    problemsInsideDTO.setFindInSubUnit("1");
    problemsInsideDTO.setUnitType("vt");
    List<ProblemMonitorDTO> problemMonitorDTOS = new ArrayList<>();
    PowerMockito.when(problemsBusiness
        .getProblemsMonitor(problemsInsideDTO.getPriorityId().toString(),
            problemsInsideDTO.getUnitId(),
            problemsInsideDTO.getFromDate(), problemsInsideDTO.getToDate(),
            problemsInsideDTO.getFindInSubUnit(),
            problemsInsideDTO.getUnitType())).thenReturn(problemMonitorDTOS);
    List<ProblemMonitorDTO> problemMonitorDTOS1 = ptProblemsRepository
        .getProblemsMonitor(problemsInsideDTO.getPriorityId().toString(),
            problemsInsideDTO.getUnitId(),
            problemsInsideDTO.getFromDate(), problemsInsideDTO.getToDate(),
            problemsInsideDTO.getFindInSubUnit(),
            problemsInsideDTO.getUnitType());
    Assert.assertEquals(problemMonitorDTOS, problemMonitorDTOS1);
  }

  @Test
  public void getProblemsMobileUnitAll() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String receiveUnitId = "2";
    List<ProblemsMobileDTO> problemsMobileDTOS = new ArrayList<>();
    PowerMockito.when(problemsBusiness.getProblemsMobileUnitAll(receiveUnitId))
        .thenReturn(problemsMobileDTOS);
    List<ProblemsMobileDTO> problemsMobileDTOS1 = ptProblemsRepository
        .getProblemsMobileUnitAll(receiveUnitId);
    Assert.assertEquals(problemsMobileDTOS, problemsMobileDTOS1);
  }

  @Test
  public void getProblemsMobileUnit() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String receiveUnitId = "2";
    List<ProblemsMobileDTO> problemsMobileDTOS = new ArrayList<>();
    PowerMockito.when(problemsBusiness.getProblemsMobileUnit(receiveUnitId))
        .thenReturn(problemsMobileDTOS);
    List<ProblemsMobileDTO> problemsMobileDTOS1 = ptProblemsRepository
        .getProblemsMobileUnit(receiveUnitId);
    Assert.assertEquals(problemsMobileDTOS, problemsMobileDTOS1);
  }

  @Test
  public void getListProblemSearchDulidate_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    UserTokenGNOCSimple userTokenGNOC = Mockito.spy(UserTokenGNOCSimple.class);
    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
    problemsInsideDTO.setProblemName("PT_ACCESS_150924_184");
    problemsInsideDTO.setPriorityId(1L);
    problemsInsideDTO.setUnitId("2");
    problemsInsideDTO.setFromDate("10/06/2019");
    problemsInsideDTO.setToDate("10/07/2019");
    problemsInsideDTO.setFindInSubUnit("1");
    problemsInsideDTO.setUnitType("vt");
    problemsInsideDTO.setSortName("problemCode");
    problemsInsideDTO.setSortType("asc");
    problemsInsideDTO.setPage(1);
    problemsInsideDTO.setPageSize(1);
    problemsInsideDTO.setUserTokenGNOC(userTokenGNOC);
    ArrayList<String> lstIn = (ArrayList<String>) splitString(problemsInsideDTO.getProblemName());
    ArrayList<ProblemsInsideDTO> lstNotIn = new ArrayList<>();
    if (userTokenGNOC != null && StringUtils.isNotNullOrEmpty(userTokenGNOC.getUnitName())) {
      ProblemsInsideDTO pdto = new ProblemsInsideDTO();
      pdto.setProblemCode(userTokenGNOC.getUnitName());
      lstNotIn.add(pdto);
    }
    PowerMockito.when(ptProblemsRepository
        .getMaxRowDuplicate(anyString())).thenReturn(1l);

    PowerMockito.when(ptProblemsRepository
        .getListProblemSearchDulidates(problemsInsideDTO.getFromDate(),
            problemsInsideDTO.getToDate(),
            userTokenGNOC, lstIn, lstNotIn, problemsInsideDTO.getPage(),
            problemsInsideDTO.getPageSize(),
            problemsInsideDTO.getSortName(), problemsInsideDTO.getSortType()))
        .thenReturn(datatable);
    Datatable datatable1 = problemsBusiness.getListProblemSearchDulidate(problemsInsideDTO);
    Assert.assertEquals(datatable, datatable1);
  }

  @Test
  public void getListProblemSearchDulidate_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    Datatable datatable = problemsBusiness.getListProblemSearchDulidate(problemsInsideDTO);
    Datatable datatable1 = Mockito.spy(Datatable.class);
    Assert.assertEquals(datatable.getTotal(), datatable1.getTotal());
  }

  @Test
  public void getListProblemSearchDulidate_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserTokenGNOCSimple userTokenGNOC = new UserTokenGNOCSimple();
    userTokenGNOC.setUserId(1L);
    userTokenGNOC.setUserName("thanhlv12");
    userTokenGNOC.setFullName("le van thanh");
    userTokenGNOC.setUnitId(1L);
    userTokenGNOC.setRoleIDs(null);
    userTokenGNOC.setGroupLevel(1L);
    userTokenGNOC.setBelongToManyGroup(false);
    userTokenGNOC.setUnitCode("1");
    userTokenGNOC.setUnitName("vtnet");
    userTokenGNOC.setMobile("0123456789");
    userTokenGNOC.setLocale("84");
    userTokenGNOC.setOffset(1);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setUserTokenGNOC(userTokenGNOC);
    Datatable datatable = problemsBusiness.getListProblemSearchDulidate(problemsInsideDTO);
    Datatable datatable1 = Mockito.spy(Datatable.class);
    Assert.assertEquals(datatable.getTotal(), datatable1.getTotal());
  }

  @Test
  public void getListProblemSearchDulidate_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    UserTokenGNOCSimple userTokenGNOC = Mockito.spy(UserTokenGNOCSimple.class);
    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
    problemsInsideDTO.setUserTokenGNOC(userTokenGNOC);
    problemsInsideDTO.setProblemName("PT_ACCESS_150924_184");
    problemsInsideDTO.setPriorityId(1L);
    problemsInsideDTO.setUnitId("2");
    problemsInsideDTO.setFromDate("10/06/2019");
    problemsInsideDTO.setToDate("10/07/2019");
    problemsInsideDTO.setFindInSubUnit("1");
    problemsInsideDTO.setUnitType("vt");
    problemsInsideDTO.setSortName("problemCode");
    problemsInsideDTO.setSortType("asc");
    problemsInsideDTO.setPage(1);
    problemsInsideDTO.setPageSize(1);
    ArrayList<String> lstIn = (ArrayList<String>) splitString(problemsInsideDTO.getProblemName());
    ArrayList<ProblemsInsideDTO> lstNotIn = new ArrayList<>();
    if (userTokenGNOC != null && StringUtils.isNotNullOrEmpty(userTokenGNOC.getUnitName())) {
      ProblemsInsideDTO pdto = new ProblemsInsideDTO();
      pdto.setProblemCode(userTokenGNOC.getUnitName());
      lstNotIn.add(pdto);
    }
    PowerMockito.when(ptProblemsRepository
        .getMaxRowDuplicate(anyString())).thenReturn(1l);

    PowerMockito.when(ptProblemsRepository
        .getListProblemSearchDulidates(problemsInsideDTO.getFromDate(),
            problemsInsideDTO.getToDate(),
            userTokenGNOC, lstIn, lstNotIn, problemsInsideDTO.getPage(),
            problemsInsideDTO.getPageSize(),
            problemsInsideDTO.getSortName(), problemsInsideDTO.getSortType()))
        .thenReturn(datatable);
    Datatable datatable1 = problemsBusiness.getListProblemSearchDulidate(problemsInsideDTO);
    Assert.assertEquals(datatable, datatable1);
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


  private final static String PROBLEM_SEQ = "PROBLEM_SEQ";

  @Value("${application.upload.folder}")
  private String uploadFolder;

/*  @Test
  public void insertProblems_01() {
    try {
      List<MultipartFile> files = new ArrayList<>();
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
      problemsInsideDTO.setProblemId(184L);
      problemsInsideDTO.setProblemCode("PT_ACCESS_150924_184");
      problemsInsideDTO.setProblemName("PT_ACCESS_150924_184");
      problemsInsideDTO.setTypeId(12L);
      problemsInsideDTO.setSubCategoryId(290L);
      problemsInsideDTO.setPriorityId(14L);
      problemsInsideDTO.setImpactId(15L);
      problemsInsideDTO.setUrgencyId(1L);
      problemsInsideDTO.setProblemState("187");
      problemsInsideDTO.setAccessId(16L);
      problemsInsideDTO.setVendor("HUAWEI");
      problemsInsideDTO.setAffectedService("DATA, SMS");
      problemsInsideDTO.setLocation(" / Việt Nam / Khu vực 1 / Hà Nội");
      problemsInsideDTO.setLocationId(292L);
      problemsInsideDTO.setCreateUserId(5L);
      problemsInsideDTO.setCreateUnitId(415874L);
      problemsInsideDTO.setCreateUserName("nims_dev");
      problemsInsideDTO.setCreateUnitName("Mức Trung tâm");
      problemsInsideDTO.setCreateUserPhone("0988698713");
      problemsInsideDTO.setInsertSource("PT");
      problemsInsideDTO.setRelatedKedb("KEDB_ACCESS_150922_62");
      problemsInsideDTO.setReceiveUnitId(12L);
      problemsInsideDTO.setReceiveUserId(4L);
      problemsInsideDTO.setRcaType(232L);
      problemsInsideDTO.setCategorization(31L);
      problemsInsideDTO.setStateCode("PT_OPEN");
      problemsInsideDTO.setPmGroup(1L);
      UserToken userToken = Mockito.spy(UserToken.class);
      userToken.setDeptId(1L);
      userToken.setUserID(999999l);
      userToken.setUserName("thanhlv12");
      PowerMockito.mockStatic(TicketProvider.class);
      PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
      PowerMockito.when(ptProblemsRepository.getSeqTableProblems(PROBLEM_SEQ)).thenReturn(
          "999");
      ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
      resultInSideDto.setKey(RESULT.SUCCESS);
      PowerMockito.when(ptProblemsRepository.add(any())).thenReturn(resultInSideDto);
      ApplicationContext context = Mockito.spy(ApplicationContext.class);
      setMockStatic(context);
      MessageSource messageSource = Mockito.spy(MessageSource.class);
      PowerMockito.when(SpringApplicationContext.bean(MessageSource.class))
          .thenReturn(messageSource);
      PowerMockito.when(messageSource.getMessage(anyString(), any(), any())).thenReturn("testTest");
      ResultInSideDto result = problemsBusiness.insertProblems(files, problemsInsideDTO);
      Assert.assertEquals(result.getKey(), RESULT.ERROR);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }*/

 /* @Test
  public void insertProblems_02() throws Exception {
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      List<MultipartFile> files = Mockito.spy(ArrayList.class);
      ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
      problemsInsideDTO.setProblemCode("PT_ACCESS_150924_184");
      problemsInsideDTO.setProblemName("PT_ACCESS_150924_184");
      problemsInsideDTO.setStateCode("PT_REDO");
      problemsInsideDTO.setWorklog("Vấn đề (Pt) trùng: PT_DD_VT_Dd_Bts_190418_2821");
      problemsInsideDTO.setAttachFileList("report_file");
      problemsInsideDTO.setAffectedNode("1,2");
      problemsInsideDTO.setNodeName("Test,Test");
      problemsInsideDTO.setNodeIp(".1,.1");
      Long id = PowerMockito.mock(Long.class);
      PowerMockito.when(ptProblemsRepository.getSeqTableProblems(PROBLEM_SEQ)).thenReturn(
          String.valueOf(id));
      ResultInSideDto resultInSideDto2 = Mockito.spy(ResultInSideDto.class);
      resultInSideDto2.setKey(RESULT.SUCCESS);
      PowerMockito.when(ptProblemsRepository.add(problemsInsideDTO)).thenReturn(resultInSideDto2);
      ProblemFilesDTO problemFilesDTO = Mockito.spy(ProblemFilesDTO.class);
      List<ProblemFilesDTO> problemFilesDTOS = Mockito.spy(ArrayList.class);
      problemFilesDTOS.add(problemFilesDTO);
      PowerMockito.when(problemNodeBusiness.insertOrUpdateListProblemNode(anyList()))
          .thenReturn(RESULT.SUCCESS);
      ProblemWorklogDTO problemWorklogDTO = Mockito.spy(ProblemWorklogDTO.class);
      PowerMockito.when(problemWorklogBusiness.onInsert(problemWorklogDTO)).thenReturn(
          resultInSideDto2);
      PowerMockito.when(problemActionLogsBusiness.insertProblemActionLogs(any()))
          .thenReturn(resultInSideDto2);
      UserToken userToken = Mockito.spy(UserToken.class);
      userToken.setDeptId(1L);
      userToken.setUserID(999999l);
      userToken.setUserName("thanhlv12");
      PowerMockito.mockStatic(TicketProvider.class);
      PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
      List<InfraDeviceDTO> infraDeviceDTOS = Mockito.spy(ArrayList.class);
      InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
      infraDeviceDTO.setDeviceCode("testCode");
      infraDeviceDTO.setNationCode("VTN");
      infraDeviceDTOS.add(infraDeviceDTO);
      PowerMockito.when(ptProblemsRepository.getInfraDeviceDTOSByListCode(anyList()))
          .thenReturn(infraDeviceDTOS);
      Field field = ProblemsBusinessImpl.class.getDeclaredField("tempFolder");
      field.setAccessible(true);
      field.set(problemsBusiness, "./pt-upload");
      PowerMockito.when(gnocFileRepository.saveListGnocFile(anyString(), anyLong(), anyList())).thenReturn(resultInSideDto2);
      UnitDTO unitToken = Mockito.spy(UnitDTO.class);
      unitToken.setUnitId(1L);
      unitToken.setUnitName("A");
      PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
      ResultInSideDto resultInSideDto1 = problemsBusiness.insertProblems(files, problemsInsideDTO);
      Assert.assertEquals(resultInSideDto1, resultInSideDto2);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }*/

  public String convertLongValue(Long value) {
    if (value == null) {
      return null;
    }
    return String.valueOf(value);
  }

  @Test
  public void insertOrUpdateListProblems() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    List<ProblemsInsideDTO> problemsInsideDTOS = new ArrayList<>();
    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
    problemsInsideDTOS.add(problemsInsideDTO);
    PowerMockito.when(ptProblemsRepository.insertOrUpdateListProblems(problemsInsideDTOS))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = problemsBusiness.insertOrUpdateListProblems(
        problemsInsideDTOS);
    Assert.assertEquals(resultInSideDto, resultInSideDto1);
  }

  @Test
  public void testUpdateListProblems_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ProblemsInsideDTO> lstProblemsInsideDTOS = Mockito.spy(ArrayList.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("SUCCESS");
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto1);
    PowerMockito.when(problemActionLogsBusiness.insertOrUpdateListProblemActionLogs(anyList()))
        .thenReturn(RESULT.SUCCESS);
    ResultInSideDto resultInSideDto2 = problemsBusiness.updateListProblems(lstProblemsInsideDTOS,
        problemsInsideDTO);
    Assert.assertEquals(resultInSideDto1, resultInSideDto2);
  }

  @Test
  public void testUpdateListProblems_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ProblemsInsideDTO> lstProblemsInsideDTOS = Mockito.spy(ArrayList.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("");
    lstProblemsInsideDTOS.add(problemsInsideDTO);
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto1);
    ResultInSideDto resultInSideDtoTmp = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoTmp.setKey(RESULT.ERROR);
    PowerMockito.when(
        problemsBusiness.insertOrUpdateListProblems(anyList())
    ).thenReturn(resultInSideDtoTmp);
    PowerMockito.when(problemActionLogsBusiness.insertOrUpdateListProblemActionLogs(anyList()))
        .thenReturn(RESULT.SUCCESS);
    ResultInSideDto resultInSideDto2 = problemsBusiness.updateListProblems(lstProblemsInsideDTOS,
        problemsInsideDTO);
    Assert.assertEquals(resultInSideDto1, resultInSideDto2);
  }

  @Test
  public void testUpdateListProblems_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ProblemsInsideDTO> lstProblemsInsideDTOS = Mockito.spy(ArrayList.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.SUCCESS);
    lstProblemsInsideDTOS.add(problemsInsideDTO);
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto1);
    ResultInSideDto resultInSideDtoTmp = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoTmp.setKey(RESULT.ERROR);
    PowerMockito.when(
        problemsBusiness.insertOrUpdateListProblems(anyList())
    ).thenReturn(resultInSideDtoTmp);
    PowerMockito.when(problemActionLogsBusiness.insertOrUpdateListProblemActionLogs(anyList()))
        .thenReturn(RESULT.SUCCESS);
    ResultInSideDto resultInSideDto2 = problemsBusiness.updateListProblems(lstProblemsInsideDTOS,
        problemsInsideDTO);
    Assert.assertEquals(resultInSideDto1, resultInSideDto2);
  }

  @Test
  public void testUpdateListProblems_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ProblemsInsideDTO> lstProblemsInsideDTOS = Mockito.spy(ArrayList.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemName("test");
    problemsInsideDTO.setWorklogNew("Test");
    problemsInsideDTO.setUserUpdateId("999999");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.SUCCESS);
    lstProblemsInsideDTOS.add(problemsInsideDTO);
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto1);
    ResultInSideDto resultInSideDtoTmp = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoTmp.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        problemsBusiness.insertOrUpdateListProblems(anyList())
    ).thenReturn(resultInSideDtoTmp);
    PowerMockito.when(problemActionLogsBusiness.insertOrUpdateListProblemActionLogs(anyList()))
        .thenReturn(RESULT.SUCCESS);
    ResultInSideDto resultInSideDto2 = problemsBusiness.updateListProblems(lstProblemsInsideDTOS,
        problemsInsideDTO);
    Assert.assertEquals(resultInSideDto1, resultInSideDto2);
  }

  @Test
  public void testUpdateListProblems_05() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("SUCCESS");
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto1);
    List<ProblemActionLogsDTO> lstActionLogsDTOs = Mockito.spy(ArrayList.class);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    lstActionLogsDTOs.add(problemActionLogsDTO);
    PowerMockito.when(problemActionLogsBusiness.insertOrUpdateListProblemActionLogs(anyList()))
        .thenReturn(RESULT.SUCCESS);
    List<ProblemsInsideDTO> problemsInsideDTOS = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto2 = problemsBusiness.updateListProblems(problemsInsideDTOS,
        problemsInsideDTO);
    Assert.assertEquals(resultInSideDto1, resultInSideDto2);
  }

  @Test
  public void updateProblems_01() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
      ProblemsInsideDTO problemsInsideDTO1 = Mockito.spy(ProblemsInsideDTO.class);
      problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTO1);
      PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto);
      UserToken userToken = Mockito.spy(UserToken.class);
      userToken.setDeptId(1L);
      userToken.setUserID(999999l);
      userToken.setUserName("thanhlv12");
      PowerMockito.mockStatic(TicketProvider.class);
      PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
      resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
      Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

  }

  @Test
  public void updateProblems_02() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
      ProblemsInsideDTO problemsInsideDTO1 = Mockito.spy(ProblemsInsideDTO.class);
      problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTO1);
      resultInSideDto.setKey(RESULT.SUCCESS);
      PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto);
      problemsInsideDTO.setWorklogNew("vtnet");
      UserToken userToken = Mockito.spy(UserToken.class);
      userToken.setDeptId(1L);
      userToken.setUserID(999999l);
      userToken.setUserName("thanhlv12");
      PowerMockito.mockStatic(TicketProvider.class);
      PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
      resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
      Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }


  @Test
  public void updateProblemsNew_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto1);
    resultInSideDto1 = problemsBusiness.updateProblemsNew(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.ERROR);
  }

  @Test
  public void updateProblemsNew_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setUserUpdateId("999999");
    problemsInsideDTO.setUnitUpdateId("999999");
    resultInSideDto1.setKey(RESULT.SUCCESS);
    problemsInsideDTO.setWorklogNew("update");
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto1);
    resultInSideDto1 = problemsBusiness.updateProblemsNew(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void updateProblemsNew_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setUserUpdateId("999999");
    problemsInsideDTO.setUnitUpdateId("999999");
    resultInSideDto1.setKey(RESULT.SUCCESS);
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto1);
    problemsInsideDTO.setProblemsNewDTO(problemsInsideDTO);
    List<String> s = Mockito.spy(ArrayList.class);
    s.add("1");
    PowerMockito.when(ptProblemsRepository.getSequenseProblems(anyString(), anyInt()))
        .thenReturn(
            s);
    List<String> s1 = Mockito.spy(ArrayList.class);
    s1.add("updateNewFile");
    problemsInsideDTO.setAttachFileList("updateNewFile");
    PowerMockito.when(ptProblemsRepository.getSeqTableProblems(anyString())).thenReturn("1");
    PowerMockito.when(problemsBusiness.add(problemsInsideDTO)).thenReturn(resultInSideDto1);
    resultInSideDto1 = problemsBusiness.updateProblemsNew(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void updateProblemsNew_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    resultInSideDto1.setKey(RESULT.SUCCESS);
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto1);
    problemsInsideDTO.setProblemsNewDTO(problemsInsideDTO);
    List<String> s = Mockito.spy(ArrayList.class);
    s.add("1");
    PowerMockito.when(ptProblemsRepository.getSequenseProblems(anyString(), anyInt()))
        .thenReturn(
            s);
    List<String> s1 = Mockito.spy(ArrayList.class);
    s1.add("updateNewFile");
    PowerMockito.when(ptProblemsRepository.getSeqTableProblems(anyString())).thenReturn("1");
    PowerMockito.when(problemsBusiness.add(problemsInsideDTO)).thenReturn(resultInSideDto1);
    resultInSideDto1 = problemsBusiness.updateProblemsNew(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.ERROR);
  }

  @Test
  public void updateProblemsNew_05() {
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
      resultInSideDto1.setKey(RESULT.SUCCESS);
      PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto1);
      problemsInsideDTO.setProblemsNewDTO(problemsInsideDTO);
      List<String> s = Mockito.spy(ArrayList.class);
      s.add("1");
      List<String> s1 = Mockito.spy(ArrayList.class);
      s1.add("updateNewFile");
      problemsInsideDTO.setWorklog("vtnet");
      problemsInsideDTO.setProblemId(184L);
      problemsInsideDTO.setUserUpdateId("1");
      PowerMockito.when(ptProblemsRepository.getSeqTableProblems(anyString())).thenReturn("1");
      PowerMockito.when(problemsBusiness.add(problemsInsideDTO)).thenReturn(resultInSideDto1);
      resultInSideDto1 = problemsBusiness.updateProblemsNew(problemsInsideDTO);
      Assert.assertEquals(resultInSideDto1.getKey(), RESULT.ERROR);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  /*@Test
  public void getTransitionStatus() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable1 = PowerMockito.mock(Datatable.class);
    ProblemsInsideDTO problemsDTO = new ProblemsInsideDTO();
    PowerMockito.when(ptProblemsRepository.getTransitionStatus(problemsDTO)).thenReturn(
        (List<CatItemDTO>) datatable1);
    Datatable datatable11 = problemsBusiness.getTransitionStatus(problemsDTO);
    Assert.assertEquals(datatable1, datatable11);
  }*/

  @Test
  public void getListProblemsByCondition() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ProblemsInsideDTO> lstProblems = Mockito.spy(ArrayList.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setPtRelatedType(1L);
    lstProblems.add(problemsInsideDTO);
    ConditionBean conditionBean = Mockito.spy(ConditionBean.class);
    conditionBean.setType("TYPE_DATE");
    conditionBean.setOperator("NAME");
    conditionBean.setValue("vtnet");
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    lstCondition.add(conditionBean);
    PowerMockito.when(ptProblemsRepository
        .getListProblemsByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstProblems);
    List<ProblemsInsideDTO> problemsInsideDTOList;
    problemsInsideDTOList = problemsBusiness
        .getListProblemsByCondition(lstCondition, 1, 5, "asc", "problemId");
    Assert.assertEquals(problemsInsideDTOList.size(), lstProblems.size());
  }


  @Test
  public void getListPtRelated_01() {
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      List<ProblemsInsideDTO> lstProblems = Mockito.spy(ArrayList.class);
      ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
      problemsInsideDTO.setPtRelatedType(1L);
      problemsInsideDTO.setProblemCode("184");
      mapPtRelatedType.put(1L, "PT_PARENT");
      lstProblems.add(problemsInsideDTO);
      ConditionBean conditionBean = Mockito.spy(ConditionBean.class);
      conditionBean.setType("TYPE_DATE");
      conditionBean.setOperator("NAME");
      conditionBean.setValue("vtnet");
      List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
      lstCondition.add(conditionBean);
      PowerMockito.when(mapPtRelatedType.get(problemsInsideDTO.getPtRelatedType()))
          .thenReturn("PT_PARENT");
      PowerMockito.when(problemsBusiness.getListPtRelated(problemsInsideDTO))
          .thenReturn(lstProblems);
      Assert.assertEquals(lstProblems.size(), 1);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Test
  public void getListPtRelated_02() {
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      List<ProblemsInsideDTO> lstProblems = Mockito.spy(ArrayList.class);
      ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
      problemsInsideDTO.setPtRelatedType(1L);
      problemsInsideDTO.setRelatedPt("itsol");
      mapPtRelatedType.put(1L, "PT_CHILDREN");
      lstProblems.add(problemsInsideDTO);
      ConditionBean conditionBean = Mockito.spy(ConditionBean.class);
      conditionBean.setType("TYPE_DATE");
      conditionBean.setOperator("NAME");
      conditionBean.setValue("vtnet");
      List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
      lstCondition.add(conditionBean);
      PowerMockito.when(mapPtRelatedType.get(problemsInsideDTO.getPtRelatedType()))
          .thenReturn("PT_CHILDREN");
      PowerMockito.when(problemsBusiness.getListPtRelated(problemsInsideDTO))
          .thenReturn(lstProblems);
      Assert.assertEquals(lstProblems.size(), 1);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }


  @Test
  public void getListChatUsers() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
    List<UsersInsideDto> usersInsideDtos = new ArrayList<>();
    PowerMockito.when(ptProblemsRepository.getListChatUsers(problemsInsideDTO)).thenReturn(
        usersInsideDtos);
    List<UsersInsideDto> usersInsideDtos1 = problemsBusiness.getListChatUsers(problemsInsideDTO);
    Assert.assertEquals(usersInsideDtos, usersInsideDtos1);
  }

  @Test
  public void sendChatListUsers_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemCode("184");
    problemsInsideDTO.setIsChat(1L);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.ERROR);
    resultInSideDto1.setMessage("common.update.fail");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);

    ApplicationContext context1 = Mockito.spy(ApplicationContext.class);
    setMockStatic(context1);
    MessageSource messageSource = Mockito.spy(MessageSource.class);
    PowerMockito.when(SpringApplicationContext.bean(MessageSource.class)).thenReturn(messageSource);
    PowerMockito.when(messageSource.getMessage(anyString(), any(), any())).thenReturn("testTest");

    ApplicationContext context = Mockito.spy(ApplicationContext.class);
    setMockStatic(context);
    WSChatPort chatPort = PowerMockito.mock(WSChatPort.class);
    setMockSingleton(chatPort);
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.when(PassProtector.decrypt(anyString(), anyString())).thenReturn("999");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    try {
      resultInSideDto1 = problemsBusiness.sendChatListUsers(problemsInsideDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.ERROR);
  }

  @Test
  public void sendChatListUsers_02() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDtos.add(usersInsideDto);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemCode("184");
    problemsInsideDTO.setUsersInsideDtos(usersInsideDtos);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.ERROR);
    resultInSideDto1.setMessage("common.update.fail");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    ApplicationContext context = Mockito.spy(ApplicationContext.class);
    setMockStatic(context);
    WSChatPort chatPort = PowerMockito.mock(WSChatPort.class);
    setMockSingleton(chatPort);
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.when(PassProtector.decrypt(anyString(), anyString())).thenReturn("999");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    try {
      resultInSideDto1 = problemsBusiness.sendChatListUsers(problemsInsideDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.ERROR);
  }

  @Test
  public void loadUserSupportGroup_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable;
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setTypeId(1L);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("10");
    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(catItemDTO);
    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), anyString()))
        .thenReturn(lst);
    CatItemDTO catItemDTO1 = Mockito.spy(CatItemDTO.class);
    catItemDTO1.setDescription("vtnet");
    datatable = problemsBusiness.loadUserSupportGroup(problemsInsideDTO);
    Assert.assertEquals(datatable.getTotal(), 0);
  }

  static void setMockStatic(ApplicationContext mockApp) {
    try {
      Field instanceTime = SpringApplicationContext.class.getDeclaredField("context");
      instanceTime.setAccessible(true);
      instanceTime.set(instanceTime, mockApp);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  static void setMockSingleton(WSChatPort wsChatPort) {
    try {
      Method createConnect = Method.class.getDeclaredMethod("createConnect");
      createConnect.setAccessible(true);
    } catch (Exception e) {
      log.error(e.getMessage(), e);

    }
  }
}
