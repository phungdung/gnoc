package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
import com.viettel.gnoc.mr.repository.MrWoTempRepository;
import com.viettel.security.PassTranformer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@PrepareForTest({MrWoTempBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class, PassTranformer.class})
public class MrWoTempBusinessImplTest {

  @InjectMocks
  MrWoTempBusinessImpl mrWoTempBusiness;

  @Mock
  MrWoTempRepository mrWoTempRepository;

  @Mock
  UserBusiness userBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UserToken userToken;

  @Test
  public void test_getListMrWoTempDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrWoTempDTO mrWoTempDTO = Mockito.spy(MrWoTempDTO.class);
    mrWoTempDTO.setCdId("5");
    List<MrWoTempDTO> mrWoTempDTOList = Mockito.spy(ArrayList.class);
    mrWoTempDTOList.add(mrWoTempDTO);
    Mockito.when(mrWoTempRepository.search(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(mrWoTempDTOList);
    List<MrWoTempDTO> mrWoTempDTOList1 = mrWoTempBusiness
        .getListMrWoTempDTO(mrWoTempDTO, 1, 1, "aaa", "aaaa");
    assertEquals(mrWoTempDTOList1.size(), mrWoTempDTOList.size());
  }

  @Test
  public void test_updateMrWoTemp() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
//    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    MrWoTempDTO mrWoTemp = Mockito.spy(MrWoTempDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    mrWoTemp.setCreateDate(DateTimeUtils.convertDateTimeStampToString(new Date()));
    mrWoTemp.setStartTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
    mrWoTemp.setEndTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
    mrWoTemp.setLastUpdateTime(DateTimeUtils.convertDateTimeStampToString(new Date()));

    Mockito.when(userBusiness.getOffsetFromUser(userToken.getUserID())).thenReturn(1.1);
    ResultInSideDto resultInSideDto1 = mrWoTempBusiness.updateMrWoTemp(mrWoTemp);

    Mockito.when(mrWoTempRepository.updateMrWoTemp(mrWoTemp)).thenReturn(resultInSideDto);
    assertNull(resultInSideDto1);


  }
}
