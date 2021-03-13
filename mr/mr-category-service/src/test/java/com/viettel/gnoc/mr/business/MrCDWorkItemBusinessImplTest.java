package com.viettel.gnoc.mr.business;

import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.mr.repository.MrCDWorkItemRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
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
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrCfgProcedureBtsBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, TicketProvider.class, DataUtil.class, CommonExport.class, DateTimeUtils.class,
    PassTranformer.class, Base64.class, ExcelWriterUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MrCDWorkItemBusinessImplTest {

  @InjectMocks
  MrCDWorkItemBusinessImpl mrCDWorkItemBusiness;

  @Mock
  MrCDWorkItemRepository mrCDWorkItemRepository;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  MrDeviceBusiness mrDeviceBusiness;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  CrServiceProxy crServiceProxy;

  Map<String, CatItemDTO> mapArray = new HashMap<>();

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrCDWorkItemBusiness, "tempFolder",
        "/tempFolder");
  }

  @Test
  public void getListMrCDWorkItemPage() {
  }

  @Test
  public void insert() {
  }

  @Test
  public void update() {
  }

  @Test
  public void delete() {
  }

  @Test
  public void getDetail() {
  }

  @Test
  public void getComboboxArray() {
  }

  @Test
  public void getDeviceTypeCbb() {
  }

  @Test
  public void getComboboxActivities() {
  }

  @Test
  public void testImportMrCDWorkItem_01() throws Exception {
    Datatable dataArray = Mockito.spy(Datatable.class);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("cvcv");
    list.add(catItemDTO);
    dataArray.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataArray);
//    MockMultipartFile multipartFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    MockMultipartFile multipartFile = null;
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FILE_IS_NULL);

    ResultInSideDto result = mrCDWorkItemBusiness.importMrCDWorkItem(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testImportMrCDWorkItem_02() throws Exception {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
    // setMapArray
    Datatable dataArray = Mockito.spy(Datatable.class);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("cvcv");
    list.add(catItemDTO);
    dataArray.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataArray);
    //====setMapArray====
    PowerMockito.mockStatic(CommonImport.class);

    MockMultipartFile multipartFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    PowerMockito.mockStatic(FileUtils.class);
    String filePath = "/filePath";
    PowerMockito.when(FileUtils
        .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), "/tempFolder"))
        .thenReturn(filePath);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);

    ResultInSideDto result = mrCDWorkItemBusiness.importMrCDWorkItem(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testImportMrCDWorkItem_03() throws Exception {
    // setMapArray
    Datatable dataArray = Mockito.spy(Datatable.class);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("cvcv");
    list.add(catItemDTO);
    dataArray.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataArray);
    //====setMapArray====
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    MockMultipartFile multipartFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    PowerMockito.mockStatic(FileUtils.class);
    String filePath = "/filePath";
    PowerMockito.when(FileUtils
        .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), "/tempFolder"))
        .thenReturn(filePath);
    Object[] objectsHeader = new Object[]{"xxx", "xxx*", "xxx*", "xxx", "xxx*", "xxx", "xxx*",
        "xxx"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objectsHeader);
    File fileImport = new File(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        7,
        0,
        5,
        2
    )).thenReturn(headerList);

    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        8,
        0,
        1,
        1000
    )).thenReturn(lstDataAll);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.NODATA);

    ResultInSideDto result = mrCDWorkItemBusiness.importMrCDWorkItem(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testImportMrCDWorkItem_04() throws Exception {
    // setMapArray
    Datatable dataArray = Mockito.spy(Datatable.class);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("cvcv");
    list.add(catItemDTO);
    dataArray.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataArray);
    //====setMapArray====
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    MockMultipartFile multipartFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    PowerMockito.mockStatic(FileUtils.class);
    String filePath = "/filePath";
    PowerMockito.when(FileUtils
        .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), "/tempFolder"))
        .thenReturn(filePath);
    Object[] objectsHeader = new Object[]{"xxx", "xxx*", "xxx*", "xxx", "xxx*", "xxx", "xxx*",
        "xxx"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objectsHeader);
    File fileImport = new File(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        7,
        0,
        5,
        2
    )).thenReturn(headerList);
    Object[] objData = new Object[]{"xxx", "xxx*", "xxx*", "xxx", "xxx*", "xxx", "xxx*", "xxx"};
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1505; i++) {
      lstDataAll.add(objData);
    }
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        8,
        0,
        1,
        1000
    )).thenReturn(lstDataAll);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.DATA_OVER);

    ResultInSideDto result = mrCDWorkItemBusiness.importMrCDWorkItem(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testImportMrCDWorkItem_05() throws Exception {

    // setMapArray
    Datatable dataArray = Mockito.spy(Datatable.class);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("cvcv");
    catItemDTO.setItemCode("xxx");
    catItemDTO.setItemName("xxx");
    catItemDTO.setItemValue("xxx");
    list.add(catItemDTO);
    dataArray.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataArray);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //====setMapArray====
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    MockMultipartFile multipartFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    PowerMockito.mockStatic(FileUtils.class);
    String filePath = "/filePath";
    PowerMockito.when(FileUtils
        .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), "/tempFolder"))
        .thenReturn(filePath);
    Object[] objectsHeader = new Object[]{"xxx", "xxx*", "xxx*", "xxx", "xxx*", "xxx", "xxx*",
        "xxx"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objectsHeader);
    File fileImport = new File(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        7,
        0,
        5,
        2
    )).thenReturn(headerList);
    Object[] objData1 = new Object[]{"xxx", "", "xxx*", "xxx", "1", "xxx", "xxx*", "xxx"};
    Object[] objData2 = new Object[]{"xxx", "281", "", "xxx", "1", "xxx", "xxx*", "xxx"};
    Object[] objData3 = new Object[]{"xxx", "281", "xxx", "", "1", "xxx", "xxx*", "xxx"};
    Object[] objData4 = new Object[]{"xxx", "281", "xxx", "xxx", "1", "xxx", "xxx*", "xxx"};
    Object[] objData5 = new Object[]{"xxx", "281", "xxx", "xxx", "1", "", "xxx*", "xxx"};
    Object[] objData6 = new Object[]{"xxx", "281", "xxx", "xxx", "1", "xxx", "", "xxx"};
    Object[] objData7 = new Object[]{"xxx", "281", "xxx", "xxx", "1", "xxx", "xxx*", ""};
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);

    lstDataAll.add(objData1);
    lstDataAll.add(objData2);
    lstDataAll.add(objData3);
    lstDataAll.add(objData4);
    lstDataAll.add(objData5);
    lstDataAll.add(objData6);
    lstDataAll.add(objData7);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        8,
        0,
        1,
        1000
    )).thenReturn(lstDataAll);

    List<ItemDataCRInside> listLocation = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(281L);
    listLocation.add(itemDataCRInside);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(listLocation);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);

    ResultInSideDto result = mrCDWorkItemBusiness.importMrCDWorkItem(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void setMapArray() {
  }

  @Test
  public void exportData() {
  }

  @Test
  public void testGetTemplate_01() throws Exception {
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    List<ItemDataCRInside> countryNameList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("hothot");
    countryNameList.add(itemDataCRInside);
    PowerMockito.when(catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null)).thenReturn(countryNameList);

    PowerMockito.when(I18n.getLanguage("MrCDWorkItem.workItems")).thenReturn("Đầu việc");
    PowerMockito.when(I18n.getLanguage("MrCDWorkItem.ListMarketName")).thenReturn("List Quốc gia");
    PowerMockito.when(I18n.getLanguage("MrCDWorkItem.ListDeviceType"))
        .thenReturn("List Loại thiết bị");
    File fileExport = new File("TEMPLATE_EXPORT_VN.xlsx");
    mrCDWorkItemBusiness.getTemplate();
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void getListMrCDWorkItemDTO() {
  }
}
