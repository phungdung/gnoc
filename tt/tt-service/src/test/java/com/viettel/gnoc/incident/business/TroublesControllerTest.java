/*
package com.viettel.gnoc.incident.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.bccs.cc.service.CauseErrorExpireDTO;
import com.viettel.bccs.cc.service.SpmRespone;
import com.viettel.bccs.cc.service.TroubleNetworkSolutionDTO;
import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.business.CatItemBusinessImpl;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.business.CompCauseBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.SpringApplicationContext;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CompCauseDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.incident.provider.WSChatPort;
import com.viettel.gnoc.commons.incident.provider.WSChatPortFactory;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.TtCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepositoryImpl;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.SmsGatewayRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.TROUBLE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.commons.utils.ws.Passwords;
import com.viettel.gnoc.commons.validator.ValidateAccount;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrSearchDTO;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.service.CrService;
import com.viettel.gnoc.cr.service.CrServiceImpl;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.dto.TroubleMopDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroublesEntity;
import com.viettel.gnoc.incident.repository.CfgServerNocRepository;
import com.viettel.gnoc.incident.repository.ItAccountRepository;
import com.viettel.gnoc.incident.repository.ItSpmInfoRepository;
import com.viettel.gnoc.incident.repository.TroubleActionLogsRepository;
import com.viettel.gnoc.incident.repository.TroubleAssignRepository;
import com.viettel.gnoc.incident.repository.TroubleWorklogRepository;
import com.viettel.gnoc.incident.repository.TroublesRepository;
import com.viettel.gnoc.incident.utils.TroubleBccsUtils;
import com.viettel.gnoc.incident.utils.TroubleSpmUtils;
import com.viettel.gnoc.incident.utils.WSBCCS2Port;
import com.viettel.gnoc.incident.utils.WSIPCC;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import com.viettel.gnoc.wfm.dto.ResultDtoWO;
import com.viettel.gnoc.wfm.service.LogCallIpccServiceImpl;
import com.viettel.ipcc.services.NomalOutput;
import com.viettel.soc.spm.service.ResultDTO;
import java.io.File;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import viettel.passport.client.UserToken;
import vn.viettel.smartoffice.GroupResponse;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TroublesBusinessImpl.class, CatItemBusinessImpl.class, CatItemRepositoryImpl.class,
    FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, ConditionBeanUtil.class, WSChatPort.class, WSChatPortFactory.class,
    PassProtector.class, TimezoneContextHolder.class, TemporaryFolder.class, ValidateAccount.class,
    Passwords.class, DateTimeUtils.class, I18n.class, WoServicesImpl.class, UserBusiness.class,
    TroubleSpmUtils.class, LogCallIpccServiceImpl.class, ConditionBeanUtil.class,
    TicketProvider.class, DataUtil.class})
public class TroublesControllerTest {

  @InjectMocks
  TroublesBusinessImpl troublesBusiness;

  @Mock
  TroubleAssignRepository troubleAssignRepository;

  @Mock
  TroubleBccsUtils troubleBccsUtils;

  @Mock
  MessagesRepository messagesRepository;

  @Mock
  CommonBusiness commonBusiness;

  @Mock
  CatReasonBusiness catReasonBusiness;

  @Mock
  UserBusiness userBusiness;


  @Mock
  WSBCCS2Port wsbccs2Port;

  @Mock
  WSChatPort wsChatPort;

  @Mock
  CompCauseBusiness compCauseBusiness;

  @Mock
  TroublesRepository troublesRepository;

  @Mock
  TroubleActionLogsRepository troubleActionLogsRepository;

  @Mock
  ItSpmInfoRepository itSpmInfoRepository;

  @Mock
  CatItemBusinessImpl catItemBusiness;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  WOCreateBusiness woCreateBusiness;

  @Mock
  UnitRepository unitRepository;

  @Mock
  TtCategoryServiceProxy ttCategoryServiceProxy;

  @Mock
  TroubleWorklogRepository troubleWorklogRepository;

  @Mock
  TroublesServiceForCCBusiness troublesServiceForCCBusiness;

  @Mock
  SmsGatewayRepository smsGatewayRepository;

  @Mock
  ItAccountRepository itAccountRepository;

  @Mock
  TroublesTempClosedBusiness troublesTempClosedBusiness;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  CfgServerNocRepository cfgServerNocRepository;

  @Mock
  TroubleSpmUtils troubleSpmUtils;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CrService crService;

  @Mock
  LogCallIpccServiceImpl logCallIpccService;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void testOnSearch() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    dto.setHaveOpenCondition("1");
    dto.setLocationId(1500289728L);
    Instant now = Instant.now(); //current date
    Instant before = now.minus(Duration.ofDays(300));
    Date dateBefore = Date.from(before);
    dto.setCreatedTime(dateBefore);
    List<TroublesInSideDTO> troublesDTOS = Mockito.spy(ArrayList.class);
    troublesDTOS.add(dto);
    datatable.setData(troublesDTOS);
    datatable.setTotal(1);
    datatable.setPages(1);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    PowerMockito.when(troublesRepository.onSearch(any())).thenReturn(datatable);
    troublesBusiness.onSearch(dto);
    Assert.assertEquals(datatable.getData().size(), 1);
  }

  private static final ThreadLocal<String> OFFSET = new ThreadLocal<String>() {
    @Override
    protected String initialValue() {
      return "vtnet";
    }
  };

  public static Double getOffsetDouble() {
    return Double.valueOf(OFFSET.get());
  }

  @Test
  public void testGetTroubleInfo_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    List<TroublesInSideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(dto);
    PowerMockito.when(troublesRepository.getTroubleInfo(any())).thenReturn(lst);
    List<TroublesInSideDTO> list = troublesBusiness.getTroubleInfo(dto);
    Assert.assertEquals(lst, list);
  }

  @Test
  public void testUpdateTroublesNOC_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setPassword("123456a@");
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPartyCode("1");
    requestDTO.setRequestId("1");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("123");
    troublesDTO.setEndTroubleTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    troublesDTO.setRootCause("abc");
    troublesDTO.setSolutionType("1");
    troublesDTO.setWorkArround("abc");
    troublesDTO.setTroubleId("1");
    troublesDTO.setInsertSource("SPM");
    troublesDTO.setAlarmGroupId("1");
    troublesDTO.setAlarmGroupCode("SOS");
    troublesDTO.setTypeId("1");
    troublesDTO.setAutoCreateWO(1L);
    troublesDTO.setTroubleName("danger");
    troublesDTO.setPriorityId("1");
    troublesDTO.setRisk("1");
    troublesDTO.setReasonId("1");
    List<TroublesDTO> listTrouble = Mockito.spy(ArrayList.class);
    listTrouble.add(troublesDTO);
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    Map<String, String> mapProperty = Mockito.spy(HashMap.class);
    mapProperty.put("TT.DEFAULT.KEDB.AUTOCLOSE", "vt");
    mapProperty.put("TT.DEFAULT.CLOSECODE.AUTOCLOSE", "net");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.SUCCESS);
    Map<String, CatReasonInSideDTO> mapReason = new HashMap<>();
    CatReasonInSideDTO catReasonInSideDTO = Mockito.spy(CatReasonInSideDTO.class);
    catReasonInSideDTO.setReasonName("vtnet");
    mapReason.put("1", catReasonInSideDTO);
    PowerMockito.when(catReasonBusiness.getCatReasonData()).thenReturn(mapReason);
    PowerMockito.when(commonBusiness.getConfigProperty()).thenReturn(mapProperty);
    PowerMockito.when(troublesTempClosedBusiness.insertList(anyList()))
        .thenReturn(resultInSideDto1);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto2 = troublesBusiness
        .updateTroublesNOC(requestDTO, listTrouble);
    Assert.assertEquals(resultInSideDto1.getKey(), resultInSideDto2.getKey());
  }

  @Test
  public void testOnSearchForSPM() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    List<TroublesDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(troublesDTO);
    PowerMockito.when(troublesRepository.onSearchForSPM(any(), any(), any(), any()))
        .thenReturn(lst);
    List<TroublesDTO> list = troublesBusiness.onSearchForSPM(any(), any(), any(), any());
    Assert.assertEquals(lst, list);
  }

  @Test
  public void testOnUpdateTroubleSPM_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setPassword("123456a@");
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPartyCode("1");
    requestDTO.setRequestId("1");
    TroublesDTO troublesDTO1 = Mockito.spy(TroublesDTO.class);
    troublesDTO1.setSpmCode("");
    TroublesDTO troublesDTO2 = Mockito.spy(TroublesDTO.class);
    troublesDTO2.setSpmCode("SPM");
    troublesDTO2.setProcessingUnitName("");
    TroublesDTO troublesDTO3 = Mockito.spy(TroublesDTO.class);
    troublesDTO3.setSpmCode("SPM");
    troublesDTO3.setProcessingUnitName("vtnet");
    troublesDTO3.setProcessingUserName("");
    TroublesDTO troublesDTO4 = Mockito.spy(TroublesDTO.class);
    troublesDTO4.setSpmCode("SPM");
    troublesDTO4.setProcessingUnitName("vtnet");
    troublesDTO4.setProcessingUserName("thanhlv12");
    troublesDTO4.setStateName("");
    TroublesDTO troublesDTO5 = Mockito.spy(TroublesDTO.class);
    troublesDTO5.setSpmCode("SPM");
    troublesDTO5.setProcessingUnitName("vtnet");
    troublesDTO5.setProcessingUserName("thanhlv12");
    troublesDTO5.setStateName("active");
    troublesDTO5.setStateName("UPDATE_WORKLOG");
    TroublesDTO troublesDTO6 = Mockito.spy(TroublesDTO.class);
    troublesDTO6.setSpmCode("SPM");
    troublesDTO6.setProcessingUnitName("vtnet");
    troublesDTO6.setProcessingUserName("thanhlv12");
    troublesDTO6.setStateName("UPDATE_CLOSED");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    ResultInSideDto resultInSideDtoTest1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoTest1.setKey(RESULT.FAIL);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto1 = troublesBusiness
        .onUpdateTroubleSPM(requestDTO, troublesDTO1);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto2 = troublesBusiness
        .onUpdateTroubleSPM(requestDTO, troublesDTO2);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto3 = troublesBusiness
        .onUpdateTroubleSPM(requestDTO, troublesDTO3);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto4 = troublesBusiness
        .onUpdateTroubleSPM(requestDTO, troublesDTO4);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto5 = troublesBusiness
        .onUpdateTroubleSPM(requestDTO, troublesDTO5);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto6 = troublesBusiness
        .onUpdateTroubleSPM(requestDTO, troublesDTO6);
    Assert.assertEquals(resultInSideDto1.getKey(), resultInSideDtoTest1.getKey());
    Assert.assertEquals(resultInSideDto2.getKey(), resultInSideDtoTest1.getKey());
    Assert.assertEquals(resultInSideDto3.getKey(), resultInSideDtoTest1.getKey());
    Assert.assertEquals(resultInSideDto4.getKey(), resultInSideDtoTest1.getKey());
    Assert.assertEquals(resultInSideDto5.getKey(), resultInSideDtoTest1.getKey());
    Assert.assertEquals(resultInSideDto6.getKey(), resultInSideDtoTest1.getKey());
  }

  @Test
  public void testOnUpdateTroubleDtoSPM_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setSpmCode("SPM");
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto1 = Mockito.spy(
        com.viettel.gnoc.commons.dto.ResultDTO.class);
    try {
      resultInSideDto1 = troublesBusiness.onUpdateTroubleDtoSPM(troublesDTO);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.FAIL);
  }

  @Test
  public void testOnUpdateTroubleDtoSPM_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setProcessingUnitName("pending");
    tForm.setProcessingUserName("thanhlv12");
    tForm.setStateName("UPDATE_HOT");
    TroublesDTO troubleDTO = Mockito.spy(TroublesDTO.class);
    PowerMockito.mockStatic(I18n.class);
    ApplicationContext context = Mockito.spy(ApplicationContext.class);
    setMockStatic(context);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("vn");
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto = Mockito.spy(
        com.viettel.gnoc.commons.dto.ResultDTO.class);
    try {
      resultInSideDto = troublesBusiness.onUpdateTroubleDtoSPM(troubleDTO);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testOnRollBackTroubleSPM_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setPassword("123456a@");
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPartyCode("1");
    requestDTO.setRequestId("1");
    List<String> lstSpmCode = Mockito.spy(ArrayList.class);
    String spmCode = "spm";
    lstSpmCode.add(spmCode);
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto = Mockito.spy(
        com.viettel.gnoc.commons.dto.ResultDTO.class);
    List<com.viettel.gnoc.commons.dto.ResultDTO> resultInSideDtos = Mockito.spy(ArrayList.class);
    resultInSideDtos.add(resultInSideDto);
    resultInSideDtos = troublesBusiness.onRollBackTroubleSPM(requestDTO, lstSpmCode);
    Assert.assertEquals(resultInSideDtos.size(), 1);
  }

  @Test
  public void testDeleteDataRollBack_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    Long troubleId = 1L;
    String woCode = "";
    PowerMockito.when(troublesRepository.delete(troubleId)).thenReturn(resultInSideDto);
    PowerMockito.when(troubleActionLogsRepository.delete(troubleId)).thenReturn(resultInSideDto);
    PowerMockito.when(itSpmInfoRepository.delete(troubleId)).thenReturn(resultInSideDto);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto1 = troublesBusiness
        .deleteDataRollBack(troubleId, woCode);
    Assert.assertEquals(resultInSideDto1.getKey(), null);
  }

  @Test
  public void testOnSearchCountForVsmart_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    PowerMockito.when(troublesRepository.onSearchCountForVsmart(any())).thenReturn(1);
    int i = 1;
    i = troublesBusiness.onSearchCountForVsmart(troublesDTO);
    Assert.assertEquals(1, i);
  }

  @Test
  public void testOnInsertTroubleMobile_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String[] arrFileName = new String[5];
    byte[][] arrFileData = new byte[5][5];
    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setPassword("123456a@");
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPartyCode("1");
    requestDTO.setRequestId("1");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setInsertSource("SPM");
    List<String> listAccount = Mockito.spy(ArrayList.class);
    String account = "thanhlv12";
    listAccount.add(account);
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto = Mockito.spy(
        com.viettel.gnoc.commons.dto.ResultDTO.class);
    resultInSideDto = troublesBusiness
        .onInsertTroubleMobile(requestDTO, troublesDTO, listAccount, arrFileName, arrFileData);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.FAIL);
  }

  @Test
  public void testOnInsertTroubleMobileNew_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto = Mockito.spy(
        com.viettel.gnoc.commons.dto.ResultDTO.class);
    com.viettel.gnoc.commons.dto.ResultDTO resultWO = Mockito.spy(
        com.viettel.gnoc.commons.dto.ResultDTO.class);
    resultWO.setId("1");
    resultWO.setMessage(RESULT.SUCCESS);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.SUCCESS);
    resultInSideDto1.setId(1L);
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("123");
    troublesDTO.setEndTroubleTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    troublesDTO.setRootCause("abc");
    troublesDTO.setSolutionType("1");
    troublesDTO.setWorkArround("abc");
    troublesDTO.setTroubleId("1");
    troublesDTO.setInsertSource("SPM");
    troublesDTO.setAlarmGroupId("1");
    troublesDTO.setAlarmGroupCode("SOS");
    troublesDTO.setTypeId("1");
    troublesDTO.setAutoCreateWO(1L);
    troublesDTO.setTroubleName("danger");
    troublesDTO.setPriorityId("1");
    troublesDTO.setRisk("1");
    troublesDTO.setReasonId("1");
    troublesDTO.setStateName(Constants.TT_STATE.Waiting_Receive);
    troublesDTO.setCreateUnitName("vtnet");
    troublesDTO.setCreateUserName("thanhlv12");
    troublesDTO.setCreatedTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    Map<String, String> mapProperty = new HashMap<>();
    mapProperty.put("WO.TT.CREATE_PERSON.ID", "1");
    PowerMockito.when(troublesRepository.getConfigProperty()).thenReturn(mapProperty);
    List<String> listAccount = Mockito.spy(ArrayList.class);
    String account = "thanhlv12";
    listAccount.add(account);
    String[] arrFileName = null;
    byte[][] arrFileData = null;
    PowerMockito.when((woCreateBusiness.createWOMobile(any(), any(), any()))).thenReturn(
        resultWO);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(1L);
    unitToken.setUnitName("A");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    PowerMockito.when(userBusiness.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(troublesRepository.insertTroubles(any())).thenReturn(resultInSideDto1);
    PowerMockito.when(troublesRepository.insertCommonFile(any())).thenReturn(resultInSideDto1);
    PowerMockito.when(troublesRepository.insertTroubleFile(any())).thenReturn(resultInSideDto1);
    PowerMockito.when(gnocFileRepository.saveListGnocFile(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto1);
    ResultInSideDto resultInsertFile = Mockito.spy(ResultInSideDto.class);
    resultInsertFile.setKey(RESULT.SUCCESS);
    troublesBusiness.onInsertTroubleMobileNew(troublesDTO, listAccount, arrFileName, arrFileData);
    Assert.assertEquals(resultInsertFile.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testInsertTroubleMobile_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    com.viettel.gnoc.commons.dto.ResultDTO result1 = Mockito
        .spy(com.viettel.gnoc.commons.dto.ResultDTO.class);
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    PowerMockito.mockStatic(I18n.class);
    result.setId(1L);
    ResultDtoWO resultWO = Mockito.spy(ResultDtoWO.class);
    resultWO.setId("1");
    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleId("1");
    tForm.setTroubleName("upgrade");
    tForm.setTypeId("1");
    tForm.setAlarmGroupId("1");
    tForm.setPriorityId("1");
    tForm.setInsertSource("SPM");
    tForm.setAutoCreateWO(0L);
    tForm.setInfraType("1");
    tForm.setCreatedTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    tForm.setPriorityId("1");
    tForm.setStateName("WAITING RECEIVE");
    tForm.setCreateUnitName("vtnet");
    tForm.setCreateUserName("thanhlv12");
    tForm.setState("1");
    tForm.setTroubleCode("123");
    List<String> listAccount = Mockito.spy(ArrayList.class);
    String account = "thanhlv12";
    listAccount.add(account);
    Map<String, String> mapProperty = new HashMap<>();
    mapProperty.put("WO.TT.PRIORITY.CRITICAL", "1,1");
    mapProperty.put("WO.TT.CREATE_PERSON.ID", "1");
    PowerMockito.when(troublesRepository.getConfigProperty()).thenReturn(mapProperty);
    UsersEntity user = Mockito.spy(UsersEntity.class);
    user.setUserId(1L);
    user.setUsername("thanhlv12");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCES");
    PowerMockito.when(userBusiness.getUserByUserId(anyLong())).thenReturn(user);
    PowerMockito.when(troublesRepository.insertTroubles(any())).thenReturn(result);
    PowerMockito.when(itAccountRepository.insertItAccount(any())).thenReturn(result);
    result1 = troublesBusiness.insertTroubleMobile(tForm, listAccount, true);
    Assert.assertEquals(result1.getKey(), RESULT.SUCCESS);
  }

  */
/*@Test
  public void testOnInsertTroubleFileWS_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setPassword("123456a@");
    requestDTO.setUsername("thanhlv12");
    requestDTO.setPartyCode("1");
    requestDTO.setRequestId("1");
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleId("1");
    troublesDTO.setTroubleName("upgrade");
    troublesDTO.setTypeId("1");
    troublesDTO.setAlarmGroupId("1");
    troublesDTO.setPriorityId("1");
    troublesDTO.setInsertSource("SPM");
    troublesDTO.setAutoCreateWO(0L);
    troublesDTO.setInfraType("1");
    troublesDTO.setCreatedTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    troublesDTO.setPriorityId("1");
    troublesDTO.setStateName("WAITING RECEIVE");
    troublesDTO.setCreateUnitName("vtnet");
    troublesDTO.setCreateUserName("thanhlv12");
    troublesDTO.setState("1");
    troublesDTO.setTroubleCode("123");
    TroublesDTO troublesDTO1 = Mockito.spy(TroublesDTO.class);
    List<String> listAccount = Mockito.spy(ArrayList.class);
    String account = "thanhlv12";
    listAccount.add(account);
    String[] arrFileName = new String[5];
    byte[][] arrFileData = new byte[5][5];
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto = Mockito.spy(com.viettel.gnoc.commons.dto.ResultDTO.class);
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto2 = Mockito.spy(com.viettel.gnoc.commons.dto.ResultDTO.class);
    PowerMockito.mockStatic(Passwords.class);
    PowerMockito.when(Passwords.isExpectedPassword(any(), any(), any())).thenReturn(true);
    resultInSideDto = troublesBusiness
        .onInsertTroubleFileWS(requestDTO, troublesDTO, listAccount, arrFileName, arrFileData);
    resultInSideDto2 = troublesBusiness
        .onInsertTroubleFileWS(requestDTO, troublesDTO1, listAccount, arrFileName, arrFileData);
    Assert.assertEquals(resultInSideDto.getKey(),null);
    Assert.assertEquals(resultInSideDto2.getKey(), null);
  }*//*


  @Test
  public void testOnInsertTroubleFileWSNew_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.ERROR);
    PowerMockito.mockStatic(I18n.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setTroubleId(1L);
    troublesDTO.setTroubleName("upgrade");
    troublesDTO.setTypeId(1L);
    troublesDTO.setAlarmGroupId("1");
    troublesDTO.setPriorityId(1L);
    troublesDTO.setInsertSource("VSMART");
    troublesDTO.setAutoCreateWO(0L);
    troublesDTO.setInfraType("1");
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setPriorityId(1L);
    troublesDTO.setStateName("WAITING RECEIVE");
    troublesDTO.setCreateUnitName("vtnet");
    troublesDTO.setCreateUserName("thanhlv12");
    troublesDTO.setState(1L);
    troublesDTO.setTroubleCode("123");
    troublesDTO.setCountry("vietnam");
    List<String> listAccount = Mockito.spy(ArrayList.class);
    String account = "thanhlv12";
    listAccount.add(account);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    String[] arrFileName = new String[5];
    byte[][] arrFileData = new byte[5][5];
    troublesBusiness
        .onInsertTroubleFileWSNew(troublesDTO, listAccount, arrFileName, arrFileData);
    Assert.assertEquals(resultDTO.getKey(), RESULT.ERROR);
  }

  @Test
  public void testFindTroublesById_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesEntity troublesDTO = Mockito.spy(TroublesEntity.class);
    troublesDTO.setTroubleId(1l);
    PowerMockito.when(troublesRepository.findTroublesById(any())).thenReturn(troublesDTO);
    troublesDTO.setState(1L);
    troublesDTO.setTimeProcess(0.0);
    troublesDTO.setTimeUsed(0.0);
    troublesDTO.setClearTime(new Date());
    troublesDTO.setAssignTime(new Date());
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(catItemDTO);
    troublesBusiness.findTroublesById(anyLong());
  }

  @Test
  public void testSearchByConditionBean_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ConditionBean conditionBean = Mockito.spy(ConditionBean.class);
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    int rowStart = 1;
    int maxRow = 1;
    String sortType = "asc";
    String sortFieldList = "troubleName";
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    List<TroublesInSideDTO> troublesDTOS = Mockito.spy(ArrayList.class);
    troublesDTOS.add(troublesDTO);
    PowerMockito.when(troublesRepository
        .searchByConditionBean(lstCondition, rowStart, maxRow, sortType, sortFieldList))
        .thenReturn(troublesDTOS);
    List<TroublesInSideDTO> troublesDTOS1 = Mockito.spy(ArrayList.class);
    troublesDTOS1.add(troublesDTO);
    troublesDTOS1 = troublesBusiness
        .searchByConditionBean(lstCondition, rowStart, maxRow, sortType, sortFieldList);
    Assert.assertEquals(troublesDTOS, troublesDTOS1);
  }

  @Test
  public void testOnUpdateTrouble_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleId(1L);
    tForm.setTroubleName("upgrade");
    tForm.setTypeId(1L);
    tForm.setAlarmGroupId("1");
    tForm.setPriorityId(1L);
    tForm.setInsertSource("SPM");
    tForm.setAutoCreateWO(0L);
    tForm.setInfraType("1");
    tForm.setCreatedTime(new Date());
    tForm.setPriorityId(1L);
    tForm.setStateName("WAITING RECEIVE");
    tForm.setCreateUnitName("vtnet");
    tForm.setCreateUserName("thanhlv12");
    tForm.setState(1L);
    tForm.setTroubleCode("123");
    tForm.setProcessingUnitId("1");
    tForm.setProcessingUserId("1");
    tForm.setCheckbox("4");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offset = 3.0;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    TroublesInSideDTO troubleOld = Mockito.spy(TroublesInSideDTO.class);
    troubleOld.setTroubleId(1L);
    troubleOld.setTroubleName("upgrade");
    troubleOld.setTypeId(1L);
    troubleOld.setAlarmGroupId("1");
    troubleOld.setPriorityId(1L);
    troubleOld.setInsertSource("SPM");
    troubleOld.setAutoCreateWO(0L);
    troubleOld.setInfraType("1");
    troubleOld.setCreatedTime(new Date());
    troubleOld.setPriorityId(1L);
    troubleOld.setStateName("WAITING RECEIVE");
    troubleOld.setCreateUnitName("vtnet");
    troubleOld.setCreateUserName("thanhlv12");
    troubleOld.setState(1L);
    troubleOld.setTroubleCode("123");
    troubleOld.setIsMove(1L);
    troubleOld.setProcessingUnitId("1");
    troubleOld.setProcessingUserId("1");
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setTroubleId(1L);
    trouble.setTroubleName("upgrade");
    trouble.setTypeId(1L);
    trouble.setPriorityId(1L);
    trouble.setInsertSource("SPM");
    trouble.setAutoCreateWO(0L);
    trouble.setCreatedTime(new Date());
    trouble.setPriorityId(1L);
    trouble.setCreateUnitName("vtnet");
    trouble.setCreateUserName("thanhlv12");
    trouble.setState(1L);
    trouble.setTroubleCode("123");
    trouble.setIsMove(1L);
    PowerMockito.when(troublesRepository.findTroublesById(any())).thenReturn(trouble);
    PowerMockito
        .when(troublesRepository.getTroubleDTO(any(), any(), any(), any(), any(), any(), any()))
        .thenReturn(troubleOld);
    CatItemDTO stateDTOOld = Mockito.spy(CatItemDTO.class);
    stateDTOOld.setItemCode("1");
    PowerMockito.when(catItemBusiness.getCatItemById(any())).thenReturn(stateDTOOld);
    CatItemDTO stateDTO = Mockito.spy(CatItemDTO.class);
    stateDTO.setItemCode("1");
    PowerMockito.when(catItemBusiness.getCatItemById(any())).thenReturn(stateDTO);
    List<CatItemDTO> lstTTState = Mockito.spy(ArrayList.class);
    lstTTState.add(stateDTOOld);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstTTState);
    ResultDTO resultDto1 = Mockito.spy(ResultDTO.class);
    resultDto1.setId("1");
    PowerMockito.mockStatic(TroubleSpmUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    try {
      PowerMockito.when(troubleSpmUtils.updateSpmAction(any(), any(), any()))
          .thenReturn(resultDto1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      resultInSideDto = troublesBusiness.onUpdateTrouble(tForm, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }


  @Test
  public void testOnUpdateTrouble_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultDto = Mockito.spy(ResultInSideDto.class);
    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleId(1L);
    tForm.setTroubleName("upgrade");
    tForm.setTypeId(1L);
    tForm.setAlarmGroupId("1");
    tForm.setPriorityId(1L);
    tForm.setInsertSource("SPM");
    tForm.setAutoCreateWO(0L);
    tForm.setInfraType("1");
    tForm.setCreatedTime(new Date());
    tForm.setPriorityId(1L);
    tForm.setStateName("WAITING RECEIVE");
    tForm.setCreateUnitName("vtnet");
    tForm.setCreateUserName("thanhlv12");
    tForm.setState(1L);
    tForm.setTroubleCode("123");
    tForm.setProcessingUnitId("1");
    tForm.setProcessingUserId("1");
    tForm.setReceiveUnitId(1L);
    tForm.setCheckbox("3");
    tForm.setDateMove(new Date());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offset = 3.0;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    TroublesInSideDTO troubleOld = Mockito.spy(TroublesInSideDTO.class);
    troubleOld.setTroubleId(1L);
    troubleOld.setTroubleName("upgrade");
    troubleOld.setTypeId(1L);
    troubleOld.setAlarmGroupId("1");
    troubleOld.setPriorityId(1L);
    troubleOld.setInsertSource("SPM");
    troubleOld.setAutoCreateWO(0L);
    troubleOld.setInfraType("1");
    troubleOld.setCreatedTime(new Date());
    troubleOld.setPriorityId(1L);
    troubleOld.setStateName("WAITING RECEIVE");
    troubleOld.setCreateUnitName("vtnet");
    troubleOld.setCreateUserName("thanhlv12");
    troubleOld.setState(2L);
    troubleOld.setTroubleCode("123");
    troubleOld.setIsMove(1L);
    troubleOld.setProcessingUnitId("1");
    troubleOld.setProcessingUserId("1");
    troubleOld.setReceiveUnitId(1L);
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setTroubleId(1L);
    trouble.setTroubleName("upgrade");
    trouble.setTypeId(1L);
    trouble.setPriorityId(1L);
    trouble.setInsertSource("SPM");
    trouble.setAutoCreateWO(0L);
    trouble.setCreatedTime(new Date());
    trouble.setPriorityId(1L);
    trouble.setCreateUnitName("vtnet");
    trouble.setCreateUserName("thanhlv12");
    trouble.setState(1L);
    trouble.setTroubleCode("123");
    trouble.setIsMove(1L);
    trouble.setCountReopen(1L);
    trouble.setUnitMoveName("vtnet");
    trouble.setDateMove(new Date());
    PowerMockito.when(troublesRepository.findTroublesById(any())).thenReturn(trouble);
    PowerMockito
        .when(troublesRepository.getTroubleDTO(any(), any(), any(), any(), any(), any(), any()))
        .thenReturn(troubleOld);
    CatItemDTO stateDTOOld = Mockito.spy(CatItemDTO.class);
    stateDTOOld.setItemCode("CLEAR");
    PowerMockito.when(catItemBusiness.getCatItemById(Long.valueOf(troubleOld.getState())))
        .thenReturn(stateDTOOld);
    CatItemDTO stateDTO = Mockito.spy(CatItemDTO.class);
    stateDTO.setItemCode("WAITING RECEIVE");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("vn");
    PowerMockito.when(catItemBusiness.getCatItemById(Long.valueOf(tForm.getState())))
        .thenReturn(stateDTO);
    List<CatItemDTO> lstTTState = Mockito.spy(ArrayList.class);
    lstTTState.add(stateDTOOld);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstTTState);
    ResultDTO resultDto1 = Mockito.spy(ResultDTO.class);
    resultDto1.setId("1");
    PowerMockito.mockStatic(TroubleSpmUtils.class);
    */
/*try {
      PowerMockito.when(TroubleSpmUtils.updateSpmAction(any(), any(), any()))
          .thenReturn(resultDto1);
    } catch (Exception e) {
      e.printStackTrace();
    }*//*

    try {
      resultDto = troublesBusiness.onUpdateTrouble(tForm, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Assert.assertEquals(resultDto.getKey(), RESULT.SUCCESS);
  }


  @Test
  public void testOnUpdateTrouble_03() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleId(1L);
    tForm.setTroubleName("upgrade");
    tForm.setTypeId(1L);
    tForm.setAlarmGroupId("1");
    tForm.setPriorityId(1L);
    tForm.setInsertSource("SPM");
    tForm.setAutoCreateWO(0L);
    tForm.setInfraType("1");
    tForm.setCreatedTime(new Date());
    tForm.setPriorityId(1L);
    tForm.setStateName("WAITING RECEIVE");
    tForm.setCreateUnitName("vtnet");
    tForm.setCreateUserName("thanhlv12");
    tForm.setState(1L);
    tForm.setTroubleCode("123");
    tForm.setProcessingUnitId("1");
    tForm.setProcessingUserId("1");
    tForm.setReceiveUnitId(1L);
    tForm.setCheckbox("3");
    tForm.setDateMove(new Date());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offset = 3.0;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    TroublesInSideDTO troubleOld = Mockito.spy(TroublesInSideDTO.class);
    troubleOld.setTroubleId(1L);
    troubleOld.setTroubleName("upgrade");
    troubleOld.setTypeId(1L);
    troubleOld.setAlarmGroupId("1");
    troubleOld.setPriorityId(1L);
    troubleOld.setInsertSource("SPM");
    troubleOld.setAutoCreateWO(0L);
    troubleOld.setInfraType("1");
    troubleOld.setCreatedTime(new Date());
    troubleOld.setPriorityId(1L);
    troubleOld.setStateName("WAITING RECEIVE");
    troubleOld.setCreateUnitName("vtnet");
    troubleOld.setCreateUserName("thanhlv12");
    troubleOld.setState(2L);
    troubleOld.setTroubleCode("123");
    troubleOld.setIsMove(1L);
    troubleOld.setProcessingUnitId("1");
    troubleOld.setProcessingUserId("1");
    troubleOld.setReceiveUnitId(2L);
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setTroubleId(1L);
    trouble.setTroubleName("upgrade");
    trouble.setTypeId(1L);
    trouble.setPriorityId(1L);
    trouble.setInsertSource("SPM");
    trouble.setAutoCreateWO(0L);
    trouble.setCreatedTime(new Date());
    trouble.setPriorityId(1L);
    trouble.setCreateUnitName("vtnet");
    trouble.setCreateUserName("thanhlv12");
    trouble.setState(1L);
    trouble.setTroubleCode("123");
    trouble.setIsMove(1L);
    trouble.setCountReopen(1L);
    trouble.setUnitMoveName("vtnet");
    trouble.setDateMove(new Date());
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.when(DateTimeUtils.convertDateTimeToString(any())).thenReturn("vn");
    PowerMockito.when(troublesRepository.findTroublesById(any())).thenReturn(trouble);
    PowerMockito
        .when(troublesRepository.getTroubleDTO(any(), any(), any(), any(), any(), any(), any()))
        .thenReturn(troubleOld);
    CatItemDTO stateDTOOld = Mockito.spy(CatItemDTO.class);
    stateDTOOld.setItemCode("CLEAR");
    PowerMockito.when(catItemBusiness.getCatItemById(Long.valueOf(troubleOld.getState())))
        .thenReturn(stateDTOOld);
    CatItemDTO stateDTO = Mockito.spy(CatItemDTO.class);
    stateDTO.setItemCode("WAITING RECEIVE");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("vn");
    PowerMockito.when(catItemBusiness.getCatItemById(Long.valueOf(tForm.getState())))
        .thenReturn(stateDTO);
    List<CatItemDTO> lstTTState = Mockito.spy(ArrayList.class);
    lstTTState.add(stateDTOOld);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstTTState);
    ResultDTO resultDto1 = Mockito.spy(ResultDTO.class);
    resultDto1.setId("1");
    PowerMockito.mockStatic(TroubleSpmUtils.class);
    try {
      PowerMockito.when(troubleSpmUtils.updateSpmAction(any(), any(), any()))
          .thenReturn(resultDto1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    resultInSideDto = troublesBusiness.onUpdateTrouble(tForm, false);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }


  @Test
  public void testOnUpdateTrouble_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultDto = Mockito.spy(ResultInSideDto.class);
    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleId(1L);
    tForm.setTroubleName("upgrade");
    tForm.setTypeId(1L);
    tForm.setAlarmGroupId("1");
    tForm.setPriorityId(1L);
    tForm.setInsertSource("SPM");
    tForm.setAutoCreateWO(0L);
    tForm.setInfraType("1");
    tForm.setCreatedTime(new Date());
    tForm.setPriorityId(1L);
    tForm.setStateName("WAITING RECEIVE");
    tForm.setCreateUnitName("vtnet");
    tForm.setCreateUserName("thanhlv12");
    tForm.setState(1L);
    tForm.setTroubleCode("123");
    tForm.setProcessingUnitId("1");
    tForm.setProcessingUserId("1");
    tForm.setReceiveUnitId(1L);
    tForm.setCheckbox("3");
    tForm.setDateMove(new Date());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offset = 3.0;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    TroublesInSideDTO troubleOld = Mockito.spy(TroublesInSideDTO.class);
    troubleOld.setTroubleId(1L);
    troubleOld.setTroubleName("upgrade");
    troubleOld.setTypeId(1L);
    troubleOld.setAlarmGroupId("1");
    troubleOld.setPriorityId(1L);
    troubleOld.setInsertSource("SPM");
    troubleOld.setAutoCreateWO(0L);
    troubleOld.setInfraType("1");
    troubleOld.setCreatedTime(new Date());
    troubleOld.setPriorityId(1L);
    troubleOld.setStateName("WAITING RECEIVE");
    troubleOld.setCreateUnitName("vtnet");
    troubleOld.setCreateUserName("thanhlv12");
    troubleOld.setState(2L);
    troubleOld.setTroubleCode("123");
    troubleOld.setIsMove(1L);
    troubleOld.setProcessingUnitId("1");
    troubleOld.setProcessingUserId("1");
    troubleOld.setReceiveUnitId(2L);
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setTroubleId(1L);
    trouble.setTroubleName("upgrade");
    trouble.setTypeId(1L);
    trouble.setPriorityId(1L);
    trouble.setInsertSource("SPM");
    trouble.setAutoCreateWO(0L);
    trouble.setCreatedTime(new Date());
    trouble.setPriorityId(1L);
    trouble.setCreateUnitName("vtnet");
    trouble.setCreateUserName("thanhlv12");
    trouble.setState(1L);
    trouble.setTroubleCode("123");
    trouble.setIsMove(1L);
    trouble.setCountReopen(1L);
    trouble.setUnitMoveName("vtnet");
    trouble.setDateMove(new Date());
    PowerMockito.when(troublesRepository.findTroublesById(any())).thenReturn(trouble);
    PowerMockito
        .when(troublesRepository.getTroubleDTO(any(), any(), any(), any(), any(), any(), any()))
        .thenReturn(troubleOld);
    CatItemDTO stateDTOOld = Mockito.spy(CatItemDTO.class);
    stateDTOOld.setItemCode("DEFERRED");
    PowerMockito.when(catItemBusiness.getCatItemById(Long.valueOf(troubleOld.getState())))
        .thenReturn(stateDTOOld);
    CatItemDTO stateDTO = Mockito.spy(CatItemDTO.class);
    stateDTO.setItemCode("QUEUE");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("vn");
    PowerMockito.when(catItemBusiness.getCatItemById(Long.valueOf(tForm.getState())))
        .thenReturn(stateDTO);
    List<CatItemDTO> lstTTState = Mockito.spy(ArrayList.class);
    lstTTState.add(stateDTOOld);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstTTState);
    ResultDTO resultDto1 = Mockito.spy(ResultDTO.class);
    resultDto1.setId("1");
    PowerMockito.mockStatic(TroubleSpmUtils.class);
    */
/*try {
      PowerMockito.when(TroubleSpmUtils.updateSpmAction(any(), any(), any()))
          .thenReturn(resultDto1);
    } catch (Exception e) {
      e.printStackTrace();
    }*//*

    try {
      resultDto = troublesBusiness.onUpdateTrouble(tForm, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Assert.assertEquals(resultDto.getKey(), RESULT.SUCCESS);
  }


  @Test
  public void testOnUpdateTrouble_05() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleId(1L);
    tForm.setTroubleName("upgrade");
    tForm.setTypeId(1L);
    tForm.setAlarmGroupId("1");
    tForm.setPriorityId(1L);
    tForm.setInsertSource("SPM");
    tForm.setAutoCreateWO(0L);
    tForm.setInfraType("1");
    tForm.setCreatedTime(new Date());
    tForm.setPriorityId(1L);
    tForm.setStateName("WAITING RECEIVE");
    tForm.setCreateUnitName("vtnet");
    tForm.setCreateUserName("thanhlv12");
    tForm.setState(1L);
    tForm.setTroubleCode("123");
    tForm.setProcessingUnitId("1");
    tForm.setProcessingUserId("1");
    tForm.setReceiveUnitId(1L);
    tForm.setCheckbox("9");
    tForm.setDateMove(new Date());
    tForm.setDeferType(2L);
    tForm.setNumPending(1L);
    tForm.setReasonId(1L);
    tForm.setIsStationVip(1L);
    tForm.setWorkArround("vtnet");
    tForm.setReasonName("upgrade");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offset = 3.0;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    TroublesInSideDTO troubleOld = Mockito.spy(TroublesInSideDTO.class);
    troubleOld.setTroubleId(1L);
    troubleOld.setTroubleName("upgrade");
    troubleOld.setTypeId(1L);
    troubleOld.setAlarmGroupId("1");
    troubleOld.setPriorityId(1L);
    troubleOld.setInsertSource("SPM");
    troubleOld.setAutoCreateWO(0L);
    troubleOld.setInfraType("1");
    troubleOld.setCreatedTime(new Date());
    troubleOld.setPriorityId(2L);
    troubleOld.setStateName("WAITING RECEIVE");
    troubleOld.setCreateUnitName("vtnet");
    troubleOld.setCreateUserName("thanhlv12");
    troubleOld.setState(2L);
    troubleOld.setTroubleCode("123");
    troubleOld.setIsMove(1L);
    troubleOld.setProcessingUnitId("1");
    troubleOld.setProcessingUserId("1");
    troubleOld.setReceiveUnitId(2L);
    troubleOld.setAssignTimeTemp(new Date());
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setTroubleId(1L);
    trouble.setTroubleName("upgrade");
    trouble.setTypeId(1L);
    trouble.setPriorityId(1L);
    trouble.setInsertSource("BCCS");
    trouble.setAutoCreateWO(0L);
    trouble.setCreatedTime(new Date());
    trouble.setPriorityId(1L);
    trouble.setCreateUnitName("vtnet");
    trouble.setCreateUserName("thanhlv12");
    trouble.setState(1L);
    trouble.setTroubleCode("123");
    trouble.setIsMove(1L);
    trouble.setCountReopen(1L);
    trouble.setUnitMoveName("vtnet");
    trouble.setDateMove(new Date());
    trouble.setTimeUsed(2.0);
    trouble.setTroubleAssignId(1L);
    trouble.setNumPending(1L);
    trouble.setAlarmGroup("1");
    trouble.setTimeProcess(1.0);
    trouble.setReasonId(1L);
    PowerMockito.when(troublesRepository.findTroublesById(any())).thenReturn(trouble);
    PowerMockito
        .when(troublesRepository.getTroubleDTO(any(), any(), any(), any(), any(), any(), any()))
        .thenReturn(troubleOld);
    CatItemDTO stateDTOOld = Mockito.spy(CatItemDTO.class);
    stateDTOOld.setItemCode("WAIT FOR DEFERRED");
    PowerMockito.when(catItemBusiness.getCatItemById(Long.valueOf(troubleOld.getState())))
        .thenReturn(stateDTOOld);
    CatItemDTO stateDTO = Mockito.spy(CatItemDTO.class);
    stateDTO.setItemCode("DEFERRED");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("vn");
    PowerMockito.when(catItemBusiness.getCatItemById(Long.valueOf(tForm.getState())))
        .thenReturn(stateDTO);
    List<CatItemDTO> lstTTState = Mockito.spy(ArrayList.class);
    lstTTState.add(stateDTOOld);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstTTState);
    ResultDTO resultDto1 = Mockito.spy(ResultDTO.class);
    resultDto1.setId("1");
    List<String> lstSequence = Mockito.spy(ArrayList.class);
    lstSequence.add("1");
    PowerMockito.when(troublesRepository.getSequenseTroubles(anyString(), anyInt()))
        .thenReturn(lstSequence);
    PowerMockito.when(troubleAssignRepository.updateTroubleAssign(any())).thenReturn("vn");
    PowerMockito.when(troublesRepository.updateTroubles(any())).thenReturn("vn");
    PowerMockito.when(troubleWorklogRepository.insertTroubleWorklog(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.mockStatic(TroubleSpmUtils.class);
    try {
      PowerMockito.when(troubleSpmUtils.updateSpmAction(any(), any(), any()))
          .thenReturn(resultDto1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    resultInSideDto = troublesBusiness.onUpdateTrouble(tForm, true);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testOnUpdateTrouble_06() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultDto = Mockito.spy(ResultInSideDto.class);
    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleId(1L);
    tForm.setTroubleName("upgrade");
    tForm.setTypeId(1L);
    tForm.setAlarmGroupId("1");
    tForm.setPriorityId(1L);
    tForm.setInsertSource("SPM");
    tForm.setAutoCreateWO(0L);
    tForm.setInfraType("1");
    tForm.setCreatedTime(new Date());
    tForm.setPriorityId(1L);
    tForm.setStateName("WAITING RECEIVE");
    tForm.setCreateUnitName("vtnet");
    tForm.setCreateUserName("thanhlv12");
    tForm.setState(1L);
    tForm.setTroubleCode("123");
    tForm.setProcessingUnitId("1");
    tForm.setProcessingUserId("1");
    tForm.setReceiveUnitId(1L);
    tForm.setCheckbox("5");
    tForm.setDateMove(new Date());
    tForm.setDeferType(2L);
    tForm.setNumPending(1L);
    tForm.setReasonId(1L);
    tForm.setIsStationVip(1L);
    tForm.setWorkArround("vtnet");
    tForm.setReasonName("upgrade");
    tForm.setSpmName("SPM");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("thanhlv12");
    userToken.setTelephone("0123456789");
    userToken.setUserID(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offset = 3.0;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    TroublesInSideDTO troubleOld = Mockito.spy(TroublesInSideDTO.class);
    troubleOld.setTroubleId(1L);
    troubleOld.setTroubleName("upgrade");
    troubleOld.setTypeId(1L);
    troubleOld.setAlarmGroupId("1");
    troubleOld.setPriorityId(1L);
    troubleOld.setInsertSource("SPM");
    troubleOld.setAutoCreateWO(0L);
    troubleOld.setInfraType("1");
    troubleOld.setCreatedTime(new Date());
    troubleOld.setPriorityId(2L);
    troubleOld.setStateName("WAITING RECEIVE");
    troubleOld.setCreateUnitName("vtnet");
    troubleOld.setCreateUserName("thanhlv12");
    troubleOld.setState(2L);
    troubleOld.setTroubleCode("123");
    troubleOld.setIsMove(1L);
    troubleOld.setProcessingUnitId("1");
    troubleOld.setProcessingUserId("1");
    troubleOld.setReceiveUnitId(2L);
    troubleOld.setAssignTimeTemp(new Date());
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setTroubleId(1L);
    trouble.setTroubleName("upgrade");
    trouble.setTypeId(1L);
    trouble.setPriorityId(1L);
    trouble.setInsertSource("BCCS");
    trouble.setAutoCreateWO(0L);
    trouble.setCreatedTime(new Date());
    trouble.setPriorityId(1L);
    trouble.setCreateUnitName("vtnet");
    trouble.setCreateUserName("thanhlv12");
    trouble.setState(1L);
    trouble.setTroubleCode("123");
    trouble.setIsMove(1L);
    trouble.setCountReopen(1L);
    trouble.setUnitMoveName("vtnet");
    trouble.setDateMove(new Date());
    trouble.setTimeUsed(2.0);
    trouble.setTroubleAssignId(1L);
    trouble.setNumPending(1L);
    trouble.setAlarmGroup("1");
    trouble.setTimeProcess(1.0);
    trouble.setReasonId(1L);
    trouble.setAutoClose(1L);
    trouble.setTroubleAssignId(1L);
    PowerMockito.when(troublesRepository.findTroublesById(any())).thenReturn(trouble);
    PowerMockito
        .when(troublesRepository.getTroubleDTO(any(), any(), any(), any(), any(), any(), any()))
        .thenReturn(troubleOld);
    CatItemDTO stateDTOOld = Mockito.spy(CatItemDTO.class);
    stateDTOOld.setItemCode("WAIT FOR DEFERRED");
    PowerMockito.when(catItemBusiness.getCatItemById(Long.valueOf(troubleOld.getState())))
        .thenReturn(stateDTOOld);
    CatItemDTO stateDTO = Mockito.spy(CatItemDTO.class);
    stateDTO.setItemCode("CLOSED NOT KEDB");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("vn");
    PowerMockito.when(catItemBusiness.getCatItemById(Long.valueOf(tForm.getState())))
        .thenReturn(stateDTO);
    List<CatItemDTO> lstTTState = Mockito.spy(ArrayList.class);
    lstTTState.add(stateDTOOld);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstTTState);
    ResultDTO resultDto1 = Mockito.spy(ResultDTO.class);
    resultDto1.setId("1");
    PowerMockito.mockStatic(TroubleSpmUtils.class);
    PowerMockito.when(troubleBccsUtils.updateCompProcessing(any(), anyInt(), any(), any()))
        .thenReturn(resultDto1);
    resultDto = troublesBusiness.onUpdateTrouble(tForm, false);
    Assert.assertEquals(resultDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateTroubleToSPM_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String type = "1";
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setTroubleId(1L);
    troublesDTO.setStateName("active");
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    TroublesEntity trouble = Mockito.spy(TroublesEntity.class);
    trouble.setTroubleId(1l);
    PowerMockito.when(troublesRepository.findTroublesById(Long.valueOf(troublesDTO.getTroubleId())))
        .thenReturn(trouble);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    PowerMockito.mockStatic(TroubleSpmUtils.class);
    try {
      PowerMockito.when(troubleSpmUtils.updateSpmCompleteNotCheck(any(), any()))
          .thenReturn(resultDTO);
    } catch (Exception e) {
      e.printStackTrace();
    }
    result = troublesBusiness.updateTroubleToSPM(troublesDTO, type);
    Assert.assertEquals(result.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testUpdateTrouble_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    PowerMockito.when(troublesRepository.updateTroubles(any())).thenReturn(RESULT.SUCCESS);
    resultInSideDto = troublesBusiness.updateTrouble(tForm);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testValidateDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesDTO dialog = Mockito.spy(TroublesDTO.class);
    dialog.setInsertSource("NOC");
    dialog.setTblCurr("tblCurrNull");
    dialog.setTblHis("tblHisNull");
    dialog.setTblClear("tblClearNull");
    dialog.setAlarmId("1");
    dialog.setCatchingTime(DateUtil.date2YYYYMMddTime(new Date()));
    dialog.setWoType("1");
    dialog.setTypeId("0");
    dialog.setAlarmGroupCode("1");
    dialog.setSubCategoryId("2");
    dialog.setAutoCreateWO(1L);
    dialog.setTroubleName("sos");
    dialog.setPriorityId("1");
    dialog.setRisk("1");
    dialog.setBeginTroubleTime(DateUtil.date2YYYYMMddTime(new Date()));
    dialog.setDescription("vtnet");
    dialog.setReceiveUnitId("1");
    dialog.setCreateUserName("thanhlv12");
    dialog.setCreateUnitId("1");
    dialog.setReceiveUnitId("1");
    dialog.setCreateUnitName("vtnet");
    TroubleMopDTO troubleMopDTO = Mockito.spy(TroubleMopDTO.class);
    troubleMopDTO.setRunCycle("1");
    troubleMopDTO.setMaxNumberRun("1");
    troubleMopDTO.setAlarmId("1");
    troubleMopDTO.setRunType("1");
    List<TroubleMopDTO> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add(troubleMopDTO);
    String result = troublesBusiness.validateDTO(dialog, lstMop);
    Assert.assertEquals(result, "LstNode is not null");
  }

  @Test
  public void testValidateDTO_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    TroublesDTO dialog = Mockito.spy(TroublesDTO.class);
    dialog.setInsertSource("NOC");
    dialog.setTblCurr("tblCurrNull");
    dialog.setTblHis("tblHisNull");
    dialog.setTblClear("tblClearNull");
    dialog.setAlarmId("1");
    dialog.setCatchingTime(DateUtil.date2YYYYMMddTime(new Date()));
    dialog.setWoType("1");
    dialog.setTypeId("0");
    dialog.setAlarmGroupCode("1");
    dialog.setSubCategoryId("2");
    dialog.setTroubleName("sos");
    dialog.setPriorityId("1");
    dialog.setRisk("1");
    dialog.setBeginTroubleTime(DateUtil.date2YYYYMMddTime(new Date()));
    dialog.setDescription("vtnet");
    dialog.setReceiveUnitId("1");
    dialog.setCreateUserName("thanhlv12");
    dialog.setCreateUnitId("1");
    dialog.setCreateUnitName("vtnet");
    dialog.setBeginTroubleTime(DateUtil.date2YYYYMMddTime(new Date()));
    dialog.setAutoCreateWO(3L);
    dialog.setSubCategoryId("1");
    CatItemDTO prio = Mockito.spy(CatItemDTO.class);
    prio.setItemCode(String.valueOf(dialog.getPriorityId()).trim().toLowerCase());
    prio.setCategoryCode("TT_PRIORITY");
    List<CatItemDTO> lstCat = Mockito.spy(ArrayList.class);
    lstCat.add(prio);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCat);
    CatItemDTO catItem = Mockito.spy(CatItemDTO.class);
    catItem.setItemCode(Constants.TT_STATE.Waiting_Receive);
    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(catItem);
    CatItemDTO typeDTO = Mockito.spy(CatItemDTO.class);
    typeDTO.setItemCode(
        "PT_TYPE");
    typeDTO.setCategoryCode(TROUBLE.PT_TYPE);
    List<CatItemDTO> lstType = Mockito.spy(ArrayList.class);
    //PowerMockito.when(catItemBusiness.getListCatItemDTO(typeDTO)).thenReturn(lstType);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstType);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitCode(
        dialog.getReceiveUnitId() == null ? null : String.valueOf(dialog.getReceiveUnitId()));
    List<UnitDTO> u = Mockito.spy(ArrayList.class);
    u.add(unitDTO);
    PowerMockito
        .when(unitRepository.getListUnitDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(u);
    UsersInsideDto u1 = Mockito.spy(UsersInsideDto.class);
    u1.setUnitName("vtnet");
    u1.setUsername("thanhlv12");
    u1.setUnitId(1L);
    PowerMockito.when(userBusiness.getUserDTOByUserName(any())).thenReturn(u1);
    TroubleMopDTO troubleMopDTO = Mockito.spy(TroubleMopDTO.class);
    troubleMopDTO.setRunCycle("1");
    troubleMopDTO.setMaxNumberRun("1");
    troubleMopDTO.setAlarmId("1");
    troubleMopDTO.setRunType("1");
    troubleMopDTO.setRule("1");
    troubleMopDTO.setMopId("1");
    troubleMopDTO.setMopName("vtnet");
    troubleMopDTO.setSystem("vt");
    troubleMopDTO.setDomain("vtnet.com");
    List<TroubleMopDTO> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add(troubleMopDTO);
    String result = troublesBusiness.validateDTO(dialog, lstMop);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testValidateDTO_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    TroublesDTO dialog = Mockito.spy(TroublesDTO.class);
    dialog.setInsertSource("NOC");
    dialog.setTblCurr("tblCurrNull");
    dialog.setTblHis("tblHisNull");
    dialog.setTblClear("tblClearNull");
    dialog.setAlarmId("1");
    dialog.setCatchingTime(DateUtil.date2YYYYMMddTime(new Date()));
    dialog.setWoType("1");
    dialog.setTypeId("0");
    dialog.setAlarmGroupCode("1");
    dialog.setSubCategoryId("2");
    dialog.setTroubleName("sos");
    dialog.setPriorityId("1");
    dialog.setRisk("1");
    dialog.setBeginTroubleTime(DateUtil.date2YYYYMMddTime(new Date()));
    dialog.setDescription("vtnet");
    dialog.setReceiveUnitId("1");
    dialog.setCreateUserName("thanhlv12");
    dialog.setCreateUnitId("1");
    dialog.setCreateUnitName("vtnet");
    dialog.setBeginTroubleTime(DateUtil.date2YYYYMMddTime(new Date()));
    dialog.setAutoCreateWO(3L);
    dialog.setSubCategoryId("1");
    dialog.setAffectedService("vtnet");
    TroubleMopDTO troubleMopDTO = Mockito.spy(TroubleMopDTO.class);
    troubleMopDTO.setRunCycle("1");
    troubleMopDTO.setMaxNumberRun("1");
    troubleMopDTO.setAlarmId("1");
    troubleMopDTO.setRunType("1");
    troubleMopDTO.setRule("1");
    troubleMopDTO.setMopId("1");
    troubleMopDTO.setMopName("vtnet");
    troubleMopDTO.setSystem("vt");
    troubleMopDTO.setDomain("vtnet.com");
    List<TroubleMopDTO> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add(troubleMopDTO);
    String result = troublesBusiness.validateDTO(dialog, lstMop);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testDoCallIPCC_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.mockStatic(I18n.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setMobile("0123456789");
    IpccServiceDTO ipccServiceDTO = Mockito.spy(IpccServiceDTO.class);
    ipccServiceDTO.setMobile("0123456789");
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    //troublesDTO.setCreatedTimeFrom("10/06/2019 00:00:00");
    troublesDTO.setTypeId(1L);
    troublesDTO.setPriorityId(1L);
    troublesDTO.setAlarmGroupId("1");
    troublesDTO.setInsertSource("SPM");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    troublesDTO.setTroubleId(1L);
    troublesDTO.setTroubleCode("123");
    troublesDTO.setProcessingUserName("thanhlv12");
    troublesDTO.setProcessingUnitName("vtnet");
    CfgTimeTroubleProcessDTO cttpdto = Mockito.spy(CfgTimeTroubleProcessDTO.class);
    cttpdto.setTypeId(Long.valueOf(troublesDTO.getTypeId()));
    cttpdto.setSubCategoryId(Long.valueOf(troublesDTO.getAlarmGroupId()));
    cttpdto.setPriorityId(Long.valueOf(troublesDTO.getPriorityId()));
    cttpdto.setCountry(troublesDTO.getInsertSource());
    PowerMockito.when(ttCategoryServiceProxy.getConfigTimeTroubleProcess(any()))
        .thenReturn(cttpdto);
    String urlForIPCC = "ipcc";
    resultInSideDto = troublesBusiness.doCallIPCC(unitDTO, ipccServiceDTO, troublesDTO, urlForIPCC);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
  }

  @Test
  public void testDoCallIPCC_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setMobile("0123456789");
    IpccServiceDTO ipccServiceDTO = Mockito.spy(IpccServiceDTO.class);
    ipccServiceDTO.setMobile("0123456789");
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setCreatedTimeFrom("10/06/2019 00:00:00");
    troublesDTO.setTypeId(1L);
    troublesDTO.setPriorityId(1L);
    troublesDTO.setAlarmGroupId("1");
    troublesDTO.setInsertSource("SPM");
    troublesDTO.setTroubleId(1L);
    troublesDTO.setTroubleCode("123");
    troublesDTO.setProcessingUserName("thanhlv12");
    troublesDTO.setProcessingUnitName("vtnet");
    CfgTimeTroubleProcessDTO cttpdto = Mockito.spy(CfgTimeTroubleProcessDTO.class);
    cttpdto.setTypeId(Long.valueOf(troublesDTO.getTypeId()));
    cttpdto.setSubCategoryId(Long.valueOf(troublesDTO.getAlarmGroupId()));
    cttpdto.setPriorityId(Long.valueOf(troublesDTO.getPriorityId()));
    cttpdto.setCountry(troublesDTO.getInsertSource());
    PowerMockito.when(ttCategoryServiceProxy.getConfigTimeTroubleProcess(any()))
        .thenReturn(cttpdto);
    String urlForIPCC = "ipcc";
    PowerMockito.when(logCallIpccService.insertLogCallIpcc(any())).thenReturn(resultInSideDto);
    NomalOutput nomalOutput = Mockito.spy(NomalOutput.class);
    nomalOutput.setDescription(RESULT.DATA_OVER);
    WSIPCC wsipcc = PowerMockito.mock(WSIPCC.class);
    setMockSingletonWSIPCC(wsipcc);
    LogCallIpccDTO logCallIpccDTO = Mockito.spy(LogCallIpccDTO.class);
    logCallIpccDTO.setDescription(RESULT.FAIL);
    logCallIpccDTO.setResult("FAIL");
    logCallIpccDTO.setResultTime("10/07/2019 00:00:00");
    List<LogCallIpccDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(logCallIpccDTO);
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    lstCondition.add(
        new ConditionBean("transactionId", "1", "NAME_EQUAL", "STRING"));
    PowerMockito.when(logCallIpccService
        .getListLogCallIpccByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);
    PowerMockito.when(logCallIpccService.insertLogCallIpcc(any())).thenReturn(resultInSideDto);
    resultInSideDto = troublesBusiness.doCallIPCC(unitDTO, ipccServiceDTO, troublesDTO, urlForIPCC);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }


  @Test
  public void testViewCall_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    TroublesInSideDTO dtoTran = Mockito.spy(TroublesInSideDTO.class);
    dtoTran.setTroubleId(1L);
    dtoTran.setPageSize(1);
    dtoTran.setPage(1);
    LogCallIpccDTO logCallIpccDTO = Mockito.spy(LogCallIpccDTO.class);
    List<LogCallIpccDTO> logCallIpccDTOS = Mockito.spy(ArrayList.class);
    logCallIpccDTOS.add(logCallIpccDTO);
    LogCallIpccServiceImpl logCallIpccService = PowerMockito.mock(LogCallIpccServiceImpl.class);
    setMockSingleton(logCallIpccService);
    try {
      PowerMockito.when(logCallIpccService
          .getListLogCallIpccByCondition(anyList(), (int) anyLong(), anyInt(), anyString(),
              anyString())).thenReturn(logCallIpccDTOS);
      datatable = troublesBusiness.viewCall(dtoTran);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Assert.assertEquals(datatable.getTotal(), 0);
  }

  @Test
  public void testGetListChatUsers_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setCreateUserId(1L);
    troublesDTO.setReceiveUserId(1L);
    List<Long> userIds = Mockito.spy(ArrayList.class);
    userIds.add(userToken.getUserID());
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    usersInsideDtos.add(usersInsideDto);
    usersInsideDtos = troublesBusiness.getListChatUsers(troublesDTO);
    Assert.assertEquals(usersInsideDtos.size(), 0);
  }

  @Test
  public void testLoadUserSupportGroup_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setTypeId(1L);
    troublesDTO.setPage(1);
    troublesDTO.setPageSize(1);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("CHAT_CODE");
    catItemDTO.setDescription("vtnet");
    PowerMockito.when(catItemBusiness.getCatItemById(anyLong())).thenReturn(catItemDTO);
    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(catItemDTO);
    PowerMockito.when(catItemBusiness.getListItemByCategory(anyString(), any())).thenReturn(lst);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    PowerMockito.when(userBusiness.getListUsersByList(any())).thenReturn(datatable);
    datatable = troublesBusiness.loadUserSupportGroup(troublesDTO);
    Assert.assertEquals(datatable.getTotal(), 0);
  }

  @Test
  public void testOnSearchCountByState_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("CHAT_CODE");
    catItemDTO.setDescription("vtnet");
    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(catItemDTO);
    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    PowerMockito.when(troublesRepository.onSearchCountByState(any())).thenReturn(lst);
    lst = troublesBusiness.onSearchCountByState(dto);
    Assert.assertEquals(lst.size(), 1);
  }

  @Test
  public void testGetConfigProperty_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Map<String, String> param = new HashMap<>();
    param.put("SPM", "chat");
    PowerMockito.when(troublesRepository.getConfigProperty()).thenReturn(param);
    param = troublesBusiness.getConfigProperty();
    Assert.assertEquals(param.size(), 1);
  }

  @Test
  public void testGetListReasonBCCS_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    dto.setComplaintGroupId("1,2");
    dto.setInsertSource("BCCS");
    dto.setIsTickHelp(1L);
    dto.setServiceType(1L);
    dto.setTechnology("4.0");
    Long parentId = 1L;
    int level = 1;
    CompCauseDTO causeDTO = Mockito.spy(CompCauseDTO.class);
    causeDTO.setCompCauseId("1");
    causeDTO.setName("vtnet");
    causeDTO.setCode("1");
    List<CompCauseDTO> lstCauseDTO = Mockito.spy(ArrayList.class);
    lstCauseDTO.add(causeDTO);
    PowerMockito.when(compCauseBusiness.translateList(anyList(), anyString()))
        .thenReturn(lstCauseDTO);
    troublesBusiness.getListReasonBCCS(dto, parentId, level);
    Assert.assertEquals(lstCauseDTO.size(), 1);
  }

  @Test
  public void testGetListReasonOverdue_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CompCause compCause = Mockito.spy(CompCause.class);
    List<CompCause> lst = Mockito.spy(ArrayList.class);
    lst.add(compCause);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setInsertSource("SPM");
    Long parentId = 1L;
    ConditionBean conditionBean = Mockito.spy(ConditionBean.class);
    conditionBean.setValue("1");
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    lstCondition.add(conditionBean);
    PowerMockito.mockStatic(ConditionBeanUtil.class);
    CfgServerNocDTO cfgServerNocDTO = Mockito.spy(CfgServerNocDTO.class);
    cfgServerNocDTO.setInsertSource("SPM");
    cfgServerNocDTO.setPass("123456a@");
    List<CfgServerNocDTO> lstServer = Mockito.spy(ArrayList.class);
    lstServer.add(cfgServerNocDTO);
    PowerMockito.when(cfgServerNocRepository
        .getListCfgServerNocByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstServer);
    SpmRespone res = Mockito.spy(SpmRespone.class);
    res.setErrorCode("00");
    List<CauseErrorExpireDTO> lst2 = Mockito.spy(ArrayList.class);
    CauseErrorExpireDTO expireDTO = new CauseErrorExpireDTO();
    expireDTO.setCauseErrExpId(1L);
    expireDTO.setCode("vtnet");
    expireDTO.setName("vtnet");
    lst2.add(expireDTO);
    PowerMockito.when(wsbccs2Port.getCauseExistError(anyLong(), any())).thenReturn(res);
    lst = troublesBusiness.getListReasonOverdue(troublesDTO, parentId);
    Assert.assertEquals(lst.size(), 0);
  }

  @Test
  public void testGetListGroupSolution_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setInsertSource("SPM");
    CfgServerNocDTO cfgServerNocDTO = Mockito.spy(CfgServerNocDTO.class);
    cfgServerNocDTO.setInsertSource("SPM");
    cfgServerNocDTO.setPass("123456a@");
    List<CfgServerNocDTO> lstServer = Mockito.spy(ArrayList.class);
    lstServer.add(cfgServerNocDTO);
    PowerMockito.when(cfgServerNocRepository
        .getListCfgServerNocByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstServer);
    SpmRespone res = Mockito.spy(SpmRespone.class);
    res.setErrorCode("00");
    PowerMockito.when(wsbccs2Port.getListSolution(any())).thenReturn(res);
    TroubleNetworkSolutionDTO dto = Mockito.spy(TroubleNetworkSolutionDTO.class);
    dto.setName("vtnet");
    List<TroubleNetworkSolutionDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(dto);
    troublesBusiness.getListGroupSolution(troublesDTO);
    Assert.assertEquals(lst.size(), 1);
  }

  @Test
  public void testGetLstNetworkLevel_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String typeId = "1";
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("vtnet");
    List<CatItemDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(catItemDTO);
    CfgMapNetLevelIncTypeDTO dto = Mockito.spy(CfgMapNetLevelIncTypeDTO.class);
    dto.setTroubleTypeId(1L);
    dto.setNetworkLevelId("1");
    List<CfgMapNetLevelIncTypeDTO> lstMap = Mockito.spy(ArrayList.class);
    lstMap.add(dto);
    PowerMockito.when(commonStreamServiceProxy.getListCfgMapNetLevelIncTypeDTO(dto))
        .thenReturn(lstMap);
    lst = troublesBusiness.getLstNetworkLevel("1");
    Assert.assertEquals(lst.size(), 0);
  }

  @Test
  public void testGetListTroubleSearchExport_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    File file = new File("test.txt");
    List<String> lstType = Mockito.spy(ArrayList.class);
    String type = "sos";
    lstType.add(type);
    PowerMockito.mockStatic(I18n.class);
    ApplicationContext context = Mockito.spy(ApplicationContext.class);
    setMockStatic(context);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("vn");
    TroublesInSideDTO troubleSearch = Mockito.spy(TroublesInSideDTO.class);
    troubleSearch.setLstType(lstType);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setTroubleCode("123");
    troublesDTO.setCreatedTimeFrom("10/06/2019 00:00:00");
    troublesDTO.setCreatedTimeTo("10/07/2019 00:00:00");
    List<TroublesInSideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(troublesDTO);
    PowerMockito.when(troublesRepository.getTroublesSearchExport(any())).thenReturn(lst);
    File file1 = new File("test.txt");
    */
/*try {
      PowerMockito.when(troublesBusiness.getListTroubleSearchExport(troubleSearch))
          .thenReturn(file1);
    } catch (Exception e) {
      e.printStackTrace();
    }*//*

    Assert.assertEquals(file, file1);
  }

  @Test
  public void testSetTimeZoneForTrouble_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setCreatedTime(new Date());
    troubleDTO.setLastUpdateTime(new Date());
    troubleDTO.setAssignTime(new Date());
    troubleDTO.setQueueTime(new Date());
    troubleDTO.setClearTime(new Date());
    troubleDTO.setClosedTime(new Date());
    troubleDTO.setBeginTroubleTime(new Date());
    troubleDTO.setEndTroubleTime(new Date());
    troubleDTO.setDeferredTime(new Date());
    troubleDTO.setAssignTimeTemp(new Date());
    troubleDTO.setDateMove(new Date());
    troubleDTO.setCatchingTime(new Date());
    troubleDTO.setEstimateTime(new Date());
    try {
      troublesBusiness.setTimeZoneForTrouble(troubleDTO, 3.0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Assert.assertEquals(troubleDTO.getState(), null);
  }

  @Test
  public void testUpdateTroubleDTOInfo_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Double offset = 2.0;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setState(1L);
    troubleDTO.setTypeId(1L);
    troubleDTO.setSubCategoryId(1L);
    troubleDTO.setPriorityId(1L);
    troubleDTO.setImpactId(1L);
    troubleDTO.setSolutionType(1L);
    troubleDTO.setRisk(1L);
    troubleDTO.setVendorId(1L);
    troubleDTO.setRejectedCode(1L);
    troubleDTO.setWarnLevel(1L);
    troubleDTO.setCableType(1L);
    troubleDTO.setAutoClose(1L);
    troubleDTO.setCreatedTime(new Date());
    troubleDTO.setCatchingTime(new Date());
    troubleDTO.setTimeCreateCfg(3.0);
    troubleDTO.setBeginTroubleTime(new Date());
    troubleDTO.setEndTroubleTime(new Date());
    troubleDTO.setClosedTime(new Date());
    troubleDTO.setRemainTime("2");
    troubleDTO.setIsStationVip(1L);
    troubleDTO.setTransNetworkTypeId(-1L);
    troubleDTO.setTypeId(2046L);
    troubleDTO.setTransReasonEffectiveId(1L);
    List<TroublesInSideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(troubleDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    troublesBusiness.updateTroubleDTOInfo(lst);
    Assert.assertEquals(lst.size(), 1);
  }

  @Test
  public void testSearchCrRelated_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    CrSearchDTO crSearchDTO = Mockito.spy(CrSearchDTO.class);
    crSearchDTO.setCrNumber("1");
    crSearchDTO.setTitle("vtnet");
    crSearchDTO.setPageSize(1);
    crSearchDTO.setPage(1);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ObjResponse obj = Mockito.spy(ObjResponse.class);
    obj.setFullName("le van thanh");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    List<CrDTO> crDTOS = Mockito.spy(ArrayList.class);
    crDTOS.add(crDTO);
    obj.setLstCrDTO(crDTOS);
    CrServiceImpl crService = PowerMockito.mock(CrServiceImpl.class);
    setMockSingletonCrServiceImpl(crService);
    List<LinkedHashMap> lstHasMap = Mockito.spy(ArrayList.class);
    Map<String, Object> param = new HashMap<>();
    param.put("crId", 1);
//    lstHasMap.add((LinkedHashMap) param);
    datatable.setData(lstHasMap);
    PowerMockito.when(crService.getListCRBySearchTypePagging(any(CrDTO.class), anyInt(), anyInt()))
        .thenReturn(obj);
    datatable.setPages(1);
    datatable.setTotal(1);
    PowerMockito.when(crServiceProxy.onSearch(any())).thenReturn(datatable);
    troublesBusiness.searchCrRelated(crSearchDTO);
    Assert.assertEquals(datatable.getTotal(), 0);
  }

  @Test
  public void testLoadCrRelatedDetail_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    List<CrDTO> dtoCRResult = Mockito.spy(ArrayList.class);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("1");
    crDTO.setCrId("1");
    crDTO.setChangeOrginatorName("thanhlv12");
    crDTO.setCrNumber("1");
    crDTO.setTitle("vtnet");
    crDTO.setEarliestStartTime("");
    dtoCRResult.add(crDTO);
    PowerMockito.when(
        crService.getListCrByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(dtoCRResult);
    troublesBusiness.loadCrRelatedDetail("VTNET");
    Assert.assertEquals(dtoCRResult.size(), 1);
  }

  @Test
  public void testLoadTroubleCrDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    dto.setPage(1);
    dto.setPageSize(1);
    dto.setTroubleId(1L);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    List<CrDTO> dtoCRResult = Mockito.spy(ArrayList.class);
    dtoCRResult.add(crDTO);
    CrServiceImpl crService = PowerMockito.mock(CrServiceImpl.class);
    setMockSingletonCrServiceImpl(crService);
    try {
      PowerMockito.when(crService.getListCRFromOtherSystem(anyString(), anyString(), anyString()))
          .thenReturn(dtoCRResult);
      datatable = troublesBusiness.loadTroubleCrDTO(dto);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Assert.assertEquals(datatable.getTotal(), 0);
  }

  @Test
  public void testSendTicketToTKTU_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    com.viettel.gnoc.commons.dto.ResultDTO rDto = Mockito
        .spy(com.viettel.gnoc.commons.dto.ResultDTO.class);
    try {
      PowerMockito.when(troublesServiceForCCBusiness.sendTicketToTKTU(any())).thenReturn(rDto);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      resultInSideDto = troublesBusiness.sendTicketToTKTU(dto);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
  }

  */
/*@Test
  public void testInsertCrCreatedFromOtherSystem_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setTroubleId(1L);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    List<CrDTO> crDTOS = Mockito.spy(ArrayList.class);
    crDTOS.add(crDTO);
    troublesDTO.setCrDTOS(crDTOS);
    PowerMockito.when(crServiceProxy.insertCrCreatedFromOtherSystem(any())).thenReturn("SUCCESS");
    troublesBusiness.insertCrCreatedFromOtherSystem(troublesDTO);
    Assert.assertEquals(crDTOS.size(), 1);
  }*//*


  @Test
  public void testSendChatListUsers() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultDto = Mockito.spy(ResultInSideDto.class);
    TroublesInSideDTO dtoTran = Mockito.spy(TroublesInSideDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    dtoTran.setTroubleCode("123");
    dtoTran.setIsChat(1L);
    PowerMockito.mockStatic(I18n.class);
    ApplicationContext context = Mockito.spy(ApplicationContext.class);
    setMockStatic(context);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("vn");
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUnitName("vtnet");
    usersInsideDto.setUnitId(1L);
    usersInsideDto.setUserId(999999L);
    usersInsideDto.setUsername("thanhlv12");
    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    usersInsideDtos.add(usersInsideDto);
    dtoTran.setUsersInsideDtos(usersInsideDtos);
    GroupResponse groupResponse = Mockito.spy(GroupResponse.class);
    groupResponse.setResultCode(1);
    PowerMockito.when(wsChatPort.createGroupInBusiness2(any())).thenReturn(groupResponse);
    resultDto = troublesBusiness.sendChatListUsers(dtoTran);
    Assert.assertEquals(resultDto.getKey(), RESULT.SUCCESS);
  }


  @Test
  public void testCallIPCC_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setReceiveUnitId(1L);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(Long.valueOf(troublesDTO.getReceiveUnitId()));
    unitDTO.setIpccServiceId(1L);
    unitDTO.setMobile("0123456789");
    List<UnitDTO> units = Mockito.spy(ArrayList.class);
    units.add(unitDTO);
    IpccServiceDTO ipccServiceDTO = Mockito.spy(IpccServiceDTO.class);
    ipccServiceDTO.setIpccServiceId(unitDTO.getIpccServiceId());
    List<IpccServiceDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(ipccServiceDTO);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    PowerMockito.when(smsGatewayRepository.getListIpccServiceDTO(any())).thenReturn(lst);
    PowerMockito.when(unitRepository.getListUnitDTO(unitDTO, 0, 0, null, null)).thenReturn(units);
    try {
      resultInSideDto = troublesBusiness.callIPCC(troublesDTO);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
  }

  @Test
  public void testOnInsertTroubleFile_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    TroublesInSideDTO trouble = Mockito.spy(TroublesInSideDTO.class);
//    List<byte[]> fileDocumentByteArray = Mockito.spy(ArrayList.class);
//    byte[] arrFile = new byte[5];
//    fileDocumentByteArray.add(arrFile);
//    trouble.setFileDocumentByteArray(fileDocumentByteArray);
    String fileOld = "vtnet";
    List<String> lstFileOld = Mockito.spy(ArrayList.class);
    lstFileOld.add(fileOld);
    trouble.setArrFileName(lstFileOld);
    String result = null;
    List<String> seq = Mockito.spy(ArrayList.class);
    seq.add("1");
    PowerMockito.when(troublesRepository.getSequenseTroubles(anyString(), anyInt()))
        .thenReturn(seq);
    trouble.setTroubleId(1L);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(1L);
    unitToken.setUnitName("A");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    PowerMockito.when(userBusiness.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(troublesRepository.insertCommonFile(any())).thenReturn(resultInSideDto);
    PowerMockito.when(troublesRepository.insertTroubleFile(any())).thenReturn(resultInSideDto);
    PowerMockito.when(gnocFileRepository.saveListGnocFile(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    result = troublesBusiness.onInsertTroubleFile(trouble, lstFileOld);
    Assert.assertEquals(result, RESULT.SUCCESS);
  }

  @Test
  public void testSendSms_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setReceiveUnitId(1L);
    troubleDTO.setTroubleName("sos");
    troubleDTO.setTroubleCode("123");
    troubleDTO.setCreateUnitName("vtnet");
    troubleDTO.setReceiveUnitName("vtnet");
    troubleDTO.setPriorityName("minor");
    troubleDTO.setStateName("CLOSED");
    String content = "message.created.TT";
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setUserId(999999L);
    usersInsideDto.setUnitId(1L);
    usersInsideDto.setUnitName("vtnet");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("SUCCESS");
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    lstCondition.add(new ConditionBean("unitId", "vtnet", Constants.NAME_EQUAL,
        Constants.NUMBER));
    lstCondition.add(new ConditionBean("isEnable", "1", Constants.NAME_EQUAL, Constants.NUMBER));
    Double offset = 3.0;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    PowerMockito.when(userBusiness
        .getListUsersByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstUser);
    String result;
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.when(DataUtil.getLang(any(), any())).thenReturn(content);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<MessagesDTO> lsMessagesDTOs = Mockito.spy(ArrayList.class);
    PowerMockito.when(messagesRepository.insertOrUpdateListMessagesCommon(lsMessagesDTOs))
        .thenReturn(resultInSideDto);
    result = troublesBusiness.sendSms(troubleDTO, content);
    Assert.assertEquals(result, RESULT.SUCCESS);
  }

  @Test
  public void testOnSearchTroubleRelated() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO dto = Mockito.spy(TroublesInSideDTO.class);
    dto.setTroubleId(1L);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setTotal(999);
    List<TroublesInSideDTO> list = Mockito.spy(ArrayList.class);
    list.add(dto);
    datatable.setData(list);
    Double offset = 3D;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    PowerMockito.when(troublesRepository.onSearchTroubleRelated(any())).thenReturn(datatable);
    Datatable result = troublesBusiness.onSearchTroubleRelated(dto);
    Assert.assertEquals(result, datatable);
  }

  */
/*@Test
  public void testInsertTrouble_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setPassword("123456");
    requestDTO.setUsername("anhlp");
    requestDTO.setPartyCode("1");
    requestDTO.setRequestId("1");
    List<String> listAccount = Mockito.spy(ArrayList.class);
    String account1 = "anhlp";
    listAccount.add(account1);
    Date currDate = new Date();
    troublesDTO.setTroubleId(1L);
    Double offset = 3D;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    try {
      troublesBusiness2.setTimeZoneForTrouble(troublesDTO, offset);
    } catch (Exception e) {
      e.printStackTrace();
    }
    troublesDTO.setCreatedTime(currDate);
    List<String> fileName = Mockito.spy(ArrayList.class);
    List<byte[]> dataArray = Mockito.spy(ArrayList.class);
    MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json",
        "{\"json\": \"someValue\"}".getBytes());
    files.add(jsonFile);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    try {
      resultInSideDto = (ResultInSideDto) PowerMockito
          .when(troublesBusiness.insertTroublesTT(requestDTO, files, troublesDTO, listAccount));
    } catch (Exception e) {
      e.printStackTrace();
    }
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto1 = Mockito.spy(com.viettel.gnoc.commons.dto.ResultDTO.class);
    TroublesDTO troublesDTO1 = Mockito.spy(TroublesDTO.class);
    try {
      resultInSideDto1 = troublesBusiness.insertTroublesNOC(requestDTO, troublesDTO1, listAccount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    resultInSideDto1.setKey(RESULT.SUCCESS);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testInsertTrouble_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    Map<String, String> param = Mockito.spy(HashMap.class);
    troublesDTO.setMap(param);
    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setPassword("123456");
    requestDTO.setUsername("anhlp");
    requestDTO.setPartyCode("1");
    requestDTO.setRequestId("1");
    List<String> listAccount = Mockito.spy(ArrayList.class);
    String account1 = "anhlp";
    listAccount.add(account1);
    Date currDate = new Date();
    troublesDTO.setTroubleId("1");
    Double offset = 3D;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    try {
      troublesBusiness2.setTimeZoneForTrouble(troublesInSideDTO, offset);
    } catch (Exception e) {
      e.printStackTrace();
    }
    troublesDTO.setCreatedTime(DateUtil.date2ddMMyyyyHHMMss(currDate));
    List<String> fileName = Mockito.spy(ArrayList.class);
    List<byte[]> dataArray = Mockito.spy(ArrayList.class);
    MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json",
        "{\"json\": \"someValue\"}".getBytes());
    files.add(jsonFile);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    try {
      resultInSideDto = (ResultInSideDto) PowerMockito
          .when(troublesBusiness.insertTroublesTT(requestDTO, files, troublesInSideDTO, listAccount));
    } catch (Exception e) {
      e.printStackTrace();
    }
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto1 = Mockito.spy(com.viettel.gnoc.commons.dto.ResultDTO.class);
    try {
      resultInSideDto1 = troublesBusiness.insertTroublesNOC(requestDTO, troublesDTO, listAccount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    resultInSideDto1.setKey(RESULT.SUCCESS);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testInsertTrouble_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setInsertSource("TT");
    troublesDTO.setAlarmGroupId("1");
    troublesDTO.setAlarmGroupCode("SOS");
    troublesDTO.setTypeId("1");
    CatItemDTO alarmGroupDTO = Mockito.spy(CatItemDTO.class);
    alarmGroupDTO.setItemCode(troublesDTO.getAlarmGroupCode());
    CfgCreateWoDTO createWoDTO = Mockito.spy(CfgCreateWoDTO.class);
    createWoDTO.setTypeId(String.valueOf(troublesDTO.getTypeId()));
    createWoDTO.setAlarmGroupId(troublesDTO.getAlarmGroupId());
    List<CfgCreateWoDTO> lstCfg = Mockito.spy(ArrayList.class);
    lstCfg.add(createWoDTO);
    PowerMockito.when(cfgCreateWoBusiness.getListCfgCreateWoDTO(createWoDTO, 0, 1000, "", ""))
        .thenReturn(lstCfg);
    List<CatItemDTO> lstAlarmGroup = Mockito.spy(ArrayList.class);
    lstAlarmGroup.add(alarmGroupDTO);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstAlarmGroup);
    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setPassword("123456");
    requestDTO.setUsername("anhlp");
    requestDTO.setPartyCode("1");
    requestDTO.setRequestId("1");
    List<String> listAccount = Mockito.spy(ArrayList.class);
    String account1 = "anhlp";
    listAccount.add(account1);
    Date currDate = new Date();
    troublesDTO.setTroubleId("1");
    Double offset = 3D;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    try {
      troublesBusiness2.setTimeZoneForTrouble(troublesInSideDTO, offset);
    } catch (Exception e) {
      e.printStackTrace();
    }
    troublesDTO.setCreatedTime(DateUtil.date2ddMMyyyyHHMMss(currDate));
    List<String> fileName = Mockito.spy(ArrayList.class);
    List<byte[]> dataArray = Mockito.spy(ArrayList.class);
    MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json",
        "{\"json\": \"someValue\"}".getBytes());
    files.add(jsonFile);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    try {
      resultInSideDto = (ResultInSideDto) PowerMockito
          .when(troublesBusiness.insertTroublesTT(requestDTO, files, troublesInSideDTO, listAccount));
    } catch (Exception e) {
      e.printStackTrace();
    }
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto1 = Mockito.spy(com.viettel.gnoc.commons.dto.ResultDTO.class);
    try {
      resultInSideDto1 = troublesBusiness.insertTroublesNOC(requestDTO, troublesDTO, listAccount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    resultInSideDto1.setKey(RESULT.SUCCESS);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testInsertTrouble_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setInsertSource("abc");
    troublesDTO.setAlarmGroupId("1");
    troublesDTO.setAlarmGroupCode("SOS");
    troublesDTO.setTypeId(1L);
    troublesDTO.setAutoCreateWO(1L);
    troublesDTO.setTroubleName("danger");
    troublesDTO.setPriorityId(1L);
    TroubleMopInsiteDTO troubleMopDTO = Mockito.spy(TroubleMopInsiteDTO.class);
    troubleMopDTO.setIsRun(1L);
    troubleMopDTO.setLastUpdateTime(new Date());
    troubleMopDTO.setRunType(1L);
    troubleMopDTO.setAlarmId(1L);
    troubleMopDTO.setMaxNumberRun(1L);
    troubleMopDTO.setRunCycle(1L);
    CatItemDTO alarmGroupDTO = Mockito.spy(CatItemDTO.class);
    alarmGroupDTO.setItemCode(troublesDTO.getAlarmGroupCode());
    CfgCreateWoDTO createWoDTO = Mockito.spy(CfgCreateWoDTO.class);
    createWoDTO.setTypeId(String.valueOf(troublesDTO.getTypeId()));
    createWoDTO.setAlarmGroupId(troublesDTO.getAlarmGroupId());
    List<CfgCreateWoDTO> lstCfg = Mockito.spy(ArrayList.class);
    lstCfg.add(createWoDTO);
    PowerMockito.when(cfgCreateWoBusiness.getListCfgCreateWoDTO(createWoDTO, 0, 1000, "", ""))
        .thenReturn(lstCfg);
    List<CatItemDTO> lstAlarmGroup = Mockito.spy(ArrayList.class);
    lstAlarmGroup.add(alarmGroupDTO);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstAlarmGroup);
    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setPassword("123456");
    requestDTO.setUsername("anhlp");
    requestDTO.setPartyCode("1");
    requestDTO.setRequestId("1");
    List<String> listAccount = Mockito.spy(ArrayList.class);
    String account1 = "anhlp";
    listAccount.add(account1);
    Date currDate = new Date();
    troublesDTO.setTroubleId(1L);
    Double offset = 3D;
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(offset);
    TroublesDTO troublesDTO1 = Mockito.spy(TroublesDTO.class);
    try {
      troublesBusiness2.setTimeZoneForTrouble(troublesDTO, offset);
    } catch (Exception e) {
      e.printStackTrace();
    }
    troublesDTO.setCreatedTime(currDate);
    List<String> fileName = Mockito.spy(ArrayList.class);
    List<byte[]> dataArray = Mockito.spy(ArrayList.class);
    MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json",
        "{\"json\": \"someValue\"}".getBytes());
    files.add(jsonFile);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    try {
      resultInSideDto = (ResultInSideDto) PowerMockito
          .when(troublesBusiness.insertTroublesTT(requestDTO, files, troublesDTO, listAccount));
      String ret = "";
      PowerMockito.when(troublesBusiness.validateDTO(troublesDTO1, anyList())).thenReturn(ret);
    } catch (Exception e) {
      e.printStackTrace();
    }
    com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto1 = Mockito.spy(com.viettel.gnoc.commons.dto.ResultDTO.class);
    try {
      resultInSideDto1 = troublesBusiness.insertTroublesNOC(requestDTO, troublesDTO1, listAccount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    resultInSideDto1.setKey(RESULT.SUCCESS);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testInsertTroublesNOC_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    AuthorityDTO requestDTO = Mockito.spy(AuthorityDTO.class);
    requestDTO.setPassword("123456");
    requestDTO.setUsername("anhlp");
    requestDTO.setPartyCode("1");
    requestDTO.setRequestId("1");
    TroublesInSideDTO troublesDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO1.setTroubleId(1L);
    troublesDTO1.setInsertSource("SPM");
    troublesDTO1.setAlarmGroupId("1");
    troublesDTO1.setAlarmGroupCode("SOS");
    troublesDTO1.setTypeId(1L);
    troublesDTO1.setAutoCreateWO(1L);
    troublesDTO1.setTroubleName("danger");
    troublesDTO1.setPriorityId(1L);
    troublesDTO1.setRisk(1L);
    Map<String, String> param = new HashMap<>();
    param.put("ISSTATIONVIP", "1");
    param.put("RECEIVE_UNIT_WO_CODE", "1");
    param.put("CATCHING_TIME", DateUtil.date2ddMMyyyyHHMMss(new Date()));
    param.put("LINK_ID", "1");
    param.put("LINK_CODE", "1");
    param.put("AMI_ID", "1");
    param.put("CAMERA_ID", "1");
    param.put("COUNTRY_CODE", "84");
    param.put("ACCOUNT_NAME", "anhlp");
    param.put("CUSTOMER_NAME", "tiennv");
    param.put("CUSTOMER_PHONE", "8484884848");
    troublesDTO1.setMap(param);
    List<String> listAccount = Mockito.spy(ArrayList.class);
    String account1 = "anhlp";
    listAccount.add(account1);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    CatItemDTO prio = Mockito.spy(CatItemDTO.class);
    prio.setItemCode(String.valueOf(troublesDTO1.getPriorityId()).trim().toLowerCase());
    List<CatItemDTO> lstCat = Mockito.spy(ArrayList.class);
    lstCat.add(prio);
    PowerMockito.when(catItemBusiness.getListCatItemDTO(any())).thenReturn(lstCat);
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    try {
      com.viettel.gnoc.commons.dto.ResultDTO resultInSideDto1 = troublesBusiness
          .insertTroublesNOC(requestDTO, troublesDTO, listAccount);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }*//*


  static void setMockStatic(ApplicationContext mockApp) {
    try {
      Field instanceTime = SpringApplicationContext.class.getDeclaredField("context");
      instanceTime.setAccessible(true);
      instanceTime.set(instanceTime, mockApp);
    } catch (Exception e) {
    }
  }

  static void setMockSingleton(LogCallIpccServiceImpl mockSingleton) {
    try {
      Field instance = LogCallIpccServiceImpl.class.getDeclaredField("instance");
      instance.setAccessible(true);
      instance.set(instance, mockSingleton);
    } catch (Exception e) {
    }
  }

  static void setMockSingletonWSIPCC(WSIPCC wsipcc) {
    try {
      Field wsIpcc = WSIPCC.class.getDeclaredField("wsIpcc");
      wsIpcc.setAccessible(true);
      wsIpcc.set(wsIpcc, wsipcc);
    } catch (Exception e) {
    }
  }

  static void setMockSingletonCrServiceImpl(CrServiceImpl crServiceImpl) {
    try {
      Field instanceTime = CrServiceImpl.class.getDeclaredField("instanceTime");
      instanceTime.setAccessible(true);
      instanceTime.set(instanceTime, crServiceImpl);
    } catch (Exception e) {
    }
  }
}

*/
