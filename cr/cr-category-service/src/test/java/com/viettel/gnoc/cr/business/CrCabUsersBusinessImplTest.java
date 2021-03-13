package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.repository.CrCabUsersRepositoty;
import java.io.File;
import java.util.ArrayList;
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
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrCabUsersBusinessImpl.class, BaseRepository.class, TicketProvider.class,
    I18n.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
@Slf4j
public class CrCabUsersBusinessImplTest {

  @InjectMocks
  CrCabUsersBusinessImpl crCabUsersBusiness;
  @Mock
  CrCabUsersRepositoty crCabUsersRepositoty;
  @Mock
  LanguageExchangeRepository languageExchangeRepository;
  @Mock
  public BaseRepository baseRepository;
  @Mock
  TicketProvider ticketProvider;
  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Test
  public void setSegmentName_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ItemDataCRInside> lstReturn = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("fixBug");
    itemDataCRInside.setValueStr(1L);
    lstReturn.add(itemDataCRInside);
    PowerMockito.when(crCabUsersRepositoty.getListImpactSegmentCBB()).thenReturn(lstReturn);
    List<LanguageExchangeDTO> lstLanguage = Mockito.spy(ArrayList.class);
    LanguageExchangeDTO dto = Mockito.spy(LanguageExchangeDTO.class);
    dto.setBussinessId(1L);
    dto.setLeeLocale("vi_VN");
    lstLanguage.add(dto);
    PowerMockito.when(languageExchangeRepository.findBySql(anyString(), anyMap(), anyMap(), any()))
        .thenReturn(lstLanguage);
    PowerMockito.mockStatic(BaseRepository.class);
    try {
      PowerMockito.when(baseRepository.setLanguage(anyList(), anyList(), anyString(), anyString()))
          .thenReturn(lstReturn);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    crCabUsersBusiness.setSegmentName();
  }

  @Test
  public void setUnitName_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CrCabUsersDTO> list = Mockito.spy(ArrayList.class);
    CrCabUsersDTO item = Mockito.spy(CrCabUsersDTO.class);
    item.setExecuteUnitName("1");
    item.setExecuteUnitId(1L);
    list.add(item);
    PowerMockito.when(crCabUsersRepositoty.getListUnitName()).thenReturn(list);
    crCabUsersBusiness.setUnitName();
  }

  @Test
  public void setCabUnitName_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CrCabUsersDTO> list = Mockito.spy(ArrayList.class);
    CrCabUsersDTO item = Mockito.spy(CrCabUsersDTO.class);
    item.setUserFullName("Le Van Thanh");
    item.setUserID(999999L);
    list.add(item);
    PowerMockito.when(crCabUsersRepositoty.getListUserFullName()).thenReturn(list);
    crCabUsersBusiness.setCabUnitName();
  }

  @Test
  public void insertCrCabUsers_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(crCabUsersRepositoty.insertCrCabUsers(any())).thenReturn(resultInSideDto);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    CrCabUsersDTO crCabUsersDTO = Mockito.spy(CrCabUsersDTO.class);
    ResultInSideDto resultInSideDto1 = crCabUsersBusiness.insertCrCabUsers(crCabUsersDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void updateCrCabUsers_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(crCabUsersRepositoty.updateCrCabUsers(any())).thenReturn(resultInSideDto);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    CrCabUsersDTO crCabUsersDTO = Mockito.spy(CrCabUsersDTO.class);
    ResultInSideDto resultInSideDto1 = crCabUsersBusiness.updateCrCabUsers(crCabUsersDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteCrCabUsers_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(crCabUsersRepositoty.deleteCrCabUsers(any())).thenReturn(resultInSideDto);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crCabUsersBusiness.deleteCrCabUsers(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteListCrCabUsers_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(crCabUsersRepositoty.deleteListCrCabUsers(anyList()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crCabUsersBusiness.deleteListCrCabUsers(anyList());
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getAllInfoCrCABUsers_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    CrCabUsersDTO crCabUsersDTO = Mockito.spy(CrCabUsersDTO.class);
    PowerMockito.when(crCabUsersRepositoty.getAllInfoCrCABUsers(any())).thenReturn(datatable);
    Datatable datatable1 = crCabUsersBusiness.getAllInfoCrCABUsers(crCabUsersDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getAllUserInUnitCrCABUsers_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<UnitDTO> unitDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(crCabUsersRepositoty
        .getAllUserInUnitCrCABUsers(anyLong(), anyLong(), anyString(), anyString(), anyString(),
            anyString(), anyString())).thenReturn(unitDTOS);
    List<UnitDTO> unitDTOS1 = crCabUsersBusiness
        .getAllUserInUnitCrCABUsers(1L, 1L, "thanhlv12", "Le Van Thanh", "1", "vtnet", "413314");
    Assert.assertEquals(unitDTOS.size(), unitDTOS1.size());
  }

  @Test
  public void findById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrCabUsersDTO crCabUsersDTO = Mockito.spy(CrCabUsersDTO.class);
    PowerMockito.when(crCabUsersRepositoty.findById(anyLong())).thenReturn(crCabUsersDTO);
    CrCabUsersDTO crCabUsersDTO1 = crCabUsersBusiness.findById(1L);
    Assert.assertNotNull(crCabUsersDTO1);
  }

  @Test
  public void exportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrCabUsersDTO crCabUsersDTO = Mockito.spy(CrCabUsersDTO.class);
    crCabUsersDTO.setSegmentName("fixBug");
    crCabUsersDTO.setExecuteUnitName("1");
    crCabUsersDTO.setUserFullName("Le Van Thanh");
    crCabUsersDTO.setCabUnitName("vtnet");
    crCabUsersDTO.setCreationUnitName("1");
    List<CrCabUsersDTO> list = Mockito.spy(ArrayList.class);
    list.add(crCabUsersDTO);
    Datatable data = Mockito.spy(Datatable.class);
    data.setData(list);
    PowerMockito.when(crCabUsersRepositoty.getAllInfoCrCABUsers(any())).thenReturn(data);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("fixBug");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    File file = crCabUsersBusiness.exportData(crCabUsersDTO);
    Assert.assertNotNull(file);
  }

}
