package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.repository.CfgUnitTtSpmRepository;
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
@PrepareForTest({CfgUnitTtSpmBusinessImpl.class, TicketProvider.class})
public class CfgUnitTtSpmControllerTest {

  @InjectMocks
  CfgUnitTtSpmBusinessImpl cfgUnitTtSpmBusiness;

  @Mock
  CfgUnitTtSpmRepository cfgUnitTtSpmRepository;

  @Test
  public void testUpdateCfgUnitTtSpm_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = cfgUnitTtSpmBusiness.updateCfgUnitTtSpm(any());
    PowerMockito.when(cfgUnitTtSpmRepository.updateCfgUnitTtSpm(any())).thenReturn(result);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testDeleteCfgUnitTtSpm_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = cfgUnitTtSpmBusiness.deleteCfgUnitTtSpm(any());
    PowerMockito.when(cfgUnitTtSpmRepository.deleteCfgUnitTtSpm(any())).thenReturn(result);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testDeleteListCfgUnitTtSpm_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = cfgUnitTtSpmBusiness.deleteListCfgUnitTtSpm(any());
    PowerMockito.when(cfgUnitTtSpmRepository.deleteListCfgUnitTtSpm(any())).thenReturn(result);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testGetListCfgUnitTtSpmDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CfgUnitTtSpmDTO> dtos = cfgUnitTtSpmBusiness
        .getListCfgUnitTtSpmDTO(any(), anyInt(), anyInt(), anyString(),
            anyString());
    PowerMockito.when(cfgUnitTtSpmRepository
        .getListCfgUnitTtSpmDTO(any(), anyInt(), anyInt(), anyString(),
            anyString())).thenReturn(dtos);
    Assert.assertEquals(dtos.size(), 0);
  }

  @Test
  public void testInsertCfgUnitTtSpm_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = cfgUnitTtSpmBusiness.insertCfgUnitTtSpm(any());
    PowerMockito.when(cfgUnitTtSpmRepository.insertCfgUnitTtSpm(any()))
        .thenReturn(resultInSideDto);
    Assert.assertEquals(resultInSideDto, null);
  }

  @Test
  public void testInsertOrUpdateListCfgUnitTtSpm_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = cfgUnitTtSpmBusiness.insertOrUpdateListCfgUnitTtSpm(any());
    PowerMockito.when(cfgUnitTtSpmRepository.insertOrUpdateListCfgUnitTtSpm(any()))
        .thenReturn(result);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testGetSequenseCfgUnitTtSpm_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> lst = cfgUnitTtSpmBusiness.getSequenseCfgUnitTtSpm(any(), any());
    PowerMockito.when(cfgUnitTtSpmRepository.getSequenseCfgUnitTtSpm(any(), any())).thenReturn(lst);
    Assert.assertEquals(lst.size(), 0);
  }

  @Test
  public void testFindCfgUnitTtSpmById_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CfgUnitTtSpmDTO cfgUnitTtSpmDTO = cfgUnitTtSpmBusiness.findCfgUnitTtSpmById(any());
    PowerMockito.when(cfgUnitTtSpmRepository.findCfgUnitTtSpmById(any()))
        .thenReturn(cfgUnitTtSpmDTO);
    Assert.assertEquals(cfgUnitTtSpmDTO, null);
  }

  @Test
  public void testGetListUnitTtSpmSearch_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = cfgUnitTtSpmBusiness.getListUnitTtSpmSearch(any());
    PowerMockito.when(cfgUnitTtSpmRepository.getListUnitTtSpmSearch(any())).thenReturn(datatable);
    Assert.assertEquals(datatable, null);
  }
}
