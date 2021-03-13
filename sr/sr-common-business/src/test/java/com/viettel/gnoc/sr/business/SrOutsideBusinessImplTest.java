package com.viettel.gnoc.sr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.SrCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRCreatedFromOtherSysDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRFilesDTO;
import com.viettel.gnoc.sr.dto.SRHisDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.repository.SRCatalogRepository2;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SRCreatedFromOtherSysRepository;
import com.viettel.gnoc.sr.repository.SRHisRepository;
import com.viettel.gnoc.sr.repository.SROutsideRepository;
import com.viettel.gnoc.sr.repository.SRWorkLogRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
@PrepareForTest({SrOutsideBusinessImplTest.class, FileUtils.class, CommonImport.class,
    CommonExport.class, TicketProvider.class, I18n.class, PassTranformer.class, DateTimeUtils.class, InputStream.class,
    FileUtils.class,Base64.class, DateUtil.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class

SrOutsideBusinessImplTest {

  @InjectMocks
  SrOutsideBusinessImpl srOutsideBusiness;

  @Mock
  SrBusiness srBusiness;

  @Mock
  SRConfigRepository srConfigRepository;

  @Mock
  SRCatalogRepository2 srCatalogRepository2;

  @Mock
  SrRepository srRepository;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  SROutsideRepository srOutsideRepository;

  @Mock
  SRCreatedFromOtherSysRepository srCreatedFromOtherSysRepository;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  SRWorkLogRepository srWorkLogRepository;

  @Mock
  SrCategoryServiceProxy srCategoryServiceProxy;

  @Mock
  SRHisRepository srHisRepository;

  @Test
  public void testPutResultFromVipa_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(null);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("FileTest");
    List<SRConfigDTO> vipaFileName = Mockito.spy(ArrayList.class);
    vipaFileName.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(vipaFileName);
    Calendar cal = Calendar.getInstance();
    ResultDTO result = srOutsideBusiness.putResultFromVipa("55", "ccc", "ggggg");
    Assert.assertEquals(resultDTO.getKey(), result.getKey());
  }

  @Test
  public void testPutResultFromVipa_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey(null);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("FileTest");
    List<SRConfigDTO> vipaFileName = Mockito.spy(ArrayList.class);
    vipaFileName.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(vipaFileName);
    Calendar cal = Calendar.getInstance();

    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    resultFileDataOld.setKey(RESULT.SUCCESS);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);

    ResultDTO result = srOutsideBusiness.putResultFromVipa("69", "OK", "ggggg");
    Assert.assertEquals(resultDTO.getKey(), result.getKey());
  }

  @Test
  public void deleteSRForOutside_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(srRepository.deleteSR(any())).thenReturn(resultInSideDto);
    String result = srOutsideBusiness.deleteSRForOutside(123L);
    Assert.assertEquals(resultInSideDto.getKey(), result);
  }

  @Test
  public void getCrNumberCreatedFromSR_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrId(1L);
    SRDTO srdto = Mockito.spy(SRDTO.class);
    srdto.setSrId("1");
    List<SrInsiteDTO> lstResult = Mockito.spy(ArrayList.class);
    lstResult.add(srInsiteDTO);
    PowerMockito.when(srRepository.getCrNumberCreatedFromSR(any(),anyInt(),anyInt(),anyBoolean())).thenReturn(lstResult);
    srOutsideBusiness.getCrNumberCreatedFromSR(srdto,1,1);
  }

  @Test
  public void getDetailSR_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrId(1L);
    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(srInsiteDTO);
    srOutsideBusiness.getDetailSR("1",999999L);
  }

  @Test
  public void getDetailSRById_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrId(1L);
    SRHisDTO srHisDTO = Mockito.spy(SRHisDTO.class);
    srHisDTO.setSrId("1");
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("abc");
    gnocFileDto.setBusinessCode("SR");
    gnocFileDto.setBusinessId(123L);
    List<GnocFileDto> lstFiles = Mockito.spy(ArrayList.class);
    lstFiles.add(gnocFileDto);
    List<SRHisDTO> lstResult = Mockito.spy(ArrayList.class);
    lstResult.add(srHisDTO);
    srInsiteDTO.setSrHisDTOList(lstResult);
    PowerMockito.when(srHisRepository.getListSRHisDTOBySrId(anyLong())).thenReturn(lstResult);
    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(srInsiteDTO);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFiles);
    srOutsideBusiness.getDetailSRById("1");
  }

  @Test
  public void getListGnocFileForSR_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("abc");
    gnocFileDto.setBusinessCode("SR");
    gnocFileDto.setBusinessId(123L);
    List<GnocFileDto> lstFiles = Mockito.spy(ArrayList.class);
    lstFiles.add(gnocFileDto);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(lstFiles);
    srOutsideBusiness.getListGnocFileForSR(gnocFileDto);
  }

  @Test
  public void updateSR_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey("SUCCESS");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRDTO srdto = Mockito.spy(SRDTO.class);
    PowerMockito.when(srBusiness.updateSR(any())).thenReturn(resultInSideDto);
    ResultDTO result = srOutsideBusiness.updateSR(srdto);
    Assert.assertEquals(resultDTO.getKey(), result.getKey());
  }

  @Test
  public void updateSRForIBPMSOutSide_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey("SUCCESS");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    SRDTO srdto = Mockito.spy(SRDTO.class);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setStatus("New");
    srdto.setSrId("123");
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srInsiteDTO);
    ResultDTO result = srOutsideBusiness.updateSRForIBPMSOutSide(srdto);
    Assert.assertEquals("FAIL", result.getKey());


    srInsiteDTO.setStatus("Rejected");
    srdto.setStatus("New");
    result = srOutsideBusiness.updateSRForIBPMSOutSide(srdto);
    Assert.assertEquals("FAIL", result.getKey());

    srdto.setServiceId("123");
    result = srOutsideBusiness.updateSRForIBPMSOutSide(srdto);
    Assert.assertEquals("FAIL", result.getKey());

    srdto.setSrUnit("123");
    result = srOutsideBusiness.updateSRForIBPMSOutSide(srdto);
    Assert.assertEquals("FAIL", result.getKey());

    srdto.setRoleCode("123");
    result = srOutsideBusiness.updateSRForIBPMSOutSide(srdto);
    Assert.assertEquals("FAIL", result.getKey());

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceArray("123");
    srCatalogDTO.setServiceGroup("123");
    srCatalogDTO.setServiceCode("123");
    srCatalogDTO.setExecutionUnit("123");

    srdto.setSrUnit("123");
    srdto.setServiceId("123");
    PowerMockito.when(srCatalogRepository2.findById(any())).thenReturn(srCatalogDTO);
    result = srOutsideBusiness.updateSRForIBPMSOutSide(srdto);
    Assert.assertEquals("FAIL", result.getKey());

    srdto.setServiceId(null);
    srdto.setServiceCode("123");
    List<SRCatalogDTO> lstCatalog = Mockito.spy(ArrayList.class);
    lstCatalog.add(srCatalogDTO);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstCatalog);
    result = srOutsideBusiness.updateSRForIBPMSOutSide(srdto);
    Assert.assertEquals("FAIL", result.getKey());


    lstCatalog.add(srCatalogDTO);
    SRRoleDTO dtoRole = Mockito.spy(SRRoleDTO.class);
    dtoRole.setRoleCode("456");
    srCatalogDTO.setRoleCode("456");
    List<SRRoleDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(dtoRole);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleDTO(any())).thenReturn(lstRole);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstCatalog);
    result = srOutsideBusiness.updateSRForIBPMSOutSide(srdto);
    Assert.assertEquals("FAIL", result.getKey());

    srCatalogDTO.setRoleCode("123");
    dtoRole.setRoleCode("123");
    lstRole.add(dtoRole);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleDTO(any())).thenReturn(lstRole);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstCatalog);
    result = srOutsideBusiness.updateSRForIBPMSOutSide(srdto);
    Assert.assertEquals("FAIL", result.getKey());

    srdto.setStartTime("07/12/2020 12:00:00");
    result = srOutsideBusiness.updateSRForIBPMSOutSide(srdto);
    Assert.assertEquals("FAIL", result.getKey());

    srdto.setSrUser("thanhlv12");
    result = srOutsideBusiness.updateSRForIBPMSOutSide(srdto);
    Assert.assertEquals("FAIL", result.getKey());
  }

  @Test
  public void insertSRWorklog_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setKey("SUCCESS");
    SRWorkLogDTO workLogDTO = Mockito.spy(SRWorkLogDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setId(1L);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(srWorkLogRepository.insertSRWorklog(any())).thenReturn(resultInSideDto);
    ResultDTO result = srOutsideBusiness.insertSRWorklog(workLogDTO);
    Assert.assertEquals(resultDTO.getKey(), result.getKey());
  }

  @Test
  public void testPutResultFromVipa_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO resultDTO = Mockito.spy(ResultDTO.class);
    resultDTO.setMessage("OK");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("FileTest");
    List<SRConfigDTO> vipaFileName = Mockito.spy(ArrayList.class);
    vipaFileName.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(vipaFileName);
    Calendar cal = Calendar.getInstance();

    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    resultFileDataOld.setKey(RESULT.SUCCESS);
    resultFileDataOld.setMessage("OK");
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultFileDataOld);

    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(96L);
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(usersEntity);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDtos.add(gnocFileDto);
    PowerMockito
        .when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(), anyLong(), anyList()))
        .thenReturn(resultFileDataOld);
    ResultDTO result = srOutsideBusiness.putResultFromVipa("69", "OK", "ggggg");
    Assert.assertEquals(resultDTO.getMessage(), result.getMessage());
  }

  @Test
  public void testPutResultFromVipa_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO result = srOutsideBusiness.putResultFromVipa(null, null, null);
    Assert.assertNotNull(result.getMessage());
  }

  @Test
  public void testCreateSRFromOtherSys_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(null);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);

    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("68");
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_05() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("1");
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_06() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("-1");
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_07() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");

    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_08() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("ntd");
    PowerMockito.when(I18n.getLanguage("sr.error.khaiBao")).thenReturn("Khai báo dịch vụ nền");
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_09() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("ntd");
    PowerMockito.when(I18n.getLanguage("sr.error.huy")).thenReturn("Hủy dịch vụ nền");
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_10() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("ntd");
    PowerMockito.when(I18n.getLanguage("sr.error.chan")).thenReturn("Chặn dịch vụ nền");
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_11() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("ntd");
    PowerMockito.when(I18n.getLanguage("sr.error.thayDoi")).thenReturn("Thay đổi dịch vụ nền");
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_12() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Thay đổi dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.thayDoi")).thenReturn("Thay đổi dịch vụ nền");
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_13() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Thay đổi dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.thayDoi")).thenReturn("Thay đổi dịch vụ nền");
    String s = "";
    for (int i = 0; i < 1005; i++) {
      s += "duong, ";
    }
    srCreatedFromOtherSysDTO.setContent(s);
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_14() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Thay đổi dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.thayDoi")).thenReturn("Thay đổi dịch vụ nền");
    srCreatedFromOtherSysDTO.setContent("duong");
    String customer = "";
    for (int i = 0; i <= 130; i++) {
      customer += "duongsilva, ";
    }
    srCreatedFromOtherSysDTO.setCustomer(customer);
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_15() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Thay đổi dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.thayDoi")).thenReturn("Thay đổi dịch vụ nền");
    srCreatedFromOtherSysDTO.setContent("duong");
    srCreatedFromOtherSysDTO.setCustomer("abc def");
    String address = "";
    for (int i = 0; i <= 505; i++) {
      address += "duongsilva, ";
    }
    srCreatedFromOtherSysDTO.setAddress(address);
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_16() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Thay đổi dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.thayDoi")).thenReturn("Thay đổi dịch vụ nền");
    srCreatedFromOtherSysDTO.setContent("duong");
    srCreatedFromOtherSysDTO.setCustomer("abc def");
    srCreatedFromOtherSysDTO.setAddress("duong.cbg");

    String accountSipTrunk = "";
    for (int i = 0; i <= 130; i++) {
      accountSipTrunk += "duongsilva, ";
    }
    srCreatedFromOtherSysDTO.setAccountSipTrunk(accountSipTrunk);
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_17() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Thay đổi dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.thayDoi")).thenReturn("Thay đổi dịch vụ nền");
    srCreatedFromOtherSysDTO.setContent("duong");
    srCreatedFromOtherSysDTO.setCustomer("abc def");
    srCreatedFromOtherSysDTO.setAddress("duong.cbg");
    srCreatedFromOtherSysDTO.setAccountSipTrunk("okijuyh");
    String ipPbx = "";
    for (int i = 0; i <= 130; i++) {
      ipPbx += "duongsilva, ";
    }
    srCreatedFromOtherSysDTO.setIpPbx(ipPbx);
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_18() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Thay đổi dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.thayDoi")).thenReturn("Thay đổi dịch vụ nền");
    srCreatedFromOtherSysDTO.setContent("duong");
    srCreatedFromOtherSysDTO.setCustomer("abc def");
    srCreatedFromOtherSysDTO.setAddress("duong.cbg");
    srCreatedFromOtherSysDTO.setAccountSipTrunk("okijuyh");
    srCreatedFromOtherSysDTO.setIpPbx("lololol");
    String ipProxy = "";
    for (int i = 0; i <= 130; i++) {
      ipProxy += "duongsilva, ";
    }
    srCreatedFromOtherSysDTO.setIpProxy(ipProxy);
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_19() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Thay đổi dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.thayDoi")).thenReturn("Thay đổi dịch vụ nền");
    srCreatedFromOtherSysDTO.setContent("duong");
    srCreatedFromOtherSysDTO.setCustomer("abc def");
    srCreatedFromOtherSysDTO.setAddress("duong.cbg");
    srCreatedFromOtherSysDTO.setAccountSipTrunk("okijuyh");
    srCreatedFromOtherSysDTO.setIpPbx("lololol");
    srCreatedFromOtherSysDTO.setIpProxy("veuveuveu");
    String subscribers = "";
    for (int i = 0; i <= 505; i++) {
      subscribers += "duongsilva, ";
    }
    srCreatedFromOtherSysDTO.setSubscribers(subscribers);
    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_20() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Khai báo dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.khaiBao")).thenReturn("Khai báo dịch vụ nền");
    srCreatedFromOtherSysDTO.setContent("duong");
    srCreatedFromOtherSysDTO.setCustomer("abc def");
    srCreatedFromOtherSysDTO.setAddress("duong.cbg");
    srCreatedFromOtherSysDTO.setAccountSipTrunk("okijuyh");
    srCreatedFromOtherSysDTO.setIpPbx("lololol");
    srCreatedFromOtherSysDTO.setIpProxy("veuveuveu");
    srCreatedFromOtherSysDTO.setSubscribers("fgfgfgfgfgfg");

    //Trang thai, quoc gia, tieu de, ngay tao, ngay gui SR
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    //ma SR
    List<String> lstSrId = Mockito.spy(ArrayList.class);
    String srId = "123";
    lstSrId.add(srId);
    PowerMockito.when(srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1)).thenReturn(lstSrId);

    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);

    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_21() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Hủy dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.huy")).thenReturn("Hủy dịch vụ nền");
    srCreatedFromOtherSysDTO.setContent("duong");
    srCreatedFromOtherSysDTO.setCustomer("abc def");
    srCreatedFromOtherSysDTO.setAddress("duong.cbg");
    srCreatedFromOtherSysDTO.setAccountSipTrunk("okijuyh");
    srCreatedFromOtherSysDTO.setIpPbx("lololol");
    srCreatedFromOtherSysDTO.setIpProxy("veuveuveu");
    srCreatedFromOtherSysDTO.setSubscribers("fgfgfgfgfgfg");

    //Trang thai, quoc gia, tieu de, ngay tao, ngay gui SR
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    //ma SR
    List<String> lstSrId = Mockito.spy(ArrayList.class);
    String srId = "123";
    lstSrId.add(srId);
    PowerMockito.when(srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1)).thenReturn(lstSrId);

    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);

    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_22() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Chặn dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.chan")).thenReturn("Chặn dịch vụ nền");
    srCreatedFromOtherSysDTO.setContent("duong");
    srCreatedFromOtherSysDTO.setCustomer("abc def");
    srCreatedFromOtherSysDTO.setAddress("duong.cbg");
    srCreatedFromOtherSysDTO.setAccountSipTrunk("okijuyh");
    srCreatedFromOtherSysDTO.setIpPbx("lololol");
    srCreatedFromOtherSysDTO.setIpProxy("veuveuveu");
    srCreatedFromOtherSysDTO.setSubscribers("fgfgfgfgfgfg");

    //Trang thai, quoc gia, tieu de, ngay tao, ngay gui SR
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    //ma SR
    List<String> lstSrId = Mockito.spy(ArrayList.class);
    String srId = "123";
    lstSrId.add(srId);
    PowerMockito.when(srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1)).thenReturn(lstSrId);

    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);

    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_23() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Thay đổi dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.thayDoi")).thenReturn("Thay đổi dịch vụ nền");
    srCreatedFromOtherSysDTO.setContent("duong");
    srCreatedFromOtherSysDTO.setCustomer("abc def");
    srCreatedFromOtherSysDTO.setAddress("duong.cbg");
    srCreatedFromOtherSysDTO.setAccountSipTrunk("okijuyh");
    srCreatedFromOtherSysDTO.setIpPbx("lololol");
    srCreatedFromOtherSysDTO.setIpProxy("veuveuveu");
    srCreatedFromOtherSysDTO.setSubscribers("fgfgfgfgfgfg");

    //Trang thai, quoc gia, tieu de, ngay tao, ngay gui SR
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    //ma SR
    List<String> lstSrId = Mockito.spy(ArrayList.class);
    String srId = "123";
    lstSrId.add(srId);
    PowerMockito.when(srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1)).thenReturn(lstSrId);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigName("jhjhjh");
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);

    List<SRCatalogDTO> lstCatalog = Mockito.spy(ArrayList.class);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstCatalog);

    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRFromOtherSys_24() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(com.viettel.security.PassTranformer.class);
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = Mockito.spy(SRCreatedFromOtherSysDTO.class);
    srCreatedFromOtherSysDTO.setSystem("1");
    srCreatedFromOtherSysDTO.setSubOrderId("6");
    srCreatedFromOtherSysDTO.setServiceName("Thay đổi dịch vụ nền");
    PowerMockito.when(I18n.getLanguage("sr.error.thayDoi")).thenReturn("Thay đổi dịch vụ nền");
    srCreatedFromOtherSysDTO.setContent("duong");
    srCreatedFromOtherSysDTO.setCustomer("abc def");
    srCreatedFromOtherSysDTO.setAddress("duong.cbg");
    srCreatedFromOtherSysDTO.setAccountSipTrunk("okijuyh");
    srCreatedFromOtherSysDTO.setIpPbx("lololol");
    srCreatedFromOtherSysDTO.setIpProxy("veuveuveu");
    srCreatedFromOtherSysDTO.setSubscribers("fgfgfgfgfgfg");

    //Trang thai, quoc gia, tieu de, ngay tao, ngay gui SR
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    //ma SR
    List<String> lstSrId = Mockito.spy(ArrayList.class);
    String srId = "123";
    lstSrId.add(srId);
    PowerMockito.when(srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1)).thenReturn(lstSrId);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigName("jhjhjh");
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(srConfigDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceArray("1");
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setServiceId(1L);
    srCatalogDTO.setExecutionUnit("1");
    srCatalogDTO.setExecutionTime("1");
    srCatalogDTO.setIsAddDay(1L);

    List<SRCatalogDTO> lstCatalog = Mockito.spy(ArrayList.class);
    lstCatalog.add(srCatalogDTO);

    ResultInSideDto insertSR = Mockito.spy(ResultInSideDto.class);
    insertSR.setKey("SUCCESS");

    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    ResultInSideDto resOtherSys = Mockito.spy(ResultInSideDto.class);
    resOtherSys.setKey("FAIL");
    resOtherSys.setId(1L);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resOtherSys);
    PowerMockito.when(srCreatedFromOtherSysRepository.insertSRCreateFromOtherSys(any())).thenReturn(resOtherSys);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(userRepository.getUserByUserName(any())).thenReturn(usersEntity);
    PowerMockito.when(srBusiness.insertSR(any())).thenReturn(insertSR);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstCatalog);

    ResultDTO result = srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
    Assert.assertNotNull(result.getKey(), res.getKey());
  }

  @Test
  public void testCreateSRByConfigGroup_01() throws Exception{
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SrOutsideBusinessImpl srCatalogBusinessInline = Mockito.spy(srOutsideBusiness);
    ResultDTO result = Mockito.spy(ResultDTO.class);
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey("0");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    SRDTO srInputDTO = Mockito.spy(SRDTO.class);
    mockFileUtilsSaveFile();
    srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, null);

    srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "abc");

    srInputDTO.setServiceCode("12");
    srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "abc");

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    List<SRCatalogDTO> lstSrCatalog = Mockito.spy(ArrayList.class);
    srCatalogDTO.setServiceCode("123");
    lstSrCatalog.add(srCatalogDTO);
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "abc");
    Assert.assertEquals(result.getKey(), res.getKey());

    srCatalogDTO.setServiceCode("123");
    lstSrCatalog.add(srCatalogDTO);
    PowerMockito.doReturn(lstSrCatalog).when(srCatalogBusinessInline).getListSRCatalogByConfigGroup(anyString());
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "abc");
    Assert.assertEquals(result.getKey(), res.getKey());

    srCatalogDTO.setServiceCode("12");
    srInputDTO.setExecutionUnit("A");
    lstSrCatalog.add(srCatalogDTO);
    PowerMockito.doReturn(lstSrCatalog).when(srCatalogBusinessInline).getListSRCatalogByConfigGroup(anyString());
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "abc");
    Assert.assertEquals(result.getKey(), res.getKey());

    srCatalogDTO.setServiceCode("12");
    lstSrCatalog.add(srCatalogDTO);
    PowerMockito.doReturn(lstSrCatalog).when(srCatalogBusinessInline).getListSRCatalogByConfigGroup(anyString());
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstSrCatalog);
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "DICH_VU_VSMART");
    Assert.assertEquals(result.getKey(), res.getKey());

    srInputDTO.setExecutionUnit("12");
    srCatalogDTO.setServiceCode("12");
    srCatalogDTO.setExecutionUnit("1");
    lstSrCatalog.add(srCatalogDTO);
    PowerMockito.doReturn(lstSrCatalog).when(srCatalogBusinessInline).getListSRCatalogByConfigGroup(anyString());
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstSrCatalog);
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "DICH_VU_VSMART");
    Assert.assertEquals(result.getKey(), res.getKey());

    srInputDTO.setExecutionUnit("1");
    srCatalogDTO.setServiceCode("12");
    srCatalogDTO.setExecutionUnit("1");
    lstSrCatalog.add(srCatalogDTO);
    PowerMockito.doReturn(lstSrCatalog).when(srCatalogBusinessInline).getListSRCatalogByConfigGroup(anyString());
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstSrCatalog);
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "DICH_VU_VSMART");
    Assert.assertEquals(result.getKey(), res.getKey());

    srCatalogDTO.setRoleCode("123");
    srCatalogDTO.setCountry("281");
    srCatalogDTO.setServiceId(1L);
    srCatalogDTO.setServiceArray("123");
    srCatalogDTO.setServiceGroup("123");
    srCatalogDTO.setExecutionTime("123");
    lstSrCatalog.add(srCatalogDTO);
    PowerMockito.doReturn(lstSrCatalog).when(srCatalogBusinessInline).getListSRCatalogByConfigGroup(anyString());
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstSrCatalog);
    UnitDTO dtoU = Mockito.spy(UnitDTO.class);
    dtoU.setUnitId(1L);
    List<UnitDTO> lstUnit = Mockito.spy(ArrayList.class);
    lstUnit.add(dtoU);
    PowerMockito.when(unitRepository.getListUnitDTO(any(),anyInt(),anyInt(),anyString(),anyString())).thenReturn(lstUnit);
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "DICH_VU_VSMART");
    Assert.assertEquals(result.getKey(), res.getKey());

    srInputDTO.setRoleCode("123");
    PowerMockito.when(unitRepository.getListUnitDTO(any(),anyInt(),anyInt(),anyString(),anyString())).thenReturn(lstUnit);
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "123");
    Assert.assertEquals(result.getKey(), res.getKey());

    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    srRoleDTO.setRoleId(1L);
    List<SRRoleDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(srRoleDTO);
    PowerMockito.when(unitRepository.getListUnitDTO(any(),anyInt(),anyInt(),anyString(),anyString())).thenReturn(lstUnit);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleDTO(any())).thenReturn(lstRole);
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "123");
    Assert.assertEquals(result.getKey(), res.getKey());

    srCatalogDTO.setRoleCode("123");
    lstSrCatalog.add(srCatalogDTO);
    srInputDTO.setCountry("282");
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstSrCatalog);
    PowerMockito.when(unitRepository.getListUnitDTO(any(),anyInt(),anyInt(),anyString(),anyString())).thenReturn(lstUnit);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleDTO(any())).thenReturn(lstRole);
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "123");
    Assert.assertEquals(result.getKey(), res.getKey());

    CatLocationDTO catLocation = Mockito.spy(CatLocationDTO.class);
    catLocation.setLocationCode("VN");
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstSrCatalog);
    PowerMockito.when(unitRepository.getListUnitDTO(any(),anyInt(),anyInt(),anyString(),anyString())).thenReturn(lstUnit);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleDTO(any())).thenReturn(lstRole);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(catLocation);
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "DICH_VU_VSMART");
    Assert.assertEquals(result.getKey(), res.getKey());

    srInputDTO.setTitle("123");
    srInputDTO.setDescription("123");
    srInputDTO.setCreatedUser("thanhlv12");
    UsersDTO usersDTO = Mockito.spy(UsersDTO.class);
    usersDTO.setUserId("999999");
    List<String> lstSrId = Mockito.spy(ArrayList.class);
    lstSrId.add("123");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    resultInSideDto.setMessage("SUCCESS");
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrId(123L);

    CatLocationDTO catLocation1 = Mockito.spy(CatLocationDTO.class);
    catLocation1.setLocationCode("281");
    srInputDTO.setCountry("281");
    srInputDTO.setCreatedUser("thanhlv12");
    UsersEntity usersEntity = Mockito.spy(UsersEntity.class);
    usersEntity.setUnitId(1L);
    usersEntity.setUsername("thanhlv12");
    srInsiteDTO.setCreatedUser("thanhlv12");
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    unitToken.setUnitName("CHAN");
    SRFilesDTO srFilesDTO = Mockito.spy(SRFilesDTO.class);
    srFilesDTO.setFileContent("123");
    srFilesDTO.setFileName("123");
    List<SRFilesDTO> lstFile = Mockito.spy(ArrayList.class);
    lstFile.add(srFilesDTO);
    srInputDTO.setLstFile(lstFile);
    resultInSideDto.setId(123L);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setPath("abc");
    gnocFileDto.setBusinessCode("SR");
    gnocFileDto.setBusinessId(123L);
    resultInSideDto.setKey("SUCCESS");
    resultInSideDto.setMessage("SUCCESS");
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstSrCatalog);
    PowerMockito.when(unitRepository.getListUnitDTO(any(),anyInt(),anyInt(),anyString(),anyString())).thenReturn(lstUnit);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleDTO(any())).thenReturn(lstRole);
    PowerMockito.when(catLocationRepository.getNationByLocationId(anyLong())).thenReturn(catLocation1);
    PowerMockito.when(srCatalogBusinessInline.checkUserByUserCodeOrName(anyString(),anyString())).thenReturn(usersDTO);
    PowerMockito.when(srRepository.getListSequenseSR(anyString(),anyInt())).thenReturn(lstSrId);
    PowerMockito.when(srBusiness.insertSR(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srBusiness.setStartTimeAndEndTimeSR(any(),anyString(),anyString(),anyString())).thenReturn(srInsiteDTO);
    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(srInsiteDTO);
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(usersEntity);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitToken);
    PowerMockito.when(srRepository.addSRFile(any())).thenReturn(resultInSideDto);
    PowerMockito.when(gnocFileRepository.saveListGnocFileNotDeleteAll(anyString(),anyLong(),anyList())).thenReturn(resultInSideDto);
    result = srCatalogBusinessInline.createSRByConfigGroup(srInputDTO, "123");
    resultInSideDto.setKey("0");
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());

  }
  @Test
  public void testCheckUserByUserCodeOrName_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UsersInsideDto user = Mockito.spy(UsersInsideDto.class);
    user.setUserId(69L);
    user.setUsername("thanhlv12");
    user.setIsEnable(1L);
    PowerMockito.when(userRepository.getUserByStaffCode(anyString())).thenReturn(user);
    UsersDTO userFinal = srOutsideBusiness.checkUserByUserCodeOrName("thanhlv12", "DICH_VU_ADD_ON");
    Assert.assertNotNull(userFinal);
  }

  @Test
  public void testCheckUserByUserCodeOrName_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    UsersEntity user = Mockito.spy(UsersEntity.class);
    user.setUnitId(69L);
    user.setIsEnable(1L);
    user.setUserId(26L);
    user.setUsername("thanhlv12");
    UsersDTO userFinallll = Mockito.spy(UsersDTO.class);
    userFinallll.setUsername("thanhlv12");
    userFinallll.setUserId("26");
    userFinallll.setUnitId("69");
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(user);
    UsersDTO userFinal = srOutsideBusiness.checkUserByUserCodeOrName("thanhlv12", "abc");
    Assert.assertEquals(userFinal.getUnitId(), userFinallll.getUnitId());
  }

  @Test
  public void testGetListSRByConfigGroup_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SRDTO> lstSr = Mockito.spy(ArrayList.class);
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setCreatedUser("thanhlv12");
    UsersDTO userFinal = Mockito.spy(UsersDTO.class);
    UsersInsideDto user = Mockito.spy(UsersInsideDto.class);
    user.setUserId(69L);
    user.setUsername("thanhlv12");
    user.setIsEnable(1L);
    userFinal = user.toOutSideDto();
    lstSr.add(dto);
    PowerMockito.when(srOutsideRepository.getListSRByConfigGroup(any(), anyString()))
        .thenReturn(lstSr);
    PowerMockito.when(userRepository.getUserByStaffCode(anyString())).thenReturn(user);
    List<SRDTO> srdtoList = srOutsideBusiness.getListSRByConfigGroup(dto, "DICH_VU_ADD_ON");
    Assert.assertEquals(lstSr.size(), srdtoList.size());
  }

  @Test
  public void testGetListSRByConfigGroup_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SRDTO> lstSr = Mockito.spy(ArrayList.class);
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setCreatedUser("thanhlv12");
    UsersDTO userFinal = Mockito.spy(UsersDTO.class);
    UsersEntity user = Mockito.spy(UsersEntity.class);
    user.setUnitId(26L);
    user.setUserId(69L);
    user.setUsername("thanhlv12");
    user.setIsEnable(1L);
    userFinal.setUserId("69");
    userFinal.setUsername("thanhlv12");
    userFinal.setUnitId("26");
    lstSr.add(dto);
    PowerMockito.when(srOutsideRepository.getListSRByConfigGroup(any(), anyString()))
        .thenReturn(lstSr);
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(user);
    List<SRDTO> srdtoList = srOutsideBusiness.getListSRByConfigGroup(dto, "ABC");
    Assert.assertEquals(lstSr.size(), srdtoList.size());
  }

  @Test
  public void testGetListSRByConfigGroup_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SRDTO> lstSr = Mockito.spy(ArrayList.class);
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setCreatedUser("thanhlv12");
    UsersDTO userFinal = Mockito.spy(UsersDTO.class);
    UsersEntity user = Mockito.spy(UsersEntity.class);

    lstSr.add(dto);
    PowerMockito.when(srOutsideRepository.getListSRByConfigGroup(any(), anyString()))
        .thenReturn(lstSr);
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(user);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("sr.error.createdUserIncorrect"))
        .thenReturn("Người tạo không tồn tại trong hệ thống");
    List<SRDTO> srdtoList = srOutsideBusiness.getListSRByConfigGroup(dto, "ABC");
    Assert.assertEquals(lstSr.size(), srdtoList.size());
  }

  @Test
  public void testGetListSRCatalogByConfigGroup_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SRCatalogDTO> lstSrCatalog = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
//    srOutsideBusiness.getListSRCatalogByConfigGroup(null, null);
    srOutsideBusiness.getListSRCatalogByConfigGroup(null);
    Assert.assertNotNull(lstSrCatalog);
  }

  @Test
  public void testGetListSRForLinkCR_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SRDTO> lst = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");
    srOutsideBusiness.getListSRForLinkCR(null, null);
    Assert.assertNotNull(lst);
  }

  @Test
  public void testGetListSRForLinkCR_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SRDTO> lst = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    UsersDTO u = Mockito.spy(UsersDTO.class);
    u.setUsername("thanhlv12");
    u.setUserId("66");
    u.setUnitId("69");

    UsersEntity user = Mockito.spy(UsersEntity.class);
    user.setUnitId(69L);
    user.setIsEnable(1L);
    user.setUsername("thanhlv12");
    user.setUserId(66L);
    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(user);

    PowerMockito.when(srRepository.getListSRForLinkCR(anyString(), anyString())).thenReturn(lst);

    srOutsideBusiness.getListSRForLinkCR("thanhlv12", null);
    Assert.assertNotNull(lst);
  }

  @Test
  public void testGetListSRForLinkCR_03() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SRDTO> lst = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    UsersDTO u = Mockito.spy(UsersDTO.class);
    u.setUsername("thanhlv12");
    u.setUserId("66");
    u.setUnitId("69");

    UsersEntity user = Mockito.spy(UsersEntity.class);
    user.setUnitId(69L);
    user.setIsEnable(1L);
    user.setUsername("thanhlv12");
    user.setUserId(66L);

    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(user);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(srInsiteDTO);
    PowerMockito.when(srRepository.getListSRForLinkCR(anyString(), anyString())).thenReturn(lst);

    srOutsideBusiness.getListSRForLinkCR("thanhlv12", "78");
    Assert.assertNotNull(lst);
  }

  @Test
  public void testGetListSRForLinkCR_04() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SRDTO> lst = Mockito.spy(ArrayList.class);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("xxx");

    UsersDTO u = Mockito.spy(UsersDTO.class);
    u.setUsername("thanhlv12");
    u.setUserId("66");
    u.setUnitId("69");

    UsersEntity user = Mockito.spy(UsersEntity.class);
    user.setUnitId(69L);
    user.setIsEnable(1L);
    user.setUsername("thanhlv12");
    user.setUserId(66L);

    PowerMockito.when(userRepository.getUserByUserName(anyString())).thenReturn(user);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(srInsiteDTO);
    SRDTO srdto = Mockito.spy(SRDTO.class);
    srdto.setStatus("1");
    lst.add(srdto);
    PowerMockito.when(srRepository.getListSRForLinkCR(anyString(), anyString())).thenReturn(lst);
    List<SRDTO> srdtoList = srOutsideBusiness.getListSRForLinkCR("thanhlv12", "78");
    Assert.assertEquals(srdtoList.size(), lst.size());
  }

  @Test
  public void testGetListSR_01() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRDTO dto = Mockito.spy(SRDTO.class);
    List<SRDTO> lst = Mockito.spy(ArrayList.class);
    SrInsiteDTO srInsiteDTO = dto.toInsideDTO();
    PowerMockito.when(srRepository.getListSRForOutside(srInsiteDTO)).thenReturn(lst);
    List<SRDTO> srdtoList = srOutsideBusiness.getListSR(dto, 123, 321);
    Assert.assertEquals(srdtoList.size(), 0L);
  }

  @Test
  public void testGetListSR_02() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setStartTime("2020-04-27 01:01:01");
    dto.setEndTime("2020-04-27 01:01:01");
    dto.setCreatedTime("2020-04-27 01:01:01");
    dto.setUpdatedTime("2020-04-27 01:01:01");
    dto.setSendDate("2020-04-27 01:01:01");

    SRDTO dtoInput = Mockito.spy(SRDTO.class);
    List<SRDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(dto);
    PowerMockito.when(srRepository.getListSRForOutside(any())).thenReturn(lst);
    List<SRDTO> srdtoList = srOutsideBusiness.getListSR(dtoInput, 123, 321);
    Assert.assertEquals(srdtoList.size(), 1L);
  }

  @Test
  public void testGetByConfigGroup_01(){
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRConfigDTO dto = Mockito.spy(SRConfigDTO.class);
    List<SRConfigDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(dto);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lst);
    srOutsideBusiness.getByConfigGroup("123");
  }

  @Test
  public void testGetListSRCatalogByConfigGroupIBPMS_01(){
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRCatalogDTO dto = Mockito.spy(SRCatalogDTO.class);
    List<SRCatalogDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(dto);
    PowerMockito.when(srOutsideRepository.getListSRCatalogByConfigGroupIBPMS(anyString())).thenReturn(lst);
    srOutsideBusiness.getListSRCatalogByConfigGroupIBPMS("123");
  }

  public void mockFileUtilsSaveFile() throws Exception{
    byte []bytes = new byte[] {1,2,3};
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(PassTranformer.class);
    ExcelWriterUtils excelWriterUtils = Mockito.spy(ExcelWriterUtils.class);
    Workbook workBook = Mockito.spy(Workbook.class);
    Sheet sheet = Mockito.spy(Sheet.class);
    CellStyle cellSt1 =Mockito.spy(CellStyle.class);
    PowerMockito.when(PassTranformer.decrypt(anyString())).thenReturn("decrypt");
    PowerMockito.when(FileUtils.saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(), any(), any())).thenReturn("/path/junit");
    PowerMockito.when(FileUtils.saveUploadFile(anyString(), any(), anyString(), any())).thenReturn("/path/upload");
    PowerMockito.when(FileUtils.getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString())).thenReturn(bytes);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/path/temp");
    PowerMockito.when(FileUtils.getFileName(any())).thenReturn("chanquama");
//    PowerMockito.when(workBook.getSheetAt(any())).thenReturn(sheet);
    PowerMockito.when(excelWriterUtils.readFileExcel(any())).thenReturn(workBook);
  }
}
