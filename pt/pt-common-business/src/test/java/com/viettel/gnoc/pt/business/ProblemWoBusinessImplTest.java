package com.viettel.gnoc.pt.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.pt.dto.ProblemWoDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.repository.ProblemWoRepository;
import com.viettel.gnoc.wfm.dto.WoSearchDTO;
import java.util.ArrayList;
import java.util.List;
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
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProblemWoBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class, FTPUtil.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*",
    "jdk.internal.reflect.*"})
public class ProblemWoBusinessImplTest {

  @InjectMocks
  ProblemWoBusinessImpl problemWoBusiness;

  @Mock
  ProblemWoRepository problemWoRepsitory;

  @Mock
  UserBusiness userBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void testInsertProblemWo_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    ProblemWoDTO problemWoDTO = Mockito.spy(ProblemWoDTO.class);
    problemWoDTO.setProblemWoId(10l);
    problemWoDTO.setProblemId(10L);
    problemWoDTO.setWoId(10L);
    problemWoDTO.setPtStatusId(10L);
    PowerMockito.when(problemWoRepsitory.insertProblemWo(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = problemWoBusiness.insertProblemWo(problemWoDTO);
    Assert.assertEquals(resultInSideDto, result);
  }

  @Test
  public void testUpdateProblemWo_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemWoDTO problemWoDTO = Mockito.spy(ProblemWoDTO.class);
    problemWoDTO.setProblemWoId(10L);
    problemWoDTO.setProblemId(10L);
    problemWoDTO.setWoId(10L);
    problemWoDTO.setPtStatusId(10L);
    PowerMockito.when(problemWoRepsitory.updateProblemWo(any())).thenReturn(RESULT.SUCCESS);
    String result2 = problemWoBusiness.updateProblemWo(problemWoDTO);
    Assert.assertEquals(RESULT.SUCCESS, result2);
  }

  @Test
  public void testDeleteProblemWo_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.when(problemWoRepsitory.deleteProblemWo(anyLong())).thenReturn(RESULT.SUCCESS);
    String result = problemWoBusiness.deleteProblemWo(10L);
    Assert.assertEquals(RESULT.SUCCESS, result);
  }

  @Test
  public void deleteListProblemWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemWoDTO problemWoDTO = Mockito.spy(ProblemWoDTO.class);
    problemWoDTO.setProblemId(10L);
    List<ProblemWoDTO> problemWoDTOList = new ArrayList<>();
    problemWoDTOList.add(problemWoDTO);
    PowerMockito.when(problemWoRepsitory.deleteListProblemWo(anyList())).thenReturn(RESULT.SUCCESS);
    String result2 = problemWoBusiness.deleteListProblemWo(problemWoDTOList);
    Assert.assertEquals(RESULT.SUCCESS, result2);
  }

  @Test
  public void findProblemWoById() {
    Logger logger = PowerMockito.mock(Logger.class);
    ProblemWoDTO problemWoDTO = Mockito.spy(ProblemWoDTO.class);
    problemWoDTO.setProblemWoId(10L);
    PowerMockito.when(problemWoRepsitory.findProblemWoById(anyLong())).thenReturn(problemWoDTO);
    ProblemWoDTO problemWoDTO1 = problemWoBusiness.findProblemWoById(10L);
    Assert.assertEquals(problemWoDTO, problemWoDTO1);
  }

  @Test
  public void testInsertOrUpdateListProblemWo_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ProblemWoDTO> problemWoDTOS = new ArrayList<>();
    ProblemWoDTO problemWoDTO = new ProblemWoDTO();
    problemWoDTO.setPtStatusId(10L);
    problemWoDTO.setWoId(10L);
    problemWoDTO.setPtStatusId(10L);
    problemWoDTO.setProblemWoId(10L);
    problemWoDTOS.add(problemWoDTO);
    PowerMockito.when(problemWoRepsitory.insertOrUpdateListProblemWo(anyList()))
        .thenReturn(RESULT.SUCCESS);
    String result2 = problemWoBusiness.insertOrUpdateListProblemWo(problemWoDTOS);
    Assert.assertEquals(RESULT.SUCCESS, result2);
  }

  @Test
  public void testGetListProblemWoByCondition_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ConditionBean conditionBean = Mockito.spy(ConditionBean.class);
    List<ProblemWoDTO> list = Mockito.spy(ArrayList.class);
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    conditionBean.setField("PROBLEM_WO_ID");
    conditionBean.setValue("cvb");
    conditionBean.setType("yui");
    lstCondition.add(conditionBean);

    String sortType = "ASC";
    String sortName = "PROBLEM_ID";
    PowerMockito.when(problemWoRepsitory
        .getListProblemWoByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(list);
    List result = problemWoBusiness
        .getListProblemWoByCondition(lstCondition, 0, 15, sortType, sortName);
    Assert.assertEquals(list, result);
  }

  @Test
  public void getSeqProblemWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    List list = PowerMockito.mock(List.class);
    String sequence = "problem_wo_seq";
    int size = 0;
    PowerMockito.when(problemWoRepsitory.getSeqProblemWo(sequence, size)).thenReturn(list);
    List result = problemWoBusiness.getSeqProblemWo(sequence, size);
    Assert.assertEquals(list, result);
  }

  @Test
  public void getListDataSearchWeb() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setPage(1);
    problemsInsideDTO.setPageSize(10);
    problemsInsideDTO.setProblemCode("Test");
    problemsInsideDTO.setSortName("problemCode");
    problemsInsideDTO.setSortType("asc");
    problemsInsideDTO.setPage(1);
    problemsInsideDTO.setPageSize(1);

    WoSearchDTO woSearchDTO = Mockito.spy(WoSearchDTO.class);
    woSearchDTO.setPage(problemsInsideDTO.getPage());
    woSearchDTO.setPageSize(problemsInsideDTO.getPageSize());
    woSearchDTO.setSortName(problemsInsideDTO.getSortName());
    woSearchDTO.setSortType(problemsInsideDTO.getSortType());

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    Double offset = 1D;
    woSearchDTO.setUserId("1");
    woSearchDTO.setOffset(offset.longValue());
    woSearchDTO.setWoSystemId(problemsInsideDTO.getProblemCode());
    woSearchDTO.setUserId(String.valueOf(userToken.getDeptId()));
    PowerMockito.when(problemWoRepsitory.getListDataSearchWeb(any())).thenReturn(datatable);
    Datatable datatable1 = problemWoBusiness.getListDataSearchWeb(problemsInsideDTO);
    Assert.assertEquals(datatable1.getPages(), datatable.getPages());
  }

  @Test
  public void getListProblemWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    ProblemWoDTO problemWoDTO = Mockito.spy(ProblemWoDTO.class);
    PowerMockito.when(problemWoRepsitory.getListProblemWo(any())).thenReturn(datatable);
    Datatable result = problemWoBusiness.getListProblemWo(problemWoDTO);
    Assert.assertEquals(datatable, result);
  }
}
