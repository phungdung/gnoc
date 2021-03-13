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
import com.viettel.gnoc.cr.dto.CrManagerUnitsOfScopeDTO;
import com.viettel.gnoc.cr.repository.CrManagerUnitsOfScopeRepository;
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
@PrepareForTest({CrManagerUnitsOfScopeBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, CommonExport.class,
    TicketProvider.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrManagerUnitsOfScopeBusinessTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  TicketProvider ticketProvider;

  @Mock
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @InjectMocks
  protected CrManagerUnitsOfScopeBusinessImpl crManagerUnitsOfScopeBusiness;

  @Mock
  protected CrManagerUnitsOfScopeRepository crManagerUnitsOfScopeRepository;

  @Test
  public void testGetCrManagerUnitsOfScope_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(crManagerUnitsOfScopeRepository.getListCrManagerUnitsOfScope(any())).thenReturn(datatable);

    Datatable result = crManagerUnitsOfScopeBusiness
        .getListUnitOfScope(new CrManagerUnitsOfScopeDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void testGetDetailCrManagerUnitsOfScope_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO = Mockito.spy(CrManagerUnitsOfScopeDTO.class);
    when(crManagerUnitsOfScopeRepository.getDetail(any())).thenReturn(crManagerUnitsOfScopeDTO);
    CrManagerUnitsOfScopeDTO result = crManagerUnitsOfScopeBusiness.getDetail(1L);
    assertEquals(crManagerUnitsOfScopeDTO, result);
  }

  @Test
  public void testAddCrManagerUnitsOfScope_01() throws Exception {
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
    CrManagerUnitsOfScopeDTO crManagerScopesOfRolesDTO = Mockito
        .spy(CrManagerUnitsOfScopeDTO.class);
    PowerMockito.when(crManagerUnitsOfScopeRepository.add(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crManagerUnitsOfScopeBusiness
        .addCrManagerUnitsOfScope(crManagerScopesOfRolesDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testEditCrManagerUnitsOfScope_01() throws Exception {
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
    CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO = Mockito.spy(CrManagerUnitsOfScopeDTO.class);
    crManagerUnitsOfScopeDTO.setCmnoseId(1L);
    PowerMockito.when(crManagerUnitsOfScopeRepository.edit(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crManagerUnitsOfScopeBusiness
        .updateCrManagerUnitsOfScope(crManagerUnitsOfScopeDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testDeleteCrManagerUnitsOfScope_01() throws Exception {
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
    PowerMockito.when(crManagerUnitsOfScopeRepository.deleteCrManagerUnitsOfScope(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crManagerUnitsOfScopeBusiness
        .deleteCrManagerUnitsOfScope(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testExportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrManagerUnitsOfScopeDTO> dtoList = new ArrayList<>();
    CrManagerUnitsOfScopeDTO dto = Mockito.spy(CrManagerUnitsOfScopeDTO.class);

    dto.setUnitCode("unitCode");
    dto.setUnitName("unitName");
    dto.setCmseCode("cmseCode");
    dto.setCmseName("cmseName");
    dto.setCrTypeName("crTypeName");
    dto.setDeviceTypeName("deviceTypeName");
    PowerMockito.mockStatic(I18n.class);
    dtoList.add(dto);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(dtoList);
    when(crManagerUnitsOfScopeRepository.getListDataExport(any()))
        .thenReturn(datatable);
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

    File result = crManagerUnitsOfScopeBusiness.exportData(new CrManagerUnitsOfScopeDTO());
    Assert.assertNull(result);
  }
}
