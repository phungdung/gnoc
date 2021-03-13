package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CrAlarmFaultGroupDTO;
import com.viettel.gnoc.commons.dto.CrModuleDraftDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.incident.provider.WSIIMPort;
import com.viettel.gnoc.commons.incident.provider.WSNIMSInfraPort;
import com.viettel.gnoc.commons.incident.provider.WSNocprov4Port;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.cr.dto.CrAlarmDTO;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrModuleDetailDTO;
import com.viettel.gnoc.cr.dto.CrVendorDetailDTO;
import com.viettel.gnoc.cr.repository.CrAlarmRepository;
import com.viettel.nims.infra.webservice.CatVendorBO;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({CrAlarmBusinessImpl.class, DataUtil.class, I18n.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrAlarmBusinessImplTest {

  @InjectMocks
  CrAlarmBusinessImpl crAlarmBusiness;
  @Mock
  CrAlarmRepository crAlarmRepository;
  @Mock
  WSNocprov4Port wsNocprov4Port;
  @Mock
  WSNIMSInfraPort wsnimsInfraPort;
  @Mock
  WSIIMPort wsiimPort;

  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(crAlarmBusiness, "tempFolder",
        "./wo-upload");
  }

  @Before
  public void setUpNationCodeList() {
    ReflectionTestUtils.setField(crAlarmBusiness, "nationCodeList",
        "VNM; RUS; VNM");
  }

  @Test
  public void getListAlarmByCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CrAlarmInsiteDTO> crAlarmInsiteDTOS = Mockito.spy(ArrayList.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crAlarmRepository.getListAlarmByCr(any())).thenReturn(crAlarmInsiteDTOS);
    List<CrAlarmInsiteDTO> crAlarmInsiteDTOS1 = crAlarmBusiness.getListAlarmByCr(crInsiteDTO);
    Assert.assertEquals(crAlarmInsiteDTOS.size(), crAlarmInsiteDTOS1.size());
  }

  @Test
  public void getListFaultSrc_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrAlarmFaultGroupDTO data = Mockito.spy(CrAlarmFaultGroupDTO.class);
    data.setFault_src("vtnet");
    List<CrAlarmFaultGroupDTO> lstFaultGroupDTO = Mockito.spy(ArrayList.class);
    lstFaultGroupDTO.add(data);
    PowerMockito.when(wsNocprov4Port.getFautGroupInfo(anyString())).thenReturn(lstFaultGroupDTO);
    HashSet<String> strings = crAlarmBusiness.getListFaultSrc("VNM");
    Assert.assertEquals(strings.size(), 1L);
  }

  @Test
  public void getListFaultSrc_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrAlarmFaultGroupDTO data = Mockito.spy(CrAlarmFaultGroupDTO.class);
    data.setFault_src("vtnet");
    List<CrAlarmFaultGroupDTO> lstFaultGroupDTO = Mockito.spy(ArrayList.class);
    PowerMockito.when(wsNocprov4Port.getFautGroupInfo(anyString())).thenReturn(lstFaultGroupDTO);
    HashSet<String> strings = crAlarmBusiness.getListFaultSrc("VNM");
    Assert.assertNull(strings);
  }

  @Test
  public void getListGroupFaultSrc_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrAlarmFaultGroupDTO data = Mockito.spy(CrAlarmFaultGroupDTO.class);
    data.setFault_src("vtnet");
    List<CrAlarmFaultGroupDTO> lstFaultGroupDTO = Mockito.spy(ArrayList.class);
    lstFaultGroupDTO.add(data);
    PowerMockito.when(wsNocprov4Port.getFautGroupInfo(anyString())).thenReturn(lstFaultGroupDTO);
    List<CrAlarmFaultGroupDTO> crAlarmFaultGroupDTOS = crAlarmBusiness
        .getListGroupFaultSrc("vtnet", "VNM");
    Assert.assertEquals(lstFaultGroupDTO.size(), crAlarmFaultGroupDTOS.size());
  }

  @Test
  public void getListGroupFaultSrc_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrAlarmFaultGroupDTO data = Mockito.spy(CrAlarmFaultGroupDTO.class);
    data.setFault_src("vtnet");
    List<CrAlarmFaultGroupDTO> lstFaultGroupDTO = Mockito.spy(ArrayList.class);
    lstFaultGroupDTO.add(data);
    PowerMockito.when(wsNocprov4Port.getFautGroupInfo(anyString())).thenReturn(null);
    List<CrAlarmFaultGroupDTO> crAlarmFaultGroupDTOS = crAlarmBusiness
        .getListGroupFaultSrc("vtnet", "VNM");
    Assert.assertNull(crAlarmFaultGroupDTOS);
  }

  @Test
  public void getAlarmList_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrAlarmInsiteDTO crAlarmDTO = Mockito.spy(CrAlarmInsiteDTO.class);
    crAlarmDTO.setFaultSrc("vtnet");
    crAlarmDTO.setFaultName("vtnet");
    crAlarmDTO.setFaultGroupId(1L);
    crAlarmDTO.setNationCode("VNM");
    crAlarmDTO.setPage(1);
    crAlarmDTO.setPageSize(1);
    List<CrAlarmInsiteDTO> lstAlarm = Mockito.spy(ArrayList.class);
    lstAlarm.add(crAlarmDTO);
    PowerMockito.when(wsNocprov4Port.getAlarmList(any())).thenReturn(lstAlarm);
    Datatable datatable = crAlarmBusiness.getAlarmList(crAlarmDTO);
    Assert.assertEquals(datatable.getPages(), 1L);
  }

  @Test
  public void getAlarmSettingByVendor_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CrAlarmSettingDTO> crAlarmSettingDTOS = Mockito.spy(ArrayList.class);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    PowerMockito.when(crAlarmRepository.getAlarmSettingByVendor(any()))
        .thenReturn(crAlarmSettingDTOS);
    List<CrAlarmSettingDTO> crAlarmSettingDTOS1 = crAlarmBusiness
        .getAlarmSettingByVendor(crAlarmSettingDTO);
    Assert.assertEquals(crAlarmSettingDTOS.size(), crAlarmSettingDTOS1.size());
  }

  @Test
  public void getAlarmSettingByModule_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CrAlarmSettingDTO> crAlarmSettingDTOS = Mockito.spy(ArrayList.class);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    PowerMockito.when(crAlarmRepository.getAlarmSettingByModule(any()))
        .thenReturn(crAlarmSettingDTOS);
    List<CrAlarmSettingDTO> crAlarmSettingDTOS1 = crAlarmBusiness
        .getAlarmSettingByModule(crAlarmSettingDTO);
    Assert.assertEquals(crAlarmSettingDTOS.size(), crAlarmSettingDTOS1.size());
  }

  @Test
  public void getListAlarmSettingByModule_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CrAlarmSettingDTO> crAlarmSettingDTOS = Mockito.spy(ArrayList.class);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    crAlarmSettingDTOS.add(crAlarmSettingDTO);
    PowerMockito.when(crAlarmRepository.getAlarmSettingByModule(any()))
        .thenReturn(crAlarmSettingDTOS);
    List<CrAlarmSettingDTO> crAlarmSettingDTOS1 = crAlarmBusiness
        .getListAlarmSettingByModule(crAlarmSettingDTOS);
    Assert.assertEquals(crAlarmSettingDTOS.size(), crAlarmSettingDTOS1.size());
  }

  @Test
  public void exportData_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CrAlarmSettingDTO> lst = Mockito.spy(ArrayList.class);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    lst.add(crAlarmSettingDTO);
    PowerMockito.when(crAlarmRepository.getListAlarm(anyList())).thenReturn(lst);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("DANH SÁCH ALARM");
    List<Long> lstCasId = Mockito.spy(ArrayList.class);
    lstCasId.add(1L);
    File file = crAlarmBusiness.exportData(lstCasId);
    Assert.assertNotNull(file);
  }

  @Test
  public void getVendorList_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrAlarmSettingDTO crAlarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    crAlarmSettingDTO.setPage(1);
    crAlarmSettingDTO.setPageSize(1);
    crAlarmSettingDTO.setVendorName("vtnet");
    crAlarmSettingDTO.setVendorCode("123");
    List<CatVendorBO> lstVendor = Mockito.spy(ArrayList.class);
    CatVendorBO catVendorBO = Mockito.spy(CatVendorBO.class);
    lstVendor.add(catVendorBO);
    try {
      PowerMockito.when(wsnimsInfraPort.getVendorList(any(), any())).thenReturn(lstVendor);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    Datatable datatable = crAlarmBusiness.getVendorList(crAlarmSettingDTO);
    Assert.assertEquals(datatable.getPages(), 1L);
  }

  @Test
  public void nationMap_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Map<String, String> stringStringMap = crAlarmBusiness.nationMap();
    Assert.assertEquals(stringStringMap.size(), 0L);
  }

  @Test
  public void setupModuleData_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrModuleDraftDTO crModuleDraftDTO = Mockito.spy(CrModuleDraftDTO.class);
    crModuleDraftDTO.setPage(1);
    crModuleDraftDTO.setPageSize(1);
    crModuleDraftDTO.setSERVICE_CODE("FIX_BUG");
    crModuleDraftDTO.setMODULE_CODE("FIX_BUG");
    crModuleDraftDTO.setNationCode("VNM");
    List<CrModuleDraftDTO> resultList = Mockito.spy(ArrayList.class);
    resultList.add(crModuleDraftDTO);
    try {
      PowerMockito.when(wsiimPort
          .getIIMModules(any(), any(), any(), any(), any(),
              any())).thenReturn(resultList);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    Datatable datatable = crAlarmBusiness.setupModuleData(crModuleDraftDTO);
    Assert.assertEquals(datatable.getPages(), 1L);
  }

  @Test
  public void getListModuleByCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CrModuleDetailDTO> crModuleDetailDTOS = Mockito.spy(ArrayList.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crAlarmRepository.getListModuleByCr(any())).thenReturn(crModuleDetailDTOS);
    List<CrModuleDetailDTO> crModuleDetailDTOS1 = crAlarmBusiness.getListModuleByCr(crInsiteDTO);
    Assert.assertEquals(crModuleDetailDTOS.size(), crModuleDetailDTOS1.size());
  }

  @Test
  public void getListVendorByCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CrVendorDetailDTO> crVendorDetailDTOS = Mockito.spy(ArrayList.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crAlarmRepository.getListVendorByCr(any())).thenReturn(crVendorDetailDTOS);
    List<CrVendorDetailDTO> crVendorDetailDTOS1 = crAlarmBusiness.getListVendorByCr(crInsiteDTO);
    Assert.assertEquals(crVendorDetailDTOS.size(), crVendorDetailDTOS1.size());
  }

  @Test
  public void getAlarmSetting_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    CrAlarmSettingDTO alarmSettingDTO = Mockito.spy(CrAlarmSettingDTO.class);
    PowerMockito.when(crAlarmRepository.getAlarmSetting(any())).thenReturn(datatable);
    Datatable datatable1 = crAlarmBusiness.getAlarmSetting(alarmSettingDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getListAlarmByCr_OutSide_01() {
    List<CrAlarmDTO> lstExpect = Mockito.spy(ArrayList.class);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    PowerMockito.when(crAlarmRepository.getListObjectByCr(anyObject())).thenReturn(lstExpect);
    List<CrAlarmDTO> lstActual = crAlarmBusiness.getListAlarmByCr(crDTO);
    Assert.assertEquals(lstExpect.size(), lstActual.size());
  }

  @Test
  public void exportDataNew_01() {
    File file = crAlarmBusiness.exportDataNew(null);
    Assert.assertNull(file);
  }
}
