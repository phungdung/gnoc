package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.TempImportColDTO;
import com.viettel.gnoc.cr.repository.CfgTempImportColRepository;
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
@PrepareForTest({CfgTempImportColBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgTempImportColBusinessImplTest {

  @InjectMocks
  CfgTempImportColBusinessImpl cfgTempImportColBusiness;
  @Mock
  CfgTempImportColRepository cfgTempImportColRepository;

  @Test
  public void findTempImportColById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    TempImportColDTO dto = Mockito.spy(TempImportColDTO.class);
    PowerMockito.when(cfgTempImportColRepository.findTempImportColById(anyLong())).thenReturn(dto);
    TempImportColDTO dto1 = cfgTempImportColBusiness.findTempImportColById(1L);
    Assert.assertNotNull(dto1);
  }

  @Test
  public void insertTempImportCol_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgTempImportColRepository.insertTempImportCol(any()))
        .thenReturn(resultInSideDto);
    TempImportColDTO dto = Mockito.spy(TempImportColDTO.class);
    ResultInSideDto resultInSideDto1 = cfgTempImportColBusiness.insertTempImportCol(dto);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteTempImportColById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgTempImportColRepository.deleteTempImportColById(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgTempImportColBusiness.deleteTempImportColById(1L);
    Assert.assertEquals(resultInSideDto.getKey(), res);
  }

  @Test
  public void updateTempImportCol_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgTempImportColRepository.updateTempImportCol(any()))
        .thenReturn(RESULT.SUCCESS);
    TempImportColDTO dto = Mockito.spy(TempImportColDTO.class);
    String res = cfgTempImportColBusiness.updateTempImportCol(dto);
    Assert.assertEquals(resultInSideDto.getKey(), res);
  }

  @Test
  public void getListTempImportCol_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgTempImportColRepository.getListTempImportCol(any())).thenReturn(datatable);
    TempImportColDTO dto = Mockito.spy(TempImportColDTO.class);
    Datatable datatable1 = cfgTempImportColBusiness.getListTempImportCol(dto);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }
}
