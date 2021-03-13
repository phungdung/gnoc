package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.ReturnCodeCatalogDTO;
import com.viettel.gnoc.cr.repository.CfgReturnCodeCatalogRepository;
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
@PrepareForTest({CfgReturnCodeCatalogBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgReturnCodeCatalogBusinessImplTest {

  @InjectMocks
  CfgReturnCodeCatalogBusinessImpl cfgReturnCodeCatalogBusiness;
  @Mock
  CfgReturnCodeCatalogRepository cfgReturnCodeCatalogRepository;

  @Test
  public void findCfgReturnCodeCatalogById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    ReturnCodeCatalogDTO returnCodeCatalogDTO = Mockito.spy(ReturnCodeCatalogDTO.class);
    PowerMockito.when(cfgReturnCodeCatalogRepository.findCfgReturnCodeCatalogById(anyLong()))
        .thenReturn(returnCodeCatalogDTO);
    ReturnCodeCatalogDTO returnCodeCatalogDTO1 = cfgReturnCodeCatalogBusiness
        .findCfgReturnCodeCatalogById(1L);
    Assert.assertNotNull(returnCodeCatalogDTO1);
  }

  @Test
  public void insertCfgReturnCodeCatalog_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgReturnCodeCatalogRepository.insertCfgReturnCodeCatalog(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgReturnCodeCatalogBusiness
        .insertCfgReturnCodeCatalog(any());
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteCfgReturnCodeCatalogById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgReturnCodeCatalogRepository.deleteCfgReturnCodeCatalogById(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgReturnCodeCatalogBusiness.deleteCfgReturnCodeCatalogById(anyLong());
    Assert.assertEquals(resultInSideDto.getKey(), res);
  }

  @Test
  public void updateCfgReturnCodeCatalog_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgReturnCodeCatalogRepository.updateCfgReturnCodeCatalog(any()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgReturnCodeCatalogBusiness.updateCfgReturnCodeCatalog(any());
    Assert.assertEquals(resultInSideDto.getKey(), res);
  }

  @Test
  public void getListReturnCodeCatalog_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgReturnCodeCatalogRepository.getListReturnCodeCatalog(any()))
        .thenReturn(datatable);
    ReturnCodeCatalogDTO dto = Mockito.spy(ReturnCodeCatalogDTO.class);
    Datatable datatable1 = cfgReturnCodeCatalogBusiness.getListReturnCodeCatalog(dto);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getListReturnCategory_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<ReturnCodeCatalogDTO> returnCodeCatalogDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(cfgReturnCodeCatalogRepository.getListReturnCategory())
        .thenReturn(returnCodeCatalogDTOS);
    List<ReturnCodeCatalogDTO> returnCodeCatalogDTOS1 = cfgReturnCodeCatalogBusiness
        .getListReturnCategory();
    Assert.assertEquals(returnCodeCatalogDTOS1.size(), 0L);
  }
}
