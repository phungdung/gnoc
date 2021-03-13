package com.viettel.gnoc.incident.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatCfgClosedTicketBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatCfgClosedTicketDTO;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatCfgClosedTicketRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.repository.TroublesRepository;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import viettel.passport.client.UserToken;

/**
 * @author TrungDuong
 */
@RunWith(PowerMockRunner.class)

@PrepareForTest({WOCreateBusinessImpl.class, FileUtils.class, CommonImport.class,TicketProvider.class,
    I18n.class, CommonExport.class, WorkbookFactory.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class WOCreateBusinessImplTest {

  @InjectMocks
  WOCreateBusinessImpl woCreateBusiness;

  @Mock
  CatCfgClosedTicketBusiness catCfgClosedTicketBusiness;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  CatCfgClosedTicketRepository catCfgClosedTicketRepository;

  @Mock
  TicketProvider ticketProvider;

  @Test
  public void createWO() throws Exception {
    TroublesRepository dao = Mockito.spy(TroublesRepository.class);

    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setDescription("ccc");
    troublesDTO.setCreateUserId(69L);
    troublesDTO.setInsertSource("NOC");
    troublesDTO.setTypeName("mlml");
    troublesDTO.setTroubleName("mmm");
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setTroubleCode("123dcm");
    troublesDTO.setAlarmGroupCode("tyu");
    troublesDTO.setPriorityId(1L);
    troublesDTO.setLinkCode("loll");
    troublesDTO.setLinkId("oo");
    troublesDTO.setAlarmId(1L);
    troublesDTO.setCameraId("2");
    troublesDTO.setAmiId(3L);
    troublesDTO.setCustomerName("RmRm");
    troublesDTO.setCustomerPhone("016966996");
    troublesDTO.setAccountGline("mlem");
    troublesDTO.setLocationCode("281");
    troublesDTO.setWoType("KKK");
    troublesDTO.setAffectedNode("123,456");
    troublesDTO.setRelatedKedb("plpl");
    troublesDTO.setNationCode("VNM");
    troublesDTO.setAlarmGroupId("5");
    troublesDTO.setTypeId(4L);
    troublesDTO.setCdId("6");
    troublesDTO.setCheckbox("1");

    Map<String, String> mapResult = new HashMap<>();
    List<ConfigPropertyDTO> result = Mockito.spy(ArrayList.class);
    ConfigPropertyDTO cfg = Mockito.spy(ConfigPropertyDTO.class);
    cfg.setKey("WO.TT.PRIORITY.CRITICAL");
    cfg.setValue("441,1952");
    result.add(cfg);
    mapResult.put("WO.TT.PRIORITY.CRITICAL", "441,1952");
    PowerMockito.when(dao.getConfigProperty()).thenReturn(mapResult);

    CfgTimeTroubleProcessDTO config = Mockito.spy(CfgTimeTroubleProcessDTO.class);
    config.setProcessWoTime(123.123);
    config.setProcessTtTime(123.123);
    config.setIsCall(1L);
    config.setFtAudioName("hmhmh");
    config.setCdAudioName("kkkhkhkh");

    List<CatCfgClosedTicketDTO> lstClosedTicketDTO = Mockito.spy(ArrayList.class);
    CatCfgClosedTicketDTO catCfgClosedTicketDTO = Mockito.spy(CatCfgClosedTicketDTO.class);
    lstClosedTicketDTO.add(catCfgClosedTicketDTO);

    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setId("1L");
    resultWO.setKey("SUCCESS");
    PowerMockito.when(woServiceProxy.createWoFollowNode(any())).thenReturn(resultWO);

    PowerMockito.when(
        catCfgClosedTicketBusiness
            .getListCatCfgClosedTicketDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstClosedTicketDTO);

    ResultDTO resultDTO = woCreateBusiness.createWO(troublesDTO, config, dao);
    Assert.assertEquals(resultWO.getKey(), resultDTO.getKey());
  }

  @Test
  public void createWOMobile() throws Exception {
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setDescription("ccc");
    troublesDTO.setCreateUserId(69L);
    troublesDTO.setInsertSource("SPM_VTNET");
    troublesDTO.setTypeName("mlml");
    troublesDTO.setTroubleName("mmm");
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setTroubleCode("123dcm");
    troublesDTO.setAlarmGroupCode("tyu");
    troublesDTO.setPriorityId(1L);
    troublesDTO.setLinkCode("loll");
    troublesDTO.setLinkId("oo");
    troublesDTO.setAlarmId(1L);
    troublesDTO.setCameraId("2");
    troublesDTO.setAmiId(3L);
    troublesDTO.setCustomerName("RmRm");
    troublesDTO.setCustomerPhone("016966996");
    troublesDTO.setAccountGline("mlem");
    troublesDTO.setLocationCode("281");
    troublesDTO.setWoType("KKK");
    troublesDTO.setAffectedNode("123,456");
    troublesDTO.setRelatedKedb("plpl");
    troublesDTO.setNationCode("VNM");
    troublesDTO.setAlarmGroupId("5");
    troublesDTO.setTypeId(4L);
    troublesDTO.setCdId("6");
    troublesDTO.setCheckbox("1");
    troublesDTO.setWoContent("jjjjjjjjjjjjjjj");
    troublesDTO.setCustomerTimeDesireFrom(DateTimeUtils.convertDateTimeToString(new Date()));
    troublesDTO.setCustomerTimeDesireTo(DateTimeUtils.convertDateTimeToString(new Date()));
    troublesDTO.setCcGroupId("3");
    troublesDTO.setServiceType(1L);
    troublesDTO.setTelServiceId("016966996");
    troublesDTO.setInfraType("CCN");
    troublesDTO.setIsCcResult("okokoko");
    troublesDTO.setProductCode("OLOLOL");
    troublesDTO.setSpmName("PPP");
    troublesDTO.setSpmCode("ROLROY");
    troublesDTO.setPriorityCode("ERERR");
    Map<String, String> mapResult = new HashMap<>();
    List<ConfigPropertyDTO> result = Mockito.spy(ArrayList.class);
    ConfigPropertyDTO cfg = Mockito.spy(ConfigPropertyDTO.class);
    cfg.setKey("WO.TT.PRIORITY.CRITICAL");
    cfg.setValue("441,1952");
    result.add(cfg);
    mapResult.put("WO.TT.PRIORITY.CRITICAL", "441,1952");
    TroublesRepository dao = Mockito.spy(TroublesRepository.class);
    PowerMockito.when(dao.getConfigProperty()).thenReturn(mapResult);

    List<String> arrFileName = Mockito.spy(ArrayList.class);
    arrFileName.add("abc");
    arrFileName.add("def");
    troublesDTO.setArrFileName(arrFileName);

    List<byte[]> list = Mockito.spy(ArrayList.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    list.add(fileContent);
    troublesDTO.setFileDocumentByteArray(list);
    CfgTimeTroubleProcessDTO config = Mockito.spy(CfgTimeTroubleProcessDTO.class);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("incident.handle")).thenReturn("yêu cầu đ/c xử lý");
    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setId("1");
    resultWO.setKey("SUCCESS");
    PowerMockito.when(woServiceProxy.insertWoForSPMProxy(any())).thenReturn(resultWO);
    woCreateBusiness.createWOMobile(troublesDTO, config, dao);
    ResultDTO resultDTO = woCreateBusiness.createWOMobile(troublesDTO, config, dao);
    Assert.assertEquals(resultWO.getKey(), resultDTO.getKey());
  }

  @Test
  public void createWOForCC() throws Exception {
    Map<String, String> mapProperty = new HashMap<>();
    mapProperty.put("TT.TIME.PER.WO.TIME", "1:2");
    mapProperty.put("WO.TT.PRIORITY.CRITICAL", "441,1952");
    mapProperty.put("WO.TYPE.XLSCVT", "2902");
    mapProperty.put("WO.TT.CREATE_PERSON.ID", "180468");
    TroublesRepository dao = Mockito.spy(TroublesRepository.class);
    PowerMockito.when(dao.getConfigProperty()).thenReturn(mapProperty);
    TroublesInSideDTO troublesDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesDTO.setDescription("ML");
    troublesDTO.setCreateUserId(1L);
    troublesDTO.setTroubleName("BN");
    troublesDTO.setWoContent("GHJI");
    troublesDTO.setBeginTroubleTime(new Date());
    troublesDTO.setTimeProcess(123.123);
    troublesDTO.setCreatedTime(new Date());
    troublesDTO.setTroubleCode("DCM");
    troublesDTO.setPriorityId(1952L);
    List<byte[]> list = Mockito.spy(ArrayList.class);
    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    list.add(fileContent);
    troublesDTO.setFileDocumentByteArray(list);
    List<String> arrFileName = Mockito.spy(ArrayList.class);
    arrFileName.add("abc");
    arrFileName.add("def");
    troublesDTO.setArrFileName(arrFileName);
    troublesDTO.setComplaintGroupId("1");
    troublesDTO.setIsCcResult("GMK");
    troublesDTO.setProductCode("LOLO");
    troublesDTO.setSpmName("Fo4");
    troublesDTO.setLocationCode("VNM");
    List<InfraDeviceDTO> lstNode = Mockito.spy(ArrayList.class);
    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    infraDeviceDTO.setDeviceName("GHGHGH");
    infraDeviceDTO.setIp("12");
    infraDeviceDTO.setDeviceCode("PLPLP");
    lstNode.add(infraDeviceDTO);
    troublesDTO.setLstNode(lstNode);
    troublesDTO.setRelatedKedb("PPP");

    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setKey("SUCCESS");
    resultWO.setId("1");
    PowerMockito.when(woServiceProxy.createWoProxy(any())).thenReturn(resultWO);

    ResultDTO resultDTO = woCreateBusiness.createWOForCC(troublesDTO, dao);
    Assert.assertEquals(resultDTO.getKey(),resultWO.getKey());
  }

  @Test
  public void changeStatusWo() {
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setId("1L");
    resultDTO.setKey("SUCCESS");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoUpdateStatusForm updateForm = Mockito.spy(WoUpdateStatusForm.class);
    PowerMockito.when(woServiceProxy.changeStatusWoProxy(any())).thenReturn(resultDTO);
    woCreateBusiness.changeStatusWo(updateForm);
  }
}
