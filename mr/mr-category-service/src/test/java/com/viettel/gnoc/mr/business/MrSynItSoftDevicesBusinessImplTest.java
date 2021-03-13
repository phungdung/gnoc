
package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftCatMarketDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrConfigRepository;
import com.viettel.gnoc.mr.repository.MrHisRepository;
import com.viettel.gnoc.mr.repository.MrITHisRepository;
import com.viettel.gnoc.mr.repository.MrITSoftCatMarketRepository;
import com.viettel.gnoc.mr.repository.MrITSoftScheduleRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    TicketProvider.class, DataUtil.class, CommonExport.class, ExcelWriterUtils.class, XSSFWorkbook.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrSynItSoftDevicesBusinessImplTest {

  @InjectMocks
  MrSynItSoftDevicesBusinessImpl mrSynItSoftDevicesBusiness;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  MrConfigRepository repository;

  @Mock
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  MrITSoftCatMarketRepository mrITSoftCatMarketRepository;

  @Mock
  MrScheduleTelRepository mrScheduleTelRepository;

  @Mock
  CatItemBusiness catItemBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UserRepository userRepository;

  @Mock
  MrITSoftScheduleRepository mrITSoftScheduleRepository;

  @Mock
  MrRepository mrRepository;

  @Mock
  MrITHisRepository mrITHisRepository;

  @Mock
  MrHisRepository mrHisRepository;

  @Mock
  CrServiceProxy crServiceProxy;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrSynItSoftDevicesBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void getListMrSynITDeviceSoftDTO() {
    PowerMockito.mockStatic(I18n.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lstCd);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("1");
    mrConfigDTO.setConfigName("1");
    List<MrConfigDTO> lstreasonBdSoft = Mockito.spy(ArrayList.class);
    lstreasonBdSoft.add(mrConfigDTO);
    when(repository.getConfigByGroup("LY_DO_KO_BD")).thenReturn(lstreasonBdSoft);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setMrConfirmSoft("1");
    mrSynItDevicesDTO.setMrSoft("1");
    mrSynItDevicesDTO.setStatus("1");
    mrSynItDevicesDTO.setCdId("1");
    mrSynItDevicesDTO.setMarketCode("1");
    mrSynItDevicesDTO.setLevelImportant("1");
    mrSynItDevicesDTO.setBoUnit("1");
    List<MrSynItDevicesDTO> result = Mockito.spy(ArrayList.class);
    result.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListMrSynITDeviceSoftDTO(any(), anyInt(), anyInt()))
        .thenReturn(result);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> lsUnit = Mockito.spy(ArrayList.class);
    lsUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(lsUnit);

    MrSynItDevicesDTO dto = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItSoftDevicesBusiness.getListMrSynITDeviceSoftDTO(dto, 2, 3);
  }

  @Test
  public void getListMrSynITDeviceSoftDTO1() {
    PowerMockito.mockStatic(I18n.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lstCd);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("1");
    mrConfigDTO.setConfigName("1");
    List<MrConfigDTO> lstreasonBdSoft = Mockito.spy(ArrayList.class);
    lstreasonBdSoft.add(mrConfigDTO);
    when(repository.getConfigByGroup("LY_DO_KO_BD")).thenReturn(lstreasonBdSoft);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setMrConfirmSoft("1");
    mrSynItDevicesDTO.setMrSoft("0");
    mrSynItDevicesDTO.setStatus("0");
    mrSynItDevicesDTO.setCdId("1");
    mrSynItDevicesDTO.setMarketCode("1");
    mrSynItDevicesDTO.setLevelImportant("1");
    mrSynItDevicesDTO.setBoUnit("1");
    List<MrSynItDevicesDTO> result = Mockito.spy(ArrayList.class);
    result.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListMrSynITDeviceSoftDTO(any(), anyInt(), anyInt()))
        .thenReturn(result);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> lsUnit = Mockito.spy(ArrayList.class);
    lsUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(lsUnit);

    MrSynItDevicesDTO dto = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItSoftDevicesBusiness.getListMrSynITDeviceSoftDTO(dto, 2, 3);
  }

  @Test
  public void getMapImportantLevel() {
    PowerMockito.mockStatic(I18n.class);
    mrSynItSoftDevicesBusiness.getMapImportantLevel();
  }

  @Test
  public void getListUnit() {
  }

  @Test
  public void setMapWoCd() {
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lstCd);
    mrSynItSoftDevicesBusiness.setMapWoCd();
  }

  @Test
  public void setMapMarketName() {
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);

    MrITSoftCatMarketDTO mrITSoftCatMarketDTO = Mockito.spy(MrITSoftCatMarketDTO.class);
    mrITSoftCatMarketDTO.setCountryCode("1");
    List<MrITSoftCatMarketDTO> mrITSoftCatMarketDTOS = Mockito.spy(ArrayList.class);
    mrITSoftCatMarketDTOS.add(mrITSoftCatMarketDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(mrITSoftCatMarketDTOS);
    when(mrITSoftCatMarketRepository.getListMrCatMarketSearch(any())).thenReturn(datatable);
    mrSynItSoftDevicesBusiness.setMapMarketName();
  }

  @Test
  public void setMrConfirmHardStr() {
    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("1");
    mrConfigDTO.setConfigName("1");
    List<MrConfigDTO> lstMrConfirmHardStr = Mockito.spy(ArrayList.class);
    lstMrConfirmHardStr.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstMrConfirmHardStr);
    mrSynItSoftDevicesBusiness.setMrConfirmSoftStr();
  }

  @Test
  public void setMapArray() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemCode("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    when(
        catItemBusiness.getListItemByCategoryAndParent(Constants.MR_ITEM_NAME.MR_SUBCATEGORY, null))
        .thenReturn(lstArrayCode);
    mrSynItSoftDevicesBusiness.setMapArray();
  }

  @Test
  public void getListDeviceTypeByArrayCode() {
    List<MrSynItDevicesDTO> lst = Mockito.spy(ArrayList.class);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any())).thenReturn(lst);
    List<MrSynItDevicesDTO> result = mrSynItSoftDevicesBusiness
        .getListDeviceTypeByArrayCode("test");
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void findMrDeviceByObjectId() {
    MrSynItDevicesDTO dto = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dto);
    MrSynItDevicesDTO result = mrSynItSoftDevicesBusiness.findMrDeviceByObjectId(dto);
    assertEquals(dto, result);
  }

  @Test
  public void insertOrUpdateListDevice() {
    List<MrSynItDevicesDTO> lstMrDeviceDto = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrSynItSoftDevicesRepository.updateList(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrSynItSoftDevicesBusiness.insertOrUpdateListDevice(lstMrDeviceDto);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void getDeviceITStationCodeCbb() {
    List<MrSynItDevicesDTO> lst = Mockito.spy(ArrayList.class);
    when(mrSynItSoftDevicesRepository.getDeviceITStationCodeCbb()).thenReturn(lst);
    List<MrSynItDevicesDTO> result = mrSynItSoftDevicesBusiness.getDeviceITStationCodeCbb();
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void onSearchEntity() {
    MrSynItDevicesDTO mrDeviceDTO = Mockito.spy(MrSynItDevicesDTO.class);

    List<MrSynItDevicesDTO> lst = Mockito.spy(ArrayList.class);
    when(mrSynItSoftDevicesRepository
        .onSearchEntity(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lst);
    List<MrSynItDevicesDTO> result = mrSynItSoftDevicesBusiness
        .onSearchEntity(mrDeviceDTO, 1, 2, "test", "test1");
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void getMrSynItDevicesDetail() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrSynItDevicesDTO dto = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItSoftDevicesRepository.getMrSynItDevicesDetail(any())).thenReturn(dto);
    mrSynItSoftDevicesBusiness.getMrSynItDevicesDetail(1L);
  }

//  @Test
//  public void exportData() throws Exception {
//    PowerMockito.mockStatic(I18n.class);
//    PowerMockito.mockStatic(CommonExport.class);
//    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
//    mrSynItDevicesDTO.setStatus("0");
//    mrSynItDevicesDTO.setMrSoft("1");
//
//    List<MrSynItDevicesDTO> mrSynITDeviceSoftList = Mockito.spy(ArrayList.class);
//    mrSynITDeviceSoftList.add(mrSynItDevicesDTO);
//    when(mrSynItSoftDevicesRepository.getListMrSynItDeviceSoftExport(any()))
//        .thenReturn(mrSynITDeviceSoftList);
//
//    File fileExportSuccess = new File("./test_junit/test.txt");
//    PowerMockito.mockStatic(CommonExport.class);
//    PowerMockito.when(CommonExport
//        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
//        .thenReturn(fileExportSuccess);
//
//    mrSynItSoftDevicesBusiness.ExportData(mrSynItDevicesDTO);
//  }
//
//  @Test
//  public void exportData1() throws Exception {
//    PowerMockito.mockStatic(I18n.class);
//    PowerMockito.mockStatic(CommonExport.class);
//    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
//    mrSynItDevicesDTO.setStatus("1");
//    mrSynItDevicesDTO.setMrSoft("0");
//
//    List<MrSynItDevicesDTO> mrSynITDeviceSoftList = Mockito.spy(ArrayList.class);
//    mrSynITDeviceSoftList.add(mrSynItDevicesDTO);
//    when(mrSynItSoftDevicesRepository.getListMrSynItDeviceSoftExport(any()))
//        .thenReturn(mrSynITDeviceSoftList);
//
//    File fileExportSuccess = new File("./test_junit/test.txt");
//    PowerMockito.mockStatic(CommonExport.class);
//    PowerMockito.when(CommonExport
//        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
//        .thenReturn(fileExportSuccess);
//
//    mrSynItSoftDevicesBusiness.ExportData(mrSynItDevicesDTO);
//  }

  @Test
  public void getListMrSynItDeviceSoftDTO() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lstCd);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("1");
    mrConfigDTO.setConfigName("1");
    List<MrConfigDTO> lstreasonBdSoft = Mockito.spy(ArrayList.class);
    lstreasonBdSoft.add(mrConfigDTO);
    when(repository.getConfigByGroup(anyString())).thenReturn(lstreasonBdSoft);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> lsUnit = Mockito.spy(ArrayList.class);
    lsUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(lsUnit);

    MrSynItDevicesDTO mrSynItDevicesDTO1 = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO1.setMrHard("0");
    mrSynItDevicesDTO1.setStatus("0");
    mrSynItDevicesDTO1.setMarketCode("1");
    mrSynItDevicesDTO1.setMrConfirmHard("1");
    mrSynItDevicesDTO1.setLevelImportant("1");
    mrSynItDevicesDTO1.setBoUnit("1");
    mrSynItDevicesDTO1.setApproveStatus("0");
    List<MrSynItDevicesDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    mrDeviceDTOList.add(mrSynItDevicesDTO1);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(mrDeviceDTOList);
    when(mrSynItSoftDevicesRepository.getListMrSynItDeviceSoftDTO(any())).thenReturn(datatable);

    UnitDTO unitDTO1 = Mockito.spy(UnitDTO.class);
    unitDTO1.setIsCommittee(0L);
    when(unitRepository.findUnitById(any())).thenReturn(unitDTO1);

    when(userRepository.checkRoleOfUser("TP", userToken.getUserID())).thenReturn(true);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItSoftDevicesBusiness.getListMrSynItDeviceSoftDTO(mrSynItDevicesDTO);
  }

  @Test
  public void getListMrSynItDeviceSoftDTO1() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lstCd);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("1");
    mrConfigDTO.setConfigName("1");
    List<MrConfigDTO> lstreasonBdSoft = Mockito.spy(ArrayList.class);
    lstreasonBdSoft.add(mrConfigDTO);
    when(repository.getConfigByGroup(anyString())).thenReturn(lstreasonBdSoft);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> lsUnit = Mockito.spy(ArrayList.class);
    lsUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(lsUnit);

    MrSynItDevicesDTO mrSynItDevicesDTO1 = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO1.setMrHard("1");
    mrSynItDevicesDTO1.setStatus("1");
    mrSynItDevicesDTO1.setMarketCode("1");
    mrSynItDevicesDTO1.setMrConfirmHard("1");
    mrSynItDevicesDTO1.setLevelImportant("1");
    mrSynItDevicesDTO1.setBoUnit("1");
    mrSynItDevicesDTO1.setApproveStatus("0");
    List<MrSynItDevicesDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    mrDeviceDTOList.add(mrSynItDevicesDTO1);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(mrDeviceDTOList);
    when(mrSynItSoftDevicesRepository.getListMrSynItDeviceSoftDTO(any())).thenReturn(datatable);

    UnitDTO unitDTO1 = Mockito.spy(UnitDTO.class);
    unitDTO1.setIsCommittee(0L);
    when(unitRepository.findUnitById(any())).thenReturn(unitDTO1);

    when(userRepository.checkRoleOfUser("TP", userToken.getUserID())).thenReturn(true);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItSoftDevicesBusiness.getListMrSynItDeviceSoftDTO(mrSynItDevicesDTO);
  }

  @Test
  public void setDetailValue() {
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setApproveStatus("0");
    mrSynItDevicesDTO.setCdId("1");

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupName("1");
    woCdGroupInsideDTO.setWoGroupId(1L);
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lstCd);
    mrSynItSoftDevicesBusiness.setMapWoCd();
//    mrSynItSoftDevicesBusiness.setDetailValue(mrSynItDevicesDTO);
  }

  @Test
  public void deleteMrSynItSoftDevice() {
    PowerMockito.mockStatic(I18n.class);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setId("1");

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrId("1");
    List<MrITSoftScheduleDTO> schedules = Mockito.spy(ArrayList.class);
    schedules.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleRepository.getListScheduleMove(any())).thenReturn(schedules);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITSoftScheduleRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);
    when(mrSynItSoftDevicesRepository.deleteMrSynItSoftDevice(any())).thenReturn(resultInSideDto);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    when(mrRepository.findMrById(mrInsideDTO.getMrId())).thenReturn(mrInsideDTO);

    mrSynItSoftDevicesBusiness.deleteMrSynItSoftDevice(mrSynItDevicesDTO);
  }

  @Test
  public void insertMrScheduleTelHis() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setDeviceId("1");
    mrITSoftScheduleDTO.setMrId("1");
    mrITSoftScheduleDTO.setCrId("1");
    mrITSoftScheduleDTO.setProcedureId("1");
    List<MrITSoftScheduleDTO> listScheduleIt = Mockito.spy(ArrayList.class);
    listScheduleIt.add(mrITSoftScheduleDTO);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    when(mrRepository.findMrById(mrInsideDTO.getMrId())).thenReturn(mrInsideDTO);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setCheckNode("1");

    mrSynItSoftDevicesBusiness.insertMrScheduleITHis(listScheduleIt, mrSynItDevicesDTO);
  }

  @Test
  public void insertMrScheduleTelHis1() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setDeviceId("1");
    mrITSoftScheduleDTO.setMrId("1");
    mrITSoftScheduleDTO.setCrId("1");
    mrITSoftScheduleDTO.setProcedureId("1");
    List<MrITSoftScheduleDTO> listScheduleIt = Mockito.spy(ArrayList.class);
    listScheduleIt.add(mrITSoftScheduleDTO);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    when(mrRepository.findMrById(mrInsideDTO.getMrId())).thenReturn(mrInsideDTO);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);

    mrSynItSoftDevicesBusiness.insertMrScheduleITHis(listScheduleIt, mrSynItDevicesDTO);
  }

  @Test
  public void genCrNumber() {
    PowerMockito.mockStatic(I18n.class);
    mrSynItSoftDevicesBusiness.genCrNumber(1L, null, "tes1");
  }

  @Test
  public void exportFileTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    List<MrSynItDevicesDTO> dtoList = Mockito.spy(ArrayList.class);
    mrSynItSoftDevicesBusiness.exportFileTemplate(dtoList, "RESULT_IMPORT");
  }

//  @Test
//  public void exportFileTemplate1() throws Exception {
//    PowerMockito.mockStatic(CommonExport.class);
//    PowerMockito.mockStatic(ExcelWriterUtils.class);
//    PowerMockito.mockStatic(XSSFWorkbook.class);
//    PowerMockito.mockStatic(I18n.class);
//    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
//    List<MrSynItDevicesDTO> dtoList = Mockito.spy(ArrayList.class);
//    mrSynItSoftDevicesBusiness.exportFileTemplate(dtoList, "test");
//  }

  @Test
  public void readerHeaderSheet() {
    String[] testList = {"t", "t"};
    List<ConfigHeaderExport> lstHeaderSheet = Mockito.spy(ArrayList.class);
    mrSynItSoftDevicesBusiness.readerHeaderSheet();
  }

  @Test
  public void approveItSoftDevice() {
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setId("1");
    MrSynItDevicesDTO mrDeviceOld = null;
    when(mrSynItSoftDevicesRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);
  }

  @Test
  public void approveItSoftDevice1() {
    PowerMockito.mockStatic(I18n.class);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setApproveStatus("1");
    mrSynItDevicesDTO.setId("1");
    mrSynItDevicesDTO.setMrSoft("0");

    MrSynItDevicesDTO mrDeviceOld = Mockito.spy(MrSynItDevicesDTO.class);
    mrDeviceOld.setMrConfirmSoft("1");
    mrDeviceOld.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrId("1");
    List<MrITSoftScheduleDTO> listSchedule = Mockito.spy(ArrayList.class);
    listSchedule.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleRepository.getListScheduleMove(any())).thenReturn(listSchedule);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    when(mrRepository.findMrById(mrInsideDTO.getMrId())).thenReturn(mrInsideDTO);

    MrInsideDTO mrInsideDTO1 = Mockito.spy(MrInsideDTO.class);
    when(mrRepository.findMrById(any())).thenReturn(mrInsideDTO1);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrRepository.deleteMr(any())).thenReturn(resultInSideDto);
    when(mrHisRepository.insertMrHis(any())).thenReturn(resultInSideDto);

    mrSynItSoftDevicesBusiness.approveItSoftDevice(mrSynItDevicesDTO);
  }

  @Test
  public void approveItSoftDevice2() {
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setApproveStatus("0");
    mrSynItDevicesDTO.setId("1");
    MrSynItDevicesDTO mrDeviceOld = null;
    when(mrSynItSoftDevicesRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);
  }

  @Test
  public void approveItSoftDevice3() {
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setApproveStatus("0");
    mrSynItDevicesDTO.setId("0");
    MrSynItDevicesDTO mrDeviceOld = null;
    when(mrSynItSoftDevicesRepository.findMrDeviceById(any())).thenReturn(mrDeviceOld);
  }

  @Test
  public void updateMrItSoftDevice() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrSynItDevicesDTO mrItDeviceOld = Mockito.spy(MrSynItDevicesDTO.class);
    mrItDeviceOld.setRegionSoft("1");
    mrItDeviceOld.setMrSoft("1");
    mrItDeviceOld.setMrConfirmSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceById(any())).thenReturn(mrItDeviceOld);

    List<MrITSoftScheduleDTO> listSchedule = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleRepository.getListScheduleMove(any())).thenReturn(listSchedule);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    when(unitRepository.findUnitById(any())).thenReturn(unitDTO);
    when(userRepository.checkRoleOfUser("TP", userToken.getUserID())).thenReturn(true);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setRegionSoft("1");
    mrSynItDevicesDTO.setMrSoft("0");
    mrSynItDevicesDTO.setId("1");
    mrSynItDevicesDTO.setBoUnit("1");
    mrSynItSoftDevicesBusiness.updateMrItSoftDevice(mrSynItDevicesDTO);
  }

  @Test
  public void updateMrItSoftDevice2() {
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrSynItDevicesDTO mrItDeviceOld = Mockito.spy(MrSynItDevicesDTO.class);
    mrItDeviceOld.setMrSoft("1");
    mrItDeviceOld.setMrConfirmSoft(null);
    when(mrSynItSoftDevicesRepository.findMrDeviceById(any())).thenReturn(mrItDeviceOld);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setDeviceId("1");
    mrITSoftScheduleDTO.setMrId("1");
    mrITSoftScheduleDTO.setCrId("1");
    mrITSoftScheduleDTO.setProcedureId("1");
    List<MrITSoftScheduleDTO> listSchedule = Mockito.spy(ArrayList.class);
    listSchedule.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleRepository.getListScheduleMove(any())).thenReturn(listSchedule);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    when(unitRepository.findUnitById(any())).thenReturn(unitDTO);
    when(userRepository.checkRoleOfUser("TP", userToken.getUserID())).thenReturn(true);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    when(mrRepository.findMrById(any())).thenReturn(mrInsideDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrRepository.deleteMr(any())).thenReturn(resultInSideDto);
    when(mrHisRepository.insertMrHis(any())).thenReturn(resultInSideDto);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setMrSoft("0");
    mrSynItDevicesDTO.setId("1");
    mrSynItDevicesDTO.setBoUnit("1");
    mrSynItDevicesDTO.setStatus("0");
    mrSynItSoftDevicesBusiness.updateMrItSoftDevice(mrSynItDevicesDTO);
  }

  @Test
  public void inActive() {
    PowerMockito.mockStatic(I18n.class);
    MrSynItDevicesDTO mrSynItDeviceDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDeviceDTO.setStatus("0");
    List<MrITSoftScheduleDTO> inActiveList = Mockito.spy(ArrayList.class);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setCrId("1");
    mrITSoftScheduleDTO.setMrId("1");
    mrITSoftScheduleDTO.setDeviceId("1");
    mrITSoftScheduleDTO.setProcedureId("1");
    List<MrITSoftScheduleDTO> listSchedule = Mockito.spy(ArrayList.class);
    listSchedule.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleRepository.getListScheduleMove(any())).thenReturn(listSchedule);

    mrSynItSoftDevicesBusiness.inActive(mrSynItDeviceDTO, inActiveList);
  }

  @Test
  public void processAttention() {
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setRegionSoft("1");
    mrSynItDevicesDTO.setGroupCode(null);
    mrSynItDevicesDTO.setStation("1");

    MrSynItDevicesDTO mrDeviceOld = Mockito.spy(MrSynItDevicesDTO.class);

    List<MrITSoftScheduleDTO> listScheduleItSoft = Mockito.spy(ArrayList.class);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setCrId("1");
    mrITSoftScheduleDTO.setMrId("1");
    mrITSoftScheduleDTO.setDeviceId("1");
    mrITSoftScheduleDTO.setProcedureId("1");
    List<MrITSoftScheduleDTO> listSchedule = Mockito.spy(ArrayList.class);
    listSchedule.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleRepository.getListScheduleMove(any())).thenReturn(listSchedule);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    mrInsideDTO.setState("6");
    when(mrRepository.findMrById(any())).thenReturn(mrInsideDTO);

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setStatus("8");
    when(crServiceProxy.findCrByIdProxy(any())).thenReturn(crInsiteDTO);

    mrSynItSoftDevicesBusiness.processAttention(mrSynItDevicesDTO, mrDeviceOld, listScheduleItSoft);
  }

  @Test
  public void updateIsComplete() {
    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    List<MrITSoftScheduleDTO> schedules = Mockito.spy(ArrayList.class);
    schedules.add(mrITSoftScheduleDTO);
    MrSynItDevicesDTO dto = Mockito.spy(MrSynItDevicesDTO.class);
    MrSynItDevicesDTO dtoOld = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItSoftDevicesBusiness.updateIsComplete(schedules, dto, dtoOld);
  }

  @Test
  public void updateIsComplete1() {
    List<MrITSoftScheduleDTO> schedules = Mockito.spy(ArrayList.class);
    MrSynItDevicesDTO dto = Mockito.spy(MrSynItDevicesDTO.class);
    MrSynItDevicesDTO dtoOld = Mockito.spy(MrSynItDevicesDTO.class);
    dtoOld.setIsCompleteSoft("1");
    mrSynItSoftDevicesBusiness.updateIsComplete(schedules, dto, dtoOld);

  }

  @Test
  public void convertScheduleTelHis() {
    PowerMockito.spy(I18n.class);
    MrITSoftScheduleDTO dto = Mockito.spy(MrITSoftScheduleDTO.class);
    dto.setCrId("1");
    mrSynItSoftDevicesBusiness.convertScheduleTelHis(dto, 1);
  }

  @Test
  public void getTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    when(
        catItemBusiness.getListItemByCategoryAndParent(Constants.MR_ITEM_NAME.MR_SUBCATEGORY, null))
        .thenReturn(lstArrayCode);

    List<MrITSoftScheduleDTO> lstRegion = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleRepository.getListRegionByMrSynItDevices(any())).thenReturn(lstRegion);

    List<MrSynItDevicesDTO> lstDeviceType = Mockito.spy(ArrayList.class);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any()))
        .thenReturn(lstDeviceType);

    List<MrConfigDTO> lstMrConfirmSoftStr = Mockito.spy(ArrayList.class);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstMrConfirmSoftStr);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(2L);
    unitDTO.setUnitName("2");
    List<UnitDTO> unitNameList = Mockito.spy(ArrayList.class);
    unitNameList.add(unitDTO);
    when(unitRepository.getListUnit(null)).thenReturn(unitNameList);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("2");
    mrConfigDTO.setConfigName("2");
    List<MrConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstConfig);

    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.idStr"))
        .thenReturn("mrSynItSoftDevice.idStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.marketName"))
        .thenReturn("mrSynItSoftDevice.marketName");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.regionSoft"))
        .thenReturn("mrSynItSoftDevice.regionSoft");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.arrayCodeStr"))
        .thenReturn("mrSynItSoftDevice.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.deviceTypeStr"))
        .thenReturn("mrSynItSoftDevice.deviceTypeStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.ipNode"))
        .thenReturn("mrSynItSoftDevice.ipNode");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.objectCode"))
        .thenReturn("mrSynItSoftDevice.objectCode");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.objectName"))
        .thenReturn("mrSynItSoftDevice.objectName");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.createUserSoft"))
        .thenReturn("mrSynItSoftDevice.createUserSoft");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.notes"))
        .thenReturn("mrSynItSoftDevice.notes");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.mrSoftStr"))
        .thenReturn("mrSynItSoftDevice.mrSoftStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.mrConfirmSoftStr"))
        .thenReturn("mrSynItSoftDevice.mrConfirmSoftStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.nodeAffected"))
        .thenReturn("mrSynItSoftDevice.nodeAffected");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.cdIdName"))
        .thenReturn("mrSynItSoftDevice.cdIdName");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.groupCode"))
        .thenReturn("mrSynItSoftDevice.groupCode");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.vendor"))
        .thenReturn("mrSynItSoftDevice.vendor");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.stationStr"))
        .thenReturn("mrSynItSoftDevice.stationStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.statusStr"))
        .thenReturn("mrSynItSoftDevice.statusStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.boUnitName"))
        .thenReturn("mrSynItSoftDevice.boUnitName");
    //      </editor-fold>
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.title"))
        .thenReturn("1a");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.regionSoft"))
        .thenReturn("2a");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.arrDevice"))
        .thenReturn("3a");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.cdIdName"))
        .thenReturn("4a");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.boUnit"))
        .thenReturn("5a");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.mrConfirmName"))
        .thenReturn("6a");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.isRunMopName"))
        .thenReturn("7a");

    mrSynItSoftDevicesBusiness.getTemplate();
  }

  @Test
  public void importData() {
    MultipartFile multipartFile = null;
    mrSynItSoftDevicesBusiness.importData(multipartFile);
  }

  @Test
  public void importData1() {
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSXs");
    mrSynItSoftDevicesBusiness.importData(multipartFile);
  }

  @Test
  public void importData2() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");
    String filePath = "/temps";
    File fileImport = new File(filePath);

    when(FileUtils.saveTempFile(any(), any(), anyString())).thenReturn(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        4,
        0,
        19,
        1000
    )).thenReturn(headerList);

    mrSynItSoftDevicesBusiness.importData(multipartFile);
  }

  @Test
  public void importData3() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");
    String filePath = "/temps";
    File fileImport = new File(filePath);
    Object[] objecttest = new Object[]{};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1501; i++) {
      dataImportList.add(objecttest);
    }
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        19,
        1000
    )).thenReturn(dataImportList);

    when(FileUtils.saveTempFile(any(), any(), anyString())).thenReturn(filePath);

    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.idStr"))
        .thenReturn("mrSynItSoftDevice.idStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.marketName"))
        .thenReturn("mrSynItSoftDevice.marketName");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.regionSoft"))
        .thenReturn("mrSynItSoftDevice.regionSoft");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.arrayCodeStr"))
        .thenReturn("mrSynItSoftDevice.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.deviceTypeStr"))
        .thenReturn("mrSynItSoftDevice.deviceTypeStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.ipNode"))
        .thenReturn("mrSynItSoftDevice.ipNode");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.objectCode"))
        .thenReturn("mrSynItSoftDevice.objectCode");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.objectName"))
        .thenReturn("mrSynItSoftDevice.objectName");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.createUserSoft"))
        .thenReturn("mrSynItSoftDevice.createUserSoft");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.notes"))
        .thenReturn("mrSynItSoftDevice.notes");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.mrSoftStr"))
        .thenReturn("mrSynItSoftDevice.mrSoftStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.mrConfirmSoftStr"))
        .thenReturn("mrSynItSoftDevice.mrConfirmSoftStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.nodeAffected"))
        .thenReturn("mrSynItSoftDevice.nodeAffected");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.cdIdName"))
        .thenReturn("mrSynItSoftDevice.cdIdName");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.groupCode"))
        .thenReturn("mrSynItSoftDevice.groupCode");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.vendor"))
        .thenReturn("mrSynItSoftDevice.vendor");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.stationStr"))
        .thenReturn("mrSynItSoftDevice.stationStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.statusStr"))
        .thenReturn("mrSynItSoftDevice.statusStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.boUnitName"))
        .thenReturn("mrSynItSoftDevice.boUnitName");
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrSynItSoftDevice.idStr"),
        I18n.getLanguage("mrSynItSoftDevice.marketName") + "*",
        I18n.getLanguage("mrSynItSoftDevice.regionSoft"),
        I18n.getLanguage("mrSynItSoftDevice.arrayCodeStr") + "*",
        I18n.getLanguage("mrSynItSoftDevice.deviceTypeStr") + "*",
        I18n.getLanguage("mrSynItSoftDevice.ipNode"),
        I18n.getLanguage("mrSynItSoftDevice.objectCode") + "*",
        I18n.getLanguage("mrSynItSoftDevice.objectName") + "*",
        I18n.getLanguage("mrSynItSoftDevice.createUserSoft") + "*",
        I18n.getLanguage("mrSynItSoftDevice.notes"),
        I18n.getLanguage("mrSynItSoftDevice.mrSoftStr") + "*",
        I18n.getLanguage("mrSynItSoftDevice.mrConfirmSoftStr"),
        I18n.getLanguage("mrSynItSoftDevice.nodeAffected"),
        I18n.getLanguage("mrSynItSoftDevice.cdIdName"),
        I18n.getLanguage("mrSynItSoftDevice.groupCode"),
        I18n.getLanguage("mrSynItSoftDevice.vendor"),
        I18n.getLanguage("mrSynItSoftDevice.stationStr"),
        I18n.getLanguage("mrSynItSoftDevice.statusStr") + "*",
        I18n.getLanguage("mrSynItSoftDevice.boUnitName")
    };
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            19,
            1000
        )).thenReturn(dataHeader);
    //      </editor-fold>

    mrSynItSoftDevicesBusiness.importData(multipartFile);
  }

  @Test
  public void importData4() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    when(multipartFile.getOriginalFilename()).thenReturn("test.XLSX");
    String filePath = "/temps";
    File fileImport = new File(filePath);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1; i++) {
      Object[] objecttest = new Object[20];
      for (int j = 0; j < objecttest.length; j++) {
        if (j == 1) {
          objecttest[j] = "t";
        }
      }
      dataImportList.add(objecttest);
    }
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        19,
        1000
    )).thenReturn(dataImportList);
    when(FileUtils.saveTempFile(any(), any(), anyString())).thenReturn(filePath);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.idStr"))
        .thenReturn("mrSynItSoftDevice.idStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.marketName"))
        .thenReturn("mrSynItSoftDevice.marketName");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.regionSoft"))
        .thenReturn("mrSynItSoftDevice.regionSoft");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.arrayCodeStr"))
        .thenReturn("mrSynItSoftDevice.arrayCodeStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.deviceTypeStr"))
        .thenReturn("mrSynItSoftDevice.deviceTypeStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.ipNode"))
        .thenReturn("mrSynItSoftDevice.ipNode");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.objectCode"))
        .thenReturn("mrSynItSoftDevice.objectCode");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.objectName"))
        .thenReturn("mrSynItSoftDevice.objectName");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.createUserSoft"))
        .thenReturn("mrSynItSoftDevice.createUserSoft");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.notes"))
        .thenReturn("mrSynItSoftDevice.notes");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.mrSoftStr"))
        .thenReturn("mrSynItSoftDevice.mrSoftStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.mrConfirmSoftStr"))
        .thenReturn("mrSynItSoftDevice.mrConfirmSoftStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.nodeAffected"))
        .thenReturn("mrSynItSoftDevice.nodeAffected");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.cdIdName"))
        .thenReturn("mrSynItSoftDevice.cdIdName");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.groupCode"))
        .thenReturn("mrSynItSoftDevice.groupCode");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.vendor"))
        .thenReturn("mrSynItSoftDevice.vendor");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.stationStr"))
        .thenReturn("mrSynItSoftDevice.stationStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.statusStr"))
        .thenReturn("mrSynItSoftDevice.statusStr");
    PowerMockito.when(I18n.getLanguage("mrSynItSoftDevice.boUnitName"))
        .thenReturn("mrSynItSoftDevice.boUnitName");
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrSynItSoftDevice.idStr"),
        I18n.getLanguage("mrSynItSoftDevice.marketName") + "*",
        I18n.getLanguage("mrSynItSoftDevice.regionSoft"),
        I18n.getLanguage("mrSynItSoftDevice.arrayCodeStr") + "*",
        I18n.getLanguage("mrSynItSoftDevice.deviceTypeStr") + "*",
        I18n.getLanguage("mrSynItSoftDevice.ipNode"),
        I18n.getLanguage("mrSynItSoftDevice.objectCode") + "*",
        I18n.getLanguage("mrSynItSoftDevice.objectName") + "*",
        I18n.getLanguage("mrSynItSoftDevice.createUserSoft") + "*",
        I18n.getLanguage("mrSynItSoftDevice.notes"),
        I18n.getLanguage("mrSynItSoftDevice.mrSoftStr") + "*",
        I18n.getLanguage("mrSynItSoftDevice.mrConfirmSoftStr"),
        I18n.getLanguage("mrSynItSoftDevice.nodeAffected"),
        I18n.getLanguage("mrSynItSoftDevice.cdIdName"),
        I18n.getLanguage("mrSynItSoftDevice.groupCode"),
        I18n.getLanguage("mrSynItSoftDevice.vendor"),
        I18n.getLanguage("mrSynItSoftDevice.stationStr"),
        I18n.getLanguage("mrSynItSoftDevice.statusStr") + "*",
        I18n.getLanguage("mrSynItSoftDevice.boUnitName")
    };
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            19,
            1000
        )).thenReturn(dataHeader);
    //      </editor-fold>
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);

    MrITSoftCatMarketDTO mrITSoftCatMarketDTO = Mockito.spy(MrITSoftCatMarketDTO.class);
    mrITSoftCatMarketDTO.setCountryCode("1");
    mrITSoftCatMarketDTO.setMarketCode("1");
    List<MrITSoftCatMarketDTO> mrITSoftCatMarketDTOS = Mockito.spy(ArrayList.class);
    mrITSoftCatMarketDTOS.add(mrITSoftCatMarketDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(mrITSoftCatMarketDTOS);
    when(mrITSoftCatMarketRepository.getListMrCatMarketSearch(any())).thenReturn(datatable);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    when(
        catItemBusiness.getListItemByCategoryAndParent(Constants.MR_ITEM_NAME.MR_SUBCATEGORY, null))
        .thenReturn(lstArrayCode);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigName("1");
    mrConfigDTO.setConfigCode("1");
    List<MrConfigDTO> lstMrConfirmHardStr = Mockito.spy(ArrayList.class);
    lstMrConfirmHardStr.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstMrConfirmHardStr);

    when(I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.0")).thenReturn("1");
    when(I18n.getLanguage("mrSynItSoftDevice.mrSoftStr.1")).thenReturn("2");
    when(I18n.getLanguage("mrSynItSoftDevice.status.1")).thenReturn("3");
    when(I18n.getLanguage("mrSynItSoftDevice.status.0")).thenReturn("4");

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrSynItSoftDevicesBusiness.importData(multipartFile);
  }
}

