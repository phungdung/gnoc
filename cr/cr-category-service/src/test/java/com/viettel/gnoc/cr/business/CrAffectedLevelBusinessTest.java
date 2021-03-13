package com.viettel.gnoc.cr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrAffectedLevelDTO;
import com.viettel.gnoc.cr.repository.CrAffectedLevelRepository;
import java.io.File;
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
@PrepareForTest({CrAffectedLevelBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, CommonExport.class,
    TicketProvider.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrAffectedLevelBusinessTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  TicketProvider ticketProvider;

  @Mock
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  protected LanguageExchangeRepository languageExchangeRepository;

  @InjectMocks
  protected CrAffectedLevelBusinessImpl crAffectedLevelBusiness;

  @Mock
  protected CrAffectedLevelRepository crAffectedLevelRepository;

  @Test
  public void testGetCrAffectedLevel_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(crAffectedLevelRepository.getListCrAffectedLevel(any())).thenReturn(datatable);
    Datatable result = crAffectedLevelBusiness.getListCrAffectedLevel(new CrAffectedLevelDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void testGetDetailCrAffectedLevel_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrAffectedLevelDTO crAffectedLevelDTO = Mockito.spy(CrAffectedLevelDTO.class);
    when(crAffectedLevelRepository.getDetail(any())).thenReturn(crAffectedLevelDTO);
    CrAffectedLevelDTO result = crAffectedLevelBusiness.getDetail(1L);
    assertEquals(crAffectedLevelDTO, result);
  }

  @Test
  public void testAddCrAffectedLevel_01() throws Exception {
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
    List<LanguageExchangeDTO> lstAffectedLevelName = Mockito.spy(ArrayList.class);
    CrAffectedLevelDTO crAffectedLevelDTO = Mockito.spy(CrAffectedLevelDTO.class);
    crAffectedLevelDTO.setListAffectedLevelName(lstAffectedLevelName);
    PowerMockito.when(crAffectedLevelRepository.addOrUpdateCrAffectedLevel(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange("COMMON_GNOC", "OPEN_PM.IMPACT_SEGMENT", 1L,
            lstAffectedLevelName))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crAffectedLevelBusiness
        .addCrAffectedLevel(crAffectedLevelDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testEditAffectedLevel_01() {
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
    List<LanguageExchangeDTO> lstAffectedLevelName = Mockito.spy(ArrayList.class);
    CrAffectedLevelDTO crAffectedLevelDTO = Mockito.spy(CrAffectedLevelDTO.class);
    crAffectedLevelDTO.setListAffectedLevelName(lstAffectedLevelName);
    crAffectedLevelDTO.setAffectedLevelId(1L);
    PowerMockito.when(crAffectedLevelRepository.addOrUpdateCrAffectedLevel(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange("COMMON_GNOC", "OPEN_PM.IMPACT_SEGMENT", 1L,
            lstAffectedLevelName))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crAffectedLevelBusiness
        .updateCrAffectedLevel(crAffectedLevelDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testDeleteAffectedLevel_01() throws Exception {
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
    PowerMockito.when(crAffectedLevelRepository.deleteCrAffectedLevel(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crAffectedLevelBusiness.deleteCrAffectedLevel(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testExportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CrAffectedLevelDTO> dtoList = new ArrayList<>();
    CrAffectedLevelDTO dto = Mockito.spy(CrAffectedLevelDTO.class);
    dto.setAffectedLevelCode("affectedLevelCode");
    dto.setAffectedLevelName("affectedLevelName");
    dto.setAppliedSystem(1L);
    dto.setIsActive(1L);
    dto.setAppliedSystemName("appliedSystemName");
    dto.setAppliedSystemName("appliedSystemName");
    PowerMockito.mockStatic(I18n.class);
    dtoList.add(dto);
    when(crAffectedLevelRepository.getListDataExport(any()))
        .thenReturn(dtoList);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(CommonExport.class);
    File file = PowerMockito.mock(File.class);
    when(CommonExport.exportExcel(anyString(), anyString(), any(), anyString(), eq("")))
        .thenReturn(file);
    File result = crAffectedLevelBusiness.exportData(dto);
    Assert.assertNull(result);
  }

  @Test
  public void deleteListCrAffectedLevel_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrAffectedLevelDTO crAffectedLevelDTO = Mockito.spy(CrAffectedLevelDTO.class);
    List<Long> listId = Mockito.spy(ArrayList.class);
    listId.add(1L);
    crAffectedLevelDTO.setListId(listId);
    PowerMockito.when(crAffectedLevelRepository.deleteCrAffectedLevel(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crAffectedLevelBusiness
        .deleteListCrAffectedLevel(crAffectedLevelDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }
}
