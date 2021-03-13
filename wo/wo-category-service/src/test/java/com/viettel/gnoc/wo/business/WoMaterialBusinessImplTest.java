package com.viettel.gnoc.wo.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatServiceDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatServiceRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDTO;
import com.viettel.gnoc.wo.repository.WoMaterialRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
@PrepareForTest({WoMaterialBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
@Slf4j
public class WoMaterialBusinessImplTest {

  @InjectMocks
  WoMaterialBusinessImpl woMaterialBusiness;

  @Mock
  WoMaterialRepository woMaterialRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  CatServiceRepository catServiceRepository;


  @Test
  public void testGetListWoMaterialPage_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(woMaterialRepository.getListWoMaterialPage(any())).thenReturn(datatable);
    Datatable datatable1 = woMaterialBusiness.getListWoMaterialPage(any());
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void testGetListDataExport_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(woMaterialRepository.getListDataExport(any())).thenReturn(datatable);
    Datatable datatable1 = woMaterialBusiness.getListDataExport(any());
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void testDelete_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woMaterialRepository.delete(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woMaterialBusiness.delete(123L);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void testInsert_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woMaterialRepository.add(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woMaterialBusiness.insert(any());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void testUpdate_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woMaterialRepository.edit(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woMaterialBusiness.update(any());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void testFindByMaterialThresId_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MaterialThresInsideDTO materialThresDTO = Mockito.spy(MaterialThresInsideDTO.class);
    PowerMockito.when(woMaterialRepository.findByMaterialThresId(anyLong()))
        .thenReturn(materialThresDTO);
    MaterialThresInsideDTO result = woMaterialBusiness.findByMaterialThresId(10L);
    Assert.assertEquals(materialThresDTO, result);
  }

  @Test
  public void testFindAllMaterial_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoMaterialDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(woMaterialRepository.findAllMaterial(anyString()))
        .thenReturn(list);
    List<WoMaterialDTO> list1 = woMaterialBusiness.findAllMaterial("abc");
    Assert.assertEquals(list.size(), list1.size());
  }


  @Test
  public void testFindWoMaterialById_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoMaterialDTO woMaterialDTO = Mockito.spy(WoMaterialDTO.class);
    PowerMockito.when(woMaterialRepository.findWoMaterialById(anyLong()))
        .thenReturn(woMaterialDTO);
    WoMaterialDTO result = woMaterialBusiness.findWoMaterialById(10L);
    Assert.assertEquals(woMaterialDTO, result);
  }

  @Test
  public void testSetMapActionName_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable actionMaster = Mockito.spy(Datatable.class);
    List<CatItemDTO> lstActionName = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("TEST");
    lstActionName.add(catItemDTO);
    actionMaster.setData(lstActionName);
    PowerMockito.when(catItemRepository
        .getItemMaster(any(), any(), any(), any(), any())).thenReturn(actionMaster);
    woMaterialBusiness.setMapActionName();
  }

  @Test
  public void testSetMapInfraTypeName_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    List<CatItemDTO> itemDTOList = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("TEST");
    itemDTOList.add(catItemDTO);
    datatable.setData(itemDTOList);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatable);
    woMaterialBusiness.setMapInfraTypeName();
  }

  @Test
  public void testSetMapServiceName_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    List<CatServiceDTO> itemDTOList = new ArrayList<>();
    CatServiceDTO catServiceDTO = new CatServiceDTO();
    catServiceDTO.setServiceId(1L);
    catServiceDTO.setServiceName("TEST");
    itemDTOList.add(catServiceDTO);
    datatable.setData(itemDTOList);
    PowerMockito.when(catServiceRepository
        .getItemServiceMaster(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatable);
    woMaterialBusiness.setMapServiceName();
  }

  @Test
  public void testSetMapMaterialName_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoMaterialDTO> itemDTOList = Mockito.spy(ArrayList.class);
    WoMaterialDTO woMaterialDTO = new WoMaterialDTO();
    woMaterialDTO.setMaterialId(1L);
    woMaterialDTO.setMaterialName("TEST");
    itemDTOList.add(woMaterialDTO);
    PowerMockito.when(woMaterialRepository.findAllMaterial(any())).thenReturn(itemDTOList);
    woMaterialBusiness.setMapMaterialName();
  }

  @Test
  public void test_getMaterialTemplate() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    datatable.setData(list);
    CatServiceDTO catServiceDTO = Mockito.spy(CatServiceDTO.class);
    catServiceDTO.setServiceName("1");
    List<CatServiceDTO> serviceNameList = Mockito.spy(ArrayList.class);
    serviceNameList.add(catServiceDTO);
    Datatable datatable1 = Mockito.spy(Datatable.class);
    datatable1.setData(serviceNameList);
    PowerMockito.when(catServiceRepository.getItemServiceMaster(anyString(),
        anyString(), anyString(), anyString())).thenReturn(datatable1);
    PowerMockito.when(catItemRepository.getItemMaster(anyString(), anyString(),
        anyString(), anyString(), anyString())).thenReturn(datatable);
    File result = woMaterialBusiness.getMaterialTemplate();
    Assert.assertNotNull(result);
  }

  @Test
  public void test_exportData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    MaterialThresInsideDTO materialThresDTO = Mockito.spy(MaterialThresInsideDTO.class);
    List<MaterialThresInsideDTO> materialThresExportDTOList = Mockito.spy(ArrayList.class);
    materialThresExportDTOList.add(materialThresDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(materialThresExportDTOList);
    PowerMockito.when(woMaterialRepository.getListDataExport(any())).thenReturn(datatable);
    woMaterialBusiness.exportData(materialThresDTO);
  }

  @Test
  public void test_importData() throws Exception {
    ResultInSideDto result = woMaterialBusiness.importData(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void test_importData_1() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    ResultInSideDto result = woMaterialBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_2() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";

    Object[] header = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        10,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woMaterialBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_3() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";

    Object[] header = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        10,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woMaterialBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_4() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";

    Object[] header = new Object[]{"1", "1*", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        10,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woMaterialBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_5() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";

    Object[] header = new Object[]{"1", "1*", "1*", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        10,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woMaterialBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_6() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";

    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        10,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woMaterialBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_7() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";

    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        10,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woMaterialBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_8() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";

    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        10,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woMaterialBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_9() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";

    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1*", "1", "1", "1"};
    Object[] header1 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    headerList1.add(header1);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstActionName = Mockito.spy(ArrayList.class);
    lstActionName.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstActionName);
    CatServiceDTO catServiceDTO = Mockito.spy(CatServiceDTO.class);
    catServiceDTO.setServiceId(1L);
    catServiceDTO.setServiceName("1");
    List<CatServiceDTO> list = Mockito.spy(ArrayList.class);
    list.add(catServiceDTO);
    Datatable datatable1 = Mockito.spy(Datatable.class);
    datatable1.setData(list);
    WoMaterialDTO woMaterialDTO = Mockito.spy(WoMaterialDTO.class);
    woMaterialDTO.setMaterialId(1L);
    woMaterialDTO.setMaterialName("1");
    List<WoMaterialDTO> lstMaterialDTOS = Mockito.spy(ArrayList.class);
    lstMaterialDTOS.add(woMaterialDTO);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(woMaterialRepository.findAllMaterial(null)).thenReturn(lstMaterialDTOS);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatable);
    PowerMockito.when(catServiceRepository
        .getItemServiceMaster(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatable1);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        10,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        10,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woMaterialBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_importData_10() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";

    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1*", "1", "1", "1"};
    Object[] header1 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", ""};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    headerList1.add(header1);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstActionName = Mockito.spy(ArrayList.class);
    lstActionName.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstActionName);
    CatServiceDTO catServiceDTO = Mockito.spy(CatServiceDTO.class);
    catServiceDTO.setServiceId(1L);
    catServiceDTO.setServiceName("1");
    List<CatServiceDTO> list = Mockito.spy(ArrayList.class);
    list.add(catServiceDTO);
    Datatable datatable1 = Mockito.spy(Datatable.class);
    datatable1.setData(list);
    WoMaterialDTO woMaterialDTO = Mockito.spy(WoMaterialDTO.class);
    woMaterialDTO.setMaterialId(1L);
    woMaterialDTO.setMaterialName("1");
    List<WoMaterialDTO> lstMaterialDTOS = Mockito.spy(ArrayList.class);
    lstMaterialDTOS.add(woMaterialDTO);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(woMaterialRepository.findAllMaterial(null)).thenReturn(lstMaterialDTOS);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatable);
    PowerMockito.when(catServiceRepository
        .getItemServiceMaster(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatable1);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        10,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        10,
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woMaterialBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

}
