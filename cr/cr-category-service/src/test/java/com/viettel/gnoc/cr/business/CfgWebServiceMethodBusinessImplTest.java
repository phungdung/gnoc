package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.WebServiceMethodDTO;
import com.viettel.gnoc.cr.repository.CfgWebServiceMethodRepository;
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
@PrepareForTest({CfgWebServiceMethodBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgWebServiceMethodBusinessImplTest {

  @InjectMocks
  CfgWebServiceMethodBusinessImpl cfgWebServiceMethodBusiness;
  @Mock
  CfgWebServiceMethodRepository cfgWebServiceMethodRepository;

  @Test
  public void findWebServiceMethodById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WebServiceMethodDTO webServiceMethodDTO = Mockito.spy(WebServiceMethodDTO.class);
    PowerMockito.when(cfgWebServiceMethodRepository.findWebServiceMethodById(any()))
        .thenReturn(webServiceMethodDTO);
    WebServiceMethodDTO webServiceMethodDTO1 = cfgWebServiceMethodBusiness
        .findWebServiceMethodById(1L);
    Assert.assertNotNull(webServiceMethodDTO1);
  }

  @Test
  public void insertWebServiceMethod_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WebServiceMethodDTO webServiceMethodDTO = Mockito.spy(WebServiceMethodDTO.class);
    PowerMockito.when(cfgWebServiceMethodRepository.insertWebServiceMethod(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgWebServiceMethodBusiness
        .insertWebServiceMethod(webServiceMethodDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteWebServiceMethodById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgWebServiceMethodRepository.deleteWebServiceMethodById(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgWebServiceMethodBusiness.deleteWebServiceMethodById(1L);
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void updateWebServiceMethod_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WebServiceMethodDTO dto = Mockito.spy(WebServiceMethodDTO.class);
    PowerMockito.when(cfgWebServiceMethodRepository.updateWebServiceMethod(any()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgWebServiceMethodBusiness.updateWebServiceMethod(dto);
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void getListWebServiceMethod_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WebServiceMethodDTO> webServiceMethodDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(cfgWebServiceMethodRepository.getListWebServiceMethod())
        .thenReturn(webServiceMethodDTOS);
    List<WebServiceMethodDTO> webServiceMethodDTOS1 = cfgWebServiceMethodBusiness
        .getListWebServiceMethod();
    Assert.assertEquals(webServiceMethodDTOS.size(), webServiceMethodDTOS1.size());
  }

  @Test
  public void deleteListWebServiceMethodByWebServiceId_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<WebServiceMethodDTO> dto = Mockito.spy(ArrayList.class);
    PowerMockito
        .when(cfgWebServiceMethodRepository.deleteListWebServiceMethodByWebServiceId(anyList()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgWebServiceMethodBusiness.deleteListWebServiceMethodByWebServiceId(dto);
    Assert.assertEquals(res, RESULT.SUCCESS);
  }
}
