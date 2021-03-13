package com.viettel.gnoc.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MappingVsaUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.repository.MappingVsaUnitRepository;
import com.viettel.gnoc.repository.UnitCommonRepository;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MappingVsaUnitBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class MappingVsaUnitBusinessImplTest {

  @InjectMocks
  MappingVsaUnitBusinessImpl mappingVsaUnitBusiness;

  @Mock
  MappingVsaUnitRepository mappingVsaUnitRepository;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitCommonRepository unitCommonRepository;

  @Test
  public void getListMappingVsaUnitDTO() {
    Datatable expected = Mockito.spy(Datatable.class);
    PowerMockito.when(
        mappingVsaUnitRepository.getListMappingVsaUnitDTO(any())
    ).thenReturn(expected);

    MappingVsaUnitDTO mappingVsaUnitDTO = Mockito.spy(MappingVsaUnitDTO.class);
    Datatable actual = mappingVsaUnitBusiness.getListMappingVsaUnitDTO(mappingVsaUnitDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void updateMappingVsaUnit_01() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mappingVsaUnitRepository.updateMappingVsaUnit(any())
    ).thenReturn(expected);

    MappingVsaUnitDTO mappingVsaUnitDTO1 = Mockito.spy(MappingVsaUnitDTO.class);
    List<MappingVsaUnitDTO> list = Mockito.spy(ArrayList.class);
    list.add(mappingVsaUnitDTO1);
    PowerMockito.when(
        mappingVsaUnitRepository.checkExistVsaUnit(any())
    ).thenReturn(list);

    MappingVsaUnitDTO mappingVsaUnitDTO = Mockito.spy(MappingVsaUnitDTO.class);
    List<Long> listVsaUnit = Mockito.spy(ArrayList.class);
    listVsaUnit.add(1L);
    listVsaUnit.add(2L);
    mappingVsaUnitDTO.setVsaUnit(listVsaUnit);
    mappingVsaUnitDTO.setAppUnitId(1L);
    ResultInSideDto actual = mappingVsaUnitBusiness.updateMappingVsaUnit(mappingVsaUnitDTO);

    Assert.assertEquals(RESULT.DUPLICATE, actual.getKey());
  }

  @Test
  public void updateMappingVsaUnit_02() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mappingVsaUnitRepository.updateMappingVsaUnit(any())
    ).thenReturn(expected);

    List<MappingVsaUnitDTO> list = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mappingVsaUnitRepository.checkExistVsaUnit(any())
    ).thenReturn(list);

    MappingVsaUnitDTO mappingVsaUnitDTO = Mockito.spy(MappingVsaUnitDTO.class);
    List<Long> listVsaUnit = Mockito.spy(ArrayList.class);
    listVsaUnit.add(1L);
    listVsaUnit.add(2L);
    mappingVsaUnitDTO.setVsaUnit(listVsaUnit);
    mappingVsaUnitDTO.setAppUnitId(1L);
    ResultInSideDto actual = mappingVsaUnitBusiness.updateMappingVsaUnit(mappingVsaUnitDTO);

    Assert.assertEquals(expected.getKey(), actual.getKey());
  }

  @Test
  public void deleteMappingVsaUnit() {
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(
        mappingVsaUnitRepository.deleteMappingVsaUnit(anyLong())
    ).thenReturn(expected);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    ResultInSideDto actual = mappingVsaUnitBusiness.deleteMappingVsaUnit(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void deleteListMappingVsaUnit() {
    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    PowerMockito.when(
        mappingVsaUnitRepository.deleteListMappingVsaUnit(any())
    ).thenReturn(expected);

    List<MappingVsaUnitDTO> list = Mockito.spy(ArrayList.class);
    ResultInSideDto actual = mappingVsaUnitBusiness.deleteListMappingVsaUnit(list);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void findMappingVsaUnitById() {
    MappingVsaUnitDTO expected = Mockito.spy(MappingVsaUnitDTO.class);
    PowerMockito.when(
        mappingVsaUnitRepository.findMappingVsaUnitById(anyLong())
    ).thenReturn(expected);

    MappingVsaUnitDTO actual = mappingVsaUnitBusiness.findMappingVsaUnitById(1L);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void insertMappingVsaUnit_01() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    MappingVsaUnitDTO mappingVsaUnitDTO1 = Mockito.spy(MappingVsaUnitDTO.class);
    List<MappingVsaUnitDTO> list1 = Mockito.spy(ArrayList.class);
    list1.add(mappingVsaUnitDTO1);
    PowerMockito.when(
        mappingVsaUnitRepository.checkExistVsaUnit(any())
    ).thenReturn(list1);

    MappingVsaUnitDTO mappingVsaUnitDTO = Mockito.spy(MappingVsaUnitDTO.class);
    List<Long> list = Mockito.spy(ArrayList.class);
    list.add(1L);
    list.add(2L);
    mappingVsaUnitDTO.setVsaUnit(list);
    mappingVsaUnitDTO.setAppUnitId(1L);
    ResultInSideDto actual = mappingVsaUnitBusiness.insertMappingVsaUnit(mappingVsaUnitDTO);

    Assert.assertEquals(RESULT.DUPLICATE, actual.getKey());
  }

  @Test
  public void insertMappingVsaUnit_02() {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    PowerMockito.when(
        mappingVsaUnitRepository
            .insertMappingVsaUnit(any())
    ).thenReturn(expected);

    List<MappingVsaUnitDTO> list1 = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        mappingVsaUnitRepository.checkExistVsaUnit(any())
    ).thenReturn(list1);

    MappingVsaUnitDTO mappingVsaUnitDTO = Mockito.spy(MappingVsaUnitDTO.class);
    List<Long> list = Mockito.spy(ArrayList.class);
    list.add(1L);
    list.add(2L);
    mappingVsaUnitDTO.setVsaUnit(list);
    mappingVsaUnitDTO.setAppUnitId(1L);
    ResultInSideDto actual = mappingVsaUnitBusiness.insertMappingVsaUnit(mappingVsaUnitDTO);

    Assert.assertEquals(expected.getKey(), actual.getKey());
  }

  @Test
  public void insertOrUpdateListMappingVsaUnit() {
    PowerMockito.mockStatic(TicketProvider.class);

    ResultInSideDto expected = Mockito.spy(ResultInSideDto.class);
    expected.setKey(Constants.RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);
    PowerMockito.when(
        mappingVsaUnitRepository
            .insertOrUpdateListMappingVsaUnit(any())
    ).thenReturn(expected);

    List<MappingVsaUnitDTO> mappingVsaUnitDTO = Mockito.spy(ArrayList.class);
    ResultInSideDto actual = mappingVsaUnitBusiness
        .insertOrUpdateListMappingVsaUnit(mappingVsaUnitDTO);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getVsaTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("test");
    PowerMockito.when(I18n.getLanguage("mappingVSA.list.appUnitCode"))
        .thenReturn("AppUnitCode");
    PowerMockito.when(I18n.getLanguage("mappingVSA.list.vsaUnitCode"))
        .thenReturn("VsaUnitCode");

    File actual = mappingVsaUnitBusiness.getVsaTemplate();

    Assert.assertNotNull(actual);
  }

  @Test
  public void importData_01() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MultipartFile testFile = null;
    ResultInSideDto actual = mappingVsaUnitBusiness.importData(testFile);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void importData_02() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())
    ).thenReturn(headerList);

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = mappingVsaUnitBusiness.importData(testFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void importData_03() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mappingVSA.list.appUnitCode")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mappingVSA.list.vsaUnitCode")).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects = new Object[]{"1(*)", "1(*)"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            1,
            2
        )
    ).thenReturn(headerList);

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = mappingVsaUnitBusiness.importData(testFile);

    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void importData_04() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("mappingVSA.list.appUnitCode")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mappingVSA.list.vsaUnitCode")).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects = new Object[]{"1(*)", "1(*)"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            1,
            2
        )
    ).thenReturn(headerList);

    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1501; i++) {
      lstDataAll.add(objects);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            1,
            1000
        )
    ).thenReturn(lstDataAll);

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = mappingVsaUnitBusiness.importData(testFile);

    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void importData_05() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when((I18n.getLanguage(anyString()))).thenReturn("test");
    PowerMockito.when(I18n.getLanguage("mappingVSA.list.appUnitCode")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mappingVSA.list.vsaUnitCode")).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects = new Object[]{"1(*)", "1(*)"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            1,
            2
        )
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{null, null};
    Object[] objects2 = new Object[]{"1(*)", "2(*)"};

    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    lstDataAll.add(objects);
    lstDataAll.add(objects1);
    lstDataAll.add(objects2);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            1,
            1000
        )
    ).thenReturn(lstDataAll);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    List<UnitDTO> lstVSA = Mockito.spy(ArrayList.class);
    lstVSA.add(unitDTO);
    PowerMockito.when(
        unitCommonRepository.getListUnitVSA(any())
    ).thenReturn(lstVSA);
    PowerMockito.when(
        unitCommonRepository.getListUnitNotLike(any())
    ).thenReturn(lstVSA);

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = mappingVsaUnitBusiness.importData(testFile);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void importData_06() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when((I18n.getLanguage(anyString()))).thenReturn("test");
    PowerMockito.when(I18n.getLanguage("mappingVSA.list.appUnitCode")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("mappingVSA.list.vsaUnitCode")).thenReturn("1");

    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String filePath = "/temp";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImport = new File(filePath);

    Object[] objects = new Object[]{"1(*)", "1(*)"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            1,
            2
        )
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{"1(*)", "2(*)"};

    List<Object[]> lstDataAll = Mockito.spy(ArrayList.class);
    lstDataAll.add(objects);
    lstDataAll.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            1,
            1000
        )
    ).thenReturn(lstDataAll);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    List<UnitDTO> lstVSA = Mockito.spy(ArrayList.class);
    lstVSA.add(unitDTO);

    PowerMockito.when(
        unitCommonRepository.getListUnitVSA(any())
    ).thenReturn(lstVSA);
    PowerMockito.when(
        unitCommonRepository.getListUnitNotLike(any())
    ).thenReturn(lstVSA);
    PowerMockito.when(
        unitCommonRepository.getListUnit(any())
    ).thenReturn(lstVSA);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.when(
        mappingVsaUnitRepository
            .insertMappingVsaUnit(any())
    ).thenReturn(resultInSideDto);

    MockMultipartFile testFile = new MockMultipartFile(
        "data", "filename.xlsx",
        "text/plain",
        "some xml".getBytes()
    );
    ResultInSideDto actual = mappingVsaUnitBusiness.importData(testFile);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }
}
