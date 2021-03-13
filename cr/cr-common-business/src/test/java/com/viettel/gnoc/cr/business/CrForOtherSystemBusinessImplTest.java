package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFileAttachOutputWithContent;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrOutputForOCSDTO;
import com.viettel.gnoc.cr.dto.CrOutputForQLTNDTO;
import com.viettel.gnoc.cr.dto.SelectionResultDTO;
import com.viettel.gnoc.cr.repository.CrAffectedServiceDetailsRepository;
import com.viettel.gnoc.cr.repository.CrForOtherSystemRepository;
import com.viettel.gnoc.cr.repository.CrHisRepository;
import com.viettel.gnoc.cr.repository.CrImpactedNodesRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrForOtherSystemBusinessImpl.class, FileUtils.class, I18n.class, DataUtil.class,
    DateTimeUtils.class, PassProtector.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrForOtherSystemBusinessImplTest {

  @InjectMocks
  CrForOtherSystemBusinessImpl crForOtherSystemBusiness;

  @Mock
  CrForOtherSystemRepository crForOtherSystemRepository;

  @Mock
  CrAffectedServiceDetailsRepository crAffectedServiceDetailsRepository;

  @Mock
  CrImpactedNodesRepository crImpactedNodesRepository;

  @Mock
  CrHisRepository crHisRepository;

  @Mock
  CrRepository crRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(crForOtherSystemBusiness, "userConf",
        "thanhlv12");
    ReflectionTestUtils.setField(crForOtherSystemBusiness, "passConf",
        "123456a@");
    ReflectionTestUtils.setField(crForOtherSystemBusiness, "saltConf",
        "Gman");
  }


  @Test
  public void getListDataByObjectId() {
  }

  @Test
  public void getListData() {
  }

  @Test
  public void getCrCreatedFromOtherSysDTO() {
  }

  @Test
  public void checkWoCloseAutoSetting() {
  }

  @Test
  public void testGetCrForQLTN_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "123";

    String kq = "NOK";
    PowerMockito.when(PassProtector.encrypt(anyString(), anyString())).thenReturn("aaa");
    PowerMockito.when(PassProtector.encrypt(anyString(), anyString())).thenReturn("bbb");
    CrOutputForQLTNDTO rs = crForOtherSystemBusiness
        .getCrForQLTN(userService, passService, crNumber);
    Assert.assertEquals(kq, rs.getResultCode());
  }

  @Test
  public void testGetCrForQLTN_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "123";
    CrOutputForQLTNDTO crOutputForQLTNDTO = Mockito.spy(CrOutputForQLTNDTO.class);
    crOutputForQLTNDTO.setResultCode("OK");
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    PowerMockito.when(crForOtherSystemRepository.getCrForQLTN(anyString(), anyString()))
        .thenReturn(crOutputForQLTNDTO);
    CrOutputForQLTNDTO rs = crForOtherSystemBusiness
        .getCrForQLTN(userService, passService, crNumber);
    Assert.assertEquals(rs.getResultCode(), crOutputForQLTNDTO.getResultCode());
  }

  @Test
  public void getListDeviceAffectForSOC() {
  }

  @Test
  public void testGetCrFileDTAttachWithContent_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "123";
    String attachTime = "PPP";
    String fileType = "1";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbb");
    List<CrFileAttachOutputWithContent> list = crForOtherSystemBusiness
        .getCrFileDTAttachWithContent(userService, passService, crNumber, attachTime, fileType);
    Assert.assertEquals(list.size(), 1L);
  }

  @Test
  public void testGetCrFileDTAttachWithContent_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "";
    String attachTime = "PPP";
    String fileType = "1";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    List<CrFileAttachOutputWithContent> list = crForOtherSystemBusiness
        .getCrFileDTAttachWithContent(userService, passService, crNumber, attachTime, fileType);
    Assert.assertEquals(list.size(), 1L);
  }

  @Test
  public void testGetCrFileDTAttachWithContent_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "fgh";
    String attachTime = "PPP";
    String fileType = "";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    List<CrFileAttachOutputWithContent> list = crForOtherSystemBusiness
        .getCrFileDTAttachWithContent(userService, passService, crNumber, attachTime, fileType);
    Assert.assertEquals(list.size(), 1L);
  }

  @Test
  public void testGetCrFileDTAttachWithContent_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "fgh";
    String attachTime = "";
    String fileType = "1";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    List<CrFileAttachOutputWithContent> list = crForOtherSystemBusiness
        .getCrFileDTAttachWithContent(userService, passService, crNumber, attachTime, fileType);
    Assert.assertEquals(list.size(), 1L);
  }

  @Test
  public void testGetCrFileDTAttachWithContent_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "12345";
    String attachTime = "yuio";
    String fileType = "1";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    List<CrFileAttachOutputWithContent> list1 = Mockito.spy(ArrayList.class);
    CrFileAttachOutputWithContent crFileAttachOutputWithContent = Mockito
        .spy(CrFileAttachOutputWithContent.class);
    crFileAttachOutputWithContent.setDescription("thth");
    list1.add(crFileAttachOutputWithContent);
    PowerMockito.when(crForOtherSystemRepository
        .getCrFileDTAttachWithContent(anyString(), anyString(), anyString())).thenReturn(list1);
    List<CrFileAttachOutputWithContent> result = crForOtherSystemBusiness
        .getCrFileDTAttachWithContent(userService, passService, crNumber, attachTime, fileType);
    Assert.assertEquals(list1.size(), result.size());
  }

  @Test
  public void getCrFileDTAttach() {
  }

  @Test
  public void testGetCrlistFromTimeInterval_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String minute = "12345";
    PowerMockito.when(PassProtector.encrypt(anyString(), anyString())).thenReturn("aaaa");
    SelectionResultDTO resultDTO = crForOtherSystemBusiness
        .getCrlistFromTimeInterval(userService, passService, minute);
    Assert.assertNotNull(resultDTO);
  }

  @Test
  public void testGetCrlistFromTimeInterval_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String minute = "12.45";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    SelectionResultDTO selectionResultDTO = Mockito.spy(SelectionResultDTO.class);
    selectionResultDTO.setKey("SUCCESS");
    selectionResultDTO.setMessage("ghighighi");
    PowerMockito.when(crForOtherSystemRepository.getCrlistFromTimeInterval(anyDouble()))
        .thenReturn(selectionResultDTO);
    SelectionResultDTO resultDTO = crForOtherSystemBusiness
        .getCrlistFromTimeInterval(userService, passService, minute);
    Assert.assertNotNull(resultDTO);
  }

  @Test
  public void testActionResolveCrOcs_01() throws Exception {
    String rs = "qqqq";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "123";
    String returnCode = "SUSU";
    String locale = "en_US";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbbb");
    String result = crForOtherSystemBusiness.actionResolveCrOcs(userService, passService,
        userName, crNumber, returnCode, locale);
    Assert.assertEquals(result, rs);
  }

  @Test
  public void testActionResolveCrOcs_02() throws Exception {
    String rs = "CrNumber is require";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "";
    String returnCode = "SUSU";
    String locale = "en_US";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    String result = crForOtherSystemBusiness.actionResolveCrOcs(userService, passService,
        userName, crNumber, returnCode, locale);
    Assert.assertEquals(result, rs);
  }

  @Test
  public void testActionResolveCrOcs_03() throws Exception {
    String rs = "userName is require";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "";
    String crNumber = "123";
    String returnCode = "SUSU";
    String locale = "en_US";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    String result = crForOtherSystemBusiness.actionResolveCrOcs(userService, passService,
        userName, crNumber, returnCode, locale);
    Assert.assertEquals(result, rs);
  }

  @Test
  public void testActionResolveCrOcs_04() throws Exception {
    String rs = "returnCode is require";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "123";
    String returnCode = "";
    String locale = "en_US";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    String result = crForOtherSystemBusiness.actionResolveCrOcs(userService, passService,
        userName, crNumber, returnCode, locale);
    Assert.assertEquals(result, rs);
  }

  @Test
  public void testActionResolveCrOcs_05() throws Exception {
    String rs = "returnCode invalid";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "123";
    String returnCode = "23";
    String locale = "en_US";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    String result = crForOtherSystemBusiness.actionResolveCrOcs(userService, passService,
        userName, crNumber, returnCode, locale);
    Assert.assertEquals(result, rs);
  }

  @Test
  public void testActionResolveCrOcs_06() throws Exception {
    String rs = "user not exist on GNOC";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "123";
    String returnCode = "39";
    String locale = "en_US";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    UsersDTO userDTO = null;
    PowerMockito.when(crForOtherSystemRepository.getUserInfo(any())).thenReturn(userDTO);

    String result = crForOtherSystemBusiness.actionResolveCrOcs(userService, passService,
        userName, crNumber, returnCode, locale);
    Assert.assertEquals(result, rs);
  }

  @Test
  public void testActionResolveCrOcs_07() throws Exception {
    String rs = "SUCCESS";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "123";
    String returnCode = "39";
    String locale = "en_US";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    UsersDTO userDTO = Mockito.spy(UsersDTO.class);
    userDTO.setUnitName("thanhlv12");
    userDTO.setUserId("12");
    userDTO.setUnitId("2L");
    PowerMockito.when(crForOtherSystemRepository.getUserInfo(any())).thenReturn(userDTO);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setNotes("rtrt");
    crInsiteDTO.setCrId("12");
    PowerMockito.when(crRepository.findCrById(anyLong(), any())).thenReturn(crInsiteDTO);
    PowerMockito.when(crRepository.actionResolveCr(any(), anyString())).thenReturn(rs);
    PowerMockito.when(crForOtherSystemRepository
        .createWorklogResolveCR(anyString(), anyString(), anyLong(), anyLong())).thenReturn(rs);

    String result = crForOtherSystemBusiness.actionResolveCrOcs(userService, passService,
        userName, crNumber, returnCode, locale);
    Assert.assertEquals(result, rs);
  }

  @Test
  public void testGetCrForOCS_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String startTime = "15/05/2020";
    PowerMockito.when(PassProtector.encrypt(anyString(), anyString())).thenReturn("qwe");
    List<CrOutputForOCSDTO> list = crForOtherSystemBusiness.getCrForOCS(userService, passService,
        userName, startTime);
    Assert.assertNotNull(list);
  }

  @Test
  public void testGetCrForOCS_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "";
    String startTime = "15/05/2020";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    List<CrOutputForOCSDTO> list = crForOtherSystemBusiness.getCrForOCS(userService, passService,
        userName, startTime);
    Assert.assertNotNull(list);
  }

  @Test
  public void testGetCrForOCS_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String startTime = "";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    List<CrOutputForOCSDTO> list = crForOtherSystemBusiness.getCrForOCS(userService, passService,
        userName, startTime);
    Assert.assertNotNull(list);
  }

  @Test
  public void testGetCrForOCS_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String startTime = ("15/05/2020 01:01:01");
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    List<CrOutputForOCSDTO> crOutputForOCSDTOList = Mockito.spy(ArrayList.class);
    CrOutputForOCSDTO crOutputForOCSDTO = Mockito.spy(CrOutputForOCSDTO.class);
    crOutputForOCSDTO.setCrNumber("123");
    crOutputForOCSDTOList.add(crOutputForOCSDTO);
    PowerMockito.when(crForOtherSystemRepository.getCrForOCS(userName, startTime))
        .thenReturn(crOutputForOCSDTOList);
    List<CrOutputForOCSDTO> list = crForOtherSystemBusiness.getCrForOCS(userService, passService,
        userName, startTime);
    Assert.assertNotNull(list);
  }

  @Test
  public void testInsertFile_01() {
    String userName = "";
    String crNumber = "a";
    String fileType = "b";
    String fileName = "ghj";
    String fileContent = "thyu";
    String result = "NOK";
    ResultDTO rs = crForOtherSystemBusiness
        .insertFile(userName, crNumber, fileType, fileName, fileContent);
    Assert.assertEquals(result, rs.getKey());
  }

  @Test
  public void testInsertFile_02() {
    String userName = "thanhlv12";
    String crNumber = "";
    String fileType = "b";
    String fileName = "ghj";
    String fileContent = "thyu";
    String result = "NOK";
    ResultDTO rs = crForOtherSystemBusiness
        .insertFile(userName, crNumber, fileType, fileName, fileContent);
    Assert.assertEquals(result, rs.getKey());
  }

  @Test
  public void testInsertFile_03() {
    String userName = "thanhlv12";
    String crNumber = "123";
    String fileType = "";
    String fileName = "ghj";
    String fileContent = "thyu";
    String result = "NOK";
    ResultDTO rs = crForOtherSystemBusiness
        .insertFile(userName, crNumber, fileType, fileName, fileContent);
    Assert.assertEquals(result, rs.getKey());
  }

  @Test
  public void testInsertFile_04() {
    String userName = "thanhlv12";
    String crNumber = "123";
    String fileType = "vbvb";
    String fileName = "";
    String fileContent = "thyu";
    String result = "NOK";
    ResultDTO rs = crForOtherSystemBusiness
        .insertFile(userName, crNumber, fileType, fileName, fileContent);
    Assert.assertEquals(result, rs.getKey());
  }

  @Test
  public void testInsertFile_05() {
    String userName = "thanhlv12";
    String crNumber = "123";
    String fileType = "vbvb";
    String fileName = "";
    for (int i = 1; i <= 501; i++) {
      fileName += "fileName.., ";
    }
    String fileContent = "thyu";
    String result = "NOK";
    ResultDTO rs = crForOtherSystemBusiness
        .insertFile(userName, crNumber, fileType, fileName, fileContent);
    Assert.assertEquals(result, rs.getKey());
  }

  @Test
  public void testInsertFile_06() {
    String userName = "thanhlv12";
    String crNumber = "123";
    String fileType = "vbvb";
    String fileName = "kkk";
    String fileContent = "";
    String result = "NOK";
    ResultDTO rs = crForOtherSystemBusiness
        .insertFile(userName, crNumber, fileType, fileName, fileContent);
    Assert.assertEquals(result, rs.getKey());
  }

  @Test
  public void testInsertFile_07() {
    String userName = "thanhlv12";
    String crNumber = "123";
    String fileType = "vbvb";
    String fileName = "kkk";
    String fileContent = "iop";
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setId("11");
    PowerMockito.when(crForOtherSystemRepository
        .insertFile(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(resultDTO);
    ResultDTO rs = crForOtherSystemBusiness
        .insertFile(userName, crNumber, fileType, fileName, fileContent);
    Assert.assertEquals(rs.getKey(), resultDTO.getKey());
  }

  @Test
  public void testCreateCRTraceFileAttach_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String username = "thanhlv12";
    String crId = "1";
    String fileType = "fgh";
    String fileName = "cvcv";
    String fileContent = "hjikop";
    String result = "NOK";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbb");
    ResultDTO rs = crForOtherSystemBusiness
        .createCRTraceFileAttach(userService, passService, username,
            crId, fileType, fileName, fileContent);
    Assert.assertEquals(rs.getKey(), result);
  }

  @Test
  public void testCreateCRTraceFileAttach_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    String username = "thanhlv12";
    String crId = "1";
    String fileType = "fgh";
    String fileName = "cvcv";
    String fileContent = "hjikop";
    String result = "OK";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    String check = "";
    PowerMockito.when(crForOtherSystemRepository
        .createMapFile(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(check);
    ResultDTO rs = crForOtherSystemBusiness
        .createCRTraceFileAttach(userService, passService, username,
            crId, fileType, fileName, fileContent);
    Assert.assertEquals(rs.getKey(), result);
  }

  @Test
  public void updateDtInfo() {
  }

  @Test
  public void testCreateCRTrace_01() throws Exception {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("NOK");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbbb");
    ResultInSideDto rs = crForOtherSystemBusiness.createCRTrace(userService, passService, crDTO);
    Assert.assertEquals(resultInSideDto.getKey(), rs.getKey());
  }

  @Test
  public void testCreateCRTrace_02() throws Exception {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("NOK");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    String check = "ccc";
    PowerMockito.when(crForOtherSystemRepository.setCrInfor(any())).thenReturn(check);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    ResultInSideDto rs = crForOtherSystemBusiness.createCRTrace(userService, passService, crDTO);
    Assert.assertEquals(resultInSideDto.getKey(), rs.getKey());
  }

  @Test
  public void testCreateCRTrace_03() throws Exception {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("OK");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
    String userService = "thanhlv12";
    String passService = "123456a@";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setState("2");
    crDTO.setChangeOrginator("v");
    crDTO.setChangeOrginatorUnit("gh");
    crDTO.setEarliestStartTime("15/05/2020 01:01:01");
    crDTO.setLatestStartTime("15/05/2020 01:01:01");

    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    CrAffectedServiceDetailsDTO detailsDTO = Mockito.spy(CrAffectedServiceDetailsDTO.class);
    detailsDTO.setCrId("1");
    lstAffectedService.add(detailsDTO);
    crDTO.setLstAffectedService(lstAffectedService);

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    crImpactedNodesDTO.setIp("1");
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    String check = "";
    PowerMockito.when(crForOtherSystemRepository.setCrInfor(any())).thenReturn(check);
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey("OK");
    result.setId(1L);
    PowerMockito.when(crForOtherSystemRepository.saveObject(any())).thenReturn(result);
    PowerMockito.when(crAffectedServiceDetailsRepository.saveListDTONoIdSession(anyList()))
        .thenReturn("abc");
    PowerMockito.when(crImpactedNodesRepository.saveListDTONoIdSession(anyList(), any()))
        .thenReturn("cbv");
    PowerMockito.when(crForOtherSystemRepository.saveObjectSession(any())).thenReturn(result);
    ResultInSideDto rs = crForOtherSystemBusiness.createCRTrace(userService, passService, crDTO);
    Assert.assertEquals(result.getKey(), rs.getKey());
  }
}
