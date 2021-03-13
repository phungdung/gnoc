package com.viettel.gnoc.wo.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.wo.dto.CfgFtOnTimeDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.repository.CfgFtOnTimeRepository;
import com.viettel.gnoc.wo.repository.WoCdGroupUnitRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgFtOnTimeBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgFtOnTimeBusinessImplTest {

  @InjectMocks
  CfgFtOnTimeBusinessImpl cfgFtOnTimeBusiness;
  @Mock
  protected CfgFtOnTimeRepository cfgFtOnTimeRepository;

  @Mock
  protected CatItemRepository catItemRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  WoCdGroupBusiness woCdGroupBusiness;

  @Mock
  UserRepository userRepository;

  @Mock
  WoCdGroupUnitRepository woCdGroupUnitRepository;

  @Test
  public void getListUserByCdGroupCBB() {
  }

  @Test
  public void getLstBusinessCBB() {
  }

  @Test
  public void onSearch() {
  }

  @Test
  public void test_exportSearchData() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    CfgFtOnTimeDTO cfgFtOnTimeDTO = Mockito.spy(CfgFtOnTimeDTO.class);
    File file = cfgFtOnTimeBusiness.exportSearchData(cfgFtOnTimeDTO);
    Assert.assertNull(file);
  }

  @Test
  public void insertOrUpdate() {
  }

  @Test
  public void findById() {
  }

  @Test
  public void test_importData() throws Exception {
    ResultInSideDto result = cfgFtOnTimeBusiness.importData(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void test_importData_1() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    ResultInSideDto result = cfgFtOnTimeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_importData_2() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Object[] header = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        4,//begin row
        0,//from column
        4,//to column
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = cfgFtOnTimeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_importData_3() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    Object[] header = new Object[]{"1", "1", "1", "1", "1"};
    Object[] header1 = new Object[]{"1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    for (int i = 0; i < 3000; i++) {
      headerList1.add(header1);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        4,//begin row
        0,//from column
        4,//to column
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        5,//begin row
        1,//from column
        4,//to column
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = cfgFtOnTimeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");
  }

  @Test
  public void test_importData_4() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    Object[] header = new Object[]{"1", "1", "1", "1", "1"};
    Object[] header1 = new Object[]{"1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    headerList1.add(header1);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupCode("1");
    woCdGroupInsideDTO.setWoGroupId(1L);
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("1");
    List<CatItemDTO> catItemDTOS = Mockito.spy(ArrayList.class);
    catItemDTOS.add(catItemDTO);
    PowerMockito.when(
        catItemRepository.getListItemByCategory(Constants.CATEGORY.CFG_FT_ON_TIME_BUSINESS, null))
        .thenReturn(catItemDTOS);
    PowerMockito.when(woCdGroupBusiness
        .getListWoCdGroupActive(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstCd);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        4,//begin row
        0,//from column
        4,//to column
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        5,//begin row
        1,//from column
        4,//to column
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = cfgFtOnTimeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_getTemplate() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupName("1");
    woCdGroupInsideDTO.setWoGroupCode("1");
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);
    PowerMockito.when(woCdGroupBusiness
        .getListWoCdGroupActive(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstCd);
    File file = cfgFtOnTimeBusiness.getTemplate();
    Assert.assertNotNull(file);
  }

  @Test
  public void delete() {
  }

  @Test
  public void getWoCdGroupByUnitCurrentSession() {
  }

  @Test
  public void test_exportFileEx() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    List lstData = Mockito.spy(ArrayList.class);
    List<ConfigHeaderExport> lstHeaderSheet = Mockito.spy(ArrayList.class);
    File file = cfgFtOnTimeBusiness.exportFileEx(lstData, lstHeaderSheet, "a", "a", "a", "a", "a");
    Assert.assertNull(file);
  }

  @Test
  public void exportFileImport() {
  }
}
