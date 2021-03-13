package com.viettel.gnoc.pt.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.pt.dto.ProblemActionLogsDTO;
import com.viettel.gnoc.pt.repository.ProblemActionLogsRepository;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProblemActionLogsBusinessImpl.class, I18n.class})
public class ProblemActionLogsControllerTest {

  @InjectMocks
  ProblemActionLogsBusinessImpl problemActionLogsBusiness;

  @Mock
  ProblemActionLogsRepository problemActionLogsRepository;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void testGetListProblemActionLogsByCondition() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemActionLogsBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    List<ProblemActionLogsDTO> problemActionLogsDTOS = Mockito.spy(ArrayList.class);
    List<ConditionBean> conditionBeans = Mockito.spy(ArrayList.class);
    problemActionLogsDTO.setProblemActionLogsId(1L);
    problemActionLogsDTOS.add(problemActionLogsDTO);

    PowerMockito.when(problemActionLogsRepository
        .getListProblemActionLogsByCondition(anyList(), anyInt(), anyInt(), anyString(),
            anyString()))
        .thenReturn(problemActionLogsDTOS);
    List<ProblemActionLogsDTO> result = problemActionLogsBusiness.
        getListProblemActionLogsByCondition(conditionBeans, 0, 0, "", "");
    Assert.assertEquals(problemActionLogsDTOS, result);
  }

  @Test
  public void testGetListProblemActionLogsDTO() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemActionLogsBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    List<ProblemActionLogsDTO> problemActionLogsDTOS = Mockito.spy(ArrayList.class);
    problemActionLogsDTO.setProblemActionLogsId(1L);
    problemActionLogsDTOS.add(problemActionLogsDTO);
    PowerMockito.when(problemActionLogsRepository
        .getListProblemActionLogsDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(problemActionLogsDTOS);
    List<ProblemActionLogsDTO> result = problemActionLogsBusiness.
        getListProblemActionLogsDTO(problemActionLogsDTO, 0, 0, "", "");
    Assert.assertEquals(problemActionLogsDTOS, result);
  }

  @Test
  public void testInsertOrUpdateListProblemActionLogs() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemActionLogsBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    List<ProblemActionLogsDTO> problemActionLogsDTOS = Mockito.spy(ArrayList.class);
    problemActionLogsDTO.setProblemActionLogsId(1L);
    problemActionLogsDTOS.add(problemActionLogsDTO);
    PowerMockito.when(problemActionLogsRepository.insertOrUpdateListProblemActionLogs(any()))
        .thenReturn(RESULT.SUCCESS);
    String result = problemActionLogsBusiness.
        insertOrUpdateListProblemActionLogs(problemActionLogsDTOS);
    Assert.assertEquals(RESULT.SUCCESS, result);
  }

  @Test
  public void testInsertProblemActionLogs() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemActionLogsBusinessImpl.class.getDeclaredField("log"), logger);

    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);

    resultInSideDto.setKey(RESULT.SUCCESS);
    problemActionLogsDTO.setProblemActionLogsId(1L);

    PowerMockito.when(problemActionLogsRepository.insertProblemActionLogs(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = problemActionLogsBusiness.
        insertProblemActionLogs(problemActionLogsDTO);
    Assert.assertEquals(resultInSideDto, result);
  }

  @Test
  public void testFindProblemActionLogsById() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemActionLogsBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    problemActionLogsDTO.setProblemActionLogsId(335L);
    PowerMockito.when(problemActionLogsRepository.findProblemActionLogsById(any()))
        .thenReturn(problemActionLogsDTO);
    ProblemActionLogsDTO problemActionLogsDTO1 = problemActionLogsBusiness
        .findProblemActionLogsById(335L);
    Mockito.verify(problemActionLogsRepository).findProblemActionLogsById(335L);
    Assert.assertEquals(problemActionLogsDTO, problemActionLogsDTO1);
  }

  @Test
  public void testGetSequenseProblemActionLogs() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemActionLogsBusinessImpl.class.getDeclaredField("log"), logger);
    List<String> sequences = Mockito.spy(ArrayList.class);
    sequences.add("999");
    PowerMockito.when(problemActionLogsRepository.getSequenseProblemActionLogs("SEQ", 1))
        .thenReturn(sequences);
    List<String> result = problemActionLogsBusiness.getSequenseProblemActionLogs("SEQ", 1);
    Assert.assertEquals(result.get(0), sequences.get(0));
  }

  @Test
  public void testUpdateProblemActionLogs() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemActionLogsBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    problemActionLogsDTO.setProblemActionLogsId(335L);
    PowerMockito.when(problemActionLogsRepository.updateProblemActionLogs(any()))
        .thenReturn(RESULT.SUCCESS);
    String result = problemActionLogsBusiness.updateProblemActionLogs(problemActionLogsDTO);
    Assert.assertEquals(result, RESULT.SUCCESS);
  }

  @Test
  public void testDeleteListProblemActionLogs() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemActionLogsBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    List<ProblemActionLogsDTO> problemActionLogsDTOS = Mockito.spy(ArrayList.class);
    problemActionLogsDTO.setProblemActionLogsId(1L);
    problemActionLogsDTOS.add(problemActionLogsDTO);
    PowerMockito.when(problemActionLogsRepository.deleteListProblemActionLogs(anyList()))
        .thenReturn(RESULT.SUCCESS);
    String result = problemActionLogsBusiness.
        deleteListProblemActionLogs(problemActionLogsDTOS);
    Assert.assertEquals(RESULT.SUCCESS, result);

  }

  @Test
  public void testDeleteProblemActionLogs() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemActionLogsBusinessImpl.class.getDeclaredField("log"), logger);
    PowerMockito.when(problemActionLogsRepository.deleteProblemActionLogs(1L))
        .thenReturn(RESULT.SUCCESS);
    String result = problemActionLogsBusiness.deleteProblemActionLogs(1L);
    Assert.assertEquals(result, RESULT.SUCCESS);
  }

  @Test
  public void testOnSearchProblemActionLogsDTO() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemActionLogsBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemActionLogsDTO problemActionLogsDTO = Mockito.spy(ProblemActionLogsDTO.class);
    problemActionLogsDTO.setProblemActionLogsId(1L);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setTotal(999);
    PowerMockito.when(problemActionLogsRepository.onSearchProblemActionLogsDTO(any()))
        .thenReturn(datatable);
    Datatable result = problemActionLogsBusiness.onSearchProblemActionLogsDTO(problemActionLogsDTO);
    Assert.assertEquals(result, datatable);
  }

  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }
}
