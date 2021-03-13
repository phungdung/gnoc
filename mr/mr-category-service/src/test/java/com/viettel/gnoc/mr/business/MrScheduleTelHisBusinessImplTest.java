package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrScheduleTelHisBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
public class MrScheduleTelHisBusinessImplTest {

  @InjectMocks
  MrScheduleTelHisBusinessImpl mrScheduleTelHisBusinessImpl;

  @Mock
  MrScheduleTelHisRepository mrScheduleTelHisRepository;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void test_getListMrScheduleTelHisPage() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    mrScheduleTelHisDTO.setMarketCode("281");
    Datatable datatable = Mockito.spy(Datatable.class);
    ArrayList listData = Mockito.spy(ArrayList.class);
    mrScheduleTelHisDTO.setMrDeviceHisId(89L);
    listData.add(mrScheduleTelHisDTO);
    datatable.setData(listData);
    PowerMockito.when(mrScheduleTelHisRepository.getListMrScheduleHardHisPage(any()))
        .thenReturn(datatable);
    Datatable data = mrScheduleTelHisBusinessImpl.getListMrScheduleTelHisPage(mrScheduleTelHisDTO);
    assertEquals(datatable.getPages(), data.getPages());
  }

  @Test
  public void test_exportSearchData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    I18n.getLocale();
    List<MrScheduleTelHisDTO> listTelHis = Mockito.spy(ArrayList.class);
//    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
//    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), null)).thenReturn(list);
    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    mrScheduleTelHisDTO.setMrDeviceHisId(89L);
    mrScheduleTelHisDTO.setMrMode("H");
    mrScheduleTelHisDTO.setNodeStatus("0");
    listTelHis.add(mrScheduleTelHisDTO);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(mrScheduleTelHisRepository.onSearchExport(any())).thenReturn(listTelHis);
    File file = PowerMockito.mock(File.class);
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), anyString()))
        .thenReturn(file);
    File result = mrScheduleTelHisBusinessImpl.exportSearchData(mrScheduleTelHisDTO);
    assertNull(result);
  }

  @Test
  public void test1_exportSearchData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    List<MrScheduleTelHisDTO> listTelHis = Mockito.spy(ArrayList.class);
    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    mrScheduleTelHisDTO.setMrDeviceHisId(89L);
    mrScheduleTelHisDTO.setNodeStatus("1");
    mrScheduleTelHisDTO.setMrMode("H");
    listTelHis.add(mrScheduleTelHisDTO);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(mrScheduleTelHisRepository.onSearchExport(any())).thenReturn(listTelHis);
    File file = new File("input.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), anyString()))
        .thenReturn(file);
    mrScheduleTelHisBusinessImpl.exportSearchData(mrScheduleTelHisDTO);
  }


  @Test
  public void test_setMapCountryName() {

  }
}
