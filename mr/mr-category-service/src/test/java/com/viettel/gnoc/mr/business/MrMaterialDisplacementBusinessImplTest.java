package com.viettel.gnoc.mr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrMaterialDTO;
import com.viettel.gnoc.maintenance.dto.MrMaterialDisplacementDTO;
import com.viettel.gnoc.mr.repository.MrMaterialDisplacementRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
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
import org.springframework.mock.web.MockMultipartFile;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrMaterialDisplacementBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, TicketProvider.class, DataUtil.class, CommonExport.class, XSSFWorkbook.class,
    ExcelWriterUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MrMaterialDisplacementBusinessImplTest {

  @InjectMocks
  MrMaterialDisplacementBusinessImpl mrMaterialDisplacementBusiness;

  @Mock
  private MrMaterialDisplacementRepository mrMaterialDisplacementRepository;

  @Mock
  private CatLocationBusiness catLocationBusiness;

  @Mock
  private CatItemRepository catItemRepository;

  @Test
  public void testGetListMrMaterialDisplacementDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrMaterialDisplacementDTO mrMaterialDisplacementDTO = Mockito
        .spy(MrMaterialDisplacementDTO.class);

    List<MrMaterialDisplacementDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrMaterialDisplacementRepository
            .getListMrMaterialDisplacementDTO(any())
    ).thenReturn(expected);

    List<MrMaterialDisplacementDTO> actual = mrMaterialDisplacementBusiness
        .getListMrMaterialDisplacementDTO(mrMaterialDisplacementDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetListMrMaterialDTO2() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrMaterialDTO mrMaterialDTO = Mockito.spy(MrMaterialDTO.class);
    mrMaterialDTO.setDeviceType("1");
    mrMaterialDTO.setDateTime(new Date());

    List<MrMaterialDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrMaterialDTO);
    Datatable expected = Mockito.spy(Datatable.class);
    expected.setData(lst);
    PowerMockito.when(
        mrMaterialDisplacementRepository
            .getListMrMaterialDTO2(any())
    ).thenReturn(expected);

    Datatable actual = mrMaterialDisplacementBusiness
        .getListMrMaterialDTO2(mrMaterialDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrMaterialDTO expected = Mockito.spy(MrMaterialDTO.class);
    PowerMockito.when(
        mrMaterialDisplacementRepository.getDetail(anyLong())
    ).thenReturn(expected);

    MrMaterialDTO actual = mrMaterialDisplacementBusiness
        .getDetail(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testExportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrMaterialDTO mrMaterialDTO = Mockito.spy(MrMaterialDTO.class);
    mrMaterialDTO.setDeviceType("1");
    mrMaterialDTO.setDateTime(new Date());

    List<MrMaterialDTO> lstEx = Mockito.spy(ArrayList.class);
    lstEx.add(mrMaterialDTO);
    PowerMockito.when(
        mrMaterialDisplacementRepository.getDataExport(any())
    ).thenReturn(lstEx);

    File actual = mrMaterialDisplacementBusiness
        .exportData(mrMaterialDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void testInsertMrMaterial_01() {
    PowerMockito.mockStatic(I18n.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrMaterialDTO mrMaterialDTO = Mockito.spy(MrMaterialDTO.class);
    mrMaterialDTO.setMaterialName("MaterialName");
    mrMaterialDTO.setDeviceType("1");
    mrMaterialDTO.setMarketCode(1L);

    List<MrMaterialDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrMaterialDTO);
    PowerMockito.when(
        mrMaterialDisplacementRepository.checkListDuplicate(any())
    ).thenReturn(lst);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .insertMrMaterial(mrMaterialDTO);

    Assert.assertEquals(RESULT.DUPLICATE, actual.getKey());
  }

  @Test
  public void testInsertMrMaterial_02() {
    PowerMockito.mockStatic(I18n.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrMaterialDTO mrMaterialDTO = Mockito.spy(MrMaterialDTO.class);
    mrMaterialDTO.setMaterialName("MaterialName");
    mrMaterialDTO.setDeviceType("1");
    mrMaterialDTO.setMarketCode(1L);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        mrMaterialDisplacementRepository
            .insertOrUpdateMrMaterial(any())
    ).thenReturn(expected);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .insertMrMaterial(mrMaterialDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testInsertMrMaterial_03() {
    PowerMockito.mockStatic(I18n.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrMaterialDTO mrMaterialDTO = Mockito.spy(MrMaterialDTO.class);
    mrMaterialDTO.setMaterialName("MaterialName");
    mrMaterialDTO.setDeviceType("1");
    mrMaterialDTO.setMarketCode(1L);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(RESULT.ERROR);
    PowerMockito.when(
        mrMaterialDisplacementRepository
            .insertOrUpdateMrMaterial(any())
    ).thenReturn(expected);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .insertMrMaterial(mrMaterialDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testUpdateMrMaterial_01() {
    PowerMockito.mockStatic(I18n.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrMaterialDTO mrMaterialDTO = Mockito.spy(MrMaterialDTO.class);
    mrMaterialDTO.setMaterialName("MaterialName");
    mrMaterialDTO.setDeviceType("1");
    mrMaterialDTO.setMarketCode(1L);
    mrMaterialDTO.setMaterialId(1L);

    MrMaterialDTO mrMaterialDTO1 = Mockito.spy(MrMaterialDTO.class);
    mrMaterialDTO1.setMaterialName("MaterialName");
    mrMaterialDTO1.setDeviceType("1");
    mrMaterialDTO1.setMarketCode(1L);
    mrMaterialDTO1.setMaterialId(2L);

    List<MrMaterialDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrMaterialDTO1);
    PowerMockito.when(
        mrMaterialDisplacementRepository.checkListDuplicate(any())
    ).thenReturn(lst);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .updateMrMaterial(mrMaterialDTO);

    Assert.assertEquals(RESULT.DUPLICATE, actual.getKey());
  }

  @Test
  public void testUpdateMrMaterial_02() {
    PowerMockito.mockStatic(I18n.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrMaterialDTO mrMaterialDTO = Mockito.spy(MrMaterialDTO.class);
    mrMaterialDTO.setMaterialName("MaterialName");
    mrMaterialDTO.setDeviceType("1");
    mrMaterialDTO.setMarketCode(1L);
    mrMaterialDTO.setMaterialId(1L);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        mrMaterialDisplacementRepository
            .insertOrUpdateMrMaterial(any())
    ).thenReturn(expected);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .updateMrMaterial(mrMaterialDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testUpdateMrMaterial_03() {
    PowerMockito.mockStatic(I18n.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrMaterialDTO mrMaterialDTO = Mockito.spy(MrMaterialDTO.class);
    mrMaterialDTO.setMaterialName("MaterialName");
    mrMaterialDTO.setDeviceType("1");
    mrMaterialDTO.setMarketCode(1L);
    mrMaterialDTO.setMaterialId(1L);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(RESULT.ERROR);
    PowerMockito.when(
        mrMaterialDisplacementRepository
            .insertOrUpdateMrMaterial(any())
    ).thenReturn(expected);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .updateMrMaterial(mrMaterialDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testDeleteMrMaterial_01() {
    PowerMockito.mockStatic(I18n.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        mrMaterialDisplacementRepository
            .deleteMrMaterial(anyLong())
    ).thenReturn(expected);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .deleteMrMaterial(1L);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testDeleteMrMaterial_02() {
    PowerMockito.mockStatic(I18n.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(RESULT.ERROR);
    PowerMockito.when(
        mrMaterialDisplacementRepository
            .deleteMrMaterial(anyLong())
    ).thenReturn(expected);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .deleteMrMaterial(1L);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testGetTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");
    PowerMockito.when(I18n.getLanguage("MATERIAL.title")).thenReturn("0");
    PowerMockito.when(I18n.getLanguage("MATERIAL.materialNameEN")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("MATERIAL.deviceTypeName")).thenReturn("3");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountry);

    File actual = mrMaterialDisplacementBusiness.getTemplate();

    Assert.assertNotNull(actual);
  }

  @Test
  public void testImportData_01() {
    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .importData(null);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportData_02() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 8, 1000)
    ).thenReturn(headerList);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .importData(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportData_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{"1", "1*", "1*", "1*", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 8, 1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{"1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 1510; i++) {
      dataImportList.add(objects1);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(fileImp, 0, 8, 0, 8, 1000)
    ).thenReturn(dataImportList);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .importData(multipartFile);

    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportData_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{"1", "1*", "1*", "1*", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 8, 1000)
    ).thenReturn(headerList);

    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(fileImp, 0, 8, 0, 8, 1000)
    ).thenReturn(dataImportList);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .importData(multipartFile);

    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void testImportData_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{"1", "1*", "1*", "1*", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 8, 1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{"1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(fileImp, 0, 8, 0, 8, 1000)
    ).thenReturn(dataImportList);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountry);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("ItemName");
    List<CatItemDTO> itemDTOList = Mockito.spy(ArrayList.class);
    itemDTOList.add(catItemDTO);
    Datatable woGroupTypeMaster = Mockito.spy(Datatable.class);
    woGroupTypeMaster.setData(itemDTOList);
    PowerMockito.when(
        catItemRepository
            .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString())
    ).thenReturn(woGroupTypeMaster);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .importData(multipartFile);

    Assert.assertNull(actual);
  }

  @Test
  public void testImportData_06() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("2");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{"1", "1*", "1*", "1*", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 8, 1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{"1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(fileImp, 0, 8, 0, 8, 1000)
    ).thenReturn(dataImportList);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountry);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("ItemName");
    List<CatItemDTO> itemDTOList = Mockito.spy(ArrayList.class);
    itemDTOList.add(catItemDTO);
    Datatable woGroupTypeMaster = Mockito.spy(Datatable.class);
    woGroupTypeMaster.setData(itemDTOList);
    PowerMockito.when(
        catItemRepository
            .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString())
    ).thenReturn(woGroupTypeMaster);

    PowerMockito.when(
        CommonExport
            .exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = mrMaterialDisplacementBusiness
        .importData(multipartFile);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }
}
