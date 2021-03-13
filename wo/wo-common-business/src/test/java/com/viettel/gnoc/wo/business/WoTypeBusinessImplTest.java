
package com.viettel.gnoc.wo.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
import com.viettel.gnoc.wo.dto.WoTypeCheckListDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.repository.WoPriorityRepository;
import com.viettel.gnoc.wo.repository.WoTypeCfgRequiredRepository;
import com.viettel.gnoc.wo.repository.WoTypeCheckListRepository;
import com.viettel.gnoc.wo.repository.WoTypeRepository;
import com.viettel.gnoc.wo.utils.NocProPort;
import com.viettel.gnoc.wo.utils.WS_CHI_PHI_Port;
import com.viettel.gnoc.wo.utils.WS_SAP_Port;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WoTypeBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    NocProPort.class, TicketProvider.class, DataUtil.class, WS_SAP_Port.class, CommonExport.class,
    DateTimeUtils.class, JAXBContext.class, WS_CHI_PHI_Port.class, PassTranformer.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class WoTypeBusinessImplTest {

  @InjectMocks
  WoTypeBusinessImpl woTypeBusiness;
  @Mock
  WoTypeRepository woTypeRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  WoTypeCheckListRepository woTypeCheckListRepository;

  @Mock
  WoTypeCfgRequiredRepository woTypeCfgRequiredRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitRepository unitRepository;

  @Mock
  WoPriorityRepository woPriorityRepository;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Test
  public void getListWoTypeByLocalePage() {
  }

  @Test
  public void delete() {
    PowerMockito.when(woTypeRepository.delete(anyLong())).thenReturn(new ResultInSideDto(null,
        RESULT.SUCCESS, null));
    PowerMockito.when(woTypeRepository.findByWoTypeId(anyLong())).thenReturn(new WoTypeInsideDTO());
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto actual = woTypeBusiness.delete(1L);
    Assert.assertEquals(actual.getKey(), RESULT.SUCCESS);
  }

  @Test
  public void getListDataExport() {
  }

  @Test
  public void setMapWoGroupTypeName() {
  }

  @Test
  public void getWoTypeTemplate() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("a");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    dataGroupType.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataGroupType);
    File file = woTypeBusiness.getWoTypeTemplate();
    Assert.assertNotNull(file);
  }

  @Test
  public void exportData() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setIsEnable(1L);
    woTypeInsideDTO.setEnableCreate(1L);
    woTypeInsideDTO.setAllowPending(1L);
    woTypeInsideDTO.setCreateFromOtherSys(1L);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<WoTypeInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(woTypeInsideDTO);
    datatable.setData(list);
    PowerMockito.when(woTypeRepository.getListDataExport(any())).thenReturn(datatable);
    File file = woTypeBusiness.exportData(woTypeInsideDTO);
    Assert.assertNull(file);
  }

  @Test
  public void importData_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    ResultInSideDto result = woTypeBusiness.importData(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void importData_02() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importData_03() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1*", "1*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "NODATA");
  }

  @Test
  public void importData_04() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1*", "1*"};
    Object[] header1 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    for (int i = 0; i < 2000; i++) {
      headerList1.add(header1);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        8,
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");

  }

  @Test
  public void importData_05() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    Object[] header = new Object[]{"1", "1*", "1*", "1*", "1*", "1*", "1*", "1*", "1*"};
    Object[] header1 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    Object[] header2 = new Object[]{"1", "", "1", "1", "1", "1", "1", "1", "1"};
    Object[] header3 = new Object[]{"1", "1", "", "1", "1", "1", "1", "1", "1"};
    Object[] header4 = new Object[]{"1", "1", "1", "", "1", "1", "1", "1", "1"};
    Object[] header5 = new Object[]{"1", "1", "1", "1", "", "1", "1", "1", "1"};
    Object[] header6 = new Object[]{"1", "1", "1", "1", "1", "", "1", "1", "1"};
    Object[] header7 = new Object[]{"1", "1", "1", "1", "1", "1", "", "1", "1"};
    Object[] header8 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "", "1"};
    Object[] header9 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", ""};

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    headerList1.add(header1);
    headerList1.add(header2);
    headerList1.add(header3);
    headerList1.add(header4);
    headerList1.add(header5);
    headerList1.add(header6);
    headerList1.add(header7);
    headerList1.add(header8);
    headerList1.add(header9);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        4,
        0,
        8,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        8,
        1000
    )).thenReturn(headerList1);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    catItemDTO.setItemId(1L);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatable);
    ResultInSideDto result = woTypeBusiness.importData(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }


  @Test
  public void findByWoTypeId() {
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    PowerMockito.when(woTypeRepository.findByWoTypeId(anyLong())).thenReturn(woTypeInsideDTO);
    woTypeBusiness.findByWoTypeId(1L);
  }

  @Test
  public void insert() throws Exception {
    List<MultipartFile> filesGuideline = Mockito.spy(ArrayList.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoCloseAutomaticTime("1");
    WoTypeCfgRequiredDTO woTypeCfgRequiredDTO = Mockito.spy(WoTypeCfgRequiredDTO.class);
    List<WoTypeCfgRequiredDTO> woTypeCfgRequiredDTOList = Mockito.spy(ArrayList.class);
    woTypeCfgRequiredDTOList.add(woTypeCfgRequiredDTO);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setIndexFile(0L);
    List<GnocFileDto> list = Mockito.spy(ArrayList.class);
    list.add(gnocFileDto);
    WoTypeCheckListDTO woTypeCheckListDTO = Mockito.spy(WoTypeCheckListDTO.class);
    List<WoTypeCheckListDTO> listDTOList = Mockito.spy(ArrayList.class);
    listDTOList.add(woTypeCheckListDTO);
    woTypeInsideDTO.setWoTypeCheckListDTOList(listDTOList);
    woTypeInsideDTO.setGnocFileCreateWoDtos(list);
    woTypeInsideDTO.setWoTypeCfgRequiredDTOList(woTypeCfgRequiredDTOList);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(1L);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    resultFileDataOld.setId(1L);
    PowerMockito.when(woTypeRepository.addCfgFileCreateWo(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(woTypeRepository.add(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woTypeCfgRequiredRepository.add(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woTypeCheckListRepository.add(any())).thenReturn(resultInSideDto);

    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(
        FileUtils
            .saveFtpFile(anyString(), anyInt(), anyString(),
                anyString(), anyString(), anyString(),
                any(), any())
    ).thenReturn("abc");
    PowerMockito.when(
        FileUtils
            .saveUploadFile(anyString(), any(), anyString(), any())
    ).thenReturn("abc");
    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(uploadFile);

    PowerMockito.when(woTypeRepository.addWoTypeFilesGuide(any())).thenReturn(resultInSideDto);

    ResultInSideDto result = woTypeBusiness.insert(files, files, woTypeInsideDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void update() throws Exception {
    List<MultipartFile> filesGuideline = Mockito.spy(ArrayList.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    woTypeInsideDTO.setWoCloseAutomaticTime("1");
    List<Long> longList = Mockito.spy(ArrayList.class);
    longList.add(1L);
    WoTypeCfgRequiredDTO woTypeCfgRequiredDTO = Mockito.spy(WoTypeCfgRequiredDTO.class);
    List<WoTypeCfgRequiredDTO> woTypeCfgRequiredDTOList = Mockito.spy(ArrayList.class);
    woTypeCfgRequiredDTOList.add(woTypeCfgRequiredDTO);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setIndexFile(0L);
    List<GnocFileDto> list = Mockito.spy(ArrayList.class);
    list.add(gnocFileDto);
    woTypeInsideDTO.setGnocFilesGuideDtos(list);
    WoTypeCheckListDTO woTypeCheckListDTO = Mockito.spy(WoTypeCheckListDTO.class);
    List<WoTypeCheckListDTO> listDTOList = Mockito.spy(ArrayList.class);
    listDTOList.add(woTypeCheckListDTO);
    woTypeInsideDTO.setWoTypeCheckListDTOList(listDTOList);
    woTypeInsideDTO.setGnocFileCreateWoDtos(list);
    woTypeInsideDTO.setWoTypeCfgRequiredDTOList(woTypeCfgRequiredDTOList);
    woTypeInsideDTO.setIdDeleteListFilesGuide(longList);
    woTypeInsideDTO.setIdDeleteListFileCreateWo(longList);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setDeptId(1L);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    resultFileDataOld.setId(1L);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey("SUCCESS");
    PowerMockito.when(commonStreamServiceProxy.insertWoTestServiceMap(any())).thenReturn(resultDTO);
    PowerMockito.when(woTypeRepository.addCfgFileCreateWo(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(woTypeRepository.add(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woTypeCfgRequiredRepository.add(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woTypeCheckListRepository.add(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woTypeRepository.findByWoTypeId(anyLong())).thenReturn(new WoTypeInsideDTO());

    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(PassTranformer.decrypt("723298837f9c6fe9953c6e07e0e4df17"))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");
    PowerMockito.when(
        FileUtils
            .saveFtpFile(anyString(), anyInt(), anyString(),
                anyString(), anyString(), anyString(),
                any(), any())
    ).thenReturn("abc");
    PowerMockito.when(
        FileUtils
            .saveUploadFile(anyString(), any(), anyString(), any())
    ).thenReturn("abc");
    MockMultipartFile uploadFile = new MockMultipartFile(
        "data",
        "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(uploadFile);
    PowerMockito.when(woTypeRepository.addWoTypeFilesGuide(any())).thenReturn(resultInSideDto);

    ResultInSideDto result = woTypeBusiness.update(files, files, woTypeInsideDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void findWoTypeById() {
  }

  @Test
  public void getListWoTypeTimeDTO() {
  }

  @Test
  public void getListWoTypeIsEnable() {
  }

  @Test
  public void getListWoTypeIsEnableDataTable() {
  }

  @Test
  public void getListWoTypeByLocaleNotLike() {
  }

  @Test
  public void getListWoTypeByWoGroup() {
  }

  @Test
  public void getListWoTypeByLocale() {
  }

  @Test
  public void getWoTypeByCode() {
  }

  @Test
  public void getLanguageExchangeWoType() {
  }

  @Test
  public void getListWoTypeFilesGuideDTO() {
  }
}

