package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHardUnitConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrHardUnitConfigRepository;
import com.viettel.gnoc.mr.repository.MrITHardScheduleRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrHardUnitConfigBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrHardUnitConfigBusinessImplTest {

  @InjectMocks
  MrHardUnitConfigBusinessImpl mrHardUnitConfigBusiness;

  @Mock
  MrHardUnitConfigRepository mrHardUnitConfigRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Mock
  MrDeviceRepository mrDeviceRepository;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Mock
  MrITHardScheduleBusiness mrITHardScheduleBusiness;

  @Mock
  MrITHardScheduleRepository mrITHardScheduleRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrHardUnitConfigBusiness, "tempFolder",
        "./test_junit");
  }


  @Test
  public void getListMrHardUnitConfigDTO() {
    MrHardUnitConfigDTO mrHardUnitConfigDTO = Mockito.spy(MrHardUnitConfigDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    when(mrHardUnitConfigRepository.getListMrHardGroupConfigDTO(any())).thenReturn(datatable);
    Datatable result = mrHardUnitConfigBusiness.getListMrHardUnitConfigDTO(mrHardUnitConfigDTO);
    assertEquals(datatable.getPages(), result.getPages());
  }

  @Test
  public void insert() {
    MrHardUnitConfigDTO mrHardUnitConfigDTO = Mockito.spy(MrHardUnitConfigDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrHardUnitConfigRepository.add(any())).thenReturn(resultInSideDto);

    ResultInSideDto result = mrHardUnitConfigBusiness.insert(mrHardUnitConfigDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void update() {
    MrHardUnitConfigDTO mrHardUnitConfigDTO = Mockito.spy(MrHardUnitConfigDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrHardUnitConfigRepository.edit(any())).thenReturn(resultInSideDto);

    ResultInSideDto result = mrHardUnitConfigBusiness.update(mrHardUnitConfigDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void getDetail() {
    MrHardUnitConfigDTO mrHardUnitConfigDTO = Mockito.spy(MrHardUnitConfigDTO.class);
    when(mrHardUnitConfigRepository.getDetail(any())).thenReturn(mrHardUnitConfigDTO);
    MrHardUnitConfigDTO result = mrHardUnitConfigBusiness.getDetail(1L);
    assertEquals(result, mrHardUnitConfigDTO);
  }

  @Test
  public void delete() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrHardUnitConfigRepository.delete(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrHardUnitConfigBusiness.delete(1L);
    assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void getListRegionByMarketCode() {
    List<MrDeviceDTO> lst = Mockito.spy(ArrayList.class);
    when(mrHardUnitConfigRepository.getListRegionByMarketCode(any())).thenReturn(lst);
    List<MrDeviceDTO> result = mrHardUnitConfigBusiness.getListRegionByMarketCode("1");
    assertEquals(result.size(), lst.size());

  }

  @Test
  public void getListNetworkTypeByArrayCode() {
    List<MrDeviceDTO> lst = Mockito.spy(ArrayList.class);
    when(mrHardUnitConfigRepository.getListNetworkTypeByArrayCode(any())).thenReturn(lst);
    List<MrDeviceDTO> result = mrHardUnitConfigBusiness.getListNetworkTypeByArrayCode("1");
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void test_setMapMarketCode() {
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(69L);
    itemDataCRInside.setDisplayStr("ABC");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    PowerMockito.when(catLocationRepository.getListLocationByLevelCBB(any(),anyLong(),anyLong())).thenReturn(lstMarketCode);
    mrHardUnitConfigBusiness.setMapMarketCode();
  }

  @Test
  public void setMapArray() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("123");
    catItemDTO.setItemName("BCS");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    PowerMockito.when(catItemRepository.getListItemByCategoryAndParent(anyString(),anyString())).thenReturn(lstArrayCode);
    mrHardUnitConfigBusiness.setMapArray();
  }

  @Test
  public void test_setMapStationCode() {
    MrSynItDevicesDTO dto = Mockito.spy(MrSynItDevicesDTO.class);
    dto.setStation("LIZ");
    List<MrSynItDevicesDTO> lstStationCode = Mockito.spy(ArrayList.class);
    lstStationCode.add(dto);
    PowerMockito.when(mrSynItSoftDevicesRepository.getDeviceITStationCodeCbb()).thenReturn(lstStationCode);
    mrHardUnitConfigBusiness.setMapStationCode();
  }

  @Test
  public void exportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrHardUnitConfigDTO mrHardUnitConfigDTO = Mockito.spy(MrHardUnitConfigDTO.class);
    List<MrHardUnitConfigDTO> mrHardUnitConfigDTOList = Mockito.spy(ArrayList.class);
    when(mrHardUnitConfigRepository.getListDataExport(any())).thenReturn(mrHardUnitConfigDTOList);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    mrHardUnitConfigBusiness.exportData(mrHardUnitConfigDTO);
  }

  @Test
  public void test_getTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("GHJ");
    itemDataCRInside.setValueStr(69L);
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    PowerMockito.when(catLocationRepository.getListLocationByLevelCBB(any(),anyLong(),anyLong())).thenReturn(lstMarketCode);

    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setRegion("KV1");
    List<MrITHardScheduleDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    mrDeviceDTOList.add(mrITHardScheduleDTO);
    PowerMockito.when(mrITHardScheduleRepository.getListRegionByMrSynItDevices(any())).thenReturn(mrDeviceDTOList);

    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("RTO");
    catItemDTO.setItemValue("OKI");
    lstArrayCode.add(catItemDTO);
    PowerMockito.when(catItemRepository.getListItemByCategoryAndParent(anyString(),anyString())).thenReturn(lstArrayCode);

    List<MrSynItDevicesDTO> lstDeviceType = Mockito.spy(ArrayList.class);
    MrSynItDevicesDTO deviceDTO = Mockito.spy(MrSynItDevicesDTO.class);
    deviceDTO.setDeviceType("OIIU");
    deviceDTO.setStation("DMM");
    lstDeviceType.add(deviceDTO);
    PowerMockito.when(mrSynItSoftDevicesRepository
        .getListDeviceTypeByArrayCode(anyString())).thenReturn(lstDeviceType);

    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupName("OKIBG");
    lstGroupInsideDTOS.add(woCdGroupInsideDTO);
    PowerMockito.when(woCategoryServiceProxy
        .getListCdGroupByUser(any())).thenReturn(lstGroupInsideDTOS);

    List<MrSynItDevicesDTO> lstStationCode = Mockito.spy(ArrayList.class);
    lstStationCode.add(deviceDTO);
    PowerMockito.when(mrSynItSoftDevicesRepository.getDeviceITStationCodeCbb()).thenReturn(lstStationCode);
    PowerMockito.when(I18n.getLanguage("mrHardUnitConfig.title")).thenReturn("Import cấu hình nhóm điều phối BD cứng viễn thông");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region")).thenReturn("Khu vực");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.arrDevice")).thenReturn("Mảng-loại thiết bị");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.id")).thenReturn("mrHardGroupConfig.id");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr")).thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.region")).thenReturn("mrHardGroupConfig.region");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.marketCodeStr")).thenReturn("mrHardGroupConfig.marketCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrayCodeStr")).thenReturn("mrHardGroupConfig.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.deviceType")).thenReturn("mrHardGroupConfig.deviceType");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.cdIdHardStr")).thenReturn("mrHardGroupConfig.cdIdHardStr");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.stationCodeStr")).thenReturn("mrHardGroupConfig.stationCodeStr");
    File fileExport = mrHardUnitConfigBusiness.getTemplate();
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void importData() {
    MultipartFile multipartFile = null;
    mrHardUnitConfigBusiness.importData(multipartFile);
  }

  @Test
  public void importData1() {
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSXs");
    mrHardUnitConfigBusiness.importData(multipartFile);
  }

  @Test
  public void importData2() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");

    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        4,
        0,
        7,
        1000
    )).thenReturn(headerList);

    mrHardUnitConfigBusiness.importData(multipartFile);
  }

  @Test
  public void importData3() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");

    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);

    //<editor-fold desc dunglv test defaultstate="collapsed">
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
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.title"))
        .thenReturn("mrHardGroupConfig.title");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrNet"))
        .thenReturn("mrHardGroupConfig.arrNet");
    //      </editor-fold>
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
//        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType") + "*",
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        4,
        0,
        7,
        1000
    )).thenReturn(headerList);

    Object[] objectstest = new Object[9];
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1501; i++) {
      dataImportList.add(objectstest);
    }
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        7,
        1000
    )).thenReturn(dataImportList);

    mrHardUnitConfigBusiness.importData(multipartFile);
  }

  @Test
  public void importData4() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");

    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);

    //<editor-fold desc dunglv test defaultstate="collapsed">
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
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.title"))
        .thenReturn("mrHardGroupConfig.title");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrNet"))
        .thenReturn("mrHardGroupConfig.arrNet");
    //      </editor-fold>
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
//        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType") + "*",
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        4,
        0,
        7,
        1000
    )).thenReturn(headerList);

    Object[] objectstest = new Object[9];
    for (int j = 0; j < objectstest.length; j++) {
      objectstest[j] = "1";

    }
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objectstest);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        7,
        1000
    )).thenReturn(dataImportList);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(lstArrayCode);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("1");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    lstStationCode.add(mrDeviceDTO);
    when(mrDeviceRepository.getDeviceStationCodeCbb()).thenReturn(lstStationCode);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    lstGroupInsideDTOS.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lstGroupInsideDTOS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrHardUnitConfigRepository.add(any())).thenReturn(resultInSideDto);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrHardUnitConfigBusiness.importData(multipartFile);
  }

  @Test
  public void importData5() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");

    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);

    //<editor-fold desc dunglv test defaultstate="collapsed">
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
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.title"))
        .thenReturn("mrHardGroupConfig.title");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.arrNet"))
        .thenReturn("mrHardGroupConfig.arrNet");
    //      </editor-fold>
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrHardGroupConfig.id"),
        I18n.getLanguage("mrHardGroupConfig.marketCodeStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.region") + "*",
        I18n.getLanguage("mrHardGroupConfig.arrayCodeStr") + "*",
//        I18n.getLanguage("mrHardGroupConfig.networkTypeStr"),
        I18n.getLanguage("mrHardGroupConfig.deviceType") + "*",
        I18n.getLanguage("mrHardGroupConfig.cdIdHardStr") + "*",
        I18n.getLanguage("mrHardGroupConfig.stationCodeStr") + "*"
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        4,
        0,
        7,
        1000
    )).thenReturn(headerList);

    Object[] objectstest = new Object[9];
    for (int j = 0; j < objectstest.length; j++) {
      if (j == 1) {
        objectstest[j] = "t";
      } else {
        objectstest[j] = "1";
      }

    }
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objectstest);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        7,
        1000
    )).thenReturn(dataImportList);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(2L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("2");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(lstArrayCode);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStationCode("2");
    List<MrDeviceDTO> lstStationCode = Mockito.spy(ArrayList.class);
    lstStationCode.add(mrDeviceDTO);
    when(mrDeviceRepository.getDeviceStationCodeCbb()).thenReturn(lstStationCode);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(2L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstGroupInsideDTOS = Mockito.spy(ArrayList.class);
    lstGroupInsideDTOS.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lstGroupInsideDTOS);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrHardUnitConfigRepository.add(any())).thenReturn(resultInSideDto);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrHardUnitConfigBusiness.importData(multipartFile);
  }

  @Test
  public void validateImportInfo() {
    PowerMockito.mockStatic(I18n.class);
    MrHardUnitConfigDTO mrHardUnitConfigDTO = Mockito.spy(MrHardUnitConfigDTO.class);
    List<MrHardUnitConfigDTO> list = Mockito.spy(ArrayList.class);
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
    mrHardUnitConfigDTO.setMarketCodeStr("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
    mrHardUnitConfigDTO.setRegionStr("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
    mrHardUnitConfigDTO.setArrayCodeStr("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
    mrHardUnitConfigDTO.setCdIdHardStr("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
    mrHardUnitConfigDTO.setStationCodeStr("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
    mrHardUnitConfigDTO.setDeviceType("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
    mrHardUnitConfigDTO.setDeviceType(null);
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
    mrHardUnitConfigDTO.setMarketCode("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
    mrHardUnitConfigDTO.setArrayCode("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
    mrHardUnitConfigDTO.setCdIdHard(1L);
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
    mrHardUnitConfigDTO.setStationCode("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setRegionHard("1");
    List<MrDeviceDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    mrDeviceDTOList.add(mrDeviceDTO);
    when(mrHardUnitConfigRepository.getListRegionByMarketCode(any())).thenReturn(mrDeviceDTOList);
    mrHardUnitConfigDTO.setRegionStr("2");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);

    mrHardUnitConfigDTO.setRegionStr("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);

    MrDeviceDTO mrDeviceDTONetwork = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTONetwork.setNetworkType("1");
    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTONetwork);
    when(mrDeviceRepository.getListNetworkTypeByArrayCode(mrHardUnitConfigDTO.getArrayCode()))
        .thenReturn(lstNetwork);
    mrHardUnitConfigDTO.setNetworkTypeStr("2");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);

    mrHardUnitConfigDTO.setNetworkTypeStr("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);

    mrHardUnitConfigDTO.setDeviceTypeStr("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);

    MrDeviceDTO mrDeviceDTOType = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTOType.setDeviceType("1");
    List<MrDeviceDTO> lstDeviceType = Mockito.spy(ArrayList.class);
    lstDeviceType.add(mrDeviceDTOType);
    when(mrDeviceRepository.getListDeviceTypeByNetworkType(any(), any())).thenReturn(lstDeviceType);
    mrHardUnitConfigDTO.setDeviceTypeStr("2");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);

    mrHardUnitConfigDTO.setDeviceTypeStr("1");
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);

    MrHardUnitConfigDTO mrHardGroupConfig = Mockito.spy(MrHardUnitConfigDTO.class);
    mrHardGroupConfig.setId(2L);
    when(mrHardUnitConfigRepository.ckeckMrHardGroupConfigExist(mrHardUnitConfigDTO))
        .thenReturn(mrHardGroupConfig);
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);

    mrHardUnitConfigDTO.setId(1L);
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);

    MrHardUnitConfigDTO dto = Mockito.spy(MrHardUnitConfigDTO.class);
    when(mrHardUnitConfigRepository.findMrHardGroupConfigById(mrHardUnitConfigDTO.getId()))
        .thenReturn(dto);
    mrHardUnitConfigBusiness.validateImportInfo(mrHardUnitConfigDTO, list);
  }

  @Test
  public void validateDuplicate() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.result.import")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrHardGroupConfig.err.dup-code-in-file")).thenReturn("1");

    MrHardUnitConfigDTO mrHardUnitConfigDTO = Mockito.spy(MrHardUnitConfigDTO.class);
    mrHardUnitConfigDTO.setResultImport("1");
    mrHardUnitConfigDTO.setMarketCode("1");
    mrHardUnitConfigDTO.setRegion("1");
    mrHardUnitConfigDTO.setArrayCode("1");
    mrHardUnitConfigDTO.setNetworkType("1");
    mrHardUnitConfigDTO.setDeviceType("1");
    mrHardUnitConfigDTO.setCdIdHard(1L);
    mrHardUnitConfigDTO.setStationCode("1");
    List<MrHardUnitConfigDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrHardUnitConfigDTO);

    mrHardUnitConfigBusiness.validateDuplicate(list, mrHardUnitConfigDTO);
  }
}
