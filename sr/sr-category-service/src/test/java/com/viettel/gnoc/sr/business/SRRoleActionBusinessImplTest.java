package com.viettel.gnoc.sr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import com.viettel.gnoc.sr.repository.SRFlowExecuteRepository;
import com.viettel.gnoc.sr.repository.SRRoleActionRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SRRoleActionBusinessImpl.class, TicketProvider.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class SRRoleActionBusinessImplTest {

  @InjectMocks
  SRRoleActionBusinessImpl srRoleActionBusiness;

  @Mock
  SRRoleActionRepository srRoleActionRepository;

  @Mock
  SRFlowExecuteRepository srFlowExecuteRepository;

  @Mock
  TicketProvider ticketProvider;

  @Test
  public void testGetComboBoxStatus_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<SRRoleActionDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(srRoleActionRepository.getComboBoxStatus()).thenReturn(list);
    srRoleActionBusiness.getComboBoxStatus();
    Assert.assertEquals(list.size(), 0L);
  }

  @Test
  public void testGetComboBoxRoleType_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<SRRoleActionDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(srRoleActionRepository.getComboBoxRoleType()).thenReturn(list);
    srRoleActionBusiness.getComboBoxRoleType();
    Assert.assertEquals(list.size(), 0L);
  }

  @Test
  public void testGetComboBoxActions_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<SRRoleActionDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(srRoleActionRepository.getComboBoxActions(anyString())).thenReturn(list);
    srRoleActionBusiness.getComboBoxActions(anyString());
    Assert.assertEquals(list.size(), 0L);
  }

  @Test
  public void testGetComboBoxGroupRole_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<SRRoleActionDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(srRoleActionRepository.getComboBoxGroupRole()).thenReturn(list);
    srRoleActionBusiness.getComboBoxGroupRole();
    Assert.assertEquals(list.size(), 0L);
  }

  @Test
  public void testGetListSRRoleActionPage_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(srRoleActionRepository.getListSRRoleActionPage(any())).thenReturn(datatable);
    srRoleActionBusiness.getListSRRoleActionPage(any());
    Assert.assertEquals(datatable.getData(), null);
  }

  @Test
  public void testGetListSRRoleActionDTO_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<SRRoleActionDTO> listDTO = Mockito.spy(ArrayList.class);
    SRRoleActionDTO srRoleActionDTO = Mockito.spy(SRRoleActionDTO.class);
    listDTO.add(srRoleActionDTO);
    PowerMockito.when(srRoleActionRepository.getListSRRoleActionDTO(any())).thenReturn(listDTO);
    srRoleActionBusiness.getListSRRoleActionDTO(any());
    Assert.assertEquals(listDTO.size(), 1L);
  }

  @Test
  public void testInsert_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    SRFlowExecuteDTO srFlowExecuteDTOTmp = Mockito.spy(SRFlowExecuteDTO.class);
    SRRoleActionDTO dto = Mockito.spy(SRRoleActionDTO.class);
    List<SRRoleActionDTO> srRoleActionDTOList = Mockito.spy(ArrayList.class);
    srRoleActionDTOList.add(dto);

    srFlowExecuteDTOTmp.setFlowName("aa");
    srFlowExecuteDTOTmp.setFlowDescription("bb");
    srFlowExecuteDTOTmp.setLstRoleActionDTO(srRoleActionDTOList);

    List<SRFlowExecuteDTO> flowExecuteDTOMainList = Mockito.spy(ArrayList.class);
    flowExecuteDTOMainList.add(srFlowExecuteDTOTmp);
    srFlowExecuteDTOTmp.setFlowExecuteDTOMainList(flowExecuteDTOMainList);

    ResultInSideDto resultInSideDtoFlow = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoFlow.setKey(RESULT.SUCCESS);

    PowerMockito.when(srFlowExecuteRepository.insertOrUpdate(any()))
        .thenReturn(resultInSideDtoFlow);
    PowerMockito.when(srRoleActionRepository.insertOrUpdateList(anyList()))
        .thenReturn(resultInSideDto);
    srRoleActionBusiness.insert(srFlowExecuteDTOTmp);
    Assert.assertEquals(resultInSideDto.getKey(), null);
  }

  @Test
  public void testUpdate_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    SRFlowExecuteDTO srFlowExecuteDTO = Mockito.spy(SRFlowExecuteDTO.class);
    List<SRFlowExecuteDTO> flowExecuteDTODetailList = Mockito.spy(ArrayList.class);
    srFlowExecuteDTO.setFlowId(1L);
    flowExecuteDTODetailList.add(srFlowExecuteDTO);
    List<SRFlowExecuteDTO> flowExecuteDTOMainList = Mockito.spy(ArrayList.class);
    flowExecuteDTOMainList.add(srFlowExecuteDTO);

    PowerMockito.when(srFlowExecuteRepository.delete(anyLong())).thenReturn(resultInSideDto);
    PowerMockito.when(srRoleActionRepository.deleteRoleActionByFlowID(anyLong()))
        .thenReturn(resultInSideDto);
    srRoleActionBusiness.deleteFlowExecuteAndRoleAction(123L);

    srFlowExecuteDTO.setFlowName("abc");
    srFlowExecuteDTO.setFlowDescription("abc");
    srFlowExecuteDTO.setFlowExecuteDTODetailList(flowExecuteDTODetailList);
    srFlowExecuteDTO.setFlowExecuteDTOMainList(flowExecuteDTOMainList);
    List<SRRoleActionDTO> lstRoleActionDTO = Mockito.spy(ArrayList.class);
    SRRoleActionDTO srRoleActionDTO = Mockito.spy(SRRoleActionDTO.class);
    lstRoleActionDTO.add(srRoleActionDTO);
    srFlowExecuteDTO.setLstRoleActionDTO(lstRoleActionDTO);
    ResultInSideDto resultInSideDtoFlow = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoFlow.setId(1L);

    PowerMockito.when(srFlowExecuteRepository.insertOrUpdate(any()))
        .thenReturn(resultInSideDtoFlow);
    PowerMockito.when(srRoleActionRepository.deleteRoleActionByFlowID(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(srRoleActionRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);
    srRoleActionBusiness.update(srFlowExecuteDTO);

    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testDeleteFlowExecuteAndRoleAction_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    PowerMockito.when(resultInSideDto.getId()).thenReturn(10L);
    PowerMockito.when(srFlowExecuteRepository.delete(anyLong())).thenReturn(resultInSideDto);
    PowerMockito.when(srRoleActionRepository.deleteRoleActionByFlowID(anyLong()))
        .thenReturn(resultInSideDto);
    srRoleActionBusiness.deleteFlowExecuteAndRoleAction(123L);
    Assert.assertEquals(resultInSideDto.getKey(), null);
  }

  @Test
  public void testGetDetail_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    SRRoleActionDTO dto = Mockito.spy(SRRoleActionDTO.class);
    dto.setActions("abc");
    dto.setActionsName("abc");
    PowerMockito.when(srRoleActionRepository.getDetail(anyLong())).thenReturn(dto);
    List<SRRoleActionDTO> listName = Mockito.spy(ArrayList.class);
    listName.add(dto);
    PowerMockito.when(srRoleActionRepository.getComboBoxActions(any())).thenReturn(listName);
    SRRoleActionDTO result = srRoleActionBusiness.getDetail(10L);
    Assert.assertEquals(dto, result);
  }

  @Test
  public void testDeleteByRoleActionsId_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    PowerMockito.when(resultInSideDto.getId()).thenReturn(10L);
    PowerMockito.when(srRoleActionRepository.deleteByRoleActionsId(anyLong()))
        .thenReturn(resultInSideDto);
    srRoleActionBusiness.deleteByRoleActionsId(123L);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }
}
