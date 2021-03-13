package com.viettel.gnoc.wo.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.ReceiveUnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import com.viettel.gnoc.wo.dto.WoTypeGroupDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.repository.WoCdGroupRepository;
import com.viettel.gnoc.wo.repository.WoCdGroupUnitRepository;
import com.viettel.gnoc.wo.repository.WoCdRepository;
import com.viettel.gnoc.wo.repository.WoTypeGroupRepository;
import com.viettel.gnoc.wo.repository.WoTypeRepository;
import com.viettel.gnoc.wo.utils.NocProPort;
import com.viettel.gnoc.wo.utils.WSNIMS_HTPort;
import com.viettel.gnoc.wo.utils.WSNIMS_HT_GLOBAL_Port;
import com.viettel.gnoc.wo.utils.WS_CHI_PHI_Port;
import com.viettel.gnoc.wo.utils.WS_SAP_Port;
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
@PrepareForTest({WoCdGroupBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    NocProPort.class, TicketProvider.class, DataUtil.class, WS_SAP_Port.class, CommonExport.class,
    DateTimeUtils.class, JAXBContext.class, WS_CHI_PHI_Port.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class WoCdGroupBusinessImplTest {

  @InjectMocks
  WoCdGroupBusinessImpl woCdGroupBusiness;
  @Mock
  WoCdGroupRepository woCdGroupRepository;

  @Mock
  WoCdGroupUnitRepository woCdGroupUnitRepository;

  @Mock
  ReceiveUnitRepository receiveUnitRepository;

  @Mock
  WoCdRepository woCdRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  WoTypeGroupRepository woTypeGroupRepository;

  @Mock
  WoTypeRepository woTypeRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  WSNIMS_HTPort wsnimsHtPort;

  @Mock
  WSNIMS_HT_GLOBAL_Port wsnimsHtGlobalPort;

  @Test
  public void getListWoCdGroupDTO() {
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoTypeId(1L);
    WoTypeGroupDTO woTypeGroupDTO = Mockito.spy(WoTypeGroupDTO.class);
    List<WoTypeGroupDTO> listTypeGroup = Mockito.spy(ArrayList.class);
    listTypeGroup.add(woTypeGroupDTO);
    PowerMockito.when(woTypeGroupRepository.getListWoTypeGroupDTO(any())).thenReturn(listTypeGroup);
    woCdGroupBusiness.getListWoCdGroupDTO(woCdGroupInsideDTO);
  }

  @Test
  public void findWoCdGroupById() {
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupBusiness.findWoCdGroupById(1L);
  }

  @Test
  public void insertWoCdGroup() {
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupCode("1");
    woCdGroupBusiness.insertWoCdGroup(woCdGroupInsideDTO);

  }

  @Test
  public void updateWoCdGroup() {
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupCode("1");
    woCdGroupBusiness.updateWoCdGroup(woCdGroupInsideDTO);
  }

  @Test
  public void deleteWoCdGroup() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(woCdGroupRepository.deleteWoCdGroup(anyLong())).thenReturn(resultInSideDto);
    PowerMockito.when(woTypeGroupRepository.deleteWoTypeGroupByWoGroupId(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = woCdGroupBusiness.deleteWoCdGroup(1L);
    Assert.assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void getListWoCdGroupUnitDTO() {
    WoCdGroupUnitDTO woCdGroupUnitDTO = Mockito.spy(WoCdGroupUnitDTO.class);
    List<WoCdGroupUnitDTO> listWoCdGroupUnit = Mockito.spy(ArrayList.class);
    listWoCdGroupUnit.add(woCdGroupUnitDTO);
    PowerMockito.when(woCdGroupUnitRepository.getListWoCdGroupUnitDTO(any()))
        .thenReturn(listWoCdGroupUnit);
    woCdGroupBusiness.getListWoCdGroupUnitDTO(woCdGroupUnitDTO);
  }

  @Test
  public void updateWoCdGroupUnit() {
    WoCdGroupUnitDTO woCdGroupUnitDTO = Mockito.spy(WoCdGroupUnitDTO.class);
    woCdGroupUnitDTO.setCdGroupUnitId(1L);
    List<Long> longList = Mockito.spy(ArrayList.class);
    longList.add(1L);
    woCdGroupUnitDTO.setListUnitIdDel(longList);
    woCdGroupUnitDTO.setListUnitIdInsert(longList);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    List<WoCdGroupUnitDTO> listWoCdGroupUnit = Mockito.spy(ArrayList.class);
    listWoCdGroupUnit.add(woCdGroupUnitDTO);
    PowerMockito.when(woCdGroupUnitRepository.deleteWoCdGroupUnit(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woCdGroupUnitRepository.getListWoCdGroupUnitDTO(any()))
        .thenReturn(listWoCdGroupUnit);
    PowerMockito.when(woCdGroupUnitRepository.updateWoCdGroupUnit(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woCdGroupUnitRepository.insertWoCdGroupUnit(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = woCdGroupBusiness.updateWoCdGroupUnit(woCdGroupUnitDTO);
    Assert.assertEquals(result.getKey(), "SUCCESS");
  }


  @Test
  public void getListWoCdDTO() {
    WoCdDTO woCdDTO = Mockito.spy(WoCdDTO.class);
    woCdDTO.setUserId(1L);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    List<UsersInsideDto> usersInsideDtoList = Mockito.spy(ArrayList.class);
    usersInsideDtoList.add(usersInsideDto);
    List<WoCdDTO> listWoCd = Mockito.spy(ArrayList.class);
    listWoCd.add(woCdDTO);
    PowerMockito.when(woCdRepository.getListWoCdDTO(any())).thenReturn(listWoCd);
    woCdGroupBusiness.getListWoCdDTO(woCdDTO);
  }

  @Test
  public void updateWoCd() {
    List<Long> longList = Mockito.spy(ArrayList.class);
    longList.add(1L);
    WoCdDTO woCdDTO = Mockito.spy(WoCdDTO.class);
    woCdDTO.setCdId(1L);
    woCdDTO.setListUserIdInsert(longList);
    woCdDTO.setListUserIdDel(longList);
    List<WoCdDTO> listWoCd = Mockito.spy(ArrayList.class);
    listWoCd.add(woCdDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(woCdRepository.deleteWoCd(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woCdRepository.getListWoCdDTO(any())).thenReturn(listWoCd);
    PowerMockito.when(woCdRepository.insertWoCd(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = woCdGroupBusiness.updateWoCd(woCdDTO);
    Assert.assertEquals(result.getKey(), "SUCCESS");
  }


  @Test
  public void getListWoTypeGroupDTO() {
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    List<WoTypeInsideDTO> woTypeInsideDTOList = Mockito.spy(ArrayList.class);
    woTypeInsideDTOList.add(woTypeInsideDTO);
    WoTypeGroupDTO woTypeGroupDTO = Mockito.spy(WoTypeGroupDTO.class);
    woTypeGroupDTO.setWoTypeId(1L);
    List<WoTypeGroupDTO> listWoTypeGroup = Mockito.spy(ArrayList.class);
    listWoTypeGroup.add(woTypeGroupDTO);
    PowerMockito.when(woTypeGroupRepository.getListWoTypeGroupDTO(any()))
        .thenReturn(listWoTypeGroup);
    woCdGroupBusiness.getListWoTypeGroupDTO(woTypeGroupDTO);
  }

  @Test
  public void updateWoTypeGroup() {
    List<Long> longList = Mockito.spy(ArrayList.class);
    longList.add(1L);
    WoCdDTO woCdDTO = Mockito.spy(WoCdDTO.class);
    woCdDTO.setCdId(1L);
    woCdDTO.setListUserIdInsert(longList);
    woCdDTO.setListUserIdDel(longList);
    WoTypeGroupDTO woTypeGroupDTO = Mockito.spy(WoTypeGroupDTO.class);
    woTypeGroupDTO.setWoTypeGroupId(1L);
    woTypeGroupDTO.setListWoTypeIdInsert(longList);
    List<WoCdDTO> listWoCd = Mockito.spy(ArrayList.class);
    listWoCd.add(woCdDTO);
    List<WoTypeGroupDTO> listWoTypeGroup = Mockito.spy(ArrayList.class);
    listWoTypeGroup.add(woTypeGroupDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(woTypeGroupRepository.insertWoTypeGroup(any())).thenReturn(resultInSideDto);
    PowerMockito.when(woTypeGroupRepository.deleteWoTypeGroup(anyLong()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(woTypeGroupRepository.getListWoTypeGroupDTO(any()))
        .thenReturn(listWoTypeGroup);
    ResultInSideDto result = woCdGroupBusiness.updateWoTypeGroup(woTypeGroupDTO);
    Assert.assertEquals(result.getKey(), "SUCCESS");
  }


  @Test
  public void exportData() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    List<WoCdGroupInsideDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    PowerMockito.when(woCdGroupRepository.getListWoCdGroupExport(any())).thenReturn(list);
    File file = woCdGroupBusiness.exportData(woCdGroupInsideDTO);
    Assert.assertNull(file);
  }

  @Test
  public void getTemplateImport() throws Exception {
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
    File file = woCdGroupBusiness.getTemplateImport();
    Assert.assertNotNull(file);
  }

  @Test
  public void getTemplateAssignUser() throws Exception {
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
    File file = woCdGroupBusiness.getTemplateAssignUser();
    Assert.assertNotNull(file);
  }

  @Test
  public void getTemplateAssignTypeGroup() throws Exception {
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
    File file = woCdGroupBusiness.getTemplateAssignTypeGroup();
    Assert.assertNotNull(file);
  }

  @Test
  public void importDataAssignUser_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    ResultInSideDto result = woCdGroupBusiness.importDataAssignUser(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");

  }

  @Test
  public void importDataAssignUser_02() throws Exception {
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

    ResultInSideDto result = woCdGroupBusiness.importDataAssignUser(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");

  }

  @Test
  public void importDataAssignUser_03() throws Exception {
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
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        3,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignUser(firstFile);
    assertEquals(result.getKey(), "NODATA");


  }

  @Test
  public void importDataAssignUser_04() throws Exception {
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
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)"};
    Object[] header1 = new Object[]{"1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    for (int i = 0; i < 600; i++) {
      headerList1.add(header1);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        3,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        6,
        0,
        3,
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignUser(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");
  }

  @Test
  public void importDataAssignUser_05() throws Exception {
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
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)"};
    Object[] header1 = new Object[]{"1", "1", "1", "1"};
    Object[] header2 = new Object[]{"1", "", "1", "1"};
    Object[] header3 = new Object[]{"1", "1", "", "1"};
    Object[] header4 = new Object[]{"1", "1", "1", ""};

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    headerList1.add(header1);
    headerList1.add(header2);
    headerList1.add(header3);
    headerList1.add(header4);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        3,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        6,
        0,
        3,
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignUser(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void importDataAssignTypeGroup_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    ResultInSideDto result = woCdGroupBusiness.importDataAssignTypeGroup(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void importDataAssignTypeGroup_02() throws Exception {
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
    ResultInSideDto result = woCdGroupBusiness.importDataAssignTypeGroup(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importDataAssignTypeGroup_03() throws Exception {
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
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        3,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignTypeGroup(firstFile);
    assertEquals(result.getKey(), "NODATA");
  }

  @Test
  public void importDataAssignTypeGroup_04() throws Exception {
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
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)"};
    Object[] header1 = new Object[]{"1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    for (int i = 0; i < 600; i++) {
      headerList1.add(header1);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        3,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        6,
        0,
        3,
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignTypeGroup(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");

  }

  @Test
  public void importDataAssignTypeGroup_05() throws Exception {
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
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)"};
    Object[] header1 = new Object[]{"1", "1", "1", "1"};
    Object[] header2 = new Object[]{"1", "", "1", "1"};
    Object[] header3 = new Object[]{"1", "1", "", "1"};
    Object[] header4 = new Object[]{"1", "1", "1", ""};

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    headerList1.add(header1);
    headerList1.add(header2);
    headerList1.add(header3);
    headerList1.add(header4);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        3,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        6,
        0,
        3,
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woCdGroupBusiness.importDataAssignTypeGroup(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void importDataWoCdGroup_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<MultipartFile> lstAttachments = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    ResultInSideDto result = woCdGroupBusiness.importDataWoCdGroup(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void importDataWoCdGroup_02() throws Exception {
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
    ResultInSideDto result = woCdGroupBusiness.importDataWoCdGroup(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void importDataWoCdGroup_03() throws Exception {
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
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1", "1", "1 (*)",
        "1 (*)"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        8,
        1000
    )).thenReturn(headerList);
    ResultInSideDto result = woCdGroupBusiness.importDataWoCdGroup(firstFile);
    assertEquals(result.getKey(), "NODATA");
  }

  @Test
  public void importDataWoCdGroup_04() throws Exception {
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
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1", "1", "1 (*)",
        "1 (*)"};
    Object[] header1 = new Object[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList.add(header);
    for (int i = 0; i < 600; i++) {
      headerList1.add(header1);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        5,
        0,
        8,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        6,
        0,
        8,
        1000
    )).thenReturn(headerList1);
    ResultInSideDto result = woCdGroupBusiness.importDataWoCdGroup(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");

  }

  @Test
  public void importDataWoCdGroup_05() throws Exception {
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
    Object[] header = new Object[]{"1", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1", "1", "1 (*)",
        "1 (*)"};
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
        5,
        0,
        8,
        1000
    )).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(
        new File(filePath),
        0,
        6,
        0,
        8,
        1000
    )).thenReturn(headerList1);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatable);
    ResultInSideDto result = woCdGroupBusiness.importDataWoCdGroup(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void exportDataWoCd() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    WoCdDTO woCdDTO = Mockito.spy(WoCdDTO.class);
    List<WoCdDTO> listWoCd = Mockito.spy(ArrayList.class);
    listWoCd.add(woCdDTO);
    PowerMockito.when(woCdRepository.getListWoCdExport(any())).thenReturn(listWoCd);
    File file = woCdGroupBusiness.exportDataWoCd(woCdDTO);
    Assert.assertNull(file);
  }

  @Test
  public void setMapNation() {
  }

  @Test
  public void setMapGroupType() {
  }

  @Test
  public void setMapWoType() {
  }

  @Test
  public void handleFileExport() {
  }

  @Test
  public void updateStatusCdGroup() {
  }

  @Test
  public void getListCdGroup() {
  }

  @Test
  public void getListWoCdGroupDTOByWoTypeAndGroupType() {
  }

  @Test
  public void getCdByStationCode() {
  }

  @Test
  public void getListCdGroupByUserDTO() {
  }

  @Test
  public void getCdByStationCodeNation() {
  }

  @Test
  public void getWoCdGroupWoByCdGroupCode() {
  }

  @Test
  public void getListWoCdGroupActive() {
  }

  @Test
  public void getListFtByUser() {
  }

  @Test
  public void getListWoCdGroupDTO1() {
  }

  @Test
  public void getListCdGroupByDTO() {
  }
}
