package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CrAlarmFaultGroupDTO;
import com.viettel.gnoc.commons.dto.CrModuleDraftDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.incident.provider.WSIIMPort;
import com.viettel.gnoc.commons.incident.provider.WSNIMSInfraPort;
import com.viettel.gnoc.commons.incident.provider.WSNocprov4Port;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.repository.CrAlarmSettingRepository;
import com.viettel.gnoc.cr.repository.CrImpactSegmentRepository;
import com.viettel.nims.infra.webservice.CatVendorBO;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
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
import org.springframework.test.util.ReflectionTestUtils;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrAlarmSettingBusinessImpl.class, I18n.class, TicketProvider.class,
    DataUtil.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrAlarmSettingBusinessImplTest {

  @InjectMocks
  CrAlarmSettingBusinessImpl crAlarmSettingBusiness;
  @Mock
  CrAlarmSettingRepository crAlarmSettingRepository;
  @Mock
  CrImpactSegmentRepository crImpactSegmentRepository;
  @Mock
  LanguageExchangeRepository languageExchangeRepository;
  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;
  @Mock
  TicketProvider ticketProvider;
  @Mock
  WSNocprov4Port wsNocprov4Port;
  @Mock
  WSIIMPort wsiimPort;
  @Mock
  WSNIMSInfraPort wsnimsInfraPort;

  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(crAlarmSettingBusiness, "tempFolder",
        "D:\\GNOC_CODE_GIT\\service-backend\\wo\\wo-common-business\\src\\test\\java\\com\\viettel\\gnoc\\wo\\business\\2019\\12\\16\\");
  }

  @Before
  public void setNationCodeList() {
    ReflectionTestUtils.setField(crAlarmSettingBusiness, "nationCodeList",
        "VNM; RUS");
  }


  @Test
  public void getCrAlarmSetting_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    crAlarmSettingDTO.setApprovalStatus(1L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Đã phê duyệt");
    List<CrAlarmSettingDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(crAlarmSettingDTO);
    datatable.setData(lst);
    PowerMockito.when(crAlarmSettingRepository.getCrAlarmSetting(crAlarmSettingDTO))
        .thenReturn(datatable);
    Datatable datatable1 = crAlarmSettingBusiness.getCrAlarmSetting(crAlarmSettingDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getCrAlarmSetting_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    crAlarmSettingDTO.setApprovalStatus(2L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Đang phê duyệt");
    List<CrAlarmSettingDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(crAlarmSettingDTO);
    datatable.setData(lst);
    PowerMockito.when(crAlarmSettingRepository.getCrAlarmSetting(crAlarmSettingDTO))
        .thenReturn(datatable);
    Datatable datatable1 = crAlarmSettingBusiness.getCrAlarmSetting(crAlarmSettingDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getListImpactSegmentCBB_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<ImpactSegmentDTO> impactSegmentDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(crImpactSegmentRepository.getListImpactSegmentDTO(any()))
        .thenReturn(impactSegmentDTOS);
    List<LanguageExchangeDTO> lstLanguage = Mockito.spy(ArrayList.class);
    PowerMockito.when(languageExchangeRepository.findBySql(anyString(), anyMap(), anyMap(), any()))
        .thenReturn(lstLanguage);
    List<ImpactSegmentDTO> impactSegmentDTOS1 = crAlarmSettingBusiness.getListImpactSegmentCBB();
    Assert.assertEquals(impactSegmentDTOS.size(), impactSegmentDTOS1.size());
  }

  @Test
  public void exportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    crAlarmSettingDTO.setApprovalStatus(1L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Đã phê duyệt");
    List<CrAlarmSettingDTO> lstEx = Mockito.spy(ArrayList.class);
    lstEx.add(crAlarmSettingDTO);
    PowerMockito.when(crAlarmSettingRepository.getCrAlarmSettingExport(any())).thenReturn(lstEx);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    File file = crAlarmSettingBusiness.exportData(crAlarmSettingDTO);
    Assert.assertNotNull(file);
  }

  @Test
  public void exportData_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    crAlarmSettingDTO.setApprovalStatus(2L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Đang phê duyệt");
    List<CrAlarmSettingDTO> lstEx = Mockito.spy(ArrayList.class);
    lstEx.add(crAlarmSettingDTO);
    PowerMockito.when(crAlarmSettingRepository.getCrAlarmSettingExport(any())).thenReturn(lstEx);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    File file = crAlarmSettingBusiness.exportData(crAlarmSettingDTO);
    Assert.assertNotNull(file);
  }

  @Test
  public void getListCrProcessCBB_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ItemDataCRInside> itemDataCRInsides = Mockito.spy(ArrayList.class);
    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    PowerMockito.when(crAlarmSettingRepository.listCrProcessCBB(any()))
        .thenReturn(itemDataCRInsides);
    List<ItemDataCRInside> itemDataCRInsides1 = crAlarmSettingBusiness
        .getListCrProcessCBB(crProcessDTO);
    Assert.assertEquals(itemDataCRInsides.size(), itemDataCRInsides1.size());
  }

  @Test
  public void saveOrUpdateAlarmSetting_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    List<CrAlarmInsiteDTO> lstAlarm = Mockito.spy(ArrayList.class);
    CrAlarmInsiteDTO crAlarmInsiteDTO = Mockito.spy(CrAlarmInsiteDTO.class);
    List<CatVendorBO> lstVendor = Mockito.spy(ArrayList.class);
    CatVendorBO catVendorBO = Mockito.spy(CatVendorBO.class);
    catVendorBO.setVendorCode("1");
    catVendorBO.setVendorName("vtnet");
    lstVendor.add(catVendorBO);
    crAlarmInsiteDTO.setLstVendor(lstVendor);
    List<CrModuleDraftDTO> lstModule = Mockito.spy(ArrayList.class);
    CrModuleDraftDTO crModuleDraftDTO = Mockito.spy(CrModuleDraftDTO.class);
    crModuleDraftDTO.setMODULE_CODE("1");
    crModuleDraftDTO.setMODULE_NAME("fixBug");
    lstModule.add(crModuleDraftDTO);
    crAlarmInsiteDTO.setLstModule(lstModule);
    crAlarmInsiteDTO.setCrAlarmSettingId(1L);
    crAlarmInsiteDTO.setDeviceTypeCode("1");
    crAlarmInsiteDTO.setFaultGroupName("fixBug");
    crAlarmInsiteDTO.setFaultId(1L);
    crAlarmInsiteDTO.setFaultLevelCode("1");
    crAlarmInsiteDTO.setFaultName("vtnet");
    crAlarmInsiteDTO.setFaultSrc("1");
    crAlarmInsiteDTO.setModuleCode("1");
    crAlarmInsiteDTO.setModuleName("fixBug");
    crAlarmInsiteDTO.setVendorCode("1");
    crAlarmInsiteDTO.setVendorName("fixBug");
    crAlarmInsiteDTO.setKeyword("fixBug");
    lstAlarm.add(crAlarmInsiteDTO);
    crAlarmSettingDTO.setLstAlarm(lstAlarm);
    crAlarmSettingDTO.setCrProcessId(1L);
    crAlarmSettingDTO.setAutoLoad(1L);
    PowerMockito.when(crAlarmSettingRepository.saveOrUpdateAlarmSetting(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    PowerMockito.when(crAlarmSettingRepository.checkDuplicate(anyLong(), anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crAlarmSettingBusiness
        .saveOrUpdateAlarmSetting(crAlarmSettingDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void updateVendorOrModuleAlarmSetting_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    List<CrAlarmInsiteDTO> lstAlarm = Mockito.spy(ArrayList.class);
    CrAlarmInsiteDTO crAlarmInsiteDTO = Mockito.spy(CrAlarmInsiteDTO.class);
    List<CatVendorBO> lstVendor = Mockito.spy(ArrayList.class);
    CatVendorBO catVendorBO = Mockito.spy(CatVendorBO.class);
    catVendorBO.setVendorCode("1");
    catVendorBO.setVendorName("vtnet");
    lstVendor.add(catVendorBO);
    crAlarmInsiteDTO.setLstVendor(lstVendor);
    List<CrModuleDraftDTO> lstModule = Mockito.spy(ArrayList.class);
    CrModuleDraftDTO crModuleDraftDTO = Mockito.spy(CrModuleDraftDTO.class);
    crModuleDraftDTO.setMODULE_CODE("1");
    crModuleDraftDTO.setMODULE_NAME("fixBug");
    lstModule.add(crModuleDraftDTO);
    crAlarmInsiteDTO.setLstModule(lstModule);
    crAlarmInsiteDTO.setCrAlarmSettingId(1L);
    crAlarmInsiteDTO.setDeviceTypeCode("1");
    crAlarmInsiteDTO.setFaultGroupName("fixBug");
    crAlarmInsiteDTO.setFaultId(1L);
    crAlarmInsiteDTO.setFaultLevelCode("1");
    crAlarmInsiteDTO.setFaultName("vtnet");
    crAlarmInsiteDTO.setFaultSrc("1");
    crAlarmInsiteDTO.setModuleCode("1");
    crAlarmInsiteDTO.setModuleName("fixBug");
    crAlarmInsiteDTO.setVendorCode("1");
    crAlarmInsiteDTO.setVendorName("fixBug");
    crAlarmInsiteDTO.setKeyword("fixBug");
    lstAlarm.add(crAlarmInsiteDTO);
    crAlarmSettingDTO.setLstAlarm(lstAlarm);
    crAlarmSettingDTO.setCrProcessId(1L);
    crAlarmSettingDTO.setAutoLoad(1L);
    PowerMockito.when(crAlarmSettingRepository.saveOrUpdateAlarmSetting(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    PowerMockito.when(crAlarmSettingRepository.checkDuplicate(anyLong(), anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crAlarmSettingBusiness
        .updateVendorOrModuleAlarmSetting(crAlarmSettingDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void validateAlarmSetting_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    PowerMockito.when(crAlarmSettingRepository.findCrAlarmSettingById(anyLong()))
        .thenReturn(crAlarmSettingDTO);
    PowerMockito.when(crAlarmSettingRepository.saveOrUpdateAlarmSetting(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crAlarmSettingBusiness.validateAlarmSetting(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void updateAlarmSetting_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    CrAlarmInsiteDTO alarmDTO = Mockito.spy(CrAlarmInsiteDTO.class);
    alarmDTO.setCrAlarmSettingId(1L);
    List<CrAlarmInsiteDTO> lstAlarm = Mockito.spy(ArrayList.class);
    lstAlarm.add(alarmDTO);
    crAlarmSettingDTO.setLstAlarm(lstAlarm);
    crAlarmSettingDTO.setCrProcessId(1L);
    crAlarmSettingDTO.setAutoLoad(1L);
    List<CatVendorBO> lstVendor = Mockito.spy(ArrayList.class);
    CatVendorBO catVendorBO = Mockito.spy(CatVendorBO.class);
    catVendorBO.setVendorCode("1");
    catVendorBO.setVendorName("vtnet");
    lstVendor.add(catVendorBO);
    alarmDTO.setLstVendor(lstVendor);
    List<CrModuleDraftDTO> lstModule = Mockito.spy(ArrayList.class);
    CrModuleDraftDTO crModuleDraftDTO = Mockito.spy(CrModuleDraftDTO.class);
    crModuleDraftDTO.setMODULE_CODE("1");
    crModuleDraftDTO.setMODULE_NAME("fixBug");
    lstModule.add(crModuleDraftDTO);
    alarmDTO.setLstModule(lstModule);
    alarmDTO.setCrAlarmSettingId(1L);
    alarmDTO.setDeviceTypeCode("1");
    alarmDTO.setFaultGroupName("fixBug");
    alarmDTO.setFaultId(1L);
    alarmDTO.setFaultLevelCode("1");
    alarmDTO.setFaultName("vtnet");
    alarmDTO.setFaultSrc("1");
    alarmDTO.setModuleCode("1");
    alarmDTO.setModuleName("fixBug");
    alarmDTO.setVendorCode("1");
    alarmDTO.setVendorName("fixBug");
    alarmDTO.setKeyword("fixBug");
    lstAlarm.add(alarmDTO);
    crAlarmSettingDTO.setLstAlarm(lstAlarm);
    crAlarmSettingDTO.setCrProcessId(1L);
    crAlarmSettingDTO.setAutoLoad(1L);
    crAlarmSettingDTO.setCasId(0L);
    List<Long> lstDeleteId = Mockito.spy(ArrayList.class);
    lstDeleteId.add(1L);
    crAlarmSettingDTO.setLstDeleteId(lstDeleteId);
    PowerMockito.when(crAlarmSettingRepository.findCrAlarmSettingById(anyLong()))
        .thenReturn(crAlarmSettingDTO);
    PowerMockito.when(crAlarmSettingRepository.deleteAlarmSetting(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(crAlarmSettingRepository.saveOrUpdateAlarmSetting(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crAlarmSettingBusiness.updateAlarmSetting(crAlarmSettingDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getListGroupFaultSrc_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CrAlarmFaultGroupDTO> lstFaultGroupDTO = Mockito.spy(ArrayList.class);
    CrAlarmFaultGroupDTO data = Mockito.spy(CrAlarmFaultGroupDTO.class);
    data.setFault_src("fixBug");
    lstFaultGroupDTO.add(data);
    PowerMockito.when(wsNocprov4Port.getFautGroupInfo(anyString())).thenReturn(lstFaultGroupDTO);
    List<CrAlarmFaultGroupDTO> crAlarmFaultGroupDTOS = crAlarmSettingBusiness
        .getListGroupFaultSrc("fixBug", "VNM");
    Assert.assertEquals(lstFaultGroupDTO.size(), crAlarmFaultGroupDTOS.size());
  }

  @Test
  public void getAlarmList_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrAlarmInsiteDTO crAlarmDTO = Mockito.spy(CrAlarmInsiteDTO.class);
    crAlarmDTO.setFaultSrc("fixBug");
    crAlarmDTO.setFaultName("fixBug");
    crAlarmDTO.setFaultGroupId(1L);
    crAlarmDTO.setNationCode("VNM");
    crAlarmDTO.setVendorCode("123");
    crAlarmDTO.setVendorName("vtnet");
    crAlarmDTO.setModuleCode("123");
    crAlarmDTO.setModuleName("fixBug");
    crAlarmDTO.setPageSize(1);
    crAlarmDTO.setPage(1);
    List<CrAlarmInsiteDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(crAlarmDTO);
    PowerMockito.when(wsNocprov4Port.getAlarmList(any())).thenReturn(lst);
    Datatable datatable = crAlarmSettingBusiness.getAlarmList(crAlarmDTO);
    Assert.assertEquals(datatable.getPages(), 1L);
  }

  @Test
  public void getListFaultSrc_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CrAlarmFaultGroupDTO> lstFaultGroupDTO = Mockito.spy(ArrayList.class);
    CrAlarmFaultGroupDTO data = Mockito.spy(CrAlarmFaultGroupDTO.class);
    data.setFault_src("fixBug");
    lstFaultGroupDTO.add(data);
    PowerMockito.when(wsNocprov4Port.getFautGroupInfo(any())).thenReturn(lstFaultGroupDTO);
    HashSet<String> strings = crAlarmSettingBusiness.getListFaultSrc("VNM");
    Assert.assertEquals(strings.size(), 1L);
  }

  @Test
  public void getModuleList_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CrModuleDraftDTO> list = Mockito.spy(ArrayList.class);
    CrModuleDraftDTO crModuleDraftDTO = Mockito.spy(CrModuleDraftDTO.class);
    list.add(crModuleDraftDTO);
    PowerMockito.when(wsiimPort.getIIMModules(any(), any(), any(), any(), any(), any()))
        .thenReturn(list);
    Datatable datatable = crAlarmSettingBusiness
        .getModuleList("1", "fixBug", "413314", "VNM", 1, 1);
    Assert.assertEquals(datatable.getPages(), 1L);
  }

  @Test
  public void findAlarmSettingByProcessId_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CrAlarmSettingDTO> lst = Mockito.spy(ArrayList.class);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    crAlarmSettingDTO.setCrProcessId(1L);
    crAlarmSettingDTO.setCasId(1L);
    crAlarmSettingDTO.setDeviceTypeCode("1");
    crAlarmSettingDTO.setFaultGroupName("fixBug");
    crAlarmSettingDTO.setFaultId(1L);
    crAlarmSettingDTO.setFaultLevelCode("1");
    crAlarmSettingDTO.setFaultName("fixBug");
    crAlarmSettingDTO.setModuleCode("1");
    crAlarmSettingDTO.setModuleName("fixBug");
    crAlarmSettingDTO.setVendorCode("1");
    crAlarmSettingDTO.setVendorName("fixBug");
    crAlarmSettingDTO.setAutoLoad(1L);
    crAlarmSettingDTO.setCreatedUser("thanhlv12");
    crAlarmSettingDTO.setNationCode("VNM");
    crAlarmSettingDTO.setKeyword("ID");
    lst.add(crAlarmSettingDTO);
    PowerMockito.when(crAlarmSettingRepository.findCrAlarmSettingByProcessId(any()))
        .thenReturn(lst);
    CrAlarmSettingDTO detail = Mockito.spy(CrAlarmSettingDTO.class);
    PowerMockito.when(crAlarmSettingRepository.findCrAlarmSettingById(any())).thenReturn(detail);
    CrAlarmSettingDTO crAlarmSettingDTO1 = crAlarmSettingBusiness
        .findAlarmSettingByProcessId(crAlarmSettingDTO);
    Assert.assertNotNull(crAlarmSettingDTO1);
  }

  @Test
  public void getVendorList_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CatVendorBO> lst = Mockito.spy(ArrayList.class);
    CatVendorBO catVendorBO = Mockito.spy(CatVendorBO.class);
    catVendorBO.setVendorId(1L);
    catVendorBO.setVendorCode("1");
    catVendorBO.setVendorName("vtnet");
    catVendorBO.setDescription("fixBug");
    catVendorBO.setCatCode("123");
    lst.add(catVendorBO);
    PowerMockito.when(wsnimsInfraPort.getVendorList(anyString(), anyString())).thenReturn(lst);
    Datatable datatable = crAlarmSettingBusiness.getVendorList("1", "vtnet", 1, 1);
    Assert.assertEquals(datatable.getPages(), 1L);
  }

  @Test
  public void findCrAlarmSettingById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrAlarmInsiteDTO crAlarmDTO = Mockito.spy(CrAlarmInsiteDTO.class);
    crAlarmDTO.setCrAlarmSettingId(1L);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    crAlarmSettingDTO.setVendorCode("1");
    crAlarmSettingDTO.setVendorName("vtnet");
    crAlarmSettingDTO.setModuleCode("1");
    crAlarmSettingDTO.setModuleName("fixBug");
    PowerMockito.when(crAlarmSettingRepository.findCrAlarmSettingById(any()))
        .thenReturn(crAlarmSettingDTO);
    CrAlarmSettingDTO crAlarmSettingDTO1 = crAlarmSettingBusiness
        .findCrAlarmSettingById(crAlarmDTO);
    Assert.assertNotNull(crAlarmSettingDTO1);
  }

  @Test
  public void nationMap_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Map<String, String> stringStringMap = crAlarmSettingBusiness.nationMap();
    Assert.assertEquals(stringStringMap.size(), 0L);
  }
}
