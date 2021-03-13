package com.viettel.gnoc.pt.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.pt.dto.ProblemWorklogDTO;
import com.viettel.gnoc.pt.repository.ProblemWorklogRepository;
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
@PrepareForTest({ProblemWorklogBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class, FTPUtil.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*",
    "jdk.internal.reflect.*"})
public class ProblemWorklogBusinessImplTest {

  @InjectMocks
  ProblemWorklogBusinessImpl problemWorklogBusiness;

  @Mock
  ProblemWorklogRepository problemWorklogRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitBusiness unitBusiness;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();


  @Test
  public void testOnInsert_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setUserID(69L);
    userToken.setDeptId(96L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("Ibrahimovic");
    ProblemWorklogDTO problemWorklogDTO = Mockito.spy(ProblemWorklogDTO.class);
    problemWorklogDTO.setProblemWorklogId(12L);
    problemWorklogDTO.setCreateUserId(12L);
    problemWorklogDTO.setProblemId(12L);
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(problemWorklogRepository.onInsert(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = problemWorklogBusiness.onInsert(problemWorklogDTO);
    Assert.assertEquals(resultInSideDto, result);
  }

  @Test
  public void testGetListProblemWorklogByCondition() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List list = PowerMockito.mock(List.class);
    ConditionBean conditionBean = Mockito.spy(ConditionBean.class);
    List<ConditionBean> conditionBeans = Mockito.spy(ArrayList.class);
    conditionBean.setField("problemWorklogId");
    conditionBean.setType("");
    conditionBean.setValue("");
    conditionBeans.add(conditionBean);
    int start = 1;
    int maxSize = 5;
    String sortName = "WORKLOG";
    String sortType = "ASC";
    PowerMockito.when(problemWorklogRepository
        .getListProblemWorklogByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(list);
    List result = problemWorklogBusiness
        .getListProblemWorklogByCondition(conditionBeans, start, maxSize, sortType, sortName);
    Assert.assertEquals(list, result);
  }

  @Test
  public void testGetSequenseProblemWorklog() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List s = PowerMockito.mock(List.class);
    String sequence = "problem_worklog_seq";
    int[] size = {0};
    PowerMockito.when(problemWorklogRepository.getSequenceProblemWorklog(sequence, size))
        .thenReturn(s);
    List result = problemWorklogBusiness.getSequenseProblemWorklog(sequence, size);
    Assert.assertEquals(s, result);
  }

  @Test
  public void testFindProblemWorklogById() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemWorklogDTO problemWorklogDTO = Mockito.spy(ProblemWorklogDTO.class);
    problemWorklogDTO.setProblemId(10L);
    PowerMockito.when(problemWorklogRepository.findProblemWorklogById(anyLong()))
        .thenReturn(problemWorklogDTO);
    ProblemWorklogDTO problemWorklogDTO1 = problemWorklogBusiness.findProblemWorklogById(10L);
    Assert.assertEquals(problemWorklogDTO, problemWorklogDTO1);
  }

  @Test
  public void testDeleteProblemWorklog() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.when(problemWorklogRepository.deleteProblemWorklog(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    String result2 = problemWorklogBusiness.deleteProblemWorklog(10L);
    Assert.assertEquals(RESULT.SUCCESS, result2);
  }

  @Test
  public void deleteListProblemWorklog() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemWorklogDTO problemWorklogDTO = Mockito.spy(ProblemWorklogDTO.class);
    PowerMockito.when(problemWorklogRepository.deleteListProblemWorklog(any()))
        .thenReturn(RESULT.SUCCESS);
    List<ProblemWorklogDTO> problemWorklogDTOS = Mockito.spy(ArrayList.class);
    problemWorklogDTOS.add(problemWorklogDTO);
    String result2 = problemWorklogBusiness.deleteListProblemWorklog(problemWorklogDTOS);
    Assert.assertEquals(RESULT.SUCCESS, result2);
  }

  @Test
  public void updateProblemWorklog() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemWorklogDTO problemWorklogDTO = Mockito.spy(ProblemWorklogDTO.class);
    problemWorklogDTO.setProblemWorklogId(12L);
    problemWorklogDTO.setCreateUserId(12L);
    problemWorklogDTO.setProblemId(2581L);
    PowerMockito.when(problemWorklogRepository.updateProblemWorklog(any()))
        .thenReturn(RESULT.SUCCESS);
    String result = problemWorklogBusiness.updateProblemWorklog(problemWorklogDTO);
    Assert.assertEquals(RESULT.SUCCESS, result);
  }

  @Test
  public void getListProblemWorklogDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    Datatable datatable = PowerMockito.mock(Datatable.class);
    ProblemWorklogDTO problemWorklogDTO = Mockito.spy(ProblemWorklogDTO.class);
    problemWorklogDTO.setProblemId(3202L);
    problemWorklogDTO.setSortName("worklog");
    PowerMockito.when(problemWorklogRepository.getListProblemWorklogDTO(any()))
        .thenReturn(datatable);
    Datatable datatable1 = problemWorklogBusiness.getListProblemWorklogDTO(problemWorklogDTO);
    Assert.assertEquals(datatable, datatable1);
  }

  @Test
  public void insertOrUpdateListProblemWorklog() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ProblemWorklogDTO> problemWorklogDTOS = Mockito.spy(ArrayList.class);
    ProblemWorklogDTO problemWorklogDTO = Mockito.spy(ProblemWorklogDTO.class);
    problemWorklogDTO.setProblemWorklogId(10L);
    problemWorklogDTO.setCreateUserId(10L);
    problemWorklogDTO.setProblemId(2581L);
    problemWorklogDTOS.add(problemWorklogDTO);
    PowerMockito.when(problemWorklogRepository.insertOrUpdateListProblemWorklog(any()))
        .thenReturn(RESULT.SUCCESS);
    String result = problemWorklogBusiness.insertOrUpdateListProblemWorklog(problemWorklogDTOS);
    Assert.assertEquals(RESULT.SUCCESS, result);
  }

}
