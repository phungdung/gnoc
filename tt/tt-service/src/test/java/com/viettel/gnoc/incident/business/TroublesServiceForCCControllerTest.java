/*
package com.viettel.gnoc.incident.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.CfgInfoTtSpmDTO;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmDTO;
import com.viettel.gnoc.commons.dto.ConcaveDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.CfgInfoTtSpmRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.incident.dto.CommonDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroublesEntity;
import com.viettel.gnoc.incident.repository.ItAccountRepository;
import com.viettel.gnoc.incident.repository.ItSpmInfoRepository;
import com.viettel.gnoc.incident.repository.TroubleActionLogsRepository;
import com.viettel.gnoc.incident.repository.TroubleNodeRepository;
import com.viettel.gnoc.incident.repository.TroubleWireRepository;
import com.viettel.gnoc.incident.repository.TroubleWorklogRepository;
import com.viettel.gnoc.incident.repository.TroublesRepository;
import com.viettel.gnoc.incident.repository.TroublesServiceForCCRepository;
import com.viettel.gnoc.incident.utils.TroubleBccsUtils;
import com.viettel.gnoc.incident.utils.TroubleTktuUtils;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import com.viettel.gnoc.ws.provider.WSCxfInInterceptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

//import com.viettel.soc.spm.service.ResultDTO;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TroublesServiceForCCBusinessImpl.class, FileUtils.class, I18n.class,
    CommonImport.class, CommonExport.class, WebServiceContext.class, WSCxfInInterceptor.class,
    DataUtil.class, TroubleTktuUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class TroublesServiceForCCControllerTest {

  @InjectMocks
  TroublesServiceForCCBusinessImpl troublesServiceForCCBusiness;
  @Mock
  TroublesServiceForCCRepository troublesServiceForCCRepository;
  @Mock
  LanguageExchangeRepository languageExchangeRepository;
  @Mock
  TroublesRepository troublesRepository;
  @Mock
  CatItemRepository catItemRepository;
  @Mock
  CatLocationRepository catLocationRepository;
  @Mock
  CfgInfoTtSpmRepository cfgInfoTtSpmRepository;
  @Mock
  UnitRepository unitRepository;
  @Mock
  CommonRepository commonRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  TroubleWorklogRepository troubleWorklogRepository;
  @Mock
  TroubleNodeRepository troubleNodeRepository;
  @Mock
  TroubleWireRepository troubleWireRepository;
  @Mock
  ItAccountRepository itAccountRepository;
  @Mock
  ItSpmInfoRepository itSpmInfoRepository;
  @Mock
  TroubleActionLogsRepository troubleActionLogsRepository;
  @Mock
  MessagesRepository messagesRepository;
  @Mock
  TroublesBusiness troublesBusiness;
  @Mock
  WOCreateBusiness woCreateBusiness;
  @Mock
  TroubleTktuUtils troubleTktuUtils;
  @Mock
  WoServices woServices;
  @Mock
  TroubleBccsUtils troubleBccsUtils;
  @Mock
  UserBusiness userBusiness;


  @Test
  public void onRollBackTroubleForCC_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CommonDTO> lstComplaint = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    List<String> lst = Mockito.spy(ArrayList.class);
    List<TroublesInSideDTO> troublesDTOS = Mockito.spy(ArrayList.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    CommonDTO commonDTO = Mockito.spy(CommonDTO.class);
    String troubleCode = "test";
    lst.add(troubleCode);
    commonDTO.setComplaintId("1");
    commonDTO.setTroubleCode(lst);
    troublesDTO.setTroubleCode("test");
    troublesDTO.setTroubleId(1l);
    troublesDTOS.add(troublesDTO);
    resultInSideDto.setKey(RESULT.SUCCESS);
    lstComplaint.add(commonDTO);
    PowerMockito.when(troublesServiceForCCRepository
        .getTroubleDTOForRollback(anyList(), anyString(), anyString(), anyString()))
        .thenReturn(troublesDTOS);
    PowerMockito.when(troublesRepository.delete(any())).thenReturn(resultInSideDto);
    PowerMockito.when(troubleActionLogsRepository.deleteTroubleActionLogsByTroubleId(any()))
        .thenReturn(
            resultInSideDto);
    PowerMockito.when(troubleWorklogRepository.deleteTroubleWorklogByTroubleId(any())).thenReturn(
        resultInSideDto);
    PowerMockito.when(itSpmInfoRepository.deleteItSpmInfoByIncidentId(any())).thenReturn(
        resultInSideDto);
    PowerMockito.when(itAccountRepository.deleteItAccountByIncidentId(any())).thenReturn(
        resultInSideDto);
    List<com.viettel.gnoc.commons.dto.ResultDTO> data = troublesServiceForCCBusiness
        .onRollBackTroubleForCC(lstComplaint);
    Assert.assertNotNull(data);


  }

  @Test
  public void onRollBackTroubleForCC_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    List<CommonDTO> lstComplaint = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    List<String> lst = Mockito.spy(ArrayList.class);
    List<TroublesInSideDTO> troublesDTOS = Mockito.spy(ArrayList.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    CommonDTO commonDTO = Mockito.spy(CommonDTO.class);
    String troubleCode = "test";
    lst.add(troubleCode);
    commonDTO.setComplaintId("1");
    commonDTO.setTroubleCode(lst);
    troublesDTO.setTroubleCode("test");
    troublesDTO.setTroubleId(1l);
    troublesDTOS.add(troublesDTO);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("là bắt buộc nhập");
    List<ResultDTO> data = troublesServiceForCCBusiness
        .onRollBackTroubleForCC(lstComplaint);
    Assert.assertNotNull(data);


  }

  @Test
  public void onSearchCountForCC() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesDTO troublesDTO = PowerMockito.mock(TroublesDTO.class);
    PowerMockito.when(troublesServiceForCCRepository.onSearchCountForCC(any())).thenReturn(1);
    int data = troublesServiceForCCBusiness.onSearchCountForCC(troublesDTO);
    Assert.assertEquals(1, data);
  }

  @Test
  public void getTroubleInfoForCC_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    List<TroublesDTO> troublesDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    troublesDTO.setTroubleId("1");
    troublesDTOS.add(troublesDTO);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Bạn chưa truyền vào mã sự cố");
    PowerMockito.when(troublesServiceForCCRepository.getTroubleInfo(any()))
        .thenReturn(troublesDTOS);
    com.viettel.gnoc.commons.dto.ResultDTO data = troublesServiceForCCBusiness
        .getTroubleInfoForCC(troublesDTO);
    Assert.assertEquals(RESULT.FAIL, data.getKey());

  }

  @Test
  public void getTroubleInfoForCC_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    List<TroublesDTO> troublesDTOS = Mockito.spy(ArrayList.class);
    troublesDTO.setTroubleId("1");
    troublesDTO.setTroubleCode("test");
    troublesDTOS.add(troublesDTO);
    PowerMockito.when(troublesServiceForCCRepository.getTroubleInfo(any()))
        .thenReturn(troublesDTOS);
    com.viettel.gnoc.commons.dto.ResultDTO data = troublesServiceForCCBusiness
        .getTroubleInfoForCC(troublesDTO);
    Assert.assertEquals(RESULT.SUCCESS, data.getKey());

  }

  @Test
  public void onInsertTroubleForCC() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    List<String> sequenseTroubles = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> catItemDTOS = Mockito.spy(ArrayList.class);
    troublesDTO.setTroubleId("1");
    byte[][] arFileData = {{100, 100}};
    String[] arFileName = {"Test"};
    sequenseTroubles.add("seq");
    catItemDTO.setCategoryCode("TT_PRIORITY");
    catItemDTO.setItemId(1l);
    catItemDTO.setItemCode("test");
    catItemDTOS.add(catItemDTO);
    troublesDTO.setArFileData(arFileData);
    troublesDTO.setArFileName(arFileName);
    troublesDTO.setAutoCreateWO(1l);
    troublesDTO.setInsertSource("test");
    troublesDTO.setTroubleName("Name");
    troublesDTO.setPriorityId("1");
    troublesDTO.setBeginTroubleTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    troublesDTO.setDeferredReason("test");
    troublesDTO.setAffectedService("test");
    troublesDTO.setDescription("test");
    troublesDTO.setTypeId("1");
    troublesDTO.setSubCategoryId("1");
    troublesDTO.setLocationId("1");
    troublesDTO.setComplaintGroupId("1");
    troublesDTO.setTimeProcess("1");
    troublesDTO.setProcessingUserName("t");
    troublesDTO.setProcessingUserName("t");
    PowerMockito.when(catItemRepository.getListCatItemDTO(any())).thenReturn(catItemDTOS);
    PowerMockito.when(troublesRepository.getSequenseTroubles(any())).thenReturn(sequenseTroubles);
    ResultDTO data = troublesServiceForCCBusiness
        .onInsertTroubleForCC(troublesDTO);
    Assert.assertEquals(RESULT.FAIL, data.getKey());
  }

  @Test
  public void findTroubleById() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesEntity troublesDTO = Mockito.spy(TroublesEntity.class);
    troublesDTO.setTroubleId(1l);
    PowerMockito.when(troublesRepository.findTroublesById(any())).thenReturn(troublesDTO);
    TroublesInSideDTO data = troublesServiceForCCBusiness.findTroublesById(1l);
    Assert.assertEquals(troublesDTO.toDTO().getTroubleId(), data.getTroubleId());
  }

  @Test
  public void getSequenseTroubles() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> result = Mockito.spy(ArrayList.class);
    String test = "test";
    result.add(test);
    int[] a = {1};
    PowerMockito.when(troublesRepository.getSequenseTroubles(anyString(), anyInt()))
        .thenReturn(result);
    List<String> data = troublesServiceForCCBusiness.getSequenseTroubles(test, a);
    Assert.assertEquals(result, data);
  }

  @Test
  public void reassignTicketForCC_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setTroubleId(1l);
    troublesDTO.setTroubleCode("test");
    troublesDTO.setTimeProcess(1d);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date());
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setState(10l);
    troublesDTO.setCreatedTime(new Date());
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("là bắt buộc nhập");
    ResultDTO data = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO.toModelOutSide());
    Assert.assertEquals(RESULT.FAIL, data.getKey());
  }

  @Test
  public void reassignTicketForCC() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    UsersInsideDto u = Mockito.spy(UsersInsideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<UnitDTO> unitDTOS = Mockito.spy(ArrayList.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    PowerMockito.mockStatic(I18n.class);
    u.setIsNotReceiveMessage(2l);
    u.setUserId(99999l);
    u.setUsername("thanh");
    usersInsideDtos.add(u);
    datatable.setData(usersInsideDtos);
    troublesDTO.setTroubleId(1l);
    troublesDTO.setTroubleCode("test");
    troublesDTO.setBeginTroubleTime(new Date());
    troublesDTO.setTimeProcess(1d);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date());
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setState(10l);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setWoCode("test");
    unitDTO.setUnitId(1l);
    unitDTO.setUnitCode("test");
    unitDTO.setUnitName("test");
    unitDTOS.add(unitDTO);

    PowerMockito.when(unitRepository.getUnitByUnitDTO(any())).thenReturn(unitDTOS);
    PowerMockito
        .when(troublesRepository.getTroubleDTO(null, troublesDTO.getTroubleCode(), null, null,
            String.valueOf(troublesDTO.getComplaintId()),
            troublesDTO.getCreatedTimeFrom(), troublesDTO.getCreatedTimeTo()))
        .thenReturn(troublesDTO);
    PowerMockito.when(troubleActionLogsRepository.insertTroubleActionLogs(any())).thenReturn(
        resultInSideDto);
    PowerMockito.when((userRepository.getListUsersDTO(any()))).thenReturn(datatable);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Sự cố không tồn tại trên GNOC");
    ResultDTO data = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO.toModelOutSide());
    Assert.assertEquals(RESULT.FAIL, data.getKey());
  }

  @Test
  public void doReassignTicketForCC() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    PowerMockito.mockStatic(I18n.class);
    troublesDTO.setTroubleId(1l);
    troublesDTO.setTroubleCode("test");
    troublesDTO.setBeginTroubleTime(new Date());
    troublesDTO.setTimeProcess(1d);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date());
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setState(10l);
    troublesDTO.setCreatedTime(new Date());
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Sự cố không tồn tại trên GNOC");
    PowerMockito
        .when(troublesRepository.getTroubleDTO(null, troublesDTO.getTroubleCode(), null, null,
            String.valueOf(troublesDTO.getComplaintId()),
            troublesDTO.getCreatedTimeFrom(), troublesDTO.getCreatedTimeTo()))
        .thenReturn(troublesDTO);
    ResultInSideDto data = troublesServiceForCCBusiness.doReassignTicketForCC(troublesDTO);
  }

  @Test
  public void getListTroubleActionLog() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<TroubleActionLogsDTO> troubleActionLogsDTOS = PowerMockito.mock(ArrayList.class);
    PowerMockito.when((troublesServiceForCCRepository.getListTroubleActionLog(any())))
        .thenReturn(troubleActionLogsDTOS);
    List<TroubleActionLogsDTO> data = troublesServiceForCCBusiness.getListTroubleActionLog("test");
    Assert.assertEquals(troubleActionLogsDTOS, data);
  }

  @Test
  public void getListWorkLog() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<TroubleWorklogInsiteDTO> troubleWorklogDTOS = PowerMockito.mock(ArrayList.class);
    PowerMockito.when((troublesServiceForCCRepository
        .searchByConditionBean(anyList(), anyInt(), anyInt(), anyString(), anyString())))
        .thenReturn(troubleWorklogDTOS);
    List<TroubleWorklogInsiteDTO> data = troublesServiceForCCBusiness.getListWorkLog("test");
    Assert.assertEquals(troubleWorklogDTOS, data);
  }

  @Test
  public void getConcaveByTicket() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TroubleTktuUtils.class);
    PowerMockito.when((troubleTktuUtils.getConcaveByTicket(any()))).thenReturn("test");
    String data = troublesServiceForCCBusiness.getConcaveByTicket("test");
    Assert.assertEquals("test", data);
  }

  @Test
  public void sendTicketToTKTU() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDto = Mockito.spy(ResultDTO.class);
    TroublesInSideDTO troublesDTO = PowerMockito.mock(TroublesInSideDTO.class);
    resultDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(TroubleTktuUtils.class);
    PowerMockito.when((troubleTktuUtils.sendTicketToTKTU(any()))).thenReturn(resultDto);
    com.viettel.gnoc.commons.dto.ResultDTO data = troublesServiceForCCBusiness
        .sendTicketToTKTU(troublesDTO);
    Assert.assertEquals(resultDto, data);
  }

  @Test
  public void getConcaveByCellAndLocation() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ConcaveDTO> concaveDTOS = PowerMockito.mock(ArrayList.class);
    Map<String, String> map = Mockito.spy(Map.class);
    map.put("RADIUS_TKTU", "RADIUS_TKTU");
    List<String> lis = Mockito.spy(ArrayList.class);
    lis.add("test");
    PowerMockito.when((troublesServiceForCCRepository.getConfigProperty())).thenReturn(map);
    PowerMockito.mockStatic(TroubleTktuUtils.class);
    PowerMockito.when((troubleTktuUtils
        .getConcaveByCellAndLocation(anyList(), anyString(), anyString(), anyString())))
        .thenReturn(concaveDTOS);
    List<ConcaveDTO> data = troublesServiceForCCBusiness.getConcaveByCellAndLocation(lis, "", "");
    Assert.assertEquals(concaveDTOS.size(), data.size());

  }

  @Test
  public void getListUnitByTrouble() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<UnitDTO> unitDTOS = PowerMockito.mock(ArrayList.class);
    PowerMockito.when((troublesServiceForCCRepository.getListUnitByTrouble(any())))
        .thenReturn(unitDTOS);
    List<UnitDTO> data = troublesServiceForCCBusiness.getListUnitByTrouble("test");
    Assert.assertEquals(unitDTOS.size(), data.size());
  }

  @Test
  public void onUpdateTroubleFromTKTU_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    Date dt = new Date();
    troublesDTO.setTroubleId(1l);
    troublesDTO.setTroubleCode("test");
    troublesDTO.setBeginTroubleTime(new Date());
    troublesDTO.setTimeProcess(1d);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date());
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setState(10l);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setWoCode("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setStateName("DEFERRED");
    troublesDTO.setEstimateTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setPriorityCode("TT_Minor");
    troublesDTO.setConcave("test");
    troublesDTO.setCellService("test");
    troublesDTO.setLongitude("103");
    troublesDTO.setLatitude("8");
    troublesDTO.setWorkArround("test");
    troublesDTO.setCreateUnitId(1l);
    PowerMockito.when((troublesRepository
        .getTroubleDTO(null, troublesDTO.getTroubleCode(), null, null, null, null, null)))
        .thenReturn(troublesDTO);
    ResultDTO data = troublesServiceForCCBusiness.onUpdateTroubleFromTKTU(troublesDTO);
    Assert.assertEquals(RESULT.SUCCESS, data.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_02() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    UsersInsideDto u = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    u.setIsNotReceiveMessage(2l);
    u.setUserId(99999l);
    u.setUsername("thanh");
    usersInsideDtos.add(u);
    datatable.setData(usersInsideDtos);
    Date dt = new Date();
    troublesDTO.setTroubleId(1l);
    troublesDTO.setEndTroubleTime(new Date());
    troublesDTO.setReasonLv1Id("test");
    troublesDTO.setReasonLv2Id("test");
    troublesDTO.setReasonLv1Name("test");
    troublesDTO.setReasonLv2Name("test");
    troublesDTO.setReasonLv3Id("test");
    troublesDTO.setReasonLv3Name("test");
    troublesDTO.setRootCause("test");
    troublesDTO.setTroubleCode("test");
    troublesDTO.setWorkArround("test");
    troublesDTO.setTotalPendingTimeGnoc("test");
    troublesDTO.setTotalProcessTimeGnoc("test");
    troublesDTO.setEvaluateGnoc("test");
    troublesDTO.setBeginTroubleTime(new Date());
    troublesDTO.setTimeProcess(1d);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date());
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setState(7l);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setWoCode("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setStateName("CLOSED");
    troublesDTO.setEstimateTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setPriorityCode("TT_Minor");
    troublesDTO.setConcave("test");
    troublesDTO.setCellService("test");
    troublesDTO.setLongitude("103");
    troublesDTO.setLatitude("8");
    troublesDTO.setWorkArround("test");
    troublesDTO.setCreateUnitId(1l);
    troublesDTO.setAutoClose(1l);
    PowerMockito.when((troublesRepository
        .getTroubleDTO(null, troublesDTO.getTroubleCode(), null, null, null, null, null)))
        .thenReturn(troublesDTO);
    PowerMockito.when(troublesBusiness.onUpdateTrouble(any(), anyBoolean()))
        .thenReturn(resultInSideDto);
    PowerMockito.when((userRepository.getListUsersDTO(any()))).thenReturn(datatable);
    com.viettel.gnoc.commons.dto.ResultDTO data = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(troublesDTO);
    Assert.assertEquals(RESULT.SUCCESS, data.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_03() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    UsersInsideDto u = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    u.setIsNotReceiveMessage(2l);
    u.setUserId(99999l);
    u.setUsername("thanh");
    usersInsideDtos.add(u);
    datatable.setData(usersInsideDtos);
    Date dt = new Date();
    troublesDTO.setTroubleId(1l);
    troublesDTO.setTroubleCode("test");
    troublesDTO.setBeginTroubleTime(new Date());
    troublesDTO.setTimeProcess(1d);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date());
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setState(10l);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setWoCode("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setStateName("QUEUE");
    troublesDTO.setEstimateTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setPriorityCode("TT_Minor");
    troublesDTO.setConcave("test");
    troublesDTO.setCellService("test");
    troublesDTO.setLongitude("103");
    troublesDTO.setLatitude("8");
    troublesDTO.setWorkArround("test");
    troublesDTO.setCreateUnitId(1l);
    troublesDTO.setAutoClose(1l);
    PowerMockito.when((troublesRepository
        .getTroubleDTO(null, troublesDTO.getTroubleCode(), null, null, null, null, null)))
        .thenReturn(troublesDTO);
    PowerMockito.when(troublesBusiness.onUpdateTrouble(any(), anyBoolean()))
        .thenReturn(resultInSideDto);
    PowerMockito.when((userRepository.getListUsersDTO(any()))).thenReturn(datatable);
    com.viettel.gnoc.commons.dto.ResultDTO data = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(troublesDTO);
    Assert.assertEquals(RESULT.SUCCESS, data.getKey());

  }

  @Test
  public void onUpdateTroubleFromWo_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    UsersInsideDto u = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    u.setIsNotReceiveMessage(2l);
    u.setUserId(99999l);
    u.setUsername("thanh");
    usersInsideDtos.add(u);
    datatable.setData(usersInsideDtos);
    Date dt = new Date();
    troublesDTO.setTroubleId(1l);
    troublesDTO.setTroubleCode("test");
    troublesDTO.setGroupSolution(1l);
    troublesDTO.setNumPending(1l);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setReceiveUserId(1l);
    troublesDTO.setState(7l);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setProcessingUserPhone("test");
    troublesDTO.setWoCode("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setStateName("DEFERRED");
    troublesDTO.setDeferredReason("test");
    troublesDTO.setDeferType(3l);
    troublesDTO.setEstimateTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setPriorityCode("TT_Minor");
    troublesDTO.setConcave("test");
    troublesDTO.setCellService("test");
    troublesDTO.setLongitude("103");
    troublesDTO.setLatitude("8");
    troublesDTO.setWorkArround("test");
    troublesDTO.setCreateUnitId(1l);
    troublesDTO.setAutoClose(1l);
    PowerMockito.when((troublesRepository
        .getTroubleDTO(null, troublesDTO.getTroubleCode(), null, null, null, null, null)))
        .thenReturn(troublesDTO);
    ResultDTO data = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(troublesDTO.toModelOutSide());
    Assert.assertEquals(RESULT.SUCCESS, data.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_02() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    UsersInsideDto u = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    u.setIsNotReceiveMessage(2l);
    u.setUserId(99999l);
    u.setUsername("thanh");
    usersInsideDtos.add(u);
    datatable.setData(usersInsideDtos);
    Date dt = new Date();
    troublesDTO.setTroubleId(1l);
    troublesDTO.setEndTroubleTime(new Date());
    troublesDTO.setReasonLv1Id("test");
    troublesDTO.setReasonLv2Id("test");
    troublesDTO.setReasonLv1Name("test");
    troublesDTO.setReasonLv2Name("test");
    troublesDTO.setReasonLv3Id("test");
    troublesDTO.setReasonLv3Name("test");
    troublesDTO.setRootCause("test");
    troublesDTO.setTroubleCode("test");
    troublesDTO.setWorkArround("test");
    troublesDTO.setTotalPendingTimeGnoc("test");
    troublesDTO.setTotalProcessTimeGnoc("test");
    troublesDTO.setEvaluateGnoc("test");
    troublesDTO.setTroubleCode("test");
    troublesDTO.setGroupSolution(1l);
    troublesDTO.setNumPending(1l);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setReceiveUserId(1l);
    troublesDTO.setState(7l);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setProcessingUserPhone("test");
    troublesDTO.setWoCode("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setStateName("CLOSED");
    troublesDTO.setDeferredReason("test");
    troublesDTO.setDeferType(3l);
    troublesDTO.setEstimateTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setPriorityCode("TT_Minor");
    troublesDTO.setConcave("test");
    troublesDTO.setCellService("test");
    troublesDTO.setLongitude("103");
    troublesDTO.setLatitude("8");
    troublesDTO.setWorkArround("test");
    troublesDTO.setCreateUnitId(1l);
    troublesDTO.setAutoClose(1l);
    PowerMockito.when((troublesRepository
        .getTroubleDTO(null, troublesDTO.getTroubleCode(), null, null, null, null, null)))
        .thenReturn(troublesDTO);
    PowerMockito.when(troublesBusiness.onUpdateTrouble(any(), anyBoolean()))
        .thenReturn(resultInSideDto);
    PowerMockito.when((userRepository.getListUsersDTO(any()))).thenReturn(datatable);
    ResultDTO data = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(troublesDTO.toModelOutSide());
    Assert.assertEquals(RESULT.SUCCESS, data.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_03() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    UsersInsideDto u = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    u.setIsNotReceiveMessage(2l);
    u.setUserId(99999l);
    u.setUsername("thanh");
    usersInsideDtos.add(u);
    datatable.setData(usersInsideDtos);
    Date dt = new Date();
    troublesDTO.setTroubleId(1l);
    troublesDTO.setEndTroubleTime(new Date());
    troublesDTO.setReasonLv1Id("test");
    troublesDTO.setReasonLv2Id("test");
    troublesDTO.setReasonLv1Name("test");
    troublesDTO.setReasonLv2Name("test");
    troublesDTO.setReasonLv3Id("test");
    troublesDTO.setReasonLv3Name("test");
    troublesDTO.setRootCause("test");
    troublesDTO.setTroubleCode("test");
    troublesDTO.setWorkArround("test");
    troublesDTO.setTotalPendingTimeGnoc("test");
    troublesDTO.setTotalProcessTimeGnoc("test");
    troublesDTO.setEvaluateGnoc("test");
    troublesDTO.setTroubleCode("test");
    troublesDTO.setGroupSolution(1l);
    troublesDTO.setNumPending(1l);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setReceiveUserId(1l);
    troublesDTO.setState(7l);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setProcessingUserPhone("test");
    troublesDTO.setWoCode("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setStateName("QUEUE");
    troublesDTO.setDeferredReason("test");
    troublesDTO.setDeferType(3l);
    troublesDTO.setEstimateTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setPriorityCode("TT_Minor");
    troublesDTO.setConcave("test");
    troublesDTO.setCellService("test");
    troublesDTO.setLongitude("103");
    troublesDTO.setLatitude("8");
    troublesDTO.setWorkArround("test");
    troublesDTO.setCreateUnitId(1l);
    troublesDTO.setAutoClose(1l);
    PowerMockito.when((troublesRepository
        .getTroubleDTO(null, troublesDTO.getTroubleCode(), null, null, null, null, null)))
        .thenReturn(troublesDTO);
    PowerMockito.when(troublesBusiness.onUpdateTrouble(any(), anyBoolean()))
        .thenReturn(resultInSideDto);
    PowerMockito.when((userRepository.getListUsersDTO(any()))).thenReturn(datatable);
    ResultDTO data = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(troublesDTO.toModelOutSide());
    Assert.assertEquals(RESULT.SUCCESS, data.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_04() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    PowerMockito.mockStatic(I18n.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    catItemDTO.setItemCode("test");
    catItemDTO.setItemId(1l);
    catItemDTO.setItemName("test");
    resultInSideDto.setKey(RESULT.SUCCESS);
    Date dt = new Date();
    troublesDTO.setTroubleId(1l);
    troublesDTO.setEndTroubleTime(new Date());
    troublesDTO.setReasonLv1Id("test");
    troublesDTO.setReasonLv2Id("test");
    troublesDTO.setReasonLv1Name("test");
    troublesDTO.setReasonLv2Name("test");
    troublesDTO.setReasonLv3Id("test");
    troublesDTO.setReasonLv3Name("test");
    troublesDTO.setRootCause("test");
    troublesDTO.setTroubleCode("test");
    troublesDTO.setWorkArround("test");
    troublesDTO.setTypeId(1l);
    troublesDTO.setTotalPendingTimeGnoc("test");
    troublesDTO.setTotalProcessTimeGnoc("test");
    troublesDTO.setEvaluateGnoc("test");
    troublesDTO.setTroubleCode("test");
    troublesDTO.setGroupSolution(1l);
    troublesDTO.setNumPending(1l);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setReceiveUserId(1l);
    troublesDTO.setState(7l);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setProcessingUserPhone("test");
    troublesDTO.setWoCode("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setStateName("UPDATE");
    troublesDTO.setDeferredReason("test");
    troublesDTO.setDeferType(3l);
    troublesDTO.setEstimateTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setPriorityCode("TT_Minor");
    troublesDTO.setConcave("test");
    troublesDTO.setCellService("test");
    troublesDTO.setLongitude("103");
    troublesDTO.setLatitude("8");
    troublesDTO.setWorkArround("test");
    troublesDTO.setCreateUnitId(1l);
    troublesDTO.setAutoClose(1l);
    PowerMockito.when((troublesRepository
        .getTroubleDTO(null, troublesDTO.getTroubleCode(), null, null, null, null, null)))
        .thenReturn(troublesDTO);
    PowerMockito.when(troublesBusiness.onUpdateTrouble(any(), anyBoolean()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(troublesBusiness.findTroublesById(Long.valueOf(troublesDTO.getTroubleId())))
        .thenReturn(troublesDTO);
    PowerMockito.when(catItemRepository
        .getCatItemById(Long.valueOf(troublesDTO.getTypeId()))).thenReturn(catItemDTO);
    PowerMockito.when(catItemRepository.getCatItemById(Long.valueOf(troublesDTO.getState())))
        .thenReturn(catItemDTO);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Cập nhật");
    ResultDTO data = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(troublesDTO.toModelOutSide());
    Assert.assertEquals(RESULT.SUCCESS, data.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_05() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    UsersInsideDto u = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    u.setIsNotReceiveMessage(2l);
    u.setUserId(99999l);
    u.setUsername("thanh");
    usersInsideDtos.add(u);
    datatable.setData(usersInsideDtos);
    Map<String, String> map = Mockito.spy(Map.class);
    map.put("TT.TIME.PER.WO.TIME", "TT.TIME.PER.WO.TIME");
    catItemDTO.setItemCode("test");
    catItemDTO.setItemId(1l);
    catItemDTO.setItemName("test");
    resultInSideDto.setKey(RESULT.SUCCESS);
    Date dt = new Date();
    troublesDTO.setTroubleId(1l);
    troublesDTO.setTroubleCode("test");
    troublesDTO.setGroupSolution(1l);
    troublesDTO.setNumPending(1l);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setReceiveUserId(1l);
    troublesDTO.setState(7l);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setProcessingUserPhone("test");
    troublesDTO.setWoCode("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setTimeProcess(12d);
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setStateName("IS_HELP");
    troublesDTO.setDeferredReason("test");
    troublesDTO.setDeferType(3l);
    troublesDTO.setEstimateTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setPriorityCode("TT_Minor");
    troublesDTO.setConcave("test");
    troublesDTO.setCellService("test");
    troublesDTO.setLongitude("103");
    troublesDTO.setLatitude("8");
    troublesDTO.setWorkArround("test");
    troublesDTO.setCreateUnitId(1l);
    troublesDTO.setAutoClose(1l);
    PowerMockito.when((troublesRepository
        .getTroubleDTO(null, troublesDTO.getTroubleCode(), null, null, null, null, null)))
        .thenReturn(troublesDTO);
    PowerMockito.when(troublesBusiness.findTroublesById(Long.valueOf(troublesDTO.getTroubleId())))
        .thenReturn(troublesDTO);
    PowerMockito.when((troublesServiceForCCRepository.getConfigProperty())).thenReturn(map);
    PowerMockito.when((userRepository.getListUsersDTO(any()))).thenReturn(datatable);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Tích help sự cố từ Wo");
    ResultDTO data = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(troublesDTO.toModelOutSide());
    Assert.assertEquals(RESULT.SUCCESS, data.getKey());
  }

  @Test
  public void getListWoLog() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setTroubleCode("test");
    troublesDTO.setTroubleId(1l);
    PowerMockito.when(troublesRepository
        .getTroubleDTO(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
            anyString())).thenReturn(troublesDTO);
    List<WoHistoryDTO> data = troublesServiceForCCBusiness.getListWoLog("test");
    Assert.assertNotNull(data);
  }

  @Test
  public void doOnInsertTroubleForCC() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    ResultDTO resultDto = Mockito.spy(ResultDTO.class);
    UsersEntity users = Mockito.spy(UsersEntity.class);
    List<InfraDeviceDTO> lst = Mockito.spy(ArrayList.class);
    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    UsersInsideDto u = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.mockStatic(I18n.class);
    u.setIsNotReceiveMessage(2l);
    u.setUserId(99999l);
    u.setUsername("thanh");
    usersInsideDtos.add(u);
    datatable.setData(usersInsideDtos);
    List<String> list = new ArrayList<>();
    list.add("test");
    infraDeviceDTO.setDeviceId("1");
    lst.add(infraDeviceDTO);
    u.setIsNotReceiveMessage(2l);
    u.setUserId(99999l);
    u.setUsername("thanh");
    Map<String, String> map = new HashMap<>();
    map.put("WO.TT.CREATE_PERSON.ID", "1");
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1l);
    resultDto.setKey(RESULT.SUCCESS);
    resultDto.setId("1");
    troublesDTO.setTroubleCode("1");
    troublesDTO.setTroubleId(1l);
    troublesDTO.setAutoCreateWO(2l);
    troublesDTO.setTypeName("SPM_PAKH_CDBR");
    //troublesDTO.setLstNode("")
    //troublesDTO.setCreateUserId(1l);
    troublesDTO.setInsertSource("BCCS");
    troublesDTO.setStateName("WAITING RECEIVE");
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setLstNode(lst);
    troublesDTO.setServiceType(1l);
    troublesDTO.setTelServiceId("1");
    troublesDTO.setInfraType("CCN");
    troublesDTO.setIsCcResult("test");
    troublesDTO.setProductCode("test");
    troublesDTO.setCustomerPhone("test");
    troublesDTO.setWoContent("test");
    troublesDTO.setListAccount(list);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setSpmCode("test");
    troublesDTO.setSpmId("1");
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setCreateUnitId(1l);
    troublesDTO.setCreateUserId(1l);
    troublesDTO.setReceiveUnitId(1l);
    PowerMockito.when((woCreateBusiness.createWOMobile(any(), any(), any()))).thenReturn(
        resultDto);
    PowerMockito.when(troublesRepository.getLocationNameFull(anyString())).thenReturn("test");
    PowerMockito.when((troublesServiceForCCRepository.getConfigProperty())).thenReturn(map);
    PowerMockito.when(userRepository.getUserByUserId(any())).thenReturn(users);
    PowerMockito.when(troublesRepository.insertTroubles(any())).thenReturn(resultInSideDto);
    PowerMockito.when((userRepository.getListUsersDTO(any()))).thenReturn(datatable);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Thêm mới");
    ResultDTO data = troublesServiceForCCBusiness.doOnInsertTroubleForCC(troublesDTO);
    Assert.assertEquals(RESULT.SUCCESS, data.getKey());
  }

  @Test
  public void validateDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    CfgUnitTtSpmDTO cfgUnitTtSpmDTO = Mockito.spy(CfgUnitTtSpmDTO.class);
    CfgInfoTtSpmDTO cfgInfoTtSpmDTO = new CfgInfoTtSpmDTO();
    List<CfgInfoTtSpmDTO> cfgInfoTtSpmDTOS = new ArrayList<>();
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    List<UnitDTO> unitDTOS = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1l);
    usersInsideDto.setUnitId(1l);
    usersInsideDto.setUsername("test");
    unitDTO.setUnitId(1l);
    unitDTO.setUnitName("test");
    unitDTOS.add(unitDTO);
    cfgInfoTtSpmDTO.setAlarmGroupId("1");
    cfgInfoTtSpmDTO.setSubCategoryId("1");
    cfgInfoTtSpmDTO.setTroubleName("test");
    cfgUnitTtSpmDTO.setUnitId("1");
    cfgInfoTtSpmDTOS.add(cfgInfoTtSpmDTO);
    List<String> lis = new ArrayList<>();
    lis.add("test");
    catLocationDTO.setLocationId("1");
    catLocationDTO.setLocationNameFull("VN");
    CatItemDTO catItemDTO = new CatItemDTO();
    CatItemDTO catItemDTO1 = new CatItemDTO();
    catItemDTO.setCategoryCode("TT_PRIORITY");
    catItemDTO1.setCategoryCode("PT_TYPE");
    catItemDTO1.setItemCode("SPM_PAKH_CDBR");
    catItemDTO1.setItemId(1l);
    catItemDTO1.setItemValue("1");
    catItemDTO1.setDescription("test");
    catItemDTO1.setParentItemId(1l);
    List<CatItemDTO> catItemDTOS = new ArrayList<CatItemDTO>();
    List<CatItemDTO> catItemDTOS1 = new ArrayList<CatItemDTO>();
    InfraDeviceDTO infraDeviceDTO = new InfraDeviceDTO();
    infraDeviceDTO.setDeviceId("1");
    infraDeviceDTO.setDeviceCode("test");
    List<InfraDeviceDTO> infraDeviceDTOS = new ArrayList<>();
    infraDeviceDTOS.add(infraDeviceDTO);
    catItemDTOS.add(catItemDTO);
    catItemDTOS1.add(catItemDTO1);
    Date dt = new Date();
    troublesDTO.setTroubleId(1l);
    troublesDTO.setEndTroubleTime(new Date());
    troublesDTO.setReasonLv1Id("test");
    troublesDTO.setReasonLv2Id("test");
    troublesDTO.setReasonLv1Name("test");
    troublesDTO.setReasonLv2Name("test");
    troublesDTO.setReasonLv3Id("test");
    troublesDTO.setReasonLv3Name("test");
    troublesDTO.setRootCause("test");
    troublesDTO.setTroubleCode("test");
    troublesDTO.setWorkArround("test");
    troublesDTO.setTypeId(1l);
    troublesDTO.setTotalPendingTimeGnoc("test");
    troublesDTO.setTotalProcessTimeGnoc("test");
    troublesDTO.setEvaluateGnoc("test");
    troublesDTO.setTroubleCode("test");
    troublesDTO.setGroupSolution(1l);
    troublesDTO.setNumPending(1l);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setReceiveUserId(1l);
    troublesDTO.setState(7l);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setProcessingUserPhone("test");
    troublesDTO.setWoCode("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setStateName("UPDATE");
    troublesDTO.setDeferredReason("test");
    troublesDTO.setDeferType(3l);
    troublesDTO.setEstimateTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setPriorityCode("TT_Minor");
    troublesDTO.setConcave("test");
    troublesDTO.setCellService("test");
    troublesDTO.setLongitude("103");
    troublesDTO.setLatitude("8");
    troublesDTO.setWorkArround("test");
    troublesDTO.setCreateUnitId(1l);
    troublesDTO.setAutoClose(1l);
    troublesDTO.setTroubleName("test");
    troublesDTO.setPriorityId(1l);
    troublesDTO.setBeginTroubleTime(new Date());
    troublesDTO.setDescription("test");
    troublesDTO.setTypeName("SPM_PAKH_CDBR");
    troublesDTO.setAutoCreateWO(1l);
    troublesDTO.setLocationId(1l);
    troublesDTO.setComplaintGroupId("1");
    troublesDTO.setTimeProcess(1d);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setAlarmGroupCode("test");
    troublesDTO.setIsCcResult("test");
    troublesDTO.setTechnology("test");
    troublesDTO.setListAccount(lis);
    troublesDTO.setServiceType(1l);
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setTypeUnit(1l);
    troublesDTO.setReceiveUnitId(1l);
    troublesDTO.setCreateUserName("test");
    troublesDTO.setLocationId(1l);
    troublesDTO.setNationCode("test");
    troublesDTO.setTelServiceId("1");

    PowerMockito.when(catItemRepository.getListCatItemDTO(any())).thenReturn(catItemDTOS)
        .thenReturn(catItemDTOS1);
    PowerMockito.when(catLocationRepository
        .getLocationByCode(String.valueOf(troublesDTO.getLocationId()), null,
            troublesDTO.getNationCode())).thenReturn(catLocationDTO);
    PowerMockito.when(cfgInfoTtSpmRepository
        .getListCfgInfoTtSpmDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(cfgInfoTtSpmDTOS);
    PowerMockito.when(unitRepository.getUnitByUnitDTO(any())).thenReturn(unitDTOS);
    PowerMockito.when(commonRepository.getUserByUserName(any())).thenReturn(usersInsideDto);
    String data = troublesServiceForCCBusiness.validateDTO(troublesDTO.toModelOutSide());
    Assert.assertNotNull(data);
  }

  @Test
  public void validateDTO_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    CfgUnitTtSpmDTO cfgUnitTtSpmDTO = Mockito.spy(CfgUnitTtSpmDTO.class);
    CfgInfoTtSpmDTO cfgInfoTtSpmDTO = new CfgInfoTtSpmDTO();
    List<CfgInfoTtSpmDTO> cfgInfoTtSpmDTOS = new ArrayList<>();
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    List<UnitDTO> unitDTOS = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1l);
    usersInsideDto.setUnitId(1l);
    usersInsideDto.setUsername("test");
    unitDTO.setUnitId(1l);
    unitDTO.setUnitName("test");
    unitDTOS.add(unitDTO);
    cfgInfoTtSpmDTO.setAlarmGroupId("1");
    cfgInfoTtSpmDTO.setSubCategoryId("1");
    cfgInfoTtSpmDTO.setTroubleName("test");
    cfgUnitTtSpmDTO.setUnitId("1");
    cfgInfoTtSpmDTOS.add(cfgInfoTtSpmDTO);
    List<String> lis = new ArrayList<>();
    lis.add("test");
    catLocationDTO.setLocationId("1");
    catLocationDTO.setLocationNameFull("VN");
    CatItemDTO catItemDTO = new CatItemDTO();
    CatItemDTO catItemDTO1 = new CatItemDTO();
    catItemDTO.setCategoryCode("TT_PRIORITY");
    catItemDTO1.setCategoryCode("PT_TYPE");
    catItemDTO1.setItemCode("SPM_PAKH_CDBR");
    catItemDTO1.setItemId(1l);
    catItemDTO1.setItemValue("1");
    catItemDTO1.setDescription("test");
    catItemDTO1.setParentItemId(1l);
    List<CatItemDTO> catItemDTOS = new ArrayList<CatItemDTO>();
    List<CatItemDTO> catItemDTOS1 = new ArrayList<CatItemDTO>();
    InfraDeviceDTO infraDeviceDTO = new InfraDeviceDTO();
    infraDeviceDTO.setDeviceId("1");
    infraDeviceDTO.setDeviceCode("test");
    List<InfraDeviceDTO> infraDeviceDTOS = new ArrayList<>();
    infraDeviceDTOS.add(infraDeviceDTO);
    catItemDTOS.add(catItemDTO);
    catItemDTOS1.add(catItemDTO1);
    Date dt = new Date();
    troublesDTO.setTroubleId(1l);
    troublesDTO.setEndTroubleTime(new Date());
    troublesDTO.setReasonLv1Id("test");
    troublesDTO.setReasonLv2Id("test");
    troublesDTO.setReasonLv1Name("test");
    troublesDTO.setReasonLv2Name("test");
    troublesDTO.setReasonLv3Id("test");
    troublesDTO.setReasonLv3Name("test");
    troublesDTO.setRootCause("test");
    troublesDTO.setTroubleCode("test");
    troublesDTO.setWorkArround("test");
    troublesDTO.setTypeId(1l);
    troublesDTO.setTotalPendingTimeGnoc("test");
    troublesDTO.setTotalProcessTimeGnoc("test");
    troublesDTO.setEvaluateGnoc("test");
    troublesDTO.setTroubleCode("test");
    troublesDTO.setGroupSolution(1l);
    troublesDTO.setNumPending(1l);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setProcessingUnitName("test");
    troublesDTO.setRejectReason("test");
    troublesDTO.setDeferredTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setReceiveUserId(1l);
    troublesDTO.setState(7l);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setProcessingUserPhone("test");
    troublesDTO.setWoCode("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setStateName("UPDATE");
    troublesDTO.setDeferredReason("test");
    troublesDTO.setDeferType(3l);
    troublesDTO.setEstimateTime(new Date(dt.getTime() + (1000 * 60 * 60 * 24)));
    troublesDTO.setPriorityCode("TT_Minor");
    troublesDTO.setConcave("test");
    troublesDTO.setCellService("test");
    troublesDTO.setLongitude("103");
    troublesDTO.setLatitude("8");
    troublesDTO.setWorkArround("test");
    troublesDTO.setCreateUnitId(1l);
    troublesDTO.setAutoClose(1l);
    troublesDTO.setTroubleName("test");
    troublesDTO.setPriorityId(1l);
    troublesDTO.setBeginTroubleTime(new Date());
    troublesDTO.setDescription("test");
    troublesDTO.setTypeName("SPM_PAKH_CDBR");
    troublesDTO.setAutoCreateWO(1l);
    troublesDTO.setLocationId(1l);
    troublesDTO.setComplaintGroupId("1");
    troublesDTO.setTimeProcess(1d);
    troublesDTO.setProcessingUserName("test");
    troublesDTO.setAlarmGroupCode("test");
    troublesDTO.setIsCcResult("test");
    troublesDTO.setTechnology("test");
    troublesDTO.setListAccount(lis);
    troublesDTO.setServiceType(1l);
    troublesDTO.setTypeUnit(1l);
    troublesDTO.setCreateUserName("test");
    troublesDTO.setLocationId(1l);
    troublesDTO.setNationCode("test");
    troublesDTO.setTelServiceId("1");
    troublesDTO.setSubCategoryId(1l);
    troublesDTO.setLstNode(infraDeviceDTOS);
    PowerMockito.when(catItemRepository.getListCatItemDTO(any())).thenReturn(catItemDTOS)
        .thenReturn(catItemDTOS1);
    PowerMockito.when(catLocationRepository
        .getLocationByCode(String.valueOf(troublesDTO.getLocationId()), null,
            troublesDTO.getNationCode())).thenReturn(catLocationDTO);
    PowerMockito.when(cfgInfoTtSpmRepository
        .getListCfgInfoTtSpmDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(cfgInfoTtSpmDTOS);
    PowerMockito.when(troublesRepository
        .getUnitByLocation(String.valueOf(troublesDTO.getLocationId()),
            String.valueOf(troublesDTO.getTypeId()), String.valueOf(troublesDTO.getTypeUnit())))
        .thenReturn(cfgUnitTtSpmDTO);
    PowerMockito.when(unitRepository.getUnitByUnitDTO(any())).thenReturn(unitDTOS);
    PowerMockito.when(commonRepository.getUserByUserName(any())).thenReturn(usersInsideDto);
    String data = troublesServiceForCCBusiness.validateDTO(troublesDTO.toModelOutSide());
    Assert.assertNotNull(data);
  }

  @Test
  public void validateTKTU_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setLocationId(1l);
    troublesDTO.setStateName("WAITING RECEIVE");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Sự cố không tồn tại trên GNOC");
    ResultInSideDto data = troublesServiceForCCBusiness.validateTKTU(troublesDTO, troublesDTO);
    Assert.assertEquals(RESULT.FAIL, data.getKey());
  }

  @Test
  public void validateTKTU_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setLocationId(1l);
    troublesDTO.setTroubleId(1l);
    troublesDTO.setStateName("WAITING RECEIVE");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("là bắt buộc nhập");
    ResultInSideDto data = troublesServiceForCCBusiness.validateTKTU(troublesDTO, troublesDTO);
    Assert.assertEquals(RESULT.FAIL, data.getKey());
  }

  @Test
  public void validateTKTU_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setLocationId(1l);
    troublesDTO.setTroubleId(1l);
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setStateName("WAITING RECEIVE");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("là bắt buộc nhập");
    ResultInSideDto data = troublesServiceForCCBusiness.validateTKTU(troublesDTO, troublesDTO);
    Assert.assertEquals(RESULT.FAIL, data.getKey());
  }

  @Test
  public void validateTKTU_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setLocationId(1l);
    troublesDTO.setTroubleId(1l);
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setReceiveUserName("test");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("là bắt buộc nhập");
    ResultInSideDto data = troublesServiceForCCBusiness.validateTKTU(troublesDTO, troublesDTO);
    Assert.assertEquals(RESULT.FAIL, data.getKey());
  }

  @Test
  public void validateTKTU_05() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setLocationId(1l);
    troublesDTO.setTroubleId(1l);
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setStateName("test");
    troublesDTO.setReceiveUserName("test");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("là bắt buộc nhập");
    ResultInSideDto data = troublesServiceForCCBusiness.validateTKTU(troublesDTO, troublesDTO);
    Assert.assertEquals(RESULT.FAIL, data.getKey());
  }

  @Test
  public void validateTKTU_06() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setLocationId(1l);
    troublesDTO.setTroubleId(1l);
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setStateName("DEFERRED");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setWorkLog("test");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("là bắt buộc nhập");
    ResultInSideDto data = troublesServiceForCCBusiness.validateTKTU(troublesDTO, troublesDTO);
    Assert.assertEquals(RESULT.FAIL, data.getKey());
  }

  @Test
  public void validateTKTU_07() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    PowerMockito.mockStatic(I18n.class);
    troublesDTO.setLocationId(1l);
    troublesDTO.setTroubleId(1l);
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setStateName("DEFERRED");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setConcave("test");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("là bắt buộc nhập");
    ResultInSideDto data = troublesServiceForCCBusiness.validateTKTU(troublesDTO, troublesDTO);
    Assert.assertEquals(RESULT.FAIL, data.getKey());
  }

  @Test
  public void validateTKTU_08() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setLocationId(1l);
    troublesDTO.setTroubleId(1l);
    troublesDTO.setReceiveUnitName("test");
    troublesDTO.setStateName("DEFERRED");
    troublesDTO.setReceiveUserName("test");
    troublesDTO.setWorkLog("test");
    troublesDTO.setConcave("test");
    troublesDTO.setEstimateTime(new Date());
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("là bắt buộc nhập");
    ResultInSideDto data = troublesServiceForCCBusiness.validateTKTU(troublesDTO, troublesDTO);
    Assert.assertEquals(RESULT.FAIL, data.getKey());
  }


}
*/
