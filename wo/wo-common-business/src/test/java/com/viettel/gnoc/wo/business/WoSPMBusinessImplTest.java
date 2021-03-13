package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.wo.dto.Wo;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoPendingDTO;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoHistoryRepository;
import com.viettel.gnoc.wo.repository.WoPendingRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
import org.slf4j.Logger;

@RunWith(PowerMockRunner.class)
@Slf4j
@PrepareForTest({WoSPMBusinessImpl.class, I18n.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class WoSPMBusinessImplTest {

  @InjectMocks
  WoSPMBusinessImpl woSPMBusiness;
  @Mock
  WoRepository woRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  WoDetailRepository woDetailRepository;
  @Mock
  MessagesRepository messagesRepository;
  @Mock
  WoHistoryRepository woHistoryRepository;
  @Mock
  CommonRepository commonRepository;
  @Mock
  WoPendingRepository woPendingRepository;

  @Test
  public void updateHelpFromSPM_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Long result = 1L;
    String woCode = "WO_HOAN_CONG_20180607_394789";
    String description = "vtnet";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(
        "Wo: [WO_CODE] , Account: [ACCOUNT_ISDN] da duoc kiem tra dieu kien tich help, ket qua: OK. D/c co the thuc hien tich help voi WO nay!#####Wo: [WO_CODE] , Account: [ACCOUNT_ISDN] check help is OK. You can continue tick help!");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(999999L);
    wo.setStatus(9L);
    wo.setWoId(394789L);
    wo.setWoCode(woCode);
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUserId(999999L);
    us.setUsername("thanhlv12");
    us.setFullname("Le Van Thanh");
    us.setMobile("0123456789");
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(us);
    WoDetailDTO wd = Mockito.spy(WoDetailDTO.class);
    wd.setAccountIsdn("h004_gftth_linhvt40");
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(wd);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(messagesRepository.insertOrUpdateWfm(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    ResultDTO resultDTO = woSPMBusiness.updateHelpFromSPM(woCode, description, result);
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getListWoForAccount_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("111");
    List<String> lstAccount = Mockito.spy(ArrayList.class);
    lstAccount.add("thanhlv12");
    List<WoDTO> woDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1111");
    List<WoDTO> woDTOS1 = woSPMBusiness.getListWoForAccount(lstAccount);
    Assert.assertEquals(woDTOS.size(), woDTOS1.size());
  }

  @Test
  public void getListWoByWoType_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoDTOSearch> woDTOSearches = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.getListWoByWoType(anyString(), anyString(), anyString()))
        .thenReturn(woDTOSearches);
    List<WoDTOSearch> woDTOSearches1 = woSPMBusiness
        .getListWoByWoType(anyString(), anyString(), anyString());
    Assert.assertEquals(woDTOSearches.size(), woDTOSearches1.size());
  }

  @Test
  public void updateDescriptionWoSPM_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoId("394747");
    woDTO.setWoDescription("vtnet");
    woDTO.setWoContent("vtnet");
    woDTO.setEndTime("30/06/2020 00:00:00");
    woDTO.setCcGroupId("1111");
    woDTO.setStartTime("30/06/2018 00:00:00");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(111L);
    wo.setWoDescription("vtnet");
    wo.setWoContent("vtnet");
    wo.setEndTime(new Date());
    wo.setCcGroupId(1L);
    woDTO.setStartTime("07/06/2018 17:45:24");
    wo.setFtId(999999L);
    wo.setCreatePersonId(999999L);
    wo.setStatus(9L);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    WoDetailDTO woDetail = Mockito.spy(WoDetailDTO.class);
    woDetail.setCcGroupId(1L);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(woDetail);
    List<WoPendingDTO> lstPending = Mockito.spy(ArrayList.class);
    WoPendingDTO i = Mockito.spy(WoPendingDTO.class);
    i.setWoPendingId(150L);
    lstPending.add(i);
    PowerMockito.when(woPendingRepository.getListWoPendingByWoId(anyLong())).thenReturn(lstPending);
    PowerMockito.when(woPendingRepository.deleteWoPending(anyLong())).thenReturn(resultInSideDto);
    UsersEntity user = Mockito.spy(UsersEntity.class);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(user);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(resultInSideDto);
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUserId(1L);
    us.setUsername("thanhlv12");
    us.setFullname("Le Van Thanh");
    us.setMobile("0123456789");
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(us);
    PowerMockito.when(messagesRepository.insertOrUpdateWfm(any())).thenReturn(resultInSideDto);
    ResultDTO resultDTO = woSPMBusiness.updateDescriptionWoSPM(woDTO);
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void closeWoForSPM_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Không tồn tại wo tương ứng với mã:");
    List<Wo> lstWo = Mockito.spy(ArrayList.class);
    Wo i = Mockito.spy(Wo.class);
    i.setWoSystemId("1");
    i.setReasonDetail("vtnet");
    i.setSolutionDetail("vtnet");
    lstWo.add(i);
    List<WoInsideDTO> lstWoNew = Mockito.spy(ArrayList.class);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setCreatePersonId(1L);
    wo.setStatus(9L);
    wo.setEndTime(new Date());
    wo.setWoId(1L);
    wo.setFinishTime(new Date());
    wo.setLastUpdateTime(new Date());
    wo.setEndPendingTime(new Date());
    try {
      wo.setStartTime(DateUtil.string2Date("07/06/2018 17:45:24"));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    lstWoNew.add(wo);
    PowerMockito.when(woRepository.getFullWoByWoSystemCode(anyString())).thenReturn(lstWoNew);
    UsersEntity us = Mockito.spy(UsersEntity.class);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(us);
    List<WoPendingDTO> lst = Mockito.spy(ArrayList.class);
    WoPendingDTO woPending = Mockito.spy(WoPendingDTO.class);
    lst.add(woPending);
    PowerMockito.when(woPendingRepository.getListWoPendingByWoId(anyLong())).thenReturn(lst);
    PowerMockito.when(woPendingRepository.updateWoPending(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    detail.setCcGroupId(1L);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woPendingRepository.getListWoPendingByWoId(anyLong())).thenReturn(lst);
    ResultDTO resultDTO = woSPMBusiness.closeWoForSPM(lstWo, "gnoc", "thanhlv12", 1L);
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);
  }

}
