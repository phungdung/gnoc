package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCheckListDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.mr.repository.MrCheckListRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
@PrepareForTest({MrScheduleTelBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrCheckListBusinessImplTest {

  @InjectMocks
  MrCheckListBusinessImpl mrCheckListBusiness;

  @Mock
  MrCheckListRepository mrCheckListRepository;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Mock
  UserRepositoryImpl userRepositoryImpl;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrCheckListBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void onSearch() {
    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setMarketCode("1");
    List<MrCheckListDTO> listDTO = Mockito.spy(ArrayList.class);
    listDTO.add(mrCheckListDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listDTO);
    when(mrCheckListRepository.onSearch(any())).thenReturn(datatable);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);

    mrCheckListBusiness.onSearch(mrCheckListDTO);
  }

  @Test
  public void insert() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    List<MrCheckListDTO> listDTOS = Mockito.spy(ArrayList.class);
    listDTOS.add(mrCheckListDTO);
    when(mrCheckListRepository.checkListDTOExisted(any())).thenReturn(listDTOS);
    mrCheckListBusiness.insert(mrCheckListDTO);
  }

  @Test
  public void insert1() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    List<MrCheckListDTO> listDTOS = Mockito.spy(ArrayList.class);
    when(mrCheckListRepository.checkListDTOExisted(any())).thenReturn(listDTOS);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrCheckListRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);
    mrCheckListBusiness.insert(mrCheckListDTO);
  }

  @Test
  public void update() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setCheckListId(1L);

    List<MrCheckListDTO> listDTOS = Mockito.spy(ArrayList.class);
    listDTOS.add(mrCheckListDTO);
    when(mrCheckListRepository.checkListDTOExisted(any())).thenReturn(listDTOS);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrCheckListRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);

    mrCheckListBusiness.update(mrCheckListDTO);
  }

  @Test
  public void update1() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setCheckListId(1L);

    MrCheckListDTO mrCheckListDTO1 = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO1.setCheckListId(2L);
    List<MrCheckListDTO> listDTOS = Mockito.spy(ArrayList.class);
    listDTOS.add(mrCheckListDTO1);
    when(mrCheckListRepository.checkListDTOExisted(any())).thenReturn(listDTOS);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrCheckListRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);

    mrCheckListBusiness.update(mrCheckListDTO);

  }

  @Test
  public void update2() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setCheckListId(1L);

    MrCheckListDTO mrCheckListDTO1 = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO1.setCheckListId(2L);
    List<MrCheckListDTO> listDTOS = Mockito.spy(ArrayList.class);
    when(mrCheckListRepository.checkListDTOExisted(any())).thenReturn(listDTOS);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrCheckListRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);

    mrCheckListBusiness.update(mrCheckListDTO);

  }

  @Test
  public void deleteMrCheckList() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrCheckListRepository.deleteMrCheckList(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrCheckListBusiness.deleteMrCheckList(1L);
    assertEquals(result, resultInSideDto);
  }

  @Test
  public void getDetail() {
    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    when(mrCheckListRepository.getDetail(any())).thenReturn(mrCheckListDTO);
    MrCheckListDTO result = mrCheckListBusiness.getDetail(1L);
    assertEquals(mrCheckListDTO, result);
  }

  @Test
  public void getListArrayDeviceTypeNetworkType() {
    List<MrCheckListDTO> lst = Mockito.spy(ArrayList.class);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(lst);
    List<MrCheckListDTO> result = mrCheckListBusiness.getListArrayDeviceTypeNetworkType();
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void exportSearchData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setMarketCode("1");
    List<MrCheckListDTO> mrCheckListDTOList = Mockito.spy(ArrayList.class);
    mrCheckListDTOList.add(mrCheckListDTO);
    when(mrCheckListRepository.onSearchExport(any())).thenReturn(mrCheckListDTOList);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrCheckListBusiness.exportSearchData(mrCheckListDTO);
  }

  @Test
  public void getFileTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.stt"))
        .thenReturn("1a");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.marketCode"))
        .thenReturn("2a");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.arrayCode"))
        .thenReturn("3a");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.networkType"))
        .thenReturn("4a");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.deviceType"))
        .thenReturn("5a");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.cycle"))
        .thenReturn("6a");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.deviceTypeAll"))
        .thenReturn("7a");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.content"))
        .thenReturn("8a");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.target"))
        .thenReturn("9a");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.createdUser"))
        .thenReturn("10");

    PowerMockito.when(I18n.getLanguage("mrCheckList.export.nameSheetOne"))
        .thenReturn("11");
    PowerMockito.when(I18n.getLanguage("mrCheckList.export.nameSheetParam1"))
        .thenReturn("12");
    PowerMockito.when(I18n.getLanguage("mrCheckList.export.nameSheetParam2"))
        .thenReturn("13");

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    when(catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);

    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(lstArrayCode);

    List<MrCheckListDTO> lstDTO = Mockito.spy(ArrayList.class);
    when(mrCheckListRepository
        .getListArrayDeviceTypeNetworkType()).thenReturn(lstDTO);

    mrCheckListBusiness.getFileTemplate();
  }

  @Test
  public void importMrCheckList() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = null;
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);
    mrCheckListBusiness.importMrCheckList(multipartFile);
  }

  @Test
  public void importMrCheckList1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);

    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSXs");
    mrCheckListBusiness.importMrCheckList(multipartFile);
  }

  @Test
  public void importMrCheckList2() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);

    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");

    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        7,
        0,
        9,
        2
    )).thenReturn(headerList);

    mrCheckListBusiness.importMrCheckList(multipartFile);
  }

  @Test
  public void importMrCheckList3() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");
    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.stt"))
        .thenReturn("mrCheckList.list.stt");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.marketCode"))
        .thenReturn("mrCheckList.list.marketCode");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.arrayCode"))
        .thenReturn("mrCheckList.list.arrayCode");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.networkType"))
        .thenReturn("mrCheckList.list.networkType");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.deviceType"))
        .thenReturn("mrCheckList.list.deviceType");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.cycle"))
        .thenReturn("mrCheckList.list.cycle");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.deviceTypeAll"))
        .thenReturn("mrCheckList.list.deviceTypeAll");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.content"))
        .thenReturn("mrCheckList.list.content");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.target"))
        .thenReturn("mrCheckList.list.target");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.createdUser"))
        .thenReturn("mrCheckList.list.createdUser");
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("mrCheckList.list.stt"),
        I18n.getLanguage("mrCheckList.list.marketCode") + "*",
        I18n.getLanguage("mrCheckList.list.arrayCode") + "*",
        I18n.getLanguage("mrCheckList.list.networkType"),
        I18n.getLanguage("mrCheckList.list.deviceType"),
        I18n.getLanguage("mrCheckList.list.cycle") + "*",
        I18n.getLanguage("mrCheckList.list.deviceTypeAll"),
        I18n.getLanguage("mrCheckList.list.content") + "*",
        I18n.getLanguage("mrCheckList.list.target"),
        I18n.getLanguage("mrCheckList.list.createdUser")
    };
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            9,
            2
        ))
        .thenReturn(dataHeader);
    //      </editor-fold>

    Object[] objecttest = new Object[]{};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1502; i++) {
      dataImportList.add(objecttest);
    }
    PowerMockito
        .when(CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            8,
            0,
            9,
            2
        )).thenReturn(dataImportList);

    mrCheckListBusiness.importMrCheckList(multipartFile);
  }

  @Test
  public void importMrCheckList4() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");
    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.stt"))
        .thenReturn("mrCheckList.list.stt");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.marketCode"))
        .thenReturn("mrCheckList.list.marketCode");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.arrayCode"))
        .thenReturn("mrCheckList.list.arrayCode");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.networkType"))
        .thenReturn("mrCheckList.list.networkType");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.deviceType"))
        .thenReturn("mrCheckList.list.deviceType");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.cycle"))
        .thenReturn("mrCheckList.list.cycle");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.deviceTypeAll"))
        .thenReturn("mrCheckList.list.deviceTypeAll");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.content"))
        .thenReturn("mrCheckList.list.content");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.target"))
        .thenReturn("mrCheckList.list.target");
    PowerMockito.when(I18n.getLanguage("mrCheckList.list.createdUser"))
        .thenReturn("mrCheckList.list.createdUser");
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("mrCheckList.list.stt"),
        I18n.getLanguage("mrCheckList.list.marketCode") + "*",
        I18n.getLanguage("mrCheckList.list.arrayCode") + "*",
        I18n.getLanguage("mrCheckList.list.networkType"),
        I18n.getLanguage("mrCheckList.list.deviceType"),
        I18n.getLanguage("mrCheckList.list.cycle") + "*",
        I18n.getLanguage("mrCheckList.list.deviceTypeAll"),
        I18n.getLanguage("mrCheckList.list.content") + "*",
        I18n.getLanguage("mrCheckList.list.target"),
        I18n.getLanguage("mrCheckList.list.createdUser")
    };
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            9,
            2
        ))
        .thenReturn(dataHeader);
    //      </editor-fold>

    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2; i++) {
      Object[] objecttest = new Object[10];
      for (int j = 0; j < objecttest.length; j++) {
        objecttest[j] = "1";
      }
      dataImportList.add(objecttest);
    }
    PowerMockito
        .when(CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            8,
            0,
            9,
            2
        )).thenReturn(dataImportList);

    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);

    List<MrCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(checkListDTOList);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrCheckListBusiness.importMrCheckList(multipartFile);
  }


  @Test
  public void setMapCountryName() {
  }

  @Test
  public void setMapArray() {
  }

  @Test
  public void setMapArrayByNetworkType() {
  }

  @Test
  public void setMapNetworkTypeByDeviceType() {
  }

  @Test
  public void validateImportInfo() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);
    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo1() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);
    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo2() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo3() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo4() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo5() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo6() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
    dto.setDeviceType("1");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("2");

    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    List<MrDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    lstDevice.add(mrDeviceDTO);
    when(mrCheckListRepository.getListDeviceType()).thenReturn(lstDevice);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setNetworkType("1");
    mrCheckListDTO.setArrayCode("1");
    List<MrCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrCheckListDTO);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(checkListDTOList);
    mrCheckListBusiness.setMapArrayByNetworkType();

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo7() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
    dto.setDeviceType("1");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");

    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    List<MrDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    lstDevice.add(mrDeviceDTO);
    when(mrCheckListRepository.getListDeviceType()).thenReturn(lstDevice);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setNetworkType("1");
    mrCheckListDTO.setArrayCode("1");
    List<MrCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrCheckListDTO);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(checkListDTOList);
    mrCheckListBusiness.setMapArrayByNetworkType();
    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo8() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
    dto.setDeviceType("1");
    dto.setCycle("t");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");

    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    List<MrDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    lstDevice.add(mrDeviceDTO);
    when(mrCheckListRepository.getListDeviceType()).thenReturn(lstDevice);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setDeviceType("1");
    mrCheckListDTO.setNetworkType("1");
    mrCheckListDTO.setArrayCode("1");
    List<MrCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrCheckListDTO);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(checkListDTOList);
    mrCheckListBusiness.setMapArrayByNetworkType();
    mrCheckListBusiness.setMapNetworkTypeByDeviceType();

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo9() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
    dto.setDeviceType("1");
    dto.setCycle("2");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");

    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    List<MrDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    lstDevice.add(mrDeviceDTO);
    when(mrCheckListRepository.getListDeviceType()).thenReturn(lstDevice);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setDeviceType("1");
    mrCheckListDTO.setNetworkType("1");
    mrCheckListDTO.setArrayCode("1");
    List<MrCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrCheckListDTO);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(checkListDTOList);
    mrCheckListBusiness.setMapArrayByNetworkType();
    mrCheckListBusiness.setMapNetworkTypeByDeviceType();

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo10() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
//    dto.setDeviceType("1");
    dto.setCycle("3");
    String a = "";
    for (int i = 0; i < 201; i++) {
      a += "t";
    }
    dto.setDeviceTypeAll(a);
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");

    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    List<MrDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    lstDevice.add(mrDeviceDTO);
    when(mrCheckListRepository.getListDeviceType()).thenReturn(lstDevice);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setDeviceType("1");
    mrCheckListDTO.setNetworkType("1");
    mrCheckListDTO.setArrayCode("1");
    List<MrCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrCheckListDTO);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(checkListDTOList);
    mrCheckListBusiness.setMapArrayByNetworkType();
    mrCheckListBusiness.setMapNetworkTypeByDeviceType();

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo11() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
//    dto.setDeviceType("1");
    dto.setCycle("3");
    String a = "";
    for (int i = 0; i < 2; i++) {
      a += "t";
    }
    dto.setDeviceTypeAll(a);
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");

    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    List<MrDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    lstDevice.add(mrDeviceDTO);
    when(mrCheckListRepository.getListDeviceType()).thenReturn(lstDevice);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setDeviceType("1");
    mrCheckListDTO.setNetworkType("1");
    mrCheckListDTO.setArrayCode("1");
    List<MrCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrCheckListDTO);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(checkListDTOList);
    mrCheckListBusiness.setMapArrayByNetworkType();
    mrCheckListBusiness.setMapNetworkTypeByDeviceType();

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo12() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
//    dto.setDeviceType("1");
    String b = "";
    for (int j = 0; j < 1001; j++) {
      b += "t";
    }
    dto.setContent(b);
    dto.setCycle("3");
    String a = "";
    for (int i = 0; i < 2; i++) {
      a += "t";
    }
    dto.setDeviceTypeAll(a);
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");

    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    List<MrDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    lstDevice.add(mrDeviceDTO);
    when(mrCheckListRepository.getListDeviceType()).thenReturn(lstDevice);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setDeviceType("1");
    mrCheckListDTO.setNetworkType("1");
    mrCheckListDTO.setArrayCode("1");
    List<MrCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrCheckListDTO);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(checkListDTOList);
    mrCheckListBusiness.setMapArrayByNetworkType();
    mrCheckListBusiness.setMapNetworkTypeByDeviceType();

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo13() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
    String a = "";
    for (int i = 0; i < 501; i++) {
      a += "t";
    }
    dto.setTarget(a);
//    dto.setDeviceType("1");
    dto.setContent("a");
    dto.setCycle("3");
    dto.setDeviceTypeAll("a");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");

    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    List<MrDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    lstDevice.add(mrDeviceDTO);
    when(mrCheckListRepository.getListDeviceType()).thenReturn(lstDevice);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setDeviceType("1");
    mrCheckListDTO.setNetworkType("1");
    mrCheckListDTO.setArrayCode("1");
    List<MrCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrCheckListDTO);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(checkListDTOList);
    mrCheckListBusiness.setMapArrayByNetworkType();
    mrCheckListBusiness.setMapNetworkTypeByDeviceType();

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo14() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
    dto.setCreatedUser("1");
    String a = "";
    for (int i = 0; i < 2; i++) {
      a += "t";
    }
    dto.setTarget(a);
//    dto.setDeviceType("1");
    dto.setContent("a");
    dto.setCycle("3");
    dto.setDeviceTypeAll("a");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");

    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    List<MrDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    lstDevice.add(mrDeviceDTO);
    when(mrCheckListRepository.getListDeviceType()).thenReturn(lstDevice);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setDeviceType("1");
    mrCheckListDTO.setNetworkType("1");
    mrCheckListDTO.setArrayCode("1");
    List<MrCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrCheckListDTO);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(checkListDTOList);
    mrCheckListBusiness.setMapArrayByNetworkType();
    mrCheckListBusiness.setMapNetworkTypeByDeviceType();

    UsersEntity usersEntity = null;
    when(userRepositoryImpl.getUserByUserName(dto.getCreatedUser())).thenReturn(usersEntity);

    mrCheckListBusiness.validateImportInfo(dto, list);
  }

  @Test
  public void validateImportInfo15() {
    PowerMockito.mockStatic(I18n.class);
    MrCheckListDTO dto = Mockito.spy(MrCheckListDTO.class);
    dto.setMarketCode("1");
    dto.setArrayCode("1");
    dto.setNetworkType("1");
    dto.setCreatedUser("1");
    String a = "";
    for (int i = 0; i < 2; i++) {
      a += "t";
    }
    dto.setTarget(a);
//    dto.setDeviceType("1");
    dto.setContent("a");
    dto.setCycle("3");
    dto.setDeviceTypeAll("a");
    List<MrCheckListDTO> list = Mockito.spy(ArrayList.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    mrCheckListBusiness.setMapCountryName();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(listData);
    mrCheckListBusiness.setMapArray();

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    mrDeviceDTO.setDeviceType("1");

    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);

    List<MrDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    lstDevice.add(mrDeviceDTO);
    when(mrCheckListRepository.getListDeviceType()).thenReturn(lstDevice);

    MrCheckListDTO mrCheckListDTO = Mockito.spy(MrCheckListDTO.class);
    mrCheckListDTO.setDeviceType("1");
    mrCheckListDTO.setNetworkType("1");
    mrCheckListDTO.setArrayCode("1");
    List<MrCheckListDTO> checkListDTOList = Mockito.spy(ArrayList.class);
    checkListDTOList.add(mrCheckListDTO);
    when(mrCheckListRepository.getListArrayDeviceTypeNetworkType()).thenReturn(checkListDTOList);
    mrCheckListBusiness.setMapArrayByNetworkType();
    mrCheckListBusiness.setMapNetworkTypeByDeviceType();

    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setIsEnable(1L);
    when(userRepositoryImpl.getUserByUserName(dto.getCreatedUser())).thenReturn(usersEntity);

    mrCheckListBusiness.validateImportInfo(dto, list);
  }
}
