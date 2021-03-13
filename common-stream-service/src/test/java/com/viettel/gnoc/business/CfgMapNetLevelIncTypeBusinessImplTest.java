package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.repository.CfgMapNetLevelIncTypeRepository;
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
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgMapNetLevelIncTypeBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class CfgMapNetLevelIncTypeBusinessImplTest {

  @InjectMocks
  CfgMapNetLevelIncTypeBusinessImpl cfgMapNetLevelIncTypeBusiness;

  @Mock
  CfgMapNetLevelIncTypeRepository cfgMapNetLevelIncTypeRepository;

  @Mock
  CatItemBusiness catItemBusiness;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  TicketProvider ticketProvider;

  @Test
  public void getListCfgMapNetLevelIncTypeDTO() {
    List<CfgMapNetLevelIncTypeDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(cfgMapNetLevelIncTypeRepository
        .getListCfgMapNetLevelIncTypeDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(expected);

    CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO = Mockito.spy(CfgMapNetLevelIncTypeDTO.class);
    List<CfgMapNetLevelIncTypeDTO> actual = cfgMapNetLevelIncTypeBusiness
        .getListCfgMapNetLevelIncTypeDTO(cfgMapNetLevelIncTypeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void setMapNetwork() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);

    List<CatItemDTO> lstArrayIncident = Mockito.spy(ArrayList.class);
    lstArrayIncident.add(catItemDTO);
    PowerMockito.when(catItemBusiness
        .getListCatItemDTOByListCategoryCode(any()))
        .thenReturn(lstArrayIncident);

    List<CatItemDTO> lstNetworkLevel = Mockito.spy(ArrayList.class);
    lstNetworkLevel.add(catItemDTO);
    PowerMockito.when(catItemBusiness
        .getListCatItemDTOByListCategoryCode(any()))
        .thenReturn(lstNetworkLevel);

    cfgMapNetLevelIncTypeBusiness.setMapNetwork();

    Assert.assertNotNull(catItemDTO);
  }

  @Test
  public void getListCfgMapNetLevelIncTypeDatatable() {
    CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO = Mockito.spy(CfgMapNetLevelIncTypeDTO.class);
    cfgMapNetLevelIncTypeDTO.setTroubleTypeId(1L);
    cfgMapNetLevelIncTypeDTO.setNetworkLevelId("aaa");
    List<CfgMapNetLevelIncTypeDTO> list = Mockito.spy(ArrayList.class);
    list.add(cfgMapNetLevelIncTypeDTO);
    Datatable expected = Mockito.spy(Datatable.class);
    expected.setData(list);
    PowerMockito.when(cfgMapNetLevelIncTypeRepository
        .getListCfgMapNetLevelIncTypeDatatable(any()))
        .thenReturn(expected);

    Datatable actual = cfgMapNetLevelIncTypeBusiness
        .getListCfgMapNetLevelIncTypeDatatable(cfgMapNetLevelIncTypeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void setParams() {
  }

  @Test
  public void updateCfgMapNetLevelIncType() {
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(cfgMapNetLevelIncTypeRepository
        .updateCfgMapNetLevelIncType(any()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO = Mockito.spy(CfgMapNetLevelIncTypeDTO.class);
    ResultInSideDto actual = cfgMapNetLevelIncTypeBusiness
        .updateCfgMapNetLevelIncType(cfgMapNetLevelIncTypeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void deleteCfgMapNetLevelIncType() {
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(cfgMapNetLevelIncTypeRepository
        .deleteCfgMapNetLevelIncType(anyLong()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto actual = cfgMapNetLevelIncTypeBusiness.deleteCfgMapNetLevelIncType(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void findCfgMapNetLevelIncTypeById() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> lstArrayIncident = Mockito.spy(ArrayList.class);
    lstArrayIncident.add(catItemDTO);
    PowerMockito.when(catItemBusiness
        .getListCatItemDTOByListCategoryCode(any()))
        .thenReturn(lstArrayIncident);
    List<CatItemDTO> lstNetworkLevel = Mockito.spy(ArrayList.class);
    lstNetworkLevel.add(catItemDTO);
    PowerMockito.when(catItemBusiness
        .getListCatItemDTOByListCategoryCode(any()))
        .thenReturn(lstNetworkLevel);

    CfgMapNetLevelIncTypeDTO expected = Mockito.spy(CfgMapNetLevelIncTypeDTO.class);
    expected.setTroubleTypeId(1L);
    expected.setNetworkLevelId("aaa");
    PowerMockito.when(cfgMapNetLevelIncTypeRepository
        .findCfgMapNetLevelIncTypeById(anyLong()))
        .thenReturn(expected);

    CfgMapNetLevelIncTypeDTO actual = cfgMapNetLevelIncTypeBusiness
        .findCfgMapNetLevelIncTypeById(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void insertCfgMapNetLevelIncType() {
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(cfgMapNetLevelIncTypeRepository
        .insertCfgMapNetLevelIncType(any()))
        .thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);

    CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO = Mockito.spy(CfgMapNetLevelIncTypeDTO.class);
    ResultInSideDto actual = cfgMapNetLevelIncTypeBusiness
        .insertCfgMapNetLevelIncType(cfgMapNetLevelIncTypeDTO);

    Assert.assertEquals(expected, actual);
  }
}
