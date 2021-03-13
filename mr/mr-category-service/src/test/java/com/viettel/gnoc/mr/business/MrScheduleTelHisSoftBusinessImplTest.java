package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisSoftRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrScheduleTelHisSoftBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
public class MrScheduleTelHisSoftBusinessImplTest {

  @InjectMocks
  MrScheduleTelHisSoftBusinessImpl mrScheduleTelHisSoftBusiness;

  @Mock
  MrScheduleTelHisSoftRepository mrScheduleTelHisSoftRepository;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  HashMap<Long, String> mapMarket;

  @Mock
  HashMap<String, CatItemDTO> mapArray;

  @Mock
  HashMap<String, String> mapStateName;


  @Test
  public void getListDataSoftSearchWeb() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    mrScheduleTelHisDTO.setMrDeviceHisId(5L);
    mrScheduleTelHisDTO.setMrId("5");
    List<MrScheduleTelHisDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrScheduleTelHisDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(mrScheduleTelHisSoftRepository.getListDataSoftSearchWeb(any()))
        .thenReturn(datatable);
    datatable.setData(list);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");

    Datatable dataArray = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setCategoryId(5l);
    List<CatItemDTO> listCat = Mockito.spy(ArrayList.class);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataArray);
    listCat.add(catItemDTO);
    dataArray.setData(listCat);

    Datatable datatable1 = mrScheduleTelHisSoftBusiness
        .getListDataSoftSearchWeb(mrScheduleTelHisDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void test_exportDataMrScheduleTelHisSoft() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    PowerMockito.mockStatic(CommonExport.class);

    Datatable dataArray = Mockito.spy(Datatable.class);
    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);

    mrScheduleTelHisDTO.setMrId("5");
    mrScheduleTelHisDTO.setMarketCode("281");
    mrScheduleTelHisDTO.setArrayCode("54");
    mrScheduleTelHisDTO.setNodeStatus("5");
    List<MrScheduleTelHisDTO> list = Mockito.spy(ArrayList.class);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> listCat = Mockito.spy(ArrayList.class);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataArray);
    listCat.add(catItemDTO);
    dataArray.setData(listCat);

    PowerMockito.when(mrScheduleTelHisSoftRepository.getListMrScheduleTelHisSoftExport(any()))
        .thenReturn(list);
    list.add(mrScheduleTelHisDTO);

    File file = PowerMockito.mock(File.class);
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), anyString()))
        .thenReturn(file);
    File result = mrScheduleTelHisSoftBusiness.exportDataMrScheduleTelHisSoft(mrScheduleTelHisDTO);
    assertNull(result);
  }

  @Test
  public void handleFileExport() {
  }

  @Test
  public void setValueDetailSoft() {
  }

  @Test
  public void setMapMarket() {
  }

  @Test
  public void setMapArray() {
  }

  @Test
  public void setMapStateName() {
  }
}
