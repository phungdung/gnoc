package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.TemplateRelationsDTO;
import com.viettel.gnoc.cr.repository.CfgTemplateRelationsRepository;
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
@PrepareForTest({CfgTemplateRelationsBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgTemplateRelationsBusinessImplTest {

  @InjectMocks
  CfgTemplateRelationsBusinessImpl cfgTemplateRelationsBusiness;
  @Mock
  CfgTemplateRelationsRepository cfgTemplateRelationsRepository;

  @Test
  public void updateTemplateRelations_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    TemplateRelationsDTO dto = Mockito.spy(TemplateRelationsDTO.class);
    PowerMockito.when(cfgTemplateRelationsRepository.updateTemplateRelations(any()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgTemplateRelationsBusiness.updateTemplateRelations(dto);
    Assert.assertEquals(RESULT.SUCCESS, res);
  }

  @Test
  public void findTemplateRelationsById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    TemplateRelationsDTO templateRelationsDTO = Mockito.spy(TemplateRelationsDTO.class);
    PowerMockito.when(cfgTemplateRelationsRepository.findTemplateRelationsById(anyLong()))
        .thenReturn(templateRelationsDTO);
    TemplateRelationsDTO templateRelationsDTO1 = cfgTemplateRelationsBusiness
        .findTemplateRelationsById(1L);
    Assert.assertNotNull(templateRelationsDTO1);
  }

  @Test
  public void insertTemplateRelations_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    TemplateRelationsDTO templateRelationsDTO = Mockito.spy(TemplateRelationsDTO.class);
    PowerMockito.when(cfgTemplateRelationsRepository.insertTemplateRelations(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = cfgTemplateRelationsBusiness
        .insertTemplateRelations(templateRelationsDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteTemplateRelationsById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.when(cfgTemplateRelationsRepository.deleteTemplateRelationsById(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    String res = cfgTemplateRelationsBusiness.deleteTemplateRelationsById(1L);
    Assert.assertEquals(RESULT.SUCCESS, res);
  }

  @Test
  public void getListTemplateRelations_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    TemplateRelationsDTO templateRelationsDTO = Mockito.spy(TemplateRelationsDTO.class);
    PowerMockito.when(cfgTemplateRelationsRepository.getListTemplateRelations(any()))
        .thenReturn(datatable);
    Datatable datatable1 = cfgTemplateRelationsBusiness
        .getListTemplateRelations(templateRelationsDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }
}
