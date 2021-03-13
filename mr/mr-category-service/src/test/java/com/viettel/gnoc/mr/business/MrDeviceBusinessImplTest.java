package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrDeviceSoftRepository;
import com.viettel.gnoc.mr.repository.MrHisRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
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
@PrepareForTest({MrDeviceBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrDeviceBusinessImplTest {

  @InjectMocks
  MrDeviceBusinessImpl mrDeviceBusiness;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  MrScheduleTelRepository mrScheduleTelRepository;

  @Mock
  MrDeviceRepository mrDeviceRepository;

  @Mock
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitRepository unitRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  MrDeviceSoftRepository mrDeviceSoftRepository;

  @Mock
  MrScheduleTelHisRepository mrScheduleTelHisRepository;

  @Mock
  MrHisRepository mrHisRepository;

  @Mock
  MrRepository mrRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrDeviceBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void setMapMarketName() {
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);
    mrDeviceBusiness.setMapMarketName();
  }

  @Test
  public void setMrConfirmHardStr() {
    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("1");
    mrConfigDTO.setConfigName("2");
    List<MrConfigDTO> lstMrConfirmHardStr = Mockito.spy(ArrayList.class);
    lstMrConfirmHardStr.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstMrConfirmHardStr);
    mrDeviceBusiness.setMrConfirmHardStr();
  }

  @Test
  public void setMapArray() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(lstArrayCode);
    mrDeviceBusiness.setMapArray();
  }

  @Test
  public void getListDeviceTypeByNetworkType() {
    List<MrDeviceDTO> lst = Mockito.spy(ArrayList.class);
    when(mrDeviceRepository.getListDeviceTypeByNetworkType(anyString(), anyString()))
        .thenReturn(lst);
    List<MrDeviceDTO> result = mrDeviceBusiness.getListDeviceTypeByNetworkType("1", "2");
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void getListNetworkTypeByArrayCode() {
    List<MrDeviceDTO> lst = Mockito.spy(ArrayList.class);
    when(mrDeviceRepository.getListNetworkTypeByArrayCode(anyString())).thenReturn(lst);
    List<MrDeviceDTO> result = mrDeviceBusiness.getListNetworkTypeByArrayCode("1");
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void getListMrDeviceSoftDTO() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setBoUnitHard(1L);
    mrDeviceDTO.setApproveStatusHard(0L);
    List<MrDeviceDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    mrDeviceDTOList.add(mrDeviceDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(mrDeviceDTOList);
    when(mrDeviceRepository.getListMrDeviceSoftDTO(any())).thenReturn(datatable);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    unitDTO.setUnitId(1L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    when(unitRepository.getListUnit(null)).thenReturn(listUnit);

    when(userRepository.getUnitParentForApprove(anyString(),
        anyString())).thenReturn("1");

    when(unitRepository.findUnitById(any())).thenReturn(unitDTO);

    when(userRepository.checkRoleOfUser("TP", userToken.getUserID())).thenReturn(true);

    mrDeviceBusiness.getListMrDeviceSoftDTO(mrDeviceDTO);
  }

  @Test
  public void setDetailValue() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setImplementUnit("1");
    mrDeviceDTO.setCheckingUnit("1");
    mrDeviceDTO.setApproveStatusHard(0L);
    mrDeviceDTO.setDateIntegratedStr("06/06/2020 12:12:12t");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(null)).thenReturn(listUnit);
    mrDeviceBusiness.setMapUnit();

    mrDeviceBusiness.setDetailValue(mrDeviceDTO);
  }

  @Test
  public void setDetailValue1() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setImplementUnit("1");
    mrDeviceDTO.setCheckingUnit("1");
    mrDeviceDTO.setApproveStatusHard(1L);
    mrDeviceDTO.setDateIntegratedStr("06/06/2020 12:12:12t");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(null)).thenReturn(listUnit);
    mrDeviceBusiness.setMapUnit();

    mrDeviceBusiness.setDetailValue(mrDeviceDTO);
  }

  @Test
  public void setDetailValue2() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setImplementUnit("1");
    mrDeviceDTO.setCheckingUnit("1");
    mrDeviceDTO.setApproveStatusHard(2L);
    mrDeviceDTO.setDateIntegratedStr("06/06/2020 12:12:12t");

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(null)).thenReturn(listUnit);
    mrDeviceBusiness.setMapUnit();

    mrDeviceBusiness.setDetailValue(mrDeviceDTO);
  }

  @Test
  public void setMapUnit() {
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    when(unitRepository.getListUnit(null)).thenReturn(listUnit);
    mrDeviceBusiness.setMapUnit();
  }

  @Test
  public void getListRegionByMarketCode() {
    List<MrDeviceDTO> lst = Mockito.spy(ArrayList.class);
    when(mrDeviceRepository.getListRegionByMarketCode(anyString())).thenReturn(lst);
    List<MrDeviceDTO> result = mrDeviceBusiness.getListRegionByMarketCode("1");
    assertEquals(lst.size(), result.size());

  }

  @Test
  public void updateMrDeviceHard() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setBoUnitHard(1L);
    mrDeviceDTO.setMrHard("1");
    mrDeviceDTO.setMrConfirmHard("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrDeviceDTO mrDeviceOld = Mockito.spy(MrDeviceDTO.class);
    mrDeviceOld.setMrConfirmHard("2");
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setMrId(1L);
    List<MrScheduleTelDTO> listSchedule = Mockito.spy(ArrayList.class);
    listSchedule.add(mrScheduleTelDTO);
    when(mrScheduleTelRepository.getListMrScheduleTelDTO(any())).thenReturn(listSchedule);

    when(userRepository.getUnitParentForApprove(anyString(), anyString())).thenReturn("1");
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitDTO);
    when(userRepository.checkRoleOfUser("TP", userToken.getUserID())).thenReturn(true);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    when(mrRepository.findMrById(any())).thenReturn(mrInsideDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrRepository.deleteMr(any())).thenReturn(resultInSideDto);
    when(mrHisRepository.insertMrHis(any())).thenReturn(resultInSideDto);

    when(mrDeviceRepository.edit(any())).thenReturn(resultInSideDto);

    mrDeviceBusiness.updateMrDeviceHard(mrDeviceDTO, false, false);
  }


  @Test
  public void updateMrDeviceHard1() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setBoUnitHard(1L);
    mrDeviceDTO.setMrHard("0");
    mrDeviceDTO.setMrConfirmHard("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrDeviceDTO mrDeviceOld = Mockito.spy(MrDeviceDTO.class);
    mrDeviceOld.setMrConfirmHard("2");
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setMrId(1L);
    List<MrScheduleTelDTO> listSchedule = Mockito.spy(ArrayList.class);
    listSchedule.add(mrScheduleTelDTO);
    when(mrScheduleTelRepository.getListMrScheduleTelDTO(any())).thenReturn(listSchedule);

    when(userRepository.getUnitParentForApprove(anyString(), anyString())).thenReturn("1");
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    when(unitRepository.findUnitById(userToken.getDeptId())).thenReturn(unitDTO);
    when(userRepository.checkRoleOfUser("TP", userToken.getUserID())).thenReturn(true);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    when(mrRepository.findMrById(any())).thenReturn(mrInsideDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrRepository.deleteMr(any())).thenReturn(resultInSideDto);
    when(mrHisRepository.insertMrHis(any())).thenReturn(resultInSideDto);

    when(mrDeviceRepository.edit(any())).thenReturn(resultInSideDto);

    mrDeviceBusiness.updateMrDeviceHard(mrDeviceDTO, false, false);
  }

  @Test
  public void convertScheduleTelHis() {
    PowerMockito.mockStatic(I18n.class);
    MrScheduleTelDTO dto = Mockito.spy(MrScheduleTelDTO.class);
    dto.setCrId(1L);
    dto.setDeviceId(1L);
    dto.setMrId(1L);
    dto.setProcedureId(1L);
    mrDeviceBusiness.convertScheduleTelHis(dto, 1);
  }

  @Test
  public void convertScheduleTelHis1() {
    PowerMockito.mockStatic(I18n.class);
    MrScheduleTelDTO dto = Mockito.spy(MrScheduleTelDTO.class);
    dto.setCrId(1L);
    dto.setDeviceId(1L);
    dto.setMrId(1L);
    dto.setProcedureId(1L);
    mrDeviceBusiness.convertScheduleTelHis(dto, 0);
  }

  @Test
  public void getDetail() {
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceRepository.getDetail(any())).thenReturn(mrDeviceDTO);
    MrDeviceDTO result = mrDeviceBusiness.getDetail(1L);
    assertEquals(result, mrDeviceDTO);
  }

  @Test
  public void deleteMrDeviceHard() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    List<MrScheduleTelDTO> schedules = Mockito.spy(ArrayList.class);
    when(mrScheduleTelRepository.getListMrScheduleTelDTO(any())).thenReturn(schedules);

    when(mrScheduleTelRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);
    when(mrDeviceRepository.delete(any())).thenReturn(resultInSideDto);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceBusiness.deleteMrDeviceHard(mrDeviceDTO);
  }

  @Test
  public void exportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStatus("1");
    mrDeviceDTO.setMrHard("1");
    mrDeviceDTO.setImplementUnit("1");
    mrDeviceDTO.setCheckingUnit("1");
    List<MrDeviceDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    mrDeviceDTOList.add(mrDeviceDTO);
    when(mrDeviceRepository.getListDataExport(any())).thenReturn(mrDeviceDTOList);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    when(unitRepository.getListUnit(null)).thenReturn(listUnit);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrDeviceBusiness.exportData(mrDeviceDTO);
  }

  @Test
  public void exportData1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setStatus("0");
    mrDeviceDTO.setMrHard("0");
    mrDeviceDTO.setImplementUnit("1");
    mrDeviceDTO.setCheckingUnit("1");
    List<MrDeviceDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    mrDeviceDTOList.add(mrDeviceDTO);
    when(mrDeviceRepository.getListDataExport(any())).thenReturn(mrDeviceDTOList);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    when(unitRepository.getListUnit(null)).thenReturn(listUnit);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrDeviceBusiness.exportData(mrDeviceDTO);
  }

  @Test
  public void getTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrDevice.deviceIdStr"))
        .thenReturn("mrDevice.deviceIdStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.marketName"))
        .thenReturn("mrDevice.marketName");
    PowerMockito.when(I18n.getLanguage("mrDevice.regionHard"))
        .thenReturn("mrDevice.regionHard");
    PowerMockito.when(I18n.getLanguage("mrDevice.arrayCodeStr"))
        .thenReturn("mrDevice.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.networkTypeStr"))
        .thenReturn("mrDevice.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.deviceTypeStr"))
        .thenReturn("mrDevice.deviceTypeStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.nodeIp"))
        .thenReturn("mrDevice.nodeIp");
    PowerMockito.when(I18n.getLanguage("mrDevice.nodeCode"))
        .thenReturn("mrDevice.nodeCode");
    PowerMockito.when(I18n.getLanguage("mrDevice.deviceName"))
        .thenReturn("mrDevice.deviceName");
    PowerMockito.when(I18n.getLanguage("mrDevice.vendor"))
        .thenReturn("mrDevice.vendor");
    PowerMockito.when(I18n.getLanguage("mrDevice.statusStr"))
        .thenReturn("mrDevice.statusStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.dateIntegrated"))
        .thenReturn("mrDevice.dateIntegrated");
    PowerMockito.when(I18n.getLanguage("mrDevice.stationCode"))
        .thenReturn("mrDevice.stationCode");
    PowerMockito.when(I18n.getLanguage("mrDevice.userMrHard"))
        .thenReturn("mrDevice.userMrHard");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrConfirmHardStr"))
        .thenReturn("mrDevice.mrConfirmHardStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrHardStr"))
        .thenReturn("mrDevice.mrHardStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.boUnitHard"))
        .thenReturn("mrDevice.boUnitHard");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrTypeStr"))
        .thenReturn("mrDevice.mrTypeStr");

    PowerMockito.when(I18n.getLanguage("mrDevice.title"))
        .thenReturn("mrDevice.title");
    PowerMockito.when(I18n.getLanguage("mrDevice.arrNet"))
        .thenReturn("mrDevice.arrNet");
    PowerMockito.when(I18n.getLanguage("mrDevice.boUnitHard"))
        .thenReturn("mrDevice.boUnitHard");
    //      </editor-fold>

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(lstArrayCode);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    lstNetwork.add(mrDeviceDTO);
    when(mrDeviceRepository.getListNetworkTypeByArrayCode(anyString())).thenReturn(lstNetwork);

    List<MrDeviceDTO> lstDeviceType = Mockito.spy(ArrayList.class);
    lstDeviceType.add(mrDeviceDTO);
    when(mrDeviceRepository.getListDeviceTypeByNetworkType(any(), any())).thenReturn(lstDeviceType);

    List<UnitDTO> unitNameList = Mockito.spy(ArrayList.class);
    when(unitRepository.getListUnit(null)).thenReturn(unitNameList);

    mrDeviceBusiness.getTemplate();
  }

  @Test
  public void importData() {
    MultipartFile multipartFile = null;
    mrDeviceBusiness.importData(multipartFile);
  }

  @Test
  public void importData1() {
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSXs");
    mrDeviceBusiness.importData(multipartFile);
  }

  @Test
  public void importData2() throws Exception {
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    PowerMockito.mockStatic(FileUtils.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");

    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(CommonImport.class);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        4,
        0,
        18,
        1000
    )).thenReturn(headerList);

    mrDeviceBusiness.importData(multipartFile);
  }

  @Test
  public void importData3() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    PowerMockito.mockStatic(FileUtils.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");

    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrDevice.deviceIdStr"))
        .thenReturn("mrDevice.deviceIdStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.marketName"))
        .thenReturn("mrDevice.marketName");
    PowerMockito.when(I18n.getLanguage("mrDevice.regionHard"))
        .thenReturn("mrDevice.regionHard");
    PowerMockito.when(I18n.getLanguage("mrDevice.arrayCodeStr"))
        .thenReturn("mrDevice.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.networkTypeStr"))
        .thenReturn("mrDevice.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.deviceTypeStr"))
        .thenReturn("mrDevice.deviceTypeStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.nodeIp"))
        .thenReturn("mrDevice.nodeIp");
    PowerMockito.when(I18n.getLanguage("mrDevice.nodeCode"))
        .thenReturn("mrDevice.nodeCode");
    PowerMockito.when(I18n.getLanguage("mrDevice.deviceName"))
        .thenReturn("mrDevice.deviceName");
    PowerMockito.when(I18n.getLanguage("mrDevice.vendor"))
        .thenReturn("mrDevice.vendor");
    PowerMockito.when(I18n.getLanguage("mrDevice.statusStr"))
        .thenReturn("mrDevice.statusStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.dateIntegrated"))
        .thenReturn("mrDevice.dateIntegrated");
    PowerMockito.when(I18n.getLanguage("mrDevice.stationCode"))
        .thenReturn("mrDevice.stationCode");
    PowerMockito.when(I18n.getLanguage("mrDevice.userMrHard"))
        .thenReturn("mrDevice.userMrHard");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrConfirmHardStr"))
        .thenReturn("mrDevice.mrConfirmHardStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrHardStr"))
        .thenReturn("mrDevice.mrHardStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.boUnitHard"))
        .thenReturn("mrDevice.boUnitHard");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrTypeStr"))
        .thenReturn("mrDevice.mrTypeStr");
    //      </editor-fold>
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrDevice.deviceIdStr"),
        I18n.getLanguage("mrDevice.marketName") + "*",
        I18n.getLanguage("mrDevice.regionHard"),
        I18n.getLanguage("mrDevice.arrayCodeStr") + "*",
        I18n.getLanguage("mrDevice.networkTypeStr") + "*",
        I18n.getLanguage("mrDevice.deviceTypeStr") + "*",
        I18n.getLanguage("mrDevice.nodeIp"),
        I18n.getLanguage("mrDevice.nodeCode") + "*",
        I18n.getLanguage("mrDevice.deviceName") + "*",
        I18n.getLanguage("mrDevice.vendor") + "*",
        I18n.getLanguage("mrDevice.statusStr") + "*",
        I18n.getLanguage("mrDevice.dateIntegrated"),
        I18n.getLanguage("mrDevice.stationCode"),
        I18n.getLanguage("mrDevice.userMrHard"),
        I18n.getLanguage("mrDevice.mrConfirmHardStr"),
        I18n.getLanguage("mrDevice.mrHardStr") + "*",
        I18n.getLanguage("mrDevice.boUnitHard"),
        I18n.getLanguage("mrDevice.mrTypeStr")
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);

    PowerMockito.mockStatic(CommonImport.class);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        4,
        0,
        18,
        1000
    )).thenReturn(headerList);

    Object[] objectstest = new Object[]{};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1501; i++) {
      dataImportList.add(objectstest);
    }
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        18,
        1000
    )).thenReturn(dataImportList);
    mrDeviceBusiness.importData(multipartFile);
  }

  @Test
  public void importData4() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    PowerMockito.mockStatic(FileUtils.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");

    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrDevice.deviceIdStr"))
        .thenReturn("mrDevice.deviceIdStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.marketName"))
        .thenReturn("mrDevice.marketName");
    PowerMockito.when(I18n.getLanguage("mrDevice.regionHard"))
        .thenReturn("mrDevice.regionHard");
    PowerMockito.when(I18n.getLanguage("mrDevice.arrayCodeStr"))
        .thenReturn("mrDevice.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.networkTypeStr"))
        .thenReturn("mrDevice.networkTypeStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.deviceTypeStr"))
        .thenReturn("mrDevice.deviceTypeStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.nodeIp"))
        .thenReturn("mrDevice.nodeIp");
    PowerMockito.when(I18n.getLanguage("mrDevice.nodeCode"))
        .thenReturn("mrDevice.nodeCode");
    PowerMockito.when(I18n.getLanguage("mrDevice.deviceName"))
        .thenReturn("mrDevice.deviceName");
    PowerMockito.when(I18n.getLanguage("mrDevice.vendor"))
        .thenReturn("mrDevice.vendor");
    PowerMockito.when(I18n.getLanguage("mrDevice.statusStr"))
        .thenReturn("mrDevice.statusStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.dateIntegrated"))
        .thenReturn("mrDevice.dateIntegrated");
    PowerMockito.when(I18n.getLanguage("mrDevice.stationCode"))
        .thenReturn("mrDevice.stationCode");
    PowerMockito.when(I18n.getLanguage("mrDevice.userMrHard"))
        .thenReturn("mrDevice.userMrHard");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrConfirmHardStr"))
        .thenReturn("mrDevice.mrConfirmHardStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrHardStr"))
        .thenReturn("mrDevice.mrHardStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.boUnitHard"))
        .thenReturn("mrDevice.boUnitHard");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrTypeStr"))
        .thenReturn("mrDevice.mrTypeStr");
    PowerMockito.when(I18n.getLanguage("mrDevice.status.1"))
        .thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrHardStr.0"))
        .thenReturn("0");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrHardStr.1"))
        .thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrDevice.mrTypeStr.BD"))
        .thenReturn("37");
    //      </editor-fold>
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrDevice.deviceIdStr"),
        I18n.getLanguage("mrDevice.marketName") + "*",
        I18n.getLanguage("mrDevice.regionHard"),
        I18n.getLanguage("mrDevice.arrayCodeStr") + "*",
        I18n.getLanguage("mrDevice.networkTypeStr") + "*",
        I18n.getLanguage("mrDevice.deviceTypeStr") + "*",
        I18n.getLanguage("mrDevice.nodeIp"),
        I18n.getLanguage("mrDevice.nodeCode") + "*",
        I18n.getLanguage("mrDevice.deviceName") + "*",
        I18n.getLanguage("mrDevice.vendor") + "*",
        I18n.getLanguage("mrDevice.statusStr") + "*",
        I18n.getLanguage("mrDevice.dateIntegrated"),
        I18n.getLanguage("mrDevice.stationCode"),
        I18n.getLanguage("mrDevice.userMrHard"),
        I18n.getLanguage("mrDevice.mrConfirmHardStr"),
        I18n.getLanguage("mrDevice.mrHardStr") + "*",
        I18n.getLanguage("mrDevice.boUnitHard"),
        I18n.getLanguage("mrDevice.mrTypeStr")
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);

    PowerMockito.mockStatic(CommonImport.class);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        4,
        0,
        18,
        1000
    )).thenReturn(headerList);

    Object[] objectstest = new Object[19];
    for (int j = 0; j < objectstest.length; j++) {
      if (j == 12) {
        objectstest[j] = "06/06/2020 12:12:12";
      } else if (j == 1) {
        objectstest[j] = "t";
      } else {
        objectstest[j] = "1";
      }
    }
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objectstest);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        18,
        1000
    )).thenReturn(dataImportList);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(lstArrayCode);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("1");
    mrConfigDTO.setConfigName("2");
    List<MrConfigDTO> lstMrConfirmHardStr = Mockito.spy(ArrayList.class);
    lstMrConfirmHardStr.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstMrConfirmHardStr);

    MrDeviceDTO dto = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(dto);

    MrDeviceDTO networkCheck = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceRepository.checkNetworkTypeByArrayCode(any(), any())).thenReturn(networkCheck);

    MrDeviceDTO deviceCheck = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceRepository.checkDeviceTypeByArrayNet(any(), any(), any())).thenReturn(deviceCheck);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> userDTOList = Mockito.spy(ArrayList.class);
    userDTOList.add(usersInsideDto);
    when(userRepository.getListUsersDTOS(any())).thenReturn(userDTOList);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    when(mrDeviceRepository.edit(any())).thenReturn(resultInSideDto1);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    mrDeviceBusiness.importData(multipartFile);
  }

  @Test
  public void onSearchEntity() {
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> lst = Mockito.spy(ArrayList.class);
    when(mrDeviceRepository.onSearchEntity(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(lst);
    List<MrDeviceDTO> result = mrDeviceBusiness.onSearchEntity(mrDeviceDTO, 1, 2, "test", "test");
    assertEquals(result.size(), lst.size());
  }

  @Test
  public void insertOrUpdateListDevice() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    List<MrDeviceDTO> lstMrDeviceDto = Mockito.spy(ArrayList.class);
    when(mrDeviceRepository.updateList(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrDeviceBusiness.insertOrUpdateListDevice(lstMrDeviceDto);
    assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void updateStatusAndLastDate() {
    MrDeviceDTO objUpdate = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(objUpdate);

    MrScheduleTelDTO objScheduleTel = Mockito.spy(MrScheduleTelDTO.class);
    mrDeviceBusiness.updateStatusAndLastDate(objScheduleTel, "1", "06/06/2020 12:12:12");
    objScheduleTel.setMrHard("1");
    objScheduleTel.setMrHardCycle(1L);
    mrDeviceBusiness.updateStatusAndLastDate(objScheduleTel, "1", "06/06/2020 12:12:12");
    objScheduleTel.setMrHardCycle(3L);
    mrDeviceBusiness.updateStatusAndLastDate(objScheduleTel, "1", "06/06/2020 12:12:12");
    objScheduleTel.setMrHardCycle(6L);
    mrDeviceBusiness.updateStatusAndLastDate(objScheduleTel, "1", "06/06/2020 12:12:12");
    objScheduleTel.setMrHardCycle(12L);
    mrDeviceBusiness.updateStatusAndLastDate(objScheduleTel, "1", "06/06/2020 12:12:12");
  }

  @Test
  public void getDeviceStationCodeCbb() {
    List<MrDeviceDTO> lst = Mockito.spy(ArrayList.class);
    when(mrDeviceRepository.getDeviceStationCodeCbb()).thenReturn(lst);
    List<MrDeviceDTO> result = mrDeviceBusiness.getDeviceStationCodeCbb();
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void approveMrDeviceHard() {
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);

    MrDeviceDTO mrDeviceOld = null;
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);
    mrDeviceBusiness.approveMrDeviceHard(mrDeviceDTO);
  }

  @Test
  public void approveMrDeviceHard1() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setMrHard("1");
    mrDeviceDTO.setApproveStatusHard(1L);

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setMrId(1L);
    List<MrScheduleTelDTO> listSchedule = Mockito.spy(ArrayList.class);
    listSchedule.add(mrScheduleTelDTO);
    when(mrScheduleTelRepository.getListMrScheduleTelDTO(any())).thenReturn(listSchedule);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    when(mrRepository.findMrById(any())).thenReturn(mrInsideDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrRepository.deleteMr(any())).thenReturn(resultInSideDto);
    when(mrHisRepository.insertMrHis(any())).thenReturn(resultInSideDto);

    MrDeviceDTO mrDeviceOld = Mockito.spy(MrDeviceDTO.class);
    mrDeviceOld.setMrConfirmHard("1");
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);
    mrDeviceBusiness.approveMrDeviceHard(mrDeviceDTO);
  }

  @Test
  public void approveMrDeviceHard2() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setMrHard("0");
    mrDeviceDTO.setApproveStatusHard(1L);

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setMrId(1L);
    List<MrScheduleTelDTO> listSchedule = Mockito.spy(ArrayList.class);
    listSchedule.add(mrScheduleTelDTO);
    when(mrScheduleTelRepository.getListMrScheduleTelDTO(any())).thenReturn(listSchedule);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    when(mrRepository.findMrById(any())).thenReturn(mrInsideDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrRepository.deleteMr(any())).thenReturn(resultInSideDto);
    when(mrHisRepository.insertMrHis(any())).thenReturn(resultInSideDto);

    MrDeviceDTO mrDeviceOld = Mockito.spy(MrDeviceDTO.class);
    mrDeviceOld.setMrConfirmHard("1");
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);
    mrDeviceBusiness.approveMrDeviceHard(mrDeviceDTO);
  }

  @Test
  public void approveMrDeviceHard3() {
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setApproveStatusHard(0L);
    mrDeviceDTO.setMrHard("1");

    MrDeviceDTO mrDeviceOld = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);

    mrDeviceBusiness.approveMrDeviceHard(mrDeviceDTO);
  }

  @Test
  public void approveMrDeviceHard4() {
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setApproveStatusHard(0L);
    mrDeviceDTO.setMrHard("0");

    MrDeviceDTO mrDeviceOld = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);

    mrDeviceBusiness.approveMrDeviceHard(mrDeviceDTO);
  }

  @Test
  public void validateImportInfo() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> list = Mockito.spy(ArrayList.class);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setMarketName("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setMarketCode("1");
    mrDeviceDTO.setDeviceId(1L);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setDeviceId(null);
    String a = "";
    for (int i = 0; i < 101; i++) {
      a += "t";
    }
    mrDeviceDTO.setRegionHard(a);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setDeviceId(1L);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setArrayCodeStr("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setArrayCode("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setNetworkTypeStr("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    MrDeviceDTO networkCheck1 = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceRepository.checkNetworkTypeByArrayCode(any(), any())).thenReturn(networkCheck1);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setDeviceTypeStr("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    MrDeviceDTO deviceCheck = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceRepository.checkDeviceTypeByArrayNet(any(), any(), any())).thenReturn(deviceCheck);
    String b = "";
    for (int j = 0; j < 2001; j++) {
      b += "t";
    }
    mrDeviceDTO.setNodeIp(b);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setNodeIp("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setNodeCode(a);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setNodeCode("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setDeviceName(b);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setDeviceName(a);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setVendor(a);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setVendor("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setStatusStr("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setStatus("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setStationCode(b);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setStationCode(a);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setIsEnable(0L);
    List<UsersInsideDto> userDTOList = Mockito.spy(ArrayList.class);
    userDTOList.add(usersInsideDto);
    when(userRepository.getListUsersDTOS(any())).thenReturn(userDTOList);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);

    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> userDTOList1 = Mockito.spy(ArrayList.class);
    userDTOList1.add(usersInsideDto);
    when(userRepository.getListUsersDTOS(any())).thenReturn(userDTOList);
    mrDeviceDTO.setMrConfirmHardStr("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);

    mrDeviceDTO.setMrConfirmHard("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setMrHardStr("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setMrHard("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setMrHard("0");
    mrDeviceDTO.setMrConfirmHard(null);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setMrConfirmHard("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setBoUnitHard(1L);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);

    UnitDTO unit = Mockito.spy(UnitDTO.class);
    when(unitRepository.findUnitById(any())).thenReturn(unit);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);

    unit.setStatus(1L);
    when(unitRepository.findUnitById(any())).thenReturn(unit);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setMrTypeStr("1");
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
    mrDeviceDTO.setMrTypeStr(null);
    mrDeviceBusiness.validateImportInfo(mrDeviceDTO, list);
  }

  @Test
  public void validateDuplicate() {
    PowerMockito.mockStatic(I18n.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setResultImport("1");
    mrDeviceDTO.setMarketCode("1");
    mrDeviceDTO.setNodeCode("1");
    List<MrDeviceDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(I18n.getLanguage("mrDevice.result.import")).thenReturn("1");
    list.add(mrDeviceDTO);
    PowerMockito.when(I18n.getLanguage("mrDevice.err.dup-code-in-file")).thenReturn("1");
    mrDeviceBusiness.validateDuplicate(list, mrDeviceDTO);
  }

  @Test
  public void insertImport() {
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setDeviceId(1L);
    mrDeviceDTO.setApproveStatusHard(1L);
    mrDeviceDTO.setMrDeviceOld(mrDeviceDTO);
    mrDeviceDTO.setRegionHard("1");
    mrDeviceDTO.setStationCode("1");
    mrDeviceDTO.setUserMrHard("1");
    mrDeviceDTO.setMrHard("1");
    List<MrDeviceDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    mrDeviceDTOList.add(mrDeviceDTO);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(TicketProvider.class);
    when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrDeviceDTO mrDeviceOld = Mockito.spy(MrDeviceDTO.class);
    mrDeviceOld.setUserMrHard("1");
    mrDeviceOld.setStationCode("1");
    mrDeviceOld.setRegionHard("1");
    when(mrDeviceSoftRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);
    mrDeviceBusiness.insertImport(mrDeviceDTOList);
  }

  @Test
  public void genCrNumber() {
    mrDeviceBusiness.genCrNumber(1L, null, "1");
  }

  @Test
  public void genCrNumber1() {
    mrDeviceBusiness.genCrNumber(1L, "1", "1");
  }
}
