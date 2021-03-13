package com.viettel.gnoc.od.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.od.dto.OdTypeDetailDTO;
import com.viettel.gnoc.od.dto.OdTypeExportDTO;
import com.viettel.gnoc.od.repository.OdTypeDetailRepository;
import com.viettel.gnoc.od.repository.OdTypeRepository;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * @Aythor DungLV
 * @Since 03/20/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OdTypeBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class OdTypeBusinessImplTest {

  @InjectMocks
  OdTypeBusinessImpl odTypeBusiness;

  @Mock
  OdTypeRepository odTypeRepository;
  @Mock
  CatItemRepository catItemRepository;
  @Mock
  OdTypeDetailRepository odTypeDetailRepository;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void testSearch_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(datatable.getTotal()).thenReturn(10);
    when(odTypeRepository.search(any())).thenReturn(datatable);

    setFinalStatic(OdTypeBusinessImpl.class.getDeclaredField("log"), logger);
    Datatable result = odTypeBusiness.search(new OdTypeDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void testGetListOdType_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(datatable.getTotal()).thenReturn(10);
    when(odTypeRepository.getListOdType(any())).thenReturn(datatable);

    setFinalStatic(OdTypeBusinessImpl.class.getDeclaredField("log"), logger);
    Datatable result = odTypeBusiness.getListOdType(new OdTypeDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void testDelete_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(odTypeRepository.delete(any())).thenReturn(resultInSideDto);

    setFinalStatic(OdTypeBusinessImpl.class.getDeclaredField("log"), logger);
    ResultInSideDto result = odTypeBusiness.delete(123L);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testDeleteList_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(odTypeRepository.deleteList(any())).thenReturn(resultInSideDto);

    setFinalStatic(OdTypeBusinessImpl.class.getDeclaredField("log"), logger);
    Long[] input = {10L, 10L};
    ResultInSideDto result = odTypeBusiness.deleteList(Arrays.asList(input));
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testGetDetail_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    OdTypeDTO odTypeDTO = new OdTypeDTO();
    List<CatItemDTO> lstOdGroupType = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    CatItemDTO catItemDTO1 = new CatItemDTO();
    catItemDTO.setItemId(1L);
    catItemDTO1.setItemId(2L);
    lstOdGroupType.add(catItemDTO);
    lstOdGroupType.add(catItemDTO1);
    List<OdTypeDetailDTO> odTypeDetailDTOS = new ArrayList<>();
    OdTypeDetailDTO odTypeDetailDTO = new OdTypeDetailDTO();
    odTypeDetailDTO.setPriorityId(1L);
    odTypeDetailDTOS.add(odTypeDetailDTO);
    odTypeDTO.setOdTypeDetailDTOS(odTypeDetailDTOS);
    when(odTypeRepository.getDetail(any())).thenReturn(odTypeDTO);
    when(catItemRepository.getDataItem(CATEGORY.OD_PRIORITY)).thenReturn(lstOdGroupType);

    setFinalStatic(OdTypeBusinessImpl.class.getDeclaredField("log"), logger);
    OdTypeDTO result = odTypeBusiness.getDetail(10L);
    assertEquals(odTypeDTO, result);
  }

  @Test
  public void testAdd_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(odTypeRepository.add(any())).thenReturn(resultInSideDto);

    setFinalStatic(OdTypeBusinessImpl.class.getDeclaredField("log"), logger);
    OdTypeDTO odTypeDTO = new OdTypeDTO();
    ResultInSideDto result = odTypeBusiness.add(odTypeDTO);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testEdit_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(odTypeRepository.edit(any())).thenReturn(resultInSideDto);

    setFinalStatic(OdTypeBusinessImpl.class.getDeclaredField("log"), logger);
    OdTypeDTO odTypeDTO = new OdTypeDTO();
    ResultInSideDto result = odTypeBusiness.edit(odTypeDTO);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testGetSeqOdType_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    when(odTypeRepository.getSeqOdType(any())).thenReturn("getSeqOdType");

    setFinalStatic(OdTypeBusinessImpl.class.getDeclaredField("log"), logger);
    String result = odTypeBusiness.getSeqOdType();
    assertEquals("getSeqOdType", result);
  }

  @Test
  public void testSetMapOdGroupTypeName_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CatItemDTO> lstOdGroupType = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("Test");
    lstOdGroupType.add(catItemDTO);
    when(catItemRepository.getDataItem(CATEGORY.OD_GROUP_TYPE)).thenReturn(lstOdGroupType);

    setFinalStatic(OdTypeBusinessImpl.class.getDeclaredField("log"), logger);
    odTypeBusiness.setMapOdGroupTypeName();
  }

  @Test
  public void testSetMapPriorityName_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CatItemDTO> lstOdGroupType = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("Test");
    lstOdGroupType.add(catItemDTO);
    when(catItemRepository.getDataItem(CATEGORY.OD_PRIORITY)).thenReturn(lstOdGroupType);

    setFinalStatic(OdTypeBusinessImpl.class.getDeclaredField("log"), logger);
    odTypeBusiness.setMapPriorityName();
  }

  @Test
  public void testExportData_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstPriority = Mockito.spy(ArrayList.class);
    lstPriority.add(catItemDTO);
    PowerMockito.when(
        catItemRepository.getDataItem(anyString())
    ).thenReturn(lstPriority);

    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(
        odTypeRepository
            .getListDataExport(any())
    ).thenReturn(datatable);
    OdTypeExportDTO odTypeExportDTO1 = Mockito.spy(OdTypeExportDTO.class);
    odTypeExportDTO1.setStatus("1");
    OdTypeExportDTO odTypeExportDTO2 = Mockito.spy(OdTypeExportDTO.class);
    odTypeExportDTO2.setStatus("2");
    List<OdTypeExportDTO> odTypeExportDTOS = Mockito.spy(ArrayList.class);
    odTypeExportDTOS.add(odTypeExportDTO1);
    odTypeExportDTOS.add(odTypeExportDTO2);
    datatable.setData(odTypeExportDTOS);

    OdTypeDTO odTypeDTO = Mockito.spy(OdTypeDTO.class);
    File actual = odTypeBusiness.exportData(odTypeDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void testImportData_01() throws Exception {
    OdTypeBusinessImpl classUnderTest = PowerMockito.spy(odTypeBusiness);
    PowerMockito.mockStatic(I18n.class);
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");

    PowerMockito.mockStatic(CommonImport.class);
    List<Object[]> lstHeader = new ArrayList<>();
    lstHeader.add(new Object[100]);
    List<Object[]> lstData = new ArrayList<>();
    lstData.add(new Object[100]);
    when(CommonImport.getDataFromExcelFile(any(File.class), anyInt(),//sheet
        anyInt(),//begin row
        anyInt(),//from column
        anyInt(),//to column
        anyInt())).thenReturn(lstHeader).thenReturn(lstData);

    // Mock private method
    PowerMockito.doReturn(true).when(classUnderTest, "validFileFormat", any());

    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    setFinalStatic(OdTypeBusinessImpl.class.getDeclaredField("log"), logger);
    ResultInSideDto result = classUnderTest.importData(multipartFile);
    assertNull(result);
  }

  @Test
  public void testGetTemplate() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    List<CatItemDTO> lstOdGroupType = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("11111");
    lstOdGroupType.add(catItemDTO);

    PowerMockito.when(
        catItemRepository
            .getDataItem(anyString())
    ).thenReturn(lstOdGroupType);

    File actual = odTypeBusiness.getTemplate();
    Assert.assertNotNull(actual);
  }

  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }

}
