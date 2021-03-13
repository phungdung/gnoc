package com.viettel.gnoc.pt.business;

import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.pt.dto.ProblemWoDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.repository.ProblemWoRepository;
import com.viettel.gnoc.wfm.dto.WoSearchDTO;
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
import viettel.passport.client.UserToken;
@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({ProblemWoBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class})
public class ProblemWoControllerTest {

  @InjectMocks
  ProblemWoBusinessImpl problemWoBusiness;

  @Mock
  ProblemWoRepository problemWoRepsitory;

  @Mock
  UserBusiness userBusiness;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void insertProblemWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    PowerMockito.when(resultInSideDto.getKey()).thenReturn(RESULT.SUCCESS);
    ProblemWoDTO problemWoDTO = new ProblemWoDTO();
    problemWoDTO.setProblemWoId(10l);
    problemWoDTO.setProblemId(10L);
    problemWoDTO.setWoId(10L);
    problemWoDTO.setPtStatusId(10L);
    PowerMockito.when(problemWoRepsitory.insertProblemWo(problemWoDTO)).thenReturn(resultInSideDto);
    ResultInSideDto result = problemWoBusiness.insertProblemWo(problemWoDTO);
    Assert.assertEquals(resultInSideDto, result);
  }

  @Test
  public void findProblemWoById() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemWoDTO problemWoDTO = new ProblemWoDTO();
    problemWoDTO.setProblemWoId(10L);
    PowerMockito.when(problemWoRepsitory.findProblemWoById(any())).thenReturn(problemWoDTO);
    ProblemWoDTO problemWoDTO1 = problemWoBusiness.findProblemWoById(10L);
    Assert.assertEquals(problemWoDTO, problemWoDTO1);
  }

  @Test
  public void deleteProblemWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.when(problemWoRepsitory.deleteProblemWo(any())).thenReturn(RESULT.SUCCESS);
    String result = problemWoBusiness.deleteProblemWo(10L);
    Assert.assertEquals(RESULT.SUCCESS, result);
  }

  @Test
  public void updateProblemWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ProblemWoDTO problemWoDTO = new ProblemWoDTO();
    problemWoDTO.setProblemWoId(10L);
    problemWoDTO.setProblemId(10L);
    problemWoDTO.setWoId(10L);
    problemWoDTO.setPtStatusId(10L);
    PowerMockito.when(problemWoRepsitory.updateProblemWo(problemWoDTO)).thenReturn(RESULT.SUCCESS);
    String result2 = problemWoBusiness.updateProblemWo(problemWoDTO);
    Assert.assertEquals(RESULT.SUCCESS, result2);
  }

  @Test
  public void deleteListProblemWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = PowerMockito.mock(String.class);
    PowerMockito.when(problemWoRepsitory.deleteListProblemWo(any())).thenReturn(result);
    ProblemWoDTO problemWoDTO = new ProblemWoDTO();
    List<ProblemWoDTO> problemWoDTOS = new ArrayList<>();
    problemWoDTOS.add(problemWoDTO);
    String result2 = problemWoBusiness.deleteListProblemWo(problemWoDTOS);
    Assert.assertEquals(result, result2);
  }

  @Test
  public void insertOrUpdateListProblemWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = PowerMockito.mock(String.class);
    List<ProblemWoDTO> problemWoDTOS = new ArrayList<>();
    ProblemWoDTO problemWoDTO = new ProblemWoDTO();
    problemWoDTO.setPtStatusId(10L);
    problemWoDTO.setWoId(10L);
    problemWoDTO.setPtStatusId(10L);
    problemWoDTO.setProblemWoId(10L);
    problemWoDTOS.add(problemWoDTO);
    PowerMockito.when(problemWoRepsitory.insertOrUpdateListProblemWo(problemWoDTOS))
        .thenReturn(result);
    String result2 = problemWoBusiness.insertOrUpdateListProblemWo(problemWoDTOS);
    Assert.assertEquals(result, result2);
  }

  @Test
  public void getListProblemWoByCondition() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List list = PowerMockito.mock(List.class);
    ConditionBean conditionBean = new ConditionBean();
    List<ConditionBean> conditionBeans = new ArrayList<>();
    conditionBean.setField("PROBLEM_WO_ID");
    conditionBean.setValue("");
    conditionBean.setType("");
    conditionBeans.add(conditionBean);
    int start = 1;
    int maxSize = 5;
    String sortType = "ASC";
    String sortName = "PROBLEM_ID";
    PowerMockito.when(problemWoRepsitory
        .getListProblemWoByCondition(conditionBeans, start, maxSize, sortType, sortName))
        .thenReturn(list);
    List result = problemWoBusiness
        .getListProblemWoByCondition(conditionBeans, start, maxSize, sortType, sortName);
    Assert.assertEquals(list, result);
  }

  @Test
  public void getSeqProblemWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
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
    WoSearchDTO woSearchDTO = Mockito.spy(WoSearchDTO.class);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setPage(1);
    problemsInsideDTO.setPageSize(10);
    problemsInsideDTO.setProblemCode("Test");
    problemsInsideDTO.setSortName("problemCode");
    problemsInsideDTO.setSortType("asc");
    problemsInsideDTO.setPage(1);
    problemsInsideDTO.setPageSize(1);
    woSearchDTO.setPage(problemsInsideDTO.getPage());
    woSearchDTO.setPageSize(problemsInsideDTO.getPageSize());
    woSearchDTO.setSortName(problemsInsideDTO.getSortName());
    woSearchDTO.setSortType(problemsInsideDTO.getSortType());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    Double offset = 1D;
    try {
      PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
      woSearchDTO.setUserId("1");
      woSearchDTO.setOffset(offset.longValue());
      woSearchDTO.setWoSystemId(problemsInsideDTO.getProblemCode());
      woSearchDTO.setUserId(String.valueOf(userToken.getDeptId()));
      Datatable datatable = problemWoRepsitory.getListDataSearchWeb(woSearchDTO);
      Assert.assertEquals(woSearchDTO, datatable);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
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
