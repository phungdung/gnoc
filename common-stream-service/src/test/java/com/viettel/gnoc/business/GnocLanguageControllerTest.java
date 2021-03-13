package com.viettel.gnoc.business;

import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.repository.GnocLanguageRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GnocLanguageBusinessImpl.class})
public class GnocLanguageControllerTest {

  @InjectMocks
  GnocLanguageBusinessImpl gnocLanguageBusiness;

  @Mock
  GnocLanguageRepository gnocLanguageRepository;

  @Test
  public void testInsertGnocLanguageDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = gnocLanguageBusiness.insertGnocLanguageDTO(any());
    PowerMockito.when(gnocLanguageRepository.insertGnocLanguageDTO(any()))
        .thenReturn(resultInSideDto);
    Assert.assertEquals(resultInSideDto, null);
  }

  @Test
  public void testUpdateGnocLanguageDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = gnocLanguageBusiness.updateGnocLanguageDTO(any());
    PowerMockito.when(gnocLanguageRepository.updateGnocLanguageDTO(any())).thenReturn(result);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testDeleteGnocLanguageById_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = gnocLanguageBusiness.deleteGnocLanguageById(any());
    PowerMockito.when(gnocLanguageRepository.deleteGnocLanguageById(any())).thenReturn(result);
    Assert.assertEquals(result, null);
  }

  @Test
  public void testGetListGnocLanguage_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = gnocLanguageBusiness.getListGnocLanguage(any());
    PowerMockito.when(gnocLanguageRepository.getListGnocLanguage(any())).thenReturn(datatable);
    Assert.assertEquals(datatable, null);
  }

  @Test
  public void testFindGnocLanguageId_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    GnocLanguageDto gnocLanguageDto = gnocLanguageBusiness.findGnocLanguageId(any());
    PowerMockito.when(gnocLanguageRepository.findGnocLanguageId(any())).thenReturn(gnocLanguageDto);
    Assert.assertEquals(gnocLanguageDto, null);
  }
}
