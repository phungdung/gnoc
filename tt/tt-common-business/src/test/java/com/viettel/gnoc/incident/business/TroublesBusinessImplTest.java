package com.viettel.gnoc.incident.business;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.bccs.cc.service.CauseDTO;
import com.viettel.bccs.cc.service.CauseErrorExpireDTO;
import com.viettel.bccs.cc.service.SpmRespone;
import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.CfgCreateWoBusiness;
import com.viettel.gnoc.commons.business.CfgInfoTtSpmBusiness;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.business.CompCauseBusiness;
import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.SpringApplicationContext;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.CatCfgClosedTicketDTO;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.CfgCreateWoDTO;
import com.viettel.gnoc.commons.dto.CfgInfoTtSpmDTO;
import com.viettel.gnoc.commons.dto.CfgRequireHaveWoDTO;
import com.viettel.gnoc.commons.dto.CfgTimeExtendTtDTO;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmDTO;
import com.viettel.gnoc.commons.dto.CompCauseDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TroubleStatisticForm;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.incident.provider.WSChatPort;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.TtCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatCfgClosedTicketRepository;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CfgRequireHaveWoRepository;
import com.viettel.gnoc.commons.repository.CfgTimeExtendTtRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.SmsGatewayRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.INSERT_SOURCE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.TROUBLE;
import com.viettel.gnoc.commons.utils.Constants.TT_STATE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.ws.Passwords;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrSearchDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.CfgServerNocEntity;
import com.viettel.gnoc.incident.model.TroublesEntity;
import com.viettel.gnoc.incident.repository.CfgServerNocRepository;
import com.viettel.gnoc.incident.repository.ItAccountRepository;
import com.viettel.gnoc.incident.repository.ItSpmInfoRepository;
import com.viettel.gnoc.incident.repository.TroubleActionLogsRepository;
import com.viettel.gnoc.incident.repository.TroubleAssignRepository;
import com.viettel.gnoc.incident.repository.TroubleNodeRepository;
import com.viettel.gnoc.incident.repository.TroubleWorklogRepository;
import com.viettel.gnoc.incident.repository.TroublesRepository;
import com.viettel.gnoc.incident.utils.TroubleBccsUtils;
import com.viettel.gnoc.incident.utils.TroubleSpmUtils;
import com.viettel.gnoc.incident.utils.TroubleTktuUtils;
import com.viettel.gnoc.incident.utils.WSBCCS2Port;
import com.viettel.gnoc.incident.utils.WSIPCC;
import com.viettel.gnoc.incident.utils.WSSAPPort;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.ipcc.services.NomalOutput;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;
import vn.viettel.smartoffice.GroupResponse;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TroublesBusinessImpl.class, SpringApplicationContext.class,
    FileUtils.class, CommonExport.class, I18n.class, DataUtil.class, TroubleBccsUtils.class,
    TroubleBccsUtils.class, Passwords.class, DateTimeUtils.class, TimezoneContextHolder.class,
    TicketProvider.class, PassTranformer.class, WSIPCC.class, DateUtil.class,
    org.apache.commons.io.FileUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class TroublesBusinessImplTest {

  @InjectMocks
  TroublesBusinessImpl troublesBusiness;

  @Mock
  TroublesRepository troublesRepository;

  @Mock
  CatItemBusiness catItemBusiness;

  @Mock
  UnitRepository unitRepository;

  @Mock
  UnitBusiness unitBusiness;

  @Mock
  UserBusiness userBusiness;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  CfgInfoTtSpmBusiness cfgInfoTtSpmBusiness;

  @Mock
  CfgCreateWoBusiness cfgCreateWoBusiness;

  @Mock
  TroubleMopBusiness troubleMopBussiness;

  @Mock
  TroubleWorklogRepository troubleWorklogRepository;

  @Mock
  TroubleNodeRepository troubleNodeRepository;

  @Mock
  TroubleActionLogsRepository troubleActionLogsRepository;

  @Mock
  ItAccountRepository itAccountRepository;

  @Mock
  ItSpmInfoRepository itSpmInfoRepository;

  @Mock
  MessagesRepository messagesRepository;

  @Mock
  TroubleAssignRepository troubleAssignRepository;

  @Mock
  CfgTimeExtendTtRepository cfgTimeExtendTtRepository;

  @Mock
  TtCategoryServiceProxy ttCategoryServiceProxy;

  @Mock
  SmsGatewayRepository smsGatewayRepository;

  @Mock
  CompCauseBusiness compCauseBusiness;

  @Mock
  CfgServerNocRepository cfgServerNocRepository;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  CatReasonBusiness catReasonBusiness;

  @Mock
  MessagesBusiness messagesBusiness;

  @Mock
  CfgRequireHaveWoRepository cfgRequireHaveWoRepository;

  @Mock
  CommonBusiness commonBusiness;

  @Mock
  TroublesTempClosedBusiness troublesTempClosedBusiness;

  @Mock
  TroublesServiceForCCBusiness troublesServiceForCCBusiness;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  WSChatPort wsChatPort;

  @Mock
  WSBCCS2Port wsbccs2Port;

  @Mock
  TroubleBccsUtils troubleBccsUtils;

  @Mock
  TroubleSpmUtils troubleSpmUtils;

  @Mock
  TroubleTktuUtils troubleTktuUtils;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  WOCreateBusiness woCreateBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  CatCfgClosedTicketRepository catCfgClosedTicketRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  WSSAPPort wssapPort;

  @Mock
  UserRepository userRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(troublesBusiness, "uploadFolder",
        "/uploadFolder");
    ReflectionTestUtils.setField(troublesBusiness, "ftpFolder",
        "/ftpFolder");
    ReflectionTestUtils.setField(troublesBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(troublesBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(troublesBusiness, "ftpServer",
        "10.240.232.25");
    ReflectionTestUtils.setField(troublesBusiness, "ftpPort",
        21);
  }

  @Test
  public void getTroubleInfo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    List<TroublesInSideDTO> result = Mockito.spy(ArrayList.class);
    result.add(dto);
    PowerMockito.when(troublesRepository.getTroubleInfo(any())).thenReturn(result);
    List<TroublesInSideDTO> actual = troublesBusiness.getTroubleInfo(dto);
    Assert.assertNotNull(actual);
  }

  @Test
  public void updateTroublesNOC_01() {
    PowerMockito.mockStatic(Passwords.class);
    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    List<TroublesDTO> listTrouble = Mockito.spy(ArrayList.class);
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("11111");
    troublesDTO.setEndTroubleTime("20/08/2020");
    troublesDTO.setReasonId("22222");
    troublesDTO.setRootCause("312312");
    troublesDTO.setSolutionType("33333");
    troublesDTO.setWorkArround("45454545");
    listTrouble.add(troublesDTO);
    Map<String, CatReasonInSideDTO> mapReason = Mockito.spy(HashMap.class);
    Map<String, String> mapProperty = Mockito.spy(HashMap.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(catReasonBusiness.getCatReasonData()).thenReturn(mapReason);
    PowerMockito.when(commonBusiness.getConfigProperty()).thenReturn(mapProperty);
    PowerMockito.when(troublesTempClosedBusiness.insertList(any())).thenReturn(resultInSideDto);

    ResultDTO actual = troublesBusiness.updateTroublesNOC(requestDTO, listTrouble);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void updateTroublesNOC_02() {
    PowerMockito.mockStatic(Passwords.class);

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    List<TroublesDTO> listTrouble = Mockito.spy(ArrayList.class);
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("11111");
    troublesDTO.setEndTroubleTime("20/08/2020");
    troublesDTO.setReasonId("22222");
    troublesDTO.setRootCause("312312");
    troublesDTO.setSolutionType("33333");
    troublesDTO.setWorkArround("45454545");
    listTrouble.add(troublesDTO);
    Map<String, CatReasonInSideDTO> mapReason = Mockito.spy(HashMap.class);
    Map<String, String> mapProperty = Mockito.spy(HashMap.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(catReasonBusiness.getCatReasonData()).thenReturn(mapReason);
    PowerMockito.when(commonBusiness.getConfigProperty()).thenReturn(mapProperty);
    PowerMockito.when(troublesTempClosedBusiness.insertList(any())).thenReturn(resultInSideDto);

    ResultDTO actual = troublesBusiness.updateTroublesNOC(requestDTO, listTrouble);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onSynchTrouble_01() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(DateTimeUtils.class);

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    String fromDate = "08/08/2020";
    String toDate = "20/08/2020";

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(DateTimeUtils.validateInput(anyString(), anyString()))
        .thenReturn(RESULT.FAIL);

    ResultDTO actual = troublesBusiness
        .onSynchTrouble(requestDTO, fromDate, toDate, "111", "222", "333");

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onSynchTrouble_02() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(DateTimeUtils.class);

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    String fromDate = "08/08/2020";
    String toDate = "20/08/2020";

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(DateTimeUtils.validateInput(anyString(), anyString()))
        .thenReturn(RESULT.SUCCESS);

    ResultDTO actual = troublesBusiness
        .onSynchTrouble(requestDTO, fromDate, toDate, "111", "222", "333");

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onSearchForSPM() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesDTO dto = Mockito.spy(TroublesDTO.class);
    List<TroublesDTO> result = Mockito.spy(ArrayList.class);
    result.add(dto);
    PowerMockito.when(
        troublesRepository.onSearchForSPM(any(), anyString(), anyString(), anyLong())
    ).thenReturn(result);
    List<TroublesDTO> actual = troublesBusiness
        .onSearchForSPM(dto, "thanhlv12", "SPM", 1L);
    Assert.assertNotNull(actual);
  }

  @Test
  public void onUpdateTroubleSPM_01() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_02() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_03() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("2222");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_04() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("2222");
    troublesDTO.setProcessingUserName("3333");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_05() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("2222");
    troublesDTO.setProcessingUserName("3333");
    troublesDTO.setStateName("UPDATE_DEFERRED");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_06() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("2222");
    troublesDTO.setProcessingUserName("3333");
    troublesDTO.setStateName("UPDATE_CLOSED");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_07() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("2222");
    troublesDTO.setProcessingUserName("3333");
    troublesDTO.setStateName("UPDATE_DEFERRED");
    troublesDTO.setWorkLog("4444");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_08() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("2222");
    troublesDTO.setProcessingUserName("3333");
    troublesDTO.setStateName("UPDATE_DEFERRED");
    troublesDTO.setWorkLog("4444");
    troublesDTO.setDeferredTime("19/08/2020 12:00:00");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_09() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("2222");
    troublesDTO.setProcessingUserName("3333");
    troublesDTO.setStateName("UPDATE_DEFERRED");
    troublesDTO.setWorkLog("4444");
    troublesDTO.setDeferredTime("19/08/2020 12:00:00");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_10() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("2222");
    troublesDTO.setProcessingUserName("3333");
    troublesDTO.setStateName("UPDATE_DEFERRED");
    troublesDTO.setWorkLog("4444");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_11() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("2222");
    troublesDTO.setProcessingUserName("3333");
    troublesDTO.setStateName("UPDATE_DEFERRED");
    troublesDTO.setWorkLog("4444");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("5555");
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_12() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_HOT");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_13() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_HOT");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesDTO.setAlarmGroupCode("1111");
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setTypeId(2222L);
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_14() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_HOT");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesDTO.setAlarmGroupCode("1111");
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setTypeId(2222L);
    troublesInSideDTO.setRemainTime("0.05");
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    trouble.setTypeId(3333L);
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);
    CfgTimeTroubleProcessDTO config = Mockito.spy(CfgTimeTroubleProcessDTO.class);
    config.setProcessTtTime(0.02);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(
        troublesRepository
            .getConfigTimeTroubleProcess(anyLong(), anyLong(), anyLong(), any())
    ).thenReturn(config);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_15() throws Exception {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_HOT");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM);
    troublesDTO.setAlarmGroupCode("1111");
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM);
    troublesInSideDTO.setTypeId(2222L);
    troublesInSideDTO.setRemainTime("0.05");
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    trouble.setTypeId(3333L);
    trouble.setState(3333L);
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    catItemDTO.setItemValue("11");
    lstCatItem.add(catItemDTO);
    CfgTimeTroubleProcessDTO config = Mockito.spy(CfgTimeTroubleProcessDTO.class);
    config.setProcessTtTime(0.02);
    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.SUCCESS);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(
        troublesRepository
            .getConfigTimeTroubleProcess(anyLong(), anyLong(), anyLong(), any())
    ).thenReturn(config);
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_16() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_WORKLOG");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_17() throws Exception {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_WORKLOG");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);
    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.FAIL);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_18() throws Exception {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_WORKLOG");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);
    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.SUCCESS);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_19() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_NUM_COMPLAINT");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_20() throws Exception {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_NUM_COMPLAINT");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);
    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.FAIL);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_21() throws Exception {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_NUM_COMPLAINT");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);
    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.SUCCESS);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_22() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_OPEN_DEFERRED");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_23() throws Exception {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_OPEN_DEFERRED");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);
    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.FAIL);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(woServiceProxy.updatePendingWo(any())).thenReturn(rdto);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_24() throws Exception {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_OPEN_DEFERRED");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    catItemDTO.setItemName("11");
    lstCatItem.add(catItemDTO);
    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.SUCCESS);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(woServiceProxy.updatePendingWo(any())).thenReturn(rdto);
    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_25() throws Exception {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_DEFERRED");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    catItemDTO.setItemName("11");
    lstCatItem.add(catItemDTO);
    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.SUCCESS);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(woServiceProxy.pendingWo(any())).thenReturn(rdto);
    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_26() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_CLOSED");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    troublesInSideDTO.setTroubleCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.FAIL);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(woServiceProxy.closeWoForSPM(any())).thenReturn(resultDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleSPM_27() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("1111");
    troublesDTO.setProcessingUnitName("1111");
    troublesDTO.setProcessingUserName("1111");
    troublesDTO.setStateName("UPDATE_CLOSED");
    troublesDTO.setWorkLog("1111");
    troublesDTO.setReasonLv3Id("1111");
    troublesDTO.setDeferredTime("19/08/9000 12:00:00");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2222L);
    troublesInSideDTO.setCreateUnitId(2222L);
    troublesInSideDTO.setCreateUserId(2222L);
    troublesInSideDTO.setState(2222L);
    troublesInSideDTO.setTroubleCode("2222");
    troublesInSideDTO.setStateName("2222");
    troublesInSideDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    troublesInSideDTO.setWoCode("2222");
    troublesInSideDTO.setTroubleCode("2222");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setInsertSource("SPM");
    trouble.setTroubleCode("3333");
    trouble.setInfoTicket("3333");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemId(11L);
    catItemDTO.setItemCode("11");
    lstCatItem.add(catItemDTO);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(woServiceProxy.closeWoForSPM(any())).thenReturn(resultDTO);
    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleSPM(requestDTO, troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onRollBackTroubleSPM_01() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    List<String> lstSpmCode = Mockito.spy(ArrayList.class);
    lstSpmCode.add("1111");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    List<ResultDTO> actual = troublesBusiness.onRollBackTroubleSPM(requestDTO, lstSpmCode);

    Assert.assertEquals(RESULT.SUCCESS, actual.get(0).getKey());
  }

  @Test
  public void onRollBackTroubleSPM_02() {
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("FAIL");

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    List<String> lstSpmCode = Mockito.spy(ArrayList.class);
    lstSpmCode.add("1111");
    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1111L);
    troubleDTO.setWoCode("1111");
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), anyString(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        woServiceProxy.deleteWOForRollbackProxy(anyString(), anyString(), anyString())
    ).thenReturn(resultDTO);

    List<ResultDTO> actual = troublesBusiness.onRollBackTroubleSPM(requestDTO, lstSpmCode);

    Assert.assertEquals(RESULT.SUCCESS, actual.get(0).getKey());
  }

  @Test
  public void onSearchForVsmart() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesDTO dto = Mockito.spy(TroublesDTO.class);
    List<TroublesDTO> result = Mockito.spy(ArrayList.class);

    PowerMockito.when(troublesRepository.onSearchForVsmart(any(), anyInt(), anyInt()))
        .thenReturn(result);

    List<TroublesDTO> actual = troublesBusiness.onSearchForVsmart(dto, 1, 5);

    Assert.assertNotNull(actual);
  }

  @Test
  public void onSearchCountForVsmart() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);

    PowerMockito.when(troublesRepository.onSearchCountForVsmart(any())).thenReturn(1);

    int actual = troublesBusiness.onSearchCountForVsmart(troublesDTO);

    Assert.assertEquals(1, actual);
  }

  @Test
  public void onInsertTroubleMobile_01() {
    PowerMockito.mockStatic(Passwords.class);

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    List<String> listAccount = Mockito.spy(ArrayList.class);
    listAccount.add("thanhlv12");
    String[] arrFileName = {"11"};
    byte[][] arFileData = {{11, 11}};

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness
        .onInsertTroubleMobile(requestDTO, troublesDTO, listAccount, arrFileName, arFileData);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onInsertTroubleMobile_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(Passwords.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    List<String> listAccount = Mockito.spy(ArrayList.class);
    listAccount.add("thanhlv12");
    String[] arrFileName = {"11"};
    byte[][] arFileData = {{11, 11}};
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setInsertSource("SPM_NOC");
    troublesDTO.setListAccount(listAccount);
    troublesDTO.setTblCurr("11");
    troublesDTO.setTblHis("11");
    troublesDTO.setTblClear("11");
    troublesDTO.setAlarmId("11");
    troublesDTO.setCatchingTime("20/08/2020");
    troublesDTO.setWoType("11");
    troublesDTO.setTypeId("11");
    troublesDTO.setAlarmGroupCode("AA");
    troublesDTO.setSubCategoryId("11");
    troublesDTO.setAutoCreateWO(2L);
    troublesDTO.setCdId("11");
    troublesDTO.setTroubleName("11");
    troublesDTO.setPriorityId("11###11###11");
    troublesDTO.setRisk("11");
    troublesDTO.setBeginTroubleTime("20/08/2020 12:00:00");
    troublesDTO.setEndTroubleTime("21/08/2020 12:00:00");
    troublesDTO.setAffectedService("11###AA###11");
    troublesDTO.setDescription("11");
    troublesDTO.setReceiveUnitId("11");
    troublesDTO.setCreateUserName("11");
    troublesDTO.setCreateUnitId("11");
    troublesDTO.setLocationId("1");
    troublesDTO.setTelServiceId("1");
    troublesDTO.setIsCcResult("1");
    troublesDTO.setServiceType(1L);
    troublesDTO.setSpmCode("1");
    troublesDTO.setSpmId("1");
    troublesDTO.setComplaintGroupId("1");
    troublesDTO.setTimeProcess("1");
    List<TroublesInSideDTO> lstTrouble = Mockito.spy(ArrayList.class);
    List<CatItemDTO> lstCfg = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("11###AA###11");
    catItemDTO.setItemId(11L);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemName("11");
    catItemDTO.setParentItemId(11L);
    lstCfg.add(catItemDTO);
    List<CatItemDTO> lstCfg1 = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO1 = Mockito.spy(CatItemDTO.class);
    catItemDTO1.setItemCode("11###AA###11");
    catItemDTO1.setItemId(11L);
    catItemDTO1.setCategoryCode(TROUBLE.PT_TYPE);
    catItemDTO1.setItemName("11");
    catItemDTO.setParentItemId(11L);
    lstCfg1.add(catItemDTO1);
    List<UnitDTO> lstUnitDTO = Mockito.spy(ArrayList.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(11L);
    unitDTO.setUnitName("11");
    unitDTO.setUnitCode("11");
    lstUnitDTO.add(unitDTO);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUnitId(11L);
    usersInsideDto.setUserId(11L);
    usersInsideDto.setUnitName("11");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .searchByConditionBean(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstTrouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCfg)
        .thenReturn(lstCfg).thenReturn(lstCfg).thenReturn(lstCfg).thenReturn(lstCfg)
        .thenReturn(lstCfg).thenReturn(lstCfg1).thenReturn(lstCfg).thenReturn(lstCfg)
        .thenReturn(lstCfg);
    PowerMockito.when(
        unitRepository
            .getListUnitDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstUnitDTO);
    PowerMockito.when(userBusiness.getUserDTOByUserName(anyString())).thenReturn(usersInsideDto);
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitDTO);
    CatLocationDTO location = Mockito.spy(CatLocationDTO.class);
    PowerMockito.when(catLocationBusiness.getLocationByCode(any(), any(), any())).thenReturn(location);
    CfgUnitTtSpmDTO ttSpmDTO = Mockito.spy(CfgUnitTtSpmDTO.class);
    ttSpmDTO.setUnitId("1");
    ttSpmDTO.setUnitName("1");
    PowerMockito.when(troublesRepository.getUnitByLocation(any(), any(), any())).thenReturn(ttSpmDTO);
    List<CfgInfoTtSpmDTO> infoTtSpmDTO = Mockito.spy(ArrayList.class);
    CfgInfoTtSpmDTO cfgInfoTtSpmDTO = Mockito.spy(CfgInfoTtSpmDTO.class);
    cfgInfoTtSpmDTO.setTroubleName("1");
    cfgInfoTtSpmDTO.setAlarmGroupId("1");
    cfgInfoTtSpmDTO.setSubCategoryId("1");
    infoTtSpmDTO.add(cfgInfoTtSpmDTO);
    PowerMockito.when(cfgInfoTtSpmBusiness.getListCfgInfoTtSpmDTO(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(infoTtSpmDTO);
    List<String> lstString = Mockito.spy(ArrayList.class);
    lstString.add("1");
    PowerMockito.when(troublesRepository.getSequenseTroubles(anyString(), any()))
        .thenReturn(lstString);
    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setKey(RESULT.SUCCESS);
    resultWO.setMessage(RESULT.SUCCESS);
    PowerMockito.when(woCreateBusiness.createWOMobile(any(), any(), any())).thenReturn(resultWO);
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setId(1L);
    result.setKey(RESULT.SUCCESS);
    PowerMockito.when(troublesRepository.insertTroubles(any())).thenReturn(result);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    PowerMockito.when(userBusiness.getUserByUserId(any())).thenReturn(usersEntity);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);

    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.when(PassTranformer.decrypt(any())).thenReturn("1");
    PowerMockito.when(FileUtils
        .saveFtpFile(anyString(), anyInt(), any(),
            any(), anyString(), anyString(), any(), any())).thenReturn("1");

    PowerMockito.when(troublesRepository.insertCommonFile(any())).thenReturn(result);

    ResultDTO actual = troublesBusiness
        .onInsertTroubleMobile(requestDTO, troublesDTO, listAccount, arrFileName, arFileData);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onInsertTroubleMobileNew() {
  }

  @Test
  public void insertTroubleMobile() {
  }

  @Test
  public void onInsertTroubleDTOMobile() {
  }

  @Test
  public void updateTroubleSpmVTNET() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setReceiveUnitId("1");
    troublesDTO.setReceiveUserId("1");
    troublesDTO.setTroubleId("1");
    troublesDTO.setReceiveUnitName("thanhlv12");
    troublesDTO.setReceiveUserName("thanhlv12");
    troublesDTO.setState("1");
    troublesDTO.setTroubleCode("1");
    troublesDTO.setStateName("1");
    troublesDTO.setRootCause("1");
    troublesDTO.setWorkArround("1");

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);

    PowerMockito.when(troublesRepository.updateTroubleSpmVTNET(any())).thenReturn(result);

    ResultInSideDto actual = troublesBusiness.updateTroubleSpmVTNET(troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void checkAccountSPM() {
  }

  @Test
  public void onInsertTroubleFileWS_01() {
    PowerMockito.mockStatic(Passwords.class);

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    List<String> listAccount = Mockito.spy(ArrayList.class);
    listAccount.add("thanhlv12");
    String[] arrFileName = {"11"};
    byte[][] arFileData = {{11, 11}};

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);

    ResultDTO actual = troublesBusiness
        .onInsertTroubleFileWS(requestDTO, troublesDTO, listAccount, arrFileName, arFileData);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onInsertTroubleFileWS_02() {
    PowerMockito.mockStatic(Passwords.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    List<String> listAccount = Mockito.spy(ArrayList.class);
    listAccount.add("thanhlv12");
    String[] arrFileName = {"11"};
    byte[][] arFileData = {{11, 11}};
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setInsertSource("SPM_NOC");
    troublesDTO.setListAccount(listAccount);
    troublesDTO.setTblCurr("11");
    troublesDTO.setTblHis("11");
    troublesDTO.setTblClear("11");
    troublesDTO.setAlarmId("11");
    troublesDTO.setCatchingTime("20/08/2020");
    troublesDTO.setWoType("11");
    troublesDTO.setTypeId("11");
    troublesDTO.setAlarmGroupCode("AA");
    troublesDTO.setSubCategoryId("11");
    troublesDTO.setAutoCreateWO(2L);
    troublesDTO.setCdId("11");
    troublesDTO.setTroubleName("11");
    troublesDTO.setPriorityId("11###11###11");
    troublesDTO.setRisk("11");
    troublesDTO.setBeginTroubleTime("20/08/2020 12:00:00");
    troublesDTO.setEndTroubleTime("21/08/2020 12:00:00");
    troublesDTO.setAffectedService("11###AA###11");
    troublesDTO.setDescription("11");
    troublesDTO.setReceiveUnitId("11");
    troublesDTO.setCreateUserName("11");
    troublesDTO.setCreateUnitId("11");
    List<TroublesInSideDTO> lstTrouble = Mockito.spy(ArrayList.class);
    List<CatItemDTO> lstCfg = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("11###AA###11");
    catItemDTO.setItemId(11L);
    catItemDTO.setCategoryCode(TROUBLE.PRIORITY);
    catItemDTO.setItemName("11");
    lstCfg.add(catItemDTO);
    List<UnitDTO> lstUnitDTO = Mockito.spy(ArrayList.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(11L);
    unitDTO.setUnitName("11");
    unitDTO.setUnitCode("11");
    lstUnitDTO.add(unitDTO);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUnitId(11L);
    usersInsideDto.setUserId(11L);
    usersInsideDto.setUnitName("11");

    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(
        troublesRepository
            .searchByConditionBean(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstTrouble);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCfg);
    PowerMockito.when(
        unitRepository
            .getListUnitDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstUnitDTO);
    PowerMockito.when(userBusiness.getUserDTOByUserName(anyString())).thenReturn(usersInsideDto);
    PowerMockito.when(unitBusiness.findUnitById(anyLong())).thenReturn(unitDTO);

    ResultDTO actual = troublesBusiness
        .onInsertTroubleFileWS(requestDTO, troublesDTO, listAccount, arrFileName, arFileData);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onInsertTroubleFileWSNew() {
  }

  @Test
  public void onInsertTroubleFileWS1() {
  }

  @Test
  public void onSearch() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<TroublesInSideDTO> troublesDTOS = Mockito.spy(ArrayList.class);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setState(1111L);
    troublesInSideDTO.setTypeId(2046L);
    troublesInSideDTO.setSubCategoryId(1111L);
    troublesInSideDTO.setPriorityId(1111L);
    troublesInSideDTO.setImpactId(1111L);
    troublesInSideDTO.setSolutionType(1111L);
    troublesInSideDTO.setRisk(1111L);
    troublesInSideDTO.setVendorId(1111L);
    troublesInSideDTO.setRejectedCode(1111L);
    troublesInSideDTO.setWarnLevel(1111L);
    troublesInSideDTO.setCableType(1111L);
    troublesInSideDTO.setAutoClose(1111L);
    troublesInSideDTO.setCreatedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesInSideDTO.setCatchingTime(DateTimeUtils.convertStringToDate("22/08/2020"));
    troublesInSideDTO.setTimeCreateCfg(0.01);
    troublesInSideDTO.setBeginTroubleTime(DateTimeUtils.convertStringToDate("21/08/2020"));
    troublesInSideDTO.setEndTroubleTime(DateTimeUtils.convertStringToDate("23/08/2020"));
    troublesInSideDTO.setClosedTime(DateTimeUtils.convertStringToDate("23/08/2020"));
    troublesInSideDTO.setRemainTime("0.05");
    troublesInSideDTO.setIsStationVip(1L);
    troublesInSideDTO.setTransNetworkTypeId(1111L);
    troublesInSideDTO.setTransReasonEffectiveId(1111L);
    troublesInSideDTO.setCreatedTimeFrom("19/08/2020");
    troublesInSideDTO.setCreatedTimeTo("25/08/2020");
    troublesInSideDTO.setTransNetworkTypeId(1111L);
    troublesDTOS.add(troublesInSideDTO);
    datatable.setData(troublesDTOS);
    Map<String, String> mapConfigProperty = Mockito.spy(HashMap.class);
    mapConfigProperty.put(CONFIG_PROPERTY.TT_TYPE_TRANS, "1111,2222");
    List<TroublesInSideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(troublesInSideDTO);
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1111L);
    catItemDTO.setItemCode("2046");
    catItemDTO.setItemName("1111");
    lstCatItem.add(catItemDTO);

    PowerMockito.when(troublesRepository.onSearch(any())).thenReturn(datatable);
    PowerMockito.when(troublesRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(troublesRepository.getTroublesSearchExport(any())).thenReturn(lst);
    PowerMockito.when(
        catItemBusiness
            .getListCatItemDTOByListCategoryLE(anyString(), anyString(), anyString(), any())
    ).thenReturn(lstCatItem);
    PowerMockito.when(
        catItemBusiness
            .getListItemByCategoryAndParent(anyString(), any())
    ).thenReturn(lstCatItem);
    PowerMockito.when(
        catReasonBusiness.getListCatReason(anyLong(), anyString())
    ).thenReturn(lstCatItem);

    Datatable actual = troublesBusiness.onSearch(dto);

    Assert.assertNotNull(actual);
  }

  @Test
  public void findTroublesById() throws Exception {
    PowerMockito.mockStatic(TimezoneContextHolder.class);

    TroublesEntity troublesDTO = Mockito.spy(TroublesEntity.class);
    troublesDTO.setState(1L);
    troublesDTO.setTimeProcess(0.05);
    troublesDTO.setTimeUsed(0.04);
    troublesDTO.setAssignTimeTemp(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setTroubleId(1L);
    troublesDTO.setCreatedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setLastUpdateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setAssignTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setQueueTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setClearTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setClosedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setBeginTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setEndTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setDeferredTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setDateMove(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setCatchingTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setEstimateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("WAIT FOR DEFERRED");

    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(troublesDTO);
    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(catItemDTO);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(2.00);

    TroublesInSideDTO actual = troublesBusiness.findTroublesById(1L);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getSequenseTroubles() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<String> result = Mockito.spy(ArrayList.class);
    result.add("1111");

    PowerMockito.when(troublesRepository.getSequenseTroubles(anyString(), anyInt(), anyInt()))
        .thenReturn(result);

    List<String> actual = troublesBusiness.getSequenseTroubles("111", 1, 2);

    Assert.assertNotNull(actual);
  }


  @Test
  public void onSearchTroubleRelated() {
    PowerMockito.mockStatic(TimezoneContextHolder.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setAssignTimeTemp(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setTroubleId(1L);
    troublesDTO.setCreatedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setLastUpdateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setAssignTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setQueueTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setClearTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setClosedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setBeginTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setEndTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setDeferredTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setDateMove(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setCatchingTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setEstimateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    Datatable datatable = Mockito.spy(Datatable.class);
    List<TroublesInSideDTO> list = Mockito.spy(ArrayList.class);
    list.add(troublesDTO);
    datatable.setData(list);

    PowerMockito.when(troublesRepository.onSearchTroubleRelated(any())).thenReturn(datatable);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(2.00);

    Datatable actual = troublesBusiness.onSearchTroubleRelated(troublesDTO);
  }

  @Test
  public void insertTroublesTT() throws Exception {
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPassword("123456a@");
    Map<String, String> map = Mockito.spy(HashMap.class);
    List<InfraDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    infraDeviceDTO.setDeviceId("111");
    infraDeviceDTO.setDeviceCode("111");
    infraDeviceDTO.setDeviceName("111");
    infraDeviceDTO.setIp("111");
    lstDevice.add(infraDeviceDTO);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setAssignTimeTemp(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setTroubleId(1L);
    troublesDTO.setCreatedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setLastUpdateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setAssignTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setQueueTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setClearTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setClosedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setBeginTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setEndTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setDeferredTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setDateMove(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setCatchingTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setEstimateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setWorkLog("1111");
    troublesDTO.setState(1L);
    troublesDTO.setMap(map);
    troublesDTO.setInsertSource("TT");
    troublesDTO.setTypeId(1L);
    troublesDTO.setAlarmGroupId("111");
    troublesDTO.setAutoCreateWO(1L);
    troublesDTO.setAlarmGroupCode("1111");
    troublesDTO.setTypeName("1111");
    troublesDTO.setCountry("VN");
    troublesDTO.setPriorityId(1L);
    troublesDTO.setIsStationVip(1L);
    troublesDTO.setLocationId(1L);
    troublesDTO.setStateName(TT_STATE.QUEUE);
    troublesDTO.setTroubleId(1L);
    troublesDTO.setCreateUnitId(1L);
    troublesDTO.setCreateUnitName("111");
    troublesDTO.setCreateUserId(1L);
    troublesDTO.setCreateUserName("111");
    troublesDTO.setLstNode(lstDevice);
    List<String> listAccount = Mockito.spy(ArrayList.class);
    listAccount.add("thanhlv12");
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    MockMultipartFile testFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes());
    files.add(testFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitName("UNIT");
    unitDTO.setUnitCode("UNIT");
    List<UnitDTO> lstUnit = Mockito.spy(ArrayList.class);
    lstUnit.add(unitDTO);
    CatItemDTO stateDTO = Mockito.spy(CatItemDTO.class);
    stateDTO.setItemCode("QUEUE");
    stateDTO.setItemId(1L);
    stateDTO.setItemValue("11");
    List<CatItemDTO> lstAlarmGroup = Mockito.spy(ArrayList.class);
    lstAlarmGroup.add(stateDTO);
    List<CfgCreateWoDTO> lstCfg = Mockito.spy(ArrayList.class);
    CfgCreateWoDTO cfgCreateWoDTO = Mockito.spy(CfgCreateWoDTO.class);
    lstCfg.add(cfgCreateWoDTO);
    List<String> stringList = Mockito.spy(ArrayList.class);
    stringList.add("1111");
    CfgTimeTroubleProcessDTO config = Mockito.spy(CfgTimeTroubleProcessDTO.class);
    config.setProcessTtTime(0.05);
    config.setTimeWoVip(0.05);
    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setKey(RESULT.SUCCESS);
    resultWO.setId("1");
    resultWO.setRequestTime("111");
    String locationNameFull = "VIETNAM";
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    usersEntity.setUsername("thanhlv12");

    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(2.00);
    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(
        unitRepository
            .getListUnitDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstUnit);
    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(stateDTO);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstAlarmGroup);
    PowerMockito.when(cfgCreateWoBusiness
        .getListCfgCreateWoDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstCfg);
    PowerMockito.when(troublesRepository.getSequenseTroubles(anyString(), anyInt()))
        .thenReturn(stringList);
    PowerMockito.when(
        troublesRepository
            .getConfigTimeTroubleProcess(anyLong(), anyLong(), anyLong(), anyString())
    ).thenReturn(config);
    PowerMockito.when(woCreateBusiness.createWO(any(), any(), any())).thenReturn(resultWO);
    PowerMockito.when(troublesRepository.getLocationNameFull(any())).thenReturn(locationNameFull);
    PowerMockito.when(troublesRepository.insertTroubles(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userBusiness.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(
        FileUtils
            .saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(),
                any(), any())
    ).thenReturn("temp");
    PowerMockito.when(
        FileUtils.saveUploadFile(anyString(), any(), anyString(), any())
    ).thenReturn("temp");
    PowerMockito.when(troublesRepository.insertCommonFile(any())).thenReturn(resultInSideDto);

    ResultInSideDto actual = troublesBusiness
        .insertTroublesTT(requestDTO, files, troublesDTO, listAccount);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleTT() throws Exception {
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setAssignTimeTemp(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setTroubleId(1L);
    tForm.setCreatedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setLastUpdateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setAssignTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setQueueTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setClearTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setClosedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setBeginTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setEndTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setDeferredTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setDateMove(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setCatchingTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    tForm.setWorkLog("1111");
    tForm.setInsertSource("SPM");
    tForm.setTypeId(1L);
    tForm.setAlarmGroupId("111");
    tForm.setAutoCreateWO(1L);
    tForm.setAlarmGroupCode("1111");
    tForm.setTypeName("1111");
    tForm.setCountry("VN");
    tForm.setPriorityId(1L);
    tForm.setIsStationVip(0L);
    tForm.setLocationId(1L);
    tForm.setState(9L);
    tForm.setStateName(TT_STATE.CLEAR);
    tForm.setTroubleId(1L);
    tForm.setCreateUnitId(1L);
    tForm.setCreateUnitName("111");
    tForm.setCreateUserId(1L);
    tForm.setCreateUserName("111");
    tForm.setReceiveUserId(1L);
    tForm.setReceiveUserName("11");
    tForm.setIsMove(1L);
    tForm.setUnitMove(1L);
    tForm.setReceiveUnitId(1L);
    tForm.setReasonId(1L);
    tForm.setWoCode("1");
    tForm.setIsTickHelp(1L);

    List<MultipartFile> multipartFileList = Mockito.spy(ArrayList.class);
    MockMultipartFile testFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes());
    multipartFileList.add(testFile);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");

    CatItemDTO stateDTO = Mockito.spy(CatItemDTO.class);
    stateDTO.setItemCode("REJECT");
    stateDTO.setItemId(1L);
    stateDTO.setItemValue("11");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitName("UNIT");
    unitDTO.setUnitCode("UNIT");

    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setIsMove(1L);
    trouble.setInsertSource("AAAAA");
    trouble.setAlarmGroup("1");
    trouble.setTypeId(1L);
    trouble.setPriorityId(11L);
    trouble.setTimeProcess(0.05);

    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    usersEntity.setUsername("thanhlv12");

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);

    CfgTimeTroubleProcessDTO config = Mockito.spy(CfgTimeTroubleProcessDTO.class);
    config.setTimeStationVip(0.05);
    config.setProcessTtTime(0.05);
    config.setCreateTtTime(0.05);

    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(2.00);

    TroublesInSideDTO troubleOld = Mockito.spy(TroublesInSideDTO.class);
    troubleOld.setAssignTimeTemp(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setTroubleId(1L);
    troubleOld.setCreatedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setLastUpdateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setAssignTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setQueueTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setClearTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setClosedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setBeginTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setEndTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setDeferredTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setDateMove(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setCatchingTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setEstimateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleOld.setWorkLog("1111");
    troubleOld.setInsertSource("SPM");
    troubleOld.setTypeId(1L);
    troubleOld.setAlarmGroupId("111");
    troubleOld.setAutoCreateWO(1L);
    troubleOld.setAlarmGroupCode("1111");
    troubleOld.setTypeName("1111");
    troubleOld.setCountry("VN");
    troubleOld.setPriorityId(1L);
    troubleOld.setIsStationVip(0L);
    troubleOld.setLocationId(1L);
    troubleOld.setState(1L);
    troubleOld.setStateName(TT_STATE.QUEUE);
    troubleOld.setTroubleId(1L);
    troubleOld.setCreateUnitId(1L);
    troubleOld.setCreateUnitName("111");
    troubleOld.setCreateUserId(1L);
    troubleOld.setCreateUserName("111");
    troubleOld.setReceiveUserId(1L);
    troubleOld.setReceiveUserName("11");
    troubleOld.setIsMove(1L);
    troubleOld.setUnitMove(1L);
    troubleOld.setReceiveUnitId(1L);
    troubleOld.setReasonId(1L);
    troubleOld.setWoCode("1");
    troubleOld.setIsTickHelp(1L);
    PowerMockito.when(troublesRepository.getTroubleDTO(anyString(), any(), any(), any(), any(), any(), any())
    ).thenReturn(troubleOld);
    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(stateDTO);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);
    PowerMockito.when(
        troublesRepository
            .getConfigTimeTroubleProcess(anyLong(), anyLong(), anyLong(), anyString())
    ).thenReturn(config);
    PowerMockito.when(userBusiness.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(
        FileUtils
            .saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(),
                any(), any())
    ).thenReturn("temp");
    PowerMockito.when(
        FileUtils.saveUploadFile(anyString(), any(), anyString(), any())
    ).thenReturn("temp");
    PowerMockito.when(troublesRepository.insertCommonFile(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(0.05);

    ResultInSideDto actual = troublesBusiness
        .onUpdateTroubleTT(tForm, multipartFileList);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void updateTroubleToSPM() {
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);

    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setIsMove(1L);
    trouble.setInsertSource("AAAAA");
    trouble.setAlarmGroup("1");
    trouble.setTypeId(1L);
    trouble.setPriorityId(11L);
    trouble.setTimeProcess(0.05);

    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(trouble);

    ResultInSideDto actual = troublesBusiness.updateTroubleToSPM(troublesInSideDTO, "1");

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void sendChatListUsers_01() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("111");
    troublesInSideDTO.setIsChat(1L);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");

    GroupResponse groupResponse = Mockito.spy(GroupResponse.class);
    groupResponse.setResultCode(1);

    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(wsChatPort.createGroupInBusiness2(any())).thenReturn(groupResponse);

    ResultInSideDto actual = troublesBusiness.sendChatListUsers(troublesInSideDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void sendChatListUsers_02() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDtos.add(usersInsideDto);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("111");
    troublesInSideDTO.setIsChat(2L);
    troublesInSideDTO.setUsersInsideDtos(usersInsideDtos);
    troublesInSideDTO.setTroubleId(1L);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");

    GroupResponse groupResponse = Mockito.spy(GroupResponse.class);
    groupResponse.setResultCode(1);

    TroublesEntity troublesEntity = Mockito.spy(TroublesEntity.class);

    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(wsChatPort.createGroupInBusiness2(any())).thenReturn(groupResponse);
    PowerMockito.when(
        troublesRepository
            .findTroublesById(anyLong())
    ).thenReturn(troublesEntity);

    ResultInSideDto actual = troublesBusiness.sendChatListUsers(troublesInSideDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void callIPCC() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(WSIPCC.class);
    PowerMockito.mockStatic(PassTranformer.class);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setReceiveUnitId(1L);
    troublesInSideDTO.setCreatedTimeFrom("20/08/2020");
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setAlarmGroupId("1");
    troublesInSideDTO.setPriorityId(1L);
    troublesInSideDTO.setInsertSource("1");
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTroubleCode("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitName("UNIT");
    unitDTO.setUnitCode("UNIT");
    unitDTO.setIpccServiceId(1L);
    unitDTO.setMobile("0909999999");

    List<UnitDTO> units = Mockito.spy(ArrayList.class);
    units.add(unitDTO);
    List<IpccServiceDTO> lst = Mockito.spy(ArrayList.class);
    IpccServiceDTO ipccServiceDTO = Mockito.spy(IpccServiceDTO.class);
    ipccServiceDTO.setIpccServiceCode("IPPC_VN");
    ipccServiceDTO.setPassword("123qwe");
    lst.add(ipccServiceDTO);
    Map<String, String> mapConfigProperty = Mockito.spy(HashMap.class);
    CfgTimeTroubleProcessDTO cttpdto = Mockito.spy(CfgTimeTroubleProcessDTO.class);
    cttpdto.setCdAudioName("1");
    NomalOutput res = Mockito.spy(NomalOutput.class);
    res.setDescription("AAA");
    WSIPCC wsipcc = Mockito.spy(WSIPCC.class);

    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(
        unitRepository.getListUnitDTO(any(), anyInt(), anyInt(), any(), any())
    ).thenReturn(units);
    PowerMockito.when(smsGatewayRepository.getListIpccServiceDTO(any())).thenReturn(lst);
    PowerMockito.when(troublesRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(
        ttCategoryServiceProxy.getConfigTimeTroubleProcess(any())
    ).thenReturn(cttpdto);
    PowerMockito.when(WSIPCC.getWsIpcc()).thenReturn(wsipcc);
    PowerMockito.when(PassTranformer.decrypt(anyString()))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");

    ResultInSideDto actual = troublesBusiness.callIPCC(troublesInSideDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void sendSms() {
  }

  @Test
  public void validateDTO() {
  }

  @Test
  public void escapeXML() {
  }

  @Test
  public void reloadTrouble() {
  }

  @Test
  public void doCallIPCC() {
  }

  @Test
  public void deleteTrouble() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitName("UNIT");
    unitDTO.setUnitCode("UNIT");

    TroublesEntity troublesEntity = Mockito.spy(TroublesEntity.class);
    troublesEntity.setInsertSource("AAA");

    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(troublesEntity);

    ResultInSideDto actual = troublesBusiness.deleteTrouble(1L);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void viewCall() {
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setPageSize(5);
    troublesInSideDTO.setPage(5);
    List<LogCallIpccDTO> logCallIpccDTOS = Mockito.spy(ArrayList.class);
    LogCallIpccDTO logCallIpccDTO = Mockito.spy(LogCallIpccDTO.class);
    logCallIpccDTOS.add(logCallIpccDTO);
    PowerMockito.when(
        woServiceProxy.
            getListLogCallIpccByCondition(any())
    ).thenReturn(logCallIpccDTOS);
    Datatable actual = troublesBusiness.viewCall(troublesInSideDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListChatUsers() {
    PowerMockito.mockStatic(TicketProvider.class);
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setCreateUserId(1L);
    troublesInSideDTO.setReceiveUserId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");
    List<UsersInsideDto> lst = Mockito.spy(ArrayList.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userBusiness.getListUsersByListUserId(anyList())).thenReturn(lst);
    List<UsersInsideDto> actual = troublesBusiness.getListChatUsers(troublesInSideDTO);
  }

  @Test
  public void loadUserSupportGroup() {
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setPageSize(5);
    troublesInSideDTO.setPage(5);
    CatItemDTO stateDTO = Mockito.spy(CatItemDTO.class);
    stateDTO.setItemCode("QUEUE");
    stateDTO.setItemId(1L);
    stateDTO.setItemValue("11");
    stateDTO.setDescription("11");
    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(stateDTO);
    Datatable datatable = Mockito.spy(Datatable.class);

    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(stateDTO);
    PowerMockito.when(
        catItemBusiness.getListItemByCategory(anyString(), any())
    ).thenReturn(lst);
    PowerMockito.when(userBusiness.getListUsersByList(any())).thenReturn(datatable);

    Datatable actual = troublesBusiness.loadUserSupportGroup(troublesInSideDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void onSearchCountByState() {
  }

  @Test
  public void getConfigProperty() {
  }

  @Test
  public void getListReasonBCCS_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setComplaintGroupId("1");
    troublesInSideDTO.setInsertSource("SPM_VTNET");
    troublesInSideDTO.setIsTickHelp(1L);
    troublesInSideDTO.setServiceType(1L);
    troublesInSideDTO.setTechnology("1");
    List<CompCauseDTO> lstCauseDTO = Mockito.spy(ArrayList.class);
    CompCauseDTO compCauseDTO = Mockito.spy(CompCauseDTO.class);
    compCauseDTO.setCompCauseId("1");
    compCauseDTO.setName("1");
    compCauseDTO.setCode("1");
    lstCauseDTO.add(compCauseDTO);
    PowerMockito.when(
        compCauseBusiness
            .translateList(anyList(), anyString())
    ).thenReturn(lstCauseDTO);

    ResultInSideDto actual = troublesBusiness.getListReasonBCCS(troublesInSideDTO, 1L, 1);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListReasonBCCS_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setComplaintGroupId("1");
    troublesInSideDTO.setInsertSource("SPM_VTNET_BCCS");
    troublesInSideDTO.setIsTickHelp(1L);
    troublesInSideDTO.setServiceType(1L);
    troublesInSideDTO.setTechnology("1");
    List<CfgServerNocEntity> lstServer = Mockito.spy(ArrayList.class);
    CfgServerNocEntity cfgServerNocEntity = Mockito.spy(CfgServerNocEntity.class);
    lstServer.add(cfgServerNocEntity);
    List<CauseDTO> lstCauseDTO = Mockito.spy(ArrayList.class);
    CauseDTO causeDTO = Mockito.spy(CauseDTO.class);
    causeDTO.setCauseId(1L);
    lstCauseDTO.add(causeDTO);
    SpmRespone res = Mockito.spy(SpmRespone.class);
    PowerMockito.when(
        cfgServerNocRepository
            .getListCfgServerNocByCondition(anyString())
    ).thenReturn(lstServer);
    PowerMockito.when(wsbccs2Port.getListCause(any(), anyInt(), any())).thenReturn(res);
    PowerMockito.when(res.getListCauseDTO()).thenReturn(lstCauseDTO);

    ResultInSideDto actual = troublesBusiness.getListReasonBCCS(troublesInSideDTO, 1L, 1);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListReasonBCCS_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setComplaintGroupId("1");
    troublesInSideDTO.setInsertSource("SPM_VTNET_AAA");
    troublesInSideDTO.setIsTickHelp(1L);
    troublesInSideDTO.setServiceType(1L);
    troublesInSideDTO.setTechnology("1");
    List<CfgServerNocEntity> lstServer = Mockito.spy(ArrayList.class);
    CfgServerNocEntity cfgServerNocEntity = Mockito.spy(CfgServerNocEntity.class);
    lstServer.add(cfgServerNocEntity);
    List<com.viettel.bccs2.CauseDTO> lstCauseDTO = Mockito.spy(ArrayList.class);
    com.viettel.bccs2.CauseDTO causeDTO = Mockito.spy(com.viettel.bccs2.CauseDTO.class);
    causeDTO.setCauseId(1L);
    lstCauseDTO.add(causeDTO);
    SpmRespone res = Mockito.spy(SpmRespone.class);
    PowerMockito.when(
        cfgServerNocRepository
            .getListCfgServerNocByCondition(anyString())
    ).thenReturn(lstServer);
    PowerMockito.when(wsbccs2Port.getListCause(any(), anyInt(), any())).thenReturn(res);
    PowerMockito.when(
        troublesServiceForCCBusiness
            .getCompCauseDTOForCC3(anyString(), anyString(), anyString(), any())
    ).thenReturn(lstCauseDTO);

    ResultInSideDto actual = troublesBusiness.getListReasonBCCS(troublesInSideDTO, 1L, 1);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListReasonOverdue_01() throws Exception {
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setInsertSource("SPM");
    List<CfgServerNocDTO> lstServer = Mockito.spy(ArrayList.class);
    CfgServerNocDTO cfgServerNocDTO = Mockito.spy(CfgServerNocDTO.class);
    lstServer.add(cfgServerNocDTO);
    SpmRespone res = Mockito.spy(SpmRespone.class);
    res.setErrorCode("00");
    List<CauseErrorExpireDTO> lstCause = Mockito.spy(ArrayList.class);
    CauseErrorExpireDTO causeErrorExpireDTO = Mockito.spy(CauseErrorExpireDTO.class);
    causeErrorExpireDTO.setCauseErrExpId(1L);
    causeErrorExpireDTO.setCode("1");
    causeErrorExpireDTO.setName("1");
    lstCause.add(causeErrorExpireDTO);

    PowerMockito.when(
        cfgServerNocRepository
            .getListCfgServerNocByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstServer);
    PowerMockito.when(wsbccs2Port.getCauseExistError(any(), any())).thenReturn(res);
    PowerMockito.when(res.getListCauseErrorExpire()).thenReturn(lstCause);

    List actual = troublesBusiness.getListReasonOverdue(troublesInSideDTO, 1L);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListReasonOverdue_02() throws Exception {
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setInsertSource("AAA_SPM");
    List<CfgServerNocDTO> lstServer = Mockito.spy(ArrayList.class);
    CfgServerNocDTO cfgServerNocDTO = Mockito.spy(CfgServerNocDTO.class);
    lstServer.add(cfgServerNocDTO);
    List<com.viettel.bccs2.CauseErrorExpireDTO> lstErrorExpireDTO = Mockito.spy(ArrayList.class);
    com.viettel.bccs2.CauseErrorExpireDTO causeErrorExpireDTO = Mockito
        .spy(com.viettel.bccs2.CauseErrorExpireDTO.class);
    causeErrorExpireDTO.setCauseErrExpId(1L);
    causeErrorExpireDTO.setCode("1");
    causeErrorExpireDTO.setName("1");
    lstErrorExpireDTO.add(causeErrorExpireDTO);

    PowerMockito.when(
        cfgServerNocRepository
            .getListCfgServerNocByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstServer);
    PowerMockito.when(
        troublesServiceForCCBusiness
            .getCauseErrorExpireForCC3(anyString(), any())
    ).thenReturn(lstErrorExpireDTO);

    List actual = troublesBusiness.getListReasonOverdue(troublesInSideDTO, 1L);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListGroupSolution() {
  }

  @Test
  public void getLstNetworkLevel() {
  }

  @Test
  public void getListTroubleSearchExport() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<String> lstType = Mockito.spy(ArrayList.class);
    lstType.add("1111");
    TroublesInSideDTO troubleSearch = Mockito.spy(TroublesInSideDTO.class);
    troubleSearch.setLstType(lstType);
    troubleSearch.setState(1111L);
    troubleSearch.setTypeId(2046L);
    troubleSearch.setSubCategoryId(1111L);
    troubleSearch.setPriorityId(1111L);
    troubleSearch.setImpactId(1111L);
    troubleSearch.setSolutionType(1111L);
    troubleSearch.setRisk(1111L);
    troubleSearch.setVendorId(1111L);
    troubleSearch.setRejectedCode(1111L);
    troubleSearch.setWarnLevel(1111L);
    troubleSearch.setCableType(1111L);
    troubleSearch.setAutoClose(1111L);
    troubleSearch.setCreatedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleSearch.setCatchingTime(DateTimeUtils.convertStringToDate("22/08/2020"));
    troubleSearch.setTimeCreateCfg(0.01);
    troubleSearch.setBeginTroubleTime(DateTimeUtils.convertStringToDate("21/08/2020"));
    troubleSearch.setEndTroubleTime(DateTimeUtils.convertStringToDate("23/08/2020"));
    troubleSearch.setClosedTime(DateTimeUtils.convertStringToDate("23/08/2020"));
    troubleSearch.setRemainTime("0.05");
    troubleSearch.setIsStationVip(1L);
    troubleSearch.setTransNetworkTypeId(1111L);
    troubleSearch.setTransReasonEffectiveId(1111L);
    troubleSearch.setCreatedTimeFrom("19/08/2020");
    troubleSearch.setCreatedTimeTo("25/08/2020");
    Map<String, String> mapConfigProperty = Mockito.spy(HashMap.class);
    mapConfigProperty.put(CONFIG_PROPERTY.TT_TYPE_TRANS, "1111,2222");
    List<TroublesInSideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(troubleSearch);
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1111L);
    catItemDTO.setItemCode("2046");
    catItemDTO.setItemName("1111");
    lstCatItem.add(catItemDTO);

    PowerMockito.when(troublesRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(troublesRepository.getTroublesSearchExport(any())).thenReturn(lst);
    PowerMockito.when(
        catItemBusiness
            .getListCatItemDTOByListCategoryLE(anyString(), anyString(), anyString(), any())
    ).thenReturn(lstCatItem);
    PowerMockito.when(
        catItemBusiness
            .getListItemByCategoryAndParent(anyString(), any())
    ).thenReturn(lstCatItem);
    PowerMockito.when(
        catReasonBusiness.getListCatReason(anyLong(), anyString())
    ).thenReturn(lstCatItem);

    File actual = troublesBusiness.getListTroubleSearchExport(troubleSearch);

    Assert.assertNull(actual);
  }

  @Test
  public void setTimeZoneForTrouble() {
  }

  @Test
  public void updateTroubleDTOInfo() {
  }

  @Test
  public void getMapItem() {
  }

  @Test
  public void searchCrRelated() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    CrSearchDTO crSearchDTO = Mockito.spy(CrSearchDTO.class);
    crSearchDTO.setCrNumber("1");
    crSearchDTO.setTitle("Title");
    crSearchDTO.setEarliestStartTimeTo("26/08/2020 12:00:00");
    crSearchDTO.setEarliestStartTime("26/08/2020 12:00:00");
    crSearchDTO.setPage(1);
    crSearchDTO.setPageSize(5);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setPages(1);
    datatable.setTotal(1);
    List<LinkedHashMap> lstHasMap = Mockito.spy(ArrayList.class);
    LinkedHashMap linkedHashMap = Mockito.spy(LinkedHashMap.class);
    linkedHashMap.put("crId", "11");
    linkedHashMap.put("changeOrginatorName", "11");
    linkedHashMap.put("crNumber", "11");
    linkedHashMap.put("title", "11");
    linkedHashMap.put("state", "11");
    linkedHashMap.put("earliestStartTime", "2020-08-26T12:00:00.000+0000");
    linkedHashMap.put("latestStartTime", "2020-08-26T12:00:00.000+0000");
    lstHasMap.add(linkedHashMap);
    datatable.setData(lstHasMap);
    PowerMockito.when(crServiceProxy.onSearch(any())).thenReturn(datatable);

    Datatable actual = troublesBusiness.searchCrRelated(crSearchDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void loadCrRelatedDetail() {
    List<CrDTO> dtoCRResult = Mockito.spy(ArrayList.class);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    dtoCRResult.add(crDTO);
    PowerMockito.when(crServiceProxy.getListCrByCondition(any())).thenReturn(dtoCRResult);
    List<CrSearchDTO> actual = troublesBusiness.loadCrRelatedDetail("111");
    Assert.assertNotNull(actual);
  }

  @Test
  public void loadTroubleCrDTO() {
    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    dto.setPage(1);
    List<CrInsiteDTO> dtoCRResult = Mockito.spy(ArrayList.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setTotalRow(5);
    dtoCRResult.add(crInsiteDTO);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(dtoCRResult);
    Datatable actual = troublesBusiness.loadTroubleCrDTO(dto);
    Assert.assertNotNull(actual);
  }

  @Test
  public void sendTicketToTKTU_01() throws Exception {
    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    ResultDTO rDto = Mockito.spy(ResultDTO.class);
    rDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(troublesServiceForCCBusiness.sendTicketToTKTU(any())).thenReturn(rDto);
    ResultInSideDto actual = troublesBusiness.sendTicketToTKTU(dto);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void sendTicketToTKTU_02() throws Exception {
    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    ResultDTO rDto = Mockito.spy(ResultDTO.class);
    rDto.setKey(RESULT.ERROR);
    PowerMockito.when(troublesServiceForCCBusiness.sendTicketToTKTU(any())).thenReturn(rDto);
    ResultInSideDto actual = troublesBusiness.sendTicketToTKTU(dto);
    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void insertCrCreatedFromOtherSystem() {
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    List<CrDTO> list = Mockito.spy(ArrayList.class);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    list.add(crDTO);
    troublesDTO.setCrDTOS(list);
    troublesBusiness.insertCrCreatedFromOtherSystem(troublesDTO);
  }

  @Test
  public void getAlarmClearGNOC() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");
    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setAlarmId(1L);
    troubleDTO.setBeginTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troubleDTO.setTblCurr("11");
    troubleDTO.setTblHis("11");
    troubleDTO.setTblClear("11");
    troubleDTO.setTroubleCode("2222");
    troubleDTO.setInsertSource(INSERT_SOURCE.SPM_VTNET);
    List<CfgServerNocDTO> lst = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        cfgServerNocRepository
            .getListCfgServerNocByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lst);

    ResultInSideDto actual = troublesBusiness.getAlarmClearGNOC(troubleDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void getDataOfTabWO() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleCode("2222");
    troubleDTO.setPageSize(5);
    troubleDTO.setPage(1);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");

    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("9");
    lstWo.add(woDTOSearch);

    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);

    Datatable actual = troublesBusiness.getDataOfTabWO(troubleDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void checkWoRequiredClosed() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTypeId(1L);
    troubleDTO.setAlarmGroupId("1");
    troubleDTO.setReasonId(1L);
    troubleDTO.setTroubleCode("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");

    List<CfgRequireHaveWoDTO> lstHaveWoDTO = Mockito.spy(ArrayList.class);
    CfgRequireHaveWoDTO cfgRequireHaveWoDTO = Mockito.spy(CfgRequireHaveWoDTO.class);
    cfgRequireHaveWoDTO.setWoTypeId("1");
    lstHaveWoDTO.add(cfgRequireHaveWoDTO);

    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoTypeId("1,2");
    lstWo.add(woDTOSearch);

    List<WoTypeInsideDTO> lstWoType = Mockito.spy(ArrayList.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    lstWoType.add(woTypeInsideDTO);

    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(
        cfgRequireHaveWoRepository
            .getListCfgRequireHaveWoDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstHaveWoDTO);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(woCategoryServiceProxy.getListWoTypeDTO(any())).thenReturn(lstWoType);

    String actual = troublesBusiness.checkWoRequiredClosed(troubleDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListDataSearchWo() {
  }

  @Test
  public void countTroubleByStation() {
  }

  @Test
  public void updateSmsContent() {
  }

  @Test
  public void convertStatusCR() {
  }

  @Test
  public void getMapWoType() {
  }

  @Test
  public void searchParentTTForCR() {
  }

  @Test
  public void countTicketByShift() {
  }

  @Test
  public void onUpdateTroubleMobile_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setStateName("QUEUE");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setStateName("CLOSED");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_07() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_08() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_09() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_10() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_11() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_12() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("DEFERRED");
    tForm.setStateWo("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_13() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("DEFERRED");
    tForm.setStateWo("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_14() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("DEFERRED");
    tForm.setStateWo("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_15() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("DEFERRED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_16() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("DEFERRED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_17() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("DEFERRED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_18() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("DEFERRED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_19() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_20() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_21() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_22() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_23() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_24() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_25() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");
    tForm.setReasonLv2Name("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_26() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");
    tForm.setReasonLv2Name("1");
    tForm.setReasonLv3Id("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_27() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");
    tForm.setReasonLv2Name("1");
    tForm.setReasonLv3Id("1");
    tForm.setReasonLv3Name("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_28() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");
    tForm.setReasonLv2Name("1");
    tForm.setReasonLv3Id("1");
    tForm.setReasonLv3Name("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_29() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");
    tForm.setReasonLv2Name("1");
    tForm.setReasonLv3Id("1");
    tForm.setReasonLv3Name("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(7L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_30() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("CLOSED");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");
    tForm.setReasonLv2Name("1");
    tForm.setReasonLv3Id("1");
    tForm.setReasonLv3Name("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(11L);
    troublesInSideDTO.setIsTickHelp(2L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_31() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("QUEUE");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");
    tForm.setReasonLv2Name("1");
    tForm.setReasonLv3Id("1");
    tForm.setReasonLv3Name("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(11L);
    troublesInSideDTO.setIsTickHelp(2L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_32() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("UPDATE");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");
    tForm.setReasonLv2Name("1");
    tForm.setReasonLv3Id("1");
    tForm.setReasonLv3Name("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(11L);
    troublesInSideDTO.setIsTickHelp(2L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    lst.add(catItemDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_33() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("UPDATE");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");
    tForm.setReasonLv2Name("1");
    tForm.setReasonLv3Id("1");
    tForm.setReasonLv3Name("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(11L);
    troublesInSideDTO.setIsTickHelp(2L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    lst.add(catItemDTO);

    TroublesEntity troubles = Mockito.spy(TroublesEntity.class);
    troubles.setInsertSource("SPM");
    troubles.setState(1L);

    com.viettel.soc.spm.service.ResultDTO resSpm = Mockito
        .spy(com.viettel.soc.spm.service.ResultDTO.class);
    resSpm.setKey(RESULT.ERROR);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(troubles);
    PowerMockito.when(troubleSpmUtils.updateSpmAction(any(), any(), anyString()))
        .thenReturn(resSpm);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_34() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUserId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setProcessingUserPhone("1");
    tForm.setStateName("UPDATE");
    tForm.setStateWo("1");
    tForm.setDeferredReason("1");
    tForm.setDeferredTime("28/08/2020 12:00:00");
    tForm.setNumPending("1");
    tForm.setTotalPendingTimeGnoc("1");
    tForm.setTotalProcessTimeGnoc("1");
    tForm.setEvaluateGnoc("1");
    tForm.setWorkLog("1");
    tForm.setIsCheck("1");
    tForm.setEndTroubleTime("28/08/2020 12:00:00");
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");
    tForm.setReasonLv2Name("1");
    tForm.setReasonLv3Id("1");
    tForm.setReasonLv3Name("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setState(11L);
    troublesInSideDTO.setIsTickHelp(2L);

    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    lst.add(catItemDTO);

    TroublesEntity troubles = Mockito.spy(TroublesEntity.class);
    troubles.setInsertSource("SPM");
    troubles.setState(1L);

    com.viettel.soc.spm.service.ResultDTO resSpm = Mockito
        .spy(com.viettel.soc.spm.service.ResultDTO.class);
    resSpm.setKey(RESULT.SUCCESS);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any())).thenReturn(lst);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(troubles);
    PowerMockito.when(troubleSpmUtils.updateSpmAction(any(), any(), anyString()))
        .thenReturn(resSpm);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleMobile_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setReceiveUnitId("1");
    tForm.setReceiveUnitName("1");
    tForm.setReceiveUserName("1");
    tForm.setStateName("DEFERRED");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onUpdateTroubleMobile(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");
    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(null);
    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("1");
    troublesDTO.setReasonId("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");
    troublesInSideDTO.setState(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_07() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("1");
    troublesDTO.setReasonId("1");
    troublesDTO.setReasonName("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");
    troublesInSideDTO.setState(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_08() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("1");
    troublesDTO.setReasonId("1");
    troublesDTO.setReasonName("1");
    troublesDTO.setReceiveUnitId("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");
    troublesInSideDTO.setState(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_09() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("1");
    troublesDTO.setReasonId("1");
    troublesDTO.setReasonName("1");
    troublesDTO.setReceiveUnitId("1");
    troublesDTO.setReceiveUserId("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");
    troublesInSideDTO.setState(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_10() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("1");
    troublesDTO.setReasonId("1");
    troublesDTO.setReasonName("1");
    troublesDTO.setReceiveUnitId("1");
    troublesDTO.setReceiveUserId("1");
    troublesDTO.setReceiveUnitName("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");
    troublesInSideDTO.setState(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_11() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("1");
    troublesDTO.setReasonId("1");
    troublesDTO.setReasonName("1");
    troublesDTO.setReceiveUnitId("1");
    troublesDTO.setReceiveUserId("1");
    troublesDTO.setReceiveUnitName("1");
    troublesDTO.setReceiveUserName("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");
    troublesInSideDTO.setState(1L);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_12() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("1");
    troublesDTO.setReasonId("1");
    troublesDTO.setReasonName("1");
    troublesDTO.setReceiveUnitId("1");
    troublesDTO.setReceiveUserId("1");
    troublesDTO.setReceiveUnitName("1");
    troublesDTO.setReceiveUserName("1");
    troublesDTO.setEndTroubleTime("26/08/2020");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_13() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("1");
    troublesDTO.setReasonId("1");
    troublesDTO.setReasonName("1");
    troublesDTO.setReceiveUnitId("1");
    troublesDTO.setReceiveUserId("1");
    troublesDTO.setReceiveUnitName("1");
    troublesDTO.setReceiveUserName("1");
    troublesDTO.setEndTroubleTime("26/08/2020");
    troublesDTO.setRootCause("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_14() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("1");
    troublesDTO.setReasonId("1");
    troublesDTO.setReasonName("1");
    troublesDTO.setReceiveUnitId("1");
    troublesDTO.setReceiveUserId("1");
    troublesDTO.setReceiveUnitName("1");
    troublesDTO.setReceiveUserName("1");
    troublesDTO.setEndTroubleTime("26/08/2020");
    troublesDTO.setRootCause("1");
    troublesDTO.setSolutionType("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_15() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("5");
    troublesDTO.setReasonId("1");
    troublesDTO.setReasonName("1");
    troublesDTO.setReceiveUnitId("1");
    troublesDTO.setReceiveUserId("1");
    troublesDTO.setReceiveUnitName("1");
    troublesDTO.setReceiveUserName("1");
    troublesDTO.setEndTroubleTime("26/08/2020");
    troublesDTO.setRootCause("1");
    troublesDTO.setSolutionType("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onClosetroubleFromWo_16() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1");
    troublesDTO.setState("1");
    troublesDTO.setReasonId("1");
    troublesDTO.setReasonName("1");
    troublesDTO.setReceiveUnitId("1");
    troublesDTO.setReceiveUserId("1");
    troublesDTO.setReceiveUnitName("1");
    troublesDTO.setReceiveUserName("1");
    troublesDTO.setEndTroubleTime("26/08/2020 12:00:00");
    troublesDTO.setRootCause("1");
    troublesDTO.setSolutionType("1");
    troublesDTO.setWorkArround("1");
    troublesDTO.setReasonName("1");
    troublesDTO.setQueueTime("26/08/2020 12:00:00");
    troublesDTO.setClosuresReplace("1");
    troublesDTO.setLineCutCode("1");
    troublesDTO.setCodeSnippetOff("1");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("1");
    troublesInSideDTO.setInsertSource("NOC");
    troublesInSideDTO.setState(1L);
    troublesInSideDTO.setEndTroubleTime(DateTimeUtils.convertStringToDate("25/08/2020 12:00:00"));
    troublesInSideDTO.setReasonId(1L);
    troublesInSideDTO.setRootCause("1");
    troublesInSideDTO.setSolutionType(1L);
    troublesInSideDTO.setWorkArround("1");
    troublesInSideDTO.setReasonName("1");
    troublesInSideDTO.setQueueTime(DateTimeUtils.convertStringToDate("25/08/2020 12:00:00"));
    troublesInSideDTO.setClosuresReplace("1");
    troublesInSideDTO.setLineCutCode("1");
    troublesInSideDTO.setCodeSnippetOff("1");
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setAlarmGroupId("1");
    troublesInSideDTO.setReceiveUnitId(1L);
    troublesInSideDTO.setTroubleName("1");
    troublesInSideDTO.setTroubleCode("1");
    troublesInSideDTO.setReceiveUnitName("1");
    troublesInSideDTO.setCreateUnitName("1");
    troublesInSideDTO.setCreateUserName("1");
    troublesInSideDTO.setPriorityName("1");
    troublesInSideDTO.setStateName("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");

    List<CfgRequireHaveWoDTO> lstHaveWoDTO = Mockito.spy(ArrayList.class);
    CfgRequireHaveWoDTO cfgRequireHaveWoDTO = Mockito.spy(CfgRequireHaveWoDTO.class);
    cfgRequireHaveWoDTO.setWoTypeId("1");
    lstHaveWoDTO.add(cfgRequireHaveWoDTO);

    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoTypeId("1,2");
    lstWo.add(woDTOSearch);

    List<WoTypeInsideDTO> lstWoType = Mockito.spy(ArrayList.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    lstWoType.add(woTypeInsideDTO);

    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setIsNotReceiveMessage(2L);
    usersInsideDto.setUserLanguage("2");
    usersInsideDto.setUserId(9999L);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setMobile("0909999999");
    lstUser.add(usersInsideDto);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemValue("1");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    lstCatItem.add(catItemDTO);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitName("UNIT");
    unitDTO.setUnitCode("UNIT");
    unitDTO.setSmsGatewayId(1L);
    List<UnitDTO> lstUnit = Mockito.spy(ArrayList.class);
    lstUnit.add(unitDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(
        cfgRequireHaveWoRepository
            .getListCfgRequireHaveWoDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstHaveWoDTO);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(woCategoryServiceProxy.getListWoTypeDTO(any())).thenReturn(lstWoType);
    PowerMockito.when(
        userBusiness
            .getListUsersByCondition(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstUser);
    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(catItemDTO);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCatItem);
    PowerMockito.when(
        unitRepository
            .getListUnitDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstUnit);
    PowerMockito.when(DataUtil.getLang(any(), anyString())).thenReturn("1");

    ResultDTO actual = troublesBusiness.onClosetroubleFromWo(troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void checkAlarmNOC_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");
    ResultDTO actual = troublesBusiness.checkAlarmNOC(null, null);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void checkAlarmNOC_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");
    ResultDTO actual = troublesBusiness.checkAlarmNOC("11", null);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void checkAlarmNOC_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);

    ResultDTO actual = troublesBusiness.checkAlarmNOC("11", "11");

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void checkAlarmNOC_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setAlarmGroupId("1");
    troublesInSideDTO.setTypeId(1L);

    List<CatCfgClosedTicketDTO> lstClosedTicketDTO = Mockito.spy(ArrayList.class);
    CatCfgClosedTicketDTO catCfgClosedTicketDTO = Mockito.spy(CatCfgClosedTicketDTO.class);
    lstClosedTicketDTO.add(catCfgClosedTicketDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(
        catCfgClosedTicketRepository
            .getListCatCfgClosedTicketDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstClosedTicketDTO);

    ResultDTO actual = troublesBusiness.checkAlarmNOC("11", "11");

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void checkAlarmNOC_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setAlarmGroupId("1");
    troublesInSideDTO.setTypeId(1L);
    troublesInSideDTO.setInsertSource("NOC");
    troublesInSideDTO.setAlarmId(1L);
    troublesInSideDTO.setBeginTroubleTime(
        DateTimeUtils.convertStringToDateTime("27/08/2020 12:00:00")
    );
    troublesInSideDTO.setTblClear("1");
    troublesInSideDTO.setTblCurr("1");
    troublesInSideDTO.setTblHis("1");
    troublesInSideDTO.setTroubleCode("1");

    List<CatCfgClosedTicketDTO> lstClosedTicketDTO = Mockito.spy(ArrayList.class);
    CatCfgClosedTicketDTO catCfgClosedTicketDTO = Mockito.spy(CatCfgClosedTicketDTO.class);
    lstClosedTicketDTO.add(catCfgClosedTicketDTO);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(
        catCfgClosedTicketRepository
            .getListCatCfgClosedTicketDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstClosedTicketDTO);

    ResultDTO actual = troublesBusiness.checkAlarmNOC("11", "11");

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void getTroubleByCode() {
  }

  @Test
  public void getStatisticTroubleTotal_01() {
    List<TroubleStatisticForm> list = Mockito.spy(ArrayList.class);
    TroubleStatisticForm trouble1 = Mockito.spy(TroubleStatisticForm.class);
    trouble1.setStatusCode("WAITING RECEIVE");
    trouble1.setPriorityCode("TT_Major");
    trouble1.setRemainTime(-0.5);
    TroubleStatisticForm trouble2 = Mockito.spy(TroubleStatisticForm.class);
    trouble2.setStatusCode("WAITING RECEIVE");
    trouble2.setPriorityCode("TT_Critical");
    trouble2.setRemainTime(-0.5);
    TroubleStatisticForm trouble3 = Mockito.spy(TroubleStatisticForm.class);
    trouble3.setStatusCode("WAITING RECEIVE");
    trouble3.setPriorityCode("TT_Minor");
    trouble3.setRemainTime(-0.5);
    list.add(trouble1);
    list.add(trouble2);
    list.add(trouble3);

    PowerMockito.when(
        troublesRepository
            .getStatisticTroubleDetail(anyString(), anyBoolean(), anyBoolean(), anyString(),
                anyString())
    ).thenReturn(list);

    List<TroubleStatisticForm> actual = troublesBusiness
        .getStatisticTroubleTotal(
            "1", true, true, "27/08/2020", "28/08/2020"
        );

    Assert.assertNotNull(actual);
  }

  @Test
  public void getStatisticTroubleTotal_02() {
    List<TroubleStatisticForm> list = Mockito.spy(ArrayList.class);
    TroubleStatisticForm trouble1 = Mockito.spy(TroubleStatisticForm.class);
    trouble1.setStatusCode("QUEUE");
    trouble1.setPriorityCode("TT_Major");
    trouble1.setRemainTime(0.5);
    TroubleStatisticForm trouble2 = Mockito.spy(TroubleStatisticForm.class);
    trouble2.setStatusCode("QUEUE");
    trouble2.setPriorityCode("TT_Critical");
    trouble2.setRemainTime(0.5);
    TroubleStatisticForm trouble3 = Mockito.spy(TroubleStatisticForm.class);
    trouble3.setStatusCode("QUEUE");
    trouble3.setPriorityCode("TT_Minor");
    trouble3.setRemainTime(0.5);
    list.add(trouble1);
    list.add(trouble2);
    list.add(trouble3);

    PowerMockito.when(
        troublesRepository
            .getStatisticTroubleDetail(anyString(), anyBoolean(), anyBoolean(), anyString(),
                anyString())
    ).thenReturn(list);

    List<TroubleStatisticForm> actual = troublesBusiness
        .getStatisticTroubleTotal(
            "1", true, true, "27/08/2020", "28/08/2020"
        );

    Assert.assertNotNull(actual);
  }

  @Test
  public void getStatisticTroubleTotal_03() {
    List<TroubleStatisticForm> list = Mockito.spy(ArrayList.class);
    TroubleStatisticForm trouble1 = Mockito.spy(TroubleStatisticForm.class);
    trouble1.setStatusCode("SOLUTION FOUND");
    trouble1.setPriorityCode("TT_Major");
    trouble1.setRemainTime(0.5);
    TroubleStatisticForm trouble2 = Mockito.spy(TroubleStatisticForm.class);
    trouble2.setStatusCode("SOLUTION FOUND");
    trouble2.setPriorityCode("TT_Critical");
    trouble2.setRemainTime(0.5);
    TroubleStatisticForm trouble3 = Mockito.spy(TroubleStatisticForm.class);
    trouble3.setStatusCode("SOLUTION FOUND");
    trouble3.setPriorityCode("TT_Minor");
    trouble3.setRemainTime(0.5);
    list.add(trouble1);
    list.add(trouble2);
    list.add(trouble3);

    PowerMockito.when(
        troublesRepository
            .getStatisticTroubleDetail(anyString(), anyBoolean(), anyBoolean(), anyString(),
                anyString())
    ).thenReturn(list);

    List<TroubleStatisticForm> actual = troublesBusiness
        .getStatisticTroubleTotal(
            "1", true, true, "27/08/2020", "28/08/2020"
        );

    Assert.assertNotNull(actual);
  }

  @Test
  public void getStatisticTroubleTotal_04() {
    List<TroubleStatisticForm> list = Mockito.spy(ArrayList.class);
    TroubleStatisticForm trouble1 = Mockito.spy(TroubleStatisticForm.class);
    trouble1.setStatusCode("WAIT FOR DEFERRED");
    trouble1.setPriorityCode("TT_Major");
    trouble1.setRemainTime(0.5);
    TroubleStatisticForm trouble2 = Mockito.spy(TroubleStatisticForm.class);
    trouble2.setStatusCode("WAIT FOR DEFERRED");
    trouble2.setPriorityCode("TT_Critical");
    trouble2.setRemainTime(0.5);
    TroubleStatisticForm trouble3 = Mockito.spy(TroubleStatisticForm.class);
    trouble3.setStatusCode("WAIT FOR DEFERRED");
    trouble3.setPriorityCode("TT_Minor");
    trouble3.setRemainTime(0.5);
    list.add(trouble1);
    list.add(trouble2);
    list.add(trouble3);

    PowerMockito.when(
        troublesRepository
            .getStatisticTroubleDetail(anyString(), anyBoolean(), anyBoolean(), anyString(),
                anyString())
    ).thenReturn(list);

    List<TroubleStatisticForm> actual = troublesBusiness
        .getStatisticTroubleTotal(
            "1", true, true, "27/08/2020", "28/08/2020"
        );

    Assert.assertNotNull(actual);
  }

  @Test
  public void getStatisticTroubleTotal_05() {
    List<TroubleStatisticForm> list = Mockito.spy(ArrayList.class);
    TroubleStatisticForm trouble1 = Mockito.spy(TroubleStatisticForm.class);
    trouble1.setStatusCode("DEFERRED");
    trouble1.setPriorityCode("TT_Major");
    trouble1.setRemainTime(0.5);
    TroubleStatisticForm trouble2 = Mockito.spy(TroubleStatisticForm.class);
    trouble2.setStatusCode("DEFERRED");
    trouble2.setPriorityCode("TT_Critical");
    trouble2.setRemainTime(0.5);
    TroubleStatisticForm trouble3 = Mockito.spy(TroubleStatisticForm.class);
    trouble3.setStatusCode("DEFERRED");
    trouble3.setPriorityCode("TT_Minor");
    trouble3.setRemainTime(0.5);
    list.add(trouble1);
    list.add(trouble2);
    list.add(trouble3);

    PowerMockito.when(
        troublesRepository
            .getStatisticTroubleDetail(anyString(), anyBoolean(), anyBoolean(), anyString(),
                anyString())
    ).thenReturn(list);

    List<TroubleStatisticForm> actual = troublesBusiness
        .getStatisticTroubleTotal(
            "1", true, true, "27/08/2020", "28/08/2020"
        );

    Assert.assertNotNull(actual);
  }

  @Test
  public void getInfoTicketForAMI_01() throws Exception {
    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(FileUtils.class);
    List<String> stringList = Mockito.spy(ArrayList.class);
    stringList.add("1");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCodes(stringList);
    troublesDTO.setAmiIds(stringList);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);

    List<GnocFileDto> lstFile = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("/temp");
    lstFile.add(gnocFileDto);
    byte[] bytes = new byte[16];

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.getFileByTrouble(any())).thenReturn(lstFile);
    PowerMockito.when(PassTranformer.decrypt(anyString()))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt(anyString()))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(
        FileUtils
            .getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString())
    ).thenReturn(bytes);

    List<TroublesDTO> actual = troublesBusiness.getInfoTicketForAMI(troublesDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getInfoTicketForAMI_02() throws Exception {
    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(FileUtils.class);
    List<String> stringList = Mockito.spy(ArrayList.class);
    stringList.add("1");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setAmiIds(stringList);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);

    List<GnocFileDto> lstFile = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("/temp");
    lstFile.add(gnocFileDto);
    byte[] bytes = new byte[16];

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), any(), any(), any(), any(), any(), any())
    ).thenReturn(troublesInSideDTO);
    PowerMockito.when(troublesRepository.getFileByTrouble(any())).thenReturn(lstFile);
    PowerMockito.when(PassTranformer.decrypt(anyString()))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt(anyString()))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(
        FileUtils
            .getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString())
    ).thenReturn(bytes);

    List<TroublesDTO> actual = troublesBusiness.getInfoTicketForAMI(troublesDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListCondition() {
  }

  @Test
  public void closetroubleFromWo() {
  }

  @Test
  public void validate() {
  }

  @Test
  public void convertStatus2String() {
  }

  @Test
  public void getListFileAttachByTroubleId() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<GnocFileDto> lstFile = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setCreateTime(DateTimeUtils.convertStringToDate("27/08/2020 09:30:00"));
    lstFile.add(gnocFileDto);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstFile);

    PowerMockito.when(troublesRepository.getListFileAttachByTroubleId(any())).thenReturn(datatable);

    Datatable actual = troublesBusiness.getListFileAttachByTroubleId(gnocFileDto);

    Assert.assertNotNull(actual);
  }

  @Test
  public void insertTroubleFilesUpload() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(FileUtils.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(testFile);

    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setTroubleId(1L);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("UNIT");
    unitToken.setUnitCode("UNIT");

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);

    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(PassTranformer.decrypt(anyString()))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt(anyString()))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(
        FileUtils
            .saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(),
                any(), any())
    ).thenReturn("/temp");
    PowerMockito.when(
        FileUtils
            .saveUploadFile(anyString(), any(), anyString(), any())
    ).thenReturn("/temp");
    PowerMockito.when(FileUtils.getFilePath(anyString())).thenReturn("/temp");
    PowerMockito.when(FileUtils.getFileName(anyString())).thenReturn("data");
    PowerMockito.when(troublesRepository.insertCommonFile(any())).thenReturn(resultInSideDto);

    ResultInSideDto actual = troublesBusiness.insertTroubleFilesUpload(files, troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void updateReasonTroubleFromNOC_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultDTO actual = troublesBusiness.updateReasonTroubleFromNOC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void updateReasonTroubleFromNOC_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1");
    tForm.setProcessingUserName("1");
    tForm.setProcessingUnitName("1");
    tForm.setReasonId("1");
    tForm.setReasonName("1");
    tForm.setRootCause("1");
    tForm.setWorkArround("1");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setState(1L);
    troubleDTO.setReasonName("1");
    troubleDTO.setRootCause("1");
    troubleDTO.setWorkArround("1");

    TroublesEntity troublesDTO = Mockito.spy(TroublesEntity.class);
    troublesDTO.setState(1L);
    troublesDTO.setTimeProcess(0.05);
    troublesDTO.setTimeUsed(0.04);
    troublesDTO.setAssignTimeTemp(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setTroubleId(1L);
    troublesDTO.setCreatedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setLastUpdateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setAssignTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setQueueTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setClearTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setClosedTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setBeginTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setEndTroubleTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setDeferredTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setDateMove(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setCatchingTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    troublesDTO.setEstimateTime(DateTimeUtils.convertStringToDate("20/08/2020"));
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("WAIT FOR DEFERRED");

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(troublesRepository.findTroublesById(anyLong())).thenReturn(troublesDTO);
    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(catItemDTO);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(2.00);

    ResultDTO actual = troublesBusiness.updateReasonTroubleFromNOC(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void getListProblemGroupParent() {
  }

  @Test
  public void getListProblemGroupByParrenId() {
  }

  @Test
  public void getListPobTypeByGroupId() {
  }

  @Test
  public void getListActionInfo() {
  }

  @Test
  public void onUpdateTrouble_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleId(1L);
    tForm.setState(1L);

    TroublesInSideDTO troubleOld = Mockito.spy(TroublesInSideDTO.class);
    troubleOld.setState(1L);

    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("CLOSED");

    CatItemDTO stateDTO = Mockito.spy(CatItemDTO.class);

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(String.valueOf(tForm.getTroubleId()), null, null, null, null, null, null)
    ).thenReturn(troubleOld);
    PowerMockito.when(
        troublesRepository
            .findTroublesById(tForm.getTroubleId())
    ).thenReturn(trouble);
    PowerMockito.when(
        catItemBusiness.getCatItemById(anyLong())
    ).thenReturn(catItemDTO);

    ResultInSideDto actual = troublesBusiness.onUpdateTrouble(tForm, true);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void onUpdateTrouble_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.mockStatic(org.apache.commons.io.FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    //<editor-fold desc tripm test dataHeader defaultstate="collapsed">
    tForm.setTroubleId(1L);
    tForm.setState(7L);
    tForm.setAffectedNode("1");
    tForm.setPriorityId(1L);
    tForm.setImpactId(1L);
    tForm.setLastUpdateTime(DateTimeUtils.convertStringToDate("27/08/2020"));
    tForm.setDescription("1");
    tForm.setTroubleName("1");
    tForm.setReceiveUserId(1L);
    tForm.setReceiveUserName("1");
    tForm.setAffectedService("1");
    tForm.setBeginTroubleTime(DateTimeUtils.convertStringToDate("27/08/2020"));
    tForm.setEndTroubleTime(DateTimeUtils.convertStringToDate("27/08/2020"));
    tForm.setTypeId(1L);
    tForm.setReceiveUnitId(1L);
    tForm.setReceiveUnitName("1");
    tForm.setSubCategoryId(1L);
    tForm.setLocationId(1L);
    tForm.setLocation("1");
    tForm.setRisk(1L);
    tForm.setReasonId(1L);
    tForm.setReasonName("1");
    tForm.setVendorId(1L);
    tForm.setAffectedNode("1");
    tForm.setCloseCode(1L);
    tForm.setRootCause("1");
    tForm.setRejectedCode(1L);
    tForm.setDeferredTime(DateTimeUtils.convertStringToDate("27/08/2020"));
    tForm.setDeferredReason("1");
    tForm.setSupportUnitId(1L);
    tForm.setSupportUnitName("1");
    tForm.setRejectReason("1");
    tForm.setRelatedKedb("1");
    tForm.setSolutionType(1L);
    tForm.setWorkArround("1");
    tForm.setNetworkLevel("1");
    tForm.setLineCutCode("1");
    tForm.setCodeSnippetOff("1");
    tForm.setCableType(1L);
    tForm.setClosuresReplace("1");
    tForm.setWhereWrong("1");
    tForm.setAsessmentData(1L);
    tForm.setReasonLv1Id("1");
    tForm.setReasonLv1Name("1");
    tForm.setReasonLv2Id("1");
    tForm.setReasonLv2Name("1");
    tForm.setReasonLv3Id("1");
    tForm.setReasonLv3Name("1");
    tForm.setReasonOverdueId("1");
    tForm.setReasonOverdueId2("1");
    tForm.setReasonOverdueName("1");
    tForm.setReasonOverdueName2("1");
    tForm.setSpmCode("1");
    tForm.setTransNetworkTypeId(1L);
    tForm.setTransReasonEffectiveId(1L);
    tForm.setTransReasonEffectiveContent("1");
    tForm.setAutoClose(1L);
    tForm.setWoCode("1");
    tForm.setRelatedTroubleCodes("1");
    tForm.setIsMove(1L);
    tForm.setDateMove(DateTimeUtils.convertStringToDate("27/08/2020"));
    tForm.setUnitMove(1L);
    tForm.setUnitMoveName("1");
    tForm.setIsStationVip(1L);
    tForm.setIsTickHelp(1L);
    tForm.setNumHelp(1L);
    tForm.setAmiId(1L);
    tForm.setNumAon(1L);
    tForm.setNumGpon(1L);
    tForm.setNumNexttv(1L);
    tForm.setNumThc(1L);
    tForm.setInfoTicket("1");
    tForm.setRelatedCr("1");
    tForm.setNationCode("1");
    tForm.setDeferType(1L);
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("27/08/2020"));
    tForm.setLongitude("1");
    tForm.setLatitude("1");
    tForm.setGroupSolution(1L);
    tForm.setCellService("1");
    tForm.setIsSendTktu(1L);
    tForm.setConcave("1");
    tForm.setTroubleAssignId(1L);
    tForm.setIsChat(1L);
    tForm.setComplaintId(1L);
    tForm.setDowntime("1");
    tForm.setNumAffect("1");
    tForm.setSubMin("1");
    tForm.setWarnLevel(1L);
    tForm.setIsFailDevice(1L);
    tForm.setCountry("1");
    tForm.setCountReopen(1L);
    tForm.setRemainTime("-0.05");
    tForm.setCheckbox("9");
    tForm.setProcessingUnitId("1");
    tForm.setProcessingUnitName("1");
    tForm.setCreateUserId(1L);
    tForm.setProcessingUserName("1");
    tForm.setAttachFileList("1");
    //      </editor-fold>

    TroublesInSideDTO troubleOld = Mockito.spy(TroublesInSideDTO.class);
    troubleOld.setState(1L);
    troubleOld.setReceiveUnitId(2L);

    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setIsMove(1L);
    trouble.setInsertSource("BCCS");
    trouble.setWoCode("1");

    CatItemDTO stateDTOOld = Mockito.spy(CatItemDTO.class);
    stateDTOOld.setItemCode("CLEAR");

    CatItemDTO stateDTO = Mockito.spy(CatItemDTO.class);
    stateDTO.setItemCode("WAITING RECEIVE");

    List<CfgTimeExtendTtDTO> cfgTimeExtendTtDTO = Mockito.spy(ArrayList.class);
    CfgTimeExtendTtDTO cfgTimeExtendTt = Mockito.spy(CfgTimeExtendTtDTO.class);
    cfgTimeExtendTt.setTimeExtend("0.05");
    cfgTimeExtendTtDTO.add(cfgTimeExtendTt);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0909999999");

    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    usersEntity.setUsername("thanhlv12");

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("UNIT");
    unitToken.setUnitCode("UNIT");

    List<String> lstSequence = Mockito.spy(ArrayList.class);
    lstSequence.add("1");

    byte[] bFile = new byte[16];

    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(String.valueOf(tForm.getTroubleId()), null, null, null, null, null, null)
    ).thenReturn(troubleOld);
    PowerMockito.when(
        troublesRepository
            .findTroublesById(tForm.getTroubleId())
    ).thenReturn(trouble);
    PowerMockito.when(
        catItemBusiness.getCatItemById(troubleOld.getState())
    ).thenReturn(stateDTOOld);
    PowerMockito.when(
        catItemBusiness.getCatItemById(tForm.getState())
    ).thenReturn(stateDTO);
    PowerMockito.when(
        cfgTimeExtendTtRepository
            .getListCfgTimeExtendTtByCondition(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(cfgTimeExtendTtDTO);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userBusiness.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(troublesRepository.getSequenseTroubles(anyString(), anyInt()))
        .thenReturn(lstSequence);
    PowerMockito.when(DataUtil.splitListFileByComma(anyString())).thenReturn(lstSequence);
    PowerMockito.when(org.apache.commons.io.FileUtils.readFileToByteArray(any())).thenReturn(bFile);

    ResultInSideDto actual = troublesBusiness.onUpdateTrouble(tForm, false);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void insertTroubleFromOtherSystem() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    // case 1
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    List<String> listAccount = Mockito.spy(ArrayList.class);
    ResultDTO actual = troublesBusiness
        .insertTroubleFromOtherSystem(troublesInSideDTO, listAccount);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    // case 2
    CatItemDTO itemDTO = Mockito.spy(CatItemDTO.class);
    itemDTO.setItemId(1L);
    itemDTO.setItemName("1");
    itemDTO.setItemCode("1");
    PowerMockito.when(troublesRepository.getItemByCode(anyString(), anyString(), anyString()))
        .thenReturn(itemDTO);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    PowerMockito.when(troublesRepository.getUnitByCode(anyString(), anyBoolean())).thenReturn(unitDTO);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    PowerMockito.when(troublesRepository.getLocationByCode(anyString())).thenReturn(catLocationDTO);
    PowerMockito.when(troublesRepository.getPriorityTrouble(any())).thenReturn(itemDTO);
    List<String> lstString = Mockito.spy(ArrayList.class);
    lstString.add("1");
    PowerMockito.when(troublesRepository.getSequenseTroubles(anyString(), any()))
        .thenReturn(lstString);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    PowerMockito.when(troublesRepository.insertTroubles(any())).thenReturn(resultInSideDto);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUnitId(1L);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(usersInsideDto);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);

    troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleName("1");
    troublesInSideDTO.setDescription("1");
    troublesInSideDTO.setTypeCode("1");
    troublesInSideDTO.setAlarmGroupCode("1");
    troublesInSideDTO.setSubCategoryCode("1");
    troublesInSideDTO.setCountryCode("1");
    troublesInSideDTO.setPriorityCode("1");
    troublesInSideDTO.setReceiveUnitCode("1");
    troublesInSideDTO.setImpactCode("1");
    troublesInSideDTO.setLocationCode("1");
    troublesInSideDTO.setMessage(null);
    troublesInSideDTO.setStateName("QUEUE");
    troublesInSideDTO.setCreateUserName("1");
    actual = troublesBusiness.insertTroubleFromOtherSystem(troublesInSideDTO, listAccount);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

}
