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
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.UserRepositoryImpl;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHardDevicesCheckListDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrHardDevicesCheckListRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
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
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrHardDevicesCheckListBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class, XSSFWorkbook.class,
    ExcelWriterUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MrHardDevicesCheckListBusinessImplTest {

  @InjectMocks
  MrHardDevicesCheckListBusinessImpl mrHardDevicesCheckListBusiness;

  @Mock
  MrHardDevicesCheckListRepository mrHardDevicesCheckListRepository;

  @Mock
  protected CatLocationBusiness catLocationBusiness;

  @Mock
  MrDeviceBusiness mrDeviceBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Mock
  UserRepositoryImpl userRepositoryImpl;

  @Mock
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Test
  public void testOnSearch() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrHardDevicesCheckListDTO mrCheckListDTO = Mockito.spy(MrHardDevicesCheckListDTO.class);
    mrCheckListDTO.setMarketCode("1");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountryName);

    List<MrHardDevicesCheckListDTO> listDTO = Mockito.spy(ArrayList.class);
    listDTO.add(mrCheckListDTO);
    Datatable expected = Mockito.spy(Datatable.class);
    expected.setData(listDTO);

    PowerMockito.when(mrHardDevicesCheckListRepository.onSearch(any())).thenReturn(expected);

    Datatable actual = mrHardDevicesCheckListBusiness.onSearch(mrCheckListDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testInsert_01() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);

    MrHardDevicesCheckListDTO mrCheckListDTO = Mockito.spy(MrHardDevicesCheckListDTO.class);

    List<MrHardDevicesCheckListDTO> listDTO = Mockito.spy(ArrayList.class);
    listDTO.add(mrCheckListDTO);
    PowerMockito.when(
        mrHardDevicesCheckListRepository
            .checkListDTOExisted(any())
    ).thenReturn(listDTO);

    ResultInSideDto actual = mrHardDevicesCheckListBusiness.insert(mrCheckListDTO);

    Assert.assertEquals(RESULT.DUPLICATE, actual.getKey());
  }

  @Test
  public void testInsert_02() {
    PowerMockito.mockStatic(TicketProvider.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("1111");
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);

    MrHardDevicesCheckListDTO mrCheckListDTO = Mockito.spy(MrHardDevicesCheckListDTO.class);

    List<MrHardDevicesCheckListDTO> listDTO = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrHardDevicesCheckListRepository
            .checkListDTOExisted(any())
    ).thenReturn(listDTO);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(mrHardDevicesCheckListRepository.insertOrUpdate(any())).thenReturn(expected);

    ResultInSideDto actual = mrHardDevicesCheckListBusiness.insert(mrCheckListDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testUpdate_01() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);

    MrHardDevicesCheckListDTO mrCheckListDTO = Mockito.spy(MrHardDevicesCheckListDTO.class);

    List<MrHardDevicesCheckListDTO> listDTO = Mockito.spy(ArrayList.class);
    listDTO.add(mrCheckListDTO);
    PowerMockito.when(
        mrHardDevicesCheckListRepository
            .checkListDTOExisted(any())
    ).thenReturn(listDTO);

//    ResultInSideDto actual = mrHardDevicesCheckListBusiness.update(mrCheckListDTO);

//    Assert.assertEquals(RESULT.DUPLICATE, actual.getKey());
  }

  @Test
  public void testUpdate_02() {
    PowerMockito.mockStatic(TicketProvider.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("1111");
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);

    MrHardDevicesCheckListDTO mrCheckListDTO = Mockito.spy(MrHardDevicesCheckListDTO.class);

    List<MrHardDevicesCheckListDTO> listDTO = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrHardDevicesCheckListRepository
            .checkListDTOExisted(any())
    ).thenReturn(listDTO);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(mrHardDevicesCheckListRepository.insertOrUpdate(any())).thenReturn(expected);

    ResultInSideDto actual = mrHardDevicesCheckListBusiness.update(mrCheckListDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDeleteMrCheckList() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(mrHardDevicesCheckListRepository.deleteMrCheckList(anyLong()))
        .thenReturn(expected);

    ResultInSideDto actual = mrHardDevicesCheckListBusiness.deleteMrCheckList(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrHardDevicesCheckListDTO expected = Mockito.spy(MrHardDevicesCheckListDTO.class);
    PowerMockito.when(mrHardDevicesCheckListRepository.getDetail(anyLong())).thenReturn(expected);

    MrHardDevicesCheckListDTO actual = mrHardDevicesCheckListBusiness.getDetail(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetListArrayDeviceTypeNetworkType() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<MrHardDevicesCheckListDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrHardDevicesCheckListRepository
            .getListArrayDeviceTypeNetworkType()
    ).thenReturn(expected);

    List<MrHardDevicesCheckListDTO> actual = mrHardDevicesCheckListBusiness
        .getListArrayDeviceTypeNetworkType();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testExportSearchData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountryName);

    MrHardDevicesCheckListDTO mrCheckListDTO = Mockito.spy(MrHardDevicesCheckListDTO.class);
    mrCheckListDTO.setMarketCode("1");
    List<MrHardDevicesCheckListDTO> mrCheckListDTOList = Mockito.spy(ArrayList.class);
    mrCheckListDTOList.add(mrCheckListDTO);
    PowerMockito.when(
        mrHardDevicesCheckListRepository
            .onSearchExport(any())
    ).thenReturn(mrCheckListDTOList);

    File actual = mrHardDevicesCheckListBusiness.exportSearchData(mrCheckListDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void testGetFileTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");
    PowerMockito.when(I18n.getLanguage("mrCheckList.export.nameSheetOne")).thenReturn("0");
    PowerMockito.when(I18n.getLanguage("mrCheckList.export.nameSheetParam1")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrCheckList.export.nameSheetParam2")).thenReturn("2");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstMarketCode);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemCode("1111");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    PowerMockito.when(
        mrCfgProcedureCDBusiness.getMrSubCategory()
    ).thenReturn(lstArrayCode);

//    MrHardDevicesCheckListDTO mrCheckListDTO = Mockito.spy(MrHardDevicesCheckListDTO.class);
////    mrCheckListDTO.setArrayCode("1");
////    mrCheckListDTO.setNetworkType("1");
////    mrCheckListDTO.setDeviceType("1");
////    List<MrHardDevicesCheckListDTO> lstDTO = Mockito.spy(ArrayList.class);
////    lstDTO.add(mrCheckListDTO);
////    PowerMockito.when(
////        mrHardDevicesCheckListRepository
////            .getListArrayDeviceTypeNetworkType()
////    ).thenReturn(lstDTO);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    List<MrSynItDevicesDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(anyString())
    ).thenReturn(list);

    File actual = mrHardDevicesCheckListBusiness.getFileTemplate();

    Assert.assertNotNull(actual);
  }

  @Test
  public void testImportMrCheckList_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountry);

    ResultInSideDto actual = mrHardDevicesCheckListBusiness
        .importMrCheckList(null);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportMrCheckList_02() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );

    ResultInSideDto actual = mrHardDevicesCheckListBusiness
        .importMrCheckList(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportMrCheckList_03() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountry);

    ResultInSideDto actual = mrHardDevicesCheckListBusiness
        .importMrCheckList(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORM, actual.getKey());
  }

  @Test
  public void testImportMrCheckList_04() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(11L);
    itemDataCRInside.setDisplayStr("11");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountry);

    Object[] objects = new Object[]{
        "1", "1*", "1*", "1", "1*",
        "1", "1*", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 7, 2)
    ).thenReturn(headerList);

    ResultInSideDto actual = mrHardDevicesCheckListBusiness
        .importMrCheckList(multipartFile);

//    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void testImportMrCheckList_05() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(11L);
    itemDataCRInside.setDisplayStr("11");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountry);

    Object[] objects = new Object[]{
        "1", "1*", "1*", "1", "1*",
        "1", "1*", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 7, 2)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{
        "1", "1", "1", "1", "1",
        "1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 1510; i++) {
      dataImportList.add(objects1);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 8, 0, 7, 2)
    ).thenReturn(dataImportList);

    ResultInSideDto actual = mrHardDevicesCheckListBusiness
        .importMrCheckList(multipartFile);

//    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportMrCheckList_06() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountry);

    Object[] objects = new Object[]{
        "1", "1*", "1*", "1", "1*",
        "1", "1*", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 7, 2)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{
        "1", "1", "1", "1", "1",
        "1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 8, 0, 7, 2)
    ).thenReturn(dataImportList);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1111");
    catItemDTO.setItemName("ItemName");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    PowerMockito.when(
        mrCfgProcedureCDBusiness.getMrSubCategory()
    ).thenReturn(listData);

    MrHardDevicesCheckListDTO mrHardDevicesCheckListDTO = Mockito
        .spy(MrHardDevicesCheckListDTO.class);
    mrHardDevicesCheckListDTO.setNetworkType("2222");
    mrHardDevicesCheckListDTO.setArrayCode("2222");
    mrHardDevicesCheckListDTO.setDeviceType("2222");
    List<MrHardDevicesCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrHardDevicesCheckListDTO);
    PowerMockito.when(
        mrHardDevicesCheckListRepository
            .getListArrayDeviceTypeNetworkType()
    ).thenReturn(checkListDTOList);

    PowerMockito.when(
        CommonExport
            .exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = mrHardDevicesCheckListBusiness
        .importMrCheckList(multipartFile);

//    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testImportMrCheckList_07() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountry);

    Object[] objects = new Object[]{
        "1", "1*", "1*", "1", "1*",
        "1", "1*", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 7, 2)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{
        "1", "1", "1", "1", "1",
        "1", null, "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 8, 0, 7, 2)
    ).thenReturn(dataImportList);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("ItemName");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    PowerMockito.when(
        mrCfgProcedureCDBusiness.getMrSubCategory()
    ).thenReturn(listData);

    MrHardDevicesCheckListDTO mrHardDevicesCheckListDTO = Mockito
        .spy(MrHardDevicesCheckListDTO.class);
    mrHardDevicesCheckListDTO.setNetworkType("1");
    mrHardDevicesCheckListDTO.setArrayCode("1");
    mrHardDevicesCheckListDTO.setDeviceType("1");
    List<MrHardDevicesCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrHardDevicesCheckListDTO);
    PowerMockito.when(
        mrHardDevicesCheckListRepository
            .getListArrayDeviceTypeNetworkType()
    ).thenReturn(checkListDTOList);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");
    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    PowerMockito.when(
        mrHardDevicesCheckListRepository.getListNetworkType()
    ).thenReturn(lstNetwork);
    PowerMockito.when(
        mrHardDevicesCheckListRepository.getListDeviceType()
    ).thenReturn(lstNetwork);

    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setIsEnable(1L);
    PowerMockito.when(
        userRepositoryImpl.getUserByUserName(anyString())
    ).thenReturn(usersEntity);

    PowerMockito.when(
        CommonExport
            .exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = mrHardDevicesCheckListBusiness
        .importMrCheckList(multipartFile);

//    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testImportMrCheckList_08() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountry);

    Object[] objects = new Object[]{
        "1", "1*", "1*", "1", "1*",
        "1", "1*", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 7, 2)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{
        "1", "1", "1", "1", "1",
        "1", null, "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 8, 0, 7, 2)
    ).thenReturn(dataImportList);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("ItemName");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    PowerMockito.when(
        mrCfgProcedureCDBusiness.getMrSubCategory()
    ).thenReturn(listData);

    MrHardDevicesCheckListDTO mrHardDevicesCheckListDTO = Mockito
        .spy(MrHardDevicesCheckListDTO.class);
    mrHardDevicesCheckListDTO.setNetworkType("1");
    mrHardDevicesCheckListDTO.setArrayCode("1");
    mrHardDevicesCheckListDTO.setDeviceType("1");
    mrHardDevicesCheckListDTO.setMrHardDevicesCheckListId(1L);
    mrHardDevicesCheckListDTO.setCreatedTime("19/05/2020 08:00:00");
    mrHardDevicesCheckListDTO.setCreatedUser("CreatedUser");
    List<MrHardDevicesCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrHardDevicesCheckListDTO);
    PowerMockito.when(
        mrHardDevicesCheckListRepository
            .getListArrayDeviceTypeNetworkType()
    ).thenReturn(checkListDTOList);
    PowerMockito.when(
        mrHardDevicesCheckListRepository
            .checkListDTOExisted(any())
    ).thenReturn(checkListDTOList);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");
    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    PowerMockito.when(
        mrHardDevicesCheckListRepository.getListNetworkType()
    ).thenReturn(lstNetwork);
    PowerMockito.when(
        mrHardDevicesCheckListRepository.getListDeviceType()
    ).thenReturn(lstNetwork);

    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setIsEnable(1L);
    PowerMockito.when(
        userRepositoryImpl.getUserByUserName(anyString())
    ).thenReturn(usersEntity);

    PowerMockito.when(
        CommonExport
            .exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = mrHardDevicesCheckListBusiness
        .importMrCheckList(multipartFile);

//    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }
}
