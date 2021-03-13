package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import com.viettel.gnoc.mr.repository.MrCauseWoWasCompletedRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.crypto.Data;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrCauseWoWasCompletedBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class MrCauseWoWasCompletedBusinessImplTest {

  @InjectMocks
  MrCauseWoWasCompletedBusinessImpl mrCauseWoWasCompletedBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  MrCauseWoWasCompletedRepository mrCauseWoWasCompletedRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrCauseWoWasCompletedBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void test_onSearch() {
    MrCauseWoWasCompletedDTO mrCauseWoWasCompletedDTO = Mockito.spy(MrCauseWoWasCompletedDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    PowerMockito.when(mrCauseWoWasCompletedRepository.onSearch(any())).thenReturn(datatable);
    Datatable result = mrCauseWoWasCompletedBusiness.onSearch(mrCauseWoWasCompletedDTO);
    assertEquals(datatable.getPages(), result.getPages());
  }

  @Test
  public void test_insertOrUpdate() {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MrCauseWoWasCompletedDTO dto = Mockito.spy(MrCauseWoWasCompletedDTO.class);
    dto.setReasonCode("1");
    dto.setReasonType("1");

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setCheck(true);
    PowerMockito.when(mrCauseWoWasCompletedRepository.checkExisted(anyString(), anyString(), any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(mrCauseWoWasCompletedRepository.insertOrUpdate(any()))
        .thenReturn(resultInSideDto);

    mrCauseWoWasCompletedBusiness.insertOrUpdate(dto);

    dto.setId(1L);
    resultInSideDto.setCheck(false);
    PowerMockito.when(mrCauseWoWasCompletedRepository.checkExisted(anyString(), anyString(), any()))
        .thenReturn(resultInSideDto);

    mrCauseWoWasCompletedBusiness.insertOrUpdate(dto);
  }

  @Test
  public void test_findById() {
    MrCauseWoWasCompletedDTO dto = Mockito.spy(MrCauseWoWasCompletedDTO.class);
    dto.setId(1L);
    PowerMockito.when(mrCauseWoWasCompletedRepository.findById(anyLong()))
        .thenReturn(dto);
    MrCauseWoWasCompletedDTO result = mrCauseWoWasCompletedBusiness.findById(1L);
    assertEquals(result.getId(), dto.getId());
  }

  @Test
  public void test_exportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    MrCauseWoWasCompletedDTO mrCauseWoWasCompletedDTO = Mockito.spy(MrCauseWoWasCompletedDTO.class);
    List<MrCauseWoWasCompletedDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(mrCauseWoWasCompletedDTO);

    File fileExportSuccess = new File("./test_junit/test.txt");

    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    PowerMockito.when(mrCauseWoWasCompletedRepository
        .onSearchExport(any())).thenReturn(lstData);
    mrCauseWoWasCompletedBusiness.exportData(mrCauseWoWasCompletedDTO);
  }

  @Test
  public void test_getTemplate() throws IOException {
    PowerMockito.mockStatic(I18n.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO itemDTO = Mockito.spy(CatItemDTO.class);
    itemDTO.setItemId(1L);
    list.add(itemDTO);
    datatable.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMasterHasParent(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatable);
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonCode"))
        .thenReturn("mrCauseWoWasNotCompleted.export.grid.reasonCode");
    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonName"))
        .thenReturn("mrCauseWoWasNotCompleted.export.grid.reasonName");
    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonType"))
        .thenReturn("mrCauseWoWasNotCompleted.export.grid.reasonType");
    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.waitingTime"))
        .thenReturn("mrCauseWoWasNotCompleted.export.grid.waitingTime");

    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.import.title"))
        .thenReturn("mrCauseWoWasNotCompleted.import.title");
    mrCauseWoWasCompletedBusiness.getTemplate();
  }

  @Test
  public void test_importData() throws IOException {
    PowerMockito.mockStatic(I18n.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO itemDTO = Mockito.spy(CatItemDTO.class);
    itemDTO.setItemId(1L);
    list.add(itemDTO);
    datatable.setData(list);
    PowerMockito.when(catItemRepository
        .getItemMasterHasParent(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(datatable);
    // case 1
    MultipartFile multipartFile = null;
    ResultInSideDto result = mrCauseWoWasCompletedBusiness.importData(multipartFile);
    assertEquals(result.getKey(), RESULT.FILE_IS_NULL);

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
    result = mrCauseWoWasCompletedBusiness.importData(multipartFile);
    assertEquals(result.getKey(), RESULT.FILE_INVALID_FORMAT);

    // case 3
    Object[] objecttest = new Object[]{};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1002; i++) {
      dataImportList.add(objecttest);
    }
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFileNew(any(File.class), anyInt(), anyInt(), anyInt(), anyInt(),
                anyInt()
            ))
        .thenReturn(dataImportList);
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonCode"))
        .thenReturn("mrCauseWoWasNotCompleted.export.grid.reasonCode");
    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonName"))
        .thenReturn("mrCauseWoWasNotCompleted.export.grid.reasonName");
    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonType"))
        .thenReturn("mrCauseWoWasNotCompleted.export.grid.reasonType");
    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.waitingTime"))
        .thenReturn("mrCauseWoWasNotCompleted.export.grid.waitingTime");
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonCode") + "(*)",
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonName") + "(*)",
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonType") + "(*)",
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.waitingTime") + "(*)"
    };
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(any(File.class), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()
            ))
        .thenReturn(dataHeader);
    result = mrCauseWoWasCompletedBusiness.importData(multipartFile);
    assertEquals(result.getKey(), "ERROR_NO_DOWNLOAD");

    // case 4
    dataImportList.clear();
    for (int i = 0; i < 3; i++) {
      Object[] objectTest = new Object[5];
      for (int j = 0; j < objectTest.length; j++) {
        objectTest[j] = "1";
      }
      dataImportList.add(objectTest);
    }
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setCheck(false);
    PowerMockito.when(mrCauseWoWasCompletedRepository.checkExisted(anyString(), anyString(), any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.err.dup-code-in-file"))
        .thenReturn("0");
    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.import.title"))
        .thenReturn("mrCauseWoWasNotCompleted.import.title");
    PowerMockito.when(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.title"))
        .thenReturn("mrCauseWoWasNotCompleted.export.grid.title");
    result = mrCauseWoWasCompletedBusiness.importData(multipartFile);
    assertEquals(result.getKey(), RESULT.ERROR);
  }

  @Test
  public void test_delete() {
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(mrCauseWoWasCompletedRepository.delete(anyLong()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = mrCauseWoWasCompletedBusiness.delete(1L);
    assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void test_getReasonWO() {
    List<MrCauseWoWasCompletedDTO> mrCauseWoWasCompletedDTOS = Mockito.spy(ArrayList.class);
    PowerMockito.when(mrCauseWoWasCompletedRepository.getReasonWO(anyString()))
        .thenReturn(mrCauseWoWasCompletedDTOS);
    List<MrCauseWoWasCompletedDTO> result = mrCauseWoWasCompletedBusiness.getReasonWO("1");
    assertEquals(result.size(), mrCauseWoWasCompletedDTOS.size());
  }
}
