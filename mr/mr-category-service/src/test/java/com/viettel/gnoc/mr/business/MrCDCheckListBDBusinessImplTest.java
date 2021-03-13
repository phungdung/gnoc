package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCDCheckListBDDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.mr.repository.MrCDCheckListBDRepository;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrCDCheckListBDBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MrCDCheckListBDBusinessImplTest {

  @InjectMocks
  MrCDCheckListBDBusinessImpl mrCDCheckListBDBusiness;

  @Mock
  MrCDCheckListBDRepository mrCDCheckListBDRepository;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  CatItemBusiness catItemBusiness;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrCDCheckListBDBusiness, "tempFolder",
        "./mr-temp");
  }

  @Test
  public void test_getListMrCDCheckListBDPage() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrCDCheckListBDDTO mrCDCheckListBDDTO = Mockito.spy(MrCDCheckListBDDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(new ArrayList<>());
    PowerMockito.when(mrCDCheckListBDRepository.getListMrCDCheckListBDPage(any()))
        .thenReturn(datatable);
    Datatable data = mrCDCheckListBDBusiness.getListMrCDCheckListBDPage(mrCDCheckListBDDTO);
    Assert.assertEquals(datatable.getPages(), data.getPages());
  }

  @Test
  public void test_insert() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrCDCheckListBDDTO mrCDCheckListBDDTO = Mockito.spy(MrCDCheckListBDDTO.class);
    PowerMockito.when(mrCDCheckListBDRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrCDCheckListBDBusiness.insert(mrCDCheckListBDDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Quốc gia,Mảng,Loại thiết bị,Chu kỳ,Nội dung kiểm tra đã tồn tại");
    List<MrCDCheckListBDDTO> list = Mockito.spy(ArrayList.class);
    list.add(new MrCDCheckListBDDTO());
    PowerMockito.when(mrCDCheckListBDRepository.getListMrCDCheckListBDDTO(any(), anyBoolean()))
        .thenReturn(list);
    result = mrCDCheckListBDBusiness.insert(mrCDCheckListBDDTO);
    Assert.assertEquals(result.getKey(), RESULT.DUPLICATE);
  }

  @Test
  public void test_update() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrCDCheckListBDDTO oldDTO = Mockito.spy(MrCDCheckListBDDTO.class);
    oldDTO.setCheckListId(1L);
    oldDTO.setDeviceType(null);
    oldDTO.setMarketCode("A");
    oldDTO.setArrayCode("A");
    oldDTO.setCycle("A");
    oldDTO.setContent("A");
    PowerMockito.when(mrCDCheckListBDRepository.getDetail(anyLong())).thenReturn(oldDTO);
    List<MrCDCheckListBDDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrCDCheckListBDRepository.getListMrCDCheckListBDDTO(any(), anyBoolean()))
        .thenReturn(list);
    PowerMockito.when(mrCDCheckListBDRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);

    MrCDCheckListBDDTO mrCDCheckListBDDTO = Mockito.spy(MrCDCheckListBDDTO.class);
    mrCDCheckListBDDTO.setCheckListId(1L);
    mrCDCheckListBDDTO.setDeviceType(null);
    mrCDCheckListBDDTO.setMarketCode("B");
    mrCDCheckListBDDTO.setArrayCode("B");
    mrCDCheckListBDDTO.setCycle("B");
    mrCDCheckListBDDTO.setContent("B");
    ResultInSideDto result = mrCDCheckListBDBusiness.update(mrCDCheckListBDDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());

    list.add(new MrCDCheckListBDDTO());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Quốc gia,Mảng,Loại thiết bị,Chu kỳ,Nội dung kiểm tra đã tồn tại");
    result = mrCDCheckListBDBusiness.update(mrCDCheckListBDDTO);
    Assert.assertEquals(result.getKey(), RESULT.DUPLICATE);

    oldDTO.setDeviceType("A");
    mrCDCheckListBDDTO.setDeviceType("B");
    result = mrCDCheckListBDBusiness.update(mrCDCheckListBDDTO);
    Assert.assertEquals(result.getKey(), RESULT.DUPLICATE);
  }

  @Test
  public void test_delete() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(mrCDCheckListBDRepository.delete(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrCDCheckListBDBusiness.delete(1L);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void test_getDetail() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrCDCheckListBDDTO mrCDCheckListBDDTO = Mockito.spy(MrCDCheckListBDDTO.class);
    mrCDCheckListBDDTO.setCheckListId(1L);
    mrCDCheckListBDDTO.setMarketCode("A");
    mrCDCheckListBDDTO.setArrayCode("A");
    mrCDCheckListBDDTO.setCycle("A");
    mrCDCheckListBDDTO.setContent("A");
    PowerMockito.when(mrCDCheckListBDRepository.getDetail(anyLong()))
        .thenReturn(mrCDCheckListBDDTO);
    setFinalStatic(MrCDCheckListBDBusinessImpl.class.getDeclaredField("log"), logger);
    MrCDCheckListBDDTO result = mrCDCheckListBDBusiness.getDetail(1L);
    Assert.assertEquals(result, mrCDCheckListBDDTO);
  }

  @Test
  public void test_getComboboxArray() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrCDCheckListBDRepository.getComboboxArray()).thenReturn(list);
    List<CatItemDTO> result = mrCDCheckListBDBusiness.getComboboxArray();
    Assert.assertEquals(result, list);
  }

  @Test
  public void test_getDeviceTypeCbb() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<MrDeviceCDDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrCDCheckListBDRepository.getDeviceTypeCbb(anyString())).thenReturn(list);
    List<MrDeviceCDDTO> result = mrCDCheckListBDBusiness.getDeviceTypeCbb("1");
    Assert.assertEquals(result, list);
  }

  @Test
  public void test_getComboboxActivities() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrCDCheckListBDRepository.getComboboxActivities(anyLong())).thenReturn(list);
    List<CatItemDTO> result = mrCDCheckListBDBusiness.getComboboxActivities(1L);
    Assert.assertEquals(result, list);
  }

  @Test
  public void test_exportData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any())).thenReturn("CheckList Bảo Dưỡng");
    List<MrCDCheckListBDDTO> lstCrEx = Mockito.spy(ArrayList.class);
    MrCDCheckListBDDTO child = Mockito.spy(MrCDCheckListBDDTO.class);
    lstCrEx.add(child);
    PowerMockito.when(mrCDCheckListBDRepository.getListAll(any())).thenReturn(lstCrEx);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrCDCheckListBDDTO mrCDCheckListBDDTO = Mockito.spy(MrCDCheckListBDDTO.class);
    File file = new File("input.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), anyString()))
        .thenReturn(file);
    mrCDCheckListBDBusiness.exportData(mrCDCheckListBDDTO);
  }

  @Test
  public void test_getListMrCDCheckListBDDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<MrCDCheckListBDDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrCDCheckListBDRepository.getListMrCDCheckListBDDTO(any(), anyBoolean()))
        .thenReturn(list);
    MrCDCheckListBDDTO mrCDCheckListBDDTO = Mockito.spy(MrCDCheckListBDDTO.class);
    List<MrCDCheckListBDDTO> result = mrCDCheckListBDBusiness
        .getListMrCDCheckListBDDTO(mrCDCheckListBDDTO);
    Assert.assertEquals(result, list);
  }

  @Test
  public void test_importMrCDCheckListBD() throws Exception {
    MrCDCheckListBDBusinessImpl classUnderTest = PowerMockito.spy(mrCDCheckListBDBusiness);
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Mockito.when(catItemBusiness.getListItemByCategoryAndParent(anyString(), anyString()))
        .thenReturn(new ArrayList<>());
    // Mock static method
    PowerMockito.mockStatic(I18n.class);
    Mockito.when(I18n.getLanguage(anyString())).thenReturn("File không đúng định dạng mẫu");

    PowerMockito.mockStatic(FileUtils.class);
    Mockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");

    PowerMockito.mockStatic(CommonImport.class);
    List<Object[]> lstHeader = new ArrayList<>();
    lstHeader.add(new Object[100]);
    List<Object[]> lstData = new ArrayList<>();
    lstData.add(new Object[100]);
    Mockito.when(CommonImport.getDataFromExcelFile(any(File.class), anyInt(),//sheet
        anyInt(),//begin row
        anyInt(),//from column
        anyInt(),//to column
        anyInt())).thenReturn(lstHeader, lstData);
    // Mock private method
    PowerMockito.doReturn(true).when(classUnderTest, "validFileFormat", any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.doReturn(resultInSideDto).when(classUnderTest, "validateDataImport", any());
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    setFinalStatic(MrCDCheckListBDBusinessImpl.class.getDeclaredField("log"), logger);
    ResultInSideDto result = classUnderTest.importMrCDCheckListBD(multipartFile);
    assertEquals(RESULT.SUCCESS, result.getKey());

    lstData.clear();
    Mockito.when(CommonImport.getDataFromExcelFile(any(File.class), anyInt(),//sheet
        anyInt(),//begin row
        anyInt(),//from column
        anyInt(),//to column
        anyInt())).thenReturn(lstHeader, lstData);
    result = classUnderTest.importMrCDCheckListBD(multipartFile);
    assertEquals(RESULT.NODATA, result.getKey());

    for (int i = 0; i <= 1500; i++) {
      lstData.add(new Object[100]);
    }
    Mockito.when(CommonImport.getDataFromExcelFile(any(File.class), anyInt(),//sheet
        anyInt(),//begin row
        anyInt(),//from column
        anyInt(),//to column
        anyInt())).thenReturn(lstHeader, lstData);
    result = classUnderTest.importMrCDCheckListBD(multipartFile);
    assertEquals(RESULT.DATA_OVER, result.getKey());

    lstHeader.clear();
    Mockito.when(CommonImport.getDataFromExcelFile(any(File.class), anyInt(),//sheet
        anyInt(),//begin row
        anyInt(),//from column
        anyInt(),//to column
        anyInt())).thenReturn(lstHeader, lstData);
    result = classUnderTest.importMrCDCheckListBD(multipartFile);
    assertEquals(RESULT.ERROR, result.getKey());

    multipartFile = null;
    result = classUnderTest.importMrCDCheckListBD(multipartFile);
    assertEquals(RESULT.FILE_IS_NULL, result.getKey());
  }

  @Test
  public void test_getTemplate() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.export.title"))
        .thenReturn("CheckList Bảo Dưỡng");
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.ListMarketName"))
        .thenReturn("List Quốc gia");
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.cycle.export.title"))
        .thenReturn("Danh sách chu kỳ");
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.ListDeviceType"))
        .thenReturn("List Loại thiết bị");
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.array.export.title"))
        .thenReturn("Danh sách mảng");
    List<ItemDataCRInside> countryNameList = Mockito.spy(ArrayList.class);
    ItemDataCRInside dataCRInside = Mockito.spy(ItemDataCRInside.class);
    countryNameList.add(dataCRInside);
    Mockito.when(catLocationBusiness.getListLocationByLevelCBB(any(), any(), any()))
        .thenReturn(countryNameList);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO itemDTO = Mockito.spy(CatItemDTO.class);
    list.add(itemDTO);
    Mockito.when(catItemBusiness.getListItemByCategoryAndParent(any(), any()))
        .thenReturn(list);
    List<MrDeviceCDDTO> mrDeviceCDDTOList = Mockito.spy(ArrayList.class);
    MrDeviceCDDTO mrDeviceCDDTO = Mockito.spy(MrDeviceCDDTO.class);
    mrDeviceCDDTOList.add(mrDeviceCDDTO);
    Mockito.when(mrCDCheckListBDRepository.getDeviceTypeCbb(any()))
        .thenReturn(mrDeviceCDDTOList);
    File result = mrCDCheckListBDBusiness.getTemplate();
    assertNotNull(result);
  }

  @Test
  public void test_validFileFormat() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(null);
    boolean result = mrCDCheckListBDBusiness.validFileFormat(headerList);
    assertEquals(false, result);

    headerList.clear();
    headerList.add(new Object[1]);
    result = mrCDCheckListBDBusiness.validFileFormat(headerList);
    assertEquals(false, result);

    headerList.clear();
    Object[] objects = new Object[9];
    objects[0] = null;
    for (int i = 1; i < 9; i++) {
      objects[i] = i;
    }
    headerList.add(objects);
    result = mrCDCheckListBDBusiness.validFileFormat(headerList);
    assertEquals(false, result);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.marketName")).thenReturn("Tên Quốc gia");
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.title.Array")).thenReturn("Mảng");
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.title.DeviceType"))
        .thenReturn("Loại thiết bị");
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.cycle")).thenReturn("Chu kỳ (tháng)");
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.purPose")).thenReturn("Chỉ tiêu/Quy định");
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.content")).thenReturn("Nội dung kiểm tra");
    PowerMockito.when(I18n.getLanguage("MrCDCheckListBD.goal")).thenReturn("Mục tiêu");
    headerList.clear();
    objects = new Object[8];
    for (int i = 0; i < 8; i++) {
      objects[i] = i;
    }
    headerList.add(objects);
    result = mrCDCheckListBDBusiness.validFileFormat(headerList);
    assertEquals(false, result);

    headerList.clear();
    objects[1] = "Tên Quốc gia*";
    headerList.add(objects);
    result = mrCDCheckListBDBusiness.validFileFormat(headerList);
    assertEquals(false, result);

    headerList.clear();
    objects[2] = "Mảng*";
    headerList.add(objects);
    result = mrCDCheckListBDBusiness.validFileFormat(headerList);
    assertEquals(false, result);

    headerList.clear();
    objects[3] = "Loại thiết bị";
    headerList.add(objects);
    result = mrCDCheckListBDBusiness.validFileFormat(headerList);
    assertEquals(false, result);

    headerList.clear();
    objects[4] = "Chu kỳ (tháng)*";
    headerList.add(objects);
    result = mrCDCheckListBDBusiness.validFileFormat(headerList);
    assertEquals(false, result);

    headerList.clear();
    objects[5] = "Chỉ tiêu/Quy định";
    headerList.add(objects);
    result = mrCDCheckListBDBusiness.validFileFormat(headerList);
    assertEquals(false, result);

    headerList.clear();
    objects[6] = "Nội dung kiểm tra*";
    headerList.add(objects);
    result = mrCDCheckListBDBusiness.validFileFormat(headerList);
    assertEquals(false, result);

    headerList.clear();
    objects[7] = "Mục tiêu";
    headerList.add(objects);
    result = mrCDCheckListBDBusiness.validFileFormat(headerList);
    assertEquals(true, result);
  }

  @Test
  public void test_validateDataImport() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    File file = new File("./input.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(
        CommonExport
            .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(file);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    Object[] objects1 = new Object[12];
    Object[] objects2 = new Object[12];
    for (int i = 0; i < 12; i++) {
      objects1[i] = null;
      objects2[i] = i + "";
    }
    lstData.add(objects1);
    lstData.add(objects2);
    ResultInSideDto result = mrCDCheckListBDBusiness.validateDataImport(lstData);
  }

  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }
}
