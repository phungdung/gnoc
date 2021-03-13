package com.viettel.gnoc.sr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SROutsideRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import com.viettel.security.PassTranformer;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.xml.security.utils.Base64;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({SrAomBusinessImpl.class, FileUtils.class, CommonImport.class,
    CommonExport.class, TicketProvider.class,Base64.class, PassTranformer.class, I18n.class, DateTimeUtils.class, InputStream.class,
    FileUtils.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class SrAomBusinessImplTest {

  @InjectMocks
  SrAomBusinessImpl srAomBusiness;

  @Mock
  SRConfigRepository srConfigRepository;

  @Mock
  SROutsideRepository srOutsideRepository;

  @Mock
  SrRepository srRepository;

  @Mock
  SrBusiness srBusiness;

  @Mock
  UserRepository userRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  FileUtils fileUtils;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Test
  public void testGetListSRForGatePro_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    ResultDTO resultDTO = srAomBusiness.getListSRForGatePro(null, null);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testGetListSRForGatePro_02() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String fromDate = "27/04/2020";
    ResultDTO resultDTO = srAomBusiness.getListSRForGatePro(fromDate, null);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testGetListSRForGatePro_03() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String fromDate = "27/04/2020 10:05:47";
    List<SRConfigDTO> lstAomConfig = Mockito.spy(ArrayList.class);
    PowerMockito.when(srConfigRepository.getByConfigGroup("abc")).thenReturn(lstAomConfig);

    ResultDTO resultDTO = srAomBusiness.getListSRForGatePro(fromDate, null);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testGetListSRForGatePro_04() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(Constants.RESULT.SUCCESS);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String fromDate = "27/04/2020 10:05:47";
    String toDate = "28/04/2020 10:05:47";
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigName("cfg name");
    List<SRConfigDTO> lstAomConfig = Mockito.spy(ArrayList.class);
    lstAomConfig.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup("DICH_VU_AOM")).thenReturn(lstAomConfig);

    SRDTO srdto = Mockito.spy(SRDTO.class);
    srdto.setSrCode("ABCGHI");
    List<SRDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srdto);
    PowerMockito.when(srOutsideRepository.getListSRForGatePro(anyString(), anyString(), any()))
        .thenReturn(lstSR);

    ResultDTO resultDTO = srAomBusiness.getListSRForGatePro(fromDate, toDate);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }


  @Test
  public void testUpdateSRForGatePro_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro(null, null, null);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testUpdateSRForGatePro_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro("abc", null, null);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testUpdateSRForGatePro_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro("abc", "dev", null);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testUpdateSRForGatePro_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String srCode = "123";
    String status = "dev";
    String fileContent = "FILE_Test";
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    List<SRDTO> lstSR = Mockito.spy(ArrayList.class);
    PowerMockito.when(srOutsideRepository.getListSRForGatePro(null, null, srDTO)).thenReturn(lstSR);
    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro(srCode, status, fileContent);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testUpdateSRForGatePro_05() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String srCode = "123";
    String status = "dev";
    String fileContent = "FILE_Test";
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    srDTO.setSrCode("123");
    srDTO.setSrId("123");
    List<SRDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srDTO);
    PowerMockito.when(srOutsideRepository.getListSRForGatePro(any(), any(), any()))
        .thenReturn(lstSR);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
//    srConfigDTO.setConfigCode("ccc123");
    List<SRConfigDTO> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstStatus);

    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro(srCode, status, fileContent);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }


  @Test
  public void testUpdateSRForGatePro_06() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String srCode = "123";
    String status = "rtg";
    String fileContent = "FILE_Test";
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    srDTO.setSrCode("123");
    srDTO.setSrId("123");
    List<SRDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srDTO);
    PowerMockito.when(srOutsideRepository.getListSRForGatePro(any(), any(), any()))
        .thenReturn(lstSR);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    List<SRConfigDTO> lstStatus = null;
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstStatus);

    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro(srCode, status, fileContent);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testUpdateSRForGatePro_07() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String srCode = "123";
    String status = "dev";
    String fileContent = "FILE_Test";
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    srDTO.setSrCode("123");
    srDTO.setSrId("123");
    srDTO.setStatus("dev");
    List<SRDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srDTO);
    PowerMockito.when(srOutsideRepository.getListSRForGatePro(any(), any(), any()))
        .thenReturn(lstSR);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("dev");
    List<SRConfigDTO> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstStatus);

    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro(srCode, status, fileContent);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testUpdateSRForGatePro_08() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String srCode = "123";
    String status = "dev";
    String fileContent = "FILE_Test";
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    srDTO.setSrCode("123");
    srDTO.setSrId("123");
    srDTO.setStatus("Draft"); // "New" //"Rejected"//"Cancelled"//"Closed"
    List<SRDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srDTO);
    PowerMockito.when(srOutsideRepository.getListSRForGatePro(any(), any(), any()))
        .thenReturn(lstSR);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("dev");
    List<SRConfigDTO> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstStatus);

    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro(srCode, status, fileContent);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testUpdateSRForGatePro_09() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String srCode = "123";
    String status = "dev";
    String fileContent = "FILE_Test";
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    srDTO.setSrCode("123");
    srDTO.setSrId("123");
    srDTO.setStatus("New"); // "New" //"Rejected"//"Cancelled"//"Closed"
    List<SRDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srDTO);
    PowerMockito.when(srOutsideRepository.getListSRForGatePro(any(), any(), any()))
        .thenReturn(lstSR);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("dev");
    List<SRConfigDTO> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstStatus);

    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro(srCode, status, fileContent);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testUpdateSRForGatePro_10() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String srCode = "123";
    String status = "dev";
    String fileContent = "FILE_Test";
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    srDTO.setSrCode("123");
    srDTO.setSrId("123");
    srDTO.setStatus("Rejected"); // "New" //"Rejected"//"Cancelled"//"Closed"
    List<SRDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srDTO);
    PowerMockito.when(srOutsideRepository.getListSRForGatePro(any(), any(), any()))
        .thenReturn(lstSR);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("dev");
    List<SRConfigDTO> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstStatus);

    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro(srCode, status, fileContent);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testUpdateSRForGatePro_11() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String srCode = "123";
    String status = "dev";
    String fileContent = "FILE_Test";
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    srDTO.setSrCode("123");
    srDTO.setSrId("123");
    srDTO.setStatus("Cancelled"); // "New" //"Rejected"//"Cancelled"//"Closed"
    List<SRDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srDTO);
    PowerMockito.when(srOutsideRepository.getListSRForGatePro(any(), any(), any()))
        .thenReturn(lstSR);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("dev");
    List<SRConfigDTO> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstStatus);

    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro(srCode, status, fileContent);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testUpdateSRForGatePro_12() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String srCode = "123";
    String status = "dev";
    String fileContent = "FILE_Test";
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    srDTO.setSrCode("123");
    srDTO.setSrId("123");
    srDTO.setStatus("Closed"); // "New" //"Rejected"//"Cancelled"//"Closed"
    List<SRDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srDTO);
    PowerMockito.when(srOutsideRepository.getListSRForGatePro(any(), any(), any()))
        .thenReturn(lstSR);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("dev");
    List<SRConfigDTO> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstStatus);

    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro(srCode, status, fileContent);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  @Test
  public void testUpdateSRForGatePro_13() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(Base64.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey(RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    String srCode = "123";
    String status = "dev";
    String fileContent = "FILE_Test";
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    srDTO.setSrCode("123");
    srDTO.setSrId("123");
    srDTO.setStatus("Closedd");
    srDTO.setSrUser("1");
    List<SRDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srDTO);
    PowerMockito.when(srOutsideRepository.getListSRForGatePro(any(), any(), any()))
        .thenReturn(lstSR);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("dev");
    List<SRConfigDTO> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add(srConfigDTO);
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    usersEntity.setUserId(1L);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("1");
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    resultFileDataOld.setId(1L);
    PowerMockito.when(srBusiness.updateStatusSRForProcess(anyString(), any())).thenReturn("SUCCESS");
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(usersEntity);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstStatus);

    ResultDTO resultDTO = srAomBusiness.updateSRForGatePro(srCode, status, fileContent);
    Assert.assertEquals(resultDTO.getKey(),"SUCCESS");
  }

}
