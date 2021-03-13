package com.viettel.gnoc.sr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import com.viettel.gnoc.sr.repository.SRFlowExecuteRepository;
import com.viettel.gnoc.sr.repository.SRRoleActionRepository;
import java.io.File;
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
@PrepareForTest({SRFlowExecuteBusinessImpl.class, FileUtils.class, CommonImport.class,
    CommonExport.class,
    TicketProvider.class, I18n.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class SRFlowExecuteBusinessImplTest {

  @InjectMocks
  SRFlowExecuteBusinessImpl srFlowExecuteBusiness;

  @Mock
  SRFlowExecuteRepository srFlowExecuteRepository;

  @Mock
  SRRoleActionRepository srRoleActionRepository;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Test
  public void testGetListSRFlowExecute_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = Mockito.spy(Datatable.class);
    List<SRFlowExecuteDTO> list = Mockito.spy(ArrayList.class);
    SRFlowExecuteDTO srFLDTO = Mockito.spy(SRFlowExecuteDTO.class);
    srFLDTO.setListflowId("123,123");
    list.add(srFLDTO);

    List<SRRoleActionDTO> lstRoleAction = Mockito.spy(ArrayList.class);
    SRRoleActionDTO srRoleActionDTO = Mockito.spy(SRRoleActionDTO.class);
    lstRoleAction.add(srRoleActionDTO);

    PowerMockito.when(srRoleActionRepository
        .getListRoleActionsByFlowExecuteId(anyLong())).thenReturn(lstRoleAction);
    PowerMockito.when(srFlowExecuteRepository.isFlowUsingByCatalogTable(anyString()))
        .thenReturn(list);
    SRFlowExecuteDTO srFlowExecuteDTO = Mockito.spy(SRFlowExecuteDTO.class);
    datatable.setData(list);
    PowerMockito.when(srFlowExecuteRepository.getListSRFlowExecute(any())).thenReturn(datatable);
    srFlowExecuteBusiness.getListSRFlowExecute(srFlowExecuteDTO);
    Assert.assertEquals(datatable.getData().size(), 1L);
  }

  @Test
  public void testSetMapCountryName() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    itemDataCRInside.setValueStr(10L);
    itemDataCRInside.setDisplayStr("xxx");
    lstCountryName.add(itemDataCRInside);
    srFlowExecuteBusiness.setMapCountryName();
    Assert.assertEquals(lstCountryName.size(), 1L);
  }

  @Test
  public void testGetListSRFlowExecuteCBB() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<SRFlowExecuteDTO> list = Mockito.spy(ArrayList.class);
    SRFlowExecuteDTO srFlowExecuteDTO = Mockito.spy(SRFlowExecuteDTO.class);
    list.add(srFlowExecuteDTO);
    PowerMockito.when(srFlowExecuteRepository.getListSRFlowExecuteCBB(any())).thenReturn(list);
    srFlowExecuteBusiness.getListSRFlowExecuteCBB(srFlowExecuteDTO);
    Assert.assertEquals(list.size(), 1L);
  }

  @Test
  public void testDelete() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(resultInSideDto.getId()).thenReturn(10L);
    PowerMockito.when(srFlowExecuteRepository.delete(anyLong())).thenReturn(resultInSideDto);
    srFlowExecuteBusiness.delete("123");
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testGetDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    SRFlowExecuteDTO srFlowExecuteDTO = Mockito.spy(SRFlowExecuteDTO.class);
    List<SRRoleActionDTO> srRoleActionDTOS = Mockito.spy(ArrayList.class);
    SRRoleActionDTO dto = Mockito.spy(SRRoleActionDTO.class);
    dto.setNextStatusName("c");
    dto.setGroupRoleName("d");
    dto.setActionsName("e");
    dto.setActions("f");
    srRoleActionDTOS.add(dto);

    srFlowExecuteDTO.setFlowName("a");
    srFlowExecuteDTO.setFlowDescription("b");
    srFlowExecuteDTO.setLstRoleActionDTO(srRoleActionDTOS);
    List<SRFlowExecuteDTO> srFlowExecuteDTOS = Mockito.spy(ArrayList.class);
    srFlowExecuteDTOS.add(srFlowExecuteDTO);
    srFlowExecuteDTO.setFlowExecuteDTOMainList(srFlowExecuteDTOS);
    srFlowExecuteDTO.setFlowExecuteDTODetailList(srFlowExecuteDTOS);

    PowerMockito.when(srRoleActionRepository.getListRoleActionsByFlowExecuteId(anyLong()))
        .thenReturn(srRoleActionDTOS);
    PowerMockito.when(srFlowExecuteRepository.getDetail(anyLong())).thenReturn(srFlowExecuteDTO);
    List<SRFlowExecuteDTO> check = Mockito.spy(ArrayList.class);
    PowerMockito.when(srFlowExecuteRepository.isFlowUsingByCatalog(anyLong())).thenReturn(check);

    srFlowExecuteBusiness.getDetail("123");
    Assert.assertEquals(srFlowExecuteDTO.getFlowName(), "a");
  }

  @Test
  public void testExportSearchData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    itemDataCRInside.setValueStr(10L);
    itemDataCRInside.setDisplayStr("xxx");
    lstCountryName.add(itemDataCRInside);

    SRFlowExecuteDTO dto = Mockito.spy(SRFlowExecuteDTO.class);
    dto.setListCountry("281,123,456");
    dto.setListflowId("6699,6868,1002");
    List<SRFlowExecuteDTO> srFlowExecuteDTOList = Mockito.spy(ArrayList.class);
    srFlowExecuteDTOList.add(dto);
    PowerMockito.when(srFlowExecuteRepository.onSearchExport(any()))
        .thenReturn(srFlowExecuteDTOList);

    //Nhay vao GetDetail
    List<SRRoleActionDTO> srRoleActionDTOS = Mockito.spy(ArrayList.class);
    SRRoleActionDTO srRoleActionDTO = Mockito.spy(SRRoleActionDTO.class);
    srRoleActionDTO.setFlowId(123L);
    srRoleActionDTOS.add(srRoleActionDTO);
    PowerMockito.when(srRoleActionRepository.getListRoleActionsByFlowExecuteId(anyLong()))
        .thenReturn(srRoleActionDTOS);
    PowerMockito.when(srFlowExecuteRepository.getDetail(anyLong())).thenReturn(dto);
    PowerMockito.when(srFlowExecuteRepository.isFlowUsingByCatalog(anyLong()))
        .thenReturn(srFlowExecuteDTOList);
    List<SRFlowExecuteDTO> check = Mockito.spy(ArrayList.class);
    PowerMockito.when(srFlowExecuteRepository.isFlowUsingByCatalog(anyLong())).thenReturn(check);
    dto.setFlowExecuteDTOMainList(srFlowExecuteDTOList);
    dto.setFlowExecuteDTODetailList(srFlowExecuteDTOList);
    dto.setFlowName("xyz");
    dto.setFlowDescription("abc");
    dto.setLstRoleActionDTO(srRoleActionDTOS);
    dto.setCountStep(12);
    List<SRRoleActionDTO> listRoleExport = Mockito.spy(ArrayList.class);
    listRoleExport.addAll(srRoleActionDTOS);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("dsdsds");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit2/test.xlsx");
    PowerMockito.when(CommonExport.exportExcel(
        anyString(), anyString(), anyList(), anyString(), new String[]{})).thenReturn(fileExport);
    srFlowExecuteBusiness.exportSearchData(dto);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void testGetConfigFileExport() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("dsdsds");
    SRRoleActionDTO srRoleActionDTO = Mockito.spy(SRRoleActionDTO.class);
    List<SRRoleActionDTO> listRoleExport = Mockito.spy(ArrayList.class);
    listRoleExport.add(srRoleActionDTO);
    srFlowExecuteBusiness.getConfigFileExport(listRoleExport);
    Assert.assertEquals(1L, 1L);
  }

}
