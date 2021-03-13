package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.LogChangeConfigRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.EmployeeDayOffDTO;
import com.viettel.gnoc.repository.EmployeeDayOffRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
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
import org.springframework.mock.web.MockMultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EmployeeDayOffBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class EmployeeDayOffBusinessImplTest {

  @InjectMocks
  EmployeeDayOffBusinessImpl employeeDayOffBusiness;

  @Mock
  EmployeeDayOffRepository employeeDayOffRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitRepository unitRepository;

  @Mock
  LogChangeConfigRepository logChangeConfigRepository;

  @Test
  public void getListEmployeeDayOff() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable expected = Mockito.spy(Datatable.class);
    PowerMockito.when(employeeDayOffRepository.getListEmployeeDayOff(any()))
        .thenReturn(expected);

    EmployeeDayOffDTO employeeDayOffDTO = Mockito.spy(EmployeeDayOffDTO.class);
    Datatable actual = employeeDayOffBusiness.getListEmployeeDayOff(employeeDayOffDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void findEmployeeDayOffById() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    EmployeeDayOffDTO expected = Mockito.spy(EmployeeDayOffDTO.class);
    PowerMockito.when(employeeDayOffRepository.findEmployeeDayOffById(anyLong()))
        .thenReturn(expected);

    EmployeeDayOffDTO actual = employeeDayOffBusiness.findEmployeeDayOffById(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void deleteEmployeeDayOffById() {
    PowerMockito.mockStatic(TicketProvider.class);
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    String expected = "test";
    PowerMockito.when(employeeDayOffRepository.deleteEmployeeDayOffById(anyLong()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String actual = employeeDayOffBusiness.deleteEmployeeDayOffById(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void insertEmployeeDayOff_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(employeeDayOffRepository.insertEmployeeDayOff(any()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    EmployeeDayOffDTO employeeDayOffDTO = Mockito.spy(EmployeeDayOffDTO.class);
    employeeDayOffDTO.setEmpId(1L);
    employeeDayOffDTO.setDayOff(DateTimeUtils.convertStringToDate("22/2/2020 15:30:45"));
    EmployeeDayOffDTO employeeDayOffDTO1 = Mockito.spy(EmployeeDayOffDTO.class);
    employeeDayOffDTO1.setEmpId(1L);
    employeeDayOffDTO1.setDayOff(DateTimeUtils.convertStringToDate("22/2/2020 15:30:45"));
    List<EmployeeDayOffDTO> list = Mockito.spy(ArrayList.class);
    list.add(employeeDayOffDTO);
    list.add(employeeDayOffDTO1);
    ResultInSideDto actual = employeeDayOffBusiness.insertEmployeeDayOff(list);

    Assert.assertEquals(expected.getMessage(), actual.getMessage());
  }

  @Test
  public void insertEmployeeDayOff_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(employeeDayOffRepository.insertEmployeeDayOff(any()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    EmployeeDayOffDTO employeeDayOffDTO = Mockito.spy(EmployeeDayOffDTO.class);
    employeeDayOffDTO.setEmpId(1L);
    employeeDayOffDTO.setDayOff(DateTimeUtils.convertStringToDate("23/2/2020 15:30:45"));
    EmployeeDayOffDTO employeeDayOffDTO1 = Mockito.spy(EmployeeDayOffDTO.class);
    employeeDayOffDTO1.setEmpId(2L);
    employeeDayOffDTO1.setDayOff(DateTimeUtils.convertStringToDate("22/2/2020 15:30:45"));
    List<EmployeeDayOffDTO> list = Mockito.spy(ArrayList.class);
    list.add(employeeDayOffDTO);
    list.add(employeeDayOffDTO1);
    ResultInSideDto actual = employeeDayOffBusiness.insertEmployeeDayOff(list);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void updateEmployeeDayOff() {
    PowerMockito.mockStatic(TicketProvider.class);
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(employeeDayOffRepository.updateEmployeeDayOff(any()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    EmployeeDayOffDTO employeeDayOffDTO = Mockito.spy(EmployeeDayOffDTO.class);
    employeeDayOffDTO.setIdDayOff(1L);
    ResultInSideDto actual = employeeDayOffBusiness.updateEmployeeDayOff(employeeDayOffDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void importData_01() {
    PowerMockito.mockStatic(TicketProvider.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MockMultipartFile testFile = null;
    ResultInSideDto actual = employeeDayOffBusiness.importData(testFile);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void importData_02() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = employeeDayOffBusiness.importData(testFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void importData_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any()))
        .thenReturn(filePath);
    File fileImport = new File(filePath);

    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport
            .getDataFromExcelFile(fileImport, 0, 4, 0, 8, 1000)
    ).thenReturn(lstHeader);

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = employeeDayOffBusiness.importData(testFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void importData_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any()))
        .thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects = new Object[]{"1", "1(*)", "1(*)", "1(*)"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1501; i++) {
      lstData.add(objects);
    }
    PowerMockito.when(
        CommonImport
            .getDataFromExcelFile(fileImport, 0, 4, 0, 8, 1000)
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport
            .getDataFromExcelFile(fileImport, 0, 5, 0, 8, 1000)
    ).thenReturn(lstData);
    PowerMockito.when(
        I18n.getLanguage(anyString())
    ).thenReturn("1");

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = employeeDayOffBusiness.importData(testFile);

    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void importData_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any()))
        .thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects = new Object[]{"1", "1(*)", "1(*)", "1(*)"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport
            .getDataFromExcelFile(fileImport, 0, 4, 0, 8, 1000)
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport
            .getDataFromExcelFile(fileImport, 0, 5, 0, 8, 1000)
    ).thenReturn(lstData);
    PowerMockito.when(
        I18n.getLanguage(anyString())
    ).thenReturn("1");

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = employeeDayOffBusiness.importData(testFile);

    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void importData_06() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any()))
        .thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects = new Object[]{"1", "1(*)", "1(*)", "1(*)"};
    Object[] objects1 = new Object[]{"1", "1", "02/03/2020", "MOR"};
    Object[] objects2 = new Object[]{"1", "1", "45/67/2020", "AFT"};
    Object[] objects3 = new Object[]{"1", null, null, "FULL"};
    Object[] objects4 = new Object[]{"1", null, null, null};

    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects);
    lstData.add(objects1);
    lstData.add(objects2);
    lstData.add(objects3);
    lstData.add(objects4);
    PowerMockito.when(
        CommonImport
            .getDataFromExcelFile(fileImport, 0, 4, 0, 8, 1000)
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport
            .getDataFromExcelFile(fileImport, 0, 5, 0, 8, 1000)
    ).thenReturn(lstData);
    PowerMockito.when(
        I18n.getLanguage(anyString())
    ).thenReturn("1");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitDTO);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    List<UsersInsideDto> lstUserBean = Mockito.spy(ArrayList.class);
    lstUserBean.add(usersInsideDto);
    PowerMockito.when(
        employeeDayOffRepository.getListUserInUnit(any())
    ).thenReturn(lstUserBean);

    List<UsersInsideDto> usersInsideDtos = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        employeeDayOffRepository.checkUserIsEnable(anyString())
    ).thenReturn(usersInsideDtos);

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = employeeDayOffBusiness.importData(testFile);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void getTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("test");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(2L);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitDTO);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> lstUserBean = Mockito.spy(ArrayList.class);
    lstUserBean.add(usersInsideDto);
    PowerMockito.when(
        employeeDayOffRepository.getListUserInUnit(any())
    ).thenReturn(lstUserBean);

    File actual = employeeDayOffBusiness.getTemplate();

    Assert.assertNotNull(actual);
  }

  @Test
  public void exportData_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonExport.class);

    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    EmployeeDayOffDTO employeeDayOffDTO1 = Mockito.spy(EmployeeDayOffDTO.class);
    employeeDayOffDTO1.setEmpUsername("empUserName1");
    employeeDayOffDTO1.setUnitName("unitName1");
    employeeDayOffDTO1.setUnitCode("unitCode1");
    employeeDayOffDTO1.setDayOff(new Date());
    employeeDayOffDTO1.setVacation("MOR");
    EmployeeDayOffDTO employeeDayOffDTO2 = Mockito.spy(EmployeeDayOffDTO.class);
    employeeDayOffDTO2.setEmpUsername("empUserName2");
    employeeDayOffDTO2.setUnitName("unitName2");
    employeeDayOffDTO2.setUnitCode("unitCode2");
    employeeDayOffDTO2.setDayOff(new Date());
    employeeDayOffDTO2.setVacation("AFT");
    EmployeeDayOffDTO employeeDayOffDTO3 = Mockito.spy(EmployeeDayOffDTO.class);
    employeeDayOffDTO3.setEmpUsername("empUserName3");
    employeeDayOffDTO3.setUnitName("unitName3");
    employeeDayOffDTO3.setUnitCode("unitCode3");
    employeeDayOffDTO3.setDayOff(new Date());
    employeeDayOffDTO3.setVacation("FULL");

    List<EmployeeDayOffDTO> list = Mockito.spy(ArrayList.class);
    list.add(employeeDayOffDTO1);
    list.add(employeeDayOffDTO2);
    list.add(employeeDayOffDTO3);
    PowerMockito.when(
        employeeDayOffRepository.getListEmployeeDayOffExport(any())
    ).thenReturn(list);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    EmployeeDayOffDTO employeeDayOffDTO = Mockito.spy(EmployeeDayOffDTO.class);
    File actual = employeeDayOffBusiness.exportData(employeeDayOffDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void exportData_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonExport.class);

    PowerMockito.when(I18n.getLocale()).thenReturn("");

    EmployeeDayOffDTO employeeDayOffDTO1 = Mockito.spy(EmployeeDayOffDTO.class);
    employeeDayOffDTO1.setEmpUsername("empUserName1");
    employeeDayOffDTO1.setUnitName("unitName1");
    employeeDayOffDTO1.setUnitCode("unitCode1");
    employeeDayOffDTO1.setDayOff(new Date());
    employeeDayOffDTO1.setVacation("MOR");
    EmployeeDayOffDTO employeeDayOffDTO2 = Mockito.spy(EmployeeDayOffDTO.class);
    employeeDayOffDTO2.setEmpUsername("empUserName2");
    employeeDayOffDTO2.setUnitName("unitName2");
    employeeDayOffDTO2.setUnitCode("unitCode2");
    employeeDayOffDTO2.setDayOff(new Date());
    employeeDayOffDTO2.setVacation("AFT");
    EmployeeDayOffDTO employeeDayOffDTO3 = Mockito.spy(EmployeeDayOffDTO.class);
    employeeDayOffDTO3.setEmpUsername("empUserName3");
    employeeDayOffDTO3.setUnitName("unitName3");
    employeeDayOffDTO3.setUnitCode("unitCode3");
    employeeDayOffDTO3.setDayOff(new Date());
    employeeDayOffDTO3.setVacation("FULL");

    List<EmployeeDayOffDTO> list = Mockito.spy(ArrayList.class);
    list.add(employeeDayOffDTO1);
    list.add(employeeDayOffDTO2);
    list.add(employeeDayOffDTO3);
    PowerMockito.when(
        employeeDayOffRepository.getListEmployeeDayOffExport(any())
    ).thenReturn(list);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    EmployeeDayOffDTO employeeDayOffDTO = Mockito.spy(EmployeeDayOffDTO.class);
    File actual = employeeDayOffBusiness.exportData(employeeDayOffDTO);

    Assert.assertNull(actual);
  }
}
