package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoUpdateServiceInfraDTO;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoUpdateServiceInfraRepository;
import lombok.extern.slf4j.Slf4j;
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
@PrepareForTest({WoUpdateServiceInfraBusinessImpl.class, I18n.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
@Slf4j
public class WoUpdateServiceInfraBusinessImplTest {

  @InjectMocks
  WoUpdateServiceInfraBusinessImpl woUpdateServiceInfraBusiness;

  @Mock
  WoUpdateServiceInfraRepository woUpdateServiceInfraRepository;

  @Mock
  protected WoDetailRepository woDetailRepository;

  @Test
  public void testGetListWoUpdateServiceInfraPage_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(woUpdateServiceInfraRepository.getListWoUpdateServiceInfraPage(any()))
        .thenReturn(datatable);
    Datatable datatable1 = woUpdateServiceInfraBusiness
        .getListWoUpdateServiceInfraPage(any());
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void testUpdate_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    WoUpdateServiceInfraDTO woUpdateServiceInfraDTO = Mockito.spy(WoUpdateServiceInfraDTO.class);
    woUpdateServiceInfraDTO.setWoId(10L);
    woUpdateServiceInfraDTO.setServiceId(123L);
    woUpdateServiceInfraDTO.setInfraType(45L);

    WoDetailDTO woDetailDTO = Mockito.spy(WoDetailDTO.class);
    woDetailDTO.setServiceId(woUpdateServiceInfraDTO.getServiceId());
    woDetailDTO.setInfraType(woUpdateServiceInfraDTO.getInfraType());

    PowerMockito.when(woDetailRepository.findById(anyLong())).thenReturn(woDetailDTO);
    PowerMockito.when(woDetailRepository.update(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woUpdateServiceInfraBusiness.update(woUpdateServiceInfraDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void testUpdate_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    WoUpdateServiceInfraDTO woUpdateServiceInfraDTO = Mockito.spy(WoUpdateServiceInfraDTO.class);
    woUpdateServiceInfraDTO.setWoId(10L);
    resultInSideDto.setKey(RESULT.FAIL);
    PowerMockito.when(woDetailRepository.findById(anyLong())).thenReturn(null);
    ResultInSideDto resultInSideDto1 = woUpdateServiceInfraBusiness.update(woUpdateServiceInfraDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

}
