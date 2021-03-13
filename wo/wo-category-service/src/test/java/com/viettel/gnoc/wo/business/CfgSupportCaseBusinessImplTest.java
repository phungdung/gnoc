package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.CfgSupportCaseDTO;
import com.viettel.gnoc.wo.dto.CfgSupportCaseTestDTO;
import com.viettel.gnoc.wo.repository.CfgSupportCaseRepository;
import com.viettel.gnoc.wo.repository.CfgSupportCaseTestRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgSupportCaseBusinessImpl.class, I18n.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgSupportCaseBusinessImplTest {

  @InjectMocks
  CfgSupportCaseBusinessImpl cfgSupportCaseBusiness;
  @Mock
  CfgSupportCaseRepository cfgSupportCaseRepository;
  @Mock
  CfgSupportCaseTestRepository cfgSupportCaseTestRepository;

  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(cfgSupportCaseBusiness, "tempFolder",
        "./wo-upload");
  }

  @Test
  public void deleteCaseAndCaseTest_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgSupportCaseRepository.deleteCaseAndCaseTest(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgSupportCaseBusiness.deleteCaseAndCaseTest(1L);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void add_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgSupportCaseRepository.add(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgSupportCaseBusiness.add(any());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void edit_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgSupportCaseRepository.edit(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgSupportCaseBusiness.edit(any());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void getDetail_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgSupportCaseDTO cfgSupportCaseDTO = Mockito.spy(CfgSupportCaseDTO.class);
    PowerMockito.when(cfgSupportCaseRepository.getDetail(anyLong())).thenReturn(cfgSupportCaseDTO);
    CfgSupportCaseDTO cfgSupportCaseDTO1 = cfgSupportCaseBusiness.getDetail(1L);
    Assert.assertNotNull(cfgSupportCaseDTO1);
  }

  @Test
  public void getListCfgSupportCaseDTONew_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgSupportCaseRepository.getListCfgSupportCaseDTONew(any()))
        .thenReturn(datatable);
    Datatable datatable1 = cfgSupportCaseBusiness.getListCfgSupportCaseDTONew(any());
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void exportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Báo cáo cấu hình case lỗi");
    List<CfgSupportCaseDTO> list = Mockito.spy(ArrayList.class);
    CfgSupportCaseDTO cfgSupportCaseDTO = Mockito.spy(CfgSupportCaseDTO.class);
    list.add(cfgSupportCaseDTO);
    PowerMockito.when(cfgSupportCaseRepository.getListCfgSupportCaseExport(any())).thenReturn(list);
    File file = cfgSupportCaseBusiness.exportData(any());
    Assert.assertNotNull(file);
  }

  @Test
  public void getListCfgSupportCaseTestId_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CfgSupportCaseTestDTO> cfgSupportCaseTestDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(cfgSupportCaseTestRepository.getListCfgSupportCaseTestId(anyLong()))
        .thenReturn(cfgSupportCaseTestDTOS);
    List<CfgSupportCaseTestDTO> cfgSupportCaseTestDTOS1 = cfgSupportCaseBusiness
        .getListCfgSupportCaseTestId(1L);
    Assert.assertEquals(cfgSupportCaseTestDTOS.size(), cfgSupportCaseTestDTOS1.size());
  }
}
