package com.viettel.gnoc.sr.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.sr.repository.SRCatalogRepository;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SRMappingProcessCRRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
@PrepareForTest({SRCatalogBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, I18n.class, CommonExport.class, WorkbookFactory.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})

public class SRMappingProcessCRBusinessImplTest {

  @Mock
  SRCatalogBusiness srCatalogBusiness;

  @Mock
  SRMappingProcessCRRepository srMappingProcessCRRepository;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  SRCatalogRepository srCatalogRepository;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  UnitBusiness unitBusiness;

  @Mock
  SRConfigRepository srConfigRepository;

  @InjectMocks
  SRMappingProcessCRBusinessImpl srMappingProcessCRBusiness;

  @Mock
  UserRepository userRepository;

  @Mock
  CrCategoryServiceProxy crCategoryServiceProxy;

  @Test
  public void setMapService() {
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("2");
    srCatalogDTO.setServiceName("ac");
    List<SRCatalogDTO> lstServices = Mockito.spy(ArrayList.class);
    lstServices.add(srCatalogDTO);
    when(srCatalogBusiness.getListServiceNameToMapping()).thenReturn(lstServices);
    srMappingProcessCRBusiness.setMapService();
  }

  @Test
  public void setMapProcess() {
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessCode("2");
    srMappingProcessCRDTO.setCrProcessName("2");
    List<SRMappingProcessCRDTO> lstProcessAll = Mockito.spy(ArrayList.class);
    lstProcessAll.add(srMappingProcessCRDTO);
    when(srMappingProcessCRRepository.getListAllProcess()).thenReturn(lstProcessAll);
    srMappingProcessCRBusiness.setMapProcess();

  }

  @Test
  public void setMapWo() {
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessCode("2");
    srMappingProcessCRDTO.setCrProcessName("2");
    List<SRMappingProcessCRDTO> lstWoAll = Mockito.spy(ArrayList.class);
    lstWoAll.add(srMappingProcessCRDTO);
    when(srMappingProcessCRRepository.getListAllWo()).thenReturn(lstWoAll);
    srMappingProcessCRBusiness.setMapWo();
  }

  @Test
  public void getListMappingProcessCR() {
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    List<SRMappingProcessCRDTO> listData = Mockito.spy(ArrayList.class);
    listData.add(srMappingProcessCRDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listData);
    when(srMappingProcessCRRepository.getListMappingProcessCR(any())).thenReturn(datatable);

    SRMappingProcessCRDTO srMappingProcessCRDTODel = Mockito.spy(SRMappingProcessCRDTO.class);
    List<SRMappingProcessCRDTO> listDel = Mockito.spy(ArrayList.class);
    listDel.add(srMappingProcessCRDTODel);
    when(srMappingProcessCRRepository.checkDeleteSRMappingProcess(any())).thenReturn(listDel);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    srMappingProcessCRDTO.setUserNameToken("thanhlv12");
    srMappingProcessCRBusiness.getListMappingProcessCR(srMappingProcessCRDTO);
  }

  @Test
  public void insertOrUpdate() {
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setWo("2");
    srMappingProcessCRDTO.setProcess("2");
    srMappingProcessCRDTO.setWoTestStartTime(new Date());
    srMappingProcessCRDTO.setWoTestEndTime(new Date());
    srMappingProcessCRDTO.setWoFtEndTime(new Date());
    srMappingProcessCRDTO.setWoFtStartTime(new Date());
    srMappingProcessCRDTO.setAutoCreateCR(1L);
    srMappingProcessCRDTO.setCrTitle("1");
    srMappingProcessCRDTO.setDutyType(1L);
    srMappingProcessCRDTO.setCrStatus(1L);
    srMappingProcessCRDTO.setServiceAffecting(1L);
    srMappingProcessCRDTO.setAffectingService("1");
    srMappingProcessCRDTO.setTotalAffectingCustomers(1L);
    srMappingProcessCRDTO.setTotalAffectingMinutes(1L);
    srMappingProcessCRDTO.setWoFtTypeId(1L);
    srMappingProcessCRDTO.setWoContentService("1");
    srMappingProcessCRDTO.setWoContentCDFT("1");
    srMappingProcessCRDTO.setWoFtDescription("1");
    srMappingProcessCRDTO.setGroupCDFT(1L);
    srMappingProcessCRDTO.setWoFtPriority(1L);
    srMappingProcessCRDTO.setWoTestTypeId(1L);
    srMappingProcessCRDTO.setWoTestDescription("1");
    srMappingProcessCRDTO.setGroupCdFtService(1L);
    srMappingProcessCRDTO.setWoTestPriority(1L);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(userRepository.getOffsetFromUser(anyString())).thenReturn(1D);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);

    PowerMockito.when(srMappingProcessCRRepository.insertOrUpdate(any())).thenReturn(resultInSideDto);
    srMappingProcessCRBusiness.insertOrUpdate(srMappingProcessCRDTO);
  }

  @Test
  public void getSRMappingProcessCRDetail() {
    Long id = 3L;
    SRMappingProcessCRDTO srMappingProcess = Mockito.spy(SRMappingProcessCRDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    srMappingProcess.setUserNameToken("thanhlv12");
    srMappingProcess.setId(id);
    SRMappingProcessCRDTO srMappingProcessCRDTO = srMappingProcessCRRepository
        .getSRMappingProcessCRDetail(srMappingProcess);
    SRMappingProcessCRDTO result = srMappingProcessCRBusiness.getSRMappingProcessCRDetail(id);
    assertEquals(srMappingProcessCRDTO, result);
  }

  @Test
  public void deleteSRMappingProcessCR() {
    Long id = 3L;
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(srMappingProcessCRRepository.deleteSRMappingProcessCR(any())).thenReturn(resultInSideDto);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    srMappingProcessCRBusiness.deleteSRMappingProcessCR(id);
  }

  @Test
  public void getListSearchDTO() {
    srMappingProcessCRBusiness.getListSearchDTO();
  }

  @Test
  public void getListWoById() {
    Long crProccessId = 2L;
    List<SRMappingProcessCRDTO> lst = Mockito.spy(ArrayList.class);
    when(srMappingProcessCRRepository.getListWo(crProccessId)).thenReturn(lst);
    List<SRMappingProcessCRDTO> result = srMappingProcessCRBusiness.getListWoById(crProccessId);
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void getListSRMappingProcessCRDTO() {
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    List<SRMappingProcessCRDTO> list = Mockito.spy(ArrayList.class);
    when(srMappingProcessCRRepository.getListSRMappingProcessCRDTO(any())).thenReturn(list);
    List<SRMappingProcessCRDTO> result = srMappingProcessCRBusiness
        .getListSRMappingProcessCRDTO(srMappingProcessCRDTO);
    assertEquals(list.size(), result.size());
  }

  @Test
  public void getStartTimeEndTimeFromCrImpact() {
    SRCreateAutoCRDTO dto = Mockito.spy(SRCreateAutoCRDTO.class);
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    when(srMappingProcessCRRepository.getStartTimeEndTimeFromCrImpact(any()))
        .thenReturn(srMappingProcessCRDTO);
    SRMappingProcessCRDTO result = srMappingProcessCRBusiness.getStartTimeEndTimeFromCrImpact(dto);
    assertEquals(srMappingProcessCRDTO, result);
  }

  @Test
  public void test_getTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.title.import.list")).thenReturn("0");
    PowerMockito.when(I18n.getLanguage("srCatalog.serviceCode")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.processWo")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.unitImplementName")).thenReturn("4");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.affectingService")).thenReturn("5");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.groupCDFT.title")).thenReturn("6");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.groupCdFtService.title"))
        .thenReturn("7");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.dutyTypeStr")).thenReturn("8");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.woPriority")).thenReturn("9");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.testServiceStr")).thenReturn("10");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.coordinationFTStr")).thenReturn("11");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.woFtTypeIdStr")).thenReturn("11");

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setServiceName("1");
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> list = Mockito.spy(ArrayList.class);
    list.add(srCatalogDTO);
    PowerMockito.when(
        srCatalogBusiness.getListServiceNameToMapping()
    ).thenReturn(list);
    PowerMockito.when(
        srCatalogRepository.getListCountryService()
    ).thenReturn(list);

    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentCode("1");
    srMappingProcessCRDTO.setProcess("1");
    srMappingProcessCRDTO.setCrProcessCode("1");
    srMappingProcessCRDTO.setWo("1");
    List<SRMappingProcessCRDTO> lstServices = Mockito.spy(ArrayList.class);
    lstServices.add(srMappingProcessCRDTO);
    PowerMockito.when(
        srMappingProcessCRRepository.getListParentChil()
    ).thenReturn(lstServices);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstAffectedService = Mockito.spy(ArrayList.class);
    lstAffectedService.add(itemDataCRInside);
    PowerMockito.when(
        crServiceProxy.getListAffectedServiceCBProxy(any())
    ).thenReturn(lstAffectedService);
    PowerMockito.when(
        crServiceProxy.getListDutyTypeCBB(any())
    ).thenReturn(lstAffectedService);
    PowerMockito.when(
        commonStreamServiceProxy
            .getListLocationByLevelCBBProxy(anyLong(), anyLong())
    ).thenReturn(lstAffectedService);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstWoCdGroup = Mockito.spy(ArrayList.class);
    lstWoCdGroup.add(woCdGroupInsideDTO);
    PowerMockito.when(
        woCategoryServiceProxy.getListCdGroupByUser(any())
    ).thenReturn(lstWoCdGroup);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> unitDTOS = Mockito.spy(ArrayList.class);
    unitDTOS.add(unitDTO);
    PowerMockito.when(
        unitBusiness.getListUnit(any())
    ).thenReturn(unitDTOS);

    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setPriorityId(1L);
    woInsideDTO.setPriorityName("1");
    List<WoInsideDTO> lstPriority = Mockito.spy(ArrayList.class);
    lstPriority.add(woInsideDTO);
    PowerMockito.when(
        srMappingProcessCRRepository.getLstPriority()
    ).thenReturn(lstPriority);

    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    woTypeInsideDTO.setWoTypeName("1");
    List<WoTypeInsideDTO> lstWoFT = Mockito.spy(ArrayList.class);
    lstWoFT.add(woTypeInsideDTO);
    PowerMockito.when(
        woCategoryServiceProxy.getListWoTypeDTO(any())
    ).thenReturn(lstWoFT);
    PowerMockito.when(
        woCategoryServiceProxy.getWoTypeByCode(anyString())
    ).thenReturn(woTypeInsideDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> listTestService = Mockito.spy(ArrayList.class);
    listTestService.add(srConfigDTO);
    PowerMockito.when(
        srConfigRepository
            .getByConfigGroup(any())
    ).thenReturn(listTestService);

    File file = srMappingProcessCRBusiness.getTemplate();
    assertNotNull(file);
  }

  @Test
  public void testImportData_01() throws Exception {
    ResultInSideDto actual = srMappingProcessCRBusiness
        .importData(null);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportData_02() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    ResultInSideDto actual = srMappingProcessCRBusiness
        .importData(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportData_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objects = new Object[]{
        "1", "1 (*)", "1 (*)", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1"
    };
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 23, 2)
    ).thenReturn(headerList);

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(fileImp, 0, 8, 0, 23, 1000)
    ).thenReturn(lstData);

    ResultInSideDto actual = srMappingProcessCRBusiness
        .importData(multipartFile);

    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void testImportData_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objects = new Object[]{
        "1", "1 (*)", "1 (*)", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1"
    };
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 23, 2)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1"
    };
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1502; i++) {
      lstData.add(objects1);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(fileImp, 0, 8, 0, 23, 1000)
    ).thenReturn(lstData);

    ResultInSideDto actual = srMappingProcessCRBusiness
        .importData(multipartFile);

    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportData_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "abc/temp.xls";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objects = new Object[]{
        "1", "1 (*)", "1 (*)", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1"
    };
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 7, 0, 23, 2)
    ).thenReturn(headerList);

    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    Object[] objects1 = new Object[]{
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1",
        "1", "1", "1"
    };
    lstData.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFileNew(fileImp, 0, 8, 0, 23, 1000)
    ).thenReturn(lstData);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstAffectedService = Mockito.spy(ArrayList.class);
    lstAffectedService.add(itemDataCRInside);
    PowerMockito.when(
        crServiceProxy.getListAffectedServiceCBProxy(any())
    ).thenReturn(lstAffectedService);
    PowerMockito.when(
        crServiceProxy.getListDutyTypeCBB(any())
    ).thenReturn(lstAffectedService);
    PowerMockito.when(
        commonStreamServiceProxy
            .getListLocationByLevelCBBProxy(anyLong(), anyLong())
    ).thenReturn(lstAffectedService);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(1L);
    woCdGroupInsideDTO.setWoGroupName("1");
    List<WoCdGroupInsideDTO> lstWoCdGroup = Mockito.spy(ArrayList.class);
    lstWoCdGroup.add(woCdGroupInsideDTO);
    PowerMockito.when(
        woCategoryServiceProxy.getListCdGroupByUser(any())
    ).thenReturn(lstWoCdGroup);

    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessCode("1");
    srMappingProcessCRDTO.setCrProcessName("1");
    srMappingProcessCRDTO.setCrProcessId(1L);
    List<SRMappingProcessCRDTO> lstWoAll = Mockito.spy(ArrayList.class);
    lstWoAll.add(srMappingProcessCRDTO);
    PowerMockito.when(
        srMappingProcessCRRepository.getListAllWo()
    ).thenReturn(lstWoAll);
    PowerMockito.when(
        srMappingProcessCRRepository.getListAllProcess()
    ).thenReturn(lstWoAll);
    PowerMockito.when(
        srMappingProcessCRRepository
            .getCrProcessIdOrWoId(anyString())
    ).thenReturn(lstWoAll);
    PowerMockito.when(
        srMappingProcessCRRepository
            .getListWo(anyLong())
    ).thenReturn(lstWoAll);
    PowerMockito.when(
        srMappingProcessCRRepository
            .getListAllMappingProcessCR(any())
    ).thenReturn(lstWoAll);
    PowerMockito.when(
        srMappingProcessCRRepository
            .getCrProcessIdOrWoId(anyString())
    ).thenReturn(lstWoAll);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> lstServices = Mockito.spy(ArrayList.class);
    lstServices.add(srCatalogDTO);
    PowerMockito.when(
        srCatalogBusiness.getListServiceNameToMapping()
    ).thenReturn(lstServices);
    PowerMockito.when(
        srCatalogRepository.getListCountryService()
    ).thenReturn(lstServices);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitName("1");
    List<UnitDTO> unitDTOS = Mockito.spy(ArrayList.class);
    unitDTOS.add(unitDTO);
    PowerMockito.when(
        unitBusiness.getListUnit(any())
    ).thenReturn(unitDTOS);

    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setPriorityId(1L);
    woInsideDTO.setPriorityName("1");
    List<WoInsideDTO> listPriority = Mockito.spy(ArrayList.class);
    listPriority.add(woInsideDTO);
    PowerMockito.when(
        srMappingProcessCRRepository.getLstPriority()
    ).thenReturn(listPriority);

    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    woTypeInsideDTO.setWoTypeName("1");
    List<WoTypeInsideDTO> listWoTypeDTO = Mockito.spy(ArrayList.class);
    listWoTypeDTO.add(woTypeInsideDTO);
    PowerMockito.when(
        woCategoryServiceProxy.getListWoTypeDTO(any())
    ).thenReturn(listWoTypeDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> listTestService = Mockito.spy(ArrayList.class);
    listTestService.add(srConfigDTO);
    PowerMockito.when(
        srConfigRepository
            .getByConfigGroup(any())
    ).thenReturn(listTestService);
    PowerMockito.when(
        woCategoryServiceProxy
            .getWoTypeByCode(anyString())
    ).thenReturn(woTypeInsideDTO);

    CrProcessInsideDTO crProcessInsideDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessInsideDTO.setCrTypeId(1L);
    List<CrProcessInsideDTO> listCrProcess = Mockito.spy(ArrayList.class);
    listCrProcess.add(crProcessInsideDTO);
    PowerMockito.when(
        crCategoryServiceProxy
            .getListCrProcessDTO(any())
    ).thenReturn(listCrProcess);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        srMappingProcessCRRepository.insertOrUpdate(any())
    ).thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("1");
    PowerMockito.when(
        TicketProvider.getUserToken()
    ).thenReturn(userToken);

    ResultInSideDto actual = srMappingProcessCRBusiness
        .importData(multipartFile);

    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void test_createComboMapping() {
    SXSSFWorkbook workbook = Mockito.spy(SXSSFWorkbook.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.processWo")).thenReturn("f");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.processCode")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.processName")).thenReturn("c");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.woCode")).thenReturn("d");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.woName")).thenReturn("e");

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceName("1");
    srCatalogDTO.setServiceCode("1");
    List<SRCatalogDTO> lstServices = Mockito.spy(ArrayList.class);
    lstServices.add(srCatalogDTO);
    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setCrProcessParentCode("1");
    srMappingProcessCRDTO.setProcess("1");
    srMappingProcessCRDTO.setCrProcessCode("1");
    srMappingProcessCRDTO.setWo("1");
    List<SRMappingProcessCRDTO> lstServices1 = Mockito.spy(ArrayList.class);
    lstServices1.add(srMappingProcessCRDTO);
    PowerMockito.when(srCatalogBusiness.getListServiceNameToMapping()).thenReturn(lstServices);
    PowerMockito.when(srMappingProcessCRRepository.getListParentChil()).thenReturn(lstServices1);
    srMappingProcessCRBusiness.createComboMapping(workbook);
  }

  @Test
  public void testExportSRMappingProcessCR() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_Vn");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("UNIT-TEST");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.service")).thenReturn("0");
    PowerMockito.when(I18n.getLanguage("SrMappingProcessCr.processWo")).thenReturn("1");

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    SRMappingProcessCRDTO srMappingProcessCRDTO = Mockito.spy(SRMappingProcessCRDTO.class);
    srMappingProcessCRDTO.setAutoCreateCR(1L);
    srMappingProcessCRDTO.setServiceAffecting(1L);
    srMappingProcessCRDTO.setWoTestTypeId(1L);
    srMappingProcessCRDTO.setWoFtTypeId(1L);
    srMappingProcessCRDTO.setDutyType(1L);
    srMappingProcessCRDTO.setGroupCdFtService(1L);
    srMappingProcessCRDTO.setGroupCDFT(1L);
    srMappingProcessCRDTO.setCrProcessParentCode("1");
    srMappingProcessCRDTO.setProcess("1");
    srMappingProcessCRDTO.setCrProcessCode("1");
    srMappingProcessCRDTO.setWo("1");

    List<SRMappingProcessCRDTO> list = Mockito.spy(ArrayList.class);
    list.add(srMappingProcessCRDTO);
    PowerMockito.when(
        srMappingProcessCRRepository
            .exportSRMappingProcessCR(any())
    ).thenReturn(list);
    PowerMockito.when(
        srMappingProcessCRRepository.getListParentChil()
    ).thenReturn(list);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> listDutyTypeCBB = Mockito.spy(ArrayList.class);
    listDutyTypeCBB.add(itemDataCRInside);
    PowerMockito.when(
        crServiceProxy.getListDutyTypeCBB(any())
    ).thenReturn(listDutyTypeCBB);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setServiceName("1");
    List<SRCatalogDTO> lstServices1 = Mockito.spy(ArrayList.class);
    lstServices1.add(srCatalogDTO);
    PowerMockito.when(
        srCatalogBusiness.getListServiceNameToMapping()
    ).thenReturn(lstServices1);

    File actual = srMappingProcessCRBusiness.exportSRMappingProcessCR(srMappingProcessCRDTO);

    Assert.assertNotNull(actual);
  }
}
