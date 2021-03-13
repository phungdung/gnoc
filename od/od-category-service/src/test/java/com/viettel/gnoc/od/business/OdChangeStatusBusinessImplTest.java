package com.viettel.gnoc.od.business;

import static org.junit.Assert.assertEquals;
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
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.od.dto.OdCfgBusinessDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusRoleDTO;
import com.viettel.gnoc.od.dto.OdExportCfgBusinessDTO;
import com.viettel.gnoc.od.model.OdTypeEntity;
import com.viettel.gnoc.od.repository.OdCfgBusinessRepository;
import com.viettel.gnoc.od.repository.OdChangeStatusRepository;
import com.viettel.gnoc.od.repository.OdChangeStatusRoleRepository;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
import org.springframework.mock.web.MockMultipartFile;

/**
 * @Author DungLV
 * @Since 03/20/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OdChangeStatusBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class OdChangeStatusBusinessImplTest {

  @InjectMocks
  OdChangeStatusBusinessImpl odChangeStatusBusiness;

  @Mock
  OdChangeStatusRepository odChangeStatusRepository;

  @Mock
  OdCfgBusinessRepository odCfgBusinessRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  OdChangeStatusRoleRepository odChangeStatusRoleRepository;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  public final Long od_change_status_id = 1041L;

  @Test
  public void testGetListOdCfgBusiness_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(datatable.getTotal()).thenReturn(10);
    when(odChangeStatusRepository.getListOdCfgBusiness(any())).thenReturn(datatable);
    setFinalStatic(OdChangeStatusBusinessImpl.class.getDeclaredField("log"), logger);
    Datatable result = odChangeStatusBusiness.getListOdCfgBusiness(new OdChangeStatusDTO());
    assertEquals(datatable.getTotal(), result.getTotal());
  }

  @Test
  public void testDeleteCfg_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    OdChangeStatusBusinessImpl classUnderTest = PowerMockito.spy(odChangeStatusBusiness);
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getKey()).thenReturn(RESULT.SUCCESS);
    when(odChangeStatusRepository.deleteCfg(any())).thenReturn(resultInSideDto);
    setFinalStatic(OdChangeStatusBusinessImpl.class.getDeclaredField("log"), logger);
    ResultInSideDto result = classUnderTest.deleteCfg(1041L);
    assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testInsertOrUpdateCfg_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getKey()).thenReturn(RESULT.SUCCESS);
    OdChangeStatusDTO odChangeStatusDTO = new OdChangeStatusDTO();
    odChangeStatusDTO.setOdTypeId(9L);
    odChangeStatusDTO.setOldStatus(9L);
    odChangeStatusDTO.setNewStatus(9L);
    odChangeStatusDTO.setOdPriority(9L);
    List<OdChangeStatusRoleDTO> odChangeStatusRoleDTOS = new ArrayList<>();
    odChangeStatusRoleDTOS.add(new OdChangeStatusRoleDTO(null, 1L, 1L));
    odChangeStatusDTO.setOdChangeStatusRoleDTO(odChangeStatusRoleDTOS);
    when(odChangeStatusRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);
    setFinalStatic(OdChangeStatusBusinessImpl.class.getDeclaredField("log"), logger);
    ResultInSideDto result = odChangeStatusBusiness.insertOrUpdateCfg(odChangeStatusDTO);
    assertEquals(resultInSideDto, result);

  }

  @Test
  public void testDeleteListCfg_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getMessage()).thenReturn(RESULT.SUCCESS);
    when(odChangeStatusRepository.deleteListCfg(any())).thenReturn(resultInSideDto);
    setFinalStatic(OdChangeStatusBusinessImpl.class.getDeclaredField("log"), logger);
    List<Long> input = new ArrayList<>();
    input.add(od_change_status_id);
    ResultInSideDto result = odChangeStatusBusiness.deleteListCfg(input);
    assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testDelete_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    when(odChangeStatusRepository.delete(any())).thenReturn(RESULT.SUCCESS);
    String result = odChangeStatusBusiness.delete(10L);
    assertEquals(RESULT.SUCCESS, result);
  }

  @Test
  public void testDelete_02() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<Long> listIds = new ArrayList<>();
    listIds.add(1041L);
    listIds.add(1042L);
    List<OdChangeStatusDTO> odChangeStatusDTOS = new ArrayList<>();
    OdChangeStatusDTO odChangeStatusDTO = new OdChangeStatusDTO();
    odChangeStatusDTO.setId(1041L);
    odChangeStatusDTOS.add(odChangeStatusDTO);
    odChangeStatusDTO.setId(1042L);
    odChangeStatusDTOS.add(odChangeStatusDTO);
    when(odChangeStatusRepository.checkConstraint(any())).thenReturn(RESULT.SUCCESS);
    when(odChangeStatusRepository.deleteLocaleList(any())).thenReturn(RESULT.SUCCESS);
    when(odChangeStatusRepository.deleteList(any())).thenReturn(2);
    String result = odChangeStatusBusiness.delete(odChangeStatusDTOS);
    assertEquals(RESULT.SUCCESS, result);
  }

  @Test
  public void testGetDetailCfg_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    OdChangeStatusBusinessImpl classUnderTest = PowerMockito.spy(odChangeStatusBusiness);
    PowerMockito.doNothing().when(logger).debug(any());
    OdChangeStatusDTO odChangeStatusDTO = new OdChangeStatusDTO();
    List<OdCfgBusinessDTO> odCfgBusinessDTOS = new ArrayList<>();
    OdCfgBusinessDTO odCfgBusinessDTO = new OdCfgBusinessDTO();
    odCfgBusinessDTO.setColumnName("Test");
    odCfgBusinessDTO.setIsVisible(1L);
    odChangeStatusDTO.setOdCfgBusinessDTO(odCfgBusinessDTOS);
    when(odChangeStatusRepository.findOdChangeStatusDTOById(any())).thenReturn(odChangeStatusDTO);
    setFinalStatic(OdChangeStatusBusinessImpl.class.getDeclaredField("log"), logger);
    OdChangeStatusDTO result = classUnderTest.getDetailCfg(1041L);
    assertEquals(odChangeStatusDTO, result);
  }

  @Test
  public void testExportData_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage("odCfgBusiness.yes")).thenReturn("YES");
    PowerMockito.when(I18n.getLanguage("odCfgBusiness.no")).thenReturn("NO");

    OdExportCfgBusinessDTO odExportCfgBusinessDTO = Mockito.spy(OdExportCfgBusinessDTO.class);
    odExportCfgBusinessDTO.setIsVisible(1L);
    odExportCfgBusinessDTO.setIsDefault(1L);
    odExportCfgBusinessDTO.setIsRequired(1L);
    odExportCfgBusinessDTO.setEditable(1L);
    List<OdExportCfgBusinessDTO> lstExport = Mockito.spy(ArrayList.class);
    lstExport.add(odExportCfgBusinessDTO);

    PowerMockito.when(
        odChangeStatusRepository
            .getOdCfgBusinessDataExport(any())
    ).thenReturn(lstExport);

    OdChangeStatusDTO odChangeStatusDTO = Mockito.spy(OdChangeStatusDTO.class);
    File actual = odChangeStatusBusiness.exportData(odChangeStatusDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void testExportData_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage("odCfgBusiness.yes")).thenReturn("YES");
    PowerMockito.when(I18n.getLanguage("odCfgBusiness.no")).thenReturn("NO");

    OdExportCfgBusinessDTO odExportCfgBusinessDTO = Mockito.spy(OdExportCfgBusinessDTO.class);
    odExportCfgBusinessDTO.setIsVisible(2L);
    odExportCfgBusinessDTO.setIsDefault(2L);
    odExportCfgBusinessDTO.setIsRequired(2L);
    odExportCfgBusinessDTO.setEditable(2L);
    List<OdExportCfgBusinessDTO> lstExport = Mockito.spy(ArrayList.class);
    lstExport.add(odExportCfgBusinessDTO);

    PowerMockito.when(
        odChangeStatusRepository
            .getOdCfgBusinessDataExport(any())
    ).thenReturn(lstExport);

    OdChangeStatusDTO odChangeStatusDTO = Mockito.spy(OdChangeStatusDTO.class);
    File actual = odChangeStatusBusiness.exportData(odChangeStatusDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void testImportData_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("odCfgBusiness.yes")).thenReturn("YES");

    String filePath = "/temp";
    File fileImport = new File(filePath);
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            11,
            1000
        )
    ).thenReturn(lstHeader);

    MockMultipartFile uploadFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = odChangeStatusBusiness.importData(uploadFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportData_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("odCfgBusiness.yes")).thenReturn("YES");

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);
    Object[] objects = new Object[]{"1", "1", "1", "1(*)", "1(*)", "1(*)", "1", "1", "1", "1", "1",
        "1"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);

    Object[] objects1 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 1501; i++) {
      lstData.add(objects1);
    }

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            11,
            1000
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            6,
            0,
            11,
            1000
        )
    ).thenReturn(lstData);

    MockMultipartFile uploadFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = odChangeStatusBusiness.importData(uploadFile);

    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportData_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("");
    PowerMockito.when(I18n.getLanguage("odCfgBusiness.yes")).thenReturn("YES");

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);
    Object[] objects = new Object[]{"1", "1", "1", "1(*)", "1(*)", "1(*)", "1", "1", "1", "1", "1",
        "1"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);

    Object[] objects1 = new Object[]{"22", "22", "22", "22", "22", "22", "22", "22", "22", "22",
        "22", "22"};
    Object[] objects2 = new Object[]{"33", "33", "33", "33", "33", "33", "33", "33", "33", "33",
        "33", "33"};
    Object[] objects3 = new Object[]{"22", "22", "22", "22", "22", "22", "22", "22", "22", "22",
        "22", "22"};

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects1);
    lstData.add(objects2);
    lstData.add(objects3);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            11,
            1000
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            6,
            0,
            11,
            1000
        )
    ).thenReturn(lstData);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("111");
    catItemDTO.setItemName("22");
    catItemDTO.setItemId(1L);
    List<CatItemDTO> catItemDTOS = Mockito.spy(ArrayList.class);
    catItemDTOS.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(catItemDTOS);
    PowerMockito.when(
        catItemRepository
            .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString())
    ).thenReturn(datatable);
    PowerMockito.when(
        catItemRepository.getDataItem(anyString())
    ).thenReturn(catItemDTOS);

    OdTypeEntity odTypeEntity = Mockito.spy(OdTypeEntity.class);
    odTypeEntity.setOdTypeId(2L);
    odTypeEntity.setOdTypeName("22");
    List<OdTypeEntity> odType = Mockito.spy(ArrayList.class);
    odType.add(odTypeEntity);
    PowerMockito.when(
        odChangeStatusRepository.findAllOdType()
    ).thenReturn(odType);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        odChangeStatusRepository.insertOrUpdate(any())
    ).thenReturn(expected);

    MockMultipartFile uploadFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = odChangeStatusBusiness.importData(uploadFile);

    Assert.assertEquals(RESULT.SUCCESS, expected.getKey());
  }

  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }
}
