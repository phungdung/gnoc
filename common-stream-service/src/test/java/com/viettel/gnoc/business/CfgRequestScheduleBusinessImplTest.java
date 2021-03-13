package com.viettel.gnoc.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.LogChangeConfigBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.ScheduleCRFormDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.RequestScheduleDTO;
import com.viettel.gnoc.cr.dto.ScheduleCRDTO;
import com.viettel.gnoc.cr.dto.ScheduleEmployeeDTO;
import com.viettel.gnoc.repository.CfgRequestScheduleRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
@PrepareForTest({CfgRequestScheduleBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class CfgRequestScheduleBusinessImplTest {

  @InjectMocks
  CfgRequestScheduleBusinessImpl cfgRequestScheduleBusiness;

  @Mock
  UnitRepository unitRepository;

  @Mock
  CfgRequestScheduleRepository cfgRequestScheduleRepository;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  private LogChangeConfigBusiness logChangeConfigBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UserBusiness userBusiness;

  @Test
  public void test_insertRequestSchedule() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(logChangeConfigBusiness.insertLog(any())).thenReturn(resultInSideDto);
    PowerMockito.when(cfgRequestScheduleRepository.insertRequestSchedule(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = cfgRequestScheduleBusiness.insertRequestSchedule(requestScheduleDTO);
    assertEquals(result.getKey(), result.getKey());
  }

  @Test
  public void test_deleteRequestSchedule() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.when(cfgRequestScheduleRepository.deleteRequestSchedule(anyLong()))
        .thenReturn("a");
    String a = cfgRequestScheduleBusiness.deleteRequestSchedule(1L);
    assertEquals(a, "a");
  }

  @Test
  public void test_getListYear() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    List<CatItemDTO> itemDTOList = Mockito.spy(ArrayList.class);
    itemDTOList.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(itemDTOList);
    PowerMockito.when(cfgRequestScheduleRepository.getListYear(any())).thenReturn(datatable);
    Datatable datatable1 = cfgRequestScheduleBusiness.getListYear(catItemDTO);
    assertEquals(datatable1.getPages(), datatable.getPages());
  }

  @Test
  public void test_getListRequestSchedule() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setStatus(1L);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<RequestScheduleDTO> requestScheduleDTOList = Mockito.spy(ArrayList.class);
    requestScheduleDTOList.add(requestScheduleDTO);
    datatable.setData(requestScheduleDTOList);
    PowerMockito.when(cfgRequestScheduleRepository.getListRequestSchedule(any()))
        .thenReturn(datatable);
    Datatable datatable1 = cfgRequestScheduleBusiness.getListRequestSchedule(requestScheduleDTO);
    assertEquals(datatable1.getPages(), datatable.getPages());
  }

  @Test
  public void test_updateRequestSchedule() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(cfgRequestScheduleRepository.updateRequestSchedule(any())).thenReturn("a");
    PowerMockito.when(logChangeConfigBusiness.insertLog(any())).thenReturn(resultInSideDto);
    String a = cfgRequestScheduleBusiness.updateRequestSchedule(requestScheduleDTO);
    assertEquals(a, "a");
  }

  @Test
  public void test_findRequestScheduleById() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    PowerMockito.when(cfgRequestScheduleRepository.findRequestScheduleById(anyLong()))
        .thenReturn(requestScheduleDTO);
    cfgRequestScheduleBusiness.findRequestScheduleById(1L);
    assertNotNull(requestScheduleDTO);
  }

  @Test
  public void test_getListUnit() {
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgRequestScheduleRepository.getListUnit(any())).thenReturn(datatable);
    Datatable datatable1 = cfgRequestScheduleBusiness.getListUnit(unitDTO);
    assertEquals(datatable1.getPages(), datatable.getPages());
  }

  @Test
  public void test_getListEmployee() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ScheduleEmployeeDTO scheduleEmployeeDTO = Mockito.spy(ScheduleEmployeeDTO.class);
    scheduleEmployeeDTO.setType("0");
    scheduleEmployeeDTO.setStartDate(DateTimeUtils.convertDateTimeStampToString(new Date()));
    scheduleEmployeeDTO.setEndDate(DateTimeUtils.convertDateTimeStampToString(new Date()));
    scheduleEmployeeDTO.setEmpArray("1");
    scheduleEmployeeDTO.setEmpChildren("1");
    scheduleEmployeeDTO.setDayOff(DateTimeUtils.convertDateTimeStampToString(new Date()));
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCbbArrayTemp = Mockito.spy(ArrayList.class);
    lstCbbArrayTemp.add(itemDataCRInside);
    CfgChildArrayDTO cfgChildArrayDTO = Mockito.spy(CfgChildArrayDTO.class);
    cfgChildArrayDTO.setChildrenId(1L);
    List<CfgChildArrayDTO> lstArrayChildTemp = Mockito.spy(ArrayList.class);
    lstArrayChildTemp.add(cfgChildArrayDTO);
    List<ScheduleEmployeeDTO> scheduleEmployeeDTOS = Mockito.spy(ArrayList.class);
    scheduleEmployeeDTOS.add(scheduleEmployeeDTO);

    PowerMockito.when(crServiceProxy.getListImpactSegmentCBB()).thenReturn(lstCbbArrayTemp);
    PowerMockito.when(crServiceProxy.getCbbChildArray(any())).thenReturn(lstArrayChildTemp);
    PowerMockito.when(cfgRequestScheduleRepository.getListEmployee(any()))
        .thenReturn(scheduleEmployeeDTOS);
    cfgRequestScheduleBusiness.getListEmployee(scheduleEmployeeDTO);
    assertEquals(scheduleEmployeeDTOS.size(), 1);
  }

  @Test
  public void test_getListEmployee_1() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ScheduleEmployeeDTO scheduleEmployeeDTO = Mockito.spy(ScheduleEmployeeDTO.class);
    scheduleEmployeeDTO.setType("1");
    scheduleEmployeeDTO.setStartDate(DateTimeUtils.convertDateTimeStampToString(new Date()));
    scheduleEmployeeDTO.setEndDate(DateTimeUtils.convertDateTimeStampToString(new Date()));
    scheduleEmployeeDTO.setEmpArray("1");
    scheduleEmployeeDTO.setEmpChildren("1");
    scheduleEmployeeDTO.setDayOff(DateTimeUtils.convertDateTimeStampToString(new Date()));
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCbbArrayTemp = Mockito.spy(ArrayList.class);
    lstCbbArrayTemp.add(itemDataCRInside);
    CfgChildArrayDTO cfgChildArrayDTO = Mockito.spy(CfgChildArrayDTO.class);
    cfgChildArrayDTO.setChildrenId(1L);
    List<CfgChildArrayDTO> lstArrayChildTemp = Mockito.spy(ArrayList.class);
    lstArrayChildTemp.add(cfgChildArrayDTO);
    List<ScheduleEmployeeDTO> scheduleEmployeeDTOS = Mockito.spy(ArrayList.class);
    scheduleEmployeeDTOS.add(scheduleEmployeeDTO);

    PowerMockito.when(crServiceProxy.getListImpactSegmentCBB()).thenReturn(lstCbbArrayTemp);
    PowerMockito.when(crServiceProxy.getCbbChildArray(any())).thenReturn(lstArrayChildTemp);
    PowerMockito.when(cfgRequestScheduleRepository.getListEmployee(any()))
        .thenReturn(scheduleEmployeeDTOS);
    cfgRequestScheduleBusiness.getListEmployee(scheduleEmployeeDTO);
    assertEquals(scheduleEmployeeDTOS.size(), 1);
  }

  @Test
  public void test_getListEmployee_2() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ScheduleEmployeeDTO scheduleEmployeeDTO = Mockito.spy(ScheduleEmployeeDTO.class);
    scheduleEmployeeDTO.setType("3");
    scheduleEmployeeDTO.setStartDate(DateTimeUtils.convertDateTimeStampToString(new Date()));
    scheduleEmployeeDTO.setEndDate(DateTimeUtils.convertDateTimeStampToString(new Date()));
    scheduleEmployeeDTO.setEmpArray("1");
    scheduleEmployeeDTO.setEmpChildren("1");
    scheduleEmployeeDTO.setDayOff(DateTimeUtils.convertDateTimeStampToString(new Date()));
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCbbArrayTemp = Mockito.spy(ArrayList.class);
    lstCbbArrayTemp.add(itemDataCRInside);
    CfgChildArrayDTO cfgChildArrayDTO = Mockito.spy(CfgChildArrayDTO.class);
    cfgChildArrayDTO.setChildrenId(1L);
    List<CfgChildArrayDTO> lstArrayChildTemp = Mockito.spy(ArrayList.class);
    lstArrayChildTemp.add(cfgChildArrayDTO);
    List<ScheduleEmployeeDTO> scheduleEmployeeDTOS = Mockito.spy(ArrayList.class);
    scheduleEmployeeDTOS.add(scheduleEmployeeDTO);

    PowerMockito.when(crServiceProxy.getListImpactSegmentCBB()).thenReturn(lstCbbArrayTemp);
    PowerMockito.when(crServiceProxy.getCbbChildArray(any())).thenReturn(lstArrayChildTemp);
    PowerMockito.when(cfgRequestScheduleRepository.getListEmployee(any()))
        .thenReturn(scheduleEmployeeDTOS);
    cfgRequestScheduleBusiness.getListEmployee(scheduleEmployeeDTO);
    assertEquals(scheduleEmployeeDTOS.size(), 1);
  }

  @Test
  public void test_cancelStatus() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    RequestScheduleDTO dto = Mockito.spy(RequestScheduleDTO.class);
    dto.setStartDate(new Date());
    dto.setEndDate(new Date());
    PowerMockito.when(cfgRequestScheduleRepository.updateRequestSchedule(any()))
        .thenReturn("SUCCESS");
    PowerMockito.when(logChangeConfigBusiness.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = cfgRequestScheduleBusiness.cancelStatus(dto);
    assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void test_onSearchCR() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("1");
    ScheduleCRDTO scheduleCRDTO = Mockito.spy(ScheduleCRDTO.class);
    scheduleCRDTO.setCrPrioritize(1L);
    scheduleCRDTO.setCrArray("1");
    scheduleCRDTO.setForbiddenDate("1");
    scheduleCRDTO.setCrLevel(1L);
    scheduleCRDTO.setCrPerformer("1");
    List<ScheduleCRDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(scheduleCRDTO);
    UsersInsideDto usersDTO = Mockito.spy(UsersInsideDto.class);
    usersDTO.setFullname("1");
    usersDTO.setUsername("1");
    PowerMockito.when(userBusiness.getUserDTOByUserName(any())).thenReturn(usersDTO);
    PowerMockito.when(cfgRequestScheduleRepository.getListScheduleCR(any())).thenReturn(lst);
    cfgRequestScheduleBusiness.onSearchCR(scheduleCRDTO, 1L);
    assertEquals(lst.size(), 1);
  }

  @Test
  public void test_exportData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("0");
    requestScheduleDTO.setMonth("1");
    requestScheduleDTO.setYear("1");
    requestScheduleDTO.setWorkTime(1L);
    CfgChildArrayDTO cfgChildArrayDTO = Mockito.spy(CfgChildArrayDTO.class);
    cfgChildArrayDTO.setChildrenId(1L);
    cfgChildArrayDTO.setChildrenName("1");
    List<CfgChildArrayDTO> lstArrayChildTemp = Mockito.spy(ArrayList.class);
    lstArrayChildTemp.add(cfgChildArrayDTO);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrId("1");
    crInsiteDTO.setImpactSegment("1");
    crInsiteDTO.setChildImpactSegment("1");
    List<CrInsiteDTO> crInsiteDTOS = Mockito.spy(ArrayList.class);
    crInsiteDTOS.add(crInsiteDTO);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCbbArrayTemp = Mockito.spy(ArrayList.class);
    lstCbbArrayTemp.add(itemDataCRInside);
    ScheduleEmployeeDTO scheduleEmployeeDTO = Mockito.spy(ScheduleEmployeeDTO.class);
    scheduleEmployeeDTO.setEmpArray("1");
    List<ScheduleEmployeeDTO> scheduleEmployeeDTOS = Mockito.spy(ArrayList.class);
    scheduleEmployeeDTOS.add(scheduleEmployeeDTO);
    PowerMockito.when(cfgRequestScheduleRepository.getListEmployee2(any()))
        .thenReturn(scheduleEmployeeDTOS);
    PowerMockito.when(crServiceProxy.getListImpactSegmentCBBLocaleProxy(any()))
        .thenReturn(lstCbbArrayTemp);
    PowerMockito.when(cfgRequestScheduleRepository.getListCrAutoSchedule(any()))
        .thenReturn(crInsiteDTOS);
    PowerMockito.when(cfgRequestScheduleRepository.getListCfgChildArray(any()))
        .thenReturn(lstArrayChildTemp);
    File result = cfgRequestScheduleBusiness.exportData(requestScheduleDTO);
    assertNotNull(result);
  }

  @Test
  public void exportDataCRAfter() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setIdSchedule(1L);
    ScheduleCRDTO scheduleCRDTO = Mockito.spy(ScheduleCRDTO.class);
    scheduleCRDTO.setCrPrioritize(1L);
    scheduleCRDTO.setCrLevel(1L);
    scheduleCRDTO.setCrArray("1");
    scheduleCRDTO.setForbiddenDate("1");
    scheduleCRDTO.setCrPerformer("1");
    scheduleCRDTO.setIdCr(1L);
    scheduleCRDTO.setStartDate(new Date());
    scheduleCRDTO.setEndDate(new Date());
    scheduleCRDTO.setForbiddenDate(DateTimeUtils.convertDateTimeStampToString(new Date()));
    List<ScheduleCRDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(scheduleCRDTO);
    requestScheduleDTO.setCrAfterList(lst);
    UsersInsideDto usersDTO = Mockito.spy(UsersInsideDto.class);
    usersDTO.setFullname("1");
    usersDTO.setUsername("1");
    PowerMockito.when(userBusiness.getUserDTOByUserName(any())).thenReturn(usersDTO);
    PowerMockito.when(cfgRequestScheduleRepository.getListScheduleCR(any())).thenReturn(lst);
    File result = cfgRequestScheduleBusiness.exportDataCRAfter(requestScheduleDTO);
    assertNotNull(result);
  }

  @Test
  public void test_searchCRAfterFail() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setIdSchedule(1L);
    ScheduleCRDTO scheduleCRDTO = Mockito.spy(ScheduleCRDTO.class);
    scheduleCRDTO.setCrPrioritize(1L);
    scheduleCRDTO.setCrArray("1");
    scheduleCRDTO.setForbiddenDate(DateTimeUtils.convertDateTimeStampToString(new Date()));
    scheduleCRDTO.setCrPerformer("1");
    scheduleCRDTO.setCrLevel(1L);
    scheduleCRDTO.setCodeCR("1");
    List<ScheduleCRDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(scheduleCRDTO);
    UsersInsideDto usersDTO = Mockito.spy(UsersInsideDto.class);
    PowerMockito.when(cfgRequestScheduleRepository.getListScheduleCR(any())).thenReturn(lst);
    PowerMockito.when(userBusiness.getUserDTOByUserName(anyString())).thenReturn(usersDTO);
    cfgRequestScheduleBusiness.searchCRAfterFail(requestScheduleDTO);
    assertEquals(lst.size(), 0);
  }


  @Test
  public void test_onSave_validate_1() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getKey(), null);
  }

  @Test
  public void test_onSave_validate_2() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getKey(), null);
  }

  @Test
  public void test_onSave_validate_3() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("0");
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getKey(), null);
  }

  @Test
  public void test_onSave_validate_4() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("0");
    requestScheduleDTO.setMonth("1");
    requestScheduleDTO.setYear("1");
    cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(1, 1);
  }

  @Test
  public void test_onSave_validate_5() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("0");
    requestScheduleDTO.setMonth("1");
    requestScheduleDTO.setYear("1");
    requestScheduleDTO.setWorkTime(0L);
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getKey(), null);

  }

  @Test
  public void test_onSave_validate_6() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("0");
    requestScheduleDTO.setMonth("1");
    requestScheduleDTO.setYear("1");
    requestScheduleDTO.setWorkTime(30L);
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getKey(), null);
  }

  @Test
  public void test_onSave_validate_7() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("1");
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getKey(), null);
  }

  @Test
  public void test_onSave_validate_8() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("1");
    requestScheduleDTO.setWeek("0");
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getKey(), null);
  }

  @Test
  public void test_onSave_validate_9() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("3");
    requestScheduleDTO.setWeek("0");
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getKey(), null);
  }

  @Test
  public void test_onSave_1() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("ERROR");
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("1");
    requestScheduleDTO.setWeek("1");
    requestScheduleDTO.setYear("1");
    requestScheduleDTO.setWorkTime(1L);
    requestScheduleDTO.setIsGetCrBefore(2L);
    ScheduleEmployeeDTO scheduleEmployeeDTO = Mockito.spy(ScheduleEmployeeDTO.class);
    List<ScheduleEmployeeDTO> scheduleEmployeeDTOList = Mockito.spy(ArrayList.class);
    scheduleEmployeeDTOList.add(scheduleEmployeeDTO);
    requestScheduleDTO.setScheduleEmployeeDTOS(scheduleEmployeeDTOList);
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getMessage(), "ERROR");
  }

  @Test
  public void test_onSave_2() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("ERROR");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("1");
    requestScheduleDTO.setWeek("1");
    requestScheduleDTO.setYear("1");
    requestScheduleDTO.setWorkTime(1L);
    requestScheduleDTO.setIsGetCrBefore(1L);
    requestScheduleDTO.setComplicateWork(1L);
    requestScheduleDTO.setSameNode(1L);
    requestScheduleDTO.setSameService(1L);
    requestScheduleDTO.setIsInsert(1L);
    ScheduleEmployeeDTO scheduleEmployeeDTO = Mockito.spy(ScheduleEmployeeDTO.class);
    List<ScheduleEmployeeDTO> scheduleEmployeeDTOList = Mockito.spy(ArrayList.class);
    scheduleEmployeeDTOList.add(scheduleEmployeeDTO);
    requestScheduleDTO.setScheduleEmployeeDTOS(scheduleEmployeeDTOList);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(cfgRequestScheduleRepository.checkExisted(any())).thenReturn(true);
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getMessage(), "ERROR");
  }

  @Test
  public void test_onSave_3() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("ERROR");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("1");
    requestScheduleDTO.setWeek("1");
    requestScheduleDTO.setYear("2020");
    requestScheduleDTO.setWorkTime(1L);
    requestScheduleDTO.setIsGetCrBefore(1L);
    requestScheduleDTO.setComplicateWork(1L);
    requestScheduleDTO.setSameNode(1L);
    requestScheduleDTO.setSameService(1L);
    requestScheduleDTO.setIsInsert(1L);
    ScheduleEmployeeDTO scheduleEmployeeDTO = Mockito.spy(ScheduleEmployeeDTO.class);
    scheduleEmployeeDTO.setUserName("1");
    scheduleEmployeeDTO.setEmpChildren("1");
    scheduleEmployeeDTO.setEmpLevel(1L);
    List<ScheduleEmployeeDTO> scheduleEmployeeDTOList = Mockito.spy(ArrayList.class);
    scheduleEmployeeDTOList.add(scheduleEmployeeDTO);
    ScheduleCRFormDTO scheduleCRFormDTO = Mockito.spy(ScheduleCRFormDTO.class);
    scheduleCRFormDTO.setId("1");
    scheduleCRFormDTO.setCrChildren("1");
    scheduleCRFormDTO.setCodeCR("1");
    scheduleCRFormDTO.setCrArray("1");
    scheduleCRFormDTO.setCrLevel(1L);
    scheduleCRFormDTO.setCrPrioritize(1L);
    scheduleCRFormDTO.setExecutionTime("1");
    scheduleCRFormDTO.setAffectServiceList("1");
    scheduleCRFormDTO.setImpactNodeList("1");
    scheduleCRFormDTO.setStartDate(new Date());
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 1);
    scheduleCRFormDTO.setEndDate(c.getTime());
    scheduleCRFormDTO.setForbiddenDate("03-04-2020");
    List<ScheduleCRFormDTO> scheduleCRFormDTOS = Mockito.spy(ArrayList.class);
    scheduleCRFormDTOS.add(scheduleCRFormDTO);
    requestScheduleDTO.setScheduleEmployeeDTOS(scheduleEmployeeDTOList);
    requestScheduleDTO.setScheduleCRFormDTOS(scheduleCRFormDTOS);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    resultInSideDto.setId(1L);
    PowerMockito.when(cfgRequestScheduleRepository.insertListCRDTO(any())).thenReturn("SUCCESS");
    PowerMockito.when(logChangeConfigBusiness.insertLog(any())).thenReturn(resultInSideDto);
    PowerMockito.when(cfgRequestScheduleRepository.insertRequestSchedule(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(cfgRequestScheduleRepository.checkExisted(any())).thenReturn(false);
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void test_onSave_4() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("ERROR");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    RequestScheduleDTO requestScheduleDTO = Mockito.spy(RequestScheduleDTO.class);
    requestScheduleDTO.setUnitId(1L);
    requestScheduleDTO.setType("3");
    requestScheduleDTO.setWeek("1");
    requestScheduleDTO.setYear("2020");
    requestScheduleDTO.setWorkTime(1L);
    requestScheduleDTO.setIsGetCrBefore(1L);
    requestScheduleDTO.setComplicateWork(1L);
    requestScheduleDTO.setSameNode(1L);
    requestScheduleDTO.setSameService(1L);
    requestScheduleDTO.setIsInsert(1L);
    requestScheduleDTO.setPdfDay(new Date());
    ScheduleEmployeeDTO scheduleEmployeeDTO = Mockito.spy(ScheduleEmployeeDTO.class);
    scheduleEmployeeDTO.setUserName("1");
    scheduleEmployeeDTO.setEmpChildren("1");
    scheduleEmployeeDTO.setEmpLevel(1L);
    List<ScheduleEmployeeDTO> scheduleEmployeeDTOList = Mockito.spy(ArrayList.class);
    scheduleEmployeeDTOList.add(scheduleEmployeeDTO);
    ScheduleCRFormDTO scheduleCRFormDTO = Mockito.spy(ScheduleCRFormDTO.class);
    scheduleCRFormDTO.setId("1");
    scheduleCRFormDTO.setCrChildren("1");
    scheduleCRFormDTO.setCodeCR("1");
    scheduleCRFormDTO.setCrArray("1");
    scheduleCRFormDTO.setCrLevel(1L);
    scheduleCRFormDTO.setCrPrioritize(1L);
    scheduleCRFormDTO.setExecutionTime("1");
    scheduleCRFormDTO.setAffectServiceList("1");
    scheduleCRFormDTO.setImpactNodeList("1");
    scheduleCRFormDTO.setStartDate(new Date());
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 1);
    scheduleCRFormDTO.setEndDate(c.getTime());
    scheduleCRFormDTO.setForbiddenDate("03-04-2020");
    List<ScheduleCRFormDTO> scheduleCRFormDTOS = Mockito.spy(ArrayList.class);
    scheduleCRFormDTOS.add(scheduleCRFormDTO);
    requestScheduleDTO.setScheduleEmployeeDTOS(scheduleEmployeeDTOList);
    requestScheduleDTO.setScheduleCRFormDTOS(scheduleCRFormDTOS);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    resultInSideDto.setId(1L);
    PowerMockito.when(cfgRequestScheduleRepository.insertListCRDTO(any())).thenReturn("SUCCESS");
    PowerMockito.when(logChangeConfigBusiness.insertLog(any())).thenReturn(resultInSideDto);
    PowerMockito.when(cfgRequestScheduleRepository.insertRequestSchedule(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(cfgRequestScheduleRepository.checkExisted(any())).thenReturn(false);
    ResultInSideDto result = cfgRequestScheduleBusiness.onSave(requestScheduleDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }
}
