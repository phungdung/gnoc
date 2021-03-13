package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.CfgMapUnitGnocNimsDTO;
import com.viettel.gnoc.wo.repository.CfgMapUnitGnocNimsRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
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
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)

@PrepareForTest({CfgMapUnitGnocNimsBusinessImpl.class, I18n.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgMapUnitGnocNimsBusinessImplTest {

  @InjectMocks
  CfgMapUnitGnocNimsBusinessImpl cfgMapUnitGnocNimsBusiness;
  @Mock
  CfgMapUnitGnocNimsRepository cfgMapUnitGnocNimsRepository;
  @Mock
  CatItemRepository catItemRepository;

  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(cfgMapUnitGnocNimsBusiness, "tempFolder",
        "./wo-upload");
  }

  @Test
  public void getListCfgMapUnitGnocNimsPage_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgMapUnitGnocNimsRepository.getListCfgMapUnitGnocNimsPage(any()))
        .thenReturn(datatable);
    Datatable datatable1 = cfgMapUnitGnocNimsBusiness.getListCfgMapUnitGnocNimsPage(any());
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void delete_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgMapUnitGnocNimsRepository.delete(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgMapUnitGnocNimsBusiness.delete(1L);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void insert_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgMapUnitGnocNimsRepository.add(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgMapUnitGnocNimsBusiness.insert(any());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void update_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgMapUnitGnocNimsRepository.edit(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgMapUnitGnocNimsBusiness.update(any());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void findById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO = Mockito.spy(CfgMapUnitGnocNimsDTO.class);
    PowerMockito.when(cfgMapUnitGnocNimsRepository.findById(anyLong()))
        .thenReturn(cfgMapUnitGnocNimsDTO);
    CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO1 = cfgMapUnitGnocNimsBusiness.findById(1L);
    Assert.assertNotNull(cfgMapUnitGnocNimsDTO1);
  }

  @Test
  public void setMapBusinessName_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable businessMaster = Mockito.spy(Datatable.class);
    List<CatItemDTO> lstBusinessName = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    lstBusinessName.add(catItemDTO);
    businessMaster.setData(lstBusinessName);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(businessMaster);
    cfgMapUnitGnocNimsBusiness.setMapBusinessName();
  }

  @Test
  public void exportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Cấu Hình Map Đơn Vị GNOC-NIMS");
    Datatable datatable = Mockito.spy(Datatable.class);
    CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO = Mockito.spy(CfgMapUnitGnocNimsDTO.class);
    List<CfgMapUnitGnocNimsDTO> cfgMapUnitGnocNimsDTOList = Mockito.spy(ArrayList.class);
    cfgMapUnitGnocNimsDTOList.add(cfgMapUnitGnocNimsDTO);
    datatable.setData(cfgMapUnitGnocNimsDTOList);
    PowerMockito.when(cfgMapUnitGnocNimsRepository.getListDataExport(any())).thenReturn(datatable);
    File file = cfgMapUnitGnocNimsBusiness.exportData(cfgMapUnitGnocNimsDTO);
    Assert.assertNotNull(file);
  }

  @Test
  public void getTemplate_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("STT");
    Datatable businessMaster = Mockito.spy(Datatable.class);
    List<CatItemDTO> businessNameList = Mockito.spy(ArrayList.class);
    CatItemDTO dto = Mockito.spy(CatItemDTO.class);
    dto.setItemName("abc");
    businessNameList.add(dto);
    businessMaster.setData(businessNameList);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(businessMaster);
    File file = cfgMapUnitGnocNimsBusiness.getTemplate();
    Assert.assertNotNull(file);
  }

}
