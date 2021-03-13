package com.viettel.gnoc.mr.business;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftCatMarketDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrConfigRepository;
import com.viettel.gnoc.mr.repository.MrHisRepository;
import com.viettel.gnoc.mr.repository.MrITHardScheduleRepository;
import com.viettel.gnoc.mr.repository.MrITHisRepository;
import com.viettel.gnoc.mr.repository.MrITSoftCatMarketRepository;
import com.viettel.gnoc.mr.repository.MrITSoftScheduleRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrSynItHardDevicesRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.opensaml.xml.signature.impl.NamedCurveImpl;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockMultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrSynItHardDevicesBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class, DateTimeUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MrSynItHardDevicesBusinessImplTest {

  @InjectMocks
  MrSynItHardDevicesBusinessImpl mrSynItHardDevicesBusiness;

  @Mock
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  MrConfigRepository repository;

  @Mock
  MrRepository mrRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UserRepository userRepository;

  @Mock
  MrSynItHardDevicesRepository mrSynItHardDevicesRepository;

  @Mock
  MrITSoftScheduleRepository mrITSoftScheduleRepository;

  @Mock
  MrITHardScheduleRepository mrITHardScheduleRepository;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  MrITHisRepository mrITHisRepository;

  @Mock
  MrHisRepository mrHisRepository;

  @Mock
  CatItemBusiness catItemBusiness;

  @Mock
  MrITSoftCatMarketRepository mrITSoftCatMarketRepository;

  @Test
  public void testGetListMrSynITDeviceSoftDTO() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(
        I18n.getLanguage("cfgProcedureView.procedureTab.combobox.veryImportal")
    ).thenReturn("1");
    PowerMockito.when(
        I18n.getLanguage("cfgProcedureView.procedureTab.combobox.importal")
    ).thenReturn("2");
    PowerMockito.when(
        I18n.getLanguage("cfgProcedureView.procedureTab.combobox.normal")
    ).thenReturn("3");

    MrSynItDevicesDTO dto = Mockito.spy(MrSynItDevicesDTO.class);
    dto.setMarketCode("1");
    dto.setMrSoft("1");
    dto.setStatus("1");
    dto.setCdId("1");
    dto.setMrConfirmHard("1");
    dto.setLevelImportant("1");
    dto.setBoUnit("1");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    PowerMockito.when(
        catLocationRepository.getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountry);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);
    PowerMockito.when(
        woCategoryServiceProxy
            .getListCdGroupByUser(any())
    ).thenReturn(lstCd);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("1");
    mrConfigDTO.setConfigName("1");
    List<MrConfigDTO> lstreasonBdSoft = Mockito.spy(ArrayList.class);
    lstreasonBdSoft.add(mrConfigDTO);
    PowerMockito.when(
        repository.getConfigByGroup(anyString())
    ).thenReturn(lstreasonBdSoft);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> lsUnit = Mockito.spy(ArrayList.class);
    lsUnit.add(unitDTO);
    PowerMockito.when(
        unitRepository.getListUnit(any())
    ).thenReturn(lsUnit);
    PowerMockito.when(
        unitRepository.getListUnitDTO(any(), anyInt(), anyInt(), anyString(), anyString())
    ).thenReturn(lsUnit);

    List<MrSynItDevicesDTO> result = Mockito.spy(ArrayList.class);
    result.add(dto);
    PowerMockito.when(
        mrSynItSoftDevicesRepository.getListMrSynITDeviceSoftDTO(any(), anyInt(), anyInt())
    ).thenReturn(result);

    List<MrSynItDevicesDTO> actual = mrSynItHardDevicesBusiness
        .getListMrSynITDeviceHardDTO(dto, 1, 1);

    Assert.assertNotNull(actual);
  }

  @Test
  public void testExportData() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");
    PowerMockito.when(
        I18n.getLanguage("cfgProcedureView.procedureTab.combobox.veryImportal")
    ).thenReturn("1");
    PowerMockito.when(
        I18n.getLanguage("cfgProcedureView.procedureTab.combobox.importal")
    ).thenReturn("2");
    PowerMockito.when(
        I18n.getLanguage("cfgProcedureView.procedureTab.combobox.normal")
    ).thenReturn("3");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setStatus("1");
    mrSynItDevicesDTO.setMrHard("1");

    List<MrSynItDevicesDTO> mrSynITDeviceHardList = Mockito.spy(ArrayList.class);
    mrSynITDeviceHardList.add(mrSynItDevicesDTO);
    PowerMockito.when(
        mrSynItSoftDevicesRepository
            .getListMrSynItDeviceSoftExport(any())
    ).thenReturn(mrSynITDeviceHardList);

    File actual = mrSynItHardDevicesBusiness.ExportData(mrSynItDevicesDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void testDeleteMrSynItHardDevice() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");

    org.apache.log4j.Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setObjectId("1");
    mrSynItDevicesDTO.setObjectCode("1");
    mrSynItDevicesDTO.setId("1");
    mrSynItDevicesDTO.setCheckNode("1");

    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setMrId(1L);
    mrITHardScheduleDTO.setMarketCode("1");
    mrITHardScheduleDTO.setArrayCode("1");
    mrITHardScheduleDTO.setDeviceType("1");
    mrITHardScheduleDTO.setDeviceId("1");
    mrITHardScheduleDTO.setDeviceCode("1");
    mrITHardScheduleDTO.setDeviceName("1");
    mrITHardScheduleDTO.setNextDateModify(new Date());
    mrITHardScheduleDTO.setMrContentId("1");
    mrITHardScheduleDTO.setMrType("1");
    mrITHardScheduleDTO.setMrCode("1");
    mrITHardScheduleDTO.setCrId(1L);
    mrITHardScheduleDTO.setWoId(1L);
    mrITHardScheduleDTO.setProcedureId(1L);
    mrITHardScheduleDTO.setProcedureName("1");
    mrITHardScheduleDTO.setNetworkType("1");
    mrITHardScheduleDTO.setCycle(1L);
    mrITHardScheduleDTO.setRegion("1");
    List<MrITHardScheduleDTO> schedules = Mockito.spy(ArrayList.class);
    schedules.add(mrITHardScheduleDTO);
    PowerMockito.when(
        mrITHardScheduleRepository
            .getListScheduleMove(any())
    ).thenReturn(schedules);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        mrITHardScheduleRepository.deleteListSchedule(anyList())
    ).thenReturn(expected);
    PowerMockito.when(
        mrSynItSoftDevicesRepository
            .deleteMrSynItSoftDevice(anyLong())
    ).thenReturn(expected);
    PowerMockito.when(
        mrITHisRepository.insertUpdateListScheduleHis(anyList())
    ).thenReturn(expected);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    PowerMockito.when(
        mrRepository.findMrById(anyLong())
    ).thenReturn(mrInsideDTO);

    ResultInSideDto actual = mrSynItHardDevicesBusiness.deleteMrSynItHardDevice(mrSynItDevicesDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void testGetTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");
    PowerMockito.when(I18n.getLanguage("mrSynItHardDevice.title")).thenReturn("0");
    PowerMockito.when(I18n.getLanguage("mrSynItHardDevice.regionSoft")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("mrSynItHardDevice.arrDevice")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("mrSynItHardDevice.boUnit")).thenReturn("4");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    PowerMockito.when(
        catLocationRepository
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstMarketCode);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    catItemDTO.setItemValue("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    PowerMockito.when(
        catItemBusiness
            .getListItemByCategoryAndParent(anyString(), any())
    ).thenReturn(lstArrayCode);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> lstRegion = Mockito.spy(ArrayList.class);
    lstRegion.add(mrITSoftScheduleDTO);
    PowerMockito.when(
        mrITSoftScheduleRepository
            .getListRegionByMrSynItDevices(anyString())
    ).thenReturn(lstRegion);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrITSoftScheduleDTO.setDeviceType("1");
    List<MrSynItDevicesDTO> lstDeviceType = Mockito.spy(ArrayList.class);
    lstDeviceType.add(mrSynItDevicesDTO);
    PowerMockito.when(
        mrSynItSoftDevicesRepository
            .getListDeviceTypeByArrayCode(anyString())
    ).thenReturn(lstDeviceType);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigName("1");
    List<MrConfigDTO> lstMrConfirmSoftStr = Mockito.spy(ArrayList.class);
    lstMrConfirmSoftStr.add(mrConfigDTO);
    PowerMockito.when(
        repository.getConfigByGroup(anyString())
    ).thenReturn(lstMrConfirmSoftStr);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> unitNameList = Mockito.spy(ArrayList.class);
    unitNameList.add(unitDTO);
    PowerMockito.when(
        unitRepository.getListUnit(any())
    ).thenReturn(unitNameList);

    File actual = mrSynItHardDevicesBusiness.getTemplate();

    Assert.assertNotNull(actual);
  }

  @Test
  public void testImportData_01() {
    ResultInSideDto actual = mrSynItHardDevicesBusiness
        .importData(null);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportData_02() {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );

    ResultInSideDto actual = mrSynItHardDevicesBusiness
        .importData(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportData_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 18, 1000)
    ).thenReturn(headerList);

    ResultInSideDto actual = mrSynItHardDevicesBusiness
        .importData(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORM, actual.getKey());
  }

  @Test
  public void testImportData_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1", "1*", "1", "1*",
        "1*", "1*", "1*", "1", "1",
        "1*", "1", "1", "1", "1*",
        "1", "1*", "1", "1(dd/MM/yyyy)"
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 18, 1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{"1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 1510; i++) {
      dataImportList.add(objects1);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 18, 1000)
    ).thenReturn(dataImportList);

    ResultInSideDto actual = mrSynItHardDevicesBusiness
        .importData(multipartFile);

//    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportData_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1", "1*", "1", "1*",
        "1*", "1*", "1*", "1", "1",
        "1*", "1", "1", "1", "1*",
        "1", "1*", "1", "1(dd/MM/yyyy)"
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 18, 1000)
    ).thenReturn(headerList);

    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 18, 1000)
    ).thenReturn(dataImportList);

    ResultInSideDto actual = mrSynItHardDevicesBusiness
        .importData(multipartFile);

//    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void testImportData_06() throws Exception {
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1", "1*", "1", "1*",
        "1*", "1*", "1*", "1", "1",
        "1*", "1", "1", "1", "1*",
        "1", "1*", "1", "1(dd/MM/yyyy)"
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 18, 1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1"
    };
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 18, 1000)
    ).thenReturn(dataImportList);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    PowerMockito.when(
        catLocationRepository
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstMarketCode);

    MrITSoftCatMarketDTO mrITSoftCatMarketDTO = Mockito.spy(MrITSoftCatMarketDTO.class);
    mrITSoftCatMarketDTO.setCountryCode("1");
    mrITSoftCatMarketDTO.setMarketCode("1");
    List<MrITSoftCatMarketDTO> mrITSoftCatMarketDTOS = Mockito.spy(ArrayList.class);
    mrITSoftCatMarketDTOS.add(mrITSoftCatMarketDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(mrITSoftCatMarketDTOS);
    PowerMockito.when(
        mrITSoftCatMarketRepository
            .getListMrCatMarketSearch(any())
    ).thenReturn(datatable);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    PowerMockito.when(
        catItemBusiness
            .getListItemByCategoryAndParent(anyString(), any())
    ).thenReturn(lstArrayCode);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigName("1");
    List<MrConfigDTO> lstMrConfirmSoftStr = Mockito.spy(ArrayList.class);
    lstMrConfirmSoftStr.add(mrConfigDTO);
    PowerMockito.when(
        repository.getConfigByGroup(anyString())
    ).thenReturn(lstMrConfirmSoftStr);

    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = mrSynItHardDevicesBusiness
        .importData(multipartFile);
//
//    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testImportData_07() throws Exception {
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrSynItHardDevice.mrHardStr.0")).thenReturn("0");
    PowerMockito.when(I18n.getLanguage("mrSynItHardDevice.mrHardStr.1")).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1", "1*", "1", "1*",
        "1*", "1*", "1*", "1", "1",
        "1*", "1", "1", "1", "1*",
        "1", "1*", "1", "1(dd/MM/yyyy)"
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 18, 1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1"
    };
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 18, 1000)
    ).thenReturn(dataImportList);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    PowerMockito.when(
        catLocationRepository
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstMarketCode);

    MrITSoftCatMarketDTO mrITSoftCatMarketDTO = Mockito.spy(MrITSoftCatMarketDTO.class);
    mrITSoftCatMarketDTO.setCountryCode("1");
    mrITSoftCatMarketDTO.setMarketCode("1");
    List<MrITSoftCatMarketDTO> mrITSoftCatMarketDTOS = Mockito.spy(ArrayList.class);
    mrITSoftCatMarketDTOS.add(mrITSoftCatMarketDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(mrITSoftCatMarketDTOS);
    PowerMockito.when(
        mrITSoftCatMarketRepository
            .getListMrCatMarketSearch(any())
    ).thenReturn(datatable);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    PowerMockito.when(
        catItemBusiness
            .getListItemByCategoryAndParent(anyString(), any())
    ).thenReturn(lstArrayCode);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigName("1");
    List<MrConfigDTO> lstMrConfirmSoftStr = Mockito.spy(ArrayList.class);
    lstMrConfirmSoftStr.add(mrConfigDTO);
    PowerMockito.when(
        repository.getConfigByGroup(anyString())
    ).thenReturn(lstMrConfirmSoftStr);

    MrSynItDevicesDTO deviceCheck = Mockito.spy(MrSynItDevicesDTO.class);
    PowerMockito.when(
        mrSynItSoftDevicesRepository
            .checkDeviceTypeByArrayCode(anyString(), anyString())
    ).thenReturn(deviceCheck);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> userDTOList = Mockito.spy(ArrayList.class);
    userDTOList.add(usersInsideDto);
    PowerMockito.when(
        userRepository.getListUsersDTOS(any())
    ).thenReturn(userDTOList);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setStatus(1L);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitDTO);

    MrSynItDevicesDTO mrDevice = Mockito.spy(MrSynItDevicesDTO.class);
    PowerMockito.when(
        mrSynItSoftDevicesRepository
            .findMrDeviceById(anyLong())
    ).thenReturn(mrDevice);

    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = mrSynItHardDevicesBusiness
        .importData(multipartFile);

//    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testImportData_08() throws Exception {
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrSynItHardDevice.mrHardStr.0")).thenReturn("0");
    PowerMockito.when(I18n.getLanguage("mrSynItHardDevice.mrHardStr.1")).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1", "1*", "1", "1*",
        "1*", "1*", "1*", "1", "1",
        "1*", "1", "1", "1", "1*",
        "1", "1*", "1", "1(dd/MM/yyyy)"
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0,
            4,
            0,
            18,
            1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1"
    };
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp,
            0,
            5,
            0,
            18,
            1000)
    ).thenReturn(dataImportList);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    PowerMockito.when(
        catLocationRepository
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstMarketCode);

    MrITSoftCatMarketDTO mrITSoftCatMarketDTO = Mockito.spy(MrITSoftCatMarketDTO.class);
    mrITSoftCatMarketDTO.setCountryCode("1");
    mrITSoftCatMarketDTO.setMarketCode("1");
    List<MrITSoftCatMarketDTO> mrITSoftCatMarketDTOS = Mockito.spy(ArrayList.class);
    mrITSoftCatMarketDTOS.add(mrITSoftCatMarketDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(mrITSoftCatMarketDTOS);
    PowerMockito.when(
        mrITSoftCatMarketRepository
            .getListMrCatMarketSearch(any())
    ).thenReturn(datatable);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    PowerMockito.when(
        catItemBusiness
            .getListItemByCategoryAndParent(anyString(), any())
    ).thenReturn(lstArrayCode);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigName("1");
    List<MrConfigDTO> lstMrConfirmSoftStr = Mockito.spy(ArrayList.class);
    lstMrConfirmSoftStr.add(mrConfigDTO);
    PowerMockito.when(
        repository.getConfigByGroup(anyString())
    ).thenReturn(lstMrConfirmSoftStr);

    MrSynItDevicesDTO deviceCheck = Mockito.spy(MrSynItDevicesDTO.class);
    PowerMockito.when(
        mrSynItSoftDevicesRepository
            .checkDeviceTypeByArrayCode(anyString(), anyString())
    ).thenReturn(deviceCheck);

    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setIsEnable(1L);
    List<UsersInsideDto> userDTOList = Mockito.spy(ArrayList.class);
    userDTOList.add(usersInsideDto);
    PowerMockito.when(
        userRepository.getListUsersDTOS(any())
    ).thenReturn(userDTOList);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setStatus(1L);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitDTO);

    MrSynItDevicesDTO mrDevice = Mockito.spy(MrSynItDevicesDTO.class);
    PowerMockito.when(
        mrSynItSoftDevicesRepository
            .findMrDeviceById(anyLong())
    ).thenReturn(mrDevice);

    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = mrSynItHardDevicesBusiness
        .importData(multipartFile);
  }


  @Test
  public void testupdateMrSynItHardDevice() throws Exception {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setBoUnitHard("1");
    mrSynItDevicesDTO.setMrConfirmHard("1");
    mrSynItDevicesDTO.setId("1");
    mrSynItDevicesDTO.setRegionHard("1");
    mrSynItDevicesDTO.setMrHard("1");
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MrITHardScheduleDTO> listSchedule = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrITHardScheduleRepository.getListScheduleMove(any())
    ).thenReturn(listSchedule);
    PowerMockito.when(
        mrSynItSoftDevicesRepository.findMrDeviceById(anyLong())
    ).thenReturn(mrSynItDevicesDTO);
    mrSynItHardDevicesBusiness.updateMrSynItHardDevice(mrSynItDevicesDTO);

  }

  @Test
  public void testupdateMrSynItHardDevice_01() throws Exception {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setBoUnitHard("1");
//    mrSynItDevicesDTO.setMrConfirmHard("1");
    mrSynItDevicesDTO.setId("1");
    mrSynItDevicesDTO.setRegionHard("1");
    mrSynItDevicesDTO.setMrHard("1");
    MrSynItDevicesDTO mrSynItDevicesDTO1= Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO1.setBoUnitHard("2");
//    mrSynItDevicesDTO1.setMrConfirmHard("1");
    mrSynItDevicesDTO1.setId("2");
    mrSynItDevicesDTO1.setRegionHard("2");
    mrSynItDevicesDTO1.setMrHard("2");
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setCycle(1L);
    List<MrITHardScheduleDTO> listSchedule = Mockito.spy(ArrayList.class);
    listSchedule.add(mrITHardScheduleDTO);
    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setIsCommittee(0L);
    PowerMockito.when(
        unitRepository.findUnitById(anyLong())
    ).thenReturn(unitDTO);
    PowerMockito.when(
        userRepository.checkRoleOfUser(anyString(), anyLong())
    ).thenReturn(true);
    PowerMockito.when(
        userRepository.getUnitParentForApprove(anyString(), any())
    ).thenReturn("1");
    PowerMockito.when(
        mrITHardScheduleRepository.getListScheduleMove(any())
    ).thenReturn(listSchedule);
    PowerMockito.when(
        mrSynItSoftDevicesRepository.findMrDeviceById(anyLong())
    ).thenReturn(mrSynItDevicesDTO1);
    mrSynItHardDevicesBusiness.updateMrSynItHardDevice(mrSynItDevicesDTO);

  }

  @Test
  public void approveItHardDevice_01() throws Exception {
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setId("99999");
    PowerMockito.when(
        mrSynItSoftDevicesRepository
            .findMrDeviceById(anyLong())
    ).thenReturn(null);

    ResultInSideDto actual = mrSynItHardDevicesBusiness.approveItHardDevice(mrSynItDevicesDTO);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void approveItHardDevice_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setId("99999");
    mrSynItDevicesDTO.setApproveReasonHard("1");
    mrSynItDevicesDTO.setApproveStatusHard("1");
    mrSynItDevicesDTO.setMrHard("1");
    mrSynItDevicesDTO.setMrConfirmHard("xxxx");
    mrSynItDevicesDTO.setObjectId("5555");
    mrSynItDevicesDTO.setObjectCode("xxxx");

    List<MrITHardScheduleDTO> listSchedule = Mockito.spy(ArrayList.class);
    MrITHardScheduleDTO mrITHardScheduleDTO1 = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO1.setCycleType("M");
    mrITHardScheduleDTO1.setCycle(1L);
    mrITHardScheduleDTO1.setMrId(11L);
    MrITHardScheduleDTO mrITHardScheduleDTO2 = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO2.setCycleType("M");
    mrITHardScheduleDTO2.setCycle(3L);
    mrITHardScheduleDTO2.setMrId(22L);
    MrITHardScheduleDTO mrITHardScheduleDTO3 = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO3.setCycleType("M");
    mrITHardScheduleDTO3.setCycle(6L);
    mrITHardScheduleDTO3.setMrId(33L);
    MrITHardScheduleDTO mrITHardScheduleDTO4 = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO4.setCycleType("M");
    mrITHardScheduleDTO4.setCycle(12L);
    mrITHardScheduleDTO4.setMrId(44L);
    listSchedule.add(mrITHardScheduleDTO1);
    listSchedule.add(mrITHardScheduleDTO2);
    listSchedule.add(mrITHardScheduleDTO3);
    listSchedule.add(mrITHardScheduleDTO4);

    PowerMockito.when(
        mrSynItSoftDevicesRepository
            .findMrDeviceById(anyLong())
    ).thenReturn(mrSynItDevicesDTO);
    PowerMockito.when(
        mrITHardScheduleRepository
            .getListScheduleMove(any())
    ).thenReturn(listSchedule);

    ResultInSideDto actual = mrSynItHardDevicesBusiness.approveItHardDevice(mrSynItDevicesDTO);

    Assert.assertNull(actual);
  }
}
