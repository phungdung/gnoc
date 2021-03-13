package com.viettel.gnoc.sr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.SrCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.sr.dto.SRMopDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.model.SRFilesEntity;
import com.viettel.gnoc.sr.repository.SRCatalogRepository2;
import com.viettel.gnoc.sr.repository.SRCreateAutoCrRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import com.viettel.security.PassTranformer;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
@PrepareForTest({SRCreateAutoCrBusinessImpl.class, FileUtils.class, CommonImport.class,
    CommonExport.class, TicketProvider.class, PassTranformer.class, ExcelWriterUtils.class,
    Workbook.class, I18n.class, DateTimeUtils.class, InputStream.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class SRCreateAutoCrBusinessImplTest {

  @InjectMocks
  SRCreateAutoCrBusinessImpl srCreateAutoCrBusiness;

  @Mock
  SRCreateAutoCrRepository srCreateAutoCrRepository;

  @Mock
  SRCatalogRepository2 srCatalogRepository2;

  @Mock
  SrCategoryServiceProxy srCategoryServiceProxy;

  @Mock
  SrRepository srRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  CrServiceProxy crServiceProxy;
  @Mock
  ExcelWriterUtils excelWriterUtils;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(srCreateAutoCrBusiness, "tempFolder",
        "/tempFolder");
    ReflectionTestUtils.setField(srCreateAutoCrBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(srCreateAutoCrBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(srCreateAutoCrBusiness, "ftpServer",
        "10.240.232.25");
    ReflectionTestUtils.setField(srCreateAutoCrBusiness, "ftpPort",
        21);
  }

  @Test
  public void findSrCreateAutoBySrId() {
    PowerMockito.mockStatic(TicketProvider.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("111");
    PowerMockito.when(
        TicketProvider.getUserToken()
    ).thenReturn(userToken);
    SRCreateAutoCRDTO expected = Mockito.spy(SRCreateAutoCRDTO.class);
    expected.setFileTypeId("111");
    expected.setPathFileProcess("aaa");
    expected.setId(1L);

    List<SRCreateAutoCRDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(expected);
    PowerMockito.when(
        srCreateAutoCrRepository
            .searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lst);

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrUser("111");
    srInsiteDTO.setServiceId("111");
    PowerMockito.when(
        srRepository.getDetail(anyLong(), anyString())
    ).thenReturn(srInsiteDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setIsCrNodes(1L);
    PowerMockito.when(
        srCatalogRepository2
            .findById(anyLong())
    ).thenReturn(srCatalogDTO);

    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    List<SRMappingProcessCRDTO> listSRMapping = Mockito.spy(ArrayList.class);
    listSRMapping.add(srMappingProcessCRDTO);
    PowerMockito.when(
        srCategoryServiceProxy
            .getListSRMappingProcessCRDTO(any())
    ).thenReturn(listSRMapping);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileName("1");
    List<GnocFileDto> lstFile = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        gnocFileRepository.getListGnocFileByDto(any())
    ).thenReturn(lstFile);

    SRMappingProcessCRDTO startTimeEndTimeFromCrImpact = Mockito.spy(SRMappingProcessCRDTO.class);
    startTimeEndTimeFromCrImpact.setStartTime("29/04/2020 ");
    startTimeEndTimeFromCrImpact.setEndTime("30/04/2020");
    PowerMockito.when(
        srCategoryServiceProxy
            .getStartTimeEndTimeFromCrImpact(any())
    ).thenReturn(startTimeEndTimeFromCrImpact);

    Map<String, GnocFileDto> gnocFilePathProceeMap = new HashMap<>();
    gnocFilePathProceeMap.put("1", gnocFileDto);
    ReflectionTestUtils.setField(srCreateAutoCrBusiness, "gnocFilePathProceeMap",
        gnocFilePathProceeMap);
    SRCreateAutoCRDTO srCreateAutoCRDTO = Mockito.spy(SRCreateAutoCRDTO.class);
    srCreateAutoCRDTO.setSrId(1L);
//    srCreateAutoCRDTO.setSrUser("1");
    srCreateAutoCRDTO.setName("1");
    srCreateAutoCRDTO.setPathFileProcess("1");
    List<SRCreateAutoCRDTO> lstInforTemplate = Mockito.spy(ArrayList.class);
    lstInforTemplate.add(srCreateAutoCRDTO);
    PowerMockito.when(
        srCreateAutoCrRepository
            .getInforTemplate(any())
    ).thenReturn(lstInforTemplate);
    SRCreateAutoCRDTO actual = srCreateAutoCrBusiness.findSrCreateAutoBySrId(srCreateAutoCRDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void insertOrUpdateSRCreateAuto() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("FAIL");
    mockUserToken();
    mockI18n();
    mockFileUtilsSaveFile();
    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> listMultiPartFile = Mockito.spy(ArrayList.class);
    listMultiPartFile.add(testFile);
    UsersInsideDto unitToken = Mockito.spy(UsersInsideDto.class);
    unitToken.setUnitId(123L);
    SRCreateAutoCRDTO dto = Mockito.spy(SRCreateAutoCRDTO.class);
    dto.setFileTypeId("123");
    dto.setPathFileProcess("123");
    dto.setTempImportId("123");
    List<SRCreateAutoCRDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(dto);
    List<SRCreateAutoCRDTO> lstInforTemplate = Mockito.spy(ArrayList.class);
    lstInforTemplate.add(dto);
    dto.setLstInforTemplate(lstInforTemplate);
    dto.setLstFileProcess(lstInforTemplate);
    dto.setId(123L);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileType("ORTHER");
    gnocFileDto.setTemplateId(123L);
    gnocFileDto.setPath("temp.xlsx");
    gnocFileDto.setFileName("123");
    gnocFileDto.setPathTemplate("temp.xlsx");
    List<GnocFileDto> lstGnocFileDto = Mockito.spy(ArrayList.class);
    lstGnocFileDto.add(gnocFileDto);
    SRCreateAutoCrBusinessImpl srCreateAutoCrBusinessInline = Mockito.spy(srCreateAutoCrBusiness);
    PowerMockito.when(userRepository.getUserDTOByUserName(any())).thenReturn(unitToken);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(lstGnocFileDto);
    PowerMockito.when(srCreateAutoCrRepository
        .searchSRCreateAutoCR(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lst);
    ResultInSideDto actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.ERROR, actual.getKey());

    PowerMockito.when(crServiceProxy.getFilePathSrCr(any())).thenReturn(gnocFileDto);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.ERROR, actual.getKey());

    PowerMockito.doReturn(true).when(srCreateAutoCrBusinessInline)
        .getFileData(anyString(), anyString());
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setCrTitle("123");
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setCrStatus(1L);
    PowerMockito.when(srCreateAutoCrRepository.insertOrUpdateSRCreateAutoCr(any()))
        .thenReturn(resultInSideDto);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setServiceAffecting(1L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setAffectingService("123");
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setTotalAffectingCustomers(1L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setTotalAffectingCustomers(0L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setTotalAffectingCustomers(1L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setTotalAffectingMinutes(0L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setTotalAffectingMinutes(1L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setWoFtTypeId(1L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setWoContentCDFT("123");
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setGroupCDFT(1L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setWoFtPriority(1L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setWoTestTypeId(1L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setWoContentService("123");
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setGroupCdFtService(1L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setWoTestPriority(1L);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setWoTestPriority(1L);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrUser("thanhlv12");
    srInsiteDTO.setServiceId("123");
    SRCatalogDTO sRCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    sRCatalogDTO.setServiceCode("123");
    SRMappingProcessCRDTO srmpcrdto = Mockito.spy(SRMappingProcessCRDTO.class);
    srmpcrdto.setIsCrNodes(1L);
    srmpcrdto.setCrProcessParentId(123L);
    List<SRMappingProcessCRDTO> listSRMapping = Mockito.spy(ArrayList.class);
    listSRMapping.add(srmpcrdto);
    PowerMockito.when(srCategoryServiceProxy.getListSRMappingProcessCRDTO(any()))
        .thenReturn(listSRMapping);
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(sRCatalogDTO);
    PowerMockito.when(srRepository.getDetail(any(), anyString())).thenReturn(srInsiteDTO);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setWoFtStartTime(new Date());
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.MINUTE, 30);
    dto.setWoFtStartTime(calendar.getTime());
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setWoFtEndTime(new Date());
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    calendar.add(Calendar.MINUTE, 50);
    dto.setWoFtEndTime(calendar.getTime());
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setWoTestStartTime(new Date());
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    calendar.add(Calendar.MINUTE, 30);
    dto.setWoTestStartTime(calendar.getTime());
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    dto.setWoTestEndTime(new Date());
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    calendar.add(Calendar.MINUTE, 50);
    dto.setWoTestEndTime(calendar.getTime());
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    calendar.add(Calendar.DAY_OF_MONTH, -1);
    dto.setExecutionTime(calendar.getTime());
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    calendar.setTime(new Date());
    calendar.add(Calendar.MINUTE, 50);
    dto.setExecutionTime(calendar.getTime());
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    dto.setExecutionEndTime(calendar.getTime());
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    calendar.setTime(new Date());
    calendar.add(Calendar.MINUTE, 100);
    dto.setExecutionEndTime(calendar.getTime());
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setSecondValue("123");
    List<ItemDataCRInside> lstDutyType = Mockito.spy(ArrayList.class);
    lstDutyType.add(itemDataCRInside);
    SRMopDTO srMopDTO = Mockito.spy(SRMopDTO.class);
    srMopDTO.setIpNode("123");
    List<SRMopDTO> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add(srMopDTO);
    dto.setLstMop(lstMop);
    PowerMockito.when(srCategoryServiceProxy.getDutyTypeByProcessIdProxy(any()))
        .thenReturn(lstDutyType);
    GnocFileDto gnocFileDto1 = Mockito.spy(GnocFileDto.class);
    gnocFileDto1.setPath("temp.xlsx");
    lstGnocFileDto.add(gnocFileDto1);
    dto.setGnocFileDtos(lstGnocFileDto);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    lstGnocFileDto.clear();
    gnocFileDto1.setRequired(1L);
    lstGnocFileDto.add(gnocFileDto1);
    lstGnocFileDto.add(gnocFileDto);
    dto.setGnocFileDtos(lstGnocFileDto);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.FAIL, actual.getKey());

    lstGnocFileDto.clear();
    gnocFileDto1.setRequired(1L);
    gnocFileDto1.setIndexFile(0L);
    lstGnocFileDto.add(gnocFileDto1);
    lstGnocFileDto.add(gnocFileDto);
    dto.setGnocFileDtos(lstGnocFileDto);
    SRFilesEntity srFilesEntity = Mockito.spy(SRFilesEntity.class);
    srFilesEntity.setFileId(123L);
    List<SRFilesEntity> srFilesOldToDelete = Mockito.spy(ArrayList.class);
    srFilesOldToDelete.add(srFilesEntity);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(123L);
    PowerMockito.when(srRepository.getListSRFileByObejctId(anyString(),anyString(),any()))
        .thenReturn(srFilesOldToDelete);
    PowerMockito.when(srRepository.addSRFile(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getOffsetFromUser(anyString()))
        .thenReturn(0.1D);
    PowerMockito.when(srCreateAutoCrRepository.insertOrUpdateSRCreateAutoCr(any()))
        .thenReturn(resultInSideDto);
    actual = srCreateAutoCrBusinessInline
        .insertOrUpdateSRCreateAutoCr(listMultiPartFile, listMultiPartFile, dto);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
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

  public void mockFileUtilsSaveFile() throws Exception {
    byte[] bytes = new byte[]{1, 2, 3};
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(PassTranformer.class);
    ExcelWriterUtils excelWriterUtils = Mockito.spy(ExcelWriterUtils.class);
    Workbook workBook = Mockito.spy(Workbook.class);
    Sheet sheet = Mockito.spy(Sheet.class);
    CellStyle cellSt1 = Mockito.spy(CellStyle.class);
    PowerMockito.when(PassTranformer.decrypt(anyString())).thenReturn("decrypt");
    PowerMockito.when(FileUtils
        .saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(),
            any(), any())).thenReturn("/path/junit");
    PowerMockito.when(FileUtils.saveUploadFile(anyString(), any(), anyString(), any()))
        .thenReturn("/path/upload.xlsx");
    PowerMockito
        .when(FileUtils.getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString()))
        .thenReturn(bytes);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/path/temp.xlsx");
    PowerMockito.when(FileUtils.getFileName(any())).thenReturn("chanquama");
    PowerMockito.when(excelWriterUtils.readFileExcel(any())).thenReturn(workBook);
  }
}
