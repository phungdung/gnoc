package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.aam.MopFileResult;
import com.viettel.aam.MopInfo;
import com.viettel.aam.MopResult;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CfgRoleDataDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.incident.provider.WSSPMProvidePort;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.CfgRoleDataRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.CR_FILE_TYPE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.AttachDtDTO;
import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
import com.viettel.gnoc.cr.dto.CrFileObjectInsite;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachResultDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.TemplateImportDTO;
import com.viettel.gnoc.cr.repository.CrDtRepositoryImpl;
import com.viettel.gnoc.cr.repository.CrFilesAttachRepository;
import com.viettel.gnoc.cr.repository.CrGeneralRepository;
import com.viettel.gnoc.cr.repository.CrProcessRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.gnoc.cr.repository.TemplateImportRepository;
import com.viettel.gnoc.cr.util.CrGeneralUtil;
import com.viettel.gnoc.cr.util.CrProcessFromClient;
import com.viettel.gnoc.ws.provider.WSTDTTPort;
import com.viettel.gnoc.ws.provider.WSVipaDdPort;
import com.viettel.gnoc.ws.provider.WSVipaIpPort;
import com.viettel.security.PassTranformer;
import com.viettel.service.PhoneNumberForm;
import com.viettel.service.ValidateTraceForm;
import com.viettel.vipa.MopDetailDTO;
import com.viettel.vipa.MopDetailOutputDTO;
import com.viettel.vmsa.MopOutputDTO;
import com.viettel.vmsa.ResultFileValidateDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;
import org.apache.xml.security.utils.Base64;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrFileAttachBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    StringUtils.class, DateUtil.class, TicketProvider.class, DataUtil.class, CommonExport.class,
    CrGeneralUtil.class, DateTimeUtils.class, PassTranformer.class, Base64.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrFileAttachBusinessImplTest {

  @InjectMocks
  CrFileAttachBusinessImpl crFileAttachBusiness;

  @Mock
  CrProcessRepository crProcessRepository;

  @Mock
  CrFilesAttachRepository crFilesAttachRepository;

  @Mock
  CrDtRepositoryImpl crDtRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitRepository unitRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  LanguageExchangeRepository languageExchangeRepository;

  @Mock
  TemplateImportRepository templateImportRepository;

  @Mock
  WSVipaDdPort vipaDdPort;

  @Mock
  WSSPMProvidePort wsspmProvidePort;

  @Mock
  WSTDTTPort wstdttPort;

  @Mock
  WSVipaIpPort wsVipaIpPort;

  @Mock
  CrProcessFromClient crProcessFromClient;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  CrRepository crRepository;

  @Mock
  CfgRoleDataRepository cfgRoleDataRepository;

  @Mock
  CrGeneralRepository crGeneralRepository;

  @Before
  public void setUpUploadFolder() {
//    ReflectionTestUtils.setField(crFileAttachBusiness, "userName",
//        "thanhlv12");
//    ReflectionTestUtils.setField(crFileAttachBusiness, "password",
//        "123456a@");
//    ReflectionTestUtils.setField(crFileAttachBusiness, "saltConf",
//        "Gman");
    ReflectionTestUtils.setField(crFileAttachBusiness, "uploadFolder",
        "/uploadFolder");
    ReflectionTestUtils.setField(crFileAttachBusiness, "ftpFolder",
        "/ftpFolder");
    ReflectionTestUtils.setField(crFileAttachBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(crFileAttachBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(crFileAttachBusiness, "ftpServer",
        "10.240.232.25");
    ReflectionTestUtils.setField(crFileAttachBusiness, "ftpPort",
        21);
  }

  @Test
  public void testGetListCrFilesSearch() {
    CrFilesAttachInsiteDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);

    List<CrFilesAttachInsiteDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(crFilesAttachDTO);

    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesSearch(any())
    ).thenReturn(lst);

    List<CrFilesAttachInsiteDTO> actual = crFileAttachBusiness
        .getListCrFilesSearch(crFilesAttachDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void testGetListFilesSearchDataTable() {
    CrFilesAttachInsiteDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    Datatable expected = Mockito.spy(Datatable.class);

    PowerMockito.when(
        crFilesAttachRepository.getListFilesSearchDataTable(any())
    ).thenReturn(expected);

    Datatable actual = crFileAttachBusiness
        .getListFilesSearchDataTable(crFilesAttachDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testInsertList_01() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> multipartFileList = Mockito.spy(ArrayList.class);
    multipartFileList.add(testFile);

    CrFileObjectInsite crFileObjectInsite = Mockito.spy(CrFileObjectInsite.class);
    crFileObjectInsite.setLinkTemplate("/unit/test/tripm/filename.xlsx");
    crFileObjectInsite.setCrProcessId("1111");
    crFileObjectInsite.setIdTemplate("1500");
    crFileObjectInsite.setIndexFile(0);

    List<CrFileObjectInsite> crFileObjects = Mockito.spy(ArrayList.class);
    crFileObjects.add(crFileObjectInsite);

    CrFilesAttachInsiteDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachDTO.setCrFileObjects(crFileObjects);
    crFilesAttachDTO.setStateCr("6");
    crFilesAttachDTO.setCrId(5L);
    crFilesAttachDTO.setFilePath("/temp");
    crFilesAttachDTO.setFilePathFtp("/temp");
    crFilesAttachDTO.setTempImportId(5L);
    crFilesAttachDTO.setFileName("filename");
    crFilesAttachDTO.setFileType("5555");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("username");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);

    CrProcessInsideDTO processDTO = Mockito.spy(CrProcessInsideDTO.class);
    processDTO.setIsVmsaActiveCellProcess(2L);

    PowerMockito.when(
        crProcessRepository
            .findCrProcessById(anyLong())
    ).thenReturn(processDTO);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
        "6cf8a37f3e6993f90eef71859b1ebf31", "/ftpFolder", "filename.xlsx", fileContent, new Date()))
        .thenReturn("tripm");

    List<CrFilesAttachInsiteDTO> lstUpdateInsert = Mockito.spy(ArrayList.class);
    lstUpdateInsert.add(crFilesAttachDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesToUpdateOrInsertForList(anyList(), anyBoolean(), anyBoolean())
    ).thenReturn(lstUpdateInsert);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(10L);
    usersInsideDto.setMobile("0909009009");
    PowerMockito.when(
        userRepository.getUserDTOByUserName(anyString())
    ).thenReturn(usersInsideDto);

    List<UsersInsideDto> lst = Mockito.spy(ArrayList.class);
    lst.add(usersInsideDto);
    PowerMockito.when(
        crFilesAttachRepository.actionGetUserByUserName(anyString())
    ).thenReturn(lst);

    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    templateImportDTO.setResult("SUCCESS");
    PowerMockito.when(
        templateImportRepository.validateTempImport(any(), any(), anyString(), anyString())
    ).thenReturn(templateImportDTO);

    ValidateTraceForm validateTraceForm = Mockito.spy(ValidateTraceForm.class);
    PowerMockito.when(wsspmProvidePort.validateUserPhoneNumber(anyList()))
        .thenReturn(validateTraceForm);
    ResultInSideDto actual = crFileAttachBusiness.insertList(
        crFilesAttachDTO,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList
    );

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testInsertList_02() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> multipartFileList = Mockito.spy(ArrayList.class);
    multipartFileList.add(testFile);

    CrFileObjectInsite crFileObjectInsite = Mockito.spy(CrFileObjectInsite.class);
    crFileObjectInsite.setLinkTemplate("/unit/test/tripm/filename.xlsx");
    crFileObjectInsite.setCrProcessId("1111");
    crFileObjectInsite.setIdTemplate("1500");
    crFileObjectInsite.setIndexFile(0);

    List<CrFileObjectInsite> crFileObjects = Mockito.spy(ArrayList.class);
    crFileObjects.add(crFileObjectInsite);

    CrFilesAttachInsiteDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachDTO.setCrFileObjects(crFileObjects);
    crFilesAttachDTO.setStateCr("6");
    crFilesAttachDTO.setCrId(5L);
    crFilesAttachDTO.setFilePath("/temp");
    crFilesAttachDTO.setFilePathFtp("/temp");
    crFilesAttachDTO.setTempImportId(5L);
    crFilesAttachDTO.setFileName("filename");
    crFilesAttachDTO.setFileType("5555");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("username");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);

    CrProcessInsideDTO processDTO = Mockito.spy(CrProcessInsideDTO.class);
    processDTO.setIsVmsaActiveCellProcess(1L);

    PowerMockito.when(
        crProcessRepository
            .findCrProcessById(anyLong())
    ).thenReturn(processDTO);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
        "6cf8a37f3e6993f90eef71859b1ebf31", "/ftpFolder", "filename.xlsx", fileContent, new Date()))
        .thenReturn("tripm");

    List<CrFilesAttachInsiteDTO> lstUpdateInsert = Mockito.spy(ArrayList.class);
    lstUpdateInsert.add(crFilesAttachDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesToUpdateOrInsertForList(anyList(), anyBoolean(), anyBoolean())
    ).thenReturn(lstUpdateInsert);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(10L);
    usersInsideDto.setMobile("0909009009");
    PowerMockito.when(
        userRepository.getUserDTOByUserName(anyString())
    ).thenReturn(usersInsideDto);

    List<UsersInsideDto> lst = Mockito.spy(ArrayList.class);
    lst.add(usersInsideDto);
    PowerMockito.when(
        crFilesAttachRepository.actionGetUserByUserName(anyString())
    ).thenReturn(lst);

    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    templateImportDTO.setResult("SUCCESS");
    PowerMockito.when(
        templateImportRepository.validateTempImport(any(), any(), anyString(), anyString())
    ).thenReturn(templateImportDTO);

    ValidateTraceForm validateTraceForm = Mockito.spy(ValidateTraceForm.class);
    PowerMockito.when(wsspmProvidePort.validateUserPhoneNumber(anyList()))
        .thenReturn(validateTraceForm);

    ResultInSideDto actual = crFileAttachBusiness.insertList(
        crFilesAttachDTO,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList
    );

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testInsertList_03() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("tripm");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> multipartFileList = Mockito.spy(ArrayList.class);
    multipartFileList.add(testFile);

    CrFileObjectInsite crFileObjectInsite = Mockito.spy(CrFileObjectInsite.class);
    crFileObjectInsite.setLinkTemplate("/unit/test/tripm/filename.xlsx");
    crFileObjectInsite.setCrProcessId("1111");
    crFileObjectInsite.setIdTemplate("1500");
    crFileObjectInsite.setIndexFile(0);

    List<CrFileObjectInsite> crFileObjects = Mockito.spy(ArrayList.class);
    crFileObjects.add(crFileObjectInsite);

    CrFilesAttachInsiteDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachDTO.setCrFileObjects(crFileObjects);
    crFilesAttachDTO.setStateCr("6");
    crFilesAttachDTO.setCrId(5L);
    crFilesAttachDTO.setFilePath("/temp");
    crFilesAttachDTO.setFilePathFtp("/temp");
    crFilesAttachDTO.setTempImportId(5L);
    crFilesAttachDTO.setFileName("filename");
    crFilesAttachDTO.setFileType("5555");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("username");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);

    CrProcessInsideDTO processDTO = Mockito.spy(CrProcessInsideDTO.class);
    processDTO.setIsVmsaActiveCellProcess(1L);
    processDTO.setVmsaActiveCellProcessKey("SUCCESS");

    PowerMockito.when(
        crProcessRepository
            .findCrProcessById(anyLong())
    ).thenReturn(processDTO);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
        "6cf8a37f3e6993f90eef71859b1ebf31", "/ftpFolder", "filename.xlsx", fileContent, new Date()))
        .thenReturn("tripm");

    PowerMockito.when(FileUtils.getFileName(any())).thenReturn("/temp");

    List<CrFilesAttachInsiteDTO> lstUpdateInsert = Mockito.spy(ArrayList.class);
    lstUpdateInsert.add(crFilesAttachDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesToUpdateOrInsertForList(anyList(), anyBoolean(), anyBoolean())
    ).thenReturn(lstUpdateInsert);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(10L);
    usersInsideDto.setMobile("0909009009");
    PowerMockito.when(
        userRepository.getUserDTOByUserName(anyString())
    ).thenReturn(usersInsideDto);

    List<UsersInsideDto> lst = Mockito.spy(ArrayList.class);
    lst.add(usersInsideDto);
    PowerMockito.when(
        crFilesAttachRepository.actionGetUserByUserName(anyString())
    ).thenReturn(lst);

    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    templateImportDTO.setResult("SUCCESS");
    PowerMockito.when(
        templateImportRepository.validateTempImport(any(), any(), anyString(), anyString())
    ).thenReturn(templateImportDTO);

    ValidateTraceForm validateTraceForm = Mockito.spy(ValidateTraceForm.class);
    PowerMockito.when(wsspmProvidePort.validateUserPhoneNumber(anyList()))
        .thenReturn(validateTraceForm);
    ResultInSideDto actual = crFileAttachBusiness.insertList(
        crFilesAttachDTO,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList
    );

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testInsertList_04() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("tripm");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> multipartFileList = Mockito.spy(ArrayList.class);
    multipartFileList.add(testFile);

    CrFileObjectInsite crFileObjectInsite = Mockito.spy(CrFileObjectInsite.class);
    crFileObjectInsite.setLinkTemplate("/unit/test/tripm/filename.xlsx");
    crFileObjectInsite.setCrProcessId("1111");
    crFileObjectInsite.setIdTemplate("1500");
    crFileObjectInsite.setIndexFile(0);

    List<CrFileObjectInsite> crFileObjects = Mockito.spy(ArrayList.class);
    crFileObjects.add(crFileObjectInsite);

    CrFilesAttachInsiteDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachDTO.setCrFileObjects(crFileObjects);
    crFilesAttachDTO.setStateCr("6");
    crFilesAttachDTO.setCrId(5L);
    crFilesAttachDTO.setFilePath("/temp");
    crFilesAttachDTO.setFilePathFtp("/temp");
    crFilesAttachDTO.setTempImportId(5L);
    crFilesAttachDTO.setFileName("filename");
    crFilesAttachDTO.setFileType("5555");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("username");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);

    CrProcessInsideDTO processDTO = Mockito.spy(CrProcessInsideDTO.class);
    processDTO.setIsVmsaActiveCellProcess(1L);
    processDTO.setVmsaActiveCellProcessKey("SUCCESS");

    PowerMockito.when(
        crProcessRepository
            .findCrProcessById(anyLong())
    ).thenReturn(processDTO);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(),
        anyString(), anyString(), anyString(), any(), any()))
        .thenReturn("filename.xlsx");

    PowerMockito.when(
        FileUtils
            .saveUploadFile(anyString(), any(), anyString(), any())
    ).thenReturn("filename.xlsx");

    PowerMockito.when(FileUtils.getFileName(any())).thenReturn("temp.xlsx");

    List<CrFilesAttachInsiteDTO> lstUpdateInsert = Mockito.spy(ArrayList.class);
    lstUpdateInsert.add(crFilesAttachDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesToUpdateOrInsertForList(anyList(), anyBoolean(), anyBoolean())
    ).thenReturn(lstUpdateInsert);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(10L);
    usersInsideDto.setMobile("0909009009");
    PowerMockito.when(
        userRepository.getUserDTOByUserName(anyString())
    ).thenReturn(usersInsideDto);

    List<UsersInsideDto> lst = Mockito.spy(ArrayList.class);
    lst.add(usersInsideDto);
    PowerMockito.when(
        crFilesAttachRepository.actionGetUserByUserName(anyString())
    ).thenReturn(lst);

    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    templateImportDTO.setResult("SUCCESS");
    PowerMockito.when(
        templateImportRepository.validateTempImport(any(), any(), anyString(), anyString())
    ).thenReturn(templateImportDTO);

    ValidateTraceForm validateTraceForm = Mockito.spy(ValidateTraceForm.class);
    PowerMockito.when(wsspmProvidePort.validateUserPhoneNumber(anyList()))
        .thenReturn(validateTraceForm);

    ResultInSideDto actual = crFileAttachBusiness.insertList(
        crFilesAttachDTO,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList
    );

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testInsertList_05() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("tripm");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> multipartFileList = Mockito.spy(ArrayList.class);
    multipartFileList.add(testFile);

    CrFileObjectInsite crFileObjectInsite = Mockito.spy(CrFileObjectInsite.class);
    crFileObjectInsite.setLinkTemplate("/unit/test/tripm/filename.xlsx");
    crFileObjectInsite.setCrProcessId("1111");
    crFileObjectInsite.setIdTemplate("1500");
    crFileObjectInsite.setIndexFile(0);

    List<CrFileObjectInsite> crFileObjects = Mockito.spy(ArrayList.class);
    crFileObjects.add(crFileObjectInsite);

    CrFilesAttachInsiteDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachDTO.setCrFileObjects(crFileObjects);
    crFilesAttachDTO.setStateCr("6");
    crFilesAttachDTO.setCrId(5L);
    crFilesAttachDTO.setFilePath("/temp");
    crFilesAttachDTO.setFilePathFtp("/temp");
    crFilesAttachDTO.setTempImportId(5L);
    crFilesAttachDTO.setFileName("filename");
    crFilesAttachDTO.setFileType("5555");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("username");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);

    CrProcessInsideDTO processDTO = Mockito.spy(CrProcessInsideDTO.class);
    processDTO.setIsVmsaActiveCellProcess(1L);
    processDTO.setVmsaActiveCellProcessKey("SUCCESS");

    PowerMockito.when(
        crProcessRepository
            .findCrProcessById(anyLong())
    ).thenReturn(processDTO);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(),
        anyString(), anyString(), anyString(), any(), any()))
        .thenReturn("123123.xlsx");

    PowerMockito.when(
        FileUtils
            .saveUploadFile(anyString(), any(), anyString(), any())
    ).thenReturn("123123.xlsx");

    PowerMockito.when(FileUtils.getFileName(any())).thenReturn("temp.xlsx");

    List<CrFilesAttachInsiteDTO> lstUpdateInsert = Mockito.spy(ArrayList.class);
    lstUpdateInsert.add(crFilesAttachDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesToUpdateOrInsertForList(anyList(), anyBoolean(), anyBoolean())
    ).thenReturn(lstUpdateInsert);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(10L);
    usersInsideDto.setMobile("0909009009");
    PowerMockito.when(
        userRepository.getUserDTOByUserName(anyString())
    ).thenReturn(usersInsideDto);

    List<UsersInsideDto> lst = Mockito.spy(ArrayList.class);
    lst.add(usersInsideDto);
    PowerMockito.when(
        crFilesAttachRepository.actionGetUserByUserName(anyString())
    ).thenReturn(lst);

    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    templateImportDTO.setResult("SUCCESS");
    PowerMockito.when(
        templateImportRepository.validateTempImport(any(), any(), anyString(), anyString())
    ).thenReturn(templateImportDTO);

    ValidateTraceForm validateTraceForm = Mockito.spy(ValidateTraceForm.class);
    PowerMockito.when(wsspmProvidePort.validateUserPhoneNumber(anyList()))
        .thenReturn(validateTraceForm);
    ResultInSideDto actual = crFileAttachBusiness.insertList(
        crFilesAttachDTO,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList
    );

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testInsertList_06() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("tripm");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> multipartFileList = Mockito.spy(ArrayList.class);
    multipartFileList.add(testFile);

    CrFileObjectInsite crFileObjectInsite = Mockito.spy(CrFileObjectInsite.class);
    crFileObjectInsite.setLinkTemplate("/unit/test/tripm/filename.xlsx");
    crFileObjectInsite.setCrProcessId("1111");
    crFileObjectInsite.setIdTemplate("1500");
    crFileObjectInsite.setIndexFile(0);

    List<CrFileObjectInsite> crFileObjects = Mockito.spy(ArrayList.class);
    crFileObjects.add(crFileObjectInsite);

    List<String> stringList = new ArrayList<>();
    stringList.add("1");
    CrFilesAttachInsiteDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachDTO.setCrFileObjects(crFileObjects);
    crFilesAttachDTO.setStateCr("6");
    crFilesAttachDTO.setCrId(5L);
    crFilesAttachDTO.setFilePath("/temp");
    crFilesAttachDTO.setFilePathFtp("/temp");
    crFilesAttachDTO.setTempImportId(5L);
    crFilesAttachDTO.setFileName("filename");
    crFilesAttachDTO.setFileType("5555");
    crFilesAttachDTO.setLstRemoveFileAttach(stringList);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserName("username");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);

    CrProcessInsideDTO processDTO = Mockito.spy(CrProcessInsideDTO.class);
    processDTO.setIsVmsaActiveCellProcess(1L);
    processDTO.setVmsaActiveCellProcessKey("SUCCESS");
    processDTO.setCrProcessId(5L);

    PowerMockito.when(
        crProcessRepository
            .findCrProcessById(anyLong())
    ).thenReturn(processDTO);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(),
        anyString(), anyString(), anyString(), any(), any()))
        .thenReturn("123123.xlsx");

    PowerMockito.when(
        FileUtils
            .saveUploadFile(anyString(), any(), anyString(), any())
    ).thenReturn("123123.xlsx");

    PowerMockito.when(FileUtils.getFileName(any())).thenReturn("temp.xlsx");

    List<CrFilesAttachInsiteDTO> lstUpdateInsert = Mockito.spy(ArrayList.class);
    lstUpdateInsert.add(crFilesAttachDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesToUpdateOrInsertForList(anyList(), anyBoolean(), anyBoolean())
    ).thenReturn(lstUpdateInsert);
    crFilesAttachDTO.setLstCustomerFile(lstUpdateInsert);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(10L);
    usersInsideDto.setMobile("0909009009");
    PowerMockito.when(
        userRepository.getUserDTOByUserName(anyString())
    ).thenReturn(usersInsideDto);

    List<UsersInsideDto> lst = Mockito.spy(ArrayList.class);
    lst.add(usersInsideDto);
    PowerMockito.when(
        crFilesAttachRepository.actionGetUserByUserName(anyString())
    ).thenReturn(lst);

    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    templateImportDTO.setResult("SUCCESS");
    PowerMockito.when(
        templateImportRepository.validateTempImport(any(), any(), anyString(), anyString())
    ).thenReturn(templateImportDTO);

    ResultFileValidateDTO validateDTO = Mockito.spy(ResultFileValidateDTO.class);
    validateDTO.setResultCode(0);
    PowerMockito.when(
        vipaDdPort
            .validateFiles(anyLong(), anyString(), anyString(), anyString(), any())
    ).thenReturn(validateDTO);

    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    resultFileDataOld.setId(3L);
    PowerMockito.when(
        crFilesAttachRepository
            .add(any())
    ).thenReturn(resultFileDataOld);

    ValidateTraceForm validateTraceForm = Mockito.spy(ValidateTraceForm.class);
    PowerMockito.when(wsspmProvidePort.validateUserPhoneNumber(anyList()))
        .thenReturn(validateTraceForm);
    ResultInSideDto actual = crFileAttachBusiness.insertList(
        crFilesAttachDTO,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList,
        multipartFileList, multipartFileList, multipartFileList, multipartFileList
    );

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

//  @Test
//  public void testLoadDtFromTDTT() {
//  }
//
//  @Test
//  public void loadDtFromVIPA() {
//  }
//
//  @Test
//  public void getMopInfo() {
//  }
//
//  @Test
//  public void getNetworkNodeTDTTV2() {
//  }
//
//  @Test
//  public void getMopFile() {
//  }
//
//  @Test
//  public void findFileAttachById() {
//  }
//
//  @Test
//  public void getListCrFilesAttachDTO() {
//  }
//
//  @Test
//  public void getListTemplateFileByProcess() {
//  }
//
//  @Test
//  public void updoadTemplateFiles() {
//  }

//  @Test
//  public void validateFile() {
//  }
//
//  @Test
//  public void insertListNoIDForImport() {
//  }
//
//  @Test
//  public void actionImportFile() {
//  }

  @Test
  public void testGetLstPhone() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("1");

    CrFilesAttachInsiteDTO fileImport = Mockito.spy(CrFilesAttachInsiteDTO.class);
    fileImport.setFilePath("/temp");

    Object[] objects = new Object[]{"1", "1"};
    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    lstDataAll.add(objects);
    lstDataAll.add(objects);
    lstDataAll.add(objects);
    lstDataAll.add(objects);
    lstDataAll.add(objects);
    lstDataAll.add(objects);
    lstDataAll.add(objects);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())
    ).thenReturn(lstDataAll);

    List<PhoneNumberForm> actual = crFileAttachBusiness.getLstPhone(fileImport);

    Assert.assertNotNull(actual);
  }

//  @Test
//  public void insertListNoID() {
//  }

  @Test
  public void testSelectDTFile_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(any())).thenReturn("UNIT-TEST");

    List<AttachDtDTO> lstAttachDtSelected = Mockito.spy(ArrayList.class);

    ResultInSideDto actual = crFileAttachBusiness
        .selectDTFile(lstAttachDtSelected, "11", "22", "33");

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void testSelectDTFile_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(any())).thenReturn("UNIT-TEST");

    AttachDtDTO attachDtDTO = Mockito.spy(AttachDtDTO.class);
    attachDtDTO.setNationCode("1111");
    attachDtDTO.setDtCode("111");
    List<AttachDtDTO> lstAttachDtSelected = Mockito.spy(ArrayList.class);
    lstAttachDtSelected.add(attachDtDTO);

    com.viettel.vipa.MopDetailOutputDTO vipaIpMop = Mockito
        .spy(com.viettel.vipa.MopDetailOutputDTO.class);
    vipaIpMop.setResultCode(0);
    PowerMockito.when(wsVipaIpPort.getMopInfo(anyString())).thenReturn(vipaIpMop);

    ResultInSideDto actual = crFileAttachBusiness
        .selectDTFile(lstAttachDtSelected, "1", "22", "33");

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void testSelectDTFile_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(any())).thenReturn("UNIT-TEST");

    List<String> stringList = Mockito.spy(ArrayList.class);
    stringList.add("1");
    AttachDtDTO attachDtDTO = Mockito.spy(AttachDtDTO.class);
    attachDtDTO.setDtCode("111");
    attachDtDTO.setLstIpAffect(stringList);
    List<AttachDtDTO> lstAttachDtSelected = Mockito.spy(ArrayList.class);
    lstAttachDtSelected.add(attachDtDTO);

    MopDetailDTO mopDetailDTO = Mockito.spy(MopDetailDTO.class);
    mopDetailDTO.setNationCode("11");
    com.viettel.vipa.MopDetailOutputDTO vipaIpMop = Mockito
        .spy(com.viettel.vipa.MopDetailOutputDTO.class);
    vipaIpMop.setResultCode(0);
    vipaIpMop.setMopDetailDTO(mopDetailDTO);
    PowerMockito.when(wsVipaIpPort.getMopInfo(anyString())).thenReturn(vipaIpMop);

    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    List<InfraDeviceDTO> lstNodeAffect = Mockito.spy(ArrayList.class);
    lstNodeAffect.add(infraDeviceDTO);
    PowerMockito.when(
        crDtRepository.geInfraDeviceByIps(anyList(), anyString())
    ).thenReturn(lstNodeAffect);

    ResultInSideDto actual = crFileAttachBusiness
        .selectDTFile(lstAttachDtSelected, "1", "22", "33");

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void testSelectDTFile_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(any())).thenReturn("UNIT-TEST");

    List<String> stringList = Mockito.spy(ArrayList.class);
    stringList.add("1");
    AttachDtDTO attachDtDTO = Mockito.spy(AttachDtDTO.class);
    attachDtDTO.setDtCode("111");
    attachDtDTO.setLstIpAffect(stringList);
    attachDtDTO.setLstIpImpact(stringList);
    List<AttachDtDTO> lstAttachDtSelected = Mockito.spy(ArrayList.class);
    lstAttachDtSelected.add(attachDtDTO);

    MopDetailDTO mopDetailDTO = Mockito.spy(MopDetailDTO.class);
    mopDetailDTO.setNationCode("11");
    com.viettel.vipa.MopDetailOutputDTO vipaIpMop = Mockito
        .spy(com.viettel.vipa.MopDetailOutputDTO.class);
    vipaIpMop.setResultCode(0);
    vipaIpMop.setMopDetailDTO(mopDetailDTO);
    PowerMockito.when(wsVipaIpPort.getMopInfo(anyString())).thenReturn(vipaIpMop);

    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    infraDeviceDTO.setDeviceId("10");
    infraDeviceDTO.setIp("192.168.1.1");
    List<InfraDeviceDTO> lstNodeAffect = Mockito.spy(ArrayList.class);
    lstNodeAffect.add(infraDeviceDTO);
    lstNodeAffect.add(infraDeviceDTO);
    PowerMockito.when(
        crDtRepository.geInfraDeviceByIps(anyList(), anyString())
    ).thenReturn(lstNodeAffect);

    PowerMockito.when(crProcessFromClient.getLstIpInvalid(anyList(), any()))
        .thenReturn("");

    ResultInSideDto actual = crFileAttachBusiness
        .selectDTFile(lstAttachDtSelected, "1", "22", "33");

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void testSelectDTFile_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(Base64.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.when(I18n.getChangeManagement(any())).thenReturn("UNIT-TEST");

    List<String> stringList = Mockito.spy(ArrayList.class);
    stringList.add("1");
    AttachDtDTO attachDtDTO = Mockito.spy(AttachDtDTO.class);
    attachDtDTO.setDtCode("111");
    attachDtDTO.setLstIpAffect(stringList);
    attachDtDTO.setLstIpImpact(stringList);
    List<AttachDtDTO> lstAttachDtSelected = Mockito.spy(ArrayList.class);
    lstAttachDtSelected.add(attachDtDTO);

    MopDetailDTO mopDetailDTO = Mockito.spy(MopDetailDTO.class);
    mopDetailDTO.setNationCode("11");
    mopDetailDTO.setMopFileName("MopFileName");
    mopDetailDTO.setMopFileContent("MopFileContent");
    mopDetailDTO.setKpiFileName("KpiFileName");
    mopDetailDTO.setKpiFileContent("KpiFileContent");
    com.viettel.vipa.MopDetailOutputDTO vipaIpMop = Mockito
        .spy(com.viettel.vipa.MopDetailOutputDTO.class);
    vipaIpMop.setResultCode(0);
    vipaIpMop.setMopDetailDTO(mopDetailDTO);
    PowerMockito.when(wsVipaIpPort.getMopInfo(anyString())).thenReturn(vipaIpMop);

    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    infraDeviceDTO.setDeviceId("10");
    infraDeviceDTO.setIp("192.168.1.1");
    List<InfraDeviceDTO> lstNodeAffect = Mockito.spy(ArrayList.class);
    lstNodeAffect.add(infraDeviceDTO);
    lstNodeAffect.add(infraDeviceDTO);
    PowerMockito.when(
        crDtRepository.geInfraDeviceByIps(anyList(), anyString())
    ).thenReturn(lstNodeAffect);

    PowerMockito.when(crProcessFromClient.getLstIpInvalid(anyList(), any()))
        .thenReturn("");

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(Base64.decode(anyString())).thenReturn(fileContent);

    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(
        FileUtils
            .saveFtpFile(
                anyString(), anyInt(), anyString(), anyString(), anyString(),
                anyString(), any(), any()
            )
    ).thenReturn("tripm");

    ResultInSideDto actual = crFileAttachBusiness
        .selectDTFile(lstAttachDtSelected, "1", "22", "33");

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

//  @Test
//  public void split() {}
//
//  @Test
//  public void loadDtTestFromVIPA() {
//  }
//
//  @Test
//  public void selectDTTestFile() {
//  }
//
//  @Test
//  public void search() {
//  }
//
//  @Test
//  public void getListFileImportByProcess() {
//  }

  @Test
  public void getListCrFilesSearchCheckRole_01() {
    CrFilesAttachInsiteDTO arg1 = PowerMockito.mock(CrFilesAttachInsiteDTO.class);
    PowerMockito.when(arg1.getCrId()).thenReturn(5l);
    mockGetListCrFilesSearchCheckRole("01");
    ResultInSideDto resultInSideDto = crFileAttachBusiness.getListCrFilesSearchCheckRole(arg1);
    Assert.assertEquals(RESULT.SUCCESS, resultInSideDto.getKey());
  }

  @Test
  public void getListCrFilesSearchCheckRole_02() {
    CrFilesAttachInsiteDTO arg1 = PowerMockito.mock(CrFilesAttachInsiteDTO.class);
    PowerMockito.when(arg1.getCrId()).thenReturn(5l);
    mockGetListCrFilesSearchCheckRole("02");
    ResultInSideDto resultInSideDto = crFileAttachBusiness.getListCrFilesSearchCheckRole(arg1);
    Assert.assertEquals(RESULT.SUCCESS, resultInSideDto.getKey());
  }

  @Test
  public void getListCrFilesSearchCheckRole_03() {
    CrFilesAttachInsiteDTO arg1 = PowerMockito.mock(CrFilesAttachInsiteDTO.class);
    PowerMockito.when(arg1.getCrId()).thenReturn(5l);
    mockGetListCrFilesSearchCheckRole("03");
    ResultInSideDto resultInSideDto = crFileAttachBusiness.getListCrFilesSearchCheckRole(arg1);
    Assert.assertEquals(RESULT.SUCCESS, resultInSideDto.getKey());
  }

  @Test
  public void loadDtFromTDTT_01() throws Exception {
    String arg1 = "1";
    String arg2 = "1";
    String arg3 = "1";
    MopResult mopResult = Mockito.spy(MopResult.class);
    List<MopInfo> lstMopInfo = Mockito.spy(ArrayList.class);
    MopInfo mopInfo = PowerMockito.mock(MopInfo.class);
    lstMopInfo.add(mopInfo);
    PowerMockito.when(mopInfo.getCode()).thenReturn("DT_CODE_01");
    PowerMockito.when(mopInfo.getName()).thenReturn("DT_NAME_01");
    PowerMockito.when(mopInfo.getCreatedDate())
        .thenReturn(PowerMockito.mock(XMLGregorianCalendar.class));
    mopResult.getMopInfos().addAll(lstMopInfo);
    PowerMockito.when(wstdttPort.getListMop(anyString(), anyString(), anyString()))
        .thenReturn(mopResult);
    List<AttachDtDTO> result = crFileAttachBusiness.loadDtFromTDTT(arg1, arg2, arg3);
    Assert.assertEquals(1, result.size());
  }

  @Test
  public void loadDtFromVIPA_01() throws Exception {
    String arg1 = "1";
    String arg2 = "1";
    String arg3 = "1";
    String arg4 = "1";
    com.viettel.vipa.MopOutputDTO mopResult = Mockito.spy(com.viettel.vipa.MopOutputDTO.class);
    List<com.viettel.vipa.MopDTO> lstMopInfo = Mockito.spy(ArrayList.class);
    com.viettel.vipa.MopDTO mopInfo = PowerMockito.mock(com.viettel.vipa.MopDTO.class);
    lstMopInfo.add(mopInfo);
    PowerMockito.when(mopInfo.getMopId()).thenReturn("1");
    mopResult.getMops().addAll(lstMopInfo);
    PowerMockito.when(wsVipaIpPort.getMopByUser(anyString(), anyString(), anyString()))
        .thenReturn(mopResult);
    List<AttachDtDTO> result = crFileAttachBusiness.loadDtFromVIPA(arg1, arg2, arg3, arg4);
    Assert.assertEquals(1, result.size());
  }

  @Test
  public void loadDtFromVIPA_02() throws Exception {
    String arg1 = "1";
    String arg2 = "2";
    String arg3 = "1";
    String arg4 = "1";
    com.viettel.vmsa.MopOutputDTO mopResult = Mockito.spy(com.viettel.vmsa.MopOutputDTO.class);
    List<com.viettel.vmsa.MopDTO> lstMopInfo = Mockito.spy(ArrayList.class);
    com.viettel.vmsa.MopDTO mopInfo = PowerMockito.mock(com.viettel.vmsa.MopDTO.class);
    lstMopInfo.add(mopInfo);
    PowerMockito.when(mopInfo.getMopId()).thenReturn("1");
    mopResult.getMops().addAll(lstMopInfo);
    PowerMockito.when(vipaDdPort.getMopByUser(anyString(), anyString(), anyString()))
        .thenReturn(mopResult);
    List<AttachDtDTO> result = crFileAttachBusiness.loadDtFromVIPA(arg1, arg2, arg3, arg4);
    Assert.assertEquals(1, result.size());
  }

  @Test
  public void getMopInfo_01() throws Exception {
    String arg1 = "1";
    MopDetailOutputDTO vipaIpMop = Mockito.spy(MopDetailOutputDTO.class);
    PowerMockito.when(wsVipaIpPort.getMopInfo(arg1)).thenReturn(vipaIpMop);
    MopDetailOutputDTO result = crFileAttachBusiness.getMopInfo(arg1);
    Assert.assertNotNull(result);
  }

  @Test
  public void getMopFile_01() throws Exception {
    String arg1 = "1";
    String arg2 = "2";
    MopFileResult mopResult = Mockito.spy(MopFileResult.class);
    PowerMockito.when(wstdttPort.getMopFile(arg1, arg2)).thenReturn(mopResult);
    MopFileResult result = crFileAttachBusiness.getMopFile(arg1, arg2);
    Assert.assertNotNull(result);
  }

  @Test
  public void findFileAttachById_01() {
    Long arg1 = 1l;
    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    PowerMockito.when(crFilesAttachRepository.findFileAttachById(arg1))
        .thenReturn(crFilesAttachInsiteDTO);
    CrFilesAttachInsiteDTO result = crFileAttachBusiness.findFileAttachById(arg1);
    Assert.assertNotNull(result);
  }

  @Test
  public void getListCrFilesAttachDTO_01() {
    CrFilesAttachInsiteDTO arg1 = Mockito.spy(CrFilesAttachInsiteDTO.class);
    int arg2 = 0;
    int arg3 = 1;
    String arg4 = "";
    String arg5 = "";
    List<CrFilesAttachInsiteDTO> lst = Mockito.spy(ArrayList.class);
    CrFilesAttachInsiteDTO item = Mockito.spy(CrFilesAttachInsiteDTO.class);
    lst.add(item);
    PowerMockito.when(crFilesAttachRepository.getListCrFilesAttachDTO(arg1, arg2, arg3, arg4, arg5))
        .thenReturn(lst);
    List<CrFilesAttachInsiteDTO> result = crFileAttachBusiness
        .getListCrFilesAttachDTO(arg1, arg2, arg3, arg4, arg5);
    Assert.assertEquals(1, result.size());
  }

  @Test
  public void getListTemplateFileByProcess_01() {
    String arg1 = "101";
    String arg2 = "1";
    List<CrFileObjectInsite> lst = Mockito.spy(ArrayList.class);
    CrFileObjectInsite item = Mockito.spy(CrFileObjectInsite.class);
    lst.add(item);
    PowerMockito.when(crFilesAttachRepository
        .getListTemplateFileByProcess(arg1, CR_FILE_TYPE.IMPORT_BY_PROCESS_IN)).thenReturn(lst);
    List<CrFileObjectInsite> result = crFileAttachBusiness.getListTemplateFileByProcess(arg1, arg2);
    Assert.assertEquals(1, result.size());
  }

  @Test
  public void insertListNoIDForImportForSR_01() throws Exception {
    List<CrFilesAttachInsiteDTO> arg1 = Mockito.spy(ArrayList.class);
    UserToken arg2 = PowerMockito.mock(UserToken.class);
    UnitDTO arg3 = PowerMockito.mock(UnitDTO.class);
    boolean arg4 = false;
    CrFileAttachBusinessImpl crFileAttachBusinessInline = Mockito.spy(crFileAttachBusiness);
    CrFilesAttachInsiteDTO item = PowerMockito.mock(CrFilesAttachInsiteDTO.class);
    PowerMockito.when(item.getFilePathFtp()).thenReturn("/GNOC/PATH");
    new CrBusinessImplTest().mockFileUtilsSaveFile();
    mockFileUtilsGetFile();
    List<TemplateImportDTO> lstTemplateImportDTO = Mockito.spy(ArrayList.class);
    TemplateImportDTO templateImportDTO = Mockito.spy(TemplateImportDTO.class);
    lstTemplateImportDTO.add(templateImportDTO);
    PowerMockito.when(crFileAttachBusinessInline.insertListNoIDForImport(arg1, arg2, arg3, arg4))
        .thenReturn(lstTemplateImportDTO);
    List<TemplateImportDTO> result = crFileAttachBusinessInline
        .insertListNoIDForImportForSR(arg1, arg2, arg3, arg4);
    Assert.assertEquals(1, result.size());
  }

  @Test
  public void insertListNoID_01() {
    List<CrFilesAttachInsiteDTO> arg1 = Mockito.spy(ArrayList.class);
    CrFilesAttachInsiteDTO item = PowerMockito.mock(CrFilesAttachInsiteDTO.class);
    arg1.add(item);
    PowerMockito.when(item.getUserId()).thenReturn(999999l);
    UsersEntity usersEntity = PowerMockito.mock(UsersEntity.class);

    UnitDTO unitDTO = PowerMockito.mock(UnitDTO.class);
    PowerMockito.when(usersEntity.getUserId()).thenReturn(999999l);
    PowerMockito.when(usersEntity.getUnitId()).thenReturn(413314l);
    PowerMockito.when(userRepository.getUserByUserId(any())).thenReturn(usersEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(crFilesAttachRepository.getListCrFilesToUpdateOrInsert(arg1, false))
        .thenReturn(arg1);
    ResultInSideDto resultInSideDto = new CrBusinessImplTest().getResultInsideSuccess();
    PowerMockito.when(crFilesAttachRepository.add(any())).thenReturn(resultInSideDto);
    PowerMockito.when(gnocFileRepository.saveListGnocFileNotDeleteAll(any(), any(), any()))
        .thenReturn(resultInSideDto);
    String result = crFileAttachBusiness.insertListNoID(arg1);
    Assert.assertEquals(RESULT.SUCCESS, result);
  }

  @Test
  public void split_01() {
    List<String> lstId = Mockito.spy(ArrayList.class);
    lstId.add("1");
    String result = crFileAttachBusiness.split(lstId);
    Assert.assertNotNull(result);
  }

  @Test
  public void loadDtTestFromVIPA_01() throws Exception {
    String arg1 = "thanhlv12";
    MopOutputDTO mopResult = Mockito.spy(MopOutputDTO.class);
    List<com.viettel.vmsa.MopDTO> lstMopInfo = Mockito.spy(ArrayList.class);
    com.viettel.vmsa.MopDTO mopInfo = PowerMockito.mock(com.viettel.vmsa.MopDTO.class);
    PowerMockito.when(mopInfo.getMopId()).thenReturn("1");
    lstMopInfo.add(mopInfo);
    PowerMockito.when(vipaDdPort.getMopTestByUser(arg1, "5")).thenReturn(mopResult);
    PowerMockito.when(mopResult.getMops()).thenReturn(lstMopInfo);
    List<AttachDtDTO> result = crFileAttachBusiness.loadDtTestFromVIPA(arg1);
    Assert.assertEquals(1, result.size());
  }

  @Test
  public void selectDTTestFile_01() throws Exception {
    List<AttachDtDTO> arg1 = Mockito.spy(ArrayList.class);
    String arg2 = "1";
    String arg3 = "CR_NUMBER_4";
    String arg4 = "4";
    AttachDtDTO item = PowerMockito.mock(AttachDtDTO.class);
    arg1.add(item);
    com.viettel.vmsa.MopDetailOutputDTO vipaDdMop = Mockito
        .spy(com.viettel.vmsa.MopDetailOutputDTO.class);
    com.viettel.vmsa.MopDetailDTO mopDetailDTO = PowerMockito
        .mock(com.viettel.vmsa.MopDetailDTO.class);
    vipaDdMop.setMopDetailDTO(mopDetailDTO);
    PowerMockito.when(vipaDdMop.getResultCode()).thenReturn(0);
    PowerMockito.when(item.getNationCode()).thenReturn("VNM");
    PowerMockito.when(item.getDtCode()).thenReturn("DT_01");
    PowerMockito.when(vipaDdPort.getMopInfo(anyString())).thenReturn(vipaDdMop);
    PowerMockito.when(vipaDdMop.getMopDetailDTO()).thenReturn(mopDetailDTO);
    PowerMockito.when(mopDetailDTO.getMopFileContent()).thenReturn("some content");
    PowerMockito.when(mopDetailDTO.getMopFileName()).thenReturn("some name");
    new CrBusinessImplTest().mockFileUtilsSaveFile();
    PowerMockito.mockStatic(Base64.class);
    byte[] bytes = new byte[]{1, 2};
    PowerMockito.when(Base64.decode("some content")).thenReturn(bytes);
    ResultInSideDto result = crFileAttachBusiness.selectDTTestFile(arg1, arg2, arg3, arg4);
    Assert.assertEquals(RESULT.SUCCESS, result.getKey());
  }

  @Test
  public void search_01() throws Exception {
    CrFilesAttachDTO arg1 = PowerMockito.mock(CrFilesAttachDTO.class);
    List<CrFilesAttachDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(arg1);
    PowerMockito.when(crFilesAttachRepository.search(arg1, 0, 1, "", "")).thenReturn(lst);
    List<CrFilesAttachDTO> result = crFileAttachBusiness.search(arg1, 0, 1, "", "");
    Assert.assertEquals(1, result.size());
  }

  @Test
  public void getListFileImportByProcess_01() throws Exception {
    CrFilesAttachDTO arg1 = PowerMockito.mock(CrFilesAttachDTO.class);
    List<CrFilesAttachResultDTO> lst = Mockito.spy(ArrayList.class);
    PowerMockito.when(crFilesAttachRepository.getListFileImportByProcessOutSide(arg1))
        .thenReturn(lst);
    List<CrFilesAttachResultDTO> result = crFileAttachBusiness.getListFileImportByProcess(arg1);
    Assert.assertEquals(0, result.size());
  }

  @Test
  public void deleteFileDT_01() throws Exception {
    String arg1 = "1";
    List<CrFilesAttachDTO> lst = Mockito.spy(ArrayList.class);
    CrFilesAttachDTO item = Mockito.spy(CrFilesAttachDTO.class);
    item.setFileId("1");
    lst.add(item);
    PowerMockito.when(crFilesAttachRepository.getCrFileDT(arg1)).thenReturn(lst);
    PowerMockito.doNothing().when(crFilesAttachRepository).deleteFileDT(anyList(), anyLong());
    ResultInSideDto result = crFileAttachBusiness.deleteFileDT(arg1);
    Assert.assertEquals(RESULT.SUCCESS, result.getKey());
  }

  @Test
  public void getCrFileAttachForOutSide_01() throws Exception {
    CrFilesAttachDTO arg1 = Mockito.spy(CrFilesAttachDTO.class);
    List<CrFilesAttachDTO> lst = Mockito.spy(ArrayList.class);
    arg1.setFileId("1");
    lst.add(arg1);
    PowerMockito.when(crFilesAttachRepository.getCrFileAttachForOutSide(arg1, 0, 1, "", ""))
        .thenReturn(lst);
    List<CrFilesAttachDTO> result = crFileAttachBusiness
        .getCrFileAttachForOutSide(arg1, 0, 1, "", "");
    Assert.assertEquals(1, result.size());
  }

  public void mockGetListCrFilesSearchCheckRole(String caseChoose) {
    new CrBusinessImplTest().mockUserToken();
    mockCrFindId();
    CfgRoleDataDTO cfgRoleDataDTO = PowerMockito.mock(CfgRoleDataDTO.class);
    if ("01".equals(caseChoose) || "02".equals(caseChoose)) {
      PowerMockito.when(cfgRoleDataRepository.getConfigByDto(any())).thenReturn(cfgRoleDataDTO);
    }
    switch (caseChoose) {
      case "01":
        PowerMockito.when(cfgRoleDataDTO.getRole()).thenReturn(0l);
        break;
      case "02":
      case "03":
        PowerMockito.when(cfgRoleDataDTO.getRole()).thenReturn(2l);
        PowerMockito.when(cfgRoleDataDTO.getAuditUnitId()).thenReturn("1,2");
        List<UnitDTO> lstUnit = Mockito.spy(ArrayList.class);
        UnitDTO unitDTO = PowerMockito.mock(UnitDTO.class);
        lstUnit.add(unitDTO);
        PowerMockito.when(unitDTO.getUnitId()).thenReturn(413314l);
        PowerMockito.when(cfgRoleDataDTO.getAuditUnitId()).thenReturn("1,2");
        PowerMockito.when(unitRepository.getListUnitChildren(anyLong())).thenReturn(lstUnit);
        break;
    }
    List<CrCabUsersDTO> lstUserCab = Mockito.spy(ArrayList.class);
    CrCabUsersDTO userCab = PowerMockito.mock(CrCabUsersDTO.class);
    lstUserCab.add(userCab);
    PowerMockito.when(userCab.getUserID()).thenReturn(999999L);
    PowerMockito.when(crGeneralRepository.getListUserCab(any())).thenReturn(lstUserCab);
    Datatable datatable = PowerMockito.mock(Datatable.class);
    PowerMockito.when(crFilesAttachRepository.getListFilesSearchDataTable(any()))
        .thenReturn(datatable);
  }

  public void mockFileUtilsGetFile() throws Exception {
    byte[] arrbytes = new byte[]{1, 2};
    PowerMockito
        .when(FileUtils.getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString()))
        .thenReturn(arrbytes);

  }

  public void mockCrFindId() {
    CrInsiteDTO inputCr = Mockito.spy(CrInsiteDTO.class);
    inputCr.setCrId("5");
    inputCr.setCrNumber("CR_NUMBER_CODE_5");
    inputCr.setCountry("3500289726");
    inputCr.setHandoverCa("999999");
    inputCr.setChangeResponsible("999999");
    inputCr.setWorkLog("111");
    inputCr.setChangeResponsibleUnit("413314");
    inputCr.setConsiderUnitId("413314");
    inputCr.setActionRight("12");
    List<MultipartFile> lstFile = Mockito.spy(ArrayList.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    lstFile.add(multipartFile);
    PowerMockito.when(crRepository.findCrById(anyLong())).thenReturn(inputCr);
  }
}
