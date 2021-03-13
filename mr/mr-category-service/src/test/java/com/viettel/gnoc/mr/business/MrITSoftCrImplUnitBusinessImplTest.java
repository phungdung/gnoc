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
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrITSoftCrImplUnitDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrITSoftCrImplUnitRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@PrepareForTest({MrITSoftCrImplUnitBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrITSoftCrImplUnitBusinessImplTest {

  @InjectMocks
  MrITSoftCrImplUnitBusinessImpl mrITSoftCrImplUnitBusiness;

  @Mock
  MrITSoftCrImplUnitRepository mrITSoftCrImplUnitRepository;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  MrITSoftScheduleBusiness mrITSoftScheduleBusiness;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrITSoftCrImplUnitBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void getListDataMrCfgCrUnitITSoft() {
    MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    mrITSoftCrImplUnitDTO.setMarketCode("1");
    mrITSoftCrImplUnitDTO.setImplementUnit("1");
    mrITSoftCrImplUnitDTO.setManageUnit("1");
    mrITSoftCrImplUnitDTO.setCheckingUnit("1");

    List<MrITSoftCrImplUnitDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrITSoftCrImplUnitDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(list);
    when(mrITSoftCrImplUnitRepository.getListDataMrCfgCrUnitITSoft(any())).thenReturn(datatable);

    List<ItemDataCRInside> listMar = Mockito.spy(ArrayList.class);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMar);

    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);

    Datatable result = mrITSoftCrImplUnitBusiness
        .getListDataMrCfgCrUnitITSoft(mrITSoftCrImplUnitDTO);
    assertEquals(datatable.getPages(), result.getPages());

  }

  @Test
  public void insertMrCfgCrUnitIT() {
    MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrITSoftCrImplUnitRepository.insertOrUpdateMrCfgCrUnitIT(any()))
        .thenReturn(resultInSideDto);

    ResultInSideDto result = mrITSoftCrImplUnitBusiness.insertMrCfgCrUnitIT(mrITSoftCrImplUnitDTO);
    assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void updateMrCfgCrUnitIT() {
    MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrITSoftCrImplUnitRepository.insertOrUpdateMrCfgCrUnitIT(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = mrITSoftCrImplUnitBusiness.updateMrCfgCrUnitIT(mrITSoftCrImplUnitDTO);
    assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void findMrCfgCrUnitITById() {
    MrITSoftCrImplUnitDTO mrCfgCrUnitTelDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    mrCfgCrUnitTelDTO.setMarketCode("1");
    mrCfgCrUnitTelDTO.setImplementUnit("1");
    mrCfgCrUnitTelDTO.setManageUnit("1");
    mrCfgCrUnitTelDTO.setCheckingUnit("1");
    when(mrITSoftCrImplUnitRepository.findMrCfgCrUnitITById(any())).thenReturn(mrCfgCrUnitTelDTO);

    List<ItemDataCRInside> listMar = Mockito.spy(ArrayList.class);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMar);

    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);

    mrITSoftCrImplUnitBusiness.findMrCfgCrUnitITById(1L);

  }

  @Test
  public void deleteMrCfgCrUnitIT() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrITSoftCrImplUnitRepository.deleteMrCfgCrUnitIT(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrITSoftCrImplUnitBusiness.deleteMrCfgCrUnitIT(1L);
    assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void exportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    mrITSoftCrImplUnitDTO.setMarketCode("1");
    mrITSoftCrImplUnitDTO.setImplementUnit("1");
    mrITSoftCrImplUnitDTO.setManageUnit("1");
    mrITSoftCrImplUnitDTO.setCheckingUnit("1");
    List<MrITSoftCrImplUnitDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrITSoftCrImplUnitDTO);
    when(mrITSoftCrImplUnitRepository.getDataExport(mrITSoftCrImplUnitDTO)).thenReturn(list);

    List<ItemDataCRInside> listMar = Mockito.spy(ArrayList.class);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMar);

    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrITSoftCrImplUnitBusiness.exportData(mrITSoftCrImplUnitDTO);
  }

  @Test
  public void handleFileExport() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    List<MrITSoftCrImplUnitDTO> list = Mockito.spy(ArrayList.class);
    String[] columnExport = {"1"};
    String data = "06/06/2020 12:12:12";
    String code = "RESULT_IMPORT";
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    mrITSoftCrImplUnitBusiness.handleFileExport(list, columnExport, data, code);
  }

  @Test
  public void importDataMrCfgCrUnitIT() {
    MultipartFile multipartFile = null;
    mrITSoftCrImplUnitBusiness.importDataMrCfgCrUnitIT(multipartFile);
  }

  @Test
  public void importDataMrCfgCrUnitIT1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] objectImport = new Object[9];
    for (int i = 0; i < objectImport.length; i++) {
      objectImport[i] = "1";
    }
    List<Object[]> dataImport = Mockito.spy(ArrayList.class);
    for (int j = 0; j < 501; j++) {
      dataImport.add(objectImport);
    }
    when(CommonImport.getDataFromExcelFileNew(fileImport, 0, 6,
        0, 7, 1000)).thenReturn(dataImport);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.STT"))
        .thenReturn("mrCfgCrUnitIT.STT");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.marketName"))
        .thenReturn("mrCfgCrUnitIT.marketName");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.region"))
        .thenReturn("mrCfgCrUnitIT.region");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.arrayCode"))
        .thenReturn("mrCfgCrUnitIT.arrayCode");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.deviceTypeId"))
        .thenReturn("mrCfgCrUnitIT.deviceTypeId");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.implementUnit"))
        .thenReturn("mrCfgCrUnitIT.implementUnit");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.checkingUnit"))
        .thenReturn("mrCfgCrUnitIT.checkingUnit");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.manageUnit"))
        .thenReturn("mrCfgCrUnitIT.manageUnit");
    //      </editor-fold>
    //<editor-fold desc dunglv test defaultstate="collapsed">
    Object[] objecttest = {
        I18n.getLanguage("mrCfgCrUnitIT.STT"),
        I18n.getLanguage("mrCfgCrUnitIT.marketName") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.region") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.arrayCode") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.deviceTypeId") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.implementUnit") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.checkingUnit") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.manageUnit") + " (*)"
    };
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objecttest);
    when(CommonImport.getDataFromExcelFile(fileImport, 0, 5,
        0, 7, 1000)).thenReturn(lstHeader);
    //      </editor-fold>
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    mrITSoftCrImplUnitBusiness.importDataMrCfgCrUnitIT(multipartFile);
  }

  @Test
  public void importDataMrCfgCrUnitIT2() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] objectImport = new Object[9];
    for (int i = 0; i < objectImport.length; i++) {
      objectImport[i] = "1";
    }
    List<Object[]> dataImport = Mockito.spy(ArrayList.class);
    for (int j = 0; j < 2; j++) {
      dataImport.add(objectImport);
    }
    when(CommonImport.getDataFromExcelFileNew(fileImport, 0, 6,
        0, 7, 1000)).thenReturn(dataImport);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.STT"))
        .thenReturn("mrCfgCrUnitIT.STT");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.marketName"))
        .thenReturn("mrCfgCrUnitIT.marketName");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.region"))
        .thenReturn("mrCfgCrUnitIT.region");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.arrayCode"))
        .thenReturn("mrCfgCrUnitIT.arrayCode");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.deviceTypeId"))
        .thenReturn("mrCfgCrUnitIT.deviceTypeId");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.implementUnit"))
        .thenReturn("mrCfgCrUnitIT.implementUnit");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.checkingUnit"))
        .thenReturn("mrCfgCrUnitIT.checkingUnit");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.manageUnit"))
        .thenReturn("mrCfgCrUnitIT.manageUnit");
    //      </editor-fold>
    //<editor-fold desc dunglv test defaultstate="collapsed">
    Object[] objecttest = {
        I18n.getLanguage("mrCfgCrUnitIT.STT"),
        I18n.getLanguage("mrCfgCrUnitIT.marketName") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.region") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.arrayCode") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.deviceTypeId") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.implementUnit") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.checkingUnit") + " (*)",
        I18n.getLanguage("mrCfgCrUnitIT.manageUnit") + " (*)"
    };
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objecttest);
    when(CommonImport.getDataFromExcelFile(fileImport, 0, 5,
        0, 7, 1000)).thenReturn(lstHeader);
    //      </editor-fold>

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
    listMarket.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMarket);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness.getListRegionByMrSynItDevices(any())).thenReturn(listRegion);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemCode("1");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable dataArray = Mockito.spy(Datatable.class);
    dataArray.setData(listArray);
    when(catItemRepository.getItemMaster(anyString(),
        anyString(), any(),
        anyString(), anyString())).thenReturn(dataArray);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrITSoftScheduleDTO.setDeviceType("1");
    List<MrSynItDevicesDTO> listDevice = Mockito.spy(ArrayList.class);
    listDevice.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any())).thenReturn(listDevice);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> list = Mockito.spy(ArrayList.class);
    when(unitRepository.getListUnit(new UnitDTO())).thenReturn(list);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrITSoftCrImplUnitRepository.insertOrUpdateMrCfgCrUnitIT(any()))
        .thenReturn(resultInSideDto);

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    mrITSoftCrImplUnitBusiness.importDataMrCfgCrUnitIT(multipartFile);
  }

  @Test
  public void setMapMarket() {
  }

  @Test
  public void setMapUnit() {
  }

  @Test
  public void setMapAll() {
  }

  @Test
  public void setDetailValue() {
    MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    mrITSoftCrImplUnitDTO.setCheckingUnit("1");
    mrITSoftCrImplUnitDTO.setImplementUnit("1");
    mrITSoftCrImplUnitDTO.setManageUnit("1");
    mrITSoftCrImplUnitDTO.setMarketCode("1");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);
    mrITSoftCrImplUnitBusiness.setMapUnit();

    mrITSoftCrImplUnitBusiness.setDetailValue(mrITSoftCrImplUnitDTO);
  }

  @Test
  public void getTemplateImport() throws Exception {
    PowerMockito.mockStatic(I18n.class);

    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.STT"))
        .thenReturn("mrCfgCrUnitIT.STT");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.marketName"))
        .thenReturn("mrCfgCrUnitIT.marketName");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.region"))
        .thenReturn("mrCfgCrUnitIT.region");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.arrayCode"))
        .thenReturn("mrCfgCrUnitIT.arrayCode");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.deviceTypeId"))
        .thenReturn("mrCfgCrUnitIT.deviceTypeId");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.implementUnit"))
        .thenReturn("mrCfgCrUnitIT.implementUnit");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.checkingUnit"))
        .thenReturn("mrCfgCrUnitIT.checkingUnit");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.manageUnit"))
        .thenReturn("mrCfgCrUnitIT.manageUnit");

    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.headerMarketCode"))
        .thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.headerMarketName"))
        .thenReturn("2");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.headerArrayCode"))
        .thenReturn("3");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.headerArrayName"))
        .thenReturn("4");

    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.unitId"))
        .thenReturn("5");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.unitCode"))
        .thenReturn("6");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.unitName"))
        .thenReturn("7");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.unitParentId"))
        .thenReturn("8");

    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.headerMarketCode"))
        .thenReturn("9");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.headerMarketName"))
        .thenReturn("9");
    PowerMockito.when(I18n.getLanguage("mrCfgCrUnitIT.import.sheetName"))
        .thenReturn("10");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    List<ItemDataCRInside> listMarket = Mockito.spy(ArrayList.class);
    listMarket.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(listMarket);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    when(unitRepository.getListUnit(null)).thenReturn(listUnit);

    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleBusiness.getListRegionByMrSynItDevices(any())).thenReturn(listRegion);

    mrITSoftCrImplUnitBusiness.getTemplateImport();
  }

  @Test
  public void validateImportInfo0() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    Map<String, String> mapImportDTO = new HashMap<>();
//
    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setMarketName("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo1() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setMarketName("1");
    importDTO.setRegion("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo2() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setMarketName("1");
    importDTO.setRegion("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo3() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setMarketName("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo4() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setRegion("1");
    importDTO.setMarketName("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo5() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setRegion("1");
    importDTO.setArrayName("1");
    importDTO.setMarketName("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
//    catItemDTO.setItemName("1");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo6() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setRegion("1");
    importDTO.setArrayName("1");
    importDTO.setMarketName("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo7() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setRegion("1");
    importDTO.setArrayName("1");
    importDTO.setMarketName("1");
    importDTO.setDeviceTypeId("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemCode("1");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    List<MrSynItDevicesDTO> listDevice = Mockito.spy(ArrayList.class);
    listDevice.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any())).thenReturn(listDevice);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo8() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setRegion("1");
    importDTO.setArrayName("1");
    importDTO.setMarketName("1");
    importDTO.setDeviceTypeId("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemCode("1");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("1");
    List<MrSynItDevicesDTO> listDevice = Mockito.spy(ArrayList.class);
    listDevice.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any())).thenReturn(listDevice);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo9() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setRegion("1");
    importDTO.setArrayName("1");
    importDTO.setMarketName("1");
    importDTO.setImplementUnitName("1");
    importDTO.setDeviceTypeId("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);
//
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemCode("1");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("1");
    List<MrSynItDevicesDTO> listDevice = Mockito.spy(ArrayList.class);
    listDevice.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any())).thenReturn(listDevice);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(2L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);
    mrITSoftCrImplUnitBusiness.setMapUnit();

    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo10() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setRegion("1");
    importDTO.setArrayName("1");
    importDTO.setMarketName("1");
    importDTO.setImplementUnitName("1");
    importDTO.setDeviceTypeId("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);
//
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemCode("1");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("1");
    List<MrSynItDevicesDTO> listDevice = Mockito.spy(ArrayList.class);
    listDevice.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any())).thenReturn(listDevice);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);
    mrITSoftCrImplUnitBusiness.setMapUnit();

    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo11() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setRegion("1");
    importDTO.setArrayName("1");
    importDTO.setMarketName("1");
    importDTO.setImplementUnitName("1");
    importDTO.setDeviceTypeId("1");
    importDTO.setCheckingUnitName("2");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);
//
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemCode("1");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("1");
    List<MrSynItDevicesDTO> listDevice = Mockito.spy(ArrayList.class);
    listDevice.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any())).thenReturn(listDevice);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);
    mrITSoftCrImplUnitBusiness.setMapUnit();

    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo12() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setRegion("1");
    importDTO.setArrayName("1");
    importDTO.setMarketName("1");
    importDTO.setImplementUnitName("1");
    importDTO.setDeviceTypeId("1");
    importDTO.setCheckingUnitName("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);
//
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemCode("1");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("1");
    List<MrSynItDevicesDTO> listDevice = Mockito.spy(ArrayList.class);
    listDevice.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any())).thenReturn(listDevice);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);
    mrITSoftCrImplUnitBusiness.setMapUnit();

    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo13() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setRegion("1");
    importDTO.setArrayName("1");
    importDTO.setMarketName("1");
    importDTO.setImplementUnitName("1");
    importDTO.setDeviceTypeId("1");
    importDTO.setCheckingUnitName("1");
    importDTO.setManageUnitName("1");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);
//
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemCode("1");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("1");
    List<MrSynItDevicesDTO> listDevice = Mockito.spy(ArrayList.class);
    listDevice.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any())).thenReturn(listDevice);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);
    mrITSoftCrImplUnitBusiness.setMapUnit();

    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }

  @Test
  public void validateImportInfo14() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    importDTO.setRegion("1");
    importDTO.setArrayName("1");
    importDTO.setMarketName("1");
    importDTO.setImplementUnitName("1");
    importDTO.setDeviceTypeId("1");
    importDTO.setCheckingUnitName("1");
    importDTO.setManageUnitName("2");

    Map<String, String> mapImportDTO = new HashMap<>();
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> list = Mockito.spy(ArrayList.class);
    list.add(itemDataCRInside);
    when(catLocationRepository.getListLocationByLevelCBB(null, 1L, null)).thenReturn(list);
    mrITSoftCrImplUnitBusiness.setMapMarket();

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> listRegion = Mockito.spy(ArrayList.class);
    listRegion.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices(any())).thenReturn(listRegion);
//
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemCode("1");
    List<CatItemDTO> listArray = Mockito.spy(ArrayList.class);
    listArray.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listArray);
    when(catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("1");
    List<MrSynItDevicesDTO> listDevice = Mockito.spy(ArrayList.class);
    listDevice.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any())).thenReturn(listDevice);
    mrITSoftCrImplUnitBusiness.setMapAll();
//
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> listUnit = Mockito.spy(ArrayList.class);
    listUnit.add(unitDTO);
    when(unitRepository.getListUnit(any())).thenReturn(listUnit);
    mrITSoftCrImplUnitBusiness.setMapUnit();

    mrITSoftCrImplUnitBusiness.validateImportInfo(importDTO, mapImportDTO, "1");
  }


  @Test
  public void validateDuplicateImport() {
    MrITSoftCrImplUnitDTO importDTO = Mockito.spy(MrITSoftCrImplUnitDTO.class);
    Map<String, String> mapString = new HashMap<>();
    mrITSoftCrImplUnitBusiness.validateDuplicateImport(importDTO, mapString, "1");
  }
}
