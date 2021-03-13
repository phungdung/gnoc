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
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.repository.CrImpactSegmentRepository;
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
@PrepareForTest({CrImpactSegmentBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, CommonExport.class,
    TicketProvider.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrImpactSegmentBusinessTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  TicketProvider ticketProvider;

  @Mock
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  protected LanguageExchangeRepository languageExchangeRepository;

  @InjectMocks
  protected CrImpactSegmentBusinessImpl crImpactSegmentBusiness;

  @Mock
  protected CrImpactSegmentRepository crImpactSegmentRepository;

  @Test
  public void testGetListCrImpactSegment_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(crImpactSegmentRepository.getListImpactSegment(any())).thenReturn(datatable);
    Datatable result = crImpactSegmentBusiness.getListCrImpactSegment(new ImpactSegmentDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void testGetDetailCrImpactSegment_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ImpactSegmentDTO impactSegmentDTO = Mockito.spy(ImpactSegmentDTO.class);
    when(crImpactSegmentRepository.getDetail(any())).thenReturn(impactSegmentDTO);
    ImpactSegmentDTO result = crImpactSegmentBusiness.getDetail(1L);
    assertEquals(impactSegmentDTO, result);
  }

  @Test
  public void testAddCrImpactSegment_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
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
    List<LanguageExchangeDTO> lstImpactSegmentName = Mockito.spy(ArrayList.class);
    ImpactSegmentDTO impactSegmentDTO = Mockito.spy(ImpactSegmentDTO.class);
    impactSegmentDTO.setListImpactSegmentName(lstImpactSegmentName);
    PowerMockito.when(crImpactSegmentRepository.addOrEditImpactSegment(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange("COMMON_GNOC", "OPEN_PM.IMPACT_SEGMENT", 1L,
            lstImpactSegmentName))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crImpactSegmentBusiness.addCrImpactSegment(impactSegmentDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testEditCrImpactSegment_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
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
    List<LanguageExchangeDTO> lstImpactSegmentName = Mockito.spy(ArrayList.class);
    ImpactSegmentDTO impactSegmentDTO = Mockito.spy(ImpactSegmentDTO.class);
    impactSegmentDTO.setListImpactSegmentName(lstImpactSegmentName);
    impactSegmentDTO.setImpactSegmentId(1L);
    PowerMockito.when(crImpactSegmentRepository.addOrEditImpactSegment(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange("COMMON_GNOC", "OPEN_PM.IMPACT_SEGMENT", 1L,
            lstImpactSegmentName))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crImpactSegmentBusiness
        .updateCrImpactSegment(impactSegmentDTO);
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
    PowerMockito.when(crImpactSegmentRepository.deleteImpactSegment(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crImpactSegmentBusiness.deleteCrImpactSegment(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testExportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ImpactSegmentDTO> dtoList = new ArrayList<>();
    ImpactSegmentDTO dto = new ImpactSegmentDTO();
    dto.setImpactSegmentId(1L);
    dto.setImpactSegmentCode("impactSegmentCode");
    dto.setImpactSegmentName("impactSegmentName");
    dto.setAppliedSystem(1L);
    dto.setIsActive(1L);
    dto.setAppliedSystemName("appliedSystemName");
    PowerMockito.mockStatic(I18n.class);
    dtoList.add(dto);
    when(crImpactSegmentRepository.getListDataExport(any()))
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

    File result = crImpactSegmentBusiness.exportData(dto);
    Assert.assertNull(result);
  }

  @Test
  public void deleteListCrImpactSegment_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    ImpactSegmentDTO impactSegmentDTO = Mockito.spy(ImpactSegmentDTO.class);
    List<Long> listId = Mockito.spy(ArrayList.class);
    listId.add(1L);
    impactSegmentDTO.setListId(listId);
    PowerMockito.when(crImpactSegmentRepository.deleteImpactSegment(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crImpactSegmentBusiness
        .deleteListCrImpactSegment(impactSegmentDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteListCrImpactSegment_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    ImpactSegmentDTO impactSegmentDTO = Mockito.spy(ImpactSegmentDTO.class);
    List<Long> listId = Mockito.spy(ArrayList.class);
    impactSegmentDTO.setListId(listId);
    PowerMockito.when(crImpactSegmentRepository.deleteImpactSegment(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crImpactSegmentBusiness
        .deleteListCrImpactSegment(impactSegmentDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.NODATA);
  }

}
