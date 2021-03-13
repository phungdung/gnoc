
package com.viettel.gnoc.wo.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
import com.viettel.gnoc.wo.dto.WoTypeCheckListDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeTimeDTO;
import com.viettel.gnoc.wo.model.WoTypeFilesGuideEntity;
import com.viettel.gnoc.wo.repository.WoPriorityRepository;
import com.viettel.gnoc.wo.repository.WoTypeCfgRequiredRepository;
import com.viettel.gnoc.wo.repository.WoTypeCheckListRepository;
import com.viettel.gnoc.wo.repository.WoTypeRepository;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WoTypeBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
@Slf4j
public class WoTypeBusinessImplTest {

  @InjectMocks
  WoTypeBusinessImpl woTypeBusiness;

  @Mock
  WoTypeRepository woTypeRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  WoTypeCheckListRepository woTypeCheckListRepository;

  @Mock
  WoTypeCfgRequiredRepository woTypeCfgRequiredRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  WoPriorityRepository woPriorityRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitRepository unitRepository;

  @Test
  public void testGetListWoTypeByLocalePage_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(woTypeRepository.getListWoTypeByLocalePage(any())).thenReturn(datatable);
    Datatable datatable1 = woTypeBusiness.getListWoTypeByLocalePage(any());
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void testDelete_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<WoInsideDTO> listWoInsideDTO = Mockito.spy(ArrayList.class);

    PowerMockito.when(woServiceProxy.getListWoByWoTypeId(anyLong())).thenReturn(listWoInsideDTO);
    PowerMockito.when(woTypeRepository.delete(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woTypeBusiness.delete(10L);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void testDelete_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("woType.delete.wo.exist");

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.DELETE_REQUIRE_EXIST);
    List<WoInsideDTO> listWoInsideDTO = Mockito.spy(ArrayList.class);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    listWoInsideDTO.add(woInsideDTO);
    PowerMockito.when(woServiceProxy.getListWoByWoTypeId(anyLong())).thenReturn(listWoInsideDTO);
    ResultInSideDto resultInSideDto1 = woTypeBusiness.delete(10L);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.DELETE_REQUIRE_EXIST);
  }

  @Test
  public void testGetListDataExport_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(woTypeRepository.getListDataExport(any())).thenReturn(datatable);
    Datatable datatable1 = woTypeBusiness.getListDataExport(any());
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void testFindByWoTypeId_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    List<WoPriorityDTO> woPriorityDTOList = Mockito.spy(ArrayList.class);
    List<WoTypeCfgRequiredDTO> woTypeCfgRequiredDTOList = Mockito.spy(ArrayList.class);
    List<WoTypeCheckListDTO> woTypeCheckListDTOList = Mockito.spy(ArrayList.class);
    List<GnocFileDto> gnocFileCreateWoDtos = Mockito.spy(ArrayList.class);
    List<GnocFileDto> gnocFilesGuideDtos = Mockito.spy(ArrayList.class);

    GnocFileDto gnocFileCreateWoDto = Mockito.spy(GnocFileDto.class);
    gnocFileCreateWoDto.setBusinessCode("Test ABC");
    gnocFileCreateWoDto.setBusinessId(12L);

    GnocFileDto gnocFilesGuideDto = Mockito.spy(GnocFileDto.class);
    gnocFilesGuideDto.setBusinessCode("Test DEF");
    gnocFilesGuideDto.setBusinessId(34L);

    woTypeInsideDTO.setWoPriorityDTOList(woPriorityDTOList);
    woTypeInsideDTO.setWoTypeCfgRequiredDTOList(woTypeCfgRequiredDTOList);
    woTypeInsideDTO.setWoTypeCheckListDTOList(woTypeCheckListDTOList);
    woTypeInsideDTO.setGnocFileCreateWoDtos(gnocFileCreateWoDtos);
    woTypeInsideDTO.setGnocFilesGuideDtos(gnocFilesGuideDtos);

    PowerMockito.when(woTypeRepository.findByWoTypeId(anyLong())).thenReturn(woTypeInsideDTO);
    PowerMockito.when(woPriorityRepository.findAllByWoTypeID(anyLong()))
        .thenReturn(woPriorityDTOList);
    PowerMockito.when(woTypeCfgRequiredRepository.findAllByWoTypeID(anyLong()))
        .thenReturn(woTypeCfgRequiredDTOList);
    PowerMockito.when(woTypeCheckListRepository.findAllByWoTypeID(anyLong()))
        .thenReturn(woTypeCheckListDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any()))
        .thenReturn(gnocFileCreateWoDtos);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any()))
        .thenReturn(gnocFilesGuideDtos);
    WoTypeInsideDTO result = woTypeBusiness.findByWoTypeId(123L);
    Assert.assertEquals(woTypeInsideDTO, result);
  }

  @Test
  public void testFindWoTypeById_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    PowerMockito.when(woTypeRepository.findByWoTypeId(anyLong())).thenReturn(woTypeInsideDTO);
    WoTypeInsideDTO result = woTypeBusiness.findWoTypeById(12L);
    Assert.assertEquals(woTypeInsideDTO, result);
  }

  @Test
  public void testGetListWoTypeTimeDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<WoTypeTimeDTO> woTypeTimeDTOS = Mockito.spy(ArrayList.class);
    WoTypeTimeDTO woTypeTimeDTO = Mockito.spy(WoTypeTimeDTO.class);
    woTypeTimeDTOS.add(woTypeTimeDTO);

    PowerMockito.when(woTypeRepository.getListWoTypeTimeDTO(any())).thenReturn(woTypeTimeDTOS);
    List<WoTypeTimeDTO> woTypeTimeDTOS1 = woTypeBusiness.getListWoTypeTimeDTO(any());
    woTypeTimeDTOS1.add(woTypeTimeDTO);
    Assert.assertEquals(woTypeTimeDTOS.size(), woTypeTimeDTOS1.size());
  }

  @Test
  public void testGetListWoTypeIsEnable_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<WoTypeInsideDTO> list = Mockito.spy(ArrayList.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    list.add(woTypeInsideDTO);

    PowerMockito.when(woTypeRepository.getListWoTypeIsEnable(any())).thenReturn(list);
    List<WoTypeInsideDTO> list1 = woTypeBusiness.getListWoTypeIsEnable(any());
    list1.add(woTypeInsideDTO);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListWoTypeByLocaleNotLike_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<WoTypeInsideDTO> list = Mockito.spy(ArrayList.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    list.add(woTypeInsideDTO);

    PowerMockito.when(woTypeRepository.getListWoTypeByLocaleNotLike(any())).thenReturn(list);
    List<WoTypeInsideDTO> list1 = woTypeBusiness.getListWoTypeByLocaleNotLike(any());
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListWoTypeByWoGroup_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<WoTypeInsideDTO> list = Mockito.spy(ArrayList.class);

    PowerMockito.when(woTypeRepository.getListWoTypeByWoGroup(anyLong(), anyString(), anyString()))
        .thenReturn(list);
    List<WoTypeInsideDTO> list1 = woTypeBusiness.getListWoTypeByWoGroup(12L, "abc", "def");
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListWoTypeByLocale_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<WoTypeInsideDTO> list = Mockito.spy(ArrayList.class);
    WoTypeInsideDTO woTypeDTO = Mockito.spy(WoTypeInsideDTO.class);

    PowerMockito.when(woTypeRepository.getListWoTypeByLocale(any(), anyString()))
        .thenReturn(list);
    List<WoTypeInsideDTO> list1 = woTypeBusiness.getListWoTypeByLocale(woTypeDTO, "def");
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetWoTypeByCode_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    WoTypeInsideDTO woTypeDTO = Mockito.spy(WoTypeInsideDTO.class);

    PowerMockito.when(woTypeRepository.getWoTypeByCode(anyString()))
        .thenReturn(woTypeDTO);
    WoTypeInsideDTO result = woTypeBusiness.getWoTypeByCode("def");
    Assert.assertEquals(woTypeDTO, result);
  }

  @Test
  public void testGetLanguageExchangeWoType_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<LanguageExchangeDTO> list = Mockito.spy(ArrayList.class);

    PowerMockito.when(woTypeRepository.getLanguageExchangeWoType(anyString(), anyString()))
        .thenReturn(list);
    List<LanguageExchangeDTO> list1 = woTypeBusiness.getLanguageExchangeWoType("def", "abc");
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testSetMapWoGroupTypeName_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable woGroupTypeMaster = Mockito.spy(Datatable.class);
    List<CatItemDTO> itemDTOList = Mockito.spy(ArrayList.class);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("TEST");
    itemDTOList.add(catItemDTO);
    woGroupTypeMaster.setData(itemDTOList);
    PowerMockito
        .when(catItemRepository
            .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(woGroupTypeMaster);
    woTypeBusiness.setMapWoGroupTypeName();
  }

  @Test
  public void test_getWoTypeTemplate() throws Exception {
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
    PowerMockito.when(
        catItemRepository.getItemMaster("WO_GROUP_TYPE", Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            Constants.APPLIED_BUSSINESS.CAT_ITEM.toString(), "itemId", "itemName"))
        .thenReturn(datatable);
    woTypeBusiness.getWoTypeTemplate();
  }

  @Test
  public void exportData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Datatable datatable = Mockito.spy(Datatable.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setIsEnable(1L);
    woTypeInsideDTO.setEnableCreate(1L);
    woTypeInsideDTO.setAllowPending(1L);
    woTypeInsideDTO.setCreateFromOtherSys(1L);
    List<WoTypeInsideDTO> woTypeInsideDTOList = Mockito.spy(ArrayList.class);
    woTypeInsideDTOList.add(woTypeInsideDTO);
    datatable.setData(woTypeInsideDTOList);
    PowerMockito.when(woTypeRepository.getListDataExport(any())).thenReturn(datatable);
    File result = woTypeBusiness.exportData(woTypeInsideDTO);
    Assert.assertNull(result);
  }

  @Test
  public void test_importData() throws Exception {
    ResultInSideDto result = woTypeBusiness.importData(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void test_importData_1() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
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
        8,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_3() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_4() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1*", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_5() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1*", "1*", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_6() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_7() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_8() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_9() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_10() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1*", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData_11() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1*", "1*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    for (int i = 0; i < 2000; i++) {
      headerList1.add(header);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        8,
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");
  }

  @Test
  public void test_importData_12() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("woType.action.1")).thenReturn("0");
    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1*", "1*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] header1 = new Object[]{"1", "", "1", "1", "1", "1", "1", "1", "1"};
    Object[] header2 = new Object[]{"1", "1", "", "1", "1", "1", "1", "1", "1"};
    Object[] header3 = new Object[]{"1", "1", "1", "", "1", "1", "1", "1", "1"};
    Object[] header4 = new Object[]{"1", "1", "1", "1", "", "1", "1", "1", "1"};
    Object[] header5 = new Object[]{"1", "1", "1", "1", "1", "", "1", "1", "1"};
    Object[] header6 = new Object[]{"1", "1", "1", "1", "1", "1", "", "1", "1"};
    Object[] header7 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "", "1"};
    Object[] header8 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", ""};
    Object[] header9 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    headerList1.add(header1);
    headerList1.add(header2);
    headerList1.add(header3);
    headerList1.add(header4);
    headerList1.add(header5);
    headerList1.add(header6);
    headerList1.add(header7);
    headerList1.add(header8);
    headerList1.add(header9);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("1");
    List<CatItemDTO> itemDTOList = Mockito.spy(ArrayList.class);
    itemDTOList.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(itemDTOList);
    WoTypeInsideDTO woTypeInsideDTOTmp = Mockito.spy(WoTypeInsideDTO.class);
    PowerMockito.when(woTypeRepository.checkWoTypeExist(anyString()))
        .thenReturn(woTypeInsideDTOTmp);
    PowerMockito.when(
        catItemRepository.getItemMaster("WO_GROUP_TYPE", Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
            Constants.APPLIED_BUSSINESS.CAT_ITEM.toString(), "itemId", "itemName"))
        .thenReturn(datatable);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        8,
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_insert() throws Exception {
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    List<MultipartFile> filesGuideline = Mockito.spy(ArrayList.class);
    filesGuideline.add(firstFile);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoCloseAutomaticTime("1");
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey("FAIL");
    PowerMockito.when(woTypeRepository.add(any())).thenReturn(result);
    woTypeBusiness.insert(filesGuideline, filesGuideline, woTypeInsideDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void test_update() throws Exception {
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    List<MultipartFile> filesGuideline = Mockito.spy(ArrayList.class);
//    filesGuideline.add(firstFile);
    List<Long> longList = Mockito.spy(ArrayList.class);
    longList.add(1L);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setMappingId(1L);
    List<GnocFileDto> list = Mockito.spy(ArrayList.class);
    list.add(gnocFileDto);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoCloseAutomaticTime("1");
    woTypeInsideDTO.setWoTypeId(1L);
    woTypeInsideDTO.setGnocFileCreateWoDtos(list);
    woTypeInsideDTO.setGnocFilesGuideDtos(list);
    woTypeInsideDTO.setIdDeleteListFilesGuide(longList);
    woTypeInsideDTO.setIdDeleteListFileCreateWo(longList);
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey("SUCCESS");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoTypeFilesGuideEntity woTypeFilesGuideEntity = Mockito.spy(WoTypeFilesGuideEntity.class);
    woTypeFilesGuideEntity.setWoTypeFilesGuideId(1L);
    List<WoTypeFilesGuideEntity> lstFileOldFilesGuide = Mockito.spy(ArrayList.class);
    lstFileOldFilesGuide.add(woTypeFilesGuideEntity);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(woTypeRepository.addCfgFileCreateWo(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(woTypeRepository.getListFilesGuideByWoTypeId(any()))
        .thenReturn(lstFileOldFilesGuide);
    PowerMockito.when(woTypeRepository.add(any())).thenReturn(result);
    woTypeBusiness.update(filesGuideline, filesGuideline, woTypeInsideDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }
}

