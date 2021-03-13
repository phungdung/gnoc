package com.viettel.gnoc.wo.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.ReceiveUnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import com.viettel.gnoc.wo.dto.WoTypeGroupDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.repository.WoCdGroupRepository;
import com.viettel.gnoc.wo.repository.WoCdGroupUnitRepository;
import com.viettel.gnoc.wo.repository.WoCdRepository;
import com.viettel.gnoc.wo.repository.WoTypeGroupRepository;
import com.viettel.gnoc.wo.repository.WoTypeRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WoCdGroupBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
@Slf4j
public class WoCdGroupBusinessImplTest {

  @InjectMocks
  WoCdGroupBusinessImpl woCdGroupBusiness;

  @Mock
  WoCdGroupRepository woCdGroupRepository;

  @Mock
  WoTypeGroupRepository woTypeGroupRepository;

  @Mock
  WoCdGroupUnitRepository woCdGroupUnitRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  ReceiveUnitRepository receiveUnitRepository;

  @Mock
  WoTypeRepository woTypeRepository;

  @Mock
  WoCdRepository woCdRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(woCdGroupBusiness, "tempFolder",
        "./wo-upload");
  }

  @Test
  public void getListWoCdGroupDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    PowerMockito.when(woCdGroupRepository.getListWoCdGroupDTO(any())).thenReturn(datatable);
    Datatable datatable1 = woCdGroupBusiness.getListWoCdGroupDTO(woCdGroupInsideDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void findWoCdGroupById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    PowerMockito.when(woCdGroupRepository.findWoCdGroupById(anyLong()))
        .thenReturn(woCdGroupInsideDTO);
    WoCdGroupInsideDTO result = woCdGroupBusiness.findWoCdGroupById(123L);
    Assert.assertEquals(woCdGroupInsideDTO, result);
  }

  @Test
  public void insertWoCdGroup_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupCode("ABC");
    PowerMockito.when(woCdGroupRepository.insertWoCdGroup(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woCdGroupBusiness.insertWoCdGroup(woCdGroupInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void updateWoCdGroup_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupCode("ABC");
    PowerMockito.when(woCdGroupRepository.updateWoCdGroup(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woCdGroupBusiness.updateWoCdGroup(woCdGroupInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void deleteWoCdGroup_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woCdGroupRepository.deleteWoCdGroup(anyLong())).thenReturn(resultInSideDto);
    PowerMockito.when(woCdRepository.deleteWoCdByWoGroupId(anyLong())).thenReturn(resultInSideDto);
    PowerMockito.when(woCdGroupUnitRepository.deleteWoCdGroupUnitByCdGroupId(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woTypeGroupRepository.deleteWoTypeGroupByWoGroupId(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woCdGroupBusiness.deleteWoCdGroup(10L);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void getListCdGroupByUser_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoCdGroupTypeUserDTO woCdGroupTypeUserDTO = Mockito.spy(WoCdGroupTypeUserDTO.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    List<WoCdGroupInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(woCdGroupInsideDTO);
    PowerMockito.when(woCdGroupRepository.getListCdGroupByUser(any())).thenReturn(list);
    List<WoCdGroupInsideDTO> list1 = woCdGroupBusiness.getListCdGroupByUser(woCdGroupTypeUserDTO);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void getListWoCdGroupUnitDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoCdGroupUnitDTO woCdGroupUnitDTO = Mockito.spy(WoCdGroupUnitDTO.class);
    woCdGroupUnitDTO.setUnitId(10L);
    List<WoCdGroupUnitDTO> listWoCdGroupUnit = Mockito.spy(ArrayList.class);
    listWoCdGroupUnit.add(woCdGroupUnitDTO);
    List<Long> listUnitId = Mockito.spy(ArrayList.class);
    List<ReceiveUnitDTO> list = Mockito.spy(ArrayList.class);
    listUnitId.add(10L);
    PowerMockito.when(woCdGroupUnitRepository.getListWoCdGroupUnitDTO(any()))
        .thenReturn(listWoCdGroupUnit);
    PowerMockito.when(receiveUnitRepository.getListReceiveUnit(anyList())).thenReturn(list);
    List<ReceiveUnitDTO> list1 = woCdGroupBusiness.getListWoCdGroupUnitDTO(woCdGroupUnitDTO);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void updateWoCdGroupUnit_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoCdGroupUnitDTO woCdGroupUnitDTO = Mockito.spy(WoCdGroupUnitDTO.class);
    List<Long> listUnitIdDel = Mockito.spy(ArrayList.class);
    listUnitIdDel.add(1L);
    List<Long> listUnitIdInsert = Mockito.spy(ArrayList.class);
    listUnitIdInsert.add(1L);
    woCdGroupUnitDTO.setListUnitIdDel(listUnitIdDel);
    woCdGroupUnitDTO.setListUnitIdInsert(listUnitIdInsert);
    woCdGroupUnitDTO.setCdGroupUnitId(8L);

    List<WoCdGroupUnitDTO> listWoCdGroupUnit = Mockito.spy(ArrayList.class);
    listWoCdGroupUnit.add(woCdGroupUnitDTO);

    PowerMockito.when(woCdGroupUnitRepository.getListWoCdGroupUnitDTO(any()))
        .thenReturn(listWoCdGroupUnit);
    PowerMockito.when(woCdGroupUnitRepository.deleteWoCdGroupUnit(anyLong()))
        .thenReturn(resultInSideDto);

    PowerMockito.when(woCdGroupUnitRepository.insertWoCdGroupUnit(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woCdGroupUnitRepository.updateWoCdGroupUnit(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woCdGroupBusiness.updateWoCdGroupUnit(woCdGroupUnitDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void getListWoCdDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoCdDTO woCdDTO = Mockito.spy(WoCdDTO.class);
    woCdDTO.setUserId(10L);
    List<WoCdDTO> listWoCd = Mockito.spy(ArrayList.class);
    listWoCd.add(woCdDTO);
    List<UsersInsideDto> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(woCdRepository.getListWoCdDTO(any())).thenReturn(listWoCd);
    PowerMockito.when(userRepository.getListUsersByListUserId(anyList())).thenReturn(list);
    List<UsersInsideDto> list1 = woCdGroupBusiness.getListWoCdDTO(woCdDTO);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void updateWoCd_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    WoCdDTO woCdDTO = Mockito.spy(WoCdDTO.class);
    woCdDTO.setUserId(10L);
    List<WoCdDTO> listWoCd = Mockito.spy(ArrayList.class);
    listWoCd.add(woCdDTO);
    List<Long> listUserIdDel = Mockito.spy(ArrayList.class);
    listUserIdDel.add(1L);
    List<Long> listUserIdInsert = Mockito.spy(ArrayList.class);
    listUserIdInsert.add(1L);
    woCdDTO.setListUserIdDel(listUserIdDel);
    woCdDTO.setListUserIdInsert(listUserIdInsert);
    PowerMockito.when(woCdRepository.getListWoCdDTO(any())).thenReturn(listWoCd);
    PowerMockito.when(woCdRepository.deleteWoCd(anyLong())).thenReturn(resultInSideDto);
    PowerMockito.when(woCdRepository.insertWoCd(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woCdRepository.updateWoCd(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woCdGroupBusiness.updateWoCd(woCdDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void getListWoTypeGroupDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoTypeInsideDTO> list = Mockito.spy(ArrayList.class);
    WoTypeGroupDTO woTypeGroupDTO = Mockito.spy(WoTypeGroupDTO.class);
    woTypeGroupDTO.setWoTypeId(10L);
    List<WoTypeGroupDTO> listWoTypeGroup = Mockito.spy(ArrayList.class);
    listWoTypeGroup.add(woTypeGroupDTO);
    List<Long> listWoTypeId = Mockito.spy(ArrayList.class);
    listWoTypeId.add(woTypeGroupDTO.getWoTypeId());
    PowerMockito.when(woTypeGroupRepository.getListWoTypeGroupDTO(any()))
        .thenReturn(listWoTypeGroup);
    PowerMockito.when(woTypeRepository.getListWoTypeForWoCdGroup(anyList())).thenReturn(list);
    List<WoTypeInsideDTO> list1 = woCdGroupBusiness.getListWoTypeGroupDTO(woTypeGroupDTO);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void getListWoTypeGroupDTO_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoTypeInsideDTO> list = Mockito.spy(ArrayList.class);
    WoTypeGroupDTO woTypeGroupDTO = Mockito.spy(WoTypeGroupDTO.class);
    woTypeGroupDTO.setWoTypeId(10L);
    List<WoTypeGroupDTO> listWoTypeGroup = Mockito.spy(ArrayList.class);
    List<Long> listWoTypeId = Mockito.spy(ArrayList.class);
    listWoTypeId.add(woTypeGroupDTO.getWoTypeId());
    PowerMockito.when(woTypeGroupRepository.getListWoTypeGroupDTO(any()))
        .thenReturn(listWoTypeGroup);
    PowerMockito.when(woTypeRepository.getListWoTypeForWoCdGroup(anyList())).thenReturn(list);
    List<WoTypeInsideDTO> list1 = woCdGroupBusiness.getListWoTypeGroupDTO(woTypeGroupDTO);
    Assert.assertNull(list1);
  }

  @Test
  public void getListWoTypeAll_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    List<WoTypeInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(woTypeInsideDTO);
    PowerMockito.when(woTypeRepository.getListWoTypeForWoCdGroup(anyList())).thenReturn(list);
    List<WoTypeInsideDTO> list1 = woCdGroupBusiness.getListWoTypeAll();
    list1.add(woTypeInsideDTO);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void updateWoTypeGroup_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    WoTypeGroupDTO woTypeGroupDTO = Mockito.spy(WoTypeGroupDTO.class);
    woTypeGroupDTO.setWoTypeId(10L);
    woTypeGroupDTO.setWoTypeGroupId(15L);

    List<Long> listWoTypeIdInsert = Mockito.spy(ArrayList.class);
    listWoTypeIdInsert.add(10L);
    woTypeGroupDTO.setListWoTypeIdInsert(listWoTypeIdInsert);

    List<WoTypeGroupDTO> listWoTypeGroup = Mockito.spy(ArrayList.class);
    listWoTypeGroup.add(woTypeGroupDTO);

    PowerMockito.when(woTypeGroupRepository.getListWoTypeGroupDTO(any()))
        .thenReturn(listWoTypeGroup);
    PowerMockito.when(woTypeGroupRepository.deleteWoTypeGroup(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woTypeGroupRepository.insertWoTypeGroup(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woTypeGroupRepository.updateWoTypeGroup(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woCdGroupBusiness.updateWoTypeGroup(woTypeGroupDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void updateWoTypeGroup_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    WoTypeGroupDTO woTypeGroupDTO = Mockito.spy(WoTypeGroupDTO.class);
    woTypeGroupDTO.setWoTypeId(10L);
    woTypeGroupDTO.setWoTypeGroupId(15L);

    List<Long> listWoTypeIdInsert = Mockito.spy(ArrayList.class);
    woTypeGroupDTO.setListWoTypeIdInsert(listWoTypeIdInsert);

    List<WoTypeGroupDTO> listWoTypeGroup = Mockito.spy(ArrayList.class);
    listWoTypeGroup.add(woTypeGroupDTO);

    PowerMockito.when(woTypeGroupRepository.getListWoTypeGroupDTO(any()))
        .thenReturn(listWoTypeGroup);
    PowerMockito.when(woTypeGroupRepository.deleteWoTypeGroup(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woTypeGroupRepository.updateWoTypeGroup(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woCdGroupBusiness.updateWoTypeGroup(woTypeGroupDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void deleteListWoTypeGroup_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    WoTypeGroupDTO woTypeGroupDTO = Mockito.spy(WoTypeGroupDTO.class);
    woTypeGroupDTO.setWoTypeGroupId(10L);

    List<WoTypeGroupDTO> listWoTypeGroup = Mockito.spy(ArrayList.class);
    listWoTypeGroup.add(woTypeGroupDTO);

    List<Long> listWoTypeGroupId = Mockito.spy(ArrayList.class);
    listWoTypeGroupId.add(woTypeGroupDTO.getWoTypeGroupId());

    PowerMockito.when(woTypeGroupRepository.getListWoTypeGroupDTO(any()))
        .thenReturn(listWoTypeGroup);
    PowerMockito.when(woTypeGroupRepository.deleteWoTypeGroup(anyLong()))
        .thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDto1 = woCdGroupBusiness.deleteListWoTypeGroup(woTypeGroupDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void deleteListWoTypeGroup_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    WoTypeGroupDTO woTypeGroupDTO = Mockito.spy(WoTypeGroupDTO.class);
    woTypeGroupDTO.setWoTypeGroupId(10L);

    List<WoTypeGroupDTO> listWoTypeGroup = Mockito.spy(ArrayList.class);

    List<Long> listWoTypeGroupId = Mockito.spy(ArrayList.class);
    listWoTypeGroupId.add(woTypeGroupDTO.getWoTypeGroupId());

    PowerMockito.when(woTypeGroupRepository.getListWoTypeGroupDTO(any()))
        .thenReturn(listWoTypeGroup);

    ResultInSideDto resultInSideDto1 = woCdGroupBusiness.deleteListWoTypeGroup(woTypeGroupDTO);
    Assert.assertNull(resultInSideDto1);
  }

  @Test
  public void exportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Khóa");
    WoCdGroupInsideDTO dto = Mockito.spy(WoCdGroupInsideDTO.class);
    dto.setIsEnable(0L);
    List<WoCdGroupInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(dto);
    PowerMockito.when(woCdGroupRepository.getListWoCdGroupExport(any())).thenReturn(list);
    File file = woCdGroupBusiness.exportData(dto);
    Assert.assertNotNull(file);
  }

  @Test
  public void exportData_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Not Khóa");
    WoCdGroupInsideDTO dto = Mockito.spy(WoCdGroupInsideDTO.class);
    dto.setIsEnable(1L);
    List<WoCdGroupInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(dto);
    PowerMockito.when(woCdGroupRepository.getListWoCdGroupExport(any())).thenReturn(list);
    File file = woCdGroupBusiness.exportData(dto);
    Assert.assertNotNull(file);
  }

  @Test
  public void getTemplateImport_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("STT");
    CatItemDTO dto = Mockito.spy(CatItemDTO.class);
    dto.setItemName("ID");
    List<CatItemDTO> listGroupType = Mockito.spy(ArrayList.class);
    listGroupType.add(dto);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(listGroupType);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataGroupType);
    File file = woCdGroupBusiness.getTemplateImport();
    Assert.assertNotNull(file);
  }

  @Test
  public void updateStatusCdGroup_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woCdGroupRepository.updateStatusCdGroup(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woCdGroupBusiness.updateStatusCdGroup(any());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void getListCdGroup_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoCdGroupInsideDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(woCdGroupRepository.getListCdGroup(anyString())).thenReturn(list);
    List<WoCdGroupInsideDTO> list1 = woCdGroupBusiness.getListCdGroup(anyString());
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void getListWoCdGroupDTOByWoTypeAndGroupType_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoCdGroupInsideDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(woCdGroupRepository.getListGroupDispatch(anyLong(), anyLong(), anyString()))
        .thenReturn(list);
    List<WoCdGroupInsideDTO> list1 = woCdGroupBusiness
        .getListWoCdGroupDTOByWoTypeAndGroupType(anyLong(), anyLong(), anyString());
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void getListCdGroupByUserDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoCdGroupInsideDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(woCdGroupRepository
        .getListCdGroupByUserDTO(any(), anyLong(), anyLong(), anyLong(), anyString()))
        .thenReturn(list);
    List<WoCdGroupInsideDTO> list1 = woCdGroupBusiness
        .getListCdGroupByUserDTO(any(), anyLong(), anyLong(), anyLong(), anyString());
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void getWoCdGroupWoByCdGroupCode_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    PowerMockito.when(woCdGroupRepository.getWoCdGroupWoByCdGroupCode(anyString()))
        .thenReturn(woCdGroupInsideDTO);
    WoCdGroupInsideDTO result = woCdGroupBusiness.getWoCdGroupWoByCdGroupCode(anyString());
    Assert.assertEquals(woCdGroupInsideDTO, result);
  }

  @Test
  public void getListWoCdGroupActive_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setIsEnable(1L);
    List<WoCdGroupInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woCdGroupInsideDTO);
    List<WoCdGroupInsideDTO> lstTmp = Mockito.spy(ArrayList.class);
    lstTmp.add(woCdGroupInsideDTO);
    PowerMockito.when(woCdGroupRepository
        .getListWoCdGroupActive(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstTmp);
    List<WoCdGroupInsideDTO> lst1 = woCdGroupBusiness
        .getListWoCdGroupActive(woCdGroupInsideDTO, 1, 1, "asc", "ID");
    Assert.assertEquals(lst.size(), lst1.size());
  }

  @Test
  public void getListWoCdGroupActive_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoCdGroupInsideDTO> lst1 = woCdGroupBusiness
        .getListWoCdGroupActive(null, 1, 1, "asc", "ID");
    Assert.assertNull(lst1);
  }

  @Test
  public void getListFtByUser_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<UsersInsideDto> list = Mockito.spy(ArrayList.class);
    PowerMockito
        .when(woCdGroupRepository.getListFtByUser(anyString(), anyString(), anyInt(), anyInt()))
        .thenReturn(list);
    List<UsersInsideDto> list1 = woCdGroupBusiness.getListFtByUser("123", "rtrt", 1, 96);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void getTemplateAssignUser() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("STT");
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupName("1");
    woCdGroupInsideDTO.setWoGroupCode("1");
    List<WoCdGroupInsideDTO> listWoCdGroupInsideDTO = Mockito.spy(ArrayList.class);
    listWoCdGroupInsideDTO.add(woCdGroupInsideDTO);
    PowerMockito.when(woCdGroupRepository.getListWoCdGroupExport(any()))
        .thenReturn(listWoCdGroupInsideDTO);
    File result = woCdGroupBusiness.getTemplateAssignUser();
    Assert.assertNotNull(result);
  }

  @Test
  public void getTemplateAssignTypeGroup() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("STT");
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupName("1");
    woCdGroupInsideDTO.setWoGroupCode("1");
    List<WoCdGroupInsideDTO> listWoCdGroupInsideDTO = Mockito.spy(ArrayList.class);
    listWoCdGroupInsideDTO.add(woCdGroupInsideDTO);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeName("1");
    woTypeInsideDTO.setWoTypeCode("1");
    List<WoTypeInsideDTO> listWoTypeInsideDTO = Mockito.spy(ArrayList.class);
    listWoTypeInsideDTO.add(woTypeInsideDTO);
    PowerMockito.when(woCdGroupRepository.getListWoCdGroupExport(any()))
        .thenReturn(listWoCdGroupInsideDTO);
    PowerMockito.when(woTypeRepository.getListWoTypeForWoCdGroup(any()))
        .thenReturn(listWoTypeInsideDTO);
    File result = woCdGroupBusiness.getTemplateAssignTypeGroup();
    Assert.assertNotNull(result);
  }

  @Test
  public void test_importDataAssignTypeGroup() throws Exception {
    ResultInSideDto result = woCdGroupBusiness.importDataAssignTypeGroup(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void test_importDataAssignTypeGroup_1() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    ResultInSideDto result = woCdGroupBusiness.importDataAssignTypeGroup(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataAssignTypeGroup_2() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Object[] header = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        4,//begin row
        0,//from column
        4,//to column
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignTypeGroup(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataAssignTypeGroup_3() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)"};
    Object[] header1 = new Object[]{"1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    for (int i = 0; i < 600; i++) {
      headerList1.add(header1);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        5,//begin row
        0,//from column
        3,//to column
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        6,//begin row
        0,//from column
        3,//to column
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignTypeGroup(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");
  }

  @Test
  public void test_importDataAssignTypeGroup_4() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)"};
    Object[] header1 = new Object[]{"1(1)", "1(1)", "1(1)", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    headerList1.add(header1);
    WoCdGroupInsideDTO woCdGroupInsideDTOTmp = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTOTmp.setWoGroupId(1L);
    WoTypeGroupDTO woTypeGroupDTOTmp = Mockito.spy(WoTypeGroupDTO.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    woTypeInsideDTO.setWoTypeCode("1");
    woTypeInsideDTO.setWoTypeName("1");
    List<WoTypeInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(woTypeInsideDTO);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        5,//begin row
        0,//from column
        3,//to column
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        6,//begin row
        0,//from column
        3,//to column
        1000
    )).thenReturn(headerList1);
    PowerMockito.when(woTypeRepository.getListWoTypeForWoCdGroup(null)).thenReturn(list);
    PowerMockito.when(woTypeGroupRepository
        .checkTypeGroupExitBy2Id(anyLong(), anyLong())).thenReturn(woTypeGroupDTOTmp);
    PowerMockito.when(woCdGroupRepository.checkWoCdGroupExit(any()))
        .thenReturn(woCdGroupInsideDTOTmp);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignTypeGroup(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_importDataAssignUser() throws Exception {
    ResultInSideDto result = woCdGroupBusiness.importDataAssignUser(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void test_importDataAssignUser_1() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    ResultInSideDto result = woCdGroupBusiness.importDataAssignUser(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataAssignUser_2() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Object[] header = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        5,//begin row
        0,//from column
        3,//to column
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignUser(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataAssignUser_3() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)"};
    Object[] header1 = new Object[]{"1", "1", "1", "1"};

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    for (int i = 0; i < 600; i++) {
      headerList1.add(header1);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        5,//begin row
        0,//from column
        3,//to column
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        6,//begin row
        0,//from column
        3,//to column
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignUser(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");
  }

  @Test
  public void test_importDataAssignUser_4() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)"};
    Object[] header1 = new Object[]{"1", "1", "1", "1"};
    WoCdGroupInsideDTO woCdGroupInsideDTOTmp = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTOTmp.setWoGroupId(1L);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    headerList1.add(header1);
    UsersInsideDto usersInsideDtoTmp = Mockito.spy(UsersInsideDto.class);
    usersInsideDtoTmp.setUserId(1L);
    WoCdDTO woCdDTOTmp = Mockito.spy(WoCdDTO.class);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        5,//begin row
        0,//from column
        3,//to column
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        6,//begin row
        0,//from column
        3,//to column
        1000
    )).thenReturn(headerList1);
    PowerMockito.when(woCdGroupRepository.checkWoCdGroupExit(any()))
        .thenReturn(woCdGroupInsideDTOTmp);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString()))
        .thenReturn(usersInsideDtoTmp);
    PowerMockito.when(woCdRepository.checkWoCdExitBy2Id(anyLong(), anyLong()))
        .thenReturn(woCdDTOTmp);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignUser(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

}
