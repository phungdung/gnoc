
package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.maintenance.dto.MrUserCfgApprovedSmsBtsDTO;
import com.viettel.gnoc.mr.repository.MrDeviceBtsRepository;
import com.viettel.gnoc.mr.repository.MrScheduleBtsHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleBtsRepository;
import com.viettel.gnoc.mr.repository.MrUserCfgApprovedSmsBtsRepository;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.io.File;
import java.util.ArrayList;
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
@PrepareForTest({MrScheduleTelBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrScheduleBtsHisBusinessImplTest {

  @InjectMocks
  MrScheduleBtsHisBusinessImpl mrScheduleBtsHisBusiness;

  @Mock
  MrScheduleBtsHisRepository mrScheduleBtsHisRepository;

  @Mock
  MrScheduleBtsRepository mrScheduleBtsRepository;

  @Mock
  MrDeviceBtsRepository mrDeviceBtsRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  UserRepository userRepository;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  MrUserCfgApprovedSmsBtsRepository mrUserCfgApprovedSmsBtsRepository;

  @Test
  public void getListMrScheduleBtsHisDTO() {
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);
    mrScheduleBtsHisDTO.setStatusWoGL("1");
    Datatable datatable = Mockito.spy(Datatable.class);
    List<MrScheduleBtsHisDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrScheduleBtsHisDTO);
    datatable.setData(list);
    when(mrScheduleBtsHisRepository.getListMrScheduleBtsHisDTO(any())).thenReturn(datatable);
    Datatable result = mrScheduleBtsHisBusiness.getListMrScheduleBtsHisDTO(mrScheduleBtsHisDTO);
    assertEquals(datatable.getPages(), result.getPages());
  }

  @Test
  public void getListWoBts() {
    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    List<MrScheduleBtsHisDetailInsiteDTO> ltsDetail = Mockito.spy(ArrayList.class);
    ltsDetail.add(mrScheduleBtsHisDetailDTO);
    when(mrScheduleBtsHisRepository
        .getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(ltsDetail);
    List<MrScheduleBtsHisDetailInsiteDTO> resultRl = Mockito.spy(ArrayList.class);
    when(mrScheduleBtsHisRepository.getListWoBts(any())).thenReturn(resultRl);
    List<MrScheduleBtsHisDetailInsiteDTO> result = mrScheduleBtsHisBusiness
        .getListWoBts(mrScheduleBtsHisDetailDTO);
    assertEquals(resultRl.size(), result.size());
  }

  @Test
  public void getListWoCodeMrScheduleBtsHisDetail() {
    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    List<MrScheduleBtsHisDetailInsiteDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleBtsHisDetailDTO);
    when(mrScheduleBtsHisRepository.getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(lst);
    List<MrScheduleBtsHisDetailInsiteDTO> result = mrScheduleBtsHisBusiness
        .getListWoCodeMrScheduleBtsHisDetail(mrScheduleBtsHisDetailDTO);
    assertEquals(lst.size(), result.size());
  }


  @Test
  public void exportData() throws Exception {
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);
    List<MrScheduleBtsHisDTO> lst = Mockito.spy(ArrayList.class);
    mrScheduleBtsHisDTO.setDeviceType("MPD");
    mrScheduleBtsHisDTO.setStatus("0");
    mrScheduleBtsHisDTO.setStatusWoGoc("0");
    for (int i = 0; i < 2; i++) {
      lst.add(mrScheduleBtsHisDTO);
    }
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    PowerMockito.mockStatic(I18n.class);
    when(mrScheduleBtsHisRepository.getDataExport(any())).thenReturn(lst);
    mrScheduleBtsHisBusiness.exportData(mrScheduleBtsHisDTO);
  }

  @Test
  public void exportData1() throws Exception {
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);
    List<MrScheduleBtsHisDTO> lst = Mockito.spy(ArrayList.class);
    mrScheduleBtsHisDTO.setDeviceType("DH");
    mrScheduleBtsHisDTO.setStatus("1");
    mrScheduleBtsHisDTO.setStatusWoGoc("1");
    for (int i = 0; i < 2; i++) {
      lst.add(mrScheduleBtsHisDTO);
    }
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    File fileExportSuccess = new File("./tests_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    PowerMockito.mockStatic(I18n.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("1");
    catLocationDTO.setPreCodeStation("1");
    List<CatLocationDTO> locationDTOS = Mockito.spy(ArrayList.class);
    locationDTOS.add(catLocationDTO);
    when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(locationDTOS);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);
    when(mrScheduleBtsHisRepository.getDataExport(any())).thenReturn(lst);

    mrScheduleBtsHisBusiness.exportData(mrScheduleBtsHisDTO);
  }

  @Test
  public void reCreateWo() {
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<MrScheduleBtsHisDetailInsiteDTO> mrScheduleBtsHisDetailDTOList = Mockito
        .spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    when(mrScheduleBtsRepository
        .getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(mrScheduleBtsHisDetailDTOList);
    mrScheduleBtsHisBusiness.reCreateWo(mrScheduleBtsHisDTO);
  }

  @Test
  public void reCreateWo1() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);
    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    List<MrScheduleBtsHisDetailInsiteDTO> mrScheduleBtsHisDetailDTOList = Mockito
        .spy(ArrayList.class);
    mrScheduleBtsHisDetailDTOList.add(mrScheduleBtsHisDetailDTO);

    List<MrScheduleBtsHisDetailInsiteDTO> lstChecklist = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    MrUserCfgApprovedSmsBtsDTO userCfgApprovedSmsBtsDTO = Mockito
        .spy(MrUserCfgApprovedSmsBtsDTO.class);
    when(mrUserCfgApprovedSmsBtsRepository
        .getApproveLevelByUserLogin(userToken.getUserName())).thenReturn(userCfgApprovedSmsBtsDTO);
    when(mrScheduleBtsRepository
        .getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(mrScheduleBtsHisDetailDTOList);
    when(mrDeviceBtsRepository
        .getListWoBts(any())).thenReturn(lstChecklist);

    mrScheduleBtsHisBusiness.reCreateWo(mrScheduleBtsHisDTO);
  }

  @Test
  public void reCreateWo2() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);
    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    List<MrScheduleBtsHisDetailInsiteDTO> mrScheduleBtsHisDetailDTOList = Mockito
        .spy(ArrayList.class);
    mrScheduleBtsHisDetailDTOList.add(mrScheduleBtsHisDetailDTO);
    MrUserCfgApprovedSmsBtsDTO userCfgApprovedSmsBtsDTO = Mockito
        .spy(MrUserCfgApprovedSmsBtsDTO.class);
    when(mrUserCfgApprovedSmsBtsRepository
        .getApproveLevelByUserLogin(userToken.getUserName())).thenReturn(userCfgApprovedSmsBtsDTO);
    when(mrScheduleBtsRepository
        .getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(mrScheduleBtsHisDetailDTOList);
    when(mrDeviceBtsRepository
        .getListWoBts(any())).thenReturn(mrScheduleBtsHisDetailDTOList);
    mrScheduleBtsHisBusiness.reCreateWo(mrScheduleBtsHisDTO);
  }

  @Test
  public void reCreateWo3() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);
    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    mrScheduleBtsHisDetailDTO.setTaskApprove(null);
    MrUserCfgApprovedSmsBtsDTO userCfgApprovedSmsBtsDTO = Mockito
        .spy(MrUserCfgApprovedSmsBtsDTO.class);
    when(mrUserCfgApprovedSmsBtsRepository
        .getApproveLevelByUserLogin(userToken.getUserName())).thenReturn(userCfgApprovedSmsBtsDTO);
    List<MrScheduleBtsHisDetailInsiteDTO> lst = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2; i++) {
      lst.add(mrScheduleBtsHisDetailDTO);
    }
    when(mrScheduleBtsRepository
        .getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(lst);
    when(mrDeviceBtsRepository
        .getListWoBts(any())).thenReturn(lst);
    mrScheduleBtsHisBusiness.reCreateWo(mrScheduleBtsHisDTO);
  }

  @Test
  public void reCreateWo4() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);
    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    mrScheduleBtsHisDetailDTO.setTaskApprove(0L);
    MrUserCfgApprovedSmsBtsDTO userCfgApprovedSmsBtsDTO = Mockito
        .spy(MrUserCfgApprovedSmsBtsDTO.class);
    when(mrUserCfgApprovedSmsBtsRepository
        .getApproveLevelByUserLogin(userToken.getUserName())).thenReturn(userCfgApprovedSmsBtsDTO);
    List<MrScheduleBtsHisDetailInsiteDTO> lst = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2; i++) {
      lst.add(mrScheduleBtsHisDetailDTO);
    }
    when(mrScheduleBtsRepository
        .getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(lst);
    when(mrDeviceBtsRepository
        .getListWoBts(any())).thenReturn(lst);
    mrScheduleBtsHisBusiness.reCreateWo(mrScheduleBtsHisDTO);
  }

  @Test
  public void reCreateWo5() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(I18n.class);
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);
    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    mrScheduleBtsHisDetailDTO.setTaskApprove(1L);
    MrUserCfgApprovedSmsBtsDTO userCfgApprovedSmsBtsDTO = Mockito
        .spy(MrUserCfgApprovedSmsBtsDTO.class);
    when(mrUserCfgApprovedSmsBtsRepository
        .getApproveLevelByUserLogin(userToken.getUserName())).thenReturn(userCfgApprovedSmsBtsDTO);
    List<MrScheduleBtsHisDetailInsiteDTO> lst = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 3; i++) {
      lst.add(mrScheduleBtsHisDetailDTO);
    }

    when(mrScheduleBtsRepository
        .getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(lst);
    when(mrDeviceBtsRepository
        .getListWoBts(any())).thenReturn(lst);
    mrScheduleBtsHisBusiness.reCreateWo(mrScheduleBtsHisDTO);
  }

  @Test
  public void reCreateWoConfirm() {
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);

    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    mrScheduleBtsHisDetailDTO.setWoCode("1_2");
    List<MrScheduleBtsHisDetailInsiteDTO> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(mrScheduleBtsHisDetailDTO);

    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTO woDTO = null;
    WoInsideDTO woInside = Mockito.spy(WoInsideDTO.class);

    when(woInside.toModelOutSide()).thenReturn(woDTO);
    when(mrScheduleBtsRepository.getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(lstWo);
    when(woServiceProxy.findWoByIdProxy(any())).thenReturn(woInside);
    when(mrUserCfgApprovedSmsBtsRepository.getApproveLevelByUserLogin(anyString()))
        .thenReturn(new MrUserCfgApprovedSmsBtsDTO());
    mrScheduleBtsHisBusiness.reCreateWoConfirm(mrScheduleBtsHisDTO);

  }

  @Test
  public void reCreateWoConfirm1() {
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);

    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    mrScheduleBtsHisDetailDTO.setWoCode("1_3");
    List<MrScheduleBtsHisDetailInsiteDTO> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(mrScheduleBtsHisDetailDTO);

    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setFtId("2");
    WoInsideDTO woInside = Mockito.spy(WoInsideDTO.class);

    ResultDTO resultWo = Mockito.spy(ResultDTO.class);
    resultWo.setId("2");
    resultWo.setMessage("SUCCESS");

    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO1 = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    mrScheduleBtsHisDetailDTO1.setWoCode("1_2");
    List<MrScheduleBtsHisDetailInsiteDTO> lstChecklistFail = Mockito.spy(ArrayList.class);
    lstChecklistFail.add(mrScheduleBtsHisDetailDTO1);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("FALSE");

    when(woInside.toModelOutSide()).thenReturn(woDTO);
    when(mrScheduleBtsRepository.getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(lstWo);
    when(woServiceProxy.findWoByIdProxy(any())).thenReturn(woInside);
    when(woServiceProxy.createWoProxy(any())).thenReturn(resultWo);
    when(mrScheduleBtsHisDTO.getMrScheduleBtsHisDetailDTOList()).thenReturn(lstChecklistFail);
    when(mrScheduleBtsHisRepository.insertHisDetail(any())).thenReturn(resultInSideDto);
    when(mrUserCfgApprovedSmsBtsRepository.getApproveLevelByUserLogin(anyString()))
        .thenReturn(new MrUserCfgApprovedSmsBtsDTO());
    mrScheduleBtsHisBusiness.reCreateWoConfirm(mrScheduleBtsHisDTO);
  }

  @Test
  public void reCreateWoConfirm2() {
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);

    PowerMockito.mockStatic(I18n.class);
    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    mrScheduleBtsHisDetailDTO.setWoCode("1_3");
    List<MrScheduleBtsHisDetailInsiteDTO> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(mrScheduleBtsHisDetailDTO);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setFtId(null);
    WoInsideDTO woInside = Mockito.spy(WoInsideDTO.class);

    ResultDTO resultWo = Mockito.spy(ResultDTO.class);
    resultWo.setId("2");
    resultWo.setMessage("SUCCESS");

    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO1 = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    mrScheduleBtsHisDetailDTO1.setWoCode("1_2");
    List<MrScheduleBtsHisDetailInsiteDTO> lstChecklistFail = Mockito.spy(ArrayList.class);
    lstChecklistFail.add(mrScheduleBtsHisDetailDTO1);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    String res = "FALSE";

    when(woInside.toModelOutSide()).thenReturn(woDTO);
    when(mrScheduleBtsRepository.getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(lstWo);
    when(woServiceProxy.findWoByIdProxy(any())).thenReturn(woInside);
    when(woServiceProxy.createWoProxy(any())).thenReturn(resultWo);
    when(mrScheduleBtsHisDTO.getMrScheduleBtsHisDetailDTOList()).thenReturn(lstChecklistFail);
    when(mrScheduleBtsHisRepository.insertHisDetail(any())).thenReturn(resultInSideDto);
    when(
        mrScheduleBtsHisRepository.updateMrScheduleBtsHis(any(), any(), any(), any(), any(), any()))
        .thenReturn(res);
    when(mrUserCfgApprovedSmsBtsRepository.getApproveLevelByUserLogin(anyString()))
        .thenReturn(new MrUserCfgApprovedSmsBtsDTO());
    mrScheduleBtsHisBusiness.reCreateWoConfirm(mrScheduleBtsHisDTO);
  }

  @Test
  public void reCreateWoConfirm3() {
    MrScheduleBtsHisDTO mrScheduleBtsHisDTO = Mockito.spy(MrScheduleBtsHisDTO.class);

    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    mrScheduleBtsHisDetailDTO.setWoCode("1_3");
    List<MrScheduleBtsHisDetailInsiteDTO> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(mrScheduleBtsHisDetailDTO);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    woDTO.setFtId(null);
    WoInsideDTO woInside = Mockito.spy(WoInsideDTO.class);

    ResultDTO resultWo = Mockito.spy(ResultDTO.class);
    resultWo.setId("2");
    resultWo.setMessage("FALSE");

    when(woInside.toModelOutSide()).thenReturn(woDTO);
    when(mrScheduleBtsRepository.getListWoCodeMrScheduleBtsHisDetail(any())).thenReturn(lstWo);
    when(woServiceProxy.findWoByIdProxy(any())).thenReturn(woInside);
    when(woServiceProxy.createWoProxy(any())).thenReturn(resultWo);
    when(mrUserCfgApprovedSmsBtsRepository.getApproveLevelByUserLogin(anyString()))
        .thenReturn(new MrUserCfgApprovedSmsBtsDTO());
    mrScheduleBtsHisBusiness.reCreateWoConfirm(mrScheduleBtsHisDTO);

  }
}

