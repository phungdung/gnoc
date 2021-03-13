package com.viettel.gnoc.mr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.mr.repository.MrChecklistBtsDetailRepository;
import com.viettel.gnoc.mr.repository.MrChecklistBtsRepository;
import com.viettel.gnoc.mr.repository.MrDeviceBtsRepository;
import java.io.File;
import java.io.IOException;
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
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrChecklistBtsBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class MrChecklistBtsBusinessImplTest {

  @InjectMocks
  MrChecklistBtsBusinessImpl mrChecklistBtsBusiness;
  @Mock
  MrChecklistBtsRepository mrChecklistBtsRepository;

  @Mock
  MrChecklistBtsDetailRepository mrChecklistBtsDetailRepository;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  MrDeviceBtsRepository mrDeviceBtsRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(mrChecklistBtsBusiness, "tempFolder",
        "./mr-temp");
  }
  private String pathTemplate = "templates" + File.separator + "maintenance" + File.separator;

  @Test
  public void getListDataSearchWeb() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Datatable datatable = Mockito.spy(Datatable.class);
    MrChecklistsBtsDTO mrChecklistsBtsDTO = Mockito.spy(MrChecklistsBtsDTO.class);
    List<MrChecklistsBtsDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrChecklistsBtsDTO);
    datatable.setData(list);
    //set map(start)
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> itemDataCRInsideList = Mockito.spy(ArrayList.class);
    itemDataCRInsideList.add(itemDataCRInside);
    Datatable datatCycle = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    catItemDTOList.add(catItemDTO);
    datatCycle.setData(catItemDTOList);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatCycle);
    PowerMockito.when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(itemDataCRInsideList);
    //set map(end)
    PowerMockito.when(mrChecklistBtsRepository.getListDataSearchWeb(any())).thenReturn(datatable);
    Datatable datatable1 = mrChecklistBtsBusiness.getListDataSearchWeb(mrChecklistsBtsDTO);
    Assert.assertEquals(datatable1.getPages(), datatable.getPages());
  }

  @Test
  public void insertMrChecklistBts() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO = Mockito.spy(MrChecklistsBtsDetailDTO.class);
    mrChecklistsBtsDetailDTO.setChecklistId(1L);
    List<MrChecklistsBtsDetailDTO> mrChecklistsBtsDetailDTOS = Mockito.spy(ArrayList.class);
    mrChecklistsBtsDetailDTOS.add(mrChecklistsBtsDetailDTO);
    MrChecklistsBtsDTO mrChecklistsBtsDTO = Mockito.spy(MrChecklistsBtsDTO.class);
    mrChecklistsBtsDTO.setListDetail(mrChecklistsBtsDetailDTOS);
    List<MrChecklistsBtsDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrChecklistsBtsDTO);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(mrChecklistBtsDetailRepository.insertMrChecklistBtsDetail(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrChecklistBtsRepository.insertMrChecklistBts(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = mrChecklistBtsBusiness.insertMrChecklistBts(mrChecklistsBtsDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());

  }

  @Test
  public void updateMrChecklistBts_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO = Mockito.spy(MrChecklistsBtsDetailDTO.class);
    mrChecklistsBtsDetailDTO.setChecklistId(1L);
    mrChecklistsBtsDetailDTO.setChecklistDetailId(1L);
    List<MrChecklistsBtsDetailDTO> mrChecklistsBtsDetailDTOS = Mockito.spy(ArrayList.class);
    mrChecklistsBtsDetailDTOS.add(mrChecklistsBtsDetailDTO);
    MrChecklistsBtsDTO mrChecklistsBtsDTO = Mockito.spy(MrChecklistsBtsDTO.class);
    mrChecklistsBtsDTO.setListDetail(mrChecklistsBtsDetailDTOS);
    List<MrChecklistsBtsDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrChecklistsBtsDTO);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(mrChecklistBtsDetailRepository.getListDetailByChecklistId(anyLong()))
        .thenReturn(mrChecklistsBtsDetailDTOS);
    PowerMockito.when(mrChecklistBtsRepository.updateMrChecklistBts(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrChecklistBtsDetailRepository.insertMrChecklistBtsDetail(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrChecklistBtsRepository.insertMrChecklistBts(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrChecklistBtsDetailRepository.updateMrChecklistBtsDetail(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrChecklistBtsDetailRepository.deleteMrChecklistBtsDetail(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = mrChecklistBtsBusiness.updateMrChecklistBts(mrChecklistsBtsDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void findMrChecklistBtsByIdFromWeb() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MrChecklistsBtsDTO mrChecklistsBtsDTO = Mockito.spy(MrChecklistsBtsDTO.class);
    mrChecklistsBtsDTO.setChecklistId(1L);
    mrChecklistsBtsDTO.setPage(1);
    mrChecklistsBtsDTO.setSortName("1");
    mrChecklistsBtsDTO.setSortType("1");
    MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO = Mockito.spy(MrChecklistsBtsDetailDTO.class);
    mrChecklistsBtsDetailDTO.setChecklistId(1L);
    List<MrChecklistsBtsDetailDTO> mrChecklistsBtsDetailDTOS = Mockito.spy(ArrayList.class);
    Datatable dataDetail = Mockito.spy(Datatable.class);
    dataDetail.setData(mrChecklistsBtsDetailDTOS);
    //set map(start)
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> itemDataCRInsideList = Mockito.spy(ArrayList.class);
    itemDataCRInsideList.add(itemDataCRInside);
    Datatable datatCycle = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    catItemDTOList.add(catItemDTO);
    datatCycle.setData(catItemDTOList);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatCycle);
    PowerMockito.when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(itemDataCRInsideList);
    //set map(end)
    PowerMockito.when(mrChecklistBtsDetailRepository.getListDataDetail(any()))
        .thenReturn(dataDetail);
    PowerMockito.when(mrChecklistBtsRepository.findMrChecklistBtsById(anyLong()))
        .thenReturn(mrChecklistsBtsDTO);
    mrChecklistBtsBusiness.findMrChecklistBtsByIdFromWeb(mrChecklistsBtsDTO);
  }

  @Test
  public void deleteMrChecklistBts_01() {
    MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO = Mockito.spy(MrChecklistsBtsDetailDTO.class);
    mrChecklistsBtsDetailDTO.setChecklistId(1L);
    mrChecklistsBtsDetailDTO.setChecklistDetailId(1L);
    List<MrChecklistsBtsDetailDTO> mrChecklistsBtsDetailDTOS = Mockito.spy(ArrayList.class);
    mrChecklistsBtsDetailDTOS.add(mrChecklistsBtsDetailDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(mrChecklistBtsDetailRepository.getListDetailByChecklistId(anyLong()))
        .thenReturn(mrChecklistsBtsDetailDTOS);
    PowerMockito.when(mrChecklistBtsDetailRepository.deleteMrChecklistBtsDetail(anyLong()))
        .thenReturn(resultInSideDto);
    mrChecklistBtsBusiness.deleteMrChecklistBts(1L);
  }

  @Test
  public void exportDataMrChecklistBts_01() throws Exception {
    MrChecklistsBtsDTO mrChecklistsBtsDTO = Mockito.spy(MrChecklistsBtsDTO.class);
    mrChecklistsBtsDTO.setMarketCode("1");
    mrChecklistsBtsDTO.setDeviceType("DH");
    mrChecklistsBtsDTO.setPhotoReq(1L);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MrChecklistsBtsDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrChecklistsBtsDTO);
    //set map(start)
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> itemDataCRInsideList = Mockito.spy(ArrayList.class);
    itemDataCRInsideList.add(itemDataCRInside);
    Datatable datatCycle = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    catItemDTOList.add(catItemDTO);
    datatCycle.setData(catItemDTOList);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatCycle);
    PowerMockito.when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(itemDataCRInsideList);
    //set map(end)
    PowerMockito.when(mrChecklistBtsRepository.getListMrChecklistBtsExport(any())).thenReturn(list);
    File file = mrChecklistBtsBusiness.exportDataMrChecklistBts(mrChecklistsBtsDTO);
    Assert.assertNull(file);

  }

  @Test
  public void getListSupplierBtsCombobox() {
  }

  @Test
  public void handleFileExport() {
  }

  @Test
  public void setDetailValue() {
  }

  @Test
  public void setMapMarket() {
  }

  @Test
  public void setMapDeviceType() {
  }

  @Test
  public void setMapMaterialType() {
  }

  @Test
  public void setMapCycle() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    datatable.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatable);
  }

  @Test
  public void test_getFileTemplate(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("DMM");
    itemDataCRInside.setValueStr(69L);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(catLocationRepository.getListLocationByLevelCBB(any(),anyLong(),anyLong())).thenReturn(lstCountry);
    PowerMockito.when(commonStreamServiceProxy.getListLocationByLevelCBBProxy(1L, null)).thenReturn(lstCountry);
    PowerMockito.when(I18n.getLanguage("mrMaterial.list.grid.sheetName")).thenReturn("Loại nhiên liệu_gas");
    MrDeviceBtsDTO dto = Mockito.spy(MrDeviceBtsDTO.class);
    dto.setFuelType("OIL");
    dto.setProducer("DMM");
    List<MrDeviceBtsDTO> lstFuelRefresher =Mockito.spy(ArrayList.class);
    lstFuelRefresher.add(dto);
    PowerMockito.when(mrDeviceBtsRepository.getListfuelTypeByDeviceType(anyString(),anyString())).thenReturn(lstFuelRefresher);
    PowerMockito.when(I18n.getLanguage("supplierCode.sheetName")).thenReturn("Hãng sản xuất");

    List<MrDeviceBtsDTO> lstSupplier =Mockito.spy(ArrayList.class);
    lstSupplier.add(dto);
    PowerMockito.when(mrDeviceBtsRepository.getListSupplierBtsByDeviceType(anyString(),anyString())).thenReturn(lstSupplier);
    File file = mrChecklistBtsBusiness.getFileTemplate();
    Assert.assertNotNull(file);
  }

  @Test
  public void test_importData_01()throws IOException {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.FILE_IS_NULL);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("DMM");
    itemDataCRInside.setValueStr(69L);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(catLocationRepository.getListLocationByLevelCBB(any(),anyLong(),anyLong())).thenReturn(lstCountry);
    PowerMockito.when(commonStreamServiceProxy.getListLocationByLevelCBBProxy(1L, null)).thenReturn(lstCountry);
    PowerMockito.when(I18n.getLanguage("mrChecklistBTS.fileEmpty")).thenReturn("file is null");
    MultipartFile multipartFile = null;
    ResultInSideDto result = mrChecklistBtsBusiness.importData(multipartFile);
  }

  @Test
  public void test_importData_02()throws IOException {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("DMM");
    itemDataCRInside.setValueStr(69L);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(catLocationRepository.getListLocationByLevelCBB(any(),anyLong(),anyLong())).thenReturn(lstCountry);
    PowerMockito.when(commonStreamServiceProxy.getListLocationByLevelCBBProxy(1L, null)).thenReturn(lstCountry);

    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile multipartFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    ResultInSideDto result = mrChecklistBtsBusiness.importData(multipartFile);
  }

  @Test
  public void test_importData_03()throws IOException {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.NODATA);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("DMM");
    itemDataCRInside.setValueStr(69L);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(catLocationRepository.getListLocationByLevelCBB(any(),anyLong(),anyLong())).thenReturn(lstCountry);
    PowerMockito.when(commonStreamServiceProxy.getListLocationByLevelCBBProxy(1L, null)).thenReturn(lstCountry);
    PowerMockito.when(I18n.getLanguage("common.searh.nodata")).thenReturn("Không có dữ liệu");
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile multipartFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    PowerMockito.mockStatic(CommonImport.class);
    List<Object[]> lstHeader = new ArrayList<>();
    lstHeader.add(new Object[100]);
    List<Object[]> lstData = new ArrayList<>();
    lstData.add(new Object[100]);
    Mockito.when(CommonImport.getDataFromExcelFileNew(new File(filePath), 0, 8,
        0, 11, 2)).thenReturn(lstHeader);
    Mockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8,
        0, 11, 2)).thenReturn(lstData);
    ResultInSideDto result = mrChecklistBtsBusiness.importData(multipartFile);
  }

  @Test
  public void test_importData_04()throws IOException {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("Việt Nam");
    itemDataCRInside.setValueStr(281L);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(catLocationRepository.getListLocationByLevelCBB(any(),anyLong(),anyLong())).thenReturn(lstCountry);
    PowerMockito.when(commonStreamServiceProxy.getListLocationByLevelCBBProxy(1L, null)).thenReturn(lstCountry);
    PowerMockito.when(I18n.getLanguage("mrChecklistBts.photoReq.yes")).thenReturn("Có");
    String filePath = "./temp.xlsx";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile multipartFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    PowerMockito.mockStatic(CommonImport.class);
    List<Object[]> lstData = new ArrayList<>();
    Object[] obj1 = new Object[]{"1","281","MPD","D","6","Hữu Toàn","abc","Có","1","1","abc", "1", "Có"};
    Object[] obj2 = new Object[]{"2","281","","D","6","Hữu Toàn","abc","Có","1","1","abc", "1", "Có"};
    Object[] obj3 = new Object[]{"3","281","MPD","","6","Hữu Toàn","abc","Có","1","1","abc", "1", "Có"};
    Object[] obj4 = new Object[]{"4","281","MPD","D","","Hữu Toàn","abc","Có","1","1","abc", "1", "Có"};
    Object[] obj5 = new Object[]{"5","281","MPD","D","6","","abc","Có","1","1","abc", "1", "Có"};
    Object[] obj6 = new Object[]{"6","281","MPD","D","6","Hữu Toàn","","Có","1","1","abc", "1", "Có"};
    Object[] obj7 = new Object[]{"7","281","MPD","D","6","Hữu Toàn","abc","","1","1","abc", "1", "Có"};
    Object[] obj8 = new Object[]{"8","281","MPD","D","6","Hữu Toàn","abc","Có","","1","abc", "1", "Có"};
    Object[] obj9 = new Object[]{"9","281","MPD","D","6","Hữu Toàn","abc","Có","1","","abc", "1", "Có"};
    Object[] obj10 = new Object[]{"10","281","MPD","D","6","Hữu Toàn","abc","Có","1","1","", "1", "Có"};
    lstData.add(obj1);
    lstData.add(obj2);
    lstData.add(obj3);
    lstData.add(obj4);
    lstData.add(obj5);
    lstData.add(obj6);
    lstData.add(obj7);
    lstData.add(obj8);
    lstData.add(obj9);
    lstData.add(obj10);
    PowerMockito.when(CommonImport.getDataFromExcelFileNew(new File(filePath), 0, 8,
        0, 12, 2)).thenReturn(lstData);
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    PowerMockito.when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);

    Datatable datatCycle = Mockito.spy(Datatable.class);
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("6");
    catItemDTO.setItemName("6");
    catItemDTOList.add(catItemDTO);
    datatCycle.setData(catItemDTOList);
    PowerMockito.when(catItemRepository.getItemMaster(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(datatCycle);

    MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO = Mockito.spy(MrChecklistsBtsDetailDTO.class);
    mrChecklistsBtsDetailDTO.setChecklistId(69L);
    mrChecklistsBtsDetailDTO.setContent("cbvnbjdhgy");
    List<MrChecklistsBtsDetailDTO> listContent = Mockito.spy(ArrayList.class);
    listContent.add(mrChecklistsBtsDetailDTO);
    PowerMockito.when(mrChecklistBtsDetailRepository.getListDetail(any())).thenReturn(listContent);
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    mrDeviceBtsDTO.setDeviceType("MPD");
    mrDeviceBtsDTO.setMarketCode("281");
    mrDeviceBtsDTO.setProducer("Hữu Toàn");
    mrDeviceBtsDTO.setFuelType("OIL");

    List<MrDeviceBtsDTO> listSupplier = Mockito.spy(ArrayList.class);
    listSupplier.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository
        .getListSupplierBtsByDeviceType(anyString(),anyString())).thenReturn(listSupplier);
    PowerMockito.when(I18n.getLanguage("mrChecklistBts.deviceType.generator")).thenReturn("Máy phát điện");
    PowerMockito.when(I18n.getLanguage("mrChecklistBts.materialType.oil")).thenReturn("Dầu");
    PowerMockito.when(I18n.getLanguage("common.success1")).thenReturn("Thành công");
    PowerMockito.when(I18n.getLanguage("mrChecklistBts.err.materialTypeName")).thenReturn("Loại nhiên liệu không được để trống");
    PowerMockito.when(I18n.getLanguage("mrChecklistBts.err.deviceTypeName")).thenReturn("Loại thiết bị không được để trống");
    PowerMockito.when(I18n.getLanguage("mrChecklistBts.err.cycleStr")).thenReturn("Chu kỳ bảo dưỡng (tháng) không được để trống");
    PowerMockito.when(I18n.getLanguage("mrChecklistBts.err.content")).thenReturn("Đầu việc thực hiện không được để trống");
    PowerMockito.when(I18n.getLanguage("mrChecklistBts.err.minPhotoStr")).thenReturn("Số lượng ảnh tối thiểu không được để trống khi chọn Có Bắt buộc ảnh");
    PowerMockito.when(I18n.getLanguage("mrChecklistBts.err.maxPhotoStr")).thenReturn("Số lượng ảnh tối đa không được để trống khi chọn Có Bắt buộc ảnh");
//    PowerMockito.when(I18n.getLanguage("mrChecklistBts.err.dup-code-in-file")).thenReturn("0");
    MrChecklistsBtsDTO dtoTmp = null;
    PowerMockito.when(mrChecklistBtsRepository.checkMrChecklistBtsExit(any())).thenReturn(dtoTmp);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(mrChecklistBtsRepository.insertMrChecklistBts(any())).thenReturn(resultInSideDto);
    PowerMockito.when(mrChecklistBtsDetailRepository.insertMrChecklistBtsDetail(any())).thenReturn(resultInSideDto);

    PowerMockito.when(I18n.getLanguage("mrMaterial.list.grid.sheetName")).thenReturn("Loại nhiên liệu_gas");
    List<MrDeviceBtsDTO> lstFuelRefresher =Mockito.spy(ArrayList.class);
    lstFuelRefresher.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListfuelTypeByDeviceType(anyString(),anyString())).thenReturn(lstFuelRefresher);
    PowerMockito.when(I18n.getLanguage("supplierCode.sheetName")).thenReturn("Hãng sản xuất");

    List<MrDeviceBtsDTO> lstSupplier =Mockito.spy(ArrayList.class);
    lstSupplier.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListSupplierBtsByDeviceType(anyString(),anyString())).thenReturn(lstSupplier);
    ResultInSideDto result = mrChecklistBtsBusiness.importData(multipartFile);
    Assert.assertEquals(result.getKey(), RESULT.SUCCESS);
  }

}
