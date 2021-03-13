package com.viettel.gnoc.cr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.repository.CfgChildArrayRepository;
import java.util.ArrayList;
import java.util.List;
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
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgChildArrayBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgChildArrayBusinessTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  TicketProvider ticketProvider;

  @Mock
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  protected LanguageExchangeRepository languageExchangeRepository;

  @InjectMocks
  protected CfgChildArrayBusinessImpl cfgChildArrayBusiness;

  @Mock
  protected CfgChildArrayRepository cfgChildArrayRepository;

  @Test
  public void testGetListCfgChildArray_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(datatable.getTotal()).thenReturn(10);
    when(cfgChildArrayRepository.getListCfgChildArray(any())).thenReturn(datatable);

    Datatable result = cfgChildArrayBusiness.getListCfgChildArray(new CfgChildArrayDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void testGetDetailCfgChildArray_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CfgChildArrayDTO cfgChildArrayDTO = Mockito.spy(CfgChildArrayDTO.class);
    when(cfgChildArrayRepository.getDetail(any())).thenReturn(cfgChildArrayDTO);
    CfgChildArrayDTO result = cfgChildArrayBusiness.getDetail(1L);
    assertEquals(cfgChildArrayDTO, result);
  }

  @Test
  public void testAddCfgChildArray_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<LanguageExchangeDTO> lstChildrenName = Mockito.spy(ArrayList.class);
    CfgChildArrayDTO cfgChildArrayDTO = Mockito.spy(CfgChildArrayDTO.class);
    cfgChildArrayDTO.setListChildrenName(lstChildrenName);
    PowerMockito.when(cfgChildArrayRepository.addOrUpdate(any())).thenReturn(resultInSideDto);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange("COMMON_GNOC", "COMMON_GNOC.CFG_CHILD_ARRAY", 1L,
            lstChildrenName))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgChildArrayBusiness.add(cfgChildArrayDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testEditCfgChildArray_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<LanguageExchangeDTO> lstChildrenName = Mockito.spy(ArrayList.class);
    CfgChildArrayDTO cfgChildArrayDTO = Mockito.spy(CfgChildArrayDTO.class);
    cfgChildArrayDTO.setChildrenId(1L);
    cfgChildArrayDTO.setListChildrenName(lstChildrenName);
    PowerMockito.when(cfgChildArrayRepository.addOrUpdate(any())).thenReturn(resultInSideDto);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange("COMMON_GNOC", "COMMON_GNOC.CFG_CHILD_ARRAY", 1L,
            lstChildrenName))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgChildArrayBusiness.update(cfgChildArrayDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testDeleteCfgChildArray_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(cfgChildArrayRepository.delete(anyLong())).thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgChildArrayBusiness.delete(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getCbbChildArray_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CfgChildArrayDTO> cfgChildArrayDTOS = Mockito.spy(ArrayList.class);
    CfgChildArrayDTO dto = Mockito.spy(CfgChildArrayDTO.class);
    PowerMockito.when(cfgChildArrayRepository.getCbbChildArray(any()))
        .thenReturn(cfgChildArrayDTOS);
    List<CfgChildArrayDTO> cfgChildArrayDTOS1 = cfgChildArrayBusiness.getCbbChildArray(dto);
    Assert.assertEquals(cfgChildArrayDTOS1.size(), 0L);
  }

  @Test
  public void getListImpactSegmentCBB_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<ImpactSegmentDTO> lstReturn = Mockito.spy(ArrayList.class);
    PowerMockito.when(cfgChildArrayRepository.getListImpactSegmentCBB()).thenReturn(lstReturn);
    List<LanguageExchangeDTO> lstLanguage = Mockito.spy(ArrayList.class);
    PowerMockito.when(languageExchangeRepository.findBySql(anyString(), anyMap(), anyMap(), any()))
        .thenReturn(lstLanguage);
    List<ImpactSegmentDTO> impactSegmentDTOS = cfgChildArrayBusiness.getListImpactSegmentCBB();
    Assert.assertEquals(impactSegmentDTOS.size(), 0L);
  }
}
