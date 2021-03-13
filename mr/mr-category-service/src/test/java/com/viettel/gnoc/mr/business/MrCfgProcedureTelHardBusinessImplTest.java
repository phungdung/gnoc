package com.viettel.gnoc.mr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.mr.repository.MrCfgProcedureTelRepository;
import com.viettel.gnoc.mr.repository.MrCheckListRepository;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TrungDuong
 */

@RunWith(PowerMockRunner.class)

@PrepareForTest({MrCfgProcedureTelHardBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class, CommonExport.class, WorkbookFactory.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class MrCfgProcedureTelHardBusinessImplTest {

  @InjectMocks
  MrCfgProcedureTelHardBusinessImpl mrCfgProcedureTelHardBusiness;

  @Mock
  MrCfgProcedureTelRepository mrCfgProcedureTelRepository;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  MrDeviceRepository mrDeviceRepository;

  @Mock
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Mock
  MrScheduleTelRepository mrScheduleTelRepository;

  @Mock
  MrScheduleTelHisRepository mrScheduleTelHisRepository;

  @Mock
  MrDeviceBusiness mrDeviceBusiness;

  @Mock
  CatItemBusiness catItemBusiness;

  @Mock
  CommonImport commonImport;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  ExcelWriterUtils ewu;

  @Mock
  MrCfgProcedureTelHardBusinessImpl mrCfgProcedureTelHardBusiness1;

  @Mock
  MrCheckListRepository mrCheckListRepository;

  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(mrCfgProcedureTelHardBusiness, "tempFolder",
        "./mr-temp");
  }

  private String pathTemplate = "templates" + File.separator + "maintenance" + File.separator;


  @Test
  public void test_setMapCountryName() {
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    itemDataCRInside.setValueStr(123L);
    itemDataCRInside.setDisplayStr("xxx");
    lstCountryName.add(itemDataCRInside);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(any(), anyLong(), anyLong())).thenReturn(lstCountryName);
    mrCfgProcedureTelHardBusiness.setMapCountryName();
    Assert.assertEquals(lstCountryName.size(), 1l);
  }

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void test_onSearch() {
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setMrMode("H");
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(new ArrayList<>());
    PowerMockito.when(mrCfgProcedureTelRepository.onSearch(any(), anyString()))
        .thenReturn(datatable);
    Datatable data = mrCfgProcedureTelHardBusiness.onSearch(mrCfgProcedureTelDTO);
    Assert.assertEquals(datatable.getPages(), data.getPages());
  }

  @Test
  public void test_insert01() {
    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.DUPLICATE);
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setMrMode("H");
    List<MrCfgProcedureTelDTO> cfgProcedureTelDTOList = Mockito.spy(ArrayList.class);
    cfgProcedureTelDTOList.add(mrCfgProcedureTelDTO);
    PowerMockito.when(mrCfgProcedureTelRepository.checkMrCfgProcedureTelHardExist2(any()))
        .thenReturn(cfgProcedureTelDTOList);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Đã tồn tại cấu hình cho mảng này");
    ResultInSideDto result = mrCfgProcedureTelHardBusiness.insert(mrCfgProcedureTelDTO);
    Assert.assertEquals(result.getKey(), RESULT.DUPLICATE);
  }

  @Test
  public void test_insert02() {
    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setMrMode("H");
    PowerMockito.when(mrCfgProcedureTelRepository.insertOrUpdate(mrCfgProcedureTelDTO))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = mrCfgProcedureTelHardBusiness.insert(mrCfgProcedureTelDTO);
    Assert.assertEquals(result.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void test_update01() {
    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), null);
  }

  @Test
  public void test_update02() {
    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);

    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setMrMode("H");
    List<MrCfgProcedureTelDTO> cfgProcedureTelDTOList = Mockito.spy(ArrayList.class);
    cfgProcedureTelDTOList.add(mrCfgProcedureTelDTO);
    PowerMockito.when(mrCfgProcedureTelRepository.checkMrCfgProcedureTelHardExist2(any()))
        .thenReturn(cfgProcedureTelDTOList);
    resultInSideDto.setKey(RESULT.DUPLICATE);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Đã tồn tại cấu hình cho mảng này");
    ResultInSideDto result = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void test_update03() {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);

    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setMrMode("H");
    mrCfgProcedureTelDTO.setProcedureId(1L);
    List<MrCfgProcedureTelDTO> cfgProcedureTelDTOList = Mockito.spy(ArrayList.class);

    MrCfgProcedureTelDTO dtoUpdate = Mockito.spy(MrCfgProcedureTelDTO.class);
    PowerMockito.when(mrCfgProcedureTelRepository.getDetail(anyLong())).thenReturn(null);
    MrScheduleTelDTO dtoTel = Mockito.spy(MrScheduleTelDTO.class);
    dtoTel.setProcedureId(1L);
    PowerMockito.when(mrCfgProcedureTelRepository.insertOrUpdate(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void test_update04() {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Move lịch do cập nhật cấu hình thủ tục");

    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setMrMode("H");
    mrCfgProcedureTelDTO.setProcedureId(1L);
    mrCfgProcedureTelDTO.setCycle(1L);
    mrCfgProcedureTelDTO.setCycleType("M");
    mrCfgProcedureTelDTO.setGenMrBefore(1L);
    mrCfgProcedureTelDTO.setMrTime(1L);
    List<MrCfgProcedureTelDTO> cfgProcedureTelDTOList = Mockito.spy(ArrayList.class);

    MrCfgProcedureTelDTO dtoUpdate = Mockito.spy(MrCfgProcedureTelDTO.class);
    dtoUpdate.setCycle(2L);
    dtoUpdate.setCycleType("A");
    mrCfgProcedureTelDTO.setGenMrBefore(2L);
    dtoUpdate.setMrTime(2L);
    PowerMockito.when(mrCfgProcedureTelRepository.getDetail(anyLong())).thenReturn(dtoUpdate);
    MrScheduleTelDTO dtoTel = Mockito.spy(MrScheduleTelDTO.class);
    dtoTel.setProcedureId(1L);
    dtoTel.setCycleType("M");
    dtoTel.setDeviceCode("A");
    dtoTel.setMrHardCycle(1L);
    List<MrScheduleTelDTO> mrScheduleTelDTOS = Mockito.spy(ArrayList.class);
    mrScheduleTelDTOS.add(dtoTel);
    PowerMockito.when(mrCfgProcedureTelRepository.getScheduleInProcedure(any())).thenReturn(mrScheduleTelDTOS);

    PowerMockito.when(mrCfgProcedureTelRepository.insertOrUpdate(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());

    dtoUpdate.setCycle(1L);
    result = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());

    dtoUpdate.setCycleType("M");
    result = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());

    dtoUpdate.setGenMrBefore(1L);
    result = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());

    dtoUpdate.setMrTime(1L);
    result = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());

    List<MrDeviceDTO> lstDTO = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNodeCode("A");
    lstDTO.add(mrDeviceDTO);
    PowerMockito.when(mrDeviceBusiness.onSearchEntity(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstDTO).thenReturn(null);
    result = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());

    PowerMockito.when(mrDeviceBusiness.onSearchEntity(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstDTO).thenReturn(null);
    mrScheduleTelDTOS.get(0).setMrHardCycle(3L);
    PowerMockito.when(mrCfgProcedureTelRepository.getScheduleInProcedure(any())).thenReturn(mrScheduleTelDTOS);
    result = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());

    PowerMockito.when(mrDeviceBusiness.onSearchEntity(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstDTO).thenReturn(null);
    mrScheduleTelDTOS.get(0).setMrHardCycle(6L);
    PowerMockito.when(mrCfgProcedureTelRepository.getScheduleInProcedure(any())).thenReturn(mrScheduleTelDTOS);
    result = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());

    PowerMockito.when(mrDeviceBusiness.onSearchEntity(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstDTO).thenReturn(null);
    mrScheduleTelDTOS.get(0).setMrHardCycle(12L);
    PowerMockito.when(mrCfgProcedureTelRepository.getScheduleInProcedure(any())).thenReturn(mrScheduleTelDTOS);
    result = mrCfgProcedureTelHardBusiness.update(mrCfgProcedureTelDTO);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void test_delete01() {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    MrScheduleTelDTO scheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    scheduleTelDTO.setProcedureId(1L);

    List<MrScheduleTelDTO> lstMrScheduleTel = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrCfgProcedureTelRepository.getScheduleInProcedure(any()))
        .thenReturn(lstMrScheduleTel);

    MrDeviceDTO dtoDevice = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> lstMrDevice = Mockito.spy(ArrayList.class);

    PowerMockito.when(mrCfgProcedureTelRepository.delete(69L)).thenReturn(resultInSideDto);

    ResultInSideDto result = mrCfgProcedureTelHardBusiness.delete(69L);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void test_delete02() {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    MrScheduleTelDTO scheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    scheduleTelDTO.setProcedureId(1L);

    List<MrScheduleTelDTO> lstMrScheduleTel = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrCfgProcedureTelRepository.getScheduleInProcedure(any()))
        .thenReturn(lstMrScheduleTel);

    MrDeviceDTO dtoDevice = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> lstMrDevice = Mockito.spy(ArrayList.class);
    scheduleTelDTO.setCycleType("M");
    scheduleTelDTO.setMrHardCycle(3L);
    scheduleTelDTO.setDeviceId(1L);
    lstMrScheduleTel.add(scheduleTelDTO);
    dtoDevice.setDeviceId(1L);
    MrDeviceDTO dtoOld = Mockito.spy(MrDeviceDTO.class);
    dtoOld.setDeviceId(10L);
    dtoOld.setIsComplete3m(3L);
    lstMrDevice.add(dtoOld);
    PowerMockito.when(mrDeviceRepository.findMrDeviceById(1L)).thenReturn(dtoOld);

//    PowerMockito.when(mrDeviceBusiness.insertOrUpdateListDevice(lstMrDevice))
//        .thenReturn(resultInSideDto);

    PowerMockito.when(mrCfgProcedureTelRepository.delete(69L)).thenReturn(resultInSideDto);

    PowerMockito.when(mrScheduleTelRepository.deleteListSchedule(lstMrScheduleTel))
        .thenReturn(resultInSideDto);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abcdef");

    ResultInSideDto result = mrCfgProcedureTelHardBusiness.delete(69L);
    Assert.assertEquals(resultInSideDto.getKey(), result.getKey());
  }

  @Test
  public void test_getDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setMrWorks("1");
    PowerMockito.when(mrCfgProcedureTelRepository.getDetail(anyLong()))
        .thenReturn(mrCfgProcedureTelDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("DDD");
    List<CatItemDTO> lstCatItem = Mockito.spy(ArrayList.class);
    CatItemDTO itemDTO = Mockito.spy(CatItemDTO.class);
    itemDTO.setItemId(1L);
    itemDTO.setItemName("1");
    lstCatItem.add(itemDTO);
    PowerMockito.when(commonStreamServiceProxy.getListCatItemDTO(any()))
        .thenReturn(lstCatItem);
    MrCfgProcedureTelDTO dto = mrCfgProcedureTelHardBusiness.getDetail(68L);
    Assert.assertEquals(dto, mrCfgProcedureTelDTO);
  }

  @Test
  public void test_importMrCfgProcedureTelHard_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> lstHeader = new ArrayList<>();
    lstHeader.add(new Object[100]);
    Object[] objects = new Object[19];
    objects[1] = "281";
    objects[2] = "cccc";
    objects[3] = "a";
    objects[4] = "b";
    objects[5] = "M";
    objects[6] = "Month";
    objects[7] = "H";
    objects[8] = "Duong";
    objects[9] = "cc";
    objects[10] = "d";
    objects[11] = "12";
    objects[12] = "Muc 1";
    objects[13] = "32";
    objects[14] = "anh huong muc 1";
    objects[15] = "1";
    objects[16] = "bcbc";
    objects[17] = "26/03/2020";
    objects[18] = "kkkk";
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects);

    List<CatItemDTO> dataOgWork = Mockito.spy(ArrayList.class);
    List<CatItemDTO> lstSubCat = Mockito.spy(ArrayList.class);
    List<MrCfgProcedureTelDTO> lstAddOrUpdate = Mockito.spy(ArrayList.class);

    PowerMockito.when(commonImport.getDataFromExcel(any(), anyInt(), anyInt(),
        anyInt(), anyInt(), anyInt())).thenReturn(lstData);

    MrCfgProcedureTelDTO dto = Mockito.spy(MrCfgProcedureTelDTO.class);
    dto.setMarketCode("281");
    dto.setArrayCode("cccc");
    dto.setNetworkType("a");
    dto.setDeviceType("b");
    dto.setCycleType("M");
    dto.setCycleStr("Month");
    dto.setMrMode("H");
    dto.setProcedureName("Duong");
    dto.setGenMrBeforeStr("cc");
    dto.setMrContentId("d");
    dto.setMrTimeStr("12");
    dto.setPriorityCode("Muc 1");
    dto.setReGenMrAfterStr("32");
    dto.setImpact("anh huong muc 1");
    dto.setStatus("1");
    dto.setMrWorks("bcbc");
    dto.setStrExpDate("26/03/2020");
    dto.setWoContent("kkkk");
    lstAddOrUpdate.add(dto);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType"))
        .thenReturn("sheetFive");
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent"))
        .thenReturn("sheetSix");
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrWork"))
        .thenReturn("sheetSeven");
    resultInSideDto.setKey(RESULT.SUCCESS);

    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    Workbook workbook = Mockito.spy(Workbook.class);
    PowerMockito.mockStatic(WorkbookFactory.class);
    PowerMockito.when(WorkbookFactory.create(any(InputStream.class))).thenReturn(workbook);
    PowerMockito.when(workbook.createFont()).thenReturn(Mockito.spy(Font.class));
    PowerMockito.when(workbook.createCellStyle()).thenReturn(Mockito.spy(CellStyle.class));
    Sheet sheetFile = Mockito.spy(Sheet.class);
    PowerMockito.when(workbook.createSheet("sheetFive")).thenReturn(sheetFile);
    PowerMockito.when(workbook.createSheet("sheetSix")).thenReturn(sheetFile);
    PowerMockito.when(workbook.createSheet("sheetSeven")).thenReturn(sheetFile);
    PowerMockito.when(workbook.getSheetAt(anyInt())).thenReturn(sheetFile);
    Row row = Mockito.spy(Row.class);
    PowerMockito.when(sheetFile.createRow(anyInt())).thenReturn(row);
    PowerMockito.when(row.createCell(anyInt())).thenReturn(Mockito.spy(Cell.class));
    PowerMockito.when(sheetFile.getRow(anyInt())).thenReturn(row);
    PowerMockito.when(row.getCell(anyInt())).thenReturn(Mockito.spy(Cell.class));

    List<MrDeviceDTO> lstMrDevice = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrCheckListRepository.getListNetworkType()).thenReturn(lstMrDevice);

    ResultInSideDto result = mrCfgProcedureTelHardBusiness
        .importMrCfgProcedureTelHard(multipartFile);
    Assert.assertNotNull(result);
    Assert.assertEquals(Constants.RESULT.ERROR, result.getKey());
  }

  @Test
  public void test_importMrCfgProcedureTelHard_02() throws Exception {
    //Case ValidateFileImport False
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/mr-temp");
    PowerMockito.mockStatic(CommonImport.class);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("DDD");

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    ResultInSideDto result = mrCfgProcedureTelHardBusiness
        .importMrCfgProcedureTelHard(multipartFile);
    Assert.assertEquals(result.getKey(), RESULT.ERROR);
  }

  @Test
  public void test_importMrCfgProcedureTelHard_03() throws Exception {
    //Case NODATA
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> lstHeader = new ArrayList<>();
    lstHeader.add(new Object[100]);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);

    List<CatItemDTO> dataOgWork = Mockito.spy(ArrayList.class);
    List<CatItemDTO> lstSubCat = Mockito.spy(ArrayList.class);
    List<MrCfgProcedureTelDTO> lstAddOrUpdate = Mockito.spy(ArrayList.class);

    PowerMockito.when(commonImport.getDataFromExcel(any(), anyInt(), anyInt(),
        anyInt(), anyInt(), anyInt())).thenReturn(lstData);

    PowerMockito.mockStatic(I18n.class);
    resultInSideDto.setKey(RESULT.NODATA);

    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    ResultInSideDto result = mrCfgProcedureTelHardBusiness
        .importMrCfgProcedureTelHard(multipartFile);
    Assert.assertNotNull(result);
    Assert.assertEquals(Constants.RESULT.NODATA, result.getKey());
  }

  @Test
  public void test_importMrCfgProcedureTelHard_04() throws Exception {
    //Truong hop cac truong bi null
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> lstHeader = new ArrayList<>();
    lstHeader.add(new Object[100]);
    Object[] objects = new Object[19];
    objects[1] = null;
    objects[2] = null;
    objects[3] = null;
    objects[4] = null;
    objects[5] = null;
    objects[6] = null;
    objects[7] = null;
    objects[8] = null;
    objects[9] = null;
    objects[10] = null;
    objects[11] = null;
    objects[12] = null;
    objects[13] = null;
    objects[14] = null;
    objects[15] = null;
    objects[16] = null;
    objects[17] = "27/03/2020";
    objects[18] = null;
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects);

    List<CatItemDTO> dataOgWork = Mockito.spy(ArrayList.class);
    List<CatItemDTO> lstSubCat = Mockito.spy(ArrayList.class);
    List<MrCfgProcedureTelDTO> lstAddOrUpdate = Mockito.spy(ArrayList.class);

    PowerMockito.when(commonImport.getDataFromExcel(any(), anyInt(), anyInt(),
        anyInt(), anyInt(), anyInt())).thenReturn(lstData);

    MrCfgProcedureTelDTO dto = Mockito.spy(MrCfgProcedureTelDTO.class);
    dto.setMarketCode(null);
    dto.setArrayCode(null);
    dto.setNetworkType(null);
    dto.setDeviceType(null);
    dto.setCycleType(null);
    dto.setCycleStr(null);
    dto.setMrMode(null);
    dto.setProcedureName(null);
    dto.setGenMrBeforeStr(null);
    dto.setMrContentId(null);
    dto.setMrTimeStr(null);
    dto.setPriorityCode(null);
    dto.setReGenMrAfterStr(null);
    dto.setImpact(null);
    dto.setStatus(null);
    dto.setMrWorks(null);
    dto.setStrExpDate("27/03/2020");
    dto.setWoContent(null);
    lstAddOrUpdate.add(dto);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType"))
        .thenReturn("sheetFive");
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent"))
        .thenReturn("sheetSix");
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrWork"))
        .thenReturn("sheetSeven");
    resultInSideDto.setKey(RESULT.SUCCESS);

    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);
    Workbook workbook = Mockito.spy(Workbook.class);

    PowerMockito.mockStatic(WorkbookFactory.class);
    PowerMockito.when(WorkbookFactory.create(any(InputStream.class))).thenReturn(workbook);
    PowerMockito.when(workbook.createFont()).thenReturn(Mockito.spy(Font.class));
    PowerMockito.when(workbook.createCellStyle()).thenReturn(Mockito.spy(CellStyle.class));
    Sheet sheetFile = Mockito.spy(Sheet.class);
    PowerMockito.when(workbook.createSheet("sheetFive")).thenReturn(sheetFile);
    PowerMockito.when(workbook.createSheet("sheetSix")).thenReturn(sheetFile);
    PowerMockito.when(workbook.createSheet("sheetSeven")).thenReturn(sheetFile);
    PowerMockito.when(workbook.getSheetAt(anyInt())).thenReturn(sheetFile);
    Row row = Mockito.spy(Row.class);
    PowerMockito.when(sheetFile.createRow(anyInt())).thenReturn(row);
    PowerMockito.when(row.createCell(anyInt())).thenReturn(Mockito.spy(Cell.class));
    PowerMockito.when(sheetFile.getRow(anyInt())).thenReturn(row);
    PowerMockito.when(row.getCell(anyInt())).thenReturn(Mockito.spy(Cell.class));
    ResultInSideDto result = mrCfgProcedureTelHardBusiness
        .importMrCfgProcedureTelHard(multipartFile);
    Assert.assertNotNull(result);
    Assert.assertEquals(Constants.RESULT.ERROR, result.getKey());
  }

  @Test
  public void test_importMrCfgProcedureTelHard_05() throws Exception {
    //Truong hop cac truong bi null
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    List<Object[]> lstHeader = new ArrayList<>();
    lstHeader.add(new Object[100]);
    Object[] objects = new Object[19];
    objects[1] = null;
    objects[2] = null;
    objects[3] = null;
    objects[4] = null;
    objects[5] = null;
    objects[6] = null;
    objects[7] = null;
    objects[8] = null;
    objects[9] = null;
    objects[10] = null;
    objects[11] = null;
    objects[12] = null;
    objects[13] = null;
    objects[14] = null;
    objects[15] = null;
    objects[16] = null;
    objects[17] = "27/03/2020";
    objects[18] = null;
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(objects);

    List<CatItemDTO> dataOgWork = Mockito.spy(ArrayList.class);
    List<CatItemDTO> lstSubCat = Mockito.spy(ArrayList.class);
    List<MrCfgProcedureTelDTO> lstAddOrUpdate = Mockito.spy(ArrayList.class);

    PowerMockito.when(commonImport.getDataFromExcel(any(), anyInt(), anyInt(),
        anyInt(), anyInt(), anyInt())).thenReturn(lstData);

    MrCfgProcedureTelDTO dto = Mockito.spy(MrCfgProcedureTelDTO.class);
    lstAddOrUpdate.add(dto);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType"))
        .thenReturn("sheetFive");
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent"))
        .thenReturn("sheetSix");
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrWork"))
        .thenReturn("sheetSeven");
    resultInSideDto.setKey(RESULT.SUCCESS);

    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);

    Workbook workbook = Mockito.spy(Workbook.class);
    PowerMockito.mockStatic(WorkbookFactory.class);
    PowerMockito.when(WorkbookFactory.create(any(InputStream.class))).thenReturn(workbook);
    PowerMockito.when(workbook.createFont()).thenReturn(Mockito.spy(Font.class));
    PowerMockito.when(workbook.createCellStyle()).thenReturn(Mockito.spy(CellStyle.class));
    Sheet sheetFile = Mockito.spy(Sheet.class);
    PowerMockito.when(workbook.createSheet("sheetFive")).thenReturn(sheetFile);
    PowerMockito.when(workbook.createSheet("sheetSix")).thenReturn(sheetFile);
    PowerMockito.when(workbook.createSheet("sheetSeven")).thenReturn(sheetFile);
    PowerMockito.when(workbook.getSheetAt(anyInt())).thenReturn(sheetFile);
    Row row = Mockito.spy(Row.class);
    PowerMockito.when(sheetFile.createRow(anyInt())).thenReturn(row);
    PowerMockito.when(row.createCell(anyInt())).thenReturn(Mockito.spy(Cell.class));
    PowerMockito.when(sheetFile.getRow(anyInt())).thenReturn(row);
    PowerMockito.when(row.getCell(anyInt())).thenReturn(Mockito.spy(Cell.class));
    ResultInSideDto result = mrCfgProcedureTelHardBusiness
        .importMrCfgProcedureTelHard(multipartFile);
    Assert.assertNotNull(result);
    Assert.assertEquals(Constants.RESULT.ERROR, result.getKey());
  }

  @Test
  public void test_importMrCfgProcedureTelHard_06() throws Exception {
    //Case FILE_IS_NULL
    Logger logger = PowerMockito.mock(Logger.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);
    // Mock static method
    PowerMockito.mockStatic(FileUtils.class);
    when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/temp");
    PowerMockito.mockStatic(CommonImport.class);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Chưa chọn file import");
    resultInSideDto.setKey(RESULT.FILE_IS_NULL);

    MultipartFile multipartFile = PowerMockito.mock(MultipartFile.class);

    ResultInSideDto result = mrCfgProcedureTelHardBusiness
        .importMrCfgProcedureTelHard(multipartFile);
    Assert.assertNotNull(result);
    Assert.assertEquals(resultInSideDto.getKey(), RESULT.FILE_IS_NULL);
  }

  @Test
  public void test_getFileTemplate() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    Workbook workBook = Mockito.spy(Workbook.class);
    ExcelWriterUtils ewu = Mockito.spy(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("ABC");
    String tempFolder = "./mr-temp";
    String fileTemplateName = "IMPORT_CFG_PROCEDURE_TEL_HARD_TEMPLATE_EN.xlsx";
    String pathFolder =
        tempFolder + File.separator + FileUtils.createPathByDate(new Date()) + File.separator;
    ewu.saveToFileExcel(workBook, pathFolder, fileTemplateName);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> data = Mockito.spy(ArrayList.class);
    data.add(catItemDTO);

    MrDeviceDTO deviceDTO = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> list = Mockito.spy(ArrayList.class);
    list.add(deviceDTO);
    PowerMockito.when(mrCfgProcedureTelRepository.getListNetworkType()).thenReturn(list);

    //Tao tieu de
    Font xssFontTitle = Mockito.spy(Font.class);
    xssFontTitle.setFontName("Times New Roman");
    xssFontTitle.setFontHeightInPoints((short) 22);
    xssFontTitle.setColor(IndexedColors.BLACK.index);
    xssFontTitle.setBold(true);

    //set font chữ cho header
    Font xSSFFontHeader = Mockito.spy(Font.class);
    xSSFFontHeader.setFontName("Times New Roman");
    xSSFFontHeader.setFontHeightInPoints((short) 12);
    xSSFFontHeader.setColor(IndexedColors.BLACK.index);
    xSSFFontHeader.setBold(true);

    CellStyle cellStyleHeader = Mockito.spy(CellStyle.class);
    cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleHeader.setBorderLeft(BorderStyle.THIN);
    cellStyleHeader.setBorderBottom(BorderStyle.THIN);
    cellStyleHeader.setBorderRight(BorderStyle.THIN);

    HSSFWorkbook hwb = null;
    hwb = new HSSFWorkbook();
    HSSFPalette palette = hwb.getCustomPalette();
    //set màu cho header
    HSSFColor myColor = palette
        .findSimilarColor(Integer.valueOf(146), Integer.valueOf(208),
            Integer.valueOf(80));
    cellStyleHeader.setBorderTop(BorderStyle.THIN);
    cellStyleHeader.setFillForegroundColor(myColor.getIndex());
    cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyleHeader.setWrapText(true);
    cellStyleHeader.setFont(xSSFFontHeader);

    MrDeviceDTO deviceDTO1 = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> listDeviceType2 = Mockito.spy(ArrayList.class);
    listDeviceType2.add(deviceDTO1);
    PowerMockito.when(mrCfgProcedureTelRepository.getListDeviceType2()).thenReturn(listDeviceType2);

    List<CatItemDTO> dataMrContent = Mockito.spy(ArrayList.class);
    dataMrContent.add(catItemDTO);
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent"))
        .thenReturn("sheetSix");

    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrWork"))
        .thenReturn("sheetSeven");
    List<CatItemDTO> lstDataArrayCode = Mockito.spy(ArrayList.class);
    lstDataArrayCode.add(catItemDTO);
    PowerMockito.when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(lstDataArrayCode);
    File result = mrCfgProcedureTelHardBusiness.getFileTemplate();
    Assert.assertNotNull(result);
  }

  @Test
  public void test_processFileTemplate() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    Workbook workBook = Mockito.spy(Workbook.class);
    ExcelWriterUtils ewu = Mockito.spy(ExcelWriterUtils.class);
    PowerMockito.mockStatic(I18n.class);
    String fileTemplateName = "IMPORT_CFG_PROCEDURE_TEL_HARD_TEMPLATE_EN.xlsx";
    String pathFileImport = pathTemplate + fileTemplateName;
    workBook = ewu.readFileExcelFromTemplate(pathFileImport);

    //Quốc gia
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);

    //Mảng
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> data = Mockito.spy(ArrayList.class);
    data.add(catItemDTO);

    //Loại mạng
    MrDeviceDTO deviceDTO = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> list = Mockito.spy(ArrayList.class);
    list.add(deviceDTO);

    //Tao tieu de
    Font xssFontTitle = Mockito.spy(Font.class);
    xssFontTitle.setFontName("Times New Roman");
    xssFontTitle.setFontHeightInPoints((short) 22);
    xssFontTitle.setColor(IndexedColors.BLACK.index);
    xssFontTitle.setBold(true);

    //set font chữ cho header
    Font xSSFFontHeader = Mockito.spy(Font.class);
    xSSFFontHeader.setFontName("Times New Roman");
    xSSFFontHeader.setFontHeightInPoints((short) 12);
    xSSFFontHeader.setColor(IndexedColors.BLACK.index);
    xSSFFontHeader.setBold(true);

    CellStyle cellStyleHeader = Mockito.spy(CellStyle.class);
    cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleHeader.setBorderLeft(BorderStyle.THIN);
    cellStyleHeader.setBorderBottom(BorderStyle.THIN);
    cellStyleHeader.setBorderRight(BorderStyle.THIN);

    HSSFWorkbook hwb = null;
    hwb = new HSSFWorkbook();
    HSSFPalette palette = hwb.getCustomPalette();
    //set màu cho header
    HSSFColor myColor = palette
        .findSimilarColor(Integer.valueOf(146), Integer.valueOf(208),
            Integer.valueOf(80));
    cellStyleHeader.setBorderTop(BorderStyle.THIN);
    cellStyleHeader.setFillForegroundColor(myColor.getIndex());
    cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyleHeader.setWrapText(true);
    cellStyleHeader.setFont(xSSFFontHeader);

    //Loại thiết bị
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType"))
        .thenReturn("sheetFive");
    MrDeviceDTO deviceDTO1 = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> listDeviceType2 = Mockito.spy(ArrayList.class);
    listDeviceType2.add(deviceDTO1);

    //Loại hoạt động
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent"))
        .thenReturn("sheetSix");
    List<CatItemDTO> dataMrContent = Mockito.spy(ArrayList.class);
    dataMrContent.add(catItemDTO);

    //Đầu việc
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrWork"))
        .thenReturn("sheetSeven");
    List<CatItemDTO> lstDataArrayCode = Mockito.spy(ArrayList.class);
    lstDataArrayCode.add(catItemDTO);

    mrCfgProcedureTelHardBusiness.processFileTemplate(ewu);
    Assert.assertNotNull(workBook);
  }

  @Test
  public void test_exportSearchData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.mockStatic(I18n.class);
    List<MrCfgProcedureTelDTO> lstData = Mockito.spy(ArrayList.class);
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrCfgProcedureTelDTO.setCycleType("Ngay");
    lstData.add(mrCfgProcedureTelDTO);
    PowerMockito.when(mrCfgProcedureTelRepository.onSearchExport(any(), anyString()))
        .thenReturn(lstData);
    MrCfgProcedureTelDTO telDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    File file = new File("TEMPLATE_EXPORT_EN.xlsx");
    PowerMockito.mockStatic(CommonExport.class);

    mrCfgProcedureTelHardBusiness.exportSearchData(telDTO);
    Assert.assertNotNull(file);
  }

  @Test
  public void test_exportFileImport() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ExcelWriterUtils ewu = Mockito.spy(ExcelWriterUtils.class);
    PowerMockito.mockStatic(FileUtils.class);
    Workbook workbook = Mockito.spy(Workbook.class);

    String fileTemplateName = "IMPORT_CFG_PROCEDURE_TEL_HARD_TEMPLATE_EN.xlsx";
    String pathFileImport = pathTemplate + fileTemplateName;
    workbook = ewu.readFileExcelFromTemplate(pathFileImport);

    //Quốc gia
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    List<ItemDataCRInside> lstCountry = Mockito.spy(ArrayList.class);
    lstCountry.add(itemDataCRInside);

    //Mảng
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> data = Mockito.spy(ArrayList.class);
    data.add(catItemDTO);

    //Loại mạng
    MrDeviceDTO deviceDTO = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> list = Mockito.spy(ArrayList.class);
    list.add(deviceDTO);

    //Tao tieu de
    Font xssFontTitle = Mockito.spy(Font.class);
    xssFontTitle.setFontName("Times New Roman");
    xssFontTitle.setFontHeightInPoints((short) 22);
    xssFontTitle.setColor(IndexedColors.BLACK.index);
    xssFontTitle.setBold(true);

    //set font chữ cho header
    Font xSSFFontHeader = Mockito.spy(Font.class);
    xSSFFontHeader.setFontName("Times New Roman");
    xSSFFontHeader.setFontHeightInPoints((short) 12);
    xSSFFontHeader.setColor(IndexedColors.BLACK.index);
    xSSFFontHeader.setBold(true);

    CellStyle cellStyleHeader = Mockito.spy(CellStyle.class);
    cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleHeader.setBorderLeft(BorderStyle.THIN);
    cellStyleHeader.setBorderBottom(BorderStyle.THIN);
    cellStyleHeader.setBorderRight(BorderStyle.THIN);

    HSSFWorkbook hwb = null;
    hwb = new HSSFWorkbook();
    HSSFPalette palette = hwb.getCustomPalette();
    //set màu cho header
    HSSFColor myColor = palette
        .findSimilarColor(Integer.valueOf(146), Integer.valueOf(208),
            Integer.valueOf(80));
    cellStyleHeader.setBorderTop(BorderStyle.THIN);
    cellStyleHeader.setFillForegroundColor(myColor.getIndex());
    cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyleHeader.setWrapText(true);
    cellStyleHeader.setFont(xSSFFontHeader);

    //Loại thiết bị
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType"))
        .thenReturn("sheetFive");
    MrDeviceDTO deviceDTO1 = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> listDeviceType2 = Mockito.spy(ArrayList.class);
    listDeviceType2.add(deviceDTO1);

    //Loại hoạt động
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent"))
        .thenReturn("sheetSix");
    List<CatItemDTO> dataMrContent = Mockito.spy(ArrayList.class);
    dataMrContent.add(catItemDTO);

    //Đầu việc
    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrWork"))
        .thenReturn("sheetSeven");
    List<CatItemDTO> lstDataArrayCode = Mockito.spy(ArrayList.class);
    lstDataArrayCode.add(catItemDTO);

    PowerMockito.when(I18n.getLanguage("cfgProcedureHardView.list.grid.importError"))
        .thenReturn("KetQua");

    List<Object[]> lstData = new ArrayList<>();
    lstData.add(new Object[100]);

    String fileTempPath = "./mr-temp/test.xlsx";

    ErrorInfo Erro = Mockito.spy(ErrorInfo.class);
    List<ErrorInfo> lstError = Mockito.spy(ArrayList.class);
    lstError.add(Erro);
    mrCfgProcedureTelHardBusiness.exportFileImport(lstData, fileTempPath, lstError);
    Assert.assertNotNull(workbook);
  }

  @Test
  public void test_setMapArrayByNetworkType() {
    MrDeviceDTO deviceDTO = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> deviceDTOList = Mockito.spy(ArrayList.class);
    deviceDTO.setNetworkType("aa");
    deviceDTO.setArrayCode("xx");
    deviceDTOList.add(deviceDTO);
    mrCfgProcedureTelHardBusiness.setMapArrayByNetworkType();
    Assert.assertEquals(deviceDTOList.size(), 1L);
  }

  @Test
  public void test_setMapNetworkTypeByDeviceType() {
    MrDeviceDTO deviceDTO = Mockito.spy(MrDeviceDTO.class);
    List<MrDeviceDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    deviceDTO.setDeviceType("aa");
    deviceDTO.setNetworkType("xx");
    mrDeviceDTOList.add(deviceDTO);
    mrCfgProcedureTelHardBusiness.setMapNetworkTypeByDeviceType();
    Assert.assertEquals(mrDeviceDTOList.size(), 1L);
  }

  @Test
  public void test_setMapArrayByMrWork() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> dataWorkList = Mockito.spy(ArrayList.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setParentItemId(3L);
    dataWorkList.add(catItemDTO);
    mrCfgProcedureTelHardBusiness.setMapArrayByMrWork();
    Assert.assertEquals(dataWorkList.size(), 1L);
  }

  @Test
  public void test_setMapNetworkType() {
    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    lstNetwork.add(mrDeviceDTO);
    PowerMockito.when(mrCheckListRepository.getListNetworkType()).thenReturn(lstNetwork);
    mrCfgProcedureTelHardBusiness.setMapNetworkType();
    Assert.assertEquals(lstNetwork.size(), 1L);
  }

  @Test
  public void test_setMapDeviceType() {
    List<MrDeviceDTO> lstNetwork = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setNetworkType("1");
    lstNetwork.add(mrDeviceDTO);
    PowerMockito.when(mrCheckListRepository.getListDeviceType()).thenReturn(lstNetwork);
    mrCfgProcedureTelHardBusiness.setMapDeviceType();
    Assert.assertEquals(lstNetwork.size(), 1L);
  }

  @Test
  public void test_setMapMrType() {
    List<CatItemDTO> dataMrContent = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    dataMrContent.add(catItemDTO);
    PowerMockito.when(catItemBusiness.getListItemByCategoryAndParent(MR_ITEM_NAME.MR_TYPE, null)).thenReturn(dataMrContent);
    mrCfgProcedureTelHardBusiness.setMapMrType();
    Assert.assertEquals(dataMrContent.size(), 1L);
  }
}
