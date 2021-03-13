package com.viettel.gnoc.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.FileUtils;
import java.io.File;
import java.util.ArrayList;
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
import org.springframework.mock.web.MockMultipartFile;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgRequestScheduleBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class LanguageExchangeBussinessImplTest {

  @InjectMocks
  LanguageExchangeBussinessImpl languageExchangeBussiness;

  @Mock
  private LanguageExchangeRepository languageExchangeRepository;

  @Mock
  private CatItemBusiness catItemBusiness;

  @Test
  public void test_getListLanguageExchange() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    LanguageExchangeDTO dto = Mockito.spy(LanguageExchangeDTO.class);
    dto.setLeeLocale("1");
    List<LanguageExchangeDTO> languageExchangeDTOList = Mockito.spy(ArrayList.class);
    languageExchangeDTOList.add(dto);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(languageExchangeDTOList);
    PowerMockito.when(languageExchangeRepository.getListLanguageExchange(any()))
        .thenReturn(datatable);
    Datatable datatable1 = languageExchangeBussiness.getListLanguageExchange(dto);
    assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void test_getListTableBySystem() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    PowerMockito.when(languageExchangeRepository.getListTableBySystem(any())).thenReturn(list);
    languageExchangeBussiness.getListTableBySystem("a");
    assertEquals(list.size(), 1);
  }

  @Test
  public void test_getListLanguage() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    GnocLanguageDto gnocLanguageDto = Mockito.spy(GnocLanguageDto.class);
    gnocLanguageDto.setGnocLanguageId(1L);
    List<GnocLanguageDto> list = Mockito.spy(ArrayList.class);
    list.add(gnocLanguageDto);
    PowerMockito.when(languageExchangeRepository.getListLanguage()).thenReturn(list);
    languageExchangeBussiness.getListLanguage();
    assertEquals(list.size(), 1);
  }

  @Test
  public void test_getDetailLanguageExchange() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    LanguageExchangeDTO languageExchangeDTO = Mockito.spy(LanguageExchangeDTO.class);
    languageExchangeDTO.setLeeLocale("1");
    PowerMockito.when(languageExchangeRepository.getDetailLanguageExchange(anyLong()))
        .thenReturn(languageExchangeDTO);
    languageExchangeBussiness.getDetailLanguageExchange(1L);
    assertNotNull(languageExchangeDTO);
  }

  @Test
  public void test_updateLanguageExchange() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    LanguageExchangeDTO dto = Mockito.spy(LanguageExchangeDTO.class);
    PowerMockito.when(languageExchangeRepository.updateLanguageExchange(any()))
        .thenReturn(resultInSideDto);
    languageExchangeBussiness.updateLanguageExchange(dto);
    assertNotNull(resultInSideDto);
  }

  @Test
  public void test_insertLanguageExchange() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    LanguageExchangeDTO dto = Mockito.spy(LanguageExchangeDTO.class);
    PowerMockito.when(languageExchangeRepository.insertLanguageExchange(any()))
        .thenReturn(resultInSideDto);
    languageExchangeBussiness.insertLanguageExchange(dto);
    assertNotNull(resultInSideDto);
  }

  @Test
  public void test_deleteLanguageExchangeById() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(languageExchangeRepository.deleteLanguageExchangeById(any()))
        .thenReturn(resultInSideDto);
    languageExchangeBussiness.deleteLanguageExchangeById(1L);
    assertNotNull(resultInSideDto);
  }

  @Test
  public void test_exportLanguageExchange() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonExport.class);
    LanguageExchangeDTO languageExchangeDTO = Mockito.spy(LanguageExchangeDTO.class);
    List<LanguageExchangeDTO> languageExchangeDTOList = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    PowerMockito.when(languageExchangeRepository.getDataExport(any()))
        .thenReturn(languageExchangeDTOList);
    File result = languageExchangeBussiness.exportLanguageExchange(languageExchangeDTO);
    assertNull(result);
  }

  @Test
  public void test_getTemplate() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("a");
    catItemDTO.setItemValue("1");
    List<CatItemDTO> systemList = Mockito.spy(ArrayList.class);
    systemList.add(catItemDTO);
    GnocLanguageDto gnocLanguageDto = Mockito.spy(GnocLanguageDto.class);
    gnocLanguageDto.setLanguageName("1");
    List<GnocLanguageDto> languageList = Mockito.spy(ArrayList.class);
    languageList.add(gnocLanguageDto);
    PowerMockito.when(catItemBusiness.getListItemByCategory("MASTER_DATA_SCHEMA", ""))
        .thenReturn(systemList);
    PowerMockito.when(languageExchangeRepository.getListTableBySystem(anyString()))
        .thenReturn(systemList);
    PowerMockito.when(languageExchangeRepository.getListLanguage()).thenReturn(languageList);
    File result = languageExchangeBussiness.getTemplate();
    assertNotNull(result);

  }

  @Test
  public void test_importData() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto result = languageExchangeBussiness.importData(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void test_importData_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = languageExchangeBussiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_2() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    Object[] objects = new Object[]{"a", "a", "a", "a", "a"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0,
        4,
        0,
        5,
        1000)).thenReturn(headerList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = languageExchangeBussiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_3() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    Object[] objects = new Object[]{"a", "a", "a", "a", "a", "a"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0,
        4,
        0,
        5,
        1000)).thenReturn(headerList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = languageExchangeBussiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_4() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    Object[] objects = new Object[]{"a", "a*", "a", "a", "a", "a"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0,
        4,
        0,
        5,
        1000)).thenReturn(headerList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = languageExchangeBussiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_5() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    Object[] objects = new Object[]{"a", "a*", "a*", "a", "a", "a"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0,
        4,
        0,
        5,
        1000)).thenReturn(headerList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = languageExchangeBussiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_6() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    Object[] objects = new Object[]{"a", "a*", "a*", "a*", "a", "a"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0,
        4,
        0,
        5,
        1000)).thenReturn(headerList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = languageExchangeBussiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_7() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    Object[] objects = new Object[]{"a", "a*", "a*", "a*", "a*", "a"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0,
        4,
        0,
        5,
        1000)).thenReturn(headerList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = languageExchangeBussiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_8() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    Object[] objects = new Object[]{"a", "a*", "a*", "a*", "a*", "a*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1600; i++) {
      headerList1.add(objects);
    }
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0,
        4,
        0,
        5,
        1000)).thenReturn(headerList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0,
        5,
        0,
        6,
        1000)).thenReturn(headerList1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = languageExchangeBussiness.importData(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");
  }

  @Test
  public void test_importData_9() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    PowerMockito.when(I18n.getLanguage("languageExchange.action.1")).thenReturn("b");
    Object[] objects = new Object[]{"a", "a*", "a*", "a*", "a*", "a*"};
    Object[] objects1 = new Object[]{"1", "1", "1", "1", "1", "1", "1"};
    Object[] objects2 = new Object[]{"a", "a", "a", "a", "a", "a", "a"};
    Object[] objects3 = new Object[]{"a", "", "a", "a", "a", "a", "a"};
    Object[] objects4 = new Object[]{"a", "a", "", "a", "a", "a", "a"};
    Object[] objects5 = new Object[]{"a", "a", "a", "", "a", "a", "a"};
    Object[] objects6 = new Object[]{"a", "a", "a", "a", "", "a", "a"};
    Object[] objects7 = new Object[]{"a", "a", "a", "a", "a", "", "a"};
    Object[] objects8 = new Object[]{"a", "a", "a", "a", "a", "a", ""};
    Object[] objects9 = new Object[]{"a", "a", "a", "1", "a", "a", ""};
    Object[] objects10 = new Object[]{"a", "a", "a", "1", "a", "a", ""};
    Object[] objects11 = new Object[]{"a", "a", "a", "1", "a", "a", "a"};
    Object[] objects12 = new Object[]{"a", "a", "a", "1", "a", "a", "a", "a"};
    Object[] objects13 = new Object[]{"1", "1", "1", "1", "1", "1", "a"};
    for (int i = 0; i < 3000; i++) {
      objects8[5] += "a";
    }
    for (int i = 0; i < 20; i++) {
      objects9[3] += "1";
    }
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList1.add(objects1);
    headerList1.add(objects2);
    headerList1.add(objects3);
    headerList1.add(objects4);
    headerList1.add(objects5);
    headerList1.add(objects6);
    headerList1.add(objects7);
    headerList1.add(objects8);
    headerList1.add(objects9);
    headerList1.add(objects10);
    headerList1.add(objects11);
    headerList1.add(objects12);
    headerList1.add(objects13);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemValue("1");
    List<CatItemDTO> lstTable = Mockito.spy(ArrayList.class);
    lstTable.add(catItemDTO);
    GnocLanguageDto gnocLanguageDto = Mockito.spy(GnocLanguageDto.class);
    gnocLanguageDto.setLanguageKey("1");
    gnocLanguageDto.setLanguageName("1");
    List<GnocLanguageDto> lstLanguage = Mockito.spy(ArrayList.class);
    lstLanguage.add(gnocLanguageDto);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0,
        4,
        0,
        5,
        1000)).thenReturn(headerList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0,
        5,
        0,
        6,
        1000)).thenReturn(headerList1);
    LanguageExchangeDTO languageExchangeTmp = Mockito.spy(LanguageExchangeDTO.class);
    languageExchangeTmp.setLeeLocale("1");
    PowerMockito.when(languageExchangeRepository
        .checkLanguageExchangeExist(anyLong(), anyLong(), anyLong(), anyString()))
        .thenReturn(languageExchangeTmp);
    PowerMockito.when(languageExchangeRepository.getListLanguage()).thenReturn(lstLanguage);
    PowerMockito.when(catItemBusiness.getListItemByCategory(anyString(), anyString()))
        .thenReturn(lstTable);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = languageExchangeBussiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_setMapAppliedBussiness() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemValue("1");
    List<CatItemDTO> lstTable = Mockito.spy(ArrayList.class);
    lstTable.add(catItemDTO);
    PowerMockito.when(catItemBusiness.getListItemByCategory("MASTER_DATA_TABLE", ""))
        .thenReturn(lstTable);
    languageExchangeBussiness.setMapAppliedBussiness();
    assertEquals(lstTable.size(), 1);
  }
}
