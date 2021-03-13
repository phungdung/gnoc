
package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.aam.LinkCrResult;
import com.viettel.gnoc.commons.business.GnocFileBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.incident.provider.WSSecurityPort;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CoordinationSettingDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.repository.CoordinationSettingRepository;
import com.viettel.gnoc.cr.repository.CrDBRepository;
import com.viettel.gnoc.cr.repository.CrFilesAttachRepository;
import com.viettel.gnoc.cr.repository.CrMobileRepository;
import com.viettel.gnoc.cr.repository.CrProcessRepository;
import com.viettel.gnoc.cr.repository.CrProcessWoRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.gnoc.cr.util.CrProcessFromClient;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import com.viettel.gnoc.ws.provider.WSTDTTPort;
import com.viettel.gnoc.ws.provider.WSVipaDdPort;
import com.viettel.gnoc.ws.provider.WSVipaIpPort;
import java.util.ArrayList;
import java.util.List;
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
@PrepareForTest({CrMobileBusinessImpl.class, I18n.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrMobileBusinessImplTest {

  @InjectMocks
  CrMobileBusinessImpl crMobileBusiness;
  @Mock
  CrMobileRepository crMobileRepository;

  @Mock
  CrRepository crRepository;

  @Mock
  CrDBRepository crDBRepository;

  @Mock
  MrCategoryProxy mrCategoryProxy;

  @Mock
  CrProcessFromClient crProcessFromClient;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  CrFilesAttachRepository crFilesAttachRepository;

  @Mock
  CrProcessWoRepository crProcessWoRepository;

  @Mock
  CrBusiness crBusiness;


  @Mock
  GnocFileBusiness gnocFileBusiness;

  @Mock
  UnitBusiness unitBusiness;

  @Mock
  UserBusiness userBusiness;

  @Mock
  CrImpactedNodesBusiness crImpactedNodesBusiness;

  @Mock
  WSSecurityPort wsSecurityPort;

  @Mock
  WSVipaIpPort wsVipaIpPort;

  @Mock
  WSTDTTPort wstdttPort;

  @Mock
  WSVipaDdPort wsVipaDdPort;

  @Mock
  CrGeneralBusiness crGeneralBusiness;

  @Mock
  CrProcessRepository crProcessRepository;

  @Mock
  CrForOtherSystemBusiness crForOtherSystemBusiness;

  @Mock
  CoordinationSettingRepository coordinationSettingRepository;

  @Mock
  UnitRepository unitRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(crMobileBusiness, "woFailtCode", "500");
    ReflectionTestUtils.setField(crMobileBusiness, "crResolveSuccess", "39");
  }

  @Test
  public void testGetListCRBySearchTypeCount() {
    List<CrDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crMobileRepository.getListCRBySearchTypeCount(any())
    ).thenReturn(expected);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    List<CrDTO> actual = crMobileBusiness.getListCRBySearchTypeCount(crDTO);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetListCRBySearchTypePaggingMobile() {
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    ObjResponse expected = Mockito.spy(ObjResponse.class);
    PowerMockito.when(crMobileRepository
        .getListCRBySearchTypePaggingMobile(
            any(), anyInt(), anyInt(), anyString()
        )
    ).thenReturn(expected);
    ObjResponse actual = crMobileBusiness
        .getListCRBySearchTypePaggingMobile(
            crDTO, 1, 1, "vi_VN"
        );
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetListPreApprovedCrOutSide() {
    List<CrDTO> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crDBRepository.getListPreApprovedCrOutSide(any())
    ).thenReturn(expected);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    List<CrDTO> actual = crMobileBusiness.getListPreApprovedCrOutSide(crDTO);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testActionApproveCRAfter() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setCrType("2");
    formRoot.setChangeOrginator("2");
    formRoot.setCreatedDate(
        DateTimeUtils.convertStringToDate("11/05/2020 09:00:00")
    );
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoId("11");
    woDTOSearch.setWoCode("1111");
    woDTOSearch.setWoTypeId("22");
    woDTOSearch.setWoTypeCode("2222");
    woDTOSearch.setCreateDate("11/05/2020 09:00:00");
    woDTOSearch.setStatus("Status");
    woDTOSearch.setStartTime("11/05/2020 09:00:00");
    woDTOSearch.setEndTime("11/05/3000 09:00:00");
    woDTOSearch.setWoContent("WoContent");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("2");
    crDTO.setUserLogin("username");
    crMobileBusiness.actionApproveCRAfter(crDTO);
  }

  @Test
  public void testActionAppraiseCRAfter() {
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setCrId("11");
    formRoot.setCreatedDate(
        DateTimeUtils.convertStringToDate("11/05/2020 10:00:00")
    );
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoId("11");
    woDTOSearch.setWoCode("WO_CR_TEST_SERVICE_AUTO_GENERATE");
    woDTOSearch.setWoTypeId("22");
    woDTOSearch.setWoTypeCode("2222");
    woDTOSearch.setCreateDate("11/05/2020 09:00:00");
    woDTOSearch.setStatus("Status");
    woDTOSearch.setStartTime("11/05/2020 09:00:00");
    woDTOSearch.setEndTime("11/05/3000 09:00:00");
    woDTOSearch.setWoContent("WoContent");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    List<GnocFileDto> lstAttachDTOs = Mockito.spy(ArrayList.class);
    lstAttachDTOs.add(gnocFileDto);
    PowerMockito.when(
        gnocFileBusiness.getListGnocFileByDto(any())
    ).thenReturn(lstAttachDTOs);

    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setObjectId("111");
    List<CrCreatedFromOtherSysDTO> lstCrCreatedFromOtherSys = Mockito.spy(ArrayList.class);
    lstCrCreatedFromOtherSys.add(crCreatedFromOtherSysDTO);
    PowerMockito.when(
        crRepository
            .getCrCreatedFromOtherSys(any())
    ).thenReturn(lstCrCreatedFromOtherSys);

    WoTestServiceMapDTO woTestServiceMapDTO = Mockito.spy(WoTestServiceMapDTO.class);
    woTestServiceMapDTO.setWoSubId("222");
    List<WoTestServiceMapDTO> lstMap = Mockito.spy(ArrayList.class);
    lstMap.add(woTestServiceMapDTO);
//    PowerMockito.when(
//        woTestServiceMapService.getListWoTestServiceMapDTO(
//            any(), anyInt(), anyInt(), anyString(), anyString()
//        )
//    ).thenReturn(lstMap);

    WoDTO dto = Mockito.spy(WoDTO.class);
    PowerMockito.when(
        woServiceProxy.findWoByIdWSProxy(anyLong())
    ).thenReturn(dto);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionType("14");
    crDTO.setUserLogin("12121212");
    String actual = crMobileBusiness.actionAppraiseCRAfter(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionVerifyMobile() throws Exception {
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setCrType("0");
    formRoot.setProcessTypeId("1111");
    formRoot.setCreatedDate(
        DateTimeUtils.convertStringToDate("11/05/2020 10:00:00")
    );
    formRoot.setChangeOrginator("ChangeOrginator");
    formRoot.setImpactSegment("ImpactSegment");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    String validateFileAttach = "SUCCESS";
    PowerMockito.when(
        crProcessFromClient.validateFileAttach(any(), anyList(), anyList())
    ).thenReturn(validateFileAttach);

    String validateFileImportByProcess = "SUCCESS";
    PowerMockito.when(
        crProcessFromClient.validateFileImportByProcess(any(), anyList(), anyString())
    ).thenReturn(validateFileImportByProcess);

    CrProcessWoDTO crProcessWoDTO = Mockito.spy(CrProcessWoDTO.class);
    crProcessWoDTO.setWoName("WoContent");
    crProcessWoDTO.setIsRequire(1L);
    crProcessWoDTO.setCreateWoWhenCloseCR(2L);
    crProcessWoDTO.setWoTypeId(22L);
    List<CrProcessWoDTO> lstCrWo = Mockito.spy(ArrayList.class);
    lstCrWo.add(crProcessWoDTO);
    PowerMockito.when(
        crProcessWoRepository
            .getLstWoFromProcessId(anyString())
    ).thenReturn(lstCrWo);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoId("11");
    woDTOSearch.setWoCode("1111");
    woDTOSearch.setWoTypeId("22");
    woDTOSearch.setWoTypeCode("2222");
    woDTOSearch.setCreateDate("11/05/2020 09:00:00");
    woDTOSearch.setStatus("Status");
    woDTOSearch.setStartTime("11/05/2020 09:00:00");
    woDTOSearch.setEndTime("11/05/3000 09:00:00");
    woDTOSearch.setWoContent("WoContent");
    WoDTOSearch woDTOSearch1 = Mockito.spy(WoDTOSearch.class);
    woDTOSearch1.setWoId("11");
    woDTOSearch1.setWoCode("1111");
    woDTOSearch1.setWoTypeId("22");
    woDTOSearch1.setWoTypeCode("2222");
    woDTOSearch1.setCreateDate("11/05/2020 09:00:00");
    woDTOSearch1.setStatus("Status");
    woDTOSearch1.setStartTime("11/05/2020 09:00:00");
    woDTOSearch1.setEndTime("11/05/3000 09:00:00");
    woDTOSearch1.setWoContent("WoContent");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    lstWo.add(woDTOSearch1);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);

//    WorkLogCategoryInsideDTO wlCatCon = Mockito.spy(WorkLogCategoryInsideDTO.class);
//    wlDto.setWlayId("11");
//    wlDto.setWlayType("WlayType");
//    List<WorkLogCategoryInsideDTO> lstWlCat = Mockito.spy(ArrayList.class);
//    lstWlCat.add(wlDto);
//    PowerMockito.when(
//        mrCategoryProxy.getListWorkLogCategoryDTO(any())).thenReturn(lstWlCat);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionRight("ActionRight");
    crDTO.setActionType("ActionType");
    crDTO.setIsTracingCr("1");
    crDTO.setRelateToPreApprovedCr("RelateToPreApprovedCr");
    crDTO.setProcessTypeId("11");
    crDTO.setProcessTypeLv3Id("33");
    PowerMockito.when(crBusiness.actionVerify(any(), anyString())).thenReturn(RESULT.SUCCESS);
    String actual = crMobileBusiness.actionVerifyMobile(crDTO, "vi_VN");
    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionReceiveCr() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setCrId("11");
    formRoot.setCrNumber("22");
    formRoot.setCreatedDate(
        DateTimeUtils.convertStringToDate("11/05/2020 12:00:00")
    );
    formRoot.setProcessTypeId("112233");
    formRoot.setEarliestStartTime(
        DateTimeUtils.convertStringToDate("11/05/2020 12:00:00")
    );
    formRoot.setUpdateTime(
        DateTimeUtils.convertStringToDate("11/05/2020 12:00:00")
    );
    formRoot.setDutyType("DutyType");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitCode("3333");
    PowerMockito.when(
        unitBusiness.findUnitById(anyLong())
    ).thenReturn(unitDTO);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setEmail("user@gmail.com");
    usersInsideDto.setUsername("Username");
    usersInsideDto.setUnitName("UnitName");
    usersInsideDto.setMobile("0909009009");
    PowerMockito.when(
        userBusiness.getUserDetailById(anyLong())
    ).thenReturn(usersInsideDto);

    String rs = "SUCCESS";
    PowerMockito.when(
        crBusiness.actionReceiveCr(any(), anyString())
    ).thenReturn(rs);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoId("11");
    woDTOSearch.setWoCode("1111");
    woDTOSearch.setWoTypeId("22");
    woDTOSearch.setWoTypeCode("2222");
    woDTOSearch.setCreateDate("11/05/2020 09:00:00");
    woDTOSearch.setStatus("Status");
    woDTOSearch.setStartTime("11/05/2020 09:00:00");
    woDTOSearch.setEndTime("11/05/3000 09:00:00");
    woDTOSearch.setWoContent("WoContent");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionType("16");
    crDTO.setChangeOrginatorUnit("1111");
    crDTO.setChangeOrginator("2222");
    crDTO.setUserLogin("121212");
    crDTO.setLatestStartTime("11/05/2020 12:00:00");
    String actual = crMobileBusiness.actionReceiveCr(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionScheduleCr_01() {
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setState("VN");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1111");
    crFilesAttachInsiteDTO.setDtFileHistory("VIPA_IP");
    List<CrFilesAttachInsiteDTO> lstAttachment = Mockito.spy(ArrayList.class);
    lstAttachment.add(crFilesAttachInsiteDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesAttachDTO(
                any(), anyInt(), anyInt(), anyString(), anyString()
            )
    ).thenReturn(lstAttachment);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setAutoExecute("1");
    crDTO.setRelateToPreApprovedCrNumber("11223344");
    crDTO.setRelateToPrimaryCrNumber("44332211");
    crDTO.setCrNumber("11");
    String actual = crMobileBusiness.actionScheduleCr(crDTO);

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionScheduleCr_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setState("VN");
    formRoot.setCrType("0");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1111");
    crFilesAttachInsiteDTO.setDtFileHistory("VIPA_IP");
    List<CrFilesAttachInsiteDTO> lstAttachment = Mockito.spy(ArrayList.class);
    lstAttachment.add(crFilesAttachInsiteDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesAttachDTO(
                any(), anyInt(), anyInt(), anyString(), anyString()
            )
    ).thenReturn(lstAttachment);

    com.viettel.vipa.ResultDTO result = Mockito.spy(com.viettel.vipa.ResultDTO.class);
    result.setResultCode(1);
    PowerMockito.when(
        wsVipaIpPort
            .updateRunAutoStatus(
                any(), anyString(), anyBoolean(), anyLong(), anyLong(), anyString()
            )
    ).thenReturn(result);

    String rs = "SUCCESS";
    PowerMockito.when(
        crBusiness.actionScheduleCr(any(), anyString())
    ).thenReturn(rs);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoId("11");
    woDTOSearch.setWoCode("1111");
    woDTOSearch.setWoTypeId("22");
    woDTOSearch.setWoTypeCode("2222");
    woDTOSearch.setCreateDate("11/05/2020 09:00:00");
    woDTOSearch.setStatus("Status");
    woDTOSearch.setStartTime("11/05/2020 09:00:00");
    woDTOSearch.setEndTime("11/05/3000 09:00:00");
    woDTOSearch.setWoContent("WoContent");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setAutoExecute("1");
    crDTO.setRelateToPreApprovedCrNumber("11223344");
    crDTO.setRelateToPrimaryCrNumber("44332211");
    crDTO.setCrNumber("11");
    crDTO.setActionType("22");
    String actual = crMobileBusiness.actionScheduleCr(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionScheduleCr_03() {
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setState("VN");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1111");
    crFilesAttachInsiteDTO.setDtFileHistory("VIPA_DD");
    List<CrFilesAttachInsiteDTO> lstAttachment = Mockito.spy(ArrayList.class);
    lstAttachment.add(crFilesAttachInsiteDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesAttachDTO(
                any(), anyInt(), anyInt(), anyString(), anyString()
            )
    ).thenReturn(lstAttachment);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setAutoExecute("1");
    crDTO.setRelateToPreApprovedCrNumber("11223344");
    crDTO.setRelateToPrimaryCrNumber("44332211");
    crDTO.setCrNumber("11");
    crDTO.setIsConfirmAction("1");
    crDTO.setIsRunType("1");
    String actual = crMobileBusiness.actionScheduleCr(crDTO);

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionScheduleCr_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setState("VN");
    formRoot.setCrType("0");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1111");
    crFilesAttachInsiteDTO.setDtFileHistory("VIPA_DD");
    List<CrFilesAttachInsiteDTO> lstAttachment = Mockito.spy(ArrayList.class);
    lstAttachment.add(crFilesAttachInsiteDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesAttachDTO(
                any(), anyInt(), anyInt(), anyString(), anyString()
            )
    ).thenReturn(lstAttachment);

    com.viettel.vmsa.ResultDTO result = Mockito.spy(com.viettel.vmsa.ResultDTO.class);
    result.setResultCode(1);
    PowerMockito.when(
        wsVipaDdPort
            .updateRunAutoStatus(
                any(), anyString(), anyBoolean(), anyLong(), anyLong(), anyString()
            )
    ).thenReturn(result);

    String rs = "SUCCESS";
    PowerMockito.when(
        crBusiness.actionScheduleCr(any(), anyString())
    ).thenReturn(rs);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoId("11");
    woDTOSearch.setWoCode("1111");
    woDTOSearch.setWoTypeId("22");
    woDTOSearch.setWoTypeCode("2222");
    woDTOSearch.setCreateDate("11/05/2020 09:00:00");
    woDTOSearch.setStatus("Status");
    woDTOSearch.setStartTime("11/05/2020 09:00:00");
    woDTOSearch.setEndTime("11/05/3000 09:00:00");
    woDTOSearch.setWoContent("WoContent");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setAutoExecute("1");
    crDTO.setRelateToPreApprovedCrNumber("11223344");
    crDTO.setRelateToPrimaryCrNumber("44332211");
    crDTO.setCrNumber("11");
    crDTO.setActionType("22");
    String actual = crMobileBusiness.actionScheduleCr(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionScheduleCr_05() {
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setState("VN");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1111");
    crFilesAttachInsiteDTO.setDtFileHistory("VIPA");
    List<CrFilesAttachInsiteDTO> lstAttachment = Mockito.spy(ArrayList.class);
    lstAttachment.add(crFilesAttachInsiteDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesAttachDTO(
                any(), anyInt(), anyInt(), anyString(), anyString()
            )
    ).thenReturn(lstAttachment);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setAutoExecute("1");
    crDTO.setRelateToPreApprovedCrNumber("11223344");
    crDTO.setRelateToPrimaryCrNumber("44332211");
    crDTO.setCrNumber("11");
    crDTO.setIsConfirmAction("1");
    crDTO.setIsRunType("1");
    String actual = crMobileBusiness.actionScheduleCr(crDTO);

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionScheduleCr_06() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setState("VN");
    formRoot.setCrType("0");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1111");
    crFilesAttachInsiteDTO.setDtFileHistory("VIPA");
    List<CrFilesAttachInsiteDTO> lstAttachment = Mockito.spy(ArrayList.class);
    lstAttachment.add(crFilesAttachInsiteDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesAttachDTO(
                any(), anyInt(), anyInt(), anyString(), anyString()
            )
    ).thenReturn(lstAttachment);

    LinkCrResult result = Mockito.spy(LinkCrResult.class);
    result.setStatus(1);
    PowerMockito.when(
        wstdttPort
            .updateRunAutoStatus(
                any(), anyString(), anyBoolean(), anyLong(), anyLong(), anyString()
            )
    ).thenReturn(result);

    String rs = "SUCCESS";
    PowerMockito.when(
        crBusiness.actionScheduleCr(any(), anyString())
    ).thenReturn(rs);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoId("11");
    woDTOSearch.setWoCode("1111");
    woDTOSearch.setWoTypeId("22");
    woDTOSearch.setWoTypeCode("2222");
    woDTOSearch.setCreateDate("11/05/2020 09:00:00");
    woDTOSearch.setStatus("Status");
    woDTOSearch.setStartTime("11/05/2020 09:00:00");
    woDTOSearch.setEndTime("11/05/3000 09:00:00");
    woDTOSearch.setWoContent("WoContent");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setAutoExecute("1");
    crDTO.setRelateToPreApprovedCrNumber("11223344");
    crDTO.setRelateToPrimaryCrNumber("44332211");
    crDTO.setCrNumber("11");
    crDTO.setActionType("22");
    String actual = crMobileBusiness.actionScheduleCr(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionResolveCr_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("UNIT-TEST");

    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setChangeResponsible("5");
    formRoot.setCreatedDate(
        DateTimeUtils.convertStringToDate("11/05/2020 12:00:00")
    );
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    String rs = "SUCCESS";
    PowerMockito.when(
        crBusiness.actionResolveCr(any(), anyString())
    ).thenReturn(rs);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setEmail("user@gmail.com");
    usersInsideDto.setUsername("username");
    PowerMockito.when(
        userBusiness.getUserDetailById(anyLong())
    ).thenReturn(usersInsideDto);

    List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crImpactedNodesBusiness
            .onSearch(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstCrImpactedNodesDTOs);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(111L);
    itemDataCRInside.setSecondValue("500");
    List<ItemDataCRInside> lstCbbReturnCode = Mockito.spy(ArrayList.class);
    lstCbbReturnCode.add(itemDataCRInside);
    PowerMockito.when(
        crGeneralBusiness.getListActionCodeByCode(anyString(), anyString())
    ).thenReturn(lstCbbReturnCode);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoCode("5555");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);

    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    PowerMockito.when(
        woServiceProxy.changeStatusWoProxy(any())
    ).thenReturn(resultDTO);

    CrProcessInsideDTO process = Mockito.spy(CrProcessInsideDTO.class);
    process.setCloseCrWhenResolveSuccess(1L);
    PowerMockito.when(
        crProcessRepository
            .findCrProcessById(anyLong())
    ).thenReturn(process);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setCrNumber("11");
    crDTO.setCrReturnResolve("111");
    crDTO.setUserLogin("121212");
    crDTO.setProcessTypeId("1111");
    crDTO.setActionReturnCodeId("39");
    String actual = crMobileBusiness.actionResolveCr(crDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void testActionResolveCr_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("UNIT-TEST");

    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setChangeResponsible("5");
    formRoot.setCreatedDate(
        DateTimeUtils.convertStringToDate("11/05/2020 12:00:00")
    );
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    PowerMockito.when(
        crBusiness.actionResolveCr(any(), anyString())
    ).thenReturn("SUCCESS");

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setEmail("user@gmail.com");
    usersInsideDto.setUsername("username");
    PowerMockito.when(
        userBusiness.getUserDetailById(anyLong())
    ).thenReturn(usersInsideDto);

    List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crImpactedNodesBusiness
            .onSearch(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstCrImpactedNodesDTOs);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(111L);
    itemDataCRInside.setSecondValue("500");
    List<ItemDataCRInside> lstCbbReturnCode = Mockito.spy(ArrayList.class);
    lstCbbReturnCode.add(itemDataCRInside);
    PowerMockito.when(
        crGeneralBusiness.getListActionCodeByCode(anyString(), anyString())
    ).thenReturn(lstCbbReturnCode);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoCode("5555");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);

    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setMessage(RESULT.SUCCESS);
    PowerMockito.when(
        woServiceProxy.changeStatusWoProxy(any())
    ).thenReturn(resultDTO);

    CrProcessInsideDTO process = Mockito.spy(CrProcessInsideDTO.class);
    process.setCloseCrWhenResolveSuccess(1L);
    PowerMockito.when(
        crProcessRepository
            .findCrProcessById(anyLong())
    ).thenReturn(process);

    PowerMockito.when(
        crBusiness
            .actionCloseCr(any(), anyString())
    ).thenReturn("SUCCESS");

    CrCreatedFromOtherSysDTO dto = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    dto.setStatus("OK");
    dto.setSystemId("4");
    dto.setObjectId("10");
    PowerMockito.when(
        crForOtherSystemBusiness.getCrCreatedFromOtherSysDTO(anyLong())
    ).thenReturn(dto);

    WoDTO wo = Mockito.spy(WoDTO.class);
    wo.setWoTypeId("33");
    PowerMockito.when(
        woServiceProxy.findWoByIdWSProxy(anyLong())
    ).thenReturn(wo);

    PowerMockito.when(
        crForOtherSystemBusiness.checkWoCloseAutoSetting(anyLong(), anyLong())
    ).thenReturn(true);

    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setCrId("5");
    List<CrCreatedFromOtherSysDTO> crList = Mockito.spy(ArrayList.class);
    crList.add(crCreatedFromOtherSysDTO);
    PowerMockito.when(
        crForOtherSystemBusiness.getListDataByObjectId(anyLong())
    ).thenReturn(crList);

    CrDTO crDTO1 = Mockito.spy(CrDTO.class);
    List<CrDTO> crData = Mockito.spy(ArrayList.class);
    crData.add(crDTO1);
    PowerMockito.when(
        crRepository
            .getCrByIdAndResolveStatuṣ̣(anyList(), anyLong())
    ).thenReturn(crData);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setCrNumber("11");
    crDTO.setCrReturnResolve("111");
    crDTO.setUserLogin("121212");
    crDTO.setProcessTypeId("1111");
    crDTO.setActionReturnCodeId("39");
    crDTO.setChangeOrginatorUnit("20");
    String actual = crMobileBusiness.actionResolveCr(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionCloseCr_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");
    PowerMockito.when(
        crBusiness.actionCloseCr(any(), anyString())
    ).thenReturn(RESULT.SUCCESS);

    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    CrCreatedFromOtherSysDTO dto = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    dto.setStatus("OK");
    dto.setSystemId("1");
    PowerMockito.when(
        crForOtherSystemBusiness.getCrCreatedFromOtherSysDTO(anyLong())
    ).thenReturn(dto);

    CrProcessWoDTO crProcessWoDTO = Mockito.spy(CrProcessWoDTO.class);
    crProcessWoDTO.setCreateWoWhenCloseCR(1L);
    List<CrProcessWoDTO> lstCrWo = Mockito.spy(ArrayList.class);
    lstCrWo.add(crProcessWoDTO);
    PowerMockito.when(
        crProcessWoRepository.getLstWoFromProcessId(anyString())
    ).thenReturn(lstCrWo);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionReturnCodeId("42");
    crDTO.setProcessTypeId("2");
    crDTO.setChangeResponsibleUnit("10");
    String actual = crMobileBusiness.actionCloseCr(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionCloseCr_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");
    PowerMockito.when(
        crBusiness.actionCloseCr(any(), anyString())
    ).thenReturn(RESULT.SUCCESS);

    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(formRoot);

    CrCreatedFromOtherSysDTO dto = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    dto.setStatus("OK");
    dto.setSystemId("1");
    PowerMockito.when(
        crForOtherSystemBusiness.getCrCreatedFromOtherSysDTO(anyLong())
    ).thenReturn(dto);

    CrProcessWoDTO crProcessWoDTO = Mockito.spy(CrProcessWoDTO.class);
    crProcessWoDTO.setCreateWoWhenCloseCR(1L);
    List<CrProcessWoDTO> lstCrWo = Mockito.spy(ArrayList.class);
    lstCrWo.add(crProcessWoDTO);
    PowerMockito.when(
        crProcessWoRepository.getLstWoFromProcessId(anyString())
    ).thenReturn(lstCrWo);

    CoordinationSettingDTO setting = Mockito.spy(CoordinationSettingDTO.class);
    PowerMockito.when(
        coordinationSettingRepository.getCoordinationSettingInfor(anyLong(), any())
    ).thenReturn(setting);

    WoDTO wdto = Mockito.spy(WoDTO.class);
    PowerMockito.when(
        crProcessFromClient.createWoCMDB(any(), any(), any())
    ).thenReturn(wdto);

    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    PowerMockito.when(
        woServiceProxy.insertWoForSPMProxy(any())
    ).thenReturn(resultDTO);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionReturnCodeId("42");
    crDTO.setProcessTypeId("2");
    crDTO.setChangeResponsibleUnit("10");
    String actual = crMobileBusiness.actionCloseCr(crDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void testActionCab() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    PowerMockito.when(
        crBusiness.actionCab(any(), anyString())
    ).thenReturn(RESULT.SUCCESS);

    CrInsiteDTO dTO = Mockito.spy(CrInsiteDTO.class);
    dTO.setCrType("0");
    dTO.setRisk("1");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(dTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoId("11");
    woDTOSearch.setWoCode("WO_CR_TEST_SERVICE_AUTO_GENERATE");
    woDTOSearch.setWoTypeId("22");
    woDTOSearch.setWoTypeCode("2222");
    woDTOSearch.setCreateDate("11/05/2020 09:00:00");
    woDTOSearch.setStatus("Status");
    woDTOSearch.setStartTime("11/05/2020 09:00:00");
    woDTOSearch.setEndTime("11/05/3000 09:00:00");
    woDTOSearch.setWoContent("WoContent");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrId("1");
    crDTO.setUserLogin("121212");
    String actual = crMobileBusiness.actionCab(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionEditCr() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    PowerMockito.when(
        crBusiness.actionEditCr(any(), anyString())
    ).thenReturn(RESULT.SUCCESS);

    CrInsiteDTO dTO = Mockito.spy(CrInsiteDTO.class);
    dTO.setCrId("1");
    dTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("11/05/2020 12:00:00")
    );
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(dTO);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setWoId("11");
    woDTOSearch.setWoCode("WO_CR_TEST_SERVICE_AUTO_GENERATE");
    woDTOSearch.setWoTypeId("22");
    woDTOSearch.setWoTypeCode("2222");
    woDTOSearch.setCreateDate("11/05/2020 09:00:00");
    woDTOSearch.setStatus("Status");
    woDTOSearch.setStartTime("11/05/2020 09:00:00");
    woDTOSearch.setEndTime("11/05/3000 09:00:00");
    woDTOSearch.setWoContent("WoContent");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setCrId("1");
    crDTO.setUserLogin("121212");
    String actual = crMobileBusiness.actionEditCr(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }
}

