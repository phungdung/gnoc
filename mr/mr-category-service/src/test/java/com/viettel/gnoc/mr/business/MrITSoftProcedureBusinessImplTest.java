package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgFileProcedureDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftProcedureDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrITHisRepository;
import com.viettel.gnoc.mr.repository.MrITSoftFileProcedureRepository;
import com.viettel.gnoc.mr.repository.MrITSoftProcedureRepository;
import com.viettel.gnoc.mr.repository.MrITSoftScheduleRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;


@RunWith(PowerMockRunner.class)
@PrepareForTest({MrScheduleTelBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class, PassTranformer.class,
    DateTimeUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrITSoftProcedureBusinessImplTest {

  @InjectMocks
  MrITSoftProcedureBusinessImpl mrITSoftProcedureBusiness;

  @Mock
  MrITSoftProcedureRepository mrITSoftProcedureRepository;

  @Mock
  MrITSoftFileProcedureRepository mrITSoftFileProcedureRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  MrSynItSoftDevicesBusiness mrSynItSoftDevicesBusiness;

  @Mock
  MrITSoftScheduleRepository mrITSoftScheduleRepository;

  @Mock
  MrITHisRepository mrITHisRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrITSoftProcedureBusiness, "tempFolder",
        "./test_junit");
    ReflectionTestUtils.setField(mrITSoftProcedureBusiness, "uploadFolder",
        "/uploadFolder");
    ReflectionTestUtils.setField(mrITSoftProcedureBusiness, "ftpFolder",
        "/ftpFolder");
    ReflectionTestUtils.setField(mrITSoftProcedureBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(mrITSoftProcedureBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(mrITSoftProcedureBusiness, "ftpServer",
        "10.240.232.25");
    ReflectionTestUtils.setField(mrITSoftProcedureBusiness, "ftpPort",
        21);
  }

  @Test
  public void getListMrSoftITProcedure() {
    MrITSoftProcedureDTO mrITSoftProcedureDTO = Mockito.spy(MrITSoftProcedureDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    when(mrITSoftProcedureRepository.getListMrSoftITProcedure(any())).thenReturn(datatable);
    Datatable result = mrITSoftProcedureBusiness.getListMrSoftITProcedure(mrITSoftProcedureDTO);
    assertEquals(datatable.getPages(), result.getPages());
  }

  @Test
  public void getDetail() {
    Long procedureId = 1L;
    MrITSoftProcedureDTO dto = Mockito.spy(MrITSoftProcedureDTO.class);
    when(mrITSoftProcedureRepository.getDetail(any())).thenReturn(dto);
    MrITSoftProcedureDTO result = mrITSoftProcedureBusiness.getDetail(procedureId);
    assertEquals(dto, result);
  }

  @Test
  public void test_onInsert() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");

    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(69L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    MrITSoftProcedureDTO mrITSoftProcedureDTO = Mockito.spy(MrITSoftProcedureDTO.class);
    ResultInSideDto dto = Mockito.spy(ResultInSideDto.class);
    dto.setKey(RESULT.SUCCESS);
    dto.setId(69L);
    PowerMockito.when(mrITSoftProcedureRepository.insertOrUpdate(any())).thenReturn(dto);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }

    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(),
        anyString(), anyString(), anyString(), any(), any())).thenReturn("/trungduongFtp");
    PowerMockito.when(FileUtils
        .saveUploadFile(any(), any(), anyString(), any())).thenReturn("/trungduongOld");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    resultFileDataOld.setKey(RESULT.SUCCESS);
    MrCfgFileProcedureDTO fileDTO = Mockito.spy(MrCfgFileProcedureDTO.class);
    PowerMockito.when(mrITSoftFileProcedureRepository
        .insertOrUpdateFiles(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(gnocFileRepository
        .saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList())).thenReturn(dto);
    PowerMockito.when(mrITSoftProcedureRepository.insertOrUpdate(any())).thenReturn(dto);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> fileAttachs = Mockito.spy(ArrayList.class);
    fileAttachs.add(firstFile);
    ResultInSideDto result = mrITSoftProcedureBusiness.onInsert(fileAttachs, mrITSoftProcedureDTO);
    Assert.assertEquals(result.getKey(), dto.getKey());
  }

//  @Test
//  public void onUpdate() throws Exception {
//    PowerMockito.mockStatic(I18n.class);
//    PowerMockito.mockStatic(FileUtils.class);
//
//    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
//    List<MultipartFile> lstMuti = Mockito.spy(ArrayList.class);
//    lstMuti.add(multipartFile);
//
//    MrITSoftProcedureDTO dtoUpdate = Mockito.spy(MrITSoftProcedureDTO.class);
//
//    MrITSoftProcedureDTO dto = Mockito.spy(MrITSoftProcedureDTO.class);
//    dto.setCycleType("M");
//
//    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
//    mrITSoftScheduleDTO.setMrId("2");
//    mrITSoftScheduleDTO.setCrId("2");
//    List<MrITSoftScheduleDTO> lstScheduleIT = Mockito.spy(ArrayList.class);
//    lstScheduleIT.add(mrITSoftScheduleDTO);
//
//    MrITSoftScheduleDTO mrITSoftScheduleDTODel = Mockito.spy(MrITSoftScheduleDTO.class);
//    List<MrITSoftScheduleDTO> lstScheduleITDel = Mockito.spy(ArrayList.class);
//    lstScheduleITDel.add(mrITSoftScheduleDTODel);
//
//    UserToken userToken = Mockito.spy(UserToken.class);
//    userToken.setDeptId(2L);
//    userToken.setUserID(999999l);
//    userToken.setUserName("thanhlv12");
//    PowerMockito.mockStatic(TicketProvider.class);
//    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
//
//    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
//
//    List<MrSynItDevicesDTO> lstDTO = Mockito.spy(ArrayList.class);
//
//    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
//    List<MrSynItDevicesDTO> resultUpdate = Mockito.spy(ArrayList.class);
//    resultUpdate.add(mrSynItDevicesDTO);
//
//    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
//
//    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
//    List<GnocFileDto> gnocFileOldToDelete = Mockito.spy(ArrayList.class);
//    gnocFileOldToDelete.add(gnocFileDto);
//    dto.setLstGnocFiles(gnocFileOldToDelete);
//
//    when(unitRepository.findUnitById(any())).thenReturn(unitToken);
//    when(mrITSoftProcedureRepository.getDetail(any())).thenReturn(dtoUpdate);
//    when( mrITSoftProcedureRepository.getScheduleInProcedureITSoft(any())).thenReturn(lstScheduleIT);
//    when(mrSynItSoftDevicesBusiness.onSearchEntity(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstDTO);
//    when(mrITSoftProcedureRepository.insertOrUpdate(any())).thenReturn(res);
//    when(mrITSoftFileProcedureRepository.getCfgProcedureFileDetail(any(), anyString())).thenReturn(gnocFileOldToDelete);
//    mrITSoftProcedureBusiness.onUpdate(lstMuti,dto);
//  }

  @Test
  public void delete() {
    PowerMockito.mockStatic(I18n.class);
    Long procedureId = 1L;
    MrITSoftScheduleDTO dto = Mockito.spy(MrITSoftScheduleDTO.class);
    dto.setDeviceType("BGW");
    List<MrITSoftScheduleDTO> lstScheduleIT = Mockito.spy(ArrayList.class);
    lstScheduleIT.add(dto);

    MrSynItDevicesDTO dtoOld = Mockito.spy(MrSynItDevicesDTO.class);
    dtoOld.setObjectId("111");
    List<MrSynItDevicesDTO> lstDevice = Mockito.spy(ArrayList.class);
    lstDevice.add(dtoOld);

    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setKey("SUCCESS");

    when(mrITSoftProcedureRepository.getScheduleInProcedureITSoft(any())).thenReturn(lstScheduleIT);
    when(mrSynItSoftDevicesBusiness.findMrDeviceByObjectId(any())).thenReturn(dtoOld);
    when(mrITSoftProcedureRepository.delete(any())).thenReturn(res);
    mrITSoftProcedureBusiness.delete(procedureId);
  }

  @Test
  public void insertMrScheduleITHis() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    List<MrITSoftScheduleDTO> lstScheduleIT = Mockito.spy(ArrayList.class);
    lstScheduleIT.add(mrITSoftScheduleDTO);

    boolean checkUpdate = false;

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    List<MrScheduleITHisDTO> lstScheduleITHis = Mockito.spy(ArrayList.class);
    lstScheduleITHis.add(mrScheduleITHisDTO);
    mrITSoftProcedureBusiness.insertMrScheduleITHis(lstScheduleIT, checkUpdate);
  }

  @Test
  public void genCrNumber() {
    String crId = "";
    String sTypeCr = null;
    String sArrayActionName = "";
    mrITSoftProcedureBusiness.genCrNumber(crId, sTypeCr, sArrayActionName);
  }

  @Test
  public void genCrNumber1() {
    String crId = "";
    String sTypeCr = "0";
    String sArrayActionName = "";
    mrITSoftProcedureBusiness.genCrNumber(crId, sTypeCr, sArrayActionName);
  }

  @Test
  public void test_exportData() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(DateTimeUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("sss");
    List<MrITSoftProcedureDTO> mrITSoftProcedureDTOS = Mockito.spy(ArrayList.class);
    MrITSoftProcedureDTO mrITSoftProcedureDTO = Mockito.spy(MrITSoftProcedureDTO.class);
    mrITSoftProcedureDTO.setArrayCode("BCS-001");
    mrITSoftProcedureDTO.setCdId(69L);
    mrITSoftProcedureDTOS.add(mrITSoftProcedureDTO);
    PowerMockito.when(mrITSoftProcedureRepository
        .getDataExport(any())).thenReturn(mrITSoftProcedureDTOS);
    File fileExport = new File("TEMPLATE_EXPORT_VN.xlsx");
    PowerMockito.when(CommonExport.exportFileWithTemplateXLSX(
        anyString()
        , anyString()
        , any()
        , anyString(),
        any(),
        any(),
        anyString()
    )).thenReturn(fileExport);
    mrITSoftProcedureBusiness.exportData(mrITSoftProcedureDTO);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void test_onUpdate() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");

    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitId(69L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    MrITSoftProcedureDTO dtoUpdate = Mockito.spy(MrITSoftProcedureDTO.class);
    dtoUpdate.setProcedureId(96L);
    dtoUpdate.setCdId(69L);
    dtoUpdate.setArrayCode("BCS-001");
    dtoUpdate.setCycleType("D");
    dtoUpdate.setCycle(12L);
    dtoUpdate.setGenMrBefore(24L);
    dtoUpdate.setMrTime(12L);
    dtoUpdate.setDeviceType("MPD");

    PowerMockito.when(mrITSoftProcedureRepository.getDetail(anyLong())).thenReturn(dtoUpdate);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<MultipartFile> fileAttachs = Mockito.spy(ArrayList.class);
    MockMultipartFile multipartFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    fileAttachs.add(multipartFile);
    MrITSoftScheduleDTO dtoTel = Mockito.spy(MrITSoftScheduleDTO.class);
    dtoTel.setDeviceType("MPD");
    dtoTel.setMrId("1");
    dtoTel.setCrId("1");
    dtoTel.setDeviceId("1");
    dtoTel.setMarketCode("281");

    List<MrITSoftScheduleDTO> lstScheduleIT = Mockito.spy(ArrayList.class);
    lstScheduleIT.add(dtoTel);
    PowerMockito.when(mrITSoftProcedureRepository.getScheduleInProcedureITSoft(any()))
        .thenReturn(lstScheduleIT);
    List<MrSynItDevicesDTO> lstDTO = Mockito.spy(ArrayList.class);
    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setArrayCode("BCS-001");
    mrSynItDevicesDTO.setObjectId("26");
    lstDTO.add(mrSynItDevicesDTO);

    PowerMockito.when(mrSynItSoftDevicesRepository
        .onSearchEntity(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstDTO);
    PowerMockito.when(mrITHisRepository.insertUpdateListScheduleHis(anyList()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrITSoftScheduleRepository.deleteListSchedule(anyList()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrSynItSoftDevicesRepository.updateList(anyList()))
        .thenReturn(resultInSideDto);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(),
        anyString(), anyString(), anyString(), any(), any())).thenReturn("/trungduongFtp");
    PowerMockito.when(FileUtils
        .saveUploadFile(any(), any(), anyString(), any())).thenReturn("/trungduongOld");
    PowerMockito.when(mrITSoftFileProcedureRepository
        .insertOrUpdateFiles(any())).thenReturn(resultInSideDto);

    PowerMockito.when(gnocFileRepository
        .saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrITSoftProcedureRepository.insertOrUpdate(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = mrITSoftProcedureBusiness.onUpdate(fileAttachs, dtoUpdate);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

}
