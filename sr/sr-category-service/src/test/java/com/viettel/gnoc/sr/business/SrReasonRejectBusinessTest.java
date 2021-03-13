package com.viettel.gnoc.sr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.sr.dto.SRReasonRejectDTO;
import com.viettel.gnoc.sr.repository.SRReasonRejectRepository;
import com.viettel.gnoc.ws.provider.WSCxfInInterceptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import javax.xml.ws.WebServiceContext;
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
@PrepareForTest({SRReasonRejectBusinessImpl.class, FileUtils.class, I18n.class,
    CommonImport.class, CommonExport.class, WebServiceContext.class, WSCxfInInterceptor.class,
    DataUtil.class, TicketProvider.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class SrReasonRejectBusinessTest {

  @Mock
  TicketProvider ticketProvider;

  @InjectMocks
  protected SRReasonRejectBusinessImpl srReasonRejectBusiness;

  @Mock
  protected SRReasonRejectRepository srReasonRejectRepository;

  @Mock
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  static void setFinalStatic(Field fields, Object val) throws Exception {
    fields.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(fields, fields.getModifiers() & ~Modifier.FINAL);
    fields.set(null, val);
  }

  @Test
  public void getListSRReasonRejectDTO_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(datatable.getTotal()).thenReturn(10);
    when(srReasonRejectBusiness.getListSRReasonRejectDTO(any())).thenReturn(datatable);

    setFinalStatic(SRServiceManageBusinessImpl.class.getDeclaredField("log"), logger);
    SRReasonRejectDTO srConfigDTO = new SRReasonRejectDTO();
    srConfigDTO.setPage(1);
    srConfigDTO.setPageSize(10);
    Datatable result = srReasonRejectBusiness.getListSRReasonRejectDTO(srConfigDTO);
    assertEquals(datatable, result);
  }

  @Test
  public void insertOrUpdateSRReasonRejectDTO_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    setFinalStatic(SRServiceManageBusinessImpl.class.getDeclaredField("log"), logger);
    SRReasonRejectDTO srConfigDTO = Mockito.spy(SRReasonRejectDTO.class);
    srConfigDTO.setCreatedTime(new Date());
    srConfigDTO.setCreatedUser(userToken.getUserName());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(srReasonRejectRepository.insertOrUpdateSRReason(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = srReasonRejectBusiness.insertOrUpdateSRReason(srConfigDTO);
    Assert.assertEquals(result, resultInSideDto);
  }

  @Test
  public void deleteSRReasonReject_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(srReasonRejectRepository.deleteSRReasonReject(anyLong())).thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = srReasonRejectBusiness.deleteSRReasonReject(1L);
    Assert.assertEquals(resultInSideDto, result);
  }

  @Test
  public void getSRReasonRejectById_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    SRReasonRejectDTO reasonRejectDTO = PowerMockito.mock(SRReasonRejectDTO.class);
    when(srReasonRejectBusiness.getSRReasonRejectById(any())).thenReturn(reasonRejectDTO);
    setFinalStatic(SRServiceManageBusinessImpl.class.getDeclaredField("log"), logger);
    SRReasonRejectDTO result = srReasonRejectBusiness.getSRReasonRejectById(1L);
    assertEquals(reasonRejectDTO, result);
  }

}
