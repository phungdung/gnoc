package com.viettel.gnoc.mr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgCrUnitTelDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.mr.repository.MrCfgCrUnitTelRepository;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import java.io.File;
import java.util.ArrayList;
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
@PrepareForTest({MrCfgCrUnitTelBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class, XSSFWorkbook.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MrCfgCrUnitTelBusinessImplTest {

  @InjectMocks
  MrCfgCrUnitTelBusinessImpl mrCfgCrUnitTelBusiness;

  @Mock
  MrCfgCrUnitTelRepository mrCfgCrUnitTelRepository;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  MrDeviceRepository mrDeviceRepository;

  @Test
  public void testGetListDataMrCfgCrUnitTelSearchWeb() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO = Mockito.spy(MrCfgCrUnitTelDTO.class);
    mrCfgCrUnitTelDTO.setMarketCode("1");
    mrCfgCrUnitTelDTO.setImplementUnit(1L);
    mrCfgCrUnitTelDTO.setCheckingUnit(1L);

    List<MrCfgCrUnitTelDTO> mrCfgCrUnitTelDTOS = Mockito.spy(ArrayList.class);
    mrCfgCrUnitTelDTOS.add(mrCfgCrUnitTelDTO);

    Datatable expected = Mockito.spy(Datatable.class);
    expected.setData(mrCfgCrUnitTelDTOS);
    PowerMockito.when(
        mrCfgCrUnitTelRepository
            .getListDataMrCfgCrUnitTelSearchWeb(any())
    ).thenReturn(expected);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> itemDataCRInsideList = Mockito.spy(ArrayList.class);
    itemDataCRInsideList.add(itemDataCRInside);
    PowerMockito.when(
        catLocationRepository.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(itemDataCRInsideList);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("UnitName");
    List<UnitDTO> unitDTOList = Mockito.spy(ArrayList.class);
    unitDTOList.add(unitDTO);
    PowerMockito.when(
        unitRepository.getListUnit(any())
    ).thenReturn(unitDTOList);

    Datatable actual = mrCfgCrUnitTelBusiness
        .getListDataMrCfgCrUnitTelSearchWeb(mrCfgCrUnitTelDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testInsertMrCfgCrUnitTel() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO = Mockito.spy(MrCfgCrUnitTelDTO.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        mrCfgCrUnitTelRepository.insertMrCfgCrUnitTel(any())
    ).thenReturn(expected);

    ResultInSideDto actual = mrCfgCrUnitTelBusiness.insertMrCfgCrUnitTel(mrCfgCrUnitTelDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testUpdateMrCfgCrUnitTel() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO = Mockito.spy(MrCfgCrUnitTelDTO.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        mrCfgCrUnitTelRepository.updateMrCfgCrUnitTel(any())
    ).thenReturn(expected);

    ResultInSideDto actual = mrCfgCrUnitTelBusiness.updateMrCfgCrUnitTel(mrCfgCrUnitTelDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFindMrCfgCrUnitTelById() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrCfgCrUnitTelDTO expected = Mockito.spy(MrCfgCrUnitTelDTO.class);
    expected.setMarketCode("1");
    expected.setImplementUnit(1L);
    expected.setCheckingUnit(1L);
    PowerMockito.when(
        mrCfgCrUnitTelRepository.findMrCfgCrUnitTelSoftWeb(anyLong())
    ).thenReturn(expected);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> itemDataCRInsideList = Mockito.spy(ArrayList.class);
    itemDataCRInsideList.add(itemDataCRInside);
    PowerMockito.when(
        catLocationRepository.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(itemDataCRInsideList);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("UnitName");
    List<UnitDTO> unitDTOList = Mockito.spy(ArrayList.class);
    unitDTOList.add(unitDTO);
    PowerMockito.when(
        unitRepository.getListUnit(any())
    ).thenReturn(unitDTOList);

    MrCfgCrUnitTelDTO actual = mrCfgCrUnitTelBusiness.findMrCfgCrUnitTelById(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDeleteMrCfgCrUnitTel() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        mrCfgCrUnitTelRepository.deleteMrCfgCrUnitTel(anyLong())
    ).thenReturn(expected);

    ResultInSideDto actual = mrCfgCrUnitTelBusiness.deleteMrCfgCrUnitTel(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testExportDataMrCfgCrUnitTel() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO = Mockito.spy(MrCfgCrUnitTelDTO.class);
    mrCfgCrUnitTelDTO.setMarketCode("1");
    mrCfgCrUnitTelDTO.setImplementUnit(1L);
    mrCfgCrUnitTelDTO.setCheckingUnit(1L);

    List<MrCfgCrUnitTelDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrCfgCrUnitTelDTO);
    PowerMockito.when(
        mrCfgCrUnitTelRepository
            .getListMrCfgCrUnitTelExport(any())
    ).thenReturn(list);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> itemDataCRInsideList = Mockito.spy(ArrayList.class);
    itemDataCRInsideList.add(itemDataCRInside);
    PowerMockito.when(
        catLocationRepository.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(itemDataCRInsideList);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("UnitName");
    List<UnitDTO> unitDTOList = Mockito.spy(ArrayList.class);
    unitDTOList.add(unitDTO);
    PowerMockito.when(
        unitRepository.getListUnit(any())
    ).thenReturn(unitDTOList);

    File actual = mrCfgCrUnitTelBusiness.exportDataMrCfgCrUnitTel(mrCfgCrUnitTelDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void testGetTemplateImport() throws Exception {
    PowerMockito.mockStatic(XSSFWorkbook.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitTel.sheetName.region")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitTel.sheetName.networkType")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitTel.sheetName.deviceType")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitTel.sheetName.unit")).thenReturn("4");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("1111");
    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
    listMarket.add(itemDataCRInside);
    PowerMockito.when(
        catLocationRepository
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(listMarket);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("2222");
    catItemDTO.setItemCode("3333");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);

    Datatable dataArray = Mockito.spy(Datatable.class);
    dataArray.setData(listArray);
    PowerMockito.when(
        catItemRepository
            .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString())
    ).thenReturn(dataArray);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitCode("1111");
    unitDTO.setUnitTrueName("2222");
    unitDTO.setParentUnitId(1L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    PowerMockito.when(
        unitRepository.getListUnit(any())
    ).thenReturn(listUnit);

    List<String> stringList = Mockito.spy(ArrayList.class);
    stringList.add("1111");
    PowerMockito.when(
        mrDeviceRepository
            .getListRegionSoftByMarketCode(anyString())
    ).thenReturn(stringList);
    PowerMockito.when(
        mrDeviceRepository.getNetworkTypeByArrayCode(anyString())
    ).thenReturn(stringList);
    PowerMockito.when(
        mrDeviceRepository
            .getDeviceTypeByNetworkType(anyString(), anyString())
    ).thenReturn(stringList);

    File actual = mrCfgCrUnitTelBusiness.getTemplateImport();

    Assert.assertNotNull(actual);
  }

  @Test
  public void testGetListRegionCombobox() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);

    List<String> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrDeviceRepository.getListRegionSoftByMarketCode(anyString())
    ).thenReturn(expected);

    List<String> actual = mrCfgCrUnitTelBusiness.getListRegionCombobox("1111");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetListNetworkTypeCombobox() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);

    List<String> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrDeviceRepository.getNetworkTypeByArrayCode(anyString())
    ).thenReturn(expected);

    List<String> actual = mrCfgCrUnitTelBusiness.getListNetworkTypeCombobox("1111");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetListDeviceTypeCombobox() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setArrayCode("1111");
    mrDeviceDTO.setNetworkType("2222");

    List<String> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrDeviceRepository
            .getDeviceTypeByNetworkType(anyString(), anyString())
    ).thenReturn(expected);

    List<String> actual = mrCfgCrUnitTelBusiness.getListDeviceTypeCombobox(mrDeviceDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testImportDataMrCfgCrUnitTel_01() {
    ResultInSideDto actual = mrCfgCrUnitTelBusiness
        .importDataMrCfgCrUnitTel(null);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportDataMrCfgCrUnitTel_02() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    ResultInSideDto actual = mrCfgCrUnitTelBusiness
        .importDataMrCfgCrUnitTel(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportDataMrCfgCrUnitTel_03() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1 (*)", "1 (*)", "1 (*)",
        "1 (*)", "1 (*)", "1 (*)", "1 (*)"
    };
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp, 0, 5, 0, 7, 1000
        )
    ).thenReturn(lstHeader);

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 501; i++) {
      lstData.add(objects);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(
            fileImp, 0, 6, 0, 7, 1000
        )
    ).thenReturn(lstData);

    ResultInSideDto actual = mrCfgCrUnitTelBusiness
        .importDataMrCfgCrUnitTel(multipartFile);

    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportDataMrCfgCrUnitTel_04() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1 (*)", "1 (*)", "1 (*)",
        "1 (*)", "1 (*)", "1 (*)", "1 (*)"
    };
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp, 0, 5, 0, 7, 1000
        )
    ).thenReturn(lstHeader);

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(
            fileImp, 0, 6, 0, 7, 1000
        )
    ).thenReturn(lstData);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
    listMarket.add(itemDataCRInside);
    PowerMockito.when(
        catLocationRepository
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(listMarket);

    List<String> stringList = Mockito.spy(ArrayList.class);
    stringList.add("1");
    PowerMockito.when(
        mrDeviceRepository
            .getListRegionSoftByMarketCode(anyString())
    ).thenReturn(stringList);
    PowerMockito.when(
        mrDeviceRepository
            .getNetworkTypeByArrayCode(anyString())
    ).thenReturn(stringList);
    PowerMockito.when(
        mrDeviceRepository
            .getDeviceTypeByNetworkType(anyString(), anyString())
    ).thenReturn(stringList);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    catItemDTOList.add(catItemDTO);

    Datatable dataArray = Mockito.spy(Datatable.class);
    dataArray.setData(catItemDTOList);
    PowerMockito.when(
        catItemRepository
            .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString())
    ).thenReturn(dataArray);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("UnitName");
    List<UnitDTO> unitDTOList = Mockito.spy(ArrayList.class);
    unitDTOList.add(unitDTO);
    PowerMockito.when(
        unitRepository.getListUnit(any())
    ).thenReturn(unitDTOList);

    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = mrCfgCrUnitTelBusiness
        .importDataMrCfgCrUnitTel(multipartFile);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testImportDataMrCfgCrUnitTel_05() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1 (*)", "1 (*)", "1 (*)",
        "1 (*)", "1 (*)", "1 (*)", "1 (*)"
    };
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp, 0, 5, 0, 7, 1000
        )
    ).thenReturn(lstHeader);

    ResultInSideDto actual = mrCfgCrUnitTelBusiness
        .importDataMrCfgCrUnitTel(multipartFile);

    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void testImportDataMrCfgCrUnitTel_06() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1 (*)", "1 (*)", "1 (*)",
        "1 (*)", "1 (*)", "1 (*)", "1 (*)"
    };
    Object[] objects1 = new Object[]{
        "1", "1", "1", "1",
        "1", "1", "1", "1"
    };
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp, 0, 5, 0, 7, 1000
        )
    ).thenReturn(lstHeader);

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(
            fileImp, 0, 6, 0, 7, 1000
        )
    ).thenReturn(lstData);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
    listMarket.add(itemDataCRInside);
    PowerMockito.when(
        catLocationRepository
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(listMarket);

    List<String> stringList = Mockito.spy(ArrayList.class);
    stringList.add("1");
    PowerMockito.when(
        mrDeviceRepository
            .getListRegionSoftByMarketCode(anyString())
    ).thenReturn(stringList);
    PowerMockito.when(
        mrDeviceRepository
            .getNetworkTypeByArrayCode(anyString())
    ).thenReturn(stringList);
    PowerMockito.when(
        mrDeviceRepository
            .getDeviceTypeByNetworkType(anyString(), anyString())
    ).thenReturn(stringList);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    catItemDTOList.add(catItemDTO);

    Datatable dataArray = Mockito.spy(Datatable.class);
    dataArray.setData(catItemDTOList);
    PowerMockito.when(
        catItemRepository
            .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString())
    ).thenReturn(dataArray);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("UnitName");
    List<UnitDTO> unitDTOList = Mockito.spy(ArrayList.class);
    unitDTOList.add(unitDTO);
    PowerMockito.when(
        unitRepository.getListUnit(any())
    ).thenReturn(unitDTOList);

    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = mrCfgCrUnitTelBusiness
        .importDataMrCfgCrUnitTel(multipartFile);

    Assert.assertNull(actual);
  }

  @Test
  public void getListUnitCombobox() {
  }

  @Test
  public void handleFileExport() {
  }

  @Test
  public void setMapArray() {
  }
}
