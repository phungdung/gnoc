package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHardGroupConfigDTO;
import com.viettel.gnoc.mr.repository.MrDeviceCDRepository;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrHardGroupConfigRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrScheduleTelHisSoftBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class MrHardGroupConfigBusinessImplTest {

  @InjectMocks
  protected MrHardGroupConfigBusinessImpl mrHardGroupConfigBusiness;

  @Mock
  protected MrHardGroupConfigRepository mrHardGroupConfigRepository;

  @Mock
  MrDeviceRepository mrDeviceRepository;
  @Mock
  TicketProvider ticketProvider;
  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;
  @Mock
  MrDeviceCDRepository mrDeviceCDRepository;
  @Mock
  CatLocationBusiness catLocationBusiness;
  @Mock
  UserRepository userRepository;
  @Mock
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Test
  public void test_getListMrHardGroupConfigDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    ArrayList listData = Mockito.spy(ArrayList.class);
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    mrHardGroupConfigDTO.setMarketCode("281");
    mrHardGroupConfigDTO.setCdIdHard(5L);
    listData.add(mrHardGroupConfigDTO);
    datatable.setData(listData);
    PowerMockito.when(mrHardGroupConfigRepository.getListMrHardGroupConfigDTO(any()))
        .thenReturn(datatable);
    Datatable data = mrHardGroupConfigBusiness.getListMrHardGroupConfigDTO(mrHardGroupConfigDTO);
    assertEquals(datatable.getPages(), data.getPages());
  }

  @Test
  public void test_insert() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(mrHardGroupConfigRepository.add(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrHardGroupConfigBusiness.insert(mrHardGroupConfigDTO);
    assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void test_update() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(mrHardGroupConfigRepository.edit(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrHardGroupConfigBusiness.update(mrHardGroupConfigDTO);
    assertEquals(resultInSideDto.getKey(), result.getKey());

  }

  @Test
  public void test_getDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(mrHardGroupConfigRepository.getDetail(anyLong()))
        .thenReturn(mrHardGroupConfigDTO);
    MrHardGroupConfigDTO mrHardGroupConfigDTO1 = mrHardGroupConfigBusiness.getDetail(1L);
    assertEquals(mrHardGroupConfigDTO1, mrHardGroupConfigDTO);
  }

  @Test
  public void test_delete() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(mrHardGroupConfigRepository.delete(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = mrHardGroupConfigBusiness.delete(1L);
    assertEquals(resultInSideDto1, resultInSideDto);
  }

  @Test
  public void test_getListRegionByMarketCode() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrDeviceDTO mrDeviceDTO = PowerMockito.mock(MrDeviceDTO.class);
    List<MrDeviceDTO> mrDeviceDTOList = PowerMockito.mock(ArrayList.class);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(anyString()))
        .thenReturn(mrDeviceDTOList);
    List<MrDeviceDTO> mrDeviceDTOList1 = mrHardGroupConfigBusiness.getListRegionByMarketCode("281");
    assertEquals(mrDeviceDTOList.size(), mrDeviceDTOList1.size());
  }

  @Test
  public void test_getListNetworkTypeByArrayCode() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrDeviceDTO mrDeviceDTO = PowerMockito.mock(MrDeviceDTO.class);
    List<MrDeviceDTO> mrDeviceDTOList = PowerMockito.mock(ArrayList.class);
    PowerMockito.when(mrHardGroupConfigRepository.getListNetworkTypeByArrayCode(anyString()))
        .thenReturn(mrDeviceDTOList);
    List<MrDeviceDTO> mrDeviceDTOList1 = mrHardGroupConfigBusiness
        .getListNetworkTypeByArrayCode("281");
    assertEquals(mrDeviceDTOList.size(), mrDeviceDTOList1.size());
  }

  @Test
  public void test_setMapMarketCode() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1L");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    mrHardGroupConfigBusiness.setMapMarketCode();
    assertNotNull(lstMarketCode);
  }

  @Test
  public void test_setMapArray() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1L");
    catItemDTO.setItemName("1L");
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    catItemDTOList.add(catItemDTO);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(catItemDTOList);
    mrHardGroupConfigBusiness.setMapArray();
    assertNotNull(catItemDTOList);
  }

  @Test
  public void test_setMapCdIdHard() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> woCdGroupInsideDTOList = Mockito.spy(ArrayList.class);
    woCdGroupInsideDTOList.add(woCdGroupInsideDTO);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(woCdGroupInsideDTOList);
    mrHardGroupConfigBusiness.setMapCdIdHard();
    assertNotNull(woCdGroupInsideDTOList);
  }

  @Test
  public void test_setMapStationCode() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("5");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    lstStationCode.add(mrDeviceDTO);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb()).thenReturn(lstStationCode);
    mrHardGroupConfigBusiness.setMapStationCode();
    assertNotNull(lstStationCode);
  }

  @Test
  public void test_exportData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    PowerMockito.mockStatic(CommonExport.class);
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);

    PowerMockito.when(mrHardGroupConfigRepository.getListDataExport(mrHardGroupConfigDTO))
        .thenReturn(mrHardGroupConfigDTOList);
    File result = mrHardGroupConfigBusiness.exportData(mrHardGroupConfigDTO);
    assertNull(result);
  }

  @Test
  public void test_getTemplate() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    XSSFWorkbook workbook = Mockito.spy(XSSFWorkbook.class);
    PowerMockito.whenNew(XSSFWorkbook.class).withArguments(InputStream.class).thenReturn(workbook);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setNetworkType("1");
    List<MrDeviceDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemValue("1");
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    List<MrDeviceDTO> lstDeviceType = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.title"))
        .thenReturn("Import cấu hình nhóm điều phối BD cứng viễn thông");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("Khu vực");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrNet"))
        .thenReturn("Loại mạng-Loại thiết bị");
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    lstMarketCode.add(itemDataCRInside);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(any()))
        .thenReturn(mrDeviceDTOList);
    mrDeviceDTOList.add(mrDeviceDTO);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(catItemDTOList);
    catItemDTOList.add(catItemDTO);
    PowerMockito.when(mrDeviceRepository.getListNetworkTypeByArrayCode(anyString()))
        .thenReturn(lstNetwork);
    lstNetwork.add(mrDeviceDTO);
    PowerMockito.when(mrDeviceRepository.getListDeviceTypeByNetworkType(anyString(), anyString()))
        .thenReturn(lstDeviceType);
    lstNetwork.add(mrDeviceDTO);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    lstGroupInsideDTOS.add(woCdGroupInsideDTO);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb()).thenReturn(mrDeviceDTOList);
    lstGroupInsideDTOS.add(woCdGroupInsideDTO);
    File result = mrHardGroupConfigBusiness.getTemplate();
    assertNotNull(result);

  }

  @Test
  public void test_importData1() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void test_importData2_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);

    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importData2_2() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };

    headerList.add(header);
    for (int i = 0; i < 5000; i++) {
      dataImportList.add(header);
    }
    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");
  }

  @Test
  public void test_importData2_3() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr"),
        I18n.getLanguage("mrHardGroupConfig.region"),
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"),
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"),
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr")
    };

    headerList.add(header);
    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }

  @Test
  public void test_importData2_4() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };

    headerList.add(header);
    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "NODATA");
  }

  @Test
  public void test_importData2_5() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    headerList.add(header);
    for (int i = 0; i < 3; i++) {
      if (header.length > 0) {
        for (int j = 0; j <= 8; j++) {
          header[j] = "1";
        }
      }
    }

    dataImportList.add(header);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }

  @Test
  public void test_importData2_6() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    headerList.add(header);
    for (int i = 0; i < 3; i++) {
      if (header.length > 0) {
        for (int j = 0; j <= 8; j++) {
          header[j] = "1";
        }
      }
    }

    dataImportList.add(header);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }

  @Test
  public void test_importData2_7() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.err.marketCodeStr");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    String[] header1 = new String[]{null, null, null, null, null, null, null, null, null};
    headerList.add(header);
    dataImportList.add(header);
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_validFileFormat() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.err.marketCodeStr");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    String[] header1 = new String[]{null, null, null, null, null, null, null, null, null};
    headerList.add(header);
    dataImportList.add(header);
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }

  @Test
  public void test_validFileFormat_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.err.marketCodeStr");
    String[] header = new String[]{
        "aaa",
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*",
    };
    String[] header1 = new String[]{null, null, null, null, null, null, null, null, null};
    headerList.add(header);
    dataImportList.add(header);
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }

  @Test
  public void test_validFileFormat_2() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.err.marketCodeStr");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        "aaaa",
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*",
    };
    String[] header1 = new String[]{null, null, null, null, null, null, null, null, null};
    headerList.add(header);
    dataImportList.add(header);
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }

  @Test
  public void test_validFileFormat_3() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.err.marketCodeStr");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        "aaa",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*",
    };
    String[] header1 = new String[]{null, null, null, null, null, null, null, null, null};
    headerList.add(header);
    dataImportList.add(header);
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }

  @Test
  public void test_validFileFormat_4() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.err.marketCodeStr");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        "aaa",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*",
    };
    String[] header1 = new String[]{null, null, null, null, null, null, null, null, null};
    headerList.add(header);
    dataImportList.add(header);
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }


  @Test
  public void test_validFileFormat_5() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.err.marketCodeStr");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        "aaaa",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*",
    };
    String[] header1 = new String[]{null, null, null, null, null, null, null, null, null};
    headerList.add(header);
    dataImportList.add(header);
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }


  @Test
  public void test_validFileFormat_6() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.err.marketCodeStr");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        "aaaa",
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*",
    };
    String[] header1 = new String[]{null, null, null, null, null, null, null, null, null};
    headerList.add(header);
    dataImportList.add(header);
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }


  @Test
  public void test_validFileFormat_7() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.err.marketCodeStr");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        "aaa",
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*",
    };
    String[] header1 = new String[]{null, null, null, null, null, null, null, null, null};
    headerList.add(header);
    dataImportList.add(header);
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }

  @Test
  public void test_validFileFormat_8() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.err.marketCodeStr");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        "aaa",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*",
    };
    String[] header1 = new String[]{null, null, null, null, null, null, null, null, null};
    headerList.add(header);
    dataImportList.add(header);
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }


  @Test
  public void test_validFileFormat_9() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.err.marketCodeStr");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        "aaa",
    };
    String[] header1 = new String[]{null, null, null, null, null, null, null, null, null};
    headerList.add(header);
    dataImportList.add(header);
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORM");
  }


  @Test
  public void test_validateImportInfo() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    headerList.add(header);
    dataImportList.add(header);
    String[] header1 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);

    List<MrDeviceDTO> lstStationCode1 = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(anyString()))
        .thenReturn(lstStationCode1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void test_validateImportInfo_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
//    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setStationCode("1");
    mrDeviceDTO.setRegionHard("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    List<MrDeviceDTO> lstStationCode1 = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.region.exist"))
        .thenReturn("mrHardGroupConfig.err.region.exist");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    headerList.add(header);
    dataImportList.add(header);
    String[] header1 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(anyString()))
        .thenReturn(lstStationCode1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }


  @Test
  public void test_validateImportInfo_2() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setStationCode("1");
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setNetworkType("5");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    List<MrDeviceDTO> lstStationCode1 = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.region.exist"))
        .thenReturn("mrHardGroupConfig.err.region.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.networkType.exist"))
        .thenReturn("mrHardGroupConfig.err.networkType.exist");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    headerList.add(header);
    dataImportList.add(header);
    String[] header1 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListNetworkTypeByArrayCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_validateImportInfo_3() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setStationCode("1");
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setNetworkType("5");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    List<MrDeviceDTO> lstStationCode1 = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.region.exist"))
        .thenReturn("mrHardGroupConfig.err.region.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.networkType.exist"))
        .thenReturn("mrHardGroupConfig.err.networkType.exist");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    headerList.add(header);
    dataImportList.add(header);
    String[] header1 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListNetworkTypeByArrayCode(anyString()))
        .thenReturn(lstStationCode1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_validateImportInfo_4() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setStationCode("1");
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    List<MrDeviceDTO> lstStationCode1 = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.region.exist"))
        .thenReturn("mrHardGroupConfig.err.region.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.networkType.exist"))
        .thenReturn("mrHardGroupConfig.err.networkType.exist");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    headerList.add(header);
//    dataImportList.add(header);
    String[] header1 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListNetworkTypeByArrayCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListDeviceTypeByNetworkType(anyString(), anyString()))
        .thenReturn(lstStationCode);

    List<MrDeviceDTO> lstRegion = Mockito.spy(ArrayList.class);
    MrDeviceDTO deviceDTO = Mockito.spy(MrDeviceDTO.class);
    deviceDTO.setMarketCode("1");
    deviceDTO.setRegionHard("1");
    deviceDTO.setArrayCode("1");
    deviceDTO.setNetworkType("1");
    deviceDTO.setDeviceType("1");
    lstRegion.add(deviceDTO);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegion())
        .thenReturn(lstRegion);
    PowerMockito.when(mrDeviceRepository.getListDevice())
        .thenReturn(lstRegion);
    PowerMockito.when(mrHardGroupConfigRepository
        .findMrHardGroupConfigById(anyLong()))
        .thenReturn(new MrHardGroupConfigDTO());

    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void test_validateImportInfo_5() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setStationCode("1");
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("5");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    List<MrDeviceDTO> lstStationCode1 = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.region.exist"))
        .thenReturn("mrHardGroupConfig.err.region.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.networkType.exist"))
        .thenReturn("mrHardGroupConfig.err.networkType.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.deviceType.exist"))
        .thenReturn("mrHardGroupConfig.err.deviceType.exist");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    headerList.add(header);
    dataImportList.add(header);
    String[] header1 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListNetworkTypeByArrayCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListDeviceTypeByNetworkType(anyString(), anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);

    assertEquals(result.getKey(), "ERROR");
  }


  @Test
  public void test_validateImportInfo_6() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setStationCode("1");
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("5");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    List<MrDeviceDTO> lstStationCode1 = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.region.exist"))
        .thenReturn("mrHardGroupConfig.err.region.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.networkType.exist"))
        .thenReturn("mrHardGroupConfig.err.networkType.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.deviceType.exist"))
        .thenReturn("mrHardGroupConfig.err.deviceType.exist");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };

    headerList.add(header);
    dataImportList.add(header);
    String[] header1 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListNetworkTypeByArrayCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListDeviceTypeByNetworkType(anyString(), anyString()))
        .thenReturn(lstStationCode1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_validateImportInfo_7() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    mrHardGroupConfigDTO.setId(2L);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setStationCode("1");
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    List<MrDeviceDTO> lstStationCode1 = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.region.exist"))
        .thenReturn("mrHardGroupConfig.err.region.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.networkType.exist"))
        .thenReturn("mrHardGroupConfig.err.networkType.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.deviceType.exist"))
        .thenReturn("mrHardGroupConfig.err.deviceType.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.duplicate"))
        .thenReturn("mrHardGroupConfig.err.duplicate");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };

    headerList.add(header);
    dataImportList.add(header);
    String[] header1 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListNetworkTypeByArrayCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListDeviceTypeByNetworkType(anyString(), anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.ckeckMrHardGroupConfigExist(any()))
        .thenReturn(mrHardGroupConfigDTO);
    PowerMockito.when(mrHardGroupConfigRepository.findMrHardGroupConfigById(anyLong()))
        .thenReturn(mrHardGroupConfigDTO);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_validateImportInfo_8() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    mrHardGroupConfigDTO.setId(2L);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setStationCode("1");
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    List<MrDeviceDTO> lstStationCode1 = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.region.exist"))
        .thenReturn("mrHardGroupConfig.err.region.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.networkType.exist"))
        .thenReturn("mrHardGroupConfig.err.networkType.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.deviceType.exist"))
        .thenReturn("mrHardGroupConfig.err.deviceType.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.duplicate"))
        .thenReturn("mrHardGroupConfig.err.duplicate");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.noData"))
        .thenReturn("mrHardGroupConfig.err.noData");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };

    headerList.add(header);
    dataImportList.add(header);
    String[] header1 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    dataImportList.add(header1);

    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListNetworkTypeByArrayCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListDeviceTypeByNetworkType(anyString(), anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.ckeckMrHardGroupConfigExist(any()))
        .thenReturn(mrHardGroupConfigDTO);
    MrHardGroupConfigDTO mrHardGroupConfigDTO1 = null;
    PowerMockito.when(mrHardGroupConfigRepository.findMrHardGroupConfigById(anyLong()))
        .thenReturn(mrHardGroupConfigDTO1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }


  @Test
  public void test_validateImportInfo_9() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/mrHardGroupConfigTest.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    MrHardGroupConfigDTO mrHardGroupConfigDTO = Mockito.spy(MrHardGroupConfigDTO.class);
    mrHardGroupConfigDTO.setId(1L);
    List<MrHardGroupConfigDTO> mrHardGroupConfigDTOList = Mockito.spy(ArrayList.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setStationCode("1");
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    List<MrDeviceDTO> lstStationCode1 = Mockito.spy(ArrayList.class);
    for (long i = 0; i < 3; i++) {
      lstArrayCode.add(catItemDTO);
      lstMarketCode.add(itemDataCRInside);
      lstGroupInsideDTOS.add(woCdGroupInsideDTO);
      lstStationCode.add(mrDeviceDTO);
    }
    MrDeviceDTO mrDeviceDTO1 = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO1.setRegionHard("mrHardGroupConfig.region");
    mrDeviceDTO1.setStationCode("mrHardGroupConfig.stationCodeStr");
    mrDeviceDTO1.setNetworkType("mrHardGroupConfig.networkTypeStr");
    mrDeviceDTO1.setDeviceType("mrHardGroupConfig.deviceType");
    lstStationCode.add(mrDeviceDTO1);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id"))
        .thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr"))
        .thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region"))
        .thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr"))
        .thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.networkTypeStr"))
        .thenReturn("mrHardGroupConfig.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType"))
        .thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr"))
        .thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr"))
        .thenReturn("mrHardGroupConfig.stationCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.region.exist"))
        .thenReturn("mrHardGroupConfig.err.region.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.networkType.exist"))
        .thenReturn("mrHardGroupConfig.err.networkType.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.deviceType.exist"))
        .thenReturn("mrHardGroupConfig.err.deviceType.exist");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.duplicate"))
        .thenReturn("mrHardGroupConfig.err.duplicate");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.noData"))
        .thenReturn("mrHardGroupConfig.err.noData");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.result.import"))
        .thenReturn("mrHardGroupConfig.result.import");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.dup-code-in-file"))
        .thenReturn("mrHardGroupConfig.err.dup-code-in-file");
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType"),
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };

    headerList.add(header);
    dataImportList.add(header);
    String[] header1 = new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    dataImportList.add(header1);
    dataImportList.add(header1);
    dataImportList.add(header1);
    String filePath = "/temp";
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                4,
                0,
                8,
                1000
            ))
        .thenReturn(headerList);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                8,
                1000
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstMarketCode);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory())
        .thenReturn(lstArrayCode);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(lstGroupInsideDTOS);
    PowerMockito.when(mrDeviceRepository.getDeviceStationCodeCbb())
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.add(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrHardGroupConfigRepository.getListRegionByMarketCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListNetworkTypeByArrayCode(anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrDeviceRepository.getListDeviceTypeByNetworkType(anyString(), anyString()))
        .thenReturn(lstStationCode);
    PowerMockito.when(mrHardGroupConfigRepository.ckeckMrHardGroupConfigExist(any()))
        .thenReturn(mrHardGroupConfigDTO);
    PowerMockito.when(mrHardGroupConfigRepository.findMrHardGroupConfigById(anyLong()))
        .thenReturn(mrHardGroupConfigDTO);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrHardGroupConfigBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }
}
