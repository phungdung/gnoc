
package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.aam.AppGroup;
import com.viettel.aam.AppGroupResult;
import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.business.RoleUserBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.incident.provider.WSNocprov4Port;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.proxy.PtServiceProxy;
import com.viettel.gnoc.commons.proxy.RiskServiceProxy;
import com.viettel.gnoc.commons.proxy.SrServiceProxy;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.WORK_LOG_SYSTEM;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.AppGroupInsite;
import com.viettel.gnoc.cr.dto.AttachDtDTO;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrModuleDetailDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.CrUpdateMopStatusHisDTO;
import com.viettel.gnoc.cr.dto.CrVendorDetailDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.dto.UserCabCrForm;
import com.viettel.gnoc.cr.dto.UserReceiveMsgDTO;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.cr.repository.CrAffectedNodeRepository;
import com.viettel.gnoc.cr.repository.CrAffectedServiceDetailsRepository;
import com.viettel.gnoc.cr.repository.CrAlarmRepository;
import com.viettel.gnoc.cr.repository.CrApprovalDepartmentRepository;
import com.viettel.gnoc.cr.repository.CrCableRepository;
import com.viettel.gnoc.cr.repository.CrDBRepository;
import com.viettel.gnoc.cr.repository.CrFilesAttachRepository;
import com.viettel.gnoc.cr.repository.CrGeneralRepository;
import com.viettel.gnoc.cr.repository.CrHisRepository;
import com.viettel.gnoc.cr.repository.CrImpactedNodesRepository;
import com.viettel.gnoc.cr.repository.CrProcessRepository;
import com.viettel.gnoc.cr.repository.CrProcessWoRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.gnoc.cr.repository.UserReceiveMsgRepository;
import com.viettel.gnoc.cr.util.CrGeneralUtil;
import com.viettel.gnoc.cr.util.CrProcessFromClient;
import com.viettel.gnoc.cr.util.MrCategoryUtil;
import com.viettel.gnoc.risk.dto.RiskDTOSearch;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.wfm.dto.WoSearchDTO;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.gnoc.ws.provider.NocProWS;
import com.viettel.gnoc.ws.provider.WSGatePort;
import com.viettel.gnoc.ws.provider.WSTDTTPort;
import com.viettel.gnoc.ws.provider.WSVipaDdPort;
import com.viettel.gnoc.ws.provider.WSVipaIpPort;
import com.viettel.security.PassTranformer;
import com.viettel.vmsa.MopDetailOutputDTO;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;
@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({CrBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    StringUtils.class, DateUtil.class, TicketProvider.class, DataUtil.class, CommonExport.class,
    CrGeneralUtil.class, DateTimeUtils.class, JAXBContext.class, NocProWS.class, PassTranformer.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrBusinessImplTest {

  @InjectMocks
  CrBusinessImpl crBusiness;

  @Mock
  CrRepository crRepository;

  @Mock
  RoleUserBusiness roleUserBusiness;

  @Mock
  CrDBRepository crDBRepository;

  @Mock
  CrProcessFromClient crProcessFromClient;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CrApprovalDepartmentRepository crApprovalDepartmentRepository;

  @Mock
  CrImpactedNodesRepository crImpactedNodesRepository;

  @Mock
  CrGeneralRepository crGeneralRepository;

  @Mock
  CrAffectedNodeRepository crAffectedNodeRepository;

  @Mock
  CrAffectedServiceDetailsRepository crAffectedServiceDetailsRepository;

  @Mock
  CrAlarmRepository crAlarmRepository;

  @Mock
  MrCategoryProxy mrCategoryProxy;

  @Mock
  PtServiceProxy ptServiceProxy;

  @Mock
  TtServiceProxy ttServiceProxy;

  @Mock
  CrCableRepository crCableRepository;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  CrHisRepository crHisRepository;

  @Mock
  CrForOtherSystemBusiness crForOtherSystemBusiness;

  @Mock
  WSTDTTPort wstdttPort;

  @Mock
  WSVipaDdPort wsVipaDdPort;

  @Mock
  WSVipaIpPort wsVipaIpPort;

  @Mock
  SrServiceProxy srServiceProxy;

  @Mock
  CrFilesAttachRepository crFilesAttachRepository;

  @Mock
  RiskServiceProxy riskServiceProxy;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  SmsDBBussiness smsDBBussiness;

  @Mock
  UserBusiness userBusiness;

  @Mock
  UnitRepository unitRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  UserReceiveMsgRepository userReceiveMsgRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  MessagesBusiness messagesBusiness;

  @Mock
  CrProcessRepository crProcessRepository;

  @Mock
  CrProcessWoRepository crProcessWoRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  CommonRepository commonRepository;

  @Mock
  WSGatePort wsGatePort;

  @Mock
  WSNocprov4Port wsNocprov4Port;

  @Mock
  MrCategoryUtil mrCategoryUtil;

  @Before
  public void setUpCrResolveSuccess() {
    ReflectionTestUtils.setField(crBusiness, "crResolveSuccess",
        "39");
  }

  @Before
  public void setUpCrCloseReturnCodeId() {
    ReflectionTestUtils.setField(crBusiness, "crCloseReturnCodeId",
        "27");
  }

  @Before
  public void setUpCrCloseActionReturnCodeId() {
    ReflectionTestUtils.setField(crBusiness, "crCloseActionReturnCodeId",
        "42");
  }

  @Before
  public void setUpSystemUserName() {
    ReflectionTestUtils.setField(crBusiness, "systemUserName",
        "thanhlv12");
  }


  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(crBusiness, "uploadFolder",
        "/uploadFolder");
    ReflectionTestUtils.setField(crBusiness, "ftpFolder",
        "/ftpFolder");
    ReflectionTestUtils.setField(crBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(crBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(crBusiness, "ftpServer",
        "10.240.232.25");
    ReflectionTestUtils.setField(crBusiness, "ftpPort",
        21);
  }

  @Test
  public void getListCRBySearchTypePagging_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(121212L);
    userToken.setDeptId(11L);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    Datatable expected = Mockito.spy(Datatable.class);
    PowerMockito.when(
        crRepository.getListCRBySearchType(any(), anyString())
    ).thenReturn(expected);

    UserReceiveMsgDTO userReceiveMsgDTO = Mockito.spy(UserReceiveMsgDTO.class);
    userReceiveMsgDTO.setCrStartTime(
        DateTimeUtils.convertStringToDate("12/05/2900 08:00:00")
    );
    userReceiveMsgDTO.setCrId(2L);
    List<UserReceiveMsgDTO> lstUserReceive = Mockito.spy(ArrayList.class);
    lstUserReceive.add(userReceiveMsgDTO);
    PowerMockito.when(
        userReceiveMsgRepository
            .getListUserReceiveMsgDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstUserReceive);

    UserCabCrForm userCabCrForm = Mockito.spy(UserCabCrForm.class);
    userCabCrForm.setImpactSegmentId("11");
    userCabCrForm.setExecuteUnitId("11");
    userCabCrForm.setUserCab("555");
    userCabCrForm.setUsername("Username");
    List<UserCabCrForm> userCabCrForms = Mockito.spy(ArrayList.class);
    userCabCrForms.add(userCabCrForm);
    PowerMockito.when(
        crGeneralRepository.getListUserCab(any(), any())
    ).thenReturn(userCabCrForms);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setImpactSegment("11");
    crInsiteDTO.setChangeResponsibleUnit("11");
    crInsiteDTO.setUserCab("555");
    crInsiteDTO.setCrId("2");
    CrInsiteDTO crInsiteDTO1 = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO1.setImpactSegment("33");
    crInsiteDTO1.setChangeResponsibleUnit("33");
    crInsiteDTO1.setChangeOrginatorUnit("33");
    crInsiteDTO1.setUserCab("555");
    crInsiteDTO1.setCrId("3");
    List<CrInsiteDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(crInsiteDTO);
    lstData.add(crInsiteDTO1);
    expected.setData(lstData);

    UsersEntity us = Mockito.spy(UsersEntity.class);
    us.setUsername("windy");
    PowerMockito.when(
        userBusiness.getUserByUserId(anyLong())
    ).thenReturn(us);

    List<Long> longList = new ArrayList<>();
    longList.add(1L);
    longList.add(2L);
    PowerMockito.when(
        crRepository
            .getlistCrIdsByNodeInfo(any(), any(), anyList())
    ).thenReturn(longList);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setSearchImpactedNodeIpIds(longList);
    crDTO.setEarliestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 08:00:00")
    );
    crDTO.setEarliestStartTimeTo("12/05/3000 12:00:00");
    Datatable actual = crBusiness.getListCRBySearchTypePagging(crDTO, null);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getSequenseCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<String> expected = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        crRepository.getSequenseCr(anyString(), any())
    ).thenReturn(expected);
    List<String> actual = crBusiness.getSequenseCr("SEQ", 1);
    Assert.assertEquals(expected.size(), actual.size());
  }

  @Test
  public void getListSecondaryCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CrInsiteDTO> crInsiteDTOS = Mockito.spy(ArrayList.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.getListSecondaryCr(any())).thenReturn(crInsiteDTOS);
    List<CrInsiteDTO> crInsiteDTOS1 = crBusiness.getListSecondaryCr(crInsiteDTO);
    Assert.assertEquals(crInsiteDTOS.size(), crInsiteDTOS1.size());
  }

  @Test
  public void getListPreApprovedCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CrInsiteDTO> crInsiteDTOS = Mockito.spy(ArrayList.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.getListPreApprovedCr(any())).thenReturn(crInsiteDTOS);
    List<CrInsiteDTO> crInsiteDTOS1 = crBusiness.getListPreApprovedCr(crInsiteDTO);
    Assert.assertEquals(crInsiteDTOS.size(), crInsiteDTOS1.size());
  }

  @Test
  public void findCrById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    List<CrAffectedServiceDetailsDTO> lstCrAffectedServiceDetailsDTOs = Mockito
        .spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(crRepository.findCrById(any(), any())).thenReturn(crInsiteDTO);
    PowerMockito.when(crAffectedServiceDetailsRepository
        .search(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstCrAffectedServiceDetailsDTOs);
    CrInsiteDTO crInsiteDTO1 = crBusiness.findCrById(1L);
    Assert.assertNotNull(crInsiteDTO1);
  }

  @Test
  public void getCrById_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.findCrById(anyLong(), any())).thenReturn(crInsiteDTO);
    UserToken userToken = Mockito.spy(UserToken.class);
    CrInsiteDTO crInsiteDTO1 = crBusiness.getCrById(1L, userToken);
    Assert.assertNotNull(crInsiteDTO1);
  }

  @Test
  public void getListScopeOfUserForAllRole_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setDeptId(413314L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    List<ItemDataCRInside> itemDataCRInsides = Mockito.spy(ArrayList.class);
    PowerMockito.when(crGeneralRepository.getListScopeOfUserForAllRole(any(), any()))
        .thenReturn(itemDataCRInsides);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    List<ItemDataCRInside> itemDataCRInsides1 = crBusiness
        .getListScopeOfUserForAllRole(crInsiteDTO);
    Assert.assertEquals(itemDataCRInsides.size(), itemDataCRInsides1.size());
  }

  @Test
  public void getNetworkNodeFromQLTN_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.doNothing().when(logger).debug(any());
    AppGroup appGroup = Mockito.spy(AppGroup.class);
    List<AppGroup> appGroups = Mockito.spy(ArrayList.class);
    AppGroupInsite appGroupInsite =  Mockito.spy(AppGroupInsite.class);
    appGroupInsite.setPage(1);
    appGroupInsite.setPageSize(1);
    appGroups.add(appGroup);
    appGroupInsite.setLstAppGroup(appGroups);
    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    List<InfraDeviceDTO> dataSuList = Mockito.spy(ArrayList.class);
    dataSuList.add(infraDeviceDTO);
    PowerMockito.when((List<InfraDeviceDTO>)  DataUtil
        .subPageList(any(), anyInt(), anyInt())).thenReturn(dataSuList);
    PowerMockito.when(crGeneralRepository.getNetworkNodeFromQLTN(anyList())).thenReturn(appGroups);
    Datatable appGroups1 = crBusiness.getNetworkNodeFromQLTN(appGroupInsite);
    Assert.assertEquals(appGroups.size(), appGroups1.getData().size());
  }

  @Test
  public void actionApproveCR_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(StringUtils.class);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.actionApproveCR(any(), any())).thenReturn(RESULT.SUCCESS);
    String res = crBusiness.actionApproveCR(crDTO, "vi_VN");
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void actionVerify_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(crRepository.actionVerify(any(), any())).thenReturn(RESULT.SUCCESS);
    String res = crBusiness.actionVerify(crDTO, "vi_VN");
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void actionReceiveCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(StringUtils.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.actionReceiveCr(any(), any())).thenReturn(RESULT.SUCCESS);
    String res = crBusiness.actionReceiveCr(crInsiteDTO, "vi_VN");
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void actionResolveCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(StringUtils.class);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.actionResolveCr(any(), any())).thenReturn(RESULT.SUCCESS);
    String res = crBusiness.actionResolveCr(crDTO, "vi_VN");
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void actionCloseCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(StringUtils.class);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.actionCloseCr(any(), any())).thenReturn(RESULT.SUCCESS);
    String res = crBusiness.actionCloseCr(crDTO, "vi_VN");
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void actionCloseGeneralCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(crProcessFromClient.actionCloseCrAfter(any())).thenReturn("");
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.actionCloseCr(any(), any())).thenReturn(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setDeptId(413314L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto1 = crBusiness.actionCloseGeneralCr(crDTO, "vi_VN");
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void actionCloseGeneralCr_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(crProcessFromClient.actionCloseCrAfter(any())).thenReturn("fixBug");
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.actionCloseCr(any(), any())).thenReturn(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setDeptId(413314L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto1 = crBusiness.actionCloseGeneralCr(crDTO, "vi_VN");
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.ERROR);
  }

  @Test
  public void actionAssignCabCRGeneral_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionType("40");
    crDTO.setCrType("0");
    crDTO.setRisk("1");
    PowerMockito.when(crRepository.actionCab(any(), any())).thenReturn(RESULT.SUCCESS);
    PowerMockito.mockStatic(CrGeneralUtil.class);
    PowerMockito.when(CrGeneralUtil.generateStateForAssignCabCR(any())).thenReturn(1L);
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.findCrById(any(), any())).thenReturn(formRoot);
    PowerMockito.when(crProcessFromClient.getFormForProcess(any())).thenReturn(crDTO);
    PowerMockito.when(crProcessFromClient.validateActionClick(any())).thenReturn("");
    ResultInSideDto resultInSideDto = crBusiness.actionAssignCabCRGeneral(crDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void actionAssignCabCRGeneral_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionType("40");
    crDTO.setCrType("0");
    crDTO.setRisk("1");
    PowerMockito.when(crRepository.actionCab(any(), any())).thenReturn(RESULT.SUCCESS);
    PowerMockito.mockStatic(CrGeneralUtil.class);
    PowerMockito.when(CrGeneralUtil.generateStateForAssignCabCR(any())).thenReturn(1L);
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.findCrById(any(), any())).thenReturn(formRoot);
    PowerMockito.when(crProcessFromClient.getFormForProcess(any())).thenReturn(crDTO);
    PowerMockito.when(crProcessFromClient.validateActionClick(any())).thenReturn(RESULT.SUCCESS);
    ResultInSideDto resultInSideDto = crBusiness.actionAssignCabCRGeneral(crDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
  }

  @Test
  public void actionAssignCabCRGeneral_03() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionType("41");
    crDTO.setCrType("0");
    crDTO.setRisk("1");
    PowerMockito.when(crRepository.actionAssignCab(any(), any())).thenReturn(RESULT.SUCCESS);
    PowerMockito.mockStatic(CrGeneralUtil.class);
    PowerMockito.when(CrGeneralUtil.generateStateForAssignCabCR(any())).thenReturn(1L);
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.findCrById(any(), any())).thenReturn(formRoot);
    PowerMockito.when(crProcessFromClient.getFormForProcess(any())).thenReturn(crDTO);
    PowerMockito.when(crProcessFromClient.validateActionClick(any())).thenReturn("");
    ResultInSideDto resultInSideDto = crBusiness.actionAssignCabCRGeneral(crDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void actionCabCRGeneral_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionType("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(crRepository.actionCab(any(), any())).thenReturn(RESULT.SUCCESS);
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.findCrById(any(), any())).thenReturn(formRoot);
    PowerMockito.when(crProcessFromClient.getFormForProcess(any())).thenReturn(crDTO);
    PowerMockito.when(crProcessFromClient.validateActionClick(any())).thenReturn("");
    PowerMockito.mockStatic(CrGeneralUtil.class);
    PowerMockito.when(CrGeneralUtil.generateStateForCabCR(any())).thenReturn(1L);
    ResultInSideDto resultInSideDto = crBusiness.actionCabCRGeneral(crDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void actionCabCRGeneral_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionType("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(crRepository.actionCab(any(), any())).thenReturn(RESULT.SUCCESS);
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.findCrById(any(), any())).thenReturn(formRoot);
    PowerMockito.when(crProcessFromClient.getFormForProcess(any())).thenReturn(crDTO);
    PowerMockito.when(crProcessFromClient.validateActionClick(any())).thenReturn(RESULT.ERROR);
    PowerMockito.mockStatic(CrGeneralUtil.class);
    PowerMockito.when(CrGeneralUtil.generateStateForCabCR(any())).thenReturn(1L);
    ResultInSideDto resultInSideDto = crBusiness.actionCabCRGeneral(crDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
  }

  @Test
  public void actionEditCRGeneral_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionType("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(crRepository.actionCab(any(), any())).thenReturn(RESULT.SUCCESS);
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.findCrById(any(), any())).thenReturn(formRoot);
    PowerMockito.when(crProcessFromClient.getFormForProcess(any())).thenReturn(crDTO);
    PowerMockito.when(crProcessFromClient.validateActionClick(any())).thenReturn("");
    PowerMockito.mockStatic(CrGeneralUtil.class);
    PowerMockito.when(CrGeneralUtil.generateStateForCabCR(any())).thenReturn(1L);
    ResultInSideDto resultInSideDto = crBusiness.actionEditCRGeneral(crDTO);
    Assert.assertNull(resultInSideDto.getKey());
  }

  @Test
  public void actionEditCRGeneral_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrId("1");
    crDTO.setActionType("1");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(crRepository.actionCab(any(), any())).thenReturn(RESULT.SUCCESS);
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.findCrById(any(), any())).thenReturn(formRoot);
    PowerMockito.when(crProcessFromClient.getFormForProcess(any())).thenReturn(crDTO);
    PowerMockito.when(crProcessFromClient.validateActionClick(any())).thenReturn(RESULT.ERROR);
    PowerMockito.mockStatic(CrGeneralUtil.class);
    PowerMockito.when(CrGeneralUtil.generateStateForCabCR(any())).thenReturn(1L);
    ResultInSideDto resultInSideDto = crBusiness.actionEditCRGeneral(crDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
  }

  @Test
  public void actionCancelCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(crRepository.actionCancelCR(any())).thenReturn(RESULT.SUCCESS);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrId("1");
    crDTO.setNotes("fixBug");
    crDTO.setUserLogin("thanhlv12");
    crDTO.setUserLoginUnit("thanhlv12");
    crDTO.setEarliestStartTime(new Date());
    crDTO.setLatestStartTime(new Date());
    PowerMockito.when(crHisRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);
    String res = crBusiness.actionCancelCr(crDTO);
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void showRR_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userTokenGNOC = Mockito.spy(UserToken.class);
    userTokenGNOC.setUserName("thanhlv12");
    List<RiskDTOSearch> lst = Mockito.spy(ArrayList.class);
    RiskDTOSearch c = Mockito.spy(RiskDTOSearch.class);
    c.setRiskId("1");
    c.setRiskCode("1");
    lst.add(c);
    PowerMockito.when(
        riskServiceProxy.getListDataSearchForOther(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);
    Datatable datatable = crBusiness.showRR(userTokenGNOC, "1", 1, 1);
    Assert.assertEquals(datatable.getPages(), 0L);
  }

  @Test
  public void showSR_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userTokenGNOC = Mockito.spy(UserToken.class);
    userTokenGNOC.setUserName("thanhlv12");

    List<SRDTO> lst = Mockito.spy(ArrayList.class);
    SRDTO srdto = Mockito.spy(SRDTO.class);
    srdto.setSrId("1");
    srdto.setSrCode("1");
    srdto.setKey("5");
    lst.add(srdto);
    PowerMockito.when(srServiceProxy.getListSRForLinkCR(anyString(), anyString())).thenReturn(lst);
    PowerMockito.mockStatic(DataUtil.class);
    List<SRDTO> crSubList = Mockito.spy(ArrayList.class);
    crSubList.add(srdto);
    PowerMockito.when((List<SRDTO>) DataUtil.subPageList(lst, 1, 1)).thenReturn(crSubList);
    Datatable datatable = crBusiness.showSR(userTokenGNOC, "1", 1, 1);
    Assert.assertEquals(datatable.getPages(), 0L);
  }

  @Test
  public void showWo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userTokenGNOC = Mockito.spy(UserToken.class);
    userTokenGNOC.setUserID(999999L);
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    WoDTOSearch c = Mockito.spy(WoDTOSearch.class);
    c.setWoId("1");
    c.setWoCode("1");
    lstWo.add(c);
    PowerMockito.when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstWo);
    Datatable datatable = crBusiness.showWo(userTokenGNOC, "1", 1, 1);
    Assert.assertEquals(datatable.getPages(), 0L);
  }

  @Test
  public void showTT_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userTokenGNOC = Mockito.spy(UserToken.class);
    userTokenGNOC.setUserID(999999L);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(ttServiceProxy.searchParentTTForCR(any())).thenReturn(datatable);
    Datatable datatable1 = crBusiness.showTT(userTokenGNOC, "1", 1, 1);
    Assert.assertEquals(datatable1.getPages(), 0L);
  }

  @Test
  public void showPT_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userTokenGNOC = Mockito.spy(UserToken.class);
    userTokenGNOC.setUserID(999999L);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(ptServiceProxy.searchParentPTForCR(any())).thenReturn(datatable);
    Datatable datatable1 = crBusiness.showPT(userTokenGNOC, "1", 1, 1);
    Assert.assertEquals(datatable1.getPages(), 0L);
  }

  @Test
  public void checkDuplicateCr_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<CrInsiteDTO> lstCR = Mockito.spy(ArrayList.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    lstCR.add(crInsiteDTO);
    PowerMockito.when(crProcessFromClient.validateCheckImpact(any(), any())).thenReturn(lstCR);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    List<InfraDeviceDTO> lstExport = Mockito.spy(ArrayList.class);
    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    lstExport.add(infraDeviceDTO);
    PowerMockito.when(crProcessFromClient.getDataExport(anyList())).thenReturn(lstExport);
    File file = new File("input.txt");
    PowerMockito.when(crProcessFromClient
        .exportFileEx(anyList(), anyList(), anyString(), anyString(), anyString(), anyString(),
            anyString())).thenReturn(file);
    ResultInSideDto resultInSideDto = crBusiness.checkDuplicateCr(crInsiteDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void checkDuplicateCr_02() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<CrInsiteDTO> lstCR = Mockito.spy(ArrayList.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    PowerMockito.when(crProcessFromClient.validateCheckImpact(any(), any())).thenReturn(lstCR);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    List<InfraDeviceDTO> lstExport = Mockito.spy(ArrayList.class);
    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    lstExport.add(infraDeviceDTO);
    PowerMockito.when(crProcessFromClient.getDataExport(anyList())).thenReturn(lstExport);
    File file = new File("input.txt");
    PowerMockito.when(crProcessFromClient
        .exportFileEx(anyList(), anyList(), anyString(), anyString(), anyString(), anyString(),
            anyString())).thenReturn(file);
    ResultInSideDto resultInSideDto = crBusiness.checkDuplicateCr(crInsiteDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
  }

  @Test
  public void checkDuplicateCr_03() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<CrInsiteDTO> lstCR = Mockito.spy(ArrayList.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrNumber("1");
    lstCR.add(crInsiteDTO);
    PowerMockito.when(crProcessFromClient.validateCheckImpact(any(), any())).thenReturn(lstCR);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    List<InfraDeviceDTO> lstExport = Mockito.spy(ArrayList.class);
    PowerMockito.when(crProcessFromClient.getDataExport(anyList())).thenReturn(lstExport);
    File file = new File("input.txt");
    PowerMockito.when(crProcessFromClient
        .exportFileEx(anyList(), anyList(), anyString(), anyString(), anyString(), anyString(),
            anyString())).thenReturn(file);
    ResultInSideDto resultInSideDto = crBusiness.checkDuplicateCr(crInsiteDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
  }

  @Test
  public void exportSearchData_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setUserLogin("thanhlv12");
    crDTO.setUserLoginUnit("thanhlv12");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setDeptId(413314L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    List<CrInsiteDTO> lstExport = Mockito.spy(ArrayList.class);
    lstExport.add(crDTO);
    PowerMockito.when(crProcessFromClient.getListCrDTO(any(), anyBoolean())).thenReturn(lstExport);
    PowerMockito.when(crProcessFromClient.convertKey2StringListCR(anyList())).thenReturn(lstExport);
    File file = new File("input.txt");
    try {
      PowerMockito.when(crProcessFromClient
          .exportFileEx(anyList(), anyList(), anyString(), anyString(), anyString(), anyString(),
              anyString())).thenReturn(file);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    File file1 = crBusiness.exportSearchData(crDTO);
    Assert.assertNotNull(file1);
  }

  @Test
  public void exportSearchData_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setDeptId(413314L);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    List<CrInsiteDTO> lstExport = Mockito.spy(ArrayList.class);
    lstExport.add(crDTO);
    PowerMockito.when(crProcessFromClient.getListCrDTO(any(), anyBoolean())).thenReturn(lstExport);
    PowerMockito.when(crProcessFromClient.convertKey2StringListCR(anyList())).thenReturn(lstExport);
    File file = new File("input.txt");
    try {
      PowerMockito.when(crProcessFromClient
          .exportFileEx(anyList(), anyList(), anyString(), anyString(), anyString(), anyString(),
              anyString())).thenReturn(file);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    File file1 = crBusiness.exportSearchData(crDTO);
    Assert.assertNotNull(file1);
  }

  @Test
  public void loadWorkOrder_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    PowerMockito.mockStatic(DateUtil.class);
    WoSearchDTO woSearchDTO = Mockito.spy(WoSearchDTO.class);
    woSearchDTO.setSortType("asc");
    woSearchDTO.setSortName("ID");
    woSearchDTO.setPage(1);
    woSearchDTO.setPageSize(1);
    woSearchDTO.setCrCreateDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    woSearchDTO.setCrId("1");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("1");
    woDTOSearch.setWoId("1");
    woDTOSearch.setStartTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    woDTOSearch.setEndTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    woDTOSearch.setLastUpdateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    woDTOSearch.setFinishTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    woDTOSearch.setWoContent("fixBug");
    woDTOSearch.setWoCode("1");
    woDTOSearch.setWoTypeName("fixBug");
    woDTOSearch.setCdName("vtnet");
    List<WoDTOSearch> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lstWo);
    List<WoSearchDTO> crSubList = Mockito.spy(ArrayList.class);
    crSubList.add(woSearchDTO);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.when((List<WoSearchDTO>) DataUtil.subPageList(anyList(), anyInt(), anyInt()))
        .thenReturn(crSubList);
    Datatable datatable = crBusiness.loadWorkOrder(woSearchDTO);
    Assert.assertEquals(datatable.getPages(), 1L);
  }

  @Test
  public void getListWoTypeDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoTypeInsideDTO> woTypeDTOS = Mockito.spy(ArrayList.class);
    PowerMockito
        .when(woCategoryServiceProxy.getListWoTypeDTO(any()))
        .thenReturn(woTypeDTOS);
    WoTypeInsideDTO woTypeDTO = Mockito.spy(WoTypeInsideDTO.class);
    List<WoTypeInsideDTO> woTypeDTOS1 = crBusiness.getListWoTypeDTO(woTypeDTO);
    Assert.assertEquals(woTypeDTOS.size(), woTypeDTOS1.size());
  }

  @Test
  public void getListCrForRelateOrPreApprove_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crDBRepository.getListCrForRelateOrPreApprove(any())).thenReturn(datatable);
    Datatable datatable1 = crBusiness.getListCrForRelateOrPreApprove(crDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getLocale_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String res = crBusiness.getLocale("vi");
    Assert.assertEquals(res, "vi_VN");
  }

  @Test
  public void getLocale_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String res = crBusiness.getLocale("");
    Assert.assertEquals(res, "en_US");
  }

  @Test
  public void getForm_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrBusinessImpl crBusiness1 = Mockito.mock(CrBusinessImpl.class);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setActionType("34");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setDeptId(413314L);
    PowerMockito.doNothing().when(crBusiness1)
        .getForm(isA(CrInsiteDTO.class), isA(UserToken.class));
    crBusiness.getForm(crDTO, userToken);
  }

  @Test
  public void getForm_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrBusinessImpl crBusiness1 = Mockito.mock(CrBusinessImpl.class);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setActionType("30");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setDeptId(413314L);
    PowerMockito.doNothing().when(crBusiness1)
        .getForm(isA(CrInsiteDTO.class), isA(UserToken.class));
    crBusiness.getForm(crDTO, userToken);
  }

  @Test
  public void getListUserGroupBySystem_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<UserGroupCategoryDTO> lstUG = Mockito.spy(ArrayList.class);
    UserGroupCategoryDTO userGroupCategoryDTO = Mockito.spy(UserGroupCategoryDTO.class);
    userGroupCategoryDTO.setUgcySystem(2L);
    lstUG.add(userGroupCategoryDTO);
    PowerMockito.when(mrCategoryUtil.getListUserGroupBySystem(any())).thenReturn(lstUG);
    List<UserGroupCategoryDTO> userGroupCategoryDTOS = crBusiness
        .getListUserGroupBySystem(userGroupCategoryDTO, "1,2");
    Assert.assertEquals(userGroupCategoryDTOS.size(), 0L);
  }

  @Test
  public void getListWorkLogCategoryDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WorkLogCategoryInsideDTO workLogCategoryDTO = Mockito.spy(WorkLogCategoryInsideDTO.class);
    List<WorkLogCategoryInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(workLogCategoryDTO);
    PowerMockito.when(mrCategoryUtil
        .getListWorkLogCategoryDTO(any()))
        .thenReturn(lst);
    List<WorkLogCategoryInsideDTO> workLogCategoryInsideDTOS = crBusiness
        .getListWorkLogCategoryDTO(workLogCategoryDTO);
    Assert.assertEquals(workLogCategoryInsideDTOS.size(), 1L);
  }

  @Test
  public void validate_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WorkLogInsiteDTO workLogInsiteDTO = Mockito.spy(WorkLogInsiteDTO.class);
    workLogInsiteDTO.setWlgObjectType(Long.parseLong(Constants.WORK_LOG_SYSTEM.CR));
    workLogInsiteDTO.setWlgText("1");
    boolean b = crBusiness.validate(workLogInsiteDTO);
    Assert.assertTrue(b);
  }

  @Test
  public void validate_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("xxx");
    WorkLogInsiteDTO workLogInsiteDTO = Mockito.spy(WorkLogInsiteDTO.class);
    workLogInsiteDTO.setWlgObjectType(2L);
    workLogInsiteDTO.setWlgText(null);
    boolean b = crBusiness.validate(workLogInsiteDTO);
    Assert.assertFalse(b);
  }

  @Test
  public void sendSMSToLstUserConfig_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.when(smsDBBussiness.sendSMSToLstUserConfig(anyString(), anyString()))
        .thenReturn(RESULT.SUCCESS);
    String res = crBusiness.sendSMSToLstUserConfig("1", "fixBug");
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void getListWorklogSearch_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WorkLogInsiteDTO> lst = Mockito.spy(ArrayList.class);
    WorkLogInsiteDTO dto = Mockito.spy(WorkLogInsiteDTO.class);
    dto.setPage(1);
    dto.setPageSize(1);
    WorkLogInsiteDTO workLogDTO = Mockito.spy(WorkLogInsiteDTO.class);
    lst.add(workLogDTO);
    Datatable expect = Mockito.spy(Datatable.class);
    expect.setData(lst);
    expect.setTotal(1);
    PowerMockito.when(mrCategoryUtil.getListWorklogSearch(any())).thenReturn(expect);
    Datatable datatable = crBusiness.getListWorklogSearch(dto);
    Assert.assertEquals(datatable.getTotal(), 1L);
  }

  @Test
  public void getListCdGroupByUser_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoCdGroupInsideDTO> woCdGroupInsideDTOS = Mockito.spy(ArrayList.class);
    WoCdGroupTypeUserDTO woCdGroupTypeUserDTO = Mockito.spy(WoCdGroupTypeUserDTO.class);
    PowerMockito.when(woCategoryServiceProxy.getListCdGroupByUser(any()))
        .thenReturn(woCdGroupInsideDTOS);
    List<WoCdGroupInsideDTO> woCdGroupInsideDTOS1 = crBusiness
        .getListCdGroupByUser(woCdGroupTypeUserDTO);
    Assert.assertEquals(woCdGroupInsideDTOS1.size(), 0L);
  }

  @Test
  public void getListWoCdGroupTypeDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoCdGroupTypeDTO> woCdGroupInsideDTOS = Mockito.spy(ArrayList.class);
    WoCdGroupTypeDTO woCdGroupTypeDTO = Mockito.spy(WoCdGroupTypeDTO.class);
    PowerMockito.when(woCategoryServiceProxy.getListWoCdGroupTypeDTO(any()))
        .thenReturn(woCdGroupInsideDTOS);
    List<WoCdGroupTypeDTO> woCdGroupTypeDTOS = crBusiness.getListWoCdGroupTypeDTO(woCdGroupTypeDTO);
    Assert.assertEquals(woCdGroupTypeDTOS.size(), 0L);
  }

  @Test
  public void getListWoPriorityDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<WoPriorityDTO> woCdGroupInsideDTOS = Mockito.spy(ArrayList.class);
    WoPriorityDTO woCdGroupTypeDTO = Mockito.spy(WoPriorityDTO.class);
    PowerMockito.when(woCategoryServiceProxy.getListWoPriorityDTO(any()))
        .thenReturn(woCdGroupInsideDTOS);
    List<WoPriorityDTO> woPriorityDTOS = crBusiness.getListWoPriorityDTO(woCdGroupTypeDTO);
    Assert.assertEquals(woPriorityDTOS.size(), 0L);
  }

  @Test
  public void loadCRRelated_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crDBRepository.loadCRRelated(any())).thenReturn(datatable);
    Datatable datatable1 = crBusiness.loadCRRelated(crInsiteDTO);
    Assert.assertEquals(datatable1.getPages(), 0L);
  }

  @Test
  public void actionAssignCabMulti_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setDeptId(413314L);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(crRepository.actionAssignCab(any(), any())).thenReturn(RESULT.SUCCESS);
    List<CrInsiteDTO> crInsiteDTOS = Mockito.spy(ArrayList.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setUserCab("thanhlv12");
    crInsiteDTO.setCrNumber("1");
    crInsiteDTOS.add(crInsiteDTO);
    ResultInSideDto resultInSideDto = crBusiness.actionAssignCabMulti(crInsiteDTOS, "vi_VN");
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void actionGetListDuplicateCRImpactedNode_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<CrInsiteDTO> crInsiteDTOS = Mockito.spy(ArrayList.class);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTOS.add(crDTO);
    try {
      PowerMockito.when(crDBRepository.actionGetListDuplicateCRImpactedNode(any()))
          .thenReturn(crInsiteDTOS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    List<CrInsiteDTO> crInsiteDTOS1 = crBusiness.actionGetListDuplicateCRImpactedNode(crDTO);
    Assert.assertEquals(crInsiteDTOS1.size(), 1L);
  }
//
//  @Test
//  public void findWorkLogCategoryById_01() {
//    Logger logger = Mockito.spy(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//    WorkLogCategoryDTO workLogCategoryDTO = Mockito.spy(WorkLogCategoryDTO.class);
//    PowerMockito.when(mrCategoryProxy.findWorkLogCategoryById(anyLong()))
//        .thenReturn(workLogCategoryDTO);
//    WorkLogCategoryDTO workLogCategoryDTO1 = crBusiness.findWorkLogCategoryById(1L);
//    Assert.assertNotNull(workLogCategoryDTO1);
//  }

  @Test
  public void getListCRFromOtherSystem_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.getListCRFromOtherSystem(any())).thenReturn(datatable);
    Datatable datatable1 = crBusiness.getListCRFromOtherSystem(crInsiteDTO);
    Assert.assertEquals(datatable1.getPages(), 0L);
  }

  @Test
  public void changeCheckboxAction_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(any())).thenReturn(RESULT.SUCCESS);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setIsCheckAction(true);
    crDTO.setCrId("1");
    crDTO.setCrNumber("1");
    crDTO.setCreatedDate(new Date());
    crDTO.setEarliestStartTime(new Date());
    crDTO.setLatestStartTime(new Date());
    crDTO.setEarliestStartTimeTo(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    PowerMockito.when(userReceiveMsgRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);
    UserReceiveMsgDTO dto = Mockito.spy(UserReceiveMsgDTO.class);
    dto.setCrStartTime(new Date());
    List<UserReceiveMsgDTO> lstUserReceive = Mockito.spy(ArrayList.class);
    lstUserReceive.add(dto);
    PowerMockito.when(userReceiveMsgRepository
        .getListUserReceiveMsgDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstUserReceive);
    PowerMockito.when(userReceiveMsgRepository.deleteUserReceiveMsg(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crBusiness.changeCheckboxAction(crDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void changeCheckboxAction_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(any())).thenReturn(RESULT.SUCCESS);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setIsCheckAction(true);
    crDTO.setCrId("1");
    crDTO.setCrNumber("1");
    crDTO.setCreatedDate(new Date());
    crDTO.setEarliestStartTime(new Date());
    crDTO.setLatestStartTime(new Date());
    crDTO.setEarliestStartTimeTo(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    PowerMockito.when(userReceiveMsgRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);
    UserReceiveMsgDTO dto = Mockito.spy(UserReceiveMsgDTO.class);
    dto.setCrStartTime(new Date());
    List<UserReceiveMsgDTO> lstUserReceive = Mockito.spy(ArrayList.class);
    lstUserReceive.add(dto);
    PowerMockito.when(userReceiveMsgRepository
        .getListUserReceiveMsgDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstUserReceive);
    PowerMockito.when(userReceiveMsgRepository.deleteUserReceiveMsg(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crBusiness.changeCheckboxAction(crDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.ERROR);
  }

  @Test
  public void changeCheckboxAction_03() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(any())).thenReturn(RESULT.SUCCESS);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setIsCheckAction(false);
    crDTO.setCrId("1");
    crDTO.setCrNumber("1");
    crDTO.setCreatedDate(new Date());
    crDTO.setEarliestStartTime(new Date());
    crDTO.setLatestStartTime(new Date());
    crDTO.setEarliestStartTimeTo(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    PowerMockito.when(userReceiveMsgRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);
    UserReceiveMsgDTO dto = Mockito.spy(UserReceiveMsgDTO.class);
    dto.setCrStartTime(new Date());
    List<UserReceiveMsgDTO> lstUserReceive = Mockito.spy(ArrayList.class);
    lstUserReceive.add(dto);
    PowerMockito.when(userReceiveMsgRepository
        .getListUserReceiveMsgDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstUserReceive);
    PowerMockito.when(userReceiveMsgRepository.deleteUserReceiveMsg(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crBusiness.changeCheckboxAction(crDTO);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void delete_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(crRepository.delete(anyLong())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = crBusiness.delete(1L);
    Assert.assertEquals(resultInSideDto1.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void actionUpdateNotify_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crRepository.actionUpdateNotify(any(), anyLong())).thenReturn(RESULT.SUCCESS);
    String res = crBusiness.actionUpdateNotify(crDTO, 1L);
    Assert.assertEquals(res, RESULT.SUCCESS);
  }

  @Test
  public void getUserInfo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    PowerMockito.when(crRepository.getUserInfo(anyString())).thenReturn(usersDTO);
    UsersDTO usersDTO1 = crBusiness.getUserInfo("thanhlv12");
    Assert.assertNotNull(usersDTO1);
  }

  @Test
  public void getUnitInfo_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    PowerMockito.when(crRepository.getUnitInfo(anyString())).thenReturn(unitDTO);
    UnitDTO unitDTO1 = crBusiness.getUnitInfo("1");
    Assert.assertNotNull(unitDTO1);
  }

  @Test
  public void insertMopUpdateHis_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrUpdateMopStatusHisDTO hisDTO = Mockito.spy(CrUpdateMopStatusHisDTO.class);
    PowerMockito.when(crRepository.insertMopUpdateHis(any())).thenReturn(1);
    int i = crBusiness.insertMopUpdateHis(hisDTO);
    Assert.assertEquals(1, i);
  }

  @Test
  public void getListCRBySearchTypePagging1_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ObjResponse objResponse = Mockito.spy(ObjResponse.class);
    PowerMockito
        .when(crRepository.getListCRBySearchTypePagging(any(), anyInt(), anyInt(), anyString()))
        .thenReturn(objResponse);
    CrDTO crDTO = Mockito.spy(CrDTO.class);
    ObjResponse objResponse1 = crBusiness.getListCRBySearchTypePagging(crDTO, 1, 1, "vi_VN");
    Assert.assertNotNull(objResponse1);
  }

  @Test
  public void getListWorkLogDTO_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WorkLogInsiteDTO workLogDTO = Mockito.spy(WorkLogInsiteDTO.class);
    List<WorkLogInsiteDTO> workLogDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrCategoryUtil.getListWorkLogDTO(any())).thenReturn(workLogDTOS);
    List<WorkLogInsiteDTO> workLogDTOS1 = crBusiness.getListWorkLogDTO(workLogDTO);
    Assert.assertEquals(workLogDTOS.size(), workLogDTOS1.size());
  }

  @Test
  public void actionGetProvinceMonitoringParamFix_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<ResultDTO> resultDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(crRepository
        .actionGetProvinceMonitoringParamFix(anyString(), anyString(), anyString(), anyString(),
            anyString())).thenReturn(resultDTOS);
    List<ResultDTO> resultDTOS1 = crBusiness
        .actionGetProvinceMonitoringParamFix("1", "1", "1", "1", "1");
    Assert.assertEquals(resultDTOS.size(), resultDTOS1.size());
  }

  @Test
  public void getDataTableSecondaryCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crDBRepository.getDataTableSecondaryCr(any())).thenReturn(datatable);
    Datatable datatable1 = crBusiness.getDataTableSecondaryCr(crInsiteDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void getDataTablePreApprovedCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(crDBRepository.getDataTablePreApprovedCr(any())).thenReturn(datatable);
    Datatable datatable1 = crBusiness.getDataTablePreApprovedCr(crInsiteDTO);
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void actionResolveCrAfterToCloseCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setActionReturnCodeId("39");
    crDTO.setProcessTypeId("1");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(crRepository.actionCloseCr(any(), anyString())).thenReturn(RESULT.SUCCESS);
    crDTO.setCrId("1");
    crDTO.setChangeResponsibleUnit("1");
    CrProcessInsideDTO process = Mockito.spy(CrProcessInsideDTO.class);
    process.setCloseCrWhenResolveSuccess(1L);
    PowerMockito.when(crProcessRepository.findCrProcessById(any())).thenReturn(process);
    CrCreatedFromOtherSysDTO dto = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    dto.setStatus("OK");
    dto.setSystemId("4");
    dto.setObjectId("1");
    PowerMockito.when(crForOtherSystemBusiness.getCrCreatedFromOtherSysDTO(any())).thenReturn(dto);
    PowerMockito.when(crProcessFromClient.stringToLong(any())).thenReturn(1L);
    WoDTO wo = Mockito.spy(WoDTO.class);
    wo.setWoTypeId("1");
    wo.setWoCode("1");
    PowerMockito.when(woServiceProxy.findWoByIdWSProxy(any())).thenReturn(wo);
    PowerMockito.when(crProcessFromClient.stringToLong(any())).thenReturn(8L);
    PowerMockito.when(crForOtherSystemBusiness.checkWoCloseAutoSetting(any(), any()))
        .thenReturn(true);
    List<CrCreatedFromOtherSysDTO> crList = Mockito.spy(ArrayList.class);
    CrCreatedFromOtherSysDTO otherDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    otherDTO.setCrId("1");
    crList.add(otherDTO);
    PowerMockito.when(crForOtherSystemBusiness.getListDataByObjectId(any())).thenReturn(crList);
    List<CrDTO> crData = Mockito.spy(ArrayList.class);
    CrDTO crDTO1 = Mockito.spy(CrDTO.class);
    crData.add(crDTO1);
    PowerMockito.when(crRepository.getCrByIdAndResolveStatuṣ̣(any(), any())).thenReturn(crData);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    resultDTO.setMessage(RESULT.SUCCESS);
    PowerMockito.when(woServiceProxy.changeStatusWoProxy(any())).thenReturn(resultDTO);
    String res = crBusiness.actionResolveCrAfterToCloseCr(crDTO);
    Assert.assertEquals(res, "");
  }

  @Test
  public void actionResolveCrAfterToCloseCr_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setActionReturnCodeId("39");
    crDTO.setProcessTypeId("1");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    PowerMockito.mockStatic(StringUtils.class);
    crDTO.setCrId("1");
    crDTO.setChangeResponsibleUnit("1");
    CrProcessInsideDTO process = Mockito.spy(CrProcessInsideDTO.class);
    process.setCloseCrWhenResolveSuccess(1L);
    PowerMockito.when(crProcessRepository.findCrProcessById(any())).thenReturn(process);
    PowerMockito.when(crRepository.actionCloseCr(any(), anyString())).thenReturn(RESULT.ERROR);
    String res = crBusiness.actionResolveCrAfterToCloseCr(crDTO);
    Assert.assertNotNull(res);
  }

  @Test
  public void actionResolveCrAfterToCloseCr_03() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setActionReturnCodeId("39");
    crDTO.setProcessTypeId("1");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(any())).thenReturn(RESULT.SUCCESS);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(crRepository.actionCloseCr(any(), anyString())).thenReturn(RESULT.SUCCESS);
    crDTO.setCrId("1");
    crDTO.setChangeResponsibleUnit("1");
    CrProcessInsideDTO process = Mockito.spy(CrProcessInsideDTO.class);
    process.setCloseCrWhenResolveSuccess(1L);
    PowerMockito.when(crProcessRepository.findCrProcessById(any())).thenReturn(process);
    CrCreatedFromOtherSysDTO dto = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    dto.setStatus("OK");
    dto.setSystemId("4");
    dto.setObjectId("1");
    PowerMockito.when(crForOtherSystemBusiness.getCrCreatedFromOtherSysDTO(any())).thenReturn(dto);
    PowerMockito.when(crProcessFromClient.stringToLong(any())).thenReturn(1L);
    WoDTO wo = Mockito.spy(WoDTO.class);
    wo.setWoTypeId("1");
    wo.setWoCode("1");
    PowerMockito.when(woServiceProxy.findWoByIdWSProxy(any())).thenReturn(wo);
    PowerMockito.when(crProcessFromClient.stringToLong(any())).thenReturn(8L);
    PowerMockito.when(crForOtherSystemBusiness.checkWoCloseAutoSetting(any(), any()))
        .thenReturn(true);
    List<CrCreatedFromOtherSysDTO> crList = Mockito.spy(ArrayList.class);
    CrCreatedFromOtherSysDTO otherDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    otherDTO.setCrId("1");
    crList.add(otherDTO);
    PowerMockito.when(crForOtherSystemBusiness.getListDataByObjectId(any())).thenReturn(crList);
    List<CrDTO> crData = Mockito.spy(ArrayList.class);
    CrDTO crDTO1 = Mockito.spy(CrDTO.class);
    crData.add(crDTO1);
    PowerMockito.when(crRepository.getCrByIdAndResolveStatuṣ̣(any(), any())).thenReturn(crData);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    resultDTO.setMessage(RESULT.ERROR);
    PowerMockito.when(woServiceProxy.changeStatusWoProxy(any())).thenReturn(resultDTO);
    String res = crBusiness.actionResolveCrAfterToCloseCr(crDTO);
    Assert.assertEquals(res, RESULT.ERROR);
  }

  @Test
  public void checkCreateWOWhenCloserCr_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setProcessTypeLv3Id("1");
    crDTO.setProcessTypeId("1");
    CrProcessWoDTO crWO = Mockito.spy(CrProcessWoDTO.class);
    crWO.setCreateWoWhenCloseCR(1L);
    List<CrProcessWoDTO> lstCrWo = Mockito.spy(ArrayList.class);
    lstCrWo.add(crWO);
    PowerMockito.when(crProcessWoRepository.getLstWoFromProcessId(any())).thenReturn(lstCrWo);
    ResultInSideDto resultInSideDto = crBusiness.checkCreateWOWhenCloserCr(crDTO);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void testActionResolveCRGeneral_01() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrId("69");
    crDTO.setChangeResponsible("gggg");
    PowerMockito.when(crProcessFromClient.getFormForProcess(any())).thenReturn(crDTO);
    String msg = "ghi logs";
    PowerMockito.when(crProcessFromClient.validateActionClick(any())).thenReturn(msg);
    ResultInSideDto result = crBusiness.actionResolveCRGeneral(crDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testActionResolveCRGeneral_02() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    crDTO.setCrId("69");
    crDTO.setChangeResponsible("gggg");
    crDTO.setActionType("68");
    PowerMockito.when(crProcessFromClient.getFormForProcess(any())).thenReturn(crDTO);
    String msg = "";
    PowerMockito.when(crProcessFromClient.validateActionClick(any())).thenReturn(msg);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(CrGeneralUtil.class);
    String ret = "SUCCESS";
    Long crState = 68L;
    PowerMockito.when(crBusiness.actionResolveCr(crDTO, I18n.getLocale())).thenReturn(ret);
    PowerMockito.when(CrGeneralUtil.generateStateForResolveClient(anyLong())).thenReturn(crState);
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setActionType("yiyiyi");
    PowerMockito.when(crRepository.findCrById(anyLong(), any())).thenReturn(formRoot);
    ResultInSideDto result = crBusiness.actionResolveCRGeneral(crDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testActionResolveCRGeneral_03() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("cr.wo.must.close"))
        .thenReturn("Đồng chí bắt buộc phải đóng WO");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    crDTO.setCrId("69");
    crDTO.setChangeResponsible("gggg");
    crDTO.setActionType("68");
    PowerMockito.when(crProcessFromClient.getFormForProcess(any())).thenReturn(crDTO);
    String msg = "";
    PowerMockito.when(crProcessFromClient.validateActionClick(any())).thenReturn(msg);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(CrGeneralUtil.class);
    String ret = "MUSTCLOSEALLWO";
    Long crState = 68L;
    PowerMockito.when(crBusiness.actionResolveCr(crDTO, I18n.getLocale())).thenReturn(ret);
    PowerMockito.when(CrGeneralUtil.generateStateForResolveClient(anyLong())).thenReturn(crState);
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setActionType("yiyiyi");
    PowerMockito.when(crRepository.findCrById(anyLong(), any())).thenReturn(formRoot);
    ResultInSideDto result = crBusiness.actionResolveCRGeneral(crDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testActionResolveCRGeneral_04() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("cr.wo.must.close"))
        .thenReturn("Đồng chí bắt buộc phải đóng WO");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    crDTO.setCrId("69");
    crDTO.setChangeResponsible("gggg");
    crDTO.setActionType("68");
    PowerMockito.when(crProcessFromClient.getFormForProcess(any())).thenReturn(crDTO);
    String msg = "";
    PowerMockito.when(crProcessFromClient.validateActionClick(any())).thenReturn(msg);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(CrGeneralUtil.class);
    String ret = "CCCCCC";
    Long crState = 68L;
    PowerMockito.when(crBusiness.actionResolveCr(crDTO, I18n.getLocale())).thenReturn(ret);
    PowerMockito.when(CrGeneralUtil.generateStateForResolveClient(anyLong())).thenReturn(crState);
    CrInsiteDTO formRoot = Mockito.spy(CrInsiteDTO.class);
    formRoot.setActionType("yiyiyi");
    PowerMockito.when(crRepository.findCrById(anyLong(), any())).thenReturn(formRoot);
    ResultInSideDto result = crBusiness.actionResolveCRGeneral(crDTO);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void testActionResolveCrForService_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(StringUtils.class);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setActionType("cccc");
    crDTO.setActionType("xx");
    String ret = "SUCCESS";
    PowerMockito.when(crBusiness.actionResolveCr(crDTO, I18n.getLocale())).thenReturn(ret);
    String s = crBusiness.actionResolveCrForService(crDTO, "en_US");
    Assert.assertEquals(s, Constants.CR_RETURN_MESSAGE.SUCCESS);
  }

  @Test
  public void testActionCabForServer() {
    PowerMockito.mockStatic(StringUtils.class);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setActionType("cccc");
    crDTO.setActionType("456");
    String msg = "xxxxx";
    PowerMockito.when(crProcessFromClient.validateForService(any())).thenReturn(msg);
    String s = crBusiness.actionCabForServer(crDTO, "en_US");
    Assert.assertEquals(s, RESULT.ERROR);
  }

  @Test
  public void testImportCheckCr_01() {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.FILE_IS_NULL);
    MultipartFile multipartFile = null;
    PowerMockito.mockStatic(CommonImport.class);

    ResultInSideDto result = crBusiness.importCheckCr(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testImportCheckCr_02() throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.DATA_OVER);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    List<Object[]> lstHeader = new ArrayList<>();
    Object[] objects = new Object[10];
    objects[0] = "CR Number";
    lstHeader.add(objects);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 1, 1000)).thenReturn(lstHeader);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    Object[] objectsdata = new Object[10];
    objectsdata[0] = "Test test 123";
    for (int i = 1; i <= 1003; i++) {
      lstData.add(objectsdata);
    }
    lstData.size();
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        0, 1, 1000)).thenReturn(lstData);

    ResultInSideDto result = crBusiness.importCheckCr(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testImportCheckCr_03() throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    List<Object[]> lstHeader = new ArrayList<>();
    Object[] objects = new Object[10];
    objects[0] = "CR Number";
    lstHeader.add(objects);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File("/temp"), 0, 4,
        0, 1, 1000)).thenReturn(lstHeader);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    Object[] objectsdata = new Object[10];
    objectsdata[0] = "Test test 123";
    for (int i = 1; i <= 10; i++) {
      lstData.add(objectsdata);
    }
    lstData.size();
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File("/temp"), 0, 5,
        0, 1, 1000)).thenReturn(lstData);

    ResultInSideDto result = crBusiness.importCheckCr(multipartFile);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void testCreateObject_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrApprovalDepartmentInsiteDTO> lstAppDept = Mockito.spy(ArrayList.class);
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    List<CrAlarmInsiteDTO> lstAlarn = Mockito.spy(ArrayList.class);
    List<CrVendorDetailDTO> lstVendorDetail = Mockito.spy(ArrayList.class);
    List<CrModuleDetailDTO> lstModuleDetail = Mockito.spy(ArrayList.class);
    List<CrCableDTO> lstCable = Mockito.spy(ArrayList.class);
    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setSystemId("1");
    CrInsiteDTO tForm = Mockito.spy(CrInsiteDTO.class);
    tForm.setCreatedDate(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    tForm.setState("1");
    tForm.setRelateToPreApprovedCr("RelateToPreApprovedCr");
    tForm.setCrType("1");
    tForm.setLstAppDept(lstAppDept);
    tForm.setLstNetworkNodeId(lstNetworkNodeId);
    tForm.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    tForm.setLstAffectedService(lstAffectedService);
    tForm.setIsClickedToAlarmTag(1);
    tForm.setLstAlarn(lstAlarn);
    tForm.setCrId("1");
    tForm.setIsClickedToVendorTag(1);
    tForm.setLstVendorDetail(lstVendorDetail);
    tForm.setIsClickedToModuleTag(1);
    tForm.setLstModuleDetail(lstModuleDetail);
    tForm.setIsClickedToCableTag(1);
    tForm.setLstCable(lstCable);
    tForm.setActionNotes("ActionNotes");
    tForm.setCrCreatedFromOtherSysDTO(crCreatedFromOtherSysDTO);

    ResultInSideDto ret = Mockito.spy(ResultInSideDto.class);
    ret.setMessage("SUCCESS");
    ret.setId(1L);
    PowerMockito.when(
        crRepository.insertCr(any())
    ).thenReturn(ret);

    PowerMockito.when(
        crApprovalDepartmentRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crImpactedNodesRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedNodeRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionCreateMappingCRwithOtherSys(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateList(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateVendorDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateModuleDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crCableRepository
            .saveOrUpdateCableDetail(anyList(), anyLong(), any())
    ).thenReturn(true);

    ResultInSideDto actual = crBusiness.createObject(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testCreateObject_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrApprovalDepartmentInsiteDTO> lstAppDept = Mockito.spy(ArrayList.class);
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    List<CrAlarmInsiteDTO> lstAlarn = Mockito.spy(ArrayList.class);
    List<CrVendorDetailDTO> lstVendorDetail = Mockito.spy(ArrayList.class);
    List<CrModuleDetailDTO> lstModuleDetail = Mockito.spy(ArrayList.class);
    List<CrCableDTO> lstCable = Mockito.spy(ArrayList.class);
    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setSystemId("1");
    CrInsiteDTO tForm = Mockito.spy(CrInsiteDTO.class);
    tForm.setCreatedDate(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    tForm.setState("1");
    tForm.setCrType("2");
    tForm.setLstAppDept(lstAppDept);
    tForm.setLstNetworkNodeId(lstNetworkNodeId);
    tForm.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    tForm.setLstAffectedService(lstAffectedService);
    tForm.setIsClickedToAlarmTag(1);
    tForm.setLstAlarn(lstAlarn);
    tForm.setCrId("1");
    tForm.setIsClickedToVendorTag(1);
    tForm.setLstVendorDetail(lstVendorDetail);
    tForm.setIsClickedToModuleTag(1);
    tForm.setLstModuleDetail(lstModuleDetail);
    tForm.setIsClickedToCableTag(1);
    tForm.setLstCable(lstCable);
    tForm.setActionNotes("ActionNotes");
    tForm.setCrCreatedFromOtherSysDTO(crCreatedFromOtherSysDTO);

    ResultInSideDto ret = Mockito.spy(ResultInSideDto.class);
    ret.setMessage("SUCCESS");
    ret.setId(1L);
    PowerMockito.when(
        crRepository.insertCr(any())
    ).thenReturn(ret);

    PowerMockito.when(
        crApprovalDepartmentRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crImpactedNodesRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedNodeRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionCreateMappingCRwithOtherSys(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateList(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateVendorDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateModuleDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crCableRepository
            .saveOrUpdateCableDetail(anyList(), anyLong(), any())
    ).thenReturn(true);

    ResultDTO retAutoApprove = Mockito.spy(ResultDTO.class);
    retAutoApprove.setKey("APPROVECRINFIRSTPLACE");
    PowerMockito.when(
        crRepository
            .actionAutomaticApproveCrIncaseOfManagerCreateOrEditCR(any(), any())
    ).thenReturn(retAutoApprove);

    ResultInSideDto actual = crBusiness.createObject(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testCreateObject_03() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CrApprovalDepartmentInsiteDTO> lstAppDept = Mockito.spy(ArrayList.class);
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    List<CrAlarmInsiteDTO> lstAlarn = Mockito.spy(ArrayList.class);
    List<CrVendorDetailDTO> lstVendorDetail = Mockito.spy(ArrayList.class);
    List<CrModuleDetailDTO> lstModuleDetail = Mockito.spy(ArrayList.class);
    List<CrCableDTO> lstCable = Mockito.spy(ArrayList.class);
    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setSystemId("1");
    CrInsiteDTO tForm = Mockito.spy(CrInsiteDTO.class);
    tForm.setCreatedDate(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    tForm.setState("0");
    tForm.setCrType("1");
    tForm.setLstAppDept(lstAppDept);
    tForm.setLstNetworkNodeId(lstNetworkNodeId);
    tForm.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    tForm.setLstAffectedService(lstAffectedService);
    tForm.setIsClickedToAlarmTag(1);
    tForm.setLstAlarn(lstAlarn);
    tForm.setCrId("1");
    tForm.setIsClickedToVendorTag(1);
    tForm.setLstVendorDetail(lstVendorDetail);
    tForm.setIsClickedToModuleTag(1);
    tForm.setLstModuleDetail(lstModuleDetail);
    tForm.setIsClickedToCableTag(1);
    tForm.setLstCable(lstCable);
    tForm.setActionNotes("ActionNotes");
    tForm.setCrCreatedFromOtherSysDTO(crCreatedFromOtherSysDTO);

    ResultInSideDto ret = Mockito.spy(ResultInSideDto.class);
    ret.setMessage("SUCCESS");
    ret.setId(1L);
    PowerMockito.when(
        crRepository.insertCr(any())
    ).thenReturn(ret);

    PowerMockito.when(
        crApprovalDepartmentRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crImpactedNodesRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedNodeRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionCreateMappingCRwithOtherSys(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateList(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateVendorDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateModuleDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crCableRepository
            .saveOrUpdateCableDetail(anyList(), anyLong(), any())
    ).thenReturn(true);

    ResultDTO retAutoApprove = Mockito.spy(ResultDTO.class);
    retAutoApprove.setKey("APPROVECRINFIRSTPLACE");
    PowerMockito.when(
        crRepository
            .actionAutomaticApproveCrIncaseOfManagerCreateOrEditCR(any(), any())
    ).thenReturn(retAutoApprove);

    ResultInSideDto actual = crBusiness.createObject(tForm);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testAddNewCrClient_01() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(StringUtils.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(2L);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrNumber("123");
    crDTO.setActionType("50");
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateForm(any())
    ).thenReturn("success");

    ResultInSideDto actual = crBusiness.addNewCrClient(crDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testAddNewCrClient_02() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(2L);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    List<CrApprovalDepartmentInsiteDTO> lstAppDept = Mockito.spy(ArrayList.class);
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    List<CrAlarmInsiteDTO> lstAlarn = Mockito.spy(ArrayList.class);
    List<CrVendorDetailDTO> lstVendorDetail = Mockito.spy(ArrayList.class);
    List<CrModuleDetailDTO> lstModuleDetail = Mockito.spy(ArrayList.class);
    List<CrCableDTO> lstCable = Mockito.spy(ArrayList.class);

    AttachDtDTO attachDtDTO = Mockito.spy(AttachDtDTO.class);
    attachDtDTO.setDtCode("5555");
    attachDtDTO.setSystemCode("1");
    List<AttachDtDTO> attachDtDTOS = Mockito.spy(ArrayList.class);
    attachDtDTOS.add(attachDtDTO);

    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setSystemId("1");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrNumber("123");
    crDTO.setActionType("50");
    crDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setState("5");
    crDTO.setRelateToPreApprovedCr("RelateToPreApprovedCr");
    crDTO.setCrType("2");
    crDTO.setLstAppDept(lstAppDept);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setLstAffectedService(lstAffectedService);
    crDTO.setIsClickedToAlarmTag(1);
    crDTO.setLstAlarn(lstAlarn);
    crDTO.setCrId("1");
    crDTO.setIsClickedToVendorTag(1);
    crDTO.setLstVendorDetail(lstVendorDetail);
    crDTO.setIsClickedToModuleTag(1);
    crDTO.setLstModuleDetail(lstModuleDetail);
    crDTO.setIsClickedToCableTag(1);
    crDTO.setLstCable(lstCable);
    crDTO.setActionNotes("ActionNotes");
    crDTO.setCrCreatedFromOtherSysDTO(crCreatedFromOtherSysDTO);
    crDTO.setAttachDtDTO(attachDtDTOS);
    crDTO.setCrNumber("1111");
    crDTO.setProcessTypeId("1");
    crDTO.setEarliestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setLatestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setCrProcessId("1");
    crDTO.setDutyType("1");
    crDTO.setWaitingMopStatus("1");
    crDTO.setOtherSystemType("SR");
    crDTO.setOtherSystemId(1L);
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateForm(any())
    ).thenReturn("");
    PowerMockito.when(
        crRepository.findCrById(anyLong())
    ).thenReturn(crDTO);

    ResultInSideDto ret = Mockito.spy(ResultInSideDto.class);
    ret.setMessage("SUCCESS");
    ret.setId(1L);
    PowerMockito.when(
        crRepository.insertCr(any())
    ).thenReturn(ret);

    PowerMockito.when(
        crApprovalDepartmentRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crImpactedNodesRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedNodeRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionCreateMappingCRwithOtherSys(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateList(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateVendorDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateModuleDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crCableRepository
            .saveOrUpdateCableDetail(anyList(), anyLong(), any())
    ).thenReturn(true);

    WoDTO woDTO = Mockito.spy(WoDTO.class);
    List<WoDTO> lstSubWo = Mockito.spy(ArrayList.class);
    lstSubWo.add(woDTO);
    PowerMockito.when(
        crProcessFromClient
            .getListSubWo(anyString(), any(), any(), anyList())
    ).thenReturn(lstSubWo);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitToken);

    List<GnocFileDto> gnocFileSrDtos = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        gnocFileRepository.getListGnocFileByDto(any())
    ).thenReturn(gnocFileSrDtos);

    ResultInSideDto actual = crBusiness.addNewCrClient(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testAddNewCrClient_03() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(2L);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    List<CrApprovalDepartmentInsiteDTO> lstAppDept = Mockito.spy(ArrayList.class);
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    List<CrAlarmInsiteDTO> lstAlarn = Mockito.spy(ArrayList.class);
    List<CrVendorDetailDTO> lstVendorDetail = Mockito.spy(ArrayList.class);
    List<CrModuleDetailDTO> lstModuleDetail = Mockito.spy(ArrayList.class);
    List<CrCableDTO> lstCable = Mockito.spy(ArrayList.class);

    AttachDtDTO attachDtDTO = Mockito.spy(AttachDtDTO.class);
    attachDtDTO.setDtCode("5555");
    attachDtDTO.setSystemCode("2");
    List<AttachDtDTO> attachDtDTOS = Mockito.spy(ArrayList.class);
    attachDtDTOS.add(attachDtDTO);

    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setSystemId("1");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrNumber("123");
    crDTO.setActionType("50");
    crDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setState("5");
    crDTO.setRelateToPreApprovedCr("RelateToPreApprovedCr");
    crDTO.setCrType("2");
    crDTO.setLstAppDept(lstAppDept);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setLstAffectedService(lstAffectedService);
    crDTO.setIsClickedToAlarmTag(1);
    crDTO.setLstAlarn(lstAlarn);
    crDTO.setCrId("1");
    crDTO.setIsClickedToVendorTag(1);
    crDTO.setLstVendorDetail(lstVendorDetail);
    crDTO.setIsClickedToModuleTag(1);
    crDTO.setLstModuleDetail(lstModuleDetail);
    crDTO.setIsClickedToCableTag(1);
    crDTO.setLstCable(lstCable);
    crDTO.setActionNotes("ActionNotes");
    crDTO.setCrCreatedFromOtherSysDTO(crCreatedFromOtherSysDTO);
    crDTO.setAttachDtDTO(attachDtDTOS);
    crDTO.setCrNumber("1111");
    crDTO.setProcessTypeId("1");
    crDTO.setEarliestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setLatestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setCrProcessId("1");
    crDTO.setDutyType("1");
    crDTO.setWaitingMopStatus("1");
    crDTO.setOtherSystemType("SR");
    crDTO.setOtherSystemId(1L);
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateForm(any())
    ).thenReturn("");
    PowerMockito.when(
        crRepository.findCrById(anyLong())
    ).thenReturn(crDTO);

    ResultInSideDto ret = Mockito.spy(ResultInSideDto.class);
    ret.setMessage("SUCCESS");
    ret.setId(1L);
    PowerMockito.when(
        crRepository.insertCr(any())
    ).thenReturn(ret);

    PowerMockito.when(
        crApprovalDepartmentRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crImpactedNodesRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedNodeRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionCreateMappingCRwithOtherSys(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateList(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateVendorDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateModuleDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crCableRepository
            .saveOrUpdateCableDetail(anyList(), anyLong(), any())
    ).thenReturn(true);

    WoDTO woDTO = Mockito.spy(WoDTO.class);
    List<WoDTO> lstSubWo = Mockito.spy(ArrayList.class);
    lstSubWo.add(woDTO);
    PowerMockito.when(
        crProcessFromClient
            .getListSubWo(anyString(), any(), any(), anyList())
    ).thenReturn(lstSubWo);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitToken);

    List<GnocFileDto> gnocFileSrDtos = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        gnocFileRepository.getListGnocFileByDto(any())
    ).thenReturn(gnocFileSrDtos);

    ResultInSideDto actual = crBusiness.addNewCrClient(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testAddNewCrClient_04() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(2L);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    List<CrApprovalDepartmentInsiteDTO> lstAppDept = Mockito.spy(ArrayList.class);
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    List<CrAlarmInsiteDTO> lstAlarn = Mockito.spy(ArrayList.class);
    List<CrVendorDetailDTO> lstVendorDetail = Mockito.spy(ArrayList.class);
    List<CrModuleDetailDTO> lstModuleDetail = Mockito.spy(ArrayList.class);
    List<CrCableDTO> lstCable = Mockito.spy(ArrayList.class);

    AttachDtDTO attachDtDTO = Mockito.spy(AttachDtDTO.class);
    attachDtDTO.setDtCode("5555");
    attachDtDTO.setSystemCode("3");
    List<AttachDtDTO> attachDtDTOS = Mockito.spy(ArrayList.class);
    attachDtDTOS.add(attachDtDTO);

    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setSystemId("1");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrNumber("123");
    crDTO.setActionType("50");
    crDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setState("5");
    crDTO.setRelateToPreApprovedCr("RelateToPreApprovedCr");
    crDTO.setCrType("2");
    crDTO.setLstAppDept(lstAppDept);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setLstAffectedService(lstAffectedService);
    crDTO.setIsClickedToAlarmTag(1);
    crDTO.setLstAlarn(lstAlarn);
    crDTO.setCrId("1");
    crDTO.setIsClickedToVendorTag(1);
    crDTO.setLstVendorDetail(lstVendorDetail);
    crDTO.setIsClickedToModuleTag(1);
    crDTO.setLstModuleDetail(lstModuleDetail);
    crDTO.setIsClickedToCableTag(1);
    crDTO.setLstCable(lstCable);
    crDTO.setActionNotes("ActionNotes");
    crDTO.setCrCreatedFromOtherSysDTO(crCreatedFromOtherSysDTO);
    crDTO.setAttachDtDTO(attachDtDTOS);
    crDTO.setCrNumber("1111");
    crDTO.setProcessTypeId("1");
    crDTO.setEarliestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setLatestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setCrProcessId("1");
    crDTO.setDutyType("1");
    crDTO.setWaitingMopStatus("1");
    crDTO.setOtherSystemType("SR");
    crDTO.setOtherSystemId(1L);
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateForm(any())
    ).thenReturn("");
    PowerMockito.when(
        crRepository.findCrById(anyLong())
    ).thenReturn(crDTO);

    ResultInSideDto ret = Mockito.spy(ResultInSideDto.class);
    ret.setMessage("SUCCESS");
    ret.setId(1L);
    PowerMockito.when(
        crRepository.insertCr(any())
    ).thenReturn(ret);

    PowerMockito.when(
        crApprovalDepartmentRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crImpactedNodesRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedNodeRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionCreateMappingCRwithOtherSys(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateList(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateVendorDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateModuleDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crCableRepository
            .saveOrUpdateCableDetail(anyList(), anyLong(), any())
    ).thenReturn(true);

    WoDTO woDTO = Mockito.spy(WoDTO.class);
    List<WoDTO> lstSubWo = Mockito.spy(ArrayList.class);
    lstSubWo.add(woDTO);
    PowerMockito.when(
        crProcessFromClient
            .getListSubWo(anyString(), any(), any(), anyList())
    ).thenReturn(lstSubWo);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitToken);

    List<GnocFileDto> gnocFileSrDtos = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        gnocFileRepository.getListGnocFileByDto(any())
    ).thenReturn(gnocFileSrDtos);

    ResultInSideDto actual = crBusiness.addNewCrClient(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testOnUpdateCrClient_01() {
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(2L);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    List<CrApprovalDepartmentInsiteDTO> lstAppDept = Mockito.spy(ArrayList.class);
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    List<CrAlarmInsiteDTO> lstAlarn = Mockito.spy(ArrayList.class);
    List<CrVendorDetailDTO> lstVendorDetail = Mockito.spy(ArrayList.class);
    List<CrModuleDetailDTO> lstModuleDetail = Mockito.spy(ArrayList.class);
    List<CrCableDTO> lstCable = Mockito.spy(ArrayList.class);

    AttachDtDTO attachDtDTO = Mockito.spy(AttachDtDTO.class);
    attachDtDTO.setDtCode("5555");
    attachDtDTO.setSystemCode("1");
    List<AttachDtDTO> attachDtDTOS = Mockito.spy(ArrayList.class);
    attachDtDTOS.add(attachDtDTO);

    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setSystemId("1");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrNumber("123");
    crDTO.setActionType("50");
    crDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setState("5");
    crDTO.setRelateToPreApprovedCr("RelateToPreApprovedCr");
    crDTO.setCrType("2");
    crDTO.setLstAppDept(lstAppDept);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setLstAffectedService(lstAffectedService);
    crDTO.setIsClickedToAlarmTag(1);
    crDTO.setLstAlarn(lstAlarn);
    crDTO.setCrId("1");
    crDTO.setIsClickedToVendorTag(1);
    crDTO.setLstVendorDetail(lstVendorDetail);
    crDTO.setIsClickedToModuleTag(1);
    crDTO.setLstModuleDetail(lstModuleDetail);
    crDTO.setIsClickedToCableTag(1);
    crDTO.setLstCable(lstCable);
    crDTO.setActionNotes("ActionNotes");
    crDTO.setCrCreatedFromOtherSysDTO(crCreatedFromOtherSysDTO);
    crDTO.setAttachDtDTO(attachDtDTOS);
    crDTO.setCrNumber("1111");
    crDTO.setProcessTypeId("1");
    crDTO.setEarliestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setLatestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setCrProcessId("1");
    crDTO.setDutyType("1");
    crDTO.setWaitingMopStatus("1");
    crDTO.setOtherSystemType("SR");
    crDTO.setOtherSystemId(1L);
    crDTO.setIsLoadMop("1");
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateForm(any())
    ).thenReturn("");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(crDTO);

    CrInsiteDTO form = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(form);

    PowerMockito.when(crRepository.updateCr(any())).thenReturn("SUCCESS");
    PowerMockito.when(
        crApprovalDepartmentRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crImpactedNodesRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionCreateMappingCRwithOtherSys(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionResetApproveCRIncaseOfEditCR(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateList(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateVendorDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateModuleDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crCableRepository
            .saveOrUpdateCableDetail(anyList(), anyLong(), any())
    ).thenReturn(true);

    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1111");
    crFilesAttachInsiteDTO.setDtFileHistory("VIPA_DD");
    List<CrFilesAttachInsiteDTO> lstAttachment = Mockito.spy(ArrayList.class);
    lstAttachment.add(crFilesAttachInsiteDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesAttachDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstAttachment);

    WoDTO woDTO = Mockito.spy(WoDTO.class);
    List<WoDTO> lstSubWo = Mockito.spy(ArrayList.class);
    lstSubWo.add(woDTO);
    PowerMockito.when(
        crProcessFromClient
            .getListSubWo(anyString(), any(), any(), anyList())
    ).thenReturn(lstSubWo);

    ResultInSideDto actual = crBusiness.onUpdateCrClient(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testOnUpdateCrClient_02() {
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(2L);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    List<CrApprovalDepartmentInsiteDTO> lstAppDept = Mockito.spy(ArrayList.class);
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    List<CrAlarmInsiteDTO> lstAlarn = Mockito.spy(ArrayList.class);
    List<CrVendorDetailDTO> lstVendorDetail = Mockito.spy(ArrayList.class);
    List<CrModuleDetailDTO> lstModuleDetail = Mockito.spy(ArrayList.class);
    List<CrCableDTO> lstCable = Mockito.spy(ArrayList.class);

    AttachDtDTO attachDtDTO = Mockito.spy(AttachDtDTO.class);
    attachDtDTO.setDtCode("5555");
    attachDtDTO.setSystemCode("1");
    List<AttachDtDTO> attachDtDTOS = Mockito.spy(ArrayList.class);
    attachDtDTOS.add(attachDtDTO);

    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setSystemId("1");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrNumber("123");
    crDTO.setActionType("50");
    crDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setState("5");
    crDTO.setRelateToPreApprovedCr("121212");
    crDTO.setCrType("2");
    crDTO.setLstAppDept(lstAppDept);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setLstAffectedService(lstAffectedService);
    crDTO.setIsClickedToAlarmTag(1);
    crDTO.setLstAlarn(lstAlarn);
    crDTO.setCrId("1");
    crDTO.setIsClickedToVendorTag(1);
    crDTO.setLstVendorDetail(lstVendorDetail);
    crDTO.setIsClickedToModuleTag(1);
    crDTO.setLstModuleDetail(lstModuleDetail);
    crDTO.setIsClickedToCableTag(1);
    crDTO.setLstCable(lstCable);
    crDTO.setActionNotes("ActionNotes");
    crDTO.setCrCreatedFromOtherSysDTO(crCreatedFromOtherSysDTO);
    crDTO.setAttachDtDTO(attachDtDTOS);
    crDTO.setCrNumber("1111");
    crDTO.setProcessTypeId("1");
    crDTO.setEarliestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setLatestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setCrProcessId("1");
    crDTO.setDutyType("1");
    crDTO.setWaitingMopStatus("1");
    crDTO.setOtherSystemType("SR");
    crDTO.setOtherSystemId(1L);
    crDTO.setIsLoadMop("2");
    crDTO.setCrId("1");
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateForm(any())
    ).thenReturn("");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(crDTO);

    CrInsiteDTO form = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(form);

    PowerMockito.when(crRepository.updateCr(any())).thenReturn("SUCCESS");
    PowerMockito.when(
        crApprovalDepartmentRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crImpactedNodesRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionCreateMappingCRwithOtherSys(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionResetApproveCRIncaseOfEditCR(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateList(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateVendorDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateModuleDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crCableRepository
            .saveOrUpdateCableDetail(anyList(), anyLong(), any())
    ).thenReturn(true);

    ResultDTO retAutoApprove = Mockito.spy(ResultDTO.class);
    retAutoApprove.setKey("APPROVECRINFIRSTPLACE");
    PowerMockito.when(
        crRepository
            .actionAutomaticApproveCrIncaseOfManagerCreateOrEditCR(any(), anyString())
    ).thenReturn(retAutoApprove);

    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1111");
    crFilesAttachInsiteDTO.setDtFileHistory("VIPA_DD");
    List<CrFilesAttachInsiteDTO> lstAttachment = Mockito.spy(ArrayList.class);
    lstAttachment.add(crFilesAttachInsiteDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesAttachDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstAttachment);

    WoDTO woDTO = Mockito.spy(WoDTO.class);
    List<WoDTO> lstSubWo = Mockito.spy(ArrayList.class);
    lstSubWo.add(woDTO);
    PowerMockito.when(
        crProcessFromClient
            .getListSubWo(anyString(), any(), any(), anyList())
    ).thenReturn(lstSubWo);

    ResultInSideDto actual = crBusiness.onUpdateCrClient(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testOnUpdateCrClient_03() {
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    userToken.setDeptId(2L);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    List<CrApprovalDepartmentInsiteDTO> lstAppDept = Mockito.spy(ArrayList.class);
    List<CrImpactedNodesDTO> lstNetworkNodeId = Mockito.spy(ArrayList.class);
    List<CrAffectedNodesDTO> lstNetworkNodeIdAffected = Mockito.spy(ArrayList.class);
    List<CrAffectedServiceDetailsDTO> lstAffectedService = Mockito.spy(ArrayList.class);
    List<CrAlarmInsiteDTO> lstAlarn = Mockito.spy(ArrayList.class);
    List<CrVendorDetailDTO> lstVendorDetail = Mockito.spy(ArrayList.class);
    List<CrModuleDetailDTO> lstModuleDetail = Mockito.spy(ArrayList.class);
    List<CrCableDTO> lstCable = Mockito.spy(ArrayList.class);

    AttachDtDTO attachDtDTO = Mockito.spy(AttachDtDTO.class);
    attachDtDTO.setDtCode("5555");
    attachDtDTO.setSystemCode("1");
    List<AttachDtDTO> attachDtDTOS = Mockito.spy(ArrayList.class);
    attachDtDTOS.add(attachDtDTO);

    CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = Mockito.spy(CrCreatedFromOtherSysDTO.class);
    crCreatedFromOtherSysDTO.setSystemId("1");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCrNumber("123");
    crDTO.setActionType("50");
    crDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setState("5");
    crDTO.setRelateToPreApprovedCr("RelateToPreApprovedCr");
    crDTO.setCrType("2");
    crDTO.setLstAppDept(lstAppDept);
    crDTO.setLstNetworkNodeId(lstNetworkNodeId);
    crDTO.setLstNetworkNodeIdAffected(lstNetworkNodeIdAffected);
    crDTO.setLstAffectedService(lstAffectedService);
    crDTO.setIsClickedToAlarmTag(1);
    crDTO.setLstAlarn(lstAlarn);
    crDTO.setCrId("1");
    crDTO.setIsClickedToVendorTag(1);
    crDTO.setLstVendorDetail(lstVendorDetail);
    crDTO.setIsClickedToModuleTag(1);
    crDTO.setLstModuleDetail(lstModuleDetail);
    crDTO.setIsClickedToCableTag(1);
    crDTO.setLstCable(lstCable);
    crDTO.setActionNotes("ActionNotes");
    crDTO.setCrCreatedFromOtherSysDTO(crCreatedFromOtherSysDTO);
    crDTO.setAttachDtDTO(attachDtDTOS);
    crDTO.setCrNumber("1111");
    crDTO.setProcessTypeId("1");
    crDTO.setEarliestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setLatestStartTime(
        DateTimeUtils.convertStringToDate("12/05/2020 10:00:00")
    );
    crDTO.setCrProcessId("1");
    crDTO.setDutyType("1");
    crDTO.setWaitingMopStatus("1");
    crDTO.setOtherSystemType("SR");
    crDTO.setOtherSystemId(1L);
    crDTO.setIsLoadMop("3");
    crDTO.setChangeOrginatorName("1111(111");
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateForm(any())
    ).thenReturn("");
    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(crDTO);

    CrInsiteDTO form = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(form);

    PowerMockito.when(crRepository.updateCr(any())).thenReturn("SUCCESS");
    PowerMockito.when(
        crApprovalDepartmentRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crImpactedNodesRepository
            .saveListDTONoIdSession(anyList(), any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .saveListDTONoIdSession(anyList())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionCreateMappingCRwithOtherSys(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crRepository.actionResetApproveCRIncaseOfEditCR(any())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateList(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateVendorDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateModuleDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crCableRepository
            .saveOrUpdateCableDetail(anyList(), anyLong(), any())
    ).thenReturn(true);

    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("1111");
    crFilesAttachInsiteDTO.setDtFileHistory("TEST");
    List<CrFilesAttachInsiteDTO> lstAttachment = Mockito.spy(ArrayList.class);
    lstAttachment.add(crFilesAttachInsiteDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesAttachDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstAttachment);

    WoDTO woDTO = Mockito.spy(WoDTO.class);
    List<WoDTO> lstSubWo = Mockito.spy(ArrayList.class);
    lstSubWo.add(woDTO);
    PowerMockito.when(
        crProcessFromClient
            .getListSubWo(anyString(), any(), any(), anyList())
    ).thenReturn(lstSubWo);

    ResultInSideDto actual = crBusiness.onUpdateCrClient(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testActionApproveCRGeneral_01() {
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("success");

    ResultInSideDto actual = crBusiness.actionApproveCRGeneral(crDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testActionApproveCRGeneral_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    CrApprovalDepartmentInsiteDTO crApp = Mockito.spy(CrApprovalDepartmentInsiteDTO.class);
    List<CrApprovalDepartmentInsiteDTO> lstAp = Mockito.spy(ArrayList.class);
    lstAp.add(crApp);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setLstAppDept(lstAp);
    crDTO.setActionType("11111");
    crDTO.setCrId("1");
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("");

    PowerMockito.when(
        crRepository.actionApproveCR(any(), anyString())
    ).thenReturn("SUCCESS");

    ResultInSideDto actual = crBusiness.actionApproveCRGeneral(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testActionApproveCRGeneral_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("UNIT-TEST");

    CrApprovalDepartmentInsiteDTO crApp = Mockito.spy(CrApprovalDepartmentInsiteDTO.class);
    CrApprovalDepartmentInsiteDTO crApp1 = Mockito.spy(CrApprovalDepartmentInsiteDTO.class);
    crApp1.setStatus("1");
    List<CrApprovalDepartmentInsiteDTO> lstAp = Mockito.spy(ArrayList.class);
    lstAp.add(crApp);
    lstAp.add(crApp1);
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setLstAppDept(lstAp);
    crDTO.setActionType("11111");
    crDTO.setCrId("1");
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("");

    PowerMockito.when(
        crRepository.actionApproveCR(any(), anyString())
    ).thenReturn("SUCCESS");

    ResultInSideDto actual = crBusiness.actionApproveCRGeneral(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testActionAppraisCRGeneral_01() {
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("SUCCESS");

    ResultInSideDto actual = crBusiness.actionAppraisCRGeneral(crDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testActionAppraisCRGeneral_02() {
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(TicketProvider.class);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("12/05/2020 15:00:00")
    );
    crDTO.setActionType("11");
    crDTO.setCrId("1");
    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("");
    PowerMockito.when(
        crRepository.actionAppraiseCr(any(), anyString())
    ).thenReturn("SUCCESS");

    ResultInSideDto actual = crBusiness.actionAppraisCRGeneral(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testActionAppraiseCr() {
    PowerMockito.mockStatic(StringUtils.class);

    List<CrAlarmInsiteDTO> lstAlarn = Mockito.spy(ArrayList.class);
    List<CrVendorDetailDTO> lstVendorDetail = Mockito.spy(ArrayList.class);
    List<CrModuleDetailDTO> lstModuleDetail = Mockito.spy(ArrayList.class);
    List<CrCableDTO> lstCable = Mockito.spy(ArrayList.class);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("13/05/2020 08:00:00")
    );
    crDTO.setActionType("14");
    crDTO.setCrId("1");
    crDTO.setIsClickedToAlarmTag(1);
    crDTO.setLstAlarn(lstAlarn);
    crDTO.setIsClickedToVendorTag(1);
    crDTO.setLstVendorDetail(lstVendorDetail);
    crDTO.setIsClickedToModuleTag(1);
    crDTO.setLstModuleDetail(lstModuleDetail);
    crDTO.setIsClickedToCableTag(1);
    crDTO.setLstCable(lstCable);

    PowerMockito.when(
        crRepository.actionAppraiseCr(any(), anyString())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateList(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateVendorDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateModuleDetail(anyList(), anyLong(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crCableRepository
            .saveOrUpdateCableDetail(anyList(), anyLong(), any())
    ).thenReturn(true);

    String actual = crBusiness.actionAppraiseCr(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionAppraiseCrForServer() {
    PowerMockito.mockStatic(StringUtils.class);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);

    PowerMockito.when(
        crProcessFromClient.validateForService(any())
    ).thenReturn("SUCCESS");

    String actual = crBusiness.actionAppraiseCrForServer(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionVerifyCRGeneral_01() {
    CrInsiteDTO crDTOSave = Mockito.spy(CrInsiteDTO.class);

    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTOSave);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("SUCCESS");
    ResultInSideDto actual = crBusiness.actionVerifyCRGeneral(crDTOSave);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testActionVerifyCRGeneral_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CrInsiteDTO xxx = Mockito.spy(CrInsiteDTO.class);

    CrInsiteDTO crDTOSave = Mockito.spy(CrInsiteDTO.class);
    crDTOSave.setActionType("39");
    crDTOSave.setCrType("0");
    crDTOSave.setRisk("3");
    crDTOSave.setListWoId("1,2,3,4");
    crDTOSave.setCrId("1");
    crDTOSave.setProcessTypeId("2");
    crDTOSave.setCreatedDate(
        DateTimeUtils.convertStringToDate("13/05/2020 09:00:00")
    );

    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTOSave);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("");
    PowerMockito.when(
        crRepository.actionAssignCab(any(), anyString())
    ).thenReturn("SUCCESS");

    WoDTO woDTO = Mockito.spy(WoDTO.class);
    List<WoDTO> lstSubWo = Mockito.spy(ArrayList.class);
    lstSubWo.add(woDTO);

    PowerMockito.when(
        crProcessFromClient
            .getListSubWo(anyString(), any(), any(), any())
    ).thenReturn(lstSubWo);

    ResultInSideDto actual = crBusiness.actionVerifyCRGeneral(crDTOSave);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testActionScheduleCrForServer_01() {
    PowerMockito.mockStatic(StringUtils.class);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);

    PowerMockito.when(
        crProcessFromClient.validateForService(any())
    ).thenReturn("SUCCESS");

    String actual = crBusiness.actionScheduleCrForServer(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionScheduleCrForServer_02() {
    PowerMockito.mockStatic(StringUtils.class);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setSubDTCode("11");
    crDTO.setAutoExecute("1");

    PowerMockito.when(
        crProcessFromClient.validateForService(any())
    ).thenReturn("");
    PowerMockito.when(
        crProcessFromClient
            .checkCrAuto(any(), anyString(), anyBoolean())
    ).thenReturn("SUCCESS");

    String actual = crBusiness.actionScheduleCrForServer(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionVerifyForServer_01() {
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setRisk("5");
    crDTO.setImpactSegment("122");
    crDTO.setActionType("45");

    String actual = crBusiness.actionVerifyForServer(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionVerifyForServer_02() {
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setRisk("4");
    crDTO.setImpactSegment("121");
    crDTO.setActionType("38");

    String actual = crBusiness.actionVerifyForServer(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionVerifyForServer_03() {
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setRisk("4");
    crDTO.setImpactSegment("121");
    crDTO.setActionType("39");
    crDTO.setCrType("0");

    String actual = crBusiness.actionVerifyForServer(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionVerifyForServer_04() {
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setRisk("4");
    crDTO.setImpactSegment("121");
    crDTO.setActionType("60");
    crDTO.setCrType("0");

    PowerMockito.when(
        crProcessFromClient.validateForService(any())
    ).thenReturn("SUCCESS");

    String actual = crBusiness.actionVerifyForServer(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionVerifyForServer_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setRisk("5");
    crDTO.setImpactSegment("122");
    crDTO.setActionType("39");
    crDTO.setCrType("0");

    PowerMockito.when(
        crProcessFromClient.validateForService(any())
    ).thenReturn("");
    PowerMockito.when(
        crRepository.actionAssignCab(any(), anyString())
    ).thenReturn("SUCCESS");

    String actual = crBusiness.actionVerifyForServer(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionVerifyForServer_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setRisk("5");
    crDTO.setImpactSegment("122");
    crDTO.setActionType("60");
    crDTO.setCrType("0");

    PowerMockito.when(
        crProcessFromClient.validateForService(any())
    ).thenReturn("");
    PowerMockito.when(
        crRepository.actionVerify(any(), anyString())
    ).thenReturn("SUCCESS");

    String actual = crBusiness.actionVerifyForServer(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.SUCCESS, actual);
  }

  @Test
  public void testActionScheduleCRGeneral_01() {
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);

    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("SUCCESS");

    ResultInSideDto actual = crBusiness.actionScheduleCRGeneral(crDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testActionScheduleCRGeneral_02() {
    PowerMockito.mockStatic(StringUtils.class);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setSubDTCode("11");
    crDTO.setAutoExecute("1");

    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("");

    ResultInSideDto actual = crBusiness.actionScheduleCRGeneral(crDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testActionScheduleCRGeneral_03() {
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");

    List<CrAlarmInsiteDTO> lstAlarn = Mockito.spy(ArrayList.class);
    List<CrVendorDetailDTO> lstVendorDetail = Mockito.spy(ArrayList.class);
    List<CrModuleDetailDTO> lstModuleDetail = Mockito.spy(ArrayList.class);
    List<CrCableDTO> lstCable = Mockito.spy(ArrayList.class);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setSubDTCode("11");
    crDTO.setAutoExecute("1");
    crDTO.setServiceAffecting("1");
    crDTO.setCrId("11");
    crDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("13/05/2020 11:00:00")
    );
    crDTO.setIsClickedToAlarmTag(1);
    crDTO.setLstAlarn(lstAlarn);
    crDTO.setIsClickedToVendorTag(1);
    crDTO.setLstVendorDetail(lstVendorDetail);
    crDTO.setIsClickedToModuleTag(1);
    crDTO.setLstModuleDetail(lstModuleDetail);
    crDTO.setIsClickedToCableTag(1);
    crDTO.setLstCable(lstCable);
    crDTO.setActionType("22");
    crDTO.setCrType("1");

    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("");
    PowerMockito.when(
        crProcessFromClient
            .checkCrAuto(any(), anyString(), anyBoolean())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        StringUtils.isStringNullOrEmpty(anyString())
    ).thenReturn(true);
    PowerMockito.when(
        crRepository.actionScheduleCr(any(), anyString())
    ).thenReturn("SUCCESS");
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateList(anyList(), any(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateVendorDetail(anyList(), any(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crAlarmRepository
            .saveOrUpdateModuleDetail(anyList(), any(), any())
    ).thenReturn(true);
    PowerMockito.when(
        crCableRepository
            .saveOrUpdateCableDetail(anyList(), any(), any())
    ).thenReturn(true);

    ResultInSideDto actual = crBusiness.actionScheduleCRGeneral(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testActionReceiveCRGeneral_01() {
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);

    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("SUCCESS");

    ResultInSideDto actual = crBusiness.actionReceiveCRGeneral(crDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testActionReceiveCRGeneral_02() {
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setActionType("50");
    crDTO.setListWoId("1,2,3,4,5,6");
    crDTO.setCrId("1");
    crDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("13/05/11:00:00")
    );
    crDTO.setProcessTypeId("1111");

    PowerMockito.when(
        crProcessFromClient.getFormForProcess(any())
    ).thenReturn(crDTO);
    PowerMockito.when(
        crProcessFromClient.validateActionClick(any())
    ).thenReturn("");
    PowerMockito.when(
        crRepository.actionReceiveCr(any(), anyString())
    ).thenReturn("SUCCESS");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("username");
    PowerMockito.when(
        TicketProvider.getUserToken()
    ).thenReturn(userToken);

    PowerMockito.when(
        StringUtils.isNotNullOrEmpty(anyString())
    ).thenReturn(true);

    WoDTO woDTO = Mockito.spy(WoDTO.class);
    List<WoDTO> lstSubWo = Mockito.spy(ArrayList.class);
    lstSubWo.add(woDTO);
    PowerMockito.when(
        crProcessFromClient
            .getListSubWo(anyString(), any(), any(), anyList())
    ).thenReturn(lstSubWo);

    ResultInSideDto actual = crBusiness.actionReceiveCRGeneral(crDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testSearchParentCr_01() {
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    Datatable actual = crBusiness.searchParentCr("7", "1", 1, 1);
    Assert.assertNull(actual.getData());
  }

  @Test
  public void testSearchParentCr_03() {
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    Datatable actual = crBusiness.searchParentCr("5", "1", 1, 1);
    Assert.assertNull(actual.getData());
  }

  @Test
  public void testSearchParentCr_04() {
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    Datatable actual = crBusiness.searchParentCr("4", "1", 1, 1);
    Assert.assertNull(actual.getData());
  }

  @Test
  public void testSearchParentCr_05() {
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    Datatable actual = crBusiness.searchParentCr("3", "1", 1, 1);
    Assert.assertNull(actual);
  }

  @Test
  public void testSearchParentCr_06() {
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    Datatable actual = crBusiness.searchParentCr("2", "1", 1, 1);
    Assert.assertNull(actual);
  }

  @Test
  public void testSearchParentCr_07() throws Exception{
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);
    Map<String, String> mapConfig = Mockito.spy(HashMap.class);
    mapConfig.put("url_rdm", "https://rdm.viettel.com");
    PowerMockito.when(commonRepository.getConfigProperty()).thenReturn(mapConfig);
    URL mockedURL = PowerMockito.mock(URL.class);
    PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(mockedURL);
    URLConnection urlConnection = PowerMockito.mock(HttpURLConnection.class);
    PowerMockito.when(mockedURL.openConnection()).thenReturn(urlConnection);
    DataOutputStream dataOutputStream = PowerMockito.mock(DataOutputStream.class);
    PowerMockito.whenNew(DataOutputStream.class).withAnyArguments().thenReturn(dataOutputStream);
    PowerMockito.doNothing().when(dataOutputStream).writeBytes(anyString());
    PowerMockito.doNothing().when(dataOutputStream).flush();
    PowerMockito.doNothing().when(dataOutputStream).close();
    HttpURLConnection conn = (HttpURLConnection) urlConnection;
    PowerMockito.when(conn.getResponseCode()).thenReturn(200);
    PowerMockito.doNothing().when(conn).disconnect();
    BufferedReader bufferedReader = PowerMockito.mock(BufferedReader.class);
    PowerMockito.whenNew(BufferedReader.class).withAnyArguments().thenReturn(bufferedReader);
    InputStreamReader inputStreamReader = PowerMockito.mock(InputStreamReader.class);
    PowerMockito.whenNew(InputStreamReader.class).withAnyArguments().thenReturn(inputStreamReader);
    ObjectMapper objectMapper = PowerMockito.mock(ObjectMapper.class);
    PowerMockito.whenNew(ObjectMapper.class).withAnyArguments().thenReturn(objectMapper);
    PowerMockito.when(objectMapper.readValue("", Map.class)).thenReturn(null);
    Datatable actual = crBusiness.searchParentCr("6", "1", 1, 1);
    Assert.assertNull(actual.getData());
  }

  @Test
  public void testGetListDataSearch() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(DateUtil.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.when(I18n.getString(anyString())).thenReturn("UNIT-TEST");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(1L);
    PowerMockito.when(TicketProvider.getUserToken()).thenReturn(userToken);

    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    woDTOSearch.setStatus("5555");
    woDTOSearch.setPriorityId("1");
    woDTOSearch.setCreateDate(("13/05/2020 12:00:00"));
    woDTOSearch.setStartTime("13/05/2020 12:00:00");
    woDTOSearch.setEndTime("13/05/2020 12:00:00");
    woDTOSearch.setLastUpdateTime("13/05/2020 12:00:00");
    woDTOSearch.setFinishTime("13/05/2020 12:00:00");

    WoSearchDTO woSearchDTO = Mockito.spy(WoSearchDTO.class);
    woSearchDTO.setSortType("111");
    woSearchDTO.setSortName("222");
    woSearchDTO.setPage(1);
    woSearchDTO.setPageSize(1);
    woSearchDTO.setWoDTOSearch(woDTOSearch);

    List<WoDTOSearch> lst = Mockito.spy(ArrayList.class);
    lst.add(woDTOSearch);
    lst.add(woDTOSearch);
    PowerMockito.when(
        woServiceProxy.getListDataSearchProxy(any())
    ).thenReturn(lst);

    WoPriorityDTO woPriorityDTO = Mockito.spy(WoPriorityDTO.class);
    woPriorityDTO.setPriorityId(1L);
    woPriorityDTO.setPriorityName("PriorityName");
    List<WoPriorityDTO> listPriorityDTO = Mockito.spy(ArrayList.class);
    listPriorityDTO.add(woPriorityDTO);
    PowerMockito.when(
        woCategoryServiceProxy.getListWoPriorityDTO(any())
    ).thenReturn(listPriorityDTO);

    List<WoSearchDTO> crSubList = Mockito.spy(ArrayList.class);
    crSubList.add(woSearchDTO);

    PowerMockito.when(
        (List<WoSearchDTO>) DataUtil
            .subPageList(anyList(), anyInt(), anyInt())
    ).thenReturn(crSubList);

    Datatable actual = crBusiness.getListDataSearch(woSearchDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void testLoadMop_01() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("UNIT-TEST");

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrId("1");
    crInsiteDTO.setIsLoadMop("1");
    crInsiteDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("13/05/2020 12:00:00")
    );

    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(crInsiteDTO);

    List<CrAffectedServiceDetailsDTO> lstCrAffectedServiceDetailsDTOs = Mockito
        .spy(ArrayList.class);
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .search(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstCrAffectedServiceDetailsDTOs);

    List<CrInsiteDTO> lstCr = Mockito.spy(ArrayList.class);
    lstCr.add(crInsiteDTO);
    PowerMockito.when(
        crRepository.getListCrByCondition(anyList(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstCr);

    ResultInSideDto actual = crBusiness.loadMop(crInsiteDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testLoadMop_02() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("UNIT-TEST");

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrId("1");
    crInsiteDTO.setIsLoadMop("1");
    crInsiteDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("13/05/2020 12:00:00")
    );
    crInsiteDTO.setState("8");

    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(crInsiteDTO);

    List<CrAffectedServiceDetailsDTO> lstCrAffectedServiceDetailsDTOs = Mockito
        .spy(ArrayList.class);
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .search(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstCrAffectedServiceDetailsDTOs);

    ResultInSideDto actual = crBusiness.loadMop(crInsiteDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testLoadMop_03() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("UNIT-TEST");

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrId("1");
    crInsiteDTO.setIsLoadMop("1");
    crInsiteDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("13/05/2020 12:00:00")
    );
    crInsiteDTO.setState("9");

    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(crInsiteDTO);

    List<CrAffectedServiceDetailsDTO> lstCrAffectedServiceDetailsDTOs = Mockito
        .spy(ArrayList.class);
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .search(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstCrAffectedServiceDetailsDTOs);

    ResultInSideDto actual = crBusiness.loadMop(crInsiteDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testLoadMop_04() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("UNIT-TEST");

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrId("1");
    crInsiteDTO.setIsLoadMop("1");
    crInsiteDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("13/05/2020 12:00:00")
    );
    crInsiteDTO.setState("7");

    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(crInsiteDTO);

    List<CrAffectedServiceDetailsDTO> lstCrAffectedServiceDetailsDTOs = Mockito
        .spy(ArrayList.class);
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .search(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstCrAffectedServiceDetailsDTOs);

    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("11");
    crFilesAttachInsiteDTO.setDtFileHistory("VIPA_TT_INSERT");
    List<CrFilesAttachInsiteDTO> lstFile = Mockito.spy(ArrayList.class);
    lstFile.add(crFilesAttachInsiteDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesAttachByCondition(anyList(), anyInt(), anyInt(), anyString(),
                anyString())
    ).thenReturn(lstFile);

    ResultInSideDto actual = crBusiness.loadMop(crInsiteDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testLoadMop_05() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("UNIT-TEST");

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCrId("1");
    crInsiteDTO.setIsLoadMop("1");
    crInsiteDTO.setCreatedDate(
        DateTimeUtils.convertStringToDate("13/05/2020 12:00:00")
    );
    crInsiteDTO.setState("7");

    PowerMockito.when(
        crRepository.findCrById(anyLong(), any())
    ).thenReturn(crInsiteDTO);

    List<CrAffectedServiceDetailsDTO> lstCrAffectedServiceDetailsDTOs = Mockito
        .spy(ArrayList.class);
    PowerMockito.when(
        crAffectedServiceDetailsRepository
            .search(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lstCrAffectedServiceDetailsDTOs);

    CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = Mockito.spy(CrFilesAttachInsiteDTO.class);
    crFilesAttachInsiteDTO.setDtCode("11");
    crFilesAttachInsiteDTO.setDtFileHistory("VIPA_DD_INSERT");
    List<CrFilesAttachInsiteDTO> lstFile = Mockito.spy(ArrayList.class);
    lstFile.add(crFilesAttachInsiteDTO);
    PowerMockito.when(
        crFilesAttachRepository
            .getListCrFilesAttachByCondition(anyList(), anyInt(), anyInt(), anyString(),
                anyString())
    ).thenReturn(lstFile);

    MopDetailOutputDTO outputDTO = Mockito.spy(MopDetailOutputDTO.class);
    PowerMockito.when(
        wsVipaDdPort.getMopNotRunnedForCR(anyList())
    ).thenReturn(outputDTO);

    ResultInSideDto actual = crBusiness.loadMop(crInsiteDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testGetListAppGroup() throws Exception {
    AppGroupResult appGroupResult = Mockito.spy(AppGroupResult.class);
    PowerMockito.when(
        wstdttPort.getListAppGroup()
    ).thenReturn(appGroupResult);
    List<AppGroup> actual = crBusiness.getListAppGroup();

    Assert.assertEquals(0, actual.size());
  }

  @Test
  public void testGetListCrInfo_01() {
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCreatedDateFrom("12/05/2020 12:00:00");
    crDTO.setCreatedDateTo("12/05/2020 12:00:00");

    List<CrDTO> actual = crBusiness.getListCrInfo(crDTO);

    Assert.assertEquals(0, actual.size());
  }

  @Test
  public void testGetListCrInfo_02() {
    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCreatedDateFrom("12/05/2020 12:00:00");
    crDTO.setCreatedDateTo("12/05/2020 12:00:00");
    crDTO.setCrNumber("5");

    List<CrDTO> actual = crBusiness.getListCrInfo(crDTO);

    Assert.assertEquals(0, actual.size());
  }

  @Test
  public void testActionApproveServiceCR_01() {
    PowerMockito.mockStatic(StringUtils.class);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setLatestStartTime(
        DateTimeUtils.convertStringToDate("13/05/2020 12:00:00")
    );
    crDTO.setActionType("2");

    String actual = crBusiness.actionApproveServiceCR(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionApproveServiceCR_02() {
    PowerMockito.mockStatic(StringUtils.class);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setLatestStartTime(
        DateTimeUtils.convertStringToDate("13/05/2020 12:00:00")
    );

    String actual = crBusiness.actionApproveServiceCR(crDTO, "vi_VN");

    Assert.assertNull(actual);
  }

  @Test
  public void testActionReceiveServiceCR_01() {
    PowerMockito.mockStatic(StringUtils.class);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setLatestStartTime(
        DateTimeUtils.convertStringToDate("13/05/2020 12:00:00")
    );
    crDTO.setActionType("16");

    String actual = crBusiness.actionReceiveServiceCR(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionReceiveServiceCR_02() {
    PowerMockito.mockStatic(StringUtils.class);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setLatestStartTime(
        DateTimeUtils.convertStringToDate("13/05/2020 12:00:00")
    );

    String actual = crBusiness.actionReceiveServiceCR(crDTO, "vi_VN");

    Assert.assertNull(actual);
  }

  @Test
  public void testActionEditServiceCR_01() {
    PowerMockito.mockStatic(StringUtils.class);

    PowerMockito.when(
        crProcessFromClient.validateTime(any(), anyBoolean(), anyString())
    ).thenReturn(false);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);

    String actual = crBusiness.actionEditServiceCR(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionEditServiceCR_02() {
    PowerMockito.mockStatic(StringUtils.class);

    PowerMockito.when(
        crProcessFromClient.validateTime(any(), anyBoolean(), anyString())
    ).thenReturn(true);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);

    String actual = crBusiness.actionEditServiceCR(crDTO, "vi_VN");

    Assert.assertNull(actual);
  }

  @Test
  public void testActionAssignCabServiceCR_01() {
    PowerMockito.mockStatic(StringUtils.class);

    PowerMockito.when(
        crProcessFromClient.validateForService(any())
    ).thenReturn("SUCCESS");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);

    String actual = crBusiness.actionEditServiceCR(crDTO, "vi_VN");

    Assert.assertEquals(RESULT.ERROR, actual);
  }

  @Test
  public void testActionAssignCabServiceCR_02() {
    PowerMockito.mockStatic(StringUtils.class);

    PowerMockito.when(
        crProcessFromClient.validateForService(any())
    ).thenReturn("");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);

    String actual = crBusiness.actionAssignCabServiceCR(crDTO, "vi_VN");

    Assert.assertNull(actual);
  }

  @Test
  public void testUpdateWorkOrder_01() throws Exception{
    PowerMockito.when(wsVipaDdPort.updateWorkOrder(anyString(), anyLong(), anyObject())).thenReturn(RESULT.SUCCESS);
    String result = crBusiness.updateWorkOrder("CR_NUMBER_2002", 1L, null);
    Assert.assertEquals(RESULT.SUCCESS, result);
  }

  @Test
  public void testDeletWoMopTest_01() throws Exception{
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(RESULT.SUCCESS);
    resultDTO.setMessage(RESULT.SUCCESS);
    resultDTO.setId("1");
    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1l);
    woInsideDTO.setWoSystemId("1");
    woInsideDTO.setWoSystemOutId("1,2");
    woInsideDTO.setWoCode("WO_CODE_2020");
    PowerMockito.when(woServiceProxy.deleteWOForRollbackProxy(anyString(), anyString(), anyString())).thenReturn(resultDTO);
    PowerMockito.when(wsVipaDdPort.updateWorkOrder(any(), anyLong(), anyObject())).thenReturn(RESULT.SUCCESS);
    ResultInSideDto actual = crBusiness.deletWoMopTest(woInsideDTO);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void getListCRForExportServiceV2_01(){
    CrInsiteDTO cr = Mockito.spy(CrInsiteDTO.class);
    cr.setCrId("1");
    Date dateSearch = Mockito.spy(Date.class);
    dateSearch.setTime(1111111l);
    List<CrDTO> expects = Mockito.spy(ArrayList.class);
    expects.add(cr.toCrDTO());
    PowerMockito.when(crRepository.getListCRForExportServiceV2(
        anyObject(), anyString(), anyObject(), anyObject(), anyObject(), anyObject(), anyInt(), anyInt(), anyString())).thenReturn(expects);
    List<CrDTO> actual = crBusiness.getListCRForExportServiceV2(cr,"", dateSearch, dateSearch, dateSearch, dateSearch, 0, 1, "vi_VN");
    Assert.assertEquals(1, actual.size());
  }

  @Test
  public void getListSecondaryCr_02(){
    CrDTO cr = Mockito.spy(CrDTO.class);
    cr.setCrId("2");
    List<CrDTO> expects = Mockito.spy(ArrayList.class);
    expects.add(cr);
    expects.add(cr);
    PowerMockito.when(crRepository.getListSecondaryCrOutSide(
        anyObject())).thenReturn(expects);
    List<CrDTO> actual = crBusiness.getListSecondaryCr(cr);
    Assert.assertEquals(2, actual.size());
  }

  @Test
  public void getListCrByCondition_01(){
    CrInsiteDTO cr = Mockito.spy(CrInsiteDTO.class);
    cr.setCrId("2");
    List<CrInsiteDTO> expects = Mockito.spy(ArrayList.class);
    List<ConditionBean> lstConditionBeans = Mockito.spy(ArrayList.class);
    expects.add(cr);
    expects.add(cr);
    expects.add(cr);
    PowerMockito.when(crRepository.getListCrByCondition(
        anyList(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(expects);
    List<CrInsiteDTO> actual = crBusiness.getListCrByCondition(lstConditionBeans, 0, 3, "", "");
    Assert.assertEquals(3, actual.size());
  }

  @Test
  public void approveAssign_01() throws Exception {
    CrInsiteDTO inputCr = Mockito.spy(CrInsiteDTO.class);
    inputCr.setCrId("1");
    inputCr.setCrNumber("CR_NUMBER_CODE_1");
    inputCr.setCountry("3500289726");
    inputCr.setHandoverCa("999999");
    inputCr.setChangeResponsible("999999");
    PowerMockito.when(crRepository.findCrById(anyLong())).thenReturn(inputCr);
    PowerMockito.when(crRepository.updateCr(any())).thenReturn(RESULT.SUCCESS);
    mockUserToken();
    mockI18n();
    mockInsertWorklogThreeParams();
    List<WorkLogInsiteDTO> lstWorks = Mockito.spy(ArrayList.class);
    List<CrAlarmInsiteDTO> lstAlarm = Mockito.spy(ArrayList.class);
    CrAlarmInsiteDTO itemAlarm = Mockito.spy(CrAlarmInsiteDTO.class);
    WorkLogInsiteDTO itemWorkLog = Mockito.spy(WorkLogInsiteDTO.class);
    itemAlarm.setCrId(1L);
    itemWorkLog.setWlayId(1L);
    lstWorks.add(itemWorkLog);
    lstAlarm.add(itemAlarm);
    PowerMockito.when(mrCategoryUtil.getListWorkLogDTO(anyObject())).thenReturn(lstWorks);
    PowerMockito.when(crAlarmRepository.getListAlarmByCr(anyObject())).thenReturn(lstAlarm);
    PowerMockito.when(wsNocprov4Port.onExecuteMapQuery(anyString(), anyObject(), anyString())).thenReturn(RESULT.SUCCESS);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.doNothing().when(messagesBusiness).insertMessageForUserCR(anyString(), anyObject());
    ResultInSideDto actual = crBusiness.approveAssign(inputCr);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void rejectAssign_01() {
    CrInsiteDTO inputCr = Mockito.spy(CrInsiteDTO.class);
    inputCr.setCrId("2");
    inputCr.setCrNumber("CR_NUMBER_CODE_2");
    inputCr.setCountry("3500289726");
    inputCr.setChangeResponsible("48");
    mockUserToken();
    mockI18n();
    PowerMockito.when(crRepository.findCrById(anyLong())).thenReturn(inputCr);
    PowerMockito.when(crRepository.updateCr(any())).thenReturn(RESULT.SUCCESS);
    mockInsertWorklogThreeParams();
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.doNothing().when(messagesBusiness).insertMessageForUserCR(anyString(), anyObject());
    ResultInSideDto actual = crBusiness.rejectAssign(inputCr);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void actionCancelCrGeneral_01() {
    CrInsiteDTO inputCr = Mockito.spy(CrInsiteDTO.class);
    CrBusinessImpl crBusinessInline = Mockito.spy(crBusiness);
    inputCr.setCrId("2");
    mockUserToken();
    mockI18n();
    PowerMockito.when(crProcessFromClient.converTimeFromClientToServer(anyObject(), anyString())).thenReturn(inputCr);
    PowerMockito.doReturn(RESULT.SUCCESS).when(crBusinessInline).actionCancelCr(any());
    String actuaFinal = crBusinessInline.actionCancelCrGeneral(inputCr);
    Assert.assertEquals(RESULT.SUCCESS, actuaFinal);
  }

  @Test
  public void getListCrByIp_01() {
    CrInsiteDTO inputCr = Mockito.spy(CrInsiteDTO.class);
    inputCr.setCrId("2");
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setTotal(10);
    PowerMockito.when(crRepository.getListCrByIp(anyObject())).thenReturn(datatable);
    Datatable actual = crBusiness.getListCrByIp(inputCr);
    Assert.assertEquals(10, actual.getTotal());
  }

  @Test
  public void updateCrTimeOverdue_01() {
    CrInsiteDTO inputCr = Mockito.spy(CrInsiteDTO.class);
    inputCr.setCrId("3");
    PowerMockito.when(crProcessFromClient.getFormForProcess(anyObject())).thenReturn(inputCr);
    PowerMockito.when(crRepository.findCrById(anyLong())).thenReturn(inputCr);
    PowerMockito.doNothing().when(crDBRepository).updateCrTimeInCaseResolve(anyObject(), anyBoolean());
    ResultInSideDto resultInSideDto = crBusiness.updateCrTimeOverdue(inputCr);
    Assert.assertEquals(RESULT.SUCCESS, resultInSideDto.getKey());
  }

  @Test
  public void updateCrTimeOverdueToMop_01() {
    CrInsiteDTO inputCr = Mockito.spy(CrInsiteDTO.class);
    inputCr.setCrId("3");
    PowerMockito.when(crProcessFromClient.updateCrToMopInsite(anyObject())).thenReturn(RESULT.SUCCESS);
    ResultInSideDto resultInSideDto = crBusiness.updateCrTimeOverdueToMop(inputCr, inputCr);
    Assert.assertEquals(RESULT.SUCCESS, resultInSideDto.getKey());
  }

  @Test
  public void updateCrTimeOverdueToMop_02() {
    CrInsiteDTO inputCr = Mockito.spy(CrInsiteDTO.class);
    inputCr.setCrId("3");
    PowerMockito.when(crProcessFromClient.updateCrToMopInsite(anyObject())).thenReturn(RESULT.ERROR);
    PowerMockito.doNothing().when(crDBRepository).updateCrTimeInCaseResolve(anyObject(), anyBoolean());
    ResultInSideDto resultInSideDto = crBusiness.updateCrTimeOverdueToMop(inputCr, inputCr);
    Assert.assertEquals(RESULT.ERROR, resultInSideDto.getKey());
  }

  @Test
  public void processWOTabAdd_01() {
    CrInsiteDTO inputCr = Mockito.spy(CrInsiteDTO.class);
    inputCr.setCrId("3");
    CrProcessWoDTO expect = Mockito.spy(CrProcessWoDTO.class);
    expect.setCrProcessWoId(1L);
    PowerMockito.when(crProcessFromClient.processWOTabAdd(anyObject())).thenReturn(expect);
    CrProcessWoDTO actual = crBusiness.processWOTabAdd(inputCr);
    Assert.assertNotNull(actual);
  }

  @Test
  public void doAssignHandoverCa_01() throws Exception {
    CrInsiteDTO inputCr = Mockito.spy(CrInsiteDTO.class);
    inputCr.setCrId("5");
    inputCr.setCrNumber("CR_NUMBER_CODE_5");
    inputCr.setCountry("3500289726");
    inputCr.setHandoverCa("999999");
    inputCr.setChangeResponsible("999999");
    inputCr.setWorkLog("111");
    List<MultipartFile> lstFile = Mockito.spy(ArrayList.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    lstFile.add(multipartFile);
    PowerMockito.when(crRepository.findCrById(anyLong())).thenReturn(inputCr);
    PowerMockito.when(crRepository.updateCr(any())).thenReturn(RESULT.SUCCESS);
    PowerMockito.when(multipartFile.getOriginalFilename()).thenReturn("file1.txt");
    byte []bytes = new byte[] {1,2,3};
    PowerMockito.when(multipartFile.getBytes()).thenReturn(bytes);
    mockFileUtilsSaveFile();
    mockUserToken();
    mockI18n();
    ResultInSideDto result = getResultInsideSuccess();
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitName("M1");
    unitDTO.setUnitId(413314L);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitDTO);
    PowerMockito.when(crFilesAttachRepository.add(any())).thenReturn(result);
    PowerMockito.when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), any(), anyList())).thenReturn(result);
    PowerMockito.when(crRepository.insertWorkLog(any())).thenReturn(result);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    PowerMockito.when(userRepository.getUserByUserId(anyLong())).thenReturn(usersEntity);
    PowerMockito.doNothing().when(messagesBusiness).insertMessageForUserCR(any(), any());
    ResultInSideDto actual = crBusiness.doAssignHandoverCa(inputCr, lstFile);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void insertWorkLog_01() throws Exception {
    CrInsiteDTO inputCr = Mockito.spy(CrInsiteDTO.class);
    inputCr.setCrId("5");
    inputCr.setCrNumber("CR_NUMBER_CODE_5");
    inputCr.setCountry("3500289726");
    inputCr.setHandoverCa("999999");
    inputCr.setChangeResponsible("999999");
    inputCr.setWorkLog("111");

    WorkLogInsiteDTO wlInput = Mockito.spy(WorkLogInsiteDTO.class);
    wlInput.setWlgObjectType(Long.valueOf(WORK_LOG_SYSTEM.CR));
    wlInput.setWlayId(79L);
    wlInput.setWlgObjectId(5l);
    wlInput.setUserGroupAction(11L);
    wlInput.setWlgObjectType(2L);
    CrBusinessImpl crBusinessInline = Mockito.spy(crBusiness);
    PowerMockito.doReturn(true).when(crBusinessInline).validate(any());
    PowerMockito.doReturn(inputCr).when(crBusinessInline).findCrById(any());
    ResultInSideDto resultInSideDto = getResultInsideSuccess();
    PowerMockito.when(crRepository.insertWorkLog(any())).thenReturn(resultInSideDto);
    PowerMockito.when(smsDBBussiness.sendSMSToLstUserConfig(any(), any())).thenReturn(RESULT.SUCCESS);
    PowerMockito.mockStatic(NocProWS.class);
    NocProWS nocProWS = PowerMockito.mock(NocProWS.class);
    PowerMockito.when(NocProWS.getNocProWS()).thenReturn(nocProWS);
    PowerMockito.when(nocProWS.updateCrToFinishImpact(any(), anyString())).thenReturn(null);
    mockI18n();
    mockUserToken();
    ResultInSideDto actual = crBusinessInline.insertWorkLog(wlInput);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  public void mockUserToken() {
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserID(999999l);
    userToken.setDeptId(413314l);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
  }

  public void mockI18n() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("some text...");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("some text...");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("some text...");
  }


  public void mockInsertWorklogThreeParams() {
    ResultInSideDto resultInSideDto = getResultInsideSuccess();
    PowerMockito.when(crRepository.insertWorkLog(anyObject())).thenReturn(resultInSideDto);
  }

  public ResultInSideDto getResultInsideSuccess() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);
    return resultInSideDto;
  }

  public void mockFileUtilsSaveFile() throws Exception{
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.when(PassTranformer.decrypt(anyString())).thenReturn("decrypt");
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(), any(), any())).thenReturn("/path/junit");
    PowerMockito.when(FileUtils.saveUploadFile(anyString(), any(), anyString(), any())).thenReturn("/path/upload");
  }
}

