package com.viettel.gnoc.kedb.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.kedb.dto.KedbActionLogsDTO;
import com.viettel.gnoc.kedb.repository.KedbActionLogsRepository;
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
@PrepareForTest({KedbActionLogsBusinessImpl.class, ConditionBeanUtil.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class KedbActionLogsBusinessImplTest {

  @InjectMocks
  KedbActionLogsBusinessImpl kedbActionLogsBusiness;
  @Mock
  KedbActionLogsRepository kedbActionLogsRepository;

  @Test
  public void getListKedbActionLogsDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<KedbActionLogsDTO> kedbActionLogsDTOS = Mockito.spy(ArrayList.class);
    KedbActionLogsDTO kedbActionLogsDTO = Mockito.spy(KedbActionLogsDTO.class);
    PowerMockito.when(kedbActionLogsRepository.getListKedbActionLogsDTO(any()))
        .thenReturn(kedbActionLogsDTOS);
    List<KedbActionLogsDTO> kedbActionLogsDTOS1 = kedbActionLogsBusiness
        .getListKedbActionLogsDTO(kedbActionLogsDTO);
    Assert.assertEquals(kedbActionLogsDTOS.size(), kedbActionLogsDTOS1.size());
  }

  @Test
  public void getListKedbActionLogsByCondition_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<KedbActionLogsDTO> kedbActionLogsDTOS = Mockito.spy(ArrayList.class);
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(ConditionBeanUtil.class);
    PowerMockito.when(kedbActionLogsRepository
        .getListKedbActionLogsByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(kedbActionLogsDTOS);
    List<KedbActionLogsDTO> kedbActionLogsDTOS1 = kedbActionLogsBusiness
        .getListKedbActionLogsByCondition(lstCondition, 1, 1, "asc", "ID");
    Assert.assertEquals(kedbActionLogsDTOS.size(), kedbActionLogsDTOS1.size());
  }

  @Test
  public void getSequenseKedbActionLogs_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> result = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbActionLogsRepository.getSequenseKedbActionLogs(anyString(), any()))
        .thenReturn(result);
    List<String> strings = kedbActionLogsBusiness.getSequenseKedbActionLogs("ID", 1);
    Assert.assertEquals(result.size(), strings.size());
  }

  @Test
  public void updateKedbActionLogs_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    KedbActionLogsDTO kedbActionLogsDTO = Mockito.spy(KedbActionLogsDTO.class);
    PowerMockito.when(kedbActionLogsRepository.updateKedbActionLogs(any()))
        .thenReturn(RESULT.SUCCESS);
    String res = kedbActionLogsBusiness.updateKedbActionLogs(kedbActionLogsDTO);
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void insertKedbActionLogs_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    KedbActionLogsDTO kedbActionLogsDTO = Mockito.spy(KedbActionLogsDTO.class);
    PowerMockito.when(kedbActionLogsRepository.insertKedbActionLogs(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = kedbActionLogsBusiness
        .insertKedbActionLogs(kedbActionLogsDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void insertOrUpdateListKedbActionLogs_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.when(kedbActionLogsRepository.insertOrUpdateListKedbActionLogs(anyList()))
        .thenReturn(RESULT.SUCCESS);
    List<KedbActionLogsDTO> kedbActionLogsDTO = Mockito.spy(ArrayList.class);
    String s = kedbActionLogsBusiness.insertOrUpdateListKedbActionLogs(kedbActionLogsDTO);
    Assert.assertEquals(s, RESULT.SUCCESS);
  }

  @Test
  public void findKedbActionLogsById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    KedbActionLogsDTO kedbActionLogsDTO = Mockito.spy(KedbActionLogsDTO.class);
    PowerMockito.when(kedbActionLogsRepository.findKedbActionLogsById(anyLong()))
        .thenReturn(kedbActionLogsDTO);
    KedbActionLogsDTO kedbActionLogsDTO1 = kedbActionLogsBusiness.findKedbActionLogsById(1L);
    Assert.assertNotNull(kedbActionLogsDTO1);
  }

  @Test
  public void deleteKedbActionLogs_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.when(kedbActionLogsRepository.deleteKedbActionLogs(anyLong()))
        .thenReturn(RESULT.SUCCESS);
    String s = kedbActionLogsBusiness.deleteKedbActionLogs(1L);
    Assert.assertEquals(s, RESULT.SUCCESS);
  }

  @Test
  public void deleteListKedbActionLogs_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<KedbActionLogsDTO> kedbActionLogsListDTO = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbActionLogsRepository.deleteListKedbActionLogs(anyList()))
        .thenReturn(RESULT.SUCCESS);
    String s = kedbActionLogsBusiness.deleteListKedbActionLogs(kedbActionLogsListDTO);
    Assert.assertEquals(s, RESULT.SUCCESS);
  }

  @Test
  public void onSearchKedbActionLogs_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    KedbActionLogsDTO kedbActionLogsDTO = Mockito.spy(KedbActionLogsDTO.class);
    PowerMockito.when(kedbActionLogsRepository.onSearchKedbActionLogs(any())).thenReturn(datatable);
    Datatable datatable1 = kedbActionLogsBusiness.onSearchKedbActionLogs(kedbActionLogsDTO);
    Assert.assertEquals(datatable1.getPages(), 0L);
  }
}
