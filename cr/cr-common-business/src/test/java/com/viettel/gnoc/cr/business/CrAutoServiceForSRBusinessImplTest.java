
package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.aam.MopInfo;
import com.viettel.aam.MopResult;
import com.viettel.aam.TdttWebservice;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.CR_TYPE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.repository.CrAlarmRepository;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.ws.provider.WSTDTTPort;
import com.viettel.gnoc.ws.provider.WSVipaDdPort;
import com.viettel.gnoc.ws.provider.WSVipaIpPort;
import com.viettel.security.PassTranformer;
import com.viettel.vmsa.MopDetailDTO;
import com.viettel.vmsa.MopDetailOutputDTO;
import com.viettel.vmsa.NodeDTO;
import java.util.ArrayList;
import java.util.Date;
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
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrAutoServiceForSRBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, TicketProvider.class, DataUtil.class, CommonExport.class, DateTimeUtils.class,
    PassTranformer.class, Base64.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrAutoServiceForSRBusinessImplTest {

  @InjectMocks
  CrAutoServiceForSRBusinessImpl crAutoServiceForSRBusiness;
  @Mock
  CrBusiness crBusiness;
  @Mock
  CrProcessBusiness crProcessBusiness;
  @Mock
  CrGeneralBusiness crGeneralBusiness;
  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;
  @Mock
  CommonRepository commonRepository;
  @Mock
  CatLocationBusiness catLocationBusiness;
  @Mock
  CrApprovalDepartmentBusiness crApprovalDepartmentBusiness;
  @Mock
  CrFileAttachBusiness crFileAttachBusiness;
  @Mock
  CrCategoryServiceProxy crCategoryServiceProxy;
  @Mock
  WSVipaIpPort wSVipaPort;
  @Mock
  WSTDTTPort wSTDTTPort;
  @Mock
  WSVipaDdPort wsVipaDdPort;
  @Mock
  WoServiceProxy woServiceProxy;
  @Mock
  TdttWebservice port;
  @Mock
  CrAlarmRepository crAlarmRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(crAutoServiceForSRBusiness, "uploadFolder",
        "/uploadFolder");
    ReflectionTestUtils.setField(crAutoServiceForSRBusiness, "ftpFolder",
        "/ftpFolder");
    ReflectionTestUtils.setField(crAutoServiceForSRBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(crAutoServiceForSRBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(crAutoServiceForSRBusiness, "ftpServer",
        "10.240.232.25");
    ReflectionTestUtils.setField(crAutoServiceForSRBusiness, "ftpPort",
        21);
  }

  @Test
  public void getCrNumber_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("EMERGENCY");
    List<String> lst = Mockito.spy(ArrayList.class);
    lst.add(RESULT.SUCCESS);
    PowerMockito.when(crBusiness.getSequenseCr(any(), any())).thenReturn(lst);
    CrProcessInsideDTO crProcess = Mockito.spy(CrProcessInsideDTO.class);
    crProcess.setCrTypeId(1L);
    crProcess.setImpactSegmentId(1L);
    PowerMockito.when(crProcessBusiness.findCrProcessById(any())).thenReturn(crProcess);
    ImpactSegmentDTO segmentDTO = Mockito.spy(ImpactSegmentDTO.class);
    segmentDTO.setImpactSegmentCode("123");
    PowerMockito.when(crCategoryServiceProxy.findById(any())).thenReturn(segmentDTO);
    String res = crAutoServiceForSRBusiness.getCrNumber("1");
    Assert.assertEquals(res, "CR_EMERGENCY_123_SUCCESS");
  }

  @Test
  public void testValidateAndSaveFileAttach_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("null");
    List<CrFilesAttachDTO> lstFilesAttach = Mockito.spy(ArrayList.class);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    String str = "File attach " + "null";
    String result = crAutoServiceForSRBusiness.validateAndSaveFileAttach(lstFilesAttach, crDTO);
    Assert.assertEquals(str, result);
  }

  @Test
  public void testValidateAndSaveFileAttach_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("null");
    CrFilesAttachDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachDTO.class);
    crFilesAttachDTO.setColName("ccc");

    List<CrFilesAttachDTO> lstFilesAttach = Mockito.spy(ArrayList.class);
    lstFilesAttach.add(crFilesAttachDTO);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setTitle("okok");
    crDTO.setState("hihi");
    String result = crAutoServiceForSRBusiness.validateAndSaveFileAttach(lstFilesAttach, crDTO);
    Assert.assertNotNull(result);
  }

  @Test
  public void testValidateAndSaveFileAttach_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("null");
    CrFilesAttachDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachDTO.class);
    crFilesAttachDTO.setColName("ccc");
    crFilesAttachDTO.setFileContent("ggggggg");

    List<CrFilesAttachDTO> lstFilesAttach = Mockito.spy(ArrayList.class);
    lstFilesAttach.add(crFilesAttachDTO);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setTitle("okok");
    crDTO.setState("hihi");
    String result = crAutoServiceForSRBusiness.validateAndSaveFileAttach(lstFilesAttach, crDTO);
    Assert.assertNotNull(result);
  }

  @Test
  public void testValidateAndSaveFileAttach_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("null");
    CrFilesAttachDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachDTO.class);
    crFilesAttachDTO.setColName("ccc");
    crFilesAttachDTO.setFileContent("ggggggg");
    crFilesAttachDTO.setFileType("nolnol");

    List<CrFilesAttachDTO> lstFilesAttach = Mockito.spy(ArrayList.class);
    lstFilesAttach.add(crFilesAttachDTO);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setTitle("okok");
    crDTO.setState("hihi");
    String result = crAutoServiceForSRBusiness.validateAndSaveFileAttach(lstFilesAttach, crDTO);
    Assert.assertNotNull(result);
  }

  @Test
  public void testValidateAndSaveFileAttach_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(Base64.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("null");
    CrFilesAttachDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachDTO.class);
    crFilesAttachDTO.setColName("ccc");
    crFilesAttachDTO.setFileName("abc");
    crFilesAttachDTO.setFileContent("ggggggg");
    crFilesAttachDTO.setFileType("nolnol");
    crFilesAttachDTO.setFileName("ert");
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }

    List<CrFilesAttachDTO> lstFilesAttach = Mockito.spy(ArrayList.class);
    lstFilesAttach.add(crFilesAttachDTO);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setTitle("okok");
    crDTO.setState("hihi");
    PowerMockito.when(Base64.decode(crFilesAttachDTO.getFileContent())).thenReturn(fileContent);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
        "6cf8a37f3e6993f90eef71859b1ebf31", "/ftpFolder", "ert", fileContent, new Date()))
        .thenReturn("FullPath");
    PowerMockito.when(FileUtils.saveUploadFile("ert", fileContent, "/uploadFolder", new Date()))
        .thenReturn("fullPathOld");
    String kq = "";
    String result = crAutoServiceForSRBusiness.validateAndSaveFileAttach(lstFilesAttach, crDTO);
    Assert.assertEquals(result, kq);
  }

  @Test
  public void testGetListFileFromMop_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("69");
    crDTO.setChangeOrginator("bbbb");
    List<String> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add("AAM");
    List<String> lstNodeIp = Mockito.spy(ArrayList.class);
    lstNodeIp.add("11");
    lstNodeIp.add("12");
    try {
      List<CrFilesAttachDTO> list = crAutoServiceForSRBusiness
          .getListFileFromMop(crDTO, "AAM", "12", lstMop, lstNodeIp);
      Assert.assertNotNull(list);
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testGetListFileFromMop_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("69");
    crDTO.setChangeOrginator("bbbb");
    List<String> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add("AAM");
    List<String> lstNodeIp = Mockito.spy(ArrayList.class);
    lstNodeIp.add("11");
    lstNodeIp.add("12");
    List<MopInfo> infoList = Mockito.spy(ArrayList.class);
    MopInfo mopInfo = Mockito.spy(MopInfo.class);
    mopInfo.setCode("AAM");
    infoList.add(mopInfo);
    MopResult mopResult = Mockito.spy(MopResult.class);
    mopResult.setStatus(1);
    mopResult.setMessage("veu veu");
    PowerMockito.when(mopResult.getMopInfos()).thenReturn(infoList);
    PowerMockito.when(wSTDTTPort.getMopInfo("AAM")).thenReturn(mopResult);
    try {
      List<CrFilesAttachDTO> list = crAutoServiceForSRBusiness
          .getListFileFromMop(crDTO, "AAM", "12", lstMop, lstNodeIp);
      Assert.assertNotNull(list);
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testGetListFileFromMop_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("69");
    crDTO.setChangeOrginator("bbbb");
    List<String> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add("AAM");
    List<String> lstNodeIp = Mockito.spy(ArrayList.class);
//    lstNodeIp.add("11");
//    lstNodeIp.add("12");
    List<MopInfo> infoList = Mockito.spy(ArrayList.class);
    MopInfo mopInfo = Mockito.spy(MopInfo.class);
    mopInfo.setCode("AAM");
    mopInfo.setMopFile("df");
    mopInfo.setMopFileContent("zlatan Ibrahimovic");
    mopInfo.setNationCode("kokoooo");
    List<String> lstIpImpact = Mockito.spy(ArrayList.class);
    lstIpImpact.add("R");
    lstIpImpact.add("L");
    PowerMockito.when(mopInfo.getIps()).thenReturn(lstIpImpact);
    infoList.add(mopInfo);
    MopResult mopResult = Mockito.spy(MopResult.class);
    mopResult.setStatus(1);
    mopResult.setMessage("veu veu");
    PowerMockito.when(mopResult.getMopInfos()).thenReturn(infoList);
    PowerMockito.when(wSTDTTPort.getMopInfo("AAM")).thenReturn(mopResult);
    InfraDeviceDTO deviceDTO = Mockito.spy(InfraDeviceDTO.class);
    deviceDTO.setIp("34");
    deviceDTO.setIpId("66");
    deviceDTO.setDeviceId("9");
    deviceDTO.setDeviceName("THU");
    deviceDTO.setDeviceCode("THUY");
    List<InfraDeviceDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(deviceDTO);
    PowerMockito.when(commonStreamServiceProxy.getListInfraDeviceIpV2(any())).thenReturn(lst);

    try {
      List<CrFilesAttachDTO> list = crAutoServiceForSRBusiness
          .getListFileFromMop(crDTO, "AAM", "12", lstMop, lstNodeIp);
      Assert.assertNotNull(list);
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testGetListFileFromMop_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("69");
    crDTO.setChangeOrginator("bbbb");
    List<String> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add("VMSA");
    List<String> lstNodeIp = Mockito.spy(ArrayList.class);
    lstNodeIp.add("11");
    lstNodeIp.add("12");
    List<MopInfo> infoList = Mockito.spy(ArrayList.class);
    MopInfo mopInfo = Mockito.spy(MopInfo.class);
    mopInfo.setCode("VMSA");
    mopInfo.setMopFile("df");
    mopInfo.setMopFileContent("zlatan Ibrahimovic");
    mopInfo.setNationCode("kokoooo");
    List<String> lstIpImpact = Mockito.spy(ArrayList.class);
    lstIpImpact.add("R");
    lstIpImpact.add("L");
    PowerMockito.when(mopInfo.getIps()).thenReturn(lstIpImpact);
    infoList.add(mopInfo);
    MopDetailOutputDTO vipaDdMop = Mockito.spy(MopDetailOutputDTO.class);
    vipaDdMop.setResultMessage("cvbmnmk");
    PowerMockito.when(wsVipaDdPort.getMopInfo("VMSA")).thenReturn(vipaDdMop);
    try {
      List<CrFilesAttachDTO> list = crAutoServiceForSRBusiness
          .getListFileFromMop(crDTO, "VMSA", "12", lstMop, lstNodeIp);
      Assert.assertNotNull(list);
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testGetListFileFromMop_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("69");
    crDTO.setChangeOrginator("bbbb");
    List<String> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add("VMSA");
    List<String> lstNodeIp = Mockito.spy(ArrayList.class);
//    lstNodeIp.add("11");
//    lstNodeIp.add("12");
    List<MopInfo> infoList = Mockito.spy(ArrayList.class);
    MopInfo mopInfo = Mockito.spy(MopInfo.class);
    mopInfo.setCode("VMSA");
    mopInfo.setMopFile("df");
    mopInfo.setMopFileContent("zlatan Ibrahimovic");
    mopInfo.setNationCode("kokoooo");
    List<String> lstIpImpact = Mockito.spy(ArrayList.class);
    lstIpImpact.add("R");
    lstIpImpact.add("L");
    PowerMockito.when(mopInfo.getIps()).thenReturn(lstIpImpact);
    infoList.add(mopInfo);
    MopDetailOutputDTO vipaDdMop = Mockito.spy(MopDetailOutputDTO.class);
    vipaDdMop.setResultMessage("cvbmnmk");
    MopDetailDTO mopDetailDTO = Mockito.spy(MopDetailDTO.class);
    mopDetailDTO.setKpiFileName("lollol");
    vipaDdMop.setMopDetailDTO(mopDetailDTO);
    List<NodeDTO> lstNode = Mockito.spy(ArrayList.class);
    NodeDTO nodeDTO = Mockito.spy(NodeDTO.class);
    nodeDTO.setNodeIp("rtrt");
    lstNode.add(nodeDTO);
    PowerMockito.when(vipaDdMop.getMopDetailDTO().getNodes()).thenReturn(lstNode);
    PowerMockito.when(wsVipaDdPort.getMopInfo("VMSA")).thenReturn(vipaDdMop);
    InfraDeviceDTO deviceDTO = Mockito.spy(InfraDeviceDTO.class);
    deviceDTO.setIp("34");
    deviceDTO.setIpId("66");
    deviceDTO.setDeviceId("9");
    deviceDTO.setDeviceName("THU");
    deviceDTO.setDeviceCode("THUY");
    List<InfraDeviceDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(deviceDTO);
    PowerMockito.when(commonStreamServiceProxy.getListInfraDeviceIpV2(any())).thenReturn(lst);
    try {
      List<CrFilesAttachDTO> list = crAutoServiceForSRBusiness
          .getListFileFromMop(crDTO, "VMSA", "12", lstMop, lstNodeIp);
      Assert.assertNotNull(list);
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testGetListFileFromMop_06() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("69");
    crDTO.setChangeOrginator("bbbb");
    List<String> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add("VMSA");
    List<String> lstNodeIp = Mockito.spy(ArrayList.class);
//    lstNodeIp.add("11");
//    lstNodeIp.add("12");
    List<MopInfo> infoList = Mockito.spy(ArrayList.class);
    MopInfo mopInfo = Mockito.spy(MopInfo.class);
    mopInfo.setCode("VMSA");
    mopInfo.setMopFile("df");
    mopInfo.setMopFileContent("zlatan Ibrahimovic");
    mopInfo.setNationCode("kokoooo");
    List<String> lstIpImpact = Mockito.spy(ArrayList.class);
    lstIpImpact.add("R");
    lstIpImpact.add("L");
    PowerMockito.when(mopInfo.getIps()).thenReturn(lstIpImpact);
    infoList.add(mopInfo);
    MopDetailOutputDTO vipaDdMop = null;
    InfraDeviceDTO deviceDTO = Mockito.spy(InfraDeviceDTO.class);
    deviceDTO.setIp("34");
    deviceDTO.setIpId("66");
    deviceDTO.setDeviceId("9");
    deviceDTO.setDeviceName("THU");
    deviceDTO.setDeviceCode("THUY");
    List<InfraDeviceDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(deviceDTO);
    PowerMockito.when(commonStreamServiceProxy.getListInfraDeviceIpV2(any())).thenReturn(lst);
    PowerMockito.when(wsVipaDdPort.getMopInfo("VMSA")).thenReturn(vipaDdMop);
    try {
      List<CrFilesAttachDTO> list = crAutoServiceForSRBusiness
          .getListFileFromMop(crDTO, "VMSA", "12", lstMop, lstNodeIp);
      Assert.assertNotNull(list);
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testGetListFileFromMop_07() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("69");
    crDTO.setChangeOrginator("bbbb");
    List<String> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add("MUMU");
    List<String> lstNodeIp = Mockito.spy(ArrayList.class);
    lstNodeIp.add("11");
    lstNodeIp.add("12");
    List<MopInfo> infoList = Mockito.spy(ArrayList.class);
    MopInfo mopInfo = Mockito.spy(MopInfo.class);
    mopInfo.setCode("MUMU");
    mopInfo.setMopFile("df");
    mopInfo.setMopFileContent("zlatan Ibrahimovic");
    mopInfo.setNationCode("kokoooo");
    List<String> lstIpImpact = Mockito.spy(ArrayList.class);
    lstIpImpact.add("R");
    lstIpImpact.add("L");
    PowerMockito.when(mopInfo.getIps()).thenReturn(lstIpImpact);
    infoList.add(mopInfo);

    com.viettel.vipa.MopDetailOutputDTO vipaIpMop = Mockito
        .spy(com.viettel.vipa.MopDetailOutputDTO.class);
    vipaIpMop.setResultCode(1111);
    com.viettel.vipa.MopDetailDTO mopDetailDTO = Mockito.spy(com.viettel.vipa.MopDetailDTO.class);
    mopDetailDTO.setKpiFileName("lollol");
    vipaIpMop.setMopDetailDTO(mopDetailDTO);
    List<com.viettel.vipa.NodeDTO> lstNode = Mockito.spy(ArrayList.class);
    com.viettel.vipa.NodeDTO nodeDTO = Mockito.spy(com.viettel.vipa.NodeDTO.class);
    nodeDTO.setNodeIp("rtrt");
    lstNode.add(nodeDTO);

    PowerMockito.when(vipaIpMop.getMopDetailDTO().getNationCode()).thenReturn("gggg");
    PowerMockito.when(vipaIpMop.getMopDetailDTO().getNodes()).thenReturn(lstNode);
    PowerMockito.when(wSVipaPort.getMopInfo(anyString())).thenReturn(vipaIpMop);
    try {
      List<CrFilesAttachDTO> list = crAutoServiceForSRBusiness
          .getListFileFromMop(crDTO, "MUMU", "12", lstMop, lstNodeIp);
      Assert.assertNotNull(list);
    } catch (Exception e) {
      e.getMessage();
    }
  }

  @Test
  public void testGetListFileFromMop_08() throws Exception {
    PowerMockito.mockStatic(Base64.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("69");
    crDTO.setChangeOrginator("bbbb");
    List<String> lstMop = Mockito.spy(ArrayList.class);
    lstMop.add("MUMU");
    List<String> lstNodeIp = Mockito.spy(ArrayList.class);
    List<MopInfo> infoList = Mockito.spy(ArrayList.class);
    MopInfo mopInfo = Mockito.spy(MopInfo.class);
    mopInfo.setCode("MUMU");
    mopInfo.setMopFile("df");
    mopInfo.setMopFileContent("zlatan Ibrahimovic");
    mopInfo.setNationCode("kokoooo");
    List<String> lstIpImpact = Mockito.spy(ArrayList.class);
    lstIpImpact.add("R");
    lstIpImpact.add("L");
    infoList.add(mopInfo);

    com.viettel.vipa.MopDetailOutputDTO vipaIpMop = Mockito
        .spy(com.viettel.vipa.MopDetailOutputDTO.class);
    vipaIpMop.setResultCode(0);
    com.viettel.vipa.MopDetailDTO mopDetailDTO = Mockito.spy(com.viettel.vipa.MopDetailDTO.class);
    mopDetailDTO.setKpiFileName("lollol");
    mopDetailDTO.setMopFileName("rtrt");
    mopDetailDTO.setMopFileContent("yyyyy");
    mopDetailDTO.setKpiFileContent("popopopopopopopo");

    List<com.viettel.vipa.NodeDTO> nodes = Mockito.spy(ArrayList.class);
    com.viettel.vipa.NodeDTO nodeItem1 = Mockito.spy(com.viettel.vipa.NodeDTO.class);
    nodeItem1.setNodeIp("1.1.1.1");
    nodes.add(nodeItem1);
    mopDetailDTO.getNodes().addAll(nodes);
    vipaIpMop.setMopDetailDTO(mopDetailDTO);
    List<com.viettel.vipa.NodeDTO> lstNode = Mockito.spy(ArrayList.class);
    com.viettel.vipa.NodeDTO nodeDTO = Mockito.spy(com.viettel.vipa.NodeDTO.class);
    nodeDTO.setNodeIp("1.1.1.1");
    lstNode.add(nodeDTO);

    PowerMockito.when(vipaIpMop.getMopDetailDTO().getNationCode()).thenReturn("gggg");
    PowerMockito.when(vipaIpMop.getMopDetailDTO().getNodes()).thenReturn(lstNode);
    PowerMockito.when(wSVipaPort.getMopInfo(anyString())).thenReturn(vipaIpMop);
    InfraDeviceDTO deviceDTO = Mockito.spy(InfraDeviceDTO.class);
    deviceDTO.setIp("1.1.1.1");
    deviceDTO.setIpId("66");
    deviceDTO.setDeviceId("9");
    deviceDTO.setDeviceName("THU");
    deviceDTO.setDeviceCode("THUY");
    lstNodeIp.add("66");
    deviceDTO.setLstIps(lstNodeIp);
    List<InfraDeviceDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(deviceDTO);
    PowerMockito.when(commonStreamServiceProxy.getListInfraDeviceIpV2SrProxy(any())).thenReturn(lst);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(Base64.decode(mopDetailDTO.getMopFileContent())).thenReturn(fileContent);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
        "6cf8a37f3e6993f90eef71859b1ebf31", "/ftpFolder", "rtrt", fileContent, new Date()))
        .thenReturn("fullPath");
    PowerMockito.when(FileUtils.saveUploadFile("rtrt", fileContent, "/uploadFolder", new Date()))
        .thenReturn("fullPathOld");

    List<CrFilesAttachDTO> list = crAutoServiceForSRBusiness
        .getListFileFromMop(crDTO, "MUMU", "12", lstMop, lstNodeIp);
//    Assert.assertNotNull(list);
  }

  @Test
  public void testValidate_01() throws Exception {
    String nationCode = "";
    String system = "";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("1");

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "System xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_02() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("1");
    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "CrId xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_03() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("1");
    crDTO.setCrId("69");

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    crTemp.setCrNumber("456");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "CrId xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_05() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("1");
    crDTO.setCrId("69");
    String crNumber = "";
    for (int i = 0; i <= 202; i++) {
      crNumber += "abc";
    }
    crDTO.setCrNumber(crNumber);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "CrNumber xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_06() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "State xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_07() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "UserCab xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_08() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    UsersDTO usersDTO = null;
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxxx");
    String test = "UserCab xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_09() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    for (int i = 1; i <= 1002; i++) {
      lstNetworkNodeId.add(crImpactedNodesDTO);
    }
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "xxxx1000";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_10() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    for (int i = 1; i <= 4; i++) {
      lstNetworkNodeId.add(crImpactedNodesDTO);
    }
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    for (int i = 1; i <= 1002; i++) {
      lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    }
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "xxxx1000";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_11() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    for (int i = 1; i <= 4; i++) {
      lstNetworkNodeId.add(crImpactedNodesDTO);
    }
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    for (int i = 1; i <= 4; i++) {
      lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    }
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "Title xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_12() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);

    String title = "";
    for (int i = 1; i <= 259; i++) {
      title += "park";
    }
    crDTO.setTitle(title);

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "Title xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_13() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("ooooo");

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "Description xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_14() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("oooo");
    String description = "";
    for (int i = 1; i <= 2009; i++) {
      description += "park";
    }
    crDTO.setDescription(description);

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "Description xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_15() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("oooo");
    crDTO.setDescription("iiiii");

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "CrProcessId xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_16() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    crDTO.setCrProcessId("88");
    CrProcessInsideDTO crProcess = null;
    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong())).thenReturn(crProcess);

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("oooo");
    crDTO.setDescription("iiiii");

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "CrProcessId xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_18() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    crDTO.setSubcategoryId("87");
    crDTO.setCrProcessId("88");
    CrProcessInsideDTO crProcess = Mockito.spy(CrProcessInsideDTO.class);
    crProcess.setCrProcessId(88L);
    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong())).thenReturn(crProcess);

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("oooo");
    crDTO.setDescription("iiiii");

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "Priority xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_19() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    crDTO.setSubcategoryId("87");
    crDTO.setCrProcessId("88");
    crDTO.setPriority("a");
    CrProcessInsideDTO crProcess = Mockito.spy(CrProcessInsideDTO.class);
    crProcess.setCrProcessId(88L);
    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong())).thenReturn(crProcess);

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("oooo");
    crDTO.setDescription("iiiii");

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "ServiceAffecting xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_20() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    crDTO.setSubcategoryId("87");
    crDTO.setCrProcessId("88");
    crDTO.setPriority("a");
    crDTO.setServiceAffecting("1");
    List<CrAffectedServiceDetailsDTO> lstAffectedService = null;
    crDTO.setLstAffectedService(lstAffectedService);

    CrProcessInsideDTO crProcess = Mockito.spy(CrProcessInsideDTO.class);
    crProcess.setCrProcessId(88L);
    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong())).thenReturn(crProcess);

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("oooo");
    crDTO.setDescription("iiiii");

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "LstAffectedService xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_21() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    crDTO.setSubcategoryId("87");
    crDTO.setCrProcessId("88");
    crDTO.setPriority("a");
    crDTO.setServiceAffecting("1");
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    CrAffectedServiceDetailsDTO crAffectedServiceDetailsDTO = Mockito
        .spy(CrAffectedServiceDetailsDTO.class);
    lstAffectedService.add(crAffectedServiceDetailsDTO);
    crDTO.setLstAffectedService(lstAffectedService);

    CrProcessInsideDTO crProcess = Mockito.spy(CrProcessInsideDTO.class);
    crProcess.setCrProcessId(88L);
    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong())).thenReturn(crProcess);

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("oooo");
    crDTO.setDescription("iiiii");

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "ImpactAffect xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_22() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    crDTO.setSubcategoryId("87");
    crDTO.setCrProcessId("88");
    crDTO.setPriority("a");
    crDTO.setServiceAffecting("1");
    crDTO.setImpactAffect("tuti");
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    CrAffectedServiceDetailsDTO crAffectedServiceDetailsDTO = Mockito
        .spy(CrAffectedServiceDetailsDTO.class);
    lstAffectedService.add(crAffectedServiceDetailsDTO);
    crDTO.setLstAffectedService(lstAffectedService);

    CrProcessInsideDTO crProcess = Mockito.spy(CrProcessInsideDTO.class);
    crProcess.setCrProcessId(88L);
    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong())).thenReturn(crProcess);

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("oooo");
    crDTO.setDescription("iiiii");

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "TotalAffectedCustomers xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_24() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    crDTO.setSubcategoryId("87");
    crDTO.setCrProcessId("88");
    crDTO.setPriority("a");
    crDTO.setServiceAffecting("1");
    crDTO.setImpactAffect("tuti");
    crDTO.setTotalAffectedCustomers("55");
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    CrAffectedServiceDetailsDTO crAffectedServiceDetailsDTO = Mockito
        .spy(CrAffectedServiceDetailsDTO.class);
    lstAffectedService.add(crAffectedServiceDetailsDTO);
    crDTO.setLstAffectedService(lstAffectedService);

    CrProcessInsideDTO crProcess = Mockito.spy(CrProcessInsideDTO.class);
    crProcess.setCrProcessId(88L);
    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong())).thenReturn(crProcess);

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("oooo");
    crDTO.setDescription("iiiii");

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("QWE");
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "TotalAffectedMinutes xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_25() throws Exception {
    String nationCode = "";
    String system = "CCCC";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setChangeOrginator("LOL");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("abcg");
    crDTO.setUserCab("thanhlv12");
    crDTO.setSubcategoryId("87");
    crDTO.setCrProcessId("88");
    crDTO.setPriority("a");
    crDTO.setServiceAffecting("1");
    crDTO.setImpactAffect("tuti");
    crDTO.setTotalAffectedCustomers("55");
    crDTO.setTotalAffectedMinutes("55");
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    CrAffectedServiceDetailsDTO crAffectedServiceDetailsDTO = Mockito
        .spy(CrAffectedServiceDetailsDTO.class);
    lstAffectedService.add(crAffectedServiceDetailsDTO);
    crDTO.setLstAffectedService(lstAffectedService);

    CrProcessInsideDTO crProcess = Mockito.spy(CrProcessInsideDTO.class);
    crProcess.setCrProcessId(88L);
    crProcess.setCrTypeId(1L);
    crProcess.setRiskLevel(2L);
    crProcess.setImpactSegmentId(2L);
    crProcess.setDeviceTypeId(3L);
    crProcess.setImpactType(6L);

    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong())).thenReturn(crProcess);

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setTitle("oooo");
    crDTO.setDescription("iiiii");

    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("89");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("ghi");
    crTemp.setCrId("69");
    PowerMockito.when(crBusiness.getCrById(69L, null)).thenReturn(crTemp);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setFileName("mlml");
    lstWo.add(woDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxxx");
    String test = "EarliestStartTime xxxx";
    String result = crAutoServiceForSRBusiness.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, test);
  }

  @Test
  public void testValidate_26() throws Exception {
    String nationCode = "";
    String system = "AAM";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    prepareValidate(crDTO, "1");
    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setFileName("fileName");
    lstWo.add(woDTO);
    CrAutoServiceForSRBusinessImpl crAutoServiceForSRBusinessChild = Mockito.spy(crAutoServiceForSRBusiness);
    PowerMockito.doReturn(StringUtils.BLANK_STRING_VALUE).when(crAutoServiceForSRBusinessChild).validateDeviceIp(anyObject(), anyString());
    String result = crAutoServiceForSRBusinessChild.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(StringUtils.BLANK_STRING_VALUE, result);
  }

  @Test
  public void testValidate_27() throws Exception {
    String nationCode = "";
    String system = "AAM";
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    prepareValidate(crDTO, "2");
    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setFileName("fileName");
    lstWo.add(woDTO);
    CrAutoServiceForSRBusinessImpl crAutoServiceForSRBusinessChild = Mockito.spy(crAutoServiceForSRBusiness);
    PowerMockito.doReturn(StringUtils.BLANK_STRING_VALUE).when(crAutoServiceForSRBusinessChild).validateDeviceIp(anyObject(), anyString());
    String result = crAutoServiceForSRBusinessChild.validate(crDTO, nationCode, system, lstWo);
    Assert.assertEquals(result, StringUtils.BLANK_STRING_VALUE);
  }

  @Test
  public void testCreateWO_01() throws Exception {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("10");
    crDTO.setChangeOrginator("KO");
    ResultDTO reuslt = Mockito.spy(ResultDTO.class);
    reuslt.setId("1");
    reuslt.setKey(RESULT.SUCCESS);

    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("CCCC");
    lstWo.add(woDTO);
    PowerMockito.when(woServiceProxy.createWoProxy(any())).thenReturn(reuslt);
    crAutoServiceForSRBusiness.createWO(crDTO, lstWo);
    Assert.assertNotNull(lstWo);
  }

  @Test
  public void testValidateDeviceIp_01() throws Exception {
    String nationCode = "XXX";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("OKOK");
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO impactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    impactedNodesDTO.setIp("2");
    lstNetworkNodeId.add(impactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);
    crDTO.setCrId("10");

    List<InfraDeviceDTO> lst = Mockito.spy(ArrayList.class);
    InfraDeviceDTO deviceDTO = Mockito.spy(InfraDeviceDTO.class);
    deviceDTO.setDeviceCode("abcdefgh");
    deviceDTO.setIpId("10");
    deviceDTO.setIp("11");
    deviceDTO.setDeviceId("12");
    deviceDTO.setDeviceName("GHI");
    lst.add(deviceDTO);
    PowerMockito.when(commonStreamServiceProxy.getListInfraDeviceIpV2SrProxy(any())).thenReturn(lst);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO affectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    affectedNodesDTO.setIp("11");
    lstNetworkNodeIdAffected.add(affectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);

    String result1 = "";
    String result2 = crAutoServiceForSRBusiness.validateDeviceIp(crDTO, nationCode);
    Assert.assertEquals(result1, result2);
  }

  @Test
  public void testValidateDeviceIp_02() throws Exception {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setLstNetworkNodeId(null);
    String result1 = "";
    String result2 = crAutoServiceForSRBusiness.validateDeviceIp(crDTO, "");
    Assert.assertEquals(result1, result2);
  }

  @Test
  public void testValidateTime_01() throws Exception {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setEarliestStartTime("12/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setLatestStartTime("12/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setDisturbanceStartTime("12/05/2020 10:30");
    crDTO.setDisturbanceEndTime("12/05/2020 10:30");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxx");
    String rs = "EarliestStartTime xxx xxx";
    String result1 = crAutoServiceForSRBusiness.validateTime(crDTO, 1L);
    Assert.assertEquals(result1, rs);
  }

  @Test
  public void testValidateTime_02() throws Exception {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setEarliestStartTime("20/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setLatestStartTime("12/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setDisturbanceStartTime("12/05/2020 10:30");
    crDTO.setDisturbanceEndTime("12/05/2020 10:30");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxx");
    String rs = "LatestStartTime xxx EarliestStartTime";
    String result1 = crAutoServiceForSRBusiness.validateTime(crDTO, 1L);
    Assert.assertNotNull(result1);
  }

  @Test
  public void testValidateTime_03() throws Exception {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setEarliestStartTime("20/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setLatestStartTime("30/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setDisturbanceStartTime("12/05/2020 10:30");
    crDTO.setDisturbanceEndTime("16/05/2020 10:30");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxx");
    String rs = "DisturbanceStartTime xxx EarliestStartTime";
    String result1 = crAutoServiceForSRBusiness.validateTime(crDTO, 1L);
    Assert.assertNotNull(result1);
  }

  @Test
  public void testValidateTime_04() throws Exception {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setEarliestStartTime("20/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setLatestStartTime("29/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setDisturbanceStartTime("21/05/2020 10:30");
    crDTO.setDisturbanceEndTime("30/05/2020 10:30");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxx");
    String rs = "LatestStartTime xxx DisturbanceEndTime";
    String result1 = crAutoServiceForSRBusiness.validateTime(crDTO, 1L);
    Assert.assertNotNull(result1);
  }

  @Test
  public void testValidateTime_05() throws Exception {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setEarliestStartTime("20/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setLatestStartTime("29/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setDisturbanceStartTime("21/05/2020 10:30");
    crDTO.setDisturbanceEndTime("27/05/2020 10:30");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxx");
    ItemDataCRInside dataCR = Mockito.spy(ItemDataCRInside.class);
    dataCR.setSecondValue("1,2");
    List<ItemDataCRInside> lstFrame = Mockito.spy(ArrayList.class);
    lstFrame.add(dataCR);
    PowerMockito.when(crGeneralBusiness.getListDutyTypeCBB(any())).thenReturn(lstFrame);
    String rs = "xxx";
    String result1 = crAutoServiceForSRBusiness.validateTime(crDTO, 1L);
    Assert.assertNotNull(result1);
  }

  @Test
  public void testValidateTime_06() throws Exception {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setEarliestStartTime("20/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setLatestStartTime("29/05/2020 10:30");  //"dd/MM/yyyy HH:mm"
    crDTO.setDisturbanceStartTime("21/05/2020 10:30");
    crDTO.setDisturbanceEndTime("27/05/2020 10:30");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxx");
    ItemDataCRInside dataCR = Mockito.spy(ItemDataCRInside.class);
    dataCR.setSecondValue("3,1");
    List<ItemDataCRInside> lstFrame = Mockito.spy(ArrayList.class);
    lstFrame.add(dataCR);
    PowerMockito.when(crGeneralBusiness.getListDutyTypeCBB(any())).thenReturn(lstFrame);
    String rs = "xxx";
    String result1 = crAutoServiceForSRBusiness.validateTime(crDTO, 1L);
    Assert.assertNotNull(result1);
  }

  @Test
  public void testGetFilePathSrCr() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setBytes(fileContent);
    gnocFileDto.setCreateTime(new Date());
    gnocFileDto.setFileName("fileTest");
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(),
        anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp");
    PowerMockito.when(FileUtils
        .saveUploadFile(any(), any(), anyString(), any())).thenReturn("/trungduongOld");
    GnocFileDto result = crAutoServiceForSRBusiness.getFilePathSrCr(gnocFileDto);
    Assert.assertNotNull(result.getPath());
  }

  @Test
  public void insertAutoCrForSR_01() throws Exception {
    CrAutoServiceForSRBusinessImpl serviceInline = Mockito.spy(crAutoServiceForSRBusiness);
    PowerMockito.mockStatic(I18n.class);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    List<CrFilesAttachDTO> lstFile = Mockito.spy(ArrayList.class);
    String system = "AAM";
    String nationCode = "VNM";
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("WO_CODE_2020");
    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    List<String> lstMop = Mockito.spy(ArrayList.class);
    List<String> lstNodeIp = Mockito.spy(ArrayList.class);
    lstWo.add(woDTO);
    PowerMockito.doReturn(StringUtils.BLANK_STRING_VALUE).when(serviceInline).validate(any(), anyString(), anyString(), anyList());
    PowerMockito.doReturn(StringUtils.BLANK_STRING_VALUE).when(serviceInline).validateAndSaveFileAttach(anyList(), any());
    //    CrDTO crDTO = Mockito.spy(CrDTO.class);
    CrProcessInsideDTO crProcessInsideDTO = Mockito.spy(CrProcessInsideDTO.class);
    prepareInsertAutoCrForSR_01(crDTO, crProcessInsideDTO, lstMop, lstNodeIp, "1");
    PowerMockito.when(crFileAttachBusiness.insertListNoID(anyList())).thenReturn(RESULT.SUCCESS);
    PowerMockito.doReturn("WO_CODE_2020").when(serviceInline).createWO(anyObject(), anyList());
    ResultDTO result = serviceInline.insertAutoCrForSR(crDTO, lstFile, system, nationCode, lstWo, lstMop, lstNodeIp);
    Assert.assertEquals(RESULT.SUCCESS, result.getKey());

  }

  @Test
  public void insertAutoCrForSR_02() throws Exception {
    CrAutoServiceForSRBusinessImpl serviceInline = Mockito.spy(crAutoServiceForSRBusiness);
    PowerMockito.mockStatic(I18n.class);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    List<CrFilesAttachDTO> lstFile = Mockito.spy(ArrayList.class);
    String system = "AAM";
    String nationCode = "VNM";
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("WO_CODE_2020");
    List<WoDTO> lstWo = Mockito.spy(ArrayList.class);
    List<String> lstMop = Mockito.spy(ArrayList.class);
    List<String> lstNodeIp = Mockito.spy(ArrayList.class);
    lstWo.add(woDTO);
    PowerMockito.doReturn(StringUtils.BLANK_STRING_VALUE).when(serviceInline).validate(any(), anyString(), anyString(), anyList());
    PowerMockito.doReturn(StringUtils.BLANK_STRING_VALUE).when(serviceInline).validateAndSaveFileAttach(anyList(), any());
    //    CrDTO crDTO = Mockito.spy(CrDTO.class);
    CrProcessInsideDTO crProcessInsideDTO = Mockito.spy(CrProcessInsideDTO.class);
    PowerMockito.when(crFileAttachBusiness.insertListNoID(anyList())).thenReturn(RESULT.SUCCESS);
    PowerMockito.doReturn("WO_CODE_2020").when(serviceInline).createWO(anyObject(), anyList());
    //case 2 Exception
    prepareInsertAutoCrForSR_01(crDTO, crProcessInsideDTO, lstMop, lstNodeIp, "2");
    ResultDTO result = serviceInline.insertAutoCrForSR(crDTO, lstFile, system, nationCode, lstWo, lstMop, lstNodeIp);
    Assert.assertEquals(RESULT.ERROR, result.getKey());
  }


  private void prepareInsertAutoCrForSR_01(CrDTO crDTO, CrProcessInsideDTO crProcessInsideDTO, List<String> lstMop, List<String> lstNodeIp, String testCase) throws Exception {
//    CrDTO crDTO = Mockito.spy(CrDTO.class);
//    CrProcessInsideDTO crProcessInsideDTO = Mockito.spy(CrProcessInsideDTO.class);
    crDTO.setCrProcessId("1");
    crDTO.setCrNumber("VT_2020");
    crDTO.setState("1");
    crDTO.setCrId("1");
//    List<CrImpactedNodesDTO> lstNetWorkNodeId = Mockito.spy(ArrayList.class);
//    List<CrAffectedNodesDTO> lstNetWorkNodeIdAffected = Mockito.spy(ArrayList.class);
//    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
//    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    crDTO.setIsClickedToAlarmTag(1);
    CrAlarmInsiteDTO crAlarmInsiteDTO = Mockito.spy(CrAlarmInsiteDTO.class);
    crAlarmInsiteDTO.setCrId(1L);
    List<CrAlarmInsiteDTO> crAlarmInsiteDTOS = Mockito.spy(ArrayList.class);
    crAlarmInsiteDTOS.add(crAlarmInsiteDTO);
    List<CrApprovalDepartmentInsiteDTO> lst = Mockito.spy(ArrayList.class);
    CrApprovalDepartmentInsiteDTO item1 = Mockito.spy(CrApprovalDepartmentInsiteDTO.class);
    CrApprovalDepartmentInsiteDTO item2 = Mockito.spy(CrApprovalDepartmentInsiteDTO.class);
    item1.setUserId("999999");
    item1.setUnitId("413314");
    item1.setUserName("thanhlv12");
    item1.setStatus("2");
    item2.setUserId("48");
    item2.setUnitId("123321");
    item2.setUserName("thaoltk");
    item2.setStatus("1");
    lst.add(item1);
    lst.add(item2);
    crProcessInsideDTO.setCrProcessId(1L);
    if ("1".equals(testCase)) {
      crProcessInsideDTO.setRiskLevel(5L);
    } else if ("2".equals(testCase)) {
      crProcessInsideDTO.setRiskLevel(1L);
    }
    lstMop.add("AAM2020");
    lstNodeIp.add("1.1.1.1");
    crProcessInsideDTO.setApprovalLevel(2L);
    crDTO.setRisk(CR_TYPE.STANDARD.toString());
    PowerMockito.when(crApprovalDepartmentBusiness.search(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);
    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong()))
        .thenReturn(crProcessInsideDTO);
    List<InfraDeviceDTO> lstAffectIpInsert = Mockito.spy(ArrayList.class);
    InfraDeviceDTO infraDeviceDTO1 = Mockito.spy(InfraDeviceDTO.class);
    infraDeviceDTO1.setIp("1.1.1.1");
    lstAffectIpInsert.add(infraDeviceDTO1);
    PowerMockito.when(commonStreamServiceProxy.getListInfraDeviceIpV2SrProxy(any()))
        .thenReturn(lstAffectIpInsert);
    MopResult mopResult = Mockito.spy(MopResult.class);
    List<MopInfo> mopInfos = Mockito.spy(ArrayList.class);
    MopInfo mopInfo = Mockito.spy(MopInfo.class);
    mopInfo.getIps().add("1.1.1.1");
    mopInfo.getAffectIps().add("1.1.1.1");
    mopInfo.setMopFileContent("byte[]");
    mopInfo.setMopFile("MOPFILE");
    mopInfo.setMopRollbackFile("MOPFILE");
    mopInfo.setMopRollbackFileContent("byte[]");
    mopInfos.add(mopInfo);
    mopResult.getMopInfos().addAll(mopInfos);
    PowerMockito.when(wSTDTTPort.getMopInfo(anyString())).thenReturn(mopResult);
    PowerMockito.mockStatic(Base64.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(PassTranformer.class);
    byte []bytes = new byte[10];
    PowerMockito.when(Base64.decode(anyString())).thenReturn(bytes);
    PowerMockito.when(PassTranformer.decrypt(anyString())).thenReturn("decrypt");
    PowerMockito.when(FileUtils.saveFtpFile(anyString(),
        anyInt(), anyString(), anyString(), anyString(), anyString(), any(), any())).thenReturn("/path");
    PowerMockito.when(FileUtils.saveUploadFile(anyString(), any(),anyString(), any())).thenReturn("/path");
    PowerMockito.when(crAlarmRepository.getListAlarmByProcess(anyObject())).thenReturn(crAlarmInsiteDTOS);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    if ("1".equals(testCase)) {
      resultInSideDto.setKey(RESULT.SUCCESS);
      resultInSideDto.setMessage(RESULT.SUCCESS);
      PowerMockito.when((wSTDTTPort).linkCr(anyString(), anyString(),
          anyString(), anyString(), anyString(), anyString(), anyLong())).thenReturn(null);
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(RESULT.ERROR);
      PowerMockito.when(crBusiness.delete(anyLong())).thenReturn(null);
      List<WoDTOSearch> lstWoDTOToDel = Mockito.spy(ArrayList.class);
      WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
      woDTOSearch.setWoCode("WOCODE_2020");
      lstWoDTOToDel.add(woDTOSearch);
      PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWoDTOToDel);
      PowerMockito.when(woServiceProxy.deleteWOForRollbackProxy(anyString(), anyString(), anyString())).thenReturn(null);
    }
    PowerMockito.when(crBusiness.createObject(any())).thenReturn(resultInSideDto);
  }

  private void prepareValidate(CrDTO crDTO, String caseTest) {
    crDTO.setChangeOrginator("999999");
    crDTO.setState("12");
    crDTO.setCrId("69");
    crDTO.setCrNumber("CR_01");
    crDTO.setUserCab("thanhlv12");
    crDTO.setSubcategoryId("87");
    crDTO.setCrProcessId("88");
    crDTO.setPriority("1");
    crDTO.setServiceAffecting("1");
    crDTO.setImpactAffect("78");
    crDTO.setTotalAffectedCustomers("55");
    crDTO.setTotalAffectedMinutes("55");
    List<ItemDataCRInside> lstItemDataCrInside = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    if ("1".equals(caseTest)) {
      crDTO.setEarliestStartTime("12/11/2030 10:10:10");
      crDTO.setLatestStartTime("12/11/2030 10:10:10");
      crDTO.setDisturbanceStartTime("12/11/2030 10:10:10");
      crDTO.setDisturbanceEndTime("12/11/2030 10:10:10");

      itemDataCRInside.setSecondValue("00:00:00,23:59:59");
      itemDataCRInside.setValueStr(2L);
      itemDataCRInside.setDisplayStr("Day [00:00:00 - 23:59:59]");
    } else {
      crDTO.setEarliestStartTime("12/11/2030 23:59:10");
      crDTO.setLatestStartTime("13/11/2030 03:03:10");
      crDTO.setDisturbanceStartTime("12/11/2030 23:59:10");
      crDTO.setDisturbanceEndTime("13/11/2030 03:03:10");

      itemDataCRInside.setSecondValue("23:00:00,04:59:59");
      itemDataCRInside.setValueStr(1L);
      itemDataCRInside.setDisplayStr("Night [23:00:00 - 04:59:59]");
    }
    lstItemDataCrInside.add(itemDataCRInside);
    crDTO.setTitle("CR GNOC2");
    crDTO.setDescription("CR GNOC2");
    crDTO.setChangeResponsibleUnit("413314");
    crDTO.setRegion("KV1");
    crDTO.setCountry("VNM");
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    CrAffectedServiceDetailsDTO crAffectedServiceDetailsDTO = Mockito
        .spy(CrAffectedServiceDetailsDTO.class);
    lstAffectedService.add(crAffectedServiceDetailsDTO);
    crDTO.setLstAffectedService(lstAffectedService);

    CrProcessInsideDTO crProcess = Mockito.spy(CrProcessInsideDTO.class);
    crProcess.setCrProcessId(88L);
    crProcess.setCrTypeId(1L);
    crProcess.setRiskLevel(2L);
    crProcess.setImpactSegmentId(2L);
    crProcess.setDeviceTypeId(3L);
    crProcess.setImpactType(6L);

    PowerMockito.when(crProcessBusiness.findCrProcessById(anyLong())).thenReturn(crProcess);

    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    lstNetworkNodeId.add(crImpactedNodesDTO);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);

    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    CrAffectedNodesDTO crAffectedNodesDTO = Mockito.spy(CrAffectedNodesDTO.class);
    lstNetworkNodeIdAffected.add(crAffectedNodesDTO);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("999999");
    usersDTO.setUnitId("413314");
    PowerMockito.when(crBusiness.getUserInfo(anyString())).thenReturn(usersDTO);

    CrInsiteDTO crTemp = Mockito.spy(CrInsiteDTO.class);
    crTemp.setActionType("40");
    crTemp.setCrId("1111111");
    PowerMockito.when(crBusiness.getCrById(1111111L, null)).thenReturn(crTemp);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("SR");
    PowerMockito.when(crGeneralBusiness.getListDutyTypeCBB(any())).thenReturn(lstItemDataCrInside);
    Map<String, String> mapConfig = Mockito.spy(Map.class);
    mapConfig.put("cr_user_org", "999999");
    mapConfig.put("cr_unit_org", "413314");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfig);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    List<CatLocationDTO> lstCat = Mockito.spy(ArrayList.class);
    catLocationDTO.setLocationId("281");
    lstCat.add(catLocationDTO);
    PowerMockito.when(catLocationBusiness.searchByConditionBean(anyList(), anyInt(),
        anyInt(), anyString(), anyString())).thenReturn(lstCat);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(413314L);
    PowerMockito.when(crBusiness.getUnitInfo(anyString())).thenReturn(unitDTO);
  }

}

