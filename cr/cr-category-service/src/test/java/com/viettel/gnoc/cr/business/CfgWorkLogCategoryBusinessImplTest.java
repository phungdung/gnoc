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
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.cr.repository.CfgWorkLogCategoryRepository;
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
@PrepareForTest({CfgWorkLogCategoryBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgWorkLogCategoryBusinessImplTest {

  @InjectMocks
  CfgWorkLogCategoryBusinessImpl cfgWorkLogCategoryBusiness;
  @Mock
  LanguageExchangeRepository languageExchangeRepository;
  @Mock
  CfgWorkLogCategoryRepository cfgWorkLogCategoryRepository;

  @Test
  public void findWorkLogCategoryById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WorkLogCategoryInsideDTO workLogCategoryDTO = Mockito.spy(WorkLogCategoryInsideDTO.class);
    PowerMockito.when(cfgWorkLogCategoryRepository.findWorkLogCategoryById(anyLong()))
        .thenReturn(workLogCategoryDTO);
    List<LanguageExchangeDTO> getListLanguageExchangeById = Mockito.spy(ArrayList.class);
    PowerMockito.when(languageExchangeRepository
        .getListLanguageExchangeById(anyString(), anyString(), anyLong(), anyString()))
        .thenReturn(getListLanguageExchangeById);
    WorkLogCategoryInsideDTO workLogCategoryDTO1 = cfgWorkLogCategoryBusiness
        .findWorkLogCategoryById(1L);
    Assert.assertNotNull(workLogCategoryDTO1);
  }

  @Test
  public void insertWorkLogCategory_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgWorkLogCategoryRepository.insertWorkLogCategory(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange(anyString(), anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    WorkLogCategoryInsideDTO workLogCategoryDTO = Mockito.spy(WorkLogCategoryInsideDTO.class);
    ResultInSideDto resultInSideDto1 = cfgWorkLogCategoryBusiness
        .insertWorkLogCategory(workLogCategoryDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteWorkLogCategoryById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        languageExchangeRepository.deleteListLanguageExchange(anyString(), anyString(), anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(cfgWorkLogCategoryRepository.deleteWorkLogCategoryById(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgWorkLogCategoryBusiness.deleteWorkLogCategoryById(1L);
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void updateWorkLogCategory_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgWorkLogCategoryRepository.updateWorkLogCategory(any()))
        .thenReturn(RESULT.SUCCESS);
    PowerMockito.when(languageExchangeRepository
        .saveListLanguageExchange(anyString(), anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    WorkLogCategoryInsideDTO dto = Mockito.spy(WorkLogCategoryInsideDTO.class);
    String res = cfgWorkLogCategoryBusiness.updateWorkLogCategory(dto);
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void getListWorkLogCategory_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgWorkLogCategoryRepository.getListWorkLogCategory(any()))
        .thenReturn(datatable);
    WorkLogCategoryInsideDTO dto = Mockito.spy(WorkLogCategoryInsideDTO.class);
    Datatable datatable1 = cfgWorkLogCategoryBusiness.getListWorkLogCategory(dto);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getListWorkLogType_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<WorkLogCategoryInsideDTO> workLogCategoryInsideDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(cfgWorkLogCategoryRepository.getListWorkLogType())
        .thenReturn(workLogCategoryInsideDTOS);
    List<WorkLogCategoryInsideDTO> workLogCategoryInsideDTOS1 = cfgWorkLogCategoryBusiness
        .getListWorkLogType();
    Assert.assertEquals(workLogCategoryInsideDTOS.size(), workLogCategoryInsideDTOS1.size());
  }
}
