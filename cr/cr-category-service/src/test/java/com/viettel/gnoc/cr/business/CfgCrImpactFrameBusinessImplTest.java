package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.repository.CfgCrImpactFrameRepository;
import java.util.ArrayList;
import java.util.List;
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
@PrepareForTest({CfgCrImpactFrameBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgCrImpactFrameBusinessImplTest {

  @InjectMocks
  CfgCrImpactFrameBusinessImpl cfgCrImpactFrameBusiness;
  @Mock
  LanguageExchangeRepository languageExchangeRepository;
  @Mock
  CfgCrImpactFrameRepository cfgCrImpactFrameRepository;

  @Test
  public void findCrImpactFrameById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrImpactFrameInsiteDTO crImpactFrameDTO = Mockito.spy(CrImpactFrameInsiteDTO.class);
    PowerMockito.when(cfgCrImpactFrameRepository.findCrImpactFrameById(anyLong()))
        .thenReturn(crImpactFrameDTO);
    List<LanguageExchangeDTO> getListLanguageExchangeById = Mockito.spy(ArrayList.class);
    PowerMockito.when(languageExchangeRepository
        .getListLanguageExchangeById(anyString(), anyString(), anyLong(), anyString()))
        .thenReturn(getListLanguageExchangeById);
    CrImpactFrameInsiteDTO crImpactFrameInsiteDTO = cfgCrImpactFrameBusiness
        .findCrImpactFrameById(anyLong());
    Assert.assertNotNull(crImpactFrameInsiteDTO);
  }

  @Test
  public void insertCrImpactFrame_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgCrImpactFrameRepository.insertCrImpactFrame(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange(anyString(), anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    CrImpactFrameInsiteDTO crImpactFrameDTO = Mockito.spy(CrImpactFrameInsiteDTO.class);
    ResultInSideDto resultInSideDto1 = cfgCrImpactFrameBusiness
        .insertCrImpactFrame(crImpactFrameDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteCrImpactFrameById() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        languageExchangeRepository.deleteListLanguageExchange(anyString(), anyString(), anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(cfgCrImpactFrameRepository.deleteCrImpactFrameById(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgCrImpactFrameBusiness.deleteCrImpactFrameById(1L);
    Assert.assertEquals(resultInSideDto.getKey(), res);
  }

  @Test
  public void updateCrImpactFrame_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgCrImpactFrameRepository.updateCrImpactFrame(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange(anyString(), anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    CrImpactFrameInsiteDTO dto = Mockito.spy(CrImpactFrameInsiteDTO.class);
    ResultInSideDto res = cfgCrImpactFrameBusiness.updateCrImpactFrame(dto);
    Assert.assertEquals(resultInSideDto.getKey(), res.getKey());
  }

  @Test
  public void getListCrImpactFrame_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgCrImpactFrameRepository.getListCrImpactFrame(any())).thenReturn(datatable);
    CrImpactFrameInsiteDTO dto = Mockito.spy(CrImpactFrameInsiteDTO.class);
    Datatable datatable1 = cfgCrImpactFrameBusiness.getListCrImpactFrame(dto);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }
}
