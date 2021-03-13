package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SearchConfigUserDTO;
import com.viettel.gnoc.repository.SearchConfigUserRepository;
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
@PrepareForTest({SearchConfigUserBusinessImpl.class, TicketProvider.class})
public class SearchConfigUserBusinessTest {

  @InjectMocks
  SearchConfigUserBusinessImpl searchConfigUserBusiness;

  @Mock
  SearchConfigUserRepository searchConfigUserRepository;

  @Mock
  TicketProvider ticketProvider;

  @Test
  public void testGetListSearchConfigUserByCondition_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SearchConfigUserDTO> lst = searchConfigUserBusiness
        .getListSearchConfigUserByCondition(anyList(), anyInt(), anyInt(), anyString(),
            anyString());
    PowerMockito.when(searchConfigUserRepository
        .getListSearchConfigUserByCondition(anyList(), anyInt(), anyInt(), anyString(),
            anyString())).thenReturn(lst);
    Assert.assertEquals(lst.size(), 0);
  }

  @Test
  public void testGetSequenseSearchConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> list = searchConfigUserBusiness.getSequenseSearchConfigUser(any(), any());
    PowerMockito.when(searchConfigUserRepository.getSequenseSearchConfigUser(any(), any()))
        .thenReturn(list);
    Assert.assertEquals(list.size(), 0);
  }

  @Test
  public void testSearchConfigUserDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SearchConfigUserDTO searchConfigUserDTO = searchConfigUserBusiness
        .findSearchConfigUserById(any());
    PowerMockito.when(searchConfigUserRepository.findSearchConfigUserById(any()))
        .thenReturn(searchConfigUserDTO);
    Assert.assertEquals(searchConfigUserDTO, null);
  }

  @Test
  public void testDeleteSearchConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = searchConfigUserBusiness.deleteSearchConfigUser(any());
    PowerMockito.when(searchConfigUserRepository.deleteSearchConfigUser(any())).thenReturn(result);
    Assert.assertEquals(result, null);
  }

//  @Test
//  public void testGetListSearchConfigUserDTO_01(){
//    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//    UserToken userToken = Mockito.spy(UserToken.class);
//    userToken.setDeptId(1L);
//    userToken.setUserName("thanhlv12");
//    userToken.setTelephone("0123456789");
//    userToken.setUserID(1L);
//    PowerMockito.mockStatic(TicketProvider.class);
//    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
//    SearchConfigUserDTO searchConfigUserDTO = Mockito.spy(SearchConfigUserDTO.class);
//    try {
//      List<SearchConfigUserDTO> searchConfigUserDTOS = searchConfigUserBusiness.getListSearchConfigUserDTO(searchConfigUserDTO, anyInt(), anyInt(), anyString(), anyString());
//    }catch (Exception e){
//      e.printStackTrace();
//    }
//    Assert.assertEquals(searchConfigUserDTO.getUserName(), null);
//  }

  @Test
  public void testUpdateSearchConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SearchConfigUserDTO searchConfigUserDTO = Mockito.spy(SearchConfigUserDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    String result = searchConfigUserBusiness.updateSearchConfigUser(searchConfigUserDTO);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testInsertSearchConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SearchConfigUserDTO searchConfigUserDTO = Mockito.spy(SearchConfigUserDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = searchConfigUserBusiness.insertSearchConfigUser(searchConfigUserDTO);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testInsertOrUpdateListSearchConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SearchConfigUserDTO searchConfigUserDTO = Mockito.spy(SearchConfigUserDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = searchConfigUserBusiness
        .insertOrUpdateListSearchConfigUser(searchConfigUserDTO);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testDeleteListSearchConfigUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SearchConfigUserDTO searchConfigUserDTO = Mockito.spy(SearchConfigUserDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = searchConfigUserBusiness
        .deleteListSearchConfigUser(searchConfigUserDTO);
    Assert.assertEquals(result, null);
  }
}
