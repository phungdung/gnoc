package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgCrUnitTelDTO;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.repository.MrCfgCrUnitTelRepository;
import com.viettel.gnoc.mr.repository.MrCfgMarketRepository;
import com.viettel.gnoc.mr.repository.MrConfigRepository;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrDeviceSoftRepository;
import com.viettel.gnoc.mr.repository.MrHisRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MaintenanceMngtBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrDeviceSoftBusinessImplTest {

  @InjectMocks
  MrDeviceSoftBusinessImpl mrDeviceSoftBusiness;

  @Mock
  MrDeviceSoftRepository mrDeviceSoftRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitRepository unitRepository;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  MrConfigRepository mrConfigRepository;

  @Mock
  MrCfgCrUnitTelRepository mrCfgCrUnitTelRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  MrScheduleTelRepository mrScheduleTelRepository;

  @Mock
  MrScheduleTelHisRepository mrScheduleTelHisRepository;

  @Mock
  MrRepository mrRepository;

  @Mock
  MrHisRepository mrHisRepository;

  @Mock
  MrCfgMarketRepository mrCfgMarketRepository;

  @Mock
  MrDeviceRepository mrDeviceRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrDeviceSoftBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void getListDataMrDeviceSoftSearchWeb() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setApproveStatusSoft(0L);
    mrDeviceDTO.setBoUnitSoft(2L);
    List<MrDeviceDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrDeviceDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lst);
    when(mrDeviceSoftRepository.getListDataMrDeviceSoftSearchWeb(any())).thenReturn(datatable);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    unitDTO.setUnitId(2L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(2L);
    List<WoCdGroupInsideDTO> listWoCdGroup = Mockito.spy(ArrayList.class);
    listWoCdGroup.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUserProxy(any())).thenReturn(listWoCdGroup);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(2L);
    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
    listMarket.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMarket);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("2");
    List<MrConfigDTO> listMrConfig = Mockito.spy(ArrayList.class);
    listMrConfig.add(mrConfigDTO);
    when(mrConfigRepository.getConfigByGroup(anyString())).thenReturn(listMrConfig);

    List<MrCfgCrUnitTelDTO> listMrCfgCrUnitTel = Mockito.spy(ArrayList.class);
    when(mrCfgCrUnitTelRepository.getListMrCfgCrUnitTel(any())).thenReturn(listMrCfgCrUnitTel);

    String unitApprove = "1";
    when(userRepository.getUnitParentForApprove(anyString(), any())).thenReturn(unitApprove);

    when(unitRepository.findUnitById(any())).thenReturn(unitDTO);

    when(userRepository.checkRoleOfUser("TP", userToken.getUserID())).thenReturn(true);

    Datatable result = mrDeviceSoftBusiness.getListDataMrDeviceSoftSearchWeb(mrDeviceDTO);
    assertEquals(datatable.getPages(), result.getPages());

  }

  @Test
  public void findMrDeviceSoftWeb() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceSoftRepository.findMrDeviceSoftWeb(any())).thenReturn(mrDeviceDTO);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(2L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(2L);
    List<WoCdGroupInsideDTO> listWoCdGroup = Mockito.spy(ArrayList.class);
    listWoCdGroup.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUserProxy(any())).thenReturn(listWoCdGroup);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(2L);
    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
    listMarket.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMarket);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("2");
    List<MrConfigDTO> listMrConfig = Mockito.spy(ArrayList.class);
    listMrConfig.add(mrConfigDTO);
    when(mrConfigRepository.getConfigByGroup(anyString())).thenReturn(listMrConfig);

    List<MrCfgCrUnitTelDTO> listMrCfgCrUnitTel = Mockito.spy(ArrayList.class);
    when(mrCfgCrUnitTelRepository.getListMrCfgCrUnitTel(any())).thenReturn(listMrCfgCrUnitTel);

    mrDeviceSoftBusiness.findMrDeviceSoftWeb(2L);
  }

  @Test
  public void updateMrDeviceSoft() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setApproveStatusSoft(0L);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrDeviceDTO mrDeviceDTOOld = Mockito.spy(MrDeviceDTO.class);

    when(mrDeviceSoftRepository.findMrDeviceById(mrDeviceDTO.getDeviceId()))
        .thenReturn(mrDeviceDTOOld);

    mrDeviceSoftBusiness.updateMrDeviceSoft(mrDeviceDTO);
  }

  @Test
  public void doUpdate() {
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setBoUnitSoft(2L);
    mrDeviceDTO.setUserIdLogin(2L);
    mrDeviceDTO.setUnitIdLogin(1L);
    mrDeviceDTO.setMrSoft("1");
    mrDeviceDTO.setCheckTP(0L);

    MrDeviceDTO mrDeviceDTOOld = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTOOld.setGroupCode("2");
    mrDeviceDTOOld.setMrSoft("0");

    String unitApprove = "1";
    when(userRepository.getUnitParentForApprove(anyString(), any())).thenReturn(unitApprove);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    when(unitRepository.findUnitById(any())).thenReturn(unitDTO);

    when(userRepository.checkRoleOfUser("TP", mrDeviceDTO.getUserIdLogin())).thenReturn(false);

    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    mrScheduleTelHisDTO.setCrId("1");
    mrScheduleTelHisDTO.setMrId("1");
    List<MrScheduleTelHisDTO> listMrScheduleTelHisExist = Mockito.spy(ArrayList.class);
    listMrScheduleTelHisExist.add(mrScheduleTelHisDTO);
    when(mrScheduleTelRepository.getListScheduleMoveToHis(any()))
        .thenReturn(listMrScheduleTelHisExist);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrScheduleTelHisRepository.insertUpdateListScheduleHis(any())).thenReturn(resultInSideDto);

    mrDeviceSoftBusiness.doUpdate(mrDeviceDTO, mrDeviceDTOOld);
  }

  @Test
  public void doUpdate1() {
    PowerMockito.mockStatic(I18n.class);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setBoUnitSoft(2L);
    mrDeviceDTO.setUserIdLogin(2L);
    mrDeviceDTO.setUnitIdLogin(1L);
    mrDeviceDTO.setMrSoft("1");
    mrDeviceDTO.setCheckTP(0L);

    MrDeviceDTO mrDeviceDTOOld = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTOOld.setGroupCode("2");
    mrDeviceDTOOld.setMrSoft("0");

    String unitApprove = "1";
    when(userRepository.getUnitParentForApprove(anyString(), any())).thenReturn(unitApprove);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    when(unitRepository.findUnitById(any())).thenReturn(unitDTO);

    when(userRepository.checkRoleOfUser("TP", mrDeviceDTO.getUserIdLogin())).thenReturn(true);

    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    mrScheduleTelHisDTO.setCrId("1");
    mrScheduleTelHisDTO.setMrId("1");
    List<MrScheduleTelHisDTO> listMrScheduleTelHisExist = Mockito.spy(ArrayList.class);
    listMrScheduleTelHisExist.add(mrScheduleTelHisDTO);
    when(mrScheduleTelRepository.getListScheduleMoveToHis(any()))
        .thenReturn(listMrScheduleTelHisExist);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrScheduleTelHisRepository.insertUpdateListScheduleHis(any())).thenReturn(resultInSideDto);
    when(mrScheduleTelRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);
    when(mrRepository.deleteMr(any())).thenReturn(resultInSideDto);
    when(mrHisRepository.insertMrHis(any())).thenReturn(resultInSideDto);

    mrDeviceSoftBusiness.doUpdate(mrDeviceDTO, mrDeviceDTOOld);
  }

  @Test
  public void doUpdate2() {
    PowerMockito.mockStatic(I18n.class);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setBoUnitSoft(2L);
    mrDeviceDTO.setUserIdLogin(2L);
    mrDeviceDTO.setUnitIdLogin(1L);
    mrDeviceDTO.setMrSoft("0");
    mrDeviceDTO.setCheckTP(0L);

    MrDeviceDTO mrDeviceDTOOld = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTOOld.setGroupCode("2");
    mrDeviceDTOOld.setMrSoft("1");

    String unitApprove = "1";
    when(userRepository.getUnitParentForApprove(anyString(), any())).thenReturn(unitApprove);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    when(unitRepository.findUnitById(any())).thenReturn(unitDTO);

    when(userRepository.checkRoleOfUser("TP", mrDeviceDTO.getUserIdLogin())).thenReturn(true);

    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    mrScheduleTelHisDTO.setCrId("1");
    mrScheduleTelHisDTO.setMrId("1");
    List<MrScheduleTelHisDTO> listMrScheduleTelHisExist = Mockito.spy(ArrayList.class);
    listMrScheduleTelHisExist.add(mrScheduleTelHisDTO);
    when(mrScheduleTelRepository.getListScheduleMoveToHis(any()))
        .thenReturn(listMrScheduleTelHisExist);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrScheduleTelHisRepository.insertUpdateListScheduleHis(any())).thenReturn(resultInSideDto);
    when(mrScheduleTelRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);
    when(mrRepository.deleteMr(any())).thenReturn(resultInSideDto);
    when(mrHisRepository.insertMrHis(any())).thenReturn(resultInSideDto);

    mrDeviceSoftBusiness.doUpdate(mrDeviceDTO, mrDeviceDTOOld);
  }

  @Test
  public void doUpdate4() {
    PowerMockito.mockStatic(I18n.class);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setBoUnitSoft(2L);
    mrDeviceDTO.setUserIdLogin(2L);
    mrDeviceDTO.setUnitIdLogin(1L);
    mrDeviceDTO.setMrSoft("0");
    mrDeviceDTO.setCheckTP(0L);

    MrDeviceDTO mrDeviceDTOOld = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTOOld.setGroupCode("2");
    mrDeviceDTOOld.setMrSoft("1");

    String unitApprove = "1";
    when(userRepository.getUnitParentForApprove(anyString(), any())).thenReturn(unitApprove);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    when(unitRepository.findUnitById(any())).thenReturn(unitDTO);

    when(userRepository.checkRoleOfUser("TP", mrDeviceDTO.getUserIdLogin())).thenReturn(false);

    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    mrScheduleTelHisDTO.setCrId("1");
    mrScheduleTelHisDTO.setMrId("1");
    List<MrScheduleTelHisDTO> listMrScheduleTelHisExist = Mockito.spy(ArrayList.class);
    listMrScheduleTelHisExist.add(mrScheduleTelHisDTO);
    when(mrScheduleTelRepository.getListScheduleMoveToHis(any()))
        .thenReturn(listMrScheduleTelHisExist);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrScheduleTelHisRepository.insertUpdateListScheduleHis(any())).thenReturn(resultInSideDto);
    when(mrScheduleTelRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);
    when(mrRepository.deleteMr(any())).thenReturn(resultInSideDto);
    when(mrHisRepository.insertMrHis(any())).thenReturn(resultInSideDto);

    mrDeviceSoftBusiness.doUpdate(mrDeviceDTO, mrDeviceDTOOld);
  }

  @Test
  public void doUpdate5() {
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setBoUnitSoft(2L);
    mrDeviceDTO.setUserIdLogin(2L);
    mrDeviceDTO.setUnitIdLogin(1L);
    mrDeviceDTO.setMrSoft("1");
    mrDeviceDTO.setCheckTP(0L);

    MrDeviceDTO mrDeviceDTOOld = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTOOld.setMrSoft("0");

    String unitApprove = "1";
    when(userRepository.getUnitParentForApprove(anyString(), any())).thenReturn(unitApprove);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    when(unitRepository.findUnitById(any())).thenReturn(unitDTO);

    when(userRepository.checkRoleOfUser("TP", mrDeviceDTO.getUserIdLogin())).thenReturn(false);

    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    mrScheduleTelHisDTO.setCrId("1");
    mrScheduleTelHisDTO.setMrId("1");
    List<MrScheduleTelHisDTO> listMrScheduleTelHisExist = Mockito.spy(ArrayList.class);
    listMrScheduleTelHisExist.add(mrScheduleTelHisDTO);
    when(mrScheduleTelRepository.getListScheduleMoveToHis(any()))
        .thenReturn(listMrScheduleTelHisExist);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrScheduleTelHisRepository.insertUpdateListScheduleHis(any())).thenReturn(resultInSideDto);

    mrDeviceSoftBusiness.doUpdate(mrDeviceDTO, mrDeviceDTOOld);
  }


  @Test
  public void deleteMrDeviceSoft() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);

    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    List<MrScheduleTelHisDTO> listScheduleHis = Mockito.spy(ArrayList.class);
    listScheduleHis.add(mrScheduleTelHisDTO);
    when(mrScheduleTelRepository.getListScheduleMoveToHis(any())).thenReturn(listScheduleHis);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrScheduleTelHisRepository.insertUpdateListScheduleHis(any())).thenReturn(resultInSideDto);
    when(mrScheduleTelRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);
    when(mrDeviceSoftRepository.deleteMrDevice(any())).thenReturn(resultInSideDto);

    mrDeviceSoftBusiness.deleteMrDeviceSoft(mrDeviceDTO);
  }

  @Test
  public void getListMaintainStatusCombobox() {
    List<MrConfigDTO> lst = Mockito.spy(ArrayList.class);
    when(mrConfigRepository.getConfigByGroup(anyString())).thenReturn(lst);
    List<MrConfigDTO> result = mrDeviceSoftBusiness.getListMaintainStatusCombobox();
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void exportDataMrDeviceSoft() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTOS = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTOS.setMrSoft("1");
    List<MrDeviceDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrDeviceDTOS);
    when(mrDeviceSoftRepository.getListMrDeviceSoftExport(any())).thenReturn(list);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    unitDTO.setUnitId(2L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(2L);
    List<WoCdGroupInsideDTO> listWoCdGroup = Mockito.spy(ArrayList.class);
    listWoCdGroup.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUserProxy(any())).thenReturn(listWoCdGroup);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(2L);
    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
    listMarket.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMarket);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("2");
    List<MrConfigDTO> listMrConfig = Mockito.spy(ArrayList.class);
    listMrConfig.add(mrConfigDTO);
    when(mrConfigRepository.getConfigByGroup(anyString())).thenReturn(listMrConfig);

    List<MrCfgCrUnitTelDTO> listMrCfgCrUnitTel = Mockito.spy(ArrayList.class);
    when(mrCfgCrUnitTelRepository.getListMrCfgCrUnitTel(any())).thenReturn(listMrCfgCrUnitTel);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceSoftBusiness.exportDataMrDeviceSoft(mrDeviceDTO);
  }

  @Test
  public void getListWoCdGroupCombobox() {
    List<WoCdGroupInsideDTO> lst = Mockito.spy(ArrayList.class);
    when(woCategoryServiceProxy.getListCdGroupByUserProxy(any())).thenReturn(lst);
    List<WoCdGroupInsideDTO> result = mrDeviceSoftBusiness.getListWoCdGroupCombobox();
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void getListConfirmSoftCombobox() {
    List<MrConfigDTO> lst = Mockito.spy(ArrayList.class);
    when(mrConfigRepository.getConfigByGroup(anyString())).thenReturn(lst);
    List<MrConfigDTO> result = mrDeviceSoftBusiness.getListConfirmSoftCombobox();
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void getListMrCfgMarket() {
    List<MrCfgMarketDTO> lst = Mockito.spy(ArrayList.class);
    when(mrCfgMarketRepository.getListCfgMarket(null)).thenReturn(lst);
    List<MrCfgMarketDTO> result = mrDeviceSoftBusiness.getListMrCfgMarket();
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void updateListMrCfgMarket() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrCfgMarketDTO mrCfgMarketDTOlst = Mockito.spy(MrCfgMarketDTO.class);
    mrCfgMarketDTOlst.setCreatedUserSoft(null);
    List<MrCfgMarketDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrCfgMarketDTOlst);

    MrCfgMarketDTO marketOld = Mockito.spy(MrCfgMarketDTO.class);
    marketOld.setCreatedUserSoft(3L);
    when(mrCfgMarketRepository.findMrCfgMarketById(any())).thenReturn(marketOld);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrCfgMarketRepository.add(any())).thenReturn(resultInSideDto);
    when(mrDeviceSoftRepository.updateCreateUserSoftByMarket(any(), any()))
        .thenReturn(resultInSideDto);

    MrCfgMarketDTO mrCfgMarketDTO = Mockito.spy(MrCfgMarketDTO.class);
    mrCfgMarketDTO.setMrCfgMarketDTOList(lst);
    mrDeviceSoftBusiness.updateListMrCfgMarket(mrCfgMarketDTO);
  }

  @Test
  public void updateListMrCfgMarket1() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrCfgMarketDTO mrCfgMarketDTOlst = Mockito.spy(MrCfgMarketDTO.class);
    mrCfgMarketDTOlst.setCreatedUserSoft(2L);
    List<MrCfgMarketDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrCfgMarketDTOlst);

    MrCfgMarketDTO marketOld = Mockito.spy(MrCfgMarketDTO.class);
    when(mrCfgMarketRepository.findMrCfgMarketById(any())).thenReturn(marketOld);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrCfgMarketRepository.add(any())).thenReturn(resultInSideDto);
    when(mrDeviceSoftRepository.updateCreateUserSoftByMarket(any(), any()))
        .thenReturn(resultInSideDto);

    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    when(userRepository.getUserByUserId(any())).thenReturn(usersEntity);

    MrCfgMarketDTO mrCfgMarketDTO = Mockito.spy(MrCfgMarketDTO.class);
    mrCfgMarketDTO.setMrCfgMarketDTOList(lst);
    mrDeviceSoftBusiness.updateListMrCfgMarket(mrCfgMarketDTO);
  }

  @Test
  public void approveMrDeviceSoft() {
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);

    MrDeviceDTO mrDeviceOld = null;
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);

    mrDeviceSoftBusiness.approveMrDeviceSoft(mrDeviceDTO);
  }

  @Test
  public void approveMrDeviceSoft1() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setApproveStatusSoft(1L);

    MrDeviceDTO mrDeviceOld = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);

    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    mrScheduleTelHisDTO.setMrId("1");
    List<MrScheduleTelHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleTelHisDTO);
    when(mrScheduleTelRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrScheduleTelHisRepository.insertUpdateListScheduleHis(any())).thenReturn(resultInSideDto);
    when(mrScheduleTelRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);
    when(mrRepository.deleteMr(any())).thenReturn(resultInSideDto);
    when(mrHisRepository.insertMrHis(any())).thenReturn(resultInSideDto);

    mrDeviceSoftBusiness.approveMrDeviceSoft(mrDeviceDTO);
  }

  @Test
  public void importDataMrDeviceSoft() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MultipartFile multipartFile = null;
    mrDeviceSoftBusiness.importDataMrDeviceSoft(multipartFile);
  }

  @Test
  public void importDataMrDeviceSoft1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String filePath = "/temps";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);

    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.STT"))
        .thenReturn("mrDeviceSoft.STT");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceId"))
        .thenReturn("mrDeviceSoft.deviceId");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.marketName"))
        .thenReturn("mrDeviceSoft.marketName");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.regionSoft"))
        .thenReturn("mrDeviceSoft.regionSoft");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.arrayCode"))
        .thenReturn("mrDeviceSoft.arrayCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.networkType"))
        .thenReturn("mrDeviceSoft.networkType");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceType"))
        .thenReturn("mrDeviceSoft.deviceType");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.nodeIp"))
        .thenReturn("mrDeviceSoft.nodeIp");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.nodeCode"))
        .thenReturn("mrDeviceSoft.nodeCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceName"))
        .thenReturn("mrDeviceSoft.deviceName");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.createUserSoft"))
        .thenReturn("mrDeviceSoft.createUserSoft");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.comments"))
        .thenReturn("mrDeviceSoft.comments");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrSoft"))
        .thenReturn("mrDeviceSoft.mrSoft");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrConfirmSoftDisplay"))
        .thenReturn("mrDeviceSoft.mrConfirmSoftDisplay");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.impactNode"))
        .thenReturn("mrDeviceSoft.impactNode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.numberOfCr"))
        .thenReturn("mrDeviceSoft.numberOfCr");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.boUnitSoftId"))
        .thenReturn("mrDeviceSoft.boUnitSoftId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.lastDateStr"))
        .thenReturn("MrScheduleTel.lastDateStr");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.cdIdName"))
        .thenReturn("mrDeviceSoft.cdIdName");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.groupCode"))
        .thenReturn("mrDeviceSoft.groupCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.vendor"))
        .thenReturn("mrDeviceSoft.vendor");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.stationCode"))
        .thenReturn("mrDeviceSoft.stationCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.status"))
        .thenReturn("mrDeviceSoft.status");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.dateIntegratedStr"))
        .thenReturn("mrDeviceSoft.dateIntegratedStr");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrTypeStr"))
        .thenReturn("mrDeviceSoft.mrTypeStr");
    //      </editor-fold>

    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("mrDeviceSoft.STT"),
        I18n.getLanguage("mrDeviceSoft.deviceId") + "\n" + I18n
            .getLanguage("mrDeviceSoft.deviceId.noti"),
        I18n.getLanguage("mrDeviceSoft.marketName") + " (*)",
        I18n.getLanguage("mrDeviceSoft.regionSoft") + "\n" + I18n
            .getLanguage("mrDeviceSoft.deviceId.required"),
        I18n.getLanguage("mrDeviceSoft.arrayCode") + " (*)",
        I18n.getLanguage("mrDeviceSoft.networkType") + " (*)",
        I18n.getLanguage("mrDeviceSoft.deviceType") + " (*)",
        I18n.getLanguage("mrDeviceSoft.nodeIp"),
        I18n.getLanguage("mrDeviceSoft.nodeCode") + " (*)",
        I18n.getLanguage("mrDeviceSoft.deviceName") + " (*)",
        I18n.getLanguage("mrDeviceSoft.createUserSoft") + " (*)",
        I18n.getLanguage("mrDeviceSoft.comments"),
        I18n.getLanguage("mrDeviceSoft.mrSoft") + " (*)",
        I18n.getLanguage("mrDeviceSoft.mrConfirmSoftDisplay") + "\n" + I18n
            .getLanguage("mrDeviceSoft.mrSoft.required.no"),
        I18n.getLanguage("mrDeviceSoft.impactNode") + "\n" + I18n
            .getLanguage("mrDeviceSoft.mrSoft.required.yes"),
        I18n.getLanguage("mrDeviceSoft.numberOfCr") + "\n" + I18n
            .getLanguage("mrDeviceSoft.mrSoft.required.yes"),
        I18n.getLanguage("mrDeviceSoft.boUnitSoftId") + "\n" + I18n
            .getLanguage("mrDeviceSoft.deviceId.required"),
        I18n.getLanguage("mrDeviceSoft.cdIdName") + "\n" + I18n
            .getLanguage("mrDeviceSoft.mrSoft.required.yes"),
        I18n.getLanguage("mrDeviceSoft.groupCode"),
        I18n.getLanguage("mrDeviceSoft.vendor") + " (*)",
        I18n.getLanguage("mrDeviceSoft.stationCode"),
        I18n.getLanguage("mrDeviceSoft.status") + " (*)",
        I18n.getLanguage("mrDeviceSoft.dateIntegratedStr"),
        I18n.getLanguage("mrDeviceSoft.mrTypeStr") + " (*)",
    };
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                23,
                1000
            ))
        .thenReturn(dataHeader);
    //      </editor-fold>
    //<editor-fold desc dunglv test dataTable defaultstate="collapsed">
    List<Object[]> dataTable = Mockito.spy(ArrayList.class);
    Object[] objectsTable = new Object[]{};
    for (int i = 0; i < 501; i++) {
      dataTable.add(objectsTable);
    }
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFileNew(
                fileImport,
                0,
                6,
                0,
                23,
                1000
            ))
        .thenReturn(dataTable);
    //      </editor-fold>

    mrDeviceSoftBusiness.importDataMrDeviceSoft(multipartFile);
  }

  @Test
  public void importDataMrDeviceSoft2() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String filePath = "/temps";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.STT"))
        .thenReturn("mrDeviceSoft.STT");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceId"))
        .thenReturn("mrDeviceSoft.deviceId");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.marketName"))
        .thenReturn("mrDeviceSoft.marketName");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.regionSoft"))
        .thenReturn("mrDeviceSoft.regionSoft");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.arrayCode"))
        .thenReturn("mrDeviceSoft.arrayCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.networkType"))
        .thenReturn("mrDeviceSoft.networkType");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceType"))
        .thenReturn("mrDeviceSoft.deviceType");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.nodeIp"))
        .thenReturn("mrDeviceSoft.nodeIp");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.nodeCode"))
        .thenReturn("mrDeviceSoft.nodeCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceName"))
        .thenReturn("mrDeviceSoft.deviceName");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.createUserSoft"))
        .thenReturn("mrDeviceSoft.createUserSoft");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.comments"))
        .thenReturn("mrDeviceSoft.comments");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrSoft"))
        .thenReturn("mrDeviceSoft.mrSoft");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrConfirmSoftDisplay"))
        .thenReturn("mrDeviceSoft.mrConfirmSoftDisplay");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.impactNode"))
        .thenReturn("mrDeviceSoft.impactNode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.numberOfCr"))
        .thenReturn("mrDeviceSoft.numberOfCr");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.boUnitSoftId"))
        .thenReturn("mrDeviceSoft.boUnitSoftId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.lastDateStr"))
        .thenReturn("MrScheduleTel.lastDateStr");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.cdIdName"))
        .thenReturn("mrDeviceSoft.cdIdName");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.groupCode"))
        .thenReturn("mrDeviceSoft.groupCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.vendor"))
        .thenReturn("mrDeviceSoft.vendor");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.stationCode"))
        .thenReturn("mrDeviceSoft.stationCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.status"))
        .thenReturn("mrDeviceSoft.status");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.dateIntegratedStr"))
        .thenReturn("mrDeviceSoft.dateIntegratedStr");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrTypeStr"))
        .thenReturn("mrDeviceSoft.mrTypeStr");
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("mrDeviceSoft.STT"),
        I18n.getLanguage("mrDeviceSoft.deviceId") + "\n" + I18n
            .getLanguage("mrDeviceSoft.deviceId.noti"),
        I18n.getLanguage("mrDeviceSoft.marketName") + " (*)",
        I18n.getLanguage("mrDeviceSoft.regionSoft") + "\n" + I18n
            .getLanguage("mrDeviceSoft.deviceId.required"),
        I18n.getLanguage("mrDeviceSoft.arrayCode") + " (*)",
        I18n.getLanguage("mrDeviceSoft.networkType") + " (*)",
        I18n.getLanguage("mrDeviceSoft.deviceType") + " (*)",
        I18n.getLanguage("mrDeviceSoft.nodeIp"),
        I18n.getLanguage("mrDeviceSoft.nodeCode") + " (*)",
        I18n.getLanguage("mrDeviceSoft.deviceName") + " (*)",
        I18n.getLanguage("mrDeviceSoft.createUserSoft") + " (*)",
        I18n.getLanguage("mrDeviceSoft.comments"),
        I18n.getLanguage("mrDeviceSoft.mrSoft") + " (*)",
        I18n.getLanguage("mrDeviceSoft.mrConfirmSoftDisplay") + "\n" + I18n
            .getLanguage("mrDeviceSoft.mrSoft.required.no"),
        I18n.getLanguage("mrDeviceSoft.impactNode") + "\n" + I18n
            .getLanguage("mrDeviceSoft.mrSoft.required.yes"),
        I18n.getLanguage("mrDeviceSoft.numberOfCr") + "\n" + I18n
            .getLanguage("mrDeviceSoft.mrSoft.required.yes"),
        I18n.getLanguage("mrDeviceSoft.boUnitSoftId") + "\n" + I18n
            .getLanguage("mrDeviceSoft.deviceId.required"),
        I18n.getLanguage("mrDeviceSoft.cdIdName") + "\n" + I18n
            .getLanguage("mrDeviceSoft.mrSoft.required.yes"),
        I18n.getLanguage("mrDeviceSoft.groupCode"),
        I18n.getLanguage("mrDeviceSoft.vendor") + " (*)",
        I18n.getLanguage("mrDeviceSoft.stationCode"),
        I18n.getLanguage("mrDeviceSoft.status") + " (*)",
        I18n.getLanguage("mrDeviceSoft.dateIntegratedStr"),
        I18n.getLanguage("mrDeviceSoft.mrTypeStr") + " (*)",
    };
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                5,
                0,
                23,
                1000
            ))
        .thenReturn(dataHeader);
    //      </editor-fold>
    //<editor-fold desc dunglv test dataTable defaultstate="collapsed">
    List<Object[]> dataTable = Mockito.spy(ArrayList.class);
    Object[] objectsTable = new Object[24];
    for (int j = 0; j < objectsTable.length; j++) {
      objectsTable[j] = "123";
    }
    for (int i = 0; i < 1; i++) {
      dataTable.add(objectsTable);
    }
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFileNew(
                fileImport,
                0,
                6,
                0,
                23,
                1000
            ))
        .thenReturn(dataTable);
    //      </editor-fold>
    //<editor-fold desc dunglv setMapall test dataHeader defaultstate="collapsed">
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(2L);
    itemDataCRInside.setDisplayStr("2");
    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
    listMarket.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMarket);

    List<String> listRegion = Mockito.spy(ArrayList.class);
    when(mrDeviceRepository.getListRegionSoftByMarketCode(any())).thenReturn(listRegion);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("2");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable dataArray = Mockito.spy(Datatable.class);
    dataArray.setData(listArray);
    when(catItemRepository.getItemMaster(anyString(), anyString(), any(), anyString(), anyString()))
        .thenReturn(dataArray);

    String a = "t";
    List<String> listNetwork = Mockito.spy(ArrayList.class);
    listNetwork.add(a);
    when(mrDeviceRepository.getNetworkTypeByArrayCode(any())).thenReturn(listNetwork);

    List<String> listDevice = Mockito.spy(ArrayList.class);
    when(mrDeviceRepository.getDeviceTypeByNetworkType(any(), any())).thenReturn(listDevice);
    //      </editor-fold>
    //<editor-fold desc dunglv setMapConfig test dataHeader defaultstate="collapsed">
    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("2");
    List<MrConfigDTO> listMrConfig = Mockito.spy(ArrayList.class);
    listMrConfig.add(mrConfigDTO);
    when(mrConfigRepository.getConfigByGroup(anyString())).thenReturn(listMrConfig);
    //      </editor-fold>
    //<editor-fold desc dunglv setMapUnit test dataHeader defaultstate="collapsed">
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(2L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);
    //      </editor-fold>
    //<editor-fold desc dunglv setMapUser test dataHeader defaultstate="collapsed">
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("thanhlv12");
    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
    listUser.add(usersInsideDto);
    when(userRepository.getListUsersDTOS(any())).thenReturn(listUser);
    //      </editor-fold>

    MrDeviceDTO dtoUpdate = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(dtoUpdate);
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.err.update.deviceId.valid")).thenReturn("ID thiết bị phải là số nguyên dương");

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    mrDeviceSoftBusiness.importDataMrDeviceSoft(multipartFile);
  }

//  @Test
//  public void importDataMrDeviceSoft3() throws Exception {
//    PowerMockito.mockStatic(I18n.class);
//    PowerMockito.mockStatic(FileUtils.class);
//    PowerMockito.mockStatic(CommonImport.class);
//    PowerMockito.mockStatic(CommonExport.class);
//    UserToken userToken = Mockito.spy(UserToken.class);
//    userToken.setDeptId(1L);
//    userToken.setUserID(999999l);
//    userToken.setUserName("thanhlv12");
//    PowerMockito.mockStatic(TicketProvider.class);
//    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
//
//    String filePath = "/temps";
//    File fileImport = new File(filePath);
//    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
//    //<editor-fold desc dunglv test defaultstate="collapsed">
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.STT"))
//        .thenReturn("mrDeviceSoft.STT");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceId"))
//        .thenReturn("mrDeviceSoft.deviceId");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.marketName"))
//        .thenReturn("mrDeviceSoft.marketName");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.regionSoft"))
//        .thenReturn("mrDeviceSoft.regionSoft");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.arrayCode"))
//        .thenReturn("mrDeviceSoft.arrayCode");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.networkType"))
//        .thenReturn("mrDeviceSoft.networkType");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceType"))
//        .thenReturn("mrDeviceSoft.deviceType");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.nodeIp"))
//        .thenReturn("mrDeviceSoft.nodeIp");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.nodeCode"))
//        .thenReturn("mrDeviceSoft.nodeCode");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceName"))
//        .thenReturn("mrDeviceSoft.deviceName");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.createUserSoft"))
//        .thenReturn("mrDeviceSoft.createUserSoft");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.comments"))
//        .thenReturn("mrDeviceSoft.comments");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrSoft"))
//        .thenReturn("mrDeviceSoft.mrSoft");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrConfirmSoftDisplay"))
//        .thenReturn("mrDeviceSoft.mrConfirmSoftDisplay");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.impactNode"))
//        .thenReturn("mrDeviceSoft.impactNode");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.numberOfCr"))
//        .thenReturn("mrDeviceSoft.numberOfCr");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.boUnitSoftId"))
//        .thenReturn("mrDeviceSoft.boUnitSoftId");
//    PowerMockito.when(I18n.getLanguage("MrScheduleTel.lastDateStr"))
//        .thenReturn("MrScheduleTel.lastDateStr");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.cdIdName"))
//        .thenReturn("mrDeviceSoft.cdIdName");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.groupCode"))
//        .thenReturn("mrDeviceSoft.groupCode");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.vendor"))
//        .thenReturn("mrDeviceSoft.vendor");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.stationCode"))
//        .thenReturn("mrDeviceSoft.stationCode");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.status"))
//        .thenReturn("mrDeviceSoft.status");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.dateIntegratedStr"))
//        .thenReturn("mrDeviceSoft.dateIntegratedStr");
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrTypeStr"))
//        .thenReturn("mrDeviceSoft.mrTypeStr");
//    //      </editor-fold>
//    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
//    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
//    Object[] objectHeader = new Object[]{
//        I18n.getLanguage("mrDeviceSoft.STT"),
//        I18n.getLanguage("mrDeviceSoft.deviceId") + "\n" + I18n
//            .getLanguage("mrDeviceSoft.deviceId.noti"),
//        I18n.getLanguage("mrDeviceSoft.marketName") + " (*)",
//        I18n.getLanguage("mrDeviceSoft.regionSoft") + "\n" + I18n
//            .getLanguage("mrDeviceSoft.deviceId.required"),
//        I18n.getLanguage("mrDeviceSoft.arrayCode") + " (*)",
//        I18n.getLanguage("mrDeviceSoft.networkType") + " (*)",
//        I18n.getLanguage("mrDeviceSoft.deviceType") + " (*)",
//        I18n.getLanguage("mrDeviceSoft.nodeIp"),
//        I18n.getLanguage("mrDeviceSoft.nodeCode") + " (*)",
//        I18n.getLanguage("mrDeviceSoft.deviceName") + " (*)",
//        I18n.getLanguage("mrDeviceSoft.createUserSoft") + " (*)",
//        I18n.getLanguage("mrDeviceSoft.comments"),
//        I18n.getLanguage("mrDeviceSoft.mrSoft") + " (*)",
//        I18n.getLanguage("mrDeviceSoft.mrConfirmSoftDisplay") + "\n" + I18n
//            .getLanguage("mrDeviceSoft.mrSoft.required.no"),
//        I18n.getLanguage("mrDeviceSoft.impactNode") + "\n" + I18n
//            .getLanguage("mrDeviceSoft.mrSoft.required.yes"),
//        I18n.getLanguage("mrDeviceSoft.numberOfCr") + "\n" + I18n
//            .getLanguage("mrDeviceSoft.mrSoft.required.yes"),
//        I18n.getLanguage("mrDeviceSoft.boUnitSoftId") + "\n" + I18n
//            .getLanguage("mrDeviceSoft.deviceId.required"),
//        I18n.getLanguage("mrDeviceSoft.cdIdName") + "\n" + I18n
//            .getLanguage("mrDeviceSoft.mrSoft.required.yes"),
//        I18n.getLanguage("mrDeviceSoft.groupCode"),
//        I18n.getLanguage("mrDeviceSoft.vendor") + " (*)",
//        I18n.getLanguage("mrDeviceSoft.stationCode"),
//        I18n.getLanguage("mrDeviceSoft.status") + " (*)",
//        I18n.getLanguage("mrDeviceSoft.dateIntegratedStr"),
//        I18n.getLanguage("mrDeviceSoft.mrTypeStr") + " (*)",
//    };
//    dataHeader.add(objectHeader);
//    PowerMockito
//        .when(CommonImport
//            .getDataFromExcelFile(
//                fileImport,
//                0,
//                5,
//                0,
//                23,
//                1000
//            ))
//        .thenReturn(dataHeader);
//    //      </editor-fold>
//    //<editor-fold desc dunglv test dataTable defaultstate="collapsed">
//    List<Object[]> dataTable = Mockito.spy(ArrayList.class);
//    Object[] objectsTable = new Object[24];
//    for (int j = 0; j<objectsTable.length; j++){
//      if(j ==1){
//        objectsTable[j] = "abc";
//      }else{
//        objectsTable[j] = "123";
//      }
//    }
//    for (int i = 0; i < 1; i++) {
//      dataTable.add(objectsTable);
//    }
//    PowerMockito
//        .when(CommonImport
//            .getDataFromExcelFileNew(
//                fileImport,
//                0,
//                6,
//                0,
//                23,
//                1000
//            ))
//        .thenReturn(dataTable);
//    //      </editor-fold>
//    //<editor-fold desc dunglv setMapall test dataHeader defaultstate="collapsed">
//    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
//    itemDataCRInside.setValueStr(2L);
//    itemDataCRInside.setDisplayStr("2");
//    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
//    listMarket.add(itemDataCRInside);
//    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMarket);
//
//    List<String> listRegion = Mockito.spy(ArrayList.class);
//    when(mrDeviceRepository.getListRegionSoftByMarketCode(any())).thenReturn(listRegion);
//
//    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
//    catItemDTO.setItemName("2");
//    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
//    listArray.add(catItemDTO);
//    Datatable dataArray = Mockito.spy(Datatable.class);
//    dataArray.setData(listArray);
//    when(catItemRepository.getItemMaster(anyString(),anyString(),any(),anyString(), anyString())).thenReturn(dataArray);
//
//    String a = "t";
//    List<String> listNetwork = Mockito.spy(ArrayList.class);
//    listNetwork.add(a);
//    when(mrDeviceRepository.getNetworkTypeByArrayCode(any())).thenReturn(listNetwork);
//
//    List<String> listDevice = Mockito.spy(ArrayList.class);
//    when(mrDeviceRepository.getDeviceTypeByNetworkType(any(), any())).thenReturn(listDevice);
//    //      </editor-fold>
//    //<editor-fold desc dunglv setMapConfig test dataHeader defaultstate="collapsed">
//    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
//    mrConfigDTO.setConfigCode("2");
//    List<MrConfigDTO> listMrConfig = Mockito.spy(ArrayList.class);
//    listMrConfig.add(mrConfigDTO);
//    when(mrConfigRepository.getConfigByGroup(anyString())).thenReturn(listMrConfig);
//    //      </editor-fold>
//    //<editor-fold desc dunglv setMapUnit test dataHeader defaultstate="collapsed">
//    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
//    unitDTO.setUnitId(2L);
//    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
//    listUnit.add(unitDTO);
//    when(unitRepository.getListUnit(any())).thenReturn(listUnit);
//    //      </editor-fold>
//    //<editor-fold desc dunglv setMapUser test dataHeader defaultstate="collapsed">
//    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
//    usersInsideDto.setUsername("thanhlv12");
//    List<UsersInsideDto> listUser = Mockito.spy(ArrayList.class);
//    listUser.add(usersInsideDto);
//    when(userRepository.getListUsersDTOS(any())).thenReturn(listUser);
//    //      </editor-fold>
//
//    MrDeviceDTO dtoUpdate = Mockito.spy(MrDeviceDTO.class);
//    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(dtoUpdate);
//    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.err.update.deviceId.valid")).thenReturn("ID thiết bị phải là số nguyên dương");
////    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.import.fileNameOut")).thenReturn("test");
//    File fileExportSuccess = new File("./test_junit/test.txt");
//    PowerMockito.mockStatic(CommonExport.class);
//    PowerMockito.when(CommonExport
//        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
//        .thenReturn(fileExportSuccess);
//    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
//    mrDeviceSoftBusiness.importDataMrDeviceSoft(multipartFile);
//  }


  @Test
  public void getTemplateImport() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.sheetName.networkType")).thenReturn("aa");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.sheetName.deviceType")).thenReturn("test");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.sheetName.unit")).thenReturn("test2");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.sheetName.woCdGroup")).thenReturn("test3");
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.STT"))
        .thenReturn("mrDeviceSoft.STT");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceId"))
        .thenReturn("mrDeviceSoft.deviceId");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.marketName"))
        .thenReturn("mrDeviceSoft.marketName");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.regionSoft"))
        .thenReturn("mrDeviceSoft.regionSoft");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.arrayCode"))
        .thenReturn("mrDeviceSoft.arrayCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.networkType"))
        .thenReturn("mrDeviceSoft.networkType");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceType"))
        .thenReturn("mrDeviceSoft.deviceType");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.nodeIp"))
        .thenReturn("mrDeviceSoft.nodeIp");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.nodeCode"))
        .thenReturn("mrDeviceSoft.nodeCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.deviceName"))
        .thenReturn("mrDeviceSoft.deviceName");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.createUserSoft"))
        .thenReturn("mrDeviceSoft.createUserSoft");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.comments"))
        .thenReturn("mrDeviceSoft.comments");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrSoft"))
        .thenReturn("mrDeviceSoft.mrSoft");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrConfirmSoftDisplay"))
        .thenReturn("mrDeviceSoft.mrConfirmSoftDisplay");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.impactNode"))
        .thenReturn("mrDeviceSoft.impactNode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.numberOfCr"))
        .thenReturn("mrDeviceSoft.numberOfCr");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.boUnitSoftId"))
        .thenReturn("mrDeviceSoft.boUnitSoftId");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.cdIdName"))
        .thenReturn("mrDeviceSoft.cdIdName");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.groupCode"))
        .thenReturn("mrDeviceSoft.groupCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.vendor"))
        .thenReturn("mrDeviceSoft.vendor");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.stationCode"))
        .thenReturn("mrDeviceSoft.stationCode");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.statusName"))
        .thenReturn("mrDeviceSoft.statusName");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.dateIntegratedStr"))
        .thenReturn("mrDeviceSoft.dateIntegratedStr");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.mrTypeStr"))
        .thenReturn("mrDeviceSoft.mrTypeStr");
    //      </editor-fold>
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
    listMarket.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMarket);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable dataArray = Mockito.spy(Datatable.class);
    dataArray.setData(listArray);
    when(catItemRepository.getItemMaster(anyString(), anyString(), any(), anyString(), anyString()))
        .thenReturn(dataArray);

    String network = "t";
    List<String> listNetwork = Mockito.spy(ArrayList.class);
    listNetwork.add(network);
    when(mrDeviceRepository.getNetworkTypeByArrayCode(any())).thenReturn(listNetwork);

    String device = "t";
    List<String> listDevice = Mockito.spy(ArrayList.class);
    listDevice.add(device);
    when(mrDeviceRepository.getDeviceTypeByNetworkType(any(), any())).thenReturn(listDevice);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    List<MrConfigDTO> listConfirmSoft = Mockito.spy(ArrayList.class);
    listConfirmSoft.add(mrConfigDTO);
    when(mrConfigRepository.getConfigByGroup(anyString())).thenReturn(listConfirmSoft);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(2L);
    unitDTO.setParentUnitId(2L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(null)).thenReturn(listUnit);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(2L);
    List<WoCdGroupInsideDTO> listWoCdGroup = Mockito.spy(ArrayList.class);
    listWoCdGroup.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUserProxy(any())).thenReturn(listWoCdGroup);

    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.unitId")).thenReturn("post");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.unitCode")).thenReturn("get");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.unitName")).thenReturn("put");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.unitParentId")).thenReturn("delete");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.woGroupId")).thenReturn("00");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.woGroupName")).thenReturn("01");
    PowerMockito.when(I18n.getLanguage("mrDeviceSoft.import.sheetName")).thenReturn("02");
    mrDeviceSoftBusiness.getTemplateImport();

  }

  @Test
  public void getListRegionCombobox() {
    List<String> lstStr = Mockito.spy(ArrayList.class);
    String marketCode = "";
    when(mrDeviceRepository.getListRegionSoftByMarketCode(any())).thenReturn(lstStr);
    List<String> result = mrDeviceSoftBusiness.getListRegionCombobox(marketCode);
    assertEquals(lstStr.size(), result.size());
  }

  @Test
  public void getListNetworkTypeCombobox() {
    List<String> lstStr = Mockito.spy(ArrayList.class);
    when(mrDeviceRepository.getNetworkTypeByArrayCode(any())).thenReturn(lstStr);
    String arrayCode = "";
    List<String> result = mrDeviceSoftBusiness.getListNetworkTypeCombobox(arrayCode);
    assertEquals(result.size(), lstStr.size());
  }

  @Test
  public void getListDeviceTypeCombobox() {
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    List<String> lstStr = Mockito.spy(ArrayList.class);
    when(mrDeviceRepository.getDeviceTypeByNetworkType(any(), any())).thenReturn(lstStr);
    List<String> result = mrDeviceSoftBusiness.getListDeviceTypeCombobox(mrDeviceDTO);
    assertEquals(lstStr.size(), result.size());
  }

  @Test
  public void getListUnitCombobox() {
    List<UnitDTO> lstUnit = Mockito.spy(ArrayList.class);
    when(unitRepository.getListUnit(null)).thenReturn(lstUnit);
    List<UnitDTO> result = mrDeviceSoftBusiness.getListUnitCombobox();
    assertEquals(result.size(), lstUnit.size());
  }

  @Test
  public void handleFileExport() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    List<MrDeviceDTO> list = Mockito.spy(ArrayList.class);
    String[] columnExport = {""};
    String date = "01";
    String code = "RESULT_IMPORT";
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    mrDeviceSoftBusiness.handleFileExport(list, columnExport, date, code);
  }

  @Test
  public void handleFileExport1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    List<MrDeviceDTO> list = Mockito.spy(ArrayList.class);
    String[] columnExport = {""};
    String date = "";
    String code = "EXPORT_MR_DEVICE_SOFT";
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    mrDeviceSoftBusiness.handleFileExport(list, columnExport, date, code);
  }

  @Test
  public void getListHeaderSheet() {
    String[] columnExport = {"a", "b"};
    mrDeviceSoftBusiness.getListHeaderSheet(columnExport);
  }

  @Test
  public void setDetailValue() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setImplementUnit("2");
    mrDeviceDTO.setCheckingUnit("2");
    mrDeviceDTO.setBoUnitSoft(2L);
    mrDeviceDTO.setApproveStatusSoft(0L);
    mrDeviceDTO.setCdId(2L);
    mrDeviceDTO.setCdIdHard(2L);
    mrDeviceDTO.setStatus("0");
    mrDeviceDTO.setDateIntegratedStr("12345678901");
    mrDeviceDTO.setMarketCode("2");
    mrDeviceDTO.setMrConfirmSoft("2");
    mrDeviceDTO.setMrSoft("1");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(2L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(2L);
    List<WoCdGroupInsideDTO> listWoCdGroup = Mockito.spy(ArrayList.class);
    listWoCdGroup.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUserProxy(any())).thenReturn(listWoCdGroup);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(2L);
    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
    listMarket.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMarket);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("2");
    List<MrConfigDTO> listMrConfig = Mockito.spy(ArrayList.class);
    listMrConfig.add(mrConfigDTO);
    when(mrConfigRepository.getConfigByGroup(anyString())).thenReturn(listMrConfig);

    List<MrCfgCrUnitTelDTO> listMrCfgCrUnitTel = Mockito.spy(ArrayList.class);
    when(mrCfgCrUnitTelRepository.getListMrCfgCrUnitTel(any())).thenReturn(listMrCfgCrUnitTel);

    mrDeviceSoftBusiness.setDetailValue(mrDeviceDTO);
  }

  @Test
  public void setMapUnit() {
  }

  @Test
  public void setMapWoCdGroup() {
  }

  @Test
  public void setMapMarket() {
  }

  @Test
  public void setMapMrConfig() {
  }

  @Test
  public void setListMrCfgCrUnitTel() {
  }

  @Test
  public void setMapAll() {
  }

  @Test
  public void setMapUser() {
  }
}
