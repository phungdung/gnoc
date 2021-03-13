package com.viettel.gnoc.pt.business;

import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.pt.dto.ProblemWorklogDTO;
import com.viettel.gnoc.pt.repository.ProblemWorklogRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({ProblemWorklogBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class})
public class ProblemWorklogControllerTest {

  @InjectMocks
  ProblemWorklogBusinessImpl problemWorklogBusiness;

  @Mock
  ProblemWorklogRepository problemWorklogRepository;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();


  @Test
  public void insertProblemWorklog() {
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
      PowerMockito.when(resultInSideDto.getKey()).thenReturn(RESULT.SUCCESS);
      ProblemWorklogDTO problemWorklogDTO = new ProblemWorklogDTO();
      problemWorklogDTO.setProblemWorklogId(12L);
      problemWorklogDTO.setCreateUserId(12L);
      problemWorklogDTO.setProblemId(12L);
      PowerMockito.when(problemWorklogRepository.onInsert(any())).thenReturn(resultInSideDto);
      ResultInSideDto result = problemWorklogBusiness.onInsert(problemWorklogDTO);
      Assert.assertEquals(resultInSideDto, result);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Test
  public void updateProblemWorklog() {
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      String resultDto = PowerMockito.mock(String.class);
      ProblemWorklogDTO problemWorklogDTO = new ProblemWorklogDTO();
      problemWorklogDTO.setProblemWorklogId(12L);
      problemWorklogDTO.setCreateUserId(12L);
      problemWorklogDTO.setProblemId(2581L);
      PowerMockito.when(problemWorklogRepository.updateProblemWorklog(any())).thenReturn(resultDto);
      String result = problemWorklogBusiness.updateProblemWorklog(problemWorklogDTO);
      Assert.assertEquals(resultDto, result);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Test
  public void deleteProblemWorklog() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.when(problemWorklogRepository.deleteProblemWorklog(any()))
        .thenReturn(RESULT.SUCCESS);
    String result2 = problemWorklogBusiness.deleteProblemWorklog(10L);
    Assert.assertEquals(RESULT.SUCCESS, result2);
  }

  @Test
  public void deleteListProblemWorklog() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = PowerMockito.mock(String.class);
    ProblemWorklogDTO problemWorklogDTO = new ProblemWorklogDTO();
    PowerMockito.when(problemWorklogRepository.deleteListProblemWorklog(any())).thenReturn(result);
    List<ProblemWorklogDTO> problemWorklogDTOS = new ArrayList<>();
    problemWorklogDTOS.add(problemWorklogDTO);
    String result2 = problemWorklogBusiness.deleteListProblemWorklog(problemWorklogDTOS);
    Assert.assertEquals(result, result2);
  }

  @Test
  public void getListProblemWorklogDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    ProblemWorklogDTO problemWorklogDTO = new ProblemWorklogDTO();
    problemWorklogDTO.setProblemId(3202L);
    problemWorklogDTO.setSortName("worklog");
    PowerMockito.when(problemWorklogRepository.getListProblemWorklogDTO(problemWorklogDTO))
        .thenReturn(datatable);
    Datatable datatable1 = problemWorklogBusiness.getListProblemWorklogDTO(problemWorklogDTO);
    Assert.assertEquals(datatable, datatable1);
  }

  @Test
  public void findProblemWorklogById() {
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      ProblemWorklogDTO problemWorklogDTO = new ProblemWorklogDTO();
      problemWorklogDTO.setProblemId(10L);
      PowerMockito.when(problemWorklogRepository.findProblemWorklogById(any()))
          .thenReturn(problemWorklogDTO);
      ProblemWorklogDTO problemWorklogDTO1 = problemWorklogBusiness.findProblemWorklogById(10L);
      Assert.assertEquals(problemWorklogDTO, problemWorklogDTO1);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Test
  public void insertOrUpdateListProblemWorklog() {
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      String s = PowerMockito.mock(String.class);
      List<ProblemWorklogDTO> problemWorklogDTOS = new ArrayList<>();
      ProblemWorklogDTO problemWorklogDTO = new ProblemWorklogDTO();
      problemWorklogDTO.setProblemWorklogId(10L);
      problemWorklogDTO.setCreateUserId(10L);
      problemWorklogDTO.setProblemId(2581L);
      problemWorklogDTOS.add(problemWorklogDTO);
      ProblemWorklogDTO problemWorklogDTO1 = new ProblemWorklogDTO();
      problemWorklogDTO1.setProblemWorklogId(11L);
      problemWorklogDTO1.setCreateUserId(12L);
      problemWorklogDTO1.setProblemId(2581L);
      problemWorklogDTOS.add(problemWorklogDTO1);
      PowerMockito
          .when(problemWorklogRepository.insertOrUpdateListProblemWorklog(problemWorklogDTOS))
          .thenReturn(s);
      String result = problemWorklogBusiness.insertOrUpdateListProblemWorklog(problemWorklogDTOS);
      Assert.assertEquals(s, result);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Test
  public void getSequenseProblemWorklog() {
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
  public void getListProblemWorklogByCondition() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List list = PowerMockito.mock(List.class);
    ConditionBean conditionBean = new ConditionBean();
    List<ConditionBean> conditionBeans = new ArrayList<>();
    conditionBean.setField("problemWorklogId");
    conditionBean.setType("");
    conditionBean.setValue("");
    conditionBeans.add(conditionBean);
    int start = 1;
    int maxSize = 5;
    String sortName = "WORKLOG";
    String sortType = "ASC";
    PowerMockito.when(problemWorklogRepository
        .getListProblemWorklogByCondition(conditionBeans, start, maxSize, sortType, sortName))
        .thenReturn(list);
    List result = problemWorklogBusiness
        .getListProblemWorklogByCondition(conditionBeans, start, maxSize, sortType, sortName);
    Assert.assertEquals(list, result);
  }
}
