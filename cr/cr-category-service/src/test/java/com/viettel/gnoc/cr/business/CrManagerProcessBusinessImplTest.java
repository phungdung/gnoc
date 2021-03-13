package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrOcsScheduleDTO;
import com.viettel.gnoc.cr.dto.CrProcessDeptGroupDTO;
import com.viettel.gnoc.cr.dto.CrProcessGroup;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.CrProcessTemplateDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.DeviceTypesDTO;
import com.viettel.gnoc.cr.dto.GroupUnitDTO;
import com.viettel.gnoc.cr.dto.GroupUnitDetailDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.TempImportDTO;
import com.viettel.gnoc.cr.repository.CfgTempImportRepository;
import com.viettel.gnoc.cr.repository.CrImpactSegmentRepository;
import com.viettel.gnoc.cr.repository.CrManagerProcessRepository;
import com.viettel.gnoc.cr.repository.DeviceTypesRepository;
import com.viettel.gnoc.cr.repository.GroupUnitDetailRepository;
import com.viettel.gnoc.cr.repository.GroupUnitRepository;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrManagerProcessBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, CommonExport.class, TicketProvider.class, XSSFWorkbook.class,
    SXSSFWorkbook.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrManagerProcessBusinessImplTest {

  @InjectMocks
  CrManagerProcessBusinessImpl crManagerProcessBusiness;

  @Mock
  protected CrManagerProcessRepository crManagerProcessRepository;

  @Mock
  protected DeviceTypesRepository deviceTypesRepository;

  @Mock
  protected CrImpactSegmentRepository crImpactSegmentRepository;

  @Mock
  protected CfgTempImportRepository cfgTempImportRepository;

  @Mock
  LanguageExchangeRepository languageExchangeRepository;

  @Mock
  protected GroupUnitRepository groupUnitRepository;

  @Mock
  protected GroupUnitDetailRepository groupUnitDetailRepository;

  @Mock
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  protected CommonRepository commonRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Test
  public void testGetListSearchCrProcess() {
    Datatable expected = Mockito.spy(Datatable.class);
    PowerMockito.when(
        crManagerProcessRepository.getListSearchCrProcess(any())
    ).thenReturn(expected);

    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    Datatable actual = crManagerProcessBusiness
        .getListSearchCrProcess(crProcessDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testActionGetListProcessType() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrProcessInsideDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crManagerProcessRepository.actionGetListProcessType(any())
    ).thenReturn(expected);

    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    List<CrProcessInsideDTO> actual = crManagerProcessBusiness
        .actionGetListProcessType(crProcessDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetListCrProcessCBB() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<ItemDataCRInside> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crManagerProcessRepository.getListCrProcessCBB(any())
    ).thenReturn(expected);

    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    List<ItemDataCRInside> actual = crManagerProcessBusiness
        .getListCrProcessCBB(crProcessDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetRootCrProcess() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrProcessInsideDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crManagerProcessRepository.getRootCrProcess()
    ).thenReturn(expected);

    List<CrProcessInsideDTO> actual = crManagerProcessBusiness
        .getRootCrProcess();

    Assert.assertEquals(expected, actual);
  }

//  @Test
//  public void getLstFileFromProcessId() {
//  }

  @Test
  public void testGetLstUnitFromProcessId() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrProcessGroup> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crManagerProcessRepository.getLstUnitFromProcessId(anyLong())
    ).thenReturn(expected);

    List<CrProcessGroup> actual = crManagerProcessBusiness
        .getLstUnitFromProcessId(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetLstAllChildrenByProcessId() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrProcessInsideDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crManagerProcessRepository.getLstAllChildrenByProcessId(anyLong())
    ).thenReturn(expected);

    List<CrProcessInsideDTO> actual = crManagerProcessBusiness
        .getLstAllChildrenByProcessId(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetParentByProcessId() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    CrProcessInsideDTO expected = Mockito.spy(CrProcessInsideDTO.class);
    PowerMockito.when(
        crManagerProcessRepository.getParentByProcessId(anyLong())
    ).thenReturn(expected);

    CrProcessInsideDTO actual = crManagerProcessBusiness
        .getParentByProcessId(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetCrProcessDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    CrProcessInsideDTO expected = Mockito.spy(CrProcessInsideDTO.class);
    PowerMockito.when(
        crManagerProcessRepository.getCrProcessDetail(anyLong())
    ).thenReturn(expected);

    CrProcessInsideDTO actual = crManagerProcessBusiness
        .getCrProcessDetail(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetCrProcessById() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    CrProcessInsideDTO expected = Mockito.spy(CrProcessInsideDTO.class);
    PowerMockito.when(
        crManagerProcessRepository.getCrProcessById(anyLong())
    ).thenReturn(expected);

    CrProcessInsideDTO actual = crManagerProcessBusiness
        .getCrProcessById(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetListCrProcessDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrProcessInsideDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crManagerProcessRepository
            .getListCrProcessDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(expected);

    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    List<CrProcessInsideDTO> actual = crManagerProcessBusiness
        .getListCrProcessDTO(crProcessDTO, 1, 1, "test", "test");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDeleteGroupUnitOrFileByProcessId() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        crManagerProcessRepository.deleteGroupUnitOrFileByProcessId(anyLong())
    ).thenReturn(expected);

    ResultInSideDto actual = crManagerProcessBusiness
        .deleteGroupUnitOrFileByProcessId(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDeleteAllChildByParent() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        crManagerProcessRepository.deleteAllChildByParent(anyLong())
    ).thenReturn(expected);

    ResultInSideDto actual = crManagerProcessBusiness
        .deleteAllChildByParent(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDeleteFileAndDataWhenChangeProcess() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        crManagerProcessRepository.deleteFileAndDataWhenChangeProcess(anyString())
    ).thenReturn(expected);

    ResultInSideDto actual = crManagerProcessBusiness
        .deleteFileAndDataWhenChangeProcess("11");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testSaveCrProcessWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        crManagerProcessRepository.saveCrProcessWo(any())
    ).thenReturn(expected);

    CrProcessWoDTO crProcessWoDTO = Mockito.spy(CrProcessWoDTO.class);
    ResultInSideDto actual = crManagerProcessBusiness
        .saveCrProcessWo(crProcessWoDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDeleteListCrProcessWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        crManagerProcessRepository.deleteListCrProcessWo(any())
    ).thenReturn(expected);

    List<Long> lstCrProcessWoId = Mockito.spy(ArrayList.class);
    ResultInSideDto actual = crManagerProcessBusiness
        .deleteListCrProcessWo(lstCrProcessWoId);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetLstWoFromProcessId() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrProcessWoDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crManagerProcessRepository.getLstWoFromProcessId(anyLong())
    ).thenReturn(expected);

    List<CrProcessWoDTO> actual = crManagerProcessBusiness
        .getLstWoFromProcessId(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetListCrOcsScheduleDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrOcsScheduleDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crManagerProcessRepository.getListCrOcsScheduleDTO(any())
    ).thenReturn(expected);

    CrOcsScheduleDTO crOcsScheduleDTO = Mockito.spy(CrOcsScheduleDTO.class);
    List<CrOcsScheduleDTO> actual = crManagerProcessBusiness
        .getListCrOcsScheduleDTO(crOcsScheduleDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testInsertOrUpdateCrOcsScheduleDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        crManagerProcessRepository.insertOrUpdateCrOcsScheduleDTO(any())
    ).thenReturn(expected);

    CrOcsScheduleDTO crOcsScheduleDTO = Mockito.spy(CrOcsScheduleDTO.class);
    ResultInSideDto actual = crManagerProcessBusiness
        .insertOrUpdateCrOcsScheduleDTO(crOcsScheduleDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testInsertOrUpdateCrProcessDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(
        crManagerProcessRepository
            .insertOrUpdateCrProcessDTO(any())
    ).thenReturn(resultInSideDto);

    CrProcessTemplateDTO crProcessTemplateDTO = Mockito.spy(CrProcessTemplateDTO.class);
    List<CrProcessTemplateDTO> listCrProcessTemplate = Mockito.spy(ArrayList.class);
    listCrProcessTemplate.add(crProcessTemplateDTO);

    CrProcessWoDTO crProcessWoDTO = Mockito.spy(CrProcessWoDTO.class);
    List<CrProcessWoDTO> listCrProcessWo = Mockito.spy(ArrayList.class);
    listCrProcessWo.add(crProcessWoDTO);

    CrProcessDeptGroupDTO crProcessDeptGroupDTO = Mockito.spy(CrProcessDeptGroupDTO.class);
    List<CrProcessDeptGroupDTO> listCrProcessDeptGroup = Mockito.spy(ArrayList.class);
    listCrProcessDeptGroup.add(crProcessDeptGroupDTO);

    CrProcessInsideDTO checkLevel = Mockito.spy(CrProcessInsideDTO.class);
    checkLevel.setCrProcessLevel(2L);
    checkLevel.setListCrProcessTemplate(listCrProcessTemplate);
    checkLevel.setListCrProcessWo(listCrProcessWo);
    checkLevel.setListCrProcessDeptGroup(listCrProcessDeptGroup);
    PowerMockito.when(
        crManagerProcessRepository.getCrProcessById(anyLong())
    ).thenReturn(checkLevel);
    PowerMockito.when(
        crManagerProcessRepository
            .getCrProcessDetail(anyLong())
    ).thenReturn(checkLevel);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessDTO.setParentId(1L);
    ResultInSideDto actual = crManagerProcessBusiness
        .insertOrUpdateCrProcessDTO(crProcessDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void testInsertOrUpdateCrProcessDTO_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(
        crManagerProcessRepository
            .insertOrUpdateCrProcessDTO(any())
    ).thenReturn(resultInSideDto);

    CrProcessTemplateDTO crProcessTemplateDTO = Mockito.spy(CrProcessTemplateDTO.class);
    List<CrProcessTemplateDTO> listCrProcessTemplate = Mockito.spy(ArrayList.class);
    listCrProcessTemplate.add(crProcessTemplateDTO);

    CrProcessWoDTO crProcessWoDTO = Mockito.spy(CrProcessWoDTO.class);
    List<CrProcessWoDTO> listCrProcessWo = Mockito.spy(ArrayList.class);
    listCrProcessWo.add(crProcessWoDTO);

    CrProcessDeptGroupDTO crProcessDeptGroupDTO = Mockito.spy(CrProcessDeptGroupDTO.class);
    List<CrProcessDeptGroupDTO> listCrProcessDeptGroup = Mockito.spy(ArrayList.class);
    listCrProcessDeptGroup.add(crProcessDeptGroupDTO);

    CrProcessInsideDTO checkLevel = Mockito.spy(CrProcessInsideDTO.class);
    checkLevel.setCrProcessLevel(2L);
    checkLevel.setListCrProcessTemplate(listCrProcessTemplate);
    checkLevel.setListCrProcessWo(listCrProcessWo);
    checkLevel.setListCrProcessDeptGroup(listCrProcessDeptGroup);
    PowerMockito.when(
        crManagerProcessRepository.getCrProcessById(anyLong())
    ).thenReturn(checkLevel);
    PowerMockito.when(
        crManagerProcessRepository
            .getCrProcessDetail(anyLong())
    ).thenReturn(checkLevel);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessDTO.setParentId(1L);
    crProcessDTO.setCrProcessId(1L);
    ResultInSideDto actual = crManagerProcessBusiness
        .insertOrUpdateCrProcessDTO(crProcessDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void testDeleteListCrOcsScheduleDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        crManagerProcessRepository.deleteListCrOcsScheduleDTO(any())
    ).thenReturn(expected);

    List<Long> crOcsScheduleDTOs = Mockito.spy(ArrayList.class);
    ResultInSideDto actual = crManagerProcessBusiness
        .deleteListCrOcsScheduleDTO(crOcsScheduleDTOs);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testSaveAllList() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(
        crManagerProcessRepository.saveAllList(any())
    ).thenReturn(resultInSideDto);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    ResultInSideDto actual = crManagerProcessBusiness
        .saveAllList(crProcessDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void testGetcCrProcessWo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    CrProcessWoDTO expected = Mockito.spy(CrProcessWoDTO.class);
    PowerMockito.when(
        crManagerProcessRepository.getcCrProcessWo(anyLong())
    ).thenReturn(expected);

    CrProcessWoDTO actual = crManagerProcessBusiness
        .getcCrProcessWo(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testExportData() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    List<CrProcessInsideDTO> lstCrEx = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crManagerProcessRepository
            .getListDataExport(any())
    ).thenReturn(lstCrEx);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    File actual = crManagerProcessBusiness.exportData(crProcessDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void testGetListWoType() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<WoTypeInsideDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crManagerProcessRepository.getListWoType()
    ).thenReturn(expected);

    List<WoTypeInsideDTO> actual = crManagerProcessBusiness
        .getListWoType();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetLstFileTemplate() {
    Datatable expected = Mockito.spy(Datatable.class);
    PowerMockito.when(
        crManagerProcessRepository.getLstFileTemplate(any())
    ).thenReturn(expected);

    CrProcessGroup crProcessGroup = Mockito.spy(CrProcessGroup.class);
    Datatable actual = crManagerProcessBusiness
        .getLstFileTemplate(crProcessGroup);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDeleteListCrProcess() {
    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        crManagerProcessRepository.deleteListCrProcess(any())
    ).thenReturn(expected);

    List<Long> crProcessIds = Mockito.spy(ArrayList.class);
    ResultInSideDto actual = crManagerProcessBusiness
        .deleteListCrProcess(crProcessIds);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDeleteCrProcess() {
    PowerMockito.mockStatic(TicketProvider.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        crManagerProcessRepository.deleteCrProcessDTO(any())
    ).thenReturn(expected);

    List<Long> listChild = Mockito.spy(ArrayList.class);
    listChild.add(1L);
    PowerMockito.when(
        crManagerProcessRepository.deleteChildByParent(anyLong())
    ).thenReturn(listChild);

    ResultInSideDto actual = crManagerProcessBusiness
        .deleteCrProcess(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetCrProcessWoDTO() {
    CrProcessWoDTO expected = Mockito.spy(CrProcessWoDTO.class);
    PowerMockito.when(
        crManagerProcessRepository.getCrProcessWoDTO(anyLong())
    ).thenReturn(expected);

    CrProcessWoDTO actual = crManagerProcessBusiness
        .getCrProcessWoDTO(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetTemplateSepical() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");
    PowerMockito.when(I18n.getLanguage("CrProcessManager.export.title")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("wo.typeWork.title")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("deviceTypes.title_export")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("ImpactSegment.export.title")).thenReturn("4");
    PowerMockito.when(I18n.getLanguage("GroupUnit.export")).thenReturn("5");
    PowerMockito.when(I18n.getLanguage("GroupUnitDetail.export")).thenReturn("6");
    PowerMockito.when(I18n.getLanguage("TempImport.export")).thenReturn("7");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    DeviceTypesDTO deviceTypesDTO = Mockito.spy(DeviceTypesDTO.class);
    deviceTypesDTO.setDeviceTypeCode("1");
    deviceTypesDTO.setDeviceTypeName("DeviceTypeName");
    List<DeviceTypesDTO> deviceList = Mockito.spy(ArrayList.class);
    deviceList.add(deviceTypesDTO);

    ImpactSegmentDTO impactSegmentDTO = Mockito.spy(ImpactSegmentDTO.class);
    impactSegmentDTO.setImpactSegmentCode("1");
    impactSegmentDTO.setImpactSegmentName("ImpactSegmentName");
    impactSegmentDTO.setAppliedSystem(1L);
    List<ImpactSegmentDTO> impactSegmentList = Mockito.spy(ArrayList.class);
    impactSegmentList.add(impactSegmentDTO);

    GroupUnitDTO groupUnitDTO = Mockito.spy(GroupUnitDTO.class);
    groupUnitDTO.setGroupUnitCode("1");
    groupUnitDTO.setGroupUnitName("GroupUnitName");
    List<GroupUnitDTO> groupUnitList = Mockito.spy(ArrayList.class);
    groupUnitList.add(groupUnitDTO);

    GroupUnitDetailDTO groupUnitDetailDTO = Mockito.spy(GroupUnitDetailDTO.class);
    groupUnitDetailDTO.setGroupUnitId(1L);
    groupUnitDetailDTO.setUnitId(1L);
    List<GroupUnitDetailDTO> groupUnitDetailList = Mockito.spy(ArrayList.class);
    groupUnitDetailList.add(groupUnitDetailDTO);

    TempImportDTO tempImportDTO = Mockito.spy(TempImportDTO.class);
    tempImportDTO.setCode("1");
    tempImportDTO.setName("Name");
    List<TempImportDTO> tempImportList = Mockito.spy(ArrayList.class);
    tempImportList.add(tempImportDTO);

    PowerMockito.when(
        deviceTypesRepository.getDeviceTypesCBB()
    ).thenReturn(deviceList);
    PowerMockito.when(
        crImpactSegmentRepository
            .getListImpactSegmentDTO(any())
    ).thenReturn(impactSegmentList);
    PowerMockito.when(
        groupUnitRepository.getAllListGroupUnitDTO(any())
    ).thenReturn(groupUnitList);
    PowerMockito.when(
        groupUnitDetailRepository
            .getAllListGroupUnitDetailDTO(any())
    ).thenReturn(groupUnitDetailList);
    PowerMockito.when(
        crManagerProcessRepository.getListTempImportDTO(anyString())
    ).thenReturn(tempImportList);

    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    woTypeInsideDTO.setWoTypeCode("2");
    woTypeInsideDTO.setWoTypeName("WoTypeName");
    List<WoTypeInsideDTO> listTypeWo = Mockito.spy(ArrayList.class);
    listTypeWo.add(woTypeInsideDTO);
    PowerMockito.when(
        woCategoryServiceProxy.getListWoTypeDTO(any())).thenReturn(listTypeWo);

    File actual = crManagerProcessBusiness.getTemplateSepical();

    Assert.assertNotNull(actual);
  }

  @Test
  public void testGetAllCrProcess() {
    List<CrProcessInsideDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crManagerProcessRepository.getAllCrProcess(anyLong())
    ).thenReturn(expected);

    CrProcessGroup crProcessGroup = Mockito.spy(CrProcessGroup.class);
    List<CrProcessInsideDTO> actual = crManagerProcessBusiness
        .getAllCrProcess(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testImportData_01() {
    MultipartFile multipartFile = null;
    ResultInSideDto actual = crManagerProcessBusiness.importData(multipartFile);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportData_02() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    List<Object[]> lstHeaderSheet2 = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            15,
            2
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            1,
            7,
            0,
            8,
            2
        )
    ).thenReturn(lstHeaderSheet2);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = crManagerProcessBusiness.importData(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportData_03() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects1 = new Object[]{"1", "1 (*)", "1 (*)", "1", "1 (*)", "1 (*)",
        "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1", "1", "1", "1", "1", "1 (*)"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects1);
    List<Object[]> lstHeaderSheet2 = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            15,
            2
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            1,
            7,
            0,
            8,
            2
        )
    ).thenReturn(lstHeaderSheet2);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = crManagerProcessBusiness.importData(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportData_04() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects1 = new Object[]{"1", "1 (*)", "1 (*)", "1", "1 (*)", "1 (*)",
        "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1", "1", "1", "1", "1", "1 (*)"};
    Object[] objects2 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects1);
    List<Object[]> lstHeaderSheet2 = Mockito.spy(ArrayList.class);
    lstHeaderSheet2.add(objects2);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 15, 2
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 1, 7, 0, 8, 2
        )
    ).thenReturn(lstHeaderSheet2);

    Object[] objects3 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1", "1", "1", "1"};
    Object[] objects4 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 1501; i++) {
      lstData.add(objects3);
    }
    List<Object[]> lstDataSheet2 = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 8, 0, 15, 1000
        )
    ).thenReturn(lstData);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 1, 8, 0, 8, 1000
        )
    ).thenReturn(lstDataSheet2);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = crManagerProcessBusiness.importData(multipartFile);

    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportData_05() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects1 = new Object[]{"1", "1 (*)", "1 (*)", "1", "1 (*)", "1 (*)",
        "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1", "1", "1", "1", "1", "1 (*)"};
    Object[] objects2 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects1);
    List<Object[]> lstHeaderSheet2 = Mockito.spy(ArrayList.class);
    lstHeaderSheet2.add(objects2);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 15, 2
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 1, 7, 0, 8, 2
        )
    ).thenReturn(lstHeaderSheet2);

    Object[] objects3 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1", "1", "1", "1"};
    Object[] objects4 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects3);
    List<Object[]> lstDataSheet2 = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 1501; i++) {
      lstDataSheet2.add(objects4);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 8, 0, 15, 1000
        )
    ).thenReturn(lstData);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 1, 8, 0, 8, 1000
        )
    ).thenReturn(lstDataSheet2);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = crManagerProcessBusiness.importData(multipartFile);

    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportData_06() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects1 = new Object[]{"1", "1 (*)", "1 (*)", "1", "1 (*)", "1 (*)",
        "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1", "1", "1", "1", "1", "1 (*)"};
    Object[] objects2 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects1);
    List<Object[]> lstHeaderSheet2 = Mockito.spy(ArrayList.class);
    lstHeaderSheet2.add(objects2);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 15, 2
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 1, 7, 0, 8, 2
        )
    ).thenReturn(lstHeaderSheet2);

    Object[] objects3 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1", "1", "1", "1"};
    Object[] objects4 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects3);
    List<Object[]> lstDataSheet2 = Mockito.spy(ArrayList.class);
    lstDataSheet2.add(objects4);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 8, 0, 15, 1000
        )
    ).thenReturn(lstData);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 1, 8, 0, 8, 1000
        )
    ).thenReturn(lstDataSheet2);

    DataItemDTO dataItemDTO = Mockito.spy(DataItemDTO.class);
    dataItemDTO.setItemName("ItemName");
    dataItemDTO.setItemId("1");
    List<DataItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(dataItemDTO);
    PowerMockito.when(commonRepository.getListCombobox(any())).thenReturn(list);

    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessDTO.setCrProcessId(1L);
    crProcessDTO.setCrProcessLevel(2L);
    PowerMockito.when(
        crManagerProcessRepository
            .getCrProcessLevelByCode(anyString())
    ).thenReturn(crProcessDTO);
    PowerMockito.when(
        crManagerProcessRepository.checkCrProcessExist(any())
    ).thenReturn(crProcessDTO);
    PowerMockito.when(
        crManagerProcessRepository
            .getCrProcessById(anyLong())
    ).thenReturn(crProcessDTO);

    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    woTypeInsideDTO.setWoTypeCode("2");
    woTypeInsideDTO.setWoTypeName("WoTypeName");
    List<WoTypeInsideDTO> listTypeWo = Mockito.spy(ArrayList.class);
    listTypeWo.add(woTypeInsideDTO);
    PowerMockito.when(
        woCategoryServiceProxy.getListWoTypeDTO(any())).thenReturn(listTypeWo);

    PowerMockito.when(I18n.getLanguage("CrProcessManager.export.title")).thenReturn("10");
    PowerMockito.when(I18n.getLanguage("wo.typeWork.title")).thenReturn("20");
    PowerMockito.when(I18n.getLanguage("deviceTypes.title_export")).thenReturn("30");
    PowerMockito.when(I18n.getLanguage("ImpactSegment.export.title")).thenReturn("40");
    PowerMockito.when(I18n.getLanguage("GroupUnit.export")).thenReturn("50");
    PowerMockito.when(I18n.getLanguage("GroupUnitDetail.export")).thenReturn("60");
    PowerMockito.when(I18n.getLanguage("TempImport.export")).thenReturn("70");

    DeviceTypesDTO deviceTypesDTO = Mockito.spy(DeviceTypesDTO.class);
    deviceTypesDTO.setDeviceTypeCode("1");
    deviceTypesDTO.setDeviceTypeName("DeviceTypeName");
    List<DeviceTypesDTO> deviceList = Mockito.spy(ArrayList.class);
    deviceList.add(deviceTypesDTO);

    ImpactSegmentDTO impactSegmentDTO = Mockito.spy(ImpactSegmentDTO.class);
    impactSegmentDTO.setImpactSegmentCode("1");
    impactSegmentDTO.setImpactSegmentName("ImpactSegmentName");
    impactSegmentDTO.setAppliedSystem(1L);
    List<ImpactSegmentDTO> impactSegmentList = Mockito.spy(ArrayList.class);
    impactSegmentList.add(impactSegmentDTO);

    GroupUnitDTO groupUnitDTO = Mockito.spy(GroupUnitDTO.class);
    groupUnitDTO.setGroupUnitCode("1");
    groupUnitDTO.setGroupUnitName("GroupUnitName");
    List<GroupUnitDTO> groupUnitList = Mockito.spy(ArrayList.class);
    groupUnitList.add(groupUnitDTO);

    GroupUnitDetailDTO groupUnitDetailDTO = Mockito.spy(GroupUnitDetailDTO.class);
    groupUnitDetailDTO.setGroupUnitId(1L);
    groupUnitDetailDTO.setUnitId(1L);
    List<GroupUnitDetailDTO> groupUnitDetailList = Mockito.spy(ArrayList.class);
    groupUnitDetailList.add(groupUnitDetailDTO);

    TempImportDTO tempImportDTO = Mockito.spy(TempImportDTO.class);
    tempImportDTO.setCode("1");
    tempImportDTO.setName("Name");
    List<TempImportDTO> tempImportList = Mockito.spy(ArrayList.class);
    tempImportList.add(tempImportDTO);

    PowerMockito.when(
        deviceTypesRepository.getDeviceTypesCBB()
    ).thenReturn(deviceList);
    PowerMockito.when(
        crImpactSegmentRepository
            .getListImpactSegmentDTO(any())
    ).thenReturn(impactSegmentList);
    PowerMockito.when(
        groupUnitRepository.getAllListGroupUnitDTO(any())
    ).thenReturn(groupUnitList);
    PowerMockito.when(
        groupUnitDetailRepository
            .getAllListGroupUnitDetailDTO(any())
    ).thenReturn(groupUnitDetailList);
    PowerMockito.when(
        crManagerProcessRepository.getListTempImportDTO(anyString())
    ).thenReturn(tempImportList);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = crManagerProcessBusiness.importData(multipartFile);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testImportData_07() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(
        I18n.getLanguage("CrProcessManager.action.NO")
    ).thenReturn("1");
    PowerMockito.when(
        I18n.getLanguage("CrProcessManager.action.YES")
    ).thenReturn("2");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects1 = new Object[]{"1", "1 (*)", "1 (*)", "1", "1 (*)", "1 (*)",
        "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1", "1", "1", "1", "1", "1 (*)"};
    Object[] objects2 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects1);
    List<Object[]> lstHeaderSheet2 = Mockito.spy(ArrayList.class);
    lstHeaderSheet2.add(objects2);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 15, 2
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 1, 7, 0, 8, 2
        )
    ).thenReturn(lstHeaderSheet2);

    Object[] objects3 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1", "1", "1", "1"};
    Object[] objects4 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects3);
    List<Object[]> lstDataSheet2 = Mockito.spy(ArrayList.class);
    lstDataSheet2.add(objects4);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 8, 0, 15, 1000
        )
    ).thenReturn(lstData);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 1, 8, 0, 8, 1000
        )
    ).thenReturn(lstDataSheet2);

    DataItemDTO dataItemDTO = Mockito.spy(DataItemDTO.class);
    dataItemDTO.setItemName("ItemName");
    dataItemDTO.setItemId("1");
    List<DataItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(dataItemDTO);
    PowerMockito.when(commonRepository.getListCombobox(any())).thenReturn(list);

    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessDTO.setCrProcessId(1L);
    crProcessDTO.setCrProcessLevel(2L);
    PowerMockito.when(
        crManagerProcessRepository
            .getCrProcessLevelByCode(anyString())
    ).thenReturn(crProcessDTO);
    PowerMockito.when(
        crManagerProcessRepository.checkCrProcessExist(any())
    ).thenReturn(crProcessDTO);
    PowerMockito.when(
        crManagerProcessRepository
            .getCrProcessById(anyLong())
    ).thenReturn(crProcessDTO);

    CrProcessTemplateDTO crProcessTemplateDTO = Mockito.spy(CrProcessTemplateDTO.class);
    crProcessTemplateDTO.setCode("12121212");
    List<CrProcessTemplateDTO> templateList = Mockito.spy(ArrayList.class);
    templateList.add(crProcessTemplateDTO);
    CrProcessInsideDTO level2Rs = Mockito.spy(CrProcessInsideDTO.class);
    level2Rs.setCrProcessId(11L);
    level2Rs.setListCrProcessTemplate(templateList);
    PowerMockito.when(
        crManagerProcessRepository
            .getCrProcessDetail(anyLong())
    ).thenReturn(level2Rs);

    boolean checkParent = true;
    PowerMockito.when(
        crManagerProcessRepository
            .checkIsParent(anyLong())
    ).thenReturn(checkParent);

    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    woTypeInsideDTO.setWoTypeCode("2");
    woTypeInsideDTO.setWoTypeName("WoTypeName");
    List<WoTypeInsideDTO> listTypeWo = Mockito.spy(ArrayList.class);
    listTypeWo.add(woTypeInsideDTO);
    PowerMockito.when(woCategoryServiceProxy.getListWoTypeDTO(any())).thenReturn(listTypeWo);

    PowerMockito.when(I18n.getLanguage("CrProcessManager.export.title")).thenReturn("10");
    PowerMockito.when(I18n.getLanguage("wo.typeWork.title")).thenReturn("20");
    PowerMockito.when(I18n.getLanguage("deviceTypes.title_export")).thenReturn("30");
    PowerMockito.when(I18n.getLanguage("ImpactSegment.export.title")).thenReturn("40");
    PowerMockito.when(I18n.getLanguage("GroupUnit.export")).thenReturn("50");
    PowerMockito.when(I18n.getLanguage("GroupUnitDetail.export")).thenReturn("60");
    PowerMockito.when(I18n.getLanguage("TempImport.export")).thenReturn("70");

    DeviceTypesDTO deviceTypesDTO = Mockito.spy(DeviceTypesDTO.class);
    deviceTypesDTO.setDeviceTypeCode("1");
    deviceTypesDTO.setDeviceTypeName("DeviceTypeName");
    List<DeviceTypesDTO> deviceList = Mockito.spy(ArrayList.class);
    deviceList.add(deviceTypesDTO);

    ImpactSegmentDTO impactSegmentDTO = Mockito.spy(ImpactSegmentDTO.class);
    impactSegmentDTO.setImpactSegmentCode("1");
    impactSegmentDTO.setImpactSegmentName("ImpactSegmentName");
    impactSegmentDTO.setAppliedSystem(1L);
    List<ImpactSegmentDTO> impactSegmentList = Mockito.spy(ArrayList.class);
    impactSegmentList.add(impactSegmentDTO);

    GroupUnitDTO groupUnitDTO = Mockito.spy(GroupUnitDTO.class);
    groupUnitDTO.setGroupUnitCode("1");
    groupUnitDTO.setGroupUnitName("GroupUnitName");
    List<GroupUnitDTO> groupUnitList = Mockito.spy(ArrayList.class);
    groupUnitList.add(groupUnitDTO);

    GroupUnitDetailDTO groupUnitDetailDTO = Mockito.spy(GroupUnitDetailDTO.class);
    groupUnitDetailDTO.setGroupUnitId(1L);
    groupUnitDetailDTO.setUnitId(1L);
    List<GroupUnitDetailDTO> groupUnitDetailList = Mockito.spy(ArrayList.class);
    groupUnitDetailList.add(groupUnitDetailDTO);

    TempImportDTO tempImportDTO = Mockito.spy(TempImportDTO.class);
    tempImportDTO.setCode("1");
    tempImportDTO.setName("Name");
    List<TempImportDTO> tempImportList = Mockito.spy(ArrayList.class);
    tempImportList.add(tempImportDTO);

    PowerMockito.when(
        deviceTypesRepository.getDeviceTypesCBB()
    ).thenReturn(deviceList);
    PowerMockito.when(
        crImpactSegmentRepository
            .getListImpactSegmentDTO(any())
    ).thenReturn(impactSegmentList);
    PowerMockito.when(
        groupUnitRepository.getAllListGroupUnitDTO(any())
    ).thenReturn(groupUnitList);
    PowerMockito.when(
        groupUnitDetailRepository
            .getAllListGroupUnitDetailDTO(any())
    ).thenReturn(groupUnitDetailList);
    PowerMockito.when(
        crManagerProcessRepository.getListTempImportDTO(anyString())
    ).thenReturn(tempImportList);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = crManagerProcessBusiness.importData(multipartFile);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

//  @Test
//  public void validateImportSheetWo() {
//  }

//  @Test
//  public void contains() {
//  }
//
//  @Test
//  public void duplicates() {
//  }

  @Test
  public void checkLevel1_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    CrProcessTemplateDTO crProcessTemplateDTO = Mockito.spy(CrProcessTemplateDTO.class);
    crProcessTemplateDTO.setCode("12121212");
    List<CrProcessTemplateDTO> listTemplate = Mockito.spy(ArrayList.class);
    listTemplate.add(crProcessTemplateDTO);

    CrProcessDeptGroupDTO crProcessDeptGroupDTO = Mockito.spy(CrProcessDeptGroupDTO.class);
    crProcessDeptGroupDTO.setGroupUnitCode("21212121");
    List<CrProcessDeptGroupDTO> listDeptGroup = Mockito.spy(ArrayList.class);
    listDeptGroup.add(crProcessDeptGroupDTO);

    CrProcessInsideDTO checkLevel = Mockito.spy(CrProcessInsideDTO.class);
    checkLevel.setCrProcessLevel(1L);
    checkLevel.setCrProcessId(1L);
    checkLevel.setListCrProcessTemplate(listTemplate);
    checkLevel.setListCrProcessDeptGroup(listDeptGroup);
    PowerMockito.when(
        crManagerProcessRepository.getCrProcessById(anyLong())
    ).thenReturn(checkLevel);

    PowerMockito.when(
        crManagerProcessRepository.checkIsParent(anyLong())
    ).thenReturn(true);

    CrProcessInsideDTO dto = Mockito.spy(CrProcessInsideDTO.class);
    dto.setCrProcessId(1L);
    dto.setFileCode("121212");
    dto.setGroupUnitCode("212121");
    crManagerProcessBusiness.checkLevel1(dto);
  }

  @Test
  public void checkLevel1_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    CrProcessTemplateDTO crProcessTemplateDTO = Mockito.spy(CrProcessTemplateDTO.class);
    crProcessTemplateDTO.setCode("12121212");
    CrProcessTemplateDTO crProcessTemplateDTO1 = Mockito.spy(CrProcessTemplateDTO.class);
    crProcessTemplateDTO1.setCode("12121212");
    List<CrProcessTemplateDTO> listTemplate = Mockito.spy(ArrayList.class);
    listTemplate.add(crProcessTemplateDTO);
    listTemplate.add(crProcessTemplateDTO1);

    CrProcessDeptGroupDTO crProcessDeptGroupDTO = Mockito.spy(CrProcessDeptGroupDTO.class);
    crProcessDeptGroupDTO.setGroupUnitCode("21212121");
    CrProcessDeptGroupDTO crProcessDeptGroupDTO1 = Mockito.spy(CrProcessDeptGroupDTO.class);
    crProcessDeptGroupDTO1.setGroupUnitCode("21212121");
    List<CrProcessDeptGroupDTO> listDeptGroup = Mockito.spy(ArrayList.class);
    listDeptGroup.add(crProcessDeptGroupDTO);
    listDeptGroup.add(crProcessDeptGroupDTO1);

    CrProcessInsideDTO checkLevel = Mockito.spy(CrProcessInsideDTO.class);
    checkLevel.setCrProcessLevel(1L);
    checkLevel.setCrProcessId(1L);
    checkLevel.setListCrProcessTemplate(listTemplate);
    checkLevel.setListCrProcessDeptGroup(listDeptGroup);
    PowerMockito.when(
        crManagerProcessRepository.getCrProcessById(anyLong())
    ).thenReturn(checkLevel);

    PowerMockito.when(
        crManagerProcessRepository.checkIsParent(anyLong())
    ).thenReturn(true);

    CrProcessInsideDTO dto = Mockito.spy(CrProcessInsideDTO.class);
    dto.setCrProcessId(1L);
    dto.setFileCode("121212");
    dto.setGroupUnitCode("212121");
    crManagerProcessBusiness.checkLevel1(dto);
  }

  @Test
  public void checkLevel2_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    CrProcessTemplateDTO crProcessTemplateDTO = Mockito.spy(CrProcessTemplateDTO.class);
    crProcessTemplateDTO.setCode("12121212");
    List<CrProcessTemplateDTO> listTemplate = Mockito.spy(ArrayList.class);
    listTemplate.add(crProcessTemplateDTO);

    CrProcessInsideDTO checkLevel = Mockito.spy(CrProcessInsideDTO.class);
    checkLevel.setCrProcessLevel(1L);
    checkLevel.setCrProcessId(1L);
    checkLevel.setListCrProcessTemplate(listTemplate);
    PowerMockito.when(
        crManagerProcessRepository.getCrProcessById(anyLong())
    ).thenReturn(checkLevel);

    PowerMockito.when(
        crManagerProcessRepository.checkIsParent(anyLong())
    ).thenReturn(true);

    CrProcessInsideDTO dto = Mockito.spy(CrProcessInsideDTO.class);
    dto.setParentId(1L);
    dto.setFileCode("121212");
    crManagerProcessBusiness.checkLevel2(dto);
  }

  @Test
  public void checkLevel2_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    CrProcessTemplateDTO crProcessTemplateDTO = Mockito.spy(CrProcessTemplateDTO.class);
    crProcessTemplateDTO.setCode("12121212");
    CrProcessTemplateDTO crProcessTemplateDTO1 = Mockito.spy(CrProcessTemplateDTO.class);
    crProcessTemplateDTO1.setCode("12121212");
    List<CrProcessTemplateDTO> listTemplate = Mockito.spy(ArrayList.class);
    listTemplate.add(crProcessTemplateDTO);
    listTemplate.add(crProcessTemplateDTO1);

    CrProcessInsideDTO checkLevel = Mockito.spy(CrProcessInsideDTO.class);
    checkLevel.setCrProcessLevel(1L);
    checkLevel.setCrProcessId(1L);
    checkLevel.setListCrProcessTemplate(listTemplate);
    PowerMockito.when(
        crManagerProcessRepository.getCrProcessById(anyLong())
    ).thenReturn(checkLevel);

    PowerMockito.when(
        crManagerProcessRepository.checkIsParent(anyLong())
    ).thenReturn(true);

    CrProcessInsideDTO dto = Mockito.spy(CrProcessInsideDTO.class);
    dto.setParentId(1L);
    dto.setFileCode("121212");
    crManagerProcessBusiness.checkLevel2(dto);
  }

//  @Test
//  public void checkLevel3() {
//  }

  @Test
  public void validateLevel3_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    CrProcessTemplateDTO crProcessTemplateDTO = Mockito.spy(CrProcessTemplateDTO.class);
    crProcessTemplateDTO.setCode("12121212");
    List<CrProcessTemplateDTO> listTemplate = Mockito.spy(ArrayList.class);
    listTemplate.add(crProcessTemplateDTO);

    CrProcessDeptGroupDTO crProcessDeptGroupDTO = Mockito.spy(CrProcessDeptGroupDTO.class);
    crProcessDeptGroupDTO.setGroupUnitCode("21212121");
    List<CrProcessDeptGroupDTO> listDeptGroup = Mockito.spy(ArrayList.class);
    listDeptGroup.add(crProcessDeptGroupDTO);

    CrProcessInsideDTO checkLevel = Mockito.spy(CrProcessInsideDTO.class);
    checkLevel.setCrTypeId(1L);
    checkLevel.setRiskLevel(1L);
    checkLevel.setDeviceTypeId(1L);
    checkLevel.setImpactSegmentId(1L);
    checkLevel.setImpactType(1L);
    checkLevel.setImpactCharacteristic(1L);
    checkLevel.setRequireFileLog(1L);
    checkLevel.setRequireMop(1L);
    checkLevel.setListCrProcessTemplate(listTemplate);
    checkLevel.setListCrProcessDeptGroup(listDeptGroup);

    CrProcessInsideDTO dto = Mockito.spy(CrProcessInsideDTO.class);
    dto.setCrTypeId(2L);
    dto.setRiskLevel(2L);
    dto.setDeviceTypeId(2L);
    dto.setImpactSegmentId(2L);
    dto.setImpactType(2L);
    dto.setImpactCharacteristic(2L);
    dto.setRequireFileLog(2L);
    dto.setRequireMop(2L);
    dto.setFileCode("121212");
    dto.setGroupUnitCode("212121");
    crManagerProcessBusiness.validateLevel3(checkLevel, dto);
  }

  @Test
  public void validateLevel3_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    CrProcessTemplateDTO crProcessTemplateDTO = Mockito.spy(CrProcessTemplateDTO.class);
    crProcessTemplateDTO.setCode("12121212");
    CrProcessTemplateDTO crProcessTemplateDTO1 = Mockito.spy(CrProcessTemplateDTO.class);
    crProcessTemplateDTO1.setCode("12121212");
    List<CrProcessTemplateDTO> listTemplate = Mockito.spy(ArrayList.class);
    listTemplate.add(crProcessTemplateDTO);
    listTemplate.add(crProcessTemplateDTO1);

    CrProcessDeptGroupDTO crProcessDeptGroupDTO = Mockito.spy(CrProcessDeptGroupDTO.class);
    crProcessDeptGroupDTO.setGroupUnitCode("21212121");
    CrProcessDeptGroupDTO crProcessDeptGroupDTO1 = Mockito.spy(CrProcessDeptGroupDTO.class);
    crProcessDeptGroupDTO1.setGroupUnitCode("21212121");
    List<CrProcessDeptGroupDTO> listDeptGroup = Mockito.spy(ArrayList.class);
    listDeptGroup.add(crProcessDeptGroupDTO);
    listDeptGroup.add(crProcessDeptGroupDTO1);

    CrProcessInsideDTO checkLevel = Mockito.spy(CrProcessInsideDTO.class);
    checkLevel.setCrTypeId(1L);
    checkLevel.setRiskLevel(1L);
    checkLevel.setDeviceTypeId(1L);
    checkLevel.setImpactSegmentId(1L);
    checkLevel.setImpactType(1L);
    checkLevel.setImpactCharacteristic(1L);
    checkLevel.setRequireFileLog(1L);
    checkLevel.setRequireMop(1L);
    checkLevel.setListCrProcessTemplate(listTemplate);
    checkLevel.setListCrProcessDeptGroup(listDeptGroup);

    CrProcessInsideDTO dto = Mockito.spy(CrProcessInsideDTO.class);
    dto.setCrTypeId(2L);
    dto.setRiskLevel(2L);
    dto.setDeviceTypeId(2L);
    dto.setImpactSegmentId(2L);
    dto.setImpactType(2L);
    dto.setImpactCharacteristic(2L);
    dto.setRequireFileLog(2L);
    dto.setRequireMop(2L);
    dto.setFileCode("121212");
    dto.setGroupUnitCode("212121");
    crManagerProcessBusiness.validateLevel3(checkLevel, dto);
  }

  @Test
  public void getTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    File actual = crManagerProcessBusiness.getTemplate();

    Assert.assertNotNull(actual);
  }

//  @Test
//  public void findCrProcess() {
//  }

//  @Test
//  public void generateCrProcessCode() {
//  }

  @Test
  public void createWoSheet() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");
    SXSSFWorkbook workbook = Mockito.spy(SXSSFWorkbook.class);
    crManagerProcessBusiness.createWoSheet(workbook);
  }
}
