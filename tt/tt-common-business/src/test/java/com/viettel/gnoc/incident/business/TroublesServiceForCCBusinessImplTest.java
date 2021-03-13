
package com.viettel.gnoc.incident.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.SpringApplicationContext;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmDTO;
import com.viettel.gnoc.commons.dto.ConcaveDTO;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.CfgInfoTtSpmRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.TROUBLE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.CommonDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroublesEntity;
import com.viettel.gnoc.incident.repository.ItAccountRepository;
import com.viettel.gnoc.incident.repository.ItSpmInfoRepository;
import com.viettel.gnoc.incident.repository.TroubleActionLogsRepository;
import com.viettel.gnoc.incident.repository.TroubleNodeRepository;
import com.viettel.gnoc.incident.repository.TroubleWireRepository;
import com.viettel.gnoc.incident.repository.TroubleWorklogRepository;
import com.viettel.gnoc.incident.repository.TroublesRepository;
import com.viettel.gnoc.incident.repository.TroublesServiceForCCRepository;
import com.viettel.gnoc.incident.utils.TroubleBccsUtils;
import com.viettel.gnoc.incident.utils.TroubleTktuUtils;
import com.viettel.gnoc.ws.provider.WSCxfInInterceptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.WebServiceContext;
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
@PrepareForTest({TroublesServiceForCCBusinessImpl.class, SpringApplicationContext.class,
    FileUtils.class, WSCxfInInterceptor.class,
    CommonExport.class, I18n.class, DataUtil.class, TroubleBccsUtils.class, TroubleBccsUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class TroublesServiceForCCBusinessImplTest {

  @InjectMocks
  TroublesServiceForCCBusinessImpl troublesServiceForCCBusiness;

  @Mock
  TroublesServiceForCCRepository troublesServiceForCCRepository;

  @Mock
  LanguageExchangeRepository languageExchangeRepository;

  @Mock
  TroublesRepository troublesRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  CfgInfoTtSpmRepository cfgInfoTtSpmRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  CommonRepository commonRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  TroubleWorklogRepository troubleWorklogRepository;

  @Mock
  TroubleNodeRepository troubleNodeRepository;

  @Mock
  TroubleWireRepository troubleWireRepository;

  @Mock
  ItAccountRepository itAccountRepository;

  @Mock
  ItSpmInfoRepository itSpmInfoRepository;

  @Mock
  TroubleActionLogsRepository troubleActionLogsRepository;

  @Mock
  MessagesRepository messagesRepository;

  @Mock
  TroublesBusiness troublesBusiness;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  TroubleBccsUtils troubleBccsUtils;

  @Mock
  TroubleTktuUtils troubleTktuUtils;

  @Mock
  WOCreateBusiness woCreateBusiness;

  @Mock
  UserBusiness userBusiness;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Test
  public void onRollBackTroubleForCC_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT TEST");

    List<CommonDTO> lstComplaint = Mockito.spy(ArrayList.class);
    List<ResultDTO> actual = troublesServiceForCCBusiness
        .onRollBackTroubleForCC(lstComplaint);

    Assert.assertEquals(RESULT.FAIL, actual.get(0).getKey());
  }

  @Test
  public void onRollBackTroubleForCC_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    List<TroublesInSideDTO> troubleDTOs = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        troublesServiceForCCRepository
            .getTroubleDTOForRollback(any(), anyString(), anyString(), anyString())
    ).thenReturn(troubleDTOs);

    CommonDTO commonDTO1 = Mockito.spy(CommonDTO.class);
    CommonDTO commonDTO2 = Mockito.spy(CommonDTO.class);
    commonDTO2.setComplaintId("111");
    List<CommonDTO> lstComplaint = Mockito.spy(ArrayList.class);
    lstComplaint.add(commonDTO1);
    lstComplaint.add(commonDTO2);
    List<ResultDTO> actual = troublesServiceForCCBusiness
        .onRollBackTroubleForCC(lstComplaint);

    Assert.assertEquals(RESULT.FAIL, actual.get(0).getKey());
    Assert.assertEquals(RESULT.SUCCESS, actual.get(1).getKey());
  }

  @Test
  public void onRollBackTroubleForCC_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(1L);
    troublesInSideDTO.setWoCode("1111");
    List<TroublesInSideDTO> troubleDTOs = Mockito.spy(ArrayList.class);
    troubleDTOs.add(troublesInSideDTO);
    PowerMockito.when(
        troublesServiceForCCRepository
            .getTroubleDTOForRollback(any(), anyString(), anyString(), anyString())
    ).thenReturn(troubleDTOs);

    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    PowerMockito.when(
        woServiceProxy.deleteWOForRollbackProxy(anyString(), anyString(), anyString())
    ).thenReturn(resultDTO);

    CommonDTO commonDTO1 = Mockito.spy(CommonDTO.class);
    CommonDTO commonDTO2 = Mockito.spy(CommonDTO.class);
    commonDTO2.setComplaintId("111");
    List<CommonDTO> lstComplaint = Mockito.spy(ArrayList.class);
    lstComplaint.add(commonDTO1);
    lstComplaint.add(commonDTO2);
    List<ResultDTO> actual = troublesServiceForCCBusiness
        .onRollBackTroubleForCC(lstComplaint);

    Assert.assertEquals(RESULT.FAIL, actual.get(0).getKey());
  }

  @Test
  public void onSearchCountForCC() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    int expected = 1;
    PowerMockito.when(
        troublesServiceForCCRepository.onSearchCountForCC(troublesDTO)
    ).thenReturn(expected);
    int actual = troublesServiceForCCBusiness
        .onSearchCountForCC(troublesDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void onSearchForCC() {
    PowerMockito.mockStatic(WSCxfInInterceptor.class);
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    PowerMockito.when(WSCxfInInterceptor.getLanguage()).thenReturn("vi_VN");
    List<TroublesDTO> actual = troublesServiceForCCBusiness.onSearchForCC(troublesDTO, 1, 1);
  }

  @Test
  public void getTroubleInfoForCC_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);

    ResultDTO actual = troublesServiceForCCBusiness.getTroubleInfoForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void getTroubleInfoForCC_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    ResultDTO actual = troublesServiceForCCBusiness.getTroubleInfoForCC(null);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void getTroubleInfoForCC_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("1111");
    troublesDTO.setTroubleId("1");
    List<TroublesDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(troublesDTO);

    List<GnocFileDto> lstFile = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        troublesServiceForCCRepository.getTroubleInfo(any())
    ).thenReturn(lst);
    PowerMockito.when(
        troublesRepository.getTroubleFileDTO(any())
    ).thenReturn(lstFile);

    ResultDTO actual = troublesServiceForCCBusiness.getTroubleInfoForCC(troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onInsertTroubleForCC_01() {
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    ResultDTO actual = troublesServiceForCCBusiness
        .onInsertTroubleForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onInsertTroubleForCC_02() {
    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setInsertSource("InsertSource");
    ResultDTO actual = troublesServiceForCCBusiness
        .onInsertTroubleForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onInsertTroubleForCC_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    CatItemDTO prio = Mockito.spy(CatItemDTO.class);
    prio.setItemId(1L);
    prio.setItemName(Constants.TT_ARRAY.SPM_PAKH_CDBR);
    prio.setItemCode("2222");
    prio.setCategoryCode(Constants.TROUBLE.PRIORITY);
    prio.setParentItemId(1L);
    List<CatItemDTO> lstCat = Mockito.spy(ArrayList.class);
    lstCat.add(prio);
    CatItemDTO prio1 = Mockito.spy(CatItemDTO.class);
    prio1.setItemId(1L);
    prio1.setItemName(Constants.TT_ARRAY.SPM_PAKH_CDBR);
    prio1.setItemCode("2222");
    prio1.setCategoryCode(TROUBLE.PT_TYPE);
    prio1.setParentItemId(1L);
    List<CatItemDTO> lstCat1 = Mockito.spy(ArrayList.class);
    lstCat1.add(prio1);
    PowerMockito.when(
        catItemRepository.getListCatItemDTO(any())
    ).thenReturn(lstCat).thenReturn(lstCat).thenReturn(lstCat).thenReturn(lstCat1)
        .thenReturn(lstCat).thenReturn(lstCat).thenReturn(lstCat).thenReturn(lstCat)
        .thenReturn(lstCat);
    PowerMockito.when(catItemRepository.getListItemByCategoryAndParent(any(), any()))
        .thenReturn(lstCat);

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setInsertSource("InsertSource");
    troublesDTO.setTroubleName("TroubleName");
    troublesDTO.setPriorityId("1111");
    troublesDTO.setBeginTroubleTime("05/05/2020 15:30:00");
    troublesDTO.setEndTroubleTime("07/05/2020 15:30:00");
    troublesDTO.setDeferredTime("06/05/9999 15:30:00");
    troublesDTO.setDeferredReason("DeferredReason");
    troublesDTO.setAffectedService("2222");
    troublesDTO.setDescription("Description");
    troublesDTO.setTypeId("1");
    troublesDTO.setSubCategoryId("1");
    troublesDTO.setTypeName(Constants.TT_ARRAY.SPM_PAKH_CDBR);
    troublesDTO.setAutoCreateWO(1L);
    troublesDTO.setLocationId("1");
    troublesDTO.setComplaintGroupId("1");
    troublesDTO.setComplaintTypeId("1");
    troublesDTO.setTimeProcess("1");
    troublesDTO.setProcessingUserName("1");
    troublesDTO.setProcessingUnitName("1");
    troublesDTO.setTypeUnit("1");

    PowerMockito.when(catLocationRepository.getLocationByCode(any(), any(), any()))
        .thenReturn(new CatLocationDTO());
    CfgUnitTtSpmDTO cfgUnitTtSpmDTO = Mockito.spy(CfgUnitTtSpmDTO.class);
    cfgUnitTtSpmDTO.setUnitId("1");
    PowerMockito.when(troublesRepository.getUnitByLocation(any(), any(), any())).thenReturn(cfgUnitTtSpmDTO);
    List<UnitDTO> u = Mockito.spy(ArrayList.class);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    u.add(unitDTO);
    PowerMockito.when(unitRepository.getUnitByUnitDTO(any())).thenReturn(u);
    List<String> lstString = Mockito.spy(ArrayList.class);
    lstString.add("1");
    PowerMockito.when(troublesRepository.getSequenseTroubles(anyString(), any())).thenReturn(lstString);
    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setMessage(RESULT.SUCCESS);
    PowerMockito.when(woCreateBusiness.createWOForCC(any(), any())).thenReturn(resultWO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);
    PowerMockito.when(troublesRepository.insertTroubles(any())).thenReturn(resultInSideDto);

    ResultDTO actual = troublesServiceForCCBusiness
        .onInsertTroubleForCC(troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void findTroublesById() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesEntity troublesEntity = Mockito.spy(TroublesEntity.class);
    PowerMockito.when(
        troublesRepository.findTroublesById(anyLong())
    ).thenReturn(troublesEntity);

    TroublesInSideDTO actual = troublesServiceForCCBusiness.findTroublesById(1L);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getSequenseTroubles() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    int[] size = {1, 2, 3};
    List<String> actual = troublesServiceForCCBusiness.getSequenseTroubles("UnitTest", size);

    Assert.assertNotNull(actual);
  }

  @Test
  public void reassignTicketForCC_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("06/05/2020 12:00:00");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("05/05/2020 12:00:00");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("05/05/2020 12:00:00");
    troublesDTO.setTimeProcess("0");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("05/05/2020 12:00:00");
    troublesDTO.setTimeProcess("24");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_07() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("05/05/2020 12:00:00");
    troublesDTO.setTimeProcess("24");
    troublesDTO.setProcessingUserName("ProcessingUserName");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_08() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("05/05/2020 12:00:00");
    troublesDTO.setTimeProcess("24");
    troublesDTO.setProcessingUserName("ProcessingUserName");
    troublesDTO.setProcessingUnitName("ProcessingUnitName");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_09() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("05/05/2020 12:00:00");
    troublesDTO.setTimeProcess("24");
    troublesDTO.setProcessingUserName("ProcessingUserName");
    troublesDTO.setProcessingUnitName("ProcessingUnitName");
    troublesDTO.setRejectReason("RejectReason");
    troublesDTO.setDeferredTime("05/05/2020 11:00:00");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_10() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("05/05/2020 12:00:00");
    troublesDTO.setTimeProcess("24");
    troublesDTO.setProcessingUserName("ProcessingUserName");
    troublesDTO.setProcessingUnitName("ProcessingUnitName");
    troublesDTO.setRejectReason("RejectReason");
    troublesDTO.setDeferredTime("05/05/2020 23:00:00");
    troublesDTO.setReceiveUnitId("222");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_11() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("UnitName");
    unitDTO.setUnitCode("UnitCode");
    List<UnitDTO> u = Mockito.spy(ArrayList.class);
    u.add(unitDTO);
    PowerMockito.when(
        unitRepository.getUnitByUnitDTO(any())
    ).thenReturn(u);

    TroublesInSideDTO troublesDTO1 = Mockito.spy(TroublesInSideDTO.class);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), anyString(), anyString(), anyString())
    ).thenReturn(troublesDTO1);

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("05/05/2020 12:00:00");
    troublesDTO.setTimeProcess("24");
    troublesDTO.setProcessingUserName("ProcessingUserName");
    troublesDTO.setProcessingUnitName("ProcessingUnitName");
    troublesDTO.setRejectReason("RejectReason");
    troublesDTO.setDeferredTime("05/05/2020 23:00:00");
    troublesDTO.setReceiveUnitId("222");
    troublesDTO.setComplaintId("333");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_12() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("UnitName");
    unitDTO.setUnitCode("UnitCode");
    List<UnitDTO> u = Mockito.spy(ArrayList.class);
    u.add(unitDTO);
    PowerMockito.when(
        unitRepository.getUnitByUnitDTO(any())
    ).thenReturn(u);

    TroublesInSideDTO troublesInsideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInsideDTO.setTroubleId(2L);
    troublesInsideDTO.setState(3L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), anyString(), anyString(), anyString())
    ).thenReturn(troublesInsideDTO);

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("05/05/2020 12:00:00");
    troublesDTO.setTimeProcess("24");
    troublesDTO.setProcessingUserName("ProcessingUserName");
    troublesDTO.setProcessingUnitName("ProcessingUnitName");
    troublesDTO.setRejectReason("RejectReason");
    troublesDTO.setDeferredTime("05/05/2020 23:00:00");
    troublesDTO.setReceiveUnitId("222");
    troublesDTO.setComplaintId("333");
    troublesDTO.setCreatedTimeFrom("05/05/2020 12:00:00");
    troublesDTO.setCreatedTimeTo("06/05/2020 12:00:00");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_13() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("UnitName");
    unitDTO.setUnitCode("UnitCode");
    List<UnitDTO> u = Mockito.spy(ArrayList.class);
    u.add(unitDTO);
    PowerMockito.when(
        unitRepository.getUnitByUnitDTO(any())
    ).thenReturn(u);

    TroublesInSideDTO troublesInsideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInsideDTO.setTroubleId(2L);
    troublesInsideDTO.setState(10L);
    troublesInsideDTO.setWoCode("123123");
    troublesInsideDTO.setCreateUnitId(5L);
    troublesInsideDTO.setCreateUserId(6L);
    troublesInsideDTO.setTroubleCode("222");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), anyString(), anyString(), anyString())
    ).thenReturn(troublesInsideDTO);

    ResultDTO resultWoDTO = Mockito.spy(ResultDTO.class);
    resultWoDTO.setMessage(RESULT.SUCCESS);
    PowerMockito.when(
        woServiceProxy.changeStatusWoProxy(any())
    ).thenReturn(resultWoDTO);

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        troubleActionLogsRepository.insertTroubleActionLogs(any())
    ).thenReturn(result);

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("05/05/2020 12:00:00");
    troublesDTO.setTimeProcess("24");
    troublesDTO.setProcessingUserName("ProcessingUserName");
    troublesDTO.setProcessingUnitName("ProcessingUnitName");
    troublesDTO.setRejectReason("RejectReason");
    troublesDTO.setDeferredTime("05/05/2020 23:00:00");
    troublesDTO.setReceiveUnitId("222");
    troublesDTO.setComplaintId("333");
    troublesDTO.setCreatedTimeFrom("05/05/2020 12:00:00");
    troublesDTO.setCreatedTimeTo("06/05/2020 12:00:00");
    troublesDTO.setInsertSource("InsertSource");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void reassignTicketForCC_14() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("UnitName");
    unitDTO.setUnitCode("UnitCode");
    List<UnitDTO> u = Mockito.spy(ArrayList.class);
    u.add(unitDTO);
    PowerMockito.when(
        unitRepository.getUnitByUnitDTO(any())
    ).thenReturn(u);

    TroublesInSideDTO troublesInsideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInsideDTO.setTroubleId(2L);
    troublesInsideDTO.setState(7L);
    troublesInsideDTO.setWoCode("123123");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), anyString(), anyString(), anyString())
    ).thenReturn(troublesInsideDTO);

    ResultDTO resultWoDTO = Mockito.spy(ResultDTO.class);
    resultWoDTO.setMessage("Message");
    resultWoDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        woServiceProxy.changeStatusWoProxy(any())
    ).thenReturn(resultWoDTO);
    PowerMockito.when(
        woServiceProxy.updatePendingWo(any())
    ).thenReturn(resultWoDTO);

    TroublesDTO troublesDTO = Mockito.spy(TroublesDTO.class);
    troublesDTO.setTroubleCode("111");
    troublesDTO.setBeginTroubleTime("05/05/2020 12:00:00");
    troublesDTO.setTimeProcess("24");
    troublesDTO.setProcessingUserName("ProcessingUserName");
    troublesDTO.setProcessingUnitName("ProcessingUnitName");
    troublesDTO.setRejectReason("RejectReason");
    troublesDTO.setDeferredTime("05/05/2020 23:00:00");
    troublesDTO.setReceiveUnitId("222");
    troublesDTO.setComplaintId("333");
    troublesDTO.setCreatedTimeFrom("05/05/2020 12:00:00");
    troublesDTO.setCreatedTimeTo("06/05/2021 12:00:00");
    ResultDTO actual = troublesServiceForCCBusiness
        .reassignTicketForCC(troublesDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void getListTroubleActionLog() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<TroubleActionLogsDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        troublesServiceForCCRepository.getListTroubleActionLog(anyString())
    ).thenReturn(expected);

    List<TroubleActionLogsDTO> actual = troublesServiceForCCBusiness
        .getListTroubleActionLog("TroubleCode");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getListWorkLog() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<TroubleWorklogInsiteDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        troublesServiceForCCRepository
            .searchByConditionBean(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(expected);

    List<TroubleWorklogInsiteDTO> actual = troublesServiceForCCBusiness
        .getListWorkLog("TroubleCode");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getConcaveByTicket() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    String expected = "temp";
    PowerMockito.when(
        troubleTktuUtils.getConcaveByTicket(anyString())
    ).thenReturn(expected);

    String actual = troublesServiceForCCBusiness
        .getConcaveByTicket("TroubleCode");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sendTicketToTKTU() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultDTO expected = Mockito.spy(ResultDTO.class);
    PowerMockito.when(
        troubleTktuUtils.sendTicketToTKTU(any())
    ).thenReturn(expected);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    ResultDTO actual = troublesServiceForCCBusiness
        .sendTicketToTKTU(tForm);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getConcaveByCellAndLocation() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Map<String, String> mapProperty = Mockito.spy(HashMap.class);
    PowerMockito.when(
        troublesServiceForCCRepository.getConfigProperty()
    ).thenReturn(mapProperty);
    List<ConcaveDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        troubleTktuUtils.getConcaveByCellAndLocation(any(), anyString(), anyString(), anyString())
    ).thenReturn(expected);

    List<String> lstCell = Mockito.spy(ArrayList.class);
    lstCell.add("aaa");
    lstCell.add("bbb");
    List<ConcaveDTO> actual = troublesServiceForCCBusiness
        .getConcaveByCellAndLocation(lstCell, "lng", "lat");

    Assert.assertEquals(expected.size(), actual.size());
  }

  @Test
  public void getListUnitByTrouble() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<UnitDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        troublesServiceForCCRepository.getListUnitByTrouble(anyString())
    ).thenReturn(expected);

    List<UnitDTO> actual = troublesServiceForCCBusiness
        .getListUnitByTrouble("TroubleCode");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void onUpdateTroubleFromTKTU_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_06() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_07() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_08() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setConcave("Concave");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_09() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setConcave("Concave");
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("05/06/2019"));
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_10() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setPriorityCode(Constants.TT_PRIORITY.TT_Minor);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setConcave("Concave");
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("05/06/3000"));
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_11() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setPriorityCode(Constants.TT_PRIORITY.TT_Major);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setConcave("Concave");
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("05/06/3000"));
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_12() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setPriorityCode(Constants.TT_PRIORITY.TT_Critical);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setConcave("Concave");
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("05/06/3000"));
    tForm.setCellService("CellService");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_13() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setPriorityCode(Constants.TT_PRIORITY.TT_Critical);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setConcave("Concave");
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("05/06/3000"));
    tForm.setCellService("CellService");
    tForm.setLongitude("Longitude");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_14() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setPriorityCode(Constants.TT_PRIORITY.TT_Critical);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setConcave("Concave");
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("05/06/3000"));
    tForm.setCellService("CellService");
    tForm.setLongitude("100");
    tForm.setLatitude("Latitude");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_15() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setPriorityCode(Constants.TT_PRIORITY.TT_Critical);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setConcave("Concave");
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("05/06/3000"));
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("5");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_16() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setPriorityCode(Constants.TT_PRIORITY.TT_Critical);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setConcave("Concave");
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("05/06/3000"));
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_17() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setPriorityCode(Constants.TT_PRIORITY.TT_Critical);
    troubleDTO.setCreateUnitId(2L);
    troubleDTO.setWorkLog("WorkLog");
    troubleDTO.setWorkArround("WorkArround");
    troubleDTO.setState(8L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setConcave("Concave");
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("05/06/3000"));
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setWorkArround("WorkArround");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_18() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setPriorityCode(Constants.TT_PRIORITY.TT_Critical);
    troubleDTO.setCreateUnitId(2L);
    troubleDTO.setWorkLog("WorkLog");
    troubleDTO.setWorkArround("WorkArround");
    troubleDTO.setState(7L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        troublesBusiness.onUpdateTrouble(any(), anyBoolean())
    ).thenReturn(expected);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setConcave("Concave");
    tForm.setEstimateTime(DateTimeUtils.convertStringToDate("05/06/3000"));
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setWorkArround("WorkArround");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertNotNull(actual);
  }

  @Test
  public void onUpdateTroubleFromTKTU_19() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);

    troubleDTO.setCreateUnitId(2L);
    troubleDTO.setWorkLog("WorkLog");
    troubleDTO.setWorkArround("WorkArround");
    troubleDTO.setState(10L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        troublesBusiness.onUpdateTrouble(any(), anyBoolean())
    ).thenReturn(expected);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_20() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);

    troubleDTO.setCreateUnitId(2L);
    troubleDTO.setWorkLog("WorkLog");
    troubleDTO.setWorkArround("WorkArround");
    troubleDTO.setState(10L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        troublesBusiness.onUpdateTrouble(any(), anyBoolean())
    ).thenReturn(expected);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setWorkArround("WorkArround");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_21() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");
    PowerMockito.when(I18n.getLanguageByLocale(any(), anyString())).thenReturn("TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(2L);
    troubleDTO.setWorkLog("WorkLog");
    troubleDTO.setWorkArround("WorkArround");
    troubleDTO.setState(20L);
    troubleDTO.setAutoClose(1L);
    troubleDTO.setReceiveUserId(22L);
    troubleDTO.setTypeId(3L);
    troubleDTO.setAlarmGroupId("10");
    troubleDTO.setReceiveUnitId(33L);
    troubleDTO.setTroubleCode("1111");
    troubleDTO.setTroubleName("TroubleName");
    troubleDTO.setCreateUnitName("CreateUnitName");
    troubleDTO.setReceiveUnitName("ReceiveUnitName");
    troubleDTO.setCreateUnitName("CreateUnitName");
    troubleDTO.setCreateUserName("CreateUserName");
    troubleDTO.setPriorityName("PriorityName");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoContent("WoContent");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        troublesBusiness.onUpdateTrouble(any(), anyBoolean())
    ).thenReturn(expected);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    usersInsideDto.setUsername("Username");
    usersInsideDto.setUserLanguage("2");
    usersInsideDto.setMobile("0900009009");
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(
        userBusiness
            .getListUsersByCondition(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstUser);

    CatItemDTO catItem = Mockito.spy(CatItemDTO.class);
    catItem.setItemCode("2222");
    catItem.setItemValue("3333");
    PowerMockito.when(
        catItemRepository
            .getCatItemById(anyLong())
    ).thenReturn(catItem);
    PowerMockito.when(
        catItemRepository.getCatItemById(anyLong())
    ).thenReturn(catItem);

    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    lstCatItem.add(catItem);
    PowerMockito.when(
        catItemRepository.getListCatItemDTO(any())
    ).thenReturn(lstCatItem);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setSmsGatewayId(1L);
    List<UnitDTO> lstUnit = Mockito.spy(ArrayList.class);
    lstUnit.add(unitDTO);
    PowerMockito.when(
        unitRepository.getListUnitDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstUnit);

    String sms = "troubleCode troubleName updateTime createUnit receiveUnit "
        + "currentUser createUser priority stateName";
    PowerMockito.when(DataUtil.getLang(any(), anyString())).thenReturn(sms);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setWorkArround("WorkArround");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_22() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");
    PowerMockito.when(I18n.getLanguageByLocale(any(), anyString())).thenReturn("TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(2L);
    troubleDTO.setWorkLog("WorkLog");
    troubleDTO.setWorkArround("WorkArround");
    troubleDTO.setState(20L);
    troubleDTO.setAutoClose(1L);
    troubleDTO.setReceiveUserId(22L);
    troubleDTO.setTypeId(3L);
    troubleDTO.setAlarmGroupId("10");
    troubleDTO.setReceiveUnitId(33L);
    troubleDTO.setTroubleCode("1111");
    troubleDTO.setTroubleName("TroubleName");
    troubleDTO.setCreateUnitName("CreateUnitName");
    troubleDTO.setReceiveUnitName("ReceiveUnitName");
    troubleDTO.setCreateUnitName("CreateUnitName");
    troubleDTO.setCreateUserName("CreateUserName");
    troubleDTO.setPriorityName("PriorityName");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoContent("WoContent");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        troublesBusiness.onUpdateTrouble(any(), anyBoolean())
    ).thenReturn(expected);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    usersInsideDto.setUsername("Username");
    usersInsideDto.setUserLanguage("2");
    usersInsideDto.setMobile("0900009009");
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(
        userBusiness
            .getListUsersByCondition(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstUser);

    CatItemDTO catItem = Mockito.spy(CatItemDTO.class);
    catItem.setItemCode("2222");
    catItem.setItemValue("3333");
    PowerMockito.when(
        catItemRepository
            .getCatItemById(anyLong())
    ).thenReturn(catItem);
    PowerMockito.when(
        catItemRepository.getCatItemById(anyLong())
    ).thenReturn(catItem);

    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    lstCatItem.add(catItem);
    PowerMockito.when(
        catItemRepository.getListCatItemDTO(any())
    ).thenReturn(lstCatItem);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setSmsGatewayId(1L);
    List<UnitDTO> lstUnit = Mockito.spy(ArrayList.class);
    lstUnit.add(unitDTO);
    PowerMockito.when(
        unitRepository.getListUnitDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstUnit);

    String sms = "troubleCode troubleName updateTime createUnit receiveUnit "
        + "currentUser createUser priority stateName";
    PowerMockito.when(DataUtil.getLang(any(), anyString())).thenReturn(sms);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.QUEUE);
    tForm.setWorkLog("WorkLog");
    tForm.setWorkArround("WorkArround");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromTKTU_23() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");
    PowerMockito.when(I18n.getLanguageByLocale(any(), anyString())).thenReturn("TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(2L);
    troubleDTO.setWorkLog("WorkLog");
    troubleDTO.setWorkArround("WorkArround");
    troubleDTO.setState(7L);
    troubleDTO.setAutoClose(1L);
    troubleDTO.setReceiveUserId(22L);
    troubleDTO.setTypeId(3L);
    troubleDTO.setAlarmGroupId("10");
    troubleDTO.setReceiveUnitId(33L);
    troubleDTO.setTroubleCode("1111");
    troubleDTO.setTroubleName("TroubleName");
    troubleDTO.setCreateUnitName("CreateUnitName");
    troubleDTO.setReceiveUnitName("ReceiveUnitName");
    troubleDTO.setCreateUnitName("CreateUnitName");
    troubleDTO.setCreateUserName("CreateUserName");
    troubleDTO.setPriorityName("PriorityName");
    troubleDTO.setStateName("StateName");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        troublesBusiness.onUpdateTrouble(any(), anyBoolean())
    ).thenReturn(expected);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(1L);
    usersInsideDto.setUsername("Username");
    usersInsideDto.setUserLanguage("2");
    usersInsideDto.setMobile("0900009009");
    List<UsersInsideDto> lstUser = Mockito.spy(ArrayList.class);
    lstUser.add(usersInsideDto);
    PowerMockito.when(
        userBusiness
            .getListUsersByCondition(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstUser);

    CatItemDTO catItem = Mockito.spy(CatItemDTO.class);
    catItem.setItemCode("2222");
    catItem.setItemValue("3333");
    PowerMockito.when(
        catItemRepository
            .getCatItemById(anyLong())
    ).thenReturn(catItem);
    PowerMockito.when(
        catItemRepository.getCatItemById(anyLong())
    ).thenReturn(catItem);

    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    lstCatItem.add(catItem);
    PowerMockito.when(
        catItemRepository.getListCatItemDTO(any())
    ).thenReturn(lstCatItem);

    String sms = "troubleCode troubleName updateTime createUnit receiveUnit "
        + "currentUser createUser priority stateName";
    PowerMockito.when(DataUtil.getLang(any(), anyString())).thenReturn(sms);

    TroublesInSideDTO tForm = Mockito.spy(TroublesInSideDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setStateName(Constants.TT_STATE.QUEUE);
    tForm.setWorkLog("WorkLog");
    tForm.setWorkArround("WorkArround");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromTKTU(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setRemainTime("48");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    Map<String, String> mapProperty = Mockito.spy(HashMap.class);
    PowerMockito.when(
        troublesServiceForCCRepository.getConfigProperty()
    ).thenReturn(mapProperty);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_HOT");
    tForm.setTimeProcess("12");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setRemainTime("48");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    Map<String, String> mapProperty = Mockito.spy(HashMap.class);
    PowerMockito.when(
        troublesServiceForCCRepository.getConfigProperty()
    ).thenReturn(mapProperty);

    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey("TESTKEY");
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_HOT");
    tForm.setTimeProcess("12");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setRemainTime("48");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    Map<String, String> mapProperty = Mockito.spy(HashMap.class);
    PowerMockito.when(
        troublesServiceForCCRepository.getConfigProperty()
    ).thenReturn(mapProperty);

    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_HOT");
    tForm.setTimeProcess("12");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setRemainTime("48");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    Map<String, String> mapProperty = Mockito.spy(HashMap.class);
    PowerMockito.when(
        troublesServiceForCCRepository.getConfigProperty()
    ).thenReturn(mapProperty);

    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("ItemValue");
    catItemDTO.setItemId(1L);
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    lstCatItem.add(catItemDTO);
    PowerMockito.when(
        catItemRepository.getListCatItemDTO(any())
    ).thenReturn(lstCatItem);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_HOT");
    tForm.setTimeProcess("12");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_06() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_WORKLOG");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_07() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey("TESTKEY");
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_WORKLOG");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_08() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_WORKLOG");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_09() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_NUM_COMPLAINT");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_10() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey("TESTKEY");
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_NUM_COMPLAINT");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_11() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.SUCCESS);
    PowerMockito.when(woServiceProxy.updateWoInfoProxy(any())).thenReturn(rdto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_NUM_COMPLAINT");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_12() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_OPEN_DEFERRED");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_13() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey("TESTKEY");
    PowerMockito.when(
        woServiceProxy
            .updatePendingWo(any())
    ).thenReturn(rdto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_OPEN_DEFERRED");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_14() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    troubleDTO.setWorkLog("WorkLog");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        woServiceProxy
            .updatePendingWo(any())
    ).thenReturn(rdto);

    CatItemDTO stateItem = Mockito.spy(CatItemDTO.class);
    stateItem.setItemName("ItemName");
    PowerMockito.when(
        catItemRepository
            .getCatItemById(anyLong())
    ).thenReturn(stateItem);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_OPEN_DEFERRED");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_15() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(11L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    troubleDTO.setWorkLog("WorkLog");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    ResultDTO rdto = Mockito.spy(ResultDTO.class);
    rdto.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        woServiceProxy
            .pendingWo(any())
    ).thenReturn(rdto);

    CatItemDTO stateItem = Mockito.spy(CatItemDTO.class);
    stateItem.setItemName("ItemName");
    PowerMockito.when(
        catItemRepository
            .getCatItemById(anyLong())
    ).thenReturn(stateItem);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_DEFERRED");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    tForm.setDeferredTime("24/05/2020 12:00:00");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_16() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(12L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    troubleDTO.setWorkLog("WorkLog");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    PowerMockito.when(
        woServiceProxy
            .closeWoForSPM(any())
    ).thenReturn(resultDTO);

    CatItemDTO stateItem = Mockito.spy(CatItemDTO.class);
    stateItem.setItemName("ItemName");
    PowerMockito.when(
        catItemRepository
            .getCatItemById(anyLong())
    ).thenReturn(stateItem);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_CLOSED");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    tForm.setDeferredTime("24/05/2020 12:00:00");
    tForm.setReasonLv3Id("123");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleCC_17() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setCreateUnitId(1L);
    troubleDTO.setCreateUserId(1L);
    troubleDTO.setState(12L);
    troubleDTO.setTroubleCode("111");
    troubleDTO.setStateName("StateName");
    troubleDTO.setWoCode("5555");
    troubleDTO.setWorkLog("WorkLog");
    troubleDTO.setEndTroubleTime(
        DateTimeUtils.convertStringToDate("20/05/2020 12:00:00")
    );
    troubleDTO.setClearTime(
        DateTimeUtils.convertStringToDate("29/05/2020 12:00:00")
    );
    troubleDTO.setClosedTime(
        DateTimeUtils.convertStringToDate("29/06/2020 12:00:00")
    );
    troubleDTO.setTimeUsed(22D);
    troubleDTO.setRemainTime("-1");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        woServiceProxy
            .closeWoForSPM(any())
    ).thenReturn(resultDTO);

    CatItemDTO stateItem = Mockito.spy(CatItemDTO.class);
    stateItem.setItemName("ItemName");
    PowerMockito.when(
        catItemRepository
            .getCatItemById(anyLong())
    ).thenReturn(stateItem);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("TroubleCode");
    tForm.setProcessingUnitName("ProcessingUnitName");
    tForm.setProcessingUserName("ProcessingUserName");
    tForm.setStateName("UPDATE_CLOSED");
    tForm.setWorkLog("WorkLog");
    tForm.setCustomerTimeDesireFrom("24");
    tForm.setCustomerTimeDesireTo("48");
    tForm.setDeferredTime("24/05/2020 12:00:00");
    tForm.setReasonLv3Id("123");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleCC(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_06() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_07() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_08() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_09() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName("StateName");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_10() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_11() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_12() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_13() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_14() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    tForm.setLongitude("111");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_15() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    tForm.setLongitude("222");
    tForm.setLatitude("222");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_16() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("222");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_17() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_18() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setDeferredTime("24");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_19() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setDeferredTime("24");
    tForm.setEstimateTime("48");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_20() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setDeferredTime("24");
    tForm.setEstimateTime("48");
    tForm.setGroupSolution("GroupSolution");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_21() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setDeferredTime("06/05/2020 12:00:00");
    tForm.setEstimateTime("48");
    tForm.setGroupSolution("GroupSolution");
    tForm.setNumPending("NumPending");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_22() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setDeferredTime("06/05/3000 12:00:00");
    tForm.setEstimateTime("06/05/2020 12:00:00");
    tForm.setGroupSolution("GroupSolution");
    tForm.setNumPending("NumPending");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_23() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setState(7L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setDeferredTime("06/05/3000 12:00:00");
    tForm.setEstimateTime("09/05/3000 12:00:00");
    tForm.setGroupSolution("300");
    tForm.setNumPending("200");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_24() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setState(10L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        troublesBusiness.onUpdateTrouble(any(), anyBoolean())
    ).thenReturn(resultInSideDto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.DEFERRED);
    tForm.setWorkLog("WorkLog");
    tForm.setDeferredReason("DeferredReason");
    tForm.setDeferType("2");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setDeferredTime("06/05/3000 12:00:00");
    tForm.setEstimateTime("09/05/3000 12:00:00");
    tForm.setGroupSolution("300");
    tForm.setNumPending("200");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_25() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_26() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_27() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_28() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_29() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("222");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_30() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("222");
    tForm.setLatitude("222");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_31() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("222");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_32() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_33() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_34() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_35() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    tForm.setReasonLv2Name("Lv2Name");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_36() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    tForm.setReasonLv2Name("Lv2Name");
    tForm.setReasonLv3Id("33");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_37() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    tForm.setReasonLv2Name("Lv2Name");
    tForm.setReasonLv3Id("33");
    tForm.setReasonLv3Name("Lv3Name");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_38() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    tForm.setReasonLv2Name("Lv2Name");
    tForm.setReasonLv3Id("33");
    tForm.setReasonLv3Name("Lv3Name");
    tForm.setRootCause("RootCause");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_39() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    tForm.setReasonLv2Name("Lv2Name");
    tForm.setReasonLv3Id("33");
    tForm.setReasonLv3Name("Lv3Name");
    tForm.setRootCause("RootCause");
    tForm.setWorkArround("WorkArround");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_40() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    tForm.setReasonLv2Name("Lv2Name");
    tForm.setReasonLv3Id("33");
    tForm.setReasonLv3Name("Lv3Name");
    tForm.setRootCause("RootCause");
    tForm.setWorkArround("WorkArround");
    tForm.setTotalPendingTimeGnoc("24");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_41() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    tForm.setReasonLv2Name("Lv2Name");
    tForm.setReasonLv3Id("33");
    tForm.setReasonLv3Name("Lv3Name");
    tForm.setRootCause("RootCause");
    tForm.setWorkArround("WorkArround");
    tForm.setTotalPendingTimeGnoc("24");
    tForm.setTotalProcessTimeGnoc("48");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.FAIL, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_42() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setState(10L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    tForm.setReasonLv2Name("Lv2Name");
    tForm.setReasonLv3Id("33");
    tForm.setReasonLv3Name("Lv3Name");
    tForm.setRootCause("RootCause");
    tForm.setWorkArround("WorkArround");
    tForm.setTotalPendingTimeGnoc("24");
    tForm.setTotalProcessTimeGnoc("48");
    tForm.setEvaluateGnoc("33");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_43() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setState(8L);
    troubleDTO.setReceiveUnitId(2L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        troublesBusiness.onUpdateTrouble(any(), anyBoolean())
    ).thenReturn(resultInSideDto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    tForm.setReasonLv2Name("Lv2Name");
    tForm.setReasonLv3Id("33");
    tForm.setReasonLv3Name("Lv3Name");
    tForm.setRootCause("RootCause");
    tForm.setWorkArround("WorkArround");
    tForm.setTotalPendingTimeGnoc("24");
    tForm.setTotalProcessTimeGnoc("48");
    tForm.setEvaluateGnoc("33");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_44() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setState(8L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        troublesBusiness.onUpdateTrouble(any(), anyBoolean())
    ).thenReturn(resultInSideDto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    tForm.setReasonLv2Name("Lv2Name");
    tForm.setReasonLv3Id("33");
    tForm.setReasonLv3Name("Lv3Name");
    tForm.setRootCause("RootCause");
    tForm.setWorkArround("WorkArround");
    tForm.setTotalPendingTimeGnoc("24");
    tForm.setTotalProcessTimeGnoc("48");
    tForm.setEvaluateGnoc("33");
    tForm.setCustomerTimeDesireTo("6");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_45() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setState(8L);
    troubleDTO.setRelatedKedb("RelatedKedb");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        troublesBusiness.onUpdateTrouble(any(), anyBoolean())
    ).thenReturn(resultInSideDto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.CLOSED);
    tForm.setWorkLog("WorkLog");
    tForm.setEndTroubleTime("10/05/2020 12:00:00");
    tForm.setGroupSolution("200");
    tForm.setCellService("CellService");
    tForm.setLongitude("110");
    tForm.setLatitude("20");
    tForm.setReasonLv1Id("11");
    tForm.setReasonLv1Name("Lv1Name");
    tForm.setReasonLv2Id("22");
    tForm.setReasonLv2Name("Lv2Name");
    tForm.setReasonLv3Id("33");
    tForm.setReasonLv3Name("Lv3Name");
    tForm.setRootCause("RootCause");
    tForm.setWorkArround("WorkArround");
    tForm.setTotalPendingTimeGnoc("24");
    tForm.setTotalProcessTimeGnoc("48");
    tForm.setEvaluateGnoc("33");
    tForm.setCustomerTimeDesireTo("6");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_46() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setState(8L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.QUEUE);
    tForm.setWorkLog("WorkLog");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_47() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setState(7L);
    troubleDTO.setReceiveUnitId(2L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        troublesBusiness.onUpdateTrouble(any(), anyBoolean())
    ).thenReturn(resultInSideDto);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("ReceiveUnitId");
    tForm.setReceiveUserId("ReceiveUserId");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName(Constants.TT_STATE.QUEUE);
    tForm.setWorkLog("WorkLog");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertNull(actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_48() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TroubleBccsUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setTypeId(2L);
    troubleDTO.setState(3L);
    troubleDTO.setInsertSource("InsertSource");
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness.findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("222");
    catItemDTO.setItemName("CatItemName");
    PowerMockito.when(
        catItemRepository
            .getCatItemById(anyLong())
    ).thenReturn(catItemDTO);

    CfgServerNocDTO cfgServerNocDTO = Mockito.spy(CfgServerNocDTO.class);
    PowerMockito.when(
        troublesRepository.getServerDTO(any())
    ).thenReturn(cfgServerNocDTO);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("2222");
    tForm.setReceiveUserId("3333");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName("UPDATE");
    tForm.setWorkLog("WorkLog");
    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void onUpdateTroubleFromWo_49() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    TroublesInSideDTO troubleDTO = Mockito.spy(TroublesInSideDTO.class);
    troubleDTO.setTroubleId(1L);
    troubleDTO.setAutoClose(1L);
    troubleDTO.setTimeProcess(12D);
    troubleDTO.setReceiveUnitId(1L);
    PowerMockito.when(
        troublesRepository
            .getTroubleDTO(any(), anyString(), any(), any(), any(), any(), any())
    ).thenReturn(troubleDTO);
    PowerMockito.when(
        troublesBusiness
            .findTroublesById(anyLong())
    ).thenReturn(troubleDTO);

    Map<String, String> mapProperty = Mockito.spy(HashMap.class);
    mapProperty.put("TT.TIME.PER.WO.TIME", "12:00:00");
    PowerMockito.when(
        troublesServiceForCCRepository.getConfigProperty()
    ).thenReturn(mapProperty);

    TroublesDTO tForm = Mockito.spy(TroublesDTO.class);
    tForm.setTroubleCode("1111");
    tForm.setReceiveUnitId("222");
    tForm.setReceiveUserId("333");
    tForm.setReceiveUnitName("ReceiveUnitName");
    tForm.setReceiveUserName("ReceiveUserName");
    tForm.setProcessingUserPhone("0909009009");
    tForm.setStateName("IS_HELP");
    tForm.setWorkLog("WorkLog");

    ResultDTO actual = troublesServiceForCCBusiness
        .onUpdateTroubleFromWo(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

//  @Test
//  public void getListWoLog() {
//  }
//
//  @Test
//  public void getCompCauseDTOForCC3() {
//  }
//
//  @Test
//  public void getGroupSolution() {
//  }
//
//  @Test
//  public void getCauseErrorExpireForCC3() {
//  }
}

