package com.viettel.gnoc.mr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.mr.repository.MrDeviceCDRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
@PrepareForTest({MrDeviceCDBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class, XSSFWorkbook.class,
    ExcelWriterUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MrDeviceCDBusinessImplTest {

  @InjectMocks
  MrDeviceCDBusinessImpl mrDeviceCDBusiness;

  @Mock
  MrDeviceCDRepository mrDeviceCDRepository;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  UserRepository userRepository;

  @Test
  public void testGetComboboxDeviceType() {
    List<MrDeviceCDDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrDeviceCDRepository.getComboboxDeviceType()).thenReturn(expected);

    List<MrDeviceCDDTO> actual = mrDeviceCDBusiness.getComboboxDeviceType();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetComboboxStationCode() {
    List<MrDeviceCDDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrDeviceCDRepository.getComboboxStationCode()).thenReturn(expected);

    List<MrDeviceCDDTO> actual = mrDeviceCDBusiness.getComboboxStationCode();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testOnSearch_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrDeviceCD.statusValue1")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrDeviceCD.statusValue2")).thenReturn("2");

    MrDeviceCDDTO mrDeviceCDDTO = Mockito.spy(MrDeviceCDDTO.class);
    mrDeviceCDDTO.setSortName("statusDisplay");
    mrDeviceCDDTO.setStatusDisplay("1");

    List<MrDeviceCDDTO> mrDeviceCDDTOList = Mockito.spy(ArrayList.class);
    mrDeviceCDDTOList.add(mrDeviceCDDTO);

    Datatable expected = Mockito.spy(Datatable.class);
    expected.setData(mrDeviceCDDTOList);

    PowerMockito.when(mrDeviceCDRepository.onSearch(any())).thenReturn(expected);

    Datatable actual = mrDeviceCDBusiness.onSearch(mrDeviceCDDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testOnSearch_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrDeviceCD.statusValue1")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrDeviceCD.statusValue2")).thenReturn("2");

    MrDeviceCDDTO mrDeviceCDDTO = Mockito.spy(MrDeviceCDDTO.class);
    mrDeviceCDDTO.setSortName("statusDisplay");
    mrDeviceCDDTO.setStatusDisplay("2");

    List<MrDeviceCDDTO> mrDeviceCDDTOList = Mockito.spy(ArrayList.class);
    mrDeviceCDDTOList.add(mrDeviceCDDTO);

    Datatable expected = Mockito.spy(Datatable.class);
    expected.setData(mrDeviceCDDTOList);

    PowerMockito.when(mrDeviceCDRepository.onSearch(any())).thenReturn(expected);

    Datatable actual = mrDeviceCDBusiness.onSearch(mrDeviceCDDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testOnSearch_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrDeviceCD.statusValue1")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrDeviceCD.statusValue2")).thenReturn("2");

    MrDeviceCDDTO mrDeviceCDDTO = Mockito.spy(MrDeviceCDDTO.class);
    mrDeviceCDDTO.setSortName("statusDisplay");
    mrDeviceCDDTO.setStatusDisplay("3");

    List<MrDeviceCDDTO> mrDeviceCDDTOList = Mockito.spy(ArrayList.class);
    mrDeviceCDDTOList.add(mrDeviceCDDTO);

    Datatable expected = Mockito.spy(Datatable.class);
    expected.setData(mrDeviceCDDTOList);

    PowerMockito.when(mrDeviceCDRepository.onSearch(any())).thenReturn(expected);

    Datatable actual = mrDeviceCDBusiness.onSearch(mrDeviceCDDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testExportSearchData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT_TEST");
    PowerMockito.when(I18n.getLanguage("mrDeviceCD.statusValue1")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrDeviceCD.statusValue2")).thenReturn("2");

    MrDeviceCDDTO mrDeviceCDDTO = Mockito.spy(MrDeviceCDDTO.class);
    mrDeviceCDDTO.setStatusDisplay("1");

    List<MrDeviceCDDTO> data = Mockito.spy(ArrayList.class);
    data.add(mrDeviceCDDTO);
    PowerMockito.when(
        mrDeviceCDRepository.exportSearchData(any())
    ).thenReturn(data);

    File actual = mrDeviceCDBusiness.exportSearchData(mrDeviceCDDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void testImportMrDeviceCD_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto actual = mrDeviceCDBusiness
        .importMrDeviceCD(null);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportMrDeviceCD_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage("mrDeviceCD.statusValue1")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrDeviceCD.statusValue2")).thenReturn("2");

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);

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
    itemDataCRInside.setValueStr(280L);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        commonStreamServiceProxy.getListLocationByLevelCBBProxy(anyLong(), any())
    ).thenReturn(lstCountry);

    Object[] objects = new Object[]{"1", null, "280", "1", "1", "1", "1", "1", "1"};
    Object[] objects1 = new Object[]{"1", "1", "281", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects);
    lstData.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcel(
            fileImp, 0, 8, 0, 8, 5
        )
    ).thenReturn(lstData);

    MrDeviceCDDTO mrDeviceCDDTO = Mockito.spy(MrDeviceCDDTO.class);
    mrDeviceCDDTO.setMarketCode("280");
    mrDeviceCDDTO.setDeviceName("1");
    mrDeviceCDDTO.setDeviceType("1");
    mrDeviceCDDTO.setStationCode("1");
    mrDeviceCDDTO.setDeviceCdId("1");
    List<MrDeviceCDDTO> lstPutMapImport = Mockito.spy(ArrayList.class);
    lstPutMapImport.add(mrDeviceCDDTO);
    PowerMockito.when(
        mrDeviceCDRepository
            .getListMrDeviceCDDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstPutMapImport);

    UsersEntity userTokenGNOC = Mockito.spy(UsersEntity.class);
    userTokenGNOC.setUserId(1L);
    userTokenGNOC.setIsEnable(1L);
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(userTokenGNOC);

    ResultInSideDto actual = mrDeviceCDBusiness
        .importMrDeviceCD(multipartFile);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testGetFileTemplate() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(280L);
    itemDataCRInside.setDisplayStr("280");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        commonStreamServiceProxy.getListLocationByLevelCBBProxy(anyLong(), any())
    ).thenReturn(lstCountry);

    File actual = mrDeviceCDBusiness.getFileTemplate();

    Assert.assertNotNull(actual);
  }
}
