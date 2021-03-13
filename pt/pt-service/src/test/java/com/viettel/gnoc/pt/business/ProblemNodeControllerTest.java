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
import com.viettel.gnoc.pt.dto.ProblemNodeDTO;
import com.viettel.gnoc.pt.repository.ProblemNodeRepository;
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
@PrepareForTest({ProblemNodeBusinessImpl.class, I18n.class})
public class ProblemNodeControllerTest {

  @InjectMocks
  ProblemNodeBusinessImpl problemNodeBusiness;

  @Mock
  ProblemNodeRepository problemNodeRepository;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void testGetListProblemNodeByCondition() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemNodeBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemNodeDTO problemNodeDTO = Mockito.spy(ProblemNodeDTO.class);
    List<ProblemNodeDTO> problemNodeDTOS = Mockito.spy(ArrayList.class);
    List<ConditionBean> conditionBeans = Mockito.spy(ArrayList.class);
    problemNodeDTO.setProblemNodeId(1L);
    problemNodeDTOS.add(problemNodeDTO);

    PowerMockito.when(problemNodeRepository
        .getListProblemNodeByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(problemNodeDTOS);
    List<ProblemNodeDTO> result = problemNodeBusiness.
        getListProblemNodeByCondition(conditionBeans, 0, 0, "", "");
    Assert.assertEquals(problemNodeDTOS, result);
  }

  @Test
  public void testGetListProblemNodeDTO() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemNodeBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemNodeDTO problemNodeDTO = Mockito.spy(ProblemNodeDTO.class);
    List<ProblemNodeDTO> problemNodeDTOS = Mockito.spy(ArrayList.class);
    problemNodeDTO.setProblemNodeId(1L);
    problemNodeDTOS.add(problemNodeDTO);
    PowerMockito.when(problemNodeRepository
        .getListProblemNodeDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(problemNodeDTOS);
    List<ProblemNodeDTO> result = problemNodeBusiness.
        getListProblemNodeDTO(problemNodeDTO, 0, 0, "", "");
    Assert.assertEquals(problemNodeDTOS, result);
  }

  @Test
  public void testInsertOrUpdateListProblemNode() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemNodeBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemNodeDTO problemNodeDTO = Mockito.spy(ProblemNodeDTO.class);
    List<ProblemNodeDTO> problemNodeDTOS = Mockito.spy(ArrayList.class);
    problemNodeDTO.setProblemNodeId(1L);
    problemNodeDTOS.add(problemNodeDTO);
    PowerMockito.when(problemNodeRepository.insertOrUpdateListProblemNode(any()))
        .thenReturn(RESULT.SUCCESS);
    String result = problemNodeBusiness.
        insertOrUpdateListProblemNode(problemNodeDTOS);
    Assert.assertEquals(RESULT.SUCCESS, result);
  }

  @Test
  public void testInsertProblemNode() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemNodeBusinessImpl.class.getDeclaredField("log"), logger);

    ProblemNodeDTO problemNodeDTO = Mockito.spy(ProblemNodeDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);

    resultInSideDto.setKey(RESULT.SUCCESS);
    problemNodeDTO.setProblemNodeId(1L);

    PowerMockito.when(problemNodeRepository.insertProblemNode(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = problemNodeBusiness.
        insertProblemNode(problemNodeDTO);
    Assert.assertEquals(resultInSideDto, result);
  }

  @Test
  public void testFindProblemNodeById() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemNodeBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemNodeDTO problemNodeDTO = Mockito.spy(ProblemNodeDTO.class);
    problemNodeDTO.setProblemNodeId(335L);
    PowerMockito.when(problemNodeRepository.findProblemNodeById(any())).thenReturn(problemNodeDTO);
    ProblemNodeDTO problemNodeDTO1 = problemNodeBusiness.findProblemNodeById(335L);
    Mockito.verify(problemNodeRepository).findProblemNodeById(335L);
    Assert.assertEquals(problemNodeDTO, problemNodeDTO1);
  }

  @Test
  public void testGetSequenseProblemNode() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemNodeBusinessImpl.class.getDeclaredField("log"), logger);
    List<String> sequences = Mockito.spy(ArrayList.class);
    sequences.add("999");
    PowerMockito.when(problemNodeRepository.getSequenseProblemNode("SEQ", 1)).thenReturn(sequences);
    List<String> result = problemNodeBusiness.getSequenseProblemNode("SEQ", 1);
    Assert.assertEquals(result.get(0), sequences.get(0));
  }

  @Test
  public void testUpdateProblemNode() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemNodeBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemNodeDTO problemNodeDTO = Mockito.spy(ProblemNodeDTO.class);
    problemNodeDTO.setProblemNodeId(335L);
    PowerMockito.when(problemNodeRepository.updateProblemNode(any())).thenReturn(RESULT.SUCCESS);
    String result = problemNodeBusiness.updateProblemNode(problemNodeDTO);
    Assert.assertEquals(result, RESULT.SUCCESS);
  }

  @Test
  public void testDeleteListProblemNode() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemNodeBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemNodeDTO problemNodeDTO = Mockito.spy(ProblemNodeDTO.class);
    List<ProblemNodeDTO> problemNodeDTOS = Mockito.spy(ArrayList.class);
    problemNodeDTO.setProblemNodeId(1L);
    problemNodeDTOS.add(problemNodeDTO);
    PowerMockito.when(problemNodeRepository.deleteListProblemNode(anyList()))
        .thenReturn(RESULT.SUCCESS);
    String result = problemNodeBusiness.
        deleteListProblemNode(problemNodeDTOS);
    Assert.assertEquals(RESULT.SUCCESS, result);

  }

  @Test
  public void testDeleteProblemNode() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemNodeBusinessImpl.class.getDeclaredField("log"), logger);
    PowerMockito.when(problemNodeRepository.deleteProblemNode(1L)).thenReturn(RESULT.SUCCESS);
    String result = problemNodeBusiness.deleteProblemNode(1L);
    Assert.assertEquals(result, RESULT.SUCCESS);
  }

  @Test
  public void testOnSearchProblemNodeDTO() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemNodeBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemNodeDTO problemNodeDTO = Mockito.spy(ProblemNodeDTO.class);
    problemNodeDTO.setProblemNodeId(1L);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setTotal(999);
    PowerMockito.when(problemNodeRepository.onSearchProblemNodeDTO(any())).thenReturn(datatable);
    Datatable result = problemNodeBusiness.onSearchProblemNodeDTO(problemNodeDTO);
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
