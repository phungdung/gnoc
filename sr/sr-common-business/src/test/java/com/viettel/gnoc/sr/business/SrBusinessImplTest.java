package com.viettel.gnoc.sr.business;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.CfgRoleDataDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.OdServiceProxy;
import com.viettel.gnoc.commons.proxy.SrCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.CfgRoleDataRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.TemplateImportDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.sr.dto.SRActionCodeDTO;
import com.viettel.gnoc.sr.dto.SRApproveDTO;
import com.viettel.gnoc.sr.dto.SRCatalogChildDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRChildAutoDTO;
import com.viettel.gnoc.sr.dto.SRConfig2DTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRCreatedFromOtherSysDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SREvaluateDTO;
import com.viettel.gnoc.sr.dto.SRHisDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.sr.dto.SRMopDTO;
import com.viettel.gnoc.sr.dto.SRParamDTO;
import com.viettel.gnoc.sr.dto.SRRenewDTO;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
import com.viettel.gnoc.sr.dto.SRWorklogTypeDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.dto.SrWsToolCrDTO;
import com.viettel.gnoc.sr.dto.UnitSRCatalogDTO;
import com.viettel.gnoc.sr.model.SRFilesEntity;
import com.viettel.gnoc.sr.model.SRParamEntity;
import com.viettel.gnoc.sr.repository.SRApproveRepository;
import com.viettel.gnoc.sr.repository.SRCatalogChildRepository;
import com.viettel.gnoc.sr.repository.SRCatalogRepository2;
import com.viettel.gnoc.sr.repository.SRChildAutoRepository;
import com.viettel.gnoc.sr.repository.SRConfig2Repository;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SRCreateAutoCrRepository;
import com.viettel.gnoc.sr.repository.SRCreatedFromOtherSysRepository;
import com.viettel.gnoc.sr.repository.SREvaluateRepository;
import com.viettel.gnoc.sr.repository.SRHisRepository;
import com.viettel.gnoc.sr.repository.SRMopRepository;
import com.viettel.gnoc.sr.repository.SRParamRepository;
import com.viettel.gnoc.sr.repository.SRRenewRepository;
import com.viettel.gnoc.sr.repository.SRWorkLogRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.nims.infra.webservice.sr.DistributeIpResourceForm;
import com.viettel.nims.infra.webservice.sr.DistributeIpResourceOutput;
import com.viettel.security.PassTranformer;
import com.viettel.vipa.ResultDeleteDt;
import com.viettel.vipa.WSForGNOC;
import com.viettel.vmsa.ResultCreateDtByFileInput;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.management.MalformedObjectNameException;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;
import viettel.passport.client.UserToken;
import org.springframework.core.io.Resource;
@RunWith(PowerMockRunner.class)
@PrepareForTest({SrBusinessImplTest.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, WSForGNOC.class, ObjectMapper.class, Workbook.class,
    ResultCreateDtByFileInput.class, ExcelWriterUtils.class, CommonExport.class,DateUtil.class,
    DataUtil.class, PassTranformer.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
@Slf4j
public class SrBusinessImplTest {

  @InjectMocks
  protected SrBusinessImpl srBusiness;
  @Mock
  protected SrRepository srRepository;

  @Mock
  SRConfigRepository srConfigRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  SRHisRepository srHisRepository;

  @Mock
  LanguageExchangeRepository languageExchangeRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  CfgRoleDataRepository cfgRoleDataRepository;

  @Mock
  SRWorkLogRepository srWorkLogRepository;

  @Mock
  SRCatalogRepository2 srCatalogRepository2;

  @Mock
  SrCategoryServiceProxy srCategoryServiceProxy;

  @Mock
  SRApproveRepository srApproveRepository;

  @Mock
  SRRenewRepository srRenewRepository;

  @Mock
  UnitBusiness unitBusiness;

  @Mock
  SRMopRepository srMopRepository;

  @Mock
  SREvaluateRepository srEvaluateRepository;

  @Mock
  OdServiceProxy odServiceProxy;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  SRCreatedFromOtherSysRepository srCreatedFromOtherSysRepository;

  @Mock
  SRConfig2Repository srConfig2Repository;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  CrCategoryServiceProxy crCategoryServiceProxy;

  @Mock
  SRCatalogChildRepository srCatalogChildRepository;

  @Mock
  SRCreateAutoCrRepository srCreateAutoCrRepository;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  SRChildAutoRepository srChildAutoRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  SRParamRepository srParamRepository;

  @Mock
  MessagesRepository messagesRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(srBusiness, "tempFolder",
        "./sr-temp");
//    ReflectionTestUtils.setField(srBusiness, "categoryServiceProxy",
//        srCategoryServiceProxy);
    ReflectionTestUtils.setField(srBusiness, "uploadFolder",
        "/uploadFolder");
    ReflectionTestUtils.setField(srBusiness, "ftpFolder",
        "/ftpFolder");
    ReflectionTestUtils.setField(srBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(srBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(srBusiness, "ftpServer",
        "10.240.232.25");
    ReflectionTestUtils.setField(srBusiness, "ftpPort",
        21);
    com.viettel.nims.infra.webservice.sr.DistributeIpResourceOutput mmm = Mockito.spy(com.viettel.nims.infra.webservice.sr.DistributeIpResourceOutput.class);
    mmm.setMessage("test");
    mmm.setResult("OK");
    DistributeIpResourceForm distributeIpResourceForm= Mockito.spy(DistributeIpResourceForm.class);
    distributeIpResourceForm.setLocation("123");
    List<DistributeIpResourceForm> lstForm = Mockito.spy(ArrayList.class);
    lstForm.add(distributeIpResourceForm);
    mmm.getListDataResponse().addAll(lstForm);
    ReflectionTestUtils.setField(srBusiness, "outputNims",
        mmm);
  }
  @Test
  public void getListSR_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Datatable datatable = Mockito.spy(Datatable.class);
    ArrayList listData = Mockito.spy(ArrayList.class);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setStatus("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setCrNumber("1");
    srInsiteDTO.setCreateFromDate(new Date());
    srInsiteDTO.setCreateToDate(new Date());
    srInsiteDTO.setCreatedTime(new Date());
    srInsiteDTO.setWlText("1");
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setReplyTime("05/18/2020 03:03:03");
    srInsiteDTO.setStartTime(new Date());
    srInsiteDTO.setStatusRenew("1");
    srInsiteDTO.setStatus("Draft");
    srInsiteDTO.setRemainExecutionTime("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    listData.add(srInsiteDTO);
    datatable.setData(listData);
    List<SrInsiteDTO> lstCrNumberDto = Mockito.spy(ArrayList.class);
    lstCrNumberDto.add(srInsiteDTO);
    SREvaluateDTO srEvaluateDTO = Mockito.spy(SREvaluateDTO.class);
    srEvaluateDTO.setSrId("1");
    srEvaluateDTO.setEvaluateReason("1");
    List<SREvaluateDTO> lstEvaTmp = Mockito.spy(ArrayList.class);
    lstEvaTmp.add(srEvaluateDTO);
    List<Date> lstDate = Mockito.spy(ArrayList.class);
    PowerMockito.when(srRepository.getTotalSRProcessTime(any())).thenReturn(lstCrNumberDto);
    PowerMockito.when(srRepository.getDayOffForExecutionTime(anyString())).thenReturn(lstDate);
    PowerMockito.when(srRepository.getListSREvaluate(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstEvaTmp);
    PowerMockito.when(srRepository.getWorkLog(any(), anyInt(), anyInt(),anyBoolean())).thenReturn(lstCrNumberDto);
    PowerMockito.when(srRepository.getCrNumberCreatedFromSR(any(), anyInt(), anyInt(),anyBoolean())).thenReturn(lstCrNumberDto);
    PowerMockito.when(srRepository.getListSRByUserLogin(srInsiteDTO)).thenReturn(datatable);
    Datatable datatable1 =  srBusiness.getListSRByUserLogin(srInsiteDTO);
    assertNotNull(datatable1.getPages());
  }

  @Test
  public void getListSR_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Datatable datatable = Mockito.spy(Datatable.class);
    ArrayList listData = Mockito.spy(ArrayList.class);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setStatus("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setCrNumber("1");
    srInsiteDTO.setCreateFromDate(new Date());
    srInsiteDTO.setCreateToDate(new Date());
    srInsiteDTO.setCreatedTime(new Date());
    srInsiteDTO.setWlText("1");
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setReplyTime("05/18/2020 03:03:03");
    srInsiteDTO.setStartTime(new Date());
    srInsiteDTO.setStatusRenew("1");
    srInsiteDTO.setStatus("Planned");
    srInsiteDTO.setRemainExecutionTime("1");
    srInsiteDTO.setExecutionTime("1");
    srInsiteDTO.setEvaluateReplyTimeDisplay("18/05/2020 03:03:03");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    listData.add(srInsiteDTO);
    datatable.setData(listData);
    List<SrInsiteDTO> lstCrNumberDto = Mockito.spy(ArrayList.class);
    lstCrNumberDto.add(srInsiteDTO);
    SREvaluateDTO srEvaluateDTO = Mockito.spy(SREvaluateDTO.class);
    srEvaluateDTO.setSrId("1");
    srEvaluateDTO.setEvaluateReason("1");
    List<SREvaluateDTO> lstEvaTmp = Mockito.spy(ArrayList.class);
    lstEvaTmp.add(srEvaluateDTO);
    List<Date> lstDate = Mockito.spy(ArrayList.class);
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    List<SRHisDTO> lstHis = Mockito.spy(ArrayList.class);
    lstHis.add(srHisDTO);
    PowerMockito.when(srHisRepository.getListSRHisDTO(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstHis);
    PowerMockito.when(srRepository.getTotalSRProcessTime(any())).thenReturn(lstCrNumberDto);
    PowerMockito.when(srRepository.getDayOffForExecutionTime(anyString())).thenReturn(lstDate);
    PowerMockito.when(srRepository.getListSREvaluate(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstEvaTmp);
    PowerMockito.when(srRepository.getWorkLog(any(), anyInt(), anyInt(),anyBoolean())).thenReturn(lstCrNumberDto);
    PowerMockito.when(srRepository.getCrNumberCreatedFromSR(any(), anyInt(), anyInt(),anyBoolean())).thenReturn(lstCrNumberDto);
    PowerMockito.when(srRepository.getListSRByUserLogin(srInsiteDTO)).thenReturn(datatable);
    Datatable datatable1 =  srBusiness.getListSRByUserLogin(srInsiteDTO);
    assertNotNull(datatable1.getPages());
  }


  @Test
  public void getListSRByUserLogin_01() {
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setCreatedTime(new Date());
    srDTO.setStatus("New");
    srDTO.setSrId(1L);
    srDTO.setOpenConnect(true);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRActionCodeDTO srActionCodeDTO = Mockito.spy(SRActionCodeDTO.class);
    srActionCodeDTO.setActionCode("1");
    srActionCodeDTO.setDefaultComment("1");
    List<SRActionCodeDTO> lsActionCode = Mockito.spy(ArrayList.class);
    lsActionCode.add(srActionCodeDTO);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> vipaHisComment = Mockito.spy(ArrayList.class);
    vipaHisComment.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(vipaHisComment);
    PowerMockito.when(srRepository.searchSrActionCode(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lsActionCode);
    PowerMockito.when(srRepository.insertSR(srDTO)).thenReturn(resultInSideDto);
    srBusiness.insertSR(srDTO);
  }

  @Test
  public void getListSRByUserLogin_02() {
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setCreatedTime(new Date());
    srDTO.setStatus("New");
    srDTO.setSrId(1L);
    srDTO.setOpenConnect(true);
    srDTO.setSrUser("1");
    srDTO.setSrCode("1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRActionCodeDTO srActionCodeDTO = Mockito.spy(SRActionCodeDTO.class);
    srActionCodeDTO.setActionCode("1");
    srActionCodeDTO.setDefaultComment("1");
    List<SRActionCodeDTO> lsActionCode = Mockito.spy(ArrayList.class);
    lsActionCode.add(srActionCodeDTO);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> vipaHisComment = Mockito.spy(ArrayList.class);
    vipaHisComment.add(srConfigDTO);
    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setUsername("2");
    List<SRRoleUserInSideDTO> lstUserUnit = Mockito.spy(ArrayList.class);
    lstUserUnit.add(srRoleUserInSideDTO);
    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    userExecute.setUsername("1");
    userExecute.setMobile("1");
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitExecute);
    PowerMockito.when(srRepository.searchSRRoleUser(any())).thenReturn(lstUserUnit);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(vipaHisComment);
    PowerMockito.when(srRepository.searchSrActionCode(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lsActionCode);
    PowerMockito.when(srRepository.insertSR(srDTO)).thenReturn(resultInSideDto);
    srBusiness.insertSR(srDTO);
  }

  @Test
  public void getListSRByUserLogin_03() {
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setCreatedTime(new Date());
    srDTO.setStatus("Under_Approval");
    srDTO.setSrId(1L);
    srDTO.setOpenConnect(true);
    srDTO.setSrUser("1");
    srDTO.setCreatedUnit("1");
    srDTO.setServiceId("1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRActionCodeDTO srActionCodeDTO = Mockito.spy(SRActionCodeDTO.class);
    srActionCodeDTO.setActionCode("1");
    srActionCodeDTO.setDefaultComment("1");
    List<SRActionCodeDTO> lsActionCode = Mockito.spy(ArrayList.class);
    lsActionCode.add(srActionCodeDTO);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> vipaHisComment = Mockito.spy(ArrayList.class);
    vipaHisComment.add(srConfigDTO);
    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setUsername("2");
    List<SRRoleUserInSideDTO> lstUserUnit = Mockito.spy(ArrayList.class);
    lstUserUnit.add(srRoleUserInSideDTO);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setApprove(1L);
    PowerMockito.when(srRepository.getUnitParentForApprove(anyString(), anyString())).thenReturn("1");
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srRepository.searchSRRoleUser(any())).thenReturn(lstUserUnit);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(vipaHisComment);
    PowerMockito.when(srRepository.searchSrActionCode(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lsActionCode);
    PowerMockito.when(srRepository.insertSR(srDTO)).thenReturn(resultInSideDto);
    srBusiness.insertSR(srDTO);
  }

  @Test
  public void insertSR_01() {
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");

  }

  @Test
  public void insertSR_02() {
    PowerMockito.mockStatic(DateUtil.class);
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setSrId(1L);
    srDTO.setServiceId("1");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrUnit(1L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTO.setServiceId(1L);
    srCatalogChildDTO.setAutoCreateSR(1L);
    srCatalogChildDTO.setServiceIdChild(1L);
    List<SRCatalogChildDTO> lstChild = Mockito.spy(ArrayList.class);
    lstChild.add(srCatalogChildDTO);
    SRCatalogDTO catalogChild = Mockito.spy(SRCatalogDTO.class);
    catalogChild.setRoleCode("1,1");
    catalogChild.setAutoCreateSR("1,1");
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(catalogChild);

    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChild);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void insertSR_03() {
    PowerMockito.mockStatic(DateUtil.class);
    PowerMockito.mockStatic(DataUtil.class);
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(new Date());
    srDTO.setServiceId("1");
    srDTO.setSrId(1L);
    srDTO.setCountry("1");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTO.setServiceId(1L);
    srCatalogChildDTO.setAutoCreateSR(1L);
    srCatalogChildDTO.setServiceIdChild(1L);
    List<SRCatalogChildDTO> lstChild = Mockito.spy(ArrayList.class);
    lstChild.add(srCatalogChildDTO);
    SRCatalogDTO catalogChild = Mockito.spy(SRCatalogDTO.class);
    catalogChild.setRoleCode("1,1");
    catalogChild.setAutoCreateSR("1,1");
    catalogChild.setServiceId(1L);
    catalogChild.setCountry("1");
    List<String> lsSequense = Mockito.spy(ArrayList.class);
    lsSequense.add("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("1");
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(catLocationDTO);
    PowerMockito.when(srRepository.getListSequenseSR(anyString(), anyInt())).thenReturn(lsSequense);
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(catalogChild);
    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChild);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void insertSR_04() {
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSR_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSR_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("2");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("OPEN_CONNECT");
    List<GnocFileDto> lstFileTemp = Mockito.spy(ArrayList.class);
    lstFileTemp.add(gnocFileDto);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileTemp);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSR_07() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setSrId(1L);
    srDTO.setServiceId("1");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrUnit(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("2");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("FILE_KY");
    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setFileType("OPEN_CONNECT");
    List<GnocFileDto> lstFileTemp = Mockito.spy(ArrayList.class);
    lstFileTemp.add(gnocFileDto);
    lstFileTemp.add(gnocFileDto1);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> lsConfig = Mockito.spy(ArrayList.class);
    lsConfig.add(srConfigDTO);
    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setApprove(1L);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lsConfig);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getOffsetFromUser(anyString())).thenReturn(1.1);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileTemp);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(lsConfig);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileTemp);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void insertSR_08() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Under_Approval");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrId(1L);
    srDTO.setServiceId("1");
    srDTO.setOpenConnect(true);
    srDTO.setAutoCreatCR(true);
    srDTO.setServiceNims(true);
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setSrId(1L);
    srDTO.setSrUnit(1L);
//    srDTO.setServiceAom(true);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("2");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("FILE_KY");
    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setFileType("OPEN_CONNECT");
    List<GnocFileDto> lstFileTemp = Mockito.spy(ArrayList.class);
    lstFileTemp.add(gnocFileDto);
    lstFileTemp.add(gnocFileDto1);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> lsConfig = Mockito.spy(ArrayList.class);
    lsConfig.add(srConfigDTO);
    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setApprove(1L);
    srCatalogDTO.setServiceCode("123");
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUserId(1L);
    usersEntity.setUnitId(1L);
    SrBusinessImpl srBusinessInline = Mockito.spy(srBusiness);
    mockAutoCreateSR();
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srRepository.getUnitParentForApprove(anyString(), anyString())).thenReturn("1");
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lsConfig);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getOffsetFromUser(anyString())).thenReturn(1.1);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileTemp);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(lsConfig);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileTemp);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusinessInline.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void insertSRDTO_09(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setSrUnit(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRDTO_10(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setSrUnit(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRDTO_11(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
//    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setSrUnit(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRDTO_12(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2019 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setSrUnit(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRDTO_13(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setSrUnit(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRDTO_14(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setSrUnit(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRDTO_15(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setEvaluate("1");
    srDTO.setSrUnit(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRDTO_16(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrId(1L);
    srDTO.setSrUnit(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }


  @Test
  public void insertSR_17() {
    PowerMockito.mockStatic(DateUtil.class);
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setSrId(1L);
    srDTO.setServiceId("1");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrUnit(1L);
    srDTO.setAutoCreatCR(true);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTO.setServiceId(1L);
    srCatalogChildDTO.setAutoCreateSR(1L);
    srCatalogChildDTO.setServiceIdChild(1L);
    List<SRCatalogChildDTO> lstChild = Mockito.spy(ArrayList.class);
    lstChild.add(srCatalogChildDTO);
    SRCatalogDTO catalogChild = Mockito.spy(SRCatalogDTO.class);
    catalogChild.setRoleCode("1,1");
    catalogChild.setAutoCreateSR("1,1");
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(catalogChild);

    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChild);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSR_18() {
    PowerMockito.mockStatic(DateUtil.class);
    PowerMockito.mockStatic(DataUtil.class);

    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrId(1L);
    srDTO.setSrUnit(1L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTO.setServiceId(1L);
    srCatalogChildDTO.setAutoCreateSR(1L);
    srCatalogChildDTO.setServiceIdChild(1L);
    List<SRCatalogChildDTO> lstChild = Mockito.spy(ArrayList.class);
    lstChild.add(srCatalogChildDTO);

    SRCatalogDTO catalogChild = Mockito.spy(SRCatalogDTO.class);
    catalogChild.setRoleCode("1,1");
    catalogChild.setAutoCreateSR("1,1");
    catalogChild.setServiceId(1L);
    catalogChild.setCountry("1");

    List<String> lsSequense = Mockito.spy(ArrayList.class);
    lsSequense.add("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("1");

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);

    OdSearchInsideDTO odSearchInsideDTO = Mockito.spy(OdSearchInsideDTO.class);
    odSearchInsideDTO.setUserId("1");
    List<OdSearchInsideDTO> odSearchInsideDTOList = Mockito.spy(ArrayList.class);
    odSearchInsideDTOList.add(odSearchInsideDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setState("2");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(odServiceProxy.getListDataSearchForOther(any())).thenReturn(odSearchInsideDTOList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(catLocationDTO);
    PowerMockito.when(srRepository.getListSequenseSR(anyString(), anyInt())).thenReturn(lsSequense);
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(catalogChild);
    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChild);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSR_19() {
    PowerMockito.mockStatic(DateUtil.class);
    PowerMockito.mockStatic(DataUtil.class);

    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrId(1L);
    srDTO.setSrUnit(1L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTO.setServiceId(1L);
    srCatalogChildDTO.setAutoCreateSR(1L);
    srCatalogChildDTO.setServiceIdChild(1L);
    List<SRCatalogChildDTO> lstChild = Mockito.spy(ArrayList.class);
    lstChild.add(srCatalogChildDTO);

    SRCatalogDTO catalogChild = Mockito.spy(SRCatalogDTO.class);
    catalogChild.setRoleCode("1,1");
    catalogChild.setAutoCreateSR("1,1");
    catalogChild.setServiceId(1L);
    catalogChild.setCountry("1");

    List<String> lsSequense = Mockito.spy(ArrayList.class);
    lsSequense.add("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("1");

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);

    OdSearchInsideDTO odSearchInsideDTO = Mockito.spy(OdSearchInsideDTO.class);
    odSearchInsideDTO.setUserId("1");
    odSearchInsideDTO.setStatusName("1");
    List<OdSearchInsideDTO> odSearchInsideDTOList = Mockito.spy(ArrayList.class);
    odSearchInsideDTOList.add(odSearchInsideDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(odServiceProxy.getListDataSearchForOther(any())).thenReturn(odSearchInsideDTOList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(catLocationDTO);
    PowerMockito.when(srRepository.getListSequenseSR(anyString(), anyInt())).thenReturn(lsSequense);
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(catalogChild);
    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChild);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSR_20() {
    PowerMockito.mockStatic(DateUtil.class);
    PowerMockito.mockStatic(DataUtil.class);

    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrId(1L);
    srDTO.setSrUnit(1L);
    srDTO.setServiceAom(true);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTO.setServiceId(1L);
    srCatalogChildDTO.setAutoCreateSR(1L);
    srCatalogChildDTO.setServiceIdChild(1L);
    List<SRCatalogChildDTO> lstChild = Mockito.spy(ArrayList.class);
    lstChild.add(srCatalogChildDTO);

    SRCatalogDTO catalogChild = Mockito.spy(SRCatalogDTO.class);
    catalogChild.setRoleCode("1,1");
    catalogChild.setAutoCreateSR("1,1");
    catalogChild.setServiceId(1L);
    catalogChild.setCountry("1");

    List<String> lsSequense = Mockito.spy(ArrayList.class);
    lsSequense.add("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("1");

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);

    OdSearchInsideDTO odSearchInsideDTO = Mockito.spy(OdSearchInsideDTO.class);
    odSearchInsideDTO.setUserId("1");
    odSearchInsideDTO.setStatusName("Closed");
    List<OdSearchInsideDTO> odSearchInsideDTOList = Mockito.spy(ArrayList.class);
    odSearchInsideDTOList.add(odSearchInsideDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(odServiceProxy.getListDataSearchForOther(any())).thenReturn(odSearchInsideDTOList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(catLocationDTO);
    PowerMockito.when(srRepository.getListSequenseSR(anyString(), anyInt())).thenReturn(lsSequense);
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(catalogChild);
    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChild);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }


  @Test
  public void insertSR_21() {
    PowerMockito.mockStatic(DateUtil.class);
    PowerMockito.mockStatic(DataUtil.class);

    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrId(1L);
    srDTO.setSrUnit(1L);
    srDTO.setServiceAom(true);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTO.setServiceId(1L);
    srCatalogChildDTO.setAutoCreateSR(1L);
    srCatalogChildDTO.setServiceIdChild(1L);
    List<SRCatalogChildDTO> lstChild = Mockito.spy(ArrayList.class);
    lstChild.add(srCatalogChildDTO);

    SRCatalogDTO catalogChild = Mockito.spy(SRCatalogDTO.class);
    catalogChild.setRoleCode("1,1");
    catalogChild.setAutoCreateSR("1,1");
    catalogChild.setServiceId(1L);
    catalogChild.setCountry("1");

    List<String> lsSequense = Mockito.spy(ArrayList.class);
    lsSequense.add("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("1");

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);

    OdSearchInsideDTO odSearchInsideDTO = Mockito.spy(OdSearchInsideDTO.class);
    odSearchInsideDTO.setUserId("1");
    odSearchInsideDTO.setStatusName("Closed");
    List<OdSearchInsideDTO> odSearchInsideDTOList = Mockito.spy(ArrayList.class);
    odSearchInsideDTOList.add(odSearchInsideDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);

    List<GnocFileDto> lstFileAttach = Mockito.spy(ArrayList.class);
    lstFileAttach.add(gnocFileDto);

    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileAttach);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(odServiceProxy.getListDataSearchForOther(any())).thenReturn(odSearchInsideDTOList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(catLocationDTO);
    PowerMockito.when(srRepository.getListSequenseSR(anyString(), anyInt())).thenReturn(lsSequense);
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(catalogChild);
    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChild);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSR_22() {
    PowerMockito.mockStatic(DateUtil.class);
    PowerMockito.mockStatic(DataUtil.class);

    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setStatus("Closed");
    srDTO.setCountry("1");
    srDTO.setTitle("1");
    srDTO.setDescription("1");
    srDTO.setServiceArray("1");
    srDTO.setServiceGroup("1");
    srDTO.setServiceId("1");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrId(1L);
    srDTO.setSrUnit(1L);
    srDTO.setServiceAom(true);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTO.setServiceId(1L);
    srCatalogChildDTO.setAutoCreateSR(1L);
    srCatalogChildDTO.setServiceIdChild(1L);
    List<SRCatalogChildDTO> lstChild = Mockito.spy(ArrayList.class);
    lstChild.add(srCatalogChildDTO);

    SRCatalogDTO catalogChild = Mockito.spy(SRCatalogDTO.class);
    catalogChild.setRoleCode("1,1");
    catalogChild.setAutoCreateSR("1,1");
    catalogChild.setServiceId(1L);
    catalogChild.setCountry("1");

    List<String> lsSequense = Mockito.spy(ArrayList.class);
    lsSequense.add("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("1");

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);

    OdSearchInsideDTO odSearchInsideDTO = Mockito.spy(OdSearchInsideDTO.class);
    odSearchInsideDTO.setUserId("1");
    odSearchInsideDTO.setStatusName("Closed");
    List<OdSearchInsideDTO> odSearchInsideDTOList = Mockito.spy(ArrayList.class);
    odSearchInsideDTOList.add(odSearchInsideDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("AOM");
    List<GnocFileDto> lstFileAttach = Mockito.spy(ArrayList.class);
    lstFileAttach.add(gnocFileDto);

    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileAttach);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(odServiceProxy.getListDataSearchForOther(any())).thenReturn(odSearchInsideDTOList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(catLocationDTO);
    PowerMockito.when(srRepository.getListSequenseSR(anyString(), anyInt())).thenReturn(lsSequense);
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(catalogChild);
    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChild);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.insertSRDTO(srDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void updateSR_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Under_Approval");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrId(1L);
    srDTO.setServiceId("1");
    srDTO.setOpenConnect(true);
    srDTO.setAutoCreatCR(true);
    srDTO.setServiceNims(true);
    srDTO.setStatus("New");
    srDTO.setStartTime(new Date());
    srDTO.setUpdatedTime(new Date());
    srDTO.setUpdatedUser("1");
    srDTO.setOpenConnect(true);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("2");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("FILE_KY");
    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setFileType("OPEN_CONNECT");
    List<GnocFileDto> lstFileTemp = Mockito.spy(ArrayList.class);
    lstFileTemp.add(gnocFileDto);
    lstFileTemp.add(gnocFileDto1);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> lsConfig = Mockito.spy(ArrayList.class);
    lsConfig.add(srConfigDTO);
    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setApprove(1L);
    srCatalogDTO.setExecutionTime("1");
    srCatalogDTO.setIsAddDay(1L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUserId(1L);
    usersEntity.setUnitId(1L);
    SrInsiteDTO objBefore = Mockito.spy(SrInsiteDTO.class);
    objBefore.setStatus("1");
    SRActionCodeDTO srActionCodeDTO = Mockito.spy(SRActionCodeDTO.class);
    srActionCodeDTO.setActionCode("1");
    srActionCodeDTO.setDefaultComment("1");
    List<SRActionCodeDTO> lsActionCode = Mockito.spy(ArrayList.class);
    lsActionCode.add(srActionCodeDTO);
    PowerMockito.when(srRepository.searchSrActionCode(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lsActionCode);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(objBefore);
    PowerMockito.when(srRepository.getUnitParentForApprove(anyString(), anyString())).thenReturn("1");
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lsConfig);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getOffsetFromUser(anyString())).thenReturn(1.1);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileTemp);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(lsConfig);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileTemp);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.updateSR(srDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void updateSR_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(DataUtil.class);

    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Under_Approval");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrId(1L);
    srDTO.setServiceId("1");
    srDTO.setOpenConnect(true);
    srDTO.setAutoCreatCR(true);
    srDTO.setServiceNims(true);
    srDTO.setStatus("New");
    srDTO.setStartTime(new Date());
    srDTO.setUpdatedTime(new Date());
    srDTO.setUpdatedUser("1");
    srDTO.setOpenConnect(true);
    srDTO.setSrUser("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("2");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("FILE_KY");
    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setFileType("OPEN_CONNECT");
    List<GnocFileDto> lstFileTemp = Mockito.spy(ArrayList.class);
    lstFileTemp.add(gnocFileDto);
    lstFileTemp.add(gnocFileDto1);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("New");
    List<SRConfigDTO> lsConfig = Mockito.spy(ArrayList.class);
    lsConfig.add(srConfigDTO);
    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setApprove(1L);
    srCatalogDTO.setExecutionTime("1");
    srCatalogDTO.setIsAddDay(1L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUserId(1L);
    usersEntity.setUnitId(1L);
    SrInsiteDTO objBefore = Mockito.spy(SrInsiteDTO.class);
    objBefore.setStatus("New");
    SRActionCodeDTO srActionCodeDTO = Mockito.spy(SRActionCodeDTO.class);
    srActionCodeDTO.setActionCode("1");
    srActionCodeDTO.setDefaultComment("1");
    List<SRActionCodeDTO> lsActionCode = Mockito.spy(ArrayList.class);
    lsActionCode.add(srActionCodeDTO);
    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(srRepository.searchSrActionCode(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lsActionCode);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(objBefore);
    PowerMockito.when(srRepository.getUnitParentForApprove(anyString(), anyString())).thenReturn("1");
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lsConfig);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getOffsetFromUser(anyString())).thenReturn(1.1);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileTemp);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(lsConfig);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileTemp);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.updateSR(srDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void updateSR_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(DataUtil.class);

    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setRoleCode("1");
    srDTO.setStartTime(DateTimeUtils.convertStringToDate("05/05/2022 03:03:03"));
    srDTO.setEndTime(DateTimeUtils.convertStringToDate("05/05/2023 03:03:03"));
    srDTO.setStatus("Under_Approval");
    srDTO.setEvaluate("1");
    srDTO.setReviewId(1L);
    srDTO.setSrId(1L);
    srDTO.setServiceId("1");
    srDTO.setOpenConnect(true);
    srDTO.setAutoCreatCR(true);
    srDTO.setServiceNims(true);
    srDTO.setStatus("Cancelled");
    srDTO.setStartTime(new Date());
    srDTO.setUpdatedTime(new Date());
    srDTO.setUpdatedUser("1");
    srDTO.setOpenConnect(true);
    srDTO.setSrUser("1");
    List<SrInsiteDTO> srInsiteDTOList = Mockito.spy(ArrayList.class);
    srInsiteDTOList.add(srDTO);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("2");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("FILE_KY");
    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setFileType("OPEN_CONNECT");
    List<GnocFileDto> lstFileTemp = Mockito.spy(ArrayList.class);
    lstFileTemp.add(gnocFileDto);
    lstFileTemp.add(gnocFileDto1);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("New");
    List<SRConfigDTO> lsConfig = Mockito.spy(ArrayList.class);
    lsConfig.add(srConfigDTO);
    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setApprove(1L);
    srCatalogDTO.setExecutionTime("1");
    srCatalogDTO.setIsAddDay(1L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUserId(1L);
    usersEntity.setUnitId(1L);
    SrInsiteDTO objBefore = Mockito.spy(SrInsiteDTO.class);
    objBefore.setStatus("Neww");
    SRActionCodeDTO srActionCodeDTO = Mockito.spy(SRActionCodeDTO.class);
    srActionCodeDTO.setActionCode("1");
    srActionCodeDTO.setDefaultComment("1");
    List<SRActionCodeDTO> lsActionCode = Mockito.spy(ArrayList.class);
    lsActionCode.add(srActionCodeDTO);
    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    PowerMockito.when(srRepository.getWorkLog(any(), anyInt(), anyInt(),anyBoolean())).thenReturn(srInsiteDTOList);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(srRepository.searchSrActionCode(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lsActionCode);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(objBefore);
    PowerMockito.when(srRepository.getUnitParentForApprove(anyString(), anyString())).thenReturn("1");
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lsConfig);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getOffsetFromUser(anyString())).thenReturn(1.1);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileTemp);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(lsConfig);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileTemp);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    ResultInSideDto result = srBusiness.updateSR(srDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }
  @Test
  public void saveSrChild_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    SrInsiteDTO srChild = Mockito.spy(SrInsiteDTO.class);
    srChild.setEndTime(new Date());
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.saveSrChild(srChild);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void saveSrChild_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    SrInsiteDTO srChild = Mockito.spy(SrInsiteDTO.class);
    srChild.setEndTime(new Date());
    srChild.setTitle("1");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.saveSrChild(srChild);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void saveSrChild_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    SrInsiteDTO srChild = Mockito.spy(SrInsiteDTO.class);
    srChild.setEndTime(new Date());
    srChild.setTitle("1");
    srChild.setDescription("1");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.saveSrChild(srChild);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void saveSrChild_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    SrInsiteDTO srChild = Mockito.spy(SrInsiteDTO.class);
    srChild.setEndTime(new Date());
    srChild.setTitle("1");
    srChild.setDescription("1");
    srChild.setServiceId("1");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = srBusiness.saveSrChild(srChild);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void saveSrChild_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    SrInsiteDTO srChild = Mockito.spy(SrInsiteDTO.class);
    srChild.setEndTime(new Date());
    srChild.setTitle("1");
    srChild.setDescription("1");
    srChild.setServiceId("1");
    srChild.setSrUnit(1L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    UnitSRCatalogDTO unitSRCatalogDTO = Mockito.spy(UnitSRCatalogDTO.class);
    unitSRCatalogDTO.setUnitId("1");
    unitSRCatalogDTO.setServiceId("1");
    List<UnitSRCatalogDTO> list = Mockito.spy(ArrayList.class);
    list.add(unitSRCatalogDTO);
    list.add(unitSRCatalogDTO);
    PowerMockito.when(srCatalogRepository2.getListUnitSRCatalog(any())).thenReturn(list);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    ResultInSideDto result = srBusiness.saveSrChild(srChild);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void saveSrChild_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    SrInsiteDTO srChild = Mockito.spy(SrInsiteDTO.class);
    srChild.setEndTime(new Date());
    srChild.setTitle("1");
    srChild.setDescription("1");
    srChild.setServiceId("1");
    srChild.setSrUnit(1L);
    srChild.setRoleCode("1");
    srChild.setSrId(1L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setServiceArray("1");
    srCatalogDTO.setCountry("1");
    UnitSRCatalogDTO unitSRCatalogDTO = Mockito.spy(UnitSRCatalogDTO.class);
    unitSRCatalogDTO.setUnitId("1");
    unitSRCatalogDTO.setServiceId("1");
    List<UnitSRCatalogDTO> list = Mockito.spy(ArrayList.class);
    list.add(unitSRCatalogDTO);
    list.add(unitSRCatalogDTO);
    List<String> lsSequense = Mockito.spy(ArrayList.class);
    lsSequense.add("1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srRepository.getListSequenseSR(any(), anyInt())).thenReturn(lsSequense);
    PowerMockito.when(srCatalogRepository2.getListUnitSRCatalog(any())).thenReturn(list);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    ResultInSideDto result = srBusiness.saveSrChild(srChild);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void saveSrChild_07() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    SrInsiteDTO srChild = Mockito.spy(SrInsiteDTO.class);
    srChild.setEndTime(new Date());
    srChild.setTitle("1");
    srChild.setDescription("1");
    srChild.setServiceId("1");
    srChild.setSrUnit(1L);
    srChild.setRoleCode("1");
    srChild.setSrId(1L);
    srChild.setParentCode("1");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setServiceArray("1");
    srCatalogDTO.setCountry("1");
    UnitSRCatalogDTO unitSRCatalogDTO = Mockito.spy(UnitSRCatalogDTO.class);
    unitSRCatalogDTO.setUnitId("1");
    unitSRCatalogDTO.setServiceId("1");
    List<UnitSRCatalogDTO> list = Mockito.spy(ArrayList.class);
    list.add(unitSRCatalogDTO);
    list.add(unitSRCatalogDTO);
    List<String> lsSequense = Mockito.spy(ArrayList.class);
    lsSequense.add("1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileName("1");
    gnocFileDto.setMappingId(1L);
    List<GnocFileDto> gnocFileDtosParent = Mockito.spy(ArrayList.class);
    gnocFileDtosParent.add(gnocFileDto);
    SRFilesEntity srFilesEntity = Mockito.spy(SRFilesEntity.class);
    srFilesEntity.setFileId(1L);
    List<SRFilesEntity> srFilesDTOSParent = Mockito.spy(ArrayList.class);
    srFilesDTOSParent.add(srFilesEntity);
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    resultFileDataOld.setId(1L);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(srRepository.getListSRFileByObejctId(anyLong())).thenReturn(srFilesDTOSParent);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtosParent);
    PowerMockito.when(userRepository.getUserDTOByUserName(any())).thenReturn(unitToken);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srRepository.getListSequenseSR(any(), anyInt())).thenReturn(lsSequense);
    PowerMockito.when(srCatalogRepository2.getListUnitSRCatalog(any())).thenReturn(list);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    ResultInSideDto result = srBusiness.saveSrChild(srChild);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void getSRCode_01() {
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("1");
    PowerMockito.when(catLocationRepository.getNationByLocationId(any())).thenReturn(catLocationDTO);
    srBusiness.getSRCode("1", 1L);

  }

  @Test
  public void getServiceArrayCBB_01() {
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setCountry("1");
    List<SRConfigDTO> lstServicesArray = Mockito.spy(ArrayList.class);
    lstServicesArray.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstServicesArray);
    srBusiness.getServiceArrayCBB("1","1");
  }

  @Test
  public void getServiceGroupCBB_01() {
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setCountry("1");
    List<SRConfigDTO> lstServicesGroup = Mockito.spy(ArrayList.class);
    lstServicesGroup.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstServicesGroup);
    srBusiness.getServiceGroupCBB("1", "1");
  }

  @Test
  public void getListServicesCBB_01() {
    SRCatalogDTO srConfigDTO = Mockito.spy(SRCatalogDTO.class);
    srConfigDTO.setCountry("1");
    srConfigDTO.setServiceCode("1");
    srConfigDTO.setServiceName("1");
    List<SRCatalogDTO> lstServicesGroup = Mockito.spy(ArrayList.class);
    lstServicesGroup.add(srConfigDTO);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstServicesGroup);
    srBusiness.getListServicesCBB("1");
  }

  @Test
  public void getListSRRoleCBB_01() {
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    srRoleDTO.setRoleCode("1");
    List<SRRoleDTO> lsRoleCode = Mockito.spy(ArrayList.class);
    lsRoleCode.add(srRoleDTO);
    PowerMockito.when(srRepository.getListSRRole(any())).thenReturn(lsRoleCode);
    srBusiness.getListSRRoleCBB(1L);
  }

  @Test
  public void getListUnitCBB_01() {
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(123L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    PowerMockito.when(unitBusiness.getListUnit(any())).thenReturn(listUnit);
    srBusiness.getListUnitCBB();
  }

  @Test
  public void getListUnitBySeviceCBB_01() {
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    UnitSRCatalogDTO unitSRCatalogDTO = Mockito.spy(UnitSRCatalogDTO.class);
    unitSRCatalogDTO.setUnitId("1");
    unitSRCatalogDTO.setUnitName("1");
    List<UnitSRCatalogDTO> lsUnitInService = Mockito.spy(ArrayList.class);
    lsUnitInService.add(unitSRCatalogDTO);
    PowerMockito.when(srCatalogRepository2.getListUnitSRCatalog(any())).thenReturn(lsUnitInService);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);

    srBusiness.getListUnitBySeviceCBB("1",true, "1");
  }

  @Test
  public void getListUnitBySeviceCBB_02() {
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    UnitSRCatalogDTO unitSRCatalogDTO = Mockito.spy(UnitSRCatalogDTO.class);
    unitSRCatalogDTO.setUnitId("1");
    unitSRCatalogDTO.setUnitName("1");
    List<UnitSRCatalogDTO> lsUnitInService = Mockito.spy(ArrayList.class);
    lsUnitInService.add(unitSRCatalogDTO);
    PowerMockito.when(srCatalogRepository2.getListUnitSRCatalog(any())).thenReturn(lsUnitInService);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    srBusiness.getListUnitBySeviceCBB("1",false, "1");
  }

  @Test
  public void getChangeExecutionTimeAndFlowExecute_01() {
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setExecutionTime("1");
    srCatalogDTO.setIsAddDay(1L);
    srCatalogDTO.setFlowExecuteName("1");
    List<SRCatalogDTO> lsUnitInService = Mockito.spy(ArrayList.class);
    lsUnitInService.add(srCatalogDTO);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lsUnitInService);
    srBusiness.getChangeExecutionTimeAndFlowExecute("20/05/2020 03:03:03", "1", "1");
  }

  @Test
  public void exportData_01() throws Exception{
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setCreatedTime(new Date());
    srInsiteDTO.setStatus("New");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(true);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setRemainExecutionTime("1");
    srInsiteDTO.setStartTime(new Date());
    srInsiteDTO.setExecutionTime("1");
    srInsiteDTO.setReplyTime("4");
    List<SrInsiteDTO> lstEx = Mockito.spy(ArrayList.class);
    lstEx.add(srInsiteDTO);
    SrInsiteDTO srDTO = Mockito.spy(SrInsiteDTO.class);
    srDTO.setCreatedTime(new Date());
    srDTO.setStatus("New");
    srDTO.setSrId(1L);
    srDTO.setOpenConnect(true);
    srDTO.setSrUser("1");
    srDTO.setSrCode("1");
    srDTO.setStartTime(new Date());
    srDTO.setRemainExecutionTime("1");
    srDTO.setExecutionTime("1");
    srDTO.setReplyTime("4");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRActionCodeDTO srActionCodeDTO = Mockito.spy(SRActionCodeDTO.class);
    srActionCodeDTO.setActionCode("1");
    srActionCodeDTO.setDefaultComment("1");
    List<SRActionCodeDTO> lsActionCode = Mockito.spy(ArrayList.class);
    lsActionCode.add(srActionCodeDTO);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> vipaHisComment = Mockito.spy(ArrayList.class);
    vipaHisComment.add(srConfigDTO);
    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setUsername("2");
    List<SRRoleUserInSideDTO> lstUserUnit = Mockito.spy(ArrayList.class);
    lstUserUnit.add(srRoleUserInSideDTO);
    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    userExecute.setUsername("1");
    userExecute.setMobile("1");
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitExecute);
    PowerMockito.when(srRepository.searchSRRoleUser(any())).thenReturn(lstUserUnit);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(vipaHisComment);
    PowerMockito.when(srRepository.searchSrActionCode(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lsActionCode);
    PowerMockito.when(srRepository.insertSR(srDTO)).thenReturn(resultInSideDto);
    PowerMockito.when(srRepository.getListSRExport(any())).thenReturn(lstEx);
    srBusiness.exportData(srInsiteDTO);
  }

  @Test
  public void getListCBBSeviceInsert_01() {
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setServiceArray("1");
    srInsiteDTO.setServiceGroup("1");
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setParentCode("1");
    srInsiteDTO.setServiceId("1");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setServiceName("1");
    List<SRCatalogDTO> lstServices = Mockito.spy(ArrayList.class);
    lstServices.add(srCatalogDTO);
    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTO.setServiceCodeChild("1");
    List<SRCatalogChildDTO> lstChild = Mockito.spy(ArrayList.class);
    lstChild.add(srCatalogChildDTO);
    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChild);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstServices);
    srBusiness.getListCBBSeviceInsert(srInsiteDTO);
  }

  @Test
  public void getListCBBSeviceInsert_02() {
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrUnit(0L);
    srInsiteDTO.setServiceArray("1");
    srInsiteDTO.setServiceGroup("1");
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setParentCode("1");
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrUser("1");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setServiceName("1");
    List<SRCatalogDTO> lstServices = Mockito.spy(ArrayList.class);
    lstServices.add(srCatalogDTO);
    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTO.setServiceCodeChild("1");
    List<SRCatalogChildDTO> lstChild = Mockito.spy(ArrayList.class);
    lstChild.add(srCatalogChildDTO);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> lsUnit = Mockito.spy(ArrayList.class);
    lsUnit.add(unitDTO);
    PowerMockito.when(srRepository.getListSRUnitForDetail(any())).thenReturn(lsUnit);
    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChild);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstServices);
    srBusiness.getListCBBSeviceInsert(srInsiteDTO);
  }

  @Test
  public void getCmbRoleCode_01() {
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setRoleCode("1");
    srCatalogDTO.setCountry("1");
    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setRoleName("1");
    List<SRRoleUserInSideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(srRoleUserInSideDTO);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lst);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    srBusiness.getCmbRoleCode("1", "1");
  }

  @Test
  public void getCmbSrUser_01() {
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserDTO.setInsertSR(true);
    srRoleUserDTO.setStatus("1");
    srRoleUserDTO.setCountry("1");
    srRoleUserDTO.setUnitId(1L);
    srRoleUserDTO.setRoleCode("1");
    List<SRRoleUserInSideDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(srRoleUserDTO);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lstRoleUser);
    srBusiness.getCmbSrUser(srRoleUserDTO);
  }

  @Test
  public void getCmbSrUser_02() {
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserDTO.setInsertSR(false);
    srRoleUserDTO.setStatus("1");
    srRoleUserDTO.setCountry("1");
    srRoleUserDTO.setUnitId(1L);
    srRoleUserDTO.setRoleCode("1");
    List<SRRoleUserInSideDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(srRoleUserDTO);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lstRoleUser);
    srBusiness.getCmbSrUser(srRoleUserDTO);
  }


  @Test
  public void getSRDetail_01() {
    SrInsiteDTO dto = Mockito.spy(SrInsiteDTO.class);
    dto.setSrId(1L);
    dto.setRemainExecutionTimeCheckStatus("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileName("1");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setStatus("Under_Approval");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setRoleCode("1");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);
    srCatalogDTO.setRenewDay(1L);
    srCatalogDTO.setApprove(1L);
    srInsiteDTO.setSrId(1L);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setAutomation("1");
    List<SRConfigDTO> statusList = Mockito.spy(ArrayList.class);
    statusList.add(srConfigDTO);
    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setUnitId(1L);
    srRoleUserInSideDTO.setIsLeader(1L);
    srRoleUserInSideDTO.setRoleCode("1");
    srRoleUserInSideDTO.setUsername("1");
    List<SRRoleUserInSideDTO> lsRoleUser = Mockito.spy(ArrayList.class);
    lsRoleUser.add(srRoleUserInSideDTO);
    SRRoleActionDTO roleActionDTO = Mockito.spy(SRRoleActionDTO.class);
    roleActionDTO.setFlowId(1L);
    roleActionDTO.setActions("1,1");
    roleActionDTO.setNextStatus("Under_Approval, Under_Approval");
    List<SRRoleActionDTO> lstDataUpdateSR = Mockito.spy(ArrayList.class);
    lstDataUpdateSR.add(roleActionDTO);
    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    List<SRCatalogChildDTO> lstChild = Mockito.spy(ArrayList.class);
    lstChild.add(srCatalogChildDTO);
    SRRenewDTO srRenewDto = Mockito.spy(SRRenewDTO.class);
    srRenewDto.setStatusRenew(1L);
    srRenewDto.setCreatedTime(new Date());
    SRApproveDTO srApproveDTO = Mockito.spy(SRApproveDTO.class);
    srApproveDTO.setApproveDateLevel1(new Date());
    UsersInsideDto createUser = Mockito.spy(UsersInsideDto.class);
    createUser.setUnitId(1L);
    SRMappingProcessCRDTO mappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    mappingProcessCRDTO.setId(1L);
    List<SRMappingProcessCRDTO> listSRMappingProcessCRDTO = Mockito.spy(ArrayList.class);
    listSRMappingProcessCRDTO.add(mappingProcessCRDTO);
    PowerMockito.when(srCategoryServiceProxy.getListSRMappingProcessCRDTO(any())).thenReturn(listSRMappingProcessCRDTO);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(statusList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(createUser);
    PowerMockito.when(srRepository.checkUserLoginIsLeader(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(true);
    PowerMockito.when(srApproveRepository.findSRApproveBySrId(anyLong())).thenReturn(srApproveDTO);
    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChild);
    PowerMockito.when(srCategoryServiceProxy.getListUser(anyString(), anyString(), anyString())).thenReturn(lsRoleUser);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleActionDTO(any())).thenReturn(lstDataUpdateSR);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lsRoleUser);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(statusList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    srBusiness.getSRDetail(dto);
  }

  @Test
  public void getBySRStatus_01() {
    PowerMockito.mockStatic(DataUtil.class);
    SRWorklogTypeDTO srWorklogTypeDTO = Mockito.spy(SRWorklogTypeDTO.class);
    List<SRWorklogTypeDTO> srWorklogTypeDTOS = Mockito.spy(ArrayList.class);
    srWorklogTypeDTOS.add(srWorklogTypeDTO);
    LanguageExchangeDTO languageExchangeDTO = Mockito.spy(LanguageExchangeDTO.class);
    List<LanguageExchangeDTO> lstLanguage = Mockito.spy(ArrayList.class);
    lstLanguage.add(languageExchangeDTO);
    PowerMockito.when(languageExchangeRepository.findBySql(anyString(), any(), any(), any())).thenReturn(lstLanguage);
    PowerMockito.when(srWorkLogRepository.getBySRStatus(any())).thenReturn(srWorklogTypeDTOS);
    srBusiness.getBySRStatus("1");
  }

  @Test
  public void getListSRWorklogWithUnit_01() {
    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    srWorkLogDTO.setWlTypeId(1L);
    srWorkLogDTO.setWlTypeName("1");
    List<SRWorkLogDTO> lstResult = Mockito.spy(ArrayList.class);
    lstResult.add(srWorkLogDTO);
    SRWorklogTypeDTO srWorklogTypeDTO = Mockito.spy(SRWorklogTypeDTO.class);
    srWorklogTypeDTO.setWlTypeCode("1");
    srWorklogTypeDTO.setWlTypeId(1L);
    List<SRWorklogTypeDTO> lstWorklogType = Mockito.spy(ArrayList.class);
    lstWorklogType.add(srWorklogTypeDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(lstResult);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogTypeDTO(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstWorklogType);
    srBusiness.getListSRWorklogWithUnit(1L);
  }

  @Test
  public void insertSRWorklog_01() {
    SRWorkLogDTO srWorklogDTO = Mockito.spy(SRWorkLogDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    srBusiness.insertSRWorklog(srWorklogDTO);
  }

  @Test
  public void getCmbReason() {
    SRWorklogTypeDTO srWorklogTypeDTO = Mockito.spy(SRWorklogTypeDTO.class);
    srWorklogTypeDTO.setWlTypeId(1L);
    srWorklogTypeDTO.setSrStatus("Rejected");
    List<SRWorklogTypeDTO> lstWorklogType = Mockito.spy(ArrayList.class);
    lstWorklogType.add(srWorklogTypeDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogTypeDTO(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstWorklogType);
    srBusiness.getCmbReason(1L);
  }

  @Test
  public void getCBBFileType_01() {
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setOpenConnect(true);
    srInsiteDTO.setServiceNims(true);
    srInsiteDTO.setServiceAom(true);
    srInsiteDTO.setAutoCreatCR(true);
    srInsiteDTO.setServiceId("1");
    SRConfig2DTO srConfig2DTO = Mockito.spy(SRConfig2DTO.class);
    srConfig2DTO.setConfigCode("FORM");
    List<SRConfig2DTO> lstFileQuery = Mockito.spy(ArrayList.class);
    lstFileQuery.add(srConfig2DTO);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setCountry("1");
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    List<SRMappingProcessCRDTO> lstSRMappingProcess = Mockito.spy(ArrayList.class);
    lstSRMappingProcess.add(srMappingProcessCRDTO);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCategoryServiceProxy.getListSRMappingProcessCRDTO(any())).thenReturn(lstSRMappingProcess);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srConfig2Repository.getFile(any())).thenReturn(lstFileQuery);
    srBusiness.getCBBFileType(srInsiteDTO);
  }


  @Test
  public void insertSRFile_01() throws Exception{
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
//    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);

    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_02() throws Exception{
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
//    gnocFileDto.setFileType("1");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);

    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_03() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("FILE_KY");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_04() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("FORM");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_05() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("FORM");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlxx");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_06() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("FORM");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn(".");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void insertSRFile_07() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("NIMS");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn(".");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_08() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("NIMS");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlxx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn(".");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }


  @Test
  public void insertSRFile_09() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("NIMS");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn(".");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_10() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("AOM_TK");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn(".");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_11() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("AOM");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn(".");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_12() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("AOM");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlxx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn(".");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_13() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("AOM");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn(".");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_14() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(Workbook.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("sr.template.nims")).thenReturn("BM_IP_Request");
    PowerMockito.when(I18n.getLanguage("sr.template.aom")).thenReturn("BM_GateAOM");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("AOM");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn("/temp");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }


  @Test
  public void insertSRFile_15() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(Workbook.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("sr.template.nims")).thenReturn("BM_IP_Request");
    PowerMockito.when(I18n.getLanguage("sr.template.aom")).thenReturn("BM_GateAOM");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(false);
    srInsiteDTO.setAutoCreatCR(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("TOOL");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    gnocFileDto.setTemplateId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("VMSA");
    srConfigDTO.setConfigCode("https://myDevServer.com/dev/api/gate");
    List<SRConfigDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstData);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(lstData);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn("/temp");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_16() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(Workbook.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(com.viettel.vmsa.ResultCreateDtByFileInput.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("sr.template.nims")).thenReturn("BM_IP_Request");
    PowerMockito.when(I18n.getLanguage("sr.template.aom")).thenReturn("BM_GateAOM");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(false);
    srInsiteDTO.setAutoCreatCR(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("TOOL");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    gnocFileDto.setTemplateId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("VMSA");
    srConfigDTO.setConfigCode("https://myDevServer.com/dev/api/gate");
    List<SRConfigDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstData);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(lstData);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn("/temp");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_17() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(Workbook.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(com.viettel.vmsa.ResultCreateDtByFileInput.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("sr.template.nims")).thenReturn("BM_IP_Request");
    PowerMockito.when(I18n.getLanguage("sr.template.aom")).thenReturn("BM_GateAOM");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(false);
    srInsiteDTO.setAutoCreatCR(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("TOOL");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    gnocFileDto.setTemplateId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("AAM");
    srConfigDTO.setConfigCode("https://myDevServer.com/dev/api/gate");
    List<SRConfigDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstData);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(lstData);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn("/temp");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_18() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(Workbook.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(com.viettel.vmsa.ResultCreateDtByFileInput.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("sr.template.nims")).thenReturn("BM_IP_Request");
    PowerMockito.when(I18n.getLanguage("sr.template.aom")).thenReturn("BM_GateAOM");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(false);
    srInsiteDTO.setAutoCreatCR(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("TOOL");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    gnocFileDto.setTemplateId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("VIPA");
    srConfigDTO.setConfigCode("https://myDevServer.com/dev/api/gate");
    List<SRConfigDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstData);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(lstData);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn("/temp");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_19() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(Workbook.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(com.viettel.vmsa.ResultCreateDtByFileInput.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("sr.template.nims")).thenReturn("BM_IP_Request");
    PowerMockito.when(I18n.getLanguage("sr.template.aom")).thenReturn("BM_GateAOM");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(false);
    srInsiteDTO.setAutoCreatCR(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("OPEN_CONNECT");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    gnocFileDto.setTemplateId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("VIPA");
    srConfigDTO.setConfigCode("https://myDevServer.com/dev/api/gate");
    List<SRConfigDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstData);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(lstData);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn("/temp");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void insertSRFile_20() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
//    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(Workbook.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(com.viettel.vmsa.ResultCreateDtByFileInput.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("sr.attach.file.formMetro")).thenReturn("Form_SR_Input_Metro.xlsx");
    PowerMockito.when(I18n.getLanguage("sr.template.nims")).thenReturn("BM_IP_Request");
    PowerMockito.when(I18n.getLanguage("sr.template.aom")).thenReturn("BM_GateAOM");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");

    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(true);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(false);
    srInsiteDTO.setAutoCreatCR(true);
    srInsiteDTO.setDvTrungKe(true);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("FORM");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    gnocFileDto.setTemplateId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }

    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("https://myDevServer.com/dev/api/gate");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("VIPA");
    srConfigDTO.setConfigCode("https://myDevServer.com/dev/api/gate");
    List<SRConfigDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstData);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(lstData);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn("templates/Form_SR_Input_Metro.xlsx");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void insertSRFile_21() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
//    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(Workbook.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(com.viettel.vmsa.ResultCreateDtByFileInput.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("sr.attach.file.formMetro")).thenReturn("Form_SR_Input_Metro.xlsx");
    PowerMockito.when(I18n.getLanguage("sr.template.nims")).thenReturn("BM_IP_Request");
    PowerMockito.when(I18n.getLanguage("sr.template.aom")).thenReturn("BM_GateAOM");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");

    MockMultipartFile firstFile = new MockMultipartFile("data", "BM_IP_Request.xlsx", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(false);
    srInsiteDTO.setDvTrungKe(false);
    srInsiteDTO.setServiceNims(true);
    srInsiteDTO.setServiceAom(false);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("NIMSssss");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    gnocFileDto.setTemplateId(1L);


    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setFileType("NIMS");
    gnocFileDto1.setIndexFile(1L);
    gnocFileDto1.setFileName("1");
    gnocFileDto1.setPath("xnxx.xlsx");
    gnocFileDto1.setTemplateId(1L);

    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);

    List<GnocFileDto> gnocFileDtoList1 = Mockito.spy(ArrayList.class);
    gnocFileDtoList1.add(gnocFileDto1);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList1);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }

    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("https://myDevServer.com/dev/api/gate");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("VIPA");
    srConfigDTO.setConfigCode("https://myDevServer.com/dev/api/gate");
    List<SRConfigDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(srConfigDTO);
//    String pathTemplate = StringUtils.removeSeparator("BM_IP_Request.xlsx");
//    Resource resource = new ClassPathResource(pathTemplate);
//    InputStream fileTemplate = resource.getInputStream();
//    XSSFWorkbook workbook_temp = new XSSFWorkbook(fileTemplate);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstData);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(lstData);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn("templates" + File.separator + "BM_IP_Request" + ".xlsx");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertSRFile_22() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
//    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(Workbook.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(com.viettel.vmsa.ResultCreateDtByFileInput.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("sr.attach.file.formMetro")).thenReturn("Form_SR_Input_Metro.xlsx");
    PowerMockito.when(I18n.getLanguage("sr.template.nims")).thenReturn("BM_IP_Request");
    PowerMockito.when(I18n.getLanguage("sr.template.aom")).thenReturn("BM_GateAOM");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");

    MockMultipartFile firstFile = new MockMultipartFile("data", "BM_IP_Request.xlsx", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setOpenConnect(true);
    srInsiteDTO.setDvTrungKe(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceAom(false);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("OPEN_CONNECTT");
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setPath("xnxx.xlsx");
    gnocFileDto.setTemplateId(1L);


    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setFileType("OPEN_CONNECT");
    gnocFileDto1.setIndexFile(1L);
    gnocFileDto1.setFileName("1");
    gnocFileDto1.setPath("xnxx.xlsx");
    gnocFileDto1.setTemplateId(1L);

    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);

    List<GnocFileDto> gnocFileDtoList1 = Mockito.spy(ArrayList.class);
    gnocFileDtoList1.add(gnocFileDto1);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList1);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }

    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("https://myDevServer.com/dev/api/gate");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("VIPA");
    srConfigDTO.setConfigCode("https://myDevServer.com/dev/api/gate");
    List<SRConfigDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstData);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(lstData);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(any(),any(), any())).thenReturn("templates" + File.separator + "BM_IP_Request" + ".xlsx");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(unitToken);
    ResultInSideDto result = srBusiness.insertSRFile(srFileList, srInsiteDTO);
    Assert.assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void insertOrUpdateSRRenew_01() {
    SRRenewDTO srRenewDTO = Mockito.spy(SRRenewDTO.class);
    srRenewDTO.setEndTime(new Date());
    srRenewDTO.setEndTimeRenew(new Date());
    srRenewDTO.setCreatedTime(new Date());
    srRenewDTO.setUpdatedTime(new Date());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyString())).thenReturn(1.1);
    srBusiness.insertOrUpdateSRRenew(srRenewDTO);

  }

  @Test
  public void insertOrUpdateSRRenew_02() {
    SRRenewDTO srRenewDTO = Mockito.spy(SRRenewDTO.class);
    srRenewDTO.setEndTime(new Date());
    srRenewDTO.setEndTimeRenew(new Date());
    srRenewDTO.setCreatedTime(new Date());
    srRenewDTO.setUpdatedTime(new Date());
    srRenewDTO.setSrId(1L);
    srRenewDTO.setRenewId(1L);
    srRenewDTO.setStatusRenew(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setCreatedUser("1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    PowerMockito.when(userRepository.getUserDTOByUserName(any())).thenReturn(userExecute);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitExecute);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(srRenewRepository.updateSRRenew(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getOffsetFromUser(anyString())).thenReturn(1.1);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    PowerMockito.when(srRenewRepository.insertSRRenew(any())).thenReturn(resultInSideDto);

    ResultInSideDto result = srBusiness.insertOrUpdateSRRenew(srRenewDTO);
    Assert.assertEquals(result.getKey(), "SUCCESS");

  }

  @Test
  public void insertOrUpdateSRRenew_03() {
    SRRenewDTO srRenewDTO = Mockito.spy(SRRenewDTO.class);
    srRenewDTO.setEndTime(new Date());
    srRenewDTO.setEndTimeRenew(new Date());
    srRenewDTO.setCreatedTime(new Date());
    srRenewDTO.setUpdatedTime(new Date());
    srRenewDTO.setSrId(1L);
    srRenewDTO.setStatusRenew(1L);
//    srRenewDTO.setRenewId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setSrUser("1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    PowerMockito.when(srRenewRepository.insertSRRenew(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getUserDTOByUserName(any())).thenReturn(userExecute);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitExecute);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(srRenewRepository.updateSRRenew(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getOffsetFromUser(anyString())).thenReturn(1.1);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.insertOrUpdateSRRenew(srRenewDTO);
    Assert.assertEquals(result.getKey(), "SUCCESS");

  }

  @Test
  public void validateFileForm() {
  }

  @Test
  public void getDataByConfigCode() {
  }

  @Test
  public void readFileExcel() {
  }

  @Test
  public void getListSRFile_01() {
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("1");
    List<GnocFileDto> lst = Mockito.spy(ArrayList.class);
    lst.add(gnocFileDto);
    SRConfig2DTO fileType = Mockito.spy(SRConfig2DTO.class);
    fileType.setConfigDes("1");
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lst);
    PowerMockito.when(srConfig2Repository.getFileTypeByConfigCode(anyString(), anyString(), anyString())).thenReturn(fileType);
    srBusiness.getListSRFile(gnocFileDto);
  }

  @Test
  public void getCBBCmbFileName_01() throws Exception{
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentId(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
    List<SRMappingProcessCRDTO> lstSRMappingProcess = Mockito.spy(ArrayList.class);
    lstSRMappingProcess.add(srMappingProcessCRDTO);
    List<SRConfigDTO> lstDataCountry = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigName("1");
    srConfigDTO.setAutomation("VMSA");
    srConfigDTO.setConfigCode("http://10.240.202.12:8668/MSChangeProcess/WSForGNOC?wsdl");
    lstDataCountry.add(srConfigDTO);
    SrWsToolCrDTO srWsToolCrDTO = Mockito.spy(SrWsToolCrDTO.class);
    srWsToolCrDTO.setFileType("TOOL");
    srWsToolCrDTO.setLstDataCountry(lstDataCountry);
    srWsToolCrDTO.setLstSRMappingProcess(lstSRMappingProcess);
    srWsToolCrDTO.setLstData(lstDataCountry);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(srConfigRepository.getByConfigGroup(any())).thenReturn(lstDataCountry);
    srBusiness.getCBBCmbFileName(srWsToolCrDTO);
  }

  @Test
  public void getCBBCmbFileName_02() throws Exception{
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentId(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
    List<SRMappingProcessCRDTO> lstSRMappingProcess = Mockito.spy(ArrayList.class);
    lstSRMappingProcess.add(srMappingProcessCRDTO);
    List<SRConfigDTO> lstDataCountry = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigName("1");
    srConfigDTO.setAutomation("AAM");
    srConfigDTO.setConfigCode("http://10.240.202.12:8668/MSChangeProcess/WSForGNOC?wsdl");
    lstDataCountry.add(srConfigDTO);
    SrWsToolCrDTO srWsToolCrDTO = Mockito.spy(SrWsToolCrDTO.class);
    srWsToolCrDTO.setFileType("TOOL");
    srWsToolCrDTO.setLstDataCountry(lstDataCountry);
    srWsToolCrDTO.setLstSRMappingProcess(lstSRMappingProcess);
    srWsToolCrDTO.setLstData(lstDataCountry);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(srConfigRepository.getByConfigGroup(any())).thenReturn(lstDataCountry);
    srBusiness.getCBBCmbFileName(srWsToolCrDTO);
  }

  @Test
  public void getCBBCmbFileName_03() throws Exception{
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentId(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
    List<SRMappingProcessCRDTO> lstSRMappingProcess = Mockito.spy(ArrayList.class);
    lstSRMappingProcess.add(srMappingProcessCRDTO);
    List<SRConfigDTO> lstDataCountry = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigName("1");
    srConfigDTO.setAutomation("VIPA");
    srConfigDTO.setConfigCode("http://10.240.202.12:8668/MSChangeProcess/WSForGNOC?wsdl");
    lstDataCountry.add(srConfigDTO);
    SrWsToolCrDTO srWsToolCrDTO = Mockito.spy(SrWsToolCrDTO.class);
    srWsToolCrDTO.setFileType("TOOL");
    srWsToolCrDTO.setLstDataCountry(lstDataCountry);
    srWsToolCrDTO.setLstSRMappingProcess(lstSRMappingProcess);
    srWsToolCrDTO.setLstData(lstDataCountry);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(srConfigRepository.getByConfigGroup(any())).thenReturn(lstDataCountry);
    srBusiness.getCBBCmbFileName(srWsToolCrDTO);
  }

  @Test
  public void findSRApproveBySrId_01() {
    srBusiness.findSRApproveBySrId(1L);
  }

  @Test
  public void insertOrUpdateSRApprove_01() {
    SRApproveDTO srApproveDTO = Mockito.spy(SRApproveDTO.class);
    srBusiness.insertOrUpdateSRApprove(srApproveDTO);
  }

  @Test
  public void insertOrUpdateSRApprove_02() {
    SRApproveDTO srApproveDTO = Mockito.spy(SRApproveDTO.class);
    srApproveDTO.setApproveId(1L);
    srBusiness.insertOrUpdateSRApprove(srApproveDTO);
  }

  @Test
  public void findSRRenewBySrId_01() {
    srBusiness.findSRRenewBySrId(1L);
  }

  @Test
  public void deleteSR_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    SrInsiteDTO dto = Mockito.spy(SrInsiteDTO.class);
    dto.setSrId(1L);
    dto.setFlowExecute("1");
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setStatus("Draft");
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setSrId(1L);

    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setIsLeader(1L);
    List<SRRoleUserInSideDTO> lsRoleUser = Mockito.spy(ArrayList.class);
    lsRoleUser.add(srRoleUserInSideDTO);

    SRRoleActionDTO srRoleActionDTO = Mockito.spy(SRRoleActionDTO.class);
    srRoleActionDTO.setActions("1,D,1,1");
    List<SRRoleActionDTO> lstDataUpdateSR = Mockito.spy(ArrayList.class);
    lstDataUpdateSR.add(srRoleActionDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleActionDTO(any())).thenReturn(lstDataUpdateSR);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lsRoleUser);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    PowerMockito.when(srRepository.deleteSR(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto result = srBusiness.deleteSR(dto);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void deleteSRChild_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    SrInsiteDTO dto = Mockito.spy(SrInsiteDTO.class);
    dto.setSrId(1L);
    dto.setFlowExecute("1");
    dto.setCreatedUser("1");
    dto.setStatus("Draft");

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setStatus("Draft");
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setSrId(1L);

    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setIsLeader(1L);
    List<SRRoleUserInSideDTO> lsRoleUser = Mockito.spy(ArrayList.class);
    lsRoleUser.add(srRoleUserInSideDTO);

    SRRoleActionDTO srRoleActionDTO = Mockito.spy(SRRoleActionDTO.class);
    srRoleActionDTO.setActions("1,D,1,1");
    List<SRRoleActionDTO> lstDataUpdateSR = Mockito.spy(ArrayList.class);
    lstDataUpdateSR.add(srRoleActionDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleActionDTO(any())).thenReturn(lstDataUpdateSR);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lsRoleUser);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    PowerMockito.when(srRepository.deleteSR(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto result = srBusiness.deleteSRChild(dto);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }


  @Test
  public void deleteSRChild_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    SrInsiteDTO dto = Mockito.spy(SrInsiteDTO.class);
    dto.setSrId(1L);
    dto.setFlowExecute("1");
    dto.setCreatedUser("1");
    dto.setStatus("Draft");

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setStatus("Draft");
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setSrId(1L);

    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setIsLeader(1L);
    List<SRRoleUserInSideDTO> lsRoleUser = Mockito.spy(ArrayList.class);
//    lsRoleUser.add(srRoleUserInSideDTO);

    SRRoleActionDTO srRoleActionDTO = Mockito.spy(SRRoleActionDTO.class);
    srRoleActionDTO.setActions("1,D,1,1");
    List<SRRoleActionDTO> lstDataUpdateSR = Mockito.spy(ArrayList.class);
    lstDataUpdateSR.add(srRoleActionDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleActionDTO(any())).thenReturn(lstDataUpdateSR);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lsRoleUser);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    PowerMockito.when(srRepository.deleteSR(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto result = srBusiness.deleteSRChild(dto);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void roleActionSRChild_01() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRRoleUserInSideDTO roleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    roleUserInSideDTO.setRoleCode("1");
    List<SRRoleUserInSideDTO> lsRoleUser = Mockito.spy(ArrayList.class);
    lsRoleUser.add(roleUserInSideDTO);

    SRRoleActionDTO srRoleActionDTO = Mockito.spy(SRRoleActionDTO.class);
    srRoleActionDTO.setActions("1,1,1,1,1,1");
    List<SRRoleActionDTO> lstRoleAction = Mockito.spy(ArrayList.class);
    lstRoleAction.add(srRoleActionDTO);

    PowerMockito.when(srCategoryServiceProxy.getListSRRoleActionDTO(any())).thenReturn(lstRoleAction);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lsRoleUser);
    srBusiness.roleActionSRChild("1");
  }

  @Test
  public void insertListSRParam_01() {
    List<SRParamDTO> lsSrParam = Mockito.spy(ArrayList.class);
    srBusiness.insertListSRParam(lsSrParam);
  }

  @Test
  public void findListSRParamBySrId_01() {
    SRParamEntity srParamEntity = Mockito.spy(SRParamEntity.class);
    srParamEntity.setSrParamId(123L);
    List<SRParamEntity> lstResult = Mockito.spy(ArrayList.class);
    lstResult.add(srParamEntity);
    PowerMockito.when(srParamRepository.findListSRParamBySrId(anyLong())).thenReturn(lstResult);
    srBusiness.findListSRParamBySrId(1L);
  }

  @Test
  public void getListSRMopDTO_01() {
    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srBusiness.getListSRMopDTO(srMopDTO);
  }

  @Test
  public void getListSRMopNotSR_01() {
    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srBusiness.getListSRMopNotSR(srMopDTO);
  }

  @Test
  public void insertListSRMop_01() {
    List<SRMopDTO> lsSrMop = Mockito.spy(ArrayList.class);
    srBusiness.insertListSRMop(lsSrMop);
  }

  @Test
  public void updateListSRMop_01() {
    List<SRMopDTO> lsSrMop = Mockito.spy(ArrayList.class);
    srBusiness.updateListSRMop(lsSrMop);
  }

  @Test
  public void deleteListSRMop_01() {
    List<SRMopDTO> lsSrMop = Mockito.spy(ArrayList.class);
    srBusiness.deleteListSRMop(lsSrMop);
  }

  @Test
  public void insertOrUpdateSREvaluate_01() {
    SREvaluateDTO srEvaluateDTO = Mockito.spy(SREvaluateDTO.class);
    srEvaluateDTO.setEvaluateId("1");
    srBusiness.insertOrUpdateSREvaluate(srEvaluateDTO);
  }

  @Test
  public void findSREvaluateBySrId_01() {
    srBusiness.findSREvaluateBySrId(1L);
  }

  @Test
  public void findListOdBySr() {
  }

  @Test
  public void findListWoBySr_01() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    SrInsiteDTO srDto = Mockito.spy(SrInsiteDTO.class);
    srDto.setSrId(1L);
    srDto.setSrCode("1");

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWoSearch = Mockito.spy(ArrayList.class);
    lstWoSearch.add(woDTOSearch);

    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setObjectId(1L);
    List<SRCreatedFromOtherSysDTO> lstWoTickHelp = Mockito.spy(ArrayList.class);
    lstWoTickHelp.add(srCreatedFromOtherSysDTO);

    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setFtId(1L);

    PowerMockito.when(woServiceProxy.findWoByIdProxy(anyLong())).thenReturn(woInsideDTO);
    PowerMockito.when(srCreatedFromOtherSysRepository.getListSRCreatedFromOtherSysDTO(any())).thenReturn(lstWoTickHelp);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWoSearch);
    srBusiness.findListWoBySr(srDto);
  }

  @Test
  public void findListCrBySr() {
  }

  @Test
  public void getListUnitSRCatalog() {
  }

  @Test
  public void fileByPath_01() throws Exception{
    SrWsToolCrDTO srWsToolCrDTO = Mockito.spy(SrWsToolCrDTO.class);
    srWsToolCrDTO.setFileType("OPEN_CONNECT");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    srBusiness.fileByPath(srWsToolCrDTO);
  }

  @Test
  public void fileByPath_02() throws Exception{
    SrWsToolCrDTO srWsToolCrDTO = Mockito.spy(SrWsToolCrDTO.class);
    srWsToolCrDTO.setFileType("NIMS");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    srBusiness.fileByPath(srWsToolCrDTO);
  }

  @Test
  public void fileByPath_03() throws Exception{
    SrWsToolCrDTO srWsToolCrDTO = Mockito.spy(SrWsToolCrDTO.class);
    srWsToolCrDTO.setFileType("AOM");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    srBusiness.fileByPath(srWsToolCrDTO);
  }

  @Test
  public void fileByPath_04() throws Exception{
    SrWsToolCrDTO srWsToolCrDTO = Mockito.spy(SrWsToolCrDTO.class);
    srWsToolCrDTO.setFileType("TOOL");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    srBusiness.fileByPath(srWsToolCrDTO);
  }

  @Test
  public void fileByPath_06() throws Exception{
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("AAM");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    List<SRMappingProcessCRDTO> lstSRMappingProcess = Mockito.spy(ArrayList.class);
    lstSRMappingProcess.add(srMappingProcessCRDTO);
    SrWsToolCrDTO srWsToolCrDTO = Mockito.spy(SrWsToolCrDTO.class);
    srWsToolCrDTO.setFileType("TOOL");
    srWsToolCrDTO.setLstData(configDTOList);
    srWsToolCrDTO.setLstDataCountry(configDTOList);
    srWsToolCrDTO.setTemplateId("1");
    srWsToolCrDTO.setLstSRMappingProcess(lstSRMappingProcess);
    srBusiness.fileByPath(srWsToolCrDTO);
  }

  @Test
  public void getListSRDialogFile() {
  }

  @Test
  public void insertSRDialogFile_01() throws Exception{
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setRequired(1L);
    gnocFileDto.setTemplateId(1L);
    gnocFileDto.setPathTemplate("1");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    gnocFileDtoList.add(gnocFileDto);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    PowerMockito.when(FileUtils.getFileName(anyString())).thenReturn("1.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any())).thenReturn("1.xlsx");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    srBusiness.insertSRDialogFile(srFileList, srInsiteDTO);
  }

  @Test
  public void insertSRDialogFile_02() throws Exception{
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setIndexFile(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setRequired(1L);
    gnocFileDto.setTemplateId(1L);
    gnocFileDto.setPathTemplate("1");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    gnocFileDtoList.add(gnocFileDto);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setGnocFileDtosAdd(gnocFileDtoList);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
    List<MultipartFile> srFileList = Mockito.spy(ArrayList.class);
    srFileList.add(firstFile);
    srFileList.add(firstFile);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    PowerMockito.when(FileUtils.getFileName(anyString())).thenReturn("1.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any())).thenReturn("1.xlsx");
    PowerMockito.when(FileUtils.saveTempFile(gnocFileDto.getFileName(), gnocFileDto.getBytes(), "./sr-temp")).thenReturn("1111111.xlsx");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    srBusiness.insertSRDialogFile(srFileList, srInsiteDTO);
  }

  @Test
  public void validateSRDTO_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
//    gnocFileDtoList.add(gnocFileDto);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("New");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//    srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    // 25/05/2020
    srInsiteDTO.setTitle("1");
    srInsiteDTO.setDescription("1");
    srInsiteDTO.setServiceArray("1");
    srInsiteDTO.setServiceGroup("1");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    // 25/05/2020
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(true, srInsiteDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("New");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//    srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    // 25/05/2020
    srInsiteDTO.setTitle("1");
    srInsiteDTO.setDescription("1");
    srInsiteDTO.setServiceArray("1");
    srInsiteDTO.setServiceGroup("1");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    // 25/05/2020
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(true, srInsiteDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("Concluded");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    // 25/05/2020
    srInsiteDTO.setTitle("1");
    srInsiteDTO.setDescription("1");
    srInsiteDTO.setServiceArray("1");
    srInsiteDTO.setServiceGroup("1");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    // 25/05/2020
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
//  crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("2");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);

    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(true, srInsiteDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("Concluded");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    // 25/05/2020
    srInsiteDTO.setTitle("1");
    srInsiteDTO.setDescription("1");
    srInsiteDTO.setServiceArray("1");
    srInsiteDTO.setServiceGroup("1");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    // 25/05/2020
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
//  crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("2");
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch);

    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(true, srInsiteDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("Concluded");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    // 25/05/2020
    srInsiteDTO.setTitle("1");
    srInsiteDTO.setDescription("1");
    srInsiteDTO.setServiceArray("1");
    srInsiteDTO.setServiceGroup("1");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    // 25/05/2020
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
  crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("2");
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch);

    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(true, srInsiteDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getString(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);

    SrInsiteDTO srDto = Mockito.spy(SrInsiteDTO.class);
    srDto.setRoleCode("1");
    srDto.setSrId(1L);
    srDto.setRoleCode("1");
    srDto.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srDto.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srDto.setStatus("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setSrUser("1");
    srDto.setCreatedUser("1");
    srDto.setAddNewFile(false);
    srDto.setServiceNims(false);
    srDto.setServiceId("1");
    srDto.setDvTrungKe(true);
    srDto.setGnocFileDtos(gnocFileDtoList);
    srDto.setUpdatedTime(new Date());
    srDto.setSrCode("1");
    srDto.setConcludedWithCr(true);
    srDto.setSrUnit(1L);
    srDto.setCountry("1");
    srDto.setAutoCreatCR(true);
    srDto.setSrUser("1");
    // 25/05/2020
    srDto.setTitle("1");
    srDto.setDescription("1");
    srDto.setServiceArray("1");
    srDto.setServiceGroup("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setDataCheck(srDto);
    // 25/05/2020
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("New");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setAutoCreatCR(true);
    srInsiteDTO.setSrUser("1");
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch);

    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
//    lstSrCreatAuto.add(srCreateAutoCRDTO);

    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(false, srDto);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_07() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getString(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);

    SrInsiteDTO srDto = Mockito.spy(SrInsiteDTO.class);
    srDto.setRoleCode("1");
    srDto.setSrId(1L);
    srDto.setRoleCode("1");
    srDto.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srDto.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srDto.setStatus("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setSrUser("1");
    srDto.setCreatedUser("1");
    srDto.setAddNewFile(false);
    srDto.setServiceNims(false);
    srDto.setServiceId("1");
    srDto.setDvTrungKe(true);
    srDto.setGnocFileDtos(gnocFileDtoList);
    srDto.setUpdatedTime(new Date());
    srDto.setSrCode("1");
    srDto.setConcludedWithCr(true);
    srDto.setSrUnit(1L);
    srDto.setCountry("1");
    srDto.setAutoCreatCR(true);
    srDto.setSrUser("1");
    // 25/05/2020
    srDto.setTitle("1");
    srDto.setDescription("1");
    srDto.setServiceArray("1");
    srDto.setServiceGroup("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setDataCheck(srDto);
    // 25/05/2020
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("New");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setAutoCreatCR(true);
    srInsiteDTO.setSrUser("1");
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch);

    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);

    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(false, srDto);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_08() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getString(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    gnocFileDto.setRequired(1L);
    gnocFileDto.setFileName("1");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);

    SrInsiteDTO srDto = Mockito.spy(SrInsiteDTO.class);
    srDto.setRoleCode("1");
    srDto.setSrId(1L);
    srDto.setRoleCode("1");
    srDto.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srDto.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srDto.setStatus("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setSrUser("1");
    srDto.setCreatedUser("1");
    srDto.setAddNewFile(false);
    srDto.setServiceNims(false);
    srDto.setServiceId("1");
    srDto.setDvTrungKe(true);
    srDto.setGnocFileDtos(gnocFileDtoList);
    srDto.setUpdatedTime(new Date());
    srDto.setSrCode("1");
    srDto.setConcludedWithCr(true);
    srDto.setSrUnit(1L);
    srDto.setCountry("1");
    srDto.setAutoCreatCR(true);
    srDto.setSrUser("1");
    srDto.setUpdatedUser("1");
    // 25/05/2020
    srDto.setTitle("1");
    srDto.setDescription("1");
    srDto.setServiceArray("1");
    srDto.setServiceGroup("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setDataCheck(srDto);
    // 25/05/2020
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("New");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setAutoCreatCR(true);
    srInsiteDTO.setSrUser("1");
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);
    srCatalogDTO.setServiceCode("1");

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch);

    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    srCreateAutoCRDTO.setExecutionTime(new Date());
    srCreateAutoCRDTO.setExecutionEndTime(new Date());
//    srCreateAutoCRDTO.setUnitImplement(1L);
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentId(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
//    srMappingProcessCRDTO.setIsCrNodes(1L);
//    srMappingProcessCRDTO.setUnitImplement(1L);
    List<SRMappingProcessCRDTO> listSRMapping = Mockito.spy(ArrayList.class);
    listSRMapping.add(srMappingProcessCRDTO);

    Object[] header = new Object[]{"1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(header);

    ResultInSideDto crNumber = Mockito.spy(ResultInSideDto.class);
    crNumber.setKey("ERROR");

    PowerMockito.when(crServiceProxy.getCrNumber(any())).thenReturn(crNumber);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File("/temp"), 0, 1, 0, 2, 1000)).thenReturn(dataImportList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(srCategoryServiceProxy.getListSRMappingProcessCRDTO(any())).thenReturn(listSRMapping);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(false, srDto);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_09() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getString(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    gnocFileDto.setRequired(1L);
    gnocFileDto.setFileName("1");
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);

    SrInsiteDTO srDto = Mockito.spy(SrInsiteDTO.class);
    srDto.setRoleCode("1");
    srDto.setSrId(1L);
    srDto.setRoleCode("1");
    srDto.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srDto.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srDto.setStatus("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setSrUser("1");
    srDto.setCreatedUser("1");
    srDto.setAddNewFile(false);
    srDto.setServiceNims(false);
    srDto.setServiceId("1");
    srDto.setDvTrungKe(true);
    srDto.setGnocFileDtos(gnocFileDtoList);
    srDto.setUpdatedTime(new Date());
    srDto.setSrCode("1");
    srDto.setConcludedWithCr(true);
    srDto.setSrUnit(1L);
    srDto.setCountry("1");
    srDto.setAutoCreatCR(true);
    srDto.setSrUser("1");
    srDto.setUpdatedUser("1");
    // 25/05/2020
    srDto.setTitle("1");
    srDto.setDescription("1");
    srDto.setServiceArray("1");
    srDto.setServiceGroup("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setDataCheck(srDto);
    // 25/05/2020
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("New");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setAutoCreatCR(true);
    srInsiteDTO.setSrUser("1");
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);
    srCatalogDTO.setServiceCode("1");

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch);

    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    srCreateAutoCRDTO.setExecutionTime(new Date());
    srCreateAutoCRDTO.setExecutionEndTime(new Date());
//    srCreateAutoCRDTO.setUnitImplement(1L);
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentId(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
//    srMappingProcessCRDTO.setIsCrNodes(1L);
//    srMappingProcessCRDTO.setUnitImplement(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
    List<SRMappingProcessCRDTO> listSRMapping = Mockito.spy(ArrayList.class);
    listSRMapping.add(srMappingProcessCRDTO);

    Object[] header = new Object[]{"1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(header);

    ResultInSideDto crNumber = Mockito.spy(ResultInSideDto.class);
    crNumber.setIdValue("1_2");
    crNumber.setKey("SUCCESS");

    CrProcessInsideDTO crProcessInsideDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessInsideDTO.setCrTypeId(1L);
    List<CrProcessInsideDTO> listCrProcess = Mockito.spy(ArrayList.class);
    listCrProcess.add(crProcessInsideDTO);

    PowerMockito.when(crCategoryServiceProxy.getListCrProcessDTO(any())).thenReturn(listCrProcess);
    PowerMockito.when(crServiceProxy.getCrNumber(any())).thenReturn(crNumber);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File("/temp"), 0, 1, 0, 2, 1000)).thenReturn(dataImportList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(srCategoryServiceProxy.getListSRMappingProcessCRDTO(any())).thenReturn(listSRMapping);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(false, srDto);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_10() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLocale()).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getString(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    gnocFileDto.setRequired(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setTemplateId(1L);

    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setPath("1");
    gnocFileDto1.setFileType("FORM");
    gnocFileDto1.setRequired(2L);
    gnocFileDto1.setFileName("1");
    gnocFileDto1.setTemplateId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    gnocFileDtoList.add(gnocFileDto1);

    SrInsiteDTO srDto = Mockito.spy(SrInsiteDTO.class);
    srDto.setRoleCode("1");
    srDto.setSrId(1L);
    srDto.setRoleCode("1");
    srDto.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srDto.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srDto.setStatus("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setSrUser("1");
    srDto.setCreatedUser("1");
    srDto.setAddNewFile(false);
    srDto.setServiceNims(false);
    srDto.setServiceId("1");
    srDto.setDvTrungKe(true);
    srDto.setGnocFileDtos(gnocFileDtoList);
    srDto.setUpdatedTime(new Date());
    srDto.setSrCode("1");
    srDto.setConcludedWithCr(true);
    srDto.setSrUnit(1L);
    srDto.setCountry("1");
    srDto.setAutoCreatCR(true);
    srDto.setSrUser("1");
    srDto.setUpdatedUser("1");
    // 25/05/2020
    srDto.setTitle("1");
    srDto.setDescription("1");
    srDto.setServiceArray("1");
    srDto.setServiceGroup("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setDataCheck(srDto);
    // 25/05/2020

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("New");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setAutoCreatCR(true);
    srInsiteDTO.setSrUser("1");
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);
    srCatalogDTO.setServiceCode("1");

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("AAM");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch);

    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    srCreateAutoCRDTO.setExecutionTime(new Date());
    srCreateAutoCRDTO.setExecutionEndTime(new Date());
//    srCreateAutoCRDTO.setUnitImplement(1L);
//    srCreateAutoCRDTO.setServiceAffecting(1L);
    srCreateAutoCRDTO.setAffectingService("1,1");
//    srCreateAutoCRDTO.setWoTestTypeId(1L);
//    srCreateAutoCRDTO.setWoFtTypeId(1L);
//    srCreateAutoCRDTO.setCrTitle("1");
//    srCreateAutoCRDTO.setCrStatus(1L);
    srCreateAutoCRDTO.setPathFileProcess("FORM_1;1;1;1;1");
    srCreateAutoCRDTO.setFileTypeId("FORM_1;1;1;1;1");
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentId(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
//    srMappingProcessCRDTO.setIsCrNodes(1L);
//    srMappingProcessCRDTO.setUnitImplement(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
    List<SRMappingProcessCRDTO> listSRMapping = Mockito.spy(ArrayList.class);
    listSRMapping.add(srMappingProcessCRDTO);

    Object[] header = new Object[]{"1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(header);

    ResultInSideDto crNumber = Mockito.spy(ResultInSideDto.class);
    crNumber.setIdValue("1_2");
    crNumber.setKey("SUCCESS");

    CrProcessInsideDTO crProcessInsideDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessInsideDTO.setCrTypeId(2L);
    List<CrProcessInsideDTO> listCrProcess = Mockito.spy(ArrayList.class);
    listCrProcess.add(crProcessInsideDTO);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    SrInsiteDTO location = Mockito.spy(SrInsiteDTO.class);
    location.setCountry("1/1/1/1");

    CatLocationDTO locationDTO = Mockito.spy(CatLocationDTO.class);
    locationDTO.setLocationCode("1");

    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srMopDTO.setDtId("1");
    srMopDTO.setIpNode("1");
    List<SRMopDTO> listMOP = Mockito.spy(ArrayList.class);
    listMOP.add(srMopDTO);
//    srDto.setLstMopTmp(listMOP);

    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    List<TemplateImportDTO> lstTemplateImport = Mockito.spy(ArrayList.class);
    lstTemplateImport.add(templateImportDTO);
    ObjectMapper objectMapper = Mockito.spy(ObjectMapper.class);
    PowerMockito.when(objectMapper.writeValueAsString(any())).thenReturn("temp");
    PowerMockito.when(crServiceProxy.insertListNoIDForImportForSR(any())).thenReturn(lstTemplateImport);
    PowerMockito.when(srMopRepository.getListSRMopDTO(any())).thenReturn(listMOP);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(locationDTO);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(location);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(FileUtils.getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString())).thenReturn(fileContent);
    PowerMockito.when(crCategoryServiceProxy.getListCrProcessDTO(any())).thenReturn(listCrProcess);
    PowerMockito.when(crServiceProxy.getCrNumber(any())).thenReturn(crNumber);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File("/temp"), 0, 1, 0, 2, 1000)).thenReturn(dataImportList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(srCategoryServiceProxy.getListSRMappingProcessCRDTO(any())).thenReturn(listSRMapping);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(false, srDto);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_11() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLocale()).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getString(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    gnocFileDto.setRequired(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setTemplateId(1L);

    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setPath("1");
    gnocFileDto1.setFileType("FORM");
    gnocFileDto1.setRequired(2L);
    gnocFileDto1.setFileName("1");
    gnocFileDto1.setTemplateId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    gnocFileDtoList.add(gnocFileDto1);

    SrInsiteDTO srDto = Mockito.spy(SrInsiteDTO.class);
    srDto.setRoleCode("1");
    srDto.setSrId(1L);
    srDto.setRoleCode("1");
    srDto.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srDto.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srDto.setStatus("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setSrUser("1");
    srDto.setCreatedUser("1");
    srDto.setAddNewFile(false);
    srDto.setServiceNims(false);
    srDto.setServiceId("1");
    srDto.setDvTrungKe(true);
    srDto.setGnocFileDtos(gnocFileDtoList);
    srDto.setUpdatedTime(new Date());
    srDto.setSrCode("1");
    srDto.setConcludedWithCr(true);
    srDto.setSrUnit(1L);
    srDto.setCountry("1");
    srDto.setAutoCreatCR(true);
    srDto.setSrUser("1");
    srDto.setUpdatedUser("1");
    // 25/05/2020
    srDto.setTitle("1");
    srDto.setDescription("1");
    srDto.setServiceArray("1");
    srDto.setServiceGroup("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setDataCheck(srDto);
    // 25/05/2020

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("New");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setAutoCreatCR(true);
    srInsiteDTO.setSrUser("1");
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);
    srCatalogDTO.setServiceCode("1");

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("AAM");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch);

    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    srCreateAutoCRDTO.setExecutionTime(new Date());
    srCreateAutoCRDTO.setExecutionEndTime(new Date());
//    srCreateAutoCRDTO.setUnitImplement(1L);
    srCreateAutoCRDTO.setServiceAffecting(1L);
    srCreateAutoCRDTO.setAffectingService("1,1");
//    srCreateAutoCRDTO.setWoTestTypeId(1L);
//    srCreateAutoCRDTO.setWoFtTypeId(1L);
//    srCreateAutoCRDTO.setCrTitle("1");
//    srCreateAutoCRDTO.setCrStatus(1L);
    srCreateAutoCRDTO.setPathFileProcess("FORM_1;1;1;1;1");
    srCreateAutoCRDTO.setFileTypeId("FORM_1;1;1;1;1");
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentId(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
//    srMappingProcessCRDTO.setIsCrNodes(1L);
//    srMappingProcessCRDTO.setUnitImplement(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
    List<SRMappingProcessCRDTO> listSRMapping = Mockito.spy(ArrayList.class);
    listSRMapping.add(srMappingProcessCRDTO);

    Object[] header = new Object[]{"1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(header);

    ResultInSideDto crNumber = Mockito.spy(ResultInSideDto.class);
    crNumber.setIdValue("1_2");
    crNumber.setKey("SUCCESS");

    CrProcessInsideDTO crProcessInsideDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessInsideDTO.setCrTypeId(2L);
    List<CrProcessInsideDTO> listCrProcess = Mockito.spy(ArrayList.class);
    listCrProcess.add(crProcessInsideDTO);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    SrInsiteDTO location = Mockito.spy(SrInsiteDTO.class);
    location.setCountry("1/1/1/1");

    CatLocationDTO locationDTO = Mockito.spy(CatLocationDTO.class);
    locationDTO.setLocationCode("1");

    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srMopDTO.setDtId("1");
    srMopDTO.setIpNode("1");
    List<SRMopDTO> listMOP = Mockito.spy(ArrayList.class);
    listMOP.add(srMopDTO);
//    srDto.setLstMopTmp(listMOP);

    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    List<TemplateImportDTO> lstTemplateImport = Mockito.spy(ArrayList.class);
    lstTemplateImport.add(templateImportDTO);
    ObjectMapper objectMapper = Mockito.spy(ObjectMapper.class);
    PowerMockito.when(objectMapper.writeValueAsString(any())).thenReturn("temp");
    PowerMockito.when(crServiceProxy.insertListNoIDForImportForSR(any())).thenReturn(lstTemplateImport);
    PowerMockito.when(srMopRepository.getListSRMopDTO(any())).thenReturn(listMOP);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(locationDTO);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(location);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(FileUtils.getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString())).thenReturn(fileContent);
    PowerMockito.when(crCategoryServiceProxy.getListCrProcessDTO(any())).thenReturn(listCrProcess);
    PowerMockito.when(crServiceProxy.getCrNumber(any())).thenReturn(crNumber);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File("/temp"), 0, 1, 0, 2, 1000)).thenReturn(dataImportList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(srCategoryServiceProxy.getListSRMappingProcessCRDTO(any())).thenReturn(listSRMapping);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(false, srDto);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_12() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLocale()).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getString(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    gnocFileDto.setRequired(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setTemplateId(1L);

    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setPath("1");
    gnocFileDto1.setFileType("FORM");
    gnocFileDto1.setRequired(2L);
    gnocFileDto1.setFileName("1");
    gnocFileDto1.setTemplateId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
//    gnocFileDtoList.add(gnocFileDto1);

    SrInsiteDTO srDto = Mockito.spy(SrInsiteDTO.class);
    srDto.setRoleCode("1");
    srDto.setSrId(1L);
    srDto.setRoleCode("1");
    srDto.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srDto.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srDto.setStatus("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setSrUser("1");
    srDto.setCreatedUser("1");
    srDto.setAddNewFile(false);
    srDto.setServiceNims(false);
    srDto.setServiceId("1");
    srDto.setDvTrungKe(true);
    srDto.setGnocFileDtos(gnocFileDtoList);
    srDto.setUpdatedTime(new Date());
    srDto.setSrCode("1");
    srDto.setConcludedWithCr(true);
    srDto.setSrUnit(1L);
    srDto.setCountry("1");
    srDto.setAutoCreatCR(true);
    srDto.setSrUser("1");
    srDto.setUpdatedUser("1");
    // 25/05/2020
    srDto.setTitle("1");
    srDto.setDescription("1");
    srDto.setServiceArray("1");
    srDto.setServiceGroup("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setDataCheck(srDto);
    // 25/05/2020

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("New");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setAutoCreatCR(true);
    srInsiteDTO.setSrUser("1");
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);
    srCatalogDTO.setServiceCode("1");

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("AAM");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch);

    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    srCreateAutoCRDTO.setExecutionTime(new Date());
    srCreateAutoCRDTO.setExecutionEndTime(new Date());
//    srCreateAutoCRDTO.setUnitImplement(1L);
    srCreateAutoCRDTO.setServiceAffecting(1L);
    srCreateAutoCRDTO.setAffectingService("1,1");
//    srCreateAutoCRDTO.setWoTestTypeId(1L);
//    srCreateAutoCRDTO.setWoFtTypeId(1L);
//    srCreateAutoCRDTO.setCrTitle("1");
//    srCreateAutoCRDTO.setCrStatus(1L);
    srCreateAutoCRDTO.setPathFileProcess("FORM_1;1;1;1;1");
    srCreateAutoCRDTO.setFileTypeId("FORM_1;1;1;1;1");
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentId(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
//    srMappingProcessCRDTO.setIsCrNodes(1L);
//    srMappingProcessCRDTO.setUnitImplement(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
    List<SRMappingProcessCRDTO> listSRMapping = Mockito.spy(ArrayList.class);
    listSRMapping.add(srMappingProcessCRDTO);

    Object[] header = new Object[]{"1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(header);

    ResultInSideDto crNumber = Mockito.spy(ResultInSideDto.class);
    crNumber.setIdValue("1_2");
    crNumber.setKey("SUCCESS");

    CrProcessInsideDTO crProcessInsideDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessInsideDTO.setCrTypeId(1L);
    List<CrProcessInsideDTO> listCrProcess = Mockito.spy(ArrayList.class);
    listCrProcess.add(crProcessInsideDTO);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    SrInsiteDTO location = Mockito.spy(SrInsiteDTO.class);
    location.setCountry("1/1/1/1");

    CatLocationDTO locationDTO = Mockito.spy(CatLocationDTO.class);
    locationDTO.setLocationCode("1");

    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srMopDTO.setDtId("1");
    srMopDTO.setIpNode("1");
    List<SRMopDTO> listMOP = Mockito.spy(ArrayList.class);
    listMOP.add(srMopDTO);
//    srDto.setLstMopTmp(listMOP);

    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    List<TemplateImportDTO> lstTemplateImport = Mockito.spy(ArrayList.class);
    lstTemplateImport.add(templateImportDTO);
    ObjectMapper objectMapper = Mockito.spy(ObjectMapper.class);
    PowerMockito.when(objectMapper.writeValueAsString(any())).thenReturn("temp");
    PowerMockito.when(crServiceProxy.insertListNoIDForImportForSR(any())).thenReturn(lstTemplateImport);
    PowerMockito.when(srMopRepository.getListSRMopDTO(any())).thenReturn(listMOP);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(locationDTO);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(location);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(FileUtils.getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString())).thenReturn(fileContent);
    PowerMockito.when(crCategoryServiceProxy.getListCrProcessDTO(any())).thenReturn(listCrProcess);
    PowerMockito.when(crServiceProxy.getCrNumber(any())).thenReturn(crNumber);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File("/temp"), 0, 1, 0, 2, 1000)).thenReturn(dataImportList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(srCategoryServiceProxy.getListSRMappingProcessCRDTO(any())).thenReturn(listSRMapping);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(false, srDto);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void validateSRDTO_13() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLocale()).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getString(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    gnocFileDto.setRequired(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setTemplateId(1L);

    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setPath("1");
    gnocFileDto1.setFileType("FORM");
    gnocFileDto1.setRequired(2L);
    gnocFileDto1.setFileName("1");
    gnocFileDto1.setTemplateId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
//    gnocFileDtoList.add(gnocFileDto1);

    SrInsiteDTO srDto = Mockito.spy(SrInsiteDTO.class);
    srDto.setRoleCode("1");
    srDto.setSrId(1L);
    srDto.setRoleCode("1");
    srDto.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srDto.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srDto.setStatus("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setSrUser("1");
    srDto.setCreatedUser("1");
    srDto.setAddNewFile(false);
    srDto.setServiceNims(false);
    srDto.setServiceId("1");
    srDto.setDvTrungKe(true);
    srDto.setGnocFileDtos(gnocFileDtoList);
    srDto.setUpdatedTime(new Date());
    srDto.setSrCode("1");
    srDto.setConcludedWithCr(true);
    srDto.setSrUnit(1L);
    srDto.setCountry("1");
    srDto.setAutoCreatCR(true);
    srDto.setSrUser("1");
    srDto.setUpdatedUser("1");
    // 25/05/2020
    srDto.setTitle("1");
    srDto.setDescription("1");
    srDto.setServiceArray("1");
    srDto.setServiceGroup("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setDataCheck(srDto);
    // 25/05/2020
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("New");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setAutoCreatCR(true);
    srInsiteDTO.setSrUser("1");
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);
    srCatalogDTO.setServiceCode("1");

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("AAM");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch);

    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    srCreateAutoCRDTO.setExecutionTime(new Date());
    srCreateAutoCRDTO.setExecutionEndTime(new Date());
//    srCreateAutoCRDTO.setUnitImplement(1L);
    srCreateAutoCRDTO.setServiceAffecting(1L);
    srCreateAutoCRDTO.setAffectingService("1,1");
//    srCreateAutoCRDTO.setWoTestTypeId(1L);
//    srCreateAutoCRDTO.setWoFtTypeId(1L);
//    srCreateAutoCRDTO.setCrTitle("1");
//    srCreateAutoCRDTO.setCrStatus(1L);
    srCreateAutoCRDTO.setPathFileProcess("FORM_1;1;1;1;1");
    srCreateAutoCRDTO.setFileTypeId("FORM_1;1;1;1;1");
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentId(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
//    srMappingProcessCRDTO.setIsCrNodes(1L);
//    srMappingProcessCRDTO.setUnitImplement(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
    List<SRMappingProcessCRDTO> listSRMapping = Mockito.spy(ArrayList.class);
    listSRMapping.add(srMappingProcessCRDTO);

    Object[] header = new Object[]{"1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(header);

    ResultInSideDto crNumber = Mockito.spy(ResultInSideDto.class);
    crNumber.setIdValue("1_2");
    crNumber.setKey("SUCCESS");

    CrProcessInsideDTO crProcessInsideDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessInsideDTO.setCrTypeId(2L);
    List<CrProcessInsideDTO> listCrProcess = Mockito.spy(ArrayList.class);
    listCrProcess.add(crProcessInsideDTO);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    SrInsiteDTO location = Mockito.spy(SrInsiteDTO.class);
    location.setCountry("1/1/1/1");

    CatLocationDTO locationDTO = Mockito.spy(CatLocationDTO.class);
    locationDTO.setLocationCode("1");

    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srMopDTO.setDtId("1");
    srMopDTO.setIpNode("1");
    List<SRMopDTO> listMOP = Mockito.spy(ArrayList.class);
    listMOP.add(srMopDTO);
//    srDto.setLstMopTmp(listMOP);

    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    List<TemplateImportDTO> lstTemplateImport = Mockito.spy(ArrayList.class);
    lstTemplateImport.add(templateImportDTO);
    ObjectMapper objectMapper = Mockito.spy(ObjectMapper.class);
    PowerMockito.when(objectMapper.writeValueAsString(any())).thenReturn("temp");
    PowerMockito.when(crServiceProxy.insertListNoIDForImportForSR(any())).thenReturn(lstTemplateImport);
    PowerMockito.when(srMopRepository.getListSRMopDTO(any())).thenReturn(listMOP);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(locationDTO);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(location);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(FileUtils.getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString())).thenReturn(fileContent);
    PowerMockito.when(crCategoryServiceProxy.getListCrProcessDTO(any())).thenReturn(listCrProcess);
    PowerMockito.when(crServiceProxy.getCrNumber(any())).thenReturn(crNumber);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File("/temp"), 0, 1, 0, 2, 1000)).thenReturn(dataImportList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(srCategoryServiceProxy.getListSRMappingProcessCRDTO(any())).thenReturn(listSRMapping);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(false, srDto);
    assertEquals(result.getKey(), "FAIL");
  }


  @Test
  public void validateSRDTO_14() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLocale()).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getString(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    com.viettel.nims.infra.webservice.sr.DistributeIpResourceOutput outputNims = Mockito.spy(DistributeIpResourceOutput.class);
    outputNims.setResult("NOK");
    ReflectionTestUtils.setField(srBusiness, "outputNims",
        outputNims);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileType("FORM");
    gnocFileDto.setRequired(1L);
    gnocFileDto.setFileName("1");
    gnocFileDto.setTemplateId(1L);

    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setPath("1");
    gnocFileDto1.setFileType("FORM");
    gnocFileDto1.setRequired(2L);
    gnocFileDto1.setFileName("1");
    gnocFileDto1.setTemplateId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
//    gnocFileDtoList.add(gnocFileDto1);

    SrInsiteDTO srDto = Mockito.spy(SrInsiteDTO.class);
    srDto.setRoleCode("1");
    srDto.setSrId(1L);
    srDto.setRoleCode("1");
    srDto.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srDto.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srDto.setStatus("https://www.youtube.com/");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setSrUser("1");
    srDto.setCreatedUser("1");
    srDto.setAddNewFile(false);
    srDto.setServiceNims(true);
    srDto.setServiceId("1");
    srDto.setDvTrungKe(true);
    srDto.setGnocFileDtos(gnocFileDtoList);
    srDto.setUpdatedTime(new Date());
    srDto.setSrCode("1");
    srDto.setConcludedWithCr(true);
    srDto.setSrUnit(1L);
    srDto.setCountry("1");
    srDto.setAutoCreatCR(true);
    srDto.setSrUser("1");
    srDto.setUpdatedUser("1");
    // 25/05/2020
    srDto.setTitle("1");
    srDto.setDescription("1");
    srDto.setServiceArray("1");
    srDto.setServiceGroup("1");
    srDto.setEvaluate("1");
    srDto.setReviewId(1L);
    srDto.setDataCheck(srDto);
    // 25/05/2020
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDate("20/05/2021 03:03:03"));
    srInsiteDTO.setEndTime(DateTimeUtils.convertStringToDate("20/08/2021 03:03:03"));
    srInsiteDTO.setStatus("https://www.youtube.com/");
    srInsiteDTO.setEvaluate("1");
    srInsiteDTO.setReviewId(1L);
    srInsiteDTO.setSrUser("1");
    srInsiteDTO.setCreatedUser("1");
    srInsiteDTO.setAddNewFile(false);
    srInsiteDTO.setServiceNims(false);
    srInsiteDTO.setServiceId("1");
    srInsiteDTO.setDvTrungKe(true);
//  srInsiteDTO.setSrWorkLogDTOList(srWorkLogDTOList);
    srInsiteDTO.setGnocFileDtos(gnocFileDtoList);
//  srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setConcludedWithCr(true);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setAutoCreatCR(true);
    srInsiteDTO.setSrUser("1");
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    List<SRHisDTO> srHisDTOList = Mockito.spy(ArrayList.class);
    srHisDTOList.add(srHisDTO);

    SRWorkLogDTO srWorkLogDTO = Mockito.spy(SRWorkLogDTO.class);
    List<SRWorkLogDTO> srWorkLogDTOList = Mockito.spy(ArrayList.class);
    srWorkLogDTOList.add(srWorkLogDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setFlowExecute(1L);
    srCatalogDTO.setServiceCode("1");

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("AAM");
    srConfigDTO.setConfigCode("https://www.youtube.com/");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    crInsiteDTO.setState("1");
    List<CrInsiteDTO> crInsiteDTOList = Mockito.spy(ArrayList.class);
    crInsiteDTOList.add(crInsiteDTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    woDTOSearch.setWoCode("1");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch);

    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    srCreateAutoCRDTO.setExecutionTime(new Date());
    srCreateAutoCRDTO.setExecutionEndTime(new Date());
//    srCreateAutoCRDTO.setUnitImplement(1L);
    srCreateAutoCRDTO.setServiceAffecting(1L);
    srCreateAutoCRDTO.setAffectingService("1,1");
//    srCreateAutoCRDTO.setWoTestTypeId(1L);
//    srCreateAutoCRDTO.setWoFtTypeId(1L);
//    srCreateAutoCRDTO.setCrTitle("1");
//    srCreateAutoCRDTO.setCrStatus(1L);
    srCreateAutoCRDTO.setPathFileProcess("FORM_1;1;1;1;1");
    srCreateAutoCRDTO.setFileTypeId("FORM_1;1;1;1;1");
    List<SRCreateAutoCRDTO> lstSrCreatAuto = Mockito.spy(ArrayList.class);
    lstSrCreatAuto.add(srCreateAutoCRDTO);

    UsersInsideDto userExecute = Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(1L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentId(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
//    srMappingProcessCRDTO.setIsCrNodes(1L);
//    srMappingProcessCRDTO.setUnitImplement(1L);
    srMappingProcessCRDTO.setCrProcessId(1L);
    List<SRMappingProcessCRDTO> listSRMapping = Mockito.spy(ArrayList.class);
    listSRMapping.add(srMappingProcessCRDTO);

    Object[] header = new Object[]{"1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(header);

    Object[] header1 = new Object[]{"1", "1", "1", "1","1"};
    List<Object[]> dataImportList1 = Mockito.spy(ArrayList.class);
    dataImportList1.add(header1);

    ResultInSideDto crNumber = Mockito.spy(ResultInSideDto.class);
    crNumber.setIdValue("1_2");
    crNumber.setKey("SUCCESS");

    CrProcessInsideDTO crProcessInsideDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessInsideDTO.setCrTypeId(2L);
    List<CrProcessInsideDTO> listCrProcess = Mockito.spy(ArrayList.class);
    listCrProcess.add(crProcessInsideDTO);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    SrInsiteDTO location = Mockito.spy(SrInsiteDTO.class);
    location.setCountry("1/1/1/1");

    CatLocationDTO locationDTO = Mockito.spy(CatLocationDTO.class);
    locationDTO.setLocationCode("1");

    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srMopDTO.setDtId("1");
    srMopDTO.setIpNode("1");
    List<SRMopDTO> listMOP = Mockito.spy(ArrayList.class);
    listMOP.add(srMopDTO);
//    srDto.setLstMopTmp(listMOP);

    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    List<TemplateImportDTO> lstTemplateImport = Mockito.spy(ArrayList.class);
    lstTemplateImport.add(templateImportDTO);
    ObjectMapper objectMapper = Mockito.spy(ObjectMapper.class);
    PowerMockito.when(ExcelWriterUtils.readExcelAddBlankRowXSSF(new File("/temp"), 0, 1, 0, 4, 1000)).thenReturn(dataImportList1);
    PowerMockito.when(objectMapper.writeValueAsString(any())).thenReturn("temp");
    PowerMockito.when(crServiceProxy.insertListNoIDForImportForSR(any())).thenReturn(lstTemplateImport);
    PowerMockito.when(srMopRepository.getListSRMopDTO(any())).thenReturn(listMOP);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(locationDTO);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(location);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(FileUtils.getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString())).thenReturn(fileContent);
    PowerMockito.when(crCategoryServiceProxy.getListCrProcessDTO(any())).thenReturn(listCrProcess);
    PowerMockito.when(crServiceProxy.getCrNumber(any())).thenReturn(crNumber);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File("/temp"), 0, 1, 0, 2, 1000)).thenReturn(dataImportList);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(srCategoryServiceProxy.getListSRMappingProcessCRDTO(any())).thenReturn(listSRMapping);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(userExecute);
    PowerMockito.when(srCreateAutoCrRepository.searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSrCreatAuto);
    PowerMockito.when(srConfigRepository.getCrInforByParentGroup(anyString())).thenReturn(configDTOList);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystemOfSR(any())).thenReturn(crInsiteDTOList);
    PowerMockito.when(srConfigRepository.getDataByConfigCode(any())).thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository2.findById(anyLong())).thenReturn(srCatalogDTO);
    PowerMockito.when(srWorkLogRepository.getListSRWorklogWithUnit(anyLong())).thenReturn(srWorkLogDTOList);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(srHisDTOList);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtoList);
    PowerMockito.when(srRepository.getDetail(anyLong(), anyString())).thenReturn(srInsiteDTO);
    ResultInSideDto result = srBusiness.validateSRDTO(false, srDto);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void updateStatusSRForProcess_01() {
    SrInsiteDTO objBefore = Mockito.spy(SrInsiteDTO.class);
    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(objBefore);
    srBusiness.updateStatusSRForProcess("1,1,1,1", "1");
  }

  @Test
  public void getListSRApprove_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRApproveDTO approve = Mockito.spy(SRApproveDTO.class);
    approve.setApproveUnitLevel1(1L);
    approve.setApproveDateLevel1(new Date());
    approve.setApproveUserLevel1(DateTimeUtils.convertDateToString(new Date()));
    approve.setApproveUnitLevel2(1L);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(1L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unit);
    PowerMockito.when(srApproveRepository.findSRApproveBySrId(anyLong())).thenReturn(approve);
    srBusiness.getListSRApprove(1L);
  }

  @Test
  public void getListSRRenewDTO_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setCreatedUser("1");
    SRRenewDTO renewDTO = Mockito.spy(SRRenewDTO.class);
    renewDTO.setStatusRenew(1L);
    List<SRRenewDTO> lstSRRenew = Mockito.spy(ArrayList.class);
    lstSRRenew.add(renewDTO);
    PowerMockito.when(srRepository.getListSRRenewDTO(any(), any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstSRRenew);
    PowerMockito.when(userRepository.getOffsetFromUser(anyString())).thenReturn(1.1);
    srBusiness.getListSRRenewDTO(srInsiteDTO);
  }

  @Test
  public void deleteMopFileWS_01() {
    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srMopDTO.setDtId("1");
    srMopDTO.setType("VMSA");
    srMopDTO.setSrId("1");
    srMopDTO.setTemplateId("1");
    List<SRMopDTO> lstMopTmp = Mockito.spy(ArrayList.class);
    lstMopTmp.add(srMopDTO);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setLstMopTmp(lstMopTmp);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    List<GnocFileDto> lstFile = Mockito.spy(ArrayList.class);
    lstFile.add(gnocFileDto);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("https://www.google.com");
    List<SRConfigDTO> lstLink = Mockito.spy(ArrayList.class);
    lstLink.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(any())).thenReturn(lstLink);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFile);
    PowerMockito.when(srMopRepository.getListSRMopDTO(any())).thenReturn(lstMopTmp);

    try{
      srBusiness.deleteMopFileWS(srInsiteDTO);
    }catch (Exception e){
      log.error(e.getMessage(), e);
    }
  }

  @Test
  public void deleteMopFileWS_02() {
    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srMopDTO.setDtId("1");
    srMopDTO.setType("AAM");
    srMopDTO.setSrId("1");
    srMopDTO.setTemplateId("1");
    List<SRMopDTO> lstMopTmp = Mockito.spy(ArrayList.class);
    lstMopTmp.add(srMopDTO);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setLstMopTmp(lstMopTmp);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    List<GnocFileDto> lstFile = Mockito.spy(ArrayList.class);
    lstFile.add(gnocFileDto);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("https://www.google.com");
    List<SRConfigDTO> lstLink = Mockito.spy(ArrayList.class);
    lstLink.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(any())).thenReturn(lstLink);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFile);
    PowerMockito.when(srMopRepository.getListSRMopDTO(any())).thenReturn(lstMopTmp);
    try{
      srBusiness.deleteMopFileWS(srInsiteDTO);
    }catch (Exception e){
      log.error(e.getMessage(), e);
    }

  }

  @Test
  public void deleteMopFileWS_03() {
    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srMopDTO.setDtId("1");
    srMopDTO.setType("VIPA");
    srMopDTO.setSrId("1");
    srMopDTO.setTemplateId("1");
    PowerMockito.mockStatic(WSForGNOC.class);
    List<SRMopDTO> lstMopTmp = Mockito.spy(ArrayList.class);
    lstMopTmp.add(srMopDTO);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setLstMopTmp(lstMopTmp);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    List<GnocFileDto> lstFile = Mockito.spy(ArrayList.class);
    lstFile.add(gnocFileDto);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("http://10.240.202.12:8576/IPChangeProcess/WSForGNOC?wsdl");
    List<SRConfigDTO> lstLink = Mockito.spy(ArrayList.class);
    lstLink.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(any())).thenReturn(lstLink);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFile);
    PowerMockito.when(srMopRepository.getListSRMopDTO(any())).thenReturn(lstMopTmp);
    try {
      srBusiness.deleteMopFileWS(srInsiteDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
  @Test
  public void getListSRForWO() {
  }

  @Test
  public void updateSRDTO_01() throws Exception {
    mockUserToken();
    mockI18n();
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    resultInSideDto.setIdValue("New");

    SrInsiteDTO srInsiteDTO =Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setStatus("Planned");
    srInsiteDTO.setOpenConnect(true);
    srInsiteDTO.setSrId(123L);
    SrBusinessImpl srBusinessInline = Mockito.spy(srBusiness);

    GnocFileDto gnocFileSearch = Mockito.spy(GnocFileDto.class);
    gnocFileSearch.setBusinessCode("SR");
    gnocFileSearch.setBusinessId(123L);
    gnocFileSearch.setComments("123");
    gnocFileSearch.setContent("123");
    gnocFileSearch.setMappingId(123L);
    List<GnocFileDto> lstGNOCFileDtos =Mockito.spy(ArrayList.class);
    lstGNOCFileDtos.add(gnocFileSearch);

    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(lstGNOCFileDtos);
    PowerMockito.when(gnocFileRepository.updateGnocFile(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srRepository.deleteSRFile(any())).thenReturn(resultInSideDto);
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).updateSR(any());
    ResultInSideDto result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());


    SRApproveDTO srApproveDTO = Mockito.spy(SRApproveDTO.class);
    srApproveDTO.setApproveLevel1(0L);
    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setStatus("Under_Approval");
    srInsiteDTO.setServiceId("123");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setApprove(1L);
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());

    srApproveDTO.setApproveLevel1(1L);
    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setStatus("Under_Approval");
    srInsiteDTO.setServiceId("123");
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());

    srCatalogDTO.setApprove(2L);
    srApproveDTO.setApproveUnitLevel2(null);
    srApproveDTO.setApproveLevel1(1L);
    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setStatus("Under_Approval");
    srInsiteDTO.setServiceId("123");
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());

    srCatalogDTO.setApprove(2L);
    srApproveDTO.setApproveUnitLevel2(null);
    srApproveDTO.setApproveLevel1(0L);
    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setStatus("Under_Approval");
    srInsiteDTO.setServiceId("123");
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());

    srCatalogDTO.setApprove(2L);
    srApproveDTO.setApproveUnitLevel2(1L);
    srApproveDTO.setApproveLevel1(1L);
    srApproveDTO.setApproveLevel2(1L);
    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setStatus("Under_Approval");
    srInsiteDTO.setServiceId("123");
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());

    srCatalogDTO.setApprove(2L);
    srApproveDTO.setApproveUnitLevel2(1L);
    srApproveDTO.setApproveLevel1(1L);
    srApproveDTO.setApproveLevel2(0L);
    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setStatus("Under_Approval");
    srInsiteDTO.setServiceId("123");
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());

    srCatalogDTO.setApprove(2L);
    srApproveDTO.setApproveUnitLevel2(1L);
    srApproveDTO.setApproveLevel1(0L);
    srApproveDTO.setApproveLevel2(0L);
    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setStatus("Under_Approval");
    srInsiteDTO.setServiceId("123");
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());


    srCatalogDTO.setApprove(2L);
    srApproveDTO.setApproveUnitLevel2(1L);
    srApproveDTO.setApproveLevel1(0L);
    srApproveDTO.setApproveLevel2(0L);
    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setStatus("Under_Approval");
    srInsiteDTO.setServiceId("123");
    resultInSideDto.setIdValue("Draft");
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());


    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setStatus("Rejected");
    srInsiteDTO.setServiceId("123");
    resultInSideDto.setIdValue("New");
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());

    srInsiteDTO.setApproveDTO(srApproveDTO);
    srInsiteDTO.setStatus("Closed");
    srInsiteDTO.setServiceId("123");
    srInsiteDTO.setEvaluate("NOK");
    srInsiteDTO.setCountry("281");
    srInsiteDTO.setFlowExecuteId(123L);
    srInsiteDTO.setCheckStatusClosedNOK(true);
    srInsiteDTO.setEvaluateReason("123");
    srInsiteDTO.setSrId(123L);
    SRRoleActionDTO dtoActionUpdate = Mockito.spy(SRRoleActionDTO.class);
    dtoActionUpdate.setCountry("281");
    resultInSideDto.setIdValue("New");
    List<SRRoleActionDTO> lstDataUpdateSR = Mockito.spy(ArrayList.class);
    lstDataUpdateSR.add(dtoActionUpdate);
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleActionDTO(any())).thenReturn(lstDataUpdateSR);
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srInsiteDTO);
    PowerMockito.when(srEvaluateRepository.insertSREvaluate(any())).thenReturn(resultInSideDto);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());


    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("FAIL");
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleActionDTO(any())).thenReturn(lstDataUpdateSR);
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srInsiteDTO);
    PowerMockito.when(srEvaluateRepository.insertSREvaluate(any())).thenReturn(resultInSideDto1);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());


    resultInSideDto.setReturnCode("123_123");
    srInsiteDTO.setAutoCreatCR(true);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoId("123");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(crServiceProxy.deleteCR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(woServiceProxy.deleteWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleActionDTO(any())).thenReturn(lstDataUpdateSR);
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srInsiteDTO);
    PowerMockito.doReturn(resultInSideDto1).when(srBusinessInline).updateSR(any());
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto1.getKey());

    mockAutoCreateSRChild();
    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srMopDTO.setSrId("123");
    List<SRMopDTO> lstSrMopDTOS = Mockito.spy(ArrayList.class);
    lstSrMopDTOS.add(srMopDTO);
    srInsiteDTO.setLstMopTmp(lstSrMopDTOS);
    srInsiteDTO.setEndTime(new Date());
    srInsiteDTO.setStartTime(new Date());
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("Assigned_Planning");
    List<SRConfigDTO> lstConfigSRChild = Mockito.spy(ArrayList.class);
    lstConfigSRChild.add(srConfigDTO);
    resultInSideDto.setIdValue("New");
    srInsiteDTO.setStatus("Assigned_Planning");
    srCatalogDTO.setServiceId(123L);
    srCatalogDTO.setServiceCode("123");
    srCatalogDTO.setRoleCode("123");
    srCatalogDTO.setAutoCreateSR("1");
    srCatalogDTO.setServiceDescription("123");
    srCatalogDTO.setExecutionUnit("123");
    srCatalogDTO.setCountry("281");
    srCatalogDTO.setServiceArray("123");
    srCatalogDTO.setServiceGroup("123");
    srCatalogDTO.setExecutionTime("1");
    List<SRCatalogDTO> lstSRCatalog = Mockito.spy(ArrayList.class);
    lstSRCatalog.add(srCatalogDTO);
    List<String> lsSequense = Mockito.spy(ArrayList.class);
    lsSequense.add("123");
    srInsiteDTO.setSrCode("SR_VN_123");
    List<SrInsiteDTO> lstSrChild = Mockito.spy(ArrayList.class);
    SrInsiteDTO srChild = Mockito.spy(SrInsiteDTO.class);
    srChild.setSrCode("SR_VN_123");
    srChild.setStatus("Draft");
    lstSrChild.add(srChild);
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).validateSRDTO(anyBoolean(),any());
    PowerMockito.when(crServiceProxy.deleteCR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    PowerMockito.when(woServiceProxy.deleteWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleActionDTO(any())).thenReturn(lstDataUpdateSR);
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srInsiteDTO);
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).updateSR(any());
    PowerMockito.when(srMopRepository.insertListSRMop(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srConfigRepository.getByConfigGroup(any())).thenReturn(lstConfigSRChild);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstSRCatalog);
    PowerMockito.when(srRepository.getListSequenseSR(any(),anyInt())).thenReturn(lsSequense);
    PowerMockito.doReturn(srInsiteDTO).when(srBusinessInline).getChangeExecutionTimeAndFlowExecute(anyString(),anyString(),anyString());
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).insertSR(any());
    PowerMockito.when(srChildAutoRepository.insertOrUpdateSRChildAuto(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srRepository.findByParenCode(any())).thenReturn(lstSrChild);
    PowerMockito.when(srRepository.getDetail(any(),anyString())).thenReturn(srChild);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());

    srInsiteDTO.setStatus("Concluded");
    mockAutoCreateSRChildByGennerateNo();
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setObjectId(123L);
    List<SRCreatedFromOtherSysDTO> lstWo1 = Mockito.spy(ArrayList.class);
    lstWo1.add(srCreatedFromOtherSysDTO);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey("SUCCESS");
    srApproveDTO.setApproveId(123L);
    srApproveDTO.setApproveLevel1(1L);
    srApproveDTO.setApproveUnitLevel2(1L);
    srApproveDTO.setApproveLevel2(null);
    srInsiteDTO.setApproveDTO(srApproveDTO);
    List<String> lstTP = Mockito.spy(ArrayList.class);
    lstTP.add("TP");
    mockSendMessages();
    srInsiteDTO.setConcludedWithCr(true);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrId("123");
    List<CrInsiteDTO> lstCr = Mockito.spy(ArrayList.class);
    lstCr.add(crInsiteDTO);
    srInsiteDTO.setServiceNims(true);
    mockFileUtilsSaveFile();
    GnocFileDto itemFileAttach = Mockito.spy(GnocFileDto.class);
    itemFileAttach.setBusinessCode("SR");
    itemFileAttach.setBusinessId(123L);
    itemFileAttach.setComments("123");
    itemFileAttach.setContent("123");
    itemFileAttach.setMappingId(123L);
    itemFileAttach.setPath("123");
    List<GnocFileDto> lstFileAttach =Mockito.spy(ArrayList.class);
    lstFileAttach.add(itemFileAttach);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFileAttach);
    PowerMockito.doReturn(lstCr).when(srBusinessInline).findListCrBySr(any());
    PowerMockito.when(srCreatedFromOtherSysRepository.getListSRCreatedFromOtherSysDTO(any())).thenReturn(lstWo1);
    PowerMockito.when(srRepository.updateStepIdCr(any(),anyString(),anyLong())).thenReturn(resultInSideDto);
    PowerMockito.when(woServiceProxy.updateCfgWoTickHelpVsmart(any())).thenReturn(resultDTO);
    PowerMockito.when(srApproveRepository.updateSRApprove(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srRepository.getLeaderApprove(anyString(),anyString())).thenReturn(lstTP);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());

    srInsiteDTO.setApproveDTO(null);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(999999L);
    usersInsideDto.setUnitId(123L);
    usersInsideDto.setUsername("123");
    PowerMockito.when(srRepository.getLeaderApprove(anyString(),anyString())).thenReturn(lstTP);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(usersInsideDto);
    result = srBusinessInline.updateSRDTO(srInsiteDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  public void mockFileUtilsSaveFile() throws Exception{
    byte []bytes = new byte[] {1,2,3};
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(PassTranformer.class);
    ExcelWriterUtils excelWriterUtils = Mockito.spy(ExcelWriterUtils.class);
    Workbook workBook = Mockito.spy(Workbook.class);
    Sheet sheet = Mockito.spy(Sheet.class);
    CellStyle cellSt1 =Mockito.spy(CellStyle.class);
    PowerMockito.when(PassTranformer.decrypt(anyString())).thenReturn("decrypt");
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(), any(), any())).thenReturn("/path/junit");
    PowerMockito.when(FileUtils.saveUploadFile(anyString(), any(), anyString(), any())).thenReturn("/path/upload");
    PowerMockito.when(FileUtils.getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString())).thenReturn(bytes);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/path/temp");
    PowerMockito.when(FileUtils.getFileName(any())).thenReturn("chanquama");
//    PowerMockito.when(workBook.getSheetAt(any())).thenReturn(sheet);
    PowerMockito.when(excelWriterUtils.readFileExcel(any())).thenReturn(workBook);
  }

  @Test
  public void updateCrNumberForSR_1() throws Exception {

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setCrNumber("CR_01");
    List<SrInsiteDTO> lstResult = Mockito.spy(ArrayList.class);
    lstResult.add(srInsiteDTO);
    PowerMockito.when(srRepository.getCrNumberCreatedFromSR(any(),anyInt(),anyInt(),anyBoolean())).thenReturn(lstResult);
    PowerMockito.when(srRepository.updateCrNumberForSR(anyString(),anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto actual = srBusiness.updateCrNumberForSR("CR_02",123L);
    Assert.assertEquals(resultInSideDto.getKey(), actual.getKey());
  }

  @Test
  public void getListSRFileCheckRole_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SrBusinessImpl srBusinessInline = Mockito.spy(srBusiness);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrCode("SR_01");
    srInsiteDTO.setStatus("Closed");
    srInsiteDTO.setUpdatedUser("thanhlv12");
    srInsiteDTO.setSrId(123L);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSrUnit(123L);
    srInsiteDTO.setRoleCode("ABC");
    srInsiteDTO.setCreatedUser("thanhlv12");
    srInsiteDTO.setSrUser("thanhlv12");

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setBusinessId(123L);
    List<GnocFileDto> lstGnoc = Mockito.spy(ArrayList.class);

    CfgRoleDataDTO objectSearch = Mockito.spy(CfgRoleDataDTO.class);
    objectSearch.setUsername("thanhlv12");
    objectSearch.setSystem(6L);
    objectSearch.setStatus(1L);
    objectSearch.setRole(1L);
    objectSearch.setAuditUnitId("123,456");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(123L);
    List<UnitDTO> lstUnit = Mockito.spy(ArrayList.class);
    lstUnit.add(unitDTO);
    mockUserToken();

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(999999L);

    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(srInsiteDTO);
    PowerMockito.doReturn(lstGnoc).when(srBusinessInline).getListSRFile(any());
    ResultInSideDto actual = srBusiness.getListSRFileCheckRole(gnocFileDto);
    Assert.assertEquals(resultInSideDto.getKey(), actual.getKey());

    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(srInsiteDTO);
    PowerMockito.when(cfgRoleDataRepository.getConfigByDto(any())).thenReturn(objectSearch);
    PowerMockito.doReturn(lstGnoc).when(srBusinessInline).getListSRFile(any());
     actual = srBusiness.getListSRFileCheckRole(gnocFileDto);
    Assert.assertEquals(resultInSideDto.getKey(), actual.getKey());

    objectSearch.setRole(3L);
    PowerMockito.when(cfgRoleDataRepository.getConfigByDto(any())).thenReturn(objectSearch);
    PowerMockito.doReturn(lstGnoc).when(srBusinessInline).getListSRFile(any());
    PowerMockito.when(unitRepository.getListUnitChildren(anyLong())).thenReturn(lstUnit);
    PowerMockito.when(unitRepository.getListUnitChildren(anyLong())).thenReturn(lstUnit);
    actual = srBusiness.getListSRFileCheckRole(gnocFileDto);
    Assert.assertEquals(resultInSideDto.getKey(), actual.getKey());
  }

  @Test
  public void closedSR_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrCode("SR_01");
    srInsiteDTO.setStatus("Closed");
    srInsiteDTO.setUpdatedUser("thanhlv12");
    srInsiteDTO.setSrId(123L);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setCreatedUser("thanhlv12");
    List<SrInsiteDTO> lstResult = Mockito.spy(ArrayList.class);
    lstResult.add(srInsiteDTO);
    SRActionCodeDTO searchActionCode =  Mockito.spy(SRActionCodeDTO.class);
    searchActionCode.setActionCode("U");
    searchActionCode.setDefaultComment("123");
    List<SRActionCodeDTO> lsActionCode = Mockito.spy(ArrayList.class);
    lsActionCode.add(searchActionCode);
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("123");
    mockUserToken();

    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(srInsiteDTO);
    PowerMockito.when(srRepository.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srRepository.searchSrActionCode(any(),anyInt(),anyInt(),anyString(),anyString())).thenReturn(lsActionCode);
    PowerMockito.when(srHisRepository.createSRHis(any())).thenReturn(resultInSideDto);
    ResultInSideDto actual = srBusiness.closedSR(123L);
    Assert.assertEquals(resultInSideDto.getKey(), actual.getKey());
  }

  public void mockAutoCreateSR() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SrBusinessImpl srBusinessInline = Mockito.spy(srBusiness);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    UnitSRCatalogDTO unitSRCatalogDTO = Mockito.spy(UnitSRCatalogDTO.class);
    List<UnitSRCatalogDTO> lstUnitInService = Mockito.spy(ArrayList.class);
    unitSRCatalogDTO.setUnitId("123");
    unitSRCatalogDTO.setAutoCreateSR("1");
    unitSRCatalogDTO.setServiceId("123");
    lstUnitInService.add(unitSRCatalogDTO);
    lstUnitInService.add(unitSRCatalogDTO);
    List<String> lsSequense = Mockito.spy(ArrayList.class);
    lsSequense.add("5555");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("VN");

    PowerMockito.when(srCatalogRepository2.getListUnitSRCatalog(any())).thenReturn(lstUnitInService);
    PowerMockito.when(srRepository.getListSequenseSR(anyString(),anyInt())).thenReturn(lsSequense);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(catLocationDTO);
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).insertSR(any());

  }

  public void mockAutoCreateSRChildByGennerateNo() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SrBusinessImpl srBusinessInline = Mockito.spy(srBusiness);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    SRChildAutoDTO srChildAutoDTO = Mockito.spy(SRChildAutoDTO.class);
    srChildAutoDTO.setStatus("Closed");
    srChildAutoDTO.setSrParentCode("SR_P_01");
    srChildAutoDTO.setGenerateNo(1L);
    srChildAutoDTO.setId(123L);
    List<SRChildAutoDTO> lstCheckSRChildClose = Mockito.spy(ArrayList.class);
    lstCheckSRChildClose.add(srChildAutoDTO);

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setServiceId("123");
    srInsiteDTO.setCountry("281");
    srInsiteDTO.setStartTime(new Date());
    srInsiteDTO.setEndTime(new Date());
    srInsiteDTO.setChildAutoId(123L);
    List<SrInsiteDTO> lstSRChildAuto =Mockito.spy(ArrayList.class);
    lstSRChildAuto.add(srInsiteDTO);

    List<String> lsSequense = Mockito.spy(ArrayList.class);
    lsSequense.add("5555");


    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("VN");

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setExecutionTime("1");
    srCatalogDTO.setIsAddDay(1L);
    srCatalogDTO.setFlowExecuteName("1");
    List<SRCatalogDTO> lsUnitInService = Mockito.spy(ArrayList.class);
    lsUnitInService.add(srCatalogDTO);


    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(catLocationDTO);
    PowerMockito.when(srChildAutoRepository.getListSRChildCheckClosed(anyString())).thenReturn(lstCheckSRChildClose);
    PowerMockito.when(srChildAutoRepository.getListSRChildAutoByGennerateNo(anyString(),anyLong())).thenReturn(lstSRChildAuto);
    PowerMockito.when(srRepository.getListSequenseSR(anyString(),anyInt())).thenReturn(lsSequense);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lsUnitInService);
    PowerMockito.doReturn(srInsiteDTO).when(srBusinessInline).getChangeExecutionTimeAndFlowExecute(any(),anyString(),anyString());
    PowerMockito.doReturn(resultInSideDto).when(srBusinessInline).insertSR(any());
    PowerMockito.when(srChildAutoRepository.getDetailSRChildAuto(anyLong())).thenReturn(srChildAutoDTO);
    PowerMockito.when(srChildAutoRepository.insertOrUpdateSRChildAuto(any())).thenReturn(resultInSideDto);
  }

  public void mockUserToken() {
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999l);
    userToken.setDeptId(413314l);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
  }

  public void mockI18n() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("some text...");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("some text...");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("some text...");
  }

  public void mockAutoCreateSRChild() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRCatalogChildDTO dtoChild = Mockito.spy(SRCatalogChildDTO.class);
    dtoChild.setServiceIdChild(123L);
    dtoChild.setGenerateNo(1L);
    dtoChild.setAutoCreateSR(1L);
    List<SRCatalogChildDTO> lstChildAll = Mockito.spy(ArrayList.class);
    lstChildAll.add(dtoChild);
    PowerMockito.when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstChildAll);
    PowerMockito.when(srCatalogChildRepository.checkGenerateNo(any())).thenReturn("-1");

  }

  public void mockSendMessages() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    List<SRConfigDTO> lsSms = Mockito.spy(ArrayList.class);

    LanguageExchangeDTO languageExchangeDTO = Mockito.spy(LanguageExchangeDTO.class);
    languageExchangeDTO.setBussinessId(123L);
    List<LanguageExchangeDTO> lstLanguage = Mockito.spy(ArrayList.class);
    lstLanguage.add(languageExchangeDTO);
    UsersInsideDto userExecute=  Mockito.spy(UsersInsideDto.class);
    userExecute.setUnitId(123L);

    UnitDTO unitExecute=  Mockito.spy(UnitDTO.class);
    userExecute.setUnitId(123L);
    unitExecute.setSmsGatewayId(1L);
    lsSms.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getSmsContent(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(lsSms);
    PowerMockito.when(languageExchangeRepository.findBySql(anyString(),any(),any(),any())).thenReturn(lstLanguage);
    PowerMockito.when(userRepository.getUserDTOByUserName(any())).thenReturn(userExecute);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitExecute);
    PowerMockito.when(messagesRepository.insertOrUpdateListMessagesCommon(any())).thenReturn(resultInSideDto);
  }

}
