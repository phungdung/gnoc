package com.viettel.gnoc.od.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SignVofficeDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.dto.WoSearchWebDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.OdCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.SrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.od.dto.OdCfgBusinessDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusForm;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdDTOSearch;
import com.viettel.gnoc.od.dto.OdHistoryDTO;
import com.viettel.gnoc.od.dto.OdRelationDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.od.repository.OdHistoryRepository;
import com.viettel.gnoc.od.repository.OdRelationRepository;
import com.viettel.gnoc.od.repository.OdRepository;
import com.viettel.gnoc.od.wsclient.WSNIMS_HTPort;
import com.viettel.gnoc.od.wsclient.WS_VOFFICE_Port;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.wfm.dto.WoTypeTimeDTO;
import com.viettel.nims.webservice.ht.InputUpdateStationAuditedStatusForm;
import com.viettel.nims.webservice.ht.ResultUpdateStationAuditedStatusForm;
import com.viettel.security.PassTranformer;
import com.viettel.voffice.ws_autosign.service.FileAttachTranfer;
import com.viettel.voffice.ws_autosign.service.Vof2EntityUser;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

/**
 * @Author DungLV
 * @Since 03/20/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class, CommonImport.class, I18n.class, TicketProvider.class,
    CommonExport.class, PassTranformer.class, Calendar.class})
public class OdBusinessImplTest {

  @InjectMocks
  OdBusinessImpl odBusiness;

  @Mock
  OdRepository odRepository;

  @Mock
  OdHistoryRepository odHistoryRepository;

  @Mock
  MessagesRepository messagesRepository;

  @Mock
  OdRelationRepository odRelationRepository;

  @Mock
  OdCategoryServiceProxy odCategoryServiceProxy;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitRepository unitRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  OdHistoryBusinessImpl odHistoryBusiness;

  @Mock
  WS_VOFFICE_Port wsVofficePort;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  SrServiceProxy srServiceProxy;

  @Mock
  CommonRepository commonRepository;

  @Mock
  WSNIMS_HTPort wsnimsHtPort;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  OdCommonBusiness odCommonBusiness;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(odBusiness, "uploadFolder",
        "/uploadFolder");
    ReflectionTestUtils.setField(odBusiness, "ftpFolder",
        "/ftpFolder");
    ReflectionTestUtils.setField(odBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(odBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(odBusiness, "ftpServer",
        "10.240.232.25");
    ReflectionTestUtils.setField(odBusiness, "ftpPort",
        21);
  }

  @Test
  public void testGetListDataSearch_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(datatable.getTotal()).thenReturn(10);
    when(odRepository.getListDataSearch(any())).thenReturn(datatable);
    setFinalStatic(OdBusinessImpl.class.getDeclaredField("log"), logger);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Datatable result = odBusiness.getListDataSearch(new OdSearchInsideDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void testFindOdById_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    OdDTO odDto = new OdDTO();
    odDto.setOdId(1L);
    when(odRepository.findOdById(any())).thenReturn(odDto);
    setFinalStatic(OdBusinessImpl.class.getDeclaredField("log"), logger);
    OdDTO result = odBusiness.findOdById(1L);
    assertEquals(odDto, result);
  }

  @Test
  public void testGetDetailOdDTOById_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> fileNames = new ArrayList<>();
    List<OdHistoryDTO> odHistoryDTOS = new ArrayList<>();
    List<OdRelationDTO> odRelationDTOS = new ArrayList<>();
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    OdHistoryDTO odHistoryDTO = new OdHistoryDTO();
    OdRelationDTO odRelationDTO = new OdRelationDTO();
    OdDTO odDTO = new OdDTO();
    GnocFileDto gnocFileDto = new GnocFileDto();

    fileNames.add("test.txt");
    odHistoryDTO.setOdHisId(1L);
    odHistoryDTO.setOdId(1L);

    odHistoryDTOS.add(odHistoryDTO);
    odRelationDTO.setOdId(1L);
    odRelationDTO.setId(1L);
    odRelationDTOS.add(odRelationDTO);

    odDTO.setOdId(1L);
    odDTO.setOdHistoryDTO(odHistoryDTOS);
    odDTO.setLstFileName(fileNames);
    odDTO.setLstOdRelation(odRelationDTOS);

    gnocFileDto.setId(1L);
    gnocFileDto.setBusinessCode("OD");
    gnocFileDto.setBusinessId(1L);
    gnocFileDtos.add(gnocFileDto);

    when(odRepository.getDetailOdDTOById(any())).thenReturn(odDTO);
    when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtos);
    setFinalStatic(OdBusinessImpl.class.getDeclaredField("log"), logger);
    OdDTO result = odBusiness.getDetailOdDTOById(1L);
    assertEquals(odDTO, result);
  }

  @Test
  public void testAdd_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    OdDTO od = new OdDTO();

    when(resultInSideDto.getId()).thenReturn(10L);
    when(odRepository.add(any())).thenReturn(resultInSideDto);
    setFinalStatic(OdBusinessImpl.class.getDeclaredField("log"), logger);

    ResultInSideDto result = odBusiness.add(od);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testEdit_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    OdDTO od = new OdDTO();
    when(resultInSideDto.getId()).thenReturn(10L);
    when(odRepository.edit(any())).thenReturn(resultInSideDto);

    setFinalStatic(OdBusinessImpl.class.getDeclaredField("log"), logger);
    ResultInSideDto result = odBusiness.edit(od);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testDelete_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(odRepository.delete(any())).thenReturn(resultInSideDto);
    setFinalStatic(OdBusinessImpl.class.getDeclaredField("log"), logger);
    ResultInSideDto result = odBusiness.delete(10L);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testGetSeqOd_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(OdBusinessImpl.class.getDeclaredField("log"), logger);
    List<String> result = odBusiness.getSeqOd(1);
    assertNotNull(result);
  }

  /*@Test
  public void testExportData_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<OdSearchInsideDTO> odSearchDTOS = new ArrayList<>();
    OdSearchInsideDTO odSearchDTO = new OdSearchInsideDTO();
    odSearchDTO.setStatus("1");
    odSearchDTO.setOdId(1L);
    odSearchDTO.setOdTypeId(1L);
    odSearchDTO.setOdCode("Code");
    odSearchDTOS.add(odSearchDTO);

    when(odRepository.getListDataExport(any())).thenReturn(odSearchDTOS);
    setFinalStatic(OdBusinessImpl.class.getDeclaredField("log"), logger);
    File result = odBusiness.exportData(odSearchDTO);
    assertNotNull(result);

  }*/

  @Test
  public void testInsertOdFromWeb_01() throws Exception {
    OdBusinessImpl classUnderTest = PowerMockito.spy(odBusiness);
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<String> lstUnit = new ArrayList<>();
    List<OdRelationDTO> odRelationDTOS = new ArrayList<>();
    List<String> sequence = new ArrayList<>();
    OdRelationDTO odRelationDTO = new OdRelationDTO();
    OdSearchInsideDTO odSearchInsideDTO = new OdSearchInsideDTO();
    ResultInSideDto resultDto = new ResultInSideDto();
    UsersEntity usersEntity = new UsersEntity();

    sequence.add("1234567");

    odRelationDTO.setOdId(1L);
    odRelationDTO.setSystem("WO");
    odRelationDTOS.add(odRelationDTO);

    lstUnit.add("1");

    odSearchInsideDTO.setCreatePersonId(1L);
    odSearchInsideDTO.setStatus("1");
    odSearchInsideDTO.setLstReceiveUnitId(lstUnit);
    odSearchInsideDTO.setCreatePersonId(999999L);
    odSearchInsideDTO.setOdName("Test");
    odSearchInsideDTO.setLstOdRelation(odRelationDTOS);
    odSearchInsideDTO.setOdTypeId(1L);

    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, 3);
    cal.set(Calendar.HOUR_OF_DAY, 19);
    odSearchInsideDTO.setEndTime(cal.getTime());
    cal.add(Calendar.DATE, -2);
    odSearchInsideDTO.setStartTime(cal.getTime());

    resultDto.setId(1234567L);
    resultDto.setMessage(RESULT.SUCCESS);
    resultDto.setKey(RESULT.SUCCESS);

    usersEntity.setUserId(999999L);

    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setId(1L);
    gnocFileDto.setBusinessCode("OD");
    gnocFileDto.setBusinessId(1L);
    gnocFileDtos.add(gnocFileDto);

    when(odRepository.getUserByUserId(any())).thenReturn(usersEntity);
    when(odRepository.getSeqOd(anyString(), anyInt())).thenReturn((List) sequence);
    when(odRepository.insertOrUpdate(any())).thenReturn(resultDto);
    when(odHistoryRepository.insertOrUpdate(any())).thenReturn(resultDto);
    when(odRelationRepository.insertOrUpdate(any())).thenReturn(resultDto);
    when(messagesRepository.insertOrUpdateWfm(any())).thenReturn(resultDto);
    when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtos);

    setFinalStatic(OdBusinessImpl.class.getDeclaredField("log"), logger);

    PowerMockito.mockStatic(I18n.class);
    when(I18n.getLanguage(any())).thenReturn("test");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(1L);
    unitToken.setUnitName("A");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);

    List<WoTypeTimeDTO> lstTime = Mockito.spy(ArrayList.class);
    WoTypeTimeDTO woTypeTimeDTO = Mockito.spy(WoTypeTimeDTO.class);
    woTypeTimeDTO.setDuration(1D);
    woTypeTimeDTO.setIsImmediate(1L);
    lstTime.add(woTypeTimeDTO);
    PowerMockito.when(odRepository.getListWoTypeTimeDtosByWoTypeId(anyLong())).thenReturn(lstTime);

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
    ).thenReturn("tripm");
    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(uploadFile);

    PowerMockito.when(odRepository.insertOdFile(any())).thenReturn(resultDto);

    ResultInSideDto result = classUnderTest.insertOdFromWeb(odSearchInsideDTO, files);
    assertEquals(resultDto.getKey(), result.getKey());
  }

  @Test
  public void testUpdateOdFromWeb_01() throws Exception {
    OdBusinessImpl classUnderTest = PowerMockito.spy(odBusiness);
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    OdSearchInsideDTO odSearchInsideDTO = new OdSearchInsideDTO();
    List<OdCfgBusinessDTO> odCfgBusinessDTOS = new ArrayList<>();
    OdCfgBusinessDTO odCfgBusinessDTO = new OdCfgBusinessDTO();
    OdDTO odDTO = odSearchInsideDTO.toDTO();
    OdChangeStatusDTO odChangeStatusDTO = new OdChangeStatusDTO();
    UsersEntity usersEntity = new UsersEntity();

    odSearchInsideDTO.setCreatePersonId(999999L);
    odSearchInsideDTO.setStatus("1");
    odSearchInsideDTO.setOdId(1L);
    odSearchInsideDTO.setOdName("Test");
    odSearchInsideDTO.setReceiveUnitId(999L);
    odSearchInsideDTO.setOldStatus(1L);
    odSearchInsideDTO.setStatus("1");
    odSearchInsideDTO.setPriorityId(2586L);
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, 3);
    odSearchInsideDTO.setEndTime(cal.getTime());
    cal.add(Calendar.DATE, -2);
    odSearchInsideDTO.setStartTime(cal.getTime());
    odSearchInsideDTO.setGnocFileDtos(new ArrayList<>());
    odSearchInsideDTO.setIdDeleteList(new ArrayList<>());

    odChangeStatusDTO.setId(1L);
    odChangeStatusDTO.setOldStatus(1L);
    odChangeStatusDTO.setNewStatus(1L);
    odChangeStatusDTO.setOdPriority(2586L);

    usersEntity.setUserId(999999L);
    usersEntity.setUnitId(1L);
    usersEntity.setUsername("thanhlv12");
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setId(1234567L);
    resultDto.setMessage(RESULT.SUCCESS);
    resultDto.setKey(RESULT.SUCCESS);

    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setId(1L);
    gnocFileDto.setBusinessCode("OD");
    gnocFileDto.setBusinessId(1L);
    gnocFileDtos.add(gnocFileDto);

    when(odRepository.getUserByUserName(anyString())).thenReturn(usersEntity);
    when(odHistoryRepository.insertOrUpdate(any())).thenReturn(resultDto);
    when(messagesRepository.insertOrUpdateWfm(any())).thenReturn(resultDto);
    when(odRepository.insertOrUpdate(any())).thenReturn(resultDto);
    when(odCategoryServiceProxy.getOdChangeStatusDTOByParams(anyString(), anyString(), anyString()))
        .thenReturn(odChangeStatusDTO);
    when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtos);

    odCfgBusinessDTO.setColumnName("description");
    odCfgBusinessDTOS.add(odCfgBusinessDTO);
    when(odCategoryServiceProxy.getListOdCfgBusiness(any())).thenReturn(odCfgBusinessDTOS);
    WSNIMS_HTPort htPort = PowerMockito.mock(WSNIMS_HTPort.class);
    InputUpdateStationAuditedStatusForm input = new InputUpdateStationAuditedStatusForm();
    input.setOrderCode(odDTO.getOdCode());
    input.setOrderStatus(1L);
    input.setType(1L);
    ResultUpdateStationAuditedStatusForm res = PowerMockito
        .mock(ResultUpdateStationAuditedStatusForm.class);
    when(res.getResult()).thenReturn(RESULT.SUCCESS);
    when(htPort.updateSubmittingOrderStatus(any())).thenReturn(res);

    PowerMockito.mockStatic(I18n.class);
    when(I18n.getLanguage(any())).thenReturn("test");
    setFinalStatic(OdBusinessImpl.class.getDeclaredField("log"), logger);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(1L);
    unitToken.setUnitName("A");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    ResultInSideDto result = classUnderTest
        .updateOdFromWeb(odSearchInsideDTO, new ArrayList<>(), "thanhlv12");
    assertEquals(resultDto.getKey(), result.getKey());
  }

  @Test
  public void testGetListStatusNext_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    UsersEntity usersEntity = new UsersEntity();
    usersEntity.setUserId(999999L);
    usersEntity.setUnitId(1L);
    usersEntity.setUsername("thanhlv12");
    Datatable datatable = PowerMockito.mock(Datatable.class);
    List<CatItemDTO> list = new ArrayList<>();
    when(datatable.getData()).thenReturn((List) list);
    when(odRepository.getUserByUserName(anyString())).thenReturn(usersEntity);
    when(odRepository.getListStatusNext(anyLong(), anyString(), anyString())).thenReturn(datatable);

    setFinalStatic(OdBusinessImpl.class.getDeclaredField("log"), logger);
    Datatable datatable1 = odBusiness.getListStatusNext(1L, "thanhlv12");
    assertNotNull(datatable1);

  }

  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }

  @Test
  public void testinsertOrUpdateListOd() {
    OdDTO odDTO = Mockito.spy(OdDTO.class);
    List<OdDTO> odDTOs = Mockito.spy(ArrayList.class);
    odDTOs.add(odDTO);
    String result = odBusiness.insertOrUpdateListOd(odDTOs);
    assertEquals(result, RESULT.SUCCESS);
  }

  @Test
  public void testExportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(TicketProvider.class);

    OdSearchInsideDTO odSearchInsideDTO = Mockito.spy(OdSearchInsideDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(9999L);
    List<OdSearchInsideDTO> odSearchInsideDTOS = Mockito.spy(ArrayList.class);

    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("test");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(odRepository.getListDataExport(any())).thenReturn(odSearchInsideDTOS);

    File result = odBusiness.exportData(odSearchInsideDTO);
    Assert.assertNull(result);
  }

  @Test
  public void testSignToVoffice() throws Exception {
    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);

    SignVofficeDTO signVofficeDTO = Mockito.spy(SignVofficeDTO.class);
    signVofficeDTO.setOdId(1111L);
    signVofficeDTO.setUserName("thanhlv12");
    signVofficeDTO.setPassword("123qwe");
    signVofficeDTO.setEmailUser("thanhlv12@viettel.com.vn");
    signVofficeDTO.setTitle("title");
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    OdDTO odresult = Mockito.spy(OdDTO.class);
    odresult.setOdId(1111L);
    UsersEntity exeUs = Mockito.spy(UsersEntity.class);
    String input = "5555";
    List<Vof2EntityUser> lstUf = Mockito.spy(ArrayList.class);
    Vof2EntityUser vof2EntityUser = Mockito.spy(Vof2EntityUser.class);
    vof2EntityUser.setSignImageIndex(11L);
    lstUf.add(vof2EntityUser);
    List<FileAttachTranfer> lstTranfer = Mockito.spy(ArrayList.class);
    MockMultipartFile testFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes() );
    files.add(testFile);
    Long signRes = 1L;

    PowerMockito.when(odRepository.findOdById(anyLong())).thenReturn(odresult);
    PowerMockito.when(odRepository.getUserByUserName(anyString())).thenReturn(exeUs);
    PowerMockito.when(odRepository.getConfigPropertyValue(anyString())).thenReturn(input);
    PowerMockito.when(wsVofficePort.getListVof2UserByMail(anyList())).thenReturn(lstUf);
    PowerMockito.when(wsVofficePort.vo2RegDigitalDocByEmail(any())).thenReturn(signRes);

    ResultInSideDto result = odBusiness.signToVoffice(signVofficeDTO, files);

    Assert.assertEquals(result.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testGetRelationsByOdId() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Active");

    List<OdRelationDTO> odRelationDTOS = Mockito.spy(ArrayList.class);
    OdRelationDTO odRelationDTO = Mockito.spy(OdRelationDTO.class);
    odRelationDTO.setSystem("WO");
    odRelationDTO.setSystemCode("1111");
    OdRelationDTO odRelationDTO1 = Mockito.spy(OdRelationDTO.class);
    odRelationDTO1.setSystem("CR");
    odRelationDTO1.setSystemId(1L);
    OdRelationDTO odRelationDTO2 = Mockito.spy(OdRelationDTO.class);
    odRelationDTO2.setSystem("SR");
    odRelationDTO2.setSystemId(2L);
    OdRelationDTO odRelationDTO3 = Mockito.spy(OdRelationDTO.class);
    odRelationDTO3.setSystem("RDM");
    odRelationDTO3.setSystemId(3L);

    odRelationDTOS.add(odRelationDTO);
    odRelationDTOS.add(odRelationDTO1);
    odRelationDTOS.add(odRelationDTO2);
    odRelationDTOS.add(odRelationDTO3);

    WoSearchWebDTO wo = Mockito.spy(WoSearchWebDTO.class);
    wo.setStatus("1");
    CrInsiteDTO cr = Mockito.spy(CrInsiteDTO.class);
    cr.setState("1");
    SrInsiteDTO sr = Mockito.spy(SrInsiteDTO.class);
    sr.setStatus("Active");

    Map<String, String> mapConfigProperty = Mockito.spy(HashMap.class);
    mapConfigProperty.put("url_rdm", "http://10.255.216.90:9092/RDM_API/api/rdm/ws/");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    PowerMockito.when(commonRepository.getUserByUserName(anyString())).thenReturn(usersInsideDto);
    PowerMockito.when(odRelationRepository.getRelationsByOdId(anyLong())).thenReturn(odRelationDTOS);
    PowerMockito.when(woServiceProxy.getWoSearchWebDTOByWoCode(anyString())).thenReturn(wo);
    PowerMockito.when(crServiceProxy.findCrByIdProxy(anyLong())).thenReturn(cr);
    PowerMockito.when(srServiceProxy.findSrFromOdByProxyId(anyLong())).thenReturn(sr);

    List<OdRelationDTO> result = odBusiness.getRelationsByOdId(1L);

    Assert.assertNotNull(result);
  }

  @Test
  public void testUpdateOdFromWeb_02() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(FileUtils.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    List<GnocFileDto> lstGnocFile = Mockito.spy(ArrayList.class);
    lstGnocFile.add(gnocFileDto);
    OdSearchInsideDTO odSearchInsideDTO = Mockito.spy(OdSearchInsideDTO.class);
    odSearchInsideDTO.setOdId(1111L);
    odSearchInsideDTO.setGnocFileDtos(lstGnocFile);
    odSearchInsideDTO.setOldStatus(0L);
    odSearchInsideDTO.setComment("comment");
    odSearchInsideDTO.setStatus("11");
    odSearchInsideDTO.setPriorityId(22L);
    odSearchInsideDTO.setOdTypeId(33L);
    odSearchInsideDTO.setDescription("description");
    odSearchInsideDTO.setReceiveUnitId(4L);
    odSearchInsideDTO.setReceiveUserId(5L);
    odSearchInsideDTO.setClearCodeId(6L);
    odSearchInsideDTO.setCloseCodeId(7L);
    odSearchInsideDTO.setEndPendingTime(new Date());
    odSearchInsideDTO.setCreatePersonId(8L);
    odSearchInsideDTO.setOdCode("5555");
    odSearchInsideDTO.setOdName("PR555");
    odSearchInsideDTO.setCreateTime(new Date());
    odSearchInsideDTO.setStartTime(new Date());
    odSearchInsideDTO.setEndTime(new Date());
    odSearchInsideDTO.setApproverId(9L);
    List<Long> longList = Mockito.spy(ArrayList.class);
    longList.add(1L);
    odSearchInsideDTO.setIdDeleteList(longList);

    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    MockMultipartFile testFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes() );
    files.add(testFile);

    OdDTO result = Mockito.spy(OdDTO.class);
    result.setWoId(2222L);
    result.setOldStatus(1L);
    result.setStatus(2L);
    result.setOdTypeId(3L);
    PowerMockito.when(odRepository.findOdById(anyLong())).thenReturn(result);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(99999L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UsersEntity user = Mockito.spy(UsersEntity.class);
    user.setUserId(55L);
    PowerMockito.when(odRepository.getUserByUserName(anyString())).thenReturn(user);
    PowerMockito.when(odRepository.getUserByUserId(anyLong())).thenReturn(user);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);

    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(
        FileUtils
            .saveFtpFile(anyString(), anyInt(), anyString(),
                anyString(), anyString(), anyString(),
                any(), any())
    ).thenReturn("tripm");
    PowerMockito.when(
        FileUtils
            .saveUploadFile(anyString(), any(), anyString(), any())
    ).thenReturn("tripm");

    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(odRepository.insertOdFile(any())).thenReturn(resultFileDataOld);

    OdChangeStatusDTO odChangeStatusDTO = Mockito.spy(OdChangeStatusDTO.class);
    odChangeStatusDTO.setSendCreate(1L);
    odChangeStatusDTO.setCreateContent("create content");
    odChangeStatusDTO.setSendReceiveUser(1L);
    odChangeStatusDTO.setReceiveUserContent("ReceiveUserContent");
    odChangeStatusDTO.setSendReceiveUnit(1L);
    odChangeStatusDTO.setReceiveUnitContent("ReceiveUnitContent");
    odChangeStatusDTO.setSendApprover(1L);
    odChangeStatusDTO.setApproverContent("ApproverContent");
    odChangeStatusDTO.setNextAction("FORCE_CLOSED,COMPLETE_NIMS_KDT_TK,CHECK_NIMS_DOC_DAO,UPDATE_NIMS_DOC_DAO_BKK,CHANGE_STATUS_TO_55");
    PowerMockito.when(
        odCategoryServiceProxy.getOdChangeStatusDTOByParams(
            anyString(), anyString(), anyString(), anyString()
        )
    ).thenReturn(odChangeStatusDTO);

    List<OdCfgBusinessDTO> odCfgBusinessDTOS = Mockito.spy(ArrayList.class);
    OdCfgBusinessDTO odCfgBusinessDTO1 = Mockito.spy(OdCfgBusinessDTO.class);
    odCfgBusinessDTO1.setColumnName("description");
    odCfgBusinessDTO1.setIsRequired(1L);
    OdCfgBusinessDTO odCfgBusinessDTO2 = Mockito.spy(OdCfgBusinessDTO.class);
    odCfgBusinessDTO2.setColumnName("receiveunitid");
    odCfgBusinessDTO2.setIsRequired(1L);
    OdCfgBusinessDTO odCfgBusinessDTO3 = Mockito.spy(OdCfgBusinessDTO.class);
    odCfgBusinessDTO3.setColumnName("receiveuserid");
    odCfgBusinessDTO3.setIsRequired(1L);
    OdCfgBusinessDTO odCfgBusinessDTO4 = Mockito.spy(OdCfgBusinessDTO.class);
    odCfgBusinessDTO4.setColumnName("clearcodeid");
    odCfgBusinessDTO4.setIsRequired(1L);
    OdCfgBusinessDTO odCfgBusinessDTO5 = Mockito.spy(OdCfgBusinessDTO.class);
    odCfgBusinessDTO5.setColumnName("closecodeid");
    odCfgBusinessDTO5.setIsRequired(1L);
    OdCfgBusinessDTO odCfgBusinessDTO6 = Mockito.spy(OdCfgBusinessDTO.class);
    odCfgBusinessDTO6.setColumnName("endpendingtime");
    odCfgBusinessDTO6.setIsRequired(1L);
    OdCfgBusinessDTO odCfgBusinessDTO7 = Mockito.spy(OdCfgBusinessDTO.class);
    odCfgBusinessDTO7.setColumnName("fileattach");
    odCfgBusinessDTO7.setIsRequired(1L);
    odCfgBusinessDTOS.add(odCfgBusinessDTO1);
    odCfgBusinessDTOS.add(odCfgBusinessDTO2);
    odCfgBusinessDTOS.add(odCfgBusinessDTO3);
    odCfgBusinessDTOS.add(odCfgBusinessDTO4);
    odCfgBusinessDTOS.add(odCfgBusinessDTO5);
    odCfgBusinessDTOS.add(odCfgBusinessDTO6);
    odCfgBusinessDTOS.add(odCfgBusinessDTO7);
    PowerMockito.when(odCategoryServiceProxy.getListOdCfgBusiness(any())).thenReturn(odCfgBusinessDTOS);

    List<OdChangeStatusDTO> lstChangeStatus = Mockito.spy(ArrayList.class);
    lstChangeStatus.add(odChangeStatusDTO);
    PowerMockito.when(odCategoryServiceProxy.search(any())).thenReturn(lstChangeStatus);

    List<UsersEntity> lstUs = Mockito.spy(ArrayList.class);
    lstUs.add(user);
    PowerMockito.when(odRepository.getListUserOfUnit(anyLong())).thenReturn(lstUs);

    ResultUpdateStationAuditedStatusForm res = Mockito.spy(ResultUpdateStationAuditedStatusForm.class);
    res.setResult("OK");
    PowerMockito.when(wsnimsHtPort.updateSubmittingOrderStatus(any())).thenReturn(res);
    PowerMockito.when(wsnimsHtPort.checkCloseOd(anyString())).thenReturn(true);
    PowerMockito.when(wsnimsHtPort.updateTuyenDocDao(anyString())).thenReturn(true);

    Datatable datatable = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("55");
    catItemDTO.setItemName("ItemName");
    List<CatItemDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(catItemDTO);
    datatable.setData(lstData);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString())
    ).thenReturn(datatable);

    ResultInSideDto actual = odBusiness.updateOdFromWeb(odSearchInsideDTO, files, "thanhlv12");

    Assert.assertEquals(actual.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void insertOdFromOtherSystem() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    OdDTOSearch odDTOSearch = Mockito.spy(OdDTOSearch.class);
    odDTOSearch.setStartTime("01/01/9999 00:00:00");
    odDTOSearch.setEndTime("01/01/9999 00:00:00");
    odDTOSearch.setCreateTime("01/01/9999 00:00:00");

    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setId("1");
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(odCommonBusiness.insertOdFromOtherSystem(any())).thenReturn(resultDTO);
    ResultDTO result = odBusiness.insertOdFromOtherSystem(odDTOSearch);
    Assert.assertEquals(result.getKey(), resultDTO.getKey());
  }

  @Test
  public void getListRDMRelationToUpdate() {
    Map<String, String> mapConfigProperty = Mockito.spy(HashMap.class);
    mapConfigProperty.put("url_rdm", "http://10.255.216.90:9092/RDM_API/api/rdm/ws/");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    Datatable result = odBusiness.getListRDMRelationToUpdate("1", "01/01/2019 00:00:00", "01/01/2020 00:00:00", 1, 1, 1D);
    Assert.assertNotNull(result);
  }

  @Test
  public void changeStatusOd() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    OdChangeStatusForm odChangeStatusForm = Mockito.spy(OdChangeStatusForm.class);
    odChangeStatusForm.setOdCode("1_1");
    odChangeStatusForm.setUserChange("1");
    odChangeStatusForm.setReasonChange("1");
    odChangeStatusForm.setSystemChange("1");
    odChangeStatusForm.setStatus("15");
    odChangeStatusForm.setCloseTime("01/01/2019 00:00:00");
    odChangeStatusForm.setFinishTime("01/01/2020 00:00:00");
    odChangeStatusForm.setIsClose("1");

    OdDTO od = Mockito.spy(OdDTO.class);
    od.setStatus(1L);
    PowerMockito.when(odRepository.findOdById(anyLong())).thenReturn(od);

    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUserId(1L);
    PowerMockito.when(odRepository.getUserByUserName(anyString())).thenReturn(us);
    ResultDTO result = odBusiness.changeStatusOd(odChangeStatusForm);
  }
}
