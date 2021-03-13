package com.viettel.gnoc.mr.business;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.*;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import com.viettel.gnoc.mr.repository.MrNodesRepository;
import com.viettel.gnoc.mr.repository.MrTestXaRepository;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.io.File;
import java.io.FileInputStream;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrTestXaBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class, FileInputStream.class,
    ExcelWriterUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrTestXaBusinessImplTest {

  @InjectMocks
  MrTestXaBusinessImpl mrTestXaBusiness;

  @Mock
  MrTestXaRepository mrTestXaRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UserRepository userRepository;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  MrNodesRepository mrNodesRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrTestXaBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void getListDatatableMrCdBatterryDTO() {
    PowerMockito.mockStatic(TicketProvider.class);

    MrCdBatteryDTO mrCdBatteryDTO = Mockito.spy(MrCdBatteryDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    Datatable result = Mockito.spy(Datatable.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mrTestXaRepository.getListDatatableMrCdBatterryDTO(any())
    ).thenReturn(result);

    Datatable actual = mrTestXaBusiness.getListDatatableMrCdBatterryDTO(mrCdBatteryDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void findMrCDBatteryById() {
    MrCdBatteryDTO result = Mockito.spy(MrCdBatteryDTO.class);

    PowerMockito.when(
        mrTestXaRepository.findMrCDBatteryById(anyLong())
    ).thenReturn(result);

    MrCdBatteryDTO actual = mrTestXaBusiness.findMrCDBatteryById(1L);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListLocationCombobox() {
    List<ItemDataCRInside> result = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        mrTestXaRepository.getListLocationCombobox(anyLong())
    ).thenReturn(result);

    List<ItemDataCRInside> actual = mrTestXaBusiness.getListLocationCombobox(1L);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListCountry() {
    List<ItemDataCRInside> result = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        mrTestXaRepository.getListCountry()
    ).thenReturn(result);

    List<ItemDataCRInside> actual = mrTestXaBusiness.getListCountry();

    Assert.assertNotNull(actual);
  }

  @Test
  public void getDetailById() {
    MrCdBatteryDTO result = Mockito.spy(MrCdBatteryDTO.class);

    PowerMockito.when(
        mrTestXaRepository.getDetailById(anyLong())
    ).thenReturn(result);

    MrCdBatteryDTO actual = mrTestXaBusiness.getDetailById(1L);

    Assert.assertNotNull(actual);
  }

  @Test
  public void updateMrCdBatteryDTO() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    MrCdBatteryDTO mrCdBatteryDTO = Mockito.spy(MrCdBatteryDTO.class);
    mrCdBatteryDTO.setDcPowerId("11");
    mrCdBatteryDTO.setWoCode("1_2_3");
    mrCdBatteryDTO.setIswoAccu("0");
    mrCdBatteryDTO.setDcPower("2222");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    List<MrCdBatteryDTO> lstWoCode = Mockito.spy(ArrayList.class);
    lstWoCode.add(mrCdBatteryDTO);
    MrCdBatteryDTO mrCdBatteryDTO1 = Mockito.spy(MrCdBatteryDTO.class);
    mrCdBatteryDTO1.setDcPowerId("11");
    mrCdBatteryDTO1.setWoCode("1_2_3");
    mrCdBatteryDTO1.setIswoAccu("1");
    mrCdBatteryDTO1.setDcPower("2222");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoCode("2222");

    PowerMockito.when(
        mrTestXaRepository
            .findMrCDBatteryByProperty(any())
    ).thenReturn(mrCdBatteryDTO1);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mrTestXaRepository
            .getListWoFromMrCdBattery(anyString())
    ).thenReturn(lstWoCode);
    PowerMockito.when(
        woServiceProxy.findWoByWoCodeNoOffset(anyString())
    ).thenReturn(woInsideDTO);

    ResultInSideDto actual = mrTestXaBusiness.updateMrCdBatteryDTO(mrCdBatteryDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void getListLocationComboboxByCode() {
    List<ItemDataCRInside> lst = Mockito.spy(ArrayList.class);
    lst.add(new ItemDataCRInside());
    PowerMockito.when(mrTestXaRepository.getListLocationComboboxByCode(anyString()))
        .thenReturn(lst);
    List<ItemDataCRInside> result = mrTestXaBusiness.getListLocationComboboxByCode("1");
    Assert.assertEquals(lst.size(), result.size());
  }

  @Test
  public void exportData() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    MrCdBatteryDTO mrCdBatteryDTO = Mockito.spy(MrCdBatteryDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    List<MrCdBatteryDTO> mrCdBatteryDTOS = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mrTestXaRepository.getDataExport(any())
    ).thenReturn(mrCdBatteryDTOS);

    File actual = mrTestXaBusiness.exportData(mrCdBatteryDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void getTemplate() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    File actual = mrTestXaBusiness.getTemplate();

    Assert.assertNotNull(actual);
  }

  @Test
  public void importData_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");
    ResultInSideDto actual = mrTestXaBusiness.importData(null);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void importData_02() throws Exception {
    MrTestXaBusiness classUnderTest = PowerMockito.spy(mrTestXaBusiness);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/test_junit/test.xlsx";
    PowerMockito.when(
        FileUtils
            .saveTempFile(any(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    Object[] objecttest = new Object[]{
        "1", "1", "1", "1", "1", "1", "1", "1"
    };
    for (int i = 0; i < 5002; i++) {
      lstData.add(objecttest);
    }

    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(
            fileImp, 0, 8, 0, 16, 2
        )
    ).thenReturn(lstData);

    PowerMockito.doReturn(true).when(classUnderTest, "validateFileData", any());
    ResultInSideDto actual = classUnderTest.importData(uploadFile);

    Assert.assertEquals("ERROR_NO_DOWNLOAD", actual.getKey());
  }

  @Test
  public void importData_03() throws Exception {
    MrTestXaBusiness classUnderTest = PowerMockito.spy(mrTestXaBusiness);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/test_junit/test.xlsx";
    PowerMockito.when(
        FileUtils
            .saveTempFile(any(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1; i++) {
      Object[] objectTest = new Object[20];
      for (int j = 0; j < objectTest.length; j++) {
        objectTest[j] = "1";
      }
      lstData.add(objectTest);
    }

    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(
            fileImp, 0, 8, 0, 16, 2
        )
    ).thenReturn(lstData);

    PowerMockito.doReturn(true).when(classUnderTest, "validateFileData", any());

    List<MrCdBatteryDTO> lstIDs = Mockito.spy(ArrayList.class);
    MrCdBatteryDTO mrCdBatteryDTO = Mockito.spy(MrCdBatteryDTO.class);
    mrCdBatteryDTO.setDcPowerId("1");
    lstIDs.add(mrCdBatteryDTO);
    PowerMockito.when(mrTestXaRepository.getByIdImport(anyList(), anyString())).thenReturn(lstIDs);
    PowerMockito.when(mrTestXaRepository.getListMrCdBatteryByListId(anyList())).thenReturn(lstIDs);
    PowerMockito.when(mrTestXaRepository.findMrCDBatteryByProperty(any()))
        .thenReturn(new MrCdBatteryDTO());
    PowerMockito.when(mrTestXaRepository.updateMrCdBatteryDTO(any()))
        .thenReturn(new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS));

    ResultInSideDto actual = classUnderTest.importData(uploadFile);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void processFileTemplate() throws Exception {
    ExcelWriterUtils ewu = Mockito.spy(ExcelWriterUtils.class);
    mrTestXaBusiness.processFileTemplate(ewu);
  }

  @Test
  public void exportFileImport() {
    PowerMockito.mockStatic(I18n.class);
    List<Object[]> lst = Mockito.spy(ArrayList.class);
    List<ErrorInfo> err = Mockito.spy(ArrayList.class);
    PowerMockito.when(I18n.getLanguage(anyString()))
        .thenReturn("1");
    mrTestXaBusiness.exportFileImport(lst, "test", err);
  }
}
