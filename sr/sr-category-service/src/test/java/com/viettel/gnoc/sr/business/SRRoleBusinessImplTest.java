package com.viettel.gnoc.sr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.repository.SRRoleRepository;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SRRoleBusinessImpl.class, FileUtils.class, CommonImport.class, CommonExport.class,
    TicketProvider.class, I18n.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class SRRoleBusinessImplTest {

  @InjectMocks
  SRRoleBusinessImpl srRoleBusiness;

  @Mock
  SRRoleRepository srRoleRepository;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

//  @Before
//  public void setUpTempFolder() {
//    ReflectionTestUtils.setField(srRoleBusiness, "tempFolder",
//        "./sr-temp");
//  }

  @Test
  public void test_getListSRRoleByLocationCBB() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    List<SRRoleDTO> data = Mockito.spy(ArrayList.class);
    PowerMockito.when(srRoleRepository.getListSRRoleByLocationCBB(any())).thenReturn(data);
    List<SRRoleDTO> result = srRoleBusiness.getListSRRoleByLocationCBB(srRoleDTO);
    Assert.assertEquals(data, result);
  }

  @Test
  public void test_getListSRRolePage() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    Datatable datatable = PowerMockito.mock(Datatable.class);
    PowerMockito.when(srRoleRepository.getListSRRolePage(any())).thenReturn(datatable);
    Datatable result = srRoleBusiness.getListSRRolePage(srRoleDTO);
    Assert.assertEquals(datatable, result);
  }

  @Test
  public void test_insert() {
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");

    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    srRoleDTO.setRoleCode("123QWE");
    srRoleDTO.setCreatedUser("thanhlv12");
    srRoleDTO.setCreatedTime(new Date());

    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(srRoleRepository.add(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = srRoleBusiness.insert(srRoleDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void test_update() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String date1 = "19/05/2019";
    Date crTime = new SimpleDateFormat("dd/MM/yyyy").parse(date1);
    SRRoleDTO detail = Mockito.spy(SRRoleDTO.class);
    detail.setCreatedTime(crTime);
    detail.setCreatedUser("thanhlv12");
    PowerMockito.when(srRoleRepository.getDetail(1L)).thenReturn(detail);

    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    srRoleDTO.setRoleId(1L);
    srRoleDTO.setRoleCode("123QWE");
    srRoleDTO.setCreatedUser(detail.getCreatedUser());
    srRoleDTO.setCreatedTime(detail.getCreatedTime());
    srRoleDTO.setUpdatedUser("thanhlv12");
    srRoleDTO.setUpdatedTime(new Date());

    PowerMockito.when(srRoleRepository.edit(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = srRoleBusiness.update(srRoleDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void test_delete() {
    Logger logger = Mockito.spy(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(10L);
    PowerMockito.when(srRoleRepository.delete(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto result = srRoleBusiness.delete(123L);
    Assert.assertEquals(resultInSideDto, result);
  }

  @Test
  public void test_getDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    PowerMockito.when(srRoleRepository.getDetail(anyLong())).thenReturn(srRoleDTO);
    SRRoleDTO result = srRoleBusiness.getDetail(10L);
    Assert.assertEquals(srRoleDTO, result);
  }

  @Test
  public void test_getTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = Mockito.spy(ExcelWriterUtils.class);
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("aa");
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(281L);
    List<ItemDataCRInside> countryNameList = Mockito.spy(ArrayList.class);
    countryNameList.add(itemDataCRInside);
    PowerMockito.when(catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null)).thenReturn(countryNameList);

    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    srRoleDTO.setCountry("281");
    srRoleDTO.setRoleCode("abc");
    srRoleDTO.setRoleName("xxx");
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    srRoleDTOList.add(srRoleDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(srRoleDTOList);
    PowerMockito.when(srRoleRepository.getListDataExport(any())).thenReturn(datatable);

    //set tên trang excel
    PowerMockito.when(I18n.getLanguage("srRole.title")).thenReturn("Nhóm xử lý SR");
    //set tên file excel
    String tempFolder = "./sr-templates/Junit";
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_SR_ROLE" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    srRoleBusiness.getTemplate();
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void test_exportData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    srRoleDTOList.add(srRoleDTO);
    datatable.setData(srRoleDTOList);
    PowerMockito.when(srRoleRepository.getListDataExport(any())).thenReturn(datatable);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("srRole.export.title")).thenReturn("sheetName");
    File file = new File("TEMPLATE_EXPORT.xlsx");
    srRoleBusiness.exportData(srRoleDTO);
    Assert.assertNotNull(file);
  }

  @Test
  public void test_importData01() throws Exception {
    //Truong hop multipartFile == null
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(10L);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    resultInSideDto.setKey(RESULT.FILE_IS_NULL);
    ResultInSideDto result = srRoleBusiness.importData(null);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void test_importData02() throws Exception {
    //Truong hop multipartFile != null
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //Truong hop form header khong dung' chuan?
    PowerMockito.mockStatic(CommonImport.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 5, 0, 15, 1000))
        .thenReturn(headerList);
    resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
    srRoleBusiness.importData(firstFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.FILE_INVALID_FORMAT);
  }

  @Test
  public void test_importData03() throws Exception {
    //Truong hop multipartFile != null
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(10L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //form header dung' chuan
    PowerMockito.mockStatic(CommonImport.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"abc", "abc" + "*",
        "abc" + "*", "abc" + "*", "abc" + "*"};
    headerList.add(header);

    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 4, 0, 4, 1000))
        .thenReturn(headerList);

    //Du~ lieu file DATA_OVER
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2000; i++) {
      dataImportList.add(new Object[250]);
    }
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 5, 0, 4, 1000))
        .thenReturn(dataImportList);

    resultInSideDto.setKey(RESULT.DATA_OVER);
    srRoleBusiness.importData(firstFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.DATA_OVER);
  }

  @Test
  public void test_importData04() throws Exception {
    //Truong hop multipartFile != null
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(10L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //form header dung' chuan
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"abc", "abc" + "*",
        "abc" + "*", "abc" + "*", "abc" + "*"};
    headerList.add(header);

    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 4, 0, 4, 1000))
        .thenReturn(headerList);

    List<SRRoleDTO> srRoleDTOS = Mockito.spy(ArrayList.class);

    //Truong hop dataImportList isEmpty
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);

    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 5, 0, 4, 1000))
        .thenReturn(dataImportList);

    resultInSideDto.setKey(RESULT.NODATA);
    resultInSideDto.setMessage("FILE_NULL");
    srRoleBusiness.importData(firstFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.NODATA);
  }

  @Test
  public void test_importData05() throws Exception {
    //Truong hop multipartFile != null
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(10L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //form header dung' chuan
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"abc", "abc" + "*",
        "abc" + "*", "abc" + "*", "abc" + "*"};
    headerList.add(header);

    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 4, 0, 4, 1000))
        .thenReturn(headerList);

    List<SRRoleDTO> srRoleDTOS = Mockito.spy(ArrayList.class);

    //Truong hop dataImportList != null
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    String[] objImport = new String[]{"abc", "abc", "abc",
        "abc", "abc", "abc"};
    for (int i = 0; i < 2; i++) {
      dataImportList.add(objImport);
    }
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 5, 0, 4, 1000))
        .thenReturn(dataImportList);

    //setMapCountryName
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(10L);
    itemDataCRInside.setDisplayStr("abc");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);

    SRRoleDTO srRoleTmp = Mockito.spy(SRRoleDTO.class);
    PowerMockito.when(srRoleRepository.checkRoleExist("abc", "xyz")).thenReturn(srRoleTmp);

    PowerMockito.when(srRoleRepository.insertOrUpdateList(anyList())).thenReturn(resultInSideDto);

    srRoleBusiness.importData(firstFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void test_importData06() throws Exception {
    //Truong hop multipartFile != null
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(10L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //form header dung' chuan
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"abc", "abc" + "*",
        "abc" + "*", "abc" + "*", "abc" + "*"};
    headerList.add(header);

    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 4, 0, 4, 1000))
        .thenReturn(headerList);

    List<SRRoleDTO> srRoleDTOS = Mockito.spy(ArrayList.class);

    //Truong hop dataImportList != null
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    String[] objImport = new String[]{"abc", "abc", "abc",
        "abc", "abc", "abc"};
    for (int i = 0; i < 1; i++) {
      dataImportList.add(objImport);
    }
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 5, 0, 4, 1000))
        .thenReturn(dataImportList);

    //setMapCountryName
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(10L);
    itemDataCRInside.setDisplayStr("abc");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);

    SRRoleDTO srRoleTmp = Mockito.spy(SRRoleDTO.class);
    PowerMockito.when(srRoleRepository.checkRoleExist("abc", "xyz")).thenReturn(srRoleTmp);
    PowerMockito.when(I18n.getLanguage("srRole.action.0")).thenReturn("con heo");

    PowerMockito.when(srRoleRepository.insertOrUpdateList(anyList()))
        .thenReturn(resultInSideDto);

    srRoleBusiness.importData(firstFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void test_importData07() throws Exception {
    //Truong hop multipartFile != null
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(10L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //form header dung' chuan
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"abc", "abc" + "*",
        "abc" + "*", "abc" + "*", "abc" + "*"};
    headerList.add(header);

    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 4, 0, 4, 1000))
        .thenReturn(headerList);

    List<SRRoleDTO> srRoleDTOS = Mockito.spy(ArrayList.class);

    //Truong hop dataImportList != null
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    String[] objImport = new String[]{"abc", "abc", "abc",
        "abc", "abc", "abc"};

    //truong hop key validate inport 2 ban ghi giong nhau: "validateDuplicate"
    for (int i = 0; i < 2; i++) {
      dataImportList.add(objImport);
    }
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 5, 0, 4, 1000))
        .thenReturn(dataImportList);

    //setMapCountryName
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(10L);
    itemDataCRInside.setDisplayStr("abc");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);

    SRRoleDTO srRoleTmp = Mockito.spy(SRRoleDTO.class);
    PowerMockito.when(srRoleRepository.checkRoleExist("abc", "xyz")).thenReturn(srRoleTmp);
    PowerMockito.when(I18n.getLanguage("srRole.action.0")).thenReturn("con heo");

    PowerMockito.when(srRoleRepository.insertOrUpdateList(anyList()))
        .thenReturn(resultInSideDto);

    srRoleBusiness.importData(firstFile);
    resultInSideDto.setKey(RESULT.ERROR);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
  }

  @Test
  public void test_importData08() throws Exception {
    //Truong hop multipartFile != null
    Logger logger = PowerMockito.mock(Logger.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(10L);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //form header dung' chuan
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"abc", "abc" + "*",
        "abc" + "*", "abc" + "*", "abc" + "*"};
    headerList.add(header);

    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 4, 0, 4, 1000))
        .thenReturn(headerList);

    List<SRRoleDTO> srRoleDTOS = Mockito.spy(ArrayList.class);

    //Truong hop dataImportList != null
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    String[] objImport = new String[]{"abc", "abc", "abc",
        "abc", "abc", "abc"};
    for (int i = 0; i < 2; i++) {
      dataImportList.add(objImport);
    }
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 5, 0, 4, 1000))
        .thenReturn(dataImportList);

    //setMapCountryName
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(10L);
    itemDataCRInside.setDisplayStr("abc");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);

    SRRoleDTO srRoleTmp = Mockito.spy(SRRoleDTO.class);
    PowerMockito.when(srRoleRepository.checkRoleExist(anyString(), anyString()))
        .thenReturn(srRoleTmp);

    PowerMockito.when(srRoleRepository.insertOrUpdateList(anyList())).thenReturn(resultInSideDto);

    srRoleBusiness.importData(firstFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void test_setMapCountryName() {
    Logger logger = PowerMockito.mock(Logger.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("TEST");
    lstCountryName.add(itemDataCRInside);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    srRoleBusiness.setMapCountryName();
    Assert.assertNotNull(lstCountryName);
  }

  @Test
  public void test_getListSRRoleDTO() {
    List<SRRoleDTO> data = Mockito.spy(ArrayList.class);
    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    PowerMockito.when(srRoleRepository.getListSRRoleDTO(any())).thenReturn(data);
    List<SRRoleDTO> result = srRoleBusiness.getListSRRoleDTO(srRoleDTO);
    Assert.assertEquals(data.size(), result.size());
  }

}
