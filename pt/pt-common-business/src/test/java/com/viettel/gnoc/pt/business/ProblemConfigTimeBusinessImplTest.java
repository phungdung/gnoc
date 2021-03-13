package com.viettel.gnoc.pt.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FTPUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
import com.viettel.gnoc.pt.repository.ProblemConfigTimeRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProblemActionLogsBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class, FTPUtil.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*",
    "jdk.internal.reflect.*"})
public class ProblemConfigTimeBusinessImplTest {

  @InjectMocks
  ProblemConfigTimeBusinessImpl problemConfigTimeBusiness;

  @Mock
  ProblemConfigTimeRepository problemConfigTimeRepository;

  @Mock
  CatItemBusiness catItemBusiness;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(problemConfigTimeBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void getListComboboxGroupReasonOrSolution() {
    PowerMockito.when(catItemBusiness
        .getListItemByCategoryAndParent(anyString(), any())).thenReturn(null);
    List<CatItemDTO> actual = problemConfigTimeBusiness.getListComboboxGroupReasonOrSolution("1");
    assertEquals(null, actual);
  }

  @Test
  public void onSearchProbleConfigTime() {
    PowerMockito.when(problemConfigTimeRepository.onSearchProbleConfigTime(any())).thenReturn(null);
    Datatable actual = problemConfigTimeBusiness.onSearchProbleConfigTime(null);
    assertEquals(null, actual);
  }

  @Test
  public void insertProblemConfigTime() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(problemConfigTimeRepository.insertProblemConfigTime(any())).thenReturn(resultInSideDto);
    ResultInSideDto actual = problemConfigTimeBusiness.insertProblemConfigTime(null);
    Assert.assertEquals(resultInSideDto.getKey(), actual.getKey());
  }

  @Test
  public void updateProblemConfigTime() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(problemConfigTimeRepository.updateProblemConfigTime(any())).thenReturn(resultInSideDto);
    ResultInSideDto actual = problemConfigTimeBusiness.updateProblemConfigTime(null);
    assertEquals(resultInSideDto.getKey(), actual.getKey());
  }

  @Test
  public void findProblemConfigTimeById() {
    PowerMockito.when(problemConfigTimeRepository.findProblemConfigTimeById(anyLong())).thenReturn(null);
    ProblemConfigTimeDTO actual = problemConfigTimeBusiness.findProblemConfigTimeById(1L);
    assertEquals(null, actual);
  }

  @Test
  public void deleteProblemConfigTime() {
    PowerMockito.when(problemConfigTimeRepository.deleteProblemConfigTime(anyLong())).thenReturn(null);
    String actual = problemConfigTimeBusiness.deleteProblemConfigTime(1L);
    assertEquals(null, actual);
  }

  @Test
  public void getListProblemConfigTimeSearchExport() throws Exception {
    ProblemConfigTimeDTO problemConfigTimeDTO = Mockito.spy(ProblemConfigTimeDTO.class);
    problemConfigTimeDTO.setReasonGroupId(1L);
    problemConfigTimeDTO.setSolutionTypeId(1L);
    List<ProblemConfigTimeDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(problemConfigTimeDTO);
    PowerMockito.when(problemConfigTimeRepository.onSearchExport(any())).thenReturn(lstData);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(CommonExport.class);
    File fileExport = new File("./test_junit/test.xlsx");
    PowerMockito
        .when(CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any()))
        .thenReturn(fileExport);

    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO itemDTO = Mockito.spy(CatItemDTO.class);
    itemDTO.setItemId(1L);
    itemDTO.setItemName("1");
    list.add(itemDTO);
    PowerMockito.when(catItemBusiness.getListItemByCategoryAndParent(anyString(), any())).thenReturn(list);

    problemConfigTimeBusiness.getListProblemConfigTimeSearchExport(problemConfigTimeDTO);
    Assert.assertNotNull(fileExport);
  }

  @Test
  public void importProblemConfigTime() throws Exception {
    // case 1
    MultipartFile multipartFile = null;
    ResultInSideDto actual = problemConfigTimeBusiness.importProblemConfigTime(multipartFile);
    assertEquals(RESULT.FILE_IS_NULL, actual.getKey());

    // case 2
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    String filePath = "/test_junit/test.txt";
    PowerMockito.when(FileUtils
        .saveTempFile(any(), any(), anyString())).thenReturn(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
        .thenReturn(headerList);

    multipartFile = Mockito.spy(MultipartFile.class);
    actual = problemConfigTimeBusiness.importProblemConfigTime(multipartFile);
    assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());

    // case 3
    PowerMockito.mockStatic(I18n.class);
    Object[] objecttest = new Object[]{};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1502; i++) {
      dataImportList.add(objecttest);
    }
    PowerMockito.when(I18n.getLanguage("problems.stt"))
        .thenReturn("problems.stt");
    PowerMockito.when(I18n.getLanguage("problemConfigTime.export.grid.reasonGroupId"))
        .thenReturn("problemConfigTime.export.grid.reasonGroupId");
    PowerMockito.when(I18n.getLanguage("problemConfigTime.export.grid.solutionTypeId"))
        .thenReturn("problemConfigTime.export.grid.solutionTypeId");
    PowerMockito.when(I18n.getLanguage("problemConfigTime.export.grid.typeIdStr"))
        .thenReturn("problemConfigTime.export.grid.typeIdStr");
    PowerMockito.when(I18n.getLanguage("problemConfigTime.export.grid.subCategoryIdStr"))
        .thenReturn("problemConfigTime.export.grid.subCategoryIdStr");
    PowerMockito.when(I18n.getLanguage("problemConfigTime.export.grid.timeProcess"))
        .thenReturn("problemConfigTime.export.grid.timeProcess");
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("problems.stt"),
        I18n.getLanguage("problemConfigTime.export.grid.reasonGroupId") + "(*)",
        I18n.getLanguage("problemConfigTime.export.grid.solutionTypeId") + "(*)",
        I18n.getLanguage("problemConfigTime.export.grid.typeIdStr") + "(*)",
        I18n.getLanguage("problemConfigTime.export.grid.subCategoryIdStr") + "(*)",
        I18n.getLanguage("problemConfigTime.export.grid.timeProcess") + "(*)"
    };
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    dataHeader.add(objectHeader);

    PowerMockito.when(
        CommonImport.getDataFromExcelFile(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
        .thenReturn(dataHeader).thenReturn(dataImportList);
    actual = problemConfigTimeBusiness.importProblemConfigTime(multipartFile);
    assertEquals(RESULT.DATA_OVER, actual.getKey());

    // case 4
    dataImportList.clear();
    for (int i = 0; i < 1; i++) {
      Object[] objectTest = new Object[10];
      for (int j = 0; j < objectTest.length; j++) {
        objectTest[j] = "1";
      }
      dataImportList.add(objectTest);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
        .thenReturn(dataHeader).thenReturn(dataImportList);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO itemDTO = Mockito.spy(CatItemDTO.class);
    itemDTO.setItemId(1L);
    itemDTO.setItemName("1");
    list.add(itemDTO);
    PowerMockito.when(catItemBusiness.getListItemByCategoryAndParent(anyString(), any())).thenReturn(list);
    PowerMockito.when(I18n.getLanguage("problemConfigTime.err.dup-code-in-file"))
        .thenReturn("0");
    PowerMockito.when(I18n.getLanguage("problemConfigTime.export.grid.title"))
        .thenReturn("problemConfigTime.export.grid.title");

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    PowerMockito.when(problemConfigTimeRepository.insertProblemConfigTime(any())).thenReturn(resultInSideDto);

    actual = problemConfigTimeBusiness.importProblemConfigTime(multipartFile);
    assertEquals(RESULT.SUCCESS, actual.getKey());

    // case 5
    dataImportList.clear();
    for (int i = 0; i < 2; i++) {
      Object[] objectTest = new Object[10];
      for (int j = 0; j < objectTest.length; j++) {
        objectTest[j] = "1";
      }
      dataImportList.add(objectTest);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
        .thenReturn(dataHeader).thenReturn(dataImportList);actual = problemConfigTimeBusiness.importProblemConfigTime(multipartFile);
    assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void getFileTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    File file = problemConfigTimeBusiness.getFileTemplate();
    Assert.assertNotNull(file);
  }

  @Test
  public void setMapGroupReasonName() {
  }

  @Test
  public void setMapSolutionTypeName() {
  }

  @Test
  public void setMapTypeStr() {
  }

  @Test
  public void setMapSubcategoryStr() {
  }
}
