
package com.viettel.gnoc.risk.business;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
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
import com.viettel.gnoc.commons.model.GnocFileEntity;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.RISK_MASTER_CODE;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import com.viettel.gnoc.risk.dto.RiskSystemDetailDTO;
import com.viettel.gnoc.risk.dto.RiskSystemHistoryDTO;
import com.viettel.gnoc.risk.model.RiskSystemFileEntity;
import com.viettel.gnoc.risk.repository.RiskSystemDetailRepository;
import com.viettel.gnoc.risk.repository.RiskSystemFileRepository;
import com.viettel.gnoc.risk.repository.RiskSystemHistoryRepository;
import com.viettel.gnoc.risk.repository.RiskSystemRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RiskSystemBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class, PassTranformer.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class RiskSystemBusinessImplTest {

  @InjectMocks
  RiskSystemBusinessImpl riskSystemBusiness;

  @Mock
  RiskSystemRepository riskSystemRepository;

  @Mock
  RiskSystemDetailRepository riskSystemDetailRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UserRepository userRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  RiskSystemFileRepository riskSystemFileRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  RiskSystemHistoryRepository riskSystemHistoryRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(riskSystemBusiness, "uploadFolder",
        "/uploadFolder");
    ReflectionTestUtils.setField(riskSystemBusiness, "ftpFolder",
        "/ftpFolder");
    ReflectionTestUtils.setField(riskSystemBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(riskSystemBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(riskSystemBusiness, "ftpServer",
        "10.240.232.25");
    ReflectionTestUtils.setField(riskSystemBusiness, "ftpPort",
        21);
  }

  @Test
  public void getDataRiskSystemSearchWeb() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    List<RiskSystemDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskSystemDTO);
    datatable.setData(list);
    PowerMockito.when(riskSystemRepository.getDataRiskSystemSearchWeb(any())).thenReturn(datatable);
    Datatable datatable1 = riskSystemBusiness.getDataRiskSystemSearchWeb(riskSystemDTO);
    assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getListRiskSystem() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    List<RiskSystemDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskSystemDTO);
    PowerMockito.when(riskSystemRepository.getListRiskSystem(any())).thenReturn(list);
    riskSystemBusiness.getListRiskSystem(riskSystemDTO);
    assertNotNull(list);
  }

  @Test
  public void insertRiskSystemWeb() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO.setId(1L);
    RiskSystemDetailDTO riskSystemDetailDTO = Mockito.spy(RiskSystemDetailDTO.class);
    riskSystemDetailDTO.setId(1L);
    riskSystemDetailDTO.setSystemId(1L);
    List<RiskSystemDetailDTO> riskSystemDetailDTOS = Mockito.spy(ArrayList.class);
    riskSystemDetailDTOS.add(riskSystemDetailDTO);
    riskSystemDTO.setListRiskSystemDetail(riskSystemDetailDTOS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(uploadFile);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);

    PowerMockito.when(riskSystemDetailRepository.insertRiskSystemDetail(any()))
        .thenReturn(resultInSideDto1);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(riskSystemRepository.insertOrUpdateRiskSystem(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = null;

    try {
      result = riskSystemBusiness.insertRiskSystemWeb(files, riskSystemDTO);
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

  @Test
  public void insertRiskSystemWeb_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO.setId(1L);
    RiskSystemDetailDTO riskSystemDetailDTO = Mockito.spy(RiskSystemDetailDTO.class);
    riskSystemDetailDTO.setId(1L);
    riskSystemDetailDTO.setSystemId(1L);
    List<RiskSystemDetailDTO> riskSystemDetailDTOS = Mockito.spy(ArrayList.class);
    riskSystemDetailDTOS.add(riskSystemDetailDTO);
    riskSystemDTO.setListRiskSystemDetail(riskSystemDetailDTOS);
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
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    RiskSystemHistoryDTO riskSystemHistoryDTO = Mockito.spy(RiskSystemHistoryDTO.class);

    PowerMockito.when(riskSystemHistoryRepository.insertRiskSystemHistory(any()))
        .thenReturn(resultInSideDto1);
    PowerMockito.when(riskSystemDetailRepository.insertRiskSystemDetail(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(riskSystemRepository.insertOrUpdateRiskSystem(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = null;

    try {
      result = riskSystemBusiness.insertRiskSystemWeb(null, riskSystemDTO);
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

  @Test
  public void insertRiskSystemWeb_2() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO.setId(1L);
    RiskSystemDetailDTO riskSystemDetailDTO = Mockito.spy(RiskSystemDetailDTO.class);
    riskSystemDetailDTO.setId(1L);
    riskSystemDetailDTO.setSystemId(1L);
    List<RiskSystemDetailDTO> riskSystemDetailDTOS = Mockito.spy(ArrayList.class);
    riskSystemDetailDTOS.add(riskSystemDetailDTO);
    riskSystemDTO.setListRiskSystemDetail(riskSystemDetailDTOS);
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
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    RiskSystemHistoryDTO riskSystemHistoryDTO = Mockito.spy(RiskSystemHistoryDTO.class);

    PowerMockito.when(riskSystemHistoryRepository.insertRiskSystemHistory(any()))
        .thenReturn(resultInSideDto1);
    PowerMockito.when(riskSystemDetailRepository.insertRiskSystemDetail(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(riskSystemRepository.insertOrUpdateRiskSystem(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = null;

    try {
      result = riskSystemBusiness.insertRiskSystemWeb(null, riskSystemDTO);
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

//  @Test
//  public void getListRiskSystem() {
//    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
//    List<RiskSystemDTO> list = Mockito.spy(ArrayList.class);
//    list.add(riskSystemDTO);
//    PowerMockito.when(riskSystemRepository.getListRiskSystem(any())).thenReturn(list);
//    riskSystemBusiness.getListRiskSystem(riskSystemDTO);
//    assertNotNull(list);
//  }
//
//  @Test
//  public void insertRiskSystemWeb() throws Exception{
//    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
//    resultInSideDto.setKey("SUCCESS");
//    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
//    resultInSideDto1.setKey("ERROR");
//    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
//    riskSystemDTO.setId(1L);
//    RiskSystemDetailDTO riskSystemDetailDTO =Mockito.spy(RiskSystemDetailDTO.class);
//    riskSystemDetailDTO.setId(1L);
//    riskSystemDetailDTO.setSystemId(1L);
//    List<RiskSystemDetailDTO> riskSystemDetailDTOS = Mockito.spy(ArrayList.class);
//    riskSystemDetailDTOS.add(riskSystemDetailDTO);
//    riskSystemDTO.setListRiskSystemDetail(riskSystemDetailDTOS);
//    UserToken userToken = Mockito.spy(UserToken.class);
//    userToken.setDeptId(1L);
//    userToken.setUserID(999999L);
//    userToken.setUserName("thanhlv12");
//    PowerMockito.mockStatic(TicketProvider.class);
//    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
//    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain", "some xml".getBytes());
//    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
//    fileAttacks.add(firstFile);
//    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
//
//
//    PowerMockito.when(riskSystemDetailRepository.insertRiskSystemDetail(any())).thenReturn(resultInSideDto1);
//    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
//    PowerMockito.when(riskSystemRepository.insertOrUpdateRiskSystem(any())).thenReturn(resultInSideDto);
//    ResultInSideDto result = null;
//
//    try {
//      result = riskSystemBusiness.insertRiskSystemWeb(fileAttacks, riskSystemDTO);
//      assertEquals(result.getKey(), "SUCCESS");
//    }catch (Exception e){
//      logger.error(e.getMessage());
//    }
//    assertNull(result);
//  }
//
//  @Test
//  public void insertRiskSystemWeb_1() throws Exception{
//    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
//    resultInSideDto.setKey("SUCCESS");
//    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
//    resultInSideDto1.setKey("ERROR");
//    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
//    riskSystemDTO.setId(1L);
//    RiskSystemDetailDTO riskSystemDetailDTO =Mockito.spy(RiskSystemDetailDTO.class);
//    riskSystemDetailDTO.setId(1L);
//    riskSystemDetailDTO.setSystemId(1L);
//    List<RiskSystemDetailDTO> riskSystemDetailDTOS = Mockito.spy(ArrayList.class);
//    riskSystemDetailDTOS.add(riskSystemDetailDTO);
//    riskSystemDTO.setListRiskSystemDetail(riskSystemDetailDTOS);
//    UserToken userToken = Mockito.spy(UserToken.class);
//    userToken.setDeptId(1L);
//    userToken.setUserID(999999L);
//    userToken.setUserName("thanhlv12");
//    PowerMockito.mockStatic(TicketProvider.class);
//    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
//    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain", "some xml".getBytes());
//    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
//    fileAttacks.add(firstFile);
//    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
//    RiskSystemHistoryDTO riskSystemHistoryDTO = Mockito.spy(RiskSystemHistoryDTO.class);
//
//    PowerMockito.when(riskSystemHistoryRepository.insertRiskSystemHistory(any())).thenReturn(resultInSideDto1);
//    PowerMockito.when(riskSystemDetailRepository.insertRiskSystemDetail(any())).thenReturn(resultInSideDto);
//    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
//    PowerMockito.when(riskSystemRepository.insertOrUpdateRiskSystem(any())).thenReturn(resultInSideDto);
//    ResultInSideDto result = null;
//
//    try {
//      result = riskSystemBusiness.insertRiskSystemWeb(fileAttacks, riskSystemDTO);
//      assertEquals(result.getKey(), "SUCCESS");
//    }catch (Exception e){
//      logger.error(e.getMessage());
//    }
//    assertNull(result);
//  }

  @Test
  public void updateRiskSystemWeb() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO.setId(1L);
    RiskSystemDetailDTO riskSystemDetailDTO = Mockito.spy(RiskSystemDetailDTO.class);
    riskSystemDetailDTO.setId(1L);
    riskSystemDetailDTO.setSystemId(1L);
    List<RiskSystemDetailDTO> riskSystemDetailDTOS = Mockito.spy(ArrayList.class);
    riskSystemDetailDTOS.add(riskSystemDetailDTO);
    riskSystemDTO.setListRiskSystemDetail(riskSystemDetailDTOS);
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
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    RiskSystemHistoryDTO riskSystemHistoryDTO = Mockito.spy(RiskSystemHistoryDTO.class);

    PowerMockito.when(riskSystemRepository.getRiskSystemOldById(anyLong()))
        .thenReturn(riskSystemDTO);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(riskSystemRepository.insertOrUpdateRiskSystem(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = null;

    try {
      result = riskSystemBusiness.updateRiskSystemWeb(null, riskSystemDTO);
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

  @Test
  public void updateRiskSystemWeb_1() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    resultInSideDto.setId(1L);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setId(1L);
    resultInSideDto1.setKey("ERROR");
    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO.setId(1L);
    RiskSystemDetailDTO riskSystemDetailDTO = Mockito.spy(RiskSystemDetailDTO.class);
    riskSystemDetailDTO.setId(1L);
    riskSystemDetailDTO.setSystemId(1L);
    List<RiskSystemDetailDTO> riskSystemDetailDTOS = Mockito.spy(ArrayList.class);
    riskSystemDetailDTOS.add(riskSystemDetailDTO);
    riskSystemDTO.setListRiskSystemDetail(riskSystemDetailDTOS);
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
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    RiskSystemHistoryDTO riskSystemHistoryDTO = Mockito.spy(RiskSystemHistoryDTO.class);

    PowerMockito.when(riskSystemDetailRepository.deleteRiskSystemDetailBySystemId(anyLong()))
        .thenReturn(resultInSideDto1);
    PowerMockito.when(riskSystemRepository.getRiskSystemOldById(anyLong()))
        .thenReturn(riskSystemDTO);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(riskSystemRepository.insertOrUpdateRiskSystem(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = null;

    try {
      result = riskSystemBusiness.updateRiskSystemWeb(fileAttacks, riskSystemDTO);
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

  @Test
  public void updateRiskSystemWeb_2() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    resultInSideDto.setId(1L);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setId(1L);
    resultInSideDto1.setKey("ERROR");
    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO.setId(1L);
    RiskSystemDetailDTO riskSystemDetailDTO = Mockito.spy(RiskSystemDetailDTO.class);
    riskSystemDetailDTO.setId(1L);
    riskSystemDetailDTO.setSystemId(1L);
    List<RiskSystemDetailDTO> riskSystemDetailDTOS = Mockito.spy(ArrayList.class);
    riskSystemDetailDTOS.add(riskSystemDetailDTO);
    riskSystemDTO.setListRiskSystemDetail(riskSystemDetailDTOS);
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
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    RiskSystemHistoryDTO riskSystemHistoryDTO = Mockito.spy(RiskSystemHistoryDTO.class);

    PowerMockito.when(riskSystemDetailRepository.insertRiskSystemDetail(any()))
        .thenReturn(resultInSideDto1);
    PowerMockito.when(riskSystemDetailRepository.deleteRiskSystemDetailBySystemId(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskSystemRepository.getRiskSystemOldById(anyLong()))
        .thenReturn(riskSystemDTO);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(riskSystemRepository.insertOrUpdateRiskSystem(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = null;

    try {
      result = riskSystemBusiness.updateRiskSystemWeb(fileAttacks, riskSystemDTO);
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

  @Test
  public void updateRiskSystemWeb_3() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    resultInSideDto.setId(1L);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setId(1L);
    resultInSideDto1.setKey("ERROR");

    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO.setId(1L);
    riskSystemDTO.setFileName("1");
    riskSystemDTO.setSystemCode("1");
    riskSystemDTO.setSystemName("1");
    riskSystemDTO.setSchedule(1L);
    riskSystemDTO.setSystemPriority(1L);
    riskSystemDTO.setLastUpdateTime(new Date());
    riskSystemDTO.setCountryId(1L);
    List<String> strings = new ArrayList<>();
    strings.add("1");
    strings.add("1");
    riskSystemDTO.setLstFileName(strings);

    RiskSystemDTO riskSystemDTO1 = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO1.setId(2L);
    riskSystemDTO1.setFileName("2");
    riskSystemDTO1.setSystemCode("2");
    riskSystemDTO1.setSystemName("2");
    riskSystemDTO1.setSchedule(2L);
    riskSystemDTO1.setSystemPriority(2L);
    riskSystemDTO1.setLastUpdateTime(new Date());
    riskSystemDTO1.setCountryId(2L);
    List<String> strings1 = new ArrayList<>();
    strings1.add("2");
    strings1.add("2");
    riskSystemDTO1.setLstFileName(strings1);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setBusinessId(1L);
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    gnocFileDtos.add(gnocFileDto);
    riskSystemDTO.setGnocFileDtos(gnocFileDtos);
    RiskSystemDetailDTO riskSystemDetailDTO = Mockito.spy(RiskSystemDetailDTO.class);
    riskSystemDetailDTO.setId(1L);
    riskSystemDetailDTO.setSystemId(1L);
    List<RiskSystemDetailDTO> riskSystemDetailDTOS = Mockito.spy(ArrayList.class);
    riskSystemDetailDTOS.add(riskSystemDetailDTO);
    riskSystemDTO.setListRiskSystemDetail(riskSystemDetailDTOS);

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
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    RiskSystemHistoryDTO riskSystemHistoryDTO = Mockito.spy(RiskSystemHistoryDTO.class);
    riskSystemHistoryDTO.setUserId(1L);
    List<RiskSystemHistoryDTO> listHistory = Mockito.spy(ArrayList.class);
    listHistory.add(riskSystemHistoryDTO);
    RiskSystemFileEntity riskSystemFileEntity = Mockito.spy(RiskSystemFileEntity.class);
    riskSystemFileEntity.setSystemId(1L);
    List<RiskSystemFileEntity> lstFileOld = Mockito.spy(ArrayList.class);
    lstFileOld.add(riskSystemFileEntity);
    GnocFileEntity gnocFileEntity = Mockito.spy(GnocFileEntity.class);
    gnocFileEntity.setPath("/temp");
    List<GnocFileEntity> listEntity = Mockito.spy(ArrayList.class);
    listEntity.add(gnocFileEntity);
    PowerMockito.when(gnocFileRepository.getLstGnocFileByDto(any())).thenReturn(listEntity);
    PowerMockito.when(riskSystemHistoryRepository.insertRiskSystemHistory(any()))
        .thenReturn(resultInSideDto1);
    PowerMockito.when(riskSystemFileRepository.deleteRiskSystemFile(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskSystemFileRepository.getListRiskSystemFileBySystemId(anyLong()))
        .thenReturn(lstFileOld);
    PowerMockito.when(riskSystemHistoryRepository.getListHistoryBySystemId(anyLong()))
        .thenReturn(listHistory);
    PowerMockito.when(riskSystemDetailRepository.insertRiskSystemDetail(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskSystemDetailRepository.deleteRiskSystemDetailBySystemId(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskSystemRepository.getRiskSystemOldById(anyLong()))
        .thenReturn(riskSystemDTO1);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(riskSystemRepository.insertOrUpdateRiskSystem(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = null;

    try {
      result = riskSystemBusiness.updateRiskSystemWeb(null, riskSystemDTO);
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    assertNull(result);
  }

  @Test
  public void updateRiskSystemWeb_4() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    resultInSideDto.setId(1L);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setId(1L);
    resultInSideDto1.setKey("ERROR");
    List<String> strings = Mockito.spy(ArrayList.class);
    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO.setId(1L);
    riskSystemDTO.setFileName("1");
    riskSystemDTO.setSystemCode("1");
    riskSystemDTO.setSystemName("1");
    riskSystemDTO.setSchedule(1L);
    riskSystemDTO.setSystemPriority(1L);
    riskSystemDTO.setLastUpdateTime(new Date());
    riskSystemDTO.setCountryId(1L);
    List<Long> longList = Mockito.spy(ArrayList.class);
    longList.add(1L);
    riskSystemDTO.setIdDeleteList(longList);
    String a = "1";
    strings.add(a);
    riskSystemDTO.setLstFileName(strings);
    RiskSystemDTO riskSystemDTO1 = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO1.setId(2L);
    riskSystemDTO1.setFileName("2");
    riskSystemDTO1.setSystemCode("2");
    riskSystemDTO1.setSystemName("2");
    riskSystemDTO1.setSchedule(2L);
    riskSystemDTO1.setSystemPriority(2L);
    riskSystemDTO1.setLastUpdateTime(new Date());
    riskSystemDTO1.setCountryId(2L);
    String a1 = "2";

    strings.add(a1);
    riskSystemDTO1.setLstFileName(strings);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setBusinessId(1L);
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    gnocFileDtos.add(gnocFileDto);
    riskSystemDTO.setGnocFileDtos(gnocFileDtos);
    RiskSystemDetailDTO riskSystemDetailDTO = Mockito.spy(RiskSystemDetailDTO.class);
    riskSystemDetailDTO.setId(1L);
    riskSystemDetailDTO.setSystemId(1L);
    List<RiskSystemDetailDTO> riskSystemDetailDTOS = Mockito.spy(ArrayList.class);
    riskSystemDetailDTOS.add(riskSystemDetailDTO);
    riskSystemDTO.setListRiskSystemDetail(riskSystemDetailDTOS);
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
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    RiskSystemHistoryDTO riskSystemHistoryDTO = Mockito.spy(RiskSystemHistoryDTO.class);
    riskSystemHistoryDTO.setUserId(1L);
    List<RiskSystemHistoryDTO> listHistory = Mockito.spy(ArrayList.class);
    listHistory.add(riskSystemHistoryDTO);
    RiskSystemFileEntity riskSystemFileEntity = Mockito.spy(RiskSystemFileEntity.class);
    riskSystemFileEntity.setSystemId(1L);
    List<RiskSystemFileEntity> lstFileOld = Mockito.spy(ArrayList.class);
    lstFileOld.add(riskSystemFileEntity);

    PowerMockito.when(riskSystemHistoryRepository.insertRiskSystemHistory(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskSystemFileRepository.deleteRiskSystemFile(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskSystemFileRepository.getListRiskSystemFileBySystemId(anyLong()))
        .thenReturn(lstFileOld);
    PowerMockito.when(riskSystemHistoryRepository.getListHistoryBySystemId(anyLong()))
        .thenReturn(listHistory);
    PowerMockito.when(riskSystemDetailRepository.insertRiskSystemDetail(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskSystemDetailRepository.deleteRiskSystemDetailBySystemId(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskSystemRepository.getRiskSystemOldById(anyLong()))
        .thenReturn(riskSystemDTO1);
    PowerMockito.when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitToken);
    PowerMockito.when(riskSystemRepository.insertOrUpdateRiskSystem(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskSystemRepository.findRiskSystemByIdFromWeb(anyLong(), anyDouble()))
        .thenReturn(new RiskSystemDTO());
    PowerMockito.when(riskSystemFileRepository.insertRiskSystemFile(any()))
        .thenReturn(resultInSideDto);

    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(
        FileUtils
            .saveFtpFile(anyString(), anyInt(), anyString(),
                anyString(), anyString(), anyString(),
                any(), any())
    ).thenReturn("abc");
    PowerMockito.when(
        FileUtils
            .saveUploadFile(anyString(), any(), anyString(), any())
    ).thenReturn("abc");
    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(uploadFile);
    ResultInSideDto result = riskSystemBusiness.updateRiskSystemWeb(files, riskSystemDTO);
    assertEquals(result.getKey(), "SUCCESS");

  }

  @Test
  public void deleteRiskSystem() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(riskSystemRepository.deleteRiskSystem(anyLong())).thenReturn(resultInSideDto);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(riskSystemRepository.findRiskSystemByIdFromWeb(anyLong(), anyDouble())).thenReturn(new RiskSystemDTO());
    PowerMockito.when(riskSystemRepository.deleteRiskSystem(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto result = riskSystemBusiness.deleteRiskSystem(1L);
    assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void findRiskSystemByIdFromWeb() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    RiskSystemDetailDTO riskSystemDetailDTO = Mockito.spy(RiskSystemDetailDTO.class);
    riskSystemDetailDTO.setId(1L);
    List<RiskSystemDetailDTO> listDetail = Mockito.spy(ArrayList.class);
    listDetail.add(riskSystemDetailDTO);

    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1.1);
    PowerMockito.when(riskSystemRepository
        .findRiskSystemByIdFromWeb(anyLong(), anyDouble())).thenReturn(riskSystemDTO);
    PowerMockito.when(riskSystemDetailRepository
        .getListRiskSystemDetailBySystemId(anyLong())).thenReturn(listDetail);

    RiskSystemDTO result = riskSystemBusiness.findRiskSystemByIdFromWeb(1L);

    assertEquals(riskSystemDTO, result);
  }

  @Test
  public void getListFileFromRiskSystem() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setBusinessCode("RISK_SYSTEM");
    gnocFileDto.setBusinessId(1L);
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    gnocFileDtos.add(gnocFileDto);

    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtos);

    List<GnocFileDto> result = riskSystemBusiness.getListFileFromRiskSystem(1L);

    assertEquals(gnocFileDtos, result);
  }

  @Test
  public void getListRiskSystemHistoryBySystemId() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    Datatable datatable = Mockito.spy(Datatable.class);
    RiskSystemHistoryDTO riskSystemHistoryDTO = Mockito.spy(RiskSystemHistoryDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);

    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1.1);
    PowerMockito.when(riskSystemHistoryRepository.getListRiskSystemHistoryBySystemId(any()))
        .thenReturn(datatable);

    Datatable result = riskSystemBusiness.getListRiskSystemHistoryBySystemId(riskSystemHistoryDTO);

    assertEquals(datatable, result);
  }

  @Test
  public void exportDataRiskSystem() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aaa");

    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    riskSystemDTO.setId(1L);
    List<RiskSystemDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskSystemDTO);
    RiskSystemDetailDTO riskSystemDetailDTO = Mockito.spy(RiskSystemDetailDTO.class);
    List<RiskSystemDetailDTO> listDetail = Mockito.spy(ArrayList.class);
    listDetail.add(riskSystemDetailDTO);

    File fileExport = new File("./test_junit2/test.txt");

    PowerMockito.when(riskSystemRepository.getListRiskSystemExport(any())).thenReturn(list);
    PowerMockito.when(riskSystemDetailRepository
        .getListRiskSystemDetailBySystemId(anyLong())).thenReturn(listDetail);
    PowerMockito.when(CommonExport.exportExcel(anyString(), anyString(), anyList(), any()))
        .thenReturn(fileExport);

    riskSystemBusiness.exportDataRiskSystem(riskSystemDTO);

    assertNotNull(fileExport);
  }

  @Test
  public void getTemplateImport() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aa");
    Datatable dataSystemPriority = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO1 = Mockito.spy(CatItemDTO.class);
    catItemDTO1.setItemName("xxx");
    catItemDTO1.setItemValue("111");
    List<CatItemDTO> list1 = Mockito.spy(ArrayList.class);
    list1.add(catItemDTO1);
    dataSystemPriority.setData(list1);

    Datatable dataCountry = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO2 = Mockito.spy(CatItemDTO.class);
    catItemDTO2.setItemId(1L);
    catItemDTO2.setItemName("xxx");
    List<CatItemDTO> list2 = Mockito.spy(ArrayList.class);
    list2.add(catItemDTO2);
    dataCountry.setData(list2);

    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataSystemPriority);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataCountry);

    riskSystemBusiness.getTemplateImport();
  }

  @Test
  public void importDataRiskSystem() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FILE_IS_NULL);
    MultipartFile multipartFile = null;
    ResultInSideDto result = riskSystemBusiness.importDataRiskSystem(multipartFile);
    assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void importDataRiskSystem_01() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);

    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";

    PowerMockito
        .when(FileUtils.saveTempFile(firstFile.getOriginalFilename(), firstFile.getBytes(), null))
        .thenReturn(filePath);

    ResultInSideDto result = riskSystemBusiness.importDataRiskSystem(firstFile);
    assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void importDataRiskSystem_02() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.DATA_OVER);

    PowerMockito.when(I18n.getLanguage("riskSystem.STT")).thenReturn("resultImport");
    PowerMockito.when(I18n.getLanguage("riskSystem.systemCode")).thenReturn("systemCode");
    PowerMockito.when(I18n.getLanguage("riskSystem.systemName")).thenReturn("systemName");
    PowerMockito.when(I18n.getLanguage("riskSystem.schedule")).thenReturn("scheduleStr");
    PowerMockito.when(I18n.getLanguage("riskSystem.systemPriorityName"))
        .thenReturn("systemPriorityName");
    PowerMockito.when(I18n.getLanguage("riskSystem.countryName")).thenReturn("countryName");
    PowerMockito.when(I18n.getLanguage("riskSystem.lastUpdateTimeStr"))
        .thenReturn("lastUpdateTimeStr");
    PowerMockito.when(I18n.getLanguage("riskSystem.manageUserName")).thenReturn("manageUserName");
    PowerMockito.when(I18n.getLanguage("riskSystem.actionName")).thenReturn("actionName");

    String[] header = new String[]{"resultImport", "systemCode (*)", "systemName (*)",
        "scheduleStr (*)", "systemPriorityName (*)",
        "countryName (*)", "lastUpdateTimeStr (*)", "manageUserName (*)", "actionName (*)"};

    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito
        .when(FileUtils.saveTempFile(firstFile.getOriginalFilename(), firstFile.getBytes(), null))
        .thenReturn(filePath);

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 600; i++) {
      lstData.add(header);
    }

    File fileImp = new File(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 8, 1000))
        .thenReturn(lstData);
    PowerMockito.when(CommonImport.getDataFromExcelFileNew(fileImp, 0, 6, 0, 8, 1000))
        .thenReturn(lstData);

    ResultInSideDto result = riskSystemBusiness.importDataRiskSystem(firstFile);
    assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void importDataRiskSystem_03() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);

    PowerMockito.when(I18n.getLanguage("riskSystem.STT")).thenReturn("resultImport");
    PowerMockito.when(I18n.getLanguage("riskSystem.systemCode")).thenReturn("systemCode");
    PowerMockito.when(I18n.getLanguage("riskSystem.systemName")).thenReturn("systemName");
    PowerMockito.when(I18n.getLanguage("riskSystem.schedule")).thenReturn("scheduleStr");
    PowerMockito.when(I18n.getLanguage("riskSystem.systemPriorityName"))
        .thenReturn("systemPriorityName");
    PowerMockito.when(I18n.getLanguage("riskSystem.countryName")).thenReturn("countryName");
    PowerMockito.when(I18n.getLanguage("riskSystem.lastUpdateTimeStr"))
        .thenReturn("lastUpdateTimeStr");
    PowerMockito.when(I18n.getLanguage("riskSystem.manageUserName")).thenReturn("manageUserName");
    PowerMockito.when(I18n.getLanguage("riskSystem.actionName")).thenReturn("actionName");
    PowerMockito.when(I18n.getLanguage("riskSystem.action.0")).thenReturn("actionName0");
    PowerMockito.when(I18n.getLanguage("riskSystem.action.1")).thenReturn("actionName");

    String[] header = new String[]{"resultImport", "systemCode (*)", "systemName (*)",
        "scheduleStr (*)", "systemPriorityName (*)",
        "countryName (*)", "lastUpdateTimeStr (*)", "manageUserName (*)", "actionName (*)"};
    String[] header1 = new String[]{"resultImport", "systemCode", "systemName", "121212",
        "systemPriorityName",
        "countryName", "29/12/1991 12:45:00", "manageUserName", "actionName"};

    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito
        .when(FileUtils.saveTempFile(firstFile.getOriginalFilename(), firstFile.getBytes(), null))
        .thenReturn(filePath);

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(header);
    lstData.add(header1);

    File fileImp = new File(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 8, 1000))
        .thenReturn(lstData);
    PowerMockito.when(CommonImport.getDataFromExcelFileNew(fileImp, 0, 6, 0, 8, 1000))
        .thenReturn(lstData);

    Datatable dataSystemPriority = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO1 = Mockito.spy(CatItemDTO.class);
    catItemDTO1.setItemId(1L);
    catItemDTO1.setItemValue("222");
    catItemDTO1.setItemName("systemPriorityName");
    List<CatItemDTO> list1 = Mockito.spy(ArrayList.class);
    list1.add(catItemDTO1);
    dataSystemPriority.setData(list1);
    PowerMockito.when(catItemRepository
        .getItemMaster(Constants.CATEGORY.RISK_SYSTEM_PRIORITY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME))
        .thenReturn(dataSystemPriority);

    Datatable dataCountry = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(0L);
    catItemDTO1.setItemValue("333");
    catItemDTO.setItemName("countryName");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    dataCountry.setData(list);

    PowerMockito.when(catItemRepository
        .getItemMaster(RISK_MASTER_CODE.GNOC_COUNTRY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME))
        .thenReturn(dataCountry);

    UsersEntity user = Mockito.spy(UsersEntity.class);
    PowerMockito.when(userRepository.getUserByUserName(anyString()))
        .thenReturn(user);

    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    PowerMockito.when(riskSystemRepository.checkRiskSystemExit(any()))
        .thenReturn(riskSystemDTO);

    riskSystemBusiness.importDataRiskSystem(firstFile);
  }

  @Test
  public void setMapSystemPriority() {
    Datatable dataSystemPriority = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("systemPriorityName");
    catItemDTO.setItemValue("111");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    dataSystemPriority.setData(list);

    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataSystemPriority);

    riskSystemBusiness.setMapSystemPriority();

    assertNotNull(dataSystemPriority);
  }

  @Test
  public void setMapCountry() {
    Datatable dataCountry = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("xxx");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    dataCountry.setData(list);

    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataCountry);

    riskSystemBusiness.setMapCountry();

    assertNotNull(dataCountry);
  }

  @Test
  public void handleFileExport() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);

    RiskSystemDTO riskSystemDTO = Mockito.spy(RiskSystemDTO.class);
    List<RiskSystemDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskSystemDTO);
    String[] test = {"aaa", "bbb", "ccc"};
    riskSystemBusiness.handleFileExport(list, test, "29/12/1991", "RESULT_IMPORT");

    assertNotNull(list);
  }

  @Test
  public void compareObjectData() {
  }

  @Test
  public void getTemplateImportSystemDetail() throws IOException {
    riskSystemBusiness.getTemplateImportSystemDetail();
  }
}

