package com.viettel.gnoc.business;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CfgFollowWorkTimeDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.repository.CfgFollowWorkTimeRepository;
import java.util.ArrayList;
import java.util.List;
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
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgFollowWorkTimeBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class CfgFollowWorkTimeBusinessImplTest {

  @InjectMocks
  CfgFollowWorkTimeBusinessImpl cfgFollowWorkTimeBusiness;

  @Mock
  CfgFollowWorkTimeRepository cfgFollowWorkTimeRepository;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Test
  public void getListCfgFollowWorkTimePage() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable expected = Mockito.spy(Datatable.class);
    PowerMockito.when(cfgFollowWorkTimeRepository.getListCfgFollowWorkTimePage(any()))
        .thenReturn(expected);

    CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO = Mockito.spy(CfgFollowWorkTimeDTO.class);
    Datatable actual = cfgFollowWorkTimeBusiness.getListCfgFollowWorkTimePage(cfgFollowWorkTimeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void insert() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(cfgFollowWorkTimeRepository.insertOrUpdate(any()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO = Mockito.spy(CfgFollowWorkTimeDTO.class);
    ResultInSideDto actual = cfgFollowWorkTimeBusiness.insert(cfgFollowWorkTimeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void update_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(cfgFollowWorkTimeRepository.insertOrUpdate(any()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO = Mockito.spy(CfgFollowWorkTimeDTO.class);
    cfgFollowWorkTimeDTO.setSystemOLD("aaa");
    cfgFollowWorkTimeDTO.setSystem("WO");
    cfgFollowWorkTimeDTO.setConfigFollowWorkTimeId(1L);
    ResultInSideDto actual = cfgFollowWorkTimeBusiness.update(cfgFollowWorkTimeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void update_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(cfgFollowWorkTimeRepository.insertOrUpdate(any()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO = Mockito.spy(CfgFollowWorkTimeDTO.class);
    cfgFollowWorkTimeDTO.setSystemOLD("aaa");
    cfgFollowWorkTimeDTO.setSystem("PT");
    cfgFollowWorkTimeDTO.setConfigFollowWorkTimeId(1L);
    ResultInSideDto actual = cfgFollowWorkTimeBusiness.update(cfgFollowWorkTimeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void update_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(cfgFollowWorkTimeRepository.insertOrUpdate(any()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO = Mockito.spy(CfgFollowWorkTimeDTO.class);
    cfgFollowWorkTimeDTO.setSystemOLD("aaa");
    cfgFollowWorkTimeDTO.setSystem("TT");
    cfgFollowWorkTimeDTO.setConfigFollowWorkTimeId(1L);
    ResultInSideDto actual = cfgFollowWorkTimeBusiness.update(cfgFollowWorkTimeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void update_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(cfgFollowWorkTimeRepository.insertOrUpdate(any()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO = Mockito.spy(CfgFollowWorkTimeDTO.class);
    cfgFollowWorkTimeDTO.setSystemOLD("aaa");
    cfgFollowWorkTimeDTO.setSystem("CR");
    cfgFollowWorkTimeDTO.setConfigFollowWorkTimeId(1L);
    ResultInSideDto actual = cfgFollowWorkTimeBusiness.update(cfgFollowWorkTimeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void delete() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(cfgFollowWorkTimeRepository.delete(anyLong()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto actual = cfgFollowWorkTimeBusiness.delete(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    CfgFollowWorkTimeDTO expected = Mockito.spy(CfgFollowWorkTimeDTO.class);
    PowerMockito.when(cfgFollowWorkTimeRepository.getDetail(anyLong()))
        .thenReturn(expected);

    CfgFollowWorkTimeDTO actual = cfgFollowWorkTimeBusiness.getDetail(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getListItemByCategory_01() {
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> itemDataCRInsides = Mockito.spy(ArrayList.class);
    itemDataCRInsides.add(itemDataCRInside);
    PowerMockito.when(crServiceProxy.getListImpactSegmentCBB())
        .thenReturn(itemDataCRInsides);

    List<CatItemDTO> actual = cfgFollowWorkTimeBusiness.getListItemByCategory("CR");

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListItemByCategory_02() {
    List<CatItemDTO> expected = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> itemDataCRInsides = Mockito.spy(ArrayList.class);
    itemDataCRInsides.add(itemDataCRInside);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any()))
        .thenReturn(expected);

    List<CatItemDTO> actual = cfgFollowWorkTimeBusiness.getListItemByCategory("WO");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getListItemByCategory_03() {
    List<CatItemDTO> expected = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> itemDataCRInsides = Mockito.spy(ArrayList.class);
    itemDataCRInsides.add(itemDataCRInside);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any()))
        .thenReturn(expected);

    List<CatItemDTO> actual = cfgFollowWorkTimeBusiness.getListItemByCategory("TT");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getListItemByCat_01() {
    PowerMockito.mockStatic(I18n.class);
    List<CatItemDTO> actual = cfgFollowWorkTimeBusiness.getListItemByCat("CR");
    Assert.assertNotNull(actual);
  }

  @Test
  public void getListItemByCat_02() {
    PowerMockito.mockStatic(I18n.class);
    List<CatItemDTO> actual = cfgFollowWorkTimeBusiness.getListItemByCat("WO");
    Assert.assertNotNull(actual);
  }

  @Test
  public void getListItemByCat_03() {
    List<CatItemDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), any()))
        .thenReturn(expected);

    List<CatItemDTO> actual = cfgFollowWorkTimeBusiness.getListItemByCat("TT");

    Assert.assertEquals(expected, actual);
  }
}
