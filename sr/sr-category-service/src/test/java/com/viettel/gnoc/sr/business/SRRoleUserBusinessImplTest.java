package com.viettel.gnoc.sr.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.repository.SRRoleUserRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SRRoleUserBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class, DataUtil.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class SRRoleUserBusinessImplTest {

  @InjectMocks
  SRRoleUserBusinessImpl srRoleUserBusiness;

  @Mock
  SRRoleUserRepository srRoleUserRepository;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  SRRoleBusiness srRoleBusiness;

  @Mock
  UnitBusiness unitBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void test_getListSRRoleUserPage() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(datatable.getTotal()).thenReturn(10);
    when(srRoleUserRepository.getListSRRoleUserPage(any())).thenReturn(datatable);
    Datatable result = srRoleUserBusiness.getListSRRoleUserPage(new SRRoleUserInSideDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void test_getListSRRoleUser() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SRRoleUserInSideDTO> data = new ArrayList<>();
    when(srRoleUserRepository.getListSRRoleUser(any())).thenReturn(data);
    List<SRRoleUserInSideDTO> result = srRoleUserBusiness
        .getListSRRoleUser(new SRRoleUserInSideDTO());
    assertEquals(data, result);
  }

  @Test
  public void test_insert() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    resultInSideDto.setId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    when(resultInSideDto.getId()).thenReturn(1L);
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserDTO.setUsernameStr("1");
    srRoleUserDTO.setIsLeaderStr("1");
    when(srRoleUserRepository.add(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = srRoleUserBusiness.insert(srRoleUserDTO);
    assertNull(result.getKey());
  }

  @Test
  public void test_update() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    resultInSideDto.setId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    SRRoleUserInSideDTO roleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    roleUserInSideDTO.setCountry("1");
    roleUserInSideDTO.setRoleCode("1");
    roleUserInSideDTO.setUsername("1");
    roleUserInSideDTO.setUnitId(1L);
    roleUserInSideDTO.setRoleUserId(1L);
    SRRoleUserInSideDTO roleUserInSideDTO1 = Mockito.spy(SRRoleUserInSideDTO.class);
    roleUserInSideDTO1.setCountry("1");
    roleUserInSideDTO1.setRoleCode("1");
    roleUserInSideDTO1.setUsername("1");
    roleUserInSideDTO1.setUnitId(1L);
    roleUserInSideDTO1.setRoleUserId(2L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(
        srRoleUserRepository.checkRoleUserExist(anyString(), anyString(), anyString(), anyLong()))
        .thenReturn(roleUserInSideDTO1);
    ResultInSideDto result = srRoleUserBusiness.update(roleUserInSideDTO);
    assertEquals(result.getKey(), "DUPLICATE");
  }

  @Test
  public void test_delete() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(srRoleUserRepository.delete(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = srRoleUserBusiness.delete(123L);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void test_getDetail() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRRoleUserInSideDTO srRoleUserDTO = new SRRoleUserInSideDTO();
    when(srRoleUserRepository.getDetail(any()))
        .thenReturn(srRoleUserDTO);
    SRRoleUserInSideDTO result = srRoleUserBusiness.getDetail(10L);
    assertEquals(srRoleUserDTO, result);
  }

  @Test
  public void test_setMapCountryName() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    List<ItemDataCRInside> itemDTOList = new ArrayList<>();
    ItemDataCRInside itemDataCRInside = new ItemDataCRInside();
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("TEST");
    itemDTOList.add(itemDataCRInside);
    when(datatable.getData()).thenReturn((List) itemDTOList);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(itemDTOList);
    srRoleUserBusiness.setMapCountryName();
  }

  @Test
  public void test_setMapUnitName() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    List<UnitDTO> itemDTOList = new ArrayList<>();
    UnitDTO unitDTO = new UnitDTO();
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("TEST");
    itemDTOList.add(unitDTO);
    when(datatable.getData()).thenReturn((List) itemDTOList);
    when(unitBusiness.getListUnit(null)).thenReturn(itemDTOList);
    srRoleUserBusiness.setMapUnitName();
  }

  @Test
  public void test_setMapRoleCode() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    List<SRRoleDTO> itemDTOList = new ArrayList<>();
    SRRoleDTO srRoleDTO = new SRRoleDTO();
    srRoleDTO.setRoleId(1L);
    srRoleDTO.setRoleCode("TEST");
    srRoleDTO.setCountry("281");
    itemDTOList.add(srRoleDTO);
    when(datatable.getData()).thenReturn((List) itemDTOList);
    when(srRoleBusiness.getListSRRoleByLocationCBB(any())).thenReturn(itemDTOList);
    srRoleUserBusiness.setMapRoleCode(srRoleDTO.getCountry());
  }

  @Test
  public void test_getTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    PowerMockito.when(I18n.getLanguage("srRoleUser.title")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("srRoleUser.unitName")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("srRoleUser.role")).thenReturn("3");
    PowerMockito.mockStatic(CommonExport.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("1");
    itemDataCRInside.setValueStr(1L);
    List<ItemDataCRInside> countryNameList = Mockito.spy(ArrayList.class);
    countryNameList.add(itemDataCRInside);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> unitNameList = Mockito.spy(ArrayList.class);
    unitNameList.add(unitDTO);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    srRoleDTOList.add(srRoleDTO);
    when(srRoleBusiness.getListSRRoleByLocationCBB(any())).thenReturn(srRoleDTOList);
    when(unitBusiness.getListUnit(null)).thenReturn(unitNameList);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(countryNameList);
    File result = srRoleUserBusiness.getTemplate();
    assertNotNull(result);
  }

  @Test
  public void test_exportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    PowerMockito.mockStatic(CommonExport.class);
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserDTO.setRoleUserId(1L);
    srRoleUserDTO.setIsLeader(1L);
    srRoleUserDTO.setStatus("A");
    List<SRRoleUserInSideDTO> srRoleUserDTOList = Mockito.spy(ArrayList.class);
    srRoleUserDTOList.add(srRoleUserDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(srRoleUserDTOList);
    when(srRoleUserRepository.getListDataExport(any())).thenReturn(datatable);
    File result = srRoleUserBusiness.exportData(srRoleUserDTO);
    assertNull(result);
  }

  @Test
  public void test_importData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    PowerMockito.mockStatic(CommonImport.class);
    ResultInSideDto result = srRoleUserBusiness.importData(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void test_importData_1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(CommonImport.class);
    ResultInSideDto result = srRoleUserBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_2() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(CommonImport.class);
    String[] header = new String[]{"1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        6,
        1000)).thenReturn(headerList);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = srRoleUserBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_3() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(CommonImport.class);
    String[] header = new String[]{"2", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        6,
        1000)).thenReturn(headerList);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = srRoleUserBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_4() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(CommonImport.class);
    String[] header = new String[]{"1", "1*", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        6,
        1000)).thenReturn(headerList);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = srRoleUserBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_5() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(CommonImport.class);
    String[] header = new String[]{"1", "1*", "1*", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        6,
        1000)).thenReturn(headerList);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = srRoleUserBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_6() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(CommonImport.class);
    String[] header = new String[]{"1", "1*", "1*", "1*", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        6,
        1000)).thenReturn(headerList);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = srRoleUserBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_7() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(CommonImport.class);
    String[] header = new String[]{"1", "1*", "1*", "1*", "1*", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        6,
        1000)).thenReturn(headerList);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = srRoleUserBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_8() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(CommonImport.class);
    String[] header = new String[]{"1", "1*", "1*", "1*", "1*", "1*", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        6,
        1000)).thenReturn(headerList);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = srRoleUserBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_9() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(CommonImport.class);
    String[] header = new String[]{"1", "1*", "1*", "1*", "1*", "1*", "1*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2000; i++) {
      headerList1.add(header);
    }
    when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        6,
        1000)).thenReturn(headerList);
    when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        5,
        0,
        6,
        1000)).thenReturn(headerList1);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = srRoleUserBusiness.importData(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");
  }

  @Test
  public void test_importData_10() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(CommonImport.class);
    String[] header = new String[]{"a", "a*", "a*", "a*", "a*", "a*", "a*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    String[] header1 = new String[]{"a", "a*", "a*", "a*", "a*", "a*", "a*"};
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList1.add(header1);
    List<UnitDTO> itemDTOList = new ArrayList<>();
    UnitDTO unitDTO = new UnitDTO();
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("TEST");
    itemDTOList.add(unitDTO);
    List<ItemDataCRInside> itemDataCRInsideList = new ArrayList<>();
    ItemDataCRInside itemDataCRInside = new ItemDataCRInside();
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("TEST");
    itemDataCRInsideList.add(itemDataCRInside);
    when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        6,
        1000)).thenReturn(headerList);
    when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        5,
        0,
        6,
        1000)).thenReturn(headerList1);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(itemDataCRInsideList);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    when(unitBusiness.getListUnit(null)).thenReturn(itemDTOList);
    ResultInSideDto result = srRoleUserBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }
}
