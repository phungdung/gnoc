
package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.CrAffectedServiceInfo;
import com.viettel.gnoc.cr.dto.CrAlarmDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrDetailInfoDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrForNocProDTO;
import com.viettel.gnoc.cr.dto.CrForNocProObj;
import com.viettel.gnoc.cr.dto.MessageForm;
import com.viettel.gnoc.cr.dto.MiniCrDTO;
import com.viettel.gnoc.cr.dto.MiniImpactedNode;
import com.viettel.gnoc.cr.repository.CrAlarmRepository;
import com.viettel.gnoc.cr.repository.CrForNocProRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.gnoc.cr.repository.SmsDBRepository;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrForNocProBusinessImpl.class, FileUtils.class, I18n.class, DataUtil.class,
    DateTimeUtils.class, PassProtector.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrForNocProBusinessImplTest {

  @InjectMocks
  CrForNocProBusinessImpl crForNocProBusiness;

  @Mock
  CrForNocProRepository crForNocProRepository;

  @Mock
  CrRepository crRepository;

  @Mock
  SmsDBRepository smsDBRepository;

  @Mock
  CrAlarmRepository crAlarmRepository;

  @Mock
  CrBusiness crBusiness;


  private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(crForNocProBusiness, "userConf",
        "thanhlv12");
    ReflectionTestUtils.setField(crForNocProBusiness, "passConf",
        "123456a@");
    ReflectionTestUtils.setField(crForNocProBusiness, "saltConf",
        "Gman");
  }


  @Test
  public void testGetCrObjectForNocPro_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String deviceCode = "a";
    String deviceName = "b";
    String ip = "c";
    String activeTimeStr = "d";
    String countryCode = "e";
    String impactSegmentCode = "f";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbb");

    CrForNocProObj result = crForNocProBusiness
        .getCrObjectForNocPro(userService, passService, deviceCode, deviceName, ip, activeTimeStr,
            countryCode, impactSegmentCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testGetCrObjectForNocPro_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String deviceCode = "a";
    String deviceName = "b";
    String ip = "c";
    String activeTimeStr = "";
    String countryCode = "e";
    String impactSegmentCode = "f";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");

    CrForNocProObj result = crForNocProBusiness
        .getCrObjectForNocPro(userService, passService, deviceCode, deviceName, ip, activeTimeStr,
            countryCode, impactSegmentCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testGetCrObjectForNocPro_03() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String deviceCode = "";
    String deviceName = "";
    String ip = "";
    String activeTimeStr = "g";
    String countryCode = "e";
    String impactSegmentCode = "";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");

    CrForNocProObj result = crForNocProBusiness
        .getCrObjectForNocPro(userService, passService, deviceCode, deviceName, ip, activeTimeStr,
            countryCode, impactSegmentCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testGetCrObjectForNocPro_04() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
//    PowerMockito.mockStatic(SimpleDateFormat.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String deviceCode = "v";
    String deviceName = "t";
    String ip = "12";
    String activeTimeStr = "05/13/2020 01:01:01";
    String countryCode = "e";
    String impactSegmentCode = "gg";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");

    List<Long> listNodeIp = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 505; i++) {
      listNodeIp.add(Long.valueOf(i));
    }
    PowerMockito
        .when(crForNocProRepository.getNodeIpId(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(listNodeIp);

    List<MiniCrDTO> crDtoList = Mockito.spy(ArrayList.class);
    MiniCrDTO miniCrDTO = Mockito.spy(MiniCrDTO.class);
    miniCrDTO.setCreatedDate(new Date());
    miniCrDTO.setEarliestStartTime(new Date());
    crDtoList.add(miniCrDTO);
    Date activeTime = this.dateFormat.parse(activeTimeStr);
    PowerMockito.when(crForNocProRepository.getCrByStateAndActiveTime(6L, activeTime))
        .thenReturn(crDtoList);

    List<CrForNocProDTO> objs = Mockito.spy(ArrayList.class);
    CrForNocProDTO crForNocProDTO = Mockito.spy(CrForNocProDTO.class);
    crForNocProDTO.setCrId(1L);
    crForNocProDTO.setActiveUser("thanhlv12");
    objs.add(crForNocProDTO);
    PowerMockito.when(crForNocProRepository.getCrFromImpactNode(anyList(), anyList(), any(), any()))
        .thenReturn(objs);
//    PowerMockito.when(crForNocProRepository.getCrFromAffectNode(anyList(),anyList(), any(),any())).thenReturn(objs);
    PowerMockito.when(crForNocProRepository.getCrByImpactSegment(anyLong(), anyString(), any()))
        .thenReturn(objs);
    CrForNocProObj result = crForNocProBusiness
        .getCrObjectForNocPro(userService, passService, deviceCode, deviceName, ip, activeTimeStr,
            countryCode, impactSegmentCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testGetCrObjectForNocPro_05() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);
//    PowerMockito.mockStatic(SimpleDateFormat.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String deviceCode = "v";
    String deviceName = "t";
    String ip = "12";
    String activeTimeStr = "05/13/2020 01:01:01";
    String countryCode = "e";
    String impactSegmentCode = "gg";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");

    List<Long> listNodeIp = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 505; i++) {
      listNodeIp.add(Long.valueOf(i));
    }
    PowerMockito
        .when(crForNocProRepository.getNodeIpId(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(listNodeIp);

    List<MiniCrDTO> crDtoList = Mockito.spy(ArrayList.class);
    MiniCrDTO miniCrDTO = Mockito.spy(MiniCrDTO.class);
    miniCrDTO.setCreatedDate(new Date());
    miniCrDTO.setEarliestStartTime(new Date());
    crDtoList.add(miniCrDTO);
    Date activeTime = this.dateFormat.parse(activeTimeStr);
    PowerMockito.when(crForNocProRepository.getCrByStateAndActiveTime(6L, activeTime))
        .thenReturn(crDtoList);

    List<CrForNocProDTO> crForNocProDTOS = Mockito.spy(ArrayList.class);
    List<CrForNocProDTO> objs = Mockito.spy(ArrayList.class);
    CrForNocProDTO crForNocProDTO = Mockito.spy(CrForNocProDTO.class);
    crForNocProDTO.setCrId(1L);
    crForNocProDTO.setActiveUser("thanhlv12");
    objs.add(crForNocProDTO);
    PowerMockito.when(crForNocProRepository.getCrFromImpactNode(anyList(), anyList(), any(), any()))
        .thenReturn(crForNocProDTOS);
    PowerMockito.when(crForNocProRepository.getCrFromAffectNode(anyList(), anyList(), any(), any()))
        .thenReturn(objs);
//    PowerMockito.when(crForNocProRepository.getCrByImpactSegment(anyLong(),anyString(),any())).thenReturn(objs);
    CrForNocProObj result = crForNocProBusiness
        .getCrObjectForNocPro(userService, passService, deviceCode, deviceName, ip, activeTimeStr,
            countryCode, impactSegmentCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testGetCrObjectForNocPro_06() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String deviceCode = "v";
    String deviceName = "t";
    String ip = "12";
    String activeTimeStr = "05/13/2020 01:01:01";
    String countryCode = "e";
    String impactSegmentCode = "gg";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");

    List<Long> listNodeIp = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 505; i++) {
      listNodeIp.add(Long.valueOf(i));
    }
    PowerMockito
        .when(crForNocProRepository.getNodeIpId(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(listNodeIp);

    List<MiniCrDTO> crDtoList = Mockito.spy(ArrayList.class);
    MiniCrDTO miniCrDTO = Mockito.spy(MiniCrDTO.class);
    miniCrDTO.setCreatedDate(new Date());
    miniCrDTO.setEarliestStartTime(new Date());
    crDtoList.add(miniCrDTO);
    Date activeTime = this.dateFormat.parse(activeTimeStr);
    PowerMockito.when(crForNocProRepository.getCrByStateAndActiveTime(6L, activeTime))
        .thenReturn(crDtoList);

    List<CrForNocProDTO> crForNocProDTOS = Mockito.spy(ArrayList.class);
    List<CrForNocProDTO> objs = Mockito.spy(ArrayList.class);
    CrForNocProDTO crForNocProDTO = Mockito.spy(CrForNocProDTO.class);
    crForNocProDTO.setCrId(1L);
    crForNocProDTO.setActiveUser("thanhlv12");
    objs.add(crForNocProDTO);
    PowerMockito.when(crForNocProRepository.getCrFromImpactNode(anyList(), anyList(), any(), any()))
        .thenReturn(crForNocProDTOS);
    PowerMockito.when(crForNocProRepository.getCrFromAffectNode(anyList(), anyList(), any(), any()))
        .thenReturn(crForNocProDTOS);
    PowerMockito.when(crForNocProRepository.getCrByImpactSegment(anyLong(), anyString(), any()))
        .thenReturn(objs);
    CrForNocProObj result = crForNocProBusiness
        .getCrObjectForNocPro(userService, passService, deviceCode, deviceName, ip, activeTimeStr,
            countryCode, impactSegmentCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testGetCrObjectV2_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    int delayMinutes = 11;
    String nationCode = "t";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbb");
    CrForNocProObj result = crForNocProBusiness
        .getCrObjectV2(userService, passService, delayMinutes, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testGetCrObjectV2_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    int delayMinutes = -11;
    String nationCode = "t";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrForNocProObj result = crForNocProBusiness
        .getCrObjectV2(userService, passService, delayMinutes, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testGetCrObjectV2_03() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    int delayMinutes = 11;
    String nationCode = "t";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    List<CrDetailInfoDTO> detaillist = Mockito.spy(ArrayList.class);
    PowerMockito
        .when(crForNocProRepository.getListCrDetailInfoDTO(anyList(), any(), any(), anyString()))
        .thenReturn(detaillist);

    CrForNocProObj result = crForNocProBusiness
        .getCrObjectV2(userService, passService, delayMinutes, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testGetCrObjectV2_04() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    int delayMinutes = 11;
    String nationCode = "t";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    List<CrDetailInfoDTO> detaillist = Mockito.spy(ArrayList.class);
    CrDetailInfoDTO crDetailInfoDTO = Mockito.spy(CrDetailInfoDTO.class);
    crDetailInfoDTO.setCrId(1L);
    crDetailInfoDTO.setCreatedDate(new Date());
    detaillist.add(crDetailInfoDTO);
    PowerMockito
        .when(crForNocProRepository.getListCrDetailInfoDTO(anyList(), any(), any(), anyString()))
        .thenReturn(detaillist);

    List<MiniImpactedNode> impactedNodeList = Mockito.spy(ArrayList.class);
    List<MiniImpactedNode> affectedNodeList = Mockito.spy(ArrayList.class);
    MiniImpactedNode miniImpactedNode = Mockito.spy(MiniImpactedNode.class);
    miniImpactedNode.setCrId(1L);
    miniImpactedNode.setDeviceName("fgh");
    impactedNodeList.add(miniImpactedNode);
    affectedNodeList.add(miniImpactedNode);

    List<CrFilesAttachDTO> fileAttachList = Mockito.spy(ArrayList.class);
    CrFilesAttachDTO crFilesAttachDTO = Mockito.spy(CrFilesAttachDTO.class);
    crFilesAttachDTO.setCrId("1");
    crFilesAttachDTO.setFileName("grgr");
    fileAttachList.add(crFilesAttachDTO);

    List<CrAlarmDTO> alarmlist = Mockito.spy(ArrayList.class);
    CrAlarmDTO crAlarmDTO = Mockito.spy(CrAlarmDTO.class);
    crAlarmDTO.setAutoLoad("cvcv");
    crAlarmDTO.setCrId(1L);
    alarmlist.add(crAlarmDTO);

    List<CrAffectedServiceInfo> affectedServiceList = Mockito.spy(ArrayList.class);
    CrAffectedServiceInfo crAffectedServiceInfo = Mockito.spy(CrAffectedServiceInfo.class);
    crAffectedServiceInfo.setCrId(1L);
    crAffectedServiceInfo.setInsertTime(new Date());
    crAffectedServiceInfo.setAffectedServiceId(2L);
    affectedServiceList.add(crAffectedServiceInfo);

    PowerMockito.when(crForNocProRepository.getImpactedNodeByCrIdsV2(anyList(), any(), any()))
        .thenReturn(impactedNodeList);
    PowerMockito.when(crForNocProRepository.getAffectedNodeByCrIdsV2(anyList(), any(), any()))
        .thenReturn(affectedNodeList);
    PowerMockito.when(crForNocProRepository.getFileAttachCrIdsV2(anyList(), any(), any()))
        .thenReturn(fileAttachList);
    PowerMockito.when(crForNocProRepository.getListAlarm(anyList(), any(), any()))
        .thenReturn(alarmlist);
    PowerMockito.when(crForNocProRepository.getListAffectService(anyList(), any(), any()))
        .thenReturn(affectedServiceList);

    CrForNocProObj result = crForNocProBusiness
        .getCrObjectV2(userService, passService, delayMinutes, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertResolveWoLog_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "69";
    Long userGroupActionId = 1L;
    Long wlayId = 10L;
    String worklogContent = "";
    for (int i = 0; i <= 1001; i++) {
      worklogContent += "a ";
    }
    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    CrForNocProObj result = crForNocProBusiness
        .insertResolveWoLog(userService, passService, userName, crNumber, worklogContent,
            userGroupActionId, wlayId);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertResolveWoLog_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "69";
    Long userGroupActionId = 1L;
    Long wlayId = 10L;
    String worklogContent = "a a a";
    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbb");
    CrForNocProObj result = crForNocProBusiness
        .insertResolveWoLog(userService, passService, userName, crNumber, worklogContent,
            userGroupActionId, wlayId);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertResolveWoLog_03() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "69";
    Long userGroupActionId = 1L;
    Long wlayId = 10L;
    String worklogContent = "a a a";
    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrDTO crDto = null;
    PowerMockito.when(crRepository.getCrByNumber(anyString(), any(), any())).thenReturn(crDto);

    CrForNocProObj result = crForNocProBusiness
        .insertResolveWoLog(userService, passService, userName, crNumber, worklogContent,
            userGroupActionId, wlayId);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertResolveWoLog_04() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "69";
    Long userGroupActionId = 1L;
    Long wlayId = 10L;
    String worklogContent = "a a a";
    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrDTO crDto = Mockito.spy(CrDTO.class);
    crDto.setSearchType("tyty");
    crDto.setCrId("1");
    PowerMockito.when(crRepository.getCrByNumber(anyString(), any(), any())).thenReturn(crDto);

    UsersDTO createdUserDTO = null;
    PowerMockito.when(this.crForNocProRepository.getUserByUserName(anyString()))
        .thenReturn(createdUserDTO);

    CrForNocProObj result = crForNocProBusiness
        .insertResolveWoLog(userService, passService, userName, crNumber, worklogContent,
            userGroupActionId, wlayId);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertResolveWoLog_05() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "69";
    Long userGroupActionId = 1L;
    Long wlayId = 10L;
    String worklogContent = "a a a";
    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrDTO crDto = Mockito.spy(CrDTO.class);
    crDto.setSearchType("tyty");
    crDto.setCrId("1");
    crDto.setChangeResponsible("6");
    PowerMockito.when(crRepository.getCrByNumber(anyString(), any(), any())).thenReturn(crDto);

    UsersDTO createdUserDTO = Mockito.spy(UsersDTO.class);
    createdUserDTO.setUnitName("abc");
    createdUserDTO.setUserId("2");
    PowerMockito.when(this.crForNocProRepository.getUserByUserName(anyString()))
        .thenReturn(createdUserDTO);

    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setMessage("");
//    PowerMockito.when(workLogService.insertWorkLog(any())).thenReturn(res);

    CrForNocProObj result = crForNocProBusiness
        .insertResolveWoLog(userService, passService, userName, crNumber, worklogContent,
            userGroupActionId, wlayId);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertResolveWoLog_06() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "69";
    Long userGroupActionId = 1L;
    Long wlayId = 10L;
    String worklogContent = "a a a";
    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrDTO crDto = Mockito.spy(CrDTO.class);
    crDto.setSearchType("tyty");
    crDto.setCrId("1");
    crDto.setChangeResponsible("");
    PowerMockito.when(crRepository.getCrByNumber(anyString(), any(), any())).thenReturn(crDto);

    UsersDTO createdUserDTO = Mockito.spy(UsersDTO.class);
    createdUserDTO.setUnitName("abc");
    createdUserDTO.setUserId("2");
    PowerMockito.when(this.crForNocProRepository.getUserByUserName(anyString()))
        .thenReturn(createdUserDTO);

    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setMessage("SUCCESS");
    PowerMockito.when(crBusiness.insertWorkLogProxy(any())).thenReturn(res);

    CrForNocProObj result = crForNocProBusiness
        .insertResolveWoLog(userService, passService, userName, crNumber, worklogContent,
            userGroupActionId, wlayId);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertResolveWoLog_07() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String userName = "thanhlv12";
    String crNumber = "69";
    Long userGroupActionId = 1L;
    Long wlayId = 10L;
    String worklogContent = "a a a";
    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrDTO crDto = Mockito.spy(CrDTO.class);
    crDto.setSearchType("tyty");
    crDto.setCrId("1");
    crDto.setChangeResponsible("68");
    PowerMockito.when(crRepository.getCrByNumber(anyString(), any(), any())).thenReturn(crDto);

    UsersDTO createdUserDTO = Mockito.spy(UsersDTO.class);
    createdUserDTO.setUnitName("abc");
    createdUserDTO.setUserId("2");
    PowerMockito.when(this.crForNocProRepository.getUserByUserName(anyString()))
        .thenReturn(createdUserDTO);

    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setMessage("SUCCESS");
    PowerMockito.when(crBusiness.insertWorkLogProxy(any())).thenReturn(res);

    List<MessageForm> lst = Mockito.spy(ArrayList.class);
    MessageForm messageForm = Mockito.spy(MessageForm.class);
    messageForm.setDeptId(1L);
    messageForm.setCellPhone("0377156987");
    lst.add(messageForm);
    PowerMockito.when(smsDBRepository.getListUsersByUserId(anyList(), anyBoolean()))
        .thenReturn(lst);

    CrForNocProObj result = crForNocProBusiness
        .insertResolveWoLog(userService, passService, userName, crNumber, worklogContent,
            userGroupActionId, wlayId);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testSendSMS_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "6";
    String smsContent = "ghighighi";
    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbbb");
    CrForNocProObj result = crForNocProBusiness
        .sendSMS(userService, passService, crNumber, smsContent);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testSendSMS_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "6";
    String smsContent = "ghighighi";
    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrDTO crDto = null;
    PowerMockito.when(crRepository.getCrByNumber(anyString(), any(), any())).thenReturn(crDto);

    CrForNocProObj result = crForNocProBusiness
        .sendSMS(userService, passService, crNumber, smsContent);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testSendSMS_03() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "6";
    String smsContent = "ghighighi";
    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrDTO crDto = Mockito.spy(CrDTO.class);
    crDto.setChangeResponsible("6");
    PowerMockito.when(crRepository.getCrByNumber(anyString(), any(), any())).thenReturn(crDto);
    List<MessageForm> lst = Mockito.spy(ArrayList.class);
    MessageForm messageForm = Mockito.spy(MessageForm.class);
    messageForm.setDeptId(1L);
    messageForm.setCellPhone("0377156987");
    lst.add(messageForm);
    PowerMockito.when(smsDBRepository.getListUsersByUserId(anyList(), anyBoolean()))
        .thenReturn(lst);

    CrForNocProObj result = crForNocProBusiness
        .sendSMS(userService, passService, crNumber, smsContent);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertAlarmDetail_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "6";
    String faultIdStr = "a";
    String faultName = "b";
    String faultSrc = "c";
    String vendorCode = "v";
    String vendorName = "d";
    String moduleCode = "TYU";
    String moduleName = "KOKO";
    String nationCode = "OIUY";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("aaaa");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("bbbb");
    CrForNocProObj result = crForNocProBusiness
        .insertAlarmDetail(userService, passService, crNumber, faultIdStr, faultName, faultSrc,
            vendorCode, vendorName, moduleCode, moduleName, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertAlarmDetail_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "";
    String faultIdStr = "a";
    String faultName = "b";
    String faultSrc = "c";
    String vendorCode = "v";
    String vendorName = "d";
    String moduleCode = "TYU";
    String moduleName = "KOKO";
    String nationCode = "OIUY";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrForNocProObj result = crForNocProBusiness
        .insertAlarmDetail(userService, passService, crNumber, faultIdStr, faultName, faultSrc,
            vendorCode, vendorName, moduleCode, moduleName, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertAlarmDetail_03() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "6";
    String faultIdStr = "";
    String faultName = "";
    String faultSrc = "";
    String vendorCode = "v";
    String vendorName = "d";
    String moduleCode = "TYU";
    String moduleName = "KOKO";
    String nationCode = "OIUY";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrForNocProObj result = crForNocProBusiness
        .insertAlarmDetail(userService, passService, crNumber, faultIdStr, faultName, faultSrc,
            vendorCode, vendorName, moduleCode, moduleName, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertAlarmDetail_04() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "6";
    String faultIdStr = "7";
    String faultName = "ki";
    String faultSrc = "opop";
    String vendorCode = "";
    String vendorName = "deee";
    String moduleCode = "";
    String moduleName = "KOKO";
    String nationCode = "OIUY";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrForNocProObj result = crForNocProBusiness
        .insertAlarmDetail(userService, passService, crNumber, faultIdStr, faultName, faultSrc,
            vendorCode, vendorName, moduleCode, moduleName, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertAlarmDetail_05() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "6";
    String faultIdStr = "7";
    String faultName = "ki";
    String faultSrc = "opop";
    String vendorCode = "";
    String vendorName = "deee";
    String moduleCode = "yyyyy";
    String moduleName = "KOKO";
    String nationCode = "";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrForNocProObj result = crForNocProBusiness
        .insertAlarmDetail(userService, passService, crNumber, faultIdStr, faultName, faultSrc,
            vendorCode, vendorName, moduleCode, moduleName, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertAlarmDetail_06() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "6";
    String faultIdStr = "7";
    String faultName = "ki";
    String faultSrc = "opop";
    String vendorCode = "HOT";
    String vendorName = "deee";
    String moduleCode = "yyyyy";
    String moduleName = "KOKO";
    String nationCode = "uyuy";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrDTO crDto = Mockito.spy(CrDTO.class);
    crDto.setProcessTypeId(null);
    PowerMockito.when(crRepository.getCrByNumber(anyString(), any(), any())).thenReturn(crDto);

    CrForNocProObj result = crForNocProBusiness
        .insertAlarmDetail(userService, passService, crNumber, faultIdStr, faultName, faultSrc,
            vendorCode, vendorName, moduleCode, moduleName, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertAlarmDetail_07() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "6";
    String faultIdStr = "7";
    String faultName = "ki";
    String faultSrc = "opop";
    String vendorCode = "HOT";
    String vendorName = "deee";
    String moduleCode = "yyyyy";
    String moduleName = "KOKO";
    String nationCode = "uyuy";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.FAIL);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrDTO crDto = Mockito.spy(CrDTO.class);
    crDto.setProcessTypeId("1");
    PowerMockito.when(crRepository.getCrByNumber(anyString(), any(), any())).thenReturn(crDto);
    List<CrAlarmSettingDTO> exitedSetting = null;
    PowerMockito.when(crAlarmRepository
        .getAlarmSetting(anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(),
            anyLong(), anyString(), anyLong(), anyLong())).thenReturn(exitedSetting);

    CrForNocProObj result = crForNocProBusiness
        .insertAlarmDetail(userService, passService, crNumber, faultIdStr, faultName, faultSrc,
            vendorCode, vendorName, moduleCode, moduleName, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void testInsertAlarmDetail_08() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("qqqq");
    PowerMockito.mockStatic(PassProtector.class);

    String userService = "thanhlv12";
    String passService = "123456a@";
    String crNumber = "6";
    String faultIdStr = "7";
    String faultName = "ki";
    String faultSrc = "opop";
    String vendorCode = "HOT";
    String vendorName = "deee";
    String moduleCode = "yyyyy";
    String moduleName = "KOKO";
    String nationCode = "uyuy";

    CrForNocProObj crObject = Mockito.spy(CrForNocProObj.class);
    crObject.setStatus(0);
    crObject.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(PassProtector.encrypt(userService, "Gman")).thenReturn("thanhlv12");
    PowerMockito.when(PassProtector.encrypt(passService, "Gman")).thenReturn("123456a@");
    CrDTO crDto = Mockito.spy(CrDTO.class);
    crDto.setProcessTypeId("1");
    PowerMockito.when(crRepository.getCrByNumber(anyString(), any(), any())).thenReturn(crDto);
    List<CrAlarmSettingDTO> exitedSetting = null;
    PowerMockito.when(crAlarmRepository
        .getAlarmSetting(anyString(), anyString(), anyString(), anyLong(), anyString(), anyLong(),
            anyLong(), anyString(), anyLong(), anyLong())).thenReturn(exitedSetting);
    boolean doInsert = true;
    PowerMockito.when(crAlarmRepository.insertListAlarmSetting(anyList())).thenReturn(doInsert);

    CrForNocProObj result = crForNocProBusiness
        .insertAlarmDetail(userService, passService, crNumber, faultIdStr, faultName, faultSrc,
            vendorCode, vendorName, moduleCode, moduleName, nationCode);
    Assert.assertEquals(crObject.getKey(), result.getKey());
  }

  @Test
  public void getWorkLog() {
    WorkLogDTO workLogDTO = Mockito.spy(WorkLogDTO.class);
    workLogDTO.setWlgText("ccc");
    PowerMockito.when(crForNocProRepository.getWorkLog(anyString())).thenReturn(workLogDTO);
    WorkLogDTO dto = crForNocProBusiness.getWorkLog("1");
    Assert.assertNotNull(dto);
  }
}

