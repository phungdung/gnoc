package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.mr.repository.MrServiceRepository;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrServiceBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
public class MrServiceBusinessImplTest {

  @InjectMocks
  MrServiceBusinessImpl mrServiceBusiness;

  @Mock
  MrServiceRepository mrServiceRepository;

  @Mock
  TicketProvider ticketProvider;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  UserBusiness userBusiness;

  @Mock
  MrApprovalDepartmentBusinessImpl mrApprovalDepartmentBusiness;

  @Test
  public void test_getListMrCrWoNew() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrSearchDTO mrSearchDTO = Mockito.spy(MrSearchDTO.class);
    Datatable dtoList = Mockito.spy(Datatable.class);
    PowerMockito.when(mrServiceRepository.getListMrCrWoNew(any()))
        .thenReturn(dtoList);
    Datatable dtoListEquals = mrServiceBusiness.getListMrCrWoNew(mrSearchDTO);
    assertEquals(dtoListEquals.getTotal(), dtoList.getTotal());
  }

  @Test
  public void test_getWorklogFromWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrSearchDTO mrSearchDTO = Mockito.spy(MrSearchDTO.class);
    List<MrDTO> dtoList = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrServiceRepository.getWorklogFromWo(any()))
        .thenReturn(dtoList);
    List list = mrServiceBusiness.getWorklogFromWo(mrSearchDTO);
    assertEquals(list.size(), dtoList.size());
  }

  @Test
  public void test_getIdSequence() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> dtoList = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrServiceRepository.getIdSequence()).thenReturn(dtoList);
    List list = mrServiceBusiness.getIdSequence();
    assertEquals(list.size(), dtoList.size());
  }

  @Test
  public void test_insertMr() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    mrInsideDTO.setEarliestTime(new Date());
    mrInsideDTO.setLastestTime(new Date());
    mrInsideDTO.setNextWoCreate(new Date());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(mrServiceRepository.insertMr(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrServiceBusiness.insertMr(mrInsideDTO);
    when(userBusiness.getOffsetFromUser(any())).thenReturn(5.0);
    assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void test_updateMr() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    mrInsideDTO.setEarliestTime(new Date());
    mrInsideDTO.setLastestTime(new Date());
    mrInsideDTO.setCreatedTime(new Date());
    mrInsideDTO.setNextWoCreate(new Date());
    when(mrApprovalDepartmentBusiness.getOffset()).thenReturn(5.0);
    PowerMockito.when(mrServiceRepository.updateMr(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrServiceBusiness.updateMr(mrInsideDTO);
    assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void test_getListWorklogSearch() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WorkLogInsiteDTO workLogInsiteDTO = Mockito.spy(WorkLogInsiteDTO.class);
    List<WorkLogInsiteDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrServiceRepository.getListWorklogSearch(any())).thenReturn(list);
    List<WorkLogInsiteDTO> list1 = mrServiceBusiness.getListWorklogSearch(workLogInsiteDTO);
    assertEquals(list1.size(), list.size());
  }
}
