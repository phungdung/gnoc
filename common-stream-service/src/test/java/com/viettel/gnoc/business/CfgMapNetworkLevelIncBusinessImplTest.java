package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({CfgMapNetworkLevelIncBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class CfgMapNetworkLevelIncBusinessImplTest {

  @InjectMocks
  CfgMapNetworkLevelIncBusinessImpl cfgMapNetworkLevelIncBusiness;

  @Mock
  TtServiceProxy ttServiceProxy;

  @Test
  public void getListCfgMapNetLevelIncTypeDTO() {
    List<CfgMapNetLevelIncTypeDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(ttServiceProxy.getListCfgMapNetLevelIncTypeDTO(any()))
        .thenReturn(expected);

    CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO = Mockito.spy(CfgMapNetLevelIncTypeDTO.class);
    List<CfgMapNetLevelIncTypeDTO> actual = cfgMapNetworkLevelIncBusiness
        .getListCfgMapNetLevelIncTypeDTO(cfgMapNetLevelIncTypeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getListCfgMapNetLevelIncTypeDatatable() {
    Datatable expected = Mockito.spy(Datatable.class);
    PowerMockito.when(ttServiceProxy.getListCfgMapNetLevelIncTypeDatatable(any()))
        .thenReturn(expected);

    CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO = Mockito.spy(CfgMapNetLevelIncTypeDTO.class);
    Datatable actual = cfgMapNetworkLevelIncBusiness
        .getListCfgMapNetLevelIncTypeDatatable(cfgMapNetLevelIncTypeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void updateCfgMapNetLevelIncType() {
    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(ttServiceProxy.updateCfgMapNetLevelIncType(any()))
        .thenReturn(expected);

    CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO = Mockito.spy(CfgMapNetLevelIncTypeDTO.class);
    ResultInSideDto actual = cfgMapNetworkLevelIncBusiness
        .updateCfgMapNetLevelIncType(cfgMapNetLevelIncTypeDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void deleteCfgMapNetLevelIncType() {
    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(ttServiceProxy.deleteCfgMapNetLevelIncType(anyLong()))
        .thenReturn(expected);

    ResultInSideDto actual = cfgMapNetworkLevelIncBusiness.deleteCfgMapNetLevelIncType(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void findCfgMapNetLevelIncTypeById() {
    CfgMapNetLevelIncTypeDTO expected = Mockito.spy(CfgMapNetLevelIncTypeDTO.class);
    PowerMockito.when(ttServiceProxy.findCfgMapNetLevelIncTypeById(anyLong()))
        .thenReturn(expected);

    CfgMapNetLevelIncTypeDTO actual = cfgMapNetworkLevelIncBusiness
        .findCfgMapNetLevelIncTypeById(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void insertCfgMapNetLevelIncType() {
    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(ttServiceProxy.insertCfgMapNetLevelIncType(any()))
        .thenReturn(expected);

    CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO = Mockito.spy(CfgMapNetLevelIncTypeDTO.class);
    ResultInSideDto actual = cfgMapNetworkLevelIncBusiness
        .insertCfgMapNetLevelIncType(cfgMapNetLevelIncTypeDTO);

    Assert.assertEquals(expected, actual);
  }
}
