package com.viettel.gnoc.mr.business;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrConfigTestXaDTO;
import com.viettel.gnoc.mr.repository.MrConfigTestXaRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrConfigTestXaBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrConfigTestXaBusinessImplTest {

  @InjectMocks
  MrConfigTestXaBusinessImpl mrConfigTestXaBusiness;

  @Mock
  MrConfigTestXaRepository mrConfigTestXaRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Test
  public void getListMrConfigTestXa_01() {
    MrConfigTestXaDTO mrConfigTestXaDTO = Mockito.spy(MrConfigTestXaDTO.class);
    Datatable expected = Mockito.spy(Datatable.class);
    PowerMockito.when(
        mrConfigTestXaRepository.getListMrConfigTestXa(any())
    ).thenReturn(expected);
    Datatable actual = mrConfigTestXaBusiness.getListMrConfigTestXa(mrConfigTestXaDTO);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void insert_01() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Duplicate");
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    MrConfigTestXaDTO mrConfigTestXaDTO = Mockito.spy(MrConfigTestXaDTO.class);
    mrConfigTestXaDTO.setCountry("VietNam");
    mrConfigTestXaDTO.setProvince("HaNoi");
    UserToken userToken = Mockito.spy(UserToken.class);
    List<MrConfigTestXaDTO> list = Mockito.spy(ArrayList.class);
    list.add(mrConfigTestXaDTO);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mrConfigTestXaRepository
            .checkListDTOExisted(any())
    ).thenReturn(list);
    PowerMockito.when(
        mrConfigTestXaRepository.insertOrUpdate(any())
    ).thenReturn(result);

    ResultInSideDto actual = mrConfigTestXaBusiness.insert(mrConfigTestXaDTO);

    Assert.assertEquals(RESULT.DUPLICATE, actual.getKey());
  }

  @Test
  public void insert_02() {
    PowerMockito.mockStatic(TicketProvider.class);
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    MrConfigTestXaDTO mrConfigTestXaDTO = Mockito.spy(MrConfigTestXaDTO.class);
    mrConfigTestXaDTO.setCountry("VietNam");
    mrConfigTestXaDTO.setProvince("HaNoi");
    UserToken userToken = Mockito.spy(UserToken.class);
    List<MrConfigTestXaDTO> list = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mrConfigTestXaRepository
            .checkListDTOExisted(any())
    ).thenReturn(list);
    PowerMockito.when(
        mrConfigTestXaRepository.insertOrUpdate(any())
    ).thenReturn(result);

    ResultInSideDto actual = mrConfigTestXaBusiness.insert(mrConfigTestXaDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void update_01() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Duplicate");
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    MrConfigTestXaDTO mrConfigTestXaDTO = Mockito.spy(MrConfigTestXaDTO.class);
    mrConfigTestXaDTO.setConfigId("1111");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    List<MrConfigTestXaDTO> listDTOS = Mockito.spy(ArrayList.class);
    listDTOS.add(mrConfigTestXaDTO);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mrConfigTestXaRepository
            .checkListDTOExisted(any())
    ).thenReturn(listDTOS);
    PowerMockito.when(
        mrConfigTestXaRepository.insertOrUpdate(any())
    ).thenReturn(result);

    ResultInSideDto actual = mrConfigTestXaBusiness.update(mrConfigTestXaDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void update_02() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("Duplicate");
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    MrConfigTestXaDTO mrConfigTestXaDTO = Mockito.spy(MrConfigTestXaDTO.class);
    mrConfigTestXaDTO.setConfigId("1111");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    List<MrConfigTestXaDTO> listDTOS = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mrConfigTestXaRepository
            .checkListDTOExisted(any())
    ).thenReturn(listDTOS);
    PowerMockito.when(
        mrConfigTestXaRepository.insertOrUpdate(any())
    ).thenReturn(result);

    ResultInSideDto actual = mrConfigTestXaBusiness.update(mrConfigTestXaDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void deleteMrConfigTestXa_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        mrConfigTestXaRepository.deleteMrConfigTestXa(anyLong())
    ).thenReturn(result);

    ResultInSideDto actual = mrConfigTestXaBusiness.deleteMrConfigTestXa(111L);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void getDetail_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrConfigTestXaDTO result = Mockito.spy(MrConfigTestXaDTO.class);
    PowerMockito.when(
        mrConfigTestXaRepository.getDetail(anyLong())
    ).thenReturn(result);
    MrConfigTestXaDTO actual = mrConfigTestXaBusiness.getDetail(111L);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getListStation_01() {
    List<MrConfigTestXaDTO> result = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrConfigTestXaRepository.getListStation()
    ).thenReturn(result);
    List<MrConfigTestXaDTO> actual = mrConfigTestXaBusiness.getListStation();

    Assert.assertNotNull(actual);
  }

  @Test
  public void exportData_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TRIPM TEST");

    MrConfigTestXaDTO mrConfigTestXaDTO = Mockito.spy(MrConfigTestXaDTO.class);
    mrConfigTestXaDTO.setStatus("1");
    List<MrConfigTestXaDTO> mrConfigTestXaDTOS = Mockito.spy(ArrayList.class);
    mrConfigTestXaDTOS.add(mrConfigTestXaDTO);

    PowerMockito.when(
        mrConfigTestXaRepository
            .getDataExport(any())
    ).thenReturn(mrConfigTestXaDTOS);

    File actual = mrConfigTestXaBusiness.exportData(mrConfigTestXaDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void getTemplate_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TRIPM TEST");
    PowerMockito.when(I18n.getLanguage("mrConfigTestXa.title")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrConfigTestXa.list.location")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("mrConfigTestXa.status.sheetName")).thenReturn("3");
    List<ItemDataCRInside> dataCRInsideList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(11L);
    itemDataCRInside.setDisplayStr("TEST");
    dataCRInsideList.add(itemDataCRInside);

    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(dataCRInsideList);

    File actual = mrConfigTestXaBusiness.getTemplate();

    Assert.assertNotNull(actual);
  }

  @Test
  public void importData_01() throws Exception {
    ResultInSideDto actual = mrConfigTestXaBusiness.importData(null);
    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void importData_02() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp";
    PowerMockito.when(
        FileUtils
            .saveTempFile(any(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            7,
            1000
        )
    ).thenReturn(headerList);

    ResultInSideDto actual = mrConfigTestXaBusiness.importData(uploadFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void importData_03() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp";
    PowerMockito.when(
        FileUtils
            .saveTempFile(any(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objecttest = new Object[]{
        "1", "1*", "1*","1*", "1*", "1", "1", "1*"
    };
    headerList.add(objecttest);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1002; i++) {
      dataImportList.add(objecttest);
    }

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 7, 1000
        )
    ).thenReturn(headerList);
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(
            fileImport, 0, 8, 0, 7, 1000
        )
    ).thenReturn(dataImportList);

    ResultInSideDto actual = mrConfigTestXaBusiness.importData(uploadFile);

    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void importData_04() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp";
    PowerMockito.when(
        FileUtils
            .saveTempFile(any(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objecttest = new Object[]{
        "1", "1*", "1*","1*", "1*", "1", "1", "1*"
    };
    headerList.add(objecttest);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 7, 1000
        )
    ).thenReturn(headerList);
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(
            fileImport, 0, 8, 0, 7, 1000
        )
    ).thenReturn(dataImportList);

    ResultInSideDto actual = mrConfigTestXaBusiness.importData(uploadFile);

    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void importData_05() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp";
    PowerMockito.when(
        FileUtils
            .saveTempFile(any(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objecttest = new Object[]{
        "1", "1*", "1*", "1*", "1*", "1", "1", "1*"
    };
    headerList.add(objecttest);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    Object[] objecttest1 = new Object[]{
        "1", "1", "1", "1", "1", "1", "1", "1"
    };
    dataImportList.add(objecttest1);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 7, 1000
        )
    ).thenReturn(headerList);
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(
            fileImport, 0, 8, 0, 7, 1000
        )
    ).thenReturn(dataImportList);

    List<ItemDataCRInside> dataCRInsideList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("TEST");
    dataCRInsideList.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(dataCRInsideList);
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), any(), anyString(), any())
    ).thenReturn(new File(filePath));

    ResultInSideDto actual = mrConfigTestXaBusiness.importData(uploadFile);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void importData_06() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp";
    PowerMockito.when(
        FileUtils
            .saveTempFile(any(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objecttest = new Object[]{
        "1", "1*", "1*", "1*", "1*", "1", "1", "1*"
    };
    headerList.add(objecttest);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    Object[] objecttest1 = new Object[]{
        "1", "1", "1", "1", "1", "1", "1", "1"
    };
    dataImportList.add(objecttest1);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 7, 1000
        )
    ).thenReturn(headerList);
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(
            fileImport, 0, 8, 0, 7, 1000
        )
    ).thenReturn(dataImportList);

    List<ItemDataCRInside> dataCRInsideList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("TEST");
    dataCRInsideList.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(dataCRInsideList);
    List<MrConfigTestXaDTO> lstStation = Mockito.spy(ArrayList.class);
    MrConfigTestXaDTO mrConfigTestXaDTO = Mockito.spy(MrConfigTestXaDTO.class);
    mrConfigTestXaDTO.setStation("1");
    mrConfigTestXaDTO.setCountry("VietNam");
    mrConfigTestXaDTO.setProvince("HaNoi");
    lstStation.add(mrConfigTestXaDTO);
    PowerMockito.when(
        mrConfigTestXaRepository
            .getListStation()
    ).thenReturn(lstStation);

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    List<MrConfigTestXaDTO> list = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mrConfigTestXaRepository
            .checkListDTOExisted(any())
    ).thenReturn(list);
    PowerMockito.when(
        mrConfigTestXaRepository.insertOrUpdate(any())
    ).thenReturn(result);


    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), any(), anyString(), any())
    ).thenReturn(new File(filePath));

    ResultInSideDto actual = mrConfigTestXaBusiness.importData(uploadFile);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }
}
