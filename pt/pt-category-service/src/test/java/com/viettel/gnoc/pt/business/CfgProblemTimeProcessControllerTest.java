package com.viettel.gnoc.pt.business;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.SpringApplicationContext;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.pt.dto.CfgProblemTimeProcessDTO;
import com.viettel.gnoc.pt.model.CfgProblemTimeProcessEntity;
import com.viettel.gnoc.pt.repository.CfgProblemTimeProcessRepository;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgProblemTimeProcessBusinessImpl.class, SpringApplicationContext.class,
    FileUtils.class, CommonExport.class, I18n.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgProblemTimeProcessControllerTest {

  @InjectMocks
  CfgProblemTimeProcessBusinessImpl cfgProblemTimeProcessBusiness;

  @Mock
  CfgProblemTimeProcessRepository cfgProblemTimeProcessRepository;

  @Mock
  CatItemBusiness catItemBusiness;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void testGetCfgProblemTimeProcessByDTO() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(CfgProblemTimeProcessBusinessImpl.class.getDeclaredField("log"), logger);
    CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO = Mockito.spy(CfgProblemTimeProcessDTO.class);
    cfgProblemTimeProcessDTO.setCfgCode("Test");
    PowerMockito.when(cfgProblemTimeProcessRepository.getCfgProblemTimeProcessByDTO(any()))
        .thenReturn(cfgProblemTimeProcessDTO);
    CfgProblemTimeProcessDTO result = cfgProblemTimeProcessBusiness
        .getCfgProblemTimeProcessByDTO(cfgProblemTimeProcessDTO);
    Assert.assertEquals(cfgProblemTimeProcessDTO, result);
  }

  @Test
  public void testExportData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    setFinalStatic(CfgProblemTimeProcessBusinessImpl.class.getDeclaredField("log"), logger);

    CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO = Mockito.spy(CfgProblemTimeProcessDTO.class);
    Datatable datatable = PowerMockito.mock(Datatable.class);
    List<CfgProblemTimeProcessDTO> cfgProblemTimeProcessDTOS = Mockito.spy(ArrayList.class);
    cfgProblemTimeProcessDTO.setCfgCode("Test");
    cfgProblemTimeProcessDTO.setLastUpdateTime(new Date());
    cfgProblemTimeProcessDTO.setId("1");
    cfgProblemTimeProcessDTO.setPriorityCode("Test");
    cfgProblemTimeProcessDTO.setTypeCode("Test");
    cfgProblemTimeProcessDTO.setRcaFoundTime("7");
    cfgProblemTimeProcessDTO.setWaFoundTime("6");
    cfgProblemTimeProcessDTO.setSlFoundTime("5");
    cfgProblemTimeProcessDTOS.add(cfgProblemTimeProcessDTO);
    PowerMockito.when(cfgProblemTimeProcessRepository.getDataTableCfgProblemTimeProcessDTO(any()))
        .thenReturn(datatable);
    PowerMockito.when(datatable.getData()).thenReturn((List) cfgProblemTimeProcessDTOS);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), anyString()))
        .thenReturn(null);
    File result = cfgProblemTimeProcessBusiness.exportData(cfgProblemTimeProcessDTO);
    Assert.assertNull(result);
  }

  @Test
  public void testOnInsert() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(CfgProblemTimeProcessBusinessImpl.class.getDeclaredField("log"), logger);
    CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO = Mockito.spy(CfgProblemTimeProcessDTO.class);
    cfgProblemTimeProcessDTO.setCfgCode("Test");
    cfgProblemTimeProcessDTO.setLastUpdateTime(new Date());
    cfgProblemTimeProcessDTO.setId("1");
    cfgProblemTimeProcessDTO.setPriorityCode("Test");
    cfgProblemTimeProcessDTO.setTypeCode("Test");
    cfgProblemTimeProcessDTO.setRcaFoundTime("5");
    cfgProblemTimeProcessDTO.setWaFoundTime("6");
    cfgProblemTimeProcessDTO.setSlFoundTime("7");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgProblemTimeProcessRepository.onInsert(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = cfgProblemTimeProcessBusiness.onInsert(cfgProblemTimeProcessDTO);
    Assert.assertEquals(result, resultInSideDto);
  }

  @Test
  public void testFindById() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(CfgProblemTimeProcessBusinessImpl.class.getDeclaredField("log"), logger);
    CfgProblemTimeProcessEntity cfgProblemTimeProcessEntity = Mockito
        .spy(CfgProblemTimeProcessEntity.class);
    cfgProblemTimeProcessEntity.setCfgCode("Test");
    cfgProblemTimeProcessEntity.setLastUpdateTime(new Date());
    cfgProblemTimeProcessEntity.setId(999l);
    cfgProblemTimeProcessEntity.setPriorityCode("Test");
    cfgProblemTimeProcessEntity.setTypeCode("Test");
    cfgProblemTimeProcessEntity.setRcaFoundTime(5.0);
    cfgProblemTimeProcessEntity.setWaFoundTime(6.0);
    cfgProblemTimeProcessEntity.setSlFoundTime(7.0);
    PowerMockito.when(cfgProblemTimeProcessRepository.findById(anyLong()))
        .thenReturn(cfgProblemTimeProcessEntity);
    CfgProblemTimeProcessDTO result = cfgProblemTimeProcessBusiness.findById(999l);
    Assert.assertNotNull(result);
  }

  @Test
  public void testGetSequense() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(CfgProblemTimeProcessBusinessImpl.class.getDeclaredField("log"), logger);
    PowerMockito.when(cfgProblemTimeProcessRepository.getSequence()).thenReturn("999");
    String result = cfgProblemTimeProcessBusiness.getSequence();
    Assert.assertEquals(result, "999");
  }

  @Test
  public void testOnDeleteList() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(CfgProblemTimeProcessBusinessImpl.class.getDeclaredField("log"), logger);
    CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO = Mockito.spy(CfgProblemTimeProcessDTO.class);
    List<CfgProblemTimeProcessDTO> cfgProblemTimeProcessDTOS = Mockito.spy(ArrayList.class);
    cfgProblemTimeProcessDTO.setCfgCode("Test");
    cfgProblemTimeProcessDTO.setLastUpdateTime(new Date());
    cfgProblemTimeProcessDTO.setId("1");
    cfgProblemTimeProcessDTO.setPriorityCode("Test");
    cfgProblemTimeProcessDTO.setTypeCode("Test");
    cfgProblemTimeProcessDTO.setRcaFoundTime("5");
    cfgProblemTimeProcessDTO.setWaFoundTime("6");
    cfgProblemTimeProcessDTO.setSlFoundTime("7");
    cfgProblemTimeProcessDTOS.add(cfgProblemTimeProcessDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgProblemTimeProcessRepository.onDeleteList(anyList()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = cfgProblemTimeProcessBusiness.
        onDeleteList(cfgProblemTimeProcessDTOS);
    Assert.assertEquals(resultInSideDto, result);
  }

  @Test
  public void testOnUpdateList() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    setFinalStatic(CfgProblemTimeProcessBusinessImpl.class.getDeclaredField("log"), logger);

    CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO = Mockito.spy(CfgProblemTimeProcessDTO.class);
    List<CfgProblemTimeProcessDTO> cfgProblemTimeProcessDTOS = Mockito.spy(ArrayList.class);
    cfgProblemTimeProcessDTO.setCfgCode("Test");
    cfgProblemTimeProcessDTO.setLastUpdateTime(new Date());
    cfgProblemTimeProcessDTO.setId("1");
    cfgProblemTimeProcessDTO.setPriorityCode("Test");
    cfgProblemTimeProcessDTO.setTypeCode("Test");
    cfgProblemTimeProcessDTO.setRcaFoundTime("5");
    cfgProblemTimeProcessDTO.setWaFoundTime("6");
    cfgProblemTimeProcessDTO.setSlFoundTime("7");
    cfgProblemTimeProcessDTOS.add(cfgProblemTimeProcessDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgProblemTimeProcessRepository.onUpdateList(anyList(), any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = cfgProblemTimeProcessBusiness.
        onUpdateList(cfgProblemTimeProcessDTOS, cfgProblemTimeProcessDTO);
    Assert.assertEquals(resultInSideDto, result);
  }

  @Test
  public void testGetListDataSearch() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable expected = Mockito.spy(Datatable.class);
    PowerMockito.when(
        cfgProblemTimeProcessRepository.getDataTableCfgProblemTimeProcessDTO(any())
    ).thenReturn(expected);

    CfgProblemTimeProcessDTO dto = Mockito.spy(CfgProblemTimeProcessDTO.class);
    dto.setPriorityCode("123123");
    dto.setTypeCode("123123");
    List<CfgProblemTimeProcessDTO> datas = Mockito.spy(ArrayList.class);
    datas.add(dto);
    expected.setData(datas);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("123123");
    List<CatItemDTO> lstCatItemDTO = Mockito.spy(ArrayList.class);
    lstCatItemDTO.add(catItemDTO);
    PowerMockito.when(
        catItemBusiness
            .getListCatItemDTOLE(anyString(), anyString(), anyString(), any(), anyInt(), anyInt(),
                anyString(), anyString())
    ).thenReturn(lstCatItemDTO);

    Datatable actual = cfgProblemTimeProcessBusiness.getListDataSearch(dto);

    Assert.assertEquals(expected, actual);
  }

  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }
}
