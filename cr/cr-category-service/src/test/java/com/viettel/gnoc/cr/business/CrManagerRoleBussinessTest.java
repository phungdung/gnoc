package com.viettel.gnoc.cr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.viettel.gnoc.cr.dto.CrRolesDTO;
import com.viettel.gnoc.cr.repository.CrRolesRepository;
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
@PrepareForTest({CrRolesBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrManagerRoleBussinessTest {

  @InjectMocks
  CrRolesBusinessImpl crRolesBusiness;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  TicketProvider ticketProvider;

  @Mock
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  protected CrRolesRepository crRolesRepository;

  @Mock
  protected LanguageExchangeRepository languageExchangeRepository;

  @Test
  public void testGetListCrRoles_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(crRolesRepository.getListCrRoles(any())).thenReturn(datatable);

    Datatable result = crRolesBusiness.getListCrRoles(new CrRolesDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void testGetDetailCrRoles_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    CrRolesDTO crRolesDTO = Mockito.spy(CrRolesDTO.class);
    crRolesDTO.setCmreId(1L);
    when(crRolesRepository.getDetail(any())).thenReturn(crRolesDTO);

    CrRolesDTO result = crRolesBusiness.getDetail(1L);
    assertEquals(crRolesDTO, result);
  }

  @Test
  public void testAddCrRoles_01() throws Exception {
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
    List<LanguageExchangeDTO> lstRoleName = Mockito.spy(ArrayList.class);
    CrRolesDTO crRolesDTO = Mockito.spy(CrRolesDTO.class);
    crRolesDTO.setListRoleName(lstRoleName);
    PowerMockito.when(crRolesRepository.addOrEdit(any())).thenReturn(resultInSideDto);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.CR_MANAGER_ROLE", 1L, lstRoleName))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crRolesBusiness.addCrRoles(crRolesDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testEditCrRoles_01() throws Exception {
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
    List<LanguageExchangeDTO> lstRoleName = Mockito.spy(ArrayList.class);
    CrRolesDTO crRolesDTO = Mockito.spy(CrRolesDTO.class);
    crRolesDTO.setListRoleName(lstRoleName);
    crRolesDTO.setCmreId(1L);
    PowerMockito.when(crRolesRepository.addOrEdit(any())).thenReturn(resultInSideDto);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.CR_MANAGER_ROLE", 1L, lstRoleName))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crRolesBusiness.updateCrRoles(crRolesDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testDeleteCrRoles_01() throws Exception {
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
    PowerMockito.when(crRolesRepository.deleteCrRoles(anyLong())).thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crRolesBusiness.deleteCrRoles(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

}
