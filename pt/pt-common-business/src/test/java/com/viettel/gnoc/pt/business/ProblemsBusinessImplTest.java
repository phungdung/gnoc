package com.viettel.gnoc.pt.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.business.RoleUserBusiness;
import com.viettel.gnoc.commons.business.RolesBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.SpringApplicationContext;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RoleUserDTO;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.incident.provider.WSChatPort;
import com.viettel.gnoc.commons.incident.provider.WSChatPortFactory;
import com.viettel.gnoc.commons.proxy.OdServiceProxy;
import com.viettel.gnoc.commons.proxy.SrServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepositoryImpl;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.RoleUserRepository;
import com.viettel.gnoc.commons.repository.RolesRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.PROBLEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemActionLogsDTO;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
import com.viettel.gnoc.pt.dto.ProblemMonitorDTO;
import com.viettel.gnoc.pt.dto.ProblemNodeDTO;
import com.viettel.gnoc.pt.dto.ProblemWorklogDTO;
import com.viettel.gnoc.pt.dto.ProblemsChartDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemsMobileDTO;
import com.viettel.gnoc.pt.repository.ProblemConfigTimeRepository;
import com.viettel.gnoc.pt.repository.PtProblemsRepository;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;
import vn.viettel.smartoffice.GroupResponse;
@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({ProblemsBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, ConditionBeanUtil.class, WSChatPort.class, WSChatPortFactory.class,
    PassProtector.class, CommonExport.class, PassTranformer.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*",
    "jdk.internal.reflect.*"})
public class ProblemsBusinessImplTest {

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
  RoleUserBusiness roleUserBusiness;
  @Mock
  RolesBusiness rolesBusiness;

  @Mock
  UnitBusiness unitBusiness;

  @Mock
  MessagesBusiness messagesBusiness;


  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  RoleUserRepository roleUserRepository;

  @Mock
  WSChatPort wsChatPort;

  @Mock
  RolesRepository rolesRepository;

  @Mock
  UserBusiness userBusiness;

  @Mock
  UserRepository userRepository;

  @Mock
  ProblemConfigTimeRepository problemConfigTimeRepository;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  OdServiceProxy odServiceProxy;

  @Mock
  SrServiceProxy srServiceProxy;

  @Test
  public void getListProblemDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
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
    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
    List<ProblemsInsideDTO> problemsInsideDTOS = Mockito.spy(ArrayList.class);
    problemsInsideDTOS.add(problemsInsideDTO);
    PowerMockito.when(problemsBusiness.getListProblemDTOForTT(problemsInsideDTO))
        .thenReturn(problemsInsideDTOS);
    List<ProblemsInsideDTO> problemsInsideDTOS1 = ptProblemsRepository.getListProblemDTO(
        problemsInsideDTO);
    Assert.assertEquals(problemsInsideDTOS, problemsInsideDTOS1);
  }


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

  @Test
  public void getListProblemsSearch() {
    Logger logger = PowerMockito.mock(Logger.class);
    Datatable datatable = PowerMockito.mock(Datatable.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(69L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    List<RolesDTO> rolesDTOS = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("96");
    rolesDTO.setRoleName("CCCCCCC");
    rolesDTOS.add(rolesDTO);
    List<String> lstRoleId = Mockito.spy(ArrayList.class);
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(69),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("96");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    PowerMockito.when(roleUserBusiness
        .getListRoleUserByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRoleUser);
    PowerMockito.when(rolesBusiness
        .getListRolesByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(rolesDTOS);

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setAffectedNode("GHI");
    problemsInsideDTO.setContent("koko");
    PowerMockito.when(ptProblemsRepository.getListProblemsSearch(any())).thenReturn(datatable);
    List<ProblemsInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(problemsInsideDTO);

    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(22L);
    lstUnitAll.add(unitDTO);
    PowerMockito.when(unitBusiness.getListUnitByLevel("")).thenReturn(lstUnitAll);

    ConditionBean condition2 = new ConditionBean("roleCode", "PM_",
        Constants.NAME_LIKE, Constants.STRING);
    List<RolesDTO> lstRolePM = Mockito.spy(ArrayList.class);
    lstRolePM.add(rolesDTO);
    PowerMockito.when(rolesBusiness
        .getListRolesByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRolePM);
//    PowerMockito.when(problemsBusiness.searchProblemTicket((List<ProblemsInsideDTO>)datatable.getData())).thenReturn(lst);
    Datatable datatable1 = problemsBusiness.getListProblemsSearch(problemsInsideDTO);
    Assert.assertEquals(datatable, datatable1);
  }

  @Test
  public void getListProblemSearch() {
    Logger logger = PowerMockito.mock(Logger.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setContent("aaaaa");
    List<ProblemsInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(problemsInsideDTO);
    PowerMockito.when(ptProblemsRepository.getListProblemSearch(any())).thenReturn(list);
    List<ProblemsInsideDTO> problemsInsideDTOS = problemsBusiness
        .getListProblemSearch(problemsInsideDTO);
    Assert.assertEquals(problemsInsideDTOS.size(), list.size());
  }

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

  @Test
  public void testInsertProblems_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
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

    problemsInsideDTO.setCreatedTime(new Date());
    problemsInsideDTO.setLastUpdateTime(new Date());
    problemsInsideDTO.setAssignedTime(new Date());
    problemsInsideDTO.setEsRcaTime(new Date());
    problemsInsideDTO.setEsWaTime(new Date());
    problemsInsideDTO.setEsSlTime(new Date());
    problemsInsideDTO.setStartedTime(new Date());
    problemsInsideDTO.setEndedTime(new Date());
    problemsInsideDTO.setRcaFoundTime(new Date());
    problemsInsideDTO.setWaFoundTime(new Date());
    problemsInsideDTO.setSlFoundTime(new Date());
    problemsInsideDTO.setClosedTime(new Date());
    problemsInsideDTO.setDelayTime(new Date());
    problemsInsideDTO.setDeferredTime(new Date());
    problemsInsideDTO.setInsertSource("NOC");
    problemsInsideDTO.setDescription("1");

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(88L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0377198479");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(ptProblemsRepository.getSeqTableProblems(PROBLEM_SEQ)).thenReturn("999");
    PowerMockito.when(ptProblemsRepository.add(any())).thenReturn(resultInSideDto);
    PowerMockito.when(unitRepository.findUnitById(88L)).thenReturn(unitToken);

    ProblemNodeDTO problemNodeDTO = Mockito.spy(ProblemNodeDTO.class);
    problemNodeDTO.setNodeName("abcfgfgfg");
    List<ProblemNodeDTO> lstProblemNodeDTOs = Mockito.spy(ArrayList.class);
    lstProblemNodeDTOs.add(problemNodeDTO);

    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    problemActionLogsDTO.setCreaterUserName("thanhlv12");
    PowerMockito.when(problemActionLogsBusiness.insertProblemActionLogs(any()))
        .thenReturn(resultInSideDto);
//    PowerMockito.when(problemWorklogBusiness.onInsert(any())).thenReturn(resultInSideDto);
    PowerMockito.when(problemNodeBusiness.insertOrUpdateListProblemNode(anyList()))
        .thenReturn(RESULT.SUCCESS);
    ApplicationContext context = Mockito.spy(ApplicationContext.class);
    setMockStatic(context);
    MessageSource messageSource = Mockito.spy(MessageSource.class);
    PowerMockito.when(SpringApplicationContext.bean(MessageSource.class))
        .thenReturn(messageSource);
    String content = "ptMngt.editpt.createdPT1";
    PowerMockito.when(messagesBusiness.insertSMSMessageForPm(content, "1", problemsInsideDTO))
        .thenReturn(resultInSideDto);
    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(uploadFile);

    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(
        FileUtils
            .saveFtpFile(anyString(), anyInt(), anyString(),
                anyString(), anyString(), anyString(),
                any(), any())
    ).thenReturn("abc");
    PowerMockito.when(
        FileUtils
            .saveUploadFile(anyString(), any(), anyString(), any())
    ).thenReturn("abc");

    PowerMockito.when(ptProblemsRepository.insertProblemFiles(any())).thenReturn(resultInSideDto);
    CatItemDTO itemDTO = Mockito.spy(CatItemDTO.class);
    itemDTO.setItemId(1L);
    itemDTO.setItemCode("1");
    itemDTO.setItemName("1");
    itemDTO.setCategoryCode("PT_TYPE");
    itemDTO.setParentItemId(1L);
    List<CatItemDTO> lstItemDTO = Mockito.spy(ArrayList.class);
    lstItemDTO.add(itemDTO);

    CatItemDTO itemDTO2 = Mockito.spy(CatItemDTO.class);
    itemDTO2.setItemId(1L);
    itemDTO2.setItemCode("1");
    itemDTO2.setItemName("1");
    itemDTO2.setCategoryCode("PT_PRIORITY");
    itemDTO.setParentItemId(1L);
    List<CatItemDTO> lstItemDTO2 = Mockito.spy(ArrayList.class);
    lstItemDTO2.add(itemDTO2);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstItemDTO).thenReturn(lstItemDTO2).thenReturn(lstItemDTO);

    UsersInsideDto u = Mockito.spy(UsersInsideDto.class);
    u.setUserId(999999L);
    u.setUnitId(413314L);
    PowerMockito.when(userBusiness.getUserDTOByUserNameInnerJoint(anyString())).thenReturn(u);

    List<UnitDTO> unit = Mockito.spy(ArrayList.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(413314L);
    unit.add(unitDTO);
    PowerMockito.when(unitRepository.getUnitByUnitDTO(any())).thenReturn(unit);

    PowerMockito.when(catLocationBusiness.getLocationByCode(anyString(), any(), any())).thenReturn(new CatLocationDTO());
    ResultInSideDto result = problemsBusiness.insertProblems(files, problemsInsideDTO);
    Assert.assertEquals(result.getKey(), RESULT.SUCCESS);
  }

  public String convertLongValue(Long value) {
    if (value == null) {
      return null;
    }
    return String.valueOf(value);
  }

  @Test
  public void insertOrUpdateListProblems() {
    Logger logger = PowerMockito.mock(Logger.class);
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
    List<ProblemsInsideDTO> lstProblemsInsideDTOS = Mockito.spy(ArrayList.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setPmGroup(1L);
    problemsInsideDTO.setUnitUpdateId("12");
    problemsInsideDTO.setUnitUpdateName("cvcv");
    problemsInsideDTO.setUserUpdateId("21");
    problemsInsideDTO.setUserUpdateName("thanhlv12");
    lstProblemsInsideDTOS.add(problemsInsideDTO);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.ERROR);
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto1);
    ResultInSideDto resultInSideDto2 = problemsBusiness.updateListProblems(lstProblemsInsideDTOS,
        problemsInsideDTO);
    Assert.assertEquals(resultInSideDto2.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void testUpdateListProblems_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    List<ProblemsInsideDTO> lstProblemsInsideDTOS = Mockito.spy(ArrayList.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setPmGroup(1L);
    problemsInsideDTO.setUnitUpdateId("12");
    problemsInsideDTO.setUnitUpdateName("cvcv");
    problemsInsideDTO.setUserUpdateId("21");
    problemsInsideDTO.setUserUpdateName("thanhlv12");
    lstProblemsInsideDTOS.add(problemsInsideDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDtoTmp = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoTmp.setKey(RESULT.ERROR);
    PowerMockito.when(problemsBusiness.insertOrUpdateListProblems(anyList()))
        .thenReturn(resultInSideDtoTmp);

    ResultInSideDto resultInSideDto2 = problemsBusiness.updateListProblems(lstProblemsInsideDTOS,
        problemsInsideDTO);
    Assert.assertEquals(resultInSideDtoTmp.getKey(), resultInSideDto2.getKey());
  }

  @Test
  public void testUpdateListProblems_03() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    List<ProblemsInsideDTO> lstProblemsInsideDTOS = Mockito.spy(ArrayList.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setPmGroup(1L);
    problemsInsideDTO.setUnitUpdateId("12");
    problemsInsideDTO.setUnitUpdateName("cvcv");
    problemsInsideDTO.setUserUpdateId("21");
    problemsInsideDTO.setUserUpdateName("thanhlv12");
    problemsInsideDTO.setContent("cbvnmb");
    problemsInsideDTO.setStateName("kkkk");
    problemsInsideDTO.setProblemId(9L);
    problemsInsideDTO.setWorklogNew("jujuju");

    lstProblemsInsideDTOS.add(problemsInsideDTO);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    problemActionLogsDTO.setContent("cbvnmb");
    List<ProblemActionLogsDTO> lstActionLogsDTOs = Mockito.spy(ArrayList.class);
    lstActionLogsDTOs.add(problemActionLogsDTO);

    ProblemWorklogDTO problemWorklogDTO = Mockito.spy(ProblemWorklogDTO.class);
    problemWorklogDTO.setSortName("acc");
    List<ProblemWorklogDTO> lstWorklogDTOs = Mockito.spy(ArrayList.class);
    lstWorklogDTOs.add(problemWorklogDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDtoTmp = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoTmp.setKey(RESULT.SUCCESS);
    PowerMockito.when(problemsBusiness.insertOrUpdateListProblems(anyList()))
        .thenReturn(resultInSideDtoTmp);
    PowerMockito.when(problemWorklogBusiness.insertOrUpdateListProblemWorklog(anyList()))
        .thenReturn(RESULT.SUCCESS);
    ResultInSideDto resultInSideDto2 = problemsBusiness.updateListProblems(lstProblemsInsideDTOS,
        problemsInsideDTO);
    Assert.assertEquals(resultInSideDtoTmp.getKey(), resultInSideDto2.getKey());
  }

  @Test
  public void testUpdateProblems_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("b");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("d");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_OPEN");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("b");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("d");
    problemsInsideDTO.setUserUpdateName("d");
    // Du thao --> cho tiep nhan
    problemsInsideDTO.setStateCode("PT_UNASSIGNED");

    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
  }

  @Test
  public void testUpdateProblems_02() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_OPEN");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setStateCode("PT_REJECTED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    PowerMockito.when(roleUserBusiness
        .getListRoleUserByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRoleUser);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);
    PowerMockito.when(rolesBusiness
        .getListRolesByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRole);
    ///////getListRolePmByUser////
    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    PowerMockito.when(problemActionLogsBusiness.insertProblemActionLogs(problemActionLogsDTO))
        .thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_03() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_OPEN");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setStateCode("PT_OPEN_2");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userBusiness
        .getListUsersByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstUser);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_04() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_REJECTED");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setPmGroup(66L);
    problemsInsideDTO.setStateCode("PT_OPEN");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_05() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_DEFERRED");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setStateCode("PT_UNASSIGNED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_06() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_REQ_DEFERRED");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setStateCode("PT_UNASSIGNED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_07() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_UNASSIGNED");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_QUEUED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_08() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_DEFERRED");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_QUEUED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_09() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_ROOT_CAUSE_PROPOSAL");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_QUEUED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_10() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_REQ_DEFERRED");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_QUEUED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_11() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_QUEUED");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_REQ_DEFERRED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_12() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_QUEUED");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_DEFERRED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_13() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_WORKARROUND_PROPOSAL");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_DIAGNOSED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_14() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_WA_FOUND");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_DIAGNOSED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_15() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_SL_FOUND");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_DIAGNOSED");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_16() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_SL_FOUND");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_WA_FOUND");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_17() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_SOLUTION_PROPOSAL");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_WA_IMPL");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_18() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_SL_FOUND");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_SOLUTION_PROPOSAL");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_19() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_SOLUTION_PROPOSAL");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_SL_FOUND");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_20() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_SOLUTION_PROPOSAL");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_SL_IMPL");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_21() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_SL_IMPL");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("ROOT_CAUSE_PROPOSAL");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateProblems_22() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(69L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    ProblemsInsideDTO problemsInsideDTOOld = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTOOld.setInsertKedb("YES");
    problemsInsideDTOOld.setUnitUpdateId("1");
    problemsInsideDTOOld.setUnitUpdateName("c");
    problemsInsideDTOOld.setUserUpdateId("1");
    problemsInsideDTOOld.setUserUpdateName("d");
    problemsInsideDTOOld.setStateCode("PT_SL_IMPL");

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemsInsideDTOOld(problemsInsideDTOOld);
    problemsInsideDTO.setInsertKedb("YES");
    problemsInsideDTO.setUnitUpdateId("1");
    problemsInsideDTO.setUnitUpdateName("c");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("d");
    problemsInsideDTO.setCreateUserId(10L);
    problemsInsideDTO.setReceiveUserId(66L);
    problemsInsideDTO.setPmGroup(52L);
    problemsInsideDTO.setStateCode("PT_WORKARROUND_PROPOSAL");

    //getListRolePmByUser
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    rolesDTO.setRoleCode("PM_XXX");
    lstRole.add(rolesDTO);

    PowerMockito.when(ptProblemsRepository.edit(any())).thenReturn(resultInSideDto);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(userRepository.isManagerOfUnits(any())).thenReturn(true);
    resultInSideDto = problemsBusiness.updateProblems(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }


  @Test
  public void updateProblemsNew_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    PowerMockito.when(problemsBusiness.edit(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = problemsBusiness.updateProblemsNew(problemsInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void updateProblemsNew_05() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUnitUpdateId("2");
    problemsInsideDTO.setUnitUpdateName("ac");
    problemsInsideDTO.setUserUpdateId("1");
    problemsInsideDTO.setUserUpdateName("ad");
    problemsInsideDTO.setStateName("ae");
    problemsInsideDTO.setProblemId(184L);
    problemsInsideDTO.setWorklog("vtnet");
    problemsInsideDTO.setWorklogNew("vtnet");
    problemsInsideDTO.setItemTypeCode("jo");
    problemsInsideDTO.setProblemsNewDTO(problemsInsideDTO);

    PowerMockito.when(problemsBusiness.edit(problemsInsideDTO)).thenReturn(resultInSideDto);

    PowerMockito.when(problemActionLogsBusiness.insertProblemActionLogs(any()))
        .thenReturn(resultInSideDto);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(1L);
    List<String> s = Mockito.spy(ArrayList.class);
    s.add("1");

    PowerMockito.when(problemsBusiness.getSequenseProblems(anyString(), anyInt())).thenReturn(s);
    PowerMockito.when(ptProblemsRepository.getSeqTableProblems(anyString())).thenReturn("1");
    PowerMockito.when(problemsBusiness.add(problemsInsideDTO)).thenReturn(resultInSideDto);
    ResultInSideDto result = problemsBusiness.updateProblemsNew(problemsInsideDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testGetTransitionStatus() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable1 = Mockito.spy(Datatable.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ProblemsInsideDTO dto = Mockito.spy(ProblemsInsideDTO.class);
    dto.setProblemState("1");
    dto.setPmGroup(10L);
    dto.setCreateUserId(999999l);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemCode("PT_OPEN");
    catItemDTO.setItemName("LeBaoBinh");
    List<CatItemDTO> lstState = Mockito.spy(ArrayList.class);
    lstState.add(catItemDTO);
    PowerMockito.when(catItemBusiness.getListItemByCategory(PROBLEM.STATE, null))
        .thenReturn(lstState);

    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    PowerMockito.when(roleUserBusiness
        .getListRoleUserByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRoleUser);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("21");
    lstRole.add(rolesDTO);
    PowerMockito.when(rolesBusiness
        .getListRolesByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRole);

    List<CatItemDTO> lstStateFinal = Mockito.spy(ArrayList.class);
    lstStateFinal.add(catItemDTO);
    datatable1.setTotal(lstStateFinal.size());
    datatable1.setData(lstStateFinal);

    Datatable datatable11 = problemsBusiness.getTransitionStatus(dto);
    Assert.assertEquals(datatable1.getTotal(), datatable11.getTotal());
  }

  @Test
  public void testGetTransitionStatus_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable1 = Mockito.spy(Datatable.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ProblemsInsideDTO dto = Mockito.spy(ProblemsInsideDTO.class);
    dto.setProblemState("1");
    dto.setPmGroup(10L);
    dto.setCreateUserId(999999l);
    dto.setReceiveUserId(999999l);
    dto.setReceiveUnitId(1L);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemCode("PT_DIAGNOSED");
    catItemDTO.setItemName("LeBaoBinh");
    List<CatItemDTO> lstState = Mockito.spy(ArrayList.class);
    lstState.add(catItemDTO);
    PowerMockito.when(catItemBusiness.getListItemByCategory(PROBLEM.STATE, null))
        .thenReturn(lstState);

    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    PowerMockito.when(roleUserBusiness
        .getListRoleUserByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRoleUser);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("21");
    lstRole.add(rolesDTO);
    PowerMockito.when(rolesBusiness
        .getListRolesByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRole);

    List<CatItemDTO> lstStateFinal = Mockito.spy(ArrayList.class);
    lstStateFinal.add(catItemDTO);
    PowerMockito.when(ptProblemsRepository.getTransitionStatus(any())).thenReturn(lstStateFinal);

    datatable1.setTotal(lstStateFinal.size());
    datatable1.setData(lstStateFinal);

    Datatable datatable11 = problemsBusiness.getTransitionStatus(dto);
//    Assert.assertEquals(datatable1.getTotal(), datatable11.getTotal());
  }

  @Test
  public void testGetTransitionStatus_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable1 = Mockito.spy(Datatable.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ProblemsInsideDTO dto = Mockito.spy(ProblemsInsideDTO.class);
    dto.setProblemState("1");
    dto.setPmGroup(10L);
    dto.setCreateUserId(999999l);
    dto.setReceiveUserId(999999l);
    dto.setReceiveUnitId(1L);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemCode("PT_OPEN_2");
    catItemDTO.setItemName("LeBaoBinh");
    List<CatItemDTO> lstState = Mockito.spy(ArrayList.class);
    lstState.add(catItemDTO);
    PowerMockito.when(catItemBusiness.getListItemByCategory(PROBLEM.STATE, null))
        .thenReturn(lstState);

    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    PowerMockito.when(roleUserBusiness
        .getListRoleUserByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRoleUser);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    lstRole.add(rolesDTO);
    PowerMockito.when(rolesBusiness
        .getListRolesByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRole);

    List<CatItemDTO> lstStateFinal = Mockito.spy(ArrayList.class);
    lstStateFinal.add(catItemDTO);
    PowerMockito.when(ptProblemsRepository.getTransitionStatus(any())).thenReturn(lstStateFinal);

    datatable1.setTotal(lstStateFinal.size());
    datatable1.setData(lstStateFinal);

    Datatable datatable11 = problemsBusiness.getTransitionStatus(dto);
    Assert.assertEquals(datatable1.getTotal(), datatable11.getTotal());
  }

  @Test
  public void testGetListRolePmByUser() {
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    PowerMockito.when(roleUserBusiness
        .getListRoleUserByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRoleUser);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("21");
    lstRole.add(rolesDTO);
    PowerMockito.when(rolesBusiness
        .getListRolesByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRole);
    List<RolesDTO> list = problemsBusiness.getListRolePmByUser(68L);
    Assert.assertEquals(list.size(), lstRole.size());
  }

  @Test
  public void testGetListRolePmByUser_02() {
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    ConditionBean condition = new ConditionBean("userId", String.valueOf(6699L),
        Constants.NAME_EQUAL, Constants.NUMBER);
    lstCondition.add(condition);
    RoleUserDTO roleUserDTO = Mockito.spy(RoleUserDTO.class);
    roleUserDTO.setRoleId("10");
    List<RoleUserDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(roleUserDTO);
    PowerMockito.when(roleUserBusiness
        .getListRoleUserByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRoleUser);
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleId("10");
    lstRole.add(rolesDTO);
    PowerMockito.when(rolesBusiness
        .getListRolesByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRole);
    List<RolesDTO> list = problemsBusiness.getListRolePmByUser(68L);
    Assert.assertEquals(list.size(), lstRole.size());
  }

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
    resultInSideDto1.setKey(RESULT.SUCCESS);
    resultInSideDto1.setMessage("common.update.fail");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx6996");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(1L);
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    PowerMockito.when(I18n.getLanguage("monitor.system.PT")).thenReturn("Quản lý vấn đề");
    GroupResponse groupResponse = Mockito.spy(GroupResponse.class);
    groupResponse.setResultCode(1);
    PowerMockito.when(wsChatPort.createGroupInBusiness2(any())).thenReturn(groupResponse);
    try {
      resultInSideDto1 = problemsBusiness.sendChatListUsers(problemsInsideDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void sendChatListUsers_02() throws Exception {
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
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx6996");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(1L);
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    PowerMockito.when(I18n.getLanguage("monitor.system.PT")).thenReturn("Quản lý vấn đề");
    GroupResponse groupResponse = Mockito.spy(GroupResponse.class);
    PowerMockito.when(wsChatPort.createGroupInBusiness2(any())).thenReturn(groupResponse);

    try {
      resultInSideDto1 = problemsBusiness.sendChatListUsers(problemsInsideDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.ERROR);
  }

  @Test
  public void sendChatListUsers_03() throws Exception {
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
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx6996");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(1L);
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);

    PowerMockito.when(I18n.getLanguage("monitor.system.PT")).thenReturn("Quản lý vấn đề");
    PowerMockito.when(wsChatPort.createGroupInBusiness2(any())).thenReturn(null);
    try {
      resultInSideDto1 = problemsBusiness.sendChatListUsers(problemsInsideDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.ERROR);
  }

  @Test
  public void sendChatListUsers_04() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    List<UsersInsideDto> usersInsideDtoList = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDtoList.add(usersInsideDto);
    problemsInsideDTO.setProblemCode("184");
    problemsInsideDTO.setProblemId(10L);
    problemsInsideDTO.setUsersInsideDtos(usersInsideDtoList);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.ERROR);
    resultInSideDto1.setMessage("common.update.fail");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(1L);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx6996");

    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(I18n.getLanguage("monitor.system.PT")).thenReturn("Quản lý vấn đề");
    GroupResponse groupResponse = Mockito.spy(GroupResponse.class);
    groupResponse.setMessage("le bao binh");
    PowerMockito.when(wsChatPort.createGroupInBusiness2(any())).thenReturn(groupResponse);

    try {
      resultInSideDto1 = problemsBusiness.sendChatListUsers(problemsInsideDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.ERROR);
  }

  @Test
  public void sendChatListUsers_05() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    List<UsersInsideDto> usersInsideDtoList = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDtoList.add(usersInsideDto);
    problemsInsideDTO.setProblemCode("184");
    problemsInsideDTO.setProblemId(10L);
    problemsInsideDTO.setUsersInsideDtos(usersInsideDtoList);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.ERROR);
    resultInSideDto1.setMessage("common.update.fail");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(1L);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx6996");

    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(I18n.getLanguage("monitor.system.PT")).thenReturn("Quản lý vấn đề");
    PowerMockito.when(wsChatPort.createGroupInBusiness2(any())).thenReturn(null);
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
    }
  }

  static void setMockSingleton(WSChatPort wsChatPort) {
    try {
      Method createConnect = Method.class.getDeclaredMethod("createConnect");
      createConnect.setAccessible(true);
    } catch (Exception e) {

    }
  }

  @Test
  public void testSetMapPMGroupName() {
    ConditionBean conditionBean = Mockito.spy(ConditionBean.class);
    conditionBean.setValue("ABC");
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    lstCondition.add(conditionBean);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleName("xxx");
    rolesDTO.setRoleId("69");
    List<RolesDTO> lstRolePM = Mockito.spy(ArrayList.class);
    lstRolePM.add(rolesDTO);
    PowerMockito.when(rolesBusiness
        .getListRolesByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRolePM);
    problemsBusiness.setMapPMGroupName();
    Assert.assertNotNull(lstRolePM);
  }

//  @Test
//  public void testCheckForUnitProcess_01() {
//    Logger logger = PowerMockito.mock(Logger.class);
//    RolesDTO lDTO = Mockito.spy(RolesDTO.class);
//    lDTO.setRoleId("69");
//    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
//    lstRole.add(lDTO);
//    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
//    problemsInsideDTO.setPmGroup(96L);
//    problemsInsideDTO.setCreateUnitId(1L);
//    problemsInsideDTO.setReceiveUnitId(1L);
//    Boolean test = problemsBusiness
//        .checkForUnitProcess(lstRole, problemsInsideDTO, "68", "PT_CANCELED", "", "");
//    Assert.assertEquals(test, false);
//  }

//  @Test
//  public void testCheckForUnitProcess_02() {
//    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//    RolesDTO lDTO = Mockito.spy(RolesDTO.class);
//    lDTO.setRoleId("69");
//    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
//    lstRole.add(lDTO);
//    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
//    problemsInsideDTO.setPmGroup(96L);
//    problemsInsideDTO.setCreateUnitId(68L);
//    problemsInsideDTO.setReceiveUnitId(68L);
//    Boolean test = problemsBusiness
//        .checkForUnitProcess(lstRole, problemsInsideDTO, "68", "PT_QUEUED", "", "");
//    Assert.assertEquals(test, false);
//  }

  @Test
  public void testSearchParentPTForCR() {
    Datatable datatable = Mockito.spy(Datatable.class);
    ConditionBean conditionBean1 = new ConditionBean("categoryId", "3", Constants.NAME_EQUAL,
        Constants.NUMBER);
    ConditionBean conditionBean2 = new ConditionBean("itemCode",
        "PT_QUEUED,PT_DIAGNOSED,PT_WA_FOUND,PT_WA_IMPL,"
            + "PT_SL_FOUND,PT_SL_IMPL,PT_CLEAR,PT_ROOT_CAUSE_PROPOSAL,PT_WORKARROUND_PROPOSAL,PT_SOLUTION_PROPOSAL",
        Constants.NAME_IN, Constants.STRING);
    List<ConditionBean> lstConditionBeans = Mockito.spy(ArrayList.class);
    lstConditionBeans.add(conditionBean1);
    lstConditionBeans.add(conditionBean2);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(12L);
    List<CatItemDTO> lstCatItemDTO = Mockito.spy(ArrayList.class);
    lstCatItemDTO.add(catItemDTO);
    PowerMockito.when(catItemRepository
        .getListCatItemByCondition(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstCatItemDTO);
    ProblemsInsideDTO dto = Mockito.spy(ProblemsInsideDTO.class);
    dto.setPmGroup(45L);
    List<ProblemsInsideDTO> problemsInsideDTOList = Mockito.spy(ArrayList.class);
    problemsInsideDTOList.add(dto);
    PowerMockito.when(ptProblemsRepository.searchParentPTForCR(any())).thenReturn(datatable);
    Datatable data = problemsBusiness.searchParentPTForCR(dto);
    Assert.assertEquals(data.getTotal(), datatable.getTotal());
  }

  @Test
  public void testGetListProblemFiles() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileName("file Test");
    gnocFileDto.setCreateTime(new Date());
    List<GnocFileDto> list = Mockito.spy(ArrayList.class);
    list.add(gnocFileDto);
    datatable.setData(list);
    PowerMockito.when(ptProblemsRepository.getListProblemFiles(any())).thenReturn(datatable);
    Datatable data = problemsBusiness.getListProblemFiles(gnocFileDto);
    Assert.assertEquals(data.getTotal(), datatable.getTotal());
  }

  @Test
  public void testInsertProblemFiles() throws IOException {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemId(69L);
    problemsInsideDTO.setCreatedTime(new Date());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(firstFile);
    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlxx");
    PowerMockito.when(FileUtils.saveUploadFile(any(), any(), any(), any())).thenReturn("/trungduongFtp.xlxx");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(ptProblemsRepository.insertProblemFiles(any())).thenReturn(resultFileDataOld);

    ResultInSideDto result = problemsBusiness.insertProblemFiles(files, problemsInsideDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testDeleteProblemFiles() {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(10L);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setMappingId(69L);
    PowerMockito.when(ptProblemsRepository.deleteProblemFiles(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(gnocFileRepository.deleteGnocFileByDto(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = problemsBusiness.deleteProblemFiles(gnocFileDto);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

//  @Test
//  public void testGetListProblemsSearchExport() throws Exception {
//    Logger logger = PowerMockito.mock(Logger.class);
//    UserToken userToken = Mockito.spy(UserToken.class);
//    userToken.setDeptId(1L);
//    userToken.setUserName("thanhlv12");
//    PowerMockito.mockStatic(TicketProvider.class);
//    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
//    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
//    rolesDTO.setRoleId("21");
//    List<RolesDTO> rolesDTOS = Mockito.spy(ArrayList.class);
//    rolesDTOS.add(rolesDTO);
//    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
//    problemsInsideDTO.setWorklogNew("vnnbjfhg");
//    List<ProblemsInsideDTO> lst =Mockito.spy(ArrayList.class);
//    lst.add(problemsInsideDTO);
//    PowerMockito.when(ptProblemsRepository.getListProblemsSearchExport(any())).thenReturn(lst);
//
//
//
//
//  }

  @Test
  public void testSearchProblemTicket() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx-xxx");
    List<ProblemsInsideDTO> lst = Mockito.spy(ArrayList.class);
    ProblemsInsideDTO dto = Mockito.spy(ProblemsInsideDTO.class);
    dto.setProblemState("a");
    dto.setPriorityId(1L);
    dto.setTypeId(2L);
    dto.setSubCategoryId(3L);
    dto.setImpactId(4L);
    dto.setUrgencyId(5L);
    dto.setAccessId(6L);
    dto.setReceiveUnitId(7L);
    dto.setReceiveUserId(8L);
    dto.setCategorization(9L);
    dto.setRcaType(10L);
    dto.setPtRelatedType(11L);
    dto.setWa("ab");
    dto.setRca("ac");
    dto.setSolution("ad");
    dto.setDescription("ae");
    dto.setPmGroup(2L);
    dto.setCreatedTime(new Date());
    dto.setRcaFoundTime(new Date());
    dto.setEsWaTime(new Date());
    dto.setEsSlTime(new Date());
    dto.setWaFoundTime(new Date());
    dto.setSlFoundTime(new Date());
    dto.setColor("white");
    dto.setIsOutOfDate("ba");

    lst.add(dto);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(11L);
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(unitDTO);
    PowerMockito.when(unitBusiness.getListUnitByLevel("")).thenReturn(lstUnitAll);
    PowerMockito
        .when(unitBusiness.getListUnitDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstUnitAll);

    //setMapPMGroupName
    ConditionBean conditionBean = Mockito.spy(ConditionBean.class);
    conditionBean.setValue("ABC");
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    lstCondition.add(conditionBean);
    RolesDTO rolesDTO = Mockito.spy(RolesDTO.class);
    rolesDTO.setRoleName("xxx");
    rolesDTO.setRoleId("69");
    List<RolesDTO> lstRolePM = Mockito.spy(ArrayList.class);
    lstRolePM.add(rolesDTO);
    PowerMockito.when(rolesBusiness
        .getListRolesByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstRolePM);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(44L);
    catItemDTO.setItemCode("cc");
    catItemDTO.setItemName("gjho");
    //Lay user
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(9L);
    usersInsideDto.setUsername("thanhlv12");
    lstUser.add(usersInsideDto);
    PowerMockito.when(userBusiness
        .getListUsersByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstUser);

    List<ProblemsInsideDTO> insideDTOList = problemsBusiness.searchProblemTicket(lst);
    Assert.assertEquals(insideDTOList.size(), lst.size());
  }

  @Test
  public void testGetListProblemsSearchExport() throws Exception{
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx-xxx");
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonExport.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    File file = problemsBusiness.getListProblemsSearchExport(problemsInsideDTO);
    Assert.assertNull(file);
  }

  @Test
  public void testCountDayOff() {
    ProblemConfigTimeDTO dto = Mockito.spy(ProblemConfigTimeDTO.class);
    dto.setSolutionTypeId(1L);
    dto.setReasonGroupId(1L);
    dto.setTimeProcess(1L);
    dto.setSubCategoryId(1L);
    dto.setTypeId(1L);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<ProblemConfigTimeDTO> problemConfigTimeDTOList = Mockito.spy(ArrayList.class);
    problemConfigTimeDTOList.add(dto);
    datatable.setData(problemConfigTimeDTOList);
    PowerMockito.when(problemConfigTimeRepository.onSearchProbleConfigTime(dto)).thenReturn(datatable);
    problemsBusiness.countDayOff(dto);
  }

  @Test
  public void getDatatableProblemsChartUpgrade() {
  }

  @Test
  public void findListOdByPt() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<OdSearchInsideDTO> odSearchInsideDTOS = Mockito.spy(ArrayList.class);
    odSearchInsideDTOS.add(new OdSearchInsideDTO());
    PowerMockito.when(odServiceProxy.getListDataSearchForOther(any())).thenReturn(odSearchInsideDTOS);
    List<OdSearchInsideDTO> actual = problemsBusiness.findListOdByPt(1L);
    Assert.assertEquals(actual.size(), odSearchInsideDTOS.size());
  }

  @Test
  public void findListSrByPt() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<SrInsiteDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(new SrInsiteDTO());
    PowerMockito.when(srServiceProxy.getListDataSearchForPt(any())).thenReturn(lst);
    List<SrInsiteDTO> actual = problemsBusiness.findListSrByPt(1L);
    Assert.assertEquals(actual.size(), lst.size());
  }

  @Test
  public void getListProblemsChartUpgrade() {
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setProblemState("1");
    problemsInsideDTO.setCreatedTime(new Date());
    problemsInsideDTO.setLastUpdateTime(new Date());
    problemsInsideDTO.setAssignedTime(new Date());
    problemsInsideDTO.setEsRcaTime(new Date());
    problemsInsideDTO.setEsWaTime(new Date());
    problemsInsideDTO.setEsSlTime(new Date());
    problemsInsideDTO.setStartedTime(new Date());
    problemsInsideDTO.setEndedTime(new Date());
    problemsInsideDTO.setClosedTime(new Date());
    problemsInsideDTO.setDelayTime(new Date());
    problemsInsideDTO.setDeferredTime(new Date());
    List<ProblemsInsideDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(problemsInsideDTO);
    PowerMockito.when(ptProblemsRepository.getListProblemsChartUpgrade(any())).thenReturn(lstData);
    problemsBusiness.getListProblemsChartUpgrade(problemsInsideDTO);
  }
}
