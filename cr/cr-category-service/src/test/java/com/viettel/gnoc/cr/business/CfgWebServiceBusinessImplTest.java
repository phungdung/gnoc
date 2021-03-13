package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.WebServiceDTO;
import com.viettel.gnoc.cr.dto.WebServiceMethodDTO;
import com.viettel.gnoc.cr.repository.CfgWebServiceMethodRepository;
import com.viettel.gnoc.cr.repository.CfgWebServiceRepository;
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
@PrepareForTest({CfgWebServiceBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgWebServiceBusinessImplTest {

  @InjectMocks
  CfgWebServiceBusinessImpl cfgWebServiceBusiness;
  @Mock
  CfgWebServiceRepository cfgWebServiceRepository;
  @Mock
  CfgWebServiceMethodRepository cfgWebServiceMethodRepository;

  @Test
  public void findWebServiceById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WebServiceDTO webServiceDTO = Mockito.spy(WebServiceDTO.class);
    PowerMockito.when(cfgWebServiceMethodRepository.findWebServiceById(anyLong()))
        .thenReturn(webServiceDTO);
    WebServiceDTO webServiceDTO1 = cfgWebServiceBusiness.findWebServiceById(1L);
    Assert.assertNotNull(webServiceDTO1);
  }

  @Test
  public void insertWebService_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    PowerMockito.when(cfgWebServiceRepository.insertWebService(any())).thenReturn(resultInSideDto);
    WebServiceDTO webServiceDTO = Mockito.spy(WebServiceDTO.class);
    List<WebServiceMethodDTO> webServiceMethodDTOS = Mockito.spy(ArrayList.class);
    WebServiceMethodDTO dto = Mockito.spy(WebServiceMethodDTO.class);
    webServiceMethodDTOS.add(dto);
    webServiceDTO.setWebServiceMethodDTOS(webServiceMethodDTOS);
    PowerMockito.when(cfgWebServiceMethodRepository.insertWebServiceMethod(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgWebServiceBusiness.insertWebService(webServiceDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteWebServiceById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<WebServiceMethodDTO> webServiceMethodDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(cfgWebServiceMethodRepository.getWebServiceMethodDTOS(anyLong()))
        .thenReturn(webServiceMethodDTOS);
    WebServiceMethodDTO dto = Mockito.spy(WebServiceMethodDTO.class);
    webServiceMethodDTOS.add(dto);
    PowerMockito.when(cfgWebServiceMethodRepository.getWebServiceMethodDTOS(anyLong()))
        .thenReturn(webServiceMethodDTOS);
    PowerMockito.when(cfgWebServiceMethodRepository.deleteWebServiceMethodById(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    PowerMockito.when(cfgWebServiceRepository.deleteWebServiceById(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgWebServiceBusiness.deleteWebServiceById(1L);
    Assert.assertEquals(RESULT.SUCCESS, res);
  }

  @Test
  public void updateWebService_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WebServiceDTO webServiceDTO = Mockito.spy(WebServiceDTO.class);
    webServiceDTO.setWebServiceId(1L);
    List<WebServiceMethodDTO> webServiceMethodDTOS = Mockito.spy(ArrayList.class);
    WebServiceMethodDTO dto = Mockito.spy(WebServiceMethodDTO.class);
    webServiceMethodDTOS.add(dto);
    webServiceDTO.setWebServiceMethodDTOS(webServiceMethodDTOS);
    PowerMockito.when(cfgWebServiceMethodRepository.deleteListWebServiceMethodByWebServiceId(any()))
        .thenReturn(RESULT.SUCCESS);
    PowerMockito.when(cfgWebServiceMethodRepository.insertWebServiceMethod(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(cfgWebServiceRepository.updateWebService(any())).thenReturn(RESULT.SUCCESS);
    String res = cfgWebServiceBusiness.updateWebService(webServiceDTO);
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void getListWebService_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgWebServiceRepository.getListWebService(any())).thenReturn(datatable);
    WebServiceDTO dto = Mockito.spy(WebServiceDTO.class);
    Datatable datatable1 = cfgWebServiceBusiness.getListWebService(dto);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }
}
