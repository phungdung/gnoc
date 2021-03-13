package com.viettel.gnoc.sr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.sr.dto.SRStatusDTO;
import com.viettel.gnoc.sr.repository.SRStatusRepository;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({SRStatusBusinessImpl.class, TicketProvider.class})
public class SRStatusBusinessImplTest {

  @InjectMocks
  SRStatusBusinessImpl srStatusBusiness;

  @Mock
  SRStatusRepository srStatusRepository;

  @Mock
  TicketProvider ticketProvider;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void getListSRStatusPage() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRStatusDTO srStatusDTO = Mockito.spy(SRStatusDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(srStatusRepository.getListSRStatusPage(any())).thenReturn(datatable);
    Datatable data = srStatusBusiness.getListSRStatusPage(srStatusDTO);
    Assert.assertEquals(datatable.getPages(), data.getPages());
  }

  @Test
  public void insert() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRStatusDTO srStatusDTO = Mockito.spy(SRStatusDTO.class);
    PowerMockito.when(srStatusRepository.add(any())).thenReturn(resultInSideDto);
    srStatusBusiness.insert(srStatusDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void update() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRStatusDTO srStatusDTO = Mockito.spy(SRStatusDTO.class);
    PowerMockito.when(srStatusRepository.add(any())).thenReturn(resultInSideDto);
    srStatusBusiness.update(srStatusDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getDetail() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRStatusDTO srStatusDTO = new SRStatusDTO();
    when(srStatusRepository.getDetail(any()))
        .thenReturn(srStatusDTO);
    setFinalStatic(SRStatusBusinessImpl.class.getDeclaredField("log"), logger);
    SRStatusDTO result = srStatusBusiness.getDetail(10L);
    assertEquals(srStatusDTO, result);
  }

  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }
}
