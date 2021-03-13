package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MapFlowTemplatesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.repository.MapFlowTemplatesRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MapFlowTemplatesBusinessImpl.class, TicketProvider.class})
public class MapFlowTemplatesControllerTest {

  @InjectMocks
  MapFlowTemplatesBusinessImpl mapFlowTemplatesBusiness;

  @Mock
  MapFlowTemplatesRepository mapFlowTemplatesRepository;

  @Mock
  TicketProvider ticketProvider;


  @Test
  public void testDeleteMapFlowTemplates_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = mapFlowTemplatesBusiness.deleteListMapFlowTemplates(any());
    PowerMockito.when(mapFlowTemplatesRepository.deleteMapFlowTemplates(any()))
        .thenReturn(resultInSideDto);
    Assert.assertEquals(resultInSideDto, null);
  }

  @Test
  public void testDeleteListMapFlowTemplates_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = mapFlowTemplatesBusiness.deleteListMapFlowTemplates(any());
    PowerMockito.when(mapFlowTemplatesRepository.deleteListMapFlowTemplates(any()))
        .thenReturn(resultInSideDto);
    Assert.assertEquals(resultInSideDto, null);
  }

  @Test
  public void testFindMapFlowTemplatesById_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MapFlowTemplatesDTO mapFlowTemplatesDTO = mapFlowTemplatesBusiness
        .findMapFlowTemplatesById(any());
    PowerMockito.when(mapFlowTemplatesRepository.findMapFlowTemplatesById(any()))
        .thenReturn(mapFlowTemplatesDTO);
    Assert.assertEquals(mapFlowTemplatesDTO, null);
  }

  @Test
  public void testGetSequenseMapFlowTemplates_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> list = mapFlowTemplatesBusiness.getSequenseMapFlowTemplates(any(), any());
    PowerMockito.when(mapFlowTemplatesRepository.getSequenseMapFlowTemplates(any(), any()))
        .thenReturn(list);
    Assert.assertEquals(list.size(), 0);
  }

  @Test
  public void testGetListMapFlowTemplatesDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = mapFlowTemplatesBusiness.getListMapFlowTemplatesDTO(any());
    PowerMockito.when(mapFlowTemplatesRepository.getListMapFlowTemplatesDTO(any()))
        .thenReturn(datatable);
    Assert.assertEquals(datatable, null);
  }

  @Test
  public void testGetListMapFlowTemplates_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<MapFlowTemplatesDTO> lst = mapFlowTemplatesBusiness
        .getListMapFlowTemplates(any(), anyInt(), anyInt(), anyString(), anyString());
    PowerMockito.when(mapFlowTemplatesRepository
        .getListMapFlowTemplates(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);
    Assert.assertEquals(lst.size(), 0);
  }

  @Test
  public void testUpdateMapFlowTemplates_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MapFlowTemplatesDTO mapFlowTemplatesDTO = Mockito.spy(MapFlowTemplatesDTO.class);
    mapFlowTemplatesDTO.setLastUpdateTime("10/06/2019 00:00:00");
    mapFlowTemplatesDTO.setUserID("1");
    ResultInSideDto resultInSideDto = mapFlowTemplatesBusiness
        .updateMapFlowTemplates(mapFlowTemplatesDTO);
    Assert.assertEquals(resultInSideDto, null);
  }

  @Test
  public void testInsertMapFlowTemplates_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MapFlowTemplatesDTO mapFlowTemplatesDTO = Mockito.spy(MapFlowTemplatesDTO.class);
    mapFlowTemplatesDTO.setLastUpdateTime("10/06/2019 00:00:00");
    mapFlowTemplatesDTO.setUserID("1");
    ResultInSideDto resultInSideDto = mapFlowTemplatesBusiness
        .insertMapFlowTemplates(mapFlowTemplatesDTO);
    Assert.assertEquals(resultInSideDto, null);
  }

  @Test
  public void testInsertOrUpdateListMapFlowTemplates_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MapFlowTemplatesDTO dto = Mockito.spy(MapFlowTemplatesDTO.class);
    dto.setLastUpdateTime("10/06/2019 00:00:00");
    dto.setUserID("1");
    List<MapFlowTemplatesDTO> mapFlowTemplatesDTO = Mockito.spy(ArrayList.class);
    mapFlowTemplatesDTO.add(dto);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = mapFlowTemplatesBusiness
        .insertOrUpdateListMapFlowTemplates(mapFlowTemplatesDTO);
    Assert.assertEquals(resultInSideDto, null);
  }
}
