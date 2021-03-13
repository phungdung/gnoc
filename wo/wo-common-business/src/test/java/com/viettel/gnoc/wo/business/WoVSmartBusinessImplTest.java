package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.business.CompCauseBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CompCauseDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.JsonUtils;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.wo.dto.CdInfoForm;
import com.viettel.gnoc.wo.dto.CfgSupportCaseTestDTO;
import com.viettel.gnoc.wo.dto.CfgWoTickHelpDTO;
import com.viettel.gnoc.wo.dto.CfgWoTickHelpInsideDTO;
import com.viettel.gnoc.wo.dto.CountWoForVSmartForm;
import com.viettel.gnoc.wo.dto.KpiCompleteVsmartResult;
import com.viettel.gnoc.wo.dto.MaterialThresDTO;
import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import com.viettel.gnoc.wo.dto.ObjFile;
import com.viettel.gnoc.wo.dto.ObjKeyValue;
import com.viettel.gnoc.wo.dto.SupportCaseForm;
import com.viettel.gnoc.wo.dto.VsmartUpdateForm;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoChecklistDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseInsideDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionInsideDTO;
import com.viettel.gnoc.wo.dto.WoSalaryResponse;
import com.viettel.gnoc.wo.dto.WoTroubleInfoDTO;
import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeServiceDTO;
import com.viettel.gnoc.wo.dto.WoTypeServiceInsideDTO;
import com.viettel.gnoc.wo.dto.WoWorklogDTO;
import com.viettel.gnoc.wo.dto.WoWorklogInsideDTO;
import com.viettel.gnoc.wo.repository.CfgWoTickHelpRepository;
import com.viettel.gnoc.wo.repository.MaterialThresRepository;
import com.viettel.gnoc.wo.repository.WoChecklistDetailRepository;
import com.viettel.gnoc.wo.repository.WoDeclareServiceRepository;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoHistoryRepository;
import com.viettel.gnoc.wo.repository.WoMerchandiseRepository;
import com.viettel.gnoc.wo.repository.WoPostInspectionRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import com.viettel.gnoc.wo.repository.WoTroubleInfoRepository;
import com.viettel.gnoc.wo.repository.WoWorklogRepository;
import com.viettel.gnoc.wo.utils.NocProPort;
import com.viettel.gnoc.wo.utils.SPM_ANALYS_Port;
import com.viettel.gnoc.wo.utils.WSNIMS_CDPort;
import com.viettel.nims.webservice.SubscriptionInfoForm;
import com.viettel.nocproV4.JsonResponseBO;
import com.viettel.nocproV4.RequestInputBO;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({WoVSmartBusinessImpl.class, I18n.class, WSNIMS_CDPort.class, DataUtil.class,
    JsonUtils.class, Gson.class, GsonBuilder.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
@Slf4j
public class WoVSmartBusinessImplTest {

  @InjectMocks
  WoVSmartBusinessImpl woVSmartBusiness;
  @Mock
  WoRepository woRepository;
  @Mock
  WoDetailRepository woDetailRepository;
  @Mock
  WSNIMS_CDPort wsnims_cdPort;
  @Mock
  WoWorklogRepository woWorklogRepository;
  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;
  @Mock
  WoPostInspectionRepository woPostInspectionRepository;
  @Mock
  CommonRepository commonRepository;
  @Mock
  WoTroubleInfoRepository woTroubleInfoRepository;
  @Mock
  WoHistoryRepository woHistoryRepository;
  @Mock
  SPM_ANALYS_Port port;
  @Mock
  UnitRepository unitRepository;
  @Mock
  GnocFileRepository gnocFileRepository;
  @Mock
  CatItemRepository catItemRepository;
  @Mock
  WoBusiness woBusiness;
  @Mock
  NocProPort nocProPort;
  @Mock
  WoPostInspectionBusiness woPostInspectionBusiness;
  @Mock
  CompCauseBusiness compCauseBusiness;
  @Mock
  UserBusiness userBusiness;
  @Mock
  WoMerchandiseRepository woMerchandiseRepository;
  @Mock
  CfgWoTickHelpRepository cfgWoTickHelpRepository;
  @Mock
  WoCdGroupBusiness woCdGroupBusiness;
  @Mock
  MaterialThresRepository materialThresRepository;
  @Mock
  WoChecklistDetailRepository woChecklistDetailRepository;
  @Mock
  MessagesRepository messagesRepository;
  @Mock
  WoDeclareServiceRepository woDeclareServiceRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(woVSmartBusiness, "uploadFolder",
        "./wo_upload");
  }

  @Before
  public void setUpExtension() {
    ReflectionTestUtils.setField(woVSmartBusiness, "extension",
        ".doc,.docx,.pdf,.xls,.xlsx,.ppt,.pptx,.csv,.txt,.rar,.zip,.7z,.jpg,.gif,.png,.bmp,.sql");
  }

  @Test
  public void checkInfraForComplete_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    WoDTO createWoDto = Mockito.spy(WoDTO.class);
    createWoDto.setWoId("1111");
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    detail.setAccountIsdn("1111");
    com.viettel.nims.webservice.SubscriptionInfoForm res = Mockito.spy(SubscriptionInfoForm.class);
    res.setInfraType("CNN");
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(wsnims_cdPort.getSubscriptionInfo(any(), any())).thenReturn(res);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(resultInSideDto);
    ResultDTO resultDTO = woVSmartBusiness.checkInfraForComplete(createWoDto);
    Assert.assertEquals(resultDTO.getKey(), RESULT.FAIL);
  }

  @Test
  public void getListCdByLocation_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoCdGroupInsideDTO> woCdGroupInsideDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.getListCdByLocation(anyString()))
        .thenReturn(woCdGroupInsideDTOS);
    List<WoCdGroupInsideDTO> woCdGroupInsideDTOS1 = woVSmartBusiness
        .getListCdByLocation(anyString());
    Assert.assertEquals(woCdGroupInsideDTOS.size(), woCdGroupInsideDTOS1.size());
  }

  @Test
  public void getListWorklogByWoIdPaging_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(woWorklogRepository.getListWorklogByWoIdPaging(any())).thenReturn(datatable);
    List<WoWorklogInsideDTO> woWorklogInsideDTOS = woVSmartBusiness
        .getListWorklogByWoIdPaging(any());
    Assert.assertNull(woWorklogInsideDTOS);
  }

  @Test
  public void getListMaterial_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoMaterialDeducteInsideDTO> woMaterialDeducteInsideDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.getListMaterial(any())).thenReturn(woMaterialDeducteInsideDTOS);
    List<WoMaterialDeducteInsideDTO> woMaterialDeducteInsideDTOS1 = woVSmartBusiness
        .getListMaterial(any());
    Assert.assertEquals(woMaterialDeducteInsideDTOS.size(), woMaterialDeducteInsideDTOS1.size());
  }

  @Test
  public void getLstSupportCase_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    detail.setInfraType(1L);
    detail.setServiceId(1L);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<LinkedHashMap> lstHasMap = Mockito.spy(ArrayList.class);
    LinkedHashMap<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
    stringObjectLinkedHashMap.put("id", "1");
    stringObjectLinkedHashMap.put("cfgSupportCaseID", "1");
    stringObjectLinkedHashMap.put("caseName", "test");
    stringObjectLinkedHashMap.put("serviceID", "1");
    stringObjectLinkedHashMap.put("serviceName", "fixBug");
    stringObjectLinkedHashMap.put("infraTypeID", "1");
    stringObjectLinkedHashMap.put("infraTypeName", "fixBug");
    lstHasMap.add(stringObjectLinkedHashMap);
    datatable.setData(lstHasMap);
    PowerMockito.when(woCategoryServiceProxy.getListCfgSupportCaseDTONew(any()))
        .thenReturn(datatable);
    CfgSupportCaseTestDTO k = Mockito.spy(CfgSupportCaseTestDTO.class);
    k.setCfgSuppportCaseId(1L);
    k.setTestCaseName("fixBug");
    k.setFileRequired(1L);
    k.setId(1L);
    List<CfgSupportCaseTestDTO> lstSCT = Mockito.spy(ArrayList.class);
    lstSCT.add(k);
    PowerMockito.when(woCategoryServiceProxy.getListCfgSupportCaseTestId(anyLong()))
        .thenReturn(lstSCT);
    List<SupportCaseForm> supportCaseForms = woVSmartBusiness.getLstSupportCase(anyLong());
    Assert.assertEquals(supportCaseForms.size(), 1L);
  }

  @Test
  public void getListExistedWoPostInspection_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoPostInspectionInsideDTO> woPostInspectionInsideDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woPostInspectionRepository.getListExistedWoPostInspection(any()))
        .thenReturn(woPostInspectionInsideDTOS);
    List<WoPostInspectionInsideDTO> woPostInspectionInsideDTOS1 = woVSmartBusiness
        .getListExistedWoPostInspection(any());
    Assert.assertEquals(woPostInspectionInsideDTOS.size(), woPostInspectionInsideDTOS1.size());
  }

  @Test
  public void onSearch_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoPostInspectionDTO woPostInspectionDTO = Mockito.spy(WoPostInspectionDTO.class);
    List<WoPostInspectionInsideDTO> woPostInspectionInsideDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woPostInspectionRepository.onSearch(any(), anyInt(), anyInt()))
        .thenReturn(woPostInspectionInsideDTOS);
    List<WoPostInspectionInsideDTO> woPostInspectionInsideDTOS1 = woVSmartBusiness
        .onSearch(woPostInspectionDTO, 0, 1);
    Assert.assertEquals(woPostInspectionInsideDTOS.size(), woPostInspectionInsideDTOS1.size());
  }

  @Test
  public void onSearchCount_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoPostInspectionDTO woPostInspectionDTO = Mockito.spy(WoPostInspectionDTO.class);
    Integer integer = woVSmartBusiness.onSearchCount(woPostInspectionDTO);
    Assert.assertEquals(java.util.Optional.ofNullable(0), java.util.Optional.ofNullable(integer));
  }

  @Test
  public void getCountWoForVSmart_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CountWoForVSmartForm> countWoForVSmartForms = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.getCountWoForVSmart(any())).thenReturn(countWoForVSmartForms);
    List<CountWoForVSmartForm> countWoForVSmartForms1 = woVSmartBusiness.getCountWoForVSmart(any());
    Assert.assertEquals(countWoForVSmartForms.size(), countWoForVSmartForms1.size());
  }

  @Test
  public void getScriptId_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    VsmartUpdateForm vsmartUpdateForm = Mockito.spy(VsmartUpdateForm.class);
    vsmartUpdateForm.setId(RESULT.SUCCESS);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoSystemId("1");
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    WoInsideDTO o = Mockito.spy(WoInsideDTO.class);
    o.setWoTypeId(1L);
    o.setWoId(1L);
    o.setStatus(6L);
    List<WoInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(o);
    PowerMockito.when(woRepository.getListWoBySystemOtherCode(any(), any())).thenReturn(lst);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC.UCTT", "vtnet");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC.UCTT.THUE", "solit");
    WoTroubleInfoDTO info = Mockito.spy(WoTroubleInfoDTO.class);
    info.setScriptId(1L);
    info.setPolesDistance(1D);
    info.setScriptName("fixBug");
    List<WoTroubleInfoDTO> lstInfo = Mockito.spy(ArrayList.class);
    lstInfo.add(info);
    PowerMockito.when(woTroubleInfoRepository.getListWoTroubleInfoByWoId(anyLong()))
        .thenReturn(lstInfo);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    VsmartUpdateForm updateForm = woVSmartBusiness.getScriptId(anyLong());
    Assert.assertEquals(updateForm.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void checkUpdateSupportWO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoSystem("SPM");
    wo.setAllowSupport(2L);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    ResultDTO resultDTO1 = woVSmartBusiness.checkUpdateSupportWO(anyLong());
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void checkUpdateSupportWO_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Có lỗi xảy ra");
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.ERROR);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoSystem("SPM");
    wo.setAllowSupport(1L);
    wo.setFtId(999999L);
    wo.setCdId(1L);
    wo.setCreatePersonId(1L);
    wo.setFileName("input.docx");
    wo.setStatus(2L);
    wo.setWoCode("123");
    wo.setWoContent("vtnet");
    wo.setWoId(1L);
    ResultInSideDto resultUpdateWo = Mockito.spy(ResultInSideDto.class);
    resultUpdateWo.setKey(RESULT.SUCCESS);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultUpdateWo);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultUpdateWo);
    WoDetailDTO wd = Mockito.spy(WoDetailDTO.class);
    wd.setAccountIsdn("0123abc");
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUsername("thanhlv12");
    us.setUserId(999999L);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(wd);
    PowerMockito.when(commonRepository.getUserByUserId(anyLong())).thenReturn(us);
    com.viettel.spm.analys.webservice.ResultDTO res = Mockito
        .spy(com.viettel.spm.analys.webservice.ResultDTO.class);
    res.setKey(RESULT.SUCCESS);
    PowerMockito.when(port.doRequestAnalys(anyList())).thenReturn(res);
    ResultDTO resultDTO1 = woVSmartBusiness.checkUpdateSupportWO(1L);
    Assert.assertEquals(resultDTO1.getKey(), RESULT.FAIL);
  }

  @Test
  public void deleteListWoPostInspection_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoPostInspectionDTO> woPostInspectionListDTO = Mockito.spy(ArrayList.class);
    WoPostInspectionDTO item = Mockito.spy(WoPostInspectionDTO.class);
    item.setId("1");
    woPostInspectionListDTO.add(item);
    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setKey(RESULT.SUCCESS);
    PowerMockito.when(woPostInspectionRepository.delete(any())).thenReturn(res);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    ResultDTO resultDTO1 = woVSmartBusiness.deleteListWoPostInspection(woPostInspectionListDTO);
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void countWOByFT_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoSalaryResponse woSalaryResponse = Mockito.spy(WoSalaryResponse.class);
    PowerMockito.when(woRepository.countWOByFT(any())).thenReturn(woSalaryResponse);
    WoSalaryResponse woSalaryResponse1 = woVSmartBusiness.countWOByFT(any());
    Assert.assertEquals(woSalaryResponse, woSalaryResponse1);
  }

  @Test
  public void getListCdInfo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CdInfoForm cdInfoForm = Mockito.spy(CdInfoForm.class);
    List<String> lstWoCode = Mockito.spy(ArrayList.class);
    lstWoCode.add("vtnet");
    cdInfoForm.setLstWoCode(lstWoCode);
    List<Long> lstWoId = Mockito.spy(ArrayList.class);
    lstWoId.add(1L);
    cdInfoForm.setLstWoId(lstWoId);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoCode("0123");
    wo.setWoId(1L);
    wo.setStatus(8L);
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    WoHistoryInsideDTO woHis = Mockito.spy(WoHistoryInsideDTO.class);
    woHis.setUserName("thanhlv12");
    PowerMockito.when(woRepository.getWoHisFinalClose(anyLong())).thenReturn(woHis);
    UsersInsideDto u = Mockito.spy(UsersInsideDto.class);
    u.setEmail("vt@gmail.com");
    u.setUsername("thanhlv12");
    u.setFullname("Le Van Thanh");
    u.setMobile("0123456789");
    PowerMockito.when(commonRepository.getUserByUserName(anyString())).thenReturn(u);
    List<CdInfoForm> cdInfoForms = woVSmartBusiness.getListCdInfo(cdInfoForm);
    Assert.assertEquals(cdInfoForms.size(), 1L);
  }

  @Test
  public void getListCdInfo_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CdInfoForm cdInfoForm = Mockito.spy(CdInfoForm.class);
    List<Long> lstWoId = Mockito.spy(ArrayList.class);
    lstWoId.add(1L);
    cdInfoForm.setLstWoId(lstWoId);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoCode("0123");
    wo.setWoId(1L);
    wo.setStatus(8L);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    WoHistoryInsideDTO woHis = Mockito.spy(WoHistoryInsideDTO.class);
    woHis.setUserName("thanhlv12");
    PowerMockito.when(woRepository.getWoHisFinalClose(anyLong())).thenReturn(woHis);
    UsersInsideDto u = Mockito.spy(UsersInsideDto.class);
    u.setEmail("vt@gmail.com");
    u.setUsername("thanhlv12");
    u.setFullname("Le Van Thanh");
    u.setMobile("0123456789");
    PowerMockito.when(commonRepository.getUserByUserName(anyString())).thenReturn(u);
    List<CdInfoForm> cdInfoForms = woVSmartBusiness.getListCdInfo(cdInfoForm);
    Assert.assertEquals(cdInfoForms.size(), 1L);
  }

  @Test
  public void getSequenseWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = RESULT.SUCCESS;
    PowerMockito.when(woRepository.getSeqTableWo(anyString())).thenReturn(result);
    String res = woVSmartBusiness.getSequenseWo(anyString());
    Assert.assertEquals(result, res);
  }

  @Test
  public void getListSequenseWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> strings = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.getListSequenseWo(anyString(), anyInt())).thenReturn(strings);
    List<String> res = woVSmartBusiness.getListSequenseWo(anyString(), anyInt());
    Assert.assertEquals(strings.size(), res.size());
  }

  @Test
  public void updateWOPostInspection_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("WO hậu kiểm sự cố");
    WoPostInspectionDTO inspectionDTO = Mockito.spy(WoPostInspectionDTO.class);
    inspectionDTO.setId("1");
    inspectionDTO.setWoId("1");
    inspectionDTO.setAccount("thanhlv12");
    inspectionDTO.setNote("fixBug");
    inspectionDTO.setCreatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
    inspectionDTO.setUserName("thanhlv12");
    inspectionDTO.setResult("NOK");
    inspectionDTO.setReceiveUserName("thanhlv12");
    inspectionDTO.setWoTypeName(Constants.AP_PARAM.POINT_OK_HKSC);
    inspectionDTO.setPoint("1");
    List<String> arrFileName = Mockito.spy(ArrayList.class);
//    arrFileName.add("input.docx");
    inspectionDTO.setArrFileName(arrFileName);
    List<byte[]> fileDocumentByteArray = Mockito.spy(ArrayList.class);
//    byte[] varByte = "any string".getBytes();
//    fileDocumentByteArray.add(varByte);
    inspectionDTO.setFileDocumentByteArray(fileDocumentByteArray);
    inspectionDTO.setCreatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
    UsersInsideDto users = Mockito.spy(UsersInsideDto.class);
    users.setUserId(999999L);
    users.setUnitId(1L);
    users.setUsername("thanhlv12");
    PowerMockito.when(commonRepository.getUserByUserName(anyString())).thenReturn(users);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    Map<String, String> map = new HashMap<>();
    map.put(Constants.AP_PARAM.VTT_KHAC_PHUC_HAU_KIEM, Constants.AP_PARAM.WO_TYPE_NAME_HKSC);
    map.put(Constants.AP_PARAM.PRIORITY_HAU_KIEM, "2");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(map);
    WoPostInspectionInsideDTO wpidto = Mockito.spy(WoPostInspectionInsideDTO.class);
    PowerMockito.when(woPostInspectionRepository.findWoInspectionById(anyLong()))
        .thenReturn(wpidto);
    UsersInsideDto receiveUserName = Mockito.spy(UsersInsideDto.class);
    receiveUserName.setUserId(99999L);
    PowerMockito.when(commonRepository.getUserByUserName(anyString())).thenReturn(receiveUserName);
    ResultInSideDto key = Mockito.spy(ResultInSideDto.class);
    key.setKey(RESULT.SUCCESS);
    PowerMockito.when(woPostInspectionRepository.updateWOPostInspection(any())).thenReturn(key);
    List<WoTypeInsideDTO> lst = Mockito.spy(ArrayList.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    lst.add(woTypeInsideDTO);
    PowerMockito.when(woCategoryServiceProxy.getListWoTypeByLocaleNotLike(any())).thenReturn(lst);
    List<CatItemDTO> lstItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("999999");
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), anyString()))
        .thenReturn(lstItem);
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstCd);
    PowerMockito.when(woCategoryServiceProxy.getListWoCdGroupDTO(any())).thenReturn(datatable);
    List<CatItemDTO> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(catItemDTO);
    try {
      PowerMockito.when(woBusiness.createWoCommon(any())).thenReturn(resultInSideDto);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    ResultDTO resultDTO = woVSmartBusiness.updateWOPostInspection(inspectionDTO);
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void createWoVsmart_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoDTO createWoDto = Mockito.spy(WoDTO.class);
    createWoDto.setWoTypeCode(Constants.AP_PARAM.WO_TYPE_KBDV_QLCTKT);
    createWoDto.setUnitCode("413716");
    List<WoTypeInsideDTO> lst = Mockito.spy(ArrayList.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    lst.add(woTypeInsideDTO);
    PowerMockito.when(woCategoryServiceProxy.getListWoTypeByLocaleNotLike(any())).thenReturn(lst);
    List<CatItemDTO> lstItem = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("123");
    lstItem.add(catItemDTO);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), anyString()))
        .thenReturn(lstItem);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    lstCd.add(woCdGroupInsideDTO);
    datatable.setData(lstCd);
    PowerMockito.when(woCategoryServiceProxy.getListWoCdGroupDTO(any())).thenReturn(datatable);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      PowerMockito.when(woBusiness.insertWoCommon(createWoDto)).thenReturn(resultInSideDto);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    ResultInSideDto resultInSideDto1 = woVSmartBusiness.createWoVsmart(createWoDto);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void checkDeviceCodeOfWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoCode("123");
    woInsideDTO.setDeviceCode("123");
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(woInsideDTO);
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.SUCCESS);
    ResultDTO resultDTO = woVSmartBusiness.checkDeviceCodeOfWo(woInsideDTO);
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getCountWOByUsers_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Integer result = 1;
    PowerMockito.when(woRepository.getCountWOByUsers(any())).thenReturn(result);
    Integer integer = woVSmartBusiness.getCountWOByUsers(any());
    Assert.assertEquals(java.util.Optional.ofNullable(integer), java.util.Optional.ofNullable(1));
  }

  @Test
  public void updatePendingWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setKey(RESULT.SUCCESS);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoCode("123");
    woInsideDTO.setEndPendingTime(new Date());
    woInsideDTO.setUser("thanhlv12");
    woInsideDTO.setComment("fixBug");
    woInsideDTO.setSystem("gnoc");
    Boolean callCC = true;
    try {
      PowerMockito.when(
          woBusiness.updatePendingWoCommon(woInsideDTO.getWoCode(), woInsideDTO.getEndPendingTime(),
              woInsideDTO.getUser(), woInsideDTO.getComment(), woInsideDTO.getSystem(), callCC))
          .thenReturn(res);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    ResultDTO resultDTO = woVSmartBusiness.updatePendingWo(woInsideDTO, callCC);
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void aprovePXK_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(woBusiness.aprovePXK(any(), any(), any(), any()))
        .thenReturn(resultDTO);
    ResultDTO resultDTO1 = woVSmartBusiness.aprovePXK(anyLong(), anyLong(), anyString(), anyLong());
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void confirmNotCreateAlarm_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Công việc không tồn tại");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    JsonResponseBO resNoc = Mockito.spy(JsonResponseBO.class);
    resNoc.setStatus(0);
    PowerMockito.when(nocProPort.onExecuteMapQuery(any())).thenReturn(resNoc);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    RequestInputBO inputBO = Mockito.spy(RequestInputBO.class);
    Long woId = 1L;
    ResultDTO resultDTO = woVSmartBusiness.confirmNotCreateAlarm(inputBO, woId);
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getDataCfgWoHelp_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ObjKeyValueVsmartDTO> objKeyValueVsmartDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woCategoryServiceProxy.getDataHeader(anyLong(), anyString()))
        .thenReturn(objKeyValueVsmartDTOS);
    List<ObjKeyValueVsmartDTO> objKeyValueVsmartDTOS1 = woVSmartBusiness
        .getDataCfgWoHelp(anyLong(), anyString());
    Assert.assertEquals(objKeyValueVsmartDTOS.size(), objKeyValueVsmartDTOS1.size());
  }

  @Test
  public void acceptWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setKey(RESULT.SUCCESS);
    try {
      PowerMockito.when(woBusiness.acceptWoCommon(anyString(), anyLong(), anyString(), anyString()))
          .thenReturn(res);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    ResultDTO resultDTO = woVSmartBusiness.acceptWo("thanhlv12", 1L, "fixBug", true);
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void findWoById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    PowerMockito.when(woBusiness.findWoById(anyLong())).thenReturn(woDTO);
    WoDTO woDTO1 = woVSmartBusiness.findWoById(anyLong());
    Assert.assertNotNull(woDTO1);
  }

  @Test
  public void insertWOPostInspectionFromVsmart_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = RESULT.SUCCESS;
    PowerMockito
        .when(woPostInspectionBusiness.insertWOPostInspectionFromVsmart(anyList(), anyList()))
        .thenReturn(result);
    String res = woVSmartBusiness.insertWOPostInspectionFromVsmart(anyList(), anyList());
    Assert.assertEquals(result, res);
  }

  @Test
  public void cancelReqBccs_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(woBusiness.cancelReqBccs(anyString(), anyString())).thenReturn(resultDTO);
    ResultDTO resultDTO1 = woVSmartBusiness.cancelReqBccs(anyString(), anyString());
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void updateMopInfo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(woBusiness.updateMopInfo(anyString(), anyString(), anyString(), anyLong()))
        .thenReturn(resultDTO);
    ResultDTO resultDTO1 = woVSmartBusiness
        .updateMopInfo(anyString(), anyString(), anyString(), anyLong());
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getListWOPostInspection_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoPostInspectionInsideDTO> list = Mockito.spy(ArrayList.class);
    WoPostInspectionInsideDTO woPostInspectionInsideDTO = Mockito
        .spy(WoPostInspectionInsideDTO.class);
    list.add(woPostInspectionInsideDTO);
    PowerMockito.when(woPostInspectionBusiness
        .getListWOPostInspection(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(list);
    WoPostInspectionDTO inspectionDTO = Mockito.spy(WoPostInspectionDTO.class);
    List<WoPostInspectionDTO> woPostInspectionDTOS = woVSmartBusiness
        .getListWOPostInspection(inspectionDTO, 1, 1, "asc", "id");
    Assert.assertEquals(list.size(), woPostInspectionDTOS.size());
  }

  @Test
  public void dispatchWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      PowerMockito.when(woBusiness.dispatchWo(anyString(), anyString(), anyLong(), anyString()))
          .thenReturn(resultInSideDto);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    ResultDTO resultDTO = woVSmartBusiness.dispatchWo("thanhlv12", "thanhlv12", "413317", "fixBug");
    Assert.assertEquals(resultDTO.getKey(), "OK");
  }

  @Test
  public void getListCompCause3Level_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CompCauseDTO> list = Mockito.spy(ArrayList.class);
    CompCauseDTO compCauseDTO = Mockito.spy(CompCauseDTO.class);
    compCauseDTO.setName("vtnet");
    compCauseDTO.setCompCauseId("1");
    compCauseDTO.setParentId("1");
    compCauseDTO.setLevelId("1");
    list.add(compCauseDTO);
    PowerMockito.when(compCauseBusiness.getCompCause(any())).thenReturn(list);
    List<CompCause> compCauses = woVSmartBusiness.getListCompCause3Level("1");
    Assert.assertEquals(list.size(), compCauses.size());
  }

  @Test
  public void createListWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ResultDTO> resultDTOS = Mockito.spy(ArrayList.class);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    resultDTOS.add(resultDTO);
    PowerMockito.when(woBusiness.createListWoVsmart(anyList())).thenReturn(resultDTOS);
    List<ResultDTO> resultDTOS1 = woVSmartBusiness.createListWo(anyList());
    Assert.assertEquals(resultDTOS1.size(), 1L);
  }

  @Test
  public void createWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(woBusiness.createWoVsmart(any())).thenReturn(resultDTO);
    ResultDTO resultDTO1 = woVSmartBusiness.createWo(any());
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void approveWoVsmart_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      PowerMockito.when(woBusiness
          .approveWoCommon(any(), anyString(), anyLong(), anyString(), anyString(), anyString()))
          .thenReturn(resultInSideDto);
    } catch (Exception e) {
    }
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    try {
      resultDTO = woVSmartBusiness
          .approveWoVsmart(updateForm, "thanhlv12", "413314", "fixBug", "code", "thanhlv12");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);

  }

  @Test
  public void insertWoKTTS_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(woBusiness.insertWoKTTS(any())).thenReturn(resultDTO);
    ResultDTO resultDTO1 = woVSmartBusiness.insertWoKTTS(any());
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getListUserByUnitCode_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<Users> users = Mockito.spy(ArrayList.class);
    PowerMockito.when(userBusiness.getListUserByUnitCode(anyString(), anyString()))
        .thenReturn(users);
    List<Users> users1 = woVSmartBusiness.getListUserByUnitCode("vtnet", "true");
    Assert.assertEquals(users1.size(), 0L);
  }

  @Test
  public void getUserInfo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UsersDTO users = Mockito.spy(UsersDTO.class);
    PowerMockito.when(userBusiness.getUserInfo(anyString(), anyString())).thenReturn(users);
    UsersDTO usersDTO = woVSmartBusiness.getUserInfo("thanhlv12", "123");
    Assert.assertNotNull(usersDTO);
  }

  @Test
  public void getListWoMerchandise_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoMerchandiseInsideDTO dto = Mockito.spy(WoMerchandiseInsideDTO.class);
    List<WoMerchandiseInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(dto);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandise(anyLong())).thenReturn(list);
    List<WoMerchandiseDTO> woMerchandiseDTOS = woVSmartBusiness.getListWoMerchandise("1");
    Assert.assertEquals(woMerchandiseDTOS.size(), 1L);
  }

  @Test
  public void getListWorklogByWoId_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoWorklogInsideDTO dto = Mockito.spy(WoWorklogInsideDTO.class);
    List<WoWorklogInsideDTO> lists = Mockito.spy(ArrayList.class);
    lists.add(dto);
    PowerMockito.when(woWorklogRepository.getListDataByWoId(any())).thenReturn(lists);
    List<WoWorklogDTO> woWorklogDTOS = woVSmartBusiness.getListWorklogByWoId("1");
    Assert.assertEquals(woWorklogDTOS.size(), 1L);
  }

  @Test
  public void getKpiComplete_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add("thanhlv12");
    KpiCompleteVsmartResult kpiCompleteVsmartResult = Mockito.spy(KpiCompleteVsmartResult.class);
    PowerMockito.when(woBusiness.getKpiComplete(anyString(), anyString(), anyList()))
        .thenReturn(kpiCompleteVsmartResult);
    KpiCompleteVsmartResult kpiCompleteVsmartResult1 = woVSmartBusiness
        .getKpiComplete("01/01/2010 10:10:10", "01/01/2010 11:10:10", lstUser);
    Assert.assertNotNull(kpiCompleteVsmartResult1);
  }

  @Test
  public void getListWoChecklistDetailByWoId_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoChecklistDTO> woChecklistDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woBusiness.getListWoChecklistDetailByWoId(anyString()))
        .thenReturn(woChecklistDTOS);
    List<WoChecklistDTO> woChecklistDTOS1 = woVSmartBusiness
        .getListWoChecklistDetailByWoId(anyString());
    Assert.assertEquals(woChecklistDTOS.size(), woChecklistDTOS1.size());
  }

  @Test
  public void getFileFromWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ObjFile> objFiles = Mockito.spy(ArrayList.class);
    List<String> lstFileName = Mockito.spy(ArrayList.class);
    lstFileName.add("input.docx");
    PowerMockito.when(woBusiness.getFileFromWo(anyLong(), anyList())).thenReturn(objFiles);
    List<ObjFile> objFiles1 = woVSmartBusiness.getFileFromWo("413314", lstFileName);
    Assert.assertEquals(objFiles1.size(), 0L);
  }

  @Test
  public void insertCfgWoTickHelp_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CfgWoTickHelpDTO cfgWoTickHelpDTO = Mockito.spy(CfgWoTickHelpDTO.class);
    cfgWoTickHelpDTO.setWoId("123");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgWoTickHelpRepository.add(any())).thenReturn(resultInSideDto);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    PowerMockito.when(woBusiness.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    PowerMockito.when(woBusiness.updateTableWo(any())).thenReturn(resultInSideDto);
    ResultDTO resultDTO = woVSmartBusiness.insertCfgWoTickHelp(cfgWoTickHelpDTO);
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void insertWOPostInspection_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = RESULT.SUCCESS;
    PowerMockito.when(woPostInspectionBusiness.insertWOPostInspection(anyList()))
        .thenReturn(result);
    String res = woVSmartBusiness.insertWOPostInspection(anyList());
    Assert.assertEquals(result, res);
  }

  @Test
  public void loadWoPostInspectionChecklist_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ObjKeyValue> objKeyValues = Mockito.spy(ArrayList.class);
    PowerMockito
        .when(woPostInspectionBusiness.loadWoPostInspectionChecklist(anyString(), anyString()))
        .thenReturn(objKeyValues);
    List<ObjKeyValue> objKeyValues1 = woVSmartBusiness
        .loadWoPostInspectionChecklist(anyString(), anyString());
    Assert.assertEquals(objKeyValues.size(), objKeyValues1.size());
  }

  @Test
  public void getListFtByUser_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<UsersInsideDto> list = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    list.add(usersInsideDto);
    PowerMockito
        .when(woCdGroupBusiness.getListFtByUser(anyString(), anyString(), anyInt(), anyInt()))
        .thenReturn(list);
    List<UsersDTO> usersDTOS = woVSmartBusiness.getListFtByUser("999999", "abc", 0, 1);
    Assert.assertEquals(list.size(), usersDTOS.size());
  }

  @Test
  public void rejectWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      PowerMockito.when(woBusiness.rejectWo(anyString(), anyLong(), anyString(), anyString()))
          .thenReturn(resultInSideDto);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    ResultDTO resultDTO = woVSmartBusiness.rejectWo("thanhlv12", "413314", "fixBug", true);
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void deleteWoPost_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woPostInspectionBusiness.delete(anyLong())).thenReturn(resultInSideDto);
    String res = woVSmartBusiness.deleteWoPost(1L);
    Assert.assertEquals(resultInSideDto.getKey(), res);
  }

  @Test
  public void updateStatus_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<WoMaterialDeducteDTO> listMaterial = Mockito.spy(ArrayList.class);
    WoMaterialDeducteDTO woMaterialDeducteDTO = Mockito.spy(WoMaterialDeducteDTO.class);
    listMaterial.add(woMaterialDeducteDTO);
    List<WoMerchandiseDTO> lstMerchandise = Mockito.spy(ArrayList.class);
    WoMerchandiseDTO woMerchandiseDTO = Mockito.spy(WoMerchandiseDTO.class);
    lstMerchandise.add(woMerchandiseDTO);
    try {
      PowerMockito.when(woBusiness
          .updateStatusCommon(any(), anyString(), anyString(), anyString(), anyString(),
              anyString(),
              anyString(), anyList(), anyLong(), anyLong(), anyLong(), anyList(), anyString(),
              anyString(), anyString(), anyString(), anyList(), anyList()))
          .thenReturn(resultInSideDto);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    List<String> listFileName = Mockito.spy(ArrayList.class);
    listFileName.add("input.docx");
    List<byte[]> fileArr = Mockito.spy(ArrayList.class);
    byte[] varString = "any string".getBytes();
    fileArr.add(varString);
    ResultDTO resultDTO = woVSmartBusiness
        .updateStatus(updateForm, "thanhlv12", "413314", "4", "fixBug", "pass", "123", listMaterial,
            1L, 2L, 3L, lstMerchandise, "bug", "thanhlv12", "", "", listFileName, fileArr);
    Assert.assertEquals(resultDTO.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getWOSummaryInfobyUser_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ResultDTO> resultDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woBusiness
        .getWOSummaryInfobyUserCommon(anyString(), anyInt(), anyLong(), anyString(), anyString()))
        .thenReturn(resultDTOS);
    List<ResultDTO> resultDTOS1 = woVSmartBusiness
        .getWOSummaryInfobyUser("999999", 1, 1L, "01/01/2010 10:00:00", "01/01/2010 11:00:00");
    Assert.assertEquals(resultDTOS.size(), resultDTOS1.size());
  }

  @Test
  public void getListWOAndAccount_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoDTO> woDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository
        .getListWOAndAccount(anyString(), anyString(), anyString(), anyString(), anyString(),
            anyString())).thenReturn(woDTOS);
    List<WoDTO> woDTOS1 = woVSmartBusiness
        .getListWOAndAccount("thanhlv12", "01/01/2010 10:00:00", "01/01/2010 11:00:00", "123",
            "123", "123");
    Assert.assertEquals(woDTOS.size(), woDTOS1.size());
  }

  @Test
  public void insertWoWorklog_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    WoWorklogDTO woWorklogDTO = Mockito.spy(WoWorklogDTO.class);
    PowerMockito.when(woBusiness.insertWoWorklog((WoWorklogDTO) any())).thenReturn(resultDTO);
    ResultDTO resultDTO1 = woVSmartBusiness.insertWoWorklog(woWorklogDTO);
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void checkRequiredStation_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoTypeId(1L);
    wo.setStationCode("84");
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    List<WoTypeCfgRequiredDTO> lst = Mockito.spy(ArrayList.class);
    WoTypeCfgRequiredDTO o = Mockito.spy(WoTypeCfgRequiredDTO.class);
    o.setValue(1L);
    lst.add(o);
    PowerMockito.when(woCategoryServiceProxy.getListWoTypeCfgRequiredByWoTypeId(any()))
        .thenReturn(lst);
    PowerMockito.when(woRepository.getStationFollowNode(anyString(), anyString())).thenReturn("84");
    ResultDTO resultDTO1 = woVSmartBusiness.checkRequiredStation("123");
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void checkRequiredStation_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoTypeId(1L);
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    List<WoTypeCfgRequiredDTO> lst = Mockito.spy(ArrayList.class);
    WoTypeCfgRequiredDTO o = Mockito.spy(WoTypeCfgRequiredDTO.class);
    o.setValue(1L);
    lst.add(o);
    PowerMockito.when(woCategoryServiceProxy.getListWoTypeCfgRequiredByWoTypeId(any()))
        .thenReturn(lst);
    ResultDTO resultDTO1 = woVSmartBusiness.checkRequiredStation("123");
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void pendingWoForVsmart_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(woBusiness
        .pendingWoForVsmart(any(), anyString(), anyString(), anyString(), anyString(), anyString(),
            anyString(), anyString(), anyString())).thenReturn(resultDTO);
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    ResultDTO resultDTO1 = woVSmartBusiness
        .pendingWoForVsmart(updateForm, "123", "10", "thanhlv12", "gnoc", "fixBug", "1",
            "thanhlv12", "0123456789");
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }
//
//  @Test
//  public void getListMaterialDTOByAction_01() {
//    Logger logger = Mockito.spy(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//    List<MaterialThresInsideDTO> materialThresDTOS = Mockito.spy(ArrayList.class);
//    PowerMockito
//        .when(materialThresRepository.getListMaterialDTOByAction(any(), anyBoolean(), anyString()))
//        .thenReturn(materialThresDTOS);
//    List<MaterialThresDTO> materialThresDTOS1 = woVSmartBusiness
//        .getListMaterialDTOByAction(1L, 1L, 1L, true, "VNM");
//    Assert.assertEquals(materialThresDTOS.size(), materialThresDTOS1.size());
//  }

  @Test
  public void getIsCheckQrCode_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoTypeServiceInsideDTO woTypeServiceDTO = Mockito.spy(WoTypeServiceInsideDTO.class);
    PowerMockito.when(woBusiness.getIsCheckQrCode(anyLong())).thenReturn(woTypeServiceDTO);
    WoTypeServiceDTO woTypeServiceDTO1 = woVSmartBusiness.getIsCheckQrCode(1L);
    Assert.assertNotNull(woTypeServiceDTO1);
  }

  @Test
  public void getCountListFtByUser_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Integer integer = 1;
    PowerMockito.when(woBusiness.getCountListFtByUser(anyString(), anyString()))
        .thenReturn(integer);
    Integer integer1 = woVSmartBusiness.getCountListFtByUser("thanhlv12", "userName");
    Assert.assertEquals(integer1.intValue(), 1);

  }

  @Test
  public void getListReasonOverdue_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CompCause> compCauses = Mockito.spy(ArrayList.class);
    PowerMockito.when(woBusiness.getListReasonOverdue(anyLong(), anyString()))
        .thenReturn(compCauses);
    List<CompCause> compCauses1 = woVSmartBusiness.getListReasonOverdue(anyLong(), anyString());
    Assert.assertEquals(compCauses.size(), compCauses1.size());
  }

  @Test
  public void actionUpdateIsSupportWO_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(woBusiness
        .actionUpdateIsSupportWO(any(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(resultDTO);
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    ResultDTO resultDTO1 = woVSmartBusiness
        .actionUpdateIsSupportWO(updateForm, "123", "1", "thanhlv12", "fixBug");
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getListWOByUsers_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoDTOSearch> woDTOSearches = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository
        .getListWOByUsers(anyString(), anyString(), anyInt(), any(), anyInt(), anyInt(), anyInt()))
        .thenReturn(woDTOSearches);
    WoDTOSearch woDTO = Mockito.spy(WoDTOSearch.class);
    List<WoDTOSearch> woDTOSearches1 = woVSmartBusiness
        .getListWOByUsers("thanhlv12", "pass", 1, woDTO, 1, 1, 1);
    Assert.assertEquals(woDTOSearches.size(), woDTOSearches1.size());
  }

  @Test
  public void updateCfgWoTickHelpVsmart_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("finishWoTickHelp");
    CfgWoTickHelpDTO cfgWoTickHelpDTO = Mockito.spy(CfgWoTickHelpDTO.class);
    List<CfgWoTickHelpInsideDTO> lstCfgEntity = Mockito.spy(ArrayList.class);
    CfgWoTickHelpInsideDTO cfgDTO = Mockito.spy(CfgWoTickHelpInsideDTO.class);
    lstCfgEntity.add(cfgDTO);
    PowerMockito.when(cfgWoTickHelpRepository.searchEntity(any())).thenReturn(lstCfgEntity);
    PowerMockito.when(cfgWoTickHelpRepository.add(any())).thenReturn(resultInSideDto);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setFtId("999999");
    woDTO.setWoCode("123");
    PowerMockito.when(woBusiness.findWoById(anyLong())).thenReturn(woDTO);
    ResultInSideDto woResult = Mockito.spy(ResultInSideDto.class);
    woResult.setKey(RESULT.SUCCESS);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(woResult);
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUsername("thanhlv12");
    us.setMobile("0123456789");
    us.setFullname("Le Van Thanh");
    us.setUserId(999999L);
    PowerMockito.when(commonRepository.getUserByUserId(anyLong())).thenReturn(us);
    PowerMockito.when(messagesRepository.insertOrUpdateCommon(any())).thenReturn(woResult);
    ResultDTO resultDTO1 = woVSmartBusiness.updateCfgWoTickHelpVsmart(cfgWoTickHelpDTO);
    Assert.assertEquals(resultDTO1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getListCdGroup_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoCdGroupInsideDTO> list = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO dto = Mockito.spy(WoCdGroupInsideDTO.class);
    list.add(dto);
    PowerMockito.when(woCdGroupBusiness.getListCdGroup(anyString())).thenReturn(list);
    List<WoCdGroupDTO> woCdGroupDTOS = woVSmartBusiness.getListCdGroup("thanhlv12");
    Assert.assertEquals(list.size(), woCdGroupDTOS.size());
  }

  @Test
  public void insertListWoChecklistDetail_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String result = RESULT.SUCCESS;
    PowerMockito.when(woChecklistDetailRepository.insertListWoChecklistDetail(anyList()))
        .thenReturn(result);
    String res = woVSmartBusiness.insertListWoChecklistDetail(anyList());
    Assert.assertEquals(result, res);
  }

  @Test
  public void getListDataByWoIdPaging_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoWorklogInsideDTO> woWorklogInsideDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woWorklogRepository
        .getListDataByWoIdPaging(anyString(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(woWorklogInsideDTOS);
    List<WoWorklogInsideDTO> woWorklogInsideDTOS1 = woVSmartBusiness
        .getListDataByWoIdPaging(anyString(), anyInt(), anyInt(), anyString(), anyString());
    Assert.assertEquals(woWorklogInsideDTOS.size(), woWorklogInsideDTOS1.size());
  }


}
