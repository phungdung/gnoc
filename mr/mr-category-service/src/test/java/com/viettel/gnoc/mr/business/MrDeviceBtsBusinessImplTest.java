package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.mr.repository.MrDeviceBtsRepository;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Assert;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrDeviceBtsBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, WorkbookFactory.class, CommonExport.class, FileUtils.class,
    ExcelWriterUtils.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class MrDeviceBtsBusinessImplTest {

  @InjectMocks
  MrDeviceBtsBusinessImpl mrDeviceBtsBusiness;
  @Mock
  MrDeviceBtsRepository mrDeviceBtsRepository;
  @Mock
  protected CatLocationBusiness catLocationBusiness;
  @Mock
  TicketProvider ticketProvider;

  @Test
  public void getListMrDeviceBtsPage_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    List<MrDeviceBtsDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrDeviceBtsDTO);
    datatable.setData(list);
    PowerMockito.when(mrDeviceBtsRepository.getListMrDeviceBtsPage(any())).thenReturn(datatable);
    Datatable datatable1 = mrDeviceBtsBusiness.getListMrDeviceBtsPage(mrDeviceBtsDTO);
    Assert.assertEquals(datatable1.getPages(), datatable.getPages());
  }

  @Test
  public void getListMrDeviceBtsDTO_01() {
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    List<MrDeviceBtsDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListMrDeviceBtsDTO(any())).thenReturn(list);
    mrDeviceBtsBusiness.getListMrDeviceBtsDTO(mrDeviceBtsDTO);
    Assert.assertEquals(list.size(), 1);

  }

  @Test
  public void update_01() {
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    mrDeviceBtsBusiness.update(mrDeviceBtsDTO);
  }

  @Test
  public void delete() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    mrDeviceBtsBusiness.delete(1L);
  }

  @Test
  public void getDetail_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    mrDeviceBtsDTO.setMarketCode("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("1");
    catLocationDTO.setLocationId("1");
    catLocationDTO.setLocationName("1");
    List<CatLocationDTO> dataList = Mockito.spy(ArrayList.class);
    dataList.add(catLocationDTO);
    PowerMockito.when(mrDeviceBtsRepository.getDetail(any())).thenReturn(mrDeviceBtsDTO);
    PowerMockito.when(catLocationBusiness.getCatLocationByParentId(any())).thenReturn(dataList);
    mrDeviceBtsBusiness.getDetail(1L);
  }

  @Test
  public void getListDeviceType_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    List<MrDeviceBtsDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListDeviceType()).thenReturn(list);
    mrDeviceBtsBusiness.getListDeviceType();
    Assert.assertEquals(list.size(), 1);

  }


  @Test
  public void getListfuelTypeByDeviceType_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    List<MrDeviceBtsDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListfuelTypeByDeviceType(anyString(), anyString()))
        .thenReturn(list);
    mrDeviceBtsBusiness.getListfuelTypeByDeviceType("1", "1");
    Assert.assertEquals(list.size(), 1);
  }

  @Test
  public void getListProducerByDeviceType_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    List<MrDeviceBtsDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListProducerByDeviceType(anyString(), anyString()))
        .thenReturn(list);
    mrDeviceBtsBusiness.getListProducerByDeviceType("1", "1");
    Assert.assertEquals(list.size(), 1);
  }

  @Test
  public void getListProvince_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    List<MrDeviceBtsDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListProvince(anyString())).thenReturn(list);
    mrDeviceBtsBusiness.getListProvince("1");
    Assert.assertEquals(list.size(), 1);
  }

  @Test
  public void getListMrDeviceBtsForCD_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    List<MrDeviceBtsDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListMrDeviceBtsForCD(any())).thenReturn(list);
    mrDeviceBtsBusiness.getListMrDeviceBtsForCD(mrDeviceBtsDTO);
    Assert.assertEquals(list.size(), 1);
  }

  @Test
  public void getListWoBts_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    List<MrScheduleBtsHisDetailInsiteDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrScheduleBtsHisDetailDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListWoBts(anyString())).thenReturn(list);
    mrDeviceBtsBusiness.getListWoBts("1");
    Assert.assertEquals(list.size(), 1);
  }

  @Test
  public void getListWoCodeMrScheduleBtsHisDetail() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO = Mockito
        .spy(MrScheduleBtsHisDetailInsiteDTO.class);
    List<MrScheduleBtsHisDetailInsiteDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrScheduleBtsHisDetailDTO);
    PowerMockito.when(mrDeviceBtsRepository.getListWoCodeMrScheduleBtsHisDetail(any()))
        .thenReturn(list);
    mrDeviceBtsBusiness.getListWoCodeMrScheduleBtsHisDetail(mrScheduleBtsHisDetailDTO);
    Assert.assertEquals(list.size(), 1);
  }

  @Test
  public void exportSearchData_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("STT");
    PowerMockito.mockStatic(CommonExport.class);
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    List<MrDeviceBtsDTO> mrDeviceBtsDTOList = Mockito.spy(ArrayList.class);
    mrDeviceBtsDTOList.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.onSearchExport(any())).thenReturn(mrDeviceBtsDTOList);
    File file = mrDeviceBtsBusiness.exportSearchData(mrDeviceBtsDTO);
    Assert.assertNull(file);

  }

  @Test
  public void importMrDeviceBts_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void importMrDeviceBts_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1*", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1*", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_06() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1*", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }


  @Test
  public void importMrDeviceBts_07() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1*", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_08() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1*", "1", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_09() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1*", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_10() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1", "1*", "1", "1", "1", "1",
        "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_11() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1", "1", "1*", "1", "1", "1",
        "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_12() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1", "1", "1", "1*", "1", "1",
        "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_13() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1*", "1",
        "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_14() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1*",
        "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_15() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1*", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_16() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1*", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_17() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importMrDeviceBts_18() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    String filePath = "/temp.xlsx";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1"};
    Object[] header1 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
//    headerList1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        8,
        0,
        14,
        2
    )).thenReturn(headerList1);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "NODATA");
  }

  @Test
  public void importMrDeviceBts_19() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    String filePath = "/temp";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1"};
    Object[] header1 = new Object[]{null, "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    Object[] header2 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1"};
    Object[] header3 = new Object[]{"1", null, "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    Object[] header4 = new Object[]{"1", "1", null, "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    Object[] header5 = new Object[]{"1", "1", "1", null, "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    Object[] header6 = new Object[]{"1", "1", "1", "1", null, "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    Object[] header7 = new Object[]{"1", "1", "1", "1", "1", null, "1", "1", "1", "1", "1", "1",
        "1", "1", "1"};
    Object[] header8 = new Object[]{"1", "1", "1", "1", "1", "1", null, "1", "1", "1", "1", "1",
        "1", "1", "1"};
    Object[] header9 = new Object[]{"1", "1", "1", "1", "1", "1", "1", null, "1", "1", "1", "1",
        "1", "1", "1"};
    Object[] header10 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", null, "1", "1", "1",
        "1", "1", "1"};
    Object[] header11 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", null, "1", "1",
        "1", "1", "1"};
    Object[] header12 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", null, "1",
        "1", "1", "1"};
    Object[] header13 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", null,
        "1", "1", "1"};
    Object[] header14 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        null, "1", "1"};
    Object[] header15 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", null, "1"};
    Object[] header16 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", null};
    Object[] header17 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "18/05/2020", "1", "1", "1"};

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList1.add(header17);
    headerList1.add(header1);
    headerList1.add(header2);
    headerList1.add(header3);
    headerList1.add(header4);
    headerList1.add(header5);
    headerList1.add(header6);
    headerList1.add(header7);
    headerList1.add(header8);
    headerList1.add(header9);
    headerList1.add(header10);
    headerList1.add(header11);
    headerList1.add(header12);
    headerList1.add(header13);
    headerList1.add(header14);
    headerList1.add(header15);
    headerList1.add(header16);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcel(
        new File(filePath),
        0,
        8,
        0,
        14,
        2
    )).thenReturn(headerList1);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void importMrDeviceBts_20() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain",
        "some xml".getBytes());
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    String filePath = "/temp";
    Object[] header = new Object[]{"1*", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1"};
    Object[] header17 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
        "18/05/2020", "1", "1", "1"};
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    mrDeviceBtsDTO.setMarketCode("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("1");
    catLocationDTO.setLocationId("1");
    catLocationDTO.setLocationName("1");
    List<CatLocationDTO> dataList = Mockito.spy(ArrayList.class);
    dataList.add(catLocationDTO);
    PowerMockito.when(mrDeviceBtsRepository.getDetail(any())).thenReturn(mrDeviceBtsDTO);
    PowerMockito.when(catLocationBusiness.getCatLocationByParentId(any())).thenReturn(dataList);
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList1.add(header17);
    PowerMockito.when(mrDeviceBtsRepository.UpdateListDeviceBts(any())).thenReturn("SUCCESS");
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        7,
        0,
        14,
        2
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcel(
        new File(filePath),
        0,
        8,
        0,
        14,
        2
    )).thenReturn(headerList1);
    ResultInSideDto result = mrDeviceBtsBusiness.importMrDeviceBts(firstFile);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void validateFileImport() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(ExcelWriterUtils.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    mrDeviceBtsBusiness.validateFileImport("/temp.xls");
  }

//  @Test
//  public void exportFileImport_01() {
//    PowerMockito.mockStatic(I18n.class);
//    PowerMockito.mockStatic(CommonImport.class);
//    PowerMockito.mockStatic(FileUtils.class);
//    PowerMockito.mockStatic(CommonExport.class);
//    PowerMockito.mockStatic(FileInputStream.class);
//    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
//    ExcelWriterUtils excelWriterUtils = Mockito.spy(ExcelWriterUtils.class);
//    Workbook workBook = Mockito.spy(Workbook.class);
//    ErrorInfo errorInfo = Mockito.spy(ErrorInfo.class);
//    errorInfo.setMsg("1");
//    errorInfo.setRow(20);
//    List<ErrorInfo> lstError = Mockito.spy(ArrayList.class);
//    lstError.add(errorInfo);
//    PowerMockito.when(excelWriterUtils.readFileExcel("/temp")).thenReturn(workBook);
//
//    mrDeviceBtsBusiness.exportFileImport("/temp", lstError);
//  }

  @Test
  public void getFileTemplate_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileInputStream.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MrDeviceBtsDTO mrDeviceBtsDTO = Mockito.spy(MrDeviceBtsDTO.class);
    mrDeviceBtsDTO.setProvinceName("1");
    mrDeviceBtsDTO.setProvinceCode("1");
    List<MrDeviceBtsDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(mrDeviceBtsDTO);
    PowerMockito.when(mrDeviceBtsRepository.onSearchExport(any())).thenReturn(lstData);

    mrDeviceBtsBusiness.getFileTemplate(mrDeviceBtsDTO);
  }

  @Test
  public void setMapCountryName() {
  }

  @Test
  public void getListSupplierBtsByDeviceType() {
  }
}
