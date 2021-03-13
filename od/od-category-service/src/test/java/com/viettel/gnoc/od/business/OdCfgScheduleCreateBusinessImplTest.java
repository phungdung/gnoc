package com.viettel.gnoc.od.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.ReceiveUnitRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.od.dto.OdCfgScheduleCreateDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.od.repository.OdCfgScheduleCreateRepository;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OdCfgScheduleCreateBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, CommonExport.class, TicketProvider.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class OdCfgScheduleCreateBusinessImplTest {

  @InjectMocks
  OdCfgScheduleCreateBusinessImpl odCfgScheduleCreateBusiness;

  @Mock
  OdCfgScheduleCreateRepository odCfgScheduleCreateRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  ReceiveUnitRepository receiveUnitRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitRepository unitRepository;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void testGetListOdCfgScheduleCreateDTOSearchWeb_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(datatable.getTotal()).thenReturn(10);
    when(odCfgScheduleCreateRepository.getListOdCfgScheduleCreateDTOSearchWeb(any()))
        .thenReturn(datatable);
    setFinalStatic(OdCfgScheduleCreateBusinessImpl.class.getDeclaredField("log"), logger);
    Datatable result = odCfgScheduleCreateBusiness
        .getListOdCfgScheduleCreateDTOSearchWeb(new OdCfgScheduleCreateDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void testFindOdCfgScheduleCreateById_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    OdCfgScheduleCreateDTO odCfgScheduleCreateDTO = new OdCfgScheduleCreateDTO();
    when(odCfgScheduleCreateRepository.findOdCfgScheduleCreateById(any()))
        .thenReturn(odCfgScheduleCreateDTO);
    setFinalStatic(OdCfgScheduleCreateBusinessImpl.class.getDeclaredField("log"), logger);
    OdCfgScheduleCreateDTO result = odCfgScheduleCreateBusiness.findOdCfgScheduleCreateById(10L);
    assertEquals(odCfgScheduleCreateDTO, result);
  }
// QuangNV comment
  /*@Test
  public void testInsertOdCfgScheduleCreate_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(odCfgScheduleCreateRepository.insertOdCfgScheduleCreate(any()))
        .thenReturn(resultInSideDto);
    setFinalStatic(OdCfgScheduleCreateBusinessImpl.class.getDeclaredField("log"), logger);
    OdCfgScheduleCreateDTO odCfgScheduleCreateDTO = new OdCfgScheduleCreateDTO();
    List<MultipartFile> fileList = new ArrayList();
    ResultInSideDto result = odCfgScheduleCreateBusiness
        .insertOdCfgScheduleCreate(fileList, odCfgScheduleCreateDTO);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testUpdateOdCfgScheduleCreate_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(odCfgScheduleCreateRepository.updateOdCfgScheduleCreate(any()))
        .thenReturn(resultInSideDto);
    setFinalStatic(OdCfgScheduleCreateBusinessImpl.class.getDeclaredField("log"), logger);
    OdCfgScheduleCreateDTO odCfgScheduleCreateDTO = new OdCfgScheduleCreateDTO();
    List<MultipartFile> fileList = null;
    ResultInSideDto result = odCfgScheduleCreateBusiness
        .updateOdCfgScheduleCreate(fileList, odCfgScheduleCreateDTO);
    assertEquals(resultInSideDto, result);
  }*/

  @Test
  public void testInsertOdCfgScheduleCreate() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitToken);
    PowerMockito.when(
        odCfgScheduleCreateRepository
            .insertOdCfgScheduleCreate(any())
    ).thenReturn(expected);

    List<MultipartFile> listMulti = Mockito.spy(ArrayList.class);
    OdCfgScheduleCreateDTO odCfgScheduleCreateDTO = Mockito.spy(OdCfgScheduleCreateDTO.class);
    ResultInSideDto actual = odCfgScheduleCreateBusiness
        .insertOdCfgScheduleCreate(listMulti, odCfgScheduleCreateDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testUpdateOdCfgScheduleCreate() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitToken);
    PowerMockito.when(
        odCfgScheduleCreateRepository
            .updateOdCfgScheduleCreate(any())
    ).thenReturn(expected);

    List<MultipartFile> listMulti = Mockito.spy(ArrayList.class);
    List<GnocFileDto> listGnocFile = Mockito.spy(ArrayList.class);
    List<Long> idDeleteList = Mockito.spy(ArrayList.class);
    OdCfgScheduleCreateDTO odCfgScheduleCreateDTO = Mockito.spy(OdCfgScheduleCreateDTO.class);
    odCfgScheduleCreateDTO.setGnocFileDtos(listGnocFile);
    odCfgScheduleCreateDTO.setIdDeleteList(idDeleteList);
    ResultInSideDto actual = odCfgScheduleCreateBusiness
        .updateOdCfgScheduleCreate(listMulti, odCfgScheduleCreateDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDeleteOdCfgScheduleCreate_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(odCfgScheduleCreateRepository.deleteOdCfgScheduleCreate(any()))
        .thenReturn(resultInSideDto);
    setFinalStatic(OdCfgScheduleCreateBusinessImpl.class.getDeclaredField("log"), logger);
    ResultInSideDto result = odCfgScheduleCreateBusiness.deleteOdCfgScheduleCreate(123L);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testDeleteListOdCfgScheduleCreate_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(odCfgScheduleCreateRepository.deleteOdCfgScheduleCreate(any()))
        .thenReturn(resultInSideDto);
    setFinalStatic(OdCfgScheduleCreateBusinessImpl.class.getDeclaredField("log"), logger);
    Long[] input = {10L, 10L};
    ResultInSideDto result = odCfgScheduleCreateBusiness
        .deleteListOdCfgScheduleCreate(Arrays.asList(input));
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testGetSequenseOdCfgScheduleCreate_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    when(odCfgScheduleCreateRepository.getSequenseOdCfgScheduleCreate(any()))
        .thenReturn("getSequenseOdCfgScheduleCreate");
    setFinalStatic(OdCfgScheduleCreateBusinessImpl.class.getDeclaredField("log"), logger);
    String result = odCfgScheduleCreateBusiness.getSequenseOdCfgScheduleCreate();
    assertEquals("getSequenseOdCfgScheduleCreate", result);
  }

  @Test
  public void testSetMapOdTypeName_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CatItemDTO> lstOdType = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("Test");
    lstOdType.add(catItemDTO);
    when(catItemRepository.getDataItem(CATEGORY.OD_TYPE)).thenReturn(lstOdType);
    setFinalStatic(OdCfgScheduleCreateBusinessImpl.class.getDeclaredField("log"), logger);
    odCfgScheduleCreateBusiness.setMapOdTypeName();
  }

  @Test
  public void testSetMapPriorityName_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CatItemDTO> lstPriority = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("Test");
    lstPriority.add(catItemDTO);
    when(catItemRepository.getDataItem(CATEGORY.OD_PRIORITY)).thenReturn(lstPriority);
    setFinalStatic(OdCfgScheduleCreateBusinessImpl.class.getDeclaredField("log"), logger);
    odCfgScheduleCreateBusiness.setMapPriorityName();
  }

  @Test
  public void testSetMapScheduleName_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CatItemDTO> lstSchedule = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("Test");
    lstSchedule.add(catItemDTO);
    when(catItemRepository.getDataItem(CATEGORY.OD_SCHEDULE)).thenReturn(lstSchedule);
    setFinalStatic(OdCfgScheduleCreateBusinessImpl.class.getDeclaredField("log"), logger);
  }

  @Test
  public void testSetMapReceiveUnitName_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ReceiveUnitDTO> lstReceiveUnit = new ArrayList<>();
    ReceiveUnitDTO dto = new ReceiveUnitDTO();
    dto.setUnitId(1L);
    dto.setUnitName("Test");
    lstReceiveUnit.add(dto);
    when(receiveUnitRepository.getListReceiveUnit()).thenReturn(lstReceiveUnit);
    setFinalStatic(OdCfgScheduleCreateBusinessImpl.class.getDeclaredField("log"), logger);
    odCfgScheduleCreateBusiness.setMapReceiveUnitName();
  }

  @Test
  public void testGetTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT TEST");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("ItemName");
    catItemDTO.setItemId(1L);
    List<CatItemDTO> lstOdGroupType = Mockito.spy(ArrayList.class);
    lstOdGroupType.add(catItemDTO);

    OdTypeDTO odTypeDTO = Mockito.spy(OdTypeDTO.class);
    odTypeDTO.setOdTypeName("OdTypeName");
    odTypeDTO.setOdTypeId(2L);
    List<OdTypeDTO> odTypeDTOList = Mockito.spy(ArrayList.class);
    odTypeDTOList.add(odTypeDTO);

    ReceiveUnitDTO receiveUnitDTO = Mockito.spy(ReceiveUnitDTO.class);
    receiveUnitDTO.setUnitName("UnitName");
    receiveUnitDTO.setUnitCode("123456");
    List<ReceiveUnitDTO> lstReceiveUnit = Mockito.spy(ArrayList.class);
    lstReceiveUnit.add(receiveUnitDTO);

    PowerMockito.when(
        catItemRepository.getDataItem(anyString())
    ).thenReturn(lstOdGroupType);
    PowerMockito.when(
        odCfgScheduleCreateRepository
            .getListOdTypeByGroupId(anyLong())
    ).thenReturn(odTypeDTOList);
    PowerMockito.when(
        odCfgScheduleCreateRepository.getListOdType()
    ).thenReturn(odTypeDTOList);
    PowerMockito.when(
        receiveUnitRepository.getListReceiveUnit()
    ).thenReturn(lstReceiveUnit);

    File actual = odCfgScheduleCreateBusiness.getTemplate();

    Assert.assertNotNull(actual);
  }

  @Test
  public void testExportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("UNIT_TEST");

    OdCfgScheduleCreateDTO odCfgScheduleCreateDTO = Mockito.spy(OdCfgScheduleCreateDTO.class);
    odCfgScheduleCreateDTO.setOdName("OdName");
    odCfgScheduleCreateDTO.setOdDescription("OdDescription");
    odCfgScheduleCreateDTO.setOdGroupTypeName("OdGroupTypeName");
    odCfgScheduleCreateDTO.setOdTypeName("OdTypeName");
    odCfgScheduleCreateDTO.setOdPriorityName("OdPriorityName");
    odCfgScheduleCreateDTO.setScheduleName("ScheduleName");
    odCfgScheduleCreateDTO.setReceiveUnitName("ReceiveUnitName");
    List<OdCfgScheduleCreateDTO> scheduleCreateDTOList = Mockito.spy(ArrayList.class);
    scheduleCreateDTOList.add(odCfgScheduleCreateDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(scheduleCreateDTOList);

    PowerMockito.when(
        odCfgScheduleCreateRepository
            .getListDataExport(any())
    ).thenReturn(datatable);

    File actual = odCfgScheduleCreateBusiness.exportData(odCfgScheduleCreateDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void testImportData_01() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitToken);

    MultipartFile multipartFile = null;
    List<MultipartFile> listMulti = Mockito.spy(ArrayList.class);
    ResultInSideDto actual = odCfgScheduleCreateBusiness
        .importData(multipartFile, listMulti);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportData_02() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    String filePath = "/temp";
    File fileImp = new File(filePath);
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitToken);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp,
            0,
            4,
            0,
            8,
            1000
        )
    ).thenReturn(lstHeader);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> listMulti = Mockito.spy(ArrayList.class);
    ResultInSideDto actual = odCfgScheduleCreateBusiness
        .importData(multipartFile, listMulti);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportData_03() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitToken);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);
    Object[] objects = new Object[]{"1", "1(*)", "1", "1", "1(*)", "1(*)", "1(*)", "1(*)"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp,
            0,
            4,
            0,
            8,
            1000
        )
    ).thenReturn(lstHeader);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> listMulti = Mockito.spy(ArrayList.class);
    ResultInSideDto actual = odCfgScheduleCreateBusiness
        .importData(multipartFile, listMulti);

    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void testImportData_04() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitToken);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);
    Object[] objects = new Object[]{"1", "1(*)", "1", "1", "1(*)", "1(*)", "1(*)", "1(*)"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 1501; i++) {
      lstData.add(objects);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp,
            0,
            4,
            0,
            8,
            1000
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp,
            0,
            5,
            0,
            8,
            1000
        )
    ).thenReturn(lstData);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> listMulti = Mockito.spy(ArrayList.class);
    ResultInSideDto actual = odCfgScheduleCreateBusiness
        .importData(multipartFile, listMulti);

    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportData_05() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitToken);
    PowerMockito.when(
        odCfgScheduleCreateRepository
            .insertOdCfgScheduleCreate(any())
    ).thenReturn(expected);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);
    Object[] objects = new Object[]{"1", "1(*)", "1", "1", "1(*)", "1(*)", "1(*)", "1(*)"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    Object[] objects1 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp,
            0,
            4,
            0,
            8,
            1000
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImp,
            0,
            5,
            0,
            8,
            1000
        )
    ).thenReturn(lstData);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("1");
    catItemDTO.setItemValue("11111");
    List<CatItemDTO> lstOdGroupType = Mockito.spy(ArrayList.class);
    lstOdGroupType.add(catItemDTO);
    PowerMockito.when(
        catItemRepository
            .getDataItem(anyString())
    ).thenReturn(lstOdGroupType);
    OdTypeDTO odTypeDTO = Mockito.spy(OdTypeDTO.class);
    odTypeDTO.setOdTypeId(2L);
    odTypeDTO.setOdTypeName("1");
    List<OdTypeDTO> lstOdType = Mockito.spy(ArrayList.class);
    lstOdType.add(odTypeDTO);
    PowerMockito.when(
        odCfgScheduleCreateRepository.getListOdType()
    ).thenReturn(lstOdType);
    ReceiveUnitDTO receiveUnitDTO = Mockito.spy(ReceiveUnitDTO.class);
    receiveUnitDTO.setUnitId(3L);
    receiveUnitDTO.setUnitName("UnitName");
    receiveUnitDTO.setUnitCode("UnitCode");
    List<ReceiveUnitDTO> lstReceiveUnit = Mockito.spy(ArrayList.class);
    lstReceiveUnit.add(receiveUnitDTO);
    PowerMockito.when(
        receiveUnitRepository.getListReceiveUnit()
    ).thenReturn(lstReceiveUnit);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> listMulti = Mockito.spy(ArrayList.class);
    ResultInSideDto actual = odCfgScheduleCreateBusiness
        .importData(multipartFile, listMulti);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }
}
