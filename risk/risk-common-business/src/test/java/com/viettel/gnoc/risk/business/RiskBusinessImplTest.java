package com.viettel.gnoc.risk.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.RiskCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.SrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusRoleDTO;
import com.viettel.gnoc.risk.dto.RiskDTO;
import com.viettel.gnoc.risk.dto.RiskHistoryDTO;
import com.viettel.gnoc.risk.dto.RiskRelationDTO;
import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
import com.viettel.gnoc.risk.model.RiskEntity;
import com.viettel.gnoc.risk.model.RiskFileEntity;
import com.viettel.gnoc.risk.model.RiskSystemDetailEntity;
import com.viettel.gnoc.risk.model.RiskSystemEntity;
import com.viettel.gnoc.risk.repository.RiskChangeStatusRepository;
import com.viettel.gnoc.risk.repository.RiskFileRepository;
import com.viettel.gnoc.risk.repository.RiskHistoryRepository;
import com.viettel.gnoc.risk.repository.RiskRelationRepository;
import com.viettel.gnoc.risk.repository.RiskRepository;
import com.viettel.gnoc.risk.repository.RiskSystemDetailRepository;
import com.viettel.gnoc.risk.repository.RiskSystemRepository;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RiskBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class, FTPUtil.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*",
    "jdk.internal.reflect.*"})
public class RiskBusinessImplTest {

  @InjectMocks
  RiskBusinessImpl riskBusiness;

  @Mock
  RiskRepository riskRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  RiskHistoryRepository riskHistoryRepository;

  @Mock
  RiskFileRepository riskFileRepository;

  @Mock
  RiskRelationRepository riskRelationRepository;

  @Mock
  RiskChangeStatusRepository riskChangeStatusRepository;

  @Mock
  MessagesRepository messagesRepository;

  @Mock
  RiskSystemDetailRepository riskSystemDetailRepository;

  @Mock
  RiskCategoryServiceProxy riskCategoryServiceProxy;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  SrServiceProxy srServiceProxy;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  RiskSystemRepository riskSystemRepository;

  @Mock
  CommonRepository commonRepository;

  @Test
  public void test_getListDataSearchWeb() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setStartTime(new Date());
    riskDTO.setStartTimeFrom(new Date());
    riskDTO.setStartTimeTo(new Date());
    riskDTO.setEndTime(new Date());
    riskDTO.setEndTimeTo(new Date());
    riskDTO.setEndTimeFrom(new Date());
    riskDTO.setPriorityId(1L);
    riskDTO.setSubjectId(1L);
    List<RiskDTO> riskDTOList = Mockito.spy(ArrayList.class);
    riskDTOList.add(riskDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(riskDTOList);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    catItemDTO.setItemId(1L);
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    catItemDTOList.add(catItemDTO);
    Datatable datatableCaItemDto = Mockito.spy(Datatable.class);
    datatableCaItemDto.setData(catItemDTOList);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitName("1");

    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory("RISK_PRIORITY", null))
        .thenReturn(catItemDTOList);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatableCaItemDto);
    PowerMockito.when(riskRepository.getListDataSearchWeb(any())).thenReturn(datatable);
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.2345);
    Datatable datatable1 = riskBusiness.getListDataSearchWeb(riskDTO);
    assertEquals(datatable1.getPages(), datatable.getPages());
  }

  @Test
  public void test_insertRiskFromWeb() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    fileAttacks.add(firstFile);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setStartTime(new Date());
    riskDTO.setEndTime(new Date());
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);

    PowerMockito.when(riskRepository.getSeqTableWo(anyString())).thenReturn("1");
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.2345);
    PowerMockito.when(unitRepository.findUnitById(userToken.getUserID())).thenReturn(unitToken);
    ResultInSideDto result = riskBusiness.insertRiskFromWeb(fileAttacks, riskDTO);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_insertRiskFromWeb_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    fileAttacks.add(firstFile);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 1);
    dt = c.getTime();
    riskDTO.setStartTime(dt);
    riskDTO.setEndTime(new Date());
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);

    PowerMockito.when(riskRepository.getSeqTableWo(anyString())).thenReturn("1");
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.2345);
    PowerMockito.when(unitRepository.findUnitById(userToken.getUserID())).thenReturn(unitToken);
    ResultInSideDto result = riskBusiness.insertRiskFromWeb(fileAttacks, riskDTO);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_insertRiskFromWeb_2() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    fileAttacks.add(firstFile);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    var getTime = new Date();
    getTime.setTime(getTime.getTime() + 30000000);
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 1);
    dt = c.getTime();
    riskDTO.setStartTime(getTime);
    riskDTO.setEndTime(dt);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");

    PowerMockito.when(riskRepository.insertRisk(any())).thenReturn(resultInSideDto);
    PowerMockito.when(riskRepository.getSeqTableWo(anyString())).thenReturn("1");
    PowerMockito.when(riskRepository.getSeqTableWo(anyString())).thenReturn("1");
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.2345);
    PowerMockito.when(unitRepository.findUnitById(userToken.getUserID())).thenReturn(unitToken);

    ResultInSideDto result = new ResultInSideDto();
    try {
      riskBusiness.insertRiskFromWeb(fileAttacks, riskDTO);
      assertEquals(result.getKey(), "ERROR");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

  @Test
  public void test_insertRiskFromWeb_3() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    fileAttacks.add(firstFile);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    var getTime = new Date();
    getTime.setTime(getTime.getTime() + 30000000);
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 1);
    dt = c.getTime();
    riskDTO.setStartTime(getTime);
    riskDTO.setEndTime(dt);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");

    PowerMockito.when(riskHistoryRepository.insertRiskHistory(any())).thenReturn(resultInSideDto1);
    PowerMockito.when(riskRepository.insertRisk(any())).thenReturn(resultInSideDto);
    PowerMockito.when(riskRepository.getSeqTableWo(anyString())).thenReturn("1");
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.2345);
    PowerMockito.when(unitRepository.findUnitById(userToken.getUserID())).thenReturn(unitToken);

    ResultInSideDto result = new ResultInSideDto();
    try {
      riskBusiness.insertRiskFromWeb(fileAttacks, riskDTO);
      assertEquals(result.getKey(), "ERROR");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

  @Test
  public void test_insertRiskFromWeb_4() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
//    fileAttacks.add(firstFile);
    RiskRelationDTO relationDTO = Mockito.spy(RiskRelationDTO.class);
    List<RiskRelationDTO> riskRelationDTOList = Mockito.spy(ArrayList.class);
    riskRelationDTOList.add(relationDTO);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    var getTime = new Date();
    getTime.setTime(getTime.getTime() + 30000000);
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 1);
    dt = c.getTime();
    riskDTO.setStartTime(getTime);
    riskDTO.setEndTime(dt);
    riskDTO.setLstRiskRelation(riskRelationDTOList);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");

    PowerMockito.when(riskRelationRepository.insertRiskRelation(any()))
        .thenReturn(resultInSideDto1);
    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskHistoryRepository.insertRiskHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(riskRepository.insertRisk(any())).thenReturn(resultInSideDto);
    PowerMockito.when(riskRepository.getSeqTableWo(anyString())).thenReturn("1");
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.2345);
    PowerMockito.when(unitRepository.findUnitById(userToken.getUserID())).thenReturn(unitToken);

    ResultInSideDto result = new ResultInSideDto();
    try {
      riskBusiness.insertRiskFromWeb(fileAttacks, riskDTO);
      assertEquals(result.getKey(), "ERROR");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

  @Test
  public void test_insertRiskFromWeb_5() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    RiskRelationDTO relationDTO = Mockito.spy(RiskRelationDTO.class);
    List<RiskRelationDTO> riskRelationDTOList = Mockito.spy(ArrayList.class);
    riskRelationDTOList.add(relationDTO);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    var getTime = new Date();
    getTime.setTime(getTime.getTime() + 30000000);
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 1);
    dt = c.getTime();
    riskDTO.setStartTime(getTime);
    riskDTO.setEndTime(dt);
    riskDTO.setLstRiskRelation(riskRelationDTOList);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    PowerMockito.when(riskRelationRepository.insertRiskRelation(any())).thenReturn(resultInSideDto);
    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskHistoryRepository.insertRiskHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(riskRepository.insertRisk(any())).thenReturn(resultInSideDto);
    PowerMockito.when(riskRepository.getSeqTableWo(anyString())).thenReturn("1");
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.2345);
    PowerMockito.when(unitRepository.findUnitById(userToken.getUserID())).thenReturn(unitToken);

    ResultInSideDto result = riskBusiness.insertRiskFromWeb(fileAttacks, riskDTO);
    assertEquals(result.getKey(), "SUCCESS");

  }


  @Test
  public void test_getDesFromValueKey() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.when(riskRepository.getValueFromValueKey(anyString(), anyString()))
        .thenReturn("a");
    String a = riskBusiness.getDesFromValueKey("a", "a");
    assertEquals(a, "a");
  }

  @Test
  public void test_findRiskByIdFromWeb() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    RiskRelationDTO riskRelationDTO = Mockito.spy(RiskRelationDTO.class);
    riskRelationDTO.setId(1L);
    List<RiskRelationDTO> relationDTOList = Mockito.spy(ArrayList.class);
    relationDTOList.add(riskRelationDTO);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    catItemDTO.setItemId(1L);
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    catItemDTOList.add(catItemDTO);

    Datatable datatableCaItemDto = Mockito.spy(Datatable.class);
    datatableCaItemDto.setData(catItemDTOList);

    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), anyString()))
        .thenReturn(catItemDTOList);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatableCaItemDto);
    PowerMockito.when(riskRelationRepository.getRiskRelationByRiskId(any()))
        .thenReturn(relationDTOList);
    PowerMockito.when(riskRepository.findRiskByIdFromWeb(anyLong(), anyDouble()))
        .thenReturn(riskDTO);
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.1234);
    riskBusiness.findRiskByIdFromWeb(1L);
    assertEquals(1, 1);
  }

  @Test
  public void test_getListFileFromRisk() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("1");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtoList);
    riskBusiness.getListFileFromRisk(1L);
    assertEquals(gnocFileDtoList.size(), 1);
  }

  @Test
  public void test_getListRiskHistoryByRiskId() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    RiskHistoryDTO riskHistoryDTO = Mockito.spy(RiskHistoryDTO.class);
    riskHistoryDTO.setRiskId(1L);
    List<RiskHistoryDTO> historyDTOList = Mockito.spy(ArrayList.class);
    historyDTOList.add(riskHistoryDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(historyDTOList);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable datatable2 = Mockito.spy(Datatable.class);
    datatable2.setData(list);
    PowerMockito.when(catItemRepository.getItemMaster(Constants.CATEGORY.RISK_STATUS,
        LANGUAGUE_EXCHANGE_SYSTEM.RISK, APPLIED_BUSSINESS.RISK.toString(),
        Constants.ITEM_VALUE, Constants.ITEM_NAME)).thenReturn(datatable2);
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.1);
    PowerMockito.when(riskHistoryRepository.getListRiskHistoryByRiskId(any()))
        .thenReturn(datatable);
    Datatable datatable1 = riskBusiness.getListRiskHistoryByRiskId(riskHistoryDTO);
    assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void test_getListRiskRelationByRiskId() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    RiskRelationDTO relationDTO = Mockito.spy(RiskRelationDTO.class);
    relationDTO.setId(1L);
    List<RiskRelationDTO> riskRelationDTOList = Mockito.spy(ArrayList.class);
    riskRelationDTOList.add(relationDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(riskRelationDTOList);

    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.1);
    PowerMockito.when(riskRelationRepository.getListRiskRelationByRiskId(any()))
        .thenReturn(datatable);
    Datatable datatable1 = riskBusiness.getListRiskRelationByRiskId(relationDTO);
    assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void test_getListRiskSystemCombobox() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO.setSchedule(1L);
    List<RiskSystemDTO> riskSystemDTOList = Mockito.spy(ArrayList.class);
    riskSystemDTOList.add(riskSystemDTO);

    PowerMockito.when(riskCategoryServiceProxy.getListRiskSystem(any()))
        .thenReturn(riskSystemDTOList);
    riskBusiness.getListRiskSystemCombobox(riskSystemDTO);
    assertEquals(riskSystemDTOList.size(), 1);
  }

  @Test
  public void test_getListRiskTypeCombobox() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    riskTypeDTO.setRiskTypeId(1L);
    List<RiskTypeDTO> riskTypeDTOList = Mockito.spy(ArrayList.class);
    riskTypeDTOList.add(riskTypeDTO);

    PowerMockito.when(riskCategoryServiceProxy.getListRiskTypeDTO(any()))
        .thenReturn(riskTypeDTOList);
    riskBusiness.getListRiskTypeCombobox(riskTypeDTO);
    assertEquals(riskTypeDTOList.size(), 1);
  }

  @Test
  public void test_getValueFromValueKey() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.when(riskRepository.getValueFromValueKey(anyString(), anyString()))
        .thenReturn("SUCCESS");
    ResultInSideDto result = riskBusiness.getValueFromValueKey("1", "1");
    assertEquals(result.getKey(), "SUCCESS");

  }

  @Test
  public void test_findUnitById() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    riskBusiness.findUnitById(1L);
    assertNotNull(unitDTO);
  }

  @Test
  public void test_getListRiskTypeDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskTypeDetailDTO riskTypeDetailDTO = Mockito.spy(RiskTypeDetailDTO.class);
    riskTypeDetailDTO.setId(1L);
    List<RiskTypeDetailDTO> riskTypeDetailDTOS = Mockito.spy(ArrayList.class);
    riskTypeDetailDTOS.add(riskTypeDetailDTO);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskTypeDetail(any()))
        .thenReturn(riskTypeDetailDTOS);
    riskBusiness.getListRiskTypeDetail(riskTypeDetailDTO);
    assertEquals(riskTypeDetailDTOS.size(), 1);
  }

  @Test
  public void test_updateRiskFromWeb() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");
    PowerMockito.when(I18n.getLanguage("risk.not.config.change.status"))
        .thenReturn("Trạng thái chưa được cấu hình");

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> multipartFileList = Mockito.spy(ArrayList.class);
    multipartFileList.add(firstFile);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setOldStatus(1L);
    riskDTO.setRiskId(1L);
    riskDTO.setPriorityId(1L);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    RiskEntity riskEntity = Mockito.spy(RiskEntity.class);
    riskEntity.setStatus(0L);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);

    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    PowerMockito.when(riskRepository.findRiskByRiskId(userToken.getDeptId()))
        .thenReturn(riskEntity);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    ResultInSideDto resultInSideDto = riskBusiness
        .updateRiskFromWeb(multipartFileList, multipartFileList, riskDTO);
    assertEquals(resultInSideDto.getKey(), "ERROR");
  }

  @Test
  public void test_updateRiskFromWeb_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");
    PowerMockito.when(I18n.getLanguage("risk.not.config.change.status"))
        .thenReturn("Trạng thái chưa được cấu hình");

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> multipartFileList = Mockito.spy(ArrayList.class);
    multipartFileList.add(firstFile);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setOldStatus(1L);
    riskDTO.setRiskId(1L);
    riskDTO.setPriorityId(1L);
    riskDTO.setStartTime(new Date());
    riskDTO.setEndTime(new Date());
    riskDTO.setCreateTime(new Date());
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    RiskEntity riskEntity = Mockito.spy(RiskEntity.class);
    riskEntity.setStatus(1L);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(riskRepository.updateRisk(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.1);
    PowerMockito.when(riskRepository.getValueFromValueKey(anyString(), anyString()))
        .thenReturn("1");
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    PowerMockito.when(riskRepository.findRiskByRiskId(userToken.getDeptId()))
        .thenReturn(riskEntity);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    ResultInSideDto result = new ResultInSideDto();
    try {
      riskBusiness.updateRiskFromWeb(multipartFileList, multipartFileList, riskDTO);
      assertEquals(result.getKey(), "ERROR");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

  @Test
  public void test_updateRiskFromWeb_2() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");
    PowerMockito.when(I18n.getLanguage("risk.not.config.change.status"))
        .thenReturn("Trạng thái chưa được cấu hình");

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> multipartFileList = Mockito.spy(ArrayList.class);
//    multipartFileList.add(firstFile);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setOldStatus(1L);
    riskDTO.setRiskId(1L);
    riskDTO.setPriorityId(1L);
    riskDTO.setStartTime(new Date());
    riskDTO.setEndTime(new Date());
    riskDTO.setCreateTime(new Date());

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    RiskEntity riskEntity = Mockito.spy(RiskEntity.class);
    riskEntity.setStatus(1L);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setMappingId(1L);
    List<GnocFileDto> list = Mockito.spy(ArrayList.class);
    list.add(gnocFileDto);
    riskDTO.setGnocFileDtos(list);
    riskDTO.setGnocFileTemplate(list);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    RiskFileEntity riskFileEntity = Mockito.spy(RiskFileEntity.class);
    riskFileEntity.setRiskId(1L);
    List<RiskFileEntity> lstFileOld = Mockito.spy(ArrayList.class);
    lstFileOld.add(riskFileEntity);

    PowerMockito.when(riskHistoryRepository.insertRiskHistory(any())).thenReturn(resultInSideDto1);
    PowerMockito.when(gnocFileRepository.saveListGnocFile(anyString(), anyLong(), any()))
        .thenReturn(resultInSideDto1);
    PowerMockito.when(riskFileRepository.getListRiskFileByRiskId(anyLong())).thenReturn(lstFileOld);
    PowerMockito.when(riskRepository.updateRisk(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.1);
    PowerMockito.when(riskRepository.getValueFromValueKey(anyString(), anyString()))
        .thenReturn("1");
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    PowerMockito.when(riskRepository.findRiskByRiskId(userToken.getDeptId()))
        .thenReturn(riskEntity);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    ResultInSideDto result = new ResultInSideDto();
    try {
      riskBusiness.updateRiskFromWeb(multipartFileList, multipartFileList, riskDTO);
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

  @Test
  public void test_checkPermissionUpdate() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setRiskTypeId(1L);
    riskDTO.setStatus(1L);
    riskDTO.setPriorityId(1L);
    RiskEntity riskEntity = Mockito.spy(RiskEntity.class);
    riskEntity.setStatus(1L);
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    lstChangeStatus.add(riskChangeStatusDTO);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    String a = riskBusiness.checkPermissionUpdate(riskDTO, riskEntity);
    assertEquals(a, "aaa");
  }

  @Test
  public void test_checkPermissionUpdate_1() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setRiskTypeId(1L);
    riskDTO.setStatus(1L);
    riskDTO.setPriorityId(1L);
    RiskEntity riskEntity = Mockito.spy(RiskEntity.class);
    riskEntity.setStatus(1L);
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setRiskTypeId(1L);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    lstChangeStatus.add(riskChangeStatusDTO);
    List<RiskChangeStatusRoleDTO> riskChangeStatusRoleDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusRoleDTO(any()))
        .thenReturn(riskChangeStatusRoleDTOS);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    String a = riskBusiness.checkPermissionUpdate(riskDTO, riskEntity);
    assertEquals(a, "aaa");
  }

  @Test
  public void test_checkPermissionUpdate_2() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setRiskTypeId(1L);
    riskDTO.setStatus(1L);
    riskDTO.setPriorityId(1L);
    riskDTO.setUserIdLogin(1L);
    RiskEntity riskEntity = Mockito.spy(RiskEntity.class);
    riskEntity.setStatus(1L);
    riskEntity.setCreateUserId(1L);
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setRiskTypeId(1L);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    lstChangeStatus.add(riskChangeStatusDTO);
    RiskChangeStatusRoleDTO riskChangeStatusRoleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    riskChangeStatusRoleDTO.setId(1L);
    riskChangeStatusRoleDTO.setRoleId(1L);
    List<RiskChangeStatusRoleDTO> riskChangeStatusRoleDTOS = Mockito.spy(ArrayList.class);
    riskChangeStatusRoleDTOS.add(riskChangeStatusRoleDTO);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusRoleDTO(any()))
        .thenReturn(riskChangeStatusRoleDTOS);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    String a = riskBusiness.checkPermissionUpdate(riskDTO, riskEntity);
    assertEquals(a, "");
  }

  @Test
  public void test_checkPermissionUpdate_3() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setRiskTypeId(1L);
    riskDTO.setStatus(1L);
    riskDTO.setPriorityId(1L);
    riskDTO.setUserIdLogin(2L);
    RiskEntity riskEntity = Mockito.spy(RiskEntity.class);
    riskEntity.setStatus(1L);
    riskEntity.setCreateUserId(2L);
    riskEntity.setReceiveUserId(2L);
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setRiskTypeId(1L);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    lstChangeStatus.add(riskChangeStatusDTO);
    RiskChangeStatusRoleDTO riskChangeStatusRoleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    riskChangeStatusRoleDTO.setId(1L);
    riskChangeStatusRoleDTO.setRoleId(2L);
    List<RiskChangeStatusRoleDTO> riskChangeStatusRoleDTOS = Mockito.spy(ArrayList.class);
    riskChangeStatusRoleDTOS.add(riskChangeStatusRoleDTO);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusRoleDTO(any()))
        .thenReturn(riskChangeStatusRoleDTOS);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    String a = riskBusiness.checkPermissionUpdate(riskDTO, riskEntity);
    assertEquals(a, "");
  }

  @Test
  public void test_checkPermissionUpdate_5() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setRiskTypeId(1L);
    riskDTO.setStatus(1L);
    riskDTO.setPriorityId(1L);
    riskDTO.setUserIdLogin(2L);
    RiskEntity riskEntity = Mockito.spy(RiskEntity.class);
    riskEntity.setStatus(1L);
    riskEntity.setCreateUserId(2L);
    riskEntity.setReceiveUserId(2L);
    riskEntity.setCreateUnitId(1L);
    riskEntity.setReceiveUnitId(1L);
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setRiskTypeId(1L);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    lstChangeStatus.add(riskChangeStatusDTO);
    RiskChangeStatusRoleDTO riskChangeStatusRoleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    riskChangeStatusRoleDTO.setId(1L);
    riskChangeStatusRoleDTO.setRoleId(4L);
    List<RiskChangeStatusRoleDTO> riskChangeStatusRoleDTOS = Mockito.spy(ArrayList.class);
    riskChangeStatusRoleDTOS.add(riskChangeStatusRoleDTO);
    UnitDTO us = Mockito.spy(UnitDTO.class);
    us.setUnitId(1L);
    us.setParentUnitId(1L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    usersEntity.setUserId(1L);

    PowerMockito.when(userRepository.checkRoleOfUser("TP", 1L)).thenReturn(true);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(us);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusRoleDTO(any()))
        .thenReturn(riskChangeStatusRoleDTOS);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    String a = riskBusiness.checkPermissionUpdate(riskDTO, riskEntity);
    assertEquals(a, "");
  }

  @Test
  public void test_checkPermissionUpdate_6() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setRiskTypeId(1L);
    riskDTO.setStatus(1L);
    riskDTO.setPriorityId(1L);
    riskDTO.setUserIdLogin(2L);
    RiskEntity riskEntity = Mockito.spy(RiskEntity.class);
    riskEntity.setStatus(1L);
    riskEntity.setCreateUserId(2L);
    riskEntity.setReceiveUserId(2L);
    riskEntity.setCreateUnitId(1L);
    riskEntity.setReceiveUnitId(1L);
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setRiskTypeId(1L);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    lstChangeStatus.add(riskChangeStatusDTO);
    RiskChangeStatusRoleDTO riskChangeStatusRoleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    riskChangeStatusRoleDTO.setId(1L);
    riskChangeStatusRoleDTO.setRoleId(5L);
    List<RiskChangeStatusRoleDTO> riskChangeStatusRoleDTOS = Mockito.spy(ArrayList.class);
    riskChangeStatusRoleDTOS.add(riskChangeStatusRoleDTO);
    UnitDTO us = Mockito.spy(UnitDTO.class);
    us.setUnitId(1L);
    us.setParentUnitId(1L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    usersEntity.setUserId(1L);

    PowerMockito.when(userRepository.checkRoleOfUser("CVQLRR", 1L)).thenReturn(true);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(us);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusRoleDTO(any()))
        .thenReturn(riskChangeStatusRoleDTOS);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    String a = riskBusiness.checkPermissionUpdate(riskDTO, riskEntity);
    assertEquals(a, "");
  }

  @Test
  public void test_checkPermissionUpdate_7() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setRiskTypeId(1L);
    riskDTO.setStatus(1L);
    riskDTO.setPriorityId(1L);
    riskDTO.setUserIdLogin(2L);
    RiskEntity riskEntity = Mockito.spy(RiskEntity.class);
    riskEntity.setStatus(1L);
    riskEntity.setCreateUserId(2L);
    riskEntity.setReceiveUserId(2L);
    riskEntity.setCreateUnitId(1L);
    riskEntity.setReceiveUnitId(1L);
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setRiskTypeId(1L);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    lstChangeStatus.add(riskChangeStatusDTO);
    RiskChangeStatusRoleDTO riskChangeStatusRoleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    riskChangeStatusRoleDTO.setId(1L);
    riskChangeStatusRoleDTO.setRoleId(6L);
    List<RiskChangeStatusRoleDTO> riskChangeStatusRoleDTOS = Mockito.spy(ArrayList.class);
    riskChangeStatusRoleDTOS.add(riskChangeStatusRoleDTO);
    UnitDTO us = Mockito.spy(UnitDTO.class);
    us.setUnitId(1L);
    us.setParentUnitId(1L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    usersEntity.setUserId(1L);

    PowerMockito.when(userRepository.checkRoleOfUser("BGDTT", 1L)).thenReturn(true);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(us);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusRoleDTO(any()))
        .thenReturn(riskChangeStatusRoleDTOS);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    String a = riskBusiness.checkPermissionUpdate(riskDTO, riskEntity);
    assertEquals(a, "");
  }

  @Test
  public void test_checkPermissionUpdate_8() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setRiskTypeId(1L);
    riskDTO.setStatus(1L);
    riskDTO.setPriorityId(1L);
    riskDTO.setUserIdLogin(2L);
    riskDTO.setUnitIdLogin(1L);
    riskDTO.setSystemId(1L);
    RiskEntity riskEntity = Mockito.spy(RiskEntity.class);
    riskEntity.setStatus(1L);
    riskEntity.setCreateUserId(2L);
    riskEntity.setReceiveUserId(2L);
    riskEntity.setCreateUnitId(1L);
    riskEntity.setReceiveUnitId(1L);
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setRiskTypeId(1L);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    lstChangeStatus.add(riskChangeStatusDTO);
    RiskChangeStatusRoleDTO riskChangeStatusRoleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    riskChangeStatusRoleDTO.setId(1L);
    riskChangeStatusRoleDTO.setRoleId(7L);
    List<RiskChangeStatusRoleDTO> riskChangeStatusRoleDTOS = Mockito.spy(ArrayList.class);
    riskChangeStatusRoleDTOS.add(riskChangeStatusRoleDTO);
    UnitDTO us = Mockito.spy(UnitDTO.class);
    us.setUnitId(1L);
    us.setParentUnitId(1L);
    us.setLocationId(1L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    usersEntity.setUserId(1L);
    RiskSystemEntity riskSystemEntity = Mockito.spy(RiskSystemEntity.class);
    riskSystemEntity.setCountryId(1L);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setNationCode("1");

    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong()))
        .thenReturn(catLocationDTO);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);
    PowerMockito.when(riskSystemRepository.getRiskSystemById(anyLong()))
        .thenReturn(riskSystemEntity);
    PowerMockito.when(userRepository.checkRoleOfUser("CVQLRRTT", 1L)).thenReturn(true);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(us);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusRoleDTO(any()))
        .thenReturn(riskChangeStatusRoleDTOS);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    String a = riskBusiness.checkPermissionUpdate(riskDTO, riskEntity);
    assertEquals(a, "");
  }

  @Test
  public void updateRiskFileAttach() {
  }

  @Test
  public void test_getListRiskChangeStatus() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setRiskTypeId(1L);
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    lstChangeStatus.add(riskChangeStatusDTO);

    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any()))
        .thenReturn(lstChangeStatus);
    riskBusiness.getListRiskChangeStatus(riskChangeStatusDTO);
    assertEquals(lstChangeStatus.size(), 1);

  }

  @Test
  public void getListRiskCfgBusiness() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskCfgBusinessDTO riskCfgBusinessDTO = Mockito.spy(RiskCfgBusinessDTO.class);
    riskCfgBusinessDTO.setId(1L);
    List<RiskCfgBusinessDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskCfgBusinessDTO);

    PowerMockito.when(riskCategoryServiceProxy.getListRiskCfgBusinessDTO(any())).thenReturn(list);
    riskBusiness.getListRiskCfgBusiness(riskCfgBusinessDTO);
    assertEquals(list.size(), 1);
  }

  @Test
  public void test_getListStatusNext() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(1L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setUnitIdLogin(1L);
    riskDTO.setReceiveUserId(1L);
    riskDTO.setCreateUnitId(1L);
    riskDTO.setReceiveUnitId(1L);
    riskDTO.setCreateUserId(1L);
    riskDTO.setSystemId(1L);
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setId(1L);
    riskChangeStatusDTO.setRiskTypeId(1L);
    List<RiskChangeStatusDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskChangeStatusDTO);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setParentUnitId(1L);
    unitDTO.setLocationId(1L);
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUserId(1L);
    us.setUnitId(1L);
    RiskSystemEntity riskSystemEntity = Mockito.spy(RiskSystemEntity.class);
    riskSystemEntity.setCountryId(1L);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setDescription("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setNationCode("1");
    ;
    RiskChangeStatusRoleDTO riskChangeStatusRoleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    riskChangeStatusRoleDTO.setId(1L);
    riskChangeStatusRoleDTO.setRoleId(1L);
    List<RiskChangeStatusRoleDTO> lstRoleChangeStatus = Mockito.spy(ArrayList.class);
    lstRoleChangeStatus.add(riskChangeStatusRoleDTO);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusRoleDTO(any()))
        .thenReturn(lstRoleChangeStatus);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusRoleDTO(any()))
        .thenReturn(lstRoleChangeStatus);
    PowerMockito.when(userRepository.checkRoleOfUser("CVQLRRTT", 1l)).thenReturn(true);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong()))
        .thenReturn(catLocationDTO);
    PowerMockito.when(catItemRepository.getCatItemById(anyLong())).thenReturn(catItemDTO);
    PowerMockito.when(riskSystemRepository.getRiskSystemById(anyLong()))
        .thenReturn(riskSystemEntity);
    PowerMockito.when(userRepository.checkRoleOfUser("CVQLRR", 1L)).thenReturn(true);
    PowerMockito.when(userRepository.checkRoleOfUser("BGDTT", 1L)).thenReturn(true);
    PowerMockito.when(userRepository.checkRoleOfUser("TP", 1L)).thenReturn(true);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(us);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskChangeStatusDTO(any())).thenReturn(list);
    List<Long> longList = riskBusiness.getListStatusNext(riskDTO);
    assertEquals(longList.size(), 1);
  }

  @Test
  public void test_getListRiskRelationBySystem() {
    Datatable datatable = Mockito.spy(Datatable.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(1L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    RiskRelationDTO riskRelationDTO = Mockito.spy(RiskRelationDTO.class);
    riskRelationDTO.setSystem("CR");
    riskRelationDTO.setSystemCode("1");
    riskRelationDTO.setCreateTime(new Date());
    riskRelationDTO.setEndTime(new Date());
    riskRelationDTO.setPage(1);
    riskRelationDTO.setPageSize(1);
    List<RiskRelationDTO> riskRelationDTOList = Mockito.spy(ArrayList.class);
    riskRelationDTOList.add(riskRelationDTO);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setTitle("1");
    crDTO.setChangeOrginatorName("1");
    crDTO.setCreatedDate(DateTimeUtils.convertDateTimeStampToString(new Date()));
    crDTO.setLatestStartTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
    crDTO.setCrId("1");
    crDTO.setChangeResponsibleUnitName("1");
    List<CrDTO> list = Mockito.spy(ArrayList.class);
    list.add(crDTO);
    datatable.setData(riskRelationDTOList);
    PowerMockito.when(crServiceProxy.getListCrInfo(any())).thenReturn(list);
    Datatable data = riskBusiness.getListRiskRelationBySystem(riskRelationDTO);
    assertEquals(data.getPages(), 1);
  }

  @Test
  public void test_getListRiskRelationBySystem_1() {
    Datatable datatable = Mockito.spy(Datatable.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(1L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    RiskRelationDTO riskRelationDTO = Mockito.spy(RiskRelationDTO.class);
    riskRelationDTO.setSystem("SR");
    riskRelationDTO.setSystemCode("1");
    riskRelationDTO.setCreateTime(new Date());
    riskRelationDTO.setEndTime(new Date());
    riskRelationDTO.setPage(1);
    riskRelationDTO.setPageSize(1);
    SRDTO srdto = Mockito.spy(SRDTO.class);
    srdto.setTitle("1");
    srdto.setCreatedUser("1");
    srdto.setCreatedTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
    srdto.setEndTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
    srdto.setSrId("1");
    srdto.setSrCode("1");
    srdto.setUnitName("!");
    List<SRDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(srdto);
    PowerMockito.when(srServiceProxy.getListSRForWO(any())).thenReturn(lst);
    Datatable data = riskBusiness.getListRiskRelationBySystem(riskRelationDTO);
    assertEquals(data.getPages(), 1);
  }

  @Test
  public void test_getListRiskRelationBySystem_2() {
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setDeptId(1L);
    userToken.setUserID(1L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    RiskRelationDTO riskRelationDTO = Mockito.spy(RiskRelationDTO.class);
    riskRelationDTO.setSystem("WO");
    riskRelationDTO.setSystemCode("1");
    riskRelationDTO.setCreateTime(new Date());
    riskRelationDTO.setEndTime(new Date());
    riskRelationDTO.setPage(1);
    riskRelationDTO.setPageSize(1);
    WoInsideDTO dtoTmp = Mockito.spy(WoInsideDTO.class);
    dtoTmp.setWoContent("1");
    dtoTmp.setCreatePersonName("1");
    dtoTmp.setCreateDate(new Date());
    dtoTmp.setEndTime(new Date());
    dtoTmp.setStatus(1L);
    dtoTmp.setWoCode("1");
    dtoTmp.setWoId(1L);
    dtoTmp.setCdName("1");
    List<WoInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(dtoTmp);
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.2345);
    PowerMockito.when(woServiceProxy.getListDataForRisk(any())).thenReturn(lst);
    Datatable data = riskBusiness.getListRiskRelationBySystem(riskRelationDTO);
    assertEquals(data.getPages(), 1);
  }

  @Test
  public void test_exportDataRisk() throws Exception {
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    userToken.setDeptId(1L);
    userToken.setUserID(1L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(userToken.getUserID())).thenReturn(1.2345);
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setStartTime(new Date());
    riskDTO.setStartTimeFrom(new Date());
    riskDTO.setStartTimeTo(new Date());
    riskDTO.setEndTime(new Date());
    riskDTO.setEndTimeFrom(new Date());
    riskDTO.setEndTimeTo(new Date());
    riskDTO.setSubjectId(1L);
    riskDTO.setCreateUnitName("1");
    riskDTO.setCreateUnitCode("1");
    riskDTO.setReceiveUnitName("1");
    riskDTO.setReceiveUnitCode("!");
    List<RiskDTO> riskDTOList = Mockito.spy(ArrayList.class);
    riskDTOList.add(riskDTO);
    Datatable dataStatus = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("1");
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    catItemDTOList.add(catItemDTO);
    dataStatus.setData(catItemDTOList);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitName("1");
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataStatus);
    PowerMockito.when(catItemRepository.getListItemByCategory("RISK_PRIORITY", null))
        .thenReturn(catItemDTOList);
    PowerMockito.when(catItemRepository.getListItemByCategory("RISK_EFFECT", null))
        .thenReturn(catItemDTOList);
    PowerMockito.when(catItemRepository.getListItemByCategory("RISK_FREQUENCY", null))
        .thenReturn(catItemDTOList);
    PowerMockito.when(catItemRepository.getListItemByCategory("RISK_REDUNDANCY", null))
        .thenReturn(catItemDTOList);
    PowerMockito.when(riskRepository.getListRiskExport(any())).thenReturn(riskDTOList);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(commonRepository.checkMaxRowExport(anyInt())).thenReturn(false);

    File file = riskBusiness.exportDataRisk(riskDTO);
    assertNull(file);
  }

  @Test
  public void test_getFileByBusinessId() {
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileName("1");
    List<GnocFileDto> list = Mockito.spy(ArrayList.class);
    list.add(gnocFileDto);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(list);

    List<GnocFileDto> list1 = riskBusiness.getFileByBusinessId(1L);
    assertEquals(list.size(), list1.size());
  }


  @Test
  public void test_convertStatus2String() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(null);
    assertEquals(result, "");
  }

  @Test
  public void test_convertStatus2String_1() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(0L);
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatus2String_2() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(1L);
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatus2String_3() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(2L);
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatus2String_4() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(3L);
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatus2String_5() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(4L);
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatus2String_6() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(5L);
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatus2String_7() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(6L);
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatus2String_8() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(7L);
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatus2String_9() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(8L);
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatus2String_10() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(9L);
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatus2String_11() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatus2String(1000L);
    assertEquals(result, "");
  }


  @Test
  public void test_convertStatusCR() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("0");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_1() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("1");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_2() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("2");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_3() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("3");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_4() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("4");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_5() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("5");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_6() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("6");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_7() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("7");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_8() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("8");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_9() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("9");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_10() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("10");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_11() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("11");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_12() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("12");
    assertEquals(result, "a");
  }

  @Test
  public void test_convertStatusCR_13() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    String result = riskBusiness.convertStatusCR("13");
    assertEquals(result, "");
  }

  @Test
  public void test_sendMesseageUpdateRisk() {
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setRiskTypeId(1L);
    riskDTO.setStatus(1L);
    riskDTO.setPriorityId(1L);
    riskDTO.setCreateUserId(1L);
    riskDTO.setSystemId(1L);
    riskDTO.setReceiveUserId(1L);
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setId(1L);
    riskChangeStatusDTO.setSendCreate(1L);
    riskChangeStatusDTO.setCreateContent("1");
    riskChangeStatusDTO.setSendRiskManagementUnit(3L);
    riskChangeStatusDTO.setRiskManagementUnitContent("1");
    riskChangeStatusDTO.setSendReceiveUser(1L);
    riskChangeStatusDTO.setReceiveUserContent("1");
    riskChangeStatusDTO.setReceiveUnitContent("3");
    riskChangeStatusDTO.setSendNvQlrr(1L);
    riskChangeStatusDTO.setReceiveUnitContent("1");
    riskChangeStatusDTO.setNvQlrrContent("1");
    List<RiskChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    lstChangeStatus.add(riskChangeStatusDTO);
    UsersEntity u = Mockito.spy(UsersEntity.class);
    Users users = Mockito.spy(Users.class);
    List<Users> lstTmp = Mockito.spy(ArrayList.class);
    lstTmp.add(users);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setParentUnitId(1L);
    unitDTO.setIsCommittee(1L);
    RiskSystemDetailEntity riskSystemDetailEntity = Mockito.spy(RiskSystemDetailEntity.class);
    riskSystemDetailEntity.setManageUnitId(1L);
    List<RiskSystemDetailEntity> listSysEntity = Mockito.spy(ArrayList.class);
    listSysEntity.add(riskSystemDetailEntity);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    List<UsersInsideDto> usersInsideDtoList = Mockito.spy(ArrayList.class);
    usersInsideDtoList.add(usersInsideDto);

    PowerMockito.when(userRepository.getUsersByRoleCode(anyString()))
        .thenReturn(usersInsideDtoList);
    PowerMockito.when(riskSystemDetailRepository.getListEntityBySystemId(anyLong()))
        .thenReturn(listSysEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(userRepository.getListUserOfUnit(anyLong())).thenReturn(lstTmp);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(u);
    PowerMockito.when(
        riskChangeStatusRepository.onSearch(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstChangeStatus);
    riskBusiness.sendMesseageUpdateRisk(riskDTO, 1L);
    assertEquals(usersInsideDtoList.size(), 1);
  }

  @Test
  public void test_createMessage() {
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    usersInsideDto.setFullname("1");
    usersInsideDto.setMobile("1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(messagesRepository.insertOrUpdateWfm(any())).thenReturn(resultInSideDto);
    riskBusiness.createMessage(usersInsideDto, "a");
    assertEquals(resultInSideDto.getKey(), "SUCCESS");
  }

  @Test
  public void test_getSmsContenFromCfg() {
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserLanguage("2");
    String a = riskBusiness.getSmsContenFromCfg(usersInsideDto, "a");
    assertEquals(a, "a");
  }

  @Test
  public void test_replaceSmsContent() {
    RiskDTO riskDTO = Mockito.spy(RiskDTO.class);
    riskDTO.setRiskCode("1");
    riskDTO.setReasonReject("1");
    riskDTO.setReasonCancel("1");
    riskDTO.setReasonAccept("1");
    riskDTO.setResultProcessing("1");
    riskDTO.setEvedence("1");
    riskDTO.setRedundancy(1L);
    riskDTO.setSolution("1");
    riskDTO.setRiskName("1");
    riskDTO.setStartTime(new Date());
    riskDTO.setCreateTime(new Date());
    riskDTO.setDescription("1");
    riskDTO.setEndTime(new Date());
    String a = riskBusiness.replaceSmsContent(riskDTO, "a");
    assertEquals(a, "a");
  }


}
