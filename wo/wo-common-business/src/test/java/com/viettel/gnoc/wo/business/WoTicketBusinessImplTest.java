package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.wo.dto.AmiOneForm;
import com.viettel.gnoc.wo.dto.CfgSupportForm;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import com.viettel.gnoc.wo.repository.WoSupportRepository;
import com.viettel.gnoc.wo.repository.WoWorklogRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
@PrepareForTest({WoTicketBusinessImpl.class, I18n.class, TtServiceProxy.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
@Slf4j
public class WoTicketBusinessImplTest {

  @InjectMocks
  WoTicketBusinessImpl woTicketBusiness;
  @Mock
  WoRepository woRepository;
  @Mock
  WoSupportRepository woSupportRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  WoBusiness woBusiness;
  @Mock
  WoDetailRepository woDetailRepository;
  @Mock
  WoWorklogRepository woWorklogRepository;
  @Mock
  MessagesRepository messagesRepository;
  @Mock
  TtServiceProxy ttServiceProxy;

  @Before
  public void setUp() {
    ReflectionTestUtils.setField(woTicketBusiness, "uploadFolder", "./wo_upload");
  }

  @Test
  public void getListWoSupportInfo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CfgSupportForm> lstRes = Mockito.spy(ArrayList.class);
    CfgSupportForm cfgSupportForm = Mockito.spy(CfgSupportForm.class);
    lstRes.add(cfgSupportForm);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStartTime(new Date());
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    PowerMockito.when(woSupportRepository.listWoSupportInfo(anyLong())).thenReturn(lstRes);
    List<CfgSupportForm> lstRes1 = woTicketBusiness.getListWoSupportInfo(anyString());
    Assert.assertEquals(lstRes1.size(), 0L);
  }

  @Test
  public void completeWorkHelp_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Mã WO là bắt buộc");
    List<UsersInsideDto> listUs = Mockito.spy(ArrayList.class);
    UsersInsideDto us = Mockito.spy(UsersInsideDto.class);
    us.setUserId(999999L);
    us.setMobile("0123456789");
    listUs.add(us);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUs);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(9L);
    wo.setWoId(1L);
    wo.setFtId(999999L);
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    try {
      PowerMockito.when(woBusiness
          .updatePendingWo(anyString(), anyString(), anyString(), anyString(), anyString(),
              anyBoolean())).thenReturn(resultDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    WoDetailDTO wd = Mockito.spy(WoDetailDTO.class);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(wd);
    ResultInSideDto resWorklog = Mockito.spy(ResultInSideDto.class);
    resWorklog.setKey(RESULT.SUCCESS);
    PowerMockito.when(woWorklogRepository.insertWoWorklog(any())).thenReturn(resWorklog);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(resultInSideDto);
    UsersEntity ft = Mockito.spy(UsersEntity.class);
    ft.setStaffCode("123456");
    ft.setUsername("thanhlv12");
    ft.setMobile("0123456789");
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(ft);
    PowerMockito.when(messagesRepository.insertOrUpdateWfm(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    ResultDTO resultDTO1 = woTicketBusiness
        .completeWorkHelp("123456", "thanhlv12", "vtnet", "0123456");
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getInfoAmiOne_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setCompletedTime(new Date());
    wo.setCommentComplete("vtnet");
    wo.setWoCode("0123");
    wo.setFtId(999999L);
    wo.setFileName("input.docx");
    wo.setWoId(11111L);
    // case này trả về null khi gọi getWoByAmiOneId
    PowerMockito.when(woRepository.getWoByAmiOneId(anyString())).thenReturn(null);
    TroublesDTO tt = Mockito.spy(TroublesDTO.class);
    tt.setClearTime("07/06/2018 10:00:00");
    List<byte[]> fileDocumentByteArray = Mockito.spy(ArrayList.class);
    byte[] myvar = "Any String you want".getBytes();
    fileDocumentByteArray.add(myvar);
    tt.setFileDocumentByteArray(fileDocumentByteArray);
    tt.setTroubleCode("0123");
    List<String> arrFileName = Mockito.spy(ArrayList.class);
    arrFileName.add("input.docx");
    tt.setArrFileName(arrFileName);
    tt.setReasonName("fixBug");
    tt.setClearUserName("thanhlv12");
    List<TroublesDTO> lstTT = Mockito.spy(ArrayList.class);
    lstTT.add(tt);
    PowerMockito.when(ttServiceProxy.getInfoTicketForAMI(any())).thenReturn(lstTT);
    List<String> lstAmiOneId = Mockito.spy(ArrayList.class);
    lstAmiOneId.add("abc");
    List<AmiOneForm> amiOneForms = woTicketBusiness.getInfoAmiOne(lstAmiOneId);
    Assert.assertEquals(amiOneForms.size(), 1L);
  }

  @Test
  public void getInfoAmiOne_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setCompletedTime(new Date());
    wo.setCommentComplete("vtnet");
    wo.setWoCode("0123");
    wo.setFtId(999999L);
    wo.setFileName("input.docx");
    wo.setWoId(11111L);
    PowerMockito.when(woRepository.getWoByAmiOneId(anyString())).thenReturn(wo);
    UsersEntity ft = Mockito.spy(UsersEntity.class);
    ft.setUsername("thanhlv12");
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(ft);
    WoDetailDTO wd = Mockito.spy(WoDetailDTO.class);
    wd.setCcResult(1L);
    CompCause lv3 = Mockito.spy(CompCause.class);
    lv3.setName("vtnet");
    lv3.setCode("AMI.ONE.KQ");
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(wd);
    PowerMockito.when(woRepository.getCompCause(anyLong())).thenReturn(lv3);
    WoInsideDTO o = Mockito.spy(WoInsideDTO.class);
    o.setFileName(
        "WoVSmartBusinessImplTest.java");
    o.setCreateDate(new Date());
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(null);
    List<String> lstAmiOneId = Mockito.spy(ArrayList.class);
    lstAmiOneId.add("abc");
    List<AmiOneForm> amiOneForms = woTicketBusiness.getInfoAmiOne(lstAmiOneId);
    Assert.assertEquals(amiOneForms.size(), 1L);
  }

}
