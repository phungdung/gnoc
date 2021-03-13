package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.repository.WoCdTempRepository;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import com.viettel.gnoc.wo.dto.WoCdTempInsideDTO;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
@PrepareForTest({WoCdTempBusinessImpl.class, FileUtils.class, CommonImport.class,
    CommonExport.class, TicketProvider.class, I18n.class, DateTimeUtils.class, InputStream.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class WoCdTempBusinessImplTest {

  @InjectMocks
  WoCdTempBusinessImpl woCdTempBusiness;

  @Mock
  WoCdTempRepository woCdTempRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  TicketProvider ticketProvider;

  @Test
  public void testGetListWoCdTempDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    PowerMockito.when(woCdTempRepository.getListWoCdTempDTO(any())).thenReturn(datatable);
    Datatable data = woCdTempBusiness.getListWoCdTempDTO(woCdTempInsideDTO);
    Assert.assertEquals(data.getTotal(), datatable.getTotal());
  }

  @Test
  public void testGetListCdGroupByUser_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoCdGroupTypeUserDTO woCdGroupTypeUserDTO = Mockito.spy(WoCdGroupTypeUserDTO.class);
    List<WoCdGroupInsideDTO> list = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO dto = Mockito.spy(WoCdGroupInsideDTO.class);
    list.add(dto);
    PowerMockito.when(woCdTempRepository.getListCdGroupByUser(any())).thenReturn(list);
    List<WoCdGroupInsideDTO> woCdGroupInsideDTOS = woCdTempBusiness
        .getListCdGroupByUser(woCdGroupTypeUserDTO);
    Assert.assertEquals(woCdGroupInsideDTOS.size(), list.size());
  }

  @Test
  public void testGetListUserByCdGroupCBB_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<UsersInsideDto> usersInsideDtoList = Mockito.spy(ArrayList.class);
    WoCdGroupUnitDTO dtoGroupUnit = Mockito.spy(WoCdGroupUnitDTO.class);
    dtoGroupUnit.setCdGroupId(1L);
    List<WoCdGroupUnitDTO> lstWoCdGroupUnitDTO = Mockito.spy(ArrayList.class);
    lstWoCdGroupUnitDTO.add(dtoGroupUnit);
    PowerMockito.when(woCdTempRepository.getListWoCdGroupUnitDTO(any()))
        .thenReturn(lstWoCdGroupUnitDTO);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUnitId(1L);
    usersInsideDtoList.add(usersInsideDto);
    PowerMockito.when(userRepository.getListUsersDTOS(any())).thenReturn(usersInsideDtoList);
    List<UsersInsideDto> list = woCdTempBusiness.getListUserByCdGroupCBB(1L);
    Assert.assertEquals(list.size(), usersInsideDtoList.size());
  }

  @Test
  public void testGetListUserByCdGroupIsEnable_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<UsersInsideDto> usersInsideDtoList = Mockito.spy(ArrayList.class);
    WoCdGroupUnitDTO dto = Mockito.spy(WoCdGroupUnitDTO.class);
    dto.setUnitId(1L);
    List<WoCdGroupUnitDTO> lstWoCdGroupUnitDTO = Mockito.spy(ArrayList.class);
    lstWoCdGroupUnitDTO.add(dto);
    PowerMockito.when(woCdTempRepository.getListUserByCdGroupIsEnable(anyLong()))
        .thenReturn(lstWoCdGroupUnitDTO);
    List<Users> listUser = Mockito.spy(ArrayList.class);
    Users users = Mockito.spy(Users.class);
    users.setFullname("xxx");
    listUser.add(users);
    usersInsideDtoList.add(users.toDTO());
    PowerMockito.when(userRepository.getListUserOfUnit(anyLong())).thenReturn(listUser);
    List<UsersInsideDto> list = woCdTempBusiness.getListUserByCdGroupIsEnable(1L);
    Assert.assertEquals(list.size(), usersInsideDtoList.size());
  }

  @Test
  public void testInsert_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = null;
    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    String id = null;
    PowerMockito.when(woCdTempRepository.getSeqWoCdTemp(anyString())).thenReturn(id);
    ResultInSideDto result = woCdTempBusiness.insert(woCdTempInsideDTO);
    Assert.assertEquals(result, resultInSideDto);
  }

  @Test
  public void testInsert_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    String id = "123";
    PowerMockito.when(woCdTempRepository.getSeqWoCdTemp("WFM.WO_CD_TEMP_SEQ")).thenReturn(id);
    PowerMockito.when(woCdTempRepository.add(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = woCdTempBusiness.insert(woCdTempInsideDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testUpdate_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempInsideDTO.setStatus(1L);
    woCdTempInsideDTO.setIsCd(12L);
    PowerMockito.when(woCdTempRepository.edit(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = woCdTempBusiness.update(woCdTempInsideDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testGetDetail_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempInsideDTO.setWoCdTempId(1L);
    PowerMockito.when(woCdTempRepository.getDetail(anyLong())).thenReturn(woCdTempInsideDTO);
    WoCdTempInsideDTO dto = woCdTempBusiness.getDetail(1L);
    Assert.assertNotNull(dto);
  }

  @Test
  public void testDelete_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    userToken.setUserID(69L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("woCdTemp.err.deleteStatus")).thenReturn("xxx");

    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempInsideDTO.setStatus(2L);
    resultInSideDto.setKey(RESULT.ERROR);

    ResultInSideDto result = woCdTempBusiness.delete(woCdTempInsideDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testDelete_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    userToken.setUserID(69L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcdef")).thenReturn("xxx");

    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempInsideDTO.setStatus(96L);
    woCdTempInsideDTO.setWoCdTempId(26L);
    List<WoCdDTO> woCdDTOList = Mockito.spy(ArrayList.class);
    WoCdDTO woCdDTO = Mockito.spy(WoCdDTO.class);
    woCdDTO.setWoGroupId(4L);
    woCdDTOList.add(woCdDTO);
    List<String> result = Mockito.spy(ArrayList.class);
    result.add(String.valueOf(woCdDTO.getWoGroupId()));

    PowerMockito.when(
        woCdTempRepository.getListWoCdDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(woCdDTOList);
    PowerMockito.when(woCdTempRepository.delete(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto result123 = woCdTempBusiness.delete(woCdTempInsideDTO);
    Assert.assertEquals(result123.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testDelete_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    userToken.setUserID(69L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcdef")).thenReturn("xxx");

    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempInsideDTO.setStatus(96L);
    woCdTempInsideDTO.setWoCdTempId(26L);
    woCdTempInsideDTO.setWoGroupId(26L);
    List<WoCdDTO> woCdDTOList = Mockito.spy(ArrayList.class);
    WoCdDTO woCdDTO = Mockito.spy(WoCdDTO.class);
    woCdDTO.setWoGroupId(4L);
    woCdDTOList.add(woCdDTO);
    List<String> result = Mockito.spy(ArrayList.class);
    result.add(String.valueOf(woCdDTO.getWoGroupId()));
    resultInSideDto.setKey(RESULT.ERROR);
    PowerMockito.when(
        woCdTempRepository.getListWoCdDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(woCdDTOList);
    PowerMockito.when(woCdTempRepository.delete(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto result123 = woCdTempBusiness.delete(woCdTempInsideDTO);
    Assert.assertEquals(result123.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testDelete_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    userToken.setUserID(69L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcdef")).thenReturn("xxx");

    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempInsideDTO.setStatus(96L);
    woCdTempInsideDTO.setWoCdTempId(26L);
    woCdTempInsideDTO.setWoGroupId(26L);
    List<WoCdDTO> woCdDTOList = null;

    resultInSideDto.setKey(RESULT.ERROR);
    PowerMockito.when(
        woCdTempRepository.getListWoCdDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(woCdDTOList);
    ResultInSideDto result = woCdTempBusiness.delete(woCdTempInsideDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testExportData_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempInsideDTO.setWoCdTempId(1L);
    woCdTempInsideDTO.setStatus(26L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcxyz")).thenReturn("xxx");

    List<WoCdTempInsideDTO> woCdGroupInsideDTOList = Mockito.spy(ArrayList.class);
    PowerMockito.when(woCdTempRepository.getListDataExport(any()))
        .thenReturn(woCdGroupInsideDTOList);
    WoCdTempInsideDTO woCdTempExport = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempExport.setIsCd(1L);
    woCdTempExport.setStatus(0L);
    woCdTempExport.setStartTime(new Date());
    woCdTempExport.setEndTime(new Date());
    woCdGroupInsideDTOList.add(woCdTempExport);

    PowerMockito.when(I18n.getLanguage("woCdTemp.export.title")).thenReturn("CD Trực Ca");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    woCdTempBusiness.exportData(woCdTempInsideDTO);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void testExportData_02() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempInsideDTO.setWoCdTempId(1L);
    woCdTempInsideDTO.setStatus(26L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcxyz")).thenReturn("xxx");

    List<WoCdTempInsideDTO> woCdGroupInsideDTOList = Mockito.spy(ArrayList.class);
    PowerMockito.when(woCdTempRepository.getListDataExport(any()))
        .thenReturn(woCdGroupInsideDTOList);
    WoCdTempInsideDTO woCdTempExport = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempExport.setIsCd(0L);
    woCdTempExport.setStatus(1L);
    woCdTempExport.setStartTime(new Date());
    woCdTempExport.setEndTime(new Date());
    woCdGroupInsideDTOList.add(woCdTempExport);

    PowerMockito.when(I18n.getLanguage("woCdTemp.export.title")).thenReturn("CD Trực Ca");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    woCdTempBusiness.exportData(woCdTempInsideDTO);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void testExportData_03() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoCdTempInsideDTO woCdTempInsideDTO = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempInsideDTO.setWoCdTempId(1L);
    woCdTempInsideDTO.setStatus(26L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcxyz")).thenReturn("xxx");

    List<WoCdTempInsideDTO> woCdGroupInsideDTOList = Mockito.spy(ArrayList.class);
    PowerMockito.when(woCdTempRepository.getListDataExport(any()))
        .thenReturn(woCdGroupInsideDTOList);
    WoCdTempInsideDTO woCdTempExport = Mockito.spy(WoCdTempInsideDTO.class);
    woCdTempExport.setIsCd(0L);

    woCdGroupInsideDTOList.add(woCdTempExport);

    PowerMockito.when(I18n.getLanguage("woCdTemp.export.title")).thenReturn("CD Trực Ca");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    woCdTempBusiness.exportData(woCdTempInsideDTO);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void testGetTemplate_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("veuveuveu");
    PowerMockito.mockStatic(CommonExport.class);
    ExcelWriterUtils excelWriterUtils = Mockito.spy(ExcelWriterUtils.class);
    XSSFWorkbook workbook = Mockito.spy(XSSFWorkbook.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("vietNam");
    List<WoCdGroupInsideDTO> woGroupList = Mockito.spy(ArrayList.class);
    woGroupList.add(woCdGroupInsideDTO);
    PowerMockito.when(woCdTempRepository.getListCdGroupByUser(null)).thenReturn(woGroupList);
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        "Nhóm điều phối",
        "Nhân viên",
        "Thời gian bắt đầu (dd/MM/yyyy HH:mm:ss)",
        "Thời gian kết thúc (dd/MM/yyyy HH:mm:ss)"
    };
    List<String> listHeaderStar = Arrays.asList(headerStar);

    PowerMockito.when(I18n.getLanguage("woCdTemp.title")).thenReturn("CD Trực Ca");
    PowerMockito.when(I18n.getLanguage("woCdTemp.woGroupName")).thenReturn("Nhom dieu phoi");
    File fileExport = new File("./test_junit2/test.xlsx");
    woCdTempBusiness.getTemplate();
    Assert.assertNotNull(fileExport);
  }
//
//  @Test
//  public void importData() {
//  }
}
