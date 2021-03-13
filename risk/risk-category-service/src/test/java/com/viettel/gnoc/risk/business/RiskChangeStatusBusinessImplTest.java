package com.viettel.gnoc.risk.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.RiskCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusRoleDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.repository.RiskCfgBusinessRepository;
import com.viettel.gnoc.risk.repository.RiskChangeStatusRepository;
import com.viettel.gnoc.risk.repository.RiskChangeStatusRoleRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RiskChangeStatusBusinessImpl.class, FileUtils.class, CommonImport.class,
    CommonExport.class, TicketProvider.class, I18n.class, DateTimeUtils.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class RiskChangeStatusBusinessImplTest {

  @InjectMocks
  RiskChangeStatusBusinessImpl riskChangeStatusBusiness;

  @Mock
  RiskChangeStatusRepository riskChangeStatusRepository;

  @Mock
  RiskCfgBusinessRepository riskCfgBusinessRepository;

  @Mock
  RiskChangeStatusRoleRepository riskChangeStatusRoleRepository;

  @Mock
  RiskCategoryServiceProxy riskCategoryServiceProxy;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitRepository unitRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  Collections collections;

  @Test
  public void testGetDataRiskChangeStatusSearchWeb_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = Mockito.spy(Datatable.class);
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    List<RiskChangeStatusDTO> list = Mockito.spy(ArrayList.class);
    riskChangeStatusDTO.setRiskTypeName("xxx");
    list.add(riskChangeStatusDTO);
    datatable.setData(list);
    PowerMockito.when(riskChangeStatusRepository.getDataRiskChangeStatusSearchWeb(any()))
        .thenReturn(datatable);

    riskChangeStatusBusiness.getDataRiskChangeStatusSearchWeb(riskChangeStatusDTO);
    Assert.assertNotNull(datatable);
  }

  @Test
  public void testGetListRiskChangeStatusDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setIsSearch(true);
    List<RiskChangeStatusDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskChangeStatusDTO);
    PowerMockito
        .when(riskChangeStatusRepository.onSearch(riskChangeStatusDTO, 0, 2147483647, "", ""))
        .thenReturn(list);
    riskChangeStatusBusiness.getListRiskChangeStatusDTO(riskChangeStatusDTO);
    Assert.assertEquals(list.size(), 1L);
  }

  @Test
  public void testGetListRiskChangeStatusDTO_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskChangeStatusDTO riskChangeStatusDTO = null;
    riskChangeStatusBusiness.getListRiskChangeStatusDTO(riskChangeStatusDTO);
    Assert.assertNull(riskChangeStatusDTO);
  }

  @Test
  public void testGetListRiskChangeStatusDTO_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    List<RiskChangeStatusDTO> list = Mockito.spy(ArrayList.class);
    riskChangeStatusDTO.setIsSearch(false);
    riskChangeStatusDTO.setIsDefault(1L);
    riskChangeStatusDTO.setRiskTypeId(null);
    PowerMockito
        .when(riskChangeStatusRepository.onSearch(riskChangeStatusDTO, 0, 2147483647, "", ""))
        .thenReturn(list);
    riskChangeStatusBusiness.getListRiskChangeStatusDTO(riskChangeStatusDTO);
    Assert.assertEquals(list.size(), 0L);
  }

  @Test
  public void testGetListRiskCfgBusinessDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskCfgBusinessDTO riskCfgBusinessDTO = Mockito.spy(RiskCfgBusinessDTO.class);
    riskCfgBusinessDTO.setRiskChangeStatusId(1L);
    List<RiskCfgBusinessDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskCfgBusinessDTO);
    PowerMockito.when(riskCfgBusinessRepository.onSearch(riskCfgBusinessDTO, 0, 2147483647, "", ""))
        .thenReturn(list);
    riskChangeStatusBusiness.getListRiskCfgBusinessDTO(riskCfgBusinessDTO);
    Assert.assertEquals(list.size(), 1L);
  }

  @Test
  public void testGetListRiskCfgBusinessDTO_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskCfgBusinessDTO riskCfgBusinessDTO = null;
    riskChangeStatusBusiness.getListRiskCfgBusinessDTO(riskCfgBusinessDTO);
    Assert.assertNull(riskCfgBusinessDTO);
  }

  @Test
  public void testGetListRiskChangeStatusRoleDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskChangeStatusRoleDTO riskChangeStatusRoleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    riskChangeStatusRoleDTO.setRoleName("xxx");
    List<RiskChangeStatusRoleDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskChangeStatusRoleDTO);
    PowerMockito.when(riskChangeStatusRoleRepository
        .onSearch(riskChangeStatusRoleDTO, 0, 2147483647, "", "")).thenReturn(list);
    riskChangeStatusBusiness.getListRiskChangeStatusRoleDTO(riskChangeStatusRoleDTO);
    Assert.assertEquals(list.size(), 1L);
  }

  @Test
  public void testGetListRiskChangeStatusRoleDTO_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskChangeStatusRoleDTO riskChangeStatusRoleDTO = null;
    riskChangeStatusBusiness.getListRiskChangeStatusRoleDTO(riskChangeStatusRoleDTO);
    Assert.assertNull(riskChangeStatusRoleDTO);
  }

  @Test
  public void testInsertOrUpdateRiskChangeStatus_01() throws IOException {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(10L);
    resultInSideDto.setKey(RESULT.SUCCESS);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> lstMultipartFile = Mockito.spy(ArrayList.class);
    //Truong hop ID == null
    RiskCfgBusinessDTO riskCfgBusinessDTO = Mockito.spy(RiskCfgBusinessDTO.class);
    List<RiskCfgBusinessDTO> lstCfgBusiness = Mockito.spy(ArrayList.class);
    lstCfgBusiness.add(riskCfgBusinessDTO);

    RiskChangeStatusRoleDTO roleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    List<RiskChangeStatusRoleDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(roleDTO);

    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setId(null);
    riskChangeStatusDTO.setLstCfgBusiness(lstCfgBusiness);
    riskChangeStatusDTO.setLstRole(lstRole);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setIndexFile(null);
    gnocFileDto.setMultipartFile(null);
    gnocFileDto.setId(21L);
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    gnocFileDtos.add(gnocFileDto);
    riskChangeStatusDTO.setGnocFileDtos(gnocFileDtos);

    PowerMockito.when(riskChangeStatusRepository
        .insertOrUpdateRiskChangeStatus(any())).thenReturn(resultInSideDto);
    riskCfgBusinessDTO.setRiskChangeStatusId(10L);
    PowerMockito.when(riskCfgBusinessRepository.insertRiskCfgBusiness(riskCfgBusinessDTO))
        .thenReturn(resultInSideDto);

    roleDTO.setRiskChangeStatusId(10L);
    PowerMockito.when(riskChangeStatusRoleRepository.insertRiskChangeStatusRole(roleDTO))
        .thenReturn(resultInSideDto);

    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = riskChangeStatusBusiness
        .insertOrUpdateRiskChangeStatus(riskChangeStatusDTO, lstMultipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testInsertOrUpdateRiskChangeStatus_02() throws IOException {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(10L);
    resultInSideDto.setKey(RESULT.SUCCESS);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> lstMultipartFile = Mockito.spy(ArrayList.class);
    //Truong hop ID != null
    RiskCfgBusinessDTO riskCfgBusinessDTO = Mockito.spy(RiskCfgBusinessDTO.class);
    List<RiskCfgBusinessDTO> lstCfgBusiness = Mockito.spy(ArrayList.class);
    riskCfgBusinessDTO.setId(69L);
    lstCfgBusiness.add(riskCfgBusinessDTO);

    RiskChangeStatusRoleDTO roleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    List<RiskChangeStatusRoleDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(roleDTO);

    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setId(96L);
    riskChangeStatusDTO.setLstCfgBusiness(lstCfgBusiness);
    riskChangeStatusDTO.setLstRole(lstRole);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setIndexFile(null);
    gnocFileDto.setMultipartFile(null);
    gnocFileDto.setId(21L);
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    gnocFileDtos.add(gnocFileDto);
    riskChangeStatusDTO.setGnocFileDtos(gnocFileDtos);

    PowerMockito.when(riskChangeStatusRepository
        .insertOrUpdateRiskChangeStatus(any())).thenReturn(resultInSideDto);
    PowerMockito.when(riskCfgBusinessRepository.deleteListRiskCfgBusiness(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskChangeStatusRoleRepository.deleteListRiskChangeStatusRole(anyLong()))
        .thenReturn(resultInSideDto);
    riskCfgBusinessDTO.setRiskChangeStatusId(10L);
    PowerMockito.when(riskCfgBusinessRepository.insertRiskCfgBusiness(riskCfgBusinessDTO))
        .thenReturn(resultInSideDto);

    roleDTO.setRiskChangeStatusId(10L);
    PowerMockito.when(riskChangeStatusRoleRepository.insertRiskChangeStatusRole(roleDTO))
        .thenReturn(resultInSideDto);

    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = riskChangeStatusBusiness
        .insertOrUpdateRiskChangeStatus(riskChangeStatusDTO, lstMultipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testInsertOrUpdateRiskChangeStatus_03() throws IOException {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(10L);
    resultInSideDto.setKey(RESULT.SUCCESS);

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(12L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MultipartFile multipartFile = null;
    List<MultipartFile> lstMultipartFile = Mockito.spy(ArrayList.class);
    lstMultipartFile.add(multipartFile);
    //Truong hop ID != null
    RiskCfgBusinessDTO riskCfgBusinessDTO = Mockito.spy(RiskCfgBusinessDTO.class);
    List<RiskCfgBusinessDTO> lstCfgBusiness = Mockito.spy(ArrayList.class);
    riskCfgBusinessDTO.setId(69L);
    lstCfgBusiness.add(riskCfgBusinessDTO);

    RiskChangeStatusRoleDTO roleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    List<RiskChangeStatusRoleDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(roleDTO);

    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setId(96L);
    riskChangeStatusDTO.setLstCfgBusiness(lstCfgBusiness);
    riskChangeStatusDTO.setLstRole(lstRole);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setIndexFile(0l);
    gnocFileDto.setMultipartFile(null);
    gnocFileDto.setId(21L);
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    gnocFileDtos.add(gnocFileDto);
    riskChangeStatusDTO.setGnocFileDtos(gnocFileDtos);

    PowerMockito.when(riskChangeStatusRepository
        .insertOrUpdateRiskChangeStatus(any())).thenReturn(resultInSideDto);
    PowerMockito.when(riskCfgBusinessRepository.deleteListRiskCfgBusiness(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskChangeStatusRoleRepository.deleteListRiskChangeStatusRole(anyLong()))
        .thenReturn(resultInSideDto);
    riskCfgBusinessDTO.setRiskChangeStatusId(10L);
    PowerMockito.when(riskCfgBusinessRepository.insertRiskCfgBusiness(riskCfgBusinessDTO))
        .thenReturn(resultInSideDto);

    roleDTO.setRiskChangeStatusId(10L);
    PowerMockito.when(riskChangeStatusRoleRepository.insertRiskChangeStatusRole(roleDTO))
        .thenReturn(resultInSideDto);

    resultInSideDto.setKey(RESULT.SUCCESS);
    riskChangeStatusDTO.setGnocFileDtos(gnocFileDtos);

    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = riskChangeStatusBusiness
        .insertOrUpdateRiskChangeStatus(riskChangeStatusDTO, lstMultipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testFindRiskChangeStatusByIdFromWeb_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    PowerMockito.when(riskChangeStatusRepository
        .findRiskChangeStatusById(anyLong())).thenReturn(riskChangeStatusDTO);
    riskChangeStatusBusiness.findRiskChangeStatusByIdFromWeb(null);
    Assert.assertEquals(riskChangeStatusDTO.getId(), null);
  }

  @Test
  public void testFindRiskChangeStatusByIdFromWeb_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    PowerMockito.when(riskChangeStatusRepository.findRiskChangeStatusById(anyLong()))
        .thenReturn(riskChangeStatusDTO);
    riskChangeStatusBusiness.findRiskChangeStatusByIdFromWeb(10L);
    Assert.assertNotNull(riskChangeStatusDTO);
  }

  @Test
  public void testExportDataRiskChangeStatus_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    List<RiskChangeStatusDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskChangeStatusDTO);
    //Truong hop list == null
    String[] header = new String[]{"riskTypeName", "riskPriorityName", "oldStatusName",
        "newStatusName",
        "changeStatusRole"};
    //Goi den handleFileExport
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    PowerMockito.mockStatic(CommonExport.class);
    List<RiskChangeStatusDTO> statusDTOS = Mockito.spy(ArrayList.class);
    RiskChangeStatusDTO dto = Mockito.spy(RiskChangeStatusDTO.class);
    list.add(dto);
    String[] columnExport = {"abc", "xyz", "qwe"};
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito
        .when(CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any()))
        .thenReturn(fileExport);

    riskChangeStatusBusiness.exportDataRiskChangeStatus(riskChangeStatusDTO);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void testExportDataRiskChangeStatus_02() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    //Truong hop list != null
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setRiskTypeName("vuvu");
    riskChangeStatusDTO.setId(10L);
    riskChangeStatusDTO.setChangeStatusRole("vnb,vbn");

    List<RiskChangeStatusDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskChangeStatusDTO);
    RiskChangeStatusRoleDTO roleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    List<RiskChangeStatusRoleDTO> listRole = Mockito.spy(ArrayList.class);
    roleDTO.setRoleName("kimochi");
    listRole.add(roleDTO);

    PowerMockito.when(riskChangeStatusRepository.getListRiskChangeStatusExport(any()))
        .thenReturn(list);
    String[] header = new String[]{"riskTypeName", "riskPriorityName", "oldStatusName",
        "newStatusName",
        "changeStatusRole"};
    //Goi den handleFileExport
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    PowerMockito.mockStatic(CommonExport.class);
    List<RiskChangeStatusDTO> statusDTOS = Mockito.spy(ArrayList.class);
    RiskChangeStatusDTO dto = Mockito.spy(RiskChangeStatusDTO.class);
    list.add(dto);
    String[] columnExport = {"abc", "xyz", "qwe"};
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito
        .when(CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any()))
        .thenReturn(fileExport);

    riskChangeStatusBusiness.exportDataRiskChangeStatus(riskChangeStatusDTO);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void testExportDataRiskChangeStatus_03() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    //Truong hop list != null
    RiskChangeStatusDTO riskChangeStatusDTO = Mockito.spy(RiskChangeStatusDTO.class);
    riskChangeStatusDTO.setRiskTypeName("vuvu");
    riskChangeStatusDTO.setId(10L);
    riskChangeStatusDTO.setChangeStatusRole("vnb,vbn");

    List<RiskChangeStatusDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskChangeStatusDTO);
    RiskChangeStatusRoleDTO roleDTO = Mockito.spy(RiskChangeStatusRoleDTO.class);
    List<RiskChangeStatusRoleDTO> listRole = Mockito.spy(ArrayList.class);
    roleDTO.setRoleName("kimochi");
    roleDTO.setId(10L);
    listRole.add(roleDTO);

    PowerMockito.when(riskChangeStatusRepository.getListRiskChangeStatusExport(any()))
        .thenReturn(list);
    PowerMockito.when(riskChangeStatusRoleRepository.getListRoleByRiskChangeStatusId(anyLong()))
        .thenReturn(listRole);

    String[] header = new String[]{"riskTypeName", "riskPriorityName", "oldStatusName",
        "newStatusName",
        "changeStatusRole"};
    //Goi den handleFileExport
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    PowerMockito.mockStatic(CommonExport.class);
    List<RiskChangeStatusDTO> statusDTOS = Mockito.spy(ArrayList.class);
    RiskChangeStatusDTO dto = Mockito.spy(RiskChangeStatusDTO.class);
    list.add(dto);
    String[] columnExport = {"abc", "xyz", "qwe"};
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito
        .when(CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any()))
        .thenReturn(fileExport);

    riskChangeStatusBusiness.exportDataRiskChangeStatus(riskChangeStatusDTO);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void testGetListRiskTypeDTOCombobox_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    List<RiskTypeDTO> dtoList = Mockito.spy(ArrayList.class);
    dtoList.add(riskTypeDTO);
    PowerMockito.when(riskCategoryServiceProxy.getListRiskTypeDTO(any())).thenReturn(dtoList);
    riskChangeStatusBusiness.getListRiskTypeDTOCombobox(riskTypeDTO);
    Assert.assertNotNull(dtoList);
  }

  @Test
  public void testDeleteRiskChangeStatus_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    PowerMockito.when(riskCfgBusinessRepository.deleteListRiskCfgBusiness(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskChangeStatusRoleRepository.deleteListRiskChangeStatusRole(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(riskChangeStatusRepository.deleteRiskChangeStatus(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = riskChangeStatusBusiness.deleteRiskChangeStatus(10L);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testHandleFileExport_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    PowerMockito.mockStatic(CommonExport.class);
    List<RiskChangeStatusDTO> list = Mockito.spy(ArrayList.class);
    RiskChangeStatusDTO dto = Mockito.spy(RiskChangeStatusDTO.class);
    list.add(dto);
    String[] columnExport = {"abc", "xyz", "qwe"};
    File fileExport = new File("./test_junit2/test.xlsx");

    PowerMockito
        .when(CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any()))
        .thenReturn(fileExport);
    riskChangeStatusBusiness.handleFileExport(list, columnExport, "16/04/2020");
    Assert.assertNotNull(fileExport);
  }

}
