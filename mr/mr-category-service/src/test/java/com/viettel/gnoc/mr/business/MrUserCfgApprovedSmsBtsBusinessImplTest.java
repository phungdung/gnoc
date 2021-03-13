package com.viettel.gnoc.mr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrUserCfgApprovedSmsBtsDTO;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.mr.repository.MrUserCfgApprovedSmsBtsRepository;
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
import org.springframework.mock.web.MockMultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrUserCfgApprovedSmsBtsBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrUserCfgApprovedSmsBtsBusinessImplTest {

  @InjectMocks
  MrUserCfgApprovedSmsBtsBusinessImpl mrUserCfgApprovedSmsBtsBusiness;

  @Mock
  MrUserCfgApprovedSmsBtsRepository mrUserCfgApprovedSmsBtsRepository;

  @Mock
  protected CatLocationBusiness catLocationBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CatItemBusiness catItemBusiness;

  @Mock
  MrScheduleTelRepository mrScheduleTelRepository;

  @Mock
  MrDeviceRepository mrDeviceRepository;

  @Mock
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Mock
  UserRepository userRepository;


  @Test
  public void getListMrUserCfgApprovedSmsBts_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TRIPM_TEST");
    MrUserCfgApprovedSmsBtsDTO smsBtsDTO = Mockito.spy(MrUserCfgApprovedSmsBtsDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<MrUserCfgApprovedSmsBtsDTO> list = Mockito.spy(ArrayList.class);
    MrUserCfgApprovedSmsBtsDTO dto = Mockito.spy(MrUserCfgApprovedSmsBtsDTO.class);
    dto.setAreaCode("CT");
    list.add(dto);
    datatable.setData(list);

    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository
            .getListMrUserCfgApprovedSmsBts(any())
    ).thenReturn(datatable);

    Datatable actual = mrUserCfgApprovedSmsBtsBusiness.getListMrUserCfgApprovedSmsBts(smsBtsDTO);

    Assert.assertNotNull(actual);
  }

  @Test
  public void getDetail_01() {
    List<ItemDataCRInside> dataCRInsideList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("TEST");
    dataCRInsideList.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(dataCRInsideList);
    MrUserCfgApprovedSmsBtsDTO dto = Mockito.spy(MrUserCfgApprovedSmsBtsDTO.class);
    dto.setProvinceCode("HN");
    dto.setMarketCode("HN");
    List<CatLocationDTO> dataList = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationCode("HN");
    catLocationDTO.setLocationName("HaNoi");
    catLocationDTO.setLocationId("111");
    dataList.add(catLocationDTO);

    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository.getDetail(anyLong())
    ).thenReturn(dto);
    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository
            .getLstProvinceNamebyCode(anyString())
    ).thenReturn(dataList);
    PowerMockito.when(
        catLocationBusiness
            .getCatLocationByParentId(anyString())
    ).thenReturn(dataList);

    MrUserCfgApprovedSmsBtsDTO actual = mrUserCfgApprovedSmsBtsBusiness.getDetail(1L);

    Assert.assertNotNull(actual);
  }

  @Test
  public void insertOrUpdate_01() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TRIPM_TEST");
    MrUserCfgApprovedSmsBtsDTO smsBtsDTO = Mockito.spy(MrUserCfgApprovedSmsBtsDTO.class);
    smsBtsDTO.setUserName("thanhlv12");
    UserToken userToken = Mockito.spy(UserToken.class);
    List<MrUserCfgApprovedSmsBtsDTO> listCheckExisted = Mockito.spy(ArrayList.class);
    listCheckExisted.add(smsBtsDTO);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository
            .checkExisted(any())
    ).thenReturn(listCheckExisted);

    ResultInSideDto actual = mrUserCfgApprovedSmsBtsBusiness.insertOrUpdate(smsBtsDTO);

    Assert.assertEquals(RESULT.DUPLICATE, actual.getKey());
  }

  @Test
  public void insertOrUpdate_02() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TRIPM_TEST");
    MrUserCfgApprovedSmsBtsDTO smsBtsDTO = Mockito.spy(MrUserCfgApprovedSmsBtsDTO.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    List<MrUserCfgApprovedSmsBtsDTO> listCheckExisted = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository
            .checkExisted(any())
    ).thenReturn(listCheckExisted);
    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository.insertOrUpdate(any())
    ).thenReturn(resultInSideDto);

    ResultInSideDto actual = mrUserCfgApprovedSmsBtsBusiness.insertOrUpdate(smsBtsDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void insertOrUpdate_03() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TRIPM_TEST");
    MrUserCfgApprovedSmsBtsDTO smsBtsDTO = Mockito.spy(MrUserCfgApprovedSmsBtsDTO.class);
    smsBtsDTO.setUserCfgApprovedSmsId(1L);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    List<MrUserCfgApprovedSmsBtsDTO> listCheckExisted = Mockito.spy(ArrayList.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);

    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository
            .checkExisted(any())
    ).thenReturn(listCheckExisted);
    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository.insertOrUpdate(any())
    ).thenReturn(resultInSideDto);

    ResultInSideDto actual = mrUserCfgApprovedSmsBtsBusiness.insertOrUpdate(smsBtsDTO);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void deleteMrUserCfgApprovedSmsBts() {
  }

  @Test
  public void getUserByUserId() {
  }

  @Test
  public void getApproveLevelByUserLogin() {
  }

  @Test
  public void exportData_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TRIPM_TEST");
    MrUserCfgApprovedSmsBtsDTO smsBtsDTO = Mockito.spy(MrUserCfgApprovedSmsBtsDTO.class);
    smsBtsDTO.setAreaCode("CT");
    List<MrUserCfgApprovedSmsBtsDTO> listExportBySearch = Mockito.spy(ArrayList.class);
    listExportBySearch.add(smsBtsDTO);
    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository
            .onSearchExport(any())
    ).thenReturn(listExportBySearch);
    File actual = mrUserCfgApprovedSmsBtsBusiness.exportData(smsBtsDTO);

    Assert.assertNull(actual);
  }

  @Test
  public void importData_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TRIPM_TEST");
    List<ItemDataCRInside> dataCRInsideList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("TEST");
    dataCRInsideList.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(dataCRInsideList);
    ResultInSideDto actual = mrUserCfgApprovedSmsBtsBusiness.importData(null);
    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void importData_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TRIPM_TEST");
    List<ItemDataCRInside> dataCRInsideList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("TEST");
    dataCRInsideList.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(dataCRInsideList);
    MockMultipartFile multipartFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );

    List<MrUserCfgApprovedSmsBtsDTO> lstMarketAreaProvince = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository
            .getLstCountryAreaProvince()
    ).thenReturn(lstMarketAreaProvince);

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
            8,
            1000
        )
    ).thenReturn(headerList);

    ResultInSideDto actual = mrUserCfgApprovedSmsBtsBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void importData_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<ItemDataCRInside> dataCRInsideList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("TEST");
    dataCRInsideList.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(dataCRInsideList);
    MockMultipartFile multipartFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );

    List<MrUserCfgApprovedSmsBtsDTO> lstMarketAreaProvince = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository
            .getLstCountryAreaProvince()
    ).thenReturn(lstMarketAreaProvince);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils
            .saveTempFile(any(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objecttest = new Object[]{
        "1", "1*", "1*", "1", "1*", "1*", "1", "1", "1*"
    };
    headerList.add(objecttest);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1002; i++) {
      dataImportList.add(objecttest);
    }

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 8, 1000
        )
    ).thenReturn(headerList);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 8, 0, 8, 1000
        )
    ).thenReturn(dataImportList);

    ResultInSideDto actual = mrUserCfgApprovedSmsBtsBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void importData_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<ItemDataCRInside> dataCRInsideList = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("TEST");
    dataCRInsideList.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(dataCRInsideList);
    MockMultipartFile multipartFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );

    List<MrUserCfgApprovedSmsBtsDTO> lstMarketAreaProvince = Mockito.spy(ArrayList.class);
    MrUserCfgApprovedSmsBtsDTO mrUserCfgApprovedSmsBtsDTO = Mockito.spy(MrUserCfgApprovedSmsBtsDTO.class);
    mrUserCfgApprovedSmsBtsDTO.setMarketCode("1");
    mrUserCfgApprovedSmsBtsDTO.setAreaCode("CT");
    mrUserCfgApprovedSmsBtsDTO.setProvinceCode("1");
    lstMarketAreaProvince.add(mrUserCfgApprovedSmsBtsDTO);
    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository
            .getLstCountryAreaProvince()
    ).thenReturn(lstMarketAreaProvince);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils
            .saveTempFile(any(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objecttest = new Object[]{
        "1", "1*", "1*", "1", "1*", "1*", "1", "1", "1*"
    };
    headerList.add(objecttest);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    Object[] objecttest1 = new Object[]{
        "1", "1", "CT", "1", "1", "1", "1", "1", "1"
    };
    dataImportList.add(objecttest1);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 8, 1000
        )
    ).thenReturn(headerList);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 8, 0, 8, 1000
        )
    ).thenReturn(dataImportList);

    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), any(), anyString(), any())
    ).thenReturn(new File(filePath));

    ResultInSideDto actual = mrUserCfgApprovedSmsBtsBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void getFileTemplate_01() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("TRIPM_TEST");
    PowerMockito.when(I18n.getLanguage("mrUserCfgApprovedSmsBts.export.sheetname")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mrUserCfgApprovedSmsBts.export.sheetname1")).thenReturn("2");

    List<MrUserCfgApprovedSmsBtsDTO> lstANT = Mockito.spy(ArrayList.class);
    MrUserCfgApprovedSmsBtsDTO dto = Mockito.spy(MrUserCfgApprovedSmsBtsDTO.class);
    lstANT.add(dto);
    PowerMockito.when(
        mrUserCfgApprovedSmsBtsRepository
            .getLstCountryAreaProvince()
    ).thenReturn(lstANT);

    File actual = mrUserCfgApprovedSmsBtsBusiness.getFileTemplate();

    Assert.assertNotNull(actual);
  }
}
