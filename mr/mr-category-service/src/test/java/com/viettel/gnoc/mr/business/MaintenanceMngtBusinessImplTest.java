
package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.*;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrApprovalDepartmentDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveRolesDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrSchedulePeriodicDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
import com.viettel.gnoc.mr.repository.MaintenanceMngtRepository;
import com.viettel.gnoc.mr.repository.MrServiceRepository;
import com.viettel.gnoc.mr.repository.UserGroupCategoryRepository;
import com.viettel.gnoc.mr.repository.WorkLogCategoryRepository;
import com.viettel.gnoc.mr.repository.WorkLogRepository;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoKTTSInfoDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseDTO;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.gnoc.ws.provider.NocProWS;
import com.viettel.nms.nocpro.service.ResponseBO;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@PrepareForTest({MaintenanceMngtBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MaintenanceMngtBusinessImplTest {

  @InjectMocks
  MaintenanceMngtBusinessImpl maintenanceMngtBusiness;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  MrServiceBusiness mrServiceBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  MrApprovalDepartmentBusinessImpl mrApprovalDepartmentServiceImpl;

  @Mock
  MaintenanceMngtRepository maintenanceMngtRepository;

  @Mock
  MrHisServiceBusiness mrHisServiceBusiness;


  @Mock
  MrImpactedNodesBusiness mrImpactedNodesBusiness;

  @Mock
  MrWoTempBusiness mrWoTempBusiness;

  @Mock
  MrSchedulePeriodicBusiness mrSchedulePeriodicBusiness;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  UserBusiness userBusiness;

  @Mock
  MrApprovalDepartmentBusiness mrApprovalDepartmentBusiness;

  @Mock
  UnitBusiness unitBusiness;

  @Mock
  MessagesBusiness messagesBusiness;

  @Mock
  UserGroupCategoryRepository userGroupCategoryRepository;

  @Mock
  WorkLogCategoryRepository workLogCategoryRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  WorkLogRepository workLogRepository;

  @Mock
  NocProWS nocProWS;

  @Mock
  MrServiceRepository mrServiceRepository;

  @Test
  public void exportSearchData() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    MrSearchDTO mrSearchDTO = Mockito.spy(MrSearchDTO.class);
    mrSearchDTO.setMrTypeName("H");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(2L);
    unitDTO.setUnitCode("2");
    unitDTO.setUnitName("2");
    unitDTO.setParentUnitId(2L);
    List<UnitDTO> lsUnit = Mockito.spy(ArrayList.class);
    lsUnit.add(unitDTO);
    when(commonStreamServiceProxy.getListUnit(any())).thenReturn(lsUnit);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUserId(2L);
    List<UsersInsideDto> lstUserDTO = Mockito.spy(ArrayList.class);
    lstUserDTO.add(usersInsideDto);
    when(commonStreamServiceProxy.getListUserDTOByProxy(any())).thenReturn(lstUserDTO);

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("2");
    List<CatLocationDTO> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(catLocationDTO);
    when(commonStreamServiceProxy.getCatLocationByLevel(any())).thenReturn(lstCountry);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(2L);
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lstCd);

    MrDTO mrDTO = Mockito.spy(MrDTO.class);
    mrDTO.setCountry("2");
    mrDTO.setRegion("2");
    mrDTO.setCdGroupWo("2");
    mrDTO.setState("CLOSE");
    mrDTO.setMrType("test");
    mrDTO.setMrTypeName("Soft");
    mrDTO.setChangeResponsible("2");
    mrDTO.setWoId("2");
    mrDTO.setResponsibleUnitCR("2");
    mrDTO.setConsiderUnitCR("2");
    mrDTO.setUnitCreateMr("2");
    mrDTO.setUnitExecute("2");
    List<MrDTO> lstTmp = Mockito.spy(ArrayList.class);
    lstTmp.add(mrDTO);
    when(mrServiceRepository.getListMrCrWoNewForExport(any())).thenReturn(lstTmp);
    when(mrServiceBusiness.getWorklogFromWo(any())).thenReturn(lstTmp);
    PowerMockito.when(I18n.getLanguage(anyString()))
        .thenReturn("xxxxxxxxxxxxxxxxxxx");
    PowerMockito.when(I18n.getLanguage("mrMngt.mrType.hardWO"))
        .thenReturn("Physical maintenance");
    PowerMockito.when(I18n.getLanguage("mrMngt.list.mrSoft"))
        .thenReturn("Soft");
    PowerMockito.when(I18n.getLanguage("mrMngt.list.mrHard"))
        .thenReturn("Hard");

    File fileExportSuccess = new File("./test_junit/test.txt");

    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    maintenanceMngtBusiness.exportSearchData(mrSearchDTO);
  }

  @Test
  public void getListMrDTOSearch() {
  }

  @Test
  public void onInsert() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    InfraDeviceDTO infraDeviceDTO = Mockito.spy(InfraDeviceDTO.class);
    List<InfraDeviceDTO> lstNode = Mockito.spy(ArrayList.class);
    lstNode.add(infraDeviceDTO);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    mrInsideDTO.setMrType("mrMngt.list.mrHard");
    String b = "a";
    mrInsideDTO.setDescription("2");
    mrInsideDTO.setMrId(2L);
    ArrayList<String> lstString = new ArrayList<>();
    lstString.add(b);
    mrInsideDTO.setUrl(lstString);
    mrInsideDTO.setLstNode(lstNode);
    mrInsideDTO.setState("Đóng");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    List<CrDTO> lstCr = Mockito.spy(ArrayList.class);
    when(crServiceProxy.getListCRFromOtherSystem(any())).thenReturn(lstCr);

    MrApproveRolesDTO mrApproveRolesDTO = Mockito.spy(MrApproveRolesDTO.class);
    List<MrApproveRolesDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(mrApproveRolesDTO);
    when(mrApprovalDepartmentServiceImpl.getLstMrApproveUserByRole(any())).thenReturn(lstRole);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveRolesDTO.setUnitId("1");
    List<MrApproveSearchDTO> lstAppDept = Mockito.spy(ArrayList.class);
    lstAppDept.add(mrApproveSearchDTO);
    lstAppDept.add(mrApproveSearchDTO);
    when(maintenanceMngtRepository.getLstMrApproveDeptByUser(any())).thenReturn(lstAppDept);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("test");
    resultInSideDto.setKey("SUCCESS");
    when(mrApprovalDepartmentServiceImpl.insertMrApprovalDepartment(any()))
        .thenReturn(resultInSideDto);
    when(mrServiceBusiness.insertMr(any())).thenReturn(resultInSideDto);
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(resultInSideDto);

    String a = "2";
    List<String> lstSeq = Mockito.spy(ArrayList.class);
    lstSeq.add(a);
    when(woServiceProxy.getSequenseWoProxy("WO_SEQ", 1)).thenReturn(lstSeq);

    when(mrImpactedNodesBusiness.insertOrUpdateListMrImpactedNodes(any())).thenReturn("a");

    PowerMockito.when(I18n.getLanguage("common.success")).thenReturn("Thanh cong");
    PowerMockito.when(I18n.getLanguage("mrMngt.list.approve")).thenReturn("ABC");
    PowerMockito.when(I18n.getLanguage("common.button.add")).thenReturn("themmoi");
    PowerMockito.when(I18n.getLanguage("mrMngt.list.mrHard")).thenReturn("test");

    maintenanceMngtBusiness.onInsert(mrInsideDTO);
  }

  @Test
  public void validateAddForm() {
    MrInsideDTO ui = Mockito.spy(MrInsideDTO.class);
    ui.setMrId(2L);
    ui.setMrTechnichcal("TBM");
    ui.setState("mrMngt.stateCbb.close");

    List<MrWoTempDTO> lst = Mockito.spy(ArrayList.class);
    when(mrWoTempBusiness.getListMrWoTempDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);

    MrSchedulePeriodicDTO mrSchedulePeriodicDTO = Mockito.spy(MrSchedulePeriodicDTO.class);
    mrSchedulePeriodicDTO.setWoId("2");
    List<MrSchedulePeriodicDTO> lstSche = Mockito.spy(ArrayList.class);
    lstSche.add(mrSchedulePeriodicDTO);
    when(mrSchedulePeriodicBusiness
        .getListMrSchedulePeriodicDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstSche);
    maintenanceMngtBusiness.validateAddForm(true, ui);
  }

  @Test
  public void validateAddForm1() {
    PowerMockito.mockStatic(I18n.class);
    MrInsideDTO ui = Mockito.spy(MrInsideDTO.class);
    ui.setMrId(2L);
    ui.setMrTechnichcal("TBM");
    ui.setState("mrMngt.stateCbb.close");

    MrWoTempDTO mrWoTempDTO = Mockito.spy(MrWoTempDTO.class);
    mrWoTempDTO.setWoWfmId("2");
    List<MrWoTempDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrWoTempDTO);
    when(mrWoTempBusiness.getListMrWoTempDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);

    MrSchedulePeriodicDTO mrSchedulePeriodicDTO = Mockito.spy(MrSchedulePeriodicDTO.class);
    mrSchedulePeriodicDTO.setWoId("3");
    List<MrSchedulePeriodicDTO> lstSche = Mockito.spy(ArrayList.class);
    lstSche.add(mrSchedulePeriodicDTO);
    when(mrSchedulePeriodicBusiness
        .getListMrSchedulePeriodicDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstSche);

    WoDTO wo = Mockito.spy(WoDTO.class);
    when(woServiceProxy.findWoById(any())).thenReturn(wo);
    maintenanceMngtBusiness.validateAddForm(true, ui);
  }

  @Test
  public void validateAddForm2() {
    MrInsideDTO ui = Mockito.spy(MrInsideDTO.class);
    ui.setMrId(2L);
    ui.setMrTechnichcal("TBM");
    ui.setState("mrMngt.stateCbb.close");

    MrWoTempDTO mrWoTempDTO = Mockito.spy(MrWoTempDTO.class);
    mrWoTempDTO.setWoWfmId("2");
    List<MrWoTempDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrWoTempDTO);
    when(mrWoTempBusiness.getListMrWoTempDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);

    MrSchedulePeriodicDTO mrSchedulePeriodicDTO = Mockito.spy(MrSchedulePeriodicDTO.class);
    mrSchedulePeriodicDTO.setWoId("3");
    List<MrSchedulePeriodicDTO> lstSche = Mockito.spy(ArrayList.class);
    lstSche.add(mrSchedulePeriodicDTO);
    when(mrSchedulePeriodicBusiness
        .getListMrSchedulePeriodicDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstSche);

    WoDTO wo = Mockito.spy(WoDTO.class);
    wo.setStatus("2");
    when(woServiceProxy.findWoById(any())).thenReturn(wo);
    maintenanceMngtBusiness.validateAddForm(true, ui);
  }

  @Test
  public void validateAddForm3() {
    MrInsideDTO ui = Mockito.spy(MrInsideDTO.class);
    ui.setMrId(2L);
    ui.setMrTechnichcal("TBMs");
    ui.setState("mrMngt.stateCbb.close");

    MrWoTempDTO mrWoTempDTO = Mockito.spy(MrWoTempDTO.class);
    mrWoTempDTO.setWoWfmId("2");
    List<MrWoTempDTO> lst = Mockito.spy(ArrayList.class);
    when(mrWoTempBusiness.getListMrWoTempDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);

    MrSchedulePeriodicDTO mrSchedulePeriodicDTO = Mockito.spy(MrSchedulePeriodicDTO.class);
    mrSchedulePeriodicDTO.setWoId("3");
    List<MrSchedulePeriodicDTO> lstSche = Mockito.spy(ArrayList.class);
    lstSche.add(mrSchedulePeriodicDTO);
    when(mrSchedulePeriodicBusiness
        .getListMrSchedulePeriodicDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstSche);

    WoDTO wo = Mockito.spy(WoDTO.class);
    wo.setStatus("2");
    when(woServiceProxy.findWoById(any())).thenReturn(wo);
    maintenanceMngtBusiness.validateAddForm(true, ui);
  }

  @Test
  public void validateAddForm4() {
    MrInsideDTO ui = Mockito.spy(MrInsideDTO.class);
    ui.setMrId(2L);
    ui.setMrTechnichcal("TBMs");
    ui.setState("mrMngt.stateCbb.close");

    MrWoTempDTO mrWoTempDTO = Mockito.spy(MrWoTempDTO.class);
    mrWoTempDTO.setWoWfmId("2");
    List<MrWoTempDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrWoTempDTO);
    when(mrWoTempBusiness.getListMrWoTempDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);

    MrSchedulePeriodicDTO mrSchedulePeriodicDTO = Mockito.spy(MrSchedulePeriodicDTO.class);
    mrSchedulePeriodicDTO.setWoId("3");
    List<MrSchedulePeriodicDTO> lstSche = Mockito.spy(ArrayList.class);
    lstSche.add(mrSchedulePeriodicDTO);
    when(mrSchedulePeriodicBusiness
        .getListMrSchedulePeriodicDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lstSche);

    WoDTO wo = Mockito.spy(WoDTO.class);
    wo.setStatus("2");
    when(woServiceProxy.findWoById(any())).thenReturn(wo);
    maintenanceMngtBusiness.validateAddForm(true, ui);
  }

  @Test
  public void validateApprove() {
    MrInsideDTO ui = Mockito.spy(MrInsideDTO.class);
    ui.setReason("2");
    ui.setReasonDetail("");
    maintenanceMngtBusiness.validateApprove(ui);
  }

  @Test
  public void validateApprove1() {
    MrInsideDTO ui = Mockito.spy(MrInsideDTO.class);
    ui.setReason("1");
    ui.setReasonDetail("");
    maintenanceMngtBusiness.validateApprove(ui);
  }

  @Test
  public void saveWoTemp() {
  }

  @Test
  public void insertWoFromWeb() throws Exception {
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    WoDetailDTO woDetail = Mockito.spy(WoDetailDTO.class);
    List<WoMerchandiseDTO> lstMerchandise = Mockito.spy(ArrayList.class);
    WoKTTSInfoDTO woKttsInfo = Mockito.spy(WoKTTSInfoDTO.class);

    String user = "";
    List<WoTestServiceMapDTO> lstWoTestService = Mockito.spy(ArrayList.class);

    ResultInSideDto dto = Mockito.spy(ResultInSideDto.class);
    when(woServiceProxy.insertWoFromWebInMrMNGT(any())).thenReturn(dto);

    maintenanceMngtBusiness
        .insertWoFromWeb(woDTO, woDetail, lstMerchandise, woKttsInfo, user, lstWoTestService);
  }

  @Test
  public void saveNodeNetwork() {
  }

  @Test
  public void getLstInsert() {
  }

  @Test
  public void setApproveDept() {
  }

  @Test
  public void getApproveDept2level() {
    Long userId = 1L;
    List<MrApproveSearchDTO> lst = Mockito.spy(ArrayList.class);
    when(maintenanceMngtRepository.getLstMrApproveDeptByUser(any())).thenReturn(lst);
    List<MrApproveSearchDTO> result = maintenanceMngtBusiness.getApproveDept2level(userId);
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void onEdit() {
    MrApprovalDepartmentDTO mrApprovalDepartmentDTO = Mockito.spy(MrApprovalDepartmentDTO.class);
    mrApprovalDepartmentDTO.setReturnCode("6");

    PowerMockito.mockStatic(I18n.class);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setState("1");
    mrDTO.setMrApprovalDepartmentDTO(mrApprovalDepartmentDTO);
    mrDTO.setIsTP(true);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(1L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    List<CrDTO> lstCr = null;
    when(crServiceProxy.getListCRFromOtherSystem(any())).thenReturn(lstCr);

    String res2 = "SUCCESS";
    when(mrApprovalDepartmentBusiness.updateMrApprovalDepartment(any())).thenReturn(res2);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveSearchDTO.setMadtLevel("2");
    mrApproveSearchDTO.setStatus("1");
    List<MrApproveSearchDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrApproveSearchDTO);
    when(mrApprovalDepartmentBusiness.getLstMrApproveSearch(any())).thenReturn(lst);

    ResultInSideDto res1 = Mockito.spy(ResultInSideDto.class);
    res1.setKey("SUCCESS");
    when(mrServiceBusiness.updateMr(any())).thenReturn(res1);

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto edit = Mockito.spy(ResultInSideDto.class);
    edit.setMessage("test");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(edit);

    maintenanceMngtBusiness.onEdit(mrDTO);
  }

  @Test
  public void onEdit2() {
    MrApprovalDepartmentDTO mrApprovalDepartmentDTO = Mockito.spy(MrApprovalDepartmentDTO.class);
    mrApprovalDepartmentDTO.setReturnCode("6");

    PowerMockito.mockStatic(I18n.class);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setState("4");
    mrDTO.setMrApprovalDepartmentDTO(mrApprovalDepartmentDTO);
    mrDTO.setIsTP(true);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(1L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    List<CrDTO> lstCr = null;
    when(crServiceProxy.getListCRFromOtherSystem(any())).thenReturn(lstCr);

    String res2 = "FALSE";
    when(mrApprovalDepartmentBusiness.updateMrApprovalDepartment(any())).thenReturn(res2);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveSearchDTO.setMadtLevel("2");
    mrApproveSearchDTO.setStatus("1");
    List<MrApproveSearchDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrApproveSearchDTO);
    when(mrApprovalDepartmentBusiness.getLstMrApproveSearch(any())).thenReturn(lst);

    ResultInSideDto res1 = Mockito.spy(ResultInSideDto.class);
    res1.setKey("SUCCESS");
    when(mrServiceBusiness.updateMr(any())).thenReturn(res1);

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto edit = Mockito.spy(ResultInSideDto.class);
    edit.setMessage("test");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(edit);

    maintenanceMngtBusiness.onEdit(mrDTO);
  }

  @Test
  public void onEdit3() {
    MrApprovalDepartmentDTO mrApprovalDepartmentDTO = Mockito.spy(MrApprovalDepartmentDTO.class);
    mrApprovalDepartmentDTO.setReturnCode("6");

    PowerMockito.mockStatic(I18n.class);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setState("7");
    mrDTO.setMrApprovalDepartmentDTO(mrApprovalDepartmentDTO);
    mrDTO.setIsTP(true);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(1L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    List<CrDTO> lstCr = null;
    when(crServiceProxy.getListCRFromOtherSystem(any())).thenReturn(lstCr);

    String res2 = "SUCCESS";
    when(mrApprovalDepartmentBusiness.updateMrApprovalDepartment(any())).thenReturn(res2);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveSearchDTO.setMadtLevel("2");
    mrApproveSearchDTO.setStatus("1");
    List<MrApproveSearchDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrApproveSearchDTO);
    when(mrApprovalDepartmentBusiness.getLstMrApproveSearch(any())).thenReturn(lst);

    ResultInSideDto res1 = Mockito.spy(ResultInSideDto.class);
    res1.setKey("FALSE");
    when(mrServiceBusiness.updateMr(any())).thenReturn(res1);

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto edit = Mockito.spy(ResultInSideDto.class);
    edit.setMessage("test");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(edit);

    maintenanceMngtBusiness.onEdit(mrDTO);
  }

  @Test
  public void onEdit4() {
    MrApprovalDepartmentDTO mrApprovalDepartmentDTO = Mockito.spy(MrApprovalDepartmentDTO.class);
    mrApprovalDepartmentDTO.setReturnCode("6");

    PowerMockito.mockStatic(I18n.class);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setState("1");
    mrDTO.setMrApprovalDepartmentDTO(mrApprovalDepartmentDTO);
    mrDTO.setIsTP(true);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(1L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    List<CrDTO> lstCr = null;
    when(crServiceProxy.getListCRFromOtherSystem(any())).thenReturn(lstCr);

    String res2 = "FALSE";
    when(mrApprovalDepartmentBusiness.updateMrApprovalDepartment(any())).thenReturn(res2);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveSearchDTO.setMadtLevel("2");
    mrApproveSearchDTO.setStatus("1");
    List<MrApproveSearchDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrApproveSearchDTO);
    when(mrApprovalDepartmentBusiness.getLstMrApproveSearch(any())).thenReturn(lst);

    ResultInSideDto res1 = Mockito.spy(ResultInSideDto.class);
    res1.setKey("FALSE");
    when(mrServiceBusiness.updateMr(any())).thenReturn(res1);

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto edit = Mockito.spy(ResultInSideDto.class);
    edit.setMessage("test");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(edit);

    maintenanceMngtBusiness.onEdit(mrDTO);
  }

  @Test
  public void onEdit5() {
    MrApprovalDepartmentDTO mrApprovalDepartmentDTO = Mockito.spy(MrApprovalDepartmentDTO.class);
    mrApprovalDepartmentDTO.setReturnCode("6");

    PowerMockito.mockStatic(I18n.class);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setState("4");
    mrDTO.setMrApprovalDepartmentDTO(mrApprovalDepartmentDTO);
    mrDTO.setIsTP(true);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(1L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    List<CrDTO> lstCr = null;
    when(crServiceProxy.getListCRFromOtherSystem(any())).thenReturn(lstCr);

    String res2 = "FALSE";
    when(mrApprovalDepartmentBusiness.updateMrApprovalDepartment(any())).thenReturn(res2);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveSearchDTO.setMadtLevel("2");
    mrApproveSearchDTO.setStatus("1");
    List<MrApproveSearchDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrApproveSearchDTO);
    when(mrApprovalDepartmentBusiness.getLstMrApproveSearch(any())).thenReturn(lst);

    ResultInSideDto res1 = Mockito.spy(ResultInSideDto.class);
    res1.setKey("FALSE");
    when(mrServiceBusiness.updateMr(any())).thenReturn(res1);

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto edit = Mockito.spy(ResultInSideDto.class);
    edit.setMessage("test");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(edit);

    maintenanceMngtBusiness.onEdit(mrDTO);
  }

  @Test
  public void onEdit6() {
    MrApprovalDepartmentDTO mrApprovalDepartmentDTO = Mockito.spy(MrApprovalDepartmentDTO.class);
    mrApprovalDepartmentDTO.setReturnCode("6");

    PowerMockito.mockStatic(I18n.class);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setState("7");
    mrDTO.setMrApprovalDepartmentDTO(mrApprovalDepartmentDTO);
    mrDTO.setIsTP(true);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(1L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    List<CrDTO> lstCr = null;
    when(crServiceProxy.getListCRFromOtherSystem(any())).thenReturn(lstCr);

    String res2 = "FALSE";
    when(mrApprovalDepartmentBusiness.updateMrApprovalDepartment(any())).thenReturn(res2);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveSearchDTO.setMadtLevel("2");
    mrApproveSearchDTO.setStatus("1");
    List<MrApproveSearchDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrApproveSearchDTO);
    when(mrApprovalDepartmentBusiness.getLstMrApproveSearch(any())).thenReturn(lst);

    ResultInSideDto res1 = Mockito.spy(ResultInSideDto.class);
    res1.setKey("FALSE");
    when(mrServiceBusiness.updateMr(any())).thenReturn(res1);

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto edit = Mockito.spy(ResultInSideDto.class);
    edit.setMessage("test");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(edit);

    maintenanceMngtBusiness.onEdit(mrDTO);
  }

  @Test
  public void saveMrHistory() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrInsideDTO previous = Mockito.spy(MrInsideDTO.class);

    MrInsideDTO mr = Mockito.spy(MrInsideDTO.class);
    mr.setState("4");

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(2L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.saveMrHistory(previous, mr, true);
  }

  @Test
  public void saveMrHistory1() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrInsideDTO previous = Mockito.spy(MrInsideDTO.class);

    MrInsideDTO mr = Mockito.spy(MrInsideDTO.class);
    mr.setState("3");

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(3L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.saveMrHistory(previous, mr, true);
  }

  @Test
  public void saveMrHistory1_1() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrInsideDTO previous = Mockito.spy(MrInsideDTO.class);

    MrInsideDTO mr = Mockito.spy(MrInsideDTO.class);
    mr.setState("3");

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(3L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.saveMrHistory(previous, mr, false);
  }

  @Test
  public void saveMrHistory2() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrInsideDTO previous = Mockito.spy(MrInsideDTO.class);

    MrInsideDTO mr = Mockito.spy(MrInsideDTO.class);
    mr.setState("1");

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(1L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.saveMrHistory(previous, mr, true);
  }

  @Test
  public void saveMrHistory3() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrInsideDTO previous = Mockito.spy(MrInsideDTO.class);
    previous.setState("1");

    MrInsideDTO mr = Mockito.spy(MrInsideDTO.class);
    mr.setState("6");

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(1L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.saveMrHistory(previous, mr, true);
  }

  @Test
  public void saveMrHistory4() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrInsideDTO previous = Mockito.spy(MrInsideDTO.class);
    previous.setState("2");

    MrInsideDTO mr = Mockito.spy(MrInsideDTO.class);
    mr.setState("6");

    UsersInsideDto dto = Mockito.spy(UsersInsideDto.class);
    dto.setUnitId(1L);
    when(userBusiness.getUserDetailById(any())).thenReturn(dto);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrHisServiceBusiness.insertMrHis(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.saveMrHistory(previous, mr, true);
  }


  @Test
  public void sendSMS() {
    PowerMockito.mockStatic(I18n.class);
    UserToken actor = Mockito.spy(UserToken.class);
    actor.setUserID(2L);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setAssignToPerson("2");
    mrDTO.setMrCode("2");

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    PowerMockito.when(I18n.getLanguage("mrMngt.message.add"))
        .thenReturn("Ban da tao mot cong viec bao duong");
    PowerMockito.when(I18n.getLanguage("mrMngt.list.mrCode")).thenReturn("MR code");
    PowerMockito.when(I18n.getLanguage("mrMngt.message.assign"))
        .thenReturn("Ban co mot yeu cau bao duong can thuc hien");

    UsersEntity assigToPerson = Mockito.spy(UsersEntity.class);
    when(userBusiness.getUserByUserId(anyLong())).thenReturn(assigToPerson);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveSearchDTO.setUnitId("2");
    List<MrApproveSearchDTO> lstMrApp = Mockito.spy(ArrayList.class);
    lstMrApp.add(mrApproveSearchDTO);
    when(maintenanceMngtRepository.getLstMrApproveDeptByUser(any())).thenReturn(lstMrApp);

    UnitDTO unitAprroveLv1 = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitAprroveLv1);

    maintenanceMngtBusiness.sendSMS(true, actor, mrDTO);
  }

  @Test
  public void sendSMS1() {
    PowerMockito.mockStatic(I18n.class);
    UserToken actor = Mockito.spy(UserToken.class);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setCreatePersonId(2L);
    mrDTO.setState("OPEN");

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    MrApproveRolesDTO mrApproveRolesDTO = Mockito.spy(MrApproveRolesDTO.class);
    List<MrApproveRolesDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(mrApproveRolesDTO);
    when(mrApprovalDepartmentBusiness.getLstMrApproveUserByRole(any())).thenReturn(lstRole);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveSearchDTO.setMadtLevel("2");
    mrApproveSearchDTO.setStatus("0");
    mrApproveSearchDTO.setUnitId("2");
    List<MrApproveSearchDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrApproveSearchDTO);
    when(mrApprovalDepartmentServiceImpl.getLstMrApproveSearch(any())).thenReturn(lst);

    UsersInsideDto mrCreator = Mockito.spy(UsersInsideDto.class);
    mrCreator.setUnitId(2L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    when(usersEntity.toDTO()).thenReturn(mrCreator);
    when(userBusiness.getUserByUserId(any())).thenReturn(usersEntity);
    maintenanceMngtBusiness.sendSMS(false, actor, mrDTO);
  }

  @Test
  public void sendSMS2() {
    PowerMockito.mockStatic(I18n.class);
    UserToken actor = Mockito.spy(UserToken.class);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setCreatePersonId(2L);
    mrDTO.setState("OPEN");

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    MrApproveRolesDTO mrApproveRolesDTO = Mockito.spy(MrApproveRolesDTO.class);
    List<MrApproveRolesDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(mrApproveRolesDTO);
    when(mrApprovalDepartmentBusiness.getLstMrApproveUserByRole(any())).thenReturn(lstRole);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveSearchDTO.setMadtLevel("2");
    mrApproveSearchDTO.setStatus("1");
    mrApproveSearchDTO.setUnitId("2");
    List<MrApproveSearchDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrApproveSearchDTO);
    when(mrApprovalDepartmentServiceImpl.getLstMrApproveSearch(any())).thenReturn(lst);

    UsersInsideDto mrCreator = Mockito.spy(UsersInsideDto.class);
    mrCreator.setUnitId(4L);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    when(usersEntity.toDTO()).thenReturn(mrCreator);
    when(userBusiness.getUserByUserId(any())).thenReturn(usersEntity);
    maintenanceMngtBusiness.sendSMS(false, actor, mrDTO);
  }

  @Test
  public void sendSMS3() {
    PowerMockito.mockStatic(I18n.class);
    UserToken actor = Mockito.spy(UserToken.class);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setCreatePersonId(2L);
    mrDTO.setState("1");

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    maintenanceMngtBusiness.sendSMS(false, actor, mrDTO);
  }

  @Test
  public void sendSMS4() {
    PowerMockito.mockStatic(I18n.class);
    UserToken actor = Mockito.spy(UserToken.class);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setCreatePersonId(2L);
    mrDTO.setState("6");

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    maintenanceMngtBusiness.sendSMS(false, actor, mrDTO);
  }

  @Test
  public void sendSMS5() {
    PowerMockito.mockStatic(I18n.class);
    UserToken actor = Mockito.spy(UserToken.class);
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setCreatePersonId(2L);
    mrDTO.setState("7");

    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    when(unitBusiness.findUnitById(any())).thenReturn(unitExecute);

    maintenanceMngtBusiness.sendSMS(false, actor, mrDTO);
  }


  @Test
  public void setMessageExecute() {
    List<MessagesDTO> lstMsg = Mockito.spy(ArrayList.class);
    String content = "";
    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setCreatePersonId(2L);
    UserToken actor = Mockito.spy(UserToken.class);
    actor.setUserID(2L);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    when(userBusiness.getUserByUserId(any())).thenReturn(usersEntity);

    maintenanceMngtBusiness.setMessageExecute(lstMsg, content, mrDTO, actor, unitExecute);
  }

  @Test
  public void setMessageApprove() {
    List<MessagesDTO> lstMsg = Mockito.spy(ArrayList.class);
    String content = "";
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);

    MrApproveRolesDTO mard = Mockito.spy(MrApproveRolesDTO.class);
    mard.setUserId("2");
    List<MrApproveRolesDTO> lstTP = Mockito.spy(ArrayList.class);
    lstTP.add(mard);
    when(mrApprovalDepartmentServiceImpl.getLstMrApproveUserByRole(any())).thenReturn(lstTP);

    UsersEntity u = Mockito.spy(UsersEntity.class);
    when(userBusiness.getUserByUserId(any())).thenReturn(u);

    maintenanceMngtBusiness.setMessageApprove(lstMsg, content, unitExecute);
  }

  @Test
  public void isTP() {
  }

  @Test
  public void setMessagesExecuteDTO() {
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    unitExecute.setSmsGatewayId(2L);
    maintenanceMngtBusiness.setMessagesExecuteDTO("test", "098632", "us01", "dunglv", unitExecute);
  }

  @Test
  public void checkState() {
    maintenanceMngtBusiness.checkState("", "1");
  }

  @Test
  public void checkState0() {
    maintenanceMngtBusiness.checkState("", "OPEN");
  }

  @Test
  public void checkState1() {
    maintenanceMngtBusiness.checkState("", "2");
  }

  @Test
  public void checkState2() {
    maintenanceMngtBusiness.checkState("", "3");
  }

  @Test
  public void checkState3() {
    maintenanceMngtBusiness.checkState("", "4");
  }

  @Test
  public void checkState4() {
    maintenanceMngtBusiness.checkState("", "5");
  }

  @Test
  public void checkState5() {
    maintenanceMngtBusiness.checkState("", "6");
  }

  @Test
  public void createWOAuto() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Day");
    PowerMockito.when(I18n.getLanguage("mrMngt.year")).thenReturn("Day");

    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setEarliestTime(new Date());
    mrDto.setLastestTime(DateTimeUtils.convertStringToDate("06/06/2020 12:12:12"));
    mrDto.setInterval("Day");
    mrDto.setMrTechnichcal("TBM");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrWoTempDTO woTempDTO = Mockito.spy(MrWoTempDTO.class);
    woTempDTO.setStartTime("06/06/2020 12:12:12");
    woTempDTO.setEndTime("06/06/2020 12:12:12");
    List<MrWoTempDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woTempDTO);
    when(mrWoTempBusiness.getListMrWoTempDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);

    String a = "2";
    List<String> lstSeq = new ArrayList<>();
    lstSeq.add(a);
    when(woServiceProxy.getSequenseWoProxy(anyString(), anyInt())).thenReturn(lstSeq);

    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setMessage("SUCCESS");
    when(woServiceProxy.insertWoProxy(any())).thenReturn(resultWO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrSchedulePeriodicBusiness.insertMrSchedulePeriodic(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.createWOAuto(mrDto);
  }

  @Test
  public void createWOAuto1() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Day");
    PowerMockito.when(I18n.getLanguage("mrMngt.year")).thenReturn("Day");

    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setEarliestTime(new Date());
    mrDto.setLastestTime(DateTimeUtils.convertStringToDate("06/06/2020 12:12:12"));
    mrDto.setInterval("Week");
    mrDto.setMrTechnichcal("TBM");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrWoTempDTO woTempDTO = Mockito.spy(MrWoTempDTO.class);
    List<MrWoTempDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woTempDTO);
    when(mrWoTempBusiness.getListMrWoTempDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);

    String a = "2";
    List<String> lstSeq = new ArrayList<>();
    lstSeq.add(a);
    when(woServiceProxy.getSequenseWoProxy(anyString(), anyInt())).thenReturn(lstSeq);

    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setMessage("SUCCESS");
    when(woServiceProxy.insertWoProxy(any())).thenReturn(resultWO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrSchedulePeriodicBusiness.insertMrSchedulePeriodic(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.createWOAuto(mrDto);
  }

  @Test
  public void createWOAuto2() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Day");
    PowerMockito.when(I18n.getLanguage("mrMngt.year")).thenReturn("Day");

    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setEarliestTime(new Date());
    mrDto.setLastestTime(DateTimeUtils.convertStringToDate("06/06/2020 12:12:12"));
    mrDto.setInterval("Month");
    mrDto.setMrTechnichcal("TBM");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrWoTempDTO woTempDTO = Mockito.spy(MrWoTempDTO.class);
    List<MrWoTempDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woTempDTO);
    when(mrWoTempBusiness.getListMrWoTempDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);

    String a = "2";
    List<String> lstSeq = new ArrayList<>();
    lstSeq.add(a);
    when(woServiceProxy.getSequenseWoProxy(anyString(), anyInt())).thenReturn(lstSeq);

    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setMessage("SUCCESS");
    when(woServiceProxy.insertWoProxy(any())).thenReturn(resultWO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrSchedulePeriodicBusiness.insertMrSchedulePeriodic(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.createWOAuto(mrDto);
  }

  @Test
  public void createWOAuto3() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Day");
    PowerMockito.when(I18n.getLanguage("mrMngt.year")).thenReturn("Day");

    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setEarliestTime(new Date());
    mrDto.setLastestTime(DateTimeUtils.convertStringToDate("06/06/2020 12:12:12"));
    mrDto.setInterval("Year");
    mrDto.setMrTechnichcal("TBM");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrWoTempDTO woTempDTO = Mockito.spy(MrWoTempDTO.class);
    List<MrWoTempDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woTempDTO);
    when(mrWoTempBusiness.getListMrWoTempDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);

    String a = "2";
    List<String> lstSeq = new ArrayList<>();
    lstSeq.add(a);
    when(woServiceProxy.getSequenseWoProxy(anyString(), anyInt())).thenReturn(lstSeq);

    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setMessage("SUCCESS");
    when(woServiceProxy.insertWoProxy(any())).thenReturn(resultWO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrSchedulePeriodicBusiness.insertMrSchedulePeriodic(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.createWOAuto(mrDto);
  }

  @Test
  public void createWOAuto4() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Day");
    PowerMockito.when(I18n.getLanguage("mrMngt.year")).thenReturn("Day");

    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setEarliestTime(new Date());
    mrDto.setLastestTime(DateTimeUtils.convertStringToDate("06/06/2020 12:12:12"));
    mrDto.setInterval("Precious");
    mrDto.setMrTechnichcal("TBM");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrWoTempDTO woTempDTO = Mockito.spy(MrWoTempDTO.class);
    List<MrWoTempDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woTempDTO);
    when(mrWoTempBusiness.getListMrWoTempDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);

    String a = "2";
    List<String> lstSeq = new ArrayList<>();
    lstSeq.add(a);
    when(woServiceProxy.getSequenseWoProxy(anyString(), anyInt())).thenReturn(lstSeq);

    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setMessage("SUCCESS");
    when(woServiceProxy.insertWoProxy(any())).thenReturn(resultWO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrSchedulePeriodicBusiness.insertMrSchedulePeriodic(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.createWOAuto(mrDto);
  }

  @Test
  public void createWOAuto5() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Day");
    PowerMockito.when(I18n.getLanguage("mrMngt.year")).thenReturn("Day");

    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setEarliestTime(new Date());
    mrDto.setLastestTime(DateTimeUtils.convertStringToDate("06/06/2020 12:12:12"));
    mrDto.setInterval("Half a year");
    mrDto.setMrTechnichcal("TBM");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrWoTempDTO woTempDTO = Mockito.spy(MrWoTempDTO.class);
    List<MrWoTempDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(woTempDTO);
    when(mrWoTempBusiness.getListMrWoTempDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);

    String a = "2";
    List<String> lstSeq = new ArrayList<>();
    lstSeq.add(a);
    when(woServiceProxy.getSequenseWoProxy(anyString(), anyInt())).thenReturn(lstSeq);

    ResultDTO resultWO = Mockito.spy(ResultDTO.class);
    resultWO.setMessage("SUCCESS");
    when(woServiceProxy.insertWoProxy(any())).thenReturn(resultWO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setMessage("2");
    when(mrSchedulePeriodicBusiness.insertMrSchedulePeriodic(any())).thenReturn(resultInSideDto);

    maintenanceMngtBusiness.createWOAuto(mrDto);
  }


  @Test
  public void getNewDate() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrMngt.day")).thenReturn("Day");
    Date time = new Date();
    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setInterval("Day");
    maintenanceMngtBusiness.getNewDate(time, 9, mrDto);
  }

  @Test
  public void getNewDate1() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrMngt.week")).thenReturn("Week");
    Date time = new Date();
    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setInterval("Week");
    maintenanceMngtBusiness.getNewDate(time, 9, mrDto);
  }

  @Test
  public void getNewDate2() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrMngt.month")).thenReturn("Month");
    Date time = new Date();
    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setInterval("Month");
    maintenanceMngtBusiness.getNewDate(time, 9, mrDto);
  }

  @Test
  public void getNewDate3() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrMngt.year")).thenReturn("Year");
    Date time = new Date();
    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setInterval("Year");
    maintenanceMngtBusiness.getNewDate(time, 9, mrDto);
  }

  @Test
  public void getNewDate4() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrMngt.precious")).thenReturn("Precious");
    Date time = new Date();
    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setInterval("Precious");
    maintenanceMngtBusiness.getNewDate(time, 9, mrDto);
  }

  @Test
  public void getNewDate5() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrMngt.halfYear")).thenReturn("Half a year");
    Date time = new Date();
    MrInsideDTO mrDto = Mockito.spy(MrInsideDTO.class);
    mrDto.setInterval("Half a year");
    maintenanceMngtBusiness.getNewDate(time, 9, mrDto);
  }

  @Test
  public void initTabApprove() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrDTO mrDTO = Mockito.spy(MrDTO.class);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveSearchDTO.setStatus("1");
    List<MrApproveSearchDTO> lstTem = Mockito.spy(ArrayList.class);
    lstTem.add(mrApproveSearchDTO);
    when(mrApprovalDepartmentServiceImpl.getLstMrApproveSearch(any())).thenReturn(lstTem);

    maintenanceMngtBusiness.initTabApprove(mrDTO);
  }

  @Test
  public void initTabApprove1() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrDTO mrDTO = null;

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    mrApproveSearchDTO.setUnitId("2");
    List<MrApproveSearchDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrApproveSearchDTO);
    lst.add(mrApproveSearchDTO);
    when(maintenanceMngtRepository.getLstMrApproveDeptByUser(any())).thenReturn(lst);

    maintenanceMngtBusiness.initTabApprove(mrDTO);
  }

  @Test
  public void getListUserGroupBySystem() {
    UserGroupCategoryDTO lstCondition = Mockito.spy(UserGroupCategoryDTO.class);
    List<UserGroupCategoryDTO> lst = Mockito.spy(ArrayList.class);
    when(userGroupCategoryRepository.getListUserGroupBySystem(any())).thenReturn(lst);
    List<UserGroupCategoryDTO> result = maintenanceMngtBusiness
        .getListUserGroupBySystem(lstCondition);
    assertEquals(result.size(), lst.size());
  }

  @Test
  public void getListWorkLogCategoryDTO() {
    WorkLogCategoryInsideDTO workLogCategoryDTO = Mockito.spy(WorkLogCategoryInsideDTO.class);
    List<WorkLogCategoryInsideDTO> lst = Mockito.spy(ArrayList.class);
    when(workLogCategoryRepository
        .getListWorkLogCategoryDTO(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);
    maintenanceMngtBusiness.getListWorkLogCategoryDTO(workLogCategoryDTO);
  }

  @Test
  public void findById() {
    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    mrInsideDTO.setCreatePersonId(1L);
    when(maintenanceMngtRepository.findById(any())).thenReturn(mrInsideDTO);

    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    when(userRepository.getUserByUserId(any())).thenReturn(usersEntity);
    maintenanceMngtBusiness.findById(1L);
  }

  @Test
  public void isCheckEdit() {
    String userId = "2";
    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    mrInsideDTO.setMrId(2L);
    mrInsideDTO.setCreatePersonId(2L);
    maintenanceMngtBusiness.isCheckEdit(mrInsideDTO, userId);
  }

  @Test
  public void isCheckEdit1() {
    String userId = "1";
    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    mrInsideDTO.setMrId(2L);
    mrInsideDTO.setCreatePersonId(2L);

    MrApproveSearchDTO mrApproveSearchDTO = Mockito.spy(MrApproveSearchDTO.class);
    List<MrApproveSearchDTO> lstApp = Mockito.spy(ArrayList.class);
    lstApp.add(mrApproveSearchDTO);
    when(mrApprovalDepartmentBusiness.getLstMrApproveSearch(any())).thenReturn(lstApp);

    MrApproveRolesDTO mrApproveRolesDTO = Mockito.spy(MrApproveRolesDTO.class);
    List<MrApproveRolesDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(mrApproveRolesDTO);
    when(mrApprovalDepartmentBusiness.getLstMrApproveUserByRole(any())).thenReturn(lstRole);

    maintenanceMngtBusiness.isCheckEdit(mrInsideDTO, userId);
  }

  @Test
  public void insertWorkLog() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    WorkLogInsiteDTO workLogInsiteDTO = Mockito.spy(WorkLogInsiteDTO.class);
    workLogInsiteDTO.setUserGroupAction(12L);
    workLogInsiteDTO.setWlgObjectType(2L);
    workLogInsiteDTO.setWlgObjectId(2L);
    workLogInsiteDTO.setWlayId(79L);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setState("2");
    when(crServiceProxy.findCrByIdProxy(any())).thenReturn(crDTO);

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey("SUCCESS");
    when(workLogRepository.insertWorkLog(any())).thenReturn(result);

    ResponseBO responseBO = Mockito.spy(ResponseBO.class);
    when(nocProWS.updateCrToFinishImpact(any(), any())).thenReturn(responseBO);

    maintenanceMngtBusiness.insertWorkLog(workLogInsiteDTO);
  }

  @Test
  public void getListCdGroupByUser() {
    WoCdGroupTypeUserDTO woCdGroupTypeUserDTO = Mockito.spy(WoCdGroupTypeUserDTO.class);
    List<WoCdGroupInsideDTO> lst = Mockito.spy(ArrayList.class);
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lst);
    List<WoCdGroupInsideDTO> result = maintenanceMngtBusiness
        .getListCdGroupByUser(woCdGroupTypeUserDTO);
    assertEquals(lst.size(), result.size());

  }

  @Test
  public void validate() {
    WorkLogInsiteDTO workLogInsiteDTO = Mockito.spy(WorkLogInsiteDTO.class);
    maintenanceMngtBusiness.validate(workLogInsiteDTO);
  }

  @Test
  public void setMapStateName() {
    PowerMockito.mockStatic(I18n.class);
    maintenanceMngtBusiness.setMapStateName();
  }
}
