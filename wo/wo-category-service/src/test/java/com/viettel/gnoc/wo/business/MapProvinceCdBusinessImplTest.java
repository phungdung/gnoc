package com.viettel.gnoc.wo.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.wo.dto.MapProvinceCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.repository.MapProvinceCdRepository;
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
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MapProvinceCdBusinessImpl.class, I18n.class, CommonImport.class, FileUtils.class,
    CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MapProvinceCdBusinessImplTest {

  @InjectMocks
  MapProvinceCdBusinessImpl mapProvinceCdBusiness;
  @Mock
  MapProvinceCdRepository mapProvinceCdRepository;
  @Mock
  CatLocationBusiness catLocationBusiness;
  @Mock
  WoCdGroupBusiness woCdGroupBusiness;

  @Before
  public void setUpTempFolder() {
    ReflectionTestUtils.setField(mapProvinceCdBusiness, "tempFolder",
        "./wo-upload");
  }

  @Test
  public void deleteMapProvinceCd_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(mapProvinceCdRepository.deleteMapProvinceCd(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = mapProvinceCdBusiness.deleteMapProvinceCd(1L);
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void add_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(mapProvinceCdRepository.add(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = mapProvinceCdBusiness.add(any());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void edit_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(mapProvinceCdRepository.edit(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultInSideDto1 = mapProvinceCdBusiness.edit(any());
    Assert.assertEquals(resultInSideDto.getKey(), resultInSideDto1.getKey());
  }

  @Test
  public void getDetail_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    MapProvinceCdDTO mapProvinceCdDTO = Mockito.spy(MapProvinceCdDTO.class);
    PowerMockito.when(mapProvinceCdRepository.getDetail(anyLong())).thenReturn(mapProvinceCdDTO);
    MapProvinceCdDTO mapProvinceCdDTO1 = mapProvinceCdBusiness.getDetail(anyLong());
    Assert.assertNotNull(mapProvinceCdDTO1);
  }

  @Test
  public void getListDTOSearchWeb_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(mapProvinceCdRepository.getListDTOSearchWeb(any())).thenReturn(datatable);
    Datatable datatable1 = mapProvinceCdBusiness.getListDTOSearchWeb(any());
    Assert.assertEquals(datatable.getPages(), datatable1.getPages());
  }

  @Test
  public void exportData_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("STT");
    List<MapProvinceCdDTO> list = Mockito.spy(ArrayList.class);
    MapProvinceCdDTO mapProvinceCdDTO = Mockito.spy(MapProvinceCdDTO.class);
    list.add(mapProvinceCdDTO);
    PowerMockito.when(mapProvinceCdRepository.getListDTOSearchWebExport(any())).thenReturn(list);
    File file = mapProvinceCdBusiness.exportData(any());
    Assert.assertNotNull(file);
  }

  @Test
  public void getTemplate_01() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("STT");
    List<CatLocationDTO> listProvince = Mockito.spy(ArrayList.class);
    CatLocationDTO dto = Mockito.spy(CatLocationDTO.class);
    dto.setLocationName("VNM");
    listProvince.add(dto);
    PowerMockito.when(catLocationBusiness.getListLocationProvince()).thenReturn(listProvince);
    WoCdGroupInsideDTO dto1 = Mockito.spy(WoCdGroupInsideDTO.class);
    dto1.setWoGroupName("vtnet");
    dto1.setWoGroupId(1L);
    List<WoCdGroupInsideDTO> listCdGroup = Mockito.spy(ArrayList.class);
    listCdGroup.add(dto1);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(listCdGroup);
    PowerMockito.when(woCdGroupBusiness.getListWoCdGroupDTO(any())).thenReturn(datatable);
    File file = mapProvinceCdBusiness.getTemplate();
    Assert.assertNotNull(file);
  }

  @Test
  public void setMapProvince_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    CatLocationDTO dto = Mockito.spy(CatLocationDTO.class);
    dto.setLocationName("VNM");
    dto.setLocationName("VNM");
    List<CatLocationDTO> list = Mockito.spy(ArrayList.class);
    list.add(dto);
    PowerMockito.when(catLocationBusiness.getListLocationProvince()).thenReturn(list);
    mapProvinceCdBusiness.setMapProvince();
  }

  @Test
  public void setMapCdGroup_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    WoCdGroupInsideDTO dto = Mockito.spy(WoCdGroupInsideDTO.class);
    dto.setWoGroupName("fixBug");
    dto.setWoGroupId(1L);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<WoCdGroupInsideDTO> list = Mockito.spy(ArrayList.class);
    list.add(dto);
    datatable.setData(list);
    PowerMockito.when(woCdGroupBusiness.getListWoCdGroupDTO(any())).thenReturn(datatable);
    mapProvinceCdBusiness.setMapCdGroup();
  }

  @Test
  public void testImportData_01() {
    ResultInSideDto actual = mapProvinceCdBusiness.importData(null);
    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportData_02() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
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
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 3, 0, 7, 1000
        )
    ).thenReturn(lstHeader);

    ResultInSideDto actual = mapProvinceCdBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportData_03() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
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
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    Object[] objects1 = new Object[] {
        "1", "1(*)", "1(*)", "1(*)", "1(*)", "1(*)", "1(*)"
    };
    lstHeader.add(objects1);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2502; i++) {
      lstData.add(objects1);
    }

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 3, 0, 7, 1000
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 5, 0, 7, 1000
        )
    ).thenReturn(lstData);

    ResultInSideDto actual = mapProvinceCdBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportData_04() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
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
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    Object[] objects1 = new Object[] {
        "1", "1(*)", "1(*)", "1(*)", "1(*)", "1(*)", "1(*)"
    };
    lstHeader.add(objects1);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 3, 0, 7, 1000
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 5, 0, 7, 1000
        )
    ).thenReturn(lstData);
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), any(), anyString(), any())
    ).thenReturn(new File("/temp"));

    ResultInSideDto actual = mapProvinceCdBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void testImportData_05() throws Exception {
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
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
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    Object[] objects1 = new Object[] {
        "1", "1(*)", "1(*)", "1(*)", "1(*)", "1(*)", "1(*)"
    };
    lstHeader.add(objects1);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    Object[] objects2 = new Object[] {
        "1", "1", "1", "1", "1", "1", "1"
    };
    lstData.add(objects2);
    List<WoCdGroupInsideDTO> lst = Mockito.spy(ArrayList.class);
    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupName("1");
    woCdGroupInsideDTO.setWoGroupId(1L);
    lst.add(woCdGroupInsideDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lst);
    List<CatLocationDTO> list = Mockito.spy(ArrayList.class);
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationName("1");
    catLocationDTO.setLocationCode("1");
    PowerMockito.when(
        catLocationBusiness.getListLocationProvince()
    ).thenReturn(list);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 3, 0, 7, 1000
        )
    ).thenReturn(lstHeader);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 5, 0, 7, 1000
        )
    ).thenReturn(lstData);
    PowerMockito.when(
        woCdGroupBusiness.getListWoCdGroupDTO(any())
    ).thenReturn(datatable);

    ResultInSideDto actual = mapProvinceCdBusiness.importData(multipartFile);
    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }
}
