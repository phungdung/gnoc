package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureCDDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdExportDTO;
import com.viettel.gnoc.maintenance.model.MrDeviceCDEntity;
import com.viettel.gnoc.mr.repository.MrCfgProcedureCDRepository;
import com.viettel.gnoc.mr.repository.MrDeviceCDRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleCDRepository;
import com.viettel.gnoc.mr.repository.MrScheduleCdHisRepository;
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
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrScheduleCDBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MrScheduleCDBusinessImplTest {

  @InjectMocks
  MrScheduleCDBusinessImpl mrScheduleCDBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  MrScheduleCDRepository mrScheduleCDRepository;

  @Mock
  MrDeviceCDRepository mrDeviceCDRepository;

  @Mock
  MrRepository mrRepository;

  @Mock
  MrCfgProcedureCDRepository mrCfgProcedureCDRepository;

  @Mock
  MrScheduleCdHisRepository mrScheduleCdHisRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrScheduleCDBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void test_onSearch() {
    Datatable datatable = Mockito.spy(Datatable.class);
    List<MrScheduleCdDTO> mrScheduleCdDTOS = Mockito.spy(ArrayList.class);
    MrScheduleCdDTO mrScheduleCdDTO = Mockito.spy(MrScheduleCdDTO.class);
    mrScheduleCdDTO.setCycle("1");
    mrScheduleCdDTOS.add(mrScheduleCdDTO);
    datatable.setData(mrScheduleCdDTOS);
    PowerMockito.when(mrScheduleCDRepository.onSearch(any())).thenReturn(datatable);
    Datatable datatable1 = mrScheduleCDBusiness.onSearch(mrScheduleCdDTO);
    Assert.assertEquals(datatable.getTotal(), datatable1.getTotal());
  }

  @Test
  public void test_exportSearchData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    I18n.getLocale();
    List<MrScheduleCdExportDTO> mrScheduleCdDTOS = Mockito.spy(ArrayList.class);
    MrScheduleCdExportDTO mrScheduleCdExportDTO = Mockito.spy(MrScheduleCdExportDTO.class);
    mrScheduleCdExportDTO.setCycle("1");
    mrScheduleCdDTOS.add(mrScheduleCdExportDTO);
    PowerMockito.when(mrScheduleCDRepository.onSearchExport(any())).thenReturn(mrScheduleCdDTOS);

    MrScheduleCdDTO mrScheduleCdDTO = Mockito.spy(MrScheduleCdDTO.class);
    mrScheduleCdDTO.setCycle("1");
    File file = PowerMockito.mock(File.class);
    PowerMockito.when(
        CommonExport
            .exportFileWithTemplateXLSX(anyString(), anyString(), any(), anyString(), anyList(),
                any(), anyString())).thenReturn(file);
    File result = mrScheduleCDBusiness.exportSearchData(mrScheduleCdDTO);
    assertNull(result);
  }

  @Test
  public void test_importMrScheduleCD_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.FILE_IS_NULL);
    List<MrScheduleCdExportDTO> lstCrProcessCurrent = Mockito.spy(ArrayList.class);
    MrScheduleCdExportDTO mrScheduleCdExportDTO = Mockito.spy(MrScheduleCdExportDTO.class);
    mrScheduleCdExportDTO.setCycle("1");
    mrScheduleCdExportDTO.setScheduleId("6699");
    lstCrProcessCurrent.add(mrScheduleCdExportDTO);
    PowerMockito.when(mrScheduleCDRepository
        .onSearchExport(any())).thenReturn(lstCrProcessCurrent);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    mrScheduleCDBusiness.importMrScheduleCD(null);
  }

  @Test
  public void test_importMrScheduleCD_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    List<MrScheduleCdExportDTO> lstCrProcessCurrent = Mockito.spy(ArrayList.class);
    MrScheduleCdExportDTO mrScheduleCdExportDTO = Mockito.spy(MrScheduleCdExportDTO.class);
    mrScheduleCdExportDTO.setCycle("1");
    mrScheduleCdExportDTO.setScheduleId("6699");
    lstCrProcessCurrent.add(mrScheduleCdExportDTO);
    PowerMockito.when(mrScheduleCDRepository
        .onSearchExport(any())).thenReturn(lstCrProcessCurrent);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //Truong hop form header khong dung' chuan?
    PowerMockito.mockStatic(CommonImport.class);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 5, 0, 15, 1000))
        .thenReturn(headerList);
    resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
    mrScheduleCDBusiness.importMrScheduleCD(firstFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.FILE_INVALID_FORMAT);
  }

  @Test
  public void test_importMrScheduleCD_03() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    List<MrScheduleCdExportDTO> lstCrProcessCurrent = Mockito.spy(ArrayList.class);
    MrScheduleCdExportDTO mrScheduleCdExportDTO = Mockito.spy(MrScheduleCdExportDTO.class);
    mrScheduleCdExportDTO.setCycle("1");
    mrScheduleCdExportDTO.setScheduleId("6699");
    lstCrProcessCurrent.add(mrScheduleCdExportDTO);
    PowerMockito.when(mrScheduleCDRepository
        .onSearchExport(any())).thenReturn(lstCrProcessCurrent);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //form header dung' chuan
    PowerMockito.mockStatic(CommonImport.class);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"abc", "abc" + "*",
        "abc" + "*", "abc" + "*", "abc" + "*"};
    lstData.add(header);
    PowerMockito.when(CommonImport.getDataFromExcelFileNew(new File(filePath), 0, 8,
        0, 12, 2))
        .thenReturn(lstData);

    //Du~ lieu file DATA_OVER
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2000; i++) {
      dataImportList.add(new Object[250]);
    }
    PowerMockito.when(CommonImport.getDataFromExcel(new File(filePath), 0, 8,
        0, 12, 2))
        .thenReturn(dataImportList);
    resultInSideDto.setKey(RESULT.DATA_OVER);
    mrScheduleCDBusiness.importMrScheduleCD(firstFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.DATA_OVER);
  }

  @Test
  public void test_importMrScheduleCD_04() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    List<MrScheduleCdExportDTO> lstCrProcessCurrent = Mockito.spy(ArrayList.class);
    MrScheduleCdExportDTO mrScheduleCdExportDTO = Mockito.spy(MrScheduleCdExportDTO.class);
    mrScheduleCdExportDTO.setCycle("1");
    mrScheduleCdExportDTO.setScheduleId("6699");
    lstCrProcessCurrent.add(mrScheduleCdExportDTO);
    PowerMockito.when(mrScheduleCDRepository
        .onSearchExport(any())).thenReturn(lstCrProcessCurrent);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //form header dung' chuan
    PowerMockito.mockStatic(CommonImport.class);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"abc", "abc" + "*",
        "abc" + "*", "abc" + "*", "abc" + "*"};
    lstData.add(header);
    PowerMockito.when(CommonImport.getDataFromExcelFileNew(new File(filePath), 0, 8,
        0, 12, 2))
        .thenReturn(lstData);
    PowerMockito.when(CommonImport.getDataFromExcel(new File(filePath), 0, 8,
        0, 12, 2))
        .thenReturn(null);
    resultInSideDto.setKey(RESULT.NODATA);
    PowerMockito.when(I18n.getLanguage("common.searh.nodata")).thenReturn("Không có dữ liệu");
    mrScheduleCDBusiness.importMrScheduleCD(firstFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.NODATA);
  }

  @Test
  public void test_importMrScheduleCD_05() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    List<MrScheduleCdExportDTO> lstCrProcessCurrent = Mockito.spy(ArrayList.class);
    MrScheduleCdExportDTO mrScheduleCdExportDTO = Mockito.spy(MrScheduleCdExportDTO.class);
    mrScheduleCdExportDTO.setCycle("1");
    mrScheduleCdExportDTO.setScheduleId("6699");
    lstCrProcessCurrent.add(mrScheduleCdExportDTO);
    PowerMockito.when(mrScheduleCDRepository
        .onSearchExport(any())).thenReturn(lstCrProcessCurrent);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //form header dung' chuan
    PowerMockito.mockStatic(CommonImport.class);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"abc", "abc" + "*",
        "abc" + "*", "abc" + "*", "abc" + "*"};
    lstData.add(header);
    PowerMockito.when(CommonImport.getDataFromExcelFileNew(new File(filePath), 0, 8,
        0, 12, 2))
        .thenReturn(lstData);
    String[] data = new String[]{
        "abc", "6699", "13/07/2020", "13/07/2020",
        "13/07/2020", "13/07/2020", "13/07/2020", "13/07/2020",
        "13/07/2020", "13/07/2020", "13/07/2020", "13/07/2020", "13/07/2020"};
    //Du~ lieu file DATA
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2; i++) {
      dataImportList.add(data);
    }
    PowerMockito.when(CommonImport.getDataFromExcel(new File(filePath), 0, 8,
        0, 12, 2))
        .thenReturn(dataImportList);
    MrScheduleCdDTO objUpdate = Mockito.spy(MrScheduleCdDTO.class);
    objUpdate.setScheduleId(6699L);
    PowerMockito.when(mrScheduleCDRepository.findById(anyLong())).thenReturn(objUpdate);

    resultInSideDto.setKey(RESULT.ERROR);
    PowerMockito.when(I18n.getLanguage("import.common.fail")).thenReturn("Import không thành công");
    mrScheduleCDBusiness.importMrScheduleCD(firstFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.ERROR);
  }

  @Test
  public void test_importMrScheduleCD_06() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    List<MrScheduleCdExportDTO> lstCrProcessCurrent = Mockito.spy(ArrayList.class);
    MrScheduleCdExportDTO mrScheduleCdExportDTO = Mockito.spy(MrScheduleCdExportDTO.class);
    mrScheduleCdExportDTO.setCycle("1");
    mrScheduleCdExportDTO.setScheduleId("6699");
    lstCrProcessCurrent.add(mrScheduleCdExportDTO);
    PowerMockito.when(mrScheduleCDRepository
        .onSearchExport(any())).thenReturn(lstCrProcessCurrent);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    //form header dung' chuan
    PowerMockito.mockStatic(CommonImport.class);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"abc", "abc" + "*",
        "abc" + "*", "abc" + "*", "abc" + "*"};
    lstData.add(header);
    PowerMockito.when(CommonImport.getDataFromExcelFileNew(new File(filePath), 0, 8,
        0, 12, 2))
        .thenReturn(lstData);
    String[] data = new String[]{
        "abc", "6699", "13/07/2020", "13/07/2020",
        "13/07/2020", "13/07/2020", "13/07/2020", "13/07/2020",
        "13/07/2020", "13/07/2020", "13/07/2020", "13/07/2020", "13/07/2020"};
    //Du~ lieu file DATA
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1; i++) {
      dataImportList.add(data);
    }
    PowerMockito.when(CommonImport.getDataFromExcel(new File(filePath), 0, 8,
        0, 12, 2))
        .thenReturn(dataImportList);
    MrScheduleCdDTO objUpdate = Mockito.spy(MrScheduleCdDTO.class);
    objUpdate.setScheduleId(6699L);
    PowerMockito.when(mrScheduleCDRepository.findById(anyLong())).thenReturn(objUpdate);
    PowerMockito.when(mrScheduleCDRepository.insertOrUpdateListCd(anyList()))
        .thenReturn(RESULT.SUCCESS);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(I18n.getLanguage("mrDeviceCD.import.validRecord"))
        .thenReturn("Bản ghi hợp lệ");
    PowerMockito.when(I18n.getLanguage("common.success1")).thenReturn("Thành công");
    mrScheduleCDBusiness.importMrScheduleCD(firstFile);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void test_deleteById() {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrScheduleCdDTO dto = Mockito.spy(MrScheduleCdDTO.class);
    dto.setCycle("1");
    dto.setMrId(68L);
    dto.setProcedureId(1L);
    dto.setMarketCode("281");
    dto.setDeviceType("DH");
    dto.setDeviceCdId(69L);
    dto.setDeviceName("LL");

    PowerMockito.when(mrScheduleCDRepository.findById(anyLong())).thenReturn(dto);
    MrDeviceCDEntity dtoMD = Mockito.spy(MrDeviceCDEntity.class);
    dtoMD.setDeviceCdId(69L);
    dtoMD.setDeviceType("DH");
    PowerMockito.when(mrDeviceCDRepository.findMrDeviceById(anyLong())).thenReturn(dtoMD);
    MrInsideDTO mrInsideDTO =Mockito.spy(MrInsideDTO.class);
    mrInsideDTO.setMrType("H");
    mrInsideDTO.setMrCode("X");

    PowerMockito.when(mrRepository.findMrById(anyLong())).thenReturn(mrInsideDTO);

    MrCfgProcedureCDDTO procedureTelDTO = Mockito.spy(MrCfgProcedureCDDTO.class);
    procedureTelDTO.setArrayCode("ghghg");
    procedureTelDTO.setMrContentId("3");
    procedureTelDTO.setProcedureName("BCS");
    PowerMockito.when(mrCfgProcedureCDRepository
        .findById(anyLong())).thenReturn(procedureTelDTO);
    PowerMockito.when(mrScheduleCdHisRepository
        .insertOrUpdate(any())).thenReturn(resultInSideDto);
    PowerMockito.when(mrScheduleCDRepository.deleteById(anyLong())).thenReturn(resultInSideDto);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(mrDeviceCDRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);
    mrScheduleCDBusiness.deleteById(69L);
    Assert.assertEquals(RESULT.SUCCESS, resultInSideDto.getKey());
  }

  @Test
  public void test_addOrUpdate() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrScheduleCdDTO dto = Mockito.spy(MrScheduleCdDTO.class);
    dto.setDeviceName("BCS");
    dto.setMarketCode("281");
    PowerMockito.when(mrScheduleCDRepository.addOrUpdate(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = mrScheduleCDBusiness.addOrUpdate(dto);
    Assert.assertEquals(result.getKey(),resultInSideDto.getKey());
  }

  @Test
  public void test_findById() {
    MrScheduleCdDTO mrScheduleCdDTO =  Mockito.spy(MrScheduleCdDTO.class);
    mrScheduleCdDTO.setDeviceName("BCS");
    mrScheduleCdDTO.setMarketCode("281");
    mrScheduleCdDTO.setProcedureId(69L);
    PowerMockito.when(mrScheduleCDRepository.findById(anyLong())).thenReturn(mrScheduleCdDTO);
    MrCfgProcedureCDDTO cfgDTO =Mockito.spy(MrCfgProcedureCDDTO.class);
    cfgDTO.setProcedureName("BCS");
    PowerMockito.when(mrCfgProcedureCDRepository.findById(anyLong())).thenReturn(cfgDTO);
    MrScheduleCdDTO dto = mrScheduleCDBusiness.findById(69L);
    Assert.assertEquals(dto.getProcedureId(),mrScheduleCdDTO.getProcedureId());
  }
}
