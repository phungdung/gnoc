package com.viettel.gnoc.mr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureBtsExportDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.mr.repository.MrCfgProcedureBtsRepository;
import com.viettel.gnoc.mr.repository.MrDeviceBtsRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xml.security.utils.Base64;
import org.junit.Assert;
import org.junit.Before;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrCfgProcedureBtsBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, TicketProvider.class, DataUtil.class, CommonExport.class, DateTimeUtils.class,
    PassTranformer.class, Base64.class, ExcelWriterUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MrCfgProcedureBtsBusinessImplTest {

  @InjectMocks
  MrCfgProcedureBtsBusinessImpl mrCfgProcedureBtsBusiness;

  @Mock
  MrCfgProcedureBtsRepository mrCfgProcedureBtsRepository;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  MrDeviceBtsRepository mrDeviceBtsRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrCfgProcedureBtsBusiness, "tempFolder",
        "/tempFolder");
  }

  @Test
  public void onSearch() {
  }

  @Test
  public void testExportSearchData_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    PowerMockito.mockStatic(CommonExport.class);
    MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO = Mockito.spy(MrCfgProcedureBtsDTO.class);
    List<MrCfgProcedureBtsExportDTO> lstData = Mockito.spy(ArrayList.class);
    MrCfgProcedureBtsExportDTO mrCfgProcedureBtsExportDTO = Mockito
        .spy(MrCfgProcedureBtsExportDTO.class);
    mrCfgProcedureBtsExportDTO.setCycle("12");
    mrCfgProcedureBtsExportDTO.setDeviceType("MPD");
    lstData.add(mrCfgProcedureBtsExportDTO);
    PowerMockito.when(mrCfgProcedureBtsRepository.onSearchExport(any())).thenReturn(lstData);
    File fileExport = new File("TEMPLATE_EXPORT_VN.xlsx");
    mrCfgProcedureBtsBusiness.exportSearchData(mrCfgProcedureBtsDTO);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void insertOrUpdateMrCfgProcedureBts() {
  }

  @Test
  public void deleteById() {
  }

  @Test
  public void findById() {
  }

  @Test
  public void testGetFileTemplate_01() throws Exception {
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("ghi");
    itemDataCRInside.setValueStr(1L);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(commonStreamServiceProxy.getListLocationByLevelCBBProxy(anyLong(), any()))
        .thenReturn(lstCountry);

    List<MrDeviceBtsDTO> lstFuelRefresher = Mockito.spy(ArrayList.class);
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    mrDeviceBtsDTO.setCountryName("VietNam");
    mrDeviceBtsDTO.setFuelType("OZON");
    mrDeviceBtsDTO.setAreaCode("282");
    mrDeviceBtsDTO.setProducer("VINFAST");
    lstFuelRefresher.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListfuelTypeByDeviceType(anyString(), anyString()))
        .thenReturn(lstFuelRefresher);
    PowerMockito.when(I18n.getLanguage("supplierCode.sheetName")).thenReturn("Hãng sản xuất");
    List<MrDeviceBtsDTO> lstSupplier = Mockito.spy(ArrayList.class);
    lstSupplier.add(mrDeviceBtsDTO);
    PowerMockito
        .when(mrDeviceBtsRepository.getListSupplierBtsByDeviceType(anyString(), anyString()))
        .thenReturn(lstSupplier);

    File result = mrCfgProcedureBtsBusiness.getFileTemplate();
    Assert.assertNotNull(result);
  }

  @Test
  public void testImportMrCfgProcedureBTS_01() throws Exception {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FILE_IS_NULL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    Map<String, ItemDataCRInside> mapMarket = new HashMap<>();
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("ghi");
    itemDataCRInside.setValueStr(1L);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(commonStreamServiceProxy.getListLocationByLevelCBBProxy(anyLong(), any()))
        .thenReturn(lstCountry);
    MultipartFile multipartFile = null;
    ResultInSideDto result = mrCfgProcedureBtsBusiness.importMrCfgProcedureBTS(multipartFile);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testImportMrCfgProcedureBTS_02() throws Exception {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("common.import.file.extend.invalid"))
        .thenReturn("Cần chọn file excel (.xls) để import");
    PowerMockito.mockStatic(FileUtils.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("ghi");
    itemDataCRInside.setValueStr(1L);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(commonStreamServiceProxy.getListLocationByLevelCBBProxy(anyLong(), any()))
        .thenReturn(lstCountry);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);

    ResultInSideDto result = mrCfgProcedureBtsBusiness.importMrCfgProcedureBTS(firstFile);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testImportMrCfgProcedureBTS_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1*", "1*"};
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
//    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFileNew(
        new File(filePath),
        0,
        8,
        0,
        8,
        2
    )).thenReturn(lstData);
    ResultInSideDto result = mrCfgProcedureBtsBusiness.importMrCfgProcedureBTS(firstFile);
    Assert.assertEquals(result.getKey(), "NODATA");
  }

  @Test
  public void testImportMrCfgProcedureBTS_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp.xlsx";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
//    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1*", "1*"};
    Object[] header1 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    Object[] header2 = new Object[]{"1", "", "1", "1", "1", "1", "1", "1", "1"};
    Object[] header3 = new Object[]{"1", "1", "", "1", "1", "1", "1", "1", "1"};
    Object[] header4 = new Object[]{"1", "1", "DH", "", "1", "1", "1", "1", "1"};
    Object[] header5 = new Object[]{"1", "1", "DH", "1", "", "1", "1", "1", "1"};
    Object[] header6 = new Object[]{"1", "1", "DH", "1", "1", "", "1", "1", "1"};
    Object[] header7 = new Object[]{"1", "1", "DH", "1", "1", "1", "", "1", "1"};
    Object[] header8 = new Object[]{"1", "1", "DH", "1", "1", "1", "1", "", "1"};
    Object[] header9 = new Object[]{"1", "1", "DH", "1", "1", "1", "1", "1", ""};

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
//    lstData.add(header);
    lstData.add(header1);
    lstData.add(header2);
    lstData.add(header3);
    lstData.add(header4);
    lstData.add(header5);
    lstData.add(header6);
    lstData.add(header7);
    lstData.add(header8);
    lstData.add(header9);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("ghi");
    itemDataCRInside.setValueStr(1L);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(commonStreamServiceProxy.getListLocationByLevelCBBProxy(anyLong(), any()))
        .thenReturn(lstCountry);

    List<MrDeviceBtsDTO> lstFuelRefresher = Mockito.spy(ArrayList.class);
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    mrDeviceBtsDTO.setCountryName("VietNam");
    mrDeviceBtsDTO.setFuelType("OZON");
    mrDeviceBtsDTO.setAreaCode("282");
    mrDeviceBtsDTO.setProducer("VINFAST");
    lstFuelRefresher.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListfuelTypeByDeviceType(anyString(), anyString()))
        .thenReturn(lstFuelRefresher);
    PowerMockito.when(I18n.getLanguage("mrMaterial.list.grid.sheetName"))
        .thenReturn("Loại nhiên liệu_gas");
    PowerMockito.when(I18n.getLanguage("supplierCode.sheetName")).thenReturn("Hãng sản xuất");

    List<MrDeviceBtsDTO> lstSupplier = Mockito.spy(ArrayList.class);
    lstSupplier.add(mrDeviceBtsDTO);
    PowerMockito
        .when(mrDeviceBtsRepository.getListSupplierBtsByDeviceType(anyString(), anyString()))
        .thenReturn(lstSupplier);

    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFileNew(
        new File(filePath),
        0,
        8,
        0,
        8,
        2
    )).thenReturn(lstData);
    List<MrCfgProcedureBtsDTO> lst = null;
    PowerMockito.when(mrCfgProcedureBtsRepository.checkDupp(any())).thenReturn(lst);

    ResultInSideDto result = mrCfgProcedureBtsBusiness.importMrCfgProcedureBTS(firstFile);
    Assert.assertEquals(result.getKey(), "ERROR");
  }

}
