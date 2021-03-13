package com.viettel.gnoc.kedb.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.kedb.dto.KedbRatingInsideDTO;
import com.viettel.gnoc.kedb.model.KedbRatingEntity;
import com.viettel.gnoc.kedb.repository.KedbRatingRepository;
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
@PrepareForTest({KedbRatingBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class KedbRatingBusinessImplTest {

  @InjectMocks
  KedbRatingBusinessImpl kedbRatingBusiness;

  @Mock
  KedbRatingRepository kedbRatingRepository;

  @Test
  public void getListKedbRatingDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    List<KedbRatingInsideDTO> kedbRatingInsideDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRatingRepository.getListKedbRatingDTO(any())).thenReturn(datatable);
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    kedbRatingInsideDTOS.add(kedbRatingInsideDTO);
    List<KedbRatingInsideDTO> kedbRatingInsideDTOS1 = kedbRatingBusiness
        .getListKedbRatingDTO(kedbRatingInsideDTO);
    Assert.assertNull(kedbRatingInsideDTOS1);
  }

  @Test
  public void findKedbRatingById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    PowerMockito.when(kedbRatingRepository.findKedbRatingById(anyLong()))
        .thenReturn(kedbRatingInsideDTO);
    KedbRatingInsideDTO kedbRatingInsideDTO1 = kedbRatingBusiness.findKedbRatingById(1L);
    Assert.assertNotNull(kedbRatingInsideDTO1);
  }

  @Test
  public void getKedbRating_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    PowerMockito.when(kedbRatingRepository.getKedbRating(any())).thenReturn(kedbRatingInsideDTO);
    KedbRatingInsideDTO kedbRatingInsideDTO1 = kedbRatingBusiness
        .getKedbRating(kedbRatingInsideDTO);
    Assert.assertNotNull(kedbRatingInsideDTO1);
  }

  @Test
  public void insertKedbRating_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(kedbRatingRepository.insertKedbRating(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = kedbRatingBusiness.insertKedbRating(any());
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void updateKedbRating_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    PowerMockito.when(kedbRatingRepository.updateKedbRating(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = kedbRatingBusiness.updateKedbRating(kedbRatingInsideDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void insertOrUpdateListKedbRating_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    List<KedbRatingInsideDTO> listKedbRatingInsideDTO = Mockito.spy(ArrayList.class);
    listKedbRatingInsideDTO.add(kedbRatingInsideDTO);
    PowerMockito.when(kedbRatingRepository.insertOrUpdateListKedbRating(anyList()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = kedbRatingBusiness
        .insertOrUpdateListKedbRating(listKedbRatingInsideDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteKedbRating_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(kedbRatingRepository.deleteKedbRating(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = kedbRatingBusiness.deleteKedbRating(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteListKedbRating_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<Long> listId = Mockito.spy(ArrayList.class);
    listId.add(1L);
    PowerMockito.when(kedbRatingRepository.deleteKedbRating(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = kedbRatingBusiness.deleteListKedbRating(listId);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getSequenseKedbRating_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> result = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRatingRepository.getSequenseKedbRating(any(), any())).thenReturn(result);
    List<String> strings = kedbRatingBusiness.getSequenseKedbRating(1);
    Assert.assertEquals(result.size(), strings.size());
  }

  @Test
  public void getListKedbRatingByCondition_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<KedbRatingInsideDTO> kedbRatingInsideDTOS = Mockito.spy(ArrayList.class);
    List<ConditionBean> lstCondition = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRatingRepository
        .getListKedbRatingByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(kedbRatingInsideDTOS);
    List<KedbRatingInsideDTO> kedbRatingInsideDTOS1 = kedbRatingBusiness
        .getListKedbRatingByCondition(lstCondition, 1, 1, "asc", "ID");
    Assert.assertNotNull(kedbRatingInsideDTOS1);
  }

  @Test
  public void insertOrUpdateKedbRating_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    kedbRatingInsideDTO.setKedbId(1L);
    kedbRatingInsideDTO.setUserName("thanhlv12");
    kedbRatingInsideDTO.setPoint(1L);
    kedbRatingInsideDTO.setNote("fixBug");
    PowerMockito.when(kedbRatingRepository.insertKedbRating(any())).thenReturn(resultInSideDto);
    List<KedbRatingEntity> listEntity = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRatingRepository.findKedbRatingEntityByMultilParam(any()))
        .thenReturn(listEntity);
    KedbRatingInsideDTO kedbRatingInsideDTO1 = kedbRatingBusiness
        .insertOrUpdateKedbRating(kedbRatingInsideDTO);
    Assert.assertNotNull(kedbRatingInsideDTO1);
  }

  @Test
  public void insertOrUpdateKedbRating_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    kedbRatingInsideDTO.setUserName("thanhlv12");
    kedbRatingInsideDTO.setPoint(1L);
    kedbRatingInsideDTO.setNote("fixBug");
    PowerMockito.when(kedbRatingRepository.insertKedbRating(any())).thenReturn(resultInSideDto);
    List<KedbRatingEntity> listEntity = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRatingRepository.findKedbRatingEntityByMultilParam(any()))
        .thenReturn(listEntity);
    KedbRatingInsideDTO kedbRatingInsideDTO1 = kedbRatingBusiness
        .insertOrUpdateKedbRating(kedbRatingInsideDTO);
    Assert.assertNotNull(kedbRatingInsideDTO1);
  }

  @Test
  public void insertOrUpdateKedbRating_03() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    kedbRatingInsideDTO.setKedbId(1L);
    kedbRatingInsideDTO.setPoint(1L);
    kedbRatingInsideDTO.setNote("fixBug");
    PowerMockito.when(kedbRatingRepository.insertKedbRating(any())).thenReturn(resultInSideDto);
    List<KedbRatingEntity> listEntity = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRatingRepository.findKedbRatingEntityByMultilParam(any()))
        .thenReturn(listEntity);
    KedbRatingInsideDTO kedbRatingInsideDTO1 = kedbRatingBusiness
        .insertOrUpdateKedbRating(kedbRatingInsideDTO);
    Assert.assertNotNull(kedbRatingInsideDTO1);
  }

  @Test
  public void insertOrUpdateKedbRating_04() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    kedbRatingInsideDTO.setKedbId(1L);
    kedbRatingInsideDTO.setUserName("thanhlv12");
    kedbRatingInsideDTO.setNote("fixBug");
    PowerMockito.when(kedbRatingRepository.insertKedbRating(any())).thenReturn(resultInSideDto);
    List<KedbRatingEntity> listEntity = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRatingRepository.findKedbRatingEntityByMultilParam(any()))
        .thenReturn(listEntity);
    KedbRatingInsideDTO kedbRatingInsideDTO1 = kedbRatingBusiness
        .insertOrUpdateKedbRating(kedbRatingInsideDTO);
    Assert.assertNotNull(kedbRatingInsideDTO1);
  }

  @Test
  public void insertOrUpdateKedbRating_05() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    kedbRatingInsideDTO.setKedbId(1L);
    kedbRatingInsideDTO.setUserName("thanhlv12");
    kedbRatingInsideDTO.setPoint(1L);
    PowerMockito.when(kedbRatingRepository.insertKedbRating(any())).thenReturn(resultInSideDto);
    List<KedbRatingEntity> listEntity = Mockito.spy(ArrayList.class);
    PowerMockito.when(kedbRatingRepository.findKedbRatingEntityByMultilParam(any()))
        .thenReturn(listEntity);
    KedbRatingInsideDTO kedbRatingInsideDTO1 = kedbRatingBusiness
        .insertOrUpdateKedbRating(kedbRatingInsideDTO);
    Assert.assertNotNull(kedbRatingInsideDTO1);
  }

  @Test
  public void insertOrUpdateKedbRating_06() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    kedbRatingInsideDTO.setKedbId(1L);
    kedbRatingInsideDTO.setUserName("thanhlv12");
    kedbRatingInsideDTO.setPoint(1L);
    kedbRatingInsideDTO.setNote("fixBug");
    PowerMockito.when(kedbRatingRepository.updateKedbRating(any())).thenReturn(resultInSideDto);
    List<KedbRatingEntity> listEntity = Mockito.spy(ArrayList.class);
    KedbRatingEntity kedbRatingEntity = Mockito.spy(KedbRatingEntity.class);
    listEntity.add(kedbRatingEntity);
    PowerMockito.when(kedbRatingRepository.findKedbRatingEntityByMultilParam(any()))
        .thenReturn(listEntity);
    KedbRatingInsideDTO kedbRatingInsideDTO1 = kedbRatingBusiness
        .insertOrUpdateKedbRating(kedbRatingInsideDTO);
    Assert.assertNotNull(kedbRatingInsideDTO1);
  }

  @Test
  public void insertOrUpdateKedbRating_07() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    KedbRatingInsideDTO kedbRatingInsideDTO = Mockito.spy(KedbRatingInsideDTO.class);
    kedbRatingInsideDTO.setKedbId(1L);
    kedbRatingInsideDTO.setUserName("thanhlv12");
    kedbRatingInsideDTO.setPoint(1L);
    kedbRatingInsideDTO.setNote("fixBug");
    PowerMockito.when(kedbRatingRepository.updateKedbRating(any())).thenReturn(resultInSideDto);
    List<KedbRatingEntity> listEntity = Mockito.spy(ArrayList.class);
    KedbRatingEntity kedbRatingEntity = Mockito.spy(KedbRatingEntity.class);
    listEntity.add(kedbRatingEntity);
    PowerMockito.when(kedbRatingRepository.findKedbRatingEntityByMultilParam(any()))
        .thenReturn(listEntity);
    KedbRatingInsideDTO kedbRatingInsideDTO1 = kedbRatingBusiness
        .insertOrUpdateKedbRating(kedbRatingInsideDTO);
    Assert.assertNotNull(kedbRatingInsideDTO1);
  }

  @Test
  public void insertOrUpdateKedbRating_08() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    KedbRatingInsideDTO kedbRatingInsideDTO1 = kedbRatingBusiness.insertOrUpdateKedbRating(null);
    Assert.assertNotNull(kedbRatingInsideDTO1);
  }
}
