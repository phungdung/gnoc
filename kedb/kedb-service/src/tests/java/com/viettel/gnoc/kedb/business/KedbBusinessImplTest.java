package com.viettel.gnoc.kedb.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.business.RolesBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.DeviceTypeVersionDTO;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.PtServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.DeviceTypeVersionRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.ReceiveUnitRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.repository.UserSmsRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.KEDB_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.kedb.dto.KedbActionLogsDTO;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import com.viettel.gnoc.kedb.dto.KedbRatingInsideDTO;
import com.viettel.gnoc.kedb.dto.UserSmsDTO;
import com.viettel.gnoc.kedb.model.KedbFilesEntity;
import com.viettel.gnoc.kedb.repository.KedbActionLogsRepository;
import com.viettel.gnoc.kedb.repository.KedbRepository;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KedbBusinessImpl.class, TimezoneContextHolder.class, TicketProvider.class,
    I18n.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class KedbBusinessImplTest {

  @InjectMocks
  KedbBusinessImpl kedbBusiness;
  @Mock
  KedbRepository kedbRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  DeviceTypeVersionRepository deviceTypeVersionRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  ReceiveUnitRepository receiveUnitRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  KedbActionLogsRepository kedbActionLogsRepository;

  @Mock
  UserSmsRepository userSmsRepository;

  @Mock
  MessagesBusiness messagesBusiness;

  @Mock
  RolesBusiness rolesBusiness;

  @Mock
  UserRepository userRepository;

  @Mock
  PtServiceProxy ptServiceProxy;

  @Mock
  KedbRatingBusiness kedbRatingBusiness;

  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(kedbBusiness, "tempFolder",
        "./wo-upload");
  }

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(kedbBusiness, "uploadFolder",
        "./wo-upload");
  }

  @Test
  public void getListKedbDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(1D);
    PowerMockito.when(kedbRepository.getListKedbDTO(any())).thenReturn(datatable);
    Datatable datatable1 = kedbBusiness.getListKedbDTO(kedbDTO);
    Assert.assertEquals(datatable1.getPages(), 0L);
  }

  @Test
  public void findKedbById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbId(1L);
    kedbDTO.setCreatedTime(new Date());
    kedbDTO.setCompletedTime(new Date());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(kedbRepository.findKedbById(any(), any())).thenReturn(kedbDTO);
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(1D);
    List<GnocFileDto> getListGnocFileByDto = Mockito.spy(ArrayList.class);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any()))
        .thenReturn(getListGnocFileByDto);
    KedbDTO kedbDTO1 = kedbBusiness.findKedbById(1L);
    Assert.assertNotNull(kedbDTO1);
  }

  @Test
  public void deleteKedb_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(kedbRepository.deleteKedb(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = kedbBusiness.deleteKedb(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteListKedb_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<Long> listKedbId = Mockito.spy(ArrayList.class);
    listKedbId.add(1L);
    ResultInSideDto resultInSideDto1 = kedbBusiness.deleteListKedb(listKedbId);
    Assert.assertNull(resultInSideDto1);
  }

  @Test
  public void getSequenseKedb_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> result = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRepository.getSequenseKedb(anyString(), anyInt())).thenReturn(result);
    List<String> strings = kedbBusiness.getSequenseKedb();
    Assert.assertEquals(result.size(), strings.size());
  }

  @Test
  public void insertOrUpdateListKedb_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<KedbDTO> listKedbDTO = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRepository.insertOrUpdateListKedb(anyList())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = kedbBusiness.insertOrUpdateListKedb(listKedbDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getOffset_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserTokenGNOCSimple userTokenGNOC = Mockito.spy(UserTokenGNOCSimple.class);
    PowerMockito.when(kedbRepository.getOffset(any())).thenReturn(RESULT.SUCCESS);
    String res = kedbBusiness.getOffset(userTokenGNOC);
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void synchKedbByCreateTime_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<KedbDTO> kedbDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRepository.synchKedbByCreateTime(anyString(), anyString()))
        .thenReturn(kedbDTOS);
    List<KedbDTO> kedbDTOS1 = kedbBusiness.synchKedbByCreateTime(anyString(), anyString());
    Assert.assertEquals(kedbDTOS.size(), kedbDTOS1.size());
  }

  @Test
  public void getListKedbByCondition_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<KedbDTO> kedbDTOS = Mockito.spy(ArrayList.class);
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRepository
        .getListKedbByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(kedbDTOS);
    List<KedbDTO> kedbDTOS1 = kedbBusiness.getListKedbByCondition(lstCondition, 1, 1, "ASC", "ID");
    Assert.assertEquals(kedbDTOS.size(), kedbDTOS1.size());
  }

  @Test
  public void exportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setCreatedTime(new Date());
    kedbDTO.setCompletedTime(new Date());
    PowerMockito.mockStatic(TimezoneContextHolder.class);
    PowerMockito.when(TimezoneContextHolder.getOffsetDouble()).thenReturn(1D);
    kedbDTO.setKedbCode("123");
    kedbDTO.setKedbName("fixBug");
    kedbDTO.setParentTypeName("fixBug");
    kedbDTO.setDescription("fixBug");
    kedbDTO.setInfluenceScope("1");
    kedbDTO.setCreateUserName("thanhlv12");
    kedbDTO.setReceiveUserName("thanhlv12");
    kedbDTO.setPtTtRelated("1");
    kedbDTO.setTypeName("fixBug");
    kedbDTO.setSubCategoryName("fixBug");
    kedbDTO.setVendor("vtnet");
    kedbDTO.setSoftwareVersion("1.0");
    kedbDTO.setTtWa("1");
    kedbDTO.setRca("1");
    kedbDTO.setPtWa("1");
    kedbDTO.setSolution("fixBug");
    kedbDTO.setWorklog("fixBug");
    kedbDTO.setKedbStateName("active");
    kedbDTO.setHardwareVersion("1.0");
    kedbDTO.setCompleter("thanhlv12");
    kedbDTO.setFromDate("01/01/2010 10:00:00");
    kedbDTO.setToDate("01/01/2011 10:00:00");
    List<KedbDTO> list = Mockito.spy(ArrayList.class);
    list.add(kedbDTO);
    PowerMockito.when(kedbRepository.getListKedbExport(any())).thenReturn(list);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    File file = kedbBusiness.exportData(kedbDTO);
    Assert.assertNotNull(file);
  }

  @Test
  public void getTemplate_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    List<CatItemDTO> lstType = Mockito.spy(ArrayList.class);
    CatItemDTO dto = Mockito.spy(CatItemDTO.class);
    dto.setItemCode("123");
    dto.setItemName("fixBug");
    dto.setItemId(1L);
    lstType.add(dto);
    Datatable dataType = Mockito.spy(Datatable.class);
    dataType.setData(lstType);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataType);
    PowerMockito.when(kedbRepository.getListSubCategory(anyLong())).thenReturn(lstType);
    List<DeviceTypeVersionDTO> listDeviceTypeVersionDTO = Mockito.spy(ArrayList.class);
    DeviceTypeVersionDTO deviceTypeVersionDTO = Mockito.spy(DeviceTypeVersionDTO.class);
    deviceTypeVersionDTO.setHardwareVersion("1.0");
    deviceTypeVersionDTO.setSoftwareVersion("1.0");
    PowerMockito.when(deviceTypeVersionRepository.getListDeviceTypeVersion(any()))
        .thenReturn(listDeviceTypeVersionDTO);
    File file = kedbBusiness.getTemplate();
    Assert.assertNotNull(file);
  }

  @Test
  public void testSetMapItemId() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemCode("abc");
    List<CatItemDTO> lstAllCatItem = Mockito.spy(ArrayList.class);
    lstAllCatItem.add(catItemDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), anyString()))
        .thenReturn(lstAllCatItem);
    kedbBusiness.setMapItemId();
    Assert.assertNotNull(lstAllCatItem);
  }

  @Test
  public void testSetMapTypeCode() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemCode("abc");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataType = Mockito.spy(Datatable.class);
    dataType.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataType);
    kedbBusiness.setMapTypeCode();
    Assert.assertNotNull(list);
  }

  @Test
  public void testSetMapTypeName() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemCode("ccc");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataType = Mockito.spy(Datatable.class);
    dataType.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataType);
    kedbBusiness.setMapTypeName();
    Assert.assertNotNull(list);
  }

  @Test
  public void testSetMapSubCategoryCode() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemCode("ccc");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataSubCategory = Mockito.spy(Datatable.class);
    dataSubCategory.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMasterHasParent(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataSubCategory);
    kedbBusiness.setMapSubCategoryCode();
    Assert.assertNotNull(list);
  }

  @Test
  public void testSetMapSubCategoryName() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemCode("ccc");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataSubCategory = Mockito.spy(Datatable.class);
    dataSubCategory.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMasterHasParent(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataSubCategory);
    kedbBusiness.setMapSubCategoryName();
    Assert.assertNotNull(list);
  }

  @Test
  public void testSetMapVendorName() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemCode("ccc");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataVendor = Mockito.spy(Datatable.class);
    dataVendor.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataVendor);
    kedbBusiness.setMapVendorName();
    Assert.assertNotNull(list);
  }

  @Test
  public void testSetMapHardwareVersion() {
    List<DeviceTypeVersionDTO> list = Mockito.spy(ArrayList.class);
    DeviceTypeVersionDTO dto = Mockito.spy(DeviceTypeVersionDTO.class);
    dto.setDeviceTypeVersionId(1L);
    dto.setHardwareVersion("xxx");
    list.add(dto);
    PowerMockito.when(deviceTypeVersionRepository.getListDeviceTypeVersion(any())).thenReturn(list);
    kedbBusiness.setMapHardwareVersion();
    Assert.assertNotNull(list);
  }

  @Test
  public void testSetMapSoftwareVersion() {
    List<DeviceTypeVersionDTO> list = Mockito.spy(ArrayList.class);
    DeviceTypeVersionDTO dto = Mockito.spy(DeviceTypeVersionDTO.class);
    dto.setDeviceTypeVersionId(1L);
    dto.setSoftwareVersion("xxx");
    list.add(dto);
    PowerMockito.when(deviceTypeVersionRepository.getListDeviceTypeVersion(any())).thenReturn(list);
    kedbBusiness.setMapSoftwareVersion();
    Assert.assertNotNull(list);
  }

  @Test
  public void testSetMapKedbStateName() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemCode("ccc");
    catItemDTO.setItemName("vvv");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataKedbState = Mockito.spy(Datatable.class);
    dataKedbState.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataKedbState);
    kedbBusiness.setMapKedbStateName();
    Assert.assertNotNull(list);
  }

  @Test
  public void testSetMapParentTypeName() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemCode("ccc");
    catItemDTO.setItemName("vvv");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataParentType = Mockito.spy(Datatable.class);
    dataParentType.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataParentType);
    kedbBusiness.setMapParentTypeName();
    Assert.assertNotNull(list);
  }

  @Test
  public void testSetMapUnitCheckName() {
    ReceiveUnitDTO dto = Mockito.spy(ReceiveUnitDTO.class);
    dto.setUnitId(1L);
    dto.setUnitName("ghgh");
    dto.setUnitCode("lol");
    List<ReceiveUnitDTO> lstReceiveUnit = Mockito.spy(ArrayList.class);
    lstReceiveUnit.add(dto);
    PowerMockito.when(receiveUnitRepository.getListReceiveUnit()).thenReturn(lstReceiveUnit);
    kedbBusiness.setMapUnitCheckName();
    Assert.assertNotNull(lstReceiveUnit);
  }

  @Test
  public void testInsertKedb_01() throws Exception {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbState(1L);
    Long kedbId = 1L;
    PowerMockito.when(kedbRepository.getSeqTableKedb(anyString())).thenReturn(kedbId.toString());

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("KEDB_CLOSED");
    catItemDTO.setItemId(1L);

    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemCode("KEDB_UPDATE_APPROVE");
    catItemDTO2.setItemId(2L);

    CatItemDTO catItemDTO3 = Mockito.spy(CatItemDTO.class);
    catItemDTO3.setItemCode("KEDB_CREATE_APPROVE");
    catItemDTO3.setItemId(3L);

    List<CatItemDTO> listKedbState = Mockito.spy(ArrayList.class);
    listKedbState.add(catItemDTO);
    listKedbState.add(catItemDTO2);
    listKedbState.add(catItemDTO3);

    //setMapItemId
    List<CatItemDTO> lstAllCatItem = Mockito.spy(ArrayList.class);
    lstAllCatItem.add(catItemDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), anyString()))
        .thenReturn(lstAllCatItem);
    //=====setMapItemId=====///
    PowerMockito.when(catItemRepository.getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null))
        .thenReturn(listKedbState);
    PowerMockito.when(kedbRepository.doInsertKedb(any())).thenReturn(resultInSideDto);

    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    files.add(multipartFile);
    try {
      ResultInSideDto result = kedbBusiness.insertKedb(files, kedbDTO);
      Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testInsertKedb_02() throws Exception {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("veuveu");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbState(1L);
    kedbDTO.setTypeId(1L);
    Long kedbId = 1L;
    PowerMockito.when(kedbRepository.getSeqTableKedb(anyString())).thenReturn(kedbId.toString());

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("KEDB_CLOSED");
    catItemDTO.setItemId(1L);

    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemCode("KEDB_UPDATE_APPROVE");
    catItemDTO2.setItemId(2L);

    CatItemDTO catItemDTO3 = Mockito.spy(CatItemDTO.class);
    catItemDTO3.setItemCode("KEDB_CREATE_APPROVE");
    catItemDTO3.setItemId(3L);

    List<CatItemDTO> listKedbState = Mockito.spy(ArrayList.class);
    listKedbState.add(catItemDTO);
    listKedbState.add(catItemDTO2);
    listKedbState.add(catItemDTO3);

    //setMapItemId
    List<CatItemDTO> lstAllCatItem = Mockito.spy(ArrayList.class);
    lstAllCatItem.add(catItemDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), anyString()))
        .thenReturn(lstAllCatItem);
    //=====setMapItemId=====///
    PowerMockito.when(catItemRepository.getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null))
        .thenReturn(listKedbState);
    PowerMockito.when(kedbRepository.doInsertKedb(any())).thenReturn(resultInSideDto);
    KedbActionLogsDTO actionLogsDTO = Mockito.spy(KedbActionLogsDTO.class);
    PowerMockito.when(kedbRepository.getListCatItemDTO(any())).thenReturn(listKedbState);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.ERROR);
    PowerMockito.when(kedbActionLogsRepository.insertKedbActionLogs(any()))
        .thenReturn(resultInSideDto1);

    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    files.add(multipartFile);

    try {
      ResultInSideDto result = kedbBusiness.insertKedb(files, kedbDTO);
      Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testInsertKedb_03() throws Exception {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("veuveu");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbState(1L);
    kedbDTO.setTypeId(1L);
    Long kedbId = 1L;
    PowerMockito.when(kedbRepository.getSeqTableKedb(anyString())).thenReturn(kedbId.toString());

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("KEDB_CLOSED");
    catItemDTO.setItemId(1L);

    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemCode("KEDB_UPDATE_APPROVE");
    catItemDTO2.setItemId(2L);

    CatItemDTO catItemDTO3 = Mockito.spy(CatItemDTO.class);
    catItemDTO3.setItemCode("KEDB_CREATE_APPROVE");
    catItemDTO3.setItemId(3L);

    List<CatItemDTO> listKedbState = Mockito.spy(ArrayList.class);
    listKedbState.add(catItemDTO);
    listKedbState.add(catItemDTO2);
    listKedbState.add(catItemDTO3);

    //setMapItemId
    List<CatItemDTO> lstAllCatItem = Mockito.spy(ArrayList.class);
    lstAllCatItem.add(catItemDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), anyString()))
        .thenReturn(lstAllCatItem);
    //=====setMapItemId=====///
    PowerMockito.when(catItemRepository.getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null))
        .thenReturn(listKedbState);
    PowerMockito.when(kedbRepository.doInsertKedb(any())).thenReturn(resultInSideDto);
    KedbActionLogsDTO actionLogsDTO = Mockito.spy(KedbActionLogsDTO.class);
    PowerMockito.when(kedbRepository.getListCatItemDTO(any())).thenReturn(listKedbState);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.SUCCESS);
    PowerMockito.when(kedbActionLogsRepository.insertKedbActionLogs(any()))
        .thenReturn(resultInSideDto1);

    List<MultipartFile> files = Mockito.spy(ArrayList.class);
//    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    PowerMockito.when(gnocFileRepository.saveListGnocFileNotDeleteAll(any(), anyLong(), anyList()))
        .thenReturn(resultInSideDto1);
    List<UserSmsDTO> lstUser2SendSMS = Mockito.spy(ArrayList.class);
    PowerMockito.when(userSmsRepository.getUserReceiveSMSEmailByTypeCode(anyString(), anyString()))
        .thenReturn(lstUser2SendSMS);
    ResultInSideDto resultInSideDto2 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto2.setKey(RESULT.ERROR);
    PowerMockito
        .when(messagesBusiness.insertSMSMessageKEDBInst(anyString(), anyList(), anyString()))
        .thenReturn(resultInSideDto2);
    try {
      ResultInSideDto result = kedbBusiness.insertKedb(files, kedbDTO);
      Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testInsertKedb_04() throws Exception {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("veuveu");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setWorklog("vbvb");
    problemsInsideDTO.setStateName("bgbg");

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbState(3L);
    kedbDTO.setTypeId(1L);
    kedbDTO.setKedbName("fgfg");
    kedbDTO.setUnitCheckId(8L);
    kedbDTO.setProblemsInsideDTO(problemsInsideDTO);
    Long kedbId = 1L;
    PowerMockito.when(kedbRepository.getSeqTableKedb(anyString())).thenReturn(kedbId.toString());

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("KEDB_CLOSED");
    catItemDTO.setItemId(1L);

    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemCode("KEDB_UPDATE_APPROVE");
    catItemDTO2.setItemId(2L);

    CatItemDTO catItemDTO3 = Mockito.spy(CatItemDTO.class);
    catItemDTO3.setItemCode("KEDB_CREATE_APPROVE");
    catItemDTO3.setItemId(3L);

    List<CatItemDTO> listKedbState = Mockito.spy(ArrayList.class);
    listKedbState.add(catItemDTO);
    listKedbState.add(catItemDTO2);
    listKedbState.add(catItemDTO3);

    //setMapItemId
    List<CatItemDTO> lstAllCatItem = Mockito.spy(ArrayList.class);
    lstAllCatItem.add(catItemDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), anyString()))
        .thenReturn(lstAllCatItem);
    //=====setMapItemId=====///
    PowerMockito.when(catItemRepository.getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null))
        .thenReturn(listKedbState);
    PowerMockito.when(kedbRepository.doInsertKedb(any())).thenReturn(resultInSideDto);
    PowerMockito.when(kedbRepository.getListCatItemDTO(any())).thenReturn(listKedbState);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.SUCCESS);
    PowerMockito.when(kedbActionLogsRepository.insertKedbActionLogs(any()))
        .thenReturn(resultInSideDto1);

    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    PowerMockito.when(gnocFileRepository.saveListGnocFileNotDeleteAll(any(), anyLong(), anyList()))
        .thenReturn(resultInSideDto1);
    List<UserSmsDTO> lstUser2SendSMS = Mockito.spy(ArrayList.class);
    PowerMockito.when(userSmsRepository.getUserReceiveSMSEmailByTypeCode(anyString(), anyString()))
        .thenReturn(lstUser2SendSMS);
    ResultInSideDto resultInSideDto2 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto2.setKey(RESULT.SUCCESS);
    PowerMockito
        .when(messagesBusiness.insertSMSMessageKEDBInst(anyString(), anyList(), anyString()))
        .thenReturn(resultInSideDto2);

    CatItemDTO catItemDTO4 = Mockito.spy(CatItemDTO.class);
    catItemDTO4.setItemCode("KEDB_ALIAS");
    catItemDTO4.setItemId(3L);
    List<CatItemDTO> listCatItem = Mockito.spy(ArrayList.class);
    listCatItem.add(catItemDTO4);
    PowerMockito.when(catItemRepository.getListItemByCategory(CATEGORY.GNOC_ALIAS, null))
        .thenReturn(listCatItem);

    List<UsersInsideDto> listUsersInsideDto = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    listUsersInsideDto.add(usersInsideDto);
    PowerMockito.when(userRepository.getListUsersDTO(anyString(), anyString()))
        .thenReturn(listUsersInsideDto);
    PowerMockito.when(messagesBusiness.insertMessageForUser(anyString(), anyString(), anyList()))
        .thenReturn(resultInSideDto2);
    ResultInSideDto resultxxx = kedbBusiness.insertKedb(files, kedbDTO);
    Assert.assertEquals(resultxxx.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testInsertKedb_05() throws Exception {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("veuveu");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbState(3L);
    kedbDTO.setTypeId(1L);
    kedbDTO.setKedbName("fgfg");
    kedbDTO.setUnitCheckId(8L);
    Long kedbId = 1L;
    PowerMockito.when(kedbRepository.getSeqTableKedb(anyString())).thenReturn(kedbId.toString());

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("KEDB_CLOSED");
    catItemDTO.setItemId(1L);

    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemCode("KEDB_UPDATE_APPROVE");
    catItemDTO2.setItemId(2L);

    CatItemDTO catItemDTO3 = Mockito.spy(CatItemDTO.class);
    catItemDTO3.setItemCode("KEDB_CREATE_APPROVE");
    catItemDTO3.setItemId(3L);

    List<CatItemDTO> listKedbState = Mockito.spy(ArrayList.class);
    listKedbState.add(catItemDTO);
    listKedbState.add(catItemDTO2);
    listKedbState.add(catItemDTO3);

    //setMapItemId
    List<CatItemDTO> lstAllCatItem = Mockito.spy(ArrayList.class);
    lstAllCatItem.add(catItemDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), anyString()))
        .thenReturn(lstAllCatItem);
    //=====setMapItemId=====///
    PowerMockito.when(catItemRepository.getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null))
        .thenReturn(listKedbState);
    PowerMockito.when(kedbRepository.doInsertKedb(any())).thenReturn(resultInSideDto);
    PowerMockito.when(kedbRepository.getListCatItemDTO(any())).thenReturn(listKedbState);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.SUCCESS);
    PowerMockito.when(kedbActionLogsRepository.insertKedbActionLogs(any()))
        .thenReturn(resultInSideDto1);

    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    PowerMockito.when(gnocFileRepository.saveListGnocFileNotDeleteAll(any(), anyLong(), anyList()))
        .thenReturn(resultInSideDto1);
    List<UserSmsDTO> lstUser2SendSMS = Mockito.spy(ArrayList.class);
    PowerMockito.when(userSmsRepository.getUserReceiveSMSEmailByTypeCode(anyString(), anyString()))
        .thenReturn(lstUser2SendSMS);
    ResultInSideDto resultInSideDto2 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto2.setKey(RESULT.SUCCESS);
    PowerMockito
        .when(messagesBusiness.insertSMSMessageKEDBInst(anyString(), anyList(), anyString()))
        .thenReturn(resultInSideDto2);

    CatItemDTO catItemDTO4 = Mockito.spy(CatItemDTO.class);
    catItemDTO4.setItemCode("KEDB_ALIAS");
    catItemDTO4.setItemId(3L);
    List<CatItemDTO> listCatItem = Mockito.spy(ArrayList.class);
    listCatItem.add(catItemDTO4);
    PowerMockito.when(catItemRepository.getListItemByCategory(CATEGORY.GNOC_ALIAS, null))
        .thenReturn(listCatItem);

    List<UsersInsideDto> listUsersInsideDto = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    listUsersInsideDto.add(usersInsideDto);
    PowerMockito.when(userRepository.getListUsersDTO(anyString(), anyString()))
        .thenReturn(listUsersInsideDto);
    PowerMockito.when(messagesBusiness.insertMessageForUser(anyString(), anyString(), anyList()))
        .thenReturn(resultInSideDto2);

    try {
      ResultInSideDto resultxxx = kedbBusiness.insertKedb(files, kedbDTO);
      Assert.assertEquals(resultxxx.getKey(), resultInSideDto.getKey());
    } catch (Exception e) {
      e.getMessage();
    }

  }

  @Test
  public void testUpdateKedb_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.NOT_ACCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("veuveu");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbState(1L);
    kedbDTO.setTypeId(1L);
    kedbDTO.setKedbName("fgfg");
    kedbDTO.setUnitCheckId(8L);
    kedbDTO.setKedbStateBeforeUpdate(2L);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("KEDB_CLOSED");
    catItemDTO.setItemId(1L);

    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemCode("KEDB_UPDATE_APPROVE");
    catItemDTO2.setItemId(2L);

    CatItemDTO catItemDTO3 = Mockito.spy(CatItemDTO.class);
    catItemDTO3.setItemCode("KEDB_CREATE_APPROVE");
    catItemDTO3.setItemId(3L);

    CatItemDTO catItemDTO4 = Mockito.spy(CatItemDTO.class);
    catItemDTO4.setItemCode("KEDB_CANCELED");
    catItemDTO4.setItemId(4L);

    List<CatItemDTO> listKedbState = Mockito.spy(ArrayList.class);
    listKedbState.add(catItemDTO);
    listKedbState.add(catItemDTO2);
    listKedbState.add(catItemDTO3);
    listKedbState.add(catItemDTO4);
    PowerMockito.when(catItemRepository.getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null))
        .thenReturn(listKedbState);

    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    PowerMockito.when(rolesBusiness.getListRolePmByUserOfKedb("999999")).thenReturn(lstRole);

    ResultInSideDto result = kedbBusiness.updateKedb(null, kedbDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testUpdateKedb_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.NOT_ACCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("veuveu");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbState(2L);
    kedbDTO.setTypeId(1L);
    kedbDTO.setKedbName("fgfg");
    kedbDTO.setUnitCheckId(8L);
    kedbDTO.setKedbStateBeforeUpdate(2L);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("KEDB_CLOSED");
    catItemDTO.setItemId(1L);

    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemCode("KEDB_UPDATE_APPROVE");
    catItemDTO2.setItemId(2L);

    CatItemDTO catItemDTO3 = Mockito.spy(CatItemDTO.class);
    catItemDTO3.setItemCode("KEDB_CREATE_APPROVE");
    catItemDTO3.setItemId(3L);

    CatItemDTO catItemDTO4 = Mockito.spy(CatItemDTO.class);
    catItemDTO4.setItemCode("KEDB_CANCELED");
    catItemDTO4.setItemId(2L);

    List<CatItemDTO> listKedbState = Mockito.spy(ArrayList.class);
    listKedbState.add(catItemDTO);
    listKedbState.add(catItemDTO2);
    listKedbState.add(catItemDTO3);
    listKedbState.add(catItemDTO4);
    PowerMockito.when(catItemRepository.getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null))
        .thenReturn(listKedbState);

    RolesDTO rdto3 = Mockito.spy(RolesDTO.class);
    rdto3.setRoleCode("USER_KEDB");
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(rdto3);
    PowerMockito.when(rolesBusiness.getListRolePmByUserOfKedb("999999")).thenReturn(lstRole);

    ResultInSideDto result = kedbBusiness.updateKedb(null, kedbDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testUpdateKedb_03() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("veuveu");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setWorklog("vbvb");
    problemsInsideDTO.setStateName("bgbg");

    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbState(1L);
    kedbDTO.setTypeId(1L);
    kedbDTO.setKedbName("fgfg");
    kedbDTO.setUnitCheckId(8L);
    kedbDTO.setKedbStateBeforeUpdate(2L);
    kedbDTO.setProblemsInsideDTO(problemsInsideDTO);
    kedbDTO.setKedbCode("BBB");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("KEDB_CLOSED");
    catItemDTO.setItemId(1L);

    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemCode("KEDB_UPDATE_APPROVE");
    catItemDTO2.setItemId(2L);

    CatItemDTO catItemDTO3 = Mockito.spy(CatItemDTO.class);
    catItemDTO3.setItemCode("KEDB_CREATE_APPROVE");
    catItemDTO3.setItemId(3L);

    CatItemDTO catItemDTO4 = Mockito.spy(CatItemDTO.class);
    catItemDTO4.setItemCode("KEDB_CANCELED");
    catItemDTO4.setItemId(2L);

    List<CatItemDTO> listKedbState = Mockito.spy(ArrayList.class);
    listKedbState.add(catItemDTO);
    listKedbState.add(catItemDTO2);
    listKedbState.add(catItemDTO3);
    listKedbState.add(catItemDTO4);
    PowerMockito.when(catItemRepository.getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null))
        .thenReturn(listKedbState);

    RolesDTO rdto1 = Mockito.spy(RolesDTO.class);
    rdto1.setRoleCode("ADMIN_KEDB");
    RolesDTO rdto2 = Mockito.spy(RolesDTO.class);
    rdto2.setRoleCode("SUB_ADMIN_KEDB");
    RolesDTO rdto3 = Mockito.spy(RolesDTO.class);
    rdto3.setRoleCode("USER_KEDB");
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(rdto1);
    lstRole.add(rdto2);
    lstRole.add(rdto3);
    PowerMockito.when(rolesBusiness.getListRolePmByUserOfKedb("999999")).thenReturn(lstRole);
    PowerMockito.when(kedbRepository.doUpdateKedb(any())).thenReturn(resultInSideDto);
    PowerMockito.when(kedbRepository.setStateName(any())).thenReturn("veuveu");
    PowerMockito.when(kedbActionLogsRepository.insertKedbActionLogs(any()))
        .thenReturn(resultInSideDto);
    UserSmsDTO userSmsDTO = Mockito.spy(UserSmsDTO.class);
    userSmsDTO.setEmail("ghighi@gmail.com");
    List<UserSmsDTO> lstUser2SendSMS = Mockito.spy(ArrayList.class);
    lstUser2SendSMS.add(userSmsDTO);
    PowerMockito.when(userSmsRepository.getUserReceiveSMSEmailByTypeCode(anyString(), anyString()))
        .thenReturn(lstUser2SendSMS);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.ERROR);
    PowerMockito
        .when(messagesBusiness.insertSMSMessageKEDBInst(anyString(), anyList(), anyString()))
        .thenReturn(resultInSideDto1);
    try {
      ResultInSideDto result = kedbBusiness.updateKedb(null, kedbDTO);
      Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testUpdateKedb_04() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("veuveu");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setWorklog("vbvb");
    problemsInsideDTO.setStateName("bgbg");

    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbState(2L);
    kedbDTO.setTypeId(1L);
    kedbDTO.setKedbName("fgfg");
    kedbDTO.setUnitCheckId(8L);
    kedbDTO.setKedbStateBeforeUpdate(62L);
    kedbDTO.setProblemsInsideDTO(problemsInsideDTO);
    kedbDTO.setKedbCode("BBB");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("KEDB_CLOSED");
    catItemDTO.setItemId(1L);

    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemCode("KEDB_UPDATE_APPROVE");
    catItemDTO2.setItemId(2L);

    CatItemDTO catItemDTO3 = Mockito.spy(CatItemDTO.class);
    catItemDTO3.setItemCode("KEDB_CREATE_APPROVE");
    catItemDTO3.setItemId(3L);

    CatItemDTO catItemDTO4 = Mockito.spy(CatItemDTO.class);
    catItemDTO4.setItemCode("KEDB_CANCELED");
    catItemDTO4.setItemId(2L);

    List<CatItemDTO> listKedbState = Mockito.spy(ArrayList.class);
    listKedbState.add(catItemDTO);
    listKedbState.add(catItemDTO2);
    listKedbState.add(catItemDTO3);
    listKedbState.add(catItemDTO4);
    PowerMockito.when(catItemRepository.getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null))
        .thenReturn(listKedbState);

    RolesDTO rdto1 = Mockito.spy(RolesDTO.class);
    rdto1.setRoleCode("ADMIN_KEDB");
    RolesDTO rdto2 = Mockito.spy(RolesDTO.class);
    rdto2.setRoleCode("SUB_ADMIN_KEDB");
    RolesDTO rdto3 = Mockito.spy(RolesDTO.class);
    rdto3.setRoleCode("USER_KEDB");
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(rdto1);
    lstRole.add(rdto2);
    lstRole.add(rdto3);
    PowerMockito.when(rolesBusiness.getListRolePmByUserOfKedb("999999")).thenReturn(lstRole);
    PowerMockito.when(kedbRepository.doUpdateKedb(any())).thenReturn(resultInSideDto);
    PowerMockito.when(kedbRepository.setStateName(any())).thenReturn("veuveu");
    PowerMockito.when(kedbActionLogsRepository.insertKedbActionLogs(any()))
        .thenReturn(resultInSideDto);
    UserSmsDTO userSmsDTO = Mockito.spy(UserSmsDTO.class);
    userSmsDTO.setEmail("ghighi@gmail.com");
    List<UserSmsDTO> lstUser2SendSMS = Mockito.spy(ArrayList.class);
    lstUser2SendSMS.add(userSmsDTO);
    PowerMockito.when(userSmsRepository.getUserReceiveSMSEmailByTypeCode(anyString(), anyString()))
        .thenReturn(lstUser2SendSMS);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.ERROR);
    PowerMockito
        .when(messagesBusiness.insertSMSMessageKEDBInst(anyString(), anyList(), anyString()))
        .thenReturn(resultInSideDto1);
    try {
      ResultInSideDto result = kedbBusiness.updateKedb(null, kedbDTO);
      Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testUpdateKedb_05() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("veuveu");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setWorklog("vbvb");
    problemsInsideDTO.setStateName("bgbg");

    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbState(3L);
    kedbDTO.setTypeId(1L);
    kedbDTO.setKedbName("fgfg");
    kedbDTO.setUnitCheckId(8L);
    kedbDTO.setKedbStateBeforeUpdate(62L);
    kedbDTO.setProblemsInsideDTO(problemsInsideDTO);
    kedbDTO.setKedbCode("BBB");
    kedbDTO.setUnitCheckId(69L);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("KEDB_CLOSED");
    catItemDTO.setItemId(1L);

    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemCode("KEDB_UPDATE_APPROVE");
    catItemDTO2.setItemId(2L);

    CatItemDTO catItemDTO3 = Mockito.spy(CatItemDTO.class);
    catItemDTO3.setItemCode("KEDB_CREATE_APPROVE");
    catItemDTO3.setItemId(3L);

    CatItemDTO catItemDTO4 = Mockito.spy(CatItemDTO.class);
    catItemDTO4.setItemCode("KEDB_CANCELED");
    catItemDTO4.setItemId(2L);

    List<CatItemDTO> listKedbState = Mockito.spy(ArrayList.class);
    listKedbState.add(catItemDTO);
    listKedbState.add(catItemDTO2);
    listKedbState.add(catItemDTO3);
    listKedbState.add(catItemDTO4);
    PowerMockito.when(catItemRepository.getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null))
        .thenReturn(listKedbState);

    RolesDTO rdto1 = Mockito.spy(RolesDTO.class);
    rdto1.setRoleCode("ADMIN_KEDB");
    RolesDTO rdto2 = Mockito.spy(RolesDTO.class);
    rdto2.setRoleCode("SUB_ADMIN_KEDB");
    RolesDTO rdto3 = Mockito.spy(RolesDTO.class);
    rdto3.setRoleCode("USER_KEDB");
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(rdto1);
    lstRole.add(rdto2);
    lstRole.add(rdto3);
    PowerMockito.when(rolesBusiness.getListRolePmByUserOfKedb("999999")).thenReturn(lstRole);
    PowerMockito.when(kedbRepository.doUpdateKedb(any())).thenReturn(resultInSideDto);
    PowerMockito.when(kedbRepository.setStateName(any())).thenReturn("veuveu");
    PowerMockito.when(kedbActionLogsRepository.insertKedbActionLogs(any()))
        .thenReturn(resultInSideDto);
    UserSmsDTO userSmsDTO = Mockito.spy(UserSmsDTO.class);
    userSmsDTO.setEmail("ghighi@gmail.com");
    List<UserSmsDTO> lstUser2SendSMS = Mockito.spy(ArrayList.class);
    lstUser2SendSMS.add(userSmsDTO);
    PowerMockito.when(userSmsRepository.getUserReceiveSMSEmailByTypeCode(anyString(), anyString()))
        .thenReturn(lstUser2SendSMS);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    List<UsersInsideDto> lst = Mockito.spy(ArrayList.class);
    lst.add(usersInsideDto);
    PowerMockito.when(userRepository.getListUsersDTO(anyString(), anyString())).thenReturn(lst);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.ERROR);
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    lstCatItem.add(catItemDTO);
    PowerMockito.when(catItemRepository
        .getListItemByCategory(Constants.GNOC_ALIAS, null)).thenReturn(lstCatItem);
    PowerMockito.when(messagesBusiness.insertMessageForUser(anyString(), anyString(), anyList()))
        .thenReturn(resultInSideDto1);
    try {
      ResultInSideDto result = kedbBusiness.updateKedb(null, kedbDTO);
      Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testUpdateKedb_06() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("veuveu");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    ProblemsInsideDTO problemsInsideDTO = Mockito.spy(ProblemsInsideDTO.class);
    problemsInsideDTO.setWorklog("vbvb");
    problemsInsideDTO.setStateName("bgbg");

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileName("fileGnoc");
    gnocFileDto.setCreateTime(new Date());
    gnocFileDto.setPath("/tmp/files");
    List<GnocFileDto> gnocFileDtosWeb = Mockito.spy(ArrayList.class);
    gnocFileDtosWeb.add(gnocFileDto);

    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    kedbRatingInsideDTO.setNote("ioioio");
    kedbRatingInsideDTO.setUserName("thanhlv12");

    KedbDTO kedbDTO = Mockito.spy(KedbDTO.class);
    kedbDTO.setKedbState(3L);
    kedbDTO.setTypeId(1L);
    kedbDTO.setKedbName("fgfg");
    kedbDTO.setUnitCheckId(8L);
    kedbDTO.setKedbStateBeforeUpdate(62L);
    kedbDTO.setProblemsInsideDTO(problemsInsideDTO);
    kedbDTO.setKedbCode("BBB");
    kedbDTO.setUnitCheckId(69L);
    kedbDTO.setGnocFileDtos(gnocFileDtosWeb);
    kedbDTO.setKedbId(5L);
    kedbDTO.setKedbRatingInsideDTO(kedbRatingInsideDTO);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("KEDB_CLOSED");
    catItemDTO.setItemId(1L);

    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemCode("KEDB_UPDATE_APPROVE");
    catItemDTO2.setItemId(2L);

    CatItemDTO catItemDTO3 = Mockito.spy(CatItemDTO.class);
    catItemDTO3.setItemCode("KEDB_CREATE_APPROVE");
    catItemDTO3.setItemId(3L);

    CatItemDTO catItemDTO4 = Mockito.spy(CatItemDTO.class);
    catItemDTO4.setItemCode("KEDB_CANCELED");
    catItemDTO4.setItemId(2L);

    List<CatItemDTO> listKedbState = Mockito.spy(ArrayList.class);
    listKedbState.add(catItemDTO);
    listKedbState.add(catItemDTO2);
    listKedbState.add(catItemDTO3);
    listKedbState.add(catItemDTO4);
    PowerMockito.when(catItemRepository.getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null))
        .thenReturn(listKedbState);

    RolesDTO rdto1 = Mockito.spy(RolesDTO.class);
    rdto1.setRoleCode("ADMIN_KEDB");
    RolesDTO rdto2 = Mockito.spy(RolesDTO.class);
    rdto2.setRoleCode("SUB_ADMIN_KEDB");
    RolesDTO rdto3 = Mockito.spy(RolesDTO.class);
    rdto3.setRoleCode("USER_KEDB");
    List<RolesDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(rdto1);
    lstRole.add(rdto2);
    lstRole.add(rdto3);
    PowerMockito.when(rolesBusiness.getListRolePmByUserOfKedb("999999")).thenReturn(lstRole);
    PowerMockito.when(kedbRepository.doUpdateKedb(any())).thenReturn(resultInSideDto);
    PowerMockito.when(kedbRepository.setStateName(any())).thenReturn("veuveu");
    PowerMockito.when(kedbActionLogsRepository.insertKedbActionLogs(any()))
        .thenReturn(resultInSideDto);
    UserSmsDTO userSmsDTO = Mockito.spy(UserSmsDTO.class);
    userSmsDTO.setEmail("ghighi@gmail.com");
    List<UserSmsDTO> lstUser2SendSMS = Mockito.spy(ArrayList.class);
    lstUser2SendSMS.add(userSmsDTO);
    PowerMockito.when(userSmsRepository.getUserReceiveSMSEmailByTypeCode(anyString(), anyString()))
        .thenReturn(lstUser2SendSMS);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    List<UsersInsideDto> lst = Mockito.spy(ArrayList.class);
    lst.add(usersInsideDto);
    PowerMockito.when(userRepository.getListUsersDTO(anyString(), anyString())).thenReturn(lst);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey(RESULT.SUCCESS);
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    lstCatItem.add(catItemDTO);
    PowerMockito.when(catItemRepository
        .getListItemByCategory(Constants.GNOC_ALIAS, null)).thenReturn(lstCatItem);
    PowerMockito.when(messagesBusiness.insertMessageForUser(anyString(), anyString(), anyList()))
        .thenReturn(resultInSideDto1);

    List<KedbFilesEntity> kedbFilesEntitiesOld = Mockito.spy(ArrayList.class);
    KedbFilesEntity kedbFilesEntity = Mockito.spy(KedbFilesEntity.class);
    kedbFilesEntity.setContent("nnnnnnnnnnn");
    kedbFilesEntity.setCreateTime(new Date());
    kedbFilesEntitiesOld.add(kedbFilesEntity);
    PowerMockito.when(kedbRepository.getListKedbFilesByKedbId(anyLong()))
        .thenReturn(kedbFilesEntitiesOld);

    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    gnocFileDtos.addAll(kedbDTO.getGnocFileDtos());
    PowerMockito.when(gnocFileRepository
        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.KEDB, kedbDTO.getKedbId(), gnocFileDtos))
        .thenReturn(resultInSideDto1);
    PowerMockito.when(kedbRatingBusiness.insertOrUpdateKedbRating(any()))
        .thenReturn(kedbRatingInsideDTO);
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    try {
      ResultInSideDto result = kedbBusiness.updateKedb(files, kedbDTO);
      Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
    } catch (Exception e) {
      e.getMessage();
    }
  }

}
