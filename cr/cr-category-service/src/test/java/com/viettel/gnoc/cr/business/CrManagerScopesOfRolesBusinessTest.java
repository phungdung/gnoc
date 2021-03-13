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
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrManagerScopesOfRolesDTO;
import com.viettel.gnoc.cr.repository.CrManagerScopesOfRolesRepository;
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
@PrepareForTest({CrManagerScopesOfRolesBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, CommonExport.class,
    TicketProvider.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrManagerScopesOfRolesBusinessTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  TicketProvider ticketProvider;

  @Mock
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @InjectMocks
  protected CrManagerScopesOfRolesBusinessImpl crManagerScopesOfRolesBusiness;

  @Mock
  protected CrManagerScopesOfRolesRepository crManagerScopesOfRolesRepository;

  @Test
  public void testGetCrManagerScopesOfRoles_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(crManagerScopesOfRolesRepository.getListManagerScopesOfRoles(any())).thenReturn(datatable);

    Datatable result = crManagerScopesOfRolesBusiness
        .getListManagerScopesOfRoles(new CrManagerScopesOfRolesDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void testGetDetailCrManagerScopesOfRoles_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO = Mockito
        .spy(CrManagerScopesOfRolesDTO.class);
    when(crManagerScopesOfRolesRepository.getDetail(any())).thenReturn(crManagerScopesOfRolesDTO);
    CrManagerScopesOfRolesDTO result = crManagerScopesOfRolesBusiness.getDetail(1L);
    assertEquals(crManagerScopesOfRolesDTO, result);
  }

  @Test
  public void testAddCrManagerScopesOfRoles_01() throws Exception {
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
    CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO = Mockito
        .spy(CrManagerScopesOfRolesDTO.class);
    PowerMockito.when(crManagerScopesOfRolesRepository.addOrEdit(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crManagerScopesOfRolesBusiness
        .addManagerScopesOfRoles(crManagerScopesOfRolesDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testEditCrManagerScopesOfRoles_01() throws Exception {
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
    CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO = Mockito
        .spy(CrManagerScopesOfRolesDTO.class);
    crManagerScopesOfRolesDTO.setCmsorsId(1L);
    PowerMockito.when(crManagerScopesOfRolesRepository.addOrEdit(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crManagerScopesOfRolesBusiness
        .updateManagerScopesOfRoles(crManagerScopesOfRolesDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testDeleteCrManagerScopesOfRoles_01() throws Exception {
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
    PowerMockito.when(crManagerScopesOfRolesRepository.deleteManagerScopesOfRoles(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crManagerScopesOfRolesBusiness
        .deleteManagerScopesOfRoles(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testExportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrManagerScopesOfRolesDTO> dtoList = new ArrayList<>();
    CrManagerScopesOfRolesDTO dto = Mockito.spy(CrManagerScopesOfRolesDTO.class);

    dto.setCmreCode("cmreCode");
    dto.setCmreName("cmreName");
    dto.setCmseCode("cmseCode");
    dto.setCmseName("cmseName");
    PowerMockito.mockStatic(I18n.class);
    dtoList.add(dto);
    when(crManagerScopesOfRolesRepository.getListDataExport(any()))
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

    File result = crManagerScopesOfRolesBusiness.exportData(new CrManagerScopesOfRolesDTO());
    Assert.assertNull(result);
  }

}
