package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.CatItemEntity;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.proxy.OdCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.od.repository.OdCommonRepository;
import com.viettel.gnoc.od.repository.OdRepository;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;


@RunWith(PowerMockRunner.class)
@PrepareForTest({OdCommonBusiness.class, FileUtils.class, CommonImport.class, I18n.class,
    CommonExport.class, BaseRepository.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class OdCommonBusinessImplTest {

  @InjectMocks
  OdCommonBusinessImpl odCommonBusiness;

  @Mock
  OdCommonRepository odCommonRepository;

  @Mock
  CommonBusiness commonBusiness;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  LanguageExchangeRepository languageExchangeRepository;

  @Mock
  OdParamBusiness odParamBusiness;

  @Mock
  CatItemBusiness catItemBusiness;

  @Mock
  OdCategoryServiceProxy odCategoryServiceProxy;

  @Mock
  OdHistoryBusiness odHistoryBusiness;

  @Mock
  OdRelationBusiness odRelationBusiness;

  @Mock
  OdRepository odRepository;

  @Mock
  MessagesBusiness messagesBusiness;

  @Mock
  UnitRepository unitRepository;

  @Mock
  GnocFileRepository gnocFileRepository;


  @Test
  public void getListDataSearch() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    OdSearchInsideDTO odSearchInsideDTO = Mockito.spy(OdSearchInsideDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(odCommonRepository.getListDataSearch(any())).thenReturn(datatable);

    Datatable actual = odCommonBusiness.getListDataSearch(odSearchInsideDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getCountListDataSearchForOther() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    OdSearchInsideDTO odDTO = Mockito.spy(OdSearchInsideDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setTotal(5);
    PowerMockito.when(odCommonRepository.getListDataSearch(any())).thenReturn(datatable);

    Integer actual = odCommonBusiness.getCountListDataSearchForOther(odDTO);

    Assert.assertEquals(5, actual.intValue());
  }

  @Test
  public void getListDataSearchForOther() {
//    PowerMockito.mockStatic(I18n.class);
//    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
//
//    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//
//    OdSearchInsideDTO odDTOSearch = Mockito.spy(OdSearchInsideDTO.class);
//    odDTOSearch.setPriorityId(1L);
//    odDTOSearch.setPriorityName("PriorityName");
//    Datatable datatable = Mockito.spy(Datatable.class);
//    List<OdSearchInsideDTO> listDataSearch = Mockito.spy(ArrayList.class);
//    listDataSearch.add(odDTOSearch);
//    datatable.setData(listDataSearch);
//    Map<String, Object> map = Mockito.spy(HashMap.class);
//    List<LanguageExchangeDTO> lstLanguage = Mockito.spy(ArrayList.class);
//
//    PowerMockito.when(odCommonRepository.getListDataSearch(any())).thenReturn(datatable);
//    PowerMockito.when(
//        languageExchangeRepository
//            .findBySql(anyString(), anyMap(), anyMap(), any())
//    ).thenReturn(lstLanguage);
//
//    List<OdSearchInsideDTO> actual = odCommonBusiness.getListDataSearchForOther(odDTOSearch);
//
//    Assert.assertNotNull(actual);
  }

  @Test
  public void getListDataSearchVsmart() {
    OdSearchInsideDTO odDTOSearch = Mockito.spy(OdSearchInsideDTO.class);
    odDTOSearch.setStatus("1111");
    odDTOSearch.setPriorityId(1111L);
    List<OdSearchInsideDTO> lstOdSearch = Mockito.spy(ArrayList.class);
    lstOdSearch.add(odDTOSearch);
    List<CatItemDTO> catItemDTOList = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1111");
    catItemDTO.setItemName("ItemName");
    catItemDTO.setItemId(1111L);
    catItemDTOList.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(catItemDTOList);

    PowerMockito.when(
        odCommonRepository.getListDataExport(any())
    ).thenReturn(lstOdSearch);
    PowerMockito.when(
        catItemBusiness
            .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString())
    ).thenReturn(datatable);

    List<OdSearchInsideDTO> actual = odCommonBusiness.getListDataSearchVsmart(odDTOSearch);

    Assert.assertNotNull(actual);
  }

  @Test
  public void insertOdFromVsmart() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    List<ObjKeyValueVsmartDTO> o = Mockito.spy(ArrayList.class);
    ObjKeyValueVsmartDTO objDTO1 = Mockito.spy(ObjKeyValueVsmartDTO.class);
    objDTO1.setKeyCode("odName");
    objDTO1.setValue("ODNAME");
    objDTO1.setType("2");
    ObjKeyValueVsmartDTO objDTO2 = Mockito.spy(ObjKeyValueVsmartDTO.class);
    objDTO2.setKeyCode("receiveUnitCode");
    objDTO2.setValue("ODNAME");
    ObjKeyValueVsmartDTO objDTO3 = Mockito.spy(ObjKeyValueVsmartDTO.class);
    objDTO3.setKeyCode("priorityCode");
    objDTO3.setValue("ODNAME");
    ObjKeyValueVsmartDTO objDTO4 = Mockito.spy(ObjKeyValueVsmartDTO.class);
    objDTO4.setKeyCode("odDescription");
    objDTO4.setValue("ODNAME");
    ObjKeyValueVsmartDTO objDTO5 = Mockito.spy(ObjKeyValueVsmartDTO.class);
    objDTO5.setKeyCode("startTime");
    objDTO5.setValue("22/08/2020 00:00:00");
    ObjKeyValueVsmartDTO objDTO6 = Mockito.spy(ObjKeyValueVsmartDTO.class);
    objDTO6.setKeyCode("endTime");
    objDTO6.setValue("23/08/2020 00:00:00");
    ObjKeyValueVsmartDTO objDTO7 = Mockito.spy(ObjKeyValueVsmartDTO.class);
    objDTO7.setKeyCode("odFile");
    objDTO7.setControlType("Uploader");
    o.add(objDTO1);
    o.add(objDTO2);
    o.add(objDTO3);
    o.add(objDTO4);
    o.add(objDTO5);
    o.add(objDTO6);
    o.add(objDTO7);

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    OdDTO odDTO = Mockito.spy(OdDTO.class);
    odDTO.setInsertSource("InsertSource");
    odDTO.setCreatePersonId(3333L);
    UsersInsideDto cu = Mockito.spy(UsersInsideDto.class);
    cu.setUserId(9999L);
    OdTypeDTO odType = Mockito.spy(OdTypeDTO.class);
    odType.setOdTypeId(5555L);
    UnitEntity unitEntity = Mockito.spy(UnitEntity.class);
    unitEntity.setUnitId(1111L);
    CatItemEntity pri = Mockito.spy(CatItemEntity.class);
    pri.setItemId(2222L);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);

    PowerMockito.when(
        commonBusiness.getUserByUserName(anyString())
    ).thenReturn(cu);
    PowerMockito.when(
        odCommonRepository.checkOdTypeExist(anyString())
    ).thenReturn(odType);
    PowerMockito.when(
        odCommonRepository.getUnitByUnitCode(anyString())
    ).thenReturn(unitEntity);
    PowerMockito.when(
        catItemRepository.getItemByItemCode(anyString())
    ).thenReturn(pri);
    PowerMockito.when(
        commonBusiness.updateObjectData(any(), any())
    ).thenReturn(odDTO);
    PowerMockito.when(
        odCommonRepository.getSeqTableOD(anyString())
    ).thenReturn("3333");
    PowerMockito.when(
        odRepository.add(any())
    ).thenReturn(result);
    PowerMockito.when(
        commonBusiness.getUserByUserId(anyLong())
    ).thenReturn(usersInsideDto);
    PowerMockito.when(
        odHistoryBusiness.insertOdHistory(any(), any(), anyLong(), anyString(), any())
    ).thenReturn(result);
    PowerMockito.when(
        odRelationBusiness.insertLstRelation(any())
    ).thenReturn(result);

    ResultDTO actual = odCommonBusiness
        .insertOdFromVsmart(o, "thanhlv12","1111", "2222", "3333", "CGA716632", "87878");

    Assert.assertNotNull(actual);
  }

  @Test
  public void insertOdFromOtherSystem() {
  }

  @Test
  public void exportData() throws Exception{
    PowerMockito.mockStatic(CommonExport.class);

    OdSearchInsideDTO odSearchInsideDTO = Mockito.spy(OdSearchInsideDTO.class);
    List<OdSearchInsideDTO> odSearchInsideDTOS = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        odCommonRepository.getListDataExport(any())
    ).thenReturn(odSearchInsideDTOS);

    File actual = odCommonBusiness.exportData(odSearchInsideDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void prepareDataInsertVsmart() {
  }

  @Test
  public void prepareDataInsert() {
  }

  @Test
  public void sendMesseageCreateOd() {
  }

  @Test
  public void replaceSmsContent() {
  }

  @Test
  public void createMessage() {
  }

  @Test
  public void updateFile() {
  }
}
