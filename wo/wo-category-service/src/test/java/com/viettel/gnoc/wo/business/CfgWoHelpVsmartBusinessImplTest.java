package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.wo.dto.CfgWoHelpVsmartDTO;
import com.viettel.gnoc.wo.repository.CfgWoHelpVsmartRepository;
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
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgWoHelpVsmartBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgWoHelpVsmartBusinessImplTest {

  @InjectMocks
  CfgWoHelpVsmartBusinessImpl cfgWoHelpVsmartBusiness;
  @Mock
  CfgWoHelpVsmartRepository cfgWoHelpVsmartRepository;
  @Mock
  GnocFileRepository gnocFileRepository;
  @Mock
  TicketProvider ticketProvider;
  @Mock
  UnitRepository unitRepository;

  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(cfgWoHelpVsmartBusiness, "tempFolder",
        "./wo-upload");
  }

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(cfgWoHelpVsmartBusiness, "uploadFolder",
        "./wo-upload");
  }

  @Test
  public void findCfgWoHelpVsmartsById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO = Mockito.spy(CfgWoHelpVsmartDTO.class);
    PowerMockito.when(cfgWoHelpVsmartRepository.findCfgWoHelpVsmartsById(anyLong()))
        .thenReturn(cfgWoHelpVsmartDTO);
    GnocFileDto gnocFileDtoResult = Mockito.spy(GnocFileDto.class);
    PowerMockito.when(gnocFileRepository.getGnocFileByDto(any())).thenReturn(gnocFileDtoResult);
    CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO1 = cfgWoHelpVsmartBusiness.findCfgWoHelpVsmartsById(1L);
    Assert.assertNotNull(cfgWoHelpVsmartDTO1);
  }

  @Test
  public void deleteCfgWoHelpVsmart_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgWoHelpVsmartRepository.deleteCfgWoHelpVsmart(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgWoHelpVsmartBusiness.deleteCfgWoHelpVsmart(1L);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void deleteListCfgWoHelpVsmart_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgWoHelpVsmartRepository.deleteListCfgWoHelpVsmart(anyList()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgWoHelpVsmartBusiness.deleteListCfgWoHelpVsmart(anyList());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void getListCfgWoHelpVsmartDTOSearchWeb_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgWoHelpVsmartRepository.getListCfgWoHelpVsmartDTOSearchWeb(any()))
        .thenReturn(datatable);
    Datatable datatable1 = cfgWoHelpVsmartBusiness.getListCfgWoHelpVsmartDTOSearchWeb(any());
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getListCbbSystem_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO item = Mockito.spy(CatItemDTO.class);
    item.setItemValue("1");
    item.setItemName("ID");
    list.add(item);
    PowerMockito.when(cfgWoHelpVsmartRepository.getListCbbSystem()).thenReturn(list);
    List<CatItemDTO> list1 = cfgWoHelpVsmartBusiness.getListCbbSystem();
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void getTemplate() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    PowerMockito.when(I18n.getLanguage("woHelpVsmart.export.extra.sheetName")).thenReturn("b");
    File result = cfgWoHelpVsmartBusiness.getTemplate("1");
    Assert.assertNotNull(result);
  }

  @Test
  public void insertCfgWoHelpVsmart() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    String filePath = "/temp";
    files.add(firstFile);
    Object[] header = new Object[]{"key(*)", "controlType(*)", "keyCode(*)", "value", "format",
        "dataCode", "dataType", "defaultValue"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    ;
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO = Mockito.spy(CfgWoHelpVsmartDTO.class);
    PowerMockito
        .when(cfgWoHelpVsmartRepository.getSequenseCfgWoHelpVsmart("CFG_WO_HELP_VSMART_SEQ"))
        .thenReturn("1");
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        0,//begin row
        0,//from column
        7,//to column
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        1,//begin row
        0,//from column
        7,//to column
        1000
    )).thenReturn(headerList1);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(FileUtils.saveUploadFile(any(), any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = cfgWoHelpVsmartBusiness
        .insertCfgWoHelpVsmart(files, cfgWoHelpVsmartDTO);
    Assert.assertEquals(result.getKey(), "NODATA");
  }

  @Test
  public void updateCfgWoHelpVsmart() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    String filePath = "/temp";
    files.add(firstFile);
    Object[] header = new Object[]{"key(*)", "controlType(*)", "keyCode(*)", "value", "format",
        "dataCode", "dataType", "defaultValue"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    ;
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO = Mockito.spy(CfgWoHelpVsmartDTO.class);
    PowerMockito
        .when(cfgWoHelpVsmartRepository.getSequenseCfgWoHelpVsmart("CFG_WO_HELP_VSMART_SEQ"))
        .thenReturn("1");
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        0,//begin row
        0,//from column
        7,//to column
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        1,//begin row
        0,//from column
        7,//to column
        1000
    )).thenReturn(headerList1);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(FileUtils.saveUploadFile(any(), any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = cfgWoHelpVsmartBusiness
        .updateCfgWoHelpVsmart(files, cfgWoHelpVsmartDTO);
    Assert.assertEquals(result.getKey(), "NODATA");
  }

  @Test
  public void updateCfgWoHelpVsmart_1() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    String filePath = "/temp";
//    files.add(firstFile);
    Object[] header = new Object[]{"key(*)", "controlType(*)", "keyCode(*)", "value", "format",
        "dataCode", "dataType", "defaultValue"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    ;
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO = Mockito.spy(CfgWoHelpVsmartDTO.class);
    cfgWoHelpVsmartDTO.setId(1L);
    cfgWoHelpVsmartDTO.setSystemId(1L);
    cfgWoHelpVsmartDTO.setTypeId("1");
    cfgWoHelpVsmartDTO.setTypeName("1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito
        .when(cfgWoHelpVsmartRepository.getSequenseCfgWoHelpVsmart("CFG_WO_HELP_VSMART_SEQ"))
        .thenReturn("1");
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        0,//begin row
        0,//from column
        7,//to column
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        1,//begin row
        0,//from column
        7,//to column
        1000
    )).thenReturn(headerList1);
    PowerMockito.when(cfgWoHelpVsmartRepository.findCfgWoHelpVsmartsById(anyLong()))
        .thenReturn(cfgWoHelpVsmartDTO);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(FileUtils.saveUploadFile(any(), any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(cfgWoHelpVsmartRepository.updateCfgWoHelpVsmart(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = cfgWoHelpVsmartBusiness
        .updateCfgWoHelpVsmart(files, cfgWoHelpVsmartDTO);
    Assert.assertEquals(result.getKey(), "SUCCESS");
  }


}
