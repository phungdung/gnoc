package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WoConfigPropertyDTO;
import com.viettel.gnoc.wo.repository.WoConfigPropertyRepository;
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
@PrepareForTest({WoConfigPropertyBusinessImpl.class, I18n.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
@Slf4j
public class WoConfigPropertyBusinessImplTest {

  @InjectMocks
  WoConfigPropertyBusinessImpl woConfigPropertyBusiness;

  @Mock
  WoConfigPropertyRepository woConfigPropertyRepository;

  @Test
  public void testGetListConfigPropertyDTO_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(woConfigPropertyRepository.getListConfigPropertyDTO(any()))
        .thenReturn(datatable);
    Datatable datatable1 = woConfigPropertyBusiness.getListConfigPropertyDTO(any());
    Assert.assertEquals(datatable.getData(), datatable1.getData());
  }

  @Test
  public void testDelete_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woConfigPropertyRepository.delete(anyString())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woConfigPropertyBusiness.delete("test");
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void testAddConfigProperty_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woConfigPropertyRepository.addConfigProperty(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woConfigPropertyBusiness.addConfigProperty(any());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void testUpdateConfigProperty_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woConfigPropertyRepository.updateConfigProperty(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woConfigPropertyBusiness.updateConfigProperty(any());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void testGetDetail_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    WoConfigPropertyDTO woConfigPropertyDTO = Mockito.spy(WoConfigPropertyDTO.class);
    PowerMockito.when(woConfigPropertyRepository.getDetail(anyString()))
        .thenReturn(woConfigPropertyDTO);
    WoConfigPropertyDTO result = woConfigPropertyBusiness.getDetail("test");
    Assert.assertEquals(woConfigPropertyDTO, result);
  }

}
