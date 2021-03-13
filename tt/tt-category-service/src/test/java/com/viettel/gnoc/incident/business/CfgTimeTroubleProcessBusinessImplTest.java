package com.viettel.gnoc.incident.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.CFG_TIME_TROUBLE_PROCESS_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.repository.CfgTimeTroubleProcessRepository;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest(
    {CfgTimeTroubleProcessBusinessImpl.class, CommonExport.class,
        CommonImport.class, FileUtils.class, I18n.class, TicketProvider.class}
)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CfgTimeTroubleProcessBusinessImplTest {

  @InjectMocks
  CfgTimeTroubleProcessBusinessImpl cfgTimeTroubleProcessBusiness;

  @Mock
  CfgTimeTroubleProcessRepository cfgTimeTroubleProcessRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  TicketProvider ticketProvider;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void testGetListCfgTimeTroubleProcessDTO_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(datatable.getTotal()).thenReturn(10);
    when(cfgTimeTroubleProcessRepository.getListCfgTimeTroubleProcessDTO(any()))
        .thenReturn(datatable);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    Datatable result = cfgTimeTroubleProcessBusiness
        .getListCfgTimeTroubleProcessDTO(new CfgTimeTroubleProcessDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void testGetConfigTimeTroubleProcess_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    CfgTimeTroubleProcessDTO expected = Mockito.spy(CfgTimeTroubleProcessDTO.class);
    PowerMockito.when(
        cfgTimeTroubleProcessRepository
            .getConfigTimeTroubleProcess(anyLong(), anyLong(), anyLong(), anyString())
    ).thenReturn(expected);

    CfgTimeTroubleProcessDTO input = Mockito.spy(CfgTimeTroubleProcessDTO.class);
    input.setTypeId(1L);
    input.setSubCategoryId(1L);
    input.setPriorityId(1L);
    input.setCountry("VietNam");
    CfgTimeTroubleProcessDTO actual = cfgTimeTroubleProcessBusiness
        .getConfigTimeTroubleProcess(input);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFindCfgTimeTroubleProcessById_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO = new CfgTimeTroubleProcessDTO();
    when(cfgTimeTroubleProcessRepository.findCfgTimeTroubleProcessById(any()))
        .thenReturn(cfgTimeTroubleProcessDTO);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    CfgTimeTroubleProcessDTO result = cfgTimeTroubleProcessBusiness
        .findCfgTimeTroubleProcessById(10L);
    assertEquals(cfgTimeTroubleProcessDTO, result);
  }

  @Test
  public void testInsertCfgTimeTroubleProcess_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(resultInSideDto.getKey()).thenReturn(RESULT.SUCCESS);
    when(cfgTimeTroubleProcessRepository.insertCfgTimeTroubleProcess(any())).thenReturn(
        resultInSideDto);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO = new CfgTimeTroubleProcessDTO();
    ResultInSideDto result = cfgTimeTroubleProcessBusiness
        .insertCfgTimeTroubleProcess(cfgTimeTroubleProcessDTO);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testUpdateCfgTimeTroubleProcess_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(resultInSideDto.getKey()).thenReturn(RESULT.SUCCESS);
    when(cfgTimeTroubleProcessRepository.updateCfgTimeTroubleProcess(any()))
        .thenReturn(resultInSideDto);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO = PowerMockito
        .mock(CfgTimeTroubleProcessDTO.class);
    ResultInSideDto result = cfgTimeTroubleProcessBusiness
        .updateCfgTimeTroubleProcess(cfgTimeTroubleProcessDTO);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testInsertOrUpdateListCfgTimeTroubleProcess_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(cfgTimeTroubleProcessRepository.insertOrUpdateListCfgTimeTroubleProcess(any()))
        .thenReturn(resultInSideDto);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    List<CfgTimeTroubleProcessDTO> list = new ArrayList<>();
    CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO = new CfgTimeTroubleProcessDTO();
    cfgTimeTroubleProcessDTO.setId(100L);
    list.add(cfgTimeTroubleProcessDTO);
    ResultInSideDto result = cfgTimeTroubleProcessBusiness
        .insertOrUpdateListCfgTimeTroubleProcess(list);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testDeleteCfgTimeTroubleProcess_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(resultInSideDto.getKey()).thenReturn(RESULT.SUCCESS);
    when(cfgTimeTroubleProcessRepository.deleteCfgTimeTroubleProcess(any())).thenReturn(
        resultInSideDto);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    ResultInSideDto result = cfgTimeTroubleProcessBusiness.deleteCfgTimeTroubleProcess(123L);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testDeleteListCfgTimeTroubleProcess_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = PowerMockito.mock(ResultInSideDto.class);
    when(resultInSideDto.getId()).thenReturn(10L);
    when(resultInSideDto.getKey()).thenReturn(RESULT.SUCCESS);
    when(cfgTimeTroubleProcessRepository.deleteCfgTimeTroubleProcess(any())).thenReturn(
        resultInSideDto);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    List<CfgTimeTroubleProcessDTO> list = new ArrayList<>();
    CfgTimeTroubleProcessDTO dto1 = new CfgTimeTroubleProcessDTO();
    CfgTimeTroubleProcessDTO dto2 = new CfgTimeTroubleProcessDTO();
    dto1.setId(100L);
    dto2.setId(101L);
    list.add(dto1);
    list.add(dto2);
    ResultInSideDto result = cfgTimeTroubleProcessBusiness.deleteListCfgTimeTroubleProcess(list);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void testGetSequenseCfgTimeTroubleProcess_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<String> list = new ArrayList<>();
    list.add("111");
    when(cfgTimeTroubleProcessRepository.getSequenseCfgTimeTroubleProcess(any(), anyInt()))
        .thenReturn(list);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    List<String> result = cfgTimeTroubleProcessBusiness.getSequenseCfgTimeTroubleProcess(1);
    assertEquals(list.get(0), result.get(0));
  }

  @Test
  public void testGetListCfgTimeTroubleProcessByCondition_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CfgTimeTroubleProcessDTO> listCfgTimeTroubleProcessDTO = new ArrayList<>();
    when(cfgTimeTroubleProcessRepository
        .getListCfgTimeTroubleProcessByCondition(any(), anyInt(), anyInt(), any(), any()))
        .thenReturn(listCfgTimeTroubleProcessDTO);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    List<ConditionBean> list = new ArrayList<>();
    ConditionBean conditionBean = new ConditionBean();
    conditionBean.setField("1");
    conditionBean.setType("1");
    conditionBean.setValue("1");
    list.add(conditionBean);
    List<CfgTimeTroubleProcessDTO> result = cfgTimeTroubleProcessBusiness
        .getListCfgTimeTroubleProcessByCondition(list, 1, 5, "ASC", "id");
    assertEquals(listCfgTimeTroubleProcessDTO, result);
  }

  @Test
  public void testGetItemDTO_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CatItemDTO> listCatItemDTO = new ArrayList<>();
    when(cfgTimeTroubleProcessRepository.getItemDTO(anyString(), anyString(), anyString()))
        .thenReturn(listCatItemDTO);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    List<CatItemDTO> result = cfgTimeTroubleProcessBusiness.getItemDTO("1", "1", "NOC");
    assertEquals(listCatItemDTO, result);
  }

  @Test
  public void testGetListSubCategory_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CatItemDTO> listCatItemDTO = new ArrayList<>();
    when(cfgTimeTroubleProcessRepository.getListSubCategory(any())).thenReturn(listCatItemDTO);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    List<CatItemDTO> result = cfgTimeTroubleProcessBusiness.getListSubCategory(1L);
    assertEquals(listCatItemDTO, result);
  }

  @Test
  public void testSetMapCountry_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    List<CatItemDTO> list = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("Test");
    list.add(catItemDTO);
    when(datatable.getData()).thenReturn((List) list);
    when(catItemRepository.getItemMaster(CFG_TIME_TROUBLE_PROCESS_MASTER_CODE.GNOC_COUNTRY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(), Constants.ITEM_ID,
        Constants.ITEM_NAME)).thenReturn(datatable);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    cfgTimeTroubleProcessBusiness.setMapCountry();
  }

  @Test
  public void testSetMapType_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    List<CatItemDTO> list = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("Test");
    list.add(catItemDTO);
    when(datatable.getData()).thenReturn((List) list);
    when(catItemRepository.getItemMaster(CFG_TIME_TROUBLE_PROCESS_MASTER_CODE.PT_TYPE,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(), Constants.ITEM_ID,
        Constants.ITEM_NAME)).thenReturn(datatable);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    cfgTimeTroubleProcessBusiness.setMapType();
  }

  @Test
  public void testSetMapSubCategory_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CatItemDTO> list = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("Test");
    list.add(catItemDTO);
    when(cfgTimeTroubleProcessBusiness.getListSubCategory(null)).thenReturn(list);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    cfgTimeTroubleProcessBusiness.setMapSubCategory();
  }

  @Test
  public void testSetMapPriority_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    List<CatItemDTO> list = new ArrayList<>();
    CatItemDTO catItemDTO = new CatItemDTO();
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("Test");
    list.add(catItemDTO);
    when(datatable.getData()).thenReturn((List) list);
    when(catItemRepository.getItemMaster(CFG_TIME_TROUBLE_PROCESS_MASTER_CODE.TT_PRIORITY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(), Constants.ITEM_ID,
        Constants.ITEM_NAME)).thenReturn(datatable);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);
    cfgTimeTroubleProcessBusiness.setMapPriority();
  }

  @Test
  public void testExportData_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<CfgTimeTroubleProcessDTO> dtoList = new ArrayList<>();
    CfgTimeTroubleProcessDTO dto = new CfgTimeTroubleProcessDTO();
    dto.setTypeName("TypeName");
    dto.setSubCategoryName("SubCategoryName");
    dto.setPriorityName("PriorityName");
    dto.setCreateTtTime(1.0);
    dto.setProcessTtTime(1.0);
    dto.setTimeStationVip(1.0);
    dto.setTimeWoVip(1.0);
    dto.setCloseTtTime(1.0);
    dto.setProcessWoTime(1.0);
    dto.setIsCall(1L);
    PowerMockito.mockStatic(I18n.class);
//    BDDMockito.given(I18n.getLanguage("cfgTimeTroubleProcessDTO.isCallName.1")).willReturn("Yes");
//    BDDMockito.given(I18n.getLanguage("cfgTimeTroubleProcessDTO.isCallName.0")).willReturn("No");
    dto.setLastUpdateTime(new Date());
    dto.setCountryName("CountryName");
    dto.setCdAudioName("CdAudioName");
    dto.setFtAudioName("FtAudioName");
    dtoList.add(dto);
    when(cfgTimeTroubleProcessRepository.getListCfgTimeTroubleProcessExport(any()))
        .thenReturn(dtoList);

    setFinalStatic(CfgTimeTroubleProcessBusinessImpl.class.getDeclaredField("log"), logger);

    PowerMockito.mockStatic(CommonExport.class);
    File file = PowerMockito.mock(File.class);
    when(CommonExport.exportExcel(anyString(), anyString(), any(), anyString(), eq("")))
        .thenReturn(file);

    File result = cfgTimeTroubleProcessBusiness.exportData(dto);
    Assert.assertNull(result);
  }

  @Test
  public void testImportData_01() throws Exception {
    MultipartFile multipartFile = null;
    ResultInSideDto actual = cfgTimeTroubleProcessBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportData_02() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any()))
        .thenReturn(filePath);
    File fileImport = new File(filePath);
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        4,
        0,
        5,
        1000))
        .thenReturn(lstHeader);
    ResultInSideDto actual = cfgTimeTroubleProcessBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportData_03() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any()))
        .thenReturn(filePath);
    File fileImport = new File(filePath);
    Object[] objects = new Object[]{
        "1", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)",
        "1 (*)", "1", "1", "1", "1", "1", "1\n1", "1\n1"
    };
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 501; i++) {
      lstData.add(objects);
    }
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        15,
        1000)
    ).thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        6,
        0,
        15,
        1000)
    ).thenReturn(lstData);

    ResultInSideDto actual = cfgTimeTroubleProcessBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportData_04() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any()))
        .thenReturn(filePath);
    File fileImport = new File(filePath);
    Object[] objects = new Object[]{
        "1", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)",
        "1 (*)", "1", "1", "1", "1", "1", "1\n1", "1\n1"
    };
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        15,
        1000)
    ).thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        6,
        0,
        15,
        1000)
    ).thenReturn(lstData);

    ResultInSideDto actual = cfgTimeTroubleProcessBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void testImportData_05() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(anyString(), any(), any()))
        .thenReturn(filePath);
    File fileImport = new File(filePath);
    Object[] objects = new Object[]{
        "1", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)",
        "1 (*)", "1", "1", "1", "1", "1", "1\n1", "1\n1"
    };
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(objects);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1; i++) {
      Object[] objectTest = new Object[20];
      for (int j = 0; j < objectTest.length; j++) {
        if (j == 2) {
          objectTest[j] = "1111";
        } else {
          objectTest[j] = "1";
        }
      }
      lstData.add(objectTest);
    }
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        5,
        0,
        15,
        1000)
    ).thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        fileImport,
        0,
        6,
        0,
        15,
        1000)
    ).thenReturn(lstData);

    Datatable dataCountry = Mockito.spy(Datatable.class);
    CatItemDTO itemDTO = Mockito.spy(CatItemDTO.class);
    itemDTO.setItemId(1L);
    itemDTO.setItemCode("1");
    itemDTO.setItemName("1");
    List<CatItemDTO> lstItemDTO = Mockito.spy(ArrayList.class);
    lstItemDTO.add(itemDTO);
    dataCountry.setData(lstItemDTO);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString())
    ).thenReturn(dataCountry);
    PowerMockito.when(cfgTimeTroubleProcessRepository.getListSubCategory(any()))
        .thenReturn(lstItemDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(cfgTimeTroubleProcessRepository.insertCfgTimeTroubleProcess(any()))
        .thenReturn(resultInSideDto);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    ResultInSideDto actual = cfgTimeTroubleProcessBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void testGetTemplate() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT_TEST");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemCode("123123");
    catItemDTO.setItemId(1L);
    catItemDTO.setItemName("ItemName");
    List<CatItemDTO> listType = Mockito.spy(ArrayList.class);
    listType.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listType);
    PowerMockito.when(
        catItemRepository
            .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString())
    ).thenReturn(datatable);
    PowerMockito.when(cfgTimeTroubleProcessRepository
        .getListSubCategory(anyLong())
    ).thenReturn(listType);

    File actual = cfgTimeTroubleProcessBusiness.getTemplate();

    Assert.assertNotNull(actual);
  }

  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }
}
