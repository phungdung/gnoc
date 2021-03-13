package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.TempImportDataDTO;
import com.viettel.gnoc.cr.repository.CfgTempImportDataRepository;
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
@PrepareForTest({CfgTempImportDataBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgTempImportDataBusinessImplTest {

  @InjectMocks
  CfgTempImportDataBusinessImpl cfgTempImportDataBusiness;
  @Mock
  CfgTempImportDataRepository cfgTempImportDataRepository;

  @Test
  public void findCfgTempImportDataById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    TempImportDataDTO tempImportDataDTO = Mockito.spy(TempImportDataDTO.class);
    PowerMockito.when(cfgTempImportDataRepository.findCfgTempImportDataById(anyLong()))
        .thenReturn(tempImportDataDTO);
    TempImportDataDTO tempImportDataDTO1 = cfgTempImportDataBusiness.findCfgTempImportDataById(1L);
    Assert.assertNotNull(tempImportDataDTO1);
  }

  @Test
  public void insertTempImportData_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgTempImportDataRepository.insertTempImportData(any()))
        .thenReturn(resultInSideDto);
    TempImportDataDTO tempImportDataDTO = Mockito.spy(TempImportDataDTO.class);
    ResultInSideDto resultInSideDto1 = cfgTempImportDataBusiness
        .insertTempImportData(tempImportDataDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteTempImportDataById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgTempImportDataRepository.deleteTempImportDataById(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgTempImportDataBusiness.deleteTempImportDataById(1L);
    Assert.assertEquals(resultInSideDto.getKey(), res);
  }

  @Test
  public void updateTempImportData_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgTempImportDataRepository.updateTempImportData(any()))
        .thenReturn(RESULT.SUCCESS);
    TempImportDataDTO tempImportDataDTO = Mockito.spy(TempImportDataDTO.class);
    String res = cfgTempImportDataBusiness.updateTempImportData(tempImportDataDTO);
    Assert.assertEquals(resultInSideDto.getKey(), res);
  }

  @Test
  public void getListTempImportData_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgTempImportDataRepository.getListTempImportData(any()))
        .thenReturn(datatable);
    TempImportDataDTO tempImportDataDTO = Mockito.spy(TempImportDataDTO.class);
    Datatable datatable1 = cfgTempImportDataBusiness.getListTempImportData(tempImportDataDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }
}
