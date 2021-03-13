package com.viettel.gnoc.pt.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.pt.dto.ProblemCrDTO;
import com.viettel.gnoc.pt.repository.ProblemCrRepository;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({ProblemCrBusinessImpl.class, FileUtils.class, I18n.class})
public class ProblemCrControllerTest {

  @InjectMocks
  ProblemCrBusinessImpl problemCrBusiness;

  @Mock
  ProblemCrRepository problemCrRepository;

  @Mock
  UserBusiness userBusiness;

  @Mock
  CrServiceProxy crServiceProxy;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void testGetListProblemCrByCondition() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemCrBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemCrDTO problemCrDTO = Mockito.spy(ProblemCrDTO.class);
    List<ProblemCrDTO> problemCrDTOS = Mockito.spy(ArrayList.class);
    List<ConditionBean> conditionBeans = Mockito.spy(ArrayList.class);
    problemCrDTO.setProblemCrId(1L);
    problemCrDTOS.add(problemCrDTO);

    PowerMockito.when(problemCrRepository
        .getListProblemCrByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(problemCrDTOS);
    List<ProblemCrDTO> result = problemCrBusiness.
        getListProblemCrByCondition(conditionBeans, 0, 0, "", "");
    Assert.assertEquals(problemCrDTOS, result);
  }

  @Test
  public void testGetListProblemCrDTO() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemCrBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemCrDTO problemCrDTO = Mockito.spy(ProblemCrDTO.class);
    List<ProblemCrDTO> problemCrDTOS = Mockito.spy(ArrayList.class);
    problemCrDTO.setProblemCrId(1L);
    problemCrDTOS.add(problemCrDTO);
    PowerMockito.when(problemCrRepository
        .getListProblemCrDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(problemCrDTOS);
    List<ProblemCrDTO> result = problemCrBusiness.
        getListProblemCrDTO(problemCrDTO, 0, 0, "", "");
    Assert.assertEquals(problemCrDTOS, result);
  }

  @Test
  public void testInsertOrUpdateListProblemCr() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemCrBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemCrDTO problemCrDTO = Mockito.spy(ProblemCrDTO.class);
    List<ProblemCrDTO> problemCrDTOS = Mockito.spy(ArrayList.class);
    problemCrDTO.setProblemCrId(1L);
    problemCrDTOS.add(problemCrDTO);
    PowerMockito.when(problemCrRepository.insertOrUpdateListProblemCr(any()))
        .thenReturn(RESULT.SUCCESS);
    String result = problemCrBusiness.
        insertOrUpdateListProblemCr(problemCrDTOS);
    Assert.assertEquals(RESULT.SUCCESS, result);
  }

  @Test
  public void testInsertProblemCr() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemCrBusinessImpl.class.getDeclaredField("log"), logger);

    ProblemCrDTO problemCrDTO = Mockito.spy(ProblemCrDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    problemCrDTO.setProblemCrId(1L);

    PowerMockito.when(problemCrRepository.insertProblemCr(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = problemCrBusiness.
        insertProblemCr(problemCrDTO);
    Assert.assertEquals(resultInSideDto, result);
  }

  @Test
  public void testFindProblemCr() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemCrBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemCrDTO problemCrDTO = Mockito.spy(ProblemCrDTO.class);
    problemCrDTO.setProblemCrId(335L);
    PowerMockito.when(problemCrRepository.findProblemCrById(any())).thenReturn(problemCrDTO);
    ProblemCrDTO result = problemCrBusiness.findProblemCrById(335L);
    Assert.assertEquals(result, problemCrDTO);
  }

  @Test
  public void testGetSequenseProblemCr() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemCrBusinessImpl.class.getDeclaredField("log"), logger);
    List<String> sequences = Mockito.spy(ArrayList.class);
    sequences.add("999");
    PowerMockito.when(problemCrRepository.getSequenseProblemCr("SEQ", 1)).thenReturn(sequences);
    List<String> result = problemCrBusiness.getSequenseProblemCr("SEQ", 1);
    Assert.assertEquals(result.get(0), sequences.get(0));
  }

  @Test
  public void testUpdateProblemCr() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemCrBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemCrDTO problemCrDTO = Mockito.spy(ProblemCrDTO.class);
    problemCrDTO.setProblemCrId(335L);
    PowerMockito.when(problemCrRepository.updateProblemCr(any())).thenReturn(RESULT.SUCCESS);
    String result = problemCrBusiness.updateProblemCr(problemCrDTO);
    Assert.assertEquals(result, RESULT.SUCCESS);
  }

  @Test
  public void testDeleteListProblemCr() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemCrBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemCrDTO problemCrDTO = Mockito.spy(ProblemCrDTO.class);
    List<ProblemCrDTO> problemCrDTOS = Mockito.spy(ArrayList.class);
    problemCrDTO.setProblemCrId(1L);
    problemCrDTOS.add(problemCrDTO);
    PowerMockito.when(problemCrRepository.deleteListProblemCr(anyList()))
        .thenReturn(RESULT.SUCCESS);
    String result = problemCrBusiness.
        deleteListProblemCr(problemCrDTOS);
    Assert.assertEquals(RESULT.SUCCESS, result);

  }

  @Test
  public void testDeleteProblemCr() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemCrBusinessImpl.class.getDeclaredField("log"), logger);
    PowerMockito.when(problemCrRepository.deleteProblemCr(1L)).thenReturn(RESULT.SUCCESS);
    String result = problemCrBusiness.deleteProblemCr(1L);
    Assert.assertEquals(result, RESULT.SUCCESS);
  }

  @Test
  public void testOnSearchProblemCrDTO() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(ProblemCrBusinessImpl.class.getDeclaredField("log"), logger);
    ProblemCrDTO problemCrDTO = Mockito.spy(ProblemCrDTO.class);
    problemCrDTO.setProblemCrId(1L);
    List<ProblemCrDTO> problemCrDTOS = Mockito.spy(ArrayList.class);
    problemCrDTOS.add(problemCrDTO);
    PowerMockito.when(problemCrRepository.onSearchProblemCrDTO(any())).thenReturn(problemCrDTOS);
    List<ProblemCrDTO> result = problemCrBusiness.onSearchProblemCrDTO(problemCrDTO);
    Assert.assertEquals(result, problemCrDTOS);
  }

  @Test
  public void testSearchProblemCr() {
    try {
      Logger logger = PowerMockito.mock(Logger.class);
      PowerMockito.doNothing().when(logger).debug(any());
      setFinalStatic(ProblemCrBusinessImpl.class.getDeclaredField("log"), logger);
      ProblemCrDTO problemCrDTO = Mockito.spy(ProblemCrDTO.class);
      problemCrDTO.setProblemCrId(1L);
      problemCrDTO.setCrId(37212L);
      problemCrDTO.setPageSize(10);
      problemCrDTO.setPage(1);
      List<ProblemCrDTO> problemCrDTOS = Mockito.spy(ArrayList.class);
      problemCrDTOS.add(problemCrDTO);
      UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
      usersInsideDto.setUserId(999999l);
      usersInsideDto.setUsername("admin");
      usersInsideDto.setUnitId(999l);
      usersInsideDto.setUnitName("VT");
      List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
      usersInsideDtos.add(usersInsideDto);
      CrDTO crDTO = Mockito.spy(CrDTO.class);
      crDTO.setCrId("37212");
      List<CrDTO> crDTOS = Mockito.spy(ArrayList.class);
      crDTOS.add(crDTO);
      PowerMockito.when(problemCrRepository.onSearchProblemCrDTO(any())).thenReturn(problemCrDTOS);

      PowerMockito.when(userBusiness
          .getListUsersByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
          .thenReturn(
              usersInsideDtos);
      PowerMockito.when(crServiceProxy.getListCrByCondition(any())).thenReturn(crDTOS);
      Datatable result = problemCrBusiness.searchProblemCr(problemCrDTO);
      Assert.assertNotNull(result);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }

}
