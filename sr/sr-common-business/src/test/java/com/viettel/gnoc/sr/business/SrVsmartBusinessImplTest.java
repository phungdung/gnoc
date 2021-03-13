package com.viettel.gnoc.sr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.SrCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SREvaluateDTO;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.dto.UnitSRCatalogDTO;
import com.viettel.gnoc.sr.repository.SRCatalogRepository2;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SREvaluateRepository;
import com.viettel.gnoc.sr.repository.SROutsideRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import com.viettel.security.PassTranformer;
import com.viettel.vmsa.ResultCreateDtByFileInput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({SrVsmartBusinessImplTest.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, ObjectMapper.class, Workbook.class,
    ResultCreateDtByFileInput.class, ExcelWriterUtils.class, CommonExport.class,
    DateUtil.class, DataUtil.class, PassTranformer.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class SrVsmartBusinessImplTest {

  @InjectMocks
  SrVsmartBusinessImpl srVsmartBusiness;
  @Mock
  SRConfigRepository srConfigRepository;

  @Mock
  SrOutsideBusiness srOutsideBusiness;

  @Mock
  SrBusiness srBusiness;

  @Mock
  SrRepository srRepository;

  @Mock
  SROutsideRepository srOutsideRepository;

  @Mock
  SrCategoryServiceProxy srCategoryServiceProxy;

  @Mock
  SRCatalogRepository2 srCatalogRepository2;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  SrNocProBusiness srNocProBusiness;

  @Mock
  SREvaluateRepository srEvaluateRepository;

  @Test
  public void getDetailSRForVSmart_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    srVsmartBusiness.getDetailSRForVSmart("1", null);
  }

  @Test
  public void getDetailSRForVSmart_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    srVsmartBusiness.getDetailSRForVSmart("1", "1");
  }

  @Test
  public void getDetailSRForVSmart_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UsersDTO user = Mockito.spy(UsersDTO.class);
    PowerMockito.when(srOutsideBusiness.checkUserByUserCodeOrName(anyString(), anyString()))
        .thenReturn(user);
    srVsmartBusiness.getDetailSRForVSmart(null, "1");
  }

  @Test
  public void getDetailSRForVSmart_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UsersDTO user = Mockito.spy(UsersDTO.class);
    PowerMockito.when(srOutsideBusiness.checkUserByUserCodeOrName(anyString(), anyString()))
        .thenReturn(user);
    srVsmartBusiness.getDetailSRForVSmart("a", "1");
  }

  @Test
  public void getDetailSRForVSmart_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UsersDTO user = Mockito.spy(UsersDTO.class);
    PowerMockito.when(srOutsideBusiness.checkUserByUserCodeOrName(anyString(), anyString()))
        .thenReturn(user);
    srVsmartBusiness.getDetailSRForVSmart("1", "1");
  }

  @Test
  public void getDetailSRForVSmart_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UsersDTO user = Mockito.spy(UsersDTO.class);
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setCreatedUser("1");
    dto.setStatus("Concluded");
    PowerMockito.when(srOutsideRepository.getDetailSRForVSmart(anyString(), anyString()))
        .thenReturn(dto);
    PowerMockito.when(srOutsideBusiness.checkUserByUserCodeOrName(anyString(), anyString()))
        .thenReturn(user);
    srVsmartBusiness.getDetailSRForVSmart("1", "1");
  }

  @Test
  public void getDetailSRForVSmart_07() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UsersDTO user = Mockito.spy(UsersDTO.class);
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setCreatedUser("1");
    dto.setStatus("Closed");
    PowerMockito.when(srOutsideRepository.getDetailSRForVSmart(anyString(), anyString()))
        .thenReturn(dto);
    PowerMockito.when(srOutsideBusiness.checkUserByUserCodeOrName(anyString(), anyString()))
        .thenReturn(user);
    srVsmartBusiness.getDetailSRForVSmart("1", "1");
  }

  @Test
  public void getDetailSRForVSmart_08() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UsersDTO user = Mockito.spy(UsersDTO.class);
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setCreatedUser("1");
    dto.setStatus("Rejected");
    PowerMockito.when(srOutsideRepository.getDetailSRForVSmart(anyString(), anyString()))
        .thenReturn(dto);
    PowerMockito.when(srOutsideBusiness.checkUserByUserCodeOrName(anyString(), anyString()))
        .thenReturn(user);
    srVsmartBusiness.getDetailSRForVSmart("1", "1");
  }

  @Test
  public void getDetailSRForVSmart_09() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UsersDTO user = Mockito.spy(UsersDTO.class);
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setCreatedUser("1");
    dto.setStatus("Draft");
    PowerMockito.when(srOutsideRepository.getDetailSRForVSmart(anyString(), anyString()))
        .thenReturn(dto);
    PowerMockito.when(srOutsideBusiness.checkUserByUserCodeOrName(anyString(), anyString()))
        .thenReturn(user);
    srVsmartBusiness.getDetailSRForVSmart("1", "1");
  }

  @Test
  public void getDetailSRForVSmart_10() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UsersDTO user = Mockito.spy(UsersDTO.class);
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setCreatedUser("1");
    dto.setStatus("Draftf");
    dto.setSrUser("1");
    PowerMockito.when(srOutsideRepository.getDetailSRForVSmart(anyString(), anyString()))
        .thenReturn(dto);
    PowerMockito.when(srOutsideBusiness.checkUserByUserCodeOrName(anyString(), anyString()))
        .thenReturn(user);
    srVsmartBusiness.getDetailSRForVSmart("1", "1");
  }

  @Test
  public void getDetailSRForVSmart_11() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UsersDTO user = Mockito.spy(UsersDTO.class);
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setCreatedUser("1");
    dto.setStatus("Draftf");
//    dto.setSrUser("1");
    SRDTO dtoTmp = Mockito.spy(SRDTO.class);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setStartTime(new Date());
    srInsiteDTO.setEndTime(new Date());
    srInsiteDTO.setCreatedTime(new Date());
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSendDate(new Date());
    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(srInsiteDTO);
    PowerMockito.when(srOutsideRepository.getDetailSRForVSmart(anyString(), anyString()))
        .thenReturn(dto);
    PowerMockito.when(srOutsideBusiness.checkUserByUserCodeOrName(anyString(), anyString()))
        .thenReturn(user);
    srVsmartBusiness.getDetailSRForVSmart("1", "1");
  }

  @Test
  public void getDetailSRForVSmart_12() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    UsersDTO user = Mockito.spy(UsersDTO.class);
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setCreatedUser("1");
    dto.setStatus("Draftf");
//    dto.setSrUser("1");
    SRDTO dtoTmp = Mockito.spy(SRDTO.class);
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setSrUnit(1L);
    srInsiteDTO.setRoleCode("1");
    srInsiteDTO.setCountry("1");
    srInsiteDTO.setSrId(1L);
    srInsiteDTO.setStartTime(new Date());
    srInsiteDTO.setEndTime(new Date());
    srInsiteDTO.setCreatedTime(new Date());
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setSendDate(new Date());
    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setUsername("1");
    List<SRRoleUserInSideDTO> lstSrUser = Mockito.spy(ArrayList.class);
    lstSrUser.add(srRoleUserInSideDTO);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lstSrUser);
    PowerMockito.when(srRepository.getDetailNoOffset(anyLong())).thenReturn(srInsiteDTO);
    PowerMockito.when(srOutsideRepository.getDetailSRForVSmart(anyString(), anyString()))
        .thenReturn(dto);
    PowerMockito.when(srOutsideBusiness.checkUserByUserCodeOrName(anyString(), anyString()))
        .thenReturn(user);
    srVsmartBusiness.getDetailSRForVSmart("1", "1");
  }

  @Test
  public void updateSRForVSmart_01() {

    mockI18n();
    SRDTO srInputDTO = Mockito.spy(SRDTO.class);
    srInputDTO.setStationCode("VSMART");
    srInputDTO.setInsertSource("VSMART");
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey("1");
    PowerMockito.when(srOutsideBusiness.createSRByConfigGroup(any(), anyString())).thenReturn(res);
    ResultDTO resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    res.setKey("0");
    srInputDTO.setSrCode("SR");
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    srInputDTO.setSrCode("SR_123");
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    SrInsiteDTO srTmp = Mockito.spy(SrInsiteDTO.class);
    srTmp.setSrId(123L);
    srTmp.setFlowExecuteId(123L);
    srTmp.setFlowExecute("123");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("123");
    srCatalogDTO.setServiceId(123L);
    srCatalogDTO.setFlowExecute(123L);
    List<SRCatalogDTO> lstVSmart = Mockito.spy(ArrayList.class);
    lstVSmart.add(srCatalogDTO);
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srTmp);
    PowerMockito.when(srOutsideBusiness.getListSRCatalogByConfigGroup(any())).thenReturn(lstVSmart);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(lstVSmart);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    srTmp.setServiceId("123");
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srTmp);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    srInputDTO.setStatus("Closed");
    srTmp.setStatus("Closed");
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srTmp);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    srInputDTO.setUpdatedUser("thanhlv12");
    UsersEntity userTKG = Mockito.spy(UsersEntity.class);
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srTmp);
    PowerMockito.when(userRepository.getUserByUserName(any())).thenReturn(userTKG);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    userTKG.setUsername("thanhlv12");
    PowerMockito.when(userRepository.getUserByUserName(any())).thenReturn(userTKG);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    srTmp.setStatus("New");
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srTmp);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    srInputDTO.setReviewId("7");
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srTmp);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    srInputDTO.setReviewId("1");
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srTmp);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    srTmp.setCreatedUser("thanhlv12");
    srTmp.setSrId(123L);
    srInputDTO.setEvaluateReason("123");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(123L);
    res.setKey("1");
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srTmp);
    PowerMockito.when(srEvaluateRepository.insertSREvaluate(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srBusiness.updateSR(any())).thenReturn(resultInSideDto);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    srInputDTO.setReCreateSR("1");
    srTmp.setCountry("281");
    SRRoleActionDTO srRoleActionDTO = Mockito.spy(SRRoleActionDTO.class);
    srRoleActionDTO.setCountry("281");
    List<SRRoleActionDTO> lstDataUpdateSR = Mockito.spy(ArrayList.class);
    lstDataUpdateSR.add(srRoleActionDTO);
    PowerMockito.when(srCategoryServiceProxy.getListSRRoleActionDTO(any())).thenReturn(lstDataUpdateSR);
    PowerMockito.when(srRepository.getDetailNoOffset(any())).thenReturn(srTmp);
    PowerMockito.when(srEvaluateRepository.insertSREvaluate(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srBusiness.updateSR(any())).thenReturn(resultInSideDto);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    res.setKey("0");
    srInputDTO.setStatus("");
    srInputDTO.setSrUser("thanhlv12");
    userTKG.setUsername("");
    PowerMockito.when(userRepository.getUserByUserName(any())).thenReturn(userTKG);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    userTKG.setUsername("thaoltk");
    PowerMockito.when(userRepository.getUserByUserName(any())).thenReturn(userTKG);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    userTKG.setUsername("thanhlv12");
    srTmp.setSrUnit(123L);
    List<SRRoleUserInSideDTO> lstUser = Mockito.spy(ArrayList.class);
    PowerMockito.when(userRepository.getUserByUserName(any())).thenReturn(userTKG);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lstUser);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());

    res.setKey("1");
    userTKG.setUsername("thanhlv12");
    srTmp.setSrUnit(123L);
    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setRoleCode("123");
    lstUser.add(srRoleUserInSideDTO);
    PowerMockito.when(userRepository.getUserByUserName(any())).thenReturn(userTKG);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lstUser);
    PowerMockito.when(srBusiness.updateSR(any())).thenReturn(resultInSideDto);
    resultDTO = srVsmartBusiness.updateSRForVSmart(srInputDTO);
    Assert.assertEquals(resultDTO.getKey(), res.getKey());
  }

  public void mockI18n() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getChangeManagement(anyString())).thenReturn("some text...");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("some text...");
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("some text...");
  }

  public void mockFileUtilsSaveFile() throws Exception {
    byte[] bytes = new byte[]{1, 2, 3};
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(PassTranformer.class);
    ExcelWriterUtils excelWriterUtils = Mockito.spy(ExcelWriterUtils.class);
    Workbook workBook = Mockito.spy(Workbook.class);
    Sheet sheet = Mockito.spy(Sheet.class);
    CellStyle cellSt1 = Mockito.spy(CellStyle.class);
    PowerMockito.when(PassTranformer.decrypt(anyString())).thenReturn("decrypt");
    PowerMockito.when(FileUtils
        .saveFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(),
            any(), any())).thenReturn("/path/junit");
    PowerMockito.when(FileUtils.saveUploadFile(anyString(), any(), anyString(), any()))
        .thenReturn("/path/upload");
    PowerMockito
        .when(FileUtils.getFtpFile(anyString(), anyInt(), anyString(), anyString(), anyString()))
        .thenReturn(bytes);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn("/path/temp");
    PowerMockito.when(FileUtils.getFileName(any())).thenReturn("chanquama");
//    PowerMockito.when(workBook.getSheetAt(any())).thenReturn(sheet);
    PowerMockito.when(excelWriterUtils.readFileExcel(any())).thenReturn(workBook);
  }

  @Test
  public void getListSRStatusForVSmart_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setStatus("A");
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    List<SRConfigDTO> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstStatus);
    srVsmartBusiness.getListSRStatusForVSmart("1");
  }

  @Test
  public void getListSRStatusForVSmart_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setStatus("A");
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    List<SRConfigDTO> lstStatus = Mockito.spy(ArrayList.class);
    srVsmartBusiness.getListSRStatusForVSmart("1");
  }

  @Test
  public void getListSRForVSmart_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRDTO dto = Mockito.spy(SRDTO.class);
    dto.setLoginUser("1");
    UsersDTO user = Mockito.spy(UsersDTO.class);
    PowerMockito.when(srOutsideBusiness.checkUserByUserCodeOrName(anyString(), anyString()))
        .thenReturn(user);
    srVsmartBusiness.getListSRForVSmart(dto);
  }

  @Test
  public void getListSRCatalogForVSmart_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRCatalogDTO dto = Mockito.spy(SRCatalogDTO.class);
    List<SRCatalogDTO> lstResult = Mockito.spy(ArrayList.class);
    PowerMockito
        .when(srOutsideRepository.getListSRCatalogByConfigGroupVSMAT(anyString(), anyString()))
        .thenReturn(lstResult);
    srVsmartBusiness.getListSRCatalogForVSmart("123", "123");
  }

  @Test
  public void getListSRCatalogForVSmart_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRCatalogDTO dto = Mockito.spy(SRCatalogDTO.class);
    dto.setServiceId(123L);
    List<SRCatalogDTO> lstResult = Mockito.spy(ArrayList.class);
    lstResult.add(dto);
    PowerMockito
        .when(srOutsideRepository.getListSRCatalogByConfigGroupVSMAT(anyString(), anyString()))
        .thenReturn(lstResult);
    srVsmartBusiness.getListSRCatalogForVSmart("123", "123");
  }

  @Test
  public void getListServiceGrouprForVsmart_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRConfigDTO dto = Mockito.spy(SRConfigDTO.class);
    dto.setConfigGroup("123");
    List<SRConfigDTO> lstResult = Mockito.spy(ArrayList.class);
    lstResult.add(dto);
    PowerMockito.when(srOutsideRepository.getListServiceGrouprForVsmart()).thenReturn(lstResult);
    srVsmartBusiness.getListServiceGrouprForVsmart();
  }

  @Test
  public void getSRReviewForVSmart_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigName("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> lstOld = Mockito.spy(ArrayList.class);
    lstOld.add(srConfigDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstOld);
    srVsmartBusiness.getSRReviewForVSmart("1");
  }

  @Test
  public void getSRReviewForVSmart_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<SRConfigDTO> lstOld = Mockito.spy(ArrayList.class);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstOld);
    srVsmartBusiness.getSRReviewForVSmart("1");
  }

  @Test
  public void getListRoleUserForVsmart_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRRoleUserDTO srRoleUserDTO = Mockito.spy(SRRoleUserDTO.class);
    srVsmartBusiness.getListRoleUserForVsmart(srRoleUserDTO);
  }

  @Test
  public void getListRoleUserForVsmart_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRRoleUserDTO srRoleUserDTO = Mockito.spy(SRRoleUserDTO.class);
    srRoleUserDTO.setCountry("a");
    srVsmartBusiness.getListRoleUserForVsmart(srRoleUserDTO);
  }

  @Test
  public void getListRoleUserForVsmart_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRRoleUserDTO srRoleUserDTO = Mockito.spy(SRRoleUserDTO.class);
    srRoleUserDTO.setCountry("1");
    SrInsiteDTO locationSelect = Mockito.spy(SrInsiteDTO.class);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(locationSelect);
    srVsmartBusiness.getListRoleUserForVsmart(srRoleUserDTO);
  }

  @Test
  public void getListRoleUserForVsmart_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRRoleUserDTO srRoleUserDTO = Mockito.spy(SRRoleUserDTO.class);
    srRoleUserDTO.setCountry("1");
    SrInsiteDTO locationSelect = Mockito.spy(SrInsiteDTO.class);
    locationSelect.setCountry("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(lstLocation);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(locationSelect);
    srVsmartBusiness.getListRoleUserForVsmart(srRoleUserDTO);
  }

  @Test
  public void getListRoleUserForVsmart_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRRoleUserDTO srRoleUserDTO = Mockito.spy(SRRoleUserDTO.class);
    srRoleUserDTO.setCountry("1");
    SrInsiteDTO locationSelect = Mockito.spy(SrInsiteDTO.class);
    locationSelect.setCountry("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("2");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(lstLocation);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(locationSelect);
    srVsmartBusiness.getListRoleUserForVsmart(srRoleUserDTO);
  }

  @Test
  public void getListRoleUserForVsmart_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRRoleUserDTO srRoleUserDTO = Mockito.spy(SRRoleUserDTO.class);
    srRoleUserDTO.setCountry("1");
    srRoleUserDTO.setUnitId("a");
    SrInsiteDTO locationSelect = Mockito.spy(SrInsiteDTO.class);
    locationSelect.setCountry("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(lstLocation);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(locationSelect);
    srVsmartBusiness.getListRoleUserForVsmart(srRoleUserDTO);
  }

  @Test
  public void getListRoleUserForVsmart_07() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRRoleUserDTO srRoleUserDTO = Mockito.spy(SRRoleUserDTO.class);
    srRoleUserDTO.setCountry("1");
    srRoleUserDTO.setUnitId("1");
    SrInsiteDTO locationSelect = Mockito.spy(SrInsiteDTO.class);
    locationSelect.setCountry("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(lstLocation);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(locationSelect);
    srVsmartBusiness.getListRoleUserForVsmart(srRoleUserDTO);
  }

  @Test
  public void getListRoleUserForVsmart_08() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRRoleUserDTO srRoleUserDTO = Mockito.spy(SRRoleUserDTO.class);
    srRoleUserDTO.setCountry("1");
    srRoleUserDTO.setUnitId("1");
    SrInsiteDTO locationSelect = Mockito.spy(SrInsiteDTO.class);
    locationSelect.setCountry("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    unitExecute.setUnitId(1L);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(lstLocation);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(locationSelect);
    srVsmartBusiness.getListRoleUserForVsmart(srRoleUserDTO);
  }

  @Test
  public void getListRoleUserForVsmart_09() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRRoleUserDTO srRoleUserDTO = Mockito.spy(SRRoleUserDTO.class);
    srRoleUserDTO.setCountry("1");
    srRoleUserDTO.setUnitId("1");
    SrInsiteDTO locationSelect = Mockito.spy(SrInsiteDTO.class);
    locationSelect.setCountry("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    UnitDTO unitExecute = Mockito.spy(UnitDTO.class);
    unitExecute.setUnitId(1L);
    SRRoleUserInSideDTO userInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    List<SRRoleUserInSideDTO> lstRoleUser = Mockito.spy(ArrayList.class);
    lstRoleUser.add(userInSideDTO);
    PowerMockito.when(srCategoryServiceProxy.getlistSRRoleUserDTO(any())).thenReturn(lstRoleUser);
    PowerMockito.when(unitRepository.findUnitById(anyLong())).thenReturn(unitExecute);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString()))
        .thenReturn(lstLocation);
    PowerMockito.when(srRepository.findNationByLocationId(any())).thenReturn(locationSelect);
    srVsmartBusiness.getListRoleUserForVsmart(srRoleUserDTO);
  }

  @Test
  public void getListUnitSRCatalogForVsmart_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRCatalogDTO dto = Mockito.spy(SRCatalogDTO.class);
    srVsmartBusiness.getListUnitSRCatalogForVsmart(dto);
  }

  @Test
  public void getListUnitSRCatalogForVsmart_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRCatalogDTO dto = Mockito.spy(SRCatalogDTO.class);
    dto.setServiceCode("1");
    UnitSRCatalogDTO unitSRCatalogDTO = Mockito.spy(UnitSRCatalogDTO.class);
    List<UnitSRCatalogDTO> lstCatalog = Mockito.spy(ArrayList.class);
    PowerMockito.when(srCatalogRepository2.getListUnitSRCatalog(any())).thenReturn(lstCatalog);
    srVsmartBusiness.getListUnitSRCatalogForVsmart(dto);
  }

  @Test
  public void getListUnitSRCatalogForVsmart_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    SRCatalogDTO dto = Mockito.spy(SRCatalogDTO.class);
    dto.setServiceCode("1");
    UnitSRCatalogDTO unitSRCatalogDTO = Mockito.spy(UnitSRCatalogDTO.class);
    unitSRCatalogDTO.setServiceId("123");
    List<UnitSRCatalogDTO> lstCatalog = Mockito.spy(ArrayList.class);
    lstCatalog.add(unitSRCatalogDTO);
    PowerMockito.when(srCatalogRepository2.getListUnitSRCatalog(any())).thenReturn(lstCatalog);
    srVsmartBusiness.getListUnitSRCatalogForVsmart(dto);
  }
}
