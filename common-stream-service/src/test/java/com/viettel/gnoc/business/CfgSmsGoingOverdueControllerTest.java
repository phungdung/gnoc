package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.dto.CfgSmsGoingOverdueDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.repository.CfgSmsGoingOverdueRepository;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgSmsGoingOverdueBusinessImpl.class})
public class CfgSmsGoingOverdueControllerTest {

  @InjectMocks
  CfgSmsGoingOverdueBusinessImpl cfgSmsGoingOverdueBusiness;

  @Mock
  CfgSmsGoingOverdueRepository cfgSmsGoingOverdueRepository;

  @Test
  public void testGetListCfgSmsGoingOverdueDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = cfgSmsGoingOverdueBusiness.getListCfgSmsGoingOverdueDTO(any());
    PowerMockito.when(cfgSmsGoingOverdueRepository.getListCfgSmsGoingOverdueDTO(any()))
        .thenReturn(datatable);
    Assert.assertEquals(datatable, null);
  }

  @Test
  public void testUpdateCfgSmsGoingOverdue_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = cfgSmsGoingOverdueBusiness.updateCfgSmsGoingOverdue(any());
    PowerMockito.when(cfgSmsGoingOverdueRepository.updateCfgSmsGoingOverdue(any()))
        .thenReturn(result);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testDeleteCfgSmsGoingOverdue_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = cfgSmsGoingOverdueBusiness.deleteCfgSmsGoingOverdue(any());
    PowerMockito.when(cfgSmsGoingOverdueRepository.deleteCfgSmsGoingOverdue(any()))
        .thenReturn(result);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testDeleteListCfgSmsGoingOverdue_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = cfgSmsGoingOverdueBusiness.deleteListCfgSmsGoingOverdue(anyList());
    PowerMockito.when(cfgSmsGoingOverdueRepository.deleteListCfgSmsGoingOverdue(anyList()))
        .thenReturn(result);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testFindCfgSmsGoingOverdueById_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO = cfgSmsGoingOverdueBusiness
        .findCfgSmsGoingOverdueById(any());
    PowerMockito.when(cfgSmsGoingOverdueRepository.findCfgSmsGoingOverdueById(any()))
        .thenReturn(cfgSmsGoingOverdueDTO);
    Assert.assertEquals(cfgSmsGoingOverdueDTO, null);
  }

  @Test
  public void testInsertCfgSmsGoingOverdue_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = cfgSmsGoingOverdueBusiness.insertCfgSmsGoingOverdue(any());
    PowerMockito.when(cfgSmsGoingOverdueRepository.insertCfgSmsGoingOverdue(any()))
        .thenReturn(resultInSideDto);
    Assert.assertEquals(resultInSideDto, null);
  }

  @Test
  public void testGetSequenseCfgSmsGoingOverdue_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> lst = cfgSmsGoingOverdueBusiness.getSequenseCfgSmsGoingOverdue(any(), any());
    PowerMockito
        .when(cfgSmsGoingOverdueRepository.getSequenseCfgSmsGoingOverdue(any(), any()))
        .thenReturn(lst);
    Assert.assertEquals(lst.size(), 0);
  }

  @Test
  public void testGetListCfgSmsGoingOverdueByCondition_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CfgSmsGoingOverdueDTO> dtos = cfgSmsGoingOverdueBusiness
        .getListCfgSmsGoingOverdueByCondition(anyList(), anyInt(), anyInt(), anyString(),
            anyString());
    PowerMockito.when(cfgSmsGoingOverdueRepository
        .getListCfgSmsGoingOverdueByCondition(anyList(), anyInt(), anyInt(), anyString(),
            anyString())).thenReturn(dtos);
    Assert.assertEquals(dtos.size(), 0);
  }

  @Test
  public void testGetUserInfo_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UsersInsideDto usersInsideDto = cfgSmsGoingOverdueBusiness.getUserInfo(anyLong());
    PowerMockito.when(cfgSmsGoingOverdueRepository.getUserInfo(anyLong())).thenReturn(
        usersInsideDto);
    Assert.assertEquals(usersInsideDto, null);
  }

  @Test
  public void testInsertOrUpdateCfg_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = cfgSmsGoingOverdueBusiness.insertOrUpdateCfg(anyList());
    PowerMockito.when(cfgSmsGoingOverdueRepository.insertOrUpdateCfg(anyList())).thenReturn(result);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testDeleteCfgSmsGoingOverdueAndUserList_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = cfgSmsGoingOverdueBusiness
        .deleteCfgSmsGoingOverdueAndUserList(any());
    PowerMockito.when(cfgSmsGoingOverdueRepository.deleteCfgSmsGoingOverdueAndUserList(any()))
        .thenReturn(resultInSideDto);
    Assert.assertEquals(resultInSideDto, null);
  }

  @Test
  public void testGetListCfgSmsGoingOverdueDTO_allFields_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CfgSmsGoingOverdueDTO> lst = cfgSmsGoingOverdueBusiness
        .getListCfgSmsGoingOverdueDTO_allFields(anyString(), anyString(), anyString(), anyString());
    PowerMockito.when(cfgSmsGoingOverdueRepository
        .getListCfgSmsGoingOverdueDTO_allFields(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(lst);
    Assert.assertEquals(lst.size(), 0);
  }

  @Test
  public void testGetListCfgSmsUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = cfgSmsGoingOverdueBusiness.getListCfgSmsUser(any());
    PowerMockito.when(cfgSmsGoingOverdueRepository.getListCfgSmsUser(any())).thenReturn(datatable);
    Assert.assertEquals(datatable, null);
  }

  @Test
  public void testUpdateCfgSmsGoingOverdue2_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = cfgSmsGoingOverdueBusiness.updateCfgSmsGoingOverdue2(any());
    PowerMockito.when(cfgSmsGoingOverdueRepository.updateCfgSmsGoingOverdue2(any())).thenReturn(
        resultInSideDto);
    Assert.assertEquals(resultInSideDto, null);
  }

  @Test
  public void testGetMaxLevelIDByUnitID_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Long aLong = cfgSmsGoingOverdueBusiness.getMaxLevelIDByUnitID(any());
    PowerMockito.when(cfgSmsGoingOverdueRepository.getMaxLevelIDByUnitID(any())).thenReturn(aLong);
    Assert.assertEquals(aLong, Long.valueOf(0));
  }
}
