package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.*;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureITHardDTO;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrITHardProcedureRepository;
import com.viettel.gnoc.mr.repository.MrITHardScheduleRepository;
import com.viettel.gnoc.mr.repository.MrITHisRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrITHardProcedureBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, TicketProvider.class, DataUtil.class, CommonExport.class, Workbook.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrITHardProcedureBusinessImplTest {

  @InjectMocks
  MrITHardProcedureBusinessImpl mrITHardProcedureBusiness;

  @Mock
  MrITHardProcedureRepository mrITHardProcedureRepository;

  @Mock
  MrSynItSoftDevicesBusiness mrSynItSoftDevicesBusiness;

  @Mock
  MrITHisRepository mrITHisRepository;

  @Mock
  MrITHardScheduleRepository mrITHardScheduleRepository;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Mock
  CatItemBusiness catItemBusiness;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrITHardProcedureBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void getListMrHardITProcedure() {
    Datatable datatable = Mockito.spy(Datatable.class);
    when(mrITHardProcedureRepository.getListMrHardITProcedure(any())).thenReturn(datatable);
    MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    Datatable result = mrITHardProcedureBusiness.getListMrHardITProcedure(mrCfgProcedureITHardDTO);

    assertEquals(datatable.getPages(), result.getPages());
  }

  @Test
  public void getDetail() {
    MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    when(mrITHardProcedureRepository.getDetail(any())).thenReturn(mrCfgProcedureITHardDTO);
    MrCfgProcedureITHardDTO result = mrITHardProcedureBusiness.getDetail(1L);
    assertEquals(result, mrCfgProcedureITHardDTO);
  }

  @Test
  public void insertMrCfgProcedureITHard() {
    MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    when(mrITHardProcedureRepository.checkDuplicateCfgProcedureITHardByDTO(any())).thenReturn(true);
    mrITHardProcedureBusiness.insertMrCfgProcedureITHard(mrCfgProcedureITHardDTO);
  }

  @Test
  public void insertMrCfgProcedureITHard1() {
    MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    when(mrITHardProcedureRepository.checkDuplicateCfgProcedureITHardByDTO(any()))
        .thenReturn(false);
    mrITHardProcedureBusiness.insertMrCfgProcedureITHard(mrCfgProcedureITHardDTO);

  }

  @Test
  public void updateMrCfgProcedureITHard() {
    when(mrITHardProcedureRepository
        .checkDuplicateCfgProcedureITHardByDTO(any())).thenReturn(true);

    MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    mrITHardProcedureBusiness.updateMrCfgProcedureITHard(mrCfgProcedureITHardDTO);
  }

  @Test
  public void updateMrCfgProcedureITHard1() {
    PowerMockito.mockStatic(I18n.class);
    when(mrITHardProcedureRepository
        .checkDuplicateCfgProcedureITHardByDTO(any())).thenReturn(false);

    MrCfgProcedureITHardDTO dtoUpdate = Mockito.spy(MrCfgProcedureITHardDTO.class);
    dtoUpdate.setCycleType("M");
    when(mrITHardProcedureRepository.getDetail(any())).thenReturn(dtoUpdate);

    MrITHardScheduleDTO mrITHardScheduleDTO1 = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO1.setNextDateModify(new Date());
    List<MrITHardScheduleDTO> lstScheduleIT = Mockito.spy(ArrayList.class);
    lstScheduleIT.add(mrITHardScheduleDTO1);
    when(mrITHardProcedureRepository.getScheduleInProcedureITHard(any())).thenReturn(lstScheduleIT);

    List<MrSynItDevicesDTO> lstDTO = Mockito.spy(ArrayList.class);
    when(mrSynItSoftDevicesBusiness
        .onSearchEntity(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstDTO);

    MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    mrCfgProcedureITHardDTO.setCycleType("D");
    mrITHardProcedureBusiness.updateMrCfgProcedureITHard(mrCfgProcedureITHardDTO);
  }

  @Test
  public void deleteMrCfgProcedureITHard() {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setDeviceId("test");
    List<MrITHardScheduleDTO> lstMrScheduleIT = Mockito.spy(ArrayList.class);
    lstMrScheduleIT.add(mrITHardScheduleDTO);
    when(mrITHardProcedureRepository.getScheduleInProcedureITHard(any())).thenReturn(lstMrScheduleIT);

    MrSynItDevicesDTO dtoOld = Mockito.spy(MrSynItDevicesDTO.class);
    dtoOld.setObjectId("test");
    when(mrSynItSoftDevicesBusiness.findMrDeviceByObjectId(any())).thenReturn(dtoOld);

    mrITHardScheduleDTO.setCycleType("M");

    mrITHardScheduleDTO.setCycle(1L);
    mrITHardProcedureBusiness.deleteMrCfgProcedureITHard(1L);

    mrITHardScheduleDTO.setCycle(3L);
    mrITHardProcedureBusiness.deleteMrCfgProcedureITHard(1L);

    mrITHardScheduleDTO.setCycle(6L);
    mrITHardProcedureBusiness.deleteMrCfgProcedureITHard(1L);

    mrITHardScheduleDTO.setCycle(12L);
    mrITHardProcedureBusiness.deleteMrCfgProcedureITHard(1L);

  }

  @Test
  public void validateFileImport() {
    mrITHardProcedureBusiness.validateFileImport(".xls");
  }

  @Test
  public void setMapArrayByDeviceType() {
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("dungtest");
    mrSynItDevicesDTO.setArrayCode("dungtest");
    List<MrSynItDevicesDTO> deviceDTOList = Mockito.spy(ArrayList.class);
    deviceDTOList.add(mrSynItDevicesDTO);

    mrITHardProcedureBusiness.setMapArrayByDeviceType();
  }

  @Test
  public void importData() throws Exception{
    MultipartFile multipartFile = null;

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("dungtest");
    mrSynItDevicesDTO.setArrayCode("dungtest");
    List<MrSynItDevicesDTO> deviceDTOList = Mockito.spy(ArrayList.class);
    deviceDTOList.add(mrSynItDevicesDTO);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);

    mrITHardProcedureBusiness.importData(multipartFile);
  }

  @Test
  public void importData1() throws Exception{
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    PowerMockito.mockStatic(FileUtils.class);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("dungtest");
    mrSynItDevicesDTO.setArrayCode("dungtest");
    List<MrSynItDevicesDTO> deviceDTOList = Mockito.spy(ArrayList.class);
    deviceDTOList.add(mrSynItDevicesDTO);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);

    String filePath = ".xls";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);

    mrITHardProcedureBusiness.importData(multipartFile);
  }

  @Test
  public void exportFileImport() {
  }

  @Test
  public void exportData() {
  }

  @Test
  public void getTemplate() {
  }

  @Test
  public void exportFileEx() throws Exception{
    List<MrCfgProcedureITHardDTO> lstData = Mockito.spy(ArrayList.class);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrITHardProcedureBusiness.exportFileEx(lstData,"RESULT_IMPORT");
  }

  @Test
  public void processFileTemplate() throws Exception{
    PowerMockito.mockStatic(I18n.class);
    ExcelWriterUtils ewu = Mockito.spy(ExcelWriterUtils.class);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountry);

    List<CatItemDTO> data = Mockito.spy(ArrayList.class);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(data);

    List<MrSynItDevicesDTO> listDeviceType = Mockito.spy(ArrayList.class);
    when(mrITHardProcedureRepository.getListDeviceType()).thenReturn(listDeviceType);

    List<CatItemDTO> dataMrContent = Mockito.spy(ArrayList.class);
    when(catItemBusiness.getListItemByCategoryAndParent(Constants.MR_ITEM_NAME.MR_TYPE, null)).thenReturn(dataMrContent);

    List<CatItemDTO> lstDataArrayCode = Mockito.spy(ArrayList.class);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(lstDataArrayCode);
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType"))
        .thenReturn("Loại thiết bị");
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent"))
        .thenReturn("Loại hoạt động");
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrWork"))
        .thenReturn("Đầu việc");

    mrITHardProcedureBusiness.processFileTemplate(ewu);
  }
}
