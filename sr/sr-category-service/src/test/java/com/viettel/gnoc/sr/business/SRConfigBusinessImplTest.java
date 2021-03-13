package com.viettel.gnoc.sr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
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
@PrepareForTest({SRConfigBusinessImpl.class, TicketProvider.class})
public class SRConfigBusinessImplTest {

  @InjectMocks
  SRConfigBusinessImpl srConfigBusiness;

  @Mock
  SRConfigRepository srConfigRepository;

  @Mock
  TicketProvider ticketProvider;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void getListSRConfigPage() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(datatable.getTotal()).thenReturn(10);
    when(srConfigRepository.getListSRConfigPage(any())).thenReturn(datatable);
    setFinalStatic(SRConfigBusinessImpl.class.getDeclaredField("log"), logger);
    Datatable result = srConfigBusiness.getListSRConfigPage(new SRConfigDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void getListConfigGroup() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SRConfigDTO> data = new ArrayList<>();
    when(srConfigRepository.getListConfigGroup(any())).thenReturn(data);
    setFinalStatic(SRConfigBusinessImpl.class.getDeclaredField("log"), logger);
    List<SRConfigDTO> result = srConfigBusiness.getListConfigGroup("123");
    assertEquals(data, result);
  }

  @Test
  public void getByConfigGroup() throws Exception{
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    List<SRConfigDTO> data = new ArrayList<>();
    when(srConfigRepository.getByConfigGroup(any())).thenReturn(data);
    List<SRConfigDTO> result = srConfigBusiness.getByConfigGroup(srConfigDTO);
    assertEquals(data, result);
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
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    PowerMockito.when(srConfigRepository.add(any())).thenReturn(resultInSideDto);
    srConfigBusiness.insert(srConfigDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getDetail() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRConfigDTO srConfigDTO = new SRConfigDTO();
    when(srConfigRepository.getDetail(any()))
        .thenReturn(srConfigDTO);
    setFinalStatic(SRConfigBusinessImpl.class.getDeclaredField("log"), logger);
    SRConfigDTO result = srConfigBusiness.getDetail(10L);
    assertEquals(srConfigDTO, result);
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
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    PowerMockito.when(srConfigRepository.getDetail(any())).thenReturn(srConfigDTO);
    PowerMockito.when(srConfigRepository.add(any())).thenReturn(resultInSideDto);
    srConfigBusiness.update(srConfigDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void delete() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(srConfigRepository.delete(any())).thenReturn(resultInSideDto);
    setFinalStatic(SRConfigBusinessImpl.class.getDeclaredField("log"), logger);
    ResultInSideDto result = srConfigBusiness.delete(123L);
    assertEquals(resultInSideDto, result);
  }

  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }
}
