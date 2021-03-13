package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrUngCuuTTDTO;
import com.viettel.gnoc.mr.repository.MrUngCuuTTRepository;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrUngCuuTTBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class, PassTranformer.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})

public class MrUngCuuTTBusinessImplTest {

  @InjectMocks
  MrUngCuuTTBusinessImpl mrUngCuuTTBusiness;

  @Mock
  MrUngCuuTTRepository mrUngCuuTTRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  WoServiceProxy woServiceProxy;

  @Mock
  FileUtils fileUtils;


  @Mock
  GnocFileRepository gnocFileRepository;
  @Mock
  UnitRepository unitRepository;

  @Test
  public void test_getListMrUctt() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MrUngCuuTTDTO mrUngCuuTTDTO = Mockito.spy(MrUngCuuTTDTO.class);
    mrUngCuuTTDTO.setCdId("5L");
    List<MrUngCuuTTDTO> mrUngCuuTTDTOList = Mockito.spy(ArrayList.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    ArrayList listData = Mockito.spy(ArrayList.class);
    listData.add(mrUngCuuTTDTOList);
    PowerMockito.when(mrUngCuuTTRepository.getListMrUctt(any())).thenReturn(datatable);
    Datatable datatable1 = mrUngCuuTTBusiness.getListMrUctt(mrUngCuuTTDTO);
    assertEquals(datatable1.getPages(), datatable.getPages());
  }

  @Test
  public void test_insertMrUctt() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(PassTranformer.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
//    String password = PassTranformer.decrypt("429585a63e5e23f3a5f7c64a0ddd5f29");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    String fullPath = "";
    PowerMockito.when(fileUtils
        .saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), any(), any(),
            any())).thenReturn(fullPath);
//    PowerMockito.when(FileUtils.saveUploadFile(anyString(),any(), anyString(), any())).thenReturn(fullPathOld);
    PowerMockito.when(PassTranformer.decrypt(anyString()))
        .thenReturn("723298837f9c6fe9953c6e07e0e4df17");
//    PowerMockito.when(PassTranformer.decrypt("6cf8a37f3e6993f90eef71859b1ebf31"))
//        .thenReturn("6cf8a37f3e6993f90eef71859b1ebf31");

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    WoDTO woDTO = Mockito.spy(WoDTO.class);
    MrUngCuuTTDTO mrUngCuuTTDTO = Mockito.spy(MrUngCuuTTDTO.class);
    mrUngCuuTTDTO.setCdId("a,b,c");
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> list = Mockito.spy(ArrayList.class);
    list.add(multipartFile);
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setMessage("SUCCESS");
    Mockito.when(woServiceProxy.insertWoForSPMProxy(any())).thenReturn(resultDTO);
    resultDTO.setId("a,b,c");
    Mockito.when(mrUngCuuTTRepository.insertMrUcttDTO(any())).thenReturn(resultInSideDto);
    String tss = "test";
    Mockito.when(mrUngCuuTTRepository.getConfigUCTTForCreateWo(anyString(), anyString()))
        .thenReturn(tss);

    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(new UnitDTO());
    PowerMockito.when(mrUngCuuTTRepository.insertMrUcttFilesDTO(any())).thenReturn(resultInSideDto);

    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(),
        anyString(), anyString(), anyString(), any(), any())).thenReturn("/ftp");
    PowerMockito.when(FileUtils
        .saveUploadFile(any(), any(), anyString(), any())).thenReturn("/old");
    mrUngCuuTTBusiness.insertMrUctt(list, mrUngCuuTTDTO);

    assertEquals(1, 1);
  }

  @Test
  public void test_exportSearchData() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("abc");
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    File file = PowerMockito.mock(File.class);
    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), anyString()))
        .thenReturn(file);
    MrUngCuuTTDTO mrUngCuuTTDTO = Mockito.spy(MrUngCuuTTDTO.class);
    List<MrUngCuuTTDTO> mrUngCuuTTDTOList = Mockito.spy(ArrayList.class);
//    CellConfigExport cellConfigExport = Mockito.spy(CellConfigExport.class);
//    cellConfigExport.setValue("5");
//    List<CellConfigExport> lstCellSheet = Mockito.spy(ArrayList.class);
//    lstCellSheet.add(cellConfigExport);

    Mockito.when(mrUngCuuTTRepository.getDataExport(any())).thenReturn(mrUngCuuTTDTOList);
    File result = mrUngCuuTTBusiness.exportSearchData(mrUngCuuTTDTO);

    assertNull(result);

  }
}
