package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TableConfigUserDTO;
import com.viettel.gnoc.repository.TableConfigUserRepository;
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
@PrepareForTest({TableConfigUserBusinessImpl.class, TicketProvider.class})
public class TableConfigUserControllerTest {

  @InjectMocks
  TableConfigUserBusinessImpl tableConfigUserBusiness;

  @Mock
  TableConfigUserRepository tableConfigUserRepository;
  @Mock
  TicketProvider ticketProvider;

//  @Test
//  public void testGetListTableConfigUserDTO_01() {
//    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//    UserToken userToken = Mockito.spy(UserToken.class);
//    userToken.setDeptId(1L);
//    userToken.setUserName("thanhlv12");
//    userToken.setTelephone("0123456789");
//    userToken.setUserID(1L);
//    PowerMockito.mockStatic(TicketProvider.class);
//    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
//    TableConfigUserDTO tableConfigUserDTO = Mockito.spy(TableConfigUserDTO.class);
//    try {
//      List<TableConfigUserDTO> lst = tableConfigUserBusiness
//          .getListTableConfigUserDTO(tableConfigUserDTO, anyInt(), anyInt(), anyString(),
//              anyString());
//    }catch (Exception e){
//      e.printStackTrace();
//    }
//    Assert.assertEquals(tableConfigUserDTO.getUserId(), null);
//  }

  @Test
  public void testGetListTableConfigUserByCondition_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<TableConfigUserDTO> lst = tableConfigUserBusiness
        .getListTableConfigUserByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString());
    PowerMockito.when(tableConfigUserRepository
        .getListTableConfigUserByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);
    Assert.assertEquals(lst.size(), 0);
  }

  @Test
  public void testGetSequenseTableConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> lst = tableConfigUserBusiness.getSequenseTableConfigUser(any(), any());
    PowerMockito.when(tableConfigUserRepository.getSequenseTableConfigUser(any(), any()))
        .thenReturn(lst);
    Assert.assertEquals(lst.size(), 0);
  }

  @Test
  public void testUpdateTableConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TableConfigUserDTO tableConfigUserDTO = Mockito.spy(TableConfigUserDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    String result = tableConfigUserBusiness.updateTableConfigUser(tableConfigUserDTO);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testInsertTableConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TableConfigUserDTO tableConfigUserDTO = Mockito.spy(TableConfigUserDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = tableConfigUserBusiness.insertTableConfigUser(tableConfigUserDTO);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testInsertOrUpdateListTableConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = tableConfigUserBusiness.insertOrUpdateListTableConfigUser(any());
    PowerMockito.when(tableConfigUserRepository.insertOrUpdateListTableConfigUser(any()))
        .thenReturn(result);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testFindTableConfigUserById_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TableConfigUserDTO tableConfigUserDTO = tableConfigUserBusiness.findTableConfigUserById(any());
    PowerMockito.when(tableConfigUserRepository.findTableConfigUserById(any()))
        .thenReturn(tableConfigUserDTO);
    Assert.assertEquals(tableConfigUserDTO, null);
  }

  @Test
  public void testDeleteTableConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = tableConfigUserBusiness.deleteTableConfigUser(any());
    PowerMockito.when(tableConfigUserRepository.deleteTableConfigUser(any())).thenReturn(
        resultInSideDto);
    Assert.assertEquals(resultInSideDto, null);
  }

  @Test
  public void testDeleteListTableConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = tableConfigUserBusiness.deleteListTableConfigUser(any());
    PowerMockito.when(tableConfigUserRepository.deleteListTableConfigUser(any()))
        .thenReturn(result);
    Assert.assertEquals(result, null);
  }
}
