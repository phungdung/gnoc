package com.viettel.gnoc.wo.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.cc.webserivce.CompCause;
import com.viettel.eee2ws.webservice.Result;
//import com.viettel.gnoc.common.service.WoTestServiceMapService;
import com.viettel.gnoc.commons.business.CatServiceBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.CatServiceRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.SmsGatewayRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.AP_PARAM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.WO_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.WO_RESULT;
import com.viettel.gnoc.commons.utils.Constants.WS_RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import com.viettel.gnoc.wo.dto.CatLocationDTO;
import com.viettel.gnoc.wo.dto.ExcelSheetDTO;
import com.viettel.gnoc.wo.dto.KpiCompleteVsamrtForm;
import com.viettel.gnoc.wo.dto.KpiCompleteVsmartResult;
import com.viettel.gnoc.wo.dto.ObjKeyValue;
import com.viettel.gnoc.wo.dto.ObjResponse;
import com.viettel.gnoc.wo.dto.SearchWoKpiCDBRForm;
import com.viettel.gnoc.wo.dto.VsmartUpdateForm;
import com.viettel.gnoc.wo.dto.WoAutoCheckDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoKTTSInfoDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseInsideDTO;
import com.viettel.gnoc.wo.dto.WoPendingDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import com.viettel.gnoc.wo.dto.WoTroubleInfoDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import com.viettel.gnoc.wo.dto.WoWorklogInsideDTO;
import com.viettel.gnoc.wo.model.WoMopInfoEntity;
import com.viettel.gnoc.wo.repository.LogCallIpccRepository;
import com.viettel.gnoc.wo.repository.WoAutoCheckRepository;
import com.viettel.gnoc.wo.repository.WoCdGroupRepository;
import com.viettel.gnoc.wo.repository.WoCdTempRepository;
import com.viettel.gnoc.wo.repository.WoDeclareServiceRepository;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoHistoryRepository;
import com.viettel.gnoc.wo.repository.WoKTTSInfoRepository;
import com.viettel.gnoc.wo.repository.WoMerchandiseRepository;
import com.viettel.gnoc.wo.repository.WoMopInfoRepository;
import com.viettel.gnoc.wo.repository.WoPendingRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import com.viettel.gnoc.wo.repository.WoSupportRepository;
import com.viettel.gnoc.wo.repository.WoTroubleInfoRepository;
import com.viettel.gnoc.wo.repository.WoTypeGroupRepository;
import com.viettel.gnoc.wo.repository.WoTypeServiceRepository;
import com.viettel.gnoc.wo.repository.WoWorklogRepository;
import com.viettel.gnoc.wo.utils.BCCS_CC_STLPort;
import com.viettel.gnoc.wo.utils.IPCCMVTPort;
import com.viettel.gnoc.wo.utils.IPCCPort;
import com.viettel.gnoc.wo.utils.IPCCSTLPort;
import com.viettel.gnoc.wo.utils.IPCCVTPPort;
import com.viettel.gnoc.wo.utils.IPCCVTZPort;
import com.viettel.gnoc.wo.utils.KTTSVsmartPort;
import com.viettel.gnoc.wo.utils.NimsStationForm;
import com.viettel.gnoc.wo.utils.NocProPort;
import com.viettel.gnoc.wo.utils.PaymentPort;
import com.viettel.gnoc.wo.utils.WSCC2Port;
import com.viettel.gnoc.wo.utils.WSNIMS_CD_BCCS2_Port;
import com.viettel.gnoc.wo.utils.WSNIMS_HTPort;
import com.viettel.gnoc.wo.utils.WSQLCTKT_CDPort;
import com.viettel.gnoc.wo.utils.WS_AMIONE_Port;
import com.viettel.gnoc.wo.utils.WS_CC_Direction;
import com.viettel.gnoc.wo.utils.WS_CHI_PHI_Port;
import com.viettel.gnoc.wo.utils.WS_CO_DIEN_Port;
import com.viettel.gnoc.wo.utils.WS_HOAN_CONG_Port;
import com.viettel.gnoc.wo.utils.WS_IMES_Port;
import com.viettel.gnoc.wo.utils.WS_NIMS_CD_Direction;
import com.viettel.gnoc.wo.utils.WS_QLCTKT_BCCS_Port;
import com.viettel.gnoc.wo.utils.WS_SAP_Port;
import com.viettel.ipcc.services.NomalOutput;
import com.viettel.nims.webservice.cd.bccs2.SubscriptionInfoForm;
import com.viettel.nims.webservice.ht.CheckPortSubDescriptionByWOForm;
import com.viettel.nims.webservice.ht.ResultCheckStatusCabinet;
import com.viettel.nims.webservice.ht.ResultCheckStatusStations;
import com.viettel.nims.webservice.ht.ResultGetDepartmentByLocationForm;
import com.viettel.nocproV4.JsonResponseBO;
import com.viettel.payment.services.StaffBean;
import com.viettel.qlctkt_cd.webservice.MessageForm;
import com.viettel.qlctkt_cd.webservice.Staff;
import com.viettel.qlctkt_cd.webservice.SysUsersBO;
import com.viettel.qldtktts.service2.CatStationBO;
import com.viettel.qldtktts.service2.CatWarehouseBO;
import com.viettel.qldtktts.service2.CntContractBO;
import com.viettel.qldtktts.service2.ConstrConstructionsBO;
import com.viettel.qldtktts.service2.Kttsbo;
import com.viettel.security.PassTranformer;
import com.viettel.soc.spm.service.ServiceProblemInfoDTO;
import com.viettel.webservice.cc_stl.Param;
import com.viettel.webservice.qlctkt.bccs.ResultService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;
@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileInputStream.class, WoBusinessImpl.class, JAXBContext.class, FileUtils.class,
    CommonImport.class, I18n.class,
    NocProPort.class, ExcelWriterUtils.class, BaseRepository.class, NocProPort.class,
    TicketProvider.class, DataUtil.class, WS_SAP_Port.class, CommonExport.class,
    DateTimeUtils.class, JAXBContext.class, WS_CHI_PHI_Port.class, PassTranformer.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class WoBusinessImplTest {

  @InjectMocks
  WoBusinessImpl woBusiness;

  @Mock
  WoRepository woRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  WoHistoryRepository woHistoryRepository;

  @Mock
  WoDetailRepository woDetailRepository;

  @Mock
  WoMerchandiseRepository woMerchandiseRepository;

  @Mock
  WoKTTSInfoRepository woKTTSInfoRepository;

  @Mock
  WoWorklogRepository woWorklogRepository;

//  @Mock
//  WoTestServiceMapService woTestServiceMapService;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  KTTSVsmartPort kttsVsmartPort;

  @Mock
  UnitRepository unitRepository;

  @Mock
  CommonRepository commonRepository;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  TtServiceProxy ttServiceProxy;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  MessagesRepository messagesRepository;

  @Mock
  WoMaterialDeducteBusiness woMaterialDeducteBusiness;

  @Mock
  WoPendingRepository woPendingRepository;

  @Mock
  WoAutoCheckRepository woAutoCheckRepository;

  @Mock
  NocProPort nocPort;

  @Mock
  WS_CHI_PHI_Port portChiPhi;

  @Mock
  WoVSmartBusiness vsmartBusiness;

  @Mock
  WSNIMS_CD_BCCS2_Port wsNims;

  @Mock
  WS_NIMS_CD_Direction ws_nims_cd_direction;

  @Mock
  WSQLCTKT_CDPort cdPort;

  @Mock
  WS_QLCTKT_BCCS_Port qltPort;

  @Mock
  WSNIMS_HTPort wsHTNims;

  @Mock
  WoCdTempRepository woCdTempRepository;

  @Mock
  CatServiceRepository catServiceRepository;

  @Mock
  WoDeclareServiceRepository woDeclareServiceRepository;

  @Mock
  WoTroubleInfoRepository woTroubleInfoRepository;

  @Mock
  PaymentPort paymentPort;

  @Mock
  BCCS_CC_STLPort ccStlPort;

  @Mock
  WS_CO_DIEN_Port coDienPort;

  @Mock
  WS_HOAN_CONG_Port hoanCongPort;

  @Mock
  UserBusiness userBusiness;

  @Mock
  CatServiceBusiness catServiceBusiness;

  @Mock
  SmsGatewayRepository smsGatewayRepository;

  @Mock
  LogCallIpccRepository logCallIpccRepository;

  @Mock
  IPCCMVTPort ipccmvtPort;

  @Mock
  IPCCVTZPort ipccvtzPort;

  @Mock
  IPCCSTLPort ipccstlPort;

  @Mock
  IPCCVTPPort ipccvtpPort;

  @Mock
  IPCCPort ipccPort;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  WoSPMBusiness woSPMBusiness;

  @Mock
  WoTicketBusiness woTicketBusiness;

  @Mock
  WSCC2Port wscc2Port;

  @Mock
  LanguageExchangeRepository languageExchangeRepository;

  @Mock
  WoMopInfoRepository woMopInfoRepository;

  @Mock
  WoTypeServiceRepository woTypeServiceRepository;

  @Mock
  MaterialCodienBusiness materialCodienBusiness;

  @Mock
  WoCdGroupRepository woCdGroupRepository;

  @Mock
  WoSupportRepository woSupportRepository;

  @Mock
  WoCdGroupBusiness woCdGroupBusiness;

  @Mock
  WoTypeBusiness woTypeBusiness;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  WoTypeGroupRepository woTypeGroupRepository;

  @Mock
  NocProPort nocProPort;

  @Mock
  WS_SAP_Port ws_sap_port;

  @Mock
  WoCdBusiness woCdBusiness;

  @Mock
  WS_AMIONE_Port ws_amione_port;

  @Mock
  WS_IMES_Port ws_imes_port;

  @Mock
  WS_CC_Direction ws_cc_direction;

  @Mock
  MrCategoryProxy mrCategoryProxy;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;
  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(woBusiness, "uploadFolder",
        "./wo-upload");
    ReflectionTestUtils.setField(woBusiness, "extension",
        "a,a");
    ReflectionTestUtils.setField(woBusiness, "ftpFolder",
        "./wo-upload");
    ReflectionTestUtils.setField(woBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(woBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(woBusiness, "tempFolder",
        "/tempFolder");
  }

  @Test
  public void getListDataSearchWeb_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setStartTime(new Date());
    woInsideDTO.setStartTimeFrom(new Date());
    woInsideDTO.setStartTimeTo(new Date());
    woInsideDTO.setEndTime(new Date());
    woInsideDTO.setEndTimeFrom(new Date());
    woInsideDTO.setEndTimeTo(new Date());
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1D);
    List<WoInsideDTO> listWoInsideDTO = Mockito.spy(ArrayList.class);
    WoInsideDTO dto = Mockito.spy(WoInsideDTO.class);
    dto.setCdId(1L);
    listWoInsideDTO.add(dto);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listWoInsideDTO);
    PowerMockito.when(woRepository.getListDataSearchWeb(any())).thenReturn(datatable);
    List<UsersInsideDto> listCd = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listCd);
    Datatable datatable1 = woBusiness.getListDataSearchWeb(woInsideDTO);
    Assert.assertEquals(datatable.getData().size(), datatable1.getData().size());
  }

  @Test
  public void findWoByIdFromWeb_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1D);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setCdId(1L);
    PowerMockito.when(woRepository.findWoById(anyLong(), anyDouble())).thenReturn(woInsideDTO);
    List<UsersInsideDto> listCd = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listCd);
    WoInsideDTO woInsideDTO1 = woBusiness.findWoByIdFromWeb(1L);
    Assert.assertEquals(woInsideDTO.getCdId(), woInsideDTO1.getCdId());
  }

  @Test
  public void validateImport_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Object merchandiseCode = "1";
    Object quantity = "1";
    String result = woBusiness.validateImport(merchandiseCode, quantity);
    Assert.assertEquals(result, "");
  }

  @Test
  public void deleteListWo_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Chỉ được xóa các WO chưa được nhân viên điều phối tiếp nhận.");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setUserID(999999L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offsetFromUser = 1D;
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(offsetFromUser);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put(Constants.CONFIG_PROPERTY.WO_TYPE_NOT_ALLOW_DELETE, "2");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    List<WoInsideDTO> listWoInsideDTO = Mockito.spy(ArrayList.class);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setCreatePersonId(999999L);
    woInsideDTO.setWoTypeId(1L);
    woInsideDTO.setStatus(0L);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setParentId(1L);
    woInsideDTO.setWoCode("123");
    woInsideDTO.setCdId(1L);
    woInsideDTO.setFtId(1L);
    woInsideDTO.setWoContent("fixBug");
    woInsideDTO.setFileName("input.txt");
    listWoInsideDTO.add(woInsideDTO);
    List<WoHistoryInsideDTO> listHistoryReject = Mockito.spy(ArrayList.class);
    PowerMockito.when(woHistoryRepository.getListWoHistoryDTO(any())).thenReturn(listHistoryReject);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    List<WoInsideDTO> listWoParent = Mockito.spy(ArrayList.class);
    listWoParent.add(woInsideDTO);
    PowerMockito.when(woRepository.getListDataSearchWoDTO(any())).thenReturn(listWoParent);
    List<WoInsideDTO> listChildWo = Mockito.spy(ArrayList.class);
//    WoInsideDTO woInsideDTO1 = Mockito.spy(WoInsideDTO.class);
//    woInsideDTO1.setStatus(4L);
//    listChildWo.add(woInsideDTO1);
    PowerMockito.when(woRepository.getListDataSearchWoDTO(any())).thenReturn(listChildWo);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woBusiness.deleteListWo(listWoInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void getListWoSystemInsertWeb_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.getListWoSystemInsertWeb()).thenReturn(list);
    List<CatItemDTO> list1 = woBusiness.getListWoSystemInsertWeb();
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void getListFileFromWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    GnocFileDto fileDto = Mockito.spy(GnocFileDto.class);
    fileDto.setPath(
        "./wo-upload");
    gnocFileDtos.add(fileDto);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtos);
    List<GnocFileDto> gnocFileDtos1 = woBusiness.getListFileFromWo(1L);
    Assert.assertEquals(gnocFileDtos.size(), gnocFileDtos1.size());
  }

  @Test
  public void getStationListNation_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(413314L);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setLocationId(84L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setNationCode("VNM");
    PowerMockito.when(woRepository.getCatLocationById(anyLong())).thenReturn(catLocationDTO);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Kttsbo kttsbo = Mockito.spy(Kttsbo.class);
    PowerMockito.when(kttsVsmartPort.getStationListNation(anyString(), anyString(), anyString()))
        .thenReturn(kttsbo);
    List<CatStationBO> catStationBOS = woBusiness
        .getStationListNation("VNM", "01/01/2010 10:00:00");
    Assert.assertNotNull(catStationBOS);
  }

  @Test
  public void getListWarehouseNation_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(413314L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setLocationId(84L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setNationCode("VNM");
    PowerMockito.when(woRepository.getCatLocationById(anyLong())).thenReturn(catLocationDTO);
    Kttsbo kttsbo = Mockito.spy(Kttsbo.class);
    PowerMockito.when(kttsVsmartPort
        .getListWarehouseNation(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(kttsbo);
    List<CatWarehouseBO> catWarehouseBOS = woBusiness
        .getListWarehouseNation("vnm", "vtnet", "bug", "1111");
    Assert.assertNotNull(catWarehouseBOS);
  }

  @Test
  public void getListContractFromConstrNation_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(413314L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setLocationId(84L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setNationCode("VNM");
    PowerMockito.when(woRepository.getCatLocationById(anyLong())).thenReturn(catLocationDTO);
    Kttsbo kttsbo = Mockito.spy(Kttsbo.class);
    PowerMockito.when(kttsVsmartPort.getListContractFromConstrNation(anyString(), anyString()))
        .thenReturn(kttsbo);
    List<CntContractBO> cntContractBOS = woBusiness.getListContractFromConstrNation("vtnet");
    Assert.assertNotNull(cntContractBOS);
  }

  @Test
  public void getConstructionListNation_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(413314L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setLocationId(84L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setNationCode("VNM");
    PowerMockito.when(woRepository.getCatLocationById(anyLong())).thenReturn(catLocationDTO);
    Kttsbo kttsbo = Mockito.spy(Kttsbo.class);
    PowerMockito.when(kttsVsmartPort.getConstructionListNation(anyString(), anyString()))
        .thenReturn(kttsbo);
    List<ConstrConstructionsBO> constrConstructionsBOS = woBusiness
        .getConstructionListNation("VNM");
    Assert.assertNotNull(constrConstructionsBOS);
  }

  @Test
  public void getListWoKttsAction_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.getListWoKttsAction(anyString())).thenReturn(list);
    List<CatItemDTO> list1 = woBusiness.getListWoKttsAction("bug");
    Assert.assertEquals(list.size(), list1.size());
  }

  @Test
  public void exportDataWo_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("WoList");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1D);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setStartTime(new Date());
    woInsideDTO.setStartTimeFrom(new Date());
    woInsideDTO.setStartTimeTo(new Date());
    woInsideDTO.setEndTime(new Date());
    woInsideDTO.setEndTimeFrom(new Date());
    woInsideDTO.setEndTimeTo(new Date());
    List<WoInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(woInsideDTO);
    PowerMockito.when(woRepository.getListWoExport(any())).thenReturn(list);
    File file = woBusiness.exportDataWo(woInsideDTO);
    Assert.assertNotNull(file);
  }

  @Test
  public void getTemplateImportWo_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("STT");
    List<CatItemDTO> listWoSystem = Mockito.spy(ArrayList.class);
    CatItemDTO dto = Mockito.spy(CatItemDTO.class);
    dto.setItemName("ID");
    listWoSystem.add(dto);
    PowerMockito.when(woRepository.getListWoSystemInsertWeb()).thenReturn(listWoSystem);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.LIST.PRIORITY", "1");
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupName("1");
    woCdGroupInsideDTO.setWoGroupCode("1");
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);
    PowerMockito.when(woCdGroupBusiness
        .getListWoCdGroupActive(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstCd);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    File file = woBusiness.getTemplateImportWo();
    Assert.assertNotNull(file);
  }

  @Test
  public void getListWoHistoryByWoId_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1D);
    PowerMockito.when(woHistoryRepository.getListWoHistoryByWoId(any())).thenReturn(datatable);
    WoHistoryInsideDTO woHistoryInsideDTO = Mockito.spy(WoHistoryInsideDTO.class);
    Datatable datatable1 = woBusiness.getListWoHistoryByWoId(woHistoryInsideDTO);
    Assert.assertEquals(datatable.getData(), datatable1.getData());
  }

  @Test
  public void getListWorklogByWoIdPaging_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1D);
    PowerMockito.when(woWorklogRepository.getListWorklogByWoIdPaging(any())).thenReturn(datatable);
    WoWorklogInsideDTO woWorklogInsideDTO = Mockito.spy(WoWorklogInsideDTO.class);
    Datatable datatable1 = woBusiness.getListWorklogByWoIdPaging(woWorklogInsideDTO);
    Assert.assertEquals(datatable.getData(), datatable1.getData());
  }

  @Test
  public void insertWoWorklog_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(woWorklogRepository.insertWoWorklog(any())).thenReturn(resultInSideDto);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    ResultInSideDto resultInSideDto1 = woBusiness.insertWoWorklog(woInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void loadTroubleCrDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setPage(1);
    woInsideDTO.setPageSize(1);
    List<CrDTO> dtoCRResult = Mockito.spy(ArrayList.class);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    dtoCRResult.add(crDTO);
    PowerMockito.when(crServiceProxy.getListCRFromOtherSystem(any()))
        .thenReturn(dtoCRResult);

    Datatable actual = woBusiness.loadTroubleCrDTO(woInsideDTO);
    Assert.assertNotNull(actual);
  }

  @Test
  public void updateFileAttack_01() throws Exception {
    WoBusinessImpl classUnderTest = PowerMockito.spy(woBusiness);
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
        "text/plain", "Spring Framework".getBytes());
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(FileUtils.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(413314L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setFileName("input.txt");
    List<String> fileNamesDelete = Mockito.spy(ArrayList.class);
    fileNamesDelete.add("delete.txt");
    woInsideDTO.setFileNamesDelete(fileNamesDelete);
    woInsideDTO.setCreateDate(new Date());
    woInsideDTO.setWoId(1L);
    List<GnocFileDto> gnocFileDtosAll = Mockito.spy(ArrayList.class);
    woInsideDTO.setGnocFileDtos(gnocFileDtosAll);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(gnocFileRepository.saveListGnocFile(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    fileAttacks.add(multipartFile);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(woInsideDTO);
    ResultInSideDto resultInSideDto1 = woBusiness.updateFileAttack(fileAttacks, woInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void getTemplateExportWOTestServiceFromCR_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("STT");
    File file = woBusiness.getTemplateExportWOTestServiceFromCR();
    Assert.assertNotNull(file);
  }

  @Test
  public void getTemplateExportWOTestServiceFromWO_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("STT");
    File file = woBusiness.getTemplateExportWOTestServiceFromWO();
    Assert.assertNotNull(file);
  }

  @Test
  public void acceptWoFromWeb_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Username không tồn tại");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setComment("fixBug");
    woInsideDTO.setRole("FT");
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    UsersInsideDto usersFtDto = Mockito.spy(UsersInsideDto.class);
    usersFtDto.setUnitId(413314L);
    usersFtDto.setUserId(999999L);
    usersFtDto.setUsername("thanhlv12");
    usersFtDto.setStaffCode("123");
    usersFtDto.setMobile("0123456789");
    usersFtDto.setFullname("Le Van Thanh");
    listUser.add(usersFtDto);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "1");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_NOT_CALL_MR, "2");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC, "3");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_THUE, "4");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT, "5");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT_THUE, "6");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_HC, "7");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI, "8");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_DC_HC, "9");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_DC_NC, "10");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_BAO_MAT, "11");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(999999L);
    wo.setStatus(3L);
    wo.setParentId(1L);
    wo.setFtAcceptedTime(new Date());
    wo.setWoSystem("SPM");
    wo.setWoCode("123");
    wo.setWoTypeId(9L);
    wo.setWoId(1L);
    wo.setStationCode("1");
    wo.setWoSystemId("10");
    wo.setCdId(1L);
    wo.setCreatePersonId(999999L);
    wo.setFileName("input.txt");
    wo.setWoContent("fixBug");
    wo.setNumPending(1L);
    wo.setFinishTime(new Date());
    wo.setReasonOverdueLV1Id("1");
    wo.setReasonOverdueLV1Name("fixBug1");
    wo.setReasonOverdueLV2Id("2");
    wo.setReasonOverdueLV2Name("fixBug2");
    wo.setSolutionDetail("fixBug");
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    WoInsideDTO parent = Mockito.spy(WoInsideDTO.class);
    parent.setStatus(4L);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(parent);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(413314L);
    unit.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unit);
    CompCause lv3 = Mockito.spy(CompCause.class);
    PowerMockito.when(woRepository.getCompCause(anyLong())).thenReturn(lv3);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(ttServiceProxy.onUpdateTroubleMobile(any())).thenReturn(resultDTO);
    Double offsetFromUser = 1D;
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(offsetFromUser);
    List<WoInsideDTO> listAllChildWo = Mockito.spy(ArrayList.class);
    listAllChildWo.add(wo);
    PowerMockito.when(woRepository.getListDataSearch1(any())).thenReturn(listAllChildWo);
    List<WoInsideDTO> lstWo = Mockito.spy(ArrayList.class);
    WoInsideDTO woUp = Mockito.spy(WoInsideDTO.class);
    woUp.setWoTypeId(10L);
    lstWo.add(woUp);
    PowerMockito.when(woRepository.getListWoBySystemOtherCode(anyString(), any()))
        .thenReturn(lstWo);
    List<WoMerchandiseInsideDTO> lstMer = Mockito.spy(ArrayList.class);
    WoMerchandiseInsideDTO o = Mockito.spy(WoMerchandiseInsideDTO.class);
    o.setMerchandiseCode("123");
    o.setQuantity(1D);
    lstMer.add(o);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any())).thenReturn(lstMer);
    Kttsbo kttsResult = Mockito.spy(Kttsbo.class);
    kttsResult.setStatus(RESULT.SUCCESS);
    PowerMockito.when(
        kttsVsmartPort.createAssetMoveCmdNation(any(), any(), anyList(), anyString(), anyString()))
        .thenReturn(kttsResult);
    PowerMockito.when(woRepository.getListWoChildByParentId(any())).thenReturn(listAllChildWo);
    ResultInSideDto resultInSideDto = woBusiness.acceptWoFromWeb(woInsideDTO);
    Assert.assertNotNull(resultInSideDto);
  }

  @Test
  public void acceptWoCommon_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Username không tồn tại");

    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    UsersInsideDto usersFtDto = Mockito.spy(UsersInsideDto.class);
    usersFtDto.setUnitId(413314L);
    usersFtDto.setUserId(999999L);
    usersFtDto.setUsername("thanhlv12");
    usersFtDto.setStaffCode("123");
    usersFtDto.setMobile("0123456789");
    usersFtDto.setFullname("Le Van Thanh");
    listUser.add(usersFtDto);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);

    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "1");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_NOT_CALL_MR, "2");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC, "3");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_THUE, "4");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT, "5");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT_THUE, "6");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_HC, "7");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI, "8");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_DC_HC, "9");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_DC_NC, "10");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_BAO_MAT, "11");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCC, "12");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);

    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(999999L);
    wo.setStatus(3L);
    wo.setParentId(1L);
    wo.setFtAcceptedTime(new Date());
    wo.setWoSystem("MR");
    wo.setWoCode("123");
    wo.setWoTypeId(1L);
    wo.setWoId(1L);
    wo.setStationCode("1");
    wo.setWoSystemId("10");
    wo.setCdId(1L);
    wo.setCreatePersonId(999999L);
    wo.setFileName("input.txt");
    wo.setWoContent("fixBug");
    wo.setNumPending(1L);
    wo.setFinishTime(new Date());
    wo.setReasonOverdueLV1Id("1");
    wo.setReasonOverdueLV1Name("fixBug1");
    wo.setReasonOverdueLV2Id("2");
    wo.setReasonOverdueLV2Name("fixBug2");
    wo.setSolutionDetail("fixBug");
    wo.setEstimateTime(new Date());
    wo.setCellService("ID");
    wo.setLongitude("1");
    wo.setLatitude("2");
    wo.setConcaveAreaCode("1");
    wo.setSolutionGroupName("fixBug");
    wo.setFinishTime(new Date());
    wo.setConstructionCode("123");
    wo.setWarehouseCode("123");
    wo.setCdAssignId(1L);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);

    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);

    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(413314L);
    unit.setUnitName("vtnet");
    unit.setLocationId(1L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unit);

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setNationCode("XXX");
    PowerMockito.when(
        woRepository.getCatLocationById(anyLong())
    ).thenReturn(catLocationDTO);

    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.SUCCESS);
    PowerMockito.when(ttServiceProxy.onUpdateTroubleFromWo(any())).thenReturn(res);
    PowerMockito.when(mrCategoryProxy.updateMrStatus(any(), anyString())).thenReturn(res);
    List<WoMerchandiseInsideDTO> lstMer = Mockito.spy(ArrayList.class);
    WoMerchandiseInsideDTO o = Mockito.spy(WoMerchandiseInsideDTO.class);
    o.setMerchandiseCode("123");
    o.setQuantity(1D);
    lstMer.add(o);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any())).thenReturn(lstMer);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeCode("123");
    PowerMockito.when(woTypeBusiness
        .findWoTypeById(anyLong())).thenReturn(woTypeInsideDTO);
    UsersEntity usersInsideDto = Mockito.spy(UsersEntity.class);
    usersInsideDto.setUnitId(413314L);
    usersInsideDto.setStaffCode("123");
    PowerMockito.when(userRepository.getUserByUserIdCheck(any())).thenReturn(usersInsideDto);
    WoKTTSInfoDTO ktts = Mockito.spy(WoKTTSInfoDTO.class);
    ktts.setProcessActionName("debug");
    ktts.setProcessActionId(1L);
    ktts.setContractId(1L);
    List<WoKTTSInfoDTO> lstKtts = Mockito.spy(ArrayList.class);
    lstKtts.add(ktts);
    CatItemDTO item = Mockito.spy(CatItemDTO.class);
    item.setItemValue("1");
    PowerMockito.when(catItemRepository.getCatItemById(any())).thenReturn(item);
    Kttsbo res1 = Mockito.spy(Kttsbo.class);
    res1.setStatus(RESULT.SUCCESS);
    try {
      PowerMockito.when(kttsVsmartPort
          .createOrderExportNation(any(), any(), any(), any(), anyList(), any(), anyString()))
          .thenReturn(res1);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    WoInsideDTO parentWo = Mockito.spy(WoInsideDTO.class);
    parentWo.setWoId(1L);
    parentWo.setStatus(4L);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(parentWo);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1D);
    List<WoInsideDTO> listAllChildWo = Mockito.spy(ArrayList.class);
    listAllChildWo.add(wo);
    PowerMockito.when(woRepository.getListWoChildByParentId(any())).thenReturn(listAllChildWo);

    ResultInSideDto resultInSideDto = woBusiness.acceptWoCommon("thanhlv12", 1L, "fixBug", "FT");
    Assert.assertNotNull(resultInSideDto);
  }

  @Test
  public void acceptWoCommon_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Username không tồn tại");
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    UsersInsideDto usersFtDto = Mockito.spy(UsersInsideDto.class);
    usersFtDto.setUnitId(413314L);
    usersFtDto.setUserId(999999L);
    usersFtDto.setUsername("thanhlv12");
    usersFtDto.setStaffCode("123");
    usersFtDto.setMobile("0123456789");
    usersFtDto.setFullname("Le Van Thanh");
    listUser.add(usersFtDto);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "1");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_NOT_CALL_MR, "2");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC, "3");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_THUE, "4");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT, "5");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT_THUE, "6");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_HC, "7");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI, "8");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_DC_HC, "9");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_DC_NC, "10");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_BAO_MAT, "11");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCC, "12");

    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(999999L);
    wo.setStatus(0L);
    wo.setParentId(1L);
    wo.setFtAcceptedTime(new Date());
    wo.setWoSystem("MR");
    wo.setWoCode("123");
    wo.setWoTypeId(1L);
    wo.setWoId(1L);
    wo.setStationCode("1");
    wo.setWoSystemId("10");
    wo.setCdId(1L);
    wo.setCreatePersonId(999999L);
    wo.setFileName("input.txt");
    wo.setWoContent("fixBug");
    wo.setNumPending(1L);
    wo.setFinishTime(new Date());
    wo.setReasonOverdueLV1Id("1");
    wo.setReasonOverdueLV1Name("fixBug1");
    wo.setReasonOverdueLV2Id("2");
    wo.setReasonOverdueLV2Name("fixBug2");
    wo.setSolutionDetail("fixBug");
    wo.setEstimateTime(new Date());
    wo.setCellService("ID");
    wo.setLongitude("1");
    wo.setLatitude("2");
    wo.setConcaveAreaCode("1");
    wo.setSolutionGroupName("fixBug");
    wo.setFinishTime(new Date());
    wo.setConstructionCode("123");
    wo.setWarehouseCode("123");
    wo.setCdAssignId(1L);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(413314L);
    unit.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unit);
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.SUCCESS);
    PowerMockito.when(ttServiceProxy.onUpdateTroubleFromWo(any())).thenReturn(res);
    PowerMockito.when(mrCategoryProxy.updateMrStatus(any(), anyString())).thenReturn(res);
    List<WoMerchandiseInsideDTO> lstMer = Mockito.spy(ArrayList.class);
    WoMerchandiseInsideDTO o = Mockito.spy(WoMerchandiseInsideDTO.class);
    o.setMerchandiseCode("123");
    o.setQuantity(1D);
    lstMer.add(o);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any())).thenReturn(lstMer);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeCode("123");
    PowerMockito.when(woTypeBusiness
        .findWoTypeById(anyLong())).thenReturn(woTypeInsideDTO);
    UsersEntity usersInsideDto = Mockito.spy(UsersEntity.class);
    usersInsideDto.setUnitId(413314L);
    usersInsideDto.setStaffCode("123");
    PowerMockito.when(userRepository.getUserByUserIdCheck(any())).thenReturn(usersInsideDto);
    WoKTTSInfoDTO ktts = Mockito.spy(WoKTTSInfoDTO.class);
    ktts.setProcessActionName("debug");
    ktts.setProcessActionId(1L);
    ktts.setContractId(1L);
    List<WoKTTSInfoDTO> lstKtts = Mockito.spy(ArrayList.class);
    lstKtts.add(ktts);
    CatItemDTO item = Mockito.spy(CatItemDTO.class);
    item.setItemValue("1");
    PowerMockito.when(catItemRepository.getCatItemById(any())).thenReturn(item);
    Kttsbo res1 = Mockito.spy(Kttsbo.class);
    res1.setStatus(RESULT.SUCCESS);
    try {
      PowerMockito.when(kttsVsmartPort
          .createOrderExportNation(any(), any(), any(), any(), anyList(), any(), anyString()))
          .thenReturn(res1);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    WoInsideDTO parentWo = Mockito.spy(WoInsideDTO.class);
    parentWo.setWoId(1L);
    parentWo.setStatus(4L);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(parentWo);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1D);
    List<WoInsideDTO> listAllChildWo = Mockito.spy(ArrayList.class);
    listAllChildWo.add(wo);
    PowerMockito.when(woRepository.getListDataSearch1(any())).thenReturn(listAllChildWo);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUser);
    ResultInSideDto resultInSideDto = woBusiness.acceptWoCommon("thanhlv12", 1L, "fixBug", "CD");
    Assert.assertNotNull(resultInSideDto);
  }

  @Test
  public void acceptWoCommon_03() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Username không tồn tại");
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    UsersInsideDto usersFtDto = Mockito.spy(UsersInsideDto.class);
    usersFtDto.setUnitId(413314L);
    usersFtDto.setUserId(999999L);
    usersFtDto.setUsername("thanhlv12");
    usersFtDto.setStaffCode("123");
    usersFtDto.setMobile("0123456789");
    usersFtDto.setFullname("Le Van Thanh");
    listUser.add(usersFtDto);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "1");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_NOT_CALL_MR, "2");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC, "3");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_THUE, "4");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT, "5");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT_THUE, "6");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_HC, "7");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI, "8");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_DC_HC, "9");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_DC_NC, "10");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_BAO_MAT, "11");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCC, "12");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(999999L);
    wo.setStatus(3L);
    wo.setParentId(1L);
    wo.setFtAcceptedTime(new Date());
    wo.setWoSystem("MR");
    wo.setWoCode("123");
    wo.setWoTypeId(10L);
    wo.setWoId(1L);
    wo.setStationCode("1");
    wo.setWoSystemId("10");
    wo.setCdId(1L);
    wo.setCreatePersonId(999999L);
    wo.setFileName("input.txt");
    wo.setWoContent("fixBug");
    wo.setNumPending(1L);
    wo.setFinishTime(new Date());
    wo.setReasonOverdueLV1Id("1");
    wo.setReasonOverdueLV1Name("fixBug1");
    wo.setReasonOverdueLV2Id("2");
    wo.setReasonOverdueLV2Name("fixBug2");
    wo.setSolutionDetail("fixBug");
    wo.setEstimateTime(new Date());
    wo.setCellService("ID");
    wo.setLongitude("1");
    wo.setLatitude("2");
    wo.setConcaveAreaCode("1");
    wo.setSolutionGroupName("fixBug");
    wo.setFinishTime(new Date());
    wo.setConstructionCode("123");
    wo.setWarehouseCode("123");
    wo.setCdAssignId(1L);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(413314L);
    unit.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unit);
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.SUCCESS);
    PowerMockito.when(ttServiceProxy.onUpdateTroubleFromWo(any())).thenReturn(res);
    PowerMockito.when(mrCategoryProxy.updateMrStatus(any(), anyString())).thenReturn(res);
    List<WoMerchandiseInsideDTO> lstMer = Mockito.spy(ArrayList.class);
    WoMerchandiseInsideDTO o = Mockito.spy(WoMerchandiseInsideDTO.class);
    o.setMerchandiseCode("123");
    o.setQuantity(1D);
    lstMer.add(o);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any())).thenReturn(lstMer);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeCode("123");
    PowerMockito.when(woTypeBusiness
        .findWoTypeById(anyLong())).thenReturn(woTypeInsideDTO);
    UsersEntity usersInsideDto = Mockito.spy(UsersEntity.class);
    usersInsideDto.setUnitId(413314L);
    usersInsideDto.setStaffCode("123");
    PowerMockito.when(userRepository.getUserByUserIdCheck(any())).thenReturn(usersInsideDto);
    WoKTTSInfoDTO ktts = Mockito.spy(WoKTTSInfoDTO.class);
    ktts.setProcessActionName("debug");
    ktts.setProcessActionId(1L);
    ktts.setContractId(1L);
    List<WoKTTSInfoDTO> lstKtts = Mockito.spy(ArrayList.class);
    lstKtts.add(ktts);
    CatItemDTO item = Mockito.spy(CatItemDTO.class);
    item.setItemValue("1");
    PowerMockito.when(catItemRepository.getCatItemById(any())).thenReturn(item);
    Kttsbo res1 = Mockito.spy(Kttsbo.class);
    res1.setStatus(RESULT.SUCCESS);
    try {
      PowerMockito.when(kttsVsmartPort
          .createOrderExportNation(any(), any(), any(), any(), anyList(), any(), anyString()))
          .thenReturn(res1);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    WoInsideDTO parentWo = Mockito.spy(WoInsideDTO.class);
    parentWo.setWoId(1L);
    parentWo.setStatus(4L);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(parentWo);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1D);
    List<WoInsideDTO> listAllChildWo = Mockito.spy(ArrayList.class);
    listAllChildWo.add(wo);
    PowerMockito.when(woRepository.getListDataSearch1(any())).thenReturn(listAllChildWo);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUser);
    PowerMockito.when(woRepository.getListWoChildByParentId(anyLong())).thenReturn(listAllChildWo);
    ResultInSideDto resultInSideDto = woBusiness.acceptWoCommon("thanhlv12", 1L, "fixBug", "FT");
    Assert.assertNotNull(resultInSideDto);
  }

  @Test
  public void acceptWoCommon_04() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Username không tồn tại");
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    UsersInsideDto usersFtDto = Mockito.spy(UsersInsideDto.class);
    usersFtDto.setUnitId(413314L);
    usersFtDto.setUserId(999999L);
    usersFtDto.setUsername("thanhlv12");
    usersFtDto.setStaffCode("123");
    usersFtDto.setMobile("0123456789");
    usersFtDto.setFullname("Le Van Thanh");
    listUser.add(usersFtDto);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "1");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_NOT_CALL_MR, "2");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC, "3");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_THUE, "4");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT, "5");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT_THUE, "6");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_HC, "7");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI, "8");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_DC_HC, "9");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_DC_NC, "10");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS_BAO_MAT, "11");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCC, "12");

    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(999999L);
    wo.setStatus(3L);
    wo.setParentId(1L);
    wo.setFtAcceptedTime(new Date());
    wo.setWoSystem("MR");
    wo.setWoCode("123");
    wo.setWoTypeId(11L);
    wo.setWoId(1L);
    wo.setStationCode("1");
    wo.setWoSystemId("10");
    wo.setCdId(1L);
    wo.setCreatePersonId(999999L);
    wo.setFileName("input.txt");
    wo.setWoContent("fixBug");
    wo.setNumPending(1L);
    wo.setFinishTime(new Date());
    wo.setReasonOverdueLV1Id("1");
    wo.setReasonOverdueLV1Name("fixBug1");
    wo.setReasonOverdueLV2Id("2");
    wo.setReasonOverdueLV2Name("fixBug2");
    wo.setSolutionDetail("fixBug");
    wo.setEstimateTime(new Date());
    wo.setCellService("ID");
    wo.setLongitude("1");
    wo.setLatitude("2");
    wo.setConcaveAreaCode("1");
    wo.setSolutionGroupName("fixBug");
    wo.setFinishTime(new Date());
    wo.setConstructionCode("123");
    wo.setWarehouseCode("123");
    wo.setCdAssignId(1L);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(413314L);
    unit.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unit);
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.SUCCESS);
    PowerMockito.when(ttServiceProxy.onUpdateTroubleFromWo(any())).thenReturn(res);
    PowerMockito.when(mrCategoryProxy.updateMrStatus(any(), anyString())).thenReturn(res);
    List<WoMerchandiseInsideDTO> lstMer = Mockito.spy(ArrayList.class);
    WoMerchandiseInsideDTO o = Mockito.spy(WoMerchandiseInsideDTO.class);
    o.setMerchandiseCode("123");
    o.setQuantity(1D);
    o.setSerial("1");
    lstMer.add(o);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any())).thenReturn(lstMer);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeCode("123");
    PowerMockito.when(woTypeBusiness
        .findWoTypeById(anyLong())).thenReturn(woTypeInsideDTO);
    UsersEntity usersInsideDto = Mockito.spy(UsersEntity.class);
    usersInsideDto.setUnitId(413314L);
    usersInsideDto.setStaffCode("123");
    PowerMockito.when(userRepository.getUserByUserIdCheck(any())).thenReturn(usersInsideDto);
    WoKTTSInfoDTO ktts = Mockito.spy(WoKTTSInfoDTO.class);
    ktts.setProcessActionName("debug");
    ktts.setProcessActionId(1L);
    ktts.setContractId(1L);
    List<WoKTTSInfoDTO> lstKtts = Mockito.spy(ArrayList.class);
    lstKtts.add(ktts);
    CatItemDTO item = Mockito.spy(CatItemDTO.class);
    item.setItemValue("1");
    PowerMockito.when(catItemRepository.getCatItemById(any())).thenReturn(item);
    Kttsbo res1 = Mockito.spy(Kttsbo.class);
    res1.setStatus(RESULT.SUCCESS);
    try {
      PowerMockito.when(kttsVsmartPort
          .createOrderExportNation(any(), any(), any(), any(), anyList(), any(), anyString()))
          .thenReturn(res1);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    WoInsideDTO parentWo = Mockito.spy(WoInsideDTO.class);
    parentWo.setWoId(1L);
    parentWo.setStatus(4L);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(parentWo);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1D);
    List<WoInsideDTO> listAllChildWo = Mockito.spy(ArrayList.class);
    listAllChildWo.add(wo);
    PowerMockito.when(woRepository.getListDataSearch1(any())).thenReturn(listAllChildWo);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUser);
    PowerMockito.when(woRepository.getListWoChildByParentId(any()))
        .thenReturn(listAllChildWo);
    Kttsbo kttsResult = Mockito.spy(Kttsbo.class);
    kttsResult.setStatus(RESULT.SUCCESS);
    PowerMockito.when(kttsVsmartPort.createAssetLostNation(anyList(), any(), anyString()))
        .thenReturn(kttsResult);
    ResultInSideDto resultInSideDto = woBusiness.acceptWoCommon("thanhlv12", 1L, "fixBug", "FT");
    Assert.assertNotNull(resultInSideDto);
  }

  @Test
  public void updateParentStatusOld_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoInsideDTO parentWo = Mockito.spy(WoInsideDTO.class);
    parentWo.setWoId(1L);
    parentWo.setStatus(3L);
    parentWo.setCdId(1L);
    parentWo.setCreatePersonId(999999L);
    parentWo.setFileName("input.txt");
    parentWo.setWoCode("123");
    parentWo.setWoContent("fixBug");
    Long statusChanged = 8L;
    String idCmbResult = WO_RESULT.NOK_CLOSE;
    UsersInsideDto user = Mockito.spy(UsersInsideDto.class);
    user.setUserId(999999L);
    user.setUsername("thanhlv12");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offsetFromUser = 1D;
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(offsetFromUser);
    List<WoInsideDTO> listAllChildWo = Mockito.spy(ArrayList.class);
    listAllChildWo.add(parentWo);
    PowerMockito.when(woRepository.getListDataSearch1(any())).thenReturn(listAllChildWo);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    woBusiness.updateParentStatusOld(parentWo, statusChanged, idCmbResult, user, "fixBug");
  }

  @Test
  public void updateParentStatusOld_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoInsideDTO parentWo = Mockito.spy(WoInsideDTO.class);
    parentWo.setWoId(1L);
    parentWo.setStatus(20L);
    parentWo.setCdId(1L);
    parentWo.setCreatePersonId(999999L);
    parentWo.setFileName("input.txt");
    parentWo.setWoCode("123");
    parentWo.setWoContent("fixBug");
    Long statusChanged = 8L;
    String idCmbResult = WO_RESULT.NOK_CLOSE;
    UsersInsideDto user = Mockito.spy(UsersInsideDto.class);
    user.setUserId(999999L);
    user.setUsername("thanhlv12");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offsetFromUser = 1D;
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(offsetFromUser);
    List<WoInsideDTO> listAllChildWo = Mockito.spy(ArrayList.class);
    listAllChildWo.add(parentWo);
    PowerMockito.when(woRepository.getListDataSearch1(any())).thenReturn(listAllChildWo);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    woBusiness.updateParentStatusOld(parentWo, statusChanged, idCmbResult, user, "fixBug");
  }

  @Test
  public void updateParentStatusOld_03() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoInsideDTO parentWo = Mockito.spy(WoInsideDTO.class);
    parentWo.setWoId(1L);
    parentWo.setStatus(20L);
    parentWo.setCdId(1L);
    parentWo.setCreatePersonId(999999L);
    parentWo.setFileName("input.txt");
    parentWo.setWoCode("123");
    parentWo.setWoContent("fixBug");
    Long statusChanged = 6L;
    String idCmbResult = WO_RESULT.NOK_CLOSE;
    UsersInsideDto user = Mockito.spy(UsersInsideDto.class);
    user.setUserId(999999L);
    user.setUsername("thanhlv12");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offsetFromUser = 1D;
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(offsetFromUser);
    List<WoInsideDTO> listAllChildWo = Mockito.spy(ArrayList.class);
    listAllChildWo.add(parentWo);
    PowerMockito.when(woRepository.getListDataSearch1(any())).thenReturn(listAllChildWo);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    woBusiness.updateParentStatusOld(parentWo, statusChanged, idCmbResult, user, "fixBug");
  }

  @Test
  public void updateParentStatusOld_04() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoInsideDTO parentWo = Mockito.spy(WoInsideDTO.class);
    parentWo.setWoId(1L);
    parentWo.setStatus(20L);
    parentWo.setCdId(1L);
    parentWo.setCreatePersonId(999999L);
    parentWo.setFileName("input.txt");
    parentWo.setWoCode("123");
    parentWo.setWoContent("fixBug");
    Long statusChanged = 5L;
    String idCmbResult = WO_RESULT.NOK_CLOSE;
    UsersInsideDto user = Mockito.spy(UsersInsideDto.class);
    user.setUserId(999999L);
    user.setUsername("thanhlv12");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offsetFromUser = 1D;
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(offsetFromUser);
    List<WoInsideDTO> listAllChildWo = Mockito.spy(ArrayList.class);
    listAllChildWo.add(parentWo);
    PowerMockito.when(woRepository.getListDataSearch1(any())).thenReturn(listAllChildWo);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    woBusiness.updateParentStatusOld(parentWo, statusChanged, idCmbResult, user, "fixBug");
  }

  @Test
  public void updateParentStatusOld_05() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoInsideDTO parentWo = Mockito.spy(WoInsideDTO.class);
    parentWo.setWoId(1L);
    parentWo.setStatus(2L);
    parentWo.setCdId(1L);
    parentWo.setCreatePersonId(999999L);
    parentWo.setFileName("input.txt");
    parentWo.setWoCode("123");
    parentWo.setWoContent("fixBug");
    parentWo.setResult(0L);
    Long statusChanged = 3L;
    String idCmbResult = "1";
    UsersInsideDto user = Mockito.spy(UsersInsideDto.class);
    user.setUserId(999999L);
    user.setUsername("thanhlv12");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    Double offsetFromUser = 1D;
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(offsetFromUser);
    List<WoInsideDTO> listAllChildWo = Mockito.spy(ArrayList.class);
    listAllChildWo.add(parentWo);
    PowerMockito.when(woRepository.getListDataSearch1(any())).thenReturn(listAllChildWo);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    woBusiness.updateParentStatusOld(parentWo, statusChanged, idCmbResult, user, "fixBug");
  }

  @Test
  public void callKTTS() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setFtId(999999L);
    woInsideDTO.setWoTypeId(1L);
    woInsideDTO.setWoCode("123");
    woInsideDTO.setWoId(1L);
    woInsideDTO.setWarehouseCode("123");
    woInsideDTO.setStationCode("123");
    woInsideDTO.setConstructionCode("123");
    UsersEntity usersInsideDto = Mockito.spy(UsersEntity.class);
    usersInsideDto.setStaffCode("123");
    usersInsideDto.setMobile("0123456789");
    PowerMockito.when(userRepository.getUserByUserIdCheck(any())).thenReturn(usersInsideDto);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeCode("123");
    PowerMockito.when(woTypeBusiness.findWoTypeById(anyLong())).thenReturn(woTypeInsideDTO);
    WoKTTSInfoDTO ktts = Mockito.spy(WoKTTSInfoDTO.class);
    ktts.setProcessActionId(1L);
    ktts.setProcessActionName("fixBug");
    List<WoKTTSInfoDTO> lstKtts = Mockito.spy(ArrayList.class);
    lstKtts.add(ktts);
    PowerMockito.when(woKTTSInfoRepository.getListWoKTTSInfoByWoId(any())).thenReturn(lstKtts);
    CatItemDTO item = Mockito.spy(CatItemDTO.class);
    item.setItemValue("1");
    PowerMockito.when(catItemRepository.getCatItemById(any())).thenReturn(item);
    WoMerchandiseInsideDTO i = Mockito.spy(WoMerchandiseInsideDTO.class);
    i.setMerchandiseCode("1");
    i.setQuantity(1D);
    List<WoMerchandiseInsideDTO> lstMer = Mockito.spy(ArrayList.class);
    lstMer.add(i);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any())).thenReturn(lstMer);
    Kttsbo res = Mockito.spy(Kttsbo.class);
    res.setStatus(RESULT.SUCCESS);
    try {
      PowerMockito.when(kttsVsmartPort
          .createOrderExportNation(anyString(), anyString(), anyString(), any(), anyList(), any(),
              anyString())).thenReturn(res);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    ResultInSideDto resultInSideDto = woBusiness.callKTTS(woInsideDTO);
    Assert.assertNotNull(resultInSideDto);
  }

  @Test
  public void checkAcceptUCTT_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(
        "Yêu cầu CD vào cập nhật danh sách hàng hóa, mã trạm, mã kho, hành động thực hiện...trước khi FT tiếp nhận");
    Map<String, String> mapCfg = new HashMap<>();
    mapCfg.put(Constants.AP_PARAM.WO_TYPE_QLTS_HC, "1");
    mapCfg.put(Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI, "2");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoTypeId(3L);
    woInsideDTO.setConstructionCode("123");
    woInsideDTO.setWarehouseCode("123");
    woInsideDTO.setWoId(1L);
    woInsideDTO.setStationCode("123");
    WoMerchandiseInsideDTO woMerchandiseInsideDTO = Mockito.spy(WoMerchandiseInsideDTO.class);
    List<WoMerchandiseInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woMerchandiseInsideDTO);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any())).thenReturn(lst);
    String result = woBusiness.checkAcceptUCTT(woInsideDTO, mapCfg);
    Assert.assertEquals(result, "");
  }

  @Test
  public void dispatchWoFromWeb_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("không tồn tại trên hệ thống");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setFtName("vtnet");
    woInsideDTO.setWoId(1L);
    woInsideDTO.setComment("fixBug");
    List<UsersInsideDto> listUserCd = Mockito.spy(ArrayList.class);
    UsersInsideDto cd = Mockito.spy(UsersInsideDto.class);
    cd.setUserId(999999L);
    cd.setUnitId(413314L);
    cd.setUsername("thanhlv12");
    listUserCd.add(cd);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUserCd);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(4L);
    wo.setFtId(999999L);
    wo.setWoTypeId(1L);
    wo.setCdId(1L);
    wo.setCreatePersonId(999999L);
    wo.setFileName("input.txt");
    wo.setWoCode("123");
    wo.setWoContent("fixBug");
    wo.setWoId(1L);
    PowerMockito.when(woRepository.findWoByIdNoOffset(any())).thenReturn(wo);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unit);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUserCd);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_QLTS, "1");
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "2");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    ResultInSideDto resultInSideDto1 = woBusiness
        .dispatchWo("thanhlv12", "thanhlv12", 1L, "fixBug");
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void rejectWoFromWeb_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Username không tồn tại");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(3L);
    wo.setFtId(999999L);
    wo.setWoTypeId(1L);
    wo.setCdId(1L);
    wo.setCreatePersonId(999999L);
    wo.setFileName("input.txt");
    wo.setWoCode("123");
    wo.setWoContent("fixBug");
    wo.setWoId(1L);
    wo.setParentId(1L);
    wo.setWoSystem("SPM");
    wo.setRole(WO_MASTER_CODE.WO_FT);
    wo.setComment("fixBug");
    PowerMockito.when(woRepository.findWoByIdNoOffset(any())).thenReturn(wo);
    List<UsersInsideDto> listUsers = Mockito.spy(ArrayList.class);
    UsersInsideDto cd = Mockito.spy(UsersInsideDto.class);
    cd.setUserId(999999L);
    cd.setUnitId(413314L);
    cd.setUsername("thanhlv12");
    listUsers.add(cd);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUsers);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "1");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unit);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUsers);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(ttServiceProxy.onUpdateTroubleMobile(any())).thenReturn(resultDTO);
    ResultInSideDto resultInSideDto1 = woBusiness.rejectWoFromWeb(wo);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void rejectWoFromWeb_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Username không tồn tại");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(0L);
    wo.setFtId(999999L);
    wo.setWoTypeId(1L);
    wo.setCdId(1L);
    wo.setCreatePersonId(999999L);
    wo.setFileName("input.txt");
    wo.setWoCode("123");
    wo.setWoContent("fixBug");
    wo.setWoId(1L);
    wo.setParentId(1L);
    wo.setWoSystem("MR");
    wo.setRole(WO_MASTER_CODE.WO_CD);
    wo.setComment("fixBug");
    PowerMockito.when(woRepository.findWoByIdNoOffset(any())).thenReturn(wo);
    List<UsersInsideDto> listUsers = Mockito.spy(ArrayList.class);
    UsersInsideDto cd = Mockito.spy(UsersInsideDto.class);
    cd.setUserId(999999L);
    cd.setUnitId(413314L);
    cd.setUsername("thanhlv12");
    listUsers.add(cd);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUsers);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "1");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unit);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUsers);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(ttServiceProxy.onUpdateTroubleFromWo(any())).thenReturn(resultDTO);
    ResultInSideDto resultInSideDto1 = woBusiness.rejectWoFromWeb(wo);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void rejectWoFromWeb_03() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Username không tồn tại");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(3L);
    wo.setFtId(999999L);
    wo.setWoTypeId(1L);
    wo.setCdId(1L);
    wo.setCreatePersonId(999999L);
    wo.setFileName("input.txt");
    wo.setWoCode("123");
    wo.setWoContent("fixBug");
    wo.setWoId(1L);
    wo.setParentId(1L);
    wo.setWoSystem("MR");
    wo.setRole(WO_MASTER_CODE.WO_FT);
    wo.setComment("fixBug");
    PowerMockito.when(woRepository.findWoByIdNoOffset(any())).thenReturn(wo);
    List<UsersInsideDto> listUsers = Mockito.spy(ArrayList.class);
    UsersInsideDto cd = Mockito.spy(UsersInsideDto.class);
    cd.setUserId(999999L);
    cd.setUnitId(413314L);
    cd.setUsername("thanhlv12");
    listUsers.add(cd);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUsers);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "1");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unit);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUsers);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(ttServiceProxy.onUpdateTroubleFromWo(any())).thenReturn(resultDTO);
    ResultInSideDto resultInSideDto1 = woBusiness.rejectWoFromWeb(wo);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void auditWoFromWeb_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Thực hiện audit wo kết quả không đạt:");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(3L);
    wo.setFtId(999999L);
    wo.setWoTypeId(1L);
    wo.setCdId(1L);
    wo.setCreatePersonId(999999L);
    wo.setFileName("input.txt");
    wo.setWoCode("123");
    wo.setWoContent("fixBug");
    wo.setWoId(1L);
    wo.setParentId(1L);
    wo.setWoSystem("MR");
    wo.setRole(WO_MASTER_CODE.WO_FT);
    wo.setComment("fixBug");
    wo.setAuditResult(WO_MASTER_CODE.WO_NOK);
    wo.setFinishTime(new Date());
    wo.setEndTime(new Date());
    UsersEntity usersInsideDto = Mockito.spy(UsersEntity.class);
    usersInsideDto.setUserId(999999L);
    usersInsideDto.setStaffCode("123");
    usersInsideDto.setFullname("Le Van Thanh");
    usersInsideDto.setMobile("0123456789");
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    PowerMockito.when(messagesRepository.insertOrUpdateWfm(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(userRepository.getUserByUserIdCheck(any())).thenReturn(usersInsideDto);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    UsersInsideDto user = Mockito.spy(UsersInsideDto.class);
    user.setUserId(999999L);
    user.setUnitId(413314L);
    user.setUsername("thanhlv12");
    user.setMobile("0123456789");
    List<UsersInsideDto> listUsers = Mockito.spy(ArrayList.class);
    listUsers.add(user);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUsers);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUsers);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woBusiness.auditWoFromWeb(wo);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void auditWoFromWeb_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Thực hiện audit wo kết quả không đạt:");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(3L);
    wo.setFtId(999999L);
    wo.setWoTypeId(1L);
    wo.setCdId(1L);
    wo.setCreatePersonId(999999L);
    wo.setFileName("input.txt");
    wo.setWoCode("123");
    wo.setWoContent("fixBug");
    wo.setWoId(1L);
    wo.setParentId(1L);
    wo.setWoSystem("MR");
    wo.setRole(WO_MASTER_CODE.WO_FT);
    wo.setComment("fixBug");
    wo.setAuditResult(WO_MASTER_CODE.WO_OK);
    wo.setFinishTime(new Date());
    wo.setEndTime(new Date());
    UsersEntity usersInsideDto = Mockito.spy(UsersEntity.class);
    usersInsideDto.setUserId(999999L);
    usersInsideDto.setStaffCode("123");
    usersInsideDto.setFullname("Le Van Thanh");
    usersInsideDto.setMobile("0123456789");
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(usersInsideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    ResultInSideDto resultInSideDto1 = woBusiness.auditWoFromWeb(wo);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void pendingWoFromWeb_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Mã wo là bắt buộc");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoCode("123");
    woInsideDTO.setEndPendingTime(DateUtil.string2DateTime("01/01/2020 10:00:00"));
    woInsideDTO.setSystem("SPM");
    woInsideDTO.setReasonName("fixBug");
    woInsideDTO.setReasonId("1");
    woInsideDTO.setCustomer("thanhlv12");
    woInsideDTO.setPhone("0123456789");
    woInsideDTO.setWoTypeId(1L);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UsersInsideDto us = Mockito.spy(UsersInsideDto.class);
    us.setUnitId(413314L);
    us.setUserId(999999L);
    us.setUsername("thanhlv12");
    List<UsersInsideDto> listUs = Mockito.spy(ArrayList.class);
    listUs.add(us);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUs);
    Map<String, String> mapConfigProperty = Mockito.spy(HashMap.class);
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "1");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoTypeId(1L);
    wo.setNumPending(1L);
    wo.setEndTime(new Date());
    wo.setWoId(1L);
    wo.setWoSystem("SPM");
    wo.setWoSystemId("1");
    wo.setCellService("1");
    wo.setLongitude("1");
    wo.setLatitude("1");
    wo.setConcaveAreaCode("1");
    wo.setSolutionGroupName("fixBug");
    wo.setFinishTime(new Date());
    wo.setReasonOverdueLV1Id("1");
    wo.setReasonOverdueLV1Name("fixBug");
    wo.setReasonOverdueLV2Id("2");
    wo.setReasonOverdueLV2Name("fixBug");
    wo.setSolutionDetail("fixBug");
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(413314L);
    unit.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unit);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    try {
      PowerMockito.when(ttServiceProxy.onUpdateTroubleFromWo(any())).thenReturn(resultDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    PowerMockito.when(woPendingRepository.insertWoPending(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woBusiness.pendingWoFromWeb(woInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void updatePendingWoFromWeb_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Mã wo là bắt buộc");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoCode("123");
    //woInsideDTO.setEndPendingTime(DateUtil.string2DateTime("01/01/2020 10:00:00"));
    woInsideDTO.setSystem("SPM");
    woInsideDTO.setReasonName("fixBug");
    woInsideDTO.setReasonId("1");
    woInsideDTO.setCustomer("thanhlv12");
    woInsideDTO.setPhone("0123456789");
    woInsideDTO.setWoTypeId(1L);
    woInsideDTO.setComment("fixBug");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoTypeId(1L);
    wo.setNumPending(1L);
    wo.setEndTime(new Date());
    wo.setWoId(1L);
    wo.setWoSystem("SPM");
    wo.setWoSystemId("1");
    wo.setCellService("1");
    wo.setLongitude("1");
    wo.setLatitude("1");
    wo.setConcaveAreaCode("1");
    wo.setSolutionGroupName("fixBug");
    wo.setFinishTime(new Date());
    wo.setReasonOverdueLV1Id("1");
    wo.setReasonOverdueLV1Name("fixBug");
    wo.setReasonOverdueLV2Id("2");
    wo.setReasonOverdueLV2Name("fixBug");
    wo.setSolutionDetail("fixBug");
    wo.setEndPendingTime(new Date());
    wo.setEndTime(new Date());
    wo.setStatus(9L);
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(413314L);
    unit.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unit);
    Map<String, String> mapConfigProperty = Mockito.spy(HashMap.class);
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "1");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    UsersInsideDto us = Mockito.spy(UsersInsideDto.class);
    us.setUnitId(413314L);
    us.setUserId(999999L);
    us.setUsername("thanhlv12");
    List<UsersInsideDto> listUs = Mockito.spy(ArrayList.class);
    listUs.add(us);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUs);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    List<WoPendingDTO> lst = Mockito.spy(ArrayList.class);
    WoPendingDTO woPending = Mockito.spy(WoPendingDTO.class);
    woPending.setInsertTime(new Date());
    lst.add(woPending);
    PowerMockito.when(woPendingRepository.getListWoPendingByWoId(anyLong())).thenReturn(lst);
    PowerMockito.when(woPendingRepository.updateWoPending(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woBusiness.updatePendingWoFromWeb(woInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void updatePendingWoFromWeb_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Mã wo là bắt buộc");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoCode("123");
    woInsideDTO.setEndPendingTime(DateUtil.string2DateTime("01/01/2020 10:00:00"));
    woInsideDTO.setSystem("SPM");
    woInsideDTO.setReasonName("fixBug");
    woInsideDTO.setReasonId("1");
    woInsideDTO.setCustomer("thanhlv12");
    woInsideDTO.setPhone("0123456789");
    woInsideDTO.setWoTypeId(1L);
    woInsideDTO.setComment("fixBug");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoTypeId(1L);
    wo.setNumPending(1L);
    wo.setEndTime(new Date());
    wo.setWoId(1L);
    wo.setWoSystem("SPM");
    wo.setWoSystemId("1");
    wo.setCellService("1");
    wo.setLongitude("1");
    wo.setLatitude("1");
    wo.setConcaveAreaCode("1");
    wo.setSolutionGroupName("fixBug");
    wo.setFinishTime(new Date());
    wo.setReasonOverdueLV1Id("1");
    wo.setReasonOverdueLV1Name("fixBug");
    wo.setReasonOverdueLV2Id("2");
    wo.setReasonOverdueLV2Name("fixBug");
    wo.setSolutionDetail("fixBug");
    wo.setEndPendingTime(new Date());
    wo.setEndTime(new Date());
    wo.setStatus(9L);
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(413314L);
    unit.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unit);
    Map<String, String> mapConfigProperty = Mockito.spy(HashMap.class);
    mapConfigProperty.put(Constants.AP_PARAM.WO_TYPE_XLSCVT, "1");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    UsersInsideDto us = Mockito.spy(UsersInsideDto.class);
    us.setUnitId(413314L);
    us.setUserId(999999L);
    us.setUsername("thanhlv12");
    List<UsersInsideDto> listUs = Mockito.spy(ArrayList.class);
    listUs.add(us);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUs);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    List<WoPendingDTO> lst = Mockito.spy(ArrayList.class);
    WoPendingDTO woPending = Mockito.spy(WoPendingDTO.class);
    woPending.setInsertTime(new Date());
    lst.add(woPending);
    PowerMockito.when(woPendingRepository.getListWoPendingByWoId(anyLong())).thenReturn(lst);
    PowerMockito.when(woPendingRepository.updateWoPending(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = woBusiness.updatePendingWoFromWeb(woInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void getListConfigPropertyValue_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> strings = Mockito.spy(ArrayList.class);
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString()))
        .thenReturn(RESULT.SUCCESS);
    strings = woBusiness.getListConfigPropertyValue(anyString());
    Assert.assertEquals(strings.size(), 1);
  }

  @Test
  public void closeNotCreateAlarm_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.mockStatic(NocProPort.class);
    PowerMockito.doNothing().when(logger).debug(any());
    JsonResponseBO resNoc = Mockito.spy(JsonResponseBO.class);
    resNoc.setStatus(1);
    try {
      ResultDTO resultDTO = woBusiness.closeNotCreateAlarm("123");
      Assert.assertNotNull(resultDTO);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

  }

  @Test
  public void setParam_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Param p = woBusiness.setParam("ID", "1");
    Assert.assertEquals(p.getName(), "ID");
  }

  @Test
  public void checkCloseWoDiDoiHuyTram_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Trạm chưa được hủy trên NIMS");
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    UsersEntity u = Mockito.spy(UsersEntity.class);
    u.setUnitId(413314L);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(u);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setLocationId(1L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setNationCode("VNM");
    PowerMockito.when(woRepository.getCatLocationById(anyLong())).thenReturn(catLocationDTO);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setDeviceCode("123");
    ResultCheckStatusCabinet res = Mockito.spy(ResultCheckStatusCabinet.class);
    res.setResult(WS_RESULT.OK);
    res.setCabinetStatus(Constants.AP_PARAM.NIMS_CABINET_STATUS_HUY);
    Kttsbo resTS = Mockito.spy(Kttsbo.class);
    resTS.setStatus(WS_RESULT.OK);
    PowerMockito.when(wsHTNims.checkStatusCabinet(any())).thenReturn(res);
    PowerMockito.when(kttsVsmartPort.checkOffStationNation(any(), any(), any()))
        .thenReturn(resTS);
    ResultDTO resultDTO1 = woBusiness.checkCloseWoDiDoiHuyTram(wo, 1L);
    Assert.assertNotNull(resultDTO1);
  }

  @Test
  public void checkCloseWoDiDoiHuyTram_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Trạm chưa được hủy trên NIMS");
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    UsersEntity u = Mockito.spy(UsersEntity.class);
    u.setUnitId(413314L);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(u);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setLocationId(1L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setNationCode("VNM");
    PowerMockito.when(woRepository.getCatLocationById(anyLong())).thenReturn(catLocationDTO);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setDeviceCode("123");
    ResultCheckStatusStations res = Mockito.spy(ResultCheckStatusStations.class);
    res.setResult(WS_RESULT.OK);
    res.setStationStatus("HUY");
    Kttsbo resTS = Mockito.spy(Kttsbo.class);
    resTS.setStatus(WS_RESULT.OK);
    PowerMockito.when(wsHTNims.checkStatusStations(any())).thenReturn(res);
    PowerMockito.when(kttsVsmartPort.checkOffStationNation(any(), any(), any()))
        .thenReturn(resTS);
    ResultDTO resultDTO1 = woBusiness.checkCloseWoDiDoiHuyTram(wo, 2L);
    Assert.assertNotNull(resultDTO1);
  }


  @Test
  public void closeToIncident_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFinishTime(new Date());
    wo.setCommentComplete("fixBug");
    wo.setFtId(999999L);
    wo.setWoSystemId("1");
    WoTroubleInfoDTO woTroubleInfo = Mockito.spy(WoTroubleInfoDTO.class);
    woTroubleInfo.setReasonTroubleId(1L);
    woTroubleInfo.setReasonTroubleName("fixBug");
    woTroubleInfo.setSolutionGroupId(1L);
    woTroubleInfo.setSolution("fixBug");
    woTroubleInfo.setSolutionGroupName("WorkArround");
    woTroubleInfo.setClosuresReplace("fixBug");
    woTroubleInfo.setCodeSnippetOff("1");
    woTroubleInfo.setLineCutCode("1");
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUnitId(413314L);
    us.setUserId(999999L);
    us.setUsername("thanhlv12");
    PowerMockito.when(userRepository.getUserByUserIdCheck(any())).thenReturn(us);
    UnitDTO un = Mockito.spy(UnitDTO.class);
    un.setUnitId(413314L);
    un.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(un);
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.SUCCESS);
    PowerMockito.when(ttServiceProxy.onClosetroubleFromWo(any())).thenReturn(res);
    woBusiness.closeToIncident(wo, woTroubleInfo);
  }

  @Test
  public void getNationFromUserId_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UsersEntity u = Mockito.spy(UsersEntity.class);
    u.setUnitId(413314L);
    PowerMockito.when(userRepository.getUserByUserIdCheck(any())).thenReturn(u);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setLocationId(84L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setNationCode("VNM");
    PowerMockito.when(woRepository.getCatLocationById(anyLong())).thenReturn(catLocationDTO);
    String result = woBusiness.getNationFromUserId(999999L);
    Assert.assertEquals(result, "VNM");
  }

  @Test
  public void updateWoTroubleInfo_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoTroubleInfoDTO woTrouble = Mockito.spy(WoTroubleInfoDTO.class);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setReasonTroubleId(1L);
    wo.setReasonTroubleName("fixBug");
    wo.setSolution("fixBug");
    wo.setSolutionGroupId(1L);
    wo.setClearTime(new Date());
    wo.setPolesDistance(1D);
    wo.setScriptId(1L);
    wo.setScriptName("fixBug");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woTroubleInfoRepository.insertWoTroubleInfo(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Có lỗi xảy ra khi lưu thông tin liên quan đến ticket");
    woBusiness.updateWoTroubleInfo(woTrouble, wo);
  }

  @Test
  public void getCdByStationCode_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    NimsStationForm form = Mockito.spy(NimsStationForm.class);
    try {
      PowerMockito.when(wsHTNims.getStationInfo(any())).thenReturn(form);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    UnitDTO u = Mockito.spy(UnitDTO.class);
    PowerMockito.when(woRepository.getUnitCodeMapNims(any(), any())).thenReturn(u);
    WoCdGroupInsideDTO model = Mockito.spy(WoCdGroupInsideDTO.class);
    model.setIsEnable(1L);
    PowerMockito.when(woRepository.getCdByUnitCode(any(), any(), any()))
        .thenReturn(model);
    WoCdGroupInsideDTO woCdGroupInsideDTO = woBusiness.getCdByStationCode("1", 1L, "1");
    Assert.assertEquals(model.getIsEnable(), woCdGroupInsideDTO.getIsEnable());
  }

  @Test
  public void getMessagesForCd_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    List<MessagesDTO> lstMess = Mockito.spy(ArrayList.class);
    MessagesDTO messagesDTO = Mockito.spy(MessagesDTO.class);
    messagesDTO.setUserLanguage("2");
    lstMess.add(messagesDTO);
    PowerMockito.when(woRepository.getMessagesForCd(any(), anyString(), any())).thenReturn(lstMess);
    List<MessagesDTO> lstMess1 = woBusiness
        .getMessagesForCd(woInsideDTO, "deBug#####fixBug", new Date());
    Assert.assertEquals(lstMess.size(), lstMess1.size());
  }

  @Test
  public void getMessagesForFT_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    List<MessagesDTO> lstMess = Mockito.spy(ArrayList.class);
    MessagesDTO messagesDTO = Mockito.spy(MessagesDTO.class);
    messagesDTO.setUserLanguage("2");
    lstMess.add(messagesDTO);
    PowerMockito.when(woRepository.getMessagesForFT(any(), anyString(), any())).thenReturn(lstMess);
    List<MessagesDTO> lstMess1 = woBusiness
        .getMessagesForFT(woInsideDTO, "deBug#####fixBug", new Date());
    Assert.assertEquals(lstMess.size(), lstMess1.size());
  }

  @Test
  public void initServiceProblemInfoDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Hệ thống tự động đóng WO sau khi có kết quả bài test. Kết quả: #Result");
    WoDetailDTO woDetailDTO = Mockito.spy(WoDetailDTO.class);
    woDetailDTO.setSpmCode("123");
    woDetailDTO.setCcResult(1L);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setWoCode("123");
    woInsideDTO.setReasonOverdueLV1Id("1");
    woInsideDTO.setReasonOverdueLV1Name("fixBug");
    woInsideDTO.setReasonOverdueLV2Id("2");
    woInsideDTO.setReasonOverdueLV2Name("fixBug");
    woInsideDTO.setFinishTime(new Date());
    woInsideDTO.setStartTime(new Date());
    woInsideDTO.setEndTime(new Date());
    CompCause lv3 = Mockito.spy(CompCause.class);
    lv3.setCompCauseId(1L);
    lv3.setName("fixBug");
    lv3.setParentId(1L);
    PowerMockito.when(woRepository.getCompCause(anyLong())).thenReturn(lv3);
    WoPendingDTO i = Mockito.spy(WoPendingDTO.class);
    i.setOpenTime(new Date());
    i.setInsertTime(new Date());
    List<WoPendingDTO> lstPending = Mockito.spy(ArrayList.class);
    lstPending.add(i);
    PowerMockito.when(woPendingRepository.getListWoPendingByWoId(any())).thenReturn(lstPending);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitName("vtnet");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    ServiceProblemInfoDTO serviceProblemInfoDTO = woBusiness
        .initServiceProblemInfoDTO(woDetailDTO, woInsideDTO, usersInsideDto, unitDTO);
    Assert.assertNotNull(serviceProblemInfoDTO);
  }

  @Test
  public void generateMessagesContent_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(
        "[WFM] WO: [WO_CODE], Account: [ACCOUNT_ISDN] da kiem tra tin hieu DAT. He thong tu dong dong WO!#####[WFM] WO: [WO_CODE], Account: [ACCOUNT_ISDN] Autocheck result SUCCESS. System auto close WO!");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoCode("123");
    String result = woBusiness.generateMessagesContent(wo, WS_RESULT.OK, "thanhlv12");
    Assert.assertNotNull(result);
  }

  @Test
  public void generateMessagesContent_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(
        "[WFM] WO: [WO_CODE], Account: [ACCOUNT_ISDN] da kiem tra tin hieu DAT. He thong tu dong dong WO!#####[WFM] WO: [WO_CODE], Account: [ACCOUNT_ISDN] Autocheck result SUCCESS. System auto close WO!");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoCode("123");
    String result = woBusiness.generateMessagesContent(wo, WS_RESULT.NOK, "thanhlv12");
    Assert.assertNotNull(result);
  }

  @Test
  public void generateMessagesContent_03() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(
        "[WFM] WO: [WO_CODE], Account: [ACCOUNT_ISDN] da kiem tra tin hieu DAT. He thong tu dong dong WO!#####[WFM] WO: [WO_CODE], Account: [ACCOUNT_ISDN] Autocheck result SUCCESS. System auto close WO!");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoCode("123");
    String result = woBusiness.generateMessagesContent(wo, WS_RESULT.N_A, "thanhlv12");
    Assert.assertNotNull(result);
  }

  @Test
  public void sendMessages_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(999999L);
    usersInsideDto.setStaffCode("123");
    usersInsideDto.setFullname("Le Van Thanh");
    usersInsideDto.setMobile("0123456789");
    PowerMockito.when(messagesRepository.insertOrUpdateWfm(any())).thenReturn(resultInSideDto);
    woBusiness.sendMessages("fixBug", usersInsideDto,null);
  }

  @Test
  public void updateStatus_SCVT_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTotalPendingTimeGnoc("1");
    troublesDTO.setTotalProcessTimeGnoc("1");
    troublesDTO.setEvaluateGnoc("1");
    troublesDTO.setReasonLv3Id("3");
    troublesDTO.setReasonLv3Name("fixBug");
    troublesDTO.setReasonLv2Id("2");
    troublesDTO.setReasonLv2Name("fixBug");
    troublesDTO.setReasonLv1Id("1");
    troublesDTO.setReasonLv1Name("fixBug");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoSystemId("1");
    woInsideDTO.setEstimateTime(new Date());
    woInsideDTO.setCellService("1");
    woInsideDTO.setLongitude("1");
    woInsideDTO.setLatitude("1");
    woInsideDTO.setConcaveAreaCode("VNM");
    woInsideDTO.setSolutionGroupName("fixBug");
    woInsideDTO.setNumPending(1L);
    woInsideDTO.setFinishTime(new Date());
    woInsideDTO.setReasonOverdueLV1Id("1");
    woInsideDTO.setReasonOverdueLV1Name("fixBug");
    woInsideDTO.setReasonOverdueLV2Id("2");
    woInsideDTO.setReasonOverdueLV2Name("fixBug");
    woInsideDTO.setSolutionDetail("fixBug");
    WoPendingDTO woPendingDTO = Mockito.spy(WoPendingDTO.class);
    woPendingDTO.setReasonPendingName("fixBug");
    woPendingDTO.setEndPendingTime(new Date());
    woPendingDTO.setPendingType(1L);
    WoDetailDTO woDetailDTO = Mockito.spy(WoDetailDTO.class);
    woDetailDTO.setCcResult(1L);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(413314L);
    unitDTO.setUnitName("vtnet");
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(999999L);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setMobile("0123456789");
    CompCause lv3 = Mockito.spy(CompCause.class);
    lv3.setCompCauseId(1L);
    lv3.setName("fixBug");
    lv3.setParentId(1L);
    PowerMockito.when(woRepository.getCompCause(anyLong())).thenReturn(lv3);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(ttServiceProxy.onUpdateTroubleFromWo(any())).thenReturn(resultDTO);
    ResultDTO resultDTO1 = woBusiness
        .updateStatus_SCVT(troublesDTO, woInsideDTO, woPendingDTO, woDetailDTO, unitDTO,
            usersInsideDto, "fixBug", "fixBug", "fixBug", "fixBug");
    Assert.assertNotNull(resultDTO1);
  }

  @Test
  public void updateStatusTT_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTotalPendingTimeGnoc("1");
    troublesDTO.setTotalProcessTimeGnoc("1");
    troublesDTO.setEvaluateGnoc("1");
    troublesDTO.setReasonLv3Id("1");
    troublesDTO.setReasonLv3Name("fixBug");
    troublesDTO.setReasonLv2Id("1");
    troublesDTO.setReasonLv1Id("1");
    troublesDTO.setReasonLv1Name("fixBug");
    troublesDTO.setIsCheck("1");
    List<byte[]> fileDocumentByteArray = Mockito.spy(ArrayList.class);
    byte[] myVar = "Any String".getBytes();
    fileDocumentByteArray.add(myVar);
    troublesDTO.setFileDocumentByteArray(fileDocumentByteArray);
    List<String> arrFileName = Mockito.spy(ArrayList.class);
    arrFileName.add("vtnet");
    troublesDTO.setArrFileName(arrFileName);
    troublesDTO.setErrorCode("404");
    troublesDTO.setConcave("1");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoSystemId("1");
    woInsideDTO.setNumPending(1L);
    woInsideDTO.setFinishTime(new Date());
    woInsideDTO.setReasonOverdueLV1Id("1");
    woInsideDTO.setReasonOverdueLV1Name("fixBug");
    woInsideDTO.setReasonOverdueLV2Id("2");
    woInsideDTO.setReasonOverdueLV2Name("fixBug");
    woInsideDTO.setSolutionDetail("fixBug");
    WoPendingDTO woPendingDTO = Mockito.spy(WoPendingDTO.class);
    woPendingDTO.setReasonPendingName("fixBug");
    woPendingDTO.setEndPendingTime(new Date());
    WoDetailDTO woDetailDTO = Mockito.spy(WoDetailDTO.class);
    woDetailDTO.setCcResult(1L);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(413314L);
    unitDTO.setUnitName("vtnet");
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(999999L);
    usersInsideDto.setUsername("thanhlv12");
    usersInsideDto.setMobile("0123456789");
    CompCause lv3 = Mockito.spy(CompCause.class);
    lv3.setCompCauseId(1L);
    lv3.setName("fixBug");
    lv3.setParentId(1L);
    PowerMockito.when(woRepository.getCompCause(anyLong())).thenReturn(lv3);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(ttServiceProxy.onUpdateTroubleMobile(any())).thenReturn(resultDTO);
    ResultDTO resultDTO1 = woBusiness
        .updateStatusTT(troublesDTO, woInsideDTO, woPendingDTO, woDetailDTO, unitDTO,
            usersInsideDto, "fixBug", "fixBug", "fixBug", "fixBug");
    Assert.assertNotNull(resultDTO1);
  }

  @Test
  public void closeWoCommon_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Có lỗi xảy ra khi đóng WO");
    WoInsideDTO i = Mockito.spy(WoInsideDTO.class);
    i.setStatus(4L);
    i.setCommentComplete("fixBug");
    i.setCdId(1L);
    i.setCreatePersonId(999999L);
    i.setFileName("input.txt");
    i.setFtId(999999L);
    i.setWoCode("123");
    i.setWoContent("fixBug");
    i.setWoId(1L);
    UsersEntity user = Mockito.spy(UsersEntity.class);
    user.setUserId(999999L);
    user.setUsername("thanhlv12");
    List<WoInsideDTO> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(i);
    PowerMockito.when(woRepository.getListWoBySystemOtherCode(any(), any())).thenReturn(lstWo);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(user);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    List<String> listCode = Mockito.spy(ArrayList.class);
    listCode.add("01/01/2010 10:10:10; 02/01/2010 10:10:10");
    ResultInSideDto resultInSideDto1 = woBusiness.closeWoCommon(listCode, "SPM");
    Assert.assertNotNull(resultInSideDto1);
  }

  @Test
  public void convertStatusCR() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Dự thảo");
    String result = woBusiness.convertStatusCR("1");
    Assert.assertNotNull(result);
  }

  @Test
  public void handleFileExport_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("WoListFromCR");
    List list = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"woCode", "woDescription", "cdName"};
    Date date = DateTimeUtils.convertDateOffset();
    File file = woBusiness
        .handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(date), "EXPORT_WO_FROM_CR");
    Assert.assertNotNull(file);
  }

  @Test
  public void convertWoDate2VietNamDate() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoInsideDTO oldDto = Mockito.spy(WoInsideDTO.class);
    oldDto.setStartTime(new Date());
    oldDto.setEndTime(new Date());
    oldDto.setCreateDate(new Date());
    oldDto.setCompletedTime(new Date());
    WoInsideDTO woInsideDTO = woBusiness.convertWoDate2VietNamDate(oldDto, 1D);
    Assert.assertEquals(oldDto, woInsideDTO);
  }

  @Test
  public void callIPCC_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Không tìm thấy nhân viên");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setUserCall("thanhlv12");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoCode("123");
    wo.setFtId(999999L);
    wo.setStatus(4L);
    wo.setCdId(1L);
    PowerMockito.when(woRepository.findWoByIdNoOffset(any())).thenReturn(wo);
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setMobile("0123456789");
    us.setUsername("thanhlv12");
    us.setUnitId(413314L);
    PowerMockito.when(userRepository.getUserByUserIdCheck(any())).thenReturn(us);
    PowerMockito.when(logCallIpccRepository.insertLogCallIpcc(any())).thenReturn(resultInSideDto);
    UnitDTO un = Mockito.spy(UnitDTO.class);
    un.setIpccServiceId(1L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(un);
    List<IpccServiceDTO> lst = Mockito.spy(ArrayList.class);
    IpccServiceDTO ipccServiceDTO = Mockito.spy(IpccServiceDTO.class);
    ipccServiceDTO.setUrl("vtnet.com");
    ipccServiceDTO.setIpccServiceCode("123");
    lst.add(ipccServiceDTO);
    PowerMockito.when(smsGatewayRepository.getListIpccServiceDTO(any())).thenReturn(lst);
    NomalOutput outPut = Mockito.spy(NomalOutput.class);
    outPut.setDescription(RESULT.SUCCESS);
    PowerMockito.when(ipccPort.autoCallout(any(), any())).thenReturn(outPut);
    ResultInSideDto resultInSideDto1 = woBusiness.callIPCC(woInsideDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void getListLogCallIpccDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(logCallIpccRepository.getListLogCallIpccDTO(any())).thenReturn(datatable);
    Datatable datatable1 = woBusiness.getListLogCallIpccDTO(any());
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getWoPriorityByWoTypeID() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<WoPriorityDTO> woPriorityDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.getWoPriorityByWoTypeID(any())).thenReturn(woPriorityDTOS);
    List<WoPriorityDTO> woPriorityDTOS1 = woBusiness.getWoPriorityByWoTypeID(1L);
    Assert.assertEquals(woPriorityDTOS.size(), woPriorityDTOS1.size());
  }

  @Test
  public void getListWoChild_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1D);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(woRepository.getListWoChild(any())).thenReturn(datatable);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setStartTime(new Date());
    woInsideDTO.setStartTimeFrom(new Date());
    woInsideDTO.setStartTimeTo(new Date());
    woInsideDTO.setEndTime(new Date());
    woInsideDTO.setEndTimeFrom(new Date());
    woInsideDTO.setEndTimeTo(new Date());
    Datatable datatable1 = woBusiness.getListWoChild(woInsideDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getListWoDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<WoInsideDTO> listIn = Mockito.spy(ArrayList.class);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    listIn.add(woInsideDTO);
    PowerMockito
        .when(woRepository.getListWoDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(listIn);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    List<WoDTO> woDTOS = woBusiness.getListWoDTO(woDTO, 1, 1, "asc", "ID");
    Assert.assertEquals(listIn.size(), woDTOS.size());
  }

  @Test
  public void getResultCallIPCC_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<LogCallIpccDTO> lst = Mockito.spy(ArrayList.class);
    LogCallIpccDTO dto = Mockito.spy(LogCallIpccDTO.class);
    lst.add(dto);
    PowerMockito.when(logCallIpccRepository.getListLogCallIpccByTransactionId(any()))
        .thenReturn(lst);
    PowerMockito.when(logCallIpccRepository.insertLogCallIpcc(any())).thenReturn(resultInSideDto);
    ResultDTO resultDTO = woBusiness
        .getResultCallIPCC("thanhlv12", "123456@", "fixBug; fixBug; fixBug; fixBug; fixBug");
    Assert.assertEquals(resultInSideDto.getKey(), resultDTO.getKey());
  }

  @Test
  public void getListWoByWoTypeId_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<WoInsideDTO> woInsideDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.getListWoByWoTypeId(any())).thenReturn(woInsideDTOS);
    List<WoInsideDTO> woInsideDTOS1 = woBusiness.getListWoByWoTypeId(1L);
    Assert.assertEquals(woInsideDTOS.size(), woInsideDTOS1.size());
  }

  @Test
  public void insertWo_01() throws Exception{
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);

    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Id công việc đã tồn tại");
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setCreatePersonId("999999");
    woDTO.setWoTypeId("1");
    woDTO.setWoId("1");
    woDTO.setCreateDate("10/10/2010 10:10:10");
    woDTO.setFileName("input.txt");
//    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
//    PowerMockito.when(woRepository.findWoByIdNoOffset(any())).thenReturn(woInsideDTO);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(413314L);
    usersEntity.setUserId(999999L);
    usersEntity.setUsername("thanhlv12");
    PowerMockito.when(userRepository.getUserByUserId(any())).thenReturn(usersEntity);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("vtnet");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    List<GnocFileDto> gnocFileGuideDtosOld = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileGuideDto = Mockito.spy(GnocFileDto.class);
    gnocFileGuideDto.setPath(
        "./wo-upload");
    gnocFileGuideDto.setFileName("input.txt");
    gnocFileGuideDtosOld.add(gnocFileGuideDto);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any()))
        .thenReturn(gnocFileGuideDtosOld);
    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(resultInSideDto);
    ResultDTO resultDTO = woBusiness.insertWo(woDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultDTO.getKey());
  }

  @Test
  public void updateWoInfo_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Khách hàng");
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("123");
    woDTO.setIsHot("1");
    woDTO.setNumComplaint("1");
    woDTO.setWoDescription("fixBug");
    woDTO.setCustomerTimeDesireFrom("10/10/2020 10:10:10");
    woDTO.setCustomerTimeDesireTo("10/10/2020 11:10:10");
    woDTO.setEstimateTime("100");
    woDTO.setEndTime("10/10/2021 11:10:10");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(999999L);
    wo.setWoId(1L);
    wo.setWoDescription("fixBug");
    wo.setStatus(4L);
    wo.setCreatePersonId(999999L);
    wo.setWoCode("123");
    wo.setWoTypeId(1L);
    wo.setEndTime(new Date());
    wo.setWoSystem("TT");
    wo.setEstimateTime(new Date());
    PowerMockito.when(woRepository.getWoByWoCode(any())).thenReturn(wo);
    UsersEntity u = Mockito.spy(UsersEntity.class);
    u.setUserLanguage("1");
    u.setUserId(999999L);
    u.setStaffCode("123");
    u.setFullname("Le Van Thanh");
    u.setMobile("0123456789");
    PowerMockito.when(userRepository.getUserByUserId(any())).thenReturn(u);
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    detail.setNumComplaint(1L);
    detail.setAccountIsdn("1a");
    detail.setCustomerName("thanhlv12");
    detail.setCcPriorityCode("1");
    PowerMockito.when(woDetailRepository.findWoDetailById(any())).thenReturn(detail);
    PowerMockito.when(woRepository.getPriorityHot(anyString(), anyString())).thenReturn(1L);
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUsername("thanhlv12");
    PowerMockito.when(userRepository.getUserByUserId(any())).thenReturn(us);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(resultInSideDto);
    List<UsersInsideDto> listUs = Mockito.spy(ArrayList.class);
    UsersInsideDto us1 = Mockito.spy(UsersInsideDto.class);
    us1.setUserId(999999L);
    us1.setUnitId(413314L);
    listUs.add(us1);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUs);
    Map<String, String> mapConfigProperty = Mockito.spy(HashMap.class);
    mapConfigProperty.put(AP_PARAM.WO_TYPE_XLSCVT, "2");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(messagesRepository.insertOrUpdateWfm(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    ResultDTO resultDTO = woBusiness.updateWoInfo(woDTO);
    Assert.assertEquals(resultInSideDto.getKey(), resultDTO.getKey());
  }

  @Test
  public void updateWoInfo_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(DateTimeUtils.class);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Khách hàng");
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoCode("123");
    woDTO.setIsHot("1");
    woDTO.setNumComplaint("1");
    woDTO.setWoDescription("fixBug");
    woDTO.setCustomerTimeDesireFrom("10/10/2020 10:10:10");
    woDTO.setCustomerTimeDesireTo("10/10/2020 11:10:10");
    woDTO.setEstimateTime("100");
    woDTO.setEndTime("10/10/2021 11:10:10");
    woDTO.setIsHot("1");
    woDTO.setNumComplaint("1");
    woDTO.setWoContent("1");
    woDTO.setCustomerTimeDesireFrom("1");
    woDTO.setCustomerTimeDesireTo("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(999999L);
    wo.setWoId(1L);
    wo.setWoDescription("fixBug");
    wo.setStatus(4L);
    wo.setCreatePersonId(999999L);
    wo.setWoCode("123");
    wo.setWoTypeId(1L);
    wo.setEndTime(new Date());
    wo.setWoSystem("SPM");
    wo.setEstimateTime(new Date());
    wo.setStartTime(new Date());
    PowerMockito.when(woRepository.getPriorityHot(anyString(), anyString())).thenReturn(1L);
    PowerMockito.when(woRepository.getWoByWoCode(any())).thenReturn(wo);
    UsersEntity u = Mockito.spy(UsersEntity.class);
    u.setUserLanguage("1");
    u.setUserId(999999L);
    u.setStaffCode("123");
    u.setFullname("Le Van Thanh");
    u.setMobile("0123456789");
    PowerMockito.when(userRepository.getUserByUserId(any())).thenReturn(u);
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    detail.setNumComplaint(1L);
    detail.setAccountIsdn("1a");
    detail.setCustomerName("thanhlv12");
    detail.setCcPriorityCode("1");
    PowerMockito.when(woDetailRepository.findWoDetailById(any())).thenReturn(detail);
    PowerMockito.when(woRepository.getPriorityHot(anyString(), anyString())).thenReturn(1L);
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUsername("thanhlv12");
    PowerMockito.when(userRepository.getUserByUserId(any())).thenReturn(us);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(resultInSideDto);
    List<UsersInsideDto> listUs = Mockito.spy(ArrayList.class);
    UsersInsideDto us1 = Mockito.spy(UsersInsideDto.class);
    us1.setUserId(999999L);
    us1.setUnitId(413314L);
    listUs.add(us1);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUs);
    Map<String, String> mapConfigProperty = Mockito.spy(HashMap.class);
    mapConfigProperty.put(AP_PARAM.WO_TYPE_XLSCVT, "2");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(messagesRepository.insertOrUpdateWfm(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    try {
      ResultDTO resultDTO = woBusiness.updateWoInfo(woDTO);
      Assert.assertEquals(resultInSideDto.getKey(), resultDTO.getKey());
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void pendingWoCommon_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("Mã wo là bắt buộc");
    List<UsersInsideDto> listUs = Mockito.spy(ArrayList.class);
    UsersInsideDto us1 = Mockito.spy(UsersInsideDto.class);
    us1.setUserId(999999L);
    us1.setUnitId(413314L);
    listUs.add(us1);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUs);
    Map<String, String> mapConfigProperty = Mockito.spy(HashMap.class);
    mapConfigProperty.put(AP_PARAM.WO_TYPE_XLSCVT, "1");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(999999L);
    wo.setWoId(1L);
    wo.setWoDescription("fixBug");
    wo.setStatus(4L);
    wo.setCreatePersonId(999999L);
    wo.setWoCode("123");
    wo.setWoTypeId(1L);
    wo.setEndTime(new Date());
    wo.setNumPending(1L);
    PowerMockito.when(woRepository.getWoByWoCode(any())).thenReturn(wo);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unit);
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    updateForm.setSolutionGroupId(1L);
    updateForm.setSolutionGroupName("fixBug");
    updateForm.setPendingType("1");
    updateForm.setEstimateTime("01/10/2020 10:00:00");
    updateForm.setLongitude("1");
    updateForm.setLatitude("1");
    updateForm.setCellService("1");
    updateForm.setSolution("fixBug");
    updateForm.setSolutionGroupId(1L);
    updateForm.setConcaveAreaCode("1");
    updateForm.setPendingType("1");
    String woCode = "123";
    Date endPendingTime = DateUtil.string2DateTime("10/10/9999 10:00:00");
    String user = "thanhlv12";
    String system = "SPM";
    String reasonName = "fixBug";
    String reasonId = "1";
    String customer = "thanhlv12";
    String phone = "0123456789";
    boolean callCC = true;
    boolean checkAllow = true;
    ResultInSideDto resultInSideDto1 = woBusiness
        .pendingWoCommon(updateForm, woCode, endPendingTime, user, system, reasonName, reasonId,
            customer, phone, callCC, checkAllow);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void searchWoKpiCDBR_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<SearchWoKpiCDBRForm> searchWoKpiCDBRForms = Mockito.spy(ArrayList.class);
    PowerMockito.when(woRepository.searchWoKpiCDBR(any(), any())).thenReturn(searchWoKpiCDBRForms);
    List<SearchWoKpiCDBRForm> searchWoKpiCDBRForms1 = woBusiness
        .searchWoKpiCDBR(anyString(), anyString());
    Assert.assertEquals(searchWoKpiCDBRForms.size(), searchWoKpiCDBRForms1.size());
  }

  @Test
  public void createWoTKTU_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoDTO createWoDto = Mockito.spy(WoDTO.class);
    createWoDto.setWoTypeId("1");
    createWoDto.setFtId("1");
    createWoDto.setWoSystem("SPM");
    createWoDto.setSpmCode("1");
    createWoDto.setAccountIsdn("1");
    createWoDto.setLocationCode("1");
    createWoDto.setNationCode("1");
    createWoDto.setWoContent("1");
    createWoDto.setPriorityId("1");
    createWoDto.setStartTime("06/06/2020 09:50:01");
    createWoDto.setEndTime("01/08/2020 09:50:01");
    createWoDto.setCdId("1");
    createWoDto.setCreatePersonId("1");
    createWoDto.setNotGetFileCfgWhenCreate("1");
    createWoDto.setCustomerTimeDesireFrom("1");
    createWoDto.setEndPendingTime("01/08/2020 09:50:01");
    createWoDto.setCustomerPhone("1");
    createWoDto.setCustomerName("1");
    createWoDto.setPortCorrectId("1");
    createWoDto.setErrTypeNims("1");
    createWoDto.setSubscriptionId("1");
    createWoDto.setCustomerGroupType("1");
    createWoDto.setKtrKeyPoint("1");
    createWoDto.setCcServiceId("1");
    createWoDto.setWoTypeCode("1");
    createWoDto.setAccountIsdn("1");
    createWoDto.setErrTypeNims("1");
    createWoDto.setService("1");
    createWoDto.setSubscriberName("1");
    createWoDto.setTechnicalClues("1");
    createWoDto.setIsCcResult("1");
    createWoDto.setCreateDate("1");
    createWoDto.setProductCode("1");
    createWoDto.setStatus("1");
    createWoDto.setWoContent("1");
    createWoDto.setCreateDate("01/08/2020 09:50:01");
    createWoDto.setIsCall(1L);
    createWoDto.setFtId("1");
    createWoDto.setAccountIsdn("1");
    createWoDto.setWoTypeId("1");
    createWoDto.setCcServiceId("1");
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    List<WoTypeInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woTypeInsideDTO);
    SubscriptionInfoForm res = Mockito.spy(SubscriptionInfoForm.class);
    res.setInfraType("GPON");
    res.setInvestType(1L);
    res.setStationDeptCode("1");
    res.setConnectorDeptCode("1");
    MessageForm resQlt = Mockito.spy(MessageForm.class);
    resQlt.setReturnCode("1");
    resQlt.setDetail("1");
    ResultService resQltBccs = Mockito.spy(ResultService.class);
    resQltBccs.setResultCode(1L);
    resQltBccs.setTeamCode("1");
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(1L);
    unit.setUnitName("1");
    unit.setUnitCode("1");
    unit.setIpccServiceId(1L);
    IpccServiceDTO ipccServiceDTO = Mockito.spy(IpccServiceDTO.class);
    ipccServiceDTO.setUrl("/thang");
    ipccServiceDTO.setIpccServiceCode("1");
    List<IpccServiceDTO> ipccServiceDTOList = Mockito.spy(ArrayList.class);
    ipccServiceDTOList.add(ipccServiceDTO);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey("SUCCESS");
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setMobile("1");
    usersInsideDto.setUsername("1");
    usersInsideDto.setUnitId(1L);
    StaffBean staffBean = Mockito.spy(StaffBean.class);
    List<UsersInsideDto> usersInsideDtoList = Mockito.spy(ArrayList.class);
    usersInsideDtoList.add(usersInsideDto);
    PowerMockito.when(catServiceBusiness.getServiceIdByCcServiceId(anyString())).thenReturn(1L);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(userRepository.getUserName(anyLong())).thenReturn("1");
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    PowerMockito.when(woRepository.getCdByUnitId(anyLong(), anyLong())).thenReturn(1L);
    PowerMockito.when(woRepository.getUnitCodeMapNims(anyString(), any())).thenReturn(unit);
    PowerMockito.when(qltPort.getDeptManageConnector(anyLong(), anyString(), any()))
        .thenReturn(resQltBccs);
    PowerMockito.when(cdPort.getUserAssignByAccount(anyString())).thenReturn(resQlt);
    PowerMockito.when(ws_nims_cd_direction.getSubscriptionInfo(any(), any(), any()))
        .thenReturn(res);
    PowerMockito.when(woTypeBusiness.getListWoTypeByLocale(any(), any())).thenReturn(lst);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(result);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(result);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(result);
    PowerMockito.when(woWorklogRepository.insertWoWorklog(any())).thenReturn(result);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(usersInsideDtoList);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unit);
    PowerMockito.when(smsGatewayRepository.getListIpccServiceDTO(any()))
        .thenReturn(ipccServiceDTOList);
    PowerMockito.when(paymentPort.getStaffByIsdn(any())).thenReturn(staffBean);
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    ResultDTO result1 = woBusiness.createWoTKTU(createWoDto);
    Assert.assertEquals(result1.getKey(), RESULT.FAIL);
  }

  @Test
  public void createWoFollowNode_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoDTO createWoDto = Mockito.spy(WoDTO.class);
//    createWoDto.setWoTypeId("1");
//    createWoDto.setIsSendFT("1");
//    createWoDto.setLocationCode("1");
    List<String> listNode = Mockito.spy(ArrayList.class);
    Map<String, String> mapConfigProperty = Mockito.spy(Map.class);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    ResultDTO resultDTO = woBusiness.createWoFollowNode(createWoDto, listNode);
    Assert.assertEquals(resultDTO.getKey(), "FAIL");

  }

  @Test
  public void createWoFollowNode_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoDTO createWoDto = Mockito.spy(WoDTO.class);
    createWoDto.setWoTypeId("WO_TYPE_AMI_ONE");
    createWoDto.setIsSendFT("1");
    createWoDto.setLocationCode("1");
    createWoDto.setUnitCode("1");
    List<String> listNode = Mockito.spy(ArrayList.class);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO_TYPE_AMI_ONE", "1");
    WoCdGroupInsideDTO cdGroup = Mockito.spy(WoCdGroupInsideDTO.class);
    PowerMockito.when(woRepository.getCdByUnitCode(anyString(), anyLong(), anyString()))
        .thenReturn(cdGroup);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    ResultDTO resultDTO = woBusiness.createWoFollowNode(createWoDto, listNode);
    Assert.assertEquals(resultDTO.getKey(), "FAIL");

  }


  @Test
  public void createWoFollowNode_03() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoDTO createWoDto = Mockito.spy(WoDTO.class);
    createWoDto.setWoTypeId("1");
    createWoDto.setIsSendFT("1");
    createWoDto.setLocationCode("1");
    createWoDto.setUnitCode("1");
    createWoDto.setWoContent("1");
    createWoDto.setWoSystem("1");
    createWoDto.setWoTypeId("1");
    createWoDto.setPriorityId("1");
    createWoDto.setStartTime("06/06/2050 09:50:01");
    createWoDto.setEndTime("01/08/2050 09:50:01");
    createWoDto.setCdId("1");
    createWoDto.setCreatePersonId("1");
    createWoDto.setNotGetFileCfgWhenCreate("1");
    createWoDto.setCustomerTimeDesireFrom("1");
    createWoDto.setEndPendingTime("01/08/2020 09:50:01");
    createWoDto.setCustomerPhone("1");
    createWoDto.setCustomerName("1");
    createWoDto.setPortCorrectId("1");
    createWoDto.setErrTypeNims("1");
    createWoDto.setSubscriptionId("1");
    createWoDto.setCustomerGroupType("1");
    createWoDto.setKtrKeyPoint("1");
    createWoDto.setCcServiceId("1");
    createWoDto.setWoTypeCode("1");
    createWoDto.setAccountIsdn("1");
    createWoDto.setErrTypeNims("1");
    createWoDto.setService("1");
    createWoDto.setSubscriberName("1");
    createWoDto.setTechnicalClues("1");
    createWoDto.setIsCcResult("1");
    createWoDto.setCreateDate("1");
    createWoDto.setProductCode("1");
    createWoDto.setStatus("1");
    createWoDto.setWoContent("1");
    createWoDto.setCreateDate("01/08/2020 09:50:01");
    createWoDto.setIsCall(1L);
    createWoDto.setFtId("1");
    createWoDto.setAccountIsdn("1");
    createWoDto.setWoTypeId("1");
    createWoDto.setCcServiceId("1");
    List<String> listNode = Mockito.spy(ArrayList.class);
    listNode.add("1");
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO_TYPE_AMI_ONE", "1,1");
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    WoCdGroupInsideDTO cdGroup = Mockito.spy(WoCdGroupInsideDTO.class);
    PowerMockito.when(woRepository.getCdByUnitCode(anyString(), anyLong(), anyString()))
        .thenReturn(cdGroup);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(userRepository.getUserName(anyLong())).thenReturn("1");
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");

    ResultDTO resultDTO = null;
    try {
      resultDTO = woBusiness.createWoFollowNode(createWoDto, listNode);
      Assert.assertEquals(resultDTO.getKey(), "FAIL");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void createWoFollowNode_04() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoDTO createWoDto = Mockito.spy(WoDTO.class);
    createWoDto.setWoTypeId("1");
    createWoDto.setIsSendFT("");
    createWoDto.setLocationCode("1");
    createWoDto.setUnitCode("1");
    createWoDto.setWoContent("1");
    createWoDto.setWoSystem("1");
    createWoDto.setWoTypeId("1");
    createWoDto.setPriorityId("1");
    createWoDto.setStartTime("06/06/2050 09:50:01");
    createWoDto.setEndTime("01/08/2050 09:50:01");
    createWoDto.setCdId("1");
    createWoDto.setCreatePersonId("1");
    createWoDto.setNotGetFileCfgWhenCreate("1");
    createWoDto.setCustomerTimeDesireFrom("1");
    createWoDto.setEndPendingTime("01/08/2020 09:50:01");
    createWoDto.setCustomerPhone("1");
    createWoDto.setCustomerName("1");
    createWoDto.setPortCorrectId("1");
    createWoDto.setErrTypeNims("1");
    createWoDto.setSubscriptionId("1");
    createWoDto.setCustomerGroupType("1");
    createWoDto.setKtrKeyPoint("1");
    createWoDto.setCcServiceId("1");
    createWoDto.setWoTypeCode("1");
    createWoDto.setAccountIsdn("1");
    createWoDto.setErrTypeNims("1");
    createWoDto.setService("1");
    createWoDto.setSubscriberName("1");
    createWoDto.setTechnicalClues("1");
    createWoDto.setIsCcResult("1");
    createWoDto.setCreateDate("1");
    createWoDto.setProductCode("1");
    createWoDto.setStatus("1");
    createWoDto.setWoContent("1");
    createWoDto.setCreateDate("01/08/2020 09:50:01");
    createWoDto.setIsCall(1L);
    createWoDto.setFtId("1");
    createWoDto.setAccountIsdn("1");
    createWoDto.setWoTypeId("1");
    createWoDto.setCcServiceId("1");
    List<String> listNode = Mockito.spy(ArrayList.class);
    listNode.add("1");
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO_TYPE_AMI_ONE", "1,1");
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    WoCdGroupInsideDTO cdGroup = Mockito.spy(WoCdGroupInsideDTO.class);
    SysUsersBO sysUsersBO = Mockito.spy(SysUsersBO.class);
    sysUsersBO.setUsername("1");
    List<SysUsersBO> lstU = Mockito.spy(ArrayList.class);
    lstU.add(sysUsersBO);
    PowerMockito.when(woRepository.getCdByUnitCode(anyString(), anyLong(), anyString()))
        .thenReturn(cdGroup);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(userRepository.getUserName(anyLong())).thenReturn("1");
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    PowerMockito.when(cdPort.getListUserByLocation(anyString())).thenReturn(lstU);

    ResultDTO resultDTO = null;
    try {
      resultDTO = woBusiness.createWoFollowNode(createWoDto, listNode);
      Assert.assertEquals(resultDTO.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void getWoDetail_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woDetail = Mockito.spy(WoInsideDTO.class);
    woDetail.setCdId(1L);
    woDetail.setStatus(6L);
    woDetail.setCreateDate(new Date());
    woDetail.setFileName("1");
    List<WoInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(woDetail);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    List<UsersInsideDto> listUserInCd = Mockito.spy(ArrayList.class);
    listUserInCd.add(usersInsideDto);
    PowerMockito.when(woRepository.getListDataSearch1(any())).thenReturn(list);
    PowerMockito.when(woCategoryServiceProxy.getListCdByGroup(anyLong())).thenReturn(listUserInCd);
    PowerMockito.when(woRepository.getListDataSearch1(any())).thenReturn(list);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1.1);
    ObjResponse objResponse = woBusiness.getWoDetail("1", "1");
    Assert.assertEquals(objResponse.getListFile().size(), 1);
  }

  @Test
  public void getListReasonOverdue_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CompCause compCause = Mockito.spy(CompCause.class);
    List<CompCause> lst = Mockito.spy(ArrayList.class);
    lst.add(compCause);
    LanguageExchangeDTO languageExchangeDTO = Mockito.spy(LanguageExchangeDTO.class);
    languageExchangeDTO.setBussinessId(1L);
    languageExchangeDTO.setLeeValue("1");
    List<LanguageExchangeDTO> lstLanguage = Mockito.spy(ArrayList.class);
    lstLanguage.add(languageExchangeDTO);
    PowerMockito.when(languageExchangeRepository.findBySql(anyString(), anyMap(), anyMap(), any()))
        .thenReturn(lstLanguage);
    PowerMockito.when(ws_cc_direction.getListReasonOverdue(anyLong(), anyString())).thenReturn(lst);
    List<CompCause> lst1 = woBusiness.getListReasonOverdue(1L, "1");
    Assert.assertEquals(lst1.size(), lst.size());
  }

  @Test
  public void setDataWoDTOUpdate_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoInsideDTO woInsideDTOUpdate = Mockito.spy(WoInsideDTO.class);
    woInsideDTOUpdate.toModelOutSide();
    woBusiness.setDataWoDTOUpdate(woInsideDTOUpdate, woInsideDTOUpdate);
  }

  @Test
  public void doExportFileTestService_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoInsideDTO woInsideDTOUpdate = Mockito.spy(WoInsideDTO.class);
    List<String> headersFileTest = Mockito.spy(ArrayList.class);
    List<List<String>> data = Mockito.spy(ArrayList.class);
    List<ExcelSheetDTO> lstDataSheetAll = Mockito.spy(ArrayList.class);
    File file = woBusiness.exportFileResult(headersFileTest, data, lstDataSheetAll);
    Assert.assertNotNull(file);
  }

  @Test
  public void exportDataWoFromListCr_02() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils
        .saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = woBusiness.exportDataWoFromListCr(firstFile, new Date(), new Date());
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void exportDataWoFromListCr_03() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1"};
    Object[] header1 = new Object[]{"1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    for (int i = 0; i < 600; i++) {
      headerList1.add(header1);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        2,//begin row
        0,//from column
        1,//to column
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        3,//begin row
        0,//from column
        1,//to column
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woBusiness.exportDataWoFromListCr(firstFile, new Date(), new Date());
    assertEquals(result.getKey(), "DATA_OVER");
  }

  @Test
  public void exportDataWoFromListCr_04() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1"};
    Object[] header1 = new Object[]{"1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    headerList1.add(header1);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TEST.SERVICE", "1");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    List<WoInsideDTO> listSearch = Mockito.spy(ArrayList.class);
    listSearch.add(woInsideDTO);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        2,//begin row
        0,//from column
        1,//to column
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,//sheet
        3,//begin row
        0,//from column
        1,//to column
        1000
    )).thenReturn(headerList1);
    PowerMockito.when(woRepository.getListDataSearchWoDTO(any())).thenReturn(listSearch);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    ResultInSideDto result = woBusiness.exportDataWoFromListCr(firstFile, new Date(), new Date());
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void aprovePXK_01() throws Exception {
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(1L);
    wo.setCdId(1L);
    wo.setCreatePersonId(1L);
    wo.setFileName("1");
    wo.setWoCode("1");
    wo.setWoContent("1");
    wo.setWoId(1L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setLocationId(1L);
    Kttsbo resultKtts = Mockito.spy(Kttsbo.class);
    resultKtts.setStatus("Success");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(commonRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(woRepository.findWoByIdNoOffset(any())).thenReturn(wo);
    PowerMockito.when(kttsVsmartPort
        .updateWareExpNoteNation(anyLong(), anyLong(), anyString(), anyLong(), anyString()))
        .thenReturn(resultKtts);
    ResultDTO resultDTO = woBusiness.aprovePXK(1L, 2L, "1", 1L);
    assertEquals(resultDTO.getKey(), "SUCCESS");
  }

  @Test
  public void createWoVsmart_01() {
    WoDTO createWoDto = Mockito.spy(WoDTO.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    createWoDto.setWoTypeId("WO_TYPE_KBDV_QLCTKT");
    createWoDto.setCreatePersonId("1");
    createWoDto.setCreatePersonName("1");
    createWoDto.setUnitCode("1");
    createWoDto.setCdId("1");
    createWoDto.setFtName("1");
    createWoDto.setWoContent("1");
    createWoDto.setWoSystem("1");
    createWoDto.setWoTypeId("1");
    createWoDto.setPriorityId("1");
    createWoDto.setStartTime("06/06/2050 09:50:01");
    createWoDto.setEndTime("01/08/2050 09:50:01");
    createWoDto.setCdId("1");
    createWoDto.setCreatePersonId("1");
    createWoDto.setNotGetFileCfgWhenCreate("1");
    createWoDto.setCustomerTimeDesireFrom("1");
    createWoDto.setEndPendingTime("01/08/2020 09:50:01");
    createWoDto.setCustomerPhone("1");
    createWoDto.setCustomerName("1");
    createWoDto.setPortCorrectId("1");
    createWoDto.setErrTypeNims("1");
    createWoDto.setSubscriptionId("1");
    createWoDto.setCustomerGroupType("1");
    createWoDto.setKtrKeyPoint("1");
    createWoDto.setCcServiceId("1");
    createWoDto.setWoTypeCode("1");
    createWoDto.setAccountIsdn("1");
    createWoDto.setErrTypeNims("1");
    createWoDto.setService("1");
    createWoDto.setSubscriberName("1");
    createWoDto.setTechnicalClues("1");
    createWoDto.setIsCcResult("1");
    createWoDto.setCreateDate("1");
    createWoDto.setProductCode("1");
    createWoDto.setStatus("1");
    createWoDto.setWoContent("1");
    createWoDto.setCreateDate("01/08/2020 09:50:01");
    createWoDto.setIsCall(1L);
    createWoDto.setFtId("1");
    createWoDto.setAccountIsdn("1");
    createWoDto.setWoTypeId("1");
    createWoDto.setCcServiceId("1");
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO_TYPE_BHHT", "1");
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    List<WoTypeInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woTypeInsideDTO);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    List<CatItemDTO> listCatItem = Mockito.spy(ArrayList.class);
    listCatItem.add(catItemDTO);
    WoCdGroupInsideDTO woCdGroup = Mockito.spy(WoCdGroupInsideDTO.class);
    PowerMockito.when(woCdGroupRepository.getWoCdGroupWoByCdGroupCode(anyString()))
        .thenReturn(woCdGroup);
    PowerMockito.when(catItemRepository.getListItemByCategory(anyString(), anyString()))
        .thenReturn(listCatItem);
    PowerMockito.when(woCategoryServiceProxy.getListWoTypeByLocaleNotLike(any())).thenReturn(lst);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    ResultDTO resultDTO = woBusiness.createWoVsmart(createWoDto);
    assertEquals(resultDTO.getKey(), "FAIL");
  }

  @Test
  public void cancelReqBccs_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setCreatePersonId(1L);
    wo.setFtId(1L);
    UsersEntity us = Mockito.spy(UsersEntity.class);
    PowerMockito.when(commonRepository.getUserByUserId(anyLong())).thenReturn(us);
    PowerMockito.when(woRepository.getWoByWoSystemCode(anyString())).thenReturn(wo);
    ResultDTO resultDTO = woBusiness.cancelReqBccs("1", "1");
    assertEquals(resultDTO.getKey(), "SUCCESS");
  }

  @Test
  public void callIPCCWithName_01() {
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoCode("1");
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setMobile("1");
    us.setUsername("1");
    us.setUnitId(1L);
    UnitDTO un = Mockito.spy(UnitDTO.class);
    un.setIpccServiceId(1L);
    IpccServiceDTO ipccServiceDTO = Mockito.spy(IpccServiceDTO.class);
    ipccServiceDTO.setUrl("1");
    List<IpccServiceDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(ipccServiceDTO);
    PowerMockito.when(smsGatewayRepository.getListIpccServiceDTO(any())).thenReturn(lst);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(un);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(us);
    ResultDTO resultDTO = woBusiness.callIPCCWithName("1", 1L, "1", "1");
    assertEquals(resultDTO.getKey(), "SUCCESS");
  }

  @Test
  public void deleteWOForRollback_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1L);
    List<WoInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woInsideDTO);
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey("SUCCESS");
    PowerMockito.when(woRepository.getListWoByWoCode(any())).thenReturn(lst);
    PowerMockito.when(userRepository.getOffsetFromUser(anyLong())).thenReturn(1.1);
    PowerMockito.when(woRepository.deleteWo(anyLong())).thenReturn(result);
    ResultDTO result1 = woBusiness.deleteWOForRollback("1", "1", "1");
    assertEquals(result1.getKey(), "SUCCESS");
  }


  @Test
  public void updateStatusCommon_01() throws Exception {
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    PowerMockito.mockStatic(BaseRepository.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO = Mockito
        .spy(WoMaterialDeducteInsideDTO.class);
    List<WoMaterialDeducteInsideDTO> listMaterial = Mockito.spy(ArrayList.class);
    listMaterial.add(woMaterialDeducteInsideDTO);
    WoMerchandiseInsideDTO woMerchandiseInsideDTO = Mockito.spy(WoMerchandiseInsideDTO.class);
    List<WoMerchandiseInsideDTO> lstMerchandise = Mockito.spy(ArrayList.class);
    lstMerchandise.add(woMerchandiseInsideDTO);
    List<String> listFileName = Mockito.spy(ArrayList.class);
    listFileName.add("1");
    List<byte[]> fileArr = Mockito.spy(ArrayList.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    listUser.add(usersInsideDto);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(1L);
    wo.setStatus(4L);
    wo.setEndTime(DateTimeUtils.convertStringToDate("03/03/2020 05:05:41"));
    wo.setWoTypeId(1L);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.XLSCVT", "2,2");
    ResultInSideDto rsDedute = Mockito.spy(ResultInSideDto.class);
    rsDedute.setKey("SUCCESS");

    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducte(any())).thenReturn(rsDedute);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    ResultInSideDto result = woBusiness
        .updateStatusCommon(updateForm, "1", "1", "5", "1", "1", "1", listMaterial, 1L, 1L, 1L,
            lstMerchandise, "1", "1", "1", "1", listFileName, fileArr);
    assertEquals(result.getKey(), "OK");

  }

  @Test
  public void updateWoKTTS_01() throws Exception {
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setWoTypeId(1L);
    wo.setCdAssignId(1L);
    UsersInsideDto ft = Mockito.spy(UsersInsideDto.class);
    ft.setUnitId(1L);
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    WoMerchandiseInsideDTO woMerchandiseInsideDTO = Mockito.spy(WoMerchandiseInsideDTO.class);
    woMerchandiseInsideDTO.setMerchandiseCode("1");
    woMerchandiseInsideDTO.setQuantity(1.1);
    woMerchandiseInsideDTO.setLongTermAssetId(1L);
    List<WoMerchandiseInsideDTO> lstMerchandise = Mockito.spy(ArrayList.class);
    lstMerchandise.add(woMerchandiseInsideDTO);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC", "1,1");
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setStaffCode("1");
    Kttsbo kttsBo = Mockito.spy(Kttsbo.class);
    kttsBo.setStatus("Success_Closed");
    PowerMockito.when(kttsVsmartPort
        .updateAcceptanceUCTTNation(any(), any(), any(), any(), any(),
            any(), any())).thenReturn(kttsBo);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(us);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any()))
        .thenReturn(lstMerchandise);
    ResultDTO result = woBusiness
        .updateWoKTTS(wo, ft, 1L, updateForm, 1L, lstMerchandise, "1", "1", mapConfigProperty, 1L);
    assertEquals(result.getKey(), "OK");
  }

  @Test
  public void updateWoKTTS_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setWoTypeId(2L);
    wo.setCdAssignId(1L);
    wo.setFtId(1L);
    UsersInsideDto ft = Mockito.spy(UsersInsideDto.class);
    ft.setUnitId(1L);
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    WoMerchandiseInsideDTO woMerchandiseInsideDTO = Mockito.spy(WoMerchandiseInsideDTO.class);
    woMerchandiseInsideDTO.setMerchandiseCode("1");
    woMerchandiseInsideDTO.setQuantity(1.1);
    woMerchandiseInsideDTO.setLongTermAssetId(1L);
    List<WoMerchandiseInsideDTO> lstMerchandise = Mockito.spy(ArrayList.class);
    lstMerchandise.add(woMerchandiseInsideDTO);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC", "1,1");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC.THUE", "1,1");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC.UCTT", "2,2");
    mapConfigProperty.put("WO_TYPE_THUHOI_CAP_NIMS", "3.3");
    mapConfigProperty.put("WO_TYPE_DO_XANG_DAU", "3.3");
    mapConfigProperty.put("WO_TYPE_BDNT", "3.3");
    mapConfigProperty.put("CAMERA_CC_SERVICE_ID", "3.3");

    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setStaffCode("1");
    Kttsbo kttsBo = Mockito.spy(Kttsbo.class);
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    PowerMockito.when(kttsVsmartPort
        .updateAcceptanceUCTTNation(anyLong(), anyString(), anyString(), anyString(), any(),
            anyLong(), anyString())).thenReturn(kttsBo);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(us);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any()))
        .thenReturn(lstMerchandise);
    ResultDTO result = woBusiness
        .updateWoKTTS(wo, ft, 1L, updateForm, 1L, lstMerchandise, "1", "1", mapConfigProperty, 1L);
    assertEquals(result.getKey(), null);
  }

  @Test
  public void updateWoKTTS_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setWoTypeId(2L);
    wo.setCdAssignId(1L);
    wo.setFtId(1L);
    wo.setCdId(1L);
    wo.setLineCode("1");
    wo.setCreatePersonId(1L);
    wo.setCdAssignId(1L);
    wo.setStationCode("1");
    wo.setWoSystem("SPM");
    wo.setWoContent("1");
    UsersInsideDto ft = Mockito.spy(UsersInsideDto.class);
    ft.setUnitId(1L);
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    WoMerchandiseInsideDTO woMerchandiseInsideDTO = Mockito.spy(WoMerchandiseInsideDTO.class);
    woMerchandiseInsideDTO.setMerchandiseCode("1");
    woMerchandiseInsideDTO.setQuantity(1.1);
    woMerchandiseInsideDTO.setLongTermAssetId(1L);
    List<WoMerchandiseInsideDTO> lstMerchandise = Mockito.spy(ArrayList.class);
    lstMerchandise.add(woMerchandiseInsideDTO);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC", "1,1");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC.THUE", "2,2");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC.UCTT", "3,3");
    mapConfigProperty.put("WO_TYPE_BDNT", "2,2");

    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setStaffCode("1");
    Kttsbo kttsBo = Mockito.spy(Kttsbo.class);
    kttsBo.setStatus("Success");
    SubscriptionInfoForm res = Mockito.spy(SubscriptionInfoForm.class);
    res.setInfraType("GPON");
    res.setInvestType(2L);
    res.setConnectorDeptCode("1");
    res.setStationCode("1");
    MessageForm resQlt = Mockito.spy(MessageForm.class);
    resQlt.setDetail("1");
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(1L);
    unit.setUnitCode("1");
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey("SUCCESS");
    Kttsbo result11 = Mockito.spy(Kttsbo.class);
    PowerMockito.when(kttsVsmartPort
        .confirmAssetMoveDeliveryNation(anyLong(), any(), anyString(), anyString(), anyString()))
        .thenReturn(result11);
    PowerMockito.when(woMerchandiseRepository.updateListWoMerchandise(any())).thenReturn(result);
    PowerMockito.when(woWorklogRepository.insertWoWorklog(any())).thenReturn(result);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(result);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(result);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(result);
    PowerMockito.when(userRepository.getUserName(anyLong())).thenReturn("1");
    PowerMockito.when(woRepository.getUnitCodeMapNims(anyString(), any())).thenReturn(unit);
    PowerMockito.when(cdPort.getUserAssignByAccount(any())).thenReturn(resQlt);
    PowerMockito.when(ws_nims_cd_direction.getSubscriptionInfo(any(), any(), any()))
        .thenReturn(res);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    PowerMockito.when(kttsVsmartPort
        .updateAcceptanceUCTTNation(any(), any(), any(), any(), any(),
            any(), any())).thenReturn(kttsBo);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(us);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any()))
        .thenReturn(lstMerchandise);
    ResultDTO result1 = woBusiness
        .updateWoKTTS(wo, ft, 1L, updateForm, 1L, lstMerchandise, "1", "1", mapConfigProperty, 1L);
    assertEquals(result1.getKey(), "OK");


  }

  @Test
  public void updateWoKTTS_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setWoTypeId(2L);
    wo.setCdAssignId(1L);
    wo.setFtId(1L);
    wo.setCdId(1L);
    wo.setLineCode("1");
    wo.setCreatePersonId(1L);
    wo.setCdAssignId(1L);
    wo.setStationCode("1");
    wo.setWoSystem("SPM");
    wo.setWoContent("1");
    wo.setWarehouseCode("1");
    UsersInsideDto ft = Mockito.spy(UsersInsideDto.class);
    ft.setUnitId(1L);
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    WoMerchandiseInsideDTO woMerchandiseInsideDTO = Mockito.spy(WoMerchandiseInsideDTO.class);
    woMerchandiseInsideDTO.setMerchandiseCode("1");
    woMerchandiseInsideDTO.setQuantity(1.1);
    woMerchandiseInsideDTO.setLongTermAssetId(1L);
    List<WoMerchandiseInsideDTO> lstMerchandise = Mockito.spy(ArrayList.class);
    lstMerchandise.add(woMerchandiseInsideDTO);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC", "1,1");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC.THUE", "3,3");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC.UCTT", "3,3");
    mapConfigProperty.put("WO_TYPE_BDNT", "2,2");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.THUHOI", "2,2");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.HC", "2,2");

    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setStaffCode("1");
    Kttsbo kttsBo = Mockito.spy(Kttsbo.class);
    SubscriptionInfoForm res = Mockito.spy(SubscriptionInfoForm.class);
    res.setInfraType("GPON");
    res.setInvestType(2L);
    res.setConnectorDeptCode("1");
    res.setStationCode("1");
    MessageForm resQlt = Mockito.spy(MessageForm.class);
    resQlt.setDetail("1");
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(1L);
    unit.setUnitCode("1");
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey("SUCCESS");
    Kttsbo result11 = Mockito.spy(Kttsbo.class);
    result11.setStatus("Success");
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    PowerMockito.when(kttsVsmartPort
        .createImpReqNation(anyString(), anyString(), any(), any(), any(), anyString()))
        .thenReturn(result11);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(kttsVsmartPort
        .confirmAssetMoveDeliveryNation(anyLong(), any(), anyString(), anyString(), anyString()))
        .thenReturn(result11);
    PowerMockito.when(woMerchandiseRepository.updateListWoMerchandise(any())).thenReturn(result);
    PowerMockito.when(woWorklogRepository.insertWoWorklog(any())).thenReturn(result);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(result);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(result);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(result);
    PowerMockito.when(userRepository.getUserName(anyLong())).thenReturn("1");
    PowerMockito.when(woRepository.getUnitCodeMapNims(anyString(), any())).thenReturn(unit);
    PowerMockito.when(cdPort.getUserAssignByAccount(any())).thenReturn(resQlt);
    PowerMockito.when(ws_nims_cd_direction.getSubscriptionInfo(any(), any(), any()))
        .thenReturn(res);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    PowerMockito.when(kttsVsmartPort
        .updateAcceptanceUCTTNation(anyLong(), anyString(), anyString(), anyString(), any(),
            anyLong(), anyString())).thenReturn(kttsBo);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(us);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any()))
        .thenReturn(lstMerchandise);
    ResultDTO result1 = woBusiness
        .updateWoKTTS(wo, ft, 1L, updateForm, 1L, lstMerchandise, "1", "1", mapConfigProperty, 1L);
    assertEquals(result1.getKey(), "OK");


  }

  @Test
  public void updateWoKTTS_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setWoTypeId(2L);
    wo.setCdAssignId(1L);
    wo.setFtId(1L);
    wo.setCdId(1L);
    wo.setLineCode("1");
    wo.setCreatePersonId(1L);
    wo.setCdAssignId(1L);
    wo.setStationCode("1");
    wo.setWoSystem("SPM");
    wo.setWoContent("1");
    wo.setWarehouseCode("1");
    UsersInsideDto ft = Mockito.spy(UsersInsideDto.class);
    ft.setUnitId(1L);
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    WoMerchandiseInsideDTO woMerchandiseInsideDTO = Mockito.spy(WoMerchandiseInsideDTO.class);
    woMerchandiseInsideDTO.setMerchandiseCode("1");
    woMerchandiseInsideDTO.setQuantity(1.1);
    woMerchandiseInsideDTO.setLongTermAssetId(1L);
    List<WoMerchandiseInsideDTO> lstMerchandise = Mockito.spy(ArrayList.class);
    lstMerchandise.add(woMerchandiseInsideDTO);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC", "1,1");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC.THUE", "3,3");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC.UCTT", "3,3");
    mapConfigProperty.put("WO_TYPE_BDNT", "2,2");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.THUHOI", "3,3");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.HC", "3,3");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.DC.NC", "2,2");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.DC.HC", "1,1");

    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setStaffCode("1");
    Kttsbo kttsBo = Mockito.spy(Kttsbo.class);
    SubscriptionInfoForm res = Mockito.spy(SubscriptionInfoForm.class);
    res.setInfraType("GPON");
    res.setInvestType(2L);
    res.setConnectorDeptCode("1");
    res.setStationCode("1");
    MessageForm resQlt = Mockito.spy(MessageForm.class);
    resQlt.setDetail("1");
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(1L);
    unit.setUnitCode("1");
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey("SUCCESS");
    Kttsbo result11 = Mockito.spy(Kttsbo.class);
    result11.setStatus("Success");
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoTypeId(1L);
    woInsideDTO.setStatus(1L);
    List<WoInsideDTO> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woInsideDTO);
    PowerMockito.when(kttsVsmartPort.rejectAssetMoveConfirmNation(any(), anyLong(), any(), any()))
        .thenReturn(result11);
    PowerMockito.when(woRepository.getListWoBySystemOtherCode(any(), any())).thenReturn(lstWo);
    PowerMockito.when(kttsVsmartPort.confirmAssetMoveRecvNation(anyLong(), any(), anyString()))
        .thenReturn(result11);
    PowerMockito.when(kttsVsmartPort
        .createImpReqNation(anyString(), anyString(), any(), any(), any(), anyString()))
        .thenReturn(result11);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(kttsVsmartPort
        .confirmAssetMoveDeliveryNation(anyLong(), any(), anyString(), anyString(), anyString()))
        .thenReturn(result11);
    PowerMockito.when(woMerchandiseRepository.updateListWoMerchandise(any())).thenReturn(result);
    PowerMockito.when(woWorklogRepository.insertWoWorklog(any())).thenReturn(result);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(result);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(result);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(result);
    PowerMockito.when(userRepository.getUserName(anyLong())).thenReturn("1");
    PowerMockito.when(woRepository.getUnitCodeMapNims(anyString(), any())).thenReturn(unit);
    PowerMockito.when(cdPort.getUserAssignByAccount(any())).thenReturn(resQlt);
    PowerMockito.when(ws_nims_cd_direction.getSubscriptionInfo(any(), any(), any()))
        .thenReturn(res);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    PowerMockito.when(kttsVsmartPort
        .updateAcceptanceUCTTNation(anyLong(), anyString(), anyString(), anyString(), any(),
            anyLong(), anyString())).thenReturn(kttsBo);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(us);
    PowerMockito.when(woMerchandiseRepository.getListWoMerchandiseDTO(any()))
        .thenReturn(lstMerchandise);
    ResultDTO result1 = woBusiness
        .updateWoKTTS(wo, ft, 1L, updateForm, 2L, lstMerchandise, "1", "1", mapConfigProperty, 1L);
    assertEquals(result1.getKey(), "OK");
  }

  @Test
  public void updateMopInfo_01() {
    woBusiness.updateMopInfo("1", "1", "1", 1L);
  }

  @Test
  public void updateMopInfo_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    PowerMockito.when(woRepository.getWoByWoCode(any())).thenReturn(wo);
    woBusiness.updateMopInfo("1", "1", "1", 1L);
  }

  @Test
  public void updateMopInfo_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setFtId(1L);
    WoMopInfoEntity woMopInfoEntity = Mockito.spy(WoMopInfoEntity.class);
    woMopInfoEntity.setResult("1");
    List<WoMopInfoEntity> lst = Mockito.spy(ArrayList.class);
    lst.add(woMopInfoEntity);
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUserId(1L);
    us.setUsername("1");
    ResultInSideDto resWorklog = Mockito.spy(ResultInSideDto.class);
    resWorklog.setKey("SUCCESS");
    PowerMockito.when(woWorklogRepository.insertWoWorklog(any())).thenReturn(resWorklog);
    PowerMockito.when(woRepository.getWoByWoCode(any())).thenReturn(wo);
    PowerMockito.when(commonRepository.getUserByUserId(any())).thenReturn(us);
    PowerMockito.when(woMopInfoRepository.getWoMopInfoByWoIdAndMopId(any())).thenReturn(lst);
    woBusiness.updateMopInfo("1", "1", "1", 2L);
  }

  @Test
  public void createListWoVsmart_01() {
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<WoDTO> createWoDto = Mockito.spy(ArrayList.class);
    createWoDto.add(woDTO);
    List<ResultDTO> resultDTOS = woBusiness.createListWoVsmart(createWoDto);
    assertEquals(createWoDto.size(), resultDTOS.size());
  }

  @Test
  public void insertWoKTTS_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setFtId("1");
    woDTO.setWoContent("1");
    woDTO.setWoSystem("1");
    woDTO.setWoTypeId("1");
    woDTO.setPriorityId("1");
    woDTO.setStartTime("03/03/2021 02:02:02");
    woDTO.setEndTime("03/03/2022 02:02:02");
    woDTO.setCdId("1");
    woDTO.setCreatePersonId("1");
    woDTO.setWoSystem("1");
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUserId(1L);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    List<WoTypeInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woTypeInsideDTO);
    WoCdGroupInsideDTO woCdGroup = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroup.setWoGroupId(1L);
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey("SUCCESS");
    PowerMockito.when(woMerchandiseRepository.updateListWoMerchandise(any())).thenReturn(result);
    PowerMockito.when(woWorklogRepository.insertWoWorklog(any())).thenReturn(result);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(result);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(result);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(result);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(us);
    PowerMockito.when(userRepository.getUserName(anyLong())).thenReturn("1");
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    PowerMockito.when(woRepository.getCdByFT(anyLong(), anyLong(), anyString()))
        .thenReturn(woCdGroup);
    PowerMockito.when(woCategoryServiceProxy.getListWoTypeByLocaleNotLike(any())).thenReturn(lst);
    PowerMockito.when(userRepository.getUserByUserName(any())).thenReturn(us);
    ResultDTO result1 = woBusiness.insertWoKTTS(woDTO);
    assertEquals(result1.getKey(), "SUCCESS");
  }

  @Test
  public void prepareDataWoKTTS_01() {
    WoInsideDTO woSource = Mockito.spy(WoInsideDTO.class);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoSourceCode("1");
    PowerMockito.when(woRepository.getWoByWoCode(any())).thenReturn(woSource);
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    woBusiness.prepareDataWoKTTS(woDTO);
  }

  @Test
  public void getKpiComplete_01() {
    List<String> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add("1");
    KpiCompleteVsamrtForm kpiCompleteVsamrtForm = Mockito.spy(KpiCompleteVsamrtForm.class);
    List<KpiCompleteVsamrtForm> lstTmp = Mockito.spy(ArrayList.class);
    lstTmp.add(kpiCompleteVsamrtForm);
    PowerMockito.when(woRepository.getListKpiComplete(any(), any(), any())).thenReturn(lstTmp);
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    KpiCompleteVsmartResult result = woBusiness
        .getKpiComplete("03/03/2021 02:02:02", "03/03/2022 02:02:02", lstUser);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void pendingWoForVsmart_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> listUs = Mockito.spy(ArrayList.class);
    listUs.add(usersInsideDto);
    Map<String, String> mapConfigProperty = new HashMap<>();
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUs);
    ResultDTO resultDTO = woBusiness
        .pendingWoForVsmart(updateForm, "1", "05/07/2020 03:02:2020", "1", "1", "1", "1", "1", "1");
    assertEquals(resultDTO.getKey(), "FAIL");

  }

  @Test
  public void getIsCheckQrCode_01() throws Exception {
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoTypeId(1L);
    WoDetailDTO woDetail = Mockito.spy(WoDetailDTO.class);
    woDetail.setServiceId(1L);
    PowerMockito.when(woRepository.findWoByIdNoOffset(1L)).thenReturn(wo);
    PowerMockito.when(woDetailRepository.findWoDetailById(1L)).thenReturn(woDetail);
    woBusiness.getIsCheckQrCode(1L);
  }

  @Test
  public void getCountListFtByUser_01() throws Exception {
    UsersEntity u = Mockito.spy(UsersEntity.class);
    u.setUsername("1");
    u.setUserId(1L);
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(u);
    woBusiness.getCountListFtByUser("1", "1");
  }

  @Test
  public void actionUpdateIsSupportWO_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<String> list = Mockito.spy(ArrayList.class);
    list.add("1");
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    updateForm.setListFileName(list);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(4L);
    wo.setFtId(1L);
    wo.setWoSystem("SPM");
    wo.setWoTypeId(1L);
    wo.setWoCode("1");
    wo.setEndTime(new Date());
    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUnitId(1L);
    UnitDTO un = Mockito.spy(UnitDTO.class);
    ResultInSideDto resWorklog = Mockito.spy(ResultInSideDto.class);
    resWorklog.setMessage("SUCCESS");
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> listUs = Mockito.spy(ArrayList.class);
    listUs.add(usersInsideDto);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.XLSCVT", "1,1");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(userRepository.getListUserDTOByuserName(any())).thenReturn(listUs);
    PowerMockito.when(woRepository.getWoByWoCode(any())).thenReturn(wo);
    PowerMockito.when(commonRepository.getConfigPropertyValue(any())).thenReturn("1");
    PowerMockito.when(woWorklogRepository.insertWoWorklog(any())).thenReturn(resWorklog);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(un);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(us);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    ResultDTO resultDTO = woBusiness.actionUpdateIsSupportWO(updateForm, "1", "1", "1", "1");
    assertEquals(resultDTO.getKey(), "SUCCESS");
  }

  @Test
  public void getWOStatistic_01() {
    woBusiness.getWOStatistic(1L, 1, 1, "05/07/2020 04:58:00", "05/07/2020 04:58:00");
  }

  @Test
  public void getListFtByUser_01() {
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    List<UsersInsideDto> lstUserInfo = Mockito.spy(ArrayList.class);
    lstUserInfo.add(usersInsideDto);
    PowerMockito
        .when(woCdGroupBusiness.getListFtByUser(anyString(), anyString(), anyInt(), anyInt()))
        .thenReturn(lstUserInfo);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(lstUserInfo);
    woBusiness.getListFtByUser("1", "1", 1, 1);
  }

  @Test
  public void insertWoFromWebInMrMNGT_01() throws Exception {
    List<String> list = Mockito.spy(ArrayList.class);
    list.add("1");
    WoKTTSInfoDTO woKTTSInfoDTO = Mockito.spy(WoKTTSInfoDTO.class);
    woKTTSInfoDTO.setContractId(1L);
    WoTestServiceMapDTO woTestServiceMapDTO = Mockito.spy(WoTestServiceMapDTO.class);
    List<WoTestServiceMapDTO> woTestServiceMapDTOList = Mockito.spy(ArrayList.class);
    woTestServiceMapDTOList.add(woTestServiceMapDTO);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setCdIdList(list);
    woInsideDTO.setWoSystem("1");
    woInsideDTO.setFileName("1");
    woInsideDTO.setWoTypeId(1L);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setWoWorklog("1");
    woInsideDTO.setWoKttsInfo(woKTTSInfoDTO);
    woInsideDTO.setLstWoTestService(woTestServiceMapDTOList);
    WoMerchandiseDTO woMerchandiseDTO = Mockito.spy(WoMerchandiseDTO.class);
    List<WoMerchandiseDTO> list1 = Mockito.spy(ArrayList.class);
    list1.add(woMerchandiseDTO);
    woInsideDTO.setLstMerchandise(list1);
    List<String> lstSeq = Mockito.spy(ArrayList.class);
    lstSeq.add("1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);
    resultInSideDto.setKey("SUCCESS");
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setMessage("SUCCESS");
    PowerMockito.when(woWorklogRepository.insertWoWorklog(any())).thenReturn(resultInSideDto);
//    PowerMockito.when(woTestServiceMapService.insertWoTestServiceMap(any())).thenReturn(resultDTO);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.getListSequenseWo("WO_SEQ", 1)).thenReturn(lstSeq);
    ResultInSideDto result = woBusiness.insertWoFromWebInMrMNGT(woInsideDTO);
    assertEquals(result.getKey(), "FAIL");
  }

  @Test
  public void getWOSummaryInfobyUser2_01() {
    woBusiness.getWOSummaryInfobyUser2("1", 1, 1L, "1", "1");
  }

  @Test
  public void runMopOnSAP_01() {
    PowerMockito.mockStatic(WS_SAP_Port.class);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoSystem("CR");
    woInsideDTO.setWoSystemOutId("1");
    ResultInSideDto result = woBusiness.runMopOnSAP(woInsideDTO);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void updateResultTestFromSAP_01() throws Exception {
    List<String> stringList = Mockito.spy(ArrayList.class);
    WoDTO woDto = Mockito.spy(WoDTO.class);
    woDto.setWoCode("1");
    woDto.setListFileName(stringList);
    woDto.setResult("OK");
    woDto.setCdId("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setResult(1L);
    wo.setCdId(1L);
    UsersEntity ft = Mockito.spy(UsersEntity.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    usersInsideDto.setUsername("1");
    usersInsideDto.setStaffCode("1");
    usersInsideDto.setFullname("1");
    usersInsideDto.setMobile("1");
    List<UsersInsideDto> lstU = Mockito.spy(ArrayList.class);
    lstU.add(usersInsideDto);
    PowerMockito.when(woCdBusiness.getListCdByGroup(anyLong())).thenReturn(lstU);
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(ft);
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    ResultDTO resultDTO = woBusiness.updateResultTestFromSAP(woDto);
    assertEquals(resultDTO.getKey(), "SUCCESS");

  }

  @Test
  public void importData_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = woBusiness.importData(null, lstAttachments);
    assertEquals(result.getKey(), "FILE_IS_NULL");

  }

  @Test
  public void importData_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);

    ResultInSideDto result = woBusiness.importData(firstFile, lstAttachments);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");

  }

  @Test
  public void importData_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] header = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        12,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woBusiness.importData(firstFile, lstAttachments);
    assertEquals(result.getKey(), "NODATA");

  }

  @Test
  public void importData_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] header = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    Object[] header1 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    for (int i = 0; i < 3000; i++) {
      headerList1.add(header1);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        12,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        6,
        1,
        12,
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woBusiness.importData(firstFile, lstAttachments);
    assertEquals(result.getKey(), "DATA_OVER");

  }

  @Test
  public void importData_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    lstAttachments.add(firstFile);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] header = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    Object[] header1 = new Object[]{"1", "1", "WFM-FT", "1", "1", "", "1", "1", "1", "1", "1", "1", "1"};

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    headerList1.add(header1);
    WoPriorityDTO woPriorityDTO = Mockito.spy(WoPriorityDTO.class);
    woPriorityDTO.setPriorityName("1");
    woPriorityDTO.setPriorityCode("1");
    List<WoPriorityDTO> list = Mockito.spy(ArrayList.class);
    list.add(woPriorityDTO);
    WoTypeInsideDTO woTypeDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeDTO.setEnableCreate(1L);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        12,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        6,
        1,
        12,
        1000
    )).thenReturn(headerList1);
    PowerMockito.when(woTypeBusiness.getWoTypeByCode(anyString())).thenReturn(woTypeDTO);
    PowerMockito.when(woRepository.getWoPriorityByWoTypeID(anyLong())).thenReturn(list);
    ResultInSideDto result = woBusiness.importData(firstFile, lstAttachments);
    assertEquals(result.getKey(), "SUCCESS");

  }

  @Test
  public void exportFileImport_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<WoDTO> lstExport = Mockito.spy(ArrayList.class);
    File file = woBusiness.exportFileImport(lstExport);
    assertNull(file);
  }

  @Test
  public void insertWoFromWeb_01() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    WoDetailDTO woDeDTO = Mockito.spy(WoDetailDTO.class);
    woDeDTO.setServiceId(1L);
    WoKTTSInfoDTO woKTTSInfoDTO = Mockito.spy(WoKTTSInfoDTO.class);
    woKTTSInfoDTO.setContractId(1L);
    WoTestServiceMapDTO woTestServiceMapDTO = Mockito.spy(WoTestServiceMapDTO.class);
    woTestServiceMapDTO.setWoId("1");
    List<WoTestServiceMapDTO> lstWoTestService = Mockito.spy(ArrayList.class);
    lstWoTestService.add(woTestServiceMapDTO);
    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setStartTime(new Date());
    woInsideDTO.setEndTime(DateTimeUtils.convertStringToDate("03/09/2021 03:03:03"));
    woInsideDTO.setEndTime(new Date());
    woInsideDTO.setCommentComplete("1");
    woInsideDTO.setOtherSystemType("SR");
    woInsideDTO.setStatus(1L);
    woInsideDTO.setWoContent("1");
    woInsideDTO.setFtId(1L);
    woInsideDTO.setWoDetailDTO(woDeDTO);
    woInsideDTO.setWoKttsInfo(woKTTSInfoDTO);
    woInsideDTO.setWoWorklogContent("1");
    woInsideDTO.setLstWoTestService(lstWoTestService);
    woInsideDTO.setCrFilesAttachInsiteDTO(crFilesAttachInsiteDTO);
    List<String> list = Mockito.spy(ArrayList.class);
    list.add("1");
    woInsideDTO.setCdIdList(list);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setMessage("SUCCESS");
    ResultDTO sTest = Mockito.spy(ResultDTO.class);
    sTest.setKey("SUCCESS");
    PowerMockito.when(commonStreamServiceProxy.insertWoTestServiceMap(any())).thenReturn(sTest);
//    PowerMockito.when(woTestServiceMapService.insertWoTestServiceMap(any())).thenReturn(resultDTO);
    PowerMockito.when(woInsideDTO.getListWoTestServiceMap()).thenReturn(lstWoTestService);
    PowerMockito.when(woInsideDTO.getWoKTTSInfoDTO()).thenReturn(woKTTSInfoDTO);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    ResultInSideDto result = woBusiness.insertWoFromWeb(fileAttacks, fileAttacks, woInsideDTO);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void insertWoFromWeb_02() throws Exception {
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setUserID(1L);
    userToken.setUserName("1");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    fileAttacks.add(firstFile);
    WoDetailDTO woDeDTO = Mockito.spy(WoDetailDTO.class);
    woDeDTO.setServiceId(1L);
    WoKTTSInfoDTO woKTTSInfoDTO = Mockito.spy(WoKTTSInfoDTO.class);
    woKTTSInfoDTO.setContractId(1L);
    WoTestServiceMapDTO woTestServiceMapDTO = Mockito.spy(WoTestServiceMapDTO.class);
    woTestServiceMapDTO.setWoId("1");
    List<WoTestServiceMapDTO> lstWoTestService = Mockito.spy(ArrayList.class);
    lstWoTestService.add(woTestServiceMapDTO);
    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1");
    List<CrFilesAttachInsiteDTO> list = Mockito.spy(ArrayList.class);
    list.add(crFilesAttachInsiteDTO);
    crFilesAttachInsiteDTO.setLstCustomerFile(list);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setStartTime(new Date());
    woInsideDTO.setEndTime(DateTimeUtils.convertStringToDate("03/09/2021 03:03:03"));
    woInsideDTO.setEndTime(new Date());
    woInsideDTO.setCommentComplete("1");
    woInsideDTO.setOtherSystemType("SR");
    woInsideDTO.setStatus(1L);
    woInsideDTO.setWoContent("1");
    woInsideDTO.setFtId(1L);
    woInsideDTO.setWoDetailDTO(woDeDTO);
    woInsideDTO.setWoKttsInfo(woKTTSInfoDTO);
    woInsideDTO.setWoWorklogContent("1");
    woInsideDTO.setLstWoTestService(lstWoTestService);
    woInsideDTO.setCrFilesAttachInsiteDTO(crFilesAttachInsiteDTO);
    woInsideDTO.setCreateDate(new Date());
    woInsideDTO.setOtherSystemType("SR");
    woInsideDTO.setOtherSystemId(1L);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setMappingId(1L);
    gnocFileDto.setPath("1");
    gnocFileDto.setMultipartFile(firstFile);
    List<GnocFileDto> list2 = Mockito.spy(ArrayList.class);
    list2.add(gnocFileDto);
    woInsideDTO.setGnocFileCreateWoDtos(list2);
    List<String> list1 = Mockito.spy(ArrayList.class);
    list1.add("1");
    woInsideDTO.setCdIdList(list1);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setMessage("SUCCESS");
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
        "6cf8a37f3e6993f90eef71859b1ebf31", "/ftpFolder", firstFile.getOriginalFilename(),
        firstFile.getBytes(), FileUtils.createDateOfFileName(woInsideDTO.getCreateDate())))
        .thenReturn("trungduong");
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(list2);
//    PowerMockito.when(woTestServiceMapService.insertWoTestServiceMap(any())).thenReturn(resultDTO);
    PowerMockito.when(woInsideDTO.getListWoTestServiceMap()).thenReturn(lstWoTestService);
    PowerMockito.when(woInsideDTO.getWoKTTSInfoDTO()).thenReturn(woKTTSInfoDTO);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    ResultInSideDto result = woBusiness.insertWoFromWeb(fileAttacks, fileAttacks, woInsideDTO);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void insertWoFromWeb_03() throws Exception {
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setUserID(1L);
    userToken.setUserName("1");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    fileAttacks.add(firstFile);
    WoDetailDTO woDeDTO = Mockito.spy(WoDetailDTO.class);
    woDeDTO.setServiceId(1L);
    WoKTTSInfoDTO woKTTSInfoDTO = Mockito.spy(WoKTTSInfoDTO.class);
    woKTTSInfoDTO.setContractId(1L);
    WoTestServiceMapDTO woTestServiceMapDTO = Mockito.spy(WoTestServiceMapDTO.class);
    woTestServiceMapDTO.setWoId("1");
    List<WoTestServiceMapDTO> lstWoTestService = Mockito.spy(ArrayList.class);
    lstWoTestService.add(woTestServiceMapDTO);
    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1");
    List<CrFilesAttachInsiteDTO> list = Mockito.spy(ArrayList.class);
    list.add(crFilesAttachInsiteDTO);
    crFilesAttachInsiteDTO.setLstCustomerFile(list);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setStartTime(new Date());
    woInsideDTO.setEndTime(DateTimeUtils.convertStringToDate("03/09/2021 03:03:03"));
    woInsideDTO.setEndTime(new Date());
    woInsideDTO.setCommentComplete("1");
    woInsideDTO.setOtherSystemType("SR");
    woInsideDTO.setStatus(1L);
    woInsideDTO.setWoContent("1");
    woInsideDTO.setFtId(1L);
    woInsideDTO.setWoDetailDTO(woDeDTO);
    woInsideDTO.setWoKttsInfo(woKTTSInfoDTO);
    woInsideDTO.setWoWorklogContent("1");
    woInsideDTO.setLstWoTestService(lstWoTestService);
    woInsideDTO.setCrFilesAttachInsiteDTO(crFilesAttachInsiteDTO);
    woInsideDTO.setCreateDate(new Date());
    woInsideDTO.setOtherSystemType("SR");
    woInsideDTO.setOtherSystemId(1L);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setMappingId(1L);
    gnocFileDto.setPath("1");
    gnocFileDto.setMultipartFile(firstFile);
    List<GnocFileDto> list2 = Mockito.spy(ArrayList.class);
    list2.add(gnocFileDto);
    woInsideDTO.setGnocFileCreateWoDtos(list2);
    List<String> list1 = Mockito.spy(ArrayList.class);
    list1.add("1");
    woInsideDTO.setCdIdList(list1);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setMessage("SUCCESS");
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
        "6cf8a37f3e6993f90eef71859b1ebf31", "/ftpFolder", firstFile.getOriginalFilename(),
        firstFile.getBytes(), FileUtils.createDateOfFileName(woInsideDTO.getCreateDate())))
        .thenReturn("trungduong");
    ResultDTO sTest = Mockito.spy(ResultDTO.class);
    sTest.setKey("SUCCESS");
    PowerMockito.when(commonStreamServiceProxy.insertWoTestServiceMap(any())).thenReturn(sTest);
//    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(list2);
//    PowerMockito.when(woTestServiceMapService.insertWoTestServiceMap(any())).thenReturn(resultDTO);
    PowerMockito.when(woInsideDTO.getListWoTestServiceMap()).thenReturn(lstWoTestService);
    PowerMockito.when(woInsideDTO.getWoKTTSInfoDTO()).thenReturn(woKTTSInfoDTO);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    ResultInSideDto result = woBusiness.insertWoFromWeb(fileAttacks, fileAttacks, woInsideDTO);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void insertWoFromWeb_04() throws Exception {
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setUserID(1L);
    userToken.setUserName("1");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    fileAttacks.add(firstFile);
    WoDetailDTO woDeDTO = Mockito.spy(WoDetailDTO.class);
    woDeDTO.setServiceId(1L);
    WoKTTSInfoDTO woKTTSInfoDTO = Mockito.spy(WoKTTSInfoDTO.class);
    woKTTSInfoDTO.setContractId(1L);
    WoTestServiceMapDTO woTestServiceMapDTO = Mockito.spy(WoTestServiceMapDTO.class);
    woTestServiceMapDTO.setWoId("1");
    List<WoTestServiceMapDTO> lstWoTestService = Mockito.spy(ArrayList.class);
    lstWoTestService.add(woTestServiceMapDTO);
    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1");
    List<CrFilesAttachInsiteDTO> list = Mockito.spy(ArrayList.class);
    list.add(crFilesAttachInsiteDTO);
    crFilesAttachInsiteDTO.setLstCustomerFile(list);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setStartTime(new Date());
    woInsideDTO.setEndTime(DateTimeUtils.convertStringToDate("03/09/2021 03:03:03"));
    woInsideDTO.setEndTime(new Date());
    woInsideDTO.setCommentComplete("1");
    woInsideDTO.setOtherSystemType("SR");
    woInsideDTO.setStatus(1L);
    woInsideDTO.setWoContent("1");
    woInsideDTO.setFtId(1L);
    woInsideDTO.setWoDetailDTO(woDeDTO);
    woInsideDTO.setWoKttsInfo(woKTTSInfoDTO);
    woInsideDTO.setWoWorklogContent("1");
    woInsideDTO.setLstWoTestService(lstWoTestService);
    woInsideDTO.setCrFilesAttachInsiteDTO(crFilesAttachInsiteDTO);
    woInsideDTO.setCreateDate(new Date());
    woInsideDTO.setOtherSystemType("SR");
    woInsideDTO.setOtherSystemId(1L);
    woInsideDTO.setWoTypeId(1L);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setMappingId(1L);
    gnocFileDto.setPath("1");
    gnocFileDto.setMultipartFile(firstFile);
    List<GnocFileDto> list2 = Mockito.spy(ArrayList.class);
    list2.add(gnocFileDto);
    woInsideDTO.setGnocFileCreateWoDtos(list2);
    List<String> list1 = Mockito.spy(ArrayList.class);
    list1.add("1");
    woInsideDTO.setCdIdList(list1);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.DC.HC", "2,2");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC", "1,1");
    Object[] header9 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(header9);
    String filePath = "./temp";
    Kttsbo resultCheckKtts = Mockito.spy(Kttsbo.class);
    resultCheckKtts.setStatus("Success");
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 2,
        0, 3, 2)).thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 3,
        1, 3, 2)).thenReturn(lstHeader);
    PowerMockito.when(kttsVsmartPort.checkMerchandiseCodeNation(any(), any()))
        .thenReturn(resultCheckKtts);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(list2);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setMessage("SUCCESS");
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
        "6cf8a37f3e6993f90eef71859b1ebf31", "/ftpFolder", firstFile.getOriginalFilename(),
        firstFile.getBytes(), FileUtils.createDateOfFileName(woInsideDTO.getCreateDate())))
        .thenReturn("trungduong");
    ResultDTO sTest = Mockito.spy(ResultDTO.class);
    sTest.setKey("SUCCESS");
    PowerMockito.when(commonStreamServiceProxy.insertWoTestServiceMap(any())).thenReturn(sTest);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(list2);
    PowerMockito.when(ExcelWriterUtils.compareHeader(any(), any())).thenReturn(true);
//    PowerMockito.when(woTestServiceMapService.insertWoTestServiceMap(any())).thenReturn(resultDTO);
    PowerMockito.when(woInsideDTO.getListWoTestServiceMap()).thenReturn(lstWoTestService);
    PowerMockito.when(woInsideDTO.getWoKTTSInfoDTO()).thenReturn(woKTTSInfoDTO);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    ResultInSideDto result = woBusiness.insertWoFromWeb(fileAttacks, fileAttacks, woInsideDTO);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void updateWoFromWeb_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(FileUtils.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setUserID(1L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    fileAttacks.add(firstFile);
    WoDetailDTO woDeDTO = Mockito.spy(WoDetailDTO.class);
    woDeDTO.setServiceId(1L);
    WoKTTSInfoDTO woKTTSInfoDTO = Mockito.spy(WoKTTSInfoDTO.class);
    woKTTSInfoDTO.setContractId(1L);
    WoTestServiceMapDTO woTestServiceMapDTO = Mockito.spy(WoTestServiceMapDTO.class);
    woTestServiceMapDTO.setWoId("1");
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    List<GnocFileDto> list3 = Mockito.spy(ArrayList.class);
    list3.add(gnocFileDto);
    List<WoTestServiceMapDTO> lstWoTestService = Mockito.spy(ArrayList.class);
    lstWoTestService.add(woTestServiceMapDTO);
    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setStartTime(new Date());
    woInsideDTO.setEndTime(DateTimeUtils.convertStringToDate("03/09/2021 03:03:03"));
    woInsideDTO.setEndTime(new Date());
    woInsideDTO.setCommentComplete("1");
    woInsideDTO.setOtherSystemType("SR");
    woInsideDTO.setStatus(1L);
    woInsideDTO.setWoContent("1");
    woInsideDTO.setFtId(1L);
    woInsideDTO.setWoDetailDTO(woDeDTO);
    woInsideDTO.setWoKttsInfo(woKTTSInfoDTO);
    woInsideDTO.setWoWorklogContent("1");
    woInsideDTO.setLstWoTestService(lstWoTestService);
    woInsideDTO.setCrFilesAttachInsiteDTO(crFilesAttachInsiteDTO);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setStatusSearchWeb("5");
    woInsideDTO.setParentId(1L);
    woInsideDTO.setCreateDate(new Date());
    woInsideDTO.setGnocFileCreateWoDtos(list3);
    List<WoInsideDTO> woInsideDTOList = Mockito.spy(ArrayList.class);
    woInsideDTOList.add(woInsideDTO);
    List<String> list = Mockito.spy(ArrayList.class);
    list.add("1");
    woInsideDTO.setCdIdList(list);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setMessage("SUCCESS");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitToken);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
        "6cf8a37f3e6993f90eef71859b1ebf31", "", firstFile.getOriginalFilename(),
        firstFile.getBytes(), FileUtils.createDateOfFileName(woInsideDTO.getCreateDate())))
        .thenReturn("trungduong");
    PowerMockito.when(woRepository.getListDataSearchWoDTO(any())).thenReturn(woInsideDTOList);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.findWoByIdNoOffset(1L)).thenReturn(woInsideDTO);
//    PowerMockito.when(woTestServiceMapService.insertWoTestServiceMap(any())).thenReturn(resultDTO);
    PowerMockito.when(woInsideDTO.getListWoTestServiceMap()).thenReturn(lstWoTestService);
    PowerMockito.when(woInsideDTO.getWoKTTSInfoDTO()).thenReturn(woKTTSInfoDTO);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    ResultInSideDto result = woBusiness.updateWoFromWeb(fileAttacks, fileAttacks, woInsideDTO);
    assertNull(result);
  }

  @Test
  public void updateWoFromWeb_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setUserID(1L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
    fileAttacks.add(firstFile);
    WoDetailDTO woDeDTO = Mockito.spy(WoDetailDTO.class);
    woDeDTO.setServiceId(1L);
    WoKTTSInfoDTO woKTTSInfoDTO = Mockito.spy(WoKTTSInfoDTO.class);
    woKTTSInfoDTO.setContractId(1L);
    WoTestServiceMapDTO woTestServiceMapDTO = Mockito.spy(WoTestServiceMapDTO.class);
    woTestServiceMapDTO.setWoId("1");
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setMappingId(1L);
    gnocFileDto.setPath("1");
    gnocFileDto.setMultipartFile(firstFile);
    List<GnocFileDto> list3 = Mockito.spy(ArrayList.class);
    list3.add(gnocFileDto);
    List<WoTestServiceMapDTO> lstWoTestService = Mockito.spy(ArrayList.class);
    lstWoTestService.add(woTestServiceMapDTO);
    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setStartTime(new Date());
    woInsideDTO.setEndTime(DateTimeUtils.convertStringToDate("03/09/2021 03:03:03"));
    woInsideDTO.setEndTime(new Date());
    woInsideDTO.setCommentComplete("1");
    woInsideDTO.setOtherSystemType("SR");
    woInsideDTO.setStatus(1L);
    woInsideDTO.setWoContent("1");
    woInsideDTO.setFtId(1L);
    woInsideDTO.setWoDetailDTO(woDeDTO);
    woInsideDTO.setWoKttsInfo(woKTTSInfoDTO);
    woInsideDTO.setWoWorklogContent("1");
    woInsideDTO.setLstWoTestService(lstWoTestService);
    woInsideDTO.setCrFilesAttachInsiteDTO(crFilesAttachInsiteDTO);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setStatusSearchWeb("5");
    woInsideDTO.setParentId(1L);
    woInsideDTO.setCreateDate(new Date());
    woInsideDTO.setWoTypeId(1l);
    woInsideDTO.setGnocFileCreateWoDtos(list3);
    List<WoInsideDTO> woInsideDTOList = Mockito.spy(ArrayList.class);
    woInsideDTOList.add(woInsideDTO);
    List<String> list = Mockito.spy(ArrayList.class);
    list.add("1");
    woInsideDTO.setCdIdList(list);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setMessage("SUCCESS");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.DC.HC", "2,2");
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS.NC", "1,1");
    String filePath = "/temp";
    Object[] header9 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(header9);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 2,
        0, 3, 2)).thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 3,
        1, 3, 2)).thenReturn(lstHeader);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(ExcelWriterUtils.compareHeader(any(), any())).thenReturn(true);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(list3);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitToken);
    PowerMockito.when(woRepository.getListDataSearchWoDTO(any())).thenReturn(woInsideDTOList);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.findWoByIdNoOffset(1L)).thenReturn(woInsideDTO);
//    PowerMockito.when(woTestServiceMapService.insertWoTestServiceMap(any())).thenReturn(resultDTO);
    PowerMockito.when(woInsideDTO.getListWoTestServiceMap()).thenReturn(lstWoTestService);
    PowerMockito.when(woInsideDTO.getWoKTTSInfoDTO()).thenReturn(woKTTSInfoDTO);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    ResultInSideDto result = woBusiness.updateWoFromWeb(fileAttacks, fileAttacks, woInsideDTO);
    assertNull(result);
  }

  @Test
  public void updateStatusFW_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setWoSystemId("1");
    woInsideDTO.setStatus(1L);
    woInsideDTO.setWoSystem("SPM");
    woInsideDTO.setWoTypeId(1L);
    woInsideDTO.setFtId(1L);
    woInsideDTO.setDeviceCode("1");
    woInsideDTO.setWoCode("1");
    List<WoInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woInsideDTO);
    WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO = Mockito
        .spy(WoMaterialDeducteInsideDTO.class);
    List<WoMaterialDeducteInsideDTO> lstDeducte = Mockito.spy(ArrayList.class);
    lstDeducte.add(woMaterialDeducteInsideDTO);
    WoHistoryInsideDTO woDtoHis = Mockito.spy(WoHistoryInsideDTO.class);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("CHECK_STATUS_APPROVE_WO_SPM", "1,1");
    mapConfigProperty.put("WO_TYPE_BD_VIBA", "1,1");
    mapConfigProperty.put("WO_TYPE_XU_LY_CAN_NHIEU", "1,1");
    mapConfigProperty.put("WO_TYPE_NIMS_HUY_TRAM", "1,1");
    mapConfigProperty.put("WO_TYPE_HOAN_CONG_OS", "1,1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    UsersEntity ft = Mockito.spy(UsersEntity.class);
    UnitDTO unFt = Mockito.spy(UnitDTO.class);
    ResultCheckStatusCabinet res = Mockito.spy(ResultCheckStatusCabinet.class);
    res.setResult("OK");
    res.setCabinetStatus("Hủy");
    Kttsbo resTS = Mockito.spy(Kttsbo.class);
    resTS.setStatus("OK");
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(hoanCongPort.updateWoToHoanCong(anyString())).thenReturn("SUCCESS");
    PowerMockito.when(kttsVsmartPort.checkOffStationNation(anyString(), anyLong(), any()))
        .thenReturn(resTS);
    PowerMockito.when(wsHTNims.checkStatusCabinet(anyString())).thenReturn(res);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unFt);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(ft);
    PowerMockito.when(woRepository.updateObjectData(any(), any())).thenReturn(woInsideDTO);
    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducte(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(woInsideDTO);
    PowerMockito.when(woRepository.getListWoBySystemOtherCode(anyString(), any())).thenReturn(lst);
    ResultInSideDto result = woBusiness.updateStatusFW(
        woInsideDTO, lstDeducte, "1", "1",
        "1", "1", "1",
        "1", true, woDtoHis, "1",
        "1", true, true, true, "1");
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void updateStatusFW_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setWoSystemId("1");
    woInsideDTO.setStatus(6L);
    woInsideDTO.setWoSystem("SPM");
    woInsideDTO.setWoTypeId(1L);
    woInsideDTO.setFtId(1L);
    woInsideDTO.setDeviceCode("1");
    woInsideDTO.setWoCode("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setWoSystemId("1");
    wo.setStatus(5L);
    wo.setWoSystem("SPM");
    wo.setWoTypeId(1L);
    wo.setFtId(1L);
    wo.setDeviceCode("1");
    wo.setWoCode("1");
    List<WoInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woInsideDTO);
    WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO = Mockito
        .spy(WoMaterialDeducteInsideDTO.class);
    woMaterialDeducteInsideDTO.setMaterialGroupCode("1");
    List<WoMaterialDeducteInsideDTO> lstDeducte = Mockito.spy(ArrayList.class);
    WoHistoryInsideDTO woDtoHis = Mockito.spy(WoHistoryInsideDTO.class);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("CHECK_STATUS_APPROVE_WO_SPM", "1,1");
    mapConfigProperty.put("WO_TYPE_BD_VIBA", "1,1");
    mapConfigProperty.put("WO_TYPE_XU_LY_CAN_NHIEU", "1,1");
    mapConfigProperty.put("WO_TYPE_NIMS_HUY_TRAM", "1,1");
    mapConfigProperty.put("WO_TYPE_HOAN_CONG_OS", "1,1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    UsersEntity ft = Mockito.spy(UsersEntity.class);
    UnitDTO unFt = Mockito.spy(UnitDTO.class);
    ResultCheckStatusCabinet res = Mockito.spy(ResultCheckStatusCabinet.class);
    res.setResult("OK");
    res.setCabinetStatus("Hủy");
    Kttsbo resTS = Mockito.spy(Kttsbo.class);
    resTS.setStatus("OK");
    PowerMockito.when(woMaterialDeducteBusiness.deleteMaterialDeducte(anyLong(), anyLong()))
        .thenReturn(resultInSideDto1);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(hoanCongPort.updateWoToHoanCong(anyString())).thenReturn("SUCCESS");
    PowerMockito.when(kttsVsmartPort.checkOffStationNation(anyString(), anyLong(), any()))
        .thenReturn(resTS);
    PowerMockito.when(wsHTNims.checkStatusCabinet(anyString())).thenReturn(res);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unFt);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(ft);
    PowerMockito.when(woRepository.updateObjectData(any(), any())).thenReturn(woInsideDTO);
    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducte(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    PowerMockito.when(woRepository.getListWoBySystemOtherCode(anyString(), any())).thenReturn(lst);
    ResultInSideDto result = woBusiness.updateStatusFW(
        woInsideDTO, lstDeducte, "1", "1",
        "1", "1", "1",
        "1", true, woDtoHis, "1",
        "1", true, true, true, "1");
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void updateStatusFW_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setWoSystemId("1");
    woInsideDTO.setStatus(6L);
    woInsideDTO.setWoSystem("SPM");
    woInsideDTO.setWoTypeId(1L);
    woInsideDTO.setFtId(1L);
    woInsideDTO.setDeviceCode("1");
    woInsideDTO.setWoCode("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setWoSystemId("1");
    wo.setStatus(5L);
    wo.setWoSystem("SPM");
    wo.setWoTypeId(1L);
    wo.setFtId(1L);
    wo.setDeviceCode("1");
    wo.setWoCode("1");
    wo.setNeedSupport(1L);
    List<WoInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woInsideDTO);
    WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO = Mockito
        .spy(WoMaterialDeducteInsideDTO.class);
    woMaterialDeducteInsideDTO.setMaterialGroupCode("1");
    List<WoMaterialDeducteInsideDTO> lstDeducte = Mockito.spy(ArrayList.class);
    WoHistoryInsideDTO woDtoHis = Mockito.spy(WoHistoryInsideDTO.class);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("CHECK_STATUS_APPROVE_WO_SPM", "1,1");
    mapConfigProperty.put("WO_TYPE_BD_VIBA", "1,1");
    mapConfigProperty.put("WO_TYPE_XU_LY_CAN_NHIEU", "1,1");
    mapConfigProperty.put("WO_TYPE_NIMS_HUY_TRAM", "1,1");
    mapConfigProperty.put("WO_TYPE_HOAN_CONG_OS", "1,1");
    mapConfigProperty.put("COMPLETED_FIRST_NEED_SUPPORT", "1,1");

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    UsersEntity ft = Mockito.spy(UsersEntity.class);
    UnitDTO unFt = Mockito.spy(UnitDTO.class);
    ResultCheckStatusCabinet res = Mockito.spy(ResultCheckStatusCabinet.class);
    res.setResult("OK");
    res.setCabinetStatus("Hủy");
    Kttsbo resTS = Mockito.spy(Kttsbo.class);
    resTS.setStatus("OK");
    List<WoMaterialDeducteInsideDTO> woMaterialDeducteInsideDTOS = Mockito.spy(ArrayList.class);
    woMaterialDeducteInsideDTOS.add(woMaterialDeducteInsideDTO);
    PowerMockito.when(woMaterialDeducteBusiness.validateMaterialCompleted(any())).thenReturn("1");
    PowerMockito.when(woMaterialDeducteBusiness.getMaterialDeducteKeyByWO(anyLong()))
        .thenReturn(woMaterialDeducteInsideDTOS);
    PowerMockito.when(woMaterialDeducteBusiness.deleteMaterialDeducte(anyLong(), anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(hoanCongPort.updateWoToHoanCong(anyString())).thenReturn("SUCCESS");
    PowerMockito.when(kttsVsmartPort.checkOffStationNation(anyString(), anyLong(), any()))
        .thenReturn(resTS);
    PowerMockito.when(wsHTNims.checkStatusCabinet(anyString())).thenReturn(res);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unFt);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(ft);
    PowerMockito.when(woRepository.updateObjectData(any(), any())).thenReturn(woInsideDTO);
    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducte(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    PowerMockito.when(woRepository.getListWoBySystemOtherCode(anyString(), any())).thenReturn(lst);
    ResultInSideDto result = woBusiness.updateStatusFW(
        woInsideDTO, lstDeducte, "1", "1",
        "1", "1", "1",
        "1", true, woDtoHis, "1",
        "1", true, true, true, "1");
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void updateStatusFW_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setWoSystemId("1");
    woInsideDTO.setStatus(6L);
    woInsideDTO.setWoSystem("SPM");
    woInsideDTO.setWoTypeId(1L);
    woInsideDTO.setFtId(1L);
    woInsideDTO.setDeviceCode("1");
    woInsideDTO.setWoCode("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setWoSystemId("1");
    wo.setStatus(5L);
    wo.setWoSystem("SPM");
    wo.setWoTypeId(1L);
    wo.setFtId(1L);
    wo.setDeviceCode("1");
    wo.setWoCode("1");
    wo.setNeedSupport(1L);
    List<WoInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woInsideDTO);
    WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO = Mockito
        .spy(WoMaterialDeducteInsideDTO.class);
    woMaterialDeducteInsideDTO.setMaterialGroupCode("1");
    List<WoMaterialDeducteInsideDTO> lstDeducte = Mockito.spy(ArrayList.class);
    WoHistoryInsideDTO woDtoHis = Mockito.spy(WoHistoryInsideDTO.class);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("CHECK_STATUS_APPROVE_WO_SPM", "1,1");
    mapConfigProperty.put("WO_TYPE_BD_VIBA", "1,1");
    mapConfigProperty.put("WO_TYPE_XU_LY_CAN_NHIEU", "1,1");
    mapConfigProperty.put("WO_TYPE_NIMS_HUY_TRAM", "1,1");
    mapConfigProperty.put("WO_TYPE_HOAN_CONG_OS", "1,1");
    mapConfigProperty.put("COMPLETED_FIRST_NEED_SUPPORT", "1,1");
    mapConfigProperty.put("WO_TYPE_BD_VIBA", "1,1");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    UsersEntity ft = Mockito.spy(UsersEntity.class);
    UnitDTO unFt = Mockito.spy(UnitDTO.class);
    ResultCheckStatusCabinet res = Mockito.spy(ResultCheckStatusCabinet.class);
    res.setResult("OK");
    res.setCabinetStatus("Hủy");
    Kttsbo resTS = Mockito.spy(Kttsbo.class);
    resTS.setStatus("OK");
    List<WoMaterialDeducteInsideDTO> woMaterialDeducteInsideDTOS = Mockito.spy(ArrayList.class);
    woMaterialDeducteInsideDTOS.add(woMaterialDeducteInsideDTO);
//    PowerMockito.when(woMaterialDeducteBusiness.validateMaterialCompleted(any())).thenReturn("1");
    PowerMockito.when(woMaterialDeducteBusiness.getMaterialDeducteKeyByWO(anyLong()))
        .thenReturn(woMaterialDeducteInsideDTOS);
    PowerMockito.when(woMaterialDeducteBusiness.deleteMaterialDeducte(anyLong(), anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(hoanCongPort.updateWoToHoanCong(anyString())).thenReturn("SUCCESS");
    PowerMockito.when(kttsVsmartPort.checkOffStationNation(anyString(), anyLong(), any()))
        .thenReturn(resTS);
    PowerMockito.when(wsHTNims.checkStatusCabinet(anyString())).thenReturn(res);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unFt);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(ft);
    PowerMockito.when(woRepository.updateObjectData(any(), any())).thenReturn(woInsideDTO);
    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducte(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    PowerMockito.when(woRepository.getListWoBySystemOtherCode(anyString(), any())).thenReturn(lst);
    ResultInSideDto result = woBusiness.updateStatusFW(
        woInsideDTO, lstDeducte, "1", "1",
        "1", "1", "1",
        "1", true, woDtoHis, "1",
        "1", true, true, true, "1");
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void updateStatusFW_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setWoSystemId("1");
    woInsideDTO.setStatus(1L);
    woInsideDTO.setWoSystem("WO_SPM");
    woInsideDTO.setWoTypeId(1L);
    woInsideDTO.setFtId(1L);
    woInsideDTO.setDeviceCode("1");
    woInsideDTO.setWoCode("1");
    woInsideDTO.setResult(1L);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setWoSystemId("1");
    wo.setStatus(1L);
    wo.setWoSystem("WO_SPM");
    wo.setWoTypeId(1L);
    wo.setFtId(1L);
    wo.setDeviceCode("1");
    wo.setWoCode("1");
    wo.setNeedSupport(1L);
    wo.setCompletedTime(new Date());
    List<WoInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woInsideDTO);
    WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO = Mockito
        .spy(WoMaterialDeducteInsideDTO.class);
    woMaterialDeducteInsideDTO.setMaterialGroupCode("1");
    List<WoMaterialDeducteInsideDTO> lstDeducte = Mockito.spy(ArrayList.class);
    WoHistoryInsideDTO woDtoHis = Mockito.spy(WoHistoryInsideDTO.class);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("CHECK_STATUS_APPROVE_WO_SPM", "1,1");
    mapConfigProperty.put("WO_TYPE_BD_VIBA", "1,1");
    mapConfigProperty.put("WO_TYPE_XU_LY_CAN_NHIEU", "1,1");
    mapConfigProperty.put("WO_TYPE_NIMS_HUY_TRAM", "1,1");
    mapConfigProperty.put("WO_TYPE_HOAN_CONG_OS", "1,1");
    mapConfigProperty.put("COMPLETED_FIRST_NEED_SUPPORT", "1,1");
    mapConfigProperty.put("AUTO_CHECKED_WHEN_APPROVE_SPM", "1,1");

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    UsersEntity ft = Mockito.spy(UsersEntity.class);
    UnitDTO unFt = Mockito.spy(UnitDTO.class);
    ResultCheckStatusCabinet res = Mockito.spy(ResultCheckStatusCabinet.class);
    res.setResult("OK");
    res.setCabinetStatus("Hủy");
    Kttsbo resTS = Mockito.spy(Kttsbo.class);
    resTS.setStatus("OK");
    List<WoMaterialDeducteInsideDTO> woMaterialDeducteInsideDTOS = Mockito.spy(ArrayList.class);
    woMaterialDeducteInsideDTOS.add(woMaterialDeducteInsideDTO);
//    PowerMockito.when(woMaterialDeducteBusiness.validateMaterialCompleted(any())).thenReturn("1");
    PowerMockito.when(woMaterialDeducteBusiness.getMaterialDeducteKeyByWO(anyLong()))
        .thenReturn(woMaterialDeducteInsideDTOS);
    PowerMockito.when(woMaterialDeducteBusiness.deleteMaterialDeducte(anyLong(), anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(resultInSideDto);
    PowerMockito.when(hoanCongPort.updateWoToHoanCong(anyString())).thenReturn("SUCCESS");
    PowerMockito.when(kttsVsmartPort.checkOffStationNation(anyString(), anyLong(), any()))
        .thenReturn(resTS);
    PowerMockito.when(wsHTNims.checkStatusCabinet(anyString())).thenReturn(res);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unFt);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(ft);
    PowerMockito.when(woRepository.updateObjectData(any(), any())).thenReturn(woInsideDTO);
    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducte(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    PowerMockito.when(woRepository.getListWoBySystemOtherCode(anyString(), any())).thenReturn(lst);
    ResultInSideDto result = woBusiness.updateStatusFW(
        woInsideDTO, lstDeducte, "1", "1",
        "1", "1", "1",
        "1", true, woDtoHis, "1",
        "1", true, true, true, "OK");
    assertEquals(result.getKey(), "ERROR");
  }


  @Test
  public void approveWoCommon_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    try {
      woBusiness.approveWoCommon(updateForm, "1", 1L, "1", "1", "1");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void approveWoCommon_02() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(1L);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    try {
      woBusiness.approveWoCommon(updateForm, "1", 1L, "1", "1", "1");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void approveWoCommon_03() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(NocProPort.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    updateForm.setByPassAutoCheck(2L);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(6L);
    wo.setFtId(1L);
    wo.setWoTypeId(1L);
    wo.setWoId(1L);
    wo.setWoSystem("SPM");
    wo.setVibaLineCode("1");
    wo.setStationCode("1");
    wo.setEndTime(DateTimeUtils.convertStringToDateTime("04/04/2021 09:09:09"));
    wo.setCompletedTime(DateTimeUtils.convertStringToDateTime("04/04/2020 09:09:09"));
    wo.setFinishTime(DateTimeUtils.convertStringToDateTime("04/04/2020 09:09:09"));
    wo.setStartTime(DateTimeUtils.convertStringToDateTime("04/04/2019 09:09:09"));
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    listUser.add(usersInsideDto);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("CHECK_WO_TYPE_NOT_ALLOW_APPROVE", "2,2");
    mapConfigProperty.put("CWO_TYPE_BD_VIBA", "1,1");
    mapConfigProperty.put("AUTO_CHECKED_WHEN_APPROVE_SPM", "1,1");
    mapConfigProperty.put("WO_TYPE_BD_VIBA", "1,1");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    Kttsbo resKtts = Mockito.spy(Kttsbo.class);
    resKtts.setStatus("SUCCESS");
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    detail.setCcResult(1L);
    detail.setInfoTicket("1");
    detail.setResultCheckName("1");
    detail.setResultCheck("1");
    ResultInSideDto rsDeducteIM = Mockito.spy(ResultInSideDto.class);
    rsDeducteIM.setKey("SUCCESS");
    WoPendingDTO woPendingDTO = Mockito.spy(WoPendingDTO.class);
    woPendingDTO.setOpenTime(new Date());
    woPendingDTO.setInsertTime(DateTimeUtils.convertStringToDateTime("04/04/2019 09:09:09"));
    List<WoPendingDTO> lstPending = Mockito.spy(ArrayList.class);
    lstPending.add(woPendingDTO);
    JsonResponseBO resNoc = Mockito.spy(JsonResponseBO.class);
    PowerMockito.when(nocPort.getDataJson(any())).thenReturn(resNoc);
    PowerMockito.when(woPendingRepository.getListWoPendingByWoId(anyLong())).thenReturn(lstPending);
    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducteToIM(anyLong(), anyBoolean()))
        .thenReturn(rsDeducteIM);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(kttsVsmartPort.checkWorkOrderNation(anyLong(), anyString()))
        .thenReturn(resKtts);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    try {
      ResultInSideDto result = woBusiness.approveWoCommon(updateForm, "1", 1L, "1", "2", "1");
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void approveWoCommon_04() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(NocProPort.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    updateForm.setByPassAutoCheck(2L);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(6L);
    wo.setFtId(1L);
    wo.setWoTypeId(1L);
    wo.setWoId(1L);
    wo.setWoSystem("SPM");
    wo.setVibaLineCode("1");
    wo.setStationCode("1");
    wo.setWoCode("1");
    wo.setEndTime(DateTimeUtils.convertStringToDateTime("04/04/2021 09:09:09"));
    wo.setCompletedTime(DateTimeUtils.convertStringToDateTime("04/04/2020 09:09:09"));
    wo.setFinishTime(DateTimeUtils.convertStringToDateTime("04/04/2020 09:09:09"));
    wo.setStartTime(DateTimeUtils.convertStringToDateTime("04/04/2019 09:09:09"));
    wo.setFileName("1");
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    listUser.add(usersInsideDto);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("CHECK_WO_TYPE_NOT_ALLOW_APPROVE", "2,2");
    mapConfigProperty.put("CWO_TYPE_BD_VIBA", "1,1");
    mapConfigProperty.put("AUTO_CHECKED_WHEN_APPROVE_SPM", "1,1");
    mapConfigProperty.put("WO_TYPE_BD_VIBA", "2,2");
    mapConfigProperty.put("WO_TYPE_NIMS_KTDL", "1,1");
    mapConfigProperty.put("WO_TYPE_SCTBCD_ACCESS", "1,1");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    Kttsbo resKtts = Mockito.spy(Kttsbo.class);
    resKtts.setStatus("SUCCESS");
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    detail.setCcResult(1L);
    detail.setInfoTicket("1");
    detail.setResultCheckName("1");
    detail.setResultCheck("1");
    ResultInSideDto rsDeducteIM = Mockito.spy(ResultInSideDto.class);
    rsDeducteIM.setKey("SUCCESS");
    WoPendingDTO woPendingDTO = Mockito.spy(WoPendingDTO.class);
    woPendingDTO.setOpenTime(new Date());
    woPendingDTO.setInsertTime(DateTimeUtils.convertStringToDateTime("04/04/2019 09:09:09"));
    List<WoPendingDTO> lstPending = Mockito.spy(ArrayList.class);
    lstPending.add(woPendingDTO);
    JsonResponseBO resNoc = Mockito.spy(JsonResponseBO.class);
    CheckPortSubDescriptionByWOForm res = Mockito.spy(CheckPortSubDescriptionByWOForm.class);
    res.setResult("OK");
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    com.viettel.webservice.codien.Result result1 = Mockito
        .spy(com.viettel.webservice.codien.Result.class);
    result1.setStatus(0L);
    PowerMockito.when(
        coDienPort.updateResultWORepairDevice(anyString(), anyLong(), anyString(), any(), any()))
        .thenReturn(result1);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtos);
    PowerMockito.when(wsHTNims.checkPortSubDescriptionByWO(any())).thenReturn(res);
    PowerMockito.when(nocPort.getDataJson(any())).thenReturn(resNoc);
    PowerMockito.when(woPendingRepository.getListWoPendingByWoId(anyLong())).thenReturn(lstPending);
    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducteToIM(anyLong(), anyBoolean()))
        .thenReturn(rsDeducteIM);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(kttsVsmartPort.checkWorkOrderNation(anyLong(), anyString()))
        .thenReturn(resKtts);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    try {
      ResultInSideDto result = woBusiness.approveWoCommon(updateForm, "1", 1L, "1", "2", "1");
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void approveWoCommon_05() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(NocProPort.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    updateForm.setByPassAutoCheck(2L);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(6L);
    wo.setFtId(1L);
    wo.setWoTypeId(1L);
    wo.setWoId(1L);
    wo.setWoSystem("WO_TT");
    wo.setVibaLineCode("1");
    wo.setStationCode("1");
    wo.setWoCode("1");
    wo.setWoTypeId(1L);
    wo.setWoCode("1");
    wo.setDeviceCode("1");
    wo.setCdId(1L);
    wo.setReasonInterference(1L);
    wo.setEndTime(DateTimeUtils.convertStringToDateTime("04/04/2021 09:09:09"));
    wo.setCompletedTime(DateTimeUtils.convertStringToDateTime("04/04/2020 09:09:09"));
    wo.setFinishTime(DateTimeUtils.convertStringToDateTime("04/04/2020 09:09:09"));
    wo.setStartTime(DateTimeUtils.convertStringToDateTime("04/04/2019 09:09:09"));
    wo.setFileName("1");
    wo.setCreatePersonId(1L);
    wo.setStartTime(new Date());
    wo.setEndTime(DateTimeUtils.convertStringToDate("03/09/2021 03:03:03"));
    wo.setEndTime(new Date());
    wo.setCreatePersonId(1L);
    wo.setCcServiceId(1L);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    listUser.add(usersInsideDto);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("CHECK_WO_TYPE_NOT_ALLOW_APPROVE", "2,2");
    mapConfigProperty.put("CWO_TYPE_BD_VIBA", "1,1");
    mapConfigProperty.put("AUTO_CHECKED_WHEN_APPROVE_SPM", "1,1");
    mapConfigProperty.put("WO_TYPE_BD_VIBA", "2,2");
    mapConfigProperty.put("WO_TYPE_NIMS_KTDL", "1,1");
    mapConfigProperty.put("WO_TYPE_SCTBCD_ACCESS", "2,2");
    mapConfigProperty.put("WO_TYPE_BDCD_TRAM", "1,1");
    mapConfigProperty.put("WO_TYPE_DO_XANG_DAU", "1,1");
    mapConfigProperty.put("WO.TYPE.XLSCVT", "1,1");
    mapConfigProperty.put("IS_CALL_TT_SCVT", "1,1");
    mapConfigProperty.put("WO_TYPE_NIMS_HUY_TRAM", "1,1");
    mapConfigProperty.put("WO_TYPE_CDBR_FOR_STL", "1,1");
    mapConfigProperty.put("WO_TYPE_XU_LY_CAN_NHIEU", "1,1");
    mapConfigProperty.put("CAMERA_CC_SERVICE_ID", "1,1");
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    Kttsbo resKtts = Mockito.spy(Kttsbo.class);
    resKtts.setStatus("SUCCESS");
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    detail.setCcResult(1L);
    detail.setInfoTicket("1");
    detail.setResultCheckName("1");
    detail.setResultCheck("1");
    ResultInSideDto rsDeducteIM = Mockito.spy(ResultInSideDto.class);
    rsDeducteIM.setKey("SUCCESS");
    WoPendingDTO woPendingDTO = Mockito.spy(WoPendingDTO.class);
    woPendingDTO.setOpenTime(new Date());
    woPendingDTO.setInsertTime(DateTimeUtils.convertStringToDateTime("04/04/2019 09:09:09"));
    List<WoPendingDTO> lstPending = Mockito.spy(ArrayList.class);
    lstPending.add(woPendingDTO);
    JsonResponseBO resNoc = Mockito.spy(JsonResponseBO.class);
    CheckPortSubDescriptionByWOForm res = Mockito.spy(CheckPortSubDescriptionByWOForm.class);
    res.setResult("OK");
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    com.viettel.webservice.codien.Result result1 = Mockito
        .spy(com.viettel.webservice.codien.Result.class);
    result1.setStatus(0L);
    ObjKeyValue objKeyValue = Mockito.spy(ObjKeyValue.class);
    objKeyValue.setCol(3);
    objKeyValue.setValue("1");
    objKeyValue.setRow(1);
    List<ObjKeyValue> lst = Mockito.spy(ArrayList.class);
    lst.add(objKeyValue);
    Result resChiPhi = Mockito.spy(Result.class);
    resChiPhi.setStatus(0L);
    ResultCheckStatusCabinet resultCheckStatusCabinet = Mockito.spy(ResultCheckStatusCabinet.class);
    resultCheckStatusCabinet.setResult("OK");
    resultCheckStatusCabinet.setCabinetStatus("Hủy");
    Kttsbo resTS = Mockito.spy(Kttsbo.class);
    resTS.setStatus("OK");
    com.viettel.webservice.cc_stl.Output output = Mockito
        .spy(com.viettel.webservice.cc_stl.Output.class);
    output.setDescription("SUCCESS");
    output.setOriginal("<description>0</description><responseCode>0</responseCode>");
    PowerMockito.when(userRepository.getUserName(anyLong())).thenReturn("1");
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    PowerMockito.when(ccStlPort.updateComplain(any())).thenReturn(output);
    PowerMockito.when(kttsVsmartPort.checkOffStationNation(anyString(), anyLong(), anyString()))
        .thenReturn(resTS);
    PowerMockito.when(wsHTNims.checkStatusCabinet(any())).thenReturn(resultCheckStatusCabinet);
    PowerMockito.when(portChiPhi.confirmSucceedWO(anyLong(), anyString(), any()))
        .thenReturn(resChiPhi);
    PowerMockito.when(vsmartBusiness.getDataTestService(anyLong())).thenReturn(lst);
    PowerMockito.when(
        coDienPort.updateResultWORepairDevice(anyString(), anyLong(), anyString(), any(), any()))
        .thenReturn(result1);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtos);
    PowerMockito.when(wsHTNims.checkPortSubDescriptionByWO(any())).thenReturn(res);
    PowerMockito.when(nocPort.getDataJson(any())).thenReturn(resNoc);
    PowerMockito.when(woPendingRepository.getListWoPendingByWoId(anyLong())).thenReturn(lstPending);
    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducteToIM(anyLong(), anyBoolean()))
        .thenReturn(rsDeducteIM);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(kttsVsmartPort.checkWorkOrderNation(anyLong(), anyString()))
        .thenReturn(resKtts);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    try {
      ResultInSideDto result = woBusiness.approveWoCommon(updateForm, "1", 1L, "1", "2", "1");
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void approveWoCommon_06() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(NocProPort.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    updateForm.setByPassAutoCheck(2L);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(6L);
    wo.setFtId(1L);
    wo.setWoTypeId(1L);
    wo.setWoId(1L);
    wo.setWoSystem("TT");
    wo.setVibaLineCode("1");
    wo.setStationCode("1");
    wo.setWoCode("1");
    wo.setWoTypeId(1L);
    wo.setWoCode("1");
    wo.setDeviceCode("1");
    wo.setCdId(1L);
    wo.setReasonInterference(1L);
    wo.setEndTime(DateTimeUtils.convertStringToDateTime("04/04/2021 09:09:09"));
    wo.setCompletedTime(DateTimeUtils.convertStringToDateTime("04/04/2020 09:09:09"));
    wo.setFinishTime(DateTimeUtils.convertStringToDateTime("04/04/2020 09:09:09"));
    wo.setStartTime(DateTimeUtils.convertStringToDateTime("04/04/2019 09:09:09"));
    wo.setFileName("1");
    wo.setCreatePersonId(1L);
    wo.setStartTime(new Date());
    wo.setEndTime(DateTimeUtils.convertStringToDate("03/09/2021 03:03:03"));
    wo.setEndTime(new Date());
    wo.setCreatePersonId(1L);
    wo.setCcServiceId(1L);
    wo.setConfirmNotCreateAlarm(1L);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    listUser.add(usersInsideDto);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("CHECK_WO_TYPE_NOT_ALLOW_APPROVE", "2,2");
    mapConfigProperty.put("CWO_TYPE_BD_VIBA", "1,1");
    mapConfigProperty.put("AUTO_CHECKED_WHEN_APPROVE_SPM", "1,1");
    mapConfigProperty.put("WO_TYPE_BD_VIBA", "2,2");
    mapConfigProperty.put("WO_TYPE_NIMS_KTDL", "1,1");
    mapConfigProperty.put("WO_TYPE_SCTBCD_ACCESS", "2,2");
    mapConfigProperty.put("WO_TYPE_BDCD_TRAM", "1,1");
    mapConfigProperty.put("WO_TYPE_DO_XANG_DAU", "1,1");
    mapConfigProperty.put("WO.TYPE.XLSCVT", "2,2");
    mapConfigProperty.put("IS_CALL_TT_SCVT", "2,2");
    mapConfigProperty.put("WO_TYPE_NIMS_HUY_TRAM", "1,1");
    mapConfigProperty.put("WO_TYPE_CDBR_FOR_STL", "1,1");
    mapConfigProperty.put("WO_TYPE_XU_LY_CAN_NHIEU", "2,2");
    mapConfigProperty.put("CAMERA_CC_SERVICE_ID", "1,1");
    mapConfigProperty.put("WO_TYPE_HOAN_CONG_OS", "1,1");
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    Kttsbo resKtts = Mockito.spy(Kttsbo.class);
    resKtts.setStatus("SUCCESS");
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    detail.setCcResult(1L);
    detail.setInfoTicket("1");
    detail.setResultCheckName("1");
    detail.setResultCheck("1");
    ResultInSideDto rsDeducteIM = Mockito.spy(ResultInSideDto.class);
    rsDeducteIM.setKey("SUCCESS");
    WoPendingDTO woPendingDTO = Mockito.spy(WoPendingDTO.class);
    woPendingDTO.setOpenTime(new Date());
    woPendingDTO.setInsertTime(DateTimeUtils.convertStringToDateTime("04/04/2019 09:09:09"));
    List<WoPendingDTO> lstPending = Mockito.spy(ArrayList.class);
    lstPending.add(woPendingDTO);
    JsonResponseBO resNoc = Mockito.spy(JsonResponseBO.class);
    CheckPortSubDescriptionByWOForm res = Mockito.spy(CheckPortSubDescriptionByWOForm.class);
    res.setResult("OK");
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    com.viettel.webservice.codien.Result result1 = Mockito
        .spy(com.viettel.webservice.codien.Result.class);
    result1.setStatus(0L);
    ObjKeyValue objKeyValue = Mockito.spy(ObjKeyValue.class);
    objKeyValue.setCol(3);
    objKeyValue.setValue("1");
    objKeyValue.setRow(1);
    List<ObjKeyValue> lst = Mockito.spy(ArrayList.class);
    lst.add(objKeyValue);
    Result resChiPhi = Mockito.spy(Result.class);
    resChiPhi.setStatus(0L);
    ResultCheckStatusCabinet resultCheckStatusCabinet = Mockito.spy(ResultCheckStatusCabinet.class);
    resultCheckStatusCabinet.setResult("OK");
    resultCheckStatusCabinet.setCabinetStatus("Hủy");
    Kttsbo resTS = Mockito.spy(Kttsbo.class);
    resTS.setStatus("OK");
    com.viettel.webservice.cc_stl.Output output = Mockito
        .spy(com.viettel.webservice.cc_stl.Output.class);
    output.setDescription("SUCCESS");
    output.setOriginal("<description>0</description><responseCode>0</responseCode>");
    List<WoInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(wo);
    WoTroubleInfoDTO woTroubleInfoDTO = Mockito.spy(WoTroubleInfoDTO.class);
    woTroubleInfoDTO.setClearTime(new Date());
    woTroubleInfoDTO.setReasonTroubleName("1");
    List<WoTroubleInfoDTO> lstWoTrouble = Mockito.spy(ArrayList.class);
    lstWoTrouble.add(woTroubleInfoDTO);
    PowerMockito.when(hoanCongPort.updateWoToHoanCong(anyString())).thenReturn("SUCCESS");
    PowerMockito.when(woTroubleInfoRepository.getListWoTroubleInfoByWoId(anyLong()))
        .thenReturn(lstWoTrouble);
    PowerMockito.when(woRepository.getListWoBySystemOtherCode(anyString(), any())).thenReturn(list);
    PowerMockito.when(userRepository.getUserName(anyLong())).thenReturn("1");
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    PowerMockito.when(ccStlPort.updateComplain(any())).thenReturn(output);
    PowerMockito.when(kttsVsmartPort.checkOffStationNation(anyString(), anyLong(), anyString()))
        .thenReturn(resTS);
    PowerMockito.when(wsHTNims.checkStatusCabinet(any())).thenReturn(resultCheckStatusCabinet);
    PowerMockito.when(portChiPhi.confirmSucceedWO(anyLong(), anyString(), any()))
        .thenReturn(resChiPhi);
    PowerMockito.when(vsmartBusiness.getDataTestService(anyLong())).thenReturn(lst);
    PowerMockito.when(
        coDienPort.updateResultWORepairDevice(anyString(), anyLong(), anyString(), any(), any()))
        .thenReturn(result1);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtos);
    PowerMockito.when(wsHTNims.checkPortSubDescriptionByWO(any())).thenReturn(res);
    PowerMockito.when(nocPort.getDataJson(any())).thenReturn(resNoc);
    PowerMockito.when(woPendingRepository.getListWoPendingByWoId(anyLong())).thenReturn(lstPending);
    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducteToIM(anyLong(), anyBoolean()))
        .thenReturn(rsDeducteIM);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(kttsVsmartPort.checkWorkOrderNation(anyLong(), anyString()))
        .thenReturn(resKtts);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    try {
      ResultInSideDto result = woBusiness.approveWoCommon(updateForm, "1", 1L, "1", "2", "1");
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void approveWoCommon_07() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(NocProPort.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    VsmartUpdateForm updateForm = Mockito.spy(VsmartUpdateForm.class);
    updateForm.setByPassAutoCheck(2L);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setStatus(6L);
    wo.setFtId(1L);
    wo.setWoTypeId(1L);
    wo.setWoId(1L);
    wo.setWoSystem("TT");
    wo.setVibaLineCode("1");
    wo.setStationCode("1");
    wo.setWoCode("1");
    wo.setWoTypeId(1L);
    wo.setWoCode("1");
    wo.setDeviceCode("1");
    wo.setCdId(1L);
    wo.setReasonInterference(1L);
    wo.setEndTime(DateTimeUtils.convertStringToDateTime("04/04/2021 09:09:09"));
    wo.setCompletedTime(DateTimeUtils.convertStringToDateTime("04/04/2020 09:09:09"));
    wo.setFinishTime(DateTimeUtils.convertStringToDateTime("04/04/2020 09:09:09"));
    wo.setStartTime(DateTimeUtils.convertStringToDateTime("04/04/2019 09:09:09"));
    wo.setFileName("1");
    wo.setCreatePersonId(1L);
    wo.setStartTime(new Date());
    wo.setEndTime(DateTimeUtils.convertStringToDate("03/09/2021 03:03:03"));
    wo.setEndTime(new Date());
    wo.setCreatePersonId(1L);
    wo.setCcServiceId(1L);
    wo.setConfirmNotCreateAlarm(2L);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUnitId(1L);
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    listUser.add(usersInsideDto);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO.TYPE.CHECK.QLTS", "1,1");
    mapConfigProperty.put("CHECK_WO_TYPE_NOT_ALLOW_APPROVE", "2,2");
    mapConfigProperty.put("CWO_TYPE_BD_VIBA", "1,1");
    mapConfigProperty.put("AUTO_CHECKED_WHEN_APPROVE_SPM", "1,1");
    mapConfigProperty.put("WO_TYPE_BD_VIBA", "2,2");
    mapConfigProperty.put("WO_TYPE_NIMS_KTDL", "1,1");
    mapConfigProperty.put("WO_TYPE_SCTBCD_ACCESS", "2,2");
    mapConfigProperty.put("WO_TYPE_BDCD_TRAM", "1,1");
    mapConfigProperty.put("WO_TYPE_DO_XANG_DAU", "1,1");
    mapConfigProperty.put("WO.TYPE.XLSCVT", "2,2");
    mapConfigProperty.put("IS_CALL_TT_SCVT", "2,2");
    mapConfigProperty.put("WO_TYPE_NIMS_HUY_TRAM", "1,1");
    mapConfigProperty.put("WO_TYPE_CDBR_FOR_STL", "1,1");
    mapConfigProperty.put("WO_TYPE_XU_LY_CAN_NHIEU", "2,2");
    mapConfigProperty.put("CAMERA_CC_SERVICE_ID", "1,1");
    mapConfigProperty.put("WO_TYPE_HOAN_CONG_OS", "2,2");
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    Kttsbo resKtts = Mockito.spy(Kttsbo.class);
    resKtts.setStatus("SUCCESS");
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    detail.setCcResult(1L);
    detail.setInfoTicket("1");
    detail.setResultCheckName("1");
    detail.setResultCheck("1");
    ResultInSideDto rsDeducteIM = Mockito.spy(ResultInSideDto.class);
    rsDeducteIM.setKey("SUCCESS");
    WoPendingDTO woPendingDTO = Mockito.spy(WoPendingDTO.class);
    woPendingDTO.setOpenTime(new Date());
    woPendingDTO.setInsertTime(DateTimeUtils.convertStringToDateTime("04/04/2019 09:09:09"));
    List<WoPendingDTO> lstPending = Mockito.spy(ArrayList.class);
    lstPending.add(woPendingDTO);
    JsonResponseBO resNoc = Mockito.spy(JsonResponseBO.class);
    CheckPortSubDescriptionByWOForm res = Mockito.spy(CheckPortSubDescriptionByWOForm.class);
    res.setResult("OK");
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    com.viettel.webservice.codien.Result result1 = Mockito
        .spy(com.viettel.webservice.codien.Result.class);
    result1.setStatus(0L);
    ObjKeyValue objKeyValue = Mockito.spy(ObjKeyValue.class);
    objKeyValue.setCol(3);
    objKeyValue.setValue("1");
    objKeyValue.setRow(1);
    List<ObjKeyValue> lst = Mockito.spy(ArrayList.class);
    lst.add(objKeyValue);
    Result resChiPhi = Mockito.spy(Result.class);
    resChiPhi.setStatus(0L);
    ResultCheckStatusCabinet resultCheckStatusCabinet = Mockito.spy(ResultCheckStatusCabinet.class);
    resultCheckStatusCabinet.setResult("OK");
    resultCheckStatusCabinet.setCabinetStatus("Hủy");
    Kttsbo resTS = Mockito.spy(Kttsbo.class);
    resTS.setStatus("OK");
    com.viettel.webservice.cc_stl.Output output = Mockito
        .spy(com.viettel.webservice.cc_stl.Output.class);
    output.setDescription("SUCCESS");
    output.setOriginal("<description>0</description><responseCode>0</responseCode>");
    List<WoInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(wo);
    WoTroubleInfoDTO woTroubleInfoDTO = Mockito.spy(WoTroubleInfoDTO.class);
    woTroubleInfoDTO.setClearTime(new Date());
    woTroubleInfoDTO.setReasonTroubleName("1");
    List<WoTroubleInfoDTO> lstWoTrouble = Mockito.spy(ArrayList.class);
    lstWoTrouble.add(woTroubleInfoDTO);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    PowerMockito.when(hoanCongPort.updateWoToHoanCong(anyString())).thenReturn("SUCCESS");
    PowerMockito.when(woTroubleInfoRepository.getListWoTroubleInfoByWoId(anyLong()))
        .thenReturn(lstWoTrouble);
    PowerMockito.when(woRepository.getListWoBySystemOtherCode(anyString(), any())).thenReturn(list);
    PowerMockito.when(userRepository.getUserName(anyLong())).thenReturn("1");
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    PowerMockito.when(ccStlPort.updateComplain(any())).thenReturn(output);
    PowerMockito.when(kttsVsmartPort.checkOffStationNation(anyString(), anyLong(), anyString()))
        .thenReturn(resTS);
    PowerMockito.when(wsHTNims.checkStatusCabinet(any())).thenReturn(resultCheckStatusCabinet);
    PowerMockito.when(portChiPhi.confirmSucceedWO(anyLong(), anyString(), any()))
        .thenReturn(resChiPhi);
    PowerMockito.when(vsmartBusiness.getDataTestService(anyLong())).thenReturn(lst);
    PowerMockito.when(
        coDienPort.updateResultWORepairDevice(anyString(), anyLong(), anyString(), any(), any()))
        .thenReturn(result1);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtos);
    PowerMockito.when(wsHTNims.checkPortSubDescriptionByWO(any())).thenReturn(res);
    PowerMockito.when(nocPort.getDataJson(any())).thenReturn(resNoc);
    PowerMockito.when(woPendingRepository.getListWoPendingByWoId(anyLong())).thenReturn(lstPending);
    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducteToIM(anyLong(), anyBoolean()))
        .thenReturn(rsDeducteIM);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(kttsVsmartPort.checkWorkOrderNation(anyLong(), anyString()))
        .thenReturn(resKtts);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitDTO);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    try {
      ResultInSideDto result = woBusiness.approveWoCommon(updateForm, "1", 1L, "1", "1", "1");
      assertEquals(result.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void dispatchWoFromWeb_02() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setFtName("1");
    woInsideDTO.setWoId(1L);
    woInsideDTO.setComment("1");
    try {
      woBusiness.dispatchWoFromWeb(woInsideDTO);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void approveWoFromWeb_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setComment("1");
    woInsideDTO.setAction("1");
    woInsideDTO.setFtName("1");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    try {
      woBusiness.approveWoFromWeb(woInsideDTO);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void updateStatusFromWeb_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    woBusiness.updateStatusFromWeb(woInsideDTO);

  }

  @Test
  public void getWoStatusName_08() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    woBusiness.getWoStatusName(0);
  }

  @Test
  public void getWoStatusName_09() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    woBusiness.getWoStatusName(2);
  }

  @Test
  public void getWoStatusName_10() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    woBusiness.getWoStatusName(3);
  }

  @Test
  public void getWoStatusName_11() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    woBusiness.getWoStatusName(6);
  }

  @Test
  public void getWoStatusName_12() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    woBusiness.getWoStatusName(7);
  }

  @Test
  public void getWoStatusName_13() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    woBusiness.getWoStatusName(8);
  }

  @Test
  public void getWoStatusName_14() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    woBusiness.getWoStatusName(9);
  }

  @Test
  public void getWoStatusName_15() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    woBusiness.getWoStatusName(20);
  }

  @Test
  public void getFTSCVT_01() throws Exception {
    WoDTO o = Mockito.spy(WoDTO.class);
    List<String> list = Mockito.spy(ArrayList.class);
    list.add("1");
    o.setLstStationCode(list);
    o.setNationCode("1");
    o.setWoTypeId("1");
    o.setLocationCode("1");
    WoTypeInsideDTO woType = Mockito.spy(WoTypeInsideDTO.class);
    woType.setUserTypeCode("1");
    Staff staff = Mockito.spy(Staff.class);
    staff.setStaffType(11L);
    staff.setUserName("1");
    staff.setTelephone("1");
    staff.setFullName("1");
    List<Staff> lstStaff = Mockito.spy(ArrayList.class);
    lstStaff.add(staff);
    ResultGetDepartmentByLocationForm resultGetDepartmentByLocationForm = Mockito
        .spy(ResultGetDepartmentByLocationForm.class);
    resultGetDepartmentByLocationForm.setDepartmentId(1L);
    resultGetDepartmentByLocationForm.setDepartmentLevel(4L);
    List<ResultGetDepartmentByLocationForm> lst = Mockito.spy(ArrayList.class);
    lst.add(resultGetDepartmentByLocationForm);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitId(1L);
    unit.setUnitName("1");
    unit.setUnitCode("1");
    PowerMockito.when(woRepository.getCdByUnitId(anyLong(), anyLong())).thenReturn(1L);
    PowerMockito.when(woRepository.getUnitCodeMapNims(any(), any())).thenReturn(unit);
    PowerMockito.when(wsHTNims.getDepartmentByLocation(anyString())).thenReturn(lst);
    PowerMockito.when(userRepository.getUserId(anyString())).thenReturn(1L);
    PowerMockito.when(cdPort.getListStaff(any(), anyString(), anyString())).thenReturn(lstStaff);
    PowerMockito.when(woRepository.getStationFollowNode(any(), anyString())).thenReturn("1");
    PowerMockito.when(commonRepository.getConfigPropertyValue(anyString())).thenReturn("1");
    PowerMockito.when(woTypeBusiness.findWoTypeById(anyLong())).thenReturn(woType);
    woBusiness.getFTSCVT(o);
  }

  @Test
  public void getFtForSPM_01() throws Exception {
    SubscriptionInfoForm res = Mockito.spy(SubscriptionInfoForm.class);
    res.setInfraType("GPON");
    res.setInvestType(2l);
    res.setConnectorDeptCode("1");
    res.setStationCode("1");
    res.setStationDeptCode("1");
    MessageForm resQlt = Mockito.spy(MessageForm.class);
    SysUsersBO sysUsersBO = Mockito.spy(SysUsersBO.class);
    sysUsersBO.setUsername("1");
    List<SysUsersBO> list = Mockito.spy(ArrayList.class);
    list.add(sysUsersBO);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    listUser.add(usersInsideDto);
    ResultService resQltBccs = Mockito.spy(ResultService.class);
    resQltBccs.setResultCode(1L);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    PowerMockito.when(woRepository.getCdByUnitId(anyLong(), anyLong())).thenReturn(1L);
    PowerMockito.when(woRepository.getUnitCodeMapNims(anyString(), any())).thenReturn(unit);
    PowerMockito.when(qltPort.getDeptManageConnector(anyLong(), anyString(), anyString()))
        .thenReturn(resQltBccs);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUser);
    PowerMockito.when(resQlt.getLstUser()).thenReturn(list);
    PowerMockito.when(cdPort.getUserAssignByAccount(anyString())).thenReturn(resQlt);
    PowerMockito.when(wsNims.getSubscriptionInfo(anyString(), any())).thenReturn(res);
    woBusiness.getFtForSPM("1", "1");
  }

  @Test
  public void changeStatusWoCommon_01() throws Exception {
    WoUpdateStatusForm updateForm = Mockito.spy(WoUpdateStatusForm.class);
    updateForm.setWoCode("1");
    updateForm.setUserChange("1");
    updateForm.setReasonChange("1");
    updateForm.setSystemChange("CR");
    updateForm.setFinishTime("12/05/2020 03:05:06");
    updateForm.setNewStatus(8L);
    updateForm.setResultClose(0L);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setCdId(1L);
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> listUsers = Mockito.spy(ArrayList.class);
    listUsers.add(usersInsideDto);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUsers);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUsers);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);
    woBusiness.changeStatusWoCommon(updateForm);
  }

  @Test
  public void changeStatusWoCommon_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoUpdateStatusForm updateForm = Mockito.spy(WoUpdateStatusForm.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    updateForm.setWoCode("1");
    updateForm.setUserChange("1");
    updateForm.setReasonChange("1");
    updateForm.setSystemChange("SPM");
    updateForm.setFinishTime("12/05/2020 03:05:06");
    updateForm.setNewStatus(8L);
    updateForm.setResultClose(0L);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setCdId(1L);
    wo.setStatus(1L);
    wo.setFtId(1L);
    wo.setWoCode("1");
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> listUsers = Mockito.spy(ArrayList.class);
    listUsers.add(usersInsideDto);
    UsersEntity ue = Mockito.spy(UsersEntity.class);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(ue);
    PowerMockito.when(woRepository.findWoByWoCodeNoWait(anyString())).thenReturn(wo);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUsers);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUsers);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);

    try {
      woBusiness.changeStatusWoCommon(updateForm);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void changeStatusWoCommon_03() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoUpdateStatusForm updateForm = Mockito.spy(WoUpdateStatusForm.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    updateForm.setWoCode("1");
    updateForm.setUserChange("1");
    updateForm.setReasonChange("1");
    updateForm.setSystemChange("SPM");
    updateForm.setFinishTime("12/05/2020 03:05:06");
    updateForm.setNewStatus(0L);
    updateForm.setResultClose(0L);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setCdId(1L);
    wo.setStatus(8L);
    wo.setFtId(1L);
    wo.setWoCode("1");
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> listUsers = Mockito.spy(ArrayList.class);
    listUsers.add(usersInsideDto);
    UsersEntity ue = Mockito.spy(UsersEntity.class);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(ue);
    PowerMockito.when(woRepository.findWoByWoCodeNoWait(anyString())).thenReturn(wo);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUsers);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUsers);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);

    try {
      woBusiness.changeStatusWoCommon(updateForm);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void changeStatusWoCommon_04() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoUpdateStatusForm updateForm = Mockito.spy(WoUpdateStatusForm.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    updateForm.setWoCode("1");
    updateForm.setUserChange("1");
    updateForm.setReasonChange("1");
    updateForm.setSystemChange("TT");
    updateForm.setFinishTime("12/05/2020 03:05:06");
    updateForm.setNewStatus(0L);
    updateForm.setResultClose(0L);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setCdId(1L);
    wo.setStatus(8L);
    wo.setFtId(1L);
    wo.setWoCode("1");
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> listUsers = Mockito.spy(ArrayList.class);
    listUsers.add(usersInsideDto);
    UsersEntity ue = Mockito.spy(UsersEntity.class);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(ue);
    PowerMockito.when(woRepository.findWoByWoCodeNoWait(anyString())).thenReturn(wo);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUsers);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUsers);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);

    try {
      woBusiness.changeStatusWoCommon(updateForm);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void changeStatusWoCommon_05() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoUpdateStatusForm updateForm = Mockito.spy(WoUpdateStatusForm.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    updateForm.setWoCode("1");
    updateForm.setUserChange("1");
    updateForm.setReasonChange("1");
    updateForm.setSystemChange("BCCS_CC");
    updateForm.setFinishTime("12/05/2020 03:05:06");
    updateForm.setNewStatus(3L);
    updateForm.setResultClose(0L);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setWoId(1L);
    wo.setCdId(1L);
    wo.setStatus(8L);
    wo.setFtId(1L);
    wo.setWoCode("1");
    WoDetailDTO detail = Mockito.spy(WoDetailDTO.class);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> listUsers = Mockito.spy(ArrayList.class);
    listUsers.add(usersInsideDto);
    UsersEntity ue = Mockito.spy(UsersEntity.class);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(ue);
    PowerMockito.when(woRepository.findWoByWoCodeNoWait(anyString())).thenReturn(wo);
    PowerMockito.when(woRepository.getUserOfCD(anyLong())).thenReturn(listUsers);
    PowerMockito.when(userRepository.getListUserDTOByuserName(anyString())).thenReturn(listUsers);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(detail);
    PowerMockito.when(woRepository.findWoByIdNoWait(anyLong())).thenReturn(wo);

    try {
      woBusiness.changeStatusWoCommon(updateForm);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void updateWoResult() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoAutoCheckDTO autoCheckDTO = Mockito.spy(WoAutoCheckDTO.class);
    autoCheckDTO.setWoCode("1");
    autoCheckDTO.setResult("1");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(1L);
    UsersEntity ue = Mockito.spy(UsersEntity.class);
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    Map<String, String> mapConfigProperty = new HashMap<>();
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unit);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(ue);
    PowerMockito.when(woRepository.findWoByWoCodeNoWait(anyString())).thenReturn(wo);

    try {
      woBusiness.updateWoResult(autoCheckDTO);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void completeWoSPM() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    List<WoInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(woInsideDTO);
    woInsideDTO.setLstWo(list);
    woInsideDTO.toWoDTOSearch();
    woBusiness.completeWoSPM(woInsideDTO);
  }

  @Test
  public void setMapResultName() {
    Datatable dataResult = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listResult = Mockito.spy(ArrayList.class);
    listResult.add(catItemDTO);
    dataResult.setData(listResult);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataResult);
    woBusiness.setMapResultName();
  }

  @Test
  public void getResultName() {
    Datatable dataResult = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> listResult = Mockito.spy(ArrayList.class);
    listResult.add(catItemDTO);
    dataResult.setData(listResult);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataResult);
    woBusiness.getResultName("1");
  }

  @Test
  public void prepairWoToCreate() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Staff staff = Mockito.spy(Staff.class);
    staff.setStaffType(1L);
    staff.setUserName("1");
    List<Staff> lstStaff = Mockito.spy(ArrayList.class);
    lstStaff.add(staff);
    WoDTO dto = Mockito.spy(WoDTO.class);
    dto.setWoTypeId("1");
    WoCdGroupInsideDTO cdGroup = Mockito.spy(WoCdGroupInsideDTO.class);
    cdGroup.setWoGroupId(1L);
    PowerMockito.when(woRepository.getCdByFT(anyLong(), anyLong(), anyString()))
        .thenReturn(cdGroup);
    PowerMockito.when(userRepository.getUserId(anyString())).thenReturn(1L);
    woBusiness.prepairWoToCreate(lstStaff, dto);
  }

  @Test
  public void prepairWoToCreate_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Staff staff = Mockito.spy(Staff.class);
    staff.setStaffType(2L);
    staff.setUserName("1");
    List<Staff> lstStaff = Mockito.spy(ArrayList.class);
    lstStaff.add(staff);
    WoDTO dto = Mockito.spy(WoDTO.class);
    dto.setWoTypeId("1");
    WoCdGroupInsideDTO cdGroup = Mockito.spy(WoCdGroupInsideDTO.class);
    cdGroup.setWoGroupId(1L);
    PowerMockito.when(woRepository.getCdByFT(anyLong(), anyLong(), anyString()))
        .thenReturn(cdGroup);
    PowerMockito.when(userRepository.getUserId(anyString())).thenReturn(1L);
    woBusiness.prepairWoToCreate(lstStaff, dto);
  }

// @Test
//  public void exportFileTestService() {
//   PowerMockito.mockStatic(I18n.class);
//   PowerMockito.mockStatic(CommonImport.class);
//   PowerMockito.mockStatic(FileUtils.class);
//   PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
//   MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
//   ResultInSideDto result = woBusiness.exportFileTestService(null);
//   assertEquals(result.getKey(), "FILE_IS_NULL");
//
// }

  @Test
  public void exportFileTestService_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = woBusiness.exportFileTestService(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");

  }

  @Test
  public void exportFileTestService_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] header = new Object[]{"1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        1,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woBusiness.exportFileTestService(firstFile);
    assertEquals(result.getKey(), "NODATA");

  }

  @Test
  public void exportFileTestService_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] header = new Object[]{"1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    Object[] header1 = new Object[]{"1", "1"};
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 600; i++) {
      headerList1.add(header1);
    }
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        1,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        1,
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woBusiness.exportFileTestService(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");

  }

  @Test
  public void exportFileTestService_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] header = new Object[]{"1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    Object[] header1 = new Object[]{"1", "1"};
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList1.add(header1);
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setFileName("1");
    woInsideDTO.setWoSystem("1");
    woInsideDTO.setWoSystemId("1");
    woInsideDTO.setCreateDate(new Date());
    woInsideDTO.setWoContent("1");
    woInsideDTO.setWoId(1L);
    woInsideDTO.setWoSystem("PM");
    woInsideDTO.setWoSystemId("1");
    woInsideDTO.setWoSystem("CR");
    woInsideDTO.setWoSystemId("1");
    woInsideDTO.setFileName("1");
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    crInsiteDTO.setTitle("1");
    crInsiteDTO.setCreatedDate(new Date());
    crInsiteDTO.setCrNumber("1");
    crInsiteDTO.setTitle("1");
    crInsiteDTO.setCreatedDate(new Date());
    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    crImpactedNodesDTO.setDeviceCode("1");
    crImpactedNodesDTO.setIp("1");
    List<CrImpactedNodesDTO> listImpactNode = Mockito.spy(ArrayList.class);
    listImpactNode.add(crImpactedNodesDTO);
//    PowerMockito.when(crServiceProxy.getListCrImpactedNodesDTO(any())).thenReturn(listImpactNode);

    PowerMockito.when(crServiceProxy.findCrByIdProxy(anyLong())).thenReturn(crInsiteDTO);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        1,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        1,
        1000
    )).thenReturn(headerList1);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(woInsideDTO);
    ResultInSideDto result = woBusiness.exportFileTestService(firstFile);
    assertEquals(result.getKey(), "ERROR");

  }

  @Test
  public void checkWoType_01() {
    PowerMockito.mockStatic(I18n.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setUserTypeCode("1");
    woTypeInsideDTO.setAllowPending(0L);
    PowerMockito.when(woTypeBusiness.findWoTypeById(anyLong())).thenReturn(woTypeInsideDTO);
    woBusiness.checkWoType(1L, 1L);
  }

  @Test
  public void checkWoType_02() {
    PowerMockito.mockStatic(I18n.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setUserTypeCode("1");
    woTypeInsideDTO.setAllowPending(0L);
    woTypeInsideDTO.setEnableCreate(0L);
    PowerMockito.when(woTypeBusiness.findWoTypeById(anyLong())).thenReturn(woTypeInsideDTO);
    woBusiness.checkWoType(1L, 2L);
  }

  @Test
  public void updateWoResult_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoAutoCheckDTO autoCheckDTO = Mockito.spy(WoAutoCheckDTO.class);
    autoCheckDTO.setResult("OK");
    autoCheckDTO.setWoCode("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setFtId(1L);
    wo.setStatus(7L);
    wo.setWoSystem("SPM");
    wo.setWoCode("1");
    wo.setWoId(1L);
    wo.setWoTypeId(1L);
    wo.setFinishTime(DateTimeUtils.convertStringToDate("05/14/2020 06:06:06"));
    wo.setStartTime(DateTimeUtils.convertStringToDate("05/14/2029 06:06:06"));
    wo.setEndTime(DateTimeUtils.convertStringToDate("05/14/2029 06:06:06"));

    UsersEntity ue = Mockito.spy(UsersEntity.class);
    ue.setUnitId(1L);
    Map<String, String> mapConfigProperty = new HashMap<>();
    mapConfigProperty.put("WO_TYPE_CDBR", "1,1");
    WoDetailDTO woDetail = Mockito.spy(WoDetailDTO.class);
    woDetail.setAccountIsdn("1");
    ResultInSideDto rsDeducteIM = Mockito.spy(ResultInSideDto.class);
    rsDeducteIM.setKey("SUCCESS");
    UnitDTO unit = Mockito.spy(UnitDTO.class);
    unit.setUnitName("1");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unit);
    PowerMockito.when(woMaterialDeducteBusiness.putMaterialDeducteToIM(anyLong(), anyBoolean()))
        .thenReturn(rsDeducteIM);
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfigProperty);
    PowerMockito.when(woDetailRepository.findWoDetailById(anyLong())).thenReturn(woDetail);
    PowerMockito.when(userRepository.getUserByUserIdCheck(anyLong())).thenReturn(ue);
    PowerMockito.when(woRepository.findWoByWoCodeNoWait(anyString())).thenReturn(wo);
    try {
      woBusiness.updateWoResult(autoCheckDTO);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

//  @Test
//  public void updateFileForWo_01() throws Exception{
//    PowerMockito.mockStatic(Base64.class);
//    PowerMockito.mockStatic(FileUtils.class);
//    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
//    List<String> list = Mockito.spy(ArrayList.class);
//    list.add("1");
//    List<byte[]> list1 = Arrays.asList("abc".getBytes());
//    WoDTO woDTO = Mockito.spy(WoDTO.class);
//    woDTO.setWoCode("1");
//    woDTO.setListFileName(list);
//    woDTO.setFileArr(list1);
//    woDTO.setCreateDate((new Date()).toString());
//    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
//    wo.setCreatePersonId(1L);
//    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
//    usersEntity.setUnitId(1L);
//    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
//    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
//    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
//    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
//    byte[] fileContent = new byte[16];
//    for (int i = 0; i < 16; i++) {
//      fileContent[i] += i;
//    }
//    list1.add(fileContent);
//    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
//        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
//    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
//        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
//    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
//        "6cf8a37f3e6993f90eef71859b1ebf31", "/ftpFolder", woDTO.getListFileName().get(0), fileContent,FileUtils.createDateOfFileName(wo.getCreateDate())))
//        .thenReturn("trungduong");
//    woBusiness.updateFileForWo(woDTO);
//  }

  @Test
  public void actionSendSms_01() {
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    woBusiness.actionSendSms(wo, "1", 1L);
  }

//  @Test
//  public void splitWoFromWeb_01() throws Exception {
//    WoBusinessImpl classUnderTest = PowerMockito.spy(woBusiness);
//    PowerMockito.mockStatic(I18n.class);
//    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
//    List<String> list = Mockito.spy(ArrayList.class);
//    PowerMockito.mockStatic(FileUtils.class);
//    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
//    list.add("1");
//    List<Boolean> list1 = Mockito.spy(ArrayList.class);
//    list1.add(true);
//    WoTestServiceMapDTO woTestServiceMapDTO = Mockito.spy(WoTestServiceMapDTO.class);
//    List<WoTestServiceMapDTO> woTestServiceMapDTOList = Mockito.spy(ArrayList.class);
//    woTestServiceMapDTOList.add(woTestServiceMapDTO);
//    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
//    woInsideDTO.setLstUsername(list);
//    woInsideDTO.setLstDescription(list);
//    woInsideDTO.setGetFileParent(list1);
//    woInsideDTO.setLstWoTestService(woTestServiceMapDTOList);
//    woInsideDTO.setCreateDate(new Date());
//    woInsideDTO.setFileName("1");
//    woInsideDTO.setCdIdList(list);
//    woInsideDTO.setParentWo(woInsideDTO);
//    PowerMockito.mockStatic(TicketProvider.class);
//    UserToken userToken = Mockito.spy(UserToken.class);
//    userToken.setUserName("thanhlv12");
//    userToken.setUserID(1L);
//    userToken.setDeptId(1L);
//    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
//    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
//        "some xml".getBytes());
//    List<MultipartFile> fileAttacks = Mockito.spy(ArrayList.class);
//    fileAttacks.add(firstFile);
//    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
//    UsersEntity u = Mockito.spy(UsersEntity.class);
//    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
//    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
//    gnocFileDtos.add(gnocFileDto);
//    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(gnocFileDtos);
//    PowerMockito.when(userBusiness.getUserByUserName(anyString())).thenReturn(u);
//    PowerMockito.when(woRepository.getSeqTableWo(anyString())).thenReturn("1");
//    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
//    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
//        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
//    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
//        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
//    PowerMockito.when(FileUtils.saveFtpFile("10.240.232.25", 21, "723298837f9c6fe9953c6e07e0e4df17",
//        "6cf8a37f3e6993f90eef71859b1ebf31", "/ftpFolder", firstFile.getOriginalFilename(),
//        firstFile.getBytes(), FileUtils.createDateOfFileName(woInsideDTO.getCreateDate())))
//        .thenReturn("trungduong");
//    WoDTO woDTO = Mockito.spy(WoDTO.class);
//    woDTO.setWoId("1");
//    PowerMockito.doReturn(woDTO).when(classUnderTest, "findWoById", any());
//    woBusiness.splitWoFromWeb(fileAttacks, woInsideDTO);
//  }

  @Test
  public void updateFile() throws Exception {
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    List<String> listFileName = Mockito.spy(ArrayList.class);
    listFileName.add("a");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    List<byte[]> fileArr = Arrays.asList("trungduong".getBytes());
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    woBusiness.updateFile(woInsideDTO, 1L, listFileName, fileArr);
  }

  @Test
  public void insertWoForSPM() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("");
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    woDTO.setWoId("1");
    woDTO.setFtId("1");
    woDTO.setWoSystem("SPM");
    woDTO.setNotGetFileCfgWhenCreate("2");
    woDTO.setCustomerTimeDesireFrom("15/05/2021 02:17:03");
    woDTO.setEndPendingTime("15/05/2021 02:17:03");
    woDTO.setEndTime("15/05/2021 02:17:03");
    woDTO.setWoTypeId("1");
    woDTO.setPriorityId("1");
    woDTO.setStartTime("15/08/2020 02:17:03");
    woDTO.setCdId("1");
    woDTO.setCreatePersonId("1");
    woDTO.setWoTypeCode("WO_TYPE_KBDV_QLCTKT");
    WoMerchandiseDTO merchandiseDTO = Mockito.spy(WoMerchandiseDTO.class);
    List<WoMerchandiseDTO> list2 = Mockito.spy(ArrayList.class);
    list2.add(merchandiseDTO);
    woDTO.setLstMerchandise(list2);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    usersEntity.setUsername("1");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    List<GnocFileDto> gnocFileGuideDtosOld = Mockito.spy(ArrayList.class);
    gnocFileGuideDtosOld.add(gnocFileDto);
    List<String> list = Mockito.spy(ArrayList.class);
    list.add("a");
    woDTO.setListFileName(list);
    List<byte[]> list1 = Arrays.asList("trungduong".getBytes());
    woDTO.setFileArr(list1);
    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey("SUCCESS");
    PowerMockito.when(woMerchandiseRepository.updateListWoMerchandise(any())).thenReturn(result);
    PowerMockito.when(woWorklogRepository.insertWoWorklog(any())).thenReturn(result);
    PowerMockito.when(woHistoryRepository.insertWoHistory(any())).thenReturn(result);
    PowerMockito.when(woRepository.insertWo(any())).thenReturn(result);
    PowerMockito.when(woDetailRepository.insertUpdateWoDetail(any())).thenReturn(result);
    PowerMockito.when(woDeclareServiceRepository.insertOrUpdateWoDeclareService(any()))
        .thenReturn(result);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any()))
        .thenReturn(gnocFileGuideDtosOld);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(woRepository.getSeqTableWo("WO_SEQ")).thenReturn("1");
    PowerMockito.when(userRepository.getUserName(anyLong())).thenReturn("1");
    try {
      ResultDTO result3 = woBusiness.insertWoForSPM(woDTO);
      assertEquals(result3.getKey(), "SUCCESS");
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Test
  public void updateFileForWo() {
    List<String> list = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    list.add("a");
    List<byte[]> list1 = Arrays.asList("trungduong".getBytes());
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setListFileName(list);
    woDTO.setFileArr(list1);
    woDTO.setWoCode("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setCreatePersonId(1L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(woRepository.getWoByWoCode(anyString())).thenReturn(wo);
    woBusiness.updateFileForWo(woDTO);
  }

  @Test
  public void updateWo_01() throws Exception {
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setWoId("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    woDTO.setCreatePersonId("1");
    woDTO.setWoTypeId("1");
    woDTO.setCreateDate("05/15/2020 05:05:05");
    woDTO.setWoId("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setCreatePersonId(1L);
    wo.setWoId(1L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    usersEntity.setUsername("1");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("1");
    gnocFileDto.setFileName("1");
    List<GnocFileDto> gnocFileGuideDtosOld = Mockito.spy(ArrayList.class);
    gnocFileGuideDtosOld.add(gnocFileDto);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("1");
    PowerMockito.when(woRepository.updateWo(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woRepository.findWoByIdNoOffset(anyLong())).thenReturn(wo);
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any()))
        .thenReturn(gnocFileGuideDtosOld);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    woBusiness.updateWo(woDTO);
  }

  @Test
  public void autoCreateWOThuHoiNims() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(JAXBContext.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setDistance("1");
    wo.setCableCode("1");
    wo.setCableTypeCode("1");
    wo.setStationCode("1");
    wo.setConstructionCode("1");
    wo.setCdAssignId(1L);
    wo.setCreatePersonId(1L);
    wo.setLineCode("1");
    wo.setCdId(1L);
    wo.setFtId(1L);
    UsersEntity ft = Mockito.spy(UsersEntity.class);
    ft.setUsername("1");
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(ft);
    woBusiness.autoCreateWOThuHoiNims(wo);
  }

  @Test
  public void updateKeyValueExcel_1() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    ObjKeyValue objKeyValue = Mockito.spy(ObjKeyValue.class);
    List<ObjKeyValue> getLstDataKeyValue = Mockito.spy(ArrayList.class);
    getLstDataKeyValue.add(objKeyValue);
    WoInsideDTO wo = Mockito.spy(WoInsideDTO.class);
    wo.setCreateDate(new Date());
    wo.setFileName("1,1");
    String filePath = "./temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    UsersInsideDto ft = Mockito.spy(UsersInsideDto.class);
    woBusiness.updateKeyValueExcel(getLstDataKeyValue, wo, ft);
  }

  @Test
  public void testGetTemplateImportAssetsForWo() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    File actual = woBusiness.getTemplateImportAssetsForWo();

    Assert.assertNotNull(actual);
  }

  @Test
  public void testGetTemplateImportWoTranSferOfProperty() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    File actual = woBusiness.getTemplateImportWoTranSferOfProperty();

    Assert.assertNotNull(actual);
  }

  @Test
  public void testGetTemplateImportWoDowngradeWithDrawal() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    File actual = woBusiness.getTemplateImportWoDowngradeWithDrawal();

    Assert.assertNotNull(actual);
  }

  @Test
  public void testGetTemplateImportWoUpgradeStation() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TEST");

    File actual = woBusiness.getTemplateImportWoUpgradeStation();

    Assert.assertNotNull(actual);
  }

}


