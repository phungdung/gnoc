package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.MethodParameterDTO;
import com.viettel.gnoc.cr.dto.TempImportColDTO;
import com.viettel.gnoc.cr.dto.TempImportDTO;
import com.viettel.gnoc.cr.repository.CfgTempImportColRepository;
import com.viettel.gnoc.cr.repository.CfgTempImportRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgTempImportBusinessImpl.class, TicketProvider.class, I18n.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgTempImportBusinessImplTest {

  @InjectMocks
  CfgTempImportBusinessImpl cfgTempImportBusiness;
  @Mock
  CfgTempImportRepository cfgTempImportRepository;
  @Mock
  LanguageExchangeRepository languageExchangeRepository;
  @Mock
  TicketProvider ticketProvider;
  @Mock
  CfgTempImportColRepository cfgTempImportColRepository;
  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;
  @Mock
  UnitRepository unitRepository;
  @Mock
  GnocFileRepository gnocFileRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(cfgTempImportBusiness, "uploadFolder",
        "./cr-upload");
  }

  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(cfgTempImportBusiness, "tempFolder",
        "./cr-upload");
  }

  @Test
  public void getListTempImport_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    TempImportDTO dto = Mockito.spy(TempImportDTO.class);
    PowerMockito.when(cfgTempImportRepository.getListTempImport(any())).thenReturn(datatable);
    Datatable datatable1 = cfgTempImportBusiness.getListTempImport(dto);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void insertTempImport_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitDTO);
    PowerMockito.when(cfgTempImportRepository.insertTempImport(any())).thenReturn(resultInSideDto);
    PowerMockito.when(cfgTempImportColRepository.insertTempImportCol(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
        "text/plain", "Spring Framework".getBytes());
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(multipartFile);
    TempImportColDTO dto = Mockito.spy(TempImportColDTO.class);
    List<TempImportColDTO> tempImportColDTOS = Mockito.spy(ArrayList.class);
    tempImportColDTOS.add(dto);
    TempImportDTO tempImportDTO = Mockito.spy(TempImportDTO.class);
    tempImportDTO.setTempImportColDTOS(tempImportColDTOS);
    LanguageExchangeDTO languageExchangeDTO = Mockito.spy(LanguageExchangeDTO.class);
    languageExchangeDTO.setFileName("input.txt");
    languageExchangeDTO.setLeeValue("1");
    languageExchangeDTO.setLeeLocale("en_US");
    List<LanguageExchangeDTO> listName = Mockito.spy(ArrayList.class);
    listName.add(languageExchangeDTO);
    tempImportDTO.setListName(listName);
    resultInSideDto.setObject(listName);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange(anyString(), anyString(), anyLong(),
            anyList())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgTempImportBusiness.insertTempImport(files, tempImportDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getMethodPrameter_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<MethodParameterDTO> methodParameterDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(cfgTempImportRepository.getMethodPrameter()).thenReturn(methodParameterDTOS);
    List<MethodParameterDTO> methodParameterDTOS1 = cfgTempImportBusiness.getMethodPrameter();
    Assert.assertEquals(methodParameterDTOS.size(), methodParameterDTOS1.size());
  }

  @Test
  public void exportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Hoạt động");
    List<TempImportDTO> lstTempImportEx = Mockito.spy(ArrayList.class);
    TempImportDTO tempImportExport = Mockito.spy(TempImportDTO.class);
    tempImportExport.setIsActive(1L);
    lstTempImportEx.add(tempImportExport);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstTempImportEx);
    PowerMockito.when(cfgTempImportRepository.getListDataExport(any())).thenReturn(datatable);
    File file = cfgTempImportBusiness.exportData(tempImportExport);
    Assert.assertNotNull(file);
  }
}
