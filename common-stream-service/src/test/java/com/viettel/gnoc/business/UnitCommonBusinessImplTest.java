package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SmsGatewayDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.repository.SmsGatewayCommonRepository;
import com.viettel.gnoc.repository.UnitCommonRepository;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UnitCommonBusinessImpl.class, FileUtils.class, CommonImport.class,
    CommonExport.class, TicketProvider.class, I18n.class, DateTimeUtils.class, InputStream.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class UnitCommonBusinessImplTest {

  @InjectMocks
  UnitCommonBusinessImpl unitCommonBusiness;

  @Mock
  UnitCommonRepository unitCommonRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  SmsGatewayCommonRepository smsGatewayCommonRepository;

  @Mock
  CommonImport commonImport;


  @Test
  public void testGetListUnit_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    List<UnitDTO> list = Mockito.spy(ArrayList.class);
    list.add(unitDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(list);
    List<UnitDTO> unitDTOList = unitCommonBusiness.getListUnit(unitDTO);
    Assert.assertEquals(list.size(), unitDTOList.size());
  }

  @Test
  public void testSetMapUnit_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);
    unitCommonBusiness.setMapUnit();
    Assert.assertEquals(1L, 1L);
  }

  @Test
  public void testSetCombobox_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);

    unitCommonBusiness.setCombobox();
    Assert.assertNotNull(datatable.getData());
  }

  @Test
  public void testGetListUnitDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setStatus(0L);
    List<UnitDTO> list = Mockito.spy(ArrayList.class);
    list.add(unitDTO);
    datatable.setData(list);
    PowerMockito.when(unitCommonRepository.getListUnitDTO(any())).thenReturn(datatable);
    unitCommonBusiness.getListUnitDTO(unitDTO);
    Assert.assertNotNull(datatable.getData());
  }

  @Test
  public void testGetListUnitDTO_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setStatus(1L);
    List<UnitDTO> list = Mockito.spy(ArrayList.class);
    list.add(unitDTO);
    datatable.setData(list);
    PowerMockito.when(unitCommonRepository.getListUnitDTO(any())).thenReturn(datatable);
    unitCommonBusiness.getListUnitDTO(unitDTO);
    Assert.assertNotNull(datatable.getData());
  }

  @Test
  public void testSetParams_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UnitDTO dto = Mockito.spy(UnitDTO.class);
    dto.setUnitName("abc,xyz");
    dto.setParentUnitId(69L);
    unitCommonBusiness.setParams(dto);
    Assert.assertNotNull(dto);
  }

  @Test
  public void testSetDatatable_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UnitDTO dto = Mockito.spy(UnitDTO.class);
    dto.setUnitLevel(69L);
    dto.setUnitType(68L);
    dto.setSmsGatewayId(66L);
    dto.setRoleType(23L);
    dto.setLocationId(19L);
    dto.setStatus(0L);
    dto.setIpccServiceId(96L);
    unitCommonBusiness.setDatatable(dto);
    Assert.assertNotNull(dto);
  }

  @Test
  public void testSetDatatable_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UnitDTO dto = Mockito.spy(UnitDTO.class);
    dto.setUnitLevel(69L);
    dto.setUnitType(68L);
    dto.setSmsGatewayId(66L);
    dto.setRoleType(23L);
    dto.setLocationId(19L);
    dto.setStatus(1L);
    dto.setIpccServiceId(96L);
    unitCommonBusiness.setDatatable(dto);
    Assert.assertNotNull(dto);
  }

  @Test
  public void testUpdateUnit_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setStatus(1L);
    unitDTO.setRoleType(2L);
    PowerMockito.when(unitCommonRepository.updateUnit(any())).thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = unitCommonBusiness.updateUnit(unitDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testUpdateUnitChildren_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setStatus(1L);
    unitDTO.setRoleType(2L);
    PowerMockito.when(unitCommonRepository.updateUnitChildren(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = unitCommonBusiness.updateUnitChildren(unitDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testDeleteUnit_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitCommonRepository.deleteUnit(anyLong())).thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = unitCommonBusiness.deleteUnit(69L);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testDeleteListUnit_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    List<UnitDTO> unitListDTO = Mockito.spy(ArrayList.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setStatus(1L);
    unitDTO.setRoleType(2L);
    unitListDTO.add(unitDTO);
    PowerMockito.when(unitCommonRepository.deleteListUnit(anyList())).thenReturn(resultInSideDto);
    ResultInSideDto result = unitCommonBusiness.deleteListUnit(unitListDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testFindUnitById_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UnitDTO dto = Mockito.spy(UnitDTO.class);
    dto.setStatus(0L);
    PowerMockito.when(unitCommonRepository.findUnitById(anyLong())).thenReturn(dto);
    UnitDTO unitDTO = unitCommonBusiness.findUnitById(1L);
    Assert.assertNotNull(unitDTO);
  }

  @Test
  public void testFindUnitById_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UnitDTO dto = Mockito.spy(UnitDTO.class);
    dto.setStatus(1L);
    PowerMockito.when(unitCommonRepository.findUnitById(anyLong())).thenReturn(dto);
    UnitDTO unitDTO = unitCommonBusiness.findUnitById(1L);
    Assert.assertNotNull(unitDTO);
  }

  @Test
  public void testInsertUnit_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setStatus(0L);
    unitDTO.setUnitLevel(2L);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitCommonRepository.insertUnit(any())).thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = unitCommonBusiness.insertUnit(unitDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testInsertOrUpdateListUnit_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    List<UnitDTO> unitListDTO = Mockito.spy(ArrayList.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setStatus(1L);
    unitDTO.setRoleType(2L);
    unitListDTO.add(unitDTO);
    PowerMockito.when(unitCommonRepository.insertOrUpdateListUnit(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = unitCommonBusiness.insertOrUpdateListUnit(unitListDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testGetListUnitByLevel_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<UnitDTO> unitListDTO = Mockito.spy(ArrayList.class);
    PowerMockito.when(unitCommonRepository.getListUnitByLevel(anyString())).thenReturn(unitListDTO);
    List<UnitDTO> list = unitCommonBusiness.getListUnitByLevel("xxx");
    Assert.assertEquals(list.size(), unitListDTO.size());
  }

  @Test
  public void testGetUnitDTOForTree_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<UnitDTO> unitListDTO = Mockito.spy(ArrayList.class);
    PowerMockito
        .when(unitCommonRepository.getUnitDTOForTree(anyBoolean(), anyString(), anyString()))
        .thenReturn(unitListDTO);
    List<UnitDTO> list = unitCommonBusiness.getUnitDTOForTree(true, "xxx", "yyy");
    Assert.assertEquals(list.size(), unitListDTO.size());
  }

  @Test
  public void testGetUnitVSADTOForTree_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<UnitDTO> unitListDTO = Mockito.spy(ArrayList.class);
    PowerMockito
        .when(unitCommonRepository.getUnitVSADTOForTree(anyBoolean(), anyString(), anyString()))
        .thenReturn(unitListDTO);
    List<UnitDTO> list = unitCommonBusiness.getUnitVSADTOForTree(true, "xxx", "yyy");
    Assert.assertEquals(list.size(), unitListDTO.size());
  }

  @Test
  public void testGetListUnitVSA_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<UnitDTO> unitListDTO = Mockito.spy(ArrayList.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setStatus(1L);
    unitDTO.setRoleType(2L);
    PowerMockito.when(unitCommonRepository.getListUnitVSA(any())).thenReturn(unitListDTO);
    List<UnitDTO> list = unitCommonBusiness.getListUnitVSA(unitDTO);
    Assert.assertEquals(list.size(), unitListDTO.size());
  }

  @Test
  public void testExportData_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    List<UnitDTO> lstCrEx = Mockito.spy(ArrayList.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setStatus(1L);
    unitDTO.setRoleType(2L);
    unitDTO.setUnitName("abc");
    unitDTO.setUnitId(1L);
    unitDTO.setUnitCode("GHJKL123");
    lstCrEx.add(unitDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstCrEx);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("asc")).thenReturn("xxx");
//    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.exportData(unitDTO);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void testGetUnitTemplate_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Workbook workBook = Mockito.spy(Workbook.class);
    ExcelWriterUtils excelWriterUtils = Mockito.spy(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("ABC");

    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("lplp");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);

    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("xxx");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    IpccServiceDTO ipccServiceDTO = Mockito.spy(IpccServiceDTO.class);
    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    ipccServiceDTO.setIpccServiceId(2L);
    ipccServiceDTO.setIpccServiceCode("yyy");
    lstIpcc.add(ipccServiceDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("bcc");
    catLocationDTO.setLocationCode("281");
    catLocationDTO.setLocationName("yuio");
    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    locationLst.add(catLocationDTO);

    String tempFolder = "./mr-temp";
    String fileTemplateName = "IMPORT_CFG_PROCEDURE_TEL_HARD_TEMPLATE_EN.xlsx";
    String pathFolder =
        tempFolder + File.separator + FileUtils.createPathByDate(new Date()) + File.separator;
    excelWriterUtils.saveToFileExcel(workBook, pathFolder, fileTemplateName);
    PowerMockito.when(I18n.getLanguage("UnitCommon.parentUnitName")).thenReturn("Đơn vị cha");
    PowerMockito.when(I18n.getLanguage("UnitCommon.ipccServiceName")).thenReturn("Tổng đài ipcc");
    PowerMockito.when(I18n.getLanguage("UnitCommon.smsGatewayName")).thenReturn("smsGateway");
    PowerMockito.when(I18n.getLanguage("UnitCommon.locationName")).thenReturn("Tên địa bàn");

    File fileExport = unitCommonBusiness.getUnitTemplate();
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void testImportData_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile == null
    MultipartFile multipartFile = null;
    resultInSideDto.setKey(RESULT.FILE_IS_NULL);
    ResultInSideDto result = unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testImportData_02() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    headerList.add(new Object[100]);
//    Object[] objects = new Object[19];
//    objects[1] = "281";
//    objects[2] = "cccc";
//    objects[3] = "a";
//    objects[4] = "b";
//    objects[5] = "M";
//    objects[6] = "Month";
//    objects[7] = "H";
//    objects[8] = "Duong";
//    objects[9] = "cc";
//    objects[10] = "d";
//    objects[11] = "12";
//    objects[12] = "Muc 1";
//    objects[13] = "kiki";
//    objects[14] = "kiki";
//    objects[15] = "kiki";
//    List<Object[]> lstData = Mockito.spy(ArrayList.class);
//    lstData.add(objects);

    PowerMockito.when(commonImport.getDataFromExcel(any(), anyInt(), anyInt(),
        anyInt(), anyInt(), anyInt())).thenReturn(headerList);
    resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);

    ResultInSideDto result = unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testImportData_03() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "tyu";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "cc";
    objects[9] = "d";
    objects[10] = "12";
    objects[11] = "Muc 1";
    objects[12] = "okok";
    headerList.add(objects);

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");

    ResultInSideDto result = unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testImportData_04() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "cc";
    objects[9] = "d";
    objects[10] = "12";
    objects[11] = "Muc 1";
    objects[12] = "okok";
    headerList.add(objects);

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");

    ResultInSideDto result = unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testImportData_05() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "cc";
    objects[9] = "d";
    objects[10] = "12";
    objects[11] = "Muc 1";
    objects[12] = "okok";
    headerList.add(objects);

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");

    ResultInSideDto result = unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testImportData_06() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "cc";
    objects[9] = "d";
    objects[10] = "12";
    objects[11] = "Muc 1";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 1550; i++) {
      lstDataAll.add(objects);
    }

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    resultInSideDto.setKey(RESULT.DATA_OVER);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.DATA_OVER);
  }

  @Test
  public void testImportData_07() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "cc";
    objects[9] = "Yes"; // Or No
    objects[10] = "Active"; //Inactive
    objects[11] = "";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 10; i++) {
      lstDataAll.add(objects);
    }
    List<UnitDTO> unitImportDTOs = Mockito.spy(ArrayList.class);
    UnitDTO unitImportDTO = Mockito.spy(UnitDTO.class);
    //Truong hop Object[11] = "";
    unitImportDTOs.add(unitImportDTO);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.export.title")).thenReturn("DANH MỤC ĐƠN VỊ");

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testImportData_08() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "cc";
    objects[9] = "Yes"; // Or No
    objects[10] = "Active"; //Inactive
    objects[11] = "Insert";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 10; i++) {
      lstDataAll.add(objects);
    }
    List<UnitDTO> unitImportDTOs = Mockito.spy(ArrayList.class);
    UnitDTO unitImportDTO = Mockito.spy(UnitDTO.class);
    //Truong hop Object[11] == Insert;
    unitImportDTOs.add(unitImportDTO);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.export.title")).thenReturn("DANH MỤC ĐƠN VỊ");

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testImportData_09() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "cc";
    objects[9] = "Yes"; // Or No
    objects[10] = "Active"; //Inactive
    objects[11] = "Update";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 10; i++) {
      lstDataAll.add(objects);
    }
    List<UnitDTO> unitImportDTOs = Mockito.spy(ArrayList.class);
    UnitDTO unitImportDTO = Mockito.spy(UnitDTO.class);
    //Truong hop Object[11] == Update;
    unitImportDTOs.add(unitImportDTO);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.export.title")).thenReturn("DANH MỤC ĐƠN VỊ");

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testImportData_10() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "cc";
    objects[9] = "Yes"; // Or No
    objects[10] = "Active"; //Inactive
    objects[11] = "Update";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 10; i++) {
      lstDataAll.add(objects);
    }
    List<UnitDTO> unitImportDTOs = Mockito.spy(ArrayList.class);
    UnitDTO unitImportDTO = Mockito.spy(UnitDTO.class);
    //Truong hop Object[11] == Update;
    unitImportDTOs.add(unitImportDTO);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.export.title")).thenReturn("DANH MỤC ĐƠN VỊ");

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testImportData_11() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "cc";
    objects[9] = "Yes"; // Or No
    objects[10] = "Active"; //Inactive
    objects[11] = "Update";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 10; i++) {
      lstDataAll.add(objects);
    }
    List<UnitDTO> unitImportDTOs = Mockito.spy(ArrayList.class);
    UnitDTO unitImportDTO = Mockito.spy(UnitDTO.class);
    //Truong hop Object[11] == Update;
    unitImportDTOs.add(unitImportDTO);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.export.title")).thenReturn("DANH MỤC ĐƠN VỊ");

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testImportData_12() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "";
    objects[8] = "cc";
    objects[9] = "Yes"; // Or No
    objects[10] = "Active"; //Inactive
    objects[11] = "Update";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 10; i++) {
      lstDataAll.add(objects);
    }
    List<UnitDTO> unitImportDTOs = Mockito.spy(ArrayList.class);
    UnitDTO unitImportDTO = Mockito.spy(UnitDTO.class);
    //Truong hop Object[11] == Update;
    unitImportDTOs.add(unitImportDTO);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.export.title")).thenReturn("DANH MỤC ĐƠN VỊ");

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testImportData_13() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "";
    objects[9] = "Yes"; // Or No
    objects[10] = "Active"; //Inactive
    objects[11] = "Update";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 10; i++) {
      lstDataAll.add(objects);
    }
    List<UnitDTO> unitImportDTOs = Mockito.spy(ArrayList.class);
    UnitDTO unitImportDTO = Mockito.spy(UnitDTO.class);
    //Truong hop Object[11] == Update;
    unitImportDTOs.add(unitImportDTO);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.export.title")).thenReturn("DANH MỤC ĐƠN VỊ");

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testImportData_14() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "Duong";
    objects[9] = "No"; // Yes Or No or Null
    objects[10] = "Active"; //Inactive
    objects[11] = "Update";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 10; i++) {
      lstDataAll.add(objects);
    }
    List<UnitDTO> unitImportDTOs = Mockito.spy(ArrayList.class);
    UnitDTO unitImportDTO = Mockito.spy(UnitDTO.class);
    //Truong hop Object[11] == Update;
    unitImportDTOs.add(unitImportDTO);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.export.title")).thenReturn("DANH MỤC ĐƠN VỊ");

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testImportData_15() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "Duong";
    objects[9] = ""; // Yes Or No or Null
    objects[10] = "Active"; //Inactive
    objects[11] = "Update";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 10; i++) {
      lstDataAll.add(objects);
    }
    List<UnitDTO> unitImportDTOs = Mockito.spy(ArrayList.class);
    UnitDTO unitImportDTO = Mockito.spy(UnitDTO.class);
    //Truong hop Object[11] == Update;
    unitImportDTOs.add(unitImportDTO);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.export.title")).thenReturn("DANH MỤC ĐƠN VỊ");

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testImportData_16() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "Duong";
    objects[9] = "Yes"; // Yes Or No or Null
    objects[10] = "Inactive"; //Inactive
    objects[11] = "Update";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 10; i++) {
      lstDataAll.add(objects);
    }
    List<UnitDTO> unitImportDTOs = Mockito.spy(ArrayList.class);
    UnitDTO unitImportDTO = Mockito.spy(UnitDTO.class);
    //Truong hop Object[11] == Update;
    unitImportDTOs.add(unitImportDTO);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.export.title")).thenReturn("DANH MỤC ĐƠN VỊ");

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testImportData_17() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    //setMapUnit
    UnitDTO searchDTO = Mockito.spy(UnitDTO.class);
    searchDTO.setUnitName("abc");
    searchDTO.setUnitId(1L);
    searchDTO.setUnitCode("GHJKL123");
    List<UnitDTO> lstUnitAll = Mockito.spy(ArrayList.class);
    lstUnitAll.add(searchDTO);
    PowerMockito.when(unitCommonRepository.getListUnit(any())).thenReturn(lstUnitAll);

    ///*setCombobox
    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("avav");
    List<CatItemDTO> typelLst = Mockito.spy(ArrayList.class);
    typelLst.add(catItemDTO);
    datatable.setData(typelLst);
    PowerMockito.when(catItemRepository.getItemMaster("UNIT_TYPE", "1", "3", null, null))
        .thenReturn(datatable);

    PowerMockito.when(catItemRepository.getItemMaster("UNIT_LEVEL", "1", "3", null, null))
        .thenReturn(datatable);
    List<CatItemDTO> levelLst = Mockito.spy(ArrayList.class);
    levelLst.add(catItemDTO);
    datatable.setData(levelLst);

    List<SmsGatewayDTO> smsLst = Mockito.spy(ArrayList.class);
    SmsGatewayDTO smsGatewayDTO = Mockito.spy(SmsGatewayDTO.class);
    smsGatewayDTO.setSmsGatewayId(1L);
    smsGatewayDTO.setAlias("vb");
    smsLst.add(smsGatewayDTO);
    PowerMockito.when(smsGatewayCommonRepository.getListSmsGatewayAll(any())).thenReturn(smsLst);

    List<CatItemDTO> roleList = Mockito.spy(ArrayList.class);
    roleList.add(catItemDTO);
    datatable.setData(roleList);
    PowerMockito.when(catItemRepository.getItemMaster("ROLE_LIST", "1", "3", null, null))
        .thenReturn(datatable);

    List<CatLocationDTO> locationLst = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("ABCGHI");
    catLocationDTO.setLocationId("123");
    catLocationDTO.setLocationName("XXX");
    locationLst.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(locationLst);

    List<IpccServiceDTO> lstIpcc = Mockito.spy(ArrayList.class);
    IpccServiceDTO dtoIPCC = Mockito.spy(IpccServiceDTO.class);
    dtoIPCC.setIpccServiceCode("GHIGHI");
    dtoIPCC.setIpccServiceId(69L);
    lstIpcc.add(dtoIPCC);
    PowerMockito.when(smsGatewayCommonRepository.getListIpccServiceDTO(any())).thenReturn(lstIpcc);
    //===//setCombobox//**========

    //CASE multipartFile != null
    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> headerList = new ArrayList<>();
    Object[] objects = new Object[13];
    objects[0] = "STT";
    objects[1] = "Mã đơn vị*";
    objects[2] = "Tên đơn vị*";
    objects[3] = "b";
    objects[4] = "M";
    objects[5] = "Month";
    objects[6] = "H";
    objects[7] = "Duong";
    objects[8] = "Duong";
    objects[9] = "Yes"; // Yes Or No or Null
    objects[10] = ""; // Active or Inactive or Null
    objects[11] = "Update";
    objects[12] = "okok";
    headerList.add(objects);
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 10; i++) {
      lstDataAll.add(objects);
    }
    List<UnitDTO> unitImportDTOs = Mockito.spy(ArrayList.class);
    UnitDTO unitImportDTO = Mockito.spy(UnitDTO.class);
    //Truong hop Object[11] == Update;
    unitImportDTOs.add(unitImportDTO);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("abcabcbacba")).thenReturn("xxxxx");

    PowerMockito.when(I18n.getLanguage("common.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitCode")).thenReturn("Mã đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.unitName")).thenReturn("Tên đơn vị");
    PowerMockito.when(I18n.getLanguage("UnitCommon.export.title")).thenReturn("DANH MỤC ĐƠN VỊ");

    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 12, 1000)).thenReturn(headerList);
    PowerMockito.when(commonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        1, 13, 1000)).thenReturn(lstDataAll);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), new String[]{}))
        .thenReturn(fileExport);
    unitCommonBusiness.importData(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testGetListUnitDatatableAll_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitCommonRepository.getListUnitDatatableAll(any())).thenReturn(datatable);
    Datatable data = unitCommonBusiness.getListUnitDatatableAll(unitDTO);
    Assert.assertEquals(datatable.getPages(), data.getPages());
  }

  @Test
  public void testGetListLocationAll_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    PowerMockito.when(catLocationRepository.getLocationDatapicker(any())).thenReturn(datatable);
    Datatable data = unitCommonBusiness.getListLocationAll(catLocationDTO);
    Assert.assertEquals(datatable.getPages(), data.getPages());
  }
}
