package com.viettel.gnoc.mr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatItemBusiness;
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
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.mr.repository.MrCfgFileProcedureTelRepository;
import com.viettel.gnoc.mr.repository.MrCfgProcedureTelRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.xml.security.utils.Base64;
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
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrCfgProcedureTelBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, TicketProvider.class, DataUtil.class, CommonExport.class, DateTimeUtils.class,
    PassTranformer.class, Base64.class, ExcelWriterUtils.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MrCfgProcedureTelBusinessImplTest {

  @InjectMocks
  MrCfgProcedureTelBusinessImpl mrCfgProcedureTelBusiness;

  @Mock
  CatItemBusiness catItemBusiness;

  @Mock
  UnitRepository unitRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  MrCfgProcedureTelRepository mrCfgProcedureTelRepository;

  @Mock
  MrDeviceBusiness mrDeviceBusiness;

  @Mock
  MrScheduleTelRepository mrScheduleTelRepository;

  @Mock
  MrScheduleTelHisRepository mrScheduleTelHisRepository;

  @Mock
  MrCfgFileProcedureTelRepository mrCfgFileProcedureTelRepository;

  @Mock
  TicketProvider ticketProvider;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrCfgProcedureTelBusiness, "uploadFolder",
        "/uploadFolder");
    ReflectionTestUtils.setField(mrCfgProcedureTelBusiness, "ftpFolder",
        "/ftpFolder");
    ReflectionTestUtils.setField(mrCfgProcedureTelBusiness, "ftpUser",
        "723298837f9c6fe9953c6e07e0e4df17");
    ReflectionTestUtils.setField(mrCfgProcedureTelBusiness, "ftpPass",
        "6cf8a37f3e6993f90eef71859b1ebf31");
    ReflectionTestUtils.setField(mrCfgProcedureTelBusiness, "ftpServer",
        "10.240.232.25");
    ReflectionTestUtils.setField(mrCfgProcedureTelBusiness, "ftpPort",
        21);
  }

  @Test
  public void testOnSearch() {
    Datatable datatable = Mockito.spy(Datatable.class);
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setMrMode("S");
    mrCfgProcedureTelDTO.setSortName("GenCrName");
    List<MrCfgProcedureTelDTO> dtos = Mockito.spy(ArrayList.class);
    dtos.add(mrCfgProcedureTelDTO);
    datatable.setData(dtos);
    PowerMockito.when(mrCfgProcedureTelRepository.onSearch(any(), anyString()))
        .thenReturn(datatable);
    datatable = mrCfgProcedureTelBusiness.onSearch(mrCfgProcedureTelDTO);
    Assert.assertNotNull(datatable);
  }

  @Test
  public void testExportSearchData_01() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("sss");
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setMrMode("S");
    mrCfgProcedureTelDTO.setCycle(1L);
    List<MrCfgProcedureTelDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(mrCfgProcedureTelDTO);
    PowerMockito.when(mrCfgProcedureTelRepository.onSearchExport(any(), anyString()))
        .thenReturn(lstData);
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
    mrCfgProcedureTelBusiness.exportSearchData(mrCfgProcedureTelDTO);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void testOnInsert() throws Exception {
    PowerMockito.mockStatic(Base64.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    PowerMockito.mockStatic(TicketProvider.class);
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Mockito.spy(FileUtils.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);

    ResultInSideDto dto = Mockito.spy(ResultInSideDto.class);
    dto.setKey(RESULT.SUCCESS);
    dto.setId(1L);
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setMrMode("S");
    PowerMockito.when(mrCfgProcedureTelRepository.insertOrUpdate(any())).thenReturn(dto);
    List<MultipartFile> fileAttachs = Mockito.spy(ArrayList.class);
    MockMultipartFile multipartFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    fileAttachs.add(multipartFile);

    byte[] fileContent = new byte[16];
    for (int i = 0; i < 16; i++) {
      fileContent[i] += i;
    }
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(),
        anyString(), anyString(), any(), any(), any())).thenReturn("/trungduongFtp");
    PowerMockito.when(FileUtils
        .saveUploadFile(any(), any(), anyString(), any())).thenReturn("/trungduongOld");
    PowerMockito.when(mrCfgFileProcedureTelRepository.insertOrUpdate(any())).thenReturn(dto);
    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList()))
        .thenReturn(dto);
    ResultInSideDto resultInSideDto = mrCfgProcedureTelBusiness
        .onInsert(fileAttachs, mrCfgProcedureTelDTO);
    Assert.assertEquals(dto.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void onUpdate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString()))
        .thenReturn("1");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("thanhlv12");
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);

    MrCfgProcedureTelDTO dtoUpdate = Mockito.spy(MrCfgProcedureTelDTO.class);
    dtoUpdate.setCycleType("1");
    PowerMockito.when(mrCfgProcedureTelRepository.getDetail(anyLong())).thenReturn(dtoUpdate);

    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setKey(RESULT.SUCCESS);
    PowerMockito.when(mrCfgProcedureTelRepository.insertOrUpdate(any())).thenReturn(res);
    PowerMockito.when(mrCfgFileProcedureTelRepository.insertOrUpdate(any())).thenReturn(res);

    List<MrScheduleTelDTO> lstScheduleTel = Mockito.spy(ArrayList.class);
    MrScheduleTelDTO telDTO = Mockito.spy(MrScheduleTelDTO.class);
    telDTO.setDeviceId(1L);
    lstScheduleTel.add(telDTO);
    PowerMockito.when(mrCfgProcedureTelRepository.getScheduleInProcedure(any()))
        .thenReturn(lstScheduleTel);

    List<MrDeviceDTO> lstDTO = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setDeviceId(1L);
    lstDTO.add(mrDeviceDTO);
    PowerMockito.when(mrDeviceBusiness
        .onSearchEntity(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstDTO).thenReturn(null);

    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(),
        anyString(), anyString(), anyString(), any(), any())).thenReturn("/trungduongFtp");
    PowerMockito.when(FileUtils
        .saveUploadFile(any(), any(), anyString(), any())).thenReturn("/trungduongOld");

    List<MultipartFile> fileAttachs = Mockito.spy(ArrayList.class);
    MockMultipartFile multipartFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    fileAttachs.add(multipartFile);
    MrCfgProcedureTelDTO dto = Mockito.spy(MrCfgProcedureTelDTO.class);
    dto.setProcedureId(1L);
    dto.setCycleType("2");
    ResultInSideDto result = mrCfgProcedureTelBusiness.onUpdate(fileAttachs, dto);
    Assert.assertEquals(res.getKey(), result.getKey());
  }

  @Test
  public void testDelete() {
    PowerMockito.mockStatic(TicketProvider.class);
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("sss");
    ResultInSideDto res = PowerMockito.mock(ResultInSideDto.class);
    res.setKey(null);
    MrScheduleTelDTO dtoTel = PowerMockito.mock(MrScheduleTelDTO.class);
    dtoTel.setMrHard("H");
    dtoTel.setCycle("3");
    dtoTel.setDeviceId(1L);
    dtoTel.setMarketCode("281");
    dtoTel.setArrayCode("ttt");
    dtoTel.setDeviceType("ko");
    dtoTel.setDeviceCode("lolo");
    dtoTel.setDeviceName("yuo");
    dtoTel.setNextDateModify(new Date());
    dtoTel.setMrContentId("4");
    dtoTel.setMrType("erer");
    dtoTel.setMrId(1L);
    dtoTel.setMrCode("OIY");
    dtoTel.setCrId(2L);
    dtoTel.setArrayActionName("KOIO");
    dtoTel.setProcedureId(1L);
    dtoTel.setProcedureName("vb");
    dtoTel.setNetworkType("bb");
    dtoTel.setRegion("123");

    List<MrScheduleTelDTO> lstScheduleTel = Mockito.spy(ArrayList.class);
    lstScheduleTel.add(dtoTel);
    PowerMockito.when(mrCfgProcedureTelRepository.getScheduleInProcedure(any()))
        .thenReturn(lstScheduleTel);
    List<MrDeviceDTO> lstDevice = Mockito.spy(ArrayList.class);
    MrDeviceDTO dtoDevice = Mockito.spy(MrDeviceDTO.class);
    dtoDevice.setDeviceId(1L);
    lstDevice.add(dtoDevice);
    PowerMockito.when(mrDeviceBusiness.getDetail(anyLong())).thenReturn(dtoDevice);
    PowerMockito.when(mrDeviceBusiness.insertOrUpdateListDevice(any())).thenReturn(res);
    PowerMockito.when(mrCfgProcedureTelRepository.delete(anyLong())).thenReturn(res);
    PowerMockito.when(mrScheduleTelRepository.deleteListSchedule(anyList())).thenReturn(res);

    PowerMockito.when(mrScheduleTelHisRepository.insertUpdateListScheduleHis(anyList()))
        .thenReturn(res);
    PowerMockito.when(gnocFileRepository.deleteGnocFile(anyString(), anyLong())).thenReturn(res);
    PowerMockito.when(mrCfgFileProcedureTelRepository.deleteByCfgProcedureId(anyLong()))
        .thenReturn(1);

    PowerMockito.when(mrCfgProcedureTelRepository.delete(anyLong())).thenReturn(new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS));
    ResultInSideDto result = mrCfgProcedureTelBusiness.delete(1L);
    Assert.assertEquals(RESULT.SUCCESS, result.getKey());
  }

  @Test
  public void getDetail() {
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setProcedureId(1L);
    PowerMockito.when(mrCfgProcedureTelRepository.getDetail(anyLong())).thenReturn(mrCfgProcedureTelDTO);
    List<GnocFileDto> lst = Mockito.spy(ArrayList.class);
    lst.add(new GnocFileDto());
    PowerMockito.when(gnocFileRepository.getListGnocFileByDto(any())).thenReturn(lst);
    MrCfgProcedureTelDTO result = mrCfgProcedureTelBusiness.getDetail(mrCfgProcedureTelDTO.getProcedureId());
    Assert.assertEquals(mrCfgProcedureTelDTO.getProcedureId(), result.getProcedureId());
  }
}

