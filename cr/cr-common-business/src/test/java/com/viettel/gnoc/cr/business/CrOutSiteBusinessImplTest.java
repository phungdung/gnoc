package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.model.ImpactSegmentEntity;
import com.viettel.gnoc.cr.repository.CrGeneralRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.security.PassTranformer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrOutSiteBusinessImpl.class, FileUtils.class,
    I18n.class, DataUtil.class, DateTimeUtils.class, PassProtector.class, Base64.class,
    PassTranformer.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrOutSiteBusinessImplTest {

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(crOutSiteBusiness, "userName",
        "thanhlv12");
    ReflectionTestUtils.setField(crOutSiteBusiness, "password",
        "123456a@");
    ReflectionTestUtils.setField(crOutSiteBusiness, "saltConf",
        "Gman");
    ReflectionTestUtils.setField(crOutSiteBusiness, "uploadFolder",
        "/uploadFolder");
    ReflectionTestUtils.setField(crOutSiteBusiness, "ftpFolder",
        "/ftpFolder");
    ReflectionTestUtils.setField(crOutSiteBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(crOutSiteBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(crOutSiteBusiness, "ftpServer",
        "10.240.232.25");
    ReflectionTestUtils.setField(crOutSiteBusiness, "ftpPort",
        21);
  }

  @InjectMocks
  CrOutSiteBusinessImpl crOutSiteBusiness;

  @Mock
  CrFileAttachBusiness crFileAttachBusiness;

  @Mock
  CrBusiness crBusiness;

  @Mock
  CommonBusiness commonBusiness;

  @Mock
  UserRepository userRepository;

  @Mock
  CrProcessBusiness crProcessBusiness;

  @Mock
  CrGeneralBusiness crGeneralBusiness;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  CrGeneralRepository crGeneralRepository;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  CrRepository crRepository;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Test
  public void testInsertAutoCr_01() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("qqqq");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    List<CrFilesAttachDTO> lstFile = Mockito.spy(ArrayList.class);
    String system = "c";
    String nationCode = "b";
    String lstFtId = "1,2,3";
    String userService = "thanhlv12";
    String passService = "123456a@";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbb");
    ResultDTO rs = Mockito.spy(ResultDTO.class);
    rs.setKey(Constants.CR_RETURN_MESSAGE.ERROR);
    ResultDTO resultDTO = crOutSiteBusiness
        .insertAutoCr(crDTO, lstFile, system, nationCode, lstFtId, userService, passService);
    Assert.assertEquals(rs.getKey(), resultDTO.getKey());
  }

  @Test
  public void testInsertAutoCr_02() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("qqqq");
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    List<CrFilesAttachDTO> lstFile = Mockito.spy(ArrayList.class);
    String system = "";
    String nationCode = "b";
    String lstFtId = "1,2,3";
    String userService = "thanhlv12";
    String passService = "123456a@";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    ResultDTO rs = Mockito.spy(ResultDTO.class);
    rs.setKey(Constants.CR_RETURN_MESSAGE.ERROR);
    String validate = "System xxxx";
    PowerMockito.when(crOutSiteBusiness.validate(crDTO, nationCode, system, lstFtId))
        .thenReturn(validate);
    ResultDTO resultDTO = crOutSiteBusiness
        .insertAutoCr(crDTO, lstFile, system, nationCode, lstFtId, userService, passService);
    Assert.assertEquals(rs.getKey(), resultDTO.getKey());
  }

  @Test
  public void testInsertAutoCr_03() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.mockStatic(Calendar.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("qqqq");
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setCrNumber("1");
    crDTO.setState("12");
    crDTO.setUserCab("ty");

    List<CrFilesAttachDTO> lstFile = Mockito.spy(ArrayList.class);
    String system = "i";
    String nationCode = "b";
    String lstFtId = "1,2,3";
    String userService = "thanhlv12";
    String passService = "123456a@";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    ResultDTO rs = Mockito.spy(ResultDTO.class);
    rs.setKey(Constants.CR_RETURN_MESSAGE.ERROR);

    //set cac gia tri cho ham validate
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUsername("thanhlv12");
    usersEntity.setUserId(1L);
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(usersEntity);
    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("1");
    PowerMockito.when(crRepository.getUserInfo(anyString())).thenReturn(usersDTO);
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);
    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("cc");
    crDTO.setDescription("fg");
    crDTO.setCrProcessId("10");
    crDTO.setSubcategoryId("12");
    crDTO.setPriority("h");
    crDTO.setEarliestStartTime("13/05/2020 01:01:01");
    crDTO.setLatestStartTime("13/05/2020 01:01:01");
    crDTO.setChangeResponsible("cc");
    crDTO.setCountry("281");
    crDTO.setRegion("567");
    UsersDTO udto = Mockito.spy(UsersDTO.class);
    udto.setUserId("12");
    udto.setUnitId("1");
    PowerMockito.when(crRepository.getUserInfo(anyString())).thenReturn(udto);
    Map<String, String> mapConfigProperty = new HashMap<>();
    PowerMockito.when(commonBusiness.getConfigProperty()).thenReturn(mapConfigProperty);
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("3");
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationBusiness
        .searchByConditionBean(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstLocation);
    CrProcessInsideDTO crProcess = Mockito.spy(CrProcessInsideDTO.class);
    crProcess.setCrTypeId(69L);
    crProcess.setRiskLevel(69L);
    crProcess.setImpactSegmentId(69L);
    crProcess.setDeviceTypeId(69L);
    crProcess.setCrProcessId(10L);
    crProcess.setImpactType(69L);
    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong())).thenReturn(crProcess);
    //validateTime
    List<ItemDataCRInside> lstFrame = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setSecondValue("12,13");
    lstFrame.add(itemDataCRInside);
    PowerMockito.when(crGeneralBusiness.getListDutyTypeCBB(any())).thenReturn(lstFrame);
    //==validateTime==//
    //========set cac gia tri cho ham validate
    //validateAndSaveFileAttach
    String validate = "File attach xxxx";
    PowerMockito.when(crOutSiteBusiness.validateAndSaveFileAttach(lstFile, crDTO))
        .thenReturn(validate);
    //===validateAndSaveFileAttach
    ResultDTO resultDTO = crOutSiteBusiness
        .insertAutoCr(crDTO, lstFile, system, nationCode, lstFtId, userService, passService);
    Assert.assertEquals(rs.getKey(), resultDTO.getKey());
  }

  @Test
  public void testActionCloseAutoCr_01() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setCrNumber("1");
    crDTO.setState("12");
    crDTO.setUserCab("ty");
    String userService = "thanhlv12";
    String passService = "123456a@";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbb");
    String result = "xxxx";
    String kq = crOutSiteBusiness.actionCloseAutoCr(crDTO, userService, passService);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testActionCloseAutoCr_02() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    CrDTO crDTO = null;
    String userService = "thanhlv12";
    String passService = "123456a@";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    String result = "crId xxxx";
    String kq = crOutSiteBusiness.actionCloseAutoCr(crDTO, userService, passService);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testActionCloseAutoCr_03() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String result = null;
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setCrNumber("1");
    crDTO.setState("12");
    crDTO.setUserCab("ty");
    String userService = "thanhlv12";
    String passService = "123456a@";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crBusiness.getCrById(anyLong(), any())).thenReturn(crInsiteDTO);
    PowerMockito.when(crBusiness.actionCloseCr(crInsiteDTO, "en_US")).thenReturn(result);
    String kq = crOutSiteBusiness.actionCloseAutoCr(crDTO, userService, passService);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testActionResolveAutoCr_01() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setCrNumber("1");
    crDTO.setState("12");
    crDTO.setUserCab("ty");
    String userService = "thanhlv12";
    String passService = "123456a@";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbb");
    String result = "xxxx";
    String kq = crOutSiteBusiness.actionResolveAutoCr(crDTO, userService, passService);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testActionResolveAutoCr_02() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    CrDTO crDTO = null;
    String userService = "thanhlv12";
    String passService = "123456a@";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    String result = "crId xxxx";
    String kq = crOutSiteBusiness.actionResolveAutoCr(crDTO, userService, passService);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testActionResolveAutoCr_03() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String result = null;
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setCrNumber("1");
    crDTO.setState("12");
    crDTO.setUserCab("ty");
    String userService = "thanhlv12";
    String passService = "123456a@";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setNotes("ghi");
    PowerMockito.when(crBusiness.getCrById(anyLong(), any())).thenReturn(crInsiteDTO);
    PowerMockito.when(crBusiness.actionResolveCr(crInsiteDTO, "en_US")).thenReturn(result);
    String kq = crOutSiteBusiness.actionResolveAutoCr(crDTO, userService, passService);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testGetCrNumber_01() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String userService = "thanhlv12";
    String passService = "123456a@";
    String crProcessId = "1";
    String result = "xxxx";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbb");
    String kq = crOutSiteBusiness.getCrNumber(crProcessId, userService, passService);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testGetCrNumber_02() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String userService = "thanhlv12";
    String passService = "123456a@";
    String crProcessId = "";
    String result = "CrProcessId xxxx";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    List<String> lst = Mockito.spy(ArrayList.class);
    lst.add("c");
    lst.add("g");
    PowerMockito.when(crBusiness.getSequenseCr("CR_SEQ", 1)).thenReturn(lst);
    String kq = crOutSiteBusiness.getCrNumber(crProcessId, userService, passService);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testGetCrNumber_03() throws Exception {
    PowerMockito.mockStatic(PassProtector.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String userService = "thanhlv12";
    String passService = "123456a@";
    String crProcessId = "4";
    String result = "CR_EMERGENCY_VEU_c";
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    List<String> lst = Mockito.spy(ArrayList.class);
    lst.add("c");
    lst.add("g");
    PowerMockito.when(crBusiness.getSequenseCr("CR_SEQ", 1)).thenReturn(lst);

    CrProcessInsideDTO crProcess = Mockito.spy(CrProcessInsideDTO.class);
    crProcess.setCrTypeId(1L);
    crProcess.setImpactSegmentId(1L);
    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong())).thenReturn(crProcess);
    ImpactSegmentEntity segmentDTO = Mockito.spy(ImpactSegmentEntity.class);
    segmentDTO.setImpactSegmentId(1L);
    segmentDTO.setImpactSegmentCode("VEU");
    PowerMockito.when(crGeneralRepository.findImpactSegmentById(anyLong())).thenReturn(segmentDTO);
    String kq = crOutSiteBusiness.getCrNumber(crProcessId, userService, passService);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void validate() {
  }

  @Test
  public void testValidateAndSaveFileAttach() throws Exception {
    String result = "File attach xxxx";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    List<CrFilesAttachDTO> lstFilesAttach = null;
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    String kq = crOutSiteBusiness.validateAndSaveFileAttach(lstFilesAttach, crDTO);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testValidateAndSaveFileAttach_01() throws Exception {
    String result = "File content xxxx";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    List<CrFilesAttachDTO> lstFilesAttach = Mockito.spy(ArrayList.class);
    CrFilesAttachDTO attachDTO = Mockito.spy(CrFilesAttachDTO.class);
    lstFilesAttach.add(attachDTO);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    String kq = crOutSiteBusiness.validateAndSaveFileAttach(lstFilesAttach, crDTO);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testValidateAndSaveFileAttach_02() throws Exception {
    String result = "File type xxxx";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    List<CrFilesAttachDTO> lstFilesAttach = Mockito.spy(ArrayList.class);
    CrFilesAttachDTO attachDTO = Mockito.spy(CrFilesAttachDTO.class);
    attachDTO.setFileContent("thangdt");
    lstFilesAttach.add(attachDTO);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    String kq = crOutSiteBusiness.validateAndSaveFileAttach(lstFilesAttach, crDTO);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testValidateAndSaveFileAttach_03() throws Exception {
    String result = "File name xxxx";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    List<CrFilesAttachDTO> lstFilesAttach = Mockito.spy(ArrayList.class);
    CrFilesAttachDTO attachDTO = Mockito.spy(CrFilesAttachDTO.class);
    attachDTO.setFileContent("thangdt");
    attachDTO.setFileType("ioio");

    lstFilesAttach.add(attachDTO);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    String kq = crOutSiteBusiness.validateAndSaveFileAttach(lstFilesAttach, crDTO);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testValidateAndSaveFileAttach_04() throws Exception {
    String result = "";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    PowerMockito.mockStatic(Base64.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    List<CrFilesAttachDTO> lstFilesAttach = Mockito.spy(ArrayList.class);
    CrFilesAttachDTO attachDTO = Mockito.spy(CrFilesAttachDTO.class);
    attachDTO.setFileContent("thangdt");
    attachDTO.setFileType("ioio");
    attachDTO.setFileName("ghi");
    lstFilesAttach.add(attachDTO);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(Base64.decode(attachDTO.getFileContent())).thenReturn(fileContent);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
        "6cf8a37f3e6993f90eef71859b1ebf31", "/ftpFolder", "ghi", fileContent))
        .thenReturn("duongnt");

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    String kq = crOutSiteBusiness.validateAndSaveFileAttach(lstFilesAttach, crDTO);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testConvertCrFileAttachDTO_01() {
    List<CrFilesAttachDTO> crFilesAttachDTOS = null;
    crOutSiteBusiness.convertCrFileAttachDTO(crFilesAttachDTOS);
    Assert.assertNull(crFilesAttachDTOS);
  }

  @Test
  public void testConvertCrFileAttachDTO_02() {
    List<CrFilesAttachDTO> crFilesAttachDTOS = Mockito.spy(ArrayList.class);
    CrFilesAttachDTO dto = Mockito.spy(CrFilesAttachDTO.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    dto.setFileType("1");
    dto.setFileName("tyu");
    crFilesAttachDTOS.add(dto);
    crFilesAttachDTOS.add(dto);
    List<CrFilesAttachInsiteDTO> lstInsite = crOutSiteBusiness
        .convertCrFileAttachDTO(crFilesAttachDTOS);
    Assert.assertNotNull(lstInsite);
  }


  @Test
  public void testCreateWO_01() throws Exception {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1L");
    crDTO.setChangeOrginator("FIFA");
    crDTO.setTitle("ggggg");
    crDTO.setEarliestStartTime("05/11/2020 01:01:01");
    crDTO.setLatestStartTime("05/11/2020 01:01:01");
    String lstFtId = "12,13,14";
    Map<String, String> mapConfigProperty = new HashMap<>();
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey("SUCCESS");
    resultDTO.setId("3");
    PowerMockito.when(woServiceProxy.createWoProxy(any())).thenReturn(resultDTO);
    PowerMockito.when(commonBusiness.getConfigProperty()).thenReturn(mapConfigProperty);
    String result = crOutSiteBusiness.createWO(crDTO, lstFtId);
    Assert.assertNotNull(result);
  }

  @Test
  public void validateTime() {
  }

  @Test
  public void testValidateDeviceIp_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String nationCode = "HOT";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setRegion("jojo");
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    crImpactedNodesDTO.setType("1");
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);
    String kq = "Ip in LstNetworkNodeId xxxx";
    String result = crOutSiteBusiness.validateDeviceIp(crDTO, nationCode);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testValidateDeviceIp_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String nationCode = "HOT";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setRegion("jojo");
    crDTO.setCrId("10");
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    crImpactedNodesDTO.setType("1");
    crImpactedNodesDTO.setIp("10");
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);
    List<InfraDeviceDTO> lst = Mockito.spy(ArrayList.class);
    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    infraDeviceDTO.setIpId("1");
    infraDeviceDTO.setIp("1");
    infraDeviceDTO.setDeviceId("10");
    infraDeviceDTO.setDeviceCode("KOKO");
    infraDeviceDTO.setDeviceName("CC");
    lst.add(infraDeviceDTO);
    PowerMockito.when(commonStreamServiceProxy.getListInfraDeviceIpV2(any())).thenReturn(lst);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    crAffectedNodesDTO.setDeviceName("hi");
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);

    String kq = "Ip in LstNetworkNodeIdAffected xxxx";
    String result = crOutSiteBusiness.validateDeviceIp(crDTO, nationCode);
    Assert.assertNotNull(result);
  }

  @Test
  public void testValidateDeviceIp_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String nationCode = "HOT";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setRegion("jojo");
    crDTO.setCrId("10");

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    crImpactedNodesDTO.setType("1");
    crImpactedNodesDTO.setIp("10");
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);
    List<InfraDeviceDTO> lst = Mockito.spy(ArrayList.class);
    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    infraDeviceDTO.setIpId("1");
    infraDeviceDTO.setIp("1");
    infraDeviceDTO.setDeviceId("10");
    infraDeviceDTO.setDeviceCode("KOKO");
    infraDeviceDTO.setDeviceName("CC");
    lst.add(infraDeviceDTO);
    PowerMockito.when(commonStreamServiceProxy.getListInfraDeviceIpV2(any())).thenReturn(lst);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    crAffectedNodesDTO.setDeviceName("hi");
    crAffectedNodesDTO.setIp("ioio");
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    List<InfraDeviceDTO> infraDeviceDTOList = Mockito.spy(ArrayList.class);
    infraDeviceDTOList.add(infraDeviceDTO);
    PowerMockito.when(commonStreamServiceProxy.getListInfraDeviceIpV2(any()))
        .thenReturn(infraDeviceDTOList);
    String kq = "";
    String result = crOutSiteBusiness.validateDeviceIp(crDTO, nationCode);
    Assert.assertEquals(result, kq);
  }
}
