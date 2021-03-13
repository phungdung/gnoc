package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.CrActionCodeDTO;
import com.viettel.gnoc.cr.repository.CfgCrActionCodeRepository;
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
@PrepareForTest({CfgCrActionCodeBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgCrActionCodeBusinessImplTest {

  @InjectMocks
  CfgCrActionCodeBusinessImpl cfgCrActionCodeBusiness;
  @Mock
  CfgCrActionCodeRepository cfgCrActionCodeRepository;

  @Test
  public void findCrActionCodeById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrActionCodeDTO crActionCodeDTO = Mockito.spy(CrActionCodeDTO.class);
    PowerMockito.when(cfgCrActionCodeRepository.findCrActionCodeById(any()))
        .thenReturn(crActionCodeDTO);
    CrActionCodeDTO crActionCodeDTO1 = cfgCrActionCodeBusiness.findCrActionCodeById(1L);
    Assert.assertNotNull(crActionCodeDTO1);
  }

  @Test
  public void insertCfgCrActionCode_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgCrActionCodeRepository.insertCfgCrActionCode(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgCrActionCodeBusiness.insertCfgCrActionCode(any());
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteCfgCrActionCodeById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgCrActionCodeRepository.deleteCfgCrActionCodeById(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgCrActionCodeBusiness.deleteCfgCrActionCodeById(1L);
    Assert.assertEquals(resultInSideDto.getKey(), res);
  }

  @Test
  public void updateCfgCrActionCode_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgCrActionCodeRepository.updateCfgCrActionCode(any()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgCrActionCodeBusiness.updateCfgCrActionCode(any());
    Assert.assertEquals(resultInSideDto.getKey(), res);
  }

  @Test
  public void getListCfgCrActionCode_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgCrActionCodeRepository.getListCfgCrActionCode(any()))
        .thenReturn(datatable);
    Datatable datatable1 = cfgCrActionCodeBusiness.getListCfgCrActionCode(any());
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }
}
