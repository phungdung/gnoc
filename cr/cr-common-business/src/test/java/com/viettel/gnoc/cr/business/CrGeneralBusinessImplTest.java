package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ItemDataCRDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.dto.UserCabCrForm;
import com.viettel.gnoc.cr.repository.CrGeneralRepository;
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
import org.springframework.test.util.ReflectionTestUtils;
import viettel.passport.client.UserTokenSSO2;
import viettel.passport.service.SSOServiceUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrGeneralBusinessImpl.class, FileUtils.class, I18n.class, DataUtil.class,
    DateTimeUtils.class, SSOServiceUtils.class, UserTokenSSO2.class, CrGeneralBusiness.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrGeneralBusinessImplTest {

  @InjectMocks
  CrGeneralBusinessImpl crGeneralBusiness;

  @Mock
  CrGeneralRepository crGeneralRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(crGeneralBusiness, "domainCode1",
        "xcxc");
    ReflectionTestUtils.setField(crGeneralBusiness, "versionApp1",
        "9.6");
    ReflectionTestUtils.setField(crGeneralBusiness, "flagRequire",
        "false");
    ReflectionTestUtils.setField(crGeneralBusiness, "ticketServiceUrl1",
        "/abc");
  }

  @Test
  public void testGetListSubcategoryCBB() {
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setSecondValue("cbjv");
    list.add(itemDataCRInside);
    PowerMockito.when(crGeneralRepository.getListSubcategoryCBB(anyString())).thenReturn(list);
    List<ItemDataCRInside> list1 = crGeneralBusiness.getListSubcategoryCBB();
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListImpactSegmentCBB_01() {
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setSecondValue("cbjv");
    list.add(itemDataCRInside);
    PowerMockito.when(crGeneralRepository.getListImpactSegmentCBB(anyString())).thenReturn(list);
    List<ItemDataCRInside> list1 = crGeneralBusiness.getListImpactSegmentCBB();
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListImpactAffectCBB() {
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setSecondValue("cbjv");
    list.add(itemDataCRInside);
    PowerMockito.when(crGeneralRepository.getListImpactAffectCBB(anyString())).thenReturn(list);
    List<ItemDataCRInside> list1 = crGeneralBusiness.getListImpactAffectCBB();
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListImpactAffectCBB1() {
    List<ItemDataCR> list = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("cbjv");
    list.add(itemDataCR);
    Object form = Mockito.spy(Object.class);
    PowerMockito.when(crGeneralRepository.getListImpactAffectCBBForService(form, "en_US"))
        .thenReturn(list);
    List<ItemDataCR> list1 = crGeneralBusiness.getListImpactAffectCBB(form, "en_US");
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListAffectedServiceCBB() {
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setSecondValue("cbjv");
    list.add(itemDataCRInside);
    PowerMockito.when(crGeneralRepository.getListAffectedServiceCBB(anyLong(), anyString()))
        .thenReturn(list);
    List<ItemDataCRInside> list1 = crGeneralBusiness.getListAffectedServiceCBB(1L);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListDutyTypeCBB() {
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setSecondValue("cbjv");
    list.add(itemDataCRInside);
    CrImpactFrameInsiteDTO form = Mockito.spy(CrImpactFrameInsiteDTO.class);
    form.setDescription("abc");
    PowerMockito.when(crGeneralRepository.getListDutyTypeCBB(form, "en_US")).thenReturn(list);
    List<ItemDataCRInside> list1 = crGeneralBusiness.getListDutyTypeCBB(form);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListDutyTypeCBB1() {
    List<ItemDataCR> list = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("cbjv");
    list.add(itemDataCR);
    CrImpactFrameDTO form = Mockito.spy(CrImpactFrameDTO.class);
    form.setDescription("abc");
    PowerMockito.when(crGeneralRepository.getListDutyTypeCBB(form, "en_US")).thenReturn(list);
    List<ItemDataCR> list1 = crGeneralBusiness.getListDutyTypeCBB(form, "en_US");
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListDeviceTypeByImpactSegmentCBB() {
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setSecondValue("cbjv");
    list.add(itemDataCRInside);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrId("1");
    crDTO.setCrNumber("1");
    PowerMockito.when(crGeneralRepository.getListDeviceType(crDTO, "en_US")).thenReturn(list);
    List<ItemDataCRInside> list1 = crGeneralBusiness.getListDeviceTypeByImpactSegmentCBB(crDTO);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListActionCodeByCode() {
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setSecondValue("cbjv");
    list.add(itemDataCRInside);
    PowerMockito.when(crGeneralRepository.getListActionCodeByCode(anyString(), anyString()))
        .thenReturn(list);
    List<ItemDataCRInside> list1 = crGeneralBusiness.getListActionCodeByCode("abc", "def");
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testActionGetListUser() {
    List<UsersInsideDto> list = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    list.add(usersInsideDto);
    PowerMockito.when(crGeneralRepository.actionGetListUser(
        anyString(), anyString(), anyString(), anyString(),
        anyString(), anyString(), anyString(), anyString())).thenReturn(list);

    List<UsersInsideDto> list1 = crGeneralBusiness
        .actionGetListUser("1", "2", "a", "b", "v", "w", "f", "t");
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListReturnCodeByActionCode() {
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setSecondValue("cbjv");
    list.add(itemDataCRInside);
    PowerMockito.when(crGeneralRepository.getListReturnCodeByActionCode(anyLong(), anyString()))
        .thenReturn(list);
    List<ItemDataCRInside> list1 = crGeneralBusiness.getListReturnCodeByActionCode(1L, "def");
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetCreatedBySys() {
    List<ItemDataCRDTO> list = Mockito.spy(ArrayList.class);
    ItemDataCRDTO itemDataCRDTO = Mockito.spy(ItemDataCRDTO.class);
    itemDataCRDTO.setSecondValue("cbjv");
    list.add(itemDataCRDTO);
    PowerMockito.when(crGeneralRepository.getCreatedBySys(anyString())).thenReturn(list);
    List<ItemDataCRDTO> list1 = crGeneralBusiness.getCreatedBySys("def");
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetCbbChildArray() {
    List<CfgChildArrayDTO> list = Mockito.spy(ArrayList.class);
    CfgChildArrayDTO dto = Mockito.spy(CfgChildArrayDTO.class);
    dto.setChildrenName("cbjv");
    list.add(dto);
    PowerMockito.when(crGeneralRepository.getCbbChildArray(any())).thenReturn(list);
    List<CfgChildArrayDTO> list1 = crGeneralBusiness.getCbbChildArray(dto);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListUserCab() {
    List<UserCabCrForm> list = Mockito.spy(ArrayList.class);
    UserCabCrForm dto = Mockito.spy(UserCabCrForm.class);
    dto.setCreateUnitId("cbjv");
    list.add(dto);
    PowerMockito.when(crGeneralRepository.getListUserCab(anyString(), anyString()))
        .thenReturn(list);
    List<UserCabCrForm> list1 = crGeneralBusiness.getListUserCab("abc", "cd");
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testGetListUnit() {
    List<UnitDTO> list = Mockito.spy(ArrayList.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitName("cbjv");
    list.add(unitDTO);
    PowerMockito.when(crGeneralRepository.getListUnit(any())).thenReturn(list);
    List<UnitDTO> list1 = crGeneralBusiness.getListUnit(unitDTO);
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void testInsertCrCreatedFromOtherSystem() {
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    result.setId(null);
    result.setMessage("ccc");
    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setCrId("1");
    PowerMockito.when(crGeneralRepository.insertCrCreatedFromOtherSystem(any())).thenReturn("ccc");
    ResultInSideDto resultInSideDto = crGeneralBusiness
        .insertCrCreatedFromOtherSystem(crCreatedFromOtherSysDTO);
    Assert.assertEquals(resultInSideDto.getMessage(), result.getMessage());
  }

  @Test
  public void testGetUserInfoForMobile_01() {
    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUsername("thanhlv12");
    usersDTO.setUserId("10");
    usersDTO.setUnitId("10");
    usersDTO.setFullname("Nguyen Van Thanh");
    usersDTO.setUnitName("CCC");
    usersDTO.setUserTimeZone("12/05/2020");
    PowerMockito.when(crGeneralRepository.getUserInfoForMobile(anyString())).thenReturn(usersDTO);
    ObjResponse objResponse = crGeneralBusiness.getUserInfoForMobile("thanhlv12", "en_US");
    Assert.assertNotNull(objResponse);
  }

  @Test
  public void testGetUserInfoForMobile_02() {
    UsersDTO usersDTO = null;
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getMessages(anyString())).thenReturn("xxx");
    PowerMockito.when(crGeneralRepository.getUserInfoForMobile(anyString())).thenReturn(usersDTO);
    ObjResponse objResponse = crGeneralBusiness.getUserInfoForMobile("thanhlv12", "en_US");
    Assert.assertNotNull(objResponse);
  }

  @Test
  public void testGetListScopeOfUserForAllRole_01() {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("10");
    List<ItemDataCR> itemDataCRList = crGeneralBusiness
        .getListScopeOfUserForAllRole(crDTO, "en_US");
    Assert.assertNotNull(itemDataCRList);
  }

  @Test
  public void testGetListScopeOfUserForAllRole_02() {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("10");
    crDTO.setUserLogin("thanhlv12");

    List<ItemDataCR> itemDataCRList = crGeneralBusiness
        .getListScopeOfUserForAllRole(crDTO, "en_US");
    Assert.assertNotNull(itemDataCRList);
  }

  @Test
  public void testGetListScopeOfUserForAllRole_03() {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("10");
    crDTO.setUserLogin("thanhlv12");
    crDTO.setUserLoginUnit("thanhlv12");

    List<ItemDataCR> itemDataCRList = crGeneralBusiness
        .getListScopeOfUserForAllRole(crDTO, "en_US");
    Assert.assertNotNull(itemDataCRList);
  }

  @Test
  public void testGetListScopeOfUserForAllRole_04() {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("10");
    crDTO.setUserLogin("thanhlv12");
    crDTO.setUserLoginUnit("12");
    crDTO.setSearchType("9");
    List<ItemDataCR> lst = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgjo");
    lst.add(itemDataCR);
    PowerMockito.when(crGeneralRepository.getListScopeOfUserNewForServiceV2(anyLong(), anyString()))
        .thenReturn(lst);

    List<ItemDataCR> itemDataCRList = crGeneralBusiness
        .getListScopeOfUserForAllRole(crDTO, "en_US");
    Assert.assertEquals(itemDataCRList.size(), lst.size());
  }

  @Test
  public void testGetListScopeOfUserForAllRole_05() {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("10");
    crDTO.setUserLogin("thanhlv12");
    crDTO.setUserLoginUnit("12");
    crDTO.setSearchType("11");
    List<ItemDataCR> lst = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgjo");
    lst.add(itemDataCR);
    PowerMockito
        .when(crGeneralRepository.getListScopeOfUserOfCabOrZ78ForServiceV2(any(), anyString()))
        .thenReturn(lst);

    List<ItemDataCR> itemDataCRList = crGeneralBusiness
        .getListScopeOfUserForAllRole(crDTO, "en_US");
    Assert.assertEquals(itemDataCRList.size(), lst.size());
  }

  @Test
  public void testGetListScopeOfUserForAllRole_06() {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("10");
    crDTO.setUserLogin("thanhlv12");
    crDTO.setUserLoginUnit("12");
    crDTO.setSearchType("8");
    List<ItemDataCR> lst = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgjo");
    lst.add(itemDataCR);
    PowerMockito.when(crGeneralRepository.getListScopeOfUserNewForServiceV2(anyLong(), anyString()))
        .thenReturn(lst);

    List<ItemDataCR> itemDataCRList = crGeneralBusiness
        .getListScopeOfUserForAllRole(crDTO, "en_US");
    Assert.assertEquals(itemDataCRList.size(), lst.size());
  }

  @Test
  public void testGetListScopeOfUserForAllRole_07() {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("10");
    crDTO.setUserLogin("thanhlv12");
    crDTO.setUserLoginUnit("12");
    crDTO.setSearchType("4");
    List<ItemDataCR> lst = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgjo");
    lst.add(itemDataCR);
    PowerMockito.when(crGeneralRepository.getListScopeOfUserNewForServiceV2(anyLong(), anyString()))
        .thenReturn(lst);

    List<ItemDataCR> itemDataCRList = crGeneralBusiness
        .getListScopeOfUserForAllRole(crDTO, "en_US");
    Assert.assertEquals(itemDataCRList.size(), lst.size());
  }

  @Test
  public void testGetListScopeOfUserForAllRole_08() {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("10");
    crDTO.setUserLogin("thanhlv12");
    crDTO.setUserLoginUnit("12");
    crDTO.setSearchType("2");
    List<ItemDataCR> lst = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgjo");
    lst.add(itemDataCR);
    PowerMockito.when(crGeneralRepository.getListScopeOfUserNewForServiceV2(anyLong(), anyString()))
        .thenReturn(lst);

    List<ItemDataCR> itemDataCRList = crGeneralBusiness
        .getListScopeOfUserForAllRole(crDTO, "en_US");
    Assert.assertEquals(itemDataCRList.size(), lst.size());
  }


  @Test
  public void getListImpactSegmentCBBForServiceV2() {
    List<ItemDataCR> itemDataCRList = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgh");
    itemDataCRList.add(itemDataCR);
    PowerMockito.when(crGeneralRepository.getListImpactSegmentCBBForServiceV2(anyString()))
        .thenReturn(itemDataCRList);
    List<ItemDataCR> dataCRList = crGeneralBusiness.getListImpactSegmentCBBForServiceV2("en_US");
    Assert.assertEquals(dataCRList.size(), itemDataCRList.size());
  }

  @Test
  public void getListSubcategoryCBBForServiceV2() {
    List<UsersDTO> usersDTOS = Mockito.spy(ArrayList.class);
    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUnitName("asd");
    usersDTOS.add(usersDTO);
    PowerMockito.when(crGeneralRepository
        .actionGetListUserForService(anyString(), anyString(), anyString(), anyString(),
            anyString(), anyString(), anyString(), anyString())).thenReturn(usersDTOS);
    List<UsersDTO> list = crGeneralBusiness.actionGetListUserForService(
        "1", "1", "a", "c", "c", "f", "c", "e");
    Assert.assertEquals(list.size(), usersDTOS.size());
  }

  @Test
  public void getListSubcategoryCBBForServiceV2_02() {
    List<ItemDataCR> itemDataCRList = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("asd");
    itemDataCRList.add(itemDataCR);
    PowerMockito.when(crGeneralRepository.getListSubcategoryCBBForServiceV2(anyString()))
        .thenReturn(itemDataCRList);
    List<ItemDataCR> list = crGeneralBusiness.getListSubcategoryCBBForServiceV2("en_US");
    Assert.assertEquals(list.size(), itemDataCRList.size());
  }

  @Test
  public void getListReturnCodeByActionCodeForService() {
    List<ItemDataCR> itemDataCRList = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgh");
    itemDataCRList.add(itemDataCR);
    PowerMockito
        .when(crGeneralRepository.getListReturnCodeByActionCodeForService(anyLong(), anyString()))
        .thenReturn(itemDataCRList);
    List<ItemDataCR> dataCRList = crGeneralBusiness
        .getListReturnCodeByActionCodeForService("1", "en_US");
    Assert.assertEquals(dataCRList.size(), itemDataCRList.size());
  }

  @Test
  public void getListAffectedServiceCBB() {
    List<ItemDataCR> itemDataCRList = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgh");
    itemDataCRList.add(itemDataCR);
    Object form = Mockito.spy(Object.class);
    PowerMockito.when(crGeneralRepository.getListAffectedServiceCBBForService(any(), anyString()))
        .thenReturn(itemDataCRList);
    List<ItemDataCR> dataCRList = crGeneralBusiness.getListAffectedServiceCBB(form, "en_US");
    Assert.assertEquals(dataCRList.size(), itemDataCRList.size());
  }


  @Test
  public void testGetListDeviceTypeCBB() {
    List<ItemDataCR> itemDataCRList = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgh");
    itemDataCRList.add(itemDataCR);
    Object form = Mockito.spy(Object.class);
    PowerMockito.when(crGeneralRepository.getListDeviceTypeCBB(form, "en_US"))
        .thenReturn(itemDataCRList);
    List<ItemDataCR> dataCRList = crGeneralBusiness.getListDeviceTypeCBB(form, "en_US");
    Assert.assertEquals(dataCRList.size(), itemDataCRList.size());


  }

  @Test
  public void testGetListDeviceTypeByImpactSegmentCBB1() {
    List<ItemDataCR> itemDataCRList = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgh");
    itemDataCRList.add(itemDataCR);
    CrDTO form = Mockito.spy(CrDTO.class);
    form.setSearchType("C");

    PowerMockito.when(crGeneralRepository.getListDeviceType(form, "en_US"))
        .thenReturn(itemDataCRList);
    List<ItemDataCR> dataCRList = crGeneralBusiness
        .getListDeviceTypeByImpactSegmentCBB(form, "en_US");
    Assert.assertEquals(dataCRList.size(), itemDataCRList.size());
  }

  @Test
  public void testGetListActionCodeByCodeForService() {
    List<ItemDataCR> itemDataCRList = Mockito.spy(ArrayList.class);
    ItemDataCR itemDataCR = Mockito.spy(ItemDataCR.class);
    itemDataCR.setSecondValue("fgh");
    itemDataCRList.add(itemDataCR);
    PowerMockito
        .when(crGeneralRepository.getListActionCodeByCodeForService(anyString(), anyString()))
        .thenReturn(itemDataCRList);
    List<ItemDataCR> dataCRList = crGeneralBusiness.getListActionCodeByCodeForService("1", "en_US");
    Assert.assertEquals(dataCRList.size(), itemDataCRList.size());
  }
}
