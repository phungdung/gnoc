package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureITHardDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrITHardScheduleRepository;
import com.viettel.gnoc.mr.repository.MrITHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.mr.repository.MrSynItHardDevicesRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
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

public class MrITHardScheduleBusinessImplTest {

  @InjectMocks
  MrITHardScheduleBusinessImpl mrITHardScheduleBusiness;

  @Mock
  MrITHardScheduleRepository mrITHardScheduleRepository;

  @Mock
  MrSynItHardDevicesRepository mrSynItHardDevicesRepository;

  @Mock
  MrITHardProcedureBusiness mrITHardProcedureBusiness;

  @Mock
  MrITHisBusiness mrITHisBusiness;

  @Mock
  MrScheduleTelRepository mrScheduleTelRepository;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Mock
  MrITHisRepository mrITHisRepository;
  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrITHardScheduleBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void getListMrScheduleITHard() {
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    when(mrITHardScheduleRepository.getListMrScheduleITHard(mrITHardScheduleDTO))
        .thenReturn(datatable);
    Datatable result = mrITHardScheduleBusiness.getListMrScheduleITHard(mrITHardScheduleDTO);
    assertEquals(datatable.getPages(), result.getPages());
  }

  @Test
  public void getDetail() {
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO);
    MrITHardScheduleDTO result = mrITHardScheduleBusiness.getDetail(1L);
    assertEquals(mrITHardScheduleDTO, result);
  }

  @Test
  public void deleteMrScheduleITHard() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("1");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("1");
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setCycleType("1");
    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO);
    MrSynItDevicesDTO mrSynItDevicesDTO =  Mockito.spy(MrSynItDevicesDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITHardScheduleRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(mrSynItDevicesDTO);
    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setCrId("1");
    List<MrScheduleITHisDTO> listScheduleITHis = new ArrayList<>();
    listScheduleITHis.add(mrScheduleITHisDTO);
    when(mrITHisRepository.insertUpdateListScheduleHis(listScheduleITHis)).thenReturn(resultInSideDto);
    when(mrITHardScheduleRepository
            .deleteMrScheduleITHard(any())).thenReturn(resultInSideDto);
    mrITHardScheduleBusiness.deleteMrScheduleITHard(1L);
  }

  @Test
  public void exportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);

    List<MrITHardScheduleDTO> lstExport = Mockito.spy(ArrayList.class);
    when(mrITHardScheduleRepository.getListDataExport(any())).thenReturn(lstExport);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrITHardScheduleBusiness.exportData(mrITHardScheduleDTO);
  }

  @Test
  public void exportFileEx() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    List<MrITHardScheduleDTO> lstData = Mockito.spy(ArrayList.class);
    mrITHardScheduleBusiness.exportFileEx(lstData, "RESULT_IMPORT", "H");
  }


  @Test
  public void exportFileEx1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    List<MrITHardScheduleDTO> lstData = Mockito.spy(ArrayList.class);
    String key = "MR_SCHEDULE_IT_HARD_EXPORT";
    mrITHardScheduleBusiness.exportFileEx(lstData, "RESULT_IMPORTs", "H");
  }

  @Test
  public void exportFileEx3() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    List<MrITHardScheduleDTO> lstData = Mockito.spy(ArrayList.class);
    mrITHardScheduleBusiness.exportFileEx(lstData, "RESULT_IMPORT", "S");
  }


  @Test
  public void exportFileEx4() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    List<MrITHardScheduleDTO> lstData = Mockito.spy(ArrayList.class);
    String key = "MR_SCHEDULE_IT_HARD_EXPORT";
    mrITHardScheduleBusiness.exportFileEx(lstData, "RESULT_IMPORTs", "S");
  }

  @Test
  public void readerHeaderSheet() {
  }

  @Test
  public void onUpdateHard() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setMrConfirmHard("1");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    mrScheduleITHisDTO.setMrDate("06/06/2020 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    MrCfgProcedureITHardDTO procedureItDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    when(mrITHardProcedureBusiness.getDetail(Long.valueOf(mrScheduleITHisDTO.getProcedureId())))
        .thenReturn(procedureItDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);

    resultInSideDto.setKey("SUCCESS");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");

    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO);
    when(mrITHardScheduleRepository.deleteListSchedule(any())).thenReturn(resultInSideDto1);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }

  @Test
  public void onUpdateHard2() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setMrConfirmHard("1");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    mrScheduleITHisDTO.setMrDate("06/06/2020 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    MrCfgProcedureITHardDTO procedureItDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    when(mrITHardProcedureBusiness.getDetail(Long.valueOf(mrScheduleITHisDTO.getProcedureId())))
        .thenReturn(procedureItDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);

    resultInSideDto.setKey("SUCCESS");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO);
    when(mrITHardScheduleRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");

    when(mrSynItHardDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto1);
    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);

    when(mrSynItHardDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);
    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }


  @Test
  public void onUpdateHard3() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setMrId(1L);
    mrITHardScheduleDTO.setCrId(1L);
    mrITHardScheduleDTO.setMrConfirmHard("5");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    mrScheduleITHisDTO.setMrDate("06/06/2020 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    when(mrITHardScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }

  @Test
  public void onUpdateHard4() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setMrId(1L);
    mrITHardScheduleDTO.setCrId(1L);
    mrITHardScheduleDTO.setMrConfirmHard("5");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    mrScheduleITHisDTO.setMrDate("06/06/2020 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITHardScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    when(mrSynItHardDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto1);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }

  @Test
  public void onUpdateHard5() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setMrId(1L);
    mrITHardScheduleDTO.setCrId(1L);
    mrITHardScheduleDTO.setMrConfirmHard("5");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    mrScheduleITHisDTO.setMrDate("06/06/2020 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITHardScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);
    when(mrSynItHardDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }

  @Test
  public void onUpdateHard6() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setCrId(1L);
    mrITHardScheduleDTO.setMrConfirmHard("5");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    mrScheduleITHisDTO.setMrDate("06/06/2020 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    MrCfgProcedureITHardDTO procedureItDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    procedureItDTO.setTypeCr(1L);
    procedureItDTO.setArrayActionName("1");
    when(mrITHardProcedureBusiness.getDetail(any())).thenReturn(procedureItDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }

  @Test
  public void onUpdateHard7() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setCrId(1L);
    mrITHardScheduleDTO.setMrConfirmHard("5");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    mrScheduleITHisDTO.setMrDate("06/06/2020 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    MrITHardScheduleDTO mrITHardScheduleDTO1 = Mockito.spy(MrITHardScheduleDTO.class);
    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO1);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    MrCfgProcedureITHardDTO procedureItDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    procedureItDTO.setTypeCr(1L);
    procedureItDTO.setArrayActionName("1");
    when(mrITHardProcedureBusiness.getDetail(any())).thenReturn(procedureItDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    when(mrITHardScheduleRepository.deleteListSchedule(any())).thenReturn(resultInSideDto1);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }

  @Test
  public void onUpdateHard8() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setCrId(1L);
    mrITHardScheduleDTO.setMrConfirmHard("5");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    mrScheduleITHisDTO.setMrDate("06/06/2020 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    MrITHardScheduleDTO mrITHardScheduleDTO1 = Mockito.spy(MrITHardScheduleDTO.class);
    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO1);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    MrCfgProcedureITHardDTO procedureItDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    procedureItDTO.setTypeCr(1L);
    procedureItDTO.setArrayActionName("1");
    when(mrITHardProcedureBusiness.getDetail(any())).thenReturn(procedureItDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    when(mrITHardScheduleRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("ERROR");
    when(mrSynItHardDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto1);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }

  @Test
  public void onUpdateHard9() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setCrId(1L);
    mrITHardScheduleDTO.setMrConfirmHard("5");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    mrScheduleITHisDTO.setMrDate("06/06/2020 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    MrITHardScheduleDTO mrITHardScheduleDTO1 = Mockito.spy(MrITHardScheduleDTO.class);
    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO1);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    MrCfgProcedureITHardDTO procedureItDTO = Mockito.spy(MrCfgProcedureITHardDTO.class);
    procedureItDTO.setTypeCr(1L);
    procedureItDTO.setArrayActionName("1");
    when(mrITHardProcedureBusiness.getDetail(any())).thenReturn(procedureItDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    when(mrITHardScheduleRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);

    when(mrSynItHardDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }

  @Test
  public void onUpdateHard10() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setCrId(1L);
    mrITHardScheduleDTO.setMrConfirmHard("5");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    mrScheduleITHisDTO.setMrDate("06/06/2020 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    MrITHardScheduleDTO mrITHardScheduleDTO1 = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO1.setGroupCode("1");
    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO1);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("1");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    when(mrITHardScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);
    when(mrSynItHardDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }

  @Test
  public void onUpdateHard11() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setCrId(1L);
    mrITHardScheduleDTO.setMrConfirmHard("5");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    mrScheduleITHisDTO.setMrDate("06/06/2020 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    MrITHardScheduleDTO mrITHardScheduleDTO1 = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO1.setGroupCode("1");
    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO1);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("1");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITHardScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);
    when(mrSynItHardDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    MrITHardScheduleDTO mrITHardScheduleDTO2 = Mockito.spy(MrITHardScheduleDTO.class);
    List<MrITHardScheduleDTO> lstMrScheduleITGroupCode = Mockito.spy(ArrayList.class);
    lstMrScheduleITGroupCode.add(mrITHardScheduleDTO2);
    when(mrITHardScheduleRepository.findListMrScheduleByGroupCode(any()))
        .thenReturn(lstMrScheduleITGroupCode);

    when(mrITHardScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }

  @Test
  public void OnUpdateHard12() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setGroupCode("1");

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrITHardScheduleDTO mrITHardScheduleDTO1 = Mockito.spy(MrITHardScheduleDTO.class);
    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTO1);

    List<MrITHardScheduleDTO> lstMrScheduleITGroupCode = Mockito.spy(ArrayList.class);
    when(mrITHardScheduleRepository.findListMrScheduleByGroupCode(any()))
        .thenReturn(lstMrScheduleITGroupCode);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrSynItHardDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    mrITHardScheduleBusiness.onUpdateHard(mrITHardScheduleDTO);
  }


  @Test
  public void deleteMrScheduleITSoft() {

  }

  @Test
  public void getTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);

    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.scheduleId"))
        .thenReturn("mtItSoftSchedule.scheduleId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nationName"))
        .thenReturn("mtItSoftSchedule.nationName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region"))
        .thenReturn("mtItSoftSchedule.region");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrayCode"))
        .thenReturn("mtItSoftSchedule.arrayCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceType"))
        .thenReturn("mtItSoftSchedule.deviceType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceCode"))
        .thenReturn("mtItSoftSchedule.deviceCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceName"))
        .thenReturn("mtItSoftSchedule.deviceName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ipNode"))
        .thenReturn("mtItSoftSchedule.ipNode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.vendor"))
        .thenReturn("mtItSoftSchedule.vendor");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.bdType"))
        .thenReturn("mtItSoftSchedule.bdType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.stationCode"))
        .thenReturn("mtItSoftSchedule.stationCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nodeAffected"))
        .thenReturn("mtItSoftSchedule.nodeAffected");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.groupCode"))
        .thenReturn("mtItSoftSchedule.groupCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDate"))
        .thenReturn("mtItSoftSchedule.nextDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDate"))
        .thenReturn("mtItSoftSchedule.lastDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrId"))
        .thenReturn("mtItSoftSchedule.mrId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.crId"))
        .thenReturn("mtItSoftSchedule.crId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.wlgText"))
        .thenReturn("mtItSoftSchedule.wlgText");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.implementUnitName"))
        .thenReturn("mtItSoftSchedule.implementUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.checkingUnitName"))
        .thenReturn("mtItSoftSchedule.checkingUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycle"))
        .thenReturn("mtItSoftSchedule.cycle");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycleType"))
        .thenReturn("mtItSoftSchedule.cycleType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.note"))
        .thenReturn("mtItSoftSchedule.note");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDateStr"))
        .thenReturn("mtItSoftSchedule.lastDateStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateStr"))
        .thenReturn("mtItSoftSchedule.nextDateStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ud"))
        .thenReturn("mtItSoftSchedule.ud");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.boUnit"))
        .thenReturn("mtItSoftSchedule.boUnit");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.approveStatus"))
        .thenReturn("mtItSoftSchedule.approveStatus");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateModify"))
        .thenReturn("mtItSoftSchedule.nextDateModify");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrConfirmName"))
        .thenReturn("mtItSoftSchedule.mrConfirmName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.title.hard"))
        .thenReturn("10");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.title.soft"))
        .thenReturn("20");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.title.region"))
        .thenReturn("30");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.title.arrNet"))
        .thenReturn("40");
    //      </editor-fold>

    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.title")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrNet")).thenReturn("3");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);

    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setRegion("1");
    List<MrITHardScheduleDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    mrDeviceDTOList.add(mrITHardScheduleDTO);
    when(
        mrITHardScheduleRepository
            .getListRegionByMrSynItDevices(any())
    ).thenReturn(mrDeviceDTOList);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    Datatable dataArray = Mockito.spy(Datatable.class);
    dataArray.setData(lstArrayCode);
    when(catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(dataArray);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("1");
    List<MrSynItDevicesDTO> lstDeviceType = Mockito.spy(ArrayList.class);
    lstDeviceType.add(mrSynItDevicesDTO);
    PowerMockito.when(
        mrSynItHardDevicesRepository
            .getListDeviceTypeByArrayCode(anyString())
    ).thenReturn(lstDeviceType);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    List<MrConfigDTO> lstMrConfigDTO = Mockito.spy(ArrayList.class);
    lstMrConfigDTO.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstMrConfigDTO);

    mrITHardScheduleBusiness.getTemplate("H");
  }

  @Test
  public void getTemplate1() throws Exception {
    PowerMockito.mockStatic(I18n.class);

    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.scheduleId"))
        .thenReturn("mtItSoftSchedule.scheduleId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nationName"))
        .thenReturn("mtItSoftSchedule.nationName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region"))
        .thenReturn("mtItSoftSchedule.region");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrayCode"))
        .thenReturn("mtItSoftSchedule.arrayCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceType"))
        .thenReturn("mtItSoftSchedule.deviceType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceCode"))
        .thenReturn("mtItSoftSchedule.deviceCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceName"))
        .thenReturn("mtItSoftSchedule.deviceName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ipNode"))
        .thenReturn("mtItSoftSchedule.ipNode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.vendor"))
        .thenReturn("mtItSoftSchedule.vendor");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.bdType"))
        .thenReturn("mtItSoftSchedule.bdType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.stationCode"))
        .thenReturn("mtItSoftSchedule.stationCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nodeAffected"))
        .thenReturn("mtItSoftSchedule.nodeAffected");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.groupCode"))
        .thenReturn("mtItSoftSchedule.groupCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDate"))
        .thenReturn("mtItSoftSchedule.nextDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDate"))
        .thenReturn("mtItSoftSchedule.lastDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrId"))
        .thenReturn("mtItSoftSchedule.mrId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.crId"))
        .thenReturn("mtItSoftSchedule.crId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.wlgText"))
        .thenReturn("mtItSoftSchedule.wlgText");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.implementUnitName"))
        .thenReturn("mtItSoftSchedule.implementUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.checkingUnitName"))
        .thenReturn("mtItSoftSchedule.checkingUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycle"))
        .thenReturn("mtItSoftSchedule.cycle");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycleType"))
        .thenReturn("mtItSoftSchedule.cycleType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.note"))
        .thenReturn("mtItSoftSchedule.note");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDateStr"))
        .thenReturn("mtItSoftSchedule.lastDateStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateStr"))
        .thenReturn("mtItSoftSchedule.nextDateStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ud"))
        .thenReturn("mtItSoftSchedule.ud");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.boUnit"))
        .thenReturn("mtItSoftSchedule.boUnit");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.approveStatus"))
        .thenReturn("mtItSoftSchedule.approveStatus");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateModify"))
        .thenReturn("mtItSoftSchedule.nextDateModify");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrConfirmName"))
        .thenReturn("mtItSoftSchedule.mrConfirmName");
    //      </editor-fold>

    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.title.soft")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrNet")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.title.hard")).thenReturn("100");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region")).thenReturn("101");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrNet")).thenReturn("102");

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);

    List<MrITHardScheduleDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    when(mrITHardScheduleRepository.getListRegionByMrSynItDevices(any()))
        .thenReturn(mrDeviceDTOList);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    Datatable dataArray = Mockito.spy(Datatable.class);
    dataArray.setData(lstArrayCode);
    when(catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(dataArray);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    List<MrConfigDTO> lstMrConfigDTO = Mockito.spy(ArrayList.class);
    lstMrConfigDTO.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstMrConfigDTO);

    mrITHardScheduleBusiness.getTemplate("S");
  }

  @Test
  public void importData() {
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = null;
    mrITHardScheduleBusiness.importData(multipartFile);
  }

  @Test
  public void importData1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        7,
        0,
        22,
        1000
    )).thenReturn(headerList);
    mrITHardScheduleBusiness.importData(multipartFile);
  }

  @Test
  public void importData2() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.scheduleId"))
        .thenReturn("mtItSoftSchedule.scheduleId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nationName"))
        .thenReturn("mtItSoftSchedule.nationName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region"))
        .thenReturn("mtItSoftSchedule.region");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrayCode"))
        .thenReturn("mtItSoftSchedule.arrayCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceType"))
        .thenReturn("mtItSoftSchedule.deviceType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceCode"))
        .thenReturn("mtItSoftSchedule.deviceCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceName"))
        .thenReturn("mtItSoftSchedule.deviceName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ipNode"))
        .thenReturn("mtItSoftSchedule.ipNode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.vendor"))
        .thenReturn("mtItSoftSchedule.vendor");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.bdType"))
        .thenReturn("mtItSoftSchedule.bdType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.stationCode"))
        .thenReturn("mtItSoftSchedule.stationCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nodeAffected"))
        .thenReturn("mtItSoftSchedule.nodeAffected");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.groupCode"))
        .thenReturn("mtItSoftSchedule.groupCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDate"))
        .thenReturn("mtItSoftSchedule.nextDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDate"))
        .thenReturn("mtItSoftSchedule.lastDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrId"))
        .thenReturn("mtItSoftSchedule.mrId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.crId"))
        .thenReturn("mtItSoftSchedule.crId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.wlgText"))
        .thenReturn("mtItSoftSchedule.wlgText");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.descriptionCr"))
        .thenReturn("mtItSoftSchedule.descriptionCr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.implementUnitName"))
        .thenReturn("mtItSoftSchedule.implementUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.checkingUnitName"))
        .thenReturn("mtItSoftSchedule.checkingUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycle"))
        .thenReturn("mtItSoftSchedule.cycle");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycleType"))
        .thenReturn("mtItSoftSchedule.cycleType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.note"))
        .thenReturn("mtItSoftSchedule.note");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ud"))
        .thenReturn("mtItSoftSchedule.ud");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.boUnit"))
        .thenReturn("mtItSoftSchedule.boUnit");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.approveStatus"))
        .thenReturn("mtItSoftSchedule.approveStatus");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateModify"))
        .thenReturn("mtItSoftSchedule.nextDateModify");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrConfirmName"))
        .thenReturn("mtItSoftSchedule.mrConfirmName");
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mtItSoftSchedule.scheduleId"),
        I18n.getLanguage("mtItSoftSchedule.nationName"),
        I18n.getLanguage("mtItSoftSchedule.region"),
        I18n.getLanguage("mtItSoftSchedule.arrayCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceType"),
        I18n.getLanguage("mtItSoftSchedule.deviceCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceName"),
        I18n.getLanguage("mtItSoftSchedule.ipNode"),
        I18n.getLanguage("mtItSoftSchedule.vendor"),
        I18n.getLanguage("mtItSoftSchedule.bdType"),
        I18n.getLanguage("mtItSoftSchedule.stationCode"),
        I18n.getLanguage("mtItSoftSchedule.nodeAffected"),
        I18n.getLanguage("mtItSoftSchedule.groupCode"),
        I18n.getLanguage("mtItSoftSchedule.nextDate"),
        I18n.getLanguage("mtItSoftSchedule.lastDate"),
        I18n.getLanguage("mtItSoftSchedule.mrId"),
        I18n.getLanguage("mtItSoftSchedule.crId"),
        I18n.getLanguage("mtItSoftSchedule.wlgText"),
        I18n.getLanguage("mtItSoftSchedule.descriptionCr"),
        I18n.getLanguage("mtItSoftSchedule.implementUnitName"),
        I18n.getLanguage("mtItSoftSchedule.checkingUnitName"),
        I18n.getLanguage("mtItSoftSchedule.cycle")
    };
    //      </editor-fold>
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        7,
        0,
        22,
        1000
    )).thenReturn(headerList);

    Object[] objectsTest = new Object[30];
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1501; i++) {
      dataImportList.add(objectsTest);
    }
    when(CommonImport.getDataFromExcelFileNew(
        fileImport,
        0,
        8,
        0,
        30,
        1000
    )).thenReturn(dataImportList);

    mrITHardScheduleBusiness.importData(multipartFile);
  }

  @Test
  public void importData3() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(I18n.getLanguage(anyString()))
        .thenReturn("1");
    String[] header = new String[]{
      "1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"
    };
    //      </editor-fold>
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        7,
        0,
        22,
        1000
    )).thenReturn(headerList);

    Object[] objectsTest = new Object[30];
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < objectsTest.length; i++) {
      if (i == 14 || i == 15 || i == 28) {
        objectsTest[i] = "06/06/2020 12:12:12";
      } else {
        objectsTest[i] = i;
      }
    }
    dataImportList.add(objectsTest);
    when(CommonImport.getDataFromExcelFileNew(
        fileImport,
        0,
        8,
        0,
        22,
        1000
    )).thenReturn(dataImportList);

    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTO.setScheduleId(1L);
    List<MrITHardScheduleDTO> results = Mockito.spy(ArrayList.class);
    results.add(mrITHardScheduleDTO);
    when(mrITHardScheduleRepository.getListMrSheduleITByIdForImport(any())).thenReturn(results);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("29");
    mrConfigDTO.setConfigName("29");
    List<MrConfigDTO> lstMrConfigDTO = Mockito.spy(ArrayList.class);
    lstMrConfigDTO.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstMrConfigDTO);
    mrITHardScheduleBusiness.setMrConfirm();

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrITHardScheduleDTO mrITHardScheduleDTODetail = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTODetail.setMrConfirmHard("1");

    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTODetail);

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItHardDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);
//
    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("1");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITHardScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    mrITHardScheduleBusiness.importData(multipartFile);
  }

  @Test
  public void importData4() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.scheduleId"))
        .thenReturn("mtItSoftSchedule.scheduleId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nationName"))
        .thenReturn("mtItSoftSchedule.nationName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region"))
        .thenReturn("mtItSoftSchedule.region");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrayCode"))
        .thenReturn("mtItSoftSchedule.arrayCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceType"))
        .thenReturn("mtItSoftSchedule.deviceType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceCode"))
        .thenReturn("mtItSoftSchedule.deviceCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceName"))
        .thenReturn("mtItSoftSchedule.deviceName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ipNode"))
        .thenReturn("mtItSoftSchedule.ipNode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.vendor"))
        .thenReturn("mtItSoftSchedule.vendor");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.bdType"))
        .thenReturn("mtItSoftSchedule.bdType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.stationCode"))
        .thenReturn("mtItSoftSchedule.stationCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nodeAffected"))
        .thenReturn("mtItSoftSchedule.nodeAffected");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.groupCode"))
        .thenReturn("mtItSoftSchedule.groupCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDate"))
        .thenReturn("mtItSoftSchedule.nextDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDate"))
        .thenReturn("mtItSoftSchedule.lastDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrId"))
        .thenReturn("mtItSoftSchedule.mrId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.crId"))
        .thenReturn("mtItSoftSchedule.crId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.wlgText"))
        .thenReturn("mtItSoftSchedule.wlgText");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.descriptionCr"))
        .thenReturn("mtItSoftSchedule.descriptionCr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.implementUnitName"))
        .thenReturn("mtItSoftSchedule.implementUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.checkingUnitName"))
        .thenReturn("mtItSoftSchedule.checkingUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycle"))
        .thenReturn("mtItSoftSchedule.cycle");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycleType"))
        .thenReturn("mtItSoftSchedule.cycleType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.note"))
        .thenReturn("mtItSoftSchedule.note");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ud"))
        .thenReturn("mtItSoftSchedule.ud");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.boUnit"))
        .thenReturn("mtItSoftSchedule.boUnit");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.approveStatus"))
        .thenReturn("mtItSoftSchedule.approveStatus");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateModify"))
        .thenReturn("mtItSoftSchedule.nextDateModify");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrConfirmName"))
        .thenReturn("mtItSoftSchedule.mrConfirmName");
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mtItSoftSchedule.scheduleId"),
        I18n.getLanguage("mtItSoftSchedule.nationName"),
        I18n.getLanguage("mtItSoftSchedule.region"),
        I18n.getLanguage("mtItSoftSchedule.arrayCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceType"),
        I18n.getLanguage("mtItSoftSchedule.deviceCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceName"),
        I18n.getLanguage("mtItSoftSchedule.ipNode"),
        I18n.getLanguage("mtItSoftSchedule.vendor"),
        I18n.getLanguage("mtItSoftSchedule.bdType"),
        I18n.getLanguage("mtItSoftSchedule.stationCode"),
        I18n.getLanguage("mtItSoftSchedule.nodeAffected"),
        I18n.getLanguage("mtItSoftSchedule.groupCode"),
        I18n.getLanguage("mtItSoftSchedule.nextDate"),
        I18n.getLanguage("mtItSoftSchedule.lastDate"),
        I18n.getLanguage("mtItSoftSchedule.mrId"),
        I18n.getLanguage("mtItSoftSchedule.crId"),
        I18n.getLanguage("mtItSoftSchedule.wlgText"),
        I18n.getLanguage("mtItSoftSchedule.descriptionCr"),
        I18n.getLanguage("mtItSoftSchedule.implementUnitName"),
        I18n.getLanguage("mtItSoftSchedule.checkingUnitName"),
        I18n.getLanguage("mtItSoftSchedule.cycle"),
        I18n.getLanguage("mtItSoftSchedule.cycleType"),
        I18n.getLanguage("mtItSoftSchedule.note"),
        I18n.getLanguage("mtItSoftSchedule.ud"),
        I18n.getLanguage("mtItSoftSchedule.boUnit"),
        I18n.getLanguage("mtItSoftSchedule.approveStatus"),
        I18n.getLanguage("mtItSoftSchedule.nextDateModify"),
        I18n.getLanguage("mtItSoftSchedule.mrConfirmName"),
    };
    //      </editor-fold>
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        7,
        0,
        30,
        1000
    )).thenReturn(headerList);

    Object[] objectsTest = new Object[30];
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < objectsTest.length; i++) {
      if (i == 14 || i == 15 || i == 28) {
        objectsTest[i] = "06/06/2020 12:12:12";
      } else {
        objectsTest[i] = i;
      }
    }
    dataImportList.add(objectsTest);
    when(CommonImport.getDataFromExcelFileNew(
        fileImport,
        0,
        8,
        0,
        30,
        1000
    )).thenReturn(dataImportList);

    MrITHardScheduleDTO mrITHardScheduleDTO = Mockito.spy(MrITHardScheduleDTO.class);
    List<MrITHardScheduleDTO> results = Mockito.spy(ArrayList.class);
    results.add(mrITHardScheduleDTO);
    when(mrITHardScheduleRepository.getListMrSheduleITByIdForImport(any())).thenReturn(results);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("28");
    mrConfigDTO.setConfigName("28");
    List<MrConfigDTO> lstMrConfigDTO = Mockito.spy(ArrayList.class);
    lstMrConfigDTO.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstMrConfigDTO);
    mrITHardScheduleBusiness.setMrConfirm();

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrITHardScheduleDTO mrITHardScheduleDTODetail = Mockito.spy(MrITHardScheduleDTO.class);
    mrITHardScheduleDTODetail.setMrConfirmHard("1");

    PowerMockito.when(I18n.getValidation("mtItSoftSchedule.scheduleId.notFound"))
        .thenReturn("resultImport");

    when(mrITHardScheduleRepository.getDetail(any())).thenReturn(mrITHardScheduleDTODetail);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
//
    mrITHardScheduleBusiness.importData(multipartFile);
  }


  @Test
  public void importData5() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    String filePath = "/temptt";
    File fileImport = new File(filePath);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.scheduleId"))
        .thenReturn("mtItSoftSchedule.scheduleId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nationName"))
        .thenReturn("mtItSoftSchedule.nationName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region"))
        .thenReturn("mtItSoftSchedule.region");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrayCode"))
        .thenReturn("mtItSoftSchedule.arrayCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceType"))
        .thenReturn("mtItSoftSchedule.deviceType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceCode"))
        .thenReturn("mtItSoftSchedule.deviceCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceName"))
        .thenReturn("mtItSoftSchedule.deviceName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ipNode"))
        .thenReturn("mtItSoftSchedule.ipNode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.vendor"))
        .thenReturn("mtItSoftSchedule.vendor");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.bdType"))
        .thenReturn("mtItSoftSchedule.bdType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.stationCode"))
        .thenReturn("mtItSoftSchedule.stationCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nodeAffected"))
        .thenReturn("mtItSoftSchedule.nodeAffected");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.groupCode"))
        .thenReturn("mtItSoftSchedule.groupCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDate"))
        .thenReturn("mtItSoftSchedule.nextDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDate"))
        .thenReturn("mtItSoftSchedule.lastDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrId"))
        .thenReturn("mtItSoftSchedule.mrId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.crId"))
        .thenReturn("mtItSoftSchedule.crId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.wlgText"))
        .thenReturn("mtItSoftSchedule.wlgText");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.descriptionCr"))
        .thenReturn("mtItSoftSchedule.descriptionCr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.implementUnitName"))
        .thenReturn("mtItSoftSchedule.implementUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.checkingUnitName"))
        .thenReturn("mtItSoftSchedule.checkingUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycle"))
        .thenReturn("mtItSoftSchedule.cycle");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycleType"))
        .thenReturn("mtItSoftSchedule.cycleType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.note"))
        .thenReturn("mtItSoftSchedule.note");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ud"))
        .thenReturn("mtItSoftSchedule.ud");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.boUnit"))
        .thenReturn("mtItSoftSchedule.boUnit");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.approveStatus"))
        .thenReturn("mtItSoftSchedule.approveStatus");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateModify"))
        .thenReturn("mtItSoftSchedule.nextDateModify");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrConfirmName"))
        .thenReturn("mtItSoftSchedule.mrConfirmName");
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mtItSoftSchedule.scheduleId"),
        I18n.getLanguage("mtItSoftSchedule.nationName"),
        I18n.getLanguage("mtItSoftSchedule.region"),
        I18n.getLanguage("mtItSoftSchedule.arrayCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceType"),
        I18n.getLanguage("mtItSoftSchedule.deviceCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceName"),
        I18n.getLanguage("mtItSoftSchedule.ipNode"),
        I18n.getLanguage("mtItSoftSchedule.vendor"),
        I18n.getLanguage("mtItSoftSchedule.bdType"),
        I18n.getLanguage("mtItSoftSchedule.stationCode"),
        I18n.getLanguage("mtItSoftSchedule.nodeAffected"),
        I18n.getLanguage("mtItSoftSchedule.groupCode"),
        I18n.getLanguage("mtItSoftSchedule.nextDate"),
        I18n.getLanguage("mtItSoftSchedule.lastDate"),
        I18n.getLanguage("mtItSoftSchedule.mrId"),
        I18n.getLanguage("mtItSoftSchedule.crId"),
        I18n.getLanguage("mtItSoftSchedule.wlgText"),
        I18n.getLanguage("mtItSoftSchedule.descriptionCr"),
        I18n.getLanguage("mtItSoftSchedule.implementUnitName"),
        I18n.getLanguage("mtItSoftSchedule.checkingUnitName"),
        I18n.getLanguage("mtItSoftSchedule.cycle"),
        I18n.getLanguage("mtItSoftSchedule.cycleType"),
        I18n.getLanguage("mtItSoftSchedule.note"),
        I18n.getLanguage("mtItSoftSchedule.ud"),
        I18n.getLanguage("mtItSoftSchedule.boUnit"),
        I18n.getLanguage("mtItSoftSchedule.approveStatus"),
        I18n.getLanguage("mtItSoftSchedule.nextDateModify"),
        I18n.getLanguage("mtItSoftSchedule.mrConfirmName"),
    };
    //      </editor-fold>
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        7,
        0,
        30,
        1000
    )).thenReturn(headerList);

    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    when(CommonImport.getDataFromExcelFileNew(
        fileImport,
        0,
        8,
        0,
        30,
        1000
    )).thenReturn(dataImportList);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrITHardScheduleBusiness.importData(multipartFile);
  }

  @Test
  public void getMapFromList() {
  }

  @Test
  public void validateFileSoftFormat() {
  }
}
