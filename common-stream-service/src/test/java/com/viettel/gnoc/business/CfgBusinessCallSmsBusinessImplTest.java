package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CfgBusinessCallSmsDTO;
import com.viettel.gnoc.commons.dto.CfgBusinessCallSmsUserDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.repository.CfgBusinessCallSmsRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.mock.web.MockMultipartFile;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgBusinessCallSmsBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class CfgBusinessCallSmsBusinessImplTest {

  @InjectMocks
  CfgBusinessCallSmsBusinessImpl cfgBusinessCallSmsBusiness;

  @Mock
  CfgBusinessCallSmsRepository cfgBusinessCallSmsRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CatItemRepository catItemRepository;

  @Test
  public void getListCfgBusinessCallSmsDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable expected = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgBusinessCallSmsRepository.getListCfgBusinessCallSmsDTO(any()))
        .thenReturn(expected);

    CfgBusinessCallSmsDTO callSmsDTO = Mockito.spy(CfgBusinessCallSmsDTO.class);
    Datatable actual = cfgBusinessCallSmsBusiness.getListCfgBusinessCallSmsDTO(callSmsDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getListWoCdGroupCBB() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<WoCdGroupInsideDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(cfgBusinessCallSmsRepository.getListWoCdGroupCBB()).thenReturn(expected);

    List<WoCdGroupInsideDTO> actual = cfgBusinessCallSmsBusiness.getListWoCdGroupCBB();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void insert() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(cfgBusinessCallSmsRepository.add(any())).thenReturn(expected);
    PowerMockito.when(cfgBusinessCallSmsRepository.addUser(any())).thenReturn(expected);
    PowerMockito.when(expected.getId()).thenReturn(1L);
    PowerMockito.when(expected.getKey()).thenReturn(RESULT.SUCCESS);

    CfgBusinessCallSmsUserDTO cfgBusinessCallSmsUserDTO = Mockito
        .spy(CfgBusinessCallSmsUserDTO.class);
    List<CfgBusinessCallSmsUserDTO> list = Mockito.spy(ArrayList.class);
    list.add(cfgBusinessCallSmsUserDTO);
    CfgBusinessCallSmsDTO callSmsDTO = Mockito.spy(CfgBusinessCallSmsDTO.class);
    callSmsDTO.setLstCfgBusinessCallSmsUser(list);
    ResultInSideDto actual = cfgBusinessCallSmsBusiness.insert(callSmsDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void update_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(cfgBusinessCallSmsRepository.edit(any())).thenReturn(expected);
    PowerMockito.when(expected.getKey()).thenReturn(RESULT.SUCCESS);
    PowerMockito.when(cfgBusinessCallSmsRepository.deleteUser(anyLong())).thenReturn(expected);

    CfgBusinessCallSmsUserDTO check = Mockito.spy(CfgBusinessCallSmsUserDTO.class);
    PowerMockito.when(cfgBusinessCallSmsRepository
        .ckeckUserExist(anyLong(), anyLong())).thenReturn(check);
    PowerMockito.when(cfgBusinessCallSmsRepository.editUser(any())).thenReturn(expected);

    List<Long> list = Mockito.spy(ArrayList.class);
    list.add(1L);
    CfgBusinessCallSmsUserDTO cfgBusinessCallSmsUserDTO = Mockito
        .spy(CfgBusinessCallSmsUserDTO.class);
    cfgBusinessCallSmsUserDTO.setId(1L);
    List<CfgBusinessCallSmsUserDTO> list1 = Mockito.spy(ArrayList.class);
    list1.add(cfgBusinessCallSmsUserDTO);
    CfgBusinessCallSmsDTO callSmsDTO = Mockito.spy(CfgBusinessCallSmsDTO.class);
    callSmsDTO.setLstDelete(list);
    callSmsDTO.setLstCfgBusinessCallSmsUser(list1);
    ResultInSideDto actual = cfgBusinessCallSmsBusiness.update(callSmsDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void update_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(cfgBusinessCallSmsRepository.edit(any())).thenReturn(expected);
    PowerMockito.when(expected.getKey()).thenReturn(RESULT.SUCCESS);
    PowerMockito.when(cfgBusinessCallSmsRepository.deleteUser(anyLong())).thenReturn(expected);

    CfgBusinessCallSmsUserDTO check = Mockito.spy(CfgBusinessCallSmsUserDTO.class);
    PowerMockito.when(cfgBusinessCallSmsRepository
        .ckeckUserExist(anyLong(), anyLong())).thenReturn(check);
    PowerMockito.when(cfgBusinessCallSmsRepository.addUser(any())).thenReturn(expected);

    List<Long> list = Mockito.spy(ArrayList.class);
    list.add(1L);
    CfgBusinessCallSmsUserDTO cfgBusinessCallSmsUserDTO = Mockito
        .spy(CfgBusinessCallSmsUserDTO.class);
    List<CfgBusinessCallSmsUserDTO> list1 = Mockito.spy(ArrayList.class);
    list1.add(cfgBusinessCallSmsUserDTO);
    CfgBusinessCallSmsDTO callSmsDTO = Mockito.spy(CfgBusinessCallSmsDTO.class);
    callSmsDTO.setLstDelete(list);
    callSmsDTO.setLstCfgBusinessCallSmsUser(list1);
    ResultInSideDto actual = cfgBusinessCallSmsBusiness.update(callSmsDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    CfgBusinessCallSmsDTO expected = Mockito.spy(CfgBusinessCallSmsDTO.class);
    PowerMockito.when(cfgBusinessCallSmsRepository.getDetail(anyLong()))
        .thenReturn(expected);

    CfgBusinessCallSmsDTO actual = cfgBusinessCallSmsBusiness.getDetail(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void delete() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(cfgBusinessCallSmsRepository.delete(1L))
        .thenReturn(expected);

    ResultInSideDto actual = cfgBusinessCallSmsBusiness.delete(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void setMapConfigType() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> lstCfgType = Mockito.spy(ArrayList.class);
    lstCfgType.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstCfgType);

    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), any(), any(), any(), any()))
        .thenReturn(datatable);

    cfgBusinessCallSmsBusiness.setMapConfigType();
    Assert.assertNotNull(lstCfgType);
  }

  @Test
  public void exportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);

    List<CfgBusinessCallSmsDTO> cfgBusinessCallSmsDTOList = Mockito.spy(ArrayList.class);
    PowerMockito.when(cfgBusinessCallSmsRepository
        .getListDataExport(any())).thenReturn(cfgBusinessCallSmsDTOList);

    CfgBusinessCallSmsDTO callSmsDTO = Mockito.spy(CfgBusinessCallSmsDTO.class);
    File actual = cfgBusinessCallSmsBusiness.exportData(callSmsDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void getTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);

    PowerMockito.when(I18n.getLanguage(anyString()))
        .thenReturn("test");
    PowerMockito.when(I18n.getLanguage("cfgBusinessCallSms.title"))
        .thenReturn("title");
    PowerMockito.when(I18n.getLanguage("cfgBusinessCallSms.cdName"))
        .thenReturn("cdName");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> listCfgType = Mockito.spy(ArrayList.class);
    listCfgType.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listCfgType);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), any(), any(), any(), any()))
        .thenReturn(datatable);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    List<WoCdGroupInsideDTO> woGroupList = Mockito.spy(ArrayList.class);
    woGroupList.add(woCdGroupInsideDTO);
    PowerMockito.when(cfgBusinessCallSmsRepository.getListWoCdGroupCBB())
        .thenReturn(woGroupList);

    File actual = cfgBusinessCallSmsBusiness.getTemplate();

    Assert.assertNotNull(actual);
  }

  @Test
  public void importData_01() {
    MockMultipartFile testFile = null;
    ResultInSideDto actual = cfgBusinessCallSmsBusiness.importData(testFile);
    Assert.assertNull(testFile);
  }

  @Test
  public void importData_02() {
    PowerMockito.mockStatic(CommonImport.class);

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = cfgBusinessCallSmsBusiness.importData(testFile);
    Assert.assertNotNull(actual);
  }

  @Test
  public void importData_03() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);

    PowerMockito.when(I18n.getLanguage(anyString()))
        .thenReturn("1");

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );

    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any()))
        .thenReturn(filePath);
    File fileImport = new File(filePath);
    Object[] objects = new Object[]{"1", "1*", "1*", "1*", "1*", "1*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        4,
        0,
        5,
        1000))
        .thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        5,
        1000
    )).thenReturn(dataImportList);

    ResultInSideDto actual = cfgBusinessCallSmsBusiness.importData(testFile);
    Assert.assertNotNull(actual);
  }

  @Test
  public void importData_04() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);

    PowerMockito.when(I18n.getLanguage(anyString()))
        .thenReturn("1");

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );

    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any()))
        .thenReturn(filePath);
    File fileImport = new File(filePath);
    Object[] objects = new Object[]{"1", "1*", "1*", "1*", "1*", "1*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        4,
        0,
        5,
        1000))
        .thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        5,
        1000
    )).thenReturn(dataImportList);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> lstCfgType = Mockito.spy(ArrayList.class);
    lstCfgType.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstCfgType);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), any(), any(), any(), any()))
        .thenReturn(datatable);

    ResultInSideDto actual = cfgBusinessCallSmsBusiness.importData(testFile);

    Assert.assertNotNull(actual);
  }


}
